package typer

import (
	"fmt"

	"tom/tomgo/stable/library/sl"
	"tom/tomgo/stable/library/tomast"
	"tom/tomgo/stable/tom"
)

// Typer is the Go port of Java's [tom.engine.typer.TyperPlugin]
// running in `newtyper` mode. Java's NewKernelTyper builds a system
// of type equations and subtype constraints by walking the AST, then
// solves it via substitution. Our 30-fixture corpus exercises only
// a tiny corner of that machinery (no `%op` declarations to consult,
// every symbol is unknown), so this port keeps just three rewrites
// — the ones empirically observed in TestPipelineDump_MutationMatrix:
//
//  1. BQAppl(opts, Name, args) → FunctionCall(Name, contextType, args')
//     when the symbol is unknown (the always-true case for us).
//     `contextType` flows from the enclosing AST node (e.g. a
//     MatchConstraint's aType, or EmptyType in a body).
//  2. Variable.AstType update: when the current AstType is the
//     "unknown type" placeholder and contextType is concrete, replace.
//  3. BQVariable.AstType propagation from pattern scope: a
//     ConstraintInstruction registers pattern Variables by name; in
//     its action body, BQVariable(name) with an "unknown type" picks
//     up the type from that scope.
//
// The full constraint-solver port (NewKernelTyper.java, 1700+ LOC)
// is deferred until fixtures appear that exercise it (multi-symbol
// constraints, polymorphism, …).
// per-file typer state — variable-type registrations don't leak
// between compilation units.
func Run(in tom.State) (tom.State, error) {
	if in.Code == nil {
		return in, nil
	}
	nkt := &newKernelTyper{
		varTypes:   make(map[string]tomast.TomType),
		symbols:    in.Symbols,
		typerAbort: in.HasInlineGom,
	}
	// Pass 1: structural type propagation (pattern↔subject, BQAppl →
	// FunctionCall, variable-scope linkage).
	out := nkt.inferAllTypes(in.Code, tomast.MakeEmptyType())
	// Pass 2: TLType substitution — replace every Type(_, sortName,
	// EmptyTargetLanguageType()) whose `sortName` has an `implement`
	// body in the SymbolTable. Java does the same via SymbolTable
	// lookups baked into TomBase.getSymbolCodomain & friends.
	out = nkt.substituteTLTypes(out)
	code, ok := out.(tomast.Code)
	if !ok {
		return in, fmt.Errorf("Typer: walker returned %T, want tomast.Code", out)
	}
	state := in
	state.Code = code
	return state, nil
}

// newKernelTyper holds the per-file mutable state of the Go typer.
// Today that's a name→type map for pattern variables plus a pointer
// to the shared SymbolTable for codomain and TLType resolution;
// Java's counterpart additionally carries unification state and a
// growing list of generated type equations.
type newKernelTyper struct {
	varTypes map[string]tomast.TomType
	symbols  *tom.SymbolTable
	// typerAbort indicates that Java's typer would have aborted on
	// this input (we proxy that via parser.HasInlineGom — see
	// tom.State for the rationale). When set, we skip the
	// BQAppl→FunctionCall rewrite so the resulting AST shape matches
	// Java's aborted-typer output.
	typerAbort bool
}

// inferAllTypes mirrors Java's NewKernelTyper.inferAllTypes(term,
// contextType): a recursive walker that threads a "contextType"
// down through the tree and rewrites specific nodes when it knows
// how to. Anything it doesn't recognise is recursed into generically
// via [sl.VisitableIntrospector], with contextType reset to
// EmptyType for the children — i.e. we only propagate a context
// across the structural boundaries we know about (MatchConstraint
// subject/pattern, ConstraintInstruction action, etc.).
func (n *newKernelTyper) inferAllTypes(subject any, contextType tomast.TomType) any {
	switch s := subject.(type) {

	case *tomast.MatchConstraintConstraint:
		// Java NewKernelTyper.inferConstraint:725 — both pattern and
		// subject get the constraint's aType as their context. We do
		// NOT elevate aType from the subject's declared type: Java's
		// constraint solver tracks the subtype relationship via a
		// fresh TypeVar and leaves `"unknown type"` placeholders
		// untouched. Eager substitution breaks parity (see Record.t
		// / regress/SuccessiveErrors.t).
		aType := s.AstType
		// Numeric-constant inference: if aType is still the parser's
		// "unknown type" placeholder AND the subject is a numeric
		// constant (BQAppl with no args whose name is `-?[0-9]+`),
		// Java's typer infers Type("int", EmptyTL). Mirror that here.
		if isUnknownType(aType) && isNumericConstSubject(s.Subject) {
			aType = tomast.MakeType(
				tomast.MakeConcTypeOption(),
				"int",
				tomast.MakeEmptyTargetLanguageType(),
			)
		}
		// Register subjects in varTypes so backquote variables in
		// the rule's body or downstream constraints can pick up the
		// concrete type (regress/UnknownSymbol1's `\`tt` after a
		// `%match(B tt)` is the canonical case).
		if name, t := bqTermName(s.Subject), bqTermType(s.Subject); name != "" && t != nil && !isUnknownType(t) && !isEmptyType(t) {
			n.varTypes[name] = t
		}
		newPattern := n.inferAllTypes(s.Pattern, aType).(tomast.TomTerm)
		newSubject := n.inferAllTypes(s.Subject, aType).(tomast.BQTerm)
		return tomast.MakeMatchConstraint(newPattern, newSubject, aType)

	case *tomast.ConstraintInstructionConstraintInstruction:
		// Java NewKernelTyper.inferConstraintInstructionList: the
		// constraint is typed first (which registers any pattern
		// variables in our varTypes map), then the action body is
		// typed with EmptyType as its outer context.
		newConstraint := n.inferAllTypes(s.Constraint, tomast.MakeEmptyType()).(tomast.Constraint)
		newAction := n.inferAllTypes(s.Action, tomast.MakeEmptyType()).(tomast.Instruction)
		return tomast.MakeConstraintInstruction(newConstraint, newAction, s.Options)

	case *tomast.RecordApplTomTerm:
		// Java NewKernelTyper.inferSlotList propagates slot types
		// only when the surrounding contextType either is the same
		// sort as the symbol's codomain OR is unconstrained
		// (EmptyType / "unknown type"). Subtype relationships
		// between contextType and codomain are tracked as
		// constraints — they don't trigger eager substitution into
		// nested pattern Variables. Mirroring that behaviour:
		//
		//   - codomain == contextType        → propagate;
		//   - contextType is EmptyType/unknown → propagate (typer-
		//     inferred sort, no conflict);
		//   - otherwise                       → don't propagate.
		headName := headSymbolName(s.NameList)
		domain := n.domainTypesFor(headName)
		propagateSlot := n.shouldPropagateSlotTypes(headName, contextType)
		slotsList, ok := s.Slots.(*tomast.ConcSlotSlotList)
		if !ok {
			return n.recurseChildren(subject)
		}
		newSlots := make([]tomast.Slot, len(slotsList.Slots))
		for i, slot := range slotsList.Slots {
			var slotCtx tomast.TomType = tomast.MakeEmptyType()
			if propagateSlot && i < len(domain) {
				slotCtx = domain[i]
			}
			pair, ok := slot.(*tomast.PairSlotApplSlot)
			if !ok {
				newSlots[i] = slot
				continue
			}
			newAppl := n.inferAllTypes(pair.Appl, slotCtx).(tomast.TomTerm)
			newSlots[i] = tomast.MakePairSlotAppl(pair.SlotName, newAppl)
		}
		// Constraints (e.g. AliasTo on `x@pat`) inherit the
		// RecordAppl's *own* type — which is contextType from the
		// caller, not its codomain. The pattern `x@zero()` has alias
		// `x` of the same type as the surrounding pattern slot.
		newConstraints := n.inferConstraintsWithType(s.Constraints, contextType)
		return tomast.MakeRecordAppl(s.Options, s.NameList, tomast.MakeConcSlot(newSlots...), newConstraints)

	case *tomast.VariableTomTerm:
		// Update AstType if the parser left it as the "unknown type"
		// placeholder AND the surrounding context provides a
		// concrete type. Register the resulting (name, type) pair in
		// varTypes so BQVariables with the same name find it. Also
		// propagate contextType into the Constraints list — Variables
		// reachable via `name@pat` (AliasTo) get the same type.
		newType := updateUnknownType(s.AstType, contextType)
		if name, ok := s.AstName.(*tomast.NameTomName); ok {
			n.varTypes[name.String_] = newType
		}
		newConstraints := n.inferConstraintsWithType(s.Constraints, newType)
		return tomast.MakeVariable(s.Options, s.AstName, newType, newConstraints)

	case *tomast.VariableStarTomTerm:
		newType := updateUnknownType(s.AstType, contextType)
		if name, ok := s.AstName.(*tomast.NameTomName); ok {
			n.varTypes[name.String_] = newType
		}
		newConstraints := n.inferConstraintsWithType(s.Constraints, newType)
		return tomast.MakeVariableStar(s.Options, s.AstName, newType, newConstraints)

	case *tomast.BQApplBQTerm:
		// Java's TyperPlugin runs TransformBQAppl after NewKernelTyper
		// to rewrite BQAppl into BuildTerm/BuildList/BuildArray/
		// FunctionCall, but only when no error was raised earlier:
		// the typer catches *any* exception (including "Unknown
		// symbol" thrown by the SyntaxChecker) and returns without
		// running TransformBQAppl, leaving BQAppl untouched. Fixtures
		// using `%gom { ... }` (TestNonLinearity, TestACU, …) end up
		// in that "typer aborted" state because the inline gom block
		// isn't expanded without a Tom.xml config — meaning the
		// symbols are unknown, an error fires, and the rewrite is
		// skipped. We mirror that: if the symbol is unknown, KEEP the
		// BQAppl; only rewrite when the symbol is in the SymbolTable.
		name, ok := s.AstName.(*tomast.NameTomName)
		if !ok {
			return s
		}
		domain := n.domainTypesFor(name.String_)
		newArgs := n.inferBQTermListWithDomain(s.Args, domain)
		if n.symbols != nil {
			if _, found := n.symbols.Symbols[name.String_]; found {
				moduleName := moduleNameFromOptions(s.Options)
				return tomast.MakeBuildTerm(name, newArgs, moduleName)
			}
		}
		return tomast.MakeFunctionCall(name, contextType, newArgs)

	case *tomast.BQVariableBQTerm:
		// Pattern-scope lookup: if a Variable with this name was
		// registered earlier in this Run with a concrete type, and
		// our AstType is still "unknown type", inherit that.
		if name, ok := s.AstName.(*tomast.NameTomName); ok {
			if known, found := n.varTypes[name.String_]; found && isUnknownType(s.AstType) {
				return tomast.MakeBQVariable(s.Options, s.AstName, known)
			}
		}
		return s

	case *tomast.BQVariableStarBQTerm:
		if name, ok := s.AstName.(*tomast.NameTomName); ok {
			if known, found := n.varTypes[name.String_]; found && isUnknownType(s.AstType) {
				return tomast.MakeBQVariableStar(s.Options, s.AstName, known)
			}
		}
		return s
	}

	// Default: recurse into every child generically. The context
	// type doesn't propagate through unknown nodes (Java treats
	// them as opaque w.r.t. typing).
	return n.recurseChildren(subject)
}

// inferConstraintsWithType walks a Variable/VariableStar's
// ConstraintList and propagates the surrounding variable's resolved
// type into any nested AliasTo(boundVar). The Java equivalent lives
// in NewKernelTyper's visit_TomTerm for Variable, where
// `Equation(getTermType(boundTerm), aType, …)` is added to the
// constraint system; we apply the equation eagerly.
func (n *newKernelTyper) inferConstraintsWithType(cl tomast.ConstraintList, ctx tomast.TomType) tomast.ConstraintList {
	c, ok := cl.(*tomast.ConcConstraintConstraintList)
	if !ok {
		return cl
	}
	if len(c.Slots) == 0 {
		return cl
	}
	out := make([]tomast.Constraint, len(c.Slots))
	for i, cn := range c.Slots {
		if alias, ok := cn.(*tomast.AliasToConstraint); ok {
			newVar := n.inferAllTypes(alias.Var, ctx).(tomast.TomTerm)
			out[i] = tomast.MakeAliasTo(newVar)
			continue
		}
		out[i] = cn
	}
	return tomast.MakeConcConstraint(out...)
}

// inferBQTermListWithDomain walks a BQTermList, recursing into each
// element with the corresponding domain type as contextType. If the
// domain slice is shorter than the args (or nil), trailing args get
// EmptyType, matching Java NewKernelTyper.inferBQTermList's "fall
// back to EmptyType" behaviour for the no-symbol path.
func (n *newKernelTyper) inferBQTermListWithDomain(args tomast.BQTermList, domain []tomast.TomType) tomast.BQTermList {
	c, ok := args.(*tomast.ConcBQTermBQTermList)
	if !ok {
		return args
	}
	if len(c.Slots) == 0 {
		return args
	}
	out := make([]tomast.BQTerm, len(c.Slots))
	for i, a := range c.Slots {
		var ctx tomast.TomType = tomast.MakeEmptyType()
		if i < len(domain) {
			ctx = domain[i]
		}
		out[i] = n.inferAllTypes(a, ctx).(tomast.BQTerm)
	}
	return tomast.MakeConcBQTerm(out...)
}

// recurseChildren walks every child of subject via the generic
// Visitable introspector, recursing with EmptyType as context. Used
// for AST nodes we don't have a typing rule for — Code, Tom,
// AbstractBlock, BQTermToInstruction, TargetLanguageToCode, …
//
// The returned value is `subject` itself when no child changed
// (preserving hash-consing); a new instance otherwise.
func (n *newKernelTyper) recurseChildren(subject any) any {
	intro := sl.VisitableIntrospector{}
	count := intro.GetChildCount(subject)
	var dup []any
	for i := range count {
		oldChild := intro.GetChildAt(subject, i)
		newChild := n.inferAllTypes(oldChild, tomast.MakeEmptyType())
		if dup != nil {
			dup[i] = newChild
		} else if newChild != oldChild {
			dup = intro.GetChildren(subject)
			dup[i] = newChild
		}
	}
	if dup == nil {
		return subject
	}
	return intro.SetChildren(subject, dup)
}

// updateUnknownType returns ctx when current is the parser's
// "unknown type" placeholder and ctx is more informative. Otherwise
// returns current unchanged. The Java equivalent achieves the same
// effect via constraint solving (Equation + substitution), which we
// short-circuit here.
func updateUnknownType(current, ctx tomast.TomType) tomast.TomType {
	if !isUnknownType(current) {
		return current
	}
	if isEmptyType(ctx) || isUnknownType(ctx) {
		return current
	}
	return ctx
}

// isUnknownType reports whether the type is the parser's "unknown
// type" placeholder, i.e. `Type(_, "unknown type", _)`.
func isUnknownType(t tomast.TomType) bool {
	tt, ok := t.(*tomast.TypeTomType)
	return ok && tt.TomType == "unknown type"
}

// isNumericConstSubject reports whether the bqterm is a literal
// integer constant — the parser-time `BQAppl(opts, Name("5"), …)`
// shape produced by parseBQTermConstant for `%match(5)` or
// `%match(int 5)` subjects.
func isNumericConstSubject(bq tomast.BQTerm) bool {
	a, ok := bq.(*tomast.BQApplBQTerm)
	if !ok {
		return false
	}
	n, ok := a.AstName.(*tomast.NameTomName)
	if !ok || n.String_ == "" {
		return false
	}
	s := n.String_
	i := 0
	if s[0] == '-' {
		if len(s) == 1 {
			return false
		}
		i = 1
	}
	for ; i < len(s); i++ {
		if s[i] < '0' || s[i] > '9' {
			return false
		}
	}
	return true
}

// isEmptyType reports whether the type is the EmptyType nullary
// constant.
func isEmptyType(t tomast.TomType) bool {
	_, ok := t.(*tomast.EmptyTypeTomType)
	return ok
}

// substituteTLTypes walks the AST and replaces every
// `Type(_, sortName, EmptyTargetLanguageType())` whose sort has an
// `implement { body }` hook in the SymbolTable with
// `Type(_, sortName, TLType(body))`. This mirrors the effect of
// Java's SymbolTable-driven type resolution that happens implicitly
// as the typer chases sort definitions.
func (n *newKernelTyper) substituteTLTypes(root any) any {
	if n.symbols == nil || len(n.symbols.Sorts) == 0 {
		return root
	}
	intro := sl.VisitableIntrospector{}
	var walk func(any) any
	walk = func(node any) any {
		if t, ok := node.(*tomast.TypeTomType); ok {
			if _, hasTL := t.TlType.(*tomast.EmptyTargetLanguageTypeTargetLanguageType); hasTL {
				if body, found := n.symbols.Sorts[t.TomType]; found && body != "" {
					return tomast.MakeType(t.TypeOptions, t.TomType, tomast.MakeTLType(body))
				}
			}
			return node
		}
		count := intro.GetChildCount(node)
		var dup []any
		for i := range count {
			oldChild := intro.GetChildAt(node, i)
			newChild := walk(oldChild)
			if dup != nil {
				dup[i] = newChild
			} else if newChild != oldChild {
				dup = intro.GetChildren(node)
				dup[i] = newChild
			}
		}
		if dup == nil {
			return node
		}
		return intro.SetChildren(node, dup)
	}
	return walk(root)
}

// shouldPropagateSlotTypes returns whether to push the symbol's slot
// types down into its arguments at typing time. Mirrors Java's
// constraint-solving discipline: only the codomain == contextType
// (or open contextType) case triggers eager propagation; everything
// else leaves the inner pattern Variable's AstType as the
// `"unknown type"` placeholder.
func (n *newKernelTyper) shouldPropagateSlotTypes(opName string, contextType tomast.TomType) bool {
	if contextType == nil || isEmptyType(contextType) || isUnknownType(contextType) {
		return true
	}
	if n.symbols == nil {
		return false
	}
	sym, ok := n.symbols.Symbols[opName]
	if !ok {
		return false
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return false
	}
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return false
	}
	codomain := tt.Codomain
	cTyped, ok := codomain.(*tomast.TypeTomType)
	if !ok {
		return false
	}
	ctxTyped, ok := contextType.(*tomast.TypeTomType)
	if !ok {
		return false
	}
	return cTyped.TomType == ctxTyped.TomType
}

// domainTypesFor returns the ordered domain (slot) types of the
// symbol named opName, looked up in [Typer]'s SymbolTable. Empty
// when the symbol is unknown or has no slots.
func (n *newKernelTyper) domainTypesFor(opName string) []tomast.TomType {
	if n.symbols == nil || opName == "" {
		return nil
	}
	sym, ok := n.symbols.Symbols[opName]
	if !ok {
		return nil
	}
	s, ok := sym.(*tomast.SymbolTomSymbol)
	if !ok {
		return nil
	}
	tt, ok := s.TypesToType.(*tomast.TypesToTypeTomType)
	if !ok {
		return nil
	}
	domain, ok := tt.Domain.(*tomast.ConcTomTypeTomTypeList)
	if !ok {
		return nil
	}
	return domain.Slots
}

// bqTermName returns the AstName.String of a BQVariable/BQVariableStar
// when it's a plain Name (not AntiName / EmptyName), else "".
func bqTermName(t tomast.BQTerm) string {
	var n tomast.TomName
	switch s := t.(type) {
	case *tomast.BQVariableBQTerm:
		n = s.AstName
	case *tomast.BQVariableStarBQTerm:
		n = s.AstName
	default:
		return ""
	}
	if name, ok := n.(*tomast.NameTomName); ok {
		return name.String_
	}
	return ""
}

// bqTermType returns the AstType slot of a BQVariable/BQVariableStar,
// or nil for any other BQTerm variant. Helper for the
// MatchConstraint typing where we want to read the subject's declared
// type without dispatching by type at the call site.
func bqTermType(t tomast.BQTerm) tomast.TomType {
	switch s := t.(type) {
	case *tomast.BQVariableBQTerm:
		return s.AstType
	case *tomast.BQVariableStarBQTerm:
		return s.AstType
	}
	return nil
}

// moduleNameFromOptions returns the ModuleName option's payload if
// present, falling back to "default" — the Java reference picks the
// same fallback in TomBase.getModuleName.
func moduleNameFromOptions(opts tomast.OptionList) string {
	c, ok := opts.(*tomast.ConcOptionOptionList)
	if !ok {
		return "default"
	}
	for _, opt := range c.Slots {
		if mn, ok := opt.(*tomast.ModuleNameOption); ok {
			return mn.String_
		}
	}
	return "default"
}

// headSymbolName returns the first name of a non-empty TomNameList,
// or "" if the list is empty (e.g. a degenerate AntiName variant the
// AstBuilder unwraps before we get here). Duplicate of the helper in
// the desugarer package — both phases consume the same shape but
// importing across phase packages would create coupling we don't want.
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
