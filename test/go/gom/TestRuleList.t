package gom

/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
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

// Port of test/gom/TestRuleList.t.
// port: `import gom.rulelist.types.*;` is the dot import of the Go
// package that gomgo generates from RuleList.gom (sub-directory
// rulelist); its rules are installed by the included mapping.
// port: assertNotSame compares with == (pointer identity, as Java's ==).
// port: testf1, testconc1, … are TestF1, TestConc1, …: go test only runs
// functions whose name continues with an upper case after Test.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  . "fixture/rulelist"
)

  %include { rulelist/RuleList.tom }

func TestF1(t *testing.T) {
    assertEquals(t, `f(b()),`f(a()))
}

func TestConc1(t *testing.T) {
    assertEquals(t, `conc(a(),c()),`conc(a(),c()))
}

func TestConcsingle(t *testing.T) {
    assertEquals(t, `conc(b()),`conc(a()))
}

func TestFlat1(t *testing.T) {
    assertEquals(t, `conc(a(),b()),`conc(a(),a()))
}

func TestFlat2(t *testing.T) {
    assertNotSame(t, `conc(b(),b()),`conc(a(),a()))
}

func TestFlat3(t *testing.T) {
    assertEquals(t, `conc(b(),b()),`conc(b(),a()))
}

func TestFlat4(t *testing.T) {
    assertEquals(t, `conc(f(c())),`conc(f(a())))
}

func TestFlat5(t *testing.T) {
    assertEquals(t, `conc(f(c()),b()),`conc(f(a()),a()))
}

func Test2(t *testing.T) {
    assertEquals(t, `conc(f(c())),`conc(f(b())))
}

func Test3(t *testing.T) {
    assertEquals(t, `conc(f(c()),f(c())),`conc(f(b()),f(b())))
}

func Test4(t *testing.T) {
    assertEquals(t, `conc(f(c()),f(c())),`conc(f(a()),f(a())))
}

func Test5(t *testing.T) {
    assertEquals(t, `conc(a(),f(c()),a(),f(c()),b()),`conc(a(),f(a()),a(),f(a()),a()))
}

func Test6(t *testing.T) {
    assertEquals(t, `conc(a(),f(c())),`conc(a(),f(b())))
}

func Test7(t *testing.T) {
    // tricky test: a() there because there is no reduction
    assertEquals(t, `conc(a(),f(c()),a(),f(c()),b()),`conc(a(),f(a()),a(),f(c()),a()))
}

func Test31(t *testing.T) {
    assertEquals(t, `g(conc(f(d()))),`g(conc(f(a()))))
    // Note: we have a b() be because C1* can be reduced when rewriting f(b()) into f(c())
}

func Test32(t *testing.T) {
    assertEquals(t, `g(conc(b(),f(d()))),`g(conc(a(),f(a()))))
}

func Test33(t *testing.T) {
    assertEquals(t, `g(conc(f(d()),f(d()))),`g(conc(f(a()),f(a()))))
}

func Test34(t *testing.T) {
    //tricky test: b() is the middle should really be there!
    assertEquals(t, `g(conc(b(),f(d()),b(),f(d()),b())),`g(conc(a(),f(a()),a(),f(a()),a())))
}

func TestUnderscore_1(t *testing.T) {
    assertEquals(t, `a(),`h(b()))
}

func TestUnderscore_2(t *testing.T) {
    assertEquals(t, `a(),`k(b(),a(),b(),b()))
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.assertNotSame(unexpected, actual)
func assertNotSame(t *testing.T, unexpected, actual any) {
  t.Helper()
  if unexpected == actual {
    t.Fatalf("expected not same")
  }
}
