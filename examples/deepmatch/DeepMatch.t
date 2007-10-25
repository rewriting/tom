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

import java.util.Stack;

import deepmatch.deepmatch.sig.types.* ;
import deepmatch.deepmatch.sig.* ;
import tom.library.sl.* ;

class DeepMatch {

  %gom {
    module sig 
    imports String
    abstract syntax

      Term = f(x:Term,y:Term)
           | l(n:String)

  }

  private static class Iter<T extends sigAbstractType> {
    private Position pos;
    private Stack<Integer> lastvisited;
    private T term;
    private boolean done = false;

    public Iter(T t) {
      term = t;
      pos = new Position();
      lastvisited = new Stack<Integer>();
    }

    public Iter() {
      term = null;
      pos = null;
      lastvisited = null;
      done = true;
    }

    private Iter(T t, Position p, Stack<Integer> v) {
      term = t;
      pos = p;
      lastvisited = v;
    }

    public boolean finished() {
      return done;
    }

    public Iter<T> next() {
      if (getSubterm().getChildCount() > 0) {
        Position newpos = pos.down(1);
        Stack<Integer> newlv = ((Stack<Integer>) lastvisited.clone());
        newlv.push(1);
        return new Iter(term, newpos, newlv);
      } else {
        Position newpos = pos;
        Stack<Integer> newlv = (Stack<Integer>) lastvisited.clone();
        T st = null;
        while(true) {
          newpos = newpos.up();
          try { st = (T) newpos.getSubterm().visit(term); }
          catch (VisitFailure e) { throw new RuntimeException(); }
          int last = newlv.pop();
          if (last < st.getChildCount()) {
            last++;
            newlv.push(last);
            newpos = newpos.down(last);
            return new Iter(term, newpos, newlv);
          } else if (newpos.depth() == 0) {
            return new Iter();
          }
        }
      }
    }

    public T getSubterm() {
      try { return (T) pos.getSubterm().visit(term); }
      catch (VisitFailure e) { throw new RuntimeException(); }
    }

    public boolean equals(Object o) {
      if (!(o instanceof Iter)) return false;
      Iter<T> t = (Iter<T>) o;
      if (done) { return t.done; }
      if (t.done) { return false; }
      if (lastvisited.size() != t.lastvisited.size()) return false;
      for(int i=0; i< lastvisited.size(); i++) {
        if (lastvisited.get(i) != t.lastvisited.get(i))
          return false;
      }
      return (term == t.term && pos.equals(t.pos));
    }

    public String toString() {
      String res = "{ ";
      res += "term = " + `term;
      res += ", lv = " + lastvisited;
      res += ", pos = " + pos;
      res += ", done = " + done;
      res += " }";
      return res;
    }
  }

  %typeterm Deep {
    implement { Iter<Term> }
    is_sort(t) { t instanceof Iter }
    equals(t1,t2) { t1.equals(t2) }
  }

  %oplist Deep deep(Term*) {
    is_fsym(l)       { l instanceof Iter }
    make_empty()     { new Iter() }
    make_insert(o,l) { null }
    get_head(l)      { l.getSubterm() }
    get_tail(l)      { l.next() }
    is_empty(l)      { l.finished() }
  }

  %op Deep iter(t:Term) {
    make(x) { new Iter(x) }
  }

  public static void main(String [] argv) {
    Term t = `f(f(l("a"),l("b")),f(l("c"),l("a")));
    System.out.println("- t = " + t);
    
    Iter<Term> it = new Iter(t);
    System.out.print("- all labels in t :");
    %match(it) {
      deep(_*,l(x),_*) -> { System.out.print(" " + `x); }
    }

    System.out.print("\n- all labels appearing at least twice in t :");
    %match(it) {
      deep(_*,l(x),_*,l(x),_*) -> { System.out.print(" " + `x); }
    }

    System.out.print("\n- future translation of f(z@{l(x)},_) -> { print(x) } :");
    %match(t) {
      f(z,_) && deep(_*,l(x),_*) << iter(z) -> { System.out.print(" " + `x); }
    }

    System.out.println();
  }
}
