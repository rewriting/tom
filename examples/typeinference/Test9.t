import test9.test9.types.*;
public class Test9 {
  %gom {
    module Test9
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(x) << tt && (x != b()) -> { System.out.println("l1 = " + `x); }
    }
  }
}
