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
package backend_java

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

	if haveEmptyArr && haveAddArr && haveGetElem && haveGetSize {
		e.emitGenericArrayHelpers(s, opName, emptyArrayRaw, addArrayRaw, getElemRaw, getSizeRaw)
	}
	if haveEmptyList && haveAddList && haveGetHead && haveGetTail && haveIsEmptyList {
		e.emitGenericListHelpers(s, opName, emptyListRaw, addListRaw, getHeadRaw, getTailRaw, isEmptyRaw)
	}
	return nil
}

// emitGenericArrayHelpers writes `tom_get_slice_<op>` and
// `tom_append_array_<op>` for an `%oparray` symbol after its
// primitive hooks have been emitted. Java's GenericGenerator
// emits the same shape for every variadic-array that survives
// dead-code elimination; the bodies just plug the per-symbol
// `make_empty` / `make_append` / `get_element` / `get_size`
// templates into the loop scaffolding.
func (e *emitter) emitGenericArrayHelpers(s *tomast.SymbolTomSymbol, opName, emptyArrayRaw, addArrayRaw, getElemRaw, getSizeRaw string) {
	codomainTL := tlTypeFor(s, e.symbols)

	// tom_get_slice_<op>(subject, begin, end): cons-loop building
	// `result` by appending `subject[begin]` until `begin == end`.
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

// emitGenericListHelpers writes `tom_append_list_<op>` and
// `tom_get_slice_<op>` for an `%oplist` symbol. The list flavour
// uses head/tail/is_empty recursion (vs the array flavour's
// indexed loops); the structure mirrors Java's GenericGenerator.
func (e *emitter) emitGenericListHelpers(s *tomast.SymbolTomSymbol, opName, emptyListRaw, addListRaw, getHeadRaw, getTailRaw, isEmptyRaw string) {
	codomainTL := tlTypeFor(s, e.symbols)

	// Pre-bake parameterised forms — each placeholder body is
	// referenced multiple times in the recursion.
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
	consAppendRec := substitutePlaceholders(addListRaw,
		[]string{getHeadL1, "tom_append_list_" + opName + "(" + getTailL1 + ",l2)"})

	// tom_append_list(l1, l2):
	//   if isEmpty(l1) → l2
	//   else if isEmpty(l2) → l1
	//   else if isEmpty(tail(l1)) → cons(head(l1), l2)
	//   else → cons(head(l1), append(tail(l1), l2))
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
	fmt.Fprintf(&e.buf,
		"   private static   %s  tom_get_slice_%s( %s  begin,  %s  end, %s  tail) {     if( begin.equals(end) ) {       return tail;     } else if( end.equals(tail)  && ( %s  ||  end.equals(%s) )) {       return begin;     }     return %s;   }",
		codomainTL, opName, codomainTL, codomainTL, codomainTL,
		isEmptyEnd, emptyListRaw, consHeadOntoRec,
	)
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
