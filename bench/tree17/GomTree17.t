package tree17;
import tree17.gomtree17.term.types.*;

import java.util.*;

public class GomTree17 {
  %gom {
    module Term
    abstract syntax
    Bool = TRUE() 
         | FALSE() 
    Nat  = ZERO()
         | S(n1:Nat)
         | PLUS(n1:Nat,n2:Nat)
         | MULT(n1:Nat,n2:Nat)
         | EXP(n1:Nat,n2:Nat)
         | PRED17(n1:Nat)
         | PLUS17(n1:Nat,n2:Nat)
         | MULT17(n1:Nat,n2:Nat)
         | EXP17(n1:Nat,n2:Nat)
         | GETMAX(t1:Tree)
         | GETVAL(t1:Tree)
         | EVAL(s1:SNat)
         | EVAL17(s1:SNat)
         | EVALSYM17(s1:SNat)
    SNat = EXZERO()
         | EXS(s1:SNat)
         | EXPLUS(s1:SNat,s2:SNat)
         | EXMULT(s1:SNat,s2:SNat)
         | EXEXP(s1:SNat,s2:SNat)
         | DEC(s1:SNat)
         | EXPAND(s1:SNat)
         | EXONE()
    Tree = LEAF(n1:Nat)
         | NODE(n1:Nat,n2:Nat,t3:Tree,t4:Tree)
         | BUILDTREE(n1:Nat,n2:Nat)
  }

  public Bool equal(Nat t1, Nat t2) {
    if(t1==t2) {
      return `TRUE();
    } else {
      return `FALSE();
    }
  }

  public SNat int2SNat(int n) {
    SNat N = `EXZERO();
    for(int i=0 ; i<n ; i++) {
      N = `EXS(N);
    }
    return N;
  }

  public void run_evalsym17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    SNat n = `EXEXP(int2SNat(2),int2SNat(max));
    Nat t1 = EVAL17(n);
    Nat t2 = EVALSYM17(n);
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t gom evalsym17 " + res);
    //System.out.println("t1 = " + t1);
    //System.out.println("t2 = " + t2);
  }

  public void run_evalexp17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    SNat n = `EXEXP(int2SNat(2),int2SNat(max));
    Nat t1 = EVAL17(n);
    Nat t2 = EVALEXP17(n);
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t gom evalexp17 " + res);
    //System.out.println("t1 = " + t1);
    //System.out.println("t2 = " + t2);
  }

  public void run_evaltree17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    Nat n = EVAL(int2SNat(max));
    Nat t1 = calctree17(n);
    Nat t2 = GETVAL(BUILDTREE(n,`ZERO()));
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t gom evaltree17 " + res);
    //System.out.println("t1 = " + t1);
    //System.out.println("t2 = " + t2);
  }

  public final static void main(String[] args) {
    int max = 0;
    try {
      max = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java tree17.GomTree17 <max>");
      return;
    }
    GomTree17 gomtest = new GomTree17();
    gomtest.run_evalsym17(max);
    gomtest.run_evalexp17(max);
    gomtest.run_evaltree17(max);
  }
  
  public Tree BUILDTREE(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      ZERO(),Val   -> { return `LEAF(Val); }
      S(X), Y -> {
        Tree Left = BUILDTREE(`X, `Y);	
        Nat Max2 = GETMAX(Left);
        Tree Right= BUILDTREE(`X, succ17(Max2));
        Nat Val2 = GETVAL(Left);
        Nat Val3 = GETVAL(Right);
        Nat Val  = PLUS17(Val2, Val3);
        Nat Max  = GETMAX(Right);
        return `NODE(Val, Max, Left, Right);
      }
    }
    return null;
  }

  %rule {
    PLUS(x,ZERO()) -> x 
    PLUS(x,S(y)) -> S(PLUS(x,y)) 
  }
  
  %rule {
    MULT(x,ZERO()) -> ZERO()
    MULT(x,S(y)) -> PLUS(MULT(x,y),x) 
  }
  
  %rule {
    EXP(x,ZERO()) -> S(ZERO()) 
    EXP(x,S(y)) -> MULT(x,EXP(x,y)) 
  }
  
  public static Nat succ17(Nat arg) {
    %match(Nat arg) {
      S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(ZERO())))))))))))))))) -> { return `ZERO(); }
      x -> { return `S(x); }
    }
    return null;
  }

  
  %rule {
    PRED17(S(x)) -> x 
    PRED17(ZERO()) -> S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(ZERO())))))))))))))))) 
  }
  
  %rule {
    PLUS17(x,ZERO()) -> x 
    PLUS17(x,S(y)) -> succ17(PLUS17(x,y)) 
  }

    
  %rule {
    MULT17(x,ZERO()) -> ZERO()
    MULT17(x,S(y)) -> PLUS17(x,MULT17(x,y)) 
  }
  
  %rule {
    EXP17(x,ZERO()) -> succ17(ZERO()) 
    EXP17(x,S(y)) -> MULT17(x,EXP17(x,y)) 
  }
  
  %rule {
    EVAL(EXZERO()) -> ZERO()
    EVAL(EXS(xs)) -> S(EVAL(xs)) 
    EVAL(EXPLUS(xs,ys)) -> PLUS(EVAL(xs), EVAL(ys)) 
    EVAL(EXMULT(xs,ys)) -> MULT(EVAL(xs), EVAL(ys)) 
    EVAL(EXEXP(xs,ys)) -> EXP(EVAL(xs), EVAL(ys)) 
  }
  
  %rule {
    EVALSYM17(EXZERO()) -> ZERO()
    EVALSYM17(EXS(Xs)) -> succ17(EVALSYM17(Xs)) 
    EVALSYM17(EXPLUS(Xs,Ys)) -> PLUS17(EVALSYM17(Xs),EVALSYM17(Ys)) 
    EVALSYM17(EXMULT(Xs,EXZERO())) -> ZERO()
    EVALSYM17(EXMULT(Xs,EXS(Ys))) -> EVALSYM17(EXPLUS(EXMULT(Xs,Ys),Xs)) 
    EVALSYM17(EXMULT(Xs,EXPLUS(Ys,Zs))) -> EVALSYM17(EXPLUS(EXMULT(Xs,Ys),EXMULT(Xs,Zs))) 
    EVALSYM17(EXMULT(Xs,EXMULT(Ys,Zs))) -> EVALSYM17(EXMULT(EXMULT(Xs,Ys),Zs)) 
    EVALSYM17(EXMULT(Xs,EXEXP(Ys,Zs))) -> EVALSYM17(EXMULT(Xs,DEC(EXEXP(Ys,Zs)))) 
    EVALSYM17(EXEXP(Xs,EXZERO())) -> succ17(ZERO()) 
    EVALSYM17(EXEXP(Xs,EXS(Ys))) -> EVALSYM17(EXMULT(EXEXP(Xs,Ys),Xs)) 
    EVALSYM17(EXEXP(Xs,EXPLUS(Ys,Zs))) -> EVALSYM17(EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs))) 
    EVALSYM17(EXEXP(Xs,EXMULT(Ys,Zs))) -> EVALSYM17(EXEXP(EXEXP(Xs,Ys),Zs)) 
    EVALSYM17(EXEXP(Xs,EXEXP(Ys,Zs))) -> EVALSYM17(EXEXP(Xs,DEC(EXEXP(Ys,Zs))))  
  }

  public Nat EVALEXP17(SNat Xs) {
    return EVAL17(EXPAND(Xs));
  }
    
  %rule {
      DEC(EXEXP(Xs,EXZERO)) -> EXS(EXZERO)
      DEC(EXEXP(Xs,EXS(Ys))) -> EXMULT(EXEXP(Xs,Ys),Xs)
      DEC(EXEXP(Xs,EXPLUS(Ys,Zs))) -> EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs))
      DEC(EXEXP(Xs,EXMULT(Ys,Zs))) -> DEC(EXEXP(EXEXP(Xs,Ys),Zs))
      DEC(EXEXP(Xs,EXEXP(Ys,Zs))) -> DEC(EXEXP(Xs, DEC(EXEXP(Ys,Zs)))) 
  }
  %rule {
    EVAL17(EXONE()) -> S(ZERO()) 
    EVAL17(EXZERO()) -> ZERO()
    EVAL17(EXS(xs)) -> succ17(EVAL17(xs))
    EVAL17(EXPLUS(xs,ys)) -> PLUS17(EVAL17(xs), EVAL17(ys))
    EVAL17(EXMULT(xs,ys)) -> MULT17(EVAL17(xs), EVAL17(ys))
    EVAL17(EXEXP(xs,ys)) -> EXP17(EVAL17(xs), EVAL(ys))
  }

  %rule {
    EXPAND(EXZERO()) -> EXZERO() 
    EXPAND(EXONE()) -> EXONE() 
    EXPAND(EXS(Xs)) -> EXPLUS(EXONE(),EXPAND(Xs)) 
    EXPAND(EXPLUS(Xs,Ys)) -> EXPLUS(EXPAND(Xs),EXPAND(Ys)) 
    EXPAND(EXMULT(Xs,EXZERO())) -> EXZERO() 
    EXPAND(EXMULT(Xs,EXONE())) -> EXPAND(Xs) 
    EXPAND(EXMULT(Xs,EXPLUS(Ys,Zs))) -> EXPAND(EXPLUS(EXMULT(Xs,Ys),EXMULT(Xs,Zs))) 
    EXPAND(EXMULT(Xs,Ys)) -> EXPAND(EXMULT(Xs,EXPAND(Ys))) 
    EXPAND(EXEXP(Xs,EXZERO())) -> EXONE() 
    EXPAND(EXEXP(Xs,EXONE())) -> EXPAND(Xs) 
    EXPAND(EXEXP(Xs,EXPLUS(Ys,Zs))) -> EXPAND(EXMULT(EXEXP(Xs,Ys),EXEXP(Xs,Zs))) 
    EXPAND(EXEXP(Xs,Ys)) -> EXPAND(EXEXP(Xs, EXPAND(Ys))) 
  }

  %rule {
    GETVAL(LEAF(Val)) -> Val
    GETVAL(NODE(Val,Max,Left,Right)) -> Val
  }

  %rule {
    GETMAX(LEAF(Val)) -> Val
    GETMAX(NODE(Val,Max,Left,Right)) -> Max
  }

  public Nat calctree17(Nat X) {
    return MULT17(EXP17(`S(S(ZERO())), PRED17(X)),PRED17(EXP17(`S(S(ZERO())),X)));
  }
}
