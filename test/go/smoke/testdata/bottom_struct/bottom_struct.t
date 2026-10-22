package main

import "fmt"

%include { int.tom }
%include { string.tom }

// Pt is a struct value type: the bottom of sort P (fresh variables of an
// OR-pattern (wa|wb)(...)) must be its zero value, not nil.
type Pt struct{ x, y int64 }

func mkPt(x int64, y int64) Pt { return Pt{x: x, y: y} }

type Wr struct {
  tag string
  p   Pt
}

func mkWr(tag string, p Pt) *Wr { return &Wr{tag: tag, p: p} }

%typeterm P {
  implement { Pt }
  is_sort(t) { true }
  equals(a,b) { $a == $b }
}

%typeterm W {
  implement { *Wr }
  is_sort(t) { true }
  equals(a,b) { $a == $b }
}

%op P pt(x:int, y:int) {
  is_fsym(t) { true }
  get_slot(x,t) { $t.x }
  get_slot(y,t) { $t.y }
  make(a,b) {
    mkPt($a, $b)
  }
}

%op W wa(p:P) {
  is_fsym(t) { $t.tag == "a" }
  get_slot(p,t) { $t.p }
  make(p) { mkWr("a", $p) }
}

%op W wb(p:P) {
  is_fsym(t) { $t.tag == "b" }
  get_slot(p,t) { $t.p }
  make(p) { mkWr("b", $p) }
}

%op W wc(p:P) {
  is_fsym(t) { $t.tag == "c" }
  get_slot(p,t) { $t.p }
  make(p) { mkWr("c", $p) }
}

func pick(w *Wr) string {
  %match(w) {
    (wa|wb)(pt(x,_)) -> { return fmt.Sprint("x=", `x) }
  }
  return "none"
}

func main() {
  fmt.Println(pick(`wa(pt(1,2))), pick(`wb(pt(3,4))), pick(`wc(pt(5,6))))
}
