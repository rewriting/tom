package backend

import (
	"bytes"
	"os"
	"path/filepath"
	"testing"

	"tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

// TestSelfBootstrap regenerates the gomast package from
// src/tom/gom/adt/*.gom using the *current* (gomast-native) backend
// and verifies that every .go file is byte-identical to the committed
// internal/gomast/. This is the strongest "behaviour is conserved"
// proof I can write: it shows that tomgo can re-emit its own AST
// package, and the output of doing so is stable.
//
// If you intentionally change the backend (e.g. the String() format
// of an alternative), this test will fail until you regenerate the
// committed gomast — that is by design.
func TestSelfBootstrap(t *testing.T) {
	repo, err := locateRepoRootFor(t)
	if err != nil {
		t.Skipf("repo root not found: %v", err)
	}
	adtDir := filepath.Join(repo, "tomgo", "src", "gom", "adt")
	committedDir := filepath.Join(repo, "tomgo", "stable", "library", "gomast")

	files := []string{"Code.gom", "Gom.gom", "Objects.gom", "Rule.gom", "SymbolTable.gom"}
	var modules []gomast.GomModule
	for _, f := range files {
		m, err := gom.ParseFile(filepath.Join(adtDir, f))
		if err != nil {
			t.Fatal(err)
		}
		modules = append(modules, m)
	}

	outDir := t.TempDir()
	if _, err := GenerateBatchToDir(modules, Options{PackageName: "gomast"}, outDir); err != nil {
		t.Fatalf("GenerateBatchToDir: %v", err)
	}

	for _, name := range []string{"code.go", "gom.go", "objects.go", "rule.go", "symboltable.go"} {
		regen, err := os.ReadFile(filepath.Join(outDir, name))
		if err != nil {
			t.Fatalf("read regenerated %s: %v", name, err)
		}
		committed, err := os.ReadFile(filepath.Join(committedDir, name))
		if err != nil {
			t.Fatalf("read committed %s: %v", name, err)
		}
		if !bytes.Equal(regen, committed) {
			t.Errorf("%s: regenerated content differs from committed stable/library/gomast/%s — "+
				"either revert the backend change or re-commit the regenerated gomast", name, name)
		}
	}
}
