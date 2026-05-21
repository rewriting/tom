// Package expander is the Go port of Java's
// [tom.engine.expander.ExpanderPlugin]. The Java plugin expands
// %match / %strategy declarations into low-level if/switch/goto
// sequences — the core of Tom's pattern-matching compilation.
//
// Identity stub for now; lands as a real implementation once the
// Typer's output is rich enough to drive it.
package expander

import "tom/tomgo/stable/tom"

// Run is currently identity; see package doc.
func Run(in tom.State) (tom.State, error) { return in, nil }
