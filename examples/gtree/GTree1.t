import aterm.*;
import java.util.*;
import jtom.runtime.*;
import jtom.runtime.set.*;
import adt.*;

public class GTree1 {

  private TermFactory factory;
  private GenericTraversal traversal;
  
  %include { term.t }
  %typeint

  public GTree1(TermFactory factory) {
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
    Tree query = `supT(inode(nil,3,nil),ackT(inode(nil,2,nil)));

    long startChrono = System.currentTimeMillis();
    Tree res         = eval(query);
    long stopChrono  = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public Tree eval(Tree tree) {
    %match(Tree tree) {
      supT(ackT(t1),ackT(t2)) -> { return `eval(ackT(eval(supT(t1,t2)))); }

      supT(nil,x) -> { return x; }
      supT(x,nil) -> { return x; }

      supT(node(l1,root1,r1),node(l2,root2,r2)) -> {
        Integer newInt = (root1.compareTo(root2)<0)?root2:root1;
        return `node(eval(supT(l1,l2)),newInt,eval(supT(r1,r2)));
      }

      ackT(x@nil) -> { return x; }
      ackT(node(l1,root1,r1)) -> {
          //Integer newInt = new Integer(ack(2,root1.intValue()));
          //return `node(eval(ackT(l1)),newInt,eval(ackT(r1)));
        return `inode(eval(ackT(l1)),ack(2,root1.intValue()),eval(ackT(r1)));
      }
        
    }
    
    return tree;
  }

    public int ack(int t1, int t2) {
      %match(int t1, int t2) {
        0, x -> { return x+1; }
        p, 0 -> { return p+1; }
        p, x -> { return ack(p-1,ack(p,x-1)); }
      }
    }
  
  public final static void main(String[] args) {
    GTree1 test = new GTree1(new TermFactory(16));

    test.run(0);
  }
  
}
