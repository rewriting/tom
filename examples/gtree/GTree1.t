import aterm.*;
import aterm.pure.*;
import jtom.runtime.*;
import adt.gtree.term.*;
import adt.gtree.term.types.*;

public class GTree1 {

  private Factory factory;
  private GenericTraversal traversal;
  
  %include { term.tom }

  public GTree1(Factory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  public Factory getTermFactory() {
    return factory;
  }

  public void run(int n) {
    Tree query = `supT(node(nil,3,nil),ackT(node(nil,5,nil)));
    long startChrono = System.currentTimeMillis();
    Tree res         = eval(query);
    long stopChrono  = System.currentTimeMillis();

    System.out.println("res = " + res + " in " + (stopChrono-startChrono) + " ms");
    
  }

  public Tree eval(Tree tree) {
    %match(Tree tree) {
      supT(ackT(t1),ackT(t2)) -> { return `eval(ackT(eval(supT(t1,t2)))); }
      supT(nil(),x) -> { return x; }
      supT(x,nil()) -> { return x; }

      supT(node(l1,root1,r1),node(l2,root2,r2)) -> {
        int max = (root1>root2)?root1:root2;
        return `node(eval(supT(l1,l2)),max,eval(supT(r1,r2)));
      }

      ackT(x@nil()) -> { return x; }
      ackT(node(l1,root1,r1)) -> {
        return `node(eval(ackT(l1)),ack(2,root1),eval(ackT(r1)));
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
    GTree1 test = new GTree1(new Factory(new PureFactory()));

    test.run(0);
  }
  
}
