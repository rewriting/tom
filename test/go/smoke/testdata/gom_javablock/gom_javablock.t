package main

// A javablock hook of a %gom block (Java Gom's internal hook kind for a
// block without Tom code, put into the class of its sort, which Tom
// does not compile; gomgo accepts it in the source as plain Go code):
// gomgo writes its Go declarations into the generated Go file, and the
// methods it declares on every alternative of the sort are methods of
// the sort's interface (Java: methods of the sort class), so they apply
// to any term of the sort. Tom+Java twin: twin/Twin.t.

import (
  "fmt"

  _ "tom/library/sl" // the generated jb_gen.go needs the Go runtime module
)

%gom {
  module jb
  abstract syntax
  T = a() | b() | f(lhs:T, rhs:T)
  L = conc(T*)
  sort T:javablock() {
    func (t *AT) Size() int { return 1 }
    func (t *BT) Size() int { return 1 }
    func (t *FT) Size() int { return 1 + t.Lhs.Size() + t.Rhs.Size() }
  }
  sort L:javablock() {
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
  fmt.Println(t, t.Size(), `b().Size())
  var l L = `conc(t, a())
  fmt.Println(l, l.Weight(), `conc().Weight())
}
