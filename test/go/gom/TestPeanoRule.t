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

// Port of test/gom/TestPeanoRule.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testpeanorule.peano.types.*;`.
// port: the Fib5:block hook (Java code inserted into the class of Fib5:
// a static method eq) holds the Go function eq; Java's == on Objects is
// == on the Go interfaces (identity of the shared terms).
// port: the instance methods fibint, int2peano and peano2int are
// functions; "…"+N is fmt.Sprint.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"
)

  %gom {
    module peano
    abstract syntax
    Term = | Zero()
           | Suc(pred:Term)
           | Plus(s1:Term,s2:Term)
           | Fib1(t:Term)
           | Fib5(t:Term)
           | Fib6(t:Term)
           | NonZ(t:Term)

    module peano:rules() {
      Plus(x,Zero()) -> x
      Plus(x,Suc(y)) -> Suc(Plus(x,y))
    }

    module peano:rules() {
      Fib1(Zero())        -> Suc(Zero())
      Fib1(res@Suc(Zero()))   -> res
      Fib1(Suc(Suc(x)))   -> Plus(Fib1(x),Fib1(Suc(x)))
    }

    module peano:rules() {
      Fib5(Zero())      -> Suc(Zero())
      Fib5(x)           -> x if eq(x,Suc(Zero()))==true
      Fib5(Suc(Suc(x))) -> Plus(Fib5(x),Fib5(Suc(x)))
    }
    Fib5:block() {
      func eq(o1 any, o2 any) bool {
        return o1 == o2
      }
    }
    module peano:rules() {
      Fib6(Zero())      -> Suc(Zero())
      Fib6(x)           -> x if x == Suc(Zero())
      Fib6(Suc(Suc(x))) -> Plus(Fib6(x),Fib6(Suc(x)))
    }
    module peano:rules() {
      NonZ(x) -> x if Zero() != x
      NonZ(x) -> Suc(x)
    }
  }

func TestPlus(t *testing.T) {
    for i:=0 ; i<100 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing Plus with N ="+fmt.Sprint(N)+": ",
          peano2int(`Plus(N,N)) == (i+i) )
    }
}

func TestFib1(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing Fib1 with N ="+fmt.Sprint(N)+": ",
          peano2int(`Fib1(N)) == fibint(i) )
    }
}

func TestFib5(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing fib5 with N ="+fmt.Sprint(N)+": ",
          peano2int(`Fib5(N)) == fibint(i) )
    }
}

func TestFib5bis(t *testing.T) {
    var N Term = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))))
    assertTrue(t, "Testing fib5 with N =" + fmt.Sprint(peano2int(N)) + ": ",
               peano2int(`Fib5(N)) == fibint(peano2int(N)))
}

func TestFib5ter(t *testing.T) {
    var N Term = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))))
    assertTrue(t, "Testing fib5 with N =" + fmt.Sprint(peano2int(N))+"1" + ": ",
               peano2int(`Fib5(Suc(N))) == fibint(peano2int(N)+1))
}

func TestFib6(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing fib6 with N ="+fmt.Sprint(N)+": ",
          peano2int(`Fib6(N)) == fibint(i) )
    }
}

func TestFib6bis(t *testing.T) {
    var N Term = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))))
    assertTrue(t, "Testing fib6 with N =" + fmt.Sprint(peano2int(N)) + ": ",
               peano2int(`Fib6(N)) == fibint(peano2int(N)))
}

func TestFib6ter(t *testing.T) {
    var N Term = `Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Suc(Zero()))))))))))
    assertTrue(t, "Testing fib6 with N =" + fmt.Sprint(peano2int(N))+"1" + ": ",
               peano2int(`Fib6(Suc(N))) == fibint(peano2int(N)+1))
}

func TestNonZ1(t *testing.T) {
    var N Term = `Zero()
    assertEquals(t, "NonZ is not zero", `NonZ(N), `Suc(Zero()))
}

func TestNonZ2(t *testing.T) {
    var N Term = `Suc(Zero())
    assertEquals(t, "NonZ is id for non Zero", `NonZ(N), N)
}

  func fibint(n int) int {
    if(n<=1) {
      return 1
    } else {
      return fibint(n-1)+fibint(n-2)
    }
  }

func TestInt2p(t *testing.T) {
    assertEquals(t, "int2peano should return sssss(tzero) for 5",
        int2peano(5),`Suc(Suc(Suc(Suc(Suc(Zero()))))))
}

  func int2peano(n int) Term {
    var N Term = `Zero()
    for i:=0 ; i<n ; i++ {
      N = `Suc(N)
    }
    return N
  }

func TestPeano2int(t *testing.T) {
    assertEquals(t, "peano2int should return 5 for sssss(tzero)",
        peano2int(`Suc(Suc(Suc(Suc(Suc(Zero())))))),5)
}

  func peano2int(N Term) int {
    %match(Term N) {
      Zero() -> { return 0 }
      Suc(x) -> {return 1+peano2int(`x) }
    }
    return 0
  }

// org.junit.Assert.assertTrue(message, condition)
func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}

// org.junit.Assert.assertEquals(message, expected, actual): the terms
// are maximally shared, so equals is ==.
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
