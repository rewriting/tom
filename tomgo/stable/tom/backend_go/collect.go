// backend_go — used-symbol collection (dead-helper elimination).
//
// Walks the AST once before emission and records every operator
// name referenced via the IR shapes or via raw helper-call text in
// TL chunks. Helpers for symbols outside this set are skipped —
// same convention as the Java backend.

package backend_go

import (
	"strings"

	"tom/tomgo/stable/library/tomast"
)

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
		case *tomast.IsEmptyArrayExpression:
			used[renderTomName(x.Opname)] = true
			walkBQ(x.Variable)
			walkBQ(x.Index)
		case *tomast.AddOneExpression:
			walkBQ(x.Variable)
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
		case *tomast.ReturnInstruction:
			walkBQ(x.Kid1)
		case *tomast.BQTermToInstructionInstruction:
			walkBQ(x.Tom)
		case *tomast.RawActionInstruction:
			walkInstr(x.AstInstruction)
		case *tomast.DoWhileInstruction:
			walkInstr(x.DoInst)
			walkExpr(x.Condition)
		case *tomast.WhileDoInstruction:
			walkExpr(x.Condition)
			walkInstr(x.DoInst)
		case *tomast.AssignArrayInstruction:
			walkExpr(x.Source)
		}
	}
	for _, c := range list.Slots {
		walkCode(c)
	}
	delete(used, "")
	return used
}

// scanReferences finds `tom_<prefix>_<op>` substrings in TL text
// and records `<op>` in `used`. Mirrors the Java backend's scanner.
func scanReferences(text string, used map[string]bool) {
	plainPrefixes := []string{
		"tom_is_fun_sym_",
		"tom_make_",
		"tom_empty_array_",
		"tom_cons_array_",
		"tom_get_slice_",
		"tom_append_array_",
	}
	suffixedPrefixes := []string{
		"tom_get_slot_",
		"tom_get_element_",
		"tom_get_size_",
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
