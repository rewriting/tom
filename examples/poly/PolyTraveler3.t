/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package poly;

import poly.expression.*;
import poly.expression.types.*;
import tom.library.sl.*;

public class PolyTraveler3 {


  %include { expression/expression.tom }
  %include { sl.tom }

  public final static void main(String[] args) {
    PolyTraveler3 test = new PolyTraveler3();
    test.run(5);
  }

  public void run(int n) {
      // a literal string cannot be used in backquoted terms
    String X = "X";
    Expression var = `variable(X);
    Expression t = var;
      // build a tower of exponential
    for(int i=0 ; i<n ; i++) {
      t = `exp(t);
    }
      // compute the n-th derivative form
    for(int i=0 ; i<n ; i++) {
      t = differentiate(t,var);
    }
  
    long startChrono = System.currentTimeMillis();
    Expression resSymplify = simplify(t);
    //System.out.println("simplify in " + (System.currentTimeMillis()-startChrono)+ " ms");

    Strategy v = `SimplifyExpression();
    Strategy bottomUp = `BottomUp(Try(v));
    Strategy innermost = `Innermost(v);
    Strategy repeatOnce = `Repeat(OnceBottomUp(v));
    Expression resBottomUp = null;
    Expression resInnermost = null;
    Expression resRepeatOnce = null;
    try {
      startChrono = System.currentTimeMillis();
      resBottomUp = (Expression) bottomUp.visitLight(t);
      //System.out.println("BottomUp in " + (System.currentTimeMillis()-startChrono)+ " ms");

      startChrono = System.currentTimeMillis();
      resInnermost = (Expression) innermost.visitLight(t);
      //System.out.println("Innermost in " + (System.currentTimeMillis()-startChrono)+ " ms");

      startChrono = System.currentTimeMillis();
      resRepeatOnce = (Expression) repeatOnce.visitLight(t);
      //System.out.println("RepeatOneBottomUp in " + (System.currentTimeMillis()-startChrono)+ " ms");
      
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + t);
    }

    /*
    System.out.println("t           = " + t);
    System.out.println("resSymplify = " + resSymplify);
    System.out.println("bottomUp    = " + resBottomUp);
    System.out.println("repeatOnce  = " + resRepeatOnce);
    System.out.println("innermost   = " + resInnermost);
    */
    //System.out.println("check: " + (resBottomUp == resSymplify));
    //System.out.println("check: " + (resRepeatOnce == resBottomUp));
    //System.out.println("check: " + (resInnermost == resRepeatOnce));
  }
  
  public Expression differentiate(Expression e, Expression v) {
    %match(Expression e, Expression v) {
        // non-linear patterns are allowed
      variable(v1), variable(v1) -> { return `one(); }
      
      plus(a1,a2), vx -> {
        return `plus(differentiate(a1,vx),
                     differentiate(a2,vx));
      }
      mult(a1,a2), vx -> { 
        return `plus(mult(a1,differentiate(a2,vx)),
                     mult(a2,differentiate(a1,vx)));      
      }
      exp(a1), vx     -> { return `mult(differentiate(a1,vx),e); }
    }
    return `zero();
  }

    // simplification
  public Expression simplify(Expression t) {
    Expression res = t;
    block:{
      %match(Expression t) {
        exp(zero())    -> { res = `one(); break block; }
        plus(zero(),x) -> { res = simplify(`x); break block; }
        plus(x,zero()) -> { res = simplify(`x); break block; }
        mult(one(),x)  -> { res = simplify(`x); break block; }
        mult(x,one())  -> { res = simplify(`x); break block; }
        mult(zero(),_) -> { res = `zero(); break block; }
        mult(_,zero()) -> { res = `zero(); break block; }
        plus(x,y)      -> { res = `plus(simplify(x),simplify(y)); break block; }
        mult(x,y)      -> { res = `mult(simplify(x),simplify(y)); break block; }
        exp(x)         -> { res = `exp(simplify(x)); break block; }
      }
    }
    if(t != res) res = simplify(res);
    return res;
  }

  %strategy SimplifyExpression() extends `Fail() { 

    visit Expression {
      exp(zero())    -> { return `one(); }
      plus(zero(),x) -> { return `x; }
      plus(x,zero()) -> { return `x; }
      mult(one(),x)  -> { return `x; }
      mult(x,one())  -> { return `x; }
      mult(zero(),_) -> { return `zero(); }
      mult(_,zero()) -> { return `zero(); }
    }
  }

  /*class SimplifyExpression extends poly.expression.VisitableFwd {
    public SimplifyExpression() {
      super(new Fail());
    }
    
    public poly.expression.types.Expression visit_Expression(poly.expression.types.Expression arg) 
      throws VisitFailure { 
      %match(Expression arg) {
        exp(zero())    -> { return `one(); }
        plus(zero(),x) -> { return `x; }
        plus(x,zero()) -> { return `x; }
        mult(one(),x)  -> { return `x; }
        mult(x,one())  -> { return `x; }
        mult(zero(),_) -> { return `zero(); }
        mult(_,zero()) -> { return `zero(); }
      }
      throw new VisitFailure();
    }
   
  }*/


}

 




