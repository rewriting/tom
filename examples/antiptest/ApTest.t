/*
 * Copyright (c) 2004-2007, INRIA
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
package antiptest;

import antiptest.aptest.peano.types.*;

public class ApTest {

  %gom {
    module Peano
      abstract syntax
      Nat = zero()
      | suc(pred:Nat)
      | plus(x1:Nat, x2:Nat)
      
      NatList = conc(Nat*)
  }  

  public Nat evaluate(Nat n) {
    %match(Nat n) {      
      zero()                    -> { System.out.println("Matched with: zero()");}
      plus(x,y)                 -> { System.out.println("Matched with: plus(x,y), with x=" + `x + " y=" + `y);}
      plus(x,_)                 -> { System.out.println("Matched with: plus(x,_), with x=" + `x );}
      plus(x,a@x)                 -> { System.out.println("Matched with: plus(x,x), with x=" + `x + "a=" + `a);}
      plus(x,a@suc(_))                 -> { System.out.println("Matched with: plus(x,suc(x)), with x=" + `x + "a=" +`a);}
      d@plus(c@x,b@suc(a@suc(plus(y,x))))    -> { System.out.println("Matched with: plus(x,suc(suc(plus(y,x)))), with x=" + `x + " y=" + `y + " a=" + `a + " b=" + `b + " c=" + `c + " d=" + `d);}
    }
    return null;
  }
  
  public void evaluateList(NatList n) {
    %match(n) {      
      conc(X*,_*,X*) -> { System.out.println("Matched with conc(X*,Y*): x*=" + `X + " y*=" + `Y);}
    }   
  }

  //-------------------------------------------------------

  public void run() {
    // System.out.println("running..."); 
    Nat one = `suc(zero());
    Nat two = `suc(one);
    Nat init = `plus(zero(),suc(suc(plus(one,zero()))));//`plus(one,two);


    System.out.println("Subject : " + init);
    Nat res = `evaluate(init);
    //System.out.println("Step = " + `res);

  }

  public final static void main(String[] args) {
    ApTest test = new ApTest();
    test.run();
  }

}
