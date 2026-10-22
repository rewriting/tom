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

// Port of test/sl/TestSL.t, with its support files sl/OldBehaviour.t,
// sl/NewBehaviour.t and sl/IntrospectorBehaviour.t (each a Go package of
// its own, see their ports; IntrospectorBehaviour is compiled with
// --genIntrospector, as test/build.xml does).
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom (sub-directory testsl).
// port: the generated code of the testsl mapping (its congruence
// strategies) uses package sl, imported here (Java names it in full).
// port: the fields nb, ob, ib are package variables (the behaviours have
// no state).
// port: a behaviour method returns (Term, error) where the original
// throws VisitFailure; must(t) fails the test on that error, as the
// exception escaping the JUnit test does.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "fixture/introspectorbehaviour"
  "fixture/newbehaviour"
  "fixture/oldbehaviour"
  . "fixture/testsl"
  "tom/library/sl"
)

  %include { testsl/testsl.tom }

  var nb *newbehaviour.NewBehaviour = &newbehaviour.NewBehaviour{}
  var ob *oldbehaviour.OldBehaviour = &oldbehaviour.OldBehaviour{}
  var ib *introspectorbehaviour.IntrospectorBehaviour = &introspectorbehaviour.IntrospectorBehaviour{}

func TestSL1(t *testing.T) {    
    var result Term = `f(a())
    assertEquals(t, result, must(t)(ob.Test1()))
    assertEquals(t, result, must(t)(nb.Test1()))
    assertEquals(t, result, must(t)(ib.Test1()))
  }
func TestSL2(t *testing.T) {    
    var result Term = `f(b())
    assertEquals(t, result, must(t)(ob.Test2()))
    assertEquals(t, result, must(t)(nb.Test2()))
    assertEquals(t, result, must(t)(ib.Test2()))
  }
func TestSL3(t *testing.T) {    
    var result Term = `f(a())
    assertEquals(t, result, must(t)(ob.Test3()))
    assertEquals(t, result, must(t)(nb.Test3()))
    assertEquals(t, result, must(t)(ib.Test3()))
  }
func TestSL4(t *testing.T) {    
    var result Term = `g(f(c()),c())
    assertEquals(t, result, must(t)(ob.Test4()))
    assertEquals(t, result, must(t)(nb.Test4()))
    assertEquals(t, result, must(t)(ib.Test4()))
  }
func TestSL5(t *testing.T) {    
    var result Term = `g(f(b()),c()) 
    assertEquals(t, result, must(t)(ob.Test5()))
    assertEquals(t, result, must(t)(nb.Test5()))
    assertEquals(t, result, must(t)(ib.Test5()))
  }
func TestSLChoice(t *testing.T) {   
    var result Term = `f(b()) //'Choice' does not fail even if both choices fail.
    assertEquals(t, result, must(t)(ob.TestChoice()))
    assertEquals(t, result, must(t)(nb.TestChoice()))
    assertEquals(t, result, must(t)(ib.TestChoice()))
  }
func TestSLChoiceSideEffect(t *testing.T) {   
    var result Term = `f(b()) //'Choice' does not fail even if both choices fail, but the side effect of first R1() remains.
    assertEquals(t, result, must(t)(ob.TestChoiceSideEffect()))
    assertEquals(t, result, must(t)(nb.TestChoiceSideEffect()))
    assertEquals(t, result, must(t)(ib.TestChoiceSideEffect()))
  }
func TestSLNot(t *testing.T) {    
    var result Term = `f(b())
    assertEquals(t, result, must(t)(ob.TestNot()))
    assertEquals(t, result, must(t)(nb.TestNot()))
    assertEquals(t, result, must(t)(ib.TestNot()))
  }
func TestSLNotSideEffect(t *testing.T) {    
    var result Term = `f(a())
    assertEquals(t, result, must(t)(ob.TestNotSideEffect()))
    assertEquals(t, result, must(t)(nb.TestNotSideEffect()))
    assertEquals(t, result, must(t)(ib.TestNotSideEffect()))
  }
func TestSL6(t *testing.T) {
    var result Term = `g(f(c()),c())
    assertEquals(t, result, must(t)(ob.Test6()))
    assertEquals(t, result, must(t)(nb.Test6()))
    assertEquals(t, result, must(t)(ib.Test6()))
  }
func TestSL7(t *testing.T) {    
    var result Term = `g(f(b()),b())
    assertEquals(t, result, must(t)(ob.Test7()))
    assertEquals(t, result, must(t)(nb.Test7()))
    assertEquals(t, result, must(t)(ib.Test7()))
  }
func TestSL8(t *testing.T) {    
    var result Term = `g(f(a()),c()) 
    assertEquals(t, result, must(t)(ob.Test8()))
    assertEquals(t, result, must(t)(nb.Test8()))
    assertEquals(t, result, must(t)(ib.Test8()))
  }
func TestSL9(t *testing.T) {    
    var result Term = `b() 
    assertEquals(t, result, must(t)(ob.Test9()))
    assertEquals(t, result, must(t)(nb.Test9()))
    assertEquals(t, result, must(t)(ib.Test9()))
  }
func TestITESideEffect(t *testing.T) {    
    var result Term = `f(a()) //todo?? should this not produce side effect?
    assertEquals(t, result, must(t)(ob.TestITESideEffect()))
    assertEquals(t, result, must(t)(nb.TestITESideEffect()))
    assertEquals(t, result, must(t)(ib.TestITESideEffect()))
  }
func TestSL10(t *testing.T) {   
    var result Term = `a()
    assertEquals(t, result, must(t)(ob.Test10()))
    assertEquals(t, result, must(t)(nb.Test10()))
    assertEquals(t, result, must(t)(ib.Test10()))
  }
func TestSL11(t *testing.T) {   
    var result Term = `g(f(c()),c())
    assertEquals(t, result, must(t)(ob.Test11()))
    assertEquals(t, result, must(t)(nb.Test11()))
    assertEquals(t, result, must(t)(ib.Test11()))
  }
func TestSL12(t *testing.T) {   
    var result Term = `g(f(c()),c()) //'Innermost' applies until no more success. (3 applications as in previous)
    assertEquals(t, result, must(t)(ob.Test12()))
    assertEquals(t, result, must(t)(nb.Test12()))
    assertEquals(t, result, must(t)(ib.Test12()))
  }
func TestSL13(t *testing.T) {   
    var result Term = `g(f(c()),c()) //'Outermost' applies until no more success. (3 applications as in previous)
    assertEquals(t, result, must(t)(ob.Test13()))
    assertEquals(t, result, must(t)(nb.Test13()))
    assertEquals(t, result, must(t)(ib.Test13()))
  }
func TestSLWhen1(t *testing.T) {    
    var result Term = `f(b())
    assertEquals(t, result, must(t)(ob.TestWhen1()))
    assertEquals(t, result, must(t)(nb.TestWhen1()))
    assertEquals(t, result, must(t)(ib.TestWhen1()))
  }
func TestSLCongruence1(t *testing.T) {    
    var result Term = `g(f(b()),c())
    assertEquals(t, result, must(t)(ob.TestCongruence1()))
    assertEquals(t, result, must(t)(nb.TestCongruence1()))
    assertEquals(t, result, must(t)(ib.TestCongruence1()))
  }
func TestSLCongruence2(t *testing.T) {
    // try { ob.testCongruence2(); fail(); } catch (RuntimeException e) { … }:
    // a VisitFailure or the failure of fail() escapes the test.
    if catchRuntimeException(func() {
      if _, err := ob.TestCongruence2(); err != nil {
        t.Fatal("tom.library.sl.VisitFailure()")
      }
      fail(t)
    }) {
      if _, err := nb.TestCongruence2(); err == nil {
        fail(t)
      } else {/*success*/}
    }
    if _, err := ib.TestCongruence2(); err == nil {
      fail(t)
    } else {/*success*/}
}
func TestSLCongruenceList(t *testing.T) {
    var result Term = `l(a(),c())
    assertEquals(t, result, must(t)(ob.TestCongruenceList()))
    assertEquals(t, result, must(t)(nb.TestCongruenceList()))
    assertEquals(t, result, must(t)(ib.TestCongruenceList()))
}

// must(t)(res, err): the VisitFailure of a behaviour method escapes the
// JUnit test in the original, so the test fails.
func must(t *testing.T) func(Term, error) Term {
  return func(r Term, err error) Term {
    t.Helper()
    if err != nil {
      t.Fatal("tom.library.sl.VisitFailure()")
    }
    return r
  }
}

// catchRuntimeException runs f and reports whether it threw a
// RuntimeException (a panic). t.Fatal (Java's fail()) is not a panic: it
// ends the test, as the AssertionError that catch (RuntimeException)
// does not catch.
func catchRuntimeException(f func()) (caught bool) {
  defer func() {
    if r := recover(); r != nil {
      caught = true
    }
  }()
  f()
  return false
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.fail()
func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
