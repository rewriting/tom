package builtin;

public class JLstring {

%include {string.tom}

  public final static void main(String[] args) {
    JLstring test = new JLstring();
    
    //test.f("hello");

    test.f2("hello");
  }

  /*
  public void f(String s) {
    Character l = `Char('l');
    Character o = `Char('o');
    %match(String s) {
      ("he",X1*,x@Char('l'),y@Char('o'),_*) -> {
        System.out.println("X1   = " + X1);
        System.out.println("char = " + x);
        System.out.println("char = " + y);
        return s;
      }
      _        -> { return "unknown"; }
    }
  }
  */

  public void f2(String s) {
    %match(String s) {
      (X1*,x@'ll',X2*) -> {
        System.out.println("X1   = " + X1);
        System.out.println("char = " + x);
        System.out.println("X2   = " + X2);
      }
    }
  }

}
