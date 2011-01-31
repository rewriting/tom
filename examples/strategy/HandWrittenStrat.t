/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 * 	- Neither the name of the INRIA nor the names of its
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

package strategy;

import tom.library.sl.*;

/**
 * Shows how to hand-write strategies (make sure the option 'autoDispatch' was passed to Tom ) 
 */

public class HandWrittenStrat {

  /**
   * The following class (Term1,f1,a1,Term2,f2,a2)
   * correspond to the following signature :
   *  Term1 = f1(x:Term1)
   *        | a1()
   *
   *  Term2 = f2(x:Term2)
   *        | a2()
   **/

  %include { sl.tom }


  private abstract static class Term1 implements Visitable { }
  private abstract static class Term2 implements Visitable { }

  private static class f1 extends Term1 {
    private Term1 x;
    
    public f1(Term1 x) { this.x = x; }
    public Term1 getx() { return x; }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat.f1) && (((HandWrittenStrat.f1)t).getx() == x); 
    }
    public String toString() { return "f1(" + x + ")"; }

    // Visitable
    public Visitable setChildren(Visitable[] children) {
      if (children.length == 1) return new f1((Term1) children[0]);
      else throw new RuntimeException();
    }
    public Visitable[] getChildren() {
      return new Term1[] { x } ;
    }
    public Visitable getChildAt(int i) {
      if(i==0) return x;
      else throw new RuntimeException();
    }
    public Visitable setChildAt(int i, Visitable child) {
      if(i==0) return new f1((Term1) child);
      else throw new RuntimeException();
    }
    public int getChildCount() { return 1; }
  }

  private static class a1 extends Term1 {
    public a1() { }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat.a1); 
    }
    public String toString() { return "a1()"; }

    // Visitable
    public Visitable setChildren(Visitable[] children) {
      if (children.length != 0) throw new RuntimeException();
      else return this;
    }
    public Visitable[] getChildren() {
      return new Term1[0];
    }
    public Visitable getChildAt(int i) {
      throw new RuntimeException();
    }
    public Visitable setChildAt(int i, Visitable child) {
      throw new RuntimeException();
    }
    public int getChildCount() { return 0; }


  }


  private static class f2 extends Term2 {
    private Term2 x;
    
    public f2(Term2 x) { this.x = x; }
    public Term2 getx() { return x; }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat.f2) && (((HandWrittenStrat.f2)t).getx() == x); 
    }
    public String toString() { return "f2(" + x + ")"; }

    // Visitable
    public Visitable setChildren(Visitable[] children) {
      if (children.length == 1) return new f2((Term2) children[0]);
      else throw new RuntimeException();
    }
    public Visitable[] getChildren() {
      return new Term2[] { x } ;
    }
    public Visitable getChildAt(int i) {
      if(i==0) return x;
      else throw new RuntimeException();
    }
    public Visitable setChildAt(int i, Visitable child) {
      if(i==0) return new f2((Term2) child);
      else throw new RuntimeException();
    }
    public int getChildCount() { return 1; }
  }

  private static class a2 extends Term2 {
    public a2() { }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat.a2); 
    }
    public String toString() { return "a2()"; }

    // Visitable
    public Visitable setChildren(Visitable[] children) {
      if (children.length != 0) throw new RuntimeException();
      else return this;
    }
    public Visitable[] getChildren() {
      return new Term2[0];
    }
    public Visitable getChildAt(int i) {
      throw new RuntimeException();
    }
    public Visitable setChildAt(int i, Visitable child) {
      throw new RuntimeException();
    }
    public int getChildCount() { return 0; }
  }




  // mappings

  %typeterm Term1 {
    implement { HandWrittenStrat.Term1 }  
    is_sort(t) { $t instanceof HandWrittenStrat.Term1 } 
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term1 f1(x:Term1) {
    is_fsym(t)        { $t instanceof HandWrittenStrat.f1 }
    get_slot(x,t)     { ((f1) $t).getx() }
    make(x)           { new f1($x) } 
  }

  %op Term1 a1() {
    is_fsym(t)        { $t instanceof HandWrittenStrat.a1 }
    make()           { new a1() } 
  }

  %typeterm Term2 {
    implement { HandWrittenStrat.Term2 }  
    is_sort(t) { $t instanceof HandWrittenStrat.Term2 } 
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term2 f2(x:Term2) {
    is_fsym(t)        { $t instanceof HandWrittenStrat.f2 }
    get_slot(x,t)     { ((f2) $t).getx() }
    make(x)           { new f2($x) } 
  }

  %op Term2 a2() {
    is_fsym(t)        { $t instanceof HandWrittenStrat.a2 }
    make()            { new a2() } 
  }

  %strategy R() extends Identity() {
    visit Term1 {
      f1(x) -> { return `x; }
    }
    visit Term2 {
      f2(f2(x)) -> { return `x; }
    }
  }

  public static void main(String[] args) throws VisitFailure {
    Term1 t1 = `f1(f1(f1(a1())));
    Term2 t2 = `f2(f2(f2(a2())));
    System.out.println( `InnermostId(R()).visit(t1) );
    System.out.println( `InnermostId(R()).visit(t2) );
  }

}

