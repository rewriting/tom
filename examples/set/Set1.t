import java.util.*;

import aterm.*;
import adt.*;

import jtom.runtime.*;
import java.io.*;

public class Set1 {
    // Jean Goubault version 
  private SetFactory factory;
  private Comparator comparator;
  private GenericTraversal traversal;
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
    1 << 30
  };
  
  %include { set.t }
  
  public Set1(SetFactory factory) {
    this.factory = factory;
    this.comparator = new MyComparator();
    this.traversal = new GenericTraversal();
    this.depth = 31;
  }
  
  public Set1(SetFactory factory, int depth) {
    this.factory = factory;
    this.comparator = new MyComparator();
    this.traversal = new GenericTraversal();
    this.depth = depth;
  }
  public SetFactory getSetFactory() {
    return factory;
  }

  public JGSet add(ATerm elt, JGSet t) {
    return override(elt, t, 0);
  }

  public JGSet remove(ATerm elt, JGSet t) {
    return remove(elt, t, 0);
  }

  public boolean member(ATerm elt, JGSet t) {
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
      emptySet    -> { return 0; }
      singleton(x) -> { return 1; }
      branch(l, r) -> {return card(l) + card(r);}
    }
    return 0;
  }

  /* Simple binary operation skeleton
 private JGSet f(JGSet m1, JGSet m2) {
   %match(JGSet m1, JGSet m2) {
      emptySet, x -> {
        return f2(m2);
      }
      x, emptySet -> {
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

  private JGSet reworkJGSet(JGSet t) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm t) {
          %match(JGSet t) {
            emptySet -> {return t;}
            singleton(x) -> {return t;}
            branch(emptySet, s@singleton(x)) -> {return s;}
            branch(s@singleton(x), emptySet) -> {return s;}
            branch(e@emptySet, emptySet) -> {return e;}
            branch(l1, l2) -> {return `branch(reworkJGSet(l1), reworkJGSet(l2));}
            _ -> { return traversal.genericTraversal(t,this); }
          }
        }
      };
    
    JGSet res = (JGSet)replace.apply(t);
    if(res != t) {
      res = reworkJGSet(res);
    }
    return res;
  }
  
  private JGSet union(JGSet m1, JGSet m2, int level) {
    %match(JGSet m1, JGSet m2) {
      emptySet, x -> {
        return m2;
      }

      x, emptySet -> {
        return m1;
      }

      singleton(y), x -> {
        return override(y, x, level);
      }

      x, singleton(y) -> {
        return underride(y, x, level);
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
      emptySet, x |
        x, emptySet -> { 
        return `emptySet();
      }
      
      s@singleton(y), x |
      x, s@singleton(y) -> {
        if (member(y, x, level)) {
          return s;
        } else {
          return `emptySet();
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
      emptySet, x |
      x, emptySet -> { 
        return `emptySet();
      }
      
      singleton(y), x -> {
        return remove(y, x, level);
      }

      x, singleton(y) -> {
        if (member(y, x)) {
          return m2;
        } else {
          return `emptySet();
        }
      }

      branch(l1, r1), branch(l2, r2) -> {
        int l = level+1;
        return `branch(restriction(l1, l2, l), restriction(r1, r2, l));
      }
    }
    return null;
  }
  
  private JGSet remove(ATerm elt, JGSet t, int level) {
    %match(JGSet t) {
      emptySet      -> {return t;}

      singleton(x)   -> {
        if (x == elt) {return `emptySet();}
        else {return t;}
      }

      branch(l, r) -> {
        JGSet l1 = null, r1=null;
        if( isBitNullAt(elt, level) ) {
          l1 = remove(elt, l, level+1);
          r1 = r;
        } else {
          l1 = l;
          r1 = remove(elt, r, level+1);
        }
        %match(JGSet l1, JGSet r1) {
          emptySet, singleton(x) -> {return r1;}
          singleton(x), emptySet -> {return l1;}
          _, _ -> {return `branch(l1, r1);}
        }
      }
    }
    return null;
  }

  private boolean member(ATerm elt, JGSet t, int level) {
    %match(JGSet t) {
      emptySet -> {return false;}
      
      singleton(x) -> {
        if(x == elt) return true;
      }
      
      branch(l, r) -> {
        if(level == depth) {
          return (member(elt, l, level) || member(elt, r, level));
        }
        if( isBitNullAt(elt, level)) {
          return member(elt, l, level+1);
        } else {
          return member(elt, r, level+1);
        }
      }
    }
    return false;
  }
  
  private JGSet override(ATerm elt, JGSet t, int level) {
    int lev = level+1;
    %match(JGSet t) {
      emptySet      -> {return `singleton(elt);}

      singleton(x)   -> {
        if(x == elt) {  return `singleton(elt);}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitNullAt(elt, level) && isBitNullAt(x, level) )  { return `branch(override(elt, t, lev), emptySet);}
        else if ( isBit1At(elt, level)  && isBit1At(x, level) )   { return `branch(emptySet, override(elt, t, lev));}
        else if ( isBitNullAt(elt, level) && isBit1At(x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBit1At(elt, level)  && isBitNullAt(x, level) ){ return `branch(t, singleton(elt));}
      }
      
      branch(l, r) -> {
        if(level >= depth) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            //continue list of element
          return `branch(t, singleton(elt));
        }
        if (isBitNullAt(elt, level)) {
          return `branch(override(elt, l, lev), r);
        } else {
          return `branch(l, override(elt, r, lev));
        }
      }
    }
    return null;
  }
  
  private JGSet underride(ATerm elt, JGSet t, int level) {
    int lev = level+1;
    %match(JGSet t) {
      emptySet      -> {return `singleton(elt);}

      singleton(x)   -> {
        if(x == elt) {  return t;}
        else if ( isBitNullAt(elt, level) && isBitNullAt(x, level) )  { return `branch(underride(elt, t, lev), emptySet);}
        else if ( isBit1At(elt, level)  && isBit1At(x, level) )   { return `branch(emptySet, underride(elt, t, lev));}
        else if ( isBitNullAt(elt, level) && isBit1At(x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBit1At(elt, level)  && isBitNullAt(x, level) ){ return `branch(t, singleton(elt));}
      }

      branch(l, r) -> {
        if (isBitNullAt(elt, level)) {return `branch(underride(elt, l, lev), r);}
        else {return `branch(l, underride(elt, r, lev));}
      }
    }
    return null;
  }

  private boolean isBitNullAt(ATerm elt, int i) {
    return ( (elt.hashCode() & mask[i]) == 0);
  }
  
  private boolean isBit1At(ATerm elt, int i) {
    return ( (elt.hashCode() & mask[i]) > 0);
  }
    // Execution 
  public void run(int n) {

    JGSet t1 = `emptySet();
    JGSet t2 = `emptySet();
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
    System.out.println("tree size = " + size + " in " + (stopChrono-startChrono) + " ms =>"+t1);

/*      //Adding element to a java Set
    TreeSet set = new TreeSet(comparator);
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      set.add(array[i]);
    }    
    stopChrono = System.currentTimeMillis();
    System.out.println("set  size = " + set.size() + " in " + (stopChrono-startChrono) + " ms");
*/
 
      // Looking for elements in a JGSet
    startChrono = System.currentTimeMillis();
    for(int i=0 ; i<3*n ; i++) {
      if( member(array[i], t1) == false) System.out.println("Loose an element");
    }
    stopChrono = System.currentTimeMillis();
    System.out.println("found each element of tree size = " + size + " in " + (stopChrono-startChrono) + " ms");

   
      //Maximal sharing
    for(int i=0 ; i<3*n ; i++) {
      t2 = add(array[i], t2);
    }
    if (t1 == t2) System.out.println("Maximal sharing");

      //Union and intersection
    JGSet t3 = union(t1, t2);
    if (t1 == t3) System.out.println("Simple union OK");
    JGSet t4 = intersection(t1, t2);
    if (t4 == t3) System.out.println("Simple intersection OK");
    
    JGSet t5 = `emptySet();
    JGSet t6 = `emptySet();
    JGSet t7 = `emptySet();
    for(int i=0 ; i<2*n ; i++) {
      t5 = add(array[i], t5);
    }
    for(int i=n ; i<3*n ; i++) {
      t6 = add(array[i], t6);
    }
    for(int i=n ; i<2*n ; i++) {
      t7 = add(array[i], t7);
    }
    System.out.println("starting complex union");
    JGSet t8 = union(t5, t6);
    if (t1 == t8) {System.out.println("Union OK");} else {System.out.println("Complex union failed with \n\tt5:"+t5+" \nand \tt6: "+t6+"\n\tres: "+t8+"\n\tbuilt: "+t1);}
    JGSet t9 = intersection(t5, t6);
    if (t7 == t9) {System.out.println("Intersection OK");} else {System.out.println("Complex intersection failed with t5:\t"+t5+"\nt6:\t"+t6+"\nres:\t"+t9+"\nbuilt:\t"+t7);}
    
  }

  
  public final static void main(String[] args) {
    SetFactory fact = new SetFactory();
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
        test = new Set1(fact, input);
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
        System.out.println("hum");
      }
      return 1;
    }
  }

}

