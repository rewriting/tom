package sl

import (
	"errors"
	"testing"
)

// ----------------------------------------------------------------
// Phase 6.3 — AbstractStrategyBasic
// ----------------------------------------------------------------

// renameStrategyBasic embeds [AbstractStrategyBasic] to demonstrate the
// pattern user-generated %strategy code follows: a struct that
// overrides VisitLight to pattern-match on the subject and falls
// through to `Any()` for unhandled cases.
type renameStrategyBasic struct {
	*AbstractStrategyBasic
	from, to string
}

func newRenameStrategyBasic(from, to string) Strategy {
	return &renameStrategyBasic{
		AbstractStrategyBasic: NewAbstractStrategyBasic(NewIdentity()),
		from:                  from,
		to:                    to,
	}
}

func (r *renameStrategyBasic) VisitLight(subject any, intro Introspector) (any, error) {
	if n, ok := subject.(*node); ok && n.label == r.from {
		return &node{label: r.to, kids: n.kids}, nil
	}
	return r.Any().VisitLight(subject, intro)
}

func TestAbstractStrategyBasic_DispatchAndFallback(t *testing.T) {
	strat := newRenameStrategyBasic("a", "X")

	// Matching subject is rewritten.
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("rewrite: %v", err)
	}
	if got.(*node).label != "X" {
		t.Errorf("expected rewrite to X, got %q", got.(*node).label)
	}

	// Non-matching subject is returned via Any() = Identity.
	other := leaf("b")
	got, err = strat.VisitLight(other, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("identity fallback: %v", err)
	}
	if got != any(other) {
		t.Errorf("expected identity fallback to return same instance")
	}
}

func TestAbstractStrategyBasic_StrategyTreeShape(t *testing.T) {
	// AbstractStrategyBasic exposes its `any` as ChildAt(0), so a Mu
	// expansion walking through it can recurse into the default. Our
	// downstream %strategy port relies on this.
	a := NewAbstractStrategyBasic(NewIdentity())
	if a.ChildCount() != 1 {
		t.Errorf("AbstractStrategyBasic.ChildCount = %d, want 1", a.ChildCount())
	}
	if _, ok := a.ChildAt(0).(*Identity); !ok {
		t.Errorf("expected Identity at slot 0, got %T", a.ChildAt(0))
	}
}

// ----------------------------------------------------------------
// Phase 6.4 — built-in walks
// ----------------------------------------------------------------

func TestMakeTry_OnFailure(t *testing.T) {
	strat := MakeTry(NewFail())
	n := leaf("a")
	got, err := strat.VisitLight(n, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Try(Fail) should swallow failure, got %v", err)
	}
	if got != any(n) {
		t.Errorf("Try should return subject unchanged on failure")
	}
}

func TestMakeTry_OnSuccess(t *testing.T) {
	strat := MakeTry(renameStrategy("a", "Z"))
	got, _ := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if got.(*node).label != "Z" {
		t.Errorf("Try should pass through First-arm result, got %q", got.(*node).label)
	}
}

// TestMakeTopDown_RenameAll applies a leaf-rewrite at every node.
// The user-strategy never fails (returns identity for unmatched
// labels), so the whole TopDown succeeds.
func TestMakeTopDown_RenameAll(t *testing.T) {
	// MakeTopDown requires v to succeed at every node. Wrap with Try
	// to swallow non-matching nodes.
	rewrite := MakeTry(renameStrategy("a", "A"))
	strat := MakeTopDown(rewrite)

	root := parent("R", leaf("a"), parent("Inner", leaf("a"), leaf("b")))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("TopDown: %v", err)
	}
	want := "(R A (Inner A b))"
	if printNode(got.(*node)) != want {
		t.Errorf("TopDown result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestMakeBottomUp_RenameAll(t *testing.T) {
	rewrite := MakeTry(renameStrategy("a", "A"))
	strat := MakeBottomUp(rewrite)
	root := parent("R", leaf("a"), parent("Inner", leaf("a"), leaf("b")))
	got, _ := strat.VisitLight(root, VisitableIntrospector{})
	want := "(R A (Inner A b))"
	if printNode(got.(*node)) != want {
		t.Errorf("BottomUp result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

// TestMakeTopDownIdStopOnSuccess is the workhorse pattern for plugin
// rewrite rules: stop descending into a subtree as soon as v rewrites.
// On (R (Inner a) b), the deeply nested "a" is rewritten and the
// walk stops without scanning beyond.
func TestMakeTopDownIdStopOnSuccess(t *testing.T) {
	rewrite := renameStrategy("a", "X") // fails on non-a, rewrites a
	strat := MakeTopDownIdStopOnSuccess(rewrite)

	root := parent("R", parent("Inner", leaf("a"), leaf("a")), leaf("b"))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("TopDownIdStopOnSuccess: %v", err)
	}
	// Both "a" leaves should be rewritten because All walks every
	// child and each child's TopDownIdStopOnSuccess walk applies v
	// independently. The "Inner" parent is not rewritten (the v walk
	// stops at "Inner" because ChoiceId(v, All(x)) descends into All
	// only if v fails — which it does on "Inner".)
	want := "(R (Inner X X) b)"
	if printNode(got.(*node)) != want {
		t.Errorf("TopDownIdStopOnSuccess result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestMakeOnceTopDown(t *testing.T) {
	rewrite := renameStrategy("a", "X")
	strat := MakeOnceTopDown(rewrite)

	root := parent("R", leaf("a"), leaf("a"), leaf("b"))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("OnceTopDown: %v", err)
	}
	// Only the first "a" should be rewritten.
	want := "(R X a b)"
	if printNode(got.(*node)) != want {
		t.Errorf("OnceTopDown result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestMakeOnceTopDown_Fails(t *testing.T) {
	strat := MakeOnceTopDown(NewFail())
	_, err := strat.VisitLight(parent("R", leaf("a"), leaf("b")), VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Errorf("OnceTopDown(Fail) should fail, got %v", err)
	}
}

// TestMakeRepeat keeps applying the rewrite until it fails. We feed
// a strategy that increments a counter once and then fails; Repeat
// should apply it once, stop, and succeed.
func TestMakeRepeat(t *testing.T) {
	calls := 0
	once := strategyFn(func(s any, _ Introspector) (any, error) {
		if calls > 0 {
			return nil, ErrVisitFailure
		}
		calls++
		return s, nil
	})
	strat := MakeRepeat(once)
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Repeat: %v", err)
	}
	if calls != 1 {
		t.Errorf("Repeat should call once and then stop, got %d calls", calls)
	}
	if got.(*node).label != "a" {
		t.Errorf("Repeat shouldn't have mutated subject")
	}
}

// TestMakeRepeatId stops as soon as the subject doesn't change.
// Strategy applies a→b once; on the second visit it'd return b
// unchanged, which RepeatId treats as "done".
func TestMakeRepeatId(t *testing.T) {
	calls := 0
	r := strategyFn(func(s any, _ Introspector) (any, error) {
		calls++
		n := s.(*node)
		if n.label == "a" {
			return &node{label: "b", kids: n.kids}, nil
		}
		return s, nil // identity → RepeatId stops
	})
	strat := MakeRepeatId(r)
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("RepeatId: %v", err)
	}
	if got.(*node).label != "b" {
		t.Errorf("RepeatId result: want=b got=%q", got.(*node).label)
	}
	if calls < 2 {
		t.Errorf("RepeatId should have visited at least twice (mutate + stop), got %d", calls)
	}
}
