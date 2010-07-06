import example6.example.types.*;
public class Example6 {
  %gom {
    module Example
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
