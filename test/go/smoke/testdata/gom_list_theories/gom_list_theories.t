package main

// Java Gom's list semantics in Go (gomgo, javaapi.go): the theories of
// a variadic operator whose domain is its codomain (FL by default, AU
// with or without neutral, ACU, Free), make_insert/make_empty hooks, the
// Cons<op>/Empty<op> operators and the list API (Length, Reverse,
// Append, GetHead/GetTail, IsEmpty/IsCons, Is<op>, Get<slot>,
// SymbolName). The expected output is the one of the same program in
// Tom+Java compiled by legacy/ (ACU orders depend on hash values, so
// only their equalities are printed).

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom {
  module sig
  imports int
  abstract syntax
  T = a() | b() | c()
    | fl(T*)
    | free(T*)
    | au(T*)
    | acu(T*)
    | ins(T*)
  L = n() | m()
    | nau(L*)
  E = w(i:int)
    | ints(int*)
  au:AU() {}
  acu:ACU() {}
  free:Free() {}
  nau:AU() { `n() }
  ins:FL() {}
  ins:make_insert(e,l) {
    %match(e) {
      c() -> { return l; }
    }
  }
  ints:make_empty() {
    return `w(0);
  }
}

func main() {
  var x T = `fl(a(), fl(b(), fl()), fl(), c())
  fmt.Println("FL", x, x.Length(), x.GetHeadfl(), x.GetTailfl(), x.IsConsfl(), x.IsEmptyfl())
  fmt.Println("FL reverse", x.Reverse(), "append", x.(*FlT).Append(`a()))
  fmt.Println("Free", `free(a(), free(b()), free()))
  fmt.Println("AU", `au(a(), au(b(), c()), au()), `au(a()), `au(), `au(au(a()), au()))
  var u T = `au(a(), b(), c())
  fmt.Println("AU tail", u.GetTailau(), u.GetTailau().GetTailau(), u.Length())
  fmt.Println("AU neutral", `nau(n(), m(), n()), `nau(), `nau(n(), n()), `nau(m(), nau(m(), n()))) 
  fmt.Println("ACU", `acu(a(), b(), c()) == `acu(c(), acu(a(), b())), `acu(b(), a()) == `acu(a(), b()), `acu(a(), c()) == `acu(a(), b()))
  fmt.Println("hooks", `ins(a(), c(), b(), c()), `ints())
  fmt.Println("Cons/Empty", `Consfl(a(), fl(b())), `Emptyfl(), `Consau(a(), b()))
  var e E = `w(3)
  fmt.Println("API", e.Isw(), e.Geti(), e.SymbolName(), x.SymbolName(), `fl().SymbolName())
  %match(x) {
    fl(_*, y, _*) -> { fmt.Print(" ", `y) }
  }
  fmt.Println()
  %match(u) {
    Consau(h, t) -> { fmt.Println("Consau", `h, `t) }
  }
}
