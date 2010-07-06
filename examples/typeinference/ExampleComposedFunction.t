import examplecomposedfunction.examplecomposedfunction.types.*;
public class ExampleComposedFunction {
  %gom {
    module ExampleComposedFunction
      imports int
      abstract syntax
      B = b()
        | f(n1:B)
        | g(n2:int)
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      f(g(x)) << B tt -> { System.out.println(`x); }
    }
  }
}
