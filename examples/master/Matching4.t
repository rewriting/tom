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

import master.matching4.peano.types.*;

public class Matching4 {

  %gom {
    module Peano
    imports String
    abstract syntax
    Nat = var(n:String)
        | zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
    Mat = True()
        | False()
        | Match(pattern:Nat, subject:Nat)
    MatList = And(Mat*)
   }

  public MatList solve(MatList p) {
    p = `And(p*,True());
    MatList res = oneStepSolve(p);
    if(p != res) {
      res = solve(res);
    }
    return res;
  }

  public MatList oneStepSolve(MatList p) {
    %match(MatList p) {
      // Delete
      And(X*,Match(zero(),zero()),Y*) -> { return `And(X*,True(),Y*);}

      // Decompose
      And(X*,Match(suc(x),suc(y)),Y*) -> { return `And(X*,Match(x,y),Y*);}
      And(_*,Match(plus(x1,x2),plus(y1,y2)),_*) -> {
        return `And(Match(x1,y1),Match(x2,y2));}

      // SymbolClash
      And(X*,Match(suc(_),zero()),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(zero(),suc(_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(plus(_,_),zero()),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(zero(),plus(_,_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(suc(_),plus(_,_)),Y*) -> { return `And(X*,False(),Y*) ;}
      And(X*,Match(plus(_,_),suc(_)),Y*) -> { return `And(X*,False(),Y*) ;}

      // PropagateClash
      And(X*,False(),Y*) -> { return `And(X*,False(),Y*) ;}

      // PropagateSuccess
      And(X*,True(),Y*) -> { return `And(X*,Y*);}

      // Merging
      And(Z*,X,Y*,X,T*) -> { return `And(Z*,X,Y*,T*) ;}

      // MergingFail
      And(Z*,Match(var(x),X),U*,Match(var(x),Y),T*) -> {
        if(`X!=`Y){
          return `And(Z*,False(),U*,T*) ;
        }
      }

      // congruence
      And(p1,p2*) -> {
        MatList s1=`solve(And(p1,True()));
        MatList s2=`solve(p2);
        return `And(s1*,s2*);
      }
    }
    return p;
  }

  //-------------------------------------------------------

  public void run() {
    System.out.println("running...");
    Nat xx=`var("x");
    Nat yy=`var("y");
    Nat zz=`var("z");

    Nat one = `suc(zero());
    Nat two = `suc(one);

    Nat pxy = `plus(xx,yy);
    Nat pzx = `plus(zz,xx);
    Nat px0 = `plus(xx,zero());
    Nat py0 = `plus(yy,zero());
    Nat p00 = `plus(zero(),zero());
    Nat p01 = `plus(zero(),one);
    Nat px2 = `plus(xx,two);
    Nat p22 = `plus(two,two);
    Nat p1p1p11 = `plus(one,plus(one,plus(one,one)));
    Nat pp11p11 = `plus(plus(one,one),plus(one,one));

//     Nat start = `Match(p22,p22);
//     Nat start = `Match(xx,p22);
    Nat t1 = `plus(pxy,pxy);
    Nat t2 = `plus(p00,p01);
    MatList start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(pxy,pzx);
    t2 = `plus(p00,p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(yy,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,xx);
    t2 = `plus(zero(),one);
    start = `solve(And(Match(zero(),zero()),Match(t1,t2)));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
  }

  public final static void main(String[] args) {
    Matching4 test = new Matching4();
    test.run();
  }

}
