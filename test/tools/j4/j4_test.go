// Package j4 holds the tests of milestone J4 that need a built dist
// (the J4 spec: docs/superpowers/specs/2026-10-24-j4-tom-go-compiler-design.md).
package j4

import (
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"sort"
	"strings"
	"testing"
	"time"

	"tom/engine/adt"
	"tom/engine/bridge"
	"tom/library/aterm"
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
	fails("goFrom Starter", "cannot hand over before Starter: the bridge starts after the parser", nil, goSrc, "-go", "-np", "--goFrom", "Starter")
	fails("goFrom Parser", "cannot hand over before Parser: the bridge starts after the parser", nil, goSrc, "-go", "-np", "--goFrom", "Parser")
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

// TestGoFromFakeTomgo drives the bridge with fake tomgo scripts
// (TOMGO_BIN): a hung tomgo is killed after TOMGO_TIMEOUT, a --goCheck
// round trip that differs names the first differing line, and without
// --goCheck the standard output of tomgo is the generated file.
func TestGoFromFakeTomgo(t *testing.T) {
	root := harness.RepoRoot(t)
	dist := harness.NewDist(t)
	src := filepath.Join(harness.GoTestDir(root), "Peano.t")
	fake := func(script string) string {
		path := filepath.Join(t.TempDir(), "tomgo")
		if err := os.WriteFile(path, []byte(script), 0o755); err != nil {
			t.Fatal(err)
		}
		return path
	}
	run := func(tomgo string, env []string, args ...string) (string, string, time.Duration, error) {
		out := t.TempDir()
		full := append(append([]string{"-d", out}, args...), src)
		env = append([]string{"TOM_HOME=" + dist.Home, "TOMGO_BIN=" + tomgo}, env...)
		start := time.Now()
		msg, err := harness.RunInTimeout(filepath.Dir(src), 60*time.Second, env, filepath.Join(dist.Home, "bin", "tom"), full...)
		return out, msg, time.Since(start), err
	}
	t.Run("hang", func(t *testing.T) {
		_, msg, d, err := run(fake("#!/bin/sh\nexec sleep 600\n"), []string{"TOMGO_TIMEOUT=2"}, "-go", "-np", "--goFrom", "Backend")
		if err == nil || strings.Contains(err.Error(), "timeout after") {
			t.Fatalf("tom did not fail by itself within 60 s: %v\n%s", err, msg)
		}
		if d > 30*time.Second {
			t.Fatalf("tom took %v", d)
		}
		if !strings.Contains(msg, "did not finish within 2 seconds") {
			t.Fatalf("no timeout message:\n%s", msg)
		}
	})
	t.Run("mismatch", func(t *testing.T) {
		_, msg, _, err := run(fake("#!/bin/sh\nsed '3s/^/X/'\n"), nil, "-go", "-np", "--goFrom", "Backend", "--goCheck")
		if err == nil {
			t.Fatalf("tom succeeded on a differing round trip\n%s", msg)
		}
		if !strings.Contains(msg, "bridge round trip differs at line 3: X") {
			t.Fatalf("no mismatch message:\n%s", msg)
		}
	})
	t.Run("success", func(t *testing.T) {
		const text = "package main\n\nfunc main() {}\n"
		out, msg, _, err := run(fake("#!/bin/sh\ncat >/dev/null\nprintf 'package main\\n\\nfunc main() {}\\n'\n"), nil, "-go", "-np", "--goFrom", "Backend")
		if err != nil {
			t.Fatalf("tom: %v\n%s", err, msg)
		}
		got, err := os.ReadFile(filepath.Join(out, "Peano.go"))
		if err != nil {
			t.Fatal(err)
		}
		if string(got) != text {
			t.Fatalf("output %q, want %q", got, text)
		}
	})
}

// minDumpBundles is the floor of inputs a P0 run must check: the .tfix.bridge
// bundles written by T1 (TestGoBackend, the cases that reach the backend)
// and the smoke fixtures (27 of the 110), measured on 2026-10-24 (see doc/go/j4-dashboard.md).
const (
	minDumpBundles = 110
	minDumpParsed  = 121 // inputs that reach the parser (T1 cases + smoke fixtures)
)

// TestDumpsRoundTrip re-reads in Go every dump that `mage bridgeCheck`
// collected in $TOMGO_DUMPS (tom -go --intermediate): the printed form
// (String(), the format of the bridge) must be identical to the file.
// The ToATerm round trip is informative only: an ATerm list does not name
// its operator, so reading it back gives the first variadic operator of
// the sort (the same limitation exists in Java).
func TestDumpsRoundTrip(t *testing.T) {
	dumps := os.Getenv(harness.DumpsEnv)
	if dumps == "" {
		t.Skipf("%s not set (run by mage bridgeCheck)", harness.DumpsEnv)
	}
	counts := map[string]int{}
	type ar struct{ ok, ko int }
	atermRT := map[string]*ar{}
	identical, different, bundles, smokeBundles := 0, 0, 0, 0
	err := filepath.WalkDir(dumps, func(p string, e fs.DirEntry, err error) error {
		if err != nil || e.IsDir() {
			return err
		}
		name := e.Name()
		i := strings.Index(name, ".tfix.")
		if i < 0 {
			return nil
		}
		suffix := name[i+len(".tfix."):]
		raw, err := os.ReadFile(p)
		if err != nil {
			return err
		}
		content := string(raw)
		counts[suffix]++
		if suffix == "bridge" && strings.HasPrefix(filepath.Base(filepath.Dir(p)), "test_go_smoke_") {
			smokeBundles++
		}
		var same bool
		var viaATerm func() bool
		func() {
			defer func() {
				if r := recover(); r != nil {
					t.Errorf("%s: panic: %v", p, r)
					same = false
				}
			}()
			switch {
			case suffix == "bridge":
				bundles++
				b, err := bridge.Read(strings.NewReader(content))
				if err != nil {
					t.Errorf("%s: %v", p, err)
					return
				}
				var sb strings.Builder
				if err := b.Write(&sb); err != nil {
					t.Errorf("%s: write: %v", p, err)
					return
				}
				same = sb.String() == content
			case suffix == "parsed.table":
				// TomParserPlugin writes symbolTable.toTerm().toATerm(): the
				// ATerm syntax (lists without operator), not String(). The
				// ATerm text must re-print identically; reading it as a
				// table and writing it back is informative.
				at := aterm.GetInstance().Parse(content)
				// Java's ATerm writer escapes a quote inside a string
				// (\'), the Go writer does not: compare modulo that escape.
				want := strings.ReplaceAll(strings.TrimSuffix(content, "\n"), "\\'", "'")
				same = fmt.Sprint(at) == want
				viaATerm = func() bool {
					return fmt.Sprint(adt.TomSymbolTableFromTerm(at).ToATerm()) == want
				}
			case strings.HasSuffix(suffix, ".table"):
				v := adt.TomSymbolTableFromString(content)
				same = fmt.Sprint(v) == strings.TrimSuffix(content, "\n")
				viaATerm = func() bool { return adt.TomSymbolTableFromTerm(v.ToATerm()) == v }
			default:
				v := adt.CodeFromString(content)
				same = fmt.Sprint(v) == strings.TrimSuffix(content, "\n")
				viaATerm = func() bool { return adt.CodeFromTerm(v.ToATerm()) == v }
			}
		}()
		if same {
			identical++
		} else {
			different++
			t.Errorf("%s: String() differs from the file", p)
		}
		if viaATerm != nil {
			r := atermRT[suffix]
			if r == nil {
				r = &ar{}
				atermRT[suffix] = r
			}
			ok := false
			func() {
				defer func() { recover() }()
				ok = viaATerm()
			}()
			if ok {
				r.ok++
			} else {
				r.ko++
			}
		}
		return nil
	})
	if err != nil {
		t.Fatal(err)
	}
	total := identical + different
	sufs := make([]string, 0, len(counts))
	for s := range counts {
		sufs = append(sufs, s)
	}
	sort.Strings(sufs)
	for _, s := range sufs {
		line := fmt.Sprintf("dumps .tfix.%s: %d", s, counts[s])
		if r := atermRT[s]; r != nil {
			line += fmt.Sprintf(" (ToATerm round trip, informative: %d identical / %d different)", r.ok, r.ko)
		}
		t.Log(line)
	}
	t.Logf("bundles (.tfix.bridge): %d, of which smoke: %d", bundles, smokeBundles)
	t.Logf("total %d, identiques %d / différents %d", total, identical, different)
	if total == 0 {
		t.Fatal("no dump found")
	}
	if bundles < minDumpBundles {
		t.Fatalf("only %d bundles checked, want at least %d", bundles, minDumpBundles)
	}
	if counts["parsed"] < minDumpParsed {
		t.Fatalf("only %d inputs parsed, want at least %d", counts["parsed"], minDumpParsed)
	}
	if different > 0 {
		t.Fatalf("%d dumps differ", different)
	}
}
