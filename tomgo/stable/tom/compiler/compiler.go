// Package compiler turns a typed Tom AST into a "compiled" AST that
// the backend can render to host-language text in a single
// mechanical walk.
//
// The transformation has three jobs:
//
//   1. Match expansion. Every `Match(constraintInstructions, opts)`
//      instruction becomes an `AbstractBlock` of independent rules.
//      Each rule is a nested chain of `If(cond, success, Nop())`
//      where the condition is a `Code` expression carrying the
//      textual helper call (e.g. `tom_is_sort_term((( ATerm )t1))`,
//      `tom_is_fun_sym_zero((( ATerm )t2))`).
//
//   2. Pattern-variable substitution. Each rule's pattern tree binds
//      every `Variable` (and any aliased `AliasTo`) to the textual
//      expression that produces its matched value (the cast subject
//      at the leaf, a `tom_get_slot_…` chain inside slots). The
//      rule's action body is walked, and every `BQVariable(name)`
//      gets replaced by a `TL(<bound-expression>)` chunk —
//      effectively inlining at compile time.
//
//   3. Backquoted-constructor materialisation. Each `BQAppl(op,
//      args)` / `BuildTerm(op, args)` becomes the sequence
//      `TL("tom_make_<op>(")` + (recursively materialised args
//      separated by `TL(",")`) + `TL(")")`. After this pass,
//      action bodies contain only `TL`/`CodeToInstruction` chunks.
//
// Side effects: the post-compile AST has NO `MatchInstruction`,
// NO `BQTermToInstruction`, NO `RawActionInstruction`. The backend
// can walk what remains as a plain (If/AbstractBlock/Nop +
// TargetLanguageToCode) tree.
package compiler

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run transforms the typed AST in `in.Code` into the compiled
// form. Returns the updated State with the new AST.
func Run(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	code, ok := in.Code.(*tomast.TomCode)
	if !ok {
		return in, fmt.Errorf("compiler: expected *TomCode, got %T", in.Code)
	}
	list, ok := code.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return in, fmt.Errorf("compiler: expected ConcCode list")
	}
	c := &compiler{symbols: in.Symbols}
	newSlots := make([]tomast.Code, len(list.Slots))
	for i, item := range list.Slots {
		newSlots[i] = c.transformCode(item)
	}
	out := in
	out.Code = tomast.MakeTom(tomast.MakeConcCode(newSlots...))
	return out, nil
}

type compiler struct {
	symbols  *tom.SymbolTable
	bindings map[string]string // per-rule variable bindings
}

func (c *compiler) transformCode(x tomast.Code) tomast.Code {
	switch v := x.(type) {
	case *tomast.InstructionToCodeCode:
		return tomast.MakeInstructionToCode(c.transformInstr(v.AstInstruction))
	case *tomast.TomIncludeCode:
		if cl, ok := v.CodeList.(*tomast.ConcCodeCodeList); ok {
			inner := make([]tomast.Code, len(cl.Slots))
			for i, item := range cl.Slots {
				inner[i] = c.transformCode(item)
			}
			return tomast.MakeTomInclude(tomast.MakeConcCode(inner...))
		}
	}
	return x
}

func (c *compiler) transformInstr(i tomast.Instruction) tomast.Instruction {
	switch x := i.(type) {
	case *tomast.MatchInstruction:
		return c.expandMatch(x)
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			inner := make([]tomast.Instruction, 0, len(list.Slots))
			for _, item := range list.Slots {
				inner = append(inner, c.transformAndSubstitute(item)...)
			}
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(inner...))
		}
	case *tomast.BQTermToInstructionInstruction:
		// Top-level BQ (outside any Match action) — there's no
		// rule-scoped binding map, but the substitution still has
		// to materialise `tom_make_<op>(...)` etc. for backquoted
		// constructors. The empty `c.bindings` map makes the inner
		// substitutor leave bare `BQVariable(tzero)` etc. as their
		// own names (which is what we want — they're host vars).
		c.bindings = map[string]string{}
		instrs := c.substituteBQTerm(x.Tom)
		if len(instrs) == 1 {
			return instrs[0]
		}
		return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(instrs...))
	}
	return i
}

// transformAndSubstitute is the per-item variant of transformInstr
// that returns a slice (so a single BQ instruction can expand into
// multiple TL chunks). Used inside AbstractBlock walks.
func (c *compiler) transformAndSubstitute(i tomast.Instruction) []tomast.Instruction {
	if bq, ok := i.(*tomast.BQTermToInstructionInstruction); ok {
		c.bindings = map[string]string{}
		return c.substituteBQTerm(bq.Tom)
	}
	return []tomast.Instruction{c.transformInstr(i)}
}

// expandMatch turns a Match into AbstractBlock(rule1, rule2, ...)
// where each rule is itself a chain of If(cond, then, Nop()).
func (c *compiler) expandMatch(m *tomast.MatchInstruction) tomast.Instruction {
	list, ok := m.ConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList)
	if !ok {
		return m
	}
	rules := make([]tomast.Instruction, 0, len(list.Slots))
	for _, ci := range list.Slots {
		row, ok := ci.(*tomast.ConstraintInstructionConstraintInstruction)
		if !ok {
			continue
		}
		rules = append(rules, c.expandRule(row))
	}
	return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(rules...))
}

// expandRule turns one (constraint, action) pair into the nested
// If structure. Variable bindings are collected from the pattern
// walk first (`collectBindings`), THEN the action body is
// transformed (so its BQVariable substitutions see populated
// bindings), and finally the wrap is built (which re-walks the
// constraint to produce the If chain).
func (c *compiler) expandRule(row *tomast.ConstraintInstructionConstraintInstruction) tomast.Instruction {
	c.bindings = map[string]string{}
	c.collectBindings(row.Constraint)
	actionBody := c.transformActionBody(row.Action)
	return c.wrapConstraint(row.Constraint, actionBody)
}

// collectBindings walks the constraint tree filling c.bindings, but
// does NOT build any If chain. Used as a pre-pass so action-body
// BQ-substitution sees all bindings up front.
func (c *compiler) collectBindings(cs tomast.Constraint) {
	switch x := cs.(type) {
	case *tomast.AndConstraintConstraint:
		for _, inner := range x.Slots {
			c.collectBindings(inner)
		}
	case *tomast.MatchConstraintConstraint:
		bq, ok := x.Subject.(*tomast.BQVariableBQTerm)
		if !ok {
			return
		}
		subjectName := nameString(bq.AstName)
		subjectTypeTL := "Object"
		if t, ok := bq.AstType.(*tomast.TypeTomType); ok {
			if tl, ok := t.TlType.(*tomast.TLTypeTargetLanguageType); ok && strings.TrimSpace(tl.String_) != "" {
				subjectTypeTL = strings.TrimSpace(tl.String_)
			}
		}
		subjectExpr := fmt.Sprintf("(( %s )%s)", subjectTypeTL, subjectName)
		c.collectPatternBindings(x.Pattern, subjectExpr, subjectTypeTL)
	}
}

// collectPatternBindings is the pattern-walker for bindings only —
// same shape as wrapPattern but emits no If, just populates
// c.bindings.
func (c *compiler) collectPatternBindings(p tomast.TomTerm, subjectExpr, subjectTypeTL string) {
	switch pat := p.(type) {
	case *tomast.VariableTomTerm:
		name := nameString(pat.AstName)
		if name != "" && !isFreshOrEmpty(name) {
			c.bindings[name] = subjectExpr
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
	case *tomast.VariableStarTomTerm:
		name := nameString(pat.AstName)
		if name != "" && !isFreshOrEmpty(name) {
			c.bindings[name] = subjectExpr
		}
	case *tomast.TermApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return
		}
		if args, ok := pat.Args.(*tomast.ConcTomTermTomList); ok {
			slotNames := c.slotNamesFor(applName)
			for i, arg := range args.Slots {
				if i >= len(slotNames) {
					continue
				}
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotNames[i], subjectExpr)
				innerTL := c.slotTLType(applName, slotNames[i])
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				c.collectPatternBindings(arg, inner, innerTL)
			}
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
	case *tomast.RecordApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return
		}
		if slots, ok := pat.Slots.(*tomast.ConcSlotSlotList); ok {
			for _, sl := range slots.Slots {
				pair, ok := sl.(*tomast.PairSlotApplSlot)
				if !ok {
					continue
				}
				slotName := nameString(pair.SlotName)
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotName, subjectExpr)
				innerTL := c.slotTLType(applName, slotName)
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				c.collectPatternBindings(pair.Appl, inner, innerTL)
			}
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
	}
}

func (c *compiler) wrapConstraint(cs tomast.Constraint, body tomast.Instruction) tomast.Instruction {
	switch x := cs.(type) {
	case *tomast.AndConstraintConstraint:
		// Each constraint wraps the result of the next. Process
		// right-to-left so the leftmost constraint's check sits
		// outermost in the resulting If chain.
		result := body
		for i := len(x.Slots) - 1; i >= 0; i-- {
			result = c.wrapConstraint(x.Slots[i], result)
		}
		return result
	case *tomast.MatchConstraintConstraint:
		return c.wrapMatchConstraint(x, body)
	}
	return body
}

func (c *compiler) wrapMatchConstraint(m *tomast.MatchConstraintConstraint, body tomast.Instruction) tomast.Instruction {
	bq, ok := m.Subject.(*tomast.BQVariableBQTerm)
	if !ok {
		return body
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
	subjectExpr := fmt.Sprintf("(( %s )%s)", subjectTypeTL, subjectName)
	wrapped := c.wrapPattern(m.Pattern, subjectExpr, subjectTypeTL, body)
	if subjectTypeName != "" {
		return makeIfText(fmt.Sprintf("tom_is_sort_%s(%s)", subjectTypeName, subjectName), wrapped)
	}
	return wrapped
}

// wrapPattern walks one pattern, populating bindings and wrapping
// `body` with the appropriate If checks. The result is an Instruction
// chain whose innermost is `body` and whose outermost is the first
// check we want evaluated.
func (c *compiler) wrapPattern(p tomast.TomTerm, subjectExpr, subjectTypeTL string, body tomast.Instruction) tomast.Instruction {
	switch pat := p.(type) {
	case *tomast.VariableTomTerm:
		name := nameString(pat.AstName)
		if name != "" && !isFreshOrEmpty(name) {
			c.bindings[name] = subjectExpr
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
		return body
	case *tomast.VariableStarTomTerm:
		name := nameString(pat.AstName)
		if name != "" && !isFreshOrEmpty(name) {
			c.bindings[name] = subjectExpr
		}
		return body
	case *tomast.TermApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return body
		}
		// Positional args use slot names from the SymbolTable.
		innerBody := body
		if args, ok := pat.Args.(*tomast.ConcTomTermTomList); ok && len(args.Slots) > 0 {
			slotNames := c.slotNamesFor(applName)
			for i := len(args.Slots) - 1; i >= 0; i-- {
				if i >= len(slotNames) {
					continue
				}
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotNames[i], subjectExpr)
				innerTL := c.slotTLType(applName, slotNames[i])
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				innerBody = c.wrapPattern(args.Slots[i], inner, innerTL, innerBody)
			}
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
		return makeIfText(fmt.Sprintf("tom_is_fun_sym_%s(%s)", applName, subjectExpr), innerBody)
	case *tomast.RecordApplTomTerm:
		applName := headApplName(pat.NameList)
		if applName == "" {
			return body
		}
		innerBody := body
		if slots, ok := pat.Slots.(*tomast.ConcSlotSlotList); ok {
			for i := len(slots.Slots) - 1; i >= 0; i-- {
				pair, ok := slots.Slots[i].(*tomast.PairSlotApplSlot)
				if !ok {
					continue
				}
				slotName := nameString(pair.SlotName)
				inner := fmt.Sprintf("tom_get_slot_%s_%s(%s)", applName, slotName, subjectExpr)
				innerTL := c.slotTLType(applName, slotName)
				if innerTL == "" {
					innerTL = subjectTypeTL
				}
				if appl, ok := pair.Appl.(tomast.TomTerm); ok {
					innerBody = c.wrapPattern(appl, inner, innerTL, innerBody)
				}
			}
		}
		c.applyAliasConstraints(pat.Constraints, subjectExpr)
		return makeIfText(fmt.Sprintf("tom_is_fun_sym_%s(%s)", applName, subjectExpr), innerBody)
	}
	return body
}

func (c *compiler) applyAliasConstraints(cs tomast.ConstraintList, subjectExpr string) {
	conc, ok := cs.(*tomast.ConcConstraintConstraintList)
	if !ok {
		return
	}
	for _, item := range conc.Slots {
		alias, ok := item.(*tomast.AliasToConstraint)
		if !ok {
			continue
		}
		if v, ok := alias.Var.(*tomast.VariableTomTerm); ok {
			name := nameString(v.AstName)
			if name != "" && !isFreshOrEmpty(name) {
				c.bindings[name] = subjectExpr
			}
		}
	}
}

// transformActionBody unwraps RawAction(If(TrueTL, body, Nop())) and
// walks the body substituting every BQ term for a TL-text chunk.
// After this pass the action body is a plain CodeToInstruction
// sequence.
func (c *compiler) transformActionBody(a tomast.Instruction) tomast.Instruction {
	switch x := a.(type) {
	case *tomast.RawActionInstruction:
		return c.transformActionBody(x.AstInstruction)
	case *tomast.IfInstruction:
		return c.transformActionBody(x.SuccesInst)
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			inner := make([]tomast.Instruction, 0, len(list.Slots))
			for _, item := range list.Slots {
				inner = append(inner, c.substituteBQ(item)...)
			}
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(inner...))
		}
	}
	return a
}

// substituteBQ turns a single action-body instruction into one or
// more compiled instructions. Pure TL chunks pass through; BQ chunks
// expand into a sequence of TL chunks.
func (c *compiler) substituteBQ(i tomast.Instruction) []tomast.Instruction {
	switch x := i.(type) {
	case *tomast.BQTermToInstructionInstruction:
		return c.substituteBQTerm(x.Tom)
	}
	return []tomast.Instruction{i}
}

func (c *compiler) substituteBQTerm(t tomast.BQTerm) []tomast.Instruction {
	var out []tomast.Instruction
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		name := nameString(x.AstName)
		text := name
		if bound, ok := c.bindings[name]; ok {
			text = bound
		}
		out = append(out, makeTLInstr(text))
	case *tomast.BQApplBQTerm:
		applName := nameString(x.AstName)
		out = append(out, makeTLInstr(fmt.Sprintf("tom_make_%s(", applName)))
		if args, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
			for i, a := range args.Slots {
				if i > 0 {
					out = append(out, makeTLInstr(","))
				}
				out = append(out, c.substituteBQTerm(a)...)
			}
		}
		out = append(out, makeTLInstr(")"))
	case *tomast.BuildTermBQTerm:
		applName := nameString(x.AstName)
		out = append(out, makeTLInstr(fmt.Sprintf("tom_make_%s(", applName)))
		if args, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
			for i, a := range args.Slots {
				if i > 0 {
					out = append(out, makeTLInstr(","))
				}
				out = append(out, c.substituteBQTerm(a)...)
			}
		}
		out = append(out, makeTLInstr(")"))
	}
	return out
}

// ---------------------------------------------------------------------------
// helpers
// ---------------------------------------------------------------------------

// makeIfText wraps `body` in `If(Code(condText), body, Nop())`. The
// backend renders Code(text) as the condition's verbatim text.
func makeIfText(condText string, body tomast.Instruction) tomast.Instruction {
	return tomast.MakeIf(tomast.MakeCode(condText), body, tomast.MakeNop())
}

// makeTLInstr wraps `text` as `CodeToInstruction(TargetLanguageToCode
// (TL(text, anchor, anchor)))`. Anchor positions are zeroed —
// positions on synthetic chunks carry no semantic info.
func makeTLInstr(text string) tomast.Instruction {
	return tomast.MakeCodeToInstruction(
		tomast.MakeTargetLanguageToCode(
			tomast.MakeTL(
				text,
				tomast.MakeTextPosition(0, 0),
				tomast.MakeTextPosition(0, 0),
			)))
}

func (c *compiler) slotNamesFor(opName string) []string {
	if c.symbols == nil {
		return nil
	}
	sym, ok := c.symbols.Symbols[opName]
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

func (c *compiler) slotTLType(opName, slotName string) string {
	if c.symbols == nil {
		return ""
	}
	sym, ok := c.symbols.Symbols[opName]
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
	names := c.slotNamesFor(opName)
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
			if c.symbols != nil {
				if body, ok := c.symbols.Sorts[t.TomType]; ok && strings.TrimSpace(body) != "" {
					return strings.TrimSpace(body)
				}
			}
		}
	}
	return ""
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

func isFreshOrEmpty(name string) bool {
	if name == "" || name == "_" {
		return true
	}
	return strings.HasPrefix(name, "_f_r_e_s_h_v_a_r_")
}
