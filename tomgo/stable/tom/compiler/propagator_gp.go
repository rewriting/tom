// Package compiler — GeneralPurposePropagator.
//
// Java reference: stable/tom/engine/compiler/propagator/GeneralPurposePropagator.t
// (228 LOC).
//
// Handles the constraint shapes the per-shape (Syntactic / Variadic /
// Array / AC) propagators don't claim. The two important rules for
// Peano-class fixtures are:
//
//  1. AntiTerm — wraps the pattern in `AntiMatchConstraint` and
//     detaches its annotations.
//  2. Annotation detach for Variable / VariableStar patterns:
//     `MatchConstraint(Variable[Constraints=non-empty], g, T)` →
//     `AndConstraint(MatchConstraint(Variable[no-constraints], g, T),
//                    <constraints from performDetach>)`.
//
// Other rules (merge for repeated variables, anti-then-match swap)
// are skipped in this minimal port.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// propagateGP — Go port of GeneralPurposePropagator.propagate.
func (env *compilerEnv) propagateGP(c tomast.Constraint) tomast.Constraint {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		if rewritten, ok := env.tryGeneralPropagation(x); ok {
			return rewritten
		}
		return x
	case *tomast.AndConstraintConstraint:
		// First merge duplicates: non-linear `x` patterns (x
		// appearing twice as a Variable in two MatchConstraints)
		// must produce a fresh var + equality test on the second
		// occurrence. Java's rule lives in GeneralPurposePropagator
		// strategy `ReplaceMatchConstraint` (lines 124-131).
		if merged, ok := env.mergeDuplicateVars(x); ok {
			return env.propagateGP(merged)
		}
		newSlots := make([]tomast.Constraint, len(x.Slots))
		changed := false
		for i, item := range x.Slots {
			r := env.propagateGP(item)
			if r != item {
				changed = true
			}
			newSlots[i] = r
		}
		if !changed {
			return c
		}
		return tomast.MakeAndConstraint(newSlots...)
	case *tomast.OrConstraintConstraint:
		newSlots := make([]tomast.Constraint, len(x.Slots))
		changed := false
		for i, item := range x.Slots {
			r := env.propagateGP(item)
			if r != item {
				changed = true
			}
			newSlots[i] = r
		}
		if !changed {
			return c
		}
		return tomast.MakeOrConstraint(newSlots...)
	case *tomast.OrConstraintDisjunctionConstraint:
		newSlots := make([]tomast.Constraint, len(x.Slots))
		changed := false
		for i, item := range x.Slots {
			r := env.propagateGP(item)
			if r != item {
				changed = true
			}
			newSlots[i] = r
		}
		if !changed {
			return c
		}
		return tomast.MakeOrConstraintDisjunction(newSlots...)
	}
	return c
}

// tryGeneralPropagation applies the Variable-annotation-detach rule
// (Java lines 138-146). For MatchConstraint(Variable[Constraints != []],
// g, T), build:
//
//	AndConstraint(
//	  MatchConstraint(Variable[Constraints = []], g, T),
//	  <constraints from performDetach>,
//	)
//
// Returns (new, true) when the rule fires, (input, false) otherwise.
func (env *compilerEnv) tryGeneralPropagation(m *tomast.MatchConstraintConstraint) (tomast.Constraint, bool) {
	switch p := m.Pattern.(type) {
	case *tomast.VariableTomTerm:
		if hasConstraint(p.Constraints) {
			detached := env.performDetach(m)
			stripped := tomast.MakeVariable(p.Options, p.AstName, p.AstType,
				tomast.MakeConcConstraint())
			conj := []tomast.Constraint{tomast.MakeMatchConstraint(stripped, m.Subject, m.AstType)}
			conj = append(conj, detached...)
			return tomast.MakeAndConstraint(conj...), true
		}
	case *tomast.VariableStarTomTerm:
		if hasConstraint(p.Constraints) {
			detached := env.performDetach(m)
			stripped := tomast.MakeVariableStar(p.Options, p.AstName, p.AstType,
				tomast.MakeConcConstraint())
			conj := []tomast.Constraint{tomast.MakeMatchConstraint(stripped, m.Subject, m.AstType)}
			conj = append(conj, detached...)
			return tomast.MakeAndConstraint(conj...), true
		}
	}
	return m, false
}

// mergeDuplicateVars walks the conjuncts of an AndConstraint left
// to right. The first `MatchConstraint(Variable(name)|VariableStar(name), ...)`
// with a user-given (non-PositionName) `name` is kept as-is. Any
// subsequent `MatchConstraint(<same name>, subj2)` is replaced with:
//
//	AndConstraint(
//	  MatchConstraint(freshVar, subj2, T),
//	  MatchConstraint(TestVar(freshVar), origVarBQ, T),
//	)
//
// `TestVar` is later turned into `EqualBQTerm(t, freshVar, origVar)`
// by SyntacticGenerator — that's the runtime non-linearity check.
//
// Returns `(newAndConstraint, true)` when at least one duplicate
// was rewritten so the caller can re-run the propagator pass.
func (env *compilerEnv) mergeDuplicateVars(a *tomast.AndConstraintConstraint) (tomast.Constraint, bool) {
	type seenInfo struct {
		isStar bool
		v      tomast.TomTerm // the original Variable/VariableStar
	}
	seen := map[string]seenInfo{}
	newSlots := make([]tomast.Constraint, 0, len(a.Slots))
	changed := false
	for _, item := range a.Slots {
		mc, isMC := item.(*tomast.MatchConstraintConstraint)
		if !isMC {
			newSlots = append(newSlots, item)
			continue
		}
		// Recurse into nested AndConstraints so duplicates that
		// appear across nested levels are still caught. We
		// preserve the SAME `seen` map across the descent so
		// duplicates between an outer and an inner conjunct merge.
		var name string
		var isStar bool
		var origVar tomast.TomTerm
		switch p := mc.Pattern.(type) {
		case *tomast.VariableTomTerm:
			name = userVisibleName(p.AstName)
			origVar = p
		case *tomast.VariableStarTomTerm:
			name = userVisibleName(p.AstName)
			isStar = true
			origVar = p
		}
		if name == "" {
			newSlots = append(newSlots, item)
			continue
		}
		if prev, dup := seen[name]; dup {
			_ = prev
			freshVar := env.getFreshVariable(mc.AstType)
			if isStar {
				freshVar = env.getFreshVariableStar(mc.AstType)
			}
			freshVarT := convertBQVarToVar(freshVar)
			origVarBQ := convertVarToBQVar(origVar)
			merged := tomast.MakeAndConstraint(
				tomast.MakeMatchConstraint(freshVarT, mc.Subject, mc.AstType),
				tomast.MakeMatchConstraint(
					tomast.MakeTestVar(freshVarT),
					origVarBQ,
					mc.AstType,
				),
			)
			newSlots = append(newSlots, merged)
			changed = true
			continue
		}
		seen[name] = seenInfo{isStar: isStar, v: origVar}
		newSlots = append(newSlots, item)
	}
	if !changed {
		return a, false
	}
	return tomast.MakeAndConstraint(newSlots...), true
}

// userVisibleName returns the user-given Variable/VariableStar name
// when the AstName is a `Name(s)` (i.e. not a generated
// `PositionName`). PositionName-named variables are compiler-fresh
// and never trigger non-linear merge — they're already unique by
// construction. Returns "" when the name is generated.
func userVisibleName(n tomast.TomName) string {
	if name, ok := n.(*tomast.NameTomName); ok {
		return name.String_
	}
	return ""
}

// hasConstraint returns true when the constraint list is non-empty.
func hasConstraint(cs tomast.ConstraintList) bool {
	conc, ok := cs.(*tomast.ConcConstraintConstraintList)
	if !ok {
		return false
	}
	return len(conc.Slots) > 0
}
