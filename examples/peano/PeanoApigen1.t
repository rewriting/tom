package Peano;

import aterm.*;
import aterm.pure.PureFactory;
import Peano.peano.*;
import Peano.peano.types.*;

public class PeanoApigen1 {

  private Factory factory;

  %include { peano.tom }
  
  public PeanoApigen1(Factory factory) {
    this.factory = factory;
  }

  public Factory getPeanoFactory() {
    return factory;
  }

  public Nat zero() {
    return getPeanoFactory().makeNat_Zero();
  }

  public Nat suc(Nat n) {
    return getPeanoFactory().makeNat_Suc(n);
  }
  
  public Nat plus(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, zero   -> { return x; }
      x, suc(y) -> { return suc(plus(x,y)); }
    }
    return null;
  }

  public Nat fib(Nat t) {
    %match(Nat t) {
      zero        -> { return suc(zero()); }
      suc(zero)   -> { return suc(zero()); }
      suc(suc(x)) -> { return plus(fib(x),fib(suc(x))); }
    }
    return null;
  }

  public void run(int n) {
    Nat N = zero();
    for(int i=0 ; i<n ; i++) {
      N = suc(N);
    }

    Nat res = fib(N);
    System.out.println("fib(" + n + ") =  " + res);
  }

  public final static void main(String[] args) {
    PeanoApigen1 test = new PeanoApigen1(new Factory(new PureFactory()));
    test.run(10);
  }


}

