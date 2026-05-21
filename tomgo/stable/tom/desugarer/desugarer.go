package desugarer

import (
	"fmt"

	"tom/tomgo/stable/library/sl"
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/platform"
)

// Desugarer is the Go port of Java's
// [tom.engine.desugarer.DesugarerPlugin]. It lowers anonymous-variable
// syntactic sugar (`_` and `_*`) to fresh named variables, and (in
// later phases) rewrites TermAppl / BQRecordAppl into their canonical
// shapes.
//
// Phase 6.6 scope: only the [desugarUnderscore] visitor is wired up,
// because that's the only one the 13 mutated fixtures in
// TestPipelineDump_MutationMatrix exercise. The TermAppl /
// BQRecordAppl rewrites will be added when fixtures appear that
// require them (typically `Foo[a=v]` record literals or
// constant-symbol rewrites in non-Java host code).
//
// Equivalence with Java is enforced by [TestDesugarer_ParityWithJava]
// in desugarer_test.go: for every fixture the Java pipeline
// transforms past parse-time, the Go Desugarer must produce a Code
// whose Stringer output matches Java's [PhaseDesugared] dump.
type Plugin struct{}

// Name implements [platform.Plugin].
func (Plugin) Name() string { return "Desugarer" }

// Run implements [platform.Plugin]. The plugin is stateless: each run
// creates fresh visitor instances so any internal counters restart
// from 1 every time (matching Java's per-DesugarerPlugin-instance
// state — TomParserPlugin spawns a new DesugarerPlugin per file).
//
// Per Java's DesugarerPlugin.run, three visitor passes happen in
// order:
//
//	1. TopDown(DesugarUnderscore)            — rename `_` to fresh vars
//	2. TopDownIdStopOnSuccess(replaceTermAppl) — TermAppl → RecordAppl
//	3. TopDown(replaceBQRecordAppl)          — BQRecordAppl → BQAppl
//
// Phase 6.6 currently ports passes #1 and #2. Pass #3 will be added
// when fixtures with `` `Foo[a=v] `` records appear.
func (Plugin) Run(in platform.State) (platform.State, error) {
	if in.Code == nil {
		return in, nil
	}
	intro := sl.VisitableIntrospector{}

	out, err := sl.MakeTopDown(newDesugarUnderscore()).VisitLight(in.Code, intro)
	if err != nil {
		return in, fmt.Errorf("Desugarer/underscore: %w", err)
	}
	out, err = sl.MakeTopDownIdStopOnSuccess(newReplaceTermApplTomSyntax(in.Symbols)).VisitLight(out, intro)
	if err != nil {
		return in, fmt.Errorf("Desugarer/termAppl: %w", err)
	}
	code, ok := out.(tomast.Code)
	if !ok {
		return in, fmt.Errorf("Desugarer: walker returned %T, want tomast.Code", out)
	}
	state := in
	state.Code = code
	return state, nil
}

// desugarUnderscore is the visitor that performs the actual rewrite.
// Java reference:
//
//	if (subject instanceof Variable && Variable.getAstName() instanceof EmptyName) {
//	    return Variable.make(Options, getFreshVariable(), AstType, Constraints);
//	}
//	if (subject instanceof VariableStar && VariableStar.getAstName() instanceof EmptyName) {
//	    return VariableStar.make(Options, getFreshVariable(), AstType, Constraints);
//	}
//	return any.visitLight(subject, intro);   // Identity in this context
//
// The visitor embeds [AbstractStrategyBasic] with [sl.Identity] as the
// fallback (matching `super(new Identity())` on the Java side), so a
// non-matching subject is returned unchanged.
type desugarUnderscore struct {
	*sl.AbstractStrategyBasic
	freshCounter int
}

func newDesugarUnderscore() *desugarUnderscore {
	return &desugarUnderscore{
		AbstractStrategyBasic: sl.NewAbstractStrategyBasic(sl.NewIdentity()),
	}
}

// getFreshVariable mirrors Java's
//
//	private TomName getFreshVariable() {
//	    freshCounter++;
//	    return Name.make("_f_r_e_s_h_v_a_r_" + freshCounter);
//	}
//
// — note pre-increment, so the first call yields counter == 1.
func (d *desugarUnderscore) getFreshVariable() tomast.TomName {
	d.freshCounter++
	return tomast.MakeName(fmt.Sprintf("_f_r_e_s_h_v_a_r_%d", d.freshCounter))
}

func (d *desugarUnderscore) VisitLight(subject any, intro sl.Introspector) (any, error) {
	switch v := subject.(type) {
	case *tomast.VariableTomTerm:
		if _, empty := v.AstName.(*tomast.EmptyNameTomName); empty {
			return tomast.MakeVariable(v.Options, d.getFreshVariable(), v.AstType, v.Constraints), nil
		}
	case *tomast.VariableStarTomTerm:
		if _, empty := v.AstName.(*tomast.EmptyNameTomName); empty {
			return tomast.MakeVariableStar(v.Options, d.getFreshVariable(), v.AstType, v.Constraints), nil
		}
	}
	return d.Any().VisitLight(subject, intro)
}

// replaceTermApplTomSyntax is the visitor for pass #2: it rewrites
// `TermAppl(opts, names, args, constraints)` into a `RecordAppl(opts,
// names, slots, constraints)` where each positional arg becomes a
// `PairSlotAppl(EmptyName(), arg')` and arg' is itself recursively
// desugared.
//
// Phase 6.6 simplification: the symbol-table-aware branch (where the
// real slot names come from the operator's declaration) is not yet
// active because the typer hasn't run. All symbols therefore look
// "unknown" and slot names default to EmptyName() — which matches
// Java's behaviour on a freshly-parsed input.
//
// Anti-symbols (`AntiName(Name)`) are unwrapped before lookup,
// faithfully mirroring DesugarerPlugin.replaceTermAppl:240-244.
type replaceTermApplTomSyntax struct {
	*sl.AbstractStrategyBasic
	symbols *platform.SymbolTable
}

func newReplaceTermApplTomSyntax(symbols *platform.SymbolTable) *replaceTermApplTomSyntax {
	return &replaceTermApplTomSyntax{
		AbstractStrategyBasic: sl.NewAbstractStrategyBasic(sl.NewIdentity()),
		symbols:               symbols,
	}
}

func (r *replaceTermApplTomSyntax) VisitLight(subject any, intro sl.Introspector) (any, error) {
	if ta, ok := subject.(*tomast.TermApplTomTerm); ok {
		return r.replaceTermAppl(ta.Options, ta.NameList, ta.Args, ta.Constraints), nil
	}
	return r.Any().VisitLight(subject, intro)
}

// replaceTermAppl is the Go port of
// DesugarerPlugin.replaceTermAppl (lines 240-283). When the symbol is
// unknown (or the arg list is empty), it returns a `RecordAppl` whose
// slots use `EmptyName()` placeholders. When the symbol resolves in
// the SymbolTable to a non-list, non-array operator, it instead reads
// the slot names from `tomSymbol.PairNameDeclList` so the emitted
// `RecordAppl` matches Java's `Foo[slotName=arg]` form.
//
// The recursive call wraps a *fresh* TopDownIdStopOnSuccess walker
// around a new visitor instance, exactly as the Java code does — the
// outer walker has already short-circuited on this term so without
// the manual recursion sub-TermAppls would be missed.
func (r *replaceTermApplTomSyntax) replaceTermAppl(
	options tomast.OptionList,
	nameList tomast.TomNameList,
	args tomast.TomList,
	constraints tomast.ConstraintList,
) tomast.TomTerm {
	argList := tomTermArgs(args)
	headName := headSymbolName(nameList)
	tomSymbol := r.lookupSymbol(headName)

	// Case A: unknown symbol AND no args → trivial RecordAppl.
	if tomSymbol == nil && len(argList) == 0 {
		return tomast.MakeRecordAppl(options, nameList, tomast.MakeConcSlot(), constraints)
	}

	// Pull the slot names from the symbol, if any. The Java code falls
	// back to EmptyName() for list/array operators and for unknown
	// symbols; we do likewise.
	slotNames := slotNamesFor(tomSymbol)

	slots := make([]tomast.Slot, 0, len(argList))
	intro := sl.VisitableIntrospector{}
	for i, arg := range argList {
		desugared, err := sl.MakeTopDownIdStopOnSuccess(newReplaceTermApplTomSyntax(r.symbols)).
			VisitLight(arg, intro)
		if err != nil {
			desugared = arg
		}
		var slotName tomast.TomName = tomast.MakeEmptyName()
		if i < len(slotNames) {
			slotName = slotNames[i]
		}
		slots = append(slots, tomast.MakePairSlotAppl(slotName, desugared.(tomast.TomTerm)))
	}
	return tomast.MakeRecordAppl(options, nameList, tomast.MakeConcSlot(slots...), constraints)
}

// lookupSymbol fetches the canonical TomSymbol for opName from the
// State.Symbols (if any). Java's getSymbolFromName also returns null
// when no symbol exists; we return nil for the same shape.
func (r *replaceTermApplTomSyntax) lookupSymbol(opName string) tomast.TomSymbol {
	if r.symbols == nil || opName == "" {
		return nil
	}
	if sym, ok := r.symbols.Symbols[opName]; ok {
		return sym
	}
	return nil
}

// headSymbolName returns the first name of a non-empty TomNameList,
// or "" if the list is empty (e.g. a degenerate AntiName variant the
// AstBuilder unwraps before we get here).
func headSymbolName(nameList tomast.TomNameList) string {
	c, ok := nameList.(*tomast.ConcTomNameTomNameList)
	if !ok || len(c.Slots) == 0 {
		return ""
	}
	switch n := c.Slots[0].(type) {
	case *tomast.NameTomName:
		return n.String_
	case *tomast.AntiNameTomName:
		if inner, ok := n.Name.(*tomast.NameTomName); ok {
			return inner.String_
		}
	}
	return ""
}

// slotNamesFor returns the ordered slot names from a TomSymbol's
// PairNameDeclList. The slice is empty for nil symbols or those
// representing list/array operators (which Java treats as having no
// fixed-position slot list).
func slotNamesFor(sym tomast.TomSymbol) []tomast.TomName {
	if sym == nil {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	list, ok := s.PairNameDeclList.(*tomast.ConcPairNameDeclPairNameDeclList)
	if !ok {
		return nil
	}
	out := make([]tomast.TomName, 0, len(list.Slots))
	for _, pair := range list.Slots {
		if p, ok := pair.(*tomast.PairNameDeclPairNameDecl); ok {
			out = append(out, p.SlotName)
		}
	}
	return out
}

// tomTermArgs returns the underlying slice of a TomList. The list is
// always a ConcTomTermTomList in well-formed AST; we still defensively
// return an empty slice for any other concrete type so callers don't
// dereference a nil interface.
func tomTermArgs(args tomast.TomList) []tomast.TomTerm {
	if c, ok := args.(*tomast.ConcTomTermTomList); ok {
		return c.Slots
	}
	return nil
}
