// Package compiler — ArrayPropagator.
//
// Java reference: stable/tom/engine/compiler/propagator/ArrayPropagator.t
// (221 LOC).
//
// Decomposes `MatchConstraint(RecordAppl[op, slots], g, T)` where the
// op is an array operator. The output is the conjunction of:
//
//   - MatchConstraint(freshSubj*, g, T)
//   - MatchConstraint(RecordAppl(op,[]), SymbolOf(freshSubj*), T)
//   - MatchConstraint(freshIndex*, Integer(0), int)
//   - performDetach(m)            — alias-constraint detachment
//   - per slot, depending on the slot's pattern shape:
//       VariableStar (X*):
//         if last slot in pattern → MatchConstraint(X*, GetSliceArray(op,freshSubj*,curIndex,GetSize(op,freshSubj*)), T)
//         else                    → MatchConstraint(beginIdx, curIndex)
//                                  /\ MatchConstraint(endIdx,   curIndex)
//                                  /\ MatchConstraint(X*, VariableHeadArray(op,freshSubj*,beginIdx,endIdx), T)
//                                  /\ MatchConstraint(newCurIdx, endIdx)
//       other pattern (element):
//         !EmptyArrayConstraint(op, freshSubj*, curIndex)
//         /\ MatchConstraint(pat, GetElement(op,T_elem,freshSubj*,curIndex), elemType)
//         /\ MatchConstraint(newCurIdx, AddOne(curIndex))
//         if last slot in pattern → also add EmptyArrayConstraint(op, freshSubj*, newCurIdx)
//
// `detachSublists` (the nested-conc-inside-conc rule) is skipped in
// this first port — fixtures using it (TestArray2) can wait.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// propagateArray walks a constraint top-down. When it sees a
// `MatchConstraint(RecordAppl[...], !SymbolOf, T)` with an array
// head symbol, it expands using the rule above.
func (env *compilerEnv) propagateArray(c tomast.Constraint) tomast.Constraint {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		if rewritten, ok := env.tryExpandArray(x); ok {
			return rewritten
		}
		return x
	case *tomast.AndConstraintConstraint:
		if ns, changed := mapConstraintSlots(x.Slots, env.propagateArray); changed {
			return tomast.MakeAndConstraint(ns...)
		}
		return c
	case *tomast.OrConstraintConstraint:
		if ns, changed := mapConstraintSlots(x.Slots, env.propagateArray); changed {
			return tomast.MakeOrConstraint(ns...)
		}
		return c
	case *tomast.OrConstraintDisjunctionConstraint:
		if ns, changed := mapConstraintSlots(x.Slots, env.propagateArray); changed {
			return tomast.MakeOrConstraintDisjunction(ns...)
		}
		return c
	}
	return c
}

// tryExpandArray performs the rewrite for one MatchConstraint with
// an array-operator head. Returns (new, true) when the rule fires.
func (env *compilerEnv) tryExpandArray(m *tomast.MatchConstraintConstraint) (tomast.Constraint, bool) {
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
	if !env.isArrayOperator(opName) {
		return m, false
	}

	aType := m.AstType
	g := m.Subject
	freshSubject := env.getFreshVariableStar(aType)
	freshSubjectVar := convertBQVarToVar(freshSubject)
	freshIndex := env.getFreshIndex()
	freshIndexVar := convertBQVarToVar(freshIndex)

	slotType := env.elemTypeOf(opName)

	freshVarDecl := tomast.MakeMatchConstraint(freshSubjectVar, g, aType)

	// Build per-slot constraints, threading curIndex across slots.
	slotsList, _ := pat.Slots.(*tomast.ConcSlotSlotList)
	var perSlot []tomast.Constraint
	if slotsList == nil || len(slotsList.Slots) == 0 {
		// Empty array pattern: just check the subject is empty.
		perSlot = append(perSlot,
			tomast.MakeEmptyArrayConstraint(firstName, freshSubject, freshIndex),
		)
	} else {
		curIndex := freshIndex
		for i, s := range slotsList.Slots {
			pair, ok := s.(*tomast.PairSlotApplSlot)
			if !ok {
				continue
			}
			appl := pair.Appl
			isLast := i == len(slotsList.Slots)-1
			newCurIndex := env.getFreshIndex()
			newCurVar := convertBQVarToVar(newCurIndex)

			if _, isVarStar := appl.(*tomast.VariableStarTomTerm); isVarStar {
				if isLast {
					// Last variable-star: bind to slice from curIndex to size.
					perSlot = append(perSlot,
						tomast.MakeMatchConstraint(
							appl,
							tomast.MakeExpressionToBQTerm(
								tomast.MakeGetSliceArray(firstName, freshSubject, curIndex,
									tomast.MakeExpressionToBQTerm(
										tomast.MakeGetSize(firstName, freshSubject),
									),
								),
							),
							aType,
						),
					)
				} else {
					beginIdx := env.getBeginIndex()
					endIdx := env.getEndIndex()
					beginVar := convertBQVarToVar(beginIdx)
					endVar := convertBQVarToVar(endIdx)
					perSlot = append(perSlot,
						tomast.MakeMatchConstraint(beginVar, curIndex, aType),
						tomast.MakeMatchConstraint(endVar, curIndex, aType),
						tomast.MakeMatchConstraint(
							appl,
							tomast.MakeVariableHeadArray(firstName, freshSubject, beginIdx, endIdx),
							aType,
						),
						tomast.MakeMatchConstraint(newCurVar, endIdx, aType),
					)
				}
			} else {
				// Ordinary element pattern.
				applType := env.termTypeOfTomTerm(appl)
				perSlot = append(perSlot,
					tomast.MakeNegate(
						tomast.MakeEmptyArrayConstraint(firstName, freshSubject, curIndex),
					),
					tomast.MakeMatchConstraint(
						appl,
						tomast.MakeExpressionToBQTerm(
							tomast.MakeGetElement(firstName, slotType, freshSubject, curIndex),
						),
						applType,
					),
					tomast.MakeMatchConstraint(
						newCurVar,
						tomast.MakeExpressionToBQTerm(tomast.MakeAddOne(curIndex)),
						env.intType(),
					),
				)
				if isLast {
					perSlot = append(perSlot,
						tomast.MakeEmptyArrayConstraint(firstName, freshSubject, newCurIndex),
					)
				}
			}
			curIndex = newCurIndex
		}
	}

	// Symbol-of guard: head equality.
	emptyOptions := pat.Options
	symbolGuard := tomast.MakeMatchConstraint(
		tomast.MakeRecordAppl(emptyOptions, nameList, tomast.MakeConcSlot(), tomast.MakeConcConstraint()),
		tomast.MakeSymbolOf(freshSubject),
		aType,
	)
	indexDecl := tomast.MakeMatchConstraint(freshIndexVar,
		tomast.MakeExpressionToBQTerm(tomast.MakeInteger(0)),
		env.intType(),
	)

	conj := []tomast.Constraint{freshVarDecl, symbolGuard, indexDecl}
	conj = append(conj, env.performDetach(m)...)
	conj = append(conj, perSlot...)
	return tomast.MakeAndConstraint(conj...), true
}

// isArrayOperator returns true when the named op carries a
// MakeEmptyArray / MakeAddArray hook in its symbol table options —
// Java's TomBase.isArrayOperator equivalent.
func (env *compilerEnv) isArrayOperator(opName string) bool {
	if env.symbols == nil {
		return false
	}
	sym, ok := env.symbols.Symbols[opName]
	if !ok {
		return false
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return false
	}
	opts, ok := s.Options.(*tomast.ConcOptionOptionList)
	if !ok {
		return false
	}
	for _, opt := range opts.Slots {
		d, ok := opt.(*tomast.DeclarationToOptionOption)
		if !ok {
			continue
		}
		switch d.AstDeclaration.(type) {
		case *tomast.MakeEmptyArrayDeclaration,
			*tomast.MakeAddArrayDeclaration:
			return true
		}
	}
	return false
}

// elemTypeOf returns the element type of a variadic operator (its
// single Domain entry). Returns nil when the symbol is unknown.
func (env *compilerEnv) elemTypeOf(opName string) tomast.TomType {
	if env.symbols == nil {
		return nil
	}
	sym, ok := env.symbols.Symbols[opName]
	if !ok {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return nil
	}
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok || len(domain.Slots) == 0 {
		return nil
	}
	return domain.Slots[0]
}

// getFreshIndex — Java Compiler.t:218-220. Fresh int-typed
// BQVariableStar named `_<n>`.
func (env *compilerEnv) getFreshIndex() tomast.BQTerm {
	return env.getFreshVariableStar(env.intType())
}

// getBeginIndex / getEndIndex — Java Compiler.t:210-216.
func (env *compilerEnv) getBeginIndex() tomast.BQTerm {
	return env.getBeginVariableStar(env.intType())
}

func (env *compilerEnv) getEndIndex() tomast.BQTerm {
	return env.getEndVariableStar(env.intType())
}
