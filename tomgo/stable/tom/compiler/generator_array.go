// Package compiler — ArrayGenerator.
//
// Java reference: stable/tom/engine/compiler/generator/ArrayGenerator.t
// (120 LOC).
//
// Rewrites the array-shaped ConstraintToExpression wrappers that
// ArrayPropagator left behind:
//
//	ConstraintToExpression(MatchConstraint(VariableStar v,
//	    VariableHeadArray(op, subject, begin, end), T)) →
//	  And(
//	    DoWhileExpression(
//	      ConstraintToExpression(MatchConstraint(end, AddOne(end), T)),
//	      Negation(GreaterThan(end, GetSize(op, subject))),
//	    ),
//	    ConstraintToExpression(MatchConstraint(v,
//	      GetSliceArray(op, subject, begin, end), T)),
//	  )
//
// The DoWhileExpression is consumed by `generateAutomata` later and
// produces a do/while loop in the host code; the second
// ConstraintToExpression then binds the sublist (a LetRef).

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// generateArray walks an Expression top-down and rewrites array-shape
// `ConstraintToExpression`s.
func (env *compilerEnv) generateArray(e tomast.Expression) tomast.Expression {
	return walkExpressionConnectives(e, env.generateArray, env.tryGenerateArray)
}

// tryGenerateArray rewrites one ConstraintToExpression carrying an
// array-loop shape. Two patterns fire:
//
//   - MatchConstraint(VariableStar v, VariableHeadArray(...)) — the
//     primary case; emits the loop driver + the LetRef binding the
//     star to the slice value.
//   - MatchConstraint(Variable v, VariableHeadArray(...)) — alias
//     case from GP detach. Rewrites the binding's subject from the
//     loop-driving VariableHeadArray to the slice value so the
//     alias gets the same content as the star.
func (env *compilerEnv) tryGenerateArray(cte *tomast.ConstraintToExpressionExpression) (tomast.Expression, bool) {
	mc, ok := cte.Cons.(*tomast.MatchConstraintConstraint)
	if !ok {
		return cte, false
	}
	headArr, ok := mc.Subject.(*tomast.VariableHeadArrayBQTerm)
	if !ok {
		return cte, false
	}
	opName := headArr.Opname
	subject := headArr.Subject
	begin := headArr.BeginIndex
	end := headArr.EndIndex
	aType := mc.AstType

	// Alias case: Variable pattern with VariableHeadArray subject.
	// Substitute the subject with GetSliceArray so we get a single
	// slice fetch — Java's GP detach does this via a fresh star.
	if v, isVar := mc.Pattern.(*tomast.VariableTomTerm); isVar {
		return tomast.MakeConstraintToExpression(
			tomast.MakeMatchConstraint(
				v,
				tomast.MakeExpressionToBQTerm(
					tomast.MakeGetSliceArray(opName, subject, begin, end),
				),
				aType,
			),
		), true
	}

	v, isVarStar := mc.Pattern.(*tomast.VariableStarTomTerm)
	if !isVarStar {
		return cte, false
	}

	doWhileTest := tomast.MakeNegation(
		tomast.MakeGreaterThan(
			tomast.MakeBQTermToExpression(end),
			tomast.MakeGetSize(opName, subject),
		),
	)
	// end = end + 1 (advance the index at the end of each iteration)
	endVarTomTerm := convertBQVarToVar(end)
	endExpression := tomast.MakeConstraintToExpression(
		tomast.MakeMatchConstraint(
			endVarTomTerm,
			tomast.MakeExpressionToBQTerm(tomast.MakeAddOne(end)),
			aType,
		),
	)

	// The pattern was an X* — declare X = GetSliceArray(...)
	// outside the loop (it'll be a LetRef via generator).
	varDecl := tomast.MakeConstraintToExpression(
		tomast.MakeMatchConstraint(
			v,
			tomast.MakeExpressionToBQTerm(
				tomast.MakeGetSliceArray(opName, subject, begin, end),
			),
			aType,
		),
	)
	return tomast.MakeAnd(
		tomast.MakeDoWhileExpression(endExpression, doWhileTest),
		varDecl,
	), true
}
