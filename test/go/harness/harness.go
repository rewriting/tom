// Package harness drives built Tom Java distributions (legacy/ and the
// repository root) and temporary Go modules for the cross-checking
// harnesses T0–T3. Every helper skips the calling test when a required
// tool (java, a built dist) is missing.
package harness

import (
	"bytes"
	"fmt"
	"io/fs"
	"os"
	"os/exec"
	"path/filepath"
	"sort"
	"strings"
	"sync"
	"testing"
)

// RepoRoot walks up from the working directory to the directory that
// holds both build.xml and test/go/go.mod.
func RepoRoot(t testing.TB) string {
	t.Helper()
	dir, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	for {
		if fileExists(filepath.Join(dir, "build.xml")) && fileExists(filepath.Join(dir, "test", "go", "go.mod")) {
			return dir
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			t.Fatal("repository root not found")
		}
		dir = parent
	}
}

func fileExists(p string) bool { _, err := os.Stat(p); return err == nil }

// Dist is a built Tom distribution (the directory TOM_HOME points to).
type Dist struct{ Home string }

func dist(t testing.TB, env, rel string) Dist {
	t.Helper()
	if _, err := exec.LookPath("java"); err != nil {
		t.Skip("java not found in PATH")
	}
	home := os.Getenv(env)
	if home == "" {
		home = filepath.Join(RepoRoot(t), rel)
	}
	if !fileExists(filepath.Join(home, "bin", "tom")) {
		t.Skipf("no Tom distribution at %s (build it, or set %s)", home, env)
	}
	return Dist{Home: home}
}

// LegacyDist is the human-written compiler built in legacy/.
func LegacyDist(t testing.TB) Dist { return dist(t, "TOM_LEGACY_HOME", "legacy/src/dist") }

// NewDist is the extended compiler built at the repository root.
func NewDist(t testing.TB) Dist { return dist(t, "TOM_NEW_HOME", "src/dist") }

// Compile runs `bin/tom -d outDir args… src` with TOM_HOME=d.Home and
// GOMGO_BIN set, and returns the combined stdout+stderr.
func (d Dist) Compile(t testing.TB, src, outDir string, args ...string) (string, error) {
	t.Helper()
	full := append(append([]string{"-d", outDir}, args...), src)
	env := []string{"TOM_HOME=" + d.Home, "GOMGO_BIN=" + GomgoBin(t)}
	return RunIn(filepath.Dir(src), env, filepath.Join(d.Home, "bin", "tom"), full...)
}

// RunIn runs a command in dir with extra environment variables and
// returns its combined output.
func RunIn(dir string, env []string, name string, args ...string) (string, error) {
	cmd := exec.Command(name, args...)
	cmd.Dir = dir
	cmd.Env = append(os.Environ(), env...)
	out, err := cmd.CombinedOutput()
	return string(out), err
}

var (
	gomgoOnce sync.Once
	gomgoPath string
	gomgoErr  error
)

// GomgoBin builds the gomgo command (tom/gom/cmd/gomgo, from
// src/go/tom) once per test binary and returns its path.
func GomgoBin(t testing.TB) string {
	t.Helper()
	root := RepoRoot(t)
	gomgoOnce.Do(func() {
		dir, err := os.MkdirTemp("", "gomgo-harness-")
		if err != nil {
			gomgoErr = err
			return
		}
		gomgoPath = filepath.Join(dir, "gomgo")
		var out string
		out, gomgoErr = RunIn(filepath.Join(root, "src", "go", "tom"), nil, "go", "build", "-o", gomgoPath, "./gom/cmd/gomgo")
		if gomgoErr != nil {
			gomgoErr = fmt.Errorf("%v\n%s", gomgoErr, out)
		}
	})
	if gomgoErr != nil {
		t.Fatalf("building gomgo: %v", gomgoErr)
	}
	return gomgoPath
}

// SetupGoModule writes a go.mod in dir that resolves module tom (the Go
// runtime: sl, sharedobjects, aterm) to the copy shipped in the new dist
// (<dist>/lib/go/tom), so generated code can import it.
func SetupGoModule(t testing.TB, dir string) {
	t.Helper()
	runtime := filepath.Join(NewDist(t).Home, "lib", "go", "tom")
	if !fileExists(filepath.Join(runtime, "go.mod")) {
		t.Fatalf("no Go runtime at %s (rebuild src.dist: ./build.sh src.dist)", runtime)
	}
	if diff := runtimeDiff(filepath.Join(RepoRoot(t), "src", "go", "tom"), runtime); diff != "" {
		t.Fatalf("dist runtime is stale: rebuild with ./build.sh src.dist (or stable); first difference: %s", diff)
	}
	mod := "module fixture\n\ngo 1.26\n\nrequire tom v0.0.0\n\nreplace tom => " + runtime + "\n"
	if err := os.WriteFile(filepath.Join(dir, "go.mod"), []byte(mod), 0o644); err != nil {
		t.Fatal(err)
	}
	if out, err := RunIn(dir, nil, "go", "mod", "tidy"); err != nil {
		t.Fatalf("go mod tidy: %v\n%s", err, out)
	}
}

// Normalize replaces machine-specific paths by stable placeholders.
func Normalize(s string, repl map[string]string) string {
	for from, to := range repl {
		s = strings.ReplaceAll(s, from, to)
	}
	return s
}

// runtimeDiff compares the non-test .go files and go.mod of src and dist
// (byte equality, relative paths, ignoring *_test.go and testdata/) and
// returns the first differing relative path, or "".
func runtimeDiff(src, dist string) string {
	collect := func(root string) map[string]bool {
		m := map[string]bool{}
		_ = filepath.WalkDir(root, func(p string, d fs.DirEntry, err error) error {
			if err != nil {
				return nil
			}
			if d.IsDir() {
				if d.Name() == "testdata" {
					return filepath.SkipDir
				}
				return nil
			}
			n := d.Name()
			if (strings.HasSuffix(n, ".go") && !strings.HasSuffix(n, "_test.go")) || (n == "go.mod" && filepath.Dir(p) == root) {
				r, _ := filepath.Rel(root, p)
				m[r] = true
			}
			return nil
		})
		return m
	}
	a, b := collect(src), collect(dist)
	var names []string
	for k := range a {
		names = append(names, k)
	}
	for k := range b {
		if !a[k] {
			names = append(names, k)
		}
	}
	sort.Strings(names)
	for _, n := range names {
		if !a[n] || !b[n] {
			return n
		}
		x, _ := os.ReadFile(filepath.Join(src, n))
		y, _ := os.ReadFile(filepath.Join(dist, n))
		if !bytes.Equal(x, y) {
			return n
		}
	}
	return ""
}
