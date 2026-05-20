// Command tomgo is the entry point of the Go port of the TOM compiler.
//
// Current scope (iteration 0..2):
//   - scan-hooks  Inventory .gom files and detect hooks (Phase 1).
//   - gom         Compile a .gom file (without hooks) to a Go package
//                 backed by the shared-objects runtime (Phase 2).
package main

import (
	"flag"
	"fmt"
	"os"

	"tom/tomgo/stable/gom/backend"
	"tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

const usage = `tomgo — Go port of the TOM compiler (work in progress)

Usage:
  tomgo <command> [arguments]

Commands:
  scan-hooks <dir>                 Walk <dir> and report .gom files with/without hooks.
  gom -o <out-dir> [--pkg <name>] <file.gom>
                                   Generate a Go package from one .gom file.
                                   The output is a standalone Go module.
  gom-batch -o <out-dir> --pkg <name> <file1.gom> <file2.gom> …
                                   Generate ONE Go package combining several
                                   .gom files. Slot types are resolved across
                                   modules. Required for sources that import
                                   each other (e.g. src/tom/gom/adt/*.gom).
  version                          Print version info.
  help                             Show this help.
`

func main() {
	if len(os.Args) < 2 {
		fmt.Fprint(os.Stderr, usage)
		os.Exit(2)
	}
	switch os.Args[1] {
	case "help", "--help", "-h":
		fmt.Print(usage)
	case "version", "--version":
		fmt.Println("tomgo 0.0.0 (phases 0–2 in progress)")
	case "scan-hooks":
		if err := runScanHooks(os.Args[2:]); err != nil {
			fmt.Fprintln(os.Stderr, "error:", err)
			os.Exit(1)
		}
	case "gom":
		if err := runGom(os.Args[2:]); err != nil {
			fmt.Fprintln(os.Stderr, "error:", err)
			os.Exit(1)
		}
	case "gom-batch":
		if err := runGomBatch(os.Args[2:]); err != nil {
			fmt.Fprintln(os.Stderr, "error:", err)
			os.Exit(1)
		}
	default:
		fmt.Fprintf(os.Stderr, "unknown command %q\n\n%s", os.Args[1], usage)
		os.Exit(2)
	}
}

func runScanHooks(args []string) error {
	fs := flag.NewFlagSet("scan-hooks", flag.ContinueOnError)
	var listNoHooks, listWithHooks bool
	fs.BoolVar(&listNoHooks, "list-no-hooks", false, "print one path per line for every .gom WITHOUT hooks")
	fs.BoolVar(&listWithHooks, "list-with-hooks", false, "print one path per line for every .gom WITH hooks")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 {
		return fmt.Errorf("usage: tomgo scan-hooks [--list-no-hooks|--list-with-hooks] <dir>")
	}
	rep, err := gom.ScanDir(fs.Arg(0))
	if err != nil {
		return err
	}
	switch {
	case listNoHooks:
		for _, p := range rep.NoHooks {
			fmt.Println(p)
		}
	case listWithHooks:
		for _, p := range rep.WithHooks {
			fmt.Println(p)
		}
	default:
		rep.PrintSummary(os.Stdout)
	}
	return nil
}

func runGom(args []string) error {
	fs := flag.NewFlagSet("gom", flag.ContinueOnError)
	var out, pkg string
	fs.StringVar(&out, "o", "", "output directory for the generated Go package (required)")
	fs.StringVar(&pkg, "pkg", "", "override the generated Go package name (default: derived from the module name)")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 {
		return fmt.Errorf("usage: tomgo gom -o <out-dir> [--pkg <name>] <file.gom>")
	}
	if out == "" {
		return fmt.Errorf("missing -o <out-dir>")
	}
	mod, err := gom.ParseFile(fs.Arg(0))
	if err != nil {
		return err
	}
	dir, err := backend.GenerateToDir(mod, backend.Options{PackageName: pkg}, out)
	if err != nil {
		return err
	}
	fmt.Printf("wrote %s/ (%d sort(s) from %s)\n", dir, gom.CountSorts(mod), gom.QualifiedName(mod))
	return nil
}

func runGomBatch(args []string) error {
	fs := flag.NewFlagSet("gom-batch", flag.ContinueOnError)
	var out, pkg string
	fs.StringVar(&out, "o", "", "output directory for the combined Go package (required)")
	fs.StringVar(&pkg, "pkg", "", "Go package name for the combined module (required)")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if out == "" || pkg == "" || fs.NArg() == 0 {
		return fmt.Errorf("usage: tomgo gom-batch -o <out-dir> --pkg <name> <file1.gom> <file2.gom> ...")
	}
	var modules []gomast.GomModule
	for _, f := range fs.Args() {
		m, err := gom.ParseFile(f)
		if err != nil {
			return err
		}
		modules = append(modules, m)
	}
	dir, err := backend.GenerateBatchToDir(modules, backend.Options{PackageName: pkg}, out)
	if err != nil {
		return err
	}
	totalSorts := 0
	totalHooks := 0
	for _, m := range modules {
		totalSorts += gom.CountSorts(m)
		totalHooks += gom.CountHooks(m)
	}
	fmt.Printf("wrote %s/ (%d module(s), %d sort(s), %d hook(s))\n",
		dir, len(modules), totalSorts, totalHooks)
	return nil
}
