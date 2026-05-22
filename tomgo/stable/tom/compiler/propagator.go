// Package compiler — ConstraintPropagator.
//
// Java reference: stable/tom/engine/compiler/ConstraintPropagator.t
// (139 LOC) and stable/tom/engine/compiler/propagator/ (~1100 LOC
// across SyntacticPropagator, ArrayPropagator, VariadicPropagator,
// ACPropagator, GeneralPurposePropagator, IBasePropagator).
//
// `performPropagations` normalises the constraint conjunction
// produced by `renameSubjects`. It runs each sub-propagator until
// the constraint reaches a fixed point. Order matters because some
// propagators introduce shape changes (e.g. SyntacticPropagator
// turns `Match(Foo(a,b), s)` into a conjunction containing
// `Match(a, subterm1(s))`, which the next pass may further reduce).
//
// Status: SKELETON. Returns the input unchanged. Real algorithms
// arrive with task #59 (SyntacticPropagator + ConstraintPropagator
// dispatcher) and later for the other sub-propagators.

package compiler

import "tom/tomgo/stable/library/tomast"

// mapConstraintSlots applies `f` to each item in `slots`, preserving
// pointer identity when no element changed (so the caller can skip
// re-building the enclosing And/Or node). Returns the new slice and
// a `changed` flag. Helper for the propagators' And/Or/
// OrConstraintDisjunction cases — Java's `%match` does the
// identity-preserving fall-through automatically.
func mapConstraintSlots(slots []tomast.Constraint, f func(tomast.Constraint) tomast.Constraint) ([]tomast.Constraint, bool) {
	out := make([]tomast.Constraint, len(slots))
	changed := false
	for i, item := range slots {
		r := f(item)
		if r != item {
			changed = true
		}
		out[i] = r
	}
	return out, changed
}

// performPropagations is the entry point used by CompileMatch. Java:
// ConstraintPropagator.performPropagations(Constraint).
//
// Java runs each sub-propagator (Syntactic, Variadic, Array,
// GeneralPurpose, AC) in order, looping until a full pass leaves
// the constraint unchanged. The Go port mirrors that fixed-point
// loop, currently with just one propagator wired up
// (SyntacticPropagator). Variadic / Array / GeneralPurpose / AC
// land later in tasks #59-end.
func (e *compilerEnv) performPropagations(c tomast.Constraint) tomast.Constraint {
	propagators := []func(tomast.Constraint) tomast.Constraint{
		e.propagateSyntactic,
		e.propagateVariadic,
		e.propagateArray,
		e.propagateGP,
	}
	idle := 0
	for {
		for _, p := range propagators {
			r := p(c)
			if r == c {
				idle++
				if idle == len(propagators) {
					return c
				}
			} else {
				idle = 0
				c = r
			}
		}
	}
}
