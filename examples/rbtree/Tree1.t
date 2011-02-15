/*
 * Copyright (c) 2004-2011, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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

  public boolean member(Tree t, Element x) {
    %match(Tree t) {
      emptyTree() -> { return false; }
      node(_,a,y,b) -> {
        int cmp = comparator.compare(`x,`y);
        if(cmp < 0) {
          return `member(a,x);
        } else if(cmp == 0) {
          return true;
        } else {
          return `member(b,x);
        }
      }
    }
    return false;
  }

  public int card(Tree t) {
    %match(Tree t) {
      emptyTree() -> { return 0; }
      node(_,a,_,b) -> { return 1 + card(`a) + card(`b); }
    }
    return 0;
  }
  
  public Tree insert(Tree t, Element x) {
    return makeBlack(ins(t,x));
  }

  private Tree ins(Tree t, Element x) {
    %match(Tree t) {
      emptyTree() -> {
        return `node(R(),t,x,t);
      }

      node(color,a,y,b) -> {
        int cmp = comparator.compare(`x,`y);
        if(cmp < 0 ) {
          return `balance(color,ins(a,x),y,b);
        } else if(cmp == 0) {
          return t;
        } else {
          return `balance(color,a,y,ins(b,x));
        }
      }
    }
    return null;
  }

  public Tree balance(Color color, Tree lhs, Element elt, Tree rhs) {
    %match(Color color, Tree lhs, Element elt, Tree rhs) {
      B(), node(R(),node(R(),a,x,b),y,c), z, d -> { return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), node(R(),a,x,node(R(),b,y,c)), z, d -> { return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), a, x, node(R(),node(R(),b,y,c),z,d) -> { return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
      B(), a, x, node(R(),b,y,node(R(),c,z,d)) -> { return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d)); }
    }
      // no balancing necessary
    return `node(color,lhs,elt,rhs);
  }
  
  public Tree balance2(Color color, Tree lhs, Element elt, Tree rhs) {
    %match(Color color, Tree lhs, Element elt, Tree rhs) {
        // color flip
      B(), node(R(),a@node(R(),_,_,_),x,b), y, node(R(),c,z,d) -> {
        return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b@node(R(),_,_,_)), y, node(R(),c,z,d) -> {
        return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b), y, node(R(),c@node(R(),_,_,_),z,d) -> {
        return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
      B(), node(R(),a,x,b), y, node(R(),c,z,d@node(R(),_,_,_)) -> {
        return `node(R(),node(B(),a,x,b),y,node(B(),c,z,d));
      }
        // single rotations
      B(), node(R(),a@node(R(),_,_,_),x,b), y, c -> { return `node(B(),a,x,node(R(),b,y,c)); }
      B(), a, x, node(R(),b,y,c@node(R(),_,_,_)) -> { return `node(B(),node(R(),a,x,b),y,c); }
        // double rotations
      B(), node(R(),a,x,node(R(),b,y,c)), z, d -> {
        return `node(B(),node(R(),a,x,b),y,node(R(),c,z,d));
      }
      B(), a, x, node(R(),node(R(),b,y,c),z,d) -> {
        return `node(B(),node(R(),a,x,b),y,node(R(),c,z,d));
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

    /*
    ATermList list = SingletonFactory.getInstance().makeList();
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      list = list.insert(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("Building ATlist of size = " + list.getLength() + " in " + (stopChrono-startChrono) + " ms");

*/
    
  }
  
  public Element en(Element e, int n) {
    Element res = e;
    for(int i=0 ; i<n ; i++) {
      res = `f(res);
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
