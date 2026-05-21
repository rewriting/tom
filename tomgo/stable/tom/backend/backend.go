// Package backend is the Go port of Java's
// [tom.engine.backend.BackendPlugin]. The Java plugin emits final
// host-language code (Java today, Go tomorrow) from the optimized
// instruction stream.
//
// Identity stub for now; downstream drivers read State.Code
// directly until the real backend lands.
package backend

import "tom/tomgo/stable/platform"

// Plugin implements [platform.Plugin] for the Backend phase.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Backend" }

// Run is currently identity; see package doc.
func (Plugin) Run(in platform.State) (platform.State, error) { return in, nil }
