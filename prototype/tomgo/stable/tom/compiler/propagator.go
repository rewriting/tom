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

// mapIdentityPreserving applies `f` to each item in `slots`, returning
// the new slice and a `changed` flag (true iff at least one element
// changed pointer identity). The caller uses `changed` to decide
// whether to rebuild the enclosing constructor (And/Or/Negation/…) or
// hand back the input untouched — Java's `%match` does this
// identity-preserving fall-through automatically.
func mapIdentityPreserving[T comparable](slots []T, f func(T) T) ([]T, bool) {
	out := make([]T, len(slots))
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

// mapConstraintSlots is the Constraint specialisation of
// [mapIdentityPreserving], kept as a named helper so the four
// propagators read cleanly at the call site.
func mapConstraintSlots(slots []tomast.Constraint, f func(tomast.Constraint) tomast.Constraint) ([]tomast.Constraint, bool) {
	return mapIdentityPreserving(slots, f)
}

// mapExpressionSlots is the Expression specialisation of
// [mapIdentityPreserving] used by the syntactic/array/variadic
// generators' OrConnector / OrExpressionDisjunction cases.
func mapExpressionSlots(slots []tomast.Expression, f func(tomast.Expression) tomast.Expression) ([]tomast.Expression, bool) {
	return mapIdentityPreserving(slots, f)
}

// walkExpressionConnectives walks the And / OrConnector /
// OrExpressionDisjunction / Negation / AntiMatchExpression
// connective layers of an Expression, recursing through `recurse`,
// and delegates the ConstraintToExpression leaves to `tryLeaf`.
// Identity is preserved through every cap: if no descendant
// changed, the input is returned as-is. All three generators
// (syntactic / variadic / array) share this exact shape — they
// only differ in `recurse` (their `generateX` self-call) and
// `tryLeaf` (their `tryGenerateX` rewriter).
func walkExpressionConnectives(
	e tomast.Expression,
	recurse func(tomast.Expression) tomast.Expression,
	tryLeaf func(*tomast.ConstraintToExpressionExpression) (tomast.Expression, bool),
) tomast.Expression {
	switch x := e.(type) {
	case *tomast.ConstraintToExpressionExpression:
		if rewritten, ok := tryLeaf(x); ok {
			return rewritten
		}
		return e
	case *tomast.AndExpression:
		l := recurse(x.Arg1)
		r := recurse(x.Arg2)
		if l != x.Arg1 || r != x.Arg2 {
			return tomast.MakeAnd(l, r)
		}
		return e
	case *tomast.OrConnectorExpression:
		if ns, changed := mapExpressionSlots(x.Slots, recurse); changed {
			return tomast.MakeOrConnector(ns...)
		}
		return e
	case *tomast.OrExpressionDisjunctionExpression:
		if ns, changed := mapExpressionSlots(x.Slots, recurse); changed {
			return tomast.MakeOrExpressionDisjunction(ns...)
		}
		return e
	case *tomast.NegationExpression:
		if inner := recurse(x.Arg); inner != x.Arg {
			return tomast.MakeNegation(inner)
		}
		return e
	case *tomast.AntiMatchExpressionExpression:
		if inner := recurse(x.Expression); inner != x.Expression {
			return tomast.MakeAntiMatchExpression(inner)
		}
		return e
	}
	return e
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
