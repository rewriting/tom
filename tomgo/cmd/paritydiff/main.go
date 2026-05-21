// Command paritydiff measures, per fixture, the structural delta
// between our Go pipeline and the Java reference. Two AST dumps are
// parsed via astcmp, simplified, then byte-compared. The output ranks
// fixtures by their first-divergence byte position (small = close).
//
// Usage: `go run ./cmd/paritydiff`
package main

import (
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"

	"tom/tomgo/stable/tom"
	tomparseq "tom/tomgo/tests/tom/parser/equiv"
	"tom/tomgo/tests/tom/parser/equiv/astcmp"
	tomparser "tom/tomgo/stable/tom/parser/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/typer"
)

type result struct {
	name string
	diff int // byte position of first divergence; -1 if identical
}

func main() {
	repoRoot := "/Users/pem/github/tom"
	tomparser.IncludeSearchPath = []string{
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include", "java"),
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include"),
		filepath.Join(repoRoot, "tomgo", "tests", "share", "tom-mappings"),
		filepath.Join(repoRoot, "tomgo", "tests", "share", "tom-mappings", "gom"),
	}
	tc, err := tomparseq.Resolve(repoRoot)
	if err != nil {
		fmt.Fprintln(os.Stderr, "no JDK:", err)
		os.Exit(2)
	}
	corpus := filepath.Join(repoRoot, "test")
	var entries []string
	if err := filepath.Walk(corpus, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".t" {
			rel, _ := filepath.Rel(corpus, path)
			if strings.HasPrefix(rel, "error/") {
				return nil
			}
			entries = append(entries, rel)
		}
		return nil
	}); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}

	var results []result
	for _, rel := range entries {
		input := filepath.Join(corpus, rel)
		src, err := os.ReadFile(input)
		if err != nil {
			continue
		}
		if _, err := tomparser.Parse(string(src), input); err != nil {
			continue
		}
		javaOut, err := tc.DumpJavaPhase(input, tomparseq.PhaseTyped)
		if err != nil {
			continue
		}
		state, err := tom.Run(tom.State{Filename: input}, starter.Run, parser.Run, transformer.Run, syntaxchecker.Run, desugarer.Run, typer.Run)
		if err != nil {
			results = append(results, result{rel, -2})
			continue
		}
		goOut := fmt.Sprintf("%v", state.Code)
		goOut = strings.ReplaceAll(goOut, input, "__INPUT__")
		goOut = strings.ReplaceAll(goOut, filepath.Dir(input), "__DIR__")
		jn, _ := astcmp.Parse(javaOut)
		gn, _ := astcmp.Parse(goOut)
		ja := astcmp.Simplify(jn).String()
		ga := astcmp.Simplify(gn).String()
		if ja == ga {
			results = append(results, result{rel, -1})
			continue
		}
		// First divergence byte position.
		minLen := len(ja)
		if len(ga) < minLen {
			minLen = len(ga)
		}
		div := minLen
		for i := 0; i < minLen; i++ {
			if ja[i] != ga[i] {
				div = i
				break
			}
		}
		results = append(results, result{rel, div})
	}

	sort.Slice(results, func(i, j int) bool {
		if results[i].diff == -1 && results[j].diff != -1 {
			return true
		}
		if results[j].diff == -1 && results[i].diff != -1 {
			return false
		}
		return results[i].diff > results[j].diff
	})

	for _, r := range results {
		status := fmt.Sprintf("%d", r.diff)
		if r.diff == -1 {
			status = "OK"
		}
		if r.diff == -2 {
			status = "PIPELINE-ERR"
		}
		fmt.Printf("%-50s %s\n", r.name, status)
	}
}
