package testalgebraic

import (
	"testing"

	"tom/tomgo/stable/library/sl"
)

%include { sl.tom }

// Term — runtime representation for the algebraic T1 sort. Single
// recursive struct: nullary constants (a, b, c) have empty Args;
// `f(x:T1)` has one arg. Hand-written to avoid an inline %gom
// (which the Go pipeline doesn't expand yet).
type Term struct {
	Name string
	Args []*Term
}

func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name || len(t.Args) != len(o.Args) {
		return false
	}
	for i := range t.Args {
		if !t.Args[i].Equals(o.Args[i]) {
			return false
		}
	}
	return true
}

func (t *Term) String() string {
	if t == nil {
		return "<nil>"
	}
	if len(t.Args) == 0 {
		return t.Name
	}
	s := t.Name + "("
	for i, a := range t.Args {
		if i > 0 {
			s += ","
		}
		s += a.String()
	}
	return s + ")"
}

// Children / SetChildren make *Term satisfy sl.Visitable so the
// strategy library's VisitableIntrospector can walk it.
func (t *Term) Children() []any {
	out := make([]any, len(t.Args))
	for i, a := range t.Args {
		out[i] = a
	}
	return out
}

func (t *Term) SetChildren(children []any) any {
	args := make([]*Term, len(children))
	for i, c := range children {
		args[i], _ = c.(*Term)
	}
	return &Term{Name: t.Name, Args: args}
}

%typeterm T1 {
	implement     { *Term }
	is_sort(t)    { $t != nil }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%op T1 a() {
	is_fsym(t) { ($t).Name == "a" }
	make()     { &Term{Name: "a"} }
}

%op T1 b() {
	is_fsym(t) { ($t).Name == "b" }
	make()     { &Term{Name: "b"} }
}

%op T1 c() {
	is_fsym(t) { ($t).Name == "c" }
	make()     { &Term{Name: "c"} }
}

%op T1 f(x:T1) {
	is_fsym(t)    { ($t).Name == "f" }
	get_slot(x,t) { ($t).Args[0] }
	make(x)       { &Term{Name: "f", Args: []*Term{$x}} }
}

%strategy R() extends Identity() {
	visit T1 {
		a() -> b()
		b() -> c()
	}
}

func TestAlgebraic(t *testing.T) {
	input := `f(a())
	s := `RepeatId(BottomUp(R()))
	res, err := s.VisitLight(input, sl.VisitableIntrospector{})
	if err != nil {
		t.Fatalf("strategy failed: %v", err)
	}
	want := `f(c())
	got, _ := res.(*Term)
	if !got.Equals(want) {
		t.Errorf("RepeatId(BottomUp(R())) on f(a()) = %s, want %s", got, want)
	}
}
