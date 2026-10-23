// Command regenadt regenerates the compiler's ADTs (package adtgen) in
// place, from the directory of module tom (src/go/tom):
//
//	go run ./internal/adtgen/regenadt [-tomhome DIR]
//
// The hooks are compiled by the Tom distribution at -tomhome (default:
// $TOM_HOME, else src/dist of the repository).
package main

import (
	"flag"
	"fmt"
	"os"
	"path/filepath"

	"tom/internal/adtgen"
)

func main() {
	def := os.Getenv("TOM_HOME")
	if def == "" {
		def = filepath.Join("..", "..", "dist")
	}
	tomHome := flag.String("tomhome", def, "TOM_HOME of the Tom distribution that compiles the hooks")
	flag.Parse()
	abs, err := filepath.Abs(*tomHome)
	if err != nil {
		fail(err)
	}
	root, err := os.Getwd()
	if err != nil {
		fail(err)
	}
	tmp, err := os.MkdirTemp("", "regenadt-")
	if err != nil {
		fail(err)
	}
	defer os.RemoveAll(tmp)
	for _, a := range adtgen.ADTs {
		files, err := adtgen.Generate(root, a, adtgen.Tools{TomHome: abs}, tmp)
		if err != nil {
			fail(fmt.Errorf("%s: %w", a.OutDir(), err))
		}
		if err := adtgen.Write(filepath.Join(root, a.OutDir()), files); err != nil {
			fail(err)
		}
		fmt.Printf("regenerated %s (%d files)\n", a.OutDir(), len(files))
	}
}

func fail(err error) {
	fmt.Fprintln(os.Stderr, "regenadt:", err)
	os.Exit(1)
}
