/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testtermgraph.m.types.*;
import gom.testtermgraph.m.*;
import tom.library.sl.*;

public class TestTermgraph {

  %include{sl.tom}

  %gom(--termgraph) {
    module m
      imports String
      abstract syntax
      Term = a()
      | b()
      | c()
      | f(arg1:Term)
      | k(arg1:Term)
      | g(arg1:Term, arg2:Term)
      | h(arg:Term2)
      
      Term2 = d()
            | e()
      
    sort Term: graphrules(rulek,Identity) {
      k(x) -> x
    }

    sort Term: graphrules(rulef,Identity) {
      f(x) -> x
    }

    sort Term: graphrules(Test,Identity) {
      g(l:a(),&l) -> f(b())
      f(g(g(a(),&l),l:b())) -> f(c())
      g(x,y) -> f(x)
      h(x) -> f(h(x))
    }

    sort Term: graphrules(Test2,Identity) {
      g(x,y) -> f(y)
    }
   
    sort Term: graphrules(TestSideEffect,Identity) {
      f(l:a()) -> g(&l,l:b())
   }

    sort Term2: graphrules(TestMultiSort,Identity) {
      d() -> e()
    }

  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestTermgraph.class.getName());
  }

  @Test
  public void testExpand() {
    Term subject = `g(g(g(LabTerm("a1",a()),LabTerm("g",g(RefTerm("a1"),RefTerm("a2")))),LabTerm("a2",a())),RefTerm("g"));
    Term expanded = `g(g(g(a(),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,2));
    assertEquals(expanded,subject.expand());
  }

  @Test
  public void testExpandCycle() {
    Term subject = `LabTerm("l",f(RefTerm("l")));
    Term expanded = `f(PathTerm(-1));
    assertEquals(expanded,subject.expand());
  }

  @Test
  public void testGraphRules1() {
    Term t1 = (Term) `g(RefTerm("a"),LabTerm("a",a())).expand();
    try {
      assertEquals(
          `f(b()),
          Term.Test().visit(t1));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules2() {
    Term t2 = (Term) `f(g(g(a(),RefTerm("b")),LabTerm("b",b()))).expand();
    try {
      assertEquals(
          `f(c()),
          Term.Test().visit(t2));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules3() {
    Term t3 = `g(g(g(a(),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,2));
    try {
      assertEquals(
          `g(g(f(a()),a()),g(PathTerm(-1,-2,1,1,1),PathTerm(-2,-2,1,2))),
          Position.makeFromArray(new int[]{1,1}).getOmega(Term.Test()).visit(t3));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules4() {
    Term t4 = `g(g(g(f(a()),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,1,1));
    try {
      assertEquals(
          `g(g(f(f(a())),a()),PathTerm(-2,1,1,1,1)),
          Position.makeFromArray(new int[]{1,1}).getOmega(Term.Test()).visit(t4));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules51() {
    Term fk = `f(k(PathTerm(-1,-1)));
    try {
      assertEquals(`k(PathTerm(-1)),Term.rulef().visit(fk));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules52() {
    Term fk = `f(k(PathTerm(-1,-1)));
    try {
      assertEquals(`f(PathTerm(-1)),Position.makeFromArray(new int[]{1}).getOmega(Term.rulek()).visit(fk));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules6() {
    Term t = `g(f(a()),PathTerm(-2,1,1));
    try {
      assertEquals(`g(g(b(),PathTerm(-2,1)),PathTerm(-2,1,1)),Position.makeFromArray(new int[]{1}).getOmega(Term.TestSideEffect()).visit(t));
    } catch(VisitFailure e) {
      fail();
    }
  }

  /* collapse rule */
  @Test
  public void testGraphRules7() {
    Term t = `f(PathTerm(-1));
    try {
      assertEquals(`PathTerm(),Term.rulef().visit(t));
    } catch(VisitFailure e) {
      fail();
    }
  }

  @Test
  public void testGraphRules8() {
  Term t = `g(a(),PathTerm(-2,1));
    try {
      assertEquals(`f(a()),Term.Test2().visit(t));
    } catch(VisitFailure e) {
      fail();
    }
  }

  /* test for subsitution in multi-sort */
  @Test
  public void testGraphRules9() {
  Term t = `g(h(d()),h(PathTerm2(-1,-2,1,1)));
    try {
      assertEquals(`g(h(e()),h(PathTerm2(-1,-2,1,1))),`TopDown(Term2.TestMultiSort()).visit(t));
    } catch(VisitFailure e) {
      fail();
    }
  }

}
