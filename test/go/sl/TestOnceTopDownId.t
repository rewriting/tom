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

// Port of test/sl/TestOnceTopDownId.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import sl.testoncetopdownid.test.types.*;`.
// port: Java's visit/visitLight throw VisitFailure; the Go Visit and
// VisitLight return it as an error (the try/catch blocks test it), and
// the result is only assigned when there is no failure, as in Java.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  "tom/library/sl"
)

  %include { sl.tom }
  %gom {
    module test
    imports
    abstract syntax
    Term = a()
         | b()
         | c()
         | f(t1:Term,t2:Term)
  }

func Test1(tt *testing.T) {
    // port: the *testing.T parameter is named tt (JUnit's test1 has no
    // parameter); the original's local `Term t` keeps its name.
    var t Term = `f(a(),a())
    if r, err := `OnceTopDownId(Bug()).VisitLight(t, sl.VisitableIntrospector{}); err == nil {
      t = r.(Term)
      assertEquals(tt, "It should rewite one a()",`f(b(),a()),t)
    } else {
      fail(tt, "It should not fail")
    }
    if r, err := `OnceTopDownId(Bug()).VisitLight(t, sl.VisitableIntrospector{}); err == nil {
      t = r.(Term)
      fail(tt, "It should fail, but got "+fmt.Sprint(t))
    } else {
    }

    var t2 Term = `f(c(),c())
    // port: the original declares `throws VisitFailure` for this
    // unprotected visit: a failure fails the test.
    r, err := `OnceTopDownId(Bug()).Visit(t2, sl.VisitableIntrospector{})
    if err != nil {
      tt.Fatal("tom.library.sl.VisitFailure()")
    }
    t = r.(Term)
    assertEquals(tt, "they should be equal",t,t2)
}

  %strategy Bug() extends Identity() {
    visit Term {
      a() -> { return `b(); }
      // port: `throw new tom.library.sl.VisitFailure();` is sl.ThrowVisitFailure().
      b() -> { sl.ThrowVisitFailure() }
    }
  }

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
