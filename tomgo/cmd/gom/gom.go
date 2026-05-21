// Command gom is the Gom compiler driver: it turns a `.gom`
// abstract-syntax description into a Go package that implements the
// AST, backed by the shared-objects runtime
// (stable/library/sharedobjects).
//
// Two modes:
//
//	gom -o <dir> <file.gom>                       single .gom
//	gom -o <dir> --pkg <name> <a.gom> <b.gom> …   batch (one Go pkg)
//
// Batch mode is required for `.gom` files that import each other —
// slot types resolve across the union of declarations rather than
// degrading to `any` at the package boundary.
package main

import (
	"flag"
	"fmt"
	"os"

	"tom/tomgo/stable/gom/backend"
	gomparser "tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

const usage = `gom — Gom compiler driver

Usage:
  gom -o <out-dir> [--pkg <name>] <file.gom>
        Compile one .gom file into a standalone Go module under <out-dir>.

  gom -o <out-dir> --pkg <name> <file1.gom> <file2.gom> …
        Compile several .gom files into ONE Go package. Slot types are
        resolved across the whole set, so cross-module imports work.

Flags (apply to both modes):
  -o <dir>      output directory (created if missing)
  --pkg <name>  Go package name (defaults to a name derived from the
                first .gom module name; required in batch mode)
`

func main() {
	out := flag.String("o", "", "output directory")
	pkg := flag.String("pkg", "", "Go package name")
	flag.Usage = func() { fmt.Fprint(os.Stderr, usage) }
	flag.Parse()

	args := flag.Args()
	if *out == "" || len(args) == 0 {
		flag.Usage()
		os.Exit(2)
	}

	if len(args) == 1 {
		runSingle(args[0], *out, *pkg)
		return
	}
	runBatch(args, *out, *pkg)
}

func runSingle(gomFile, outDir, pkgName string) {
	mod, err := gomparser.ParseFile(gomFile)
	if err != nil {
		die("parse %s: %v", gomFile, err)
	}
	if _, err := backend.GenerateToDir(mod, backend.Options{PackageName: pkgName}, outDir); err != nil {
		die("generate: %v", err)
	}
	fmt.Printf("OK: %s → %s\n", gomFile, outDir)
}

func runBatch(gomFiles []string, outDir, pkgName string) {
	if pkgName == "" {
		die("--pkg is required in batch mode")
	}
	modules := make([]gomast.GomModule, 0, len(gomFiles))
	for _, f := range gomFiles {
		mod, err := gomparser.ParseFile(f)
		if err != nil {
			die("parse %s: %v", f, err)
		}
		modules = append(modules, mod)
	}
	if _, err := backend.GenerateBatchToDir(modules, backend.Options{PackageName: pkgName}, outDir); err != nil {
		die("generate batch: %v", err)
	}
	fmt.Printf("OK: %d files → %s (package %s)\n", len(gomFiles), outDir, pkgName)
}

func die(format string, args ...any) {
	fmt.Fprintf(os.Stderr, "gom: "+format+"\n", args...)
	os.Exit(1)
}
