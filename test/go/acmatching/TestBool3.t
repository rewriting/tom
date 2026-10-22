package acmatching
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

// Port of test/acmatching/TestBool3.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import acmatching.testbool3.term.types.*;`.
// port: the TestBool3 instance methods are functions: `TestBool3 b3 = new
// TestBool3();` disappears and b3.e_and(…) is e_and(…).
// port: `X*.isEmptyp()` is `X*.IsEmptyp()` (gomgo's Java API).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %include{ int.tom }
  %include{ intarray.tom }
  %gom {
    module Term
      imports 
      abstract syntax

      T = | b0()
      | b1()
      | b2()
      | a1()
      | a2()
      | a3()
      | a4()
      | a5()
      | a6()
      | a7()
      | a8()
      | p(T*)
      | m(T*)
      | and(x:T,y:T)
      | or(x:T,y:T)
      | not(x:T)

      m:AC{}
      p:AC{}

  }

func Test1(t *testing.T) {
      var res1 T = e_and(`a1(),`a1())
      var res2 T = e_not( e_or( e_not(`a1()), e_not(`a1())))
      assertEquals(t, "and(a1,a1)==not(or(not(a1),not(a1)))",res1,res2)
}

func Test2(t *testing.T) {
      /*
         and(and(and(a1, a2), and(a3, a4)),and(a5,a6))
         ==
         not(or(or(or(not(a1), not(a2)), or(not(a3), not(a4))),or(not(a5),not(a6))))
       */
      var res1 T = e_and(e_and(e_and(`a1(), `a2()), e_and(`a3(), `a4())),e_and(`a5(),`a6()))
      var res2 T = e_not(e_or(e_or(e_or(e_not(`a1()), e_not(`a2())), e_or(e_not(`a3()), e_not(`a4()))),e_or(e_not(`a5()),e_not(`a6()))))

      //System.out.println("res1: " + res1); 
      //System.out.println("res2: " + res2); 
      //System.out.println(res1 == res2); 
      assertEquals(t, "and(a1,...,a6)==not(or(not(a1),...,not(a6)))",res1,res2)
}

func e(t T) T {
    %match(t) {
      m(x) -> { 
        return `e(x)
      }

      m??(X,X,X,Z*) -> { 
        return `e(p(e(m(X,Z*))))
      }

      m??(b0(),Z*) -> { 
        return `b0() 
      }

      m??(b1(),Z*) -> { 
        return `e(Z*)
      }

      m??(p??(X*, Y*), Z*) -> {
        if(!`X*.IsEmptyp() && !`Y*.IsEmptyp() && !`Z*.IsEmptym()) {
          return `e(p( 
                e(m(e(X*), Z*)), 
                e(m(e(Y*), Z*))
                ))
        }
      }

      p(x) -> { 
        return `e(x)
      }

      p??(X,X,X,Z*) -> { 
        return `e(p(b0(),Z*))
      }

      p??(b0(),Z*) -> {
        return `e(Z*) 
      }
    }
    return t
}

func e_and(X T,Y T) T {
    var mX T= `e(m(X,X))
    var mY T= `e(m(Y,Y))
    var b2 T= `e_b2()
    return `e(p( 
          e(m(mX, mY)),
          e(m(b2, mX, Y)),
          e(m(b2, mY, X)),
          e(m(b2, X, Y)) 
          ))
}

func e_or(X T, Y T) T {
    var mX T= `e(m(X,X))
    var mY T= `e(m(Y,Y))
    var b2 T= `e_b2()
    return `e(p(
          e(m(b2,mX, mY)),
          e(m(mX, Y)),
          e(m(mY, X)),
          e(m(X, Y)),
          X, Y))
}


func e_b2() T {
    return `e(p(b1(),b1())) 
}

func e_not(x T) T {
    var b2 T= `e_b2()
    return `e(p(
          e(m(b2,x)),
          b1()
          )) 
}

// org.junit.Assert.assertEquals(message, expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
