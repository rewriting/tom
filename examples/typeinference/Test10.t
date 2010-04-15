import test10.test10.types.*;
public class Test10 {
  %gom {
    module Test10
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(a@f(x)) << tt -> { System.out.println("l1 = " + `a); }
    }
  }
}
