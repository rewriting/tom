// Backend — lowering of post-Compiler IR shapes.
//
// The Java reference Compiler emits its matching automata as a tree
// of `Let / LetRef / Assign / If / DoWhile / WhileDo / Return / Nop /
// Cast / IsSort / IsFsym / BQTermToExpression / BuildTerm / FunctionCall`
// nodes nested inside `CompiledMatch(AbstractBlock(...), opts)`. This
// file teaches the Backend to render each of those.
//
// The legacy ad-hoc compiler (compiler/legacy.go) emits raw `TL`
// chunks instead, so these renderers are inactive on its output —
// the existing `emitInstruction` cases short-circuit before reaching
// the new shapes.
//
// Java reference for the rendering conventions:
//   - stable/tom/engine/prettyprinter/  (full Java back-end)
//   - tests/testdata/java-ast/<fixture>.java   (the actual `.java`
//     produced by `tom`)
//
// Coverage in this file targets the SHAPES we need for syntactic
// matches first (Peano-class fixtures). Variadic/array shapes
// (BuildEmptyArray, BuildConsArray, BuildAppendArray) are lowered
// too, since the variadic-aware Compiler will emit them.

package backend_java

import (
	"fmt"
	"strings"

	"tom/tomgo/stable/library/tomast"
)

// emitIRInstruction adds rendering for the new IR-shape instructions
// the Java reference Compiler emits. Returns true when handled,
// false to let the caller fall through to legacy cases.
func (e *emitter) emitIRInstruction(i tomast.Instruction) (bool, error) {
	switch x := i.(type) {
	case *tomast.CompiledMatchInstruction:
		// The block-of-CompiledPattern is inside AutomataInst.
		// Wrap it in a host `{ ... }` so the surrounding context
		// gets a self-contained scope (matches Java's reference,
		// see Compiler.t line 261).
		e.buf.WriteString("{")
		if err := e.emitInstruction(x.AutomataInst); err != nil {
			return true, err
		}
		e.buf.WriteString("}")
		return true, nil

	case *tomast.CompiledPatternInstruction:
		// Constraint is informational (kept for tooling); the
		// rendered code is the AutomataInst chain alone.
		return true, e.emitInstruction(x.AutomataInst)

	case *tomast.LetInstruction:
		varName := renderTomName(bqTermName(x.Variable))
		typeTL := tlTypeOfBQTerm(x.Variable, e.symbols)
		fmt.Fprintf(&e.buf, " %s %s = ", typeTL, varName)
		e.emitExpression(x.Source)
		e.buf.WriteString("; ")
		return true, e.emitInstruction(x.AstInstruction)

	case *tomast.LetRefInstruction:
		// Same Java syntax as Let — the LetRef tag is informational
		// (it says the binding is referenced later by an Assign).
		varName := renderTomName(bqTermName(x.Variable))
		typeTL := tlTypeOfBQTerm(x.Variable, e.symbols)
		fmt.Fprintf(&e.buf, " %s %s = ", typeTL, varName)
		e.emitExpression(x.Source)
		e.buf.WriteString("; ")
		return true, e.emitInstruction(x.AstInstruction)

	case *tomast.AssignInstruction:
		varName := renderTomName(bqTermName(x.Variable))
		fmt.Fprintf(&e.buf, "%s = ", varName)
		e.emitExpression(x.Source)
		e.buf.WriteString(";")
		return true, nil

	case *tomast.AssignArrayInstruction:
		varName := renderTomName(bqTermName(x.Variable))
		fmt.Fprintf(&e.buf, "%s[", varName)
		e.emitBQTerm(x.Index)
		e.buf.WriteString("] = ")
		e.emitExpression(x.Source)
		e.buf.WriteString(";")
		return true, nil

	case *tomast.DoWhileInstruction:
		e.buf.WriteString("do {")
		if err := e.emitInstruction(x.DoInst); err != nil {
			return true, err
		}
		e.buf.WriteString("} while(")
		e.emitExpression(x.Condition)
		e.buf.WriteString(");")
		return true, nil

	case *tomast.WhileDoInstruction:
		e.buf.WriteString("while(")
		e.emitExpression(x.Condition)
		e.buf.WriteString(") {")
		if err := e.emitInstruction(x.DoInst); err != nil {
			return true, err
		}
		e.buf.WriteString("}")
		return true, nil

	case *tomast.ReturnInstruction:
		e.buf.WriteString("return ")
		e.emitBQTerm(x.Kid1)
		e.buf.WriteString(";")
		return true, nil

	case *tomast.BQTermToInstructionInstruction:
		// `BQTermToInstruction` wraps a backquote `x` / `Foo(x,y)`
		// inside an AbstractBlock interleaved with TL chunks. The
		// chunks already carry punctuation (the trailing `;` of
		// `return ...;`, or the comma/paren of an arg list), so we
		// emit the BQTerm raw — no `;` added.
		e.emitBQTerm(x.Tom)
		return true, nil

	case *tomast.RawActionInstruction:
		// `RawAction(...)` is how the parser wraps a user-supplied
		// action body. The wrapper carries no extra semantics — emit
		// the inner instruction unchanged.
		return true, e.emitInstruction(x.AstInstruction)
	}
	return false, nil
}

// emitMakeBody renders the structured body of a `%op` `make` hook
// when the parser captured it as an AbstractBlock of TL chunks and
// BQTermToInstruction nodes (see parser.go `case "make"`). Used by
// `emitSymbolHelpers` to render `return <body>;` for the
// `tom_make_<op>(...)` helper. `names` is the list of arg names —
// each occurrence of `$<argname>` (or `$<index>`) in a TL chunk is
// replaced with the argument name.
func (e *emitter) emitMakeBody(i tomast.Instruction, names []string) {
	switch x := i.(type) {
	case *tomast.AbstractBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			for _, item := range list.Slots {
				e.emitMakeBody(item, names)
			}
		}
	case *tomast.UnamedBlockInstruction:
		if list, ok := x.InstList.(*tomast.ConcInstructionInstructionList); ok {
			e.buf.WriteByte('{')
			for _, item := range list.Slots {
				e.emitMakeBody(item, names)
			}
			e.buf.WriteByte('}')
		}
	case *tomast.CodeToInstructionInstruction:
		if tl, ok := x.Code.(*tomast.TargetLanguageToCodeCode); ok {
			text := ""
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
		// Legacy single-Code form — emit verbatim with placeholders.
		if c, ok := x.Expr.(*tomast.CodeExpression); ok {
			e.buf.WriteString(substituteDollarVars(c.Code, names))
		}
	}
}

// substituteDollarVars replaces both `$<argname>` and `{N}` in
// `text` with the corresponding arg name. The parser uses one OR
// the other depending on the hook kind (make hooks now keep `$var`
// raw to avoid confusing `lowerActionBody`'s block grammar; older
// callsites still apply `abstractCode` → `{N}`). Handles both.
func substituteDollarVars(text string, names []string) string {
	for i, name := range names {
		text = strings.ReplaceAll(text, "$"+name, name)
		text = strings.ReplaceAll(text, fmt.Sprintf("{%d}", i), name)
	}
	return text
}

// emitExpression renders an Expression (boolean predicate inside
// If/DoWhile/WhileDo or right-hand-side of Let/Assign) to host code.
//
// Special case: `CodeExpression` carries pre-baked host text — the
// legacy compiler emits these and we just splice the string.
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
	case *tomast.GetSlotExpression:
		opName := renderTomName(x.AstName)
		fmt.Fprintf(&e.buf, "tom_get_slot_%s_%s(", opName, x.SlotNameString)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
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
	case *tomast.AddOneExpression:
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(" + 1")
	case *tomast.IntegerExpression:
		fmt.Fprintf(&e.buf, "%d", x.Value)
	case *tomast.IsEmptyArrayExpression:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "(%s >= tom_get_size_%s_%s(", bqTermAsText(x.Index), opName, codomainSort)
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
	case *tomast.ConditionalExpression:
		e.buf.WriteString("((")
		e.emitExpression(x.Cond)
		e.buf.WriteString(")?(")
		e.emitExpression(x.Then)
		e.buf.WriteString("):(")
		e.emitExpression(x.Else)
		e.buf.WriteString("))")
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
		tl := tomTypeTL(x.AstType, e.symbols)
		fmt.Fprintf(&e.buf, "(( %s )", tl)
		e.emitExpression(x.Source)
		e.buf.WriteString(")")
	case *tomast.TrueTLExpression:
		e.buf.WriteString("true")
	case *tomast.FalseTLExpression:
		e.buf.WriteString("false")
	case *tomast.BottomExpression:
		// Bottom is the "uninitialised" default Java's reference
		// pre-decl emits for fresh variables that the disjunction
		// chain will assign in each branch. The TL-specific default
		// is what counts: null for object types, 0 for int, false
		// for boolean.
		tl := tomTypeTL(x.TomType, e.symbols)
		switch tl {
		case "int", "long", "short", "byte", "float", "double", "char":
			e.buf.WriteString("0")
		case "boolean":
			e.buf.WriteString("false")
		default:
			e.buf.WriteString("null")
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
		fmt.Fprintf(&e.buf, "/* backend: unsupported expression %T */", x)
	}
}

// emitBQTerm renders a BQTerm (the right-hand side of a Let/Assign,
// the operand of IsFsym/IsSort, an arg of a BuildTerm/FunctionCall).
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
	case *tomast.BQApplBQTerm:
		// `BQAppl` is the parser's raw shape. The Typer rewrites
		// known ops into `BuildTerm` and unknowns into
		// `FunctionCall`, but when an action body sneaks past the
		// typer (e.g. a backquoted op never declared with `%op`),
		// we land here. If the symbol is known we still emit the
		// `tom_make_<op>` helper; otherwise we fall back to a
		// direct Java call (matches what Java's PrettyPrinter does
		// for un-typed BQAppls).
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
		// Java's reference renders a bare BuildConstant as the host
		// identifier of the same name — most often a static field
		// the user declared (e.g. `tzero` for Peano).
		e.buf.WriteString(renderTomName(x.AstName))
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
	case *tomast.ExpressionToBQTermBQTerm:
		// Java wraps an Expression in this BQTerm node when it needs
		// to put an expression where a BQTerm is expected (e.g.
		// inside `IsFsym(name, ExpressionToBQTerm(Cast(...)))`). The
		// host syntax is just the expression itself.
		e.emitExpression(x.Exp)
	case *tomast.ListHeadBQTerm:
		// Should normally be rewritten to GetHead by the compiler
		// — fall back to emit it as a helper call.
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_head_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.ListTailBQTerm:
		opName := renderTomName(x.Opname)
		codomainSort := codomainSortFor(e.symbols, opName)
		fmt.Fprintf(&e.buf, "tom_get_tail_%s_%s(", opName, codomainSort)
		e.emitBQTerm(x.Variable)
		e.buf.WriteString(")")
	case *tomast.VariableHeadListBQTerm:
		// Should normally be consumed by VariadicGenerator. As a
		// fallback emit the begin index.
		e.emitBQTerm(x.Begin)
	case *tomast.VariableHeadArrayBQTerm:
		// VariableHeadArray is referenced only inside an
		// ArrayPropagator-built constraint; the ArrayGenerator
		// consumes it before reaching the Backend, so seeing one
		// here is unusual. Render as the begin index — best we
		// can do as a fallback.
		e.emitBQTerm(x.BeginIndex)
	case *tomast.CompositeBQTerm:
		// `Composite` is the parser's way of carrying mixed host
		// text + nested BQTerms (a backquote interpolation). Each
		// slot is either a TL chunk (raw text) or a BQTerm.
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
		fmt.Fprintf(&e.buf, "/* backend: unsupported bqterm %T */", x)
	}
}

// emitBQTermArgList renders a comma-separated BQTermList.
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


// bqTermAsText renders a BQTerm to a plain string (used when we
// need the text form for inline composition with raw expressions
// like `(idx >= tom_get_size_...())`).
func bqTermAsText(b tomast.BQTerm) string {
	var sub emitter
	sub.emitBQTerm(b)
	return sub.buf.String()
}
