/*
 * Copyright (c) 2004-2005, INRIA
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
import tom.library.traversal.*;

public class PolyAdvanced3 {
    
  private ATermFactory factory;
  private GenericTraversal traversal;

  public PolyAdvanced3(ATermFactory factory) {
    this.factory = factory;
    this.traversal = new GenericTraversal();
  }

  %include { Poly.signature }
  
    // Simplified version of differentiate
  public ATermAppl differentiate(ATermAppl poly, ATermAppl variable) {
    %match(term poly, term variable) {
      X(), X()          -> { return `one(); }
      Y(), Y()          -> { return `one(); }
      plus(a1,a2), var  -> { return `plus(differentiate(a1, var),
                                          differentiate(a2, var)); }
      mult(a1,a2), var  -> { 

        ATermAppl res1, res2;
        res1 =`mult(a1, differentiate(a2, var));
        res2 =`mult(a2, differentiate(a1, var));
        return `plus(res1,res2);
      }
    }
    return `zero();
  }

    // Simplification using a traversal function
  public ATermAppl simplify(ATermAppl t) {
    Replace1 replace = new Replace1() {
        public ATerm apply(ATerm t) {
          %match(term t) {
            plus(zero(),x) -> { return `x; }
            plus(x,zero()) -> { return `x; }
            mult(one(),x)  -> { return `x; }
            mult(x,one())  -> { return `x; }
            mult(zero(),_) -> { return `zero(); }
            mult(_,zero()) -> { return `zero(); }
            _ -> { return traversal.genericTraversal(t,this); }
          }
        }
      };

    ATermAppl res = (ATermAppl)replace.apply(t);
    if(res != t) {
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
    PolyAdvanced3 test = new  PolyAdvanced3(new PureFactory());
    test.run();
  }
}

