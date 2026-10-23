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
	"bytes"
	"fmt"
	"os"
	"os/exec"
	"path/filepath"
	"sort"
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

// Test runs the unit tests of the validated Go tree (module tom) and the
// tests of the compiler's ADTs (test/go/adt).
func Test() error {
	mg.Deps(Bootstrap)
	if err := runIn(tomDir, "go", "test", "-count=1", "./..."); err != nil {
		return err
	}
	return runIn(testGoDir, "go", "test", "-count=1", "./adt/...")
}

// Vet vets the validated Go tree and the Tom+Go test harnesses.
func Vet() error {
	mg.Deps(Bootstrap)
	if err := runIn(tomDir, "go", "vet", "./..."); err != nil {
		return err
	}
	return runIn(testGoDir, "go", "vet", "./...")
}

// SelfBootstrap checks that the compiler's ADTs (tom/gom/adt, the AST
// gomgo is built on, and tom/engine/adt) regenerate from their .gom
// files byte-identically to the amorce stable/go/tom, and that the .gom
// copies have the signatures of the Java originals.
func SelfBootstrap() error {
	mg.Deps(Bootstrap)
	return runIn(tomDir, "go", "test", "-count=1", "-run", "TestSelfBootstrap|TestSignatureParity", "./gom/backend/")
}

// The compiler's ADTs: src/go/tom/<dir> holds the .gom sources only (the
// generated .go and .tom are gitignored), stable/go/tom/<dir> the
// committed generation (the amorce, as stable/ holds the Java code that
// bootstraps the Java compiler).
var adts = []struct{ dir, pkg string }{
	{"gom/adt", "adt"},    // the AST of Gom: gomgo is built on it
	{"engine/adt", "adt"}, // the AST of Tom
}

const stableGoDir = "stable/go/tom"

// Bootstrap makes src/go/tom buildable from the .gom sources (from a
// fresh clone too): it copies the amorce of gom/adt (stable/go/tom/gom/adt)
// into src/go/tom/gom/adt, builds gomgo on it, regenerates both ADTs from
// their .gom files into src/go/tom (gomgo gom-batch --gohooks), and
// requires the regenerated gom/adt to be byte-identical to the amorce
// (fixpoint). After a deliberate change of src/go/tom/gom/adt/*.gom or of
// gomgo, run promoteAdt, then bootstrap again.
func Bootstrap() error {
	if err := syncGenerated(filepath.Join(stableGoDir, "gom", "adt"), filepath.Join(tomDir, "gom", "adt")); err != nil {
		return err
	}
	if err := regenAdts(); err != nil {
		return err
	}
	if diff, err := generatedDiff(filepath.Join(stableGoDir, "gom", "adt"), filepath.Join(tomDir, "gom", "adt")); err != nil {
		return err
	} else if diff != "" {
		return fmt.Errorf("bootstrap: %s/gom/adt regenerated from its .gom files differs from the amorce %s/gom/adt (%s). "+
			"If the change of the .gom files or of gomgo is deliberate, run `mage -d magefiles -w . promoteAdt`, then `bootstrap` again (fixpoint)",
			tomDir, stableGoDir, diff)
	}
	return nil
}

// PromoteAdt regenerates both ADTs from their .gom files into
// src/go/tom (with the gomgo built on the current src/go/tom/gom/adt,
// the amorce when it is missing) and copies the generation into the
// amorce stable/go/tom. Use it deliberately, when the .gom files or
// gomgo evolve; then run bootstrap, which checks the fixpoint.
func PromoteAdt() error {
	gen := filepath.Join(tomDir, "gom", "adt")
	if names, err := generatedFiles(gen); err != nil {
		return err
	} else if len(names) == 0 {
		if err := syncGenerated(filepath.Join(stableGoDir, "gom", "adt"), gen); err != nil {
			return err
		}
	}
	if err := regenAdts(); err != nil {
		return err
	}
	for _, a := range adts {
		if err := syncGenerated(filepath.Join(tomDir, filepath.FromSlash(a.dir)), filepath.Join(stableGoDir, filepath.FromSlash(a.dir))); err != nil {
			return err
		}
	}
	fmt.Println("promoted the generated ADTs to " + stableGoDir + "; now run bootstrap (fixpoint check)")
	return nil
}

// regenAdts builds gomgo on the current src/go/tom/gom/adt and
// regenerates both ADTs from their .gom files into src/go/tom.
func regenAdts() error {
	tmp, err := os.MkdirTemp("", "tom-bootstrap-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(tmp)
	gomgo := filepath.Join(tmp, "gomgo")
	if err := runIn(tomDir, "go", "build", "-o", gomgo, "./gom/cmd/gomgo"); err != nil {
		return fmt.Errorf("bootstrap: building gomgo on %s/gom/adt: %w", tomDir, err)
	}
	for i, a := range adts {
		src := filepath.Join(tomDir, filepath.FromSlash(a.dir))
		files, err := filepath.Glob(filepath.Join(src, "*.gom"))
		if err != nil || len(files) == 0 {
			return fmt.Errorf("bootstrap: no .gom file in %s", src)
		}
		sort.Strings(files)
		out := filepath.Join(tmp, fmt.Sprint(i))
		args := []string{"gom-batch", "--gohooks", "--pkg", a.pkg, "-o", out}
		for _, f := range files {
			args = append(args, filepath.Base(f))
		}
		cmd := exec.Command(gomgo, args...)
		cmd.Dir = src
		if b, err := cmd.CombinedOutput(); err != nil {
			return fmt.Errorf("bootstrap: gomgo gom-batch in %s: %v\n%s", src, err, b)
		}
		// gom-batch writes a stand-alone go.mod; the ADTs live in module tom
		if err := os.Remove(filepath.Join(out, "go.mod")); err != nil {
			return err
		}
		if err := syncGenerated(out, src); err != nil {
			return err
		}
	}
	return nil
}

// generatedFiles returns the names of the generated files of an ADT
// directory: every file but the .gom sources.
func generatedFiles(dir string) ([]string, error) {
	entries, err := os.ReadDir(dir)
	if err != nil && !os.IsNotExist(err) {
		return nil, err
	}
	var out []string
	for _, e := range entries {
		if !e.IsDir() && !strings.HasSuffix(e.Name(), ".gom") && e.Name() != ".DS_Store" {
			out = append(out, e.Name())
		}
	}
	sort.Strings(out)
	return out, nil
}

// syncGenerated makes the generated files of dir those of from: it
// writes the files that differ and removes those that from lacks.
func syncGenerated(from, dir string) error {
	want, err := generatedFiles(from)
	if err != nil {
		return err
	}
	if len(want) == 0 {
		return fmt.Errorf("bootstrap: no generated file in %s", from)
	}
	if err := os.MkdirAll(dir, 0o755); err != nil {
		return err
	}
	have, err := generatedFiles(dir)
	if err != nil {
		return err
	}
	keep := map[string]bool{}
	for _, n := range want {
		keep[n] = true
		b, err := os.ReadFile(filepath.Join(from, n))
		if err != nil {
			return err
		}
		if old, err := os.ReadFile(filepath.Join(dir, n)); err == nil && bytes.Equal(old, b) {
			continue
		}
		if err := os.WriteFile(filepath.Join(dir, n), b, 0o644); err != nil {
			return err
		}
	}
	for _, n := range have {
		if !keep[n] {
			if err := os.Remove(filepath.Join(dir, n)); err != nil {
				return err
			}
		}
	}
	return nil
}

// generatedDiff describes the first difference between the generated
// files of a and b ("" when they are identical).
func generatedDiff(a, b string) (string, error) {
	fa, err := generatedFiles(a)
	if err != nil {
		return "", err
	}
	fb, err := generatedFiles(b)
	if err != nil {
		return "", err
	}
	if strings.Join(fa, " ") != strings.Join(fb, " ") {
		return fmt.Sprintf("files %v vs %v", fa, fb), nil
	}
	for _, n := range fa {
		x, _ := os.ReadFile(filepath.Join(a, n))
		y, _ := os.ReadFile(filepath.Join(b, n))
		if !bytes.Equal(x, y) {
			return "file " + n + " differs", nil
		}
	}
	return "", nil
}

// PortJava runs T0: the root compiler's Java output must equal legacy/'s
// on every test/*.t (needs legacy/src/dist and src/dist built).
func PortJava() error {
	mg.Deps(Bootstrap)
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "-timeout", "30m", "./javaparity/...")
}

// PortGo runs T1 (golden Go output, build and run) on the Tom+Go ports
// in test/go/ (needs src/dist built), T2 (behaviour vs the legacy/
// oracle cached in test/go/testdata/oracle/; refresh it with
// `go test ./ -run TestSameBehaviourAsLegacy -args -update-oracle` in
// test/go) and rewrites doc/go/port-dashboard.md.
func PortGo() error {
	mg.Deps(Bootstrap)
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "./", "-args", "-write-dashboard")
}

// Coverage runs T3 and rewrites doc/go/coverage.md.
func Coverage() error {
	mg.Deps(Bootstrap)
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "-v", "./coverage/...", "-args", "-write-coverage")
}

// Smoke runs the smoke tests of module tomtest (needs src/dist built; the
// harness builds gomgo itself).
func Smoke() error {
	mg.Deps(Bootstrap)
	return runInEnv(testGoDir, requireTools, "go", "test", "-count=1", "./smoke/")
}

// All runs vet, the unit tests, the self-bootstrap check and the smoke
// tests (after Bootstrap: it works from a fresh clone).
func All() { mg.SerialDeps(Bootstrap, Vet, VetStandalone, Test, SelfBootstrap, Smoke) }

// VetStandalone checks that both validated modules build without the workspace.
func VetStandalone() error {
	mg.Deps(Bootstrap)
	if err := runInEnv(tomDir, []string{"GOWORK=off"}, "go", "vet", "./..."); err != nil {
		return err
	}
	return runInEnv(testGoDir, []string{"GOWORK=off"}, "go", "vet", "./...")
}
