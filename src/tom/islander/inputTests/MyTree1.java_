package rbtree;

import java.util.*;

import aterm.*;
import aterm.pure.*;
import rbtree.tree.*;
import rbtree.tree.types.*;

public class Tree1 {

  private Comparator comparator;
  %include { tree/tree.tom }
  
  public Tree1() {
    this.comparator = new MyComparator();
  }

  private Tree makeBlack(Tree t) {
    if(!t.getcolor().isB()) {
      t = `node(B(),t.getlhs(),t.getvalue(),t.getrhs()); 
    }
    return t;
  }

  @Tomrule
  public boolean member(Tree t, Element x) {
    throw("Not yet implemented");
  }

  @Tomrule
  public int card(Tree t) {
    throw("Not yet implemented");
  }
  
  public Tree insert(Tree t, Element x) {
    return makeBlack(ins(t,x));
  }

  @Tomrule
  private Tree ins(Tree t, Element x) {
    throw("Not yet implemented");
  }

  @Tomrule
  public Tree balance(Color color, Tree lhs, Element elt, Tree rhs) {
    throw("Not yet implemented");
  }
  
  @Tomrule
  public Tree balance2(Color color, Tree lhs, Element elt, Tree rhs) {
    throw("Not yet implemented");
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
    System.out.println("Building " + n + " elements in " + (stopChrono-startChrono) + " ms");
      
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      t = insert(t,array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    int size = card(t);
    System.out.println("Building Tree of size = " + size + " in " + (stopChrono-startChrono) + " ms");
    
    TreeSet set = new TreeSet(comparator);
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      set.add(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("Building Set of size = " + set.size() + " in " + (stopChrono-startChrono) + " ms");

  }
  
  public Element en(Element e, int n) {
    Element res = e;
    for(int i=0 ; i<n ; i++) {
      res = `f(res);// new 
    }
    return res;
  }
  
  public final static void main(String[] args) {
    Tree1 test = new Tree1();
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
        System.out.println("o1 :"+o1);
        System.out.println("o2 :"+o2);
        System.out.println("compare: hashCode collision");
        System.exit(1);
      }
      return 1;
    }
  }
}
