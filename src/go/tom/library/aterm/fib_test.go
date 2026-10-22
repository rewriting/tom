package aterm

import (
	"testing"
)

// fibHelper packages the suc/zero/plus/fib AFuns Java's TestFib
// keeps as fields. Mirrors TestFib.java line-for-line — same
// rewrite rules, same recursive structure, same fixed-point
// loops.
type fibHelper struct {
	factory                *Factory
	zero, suc, plus, fibFn *AFun
	tzero                  *ATermAppl
}

func newFib(f *Factory) *fibHelper {
	h := &fibHelper{factory: f}
	h.zero = f.MakeAFun("zero", 0, false)
	h.suc = f.MakeAFun("suc", 1, false)
	h.plus = f.MakeAFun("plus", 2, false)
	h.fibFn = f.MakeAFun("fib", 1, false)
	h.tzero = f.MakeAppl(h.zero)
	return h
}

// normalizePlus runs the Peano addition rewrite rules until a
// fixed point. The 5-step bulk-shift optimisation matches the
// Java reference exactly so that the canonical-term traffic
// (and hence cache contents) is identical.
func (h *fibHelper) normalizePlus(t *ATermAppl) *ATermAppl {
	res := t
	for {
		v0 := res.GetArgument(0).(*ATermAppl)

		if v0.GetAFun() == h.suc {
			v1, ok := v0.GetArgument(0).(*ATermAppl)
			if ok && v1.GetAFun() == h.suc {
				v2, ok := v1.GetArgument(0).(*ATermAppl)
				if ok && v2.GetAFun() == h.suc {
					v3, ok := v2.GetArgument(0).(*ATermAppl)
					if ok && v3.GetAFun() == h.suc {
						v4, ok := v3.GetArgument(0).(*ATermAppl)
						if ok && v4.GetAFun() == h.suc {
							inner := h.factory.MakeAppl(h.suc,
								h.factory.MakeAppl(h.suc,
									h.factory.MakeAppl(h.suc,
										h.factory.MakeAppl(h.suc,
											h.factory.MakeAppl(h.suc, res.GetArgument(1))))))
							res = h.factory.MakeAppl(h.plus, v4.GetArgument(0), inner)
							continue
						}
					}
				}
			}
		}

		// plus(0, x) = x
		if v0.GetAFun() == h.zero {
			res = res.GetArgument(1).(*ATermAppl)
			break
		}

		// plus(suc(x), y) = plus(x, suc(y))
		if v0.GetAFun() == h.suc {
			res = h.factory.MakeAppl(h.plus,
				v0.GetArgument(0),
				h.factory.MakeAppl(h.suc, res.GetArgument(1)))
			continue
		}
		break
	}
	return res
}

// normalizeFib evaluates fib(N) by recursing on a single argument
// and delegating addition to normalizePlus.
func (h *fibHelper) normalizeFib(t *ATermAppl) *ATermAppl {
	res := t
	for {
		v0 := res.GetArgument(0).(*ATermAppl)

		// fib(0) = suc(0)
		if v0.GetAFun() == h.zero {
			res = h.factory.MakeAppl(h.suc, v0)
			break
		}
		// fib(suc(0)) = suc(0)
		if v0.GetAFun() == h.suc {
			v1 := v0.GetArgument(0).(*ATermAppl)
			if v1.GetAFun() == h.zero {
				res = v0
				break
			}
		}
		// fib(suc(suc(x))) = plus(fib(x), fib(suc(x)))
		if v0.GetAFun() == h.suc {
			v1 := v0.GetArgument(0).(*ATermAppl)
			if v1.GetAFun() == h.suc {
				v2 := v1.GetArgument(0).(*ATermAppl)
				fib1 := h.normalizeFib(h.factory.MakeAppl(h.fibFn, v2))
				fib2 := h.normalizeFib(h.factory.MakeAppl(h.fibFn, v1))
				res = h.normalizePlus(h.factory.MakeAppl(h.plus, fib1, fib2))
				break
			}
		}
		break
	}
	return res
}

// peanoN builds suc(suc(...zero)) — n times. Helpful when checking
// that a Peano answer matches an int.
func (h *fibHelper) peanoN(n int) *ATermAppl {
	N := h.tzero
	for i := 0; i < n; i++ {
		N = h.factory.MakeAppl(h.suc, N)
	}
	return N
}

// peanoToInt reads back a Peano numeral, returning -1 if the term
// isn't of the expected shape.
func (h *fibHelper) peanoToInt(t *ATermAppl) int {
	n := 0
	cur := t
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

func TestFib_NormalizePlusBasics(t *testing.T) {
	f := NewFactory()
	h := newFib(f)
	// plus(2, 2) → 4
	four := h.normalizePlus(
		f.MakeAppl(h.plus, h.peanoN(2), h.peanoN(2)))
	if got := h.peanoToInt(four); got != 4 {
		t.Errorf("plus(2,2) = %d, want 4", got)
	}
}

func TestFib_NormalizeFibSmall(t *testing.T) {
	f := NewFactory()
	h := newFib(f)
	// Reference fib(n) values, 0-indexed at zero=fib(0)=1.
	want := []int{1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144}
	for i, w := range want {
		t.Run("", func(t *testing.T) {
			result := h.normalizeFib(f.MakeAppl(h.fibFn, h.peanoN(i)))
			if got := h.peanoToInt(result); got != w {
				t.Errorf("fib(%d) = %d, want %d", i, got, w)
			}
		})
	}
}

// Sharing check: every term in the fib-of-12 normal form is in the
// hash-cons table — running fib twice in a row produces the same
// canonical pointer.
func TestFib_NormalizeFibSharing(t *testing.T) {
	f := NewFactory()
	h := newFib(f)
	a := h.normalizeFib(f.MakeAppl(h.fibFn, h.peanoN(12)))
	b := h.normalizeFib(f.MakeAppl(h.fibFn, h.peanoN(12)))
	if a != b {
		t.Fatalf("fib(12) computed twice produced distinct canonical terms")
	}
}
