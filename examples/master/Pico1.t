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
import master.pico1.term.types.*;

import java.util.*;

class Pico1 {
  %gom {
    module Term
    imports int String
      
    abstract syntax
		Bool = True() 
		     | False() 
		     | Not(b:Bool)
		     | Or(b1:Bool, b2:Bool) 
		     | And(b1:Bool, b2:Bool) 
		     | Eq(e1:Expr, e2:Expr) 

    Expr = Var(name:String) 
         | Cst(val:int) 
		     | Plus(e1:Expr, e2:Expr) 
		     | Mult(e1:Expr, e2:Expr) 
		     | Mod(e1:Expr, e2:Expr) 

		Inst = Skip() 
         | Assign(name:String, e:Expr) 
		     | Seq(i1:Inst, i2:Inst) 
		     | If(cond:Bool, i1:Inst, i2:Inst) 
		     | While(cond:Bool, i:Inst) 
		     | Print(e:Expr) 
  }

	public void eval(Map<String,Expr> env, Inst inst) {
		//System.out.println("eval: " + inst);
		%match(Inst inst) {
			Skip() -> {
				return;
			}

			Assign(name,e) -> {
				env.put(`name,evalExpr(env,`e));
				return;
			}

			Seq(i1,i2) -> {
				eval(env,`i1);
				eval(env,`i2);
				return;
			}
			
			If(b,i1,i2) -> {
				if(evalBool(env,`b)==`True()) {
					eval(env,`i1);
				} else {
					eval(env,`i2);
				}
				return;
			}
			
			w@While(b,i) -> {
				Bool cond = evalBool(env,`b);
				//System.out.println("cond = " + cond);
				if(cond==`True()) {
					eval(env,`i);
					eval(env,`w);
				}
				return;
			}

			Print(e) -> {
				System.out.println(evalExpr(env,`e));
				return;
			}
		}
		throw new RuntimeException("strange term: " + inst);
	}
 
	public Bool evalBool(Map env,Bool bool) {
		%match(Bool bool) {	
			Not(True()) -> { return `False(); }
			Not(False()) -> { return `True(); }
			Not(b) -> { return `evalBool(env,Not(evalBool(env,b))); }

			Or(True(),_) -> { return `True(); }
			Or(_,True()) -> { return `True(); }
			Or(False(),b2) -> { return `b2; }
			Or(b1,False()) -> { return `b1; }
			And(True(),b2) -> { return `b2; }
			And(b1,True()) -> { return `b1; }
			And(False(),_) -> { return `False(); }
			And(_,False()) -> { return `False(); }
			
			Eq(e1,e2) -> { 
				Expr x=`evalExpr(env,e1);
				Expr y=`evalExpr(env,e2);
				return (x==y)?`True():`False();
			}

			x -> { return `x; }
		}
		throw new RuntimeException("strange term: " + bool);
	}
	
	public Expr evalExpr(Map env,Expr expr) {
		%match(Expr expr) {	
			Var(n) -> { return (Expr)env.get(`n); }
			Plus(Cst(v1),Cst(v2)) -> { return `Cst(v1 + v2); }
			Mult(Cst(v1),Cst(v2)) -> { return `Cst(v1 * v2); }
			Mod(Cst(v1),Cst(v2)) -> { return `Cst(v1 % v2); }
			
			Plus(e1,e2) -> { return `evalExpr(env,Plus(evalExpr(env,e1),evalExpr(env,e2))); }
			Mult(e1,e2) -> { return `evalExpr(env,Mult(evalExpr(env,e1),evalExpr(env,e2))); }
			Mod(e1,e2) -> { return `evalExpr(env,Mod(evalExpr(env,e1),evalExpr(env,e2))); }
			
			x -> { return `x; }
		}
		throw new RuntimeException("strange term: " + expr);
	}

	public Inst opti(Inst inst) {
		%match(Inst inst) {
			If(Not(b),i1,i2) -> { return `opti(If(b,i2,i1)); }
			Seq(Skip(),i) -> { return `opti(i); }
			Seq(i,Skip()) -> { return `opti(i); }

			Seq(i1,i2) -> { return `Seq(opti(i1),opti(i2)); }
			While(b,i) -> { return `While(b,opti(i)); }
			x -> { return `x; }
		}
		throw new RuntimeException("strange term: " + inst);
	}
		
  //-------------------------------------------------------

  public void run() {
		Map<String,Expr> env = new HashMap<String,Expr>();

    System.out.println("running...");
    Inst p1 = `Seq(Assign("a",Cst(1)) , Print(Var("a")));
    System.out.println("p1: " + p1);
		eval(env,p1);

    Inst p2 = `Seq(Assign("b",Plus(Var("a"),Cst(2))) , Print(Var("b")));
    System.out.println("p2: " + p2);
		eval(env,p2);
    
		Inst p3 = `Seq(Assign("i",Cst(0)),
									 While(Not(Eq(Var("i"),Cst(10))),
										     Seq(Print(Var("i")),
													   Assign("i",Plus(Var("i"),Cst(1))))));
    System.out.println("p3: " + p3);
		eval(env,p3);

		Inst p4 = `Seq(Assign("i",Cst(0)),
				       Seq(Skip(),
									 If(Not(Eq(Var("i"),Cst(10))),
										     Seq(Print(Var("i")), Assign("i",Plus(Var("i"),Cst(1)))),Skip())));
    System.out.println("p4: " + p4);
    System.out.println("opti(p4): " + opti(p4));
		
		Inst p5 = `Seq(Assign("n",Cst(2)),
					   While(Not(Eq(Var("n"),Cst(100))),
									 Seq(Assign("i",Cst(2)),
									 Seq(Assign("p",Cst(1)),
									 Seq(While(Not(Eq(Var("i"),Var("n"))),
											 Seq(If(Eq(Mod(Var("n"),Var("i")),Cst(0)),Assign("p",Cst(0)),Skip()),
											 Assign("i",Plus(Var("i"),Cst(1)))
											 )),
									 Seq(If(Not(Eq(Var("p"),Cst(0))),Print(Var("n")),Skip()),
									 Assign("n",Plus(Var("n"),Cst(1)))
									 ))))));


    //System.out.println("p5: " + p5);
		//eval(env,p5);
    System.out.println("opti(p5): " + opti(p5));
		eval(env,opti(p5));
  }
  
  public final static void main(String[] args) {
    Pico1 test = new Pico1();
    test.run();
  }


}
