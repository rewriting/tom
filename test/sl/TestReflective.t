/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package sl;

import static org.junit.Assert.*;
import org.junit.Test;

import tom.library.sl.*;

public class TestReflective {

  %include { sl.tom }
  %include { testsl/testsl.tom }
  
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestReflective.class.getName());
  }

  @Test
  public void testMatchId() {
    Strategy s = `Identity();
    %match(s) {
      Identity() -> { return; }
    }
    fail("Identity should have matched");
  }

  @Test
  public void testMatchIdFail() {
    Strategy s = `Fail();
    %match(s) {
      Identity() -> { fail("Fail should not match"); }
    }
  }

  @Test
  public void testMatchS1() {
    Strategy s = `S1();
    %match(s) {
      S1() -> { return; }
    }
    fail("S1 should have matched");
  }

  @Test
  public void testMatchS1Fail() {
    Strategy s = `Fail();
    %match(s) {
      S1() -> { fail("Fail should not match"); }
    }
  }

  @Test
  public void testMatchSequence1() {
    Strategy s = `Sequence(Identity(), Fail());
    %match(s) {
      Sequence(Identity(), Fail()) -> { return; }
    }
    fail("match should success");
  }

  @Test
  public void testMatchSequence2() {
    Strategy s = `Sequence(Identity(), S1());
    %match(Strategy s) {
      Sequence(Identity(), Fail()) -> { fail("should not match"); }
    }
  }

  @Test
  public void testMatchSequence3() {
    Strategy s = `Sequence(Identity(), S1());
    %match(s) {
      Sequence(Identity(), S1()) -> { return; }
    }
    fail("match should success");
  }

  @Test
  public void testMatchSequence4() {
    Strategy s = `Sequence(Identity(), Fail());
    %match(s) {
      Sequence(Identity(), S1()) -> { fail("match should not success"); }
    }
  }

  @Test
  public void testMatchSequenceVar() {
    Strategy s = `Sequence(Identity(), Fail());
    %match(s) {
      Sequence(s1, s2) -> { 
        %match(s1, s2) {
          Identity(), Fail() -> {
            return;
          }
        }
      }
    }
    fail("match should success");
  }

  @Test
  public void testSequenceVarS1() {
    Strategy s = `Sequence(Identity(), S1());
    %match(s) {
      Sequence(s1, s2) -> {
        %match(s1, s2) {
          Identity(), S1() -> { return; }
        }
      }
    }
    fail("match should success");
  }

  @Test
  public void testMatchS2() {
    Strategy s = `S2(0, "msg", Identity());
    %match(s) {
      S2(i, str, _s1) -> {
        if (`i == 0) {
          if(`str.equals("msg")) {
            return;
          }
        }
      }
    }
    fail("match should success");
  }

  @Test
  public void testMatchS2in() {
    Strategy s = `S2(1, "msg", Identity());
    %match(s) {
      S2(1, "msg", s1) -> {
        %match(s1) {
          Identity() -> {
            return;
          }
        }
      }
    }
    fail("match should success");
  }

  @Test
  public void testMatchS2Fail() {
    Strategy s = `S2(1, "msg", S1());
    %match(s) {
      S2(1, "msg", Identity()) -> {
        fail("match should not succeed");
      }
    }
  }

  @Test
  public void testMatchAll1() {
    Strategy s = `Identity();
    %match(s) {
      All(All(_x)) -> { fail("no !"); }
      All(_x) -> { fail("no !"); }
    }
  }

  @Test
  public void testMatchAll2() {
    Strategy s = `All(Identity());
    %match(s) {
      All(All(_x)) -> { fail("no !"); }
      All(_x) -> { return; }
    }
    fail("no !");
  }

  @Test
  public void testMatchAll3() {
    Strategy s = `All(All(Identity()));
    %match(s) {
      All(All(_x)) -> { return; }
      All(_x) -> { fail("no !"); }
    }
    fail("no !");
  }

  @Test
  public void testS1Id() throws VisitFailure{
    Strategy s = `S1().visit(`Identity());
    %match(s) {
      Identity() -> { fail("Id should rewrite to fail"); }
      Fail() -> { return; }
    }
    fail("should not be here");
  }

  @Test
  public void testS1Fail() throws VisitFailure{
    Strategy s = `S1().visit(`Fail());
    %match(s) {
      Fail() -> { fail("Fail should rewrite to Identity"); }
      Identity() -> { return; }
    }
    fail("should not be here");
  }

  @Test
  public void testS3Id() throws VisitFailure{
    Strategy s = `S3().visit(`Identity());
    %match(s) {
      Identity() -> { return; }
    }
    fail("should not be here");
  }

  @Test
  public void testS3All() throws VisitFailure{
    Strategy s = `S3().visit(`All(Identity()));
    %match(s) {
      All[] -> { return; }
    }
    fail("should not be here");
  }

  @Test
  public void testS3AllAllvisit() throws VisitFailure{
    try {
      Strategy s = `S3().visitLight(`All(All(Identity())));
      %match(s) {
        All(All(Identity())) -> { fail("S3 did not rewrite s"); }
        All(Identity()) -> { return; }
      }
    } catch (tom.library.sl.VisitFailure vf) {
      fail("should not catch exception");
    }
    fail("should not be here");
  }

  @Test
  public void testS3AllAll() throws VisitFailure{
    Strategy s = `S3().visit(`All(All(Identity())));
    %match(s) {
      All(All(Identity())) -> { fail("S3 did not rewrite s"); }
      All(Identity()) -> { return; }
    }
    fail("should not be here with "+s);
  }

  @Test
  public void testS3Allllll() throws VisitFailure{
    Strategy s = `S3().visit(`All(All(All(All(Identity())))));
    %match(s) {
      All(Identity()) -> { fail("should not be here"); }
    }
  }

  @Test
  public void testAlllllBU() throws VisitFailure{
    Strategy s = `BottomUp(S3()).visit(`All(All(All(All(Identity())))));
    %match(s) {
      All(Identity()) -> { return; }
    }
    fail("should not be here with "+s);
  }

  @Test
  public void testcountAll1() throws VisitFailure {
    Strategy s = `All(S2(0, "", All(All(Identity()))));
    assertEquals("countAll should return 3 with "+s, 3, countAll(s));
  }

  @Test
  public void testCountAll2() throws VisitFailure{
    Strategy s = `All(S2(0, "", All(All(Identity()))));
    assertEquals(
        "countAll should return 2 with `BottomUp(S3()).visit(`"+s+")", 2,
        countAll(`BottomUp(S3()).visit(s)));
  }

  @Test
  public void testStratNoStrat() throws VisitFailure{
    assertEquals("S3 on something that is not a strat fails back to Id",
        `a(),`S3().visit(`a()));
  }

  @Test
  public void testStratNoStratFail() throws VisitFailure{
    try {
      `S4(S1(),0,S1()).visit(`a());
      fail("This application should fail");
    } catch (tom.library.sl.VisitFailure e) {
      return;
    }
    fail("The Exception should have been catched");
  }

  @Test
  public void testcountAll3() throws VisitFailure {
    Strategy s = `All(S4(All(All(Identity())), 0, All(All(All(Identity())))));
    assertEquals("countAll should return 6 with `"+s, 6, countAll(s));
  }

  @Test
  public void testcountAll4() throws VisitFailure{
    Strategy s = `All(S4(All(All(Identity())), 0, All(All(All(Identity())))));
    assertEquals(
        "countAll should return 3 with `BottomUp(S3()).visit("+s+")", 3,
        countAll(`BottomUp(S3()).visit(s)));
  }

  @Test
  public void testcountAll5() throws VisitFailure{
    Strategy s = `S4(Identity(), 0, Fail());
    assertEquals(
        "countAll should return 2 with `BottomUp(S5()).visit(`"+s+")", 2,
        countAll(`BottomUp(S5()).visit(s)));
  }

  @Test
  public void testMake() throws VisitFailure  {
    Strategy s = `Make_Consl(Make_a(),Make_Emptyl());
    %match(s) {
      Make_Consl(x,Make_Emptyl()) -> { return; }
    }  
    fail("match should success");
  }

  @Test
  public void testMakeWithBuiltin() throws VisitFailure  {
    Strategy s = `Make_m(1);
    %match(s) {
      Make_m(_) -> { return; }
    }  
    fail("match should success");
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
  private static class Counter { public int count = 0; }
  %typeterm Counter {
    implement { Counter }
    is_sort(t) { t instanceof Counter }
  }
  public int countAll(Strategy s) throws VisitFailure {
    Counter c = new Counter();
    `TopDown(incAll(c)).visit(s);
    return c.count;
  }

  %strategy incAll(c:Counter) extends `Identity() {
    visit Strategy {
      All(_) -> { c.count++; }
    }
  }
}
