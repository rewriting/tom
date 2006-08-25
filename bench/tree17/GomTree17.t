package tree17;
import tree17.gomtree17.term.types.*;

import java.util.*;

public class GomTree17 {
  %gom {
    module Term
    abstract syntax
		Bool = True() 
		     | False() 
		     | Not(b1:Bool)
		     | Or(b1:Bool, b2:Bool) 
		     | And(b1:Bool, b2:Bool) 
		     | Equal(b1:Bool, b2:Bool) 
		     | Less(b1:Bool, b2:Bool) 
    Nat  = Z()
         | S(n1:Nat)
         | Plus(n1:Nat,n2:Nat)
         | Mult(n1:Nat,n2:Nat)
         | Exp(n1:Nat,n2:Nat)
         | Pred17(n1:Nat)
         | Plus17(n1:Nat,n2:Nat)
         | Mult17(n1:Nat,n2:Nat)
         | Exp17(n1:Nat,n2:Nat)
         | GetMax(t1:Tree)
         | GetVal(t1:Tree)
         | Eval(s1:SNat)
         | Eval17(s1:SNat)
         | EvalSym17(s1:SNat)
    SNat = ExZ()
         | ExOne()
         | ExS(s1:SNat)
         | ExPlus(s1:SNat,s2:SNat)
         | ExMult(s1:SNat,s2:SNat)
         | ExExp(s1:SNat,s2:SNat)
         | Dec(s1:SNat)
         | Expand(s1:SNat)
    Tree = Leaf(n1:Nat)
         | Node(n1:Nat,n2:Nat,t3:Tree,t4:Tree)
         | BuildTree(n1:Nat,n2:Nat)
  }

  public void run(int max) {
    System.out.print(max);
    long startChrono = System.currentTimeMillis();
		//eval(env,crible);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000.);
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
    gomtest.run(max);
  }
}
