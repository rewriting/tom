import testundeclaredsymbol.test.types.*;
public class TestUndeclaredSymbol {
  %gom {
    module Test
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << A tt -> { System.out.println(`tt); }
    }
  }
}
