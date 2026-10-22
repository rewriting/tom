package testbackquote

// Port of test/TestBackQuote.t.

import "testing"

%gom {
  module thing
    imports String
    abstract syntax
    L = conc( E* )
    E = a() | b() | c() | d()
}

func Test1(t *testing.T) {
  var l L = `conc(a(), b(), c(), a(), b())
  var l2 L = `conc()
  // do not modify the layout
  // the problem was that space after the 'z*'
  %match(L l) {
    conc(
        x*,
        b(),
        y*,
        a(),
        z*
    ) -> {
      l2 = `conc(x*,
                 b(),
                 y*,
                 a(),
                 z*
           )
    }
  }
  _ = l2
  return
}

%op L abc() {}
func abc() L {
  return `conc(a(),b(),c())
}

func Test2(t *testing.T) {
  assertEquals(t, `conc(a(),b(),c()),`conc(abc*())) // was without *
}

// org.junit.Assert.assertEquals
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
