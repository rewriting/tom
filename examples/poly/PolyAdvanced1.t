/*
 * Copyright (c) 2004, INRIA
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

import aterm.*;
import aterm.pure.*;

public class PolyAdvanced1 {
    
  private ATermFactory factory;
  public PolyAdvanced1(ATermFactory factory) {
    this.factory = factory;
  }

  %include { Poly.signature }
  
    // Everything is now AtermAppl to avoid casting:
  public ATermAppl differentiate(ATermAppl poly, ATermAppl variable) {
    %match(term poly, term variable) {
      X(), X() | Y(), Y() -> { return `one(); }
      plus(a1,a2), var  -> { return `plus(differentiate(a1, var),differentiate(a2, var)); }
      mult(a1,a2), var  -> { 
        ATermAppl res1, res2;
        res1 =`mult(a1, differentiate(a2, var));
        res2 =`mult(a2, differentiate(a1, var));
        return `plus(res1,res2);
      }
      X(), var | Y(), var | a(), var | b(), var | c(), var
            -> { return `zero(); }

      _ , _ -> { System.out.println("No match for: " + poly +" , "+variable); }
	    
    }
    return null;
  }
    
    // Improved simplification
  public ATermAppl simplify(ATermAppl t) {
    ATermAppl res = t;
    block:{
      %match(term t) {
        plus(zero(), x) | plus(x, zero()) |
        mult(one(), x)  | mult(x, one())  -> { res = simplify(`x);  break block; }
        mult(zero(), _) | mult(_, zero()) -> { res = `zero();       break block; }
        plus(x,y) -> { res = `plus( simplify(x), simplify(y) ); break block; }
        mult(x,y) -> { res = `mult( simplify(x), simplify(y) ); break block; }
      }
    }
    if(t != res) {
      res = simplify(res);
    }
    return res;
  }

  public void run() {
    ATermAppl t    = `mult(X(),plus(X(),a()));
    ATermAppl var1 = `X();
    ATermAppl var2 = `Y();
    ATermAppl res;
    res = differentiate(t,var1);
    System.out.println("Derivative form of " + t + " wrt. " + var1 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
    
    res = differentiate(t,var2);
    System.out.println("Derivative form of " + t + " wrt. " + var2 + " is:\n\t" + res);
    res = simplify(res);
    System.out.println("Simplified form is:\n\t" + res);
  }
  
  public final static void main(String[] args) {
    PolyAdvanced1 test = new  PolyAdvanced1(new PureFactory());
    test.run();
  }
}

