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
import prodrule.fib.*;
import prodrule.fib.types.*;

class Fib1 {
  private Factory factory;

  %vas {
    // extension of adt syntax
    module fib
      
    public
      sorts Element Space
      
    abstract syntax
      Undef -> Element
      Nat( value:Int ) -> Element
      Fib(arg:Int, val:Element) -> Element
      concElement( Element* ) -> Space
   }

  public Fib1(Factory factory) {
    this.factory = factory;
  } 

  public Factory getFibFactory() {
    return factory;
  }
  
  public void run() {
    long startChrono = System.currentTimeMillis();
    System.out.println("running...");
    int n = 200;
    Space space = `concElement(Fib(0,Nat(1)) , Fib(1,Nat(1)) , Fib(n,Undef));
    space = loop(space);
    System.out.println("fib(" + n + ") = " + result(space,n) + " (in " + (System.currentTimeMillis()-startChrono)+ " ms)");
  }
  
  public Space loop(Space s) {
    Space oldSpace = s;
    Space space = compute(rec(oldSpace));
    while(space != oldSpace) {
      oldSpace = space;
      space = compute(rec(space));
    } 
    return space;
  } 

  public final static void main(String[] args) {
    Fib1 test = new Fib1(new Factory(new PureFactory(16)));
    test.run();
  }

  public Space rec(Space s) {
    %match(Space s) {
      concElement(S1*, Fib[arg=n,val=Undef], S2*) -> {
        if(`n >2 && !occursFib(`concElement(S1*,S2*),`n-1)) {
          return `manySpace(Fib(n-1,Undef),s);
        }
      }
    }
    return s;
  }

  public Space compute(Space s) {
    %match(Space s) {
      concElement(S1*, Fib[arg=n,val=Undef], S2*) -> {
        Space s12 = `concElement(S1*,S2*);
        %match(Space s12) {
          concElement(T1*, Fib[arg=n1,val=Nat(v1)], T2*) -> {
            if(`n1+1 == `n) {
              Space t12 = `concElement(T1*,T2*);
              %match(Space t12) {
                concElement(_*,Fib[arg=n2,val=Nat(v2)] , _*) -> {
                  if(`n2+1 == `n1) {
                    int modulo = `(v1+v2)%1000000;
                    return `manySpace(Fib(n,Nat(modulo)),s12);
                  }
                }
              }
            }
          }
        }
      }
    }
    return s;
  }

  public boolean occursFib(Space s, int value) {
    %match(Space s) {
      concElement(S1*, Fib[arg=n], S2*) -> {
        if(`n == value) {
          return true;
        }
      }
    }
    return false;
  }

  public int result(Space s, int value) {
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
