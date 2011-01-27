/*
 * Copyright (c) 2007-2011, INPL, INRIA
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

  public static int s(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Source=x] -> { return `x; }
      //no longer necessary: c0() -> { return 0; }
      // We want A here
      c0(head,tail*) -> { return s(`head) + s(`tail*); } 
      c1() -> { return 0; }
      // We want A here
      c1(head,_*) -> { return s(`head); }
    }
    throw new tom.engine.exception.TomRuntimeException("strange term: " + t);
  }

  public static int t(TwoPath t) {
    %match(t) {
      id(n) -> { return `n; }
      g[Target=x] -> { return `x; }
      //no longer necessary: c0() -> { return 0; }
      // We want A here
      c0(head,tail*) -> { return t(`head) + t(`tail*); }
      c1() -> { return 0; }
      // We want A here
      c1(_*,last) -> { return t(`last); }
    }
    throw new RuntimeException("strange term: " + t);
  }

  public static void main(String[] args) {
    TwoPath zero = `g("zero",0,1);
    TwoPath suc = `g("suc",1,1);
    TwoPath dup = `g("dup",1,2);
    TwoPath add = `g("add",2,1);

    TwoPath res = `c1(
        c0(c1(dup,
            c0(suc,id(1))), id(1)),
        c0(id(1),c1(c0(suc,suc),add)));
    try {
      //System.out.println("res0 = " + res);
      res = (TwoPath) `Repeat(OnceTopDown(Splitting())).visit(res);
      //res = (TwoPath) `Repeat(OnceTopDown(Sequence(Print(),Splitting()))).visit(res);

      //res = `c0(c1(dup, c0(suc,id(1))), id(1));
      //res = (TwoPath) `Splitting().visit(res);
      System.out.println("res1 = " + res);

      res = (TwoPath) `Repeat(OnceTopDown(Gravity())).visit(res);
      System.out.println("res2 = " + res);
    } catch(VisitFailure e) {
      System.out.println("Failure");
    }
  }

  public static TwoPath computeNF(TwoPath res) {
    try {
      res = (TwoPath) `Repeat(OnceTopDown(Splitting())).visit(res);
      res = (TwoPath) `Repeat(OnceTopDown(Gravity())).visit(res);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("strange term: " + res);
    }
    return res;
  }

  %strategy Print() extends Identity() {
    visit TwoPath {
      x -> { System.out.println(`x); }
    }
  }

  %strategy Lifting() extends Fail() {
    visit TwoPath {
      /*
       * Lifting rule
       */
      c0(X*,f@!id[],Y*,g@!id[],Z*) -> {
        return `c1(c0(X*,f,Y*,id(s(g)),Z*),
                   c0(id(t(X*)),id(t(f)),id(t(Y*)),g,id(t(Z*))));
      }
    }
  }

  %strategy Splitting() extends Fail() {
    visit TwoPath {
      /*
       * C0(id(m),g,tail*) -> C1(C0(id(m),g,id(source(tail*)),
       *                         C0(id(m+target(g)),tail*)) g notin tail
       */
      c0(head@id(_), c1(f*,g*), tail*) -> {
        if((!`f*.isEmptyc1()) && (!`g*.isEmptyc1())) {
          return `c1(c0(head,f*,tail*), c0(head,g*,tail*)); 
        }
      }
      c0(head*, c1(f*,g*), tail@id(_)) -> {
        if((!`f*.isEmptyc1()) && (!`g*.isEmptyc1())) {
          return `c1(c0(head*,f*,tail), c0(head*,g*,tail)); 
        }
       }
/*
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
      */
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
          int sp = s(`P*);
          int tm = t(`M*);
          if((sp <= tm) && (sp+`m >= tm+t(`f))) {
            return `c1(c0(M*,id(s(f)),N*),
                c0(P*,id(tm-sp),f*,id(t(N*)-s(Q*)),Q*),
                tail*);
          }
        }
      }
    }
  }

  private static boolean isEmptyOrId(TwoPath l) {
    %match(l) {
      c0()  -> { return true; }
      id(_) -> { return true; }
    }
    return false;
  }
}
