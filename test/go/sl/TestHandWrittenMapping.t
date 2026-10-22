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

// Port of test/sl/TestHandWrittenMapping.t (compiled with
// --genIntrospector, as test/build.xml does).
// port: the hand-written Java classes Term, f and a become Go types
// (an interface and two structs) with the same equals/toString; the
// hooks of the hand-written mappings are written in Go.
// port: java.util.List (LinkedList for %oplist, ArrayList for %oparray)
// becomes the slice type List; insert and add return a new slice
// (the Java versions mutate their argument and return it).
// port: `new LocalIntrospector()` is &LocalIntrospector{}, the Go
// sl.Introspector that --genIntrospector generates; Java's visit throws
// VisitFailure, the Go Visit returns it (the catch blocks call fail()).
// port: the *testing.T parameter is named tt because the original's
// local `List t` would shadow it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  "tom/library/sl"
)

  %include{sl.tom}

  /**
   *  Term = f(x:Term)
   *        | a()
   *  List = l(Term*)
   **/


  type Term interface {
    Equals(t any) bool
  }

  type f struct {
    x Term
  }

  func newF(x Term) *f { return &f{x: x} }
  func (t *f) getx() Term { return t.x }
  func (t *f) Equals(o any) bool {
    g, ok := o.(*f)
    return ok && g.getx().Equals(t.x)
  }
  func (t *f) String() string { return "f(" + fmt.Sprint(t.x) + ")" }

  type a struct{}

  func newA() *a { return &a{} }
  func (t *a) Equals(o any) bool {
    _, ok := o.(*a)
    return ok
  }
  func (t *a) String() string { return "a()" }


  // mappings
  %typeterm Term {
    implement     { Term }
    is_sort(t)    { func() bool { _, ok := ($t).(Term); return ok }() }
    equals(t1,t2) { $t1.Equals($t2) }
  }

  %op Term f(x:Term) {
    is_fsym(t)    { func() bool { _, ok := ($t).(*f); return ok }() }
    get_slot(x,t) { ($t).(*f).getx() }
    make(x)       { newF($x) }
  }

  %op Term a() {
    is_fsym(t) { func() bool { _, ok := ($t).(*a); return ok }() }
    make()     { newA() }
  }


  %typeterm List {
    implement { List }
    is_sort(t) { func() bool { _, ok := ($t).(List); return ok }() }
    equals(l1,l2) { $l1.Equals($l2) }
  }

  %oplist List l(Term*) {
    is_fsym(t) { $t != nil }
    make_empty()  { List{} }
    make_insert(e,l) { insert($e,$l) }
    get_head(l)   { $l[0] }
    get_tail(l)   { $l[1:] }
    is_empty(l)   { len($l) == 0 }
  }

  %oparray List t(Term*) {
    is_fsym(t) { $t != nil }
    make_empty(n)    { make(List, 0, $n) }
    make_append(e,l) { add($e,$l) }
    get_element(l,n) { $l[$n] }
    get_size(l)      { int64(len($l)) }
  }

  type List []Term

  // java.util.List.equals: same size and equal elements.
  func (l List) Equals(o any) bool {
    m, ok := o.(List)
    if !ok || len(l) != len(m) {
      return false
    }
    for i := range l {
      if !l[i].Equals(m[i]) {
        return false
      }
    }
    return true
  }

  func add(e Term, l List) List {
    return append(append(List(nil), l...), e)
  }

  func insert(e Term, l List) List {
    return append(List{e}, l...)
  }

  %strategy R() extends Identity() {
    visit Term {
      f(x) -> { return `x; }
    }
  }

  %strategy R2() extends Identity() {
    visit Term {
      a() -> { return `f(a()); }
    }
  }


    func Test1(tt *testing.T) {
        var t Term = `f(f(f(a())))
        assertEquals(tt, `f(a()), visit(tt, `TopDown(R()), t, &LocalIntrospector{}))
    }

    func Test2(tt *testing.T) {
        var t Term = `f(a())
        assertEquals(tt, `f(f(a())), visit(tt, `BottomUp(R2()), t, &LocalIntrospector{}))
    }

    func Test3(tt *testing.T) {
        var t Term = `f(f(f(a())))
        assertEquals(tt, `a(), visit(tt, `InnermostId(R()), t, &LocalIntrospector{}))
    }

    func TestOpList1(tt *testing.T) {
        var l List = `l(a(),a(),a())
        assertEquals(tt, `l(f(a()),f(a()),f(a())), visit(tt, `All(R2()), l, &LocalIntrospector{}))
    }


    func TestOpList2(tt *testing.T) {
        var t List = `l(f(f(f(a()))),a(),f(f(f(a()))))
        assertEquals(tt, `l(f(a()),a(),f(a())), visit(tt, `TopDown(R()), t, &LocalIntrospector{}))
    }

    func TestOpList3(tt *testing.T) {
        var t List = `l(f(a()),a(),f(a()))
        assertEquals(tt, `l(f(f(a())),f(a()),f(f(a()))), visit(tt, `BottomUp(R2()), t, &LocalIntrospector{}))
    }

    func TestOpList4(tt *testing.T) {
        var t List = `l(f(f(f(a()))),a(),f(f(f(a()))))
        assertEquals(tt, `l(a(),a(),a()), visit(tt, `InnermostId(R()), t, &LocalIntrospector{}))
    }

    func TestOpArray1(tt *testing.T) {
        var t List = `t(a(),a(),a())
        assertEquals(tt, `t(f(a()),f(a()),f(a())),visit(tt, `All(R2()), t, &LocalIntrospector{}))
    }

    func TestOpArray2(tt *testing.T) {
        var t List = `t(f(f(f(a()))),a(),f(f(f(a()))))
        assertEquals(tt, `t(f(a()),a(),f(a())), visit(tt, `TopDown(R()), t, &LocalIntrospector{}))
    }

    func TestOpArray3(tt *testing.T) {
        var t List = `t(f(a()),a(),f(a()))
        assertEquals(tt, `t(f(f(a())),f(a()),f(f(a()))), visit(tt, `BottomUp(R2()), t, &LocalIntrospector{}))
    }

    func TestOpArray4(tt *testing.T) {
        var t List = `t(f(f(f(a()))),a(),f(f(f(a()))))
        assertEquals(tt, `t(a(),a(),a()), visit(tt, `InnermostId(R()), t, &LocalIntrospector{}))
    }

// s.visit(subject, introspector) inside try { … } catch (VisitFailure e)
// { fail(); }: a failure fails the test.
func visit(t *testing.T, s sl.Strategy, subject any, intro sl.Introspector) any {
  t.Helper()
  r, err := s.Visit(subject, intro)
  if err != nil {
    t.Fatal()
  }
  return r
}

// org.junit.Assert.assertEquals(expected, actual): expected.equals(actual)
func assertEquals(t *testing.T, expected interface{ Equals(any) bool }, actual any) {
  t.Helper()
  if !expected.Equals(actual) {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
