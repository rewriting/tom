import testuniversaltype.testuniversaltype.types.*;
public class TestSublist{
  %gom {
    module TestUniversalType
      abstract syntax
      Term = a()
       | f(s:Term)
  }

  public static void main(String[] args) {
    TestUniversalType test = new TestUniversalType();
  }

  public void test() {
    %match(f(a()),ff(a())) {
      f(a()),f(_x) -> { return; }
    }
    fail("a,f(a) hould match");
  }
}
