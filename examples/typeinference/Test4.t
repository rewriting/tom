import test4.test4.types.*;
public class Test4 {
  %gom {
    module Test4
      abstract syntax
      B = b()
        | f(B*)
  }

  public static void main(String[] args) {
    B tt = `f(f(b()));
    %match{
      b() << tt -> { System.out.println(`tt); }
      f(x) << tt -> { System.out.println(`x); }
    }
  }
}
