import aterm.*;
import java.util.*;
import jtom.runtime.*;
import jtom.runtime.set.*;
import adt.*;

public class GTree2 {

  private TermFactory factory;
  private GenericTraversal traversal;
  
  %include { term.t }
  %typeint

  public GTree2(TermFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public TermFactory getTermFactory() {
    return factory;
  }

  Tree inode(Tree l1, int root, Tree r1) {
    Integer number   = new Integer(root);
    return `node(l1,number,r1);
  }
  
  public void run(int n) {
    Tree query = `supT(ackT(inode(nil,3,nil)),ackT(inode(nil,2,nil)));

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
        Integer newInt = (root1.compareTo(root2)<0)?root2:root1;
        return `node(supT(l1,l2),newInt,supT(r1,r2));
      }

      ackT(x@nil) -> { return x; }
      ackT(node(l1,root1,r1)) -> {
        return `inode(ackT(l1),ack(2,root1.intValue()),ackT(r1));
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
    GTree2 test = new GTree2(new TermFactory(16));

    test.run(0);
  }
  
}
