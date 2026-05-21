// Backend — Strategy class emission.
//
// `%strategy R() extends Identity() { visit T1 { rules } }` lowers
// to a Java class:
//
//   class R extends tom.library.sl.AbstractStrategyBasic {
//     public R() { super(<extends>); }
//     // boilerplate methods (getChildAt, setChildAt, …)
//     public <T> T visitLight(T v, Introspector intro) throws VisitFailure {
//       if (tom_is_sort_<sort>(v)) return ((T) visit_<sort>((<TL>) v, intro));
//       ...
//       return any.visitLight(v, intro);
//     }
//     public <TL> visit_<sort>(<TL> tom__arg, Introspector intro) throws VisitFailure {
//       <compiled match>
//       return _visit_<sort>(tom__arg, intro);
//     }
//     public <TL> _visit_<sort>(<TL> arg, Introspector intro) throws VisitFailure {
//       return ((TL) any.visitLight(arg, intro));
//     }
//   }
//   private static Strategy tom_make_<Name>() { return new <Name>(); }
//
// Each `visit <sort>` block is converted to a synthetic
// `MatchInstruction([rules], opts)` and compiled via
// `compiler.CompileMatch` — the same path used for user-written
// `%match` blocks. Mirrors Java's tom.engine.expander.StrategyExpander
// + tom.engine.backend.GenericGenerator (~400 LOC combined).

package backend_java

import (
	"fmt"

	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom/compiler"
)

// emitStrategy writes the full Java class for a StrategyDeclaration
// plus its `tom_make_<Name>()` helper.
func (e *emitter) emitStrategy(strat *tomast.StrategyDeclaration) error {
	name := renderTomName(strat.SName)
	if name == "" {
		return nil
	}
	visits := flattenVisitList(strat.VisitList)

	// Per-sort method bodies: synthesise a Match from the visit
	// rules and run it through the compiler. The result is spliced
	// into the visit_<Sort> Java method.
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
		// The visit's pattern rules are already in
		// ConstraintInstructionList shape — wrap as a MatchInstruction
		// with the synthetic `tom__arg` subject.
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

	// Class header + constructor. The `extends` BQTerm gives us
	// the super(...) argument, e.g. `tom_make_Identity()` for
	// `extends Identity()`.
	fmt.Fprintf(&e.buf, "public static class %s extends tom.library.sl.AbstractStrategyBasic {", name)
	e.buf.WriteString("public ")
	e.buf.WriteString(name)
	e.buf.WriteString("() {super(")
	e.emitBQTerm(strat.ExtendsTerm)
	e.buf.WriteString(");}")
	e.emitStrategyBoilerplate()

	// visitLight dispatcher: one `if (tom_is_sort_X(v)) ...` per sort.
	e.buf.WriteString("@SuppressWarnings(\"unchecked\")")
	e.buf.WriteString("public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {")
	for _, ps := range perSort {
		fmt.Fprintf(&e.buf, "if (tom_is_sort_%s(v)) {return ((T)visit_%s((( %s )v),introspector));}", ps.sortName, ps.sortName, ps.sortTL)
	}
	e.buf.WriteString("if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}")
	e.buf.WriteString("}")

	// Per-sort visit_<Sort> + _visit_<Sort> methods.
	for _, ps := range perSort {
		// Fallback _visit_<Sort> — delegates to `any` when none of
		// the rules fired.
		fmt.Fprintf(&e.buf,
			"@SuppressWarnings(\"unchecked\")public %s _visit_%s(%s arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( %s )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}",
			ps.sortTL, ps.sortName, ps.sortTL, ps.sortTL,
		)
		// visit_<Sort> — compiled rules first, then fallback call.
		fmt.Fprintf(&e.buf,
			"@SuppressWarnings(\"unchecked\")public %s visit_%s(%s tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {",
			ps.sortTL, ps.sortName, ps.sortTL,
		)
		if err := e.emitInstruction(ps.compiled); err != nil {
			return err
		}
		fmt.Fprintf(&e.buf, "return _visit_%s(tom__arg,introspector);}", ps.sortName)
	}

	e.buf.WriteString("}")
	// Static factory: tom_make_<Name>() → new <Name>().
	fmt.Fprintf(&e.buf, "private static tom.library.sl.Strategy tom_make_%s() { return new %s();}", name, name)
	return nil
}

// emitStrategyBoilerplate writes the getChildren/setChildren/
// getChildCount/getChildAt/setChildAt overrides Java's reference
// always emits.
func (e *emitter) emitStrategyBoilerplate() {
	e.buf.WriteString("public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}")
	e.buf.WriteString("public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}")
	e.buf.WriteString("public int getChildCount() {return 1;}")
	e.buf.WriteString("public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}")
	e.buf.WriteString("public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}")
}

// flattenVisitList returns the visit slots of a TomVisitList.
func flattenVisitList(l tomast.TomVisitList) []tomast.TomVisit {
	c, ok := l.(*tomast.ConcTomVisitTomVisitList)
	if !ok {
		return nil
	}
	return c.Slots
}
