
package bytecode;

public class Subject {
  static protected int i = 42;
  static private String s = "hello";
  private String myAttr;
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
      myAttr = "try";

      String s = "test";
      int k = 0;
      for(k = 0; k < 4; ++k) {
        String s2 = Integer.toString(k);
        s = s + s2;
      }
      System.out.println(s);
    } catch(Exception ex) {
      myAttr = "catch";
    } finally {
      myAttr = "finally";
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

  public void endingStore() {
    int i = 0;
  }

  public void switchfct(int value) {
    switch(value) {
      case 3:
        value += 3;
        break;
      case 4:
        value += 4;
        break;
      case 7:
        value += 7;
      case 8:
        value += 8;
        break;
      default:
        value -= 1;
    }

    System.out.println(value);

    switch(value) {
      case -4:
        value = -4;
        break;
      case 30:
        value = 30;
        break;
    }

    System.out.println(value);
  }
}

