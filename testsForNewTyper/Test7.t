import test7.test7.types.*;
public class Test7 {
  %gom {
    module Test7
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(x) << tt && f(b()) << x -> { System.out.println("l1 = " + `x); }
    }
  }
}
