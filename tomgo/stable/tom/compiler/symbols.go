// Package compiler — symbol-table helpers.
//
// Java reference: tom.engine.TomBase.* utility methods.
//
// The propagator and generator need to look up information about
// operators (codomain, slot types, list/array status, theory) and
// about BQTerms (their inferred type). These helpers wrap the
// SymbolTable lookups in shapes the compiler-side code expects.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// isListOperator returns true when the named operator carries a
// MakeEmptyList / MakeAddList hook (the markers Java's
// TomBase.isListOperator looks for).
func (env *compilerEnv) isListOperator(opName string) bool {
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
		case *tomast.MakeEmptyListDeclaration,
			*tomast.MakeAddListDeclaration:
			return true
		}
	}
	return false
}

// isSyntacticOperator returns true when the named operator is a
// regular constructor (not a list/array op, not AC). Mirrors Java's
// TomBase.isSyntacticOperator. A symbol is "syntactic" when it does
// not carry a MakeEmptyList / MakeEmptyArray / MakeAddList /
// MakeAddArray hook in its options — those are the markers Java's
// `isListOperator` / `isArrayOperator` look for.
func (env *compilerEnv) isSyntacticOperator(opName string) bool {
	if env.symbols == nil {
		return true
	}
	sym, ok := env.symbols.Symbols[opName]
	if !ok {
		// Unknown symbol — treat as syntactic so the syntactic
		// rewrite at least tries (matches Java's behaviour: it
		// returns true for null symbol via the convention).
		return true
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return true
	}
	opts, ok := s.Options.(*tomast.ConcOptionOptionList)
	if !ok {
		return true
	}
	for _, opt := range opts.Slots {
		dec, ok := opt.(*tomast.DeclarationToOptionOption)
		if !ok {
			continue
		}
		switch dec.AstDeclaration.(type) {
		case *tomast.MakeEmptyListDeclaration,
			*tomast.MakeAddListDeclaration,
			*tomast.MakeEmptyArrayDeclaration,
			*tomast.MakeAddArrayDeclaration:
			return false
		}
	}
	return true
}

// termTypeOfBQTerm returns the AstType embedded in a BQTerm. Java's
// TomBase.getTermType(BQTerm, SymbolTable) walks more shapes
// (BuildTerm, FunctionCall, …); the minimal port covers BQVariable
// + BQVariableStar which is enough for renamed subjects.
func (env *compilerEnv) termTypeOfBQTerm(t tomast.BQTerm) tomast.TomType {
	var r tomast.TomType
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		r = x.AstType
	case *tomast.BQVariableStarBQTerm:
		r = x.AstType
	case *tomast.BuildTermBQTerm:
		r = env.codomainOf(nameString(x.AstName))
	case *tomast.FunctionCallBQTerm:
		r = x.AstType
	case *tomast.BuildConstantBQTerm:
		r = env.codomainOf(nameString(x.AstName))
	case *tomast.SymbolOfBQTerm:
		// SymbolOf(t) has the inner term's type.
		r = env.termTypeOfBQTerm(x.GroundTerm)
	case *tomast.ExpressionToBQTermBQTerm:
		r = env.termTypeOfExpression(x.Exp)
	case *tomast.SubtermBQTerm:
		// Subterm(name, slotName, _) has the slot's type.
		r = env.slotTypeFor(nameString(x.AstName), nameString(x.SlotName))
	}
	if r == nil {
		r = tomast.MakeEmptyType()
	}
	return r
}

// termTypeOfExpression returns the result type of an Expression
// used as the source of a constraint (typically wrapped in
// `ExpressionToBQTerm`). Handles the IR shapes whose codomain is
// explicit in the node — GetElement, GetHead, Cast — so the typer
// can propagate concrete types down to fresh hoist variables.
func (env *compilerEnv) termTypeOfExpression(e tomast.Expression) tomast.TomType {
	switch x := e.(type) {
	case *tomast.CastExpression:
		return x.AstType
	case *tomast.GetElementExpression:
		return x.Codomain
	case *tomast.GetHeadExpression:
		return x.Codomain
	case *tomast.BQTermToExpressionExpression:
		return env.termTypeOfBQTerm(x.AstTerm)
	}
	return nil
}

// termTypeOfTomTerm returns the AstType of a TomTerm (the pattern
// side). Variables carry their type directly; RecordAppls inherit
// the head op's codomain.
func (env *compilerEnv) termTypeOfTomTerm(t tomast.TomTerm) tomast.TomType {
	switch x := t.(type) {
	case *tomast.VariableTomTerm:
		return x.AstType
	case *tomast.VariableStarTomTerm:
		return x.AstType
	case *tomast.RecordApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok && len(nl.Slots) > 0 {
			return env.codomainOf(nameString(nl.Slots[0]))
		}
	case *tomast.TermApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok && len(nl.Slots) > 0 {
			return env.codomainOf(nameString(nl.Slots[0]))
		}
	}
	return nil
}

// codomainOf looks up an operator's codomain TomType from the
// SymbolTable. Returns nil when the symbol is unknown.
func (env *compilerEnv) codomainOf(opName string) tomast.TomType {
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
	return tt.Codomain
}

// slotTypeFor returns the TomType of slot `slotName` on operator
// `opName`. Mirrors Java's TomBase.getSlotType. Returns nil when
// the symbol or slot can't be resolved.
func (env *compilerEnv) slotTypeFor(opName, slotName string) tomast.TomType {
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
	if !ok {
		return nil
	}
	pairs, ok := s.PairNameDeclList.(*tomast.ConcPairNameDeclPairNameDeclList)
	if !ok || len(pairs.Slots) == 0 {
		// Variadic (no slot names) — fall back to Domain[0] which
		// is the element type.
		if len(domain.Slots) > 0 {
			return domain.Slots[0]
		}
		return nil
	}
	for i, p := range pairs.Slots {
		pair, ok := p.(*tomast.PairNameDeclPairNameDecl)
		if !ok {
			continue
		}
		if nameString(pair.SlotName) == slotName && i < len(domain.Slots) {
			return domain.Slots[i]
		}
	}
	return nil
}
