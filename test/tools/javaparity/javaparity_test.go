// Package javaparity is T0: the extended compiler must produce, for the
// default Java target, exactly what the human-written compiler in legacy/
// produces on every test/java/ input.
package javaparity

import (
	"os"
	"path/filepath"
	"regexp"
	"strings"
	"testing"

	"tomtools/harness"
)

// timing matches the "123 ms" phase timings the compiler prints.
var timing = regexp.MustCompile(`\d+ ms`)

// stackLine matches the source line of a Java stack-trace frame: editing
// a compiler source shifts those lines without changing any behaviour.
var stackLine = regexp.MustCompile(`\.java:\d+\)`)

// tmpName matches the random temporary file names of generated .t files.
var tmpName = regexp.MustCompile(`/[^\s]*/tmp\d+\.t`)

func inputs(t *testing.T, root string) []string {
	var files []string
	err := filepath.Walk(harness.JavaTestDir(root), func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() && strings.HasSuffix(p, ".t") {
			files = append(files, p)
		}
		return err
	})
	if err != nil {
		t.Fatal(err)
	}
	if len(files) == 0 {
		t.Fatalf("no .t input found under %s: T0 would pass vacuously", harness.JavaTestDir(root))
	}
	return files
}

// Floors against a vacuous pass: the minimum number of inputs on which
// the legacy compiler must succeed and produce output files. Measured on
// 2026-10-23 with the Gom mappings generated as in test/java/build.xml: 102
// inputs (default) and 103 (-np), up from 81 and 82 before, when the 21
// inputs of test/java/gom and test/java/sl including a generated mapping crashed
// identically in both compilers. The floors are the measured counts.
const (
	minDefault = 102
	minNP      = 103
)

// mustProduce lists the test/java/ subdirectories (slash-terminated, relative
// to test/java/) whose every input must compile and produce
// Java in both dists: they are the ones that include Gom-generated
// mappings, and a regression in the Gom step must not silently turn them
// back into two identical crashes.
var mustProduce = []string{"gom/", "sl/"}

// The inputs are compiled the way test/java/build.xml compiles them (targets
// build and build.nt), so that those including Gom-generated mappings are
// real comparisons rather than two identical "included file not found"
// crashes:
//   - each dist's own Gom first compiles the .gom files of test/java/ into a
//     temporary directory (gomRuns mirrors the two <gom.preset> calls);
//   - tom then finds the mappings as build.xml's tom does: there tom shares
//     Gom's destdir and TomStreamManager.getImportList adds
//     destdir/<package path>; here -d is the compared output directory, so
//     the same directory, <gen>/<package path>, is passed with -I;
//   - the inputs that build.xml compiles with extra options get them too
//     (tomOptions).

// gomRuns mirrors the <gom.preset> calls of test/java/build.xml: options, and
// whether a .gom (path relative to test/java/, slash-separated) belongs to the
// call. Both calls exclude **/error/** and **/regress/**.
var gomRuns = []struct {
	args    []string
	include func(rel string) bool
}{
	{[]string{"--package", "sl", "--termpointer", "--withCongruenceStrategies"},
		func(rel string) bool { return rel == "sl/testsl.gom" }},
	{nil, func(rel string) bool { return rel != "sl/testsl.gom" }},
}

// tomOptions are the extra options of the dedicated <tom.preset> calls of
// test/java/build.xml (input path relative to test/java/). --intermediate is left
// out: it only dumps the intermediate ASTs next to the output.
var tomOptions = map[string][]string{
	"TestOptimizer.t":             {"--optimize"},
	"sl/TestHandWrittenMapping.t": {"--genIntrospector"},
	"sl/IntrospectorBehaviour.t":  {"--genIntrospector"},
}

// generateGom runs d's Gom as test/java/build.xml does and returns the
// destination directory.
func generateGom(t *testing.T, d harness.Dist, root string) string {
	t.Helper()
	testDir := harness.JavaTestDir(root)
	var all []string
	if err := filepath.Walk(testDir, func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() {
			switch info.Name() {
			case "error", "regress":
				return filepath.SkipDir
			}
			return nil
		}
		if strings.HasSuffix(p, ".gom") {
			all = append(all, p)
		}
		return nil
	}); err != nil {
		t.Fatal(err)
	}
	gen := t.TempDir()
	for _, run := range gomRuns {
		var files []string
		for _, p := range all {
			rel, _ := filepath.Rel(testDir, p)
			if run.include(filepath.ToSlash(rel)) {
				files = append(files, p)
			}
		}
		if len(files) == 0 {
			t.Fatalf("gom run %v: no .gom file selected", run.args)
		}
		args := append(append([]string{"--destdir", gen}, run.args...), files...)
		if out, err := harness.RunIn(testDir, []string{"TOM_HOME=" + d.Home}, filepath.Join(d.Home, "bin", "gom"), args...); err != nil {
			t.Fatalf("%s/bin/gom %v: %v\n%s", d.Home, args, err, out)
		}
	}
	return gen
}

// packageDecl matches the package declaration of a Tom+Java input.
var packageDecl = regexp.MustCompile(`(?m)^\s*package\s+([\w.]+)\s*;`)

// buildArgs returns the options test/java/build.xml gives tom for src, gen
// being the directory generateGom filled.
func buildArgs(t *testing.T, root, src, gen string) []string {
	t.Helper()
	text, err := os.ReadFile(src)
	if err != nil {
		t.Fatal(err)
	}
	inc := gen
	if m := packageDecl.FindSubmatch(text); m != nil {
		inc = filepath.Join(gen, strings.ReplaceAll(string(m[1]), ".", string(filepath.Separator)))
	}
	rel, _ := filepath.Rel(harness.JavaTestDir(root), src)
	return append([]string{"-I", inc}, tomOptions[filepath.ToSlash(rel)]...)
}

func TestJavaOutputIdenticalToLegacy(t *testing.T) {
	compareWithLegacy(t, nil, minDefault)
}

// npAllowed lists the inputs whose -np Java output may differ from
// legacy/'s, each with the reason the new output is the correct one.
var npAllowed = map[string]string{}

// TestJavaOutputIdenticalToLegacyNewParser is T0 with the island parser
// (-np), the parser the Go target uses: changes made to it for Go must
// not alter the Java output, except for the allow-listed inputs.
func TestJavaOutputIdenticalToLegacyNewParser(t *testing.T) {
	compareWithLegacy(t, npAllowed, minNP, "-np")
}

// compareWithLegacy compiles every test/java/*.t with both compilers (with
// args) and requires identical diagnostics and output trees, except for
// the inputs listed in allowed (repository-relative path → reason).
func compareWithLegacy(t *testing.T, allowed map[string]string, floor int, args ...string) {
	legacy, current := harness.LegacyDist(t), harness.NewDist(t)
	root := harness.RepoRoot(t)
	genL, genN := generateGom(t, legacy, root), generateGom(t, current, root)
	var succeeded, filesCompared int
	defer func() {
		t.Logf("T0 %v: %d inputs with legacy success, %d .java files compared", args, succeeded, filesCompared)
		if t.Failed() {
			return
		}
		if succeeded < floor || filesCompared == 0 {
			t.Fatalf("vacuous comparison: legacy succeeded with output on %d inputs (floor %d), %d files compared", succeeded, floor, filesCompared)
		}
	}()
	for _, src := range inputs(t, root) {
		rel, _ := filepath.Rel(root, src)
		javaRel, _ := filepath.Rel(harness.JavaTestDir(root), src) // relative to test/java/, for mustProduce
		t.Run(rel, func(t *testing.T) {
			if reason, ok := allowed[rel]; ok {
				t.Skipf("allowed difference: %s", reason)
			}
			outL, outN := t.TempDir(), t.TempDir()
			logL, errL := legacy.Compile(t, src, outL, append(buildArgs(t, root, src, genL), args...)...)
			logN, errN := current.Compile(t, src, outN, append(buildArgs(t, root, src, genN), args...)...)
			norm := func(s, out string) string {
				s = timing.ReplaceAllString(s, "N ms")
				s = tmpName.ReplaceAllString(s, "__TMP__.t")
				s = stackLine.ReplaceAllString(s, ".java:N)")
				return harness.Normalize(s, map[string]string{out: "__OUT__", genL: "__GEN__", genN: "__GEN__", legacy.Home: "__HOME__", current.Home: "__HOME__"})
			}
			if (errL == nil) != (errN == nil) || norm(logL, outL) != norm(logN, outN) {
				t.Fatalf("diagnostics differ\nlegacy (%v):\n%s\nnew (%v):\n%s", errL, logL, errN, logN)
			}
			n := compareTrees(t, outL, outN)
			filesCompared += n
			for _, dir := range mustProduce {
				if strings.HasPrefix(filepath.ToSlash(javaRel), dir) && (errL != nil || errN != nil || n == 0) {
					t.Errorf("%s must produce Java in both dists (legacy err %v, new err %v, %d files)\nlegacy:\n%s\nnew:\n%s", rel, errL, errN, n, logL, logN)
				}
			}
			if errL == nil && n > 0 {
				succeeded++
			}
		})
	}
}

// compareTrees requires the two output trees to be identical and
// returns the number of files in a.
func compareTrees(t *testing.T, a, b string) int {
	t.Helper()
	seen := map[string]bool{}
	count := 0
	if err := filepath.Walk(a, func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		rel, _ := filepath.Rel(a, p)
		seen[rel] = true
		count++
		want, err := os.ReadFile(p)
		if err != nil {
			t.Errorf("%s: cannot read legacy output: %v", rel, err)
			return nil
		}
		got, err := os.ReadFile(filepath.Join(b, rel))
		if err != nil {
			t.Errorf("%s: missing in new output", rel)
		} else if string(want) != string(got) {
			t.Errorf("%s: content differs from legacy", rel)
		}
		return nil
	}); err != nil {
		t.Errorf("walking legacy output %s: %v", a, err)
	}
	if err := filepath.Walk(b, func(p string, info os.FileInfo, err error) error {
		if err == nil && !info.IsDir() {
			if rel, _ := filepath.Rel(b, p); !seen[rel] {
				t.Errorf("%s: extra file in new output", rel)
			}
		}
		return err
	}); err != nil {
		t.Errorf("walking new output %s: %v", b, err)
	}
	return count
}
