import example10.example.types.*;
public class Example10 {
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
      f(a@f(x)) << tt -> { System.out.println("l1 = " + `a); }
    }
  }
}
