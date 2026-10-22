// Package backend_shared — used-symbol collection.
//
// CollectUsedSymbols walks the AST once before emission and records
// every operator name referenced via the IR shapes or via raw
// helper-call text in TL chunks. The set drives dead-helper
// elimination — symbols outside it get no `tom_X` helpers emitted.
//
// The set of helper-call prefixes is supplied by the caller (each
// backend declares what it emits), so this collector is fully
// shared between targets.

package backend_shared

import (
	"strings"

	"tom/tomgo/stable/library/tomast"
)

// HelperPrefixes lists the `tom_<prefix>_<…>` shapes a backend
// emits. `Plain` prefixes have the operator name immediately
// after the prefix; `Suffixed` prefixes have a trailing
// `_<sort-or-slot>` after the op name, which the scanner strips.
type HelperPrefixes struct {
	Plain    []string
	Suffixed []string
}

// DefaultHelperPrefixes returns the union of all helper-call
// shapes the Java and Go backends emit. Each backend can also
// pass a tighter set if it wants — the union is safe (extra
// entries just look at non-existent identifiers in TL chunks).
func DefaultHelperPrefixes() HelperPrefixes {
	return HelperPrefixes{
		Plain: []string{
			"tom_is_fun_sym_",
			"tom_make_",
			"tom_empty_array_",
			"tom_cons_array_",
			"tom_get_slice_",
			"tom_append_array_",
			"tom_empty_list_",
			"tom_cons_list_",
			"tom_append_list_",
		},
		Suffixed: []string{
			"tom_get_slot_",
			"tom_get_element_",
			"tom_get_size_",
			"tom_get_head_",
			"tom_get_tail_",
			"tom_is_empty_",
		},
	}
}

// CollectUsedSymbols walks the AST and returns the set of operator
// names referenced via any IR shape or TL chunk. Same set every
// backend uses — the per-target diff is only in the helper-
// declaration syntax, not in which symbols are reachable.
func CollectUsedSymbols(list *tomast.ConcCodeCodeList, prefixes HelperPrefixes) map[string]bool {
	used := map[string]bool{}
	var walkCode func(c tomast.Code)
	var walkInstr func(i tomast.Instruction)
	var walkExpr func(e tomast.Expression)
	var walkBQ func(b tomast.BQTerm)

	walkBQ = func(b tomast.BQTerm) {
		switch x := b.(type) {
		case *tomast.BuildTermBQTerm:
			used[RenderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.FunctionCallBQTerm:
			used[RenderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.BuildConstantBQTerm:
			used[RenderTomName(x.AstName)] = true
		case *tomast.BuildEmptyListBQTerm:
			used[RenderTomName(x.AstName)] = true
		case *tomast.BuildEmptyArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.Size)
		case *tomast.BuildConsListBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildConsArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendListBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.ExpressionToBQTermBQTerm:
			walkExpr(x.Exp)
		case *tomast.ListHeadBQTerm:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.ListTailBQTerm:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.VariableHeadListBQTerm:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Begin)
			walkBQ(x.End)
		case *tomast.VariableHeadArrayBQTerm:
			used[RenderTomName(x.Opname)] = true
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
			ScanReferences(x.Code, used, prefixes)
		case *tomast.IsFsymExpression:
			used[RenderTomName(x.AstName)] = true
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
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.Variable)
		case *tomast.GetSizeExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetElementExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
			walkBQ(x.Index)
		case *tomast.GetSliceArrayExpression:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.SubjectListName)
			walkBQ(x.VariableBeginAST)
			walkBQ(x.VariableEndAST)
		case *tomast.IsEmptyArrayExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
			walkBQ(x.Index)
		case *tomast.IsEmptyListExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetHeadExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetTailExpression:
			used[RenderTomName(x.Opname)] = true
			walkBQ(x.Variable)
		case *tomast.GetSliceListExpression:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.VariableBeginAST)
			walkBQ(x.VariableEndAST)
			walkBQ(x.Tail)
		case *tomast.AddOneExpression:
			walkBQ(x.Variable)
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
				ScanReferences(tl.Code, used, prefixes)
			} else if itl, ok := x.Tl.(*tomast.ITLTargetLanguage); ok {
				ScanReferences(itl.Code, used, prefixes)
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
		case *tomast.RawActionInstruction:
			walkInstr(x.AstInstruction)
		}
	}
	for _, c := range list.Slots {
		walkCode(c)
	}
	delete(used, "")
	return used
}

// ScanReferences finds `tom_<prefix>_<op>` substrings in `text` and
// adds `<op>` to `used`. Used to pick up TL-chunk callsites the
// AST walker can't see structurally.
func ScanReferences(text string, used map[string]bool, prefixes HelperPrefixes) {
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
	for _, p := range prefixes.Plain {
		scan(p, false)
	}
	for _, p := range prefixes.Suffixed {
		scan(p, true)
	}
}

func isIdentByte(c byte) bool {
	return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_'
}

// ---------------------------------------------------------------------------
// Strategy / pattern symbol collection
// ---------------------------------------------------------------------------
//
// CollectStrategySymbols walks every `StrategyDeclaration` reachable
// from the top-level Code list and marks the symbols its emission
// will reference. The visit-rule patterns and action bodies live
// outside the main IR walk, so without this pass they'd get a dead
// `tom_make_X` / `tom_is_fun_sym_X` skipped from the helper section
// even though the strategy uses them.
//
// Structural-only (no language-specific knowledge), so shared
// between Java and Go targets.

// CollectStrategySymbols extends `used` with every operator name
// referenced by a `StrategyDeclaration` reachable from `list`.
func CollectStrategySymbols(list *tomast.ConcCodeCodeList, used map[string]bool, prefixes HelperPrefixes) {
	var walkCode func(tomast.Code)
	var walkInstr func(tomast.Instruction)
	walkCode = func(c tomast.Code) {
		switch x := c.(type) {
		case *tomast.DeclarationToCodeCode:
			if strat, ok := x.AstDeclaration.(*tomast.StrategyDeclaration); ok {
				ProcessStrategy(strat, used, prefixes)
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

// ProcessStrategy marks all symbols a strategy declaration's emit
// will reference. The `ExtendsTerm` head becomes a `tom_make_<X>()`
// call; every visit rule's action body may use `tom_is_fun_sym_<X>`,
// `tom_make_<X>`, etc.
func ProcessStrategy(strat *tomast.StrategyDeclaration, used map[string]bool, prefixes HelperPrefixes) {
	ScanBQTerm(strat.ExtendsTerm, used, prefixes)
	vl, ok := strat.VisitList.(*tomast.ConcTomVisitTomVisitList)
	if !ok {
		return
	}
	for _, v := range vl.Slots {
		vt, ok := v.(*tomast.VisitTermTomVisit)
		if !ok {
			continue
		}
		cil, ok := vt.AstConstraintInstructionList.(*tomast.ConcConstraintInstructionConstraintInstructionList)
		if !ok {
			continue
		}
		for _, ci := range cil.Slots {
			inst, ok := ci.(*tomast.ConstraintInstructionConstraintInstruction)
			if !ok {
				continue
			}
			ScanConstraint(inst.Constraint, used)
			ScanInstrForUsedSymbols(inst.Action, used, prefixes)
		}
	}
}

// ScanBQTerm marks the head op of any BQAppl/BuildTerm/… as used.
// Implemented as a wrap into a BQTermToInstruction so the full
// ScanInstrForUsedSymbols walker handles all BQTerm shapes.
func ScanBQTerm(b tomast.BQTerm, used map[string]bool, prefixes HelperPrefixes) {
	ScanInstrForUsedSymbols(tomast.MakeBQTermToInstruction(b), used, prefixes)
}

// ScanConstraint scans a constraint's pattern for op references.
func ScanConstraint(c tomast.Constraint, used map[string]bool) {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		ScanPattern(x.Pattern, used)
	case *tomast.AndConstraintConstraint:
		for _, s := range x.Slots {
			ScanConstraint(s, used)
		}
	case *tomast.OrConstraintConstraint:
		for _, s := range x.Slots {
			ScanConstraint(s, used)
		}
	}
}

// ScanPattern adds every RecordAppl / TermAppl head name to `used`.
// Recurses into sub-patterns (record slots, application args).
func ScanPattern(p tomast.TomTerm, used map[string]bool) {
	switch x := p.(type) {
	case *tomast.RecordApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok {
			for _, n := range nl.Slots {
				used[RenderTomName(n)] = true
			}
		}
		if sl, ok := x.Slots.(*tomast.ConcSlotSlotList); ok {
			for _, s := range sl.Slots {
				if pair, ok := s.(*tomast.PairSlotApplSlot); ok {
					ScanPattern(pair.Appl, used)
				}
			}
		}
	case *tomast.TermApplTomTerm:
		if nl, ok := x.NameList.(*tomast.ConcTomNameTomNameList); ok {
			for _, n := range nl.Slots {
				used[RenderTomName(n)] = true
			}
		}
		if a, ok := x.Args.(*tomast.ConcTomTermTomList); ok {
			for _, s := range a.Slots {
				ScanPattern(s, used)
			}
		}
	}
}

// ScanInstrForUsedSymbols walks an Instruction (used to scan
// MakeDecl bodies stored in the SymbolTable) and adds every
// referenced operator name to `used`. A standalone driver:
// `CollectUsedSymbols`'s closure-style walkers aren't reachable
// from this scope.
func ScanInstrForUsedSymbols(i tomast.Instruction, used map[string]bool, prefixes HelperPrefixes) {
	var walkInstr func(tomast.Instruction)
	var walkExpr func(tomast.Expression)
	var walkBQ func(tomast.BQTerm)
	walkBQ = func(b tomast.BQTerm) {
		switch x := b.(type) {
		case *tomast.BuildTermBQTerm:
			used[RenderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.FunctionCallBQTerm:
			used[RenderTomName(x.AstName)] = true
			if a, ok := x.Args.(*tomast.ConcBQTermBQTermList); ok {
				for _, arg := range a.Slots {
					walkBQ(arg)
				}
			}
		case *tomast.BuildConstantBQTerm:
			used[RenderTomName(x.AstName)] = true
		case *tomast.BuildEmptyListBQTerm:
			used[RenderTomName(x.AstName)] = true
		case *tomast.BuildEmptyArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.Size)
		case *tomast.BuildConsListBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildConsArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendListBQTerm:
			used[RenderTomName(x.AstName)] = true
			walkBQ(x.HeadTerm)
			walkBQ(x.TailTerm)
		case *tomast.BuildAppendArrayBQTerm:
			used[RenderTomName(x.AstName)] = true
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
			ScanReferences(x.Code, used, prefixes)
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
					ScanReferences(t.Code, used, prefixes)
				case *tomast.ITLTargetLanguage:
					ScanReferences(t.Code, used, prefixes)
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
