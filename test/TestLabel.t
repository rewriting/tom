import static org.junit.Assert.*;
import org.junit.Test;

public class TestLabel {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestLabel.class);
  }
  
  %include { string.tom }

  @Test
  public void test1() {
    int a = 0;
    int b = 0;
    int c = 0;
    String s = "abcba";
    %match(s) {
l1: concString(_*,'a',_*) -> { a++; break l1; }
l2: concString(_*,'b',_*) -> { b++; break l2; }
l3: concString(_*,'c',_*) -> { c++; }
l4: concString(_*,'b',_*) -> { b++; }
l5: concString(_*,'a',_*) -> { a++; }
    }
    assertEquals(a,3);
    assertEquals(b,3);
    assertEquals(c,1);
  }

}
