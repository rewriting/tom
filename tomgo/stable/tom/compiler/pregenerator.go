// Package compiler — PreGenerator.
//
// Java reference: stable/tom/engine/compiler/PreGenerator.t (462 LOC).
//
// `performPreGenerationTreatment` folds the propagated Constraint
// conjunction into a single Expression. The Expression carries the
// shape ConstraintGenerator expects (essentially a normalised
// And/Or/Not tree over MatchConstraint leaves) so the generator can
// emit the matching automaton without re-discovering the constraint
// structure.
//
// Status: SKELETON. Returns a dummy `TrueTL` expression. Real
// implementation lands with task #60.

package compiler

import "tom/tomgo/stable/library/tomast"

// performPreGenerationTreatment is the entry point used by
// CompileMatch. Java: PreGenerator.performPreGenerationTreatment.
//
// Java does two passes:
//
//  1. orderConstraints — reorders the conjuncts of an AndConstraint
//     using ~15 SwitchXxx rules (SwitchSymbolOf, SwitchAnti,
//     SwitchEmpty, SwitchVar, SwitchIsSort, SwitchTestVars). The
//     order affects the variable scoping of the generated automaton.
//  2. constraintsToExpressions — folds the constraint tree into an
//     Expression tree (head/tail decomposition into And / OrConnector /
//     OrExpressionDisjunction / ConstraintToExpression / IsSort /
//     AntiMatchExpression / Negation / IsEmptyArray / etc.).
//
// Pass 1 is reordering only — it doesn't affect *correctness*, just
// the quality of the generated code (variable scoping, early-exit
// efficiency). We skip it in this first port. Pass 2 is implemented
// in `constraintsToExpressions` below.
func (e *compilerEnv) performPreGenerationTreatment(c tomast.Constraint) tomast.Expression {
	return e.constraintsToExpressions(c)
}

// constraintsToExpressions — Go port of Java
// PreGenerator.constraintsToExpressions (lines 430-460).
//
// Recursive head/tail decomposition: AndConstraint(m, X*) → And(m, X*).
// Tomast's AndExpression is binary, so chains get rebuilt right-leaning.
func (e *compilerEnv) constraintsToExpressions(c tomast.Constraint) tomast.Expression {
	switch x := c.(type) {
	case *tomast.AndConstraintConstraint:
		return e.foldExprs(x.Slots, true, false)
	case *tomast.OrConstraintConstraint:
		return e.foldExprs(x.Slots, false, false)
	case *tomast.OrConstraintDisjunctionConstraint:
		return e.foldExprs(x.Slots, false, true)
	case *tomast.MatchConstraintConstraint:
		return tomast.MakeConstraintToExpression(c)
	case *tomast.NumericConstraintConstraint:
		return tomast.MakeConstraintToExpression(c)
	case *tomast.AntiMatchConstraintConstraint:
		return tomast.MakeAntiMatchExpression(e.constraintsToExpressions(x.Constraint))
	case *tomast.NegateConstraint:
		return tomast.MakeNegation(e.constraintsToExpressions(x.C))
	case *tomast.IsSortConstraintConstraint:
		return tomast.MakeIsSort(x.AstType, x.BQTerm)
	case *tomast.EmptyArrayConstraintConstraint:
		return tomast.MakeIsEmptyArray(x.Opname, x.Variable, x.Index)
	case *tomast.EmptyListConstraintConstraint:
		return tomast.MakeIsEmptyList(x.Opname, x.Variable)
	}
	// Unknown shape — emit TrueTL to keep the AST well-formed.
	return tomast.MakeTrueTL()
}

// foldExprs builds an n-ary And / OrConnector / OrExpressionDisjunction
// tree from a slice of constraints. `binAnd` chooses between And and
// the two Or variants. Empty slices fold to TrueTL (And) / FalseTL
// (Or), matching Java's behaviour at the AST level when the recursion
// hits the empty case.
func (e *compilerEnv) foldExprs(items []tomast.Constraint, binAnd, disjunction bool) tomast.Expression {
	if len(items) == 0 {
		if binAnd {
			return tomast.MakeTrueTL()
		}
		return tomast.MakeFalseTL()
	}
	if len(items) == 1 {
		return e.constraintsToExpressions(items[0])
	}
	head := e.constraintsToExpressions(items[0])
	tail := e.foldExprs(items[1:], binAnd, disjunction)
	switch {
	case binAnd:
		return tomast.MakeAnd(head, tail)
	case disjunction:
		return tomast.MakeOrExpressionDisjunction(head, tail)
	default:
		return tomast.MakeOrConnector(head, tail)
	}
}
