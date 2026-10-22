package andor

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

// Port of test/andor/TestAndOr.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import andor.testandor.m.types.*;`.
// port: `x != y` on terms is != on the Go interfaces (identity of the
// shared terms, as in Java).
// port: the *testing.T parameter is named tt where the original's local
// `Term t` would shadow it.
// port: Test14 declares `counter` without using it: `_ = counter`, as
// Go rejects an unused local variable.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module M
      abstract syntax
      Term = 
      | a()
      | b()
      | c()
      | list( Term* )
      | f(t1:Term,t2:Term)
      | h( l:TermList)
      | g(t:Term)

      TermList = termList(Term*)
  }
func Test1(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {
        return
      }
    }
    fail(t)
}

func Test2(t *testing.T) {
    %match(f(b(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {
        return
      }
    }
    fail(t)
}

func Test3(t *testing.T) {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << a() || _x << b() ) -> {
        fail(t)
      }
    }
}

func Test4(t *testing.T) {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        fail(t)
      }
    }
}

func Test5(t *testing.T) {
    %match(f(c(),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        fail(t)
      }
    }
}

func Test6(t *testing.T) {
    %match(f(g(a()),b())) {
      f(_x,_y) && ( _x << g(a()) || _x << b() ) -> {
        return
      }
    }
    fail(t)
}

func Test7(t *testing.T) {
    var s Term = `f(g(a()),b())
    %match(s) {
      f(a(),_y) || f(b(),_y) << s -> {
        fail(t)
      }
    }
    return
}

func Test8(t *testing.T) {
    var s Term = `f(g(a()),b())
    %match(s) {
      f(g(a()),_y) || f(b(),_y) << s -> {
        return
      }
    }
    fail(t)
}

func Test9(t *testing.T) {
    var s Term = `f(g(a()),b())
    %match(s) {
      f(b(),_y) || f(g(a()),_y) << s -> {
        return
      }
    }
    fail(t)
}

  /*
   * completeness tests
   */

func Test10(t *testing.T) {
    var s Term = `f(g(a()),b())
    counter := 0
    %match(s) {
      f(_x,_y) && ( a() << a() || b() << b() ) -> {
        counter++
      }
    }
    if (counter != 2){
      fail(t)
    }
}

func Test11(t *testing.T) {
    var s Term = `f(g(a()),b())
    counter := 0
    %match(s) {
      f(_x,_y) && ( g(_x) << g(g(a())) || _x << g(a()) ) -> {
        counter++
      }
    }
    if (counter != 2){
      fail(t)
    }
}

func Test12(t *testing.T) {
    var s Term = `f(g(a()),g(b()))
    counter := 0
    %match(s) {
      f(_,_) || f(g(_),g(_)) << s -> {
        counter++
      }
    }
    if (counter != 2){
      fail(t)
    }
}

func Test13(t *testing.T) {
    var s Term = `f(g(a()),g(b()))
    counter := 0
    %match(s) {
      f(_,_) || f(g(a()),g(b())) << s || f(g(_),g(_)) << s -> {
        counter++
      }
    }
    if (counter != 3){
      fail(t)
    }
}

func Test14(t *testing.T) {
    var s Term = `f(g(a()),g(b()))
    counter := 0; _ = counter
    %match(s) {
      // used to generate an error with the optimizer
      f(_,_) && (z << a() || z << b()) -> {
        if (`z != `a() && `z != `b()){
          fail(t)
        }
      }
    }
}

func Test15(tt *testing.T) {
    var t Term = `f(a(),b())
    %match(t) {
      f(x,_y) && (a() << x || b() << x)  -> {
        if (`x != `a()){
          fail(tt)
        }
        return
      }
    }
    fail(tt)
}

func Test16(tt *testing.T) {
    var t Term = `f(g(a()),b())
    %match(t) {
      f(x,_y) && g(z) << x  -> {
        if (`z != `a()){
          fail(tt)
        }
        return
      }
    }
    fail(tt)
}

func Test17(tt *testing.T) {
    var t Term = `f(g(a()),b())
    %match(t) {
      _ && g(z) << x && f(x,_y) << t  -> {
        if (`z != `a()){
          fail(tt)
        }
        return
      }
    }
    fail(tt)
}

func Test18(t *testing.T) {
    var l Term = `list(a(),b(),g(a()),c())
    %match(l) {
      list(X*,b(),Y*) && z << list(X*,Y*)  -> {
        if (`z != `list(a(),g(a()),c())){
          fail(t)
        }
        return
      }
    }
    fail(t)
}

func Test19(t *testing.T) {
    var l Term = `list(a(),b(),g(a()),c())
    %match(l) {
      list(X*,b(),Y*) && list(_*,g(z),_*) << list(X*,Y*)  -> {
        if (`z != `a()){
          fail(t)
        }
        return
      }
    }
    fail(t)
}

func Test20(t *testing.T) {
    var s Term = `a()
    cnt := 0
    %match(s) {
      a() || b() << s  -> { cnt++; }
    }
    if (cnt > 1) {
      fail(t)
    }
}

func Test21(t *testing.T) {
    var s Term = `a()
    cnt := 0
    %match(Term s) {
      x || x << Term s  -> {
        if (`x != `a()) {
          fail(t)
        }
        cnt++
      }
    }
    if (cnt != 2) {
      fail(t)
    }
}

func Test22(t *testing.T) {
    var s Term = `g(a())
    cnt := 0
    %match(s) {
      g(x) || g(x) << s -> {
        if (`x != `a()) {
          fail(t)
        }
        cnt++
      }
    }
    if (cnt != 2) {
      fail(t)
    }
}

  // allow any number of parantheses
func Test23(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && ((((( _x << a() || _x << b() ))))) -> {
        return
      }
    }
    fail(t)
}

func Test24(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && (( _x << a() || _x << b() )) -> {
        return
      }
    }
    fail(t)
}

func Test25(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && (( (_x << a()) || ((_x << b())) )) -> {
        return
      }
    }
    fail(t)
}

func Test26(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && ( (_x << a() && _x << a()) || ( _x << b() && _x << b() ) ) -> {
        return
      }
    }
    fail(t)
}


func Test27(t *testing.T) {
    %match(f(a(),b())) {
      f(_x,_y) && ( (((_x << a())) && _x << a()) || ( _x << b() && _x << b() ) ) -> {
        return
      }
    }
    fail(t)
}

func Test28(t *testing.T) {
    var l Term = `list(a(),b(),g(a()),c())
    %match(l) {
      list(_*,b(),_*) || list(_*,c(),_*) << l -> {
        return
      }
    }
    fail(t)
}

func Test29(t *testing.T) {
    var l Term = `f(a(),a())
    %match(l) {
      f(t1,t2) && t1==t2 -> {
        return
      }
    }
    fail(t)
}

func Test30(t *testing.T) {
    var l Term = `f(a(),b())
    %match(l) {
      g(_) || f(x,x)<<l -> {
        fail(t)
      }
    }
    return
}

// org.junit.Assert.fail()
func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
