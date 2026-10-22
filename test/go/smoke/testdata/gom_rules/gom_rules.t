package main

// Gom rules (`module M:rules()`) in Go: gomgo parses them (Java Gom's
// Rule.g) and generates the hooks of Java Gom's RuleExpander (Tom code:
// a %match returning the right-hand side) — for a non-variadic
// operator a make hook, for a variadic one a make_empty hook (rules for
// `op()`) and a make_insert hook matching realMake(head,tail); block
// hooks are host code of the mapping. The expected output is the one of
// the same program in Tom+Java compiled by legacy/.

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom {
  module sig
  imports int
  abstract syntax
  T = a() | b() | c()
    | f(x:T)
    | g(x:T, y:T)
    | n(i:int)
  L = conc(T*)
  M = m0() | lm(T*)
  module sig:rules() {
    f(a()) -> b()
    f(r@g(x,_)) -> x if isA(r) == false
    g(x,y) -> c() if x == y && x != a()
    n(i) -> n(0) if i < 0 || i > 100
    // lists
    lm() -> m0()
    conc(x*, b(), y*) -> conc(x*, y*)
    conc(c(), c(), x*) -> conc(c(), x*)
  }
  f:block() {
    func isA(t any) bool { _, ok := t.(*AT); return ok }
  }
}

func main() {
  fmt.Println(`f(a()), `f(b()), `f(g(b(), a())), `f(g(a(), b())))
  fmt.Println(`g(b(), b()), `g(a(), a()), `g(a(), b()))
  fmt.Println(`n(-5), `n(50), `n(500))
  fmt.Println(`conc(), `conc(b(), c(), b()), `conc(c(), c(), a(), c(), c()), `lm())
}
