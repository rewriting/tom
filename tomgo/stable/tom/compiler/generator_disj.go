// Package compiler — disjunction + anti-match lowering.
//
// Java references:
//   - ConstraintGenerator.buildExpressionDisjunction (lines 260-300)
//   - ConstraintGenerator.buildDisjunctionIfElse    (lines 280-300)
//   - ConstraintGenerator.buildAntiMatchInstruction (lines 313-322)
//
// Both shapes thread a synthesised boolean "match successful" flag.
// The disjunction generates one if/else-if chain per branch; on the
// first branch that succeeds, the flag is set true and the rest of
// the chain is skipped. At the end, when the flag is true, the
// action runs. The anti-match is the mirror image (action runs when
// the flag stays false).

package compiler

import (
	"tom/tomgo/stable/library/tomast"
)

// buildExpressionDisjunction — port of Java's buildExpressionDisjunction.
//
// Shape produced:
//
//	LetRef(flag, FalseTL(),
//	  LetRef(freshVar1, Bottom(type1),
//	    ...
//	      AbstractBlock([
//	        ifElseChain,
//	        If(flag, action, Nop()),
//	      ])))
//
// The freshVar pre-declarations are critical: each disjunct branch
// assigns to those vars (so the if-chain selects one of N versions),
// and the action body needs them in scope OUTSIDE the chain.
func (env *compilerEnv) buildExpressionDisjunction(or *tomast.OrExpressionDisjunctionExpression, action tomast.Instruction) tomast.Instruction {
	flag := env.getFreshVariable(env.booleanType())
	assignFlagTrue := tomast.MakeAssign(flag, tomast.MakeTrueTL())

	// Collect every Variable that any disjunct's MatchConstraint
	// uses as its Pattern. These need to be pre-declared so they
	// stay in scope after the chain.
	freshVars := collectMatchVars(or)

	// Stash the names so buildDisjunctionIfElse can convert
	// matching LetRefs to Assigns inside each branch.
	prev := env.disjunctionVars
	env.disjunctionVars = map[string]bool{}
	for _, v := range freshVars {
		env.disjunctionVars[bqVarName(v)] = true
	}
	defer func() { env.disjunctionVars = prev }()

	chain := env.buildDisjunctionIfElse(or.Slots, assignFlagTrue)
	body := tomast.MakeAbstractBlock(tomast.MakeConcInstruction(
		chain,
		tomast.MakeIf(tomast.MakeBQTermToExpression(flag), action, tomast.MakeNop()),
	))
	// Wrap with LetRef declarations (innermost outwards).
	for i := len(freshVars) - 1; i >= 0; i-- {
		bq := freshVars[i]
		body = tomast.MakeLetRef(bq, tomast.MakeBottom(bqTypeOf(bq)), body)
	}
	return tomast.MakeLetRef(flag, tomast.MakeFalseTL(), body)
}

// collectMatchVars — Go port of Java's CollectVar strategy. Walks an
// Expression looking for `ConstraintToExpression(MatchConstraint(
// Variable|VariableStar v, _))`, returning each `v` converted to a
// BQVariable / BQVariableStar. Deduplicates by name.
func collectMatchVars(expr tomast.Expression) []tomast.BQTerm {
	seen := map[string]bool{}
	var out []tomast.BQTerm
	var walkExpr func(tomast.Expression)
	walkExpr = func(e tomast.Expression) {
		switch x := e.(type) {
		case *tomast.AndExpression:
			walkExpr(x.Arg1)
			walkExpr(x.Arg2)
		case *tomast.OrConnectorExpression:
			for _, s := range x.Slots {
				walkExpr(s)
			}
		case *tomast.OrExpressionDisjunctionExpression:
			for _, s := range x.Slots {
				walkExpr(s)
			}
		case *tomast.NegationExpression:
			walkExpr(x.Arg)
		case *tomast.AntiMatchExpressionExpression:
			// Don't descend under anti — matches Java's CollectFreeVar.
		case *tomast.ConstraintToExpressionExpression:
			if mc, ok := x.Cons.(*tomast.MatchConstraintConstraint); ok {
				switch p := mc.Pattern.(type) {
				case *tomast.VariableTomTerm:
					nm := tomNameKey(p.AstName)
					if nm != "" && !seen[nm] {
						seen[nm] = true
						out = append(out, tomast.MakeBQVariable(p.Options, p.AstName, p.AstType))
					}
				case *tomast.VariableStarTomTerm:
					nm := tomNameKey(p.AstName)
					if nm != "" && !seen[nm] {
						seen[nm] = true
						out = append(out, tomast.MakeBQVariableStar(p.Options, p.AstName, p.AstType))
					}
				}
			}
		}
	}
	walkExpr(expr)
	return out
}

// downgradeLetRefsForKnownNames walks the instruction tree and
// replaces each `LetRef(BQVariable(name), src, body)` whose name is
// in `known` with `AbstractBlock([Assign(var, src), body])`. Java's
// PostGenerator does the same via Omega root-path lookup; this
// local version covers the disjunction-branch case.
func downgradeLetRefsForKnownNames(i tomast.Instruction, known map[string]bool) tomast.Instruction {
	if known == nil || len(known) == 0 {
		return i
	}
	switch x := i.(type) {
	case *tomast.LetRefInstruction:
		name := bqVarName(x.Variable)
		newBody := downgradeLetRefsForKnownNames(x.AstInstruction, known)
		if known[name] {
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(
				tomast.MakeAssign(x.Variable, x.Source),
				newBody,
			))
		}
		if newBody != x.AstInstruction {
			return tomast.MakeLetRef(x.Variable, x.Source, newBody)
		}
	case *tomast.LetInstruction:
		newBody := downgradeLetRefsForKnownNames(x.AstInstruction, known)
		if newBody != x.AstInstruction {
			return tomast.MakeLet(x.Variable, x.Source, newBody)
		}
	case *tomast.IfInstruction:
		t := downgradeLetRefsForKnownNames(x.SuccesInst, known)
		f := downgradeLetRefsForKnownNames(x.FailureInst, known)
		if t != x.SuccesInst || f != x.FailureInst {
			return tomast.MakeIf(x.Condition, t, f)
		}
	case *tomast.AbstractBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := downgradeLetRefsForKnownNames(s, known)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeAbstractBlock(tomast.MakeConcInstruction(newSlots...))
		}
	case *tomast.UnamedBlockInstruction:
		list, ok := x.InstList.(*tomast.ConcInstructionInstructionList)
		if !ok {
			return i
		}
		newSlots := make([]tomast.Instruction, len(list.Slots))
		changed := false
		for k, s := range list.Slots {
			r := downgradeLetRefsForKnownNames(s, known)
			if r != s {
				changed = true
			}
			newSlots[k] = r
		}
		if changed {
			return tomast.MakeUnamedBlock(tomast.MakeConcInstruction(newSlots...))
		}
	}
	return i
}

// bqTypeOf returns the AstType of a BQVariable / BQVariableStar.
func bqTypeOf(b tomast.BQTerm) tomast.TomType {
	switch x := b.(type) {
	case *tomast.BQVariableBQTerm:
		return x.AstType
	case *tomast.BQVariableStarBQTerm:
		return x.AstType
	}
	return tomast.MakeEmptyType()
}

// buildDisjunctionIfElse — port of Java's buildDisjunctionIfElse.
//
// Walks the disjunct list and builds a chain of nested If/else nodes.
// Each disjunct is of shape `And(check, assign)` — typically
// `IsFsym(...)` test followed by per-slot assignments — so the
// branch first checks `check`, then on success commits the
// assignments and sets the flag; on failure recurses into the next
// disjunct.
func (env *compilerEnv) buildDisjunctionIfElse(slots []tomast.Expression, assignFlagTrue tomast.Instruction) tomast.Instruction {
	if len(slots) == 0 {
		return tomast.MakeNop()
	}
	head := slots[0]
	rest := slots[1:]
	subtest := env.buildDisjunctionIfElse(rest, assignFlagTrue)
	if and, ok := head.(*tomast.AndExpression); ok {
		check := and.Arg1
		assign := and.Arg2
		if _, isSort := check.(*tomast.IsSortExpression); isSort {
			// Java's first sub-case: pull the IsSort out as an If
			// guard around the rest of the disjunct.
			rebuilt := []tomast.Expression{assign}
			rebuilt = append(rebuilt, rest...)
			return tomast.MakeIf(check,
				env.buildDisjunctionIfElse(rebuilt, assignFlagTrue),
				tomast.MakeNop(),
			)
		}
		branchBody := env.generateAutomata(assign, tomast.MakeNop())
		// Each pre-declared var (collected by collectMatchVars in
		// buildExpressionDisjunction) needs its inner LetRef
		// downgraded to Assign so the outer declaration captures
		// the value. Java does this via PostGenerator + Omega
		// paths; the Go port does it locally on each branch.
		branchBody = downgradeLetRefsForKnownNames(branchBody, env.disjunctionVars)
		return tomast.MakeIf(check,
			tomast.MakeUnamedBlock(tomast.MakeConcInstruction(
				assignFlagTrue,
				branchBody,
			)),
			subtest,
		)
	}
	// Plain check (no `And` wrapping) — set the flag if it succeeds.
	return tomast.MakeIf(head, assignFlagTrue, subtest)
}

// buildAntiMatchInstruction — port of Java's buildAntiMatchInstruction
// (lines 313-322).
//
// Shape produced:
//
//	LetRef(flag, FalseTL(),
//	  AbstractBlock([
//	    generateAutomata(expression, Assign(flag, TrueTL())),
//	    If(Negation(flag), action, Nop()),
//	  ]))
func (env *compilerEnv) buildAntiMatchInstruction(expr tomast.Expression, action tomast.Instruction) tomast.Instruction {
	flag := env.getFreshVariable(env.booleanType())
	assignFlagTrue := tomast.MakeAssign(flag, tomast.MakeTrueTL())
	automata := env.generateAutomata(expr, assignFlagTrue)
	body := tomast.MakeAbstractBlock(tomast.MakeConcInstruction(
		automata,
		tomast.MakeIf(
			tomast.MakeNegation(tomast.MakeBQTermToExpression(flag)),
			action,
			tomast.MakeNop(),
		),
	))
	return tomast.MakeLetRef(flag, tomast.MakeFalseTL(), body)
}

// booleanType returns the canonical Tom type used for boolean flag
// variables. Java looks it up via `SymbolTable.getBooleanType()`; we
// synthesise the same shape directly so the lowering works without a
// fully-populated table.
func (env *compilerEnv) booleanType() tomast.TomType {
	return tomast.MakeType(
		tomast.MakeConcTypeOption(),
		"boolean",
		tomast.MakeTLType("boolean"),
	)
}
