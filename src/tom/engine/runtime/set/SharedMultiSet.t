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

import jtom.runtime.set.jgtreeset.SetFactory;
import jtom.runtime.set.jgtreeset.JGTreeSet;

import jtom.runtime.GenericTraversal;
import jtom.runtime.Replace1;
import jtom.runtime.Collect1;

public class SharedMultiSet extends ATermSet {
  %include { set.t }
  
  public SharedMultiSet(PureFactory fact) {
    this.factory = new SetFactory(fact);
    this.emptyTree = getSetFactory().makeJGTreeSet_EmptySet();
    this.tree = makeEmptySet();
  }
  
  private SharedMultiSet(SetFactory fact, JGTreeSet tree, int count) {
    this.factory = fact;
    this.tree = tree;
    this.count = count;
  }
  
  public Object[] toArray() {
    final Collection res = new ArrayList();
    Collect1 collect = new Collect1() {
        public boolean apply(ATerm t) {
          if(t instanceof JGTreeSet) {
            %match(JGTreeSet t) {
              emptySet -> {return false;}
              pair[value=x] -> {
                res.add(x);
                return false;
              }
              _ -> {return true;}
            }
          } else {
            return true;
          }
        } // Apply
      }; //new
    
    SharedMultiSet.this.traversal.genericCollect(tree, collect);
    ATerm[] result = new ATerm[res.size()];
    for(int i=0;i<res.size();i++) {
      result[i] = (ATerm) (((ArrayList)res).get(i));
    }
    return result;
  }
  
  public Object[] toArray(Object[] o) {
    throw new RuntimeException("Not Yet Implemented");
  }
  
  public SharedMultiSet getTail() {
    JGTreeSet set = remove(getHead(tree), tree);
    return new SharedMultiSet(getSetFactory(), set, count-1);
  }
  
  private JGTreeSet makePair(ATerm trm, int i) {
    return getSetFactory().makeJGTreeSet_Pair(trm, new Integer(i));
  }
  
  private JGTreeSet makePair(ATerm trm, Integer i) {
    return getSetFactory().makeJGTreeSet_Pair(trm, i);
  }
    // Low interface
  
  protected int size(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet    -> { return 0; }
      pair[value=x] -> { return 1; }
      branch(l, r) -> {return size(l) + size(r);}
    }
    return 0;
  }
  
  public int multiplicitySize() {
    return multiplicitySize(tree);
  }

  private int multiplicitySize(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet    -> { return 0; }
      pair[multiplicity=m] -> {
        return m.intValue();
      }
      branch(l, r) -> {return multiplicitySize(l) + multiplicitySize(r);}
    }
    return 0;
  }
      // getHead return the first left inner element found
  protected ATerm getHead(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet -> {
        return null;
      }
      pair[value=x] -> {return x;}
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

  protected JGTreeSet reworkJGTreeSet(JGTreeSet t) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm t) {
          %match(JGTreeSet t) {
            emptySet -> {return t;}
            pair -> {return t;}
            branch(emptySet, p@pair) -> {return p;}
            branch(p@pair, emptySet) -> {return p;}
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
  
  protected JGTreeSet union(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
      emptySet, x -> {
        return m2;
      }

      x, emptySet -> {
        return m1;
      }

      pair(elt, mult), x -> {
        return override(elt, mult, x, level);
      }

      x, pair(elt, mult) -> {
          // underride dont worry about multiplicity
        return underride(elt, x, level);
      }

      branch(l1, r1), branch(l2, r2) -> {
        int l = level+1;
        return `branch(union(l1, l2, l), union(r1, r2, l));
      }
    }
    return null;
  }
  
  protected JGTreeSet intersection(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
      emptySet, x |
        x, emptySet -> { 
        return `emptySet();
      }
      
      p@pair(elt,mult), x |
        x, p@pair(elt,mult) -> {
        if (contains(elt, x, level)) {
            /* Warning: we have to choose one of the 2 element: with the lowest multiplicity*/
          return p;
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
  
  protected JGTreeSet restriction(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
      emptySet, x |
      x, emptySet -> { 
        return `emptySet();
      }
      
      pair[value=y], x -> {
        return remove(y, x, level);
      }

      x, pair[value=y] -> {
        if (contains(y, x)) {
            // Warning: we have to choose one of the 2 element: with the lowest multiplicity
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
  
  protected JGTreeSet remove(ATerm elt, JGTreeSet t, int level) {
    %match(JGTreeSet t) {
      emptySet      -> {return t;}

      pair[value=x]   -> {
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
          emptySet, pair -> {return r1;}
          pair, emptySet -> {return l1;}
          _, _ -> {return `branch(l1, r1);}
        }
      }
    }
    return null;
  }

  protected boolean contains(ATerm elt, JGTreeSet t, int level) {
    %match(JGTreeSet t) {
      emptySet -> {return false;}
      
      pair[value=x] -> {
        if(x == elt) return true;
      }
      
      branch(l, r) -> {
        if(level == depth) {
          return (contains(elt, l, level) || contains(elt, r, level));
        }
        if( isBitZero(elt, level)) {
          return contains(elt, l, level+1);
        } else {
          return contains(elt, r, level+1);
        }
      }
    }
    return false;
  }
  
  protected JGTreeSet override(ATerm elt, Integer multiplicity, JGTreeSet t, int level) {
    int lev = level+1;
    %match(JGTreeSet t) {
      emptySet      -> {
        return makePair(elt, multiplicity);
      }

      pair[value=x, multiplicity=mult]   -> {
        if(x == elt) {  return makePair(elt, mult.intValue()+multiplicity.intValue());}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitZero(elt, level) && isBitZero(x, level) )  { return `branch(override(elt, multiplicity, t, lev), emptySet);}
        else if ( isBitOne(elt, level)  && isBitOne(x, level) )   { return `branch(emptySet, override(elt, multiplicity, t, lev));}
        else if ( isBitZero(elt, level) && isBitOne(x, level) ) { return `branch(makePair(elt, multiplicity), t);}
        else if ( isBitOne(elt, level)  && isBitZero(x, level) ){ return `branch(t, makePair(elt, multiplicity));}
      }
      
      branch(l, r) -> {
        if(level >= depth) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            //continue list of element
          return `branch(t, singleton(elt));
        }
        if (isBitZero(elt, level)) {
          return `branch(override(elt, multiplicity, l, lev), r);
        } else {
          return `branch(l, override(elt, multiplicity, r, lev));
        }
      }
        //_ -> {System.out.println("Strange Tree:"+t+" from  "+t.getClass());System.exit(1);}
    }
    return null;
  }
  
  protected JGTreeSet underride(ATerm elt, JGTreeSet t, int level) {
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
  
} //Class SharedMultiSet
