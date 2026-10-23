package main

// The hooks of a %gom block compiled with gomgo's --gohooks are plain Go
// code (without Tom code), written into the generated Go file instead of
// the Tom mapping (the mode of the compiler's own ADTs): a make hook
// runs at the head of Make<Op>, and the methods a sort block declares on
// every alternative of the sort are methods of the sort's interface
// (Java: methods of the sort class), so they apply to any term of the
// sort. Tom+Java twin: twin/Twin.t.

import (
  "fmt"

  _ "tom/library/sl" // the generated jb_gen.go needs the Go runtime module
)

%gom(--gohooks) {
  module jb
  abstract syntax
  T = a() | b() | f(lhs:T, rhs:T)
  L = conc(T*)
  f:make(l, r) {
    if l.Isb() && r.Isb() {
      return MakeA()
    }
  }
  sort T:block() {
    func (t *AT) Size() int { return 1 }
    func (t *BT) Size() int { return 1 }
    func (t *FT) Size() int { return 1 + t.Lhs.Size() + t.Rhs.Size() }
  }
  sort L:block() {
    func (t *ConcL) Weight() int {
      w := 0
      var c L = t
      for c.IsConsconc() {
        w += c.GetHeadconc().Size()
        c = c.GetTailconc()
      }
      return w
    }
  }
}

func main() {
  var t T = `f(a(), f(b(), a()))
  fmt.Println(t, t.Size(), `b().Size(), `f(b(), b()))
  var l L = `conc(t, a())
  fmt.Println(l, l.Weight(), `conc().Weight())
}
