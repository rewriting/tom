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

// Port of test/sl/TestImportStrat.t.
// port: `import sl.testsl.types.*;` is the dot import of the Go package
// that gomgo generates from testsl.gom; CommonStrat (sl/CommonStrat.t)
// is the Go package commonstrat (see its port), whose strategy R is
// built by commonstrat.NewR().
// port: tom.library.sl.VisitFailure is the error returned by VisitLight.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "fixture/commonstrat"
  . "fixture/testsl"
  "tom/library/sl"
)

  %include { sl.tom }
  %include { testsl/testsl.tom }

  %op Strategy R() {
    make() { commonstrat.NewR() }
  }

// port: the *testing.T parameter is named tt because the original's
// local `Term t` would shadow it.
func Test1(tt *testing.T) {
    var t Term = `a()
    if r, err := `R().VisitLight(t, sl.VisitableIntrospector{}); err == nil {
      t = r.(Term)
      assertEquals(tt, "t==b()",`b(),t)
    } else {
      fail(tt, "It should not fail")
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
