package main

// Port of test/regress/BadBackquote.t, a non-regression test of tom's
// diagnostics (test/build.xml, target regress): tom must reject it, with
// the same errors as legacy/ on the original (port_test.go, Regress).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import regress.badbackquote.example.types.*;`.
// port: `_ = t`, as Go rejects an unused local variable.

  %gom {
    module Example
      abstract syntax
      A = a()
      | h(num:B)

      B = b()
  }

  func main() {
    var t A = `h(a())
    _ = t
  }
