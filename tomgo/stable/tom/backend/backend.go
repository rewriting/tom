// Package backend emits Java source code from the typed Tom AST.
//
// Single-target implementation aimed at compiling Peano.t end-to-end.
// Walks the Code list and produces Java text:
//
//   - TargetLanguageToCode(TL(...))         verbatim
//   - DeclarationToCode(TypeTermDecl(...))  tom_is_sort_<sort>,
//                                            tom_equal_term_<sort>
//   - DeclarationToCode(SymbolDecl(name))   tom_is_fun_sym_<op>,
//                                            tom_make_<op>,
//                                            tom_get_slot_<op>_<slot>
//                                            (only for symbols USED
//                                            in patterns or backquotes —
//                                            mirrors Java's reference
//                                            which omits dead helpers)
//   - InstructionToCode(Match(...))         nested-if cascade over
//                                            constraints, with each
//                                            pattern variable bound
//                                            to a `cast/slot-call`
//                                            expression and inlined
//                                            into the action body
package backend

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run consumes the typed Tom AST in `in.Code` and replaces State.Source
// with the generated Java source.
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
	usedSymbols := collectUsedSymbols(list)
	e := &emitter{symbols: in.Symbols, usedSymbols: usedSymbols, bindings: map[string]string{}}
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
	// bindings maps a pattern-variable name (e.g. "x", "y") to the
	// Java expression that produces its matched value (e.g.
	// "(( ATerm )t1)" or "tom_get_slot_suc_pred((( ATerm )t2))").
	// Populated as a Match-rule's pattern tree is walked; consulted
	// when the rule's action body emits a BQVariable.
	bindings map[string]string
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

// emitSymbolHelpers writes the per-operator helper methods. To match
// Java's reference, only symbols actually USED by the rest of the
// compiled code get helpers — others (like `plus1`/`plus2`/`term2appl`
// in Peano, which are declared but never referenced in patterns or
// backquotes) are skipped to avoid emitting dead code whose body
// would also cause Java compile errors (e.g. a static helper calling
// an instance method).
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
	// All hooks live in Options (the Go parser doesn't populate
	// PairNameDecl.SlotDecl). Iterate and emit one helper per
	// recognised declaration variant.
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
		return e.emitInstruction(x.AstInstruction)
	case *tomast.IfInstruction:
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

// emitMatch turns a Match instruction into independent rules: each
// ConstraintInstruction expands into its own if-cascade, so a rule
// that doesn't fire falls through to the next one.
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
	// Fresh binding scope per rule — `x` from rule N must not leak
	// into rule N+1.
	e.bindings = map[string]string{}
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

// emitMatchConstraint handles `pattern << subject`. It opens
// `if (tom_is_sort_<type>(subject)) { ... }` then recurses on the
// pattern via emitPattern.
func (e *emitter) emitMatchConstraint(m *tomast.MatchConstraintConstraint) int {
	bq, ok := m.Subject.(*tomast.BQVariableBQTerm)
	if !ok {
		return 0
	}
	subjectName := nameString(bq.AstName)
	subjectTypeName := ""
	subjectTypeTL := "Object"
	if t, ok := bq.AstType.(*tomast.TypeTomType); ok {
		subjectTypeName = t.TomType
		if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
			subjectTypeTL = strings.TrimSpace(tl.String_)
		}
	}
	depth := 0
	if subjectTypeName != "" {
		fmt.Fprintf(&e.buf, "if (tom_is_sort_%s(%s)) {", subjectTypeName, subjectName)
		depth++
	}
	// The subject expression as used in pattern checks is the
	// cast form `(( <TL> )<subject>)` — Java's reference uses this
	// idiom everywhere it consumes the subject.
	subjectExpr := fmt.Sprintf("(( %s )%s)", subjectTypeTL, subjectName)
	depth += e.emitPattern(m.Pattern, subjectExpr, subjectTypeTL)
	return depth
}

// emitPattern walks one pattern. For each `if`-style check it opens
// a `{` and returns +1 to the depth count so the caller can close
// the right number of braces. Variable bindings are recorded in
// e.bindings so the rule's action body can substitute them inline.
func (e *emitter) emitPattern(p tomast.TomTerm, subjectExpr, subjectTypeTL string) int {
	switch pat := p.(type) {
	case *tomast.VariableTomTerm:
		return e.bindVariable(pat, subjectExpr)
	case *tomast.VariableStarTomTerm:
		// Star patterns (`x*`) don't show up in plain `%match` over
		// non-variadic ops. Treat like Variable for now.
		name := nameString(pat.AstName)
		if name != "" && !isFreshOrEmpty(name) {
			e.bindings[name] = subjectExpr
		}
		return 0
	case *tomast.TermApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return 0
		}
		fmt.Fprintf(&e.buf, "if (tom_is_fun_sym_%s(%s)) {", applName, subjectExpr)
		depth := 1
		// Positional args — slot names come from the SymbolTable.
		if args, ok := pat.Args.(*tomast.ConcTomTermTomList); ok && len(args.Slots) > 0 {
			slotNames := e.slotNamesFor(applName)
			for i, arg := range args.Slots {
				if i >= len(slotNames) {
					break
				}
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotNames[i], subjectExpr)
				// The inner subject's TL type is the slot's
				// declared type. For Peano all slots are `term`
				// (TL `ATerm`), and a downstream
				// tom_is_fun_sym_<X>(inner) re-checks the head.
				innerTL := e.slotTLType(applName, slotNames[i])
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				depth += e.emitPattern(arg, inner, innerTL)
			}
		}
		// Top-level annotation: `x@<pat>` carries x as an AliasTo
		// constraint on the OUTER pattern. Bind x to the WHOLE
		// matched subject (not the inner).
		e.applyAliasConstraints(pat.Constraints, subjectExpr)
		return depth
	case *tomast.RecordApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return 0
		}
		fmt.Fprintf(&e.buf, "if (tom_is_fun_sym_%s(%s)) {", applName, subjectExpr)
		depth := 1
		if slots, ok := pat.Slots.(*tomast.ConcSlotSlotList); ok {
			for _, sl := range slots.Slots {
				pair, ok := sl.(*tomast.PairSlotApplSlot)
				if !ok {
					continue
				}
				slotName := nameString(pair.SlotName)
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotName, subjectExpr)
				innerTL := e.slotTLType(applName, slotName)
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				if appl, ok := pair.Appl.(tomast.TomTerm); ok {
					depth += e.emitPattern(appl, inner, innerTL)
				}
			}
		}
		e.applyAliasConstraints(pat.Constraints, subjectExpr)
		return depth
	}
	return 0
}

// bindVariable handles a leaf Variable pattern. Variable's name may
// be a parser-emitted placeholder (Empty / _f_r_e_s_h_v_a_r_N from the
// desugarer) — in that case we look at its AliasTo constraint to find
// the source-level alias name `x` and bind THAT instead.
func (e *emitter) bindVariable(v *tomast.VariableTomTerm, subjectExpr string) int {
	name := nameString(v.AstName)
	if name != "" && !isFreshOrEmpty(name) {
		e.bindings[name] = subjectExpr
	}
	// Apply AliasTo constraints: `x@_` after desugar becomes
	// `Variable(_fresh_var_N, AliasTo(Variable(x)))`. Record x as
	// bound to the same subject so the action sees it.
	e.applyAliasConstraints(v.Constraints, subjectExpr)
	return 0
}

// applyAliasConstraints walks a Variable/TermAppl/RecordAppl's
// Constraints list looking for `AliasTo(Variable(name))` entries and
// binds `name` to the supplied expression.
func (e *emitter) applyAliasConstraints(cs tomast.ConstraintList, subjectExpr string) {
	c, ok := cs.(*tomast.ConcConstraintConstraintList)
	if !ok {
		return
	}
	for _, item := range c.Slots {
		alias, ok := item.(*tomast.AliasToConstraint)
		if !ok {
			continue
		}
		if v, ok := alias.Var.(*tomast.VariableTomTerm); ok {
			name := nameString(v.AstName)
			if name != "" && !isFreshOrEmpty(name) {
				e.bindings[name] = subjectExpr
			}
		}
	}
}

// slotNamesFor returns the slot names of op `name` in declaration
// order, from the SymbolTable. Empty if the op is unknown or has no
// pair-name list.
func (e *emitter) slotNamesFor(opName string) []string {
	if e.symbols == nil {
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
	pairs, ok := s.PairNameDeclList.(*tomast.ConcPairNameDeclPairNameDeclList)
	if !ok {
		return nil
	}
	var out []string
	for _, p := range pairs.Slots {
		pair, ok := p.(*tomast.PairNameDeclPairNameDecl)
		if !ok {
			continue
		}
		out = append(out, nameString(pair.SlotName))
	}
	return out
}

// slotTLType returns the target-language type of slot `slotName` on
// op `opName`. Used to inherit the correct cast type into nested
// patterns.
func (e *emitter) slotTLType(opName, slotName string) string {
	if e.symbols == nil {
		return ""
	}
	sym, ok := e.symbols.Symbols[opName]
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
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok {
		return ""
	}
	names := e.slotNamesFor(opName)
	for i, n := range names {
		if n != slotName {
			continue
		}
		if i >= len(domain.Slots) {
			break
		}
		if t, ok := domain.Slots[i].(*tomast.TypeTomType); ok {
			if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
				return strings.TrimSpace(tl.String_)
			}
			if e.symbols != nil {
				if body, ok := e.symbols.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
					return strings.TrimSpace(body)
				}
			}
		}
	}
	return ""
}

func (e *emitter) emitAction(a tomast.Instruction) {
	switch x := a.(type) {
	case *tomast.RawActionInstruction:
		e.emitInstruction(x.AstInstruction)
	case *tomast.IfInstruction:
		e.emitInstruction(x.SuccesInst)
	case *tomast.NopInstruction:
	}
}

// emitBQTerm renders a backquote term — used inside RawAction bodies
// to substitute pattern variables and synthesise calls to
// `tom_make_<op>` helpers.
func (e *emitter) emitBQTerm(t tomast.BQTerm) error {
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		name := nameString(x.AstName)
		if bound, ok := e.bindings[name]; ok {
			e.buf.WriteString(bound)
			return nil
		}
		e.buf.WriteString(name)
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
	case *tomast.BuildTermBQTerm:
		// After Typer / Desugarer, backquoted constructors become
		// BuildTerm(Name(op), args, "moduleName"). Emit the same
		// `tom_make_<op>(args)` shape.
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
// Pre-pass: which symbols does the compiled output actually reference?
// ---------------------------------------------------------------------------

// collectUsedSymbols walks the AST once before code emission, recording
// every symbol name that appears as the head of a pattern application
// (TermAppl / RecordAppl) or a backquoted constructor (BQAppl /
// BuildTerm). Symbols not in this set are declared but never used,
// and emitting their helpers risks Java compile errors (e.g. when
// the body of `make()` calls a non-static method that's only legal
// in the surrounding class's instance methods). Java's reference
// compiler does the same dead-code elimination.
func collectUsedSymbols(list *tomast.ConcCodeCodeList) map[string]bool {
	used := map[string]bool{}
	var walk func(any)
	walk = func(n any) {
		if n == nil {
			return
		}
		switch x := n.(type) {
		case *tomast.TermApplTomTerm:
			used[headApplName(x.NameList)] = true
			if args, ok := x.Args.(*tomast.ConcTomTermTomList); ok {
				for _, a := range args.Slots {
					walk(a)
				}
			}
			walkConstraintList(x.Constraints, walk)
		case *tomast.RecordApplTomTerm:
			used[headApplName(x.NameList)] = true
			if slots, ok := x.Slots.(*tomast.ConcSlotSlotList); ok {
				for _, sl := range slots.Slots {
					if pair, ok := sl.(*tomast.PairSlotApplSlot); ok {
						walk(pair.Appl)
					}
				}
			}
			walkConstraintList(x.Constraints, walk)
		case *tomast.VariableTomTerm:
			walkConstraintList(x.Constraints, walk)
		case *tomast.BQApplBQTerm:
			used[nameString(x.AstName)] = true
			walkBQList(x.Args, walk)
		case *tomast.BuildTermBQTerm:
			used[nameString(x.AstName)] = true
			walkBQList(x.Args, walk)
		case *tomast.BQVariableBQTerm:
		case *tomast.MatchConstraintConstraint:
			walk(x.Pattern)
			walk(x.Subject)
		case *tomast.AndConstraintConstraint:
			for _, c := range x.Slots {
				walk(c)
			}
		case *tomast.MatchInstruction:
			if cl, ok := x.ConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList); ok {
				for _, ci := range cl.Slots {
					if row, ok := ci.(*tomast.ConstraintInstructionConstraintInstruction); ok {
						walk(row.Constraint)
						walk(row.Action)
					}
				}
			}
		case *tomast.AbstractBlockInstruction:
			if il, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, inst := range il.Slots {
					walk(inst)
				}
			}
		case *tomast.CodeToInstructionInstruction:
			walk(x.Code)
		case *tomast.BQTermToInstructionInstruction:
			walk(x.Tom)
		case *tomast.RawActionInstruction:
			walk(x.AstInstruction)
		case *tomast.IfInstruction:
			walk(x.SuccesInst)
			walk(x.FailureInst)
		case *tomast.TargetLanguageToCodeCode:
		case *tomast.DeclarationToCodeCode:
		case *tomast.InstructionToCodeCode:
			walk(x.AstInstruction)
		case *tomast.TomIncludeCode:
			if cl, ok := x.CodeList.(*tomast.ConcCodeCodeList); ok {
				for _, c := range cl.Slots {
					walk(c)
				}
			}
		}
	}
	for _, c := range list.Slots {
		walk(c)
	}
	delete(used, "")
	return used
}

func walkConstraintList(cl tomast.ConstraintList, walk func(any)) {
	c, ok := cl.(*tomast.ConcConstraintConstraintList)
	if !ok {
		return
	}
	for _, item := range c.Slots {
		walk(item)
	}
}

func walkBQList(l tomast.BQTermList, walk func(any)) {
	c, ok := l.(*tomast.ConcBQTermBQTermList)
	if !ok {
		return
	}
	for _, b := range c.Slots {
		walk(b)
	}
}

// ---------------------------------------------------------------------------
// Helpers
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

// isFreshOrEmpty reports whether a Variable name is one of the
// synthetic placeholders the parser/desugarer introduces (`_`,
// `_f_r_e_s_h_v_a_r_<N>`, …) — those should never become user-
// visible bindings.
func isFreshOrEmpty(name string) bool {
	if name == "" || name == "_" {
		return true
	}
	return strings.HasPrefix(name, "_f_r_e_s_h_v_a_r_")
}
