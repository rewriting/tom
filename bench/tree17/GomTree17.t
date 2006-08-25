package tree17;
import tree17.gomtree17.term.types.*;

import java.util.*;

public class GomTree17 {
  %gom {
    module Term
    abstract syntax
		Bool = True() 
		     | False() 
    Nat  = Zero()
         | S(n1:Nat)
    SNat = ExZero()
         | ExS(s1:SNat)
         | ExPlus(s1:SNat,s2:SNat)
         | ExMult(s1:SNat,s2:SNat)
         | ExExp(s1:SNat,s2:SNat)
         | Dec(s1:SNat)
    Tree = Leaf(n1:Nat)
         | Node(n1:Nat,n2:Nat,t3:Tree,t4:Tree)
  }

  public Nat plus(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, Zero() -> { return `x; }
      x, S(y) -> { return `S(plus(x,y)); }
    }
    return null;
  }

  public Nat mult(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, Zero() -> { return `Zero(); }
      x, S(y) -> { return `plus(mult(x,y),x); }
    }
    return null;
  }

  public Nat exp(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      x, Zero() -> { return `S(Zero()); }
      x, S(y) -> { return `mult(x,exp(x,y)); }
    }
    return null;
  }

  public Bool equal(Nat t1, Nat t2) {
    if(t1==t2) {
      return `True();
    } else {
      return `False();
    }
  }

  public Nat succ17(Nat t1) {
    %match(Nat t1) {
      S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(Zero())))))))))))))))) -> { return `Zero(); }
      x -> { return `S(x); }
    }
    return null;
  }

  public Nat pred17(Nat t1) {
    %match(Nat t1) {
      S(x) -> { return `x; }
      Zero() -> { return `S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(S(Zero())))))))))))))))); }
    }
    return null;
  }

  public Nat plus17(Nat arg1, Nat arg2) {
    %match(Nat arg1, Nat arg2) {
      x, Zero()  -> { return `x; }
      x, S(y) -> { return `succ17(plus17(x,y)); }
    }
    return null;
  }

  public Nat mult17(Nat arg1, Nat arg2) {
    %match(Nat arg1, Nat arg2) {
      x, Zero()  -> { return `Zero(); }
      x, S(y) -> { return `plus17(x,mult17(x,y)); }
    }
    return null;
  }

  public Nat exp17(Nat arg1, Nat arg2) {
    %match(Nat arg1, Nat arg2) {
      x, Zero()  -> { return `succ17(Zero()); }
      x, S(y) -> { return `mult17(x,exp17(x,y)); }
    }
    return null;
  }

  public Nat eval(SNat arg1) {
    %match(SNat arg1) {
      ExZero()  -> { return `Zero(); }
      ExS(x)  -> { return `S(eval(x)); }
      ExPlus(x,y)  -> { return `plus(eval(x),eval(y)); }
      ExMult(x,y)  -> { return `mult(eval(x),eval(y)); }
      ExExp(x,y)  -> { return `exp(eval(x),eval(y)); }
    }
    return null;
  }

  public Nat eval17(SNat arg1) {
    %match(SNat arg1) {
      ExZero()  -> { return `Zero(); }
      ExS(x)  -> { return `succ17(eval17(x)); }
      ExPlus(x,y)  -> { return `plus17(eval17(x),eval17(y)); }
      ExMult(x,y)  -> { return `mult17(eval17(x),eval17(y)); }
      ExExp(x,y)  -> { return `exp17(eval17(x),eval17(y)); }
    }
    return null;
  }

  public Nat evalsym17(SNat arg1) {
    %match(SNat arg1) {
      ExZero() -> { return `Zero(); }
      ExS(x) -> { return `succ17(evalsym17(x)); }
      ExPlus(x,y) -> { return `plus17(evalsym17(x),evalsym17(y)); }
      ExMult(x,ExZero()) -> { return `Zero(); }
      ExMult(x,ExS(y)) -> { return `evalsym17(ExPlus(ExMult(x,y),x)); }
      ExMult(x,ExPlus(y,z)) -> { return `evalsym17(ExPlus(ExMult(x,y),ExMult(x,z))); }
      ExMult(x,ExMult(y,z)) -> { return `evalsym17(ExMult(ExMult(x,y),z)); }
      ExMult(x,ExExp(y,z)) -> { return `evalsym17(ExMult(x,dec(ExExp(y,z)))); }
      ExExp(x,ExZero()) -> { return `succ17(Zero()); }
      ExExp(x,ExS(y)) -> { return `evalsym17(ExMult(ExExp(x,y),x)); }
      ExExp(x,ExPlus(y,z)) -> { return `evalsym17(ExMult(ExExp(x,y),ExExp(x,z))); }
      ExExp(x,ExMult(y,z)) -> { return `evalsym17(ExExp(ExExp(x,y),z)); }
      ExExp(x,ExExp(y,z)) -> { return `evalsym17(ExExp(x,dec(ExExp(y,z)))); }
    }
    return null;
  }
  
  public SNat dec(SNat t) {
    System.out.println("dec: " + t);
    %match(SNat t) {
      ExExp(x,ExZ) -> { return `ExS(ExZ); }
      ExExp(x,ExS(y)) -> { return `ExMult(ExExp(x,y),x); }
      ExExp(x,ExPlus(y,z)) -> { return `ExMult(ExExp(x,y),ExExp(x,z)); }
      ExExp(x,ExMult(y,z)) -> { return `dec(ExExp(ExExp(x,y),z)); }
      ExExp(x,ExExp(y,z)) -> { return `dec(ExExp(x, dec(ExExp(y,z)))); }
    }
    return null;
  }

  public Nat evalexp17(SNat x) {
    return eval17(expand(x));
  }

  public SNat expand(SNat arg1) {
    %match(SNat arg1) {
      ExZero() -> { return `ExZero(); } 
      ExS(x) -> { return `ExPlus(ExS(ExZero()),expand(x)); }
      ExPlus(x,y) -> { return `ExPlus(expand(x),expand(y)); }
      ExMult(x,ExZero()) -> { return `ExZero(); }
      ExMult(x,ExS(ExZero())) -> { return `expand(x); }
      ExMult(x,ExPlus(y,z)) -> { return `expand(ExPlus(ExMult(x,y),ExMult(x,z))); }
      ExMult(x,y) -> { return `expand(ExMult(x,expand(y))); }
      ExExp(x,ExZero()) -> { return `ExS(ExZero()); }
      ExExp(x,ExS(ExZero())) -> { return `expand(x); }
      ExExp(x,ExPlus(y,z)) -> { return `expand(ExMult(ExExp(x,y),ExExp(x,z))); }
      ExExp(x,y) -> { return `expand(ExExp(x, expand(y))); }
    }
    return null;
  }
  
  public Nat getval(Tree t) {
    %match(Tree t) {
      Leaf(val) -> { return `val; }
      Node(val,_,_,_) -> { return `val; }
    }
    return null;
  }
  
  public Nat getmax(Tree t) {
    %match(Tree t) {
      Leaf(val) -> { return `val; }
      Node(_,max,_,_) -> { return `max; }
    }
    return null;
  }

  public Tree buildtree(Nat t1, Nat t2) {
    %match(Nat t1, Nat t2) {
      Zero(),val   -> { return `Leaf(val); }
      S(x), y -> {
        Tree left = `buildtree(x, y);	
        Nat max2 = getmax(left);
        Tree right = `buildtree(x, succ17(max2));
        Nat val2 = getval(left);
        Nat val3 = getval(right);
        Nat val  = plus17(val2, val3);
        Nat max  = getmax(right);
        return `Node(val, max, left, right);
      }
    }
    return null;
  }

  public Nat calctree17(Nat x) {
    return `mult17(exp17(S(S(Zero())), pred17(x)), pred17(exp17(S(S(Zero())),x)));
  }

  public SNat int2SNat(int n) {
    SNat N = `ExZero();
    for(int i=0 ; i<n ; i++) {
      N = `ExS(N);
    }
    return N;
  }

  public void run_evalsym17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    SNat n = `ExExp(int2SNat(2),int2SNat(max));
    Nat t1 = eval17(n);
    Nat t2 = evalsym17(n);
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evalsym17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
  }

  public void run_evalexp17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    SNat n = `ExExp(int2SNat(2),int2SNat(max));
    Nat t1 = eval17(n);
    Nat t2 = evalexp17(n);
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evalexp17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
  }

  public void run_evaltree17(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
    Nat n = eval(int2SNat(max));
    Nat t1 = calctree17(n);
    Nat t2 = getval(buildtree(n,`Zero()));
    Bool res = equal(t1,t2);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000. + "\t evaltree17 " + res);
    System.out.println("t1 = " + t1);
    System.out.println("t2 = " + t2);
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
}
