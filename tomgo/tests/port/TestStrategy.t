package teststrategy

// Port of test/TestStrategy.t. Each @Test method becomes a Go test
// (testS → TestS, testPosition → TestPosition, testFnc → TestFnc).
// port: the generated %gom types live in this package, so there is no
// counterpart to `import teststrategy.term.types.*;`.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
// port: setUp() is empty and never called by JUnit 4 (no @Before): dropped.

import (
  "container/list"
  "testing"

  "tom/tomgo/stable/library/sl"
)

%include { sl.tom }
%include { boolean.tom }
%include { long.tom }
// port: there is no Go mapping java/util/LinkedList.tom (Go has no
// java.util.LinkedList); only the sort is used, so it is declared here
// on Go's container/list.
%typeterm LinkedList {
  implement { *list.List }
}

%typeterm Hashtable {
  implement { Hashtable }
}

// port: java.util.Hashtable becomes a Go map.
type Hashtable map[any]any

%gom{
    module Term

      abstract syntax
      Term = a() | b() | c() | d()
      | f(s1:Term)
      | ff(s1:Term)
      | g(s1:Term,s2:Term)
      | l(s3:L)
      | h(s1:Term,s2:Term)
      Term1 = e()
      L = concTerm(Term*)

      Pf = Unary() | Variadic(Pf*)
  }

var bool0 bool = true

func TestS(t *testing.T) {

  var hashtable Hashtable = Hashtable{}
  var linkedList *list.List = list.New()
  var bool bool = true
  var along int64 = 1
  var string string = ""
  var i int64 = 0

  var rule0 sl.Strategy = `S0()
  var rule1 sl.Strategy = `S1(bool)
  var rule2 sl.Strategy = `S2(along,string)
  var rule3 sl.Strategy = `S3(i)
  var rule4 sl.Strategy = `S4(hashtable, linkedList)
  var rule5 sl.Strategy = `S5()
  var rule6 sl.Strategy = `S6()
  var rule7 sl.Strategy = `S7()
  var rule8 sl.Strategy = `S8(bool)
  var rule9 sl.Strategy = `S9(i)

  // port: Java's visitLight(t) throws VisitFailure; the Go VisitLight
  // returns it, and visitLight below turns it back into the panic the
  // deferred recover plays the catch block of.
  defer func() {
    if r := recover(); r != nil {
      if _, ok := r.(visitFailure); !ok {
        panic(r)
      }
      println("tom.library.sl.VisitFailure()")
    }
  }()
  assertSame(t, "g(a,a) return a", visitLight(`rule0, `g(a(),a())), `a())
  assertSame(t, "a return b", visitLight(`rule1, `a()), `b())
  assertSame(t, "g(a,a) return a", visitLight(`rule2, `g(a(),a())), `a())
  assertSame(t, "g(a,a) return a", visitLight(`rule3, `g(a(),a())), `a())
  assertSame(t, "g(a,b) return g(b,a)", visitLight(`rule4, `g(a(),b())), `g(b(),a()))
  assertSame(t, "g(a,a) return a", visitLight(`rule5, `g(a(),a())), `a())
  assertSame(t, "g(a,a) return a", visitLight(`rule6, `g(a(),a())), `a())
  assertSame(t, "g(a,a) return a", visitLight(`rule7, `g(a(),a())), `a())
  assertSame(t, "g(a,a) return a", visitLight(`rule8, `g(a(),a())), `a())
  assertSame(t, "g(a,a) return a", visitLight(`rule9, `g(a(),a())), `a())
}

// port: the *testing.T parameter is named tt because the original's
// local `Term t` would shadow it.
func TestPosition(tt *testing.T) {
    //3 positions for now
    var positions Hashtable = Hashtable{}
    var t Term = `f(a())
    var tBis Term = `f(b())
    var getPos1 sl.Strategy = `GetPositionA(positions,"p1")
    var getPos2 sl.Strategy = `GetPositionA(positions,"p2")
    var getPos3 sl.Strategy = `GetPositionA(positions,"p3")

    visit(tt, `BottomUp(getPos1), t)
    visit(tt, `BottomUp(getPos2), t)
    visit(tt, `BottomUp(getPos3), tBis)

    // port: Java's (Position) cast lets null through; the comma-ok
    // assertion keeps a nil *sl.Position (p3: tBis has no a()).
    p1, _ := positions["p1"].(*sl.Position)
    p2, _ := positions["p2"].(*sl.Position)
    p3, _ := positions["p3"].(*sl.Position)
    assertTrue(tt, "equality on Position", p1.Equals(p2))
    assertFalse(tt, "inequality on Position", p1.Equals(p3))
}

  %strategy GetPositionA(positions:Hashtable,posName:String) extends Identity(){
    visit Term {
      // port: host code reads the strategy parameters as fields of the
      // receiver, and getEnvironment() as tom__strat.GetEnvironment()
      // (positions.put(posName,getEnvironment().getPosition()) in Java).
      a() -> {tom__strat.positions[tom__strat.posName] = tom__strat.GetEnvironment().GetPosition()}
    }
  }

  %strategy S0() extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  //with parameter
  %strategy S1(b:boolean) extends Identity() {
    visit Term {
      a() -> { return `b(); }
    }
  }

  //with 2 parameters
  %strategy S2(l:long,s:String) extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  //with builtin parameter
  %strategy S3(i:int) extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S4(hashtable:Hashtable,linkedList:LinkedList) extends Identity() {
    visit Term {
      // port: host code reads the strategy parameter as a field of the
      // receiver (linkedList.add("ok") in Java).
      g(x,y) -> { tom__strat.linkedList.PushBack("ok"); return `g(y,x);}
    }
  }

   //with many visits
  %strategy S5() extends Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
    visit Term1 {
      e() -> { return `e(); }
    }
  }
  //with visits in reverse order
  %strategy S6() extends Identity() {
    visit Term1 {
      e() -> { return `e(); }
    }

    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S7() extends S8(bool0) {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }
  %strategy S8(b:boolean) extends `S9(3) {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy S9(i:int) extends  Identity() {
    visit Term {
      g(x,x) -> { return `x; }
    }
  }

  %strategy fnc() extends Identity() {
    visit Pf {
      Unary() -> { return `Variadic(Variadic(Unary()));}
    }
  }

func TestFnc(t *testing.T) {
  r, err := `fnc().VisitLight(`Unary(), sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  assertEquals(t, r, `Variadic(Variadic(Unary())))
}

  //with no visit
  //%strategy S4() extends Identity() {
  //}


  //with empty visit

  //with underscore only

// visitFailure carries a VisitFailure out of visitLight (see TestS).
type visitFailure struct{ err error }

// tom.library.sl.Strategy.visitLight(t), which throws VisitFailure
func visitLight(s sl.Strategy, subject any) any {
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    panic(visitFailure{err})
  }
  return r
}

// tom.library.sl.Strategy.visit(t), which throws VisitFailure out of
// the JUnit test (reported as an error): the Go test fails instead.
func visit(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}

// org.junit.Assert.assertTrue
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Errorf("%s", message)
  }
}

// org.junit.Assert.assertFalse
func assertFalse(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Errorf("%s", message)
  }
}

// org.junit.Assert.assertSame
func assertSame(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.assertEquals
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
