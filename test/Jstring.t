public class Jstring {

  %typestring

  public final static void main(String[] args) {
    Jstring test = new Jstring();
    String res = test.f("hello");

    System.out.println("res = " + res);
  }

  public String f(String s) {
    %match(string s) {
      "coucou" -> { return "coucou"; }
      "hello"  -> { return "hello"; }
      n -> { return "unknown"; }
    }
  }

}

