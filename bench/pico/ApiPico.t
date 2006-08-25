package pico;
import pico.apipico.term.types.*;

import java.util.*;

public class ApiPico {
  %vas {
    module Term
    public
      sorts Bool Expr Inst
      abstract syntax
        True() -> Bool
        False()  -> Bool
        Not(b:Bool) -> Bool
        Or(b1:Bool, b2:Bool)  -> Bool
        And(b1:Bool, b2:Bool)  -> Bool
        Eq(e1:Expr, e2:Expr)  -> Bool

        Var(name:String) -> Expr
        Cst(val:int)  -> Expr
        Plus(e1:Expr, e2:Expr)  -> Expr
        Mult(e1:Expr, e2:Expr)  -> Expr
        Mod(e1:Expr, e2:Expr)  -> Expr

        Skip() -> Inst
        Assign(name:String, e:Expr)  -> Inst
        Seq(i1:Inst, i2:Inst)  -> Inst
        If(cond:Bool, i1:Inst, i2:Inst)  -> Inst
        While(cond:Bool, i:Inst)  -> Inst
        Print(e:Expr)  -> Inst
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
					eval(env,`w);
				}
				return;
			}

			Print(e) -> {
				//System.out.println(evalExpr(env,`e));
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

			Or(True(),b2) -> { return `True(); }
			Or(b1,True()) -> { return `True(); }
			Or(False(),b2) -> { return `b2; }
			Or(b1,False()) -> { return `b1; }
			And(True(),b2) -> { return `b2; }
			And(b1,True()) -> { return `b1; }
			And(False(),b2) -> { return `False(); }
			And(b1,False()) -> { return `False(); }
			
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
			Plus(e1,e2) -> {
        return `evalExpr(env,Plus(evalExpr(env,e1),evalExpr(env,e2)));
      }
			Mult(e1,e2) -> {
        return `evalExpr(env,Mult(evalExpr(env,e1),evalExpr(env,e2)));
      }
			Mod(e1,e2) -> {
        return `evalExpr(env,Mod(evalExpr(env,e1),evalExpr(env,e2)));
      }
			
			x -> { return `x; }
		}
		throw new RuntimeException("strange term: " + expr);
	}

  public void run(int max) {
		Map env = new HashMap();
    env.put("MAX",`Cst(max));

    // Crible d'Erathosthene
		Inst crible = `Seq(Assign("n",Cst(2)),
				           While(Not(Eq(Var("n"),Var("MAX"))),
									 Seq(Assign("i",Cst(2)),
									 Seq(Assign("p",Cst(1)),
									 Seq(While(Not(Eq(Var("i"),Var("n"))),
											 Seq(If(Eq(Mod(Var("n"),Var("i")),Cst(0)),Assign("p",Cst(0)),Skip()),
											 Assign("i",Plus(Var("i"),Cst(1)))
											 )),
									 Seq(If(Not(Eq(Var("p"),Cst(0))),Print(Var("n")),Skip()),
									 Assign("n",Plus(Var("n"),Cst(1)))
									 ))))));

    System.out.print(max);
    long startChrono = System.currentTimeMillis();
		eval(env,crible);
		long stopChrono = System.currentTimeMillis();
		System.out.println("\t" + (stopChrono-startChrono)/1000.);
  }
  
  public final static void main(String[] args) {
    int criblemax = 0;
    try {
      criblemax = Integer.parseInt(args[0]);
    } catch (Exception e) {
      System.out.println("Usage: java pico.ApiPico <criblemax>");
      return;
    }
    ApiPico test = new ApiPico();
    test.run(criblemax);
  }
}
