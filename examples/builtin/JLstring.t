public class JLstring {

%include {charlist.tom}

  public final static void main(String[] args) {
    JLstring test = new JLstring();
    String res = test.f("hello");

    System.out.println("res = " + res);
  }

  public String f(String s) {
    Character l = `Char('l');
    Character o = `Char('o');
    %match(String s) {
      (X1*,x@Char('l'),y@Char('o'),_*) -> {
        System.out.println("X1   = " + X1);
        System.out.println("char = " + x);
        System.out.println("char = " + y);
        return s;
      }
      _        -> { return "unknown"; }
    }
  }

}
