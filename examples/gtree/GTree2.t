import aterm.*;
import aterm.pure.*;
import jtom.runtime.*;
import adt.gtree.*;

public class GTree2 {

  private TermFactory factory;
  private GenericTraversal traversal;
  
  %include { adt/gtree/term.tom }

  public GTree2(TermFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public TermFactory getTermFactory() {
    return factory;
  }

  public void run(int n) {
    Tree query = `supT(ackT(node(nil,3,nil)),ackT(node(nil,2,nil)));

    long startChrono = System.currentTimeMillis();
    Tree res         = normalize(query);
    long stopChrono  = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public Tree evalFunction(Tree tree) {
    %match(Tree tree) {
      supT(nil,x) -> { return x; }
      supT(x,nil) -> { return x; }

      supT(node(l1,root1,r1),node(l2,root2,r2)) -> {
        int max = (root1>root2)?root1:root2;
        return `node(supT(l1,l2),max,supT(r1,r2));
      }

      ackT(x@nil) -> { return x; }
      ackT(node(l1,root1,r1)) -> {
        return `node(ackT(l1),ack(2,root1),ackT(r1));
      }
        
    }
    
    return tree;
  }

 
  public Tree simplifyFunction(Tree tree) {
    %match(Tree tree) {
      supT(ackT(t1),ackT(t2)) -> { return `ackT(supT(t1,t2)); }

      _ -> { return (Tree) traversal.genericTraversal(tree,replace); }
    }
    
  }
  
  Replace1 replace = new Replace1() {
      public ATerm apply(ATerm tree) {
        if(tree instanceof Tree) {
          Tree simplifiedTree = simplifyFunction((Tree)tree);
          Tree evaluatedTree = evalFunction(simplifiedTree);
          return evaluatedTree;
        } else {
          return tree;
        }
      }
    };

    // Simplification using a traversal function
  public Tree normalize(Tree t) {
    Tree res = (Tree)replace.apply(t);
    if(res != t) {
      res = normalize(res);
    }
    return res;
  }
  
  public int ack(int t1, int t2) {
      %match(int t1, int t2) {
        0, x -> { return x+1; }
        p, 0 -> { return p+1; }
        p, x -> { return ack(p-1,ack(p,x-1)); }
      }
    }
  
  public final static void main(String[] args) {
    GTree2 test = new GTree2(new TermFactory(new PureFactory()));

    test.run(0);
  }
  
}
