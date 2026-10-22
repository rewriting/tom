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

// Port of test/acmatching/TestAC.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import acmatching.testac.term.types.*;`.
// port: a java.util.Set<T> (HashSet) is a Go set map[T]bool: add(x) is
// bag[x] = true, size() is len; assertEquals on two sets compares them as
// sets (assertEqualSets).
// port: assertThat(reason, bag, hasItem(t)) keeps its shape: hasItem(t)
// is a Go matcher (a predicate on the set), like JUnitMatchers.hasItem.
// port: the private static hasItem(T) helper of the original (a typed
// JUnitMatchers.hasItem) is the Go hasItem below.
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

      T =
      | a()
      | b()
      | c()
      | f(T*)
      | g(T*)

      f:AC{}
      g:AC{}

  }

func Test1(t *testing.T) {
    var t1 T = `f(a(),b(),c())
    bagX := map[T]bool{}
    bagY := map[T]bool{}

    %match(t1) {
      f??(X*,a(),Y*) -> {
        bagX[`X] = true
        bagY[`Y] = true
      }
    }

    assertThat(t, "f() is solution", bagX, hasItem(`f()))
    assertThat(t, "b() is solution", bagX, hasItem(`b()))
    assertThat(t, "c() is solution", bagX, hasItem(`c()))
    assertThat(t, "f(b(),c()) is solution", bagX, hasItem(`f(b(),c())))
    assertThat(t, "f(c(),b()) is solution", bagX, hasItem(`f(c(),b())))
    assertEqualSets(t, "bagX==bagY", bagX, bagY)
    assertEquals(t, "bagX.size==4", 4, len(bagX))
}

func Test2(t *testing.T) {
      var t1 T = `f(a(),b(),a())
      bagX := map[T]bool{}
      bagY := map[T]bool{}

      %match(t1) {
        f??(X*,X*,Y*) -> {
          bagX[`X] = true
          bagY[`Y] = true
        }
      }

      assertThat(t, "f() is solution of X", bagX, hasItem(`f()))
      assertThat(t, "f(a(),a()) is solution of X", bagX, hasItem(`f(a(),a())))
      assertThat(t, "t1 is solution ofYX", bagY, hasItem(t1))
      assertThat(t, "b() is solution of Y", bagY, hasItem(`b()))
      assertEquals(t, "bagX.size==2", 2, len(bagX))
      assertEquals(t, "bagY.size==2", 2, len(bagY))

      bagXX := map[T]bool{}
      bagYY := map[T]bool{}
      %match(t1) {
        f??(XX*,YY*,YY*) -> {
          bagXX[`XX] = true
          bagYY[`YY] = true
        }
      }
      assertEqualSets(t, "bagX==bagYY",bagX,bagYY)
      assertEqualSets(t, "bagY==bagXX",bagY,bagXX)

}

func Test3(t *testing.T) {
      var t1 T = `f(a(),b(),a())
      bagX := map[T]bool{}
      bagY := map[T]bool{}

      %match(t1) {
        f??(C*,X*,X*,Y*,Y*) -> {
          bagX[`X] = true
          bagY[`Y] = true
        }
      }

      assertThat(t, "f() is solution of X", bagX, hasItem(`f()))
      assertThat(t, "f() is solution of Y", bagY, hasItem(`f()))
      assertThat(t, "f(a(),a()) is solution of X", bagX, hasItem(`f(a(),a())))
      assertThat(t, "f(a(),a()) is solution of Y", bagY, hasItem(`f(a(),a())))
      assertEquals(t, "bagX.size==2", 2, len(bagX))
      assertEquals(t, "bagY.size==2", 2, len(bagY))
}

func Test4(t *testing.T) {
    var t1 T = `f(a(),b(),b(),a())
    bagX := map[T]bool{}

    %match(t1) {
      f??(X*,Y*) && X==Y -> {
        bagX[`X] = true
      }
    }

    assertThat(t, "f(a(),b()) is solution of X", bagX, hasItem(`f(a(),b())))
    assertThat(t, "f(b(),a()) is solution of X", bagX, hasItem(`f(b(),a())))
    assertEquals(t, "bagX.size==1", 1, len(bagX))
}

// JUnitMatchers.hasItem(t): a set holding t
func hasItem(item T) func(map[T]bool) bool {
  return func(bag map[T]bool) bool { return bag[item] }
}

// org.junit.Assert.assertThat(reason, actual, matcher)
func assertThat(t *testing.T, reason string, actual map[T]bool, matcher func(map[T]bool) bool) {
  t.Helper()
  if !matcher(actual) {
    t.Fatalf("%s: got %v", reason, actual)
  }
}

// org.junit.Assert.assertEquals(message, expected, actual) on two sets
func assertEqualSets(t *testing.T, message string, expected, actual map[T]bool) {
  t.Helper()
  equal := len(expected) == len(actual)
  for x := range expected {
    equal = equal && actual[x]
  }
  if !equal {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.assertEquals(message, expected, actual) on ints
func assertEquals(t *testing.T, message string, expected, actual int) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
