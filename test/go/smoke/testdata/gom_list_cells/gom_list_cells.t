package main

// The lists of gomgo's user modules are Java Gom's maximally-shared cons
// cells (gom/backend/cells.go): a tail is shared (the tail of
// conc(a(), l*) is l itself, not a copy), and a list may end with a tail
// that is not a list — Java's Consconc(e, x) with x of the list sort
// built by another operator, explicitly or by a make_empty hook — which
// the former flat slices could not represent (Cons<Op> panicked). Same
// output as the Tom+Java twin (twin/) compiled by legacy/.

import (
  "fmt"

  "tom/library/aterm"
  _ "tom/library/sl" // the generated cells_gen.go needs the Go runtime module
)

%gom {
  module cells
  abstract syntax
  E = a() | b() | c()
  L = conc(E*) | nil()
  M = lm(E*) | m0()
  lm:make_empty() {
    return `m0();
  }
}

func main() {
  // sharing of tails
  var l L = `conc(b(), a())
  var m L = `conc(a(), l*)
  fmt.Println("shared", m.GetTailconc() == l, m == `conc(a(), b(), a()), m.GetTailconc().GetTailconc() == `conc(a()))

  // a tail that is not a list
  var x L = `Consconc(a(), nil())
  fmt.Println("x", x, x.Length(), x.GetTailconc() == `nil(), x.IsConsconc(), x.IsEmptyconc())
  var y L = `Consconc(b(), x)
  fmt.Println("y", y, y.Length(), y.Reverse(), y.(*ConcL).Append(`c()), len(y.(*ConcL).ToArray()), y.GetTailconc() == x)
  %match(y) {
    Consconc(h, Consconc(h2, t)) -> { fmt.Println("match", `h, `h2, `t) }
  }

  // the make_empty hook ends the lists with m0()
  var p M = `lm(a(), b())
  fmt.Println("lm", p, p.Length(), p.GetTaillm().GetTaillm() == `m0(), `lm(), p.Reverse(), p.(*LmM).Append(`c()))

  // Visitable children: the elements (Java's typed toArray), which
  // cannot hold a last tail of another sort (Java's ArrayStoreException)
  fmt.Println("children", childCount(l.(*ConcL)), childCount(x.(*ConcL)), childCount(p.(*LmM)))
}

// childCount is v.ChildCount(), or the GomError it panics with (Java's
// getChildCount throwing ArrayStoreException).
func childCount(v interface{ ChildCount() int }) (res any) {
  defer func() {
    if r := recover(); r != nil {
      if _, ok := r.(*aterm.GomError); !ok {
        panic(r)
      }
      res = "ArrayStoreException"
    }
  }()
  return v.ChildCount()
}
