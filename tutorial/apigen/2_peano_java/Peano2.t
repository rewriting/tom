import aterm.*;
import aterm.pure.PureFactory;
import peano.*;
import peano.types.*;

public class Peano2 {

  private Factory factory;

  %include { peano.tom }
  
  public Peano2(Factory factory) {
    this.factory = factory;
  }

  public Factory getPeanoFactory() {
    return factory;
  }

  public Nat plus(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, zero   -> { return x; }
      x, suc(y) -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public Nat fib(Nat t) {
    %match(Nat t) {
      y@zero        -> { return `suc(y); }
      y@suc(zero)   -> { return y; }
      suc(y@suc(x)) -> { return `plus(fib(x),fib(y)); }
    }
    return null;
  }

  public void run(int n) {
    Nat N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }

    Nat res = fib(N);
    System.out.println("fib(" + n + ") =  " + res);
  }

  public final static void main(String[] args) {
    Peano2 test = new Peano2(new Factory(new PureFactory()));
    test.run(10);
  }

}

