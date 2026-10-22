// Package compiler — ConstraintGenerator.
//
// Java reference: stable/tom/engine/compiler/ConstraintGenerator.t
// (508 LOC) + stable/tom/engine/compiler/generator/*.t (~580 LOC
// across SyntacticGenerator, ArrayGenerator, VariadicGenerator,
// ACGenerator, IBaseGenerator).
//
// `performGenerations` consumes an Expression (output of
// PreGenerator) and an action body, and emits the matching automaton
// as an Instruction tree (chains of Let / If / Assign / Cast /
// IsFsym / IsSort / Return / Nop).
//
// Status: SKELETON. Currently returns the unmodified action wrapped
// in an UnamedBlock so the rest of the compiler can hand it off
// without crashing. Real implementation lands with task #61
// (Syntactic generator first, then per-shape sub-generators).

package compiler

import "tom/tomgo/stable/library/tomast"

// performGenerations is the entry point used by CompileMatch. Java:
// ConstraintGenerator.performGenerations(Expression, Instruction).
//
// Java pipeline (lines 90-131 of ConstraintGenerator.t):
//
//  1. Run each sub-generator (AC, Syntactic, Variadic, Array) until
//     a full pass leaves the Expression unchanged. Each generator
//     rewrites the ConstraintToExpression wrappers its shape
//     produces.
//  2. Replace every `Subterm(name, slot, t)` BQTerm with
//     `ExpressionToBQTerm(GetSlot(...))`.
//  3. `generateAutomata(expression, action)` — folds the Expression
//     tree into an Instruction tree (Let / If / Assign / DoWhile /
//     AbstractBlock).
//
// The Go port currently wires only SyntacticGenerator. Variadic /
// Array / AC generators land later.
func (e *compilerEnv) performGenerations(expr tomast.Expression, action tomast.Instruction) tomast.Instruction {
	expr = e.generateSyntactic(expr)
	expr = e.generateVariadic(expr)
	expr = e.generateArray(expr)
	expr = e.replaceListHeadTail(expr)
	expr = e.replaceSubterms(expr)
	return e.generateAutomata(expr, action)
}

// generateAutomata — Go port of ConstraintGenerator.generateAutomata
// (lines 136-205).
//
// Walks an Expression tree producing an Instruction tree, threading
// `action` through the leaves:
//
//	And(l, r)        → generateAutomata(l, generateAutomata(r, action))
//	OrConnector(...) → AbstractBlock([generateAutomata(c, action) for c in slots])
//	ConstraintToExpression(MatchConstraint(Variable v, t)):
//	    if patternType ≠ subjectType:
//	      LetRef(toBQVar(v), Cast(patternType, BQTermToExpression(t)), action)
//	    else:
//	      LetRef(toBQVar(v), BQTermToExpression(t), action)
//	TomInstructionToExpression(i) → i
//	OrExpressionDisjunction(...) → buildExpressionDisjunction
//	AntiMatchExpression(e)       → buildAntiMatchInstruction
//	<anything else>              → If(expression, action, Nop())
//
// Minimal port — skips numeric constraints, ACMatchLoop, DoWhile;
// those will land when Array/Variadic/AC generators come in.
func (e *compilerEnv) generateAutomata(expr tomast.Expression, action tomast.Instruction) tomast.Instruction {
	switch x := expr.(type) {
	case *tomast.AndExpression:
		sub := e.generateAutomata(x.Arg2, action)
		return e.generateAutomata(x.Arg1, sub)

	case *tomast.OrConnectorExpression:
		// Duplicate the action across each disjunct.
		parts := make([]tomast.Instruction, len(x.Slots))
		for i, s := range x.Slots {
			parts[i] = e.generateAutomata(s, action)
		}
		return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(parts...))

	case *tomast.OrExpressionDisjunctionExpression:
		return e.buildExpressionDisjunction(x, action)

	case *tomast.AntiMatchExpressionExpression:
		return e.buildAntiMatchInstruction(x.Expression, action)

	case *tomast.DoWhileExpressionExpression:
		// Java's `DoWhileExpression(expr, cond)` lowers to:
		//   DoWhile(UnamedBlock(concInstruction(action, subInst)), cond)
		// where `subInst = generateAutomata(expr, Nop())`. The action
		// runs inside the loop body; each iteration advances the
		// loop counter via subInst.
		subInst := e.generateAutomata(x.EndExpression, tomast.MakeNop())
		return tomast.MakeDoWhile(
			tomast.MakeUnamedBlock(tomast.MakeConcInstruction(action, subInst)),
			x.LoopCondition,
		)

	case *tomast.ConstraintToExpressionExpression:
		if mc, ok := x.Cons.(*tomast.MatchConstraintConstraint); ok {
			if v, ok := mc.Pattern.(*tomast.VariableTomTerm); ok {
				return e.makeVariableLetRef(v, mc.Subject, action)
			}
			if v, ok := mc.Pattern.(*tomast.VariableStarTomTerm); ok {
				return e.makeVariableStarLetRef(v, mc.Subject, action)
			}
		}
		// Unhandled — fall through to the catch-all so the action is
		// at least gated by a test (likely TrueTL).
		return tomast.MakeIf(expr, action, tomast.MakeNop())

	case *tomast.TomInstructionToExpressionExpression:
		return x.Instruction

	default:
		// `If(expression, action, Nop())` — Java's catch-all.
		return tomast.MakeIf(expr, action, tomast.MakeNop())
	}
}

// makeVariableLetRef emits the Variable-binding shape (LetRef) Java
// uses for `MatchConstraint(Variable v, t)`. With or without Cast
// depending on whether the pattern and subject types match. Skips
// the Cast when the pattern type is unresolved (the tomast hook for
// MakeCast panics on "unknown type" — Java handles this via a
// separate type-checking pass that fills in the gap; we just leave
// the binding uncasted, which is correct for Java compilation).
func (e *compilerEnv) makeVariableLetRef(v *tomast.VariableTomTerm, subject tomast.BQTerm, action tomast.Instruction) tomast.Instruction {
	patType := v.AstType
	subjType := e.termTypeOfBQTerm(subject)
	bqVar := tomast.MakeBQVariable(v.Options, v.AstName, v.AstType)
	if !sameTomType(patType, subjType) && patType != nil && !isUnknownType(patType) {
		return tomast.MakeLetRef(bqVar,
			tomast.MakeCast(patType, tomast.MakeBQTermToExpression(subject)),
			action,
		)
	}
	return tomast.MakeLetRef(bqVar,
		tomast.MakeBQTermToExpression(subject),
		action,
	)
}

// makeVariableStarLetRef is the VariableStar variant.
func (e *compilerEnv) makeVariableStarLetRef(v *tomast.VariableStarTomTerm, subject tomast.BQTerm, action tomast.Instruction) tomast.Instruction {
	patType := v.AstType
	subjType := e.termTypeOfBQTerm(subject)
	bqVar := tomast.MakeBQVariableStar(v.Options, v.AstName, v.AstType)
	if !sameTomType(patType, subjType) && patType != nil && !isUnknownType(patType) {
		return tomast.MakeLetRef(bqVar,
			tomast.MakeCast(patType, tomast.MakeBQTermToExpression(subject)),
			action,
		)
	}
	return tomast.MakeLetRef(bqVar,
		tomast.MakeBQTermToExpression(subject),
		action,
	)
}

// isUnknownType returns true when `t` is the "unknown type" sentinel
// (`Type(_, "unknown type", _)`) that the parser stamps into
// untyped positions before the typer runs. The MakeCast hook
// panics on that — Java's parser uses TYPE_UNKNOWN too and the
// compiler knows to skip the cast in that case.
func isUnknownType(t tomast.TomType) bool {
	tt, ok := t.(*tomast.TypeTomType)
	if !ok {
		return false
	}
	return tt.TomType == "unknown type"
}
