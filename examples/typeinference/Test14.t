import test14.test14.types.*;
public class Test14 {
  %gom {
    module Test14
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
