// Package backend renders a compiled Tom AST to host-language
// (currently Java) source text.
//
// The input is the AST that comes out of [tom/compiler]: it has
// been stripped of `Match` / `BQTermToInstruction` / `RawAction`
// nodes (the compiler replaced them with `If(Code(...), then,
// Nop())` chains plus inlined `TL` chunks). All this package does
// is a mechanical tree walk:
//
//   TargetLanguageToCode(TL(text,...))   → text (verbatim)
//   DeclarationToCode(TypeTermDecl)      → tom_is_sort / tom_equal helpers
//   DeclarationToCode(SymbolDecl(name))  → tom_is_fun_sym / tom_make /
//                                           tom_get_slot helpers (filtered
//                                           through `usedSymbols`)
//   InstructionToCode(If(Code, t, Nop))  → `if (<cond>) { <then> }`
//   InstructionToCode(AbstractBlock([…]))→ `{ <items> }`
//   InstructionToCode(Nop)               → ``
//
// Switching the backend to Go later is a matter of writing the
// equivalents of `emitTypeTermHelpers`, `emitSymbolHelpers`, and
// the host-language-specific text in the If/AbstractBlock writer —
// the compiled AST itself is target-agnostic.
package backend

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run consumes the compiled AST in `in.Code` and writes the
// generated host-language source into `in.Source`.
func Run(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	code, ok := in.Code.(*tomast.TomCode)
	if !ok {
		return in, fmt.Errorf("backend: expected *TomCode, got %T", in.Code)
	}
	list, ok := code.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return in, fmt.Errorf("backend: expected ConcCode list")
	}
	e := &emitter{symbols: in.Symbols, usedSymbols: collectUsedSymbols(list)}
	for _, c := range list.Slots {
		if err := e.emitCode(c); err != nil {
			return in, err
		}
	}
	out := in
	out.Source = []byte(e.buf.String())
	return out, nil
}

type emitter struct {
	buf         strings.Builder
	symbols     *tom.SymbolTable
	usedSymbols map[string]bool
}

func (e *emitter) emitCode(c tomast.Code) error {
	switch x := c.(type) {
	case *tomast.TargetLanguageToCodeCode:
		return e.emitTL(x.Tl)
	case *tomast.DeclarationToCodeCode:
		return e.emitDecl(x.AstDeclaration)
	case *tomast.InstructionToCodeCode:
		return e.emitInstruction(x.AstInstruction)
	case *tomast.TomIncludeCode:
		if cl, ok := x.CodeList.(*tomast.ConcCodeCodeList); ok {
			for _, inner := range cl.Slots {
				if err := e.emitCode(inner); err != nil {
					return err
				}
			}
		}
	}
	return nil
}

func (e *emitter) emitTL(t tomast.TargetLanguage) error {
	switch x := t.(type) {
	case *tomast.TLTargetLanguage:
		e.buf.WriteString(x.Code)
	case *tomast.ITLTargetLanguage:
		e.buf.WriteString(x.Code)
	}
	return nil
}

func (e *emitter) emitDecl(d tomast.Declaration) error {
	switch x := d.(type) {
	case *tomast.TypeTermDeclDeclaration:
		return e.emitTypeTermHelpers(x)
	case *tomast.SymbolDeclDeclaration:
		return e.emitSymbolHelpers(x.AstName)
	}
	return nil
}

// emitTypeTermHelpers writes `tom_is_sort_<sort>` and
// `tom_equal_term_<sort>` static helpers from the IsSortDecl /
// EqualTermDecl hooks attached to a `%typeterm`.
func (e *emitter) emitTypeTermHelpers(t *tomast.TypeTermDeclDeclaration) error {
	sortName := nameString(t.AstName)
	decls := flattenDeclList(t.Declarations)
	for _, d := range decls {
		switch decl := d.(type) {
		case *tomast.EqualTermDeclDeclaration:
			body := codeBodyFromExpr(decl.Expr)
			body = substitutePlaceholders(body, []string{"t1", "t2"})
			fmt.Fprintf(&e.buf, "private static boolean tom_equal_term_%s(Object t1, Object t2) {return %s;}",
				sortName, body)
		case *tomast.IsSortDeclDeclaration:
			body := codeBodyFromExpr(decl.Expr)
			body = substitutePlaceholders(body, []string{"t"})
			fmt.Fprintf(&e.buf, "private static boolean tom_is_sort_%s(Object t) {return %s;}",
				sortName, body)
		}
	}
	return nil
}

// emitSymbolHelpers writes per-operator helper methods (only for
// symbols actually used by patterns/backquotes — dead-helper
// elimination mirrors Java's reference).
func (e *emitter) emitSymbolHelpers(name tomast.TomName) error {
	if e.symbols == nil {
		return nil
	}
	opName := nameString(name)
	if !e.usedSymbols[opName] {
		return nil
	}
	sym, ok := e.symbols.Symbols[opName]
	if !ok {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	opts, ok := s.Options.(*tomast.ConcOptionOptionList)
	if !ok {
		return nil
	}
	for _, opt := range opts.Slots {
		declOpt, ok := opt.(*tomast.DeclarationToOptionOption)
		if !ok {
			continue
		}
		switch decl := declOpt.AstDeclaration.(type) {
		case *tomast.IsFsymDeclDeclaration:
			body := codeBodyFromExpr(decl.Expr)
			body = substitutePlaceholders(body, []string{"t"})
			codomainTL := tlTypeFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "private static boolean tom_is_fun_sym_%s(%s t) {return %s;}",
				opName, codomainTL, body)
		case *tomast.MakeDeclDeclaration:
			body := codeBodyFromInstruction(decl.Instr)
			codomainTL := tlTypeFor(s, e.symbols)
			params, names := makeDeclParams(decl, e.symbols)
			body = substitutePlaceholders(body, names)
			fmt.Fprintf(&e.buf, "private static %s tom_make_%s(%s) { return %s;}",
				codomainTL, opName, params, body)
		case *tomast.GetSlotDeclDeclaration:
			body := codeBodyFromExpr(decl.Expr)
			body = substitutePlaceholders(body, []string{"t"})
			codomainTL := tlTypeFor(s, e.symbols)
			slotName := nameString(decl.SlotName)
			fmt.Fprintf(&e.buf, "private static %s tom_get_slot_%s_%s(%s t) {return %s;}",
				codomainTL, opName, slotName, codomainTL, body)
		}
	}
	return nil
}

// emitInstruction dispatches on the post-compile instruction shapes.
// Match / BQTermToInstruction / RawAction have all been removed by
// the Compiler; if we encounter them here it's a bug upstream and we
// surface that as a fmt directive in the output (which will fail
// `javac` and pinpoint the missing case).
func (e *emitter) emitInstruction(i tomast.Instruction) error {
	switch x := i.(type) {
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
	case *tomast.CodeToInstructionInstruction:
		return e.emitCode(x.Code)
	case *tomast.IfInstruction:
		// `if (<cond>) { <then> } else { <else> }` — but if the
		// else branch is Nop the brace pair would compile cleanly
		// either way, so we emit `else { … }` only when there's
		// real content.
		cond := codeBodyFromExpr(x.Condition)
		fmt.Fprintf(&e.buf, "if (%s) {", cond)
		if err := e.emitInstruction(x.SuccesInst); err != nil {
			return err
		}
		e.buf.WriteString("}")
		if _, nop := x.FailureInst.(*tomast.NopInstruction); !nop {
			e.buf.WriteString(" else {")
			if err := e.emitInstruction(x.FailureInst); err != nil {
				return err
			}
			e.buf.WriteString("}")
		}
	case *tomast.NopInstruction:
		// Emit nothing.
	default:
		// Unknown node — surface as a comment so the bug is visible
		// in the generated Java without breaking javac.
		fmt.Fprintf(&e.buf, "/* backend: unsupported instruction %T */", x)
	}
	return nil
}

// ---------------------------------------------------------------------------
// Used-symbol pre-pass (dead-helper elimination)
// ---------------------------------------------------------------------------

// collectUsedSymbols walks the AST once before emission, recording
// every operator name that appears as the head of an If condition
// (matching `tom_is_fun_sym_<op>`, `tom_get_slot_<op>_<slot>`,
// `tom_make_<op>(...)`) inside Code expressions or TL chunks. Only
// symbols in this set get their helpers emitted, mirroring Java's
// dead-code elimination.
func collectUsedSymbols(list *tomast.ConcCodeCodeList) map[string]bool {
	used := map[string]bool{}
	var walkCode func(c tomast.Code)
	var walkInstr func(i tomast.Instruction)
	walkCode = func(c tomast.Code) {
		switch x := c.(type) {
		case *tomast.InstructionToCodeCode:
			walkInstr(x.AstInstruction)
		case *tomast.TargetLanguageToCodeCode:
			if tl, ok := x.Tl.(*tomast.TLTargetLanguage); ok {
				scanReferences(tl.Code, used)
			} else if itl, ok := x.Tl.(*tomast.ITLTargetLanguage); ok {
				scanReferences(itl.Code, used)
			}
		case *tomast.TomIncludeCode:
			if cl, ok := x.CodeList.(*tomast.ConcCodeCodeList); ok {
				for _, c2 := range cl.Slots {
					walkCode(c2)
				}
			}
		}
	}
	walkInstr = func(i tomast.Instruction) {
		switch x := i.(type) {
		case *tomast.AbstractBlockInstruction:
			if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, inner := range list.Slots {
					walkInstr(inner)
				}
			}
		case *tomast.CodeToInstructionInstruction:
			walkCode(x.Code)
		case *tomast.IfInstruction:
			if c, ok := x.Condition.(*tomast.CodeExpression); ok {
				scanReferences(c.Code, used)
			}
			walkInstr(x.SuccesInst)
			walkInstr(x.FailureInst)
		}
	}
	for _, c := range list.Slots {
		walkCode(c)
	}
	delete(used, "")
	return used
}

// scanReferences finds `tom_is_fun_sym_<op>`, `tom_get_slot_<op>_…`,
// and `tom_make_<op>` substrings in `text` and adds `<op>` to `used`.
// We're parsing the synthetic helper calls the compiler emitted into
// TL/Code chunks, so the names are stable identifiers in those
// chunks.
func scanReferences(text string, used map[string]bool) {
	for _, prefix := range []string{"tom_is_fun_sym_", "tom_make_", "tom_get_slot_"} {
		i := 0
		for {
			idx := strings.Index(text[i:], prefix)
			if idx < 0 {
				break
			}
			start := i + idx + len(prefix)
			end := start
			for end < len(text) && isIdentByte(text[end]) {
				end++
			}
			name := text[start:end]
			if prefix == "tom_get_slot_" {
				// `tom_get_slot_<op>_<slotName>` — strip trailing
				// `_<slot>` so we record just `<op>`. We don't
				// know which `_` is the separator, so just take
				// what comes before the LAST underscore.
				if u := strings.LastIndex(name, "_"); u > 0 {
					name = name[:u]
				}
			}
			used[name] = true
			i = end
		}
	}
}

func isIdentByte(c byte) bool {
	return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'
}

// ---------------------------------------------------------------------------
// Helpers (shared shape with compiler — duplicated to avoid coupling)
// ---------------------------------------------------------------------------

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

func tlTypeFor(s *tomast.SymbolTomSymbol, st *tom.SymbolTable) string {
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return "Object"
	}
	cod, ok := tt.Codomain.(*tomast.TypeTomType)
	if !ok {
		return "Object"
	}
	if tl, ok := cod.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
		return strings.TrimSpace(tl.String_)
	}
	if st != nil {
		if body, ok := st.Sorts[cod.TomType]; ok && strings.TrimSpace(body) != "" {
			return strings.TrimSpace(body)
		}
	}
	return "Object"
}

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
		tl := "Object"
		if t, ok := v.AstType.(*tomast.TypeTomType); ok {
			if tlt, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tlt.String_) != "" {
				tl = strings.TrimSpace(tlt.String_)
			} else if st != nil {
				if body, ok := st.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
					tl = strings.TrimSpace(body)
				}
			}
		}
		parts = append(parts, tl+" "+nm)
	}
	return strings.Join(parts, ","), names
}
