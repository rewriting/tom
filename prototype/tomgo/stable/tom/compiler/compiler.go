// Package compiler ports tom.engine.compiler.Compiler.t to Go.
//
// The Java reference (stable/tom/engine/compiler/) drives the
// pipeline below for every `MatchInstruction` it finds inside the
// typed AST:
//
//  1. renameSubjects  — hoist each subject into a fresh BQVariable
//     so it's evaluated exactly once.
//  2. ConstraintPropagator
//     — normalise the constraint conjunction: split
//     `Match(Foo(p1,p2), s)` into per-slot
//     sub-constraints, etc. Dispatches to a
//     per-shape sub-propagator (Syntactic, Array,
//     Variadic, AC, GeneralPurpose).
//  3. PreGenerator    — fold the propagated constraint tree into a
//     single `Expression`.
//  4. ConstraintGenerator
//     — turn the Expression + action body into a
//     matching automaton `Instruction` tree (Let,
//     If, Assign, Cast, IsFsym, IsSort, …).
//  5. PostGenerator   — peephole cleanup on the automaton.
//
// Output is the typed AST with each `Match` replaced by
// `UnamedBlock(CompiledMatch(AbstractBlock(automataList), opts))`.
//
// Two implementations live in this package while the port stabilises:
//   - runLegacy (legacy.go) — ad-hoc match → If-chain compilation
//     that keeps Peano.t / TestPeano.t green.
//   - runNew (compilematch.go) — incremental port of the Java pipeline.
//
// Selection: the env var `TOMGO_NEW_COMPILER=1` switches the entry
// point over to the new implementation. Default is `runLegacy` so
// regressions are explicit.
package compiler

import (
	"os"

	"tom/tomgo/stable/tom"
)

// Run dispatches to either the legacy or the new compiler. Default
// is the new (IL-faithful) pipeline; setting `TOMGO_LEGACY_COMPILER`
// switches back to the ad-hoc legacy fallback (kept until the new
// pipeline reaches parity across the full corpus).
func Run(in tom.State) (tom.State, error) {
	if os.Getenv("TOMGO_LEGACY_COMPILER") != "" {
		return runLegacy(in)
	}
	return runNew(in)
}
