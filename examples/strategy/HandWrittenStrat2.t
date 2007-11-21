/*
 * Copyright (c) 2004-2007, INRIA
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


import strategy.sl.*;

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

  //modified sl
  %include{string.tom}

  %typeterm Strategy {
    implement { strategy.sl.Strategy }
    is_sort(t) { ($t instanceof strategy.sl.Strategy) }
    equals(t1,t2) { ($t1.equals($t2)) }
    visitor_fwd { strategy.sl.reflective.StrategyFwd }
  }

  %oplist Strategy Sequence(Strategy*) {
    is_fsym(t) {( ($t instanceof strategy.sl.Sequence) )}
    make_empty() {( null )}
    make_insert(head,tail) {( ($tail==null)?$head:new strategy.sl.Sequence($head,$tail) )}
    get_head(t) {( (strategy.sl.Strategy)$t.getChildAt(strategy.sl.Sequence.FIRST) )}
    get_tail(t) {( (strategy.sl.Strategy)$t.getChildAt(strategy.sl.Sequence.THEN) )}
    is_empty(t) {( $t == null )}
  }

  %op Strategy Fail() {
    is_fsym(t) {( ($t instanceof strategy.sl.Fail) )}
    make() {( new strategy.sl.Fail() )}
  }

  %op Strategy All(s1:Strategy) {
    is_fsym(t) {( ($t instanceof strategy.sl.All) )}
    make(v) {( new strategy.sl.All($v) )}
    get_slot(s1, t) {( (strategy.sl.Strategy)$t.getChildAt(strategy.sl.All.ARG) )}
  }

  %op Strategy Identity() {
    is_fsym(t) {( ($t instanceof strategy.sl.Identity) )}
    make() {( new strategy.sl.Identity() )}
  }

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

  //mapping for Visitable (will be generated)
  public static class MyIntrospector implements Introspector {

    //singleton
    private static MyIntrospector mapping = new MyIntrospector();

    private MyIntrospector() {
    }

    public static MyIntrospector getInstance() {
      return mapping;
    } 

    public Object setChildren(Object o, Object[] children) {
      %match(o) {
        f1[] -> { 
          if (children.length == 1) return new f1((Term1) children[0]);
        }
        a1[] -> { 
          if (children.length == 0) return o;       
        }
      }
      %match(o) {
        f2[] -> {
          if (children.length == 1) return new f2((Term2) children[0]);
        }
        a2[] -> { 
          if (children.length == 0) return  o;       
        }
      }
      throw new RuntimeException();
    }

    public Object[] getChildren(Object o) {
      %match(o) {
        f1[] -> { 
          return new Object[] { ((HandWrittenStrat2.f1)o).getx() } ;
        }
        a1[] -> { 
          return new Object[] {};
        }
      }
      %match(o) {
        f2[] -> {
          return new Object[] { ((HandWrittenStrat2.f2)o).getx() } ;
        }
        a2[] -> { 
          return new Object[] {};
        }
      }
      throw new RuntimeException();
    }

    public Object getChildAt(Object o, int i) {
      %match(o) {
        f1[] -> { 
          if(i==0) return (((HandWrittenStrat2.f1)o).getx());
        }
      }
      %match(o) {
        f2[] -> {
          if(i==0) return ((HandWrittenStrat2.f2)o).getx();
        }
      }
      throw new RuntimeException();
    }

    public Object setChildAt(Object o, int i, Object child) {
      %match(o) {
        f1[] -> { 
          if(i==0) return new f1((Term1) child);
        }
      }
      %match(o) {
        f2[] -> { if(i==0) return new f1((Term1) child);
        }
      }
      throw new RuntimeException();
    }

    public int getChildCount(Object o) { 
      %match(o) {
        f1[] -> { return 1;}
        a1[] -> { return 0;}
      }
      %match(o) {
        f2[] -> { return 1;}
        a2[] -> { return 0;}
      }
      throw new RuntimeException(); 
    }

  }

  // mappings

  %typeterm Term1 {
    implement { HandWrittenStrat2.Term1 }  
    is_sort(t) { ($t instanceof HandWrittenStrat2.Term1) } 
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term1 f1(x:Term1) {
    is_fsym(t)        { $t instanceof HandWrittenStrat2.f1 }
    get_slot(x,t)     { ((f1) $t).getx() }
    make(x)           { new f1($x) } 
  }

  %op Term1 a1() {
    is_fsym(t)        { $t instanceof HandWrittenStrat2.a1 }
    make()           { new a1() } 
  }

  %typeterm Term2 {
    implement { HandWrittenStrat2.Term2 }  
    is_sort(t) { $t instanceof HandWrittenStrat2.Term2 } 
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Term2 f2(x:Term2) {
    is_fsym(t)        { $t instanceof HandWrittenStrat2.f2 }
    get_slot(x,t)     { ((f2) $t).getx() }
    make(x)           { new f2($x) } 
  }

  %op Term2 a2() {
    is_fsym(t)        { $t instanceof HandWrittenStrat2.a2 }
    make()            { new a2() } 
  }


  //modify the generated code for:
  /**
    %strategy R() extends Identity() {
    visit Term1 {
    f1(x) -> { return `x; }
    }
    visit Term2 {
    f2(f2(x)) -> { return `x; }
    }
    }
   */

  %op Strategy R() {
    is_fsym(t) {( ($t instanceof R) )}
    make() {( new R() )}
  }

  private static class R extends strategy.sl.BasicStrategy {

    public R() { super(`Identity());}

    public  HandWrittenStrat2.Term1  visit_Term1( HandWrittenStrat2.Term1  tom__arg, Introspector m) throws strategy.sl.VisitFailure {
      %match(tom__arg) {
        f1(x) -> { return `x; }
      }
      return _visit_Term1(tom__arg,m);
    }

    public  HandWrittenStrat2.Term2  visit_Term2( HandWrittenStrat2.Term2  tom__arg, Introspector m) throws strategy.sl.VisitFailure {
      %match(tom__arg) {
        f2(f2(x)) -> { return `x; }
      }
      return _visit_Term2(tom__arg,m);
    }

    public  HandWrittenStrat2.Term1  _visit_Term1( HandWrittenStrat2.Term1  arg, Introspector m) throws strategy.sl.VisitFailure {
      if (!((environment ==  null ))) {
        return (( HandWrittenStrat2.Term1 )any.visit(environment,m));
      } else {
        return (( HandWrittenStrat2.Term1 )any.visitLight(arg,m));
      } 
    }

    public  HandWrittenStrat2.Term2  _visit_Term2( HandWrittenStrat2.Term2  arg, Introspector m) throws strategy.sl.VisitFailure {
      if (!((environment ==  null ))) {
        return (( HandWrittenStrat2.Term2 )any.visit(environment,m));
      } else {
        return (( HandWrittenStrat2.Term2 )any.visitLight(arg,m));
      } 
    }

    public Object visitLight(Object v,Introspector m) throws strategy.sl.VisitFailure {
      if (tom_is_sort_Term1(v)) {
        return visit_Term1((( HandWrittenStrat2.Term1 )v),m);
      }
      if (tom_is_sort_Term2(v)) {
        return visit_Term2((( HandWrittenStrat2.Term2 )v),m);
      }if (!((environment ==  null ))) {
        return any.visit(environment,m);
      } 
      else {
        return any.visitLight(v,m);
      } 
    }


  }


  public static void main(String[] args) throws VisitFailure {
    Term1 t1 = `f1(f1(f1(a1())));
    Term2 t2 = `f2(f2(f2(a2())));
    System.out.println( `All(R()).visit(t1,MyIntrospector.getInstance()) );
    System.out.println( `All(R()).visit(t2, MyIntrospector.getInstance()) );
  }

}
