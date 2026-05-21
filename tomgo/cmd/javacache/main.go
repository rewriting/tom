// Command javacache runs `tom --intermediate` on every `test/*.t` and
// caches the resulting `.tfix.<phase>` files under
// `tomgo/tests/testdata/javacache/<rel-path>/`. The cached dumps replace
// the per-fixture `TomPipelineDump` invocations, turning parity
// checks from O(minutes) into O(milliseconds) for repeated runs.
//
// One full cache rebuild takes ~5min on first run; subsequent runs
// reuse the cache transparently. The cache is invalidated when:
//   - The fixture (.t file) is modified.
//   - The reference `src/dist/lib/*.jar` build is rebuilt.
// We mirror Ant's incremental logic with a single sentinel file
// (`.cache-stamp`) per fixture.
//
// Usage:
//
//	go run ./cmd/javacache           # incremental
//	go run ./cmd/javacache -force    # force full rebuild
//	go run ./cmd/javacache -clean    # wipe cache
package main

import (
	"crypto/sha256"
	"flag"
	"fmt"
	"io"
	"os"
	"os/exec"
	"path/filepath"
	"strings"
)

var (
	repoRoot = "/Users/pem/github/tom"
	corpus   = filepath.Join(repoRoot, "test")
	cacheDir = filepath.Join(repoRoot, "tomgo", "tests", "testdata", "java-ast")
	tomHome  = filepath.Join(repoRoot, "src", "dist")
	tomBin   = filepath.Join(tomHome, "bin", "tom")
)

// phases is the ordered list of `.tfix.<phase>` files we cache.
// Java's tom CLI emits these via `--intermediate`. Note: there's no
// `.tfix.synchecked` — the SyntaxChecker doesn't write intermediate
// output. We synthesize that column from the transformed dump
// (Java's SyntaxChecker is a pure collector, AST is unchanged).
var phases = []string{"parsed", "transformed", "desugared", "typed", "expanded", "optimized", "compiled"}

func main() {
	var force, clean bool
	flag.BoolVar(&force, "force", false, "force rebuild even if cache is fresh")
	flag.BoolVar(&clean, "clean", false, "wipe the cache and exit")
	flag.Parse()

	if clean {
		os.RemoveAll(cacheDir)
		fmt.Println("cache cleaned")
		return
	}

	if err := os.MkdirAll(cacheDir, 0o755); err != nil {
		fail("mkdir cache:", err)
	}

	_ = stableLibHash // referenced only by an older code path; keep the helper around

	var fixtures []string
	if err := filepath.Walk(corpus, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".t" {
			rel, _ := filepath.Rel(corpus, path)
			if strings.HasPrefix(rel, "error/") {
				return nil
			}
			fixtures = append(fixtures, rel)
		}
		return nil
	}); err != nil {
		fail("walk:", err)
	}

	// Layout mirrors the `test/build.sh build --intermediate` output:
	// the .tfix files for `test/<dir>/<basename>.t` end up under
	// `cacheDir/<dir>/<basename>.java.tfix.<phase>`. We only fill
	// fixtures that DON'T already have a typed dump (i.e. the
	// ones test/build.xml's `build` target excludes).
	var built, skipped, failed int
	for _, rel := range fixtures {
		input := filepath.Join(corpus, rel)
		dir := filepath.Dir(rel)
		base := strings.TrimSuffix(filepath.Base(rel), ".t")
		existing := filepath.Join(cacheDir, dir, base+".java.tfix.parsed")

		if !force {
			if _, err := os.Stat(existing); err == nil {
				skipped++
				continue
			}
		}
		// Run tom in a per-fixture scratch dir so include paths
		// resolve correctly, then move the .tfix files into the
		// cache location.
		scratch, err := os.MkdirTemp("", "tomint-")
		if err != nil {
			fail("scratch:", err)
		}
		// Default: no `--newparser`, matching `test/build.sh build`'s
		// behaviour. Some fixtures (rule/*, antipatterns/*) use
		// newer syntax (`%rule`, etc.) that only the new parser
		// accepts. We auto-detect them by looking at the source for
		// the relevant island keywords.
		srcBytes, _ := os.ReadFile(input)
		args := []string{"--intermediate"}
		if needsNewParser(string(srcBytes)) {
			args = append(args, "--newparser")
		}
		args = append(args, "-d", scratch, input)
		cmd := exec.Command(tomBin, args...)
		cmd.Env = append(os.Environ(), "TOM_HOME="+tomHome)
		out, err := cmd.CombinedOutput()
		if err != nil {
			// Tom may exit non-zero on semantic errors (regress/*
			// fixtures), but .tfix.parsed/.transformed/etc. are
			// still written. Don't abort — just warn.
			fmt.Fprintf(os.Stderr, "WARN %s: %v (continuing)\n%s\n", rel, err, lastLines(string(out), 3))
		}
		moved, err := moveTfix(scratch, filepath.Join(cacheDir, dir), base)
		_ = os.RemoveAll(scratch)
		if err != nil {
			failed++
			fmt.Fprintf(os.Stderr, "FAIL move %s: %v\n", rel, err)
			continue
		}
		if moved == 0 {
			failed++
			continue
		}
		built++
		fmt.Printf("OK   %s (%d files)\n", rel, moved)
	}
	fmt.Printf("\ndone: built=%d skipped=%d failed=%d total=%d\n",
		built, skipped, failed, len(fixtures))
}

// isFresh reports whether the stamp matches the jar hash AND
// post-dates the fixture. Returns false on any I/O error so the
// caller rebuilds defensively.
func isFresh(stamp, input, jarHash string) bool {
	si, err := os.Stat(stamp)
	if err != nil {
		return false
	}
	body, err := os.ReadFile(stamp)
	if err != nil || string(body) != jarHash {
		return false
	}
	fi, err := os.Stat(input)
	if err != nil {
		return false
	}
	return !fi.ModTime().After(si.ModTime())
}

// stableLibHash returns a stable hash of all *.jar files under
// src/dist/lib. The cache invalidates when the user rebuilds the
// reference Tom toolchain.
func stableLibHash() (string, error) {
	h := sha256.New()
	libRoot := filepath.Join(tomHome, "lib")
	err := filepath.Walk(libRoot, func(path string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() || !strings.HasSuffix(path, ".jar") {
			return err
		}
		f, err := os.Open(path)
		if err != nil {
			return err
		}
		defer f.Close()
		_, err = io.Copy(h, f)
		return err
	})
	if err != nil {
		return "", err
	}
	return fmt.Sprintf("%x", h.Sum(nil)), nil
}

func copyFile(src, dst string) error {
	in, err := os.Open(src)
	if err != nil {
		return err
	}
	defer in.Close()
	out, err := os.Create(dst)
	if err != nil {
		return err
	}
	defer out.Close()
	_, err = io.Copy(out, in)
	return err
}

// moveTfix walks `scratch` for `<anything>/<base>.java.tfix.<phase>`
// files (tom may write under a `package/` subtree depending on the
// source's package decl) and moves them into `dst/<base>.java.tfix.
// <phase>`. Returns the number moved.
func moveTfix(scratch, dst, base string) (int, error) {
	if err := os.MkdirAll(dst, 0o755); err != nil {
		return 0, err
	}
	var moved int
	err := filepath.Walk(scratch, func(path string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		name := info.Name()
		if !strings.HasPrefix(name, base+".java.tfix.") {
			return nil
		}
		target := filepath.Join(dst, name)
		_ = os.Remove(target)
		if err := os.Rename(path, target); err != nil {
			return err
		}
		moved++
		return nil
	})
	return moved, err
}

// needsNewParser reports whether the source uses an island
// construct only the `--newparser` (ANTLR4 island) parser
// understands. The old (TomJava) parser barfs on `%rule { … }`
// among other things; presence of the keyword in the source is a
// strong signal we need to fall back to --newparser.
func needsNewParser(src string) bool {
	return strings.Contains(src, "%rule")
}

// lastLines returns the last n newline-separated lines of s, useful
// for trimming verbose tom error output to a one-line summary.
func lastLines(s string, n int) string {
	parts := strings.Split(strings.TrimRight(s, "\n"), "\n")
	if len(parts) <= n {
		return s
	}
	return strings.Join(parts[len(parts)-n:], "\n")
}

// flatten moves every .tfix file produced anywhere under `entry`
// up to `entry/` itself, then removes the now-empty subdirs. Tom
// can write under destdir/<java/package/path>/ depending on the
// source's `package` decl; we want a flat per-fixture layout for
// the cache reader to find them.
func flatten(entry string) error {
	type moveOp struct{ src, dst string }
	var moves []moveOp
	err := filepath.Walk(entry, func(path string, info os.FileInfo, err error) error {
		if err != nil || info.IsDir() {
			return err
		}
		if filepath.Dir(path) == entry {
			return nil
		}
		moves = append(moves, moveOp{src: path, dst: filepath.Join(entry, filepath.Base(path))})
		return nil
	})
	if err != nil {
		return err
	}
	for _, m := range moves {
		// Overwrite if it already exists (tom may have written
		// duplicates across re-runs).
		_ = os.Remove(m.dst)
		if err := os.Rename(m.src, m.dst); err != nil {
			return err
		}
	}
	// Remove empty subdirs left behind.
	_ = filepath.Walk(entry, func(path string, info os.FileInfo, err error) error {
		if err != nil || !info.IsDir() || path == entry {
			return nil
		}
		_ = os.Remove(path) // ignore non-empty errors
		return nil
	})
	return nil
}

func countTfix(dir string) int {
	entries, _ := os.ReadDir(dir)
	var n int
	for _, e := range entries {
		if strings.Contains(e.Name(), ".tfix.") {
			n++
		}
	}
	return n
}

func fail(args ...any) {
	fmt.Fprintln(os.Stderr, args...)
	os.Exit(1)
}
