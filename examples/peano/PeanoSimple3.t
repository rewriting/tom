package Peano;

import aterm.*;
import aterm.pure.*;


public class PeanoSimple3 {
  ATermFactory factory;
  public PeanoSimple3(ATermFactory factory) {
    this.factory = factory;
  }

  %typeterm term {
    implement           { ATermAppl }
    get_fun_sym(t)      { t.getAFun() }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { t.getArgument(n) }
  }

  %op term zero {
    fsym { factory.makeAFun("zero",0,false) }
    make { factory.makeAppl(factory.makeAFun("zero",0,false)) }
  }
  
  %op term suc(term) {
    fsym    { factory.makeAFun("suc",1,false) }
    make(t) { factory.makeAppl(factory.makeAFun("suc",1,false),t) }
  }

  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc(y) -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public void run(int n) {
    ATermAppl N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }
    ATermAppl res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }

  public final static void main(String[] args) {
    PeanoSimple3 test = new PeanoSimple3(new PureFactory());
    test.run(10);
  }
 

}

