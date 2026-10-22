package backquote

/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *   - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *   - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *  - Neither the name of the Inria nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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

// Port of test/backquote/TestBackquoteGom.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import backquote.testbackquotegom.sig.*;` and
// `import backquote.testbackquotegom.sig.types.*;`.
// port: Java's string concatenation with an int ("i="+i) is
// "i="+str(i), str being Java's int-to-String conversion; Go has no
// increment expression, so i++ and ++i inside an expression are
// postInc(&i) and preInc(&i).
// port: the empty @Before setUp() is dropped.
// port: no main — `go test` runs the tests (the original main only
// launched JUnit, and on TestDefaultValue).

import (
  "strconv"
  "testing"
)

  %gom {
    module sig
    imports String
    abstract syntax
    T = f(s:String)
  }
  
func TestBQ1(t *testing.T) {
    i := 0
    assertEquals(t, `f("i="+str(i)), `f("i=0") )
    assertEquals(t, `f("i=" + str(i)), `f("i=0") )
    assertEquals(t, `f("i=" + str(postInc(&i))), `f("i=0") )
    assertEquals(t, `f("i=" + str(preInc(&i))), `f("i=2") )
}

// Java's conversion of an int to a String in a concatenation
func str(i int) string { return strconv.Itoa(i) }

// Java's i++ (the value before the increment)
func postInc(i *int) int { v := *i; *i++; return v }

// Java's ++i (the value after the increment)
func preInc(i *int) int { *i++; return *i }

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
