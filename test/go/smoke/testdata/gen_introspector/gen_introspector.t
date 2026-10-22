package main

// --genIntrospector: tom -go generates LocalIntrospector, an
// sl.Introspector built from the mappings (get_slot/make of %op, the
// list hooks of %oplist, the array hooks of %oparray — whose get_size
// is an int64 in Go), so that
// strategies traverse terms that are not sl.Visitable.

import (
  "fmt"

  "tom/library/sl"
)

%include { sl.tom }

type Term interface{ isTerm() }
type F struct{ X Term }
type A struct{}

func (*F) isTerm() {}
func (*A) isTerm() {}
func (f *F) String() string { return fmt.Sprintf("f(%v)", f.X) }
func (*A) String() string   { return "a()" }

%typeterm Term {
  implement     { Term }
  is_sort(t)    { func() bool { _, ok := ($t).(Term); return ok }() }
  equals(t1,t2) { fmt.Sprint($t1) == fmt.Sprint($t2) }
}

%op Term f(x:Term) {
  is_fsym(t)    { func() bool { _, ok := ($t).(*F); return ok }() }
  get_slot(x,t) { ($t).(*F).X }
  make(x)       { &F{$x} }
}

%op Term a() {
  is_fsym(t) { func() bool { _, ok := ($t).(*A); return ok }() }
  make()     { &A{} }
}

type List []Term

%typeterm List {
  implement     { List }
  is_sort(t)    { func() bool { _, ok := ($t).(List); return ok }() }
  equals(l1,l2) { fmt.Sprint($l1) == fmt.Sprint($l2) }
}

%oplist List l(Term*) {
  is_fsym(t)       { $t != nil }
  make_empty()     { List{} }
  make_insert(e,l) { append(List{$e}, $l...) }
  get_head(l)      { $l[0] }
  get_tail(l)      { $l[1:] }
  is_empty(l)      { len($l) == 0 }
}

type Arr []Term

%typeterm Arr {
  implement     { Arr }
  is_sort(t)    { func() bool { _, ok := ($t).(Arr); return ok }() }
  equals(l1,l2) { fmt.Sprint($l1) == fmt.Sprint($l2) }
}

%oparray Arr t(Term*) {
  is_fsym(t)       { $t != nil }
  make_empty(n)    { make(Arr, 0, $n) }
  make_append(e,l) { append(append(Arr(nil), $l...), $e) }
  get_element(l,n) { $l[$n] }
  get_size(l)      { int64(len($l)) }
}

%strategy R() extends Identity() {
  visit Term {
    f(x) -> { return `x; }
  }
}

%strategy R2() extends Identity() {
  visit Term {
    a() -> { return `f(a()); }
  }
}

func run(name string, s sl.Strategy, t any) {
  r, err := s.Visit(t, &LocalIntrospector{})
  fmt.Println(name, r, err)
}

func main() {
  run("TopDown", `TopDown(R()), `f(f(f(a()))))
  run("BottomUp", `BottomUp(R2()), `f(a()))
  run("InnermostId", `InnermostId(R()), `f(f(f(a()))))
  run("All/list", `All(R2()), `l(a(),a(),f(a())))
  run("TopDown/list", `TopDown(R()), `l(f(f(a())),a()))
  run("All/array", `All(R2()), `t(a(),f(a()),a()))
  run("BottomUp/array", `BottomUp(R()), `t(f(f(a())),a()))
  intro := &LocalIntrospector{}
  fmt.Println(intro.GetChildCount(`f(a())), intro.GetChildCount(`l(a(),a(),a())), intro.GetChildCount(`t(a(),a())), intro.GetChildCount(nil))
  // as the Java expander's code (it rebuilds an array from its last child
  // down with make_append), SetChildren on a %oparray reverses the children
  // (checked with legacy/ -gi on an ArrayList-based %oparray)
  fmt.Println(intro.GetChildren(`t(a(),f(a()))), intro.SetChildren(`t(a(),a()), []any{`f(a()), `a()}), intro.GetChildAt(`t(a(),f(a())), 1))
}
