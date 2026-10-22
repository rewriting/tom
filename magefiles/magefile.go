//go:build mage

// Root build orchestration for the Go side of Tom.
//
// Run from the repository root:
//
//	mage -d magefiles -w . -l        # list targets
//	mage -d magefiles -w . all       # vet + unit tests + self-bootstrap
//
// The validated Go tree is module tom (src/go/tom); the Tom+Go tests are
// module tomtest (test/go). The frozen prototype (prototype/tomgo) keeps
// its own magefile.
package main

import (
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"strings"

	"github.com/magefile/mage/mg"
)

const (
	tomDir    = "src/go/tom"
	testGoDir = "test/go"
)

// runIn runs name with args in dir (relative to the repository root),
// streaming its output.
func runIn(dir, name string, args ...string) error {
	cmd := exec.Command(name, args...)
	cmd.Dir = dir
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	if mg.Verbose() {
		fmt.Println("exec (in "+dir+"):", name, strings.Join(args, " "))
	}
	return cmd.Run()
}

// rel returns files relative to base.
func rel(base string, files []string) []string {
	out := make([]string, 0, len(files))
	for _, f := range files {
		r, err := filepath.Rel(base, f)
		if err != nil {
			r = f
		}
		out = append(out, r)
	}
	return out
}

// Test runs the unit tests of the validated Go tree (module tom).
func Test() error { return runIn(tomDir, "go", "test", "-count=1", "./...") }

// Vet vets the validated Go tree and the Tom+Go test harnesses.
func Vet() error {
	if err := runIn(tomDir, "go", "vet", "./..."); err != nil {
		return err
	}
	return runIn(testGoDir, "go", "vet", "./...")
}

// SelfBootstrap checks that tom/gom/adt regenerates byte-identically.
func SelfBootstrap() error {
	return runIn(tomDir, "go", "test", "-count=1", "-run", "TestSelfBootstrap", "./gom/backend/")
}

// RegenGomAdt regenerates src/go/tom/gom/adt from src/tom/gom/adt/*.gom.
func RegenGomAdt() error {
	files, err := filepath.Glob("src/tom/gom/adt/*.gom")
	if err != nil || len(files) == 0 {
		return fmt.Errorf("no .gom in src/tom/gom/adt: %v", err)
	}
	args := append([]string{"run", "./gom/cmd/gomgo", "gom-batch", "--pkg", "adt", "-o", "gom/adt"}, rel(tomDir, files)...)
	if err := runIn(tomDir, "go", args...); err != nil {
		return err
	}
	// gom-batch drops a stand-alone go.mod next to the generated package;
	// inside module tom it would cut gom/adt off the module.
	if err := os.Remove(filepath.Join(tomDir, "gom", "adt", "go.mod")); err != nil && !os.IsNotExist(err) {
		return err
	}
	return nil
}

// PortJava runs T0: the root compiler's Java output must equal legacy/'s
// on every test/*.t (needs legacy/src/dist and src/dist built).
func PortJava() error {
	return runIn(testGoDir, "go", "test", "-count=1", "-timeout", "30m", "./javaparity/...")
}

// PortGo runs T1 (golden Go output, build and run) on the Tom+Go ports
// in test/go/ (needs src/dist built), T2 (behaviour vs the legacy/
// oracle cached in test/go/testdata/oracle/; refresh it with
// `go test ./ -run TestSameBehaviourAsLegacy -args -update-oracle` in
// test/go) and rewrites doc/go/port-dashboard.md.
func PortGo() error {
	return runIn(testGoDir, "go", "test", "-count=1", "./", "-args", "-write-dashboard")
}

// Coverage runs T3 and rewrites doc/go/coverage.md.
func Coverage() error {
	return runIn(testGoDir, "go", "test", "-count=1", "-v", "./coverage/...", "-args", "-write-coverage")
}

// All runs vet, the unit tests and the self-bootstrap check.
func All() { mg.SerialDeps(Vet, Test, SelfBootstrap) }
