// Package compile_test pins the set of `.t` fixtures that the Go
// Tom pipeline can drive all the way to a working `.class` file.
//
// For each fixture under tests/tom/compile/fixtures/:
//
//   1. Run the Go pipeline (cmd/tom equivalent) on the .t source.
//   2. javac the resulting .java.
//   3. java the resulting .class (optionally with JUnit on Test*.t).
//   4. Assert the runtime output matches the per-fixture
//      `expected.txt` (substring or full match — keep it simple).
//
// Skipped without JDK / aterm.jar. The fixtures live in this
// directory (copies, not symlinks) so the test is reproducible
// independent of the rest of /tom/.
package compile_test

import (
	"bytes"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/stable/tom"
	"tom/tomgo/stable/tom/backend"
	"tom/tomgo/stable/tom/compiler"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/typer"
)

// fixture describes one entry under fixtures/. Class is the public
// class name expected inside the generated .java (we infer it from
// the basename). JUnit pulls junit.jar in for Test*.t fixtures.
type fixture struct {
	name        string   // .t basename without extension; also the Java class name
	expected    []string // substrings that MUST appear in stdout (in order)
	useJUnit    bool     // include junit.jar/hamcrest.jar on the run classpath
	expectedFmt string   // optional human-readable expected blob; only used in error messages
}

var fixtures = []fixture{
	{
		name: "Peano",
		expected: []string{
			"First loop has successfully terminated.",
			"Second loop has successfully terminated.",
			"Peano test has successfully terminated.",
		},
	},
	{
		name:     "TestPeano",
		useJUnit: true,
		expected: []string{
			"JUnit version 4.10",
			// JUnit prints exactly N dots, one per passing test;
			// then "OK (N tests)" on the final summary line. The
			// number doesn't drift unless the source adds/removes
			// `@Test` methods.
			"OK (8 tests)",
		},
	},
}

func TestCompileFixtures(t *testing.T) {
	resources, ok := resolveResources(t)
	if !ok {
		return
	}
	for _, f := range fixtures {
		t.Run(f.name, func(t *testing.T) {
			workDir := t.TempDir()

			sourcePath := filepath.Join(resources.fixturesDir, f.name+".t")
			jarPath := filepath.Join(workDir, f.name+".java")
			if err := runGoPipeline(sourcePath, jarPath); err != nil {
				t.Fatalf("pipeline failed: %v", err)
			}
			if err := javac(resources, workDir, jarPath, f.useJUnit); err != nil {
				t.Fatalf("javac failed: %v", err)
			}
			out, err := javaRun(resources, workDir, f.name, f.useJUnit)
			if err != nil {
				t.Fatalf("java run failed: %v\nstdout=%s", err, out)
			}
			for _, want := range f.expected {
				if !strings.Contains(out, want) {
					t.Errorf("expected substring %q not in stdout:\n%s", want, out)
				}
			}
		})
	}
}

// runGoPipeline runs the same chain cmd/tom uses, then writes the
// generated Java source to `dst`.
func runGoPipeline(src, dst string) error {
	state := tom.State{Filename: src}
	state, err := tom.Run(state,
		starter.Run,
		parser.Run,
		transformer.Run,
		syntaxchecker.Run,
		desugarer.Run,
		typer.Run,
		compiler.Run,
		backend.Run,
	)
	if err != nil {
		return err
	}
	return os.WriteFile(dst, state.Source, 0o644)
}

// resources captures the absolute paths the test needs: the jars
// directory, the (optional) junit/hamcrest jars, and the fixture
// directory inside the package.
type resources struct {
	javaHome    string
	jars        []string // runtime + tool jars (aterm, etc.)
	junitJar    string
	hamcrestJar string
	fixturesDir string
}

// resolveResources locates everything needed. Returns ok=false (with
// t.Skip already issued) when any prerequisite is missing.
func resolveResources(t *testing.T) (resources, bool) {
	t.Helper()
	var r resources
	r.javaHome = findJavaHome()
	if r.javaHome == "" {
		t.Skip("javac/java not found in PATH; install a JDK to run the compile tests")
		return r, false
	}
	// Find aterm.jar — the runtime dependency. We use the repo's
	// stable/dist/lib/runtime/ tree; that's enough for both Peano
	// (ATerm) and TestPeano (ATerm + JUnit).
	cwd, _ := os.Getwd()
	repoRoot := walkUpFor(cwd, "tomgo/go.mod")
	if repoRoot == "" {
		t.Skip("repo root not found above " + cwd)
		return r, false
	}
	distLib := filepath.Join(repoRoot, "stable", "dist", "lib")
	jars, err := collectJars(filepath.Join(distLib, "runtime"))
	if err != nil || len(jars) == 0 {
		t.Skipf("aterm runtime jars missing under %s", distLib)
		return r, false
	}
	r.jars = jars
	r.junitJar = filepath.Join(distLib, "compiletime", "junit.jar")
	// hamcrest lives in lab/ historically — match what cmd/tom users
	// already do at the shell. If absent, JUnit fixtures will still
	// run their assertions (hamcrest is only needed for richer
	// matchers we don't use).
	for _, p := range []string{
		filepath.Join(repoRoot, "lab", "enumerator", "lib", "hamcrest-all-1.3.jar"),
		filepath.Join(repoRoot, "stable", "lib", "tools", "hamcrest.jar"),
	} {
		if _, err := os.Stat(p); err == nil {
			r.hamcrestJar = p
			break
		}
	}
	r.fixturesDir = filepath.Join(cwd, "fixtures")
	return r, true
}

func javac(r resources, workDir, src string, useJUnit bool) error {
	cp := buildClasspath(r, useJUnit)
	cmd := exec.Command(filepath.Join(r.javaHome, "bin", "javac"),
		"-cp", cp,
		"-d", workDir,
		src,
	)
	var stderr bytes.Buffer
	cmd.Stderr = &stderr
	if err := cmd.Run(); err != nil {
		return &execError{err: err, stderr: stderr.String()}
	}
	return nil
}

func javaRun(r resources, workDir, className string, useJUnit bool) (string, error) {
	cp := workDir + string(os.PathListSeparator) + buildClasspath(r, useJUnit)
	cmd := exec.Command(filepath.Join(r.javaHome, "bin", "java"), "-cp", cp, className)
	var stdout, stderr bytes.Buffer
	cmd.Stdout = &stdout
	cmd.Stderr = &stderr
	if err := cmd.Run(); err != nil {
		// JUnit fixtures may print to stderr on failure; keep that
		// visible to the caller.
		combined := stdout.String() + stderr.String()
		return combined, &execError{err: err, stderr: stderr.String()}
	}
	return stdout.String(), nil
}

func buildClasspath(r resources, useJUnit bool) string {
	parts := append([]string{}, r.jars...)
	if useJUnit {
		if r.junitJar != "" {
			parts = append(parts, r.junitJar)
		}
		if r.hamcrestJar != "" {
			parts = append(parts, r.hamcrestJar)
		}
	}
	return strings.Join(parts, string(os.PathListSeparator))
}

type execError struct {
	err    error
	stderr string
}

func (e *execError) Error() string {
	if e.stderr == "" {
		return e.err.Error()
	}
	return e.err.Error() + ": " + e.stderr
}

// findJavaHome resolves JAVA_HOME, falling back to a few common
// install locations on macOS. Returns "" if no usable JDK is found.
func findJavaHome() string {
	if v := os.Getenv("JAVA_HOME"); v != "" {
		if _, err := os.Stat(filepath.Join(v, "bin", "javac")); err == nil {
			return v
		}
	}
	candidates := []string{
		"/Library/Java/JavaVirtualMachines/zulu-8.jdk/Contents/Home",
		"/opt/homebrew/opt/openjdk",
	}
	for _, c := range candidates {
		if _, err := os.Stat(filepath.Join(c, "bin", "javac")); err == nil {
			return c
		}
	}
	if p, err := exec.LookPath("javac"); err == nil {
		// javac is in PATH — derive a JAVA_HOME-ish dir.
		bin := filepath.Dir(p)
		return filepath.Dir(bin)
	}
	return ""
}

func walkUpFor(start, marker string) string {
	cur := start
	for {
		if _, err := os.Stat(filepath.Join(cur, marker)); err == nil {
			return cur
		}
		parent := filepath.Dir(cur)
		if parent == cur {
			return ""
		}
		cur = parent
	}
}

func collectJars(dir string) ([]string, error) {
	entries, err := os.ReadDir(dir)
	if err != nil {
		return nil, err
	}
	var out []string
	for _, e := range entries {
		if !e.IsDir() && strings.HasSuffix(e.Name(), ".jar") {
			out = append(out, filepath.Join(dir, e.Name()))
		}
	}
	return out, nil
}
