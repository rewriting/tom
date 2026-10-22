package main

// %gom(--nosharing) (Java Gom's maximalsharing=false): each make
// allocates a new term (no hash-consing), terms are compared with
// DeepEquals, which is also the equals of the mapping (non-linear
// patterns); --multithread is accepted (the Go factory is thread-safe).

import (
  "fmt"

  _ "tom/library/sl" // the generated sig_gen.go needs the Go runtime module
)

%gom(--nosharing, --multithread) {
  module sig
  abstract syntax
  T = a() | b()
    | f(x:T, y:T)
    | l(T*)
}

func main() {
  var t1 T = `f(a(), l(b(), a()))
  var t2 T = `f(a(), l(b(), a()))
  fmt.Println(t1 == t2, t1.DeepEquals(t2), t1.DeepEquals(`f(a(), l(b()))))
  %match(t1) {
    f(x, l(_, x)) -> { fmt.Println("non-linear match", `x) }
  }
}
