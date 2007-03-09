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

%op TwoPath idS(t:TwoPath) {}
%op TwoPath idT(t:TwoPath) {}
private static TwoPath idS(TwoPath t) { return `id(getPGSource(t)); }
private static TwoPath idT(TwoPath t) { return `id(getPGTarget(t)); }

  public static int getPGSource(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Source=x] -> { return `x; }
      c0() -> { return 0; }
      // We want A here
      c0(head,tail*) -> { if(!`tail*.isEmptyc0()) { return getPGSource(`head) + getPGSource(`tail*); } }

      c1() -> { return 0; }
      // We want A here
      c1(head,tail*) -> { if(!`tail*.isEmptyc1()) { return getPGSource(`head); } }
    }
    throw new RuntimeException("strange term: " + t);
  }

  public static int getPGTarget(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Target=x] -> { return `x; }
      c0() -> { return 0; }
      // We want A here
      c0(head,tail*) -> { if(!`tail*.isEmptyc0()) { return getPGTarget(`head) + getPGTarget(`tail*); } }

      c1() -> { return 0; }
      // We want A here
      c1(head*,last) -> { if(!`head*.isEmptyc1()) { return getPGTarget(`head); } }
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

    System.out.println("two+two = " + `c1(c1(suc,suc)));
    System.out.println("two+two = " + `c1(c1(suc,suc),c1(suc,suc)));


    TwoPath two2 = `c0(id(0),
                       c1(id(0),zero,id(1),id(1),suc,id(1)),id(0),
                       c1(id(0),zero,id(1),id(1),suc,id(1)),id(0),
		       id(0));
    System.out.println("two = " + two2);

    TwoPath res = `c1(
                        c0(c1(dup,
                              c0(suc,id(1))), id(1)),
                        c0(id(1),c1(c0(suc,suc),add)));

//c1(
//   c1(c0(g("dup",1,2),id(1)),
//      c0(c0(g("suc",1,1),id(1)),id(1))),
//   c1(c0(id(1),g("suc",1,1),id(1)),
//      c0(id(2),g("suc",1,1))),
//   c0(id(1),g("add",2,1)))

    System.out.println("res0 = " + res);
    res = (TwoPath) `Repeat(OnceTopDown(Transform())).fire(res);
    System.out.println("res1 = " + res);
    res = (TwoPath) `Repeat(OnceTopDown(Gravity())).fire(res);
    System.out.println("res2 = " + res);

  }

  %strategy Print() extends Identity() {
    visit TwoPath {
      x -> { System.out.println(`x); }
    }
  }

  %strategy Transform() extends Fail() {
    visit TwoPath {
      /*
       * Lifting rule
       */
      c0(X*,f@!id[],Y*,g@!id[],Z*) -> {
	return `c1(c0(X*,f,Y*,idS(g),Z*),
	           c0(idT(X*),idT(f),idT(Y*),g,idT(Z*)));
      }

      /*
       * Vertical Splitting rule
       * C0(id(m),C1(f*,g*),id(n)) -> C1(C0(id(m),f*,id(n)),C0(id(m),g*,id(n)))
       */
      c0(head*, c1(f*,g*), tail*) -> {
	// head and tail should not be both empty
	if(!`head*.isEmptyc0() || !`tail*.isEmptyc0()) {
	  // f*,g* should be a non empty c1 list or a single element
	  if((!`f*.isEmptyc1()) && (!`g*.isEmptyc1())) {
	    // head, tail are either empty or id(m)
	    // idea: use id(m) with m possibily 0
	    // i.e. id(0) is neutral wrt. c0
	    if(isEmptyOrId(`head) && isEmptyOrId(`tail)) {
	      return `c1(c0(head*,f*,tail*),
		         c0(head*,g*,tail*));
	    }
	  }
	}
      }
    }
  }

  %strategy Gravity() extends Fail() {
    visit TwoPath {
      /*
       * Gravity rule
       */
      c1(c0(M*,f,N*),
	 c0(P*,id(m),Q*),
	 tail*) -> {
	if(!`f.isid()) {
	  int sp = getPGSource(`P*);
	  int sq = getPGSource(`Q*);
	  int tm = getPGTarget(`M*);
	  int tn = getPGTarget(`N*);
	  int tf = getPGTarget(`f);
	  if((sp <= tm) && (sp+`m >= tm+tf)) {
	      return `c1(c0(M*,idS(f),N*),
                         c0(P*,id(tm-sp),f*,id(tn-sq),Q*),
                         tail*);
	  }
	}
      }

/* bug anti-pattern
      c1(c0(M*,f@!id[],N*),
	 c0(P*,g@!id[],Q*),
	 tail*) -> {
 */
/*
      c1(c0(M*,f,N*),
	 c0(P*,g,Q*),
	 tail*) -> {
	if(!`f.isid() && !`g.isid()) {
	  if(isEmptyOrId(`M) && isEmptyOrId(`N) && isEmptyOrId(`P) &&isEmptyOrId(`Q)) {
	    int delta = getPGSource(`P) - (getPGTarget(`M) + getPGTarget(`f));
	    if(delta >= 0) {
	      return `c1(c0(M*,f,id(delta),g,Q*),
                         tail*);
	    } 

	    delta = getPGTarget(`M) - (getPGSource(`P) + getPGSource(`g));
	    if(delta>=0) {
	      return `c1(c0(P*,g,id(delta),f,N*),
                         tail*);
	    }
	  }
	}
      }
*/
    }
  }
  private static boolean isEmptyOrId(TwoPath l) {
    %match(l) {
      c0()  -> { return true; }
      id(n) -> { return true; }
    }
    return false;
  }
}
