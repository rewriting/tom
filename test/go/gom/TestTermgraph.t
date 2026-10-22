package gom

/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

// Port of test/gom/TestTermgraph.t.
// port: unsupported testExpand, testExpandCycle, testGraphRules1,
// testGraphRules2, testGraphRules3, testGraphRules4, testGraphRules51,
// testGraphRules52, testGraphRules6, testGraphRules7, testGraphRules8,
// testGraphRules9: they need Gom's --termgraph option (the term-graph
// operators LabTerm, RefTerm, PathTerm, the expand() method, and the
// graphrules hooks compiled into term-graph rewriting strategies with
// their position arithmetic), which gomgo does not implement (it rejects
// --termgraph and graphrules hooks). The %gom block and the tests are
// kept below in comments.
// port: the Java Gom static methods Term.Test(), Term.rulef(), … would be
// TermTest(), TermRulef(), …; Position.makeFromArray(new int[]{…}) is
// sl.MakePositionFromArray([]int{…}); Java's visit throws VisitFailure,
// the Go Visit returns it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "tom/library/sl"
)

  %include{sl.tom}

  //%gom(--termgraph) {
  //  module m
  //    imports String
  //    abstract syntax
  //    Term = a()
  //    | b()
  //    | c()
  //    | f(arg1:Term)
  //    | k(arg1:Term)
  //    | g(arg1:Term, arg2:Term)
  //    | h(arg:Term2)
  //    
  //    Term2 = d()
  //          | e()
  //    
  //  sort Term: graphrules(rulek,Identity) {
  //    k(x) -> x
  //  }

  //  sort Term: graphrules(rulef,Identity) {
  //    f(x) -> x
  //  }

  //  sort Term: graphrules(Test,Identity) {
  //    g(l:a(),&l) -> f(b())
  //    f(g(g(a(),&l),l:b())) -> f(c())
  //    g(x,y) -> f(x)
  //    h(x) -> f(h(x))
  //  }

  //  sort Term: graphrules(Test2,Identity) {
  //    g(x,y) -> f(y)
  //  }
  // 
  //  sort Term: graphrules(TestSideEffect,Identity) {
  //    f(l:a()) -> g(&l,l:b())
  // }

  //  sort Term2: graphrules(TestMultiSort,Identity) {
  //    d() -> e()
  //  }

  //}

  //func TestExpand(t *testing.T) {
  //  var subject Term = `g(g(g(LabTerm("a1",a()),LabTerm("g",g(RefTerm("a1"),RefTerm("a2")))),LabTerm("a2",a())),RefTerm("g"))
  //  var expanded Term = `g(g(g(a(),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,2))
  //  assertEquals(t, expanded,subject.Expand())
  //}

  //func TestExpandCycle(t *testing.T) {
  //  var subject Term = `LabTerm("l",f(RefTerm("l")))
  //  var expanded Term = `f(PathTerm(-1))
  //  assertEquals(t, expanded,subject.Expand())
  //}

  //func TestGraphRules1(t *testing.T) {
  //  var t1 Term = `g(RefTerm("a"),LabTerm("a",a())).Expand().(Term)
  //  if r, e := TermTest().Visit(t1, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t,
  //        `f(b()),
  //        r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules2(t *testing.T) {
  //  var t2 Term = `f(g(g(a(),RefTerm("b")),LabTerm("b",b()))).Expand().(Term)
  //  if r, e := TermTest().Visit(t2, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t,
  //        `f(c()),
  //        r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules3(t *testing.T) {
  //  var t3 Term = `g(g(g(a(),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,2))
  //  if r, e := sl.MakePositionFromArray([]int{1,1}).GetOmega(TermTest()).Visit(t3, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t,
  //        `g(g(f(a()),a()),g(PathTerm(-1,-2,1,1,1),PathTerm(-2,-2,1,2))),
  //        r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules4(t *testing.T) {
  //  var t4 Term = `g(g(g(f(a()),g(PathTerm(-1,-2,1),a())),PathTerm(-2,1,2,2)),PathTerm(-2,1,1,1,1))
  //  if r, e := sl.MakePositionFromArray([]int{1,1}).GetOmega(TermTest()).Visit(t4, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t,
  //        `g(g(f(f(a())),a()),PathTerm(-2,1,1,1,1)),
  //        r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules51(t *testing.T) {
  //  var fk Term = `f(k(PathTerm(-1,-1)))
  //  if r, e := TermRulef().Visit(fk, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t, `k(PathTerm(-1)),r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules52(t *testing.T) {
  //  var fk Term = `f(k(PathTerm(-1,-1)))
  //  if r, e := sl.MakePositionFromArray([]int{1}).GetOmega(TermRulek()).Visit(fk, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(t, `f(PathTerm(-1)),r)
  //  } else {
  //    fail(t)
  //  }
  //}

  //func TestGraphRules6(tt *testing.T) {
  //  var t Term = `g(f(a()),PathTerm(-2,1,1))
  //  if r, e := sl.MakePositionFromArray([]int{1}).GetOmega(TermTestSideEffect()).Visit(t, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(tt, `g(g(b(),PathTerm(-2,1)),PathTerm(-2,1,1)),r)
  //  } else {
  //    fail(tt)
  //  }
  //}

  /* collapse rule */
  //func TestGraphRules7(tt *testing.T) {
  //  var t Term = `f(PathTerm(-1))
  //  if r, e := TermRulef().Visit(t, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(tt, `PathTerm(),r)
  //  } else {
  //    fail(tt)
  //  }
  //}

  //func TestGraphRules8(tt *testing.T) {
  //  var t Term = `g(a(),PathTerm(-2,1))
  //  if r, e := TermTest2().Visit(t, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(tt, `f(a()),r)
  //  } else {
  //    fail(tt)
  //  }
  //}

  /* test for subsitution in multi-sort */
  //func TestGraphRules9(tt *testing.T) {
  //  var t Term = `g(h(d()),h(PathTerm2(-1,-2,1,1)))
  //  if r, e := `TopDown(Term2TestMultiSort()).Visit(t, sl.VisitableIntrospector{}); e == nil {
  //    assertEquals(tt, `g(h(e()),h(PathTerm2(-1,-2,1,1))),r)
  //  } else {
  //    fail(tt)
  //  }
  //}

// org.junit.Assert.assertEquals(expected, actual)
//func assertEquals(t *testing.T, expected, actual any) {
//  t.Helper()
//  if expected != actual {
//    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
//  }
//}

// org.junit.Assert.fail()
//func fail(t *testing.T) {
//  t.Helper()
//  t.Fatal()
//}
