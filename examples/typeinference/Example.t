import example.example.types.*;
public class Example{
  %gom {
    module Example
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    /*
    int a = 1;
    double b = 1.1;
    boolean c = true;
    if (a == b) { System.out.println("Test for 'a == b'"); }
    if (b == a) { System.out.println("Test for 'b == a'"); }
    if (a.equals(b)) { System.out.println("Test for 'a.equals(b)'"); }
    if (b.equals(a)) { System.out.println("Test for 'b.equals(a)'"); }
    if (a.equals(c)) { System.out.println("Test for 'a.equals(c)' where c is boolean"); }
    if (c.equals(a)) { System.out.println("Test for 'c.equals(a)' where c is boolean"); }
    */
    B tt = `b();
    %match{
      b() << B tt  && (tt == b()) -> { System.out.println(`tt); }
    }
  }
}
