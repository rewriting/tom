/*
 * Copyright (c) 2004, INRIA
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

package prodrule;

import aterm.*;
import aterm.pure.*;
import prodrule.fib3.fib.*;
import prodrule.fib3.fib.types.*;
import java.util.*;

public class Fib3 {
  private Factory factory;

  %vas {
    // extension of adt syntax
    module fib
      
    public
      sorts Element
      
    abstract syntax
      Undef -> Element
      Nat( value:Int ) -> Element
      Fib(arg:Int, val:Element) -> Element
   }

  %typearray Space {
    implement { ArrayList }
    get_fun_sym(t)   { ((t instanceof ArrayList)?factory.getPureFactory().makeAFun("concElement", 1, false):null) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    equals(l1,l2)      { l1.equals(l2) }
    get_element(l,n)   { l.get(n) }
    get_size(l)        { l.size() }
  }

  %oparray Space concElement( Element* ) {
    fsym            { factory.getPureFactory().makeAFun("concElement", 1, false) }
    make_empty(n)   { myEmpty(n) }
    make_append(e,l) { myAdd(e,(ArrayList)l) }
  }

  private ArrayList myAdd(Object e,ArrayList l) {
    l.add(e);
    return l;
  }
  
  private ArrayList myEmpty(int n) {
    ArrayList res = new ArrayList(n);
    return res;
  }

  public Fib3(Factory factory) {
    this.factory = factory;
  } 

  public Factory getFibFactory() {
    return factory;
  }
  
  public int run(int n) {
    long startChrono = System.currentTimeMillis();
    System.out.println("running...");
    ArrayList space = `concElement(Fib(0,Nat(1)) , Fib(1,Nat(1)) , Fib(n,Undef));
    space = loop(space);
    System.out.println("fib(" + n + ") = " + result(space,n) + " (in " + (System.currentTimeMillis()-startChrono)+ " ms)");
    return result(space,n);
  }
  
  public ArrayList loop(ArrayList s) {
    ArrayList oldSpace = s;
    ArrayList space = compute(rec(oldSpace));
    while(space != oldSpace) {
      oldSpace = space;
      space = compute(rec(space));
    } 
    return space;
  }

  public final static void main(String[] args) {
    Fib3 test = new Fib3(new Factory(new PureFactory(16)));
    try {
      test.run(Integer.parseInt(args[0]));
    } catch (Exception e) {
      System.out.println("Usage: java Fib <nb>");
      return;
    }
  }
				
  public ArrayList rec(ArrayList s) {
    %match(Space s) {
	    concElement(S1*, Fib[arg=n,val=Undef()], S2*) -> {
        if( `(n>2 && !occursFib(S1*,n-1) && !occursFib(S2*,n-1)) ) {
          //if( `n>2 && !`occursFib(S1*,n-1) && !`occursFib(S2*,n-1))  {
          //  if( `n>2 && !`occursFib(S1*,n-1) && !`occursFib(S2*,n-1) ) {
          return `concElement(Fib(n-1,Undef),s*);
        }
	    }
    }
    return s;
  }

  public ArrayList compute(ArrayList s) {
    %match(Space s) {
      concElement(S1*, Fib[arg=n,val=Undef()], S2*) -> {
        ArrayList s12 = `concElement(S1*,S2*);
        %match(Space s12) {
          concElement(T1*, f1@Fib[arg=n1,val=Nat(v1)], T2*, f2@Fib[arg=n2,val=Nat(v2)], T3*) -> {
            if(`(n1+1==n && n2+2==n)) {
              int modulo = (`v1+`v2)%1000000;
              return `concElement(Fib(n,Nat(modulo)),f1,T1*,T2*,T3*);
            } else if(`(n2+1==n && n1+2==n) ) {
              int modulo = (`v1+`v2)%1000000;
              return `concElement(Fib(n,Nat(modulo)),f2,T1*,T2*,T3*);
            }

          }
        }
      }
    }
    return s;
  }

  public boolean occursFib(ArrayList s, int value) {
    %match(Space s) {
      concElement(S1*, Fib[arg=n], S2*) -> {
        if(`n == value) {
          return true;
        }
      }
    }
    return false;
  }

  public int result(ArrayList s, int value) {
    %match(Space s) {
      concElement(_*, Fib[arg=n, val=Nat(v)], _*) -> {
        if(`n == value) {
          return `v;
        }
      }
    }
    return 0;
  }

}
