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

public class ProblemAnti{
  /*
  %gom {
    module Example
      abstract syntax
      tNat = a()
          | f(n1:tNat)

      tInt = l(tInt*)

      tNat <: tInt
  }
  */
  
  //-------- Java classes -----------
  static class tInt {
    public String getOperator() {
      return "";
    }
  }

  static class l extends tInt {
    public tInt headL;
    public tInt tailL;
    public l() {
      headL = null;
      tailL = null;
    }
    public l(tInt headL1, tInt tailL1) {
      headL = headL1;
      tailL = tailL1;
    }
    public boolean isEmpty() {
      return (headL == null && tailL == null);
    }
    public String getOperator() {
      return "l";
    }
    public String toString(){
      String result = "l(";
      if (headL != null) {
        result += headL;
      }
      if (tailL != null) {
        result += ("," + tailL);
      }
      result += ")";
      return result;
    }
  } 

  static class tNat extends tInt{
    public String getOperator(){
      return "";
    }
  }

  static class a extends tNat {
    public a() {}
    public String getOperator() {
      return "a";
    }
    public String toString() {
      return "a()";
    }
    public boolean equals(Object o) {
      if(o instanceof a) {
        return true;
      }
      return false;
    }
  }

  static class f extends tNat {
    public tNat n1;
    public f(tNat nn1) {
      n1 = nn1;
    }
    public String getOperator() {
      return "f";
    }
    public String toString(){
      return "f(" + n1 + ")";
    }
    public boolean equals(Object o) {
      if(o instanceof f) {
        f obj = (f) o;
        return n1.equals(obj.n1);
      }
      return false;
    }
  } 

  //-------- Tom mappings -----------
  

  %typeterm tInt {
    implement { tInt }
    is_sort(t) { ($t instanceof tInt) }

    equals(t1,t2) { ($t1==$t2) }

  }

  %typeterm tNat extends tInt {
    implement { tNat }
    is_sort(t) { ($t instanceof tNat) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %oplist tInt l( tInt* ) {
    is_fsym(t)         { ($t instanceof l) }
    get_head(t)        { ((l)$t).headL }
    get_tail(t)        { ((l)$t).tailL }
    is_empty(t)        { ((l)$t).isEmpty() }
    make_empty()       { new l() }
    make_insert(e,t)   { new l($e,$t) }
  }

  %op tNat a() {
    is_fsym(t) { ($t instanceof a) }
    make() { new a() }
  }

  %op tNat f(n1:tNat) {
    is_fsym(t) { ($t instanceof f) }
    get_slot(n1, t) { ((f)$t).n1 }
    make(t0) { new f($t0) }
  }

  //---------------------------------
  public static void main(String[] args) {
    tInt arg = `l(a(),a());
    %match {
      l(x*,y@!f(n),z*) << arg -> { System.out.println("y = " +`y); }
    }

  }
}
