// Package compiler — SyntacticGenerator.
//
// Java reference: stable/tom/engine/compiler/generator/SyntacticGenerator.t
// (111 LOC).
//
// Rewrites Expression nodes that the SyntacticPropagator left behind
// in `ConstraintToExpression` wrappers:
//
//   - `ConstraintToExpression(MatchConstraint(RecordAppl(opts,[N],[],_),
//                                              SymbolOf(subject), aType))`
//     → `buildEqualFunctionSymbol(aType, subject, N, theory)`.
//     For ordinary (non-list/array, non-AU) symbols this produces
//     `IsFsym(N, ExpressionToBQTerm(Cast(aType, BQTermToExpression(subject))))`.
//
//   - `ConstraintToExpression(MatchConstraint(TestVar(v), t))` →
//     `EqualBQTerm(v.AstType, t, toBQVar(v))`. (Not needed for Peano.)
//
// Anything else is left intact for downstream generators (Variadic,
// Array, AC) or for the generator's `generateAutomata` catch-all.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// generateSyntactic walks the Expression top-down and rewrites the
// shapes SyntacticGenerator's strategy handles. Returns the
// (possibly new) Expression.
func (env *compilerEnv) generateSyntactic(e tomast.Expression) tomast.Expression {
	return walkExpressionConnectives(e, env.generateSyntactic, env.tryGenerateSyntactic)
}

// tryGenerateSyntactic does the per-shape rewrite. Returns the new
// expression and `true` when the rule fires.
func (env *compilerEnv) tryGenerateSyntactic(cte *tomast.ConstraintToExpressionExpression) (tomast.Expression, bool) {
	mc, ok := cte.Cons.(*tomast.MatchConstraintConstraint)
	if !ok {
		return cte, false
	}
	// Case A — Pattern is RecordAppl with a single name, Subject is SymbolOf.
	if pat, okp := mc.Pattern.(*tomast.RecordApplTomTerm); okp {
		if sym, oks := mc.Subject.(*tomast.SymbolOfBQTerm); oks {
			if nl, okn := pat.NameList.(*tomast.ConcTomNameTomNameList); okn && len(nl.Slots) == 1 {
				name := nl.Slots[0]
				return env.buildEqualFunctionSymbol(mc.AstType, sym.GroundTerm, name), true
			}
		}
	}
	// Case B — Pattern is TestVar(v) — Java's "replace"-generated
	// case, used by mergeDuplicateVars for non-linear pattern
	// merges. Emits `EqualBQTerm(type, t, toBQVar(v))`.
	if tv, ok := mc.Pattern.(*tomast.TestVarTomTerm); ok {
		t := mc.Subject
		switch v := tv.Variable.(type) {
		case *tomast.VariableTomTerm:
			return tomast.MakeEqualBQTerm(v.AstType, t,
				tomast.MakeBQVariable(v.Options, v.AstName, v.AstType),
			), true
		case *tomast.VariableStarTomTerm:
			return tomast.MakeEqualBQTerm(v.AstType, t,
				tomast.MakeBQVariableStar(v.Options, v.AstName, v.AstType),
			), true
		}
	}
	return cte, false
}

// buildEqualFunctionSymbol — Java SyntacticGenerator lines 95-110.
//
// Returns the Expression to test whether `subject` equals the
// constructor `name` of type `type`. The decision depends on whether
// the type is builtin and whether the symbol carries IsFsym /
// list/array hooks. For Peano we always hit the last branch:
//
//	IsFsym(name, ExpressionToBQTerm(Cast(type, BQTermToExpression(subject))))
func (env *compilerEnv) buildEqualFunctionSymbol(t tomast.TomType, subject tomast.BQTerm, name tomast.TomName) tomast.Expression {
	// Without a symbol table the canonical Peano-shape applies.
	return tomast.MakeIsFsym(name,
		tomast.MakeExpressionToBQTerm(
			tomast.MakeCast(t,
				tomast.MakeBQTermToExpression(subject),
			),
		),
	)
}

// replaceSubterms walks an Expression / BQTerm tree top-down (no
// stop-on-success) and rewrites every `Subterm(name, slotName, t)`
// into `ExpressionToBQTerm(GetSlot(slotType, name, slotNameStr, t))`.
// Java strategy: ConstraintGenerator.ReplaceSubterms.
func (env *compilerEnv) replaceSubterms(e tomast.Expression) tomast.Expression {
	switch x := e.(type) {
	case *tomast.AndExpression:
		l := env.replaceSubterms(x.Arg1)
		r := env.replaceSubterms(x.Arg2)
		if l != x.Arg1 || r != x.Arg2 {
			return tomast.MakeAnd(l, r)
		}
	case *tomast.OrConnectorExpression:
		if ns, changed := mapExpressionSlots(x.Slots, env.replaceSubterms); changed {
			return tomast.MakeOrConnector(ns...)
		}
	case *tomast.OrExpressionDisjunctionExpression:
		if ns, changed := mapExpressionSlots(x.Slots, env.replaceSubterms); changed {
			return tomast.MakeOrExpressionDisjunction(ns...)
		}
	case *tomast.NegationExpression:
		inner := env.replaceSubterms(x.Arg)
		if inner != x.Arg {
			return tomast.MakeNegation(inner)
		}
	case *tomast.AntiMatchExpressionExpression:
		inner := env.replaceSubterms(x.Expression)
		if inner != x.Expression {
			return tomast.MakeAntiMatchExpression(inner)
		}
	case *tomast.IsFsymExpression:
		newVar := env.replaceSubtermsBQ(x.Variable)
		if newVar != x.Variable {
			return tomast.MakeIsFsym(x.AstName, newVar)
		}
	case *tomast.IsSortExpression:
		newVar := env.replaceSubtermsBQ(x.Variable)
		if newVar != x.Variable {
			return tomast.MakeIsSort(x.AstType, newVar)
		}
	case *tomast.CastExpression:
		inner := env.replaceSubterms(x.Source)
		if inner != x.Source {
			return tomast.MakeCast(x.AstType, inner)
		}
	case *tomast.BQTermToExpressionExpression:
		newBQ := env.replaceSubtermsBQ(x.AstTerm)
		if newBQ != x.AstTerm {
			return tomast.MakeBQTermToExpression(newBQ)
		}
	case *tomast.ConstraintToExpressionExpression:
		// Constraint hasn't been rewritten by SyntacticGenerator —
		// typically a Variable / VariableStar binding. Walk into
		// the constraint to rewrite Subterms that appear as the
		// subject of a MatchConstraint.
		if mc, ok := x.Cons.(*tomast.MatchConstraintConstraint); ok {
			newSubj := env.replaceSubtermsBQ(mc.Subject)
			if newSubj != mc.Subject {
				return tomast.MakeConstraintToExpression(
					tomast.MakeMatchConstraint(mc.Pattern, newSubj, mc.AstType),
				)
			}
		}
	}
	return e
}

// replaceSubtermsBQ walks BQTerm-level subterm uses. Subterm itself
// is a BQTerm — that's where the rewrite fires.
func (env *compilerEnv) replaceSubtermsBQ(b tomast.BQTerm) tomast.BQTerm {
	if sub, ok := b.(*tomast.SubtermBQTerm); ok {
		slotType := env.slotTypeFor(nameString(sub.AstName), nameString(sub.SlotName))
		if slotType == nil {
			slotType = tomast.MakeEmptyType()
		}
		return tomast.MakeExpressionToBQTerm(
			tomast.MakeGetSlot(slotType, sub.AstName, nameString(sub.SlotName), sub.GroundTerm),
		)
	}
	switch x := b.(type) {
	case *tomast.ExpressionToBQTermBQTerm:
		newExp := env.replaceSubterms(x.Exp)
		if newExp != x.Exp {
			return tomast.MakeExpressionToBQTerm(newExp)
		}
	case *tomast.SymbolOfBQTerm:
		inner := env.replaceSubtermsBQ(x.GroundTerm)
		if inner != x.GroundTerm {
			return tomast.MakeSymbolOf(inner)
		}
	}
	return b
}
