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

	"tom/tomgo/tests/tomjava"
)

var update = flag.Bool("update", false, "rewrite testdata/golden/*.go from the current compiler")

type portCase struct {
	Rel    string // path relative to test/ (and to tests/port/)
	GoTest bool   // run with `go test` (JUnit-style) rather than `go run`
	// Unsupported lists the original tests (Go-capitalised name ->
	// reason) that the port keeps commented out under a
	// `// port: unsupported` note because the Go runtime cannot express
	// them. T2 exempts them from the legacy pass set, and fails if one
	// of them exists and passes in Go (stale entry).
	Unsupported map[string]string
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
	{Rel: "TestStrategy.t", GoTest: true, Unsupported: map[string]string{
		"TestPosition": "needs the environment-based visit (getEnvironment().getPosition()) and sl.Position, absent from the Go sl",
	}},
}

func base(c portCase) string { return strings.TrimSuffix(filepath.Base(c.Rel), ".t") }

func compileCase(t *testing.T, c portCase) string {
	t.Helper()
	dist := tomjava.NewDist(t)
	dir := t.TempDir()
	src, err := filepath.Abs(c.Rel)
	if err != nil {
		t.Fatal(err)
	}
	if out, err := dist.Compile(t, src, dir, "-go", "-np"); err != nil {
		t.Fatalf("tom -go failed: %v\n%s", err, out)
	}
	gen := filepath.Join(dir, base(c)+".go")
	if c.GoTest {
		if err := os.Rename(gen, filepath.Join(dir, base(c)+"_test.go")); err != nil {
			t.Fatal(err)
		}
		gen = filepath.Join(dir, base(c)+"_test.go")
	}
	checkGolden(t, c, gen)
	tomjava.SetupGoModule(t, dir)
	return dir
}

func checkGolden(t *testing.T, c portCase, gen string) {
	t.Helper()
	got, err := os.ReadFile(gen)
	if err != nil {
		t.Fatal(err)
	}
	golden := filepath.Join("testdata", "golden", strings.ReplaceAll(strings.TrimSuffix(c.Rel, ".t"), "/", "_")+".go")
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
		out, err := tomjava.RunIn(dir, nil, "go", "run", ".")
		return nil, out, err
	}
	out, err := tomjava.RunIn(dir, nil, "go", "test", "-json", "-count=1", ".")
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
	writeDashboard = flag.Bool("write-dashboard", false, "rewrite ../../reports/port-dashboard.md")
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

func capitalise(name string) string {
	if name == "" {
		return name
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
	if out, err := tomjava.RunIn(dir, nil, "javac", "-nowarn", "-cp", cp, "-d", dir, lsrc); err != nil {
		t.Fatalf("javac listener failed: %v\n%s", err, out)
	}
	out, err := tomjava.RunIn(dir, nil, "java", "-cp", cp, "T2JUnitListener", class)
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
		if err := oracleStale(c, o, filepath.Join(tomjava.RepoRoot(t), "test", c.Rel)); err != nil {
			t.Fatal(err)
		}
		return o
	}
	legacy := tomjava.LegacyDist(t)
	root := tomjava.RepoRoot(t)
	src := filepath.Join(root, "test", c.Rel)
	dir := t.TempDir()
	if out, err := legacy.Compile(t, src, dir); err != nil {
		t.Fatalf("legacy tom failed: %v\n%s", err, out)
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
	if out, err := tomjava.RunIn(dir, nil, "javac", append([]string{"-nowarn", "-encoding", "UTF-8", "-cp", cp, "-d", dir}, javaFiles...)...); err != nil {
		t.Fatalf("javac failed: %v\n%s", err, out)
	}
	class := base(c)
	if pkg := javaPackage(t, src); pkg != "" {
		class = pkg + "." + class
	}
	var o oracle
	sum, err := sourceSHA256(src)
	if err != nil {
		t.Fatal(err)
	}
	o.SourceSHA256 = sum
	if c.GoTest {
		runJUnit(t, c, src, dir, cp, class, &o)
	} else {
		out, err := tomjava.RunIn(dir, nil, "java", "-cp", cp, class)
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

func sourceSHA256(path string) (string, error) {
	f, err := os.Open(path)
	if err != nil {
		return "", err
	}
	defer f.Close()
	h := sha256.New()
	if _, err := io.Copy(h, f); err != nil {
		return "", err
	}
	return hex.EncodeToString(h.Sum(nil)), nil
}

// oracleStale checks, without Java, that a cached oracle still matches
// the original source src.
func oracleStale(c portCase, o oracle, src string) error {
	sum, err := sourceSHA256(src)
	if err != nil {
		return err
	}
	if sum != o.SourceSHA256 {
		return fmt.Errorf("oracle stale: %s changed since the oracle was computed (run with -update-oracle)", src)
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
	if required == 0 {
		diffs = append(diffs, "vacuous oracle: no legacy-passing test remains required once Unsupported is removed")
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
// -write-dashboard, rewrites reports/port-dashboard.md.
func TestDashboard(t *testing.T) {
	if !*writeDashboard {
		t.Skip("run with -write-dashboard")
	}
	root := tomjava.RepoRoot(t)
	ported := map[string]portCase{}
	for _, c := range cases {
		ported[c.Rel] = c
	}
	var rows []string
	counts := map[string]int{}
	filepath.Walk(filepath.Join(root, "test"), func(p string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() || !strings.HasSuffix(p, ".t") {
			return err
		}
		rel, _ := filepath.Rel(filepath.Join(root, "test"), p)
		rel = filepath.ToSlash(rel)
		if strings.HasPrefix(rel, "error/") {
			return nil
		}
		status, note := "non porté", ""
		if c, ok := ported[rel]; ok {
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
	b.WriteString("Généré par `mage portGo` (`go test ./tests/port/ -args -write-dashboard`).\n")
	b.WriteString("Chaque `test/**/*.t` hors `error/` ; un cas porté est OK quand son port\n")
	b.WriteString("Tom+Go, compilé par `tom -go`, se comporte comme l'original compilé par\n")
	b.WriteString("`legacy/` (mêmes tests JUnit réussis, ou même sortie et même statut).\n\n")
	b.WriteString("- total : " + strconv.Itoa(len(rows)) + "\n")
	for _, s := range []string{"OK", "divergence", "échec go test", "échec go build", "échec tom", "oracle invalide", "non porté"} {
		b.WriteString("- " + s + " : " + strconv.Itoa(counts[s]) + "\n")
	}
	b.WriteString("\n| Cas | Statut | Notes |\n|---|---|---|\n" + strings.Join(rows, "\n") + "\n")
	if err := os.WriteFile(filepath.Join(root, "tomgo", "reports", "port-dashboard.md"), []byte(b.String()), 0o644); err != nil {
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
		dist := tomjava.NewDist(t)
		dir := t.TempDir()
		src, err := filepath.Abs(c.Rel)
		if err != nil {
			t.Fatal(err)
		}
		if _, err := dist.Compile(t, src, dir, "-go", "-np"); err != nil {
			return
		}
		if c.GoTest {
			gen := filepath.Join(dir, base(c)+".go")
			if err := os.Rename(gen, filepath.Join(dir, base(c)+"_test.go")); err != nil {
				return
			}
		}
		tomjava.SetupGoModule(t, dir)
		buildArgs := []string{"build", "."}
		if c.GoTest {
			buildArgs = []string{"test", "-c", "-o", os.DevNull, "."}
		}
		if _, err := tomjava.RunIn(dir, nil, "go", buildArgs...); err != nil {
			status = "échec go build"
			return
		}
		passed, out, err := runCase(t, c, dir)
		switch {
		case c.GoTest && err != nil && len(passed) == 0:
			status = "échec go test"
		case len(compareWithOracle(c, want, passed, out, err)) > 0:
			status = "divergence"
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
		oracles[filepath.Base(oraclePath(c))] = true
	}
	filepath.Walk(".", func(p string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() && (p == "aterm" || p == "testdata") {
			return filepath.SkipDir
		}
		if !info.IsDir() && strings.HasSuffix(p, ".t") && !inCases[filepath.ToSlash(p)] {
			t.Errorf("port %s is not listed in cases", p)
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
		if _, err := os.Stat(c.Rel); err != nil {
			t.Errorf("case %s has no port: %v", c.Rel, err)
		}
	}
}
