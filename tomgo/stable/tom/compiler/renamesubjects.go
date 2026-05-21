// Package compiler — renameSubjects strategy.
//
// Java reference: Compiler.t:274-358 (renameSubjects strategy).
//
// Goal: ensure each subject expression is evaluated exactly once,
// even when shared by multiple patterns. Java's implementation walks
// the Constraint top-down (stop-on-success) and at every
// MatchConstraint(p, s, T):
//
//   1. If `s` is already in subjectList → re-use the previously
//      generated renamed subject.
//   2. Otherwise → generate a fresh BQVariable `_freshSubject_N`
//      (typed as T), add the entry to (subjectList, renamedSubjects).
//
// In both cases the constraint is wrapped in:
//
//     AndConstraint(
//       MatchConstraint(toVar(freshVar), s,       freshSubjectType),
//       IsSortConstraint(castType,       freshVar),
//       MatchConstraint(renamedVar,
//         ExpressionToBQTerm(Cast(freshSubjectType,
//           BQTermToExpression(freshVar))),
//         freshSubjectType),
//       <originalConstraint with Subject = renamedVar>,
//     )
//
// This Go port covers the common case where the subject is a plain
// BQVariable (Peano-class fixtures). Other subject shapes
// (BuildTerm, FunctionCall, …) fall back to identity for now and
// will be filled in when needed.

package compiler

import (
	"fmt"

	"tom/tomgo/stable/library/tomast"
)

// renameSubjects walks a Constraint top-down, applying the subject
// hoist at every MatchConstraint encountered.
//
// `subjectList` and `renamedSubjects` are caller-owned slices that
// accumulate across all ConstraintInstructions of the same Match —
// they let the function notice repeated subjects across patterns
// and reuse the same fresh subject. CompileMatch passes the same
// pair to every call within a Match.
func (env *compilerEnv) renameSubjects(
	c tomast.Constraint,
	subjectList *[]tomast.BQTerm,
	renamedSubjects *[]tomast.TomTerm,
) tomast.Constraint {
	switch x := c.(type) {
	case *tomast.MatchConstraintConstraint:
		return env.renameOneMatchConstraint(x, subjectList, renamedSubjects)
	case *tomast.AndConstraintConstraint:
		newSlots := make([]tomast.Constraint, len(x.Slots))
		for i, item := range x.Slots {
			newSlots[i] = env.renameSubjects(item, subjectList, renamedSubjects)
		}
		return tomast.MakeAndConstraint(newSlots...)
	case *tomast.OrConstraintConstraint:
		newSlots := make([]tomast.Constraint, len(x.Slots))
		for i, item := range x.Slots {
			newSlots[i] = env.renameSubjects(item, subjectList, renamedSubjects)
		}
		return tomast.MakeOrConstraint(newSlots...)
	}
	return c
}

// renameOneMatchConstraint expands a single MatchConstraint into
// the four-clause AndConstraint described above. The minimal port
// handles the case where the subject is a `BQVariableBQTerm` —
// enough for Peano-style fixtures.
func (env *compilerEnv) renameOneMatchConstraint(
	m *tomast.MatchConstraintConstraint,
	subjectList *[]tomast.BQTerm,
	renamedSubjects *[]tomast.TomTerm,
) tomast.Constraint {
	subject := m.Subject
	castType := m.AstType

	// We only know how to hoist BQVariable subjects right now. For
	// other shapes return the original constraint unchanged.
	subjVar, ok := subject.(*tomast.BQVariableBQTerm)
	if !ok {
		return m
	}
	freshSubjectType := subjVar.AstType

	// Look for an existing renamed subject for the same source.
	for i, s := range *subjectList {
		if existing, ok := s.(*tomast.BQVariableBQTerm); ok && existing == subjVar {
			renamed := (*renamedSubjects)[i]
			return env.wrapWithHoist(m, subject, renamed, freshSubjectType, castType)
		}
	}

	// Generate a fresh subject variable.
	freshSubjectName := env.buildFreshSubjectName()
	renamedVar := tomast.MakeVariable(
		tomast.MakeConcOption(),
		freshSubjectName,
		freshSubjectType,
		tomast.MakeConcConstraint(),
	)
	*subjectList = append(*subjectList, subject)
	*renamedSubjects = append(*renamedSubjects, renamedVar)

	return env.wrapWithHoist(m, subject, renamedVar, freshSubjectType, castType)
}

// wrapWithHoist produces the four-clause AndConstraint Java emits in
// Compiler.t:308-313 (current "subject is fresh" path), which is also
// reused in the "subject already seen" branch with the same shape.
//
// Layout:
//
//	AndConstraint(
//	  MatchConstraint(freshVar,   originalSubject, freshSubjectType),
//	  IsSortConstraint(castType,  freshVarBQ),
//	  MatchConstraint(renamedVar, Cast(freshSubjectType, freshVarBQ),
//	                              freshSubjectType),
//	  <originalConstraint with Subject = renamedVarBQ>,
//	)
func (env *compilerEnv) wrapWithHoist(
	original *tomast.MatchConstraintConstraint,
	originalSubject tomast.BQTerm,
	renamedVar tomast.TomTerm,
	freshSubjectType, castType tomast.TomType,
) tomast.Constraint {
	freshVarBQ := env.getFreshVariable(freshSubjectType)
	freshVarTomTerm := convertBQVarToVar(freshVarBQ)
	renamedVarBQ := convertVarToBQVar(renamedVar)

	c1 := tomast.MakeMatchConstraint(freshVarTomTerm, originalSubject, freshSubjectType)
	c2 := tomast.MakeIsSortConstraint(castType, freshVarBQ)
	c3 := tomast.MakeMatchConstraint(
		renamedVar,
		tomast.MakeExpressionToBQTerm(
			tomast.MakeCast(freshSubjectType,
				tomast.MakeBQTermToExpression(freshVarBQ),
			),
		),
		freshSubjectType,
	)
	c4 := tomast.MakeMatchConstraint(original.Pattern, renamedVarBQ, original.AstType)

	return tomast.MakeAndConstraint(c1, c2, c3, c4)
}

// buildFreshSubjectName produces `PositionName(concTomNumber(
// rootpath*, NameNumber(Name("_freshSubject_<n>"))))` — Java
// Compiler.t:317.
func (env *compilerEnv) buildFreshSubjectName() tomast.TomName {
	n := env.genFreshSubjectCounter()
	parts := []tomast.TomNumber{}
	if rp, ok := env.rootpath.(*tomast.ConcTomNumberTomNumberList); ok {
		parts = append(parts, rp.Slots...)
	}
	parts = append(parts,
		tomast.MakeNameNumber(tomast.MakeName(fmt.Sprintf("_freshSubject_%d", n))),
	)
	return tomast.MakePositionName(tomast.MakeConcTomNumber(parts...))
}

// getFreshVariable mirrors Java Compiler.t:420-424. Returns a fresh
// BQVariable named via `buildVariableName("_<n>")`, which produces
// `PositionName(concTomNumber(rootpath*, NameNumber(Name("_<n>"))))`
// — rendered by the Backend as e.g. `tomMatch1_0`, `tomMatch2_5`.
func (env *compilerEnv) getFreshVariable(t tomast.TomType) tomast.BQTerm {
	n := env.genFreshVarCounter()
	return tomast.MakeBQVariable(
		tomast.MakeConcOption(),
		env.buildVariableName(fmt.Sprintf("_%d", n)),
		t,
	)
}

// getFreshVariableStar — Java Compiler.t:435-439. Same as
// getFreshVariable but produces a BQVariableStar. Used by
// ArrayPropagator / VariadicPropagator for sublist captures.
func (env *compilerEnv) getFreshVariableStar(t tomast.TomType) tomast.BQTerm {
	n := env.genFreshVarCounter()
	return tomast.MakeBQVariableStar(
		tomast.MakeConcOption(),
		env.buildVariableName(fmt.Sprintf("_%d", n)),
		t,
	)
}

// getBeginVariableStar — Java Compiler.t (same module). Returns a
// fresh BQVariableStar named `_begin_<n>`. Used by ArrayPropagator
// for sublist begin indices.
func (env *compilerEnv) getBeginVariableStar(t tomast.TomType) tomast.BQTerm {
	n := env.genFreshVarCounter()
	return tomast.MakeBQVariableStar(
		tomast.MakeConcOption(),
		env.buildVariableName(fmt.Sprintf("_begin_%d", n)),
		t,
	)
}

// getEndVariableStar — Java Compiler.t (same module). Returns a
// fresh BQVariableStar named `_end_<n>`. Used by ArrayPropagator
// for sublist end indices.
func (env *compilerEnv) getEndVariableStar(t tomast.TomType) tomast.BQTerm {
	n := env.genFreshVarCounter()
	return tomast.MakeBQVariableStar(
		tomast.MakeConcOption(),
		env.buildVariableName(fmt.Sprintf("_end_%d", n)),
		t,
	)
}

// buildVariableName builds a `PositionName(concTomNumber(rootpath*,
// NameNumber(Name(suffix))))` — Java's
// `Compiler.buildVariableName(prefix, name)`.
func (env *compilerEnv) buildVariableName(suffix string) tomast.TomName {
	parts := []tomast.TomNumber{}
	if rp, ok := env.rootpath.(*tomast.ConcTomNumberTomNumberList); ok {
		parts = append(parts, rp.Slots...)
	}
	parts = append(parts,
		tomast.MakeNameNumber(tomast.MakeName(suffix)),
	)
	return tomast.MakePositionName(tomast.MakeConcTomNumber(parts...))
}

// intType returns the canonical Tom int type. Mirrors
// SymbolTable.getIntType().
func (env *compilerEnv) intType() tomast.TomType {
	return tomast.MakeType(
		tomast.MakeConcTypeOption(),
		"int",
		tomast.MakeTLType("int"),
	)
}

// convertBQVarToVar — Java's TomBase.convertFromBQVarToVar. Promotes
// a BQVariable / BQVariableStar to a Variable / VariableStar
// (TomTerm sort).
func convertBQVarToVar(b tomast.BQTerm) tomast.TomTerm {
	switch v := b.(type) {
	case *tomast.BQVariableBQTerm:
		return tomast.MakeVariable(
			v.Options, v.AstName, v.AstType,
			tomast.MakeConcConstraint(),
		)
	case *tomast.BQVariableStarBQTerm:
		return tomast.MakeVariableStar(
			v.Options, v.AstName, v.AstType,
			tomast.MakeConcConstraint(),
		)
	}
	return nil
}

// convertVarToBQVar — Java's TomBase.convertFromVarToBQVar. The
// inverse of the above; loses the per-Variable Constraints list
// (matches Java's behaviour).
func convertVarToBQVar(t tomast.TomTerm) tomast.BQTerm {
	switch v := t.(type) {
	case *tomast.VariableTomTerm:
		return tomast.MakeBQVariable(v.Options, v.AstName, v.AstType)
	case *tomast.VariableStarTomTerm:
		return tomast.MakeBQVariableStar(v.Options, v.AstName, v.AstType)
	}
	return nil
}
