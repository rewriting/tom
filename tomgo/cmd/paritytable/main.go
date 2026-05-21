// Command paritytable runs each `test/*.t` fixture through every
// pipeline cut-point (Parser, Desugarer, Typer) and compares the
// Go-side AST against Java's `TomPipelineDump` at the matching
// phase. The output is a markdown table:
//
//	| fixture | parser | transformer | syntaxchecker | desugarer | typer |
//	|---------|--------|-------------|---------------|-----------|-------|
//
// We have no Go ports of Transformer or SyntaxChecker yet — Java
// keeps them identity-ish on this corpus, so we reuse the parser
// output for those columns.
//
// A `OK` cell means `astcmp.Equal` returned true at that phase. A
// blank cell means the previous phase already diverged (skipped),
// `parse-fail` means our parser didn't accept the input.
package main

import (
	"fmt"
	"os"
	"path/filepath"
	"sort"
	"strings"

	"tom/tomgo/stable/platform"
	"tom/tomgo/stable/platform/plugins"
	tomparseq "tom/tomgo/stable/tom/parser/equiv"
	"tom/tomgo/stable/tom/parser/equiv/astcmp"
	tomparser "tom/tomgo/stable/tom/parser/parser"
)

type row struct {
	name string
	// Per-phase status. "OK" / "DIFF" for real plugins; "OK-stub" /
	// "DIFF-stub" when the Go side has no plugin and we're reusing the
	// parser output (Transformer, SyntaxChecker — Java treats them as
	// near-identity on this corpus). "" = skipped because the previous
	// phase didn't run.
	parser, transformer, synchecked, desugarer, typer string
}

func main() {
	repoRoot := "/Users/pem/github/tom"
	tomparser.IncludeSearchPath = []string{
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include", "java"),
		filepath.Join(repoRoot, "utils", "eclipse-plugin", "plugin", "include"),
		filepath.Join(repoRoot, "tomgo", "share", "tom-mappings"),
		filepath.Join(repoRoot, "tomgo", "share", "tom-mappings", "gom"),
	}

	tc, err := tomparseq.Resolve(repoRoot)
	if err != nil {
		fmt.Fprintln(os.Stderr, "no JDK:", err)
		os.Exit(2)
	}

	corpus := filepath.Join(repoRoot, "test")
	var fixtures []string
	if err := filepath.Walk(corpus, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if !info.IsDir() && filepath.Ext(path) == ".t" {
			rel, _ := filepath.Rel(corpus, path)
			if strings.HasPrefix(rel, "error/") {
				return nil
			}
			fixtures = append(fixtures, rel)
		}
		return nil
	}); err != nil {
		fmt.Fprintln(os.Stderr, err)
		os.Exit(2)
	}
	sort.Strings(fixtures)

	rows := make([]row, 0, len(fixtures))

	check := func(input string, goCode string, phase tomparseq.Phase) string {
		jaOut, err := tc.DumpJavaPhase(input, phase)
		if err != nil {
			return "java-fail"
		}
		goOut := strings.ReplaceAll(goCode, input, "__INPUT__")
		goOut = strings.ReplaceAll(goOut, filepath.Dir(input), "__DIR__")
		eq, err := astcmp.Equal(jaOut, goOut)
		if err != nil {
			return "cmp-err"
		}
		if eq {
			return "OK"
		}
		return "DIFF"
	}

	for _, rel := range fixtures {
		input := filepath.Join(corpus, rel)
		r := row{name: rel}

		src, err := os.ReadFile(input)
		if err != nil {
			r.parser = "read-fail"
			rows = append(rows, r)
			continue
		}
		// Parser.
		parsed, err := tomparser.Parse(string(src), input)
		if err != nil {
			r.parser = "parse-fail"
			rows = append(rows, r)
			continue
		}
		goParsed := fmt.Sprintf("%v", parsed)
		r.parser = check(input, goParsed, tomparseq.PhaseParsed)
		// We have no Go Transformer / SyntaxChecker plugin — Java's
		// stays near-identity on this corpus, so we reuse the parser
		// output for those columns and tag the cells as `-stub` so
		// the table renders a distinct glyph.
		stubize := func(s string) string {
			if s == "OK" {
				return "OK-stub"
			}
			if s == "DIFF" {
				return "DIFF-stub"
			}
			return s
		}
		r.transformer = stubize(check(input, goParsed, tomparseq.PhaseTransformed))
		r.synchecked = stubize(check(input, goParsed, tomparseq.PhaseSynchecked))

		// Desugarer.
		desState, err := platform.New(
			plugins.Starter{},
			plugins.Parser{},
			plugins.Transformer{},
			plugins.SyntaxChecker{},
			plugins.Desugarer{},
		).Run(platform.State{Filename: input})
		if err == nil {
			goDesugared := fmt.Sprintf("%v", desState.Code)
			r.desugarer = check(input, goDesugared, tomparseq.PhaseDesugared)
		} else {
			r.desugarer = "pipeline-err"
		}

		// Typer.
		tState, err := platform.New(
			plugins.Starter{},
			plugins.Parser{},
			plugins.Transformer{},
			plugins.SyntaxChecker{},
			plugins.Desugarer{},
			plugins.Typer{},
		).Run(platform.State{Filename: input})
		if err == nil {
			goTyped := fmt.Sprintf("%v", tState.Code)
			r.typer = check(input, goTyped, tomparseq.PhaseTyped)
		} else {
			r.typer = "pipeline-err"
		}

		rows = append(rows, r)
	}

	// Markdown table.
	fmt.Println("| Fixture | Parser | Transformer (stub) | SyntaxChecker (stub) | Desugarer | Typer |")
	fmt.Println("|---|---|---|---|---|---|")
	cell := func(s string) string {
		switch s {
		case "OK":
			return "✅"
		case "DIFF":
			return "❌"
		case "OK-stub":
			return "🟢 stub"
		case "DIFF-stub":
			return "🟠 stub"
		case "":
			return "—"
		case "parse-fail":
			return "🔴 parse"
		case "java-fail":
			return "⚠️ java"
		default:
			return s
		}
	}
	for _, r := range rows {
		fmt.Printf("| `%s` | %s | %s | %s | %s | %s |\n",
			r.name,
			cell(r.parser),
			cell(r.transformer),
			cell(r.synchecked),
			cell(r.desugarer),
			cell(r.typer),
		)
	}

	// Totals.
	var totals [5]int
	var oks [5]int
	for _, r := range rows {
		cells := []string{r.parser, r.transformer, r.synchecked, r.desugarer, r.typer}
		for i, c := range cells {
			if c == "" {
				continue
			}
			totals[i]++
			if c == "OK" || c == "OK-stub" {
				oks[i]++
			}
		}
	}
	names := []string{"Parser", "Transformer", "SyntaxChecker", "Desugarer", "Typer"}
	fmt.Println()
	fmt.Println("**Totals (OK / total):**")
	for i, n := range names {
		fmt.Printf("- %s: %d / %d\n", n, oks[i], totals[i])
	}
}
