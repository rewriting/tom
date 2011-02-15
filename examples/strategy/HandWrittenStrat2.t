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

public class HandWrittenStrat2 {

  /**
   * The following class (Term1,f1,a1,Term2,f2,a2)
   * correspond to the following signature :
   *  Term1 = f1(x:Term1)
   *        | a1()
   *
   *  Term2 = f2(x:Term2)
   *        | a2()
   **/


  %include{sl.tom}


  private abstract static class Term1 { }
  private abstract static class Term2 { }

  private static class f1 extends Term1 {
    private Term1 x;

    public f1(Term1 x) { this.x = x; }
    public Term1 getx() { return x; }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat2.f1) && (((HandWrittenStrat2.f1)t).getx() == x); 
    }
    public String toString() { return "f1(" + x + ")"; }

  }

  private static class a1 extends Term1 {
    public a1() { }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat2.a1); 
    }
    public String toString() { return "a1()"; }
  }


  private static class f2 extends Term2 {
    private Term2 x;

    public f2(Term2 x) { this.x = x; }
    public Term2 getx() { return x; }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat2.f2) && (((HandWrittenStrat2.f2)t).getx() == x); 
    }
    public String toString() { return "f2(" + x + ")"; }

  }

  private static class a2 extends Term2 {
    public a2() { }
    public boolean equals(Object t) { 
      return (t instanceof HandWrittenStrat2.a2); 
    }
    public String toString() { return "a2()"; }

  }

  // mappings

  %typeterm Term1 {
    implement     { HandWrittenStrat2.Term1 }  
    is_sort(t)    { ($t instanceof HandWrittenStrat2.Term1) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term1 f1(x:Term1) {
    is_fsym(t)    { $t instanceof HandWrittenStrat2.f1 }
    get_slot(x,t) { ((f1) $t).getx() }
    make(x)       { new f1($x) } 
  }

  %op Term1 a1() {
    is_fsym(t) { $t instanceof HandWrittenStrat2.a1 }
    make()     { new a1() } 
  }

  %typeterm Term2 {
    implement     { HandWrittenStrat2.Term2 }  
    is_sort(t)    { ($t instanceof HandWrittenStrat2.Term2) }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term2 f2(x:Term2) {
    is_fsym(t)    { $t instanceof HandWrittenStrat2.f2 }
    get_slot(x,t) { ((f2) $t).getx() }
    make(x)       { new f2($x) } 
  }

  %op Term2 a2() {
    is_fsym(t) { $t instanceof HandWrittenStrat2.a2 }
    make()     { new a2() } 
  }

  //modify the generated code for:
  %strategy R() extends Identity() {
    visit Term1 {
      f1(x) -> { return `x; }
    }
    visit Term2 {
      f2(f2(x)) -> { return `x; }
    }
  }

  %strategy R2() extends Identity() {
    visit Term1 {
      a1() -> f1(a1())
    }
  }

  public static void main(String[] args) throws VisitFailure {
    Term1 t1 = `f1(f1(f1(a1())));
    Term2 t2 = `f2(f2(f2(a2())));
    System.out.println( `All(R()).visit(t1,new LocalIntrospector()) );
    System.out.println( `All(R()).visit(t2, new LocalIntrospector()) );
    System.out.println( `TopDown(R()).visit(t1, new LocalIntrospector()) );
    System.out.println( `TopDown(R()).visit(t2, new LocalIntrospector()) );
    Term1 t3 = `f1(f1(f1(null)));
    System.out.println( `TopDown(R2()).visit(t3, new LocalIntrospector()) );
  }

}
