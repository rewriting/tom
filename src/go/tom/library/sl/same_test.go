package sl

import "testing"

func TestSame(t *testing.T) {
	s := []int{1, 2}
	m := map[int]int{}
	n := leaf("a")
	for _, c := range []struct {
		name string
		a, b any
		want bool
	}{
		{"nil", nil, nil, true},
		{"nil/value", nil, 1, false},
		{"int", 1, 1, true},
		{"pointer", n, n, true},
		{"pointers", n, leaf("a"), false},
		{"types", int32(1), int64(1), false},
		{"slice/same", s, s, true},
		{"slice/copy", s, append([]int(nil), s...), false},
		{"slice/prefix", s, s[:1], false},
		{"map", m, m, true},
		{"maps", m, map[int]int{}, false},
	} {
		if got := same(c.a, c.b); got != c.want {
			t.Errorf("%s: same = %v, want %v", c.name, got, c.want)
		}
	}
}

// The *Id combinators compare subjects as Java does (by reference),
// without panicking on a slice subject (an uncomparable Go type).
func TestIdCombinatorsOnSliceSubject(t *testing.T) {
	subject := []int{1}
	id := strategyFn(func(s any, _ Introspector) (any, error) { return s, nil })
	changed := strategyFn(func(any, Introspector) (any, error) { return []int{2}, nil })
	for name, s := range map[string]Strategy{
		"SequenceId": NewSequenceId(id, NewFail()),
		"ChoiceId":   NewChoiceId(changed, NewFail()),
	} {
		for _, visit := range []func() (any, error){
			func() (any, error) { return s.VisitLight(subject, VisitableIntrospector{}) },
			func() (any, error) { return s.Visit(subject, VisitableIntrospector{}) },
		} {
			if _, err := visit(); err != nil {
				t.Errorf("%s: %v", name, err)
			}
		}
	}
}

// Strategies are terms for VisitableIntrospector (Java's Strategy
// extends Visitable): a strategy can rewrite another one.
func TestVisitableIntrospectorOnStrategies(t *testing.T) {
	intro := VisitableIntrospector{}
	s := NewAll(NewAll(NewIdentity()))
	if n := intro.GetChildCount(s); n != 1 {
		t.Fatalf("GetChildCount(All(All(Id))) = %d, want 1", n)
	}
	inner := intro.GetChildAt(s, 0)
	if !IsAll(inner) {
		t.Fatalf("child = %T, want *All", inner)
	}
	// All(x) -> x, top-down: All(All(Id)) -> All(Id), then its child Id stays
	unwrap := strategyFn(func(v any, _ Introspector) (any, error) {
		if a, ok := v.(*All); ok {
			return a.ChildAt(0), nil
		}
		return v, nil
	})
	r, err := MakeTopDown(unwrap).VisitLight(NewAll(NewAll(NewIdentity())), intro)
	if err != nil || !IsAll(r) || !IsIdentity(r.(Strategy).ChildAt(0)) {
		t.Fatalf("TopDown(unwrap) = %T, %v; want All(Identity())", r, err)
	}
	if got := intro.SetChildren(s, []any{NewFail()}); got != any(s) || !IsFail(s.ChildAt(0)) {
		t.Fatalf("SetChildren did not update the strategy in place")
	}
}
