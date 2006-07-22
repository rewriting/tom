package gombench;

import gombench.gomgc.spec.types.*;

class GomGC {

  %include { mustrategy.tom }
  %gom {
    module spec
    abstract syntax
    Nat = | Zero()
          | Suc(n:Nat)
          | C(n1:Nat,n2:Nat)
          | F(n1:Nat,n2:Nat,n3:Nat,n4:Nat,n5:Nat)
          | M()
          | N()
          | P()
  }

  public static Nat base = `Zero();

  %strategy Rewrite() extends Identity() {
    visit Nat {
      M() -> { return `Suc(gombench.GomGC.base); }
      N() -> { return gombench.GomGC.base; }
      P() -> { return `Suc(Suc(gombench.GomGC.base)); }
      C(Zero(),y) -> { return `y; }
      C(Suc(x),y) -> { return `Suc(C(x,y)); }
      F(x,y,Suc(z),t,u) -> { return `F(x,y,z,C(t,t),u); } 
      F(x,Suc(y),Zero(),t,u) -> { return `F(x,y,P(),t,t); } 
      F(Suc(x),Zero(),Zero(),t,u) -> {
        return `F(x,N(),P(),Suc(Zero()),Zero());
      }
      F(Zero(),Zero(),Zero(),t,_) -> { return `t; } 

    }
  }

  public static void main(String[] args) {
    
    int intbase = 0;
    try {
      intbase = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java gom.GomGC <base>");
      return;
    }
    base = buildInt(intbase);
    long startChrono = System.currentTimeMillis();
    Nat pre = null;
    Nat post = `F(M(),N(),P(),Zero(),Suc(Zero()));
    while (post != pre) {
      pre = post;
      post = (Nat) `BottomUp(Rewrite()).apply(post);
    }
    System.out.println(intbase+" \t "+
        (System.currentTimeMillis()-startChrono)/1000.);
  }

  public static Nat buildInt(int i) {
    Nat res = `Zero();
    for(int j=0; j<i; j++) {
      res = `Suc(res);
    }
    return res;
  }
}
