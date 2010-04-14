import test5.test5.types.*;
public class Test5 {
  %gom {
    module Test5
      abstract syntax
      B = b()
        | f(B*)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      !b() << tt -> { System.out.println(`tt); }
      f(x) << tt -> { System.out.println(`x); }
    }
  }
}
