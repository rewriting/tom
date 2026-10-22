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

// Port of test/gom/TestFromMethods.t: Java Gom's fromTerm(toATerm()) and
// fromString(toString()) round trips on every builtin sort.
// port: `import gom.b.u.i.l.t.i.n.builtin.types.*;` is the dot import of
// the Go package that gomgo generates from Builtin.gom (sub-directory
// b/u/i/l/t/i/n/builtin).
// port: the JUnit Theories runner calls each @Theory with every
// @DataPoints value: a Go test loops over data(), and fails on the first
// value that does not round-trip, as the theory does.
// port: java.lang.Integer.MAX_VALUE and java.lang.Long.MAX_VALUE are
// math.MaxInt32 and math.MaxInt64; (float)20.3 is float32(20.3);
// aterm.pure.SingletonFactory.getInstance() is aterm.GetInstance();
// Wrapper.fromTerm/fromString and toATerm/toString are
// WrapperFromTerm/WrapperFromString and ToATerm/fmt.Sprint.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "math"
  "testing"

  . "fixture/b/u/i/l/t/i/n/builtin"
  "tom/library/aterm"
)

  %include { b/u/i/l/t/i/n/builtin/Builtin.tom }

  func data() []Wrapper {
    return []Wrapper {
      `Char('a'),
      `Int(21),
      `Int(math.MaxInt32-2),
      `Int(math.MaxInt32),
      `Real(21),
      `Real(float32(20.3)),
      `Double(21),
      `Double(20.3),
      `Bool(true()),
      `Bool(false()),
      `Long(23),
      `Long(math.MaxInt32-3),
      `Long(math.MaxInt32-1),
      `Long(math.MaxInt32),
      `Long(math.MaxInt64-10),
      `Long(math.MaxInt64-1),
      `Long(math.MaxInt64),
      `Name("who?"),
      `Name("\"Hello\""),
      `Name("\"Hello\tWorld\n print-it\f end\""),
      `Node(aterm.GetInstance().Parse("f(g,(h(<a>,b),b),c)")),
      `concWrap(Int(1),Int(2),Name("toto"),Name("blop")),
      `concWrap(Int(1),concWrap(Name("a"),Name("b"),Name("c")),Int(3)),
      `WrapListInt(concInt(1,2,3,4,5,6,7,8,9,0)),
      `WrapListLong(concLong(10000,123455,23445556)),
      `WrapListBool(concBool(true(),false(),true(),false())),
      `WrapListChar(concChar('t','o','m')),
    }
  }

func TestFromTerm(t *testing.T) {
  for _, testSubject := range data() {
    var newObj Wrapper = WrapperFromTerm(testSubject.ToATerm())
    assertEquals(t, testSubject,newObj)
  }
}

func TestFromString(t *testing.T) {
  for _, testSubject := range data() {
    var newObj Wrapper = WrapperFromString(fmt.Sprint(testSubject))
    assertEquals(t, testSubject,newObj)
  }
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
