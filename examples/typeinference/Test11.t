import test11.test11.types.*;
public class Test11 {
  %gom {
    module Test11
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      x << tt -> { System.out.println("l1 = " + `x); }
      f(x) << tt -> { System.out.println("l2 = " + `x); }
    }
  }
}
