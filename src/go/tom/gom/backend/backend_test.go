package backend

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	"tom/gom/parser"
)

// gomNoHooksCorpus lists the hook-free .gom files of the repository's
// test/gom/ directory that make up the Phase 2 corpus: parse, generate
// and build must succeed for each of them without any hook support.
var gomNoHooksCorpus = []string{
	"Builtin.gom", "Dotted.gom", "Imported.gom", "Importing.gom",
	"Leaf.gom", "List.gom", "Minimal.gom", "Yang.gom", "Ying.gom",
	"fromterm/foo.gom",
}

// TestGenerate_BuildsCorpus is the Phase 2 gate: every hook-free .gom of
// test/gom/ listed in gomNoHooksCorpus must round-trip cleanly through
//
//	parse → generate → go build
//
// We delete the per-file output directory after the assertion to keep
// the test artefact-free.
func TestGenerate_BuildsCorpus(t *testing.T) {
	corpus, err := filepath.Abs(filepath.Join("..", "..", "..", "..", "..", "test", "gom"))
	if err != nil {
		t.Fatal(err)
	}
	var entries []string
	for _, name := range gomNoHooksCorpus {
		path := filepath.Join(corpus, name)
		if _, err := os.Stat(path); err != nil {
			t.Fatalf("corpus file %s is missing (expected in the repository's test/gom/): %v", path, err)
		}
		entries = append(entries, path)
	}
	for _, path := range entries {
		path := path
		name := strings.TrimSuffix(filepath.Base(path), ".gom")
		t.Run(name, func(t *testing.T) {
			mod, err := gom.ParseFile(path)
			if err != nil {
				t.Fatal(err)
			}
			outDir := t.TempDir()
			if _, err := GenerateToDir(mod, Options{}, outDir); err != nil {
				t.Fatal(err)
			}
			cmd := exec.Command("go", "build", "./...")
			cmd.Dir = outDir
			if out, err := cmd.CombinedOutput(); err != nil {
				t.Fatalf("go build failed for %s:\n%s", path, out)
			}
		})
	}
}

// TestGenerate_ProvesSharing generates a small fixture and runs a Go
// program against it that exercises MakeXxx and verifies that two
// structurally equal terms share the same pointer.
func TestGenerate_ProvesSharing(t *testing.T) {
	src := `module test.Peano
abstract syntax
Nat = zero()
    | suc(pred:Nat)
`
	mod, err := gom.ParseBytes([]byte(src))
	if err != nil {
		t.Fatal(err)
	}
	outDir := t.TempDir()
	if _, err := GenerateToDir(mod, Options{}, outDir); err != nil {
		t.Fatal(err)
	}
	// Drop a *_test.go file that exercises sharing and stats.
	testSrc := `package peano

import "testing"

func TestSharing(t *testing.T) {
	z1 := MakeZero()
	z2 := MakeZero()
	if z1 != z2 {
		t.Fatalf("zero() is not shared: %p vs %p", z1, z2)
	}
	s1 := MakeSuc(MakeSuc(z1))
	s2 := MakeSuc(MakeSuc(MakeZero()))
	if s1 != s2 {
		t.Fatalf("suc(suc(zero())) is not shared: %p vs %p", s1, s2)
	}
	// Distinct values must not share.
	if MakeSuc(z1) == MakeSuc(MakeSuc(z1)) {
		t.Fatal("suc(zero()) and suc(suc(zero())) must differ")
	}
	if got := Factory().Stats().NumTerms; got != 3 {
		t.Fatalf("expected 3 canonical terms (zero, suc(zero), suc(suc(zero))), got %d", got)
	}
}
`
	if err := os.WriteFile(filepath.Join(outDir, "peano_test.go"), []byte(testSrc), 0o644); err != nil {
		t.Fatal(err)
	}
	cmd := exec.Command("go", "test", "./...")
	cmd.Dir = outDir
	if out, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("go test failed:\n%s", out)
	}
}
