package main

// Port of test/regress/SuccessiveErrors.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import regress.successiveerrors.simple.types.*;`.

import (
  "fmt"
)

  %gom {
    module Simple 
      abstract syntax
      A = a()
        | f(n:A)
      B = b()
        | g(m:B)
  }

  func main() {
    var tt A = `a()
    %match{
      // a type error for the matching and another one for the bad typed term
      // f(b()) 
      x@b() << A tt && (g(x) == tt) -> { 
        fmt.Println(`f(x)) 
      }
    }
  }
