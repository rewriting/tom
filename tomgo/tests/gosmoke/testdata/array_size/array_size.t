package main

import "fmt"

// get_size returns the Tom int type (int64): the Go body len($l) is an
// int and is converted; a statement-style trailing ';' is tolerated.
type E struct{ name string }

%typeterm E {
  implement { *E }
  is_sort(t) { $t != nil }
  equals(a,b) { $a.name == $b.name }
}

%typeterm A {
  implement { []*E }
  is_sort(t) { $t != nil }
  equals(a,b) { len($a) == len($b) }
}

%op E e(n:String) {
  is_fsym(t) { $t != nil }
  get_slot(n,t) { $t.name }
  make(n) { &E{name: $n} }
}

%oparray A arr(E*) {
  is_fsym(t) { $t != nil }
  make_empty(n) { make([]*E, 0, $n) }
  make_append(e,l) { append($l, $e) }
  get_element(l,n) { $l[$n] }
  get_size(l) { len($l); }
}

%include { string.tom }

func last(l []*E) string {
  %match(l) {
    arr(_*, e(x)) -> { return `x }
  }
  return "none"
}

func main() {
  fmt.Println(last(`arr(e("a"), e("b"), e("c"))), last(`arr()))
}
