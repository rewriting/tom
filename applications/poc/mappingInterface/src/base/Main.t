package base;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import base.data.types.*;
import tom.library.sl.*;

public class Main extends TestCase {
  %include { base/data/data.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(Main.class));
  }

  public void testMatch() {
    T1 subject = `f(f(a(),b()),g(b()));
    %match(subject) {
      f(x,g(y)) -> { 
        assertEquals(`x,`f(a(),b()));
        assertEquals(`y,`b());
        return;
      }
    }
    fail();
  }

  public void testVisit() {
    T1 subject = `f(f(a(),b()),g(b()));
    try {
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject);
      assertEquals(res, `a());
    } catch(VisitFailure e) {
      fail();
    }
  }

  public void test_listMatchFirst() {
   ListT1 subject = `concT1(a(), f(a(), b()), f(a(), g(b())));

    %match(subject) {
      concT1(first, middle*, last) -> {

        assertEquals(`first, `a());
        assertEquals(`last, `f(a(),g(b())));
        assertEquals(`middle*, `concT1(f(a(),b())));
        return;
      }
    }
    fail();
  }

  public void test_listMatchLast() {
    Object subject = `concT1(a(), f(a(), b()), f(a(), g(b())));

    %match(subject) {
      concT1(_*, last) -> {

        assertEquals(`last, `f(a(), g(b())));
        return;
      }
    }
    fail();
  }

  public void test_listMatchCount() {
    T2 subject = `h(concT1(a(), f(a(), b()), f(a(), g(b()))));

    int i = 0;
    %match(subject) {
      h(concT1(_*, x, _*)) -> {
        i++;
      }
    }
    assertEquals(i,3);
  }

  public void test_listMatchNonLinear() {
    T2 subject = `h(concT1(a(), f(a(), b()), f(a(),b()), a(), f(a(), g(b()))));

    %match(subject) {
      h(concT1(X1*, x, X2*,x,X3*)) -> {
        assertEquals(`x,`a());
        assertEquals(`X1,`concT1());
        assertEquals(`X2,`concT1(f(a(),b()),f(a(),b())));
        assertEquals(`X3,`concT1(f(a(),g(b()))));
        return;
      }
      _ -> {
        fail();
      }
    }
  }

  public void test_listMatchAny() {
    T2 subject = `h(concT1(a(), f(a(), b()), f(a(), g(b()))));

    %match(subject) {
      !h(concT1(_*, a(), _*)) -> {
        fail();
        return;
      }
    }
  }

  public void test_congWithListMatch() {
    T2 subject = `h(concT1(a(), f(a(), b()), f(a(), g(b()))));

    // todo: how to do this?
    //`_h(_concT1(map(Print()))).visitLight(subject);
  }

  %strategy Rule() extends Fail() {
    visit T1 {
      f(x,y) -> {
        return `x;
      }
    }
  }

}
