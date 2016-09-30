import static org.junit.Assert.*;
import org.junit.Test;

import tom.library.sl.*;

public class TestReflectiveStrategy {
  %include { sl.tom }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestReflectiveStrategy.class.getName());
  }

  /**
   * Sets up the test fixture. 
   * (Called before every test case method.)
   */
  public void setUp() {
  }

  /**
   * Tears down the test fixture. 
   * (Called after every test case method.)
   */
  public void tearDown() {
  }

  @Test
  public void test1() {
    assertTrue("matchIdentity should return true with `Identity()", matchIdentity(`Identity()));
  }

  @Test
  public void test2() {
    assertFalse("matchIdentity should return false with `Fail()", matchIdentity(`Fail()));
  }

  @Test
  public void test3() {
    assertTrue("matchS1 should return true with `S1()", matchS1(`S1()));
  }

  @Test
  public void test4() {
    assertFalse("matchS1 should return false with `Identity()", matchS1(`Identity()));
  }

  @Test
  public void test5() {
    assertTrue("matchSequence1 should return true with `Sequence(Identity(), Fail())", matchSequence1(`Sequence(Identity(), Fail())));
  }

  @Test
  public void test6() {
    assertFalse("matchSequence1 should return false with `Sequence(Identity(), S1())", matchSequence1(`Sequence(Identity(), S1())));
  }

  @Test
  public void test7() {
    assertTrue("matchSequence2 should return true with `Sequence(Identity(), S1())", matchSequence2(`Sequence(Identity(), S1())));
  }

  @Test
  public void test8() {
    assertFalse("matchSequence2 should return false with `Sequence(Identity(), Fail())", matchSequence2(`Sequence(Identity(), Fail())));
  }

  @Test
  public void test9() {
    assertTrue("matchSequence3 should return true with `Sequence(Identity(), Fail())", matchSequence3(`Sequence(Identity(), Fail())));
  }

  @Test
  public void test10() {
    assertFalse("matchSequence3 should return false with `Sequence(Identity(), S1())", matchSequence3(`Sequence(Identity(), S1())));
  }

  @Test
  public void test11() {
    assertTrue("matchSequence4 should return true with `Sequence(Identity(), S1())", matchSequence4(`Sequence(Identity(), S1())));
  }

  @Test
  public void test12() {
    assertFalse("matchSequence4 should return false with `Sequence(Identity(), Fail())", matchSequence4(`Sequence(Identity(), Fail())));
  }

  @Test
  public void test13() {
    assertEquals("matchS2 should return 1 with `S2(0, \"msg\", Identity())", 1, matchS2(`S2(0, "msg", Identity())));
  }

  @Test
  public void test14() {
    assertEquals("matchS2 should return 2 with `S2(1, \"msg\", Identity())", 2, matchS2(`S2(1, "msg", Identity())));
  }

  @Test
  public void test15() {
    assertEquals("matchS2 should return 3 with `S2(1, \"\", Identity())", 3, matchS2(`S2(1, "", Identity())));
  }

  @Test
  public void test16() {
    assertEquals("matchS2 should return 4 with `S2(1, \"\", S1())", 4, matchS2(`S2(1, "", S1())));
  }

  @Test
  public void test17() {
    assertEquals("matchS2 should return 0 with `S2(1, \"\", Fail())", 0, matchS2(`S2(1, "", Fail())));
  }

  @Test
  public void test18() {
    assertEquals("matchAll should return 0 with `Identity()", 0, matchAll(`Identity()));
  }

  @Test
  public void test19() {
    assertEquals("matchAll should return 1 with `All(Identity())", 1, matchAll(`All(Identity())));
  }

  @Test
  public void test20() {
    assertEquals("matchAll should return 2 with `All(All(Identity()))", 2, matchAll(`All(All(Identity()))));
  }

  @Test
  public void test21() throws tom.library.sl.VisitFailure {
    assertTrue("matchIdentity should return true with `S3().visitLight(`Identity())", matchIdentity((Strategy)`S3().visitLight(`Identity())));
  }

  @Test
  public void test22() throws tom.library.sl.VisitFailure {
    assertEquals("matchAll should return 1 with `S3().visitLight(`All(Identity())", 1, matchAll((Strategy)`S3().visitLight(`All(Identity()))));
  }

  @Test
  public void test23() throws tom.library.sl.VisitFailure  {
    assertEquals("matchAll should return 1 with `S3().visitLight(`All(All(Identity())))", 1, matchAll((Strategy)`S3().visitLight(`All(All(Identity())))));
  }

  @Test
  public void test24() throws tom.library.sl.VisitFailure {
    assertEquals("matchAll should return 1 with `TopDown(S3()).visitLight(`All(All(All(All(Identity())))))", 2, matchAll((Strategy)`TopDown(S3()).visitLight(`All(All(All(All(Identity())))))));
  }

  @Test
  public void test25() throws tom.library.sl.VisitFailure {
    assertEquals("matchAll should return 1 with `BottomUp(S3()).visitLight(`All(All(All(All(Identity())))))", 1, matchAll((Strategy)`BottomUp(S3()).visitLight(`All(All(All(All(Identity())))))));
  }

  @Test
  public void test26() throws tom.library.sl.VisitFailure {
    assertEquals("countAll should return 3 with `All(S2(0, \"\", All(All(Identity()))))", 3, countAll(`All(S2(0, "", All(All(Identity()))))));
  }

  @Test
  public void test27() throws tom.library.sl.VisitFailure {
    assertEquals("countAll should return 2 with `BottomUp(S3()).visitLight(`All(S2(0, \"\", All(All(Identity())))))", 2, countAll((Strategy)`BottomUp(S3()).visitLight(`All(S2(0, "", All(All(Identity())))))));
  }

  @Test
  public void test28() throws tom.library.sl.VisitFailure {
    assertEquals("countAll should return 6 with `All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 6, countAll(`All(S4(All(All(Identity())), 0, All(All(All(Identity())))))));
  }

  @Test
  public void test29() throws tom.library.sl.VisitFailure {
    assertEquals("countAll should return 3 with `BottomUp(S3()).visitLight(`All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 3, countAll((Strategy)`BottomUp(S3()).visitLight(`All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))));
  }

  @Test
  public void test30() throws tom.library.sl.VisitFailure {
    assertEquals("countAll should return 2 with `BottomUp(S5()).visitLight(`S4(Identity(), 0, Fail()))", 2, countAll((Strategy)`BottomUp(S5()).visitLight(`S4(Identity(), 0, Fail()))));
  }
  
  @Test
  public void test31() throws tom.library.sl.VisitFailure {
    Counter c = new Counter();
    Strategy s = `mu(MuVar("x"),TopDownCollect(CollectExceptFirst(MuVar("x"),c)));
    s.visitLight(`Sequence(S1(),Sequence(Choice(S1(),S1()),Choice(S1(),S1()))));
    assertEquals("collectExceptFirst should return 5 when applied on `Sequence(S1(),Sequence(Choice(S1(),S1()),Choice(S1(),S1())))", 5, c.count);
  }

  // matching basic strategy
  public boolean matchIdentity(Strategy s) {
    %match(s) {
      Identity() -> { return true; }
    }
    return false;
  }

  // matching user defined strategy
  public boolean matchS1(Strategy s) {
    %match(s) {
      S1() -> { return true; }
    }
    return false;
  }

  // matching basic strategy with basic children
  public boolean matchSequence1(Strategy s) {
    %match(s) {
      Sequence(Identity(), Fail()) -> { return true; }
    }
    return false;
  }

  // matching basic strategy with user defined children
  public boolean matchSequence2(Strategy s) {
    %match(s) {
      Sequence(Identity(), S1()) -> { return true; }
    }
    return false;
  }

  // matching basic strategy with basic children using slots
  public boolean matchSequence3(Strategy s) {
    %match(s) {
      Sequence(s1, s2) -> { return matchSequence1(`Sequence(s1, s2)); }
    }
    return false;
  }

  // matching basic strategy with user defined children using slots
  public boolean matchSequence4(Strategy s) {
    %match(s) {
      Sequence(s1, s2) -> { return matchSequence2(`Sequence(s1, s2)); }
    }
    return false;
  }

  // matching user defined strategy and use slots
  public int matchS2(Strategy s) {
    %match(s) {
      S2(i, str, s1) -> {
        if (`i == 0) {
          return 1;
        } else if (`str.equals("msg")) {
          return 2;
        } else if (matchIdentity(`s1)) {
          return 3;
        } else if (matchS1(`s1)) {
          return 4;
        }
      }
    }
    return 0;
  }

  // match 1 or 2 All
  public int matchAll(Strategy s) {
    %match(Strategy s) {
      All(All(_x)) -> { return 2; }
      All(_x) -> { return 1; }
    }
    return 0;
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
  private static  class Counter { public int count = 0; }
  %typeterm Counter {
    implement { Counter }
  }
  public int countAll(Strategy s) throws tom.library.sl.VisitFailure {
    Counter c = new Counter();
    `TopDown(incAll(c)).visitLight(s);
    return c.count;
  }
  %strategy incAll(c:Counter) extends `Identity() {
    visit Strategy {
      All[] -> { c.count++; }
    }
  }
 
  /* collect all strategies, except those in first argument of a sequence */
  %strategy CollectExceptFirst(current:Strategy, c:Counter) extends `Identity() {
    visit Strategy {
      Sequence(_,s2*) -> {
      current.visitLight(`s2);
      throw new VisitFailure(); 
      }

      _x -> { c.count++; }
    }
  }
}

