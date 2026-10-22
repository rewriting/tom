// Package port holds the Tom+Go ports of test/*.t and checks them:
// T1 (generated Go is pinned by golden files and builds) and, from
// Task 8, T2 (same behaviour as the original under legacy/).
package port

import (
	"bufio"
	"encoding/json"
	"flag"
	"os"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

var update = flag.Bool("update", false, "rewrite testdata/golden/*.go from the current compiler")

type portCase struct {
	Rel    string // path relative to test/ (and to tests/port/)
	GoTest bool   // run with `go test` (JUnit-style) rather than `go run`
}

var cases = []portCase{
	{Rel: "Peano.t"},
	{Rel: "TestPeano.t", GoTest: true},
	{Rel: "Array.t"},
	{Rel: "TestArray.t", GoTest: true},
	{Rel: "TestList.t", GoTest: true},
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
