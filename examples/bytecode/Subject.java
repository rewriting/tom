
package bytecode;

public class Subject {
  static protected int i = 42;
  static private String s = "hello";
/*
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
  }*/

  public void fct() throws Exception {
    while(true) {
      System.out.println(i);
      if(i > 5) {
        throw new Exception();
      }
    }
  }

  public void t() throws Exception {
    try {
      fct();
    } catch(Exception ex) {
      fct();
    } finally {
      fct();
    }
  }

  public void stratKiller() {
    int i = 0;
    int j = 0;
    while(true) {
      i = 1;
      i = 2;
      j = i;
    }
  }
}

