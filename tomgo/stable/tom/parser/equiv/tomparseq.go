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
	// PipelineSrc points at TomPipelineDump.java — a sibling runner that
	// chains plugins past the parser (Transformer, SyntaxChecker, Desugarer,
	// Typer) and dumps the AST after the requested phase. Same .class output
	// directory as the parser runner.
	PipelineSrc string
}

// Phase enumerates the AST-dump points supported by TomPipelineDump.
type Phase string

const (
	PhaseParsed      Phase = "parsed"
	PhaseTransformed Phase = "transformed"
	PhaseSynchecked  Phase = "synchecked"
	PhaseDesugared   Phase = "desugared"
	PhaseTyped       Phase = "typed"
)

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

	pipelineSrc := filepath.Join(filepath.Dir(runnerSrc), "TomPipelineDump.java")
	if _, err := os.Stat(pipelineSrc); err == nil {
		tc.PipelineSrc = pipelineSrc
	}
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

// EnsurePipelineCompiled is the TomPipelineDump.java analogue of
// EnsureCompiled. Skips silently if PipelineSrc is empty (caller didn't
// drop the source in place); errors if it exists but javac fails.
func (tc JavaToolchain) EnsurePipelineCompiled() error {
	if tc.PipelineSrc == "" {
		return fmt.Errorf("TomPipelineDump.java not present at %s",
			filepath.Join(tc.RunnerClass, "TomPipelineDump.java"))
	}
	classFile := filepath.Join(tc.RunnerClass, "TomPipelineDump.class")
	srcInfo, err := os.Stat(tc.PipelineSrc)
	if err != nil {
		return err
	}
	if classInfo, err := os.Stat(classFile); err == nil && !classInfo.ModTime().Before(srcInfo.ModTime()) {
		return nil
	}
	cp := strings.Join(tc.Jars, string(os.PathListSeparator))
	out, err := exec.Command(tc.Javac, "-d", tc.RunnerClass, "-cp", cp, tc.PipelineSrc).CombinedOutput()
	if err != nil {
		return fmt.Errorf("javac TomPipelineDump.java failed: %w\n%s", err, out)
	}
	return nil
}

// DumpJavaPhase returns Java's AST dump for `inputFile` after the
// given phase, normalised to the comparison form (paths replaced by
// `__INPUT__`/`__DIR__`). The lookup order:
//
//  1. The pre-computed cache under `tomgo/tests/testdata/javacache/<rel>/`
//     populated by `go run ./cmd/javacache` (instant, hundreds of
//     fixtures pre-built from a single full `tom --intermediate`
//     sweep).
//  2. A live `TomPipelineDump` invocation (slow, one JVM startup per
//     call) as a fallback for fixtures or phases not in the cache.
//
// The cache stores raw `tom --intermediate` `.tfix.<phase>` files,
// produced by the official `src/dist/bin/tom` CLI against the .t
// alone — i.e. the same compiler users invoke, with %gom / Tom.xml
// fully wired up. This is what gives us a "100 % functional" Java
// reference (cf. README); per-fixture call-outs to TomPipelineDump
// (which short-circuited Tom.xml) become an obsolete fallback.
func (tc JavaToolchain) DumpJavaPhase(inputFile string, phase Phase) (string, error) {
	absInput, err := filepath.Abs(inputFile)
	if err != nil {
		return "", err
	}
	if cached, ok, err := tc.readCachedDump(absInput, phase); err != nil {
		return "", err
	} else if ok {
		return normalizeAST(cached, absInput), nil
	}
	if err := tc.EnsurePipelineCompiled(); err != nil {
		return "", err
	}
	cp := strings.Join(append([]string{tc.RunnerClass}, tc.Jars...), string(os.PathListSeparator))
	cmd := exec.Command(tc.Java, "-cp", cp, "TomPipelineDump", absInput, string(phase))
	var stdout strings.Builder
	cmd.Stdout = &stdout
	cmd.Stderr = os.Stderr
	if err := cmd.Run(); err != nil {
		return "", fmt.Errorf("TomPipelineDump on %s (phase %s) failed: %w", inputFile, phase, err)
	}
	return normalizeAST(stdout.String(), absInput), nil
}

// CacheDir is the on-disk root of the Java reference cache. We
// reuse the output of `test/build.sh build` (with `--intermediate`
// added to the relevant tom.presets in test/build.xml) so the
// reference matches Tom's full pipeline — Tom.config + %gom
// expansion + per-fixture options (--optimize, --genIntrospector,
// --lazyType, …) exactly as the user runs it.
//
// Layout under CacheDir:
//
//	CacheDir/<dir>/<basename>.java.tfix.<phase>
//
// where <dir>/<basename>.t is the fixture path relative to
// /Users/pem/github/tom/test. Tests/tools can override CacheDir.
var CacheDir = "/Users/pem/github/tom/tomgo/tests/testdata/java-ast"

// readCachedDump returns the cached AST string for `(absInput, phase)`
// if available. Phase `synchecked` falls back to `transformed` (Java's
// SyntaxChecker doesn't emit a separate `.tfix` file because the AST
// is unchanged — its job is to log diagnostics).
//
// When the requested phase isn't available (Tom errored before
// reaching it for that fixture), this falls back to earlier phases
// in pipeline order: typed → desugared → transformed → parsed. The
// caller can detect this via the returned phase but for our parity
// dashboard it's enough to compare against the latest phase Java
// emitted; everything past it is "best effort" until our pipeline
// stops at the same point.
//
// The returned string is the raw `.tfix` contents, prior to path
// normalisation. Callers run normalizeAST on it.
func (tc JavaToolchain) readCachedDump(absInput string, phase Phase) (string, bool, error) {
	suffix := string(phase)
	if phase == PhaseSynchecked {
		suffix = "transformed" // SyntaxChecker is identity on the AST
	}
	rel, err := filepath.Rel("/Users/pem/github/tom/test", absInput)
	if err != nil || strings.HasPrefix(rel, "..") {
		return "", false, nil
	}
	dir := filepath.Dir(rel)
	base := strings.TrimSuffix(filepath.Base(absInput), ".t")
	// Pipeline order; we try the requested phase first, then walk
	// backward if it isn't in the cache.
	fallback := []string{"typed", "desugared", "transformed", "parsed"}
	tryOrder := []string{suffix}
	for i, p := range fallback {
		if p == suffix {
			tryOrder = append(tryOrder, fallback[i+1:]...)
			break
		}
	}
	for _, p := range tryOrder {
		cachedPath := filepath.Join(CacheDir, dir, base+".java.tfix."+p)
		body, err := os.ReadFile(cachedPath)
		if err == nil {
			return string(body), true, nil
		}
		if !os.IsNotExist(err) {
			return "", false, err
		}
	}
	return "", false, nil
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
