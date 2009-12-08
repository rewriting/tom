package fib;
import fib.gomfib.peano.types.*;

import java.util.*;

public class GomFib {
  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
  }

  public Nat plus(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, zero() -> { return `x; }
      x, suc(y) -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public Nat fib(Nat t) {
    %match(Nat t) {
      zero()      -> { return `suc(zero()); }
      suc(zero()) -> { return `suc(zero()); }
      suc(suc(x)) -> { return `plus(fib(x),fib(suc(x))); }
    }
    return null;
  }

  public void run(int n) {
    System.out.print(n);
    Nat N = `zero();
    for(int i=0 ; i<n ; i++) {
      N = `suc(N);
    }
    long startChrono = System.currentTimeMillis();
    for(int i=0 ; i<10 ; i++) {
      fib(N);
    }
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000.);
    
  }

  public final static void main(String[] args) {
    int n = 0;
    try {
      n = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java fib.GomFib <n>");
      return;
    }
    GomFib test = new GomFib();
    test.run(n);
  }
}
