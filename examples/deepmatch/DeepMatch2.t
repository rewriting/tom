/*
 * Copyright (c) 2004-2007, INRIA
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
 *	- Neither the name of the INRIA nor the names of its
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

  %typeterm DeepB {
    implement { Iter<B> }
    is_sort(t) { $t instanceof Iter }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %oplist DeepB deepB(B*) {
    is_fsym(l)       { $l instanceof Iter }
    make_empty()     { new Iter<B>() }
    make_insert(o,l) { null }
    get_head(l)      { $l.getElement() }
    get_tail(l)      { $l.next() }
    is_empty(l)      { $l.finished() }
  }

  %op DeepB iterB(t:B) {
    make(x) { new Iter<B>($x) }
  }

  %op DeepB iterA(t:A) {
    make(x) { new Iter<A>($x) }
  }


  public static void main(String [] argv) {
    A t = `f(f(B2A(l("a")),B2A(l("b"))),f(B2A(l("c")),B2A(l("a"))));
    System.out.println("- t = " + t);

    Iter<A> it = `iterA(t);
    System.out.print("- all labels in t :");
    %match(it) {
      deepB(_*,l(x),_*) -> { System.out.print(" " + `x); }
    }

    System.out.print("\n- all labels appearing at least twice in t :");
    %match(it) {
      deepB(_*,l(x),_*,l(x),_*) -> { System.out.print(" " + `x); }
    }

    System.out.print("\n- future translation of f(z@{l(x)},_) -> { print(x) } :");
    %match(t) {
      f(z,_) && deepB(_*,l(x),_*) << iterA(z) -> { System.out.print(" " + `x); }
    }

    System.out.println();
  }


  %strategy Collect(bag:Collection) extends Identity() {
    visit B {
      x -> { bag.add(getEnvironment().getPosition()); }
    }
  }

  private static class Iter<T extends tom.library.sl.Visitable> {
    private ListIterator it;
    private T term;
    private Position next;

    public Iter() {
    }

    public Iter(T t) {
      term = t;
      try {
        List c = new ArrayList();
        `BottomUp(Collect(c)).visit(t);
        System.out.println("c  = " + c);
        it = c.listIterator();
        if(it.hasNext()) {
          next = (Position) it.next();
        }
      } catch(VisitFailure e) {
        System.out.println("failure");
      }
    }

    public boolean finished() {
      return !it.hasNext();
    }

    public Iter<T> next() {
      Position p = (Position) it.next();

    }

    public T getElement() {
      try {
        if(next==null || term==null) {
          return null;
        }
        return (T) next.getSubterm().visit(term);
      } catch (VisitFailure e) {
        throw new RuntimeException(); 
      }
    }

  }



}
