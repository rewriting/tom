/*
  
    TOM - To One Matching Compiler
 

*/

import jaterm.api.*;
import jaterm.shared.*;
import java.util.*;

public class JfibT8 {

  private ATermFactory factory;
  
  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;


  %typeterm term {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op term zero {
    fsym { fzero }
    make { factory.makeAppl(fzero) }
  }
  
  %op term suc(term) {
    fsym { fsuc }
    make(t) { factory.makeAppl(fsuc,t) }
  }
  
  %op term plus(term,term) {
    fsym { fplus }
    make(t1,t2) { plus(t1,t2) }
  }
  
  %op term fib(term) {
    fsym { ffib }
    make(t) { fib(t) }
  }

  public JfibT8(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    fplus = factory.makeAFun("plus", 2, false);
    ffib  = factory.makeAFun("fib" , 1, false);
    tzero = factory.makeAppl(fzero);

  }

  public void run(int loop, int n) {
      //System.out.println("run: " + n);
    ATerm N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    long start = System.currentTimeMillis();
    ATerm res = null;
    for(int i=0 ; i<loop; i++) {
      res = fib(N);
    }
    long end   = System.currentTimeMillis();
    System.out.println("fib(" + n + ") in " + (end-start) + " ms");
    System.out.println("result = " + res);
    
      //System.out.println(factory);
  }

  public final static void main(String[] args) {
    JfibT8 test = new JfibT8(new PureFactory(16));
    test.run(1,10);
  }

  public ATerm plus(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero   -> { return(x); }

// ****** erreur ****** suc(y,z) au lieu de suc(y)

      x,suc(y,z) -> { return(suc(plus(x,y))); }

// ****** erreur ******

    }
    return null;
  }

  int cpt = 0;
  public ATerm fib(ATerm t) {
    %match(term t) {
      zero        -> { return(suc(tzero)); }
      suc(zero)   -> {
        return(suc(tzero));
      }
      suc(suc(x)) -> { return(plus(fib(x),fib(suc(x)))); }
    }
    return null;
  }

  public ATerm suc(ATerm t) {
    return (ATerm)factory.makeAppl(fsuc,t);
  }

}

