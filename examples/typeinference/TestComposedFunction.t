import testcomposedfunction.testcomposedfunction.types.*;
public class TestComposedFunction {
  %gom {
    module TestComposedFunction
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
