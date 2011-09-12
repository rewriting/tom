import regress.testnonlinearity.test.types.*;
public class TestNonLinearity {
  %gom {
    module Test
      abstract syntax
      A = a()
      | inc(num:B)
      | inc2(num2:A)

      B = b()
      | plus(p1:B,p2:B)
      | f(B*)
      | g(B*)
  }

  public static void main(String[] args) {
    A t = `inc(b());
    %match {
      inc(x) << A t 
        && (inc2(x) << inc2(a()) && (x == a())) -> {
          System.out.println("Match imbrique: " + `x);
        }
    }

    B tt1 = `f(f(b()));
    B tt2 = `g(f(b()));
    B tt3 = `f();
    B tt4 = `g();
    %match{
      f(x*) << tt1 && g(x*) << tt2 -> { System.out.println("Line 1: x = " +`x); }
      f(x*) << tt1 && g(y*) << tt2 && (x == y) -> { System.out.println("Line 2: x = " +`x); }
      f(x*) << tt3 && g(x*) << tt4 -> { System.out.println("Line 3: x = " +`x); }
      f(x*) << tt3 && g(y*) << tt4 && (x == y) -> { System.out.println("Line 4: x = " +`x); }
    }
  }
}
