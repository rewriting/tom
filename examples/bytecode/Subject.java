
package bytecode;

public class Subject {
  static protected int i = 42;
  static private String s = "hello";

  private static String gets() {
    String sUp = s.toUpperCase();
    ++i;
    return sUp;
  }

  public void f() {
    for(int i = 0; i < 4; ++i) {
      System.out.println(gets());
    }
  }

  public void u() throws Exception {
    try {
      int i[] = {1, 2, 3};
      i[4] = i[2] + 1;
    } catch (ArrayIndexOutOfBoundsException ex) {
      throw new Exception();
    }
  }

  public class SubSubject {
  }
}

