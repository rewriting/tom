package main

// AC matching in Go: gomgo's AC theory (f:AC{} — a hook without
// arglist — flattened and sorted by CompareTo) and the helpers the
// compiler adds for an f?? pattern (__computeLength_f,
// __getMultiplicities_f, __getTermForMult_f on the intarray mapping, and
// the Go version of next_minimal_extract). The order of the elements of
// an AC term depends on the hash values (it differs from Java's): only
// order-independent facts are printed. Same output as the Tom+Java twin
// compiled by legacy/.

import (
  "fmt"
  "sort"

  _ "tom/library/sl" // the generated term_gen.go needs the Go runtime module
)

%include{ int.tom }
%include{ intarray.tom }
%gom {
  module Term
    imports
    abstract syntax
    T = a() | b() | c() | f(T*)
    f:AC{}
}

func main() {
  fmt.Println(`f(a(),b(),c()) == `f(c(),a(),b()), `f(a(),f(b(),c())) == `f(b(),c(),a()))

  fmt.Println(`f(c(),b(),a(),c(),b()) == `f(a(),b(),b(),c(),c()), `f(f(c(),a()),f(b(),c(),f(a()))) == `f(a(),a(),b(),c(),c()), `f(c(),b(),a(),c(),b()).Length())
  var t1 T = `f(a(),b(),c())
  var sols []string
  %match(t1) {
    f??(X*,a(),Y*) -> {
      sols = append(sols, fmt.Sprint(`X == `f(), `X == `b(), `X == `c(), `X == `f(b(),c()), `Y == `f(b(),c()), `Y == `f()))
    }
  }
  sort.Strings(sols)
  fmt.Println(len(sols), sols)

  var t2 T = `f(a(),b(),a())
  n, k := 0, 0
  %match(t2) {
    f??(X*,X*,Y*) -> {
      n++
      if `X == `f() && `Y == t2 || `X == `a() && `Y == `f(b()) {
        k++
      }
    }
  }
  fmt.Println(n, k)
}
