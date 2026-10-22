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

// Port of test/antipatterns/TestAntiPattern3.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import antipatterns.testantipattern3.peano.types.*;`.
// port: the java.util.HashSet of Integer is a Go set (map[int]bool):
// bag.add(i) is bag[i] = true, bag.contains(i) is bag[i].
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module Peano
      abstract syntax
      Nat = zero()
      | suc(pred:Nat)
      | plus(x1:Nat, x2:Nat)
  }

func evaluate(n Nat) map[int]bool {
    bag := map[int]bool{}
    %match(Nat n) {
      plus(!x,!y)               -> { bag[1] = true }
      zero()                    -> { bag[2] = true }
      plus(x,!suc(y))           -> { bag[3] = true }
      plus(x,!suc(x))           -> { bag[4] = true }
      plus(x,y)                 -> { bag[5] = true }
      plus(x,x)                 -> { bag[6] = true }
      plus(x,!x)                -> { bag[7] = true }
      !plus(x,!suc(y))          -> { bag[8] = true }
      !plus(x,!suc(suc(y)))     -> { bag[9] = true }
      plus(x,suc(suc(plus(y,!x))))    -> { bag[10] = true }
    }
    return bag
}

func Test1(t *testing.T) {
    bag := evaluate(`zero())
    assertTrue(t, !bag[1])
    assertTrue(t, bag[2])
    assertTrue(t, !bag[3])
    assertTrue(t, !bag[4])
    assertTrue(t, !bag[5])
    assertTrue(t, !bag[6])
    assertTrue(t, !bag[7])
    assertTrue(t, bag[8])
    assertTrue(t, bag[9])
    assertTrue(t, !bag[10])
}

func Test2(t *testing.T) {
    bag := evaluate(`plus(zero(),zero()))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",bag[3])
    assertTrueMsg(t, "4",bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",bag[6])
    assertTrueMsg(t, "7",!bag[7])
    assertTrueMsg(t, "8",!bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}

func Test3(t *testing.T) {
    bag := evaluate(`plus(zero(),suc(zero())))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",!bag[3])
    assertTrueMsg(t, "4",!bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}

func Test4(t *testing.T) {
    bag := evaluate(`plus(suc(zero()),zero()))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",bag[3])
    assertTrueMsg(t, "4",bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",!bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}

func Test5(t *testing.T) {
    bag := evaluate(`plus(suc(zero()),suc(suc(zero()))))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",!bag[3])
    assertTrueMsg(t, "4",!bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",bag[8])
    assertTrueMsg(t, "9",bag[9])
    assertTrueMsg(t, "10",!bag[10])
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertTrue(message, condition)
func assertTrueMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}
