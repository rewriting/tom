// backend_go — IR + Expression + BQTerm lowering to Go syntax.

package backend_go

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
)

// emitIRInstruction handles the post-Compiler shapes the new
// compiler emits (Let / LetRef / Assign / Return / DoWhile / Cast /
// IsSort / IsFsym / GetSlot / …). Returns (handled, err); the
// caller (emitInstruction) falls through to legacy cases on false.
func (e *emitter) emitIRInstruction(i tomast.Instruction) (bool, error) {
	switch x := i.(type) {
	case *tomast.CompiledMatchInstruction:
		e.buf.WriteString("{")
		if err := e.emitInstruction(x.AutomataInst); err != nil {
			return true, err
		}
		e.buf.WriteString("}\n")
		return true, nil

	case *tomast.CompiledPatternInstruction:
		return true, e.emitInstruction(x.AutomataInst)

	case *tomast.LetInstruction:
		varName := renderTomName(bqTermName(x.Variable))
		typeTL := tlTypeOfBQTerm(x.Variable, e.symbols)
		fmt.Fprintf(&e.buf, " var %s %s = ", varName, typeTL)
		e.emitExpression(x.Source)
		e.buf.WriteString("; _ = ")
		e.buf.WriteString(varName)
		e.buf.WriteString("; ")
		return true, e.emitInstruction(x.AstInstruction)

	case *tomast.LetRefInstruction:
		varName := renderTomName(bqTermName(x.Variable))
		typeTL := tlTypeOfBQTerm(x.Variable, e.symbols)
		fmt.Fprintf(&e.buf, " var %s %s = ", varName, typeTL)
		e.emitExpression(x.Source)
		e.buf.WriteString("; _ = ")
		e.buf.WriteString(varName)
		e.buf.WriteString("; ")
		return true, e.emitInstruction(x.AstInstruction)

	case *tomast.AssignInstruction:
		// Leading newline ensures the assignment is its own
		// statement when it follows a closing `}` (Go's
		// auto-`;` insertion needs end-of-line after `}`).
		varName := renderTomName(bqTermName(x.Variable))
		fmt.Fprintf(&e.buf, "\n%s = ", varName)
		e.emitExpression(x.Source)
		e.buf.WriteString(";")
		return true, nil

	case *tomast.ReturnInstruction:
		e.buf.WriteString("return ")
		e.emitBQTerm(x.Kid1)
		e.buf.WriteString(";")
		return true, nil

	case *tomast.BQTermToInstructionInstruction:
		// Bare backquote term in body — let the surrounding TL
		// chunks contribute punctuation.
		e.emitBQTerm(x.Tom)
		return true, nil

	case *tomast.RawActionInstruction:
		return true, e.emitInstruction(x.AstInstruction)

	case *tomast.DoWhileInstruction:
		// Go has no do/while — use `for { body; if !cond { break } }`.
		// Mirrors `do { body } while(cond);` semantics: body runs at
		// least once, condition checked at end of each iteration.
		e.buf.WriteString("for {")
		if err := e.emitInstruction(x.DoInst); err != nil {
			return true, err
		}
		e.buf.WriteString(" if !(")
		e.emitExpression(x.Condition)
		e.buf.WriteString(") { break }}\n")
		return true, nil

	case *tomast.WhileDoInstruction:
		e.buf.WriteString("for ")
		e.emitExpression(x.Condition)
		e.buf.WriteString(" {")
		if err := e.emitInstruction(x.DoInst); err != nil {
			return true, err
		}
		e.buf.WriteString("}\n")
		return true, nil

	case *tomast.AssignArrayInstruction:
		// `var[idx] = expr` — Go syntax matches Java.
		varName := renderTomName(bqTermName(x.Variable))
		fmt.Fprintf(&e.buf, "%s[", varName)
		e.emitBQTerm(x.Index)
		e.buf.WriteString("] = ")
		e.emitExpression(x.Source)
		e.buf.WriteString(";")
		return true, nil
	}
	return false, nil
}

// emitExpression renders an Expression as a Go expression.
func (e *emitter) emitExpression(expr tomast.Expression) {
	switch x := expr.(type) {
	case *tomast.CodeExpression:
		e.buf.WriteString(x.Code)
	case *tomast.BQTermToExpressionExpression:
		e.emitBQTerm(x.AstTerm)
	case *tomast.IsSortExpression:
		sort := tomTypeSortName(x.AstType)
		e.buf.WriteString("tom_is_sort_")
		e.buf.WriteString(sort)
		e.buf.WriteString("(")
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.IsFsymExpression:
		name := renderTomName(x.AstName)
		e.buf.WriteString("tom_is_fun_sym_")
		e.buf.WriteString(name)
		e.buf.WriteString("(")
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.EqualBQTermExpression:
		sort := tomTypeSortName(x.TomType)
		e.buf.WriteString("tom_equal_term_")
		e.buf.WriteString(sort)
		e.buf.WriteString("(")
		e.emitBQTerm(x.T1)
		e.buf.WriteString(", ")
		e.emitBQTerm(x.T2)
		e.buf.WriteString(")")
	case *tomast.CastExpression:
		// Go-style cast/conversion. For interface→concrete we'd
		// need `.(T)`, but Peano's cast is `term -> *Term` which is
		// already the concrete type — emit the underlying expr and
		// rely on Go's untyped propagation. Mirrors Java's identity
		// cast at the same shape.
		e.emitExpression(x.Source)
	case *tomast.TrueTLExpression:
		e.buf.WriteString("true")
	case *tomast.FalseTLExpression:
		e.buf.WriteString("false")
	case *tomast.BottomExpression:
		// Untyped zero value. For `*Term` (pointer) → `nil`. For
		// int → `0`. For bool → `false`.
		tl := tomTypeTL(x.TomType, e.symbols)
		switch tl {
		case "int", "int64", "int32", "float32", "float64", "byte", "rune":
			e.buf.WriteString("0")
		case "bool":
			e.buf.WriteString("false")
		default:
			e.buf.WriteString("nil")
		}
	case *tomast.NegationExpression:
		e.buf.WriteString("!(")
		e.emitExpression(x.Arg)
		e.buf.WriteString(")")
	case *tomast.AndExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" && ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	case *tomast.OrExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" || ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	case *tomast.GetSlotExpression:
		opName := renderTomName(x.AstName)
		fmt.Fprintf(&e.buf, "tom_get_slot_%s_%s(", opName, x.SlotNameString)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.IntegerExpression:
		fmt.Fprintf(&e.buf, "%d", x.Value)
	case *tomast.AddOneExpression:
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(" + 1")
	case *tomast.GetSizeExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_size_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.GetElementExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_element_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(",")
		e.emitBQTerm(x.Index)
		e.buf.WriteString(")")
	case *tomast.GetSliceArrayExpression:
		opName := renderTomName(x.AstName)
		fmt.Fprintf(&e.buf, "tom_get_slice_%s(", opName)
		e.emitBQTerm(x.SubjectListName)
		e.buf.WriteString(",")
		e.emitBQTerm(x.VariableBeginAST)
		e.buf.WriteString(",")
		e.emitBQTerm(x.VariableEndAST)
		e.buf.WriteString(")")
	case *tomast.IsEmptyArrayExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "(")
		e.emitBQTerm(x.Index)
		fmt.Fprintf(&e.buf, " >= tom_get_size_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString("))")
	case *tomast.IsEmptyListExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_is_empty_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.GetHeadExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_head_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.GetTailExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_tail_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.GetSliceListExpression:
		opName := renderTomName(x.AstName)
		fmt.Fprintf(&e.buf, "tom_get_slice_%s(", opName)
		e.emitBQTerm(x.VariableBeginAST)
		e.buf.WriteString(",")
		e.emitBQTerm(x.VariableEndAST)
		e.buf.WriteString(",")
		e.emitBQTerm(x.Tail)
		e.buf.WriteString(")")
	case *tomast.GreaterThanExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" > ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	case *tomast.GreaterOrEqualThanExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" >= ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	case *tomast.LessThanExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" < ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	case *tomast.LessOrEqualThanExpression:
		e.buf.WriteString("(")
		e.emitExpression(x.Arg1)
		e.buf.WriteString(" <= ")
		e.emitExpression(x.Arg2)
		e.buf.WriteString(")")
	default:
		fmt.Fprintf(&e.buf, "/* backend_go: unsupported expression %T */", x)
	}
}

// emitBQTerm renders a BQTerm (e.g. as a Go expression).
func (e *emitter) emitBQTerm(t tomast.BQTerm) {
	switch x := t.(type) {
	case *tomast.BQVariableBQTerm:
		e.buf.WriteString(renderTomName(x.AstName))
	case *tomast.BQVariableStarBQTerm:
		e.buf.WriteString(renderTomName(x.AstName))
	case *tomast.FunctionCallBQTerm:
		fmt.Fprintf(&e.buf, "%s(", renderTomName(x.AstName))
		e.emitBQTermArgList(x.Args)
		e.buf.WriteString(")")
	case *tomast.BuildTermBQTerm:
		fmt.Fprintf(&e.buf, "tom_make_%s(", renderTomName(x.AstName))
		e.emitBQTermArgList(x.Args)
		e.buf.WriteString(")")
	case *tomast.BuildEmptyArrayBQTerm:
		fmt.Fprintf(&e.buf, "tom_empty_array_%s(", renderTomName(x.AstName))
		e.emitBQTerm(x.Size)
		e.buf.WriteString(")")
	case *tomast.BuildConsArrayBQTerm:
		fmt.Fprintf(&e.buf, "tom_cons_array_%s(", renderTomName(x.AstName))
		e.emitBQTerm(x.HeadTerm)
		e.buf.WriteString(",")
		e.emitBQTerm(x.TailTerm)
		e.buf.WriteString(")")
	case *tomast.BuildAppendArrayBQTerm:
		fmt.Fprintf(&e.buf, "tom_append_array_%s(", renderTomName(x.AstName))
		e.emitBQTerm(x.HeadTerm)
		e.buf.WriteString(",")
		e.emitBQTerm(x.TailTerm)
		e.buf.WriteString(")")
	case *tomast.BuildEmptyListBQTerm:
		fmt.Fprintf(&e.buf, "tom_empty_list_%s()", renderTomName(x.AstName))
	case *tomast.BuildConsListBQTerm:
		fmt.Fprintf(&e.buf, "tom_cons_list_%s(", renderTomName(x.AstName))
		e.emitBQTerm(x.HeadTerm)
		e.buf.WriteString(",")
		e.emitBQTerm(x.TailTerm)
		e.buf.WriteString(")")
	case *tomast.BuildAppendListBQTerm:
		fmt.Fprintf(&e.buf, "tom_append_list_%s(", renderTomName(x.AstName))
		e.emitBQTerm(x.HeadTerm)
		e.buf.WriteString(",")
		e.emitBQTerm(x.TailTerm)
		e.buf.WriteString(")")
	case *tomast.BQApplBQTerm:
		// Pre-typer raw shape — fall back to a direct call if
		// the symbol isn't in the table.
		name := renderTomName(x.AstName)
		if e.symbols != nil {
			if _, known := e.symbols.Symbols[name]; known {
				fmt.Fprintf(&e.buf, "tom_make_%s(", name)
				e.emitBQTermArgList(x.Args)
				e.buf.WriteString(")")
				break
			}
		}
		fmt.Fprintf(&e.buf, "%s(", name)
		e.emitBQTermArgList(x.Args)
		e.buf.WriteString(")")
	case *tomast.BuildConstantBQTerm:
		e.buf.WriteString(renderTomName(x.AstName))
	case *tomast.ExpressionToBQTermBQTerm:
		e.emitExpression(x.Exp)
	case *tomast.CompositeBQTerm:
		for _, m := range x.Slots {
			switch s := m.(type) {
			case *tomast.CompositeTLCompositeMember:
				if tl, ok := s.Tl.(*tomast.TLTargetLanguage); ok {
					e.buf.WriteString(tl.Code)
				} else if itl, ok := s.Tl.(*tomast.ITLTargetLanguage); ok {
					e.buf.WriteString(itl.Code)
				}
			case *tomast.CompositeBQTermCompositeMember:
				e.emitBQTerm(s.Term)
			}
		}
	default:
		fmt.Fprintf(&e.buf, "/* backend_go: unsupported bqterm %T */", x)
	}
}

func (e *emitter) emitBQTermArgList(l tomast.BQTermList) {
	conc, ok := l.(*tomast.ConcBQTermBQTermList)
	if !ok {
		return
	}
	for i, a := range conc.Slots {
		if i > 0 {
			e.buf.WriteString(",")
		}
		e.emitBQTerm(a)
	}
}

// emitMakeBody renders the structured body of a `%op` `make` hook
// (TL chunks + BQTermToInstruction children). Same shape as the
// Java backend's version.
func (e *emitter) emitMakeBody(i tomast.Instruction, names []string) {
	switch x := i.(type) {
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, item := range list.Slots {
				e.emitMakeBody(item, names)
			}
		}
	case *tomast.CodeToInstructionInstruction:
		if tl, ok := x.Code.(*tomast.TargetLanguageToCodeCode); ok {
			var text string
			switch t := tl.Tl.(type) {
			case *tomast.TLTargetLanguage:
				text = t.Code
			case *tomast.ITLTargetLanguage:
				text = t.Code
			}
			e.buf.WriteString(substituteDollarVars(text, names))
		}
	case *tomast.BQTermToInstructionInstruction:
		e.emitBQTerm(x.Tom)
	case *tomast.ExpressionToInstructionInstruction:
		if c, ok := x.Expr.(*tomast.CodeExpression); ok {
			e.buf.WriteString(substituteDollarVars(c.Code, names))
		}
	}
}

// substituteDollarVars replaces both `$<argname>` (raw form kept by
// the parser when the body has no backquote) AND `{N}` (positional
// form `abstractCode` produces) with the corresponding arg name.
// The parser uses one OR the other depending on the hook kind, so
// this is the safe union.
func substituteDollarVars(text string, names []string) string {
	for i, name := range names {
		text = strings.ReplaceAll(text, "$"+name, name)
		text = strings.ReplaceAll(text, fmt.Sprintf("{%d}", i), name)
	}
	return text
}
