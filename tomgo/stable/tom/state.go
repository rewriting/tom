// Package tom carries the shared State that flows through every
// phase of the Tom compiler pipeline. Each phase lives in its own
// sub-package (starter, parser, transformer, syntaxchecker,
// desugarer, typer, typechecker, expander, compiler, optimizer,
// backend) and exposes a single `Run(State) (State, error)`
// function. The cmd/tom binary chains those calls in order.
//
// We deliberately keep this package tiny — it is the dependency
// every phase imports, so anything it depends on transitively
// becomes part of every phase's compile graph. Keep it to data
// types and a couple of constructors.
package tom

import "tom/tomgo/stable/library/tomast"

// SymbolTable holds the global signature data (sorts, operators)
// the engine accumulates as the AST flows down the pipeline.
//
// The Parser plugin populates it from `%typeterm` and `%op`
// declarations; downstream phases (Desugarer, Typer, Compiler) read
// it to resolve slot names, codomain types, and target-language
// type implementations.
type SymbolTable struct {
	// Sorts maps each declared sort name to the body of its
	// `implement { ... }` hook (the target-language type
	// representation). Sorts without an `implement` hook map to the
	// empty string.
	Sorts map[string]string

	// Symbols maps each declared operator name to the canonical
	// [tomast.TomSymbol] term encoding its codomain, domain, slot
	// names and option list. Built by buildTomSymbol() in the parser
	// from `%op codomain ctor(slots) { … }` declarations.
	Symbols map[string]tomast.TomSymbol
}

// NewSymbolTable returns an empty SymbolTable with both maps initialised.
func NewSymbolTable() *SymbolTable {
	return &SymbolTable{
		Sorts:   make(map[string]string),
		Symbols: make(map[string]tomast.TomSymbol),
	}
}

// State is the snapshot threaded through the phase chain. A phase is
// expected to copy the State, mutate the relevant field(s), and
// return the new value. AST sharing (hash-consing) makes those
// "copies" cheap.
type State struct {
	Filename string
	Source   []byte
	Code     tomast.Code

	// Symbols accumulates sort / operator declarations as the
	// pipeline progresses. Starter creates a fresh one; later phases
	// read and write to it. The embedded pointer makes the table
	// itself shared even though State is passed by value.
	Symbols *SymbolTable

	// HasInlineGom is set by the Parser when the source contained a
	// `%gom { ... }` block. Java's TomParserTool relies on `tom.home`
	// + Tom.xml to expand such a block into a TomInclude; without
	// that, the typer hits "Unknown symbol" / "Unknown type" errors
	// and aborts before running TransformBQAppl. Our typer mirrors
	// that behaviour by skipping BQAppl→FunctionCall rewriting when
	// this flag is set, so AST shapes stay parity-compatible.
	HasInlineGom bool
}
