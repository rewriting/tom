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

// Port of test/backquote/TestDefaultValue.t.
// port: the hand-written mapping of the Gom module backquote.term
// (term.gom) is ported to the Go API that gomgo generates for it (package
// fixture/term): `$t instanceof backquote.term.types.term.f` is a type
// assertion to *term.FTerm, `backquote.term.types.term.f.make(…)` is
// term.MakeF(…), Consl/Emptyl are the non-empty/empty *term.LTerm, made by
// term.ConsL/term.EmptyL, and the getters are Getarg1, GetHeadl…
// port: the empty @Before setUp() is dropped.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"

  "fixture/term"
)

  //%include { testdefaultvalue/term.tom }
%include { string.tom }

%typeterm Term {
  implement { term.Term }
  is_sort(t) { func() bool { _, ok := any($t).(term.Term); return ok }() }

  equals(t1,t2) { ($t1==$t2) }

}
%op Term a() {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.ATerm); return ok }() }
  make() { term.MakeA() }
}

%op Term b() {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.BTerm); return ok }() }
  make() { term.MakeB() }
}

%op Term c() {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.CTerm); return ok }() }
  make() { term.MakeC() }
}

%op Term f(arg1:Term) {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.FTerm); return ok }() }
  get_slot(arg1, t) { $t.Getarg1() }
  get_default(arg1) { `a() }
  make(t0) { term.MakeF($t0) }
}

%op Term g(arg1:Term, arg2:Term) {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.GTerm); return ok }() }
  get_slot(arg1, t) { $t.Getarg1() }
  get_slot(arg2, t) { $t.Getarg2() }
  get_default(arg1) { `a() }
  get_default(arg2) { `b() }
  make(t0, t1) { term.MakeG($t0, $t1) }
}

%op Term h(arg1:Term, arg2:Term, arg3:Term) {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.HTerm); return ok }() }
  get_slot(arg1, t) { $t.Getarg1() }
  get_slot(arg2, t) { $t.Getarg2() }
  get_slot(arg3, t) { $t.Getarg3() }
  get_default(arg1) { `a() }
  get_default(arg2) { `g(_,  _ ) }
  get_default(arg3) { `f( _) }
  make(t0, t1, t2) { term.MakeH($t0, $t1, $t2) }
}

%op Term Emptyl() {
  is_fsym(t) { func() bool { c, ok := ($t).(*term.LTerm); return ok && len(c.Slots) == 0 }() }
  make() { term.EmptyL() }
}

%op Term Consl(Headl:Term, Taill:Term) {
  is_fsym(t) { func() bool { c, ok := ($t).(*term.LTerm); return ok && len(c.Slots) > 0 }() }
  get_slot(Headl, t) { $t.GetHeadl() }
  get_slot(Taill, t) { $t.GetTaill() }
  make(t0, t1) { term.ConsL($t0, $t1) }
}


%oplist Term l(Term*) {
  is_fsym(t) { func() bool { _, ok := ($t).(*term.LTerm); return ok }() }
  make_empty() { term.EmptyL() }
  make_insert(e,l) { term.ConsL($e,$l) }
  get_head(l) { $l.GetHeadl() }
  get_tail(l) { $l.GetTaill() }
  is_empty(l) { $l.IsEmptyl() }
}

func TestDefault1(t *testing.T) {
    assertEquals(t, `f(_).Getarg1(), `a() )
    assertEquals(t, `f( _).Getarg1(), `a() )
    assertEquals(t, `f(_ ).Getarg1(), `a() )
    assertEquals(t, `f( _ ).Getarg1(), `a() )
}

func TestDefault2(t *testing.T) {
    assertEquals(t, `g(a(),_).Getarg1(), `a() )
    assertEquals(t, `g(a(),_).Getarg2(), `b() )
    assertEquals(t, `g(a(), _).Getarg2(), `b() )
    assertEquals(t, `g(a(),_ ).Getarg2(), `b() )
    assertEquals(t, `g(a(), _ ).Getarg2(), `b() )
}

func TestDefault3(t *testing.T) {
    assertEquals(t, `h(_,_,_).Getarg1(), `a() )
    assertEquals(t, `h(_,_,_).Getarg2(), `g(_,_) )
    assertEquals(t, `h(_,_,_).Getarg2(), `g(a(),b()) )
    assertEquals(t, `h(_,_,_).Getarg3(), `f(_) )
    assertEquals(t, `h(_,_,_).Getarg3(), `f(a()) )
}

func TestDefault4(t *testing.T) {
    assertEquals(t, `f(g(_,_)).Getarg1(), `g(_,_) )
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
