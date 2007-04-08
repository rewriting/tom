/*
 * Copyright (c) 2004-2007, INRIA
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
 * 	- Neither the name of the INRIA nor the names of its
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

import sl.testsl.types.*;
import tom.library.sl.Strategy;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestSLModes extends TestCase {

  %include { sl.tom }
  %include { testsl/testsl.tom }
  %include { testsl/_testsl.tom }

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestSLModes.class));
	}

  %strategy R1() extends `Identity() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }

  %strategy R2() extends `Fail() {
    visit Term {
      f(a()) -> { return `f(b()); }
      b() -> { return `c(); }
    }
  }

  %strategy R3() extends `Fail() {
    visit Term {
      f(b()) -> { return `f(c()); }
      c() -> { return `a(); }
    }
  }

  %strategy R4() extends `Identity() {
    visit Term {
      f(b()) -> { return `f(c()); }
      c() -> { return `a(); }
    }
  }

  public void testIdentity() {
    Term subject = `f(a());
    Strategy s = `Identity();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Identity do not change subject",subject,resJ);
    } catch (jjtraveler.VisitFailure e) {
      fail("Identity.visit should not fail");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Identity do not change subject",subject,resS);
    } catch (tom.library.sl.FireException e) {
      fail("Identity.fire should not fail");
    }
    assertEquals(resJ,resS);
  }

  public void testR1() {
    Term subject = `f(a());
    Strategy s = `R1();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("R1.visit should not fail on f(a)");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (tom.library.sl.FireException e) {
      fail("R1.fire should not fail on f(a)");
    }
    assertEquals(resJ,resS);
  }

  public void testR1Fail() {
    Term subject = `f(b());
    Strategy s = `R1();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule : fail is identity",resJ,subject);
    } catch (jjtraveler.VisitFailure e) {
      fail("R1.visit should not throw failure on f(b)");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,subject);
    } catch (tom.library.sl.FireException e) {
      fail("R1.fire should not throw failure on f(b)");
    }
    assertEquals(resJ,resS);
  }

  public void testR2() {
    Term subject = `f(a());
    Strategy s = `R2();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("R2.visit should not fail on f(a)");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (tom.library.sl.FireException e) {
      fail("R2.fire should not fail on f(a)");
    }
    assertEquals(resJ,resS);
  }

  public void testR2Fail() {
    Term subject = `f(b());
    Strategy s = `R2();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("R2.visit should fail on f(b)");
    } catch (jjtraveler.VisitFailure e) {
      assertNull(resJ);
    }
    try {
      resS = (Term) s.fire(subject);
      fail("R2.fire should fail on f(b)");
    } catch (tom.library.sl.FireException e) {
      assertNull(resS);
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testOne() {
    Term subject = `f(b());
    Strategy s = `One(R1());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`f(c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("One(R1).visit should not fail on f(b)");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(c()));
    } catch (tom.library.sl.FireException e) {
      fail("One(R1).fire should not fail on f(b)");
    }
    assertEquals(resJ,resS);
  }

  public void testOneFail() {
    Term subject = `f(a());
    Strategy s = `One(R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("One(R2).visit should fail on f(a)");
    } catch (jjtraveler.VisitFailure e) {
      assertNull(resJ);
    }
    try {
      resS = (Term) s.fire(subject);
      fail("One(R2).fire should fail on f(a)");
    } catch (tom.library.sl.FireException e) {
      assertNull(resS);
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testOnceBottomUp() {
    Term subject = `g(f(a()),b());
    Strategy s = `OnceBottomUp((R2()));
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("OnceBottomUp(R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),b()));
    } catch (tom.library.sl.FireException e) {
      fail("OnceBottomUp(R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
    subject = resJ;
    /* second application */
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(c()),b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("OnceBottomUp(R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(c()),b()));
    } catch (tom.library.sl.FireException e) {
      fail("OnceBottomUp(R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
    subject = resJ;
    /* third application */
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(c()),c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("OnceBottomUp(R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(c()),c()));
    } catch (tom.library.sl.FireException e) {
      fail("OnceBottomUp(R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
    subject = resJ;
    /* fourth application: it fails */
    try {
      resJ = (Term) s.visit(subject);
      fail("OnceBottomUp(R2).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
      resJ = null;
    }
    try {
      resS = (Term) s.fire(subject);
      fail("OnceBottomUp(R2).fire should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
      resS = null;
    }
    assertNull(resS);
    assertNull(resJ);
  }

  public void testAll() {
    Term subject = `g(f(a()),b());
    Strategy s = `All((R2()));
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("All(R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),c()));
    } catch (tom.library.sl.FireException e) {
      fail("All(R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
    /* second application should fail */
    subject = resJ;
    try {
      resJ = (Term) s.visit(subject);
      fail("All(R2).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
      resJ = null;
    }
    try {
      resS = (Term) s.fire(subject);
      fail("All(R2).fire should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
      resS = null;
    }
    assertNull(resS);
    assertNull(resJ);
  }

  public void testAllFail() {
    Term subject = `g(f(b()),b());
    Strategy s = `All((R2()));
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("All(R2).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
    }
    try {
      resS = (Term) s.fire(subject);
      fail("All(R2).fire should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
    }
    assertNull(resS);
    assertNull(resJ);
  }

  public void testOmega0() {
    Term subject = `f(a());
    Strategy s = `Omega(0,R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      /* Omega(0,R) == Identity */
      assertEquals("Applied a rule",resJ,`f(a()));
    } catch (jjtraveler.VisitFailure e) {
      fail("Omega(0,R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      /* Omega(0,R) == Identity */
      assertEquals("Applied a rule",resJ,`f(a()));
    } catch (tom.library.sl.FireException e) {
      fail("Omega(0,R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testOmega1() {
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(1,R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("Omega(1,R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),b()));
    } catch (tom.library.sl.FireException e) {
      fail("Omega(1,R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testOmega2() {
    Term subject = `g(f(a()),b());
    Strategy s = `Omega(2,R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(a()),c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("Omega(2,R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(a()),c()));
    } catch (tom.library.sl.FireException e) {
      fail("Omega(2,R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testOmega1Fail() {
    Term subject = `g(f(b()),b());
    Strategy s = `Omega(1,R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("Omega(1,R2).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
    }
    try {
      resS = (Term) s.fire(subject);
      fail("Omega(1,R2).visit should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testMake_a() {
    Term subject = `f(c());
    Strategy s = `Make_a();
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`a());
    } catch (jjtraveler.VisitFailure e) {
      fail("IfThenElse.visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`a());
    } catch (tom.library.sl.FireException e) {
      fail("IfThenElse.fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testMake_gfab() {
    Term subject = `f(c());
    Strategy s = `Make_g(Make_f(Make_a()),Make_b());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`g(f(a()),b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("IfThenElse.visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(a()),b()));
    } catch (tom.library.sl.FireException e) {
      fail("IfThenElse.fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testIfThenElse1() {
    Term subject = `f(a());
    Strategy s = `IfThenElse(R2(),Make_a(),Make_b());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`a());
    } catch (jjtraveler.VisitFailure e) {
      fail("IfThenElse.visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`a());
    } catch (tom.library.sl.FireException e) {
      fail("IfThenElse.fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testIfThenElse2() {
    Term subject = `f(b());
    Strategy s = `IfThenElse(R2(),Make_a(),Make_b());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`b());
    } catch (jjtraveler.VisitFailure e) {
      fail("IfThenElse.visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`b());
    } catch (tom.library.sl.FireException e) {
      fail("IfThenElse.fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testSequence() {
    Term subject = `f(a());
    Strategy s = `Sequence(R2(),R3());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied sequence",resJ,`f(c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("Sequence(R2,R3).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(c()));
    } catch (tom.library.sl.FireException e) {
      fail("Sequence(R2,R3).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testSequenceFail1() {
    Term subject = `f(c());
    Strategy s = `Sequence(R2(),Identity());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("Sequence(R2,id).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
    }
    try {
      resS = (Term) s.fire(subject);
      fail("Sequence(R2,id).visit should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testSequenceFail2() {
    Term subject = `f(c());
    Strategy s = `Sequence(Identity(),R3());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("Sequence(Identity,R3).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
    }
    try {
      resS = (Term) s.fire(subject);
      fail("Sequence(Identity,R3).visit should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testWhen() {
    Term subject = `f(a());
    Strategy s = `When_f(R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied sequence",resJ,`f(b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("When_f(R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (tom.library.sl.FireException e) {
      fail("When_f(R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testWhenFail() {
    Term subject = `f(b());
    Strategy s = `When_f(R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("When_f(R2).visit should fail on "+subject);
    } catch (jjtraveler.VisitFailure e) {
    }
    try {
      resS = (Term) s.fire(subject);
      fail("When_f(R2).visit should fail on "+subject);
    } catch (tom.library.sl.FireException e) {
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testCongruence() {
    Term subject = `g(f(a()),b());
    Strategy s = `_g(R1(),R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied congruence",resJ,`g(f(b()),c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("_g(R1,R2).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`g(f(b()),c()));
    } catch (tom.library.sl.FireException e) {
      fail("_g(R1,R2).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testNotR2() {
    Term subject = `f(b());
    Strategy s = `Not(R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (jjtraveler.VisitFailure e) {
      fail("Not(R2).visit should not fail on f(b)");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied a rule",resJ,`f(b()));
    } catch (tom.library.sl.FireException e) {
      fail("Not(R2).fire should not fail on f(b)");
    }
    assertEquals(resJ,resS);
  }

  public void testNotR2Fail() {
    Term subject = `f(a());
    Strategy s = `Not(R2());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      fail("Not(R2).visit should fail on f(a)");
    } catch (jjtraveler.VisitFailure e) {
      assertNull(resJ);
    }
    try {
      resS = (Term) s.fire(subject);
      fail("Not(R2).fire should fail on f(a)");
    } catch (tom.library.sl.FireException e) {
      assertNull(resS);
    }
    assertNull(resJ);
    assertNull(resS);
  }

  public void testSequenceId() {
    Term subject = `f(a());
    Strategy s = `SequenceId(R1(),R4());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals("Applied SequenceId",resJ,`f(c()));
    } catch (jjtraveler.VisitFailure e) {
      fail("SequenceId(R1,R4).visit should not fail on "+subject);
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals("Applied SequenceId",resJ,`f(c()));
    } catch (tom.library.sl.FireException e) {
      fail("SequenceId(R1,R4).fire should not fail on "+subject);
    }
    assertEquals(resJ,resS);
  }

  public void testSequenceIdFail1() {
    Term subject = `f(c());
    Strategy s = `SequenceId(R1(),Identity());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visit(subject);
      assertEquals(resJ,subject);
    } catch (jjtraveler.VisitFailure e) {
      fail("SequenceId(R1,id).visit should never throw failure");
    }
    try {
      resS = (Term) s.fire(subject);
      assertEquals(resS,subject);
    } catch (tom.library.sl.FireException e) {
      fail("SequenceId(R1,id).visit should never throw failure");
    }
    assertEquals(resJ,resS);
  }
}
