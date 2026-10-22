package aterm

import (
	"testing"
)

// fibInterp ports TestFibInterpreted.java — a tiny term-rewriting
// interpreter that drives normalization via match/make on pre-parsed
// rule patterns. The point is to exercise:
//
//   - MatchTerm against a cached pattern (rule LHS)
//   - MakeTerm against a cached pattern (rule RHS)
//   - placeholders captured on the LHS getting plugged into the RHS
//   - congruence rules that recurse on substructure
type fibInterp struct {
	factory                *Factory
	zero, suc, plus, fibFn *AFun
	tzero                  *ATermAppl
	fail                   ATerm
	lhs, rhs               []ATerm
}

func newFibInterp(f *Factory) *fibInterp {
	h := &fibInterp{factory: f}
	h.zero = f.MakeAFun("zero", 0, false)
	h.suc = f.MakeAFun("suc", 1, false)
	h.plus = f.MakeAFun("plus", 2, false)
	h.fibFn = f.MakeAFun("fib", 1, false)
	h.tzero = f.MakeAppl(h.zero)
	h.fail = f.Parse("fail")
	h.initRules()
	return h
}

func (h *fibInterp) initRules() {
	// One row per rule. The ordering matches TestFibInterpreted.java.
	specs := [][2]string{
		// fib(zero) -> suc(zero)
		{"fib(zero)", "suc(zero)"},
		// fib(suc(zero)) -> suc(zero)
		{"fib(suc(zero))", "suc(zero)"},
		// fib(suc(suc(X))) -> plus(fib(X),fib(suc(X)))
		{"fib(suc(suc(<term>)))", "plus(fib(<term>),fib(suc(<term>)))"},
		// plus(zero,X) -> X
		{"plus(zero,<term>)", "<term>"},
		// plus(suc(X),Y) -> plus(X,suc(Y))
		{"plus(suc(<term>),<term>)", "plus(<term>,suc(<term>))"},
		// congruence (suc)
		{"suc(<term>)", "suc(<term>)"},
		// congruence (plus)
		{"plus(<term>,<term>)", "plus(<term>,<term>)"},
	}
	h.lhs = make([]ATerm, len(specs))
	h.rhs = make([]ATerm, len(specs))
	for i, p := range specs {
		h.lhs[i] = h.factory.Parse(p[0])
		h.rhs[i] = h.factory.Parse(p[1])
	}
}

func (h *fibInterp) oneStep(subject ATerm) ATerm {
	// Rule 0: fib(zero) -> suc(zero)
	if subject.MatchTerm(h.lhs[0]) != nil {
		return h.rhs[0]
	}
	// Rule 1: fib(suc(zero)) -> suc(zero)
	if subject.MatchTerm(h.lhs[1]) != nil {
		return h.rhs[1]
	}
	// Rule 2: fib(suc(suc(X))) -> plus(fib(X),fib(suc(X)))
	if list := subject.MatchTerm(h.lhs[2]); list != nil {
		X := list[0]
		// RHS has 2 <term> placeholders — push X again to fill the
		// second slot (matches Java's `list.add(X)`).
		return h.factory.MakeTerm(h.rhs[2], []any{X, X})
	}
	// Rule 3: plus(zero,X) -> X
	if list := subject.MatchTerm(h.lhs[3]); list != nil {
		return h.factory.MakeTerm(h.rhs[3], list)
	}
	// Rule 4: plus(suc(X),Y) -> plus(X,suc(Y))
	if list := subject.MatchTerm(h.lhs[4]); list != nil {
		// RHS placeholders consume X, Y in order — we already have
		// [X, Y] from the LHS match.
		return h.factory.MakeTerm(h.rhs[4], list)
	}
	// Rule 5: congruence on suc(<term>). Recurse on X; if X is in
	// normal form (oneStep == fail) we bail out so the outer
	// normalize loop terminates.
	if list := subject.MatchTerm(h.lhs[5]); list != nil {
		X := list[0]
		Xp := h.oneStep(X.(ATerm))
		if Xp == h.fail {
			return h.fail
		}
		return h.factory.MakeTerm(h.rhs[5], []any{Xp})
	}
	// Rule 6: congruence on plus(<term>,<term>). Try left first; if
	// the left is in normal form, try right; if both are, the term
	// is a normal form here too.
	if list := subject.MatchTerm(h.lhs[6]); list != nil {
		X := list[0].(ATerm)
		Y := list[1].(ATerm)
		Xp := h.oneStep(X)
		if Xp == h.fail {
			Yp := h.oneStep(Y)
			if Yp == h.fail {
				return h.fail
			}
			return h.factory.MakeTerm(h.rhs[6], []any{X, Yp})
		}
		return h.factory.MakeTerm(h.rhs[6], []any{Xp, Y})
	}
	return h.fail
}

// normalize iterates oneStep until it returns fail (no rule fires),
// then returns the last non-failing term — the normal form.
func (h *fibInterp) normalize(t ATerm) ATerm {
	s := t
	for {
		t = s
		s = h.oneStep(t)
		if s == h.fail {
			return t
		}
	}
}

func (h *fibInterp) peano(n int) *ATermAppl {
	N := h.tzero
	for i := 0; i < n; i++ {
		N = h.factory.MakeAppl(h.suc, N)
	}
	return N
}

// peanoToInt decodes a Peano numeral. Returns -1 if the term isn't
// of the expected shape.
func (h *fibInterp) peanoToInt(t ATerm) int {
	n := 0
	cur, ok := t.(*ATermAppl)
	if !ok {
		return -1
	}
	for cur.GetAFun() == h.suc {
		n++
		next, ok := cur.GetArgument(0).(*ATermAppl)
		if !ok {
			return -1
		}
		cur = next
	}
	if cur.GetAFun() != h.zero {
		return -1
	}
	return n
}

func TestFibInterpreted_Small(t *testing.T) {
	f := NewFactory()
	h := newFibInterp(f)
	// Reference fib(n) values, 0-indexed with fib(0)=1.
	want := []int{1, 1, 2, 3, 5, 8, 13, 21}
	for i, w := range want {
		t.Run("", func(t *testing.T) {
			tfib := f.MakeAppl(h.fibFn, h.peano(i))
			normal := h.normalize(tfib)
			if got := h.peanoToInt(normal); got != w {
				t.Errorf("fib(%d) = %d (term %s), want %d", i, got, normal.String(), w)
			}
		})
	}
}

// TestFibInterpreted_OneStep — pin individual rule firings to catch
// regressions in MatchTerm/MakeTerm wiring before they show up as
// "fib hangs in normalize".
func TestFibInterpreted_OneStep(t *testing.T) {
	f := NewFactory()
	h := newFibInterp(f)

	if got := h.oneStep(f.Parse("fib(zero)")); got != f.Parse("suc(zero)") {
		t.Errorf("rule 0 fib(zero): %v", got)
	}
	if got := h.oneStep(f.Parse("fib(suc(zero))")); got != f.Parse("suc(zero)") {
		t.Errorf("rule 1 fib(suc(zero)): %v", got)
	}
	if got := h.oneStep(f.Parse("plus(zero,suc(zero))")); got != f.Parse("suc(zero)") {
		t.Errorf("rule 3 plus(zero,suc(zero)): %v", got)
	}
	if got := h.oneStep(f.Parse("plus(suc(zero),suc(zero))")); got != f.Parse("plus(zero,suc(suc(zero)))") {
		t.Errorf("rule 4: %v", got)
	}
}

// TestFibInterpreted_Sharing — running the interpreter twice produces
// the same canonical pointer (every intermediate term is hash-consed).
func TestFibInterpreted_Sharing(t *testing.T) {
	f := NewFactory()
	h := newFibInterp(f)
	a := h.normalize(f.MakeAppl(h.fibFn, h.peano(7)))
	b := h.normalize(f.MakeAppl(h.fibFn, h.peano(7)))
	if a != b {
		t.Fatalf("interpreter produced distinct canonical terms across runs")
	}
}

// TestFibInterpreted_Deep — TestFibInterpreted.java drives n=5, we
// push to n=12 so the rewriter actually does meaningful work
// (fib(12) = 144 takes thousands of rule applications). Pin the
// arithmetic result rather than wall time.
func TestFibInterpreted_Deep(t *testing.T) {
	f := NewFactory()
	h := newFibInterp(f)
	normal := h.normalize(f.MakeAppl(h.fibFn, h.peano(12)))
	if got := h.peanoToInt(normal); got != 233 {
		t.Errorf("fib(12) = %d, want 233", got)
	}
}
