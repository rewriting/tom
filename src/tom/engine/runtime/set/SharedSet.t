/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2004, Pierre-Etienne Moreau
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
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
 *
 * INRIA, Nancy, France 
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package jtom.runtime.set;

import java.util.*;

import aterm.*;
import aterm.pure.PureFactory;

import jtom.adt.set.*;
import jtom.adt.set.types.*;

import jtom.runtime.Replace1;
import jtom.runtime.Collect1;

public class SharedSet extends ATermSet {

  %include { ../../adt/Set.tom }
  
  public SharedSet(PureFactory pureFactory) {
    if (factory==null) {
      factory = new Factory(pureFactory);
    }
    emptyTree = getSetFactory().makeJGTreeSet_EmptySet();
    this.tree = makeEmptySet();
  }
  
  private SharedSet(Factory fact, JGTreeSet tree, int count) {
    factory = fact;
    this.tree = tree;
    this.count = count;
  }
  
  public Object[] toArray() {
    final Collection res = new ArrayList();
    Collect1 collect = new Collect1() {
        public boolean apply(ATerm t) {
          if(t instanceof JGTreeSet) {
            %match(JGTreeSet t) {
              emptySet()-> {return false;}
              singleton(x) -> {
                res.add(`x);
                return false;
              }
              _ -> {return true;}
            }
          } else {
            return true;
          }
        } // Apply
      }; //new
    
    ATermSet.traversal.genericCollect(tree, collect);
    ATerm[] result = new ATerm[res.size()];
    for(int i=0;i<res.size();i++) {
      result[i] = (ATerm) (((ArrayList)res).get(i));
    }
    return result;
  }

  public Object[] toArray(Object[] o) {
    throw new RuntimeException("Not Yet Implemented");
  }
  
  public SharedSet getTail() {
    JGTreeSet set = remove(getHead(tree), tree);
    return new SharedSet(getSetFactory(), set, count-1);
  }
    // Low interface  
  protected int size(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet()   -> { return 0; }
      singleton(x) -> { return 1; }
      branch(l, r) -> {return size(`l) + size(`r);}
    }
    return 0;
  }

      // getHead return the first left inner element found
  protected ATerm getHead(JGTreeSet t) {
    %match(JGTreeSet t) {
      emptySet()-> {
        return null;
      }
      singleton(x) -> {return `x;}
      branch(l,r) -> {
        ATerm left = getHead(`l);
        if(left != null) {
          return left;
        }
        return getHead(`r);
      }
    }
    return null;
  }
  
  /* Simple binary operation skeleton
 private JGTreeSet f(JGTreeSet m1, JGTreeSet m2) {
   %match(JGTreeSet m1, JGTreeSet m2) {
      emptySet(), x -> {
        return f2(m2);
      }
      x, emptySet()-> {
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
            emptySet()-> {return t;}
            singleton(x) -> {return t;}
            branch(emptySet(), s@singleton(x)) -> {return `s;}
            branch(s@singleton(x), emptySet()) -> {return `s;}
            branch(e@emptySet(), emptySet()) -> {return `e;}
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
      emptySet(), x -> {
        return m2;
      }

      x, emptySet()-> {
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
  
  protected JGTreeSet intersection(JGTreeSet m1, JGTreeSet m2, int level) {
    %match(JGTreeSet m1, JGTreeSet m2) {
      emptySet(), x |
        x, emptySet()-> { 
        return `emptySet();
      }
      
      s@singleton(y), x |
      x, s@singleton(y) -> {
        if (contains(`y, `x, level)) {
          return `s;
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
      emptySet(), x |
      x, emptySet()-> { 
        return `emptySet();
      }
      
      singleton(y), x -> {
        return remove(`y, `x, level);
      }

      x, singleton(y) -> {
        if (contains(`y, `x)) {
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
      emptySet()     -> {return t;}

      singleton(x)   -> {
        if (`x == elt) {return `emptySet();}
        else {return t;}
      }

      branch(l, r) -> {
        JGTreeSet l1 = null, r1=null;
        if( isBitZero(elt, level) ) {
          l1 = remove(elt, `l, level+1);
          r1 = `r;
        } else {
          l1 = `l;
          r1 = remove(elt, `r, level+1);
        }
        %match(JGTreeSet l1, JGTreeSet r1) {
          emptySet(), singleton(x) -> {return r1;}
          singleton(x), emptySet()-> {return l1;}
          _, _ -> {return `branch(l1, r1);}
        }
      }
    }
    return null;
  }

  protected boolean contains(ATerm elt, JGTreeSet t, int level) {
    %match(JGTreeSet t) {
      emptySet()-> {return false;}
      
      singleton(x) -> {
        if(`x == elt) return true;
      }
      
      branch(l, r) -> {
        if(level == depth) {
          return (contains(elt, `l, level) || contains(elt, `r, level));
        }
        if( isBitZero(elt, level)) {
          return contains(elt, `l, level+1);
        } else {
          return contains(elt, `r, level+1);
        }
      }
    }
    return false;
  }

  protected JGTreeSet override(ATerm elt, int multiplicity, JGTreeSet t, int level) {
    return override(elt, t, level);
  }
  
  private JGTreeSet override(ATerm elt, JGTreeSet t, int level) {
    int lev = level+1;
    %match(JGTreeSet t) {
      emptySet()     -> {
        return `singleton(elt);
      }

      singleton(x)   -> {
        if(`x == elt) {  return `singleton(elt);}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitZero(elt, level) && isBitZero(`x, level) )  { return `branch(override(elt, t, lev), emptySet());}
        else if ( isBitOne(elt, level)  && isBitOne(`x, level) )   { return `branch(emptySet(), override(elt, t, lev));}
        else if ( isBitZero(elt, level) && isBitOne(`x, level) ) { return `branch(singleton(elt), t);}
        else if ( isBitOne(elt, level)  && isBitZero(`x, level) ){ return `branch(t, singleton(elt));}
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
    return null;
  }
  
  protected JGTreeSet underride(ATerm elt, JGTreeSet t, int level) {
    int lev = level+1;
    %match(JGTreeSet t) {
      emptySet()     -> {return `singleton(elt);}

      singleton(x)   -> {
        if(`x == elt) {  return t;}
        else if( level >= depth ) {
          System.out.println("Collision!!!!!!!!");
          collisions++;
            // Create 1rst list of element as it was a branch
          return `branch(t, singleton(elt));
          
        }
        else if ( isBitZero(elt, level) && isBitZero(`x, level) )  { return `branch(underride(elt, t, lev), emptySet());}
        else if ( isBitOne(elt, level)  && isBitOne(`x, level) )   { return `branch(emptySet(), underride(elt, t, lev));}
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

} //Class SharedSet
