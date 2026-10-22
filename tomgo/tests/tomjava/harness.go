// Package tomjava drives built Tom Java distributions (legacy/ and the
// repository root) and temporary Go modules for the cross-checking
// harnesses T0–T3. Every helper skips the calling test when a required
// tool (java, a built dist) is missing.
package tomjava

import (
	"os"
	"os/exec"
	"path/filepath"
	"strings"
	"sync"
	"testing"
)

// RepoRoot walks up from the working directory to the directory that
// holds both build.xml and tomgo/.
func RepoRoot(t testing.TB) string {
	t.Helper()
	dir, err := os.Getwd()
	if err != nil {
		t.Fatal(err)
	}
	for {
		if fileExists(filepath.Join(dir, "build.xml")) && fileExists(filepath.Join(dir, "tomgo", "go.mod")) {
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
// TOMGO_BIN set, and returns the combined stdout+stderr.
func (d Dist) Compile(t testing.TB, src, outDir string, args ...string) (string, error) {
	t.Helper()
	full := append(append([]string{"-d", outDir}, args...), src)
	env := []string{"TOM_HOME=" + d.Home, "TOMGO_BIN=" + TomgoBin(t)}
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
	tomgoOnce sync.Once
	tomgoPath string
	tomgoErr  error
)

// TomgoBin builds cmd/tomgo once per test binary and returns its path.
func TomgoBin(t testing.TB) string {
	t.Helper()
	root := RepoRoot(t)
	tomgoOnce.Do(func() {
		dir, err := os.MkdirTemp("", "tomgo-harness-")
		if err != nil {
			tomgoErr = err
			return
		}
		tomgoPath = filepath.Join(dir, "tomgo")
		_, tomgoErr = RunIn(filepath.Join(root, "tomgo"), nil, "go", "build", "-o", tomgoPath, "./cmd/tomgo")
	})
	if tomgoErr != nil {
		t.Fatalf("building tomgo: %v", tomgoErr)
	}
	return tomgoPath
}

// SetupGoModule writes a go.mod in dir that resolves tom/tomgo to the
// local checkout, so generated code can import sl and sharedobjects.
func SetupGoModule(t testing.TB, dir string) {
	t.Helper()
	tomgo := filepath.Join(RepoRoot(t), "tomgo")
	mod := "module fixture\n\ngo 1.22\n\nrequire tom/tomgo v0.0.0\n\nreplace tom/tomgo => " + tomgo + "\n"
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
