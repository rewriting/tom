import test3.test3.types.*;
public class Test3 {
  %gom {
    module Test3
      abstract syntax
      B = b()
        | f(n:B)
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << tt -> { System.out.println(`tt); }
      f(x) << tt -> { System.out.println(`x); }
    }
  }
}
