//go:build mage

// Root build orchestration for the Go side of Tom.
//
// Run from the repository root:
//
//	mage -d magefiles -w . -l        # list targets
//	mage -d magefiles -w . all       # vet + unit tests + self-bootstrap + smoke
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

// init moves to the repository root (the directory holding build.xml and
// go.work), found by walking up from the working directory, so that the
// targets work with or without `-w .`.
func init() {
	dir, err := os.Getwd()
	if err != nil {
		fmt.Fprintln(os.Stderr, "magefiles:", err)
		os.Exit(1)
	}
	for d := dir; ; {
		_, e1 := os.Stat(filepath.Join(d, "build.xml"))
		_, e2 := os.Stat(filepath.Join(d, "go.work"))
		if e1 == nil && e2 == nil {
			_ = os.Chdir(d)
			return
		}
		parent := filepath.Dir(d)
		if parent == d {
			fmt.Fprintf(os.Stderr, "magefiles: repository root (build.xml + go.work) not found above %s\n", dir)
			os.Exit(1)
		}
		d = parent
	}
}

// runIn runs name with args in dir (relative to the repository root),
// streaming its output.
func runIn(dir, name string, args ...string) error {
	return runInEnv(dir, nil, name, args...)
}

// runInEnv is runIn with extra environment variables (KEY=VALUE).
func runInEnv(dir string, env []string, name string, args ...string) error {
	cmd := exec.Command(name, args...)
	cmd.Env = append(os.Environ(), env...)
	cmd.Dir = dir
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	if mg.Verbose() {
		fmt.Println("exec (in "+dir+"):", name, strings.Join(args, " "))
	}
	return cmd.Run()
}

// requireTools makes the harness fail, instead of skipping, when a tool
// (java, javac, cc, a built dist) is missing: a target must not pass
// vacuously (test/go/harness).
var requireTools = []string{"TOMGO_REQUIRE_TOOLS=1"}

// Test runs the unit tests of the validated Go tree (module tom).
func Test() error { return runIn(tomDir, "go", "test", "-count=1", "./...") }

// Vet vets the validated Go tree and the Tom+Go test harnesses.
func Vet() error {
	if err := runIn(tomDir, "go", "vet", "./..."); err != nil {
		return err
	}
	return runIn(testGoDir, "go", "vet", "./...")
}

// SelfBootstrap checks that the compiler's ADTs (tom/gom/adt, the AST
// gomgo is built on, tom/engine/adt and tom/engine/adt/il) regenerate
// byte-identically from their .gom files (needs src/dist built: the
// hooks of engine/adt are compiled by tom -go).
func SelfBootstrap() error {
	return runInEnv(tomDir, requireTools, "go", "test", "-count=1", "-run", "TestSelfBootstrap|TestSignatureParity", "./internal/adtgen/")
}

// RegenAdt regenerates the compiler's ADTs in src/go/tom (gom/adt,
// engine/adt, engine/adt/il) from their .gom files: gomgo gom-batch, then
// tom -go (TOM_HOME, default src/dist) for the hooks of engine/adt
// (internal/adtgen). Run it twice after a change of the Gom ADT or of
// gomgo: the second run rebuilds gomgo on the regenerated gom/adt and
// must change nothing (TestSelfBootstrap).
func RegenAdt() error {
	return runIn(tomDir, "go", "run", "./internal/adtgen/regenadt")
}

// PortJava runs T0: the root compiler's Java output must equal legacy/'s
// on every test/*.t (needs legacy/src/dist and src/dist built).
func PortJava() error {
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "-timeout", "30m", "./javaparity/...")
}

// PortGo runs T1 (golden Go output, build and run) on the Tom+Go ports
// in test/go/ (needs src/dist built), T2 (behaviour vs the legacy/
// oracle cached in test/go/testdata/oracle/; refresh it with
// `go test ./ -run TestSameBehaviourAsLegacy -args -update-oracle` in
// test/go) and rewrites doc/go/port-dashboard.md.
func PortGo() error {
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "./", "-args", "-write-dashboard")
}

// Coverage runs T3 and rewrites doc/go/coverage.md.
func Coverage() error {
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "-v", "./coverage/...", "-args", "-write-coverage")
}

// Smoke runs the smoke tests of module tomtest (needs src/dist built; the
// harness builds gomgo itself).
func Smoke() error {
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "./smoke/")
}

// All runs vet, the unit tests, the self-bootstrap check and the smoke tests.
func All() { mg.SerialDeps(Vet, VetStandalone, Test, SelfBootstrap, Smoke) }

// VetStandalone checks that both validated modules build without the workspace.
func VetStandalone() error {
	if err := runInEnv(tomDir, []string{"GOWORK=off"}, "go", "vet", "./..."); err != nil {
		return err
	}
	return runInEnv(testGoDir, []string{"GOWORK=off"}, "go", "vet", "./...")
}
