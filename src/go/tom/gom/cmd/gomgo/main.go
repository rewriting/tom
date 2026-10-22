// Command gomgo is Gom written in Go (used by `tom -go`): it compiles
// Gom signatures (.gom files or Tom %gom blocks) to Go packages backed
// by the shared-objects runtime (tom/library/sharedobjects).
//
// Commands:
//   - gom        Compile one .gom file to a standalone Go module.
//   - gom-batch  Compile several cross-importing .gom files to one package.
//   - gom-block  Compile a Tom %gom block for the Go target (tom -go).
package main

import (
	"flag"
	"fmt"
	"os"

	gomast "tom/gom/adt"
	"tom/gom/backend"
	"tom/gom/parser"
)

const usage = `gomgo — Gom written in Go (used by tom -go)

Usage:
  gomgo <command> [arguments]

Commands:
  gom -o <out-dir> [--pkg <name>] <file.gom>
                                   Generate a Go package from one .gom file.
                                   The output is a standalone Go module.
  gom-batch -o <out-dir> --pkg <name> <file1.gom> <file2.gom> …
                                   Generate ONE Go package combining several
                                   .gom files. Slot types are resolved across
                                   modules. Required for sources that import
                                   each other (e.g. src/tom/gom/adt/*.gom).
  gom-block -o <dir> --pkg <name> <file.gom>
                                   Compile a Tom gom block for the Go target
                                   (used by tom -go): writes <module>_gen.go
                                   and <Module>.tom, prints the .tom path.
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
		fmt.Println("gomgo 0.0.0")
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
	case "gom-block":
		if err := runGomBlock(os.Args[2:]); err != nil {
			fmt.Fprintln(os.Stderr, "error:", err)
			os.Exit(1)
		}
	default:
		fmt.Fprintf(os.Stderr, "unknown command %q\n\n%s", os.Args[1], usage)
		os.Exit(2)
	}
}

func runGom(args []string) error {
	fs := flag.NewFlagSet("gom", flag.ContinueOnError)
	var out, pkg string
	fs.StringVar(&out, "o", "", "output directory for the generated package (required)")
	fs.StringVar(&pkg, "pkg", "", "override the generated package name (default: derived from the module name)")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 {
		return fmt.Errorf("usage: gomgo gom -o <out-dir> [--pkg <name>] <file.gom>")
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
	fmt.Printf("wrote %s/ (%d sort(s) from %s) [target=go]\n", dir, gom.CountSorts(mod), gom.QualifiedName(mod))
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
		return fmt.Errorf("usage: gomgo gom-batch -o <out-dir> --pkg <name> <file1.gom> <file2.gom> ...")
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
