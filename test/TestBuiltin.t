import aterm.*;
import aterm.pure.*;

public class TestBuiltin {
  private static ATerm ok,fail;
  private static ATerm pattern1,pattern2,pattern3,pattern4,pattern5;
 
  private ATermFactory factory;
  
  public static void main(String args[]) {
    TestBuiltin test = new TestBuiltin();
    test.setUp();
    test.test1();
    test.test2();
    test.test3();
    test.test4();
    test.test5();
    test.test6();
    test.test7();
    test.test8();
  }

  public void setUp() {
    factory = new PureFactory(16);
    ok   = factory.parse("ok");
    fail = factory.parse("fail");
  }

  %include { int.tom }  
  %include { double.tom }  
  %include { string.tom }  
  %include { char.tom }  

  %typeterm E {
    implement { ATerm }
    get_fun_sym(t)      { (((ATermAppl)t).getAFun()) }
    cmp_fun_sym(t1,t2) { t1 == t2 }
    get_subterm(t, n)   { (((ATermAppl)t).getArgument(n)) }
    equals(t1, t2)      { (t1.equals(t2)) }
  }

  %op E string(stringSlot:String) {
    fsym { factory.makeAFun("string", 1, false) }
    get_slot(stringSlot,t) { getString(t) }
    make(t) { makeString(t)  }
  }

  %op E char(charSlot:char) {
    fsym { factory.makeAFun("char", 1, false) }
    get_slot(charSlot,t) { getChar(t) }
    make(t) { makeChar(t)  }
  }

  %op E int(intSlot:int) {
    fsym { factory.makeAFun("int", 1, false) }
    get_slot(intSlot,t) { getInt(t) }
    make(t) { makeInt(t) }
  }

  %op E double(doubleSlot:double) {
    fsym { factory.makeAFun("double", 1, false) }
    get_slot(doubleSlot,t) { getDouble(t) }
    make(t) { makeDouble(t) }
  }

  public ATerm makeString(String t) {
    return factory.makeAppl(factory.makeAFun("string", 1, false),
                            factory.makeAppl(factory.makeAFun(t, 0, true)));
  }

  public ATerm makeChar(char t) {
    return factory.makeAppl(factory.makeAFun("char", 1, false),
                            factory.makeInt((int)t));
  }

  public ATerm makeInt(int t) {
    return factory.makeAppl(factory.makeAFun("int", 1, false),
                            factory.makeInt(t));
  }

  public ATerm makeDouble(double t) {
    return factory.makeAppl(factory.makeAFun("double", 1, false),
                            factory.makeReal(t));
  }

  public String getString(ATerm t) {
    ATermAppl subterm = (ATermAppl) ((ATermAppl)t).getArgument(0);
    String name = subterm.getName();
    return name;
  }
  
  public int getInt(ATerm t) {
    ATermInt subterm = (ATermInt) ((ATermAppl)t).getArgument(0);
    int value = subterm.getInt();
    return value;
  }

  public char getChar(ATerm t) {
    ATermInt subterm = (ATermInt) ((ATermAppl)t).getArgument(0);
    int value = subterm.getInt();
    return (char)value;
  }

  public double getDouble(ATerm t) {
    ATermReal subterm = (ATermReal) ((ATermAppl)t).getArgument(0);
    double value = subterm.getReal();
    return value;
  }

  public int fib(int t) {
    %match(int t) {
      0 -> { return 1; }
      1 -> { return 1; }
      n -> { return fib(n-1) + fib(n-2); }
    }
  }

  public int fibE(ATerm t) {
    %match(E t) {
      int(0) -> { return 1; }
      int(1) -> { return 1; }
      int(n) -> {
        int n1 = n-1;
        int n2 = n-2;
        return fibE(`int(n1)) + fibE(`int(n2)); }
    }
    return -1;
  }

  public String matchString(String t) {
    %match(String t) {
        "Albert" -> { return "Albert"; }
        "Roger"  -> { return "Roger"; }
        _ -> { return "Unknown"; }
    }
  }

  public String matchStringE(ATerm t) {
    %match(E t) {
        string("Albert") -> { return "Albert"; }
        string("Roger")  -> { return "Roger"; }
        string(_)        -> { return "Unknown"; }
        _ -> { return "Unknown"; }
    }
  }

  public char matchChar(char t) {
    %match(char t) {
      'a' -> { return 'a'; }
      'b' -> { return 'b'; }
      _ -> { return '-'; }
    }
  }

  public char matchCharE(ATerm t) {
    %match(E t) {
      char('a') -> { return 'a'; }
      char('b') -> { return 'b'; }
      _ -> { return '-'; }
    }
  }

  public double matchDouble(double t) {
    %match(double t) {
        1.23 -> { return 1.23; }
        3.14 -> { return 3.14; }
        _ -> { return 0; }
    }
  }

  public double matchDoubleE(ATerm t) {
    %match(E t) {
        double(1.23) -> { return 1.23; }
        double(3.14) -> { return 3.14; }
        _ -> { return 0; }
    }
  }

  public void test1() {
    assertTrue(89 == fib(10));
  }

  public void test2() {
    assertTrue(89 == `fibE(int(10)));
  }
  
  public void test3() {
    assertTrue("Roger".equals(matchString("Roger")));
  }

  public void test4() {
    assertTrue("Roger".equals(matchStringE(`string("Roger"))));
  }

  public void test5() {
    assertTrue(3.14 == matchDouble(3.14));
  }

  public void test6() {
    assertTrue(1.23 == matchDoubleE(`double(1.23)));
  }

  public void test7() {
    assertTrue('a' == matchChar('a'));
  }

  public void test8() {
    assertTrue('b' == matchCharE(`char('b')));
  }
  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

}
