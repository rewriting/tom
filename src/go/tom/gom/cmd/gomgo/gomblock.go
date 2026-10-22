package main

import (
	"bytes"
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"strings"

	gomast "tom/gom/adt"
	"tom/gom/backend"
	gomparser "tom/gom/parser"
)

// runGomBlock compiles the body of a Tom `%gom { … }` block for the Go
// target. It is called by the Tom Java compiler (TomParserTool) when
// -go is active; the last stdout line is the generated mapping's path.
func runGomBlock(args []string) error {
	fs := flag.NewFlagSet("gom-block", flag.ContinueOnError)
	var out, pkg string
	var opts backend.Options
	fs.StringVar(&out, "o", "", "output directory (required)")
	fs.StringVar(&pkg, "pkg", "", "Go package of the enclosing .t file (required)")
	// Gom options (the `%gom(--opt)` options, or those test/build.xml
	// gives a standalone .gom); the other Java Gom options are not
	// supported and rejected as undefined flags.
	fs.BoolVar(&opts.WithCongruenceStrategies, "withCongruenceStrategies", false, "add the congruence strategies (_op, When_op, Is_op) to the mapping")
	if err := fs.Parse(args); err != nil {
		return err
	}
	if fs.NArg() != 1 || out == "" || pkg == "" {
		return fmt.Errorf("usage: gomgo gom-block -o <dir> --pkg <name> [--withCongruenceStrategies] <file.gom>")
	}
	mod, err := gomparser.ParseFile(fs.Arg(0))
	if err != nil {
		return err
	}
	imported, err := importClosure(mod, filepath.Dir(fs.Arg(0)))
	if err != nil {
		return err
	}
	tomPath, err := writeGomBlock(mod, pkg, out, opts, imported...)
	if err != nil {
		return err
	}
	fmt.Println(tomPath)
	return nil
}

// importClosure loads the user modules mod imports, directly or not, as
// Java Gom does: an `imports M` that is not a builtin names the module
// of the file M.gom, looked up in dir (the directory of the importing
// file). The result is in dependency order (a module after the modules
// it imports), without mod itself; import cycles are allowed.
func importClosure(mod gomast.GomModule, dir string) ([]gomast.GomModule, error) {
	var out []gomast.GomModule
	seen := map[string]bool{lastName(mod): true}
	var visit func(m gomast.GomModule) error
	visit = func(m gomast.GomModule) error {
		for _, name := range backend.ImportedUserModules(m) {
			if seen[name] {
				continue
			}
			seen[name] = true
			dep, err := gomparser.ParseFile(filepath.Join(dir, name+".gom"))
			if err != nil {
				return fmt.Errorf("module %s imports %s: %w", backend.QualifiedName(m), name, err)
			}
			if err := visit(dep); err != nil {
				return err
			}
			out = append(out, dep)
		}
		return nil
	}
	if err := visit(mod); err != nil {
		return nil, err
	}
	return out, nil
}

// lastName is the last component of the module's qualified name.
func lastName(mod gomast.GomModule) string {
	name := gomparser.QualifiedName(mod)
	if i := strings.LastIndex(name, "."); i >= 0 {
		name = name[i+1:]
	}
	return name
}

// writeGomBlock writes <out>/<lower(Module)>_gen.go in package pkg and
// <out>/<Module>.tom (Go-host-code mapping), and returns the absolute
// path of the mapping. The modules mod imports (imported, see
// importClosure) are generated in the same package — Go packages cannot
// import each other cyclically, as Gom modules can — each into its own
// <lower(Module)>_gen.go, and the mapping declares their sorts and
// operators too, as Java Gom's mapping of an importing module does.
func writeGomBlock(mod gomast.GomModule, pkg, out string, opts backend.Options, imported ...gomast.GomModule) (string, error) {
	name := lastName(mod)
	if name == "" {
		name = "M"
	}
	if err := os.MkdirAll(out, 0o755); err != nil {
		return "", err
	}
	opts.PackageName = pkg
	all := append(append([]gomast.GomModule(nil), imported...), mod)
	var mapping []byte
	mapping = append(mapping, backend.BuiltinIncludesGo(mod, imported...)...)
	for i, m := range all {
		mopts := opts
		if len(imported) > 0 {
			mopts.ImportedSorts = map[string]bool{}
			for j, o := range all {
				if j != i {
					for _, s := range backend.SortNames(o) {
						mopts.ImportedSorts[s] = true
					}
				}
			}
		}
		var buf bytes.Buffer
		// the importing module's file declares the package's factory
		if err := backend.GenerateGenerated(m, mopts, m == mod, &buf); err != nil {
			return "", err
		}
		mname := lastName(m)
		if mname == "" {
			mname = "M"
		}
		if err := os.WriteFile(filepath.Join(out, strings.ToLower(mname)+"_gen.go"), buf.Bytes(), 0o644); err != nil {
			return "", err
		}
		mapping = append(mapping, backend.EmitTomMappingGo(m, mopts)...)
	}
	tomPath := filepath.Join(out, name+".tom")
	if err := os.WriteFile(tomPath, mapping, 0o644); err != nil {
		return "", err
	}
	return filepath.Abs(tomPath)
}
