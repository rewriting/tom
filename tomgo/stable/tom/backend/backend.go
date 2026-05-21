// Package backend is the Go port of Java's
// [tom.engine.backend.BackendPlugin]. The Java plugin emits final
// host-language code (Java today, Go tomorrow) from the optimized
// instruction stream.
//
// Identity stub for now; downstream drivers read State.Code
// directly until the real backend lands.
package backend

import "tom/tomgo/stable/tom"

// Run is currently identity; see package doc.
func Run(in tom.State) (tom.State, error) { return in, nil }
