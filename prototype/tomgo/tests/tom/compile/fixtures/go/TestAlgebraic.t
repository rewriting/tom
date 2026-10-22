package testalgebraic

import (
	"testing"

	"tom/tomgo/stable/library/sl"
)

%include { sl.tom }

%gom {
	module sig
	abstract syntax
	T1 = a() | b() | c()
	   | f(x:T1)
	T2 = d()
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
	got, _ := res.(T1)
	if !got.Equivalent(want) {
		t.Errorf("RepeatId(BottomUp(R())) on f(a()) = %v, want %v", got, want)
	}
}
