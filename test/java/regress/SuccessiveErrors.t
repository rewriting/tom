import regress.successiveerrors.simple.types.*;
public class SuccessiveErrors {
  %gom {
    module Simple 
      abstract syntax
      A = a()
        | f(n:A)
      B = b()
        | g(m:B)
  }

  public static void main(String[] args) {
    A tt = `a();
    %match{
      // a type error for the matching and another one for the bad typed term
      // f(b()) 
      x@b() << A tt && (g(x) == tt) -> { 
        System.out.println(`f(x)); 
      }
    }
  }
}
