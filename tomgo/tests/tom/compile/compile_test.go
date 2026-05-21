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
	"tom/tomgo/stable/tom/backend_java"
	"tom/tomgo/stable/tom/backend_go"
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

	// needsMapping toggles passing $TOMGO_TOM_INCLUDE to the Go
	// pipeline so `%include { sl.tom }` and friends resolve to the
	// local `stable/tom/library/mapping/` tree.
	needsMapping bool
	// hasInlineGom signals the fixture uses a `%gom { module ... }`
	// block; the parser then writes Java AST classes to a `gen/`
	// subdirectory next to the source, which must be compiled into
	// the work dir alongside the main `.java` before running.
	hasInlineGom bool
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
	{
		// Array.t — variadic %oparray L conc(E*) patterns with
		// X*/x/X*/y/X* decompositions. main() calls testArray1()
		// which runs assertTrue(sort1(l).equals(res)) and
		// assertTrue(sort2(l).equals(res)). Both assertions throw
		// "assertion failed." (a RuntimeException) when they
		// fail — exit 0 with no stdout means both succeeded.
		name:     "Array",
		expected: []string{},
	},
	{
		// TestArray.t — JUnit-driven extension of Array exercising
		// non-linear patterns (`conc(X1*, x, X2*, x, X3*)`) which
		// triggers GeneralPurposePropagator's duplicate-variable
		// merge (fresh var + EqualBQTerm test) on the second `x`.
		name:     "TestArray",
		useJUnit: true,
		expected: []string{
			"JUnit version 4.10",
			"OK (7 tests)",
		},
	},
	{
		// TestList.t — JUnit-driven %oplist L conc(E*) suite. Uses
		// head/tail/is_empty hooks (vs Array's get_element/get_size)
		// — exercises VariadicPropagator / VariadicGenerator and the
		// list-shape Backend helpers (tom_get_head, tom_get_tail,
		// tom_is_empty, tom_append_list, tom_get_slice).
		name:     "TestList",
		useJUnit: true,
		expected: []string{
			"JUnit version 4.10",
			"OK (34 tests)",
		},
	},
	{
		// TestBackQuote.t — inline %gom (`L = conc(E*); E = a()|b()|c()|d()`),
		// `%match` with multi-star pattern `conc(x*, b(), y*, a(), z*)`
		// and reconstruction backquote `\`conc(x*, b(), y*, a(), z*)`,
		// plus the splice case `\`conc(abc*())` where `abc()` is a
		// private static method returning an L that gets spliced into
		// the outer conc's arg list. Exercises:
		//   - star-var splicing in BQAppl args (not just patterns);
		//   - `%op L abc() {}` — operator declared with no hooks
		//     (just a signature) so the splice path can resolve;
		//   - inline %gom expansion + JUnit assertEquals.
		name:         "TestBackQuote",
		useJUnit:     true,
		needsMapping: true,
		hasInlineGom: true,
		expected: []string{
			"JUnit version 4.10",
			"OK (2 tests)",
		},
	},
	{
		// TestAlgebraic.t — uses %strategy + %gom + %include of sl.
		// Exercises:
		//   - inline gom expansion (generates testalgebraic.sig
		//     classes under fixtures/gen/)
		//   - %include resolution via $TOMGO_TOM_INCLUDE
		//   - backquote expansion inside `%op` `make` hooks (sl's
		//     strategy combinators)
		//   - `%strategy R()` class generation + tom_make_R helper
		//   - typer's BQAppl→BuildTerm/BuildConsList rewriting
		name:         "TestAlgebraic",
		useJUnit:     true,
		needsMapping: true,
		hasInlineGom: true,
		expected: []string{
			"JUnit version 4.10",
			"OK (1 test)",
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

			// Per-fixture environment: fixtures using %include of
			// `sl.tom` (e.g. TestAlgebraic) need TOMGO_TOM_INCLUDE
			// set so `expandGomBlock` and the parser's include
			// resolver find the mapping/ tree.
			if f.needsMapping {
				mapDir := filepath.Join(resources.repoRoot, "stable", "tom", "library", "mapping")
				prev := os.Getenv("TOMGO_TOM_INCLUDE")
				t.Setenv("TOMGO_TOM_INCLUDE",
					filepath.Join(mapDir, "java")+string(os.PathListSeparator)+mapDir)
				defer os.Setenv("TOMGO_TOM_INCLUDE", prev)
			}

			sourcePath := filepath.Join(resources.fixturesDir, "java", f.name+".t")
			jarPath := filepath.Join(workDir, f.name+".java")
			if err := runGoPipeline(sourcePath, jarPath); err != nil {
				t.Fatalf("pipeline failed: %v", err)
			}

			// Fixtures with inline %gom drop generated AST classes
			// under fixtures/java/gen/. Compile those into the work
			// dir before the main .java so its imports resolve.
			if f.hasInlineGom {
				genDir := filepath.Join(resources.fixturesDir, "java", "gen")
				if err := javacGen(resources, workDir, genDir, f.useJUnit); err != nil {
					t.Fatalf("javac gen/ failed: %v", err)
				}
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

// goFixtures lists the Tom→Go fixtures. Each maps to a `.t` source
// under `fixtures/go/`. When `useGoTest` is true the harness wraps
// the emitted file as `<name>_test.go`, drops a tiny `go.mod`
// alongside it, and runs `go test -v .`; otherwise it runs `go run`
// on the file directly (single-file mode).
var goFixtures = []struct {
	name         string
	expected     []string
	useGoTest    bool
	needsMapping bool // set TOMGO_TOM_INCLUDE before running the pipeline
	useSlLib     bool // require tom/tomgo + replace in the generated go.mod
}{
	{
		name: "Peano",
		expected: []string{
			"First loop has successfully terminated.",
			"Peano test has successfully terminated.",
		},
	},
	{
		name:      "TestPeano",
		useGoTest: true,
		expected: []string{
			"=== RUN   TestPlus1",
			"=== RUN   TestPlus2",
			"=== RUN   TestFib3",
			"=== RUN   TestInt2p",
			"=== RUN   TestPeano2int",
			"PASS",
		},
	},
	{
		// Array.t — `%oparray L conc(E*)` with variadic patterns
		// (`conc(X1*, x, X2*, y, X3*)`) + non-linear pattern
		// (double1's `conc(X1*, x, X2*, x, X3*)`). Exercises the
		// Go backend's array IR lowering: tom_empty_array_conc,
		// tom_cons_array_conc, tom_get_element_conc_L,
		// tom_get_size_conc_L, plus generic tom_get_slice_conc /
		// tom_append_array_conc, and the do/while loop as a Go
		// `for { body; if !cond { break } }`.
		name: "Array",
		expected: []string{
			"Array test has successfully terminated.",
		},
	},
	{
		// TestArray.t — JUnit-style 7-test suite ported to Go's
		// testing package. Exercises sort1/sort2, double1/2/4/5
		// (incl. `x@a()` alias + 2-rule pattern), and a
		// VariableStar split-count test (`conc(X1*, X2*, X3*)`
		// on [a, b] → 6 ways).
		name:      "TestArray",
		useGoTest: true,
		expected: []string{
			"=== RUN   TestSort1",
			"=== RUN   TestSort2",
			"=== RUN   TestDouble1",
			"=== RUN   TestDouble5",
			"=== RUN   TestVariableStar1",
			"PASS",
		},
	},
	{
		// TestList.t — `%oplist L conc(E*)` with head/tail/is_empty
		// hooks (slice-based: l[0], l[1:], len(l)==0). Exercises
		// VariadicPropagator / VariadicGenerator and the Go
		// backend's list IR shapes (tom_get_head, tom_get_tail,
		// tom_is_empty, tom_get_slice_list).
		name:      "TestList",
		useGoTest: true,
		expected: []string{
			"=== RUN   TestEmpty",
			"=== RUN   TestSingletonFA",
			"=== RUN   TestEndsFA",
			"=== RUN   TestFAthenFB",
			"=== RUN   TestStartsFBthenFB",
			"=== RUN   TestFirstHead",
			"=== RUN   TestCount",
			"PASS",
		},
	},
	{
		// TestBackQuote.t — Go counterpart of fixtures/java/TestBackQuote.t.
		// Exercises:
		//   - star-vars in BQAppl args (`\`conc(x*, b(), y*, a(), z*)`),
		//     not just in %match patterns;
		//   - `%op L abc() {}` — declared with no hooks, the Go func
		//     `abc()` provides the implementation;
		//   - the same %match pattern with two star-vars in non-
		//     adjacent positions (`x*`, `y*`, `z*` separated by
		//     anchors `b()` and `a()`).
		name:      "TestBackQuote",
		useGoTest: true,
		expected: []string{
			"=== RUN   TestRoundTrip",
			"=== RUN   TestAbcDirect",
			"PASS",
		},
	},
	{
		// TestAlgebraic.t — first Go fixture using %strategy. Includes
		// sl.tom from the Go mapping (Strategy / mu / MuVar / All /
		// Sequence / SequenceId + composed RepeatId / BottomUp), and
		// imports the runtime sl package for the introspector +
		// strategy interface. Exercises:
		//   - StrategyDeclaration emission in backend_go (R struct +
		//     tom_make_R factory + visit_T1 / _visit_T1 methods);
		//   - the transitive-closure walk over SymbolTable make
		//     hook bodies so `RepeatId`'s body pulls in `mu`/`MuVar`/
		//     `SequenceId`'s helpers;
		//   - the strategy-symbol collect pass so the visit body's
		//     `b()` action emits `tom_make_b` even though it never
		//     appears outside the strategy.
		name:         "TestAlgebraic",
		useGoTest:    true,
		needsMapping: true,
		useSlLib:     true,
		expected: []string{
			"=== RUN   TestAlgebraic",
			"--- PASS: TestAlgebraic",
			"PASS",
		},
	},
}

// TestCompileGoFixtures is the Go-target counterpart of
// TestCompileFixtures. Each fixture is the same Tom source format
// (`%typeterm`, `%op`, `%match`, …) but the host syntax inside TL
// chunks is Go, and the Tom pipeline finishes with backend_go.Run.
func TestCompileGoFixtures(t *testing.T) {
	resources, ok := resolveResources(t)
	if !ok {
		return
	}
	goBin, err := exec.LookPath("go")
	if err != nil {
		t.Skipf("`go` binary not found in PATH: %v", err)
	}
	for _, f := range goFixtures {
		t.Run(f.name, func(t *testing.T) {
			workDir := t.TempDir()
			sourcePath := filepath.Join(resources.fixturesDir, "go", f.name+".t")

			// Mapping include path: fixtures using `%include { sl.tom }`
			// (e.g. TestAlgebraic) need TOMGO_TOM_INCLUDE pointed at
			// the Go mapping tree before the parser resolves imports.
			if f.needsMapping {
				mapDir := filepath.Join(resources.repoRoot, "stable", "tom", "library", "mapping")
				t.Setenv("TOMGO_TOM_INCLUDE",
					filepath.Join(mapDir, "go")+string(os.PathListSeparator)+mapDir)
			}

			// Test files must end in `_test.go` for `go test` to
			// pick them up. `go run` can use any name.
			fname := f.name + ".go"
			if f.useGoTest {
				fname = f.name + "_test.go"
			}
			goPath := filepath.Join(workDir, fname)
			if err := runGoPipelineToGo(sourcePath, goPath); err != nil {
				t.Fatalf("pipeline failed: %v", err)
			}

			var cmd *exec.Cmd
			if f.useGoTest {
				// `go test` needs a module — drop a tiny go.mod
				// alongside the generated _test.go. Fixtures
				// importing `tom/tomgo/stable/library/sl` need a
				// `replace` line pointing at the repo root.
				modBody := "module peano_test_fixture\n\ngo 1.22\n"
				if f.useSlLib {
					modBody += "\nrequire tom/tomgo v0.0.0\n"
					modBody += "replace tom/tomgo => " + resources.repoRoot + "\n"
				}
				if err := os.WriteFile(filepath.Join(workDir, "go.mod"), []byte(modBody), 0o644); err != nil {
					t.Fatalf("write go.mod: %v", err)
				}
				if f.useSlLib {
					// `go test` needs a tidy module before it runs;
					// otherwise it complains about the missing
					// require entry even with the replace in place.
					tidy := exec.Command(goBin, "mod", "tidy")
					tidy.Dir = workDir
					var tidyErr bytes.Buffer
					tidy.Stderr = &tidyErr
					if err := tidy.Run(); err != nil {
						t.Fatalf("go mod tidy failed: %v\nstderr=%s", err, tidyErr.String())
					}
				}
				cmd = exec.Command(goBin, "test", "-v", ".")
			} else {
				cmd = exec.Command(goBin, "run", goPath)
			}
			cmd.Dir = workDir
			var stdout, stderr bytes.Buffer
			cmd.Stdout = &stdout
			cmd.Stderr = &stderr
			if err := cmd.Run(); err != nil {
				t.Fatalf("go failed: %v\nstderr=%s\nstdout=%s\nsource=%s",
					err, stderr.String(), stdout.String(), goPath)
			}
			out := stdout.String() + stderr.String()
			for _, want := range f.expected {
				if !strings.Contains(out, want) {
					t.Errorf("expected substring %q not in output:\n%s", want, out)
				}
			}
		})
	}
}

// javacGen compiles every .java file under `genDir` into `workDir`.
// Used by inline-%gom fixtures (TestAlgebraic) whose AST classes
// must be on the classpath before the main fixture compiles.
func javacGen(r resources, workDir, genDir string, useJUnit bool) error {
	var sources []string
	err := filepath.Walk(genDir, func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && strings.HasSuffix(p, ".java") {
			sources = append(sources, p)
		}
		return nil
	})
	if err != nil || len(sources) == 0 {
		return nil
	}
	cp := buildClasspath(r, useJUnit)
	args := append([]string{"-cp", cp, "-d", workDir}, sources...)
	cmd := exec.Command(filepath.Join(r.javaHome, "bin", "javac"), args...)
	var stderr bytes.Buffer
	cmd.Stderr = &stderr
	if err := cmd.Run(); err != nil {
		return &execError{err: err, stderr: stderr.String()}
	}
	return nil
}

// runGoPipeline runs the same chain cmd/tom uses, then writes the
// generated Java source to `dst`. Used by the Java-target tests.
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
		backend_java.Run,
	)
	if err != nil {
		return err
	}
	return os.WriteFile(dst, state.Source, 0o644)
}

// runGoPipelineToGo runs the same upstream phases as runGoPipeline
// but uses the Go-target backend at the end. Used by the Go-target
// fixture tests under `fixtures/go/`.
func runGoPipelineToGo(src, dst string) error {
	state := tom.State{Filename: src, Target: "go"}
	state, err := tom.Run(state,
		starter.Run,
		parser.Run,
		transformer.Run,
		syntaxchecker.Run,
		desugarer.Run,
		typer.Run,
		compiler.Run,
		backend_go.Run,
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
	repoRoot    string // absolute path to tomgo's parent (for include paths)
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
	r.repoRoot = filepath.Join(repoRoot, "tomgo")
	return r, true
}

func javac(r resources, workDir, src string, useJUnit bool) error {
	// Prepend workDir to the classpath so a fixture's own previously-
	// emitted classes (e.g. AST classes from inline-gom under gen/
	// compiled by javacGen) resolve here.
	cp := workDir + string(os.PathListSeparator) + buildClasspath(r, useJUnit)
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
