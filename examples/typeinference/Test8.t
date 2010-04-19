import test8.test8.types.*;
public class Test8 {
  %gom {
    module Test8
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(x) << tt || f(f(x)) << tt -> { System.out.println("l1 = " + `x); }
    }
  }
}
