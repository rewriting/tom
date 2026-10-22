package main

// The make_empty hook of an AC operator: Java Gom's AC hook (the swap
// that keeps the elements sorted) rebuilds the tail as
// Cons(t.getTail(), Empty.make()), so the make_empty hook runs once per
// swap. gomgo's shortcut that reuses the tail of t (javaapi.go,
// emitVariadicJava) must not skip it. Which of f(y, lx*) and f(x, ly*)
// swaps depends on the hash values (they differ from Java's): only the
// total is printed. Same output as the Tom+Java twin (twin/) compiled by
// legacy/.

import (
  "fmt"

  _ "tom/library/sl" // the generated term_gen.go needs the Go runtime module
)

var emptyCalls int

%gom {
  module Term
    abstract syntax
    T = a() | b() | f(T*)
    f:AC{}
    f:make_empty() {
      emptyCalls++
    }
}

func main() {
  var lx T = `f(a())
  var ly T = `f(b())
  emptyCalls = 0
  var t1 T = `f(b(), lx*)
  var t2 T = `f(a(), ly*)
  fmt.Println(emptyCalls, t1 == t2, t1.Length())
}
