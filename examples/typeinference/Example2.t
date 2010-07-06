import example2.example.types.*;
public class Example2 {
  %gom {
    module Example
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
