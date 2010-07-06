import example8.example.types.*;
public class Example8 {
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
      f(x) << tt || f(f(x)) << tt -> { System.out.println("l1 = " + `x); }
    }
  }
}
