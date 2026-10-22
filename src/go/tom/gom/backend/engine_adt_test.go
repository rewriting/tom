package backend_test

import (
	"os"
	"os/exec"
	"path/filepath"
	"testing"

	gomast "tom/gom/adt"
	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

// TestEngineADTCompiles generates the Tom engine ADT (src/tom/engine/adt/*.gom,
// with its hooks) into a temporary module and builds it. It replaces the
// prototype's committed-tomast tests: the validated tree does not ship tomast,
// but the backend must keep handling the engine ADT for the future Tom+Go
// compiler.
func TestEngineADTCompiles(t *testing.T) {
	if _, err := exec.LookPath("go"); err != nil {
		t.Skip("go not in PATH")
	}
	root := repoRoot(t)
	files, err := filepath.Glob(filepath.Join(root, "src", "tom", "engine", "adt", "*.gom"))
	if err != nil || len(files) == 0 {
		t.Fatalf("no engine .gom files: %v", err)
	}
	var mods []gomast.GomModule
	for _, f := range files {
		m, err := gomparser.ParseFile(f)
		if err != nil {
			t.Fatalf("%s: %v", f, err)
		}
		mods = append(mods, m)
	}
	out := t.TempDir()
	if _, err := backend.GenerateBatchToDir(mods, backend.Options{PackageName: "tomast"}, out); err != nil {
		t.Fatal(err)
	}
	cmd := exec.Command("go", "build", "./...")
	cmd.Dir = out
	if b, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("generated engine ADT does not build: %v\n%s", err, b)
	}
}

func repoRoot(t *testing.T) string {
	t.Helper()
	dir, _ := os.Getwd()
	for {
		if _, err := os.Stat(filepath.Join(dir, "build.xml")); err == nil {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			t.Fatal("repository root not found")
		}
		dir = parent
	}
}
