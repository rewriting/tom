//go:build mage

// Root build orchestration for the Go side of Tom.
//
// Run from the repository root:
//
//	mage -d magefiles -w . -l        # list targets
//	mage -d magefiles -w . all       # vet + unit tests + self-bootstrap + smoke
//
// The validated Go tree is module tom (src/go/tom); the Tom+Go tests are
// module tomtest (test/go: ADT tests, ports, reference data); the programs
// comparing Java, Go and legacy are module tomtools (test/tools). The frozen prototype (prototype/tomgo) keeps
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
	// toolsDir is module tomtools: the programs that compare the Java, Go
	// and legacy worlds. harness.JavaTestDir/GoTestDir/ToolsDir define the
	// same locations for the Go code; this is the mage-side mirror.
	toolsDir = "test/tools"
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
// vacuously (test/tools/harness).
var requireTools = []string{"TOMGO_REQUIRE_TOOLS=1"}

// Test runs the unit tests of the validated Go tree (module tom) and the
// tests of the compiler's ADTs (test/go/adt).
func Test() error {
	mg.Deps(TomGoFresh)
	if err := runIn(tomDir, "go", "test", "-count=1", "./..."); err != nil {
		return err
	}
	return runIn(testGoDir, "go", "test", "-count=1", "./adt/...")
}

// Vet vets the validated Go tree, the ADT tests (test/go) and the
// comparison tools (test/tools).
func Vet() error {
	mg.Deps(TomGoFresh)
	if err := runIn(tomDir, "go", "vet", "./..."); err != nil {
		return err
	}
	if err := runIn(testGoDir, "go", "vet", "./..."); err != nil {
		return err
	}
	return runIn(toolsDir, "go", "vet", "./...")
}

// SelfBootstrap checks that the compiler's ADTs (tom/gom/adt, the AST
// gomgo is built on, and tom/engine/adt) regenerate from their .gom
// files with the current backend byte-identically to their generation by
// the amorce stable/go/tom (bootstrap), and that the .gom copies have the
// signatures of the Java originals.
func SelfBootstrap() error {
	mg.Deps(Bootstrap)
	return runIn(tomDir, "go", "test", "-count=1", "-run", "TestSelfBootstrap|TestSignatureParity", "./gom/backend/")
}

// The compiler's ADTs: src/go/tom/<dir> holds the .gom sources only
// (their generated .go and .tom are gitignored).
var adts = []struct{ dir, pkg string }{
	{"gom/adt", "adt"},    // the AST of Gom: gomgo is built on it
	{"engine/adt", "adt"}, // the AST of Tom
}

// stableGoDir is the Go amorce: a full standalone copy of module tom
// (non-test Go, generated gom/adt included), as stable/ holds the Java
// code that bootstraps the Java compiler. It is not in go.work: it is
// built with GOWORK=off.
const stableGoDir = "stable/go/tom"

// Bootstrap makes src/go/tom buildable from the .gom sources (from a
// fresh clone too; idempotent): it builds gomgo from the amorce
// stable/go/tom, generates both ADTs from their .gom files into
// src/go/tom with it (stage 1), builds gomgo from src/go/tom on them,
// regenerates both ADTs with it (stage 2) and requires stage 2 to be
// byte-identical to stage 1 (fixpoint: the generated code works in gomgo
// itself). After a deliberate change of gomgo or of the .gom files that
// changes the generated code, run promoteGo, then bootstrap again.
//
// Caveats: right after promoteGo the amorce gomgo and the src gomgo are
// the same code, so the fixpoint is trivially true until gomgo changes;
// for the engine ADT it only shows that both gomgo generate the same code
// (gomgo is not built on the engine ADT): the proof that the generated
// code works is test/go/adt (and go vet).
//
// It also gives each Tom+Go source its generated X.t.go from the amorce
// when src lacks it (tomGoFromAmorce).
func Bootstrap() error {
	tmp, err := os.MkdirTemp("", "tom-bootstrap-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(tmp)
	stable := filepath.Join(tmp, "gomgo-stable")
	if err := runInEnv(stableGoDir, []string{"GOWORK=off"}, "go", "build", "-o", stable, "./gom/cmd/gomgo"); err != nil {
		return fmt.Errorf("bootstrap: building gomgo from the amorce %s: %w", stableGoDir, err)
	}
	if err := generateAdts(stable, tmp, "stage1", true); err != nil {
		return err
	}
	src := filepath.Join(tmp, "gomgo-src")
	if err := runIn(tomDir, "go", "build", "-o", src, "./gom/cmd/gomgo"); err != nil {
		return fmt.Errorf("bootstrap: building gomgo from %s on the generated ADTs: %w", tomDir, err)
	}
	if err := generateAdts(src, tmp, "stage2", false); err != nil {
		return err
	}
	for _, a := range adts {
		d := filepath.FromSlash(a.dir)
		if diff, err := generatedDiff(filepath.Join(tmp, "stage1", d), filepath.Join(tmp, "stage2", d)); err != nil {
			return err
		} else if diff != "" {
			return fmt.Errorf("bootstrap: %s generated by the gomgo of %s differs from its generation by the amorce %s (%s). "+
				"If the change of gomgo or of the .gom files is deliberate, run `mage -d magefiles -w . promoteGo`, then `bootstrap` again",
				a.dir, tomDir, stableGoDir, diff)
		}
	}
	return tomGoFromAmorce()
}

// PromoteGo makes the current src/go/tom the amorce: it regenerates both
// ADTs with the gomgo of src/go/tom (twice: the second gomgo is built on
// the first generation, and must regenerate it identically), then copies
// module tom — its non-test .go files and go.mod, the generated ADTs
// included — into stable/go/tom, and checks that the amorce builds
// standalone (GOWORK=off) and that its gomgo regenerates its own gom/adt
// identically. The Go generated from the Tom+Go sources (X.t.go, mage
// regenTomGo) is copied with the rest of the module. Use it deliberately, when gomgo or the .gom files evolve
// (a gomgo feature the .gom files need), then run bootstrap.
func PromoteGo() error {
	if err := checkTomGoFresh(); err != nil {
		return err
	}
	tmp, err := os.MkdirTemp("", "tom-promote-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(tmp)
	for i := 1; i <= 2; i++ {
		bin := filepath.Join(tmp, fmt.Sprint("gomgo", i))
		if err := runIn(tomDir, "go", "build", "-o", bin, "./gom/cmd/gomgo"); err != nil {
			return fmt.Errorf("promoteGo: building gomgo from %s: %w", tomDir, err)
		}
		if err := generateAdts(bin, tmp, fmt.Sprint("gen", i), true); err != nil {
			return err
		}
	}
	for _, a := range adts {
		d := filepath.FromSlash(a.dir)
		if diff, err := generatedDiff(filepath.Join(tmp, "gen1", d), filepath.Join(tmp, "gen2", d)); err != nil {
			return err
		} else if diff != "" {
			return fmt.Errorf("promoteGo: %s is not a fixpoint of the gomgo of %s (%s)", a.dir, tomDir, diff)
		}
	}
	if err := copyModule(tomDir, stableGoDir); err != nil {
		return err
	}
	bin := filepath.Join(tmp, "gomgo-stable")
	if err := runInEnv(stableGoDir, []string{"GOWORK=off"}, "go", "build", "-o", bin, "./gom/cmd/gomgo"); err != nil {
		return fmt.Errorf("promoteGo: the amorce %s does not build: %w", stableGoDir, err)
	}
	if err := runInEnv(stableGoDir, []string{"GOWORK=off"}, "go", "vet", "./..."); err != nil {
		return err
	}
	if err := generateAdts(bin, tmp, "check", false); err != nil {
		return err
	}
	// the amorce holds the Go code of module tom, not the mappings
	if maps, _ := filepath.Glob(filepath.Join(tmp, "check", "gom", "adt", "*.tom")); len(maps) > 0 {
		for _, m := range maps {
			if err := os.Remove(m); err != nil {
				return err
			}
		}
	}
	if diff, err := generatedDiff(filepath.Join(tmp, "check", "gom", "adt"), filepath.Join(stableGoDir, "gom", "adt")); err != nil {
		return err
	} else if diff != "" {
		return fmt.Errorf("promoteGo: the gomgo of the amorce does not regenerate its gom/adt identically (%s)", diff)
	}
	fmt.Println("promoted " + tomDir + " to the amorce " + stableGoDir + "; now run bootstrap")
	return nil
}

// generateAdts generates both ADTs from their .gom files (src/go/tom)
// with the gomgo binary into <tmp>/<stage>/<dir>, and, when toSrc, also
// makes them the generated files of src/go/tom/<dir>.
func generateAdts(gomgo, tmp, stage string, toSrc bool) error {
	for _, a := range adts {
		src := filepath.Join(tomDir, filepath.FromSlash(a.dir))
		files, err := filepath.Glob(filepath.Join(src, "*.gom"))
		if err != nil || len(files) == 0 {
			return fmt.Errorf("bootstrap: no .gom file in %s", src)
		}
		sort.Strings(files)
		out := filepath.Join(tmp, stage, filepath.FromSlash(a.dir))
		args := []string{"gom-batch", "--gohooks", "--pkg", a.pkg, "-o", out}
		for _, f := range files {
			args = append(args, filepath.Base(f))
		}
		cmd := exec.Command(gomgo, args...)
		cmd.Dir = src
		if b, err := cmd.CombinedOutput(); err != nil {
			return fmt.Errorf("bootstrap: %s gom-batch in %s: %v\n%s", filepath.Base(gomgo), src, err, b)
		}
		// gom-batch writes a stand-alone go.mod; the ADTs live in module tom
		if err := os.Remove(filepath.Join(out, "go.mod")); err != nil {
			return err
		}
		if toSrc {
			if err := syncGenerated(out, src); err != nil {
				return err
			}
		}
	}
	return nil
}

// copyModule makes dst a copy of the Go module at src: its non-test .go
// files (testdata excluded) and go.mod; the other files of dst are
// removed.
func copyModule(src, dst string) error {
	want := map[string]bool{}
	err := filepath.WalkDir(src, func(p string, d os.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			if d.Name() == "testdata" {
				return filepath.SkipDir
			}
			return nil
		}
		n := d.Name()
		if (strings.HasSuffix(n, ".go") && !strings.HasSuffix(n, "_test.go")) || (n == "go.mod" && filepath.Dir(p) == src) {
			r, _ := filepath.Rel(src, p)
			want[r] = true
		}
		return nil
	})
	if err != nil {
		return err
	}
	for r := range want {
		b, err := os.ReadFile(filepath.Join(src, r))
		if err != nil {
			return err
		}
		t := filepath.Join(dst, r)
		if err := os.MkdirAll(filepath.Dir(t), 0o755); err != nil {
			return err
		}
		if old, err := os.ReadFile(t); err == nil && bytes.Equal(old, b) {
			continue
		}
		if err := os.WriteFile(t, b, 0o644); err != nil {
			return err
		}
	}
	return filepath.WalkDir(dst, func(p string, d os.DirEntry, err error) error {
		if err != nil || d.IsDir() {
			return err
		}
		if r, _ := filepath.Rel(dst, p); !want[r] {
			return os.Remove(p)
		}
		return nil
	})
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

// tomGoSources returns the Tom+Go sources (*.t) of src/go/tom, testdata
// excluded; the Go generated from X.t is X.t.go, next to it (mage
// regenTomGo; gitignored in src/, kept in the amorce by promoteGo).
func tomGoSources() ([]string, error) {
	var out []string
	err := filepath.WalkDir(tomDir, func(p string, d os.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			if d.Name() == "testdata" {
				return filepath.SkipDir
			}
			return nil
		}
		if strings.HasSuffix(p, ".t") {
			out = append(out, p)
		}
		return nil
	})
	sort.Strings(out)
	return out, err
}

// tomGoFromAmorce gives each Tom+Go source X.t of src/go/tom that lacks
// its generated X.t.go (a fresh clone) the copy of the amorce
// (stable/go/tom, same path), so that module tom builds without Java. A
// source the amorce does not know yet is left to regenTomGo (tomGoFresh
// reports it).
func tomGoFromAmorce() error {
	srcs, err := tomGoSources()
	if err != nil {
		return err
	}
	for _, s := range srcs {
		gen := s + ".go"
		if _, err := os.Stat(gen); err == nil {
			continue
		}
		rel, _ := filepath.Rel(tomDir, gen)
		b, err := os.ReadFile(filepath.Join(stableGoDir, rel))
		if err != nil {
			continue
		}
		if err := os.WriteFile(gen, b, 0o644); err != nil {
			return err
		}
		fmt.Println("bootstrap: " + gen + " taken from the amorce " + stableGoDir)
	}
	return nil
}

// checkTomGoFresh fails when a Tom+Go source X.t of src/go/tom has no
// generated X.t.go or is newer than it.
func checkTomGoFresh() error {
	srcs, err := tomGoSources()
	if err != nil {
		return err
	}
	for _, s := range srcs {
		st, err := os.Stat(s)
		if err != nil {
			return err
		}
		gt, err := os.Stat(s + ".go")
		if err != nil {
			return fmt.Errorf("%s has no generated %s.go: run mage -d magefiles -w . regenTomGo (needs src/dist)", s, filepath.Base(s))
		}
		if st.ModTime().After(gt.ModTime()) {
			return fmt.Errorf("%s is newer than its generated %s.go: run mage -d magefiles -w . regenTomGo (needs src/dist), "+
				"or delete the .t.go to take the amorce's (bootstrap)", s, filepath.Base(s))
		}
	}
	return nil
}

// TomGoFresh fails when the Go generated from a Tom+Go source of
// src/go/tom (X.t -> X.t.go) is missing or older than its source.
func TomGoFresh() error {
	mg.Deps(Bootstrap)
	return checkTomGoFresh()
}

// RegenTomGo compiles the Tom+Go sources of the compiler
// (src/go/tom/engine/**/*.t) with src/dist (D4; its compiler must be
// fresh) into their X.t.go; needs a built src/dist (JDK 8,
// ./build.sh src.dist). After it: promoteGo, then bootstrap, to put them
// in the amorce.
func RegenTomGo() error {
	mg.Deps(Bootstrap)
	return runIn(toolsDir, "go", "run", "./j4/regentomgo")
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
// on every test/java/*.t (needs legacy/src/dist and src/dist built).
func PortJava() error {
	mg.Deps(Bootstrap)
	return runInEnv(toolsDir, requireTools, "go", "test", "-count=1", "-timeout", "30m", "./javaparity/...")
}

// PortGo runs T1 (golden Go output, build and run) on the Tom+Go ports
// in test/go/ (needs src/dist built), T2 (behaviour vs the legacy/
// oracle cached in test/go/testdata/oracle/; refresh it with
// `go test ./parity/ -run TestSameBehaviourAsLegacy -args -update-oracle`
// in test/tools) and rewrites doc/go/port-dashboard.md.
func PortGo() error {
	mg.Deps(Bootstrap)
	return runInEnv(toolsDir, requireTools, "go", "test", "-count=1", "./parity/", "-args", "-write-dashboard")
}

// Coverage runs T3 and rewrites doc/go/coverage.md.
func Coverage() error {
	mg.Deps(Bootstrap)
	return runInEnv(toolsDir, requireTools, "go", "test", "-count=1", "-v", "./coverage/...", "-args", "-write-coverage")
}

// Smoke runs the smoke tests (test/tools/smoke, fixtures in
// test/go/smoke/testdata; needs src/dist built, the harness builds gomgo
// itself).
func Smoke() error {
	mg.Deps(Bootstrap)
	return runInEnv(toolsDir, requireTools, "go", "test", "-count=1", "./smoke/")
}

// J4 runs the J4 tests that need a built dist (test/tools/j4).
func J4() error {
	mg.Deps(TomGoFresh)
	return runInEnv(toolsDir, requireTools, "go", "test", "-count=1", "./j4/...")
}

// BridgeCheck runs P0 (J4 spec 5.1): T1 and the smoke tests with
// --goFrom Backend --goCheck (each bundle echoed by tomgo must be
// identical) and --intermediate, then re-reads every dump in Go.
func BridgeCheck() error {
	mg.Deps(TomGoFresh)
	dumps, err := os.MkdirTemp("", "tom-dumps-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(dumps)
	env := append([]string{"TOMGO_GOCHECK=1", "TOMGO_DUMPS=" + dumps}, requireTools...)
	if err := runInEnv(toolsDir, env, "go", "test", "-count=1", "-timeout", "30m", "./parity/", "-run", "TestGoBackend"); err != nil {
		return err
	}
	if err := runInEnv(toolsDir, env, "go", "test", "-count=1", "./smoke/", "-run", "TestGoSmoke"); err != nil {
		return err
	}
	return runInEnv(toolsDir, env, "go", "test", "-count=1", "-v", "./j4/", "-run", "TestDumpsRoundTrip")
}

// goCommands are the Go commands of module tom installed in <dist>/bin
// (the dist ships binaries; src/ holds sources only). A command whose
// main package is absent from the tree is skipped (tomgo in an amorce
// older than J4.0c).
var goCommands = []struct{ name, pkg string }{
	{"gomgo", "./gom/cmd/gomgo"},
	{"tomgo", "./engine/cmd/tomgo"},
}

// InstallGo builds the Go commands into binDir: from = "src" builds them
// from src/go/tom (after bootstrap: the ADTs are generated locally),
// from = "stable" from the amorce stable/go/tom (GOWORK=off). Called by
// build.xml (src.dist, stable.dist).
func InstallGo(from, binDir string) error {
	var dir string
	var env []string
	switch from {
	case "src":
		mg.Deps(Bootstrap)
		dir = tomDir
	case "stable":
		dir, env = stableGoDir, []string{"GOWORK=off"}
	default:
		return fmt.Errorf("installGo: from must be src or stable, not %q", from)
	}
	abs, err := filepath.Abs(binDir)
	if err != nil {
		return err
	}
	if err := os.MkdirAll(abs, 0o755); err != nil {
		return err
	}
	for _, c := range goCommands {
		if _, err := os.Stat(filepath.Join(dir, filepath.FromSlash(c.pkg))); err != nil {
			fmt.Printf("installGo: %s: no %s in %s, not installed\n", c.name, c.pkg, dir)
			continue
		}
		if err := runInEnv(dir, env, "go", "build", "-o", filepath.Join(abs, c.name), c.pkg); err != nil {
			return fmt.Errorf("installGo: building %s from %s: %w", c.name, dir, err)
		}
		fmt.Printf("installGo: %s -> %s\n", c.name, filepath.Join(abs, c.name))
	}
	return nil
}

// All runs vet, the unit tests, the self-bootstrap check and the smoke
// tests (after Bootstrap: it works from a fresh clone).
func All() { mg.SerialDeps(Bootstrap, Vet, VetStandalone, Test, SelfBootstrap, Smoke) }

// VetStandalone checks that both validated modules build without the workspace.
func VetStandalone() error {
	mg.Deps(TomGoFresh)
	if err := runInEnv(tomDir, []string{"GOWORK=off"}, "go", "vet", "./..."); err != nil {
		return err
	}
	if err := runInEnv(testGoDir, []string{"GOWORK=off"}, "go", "vet", "./..."); err != nil {
		return err
	}
	return runInEnv(toolsDir, []string{"GOWORK=off"}, "go", "vet", "./...")
}
