import examplelinearity.examplelinearity.types.*;
public class ExampleLinearity {
  %gom {
    module ExampleLinearity
      imports int
      abstract syntax
      B = b()
        | f(n:B)
        | g(m:int)
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      g(x) << B tt -> { System.out.println(`tt); }
      f(x) << B tt -> { System.out.println(`x); }
    }
  }
}
