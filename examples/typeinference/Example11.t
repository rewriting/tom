import example11.example.types.*;
public class Example11 {
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
      x << tt -> { System.out.println("l1 = " + `x); }
      f(x) << tt -> { System.out.println("l2 = " + `x); }
    }
  }
} 
