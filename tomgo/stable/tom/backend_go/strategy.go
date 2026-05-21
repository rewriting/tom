// Backend_go — Strategy emission for `%strategy R() extends X() { visit T { rules } }`.
//
// Same shape as backend_java/backend_strategy.go but with Go-syntax
// output. Key Go-vs-Java differences:
//
//   - No class extension — `R` is a struct embedding
//     `*sl.AbstractStrategyBasic`, which already provides
//     ChildCount/ChildAt/SetChildAt via its `abstractCombinator`
//     base. No boilerplate getChildren/setChildren methods to emit.
//   - No generics on VisitLight — the method takes `any` and the
//     per-sort dispatch does a type assertion.
//   - No checked exceptions — return `(any, error)` instead of
//     `throws VisitFailure`.
//   - `tom_make_R()` is a plain top-level function (no
//     `private static` class-member modifier).
//
// The visit body is compiled with `compiler.CompileMatch`, the
// same entry-point the user-written `%match` blocks go through.

package backend_go

import (
	"fmt"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom/compiler"
)

// emitStrategy writes the Go struct + tom_make_<Name> factory for a
// StrategyDeclaration, plus one `visit_<Sort>` / `_visit_<Sort>`
// method per visit block.
func (e *emitter) emitStrategy(strat *tomast.StrategyDeclaration) error {
	name := renderTomName(strat.SName)
	if name == "" {
		return nil
	}
	visits := flattenVisitList(strat.VisitList)

	type sortEmit struct {
		sortName string
		sortTL   string
		compiled tomast.Instruction
	}
	var perSort []sortEmit
	for _, v := range visits {
		vt, ok := v.(*tomast.VisitTermTomVisit)
		if !ok {
			continue
		}
		sortName := tomTypeSortName(vt.VNode)
		sortTL := tomTypeTL(vt.VNode, e.symbols)
		match := tomast.MakeMatch(
			vt.AstConstraintInstructionList,
			tomast.MakeConcOption(),
		)
		compiled := compiler.CompileMatch(match.(*tomast.MatchInstruction), e.symbols)
		perSort = append(perSort, sortEmit{
			sortName: sortName,
			sortTL:   sortTL,
			compiled: compiled,
		})
	}

	// Struct + factory. `*sl.AbstractStrategyBasic` embed brings
	// ChildCount/ChildAt/SetChildAt for free; only VisitLight is
	// user-supplied.
	fmt.Fprintf(&e.buf, "\ntype %s struct {\n\t*sl.AbstractStrategyBasic\n}\n", name)

	fmt.Fprintf(&e.buf, "\nfunc tom_make_%s() sl.Strategy {\n\tr := &%s{}\n\tr.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(", name, name)
	e.emitBQTerm(strat.ExtendsTerm)
	e.buf.WriteString(")\n\treturn r\n}\n")

	// VisitLight dispatcher.
	fmt.Fprintf(&e.buf, "\nfunc (tom__strat *%s) VisitLight(v any, introspector sl.Introspector) (any, error) {\n", name)
	for _, ps := range perSort {
		fmt.Fprintf(&e.buf, "\tif tom__t, tom__ok := v.(%s); tom__ok && tom_is_sort_%s(tom__t) {\n\t\treturn tom__strat.visit_%s(tom__t, introspector)\n\t}\n",
			ps.sortTL, ps.sortName, ps.sortName)
	}
	e.buf.WriteString("\treturn tom__strat.Any().VisitLight(v, introspector)\n}\n")

	// Per-sort visit_<Sort> + _visit_<Sort> methods.
	for _, ps := range perSort {
		// _visit_<Sort>: fall back to `any` when no rule fired.
		fmt.Fprintf(&e.buf, "\nfunc (tom__strat *%s) _visit_%s(arg %s, introspector sl.Introspector) (%s, error) {\n",
			name, ps.sortName, ps.sortTL, ps.sortTL)
		fmt.Fprintf(&e.buf, "\tres, err := tom__strat.Any().VisitLight(arg, introspector)\n\tif err != nil {\n\t\treturn arg, err\n\t}\n\ttyped, _ := res.(%s)\n\treturn typed, nil\n}\n", ps.sortTL)

		// visit_<Sort>: wrap the compiled match body in an inner
		// closure that returns just the rewrite target — the
		// CompileMatch IR emits bare `return X;` for action rules
		// like `a() -> b()`, which doesn't satisfy our (Sort, error)
		// outer signature. The closure converts that pattern to
		// "non-nil = rewrote; nil = no rule fired", and we propagate
		// the outer error via the surrounding return.
		fmt.Fprintf(&e.buf, "\nfunc (tom__strat *%s) visit_%s(tom__arg %s, introspector sl.Introspector) (%s, error) {\n",
			name, ps.sortName, ps.sortTL, ps.sortTL)
		fmt.Fprintf(&e.buf, "\tif tom__res := func() %s {\n", ps.sortTL)
		if err := e.emitInstruction(ps.compiled); err != nil {
			return err
		}
		fmt.Fprintf(&e.buf, "\nreturn nil}(); tom__res != nil {\n\t\treturn tom__res, nil\n\t}\n")
		fmt.Fprintf(&e.buf, "\treturn tom__strat._visit_%s(tom__arg, introspector)\n}\n", ps.sortName)
	}

	return nil
}

func flattenVisitList(l tomast.TomVisitList) []tomast.TomVisit {
	c, ok := l.(*tomast.ConcTomVisitTomVisitList)
	if !ok {
		return nil
	}
	return c.Slots
}
