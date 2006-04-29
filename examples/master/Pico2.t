package master;
import master.pico2.term.types.*;

import java.util.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

class Pico2 {
	%include { mutraveler.tom }
	%include { java/util/types/HashMap.tom }

  %gom {
    module Term
    imports
        int String
      
    abstract syntax
		Bool = True() 
		     | False() 
		     | Neg(b:Bool)
		     | Or(b1:Bool, b2:Bool) 
		     | And(b1:Bool, b2:Bool) 
		     | Eq(e1:Expr, e2:Expr) 

    Expr = Var(name:String) 
         | Cst(val:int) 
         | Let(name:String, e:Expr, body:Expr) 
		     | Seq( Expr* )
		     | If(cond:Bool, e1:Expr, e2:Expr) 
		     | Print(e:Expr)
		     | Plus(e1:Expr, e2:Expr)
  }

	public Expr opti(Expr expr) {
		%match(Expr expr) {
			If(Neg(b),i1,i2) -> { return `opti(If(b,i2,i1)); }
			Seq(X*,Seq(),Y*) -> {
				if(`X*.isEmptySeq() && `Y*.isEmptySeq()) {
					return `Seq();
				} else {
					return `opti(Seq(X*,Y*)); 
				}
			}

			Seq(head,tail*) -> { return `Seq(opti(head),opti(tail*)); }
			x -> { return `x; }
		}
		throw new RuntimeException("strange term: " + expr);
	}
		
  //-------------------------------------------------------

  public void run() {

    System.out.println("running...");
    Expr p1 = `Print(Let("a",Cst(1), Var("a")));
    Expr p2 = `Print(Let("b",Plus(Var("a"),Cst(2)), Var("b")));
		Expr p4 = `Let("i",Cst(0),
									 If(Neg(Eq(Var("i"),Cst(10))),
										     Seq(Print(Var("i")), Let("i",Plus(Var("i"),Cst(1)),Var("i"))),Seq()));

		System.out.println("p1 = " + p1);
		printCst(p1);
		System.out.println("p4 = " + p4);
		printCst(p4);

		System.out.println("Optimize...");
		System.out.println("p1 = " + p1);
		optimize(p1);
		System.out.println("p2 = " + p2);
		optimize(p2);
		System.out.println("p4 = " + p4);
		optimize(p4);
		
		System.out.println("Propagate...");
		System.out.println("p1 = " + p1);
		HashMap env = new HashMap();
		propagate(env,p1);
  }
  
  public final static void main(String[] args) {
    Pico2 test = new Pico2();
    test.run();
  }

	public void printCst(Expr expr) {
		try {
			`TopDown(Try(stratPrintCst())).visit(expr);
			`TopDown(Try(Sequence(FindCst(),PrintTree()))).visit(expr);
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
	}

	public void optimize(Expr expr) {
		try {
			//`Sequence(TopDown(Try(stratRenameVar())),PrintTree()).visit(expr);
			//`Repeat(Sequence(OnceBottomUp(stratRenameVar()),PrintTree())).visit(expr);
			//`Repeat(Sequence(OnceBottomUp(Sequence(Not(RenamedVar()),stratRenameVar())),PrintTree())).visit(expr);
			//`Sequence(Innermost(Sequence(Not(RenamedVar()),stratRenameVar())),PrintTree()).visit(expr);

			`Sequence(Innermost(OptIf()),PrintTree()).visit(expr);
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
	}

  %strategy stratPrintCst() extends `Fail() {
		visit Expr {
			Cst(x) -> { System.out.println("cst: " + `x); }
		}
	}
  
  %strategy FindCst() extends `Fail() {
		visit Expr {
			c@Cst(x) -> { return `c; }
		}
	}

	%strategy PrintTree() extends `Identity() {
		visit Expr { 
			x -> { System.out.println(`x); }
		}
	}
	
	%strategy stratRenameVar() extends `Fail() {
		visit Expr {
			Var(name) -> { return `Var("_"+name); }
		}
	}
  
	%strategy RenamedVar() extends `Fail() {
		visit Expr {
			v@Var(('_',name*)) -> { return `v; }
		}
	}

	%strategy OptIf() extends `Fail() {
		visit Expr {
			If(Neg(b),i1,i2) -> { return `If(b,i2,i1); }
		}
	}

	public Expr propagate(HashMap env, Expr expr) {
		try {
			return (Expr) `TopDown(Try(PropagateCst(env))).visit(expr);
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
		return expr;
	}

/*
	private boolean noAssign(String n, Expr expr) {
		%match(Expr I) {
			Seq(_*,Assign(name,_),_*) -> {
				if(name==n) {
					return false;
				}
			}
		}
		return true;
	}
*/

	%strategy PropagateCst(m:HashMap) extends `Fail() {
		visit Expr {
			l@Let(n,e,b) -> { 
				Expr newE = (Expr) this.visit(`e);
				m.put(`n,newE);
				return `l;
			}

			Var(n) -> {
				if(m.containsKey(`n)) {
					return (Expr) m.get(`n);
				}
			}
		}
	}
}
