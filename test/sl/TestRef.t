/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

import sl.testsl.types.*;
import sl.testsl.*;
import tom.library.sl.Strategy;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestRef {

  %include { sl.tom }
  %include { sl/graph.tom }
  //%include { testsl/_testsl.tom }
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
    org.junit.runner.JUnitCore.main(TestRef.class.getName());
  }

  @Test
  public void testRef1() {
    Term subject = (Term) `g(g(a(),RefTerm("l")),LabTerm("l",a())).expand();
    try{
      Term res =  `TopDown(StrictDeRef(AB())).visit(subject);
      res =  `TopDown(DeRef(AB())).visit(subject);
      res =  `TopDownSeq(StrictDeRef(AB())).visit(subject);
      assertEquals(`g(g(a(),RefTerm("n")),LabTerm("n",b())).expand(),res);
    } catch (tom.library.sl.VisitFailure e) {
      fail("It should not fail");
    }
  }

  @Test
  public void testRef2() {
    Term subject = (Term) `g(g(a(),RefTerm("l")),LabTerm("l",a())).expand();
    try{
      Term res =  `TopDown(StrictDeRef(AB())).visit(subject);
      res =  `TopDown(DeRef(AB())).visit(subject);
      res =  `TopDownSeq(StrictDeRef(AB())).visit(subject);
      res =  `TopDownSeq(DeRef(AB())).visit(subject);
      assertEquals(`g(g(b(),RefTerm("n")),LabTerm("n",c())).expand(),res);
    } catch (tom.library.sl.VisitFailure e) {
      fail("It should not fail");
    }
  }

  @Test
  public void testRef3() {
    Term subject2 =  (Term) `g(g(a(),LabTerm("l",a())),RefTerm("l")).expand();
    try{
      Term res2 =  `TopDown(StrictDeRef(AB())).visit(subject2);
      res2 =  `TopDown(DeRef(AB())).visit(subject2);
      res2 =  `TopDownSeq(StrictDeRef(AB())).visit(subject2);
      assertEquals(`g(g(a(),LabTerm("n",b())),RefTerm("n")).expand(),res2);
    } catch (tom.library.sl.VisitFailure e) {
      fail("It should not fail");
    }
  }

  @Test
  public void testRef4() {
    Term subject2 = (Term) `g(g(a(),LabTerm("l",a())),RefTerm("l")).expand();
    try{
      Term res2 =  `TopDown(StrictDeRef(AB())).visit(subject2);
      res2 =  `TopDown(DeRef(AB())).visit(subject2);
      res2 =  `TopDownSeq(StrictDeRef(AB())).visit(subject2);
      res2 =  `TopDownSeq(DeRef(AB())).visit(subject2);
      assertEquals(`g(g(b(),LabTerm("n",c())),RefTerm("n")).expand(),res2);
    } catch (tom.library.sl.VisitFailure e) {
      fail("It should not fail");
    }
  }
}
