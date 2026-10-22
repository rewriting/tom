package main

import (
	"bytes"
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/gom/backend"
	gomparser "tom/tomgo/stable/gom/parser"
	"tom/tomgo/stable/library/gomast"
)

// runGomBlock compiles the body of a Tom `%gom { … }` block for the Go
// target. It is called by the Tom Java compiler (TomParserTool) when
// -go is active; the last stdout line is the generated mapping's path.
func runGomBlock(args []string) error {
	fs := flag.NewFlagSet("gom-block", flag.ContinueOnError)
	var out, pkg string
	fs.StringVar(&out, "o", "", "output directory (required)")
	fs.StringVar(&pkg, "pkg", "", "Go package of the enclosing .t file (required)")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 || out == "" || pkg == "" {
		return fmt.Errorf("usage: tomgo gom-block -o <dir> --pkg <name> <file.gom>")
	}
	mod, err := gomparser.ParseFile(fs.Arg(0))
	if err != nil {
		return err
	}
	tomPath, err := writeGomBlock(mod, pkg, out)
	if err != nil {
		return err
	}
	fmt.Println(tomPath)
	return nil
}

// writeGomBlock writes <out>/<lower(Module)>_gen.go in package pkg and
// <out>/<Module>.tom (Go-host-code mapping), and returns the absolute
// path of the mapping.
func writeGomBlock(mod gomast.GomModule, pkg, out string) (string, error) {
	name := gomparser.QualifiedName(mod)
	if i := strings.LastIndex(name, "."); i >= 0 {
		name = name[i+1:]
	}
	if name == "" {
		name = "M"
	}
	if err := os.MkdirAll(out, 0o755); err != nil {
		return "", err
	}
	opts := backend.Options{PackageName: pkg}
	var buf bytes.Buffer
	if err := backend.Generate(mod, opts, &buf); err != nil {
		return "", err
	}
	if err := os.WriteFile(filepath.Join(out, strings.ToLower(name)+"_gen.go"), buf.Bytes(), 0o644); err != nil {
		return "", err
	}
	tomPath := filepath.Join(out, name+".tom")
	mapping := append(backend.BuiltinIncludesGo(mod), backend.EmitTomMappingGo(mod, opts)...)
	if err := os.WriteFile(tomPath, mapping, 0o644); err != nil {
		return "", err
	}
	return filepath.Abs(tomPath)
}
