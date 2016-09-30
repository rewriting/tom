import aterm.*;
import aterm.pure.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestBuiltin {
  private static ATermFactory factory = SingletonFactory.getInstance();

	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestBuiltin.class.getName());
	}
  
  %include { int.tom }  
  %include { long.tom }  
  %include { double.tom }  
  %include { string.tom }  

  %typeterm E {
    implement { ATerm }
    is_sort(t) { $t instanceof ATerm }
    equals(t1, t2)      { ($t1.equals($t2)) }
  }

  %op E string(stringSlot:String) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "string" }
    get_slot(stringSlot,t) { getString($t) }
    make(t) { makeString($t)  }
  }

  %op E char(charSlot:char) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "char" }
    get_slot(charSlot,t) { getChar($t) }
    make(t) { makeChar($t)  }
  }

  %op E int(intSlot:int) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "int" }
    get_slot(intSlot,t) { getInt($t) }
    make(t) { makeInt($t) }
  }

  %op E long(longSlot:long) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "long" }
    get_slot(longSlot,t) { getLong($t) }
    make(t) { makeLong($t) }
  }

  %op E double(doubleSlot:double) {
    is_fsym(t) { ((ATermAppl)$t).getName() == "double" }
    get_slot(doubleSlot,t) { getDouble($t) }
    make(t) { makeDouble($t) }
  }

  public static ATerm makeString(String t) {
    return factory.makeAppl(factory.makeAFun("string", 1, false),
                            factory.makeAppl(factory.makeAFun(t, 0, true)));
  }

  public static ATerm makeChar(char t) {
    return factory.makeAppl(factory.makeAFun("char", 1, false),
                            factory.makeInt((int)t));
  }

  public static ATerm makeInt(int t) {
    return factory.makeAppl(factory.makeAFun("int", 1, false),
                            factory.makeInt(t));
  }

  public static ATerm makeLong(long t) {
    return factory.makeAppl(factory.makeAFun("long", 1, false),
                            factory.makeInt((int)t));
  }

  public static ATerm makeDouble(double t) {
    return factory.makeAppl(factory.makeAFun("double", 1, false),
                            factory.makeReal(t));
  }

  public static String getString(ATerm t) {
    ATermAppl subterm = (ATermAppl) ((ATermAppl)t).getArgument(0);
    String name = subterm.getName();
    return name;
  }
  
  public static int getInt(ATerm t) {
    ATermInt subterm = (ATermInt) ((ATermAppl)t).getArgument(0);
    int value = subterm.getInt();
    return value;
  }

  public static long getLong(ATerm t) {
    ATermInt subterm = (ATermInt) ((ATermAppl)t).getArgument(0);
    long value = (long)subterm.getInt();
    return value;
  }

  public static char getChar(ATerm t) {
    ATermInt subterm = (ATermInt) ((ATermAppl)t).getArgument(0);
    int value = subterm.getInt();
    return (char)value;
  }

  public static double getDouble(ATerm t) {
    ATermReal subterm = (ATermReal) ((ATermAppl)t).getArgument(0);
    double value = subterm.getReal();
    return value;
  }

  public int fib(int t) {
    %match(int t) {
      0 -> { return 1; }
      1 -> { return 1; }
      n -> { return fib(`n - 1) + fib(`n - 2); }
    }
		return -1;
  }

  public int fibE(ATerm t) {
    %match(E t) {
      int(0) -> { return 1; }
      int(1) -> { return 1; }
      int(n) -> {
        int n1 = `n - 1;
        int n2 = `n - 2;
        return fibE(`int(n1)) + fibE(`int(n2)); }
    }
    return -1;
  }

  public long fibLong(long t) {
    %match(long t) {
      0l -> { return 1; }
      1L -> { return 1; }
      n -> { return fibLong(`n - 1) + fibLong(`n - 2); }
    }
		return -1;
  }

  public long fibLongE(ATerm t) {
    %match(E t) {
      long(0l) -> { return 1; }
      long(1L) -> { return 1; }
      long(n) -> {
        long n1 = `n - 1;
        long n2 = `n - 2;
        return fibLongE(`long(n1)) + fibLongE(`long(n2)); }
    }
    return -1;
  }

  public String matchString(String t) {
    %match(String t) {
        "Albert" -> { return "Albert"; }
        "Roger"  -> { return "Roger"; }
    }
		return "Unknown"; 
  }

  public String matchStringE(ATerm t) {
    %match(E t) {
        string("Albert") -> { return "Albert"; }
        string("Roger")  -> { return "Roger"; }
        string(_)        -> { return "Unknown"; }
    }
		return "Unknown"; 
  }

  public char matchChar(char t) {
    %match(char t) {
      'a' -> { return 'a'; }
      'b' -> { return 'b'; }
    }
		return '-'; 
  }

  public char matchCharE(ATerm t) {
    %match(E t) {
      char('a') -> { return 'a'; }
      char('b') -> { return 'b'; }
    }
		return '-'; 
  }

  public int matchInt(int t) {
    %match(int t) {
      -1 -> { return t; }
      -2 -> { return -2; }
    }
		return 0; 
  }
  public double matchDouble(double t) {
    %match(double t) {
        1.23 -> { return 1.23; }
        3.14 -> { return 3.14; }
    }
		return 0; 
  }

  public double matchDoubleE(ATerm t) {
    %match(E t) {
        double(1.23) -> { return 1.23; }
        double(3.14) -> { return 3.14; }
    }
		return 0; 
  }

  @Test
  public void test1() {
    assertTrue(89 == fib(10));
  }

  @Test
  public void test2() {
    assertTrue(89 == `fibE(int(10)));
  }
  
  @Test
  public void test3() {
    assertTrue(89 == fibLong(10));
  }

  @Test
  public void test4() {
    assertTrue(89 == `fibLongE(long(10)));
  }

  @Test
  public void testMatchString1() {
    assertTrue("Albert".equals(matchString("Albert")));
  }

  @Test
  public void testMatchString2() {
    assertTrue("Roger".equals(matchString("Roger")));
  }
 
  @Test
  public void testMatchString3() {
    String s = "abcaabbccabc";
    int a = 0;
    int b = 0;
    int c = 0;
    int a_b = 0;
    int a_c = 0;
    int b_c = 0;
    int ab = 0;
    int bc = 0;
    int abc = 0;
    %match(s) {
      concString(_*,'a',_*) -> { a++; }
      concString(_*,'b',_*) -> { b++; }
      concString(_*,'c',_*) -> { c++; }
      concString(_*,'a',_*,'b',_*) -> { a_b++; }
      concString(_*,'a',_*,'c',_*) -> { a_c++; }
      concString(_*,'b',_*,'c',_*) -> { b_c++; }
      concString(_*,'ab',_*) -> { ab++; }
      concString(_*,'bc',_*) -> { bc++; }
      concString(_*,'abc',_*) -> { abc++; }
    }
    assertEquals(a,4);
    assertEquals(b,4);
    assertEquals(c,4);
    assertEquals(a_b,11);
    assertEquals(a_c,11);
    assertEquals(b_c,11);
    assertEquals(ab,3);
    assertEquals(bc,3);
    assertEquals(abc,2);
  }

  @Test
  public void testMatchString4() {
    String s = "a'b\"c\\d'''e";
    int a = 0;
    int b = 0;
    int c = 0;
    int aa = 0;
    %match(s) {
      concString(_*,'\'',_*) -> { a++; }
      concString(_*,'"',_*) -> { b++; }
      concString(_*,'\\',_*) -> { c++; }
      concString(_*,'\'\'',_*) -> { aa++; }
    }
    assertEquals(a,4);
    assertEquals(b,1);
    assertEquals(c,1);
    assertEquals(aa,2);
  }

  @Test
  public void testMatchStringDefault() {
    assertTrue("Unknown".equals(matchString("Marcel")));
  }

  @Test
  public void testMatchStringE1() {
    assertTrue("Albert".equals(matchStringE(`string("Albert"))));
  }

  @Test
  public void testMatchStringE2() {
    assertTrue("Roger".equals(matchStringE(`string("Roger"))));
  }

  @Test
  public void testMatchStringEdefault() {
    assertTrue("Unknown".equals(matchStringE(`string("Marcel"))));
  }

  @Test
  public void testDouble1() {
    assertTrue(1.23 == matchDouble(1.23));
  }

  @Test
  public void testDouble2() {
    assertTrue(3.14 == matchDouble(3.14));
  }

  @Test
  public void testDoubleDefault() {
    assertTrue(0 == matchDouble(2.71));
  }

  @Test
  public void testDoubleE1() {
    assertTrue(1.23 == matchDoubleE(`double(1.23)));
  }

  @Test
  public void testDoubleE2() {
    assertTrue(3.14 == matchDoubleE(`double(3.14)));
  }

  @Test
  public void testDoubleEdefault() {
    assertTrue(0 == matchDoubleE(`double(2.71)));
  }

  @Test
  public void testChar1() {
    assertTrue('a' == matchChar('a'));
  }

  @Test
  public void testChar2() {
    assertTrue('b' == matchChar('b'));
  }

  @Test
  public void testCharDefault() {
    assertTrue('-' == matchChar('z'));
  }

  @Test
  public void testCharE1() {
    assertTrue('a' == matchCharE(`char('a')));
  }

  @Test
  public void testCharE2() {
    assertTrue('b' == matchCharE(`char('b')));
  }

  @Test
  public void testCharEdefault() {
    assertTrue('-' == matchCharE(`char('k')));
  }

  @Test
  public void testInt() {
    assertTrue(-1 == matchInt(`(-1)));
    assertTrue(-2 == matchInt(`(-2)));
    assertTrue(0 == matchInt(`(-3)));
  }
}
