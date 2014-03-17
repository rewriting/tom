/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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

public class ProblemSubAnti{
  /*
  %gom {
    module Example
      abstract syntax
      tPInt = zero()
          | one()

      tNInt = uminus(m:tPInt)
      
      tInt = plus(n1:tInt,n2:tInt)


      tPInt <: tInt
      tNInt <: tInt
  }
  */
  
  //-------- Java classes -----------
  static class tInt {
    public String getOperator(){
      return "";
    }
  }

  static class tNInt extends tInt{
    public String getOperator(){
      return "";
    }
  }

  static class tPInt extends tInt{
    public String getOperator(){
      return "";
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

  static class uminus extends tNInt {
    public tPInt m;
    public uminus(tPInt mm) {
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
  
  static class zero extends tPInt {
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

  static class one extends tPInt {
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

  //-------- Tom mappings -----------
  %typeterm tInt{
    implement { tInt }
    is_sort(t) { ($t instanceof tInt) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %typeterm tPInt extends tInt {
    implement { tPInt }
    is_sort(t) { ($t instanceof tPInt) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %typeterm tNInt extends tInt {
    implement { tNInt }
    is_sort(t) { ($t instanceof tNInt) }

    equals(t1,t2) { ($t1.equals($t2)) }

  }

  %op tPInt zero() {
    is_fsym(t) { ($t instanceof zero) }
    make() { new zero() }
  }

  %op tPInt one() {
    is_fsym(t) { ($t instanceof one) }
    make() { new one() }
  }

  %op tNInt uminus(m:tPInt) {
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

  //---------------------------------
  public static void main(String[] args) {
    tInt num = `one();
    %match {
      x << tPInt num -> { System.out.println("Line 1: " + `num); }
      x << tNInt num -> { System.out.println("Line 2: " + `num); }
      !x << tNInt num -> { System.out.println("Line 3: " + `num); }
    }
  }
}
