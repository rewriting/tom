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

import master.matching3.peano.types.*;

public class Matching3 {

  %gom {
    module Peano
    imports String
    abstract syntax
    Nat = var(n:String)
        | zero()
        | suc(pred:Nat)
        | plus(x1:Nat, x2:Nat)
        | True()
        | False()
        | Match(pattern:Nat, subject:Nat)
        | And(n1:Nat,n2:Nat)
   }

  public Nat solve(Nat p) {
    Nat res = oneStepSolve(p);
    if(p != res) {
      res = solve(res);
    }
    return res;
  }

  public Nat oneStepSolve(Nat p) {
    %match(Nat p) {

      // Delete
      Match(zero(),zero()) -> { return `True();}

      // Decompose
      Match(suc(x),suc(y)) -> { return `Match(x,y);}
      Match(plus(x1,x2),plus(y1,y2)) -> {
        return `And(Match(x1,y1),Match(x2,y2));}

    // SymbolClash
      Match(suc(_),zero()) -> { return `False() ;}
      Match(zero(),suc(_)) -> { return `False() ;}
      Match(plus(_,_),zero()) -> { return `False() ;}
      Match(zero(),plus(_,_)) -> { return `False() ;}
      Match(suc(_),plus(_,_)) -> { return `False() ;}
      Match(plus(_,_),suc(_)) -> { return `False() ;}

    // PropagateClash
      And(False(),_) -> { return `False() ;}
      And(_,False()) -> { return `False() ;}

    // PropagateSuccess
      And(True(),X) -> { return `X ;}
      And(X,True()) -> { return `X ;}

    // Merging
      And(X,X) -> { return `X ;}
      And(X,And(X,Y)) -> { return  `And(X,Y);}

    // MergingFail
      And(Match(var(x),X),Match(var(x),Y)) -> {
        if(`X!=`Y){
          return `False() ;
        }
      }
      And(Match(var(x),X),And(Match(var(x),Y),_)) -> {
        if(`X!=`Y){
          return `False() ;
        }
      }

    // Sort
      And(Match(var(x),X),Match(var(y),Y)) -> {
        if(`x.compareTo(`y)<0){
          return `And(Match(var(y),Y),Match(var(x),X)) ;
        }
      }
      And(Match(var(x),X),And(Match(var(y),Y),P)) -> {
        if(`x.compareTo(`y)<0){
          return `And(Match(var(y),Y),And(Match(var(x),X),P)) ;
        }
      }

     // Assoc
        And(And(p1,p2),p3) -> { return `And(p1, And(p2,p3)); }

    // congruence
        And(p1,p2) -> { return `And(solve(p1),solve(p2)); }
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
    Nat start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(pxy,pzx);
    t2 = `plus(p00,p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(yy,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,pxy);
    t2 = `plus(zero(),p00);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
    t1 = `plus(xx,xx);
    t2 = `plus(zero(),one);
    start = `solve(Match(t1,t2));
    System.out.println("Match("+`t1+","+`t2+") = " + `start);
  }

  public final static void main(String[] args) {
    Matching3 test = new Matching3();
    test.run();
  }

}
