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

public class Peano1 {

  %gom {
    module Peano
    imports int
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
        | mult(x1:Nat, x2:Nat)
        | fib(n:Nat)

    module Peano:rules() {
      plus(x, zero())    -> x 
      plus(x, suc(y))    -> suc(plus(x,y))
      mult(x, zero())    -> zero() 
      mult(x, suc(y))    -> plus(x,mult(x,y))
      fib(zero()) -> suc(zero())
      fib(suc(zero())) -> suc(zero())
      fib(suc(suc(x))) -> plus(fib(x),fib(suc(x)))
    } 
  }
  
  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
		//Nat one = `suc(zero());
		//Nat two = `suc(one);

    //System.out.println("plus(one,two) = " + plus(one,two));
    System.out.println("plus(one,two) = " + `plus(suc(zero()),suc(suc(zero()))));
    System.out.println("mult(two,two) = " + `mult(suc(suc(zero())),suc(suc(zero()))));
    System.out.println("fib(10) = " + `fib(suc(suc(suc(suc(suc(suc(suc(suc(suc(suc(zero()))))))))))));
  }
  
  public final static void main(String[] args) {
    Peano1 test = new Peano1();
    test.run();
  }

}
