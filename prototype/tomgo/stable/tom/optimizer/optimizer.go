// Package optimizer is the Go port of Java's
// [tom.engine.optimizer.OptimizerPlugin]. The Java plugin applies
// peephole optimisations (dead-code elimination, branch fusion, …)
// to the compiled code.
//
// Identity stub for now.
package optimizer

import "tom/tomgo/stable/tom"

// Run is currently identity; see package doc.
func Run(in tom.State) (tom.State, error) { return in, nil }
