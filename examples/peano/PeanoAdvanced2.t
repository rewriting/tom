package peano;

import aterm.*;
import aterm.pure.*;

public class PeanoAdvanced2 {
  ATermFactory factory;
  public PeanoAdvanced2(ATermFactory factory) {
    this.factory = factory;
  }

  %typeterm term {
    implement           { ATermAppl }
    get_fun_sym(t)      { null }
    cmp_fun_sym(t1,t2)  { false }
    get_subterm(t, n)   { null }
  }

  %op term zero {
    fsym       { /* empty */ }
    is_fsym(t) { t.getAFun() == factory.makeAFun("zero",0,false) }
    make       { factory.makeAppl(factory.makeAFun("zero",0,false)) }
  }
  
  %op term suc(pred:term) {
    fsym             { /* empty */ }
    is_fsym(t)       { t.getAFun() == factory.makeAFun("suc",1,false) }
    get_slot(pred,t) { (ATermAppl)t.getArgument(0) }
    make(t)          { factory.makeAppl(factory.makeAFun("suc",1,false),t) }
  }

  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(term t1, term t2) {
      x,zero        -> { return x; }
      x,suc[pred=y] -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public ATermAppl fib(ATermAppl t) {
    %match(term t) {
      y@zero             -> { return `suc(y); }
      y@suc(zero)        -> { return y; }
      suc[pred=y@suc(x)] -> { return `plus(fib(x),fib(y)); }
    }
    return null;
  }
  
  public void run(int n) {
    ATermAppl N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }
    ATermAppl res = fib(N);
    System.out.println("fib(" + n + ") = " + res);
  }

  public final static void main(String[] args) {
    PeanoAdvanced2 test = new PeanoAdvanced2(new PureFactory());
    test.run(10);
  }
 

}

