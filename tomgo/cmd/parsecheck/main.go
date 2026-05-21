// Command parsecheck reports how many .t files under /Users/pem/github/tom/test
// the hand-rolled Go parser can consume. It mirrors what mage equivJava
// does but at the parser layer only — no Java toolchain needed.
//
// Flags:
//
//	-v   print one line per failing fixture with the parser's error
//
// Example: `go run ./cmd/parsecheck -v | head -20`.
package main

import (
	"flag"
	"fmt"
	"os"
	"path/filepath"
	"sort"

	tomparser "tom/tomgo/stable/tom/parser/parser"
)

func main() {
	verbose := flag.Bool("v", false, "print each failure with its error")
	flag.Parse()

	// Wire the standard Tom mapping directory so test/* fixtures that
	// `%include { sl.tom }` etc. resolve.
	tomparser.IncludeSearchPath = []string{
		"/Users/pem/github/tom/utils/eclipse-plugin/plugin/include/java",
		"/Users/pem/github/tom/utils/eclipse-plugin/plugin/include",
		// Generated *.tom mappings emitted by `go run ./cmd/gentom`.
		// They mirror Java's gom build output so .t fixtures that
		// `%include {<modpath>/<File>.tom}` resolve without a real
		// gom build.
		"/Users/pem/github/tom/tomgo/tests/share/tom-mappings",
		// The `gom` subtree in test/ uses module names prefixed
		// with `gom.` but the .t fixtures include with the segment
		// stripped — point at the `gom` subdir explicitly so
		// `bool/Bool.tom` resolves to `…/gom/bool/Bool.tom`.
		"/Users/pem/github/tom/tomgo/tests/share/tom-mappings/gom",
	}

	root := "/Users/pem/github/tom/test"
	var paths []string
	if err := filepath.Walk(root, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".t" {
			paths = append(paths, path)
		}
		return nil
	}); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}

	var pass int
	var fails []string
	for _, path := range paths {
		src, err := os.ReadFile(path)
		if err != nil {
			continue
		}
		rel, _ := filepath.Rel(root, path)
		// Report which file is being parsed before attempting, so a
		// crash points at the culprit.
		fmt.Fprintln(os.Stderr, ">>> ", rel)
		if _, err := tomparser.Parse(string(src), path); err != nil {
			fails = append(fails, fmt.Sprintf("%s: %v", rel, err))
			continue
		}
		pass++
	}
	sort.Strings(fails)
	fmt.Printf("pass=%d fail=%d\n", pass, len(fails))
	if *verbose {
		for _, f := range fails {
			fmt.Println("  FAIL:", f)
		}
	}
}
