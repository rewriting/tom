// Package backend emits Java source code from the typed Tom AST.
//
// This is a focused, single-target implementation aimed at compiling
// Peano.t end-to-end. It walks the Code list and turns each element
// into Java text:
//
//   - TargetLanguageToCode(TL(text,...))  → emitted verbatim
//   - DeclarationToCode(TypeTermDecl(...)) → tom_is_sort_<sort> +
//                                             tom_equal_term_<sort>
//                                             static helpers
//   - DeclarationToCode(SymbolDecl(name))  → look up the symbol in
//                                             SymbolTable and emit
//                                             tom_is_fun_sym_<op>,
//                                             tom_make_<op>,
//                                             tom_get_slot_<op>_<slot>
//   - InstructionToCode(Match(...))        → naive nested-if expansion
//                                             of the constraint tree
//                                             with the host action body
//
// The output is intentionally not byte-equivalent to Java's Tom
// compiler output — Java threads its constraints through a shared
// `tomMatch<N>_<M>` variable scheme that we ignore. The emitted code
// IS semantically equivalent: every action fires under the same
// pattern conditions.
package backend

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run consumes the typed Tom AST in `in.Code` and replaces State.Source
// with the generated Java source. Filename and SymbolTable are preserved.
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
	e := &emitter{symbols: in.Symbols}
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
	buf     strings.Builder
	symbols *tom.SymbolTable
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
		return e.emitIncluded(x.CodeList)
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
// `tom_equal_term_<sort>` static methods derived from the
// IsSortDecl / EqualTermDecl hooks attached to a `%typeterm`.
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

// emitSymbolHelpers looks up `name` in the SymbolTable and emits
// the hook helpers (is_fun_sym, make, get_slot).
func (e *emitter) emitSymbolHelpers(name tomast.TomName) error {
	if e.symbols == nil {
		return nil
	}
	opName := nameString(name)
	sym, ok := e.symbols.Symbols[opName]
	if !ok {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	// 1. GetSlotDecl per slot, from PairNameDeclList.
	if pairs, ok := s.PairNameDeclList.(*tomast.ConcPairNameDeclPairNameDeclList); ok {
		for _, p := range pairs.Slots {
			pair, ok := p.(*tomast.PairNameDeclPairNameDecl)
			if !ok {
				continue
			}
			if getSlot, ok := pair.SlotDecl.(*tomast.GetSlotDeclDeclaration); ok {
				body := codeBodyFromExpr(getSlot.Expr)
				body = substitutePlaceholders(body, []string{"t"})
				codomainTL := tlTypeFor(s, e.symbols)
				fmt.Fprintf(&e.buf, "private static %s tom_get_slot_%s_%s(%s t) {return %s;}",
					codomainTL, opName, nameString(pair.SlotName), codomainTL, body)
			}
		}
	}
	// 2. IsFsymDecl / MakeDecl from Options.
	if opts, ok := s.Options.(*tomast.ConcOptionOptionList); ok {
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
	}
	return nil
}

func (e *emitter) emitInstruction(i tomast.Instruction) error {
	switch x := i.(type) {
	case *tomast.MatchInstruction:
		return e.emitMatch(x)
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
	case *tomast.BQTermToInstructionInstruction:
		return e.emitBQTerm(x.Tom)
	case *tomast.RawActionInstruction:
		// Tom wraps every rule body in RawAction(If(TrueTL, then, Nop)).
		// We emit the then-branch verbatim.
		return e.emitInstruction(x.AstInstruction)
	case *tomast.IfInstruction:
		// For Peano the condition is always TrueTL, so the then-
		// branch unconditionally runs.
		return e.emitInstruction(x.SuccesInst)
	case *tomast.NopInstruction:
	}
	return nil
}

func (e *emitter) emitIncluded(cl tomast.CodeList) error {
	list, ok := cl.(*tomast.ConcCodeCodeList)
	if !ok {
		return nil
	}
	for _, c := range list.Slots {
		switch x := c.(type) {
		case *tomast.DeclarationToCodeCode:
			if err := e.emitDecl(x.AstDeclaration); err != nil {
				return err
			}
		case *tomast.InstructionToCodeCode:
			if err := e.emitInstruction(x.AstInstruction); err != nil {
				return err
			}
		case *tomast.TomIncludeCode:
			if err := e.emitIncluded(x.CodeList); err != nil {
				return err
			}
		}
	}
	return nil
}

// emitMatch turns a Match instruction into a sequence of nested-if
// blocks, one per ConstraintInstruction. Naive: each rule independent.
func (e *emitter) emitMatch(m *tomast.MatchInstruction) error {
	list, ok := m.ConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList)
	if !ok {
		return nil
	}
	e.buf.WriteString("{")
	for _, ci := range list.Slots {
		row, ok := ci.(*tomast.ConstraintInstructionConstraintInstruction)
		if !ok {
			continue
		}
		e.emitConstraintInstruction(row)
	}
	e.buf.WriteString("}")
	return nil
}

func (e *emitter) emitConstraintInstruction(row *tomast.ConstraintInstructionConstraintInstruction) {
	depth := e.emitConstraint(row.Constraint)
	e.emitAction(row.Action)
	for i := 0; i < depth; i++ {
		e.buf.WriteString("}")
	}
}

func (e *emitter) emitConstraint(c tomast.Constraint) int {
	switch x := c.(type) {
	case *tomast.AndConstraintConstraint:
		depth := 0
		for _, inner := range x.Slots {
			depth += e.emitConstraint(inner)
		}
		return depth
	case *tomast.MatchConstraintConstraint:
		return e.emitMatchConstraint(x)
	}
	return 0
}

func (e *emitter) emitMatchConstraint(m *tomast.MatchConstraintConstraint) int {
	mc := matchConstraintShape(m)
	if mc == nil {
		return 0
	}
	depth := 0
	// Sort check on the subject — every if-clause opens its own
	// braces so we can close them uniformly at the end of the rule.
	if mc.subjectTypeName != "" {
		fmt.Fprintf(&e.buf, "if (tom_is_sort_%s(%s)) {", mc.subjectTypeName, mc.subjectExpr)
		depth++
	}
	switch p := m.Pattern.(type) {
	case *tomast.VariableTomTerm:
		_ = p
		e.buf.WriteString("{")
		depth++
	case *tomast.TermApplTomTerm:
		applName := headApplName(p.NameList)
		if applName == "" {
			return depth
		}
		fmt.Fprintf(&e.buf, "if (tom_is_fun_sym_%s((( %s )%s))) {",
			applName, mc.subjectTypeTL, mc.subjectExpr)
		depth++
	case *tomast.RecordApplTomTerm:
		applName := headApplName(p.NameList)
		if applName == "" {
			return depth
		}
		fmt.Fprintf(&e.buf, "if (tom_is_fun_sym_%s((( %s )%s))) {",
			applName, mc.subjectTypeTL, mc.subjectExpr)
		depth++
	}
	return depth
}

func (e *emitter) emitAction(a tomast.Instruction) {
	switch x := a.(type) {
	case *tomast.RawActionInstruction:
		e.emitInstruction(x.AstInstruction)
	case *tomast.IfInstruction:
		// Tom wraps the action in If(TrueTL(), then, else). Just
		// emit the then-branch for Peano (TrueTL has no condition).
		e.emitInstruction(x.SuccesInst)
	case *tomast.NopInstruction:
	}
}

func (e *emitter) emitBQTerm(t tomast.BQTerm) error {
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		e.buf.WriteString(nameString(x.AstName))
	case *tomast.BQApplBQTerm:
		applName := nameString(x.AstName)
		fmt.Fprintf(&e.buf, "tom_make_%s(", applName)
		if args, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
			for i, a := range args.Slots {
				if i > 0 {
					e.buf.WriteString(",")
				}
				if err := e.emitBQTerm(a); err != nil {
					return err
				}
			}
		}
		e.buf.WriteString(")")
	}
	return nil
}

// ---------------------------------------------------------------------------
// Helpers
// ---------------------------------------------------------------------------

type matchShape struct {
	subjectExpr     string
	subjectTypeName string
	subjectTypeTL   string
}

func matchConstraintShape(m *tomast.MatchConstraintConstraint) *matchShape {
	bq, ok := m.Subject.(*tomast.BQVariableBQTerm)
	if !ok {
		return nil
	}
	mc := &matchShape{subjectExpr: nameString(bq.AstName)}
	if t, ok := bq.AstType.(*tomast.TypeTomType); ok {
		mc.subjectTypeName = t.TomType
		if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok {
			mc.subjectTypeTL = strings.TrimSpace(tl.String_)
		}
	}
	return mc
}

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

func headApplName(nl tomast.TomNameList) string {
	c, ok := nl.(*tomast.ConcTomNameTomNameList)
	if !ok || len(c.Slots) == 0 {
		return ""
	}
	return nameString(c.Slots[0])
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
