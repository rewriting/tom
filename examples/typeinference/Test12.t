import test12.test12.types.*;
public class Test12 {
  %gom {
    module Test12
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      x << tt -> { System.out.println("l1 = " + `f(x)); }
    }
  }
}
