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
	used := collectUsedSymbols(list)
	// Also include symbols referenced in Strategy declarations that
	// the Backend will emit (their ExtendsTerm + visit rule actions
	// reach helpers like `tom_make_Identity`, `tom_make_b`, …).
	collectStrategySymbols(list, used)
	// Transitive closure: a symbol's helpers may reference other
	// symbols via their `make` hook bodies (e.g. sl's `BottomUp`
	// body references `Sequence`, `mu`, `MuVar`, `All`). Walk the
	// SymbolTable's MakeDecl bodies repeatedly until no new symbol
	// is discovered.
	if in.Symbols != nil {
		for {
			added := false
			for name := range used {
				sym, ok := in.Symbols.Symbols[name]
				if !ok {
					continue
				}
				s, ok := sym.(*tomast.SymbolTomSymbol)
				if !ok {
					continue
				}
				opts, ok := s.Options.(*tomast.ConcOptionOptionList)
				if !ok {
					continue
				}
				for _, opt := range opts.Slots {
					declOpt, ok := opt.(*tomast.DeclarationToOptionOption)
					if !ok {
						continue
					}
					mkd, ok := declOpt.AstDeclaration.(*tomast.MakeDeclDeclaration)
					if !ok {
						continue
					}
					before := len(used)
					scanInstrForUsedSymbols(mkd.Instr, used)
					if len(used) > before {
						added = true
					}
				}
			}
			if !added {
				break
			}
		}
	}
	e := &emitter{symbols: in.Symbols, usedSymbols: used}
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
	case *tomast.ListSymbolDeclDeclaration:
		// %oplist constructor — same helpers as syntactic, plus
		// the list-specific variadic ones (handled inside).
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.ArraySymbolDeclDeclaration:
		// %oparray constructor — same path as above; the variadic
		// detection inside emitSymbolHelpers picks up the
		// MakeEmptyArray/MakeAddArray hooks.
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.StrategyDeclaration:
		// %strategy R() extends X() — emit the full Java class +
		// `tom_make_R()` static factory. The visit blocks are
		// compiled via the standalone compiler.CompileMatch
		// entry-point (backend_strategy.go).
		return e.emitStrategy(x)
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
//
// For variadic `%oparray L conc(E*)` symbols this also emits the two
// derived helpers `tom_get_slice_<op>` and `tom_append_array_<op>`,
// inlining the operator's own `make_empty` / `make_append` /
// `get_element` / `get_size` bodies. Mirrors the bodies Java's
// reference compiler emits in `Array.java`.
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

	// Raw placeholder-form bodies captured during the loop — re-used
	// when emitting the generic slice/append helpers afterwards.
	var (
		emptyArrayRaw string
		addArrayRaw   string
		getElemRaw    string
		getSizeRaw    string
		haveEmptyArr  bool
		haveAddArr    bool
		haveGetElem   bool
		haveGetSize   bool

		// %oplist raw bodies for the list-shaped helpers.
		emptyListRaw    string
		addListRaw      string
		getHeadRaw      string
		getTailRaw      string
		isEmptyRaw      string
		haveEmptyList   bool
		haveAddList     bool
		haveGetHead     bool
		haveGetTail     bool
		haveIsEmptyList bool
	)

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
			codomainTL := tlTypeFor(s, e.symbols)
			params, names := makeDeclParams(decl, e.symbols)
			// Two body shapes coexist:
			//   - Raw: ExpressionToInstruction(Code(rawText)) — the
			//     parser keeps the body verbatim when it contains
			//     no backquote. We substitute `$<argname>` at emit.
			//   - Structured: AbstractBlock(TL + BQTermToInstruction
			//     chunks) emitted when the body has at least one
			//     `` ` `` so the typer can rewrite the embedded
			//     terms (e.g. sl's strategy combinators) into
			//     BuildTerm / BuildConsList.
			fmt.Fprintf(&e.buf, "private static %s tom_make_%s(%s) { return ",
				codomainTL, opName, params)
			if body := codeBodyFromInstruction(decl.Instr); body != "" {
				e.buf.WriteString(substituteDollarVars(body, names))
			} else {
				e.emitMakeBody(decl.Instr, names)
			}
			e.buf.WriteString(";}")
		case *tomast.GetSlotDeclDeclaration:
			body := codeBodyFromExpr(decl.Expr)
			body = substitutePlaceholders(body, []string{"t"})
			// PARAM type is the op's codomain (the helper accepts
			// the subject and casts inside); RETURN type is the
			// slot's declared type (read from the symbol's Domain
			// at the slot's position).
			codomainTL := tlTypeFor(s, e.symbols)
			slotName := nameString(decl.SlotName)
			slotTL := slotTLTypeFor(s, slotName, e.symbols)
			if slotTL == "" {
				slotTL = codomainTL // fallback when the slot's TL is unknown
			}
			fmt.Fprintf(&e.buf, "private static %s tom_get_slot_%s_%s(%s t) {return %s;}",
				slotTL, opName, slotName, codomainTL, body)
		case *tomast.MakeEmptyArrayDeclaration:
			emptyArrayRaw = codeBodyFromInstruction(decl.Instr)
			haveEmptyArr = true
			sizeName := bqVarName(decl.VarSize)
			if sizeName == "" {
				sizeName = "n"
			}
			body := substitutePlaceholders(emptyArrayRaw, []string{sizeName})
			codomainTL := tlTypeFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "private static %s tom_empty_array_%s(int %s) { return %s;}",
				codomainTL, opName, sizeName, body)
		case *tomast.MakeAddArrayDeclaration:
			addArrayRaw = codeBodyFromInstruction(decl.Instr)
			haveAddArr = true
			eltName := bqVarName(decl.VarElt)
			listName := bqVarName(decl.VarList)
			body := substitutePlaceholders(addArrayRaw, []string{eltName, listName})
			codomainTL := tlTypeFor(s, e.symbols)
			elemTL := elemTLFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "private static %s tom_cons_array_%s(%s %s, %s %s) { return %s;}",
				codomainTL, opName, elemTL, eltName, codomainTL, listName, body)
		case *tomast.GetElementDeclDeclaration:
			getElemRaw = codeBodyFromExpr(decl.Expr)
			haveGetElem = true
			listName := bqVarName(decl.Variable)
			indexName := bqVarName(decl.Index)
			body := substitutePlaceholders(getElemRaw, []string{listName, indexName})
			codomainTL := tlTypeFor(s, e.symbols)
			elemTL := elemTLFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "private static %s tom_get_element_%s_%s(%s %s, int %s) {return %s;}",
				elemTL, opName, codomainSort, codomainTL, listName, indexName, body)
		case *tomast.GetSizeDeclDeclaration:
			getSizeRaw = codeBodyFromExpr(decl.Expr)
			haveGetSize = true
			listName := bqVarName(decl.Variable)
			body := substitutePlaceholders(getSizeRaw, []string{listName})
			codomainTL := tlTypeFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "private static int tom_get_size_%s_%s(%s %s) {return %s;}",
				opName, codomainSort, codomainTL, listName, body)
		// ---------------------------------------------------------------
		// %oplist hook helpers — list version of the array helpers above.
		case *tomast.MakeEmptyListDeclaration:
			emptyListRaw = codeBodyFromInstruction(decl.Instr)
			haveEmptyList = true
			codomainTL := tlTypeFor(s, e.symbols)
			// Synthetic / parser-marker MakeEmptyList terms wrap a
			// Nop instruction. Emit the helper only when there's a
			// real body.
			body := emptyListRaw
			if body != "" {
				fmt.Fprintf(&e.buf, "private static %s tom_empty_list_%s() { return %s;}",
					codomainTL, opName, body)
			}
		case *tomast.MakeAddListDeclaration:
			addListRaw = codeBodyFromInstruction(decl.Instr)
			haveAddList = true
			eltName := bqVarName(decl.VarElt)
			listName := bqVarName(decl.VarList)
			body := substitutePlaceholders(addListRaw, []string{eltName, listName})
			codomainTL := tlTypeFor(s, e.symbols)
			elemTL := elemTLFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "private static %s tom_cons_list_%s(%s %s, %s %s) { return %s;}",
				codomainTL, opName, elemTL, eltName, codomainTL, listName, body)
		case *tomast.GetHeadDeclDeclaration:
			getHeadRaw = codeBodyFromExpr(decl.Expr)
			haveGetHead = true
			listName := bqVarName(decl.Variable)
			body := substitutePlaceholders(getHeadRaw, []string{listName})
			codomainTL := tlTypeFor(s, e.symbols)
			elemTL := elemTLFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "private static %s tom_get_head_%s_%s(%s %s) {return %s;}",
				elemTL, opName, codomainSort, codomainTL, listName, body)
		case *tomast.GetTailDeclDeclaration:
			getTailRaw = codeBodyFromExpr(decl.Expr)
			haveGetTail = true
			listName := bqVarName(decl.Variable)
			body := substitutePlaceholders(getTailRaw, []string{listName})
			codomainTL := tlTypeFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "private static %s tom_get_tail_%s_%s(%s %s) {return %s;}",
				codomainTL, opName, codomainSort, codomainTL, listName, body)
		case *tomast.IsEmptyDeclDeclaration:
			isEmptyRaw = codeBodyFromExpr(decl.Expr)
			haveIsEmptyList = true
			listName := bqVarName(decl.Variable)
			body := substitutePlaceholders(isEmptyRaw, []string{listName})
			codomainTL := tlTypeFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "private static boolean tom_is_empty_%s_%s(%s %s) {return %s;}",
				opName, codomainSort, codomainTL, listName, body)
		}
	}

	// Variadic-array generic helpers: tom_get_slice_<op> and
	// tom_append_array_<op>. Java's reference emits these for every
	// `%oparray` that survives dead-code elimination; we mirror the
	// same shape with inline-substituted hook bodies.
	if haveEmptyArr && haveAddArr && haveGetElem && haveGetSize {
		codomainTL := tlTypeFor(s, e.symbols)

		// tom_get_slice_<op>(subject, begin, end): result is built
		// by repeatedly cons-ing element `subject[begin]` onto
		// `result`, starting from `tom_empty_array_<op>(end-begin)`.
		emptySliceBody := substitutePlaceholders(emptyArrayRaw, []string{"end-begin"})
		sliceGetElem := substitutePlaceholders(getElemRaw, []string{"subject", "begin"})
		sliceAddBody := substitutePlaceholders(addArrayRaw, []string{sliceGetElem, "result"})
		fmt.Fprintf(&e.buf,
			"   private static   %s  tom_get_slice_%s( %s  subject, int begin, int end) {      %s  result = %s;     while(begin!=end) {       result = %s;       begin++;     }     return result;   }",
			codomainTL, opName, codomainTL, codomainTL, emptySliceBody, sliceAddBody)

		// tom_append_array_<op>(l2, l1): copy l1 then l2 into a
		// freshly-built array sized size1+size2.
		appSize1 := substitutePlaceholders(getSizeRaw, []string{"l1"})
		appSize2 := substitutePlaceholders(getSizeRaw, []string{"l2"})
		appEmpty := substitutePlaceholders(emptyArrayRaw, []string{"size1+size2"})
		appGetElemL1 := substitutePlaceholders(getElemRaw, []string{"l1", "size1-index"})
		appAddL1 := substitutePlaceholders(addArrayRaw, []string{appGetElemL1, "result"})
		appGetElemL2 := substitutePlaceholders(getElemRaw, []string{"l2", "size2-index"})
		appAddL2 := substitutePlaceholders(addArrayRaw, []string{appGetElemL2, "result"})
		fmt.Fprintf(&e.buf,
			"    private static   %s  tom_append_array_%s( %s  l2,  %s  l1) {     int size1 = %s;     int size2 = %s;     int index;      %s  result = %s;     index=size1;     while(index >0) {       result = %s;       index--;     }      index=size2;     while(index > 0) {       result = %s;       index--;     }     return result;   }",
			codomainTL, opName, codomainTL, codomainTL, appSize1, appSize2, codomainTL, appEmpty, appAddL1, appAddL2)
	}

	// Variadic-list generic helpers: tom_append_list_<op> and
	// tom_get_slice_<op>. Mirrors Java's GenericGenerator output
	// for every `%oplist` symbol that survived dead-code
	// elimination.
	if haveEmptyList && haveAddList && haveGetHead && haveGetTail && haveIsEmptyList {
		codomainTL := tlTypeFor(s, e.symbols)
		elemTL := elemTLFor(s, e.symbols)
		_ = elemTL

		// All four bodies are referenced multiple times — pre-bake
		// the parameterised forms.
		isEmptyL1 := substitutePlaceholders(isEmptyRaw, []string{"l1"})
		isEmptyL2 := substitutePlaceholders(isEmptyRaw, []string{"l2"})
		isEmptyTailL1 := substitutePlaceholders(isEmptyRaw,
			[]string{substitutePlaceholders(getTailRaw, []string{"l1"})})
		isEmptyEnd := substitutePlaceholders(isEmptyRaw, []string{"end"})
		getHeadL1 := substitutePlaceholders(getHeadRaw, []string{"l1"})
		getHeadBegin := substitutePlaceholders(getHeadRaw, []string{"begin"})
		getTailL1 := substitutePlaceholders(getTailRaw, []string{"l1"})
		getTailBegin := substitutePlaceholders(getTailRaw, []string{"begin"})
		consHeadOntoL2 := substitutePlaceholders(addListRaw, []string{getHeadL1, "l2"})
		consHeadOntoRec := substitutePlaceholders(addListRaw,
			[]string{getHeadBegin,
				"(" + codomainTL + ")tom_get_slice_" + opName + "(" + getTailBegin + ",end,tail)"})
		// tom_append_list(l1, l2):
		//   if isEmpty(l1) → l2
		//   else if isEmpty(l2) → l1
		//   else if isEmpty(tail(l1)) → cons(head(l1), l2)
		//   else → cons(head(l1), append(tail(l1), l2))
		consAppendRec := substitutePlaceholders(addListRaw,
			[]string{getHeadL1, "tom_append_list_" + opName + "(" + getTailL1 + ",l2)"})
		fmt.Fprintf(&e.buf,
			"   private static   %s  tom_append_list_%s( %s l1,  %s  l2) {     if(%s) {       return l2;     } else if(%s) {       return l1;     } else if(%s) {       return %s;     } else {       return %s;     }   }",
			codomainTL, opName, codomainTL, codomainTL,
			isEmptyL1, isEmptyL2, isEmptyTailL1,
			consHeadOntoL2, consAppendRec,
		)

		// tom_get_slice(begin, end, tail):
		//   if begin == end → tail
		//   else if end == tail && (isEmpty(end) || end == empty()) → begin
		//   else → cons(head(begin), get_slice(tail(begin), end, tail))
		emptyBody := emptyListRaw
		fmt.Fprintf(&e.buf,
			"   private static   %s  tom_get_slice_%s( %s  begin,  %s  end, %s  tail) {     if( begin.equals(end) ) {       return tail;     } else if( end.equals(tail)  && ( %s  ||  end.equals(%s) )) {       return begin;     }     return %s;   }",
			codomainTL, opName, codomainTL, codomainTL, codomainTL,
			isEmptyEnd, emptyBody, consHeadOntoRec,
		)
	}
	return nil
}

// emitInstruction dispatches on the post-compile instruction shapes.
// Match / BQTermToInstruction / RawAction have all been removed by
// the legacy Compiler. The new-path Compiler emits CompiledMatch /
// Let / LetRef / Assign / DoWhile / WhileDo / Return / Cast /
// IsSort / IsFsym (handled by `emitIRInstruction` in backend_ir.go).
// Anything else surfaces as a `/* unsupported */` comment so the
// generated Java still parses and the missing case is visible.
func (e *emitter) emitInstruction(i tomast.Instruction) error {
	// Try the IR-shape renderer first — it returns (handled, err).
	if handled, err := e.emitIRInstruction(i); handled {
		return err
	}
	switch x := i.(type) {
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
	case *tomast.UnamedBlockInstruction:
		// `{ ... }` from host-language braces inside an action.
		// Wrap the contents in literal braces so Java sees a
		// proper block.
		e.buf.WriteString("{")
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
		e.buf.WriteString("}")
	case *tomast.CodeToInstructionInstruction:
		return e.emitCode(x.Code)
	case *tomast.IfInstruction:
		// `if (<cond>) { <then> } else { <else> }` — but if the
		// else branch is Nop the brace pair would compile cleanly
		// either way, so we emit `else { … }` only when there's
		// real content. The condition is rendered via the
		// expression lowering (handles both legacy raw Code and
		// new-path IsSort/IsFsym/...).
		e.buf.WriteString("if (")
		e.emitExpression(x.Condition)
		e.buf.WriteString(") {")
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

// collectStrategySymbols walks every StrategyDeclaration reachable
// from the top-level Code list and marks the symbols its emission
// will reference: the head of `ExtendsTerm` (e.g. `Identity`) and
// every BQAppl appearing in the visit rule action bodies.
func collectStrategySymbols(list *tomast.ConcCodeCodeList, used map[string]bool) {
	var walkCode func(tomast.Code)
	var walkInstr func(tomast.Instruction)
	walkCode = func(c tomast.Code) {
		switch x := c.(type) {
		case *tomast.DeclarationToCodeCode:
			if strat, ok := x.AstDeclaration.(*tomast.StrategyDeclaration); ok {
				processStrategy(strat, used)
			}
		case *tomast.InstructionToCodeCode:
			walkInstr(x.AstInstruction)
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
			if l, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, s := range l.Slots {
					walkInstr(s)
				}
			}
		case *tomast.UnamedBlockInstruction:
			if l, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, s := range l.Slots {
					walkInstr(s)
				}
			}
		case *tomast.CodeToInstructionInstruction:
			walkCode(x.Code)
		}
	}
	for _, c := range list.Slots {
		walkCode(c)
	}
}

// processStrategy marks all symbols a strategy declaration's emit
// will reference. The ExtendsTerm head becomes a `tom_make_<X>()`
// call; every visit rule's action body may use `tom_is_fun_sym_<X>`,
// `tom_make_<X>`, etc.
func processStrategy(strat *tomast.StrategyDeclaration, used map[string]bool) {
	// ExtendsTerm — the super() argument.
	scanBQTerm(strat.ExtendsTerm, used)
	// Visit list — recurse into each visit's ConstraintInstructions.
	if vl, ok := strat.VisitList.(*tomast.ConcTomVisitTomVisitList); ok {
		for _, v := range vl.Slots {
			vt, ok := v.(*tomast.VisitTermTomVisit)
			if !ok {
				continue
			}
			if cil, ok := vt.AstConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList); ok {
				for _, ci := range cil.Slots {
					inst, ok := ci.(*tomast.ConstraintInstructionConstraintInstruction)
					if !ok {
						continue
					}
					// Mark the pattern's head symbol as used.
					scanConstraint(inst.Constraint, used)
					// Walk the action body (RawAction wrapping
					// AbstractBlock).
					scanInstrForUsedSymbols(inst.Action, used)
				}
			}
		}
	}
}

// scanBQTerm marks the head op of a BQAppl/BuildTerm/etc. as used.
func scanBQTerm(b tomast.BQTerm, used map[string]bool) {
	scanInstrForUsedSymbols(tomast.MakeBQTermToInstruction(b), used)
}

// scanConstraint scans a constraint's pattern for op references.
func scanConstraint(c tomast.Constraint, used map[string]bool) {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		scanPattern(x.Pattern, used)
	case *tomast.AndConstraintConstraint:
		for _, s := range x.Slots {
			scanConstraint(s, used)
		}
	case *tomast.OrConstraintConstraint:
		for _, s := range x.Slots {
			scanConstraint(s, used)
		}
	}
}

// scanPattern adds every RecordAppl / TermAppl head name to `used`.
func scanPattern(p tomast.TomTerm, used map[string]bool) {
	switch x := p.(type) {
	case *tomast.RecordApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok {
			for _, n := range nl.Slots {
				used[renderTomName(n)] = true
			}
		}
		if sl, ok := x.Slots.(*tomast.ConcSlotSlotList); ok {
			for _, s := range sl.Slots {
				if pair, ok := s.(*tomast.PairSlotApplSlot); ok {
					scanPattern(pair.Appl, used)
				}
			}
		}
	case *tomast.TermApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok {
			for _, n := range nl.Slots {
				used[renderTomName(n)] = true
			}
		}
		if a, ok := x.Args.(*tomast.ConcTomTermTomList); ok {
			for _, s := range a.Slots {
				scanPattern(s, used)
			}
		}
	}
}

// scanInstrForUsedSymbols walks an Instruction (used to scan MakeDecl
// bodies stored in the SymbolTable) and adds every referenced
// operator name to `used`. Reuses the same per-shape walkers as
// `collectUsedSymbols` but as a standalone driver — that helper's
// closure-style nested funcs aren't reachable from this scope.
func scanInstrForUsedSymbols(i tomast.Instruction, used map[string]bool) {
	var walkInstr func(tomast.Instruction)
	var walkExpr func(tomast.Expression)
	var walkBQ func(tomast.BQTerm)
	walkBQ = func(b tomast.BQTerm) {
		switch x := b.(type) {
		case *tomast.BuildTermBQTerm:
			used[renderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.FunctionCallBQTerm:
			used[renderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.BuildConstantBQTerm:
			used[renderTomName(x.AstName)] = true
		case *tomast.BuildEmptyListBQTerm:
			used[renderTomName(x.AstName)] = true
		case *tomast.BuildEmptyArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.Size)
		case *tomast.BuildConsListBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildConsArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendListBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.ExpressionToBQTermBQTerm:
			walkExpr(x.Exp)
		case *tomast.CompositeBQTerm:
			for _, m := range x.Slots {
				if bm, ok := m.(*tomast.CompositeBQTermCompositeMember); ok {
					walkBQ(bm.Term)
				}
			}
		}
	}
	walkExpr = func(expr tomast.Expression) {
		switch x := expr.(type) {
		case *tomast.CodeExpression:
			scanReferences(x.Code, used)
		case *tomast.BQTermToExpressionExpression:
			walkBQ(x.AstTerm)
		}
	}
	walkInstr = func(in tomast.Instruction) {
		switch x := in.(type) {
		case *tomast.AbstractBlockInstruction:
			if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, inner := range list.Slots {
					walkInstr(inner)
				}
			}
		case *tomast.UnamedBlockInstruction:
			if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, inner := range list.Slots {
					walkInstr(inner)
				}
			}
		case *tomast.CodeToInstructionInstruction:
			if tl, ok := x.Code.(*tomast.TargetLanguageToCodeCode); ok {
				switch t := tl.Tl.(type) {
				case *tomast.TLTargetLanguage:
					scanReferences(t.Code, used)
				case *tomast.ITLTargetLanguage:
					scanReferences(t.Code, used)
				}
			}
		case *tomast.BQTermToInstructionInstruction:
			walkBQ(x.Tom)
		case *tomast.ExpressionToInstructionInstruction:
			walkExpr(x.Expr)
		}
	}
	walkInstr(i)
}

// collectUsedSymbols walks the AST once before emission, recording
// every operator name referenced as a helper call. Two sources of
// references coexist:
//
//   - **Legacy path** — `Code(...)` chunks the legacy compiler
//     emits with raw helper-call text (`tom_is_fun_sym_X(...)`,
//     `tom_make_X(...)`, `tom_get_slot_X_slot(...)`). Names recovered
//     by `scanReferences`.
//   - **New IL path** — explicit AST nodes (`IsFsymExpression`,
//     `BuildTermBQTerm`, `BuildConsArrayBQTerm`, …) where the symbol
//     name is right there in the node. Names recovered by walking
//     the tree structurally.
//
// Only symbols in the resulting set get their helpers emitted, which
// matches Java's reference dead-code elimination.
func collectUsedSymbols(list *tomast.ConcCodeCodeList) map[string]bool {
	used := map[string]bool{}
	var walkCode func(c tomast.Code)
	var walkInstr func(i tomast.Instruction)
	var walkExpr func(e tomast.Expression)
	var walkBQ func(b tomast.BQTerm)

	walkBQ = func(b tomast.BQTerm) {
		switch x := b.(type) {
		case *tomast.BuildTermBQTerm:
			used[renderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.FunctionCallBQTerm:
			used[renderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.BuildConstantBQTerm:
			used[renderTomName(x.AstName)] = true
		case *tomast.BuildEmptyListBQTerm:
			used[renderTomName(x.AstName)] = true
		case *tomast.BuildEmptyArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.Size)
		case *tomast.BuildConsListBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildConsArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendListBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendArrayBQTerm:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.ExpressionToBQTermBQTerm:
			walkExpr(x.Exp)
		case *tomast.ListHeadBQTerm:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.ListTailBQTerm:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.VariableHeadListBQTerm:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Begin)
			walkBQ(x.End)
		case *tomast.VariableHeadArrayBQTerm:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Subject)
			walkBQ(x.BeginIndex)
			walkBQ(x.EndIndex)
		case *tomast.SymbolOfBQTerm:
			walkBQ(x.GroundTerm)
		case *tomast.CompositeBQTerm:
			for _, m := range x.Slots {
				if bm, ok := m.(*tomast.CompositeBQTermCompositeMember); ok {
					walkBQ(bm.Term)
				}
			}
		}
	}
	walkExpr = func(expr tomast.Expression) {
		switch x := expr.(type) {
		case *tomast.CodeExpression:
			scanReferences(x.Code, used)
		case *tomast.IsFsymExpression:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.Variable)
		case *tomast.IsSortExpression:
			walkBQ(x.Variable)
		case *tomast.EqualBQTermExpression:
			walkBQ(x.T1)
			walkBQ(x.T2)
		case *tomast.CastExpression:
			walkExpr(x.Source)
		case *tomast.NegationExpression:
			walkExpr(x.Arg)
		case *tomast.AndExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.OrExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.BQTermToExpressionExpression:
			walkBQ(x.AstTerm)
		case *tomast.GetSlotExpression:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.Variable)
		case *tomast.GetSizeExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetElementExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
			walkBQ(x.Index)
		case *tomast.GetSliceArrayExpression:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.SubjectListName)
			walkBQ(x.VariableBeginAST)
			walkBQ(x.VariableEndAST)
		case *tomast.AddOneExpression:
			walkBQ(x.Variable)
		case *tomast.IsEmptyArrayExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
			walkBQ(x.Index)
		case *tomast.IsEmptyListExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetHeadExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetTailExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetSliceListExpression:
			used[renderTomName(x.AstName)] = true
			walkBQ(x.VariableBeginAST)
			walkBQ(x.VariableEndAST)
			walkBQ(x.Tail)
		case *tomast.ConditionalExpression:
			walkExpr(x.Cond)
			walkExpr(x.Then)
			walkExpr(x.Else)
		case *tomast.GreaterThanExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.LessThanExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.GreaterOrEqualThanExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.LessOrEqualThanExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		}
	}
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
		case *tomast.UnamedBlockInstruction:
			if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
				for _, inner := range list.Slots {
					walkInstr(inner)
				}
			}
		case *tomast.CodeToInstructionInstruction:
			walkCode(x.Code)
		case *tomast.IfInstruction:
			walkExpr(x.Condition)
			walkInstr(x.SuccesInst)
			walkInstr(x.FailureInst)
		case *tomast.CompiledMatchInstruction:
			walkInstr(x.AutomataInst)
		case *tomast.CompiledPatternInstruction:
			walkInstr(x.AutomataInst)
		case *tomast.LetInstruction:
			walkExpr(x.Source)
			walkInstr(x.AstInstruction)
		case *tomast.LetRefInstruction:
			walkExpr(x.Source)
			walkInstr(x.AstInstruction)
		case *tomast.AssignInstruction:
			walkExpr(x.Source)
		case *tomast.AssignArrayInstruction:
			walkExpr(x.Source)
		case *tomast.DoWhileInstruction:
			walkInstr(x.DoInst)
			walkExpr(x.Condition)
		case *tomast.WhileDoInstruction:
			walkExpr(x.Condition)
			walkInstr(x.DoInst)
		case *tomast.ReturnInstruction:
			walkBQ(x.Kid1)
		case *tomast.BQTermToInstructionInstruction:
			walkBQ(x.Tom)
		}
	}
	for _, c := range list.Slots {
		walkCode(c)
	}
	delete(used, "")
	return used
}

// scanReferences finds helper-call substrings in `text` (the synthetic
// names the compiler emits into TL/Code chunks) and records each
// referenced operator name in `used`. Prefixes that carry a trailing
// `_<discriminator>` (slot name, codomain sort name) require stripping
// after extracting the identifier run.
func scanReferences(text string, used map[string]bool) {
	// Prefixes whose immediate identifier IS the operator name.
	plainPrefixes := []string{
		"tom_is_fun_sym_",
		"tom_make_",
		"tom_empty_array_",
		"tom_cons_array_",
		"tom_get_slice_",
		"tom_append_array_",
		"tom_empty_list_",
		"tom_cons_list_",
		"tom_append_list_",
	}
	// Prefixes of shape `<prefix><op>_<sort-or-slot>` — we strip the
	// trailing `_<X>` (after the last underscore) to recover just `<op>`.
	suffixedPrefixes := []string{
		"tom_get_slot_",
		"tom_get_element_",
		"tom_get_size_",
		"tom_get_head_",
		"tom_get_tail_",
		"tom_is_empty_",
	}
	scan := func(prefix string, strip bool) {
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
			if strip {
				if u := strings.LastIndex(name, "_"); u > 0 {
					name = name[:u]
				}
			}
			used[name] = true
			i = end
		}
	}
	for _, p := range plainPrefixes {
		scan(p, false)
	}
	for _, p := range suffixedPrefixes {
		scan(p, true)
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

// slotTLTypeFor returns the target-language type of slot `slotName`
// on symbol `s`, looked up via the symbol's Domain (parallel to the
// slot's position in PairNameDeclList). Returns "" when unknown.
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

// bqVarName extracts the variable name of a `BQVariable` term. Hook
// parameters reach the backend wrapped this way (see
// `makeHookBQVariable` in the parser).
func bqVarName(t tomast.BQTerm) string {
	if v, ok := t.(*tomast.BQVariableBQTerm); ok {
		return nameString(v.AstName)
	}
	return ""
}

// elemTLFor returns the target-language type of the operator's
// element sort (i.e. its single Domain entry for a `%oparray` / `%oplist`
// symbol). Falls back to the codomain TL when the element type can't
// be resolved.
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

// codomainSortName returns the SORT name (not TL!) of the operator's
// codomain. Used as the trailing discriminator on
// `tom_get_element_<op>_<sort>` / `tom_get_size_<op>_<sort>` helpers
// (matching Java's convention).
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
