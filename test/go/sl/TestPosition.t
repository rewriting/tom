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

// Port of test/sl/TestPosition.t.
// port: tom.library.sl.Position and Path are sl.Position and sl.Path of
// the Go sl library (Position.makeFromArray is sl.MakePositionFromArray).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "tom/library/sl"
)

func TestNormalize(t *testing.T){
  var p *sl.Position = sl.MakePositionFromArray([]int{1,-1,2,-2,1,2,3,-3,1,1})
  var pp sl.Path = p.GetCanonicalPath()
  assertEquals(t, pp,sl.MakePositionFromArray([]int{1,2,1,1}))
}

func TestAdd(t *testing.T){
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1,2,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,2,1,2,1})
  var r sl.Path = p2.Add(p1)
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{1,2,1,2,1,1,1,2,1}))
}

func TestInverse(t *testing.T){
  var p *sl.Position = sl.MakePositionFromArray([]int{1,1,2})
  var r sl.Path = p.Inverse()
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{-2,-1,-1}))
}

func TestSub(t *testing.T){
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1,2,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,2,1,2,1})
  var r sl.Path = p2.Sub(p1)
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{-1, -2, -1, 2, 1, 2, 1}))
}

func TestEquals(t *testing.T) {
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  assertEqualsMsg(t, "Similar objects should be equal" ,p1,p2)
}

func TestHashCode(t *testing.T) {
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  assertEqualsMsg(t, "HashCode for similar objects should be equal",
      p1.HashCode(),p2.HashCode())
}

// org.junit.Assert.assertEquals: expected.equals(actual) (Equals for
// sl.Position, == otherwise).
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  assertEqualsMsg(t, "", expected, actual)
}

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  equal := expected == actual
  if e, ok := expected.(interface{ Equals(any) bool }); ok {
    equal = e.Equals(actual)
  }
  if !equal {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
