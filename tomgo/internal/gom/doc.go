// Package gom holds the Go port of the Gom parser and AST.
//
// Scope of this iteration (Phase 2b): parse .gom files that do NOT contain
// hooks. Hooks (`Symbol:hookname(args) { ... }`) are reported as parse
// errors and deferred to a future sub-phase.
package gom
