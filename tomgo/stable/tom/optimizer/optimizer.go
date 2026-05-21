// Package optimizer is the Go port of Java's
// [tom.engine.optimizer.OptimizerPlugin]. The Java plugin applies
// peephole optimisations (dead-code elimination, branch fusion, …)
// to the compiled code.
//
// Identity stub for now.
package optimizer

import "tom/tomgo/stable/platform"

// Plugin implements [platform.Plugin] for the Optimizer phase.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Optimizer" }

// Run is currently identity; see package doc.
func (Plugin) Run(in platform.State) (platform.State, error) { return in, nil }
