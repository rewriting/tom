// Package tomparseq drives a byte-for-byte equivalence check between the
// reference Java TOM parser (stable/dist/lib/, antlr4 newparser `-np`) and a
// candidate Go AST. It is the harness for Phase 4.C of the TOM → Go port:
// once the hand-rolled Go parser exists (Phase 4.D), the candidate AST will
// be produced by parsing the same .t file in Go; until then, tests feed it a
// manually-constructed tomast.Code and verify the format is in lockstep.
//
// The Java side runs `tomgo/internal/tomparseq/java/TomParseDump.java`, a tiny
// driver that instantiates TomParserPlugin directly (no Tom.java, no Tom.config
// — those would drag in every other plugin and trigger the aterm.jar JDK11+
// serialization bug). It prints `code.toString()` to stdout, the Gom-canonical
// Op(arg1,arg2,…) format, which tomast.*.String() reproduces verbatim on the
// Go side.
//
// Path-dependent fields (OriginTracking carries the absolute input path) are
// normalised to a placeholder "__INPUT__" before comparison.
package tomparseq

import (
	"errors"
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
)

// JavaToolchain captures the paths needed to invoke the Java parser dump.
type JavaToolchain struct {
	Java        string   // absolute path to `java`
	Javac       string   // absolute path to `javac`
	Jars        []string // every jar from stable/dist/lib/ that's needed at runtime
	RunnerSrc   string   // path to TomParseDump.java
	RunnerClass string   // directory where TomParseDump.class lives after compile
}

// SkipIfNoJava marks the test as skipped (not failed) when the JDK or the
// reference stable/dist/lib are missing. Mirrors equivtest.SkipIfNoJava so
// CI lanes without Java continue to pass.
func SkipIfNoJava(t interface{ Skip(args ...any) }, err error) {
	t.Skip("tomparseq: prerequisites missing — " + err.Error())
}

// Resolve locates the JDK and reference jars. repoRoot points at the top of
// the TOM repository (the one containing stable/, applications/, tomgo/).
// Returns an error explaining what is missing when resolution fails.
func Resolve(repoRoot string) (JavaToolchain, error) {
	tc := JavaToolchain{}
	java, javac, err := findJavaTools()
	if err != nil {
		return tc, err
	}
	tc.Java, tc.Javac = java, javac

	distLib := os.Getenv("TOMGO_STABLE_DIST_LIB")
	if distLib == "" {
		distLib = filepath.Join(repoRoot, "stable", "dist", "lib")
	}
	if _, err := os.Stat(distLib); err != nil {
		return tc, fmt.Errorf("stable/dist/lib missing at %s (run `./build.sh stable` or set $TOMGO_STABLE_DIST_LIB)", distLib)
	}
	jars, err := collectJars(distLib)
	if err != nil {
		return tc, err
	}
	if len(jars) == 0 {
		return tc, fmt.Errorf("no jars under %s", distLib)
	}
	tc.Jars = jars

	runnerSrc := filepath.Join(repoRoot, "tomgo", "stable", "tom", "parser", "equiv", "java", "TomParseDump.java")
	if _, err := os.Stat(runnerSrc); err != nil {
		return tc, fmt.Errorf("TomParseDump.java not found at %s", runnerSrc)
	}
	tc.RunnerSrc = runnerSrc
	tc.RunnerClass = filepath.Dir(runnerSrc)
	return tc, nil
}

// EnsureCompiled compiles TomParseDump.java if its .class is missing or older
// than the source. Idempotent — safe to call from every test.
func (tc JavaToolchain) EnsureCompiled() error {
	classFile := filepath.Join(tc.RunnerClass, "TomParseDump.class")
	srcInfo, err := os.Stat(tc.RunnerSrc)
	if err != nil {
		return err
	}
	if classInfo, err := os.Stat(classFile); err == nil && !classInfo.ModTime().Before(srcInfo.ModTime()) {
		return nil
	}
	cp := strings.Join(tc.Jars, string(os.PathListSeparator))
	out, err := exec.Command(tc.Javac, "-d", tc.RunnerClass, "-cp", cp, tc.RunnerSrc).CombinedOutput()
	if err != nil {
		return fmt.Errorf("javac TomParseDump.java failed: %w\n%s", err, out)
	}
	return nil
}

// DumpJava runs TomParseDump on a .t file and returns its stdout (the
// Gom-canonical Code.toString()) after applying the standard normalisation
// (see normalizeAST).
func (tc JavaToolchain) DumpJava(inputFile string) (string, error) {
	if err := tc.EnsureCompiled(); err != nil {
		return "", err
	}
	absInput, err := filepath.Abs(inputFile)
	if err != nil {
		return "", err
	}
	cp := strings.Join(append([]string{tc.RunnerClass}, tc.Jars...), string(os.PathListSeparator))
	cmd := exec.Command(tc.Java, "-cp", cp, "TomParseDump", absInput)
	var stdout strings.Builder
	cmd.Stdout = &stdout
	cmd.Stderr = os.Stderr // surface parser diagnostics during test runs
	if err := cmd.Run(); err != nil {
		return "", fmt.Errorf("TomParseDump on %s failed: %w", inputFile, err)
	}
	// The parser plugin's logger prints "antlr4: <path> parsing + building ..."
	// to stdout before the AST. Strip the chatter and normalise paths.
	out := stripParserChatter(stdout.String())
	return normalizeAST(out, absInput), nil
}

// normalizeAST is the path-rewrite applied to *both* the Java and Go sides
// before byte-comparison. Two substitutions, in order:
//
//	absInput              → __INPUT__   (the main .t file)
//	dirname(absInput)     → __DIR__     (its containing directory, used for
//	                                     any %include'd files alongside it)
//
// Order matters: replacing the directory first would also rewrite absInput
// (since absInput starts with its own directory).
func normalizeAST(s, absInput string) string {
	s = strings.ReplaceAll(s, absInput, "__INPUT__")
	s = strings.ReplaceAll(s, filepath.Dir(absInput), "__DIR__")
	return s
}

// stripParserChatter removes the "antlr4: ..." progress line printed by
// TomParserPlugin (it goes to stdout, sigh) so only the AST term remains.
func stripParserChatter(s string) string {
	// The progress line ends with a newline before the AST starts.
	idx := strings.Index(s, "\nTom(")
	if idx >= 0 {
		return s[idx+1:]
	}
	// Fallback: if there's no leading chatter, return as-is.
	return s
}

// AssertParityWithGo compares the Java reference dump against a Go-built AST
// string, returning nil on byte-equality and a unified-diff error otherwise.
// The Go side is normalised the same way the Java side is (see normalizeAST).
// Callers may safely pass either an unnormalised AST (containing absInput and
// its directory) or a pre-normalised one (containing only __INPUT__/__DIR__
// placeholders) — the substitutions are idempotent on the placeholders.
func (tc JavaToolchain) AssertParityWithGo(inputFile, goSide string) error {
	javaSide, err := tc.DumpJava(inputFile)
	if err != nil {
		return err
	}
	absInput, err := filepath.Abs(inputFile)
	if err != nil {
		return err
	}
	goSide = normalizeAST(goSide, absInput)
	if javaSide == goSide {
		return nil
	}
	return fmt.Errorf("AST mismatch on %s\n--- java ---\n%s\n--- go ---\n%s\n",
		inputFile, javaSide, goSide)
}

// findJavaTools mirrors equivtest.findJavaTools but is duplicated rather than
// imported so this package stays free of the equivtest-specific Resolve.
func findJavaTools() (string, string, error) {
	if h := os.Getenv("JAVA_HOME"); h != "" {
		j := filepath.Join(h, "bin", "java")
		jc := filepath.Join(h, "bin", "javac")
		if _, err := os.Stat(j); err == nil {
			return j, jc, nil
		}
	}
	if _, err := os.Stat("/opt/homebrew/opt/openjdk/bin/javac"); err == nil {
		return "/opt/homebrew/opt/openjdk/bin/java", "/opt/homebrew/opt/openjdk/bin/javac", nil
	}
	j, errJ := exec.LookPath("java")
	jc, errC := exec.LookPath("javac")
	if errJ == nil && errC == nil {
		return j, jc, nil
	}
	return "", "", errors.New("no JDK found (set JAVA_HOME, install /opt/homebrew/opt/openjdk, or put java+javac on PATH)")
}

// collectJars walks distLib and gathers every .jar except those under
// compiletime/ (Ant + JUnit — not needed at runtime).
func collectJars(distLib string) ([]string, error) {
	var out []string
	err := filepath.Walk(distLib, func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() {
			return nil
		}
		if !strings.HasSuffix(p, ".jar") {
			return nil
		}
		if strings.Contains(p, string(os.PathSeparator)+"compiletime"+string(os.PathSeparator)) {
			return nil
		}
		out = append(out, p)
		return nil
	})
	return out, err
}

// FindRepoRoot walks up from a starting directory until it finds the marker
// file `tomgo/go.mod`. Used by tests so they can locate the repo regardless of
// the current working directory.
func FindRepoRoot(start string) (string, error) {
	cur := start
	for {
		if _, err := os.Stat(filepath.Join(cur, "tomgo", "go.mod")); err == nil {
			return cur, nil
		}
		parent := filepath.Dir(cur)
		if parent == cur {
			return "", errors.New("repo root not found above " + start)
		}
		cur = parent
	}
}
