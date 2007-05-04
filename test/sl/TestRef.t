/*
 * Copyright (c) 2004-2007, INRIA
 * All rights res.
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
import sl.testsl.*;
import tom.library.sl.Strategy;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestRef extends TestCase {

  %include { sl.tom }
  %include { strategy/graph_sl.tom }
  %include { testsl/_testsl.tom }
  %include { testsl/testsl.tom }

  %op Strategy TopDownSeq(s1:Strategy) {
    make(v) { `mu(MuVar("_x"),Sequence(v,AllSeq(MuVar("_x")))) }
  }

  %strategy AB() extends `Identity() {
    visit Term {
      a() -> { return `b(); }
      b() -> { return `c(); }
    }
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestRef.class));
  }

  public void testRef() {
    Term subject = (Term) testslAbstractType.expand(`g(g(a(),refTerm("l")),labTerm("l",a())));
    Term subject2 = (Term) testslAbstractType.expand(`g(g(a(),labTerm("l",a())),refTerm("l")));
    try{
      Term res = (Term) `TopDown(StrictDeRef(AB())).visit(subject);
      //assertEquals(testslAbstractType.expand(`g(g(a(),refTerm("n")),labTerm("n",a()))),res);
      res = (Term) `TopDown(DeRef(AB())).visit(subject);
      //assertEquals(testslAbstractType.expand(`g(g(b(),refTerm("n")),labTerm("n",b()))),res);
      res = (Term) `TopDownSeq(StrictDeRef(AB())).visit(subject);
      assertEquals(testslAbstractType.expand(`g(g(a(),refTerm("n")),labTerm("n",b()))),res);
      res = (Term) `TopDownSeq(DeRef(AB())).visit(subject);
      assertEquals(testslAbstractType.expand(`g(g(b(),refTerm("n")),labTerm("n",c()))),res);
    
      Term res2 = (Term) `TopDown(StrictDeRef(AB())).visit(subject2);
      //assertEquals(testslAbstractType.expand(`g(g(a(),labTerm("n",a())),refTerm("n"))),res2);
      res2 = (Term) `TopDown(DeRef(AB())).visit(subject2);
      //assertEquals(testslAbstractType.expand(`g(g(b(),labTerm("n",b())),refTerm("n"))),res2);
      res2 = (Term) `TopDownSeq(StrictDeRef(AB())).visit(subject2);
      assertEquals(testslAbstractType.expand(`g(g(a(),labTerm("n",b())),refTerm("n"))),res2);
      res2 = (Term) `TopDownSeq(DeRef(AB())).visit(subject2);
      assertEquals(testslAbstractType.expand(`g(g(b(),labTerm("n",c())),refTerm("n"))),res2);
    } catch (tom.library.sl.VisitFailure e) {
      fail("It should not fail");
    }
  }

}
