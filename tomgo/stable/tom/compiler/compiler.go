// Package compiler is the Go port of Java's
// [tom.engine.compiler.CompilerPlugin]. The Java plugin turns
// Expander-output constraints into runnable Tom instructions (the
// shape Backend then emits as host-language code).
//
// Identity stub for now.
package compiler

import "tom/tomgo/stable/tom"

// Run is currently identity; see package doc.
func Run(in tom.State) (tom.State, error) { return in, nil }
