package testalgebraic

// Port of test/TestAlgebraic.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import testalgebraic.sig.*; import testalgebraic.sig.types.*;`.
// port: no main — `go test` runs Test1 (the original main only launched JUnit).

import (
  "testing"

  "tom/library/sl"
)

%include { sl.tom }
%gom {
  module sig
  imports
  abstract syntax
  T1 = a() | b() | c()
    | f(x:T1)
  T2 = d()
}

%strategy R() extends Identity() {
  visit T1 {
    a() -> b()
    b() -> c()
  }
}

func Test1(t *testing.T) {
  var input T1 = `f(a())
  var s sl.Strategy = `RepeatId(BottomUp(R()))
  // port: s.visit(input) is s.Visit(input, sl.VisitableIntrospector{}) (Go
  // has no overloading); the VisitFailure exception becomes the returned
  // error, and the generic <T> T result an r.(T1).
  r, err := s.Visit(input, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatalf("VisitFailure: %v", err)
  }
  var res T1 = r.(T1)
  assertEquals(t, res, `f(c()))
}

// org.junit.Assert.assertEquals
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
