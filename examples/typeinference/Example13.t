import example13.example.types.*;
public class Example13 {
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
      f(x) << tt -> { System.out.println("l1 = " + `x); }
    }

    %match{
      g(x) << tt -> { System.out.println("l2 = " + `x); }
    }
  }
}
