package tom

import "fmt"

// Phase is the signature every phase function in stable/tom/<phase>/
// satisfies — `Run(in State) (State, error)`. Each phase advances the
// State by one step.
type Phase = func(State) (State, error)

// Run threads `initial` through `phases` in order. On the first
// error, returns the most-recent State and the error wrapped with
// a hint of which phase number failed.
//
// Callers can use this directly to run partial pipelines — e.g.
// tests that stop after the Desugarer or the cmd/tom driver that
// honours a `-stop` flag.
func Run(initial State, phases ...Phase) (State, error) {
	state := initial
	for i, phase := range phases {
		next, err := phase(state)
		if err != nil {
			return state, fmt.Errorf("phase %d: %w", i, err)
		}
		state = next
	}
	return state, nil
}
