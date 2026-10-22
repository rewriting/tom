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

// Port of test/sl/TestGetSubterm.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import sl.testgetsubterm.test.types.*;`.
// port: tom.library.sl.Position and Strategy are sl.Position and
// sl.Strategy; Java's visit/visitLight throw VisitFailure, the Go Visit
// and VisitLight return it as an error.
// port: java.util.Collection<Position> (a HashSet) becomes positionSet,
// a set of *sl.Position compared with Position.Equals.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
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

// port: the *testing.T parameter is named tt because the original's
// local `Term t` would shadow it.
func Test1(tt *testing.T) {
    var bag *positionSet = &positionSet{}
    var t Term = `f(a(),a())
    if r, err := `TopDown(Bug(bag)).Visit(t, sl.VisitableIntrospector{}); err == nil {
      t = r.(Term)
      assertEquals(tt, "There are two a()",2,bag.size())
    } else {
      fail(tt, "It should not fail")
    }
    for _, p := range bag.elems {
      var sts sl.Strategy = p.GetSubterm()
      // port: the original declares `throws VisitFailure` for this
      // unprotected visit: a failure fails the test.
      r, err := sts.Visit(t, sl.VisitableIntrospector{})
      if err != nil {
        tt.Fatal("tom.library.sl.VisitFailure()")
      }
      var st Term = r.(Term)
      assertEquals(tt, "", `a(),st)
    }
    for _, p := range bag.elems {
      var sts sl.Strategy = p.GetSubterm()
      if r, err := sts.VisitLight(t, sl.VisitableIntrospector{}); err == nil {
        var st Term = r.(Term)
        assertEquals(tt, "", `a(),st)
      } else {
        fail(tt, "getsubterm should not fail there")
      }
    }
}

  %typeterm PositionCollection {
    implement { *positionSet }
    is_sort(t) { func() bool { _, ok := (t).(*positionSet); return ok }() }
  }
  %strategy Bug(bag:PositionCollection) extends Identity() {
    visit Term {
      // port: host code reads the strategy parameter as a field of the
      // receiver, and getEnvironment() as tom__strat.GetEnvironment().
      a() -> { tom__strat.bag.add(tom__strat.GetEnvironment().GetPosition()) }
    }
  }

// positionSet is java.util.HashSet<Position>: add keeps one element per
// equal (Position.Equals) position.
type positionSet struct{ elems []*sl.Position }

func (s *positionSet) add(p *sl.Position) bool {
  for _, e := range s.elems {
    if e.Equals(p) {
      return false
    }
  }
  s.elems = append(s.elems, p)
  return true
}

func (s *positionSet) size() int { return len(s.elems) }

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
