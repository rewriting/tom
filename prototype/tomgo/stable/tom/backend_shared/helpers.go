// Package backend_shared collects the language-agnostic helpers
// that both backend_java and backend_go rely on:
//
//   - AST extractors / queries (nameString, codeBody*, codomainSort*,
//     elemTL*, slotTLType*, bqTermName, …).
//   - Identifier rendering (renderTomName, renderTomNumber) — both
//     backends agree on the `tomMatch<N>_<suffix>` format that Java's
//     reference PrettyPrinter emits.
//   - Text substitution helpers (substituteDollarVars,
//     substitutePositional).
//   - Used-symbol collection (CollectUsedSymbols, ScanReferences) —
//     parameterised by the helper-prefix list so each backend can
//     declare exactly the function-name shapes it emits.
//
// What's NOT here: the actual syntax of helper declarations
// (`private static …` vs `func …`), control-flow keywords
// (`if (cond)` vs `if cond`), default value words (`null` vs
// `nil`), or the per-IR-shape rendering. Those live in each
// backend's own files.
package backend_shared

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// FlattenDeclList unwraps a DeclarationList into its Slot list.
func FlattenDeclList(d tomast.DeclarationList) []tomast.Declaration {
	c, ok := d.(*tomast.ConcDeclarationDeclarationList)
	if !ok {
		return nil
	}
	return c.Slots
}

// NameString returns the `s` of a `Name(s)` TomName, or "" for
// other shapes (EmptyName, PositionName, …).
func NameString(n tomast.TomName) string {
	if x, ok := n.(*tomast.NameTomName); ok {
		return x.String_
	}
	return ""
}

// CodeBodyFromExpr extracts the raw text of a `Code(text)` Expression.
func CodeBodyFromExpr(e tomast.Expression) string {
	if c, ok := e.(*tomast.CodeExpression); ok {
		return c.Code
	}
	return ""
}

// CodeBodyFromInstruction is the same for an
// `ExpressionToInstruction(Code(text))` wrapper.
func CodeBodyFromInstruction(i tomast.Instruction) string {
	if e, ok := i.(*tomast.ExpressionToInstructionInstruction); ok {
		return CodeBodyFromExpr(e.Expr)
	}
	return ""
}

// SubstitutePlaceholders replaces `{0}`, `{1}`, … with the matching
// `names[i]`. Used by the make-hook body emitter for the
// `abstractCode`-style positional form.
func SubstitutePlaceholders(body string, names []string) string {
	for i, n := range names {
		body = strings.ReplaceAll(body, fmt.Sprintf("{%d}", i), n)
	}
	return body
}

// SubstituteDollarVars accepts both `$<argname>` (raw form the
// parser keeps when the make body has no backquote) AND `{N}`
// (positional form `abstractCode` produces for other hooks). The
// two coexist depending on the hook kind — this helper handles
// both uniformly.
func SubstituteDollarVars(text string, names []string) string {
	for i, name := range names {
		text = strings.ReplaceAll(text, "$"+name, name)
		text = strings.ReplaceAll(text, fmt.Sprintf("{%d}", i), name)
	}
	return text
}

// SubstitutePositional replaces `{N}` placeholders with the matching
// slot value. Used when emitting the generic list/array helpers
// (`tom_get_slice_<op>` etc.) where the substitution targets are
// arbitrary inline expressions, not arg names.
func SubstitutePositional(text string, vals []string) string {
	for i, v := range vals {
		text = strings.ReplaceAll(text, fmt.Sprintf("{%d}", i), v)
	}
	return text
}

// SlotTLTypeFor returns the TL of slot `slotName` on symbol `s` by
// walking `TypesToType.Domain` parallel to `PairNameDeclList`.
// Returns "" when unresolvable; callers fall back to the codomain.
func SlotTLTypeFor(s *tomast.SymbolTomSymbol, slotName string, st *tom.SymbolTable, fallback string) string {
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
		if !ok || NameString(pair.SlotName) != slotName {
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

// TLTypeFor returns the codomain TL of `s` — its `implement` body
// for the codomain sort, or the embedded TLType if present.
// `fallback` is the value returned when nothing resolves (e.g.
// "Object" for Java, "any" for Go).
func TLTypeFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable, fallback string) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return fallback
	}
	cod, ok := tt.Codomain.(*tomast.TypeTomType)
	if !ok {
		return fallback
	}
	if tl, ok := cod.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
		return strings.TrimSpace(tl.String_)
	}
	if st != nil {
		if body, ok := st.Sorts[cod.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return fallback
}

// ElemTLFor returns the TL of the first Domain entry of `s` — the
// element type of an `%oparray` / `%oplist` operator. Falls back
// to the codomain TL.
func ElemTLFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable, fallback string) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return TLTypeFor(s, st, fallback)
	}
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok || len(domain.Slots) == 0 {
		return TLTypeFor(s, st, fallback)
	}
	t, ok := domain.Slots[0].(*tomast.TypeTomType)
	if !ok {
		return TLTypeFor(s, st, fallback)
	}
	if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
		return strings.TrimSpace(tl.String_)
	}
	if st != nil {
		if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return TLTypeFor(s, st, fallback)
}

// CodomainSortFor returns the codomain SORT NAME (not the TL) of
// `opName` — e.g. "L" for `conc`. Used to suffix variadic helper
// names like `tom_get_size_conc_L`.
func CodomainSortFor(st *tom.SymbolTable, opName string) string {
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
	return CodomainSortName(s)
}

// CodomainSortName is the *SymbolTomSymbol-taking variant of
// CodomainSortFor.
func CodomainSortName(s *tomast.SymbolTomSymbol) string {
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

// TomTypeSortName returns the bare sort name from a TomType (e.g.
// "term", "L", "appl"). Used by IsSort renderers.
func TomTypeSortName(t tomast.TomType) string {
	if tt, ok := t.(*tomast.TypeTomType); ok {
		return tt.TomType
	}
	return ""
}

// TomTypeTL converts a TomType to its host TL string. If TLType has
// content, use it; otherwise look up the sort name in `st.Sorts`.
// `fallback` covers the "unresolved" case ("Object" for Java, "any"
// for Go).
func TomTypeTL(t tomast.TomType, st *tom.SymbolTable, fallback string) string {
	tt, ok := t.(*tomast.TypeTomType)
	if !ok {
		return fallback
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
	return fallback
}

// TLTypeOfBQTerm returns the TL of a BQVariable / BQVariableStar's
// declared AstType. Falls back to `fallback` for other shapes.
func TLTypeOfBQTerm(t tomast.BQTerm, st *tom.SymbolTable, fallback string) string {
	var ttype tomast.TomType
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		ttype = x.AstType
	case *tomast.BQVariableStarBQTerm:
		ttype = x.AstType
	default:
		return fallback
	}
	return TomTypeTL(ttype, st, fallback)
}

// BqVarName extracts the variable name from a BQVariable /
// BQVariableStar (a `%op` hook parameter). Empty string for other
// shapes.
func BqVarName(t tomast.BQTerm) string {
	if v, ok := t.(*tomast.BQVariableBQTerm); ok {
		return NameString(v.AstName)
	}
	if v, ok := t.(*tomast.BQVariableStarBQTerm); ok {
		return NameString(v.AstName)
	}
	return ""
}

// BqTermName extracts the AstName from any BQTerm shape that carries
// one (BQVariable / BQVariableStar / FunctionCall / BuildTerm /
// BuildConstant). Returns EmptyName for shapes without a name.
func BqTermName(t tomast.BQTerm) tomast.TomName {
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

// RenderTomName converts a TomName to its host-identifier form.
//
//	Name(s)               → s
//	EmptyName             → ""
//	PositionName(numbers) → joined parts (MatchNumber(N) →
//	                        "tomMatchN", NameNumber(Name(s)) → s)
//
// Same convention as Java's reference PrettyPrinter — both
// backends agree.
func RenderTomName(n tomast.TomName) string {
	switch x := n.(type) {
	case *tomast.NameTomName:
		return x.String_
	case *tomast.EmptyNameTomName:
		return ""
	case *tomast.PositionNameTomName:
		var sb strings.Builder
		if list, ok := x.NumberList.(*tomast.ConcTomNumberTomNumberList); ok {
			for _, num := range list.Slots {
				sb.WriteString(RenderTomNumber(num))
			}
		}
		return sb.String()
	case *tomast.AntiNameTomName:
		return "!" + RenderTomName(x.Name)
	}
	return ""
}

// RenderTomNumber renders one TomNumber to its host-identifier
// fragment.
func RenderTomNumber(n tomast.TomNumber) string {
	switch x := n.(type) {
	case *tomast.MatchNumberTomNumber:
		return fmt.Sprintf("tomMatch%d", x.Integer)
	case *tomast.PatternNumberTomNumber:
		return fmt.Sprintf("_pattern%d", x.Integer)
	case *tomast.NameNumberTomNumber:
		return RenderTomName(x.AstName)
	case *tomast.IndexNumberTomNumber:
		return fmt.Sprintf("_%d", x.Integer)
	case *tomast.ListNumberTomNumber:
		return fmt.Sprintf("_list%d", x.Integer)
	case *tomast.BeginTomNumber:
		return fmt.Sprintf("_begin_%d", x.Integer)
	case *tomast.EndTomNumber:
		return fmt.Sprintf("_end_%d", x.Integer)
	case *tomast.SaveTomNumber:
		return fmt.Sprintf("_save_%d", x.Integer)
	case *tomast.PositionTomNumber:
		return fmt.Sprintf("_pos_%d", x.Integer)
	case *tomast.AbsVarTomNumber:
		return fmt.Sprintf("_absvar%d", x.Integer)
	case *tomast.RenamedVarTomNumber:
		return RenderTomName(x.AstName)
	}
	return ""
}

// MakeDeclParams formats a `make` hook's BQVariable arg list. The
// `formatter` callback receives `(name, tl)` per arg and returns
// the per-arg syntax fragment (e.g. `Type name` for Java, `name
// Type` for Go). Returns the joined parameter list and the bare
// arg names (for later `$arg` substitution in the body).
func MakeDeclParams(d *tomast.MakeDeclDeclaration, st *tom.SymbolTable, fallback string, formatter func(name, tl string) string) (string, []string) {
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
		nm := NameString(v.AstName)
		names = append(names, nm)
		tl := fallback
		if t, ok := v.AstType.(*tomast.TypeTomType); ok {
			if tlt, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tlt.String_) != "" {
				tl = strings.TrimSpace(tlt.String_)
			} else if st != nil {
				if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
					tl = strings.TrimSpace(body)
				}
			}
		}
		parts = append(parts, formatter(nm, tl))
	}
	return strings.Join(parts, ","), names
}
