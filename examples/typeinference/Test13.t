import test13.test13.types.*;
public class Test13 {
  %gom {
    module Test13
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      f(x) << tt -> { System.out.println("l1 = " + `x); }
    }

    %match{
      g(x) << tt -> { System.out.println("l2 = " + `x); }
    }
  }
}
