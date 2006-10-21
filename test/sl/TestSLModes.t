/*
 * Copyright (c) 2004-2006, INRIA
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
}
