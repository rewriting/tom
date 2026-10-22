// Package backend_go is the Go-target counterpart of stable/tom/backend.
//
// The compiler pipeline (parser → optimizer) is language-agnostic.
// Only this final phase translates the post-Compiler IR (Let / If /
// Assign / Return / Cast / IsSort / IsFsym / GetSlot / BuildTerm /
// …) to host-language source — Java in `stable/tom/backend/`, Go
// here.
//
// The output is a single `.go` source file: TL chunks pass through
// verbatim (they already contain valid Go from the `.t` file) and
// the Tom-emitted helpers + compiled matches are rendered as Go
// `func` declarations and statements.
//
// This first cut covers exactly what Peano needs:
//   - `%typeterm`        → `tom_is_sort_X` / `tom_equal_term_X`
//   - `%op`              → `tom_is_fun_sym_X` / `tom_make_X` /
//     `tom_get_slot_X_<slot>`
//   - `%match`           → Let-chained `if` blocks (via the
//     compiler's CompiledMatch shape)
//   - BuildTerm / BQVariable / Return — `%match` action bodies
//
// Variadic-list/array, %strategy, %gom, etc. are NOT yet wired up
// — Peano doesn't need them.
package backend_go

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Run is the public phase entry point. Drops the generated Go
// source into `state.Source`, leaving `state.Code` untouched (the
// other phases don't run after this).
func Run(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	code, ok := in.Code.(*tomast.TomCode)
	if !ok {
		return in, fmt.Errorf("backend_go: expected *TomCode, got %T", in.Code)
	}
	list, ok := code.CodeList.(*tomast.ConcCodeCodeList)
	if !ok {
		return in, fmt.Errorf("backend_go: expected ConcCode list")
	}
	used := collectUsedSymbols(list)
	// Also pick up symbols referenced by Strategy declarations (their
	// `extends` term + visit-rule actions reach `tom_make_Identity`,
	// `tom_make_b`, … that the structural walk can't see).
	collectStrategySymbols(list, used)
	// Transitive closure on SymbolTable `make` hook bodies: composed
	// combinators like `RepeatId` / `BottomUp` reference `mu` /
	// `MuVar` / `Sequence` / `All` via their backquote expansion,
	// which only becomes visible when we walk those bodies. Same
	// fixed-point loop as backend_java.
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

// emitCode dispatches on the AST shape of one top-level code entry.
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
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.ArraySymbolDeclDeclaration:
		return e.emitSymbolHelpers(x.AstName)
	case *tomast.StrategyDeclaration:
		return e.emitStrategy(x)
	}
	return nil
}

// emitTypeTermHelpers writes `tom_is_sort_X` / `tom_equal_term_X`
// from the `is_sort(t)` / `equals(t1, t2)` hooks. Unlike Java where
// Object is the natural universal type, Go uses the typeterm's
// `implement` TL (e.g. `*Term`) so user-written bodies like
// `$t != nil` or `$t1.Equals($t2)` type-check.
func (e *emitter) emitTypeTermHelpers(t *tomast.TypeTermDeclDeclaration) error {
	sortName := nameString(t.AstName)
	sortTL := sortName
	if e.symbols != nil {
		if body, ok := e.symbols.Sorts[sortName]; ok && strings.TrimSpace(body) != "" {
			sortTL = strings.TrimSpace(body)
		}
	}
	for _, d := range flattenDeclList(t.Declarations) {
		switch decl := d.(type) {
		case *tomast.EqualTermDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t1", "t2"})
			fmt.Fprintf(&e.buf, "\nfunc tom_equal_term_%s(t1, t2 %s) bool { return %s }\n",
				sortName, sortTL, body)
		case *tomast.IsSortDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			fmt.Fprintf(&e.buf, "\nfunc tom_is_sort_%s(t %s) bool { return %s }\n",
				sortName, sortTL, body)
		}
	}
	return nil
}

// emitSymbolHelpers writes per-operator helpers (filtered through
// `usedSymbols` — dead-code elimination, same convention as Java).
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
	codomainTL := tlTypeFor(s, e.symbols)
	// Raw bodies captured during the loop — re-used when emitting
	// the generic tom_get_slice / tom_append_array / tom_append_list
	// helpers.
	var (
		emptyArrayRaw   string
		addArrayRaw     string
		getElemRaw      string
		getSizeRaw      string
		haveEmptyArr    bool
		haveAddArr      bool
		haveGetElem     bool
		haveGetSize     bool
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
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			fmt.Fprintf(&e.buf, "\nfunc tom_is_fun_sym_%s(t %s) bool { return %s }\n",
				opName, codomainTL, body)
		case *tomast.MakeDeclDeclaration:
			params, names := makeDeclParams(decl, e.symbols)
			fmt.Fprintf(&e.buf, "\nfunc tom_make_%s(%s) %s { return ",
				opName, params, codomainTL)
			if body := codeBodyFromInstruction(decl.Instr); body != "" {
				// Body kept as raw text by the parser (no backquote
				// inside). Substitute `$<argname>` → `<argname>`.
				e.buf.WriteString(substituteDollarVars(body, names))
			} else {
				e.emitMakeBody(decl.Instr, names)
			}
			e.buf.WriteString(" }\n")
		case *tomast.GetSlotDeclDeclaration:
			body := substituteDollarVars(codeBodyFromExpr(decl.Expr), []string{"t"})
			slotName := nameString(decl.SlotName)
			slotTL := slotTLTypeFor(s, slotName, e.symbols)
			if slotTL == "" {
				slotTL = codomainTL
			}
			fmt.Fprintf(&e.buf, "\nfunc tom_get_slot_%s_%s(t %s) %s { return %s }\n",
				opName, slotName, codomainTL, slotTL, body)
		case *tomast.MakeEmptyArrayDeclaration:
			emptyArrayRaw = codeBodyFromInstruction(decl.Instr)
			haveEmptyArr = true
			sizeName := bqVarName(decl.VarSize)
			if sizeName == "" {
				sizeName = "n"
			}
			body := substituteDollarVars(emptyArrayRaw, []string{sizeName})
			fmt.Fprintf(&e.buf, "\nfunc tom_empty_array_%s(%s int) %s { return %s }\n",
				opName, sizeName, codomainTL, body)
		case *tomast.MakeAddArrayDeclaration:
			addArrayRaw = codeBodyFromInstruction(decl.Instr)
			haveAddArr = true
			eltName := bqVarName(decl.VarElt)
			listName := bqVarName(decl.VarList)
			body := substituteDollarVars(addArrayRaw, []string{eltName, listName})
			elemTL := elemTLFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "\nfunc tom_cons_array_%s(%s %s, %s %s) %s { return %s }\n",
				opName, eltName, elemTL, listName, codomainTL, codomainTL, body)
		case *tomast.GetElementDeclDeclaration:
			getElemRaw = codeBodyFromExpr(decl.Expr)
			haveGetElem = true
			listName := bqVarName(decl.Variable)
			indexName := bqVarName(decl.Index)
			body := substituteDollarVars(getElemRaw, []string{listName, indexName})
			elemTL := elemTLFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "\nfunc tom_get_element_%s_%s(%s %s, %s int) %s { return %s }\n",
				opName, codomainSort, listName, codomainTL, indexName, elemTL, body)
		case *tomast.GetSizeDeclDeclaration:
			getSizeRaw = codeBodyFromExpr(decl.Expr)
			haveGetSize = true
			listName := bqVarName(decl.Variable)
			body := substituteDollarVars(getSizeRaw, []string{listName})
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "\nfunc tom_get_size_%s_%s(%s %s) int { return %s }\n",
				opName, codomainSort, listName, codomainTL, body)
		case *tomast.MakeEmptyListDeclaration:
			emptyListRaw = codeBodyFromInstruction(decl.Instr)
			haveEmptyList = true
			if strings.TrimSpace(emptyListRaw) != "" {
				fmt.Fprintf(&e.buf, "\nfunc tom_empty_list_%s() %s { return %s }\n",
					opName, codomainTL, emptyListRaw)
			}
		case *tomast.MakeAddListDeclaration:
			addListRaw = codeBodyFromInstruction(decl.Instr)
			haveAddList = true
			eltName := bqVarName(decl.VarElt)
			listName := bqVarName(decl.VarList)
			body := substituteDollarVars(addListRaw, []string{eltName, listName})
			elemTL := elemTLFor(s, e.symbols)
			fmt.Fprintf(&e.buf, "\nfunc tom_cons_list_%s(%s %s, %s %s) %s { return %s }\n",
				opName, eltName, elemTL, listName, codomainTL, codomainTL, body)
		case *tomast.GetHeadDeclDeclaration:
			getHeadRaw = codeBodyFromExpr(decl.Expr)
			haveGetHead = true
			listName := bqVarName(decl.Variable)
			body := substituteDollarVars(getHeadRaw, []string{listName})
			elemTL := elemTLFor(s, e.symbols)
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "\nfunc tom_get_head_%s_%s(%s %s) %s { return %s }\n",
				opName, codomainSort, listName, codomainTL, elemTL, body)
		case *tomast.GetTailDeclDeclaration:
			getTailRaw = codeBodyFromExpr(decl.Expr)
			haveGetTail = true
			listName := bqVarName(decl.Variable)
			body := substituteDollarVars(getTailRaw, []string{listName})
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "\nfunc tom_get_tail_%s_%s(%s %s) %s { return %s }\n",
				opName, codomainSort, listName, codomainTL, codomainTL, body)
		case *tomast.IsEmptyDeclDeclaration:
			isEmptyRaw = codeBodyFromExpr(decl.Expr)
			haveIsEmptyList = true
			listName := bqVarName(decl.Variable)
			body := substituteDollarVars(isEmptyRaw, []string{listName})
			codomainSort := codomainSortName(s)
			fmt.Fprintf(&e.buf, "\nfunc tom_is_empty_%s_%s(%s %s) bool { return %s }\n",
				opName, codomainSort, listName, codomainTL, body)
		}
	}

	if haveEmptyArr && haveAddArr && haveGetElem && haveGetSize {
		e.emitGenericArrayHelpers(opName, codomainTL, emptyArrayRaw, addArrayRaw, getElemRaw, getSizeRaw)
	}
	if haveEmptyList && haveAddList && haveGetHead && haveGetTail && haveIsEmptyList {
		e.emitGenericListHelpers(s, opName, codomainTL, emptyListRaw, addListRaw, getHeadRaw, getTailRaw, isEmptyRaw)
	}
	return nil
}

// emitGenericArrayHelpers writes `tom_get_slice_<op>` and
// `tom_append_array_<op>` for an `%oparray` symbol after its
// primitive hooks have been emitted. Mirrors the Java backend's
// helper of the same name with Go syntax (no `private static`,
// `for` instead of `while`, `:=` for short-decl).
func (e *emitter) emitGenericArrayHelpers(opName, codomainTL, emptyArrayRaw, addArrayRaw, getElemRaw, getSizeRaw string) {
	// tom_get_slice_<op>(subject, begin, end): cons-loop building
	// `result` by appending `subject[begin]` until `begin == end`.
	emptySliceBody := substitutePositional(emptyArrayRaw, []string{"end-begin"})
	sliceGetElem := substitutePositional(getElemRaw, []string{"subject", "begin"})
	sliceAddBody := substitutePositional(addArrayRaw, []string{sliceGetElem, "result"})
	fmt.Fprintf(&e.buf,
		"\nfunc tom_get_slice_%s(subject %s, begin int, end int) %s { var result %s = %s; for begin != end { result = %s; begin++ }; return result }\n",
		opName, codomainTL, codomainTL, codomainTL, emptySliceBody, sliceAddBody)

	// tom_append_array_<op>(l2, l1): copy l1 then l2 into a fresh
	// slice. Builds via the same make_empty + cons pipeline.
	appSize1 := substitutePositional(getSizeRaw, []string{"l1"})
	appSize2 := substitutePositional(getSizeRaw, []string{"l2"})
	appEmpty := substitutePositional(emptyArrayRaw, []string{"size1+size2"})
	appGetElemL1 := substitutePositional(getElemRaw, []string{"l1", "size1-index"})
	appAddL1 := substitutePositional(addArrayRaw, []string{appGetElemL1, "result"})
	appGetElemL2 := substitutePositional(getElemRaw, []string{"l2", "size2-index"})
	appAddL2 := substitutePositional(addArrayRaw, []string{appGetElemL2, "result"})
	fmt.Fprintf(&e.buf,
		"\nfunc tom_append_array_%s(l2 %s, l1 %s) %s { size1 := %s; size2 := %s; var result %s = %s; index := size1; for index > 0 { result = %s; index-- }; index = size2; for index > 0 { result = %s; index-- }; return result }\n",
		opName, codomainTL, codomainTL, codomainTL, appSize1, appSize2, codomainTL, appEmpty, appAddL1, appAddL2)
}

// emitGenericListHelpers writes `tom_append_list_<op>` and
// `tom_get_slice_<op>` for an `%oplist` symbol. The list flavour
// uses head/tail recursion driven by `is_empty` checks; equality
// of L values goes through the typeterm's `tom_equal_term_<sort>`
// helper so the comparison stays portable.
func (e *emitter) emitGenericListHelpers(s *tomast.SymbolTomSymbol, opName, codomainTL, emptyListRaw, addListRaw, getHeadRaw, getTailRaw, isEmptyRaw string) {
	isEmptyL1 := substitutePositional(isEmptyRaw, []string{"l1"})
	isEmptyL2 := substitutePositional(isEmptyRaw, []string{"l2"})
	getTailL1 := substitutePositional(getTailRaw, []string{"l1"})
	isEmptyTailL1 := substitutePositional(isEmptyRaw, []string{getTailL1})
	isEmptyEnd := substitutePositional(isEmptyRaw, []string{"end"})
	getHeadL1 := substitutePositional(getHeadRaw, []string{"l1"})
	getHeadBegin := substitutePositional(getHeadRaw, []string{"begin"})
	getTailBegin := substitutePositional(getTailRaw, []string{"begin"})
	consHeadOntoL2 := substitutePositional(addListRaw, []string{getHeadL1, "l2"})
	consAppendRec := substitutePositional(addListRaw, []string{
		getHeadL1, "tom_append_list_" + opName + "(" + getTailL1 + ",l2)",
	})
	consHeadOntoRec := substitutePositional(addListRaw, []string{
		getHeadBegin, "tom_get_slice_" + opName + "(" + getTailBegin + ",end,tail)",
	})

	// tom_append_list_<op>: same recursion as the Java backend.
	fmt.Fprintf(&e.buf,
		"\nfunc tom_append_list_%s(l1 %s, l2 %s) %s { if %s { return l2 } else if %s { return l1 } else if %s { return %s } else { return %s } }\n",
		opName, codomainTL, codomainTL, codomainTL,
		isEmptyL1, isEmptyL2, isEmptyTailL1,
		consHeadOntoL2, consAppendRec,
	)

	// tom_get_slice_<op>(begin, end, tail): edge-case-aware
	// recursion. The `begin == end || (end == tail && (isEmpty(end)
	// || end == empty()))` guard short-circuits common cases.
	codomainSort := codomainSortName(s)
	fmt.Fprintf(&e.buf,
		"\nfunc tom_get_slice_%s(begin, end, tail %s) %s { if tom_equal_term_%s(begin, end) { return tail } else if tom_equal_term_%s(end, tail) && (%s || tom_equal_term_%s(end, %s)) { return begin } else { return %s } }\n",
		opName, codomainTL, codomainTL,
		codomainSort, codomainSort, isEmptyEnd, codomainSort, emptyListRaw, consHeadOntoRec,
	)
}

// substitutePositional substitutes `{N}` placeholders with the
// matching slot value. Mirrors the Java backend's helper used for
// the same purpose.
func substitutePositional(text string, vals []string) string {
	for i, v := range vals {
		text = strings.ReplaceAll(text, fmt.Sprintf("{%d}", i), v)
	}
	return text
}

// emitInstruction dispatches on the post-compile instruction shapes.
func (e *emitter) emitInstruction(i tomast.Instruction) error {
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
		// Host-language braces inside an action. In Go this becomes
		// a plain `{ ... }` scope. The trailing `\n` triggers Go's
		// auto-semicolon insertion so two sibling blocks `}{` don't
		// run together as one syntactically invalid statement.
		e.buf.WriteString("{")
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, inner := range list.Slots {
				if err := e.emitInstruction(inner); err != nil {
					return err
				}
			}
		}
		e.buf.WriteString("}\n")
	case *tomast.CodeToInstructionInstruction:
		return e.emitCode(x.Code)
	case *tomast.IfInstruction:
		// Go's `if cond { … }` — no parens around the condition.
		// The leading `\n` keeps Go's auto-`;` insertion happy when
		// the previous instruction ended on `}`.
		e.buf.WriteString("\nif ")
		e.emitExpression(x.Condition)
		e.buf.WriteString(" {")
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
		// Nothing.
	default:
		fmt.Fprintf(&e.buf, "/* backend_go: unsupported instruction %T */", x)
	}
	return nil
}
