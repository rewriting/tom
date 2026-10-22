package main

// A %strategy parameter used by the generated methods: in a rule
// right-hand side, inside a backquote term and as a %match subject.

import (
  "fmt"

  "tom/tomgo/stable/library/sl"
)

%include { sl.tom }
%gom {
  module sig
  abstract syntax
  T1 = a() | b() | c() | d()
    | f(x:T1)
    | g(x:T1, y:T1)
}

%strategy Replace(by:T1) extends Identity() {
  visit T1 {
    a() -> by
    b() -> f(by)
    c() -> {
      %match(by) {
        f(x) -> { return `g(x, by) }
      }
    }
  }
}

func main() {
  var s sl.Strategy = `Replace(f(d()))
  for _, in := range []T1{`a(), `b(), `c(), `d()} {
    out, err := s.VisitLight(in, sl.VisitableIntrospector{})
    fmt.Println(in, "->", out, err)
  }
}
