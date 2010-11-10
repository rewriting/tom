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
  }

  public static void main(String[] args) {
    A t = `inc(b());
    %match {
      inc(x) << A t 
        && (inc2(x) << inc2(a()) && (x == a())) -> {
          System.out.println("Match imbrique: " + `x);
        } 
    }
  }
}
