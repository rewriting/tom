package main

// %gom(--withCongruenceStrategies): tom -go passes the %gom options to
// gomgo, whose mapping then defines the congruence strategies _f,
// When_f, Is_f (and _l on a variadic operator); also IfThenElse, Not
// and Outermost of the Go sl mappings.

import (
  "fmt"

  "tom/library/sl"
)

%include { sl.tom }
%gom(--withCongruenceStrategies) {
  module sig
  abstract syntax
  T = a() | b() | c()
    | f(x:T, y:T)
    | l(T*)
}

%strategy AB() extends Fail() {
  visit T {
    a() -> b()
  }
}

%strategy BC() extends Fail() {
  visit T {
    b() -> c()
  }
}

func show(name string, s sl.Strategy, t T) {
  r, err := s.VisitLight(t, sl.VisitableIntrospector{})
  e, errE := s.Visit(t, sl.VisitableIntrospector{})
  if err != nil {
    fmt.Println(name, "fails", errE != nil)
    return
  }
  fmt.Println(name, r, e)
}

func main() {
  show("_f", `_f(AB(), Try(AB())), `f(a(), b()))
  show("_f/fail", `_f(AB(), AB()), `f(a(), b()))
  show("_f/other", `_f(AB(), AB()), `a())
  show("Is_f", `Is_f(), `f(a(), a()))
  show("Is_f/other", `Is_f(), `a())
  show("When_f", `When_f(All(Try(AB()))), `f(a(), b()))
  show("_l", `_l(Try(AB())), `l(a(), b(), a()))
  show("ITE", `IfThenElse(AB(), BC(), Identity()), `a())
  show("ITE/else", `IfThenElse(AB(), Identity(), BC()), `b())
  show("Not", `Not(AB()), `b())
  show("Not/fail", `Not(AB()), `a())
  show("Outermost", `Outermost(Choice(AB(), BC())), `f(a(), l(b(), a())))
}
