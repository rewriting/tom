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

import master.fibonacci2.peano.types.*;

public class Fibonacci2 {

  %gom {
    module Peano
    imports int
    abstract syntax
    Nat = zero()
        | suc(pred:Nat)
        | pred(suc:Nat)
        | plus(x1:Nat, x2:Nat)
        | mult(x1:Nat, x2:Nat)
        | fib(x:Nat)
    module Peano:rules() {
      pred(suc(x))     -> x
      suc(pred(x))     -> x
      plus(x, zero())  -> x
      plus(x, suc(y))  -> suc(plus(x,y))
      plus(x, pred(y)) -> pred(plus(x,y))
      mult(x, zero())  -> zero()
      mult(x, suc(y))  -> plus(x,mult(x,y))
      fib(zero())      -> suc(zero())
      fib(suc(zero())) -> suc(zero())
      fib(x)           -> plus(fib(pred(x)),fib(pred(pred(x))))
    }
  }

  public int fibint(int n) {
    %match(int n) {
      0 -> { return 1; }
      1 -> { return 1; }
      x -> {
        if(`x>1) {
          return `fibint(x-1) + `fibint(x-2);
        }
      }
    }
    return -1;
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Nat one = `suc(zero());
    Nat two = `suc(one);
    Nat minusone = `pred(zero());

    System.out.println("plus(one,minusone) = " + `plus(one,minusone));
    System.out.println("plus(one,two) = " + `plus(one,two));
    System.out.println("mult(two,two) = " + `mult(two,two));
    System.out.println("fibint(4) = " + `fibint(4));
    System.out.println("fib(four) = " + `fib(mult(two,two)));
  }

  public final static void main(String[] args) {
    Fibonacci2 test = new Fibonacci2();
    test.run();
  }

}
