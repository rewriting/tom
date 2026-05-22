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
	"tom/tomgo/stable/tom"
)

// resolveSymbolIn fetches the SymbolTomSymbol for opName from `st`.
// Returns (nil, false) when the table is missing, the symbol is
// unknown, or the entry has an unexpected shape. Shared by every
// helper below that walks a symbol's Options / TypesToType /
// PairNameDeclList — none of those casts make sense without first
// landing on a valid SymbolTomSymbol.
func resolveSymbolIn(st *tom.SymbolTable, opName string) (*tomast.SymbolTomSymbol, bool) {
	if st == nil {
		return nil, false
	}
	sym, ok := st.Symbols[opName]
	if !ok {
		return nil, false
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil, false
	}
	return s, true
}

// resolveSymbol is the compilerEnv-bound convenience wrapper.
func (env *compilerEnv) resolveSymbol(opName string) (*tomast.SymbolTomSymbol, bool) {
	return resolveSymbolIn(env.symbols, opName)
}

// hasMakeOptionDecl returns true when the symbol's Options list
// carries any of the supplied Make* declaration markers. Used to
// distinguish list / array / syntactic operators (Java's
// TomBase.isListOperator & friends rely on the same marker
// presence).
func hasMakeOptionDecl(s *tomast.SymbolTomSymbol, isMatch func(tomast.Declaration) bool) bool {
	opts, ok := s.Options.(*tomast.ConcOptionOptionList)
	if !ok {
		return false
	}
	for _, opt := range opts.Slots {
		d, ok := opt.(*tomast.DeclarationToOptionOption)
		if !ok {
			continue
		}
		if isMatch(d.AstDeclaration) {
			return true
		}
	}
	return false
}

// isListOperator returns true when the named operator carries a
// MakeEmptyList / MakeAddList hook (the markers Java's
// TomBase.isListOperator looks for).
func (env *compilerEnv) isListOperator(opName string) bool {
	s, ok := env.resolveSymbol(opName)
	if !ok {
		return false
	}
	return hasMakeOptionDecl(s, func(d tomast.Declaration) bool {
		switch d.(type) {
		case *tomast.MakeEmptyListDeclaration,
			*tomast.MakeAddListDeclaration:
			return true
		}
		return false
	})
}

// isSyntacticOperator returns true when the named operator is a
// regular constructor (not a list/array op, not AC). Mirrors Java's
// TomBase.isSyntacticOperator. A symbol is "syntactic" when it does
// not carry a MakeEmptyList / MakeEmptyArray / MakeAddList /
// MakeAddArray hook in its options — those are the markers Java's
// `isListOperator` / `isArrayOperator` look for.
func (env *compilerEnv) isSyntacticOperator(opName string) bool {
	s, ok := env.resolveSymbol(opName)
	if !ok {
		// Unknown symbol — treat as syntactic so the syntactic
		// rewrite at least tries (matches Java's behaviour: it
		// returns true for null symbol via the convention).
		return true
	}
	return !hasMakeOptionDecl(s, func(d tomast.Declaration) bool {
		switch d.(type) {
		case *tomast.MakeEmptyListDeclaration,
			*tomast.MakeAddListDeclaration,
			*tomast.MakeEmptyArrayDeclaration,
			*tomast.MakeAddArrayDeclaration:
			return true
		}
		return false
	})
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
	case *tomast.ListHeadBQTerm:
		// ListHead(op, codomain, v) has the slot (element) type
		// stored in `Codomain`. Used by VariadicPropagator before
		// `replaceListHeadTail` rewrites it to GetHead.
		r = x.Codomain
	case *tomast.ListTailBQTerm:
		// ListTail(op, v) has the list type (= op's codomain).
		r = env.codomainOf(nameString(x.Opname))
	case *tomast.VariableHeadListBQTerm:
		r = env.codomainOf(nameString(x.Opname))
	case *tomast.VariableHeadArrayBQTerm:
		r = env.codomainOf(nameString(x.Opname))
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
	case *tomast.AntiTermTomTerm:
		// `!pat` has the same type as `pat` — anti-pattern matches
		// the same domain, just negated.
		return env.termTypeOfTomTerm(x.TomTerm)
	}
	return nil
}

// typesToType extracts the TypesToTypeTomType node holding a
// symbol's (Domain, Codomain). nil when the symbol's TypesToType
// slot has an unexpected shape.
func typesToType(s *tomast.SymbolTomSymbol) *tomast.TypesToTypeTomType {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return nil
	}
	return tt
}

// codomainOf looks up an operator's codomain TomType from the
// SymbolTable. Returns nil when the symbol is unknown.
func (env *compilerEnv) codomainOf(opName string) tomast.TomType {
	s, ok := env.resolveSymbol(opName)
	if !ok {
		return nil
	}
	tt := typesToType(s)
	if tt == nil {
		return nil
	}
	return tt.Codomain
}

// slotTypeFor returns the TomType of slot `slotName` on operator
// `opName`. Mirrors Java's TomBase.getSlotType. Returns nil when
// the symbol or slot can't be resolved.
func (env *compilerEnv) slotTypeFor(opName, slotName string) tomast.TomType {
	s, ok := env.resolveSymbol(opName)
	if !ok {
		return nil
	}
	tt := typesToType(s)
	if tt == nil {
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
