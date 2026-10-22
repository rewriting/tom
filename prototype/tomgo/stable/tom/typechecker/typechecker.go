// Package typechecker is the Go port of Java's
// [tom.engine.checker.TypeCheckerPlugin]. The Java plugin validates
// the type-resolved AST: every subterm sits in its operator's
// declared domain, every constraint is well-typed. Errors are
// emitted to the platform's diagnostic stream; the AST is returned
// unchanged.
//
// We keep the stub semantics (AST in == AST out) until a
// diagnostics channel lands on [tom.State]; the plugin's
// presence pins the canonical pipeline order.
package typechecker

import "tom/tomgo/stable/tom"

// Run is currently identity; see package doc.
func Run(in tom.State) (tom.State, error) { return in, nil }
