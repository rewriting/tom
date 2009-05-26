import poly.example.types.*;

public class Poly {

	%gom { 
	  module Example 
	  imports int
	  abstract syntax 
	 	 Expr = a() | b() | c()
	 	      | Plus(x:Expr,y:Expr)
	 	      | Mult(x:Expr,y:Expr)
	}

  public final static void main(String[] args) {
    Expr s;
    s = `Mult(a(),Plus(b(),c()));
    s = facto(distrib(s));
    System.out.println("s = " + s );

    s = `Plus(a(),Mult(a(),Plus(b(),c())));
    s = distrib(s);
    System.out.println("s = " + s );
  }

  public static Expr distrib(Expr e) {
    %match (e){
	  Mult(z,Plus(x,y)) -> { return `Plus(Mult(z,x),Mult(z,y)); }
	  Mult(Plus(x,y),z) -> { return `Plus(Mult(x,z),Mult(y,z)); }
    }
    return e;
  }

  public static Expr facto(Expr e) {
    %match (e){
	  Plus(Mult(z,x),Mult(z,y)) -> { return `Mult(z,Plus(x,y)); }
	  Plus(Mult(x,z),Mult(y,z)) -> { return `Mult(Plus(x,y),z); }
    }
    return e;
  }
}