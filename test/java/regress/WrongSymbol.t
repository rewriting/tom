import regress.wrongsymbol.example.types.*;
public class WrongSymbol {
  %gom {
    module Example
      abstract syntax
      A = a()
      | f(n:A)
  }

  public static void main(String[] args) {
    A t = `f(a());
    %match {
      f(x*) << t -> { 
        System.out.println("x = " + `x);
      }
    }
  }
}
