import aterm.*;
import aterm.pure.*;
import java.util.*;

import peano.*;

public class PeanoTest {

  private PeanoFactory factory;

  %include { Peano.tom }
  
  public PeanoTest(PeanoFactory factory) {
    this.factory = factory;
  }

  public PeanoFactory getPeanoFactory() {
    return factory;
  }
  
  public void run(int loop, int n) {
    Nat N = getPeanoFactory().makeNat_ConsZero();
    for(int i=0 ; i<n ; i++) {
      N = getPeanoFactory().makeNat_ConsSuc(N);
    }

    long start = System.currentTimeMillis();
    Nat res = null;
    for(int i=0 ; i<loop; i++) {
      res = fib(N);
    }
    long end   = System.currentTimeMillis();

    System.out.println(loop + " x fib(" + n + ") in  " + (end-start) + " ms");
    // System.out.println(res);
    System.out.println(factory);
  }

  public final static void main(String[] args) {
    PeanoTest test = new PeanoTest(new PeanoFactory());
    System.err.println("beginning");
    test.run(10,17);
  }

  public Nat plus(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x,consZero         -> { return x; }
      x,consSuc[pred=y]  -> { return `consSuc(plus(x,y)); }
    }
    return null;
  }

  public Nat fib(Nat t) {
    %match(Nat t) {
      consZero     -> { return `consSuc(consZero); }
      pred@consSuc[pred=consZero]   -> { return pred; }
      consSuc[pred=pred@consSuc[pred=x]] -> { return plus(fib(x),fib(pred)); }
    }
    return null;
  }
 
}

