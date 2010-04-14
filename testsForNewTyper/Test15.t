import test15.test15.types.*;
public class Test15 {
  %gom {
    module Test15
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(x) << tt && (f(b()) << x || b() << x) -> { System.out.println("l1 = " + `x); }
    }
  }
}
