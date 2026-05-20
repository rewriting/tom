package backend

import (
	"bytes"
	"os"
	"path/filepath"
	"testing"

	"tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

// TestSelfBootstrap_Tomast regenerates the tomast package from
// src/tom/engine/adt/*.gom and asserts byte-identity with the
// committed internal/tomast/. Counterpart of TestSelfBootstrap (for
// gomast). The 15 module files of the TOM engine ADT include 9 hooks
// of various flavours (AU, make, make_insert, module:rules), so this
// test also exercises the knownHookTable entries that lower those
// hooks to Go.
func TestSelfBootstrap_Tomast(t *testing.T) {
	repo, err := locateRepoRootFor(t)
	if err != nil {
		t.Skipf("repo root not found: %v", err)
	}
	adtDir := filepath.Join(repo, "tomgo", "src", "engine", "adt")
	committedDir := filepath.Join(repo, "tomgo", "stable", "library", "tomast")

	files := []string{
		"CST.gom", "Code.gom", "Il.gom", "Theory.gom",
		"TomConstraint.gom", "TomDeclaration.gom", "TomExpression.gom",
		"TomInstruction.gom", "TomName.gom", "TomOption.gom",
		"TomSignature.gom", "TomSlot.gom", "TomTerm.gom",
		"TomType.gom", "TypeConstraints.gom",
	}
	var modules []gomast.GomModule
	for _, f := range files {
		m, err := gom.ParseFile(filepath.Join(adtDir, f))
		if err != nil {
			t.Fatal(err)
		}
		modules = append(modules, m)
	}

	outDir := t.TempDir()
	if _, err := GenerateBatchToDir(modules, Options{PackageName: "tomast"}, outDir); err != nil {
		t.Fatalf("GenerateBatchToDir: %v", err)
	}

	// Each .gom file gets a lowercased name in the output.
	expectedGoNames := []string{
		"cst.go", "code.go", "il.go", "theory.go",
		"tomconstraint.go", "tomdeclaration.go", "tomexpression.go",
		"tominstruction.go", "tomname.go", "tomoption.go",
		"tomsignature.go", "tomslot.go", "tomterm.go",
		"tomtype.go", "typeconstraints.go",
	}
	for _, name := range expectedGoNames {
		regen, err := os.ReadFile(filepath.Join(outDir, name))
		if err != nil {
			t.Fatalf("read regenerated %s: %v", name, err)
		}
		committed, err := os.ReadFile(filepath.Join(committedDir, name))
		if err != nil {
			t.Fatalf("read committed %s: %v", name, err)
		}
		if !bytes.Equal(regen, committed) {
			t.Errorf("%s: regenerated content differs from committed stable/library/tomast/%s — "+
				"either revert the backend change or re-commit the regenerated tomast", name, name)
		}
	}
}
