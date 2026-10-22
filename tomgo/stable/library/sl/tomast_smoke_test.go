package sl_test

import (
	"fmt"
	"strings"
	"testing"

	"tom/tomgo/stable/library/sl"
	"tom/tomgo/stable/library/tomast"
)

// TestVisitable_OnTomast is the end-to-end Phase 6.5 smoke test: build
// a real tomast.* tree, then walk it with VisitableIntrospector and
// confirm the generated Children/SetChildren/ChildCount/ChildAt/
// SetChildAt methods behave correctly. This proves the strategy
// library can drive the engine ADT in production without any
// hand-rolled introspector.
func TestVisitable_OnTomast(t *testing.T) {
	// Build a small TomTerm: Variable(concOption(), Name("x"),
	// Type(concTypeOption(), "T", EmptyTargetLanguageType()),
	// concConstraint()).
	v := tomast.MakeVariable(
		tomast.MakeConcOption(),
		tomast.MakeName("x"),
		tomast.MakeType(
			tomast.MakeConcTypeOption(),
			"T",
			tomast.MakeEmptyTargetLanguageType(),
		),
		tomast.MakeConcConstraint(),
	)

	intro := sl.VisitableIntrospector{}

	// 4 slots: Options, AstName, AstType, Constraints.
	if got := intro.GetChildCount(v); got != 4 {
		t.Errorf("Variable.ChildCount = %d, want 4", got)
	}

	// ChildAt(1) → Name("x").
	got := intro.GetChildAt(v, 1)
	name, ok := got.(tomast.TomName)
	if !ok {
		t.Fatalf("ChildAt(1) should return a TomName, got %T", got)
	}
	if !strings.Contains(fmt.Sprintf("%v", name), `"x"`) {
		t.Errorf("expected Name(\"x\") at slot 1, got %v", name)
	}
}

// TestStrategyOnTomast applies MakeTopDownIdStopOnSuccess with a
// rewrite that renames every Variable whose AstName is EmptyName().
// On a Variable carrying EmptyName, the strategy fires once; the
// rewrite returns a Variable with a fresh Name. The result must
// reflect that change.
func TestStrategyOnTomast(t *testing.T) {
	// Build Variable(_, EmptyName(), Type("?", _), _).
	v := tomast.MakeVariable(
		tomast.MakeConcOption(),
		tomast.MakeEmptyName(),
		tomast.MakeType(
			tomast.MakeConcTypeOption(),
			"unknown type",
			tomast.MakeEmptyTargetLanguageType(),
		),
		tomast.MakeConcConstraint(),
	)

	// Rewrite: if subject is a Variable whose AstName is EmptyName(),
	// return a copy with Name("fresh1") instead.
	rewrite := &renameEmpty{}
	// ChoiceId propagates failure (Java): rewrite must be non-failing.
	walker := sl.MakeTopDownIdStopOnSuccess(sl.MakeTry(rewrite))

	got, err := walker.VisitLight(v, sl.VisitableIntrospector{})
	if err != nil {
		t.Fatalf("walker: %v", err)
	}

	// The result should be a Variable whose name is "fresh1".
	out, ok := got.(*tomast.VariableTomTerm)
	if !ok {
		t.Fatalf("expected Variable result, got %T", got)
	}
	if !strings.Contains(fmt.Sprintf("%v", out.AstName), `"fresh1"`) {
		t.Errorf("expected fresh1 in result, got %v", out)
	}
}

// renameEmpty matches Java's DesugarUnderscore on a single fixture:
// any Variable with EmptyName() becomes Variable(..., Name("fresh1"),
// ...). The strategy fails on anything else so MakeTopDownIdStopOnSuccess
// keeps walking
// once wrapped in sl.MakeTry (ChoiceId propagates failure, as in Java).
type renameEmpty struct{ sl.AbstractStrategy }

func (*renameEmpty) VisitLight(subject any, _ sl.Introspector) (any, error) {
	v, ok := subject.(*tomast.VariableTomTerm)
	if !ok {
		return nil, sl.ErrVisitFailure
	}
	if _, empty := v.AstName.(*tomast.EmptyNameTomName); !empty {
		return nil, sl.ErrVisitFailure
	}
	return tomast.MakeVariable(
		v.Options,
		tomast.MakeName("fresh1"),
		v.AstType,
		v.Constraints,
	), nil
}

func (*renameEmpty) ChildCount() int                  { return 0 }
func (*renameEmpty) ChildAt(int) sl.Strategy          { return nil }
func (*renameEmpty) SetChildAt(int, sl.Strategy)      {}
func (r *renameEmpty) VisitEnv(i sl.Introspector) int { return sl.VisitBasic(r, i) }
func (r *renameEmpty) Visit(subject any, i sl.Introspector) (any, error) {
	return sl.VisitStrategy(r, subject, i)
}
func (r *renameEmpty) VisitWith(env *sl.Environment, i sl.Introspector) (any, error) {
	return sl.VisitStrategyWith(r, env, i)
}

// TestCongruenceOnTomast_VisitAgreesWithVisitLight runs the Gom
// generated congruence (`_Variable`, Go VisitVariable) and predicate
// (`Is_Variable`) strategies with both visits.
func TestCongruenceOnTomast_VisitAgreesWithVisitLight(t *testing.T) {
	v := tomast.MakeVariable(
		tomast.MakeConcOption(),
		tomast.MakeEmptyName(),
		tomast.MakeType(tomast.MakeConcTypeOption(), "unknown type", tomast.MakeEmptyTargetLanguageType()),
		tomast.MakeConcConstraint(),
	)
	intro := sl.VisitableIntrospector{}
	mk := func() sl.Strategy {
		return tomast.NewVisitVariable(sl.NewIdentity(), sl.MakeTry(&renameName{}), sl.NewIdentity(), sl.NewIdentity())
	}
	light, err := mk().VisitLight(v, intro)
	if err != nil {
		t.Fatal(err)
	}
	env, err := mk().Visit(v, intro)
	if err != nil {
		t.Fatal(err)
	}
	if light != env || !strings.Contains(fmt.Sprintf("%v", env), `"fresh1"`) {
		t.Fatalf("VisitLight = %v, Visit = %v", light, env)
	}
	if _, err := (&tomast.IsVariable{}).Visit(v, intro); err != nil {
		t.Fatalf("IsVariable.Visit: %v", err)
	}
	if _, err := (&tomast.IsTermAppl{}).Visit(v, intro); err == nil {
		t.Fatal("IsTermAppl.Visit must fail on a Variable")
	}
	if _, err := mk().Visit(tomast.MakeEmptyName(), intro); err == nil {
		t.Fatal("VisitVariable.Visit must fail on a non-Variable")
	}
}

// renameName rewrites EmptyName() into Name("fresh1").
type renameName struct{ sl.AbstractStrategy }

func (*renameName) VisitLight(subject any, _ sl.Introspector) (any, error) {
	if _, ok := subject.(*tomast.EmptyNameTomName); ok {
		return tomast.MakeName("fresh1"), nil
	}
	return nil, sl.ErrVisitFailure
}
func (*renameName) ChildCount() int                               { return 0 }
func (*renameName) ChildAt(int) sl.Strategy                       { return nil }
func (*renameName) SetChildAt(int, sl.Strategy)                   {}
func (r *renameName) VisitEnv(i sl.Introspector) int              { return sl.VisitBasic(r, i) }
func (r *renameName) Visit(s any, i sl.Introspector) (any, error) { return sl.VisitStrategy(r, s, i) }
func (r *renameName) VisitWith(env *sl.Environment, i sl.Introspector) (any, error) {
	return sl.VisitStrategyWith(r, env, i)
}
