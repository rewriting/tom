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

import poly.poly.types.*;

import static org.junit.Assert.*;
import org.junit.Test;

import tom.library.sl.Strategy;

public class PolyAdvanced1 {
    
  Term t;
  Term var1;
  Term var2;
  Term res;

  public PolyAdvanced1() {
    t    = `mult(X(),plus(X(),a()));
    var1 = `X();
    var2 = `Y();
  }

  %include { poly/Poly.tom }
  
    // Everything is now of type Term to avoid casting:
  public Term differentiate(Term p, Term variable) {
    %match(Term p, Term variable) {
      X(), X() -> { return `one(); }
      Y(), Y() -> { return `one(); }
      plus(a1,a2), var  -> { return `plus(differentiate(a1, var),differentiate(a2, var)); }
      mult(a1,a2), var  -> { 
        Term res1, res2;
        res1 =`mult(a1, differentiate(a2, var));
        res2 =`mult(a2, differentiate(a1, var));
        return `plus(res1,res2);
      }
      (X|Y|a|b|c)(), _ -> { return `zero(); }

      _ , _ -> { System.out.println("No match for: " + p +" , "+variable); }
	    
    }
    return null;
  }
    
    // Improved simplification
  public Term simplify(Term t) {
    Term result = t;
    block:{
      %match(Term t) {
        plus(zero(), x) -> { result = simplify(`x);  break block; }
        plus(x, zero()) -> { result = simplify(`x);  break block; }
        mult(one(), x)  -> { result = simplify(`x);  break block; }
        mult(x, one())  -> { result = simplify(`x);  break block; }
        mult(zero(), _) -> { result = `zero();       break block; }
        mult(_, zero()) -> { result = `zero();       break block; }
        plus(x,y) -> { result = `plus( simplify(x), simplify(y) ); break block; }
        mult(x,y) -> { result = `mult( simplify(x), simplify(y) ); break block; }
      }
    }
    if(t != result) {
      result = simplify(result);
    }
    return result;
  }

  @Test
  public void testX() {
    res = differentiate(t,var1);
    //System.out.println("Derivative form of " + t + " wrt. " + var1 + " is:\n\t" + res);
    assertSame("differentiate(mult(X,plus(X,a)),X) is plus(mult(X,plus(1,0)),mult(plus(X,a),1))",`plus(mult(X(),plus(one(),zero())),mult(plus(X(),a()),one())),res);
    res = simplify(res);
    assertSame("simplify(plus(mult(X,plus(1,0)),mult(plus(X,a),1))) is plus(X(),plus(X,a))",`plus(X(),plus(X(),a())),res);
    //System.out.println("Simplified form is:\n\t" + res);
   } 

  @Test
  public void testY() {
    res = differentiate(t,var2);
    //System.out.println("Derivative form of " + t + " wrt. " + var2 + " is:\n\t" + res);
    assertSame("differentiate(mult(X,plus(X,a)),Y) is plus(mult(X,plus(0,0)),mult(plus(X,a),0))",`plus(mult(X(),plus(zero(),zero())),mult(plus(X(),a()),zero())),res);
    res = simplify(res);
    //System.out.println("Simplified form is:\n\t" + res);
    assertSame("simplify(plus(mult(X,plus(1,0)),mult(plus(X,a),1))) is 0",`zero(),res);
  }
  
  public final static void main(String[] args) {
    org.junit.runner.JUnitCore.main(PolyAdvanced1.class.getName());
  }
}

