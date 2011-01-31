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

import master.poly2.poly.types.*;

public class Poly2 {

  %gom {
    module Poly
    imports int String
    abstract syntax
    Poly = m(coef:int,name:String,exp:int)
         | plus(pl:PolyList)
         | mult(pl:PolyList)
    PolyList = conc(Poly*)
   }

  public Poly deriv(Poly p) {
    %match(Poly p) {
      m(_,"X",0) -> { return `m(0,"X",0); }
      m(c,"X",n) -> {
        if(`n>0) {
          return `m(c * n,"X",n-1);
        }
      }

      plus(conc(A*)) -> {
        return `plus(deriv(A*));
      }
      mult(conc(A,B*)) -> {
        Poly da = `deriv(A);
        PolyList db = `deriv(B*);
        return `plus(conc(mult(conc(da,B*)),
                        mult(conc(db*,A))));

      }
    }
    return `p;
  }

  public PolyList deriv(PolyList pl) {
    %match(PolyList pl) {
      conc(h,p*) -> {
        PolyList dp = `deriv(p*);
        return `conc(deriv(h),dp*);
      }
    }
    return `pl;
  }

  public Poly simplify(Poly p) {
    Poly res = oneStepSimplify(p);
    if(p != res) {
      res = simplify(res);
    }
    return res;
  }

  public Poly oneStepSimplify(Poly p) {
    %match(Poly p) {
      plus(conc(plus(conc(A*)),plus(conc(B*)))) -> {
        return `plus(conc(A*,B*));
      }
      plus(conc(A*,m(c1,"X",n),B*,m(c2,"X",n),C*)) -> {
        return `plus(conc(A*,m(c1+c2,"X",n),B*,C*));
      }
      plus(conc(A*,m1@m(_,"X",n1),B*,m2@m(_,"X",n2),C*)) -> {
        if(`n2>`n1) {
          return `plus(conc(A*,m2,B*,m1,C*));
        }
      }

      // congruence
      plus(conc(p1,p2)) -> { return `plus(conc(simplify(p1),simplify(p2))); }
    }
    return p;
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Poly X_2 = `m(1,"X",2);

    System.out.println("X_2 = " + X_2);
    System.out.println("X_2+3 = " + `plus(conc(X_2,m(3,"X",0))));
    System.out.println("deriv(X_2+3) = " + `deriv(plus(conc(X_2,m(3,"X",0)))));

    // 3*X2+X+7
    Poly P1 = `plus(conc(m(3,"X",2),m(1,"X",1),m(7,"X",0)));
    // 5*X2+3*X+4
    Poly P2 = `plus(conc(m(5,"X",2),m(3,"X",1),m(4,"X",0)));
    System.out.println("P1 = " + `P1);
    System.out.println("P2 = " + `P2);
    System.out.println("deriv(plus(P1,P2)) = " + `deriv(plus(conc(P1,P2))));
    System.out.println("deriv(plus(P1,P2)) = " + `simplify(deriv(plus(conc(P1,P2)))));

  }

  public final static void main(String[] args) {
    Poly2 test = new Poly2();
    test.run();
  }

}
