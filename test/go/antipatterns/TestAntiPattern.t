package antipatterns

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

// Port of test/antipatterns/TestAntiPattern.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import antipatterns.testantipattern.antipattern.types.*;`.
// port: `x == y` on terms is == on the Go interfaces (identity of the
// shared terms, as in Java).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module AntiPattern
    imports String int
    abstract syntax
    Term = a()
    | b()
    | c()
    | f(x1:Term, x2:Term) 
    | g(pred:Term)
    | ff(x1:Term, x2:Term)
    | i(val:int)
    | j(val:int)

    Result = True()
    | False()
    | And(Result*)
    | Or(Result*)
    | Equal(x1:String,x2:Term)
    | NEqual(x1:String,x2:Term)
  }

// port: the empty @Before setUp() is dropped.

func match1(subject Term) Result {
    %match(Term subject){
      a() ->{
        return `True()
      }
    }
    return `False()
}

func TestAp1(t *testing.T) {
    assertTrue(t, match1(`a()) == `True())
    assertTrue(t, match1(`b()) == `False())
}

func match2(subject Term) Result {
    %match(Term subject){
      !a() ->{
        return `True()
      }
    }
    return `False()
}


func TestAp2(t *testing.T) {
    assertTrue(t, match2(`a()) == `False())
    assertTrue(t, match2(`b()) == `True())
}

func match3(subject Term) Result {
    %match(Term subject){
      f(a(),!b()) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp3(t *testing.T) {
    assertTrue(t, match3(`f(a(),a())) == `True())
    assertTrue(t, match3(`f(a(),c())) == `True())
    assertTrue(t, match3(`f(a(),b())) == `False())
    assertTrue(t, match3(`f(b(),c())) == `False())
}

func match3_1(subject Term) Result {
    %match(Term subject){
      f(!a(),!b()) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp3_1(t *testing.T) {
    assertTrue(t, match3_1(`f(a(),a())) == `False())
    assertTrue(t, match3_1(`f(a(),c())) == `False())
    assertTrue(t, match3_1(`f(a(),b())) == `False())
    assertTrue(t, match3_1(`f(b(),c())) == `True())
    assertTrue(t, match3_1(`f(b(),b())) == `False())
}

func match3_2(subject Term) Result {
    %match(Term subject){
      !f[] ->{
        return `True()
      }
    }
    return `False()
}

func TestAp3_2(t *testing.T) {
    assertTrue(t, match3_2(`f(a(),a())) == `False())
    assertTrue(t, match3_2(`a()) == `True())
}

func match3_3(subject Term) Result {
    %match(Term subject){
      !f(_,_) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp3_3(t *testing.T) {
    assertTrue(t, match3_3(`f(a(),a())) == `False())
    assertTrue(t, match3_3(`a()) == `True())
}

func match3_4(subject Term) Result {
    %match(Term subject){
      !(f|ff)(a(),!b()) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp3_4(t *testing.T) {
    assertTrue(t, match3_4(`f(a(),a())) == `False())
    assertTrue(t, match3_4(`ff(a(),a())) == `False())
    assertTrue(t, match3_4(`f(a(),b())) == `True())
    assertTrue(t, match3_4(`ff(a(),b())) == `True())
    assertTrue(t, match3_4(`f(b(),b())) == `True())
    assertTrue(t, match3_4(`ff(b(),b())) == `True())
    assertTrue(t, match3_4(`a()) == `True())
}


func match4(subject Term) Result {
    %match(Term subject){
      !f(a(),!b()) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp4(t *testing.T) {
    assertTrue(t, match4(`f(a(),a())) == `False())
    assertTrue(t, match4(`f(a(),c())) == `False())
    assertTrue(t, match4(`f(a(),b())) == `True())
    assertTrue(t, match4(`f(b(),c())) == `True())
    assertTrue(t, match4(`g(b())) == `True())
    assertTrue(t, match4(`f(b(),b())) == `True())
}

func match5(subject Term) Result {
    %match(Term subject){
      f(x,x) ->{
        return `Equal("x",x)
      }
    }
    return `False()
}

func TestAp5(t *testing.T) {
    assertTrue(t, match5(`f(a(),a())) == `Equal("x",a()))
    assertTrue(t, match5(`f(a(),b())) == `False())
}

func match7(subject Term) Result {
    %match(Term subject){
      !f(x,x) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp7(t *testing.T) {
    assertTrue(t, match7(`f(a(),a())) == `False())
    assertTrue(t, match7(`f(a(),b())) == `True())
    assertTrue(t, match7(`g(a())) == `True())
}


func match8(subject Term) Result {
    %match(Term subject){
      f(x,!g(x)) ->{
        return `Equal("x",x)
      }
    }
    return `False()
}

func TestAp8(t *testing.T) {
    assertTrue(t, match8(`f(a(),b())) == `Equal("x",a()))
    assertTrue(t, match8(`f(a(),g(b()))) == `Equal("x",a()))
    assertTrue(t, match8(`f(b(),g(b()))) == `False())
    assertTrue(t, match8(`g(b())) == `False())
}

func match8_1(subject Term) Result {
    %match(Term subject){
      f(!g(x),x) ->{
        return `Equal("x",x)
      }
    }
    return `False()
}

func TestAp8_1(t *testing.T) {
    assertTrue(t, match8_1(`f(b(),a())) == `Equal("x",a()))
    assertTrue(t, match8_1(`f(g(b()),a())) == `Equal("x",a()))
    assertTrue(t, match8_1(`f(g(b()),b())) == `False())
    assertTrue(t, match8_1(`g(b())) == `False())
}

func match9(subject Term) Result {
    %match(Term subject){
      !f(x,!g(x)) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp9(t *testing.T) {
    assertTrue(t, match9(`f(a(),b())) == `False())
    assertTrue(t, match9(`f(a(),g(b()))) == `False())
    assertTrue(t, match9(`f(b(),g(b()))) == `True())
    assertTrue(t, match9(`g(b())) == `True())
}

func match9_1(subject Term) Result {
    %match(Term subject){
      !f(_x,!g(_y)) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp9_1(t *testing.T) {
    assertTrue(t, match9_1(`f(b(),g(b()))) == `True())
    assertTrue(t, match9_1(`f(a(),g(b()))) == `True())
    assertTrue(t, match9_1(`f(a(),a())) == `False())
}

func match10(subject Term) Result {
    %match(Term subject){
      f(x,!x) ->{
        return `Equal("x",x)
      }
    }
    return `False()
}

func TestAp10(t *testing.T) {
    assertTrue(t, match10(`f(a(),a())) == `False())
    assertTrue(t, match10(`f(a(),b())) == `Equal("x",a()))
}

func match11(subject Term) Result {
    %match(Term subject){
      f(!g(x),!g(x)) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp11(t *testing.T) {
    assertTrue(t, match11(`f(g(a()),g(b()))) == `False())
}

func match12(subject Term) Result {
    %match(Term subject){
      !f(!g(x),!g(x)) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp12(t *testing.T) {
    assertTrue(t, match12(`f(g(a()),g(b()))) == `True())
}

func match13(subject Term) Result {
    %match(Term subject){
      f(!x,!x) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp13(t *testing.T) {
    assertTrue(t, match13(`f(a(),a())) == `False())
    assertTrue(t, match13(`f(a(),b())) == `False())
}

func match14(subject Term) Result {
    %match(Term subject){
      !!_x ->{
        return `True()
      }
    }
    return `False()
}

func match15(subject Term) Result {
    %match(Term subject){
      f(_x,!_y) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp15(t *testing.T) {
    assertTrue(t, match15(`f(a(),a())) == `False())
    assertTrue(t, match15(`f(a(),b())) == `False())
    assertTrue(t, match15(`b()) == `False())
}

func match16(subject Term) Result {
    %match(Term subject){
      f(x,f(a(),f(y,!g(x)))) ->{
        return `And(Equal("x",x),Equal("y",y))
      }
    }
    return `False()
}


func TestAp16(t *testing.T) {
    assertTrue(t, match16(`f(b(),f(a(),f(a(),g(b()))))) == `False())
    assertTrue(t, match16(`f(b(),f(a(),f(a(),g(a()))))) == `And(Equal("x",b()),Equal("y",a())))
    assertTrue(t, match16(`f(a(),f(a(),f(b(),b())))) == `And(Equal("x",a()),Equal("y",b())))
    assertTrue(t, match16(`f(b(),f(a(),b()))) == `False())
}

func match17(subject Term) Result {
    %match(Term subject){
      i(!1) ->{
        return `True()
      }
    }
    return `False()
}

func TestAp17(t *testing.T) {
    assertTrue(t, match17(`i(1)) == `False())
    assertTrue(t, match17(`i(2)) == `True())
}

func match18(subject Term) Result {
    %match(Term subject){
      i(x@!1) ->{
        return `Equal("j(x)",j(x))
      }
    }
    return `False()
}

func TestAp18(t *testing.T) {
    assertTrue(t, match18(`i(1)) == `False())
    assertTrue(t, match18(`i(2)) == `Equal("j(x)",j(2)))
}

func match19(subject Term) Result {
    %match(Term subject){
      ff(i(x@!1),i(x)) ->{
        return `Equal("j(x)",j(x))
      }
    }
    return `False()
}

func TestAp19(t *testing.T) {
    assertTrue(t, match19(`ff(i(1),i(2))) == `False())
    assertTrue(t, match19(`ff(i(2),i(2))) == `Equal("j(x)",j(2)))
}

func match20(subject Term) Result {
    %match(Term subject){
      ff(x@!a(),x) ->{
        return `Equal("x",x)
      }
    }
    return `False()
}

func TestAp20(t *testing.T) {
    assertTrue(t, match20(`ff(a(),a())) == `False())
    assertTrue(t, match20(`ff(a(),b())) == `False())
    assertTrue(t, match20(`ff(b(),b())) == `Equal("x",b()))
}
// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}
