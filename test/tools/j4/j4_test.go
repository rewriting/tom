// Package j4 holds the tests of milestone J4 that need a built dist
// (the J4 spec: docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md).
package j4

import (
	"os"
	"path/filepath"
	"testing"

	"tomtools/harness"
)

// buildAgainstSrc writes in dir a go.mod whose module tom is the source
// tree src/go/tom (the dist ships only the runtime, not the engine ADT)
// and tidies it.
func buildAgainstSrc(t *testing.T, dir string) {
	t.Helper()
	root := harness.RepoRoot(t)
	mod := "module adtuse\n\ngo 1.26\n\nrequire tom v0.0.0\n\nreplace tom => " +
		filepath.Join(root, "src", "go", "tom") + "\n"
	if err := os.WriteFile(filepath.Join(dir, "go.mod"), []byte(mod), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := harness.RunIn(dir, []string{"GOWORK=off"}, "go", "mod", "tidy"); err != nil {
		t.Fatalf("go mod tidy: %v\n%s", err, out)
	}
}

// TestEngineADTFromTomGo compiles test/go/j4/testdata/adtuse/adtuse.t
// with `tom -go -np -I src/go/tom/engine/adt`, builds it in a module
// whose tom is src/go/tom (the dist ships only the runtime) and checks
// its output on term.txt.
func TestEngineADTFromTomGo(t *testing.T) {
	harness.LookPath(t, "go")
	root := harness.RepoRoot(t)
	dist := harness.NewDist(t)
	adtDir := filepath.Join(root, "src", "go", "tom", "engine", "adt")
	if _, err := os.Stat(filepath.Join(adtDir, "adt.tom")); err != nil {
		t.Fatalf("no adt.tom in %s: run mage bootstrap", adtDir)
	}
	data := filepath.Join(harness.GoTestDir(root), "j4", "testdata", "adtuse")
	want, err := os.ReadFile(filepath.Join(data, "expected.txt"))
	if err != nil {
		t.Fatal(err)
	}
	out := t.TempDir()
	if msg, err := dist.Compile(t, filepath.Join(data, "adtuse.t"), out, "-go", "-np", "-I", adtDir); err != nil {
		t.Fatalf("tom -go: %v\n%s", err, msg)
	}
	buildAgainstSrc(t, out)
	got, err := harness.RunIn(out, []string{"GOWORK=off"}, "go", "run", ".", filepath.Join(data, "term.txt"))
	if err != nil {
		t.Fatalf("go run: %v\n%s", err, got)
	}
	if got != string(want) {
		t.Fatalf("output %q, want %q", got, want)
	}
}
