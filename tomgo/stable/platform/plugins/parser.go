// Package plugins holds the concrete [platform.Plugin] implementations
// for each phase of the Tom compiler pipeline. Their order in the
// engine matches the Java reference:
//
//	Starter → Parser → Transformer → SyntaxChecker → Desugarer
//	      → Typer → TypeChecker → Expander → Compiler → Optimizer → Backend
//
// Each plugin lives in its own file: parser.go (this file) wraps the
// hand-rolled Go parser from stable/tom/parser/parser. Subsequent
// phases will be ported one by one with their own AST↔Java equivalence
// suite.
package plugins

import (
	"fmt"
	"os"

	"tom/tomgo/stable/platform"
	tomparser "tom/tomgo/stable/tom/parser/parser"
)

// Parser is the [platform.Plugin] that turns a source filename into the
// initial [tomast.Code] AST. It corresponds to Java's
// tom.engine.parser.TomParserPlugin.
//
// Behaviour:
//   - If state.Source is empty, read it from state.Filename.
//   - Invoke the hand-rolled parser (stable/tom/parser/parser.Parse).
//   - Return a new State whose Code field carries the resulting AST.
//     Filename and Source are preserved for downstream plugins that
//     may want to look back at the input (e.g. for error messages
//     locating into the raw bytes).
type Parser struct{}

// Name implements [platform.Plugin].
func (Parser) Name() string { return "Parser" }

// Run implements [platform.Plugin].
func (Parser) Run(in platform.State) (platform.State, error) {
	if in.Filename == "" {
		return in, fmt.Errorf("Parser: empty Filename")
	}
	src := in.Source
	if len(src) == 0 {
		bytes, err := os.ReadFile(in.Filename)
		if err != nil {
			return in, fmt.Errorf("Parser: read %s: %w", in.Filename, err)
		}
		src = bytes
	}
	code, err := tomparser.Parse(string(src), in.Filename)
	if err != nil {
		return in, fmt.Errorf("Parser: parse %s: %w", in.Filename, err)
	}
	out := in
	out.Source = src
	out.Code = code
	return out, nil
}
