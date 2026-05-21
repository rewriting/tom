// Package typer — buildArray / buildList helpers.
//
// Java reference: tom.engine.tools.ASTFactory.buildArray /
// buildList (~70 LOC each). Invoked from TyperPlugin.TransformBQAppl
// when a backquoted constructor turns out to name a list or array
// operator.
//
// Goal: turn `BQAppl(name, [arg1, arg2, ..., argN])` for an
// array op into:
//
//	BuildConsArray(name, argN,
//	  BuildConsArray(name, argN-1,
//	    ...
//	      BuildConsArray(name, arg1,
//	        BuildEmptyArray(name, Integer(N)))))
//
// — outermost cons holds the LAST arg, innermost holds the FIRST.
// `myAdd` (or whatever `make_append` resolves to) appends from the
// innermost outwards, restoring source order at runtime.
//
// `buildList` is the same shape with `BuildConsList` and
// `BuildEmptyList`.

package typer

import "tom/tomgo/stable/library/tomast"

// buildArrayBQTerm wraps a BQTermList into the cons / empty chain
// described above. Java: ASTFactory.buildArray(name, args.reverse(),
// 0, symbolTable). The reverse + walk-from-head is equivalent to
// our walk-from-tail.
//
// This minimal port skips the `BuildAppendArray` flattening Java
// performs when an arg is itself a list of the same name (top
// domain == top codomain edge case). Fixtures relying on that go
// through later.
func buildArrayBQTerm(name tomast.TomName, args tomast.BQTermList) tomast.BQTerm {
	conc, ok := args.(*tomast.ConcBQTermBQTermList)
	if !ok {
		return tomast.MakeBuildEmptyArray(name,
			tomast.MakeExpressionToBQTerm(tomast.MakeInteger(0)),
		)
	}
	n := len(conc.Slots)
	result := tomast.MakeBuildEmptyArray(name,
		tomast.MakeExpressionToBQTerm(tomast.MakeInteger(int64(n))),
	)
	// Walk in source order so the FIRST arg ends up innermost.
	// For BQVariableStar args (X*), use BuildAppendArray (flatten);
	// otherwise BuildConsArray (single element).
	for i := 0; i < n; i++ {
		head := conc.Slots[i]
		if isVarStarLike(head) {
			result = tomast.MakeBuildAppendArray(name, head, result)
		} else {
			result = tomast.MakeBuildConsArray(name, head, result)
		}
	}
	return result
}

// isVarStarLike returns true when a BQTerm should be flattened into
// the enclosing list/array — i.e. it represents a (sub)list rather
// than a single element. Java treats BQVariableStar and
// `Composite(CompositeBQTerm(BQVariableStar))` both as "always
// flattened".
func isVarStarLike(t tomast.BQTerm) bool {
	switch x := t.(type) {
	case *tomast.BQVariableStarBQTerm:
		return true
	case *tomast.CompositeBQTerm:
		for _, m := range x.Slots {
			if bm, ok := m.(*tomast.CompositeBQTermCompositeMember); ok {
				if _, isStar := bm.Term.(*tomast.BQVariableStarBQTerm); isStar {
					return true
				}
			}
		}
	}
	return false
}

// buildListBQTerm is the analogous helper for `%oplist` operators.
// Same algorithm, BuildConsList / BuildEmptyList nodes — but for
// lists, Java's `ASTFactory.buildList` does NOT reverse args (it
// recurses head-first), so the OUTERMOST cons holds the FIRST arg.
// This is the opposite of the array convention because `make_insert`
// on lists typically prepends (e.g. ATermList.insert) and SequenceId/
// Sequence's `make(first, then)` binary node treats first/then as
// the constructor's left/right children.
func buildListBQTerm(name tomast.TomName, args tomast.BQTermList) tomast.BQTerm {
	conc, ok := args.(*tomast.ConcBQTermBQTermList)
	if !ok {
		return tomast.MakeBuildEmptyList(name)
	}
	n := len(conc.Slots)
	result := tomast.MakeBuildEmptyList(name)
	// Walk from the LAST arg backwards so the FIRST arg ends up at
	// the outermost cons.
	for i := n - 1; i >= 0; i-- {
		head := conc.Slots[i]
		if isVarStarLike(head) {
			result = tomast.MakeBuildAppendList(name, head, result)
		} else {
			result = tomast.MakeBuildConsList(name, head, result)
		}
	}
	return result
}

// isArrayOperator returns true when the symbol carries a
// MakeEmptyArray / MakeAddArray hook (the markers Java's
// TomBase.isArrayOperator looks for).
func (n *newKernelTyper) isArrayOperator(sym tomast.TomSymbol) bool {
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

// lookupSymbol fetches a symbol by name from the typer's symbol
// table. Returns (sym, true) when found.
func (n *newKernelTyper) lookupSymbol(name string) (tomast.TomSymbol, bool) {
	if n.symbols == nil {
		return nil, false
	}
	s, ok := n.symbols.Symbols[name]
	return s, ok
}

// codomainOfSymbol returns the codomain TomType of a symbol, or nil
// if the symbol's shape is unexpected.
func (n *newKernelTyper) codomainOfSymbol(sym tomast.TomSymbol) tomast.TomType {
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

// isListOperator is the mirror for `%oplist`.
func (n *newKernelTyper) isListOperator(sym tomast.TomSymbol) bool {
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
