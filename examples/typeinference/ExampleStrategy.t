import examplestrategy.examplestrategy.types.*;
import tom.engine.exception.TomRuntimeException;
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
         | Test(x:Term)

    Term = a() | b()
  }
  public void run() {
    System.out.println("running...");
    Expr p1 = `Let("a",Cst(1), Var("a"));
    Expr new_p1 = propagate(p1);
    System.out.println(new_p1);

    Expr p2 = `Test(a());
    propagate(p2);
  }
  
  public final static void main(String[] args) {
    ExampleStrategy test = new ExampleStrategy();
    test.run();
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
      a() -> { System.out.println("Test!!"); }
    }
	}
}
