package backend

import (
	"os"
	"path/filepath"
	"strings"
	"testing"

	gomast "tom/gom/adt"
	gom "tom/gom/parser"
)

func TestIsTomModule(t *testing.T) {
	cases := map[string]bool{
		"module tom\n\ngo 1.26\n":       true,
		"  module tom  \n":              true,
		"module tom/tomgo\n\ngo 1.26\n": false,
		"module tomtest\n":              false,
		"// module tom\nmodule x\n":     false,
	}
	for in, want := range cases {
		if got := isTomModule([]byte(in)); got != want {
			t.Errorf("isTomModule(%q) = %v, want %v", in, got, want)
		}
	}
}

func TestLocateTomModuleRoot_FindsModuleTom(t *testing.T) {
	root, err := locateTomModuleRoot()
	if err != nil {
		t.Fatal(err)
	}
	gm, err := os.ReadFile(filepath.Join(root, "go.mod"))
	if err != nil || !isTomModule(gm) {
		t.Fatalf("%s/go.mod does not declare module tom (err=%v)", root, err)
	}
}

// Outside module tom, generation must fail loudly instead of writing a
// go.mod whose `replace tom => ..` cannot resolve tom/library/...
func TestGenerate_FailsOutsideTomModule(t *testing.T) {
	mod, err := gom.Parse(strings.NewReader("module m.M\nabstract syntax\nT = a()\n"))
	if err != nil {
		t.Fatal(err)
	}
	outDir := t.TempDir()
	t.Chdir(t.TempDir())
	if _, err := locateTomModuleRoot(); err == nil {
		t.Fatal("locateTomModuleRoot: expected an error outside module tom")
	}
	if _, err := GenerateToDir(mod, Options{}, filepath.Join(outDir, "single")); err == nil {
		t.Error("GenerateToDir: expected an error outside module tom")
	}
	if _, err := GenerateBatchToDir([]gomast.GomModule{mod}, Options{PackageName: "m"}, filepath.Join(outDir, "batch")); err == nil {
		t.Error("GenerateBatchToDir: expected an error outside module tom")
	}
}
