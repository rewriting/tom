package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import tom.library.sl.Strategy;
import gom.teststratmapping.m.types.*;

public class TestStratMapping {

  %include { sl.tom }
  %gom(--withCongruenceStrategies) {
    module M
    abstract syntax
    S = f(h:S,t:S)
      | a() | b()

    S2 = g(s:S2) | c()
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestStratMapping.class.getName());
  }

  %strategy Strat() extends Identity() {
    visit S {
      f(x,_y) -> { return `x; }
    }
  }

  @Test
  public void testCongruenceConstant() {
    S t = `f(f(a(),b()),f(b(),a()));
    Strategy s1 = `_f(Strat(),Strat());
    Strategy s2 = `When_f(Strat());
    Strategy s3 = `Is_a();
    Strategy s4 = `Is_f();
    Strategy s5 = `Make_f(Make_a(),Make_c());
    Strategy s6 = `Make_a();
    try {
      t = s1.visit(t);
      assertEquals(`f(a(),b()),t);
    } catch (tom.library.sl.VisitFailure ee) {
      fail("s1 failed"); 
    }

    try {
      t = s2.visit(t);
      assertEquals(`a(),t);
    } catch (tom.library.sl.VisitFailure ee) {
      fail("s2 failed"); 
    }

    try {
      t = s3.visit(t);
    } catch (tom.library.sl.VisitFailure ee) {
      fail("s3 failed"); 
    }

    try {
      t = s4.visit(t);
      fail("_f() has not failed on a()"); 
    } catch (tom.library.sl.VisitFailure ee) {
    }

    try {
      t = s5.visit(t);
      fail("Make_f(Make_a(),Make_c()) is not well formed and has not failed"); 
    } catch (tom.library.sl.VisitFailure ee) {
    }

    try {
      t = s6.visit(t);
      assertEquals(`a(),t);
    } catch (tom.library.sl.VisitFailure ee) {
      fail("Make_a() failed"); 
    }
  }

}
