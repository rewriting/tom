import static org.junit.Assert.*;
import org.junit.Test;
import tom.library.sl.*;
import testlabelns.mod.*;
import testlabelns.mod.types.*;

public class TestLabelNS {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestLabelNS.class.getName());
  }
  
  %include { string.tom }
  %include { sl.tom }

  %gom {
    module Mod
      abstract syntax
      T = a() 
  }

  @Test
  public void test1() {
    int a = 0;
    int b = 0;
    int c = 0;
    String s = "abccba";
    %match(s) {
l1: concString(_*,'a',_*) -> { a++; break l1; }
l2: concString(_*,'b',_*) -> { b++; break l2; }
l3: concString(_*,'c',_*) -> { c++; }
    concString(_*,'c',_*) -> { c++; }
    concString(_*,'b',_*) -> { b++; }
    concString(_*,'a',_*) -> { a++; }
    }
    assertEquals(a,3);
    assertEquals(b,3);
    assertEquals(c,4);
  }

  %strategy Count() extends Identity() {
    visit T {
  l1: a() -> { break l1; }
      a() -> {  }
    }
  }
}
