// Package harness drives built Tom Java distributions (legacy/ and the
// repository root) and temporary Go modules for the cross-checking
// harnesses T0–T3. Every helper skips the calling test when a required
// tool (java, javac, cc, a built dist) is missing, unless
// TOMGO_REQUIRE_TOOLS=1 (set by the mage targets portGo, portJava, smoke
// and coverage, and implied by -write-dashboard): then a missing tool is
// a failure, so that a run without its tools cannot pass vacuously. A
// stale src/dist (older than the compiler's sources) is always a failure.
package harness

import (
	"bytes"
	"context"
	"fmt"
	"io/fs"
	"os"
	"os/exec"
	"path/filepath"
	"slices"
	"sort"
	"strings"
	"sync"
	"testing"
	"time"
)

// RequireToolsEnv is the environment variable that turns every
// missing-tool skip of the harness into a failure (value "1").
const RequireToolsEnv = "TOMGO_REQUIRE_TOOLS"

// RequireTools reports whether a missing tool must fail the test.
func RequireTools() bool { return os.Getenv(RequireToolsEnv) == "1" }

// Missing reports a missing tool: it skips the test, or fails it when
// RequireTools.
func Missing(t testing.TB, format string, args ...any) {
	t.Helper()
	msg := fmt.Sprintf(format, args...)
	if RequireTools() {
		t.Fatalf("%s (%s=1: required tools must be present)", msg, RequireToolsEnv)
	}
	t.Skip(msg)
}

// LookPath returns the path of the command name, or reports it missing
// (Missing).
func LookPath(t testing.TB, name string) string {
	t.Helper()
	p, err := exec.LookPath(name)
	if err != nil {
		Missing(t, "%s not found in PATH", name)
	}
	return p
}

// The test tree. This is the ONE place that knows where its parts live:
// every other file derives its paths from these functions.
//
//	test/java  the Tom+Java tests (the originals; built by test/java/build.xml)
//	test/go    the Tom+Go ports and their reference data (testdata/, smoke/testdata)
//	test/tools the programs that compare the three (module tomtools)
const (
	testDirName  = "test"
	javaDirName  = "java"
	goDirName    = "go"
	toolsDirName = "tools"
)

// JavaTestDir is the directory of the Tom+Java tests under root.
func JavaTestDir(root string) string { return filepath.Join(root, testDirName, javaDirName) }

// GoTestDir is the directory of the Tom+Go tests (ports, oracles) under root.
func GoTestDir(root string) string { return filepath.Join(root, testDirName, goDirName) }

// ToolsDir is the directory of the comparison tools (module tomtools).
func ToolsDir(root string) string { return filepath.Join(root, testDirName, toolsDirName) }

// FindRoot walks up from the working directory to the directory that
// holds both build.xml and the Java test tree's build.xml.
func FindRoot() (string, error) {
	dir, err := os.Getwd()
	if err != nil {
		return "", err
	}
	for {
		if fileExists(filepath.Join(dir, "build.xml")) && fileExists(filepath.Join(JavaTestDir(dir), "build.xml")) {
			return dir, nil
		}
		parent := filepath.Dir(dir)
		if parent == dir {
			return "", fmt.Errorf("repository root not found")
		}
		dir = parent
	}
}

// RepoRoot is FindRoot, failing the test when the root is not found.
func RepoRoot(t testing.TB) string {
	t.Helper()
	root, err := FindRoot()
	if err != nil {
		t.Fatal(err)
	}
	return root
}

// ChdirGoTest changes the working directory to test/go/<sub> (test/go
// when sub is empty), where the ports and their reference data live: the
// runners address them by relative paths (testdata/golden, <Rel>.t, …).
// For TestMain; it exits the process on failure.
func ChdirGoTest(sub string) {
	root, err := FindRoot()
	if err == nil {
		err = os.Chdir(filepath.Join(GoTestDir(root), sub))
	}
	if err != nil {
		fmt.Fprintln(os.Stderr, "harness.ChdirGoTest:", err)
		os.Exit(1)
	}
}

func fileExists(p string) bool { _, err := os.Stat(p); return err == nil }

// Dist is a built Tom distribution (the directory TOM_HOME points to).
type Dist struct{ Home string }

func dist(t testing.TB, env, rel string) Dist {
	t.Helper()
	LookPath(t, "java")
	home := os.Getenv(env)
	if home == "" {
		home = filepath.Join(RepoRoot(t), rel)
	}
	if !fileExists(filepath.Join(home, "bin", "tom")) {
		Missing(t, "no Tom distribution at %s (build it, or set %s)", home, env)
	}
	return Dist{Home: home}
}

// LegacyDist is the human-written compiler built in legacy/.
func LegacyDist(t testing.TB) Dist { return dist(t, "TOM_LEGACY_HOME", "legacy/src/dist") }

// NewDist is the extended compiler built at the repository root. It
// fails (whatever RequireTools) when the dist is older than the
// compiler's sources (CheckFresh).
func NewDist(t testing.TB) Dist {
	t.Helper()
	d := dist(t, "TOM_NEW_HOME", "src/dist")
	if err := freshness(RepoRoot(t), d.Home); err != nil {
		t.Fatal(err)
	}
	d.Bin(t, "gomgo")
	return d
}

// Bin is the Go command name installed in the dist (<home>/bin/name,
// by mage installGo, called by ./build.sh src.dist and stable); a
// missing binary fails the test: the harness uses the binaries of the
// dist under test, never builds them.
func (d Dist) Bin(t testing.TB, name string) string {
	t.Helper()
	p := filepath.Join(d.Home, "bin", name)
	if !fileExists(p) {
		t.Fatalf("no %s in the dist %s: rebuild it (unset TOM_HOME && ./build.sh src.dist)", name, d.Home)
	}
	return p
}

var (
	freshMu    sync.Mutex
	freshCache = map[string]error{}
)

// freshness is CheckFresh, computed once per dist home.
func freshness(root, home string) error {
	freshMu.Lock()
	defer freshMu.Unlock()
	if err, ok := freshCache[home]; ok {
		return err
	}
	err := CheckFresh(root, home)
	freshCache[home] = err
	return err
}

// CheckFreshCompiler returns an error when the compiler of the dist at
// home (lib/tom-compiler-full.jar) is older than one of the sources it is
// built from: src/tom/**/*.{t,java,tom,config}, src/tom/library/mapping/**
// and src/config/*.config of the repository root. regentomgo needs only
// this: it runs the compiler, not the Go commands of the dist.
func CheckFreshCompiler(root, home string) error {
	jar := filepath.Join(home, "lib", "tom-compiler-full.jar")
	st, err := os.Stat(jar)
	if err != nil {
		return fmt.Errorf("no compiler jar in the dist: %v (rebuild ./build.sh src.dist)", err)
	}
	var newest string
	var newestTime time.Time
	consider := func(p string, d fs.DirEntry) {
		if info, err := d.Info(); err == nil && info.ModTime().After(newestTime) {
			newest, newestTime = p, info.ModTime()
		}
	}
	src := filepath.Join(root, "src", "tom")
	mapping := filepath.Join(src, "library", "mapping")
	err = filepath.WalkDir(src, func(p string, d fs.DirEntry, err error) error {
		if err != nil {
			return err
		}
		if d.IsDir() {
			return nil
		}
		if hasAnySuffix(p, ".t", ".java", ".tom", ".config") || strings.HasPrefix(p, mapping+string(filepath.Separator)) {
			consider(p, d)
		}
		return nil
	})
	if err != nil {
		return fmt.Errorf("scanning the compiler's sources: %v", err)
	}
	cfgs, _ := filepath.Glob(filepath.Join(root, "src", "config", "*.config"))
	{
		for _, p := range cfgs {
			if info, err := os.Stat(p); err == nil && info.ModTime().After(newestTime) {
				newest, newestTime = p, info.ModTime()
			}
		}
	}
	if newest != "" && newestTime.After(st.ModTime()) {
		rel, _ := filepath.Rel(root, newest)
		return fmt.Errorf("stale compiler: %s (%s) is newer than %s (%s): rebuild ./build.sh src.dist",
			rel, newestTime.Format(time.RFC3339), jar, st.ModTime().Format(time.RFC3339))
	}
	return nil
}

// CheckFresh is CheckFreshCompiler plus the Go commands installed in
// bin/: it also fails when a Go command installed there (gomgo, tomgo) is
// older than the newest src/go/tom/**/*.go (not *_test.go, not testdata/)
// or *.gom. (The Go runtime the dist ships is compared with src/go/tom by
// SetupGoModule.)
func CheckFresh(root, home string) error {
	if err := CheckFreshCompiler(root, home); err != nil {
		return err
	}
	// the Go commands installed in bin/
	var goNewest string
	var goNewestTime time.Time
	_ = filepath.WalkDir(filepath.Join(root, "src", "go", "tom"), func(p string, d fs.DirEntry, err error) error {
		if err != nil {
			return nil
		}
		if d.IsDir() {
			if d.Name() == "testdata" {
				return filepath.SkipDir
			}
			return nil
		}
		if (strings.HasSuffix(p, ".go") && !strings.HasSuffix(p, "_test.go")) || strings.HasSuffix(p, ".gom") {
			if info, err := d.Info(); err == nil && info.ModTime().After(goNewestTime) {
				goNewest, goNewestTime = p, info.ModTime()
			}
		}
		return nil
	})
	for _, name := range []string{"gomgo", "tomgo"} {
		bin := filepath.Join(home, "bin", name)
		bst, err := os.Stat(bin)
		if err != nil {
			continue
		}
		if goNewest != "" && goNewestTime.After(bst.ModTime()) {
			rel, _ := filepath.Rel(root, goNewest)
			return fmt.Errorf("stale Go command: %s is newer than %s: rebuild ./build.sh src.dist", rel, bin)
		}
	}
	return nil
}

// Environment variables of the Java -> Go bridge checks (J4.0d, P0); they
// are read by Compile for a dist that ships bin/tomgo (never legacy/) and
// only when the arguments contain -go.
//
//	TOMGO_GOFROM=<phase>  adds --goFrom <phase>
//	TOMGO_GOCHECK=1       adds --goFrom Backend --goCheck (exclusive with TOMGO_GOFROM)
//	TOMGO_DUMPS=<dir>     adds --intermediate and moves every *.tfix* dump of
//	                      the output directory to <dir>/<unique name>/
const (
	GoFromEnv  = "TOMGO_GOFROM"
	GoCheckEnv = "TOMGO_GOCHECK"
	DumpsEnv   = "TOMGO_DUMPS"
)

// Compile runs `bin/tom -d outDir args… src` with TOM_HOME=d.Home and
// GOMGO_BIN set to the dist's bin/gomgo (when it exists), and returns the combined stdout+stderr.
// See GoFromEnv for the bridge variables.
func (d Dist) Compile(t testing.TB, src, outDir string, args ...string) (string, error) {
	t.Helper()
	env := []string{"TOM_HOME=" + d.Home}
	// the compiler of stable/ still reads GOMGO_BIN; legacy/ has no gomgo
	if g := filepath.Join(d.Home, "bin", "gomgo"); fileExists(g) {
		env = append(env, "GOMGO_BIN="+g)
	}
	dumps := ""
	if fileExists(filepath.Join(d.Home, "bin", "tomgo")) && slices.Contains(args, "-go") {
		from, check := os.Getenv(GoFromEnv), os.Getenv(GoCheckEnv) == "1"
		if from != "" && check {
			t.Fatalf("%s and %s=1 are exclusive", GoFromEnv, GoCheckEnv)
		}
		if check {
			args = append(slices.Clone(args), "--goFrom", "Backend", "--goCheck")
		} else if from != "" {
			args = append(slices.Clone(args), "--goFrom", from)
		}
		if dumps = os.Getenv(DumpsEnv); dumps != "" {
			args = append(slices.Clone(args), "--intermediate")
		}
	}
	full := append(append([]string{"-d", outDir}, args...), src)
	out, err := RunIn(filepath.Dir(src), env, filepath.Join(d.Home, "bin", "tom"), full...)
	if dumps != "" {
		moveDumps(t, src, outDir, dumps)
	}
	return out, err
}

// moveDumps moves every *.tfix* file under outDir into a new directory of
// dumps named after src (path relative to the repository, / -> _, plus a
// counter when taken), so that goldens and build directories stay clean.
func moveDumps(t testing.TB, src, outDir, dumps string) {
	t.Helper()
	name := filepath.Base(src)
	if root, err := FindRoot(); err == nil {
		if rel, err := filepath.Rel(root, src); err == nil && !strings.HasPrefix(rel, "..") {
			name = strings.ReplaceAll(rel, string(filepath.Separator), "_")
		}
	}
	dir := filepath.Join(dumps, name)
	for n := 2; fileExists(dir); n++ {
		dir = filepath.Join(dumps, fmt.Sprintf("%s.%d", name, n))
	}
	if err := os.MkdirAll(dir, 0o755); err != nil {
		t.Fatal(err)
	}
	filepath.WalkDir(outDir, func(p string, e fs.DirEntry, err error) error {
		if err != nil || e.IsDir() || !strings.Contains(e.Name(), ".tfix") {
			return nil
		}
		if err := os.Rename(p, filepath.Join(dir, e.Name())); err != nil {
			t.Errorf("moving dump %s: %v", p, err)
		}
		return nil
	})
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

// RunInTimeout is RunIn with a deadline: the command is killed after d
// and the error then wraps context.DeadlineExceeded.
func RunInTimeout(dir string, d time.Duration, env []string, name string, args ...string) (string, error) {
	ctx, cancel := context.WithTimeout(context.Background(), d)
	defer cancel()
	cmd := exec.CommandContext(ctx, name, args...)
	cmd.Dir = dir
	cmd.Env = append(os.Environ(), env...)
	cmd.WaitDelay = time.Second
	out, err := cmd.CombinedOutput()
	if ctx.Err() != nil {
		err = fmt.Errorf("timeout after %v: %w", d, ctx.Err())
	}
	return string(out), err
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

// runtimeDiff compares the Go runtime of src and dist — the non-test .go
// files of library/ and go.mod, the files build.xml copies into
// <dist>/lib/go/tom (byte equality, relative paths, ignoring *_test.go
// and testdata/) — and returns the first differing relative path, or "".
// gomgo and the compiler's ADTs (gom/, engine/) are not shipped as
// sources: gomgo/tomgo are installed in <dist>/bin by mage installGo.
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
			lib := strings.HasPrefix(p, filepath.Join(root, "library")+string(filepath.Separator))
			if (lib && strings.HasSuffix(n, ".go") && !strings.HasSuffix(n, "_test.go")) || (n == "go.mod" && filepath.Dir(p) == root) {
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

func hasAnySuffix(p string, suffixes ...string) bool {
	for _, s := range suffixes {
		if strings.HasSuffix(p, s) {
			return true
		}
	}
	return false
}
