// Package smoke compiles small Tom+Go programs with the root compiler
// (tom -go -np), runs them with `go run`, and compares their stdout with
// the expected output stored next to each fixture (test/go/smoke/testdata) (extra tom options in
// an optional args.txt).
//
// A fixture testdata/<name>/<name>.t whose expected output was measured
// on Java keeps its Tom+Java twin in testdata/<name>/twin/Twin.t (class
// smoke.Twin, ignored by TestGoSmoke): TestJavaTwins compiles it with
// legacy/, runs it under the JVM and requires the same expected.txt.
package smoke

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	"tomtools/harness"
)

// The fixtures live in test/go/smoke/testdata; the tests address them by
// relative paths.
func TestMain(m *testing.M) {
	harness.ChdirGoTest("smoke")
	os.Exit(m.Run())
}

func TestGoSmoke(t *testing.T) {
	harness.LookPath(t, "go")
	dist := harness.NewDist(t)
	dirs, err := filepath.Glob("testdata/*")
	if err != nil {
		t.Fatal(err)
	}
	if len(dirs) == 0 {
		t.Fatal("no fixture under testdata/")
	}
	for _, d := range dirs {
		name := filepath.Base(d)
		src, err := filepath.Abs(filepath.Join(d, name+".t"))
		if err != nil {
			t.Fatal(err)
		}
		if _, err := os.Stat(src); err != nil {
			continue
		}
		t.Run(name, func(t *testing.T) {
			want, err := os.ReadFile(filepath.Join(d, "expected.txt"))
			if err != nil {
				t.Fatal(err)
			}
			out := t.TempDir()
			args := []string{"-go", "-np"}
			// optional extra tom options, one per line
			if extra, err := os.ReadFile(filepath.Join(d, "args.txt")); err == nil {
				args = append(args, strings.Fields(string(extra))...)
			}
			if msg, err := dist.Compile(t, src, out, args...); err != nil {
				t.Fatalf("tom -go: %v\n%s", err, msg)
			}
			gen := filepath.Join(out, name+".go")
			code, err := os.ReadFile(gen)
			if err != nil {
				t.Fatalf("generated file: %v", err)
			}
			if strings.Contains(string(code), "\"tom/") {
				harness.SetupGoModule(t, out)
			} else if err := os.WriteFile(filepath.Join(out, "go.mod"), []byte("module smoke\n\ngo 1.22\n"), 0o644); err != nil {
				t.Fatal(err)
			}
			cmd := exec.Command("go", "run", ".")
			cmd.Dir = out
			var stderr strings.Builder
			cmd.Stderr = &stderr
			got, err := cmd.Output()
			if err != nil {
				t.Fatalf("go run: %v\n%s\ngenerated code:\n%s", err, stderr.String(), code)
			}
			if string(got) != string(want) {
				t.Errorf("stdout mismatch\n got: %q\nwant: %q", got, want)
			}
		})
	}
}

// TestJavaTwins checks the expected output of the fixtures that have a
// Tom+Java twin (testdata/<name>/twin/Twin.t, class smoke.Twin): compiled
// by legacy/ and run under the JVM, it prints expected.txt.
func TestJavaTwins(t *testing.T) {
	twins, err := filepath.Glob("testdata/*/twin/Twin.t")
	if err != nil {
		t.Fatal(err)
	}
	if len(twins) == 0 {
		t.Fatal("no twin under testdata/*/twin/")
	}
	legacy := harness.LegacyDist(t)
	harness.LookPath(t, "javac")
	cp := filepath.Join(legacy.Home, "lib", "tom-runtime-full.jar")
	for _, twin := range twins {
		d := filepath.Dir(filepath.Dir(twin))
		t.Run(filepath.Base(d), func(t *testing.T) {
			want, err := os.ReadFile(filepath.Join(d, "expected.txt"))
			if err != nil {
				t.Fatal(err)
			}
			src, err := filepath.Abs(twin)
			if err != nil {
				t.Fatal(err)
			}
			out := t.TempDir()
			if msg, err := legacy.Compile(t, src, out); err != nil {
				t.Fatalf("legacy tom: %v\n%s", err, msg)
			}
			var javaFiles []string
			filepath.Walk(out, func(p string, info os.FileInfo, err error) error {
				if err == nil && strings.HasSuffix(p, ".java") {
					javaFiles = append(javaFiles, p)
				}
				return err
			})
			args := append([]string{"-nowarn", "-encoding", "UTF-8", "-cp", cp, "-d", out}, javaFiles...)
			if msg, err := harness.RunIn(out, nil, "javac", args...); err != nil {
				t.Fatalf("javac: %v\n%s", err, msg)
			}
			cmd := exec.Command("java", "-cp", cp+string(os.PathListSeparator)+out, "smoke.Twin")
			var stderr strings.Builder
			cmd.Stderr = &stderr
			got, err := cmd.Output()
			if err != nil {
				t.Fatalf("java: %v\n%s", err, stderr.String())
			}
			if string(got) != string(want) {
				t.Errorf("Java twin stdout differs from expected.txt\n got: %q\nwant: %q", got, want)
			}
		})
	}
}
