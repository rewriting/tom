// Package gosmoke compiles small Tom+Go programs with the root compiler
// (tom -go -np), runs them with `go run`, and compares their stdout with
// the expected output stored next to each fixture.
package gosmoke

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"testing"

	"tom/tomgo/tests/tomjava"
)

func TestGoSmoke(t *testing.T) {
	if _, err := exec.LookPath("go"); err != nil {
		t.Skip("go not found in PATH")
	}
	dist := tomjava.NewDist(t)
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
			if msg, err := dist.Compile(t, src, out, "-go", "-np"); err != nil {
				t.Fatalf("tom -go: %v\n%s", err, msg)
			}
			gen := filepath.Join(out, name+".go")
			code, err := os.ReadFile(gen)
			if err != nil {
				t.Fatalf("generated file: %v", err)
			}
			if strings.Contains(string(code), "\"tom/tomgo/") {
				tomjava.SetupGoModule(t, out)
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
