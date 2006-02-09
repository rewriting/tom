package master;

import aterm.pure.SingletonFactory;
import master.pico1.term.*;
import master.pico1.term.types.*;

import java.util.*;

class Pico1 {
  %vas {
    module Term
    imports 
    public
    sorts Inst Expr Bool
      
    abstract syntax
		True -> Bool
		False -> Bool
		Not(b:Bool) -> Bool
		Or(b1:Bool, b2:Bool) -> Bool
		And(b1:Bool, b2:Bool) -> Bool
		Eq(e1:Expr, e2:Expr) -> Bool

    Var(name:String) -> Expr
    Cst(val:int) -> Expr
		Plus(e1:Expr, e2:Expr) -> Expr
		Mult(e1:Expr, e2:Expr) -> Expr

		Skip() -> Inst
    Assign(name:String, e:Expr) -> Inst
		Seq(i1:Inst, i2:Inst) -> Inst
		If(cond:Bool, i1:Inst, i2:Inst) -> Inst
		While(cond:Bool, i:Inst) -> Inst
		Print(e:Expr) -> Inst

  }

	public void eval(Map env, Inst inst) {
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
					eval(env,w);
				}
				return;
			}

			Print(e) -> {
				System.out.println(evalExpr(env,`e));
				return;
			}
		}
	}
 
	public Bool evalBool(Map env,Bool bool) {
		%match(Bool bool) {	
			Not(True()) -> { return `False(); }
			Not(False()) -> { return `True(); }
			Not(b) -> { return `evalBool(env,Not(evalBool(env,b))); }

			Or(True(),b2) -> { return `True(); }
			Or(b1,True()) -> { return `True(); }
			Or(False(),b2) -> { return `b2; }
			Or(b1,False()) -> { return `b1; }
			And(True(),b2) -> { return `b2; }
			And(b1,True()) -> { return `b1; }
			And(False(),b2) -> { return `False(); }
			And(b1,False()) -> { return `False(); }
			
			Eq(e1,e2) -> { 
				Expr x=evalExpr(env,e1);
				Expr y=evalExpr(env,e2);
				return (x==y)?`True():`False();
			}

			x -> { return x; }
		}
	}
	
	public Expr evalExpr(Map env,Expr expr) {
		%match(Expr expr) {	
			Var(n) -> { return (Expr)env.get(n); }
			Plus(Cst(v1),Cst(v2)) -> { return `Cst(v1 + v2); }
			Mult(Cst(v1),Cst(v2)) -> { return `Cst(v1 * v2); }
			
			Plus(e1,e2) -> { return `evalExpr(env,Plus(evalExpr(env,e1),evalExpr(env,e2))); }
			Mult(e1,e2) -> { return `evalExpr(env,Mult(evalExpr(env,e1),evalExpr(env,e2))); }
			
			x -> { return `x; }
		}
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
	}
		

  //-------------------------------------------------------
  public Pico1() {
  } 

  public void run() {
		Map env = new HashMap();

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
  }
  
  public final static void main(String[] args) {
    Pico1 test = new Pico1();
    test.run();
  }


}
