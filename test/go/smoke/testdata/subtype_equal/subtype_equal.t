package main

// Equality between sorts related by subtyping (%typeterm … extends …):
// Java's tom_equal_term_<sort> takes Objects, so the compiler compares a
// value of a supertype (here the slot n:tInt of div, bound to x) with a
// value of the subtype (zero(): tNat) without a cast. In Go the
// equality takes the sort's type, so the backend converts the arguments
// (an argument that is not a value of the sort is not equal, where
// Java's equals returns false). Same output as the Tom+Java twin
// (twin/) compiled by legacy/ (test/TestConstraintCombinationWithSubtype.t).

import "fmt"

type tInt interface{ isTInt() }
type tNat interface {
  tInt
  isTNat()
}

type zero struct{}
type neg struct{ m tNat }
type div struct{ n tInt }

func (*zero) isTInt() {}
func (*zero) isTNat() {}
func (*neg) isTInt()  {}

func is[T any](t any) bool { _, ok := t.(T); return ok }

%typeterm tFloat {
  implement { *div }
  is_sort(t) { $t != nil }
  equals(t1,t2) { $t1 == $t2 }
}

%typeterm tInt {
  implement { tInt }
  is_sort(t) { is[tInt]($t) }
  equals(t1,t2) { tIntEquals($t1, $t2) }
}

%typeterm tNat extends tInt {
  implement { tNat }
  is_sort(t) { is[tNat]($t) }
  equals(t1,t2) { is[*zero]($t1) && is[*zero]($t2) }
}

%op tNat zero() {
  is_fsym(t) { is[*zero]($t) }
  make() { &zero{} }
}

%op tInt neg(m:tNat) {
  is_fsym(t) { is[*neg]($t) }
  get_slot(m, t) { $t.(*neg).m }
  make(t0) { &neg{$t0} }
}

%op tFloat div(n:tInt) {
  is_fsym(t) { $t != nil }
  get_slot(n, t) { $t.n }
  make(t0) { &div{$t0} }
}

func tIntEquals(a, b tInt) bool {
  if x, ok := a.(*neg); ok {
    y, ok := b.(*neg)
    return ok && x.m.(*zero) != nil && y.m.(*zero) != nil
  }
  return is[*zero](a) && is[*zero](b)
}

func count(d *div) int {
  n := 0
  %match {
    (zero() == x) && div[n=x] << d -> { n++ }
    (zero() != x) && div[n=x] << d -> { n += 10 }
  }
  return n
}

func main() {
  fmt.Println("div(zero())", count(`div(zero())))
  fmt.Println("div(neg(zero()))", count(`div(neg(zero()))))
}
