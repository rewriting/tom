// Package port holds the Tom+Go ports of test/*.t and checks them:
// T1 (generated Go is pinned by golden files and builds) and, from
// Task 8, T2 (same behaviour as the original under legacy/).
package port

import (
	"bufio"
	"crypto/sha256"
	"encoding/hex"
	"encoding/json"
	"errors"
	"flag"
	"fmt"
	"io"
	"os"
	"path/filepath"
	"regexp"
	"sort"
	"strconv"
	"strings"
	"testing"

	gomparser "tom/gom/parser"
	"tomtest/harness"
)

var update = flag.Bool("update", false, "rewrite testdata/golden/*.go from the current compiler")

type portCase struct {
	Rel    string // path relative to test/ (and to test/go/)
	GoTest bool   // run with `go test` (JUnit-style) rather than `go run`
	// Unsupported lists the original tests (Go-capitalised name ->
	// reason) that the port keeps commented out under a
	// `// port: unsupported` note because the Go runtime cannot express
	// them. T2 exempts them from the legacy pass set, and fails if one
	// of them exists and passes in Go (stale entry).
	Unsupported map[string]string
	// FullyUnsupported marks a port all of whose tests are Unsupported:
	// T2 then only checks that what remains compiles and that no test
	// passes, instead of reporting a vacuous oracle; the dashboard status
	// is "non supporté".
	FullyUnsupported bool
	// Gom lists the standalone .gom files test/build.xml compiles with
	// Gom before the case, whose generated mappings the case includes.
	Gom []gomFile
	// Support lists other .t files (relative to test/) compiled with the
	// case: the original needs their classes (same Java package). In Go
	// each one is compiled into its own package, in the sub-directory
	// supportDir(rel) of the case's module, because two Tom-generated Go
	// files cannot share a package (each defines the tom_* helpers of the
	// mappings it includes).
	Support []string
	// TomArgs are the extra options test/build.xml gives tom (both
	// compilers) for an input of the case: the case itself or a support
	// file (key: path relative to test/).
	TomArgs map[string][]string
}

// gomFile is a standalone .gom file of test/ (Rel, relative to test/);
// the port directory keeps an identical copy at test/go/<Rel>
// (TestGomCopiesIdentical), from which gomgo generates the Go side.
type gomFile struct {
	Rel string
	// JavaArgs are the options of test/build.xml's <gom.preset> call,
	// given to legacy/'s Gom for the oracle.
	JavaArgs []string
	// GoArgs are the options given to `gomgo gom-block`.
	GoArgs []string
	// Dir overrides the directory (relative to the Go module root, or to
	// the -I root for legacy/) that receives the module: Java Gom's
	// package path of the module below the case's own package, e.g.
	// b/u/i/l/t/i/n/builtin for gom.b.u.i.l.t.i.n.Builtin included from
	// package gom. Default: the lower-cased base name of Rel.
	Dir string
	// Deps lists the other .gom files of test/ the module imports (Java
	// Gom finds them next to Rel); gomgo reads the identical copies next
	// to the port's copy and generates them in the same Go package.
	Deps []string
	// PortedHooks: the hook bodies of Rel are host code (Java), ported to
	// Go in the copy; the rest of the copy (the signature, the hooks'
	// names, kinds and arguments) must still equal the original.
	PortedHooks bool
}

// testslGom is test/sl/testsl.gom, compiled by test/build.xml with
// package=sl, termpointer=true and withCongruenceStrategies=true.
// gomgo does not implement --termpointer (term pointers: the
// VarTerm/RefTerm/PathTerm/LabTerm operators and expand()); only
// sl/TestRef.t relies on them.
var testslGom = gomFile{
	Rel:      "sl/testsl.gom",
	JavaArgs: []string{"--package", "sl", "--termpointer", "--withCongruenceStrategies"},
	GoArgs:   []string{"--withCongruenceStrategies"},
}

// importingGom is test/gom/Importing.gom, which imports Imported.gom,
// which imports Leaf.gom.
var importingGom = gomFile{Rel: "gom/Importing.gom", Deps: []string{"gom/Imported.gom", "gom/Leaf.gom"}}

// gomModuleDir is the directory, relative to the case's output
// directory (the Go module root, or the -I root for legacy/), that
// receives the code generated for g: Gom's <module>/<module>.tom layout,
// so that `%include { testsl/testsl.tom }` resolves against -I.
func gomModuleDir(g gomFile) string {
	if g.Dir != "" {
		return g.Dir
	}
	return strings.ToLower(strings.TrimSuffix(filepath.Base(g.Rel), ".gom"))
}

// gomPackage is the Go package name of the code generated for g: the
// last element of its directory.
func gomPackage(g gomFile) string { return filepath.Base(gomModuleDir(g)) }

// supportDir is the Go package directory (relative to the case's module
// root) of a support file: its lower-cased base name.
func supportDir(rel string) string {
	return strings.ToLower(strings.TrimSuffix(filepath.Base(rel), ".t"))
}

var cases = []portCase{
	{Rel: "Peano.t"},
	{Rel: "TestPeano.t", GoTest: true},
	{Rel: "Array.t"},
	{Rel: "TestArray.t", GoTest: true},
	{Rel: "TestList.t", GoTest: true},
	{Rel: "TestBackQuote.t", GoTest: true},
	{Rel: "TestVarVarStar.t", GoTest: true},
	{Rel: "TestNonVarSubjects.t", GoTest: true},
	{Rel: "TestSublists.t", GoTest: true},
	{Rel: "numeric/TestNumericConditions.t", GoTest: true},
	{Rel: "TestAlgebraic.t", GoTest: true},
	{Rel: "TestStrategy.t", GoTest: true},
	{Rel: "sl/TestEnvironment.t", GoTest: true},
	{Rel: "sl/TestPosition.t", GoTest: true},
	{Rel: "sl/TestOnceTopDownId.t", GoTest: true},
	{Rel: "sl/TestGetSubterm.t", GoTest: true},
	{Rel: "sl/TestImportStrat.t", GoTest: true, Gom: []gomFile{testslGom}, Support: []string{"sl/CommonStrat.t"}},
	{Rel: "sl/TestReduceAll.t", GoTest: true},
	{Rel: "sl/TestSLModes.t", GoTest: true, Gom: []gomFile{testslGom}},
	{Rel: "sl/TestSL.t", GoTest: true, Gom: []gomFile{testslGom},
		Support: []string{"sl/OldBehaviour.t", "sl/NewBehaviour.t", "sl/IntrospectorBehaviour.t"},
		TomArgs: map[string][]string{"sl/IntrospectorBehaviour.t": {"--genIntrospector"}}},
	{Rel: "sl/TestReflective.t", GoTest: true, Gom: []gomFile{testslGom}, Unsupported: map[string]string{
		"TestMake": "gomgo represents the variadic l(Term*) of testsl.gom as a flat list, without Java Gom's Consl/Emptyl constructors, hence without Make_Consl/Make_Emptyl",
	}},
	{Rel: "sl/TestRef.t", GoTest: true, Gom: []gomFile{testslGom}, FullyUnsupported: true, Unsupported: map[string]string{
		"TestRef1": testRefUnsupported, "TestRef2": testRefUnsupported, "TestRef3": testRefUnsupported, "TestRef4": testRefUnsupported,
	}},
	{Rel: "sl/TestHandWrittenMapping.t", GoTest: true, TomArgs: map[string][]string{"sl/TestHandWrittenMapping.t": {"--genIntrospector"}}},
	{Rel: "gom/TestDotted.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Dotted.gom"}}},
	{Rel: "gom/BenchConsGom.t", Gom: []gomFile{{Rel: "gom/List.gom"}}},
	{Rel: "gom/TestBool.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Bool.gom"}}},
	{Rel: "gom/TestMultiHook.t", GoTest: true, Gom: []gomFile{{Rel: "gom/MultiHook.gom"}}},
	{Rel: "gom/TestTomHook.t", GoTest: true},
	{Rel: "gom/TestStratMapping.t", GoTest: true},
	{Rel: "gom/TestGomMultithread.t", GoTest: true},
	{Rel: "gom/TestGomWithoutMaximalSharing.t", GoTest: true},
	{Rel: "gom/TestImport.t", GoTest: true, Gom: []gomFile{importingGom}},
	{Rel: "gom/TestRuleBool.t", GoTest: true, Gom: []gomFile{{Rel: "gom/RuleBool.gom"}}},
	{Rel: "gom/TestRuleCond.t", GoTest: true},
	{Rel: "gom/TestPeanoRule.t", GoTest: true},
	{Rel: "gom/TestMakeEmpty.t", GoTest: true},
	{Rel: "gom/TestJavaHook.t", GoTest: true, Gom: []gomFile{{Rel: "gom/JavaHook.gom", PortedHooks: true}}},
	{Rel: "gom/TestFL.t", GoTest: true},
	{Rel: "gom/TestAU.t", GoTest: true},
	{Rel: "gom/TestACU.t", GoTest: true},
	{Rel: "gom/TestList.t", GoTest: true},
	{Rel: "gom/TestRuleList.t", GoTest: true, Gom: []gomFile{{Rel: "gom/RuleList.gom"}}},
	{Rel: "gom/TestRuleSortList.t", GoTest: true},
	{Rel: "gom/TestYingYang.t", GoTest: true, Gom: []gomFile{{Rel: "gom/Ying.gom", Deps: []string{"gom/Yang.gom"}}}},
}

// testRefUnsupported is why the tests of sl/TestRef.t are not ported.
const testRefUnsupported = "needs Gom's --termpointer (RefTerm/LabTerm/PathTerm/VarTerm, expand()) and the term-graph strategies of sl/graph.tom (DeRef, StrictDeRef, AllSeq), not implemented by gomgo and the Go sl library"

func base(c portCase) string { return strings.TrimSuffix(filepath.Base(c.Rel), ".t") }

func compileCase(t *testing.T, c portCase) string {
	t.Helper()
	dir := t.TempDir()
	gens, out, err := generateGo(t, c, dir)
	if err != nil {
		t.Fatalf("%v\n%s", err, out)
	}
	for rel, gen := range gens {
		checkGolden(t, rel, gen)
	}
	harness.SetupGoModule(t, dir)
	return dir
}

// generateGo fills dir (the future Go module root) as test/build.xml
// would for the Java original: gomgo first compiles the case's
// standalone .gom copies into dir/<module>/, then tom -go (with -I dir)
// compiles the support files into dir/<supportDir>/ and the case into
// dir. It returns the generated file of each .t (relative to test/).
func generateGo(t *testing.T, c portCase, dir string) (map[string]string, string, error) {
	t.Helper()
	dist := harness.NewDist(t)
	args := []string{"-go", "-np"}
	if len(c.Gom) > 0 {
		args = append(args, "-I", dir)
	}
	for _, g := range c.Gom {
		src, err := filepath.Abs(g.Rel)
		if err != nil {
			t.Fatal(err)
		}
		mod := gomModuleDir(g)
		gargs := append(append([]string{"gom-block", "-o", filepath.Join(dir, mod), "--pkg", gomPackage(g)}, g.GoArgs...), src)
		if out, err := harness.RunIn(dir, nil, harness.GomgoBin(t), gargs...); err != nil {
			return nil, out, fmt.Errorf("gomgo %v failed: %v", gargs, err)
		}
	}
	gens := map[string]string{}
	for _, rel := range c.Support {
		src, err := filepath.Abs(rel)
		if err != nil {
			t.Fatal(err)
		}
		sub := filepath.Join(dir, supportDir(rel))
		if out, err := dist.Compile(t, src, sub, append(append([]string(nil), args...), c.TomArgs[rel]...)...); err != nil {
			return nil, out, fmt.Errorf("tom -go failed on %s: %v", rel, err)
		}
		gens[rel] = filepath.Join(sub, strings.TrimSuffix(filepath.Base(rel), ".t")+".go")
	}
	src, err := filepath.Abs(c.Rel)
	if err != nil {
		t.Fatal(err)
	}
	if out, err := dist.Compile(t, src, dir, append(args, c.TomArgs[c.Rel]...)...); err != nil {
		return nil, out, fmt.Errorf("tom -go failed: %v", err)
	}
	gen := filepath.Join(dir, base(c)+".go")
	if c.GoTest {
		if err := os.Rename(gen, filepath.Join(dir, base(c)+"_test.go")); err != nil {
			return nil, "", err
		}
		gen = filepath.Join(dir, base(c)+"_test.go")
	}
	gens[c.Rel] = gen
	return gens, "", nil
}

func checkGolden(t *testing.T, rel, gen string) {
	t.Helper()
	got, err := os.ReadFile(gen)
	if err != nil {
		t.Fatal(err)
	}
	golden := filepath.Join("testdata", "golden", strings.ReplaceAll(strings.TrimSuffix(rel, ".t"), "/", "_")+".go")
	if *update {
		if err := os.WriteFile(golden, got, 0o644); err != nil {
			t.Fatal(err)
		}
		return
	}
	want, err := os.ReadFile(golden)
	if err != nil {
		t.Fatalf("missing golden %s (run with -update): %v", golden, err)
	}
	if string(want) != string(got) {
		t.Fatalf("generated Go differs from %s (run with -update if intended)", golden)
	}
}

// runCase runs the compiled case; for go-test cases it returns the set
// of test names that passed.
func runCase(t *testing.T, c portCase, dir string) (map[string]bool, string, error) {
	t.Helper()
	if !c.GoTest {
		out, err := harness.RunIn(dir, nil, "go", "run", ".")
		return nil, out, err
	}
	out, err := harness.RunIn(dir, nil, "go", "test", "-json", "-count=1", ".")
	passed := map[string]bool{}
	sc := bufio.NewScanner(strings.NewReader(out))
	for sc.Scan() {
		var ev struct{ Action, Test string }
		if json.Unmarshal(sc.Bytes(), &ev) == nil && ev.Test != "" && ev.Action == "pass" {
			passed[ev.Test] = true
		}
	}
	return passed, out, err
}

func TestGoBackend(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			dir := compileCase(t, c)
			if _, out, err := runCase(t, c, dir); err != nil {
				t.Fatalf("running %s failed: %v\n%s", c.Rel, err, out)
			}
		})
	}
}

// ---------------------------------------------------------------------
// T2: behaviour of each port vs the original run by legacy/.

var (
	updateOracle   = flag.Bool("update-oracle", false, "recompute testdata/oracle/*.json with legacy/")
	writeDashboard = flag.Bool("write-dashboard", false, "rewrite ../../doc/go/port-dashboard.md")
)

// oracle is what the original test/<Rel>, compiled by legacy/ and run
// under the JVM, did.
type oracle struct {
	// Passed lists the JUnit tests that passed, Go-capitalised
	// (testFoo -> TestFoo). A @Parameterized test counts as passed only
	// if every parameter row passed (JUnit names rows testFoo[i]; the Go
	// ports loop over the rows inside one TestFoo).
	Passed []string `json:"passed"`
	// Failed lists the JUnit tests that failed (same naming), for the
	// record: they are not required to pass in Go.
	Failed []string `json:"failed,omitempty"`
	Stdout string   `json:"stdout"` // stdout+stderr of a main program
	ExitOK bool     `json:"exitOK"`
	// SourceSHA256 is the hash of test/<Rel> the oracle was computed
	// from; a mismatch means the oracle is stale.
	SourceSHA256 string `json:"source_sha256"`
}

func oraclePath(c portCase) string {
	return filepath.Join("testdata", "oracle", strings.ReplaceAll(strings.TrimSuffix(c.Rel, ".t"), "/", "_")+".json")
}

// capitalise gives the Go name of a JUnit test: testFoo -> TestFoo, and
// testfoo -> TestFoo too, since go test only runs the functions whose
// name continues with an upper case (or a digit) after Test.
func capitalise(name string) string {
	if name == "" {
		return name
	}
	if strings.HasPrefix(name, "test") && len(name) > 4 {
		return "Test" + strings.ToUpper(name[4:5]) + name[5:]
	}
	return strings.ToUpper(name[:1]) + name[1:]
}

// junitListener runs JUnitCore on one class and reports each executed
// test on stdout as "@@T2 started|failed <method>" (a parameterised row
// is "testFoo[3]"). Reading the runner's own events, rather than
// JUnitCore's text report, sees exactly the tests JUnit ran.
const junitListener = `import org.junit.runner.*;
import org.junit.runner.notification.*;

public class T2JUnitListener extends RunListener {
  public void testStarted(Description d) { System.out.println("@@T2 started " + d.getMethodName()); }
  public void testFailure(Failure f) { System.out.println("@@T2 failed " + f.getDescription().getMethodName()); }
  public void testAssumptionFailure(Failure f) { System.out.println("@@T2 failed " + f.getDescription().getMethodName()); }
  public static void main(String[] args) throws Exception {
    JUnitCore core = new JUnitCore();
    core.addListener(new T2JUnitListener());
    Result r = core.run(Class.forName(args[0]));
    System.exit(r.wasSuccessful() ? 0 : 1);
  }
}
`

// junitEvent matches a listener line; the parameter index of a
// @Parameterized row ("[3]") is stripped by the second group.
var junitEvent = regexp.MustCompile(`^@@T2 (started|failed) ([A-Za-z_$][\w$]*)(\[[^\]]*\])?$`)

// runJUnit runs the compiled original under JUnit and fills o.Passed /
// o.Failed. A parameterised test passes only if all its rows pass.
func runJUnit(t *testing.T, c portCase, src, dir, cp, class string, o *oracle) {
	t.Helper()
	lsrc := filepath.Join(dir, "T2JUnitListener.java")
	if err := os.WriteFile(lsrc, []byte(junitListener), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := harness.RunIn(dir, nil, "javac", "-nowarn", "-cp", cp, "-d", dir, lsrc); err != nil {
		t.Fatalf("javac listener failed: %v\n%s", err, out)
	}
	out, err := harness.RunIn(dir, nil, "java", "-cp", cp, "T2JUnitListener", class)
	o.ExitOK = err == nil
	started, failed := map[string]bool{}, map[string]bool{}
	var order []string
	for _, line := range strings.Split(out, "\n") {
		m := junitEvent.FindStringSubmatch(strings.TrimSpace(line))
		if m == nil {
			continue
		}
		if m[1] == "failed" {
			failed[m[2]] = true
			continue
		}
		if !started[m[2]] {
			order = append(order, m[2])
		}
		started[m[2]] = true
	}
	for name := range failed {
		if !started[name] {
			t.Fatalf("JUnit failure outside a test (%s) in %s\n%s", name, c.Rel, out)
		}
	}
	if err != nil && len(failed) == 0 {
		t.Fatalf("JUnit failed without a test failure: %v\n%s", err, out)
	}
	// Cross-check: JUnit ran exactly the active @Test methods of the source.
	declared := junitTests(t, src)
	if strings.Join(sortedCopy(declared), ",") != strings.Join(sortedCopy(order), ",") {
		t.Fatalf("JUnit ran %v but %s declares %v\n%s", order, c.Rel, declared, out)
	}
	for _, m := range declared {
		if failed[m] {
			o.Failed = append(o.Failed, capitalise(m))
		} else {
			o.Passed = append(o.Passed, capitalise(m))
		}
	}
	if len(declared) == 0 {
		t.Fatalf("no @Test found in %s", src)
	}
}

func sortedCopy(xs []string) []string {
	ys := append([]string(nil), xs...)
	sort.Strings(ys)
	return ys
}

// stripComments blanks Java /* */ and // comments (outside string and
// char literals), keeping line breaks so line structure survives.
func stripComments(src string) string {
	var b strings.Builder
	for i := 0; i < len(src); i++ {
		switch {
		case src[i] == '"' || src[i] == '\'':
			q := src[i]
			b.WriteByte(q)
			for i++; i < len(src) && src[i] != q && src[i] != '\n'; i++ {
				if src[i] == '\\' && i+1 < len(src) {
					b.WriteByte(src[i])
					i++
				}
				b.WriteByte(src[i])
			}
			if i < len(src) {
				b.WriteByte(src[i])
			}
		case strings.HasPrefix(src[i:], "//"):
			for i < len(src) && src[i] != '\n' {
				i++
			}
			if i < len(src) {
				b.WriteByte('\n')
			}
		case strings.HasPrefix(src[i:], "/*"):
			end := strings.Index(src[i+2:], "*/")
			if end < 0 {
				end = len(src) - i - 2
			}
			b.WriteString(strings.Repeat("\n", strings.Count(src[i:i+2+end], "\n")))
			i += 2 + end + 1
		default:
			b.WriteByte(src[i])
		}
	}
	return b.String()
}

// legacyOracle compiles and runs the ORIGINAL test/<Rel> with the
// human-written compiler (javac + JUnit or java main) and caches what
// it observed in testdata/oracle/.
func legacyOracle(t *testing.T, c portCase) oracle {
	t.Helper()
	path := oraclePath(c)
	if !*updateOracle {
		data, err := os.ReadFile(path)
		if err != nil {
			t.Fatalf("missing %s (run with -update-oracle)", path)
		}
		var o oracle
		if err := json.Unmarshal(data, &o); err != nil {
			t.Fatal(err)
		}
		srcs := sourcesOf(harness.RepoRoot(t), c)
		if err := oracleStale(c, o, srcs[0], srcs[1:]...); err != nil {
			t.Fatal(err)
		}
		return o
	}
	legacy := harness.LegacyDist(t)
	root := harness.RepoRoot(t)
	src := filepath.Join(root, "test", c.Rel)
	dir := t.TempDir()
	// As test/build.xml: Gom first, then tom with -I towards the
	// generated mappings of the input's package, on the case and its
	// support files.
	var targs []string
	if len(c.Gom) > 0 {
		gen := t.TempDir()
		for _, g := range c.Gom {
			gargs := append(append([]string{"--destdir", gen}, g.JavaArgs...), filepath.Join(root, "test", g.Rel))
			if out, err := harness.RunIn(gen, []string{"TOM_HOME=" + legacy.Home}, filepath.Join(legacy.Home, "bin", "gom"), gargs...); err != nil {
				t.Fatalf("legacy gom %v failed: %v\n%s", gargs, err, out)
			}
		}
		inc := gen
		if pkg := javaPackage(t, src); pkg != "" {
			inc = filepath.Join(gen, strings.ReplaceAll(pkg, ".", string(filepath.Separator)))
		}
		targs = append([]string{"-I", inc}, targs...)
		// Gom's own Java sources belong to the program.
		if err := copyTree(gen, dir); err != nil {
			t.Fatal(err)
		}
	}
	for _, rel := range append(append([]string(nil), c.Support...), c.Rel) {
		if out, err := legacy.Compile(t, filepath.Join(root, "test", rel), dir, append(append([]string(nil), targs...), c.TomArgs[rel]...)...); err != nil {
			t.Fatalf("legacy tom failed on %s: %v\n%s", rel, err, out)
		}
	}
	cp := strings.Join([]string{
		filepath.Join(legacy.Home, "lib", "tom-runtime-full.jar"),
		filepath.Join(root, "legacy", "stable", "lib", "compiletime", "junit.jar"),
		dir,
	}, string(os.PathListSeparator))
	var javaFiles []string
	filepath.Walk(dir, func(p string, info os.FileInfo, err error) error {
		if err == nil && strings.HasSuffix(p, ".java") {
			javaFiles = append(javaFiles, p)
		}
		return err
	})
	if out, err := harness.RunIn(dir, nil, "javac", append([]string{"-nowarn", "-encoding", "UTF-8", "-cp", cp, "-d", dir}, javaFiles...)...); err != nil {
		t.Fatalf("javac failed: %v\n%s", err, out)
	}
	class := base(c)
	if pkg := javaPackage(t, src); pkg != "" {
		class = pkg + "." + class
	}
	var o oracle
	sum, err := sourceSHA256(sourcesOf(root, c)...)
	if err != nil {
		t.Fatal(err)
	}
	o.SourceSHA256 = sum
	if c.GoTest {
		runJUnit(t, c, src, dir, cp, class, &o)
	} else {
		out, err := harness.RunIn(dir, nil, "java", "-cp", cp, class)
		o.Stdout, o.ExitOK = out, err == nil
	}
	data, _ := json.MarshalIndent(o, "", "  ")
	if err := os.MkdirAll(filepath.Dir(path), 0o755); err != nil {
		t.Fatal(err)
	}
	if err := os.WriteFile(path, append(data, '\n'), 0o644); err != nil {
		t.Fatal(err)
	}
	return o
}

// junitTests lists the active @Test methods of a JUnit .t source;
// commented-out tests (// or /* */) are skipped.
func junitTests(t *testing.T, src string) []string {
	t.Helper()
	data, err := os.ReadFile(src)
	if err != nil {
		t.Fatal(err)
	}
	return junitTestNames(string(data))
}

func junitTestNames(source string) []string {
	var names []string
	lines := strings.Split(stripComments(source), "\n")
	for i, line := range lines {
		s := strings.TrimSpace(line)
		if s != "@Test" && !strings.HasPrefix(s, "@Test(") {
			continue
		}
		for _, next := range lines[i+1:] {
			if j := strings.Index(next, "void "); j >= 0 {
				rest := next[j+5:]
				if k := strings.Index(rest, "("); k > 0 {
					names = append(names, strings.TrimSpace(rest[:k]))
				}
				break
			}
		}
	}
	return names
}

func javaPackage(t *testing.T, src string) string {
	t.Helper()
	data, _ := os.ReadFile(src)
	for _, line := range strings.Split(string(data), "\n") {
		if s := strings.TrimSpace(line); strings.HasPrefix(s, "package ") {
			return strings.TrimSuffix(strings.TrimSpace(strings.TrimPrefix(s, "package ")), ";")
		}
	}
	return ""
}

// sourceSHA256 hashes the concatenation of the given files (for a
// single file, the hash of that file).
func sourceSHA256(paths ...string) (string, error) {
	h := sha256.New()
	for _, path := range paths {
		f, err := os.Open(path)
		if err != nil {
			return "", err
		}
		_, err = io.Copy(h, f)
		f.Close()
		if err != nil {
			return "", err
		}
	}
	return hex.EncodeToString(h.Sum(nil)), nil
}

// sourcesOf lists the original files whose content determines the
// oracle of c: test/<Rel>, then its support files and .gom files.
func sourcesOf(root string, c portCase) []string {
	paths := []string{filepath.Join(root, "test", c.Rel)}
	for _, rel := range c.Support {
		paths = append(paths, filepath.Join(root, "test", rel))
	}
	for _, g := range c.Gom {
		for _, rel := range append([]string{g.Rel}, g.Deps...) {
			paths = append(paths, filepath.Join(root, "test", rel))
		}
	}
	return paths
}

// copyTree copies the regular files of src into dst (same layout).
func copyTree(src, dst string) error {
	return filepath.Walk(src, func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		rel, _ := filepath.Rel(src, p)
		data, err := os.ReadFile(p)
		if err != nil {
			return err
		}
		if err := os.MkdirAll(filepath.Join(dst, filepath.Dir(rel)), 0o755); err != nil {
			return err
		}
		return os.WriteFile(filepath.Join(dst, rel), data, 0o644)
	})
}

// oracleStale checks, without Java, that a cached oracle still matches
// the original source src.
func oracleStale(c portCase, o oracle, src string, extra ...string) error {
	sum, err := sourceSHA256(append([]string{src}, extra...)...)
	if err != nil {
		return err
	}
	if sum != o.SourceSHA256 {
		return fmt.Errorf("oracle stale: %s (or its support/.gom files) changed since the oracle was computed (run with -update-oracle)", src)
	}
	if !c.GoTest {
		return nil
	}
	data, err := os.ReadFile(src)
	if err != nil {
		return err
	}
	var declared []string
	for _, m := range junitTestNames(string(data)) {
		declared = append(declared, capitalise(m))
	}
	recorded := append(append([]string(nil), o.Passed...), o.Failed...)
	if strings.Join(sortedCopy(declared), ",") != strings.Join(sortedCopy(recorded), ",") {
		return fmt.Errorf("oracle stale: %s declares %v but the oracle records %v (run with -update-oracle)",
			src, sortedCopy(declared), sortedCopy(recorded))
	}
	return nil
}

// compareWithOracle returns the T2 divergences of one run against the
// legacy oracle (empty when the port behaves like the original).
func compareWithOracle(c portCase, want oracle, passed map[string]bool, out string, runErr error) []string {
	var diffs []string
	if !c.GoTest {
		if (runErr == nil) != want.ExitOK || out != want.Stdout {
			diffs = append(diffs, "main program differs\nlegacy (ok="+strconv.FormatBool(want.ExitOK)+"):\n"+want.Stdout+
				"\ngo (ok="+strconv.FormatBool(runErr == nil)+"):\n"+out)
		}
		return diffs
	}
	legacyPassed := map[string]bool{}
	for _, name := range want.Passed {
		legacyPassed[name] = true
	}
	topPassed := map[string]bool{}
	for name := range passed {
		if !strings.Contains(name, "/") { // subtests are the port's business
			topPassed[name] = true
		}
	}
	required := 0
	for _, name := range want.Passed {
		if _, unsupported := c.Unsupported[name]; !unsupported {
			required++
		}
	}
	switch {
	case required == 0 && !c.FullyUnsupported:
		diffs = append(diffs, "vacuous oracle: no legacy-passing test remains required once Unsupported is removed")
	case required > 0 && c.FullyUnsupported:
		diffs = append(diffs, "FullyUnsupported, but "+strconv.Itoa(required)+" legacy-passing test(s) are not Unsupported")
	}
	if runErr != nil && len(topPassed) == 0 {
		diffs = append(diffs, "go test failed and no Go test passed ("+runErr.Error()+")")
	}
	for _, name := range want.Passed {
		if _, unsupported := c.Unsupported[name]; unsupported {
			continue
		}
		if !topPassed[name] {
			diffs = append(diffs, name+" passes under legacy/ but not in Go")
		}
	}
	for name := range c.Unsupported {
		if topPassed[name] {
			diffs = append(diffs, name+" is listed as unsupported but passes in Go (stale Unsupported entry)")
		}
		if !legacyPassed[name] {
			diffs = append(diffs, name+" is listed as unsupported but is not a passing test of the original")
		}
	}
	for name := range topPassed {
		if !legacyPassed[name] {
			diffs = append(diffs, name+" passes in Go but does not pass (or does not exist) under legacy/")
		}
	}
	sort.Strings(diffs)
	return diffs
}

func TestSameBehaviourAsLegacy(t *testing.T) {
	for _, c := range cases {
		c := c
		t.Run(c.Rel, func(t *testing.T) {
			want := legacyOracle(t, c)
			dir := compileCase(t, c)
			passed, out, err := runCase(t, c, dir)
			diffs := compareWithOracle(c, want, passed, out, err)
			for _, d := range diffs {
				t.Error(d)
			}
			if len(diffs) > 0 && c.GoTest {
				t.Logf("go test output:\n%s", goTestOutput(out))
				if vout, verr := harness.RunIn(dir, nil, "go", "vet", "./..."); verr != nil {
					t.Logf("go vet ./... (build diagnostics):\n%s", vout)
				}
			}
		})
	}
}

// goTestOutput extracts the human-readable output from `go test -json`.
func goTestOutput(jsonOut string) string {
	var b strings.Builder
	sc := bufio.NewScanner(strings.NewReader(jsonOut))
	for sc.Scan() {
		var ev struct{ Action, Output string }
		if json.Unmarshal(sc.Bytes(), &ev) != nil {
			b.WriteString(sc.Text() + "\n")
		} else if ev.Action == "output" {
			b.WriteString(ev.Output)
		}
	}
	return b.String()
}

// TestUnsupportedMarkers keeps the Unsupported maps in step with the
// `// port: unsupported` notes in the ports.
func TestUnsupportedMarkers(t *testing.T) {
	for _, c := range cases {
		data, err := os.ReadFile(c.Rel)
		if err != nil {
			t.Fatal(err)
		}
		marked := strings.Contains(string(data), "// port: unsupported")
		if marked != (len(c.Unsupported) > 0) {
			t.Errorf("%s: `// port: unsupported` note present=%v but Unsupported has %d entries", c.Rel, marked, len(c.Unsupported))
		}
		for name := range c.Unsupported {
			lower := strings.ToLower(name[:1]) + name[1:]
			if !strings.Contains(string(data), lower) {
				t.Errorf("%s: unsupported test %s (%s) not mentioned in the port", c.Rel, name, lower)
			}
		}
	}
}

// TestDashboard classifies every test/**/*.t (outside error/) and, with
// -write-dashboard, rewrites doc/go/port-dashboard.md.
func TestDashboard(t *testing.T) {
	if !*writeDashboard {
		t.Skip("run with -write-dashboard")
	}
	root := harness.RepoRoot(t)
	ported := map[string]portCase{}
	supportOf := map[string]string{} // support file -> its case
	for _, c := range cases {
		ported[c.Rel] = c
		for _, rel := range c.Support {
			supportOf[rel] = c.Rel
		}
	}
	var rows []string
	counts := map[string]int{}
	filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		// test/go holds the ports themselves, not original cases.
		if err == nil && info.IsDir() && p == filepath.Join(root, "test", "go") {
			return filepath.SkipDir
		}
		if err != nil || info.IsDir() || !strings.HasSuffix(p, ".t") {
			return err
		}
		rel, _ := filepath.Rel(filepath.Join(root, "test"), p)
		rel = filepath.ToSlash(rel)
		if strings.HasPrefix(rel, "error/") {
			return nil
		}
		status, note := "non porté", ""
		if main, ok := supportOf[rel]; ok {
			// A support file has no test of its own: its classes are
			// exercised, in Java and in Go, by the case that uses them.
			status, note = "support", "compilé avec `"+main+"`, couvert par son statut"
		} else if c, ok := ported[rel]; ok {
			status = dashboardStatus(t, c)
			var names []string
			for name := range c.Unsupported {
				names = append(names, name)
			}
			sort.Strings(names)
			for _, name := range names {
				if note != "" {
					note += " ; "
				}
				note += "`" + name + "` non supporté : " + c.Unsupported[name]
			}
		}
		counts[status]++
		rows = append(rows, "| `"+rel+"` | "+status+" | "+note+" |")
		return nil
	})
	var b strings.Builder
	b.WriteString("# Tableau de bord du portage Tom+Go (T2)\n\n")
	b.WriteString("Généré par `mage portGo` (`go test ./ -args -write-dashboard` dans `test/go`).\n")
	b.WriteString("Chaque `test/**/*.t` hors `error/` ; un cas porté est OK quand son port\n")
	b.WriteString("Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par\n")
	b.WriteString("`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).\n")
	b.WriteString("Un fichier « support » n'a pas de test propre : ses classes sont compilées\n")
	b.WriteString("avec le cas qui les utilise (comme `test/build.xml` compile tout le\n")
	b.WriteString("répertoire), et c'est le statut de ce cas qui les valide.\n\n")
	b.WriteString("- total : " + strconv.Itoa(len(rows)) + "\n")
	for _, s := range []string{"OK", "divergence", "échec go test", "échec go build", "échec tom", "oracle invalide", "non supporté", "support", "non porté"} {
		b.WriteString("- " + s + " : " + strconv.Itoa(counts[s]) + "\n")
	}
	b.WriteString("\n| Cas | Statut | Notes |\n|---|---|---|\n" + strings.Join(rows, "\n") + "\n")
	if err := os.WriteFile(filepath.Join(root, "doc", "go", "port-dashboard.md"), []byte(b.String()), 0o644); err != nil {
		t.Fatal(err)
	}
}

// dashboardStatus re-runs one case and classifies it without failing
// the dashboard test.
func dashboardStatus(t *testing.T, c portCase) string {
	t.Helper()
	// Subtests are allowed to fail: their outcome is the status. The
	// oracle is loaded first so that a missing or stale oracle is not
	// mistaken for a Tom failure.
	var want oracle
	if !t.Run("oracle/"+c.Rel, func(t *testing.T) { want = legacyOracle(t, c) }) {
		return "oracle invalide"
	}
	status := "échec tom"
	t.Run("status/"+c.Rel, func(t *testing.T) {
		dir := t.TempDir()
		if _, _, err := generateGo(t, c, dir); err != nil {
			return
		}
		harness.SetupGoModule(t, dir)
		buildArgs := []string{"build", "."}
		if c.GoTest {
			buildArgs = []string{"test", "-c", "-o", os.DevNull, "."}
		}
		if _, err := harness.RunIn(dir, nil, "go", buildArgs...); err != nil {
			status = "échec go build"
			return
		}
		passed, out, err := runCase(t, c, dir)
		switch {
		case c.GoTest && err != nil && len(passed) == 0:
			status = "échec go test"
		case len(compareWithOracle(c, want, passed, out, err)) > 0:
			status = "divergence"
		case c.FullyUnsupported:
			status = "non supporté"
		default:
			status = "OK"
		}
	})
	return status
}

// TestCompareWithOracle pins the T2 rules on synthetic runs.
func TestCompareWithOracle(t *testing.T) {
	c := portCase{Rel: "X.t", GoTest: true, Unsupported: map[string]string{"TestU": "why"}}
	want := oracle{Passed: []string{"TestA", "TestU"}, Failed: []string{"TestF"}}
	set := func(xs ...string) map[string]bool {
		m := map[string]bool{}
		for _, x := range xs {
			m[x] = true
		}
		return m
	}
	for _, tc := range []struct {
		passed map[string]bool
		ndiff  int
	}{
		{set("TestA"), 0},
		{set("TestA", "TestA/row1"), 0},
		{set(), 1},                   // TestA missing
		{set("TestA", "TestU"), 1},   // stale unsupported entry
		{set("TestA", "TestF"), 1},   // fails under legacy
		{set("TestA", "TestNew"), 1}, // absent from the original
	} {
		if got := compareWithOracle(c, want, tc.passed, "", nil); len(got) != tc.ndiff {
			t.Errorf("passed=%v: got %v, want %d divergence(s)", tc.passed, got, tc.ndiff)
		}
	}
	main := portCase{Rel: "M.t"}
	if d := compareWithOracle(main, oracle{Stdout: "x\n", ExitOK: true}, nil, "x\n", nil); len(d) != 0 {
		t.Errorf("identical main: %v", d)
	}
	if d := compareWithOracle(main, oracle{Stdout: "x\n", ExitOK: true}, nil, "x", nil); len(d) != 1 {
		t.Errorf("stdout differs: %v", d)
	}
	if got := junitTests(t, writeTemp(t, "/* @Test\npublic void testA() {} */\n// @Test\n@Test\npublic void testB() { String s = \"/*\"; }\n@Test\npublic void testC() {}\n")); strings.Join(got, ",") != "testB,testC" {
		t.Errorf("junitTests = %v", got)
	}
}

func writeTemp(t *testing.T, content string) string {
	p := filepath.Join(t.TempDir(), "x.t")
	if err := os.WriteFile(p, []byte(content), 0o644); err != nil {
		t.Fatal(err)
	}
	return p
}

// TestOracleGuards pins the vacuity and staleness guards (fix round 1).
func TestOracleGuards(t *testing.T) {
	hasDiff := func(diffs []string, sub string) bool {
		for _, d := range diffs {
			if strings.Contains(d, sub) {
				return true
			}
		}
		return false
	}
	junit := portCase{Rel: "X.t", GoTest: true}
	if d := compareWithOracle(junit, oracle{}, map[string]bool{}, "", nil); !hasDiff(d, "vacuous") {
		t.Errorf("empty oracle vs empty Go run: %v, want a vacuous-oracle divergence", d)
	}
	allUnsupported := portCase{Rel: "X.t", GoTest: true, Unsupported: map[string]string{"TestU": "why"}}
	if d := compareWithOracle(allUnsupported, oracle{Passed: []string{"TestU"}}, map[string]bool{}, "", nil); !hasDiff(d, "vacuous") {
		t.Errorf("all-unsupported oracle: %v, want a vacuous-oracle divergence", d)
	}
	fully := allUnsupported
	fully.FullyUnsupported = true
	if d := compareWithOracle(fully, oracle{Passed: []string{"TestU"}}, map[string]bool{}, "", nil); len(d) != 0 {
		t.Errorf("FullyUnsupported port: %v, want no divergence", d)
	}
	if d := compareWithOracle(fully, oracle{Passed: []string{"TestU", "TestA"}}, map[string]bool{}, "", nil); !hasDiff(d, "FullyUnsupported") {
		t.Errorf("FullyUnsupported with a required test: %v, want a divergence", d)
	}
	if d := compareWithOracle(junit, oracle{Passed: []string{"TestA"}}, map[string]bool{}, "build failed", errors.New("exit status 1")); !hasDiff(d, "no Go test passed") {
		t.Errorf("failed go test with zero passes: %v, want an explicit report", d)
	}

	src := writeTemp(t, "@Test\npublic void testA() {}\n/* @Test\npublic void testZ() {} */\n@Test\npublic void testB() {}\n")
	sum, err := sourceSHA256(src)
	if err != nil {
		t.Fatal(err)
	}
	fresh := oracle{Passed: []string{"TestA"}, Failed: []string{"TestB"}, SourceSHA256: sum}
	if err := oracleStale(junit, fresh, src); err != nil {
		t.Errorf("fresh oracle reported stale: %v", err)
	}
	missing := fresh
	missing.Failed = nil
	if err := oracleStale(junit, missing, src); err == nil {
		t.Error("oracle missing testB not reported stale")
	}
	extra := fresh
	extra.Passed = []string{"TestA", "TestZ"}
	if err := oracleStale(junit, extra, src); err == nil {
		t.Error("oracle with commented-out testZ not reported stale")
	}
	edited := fresh
	edited.SourceSHA256 = "0000"
	if err := oracleStale(portCase{Rel: "M.t"}, edited, src); err == nil {
		t.Error("main oracle with a wrong source hash not reported stale")
	}
	if err := oracleStale(portCase{Rel: "M.t"}, oracle{SourceSHA256: sum, ExitOK: true}, src); err != nil {
		t.Errorf("fresh main oracle reported stale: %v", err)
	}
}

// TestCasesCoverPorts keeps cases, the ports and the oracle cache in step.
func TestCasesCoverPorts(t *testing.T) {
	inCases := map[string]bool{}
	oracles := map[string]bool{}
	for _, c := range cases {
		inCases[c.Rel] = true
		for _, rel := range c.Support {
			inCases[rel] = true
		}
		for _, g := range c.Gom {
			inCases[g.Rel] = true
			for _, rel := range g.Deps {
				inCases[rel] = true
			}
		}
		oracles[filepath.Base(oraclePath(c))] = true
	}
	filepath.Walk(".", func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() && (p == "testdata" || p == "harness" || p == "javaparity" || p == "smoke" || p == "coverage") {
			return filepath.SkipDir
		}
		if !info.IsDir() && (strings.HasSuffix(p, ".t") || strings.HasSuffix(p, ".gom")) && !inCases[filepath.ToSlash(p)] {
			t.Errorf("port %s is not listed in cases (as Rel, Support or Gom)", p)
		}
		return nil
	})
	files, _ := filepath.Glob(filepath.Join("testdata", "oracle", "*.json"))
	for _, f := range files {
		if !oracles[filepath.Base(f)] {
			t.Errorf("oracle %s belongs to no case", f)
		}
	}
	for _, c := range cases {
		for _, rel := range append([]string{c.Rel}, c.Support...) {
			if _, err := os.Stat(rel); err != nil {
				t.Errorf("case %s has no port %s: %v", c.Rel, rel, err)
			}
		}
	}
}

// TestGomCopiesIdentical: a standalone .gom is an algebraic signature,
// not host code, so the copy the Go side compiles must equal the original.
func TestGomCopiesIdentical(t *testing.T) {
	root := harness.RepoRoot(t)
	for _, c := range cases {
		for _, g := range c.Gom {
			for _, rel := range append([]string{g.Rel}, g.Deps...) {
				orig, err := os.ReadFile(filepath.Join(root, "test", rel))
				if err != nil {
					t.Fatal(err)
				}
				cp, err := os.ReadFile(rel)
				if err != nil {
					t.Errorf("%s: missing copy of test/%s: %v", c.Rel, rel, err)
					continue
				}
				if rel == g.Rel && g.PortedHooks {
					o, err1 := gomWithoutHookBodies(orig)
					c, err2 := gomWithoutHookBodies(cp)
					if err1 != nil || err2 != nil {
						t.Errorf("%s: parsing: %v %v", rel, err1, err2)
					} else if o != c {
						t.Errorf("test/go/%s differs from test/%s outside its hook bodies:\n%s\n%s", rel, rel, o, c)
					}
					continue
				}
				if string(orig) != string(cp) {
					t.Errorf("test/go/%s differs from test/%s", rel, rel)
				}
			}
		}
	}
}

// hookCode matches the code of a hook in the printed Gom AST, origin a
// line number (which the ported hook bodies may shift).
var (
	hookCode = regexp.MustCompile(`HookCode\("(?:[^"\\]|\\.)*"\)`)
	origin   = regexp.MustCompile(`Origin\([0-9]+\)`)
)

// gomWithoutHookBodies is the printed AST of a .gom with its hook code
// blanked (hook names, kinds and arguments are kept).
func gomWithoutHookBodies(src []byte) (string, error) {
	mod, err := gomparser.ParseBytes(src)
	if err != nil {
		return "", err
	}
	return origin.ReplaceAllString(hookCode.ReplaceAllString(fmt.Sprint(mod), `HookCode("")`), "Origin(0)"), nil
}

// TestCapitalise pins the JUnit -> Go test names.
func TestCapitalise(t *testing.T) {
	for in, want := range map[string]string{
		"testFoo": "TestFoo", "testfoo": "TestFoo", "test1": "Test1",
		"testcountAll1": "TestCountAll1", "foo": "Foo",
	} {
		if got := capitalise(in); got != want {
			t.Errorf("capitalise(%q) = %q, want %q", in, got, want)
		}
	}
}
