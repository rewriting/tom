// Package compiler is the Go port of Java's
// [tom.engine.compiler.CompilerPlugin]. The Java plugin turns
// Expander-output constraints into runnable Tom instructions (the
// shape Backend then emits as host-language code).
//
// Identity stub for now.
package compiler

import "tom/tomgo/stable/platform"

// Plugin implements [platform.Plugin] for the Compiler phase.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Compiler" }

// Run is currently identity; see package doc.
func (Plugin) Run(in platform.State) (platform.State, error) { return in, nil }
