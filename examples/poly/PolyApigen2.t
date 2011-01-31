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

import static org.junit.Assert.*;
import org.junit.Test;

public class PolyApigen2 {

//modify it to change the complexity of t 
//beware: if you change this constant, junit tests have to be modified as well.
private final static int COMPLEXITY = 2;

  public PolyApigen2() {
  }

  %include { expression/expression.tom }

  %op Expression differentiate(arg1:Expression, arg2:Expression) {}

  public static Expression differentiate(Expression t1, Expression t2) {
    %match(t1,t2) {
      variable(v1),variable(v1) -> { return `one(); }
      plus(a1,a2),vx -> { return `plus(differentiate(a1,vx), differentiate(a2,vx)); }
      mult(a1,a2),vx -> { return `plus(mult(a1,differentiate(a2,vx)), mult(a2,differentiate(a1,vx))); }
      e@exp(a1),vx   -> { return `mult(differentiate(a1,vx),e); }
      variable(_),_  -> { return `zero(); }
      constant(_),_ -> { return `zero(); }
      number(_),_ -> { return `zero(); }
      zero(),_ -> { return `zero(); }
      one(),_ -> { return `zero(); }
    }
    return null;
  }
    
  // simplification
  public static Expression simplify(Expression t) {
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

  @Test
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
    
    //System.out.println("Derivative form of " + t + " wrt. " + var + " is:\n\t" + res);
    assertSame("Derivative form of exp(exp(variable(\"X\"))) wrt. variable(\"X\") is:",`plus(mult(mult(one(),exp(variable("X"))),mult(mult(one(),exp(variable("X"))),exp(exp(variable("X"))))),mult(exp(exp(variable("X"))),plus(mult(one(),mult(one(),exp(variable("X")))),mult(exp(variable("X")),zero())))),res);
    res = simplify(res);
    //System.out.println("Simplified form is:\n\t" + res);
    assertSame("Simplified form is:", `plus(mult(exp(variable("X")),mult(exp(variable("X")),exp(exp(variable("X"))))),mult(exp(exp(variable("X"))),exp(variable("X")))),res);
   
    t = `mult(variable("X"),plus(variable("X"),constant("a")));
    res = differentiate(t,res);  
    //System.out.println("Derivative form of " + t + " wrt. " + var + " is:\n\t" + res);
    assertSame("Derivative form of mult(variable(\"X\"),plus(variable(\"X\"),constant(\"a\"))) wrt. variable(\"X\") is:        plus(mult(variable(\"X\"),plus(zero,zero)),mult(plus(variable(\"X\"),constant(\"a\")),zero))",`plus(mult(variable("X"),plus(zero(),zero())),mult(plus(variable("X"),constant("a")),zero())),res);
    res = simplify(res);
    //System.out.println("Simplified form is:\n\t" + res);
assertSame("Simplified form is: zero",`zero(),res);
  }
    
  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(PolyApigen2.class.getName());
  }
}
