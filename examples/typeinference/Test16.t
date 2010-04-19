import test16.test16.types.*;
public class Test16 {
  %gom {
    module Test16
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

    %match{
      g(x) << tt -> { System.out.println("l3 = " + `x); }
    }
  }
}
