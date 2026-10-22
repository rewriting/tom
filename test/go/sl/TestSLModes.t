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

// Port of test/sl/TestSLModes.t.
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom (sub-directory testsl of the test
// module); tom.library.sl.Strategy is sl.Strategy.
// port: Java's visit/visitLight throw VisitFailure; the Go Visit and
// VisitLight return it as an error: each
//   try { res = s.visit(subject); … } catch (VisitFailure e) { … }
// becomes
//   if r, err := s.Visit(subject, …); err == nil { res = r.(Term); … } else { … }
// (res is only assigned on success, as in Java).
// port: string concatenation with a term ("…"+subject) is fmt.Sprint.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  . "fixture/testsl"
  "tom/library/sl"
)

  %include { sl.tom }
  %include { testsl/testsl.tom }
  //%include { testsl/_testsl.tom }

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

  func TestIdentity(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Identity()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Identity do not change subject",subject,resJ)
    } else {
      fail(t, "Identity.visit should not fail")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Identity do not change subject",subject,resS)
    } else {
      fail(t, "Identity.fire should not fail")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR1(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `R1()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`f(b()))
    } else {
      fail(t, "R1.visit should not fail on f(a)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(b()))
    } else {
      fail(t, "R1.fire should not fail on f(a)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR1Fail(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `R1()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule : fail is identity",resJ,subject)
    } else {
      fail(t, "R1.visit should not throw failure on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,subject)
    } else {
      fail(t, "R1.fire should not throw failure on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR2(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `R2()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`f(b()))
    } else {
      fail(t, "R2.visit should not fail on f(a)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(b()))
    } else {
      fail(t, "R2.fire should not fail on f(a)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR2Fail(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `R2()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "R2.visit should fail on f(b)")
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "R2.fire should fail on f(b)")
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOne(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `One(R1())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`f(c()))
    } else {
      fail(t, "One(R1).visit should not fail on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(c()))
    } else {
      fail(t, "One(R1).fire should not fail on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestOneFail(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `One(R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "One(R2).visit should fail on f(a)")
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "One(R2).fire should fail on f(a)")
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOnceBottomUp(t *testing.T) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `OnceBottomUp((R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(b()),b()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(b()),b()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    /* second application */
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(c()),b()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(c()),b()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    /* third application */
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(c()),c()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(c()),c()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    /* fourth application: it fails */
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "OnceBottomUp(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      resJ = nil
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "OnceBottomUp(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      resS = nil
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestAll(t *testing.T) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `All((R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(b()),c()))
    } else {
      fail(t, "All(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(b()),c()))
    } else {
      fail(t, "All(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    /* second application should fail */
    subject = resJ
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "All(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      resJ = nil
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "All(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      resS = nil
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestAllFail(t *testing.T) {
    var subject Term = `g(f(b()),b())
    var s sl.Strategy = `All((R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "All(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "All(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestOmega0(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Omega(0,R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`f(b()))
    } else {
      fail(t, "Omega(0,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(b()))
    } else {
      fail(t, "Omega(0,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega1(t *testing.T) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Omega(1,R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(b()),b()))
    } else {
      fail(t, "Omega(1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(b()),b()))
    } else {
      fail(t, "Omega(1,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega2(t *testing.T) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Omega(2,R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(a()),c()))
    } else {
      fail(t, "Omega(2,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(a()),c()))
    } else {
      fail(t, "Omega(2,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega1Fail(t *testing.T) {
    var subject Term = `g(f(b()),b())
    var s sl.Strategy = `Omega(1,R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Omega(1,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Omega(1,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOmegaAritOverflow(t *testing.T) {
    var subject Term = `g(f(b()),b())
    var s sl.Strategy = `Omega(3,R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Omega(3,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Omega(3,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestMake_a(t *testing.T) {
    var subject Term = `f(c())
    var s sl.Strategy = `Make_a()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`a())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`a())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestMake_gfab(t *testing.T) {
    var subject Term = `f(c())
    var s sl.Strategy = `Make_g(Make_f(Make_a()),Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`g(f(a()),b()))
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(a()),b()))
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestIfThenElse1(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `IfThenElse(R2(),Make_a(),Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`a())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`a())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestIfThenElse2(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `IfThenElse(R2(),Make_a(),Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`b())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`b())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequence(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Sequence(R2(),R3())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied sequence",resJ,`f(c()))
    } else {
      fail(t, "Sequence(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(c()))
    } else {
      fail(t, "Sequence(R2,R3).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequenceFail1(t *testing.T) {
    var subject Term = `f(c())
    var s sl.Strategy = `Sequence(R2(),Identity())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Sequence(R2,id).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Sequence(R2,id).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestSequenceFail2(t *testing.T) {
    var subject Term = `f(c())
    var s sl.Strategy = `Sequence(Identity(),R3())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Sequence(Identity,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Sequence(Identity,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestWhen(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `When_f(R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied sequence",resJ,`f(b()))
    } else {
      fail(t, "When_f(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(b()))
    } else {
      fail(t, "When_f(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestWhenFail(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `When_f(R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "When_f(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "When_f(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestCongruence(t *testing.T) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `_g(R1(),R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied congruence",resJ,`g(f(b()),c()))
    } else {
      fail(t, "_g(R1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`g(f(b()),c()))
    } else {
      fail(t, "_g(R1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestNotR2(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `Not(R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,`f(b()))
    } else {
      fail(t, "Not(R2).visit should not fail on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,`f(b()))
    } else {
      fail(t, "Not(R2).fire should not fail on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestNotR2Fail(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Not(R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Not(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Not(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestSequenceId(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `SequenceId(R1(),R4())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied SequenceId",resJ,`f(c()))
    } else {
      fail(t, "SequenceId(R1,R4).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied SequenceId",resS,`f(c()))
    } else {
      fail(t, "SequenceId(R1,R4).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequenceIdFail1(t *testing.T) {
    var subject Term = `f(c())
    var s sl.Strategy = `SequenceId(R1(),Identity())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEquals(t, resJ,subject)
    } else {
      fail(t, "SequenceId(R1,id).visit should never throw failure")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEquals(t, resS,subject)
    } else {
      fail(t, "SequenceId(R1,id).visit should never throw failure")
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoice1(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Choice(R2(),R3())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choice",resJ,`f(b()))
    } else {
      fail(t, "Choice(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choice",resS,`f(b()))
    } else {
      fail(t, "Choice(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoice2(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Choice(R3(),R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choice",resJ,`f(b()))
    } else {
      fail(t, "Choice(R3,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choice",resS,`f(b()))
    } else {
      fail(t, "Choice(R3,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceFail(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `Choice(R3(),R3())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Choice(R3,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Choice(R3,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestChoiceId1(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `ChoiceId(R2(),R3())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resJ,`f(b()))
    } else {
      fail(t, "ChoiceId(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resS,`f(b()))
    } else {
      fail(t, "ChoiceId(R2,R3).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceId2(t *testing.T) {
    var subject Term = `f(a())
    var s sl.Strategy = `ChoiceId(Identity(),R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resJ,`f(b()))
    } else {
      fail(t, "ChoiceId(Identity,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resS,`f(b()))
    } else {
      fail(t, "ChoiceId(Identity,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceIdFail1(t *testing.T) {
    var subject Term = `f(b())
    var s sl.Strategy = `ChoiceId(R2(),R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "ChoiceId(R2,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "ChoiceId(R2,R2).fire should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }


// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  assertEqualsMsg(t, "", expected, actual)
}

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.assertNull(object)
func assertNull(t *testing.T, object Term) {
  t.Helper()
  if object != nil {
    t.Errorf("expected null, but was:<%v>", object)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
