/*
 * Copyright (c) 2004-2012, INPL, INRIA
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
 *  - Neither the name of the INRIA nor the names of its
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

package sl;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.*;
import tom.library.sl.*;

public class TestIntrospector {


  %include{sl.tom}

  /**
   *  Term = f(x:Term)
   *        | a()
   *  List = l(Term*)
   **/


  private abstract static class Term { }
  
  private static class f extends Term {
    private Term x;

    public f(Term x) { this.x = x; }
    public Term getx() { return x; }
    public boolean equals(Object t) { 
      return (t instanceof f) && (((f)t).getx().equals(x)); 
    }
    public String toString() { return "f(" + x + ")"; }

  }

  private static class a extends Term {
    public a() { }
    public boolean equals(Object t) { 
      return (t instanceof a); 
    }
    public String toString() { return "a()"; }
  }


  // mappings

  %typeterm Term {
    implement     { Term }  
    is_sort(t)    { ($t instanceof Term) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term f(x:Term) {
    is_fsym(t)    { $t instanceof f }
    get_slot(x,t) { ((f) $t).getx() }
    make(x)       { new f($x) } 
  }

  %op Term a() {
    is_fsym(t) { $t instanceof a }
    make()     { new a() } 
  }


  %typeterm List {
    implement { List }
    is_sort(t) { $t instanceof List }
    equals(l1,l2) { $l1.equals($l2) }
  }

  %oparray List l(Term*) {
    is_fsym(t) { $t instanceof List }
    make_empty(n)    { new ArrayList($n) }
    make_append(e,l) { myAdd($e,(ArrayList)$l) }
    get_element(l,n) { ((Term)((ArrayList)$l).get($n)) }
    get_size(l)      { ((ArrayList)$l).size() }
  }

  private static ArrayList myAdd(Object e, ArrayList l) {
    l.add(e);
    return l;
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


  @Test
    public void test1() {
      try {
        Term t = `f(f(f(a())));
        assertEquals(`f(a()), `TopDown(R()).visit(t, new LocalIntrospector()));
      } catch (VisitFailure e) { fail(); }
    }

  @Test
    public void test2() {
      try {
        Term t = `f(a());
        assertEquals(`f(f(a())), `BottomUp(R2()).visit(t, new LocalIntrospector()));
      } catch (VisitFailure e) { fail(); }
    }

  @Test
    public void test3() {
      try {
        Term t = `f(f(f(a())));
        assertEquals(`a(), `InnermostId(R()).visit(t, new LocalIntrospector()));
      } catch (VisitFailure e) { fail(); }
    }


  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestIntrospector.class.getName());
  }

}
