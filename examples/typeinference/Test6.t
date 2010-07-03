import test6.test6.types.*;
public class Test6 {
  %gom {
    module Test6
      abstract syntax
      B = b()
        | f(n:B)
        | g(n:B)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      !b() << tt -> { System.out.println(`tt); }
      (f|g)(x) << tt -> { System.out.println(`x); }
    }
  }
}
