// Command gentom scans a tree for *.gom files and writes the
// corresponding *.tom mappings under a shared directory. The output
// layout mirrors Java's gom build: each module's segments become the
// directory path (all lowercased), the last segment names the file
// (original case + ".tom"). Other .t fixtures can `%include
// {<modulepath>/<File>.tom}` and the parser's IncludeSearchPath will
// resolve via this shared cache.
//
// Usage: `go run ./cmd/gentom [-src DIR] [-dest DIR]`. Defaults to
// scanning `<repo>/test/` and writing to `<repo>/tomgo/tests/share/tom-mappings/`.
package main

import (
	"bytes"
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"strings"

	"tom/tomgo/stable/gom/backend"
	gomparse "tom/tomgo/stable/gom/parser"
)

func main() {
	srcRoot := flag.String("src", "/Users/pem/github/tom/test", "directory to scan for *.gom files")
	destRoot := flag.String("dest", "/Users/pem/github/tom/tomgo/tests/share/tom-mappings", "directory to write generated *.tom files")
	flag.Parse()

	if err := os.MkdirAll(*destRoot, 0o755); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}
	var gomFiles []string
	if err := filepath.Walk(*srcRoot, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".gom" {
			gomFiles = append(gomFiles, path)
		}
		return nil
	}); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}

	var written, failed int
	for _, gomFile := range gomFiles {
		src, err := os.ReadFile(gomFile)
		if err != nil {
			failed++
			fmt.Fprintf(os.Stderr, "  read %s: %v\n", gomFile, err)
			continue
		}
		mod, err := gomparse.Parse(bytes.NewReader(src))
		if err != nil {
			failed++
			fmt.Fprintf(os.Stderr, "  parse %s: %v\n", gomFile, err)
			continue
		}
		// moduleQualifiedName is unexported; re-derive by reading the
		// `Qualified` field of GomModule. The backend's
		// defaultPackageName also gives us the package name we'll
		// thread through to the .tom emitter.
		qualName := backend.QualifiedName(mod)
		segments := strings.Split(qualName, ".")
		dirParts := make([]string, 0, len(segments))
		for _, seg := range segments {
			dirParts = append(dirParts, strings.ToLower(seg))
		}
		lastSeg := segments[len(segments)-1]
		outDir := filepath.Join(append([]string{*destRoot}, dirParts...)...)
		if err := os.MkdirAll(outDir, 0o755); err != nil {
			failed++
			fmt.Fprintf(os.Stderr, "  mkdir %s: %v\n", outDir, err)
			continue
		}
		opts := backend.Options{PackageName: lastSeg}
		body := backend.EmitTomMapping(mod, opts)
		outFile := filepath.Join(outDir, lastSeg+".tom")
		if err := os.WriteFile(outFile, body, 0o644); err != nil {
			failed++
			fmt.Fprintf(os.Stderr, "  write %s: %v\n", outFile, err)
			continue
		}
		written++
	}
	fmt.Printf("gentom: %d written, %d failed\n", written, failed)
}
