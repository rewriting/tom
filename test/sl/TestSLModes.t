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
    Strategy s = `(R1());
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

}
