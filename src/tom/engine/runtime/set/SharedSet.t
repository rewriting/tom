/*
  
    TOM - To One Matching Compiler
    
    Copyright (C) 2000-2003  LORIA (CNRST, INPL, INRIA, UHP, U-Nancy 2)
    Nancy, France.
    
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.
    
    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
    
    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
    
    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Julien Guyon

*/

package jtom.runtime.set;

import java.util.*;

import aterm.*;
import aterm.pure.PureFactory;

import jtom.runtime.set.jgtreeset.*;

import jtom.runtime.GenericTraversal;
import jtom.runtime.Replace1;

public class SharedSet {
  
  private static SetFactory factory = null;
  private int depth = 31;
  private JGTreeSet tree;
  
  private GenericTraversal traversal = new GenericTraversal();;
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
  private int collisions = 0;

  %include { set.t }
  
  
/*  public SharedSet() {
    this(10);
  }
*/
  
  public SharedSet(PureFactory fact) {
    this.factory = new SetFactory(fact);
    this.traversal = new GenericTraversal();
    tree = makeEmptySet();
  }

  public JGTreeSet getTreeSet() {
    return tree;
  }
  
  public SetFactory getSetFactory() { 
    return factory;
  }

  public boolean equals(SharedSet set) {
    return (tree == set.getTreeSet());
  }
  
  public boolean isEmpty(JGTreeSet set) {
    return set.isEmptySet();
  }

  public JGTreeSet makeEmptySet() {
    return getSetFactory().makeJGTreeSet_EmptySet();
  }

  public JGTreeSet makeSingleton(ATerm trm) {
    return getSetFactory().makeJGTreeSet_Singleton(trm);
  }
  
  public JGTreeSet makeBranch(JGTreeSet left, JGTreeSet right) {
    return getSetFactory().makeJGTreeSet_Branch(left, right);
  }
  
  public boolean add(ATerm elt) {
    JGTreeSet c = (JGTreeSet)tree.duplicate();
    tree = override(elt, tree, 0);
    return (tree.equivalent(c));
  }
  
  public JGTreeSet add(ATerm elt, JGTreeSet t) {
    return override(elt, t, 0);
  }

  public boolean remove(ATerm elt) {
    JGTreeSet c = (JGTreeSet)tree.duplicate();
    tree = remove(elt, tree, 0);
    return (tree.equivalent(c));
  }
  public JGTreeSet remove(ATerm elt, JGTreeSet t) {
    return remove(elt, t, 0);
  }

  public boolean member(ATerm elt) {
    return member(elt, tree, 0);
  }
  
  public boolean member(ATerm elt, JGTreeSet t) {
    return member(elt, t, 0);
  }

  public JGTreeSet union(JGTreeSet t1, JGTreeSet t2) {
    return union(t1, t2, 0);
  }

  public JGTreeSet intersection(JGTreeSet t1, JGTreeSet t2) {
    JGTreeSet result = intersection(t1, t2, 0);
    return result;
      //return reworkJGTreeSet(result);
  }
  
  public int card(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet    -> { return 0; }
      singleton(x) -> { return 1; }
      branch(l, r) -> {return card(l) + card(r);}
    }
    return 0;
  }

  public void topRepartition(JGTreeSet t) {
    %match(JGTreeSet t) {
      branch(l,r) -> { System.out.println("Left branch: "+card(l)+"\tright branch: "+card(r));return;}
      _ ->  {System.out.println("topRepartition: No a branch");}
    }
  }

    // getHead return the first left inner element found
  public ATerm getHead(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet -> {
        return null;
      }
      singleton(x) -> {return x;}
      branch(l,r) -> {
        ATerm left = getHead(l);
        if(left != null) {
          return left;
        }
        return getHead(r);
      }
    }
    return null;
  }

  public JGTreeSet getTail(JGTreeSet t) {
    return remove(getHead(t), t);
  }
  
  /* Simple binary operation skeleton
 private JGTreeSet f(JGTreeSet m1, JGTreeSet m2) {
   %match(JGTreeSet m1, JGTreeSet m2) {
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

  private JGTreeSet reworkJGTreeSet(JGTreeSet t) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm t) {
          %match(JGTreeSet t) {
            emptySet -> {return t;}
            singleton(x) -> {return t;}
            branch(emptySet, s@singleton(x)) -> {return s;}
            branch(s@singleton(x), emptySet) -> {return s;}
            branch(e@emptySet, emptySet) -> {return e;}
            branch(l1, l2) -> {return `branch(reworkJGTreeSet(l1), reworkJGTreeSet(l2));}
            _ -> { return traversal.genericTraversal(t,this); }
          }
        }
      };
    
    JGTreeSet res = (JGTreeSet)replace.apply(t);
    if(res != t) {
      res = reworkJGTreeSet(res);
    }
    return res;
  }
  
  private JGTreeSet union(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
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
  
  private JGTreeSet intersection(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
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
  
  public JGTreeSet restriction(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
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
  
  private JGTreeSet remove(ATerm elt, JGTreeSet t, int level) {
    %match(JGTreeSet t) {
      emptySet      -> {return t;}

      singleton(x)   -> {
        if (x == elt) {return `emptySet();}
        else {return t;}
      }

      branch(l, r) -> {
        JGTreeSet l1 = null, r1=null;
        if( isBitZero(elt, level) ) {
          l1 = remove(elt, l, level+1);
          r1 = r;
        } else {
          l1 = l;
          r1 = remove(elt, r, level+1);
        }
        %match(JGTreeSet l1, JGTreeSet r1) {
          emptySet, singleton(x) -> {return r1;}
          singleton(x), emptySet -> {return l1;}
          _, _ -> {return `branch(l1, r1);}
        }
      }
    }
    return null;
  }

  private boolean member(ATerm elt, JGTreeSet t, int level) {
    %match(JGTreeSet t) {
      emptySet -> {return false;}
      
      singleton(x) -> {
        if(x == elt) return true;
      }
      
      branch(l, r) -> {
        if(level == depth) {
          return (member(elt, l, level) || member(elt, r, level));
        }
        if( isBitZero(elt, level)) {
          return member(elt, l, level+1);
        } else {
          return member(elt, r, level+1);
        }
      }
    }
    return false;
  }
  
  private JGTreeSet override(ATerm elt, JGTreeSet t, int level) {
    int lev = level+1;
    %match(JGTreeSet t) {
      emptySet      -> {
        return `singleton(elt);
      }

      singleton(x)   -> {
        if(x == elt) {  return `singleton(elt);}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitZero(elt, level) && isBitZero(x, level) )  { return `branch(override(elt, t, lev), emptySet);}
        else if ( isBitOne(elt, level)  && isBitOne(x, level) )   { return `branch(emptySet, override(elt, t, lev));}
        else if ( isBitZero(elt, level) && isBitOne(x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBitOne(elt, level)  && isBitZero(x, level) ){ return `branch(t, singleton(elt));}
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
        //_ -> {System.out.println("Strange Tree:"+t+" from  "+t.getClass());System.exit(1);}
    }
    return null;
  }
  
  private JGTreeSet underride(ATerm elt, JGTreeSet t, int level) {
    int lev = level+1;
    %match(JGTreeSet t) {
      emptySet      -> {return `singleton(elt);}

      singleton(x)   -> {
        if(x == elt) {  return t;}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitZero(elt, level) && isBitZero(x, level) )  { return `branch(underride(elt, t, lev), emptySet);}
        else if ( isBitOne(elt, level)  && isBitOne(x, level) )   { return `branch(emptySet, underride(elt, t, lev));}
        else if ( isBitZero(elt, level) && isBitOne(x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBitOne(elt, level)  && isBitZero(x, level) ){ return `branch(t, singleton(elt));}
      }

      branch(l, r) -> {
        if (isBitZero(elt, level)) {return `branch(underride(elt, l, lev), r);}
        else {return `branch(l, underride(elt, r, lev));}
      }
    }
    return null;
  }

  private boolean isBitZero(ATerm elt, int position) {
    return ( (elt.getUniqueIdentifier() & mask[position]) == 0);
  }
  
  private boolean isBitOne(ATerm elt, int position) {
    return ( (elt.getUniqueIdentifier() & mask[position]) > 0);
  }

} //Class SharedSet
