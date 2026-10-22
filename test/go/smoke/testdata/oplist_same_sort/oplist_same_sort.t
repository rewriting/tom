package main

import "fmt"

%include { int.tom }

type Lst struct { head *Lst; tail *Lst; val int64 }
func mk(e *Lst, l *Lst) *Lst { r := new(Lst); r.head = e; r.tail = l; return r }

%typeterm L {
  implement { *Lst }
  is_sort(t) { true }
  equals(a,b) { $a == $b }
}

%oplist L conc(L*) {
  is_fsym(t) { $t == nil || $t.head != nil }
  make_empty() { nil }
  make_insert(e,l) { mk($e,$l) }
  get_head(l) { $l.head }
  get_tail(l) { $l.tail }
  is_empty(l) { $l == nil }
}

func count(l *Lst) int64 {
  var c int64 = 0
  %match(l) {
    conc(_*, x, _*) -> { _ = `x; c = c + 1 }
  }
  return c
}

func main() {
  a := &Lst{val: 1}
  b := &Lst{val: 2}
  fmt.Println(count(&Lst{head: a, tail: &Lst{head: b}}))
}
