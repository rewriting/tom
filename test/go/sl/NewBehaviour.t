package newbehaviour

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

// Port of test/sl/NewBehaviour.t, whose methods sl/TestSL.t calls.
// port: package newbehaviour instead of sl: two Tom-generated Go files cannot
// share a package (each defines the tom_* helpers of the mappings it
// includes), so the port harness compiles this file into its own package
// (sub-directory newbehaviour of the test module); the class becomes the
// struct NewBehaviour and its methods are exported (testFoo -> TestFoo).
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom (sub-directory testsl).
// port: a method that throws VisitFailure returns (Term, error); the
// strategies are applied with visit.

import (
  . "fixture/testsl"
  "tom/library/sl"
)

// NewBehaviour is the class of the original (no state).
type NewBehaviour struct{}

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
  %strategy Builda() extends `Identity() {
    visit Term {
      _ -> { return `a(); }
    }
  }
  %strategy Buildb() extends `Identity() {
    visit Term {
      _ -> { return `b(); }
    }
  }

  func (nb *NewBehaviour) Test1() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `Identity()
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test2() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `(R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test3() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `One(R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test4() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `OnceBottomUp((R2()))
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test5() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `All((R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestChoice() (Term, error) {
    var subject Term = `f(b())
    var s sl.Strategy = `Choice(R2(),R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestChoiceSideEffect() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `Choice(Sequence(R1(),R2()),R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestNot() (Term, error) {
    var subject Term = `f(b())
    var s sl.Strategy = `Not(R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestNotSideEffect() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `Not(Sequence(R1(),R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test6() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Sequence(All(R2()),All(All(R1())))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test7() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Omega(1,R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test8() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Omega(2,R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test9() (Term, error) {
    var subject Term = `f(b())
    var s sl.Strategy = `IfThenElse(R2(),Builda(),Buildb())
    return visit(s, subject)
  }
  
  func (nb *NewBehaviour) TestITESideEffect() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `IfThenElse(R2(),Identity(),Buildb())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test10() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `IfThenElse(R2(),Builda(),Buildb())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test11() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `OnceTopDownId((R1()))
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test12() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Innermost((R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test13() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `Outermost((R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestWhen1() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `When_f(R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruence1() (Term, error) {
    var subject Term = `g(f(a()),b())
    var s sl.Strategy = `_g(R1(),R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruence2() (Term, error) {
    var subject Term = `f(a())
    var s sl.Strategy = `_g(R1(),R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruenceList() (Term, error) {
    var subject Term = `l(a(),b())
    var s sl.Strategy = `_l(R1())
    return visit(s, subject)
  }


// s.visit(subject) of a Term: the VisitFailure is the error.
func visit(s sl.Strategy, subject Term) (Term, error) {
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    return nil, err
  }
  return r.(Term), nil
}
