import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestLabel extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestLabel.class));
  }
  
  %include { String.tom }

  public void test1() {
    int a = 0;
    int b = 0;
    int c = 0;
    String s = "abcba";
    %match(s) {
l1: concString(_*,'a',_*) -> { a++; break l2; }
l2: concString(_*,'b',_*) -> { b++; break l4; }
l3: concString(_*,'c',_*) -> { c++; }
l4: concString(_*,'b',_*) -> { b++; }
l5: concString(_*,'a',_*) -> { a++; }
    }
    assertEquals(a,3);
    assertEquals(b,3);
    assertEquals(c,0);
  }

}
