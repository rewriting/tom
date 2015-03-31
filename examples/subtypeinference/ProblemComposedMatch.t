/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package subtypeinference;

public class ProblemComposedMatch{
  /*
  %gom {
    module Example
      abstract syntax
      tNat = zero()
          | one()
          | two()
          | suc(m:tNat)
          | square(n:tInt)
          | mult(m1:tNat,m2:tNat)

      natList = nList(tNat*)

      tInt = uminus(m:tNat)
          | plus(n1:tInt,n2:tInt)

      tFloat = div(n1:tInt,n2:tInt)

      tNat <: tInt
      tInt <: tFloat
  }
  */
  
  //-------- Java classes -----------
static class tFloat {
    public String getOperator(){
      return "";
    }
  }

  static class div extends tFloat {
    public tInt n1;
    public tInt n2;
    public div(tInt nn1, tInt nn2) {
      n1 = nn1;
      n2 = nn2;
    }
    public String getOperator() {
      return "div";
    }
    public String toString(){
      return "div(" + n1 + "," + n2 + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof div) {
        div obj = (div) o;
        return n1.equals(obj.n1) && n2.equals(obj.n2);
      }
      return false;
    }
  } 

  static class tInt extends tFloat{
    public String getOperator(){
      return "";
    }
  }

  static class uminus extends tInt {
    public tNat m;
    public uminus(tNat mm) {
      m = mm;
    }
    public String getOperator() {
      return "uminus";
    }
    public String toString(){
      return "uminus(" + m + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof uminus) {
        uminus obj = (uminus) o;
        return m.equals(obj.m);
      }
      return false;
    }
  } 

  static class plus extends tInt {
    static public tInt n1;
    static public tInt n2;
    public plus(tInt nn1, tInt nn2) {
      n1 = nn1;
      n2 = nn2;
    }
    public String getOperator() {
      return "plus";
    }
    public String toString(){
      return "plus(" + n1 + "," + n2 + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof plus) {
        plus obj = (plus) o;
        return n1.equals(obj.n1) && n2.equals(obj.n2);
      }
      return false;
    }
  } 

  static class tNat extends tInt{
    public String getOperator(){
      return "";
    }
  }

  static class zero extends tNat {
    public zero() {}
    public String getOperator() {
      return "zero";
    }
    public String toString() {
      return "zero()";
    }
    public boolean equals(Object o) {
      if(o instanceof zero) {
        return true;
      }
      return false;
    }
  }

  static class one extends tNat {
    public one() {}
    public String getOperator() {
      return "one";
    }
    public String toString() {
      return "one()";
    }
    public boolean equals(Object o) {
      if(o instanceof one) {
        return true;
      }
      return false;
    }
  }

  static class two extends tNat {
    public two() {}
    public String getOperator() {
      return "two";
    }
    public String toString() {
      return "two()";
    }
    public boolean equals(Object o) {
      if(o instanceof two) {
        return true;
      }
      return false;
    }
  }

  static class suc extends tNat {
    public tNat m;
    public suc(tNat mm) {
      m = mm;
    }
    public String getOperator() {
      return "suc";
    }
    public String toString(){
      return "suc(" + m + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof suc) {
        suc obj = (suc) o;
        return m.equals(obj.m);
      }
      return false;
    }
  } 

  static class square extends tNat {
    public tInt n;
    public square(tInt nn) {
      n = nn;
    }
    public String getOperator() {
      return "square";
    }
    public String toString(){
      return "square(" + n + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof square) {
        square obj = (square) o;
        return n.equals(obj.n);
      }
      return false;
    }
  }

  static class mult extends tNat {
    static public tNat m1;
    static public tNat m2;
    public mult(tNat mm1, tNat mm2) {
      m1 = mm1;
      m2 = mm2;
    }
    public String getOperator() {
      return "mult";
    }
    public String toString(){
      return "mult(" + m1 + "," + m2 + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof mult) {
        mult obj = (mult) o;
        return m1.equals(obj.m1) && m2.equals(obj.m2);
      }
      return false;
    }
  }

  static class natList {
    public String getOperator() {
      return "";
    }
  }

  static class nList extends natList {
    public tNat headnList;
    public natList tailnList;
    public nList() {
      headnList = null;
      tailnList = null;
    }
    public nList(tNat headnnList, natList tailnnList) {
      headnList = headnnList;
      tailnList = tailnnList;
    }
    public boolean isEmpty() {
      return (headnList == null && tailnList == null);
    }
    public String getOperator() {
      return "nList";
    }
    public String toString(){
      String result = "nList(";
      if (headnList != null) {
        result += headnList;
      }
      if (tailnList != null) {
        result += ("," + tailnList);
      }
      result += ")";
      return result;
    }
    public boolean equals(Object o) {
      if (o instanceof nList) {
        nList obj = (nList) o;
        if (this.isEmpty() && obj.isEmpty()) {
          return true;
        } else if (!this.isEmpty() && !obj.isEmpty()) {
          return 
            headnList.equals(obj.headnList) && tailnList.equals(obj.tailnList);
        }
      }
      return false;
    } 
  }

  //-------- Tom mappings -----------
  %typeterm tFloat {
    implement { tFloat }
    is_sort(t) { ($t instanceof tFloat) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %typeterm tInt extends tFloat {
    implement { tInt }
    is_sort(t) { ($t instanceof tInt) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %typeterm tNat extends tInt {
    implement { tNat }
    is_sort(t) { ($t instanceof tNat) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %typeterm natList {
    implement { natList }
    is_sort(t) { ($t instanceof natList) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %oplist natList nList( tNat* ) {
    is_fsym(t)         { ($t instanceof nList) }
    get_head(l)        { ((nList)$l).headnList }
    get_tail(l)        { ((nList)$l).tailnList }
    is_empty(l)        { ((nList)$l).isEmpty() }
    make_empty()       { new nList() }
    make_insert(e,l)   { new nList($e,$l) }
  }

  %op tNat zero() {
    is_fsym(t) { ($t instanceof zero) }
    make() { new zero() }
  }

  %op tNat one() {
    is_fsym(t) { ($t instanceof one) }
    make() { new one() }
  }

  %op tNat two() {
    is_fsym(t) { ($t instanceof two) }
    make() { new two() }
  }

  %op tNat suc(m:tNat) {
    is_fsym(t) { ($t instanceof suc) }
    get_slot(m, t) { ((suc)$t).m }
    make(t0) { new suc($t0) }
  }

  %op tNat square(n:tInt) {
    is_fsym(t) { ($t instanceof square) }
    get_slot(n, t) { ((square)$t).n }
    make(t0) {  new square($t0) }
  }

  %op tNat mult(m1:tNat, m2:tNat) {
    is_fsym(t) { ($t instanceof mult) }
    get_slot(m1, t) { ((mult)$t).m1 }
    get_slot(m2, t) { ((mult)$t).m2 }
    make(t0, t1) { new mult($t0, $t1) }
  }

  %op tInt uminus(m:tNat) {
    is_fsym(t) { ($t instanceof uminus) }
    get_slot(m, t) { ((uminus)$t).m }
    make(t0) { new uminus($t0) }
  }

  %op tInt plus(n1:tInt, n2:tInt) {
    is_fsym(t) { ($t instanceof plus) }
    get_slot(n1, t) { ((plus)$t).n1 }
    get_slot(n2, t) { ((plus)$t).n2 }
    make(t0, t1) { new plus($t0, $t1) }
  }

  %op tFloat div(n1:tInt, n2:tInt) {
    is_fsym(t) { ($t instanceof div) }
    get_slot(n1, t) { ((div)$t).n1 }
    get_slot(n2, t) { ((div)$t).n2 }
    make(t0, t1) { new div($t0, $t1) }
  }

  //---------------------------------
  public static void main(String[] args) {
    %match {
      div(x,y) << div(zero(),uminus(zero()))
        && (x != y) -> { System.out.println("#1: x = " +`x); }

      div(x,y) << div(uminus(zero()),zero())
        && suc(zero()) << suc(x) -> { 
          System.out.println("#2: x = " +`x + ", y = " + `y);
        } 

      suc(zero()) << suc(x)
        && div(x,y) << div(uminus(zero()),zero()) -> {
          System.out.println("#3: x = " +`x + ", y = " + `y);
        }
    }
  }
}
