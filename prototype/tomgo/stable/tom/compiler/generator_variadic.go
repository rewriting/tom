// Package compiler — VariadicGenerator.
//
// Java reference: stable/tom/engine/compiler/generator/VariadicGenerator.t
// (151 LOC).
//
// Rewrites two shapes left by VariadicPropagator:
//
//   - Expression-level: `ConstraintToExpression(MatchConstraint(
//     VariableStar v, VariableHeadList(op, begin, end), T))` →
//     `And(DoWhileExpression(endExpr, doWhileTest), varDecl)` where
//        doWhileTest = !(end == begin)
//        endExpr     = if IsEmptyList(end): end = begin
//                      else                end = ListTail(end)
//        varDecl     = MatchConstraint(v, GetSliceList(op, begin, end,
//                                                       BuildEmptyList(op)), T)
//
//   - BQTerm-level: `ListHead(op, T, v)` / `ListTail(op, v)` →
//     `ExpressionToBQTerm(GetHead/GetTail(...))`.
//
// `MatchConstraint(Variable, VariableHeadList)` mirrors the array
// alias case — bind directly to GetSliceList for the single-fetch
// alias.

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// generateVariadic walks an Expression top-down and rewrites list-
// shape constraints.
func (env *compilerEnv) generateVariadic(e tomast.Expression) tomast.Expression {
	return walkExpressionConnectives(e, env.generateVariadic, env.tryGenerateVariadic)
}

// tryGenerateVariadic rewrites one ConstraintToExpression carrying
// a list-loop shape.
func (env *compilerEnv) tryGenerateVariadic(cte *tomast.ConstraintToExpressionExpression) (tomast.Expression, bool) {
	mc, ok := cte.Cons.(*tomast.MatchConstraintConstraint)
	if !ok {
		return cte, false
	}
	headList, ok := mc.Subject.(*tomast.VariableHeadListBQTerm)
	if !ok {
		return cte, false
	}
	opName := headList.Opname
	begin := headList.Begin
	end := headList.End
	aType := mc.AstType

	endVarType := bqAstType(end)

	// Alias case: Variable pattern → bind directly to slice.
	if v, isVar := mc.Pattern.(*tomast.VariableTomTerm); isVar {
		return tomast.MakeConstraintToExpression(
			tomast.MakeMatchConstraint(
				v,
				tomast.MakeExpressionToBQTerm(
					tomast.MakeGetSliceList(opName, begin, end,
						tomast.MakeBuildEmptyList(opName)),
				),
				aType,
			),
		), true
	}

	v, isVarStar := mc.Pattern.(*tomast.VariableStarTomTerm)
	if !isVarStar {
		return cte, false
	}

	// doWhileTest = !(end == begin)
	doWhileTest := tomast.MakeNegation(
		tomast.MakeEqualBQTerm(endVarType, end, begin),
	)
	// testEmpty = IsEmptyList(opName, end)
	testEmpty := tomast.MakeIsEmptyList(opName, end)
	// endExpression = If(testEmpty, Assign(end, BQTermToExpression(begin)),
	//                                 Assign(end, GetTail(opName, end)))
	endExpression := tomast.MakeTomInstructionToExpression(
		tomast.MakeIf(
			testEmpty,
			tomast.MakeAssign(end, tomast.MakeBQTermToExpression(begin)),
			tomast.MakeAssign(end, tomast.MakeGetTail(opName, end)),
		),
	)
	// varDecl = MatchConstraint(v, GetSliceList(opName, begin, end,
	//                                            BuildEmptyList(opName)), aType)
	varDecl := tomast.MakeConstraintToExpression(
		tomast.MakeMatchConstraint(
			v,
			tomast.MakeExpressionToBQTerm(
				tomast.MakeGetSliceList(opName, begin, end,
					tomast.MakeBuildEmptyList(opName)),
			),
			aType,
		),
	)
	return tomast.MakeAnd(
		tomast.MakeDoWhileExpression(endExpression, doWhileTest),
		varDecl,
	), true
}

// replaceListHeadTail walks an Expression / BQTerm tree and replaces
// `ListHead(op, T, v)` with `ExpressionToBQTerm(GetHead(op, T, v))`
// and `ListTail(op, v)` with `ExpressionToBQTerm(GetTail(op, v))`.
// Java's VariadicGenerator runs these as part of its strategy.
func (env *compilerEnv) replaceListHeadTail(e tomast.Expression) tomast.Expression {
	switch x := e.(type) {
	case *tomast.AndExpression:
		l := env.replaceListHeadTail(x.Arg1)
		r := env.replaceListHeadTail(x.Arg2)
		if l != x.Arg1 || r != x.Arg2 {
			return tomast.MakeAnd(l, r)
		}
	case *tomast.OrConnectorExpression:
		newSlots := make([]tomast.Expression, len(x.Slots))
		changed := false
		for i, s := range x.Slots {
			r := env.replaceListHeadTail(s)
			if r != s {
				changed = true
			}
			newSlots[i] = r
		}
		if changed {
			return tomast.MakeOrConnector(newSlots...)
		}
	case *tomast.OrExpressionDisjunctionExpression:
		newSlots := make([]tomast.Expression, len(x.Slots))
		changed := false
		for i, s := range x.Slots {
			r := env.replaceListHeadTail(s)
			if r != s {
				changed = true
			}
			newSlots[i] = r
		}
		if changed {
			return tomast.MakeOrExpressionDisjunction(newSlots...)
		}
	case *tomast.NegationExpression:
		inner := env.replaceListHeadTail(x.Arg)
		if inner != x.Arg {
			return tomast.MakeNegation(inner)
		}
	case *tomast.IsFsymExpression:
		newVar := env.replaceListHeadTailBQ(x.Variable)
		if newVar != x.Variable {
			return tomast.MakeIsFsym(x.AstName, newVar)
		}
	case *tomast.IsSortExpression:
		newVar := env.replaceListHeadTailBQ(x.Variable)
		if newVar != x.Variable {
			return tomast.MakeIsSort(x.AstType, newVar)
		}
	case *tomast.CastExpression:
		inner := env.replaceListHeadTail(x.Source)
		if inner != x.Source {
			return tomast.MakeCast(x.AstType, inner)
		}
	case *tomast.BQTermToExpressionExpression:
		newBQ := env.replaceListHeadTailBQ(x.AstTerm)
		if newBQ != x.AstTerm {
			return tomast.MakeBQTermToExpression(newBQ)
		}
	case *tomast.ConstraintToExpressionExpression:
		if mc, ok := x.Cons.(*tomast.MatchConstraintConstraint); ok {
			newSubj := env.replaceListHeadTailBQ(mc.Subject)
			if newSubj != mc.Subject {
				return tomast.MakeConstraintToExpression(
					tomast.MakeMatchConstraint(mc.Pattern, newSubj, mc.AstType),
				)
			}
		}
	}
	return e
}

// replaceListHeadTailBQ converts ListHead/ListTail BQTerms to
// the GetHead/GetTail expressions Java emits.
func (env *compilerEnv) replaceListHeadTailBQ(b tomast.BQTerm) tomast.BQTerm {
	switch x := b.(type) {
	case *tomast.ListHeadBQTerm:
		return tomast.MakeExpressionToBQTerm(
			tomast.MakeGetHead(x.Opname, x.Codomain, x.Variable),
		)
	case *tomast.ListTailBQTerm:
		return tomast.MakeExpressionToBQTerm(
			tomast.MakeGetTail(x.Opname, x.Variable),
		)
	case *tomast.ExpressionToBQTermBQTerm:
		newExp := env.replaceListHeadTail(x.Exp)
		if newExp != x.Exp {
			return tomast.MakeExpressionToBQTerm(newExp)
		}
	case *tomast.SymbolOfBQTerm:
		inner := env.replaceListHeadTailBQ(x.GroundTerm)
		if inner != x.GroundTerm {
			return tomast.MakeSymbolOf(inner)
		}
	}
	return b
}

// bqAstType extracts the AstType of a BQVariable / BQVariableStar.
// Returns EmptyType for shapes that don't carry one.
func bqAstType(b tomast.BQTerm) tomast.TomType {
	switch x := b.(type) {
	case *tomast.BQVariableBQTerm:
		return x.AstType
	case *tomast.BQVariableStarBQTerm:
		return x.AstType
	}
	return tomast.MakeEmptyType()
}
