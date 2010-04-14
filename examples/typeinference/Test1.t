import test1.test1.types.*;
public class Test1 {
  %gom {
    module Test1
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
