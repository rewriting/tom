package fib;
import aterm.*;
import aterm.pure.*;

import java.util.*;

public class ATermFib {
  static AFun fzero = SingletonFactory.getInstance().makeAFun("zero", 0, false);
  static AFun fsuc = SingletonFactory.getInstance().makeAFun("suc" , 1, false);

  %typeterm Nat {
    implement { ATermAppl }
  }

  %op Nat zero() {
    is_fsym(t) { (t.getAFun())==fzero }
    make() { SingletonFactory.getInstance().makeAppl(fzero) }
  }
  
  %op Nat suc(p:Nat) {
    is_fsym(t) { (t.getAFun())==fsuc }
    get_slot(p,t) { (ATermAppl)t.getArgument(0) }
    make(t) { SingletonFactory.getInstance().makeAppl(fsuc,t) }
  }

  public ATermAppl plus(ATermAppl t1, ATermAppl t2) {
    %match(Nat t1, Nat t2) {
      x, zero() -> { return `x; }
      x, suc(y) -> { return `suc(plus(x,y)); }
    }
    return null;
  }

  public ATermAppl fib(ATermAppl t) {
    %match(Nat t) {
      zero()      -> { return `suc(zero()); }
      suc(zero()) -> { return `suc(zero()); }
      suc(suc(x)) -> { return `plus(fib(x),fib(suc(x))); }
    }
    return null;
  }

  public void run(int n) {
    System.out.print(n);
    ATermAppl N = `zero();
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
      System.out.println("Usage: java fib.ATermFib <n>");
      return;
    }
    ATermFib test = new ATermFib();
    test.run(n);
  }
}
