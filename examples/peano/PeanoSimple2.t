package Peano;

import aterm.*;
import aterm.pure.*;

public class PeanoSimple2 {

  ATermFactory factory;
  AFun fzero, fsuc;
  ATermAppl tzero;

  public PeanoSimple2(ATermFactory factory) {
    this.factory = factory;
    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    tzero = factory.makeAppl(fzero);
  }

  %typeterm term {
    implement           { ATermAppl }
    get_fun_sym(t)      { t.getAFun() }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { t.getArgument(n) }
  }

  %op term zero {
    fsym { fzero }
  }
  
  %op term suc(term) {
    fsym { fsuc }
  }

  public ATermAppl suc(ATermAppl t) {
    return factory.makeAppl(fsuc,t);
  }
  
  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc(y) -> { return suc(plus(x,y)); }
    }
    return null;
  }

  public void run(int n) {
    ATermAppl N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    ATermAppl res = plus(N,N);
    System.out.println("plus(" + n + "," + n + ") = " + res);
  }

  public final static void main(String[] args) {
    PeanoSimple2 test = new PeanoSimple2(new PureFactory());
    test.run(10);
  }
 

}

