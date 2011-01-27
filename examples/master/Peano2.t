/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
 */
package master;

import master.peano2.peano.types.*;

public class Peano2 {

  %gom {
    module Peano
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
        | mult(x1:Nat, x2:Nat)
   }

	public Nat evaluate(Nat n) {
		%match(Nat n) {
      //       plus(zero(), x)    -> { return `x; }
      plus(x, suc(y))    -> { return `suc(plus(x,y));}
      plus(x, zero())    -> { return `x; }

      //       mult(zero(), x)    -> { return `zero(); }
      mult(_, zero())    -> {return `zero();}
      mult(x, suc(y))    -> {return `plus(x,mult(x,y));}

      plus(x,y) -> { return `plus(x,evaluate(y));}
      mult(x,y) -> { return `mult(x,evaluate(y));}
      suc(x) -> { return `suc(evaluate(x));}
      zero() -> { return `zero();}

		}
		return null;
	}

//   %rule {
//     mult(x, zero())    -> zero()
//     mult(x, suc(y))    -> plus(x,mult(x,y))
//   } // rule
 
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		Nat one = `suc(zero());
		Nat two = `suc(one);

//     Nat init = `plus(two,two);
//     Nat init = `plus(two,two);
//     Nat init = `plus(one,plus(one,plus(one,one)));
//     Nat init = `plus(plus(one,one),plus(one,one));
//     Nat init  = `plus(zero(),plus(one,one));
    Nat init  =  `mult(zero(),suc(suc(zero())));
    boolean done=false;
    System.out.println("Start = " + init);
    while(!done){
      Nat res = `evaluate(init);
      System.out.println("Step = " + `res);
      if(res==init){
        done=true;
      } else {
        init=res;
      }
    }
  }
  
  public final static void main(String[] args) {
    Peano2 test = new Peano2();
    test.run();
  }

}
