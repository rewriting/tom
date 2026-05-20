//go:build mage

// Mage build orchestrator for tomgo.
//
// Usage (from the tomgo/ directory):
//
//	mage <target>           # one of: build, test, vet, regen, promote, …
//	mage -l                 # list available targets
//	mage -h <target>        # detail for one target
//
// Install once with:  go install github.com/magefile/mage@latest
//
// Targets follow the two-step regen flow requested for safety:
//
//	regen   →  produce src/library/{gomast,tomast}/, verify it compiles.
//	            stable/library/ is NOT touched.
//	promote →  copy src/library/{gomast,tomast}/ → stable/library/,
//	            then run the full test suite. Use after manual review
//	            (`diff -r src/library/gomast stable/library/gomast`).
package main

import (
	"fmt"
	"io"
	"os"
	"path/filepath"
	"strings"

	"github.com/magefile/mage/mg"
	"github.com/magefile/mage/sh"
)

// gomADT lists the 5 .gom files describing the Gom AST (Phase 3).
var gomADT = []string{
	"Code.gom",
	"Gom.gom",
	"Objects.gom",
	"Rule.gom",
	"SymbolTable.gom",
}

// tomADT lists the 15 .gom files describing the TOM engine AST (Phase 4.A).
var tomADT = []string{
	"CST.gom",
	"Code.gom",
	"Il.gom",
	"Theory.gom",
	"TomConstraint.gom",
	"TomDeclaration.gom",
	"TomExpression.gom",
	"TomInstruction.gom",
	"TomName.gom",
	"TomOption.gom",
	"TomSignature.gom",
	"TomSlot.gom",
	"TomTerm.gom",
	"TomType.gom",
	"TypeConstraints.gom",
}

// goPackages is the slice of import paths covering everything we care about
// (excludes the deferred test packages with build tags, etc.).
var goPackages = []string{"./cmd/...", "./stable/..."}

// Build compiles every package under cmd/ and stable/.
func Build() error {
	args := append([]string{"build"}, goPackages...)
	return sh.RunV("go", args...)
}

// Test runs the Go test suite (all packages). Java-equivalence tests skip
// cleanly when JDK or stable/dist/lib is unavailable.
func Test() error {
	args := append([]string{"test"}, goPackages...)
	return sh.RunV("go", args...)
}

// Vet runs go vet on every package.
func Vet() error {
	args := append([]string{"vet"}, goPackages...)
	return sh.RunV("go", args...)
}

// Regen regenerates src/library/{gomast,tomast}/ from the .gom sources in
// src/{gom,engine}/adt/. It then verifies the generated code compiles.
// stable/library/ is NOT modified. Use `mage promote` to copy the regen
// result to stable/ once you have reviewed the diff.
func Regen() error {
	mg.SerialDeps(RegenGomAst, RegenTomAst)
	fmt.Println(">>> verifying generated code compiles")
	if err := sh.RunV("go", "build", "./src/library/..."); err != nil {
		return fmt.Errorf("generated code does NOT build — stable/ left untouched: %w", err)
	}
	fmt.Println(">>> regen ok. To inspect changes:")
	fmt.Println("      diff -r src/library/gomast stable/library/gomast")
	fmt.Println("      diff -r src/library/tomast stable/library/tomast")
	fmt.Println(">>> when satisfied, run: mage promote")
	return nil
}

// RegenGomAst regenerates src/library/gomast/ from src/gom/adt/*.gom.
func RegenGomAst() error {
	return regenPackage("gomast", filepath.Join("src", "gom", "adt"), gomADT)
}

// RegenTomAst regenerates src/library/tomast/ from src/engine/adt/*.gom.
func RegenTomAst() error {
	return regenPackage("tomast", filepath.Join("src", "engine", "adt"), tomADT)
}

func regenPackage(pkg, adtDir string, files []string) error {
	outDir := filepath.Join("src", "library", pkg)
	fmt.Printf(">>> clearing %s/\n", outDir)
	if err := os.RemoveAll(outDir); err != nil {
		return err
	}
	if err := os.MkdirAll(outDir, 0o755); err != nil {
		return err
	}
	args := []string{"run", "./cmd/tomgo", "gom-batch", "--pkg", pkg, "-o", outDir}
	for _, f := range files {
		args = append(args, filepath.Join(adtDir, f))
	}
	fmt.Printf(">>> regen %s → %s/\n", pkg, outDir)
	if err := sh.RunV("go", args...); err != nil {
		return err
	}
	// The codegen has no business dropping a go.mod, but if it did we'd
	// see a module-boundary error later. Defensive cleanup:
	_ = os.Remove(filepath.Join(outDir, "go.mod"))
	return nil
}

// Promote copies src/library/{gomast,tomast}/ to stable/library/, overwriting
// the committed bootstrap. Runs `mage regen` first (so the promotion always
// reflects the latest source). After the copy, runs the full test suite to
// confirm the new bootstrap compiles and behaves byte-stable.
//
// `doc.go` files in stable/library/{gomast,tomast}/ (hand-written) are
// preserved.
func Promote() error {
	mg.SerialDeps(Regen)
	if err := promotePackage("gomast"); err != nil {
		return err
	}
	if err := promotePackage("tomast"); err != nil {
		return err
	}
	fmt.Println(">>> running tests to verify promoted bootstrap")
	return Test()
}

func promotePackage(pkg string) error {
	srcDir := filepath.Join("src", "library", pkg)
	dstDir := filepath.Join("stable", "library", pkg)
	fmt.Printf(">>> promoting %s/ → %s/\n", srcDir, dstDir)

	// Wipe stable/library/<pkg>/ of stale generated .go files. Preserve
	// hand-written ones: doc.go and any *_test.go. This way the promote
	// step cleans up any file that a previous regen produced but the
	// current one no longer does, while leaving the hand-written
	// smoke/doc helpers in place.
	entries, err := os.ReadDir(dstDir)
	if err != nil {
		return err
	}
	for _, e := range entries {
		name := e.Name()
		if !e.Type().IsRegular() || filepath.Ext(name) != ".go" {
			continue
		}
		if name == "doc.go" || strings.HasSuffix(name, "_test.go") {
			continue
		}
		if err := os.Remove(filepath.Join(dstDir, name)); err != nil {
			return err
		}
	}
	// Copy every .go from src/library/<pkg>/ to stable/library/<pkg>/.
	srcEntries, err := os.ReadDir(srcDir)
	if err != nil {
		return err
	}
	for _, e := range srcEntries {
		if !e.Type().IsRegular() || filepath.Ext(e.Name()) != ".go" {
			continue
		}
		if err := copyFile(filepath.Join(srcDir, e.Name()), filepath.Join(dstDir, e.Name())); err != nil {
			return fmt.Errorf("copy %s: %w", e.Name(), err)
		}
	}
	return nil
}


func copyFile(src, dst string) error {
	in, err := os.Open(src)
	if err != nil {
		return err
	}
	defer in.Close()
	out, err := os.Create(dst)
	if err != nil {
		return err
	}
	defer out.Close()
	_, err = io.Copy(out, in)
	return err
}

// SelfBootstrap runs the byte-stability tests of stable/library/{gomast,tomast}.
// These tests regenerate the AST packages to a tempdir and compare with the
// committed stable/library/. Useful to confirm that the current backend
// produces output byte-identical to what is in stable/.
func SelfBootstrap() error {
	return sh.RunV("go", "test", "-v", "-run", "TestSelfBootstrap", "./stable/gom/backend/...")
}

// EquivJava runs the Java equivalence harnesses (Gom + TOM parser). Requires
// JDK + stable/dist/lib (produced by /tom/build.sh stable). Skips cleanly
// when missing.
func EquivJava() error {
	fmt.Println(">>> Gom Java equivalence (3 fixtures)")
	if err := sh.RunV("go", "test", "-v", "-run", "TestEquivalence", "./stable/gom/equiv/..."); err != nil {
		return err
	}
	fmt.Println(">>> TOM parser Java equivalence (20+ fixtures)")
	return sh.RunV("go", "test", "-v", "-run", "TestGoParserAgainstJava", "./stable/tom/parser/equiv/...")
}

// All chains the standard checks: vet, build, test, selfBootstrap.
func All() {
	mg.SerialDeps(Vet, Build, Test, SelfBootstrap)
}

// Clean removes the scratch src/library/ directory produced by Regen.
// stable/library/ is NOT touched.
func Clean() error {
	dir := filepath.Join("src", "library")
	fmt.Printf(">>> removing %s/\n", dir)
	return os.RemoveAll(dir)
}
