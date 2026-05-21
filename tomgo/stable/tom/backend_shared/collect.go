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
