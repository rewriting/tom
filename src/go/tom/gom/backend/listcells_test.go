package backend_test

import (
	"os"
	"os/exec"
	"path/filepath"
	"testing"

	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

// TestListCells generates testdata/listcells/List.gom (a user module:
// Java Gom's list API on shared cons cells) into a temporary module,
// then runs the tests and benchmarks of testdata/listcells/list_test.go
// there: tail sharing and O(n) cons/GetTail. With -v, the benchmark
// results (ns/op, B/op) are logged.
func TestListCells(t *testing.T) {
	if _, err := exec.LookPath("go"); err != nil {
		t.Skip("go not in PATH")
	}
	if testing.Short() {
		t.Skip("short mode")
	}
	mod, err := gomparser.ParseFile(filepath.Join("testdata", "listcells", "List.gom"))
	if err != nil {
		t.Fatal(err)
	}
	out := t.TempDir()
	dir, err := backend.GenerateToDir(mod, backend.Options{PackageName: "list"}, out)
	if err != nil {
		t.Fatal(err)
	}
	src, err := os.ReadFile(filepath.Join("testdata", "listcells", "list_test.go"))
	if err != nil {
		t.Fatal(err)
	}
	if err := os.WriteFile(filepath.Join(dir, "list_test.go"), src, 0o644); err != nil {
		t.Fatal(err)
	}
	cmd := exec.Command("go", "test", "-count=1", "-bench", ".", "-benchtime", "20x", "-benchmem", ".")
	cmd.Dir = dir
	b, err := cmd.CombinedOutput()
	if err != nil {
		t.Fatalf("list cell tests failed: %v\n%s", err, b)
	}
	t.Logf("%s", b)
}
