import example.example.types.*;
public class Example{
  %gom {
    module Example
      abstract syntax
      B = b()
  }

  public static void main(String[] args) {
    
    Integer a = new Integer(1);
    Integer a1 = new Integer(1);
    double b = 1;
    String c = new String("c");
    if (a == b) { System.out.println("Test for 'a == b'"); }
    if (b == a) { System.out.println("Test for 'b == a'"); }
    //if (a == c) { System.out.println("Test for 'a == c'"); }
    if (a.equals(a1)) { System.out.println("Test for 'a.equals(a1)'"); }
    if (a1.equals(a)) { System.out.println("Test for 'a1.equals(a)'"); }
    if (a.equals(b)) { System.out.println("Test for 'a.equals(b)'"); }
    //if (b.equals(a)) { System.out.println("Test for 'b.equals(a)'"); }
    if (a.equals(c)) { System.out.println("Test for 'a.equals(c)' where c is boolean"); }
    if (c.equals(a)) { System.out.println("Test for 'c.equals(a)' where c is boolean"); }
    
    B tt = `b();
    %match{
      b() << B tt  && (tt == b()) -> { System.out.println(`tt); }
    }
  }
}
