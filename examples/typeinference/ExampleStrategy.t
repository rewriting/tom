/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 *  - Neither the name of the Inria nor the names of its
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
package typeinference;

import typeinference.examplestrategy.examplestrategy.types.*;
import tom.library.sl.*;

public class ExampleStrategy{
	%include { sl.tom }

  %gom {
    module ExampleStrategy
    imports
        int String
      
    abstract syntax
    Expr = Var(name:String) 
         | Cst(val:int) 
         | Let(name:String, e:Expr, body:Expr) 
         | Call(x:Term)

    Term = a() | b()
  }
  public void run() {

    System.out.println("running...");
    Expr p1 = `Let("a",Cst(1), Var("a"));
    Expr new_p1 = propagate(p1);
    System.out.println(new_p1);

		Expr p2 = `Call(a());
    propagate(p2);
  }
  
  public final static void main(String[] args) {
    ExampleStrategy ex = new ExampleStrategy();
    ex.run();
  }

	public Expr propagate(Expr expr) {
		try {
			return (Expr) `TopDown(Try(RenamedVar())).visitLight(expr);
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
		return expr;
	}

  %strategy RenamedVar() extends `Fail() {
    visit Expr {
      v@Var(concString('_',_*)) -> { return `v; }
    }

    visit Term {
      a() -> { System.out.println("Call!!"); }
    }
  }
}
