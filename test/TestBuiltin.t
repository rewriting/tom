import java.util.*;
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
  }

  public void setUp() {
    factory = new PureFactory(16);
    ok   = factory.parse("ok");
    fail = factory.parse("fail");
  }

  %typeint
  %typestring
  
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

  %op E int(intSlot:int) {
    fsym { factory.makeAFun("int", 1, false) }
    get_slot(intSlot,t) { getInt(t) }
    make(t) { makeInt(t) }
  }

  public ATerm makeString(String t) {
    return factory.makeAppl(factory.makeAFun("string", 1, false),
                            factory.makeAppl(factory.makeAFun(t, 0, true)));
  }

  public ATerm makeInt(int t) {
    return factory.makeAppl(factory.makeAFun("int", 1, false),
                            factory.makeInt(t));
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
        string("Roger") -> { return "Roger"; }
        _ -> { return "Unknown"; }
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

  static void  assertTrue(boolean condition) {
    if(!condition) {
      throw new RuntimeException("assertion failed.");
    }
  }

}
