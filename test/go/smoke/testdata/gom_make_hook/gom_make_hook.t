package main

// A user make hook of a %gom block (Java Gom inserts the hook body,
// Tom code included, at the head of the operator's make): gomgo puts
// the body into the mapping, which sets the hook the Go smart
// constructor consults; a body that does not return falls through.

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom {
  module sig
  abstract syntax
  T = a() | b()
    | f(lhs:T, rhs:T)
    | g(x:T)
  f:make(x,y) {
    %match(x,y) {
      b(), b() -> { return `a(); }
      a(), z -> { return `g(z); }
    }
  }
}

func main() {
  fmt.Println(`f(b(), b()))
  fmt.Println(`f(a(), b()))
  fmt.Println(`f(b(), a()))
  fmt.Println(`g(f(b(), b())))
}
