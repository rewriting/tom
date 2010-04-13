import test.test.types.*;
public class Test {
  %gom {
    module Test
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    B tt = `b();
    %match{
      b() << tt -> { System.out.println(`tt); }
    }
  }
}
