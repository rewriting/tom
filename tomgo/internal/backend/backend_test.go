package backend

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/internal/gom"
)

// TestGenerate_BuildsCorpus is the Phase 2 gate: every .gom in
// testdata/corpus/gom-nohooks must round-trip cleanly through
//
//	parse → generate → go build
//
// We delete the per-file output directory after the assertion to keep
// the test artefact-free.
func TestGenerate_BuildsCorpus(t *testing.T) {
	corpus, err := filepath.Abs(filepath.Join("..", "..", "testdata", "corpus", "gom-nohooks"))
	if err != nil {
		t.Fatal(err)
	}
	entries, err := filepathWalkGom(corpus)
	if err != nil {
		t.Fatal(err)
	}
	if len(entries) == 0 {
		t.Fatal("no .gom in corpus directory")
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

// filepathWalkGom returns every .gom file under root, recursively, in a
// deterministic order. It is the minimal helper this test needs without
// pulling in a heavier dependency.
func filepathWalkGom(root string) ([]string, error) {
	var out []string
	err := filepath.Walk(root, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() {
			return nil
		}
		if strings.HasSuffix(path, ".gom") {
			out = append(out, path)
		}
		return nil
	})
	return out, err
}
