import testuniversaltype.testuniversaltype.types.*;
public class TestUniversalType{
  %gom {
    module TestUniversalType
      abstract syntax
      Term = a()
       | f(s:Term)
  }

  public static void main(String[] args) {
    TestUniversalType test = new TestUniversalType();
    %match {
      a() << B test() -> { System.out.println("test OK!"); }
    }
  }

  public static Term test() {
    Term n = `a();
    %match(n) {
      x@f(a()) -> { return `x; }
    }
    return `n;
  }
}
