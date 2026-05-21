package sl

import (
	"errors"
	"fmt"
	"strings"
	"testing"
)

// node is a toy AST type for testing: a label plus an ordered list of
// children. It implements [Visitable] so [VisitableIntrospector] can
// walk it without bespoke introspector code.
type node struct {
	label string
	kids  []*node
}

func leaf(label string) *node { return &node{label: label} }
func parent(label string, kids ...*node) *node {
	return &node{label: label, kids: kids}
}

func (n *node) Children() []any {
	out := make([]any, len(n.kids))
	for i, k := range n.kids {
		out[i] = k
	}
	return out
}

func (n *node) SetChildren(children []any) any {
	dup := make([]*node, len(children))
	for i, c := range children {
		dup[i] = c.(*node)
	}
	return &node{label: n.label, kids: dup}
}

// printNode renders a node as a Lisp-ish string for diff-friendly
// failure messages.
func printNode(n *node) string {
	if len(n.kids) == 0 {
		return n.label
	}
	parts := []string{n.label}
	for _, k := range n.kids {
		parts = append(parts, printNode(k))
	}
	return "(" + strings.Join(parts, " ") + ")"
}

// renameStrategy returns a strategy that, given a *node whose label
// is `from`, returns a fresh copy with label `to`. Other nodes are
// rejected with [ErrVisitFailure]. Useful for testing Choice/All/One.
func renameStrategy(from, to string) Strategy {
	return strategyFn(func(subject any, _ Introspector) (any, error) {
		n, ok := subject.(*node)
		if !ok || n.label != from {
			return nil, ErrVisitFailure
		}
		return &node{label: to, kids: n.kids}, nil
	})
}

// strategyFn lets us build inline strategies from a closure — saves
// having to declare a struct + helpers for one-off tests. Backed by a
// struct (not a bare function type) because Mu.expand uses a map
// keyed on Strategy, and function types are not hashable in Go.
type strategyFnImpl struct {
	fn func(any, Introspector) (any, error)
}

func strategyFn(fn func(any, Introspector) (any, error)) Strategy {
	return &strategyFnImpl{fn: fn}
}

func (s *strategyFnImpl) VisitLight(subject any, i Introspector) (any, error) {
	return s.fn(subject, i)
}
func (*strategyFnImpl) ChildCount() int        { return 0 }
func (*strategyFnImpl) ChildAt(int) Strategy   { return nil }
func (*strategyFnImpl) SetChildAt(int, Strategy) {}

// ----------------------------------------------------------------
// Phase 6.0 — Identity / Fail
// ----------------------------------------------------------------

func TestIdentity(t *testing.T) {
	n := leaf("a")
	got, err := NewIdentity().VisitLight(n, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Identity returned error: %v", err)
	}
	if got != any(n) {
		t.Errorf("Identity should return same instance, got %#v", got)
	}
}

func TestFail(t *testing.T) {
	_, err := NewFail().VisitLight(leaf("a"), VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Errorf("Fail should return ErrVisitFailure, got %v", err)
	}
}

func TestFailWith(t *testing.T) {
	_, err := NewFailWith("nope").VisitLight(leaf("a"), VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Fatalf("FailWith should still match ErrVisitFailure, got %v", err)
	}
	if !strings.Contains(err.Error(), "nope") {
		t.Errorf("FailWith should embed message, got %q", err.Error())
	}
}

// ----------------------------------------------------------------
// Phase 6.1 — Sequence / Choice / All / One + Id variants
// ----------------------------------------------------------------

func TestSequence_BothSucceed(t *testing.T) {
	// a → b → c via two renames in sequence.
	strat := NewSequence(renameStrategy("a", "b"), renameStrategy("b", "c"))
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Sequence(a→b,b→c) on a should succeed, got %v", err)
	}
	if got.(*node).label != "c" {
		t.Errorf("expected label=c, got %q", got.(*node).label)
	}
}

func TestSequence_FirstFails(t *testing.T) {
	strat := NewSequence(NewFail(), renameStrategy("a", "c"))
	_, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Errorf("Sequence(Fail,_) should fail, got %v", err)
	}
}

func TestSequence_NullTail(t *testing.T) {
	// Java reference: NewSequence(s, nil) == s.
	id := NewIdentity()
	if got := NewSequence(id, nil); got != id {
		t.Errorf("NewSequence(s, nil) should return s, got different strategy")
	}
}

func TestChoice_FirstSucceeds(t *testing.T) {
	strat := NewChoice(renameStrategy("a", "b"), renameStrategy("a", "x"))
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Choice on a should succeed via first, got %v", err)
	}
	if got.(*node).label != "b" {
		t.Errorf("expected first-arm label=b, got %q", got.(*node).label)
	}
}

func TestChoice_FallsThrough(t *testing.T) {
	strat := NewChoice(NewFail(), renameStrategy("a", "x"))
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Choice(Fail, x) on a should succeed via Then, got %v", err)
	}
	if got.(*node).label != "x" {
		t.Errorf("expected Then-arm label=x, got %q", got.(*node).label)
	}
}

func TestChoice_PropagatesNonVisitFailure(t *testing.T) {
	custom := errors.New("boom")
	strat := NewChoice(
		strategyFn(func(any, Introspector) (any, error) { return nil, custom }),
		NewIdentity(),
	)
	_, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if !errors.Is(err, custom) {
		t.Errorf("Choice should propagate non-ErrVisitFailure, got %v", err)
	}
}

func TestAll_RenameAllChildren(t *testing.T) {
	root := parent("R", leaf("a"), leaf("a"), leaf("b"))
	strat := NewAll(NewChoice(renameStrategy("a", "X"), NewIdentity()))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("All should succeed, got %v", err)
	}
	want := "(R X X b)"
	if printNode(got.(*node)) != want {
		t.Errorf("All result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestAll_OnLeaf(t *testing.T) {
	// Per spec: All on a leaf is identity.
	n := leaf("a")
	got, err := NewAll(NewFail()).VisitLight(n, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("All on leaf should succeed, got %v", err)
	}
	if got != any(n) {
		t.Errorf("All on leaf should be identity")
	}
}

func TestOne_FirstChildSucceeds(t *testing.T) {
	root := parent("R", leaf("a"), leaf("b"))
	strat := NewOne(renameStrategy("a", "X"))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("One should succeed via first child, got %v", err)
	}
	want := "(R X b)"
	if printNode(got.(*node)) != want {
		t.Errorf("One result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestOne_AllChildrenFail(t *testing.T) {
	root := parent("R", leaf("a"), leaf("b"))
	strat := NewOne(renameStrategy("z", "X"))
	_, err := strat.VisitLight(root, VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Errorf("One should fail when no child matches, got %v", err)
	}
}

func TestOneId_SkipsUnchanged(t *testing.T) {
	// Strategy that "succeeds" on every node but only mutates "b".
	strat := NewOneId(strategyFn(func(s any, _ Introspector) (any, error) {
		n := s.(*node)
		if n.label == "b" {
			return &node{label: "B", kids: n.kids}, nil
		}
		return n, nil // succeeds, no change
	}))
	root := parent("R", leaf("a"), leaf("b"), leaf("c"))
	got, err := strat.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("OneId: %v", err)
	}
	want := "(R a B c)"
	if printNode(got.(*node)) != want {
		t.Errorf("OneId result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

func TestSequenceId_FirstUnchangedSkipsThen(t *testing.T) {
	called := false
	then := strategyFn(func(s any, _ Introspector) (any, error) {
		called = true
		return s, nil
	})
	strat := NewSequenceId(NewIdentity(), then)
	_, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("SequenceId: %v", err)
	}
	if called {
		t.Errorf("SequenceId should NOT invoke Then when First was identity")
	}
}

func TestSequenceId_FirstChangedFiresThen(t *testing.T) {
	strat := NewSequenceId(renameStrategy("a", "b"), renameStrategy("b", "c"))
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("SequenceId: %v", err)
	}
	if got.(*node).label != "c" {
		t.Errorf("expected label=c after SequenceId chain, got %q", got.(*node).label)
	}
}

func TestChoiceId_FirstIdentityFallsToThen(t *testing.T) {
	strat := NewChoiceId(NewIdentity(), renameStrategy("a", "X"))
	got, err := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if err != nil {
		t.Fatalf("ChoiceId: %v", err)
	}
	if got.(*node).label != "X" {
		t.Errorf("ChoiceId(Identity, a→X) should reach Then, got %q", got.(*node).label)
	}
}

func TestChoiceId_FirstChangedShortCircuits(t *testing.T) {
	strat := NewChoiceId(renameStrategy("a", "Y"), renameStrategy("a", "Z"))
	got, _ := strat.VisitLight(leaf("a"), VisitableIntrospector{})
	if got.(*node).label != "Y" {
		t.Errorf("ChoiceId should return First-arm result when it changed, got %q", got.(*node).label)
	}
}

// ----------------------------------------------------------------
// Phase 6.2 — Mu / MuVar (recursive strategies)
// ----------------------------------------------------------------

// TestMu_TopDownReplace builds a manual TopDownIdStopOnSuccess walk
// using Mu/MuVar/ChoiceId/All and renames the first "a" it finds.
//
//	rec = Mu(x, ChoiceId(rename a→X, All(x)))
//
// On (R (R a) b) the walk should rewrite the deeply nested "a" to
// "X" and stop (ChoiceId short-circuits once a child changed).
func TestMu_TopDownReplace(t *testing.T) {
	x := NewMuVar("x")
	body := NewChoiceId(renameStrategy("a", "X"), NewAll(x))
	rec := NewMu(x, body)

	root := parent("R", parent("R", leaf("a")), leaf("b"))
	got, err := rec.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Mu walk: %v", err)
	}
	want := "(R (R X) b)"
	if printNode(got.(*node)) != want {
		t.Errorf("Mu walk result mismatch: want=%s got=%s", want, printNode(got.(*node)))
	}
}

// TestMu_TwoLevelRecursion builds a fix-point that descends until it
// finds "a" anywhere in the tree. Counts the number of *a* leaves
// found by accumulating into a captured slice.
func TestMu_TwoLevelRecursion(t *testing.T) {
	var found []string
	collect := strategyFn(func(s any, _ Introspector) (any, error) {
		n := s.(*node)
		if n.label == "a" {
			found = append(found, n.label)
			return &node{label: "A"}, nil
		}
		return n, nil // identity — encourages ChoiceId to retry deeper
	})
	x := NewMuVar("x")
	rec := NewMu(x, NewChoiceId(collect, NewAll(x)))

	root := parent("R",
		parent("L", leaf("a"), leaf("b")),
		parent("L", leaf("a"), parent("Inner", leaf("a"))),
	)
	got, err := rec.VisitLight(root, VisitableIntrospector{})
	if err != nil {
		t.Fatalf("Mu walk: %v", err)
	}
	// Behaviour spec: ChoiceId(collect, All(x)) walks down via All until
	// it finds an "a" at every leaf; collect's identity-return on
	// non-"a" makes ChoiceId fall through to All which recurses.
	if len(found) != 3 {
		t.Errorf("expected 3 hits, got %d (visited: %v)", len(found), found)
	}
	if !strings.Contains(printNode(got.(*node)), "A") {
		t.Errorf("expected at least one A in result, got %s", printNode(got.(*node)))
	}
}

// TestMuVar_Unbound asserts an isolated MuVar (never expanded by an
// enclosing Mu) fails cleanly.
func TestMuVar_Unbound(t *testing.T) {
	_, err := NewMuVar("z").VisitLight(leaf("a"), VisitableIntrospector{})
	if !errors.Is(err, ErrVisitFailure) {
		t.Errorf("Unbound MuVar should fail, got %v", err)
	}
}

// Sanity: surface a few invariants of the strategy-tree shape, so a
// future refactor that changes the Mu expansion or arg layout flags
// loudly.
func TestStrategyTreeShape(t *testing.T) {
	cases := []struct {
		name string
		s    Strategy
		args int
	}{
		{"Identity", NewIdentity(), 0},
		{"Fail", NewFail(), 0},
		{"Sequence", NewSequence(NewIdentity(), NewIdentity()), 2},
		{"Choice", NewChoice(NewIdentity(), NewIdentity()), 2},
		{"All", NewAll(NewIdentity()), 1},
		{"One", NewOne(NewIdentity()), 1},
		{"SequenceId", NewSequenceId(NewIdentity(), NewIdentity()), 2},
		{"ChoiceId", NewChoiceId(NewIdentity(), NewIdentity()), 2},
		{"OneId", NewOneId(NewIdentity()), 1},
		{"Mu", NewMu(NewMuVar("x"), NewIdentity()), 2},
		{"MuVar", NewMuVar("x"), 0},
	}
	for _, c := range cases {
		if got := c.s.ChildCount(); got != c.args {
			t.Errorf("%s: ChildCount = %d, want %d", c.name, got, c.args)
		}
	}
}

// ensure we don't accidentally leak fmt — used only here for debug.
var _ = fmt.Sprintf
