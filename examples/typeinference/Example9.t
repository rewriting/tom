import example9.example.types.*;
public class Example9 {
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
      f(x) << tt && (x != b()) -> { System.out.println("l1 = " + `x); }
    }
  }
}
