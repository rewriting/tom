import example3.example.types.*;
public class Example3 {
  %gom {
    module Example
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
