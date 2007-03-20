package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import tom.library.sl.Strategy;
import gom.teststratmapping.m.types.*;

public class TestStratMapping extends TestCase {

  %include { sl.tom }
  %gom(--strategies-mapping) {
    module M
    abstract syntax
    S1 = f(h:S1,t:S2)
       | g(s:S1)
       | a() | b()
    S2 = f2(h2:S2,t1:S1)
       | h(s2:S2)
       | k()
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStratMapping.class));
  }

  public void testCongruenceConstant() {
    S1 t = `a();
    Strategy s = `_a();
    try {
      t = (S1) s.fire(t);
      assertEquals(`a(),t);
    } catch (tom.library.sl.FireException ee) {
      fail("_a() failed on a()"); 
    }
  }
}
