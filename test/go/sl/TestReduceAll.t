package sl

/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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

// Port of test/sl/TestReduceAll.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import sl.testreduceall.reduce.types.*;`.
// port: tom.library.sl.Strategy is sl.Strategy; visit/visitLight return
// the VisitFailure as an error, which fails the test (the original
// declares `throws VisitFailure`): see the helpers at the end.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "tom/library/sl"
)

  %include { sl.tom }
  %gom(--withCongruenceStrategies) {
    module reduce
    abstract syntax
    Term = f(lhs:Term,rhs:Term)
         | g(arg:Term)
         | a()
         | b()
         | c()
    f:make(x,y) {
      %match(x,y) {
        b(), b() -> { return `a(); }
      }
    }
  }
  //%include { testreduceall/reduce/_reduce.tom }

  %strategy AB() extends `Fail() {
    visit Term {
      a() -> { return `b(); }
      b() -> { return `c(); }
    }
  }

func TestOneRed(t *testing.T) {
    var subject Term = `f(a(),b())
    var s sl.Strategy = `One(AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,`a())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,`a())
    assertEquals(t, resJ,resS)
}

func TestAllRed_Visit(t *testing.T) {
    var subject Term = `f(a(),b())
    var s sl.Strategy = `All(AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,`f(b(),c()))
}

func TestAllRed_Fire(t *testing.T) {
    var subject Term = `f(a(),b())
    var s sl.Strategy = `All(AB())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,`f(b(),c()))
}

func TestCongruenceRed_Visit(t *testing.T) {
    var subject Term = `f(a(),b())
    var s sl.Strategy = `_f(AB(),AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,`f(b(),c()))
}

func TestCongruenceRed_Fire(t *testing.T) {
    var subject Term = `f(a(),b())
    var s sl.Strategy = `_f(AB(),AB())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,`f(b(),c()))
}

  %strategy G() extends `Fail() {
    visit Term {
      a() -> { return `b(); }
      g(a()) -> { return `b(); }
      g(f(c(),c())) -> { return `c(); }
    }
  }

func TestOutermost(t *testing.T) {
    var subject Term = `g(f(a(),a()))
    var s sl.Strategy = `Outermost(G())
    var res1 Term = visit(t, s, subject).(Term)
    assertEquals(t, res1,`b())
    var res2 Term = visitLight(t, s, subject).(Term)
    assertEquals(t, res2,`b())
}

// tom.library.sl.Strategy.visitLight(t), whose VisitFailure the test
// method lets through (the JUnit test then fails).
func visitLight(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}

// tom.library.sl.Strategy.visit(t), same failure handling.
func visit(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
