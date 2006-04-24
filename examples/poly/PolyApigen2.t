/*
 * Copyright (c) 2004-2006, INRIA
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

import expression.*;
import expression.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PolyApigen2 extends TestCase {

//modify it to change the complexity of t 
//beware: if you change this constant, junit tests have to be modified as well.
private final static int COMPLEXITY = 2;

  public PolyApigen2() {
  }

  %include { expression/expression.tom }

  %op Expression differentiate(arg1:Expression, arg2:Expression) {
    is_fsym(t) { false }
    make(t1,t2) { differentiate(t1,t2) }
  }

  %rule {
    differentiate(variable(v1),variable(v1)) -> one()
    differentiate(plus(a1,a2),vx) -> plus(differentiate(a1,vx),
                                          differentiate(a2,vx))
    differentiate(mult(a1,a2),vx) -> plus(mult(a1,differentiate(a2,vx)),
                                          mult(a2,differentiate(a1,vx)))
    differentiate(e@exp(a1),vx)   -> mult(differentiate(a1,vx),e)
    differentiate(variable(_),_)  -> zero()
    differentiate(constant(_),_) -> zero()
    differentiate(number(_),_)    -> zero()
    differentiate(zero(),_)         -> zero() 
    differentiate(one(),_)          -> zero()
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
        plus(x,y)    -> { res = `plus(simplify(x),simplify(y)); break block; }
        mult(x,y)    -> { res = `mult(simplify(x),simplify(y));  break block; }
        exp(x)       -> { res = `exp(simplify(x));  break block; }
      }
    }
    if(t != res) {
      res = simplify(res);
    }
    return res;
  }

  public void testX() {
    Expression var = `variable("X");
    Expression t = var;

      // build a tower of exponential
    for(int i=0 ; i<COMPLEXITY ; i++) {
      t = `exp(t);
    }
      // compute the n-th derivative form
    Expression res = t;
    for(int i=0 ; i<COMPLEXITY ; i++) {
      res = differentiate(res,var);
    }
    
    System.out.println("Derivative form of " + t + " wrt. " + var + " is:\n\t" + res);
    assertSame("Derivative form of exp(exp(variable(\"X\"))) wrt. variable(\"X\") is:",`plus(mult(mult(one(),exp(variable("X"))),mult(mult(one(),exp(variable("X"))),exp(exp(variable("X"))))),mult(exp(exp(variable("X"))),plus(mult(one(),mult(one(),exp(variable("X")))),mult(exp(variable("X")),zero())))),res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
    assertSame("Simplified form is:", `plus(mult(exp(variable("X")),mult(exp(variable("X")),exp(exp(variable("X"))))),mult(exp(exp(variable("X"))),exp(variable("X")))),res);
   
    t = `mult(variable("X"),plus(variable("X"),constant("a")));
    res = differentiate(t,res);  
    System.out.println("Derivative form of " + t + " wrt. " + var + " is:\n\t" + res);
    assertSame("Derivative form of mult(variable(\"X\"),plus(variable(\"X\"),constant(\"a\"))) wrt. variable(\"X\") is:        plus(mult(variable(\"X\"),plus(zero,zero)),mult(plus(variable(\"X\"),constant(\"a\")),zero))",`plus(mult(variable("X"),plus(zero(),zero())),mult(plus(variable("X"),constant("a")),zero())),res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
assertSame("Simplified form is: zero",`zero(),res);
  }
    
  public final static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(PolyApigen2.class));
  }
}
