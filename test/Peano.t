import aterm.*;
import aterm.pure.*;
import java.util.*;

public class Peano {

  private ATermFactory factory;
  private AFun fzero, fsuc, fplus;
  public ATermAppl tzero;

  %typeterm term {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
  }

  %op term zero {
    fsym { fzero }
    make { factory.makeAppl(fzero) }
    is_fsym(t) { ((((ATermAppl)t).getAFun()) == fzero)  }
  }
  
  %op term suc(pred:term) {
    fsym { fsuc }
    make(t) { factory.makeAppl(fsuc,t) }
  }

  %op term fib1(term) {
    fsym { factory.makeAFun("fib1" , 1, false) }
    make(t) { fib1(t) }
  }

  %op term fib2(term) {
    fsym { factory.makeAFun("fib2" , 1, false) }
    make(t) { fib2(t) }
  }

  public Peano(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    fplus = factory.makeAFun("plus", 2, false);
    tzero = factory.makeAppl(fzero);
  }

  public void run() {
    for(int i=0 ; i<100 ; i++) {
      ATerm N = int2peano(i);
      assertTrue( peano2int(plus1(N,N)) == (i+i) );
      assertTrue( peano2int(plus2(N,N)) == (i+i) );
      assertTrue( peano2int(plus3(N,N)) == (i+i) );
    }

    for(int i=0 ; i<15 ; i++) {
      ATerm N = int2peano(i);
      assertTrue( peano2int(fib1(N)) == fibint(i) );
      assertTrue( peano2int(fib2(N)) == fibint(i) );
      assertTrue( peano2int(fib3(N)) == fibint(i) );
      assertTrue( peano2int(fib4(N)) == fibint(i) );
    }
    
  }
  
  public final static void main(String[] args) {
    Peano test = new Peano(new PureFactory(16));
    test.run();
  }

  public ATerm plus1(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc(y) -> { return suc(plus1(x,y)); }
    }
    return null;
  }

  public ATerm plus2(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x,suc[pred=y] -> { return suc(plus2(x,y)); }
    }
    return null;
  }

  public ATerm plus3(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero   -> { return x; }
      x@_,suc[pred=y@z] -> { return suc(plus3(x,y)); }
    }
    return null;
  }

  %rule {
    fib1(zero)        -> suc(zero)
    fib1(suc(zero))   -> suc(zero)
    fib1(suc(suc(x))) -> plus1(fib1(x),fib1(suc(x)))
  }
  
  %rule {
    fib2(zero) | fib2(x@suc[pred=zero]) -> suc(zero)
    fib2(suc(y@suc(x)))    -> plus2(fib2(x),fib2(y))
  }

  public ATerm fib3(ATerm t) {
    %match(term t) {
      zero             -> { return `suc(zero); }
      suc[pred=x@zero] -> { return `suc(x); }
      suc(suc(x))      -> { return plus3(fib3(x),fib3(suc(x))); }
    }
    return null;
  }

  public ATerm fib4(ATerm t) {
    %match(term t) {
      zero | suc(zero) -> { return `suc(zero); }
      suc(suc(x))      -> { return plus3(fib4(x),fib4(suc(x))); }
    }
    return null;
  }

  public ATerm suc(ATerm t) {
    return (ATerm)factory.makeAppl(fsuc,t);
  }

  public int fibint(int n) {
    if(n<=1) {
      return 1;
    } else {
      return fibint(n-1)+fibint(n-2);
    }
  }
  
  public ATerm int2peano(int n) {
    ATerm N = tzero;
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }
    return N;
  }

  public int peano2int(ATerm N) {
    %match(term N) {
      zero -> { return 0; }
      suc(x) -> {return 1+peano2int(x); }
    }
    return 0;
  }
  
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}

