package main

// Port of test/regress/UndeclaredExplicityType.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import regress.undeclaredexplicitytype.simple.types.*;`.

import (
  "fmt"
)

  %gom {
    module Simple 
      abstract syntax
      B = b()
  }

  func main() {
    var tt B = `b()
    %match{
      // "A" was not declared (i.e. "A" is a bad type)
      b() << A tt -> { fmt.Println(`tt) }
    }
  }
