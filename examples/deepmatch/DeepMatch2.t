/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *	- Redistributions of source code must retain the above copyright
 *	notice, this list of conditions and the following disclaimer.  
 *	- Redistributions in binary form must reproduce the above copyright
 *	notice, this list of conditions and the following disclaimer in the
 *	documentation and/or other materials provided with the distribution.
 *	- Neither the name of the Inria nor the names of its
 *	contributors may be used to endorse or promote products derived from
 *	this software without specific prior written permission.
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

package deepmatch;

import java.util.*;

import deepmatch.deepmatch2.sig.types.* ;
import deepmatch.deepmatch2.sig.* ;
import tom.library.sl.* ;

class DeepMatch2 {
  %include { util/types/Collection.tom }
  %include { sl.tom }

  %gom {
    module sig 
    imports String
    abstract syntax

      A = f(x:A,y:A)
           | B2A(b:B)
      B = l(n:String)
  }

  %typeterm DeepAB {
    implement { Iter<A,B> }
    is_sort(t) { ($t instanceof Iter) }
    equals(t1,t2) { ($t1.equals($t2)) }
  }

  %oplist DeepAB deepAB(B*) {
    is_fsym(l)       { ($l instanceof Iter) }
    make_empty()     { (new Iter<A,B>()) }
    make_insert(o,l) { null }
    get_head(l)      { ($l.getElement()) }
    get_tail(l)      { ($l.next()) }
    is_empty(l)      { (!$l.hasNext()) }
  }

  %op DeepAB iterAB(t:A) {
    make(x) { (new Iter<A,B>($x)) }
  }

  public static void main(String [] argv) {
    A t = `f(f(B2A(l("a")),B2A(l("a"))),f(B2A(l("c")),B2A(l("a"))));
    System.out.println("t = " + t);

/*
    Iter iter = `iterAB(t);
    while(iter.hasNext()) {
      System.out.println("getElement: " + iter.getElement());
      System.out.println("next");
      iter.next();
    }
    
    %match(iter) {
      deepAB(_*,l(x),_*,l(x),_*) -> { System.out.println(" " + `x); }
   }
*/

    %match(A t) {
      //f(z,_) && deepAB(_*,l(x),_*) << iterAB(z) -> { System.out.println(" " + `x); }
      //z && deepAB(_*,l(x),_*,l(x),_*) << iterAB(z) -> { System.out.println(" " + `x); }
      z && deepAB(_*,l(x),_*,l(x),_*) << iterAB(z) -> { System.out.println(" " + `x); }
    }

  }


  %strategy CollectS(bag:Collection) extends Identity() {
    visit B {
      x -> { bag.add(getEnvironment().getPosition()); }
    }
  }

  private static class Iter<T extends tom.library.sl.Visitable, S extends tom.library.sl.Visitable> {
    private ArrayList<Position> list;
    private T term;

    public Iter() {
    }

    public Iter(T t) {
      term = t;
      try {
        list = new ArrayList<Position>();
        `BottomUp(CollectS(list)).visit(t);
        System.out.println("list  = " + list);
      } catch(VisitFailure e) {
        System.out.println("failure");
      }
    }

    public boolean hasNext() {
      return !list.isEmpty();
    }

    public Iter<T,S> next() {
      Iter<T,S> res = new Iter<T,S>();
      res.term = this.term;
      res.list = (ArrayList<Position>)this.list.clone();
      if(!res.list.isEmpty()) {
        res.list.remove(0);
      } 
      return res;
    }

    public S getElement() {
      try {
        return (S) list.get(0).getSubterm().visit(term);
      } catch (VisitFailure e) {
        throw new RuntimeException(); 
      }
    }

  }



}
