// compilerEnv mirrors the inner class
// `tom.engine.compiler.Compiler.CompilerEnvironment` (lines 80-160
// of Compiler.t). It holds the per-input mutable state the
// CompileMatch / propagator / generator strategies share:
//
//   - symbolTable     — for codomain / slot / TLType lookups.
//   - rootpath        — TomNumberList prefix used to name fresh vars
//                       distinctly across Match instructions.
//   - matchNumber     — incremented per Match; rotates rootpath.
//   - freshSubject /
//     freshVar counter — sequence for `_freshSubject_<n>` and
//                       `_freshVar_<n>` identifiers.
//
// All side-effecting helpers (nextMatch, genFresh*, etc.) live here
// so the strategies stay free of long-lived state.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// compilerEnv is the Go counterpart of CompilerEnvironment.
type compilerEnv struct {
	symbols *tom.SymbolTable

	rootpath           tomast.TomNumberList
	matchNumber        int
	freshSubjectNumber int
	freshVarNumber     int

	// disjunctionVars is the set of variable names that have been
	// pre-declared by `buildExpressionDisjunction` for the current
	// disjunction it is generating. `buildDisjunctionIfElse` uses
	// this set to downgrade matching LetRefs to Assigns inside each
	// branch so the outer declaration captures the value.
	disjunctionVars map[string]bool
}

func newCompilerEnv(st *tom.SymbolTable) *compilerEnv {
	return &compilerEnv{symbols: st}
}

// nextMatch — Java Compiler.t line 100-105: increment matchNumber,
// reset per-match counters, recompute rootpath as
// `concTomNumber(MatchNumber(matchNumber))`.
func (e *compilerEnv) nextMatch() {
	e.matchNumber++
	e.rootpath = tomast.MakeConcTomNumber(tomast.MakeMatchNumber(int64(e.matchNumber)))
	e.freshSubjectNumber = 0
	e.freshVarNumber = 0
}

// genFreshSubjectCounter mirrors Java's
// CompilerEnvironment.genFreshSubjectCounter — returns the current
// counter then increments. Used by renameSubjects to name fresh
// hoist variables `_freshSubject_<n>`.
func (e *compilerEnv) genFreshSubjectCounter() int {
	n := e.freshSubjectNumber
	e.freshSubjectNumber++
	return n
}

// genFreshVarCounter mirrors Java's
// CompilerEnvironment.genFreshVarCounter — returns the current
// counter then increments. Used by getFreshVariable.
func (e *compilerEnv) genFreshVarCounter() int {
	n := e.freshVarNumber
	e.freshVarNumber++
	return n
}
