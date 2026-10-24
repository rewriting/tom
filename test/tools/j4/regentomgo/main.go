// Command regentomgo compiles the Tom+Go sources of the compiler
// (src/go/tom/engine/**/*.t, testdata excluded) with the dist src/dist
// (spec J4 §4.3, decision D4) into X.t.go next to each X.t:
//
//	tom -go -np -I src/go/tom/engine/adt -o <tmp>/X.t.go X.t
//
// A generated file is written only when its content changes (mage
// tomGoFresh and CheckFresh compare dates). The compiler of the dist must
// be fresh (harness.CheckFreshCompiler). Each generated file is also
// compared with the amorce (stable/go/tom, same path): a difference is
// reported (run mage promoteGo).
//
// With -check nothing is written, and a generated file that differs from
// src or from the amorce is an error.
package main

import (
	"bytes"
	"flag"
	"fmt"
	"io/fs"
	"os"
	"path/filepath"
	"sort"
	"strings"

	"tomtools/harness"
)

func main() {
	check := flag.Bool("check", false, "write nothing; fail when a generated file differs from src or from the amorce")
	verbose := flag.Bool("v", false, "print the output of the compiler")
	flag.Parse()
	if err := run(*check, *verbose); err != nil {
		fmt.Fprintln(os.Stderr, "regentomgo:", err)
		os.Exit(1)
	}
}

// sources returns the Tom+Go sources under dir (testdata excluded), sorted.
func sources(dir string) ([]string, error) {
	var out []string
	err := filepath.WalkDir(dir, func(p string, d fs.DirEntry, err error) error {
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

func run(check, verbose bool) error {
	root, err := harness.FindRoot()
	if err != nil {
		return err
	}
	home := filepath.Join(root, "src", "dist")
	if err := harness.CheckFreshCompiler(root, home); err != nil {
		return err
	}
	module := filepath.Join(root, "src", "go", "tom")
	adtDir := filepath.Join(module, "engine", "adt")
	if _, err := os.Stat(filepath.Join(adtDir, "adt.tom")); err != nil {
		return fmt.Errorf("no adt.tom in %s: run mage -d magefiles -w . bootstrap", adtDir)
	}
	srcs, err := sources(filepath.Join(module, "engine"))
	if err != nil {
		return err
	}
	tmp, err := os.MkdirTemp("", "regentomgo-")
	if err != nil {
		return err
	}
	defer os.RemoveAll(tmp)
	env := []string{"TOM_HOME=" + home, "GOMGO_BIN=" + filepath.Join(home, "bin", "gomgo")}
	var written, unchanged, stale, amorce []string
	for i, src := range srcs {
		rel, _ := filepath.Rel(module, src)
		gen := filepath.Join(tmp, fmt.Sprint(i), filepath.Base(src)+".go")
		if err := os.MkdirAll(filepath.Dir(gen), 0o755); err != nil {
			return err
		}
		out, err := harness.RunIn(filepath.Dir(src), env, filepath.Join(home, "bin", "tom"),
			"-go", "-np", "-I", adtDir, "-o", gen, src)
		if verbose || err != nil {
			fmt.Print(out)
		}
		if err != nil {
			return fmt.Errorf("tom -go %s: %v", rel, err)
		}
		b, err := os.ReadFile(gen)
		if err != nil {
			return fmt.Errorf("%s: no generated file: %v", rel, err)
		}
		if a, err := os.ReadFile(filepath.Join(root, "stable", "go", "tom", rel+".go")); err != nil || !bytes.Equal(a, b) {
			amorce = append(amorce, rel+".go")
		}
		dst := src + ".go"
		if old, err := os.ReadFile(dst); err == nil && bytes.Equal(old, b) {
			unchanged = append(unchanged, rel+".go")
			continue
		}
		if check {
			stale = append(stale, rel+".go")
			continue
		}
		if err := os.WriteFile(dst, b, 0o644); err != nil {
			return err
		}
		written = append(written, rel+".go")
	}
	fmt.Printf("regentomgo: %d source(s), %d written, %d unchanged\n", len(srcs), len(written), len(unchanged))
	for _, w := range written {
		fmt.Println("  written", w)
	}
	for _, s := range stale {
		fmt.Println("  differs from src (run mage regenTomGo):", s)
	}
	for _, a := range amorce {
		fmt.Println("  differs from the amorce (run mage promoteGo):", a)
	}
	if check && len(stale)+len(amorce) > 0 {
		return fmt.Errorf("%d generated file(s) differ from src, %d from the amorce", len(stale), len(amorce))
	}
	return nil
}
