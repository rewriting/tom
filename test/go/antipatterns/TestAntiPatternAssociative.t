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

// Port of test/antipatterns/TestAntiPatternAssociative.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import antipatterns.testantipatternassociative.antipatternassociative.types.*;`.
// port: `x == y` on terms is == on the Go interfaces (identity of the
// shared terms, as in Java).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module AntiPatternAssociative
      imports String
      abstract syntax
      Term = 
      | a()
      | b()
      | c()
      | d()
      | f(x1:Term, x2:Term) 
      | g(pred:Term)
      | l(list:TermList)
      | lst(Term*)

      TermList = concTerm(Term*)

      Result = 
      | True()
      | False()
      | And(Result*)
      | Or(Result*)
      | Equal(x1:String,x2:Term)
      | NEqual(x1:String,x2:Term)
  }

// port: the empty @Before setUp() is dropped.

func match1(subject TermList) Result {
    %match(TermList subject) {
      concTerm(_*,a(),_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp1(t *testing.T) {
    assertTrue(t, match1(`concTerm(b(),a(),c())) == `True())
    assertTrue(t, match1(`concTerm(b(),f(a(),c()),c())) == `False())
}

func match2(subject TermList) Result {
    %match(TermList subject) {
      !concTerm(_*,a(),_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp2(t *testing.T) {
    assertTrue(t, match2(`concTerm(b(),a(),c())) == `False())
    assertTrue(t, match2(`concTerm(b(),f(a(),c()),c())) == `True())
    assertTrue(t, match2(`concTerm(b(),f(b(),c()),c())) == `True())
}

func match3(subject TermList) Result {
    %match(TermList subject) {
      concTerm(_*,!a(),_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp3(t *testing.T) {
    assertTrue(t, match3(`concTerm(b(),a(),c())) == `True())
    assertTrue(t, match3(`concTerm(b(),f(a(),c()),c())) == `True())
    assertTrue(t, match3(`concTerm(a(),f(a(),c()),a())) == `True())
    assertTrue(t, match3(`concTerm(a(),a(),c(),a())) == `True())
    assertTrue(t, match3(`concTerm(a(),a(),a(),a())) == `False())
}

func match4(subject TermList) Result {
    %match(TermList subject){
      !concTerm(_*,!a(),_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp4(t *testing.T) {
    assertTrue(t, match4(`concTerm(b(),a(),c())) == `False())
    assertTrue(t, match4(`concTerm(b(),f(a(),c()),c())) == `False())
    assertTrue(t, match4(`concTerm(a(),f(a(),c()),a())) == `False())
    assertTrue(t, match4(`concTerm(a(),a(),c(),a())) == `False())
    assertTrue(t, match4(`concTerm(a(),a(),a(),a())) == `True())
}

func match4_1(subject TermList) Result {
    %match(TermList subject) {
      concTerm(X*,X*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp4_1(t *testing.T) {
    assertTrue(t, match4_1(`concTerm(b(),a(),c())) == `False())
    assertTrue(t, match4_1(`concTerm(b(),f(a(),c()),c())) == `False())
    assertTrue(t, match4_1(`concTerm(a(),f(a(),c()),a())) == `False())
    assertTrue(t, match4_1(`concTerm(a(),a(),c(),a())) == `False())
    assertTrue(t, match4_1(`concTerm(a(),a(),a(),a())) == `True())
    assertTrue(t, match4_1(`concTerm(a(),a(),b(),b())) == `False())
    assertTrue(t, match4_1(`concTerm(a(),c(),a(),c())) == `True())
}

func match5(subject TermList) Result {
    %match(TermList subject) {
      !concTerm(X*,X*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp5(t *testing.T) {
    assertTrue(t, match5(`concTerm(b(),a(),c())) == `True())
    assertTrue(t, match5(`concTerm(b(),f(a(),c()),c())) == `True())
    assertTrue(t, match5(`concTerm(a(),f(a(),c()),a())) == `True())
    assertTrue(t, match5(`concTerm(a(),a(),c(),a())) == `True())
    assertTrue(t, match5(`concTerm(a(),a(),a(),a())) == `False())
    assertTrue(t, match5(`concTerm(a(),a(),b(),b())) == `True())
    assertTrue(t, match5(`concTerm(a(),c(),a(),c())) == `False())
}

func match6(subject TermList) Result {
    %match(TermList subject) {
      !concTerm(_*,x,_*,x,_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp6(t *testing.T) {
    assertTrue(t, match6(`concTerm(b(),a(),c())) == `True())
    assertTrue(t, match6(`concTerm(b(),f(a(),c()),c())) == `True())
    assertTrue(t, match6(`concTerm(a(),f(a(),c()),a())) == `False())
    assertTrue(t, match6(`concTerm(a(),a(),c(),a())) == `False())
    assertTrue(t, match6(`concTerm(b(),a(),c(),a())) == `False())
    assertTrue(t, match6(`concTerm(a(),a(),b(),b())) == `False())
}

func match7(subject TermList) Result {
    %match(TermList subject) {
      concTerm(_*,x,_*,!x,_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp7(t *testing.T) {
    assertTrue(t, match7(`concTerm(b(),a(),c())) == `True())
    assertTrue(t, match7(`concTerm(b(),f(a(),c()),c())) == `True())
    assertTrue(t, match7(`concTerm(a(),f(a(),c()),a())) == `True())
    assertTrue(t, match7(`concTerm(a(),a(),c(),a())) == `True())
    assertTrue(t, match7(`concTerm(b(),a(),c(),a())) == `True())
    assertTrue(t, match7(`concTerm(a(),a(),a(),b())) == `True())
    assertTrue(t, match7(`concTerm(a(),a(),a(),a())) == `False())
}

func match8(subject TermList) Result {
    %match(TermList subject) {
      !concTerm(_*,x,_*,!x,_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp8(t *testing.T) {
    assertTrue(t, match8(`concTerm(b(),a(),c())) == `False())
    assertTrue(t, match8(`concTerm(b(),f(a(),c()),c())) == `False())
    assertTrue(t, match8(`concTerm(a(),f(a(),c()),a())) == `False())
    assertTrue(t, match8(`concTerm(a(),a(),c(),a())) == `False())
    assertTrue(t, match8(`concTerm(b(),a(),c(),a())) == `False())
    assertTrue(t, match8(`concTerm(a(),a(),a(),b())) == `False())
    assertTrue(t, match8(`concTerm(a(),a(),a(),a())) == `True())

}

func match9(subject TermList) Result {
    %match(TermList subject) {
      concTerm(_*,_x,_*,!_y,_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp9(t *testing.T) {
    assertTrue(t, match9(`concTerm(b(),a(),c())) == `False())
    assertTrue(t, match9(`concTerm(b(),f(a(),c()),c())) == `False())
    assertTrue(t, match9(`concTerm(a(),f(a(),c()),a())) == `False())
    assertTrue(t, match9(`concTerm(a(),a(),c(),a())) == `False())
    assertTrue(t, match9(`concTerm(b(),a(),c(),a())) == `False())
    assertTrue(t, match9(`concTerm(a(),a(),a(),b())) == `False())
    assertTrue(t, match9(`concTerm(a(),a(),a(),a())) == `False())
}

func match10(subject Term) Result {
    %match(Term subject) {
      f(x,l(concTerm(_*,x,_*))) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp10(t *testing.T) {
    assertTrue(t, match10(`f(a(),l(concTerm(b(),a(),c())))) == `True())
    assertTrue(t, match10(`f(a(),l(concTerm(b(),c(),c())))) == `False())
    assertTrue(t, match10(`f(g(a()),l(concTerm(b(),c(),c())))) == `False())
    assertTrue(t, match10(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `True())
}

func match10_1(subject Term) Result {
    %match(Term subject) {
      f(x,l(!concTerm(_*,x,_*))) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp10_1(t *testing.T) {
    assertTrue(t, match10_1(`f(a(),l(concTerm(b(),a(),c())))) == `False())
    assertTrue(t, match10_1(`f(a(),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match10_1(`f(g(a()),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match10_1(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False())
}

func match11(subject Term) Result {
    %match(Term subject) {
      !f(x,l(concTerm(_*,x,_*)))-> {
        return `True()
      }
    }
    return `False()
}

func TestAp11(t *testing.T) {
    assertTrue(t, match11(`f(a(),l(concTerm(b(),a(),c())))) == `False())
    assertTrue(t, match11(`f(a(),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match11(`f(g(a()),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match11(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False())
    assertTrue(t, match11(`a()) == `True())
}

func match12(subject Term) Result {
    %match(Term subject) {
      f(x,!l(concTerm(_*,x,_*)))-> {
        return `True()
      }
    }
    return `False()
}

func TestAp12(t *testing.T) {
    assertTrue(t, match12(`f(a(),l(concTerm(b(),a(),c())))) == `False())
    assertTrue(t, match12(`f(a(),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match12(`f(g(a()),l(concTerm(b(),c(),c())))) == `True())
    assertTrue(t, match12(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False())
    assertTrue(t, match12(`a()) == `False())
    assertTrue(t, match12(`f(g(a()),b())) == `True())
}

  // used to generate a compilation error
func match13(subject Term) Result {
    %match(Term subject) {
      lst(_*,lst(_*,f(a(),x@!a()),_*),lst(_*,f(a(),x),_*),_*)-> {
        return `True()
      }
    }
    return `False()
}

  // used to generate a compilation error
func match14(subject Term) Result {
    %match(Term subject) {
      lst(_*,lst(_*,f(a(),x@!a()),_*),lst(_*,f(a(),x@!a()),_*),_*)-> {
        return `True()
      }
    }
    return `False()
}

func match15(subject Term) Result {
    %match(Term subject) {
      //used to be before automatic FL:
      // lst(_*,lst(_*,f(y,x@!a()),_*),lst(_*,f(y,_),_*),_*) -> {
      lst(_*,f(y,x@!a()),_*,f(y,_),_*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp15(t *testing.T) {
    var s Term = `lst(lst(f(a(),a()),f(a(),b()),f(a(),a())),lst(f(a(),a()),f(a(),b()),f(a(),a())))
    assertTrue(t, match15(s) == `True())
}

func match16(subject TermList) Result {
    %match( subject ) {
      concTerm(_before*,lst(_begin0*,f(_col0,_x@!a()),_end0*),lst(_begin1*,f(_col0,a()),_end1*),_after*) -> {
        return `True()
      }
    }
    return `False()
}

func TestAp16(t *testing.T) {
    var tLst TermList = `concTerm(
        lst(f(b(),a()),f(c(),a()),f(d(),a())),
        lst(f(b(),b()),f(c(),a()),f(d(),a())),
        lst(f(b(),b()),f(c(),a()),f(d(),c())),
        lst(f(b(),c()),f(c(),c()),f(d(),a())),
        lst(f(b(),b()),f(c(),d()),f(d(),d()))
    )

    assertTrue(t, match16(tLst) == `True())
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}
