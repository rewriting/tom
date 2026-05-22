// Package compiler — VariadicPropagator.
//
// Java reference: stable/tom/engine/compiler/propagator/VariadicPropagator.t
// (215 LOC).
//
// Same idea as ArrayPropagator but for `%oplist` symbols. The
// decomposition uses head/tail/is_empty instead of
// get_element/get_size:
//
//   freshVar = g
//   /\ conc[]   = SymbolOf(freshVar)
//   /\ for each non-last slot:
//        if element: NotEmpty(freshVar)
//                  /\ x = ListHead(name, T_elem, freshVar)
//                  /\ newFresh = ListTail(name, freshVar)
//        if X*:     begin = freshVar
//                  /\ end   = freshVar
//                  /\ X* = VariableHeadList(name, begin, end)
//                  /\ newFresh = end
//        and freshVar := newFresh for next slot
//   /\ for the LAST slot (if element): also assert EmptyList on newFresh
//   /\ for the LAST slot (if X*): bind X* directly to freshVar
//
// VariableHeadList is consumed by VariadicGenerator into a do/while
// loop that advances `end` along the tail of `begin` until empty.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// isSameVariadicHead reports whether `appl` is a RecordAppl whose
// (first) head name matches `opName`. Used to detect the "sublist
// range" case where a variadic-headed pattern appears as an arg
// inside the SAME variadic operator's arg list (e.g. `list(_*,
// list(a,b,c), _*)` against a list subject). Java's reference
// (VariadicGenerator.t) routes these to the slice-iteration
// emitter rather than to single-element head/tail walking.
func isSameVariadicHead(appl tomast.TomTerm, opName string) bool {
	ra, ok := appl.(*tomast.RecordApplTomTerm)
	if !ok {
		return false
	}
	nameList, ok := ra.NameList.(*tomast.ConcTomNameTomNameList)
	if !ok || len(nameList.Slots) == 0 {
		return false
	}
	head, ok := nameList.Slots[0].(*tomast.NameTomName)
	if !ok {
		return false
	}
	return head.String_ == opName
}

// propagateVariadic walks a constraint and applies the list-style
// expansion top-down at every match-able node.
func (env *compilerEnv) propagateVariadic(c tomast.Constraint) tomast.Constraint {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		if rewritten, ok := env.tryExpandVariadic(x); ok {
			return rewritten
		}
		return x
	case *tomast.AndConstraintConstraint:
		newSlots := make([]tomast.Constraint, len(x.Slots))
		changed := false
		for i, item := range x.Slots {
			r := env.propagateVariadic(item)
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
			r := env.propagateVariadic(item)
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
			r := env.propagateVariadic(item)
			if r != item {
				changed = true
			}
			newSlots[i] = r
		}
		if !changed {
			return c
		}
		return tomast.MakeOrConstraintDisjunction(newSlots...)
	case *tomast.NegateConstraint:
		// Anti-pattern `!list(_*,...,_*)` is wrapped in Negate by
		// the GP propagator. The inner MatchConstraint may itself
		// be a list-pattern that needs further variadic expansion.
		inner := env.propagateVariadic(x.C)
		if inner != x.C {
			return tomast.MakeNegate(inner)
		}
		return c
	}
	return c
}

// tryExpandVariadic does the actual rewrite on one MatchConstraint
// whose head is a list operator. Returns (new, true) when the rule
// fires.
func (env *compilerEnv) tryExpandVariadic(m *tomast.MatchConstraintConstraint) (tomast.Constraint, bool) {
	pat, ok := m.Pattern.(*tomast.RecordApplTomTerm)
	if !ok {
		return m, false
	}
	if _, isSymbolOf := m.Subject.(*tomast.SymbolOfBQTerm); isSymbolOf {
		return m, false
	}
	nameList, ok := pat.NameList.(*tomast.ConcTomNameTomNameList)
	if !ok || len(nameList.Slots) == 0 {
		return m, false
	}
	firstName, ok := nameList.Slots[0].(*tomast.NameTomName)
	if !ok {
		return m, false
	}
	opName := firstName.String_
	if !env.isListOperator(opName) {
		return m, false
	}

	aType := m.AstType
	g := m.Subject
	slotType := env.elemTypeOf(opName)

	freshVariable := env.getFreshVariableStar(aType)
	freshVariableVar := convertBQVarToVar(freshVariable)
	freshVarDecl := tomast.MakeMatchConstraint(freshVariableVar, g, aType)

	emptyOptions := pat.Options
	isSymbolConstr := tomast.MakeMatchConstraint(
		tomast.MakeRecordAppl(emptyOptions, nameList, tomast.MakeConcSlot(), tomast.MakeConcConstraint()),
		tomast.MakeSymbolOf(freshVariable),
		aType,
	)

	slotsList, _ := pat.Slots.(*tomast.ConcSlotSlotList)
	var perSlot []tomast.Constraint

	currentFresh := freshVariable
	if slotsList == nil || len(slotsList.Slots) == 0 {
		perSlot = append(perSlot,
			tomast.MakeEmptyListConstraint(firstName, currentFresh),
		)
	} else {
		for i, s := range slotsList.Slots {
			pair, ok := s.(*tomast.PairSlotApplSlot)
			if !ok {
				continue
			}
			appl := pair.Appl
			isLast := i == len(slotsList.Slots)-1
			newFresh := env.getFreshVariableStar(aType)

			if _, isVarStar := appl.(*tomast.VariableStarTomTerm); isVarStar {
				if isLast {
					// Last var-star: bind directly to the remaining
					// freshVariable (no more list to traverse).
					perSlot = append(perSlot,
						tomast.MakeMatchConstraint(appl, currentFresh, aType),
					)
				} else {
					beginSublist := env.getBeginVariableStar(aType)
					endSublist := env.getEndVariableStar(aType)
					perSlot = append(perSlot,
						tomast.MakeMatchConstraint(convertBQVarToVar(beginSublist), currentFresh, aType),
						tomast.MakeMatchConstraint(convertBQVarToVar(endSublist), currentFresh, aType),
						tomast.MakeMatchConstraint(
							appl,
							tomast.MakeVariableHeadList(firstName, beginSublist, endSublist),
							aType,
						),
						tomast.MakeMatchConstraint(convertBQVarToVar(newFresh), endSublist, aType),
					)
				}
			} else if isSameVariadicHead(appl, opName) {
				// Sub-range matching: the element pattern is itself a
				// RecordAppl whose head is the SAME variadic operator
				// as the outer list. Tom semantics says match a
				// CONTIGUOUS RANGE of the outer list — iterate (begin,
				// end) cursors and recursively pattern-match the
				// inner against the resulting slice. The shape is the
				// same as var-star non-last (begin / end / slice /
				// newFresh), except the slice is consumed by another
				// MatchConstraint against the inner pattern, which
				// the next propagator iteration expands.
				beginSublist := env.getBeginVariableStar(aType)
				endSublist := env.getEndVariableStar(aType)
				perSlot = append(perSlot,
					tomast.MakeMatchConstraint(convertBQVarToVar(beginSublist), currentFresh, aType),
					tomast.MakeMatchConstraint(convertBQVarToVar(endSublist), currentFresh, aType),
					tomast.MakeMatchConstraint(
						appl,
						tomast.MakeVariableHeadList(firstName, beginSublist, endSublist),
						aType,
					),
					tomast.MakeMatchConstraint(convertBQVarToVar(newFresh), endSublist, aType),
				)
			} else {
				// Ordinary element pattern: NotEmpty + ListHead +
				// ListTail.
				applType := env.termTypeOfTomTerm(appl)
				if applType == nil {
					applType = aType
				}
				perSlot = append(perSlot,
					tomast.MakeNegate(
						tomast.MakeEmptyListConstraint(firstName, currentFresh),
					),
					tomast.MakeMatchConstraint(
						appl,
						tomast.MakeListHead(firstName, slotType, currentFresh),
						applType,
					),
					tomast.MakeMatchConstraint(
						convertBQVarToVar(newFresh),
						tomast.MakeListTail(firstName, currentFresh),
						aType,
					),
				)
				if isLast {
					perSlot = append(perSlot,
						tomast.MakeEmptyListConstraint(firstName, newFresh),
					)
				}
			}
			currentFresh = newFresh
		}
	}

	conj := []tomast.Constraint{freshVarDecl, isSymbolConstr}
	conj = append(conj, env.performDetach(m)...)
	conj = append(conj, perSlot...)
	return tomast.MakeAndConstraint(conj...), true
}
