import aterm.*;
import aterm.pure.*;

public class Peano {

  private static ATermFactory factory;
  private static AFun fzero, fsuc, fplus;
  public ATermAppl tzero;

  %typeterm term {
    implement { ATerm }
    is_sort(t) { $t instanceof ATerm }
    equals(t1, t2)      { $t1 == $t2}
  }

  %typeterm appl {
    implement { ATermAppl }
    is_sort(t) { $t instanceof ATermAppl }
    equals(t1, t2)      { $t1 == $t2}
  }

  %op term zero() {
    is_fsym(t) { ((ATermAppl)$t).getAFun() == fzero }
    make() { factory.makeAppl(fzero) }
  }
  
  %op term suc(pred:term) {
    is_fsym(t) { ((ATermAppl)$t).getAFun() == fsuc }
    get_slot(pred,t) { ((ATermAppl)$t).getArgument(0)  }
    make(t) { factory.makeAppl(fsuc,$t) }
  }

  %op term plus1(s1:term,s2:term) {
    make(t1,t2) { plus1($t1,$t2) }
  }

  %op term plus2(s1:term,s2:term) {
    make(t1,t2) { plus2($t1,$t2) }
  }

  %op appl term2appl(s1:term) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "term2appl" }
    get_slot(s1,t) { ((ATermAppl)$t).getArgument(0) }
    make(t) { factory.makeAppl(factory.makeAFun("term2appl",1,false),$t) }
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
      assertTrue( peano2int(plus4(N,N)) == (i+i) );
    }
    System.out.println("First loop has successfully terminated.");

    for(int i=0 ; i<15 ; i++) {
      ATerm N = int2peano(i);
      assertTrue( peano2int(fib3(N)) == fibint(i) );
      assertTrue( peano2int(fib4(N)) == fibint(i) );
    }
    System.out.println("Second loop has successfully terminated.");
    
  }

  public final static void main(String[] args) {
    Peano test = new Peano(new PureFactory(16));
    test.run();
    System.out.println("Peano test has successfully terminated.");
  }

  public ATerm plus1(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,(zero|zero)[]   -> { return `x; }
      x,suc(y) -> { return suc(plus1(`x,`y)); }
    }
    //@ assert false;  
    return null;
  }

  public ATerm plus2(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero()   -> { return `x; }
      x,suc[pred=y] -> { return suc(plus2(`x,`y)); }
    }
    return null;
  }

  public ATerm plus3(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,zero()   -> { return `x; }
      x@_,suc[pred=y@z] -> { return suc(plus3(`x,`y)); }
    }
    return null;
  }

    public ATerm plus4(ATerm t1, ATerm t2) {
    %match(term t1, term t2) {
      x,(zero|zero)[]   -> { return `x; }
      x,(suc|suc)[pred=y] -> { return suc(plus1(`x,`y)); }
    }
    return null;
  }
  
  public ATerm fib3(ATerm t) {
    %match(term t) {
      zero()             -> { return `suc(zero()); }
      suc[pred=x@zero()] -> { return `suc(x); }
      suc(suc(x))      -> { return plus3(fib3(`x),fib3(suc(`x))); }
    }
    return null;
  }

  public ATerm fib4(ATerm t) {
    %match(term t) {
      zero()      -> { return `suc(zero()); }
      suc(zero()) -> { return `suc(zero()); }
      suc(suc(x)) -> { return plus3(fib4(`x),fib4(suc(`x))); }
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
      suc(x) -> {return 1 +peano2int(`x); }
    }
    return 0;
  }
  
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }
  
}

