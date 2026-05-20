// Package platform provides the Tom compiler driver: a sequence of plugins
// each of which takes a [State] in and returns a [State] out.
//
// This is the Go analogue of Java's tom.platform.PluginPlatform. Each
// plugin in the official engine pipeline (Starter, Parser, Transformer,
// SyntaxChecker, Desugarer, Typer, TypeChecker, Expander, Compiler,
// Optimizer, Backend) becomes one [Plugin] implementation; the [Platform]
// runs them in order, threading [State] through.
//
// The Go interface is strongly typed (no Object[] generic), which trades
// the Java flexibility for compile-time guarantees. The shared state
// carries:
//
//   - Filename: the input source path (set by the Starter/Parser).
//   - Source:   the raw source bytes (set by the Parser if needed).
//   - Code:     the tomast.Code AST (produced by Parser, transformed by
//               every later plugin).
//
// Later phases may need additional fields (SymbolTable, options, …);
// add them here as the pipeline grows.
package platform

import (
	"fmt"

	"tom/tomgo/stable/library/tomast"
)

// SymbolTable holds the global signature data (sorts, operators, …)
// the engine accumulates as the AST flows down the pipeline. It is
// the Go analogue of Java's tom.engine.tools.SymbolTable (held by
// TomStreamManager).
//
// Phase 5 puts a placeholder in place; the typer / type-checker phases
// will populate Sorts and Symbols with the parsed `%typeterm` and `%op`
// declarations. Lookups happen by raw name string.
type SymbolTable struct {
	// Sorts maps sort name → its declaring TypeTermDecl term.
	Sorts map[string]tomast.Declaration

	// Symbols maps operator name → its SymbolDecl / ListSymbolDecl /
	// ArraySymbolDecl term.
	Symbols map[string]tomast.Declaration
}

// NewSymbolTable returns an empty SymbolTable with both maps initialised.
func NewSymbolTable() *SymbolTable {
	return &SymbolTable{
		Sorts:   make(map[string]tomast.Declaration),
		Symbols: make(map[string]tomast.Declaration),
	}
}

// State is the snapshot threaded through the plugin chain. A plugin is
// expected to copy the State, mutate the relevant field(s), and return
// the new value.
type State struct {
	Filename string
	Source   []byte
	Code     tomast.Code

	// Symbols accumulates sort / operator declarations as the pipeline
	// progresses. Starter creates a fresh one; later plugins read and
	// write to it. Plugins receive the State by value but the embedded
	// pointer makes the table itself shared.
	Symbols *SymbolTable
}

// Plugin is the contract every compilation stage implements.
type Plugin interface {
	// Name returns a short human-readable identifier (e.g. "Parser") used
	// in error messages and traces.
	Name() string

	// Run transforms the input state into the output state. It must NOT
	// mutate `in`; callers expect immutable AST sharing thanks to the
	// hash-consing runtime.
	Run(in State) (State, error)
}

// Platform chains plugins. It is conceptually a `func(State) (State, error)`
// composed with a stable ordering; the engine's pipeline is built by
// instantiating one Platform per compilation unit and walking through
// its plugin list.
type Platform struct {
	Plugins []Plugin
}

// New creates a Platform with the given ordered plugin list.
func New(plugins ...Plugin) *Platform {
	return &Platform{Plugins: plugins}
}

// Run executes the plugin chain starting from `initial`. The first plugin
// typically expects only Filename to be set (the parser fills in Source
// and Code). Subsequent plugins see the AST progressively transformed.
//
// On the first error, Run returns the most-recent State and the wrapped
// error tagged with the failing plugin's Name.
func (p *Platform) Run(initial State) (State, error) {
	state := initial
	for _, plugin := range p.Plugins {
		next, err := plugin.Run(state)
		if err != nil {
			return state, fmt.Errorf("plugin %s: %w", plugin.Name(), err)
		}
		state = next
	}
	return state, nil
}
