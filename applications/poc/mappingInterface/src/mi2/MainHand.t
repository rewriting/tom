package mi2;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.List;

import base.hand.types.*;
import base.hand.types.t1.*;
import base.hand.types.t2.*;
import tom.library.sl.*;

public class MainHand extends TestCase {
  %include { mi2/hand/mapping.tom }
  %include { java/util/ArrayList.tom }
  %include { sl.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(MainHand.class));
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
      T1 res = (T1) `Repeat(OnceBottomUp(Rule())).visitLight(subject, mi2.mapping.Introspector.instance);
      assertEquals(res, `a());
    } catch(VisitFailure e) {
      fail();
    }

  }

  public void test_listMatchFirst() {
    /*t1List*/List<T1> subject = `concT1(a(), f(a(), b()), f(a(), g(b())));

    %match(subject) {
      concT1(first, _*) -> {

        assertEquals(`first, `a());
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

  public void test_listMatchAny() {
    T2 subject = `h(concT1(a(), f(a(), b()), f(a(), g(b()))));

    %match(subject) {
      !h(concT1(_*, a(), _*)) -> {
        fail();
        return;
      }
    }
    System.out.println("h takes a() as argument.");
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
