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

import master.poly1.poly.types.*;

public class Poly1 {

  %gom {
    module Poly
    imports int String
    abstract syntax
    Polynome = m(coef:int,name:String,exp:int)
             | plus(p1:Polynome, p2:Polynome)
             | mult(p1:Polynome, p2:Polynome)
   }

  public Polynome deriv(Polynome p) {
    %match(Polynome p) {
      m(_,"X",0) -> { return `m(0,"X",0); }
      m(c,"X",n) -> {
        if(`n>0) {
          return `m(c * n,"X",n-1);
        }
      }

      plus(p1,p2) -> { return `plus(deriv(p1),deriv(p2)); }
      mult(p1,p2) -> { return `plus(mult(deriv(p1),p2),mult(p1,deriv(p2))); }
    }
    return null;
  }

  public Polynome simplify(Polynome p) {
    Polynome res = oneStepSimplify(p);
    if(p != res) {
      res = simplify(res);
    }
    return res;
  }

  public Polynome oneStepSimplify(Polynome p) {
    %match(Polynome p) {
      plus(m(c1,"X",n),m(c2,"X",n)) -> { return `m(c1+c2,"X",n); }
      plus(m1@m(_,"X",n1),m2@m(_,"X",n2)) -> {
        if(`n2>`n1) {
          return `plus(m2,m1);
        }
      }

      plus(m(c1,"X",n),plus(m(c2,"X",n),p2)) -> { return `plus(m(c1+c2,"X",n),p2); }
      plus(m1@m(_,"X",n1),plus(m2@m(_,"X",n2),p2)) -> {
        if(`n2>`n1) {
          return `plus(m2,plus(m1,p2));
        }
      }

      plus(plus(m1,p1),p2) -> { return `plus(m1,plus(p1,p2)); }

      // congruence
      plus(p1,p2) -> { return `plus(simplify(p1),simplify(p2)); }
    }
    return p;
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Polynome X_2 = `m(1,"X",2);

    System.out.println("X_2 = " + X_2);
    System.out.println("X_2+3 = " + `plus(X_2,m(3,"X",0)));
    System.out.println("deriv(X_2+3) = " + `deriv(plus(X_2,m(3,"X",0))));

    // 3*X2+X+7
    Polynome P1 = `plus(m(3,"X",2),plus(m(1,"X",1),m(7,"X",0)));
    // 5*X2+3*X+4
    Polynome P2 = `plus(m(5,"X",2),plus(m(3,"X",1),m(4,"X",0)));
    System.out.println("deriv(plus(P1,P2)) = " + `deriv(plus(P1,P2)));
    System.out.println("deriv(plus(P1,P2)) = " + `simplify(deriv(plus(P1,P2))));

  }

  public final static void main(String[] args) {
    Poly1 test = new Poly1();
    test.run();
  }

}
