import test2.test2.types.*;
public class Test2 {
  %gom {
    module Test2
      abstract syntax
      B = b()
        | f(n:B)
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << B tt -> { System.out.println(`tt); }
      f(x) << B tt -> { System.out.println(`x); }
    }
  }
}
