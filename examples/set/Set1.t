/*
 * Copyright (c) 2004-2014, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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

package set;

import java.util.*;

import set.jgset.*;
import set.jgset.types.*;

import tom.library.sl.*;

import java.io.*;

//TODO
//In this version, the maximal sharing is not preserved
//replace hashcode() by getUniqueIdentifier()
//when it is implemented in gom

public class Set1 {
  %include{sl.tom}
    // Jean Goubault version 
  private Comparator comparator;
  private int depth;
  private int collisions;
  private final static int[] mask =
  { 1 << 0, 
    1 << 1,
    1 << 2,
    1 << 3,
    1 << 4,
    1 << 5,
    1 << 6,
    1 << 7,
    1 << 8,
    1 << 9,
    1 << 10,
    1 << 11,
    1 << 12,
    1 << 13,
    1 << 14,
    1 << 15,
    1 << 16,
    1 << 17,
    1 << 18,
    1 << 19,
    1 << 20,
    1 << 21,
    1 << 22,
    1 << 23,
    1 << 24,
    1 << 25,
    1 << 26,
    1 << 27,
    1 << 28,
    1 << 29,
    1 << 30,
    1 << 31
  };

  %include { jgset/jgset.tom }
  
  public Set1(int depth) {
    this.comparator = new MyComparator();
    if (depth <= 32) {
      this.depth = depth;
    } else {
      this.depth = 32;
    }
  }

  public JGSet add(Element elt, JGSet t) {
    return override(elt, t, 0);
  }

  public JGSet remove(Element elt, JGSet t) {
    return remove(elt, t, 0);
  }

  public boolean member(Element elt, JGSet t) {
    return member(elt, t, 0);
  }

  public JGSet union(JGSet t1, JGSet t2) {
    return union(t1, t2, 0);
  }

  public JGSet intersection(JGSet t1, JGSet t2) {
    JGSet result = intersection(t1, t2, 0);
    return reworkJGSet(result);
  }
  
  public int card(JGSet t) {
    %match(JGSet t) {
      emptyJGSet()    -> { return 0; }
      singleton(_)    -> { return 1; }
      branch(l, r)    -> { return card(`l) + card(`r); }
    }
    return 0;
  }

  public void topRepartition(JGSet t) {
    %match(JGSet t) {
      branch(l,r) -> { System.out.println("Left branch: "+card(`l)+"\tright branch: "+card(`r));return; }
      _ ->  { System.out.println("topRepartition: No a branch") ;}
    }
  }

    // getHead return the first left inner element found
  public Element getHead(JGSet t) {
    %match(JGSet t) {
      emptyJGSet() -> {
        return null;
      }
      singleton(x) -> {return `x;}
      branch(l,r) -> {
        Element left = getHead(`l);
        if(left != null) {
          return left;
        }
        return getHead(`r);
      }
    }
    return null;
  }

  public JGSet getTail(JGSet t) {
    return remove(getHead(t), t);
  }
  
  /* Simple binary operation skeleton
 private JGSet f(JGSet m1, JGSet m2) {
   %match(JGSet m1, JGSet m2) {
      emptyJGSet, x -> {
        return f2(m2);
      }
      x, emptyJGSet -> {
        return f1(m1);
      }
      singleton(y) , x -> {
        return g2(y, m2);
      }
      x, singleton(y) -> {
        return g1(y, m1)
      }
      branch(l1, r1), branch(l2, r2) -> {
        return `branch(f(l1, l2, level+1), f(r1, r2, level+1));
      }
    }
  }*/

  %strategy reworkJGSetOnce() extends `Identity(){
    visit JGSet {
      branch(emptyJGSet(), s@singleton(_)) -> { return `s; }
      branch(s@singleton(_), emptyJGSet()) -> { return `s; }
      branch(e@emptyJGSet(), emptyJGSet()) -> { return `e; }
    }
  }
  
  private JGSet reworkJGSet(JGSet t) {
    JGSet res = null;
    try {
      res = (JGSet) `RepeatId(TopDown(reworkJGSetOnce())).visitLight(t);
    } catch(VisitFailure e) { 
      System.out.println("failure in reworkJGSet strategy"); 
    }
    return res;
  }
  
  
  /*
  private JGSet reworkJGSet(JGSet t) {
  Replace1 replace = new Replace1() {
        public Element apply(Element t) {
          %match(JGSet t) {
            emptyJGSet() -> {return t;}
            singleton(_) -> {return t;}
            branch(emptyJGSet(), s@singleton(_)) -> {return `s;}
            branch(s@singleton(_), emptyJGSet()) -> {return `s;}
            branch(e@emptyJGSet(), emptyJGSet()) -> {return `e;}
            branch(l1, l2) -> {return `branch(reworkJGSet(l1), reworkJGSet(l2));}
          }
					return traversal.genericTraversal(t,this); 
        }
      };

    JGSet res = (JGSet)replace.apply(t);
    if(res != t) {
      res = reworkJGSet(res);
    }
    return res;
  }
   */

  private JGSet union(JGSet m1, JGSet m2, int level) {
    %match(JGSet m1, JGSet m2) {
      emptyJGSet(), _ -> {
        return m2;
      }

      _, emptyJGSet() -> {
        return m1;
      }

      singleton(y), x -> {
        return override(`y, `x, level);
      }

      x, singleton(y) -> {
        return underride(`y, `x, level);
      }

      branch(l1, r1), branch(l2, r2) -> {
        int l = level+1;
        return `branch(union(l1, l2, l), union(r1, r2, l));
      }
    }
    return null;
  }
  
  private JGSet intersection(JGSet m1, JGSet m2, int level) {
    %match(JGSet m1, JGSet m2) {
      emptyJGSet(), _ -> { return `emptyJGSet(); }
      _, emptyJGSet() -> { return `emptyJGSet(); }
      
      s@singleton(y), x -> {
        if (`member(y, x, level)) {
          return `s;
        } else {
          return `emptyJGSet();
        }
      }

      x, s@singleton(y) -> {
        if (`member(y, x, level)) {
          return `s;
        } else {
          return `emptyJGSet();
        }
      }

      branch(l1, r1), branch(l2, r2) -> {
        int l = level+1;
        return `branch(intersection(l1, l2, l), intersection(r1, r2, l));        
      }
    }
    return null;
  }
  
  public JGSet restriction(JGSet m1, JGSet m2, int level) {
    %match(JGSet m1, JGSet m2) {
      emptyJGSet(), _ -> { return `emptyJGSet(); }
      _, emptyJGSet() -> { return `emptyJGSet(); }
      
      singleton(y), x -> {
        return `remove(y, x, level);
      }

      x, singleton(y) -> {
        if (`member(y, x)) {
          return m2;
        } else {
          return `emptyJGSet();
        }
      }

      branch(l1, r1), branch(l2, r2) -> {
        int l = level+1;
        return `branch(restriction(l1, l2, l), restriction(r1, r2, l));
      }
    }
    return null;
  }
  
  private JGSet remove(Element elt, JGSet t, int level) {
    %match(JGSet t) {
      emptyJGSet()     -> {return t;}

      singleton(x)   -> {
        if (`x == elt) {return `emptyJGSet();}
        else {return t;}
      }

      branch(l, r) -> {
        JGSet l1 = null, r1=null;
        if( isBitZero(elt, level) ) {
          l1 = `remove(elt, l, level+1);
          r1 = `r;
        } else {
          l1 = `l;
          r1 = `remove(elt, r, level+1);
        }
        %match(JGSet l1, JGSet r1) {
          emptyJGSet(), singleton(_) -> {return r1;}
          singleton(_), emptyJGSet() -> {return l1;}
          _, _ -> {return `branch(l1, r1);}
        }
      }
    }
    return null;
  }

  private boolean member(Element elt, JGSet t, int level) {
    %match(JGSet t) {
      emptyJGSet() -> {return false;}
      
      singleton(x) -> {
        if(`x == elt) return true;
      }
      
      branch(l, r) -> {
        if(level == depth) {
          return (`member(elt, l, level) || `member(elt, r, level));
        }
        if( isBitZero(elt, level)) {
          return `member(elt, l, level+1);
        } else {
          return `member(elt, r, level+1);
        }
      }
    }
    return false;
  }
  
  private JGSet override(Element elt, JGSet t, int level) {
    //System.out.println("elt = " + elt + " id = " + elt.getUniqueIdentifier());

    int lev = level+1;
    %match(JGSet t) {
      emptyJGSet()   -> { return `singleton(elt); }

      singleton(x)   -> {
        if(`x == elt) {  
          return `singleton(elt);
        } else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1st list of element as it was a branch
          return `branch(t, singleton(elt));
          
        } else if ( isBitZero(elt, level) && isBitZero(`x, level) ) {
          return `branch(override(elt, t, lev), emptyJGSet());
        } else if ( isBitOne(elt, level)  && isBitOne(`x, level) ) {
          return `branch(emptyJGSet(), override(elt, t, lev));
        } else if ( isBitZero(elt, level) && isBitOne(`x, level) ) {
          return `branch(singleton(elt), t);
        } else if ( isBitOne(elt, level)  && isBitZero(`x, level) ) {
          return `branch(t, singleton(elt));
        }
      }
      
      branch(l, r) -> {
        if(level >= depth) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            //continue list of element
          return `branch(t, singleton(elt));
        }
        if (isBitZero(elt, level)) {
          return `branch(override(elt, l, lev), r);
        } else {
          return `branch(l, override(elt, r, lev));
        }
      }
    }
    System.out.println("Should not be there: set = " + t);
    return null;
  }
  
  private JGSet underride(Element elt, JGSet t, int level) {
    int lev = level+1;
    %match(JGSet t) {
      emptyJGSet()     -> {return `singleton(elt);}

      singleton(x)   -> {
        if(`x == elt) {  return t;}
        else if ( isBitZero(elt, level) && isBitZero(`x, level) )  { return `branch(underride(elt, t, lev), emptyJGSet());}
        else if ( isBitOne(elt, level)  && isBitOne(`x, level) )   { return `branch(emptyJGSet(), underride(elt, t, lev));}
        else if ( isBitZero(elt, level) && isBitOne(`x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBitOne(elt, level)  && isBitZero(`x, level) ){ return `branch(t, singleton(elt));}
      }

      branch(l, r) -> {
        if (isBitZero(elt, level)) {return `branch(underride(elt, l, lev), r);}
        else {return `branch(l, underride(elt, r, lev));}
      }
    }
    return null;
  }

  private boolean isBitZero(Element elt, int position) {
    return ( (elt.getUniqueIdentifier() & mask[position]) == 0);
  }
  
  private boolean isBitOne(Element elt, int position) {
    return ( (elt.getUniqueIdentifier() & mask[position]) > 0);
  }
  
  public final static void main(String[] args) {
    Set1 test;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    int input = 0;
    while(true) {
      try {
        String str = "";
        System.out.print(">Depth:");
        str = in.readLine();
        try {
          input = Integer.parseInt(str, 10);
        }catch (NumberFormatException e) {
          System.out.println("Not a valid number");
          continue;
        }
        if( input == 0 ) System.exit(0);
        test = new Set1(input);
        while(true) {
          try {
            str = "";
            System.out.print(">Nb elements(will be multiply by 3):");
            str = in.readLine();
            try {
              input = Integer.parseInt(str, 10);
            }catch (NumberFormatException e) {
              System.out.println("Not a valid number");
              continue;
            }
            if( input == 0 ) System.exit(0);
            test.run(input);
            break;
          } catch (IOException e) {
          }
        }
      } catch (IOException e) {
      }
    }    
  }

      // Execution 
  public void run(int n) {

    JGSet t0 = `emptyJGSet();
    JGSet t00 = `emptyJGSet();
    JGSet t1 = `emptyJGSet();
    JGSet t2 = `emptyJGSet();
    Element e1 = `e1();
    Element e2 = `e2();
    Element e3 = `e3();
    
    Element array[] = new Element[3*n];
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
    
      // Adding elements to JGSet t
    long startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      t1 = add(array[i], t1);
    }    
    long stopChrono = System.currentTimeMillis();
    int size = card(t1);
    System.out.println("JGSet tree size = " + size + " in " + (stopChrono-startChrono) + " ms");
      // Repartition issue
    topRepartition(t1);

      // getHead and Tail
    System.out.println("getHead de empty: "+getHead(t0));
    t0 = add(e1, t0);
    System.out.println("getHead de t0: "+t0+" = " +getHead(t0));
    System.out.println("getTail de t0: "+getTail(t0));

    startChrono = System.currentTimeMillis();
    Element trm = getHead(t1), trm2 = getHead(t1);
    stopChrono = System.currentTimeMillis();
    if (trm == trm2) {System.out.println("getHead is OK");}
    System.out.println("2 times getHead from tree in: "+ (stopChrono-startChrono) + " ms");
    
    startChrono = System.currentTimeMillis();
    t0 = getTail(t1);
    stopChrono = System.currentTimeMillis();
    if (t0 == remove(trm, t1)) {System.out.println("getTail is OK");}
    System.out.println("getTail from tree in: "+ (stopChrono-startChrono) + " ms");
    
      //Adding element to a java JGSet
    TreeSet treeset = new TreeSet(comparator);
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      treeset.add(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("Java treeset  size = " + treeset.size() + " in " + (stopChrono-startChrono) + " ms");
    
      // Looking for elements in a JGSet
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      if( member(array[i], t1) == false) System.out.println("Loose an element");
    }
    stopChrono = System.currentTimeMillis();
    System.out.println("Found each element of JGSet size = " + size + " in " + (stopChrono-startChrono) + " ms");
    
      // Looking for elements in a java JGSet
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      if( treeset.contains(array[i]) == false) System.out.println("Loose an element");
    }
    stopChrono = System.currentTimeMillis();
    System.out.println("Found each element of java JGSet size = " + size + " in " + (stopChrono-startChrono) + " ms");
    
      //Maximal sharing
    TreeSet treeset2 = new TreeSet(comparator);
    for(int i=3*n-1 ; i>=0 ; i--) {
      t2 = add(array[i], t2);
      treeset2.add(array[i]);
    }
    if (t1 == t2) System.out.println("Maximal sharing is OK");
    
      //Union and intersection
    startChrono = System.currentTimeMillis();
    JGSet t3 = union(t1, t2);
    stopChrono = System.currentTimeMillis();
    if (t1 == t3) System.out.println("Simple union OK");
    System.out.println("Simple union for JGSet in " + (stopChrono-startChrono) + " ms");

    startChrono = System.currentTimeMillis();
    treeset.addAll(treeset2);
    stopChrono = System.currentTimeMillis();
    System.out.println("Simple union for java JGSet in " + (stopChrono-startChrono) + " ms");

     
    startChrono = System.currentTimeMillis();
    JGSet t4 = intersection(t1, t2);
    stopChrono = System.currentTimeMillis();
    if (t1 == t4) System.out.println("Simple intersection OK");
    System.out.println("Simple intersection for JGSet in " + (stopChrono-startChrono) + " ms");

    startChrono = System.currentTimeMillis();
    treeset.containsAll(treeset2);
    stopChrono = System.currentTimeMillis();
    System.out.println("Simple intersection for java JGSet in " + (stopChrono-startChrono) + " ms");
    
    JGSet t5 = `emptyJGSet();
    JGSet t6 = `emptyJGSet();
    JGSet t7 = `emptyJGSet();
    TreeSet treeset5 = new TreeSet(comparator);
    TreeSet treeset6 = new TreeSet(comparator);
    TreeSet treeset7 = new TreeSet(comparator);
    for(int i=0 ; i<2*n ; i++) {
      t5 = add(array[i], t5);
      treeset5.add(array[i]);
    }
    for(int i=n ; i<3*n ; i++) {
      t6 = add(array[i], t6);
      treeset6.add(array[i]);
    }
    for(int i=n ; i<2*n ; i++) {
      t7 = add(array[i], t7);
      treeset6.add(array[i]);
    }
    
    startChrono = System.currentTimeMillis();
    JGSet t8 = union(t5, t6);
    stopChrono = System.currentTimeMillis();
    if (t1 == t8) {System.out.println("Complex union for JGSet in " + (stopChrono-startChrono) + " ms");} else {System.out.println("Complex union failed with \n\tt5:"+t5+" \nand \tt6: "+t6+"\n\tres: "+t8+"\n\tbuilt: "+t1);}

    startChrono = System.currentTimeMillis();
    treeset5.addAll(treeset6);
    stopChrono = System.currentTimeMillis();
    System.out.println("Complex union for java JGSet in " + (stopChrono-startChrono) + " ms");


    startChrono = System.currentTimeMillis();
    JGSet t9 = intersection(t5, t6);
    stopChrono = System.currentTimeMillis();
    if (t7 == t9) {System.out.println("Complex intersection for JGSet in " + (stopChrono-startChrono) + " ms");} else {System.out.println("Complex intersection");}// failed with t5:\t"+t5+"\nt6:\t"+t6+"\nres:\t"+t9+"\nbuilt:\t"+t7);}

    startChrono = System.currentTimeMillis();
    treeset5.containsAll(treeset6);
    stopChrono = System.currentTimeMillis();
    System.out.println("Complex intersection for java JGSet in " + (stopChrono-startChrono) + " ms");
    
  }

  class MyComparator implements Comparator {
    public int compare(Object o1, Object o2) {
      if(o1==o2) {
        return 0;
      }

      int ho1 = ((Element)o1).getUniqueIdentifier();
      int ho2 = ((Element)o2).getUniqueIdentifier();
      
      if(ho1 < ho2) {
        return -1;
      } else if(ho1 > ho2) {
        return 1;
      } else {
        System.out.println("uniqueID collision");
      }
      return 1;
    }
  }

}

