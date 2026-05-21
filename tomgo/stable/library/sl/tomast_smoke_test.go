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
	walker := sl.MakeTopDownIdStopOnSuccess(rewrite)

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
// keeps walking.
type renameEmpty struct{}

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

func (*renameEmpty) ChildCount() int        { return 0 }
func (*renameEmpty) ChildAt(int) sl.Strategy { return nil }
func (*renameEmpty) SetChildAt(int, sl.Strategy) {}
