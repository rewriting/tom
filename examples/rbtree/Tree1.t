import java.util.*;

import aterm.*;
import aterm.pure.*;
import adt.rbtree.*;

public class Tree1 {

  private TreeFactory factory;
  private Comparator comparator;
  
  %include { adt/rbtree/tree.tom }
  
  public Tree1(TreeFactory factory) {
    this.factory = factory;
    this.comparator = new MyComparator();
  }

  public TreeFactory getTreeFactory() {
    return factory;
  }

  private Tree makeBlack(Tree t) {
    if(!t.getColor().isB()) {
      t = `t.setColor(B());        
    }
    return t;
  }

  public boolean member(Tree t, ATerm x) {
    %match(Tree t) {
      emptyTree -> { return false; }
      node(_,a,y,b) -> {
        int cmp = comparator.compare(x,y);
        if(cmp < 0) {
          return member(a,x);
        } else if(cmp == 0) {
          return true;
        } else {
          return member(b,x);
        }
      }
    }
    return false;
  }

  public int card(Tree t) {
    %match(Tree t) {
      emptyTree -> { return 0; }
      node(color,a,y,b) -> { return 1 + card(a) + card(b); }
    }
    return 0;
  }
  
  public Tree insert(Tree t, ATerm x) {
    return makeBlack(ins(t,x));
  }

  private Tree ins(Tree t, ATerm x) {
    %match(Tree t) {
      emptyTree -> {
        return `node(R,t,x,t);
      }

      node(color,a,y,b) -> {
        int cmp = comparator.compare(x,y);
        if(cmp < 0 ) {
          return balance(color,ins(a,x),y,b);
        } else if(cmp == 0) {
          return t;
        } else {
          return balance(color,a,y,ins(b,x));
        }
      }
    }
    return null;
  }

  public Tree balance(Color color, Tree lhs, ATerm elt, Tree rhs) {
    %match(Color color, Tree lhs, ATerm elt, Tree rhs) {
      B, node(R,node(R,a,x,b),y,c), z, d |
      B, node(R,a,x,node(R,b,y,c)), z, d |
      B, a, x, node(R,node(R,b,y,c),z,d) |
      B, a, x, node(R,b,y,node(R,c,z,d)) -> {
        return `node(R,node(B,a,x,b),y,node(B,c,z,d));
      }
    }
      // no balancing necessary
    return `node(color,lhs,elt,rhs);
  }
  
  public Tree balance2(Color color, Tree lhs, ATerm elt, Tree rhs) {
    %match(Color color, Tree lhs, ATerm elt, Tree rhs) {
        // color flip
      B, node(R,a@node(R,_,_,_),x,b), y, node(R,c,z,d) |
      B, node(R,a,x,b@node(R,_,_,_)), y, node(R,c,z,d) |
      B, node(R,a,x,b), y, node(R,c@node(R,_,_,_),z,d) |
      B, node(R,a,x,b), y, node(R,c,z,d@node(R,_,_,_)) -> {
        return `node(R,node(B,a,x,b),y,node(B,c,z,d));
      }
        // single rotations
      B, node(R,a@node(R,_,_,_),x,b), y, c -> { return `node(B,a,x,node(R,b,y,c)); }
      B, a, x, node(R,b,y,c@node(R,_,_,_)) -> { return `node(B,node(R,a,x,b),y,c); }
        // double rotations
      B, node(R,a,x,node(R,b,y,c)), z, d |
      B, a, x, node(R,node(R,b,y,c),z,d) -> {
        return `node(B,node(R,a,x,b),y,node(R,c,z,d));
      }
    }
      // no balancing necessary
    return `node(color,lhs,elt,rhs);
  }

  
  public void run(int n) {

    Tree t = `emptyTree();
    Element e1 = `e1();
    Element e2 = `e2();
    Element e3 = `e3();

    Element array[] = new Element[3*n];
    long startChrono = System.currentTimeMillis();
    array[0] = e1;
    array[1] = e2;
    array[2] = e3;
    for(int i=1 ; i<n ; i++) {
      Element old_e1 = array[3*i+0-3];
      Element old_e2 = array[3*i+1-3];
      Element old_e3 = array[3*i+2-3];
      array[3*i+0] = `f(old_e1);
      array[3*i+1] = `f(old_e2);
      array[3*i+2] = `f(old_e3);
    }
    long stopChrono = System.currentTimeMillis();
    System.out.println("building " + n + " in " + (stopChrono-startChrono) + " ms");
      
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      t = insert(t,array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    int size = card(t);
    System.out.println("tree size = " + size + " in " + (stopChrono-startChrono) + " ms");
    
    TreeSet set = new TreeSet(comparator);
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      set.add(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("set  size = " + set.size() + " in " + (stopChrono-startChrono) + " ms");

    ATermList list = factory.getPureFactory().makeList();
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      list = list.insert(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("ATlist  size = " + list.getLength() + " in " + (stopChrono-startChrono) + " ms");


    
  }
  
  public Element en(Element e, int n) {
    Element res = e;
    for(int i=0 ; i<n ; i++) {
      res = `f(res);
    }
    return res;
  }
  
  public final static void main(String[] args) {
    Tree1 test = new Tree1(new TreeFactory(new PureFactory()));
    test.run(10000);
  }

  class MyComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      if(o1==o2) {
        return 0;
      }

      int ho1 = o1.hashCode();
      int ho2 = o2.hashCode();
      
      if(ho1 < ho2) {
        return -1;
      } else if(ho1 > ho2) {
        return 1;
      } else {
        System.out.println("compare: hashCode collision");
        System.exit(1);
      }
      return 1;
    }
  }

}

