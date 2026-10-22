package gom

// Port of test/gom/TestTomHook.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testtomhook.bool.types.*;`.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module Bool
    abstract syntax
    Bool = True()
         | False()
         | Not(b:Bool)
         | And(l:Bool,r:Bool)
         | Or(l:Bool,r:Bool)
    Not:make(b) {
      %match(Bool b) {
        And(l,r) -> { return `Or(Not(l),Not(r)); }
        Or(l,r)  -> { return `And(Not(l),Not(r)); }
        True()   -> { return `False(); }
        False()  -> { return `True(); }
      }
    }
  }

func TestTrue(t *testing.T) {
    var test Bool = `Not(True())
    assertEquals(t, test,`False())
}

func TestFalse(t *testing.T) {
    var test Bool = `Not(False())
    assertEquals(t, test,`True())
}

func TestNotAndHook(t *testing.T) {
    var test Bool = `Not(And(True(),True()))
    assertEquals(t, test,`Or(False(),False()))
}

func TestNotOrHook(t *testing.T) {
    var test Bool = `Not(Or(True(),True()))
    assertEquals(t, test,`And(False(),False()))
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
