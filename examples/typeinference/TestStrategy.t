import teststrategy.teststrategy.types.*;
public class TestStrategy{
	%include { sl.tom }
	%include { java/util/types/HashMap.tom }

  %gom {
    module TestStrategy
    imports
        int String
      
    abstract syntax
    Expr = Var(name:String) 
         | Cst(val:int) 
         | Let(name:String, e:Expr, body:Expr) 
  }
  public void run() {

    System.out.println("running...");
    Expr p1 = `Print(Let("a",Cst(1), Var("a")));

		HashMap env = new HashMap();
  }
  
  public final static void main(String[] args) {
    TestStrategy test = new Backquote();
    test.run();
  }

	public Expr propagate(HashMap env, Expr expr) {
		try {
			return (Expr) `TopDown(Try(RenamedVar())).visitLight(expr);
		} catch (VisitFailure e) {
			System.out.println("strategy failed");
		}
		return expr;
	}

  %strategy RenamedVar() extends `Fail() {
		visit Expr {
			v@Var(('_',_*)) -> { return `v; }
		}
	}
}
