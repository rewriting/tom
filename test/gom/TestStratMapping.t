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
    S = f(h:S,t:S)
       | a() | b()
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStratMapping.class));
  }

  %strategy Strat() extends Identity() {
    visit S {
      f(x,y)            -> { return `x; }
    }
  }


  public void testCongruenceConstant() {
    S t = `f(f(a(),b()),f(b(),a()));
    Strategy s1 = `_f(Strat(),Strat());
    Strategy s2 = `When_f(Strat());
    Strategy s3 = `Is_a();
    Strategy s4 = `Is_f();
    try {
      t = (S) s1.fire(t);
      assertEquals(`f(a(),b()),t);
         } catch (tom.library.sl.FireException ee) {
      fail("s1 failed"); 
    }

    try {
      t = (S) s2.fire(t);
      assertEquals(`a(),t);
     } catch (tom.library.sl.FireException ee) {
      fail("s2 failed"); 
    }
    try {
      t = (S) s3.fire(t);
     } catch (tom.library.sl.FireException ee) {
      fail("s3 failed"); 
    }
    try {
      t = (S) s4.fire(t);
      fail("_f() has not failed on a()"); 
     } catch (tom.library.sl.FireException ee) {
    }
  }
}
