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
import master.ruleevaluator.expression.types.*;

class RuleEvaluator {
  %gom {
    module Expression
      imports int 
      abstract syntax
      Expr = Cst(val:int) 
           | Plus(e1:Expr, e2:Expr) 
           | Mult(e1:Expr, e2:Expr) 
    module Expression:rules() {
      Plus(Cst(v1),Cst(v2)) -> Cst(intplus(v1,v2))
      Mult(Cst(v1),Cst(v2)) -> Cst(intmult(v1,v2))
    }

    Plus:block() { private static int intplus(int x,int y) { return x+y; } }
    Mult:block() { private static int intmult(int x,int y) { return x*y; } }

  }

  
  //-------------------------------------------------------

  public final static void main(String[] args) {
    System.out.println("running...");
    Expr e1 = `Plus(Cst(1),Cst(2));
    System.out.println("e1" + " --> " + e1);
    
    Expr e2 = `Plus(Mult(Cst(3),Cst(4)),Cst(1));
    System.out.println("e2" + " --> " + e2);
    
  }


}
