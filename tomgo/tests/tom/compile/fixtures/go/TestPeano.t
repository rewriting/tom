package peano

import (
	"fmt"
	"testing"
)

// Term is the runtime representation of `term` values. Same shape as
// fixtures/go/Peano.t — kept separate so each fixture is fully
// self-contained.
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

%typeterm term {
	implement     { *Term }
	is_sort(t)    { $t != nil }
	equals(t1,t2) { ($t1).Equals($t2) }
}

%op term zero() {
	is_fsym(t) { ($t).Name == "zero" }
	make()     { &Term{Name: "zero"} }
}

%op term suc(pred:term) {
	is_fsym(t)       { ($t).Name == "suc" }
	get_slot(pred,t) { ($t).Args[0] }
	make(p)          { &Term{Name: "suc", Args: []*Term{$p}} }
}

// plus1 — naive recursion on the right operand.
func plus1(t1, t2 *Term) *Term {
	%match(term t1, term t2) {
		x, zero() -> { return x }
		x, suc(y) -> { return `suc(plus1(x, y)) }
	}
	return nil
}

// plus2 — same as plus1 but with the slot-named record pattern.
func plus2(t1, t2 *Term) *Term {
	%match(term t1, term t2) {
		x, zero()       -> { return x }
		x, suc[pred=y]  -> { return `suc(plus2(x, y)) }
	}
	return nil
}

// fib3 — naive Fibonacci on Peano numerals.
func fib3(t *Term) *Term {
	%match(term t) {
		zero()             -> { return `suc(zero()) }
		suc[pred=x@zero()] -> { return `suc(x) }
		suc(suc(x))        -> { return plus1(fib3(x), fib3(`suc(x))) }
	}
	return nil
}

// int2peano builds n as suc^n(zero).
func int2peano(n int) *Term {
	t := `zero()
	for i := 0; i < n; i++ {
		t = `suc(t)
	}
	return t
}

// peano2int counts suc layers via a `%match`.
func peano2int(t *Term) int {
	%match(term t) {
		zero() -> { return 0 }
		suc(x) -> { return 1 + peano2int(x) }
	}
	return 0
}

// fibint is the integer reference fib used by testFib*.
func fibint(n int) int {
	if n <= 1 {
		return 1
	}
	return fibint(n-1) + fibint(n-2)
}

func TestPlus1(t *testing.T) {
	for i := 0; i < 100; i++ {
		N := int2peano(i)
		if got := peano2int(plus1(N, N)); got != i+i {
			t.Errorf("plus1(%d, %d) = %d, want %d  (N=%s)", i, i, got, i+i, N)
		}
	}
}

func TestPlus2(t *testing.T) {
	for i := 0; i < 100; i++ {
		N := int2peano(i)
		if got := peano2int(plus2(N, N)); got != i+i {
			t.Errorf("plus2(%d, %d) = %d, want %d  (N=%s)", i, i, got, i+i, N)
		}
	}
}

func TestFib3(t *testing.T) {
	for i := 0; i < 15; i++ {
		N := int2peano(i)
		if got := peano2int(fib3(N)); got != fibint(i) {
			t.Errorf("fib3(%d) = %d, want %d  (N=%s)", i, got, fibint(i), N)
		}
	}
}

func TestInt2p(t *testing.T) {
	got := int2peano(5)
	want := `suc(suc(suc(suc(suc(zero())))))
	if !got.Equals(want) {
		t.Errorf("int2peano(5) = %s, want %s", got, want)
	}
}

func TestPeano2int(t *testing.T) {
	in := `suc(suc(suc(suc(suc(zero())))))
	if got := peano2int(in); got != 5 {
		t.Errorf("peano2int(%s) = %d, want 5", in, got)
	}
}

// _ keeps fmt used even when the test functions don't trigger
// printf branches.
var _ = fmt.Sprintf
