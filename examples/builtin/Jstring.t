public class Jstring {

  %include {string.tom}

  public final static void main(String[] args) {
    Jstring test = new Jstring();
    String res = test.f("hello");

    System.out.println("res = " + res);
  }

  public String f(String s) {
    %match(String s) {
      "coucou" -> { return "coucou"; }
      "hello"  -> { return "hello"; }
      n -> { return "unknown"; }
    }
  }

}

