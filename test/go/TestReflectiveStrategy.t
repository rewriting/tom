// Port of test/TestReflectiveStrategy.t: strategies matched and built as
// terms.
// port: tom.library.sl.Strategy is sl.Strategy; Java's visitLight throws
// VisitFailure, the Go VisitLight returns it: visitLight below fails the
// test on it, as the exception escaping the JUnit test does (the tests
// declare `throws VisitFailure`); the `(Strategy)` casts of its result are
// .(sl.Strategy). In a %strategy rule, `current.visitLight(s2)` is
// sl.MustVisit(…VisitLight(…)) (the VisitFailure propagates) and
// `throw new VisitFailure()` is sl.ThrowVisitFailure().
// port: host code reads the strategy parameters as fields of the receiver
// (tom__strat.c, tom__strat.current), as in the port of sl/TestReflective.t.
// port: the instance methods are functions; `str.equals("msg")` is
// `str == "msg"`; assertTrue/assertFalse/assertEquals are helpers.
// port: the empty setUp/tearDown (not JUnit fixtures: no annotation) are
// kept as empty functions.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testreflectivestrategy

import (
  "testing"

  "tom/library/sl"
)

  %include { sl.tom }

  /**
   * Sets up the test fixture. 
   * (Called before every test case method.)
   */
func setUp() {
  }

  /**
   * Tears down the test fixture. 
   * (Called after every test case method.)
   */
func tearDown() {
  }

func Test1(t *testing.T) {
    assertTrue(t, "matchIdentity should return true with `Identity()", matchIdentity(`Identity()))
  }

func Test2(t *testing.T) {
    assertFalse(t, "matchIdentity should return false with `Fail()", matchIdentity(`Fail()))
  }

func Test3(t *testing.T) {
    assertTrue(t, "matchS1 should return true with `S1()", matchS1(`S1()))
  }

func Test4(t *testing.T) {
    assertFalse(t, "matchS1 should return false with `Identity()", matchS1(`Identity()))
  }

func Test5(t *testing.T) {
    assertTrue(t, "matchSequence1 should return true with `Sequence(Identity(), Fail())", matchSequence1(`Sequence(Identity(), Fail())))
  }

func Test6(t *testing.T) {
    assertFalse(t, "matchSequence1 should return false with `Sequence(Identity(), S1())", matchSequence1(`Sequence(Identity(), S1())))
  }

func Test7(t *testing.T) {
    assertTrue(t, "matchSequence2 should return true with `Sequence(Identity(), S1())", matchSequence2(`Sequence(Identity(), S1())))
  }

func Test8(t *testing.T) {
    assertFalse(t, "matchSequence2 should return false with `Sequence(Identity(), Fail())", matchSequence2(`Sequence(Identity(), Fail())))
  }

func Test9(t *testing.T) {
    assertTrue(t, "matchSequence3 should return true with `Sequence(Identity(), Fail())", matchSequence3(`Sequence(Identity(), Fail())))
  }

func Test10(t *testing.T) {
    assertFalse(t, "matchSequence3 should return false with `Sequence(Identity(), S1())", matchSequence3(`Sequence(Identity(), S1())))
  }

func Test11(t *testing.T) {
    assertTrue(t, "matchSequence4 should return true with `Sequence(Identity(), S1())", matchSequence4(`Sequence(Identity(), S1())))
  }

func Test12(t *testing.T) {
    assertFalse(t, "matchSequence4 should return false with `Sequence(Identity(), Fail())", matchSequence4(`Sequence(Identity(), Fail())))
  }

func Test13(t *testing.T) {
    assertEquals(t, "matchS2 should return 1 with `S2(0, \"msg\", Identity())", 1, matchS2(`S2(0, "msg", Identity())))
  }

func Test14(t *testing.T) {
    assertEquals(t, "matchS2 should return 2 with `S2(1, \"msg\", Identity())", 2, matchS2(`S2(1, "msg", Identity())))
  }

func Test15(t *testing.T) {
    assertEquals(t, "matchS2 should return 3 with `S2(1, \"\", Identity())", 3, matchS2(`S2(1, "", Identity())))
  }

func Test16(t *testing.T) {
    assertEquals(t, "matchS2 should return 4 with `S2(1, \"\", S1())", 4, matchS2(`S2(1, "", S1())))
  }

func Test17(t *testing.T) {
    assertEquals(t, "matchS2 should return 0 with `S2(1, \"\", Fail())", 0, matchS2(`S2(1, "", Fail())))
  }

func Test18(t *testing.T) {
    assertEquals(t, "matchAll should return 0 with `Identity()", 0, matchAll(`Identity()))
  }

func Test19(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `All(Identity())", 1, matchAll(`All(Identity())))
  }

func Test20(t *testing.T) {
    assertEquals(t, "matchAll should return 2 with `All(All(Identity()))", 2, matchAll(`All(All(Identity()))))
  }

func Test21(t *testing.T) {
    assertTrue(t, "matchIdentity should return true with `S3().visitLight(`Identity())", matchIdentity(visitLight(t, `S3(), `Identity()).(sl.Strategy)))
  }

func Test22(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `S3().visitLight(`All(Identity())", 1, matchAll(visitLight(t, `S3(), `All(Identity())).(sl.Strategy)))
  }

func Test23(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `S3().visitLight(`All(All(Identity())))", 1, matchAll(visitLight(t, `S3(), `All(All(Identity()))).(sl.Strategy)))
  }

func Test24(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `TopDown(S3()).visitLight(`All(All(All(All(Identity())))))", 2, matchAll(visitLight(t, `TopDown(S3()), `All(All(All(All(Identity()))))).(sl.Strategy)))
  }

func Test25(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `BottomUp(S3()).visitLight(`All(All(All(All(Identity())))))", 1, matchAll(visitLight(t, `BottomUp(S3()), `All(All(All(All(Identity()))))).(sl.Strategy)))
  }

func Test26(t *testing.T) {
    assertEquals(t, "countAll should return 3 with `All(S2(0, \"\", All(All(Identity()))))", 3, countAll(t, `All(S2(0, "", All(All(Identity()))))))
  }

func Test27(t *testing.T) {
    assertEquals(t, "countAll should return 2 with `BottomUp(S3()).visitLight(`All(S2(0, \"\", All(All(Identity())))))", 2, countAll(t, visitLight(t, `BottomUp(S3()), `All(S2(0, "", All(All(Identity()))))).(sl.Strategy)))
  }

func Test28(t *testing.T) {
    assertEquals(t, "countAll should return 6 with `All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 6, countAll(t, `All(S4(All(All(Identity())), 0, All(All(All(Identity())))))))
  }

func Test29(t *testing.T) {
    assertEquals(t, "countAll should return 3 with `BottomUp(S3()).visitLight(`All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 3, countAll(t, visitLight(t, `BottomUp(S3()), `All(S4(All(All(Identity())), 0, All(All(All(Identity())))))).(sl.Strategy)))
  }

func Test30(t *testing.T) {
    assertEquals(t, "countAll should return 2 with `BottomUp(S5()).visitLight(`S4(Identity(), 0, Fail()))", 2, countAll(t, visitLight(t, `BottomUp(S5()), `S4(Identity(), 0, Fail())).(sl.Strategy)))
  }
  
func Test31(t *testing.T) {
    c := &Counter{}
    var s sl.Strategy = `mu(MuVar("x"),TopDownCollect(CollectExceptFirst(MuVar("x"),c)))
    visitLight(t, s, `Sequence(S1(),Sequence(Choice(S1(),S1()),Choice(S1(),S1()))))
    assertEquals(t, "collectExceptFirst should return 5 when applied on `Sequence(S1(),Sequence(Choice(S1(),S1()),Choice(S1(),S1())))", 5, c.count)
  }

  // matching basic strategy
  func matchIdentity(s sl.Strategy) bool {
    %match(s) {
      Identity() -> { return true }
    }
    return false
  }

  // matching user defined strategy
  func matchS1(s sl.Strategy) bool {
    %match(s) {
      S1() -> { return true }
    }
    return false
  }

  // matching basic strategy with basic children
  func matchSequence1(s sl.Strategy) bool {
    %match(s) {
      Sequence(Identity(), Fail()) -> { return true }
    }
    return false
  }

  // matching basic strategy with user defined children
  func matchSequence2(s sl.Strategy) bool {
    %match(s) {
      Sequence(Identity(), S1()) -> { return true }
    }
    return false
  }

  // matching basic strategy with basic children using slots
  func matchSequence3(s sl.Strategy) bool {
    %match(s) {
      Sequence(s1, s2) -> { return matchSequence1(`Sequence(s1, s2)) }
    }
    return false
  }

  // matching basic strategy with user defined children using slots
  func matchSequence4(s sl.Strategy) bool {
    %match(s) {
      Sequence(s1, s2) -> { return matchSequence2(`Sequence(s1, s2)) }
    }
    return false
  }

  // matching user defined strategy and use slots
  func matchS2(s sl.Strategy) int {
    %match(s) {
      S2(i, str, s1) -> {
        if `i == 0 {
          return 1
        } else if `str == "msg" {
          return 2
        } else if matchIdentity(`s1) {
          return 3
        } else if matchS1(`s1) {
          return 4
        }
      }
    }
    return 0
  }

  // match 1 or 2 All
  func matchAll(s sl.Strategy) int {
    %match(Strategy s) {
      All(All(_x)) -> { return 2 }
      All(_x) -> { return 1 }
    }
    return 0
  }


  %strategy S1() extends `Identity() {
    visit Strategy {
      Identity() -> { return `Fail(); }
      Fail() -> { return `Identity(); }
    }
  }

  %strategy S2(i:int, str:String, s:Strategy) extends `Identity() {
    visit Strategy {
      Identity() -> { return `Fail(); }
    }
  }

  %strategy S3() extends `Identity() {
    visit Strategy {
      All(All(x)) -> { return `All(x); }
    }
  }

  %strategy S4(s1:Strategy, i:int, s2:Strategy) extends `Fail() {
    visit Strategy {
      Identity() -> { return `s1; }
      Fail() -> { return `All(s2); }
    }
  }

  %strategy S5() extends `Identity() {
    visit Strategy {
      Fail() -> { return `All(Identity()); }
    }
  }

  // count the number of All nodes
  type Counter struct{ count int }
  %typeterm Counter {
    implement { *Counter }
  }
  // port: countAll fails the test on a VisitFailure (the original throws
  // it).
  func countAll(t *testing.T, s sl.Strategy) int {
    c := &Counter{}
    visitLight(t, `TopDown(incAll(c)), s)
    return c.count
  }
  %strategy incAll(c:Counter) extends `Identity() {
    visit Strategy {
      All[] -> { tom__strat.c.count++ }
    }
  }
 
  /* collect all strategies, except those in first argument of a sequence */
  %strategy CollectExceptFirst(current:Strategy, c:Counter) extends `Identity() {
    visit Strategy {
      Sequence(_,s2*) -> {
      sl.MustVisit(tom__strat.current.VisitLight(`s2, sl.VisitableIntrospector{}))
      sl.ThrowVisitFailure() 
      }

      _x -> { tom__strat.c.count++ }
    }
  }

// s.visitLight(subject), whose VisitFailure the original lets escape the
// test.
func visitLight(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}

// org.junit.Assert.assertTrue / assertFalse / assertEquals (message first)
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}

func assertFalse(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Error(message)
  }
}

func assertEquals(t *testing.T, message string, expected, actual int) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%d> but was:<%d>", message, expected, actual)
  }
}
