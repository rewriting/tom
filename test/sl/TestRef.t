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

public class TestRef extends TestCase {

  %include { sl.tom }
  %include { strategy/graph_sl.tom }
  %include { testsl/_testsl.tom }
  %include { testsl/testsl.tom }

  //TODO remove when composed will be added to sl.tom
  %op Strategy TopDown(s1:Strategy) {
    make(v) { `mu(MuVar("_x"),Sequence(v,All(MuVar("_x")))) }
  }

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
    Term subject = `expTerm(g(g(a(),refTerm("l")),labTerm("l",a())));
    try{
      Term res = (Term) `TopDown(StrictRelativeRef(AB())).fire(subject);
      assertEquals(res,`expTerm(g(g(a(),refTerm("n")),labTerm("n",b()))));
      res = (Term) `TopDown(RelativeRef(AB())).fire(subject);
      assertEquals(res,`expTerm(g(g(b(),refTerm("n")),labTerm("n",b()))));
      res = (Term) `TopDownSeq(StrictRelativeRef(AB())).fire(subject);
      assertEquals(res,`expTerm(g(g(a(),refTerm("n")),labTerm("n",b()))));
      res = (Term) `TopDownSeq(RelativeRef(AB())).fire(subject);
      assertEquals(res,`expTerm(g(g(b(),refTerm("n")),labTerm("n",c()))));
    } catch (tom.library.sl.FireException e) {
      fail("It should not fail");
    }
  }

}
