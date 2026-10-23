// Package j4 holds the tests of milestone J4 that need a built dist
// (the J4 spec: docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md).
package j4

import (
	"os"
	"path/filepath"
	"strings"
	"testing"
	"time"

	"tomtools/harness"
)

// buildAgainstSrc writes in dir a go.mod whose module tom is the source
// tree src/go/tom (the dist ships only the runtime, not the engine ADT)
// and tidies it.
func buildAgainstSrc(t *testing.T, dir string) {
	t.Helper()
	root := harness.RepoRoot(t)
	mod := "module adtuse\n\ngo 1.26\n\nrequire tom v0.0.0\n\nreplace tom => " +
		filepath.Join(root, "src", "go", "tom") + "\n"
	if err := os.WriteFile(filepath.Join(dir, "go.mod"), []byte(mod), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := harness.RunIn(dir, []string{"GOWORK=off"}, "go", "mod", "tidy"); err != nil {
		t.Fatalf("go mod tidy: %v\n%s", err, out)
	}
}

// TestEngineADTFromTomGo compiles test/go/j4/testdata/adtuse/adtuse.t
// with `tom -go -np -I src/go/tom/engine/adt`, builds it in a module
// whose tom is src/go/tom (the dist ships only the runtime) and checks
// its output on term.txt.
func TestEngineADTFromTomGo(t *testing.T) {
	harness.LookPath(t, "go")
	root := harness.RepoRoot(t)
	dist := harness.NewDist(t)
	adtDir := filepath.Join(root, "src", "go", "tom", "engine", "adt")
	if _, err := os.Stat(filepath.Join(adtDir, "adt.tom")); err != nil {
		t.Fatalf("no adt.tom in %s: run mage bootstrap", adtDir)
	}
	data := filepath.Join(harness.GoTestDir(root), "j4", "testdata", "adtuse")
	want, err := os.ReadFile(filepath.Join(data, "expected.txt"))
	if err != nil {
		t.Fatal(err)
	}
	out := t.TempDir()
	if msg, err := dist.Compile(t, filepath.Join(data, "adtuse.t"), out, "-go", "-np", "-I", adtDir); err != nil {
		t.Fatalf("tom -go: %v\n%s", err, msg)
	}
	buildAgainstSrc(t, out)
	got, err := harness.RunIn(out, []string{"GOWORK=off"}, "go", "run", ".", filepath.Join(data, "term.txt"))
	if err != nil {
		t.Fatalf("go run: %v\n%s", err, got)
	}
	if got != string(want) {
		t.Fatalf("output %q, want %q", got, want)
	}
}

// TestTwoFilesOnePackage compiles test/go/j4/testdata/multi/{A,B}.t, two
// files of one Go package that both include the int, intarray, string
// and Term mappings and both use AC matching, with `tom -go -np` next to
// the gomgo output of Term.gom: the per-file prefix tom_<F>_ of the
// generated declarations (J4.0b) lets them coexist (go vet, go run).
func TestTwoFilesOnePackage(t *testing.T) {
	harness.LookPath(t, "go")
	root := harness.RepoRoot(t)
	dist := harness.NewDist(t)
	data := filepath.Join(harness.GoTestDir(root), "j4", "testdata", "multi")
	want, err := os.ReadFile(filepath.Join(data, "expected.txt"))
	if err != nil {
		t.Fatal(err)
	}
	out := t.TempDir()
	gargs := []string{"gom-block", "-o", out, "--pkg", "main", filepath.Join(data, "Term.gom")}
	if msg, err := harness.RunIn(out, nil, dist.Bin(t, "gomgo"), gargs...); err != nil {
		t.Fatalf("gomgo %v: %v\n%s", gargs, err, msg)
	}
	for _, f := range []string{"A.t", "B.t"} {
		if msg, err := dist.Compile(t, filepath.Join(data, f), out, "-go", "-np", "-I", out); err != nil {
			t.Fatalf("tom -go %s: %v\n%s", f, err, msg)
		}
	}
	harness.SetupGoModule(t, out)
	if msg, err := harness.RunIn(out, nil, "go", "vet", "."); err != nil {
		t.Fatalf("go vet: %v\n%s", err, msg)
	}
	if msg, err := harness.RunIn(out, nil, "go", "build", "-o", "prog", "."); err != nil {
		t.Fatalf("go build: %v\n%s", err, msg)
	}
	got, err := harness.RunInTimeout(out, 60*time.Second, nil, filepath.Join(out, "prog"))
	if err != nil {
		t.Fatalf("run (timeout (AC loop?)): %v\n%s", err, got)
	}
	if got != string(want) {
		t.Fatalf("output %q, want %q", got, want)
	}
}

// TestGoFrom checks the Java side of the bridge (J4.0c, spec §4.1):
// --goFrom stops the Java chain before the named plugin and hands the
// bundle to <dist>/bin/tomgo suffix, --goCheck checks the round trip
// (tomgo suffix --echo) then lets the Java chain finish; the errors
// (unknown plugin, no -go, no tomgo, phase not ported) are explicit.
func TestGoFrom(t *testing.T) {
	root := harness.RepoRoot(t)
	dist := harness.NewDist(t)
	dist.Bin(t, "tomgo")
	goSrc := filepath.Join(harness.GoTestDir(root), "Peano.t")
	golden, err := os.ReadFile(filepath.Join(harness.GoTestDir(root), "testdata", "golden", "Peano.go"))
	if err != nil {
		t.Fatal(err)
	}
	tom := func(env []string, src string, args ...string) (string, string, error) {
		out := t.TempDir()
		full := append(append([]string{"-d", out}, args...), src)
		env = append([]string{"TOM_HOME=" + dist.Home, "TOMGO_BIN="}, env...)
		msg, err := harness.RunIn(filepath.Dir(src), env, filepath.Join(dist.Home, "bin", "tom"), full...)
		return out, msg, err
	}
	fails := func(name, want string, env []string, src string, args ...string) {
		t.Run(name, func(t *testing.T) {
			_, msg, err := tom(env, src, args...)
			if err == nil {
				t.Fatalf("tom %v succeeded, want an error %q\n%s", args, want, msg)
			}
			if !strings.Contains(msg, want) {
				t.Fatalf("tom %v: output does not contain %q:\n%s", args, want, msg)
			}
		})
	}
	fails("unknown plugin", "--goFrom Nope: no such plugin", nil, goSrc, "-go", "-np", "--goFrom", "Nope")
	fails("without -go", "--goFrom needs -go", nil, filepath.Join(harness.JavaTestDir(root), "Peano.t"), "--goFrom", "Backend")
	fails("no tomgo", "tomgo not found at /nonexistent", []string{"TOMGO_BIN=/nonexistent"}, goSrc, "-go", "-np", "--goFrom", "Backend")
	fails("not ported", "phase Backend is not ported to Go yet", nil, goSrc, "-go", "-np", "--goFrom", "Backend")
	for _, from := range []string{"Backend", "TypeChecker", "optimizer"} {
		t.Run("goCheck "+from, func(t *testing.T) {
			out, msg, err := tom(nil, goSrc, "-go", "-np", "--goFrom", from, "--goCheck", "--intermediate")
			if err != nil {
				t.Fatalf("tom --goFrom %s --goCheck: %v\n%s", from, err, msg)
			}
			got, err := os.ReadFile(filepath.Join(out, "Peano.go"))
			if err != nil {
				t.Fatal(err)
			}
			if string(got) != string(golden) {
				t.Fatalf("--goCheck changed the output of the Java chain")
			}
			bundle, err := os.ReadFile(filepath.Join(out, "Peano.go.tfix.bridge"))
			if err != nil {
				t.Fatal(err)
			}
			name := map[string]string{"Backend": "Backend", "TypeChecker": "Typechecker", "optimizer": "Optimizer"}[from]
			if head := "tom-bridge 1\nfrom " + name + "\n"; !strings.HasPrefix(string(bundle), head) || !strings.HasSuffix(string(bundle), "\nend\n") {
				t.Fatalf("bundle starts with %.80q, want %q ... end", bundle, head)
			}
		})
	}
}
