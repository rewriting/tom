// backend_go — language-agnostic helpers shared with the Java
// backend. Most are direct copies — keeping them duplicated lets us
// evolve each backend independently without coupling.

package backend_go

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

func flattenDeclList(d tomast.DeclarationList) []tomast.Declaration {
	c, ok := d.(*tomast.ConcDeclarationDeclarationList)
	if !ok {
		return nil
	}
	return c.Slots
}

func nameString(n tomast.TomName) string {
	if x, ok := n.(*tomast.NameTomName); ok {
		return x.String_
	}
	return ""
}

func codeBodyFromExpr(e tomast.Expression) string {
	if c, ok := e.(*tomast.CodeExpression); ok {
		return c.Code
	}
	return ""
}

func codeBodyFromInstruction(i tomast.Instruction) string {
	if e, ok := i.(*tomast.ExpressionToInstructionInstruction); ok {
		return codeBodyFromExpr(e.Expr)
	}
	return ""
}

func substitutePlaceholders(body string, names []string) string {
	for i, n := range names {
		body = strings.ReplaceAll(body, fmt.Sprintf("{%d}", i), n)
	}
	return body
}

// slotTLTypeFor returns the target-language type of slot `slotName`
// on symbol `s`. Walks `TypesToType.Domain` parallel to
// `PairNameDeclList` — same logic as the Java backend.
func slotTLTypeFor(s *tomast.SymbolTomSymbol, slotName string, st *tom.SymbolTable) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return ""
	}
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok {
		return ""
	}
	pairs, ok := s.PairNameDeclList.(*tomast.ConcPairNameDeclPairNameDeclList)
	if !ok {
		return ""
	}
	for i, p := range pairs.Slots {
		pair, ok := p.(*tomast.PairNameDeclPairNameDecl)
		if !ok || nameString(pair.SlotName) != slotName {
			continue
		}
		if i >= len(domain.Slots) {
			return ""
		}
		t, ok := domain.Slots[i].(*tomast.TypeTomType)
		if !ok {
			return ""
		}
		if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
			return strings.TrimSpace(tl.String_)
		}
		if st != nil {
			if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
				return strings.TrimSpace(body)
			}
		}
		return ""
	}
	return ""
}

func tlTypeFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return "any"
	}
	cod, ok := tt.Codomain.(*tomast.TypeTomType)
	if !ok {
		return "any"
	}
	if tl, ok := cod.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
		return strings.TrimSpace(tl.String_)
	}
	if st != nil {
		if body, ok := st.Sorts[cod.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return "any"
}

// makeDeclParams formats the `make` hook's BQVariable arg list as
// a Go-syntax parameter list (e.g. `p *Term`) and returns the bare
// names for placeholder substitution.
func makeDeclParams(d *tomast.MakeDeclDeclaration, st *tom.SymbolTable) (string, []string) {
	args, ok := d.Args.(*tomast.ConcBQTermBQTermList)
	if !ok || len(args.Slots) == 0 {
		return "", nil
	}
	var parts []string
	var names []string
	for _, a := range args.Slots {
		v, ok := a.(*tomast.BQVariableBQTerm)
		if !ok {
			continue
		}
		nm := nameString(v.AstName)
		names = append(names, nm)
		tl := "any"
		if t, ok := v.AstType.(*tomast.TypeTomType); ok {
			if tlt, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tlt.String_) != "" {
				tl = strings.TrimSpace(tlt.String_)
			} else if st != nil {
				if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
					tl = strings.TrimSpace(body)
				}
			}
		}
		// Go param syntax: `<name> <type>` (reverse of Java).
		parts = append(parts, nm+" "+tl)
	}
	return strings.Join(parts, ","), names
}

// tlTypeOfBQTerm returns the TL of a BQVariable / BQVariableStar.
// Falls back to "any" when the type isn't resolvable.
func tlTypeOfBQTerm(t tomast.BQTerm, st *tom.SymbolTable) string {
	var ttype tomast.TomType
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		ttype = x.AstType
	case *tomast.BQVariableStarBQTerm:
		ttype = x.AstType
	default:
		return "any"
	}
	return tomTypeTL(ttype, st)
}

func tomTypeTL(t tomast.TomType, st *tom.SymbolTable) string {
	tt, ok := t.(*tomast.TypeTomType)
	if !ok {
		return "any"
	}
	if tl, ok := tt.TlType.(*tomast.TLTypeTargetLanguageType); ok {
		if body := strings.TrimSpace(tl.String_); body != "" {
			return body
		}
	}
	if st != nil {
		if body, ok := st.Sorts[tt.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return "any"
}

func tomTypeSortName(t tomast.TomType) string {
	if tt, ok := t.(*tomast.TypeTomType); ok {
		return tt.TomType
	}
	return ""
}

// bqTermName extracts the AstName of a BQVariable / BQVariableStar /
// FunctionCall / BuildTerm / BuildConstant.
func bqTermName(t tomast.BQTerm) tomast.TomName {
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		return x.AstName
	case *tomast.BQVariableStarBQTerm:
		return x.AstName
	case *tomast.FunctionCallBQTerm:
		return x.AstName
	case *tomast.BuildTermBQTerm:
		return x.AstName
	case *tomast.BuildConstantBQTerm:
		return x.AstName
	}
	return tomast.MakeEmptyName()
}

// renderTomName converts a TomName to its host-identifier form.
// Identical to the Java backend's version — host-language-agnostic.
func renderTomName(n tomast.TomName) string {
	switch x := n.(type) {
	case *tomast.NameTomName:
		return x.String_
	case *tomast.EmptyNameTomName:
		return ""
	case *tomast.PositionNameTomName:
		var sb strings.Builder
		if list, ok := x.NumberList.(*tomast.ConcTomNumberTomNumberList); ok {
			for _, num := range list.Slots {
				sb.WriteString(renderTomNumber(num))
			}
		}
		return sb.String()
	}
	return ""
}

// codomainSortFor returns the sort name (not the TL) of an
// operator's codomain. Used to suffix variadic helper names like
// `tom_get_size_conc_L`, `tom_get_element_conc_L`.
func codomainSortFor(st *tom.SymbolTable, opName string) string {
	if st == nil {
		return ""
	}
	sym, ok := st.Symbols[opName]
	if !ok {
		return ""
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return ""
	}
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return ""
	}
	cod, ok := tt.Codomain.(*tomast.TypeTomType)
	if !ok {
		return ""
	}
	return cod.TomType
}

// elemTLFor returns the TL of an `%oparray` / `%oplist` operator's
// single element-sort entry. Falls back to the codomain TL when
// unresolvable. Used to type the element parameter of helpers like
// `tom_cons_array_conc`.
func elemTLFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return tlTypeFor(s, st)
	}
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok || len(domain.Slots) == 0 {
		return tlTypeFor(s, st)
	}
	t, ok := domain.Slots[0].(*tomast.TypeTomType)
	if !ok {
		return tlTypeFor(s, st)
	}
	if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
		return strings.TrimSpace(tl.String_)
	}
	if st != nil {
		if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return tlTypeFor(s, st)
}

// codomainSortName returns the codomain sort name of `s`. Same as
// codomainSortFor but takes a *SymbolTomSymbol directly.
func codomainSortName(s *tomast.SymbolTomSymbol) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return ""
	}
	cod, ok := tt.Codomain.(*tomast.TypeTomType)
	if !ok {
		return ""
	}
	return cod.TomType
}

// bqVarName extracts the variable name from a BQVariable /
// BQVariableStar passed as a `%op` hook parameter. Returns "" for
// unexpected shapes.
func bqVarName(t tomast.BQTerm) string {
	if v, ok := t.(*tomast.BQVariableBQTerm); ok {
		return nameString(v.AstName)
	}
	if v, ok := t.(*tomast.BQVariableStarBQTerm); ok {
		return nameString(v.AstName)
	}
	return ""
}

func renderTomNumber(n tomast.TomNumber) string {
	switch x := n.(type) {
	case *tomast.MatchNumberTomNumber:
		return fmt.Sprintf("tomMatch%d", x.Integer)
	case *tomast.PatternNumberTomNumber:
		return fmt.Sprintf("_pattern%d", x.Integer)
	case *tomast.NameNumberTomNumber:
		return renderTomName(x.AstName)
	}
	return ""
}
