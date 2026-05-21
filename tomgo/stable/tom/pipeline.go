// Package tom assembles the canonical Tom compiler pipeline by
// wiring together the per-phase [platform.Plugin] implementations
// that live in the sub-packages.
//
// The order matches Java's BootstrapPluginsList:
//
//	Starter → Parser → Transformer → SyntaxChecker → Desugarer
//	      → Typer → TypeChecker → Expander → Compiler → Optimizer → Backend
//
// Phase 5 status: Starter, Parser, Transformer, SyntaxChecker,
// Desugarer, and Typer are real plugins. TypeChecker, Expander,
// Compiler, Optimizer, and Backend are identity stubs that preserve
// State unchanged — they pin the canonical phase order so any
// downstream code that picks a specific phase by name finds it.
//
// To run only a subset, build the chain directly with
// [platform.New]; nothing in this package is required.
package tom

import (
	"tom/tomgo/stable/platform"
	"tom/tomgo/stable/tom/backend"
	"tom/tomgo/stable/tom/compiler"
	"tom/tomgo/stable/tom/desugarer"
	"tom/tomgo/stable/tom/expander"
	"tom/tomgo/stable/tom/optimizer"
	"tom/tomgo/stable/tom/parser"
	"tom/tomgo/stable/tom/starter"
	"tom/tomgo/stable/tom/syntaxchecker"
	"tom/tomgo/stable/tom/transformer"
	"tom/tomgo/stable/tom/typechecker"
	"tom/tomgo/stable/tom/typer"
)

// Default returns a Platform pre-configured with the canonical
// Tom engine pipeline.
func Default() *platform.Platform {
	return platform.New(
		starter.Plugin{},
		parser.Plugin{},
		transformer.Plugin{},
		syntaxchecker.Plugin{},
		desugarer.Plugin{},
		typer.Plugin{},
		typechecker.Plugin{},
		expander.Plugin{},
		compiler.Plugin{},
		optimizer.Plugin{},
		backend.Plugin{},
	)
}
