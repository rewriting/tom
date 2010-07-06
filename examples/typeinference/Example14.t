import example14.example.types.*;
public class Example14 {
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
      f(x) << tt -> { 
        System.out.println("l1 = " + `x);
        %match{
          f(y) << x -> { System.out.println("l2 = " + `y); }
        }
      }
    }
  }
}
