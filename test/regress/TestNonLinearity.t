import testnonlinearity.test.types.*;
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
      inc(x) << A t -> {
        System.out.println("Before the inner match: " + `x);
        A n = `inc2(a());
        %match {
          inc2(x) << n && (x == a()) -> { System.out.println("Match imbriqué: " + `x); } 
        }
      }
    }
  }
}
