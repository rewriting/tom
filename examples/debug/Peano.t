import aterm.*;
import aterm.pure.*;

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
  
  %op term suc(term) {
    fsym { fsuc }
    make(t) { factory.makeAppl(fsuc,t) }
  }

  public Peano(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    fplus = factory.makeAFun("plus", 2, false);
    tzero = factory.makeAppl(fzero);
  }

  public void run() {
    ATerm N = int2peano(2);
    assertTrue( peano2int(plus(N,N)) == 4 );
  }
  
  public final static void main(String[] args) {
    Peano test = new Peano(new PureFactory(16));
    test.run();
  }

  private ATerm id(ATerm t) {
    return t;
  }
  
  public ATerm plus(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x@_,y@zero()   -> { return `x; }
      x,suc(y) -> { return `suc(plus1(x,y)); }
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
      zero() -> { return 0; }
      suc(x) -> {return 1+`peano2int(x); }
    }
    return 0;
  }
  
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}

