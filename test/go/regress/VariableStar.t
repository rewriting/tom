package main

// Port of test/regress/VariableStar.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import variablestar.bug.types.*;`.
// port: `"x: " + `x` (Java string concatenation) is "x: " + fmt.Sprint(`x).

import (
  "fmt"
)

  %gom {
    module Bug
    imports int String
    abstract syntax
    T = a()
      | f(x:T)

    L = conc(T*)
  }

  func main() {
    var t L = `conc(a())
    %match {
      _* << t -> { fmt.Println("x: " + fmt.Sprint(`x)) }
      conc(_*) << t -> { fmt.Println("x: " + fmt.Sprint(`x)) }
    }
  }

