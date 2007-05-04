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

import sl.testreduceall.reduce.types.*;
import tom.library.sl.Strategy;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestReduceAll extends TestCase {

  %include { sl.tom }
  %gom {
    module reduce
    abstract syntax
    Term = f(lhs:Term,rhs:Term)
         | a() | b() | c()
    f:make(x,y) {
      %match(x,y) {
        b(), b() -> { return `a(); }
      }
    }
  }
  %include { testreduceall/reduce/_reduce.tom }

  %strategy AB() extends `Fail() {
    visit Term {
      a() -> { return `b(); }
      b() -> { return `c(); }
    }
  }

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestReduceAll.class));
	}

  public void testOneRed() {
    Term subject = `f(a(),b());
    Strategy s = `One(AB());
    Term resJ = null;
    Term resS = null;
    try {
      resJ = (Term) s.visitLight(subject);
      assertEquals(resJ,`a());
    } catch (tom.library.sl.VisitFailure e) {
      fail("Identity.visit should not fail");
    }
    try {
      resS = (Term) s.visit(subject);
      assertEquals(resS,`a());
    } catch (tom.library.sl.VisitFailure e) {
    }
    assertEquals(resJ,resS);
  }

  public void testAllRed_Visit() {
    Term subject = `f(a(),b());
    Strategy s = `All(AB());
    Term resJ = null;
    try {
      resJ = (Term) s.visitLight(subject);
      assertEquals(resJ,`f(b(),c()));
    } catch (tom.library.sl.VisitFailure e) {
      fail("Visit should not fail");
    }
  }

  public void testAllRed_Fire() {
    Term subject = `f(a(),b());
    Strategy s = `All(AB());
    Term resS = null;
    try {
      resS = (Term) s.visit(subject);
      assertEquals(resS,`f(b(),c()));
    } catch (tom.library.sl.VisitFailure e) {
      fail("Fire should not fail");
    }
  }

  public void testCongruenceRed_Visit() {
    Term subject = `f(a(),b());
    Strategy s = `_f(AB(),AB());
    Term resJ = null;
    try {
      resJ = (Term) s.visitLight(subject);
      assertEquals(resJ,`f(b(),c()));
    } catch (tom.library.sl.VisitFailure e) {
      fail("Visit should not fail");
    }
  }

  public void testCongruenceRed_Fire() {
    Term subject = `f(a(),b());
    Strategy s = `_f(AB(),AB());
    Term resS = null;
    try {
      resS = (Term) s.visit(subject);
      assertEquals(resS,`f(b(),c()));
    } catch (tom.library.sl.VisitFailure e) {
      fail("Fire should not fail");
    }
  }


}
