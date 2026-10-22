// Package compiler — public API helpers.
//
// `Run` drives the whole pipeline on a `tom.State`. Clients that
// need to compile a single `MatchInstruction` outside the pipeline
// (e.g. the Backend's `%strategy` emitter, which synthesises a
// match-per-visit on the fly) can use the helpers below.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// CompileMatch compiles a freshly-built `MatchInstruction` and
// returns the resulting Instruction tree (a `UnamedBlock` wrapping
// `CompiledMatch`). The caller can splice that wherever it needs —
// in particular the Backend uses it inside the body of each
// `visit_<Sort>` method when emitting a `%strategy` class.
//
// `symbols` is the SymbolTable for sort/op lookups (e.g. by
// renameSubjects, the propagators, the typer-symbolic checks).
func CompileMatch(m *tomast.MatchInstruction, symbols *tom.SymbolTable) tomast.Instruction {
	env := newCompilerEnv(symbols)
	return env.compileMatch(m)
}
