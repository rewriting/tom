import simpletest.example.types.*;
public class SimpleTest {
  %gom {
    module Example
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << B tt -> { System.out.println(`tt); }
    }
  }
}
