import aterm.*;
import aterm.pure.*;
import java.util.*;

public class PeanoTestTom {
  private ATermFactory factory;

  private AFun fzero, fsuc, fplus,ffib;
  public ATermAppl tzero;

  %typeterm Nat {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2)  { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op Nat Nat_ConsZero {
    fsym { fzero }
    make { factory.makeAppl(fzero) }
  }
  
  %op Nat Nat_ConsSuc(pred:Nat) {
    fsym { fsuc }
    make(t) { factory.makeAppl(fsuc,t) }
  }

  public PeanoTestTom(ATermFactory factory) {
    this.factory = factory;

    fzero = factory.makeAFun("zero", 0, false);
    fsuc  = factory.makeAFun("suc" , 1, false);
    tzero = factory.makeAppl(fzero);

  }

  public ATermFactory getPeanoFactory() {
    return factory;
  }
  
  public void run(int loop, int n) {
    ATerm N = `Nat_ConsZero();
    for(int i=0 ; i<n ; i++) {
      N = `Nat_ConsSuc(N);
    }

    long start = System.currentTimeMillis();
    ATerm res = null;
    for(int i=0 ; i<loop; i++) {
      res = fib(N);
    }
    long end   = System.currentTimeMillis();

    System.out.println(loop + " x fib(" + n + ") in  " + (end-start) + " ms");
    // System.out.println(res);
    System.out.println(factory);
  }

  public final static void main(String[] args) {
    PeanoTestTom test = new PeanoTestTom(new PureFactory());
    System.err.println("beginning");
    test.run(10,17);
  }

  public ATerm plus(ATerm t1, ATerm t2) {
    %match(Nat t1, Nat t2) {
      x,Nat_ConsZero         -> { return x; }
      x,Nat_ConsSuc[pred=y]  -> { return `Nat_ConsSuc(plus(x,y)); }
    }
    return null;
  }

  public ATerm fib(ATerm t) {
    %match(Nat t) {
      Nat_ConsZero     -> { return`Nat_ConsSuc(Nat_ConsZero); }
      pred@Nat_ConsSuc[pred=Nat_ConsZero]   -> { return pred; }
      Nat_ConsSuc[pred=pred@Nat_ConsSuc[pred=x]] -> { return plus(fib(x),fib(pred)); }
    }
    return null;
  }
 
}

