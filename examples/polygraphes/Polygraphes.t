/*
 * Copyright (c) 2007-2007, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
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

package polygraphes;

import java.io.*;
import java.util.*;

import tom.library.sl.*;

import polygraphes.polygraphes.*;
import polygraphes.polygraphes.types.*;

public class Polygraphes {

  %include { sl.tom }
  %include { polygraphes/Polygraphes.tom }

  public static int getSource(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Source=x] -> { return `x; }
      c0() -> { return 0; }
      c0(head,tail*) -> { if(!`tail*.isEmptyconcc0()) { return getSource(`head) + getSource(`tail*); } }

      c1() -> { return 0; }
      c1(head,tail*) -> { if(!`tail*.isEmptyconcc1()) { return getSource(`head); } }
    }
    throw new RuntimeException("strange term: " + t);
  }

  public static int getTarget(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Target=x] -> { return `x; }
      c0() -> { return 0; }
      c0(head,tail*) -> { if(!`tail*.isEmptyconcc0()) { return getTarget(`head) + getTarget(`tail*); } }

      c1() -> { return 0; }
      c1(head*,last) -> { if(!`head*.isEmptyconcc1()) { return getTarget(`head); } }
    }
    throw new RuntimeException("strange term: " + t);
  }

  public static void main(String[] args) {
    TwoPath zero = `g("zero",0,1);
    TwoPath suc = `g("suc",1,1);
    TwoPath dup = `g("dup",1,2);
    TwoPath add = `g("add",2,1);
    TwoPath two = `c0(c1(zero,suc),c1(zero,suc));
    System.out.println("two = " + two);
    
    TwoPath two2 = `c0(id(0),
                       c1(id(0),zero,id(1),id(1),suc,id(1)),id(0),
                       c1(id(0),zero,id(1),id(1),suc,id(1)),id(0),
		       id(0));
    System.out.println("two = " + two2);

    TwoPath dupadd = `c1(
	                 c0(c1(dup,
			      c0(suc,id(1))), id(1)),
			 c0(id(1),c1(c0(suc,suc),add)));

//res = c1(concC1(
//c0(concC0(g("dup",1,2),id(1))),
//c0(concC0(g("suc",1,1),id(2))),
//c0(concC0(id(1),g("suc",1,1),id(1))),
//c0(concC0(id(2),g("suc",1,1))),
//c0(concC0(id(1),g("add",2,1)))))

    System.out.println("dup = " + dupadd);
    TwoPath res = (TwoPath) `Repeat(OnceTopDown(Sequence(Transform(),Print()))).fire(dupadd);
    System.out.println("res = " + res);

  }

  %strategy Print() extends Identity() {
    visit TwoPath {
      x -> { System.out.println(`x); }
    }
  }

  %strategy Transform() extends Fail() {
    visit TwoPath {
      /*
       * C0(C1(f,g),C1(h,k)) -> C1(C0(f,h),C0(g,k)) si target(f) = source(g)
       */
      /*
	 c0(concC0( c1(concC1(f*,g*)), c1(concC1(h*,k*)))) -> {
	 if(`f*.isEmptyconcC1() || `g*.isEmptyconcC1() || `h*.isEmptyconcC1() || `k*.isEmptyconcC1() ) {
      // do nothing 
      } else {
      return `c1(concC1( c0(concC0(c12c0(f*),c12c0(h*))), c0(concC0(c12c0(g*),c12c0(k*)))));
      }
      }
       */

      /*
       * C0(id(m),g,tail*) -> C1(C0(id(m),g,id(source(tail*)),
       C0(id(m+target(g)),tail*)) g notin tail
       */
      c0(id(m),g@g[],tail*) -> {
	%match(TwoPath tail) {
	  c0(_*,!id[],_*) -> { 
	    return `c1(c0(id(m),g,           id(getSource(tail*))), 
		       c0(id(m+getTarget(g)),tail*));
	  }
	}
      }

      /*
       * C0(id(m),C1(f*,g*),id(n)) -> C1(C0(id(m),f*,id(n)),C0(id(m),g*,id(n)))
       */
      c0(head*, c1(f*,g*), tail*) -> {
	if(`f*.isEmptyconcc1() || `g*.isEmptyconcc1()) {
	  // do nothing 
	} else {
	  // head, tail are either empty or id(m)
	  if(isEmptyOrId(`head) && isEmptyOrId(`tail)) {
	    return `c1(c0(head*,c12c0(f*),tail*),
		       c0(head*,c12c0(g*),tail*));
	  }
	}
      }

    }
  }

  private static boolean isEmptyOrId(TwoPath l) {
    %match(l) {
      c0()  -> { return true; }
      id(n) -> { return true; }
    }
    return false;
  }

  /* conversion functions */
  %op TwoPath c02c1(l:TwoPath) {}
  static private TwoPath c02c1(TwoPath l) {
    %match(TwoPath l) {
      Emptyc0() -> { return `Emptyc1(); }
      Consc0(head,tail) -> { return `Consc1(head,c02c1(tail)); }
    }
    return null;
  }

  %op TwoPath c12c0(l:TwoPath) {}
  static private TwoPath c12c0(TwoPath l) {
    %match(TwoPath l) {
      Emptyc1() -> { return `Emptyc0(); }
      Consc1(head,tail) -> { return `Consc0(head,c12c0(tail)); }
    }
    return null;
  }
}
