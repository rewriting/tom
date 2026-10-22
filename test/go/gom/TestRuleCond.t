package gom

/*
 * Copyright (c) 2010-2015, Universite de Lorraine, Inria
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
 *
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */

// Port of test/gom/TestRuleCond.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testrulecond.term.types.*;`.
// port: assertSame/assertNotSame compare with == (pointer identity, as
// Java's ==).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module Term
    abstract syntax
    Term = a()
         | b()
         | c()
         | f(t:Term)
         | g(t1:Term,t2:Term)
         | h(t1:Term,t2:Term,t3:Term)
   module Term:rules() {
    g(x,y) -> a() if x == a() && y == b()
    g(x,y) -> c() if x == a() && y != b() && y != c()
    f(x) -> a() if x == a() || x == b()
    h(x,y,z) -> a() if ((x == a() && y != b()) || (y != c() && z == a()))
   }
  }

func TestRuleFalseCond(t *testing.T) {
    var test Term = `g(b(),b())
    assertEquals(t, test,`g(b(),b()))
}

func TestRuleAnd(t *testing.T) {
    var test Term = `g(a(),b())
    assertEquals(t, test,`a())
}

func TestRuleOra(t *testing.T) {
    var test Term = `f(a())
    assertEquals(t, test,`a())
}

func TestRuleOrb(t *testing.T) {
    var test Term = `f(b())
    assertEquals(t, test,`a())
}

func TestRuleOrc(t *testing.T) {
    var test Term = `f(c())
    assertEquals(t, test,`f(c()))
}

func TestRuleAndAnd(t *testing.T) {
    var test Term = `g(a(),a())
    assertEquals(t, test,`c())
}

func TestRuleParena(t *testing.T) {
    var test Term = `h(a(),a(),a())
    assertSame(t, test,`a())
}
func TestRuleParenb(t *testing.T) {
    var test Term = `h(a(),c(),c())
    assertSame(t, test,`a())
}
func TestRuleParenc(t *testing.T) {
    var test Term = `h(b(),b(),a())
    assertSame(t, test,`a())
}
func TestRuleParend(t *testing.T) {
    var test Term = `h(b(),b(),b())
    assertNotSame(t, test,`a())
}
func TestRuleParene(t *testing.T) {
    var test Term = `h(b(),a(),c())
    assertNotSame(t, test,`a())
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.assertSame(expected, actual)
func assertSame(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected same:<%v> was not:<%v>", expected, actual)
  }
}

// org.junit.Assert.assertNotSame(unexpected, actual)
func assertNotSame(t *testing.T, unexpected, actual any) {
  t.Helper()
  if unexpected == actual {
    t.Fatalf("expected not same")
  }
}
