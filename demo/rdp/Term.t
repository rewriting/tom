import term.term.types.*;
public class Term {

  %gom {
    module Term
    imports String int
    abstract syntax

    T = | a()
        | b()
        | c()
        | f(x1:T, x2:T) 
        | g(x1:T)
        | zero()
        | s(x1:T)

     L = conc( T* )

  }

  public static T plus(T a1, T a2) {
    %match(T a1,a2) {
      x, zero() -> { return `x; }
      x, s(y)   -> { return `s(plus(x,y)); }
    }
      return null;
  }


  public final static void main(String[] args) {
    String t = "abcab";
    
    System.out.println("t = " + t);

    %match(t) {
      concString(C1*,x,_*,x,_*) -> { System.out.println("x = " + `x);  }
    }

  }

}

