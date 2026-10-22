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

// Port of test/sl/TestEnvironment.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import sl.testenvironment.term.types.*;`.
// port: tom.library.sl.Environment and Position are sl.Environment and
// sl.Position of the Go sl library.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "tom/tomgo/stable/library/sl"
)

  %gom {
    module term
    abstract syntax
    Term = f(lhs:Term,rhs:Term)
         | a() | b() | c()
  }

func TestEqual(t *testing.T){
    var e1 *sl.Environment = sl.NewEnvironment()
    var e2 *sl.Environment = sl.NewEnvironment()
    assertEquals(t, e1,e2)
    var subject Term = `f(a(),b())
    e1.SetSubject(subject)
    e2.SetSubject(subject)
    assertEquals(t, e1,e2)
    e1.Down(2)
    e2.Down(2)
    assertEquals(t, e1,e2)
    e1.Up()
    e2.Up()
    e1.Down(1)
    e2.Down(1)
    assertEquals(t, e1,e2)
}


func TestUpAndDown(t *testing.T){
    var e1 *sl.Environment = sl.NewEnvironment()
    var e2 *sl.Environment = sl.NewEnvironment()
    var subject Term = `f(a(),b())
    e1.SetSubject(subject)
    e2.SetSubject(subject)
    e1.Down(2)
    e2.Down(2)
    //test of down
    assertEquals(t, e1.GetRoot(),`f(a(),b()))
    assertEquals(t, e1.GetPosition(), sl.MakePositionFromArray([]int{2}))
    assertEquals(t, e1.GetSubject(),`b())
    e1.Up()
    e2.Up()
    //test of up
    assertEquals(t, e1.GetRoot(),`f(a(),b()))
    assertEquals(t, e1.GetPosition(), sl.MakePosition())
    assertEquals(t, e1.GetSubject(),`f(a(),b()))
    e1.Down(1)
    e2.Down(1)
    //test of down
    assertEquals(t, e1.GetRoot(),`f(a(),b()))
    assertEquals(t, e1.GetPosition(),sl.MakePositionFromArray([]int{1}))
    assertEquals(t, e1.GetSubject(),`a())
}

// org.junit.Assert.assertEquals: expected.equals(actual), that is
// Equals for sl.Environment and sl.Position, == for the maximally
// shared terms.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  equal := expected == actual
  if e, ok := expected.(interface{ Equals(any) bool }); ok {
    equal = e.Equals(actual)
  }
  if !equal {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
