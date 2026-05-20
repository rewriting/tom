package backend

import (
	"os"
	"os/exec"
	"path/filepath"
	"testing"

	"tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

// TestGenerate_ADT verifies that the batch-mode backend can lower the
// real Gom ADT (`src/tom/gom/adt/*.gom`) — five cross-importing modules,
// 63 sorts in total, one `sort HookList:block()` hook — to a single Go
// package that compiles AND whose ContainsTomCode() method behaves as
// the Tom source describes.
func TestGenerate_ADT(t *testing.T) {
	repo, err := locateRepoRootFor(t)
	if err != nil {
		t.Skipf("repo root not located: %v", err)
	}
	adtDir := filepath.Join(repo, "src", "tom", "gom", "adt")
	entries, err := os.ReadDir(adtDir)
	if err != nil {
		t.Fatal(err)
	}
	var modules []gomast.GomModule
	for _, e := range entries {
		if filepath.Ext(e.Name()) != ".gom" {
			continue
		}
		m, err := gom.ParseFile(filepath.Join(adtDir, e.Name()))
		if err != nil {
			t.Fatalf("parse %s: %v", e.Name(), err)
		}
		modules = append(modules, m)
	}
	if len(modules) != 5 {
		t.Fatalf("expected 5 .gom files under %s, got %d", adtDir, len(modules))
	}

	outDir := t.TempDir()
	if _, err := GenerateBatchToDir(modules, Options{PackageName: "adt"}, outDir); err != nil {
		t.Fatalf("GenerateBatchToDir: %v", err)
	}
	if out, err := exec.Command("go", "build", "./...").CombinedOutput(); err != nil {
		_ = out // for clarity below
	}
	cmd := exec.Command("go", "build", "./...")
	cmd.Dir = outDir
	if out, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("go build failed:\n%s", out)
	}

	// Drop in a small _test.go that exercises ContainsTomCode in two
	// ways: a list whose hooks all have HasTomCode=false (expect
	// false) and a list with one MakeHook HasTomCode=true (expect
	// true). The slot structure of Hook follows Objects.gom.
	testProgram := `package adt

import "testing"

func TestContainsTomCode_NoTomCode(t *testing.T) {
	empty := MakeConcSlotField()                                                  // empty SlotFieldList
	codeProg := MakeCode("// trivial Go body")                                   // Code( prog:String )
	h1 := MakeBlockHook(codeProg, false)
	h2 := MakeInterfaceHook(codeProg)
	list := MakeConcHook(h1, h2)
	if list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = false, got true")
		_ = empty
	}
}

func TestContainsTomCode_FromBlockHook(t *testing.T) {
	code := MakeCode("// pretend this contains %match …")
	mh := MakeBlockHook(code, true)
	list := MakeConcHook(mh)
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on BlockHook with HasTomCode=true")
	}
}

func TestContainsTomCode_FromMakeHook(t *testing.T) {
	args := MakeConcSlotField()
	code := MakeCode("// body")
	list := MakeConcHook(MakeMakeHook(args, code, true))
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on MakeHook")
	}
}

func TestContainsTomCode_FromMakeBeforeHook(t *testing.T) {
	args := MakeConcSlotField()
	code := MakeCode("// body")
	list := MakeConcHook(MakeMakeBeforeHook(args, code, true))
	if !list.ContainsTomCode() {
		t.Fatal("expected ContainsTomCode() = true on MakeBeforeHook")
	}
}

func TestContainsTomCode_NonHasTomCodeKind(t *testing.T) {
	// InterfaceHook / ImportHook / MappingHook have no HasTomCode
	// field, so they must never contribute to ContainsTomCode.
	code := MakeCode("hi")
	list := MakeConcHook(MakeInterfaceHook(code), MakeImportHook(code), MakeMappingHook(code))
	if list.ContainsTomCode() {
		t.Fatal("hooks without HasTomCode must not be reported")
	}
}
`
	if err := os.WriteFile(filepath.Join(outDir, "adt_test.go"), []byte(testProgram), 0o644); err != nil {
		t.Fatal(err)
	}
	cmd = exec.Command("go", "test", "./...")
	cmd.Dir = outDir
	if out, err := cmd.CombinedOutput(); err != nil {
		t.Fatalf("go test of generated package failed:\n%s", out)
	}
}

// locateRepoRootFor walks up from the current working directory looking
// for the directory that contains `src/tom/gom/adt`.
func locateRepoRootFor(t *testing.T) (string, error) {
	dir, err := os.Getwd()
	if err != nil {
		return "", err
	}
	for {
		if _, err := os.Stat(filepath.Join(dir, "src", "tom", "gom", "adt")); err == nil {
			return dir, nil
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", os.ErrNotExist
		}
		dir = parent
	}
}
