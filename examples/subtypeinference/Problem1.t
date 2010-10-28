import problem1.example.types.*;
public class Problem1{
  %gom {
    module Example
      abstract syntax
      Nat = zero()
          | suc(n:Nat)
          | square(m:Int)

      Int = uminus(n:Nat)
      | plus(n1:Int,n2:Int)

      Float = div(n1:Int,n2:Int)
      }

  public static void main(String[] args) {
    %match{
      x << zero() -> { System.out.println(`x); }
    }
  }
}
