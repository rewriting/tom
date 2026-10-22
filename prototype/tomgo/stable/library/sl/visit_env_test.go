// Frozen prototype copy — the maintained version is src/go/tom/library/sl (module tom).

package sl

import (
	"errors"
	"testing"
)

// visitBoth runs s with VisitLight and with the environment-based
// Visit on fresh copies of the same strategy, and checks that both
// agree (same success, same printed result).
func visitBoth(t *testing.T, name string, mk func() Strategy, subject *node) (string, error) {
	t.Helper()
	intro := VisitableIntrospector{}
	light, errL := mk().VisitLight(subject, intro)
	env, errE := mk().Visit(subject, intro)
	if (errL == nil) != (errE == nil) {
		t.Fatalf("%s: VisitLight err=%v, Visit err=%v", name, errL, errE)
	}
	if errL != nil {
		if !errors.Is(errE, ErrVisitFailure) {
			t.Fatalf("%s: Visit must fail with ErrVisitFailure, got %v", name, errE)
		}
		return "", errE
	}
	pl, pe := printNode(light.(*node)), printNode(env.(*node))
	if pl != pe {
		t.Fatalf("%s: VisitLight = %s, Visit = %s", name, pl, pe)
	}
	return pe, nil
}

func tree3() *node { return parent("f", leaf("a"), parent("g", leaf("a"), leaf("b"))) }

func TestVisitEnv_AgreesWithVisitLight(t *testing.T) {
	ab := func() Strategy { return renameStrategy("a", "b") }
	id := func() Strategy { return strategyFn(func(s any, _ Introspector) (any, error) { return s, nil }) }
	cases := []struct {
		name    string
		mk      func() Strategy
		subject *node
		want    string // "" = failure
	}{
		{"Identity", NewIdentity, tree3(), "(f a (g a b))"},
		{"Fail", NewFail, tree3(), ""},
		{"FailWith", func() Strategy { return NewFailWith("boom") }, tree3(), ""},
		{"Sequence", func() Strategy { return NewSequence(ab(), renameStrategy("b", "c")) }, leaf("a"), "c"},
		{"Sequence/first fails", func() Strategy { return NewSequence(ab(), NewIdentity()) }, leaf("x"), ""},
		{"SequenceId/unchanged", func() Strategy { return NewSequenceId(id(), NewFail()) }, leaf("x"), "x"},
		{"SequenceId/changed", func() Strategy { return NewSequenceId(ab(), renameStrategy("b", "c")) }, leaf("a"), "c"},
		{"Choice/first", func() Strategy { return NewChoice(ab(), NewFail()) }, leaf("a"), "b"},
		{"Choice/then", func() Strategy { return NewChoice(ab(), renameStrategy("x", "y")) }, leaf("x"), "y"},
		{"ChoiceId/changed", func() Strategy { return NewChoiceId(ab(), NewFail()) }, leaf("a"), "b"},
		{"ChoiceId/unchanged", func() Strategy { return NewChoiceId(id(), renameStrategy("x", "y")) }, leaf("x"), "y"},
		{"ChoiceId/first fails", func() Strategy { return NewChoiceId(NewFail(), NewIdentity()) }, leaf("a"), ""},
		{"All", func() Strategy { return NewAll(MakeTry(ab())) }, tree3(), "(f b (g a b))"},
		{"All/leaf", func() Strategy { return NewAll(NewFail()) }, leaf("a"), "a"},
		{"All/fails", func() Strategy { return NewAll(ab()) }, tree3(), ""},
		{"One", func() Strategy { return NewOne(ab()) }, parent("f", leaf("x"), leaf("a"), leaf("a")), "(f x b a)"},
		{"One/fails", func() Strategy { return NewOne(ab()) }, parent("f", leaf("x")), ""},
		{"OneId", func() Strategy { return NewOneId(MakeTry(ab())) }, parent("f", leaf("x"), leaf("a")), "(f x b)"},
		{"OneId/none", func() Strategy { return NewOneId(MakeTry(ab())) }, parent("f", leaf("x")), "(f x)"},
		{"Omega", func() Strategy { return NewOmega(2, NewOmega(1, ab())) }, tree3(), "(f a (g b b))"},
		{"Omega/0", func() Strategy { return NewOmega(0, ab()) }, leaf("a"), "b"},
		{"Omega/out", func() Strategy { return NewOmega(3, ab()) }, tree3(), ""},
		{"Mu/TopDown", func() Strategy { return MakeTopDown(MakeTry(ab())) }, tree3(), "(f b (g b b))"},
		{"Mu/BottomUp", func() Strategy { return MakeBottomUp(MakeTry(ab())) }, tree3(), "(f b (g b b))"},
		{"Mu/TopDownIdStopOnSuccess", func() Strategy { return MakeTopDownIdStopOnSuccess(MakeTry(ab())) }, tree3(), "(f b (g b b))"},
		{"Mu/OnceTopDown", func() Strategy { return MakeOnceTopDown(ab()) }, tree3(), "(f b (g a b))"},
		{"Mu/OnceTopDownId", func() Strategy { return MakeOnceTopDownId(MakeTry(ab())) }, tree3(), "(f b (g a b))"},
		{"Mu/Repeat", func() Strategy { return MakeRepeat(MakeOnceTopDown(ab())) }, tree3(), "(f b (g b b))"},
		{"Mu/RepeatId", func() Strategy { return MakeRepeatId(MakeOnceTopDownId(MakeTry(ab()))) }, tree3(), "(f b (g b b))"},
		{"MuVar/unbound", func() Strategy { return NewMuVar("x") }, leaf("a"), ""},
		{"Basic", func() Strategy { return newRenameStrategyBasic("a", "b") }, leaf("a"), "b"},
		{"Basic/fallback", func() Strategy { return newRenameStrategyBasic("a", "b") }, leaf("x"), "x"},
		{"Basic/TopDown", func() Strategy { return MakeTopDown(newRenameStrategyBasic("a", "b")) }, tree3(), "(f b (g b b))"},
	}
	for _, c := range cases {
		got, err := visitBoth(t, c.name, c.mk, c.subject)
		if c.want == "" {
			if err == nil {
				t.Errorf("%s: expected failure, got %s", c.name, got)
			}
			continue
		}
		if err != nil || got != c.want {
			t.Errorf("%s: got %q, %v; want %q", c.name, got, err, c.want)
		}
	}
}

// Java's Choice restores the subject before trying THEN; the light
// flavour does it by construction (it passes the original subject).
func TestVisitEnv_ChoiceRestoresSubject(t *testing.T) {
	// FIRST rewrites a -> b and then fails: THEN must see a again.
	first := NewSequence(renameStrategy("a", "b"), NewFail())
	var seen string
	then := strategyFn(func(s any, _ Introspector) (any, error) {
		seen = printNode(s.(*node))
		return s, nil
	})
	if _, err := NewChoice(first, then).Visit(leaf("a"), VisitableIntrospector{}); err != nil {
		t.Fatal(err)
	}
	if seen != "a" {
		t.Fatalf("THEN saw %q, want the original subject a", seen)
	}
}

// positionRecorder is the shape Tom generates for
//
//	%strategy GetPos(out) extends Identity() { visit T { a() -> { out = getEnvironment().getPosition() } } }
type positionRecorder struct {
	*AbstractStrategyBasic
	positions []string
}

func newPositionRecorder() *positionRecorder {
	r := &positionRecorder{}
	r.AbstractStrategyBasic = NewAbstractStrategyBasic(NewIdentity())
	return r
}

func (r *positionRecorder) VisitLight(subject any, intro Introspector) (any, error) {
	if n, ok := subject.(*node); ok && n.label == "a" {
		r.positions = append(r.positions, r.GetEnvironment().GetPosition().String())
		return subject, nil
	}
	if r.HasEnvironment() {
		return r.Any().VisitWith(r.GetEnvironment(), intro)
	}
	return r.Any().VisitLight(subject, intro)
}
func (r *positionRecorder) VisitEnv(intro Introspector) int { return VisitBasic(r, intro) }
func (r *positionRecorder) Visit(subject any, intro Introspector) (any, error) {
	return VisitStrategy(r, subject, intro)
}
func (r *positionRecorder) VisitWith(env *Environment, intro Introspector) (any, error) {
	return VisitStrategyWith(r, env, intro)
}

func TestVisitEnv_StrategyReadsItsPosition(t *testing.T) {
	r := newPositionRecorder()
	tree := tree3()
	res, err := MakeBottomUp(r).Visit(tree, VisitableIntrospector{})
	if err != nil || res != tree {
		t.Fatalf("BottomUp(recorder).Visit = %v, %v", res, err)
	}
	if len(r.positions) != 2 || r.positions[0] != "[1]" || r.positions[1] != "[2, 1]" {
		t.Fatalf("positions = %v, want [[1] [2, 1]]", r.positions)
	}
	// The same position is read by two runs on equal subjects.
	r2 := newPositionRecorder()
	if _, err := MakeBottomUp(r2).Visit(tree3(), VisitableIntrospector{}); err != nil {
		t.Fatal(err)
	}
	if r2.positions[1] != r.positions[1] {
		t.Fatal("positions differ between runs")
	}
}

func TestVisitEnv_GetEnvironmentPanicsWhenUninitialised(t *testing.T) {
	s := NewIdentity()
	if s.(*Identity).HasEnvironment() {
		t.Fatal("fresh strategy has no environment")
	}
	defer func() {
		if r := recover(); r == nil {
			t.Fatal("GetEnvironment on a fresh strategy must panic (Java: RuntimeException)")
		}
	}()
	s.GetEnvironment()
}

func TestVisitEnv_InitSharesOneEnvironment(t *testing.T) {
	inner := NewIdentity()
	s := MakeTopDown(NewSequence(inner, NewIdentity()))
	if _, err := s.Visit(leaf("a"), VisitableIntrospector{}); err != nil {
		t.Fatal(err)
	}
	if inner.GetEnvironment() != s.GetEnvironment() {
		t.Fatal("Init must give every sub-strategy the same environment")
	}
	// A second Visit installs a fresh environment, even through the
	// cycles that Mu expansion created.
	old := s.GetEnvironment()
	if _, err := s.Visit(leaf("a"), VisitableIntrospector{}); err != nil {
		t.Fatal(err)
	}
	if s.GetEnvironment() == old || inner.GetEnvironment() != s.GetEnvironment() {
		t.Fatal("second Visit must re-initialise every sub-strategy")
	}
}

func TestVisitWith_UsesTheGivenEnvironment(t *testing.T) {
	tree := tree3()
	env := NewEnvironment()
	env.SetRoot(tree)
	env.Down(2)
	res, err := NewAll(MakeTry(renameStrategy("a", "z"))).VisitWith(env, VisitableIntrospector{})
	if err != nil || printNode(res.(*node)) != "(g z b)" {
		t.Fatalf("VisitWith = %v, %v", res, err)
	}
	if env.Depth() != 1 {
		t.Fatal("VisitWith must leave the environment at its depth")
	}
	if _, err := NewFail().VisitWith(env, VisitableIntrospector{}); !errors.Is(err, ErrVisitFailure) {
		t.Fatalf("VisitWith(Fail) = %v", err)
	}
}

// A non-VisitFailure error of a light strategy run under the
// environment is fatal: Java lets the RuntimeException propagate, Go
// panics with the error.
func TestVisitBasic_FatalErrorPanics(t *testing.T) {
	boom := errors.New("boom")
	s := strategyFn(func(any, Introspector) (any, error) { return nil, boom })
	defer func() {
		if r := recover(); r != boom {
			t.Fatalf("recovered %v, want boom", r)
		}
	}()
	s.Visit(leaf("a"), VisitableIntrospector{})
}
