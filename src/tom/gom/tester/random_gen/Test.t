import test.truc.types.*;
import tom.library.sl.*;
public class Test {
	%include { sl.tom }
	%gom{
		module truc
		abstract syntax
		Expr = zero()
			| un()
			| plus(a:Expr, b:Expr)
			| mult(a:Expr, b:Expr)
	}
	
	%strategy Mod() extends Fail(){
		visit Expr {
			zero()->un()
		}
	}
	
	public static Expr rand_gen(){
		int choice = (int)(Math.random()*4);
		switch(choice) {
			case 0:
				return `zero();
			case 1:
				return `un();
			case 2:
				return `plus(rand_gen(), rand_gen());
			case 3:
				return `mult(rand_gen(), rand_gen());
		}
		return null;
	}
	
	public static void main(String[] args) {
		Expr a = rand_gen();
		System.out.println(a);
	}
}
