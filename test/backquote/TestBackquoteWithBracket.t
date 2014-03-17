/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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

package backquote;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import backquote.term.types.*;

public class TestBackquoteWithBracket {

%include { string.tom }

%typeterm Term {
  implement { backquote.term.types.Term }
  is_sort(t) { ($t instanceof backquote.term.types.Term) }

  equals(t1,t2) { ($t1==$t2) }

}
%op Term a() {
  is_fsym(t) { ($t instanceof backquote.term.types.term.a) }
  make() { backquote.term.types.term.a.make() }
}

%op Term b() {
  is_fsym(t) { ($t instanceof backquote.term.types.term.b) }
  make() { backquote.term.types.term.b.make() }
}

%op Term c() {
  is_fsym(t) { ($t instanceof backquote.term.types.term.c) }
  make() { backquote.term.types.term.c.make() }
}

%op Term f(arg1:Term) {
  is_fsym(t) { ($t instanceof backquote.term.types.term.f) }
  get_slot(arg1, t) { $t.getarg1() }
  get_default(arg1) { `a[] }
  make(t0) { backquote.term.types.term.f.make($t0) }
}

%op Term g(arg1:Term, arg2:Term) {
  is_fsym(t) { ($t instanceof backquote.term.types.term.g) }
  get_slot(arg1, t) { $t.getarg1() }
  get_slot(arg2, t) { $t.getarg2() }
  get_default(arg1) { `a() }
  get_default(arg2) { `b() }
  make(t0, t1) { backquote.term.types.term.g.make($t0, $t1) }
}

%op Term h(arg1:Term, arg2:Term, arg3:Term) {
  is_fsym(t) { ($t instanceof backquote.term.types.term.h) }
  get_slot(arg1, t) { $t.getarg1() }
  get_slot(arg2, t) { $t.getarg2() }
  get_slot(arg3, t) { $t.getarg3() }
  get_default(arg1) { `a() }
  get_default(arg2) { `g(_,  _ ) }
  get_default(arg3) { `f[] }
  make(t0, t1, t2) { backquote.term.types.term.h.make($t0, $t1, $t2) }
}

%op Term Emptyl() {
  is_fsym(t) { ($t instanceof backquote.term.types.term.Emptyl) }
  make() { backquote.term.types.term.Emptyl.make() }
}

%op Term Consl(Headl:Term, Taill:Term) {
  is_fsym(t) { ($t instanceof backquote.term.types.term.Consl) }
  get_slot(Headl, t) { $t.getHeadl() }
  get_slot(Taill, t) { $t.getTaill() }
  make(t0, t1) { backquote.term.types.term.Consl.make($t0, $t1) }
}


%oplist Term l(Term*) {
  is_fsym(t) { (($t instanceof backquote.term.types.term.Consl) || ($t instanceof backquote.term.types.term.Emptyl)) }
  make_empty() { backquote.term.types.term.Emptyl.make() }
  make_insert(e,l) { backquote.term.types.term.Consl.make($e,$l) }
  get_head(l) { $l.getHeadl() }
  get_tail(l) { $l.getTaill() }
  is_empty(l) { $l.isEmptyl() }
}
  
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestDefaultValue.class.getName());
  }

  @Before
  public void setUp() {
  }

  @Test
  public void testDefault1() {
    assertEquals( `f[arg1=_].getarg1(), `a() );
    assertEquals( `f[arg1= _].getarg1(), `a() );
    assertEquals( `f[arg1=_ ].getarg1(), `a() );
    assertEquals( `f[arg1= _ ].getarg1(), `a() );
    assertEquals( `f[].getarg1(), `a() );
    assertEquals( `f[].getarg1(), `a[] );
  }

  @Test
  public void testDefault2() {
    assertEquals( `g[arg1=a()].getarg1(), `a() );
    assertEquals( `g[arg1=a()].getarg2(), `b() );
    assertEquals( `g[arg1=a[]].getarg2(), `b() );
    assertEquals( `g[arg1=a()].getarg2(), `b() );
    assertEquals( `g[arg1=a(),arg2=_].getarg2(), `b() );
  }

  @Test
  public void testDefault3() {
    assertEquals( `h[].getarg1(), `a() );
    assertEquals( `h[].getarg2(), `g(_,_) );
    assertEquals( `h[].getarg2(), `g[arg1=a(),arg2=b()] );
    assertEquals( `h[].getarg3(), `f[arg1=_] );
    assertEquals( `h[].getarg3(), `f[arg1=a()] );
  }

  @Test
  public void testDefault4() {
    assertEquals( `f[arg1=g[]].getarg1(), `g[] );
  }
}
