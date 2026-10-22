package gom

// Port of test/gom/TestGomWithoutMaximalSharing.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testgomwithoutmaximalsharing.term.types.*;`.
// port: deepEquals is DeepEquals; assertSame/assertNotSame compare with
// == (pointer identity, as Java's ==).
// port: tom.library.sl.VisitFailure is the error returned by Visit and
// VisitLight (the try/catch blocks test it).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "tom/library/sl"
)

  %gom(--nosharing) {
    module term
    abstract syntax
    T = a()
      | b()
      | c()
      | f(One:T,Two:T)
      | g(One:T)
  }

  %include{ sl.tom }

func TestBuild(t *testing.T) {
    var t1 T = `f(g(a()),g(b()))
    var t2 T = `f(g(a()),g(b()))
    assertNotSame(t, "Should be not the same", t1, t2)
    assertTrue(t, "Should be equivalent", t1.DeepEquals(t2))
}


func TestMatch(tt *testing.T) {
    // port: the *testing.T parameter is named tt because the original's
    // local `T t` would shadow it.
    var t T = `f(g(a()),g(b()))
    %match (t) {
      f(x,y) -> {
        assertTrue(tt, "Should be equivalent", `x.DeepEquals(`g(a())))
        assertTrue(tt, "Should be equivalent", `y.DeepEquals(`g(b())))
      }
    }
}


  %strategy Transform() extends Identity() {
    visit T {
      a() -> c()
      b() -> c()
    }
  }


func TestStrategy(tt *testing.T) {
    var t T = `f(g(a()),g(b()))
    {
    r1, e1 := `TopDown(Transform()).Visit(t, sl.VisitableIntrospector{})
    if e1 != nil {
      fail(tt)
    }
    assertTrue(tt, "Should be equivalent", `f(g(c()),g(c())).DeepEquals(r1))
    r2, e2 := `TopDown(Transform()).VisitLight(t, sl.VisitableIntrospector{})
    if e2 != nil {
      fail(tt)
    }
    assertTrue(tt, "Should be equivalent", `f(g(c()),g(c())).DeepEquals(r2))
    }
}


func TestIdentity(tt *testing.T) {
    var t T = `f(g(a()),g(b()))
    {
      r1, e1 := `TopDown(Identity()).Visit(t, sl.VisitableIntrospector{})
      if e1 != nil {
        fail(tt)
      }
      assertSame(tt, "Should be equal", r1, t)
      r2, e2 := `TopDown(Identity()).VisitLight(t, sl.VisitableIntrospector{})
      if e2 != nil {
        fail(tt)
      }
      assertSame(tt, "Should be equal", r2, t) 
    }
}

// org.junit.Assert.assertTrue(message, condition)
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}

// org.junit.Assert.assertSame(message, expected, actual)
func assertSame(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.assertNotSame(message, unexpected, actual)
func assertNotSame(t *testing.T, message string, unexpected, actual any) {
  t.Helper()
  if unexpected == actual {
    t.Fatalf("%s expected not same", message)
  }
}

// org.junit.Assert.fail()
func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
