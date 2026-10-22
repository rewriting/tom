package main

// Port of test/regress/WrongSymbol.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import regress.wrongsymbol.example.types.*;`.
// port: `"x = " + `x` (Java string concatenation) is "x = " + fmt.Sprint(`x).

import (
  "fmt"
)

  %gom {
    module Example
      abstract syntax
      A = a()
      | f(n:A)
  }

  func main() {
    var t A = `f(a())
    %match {
      f(x*) << t -> { 
        fmt.Println("x = " + fmt.Sprint(`x))
      }
    }
  }
