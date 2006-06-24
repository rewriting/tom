package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.*;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.MuStrategy;
import tom.library.strategy.mutraveler.Identity;

import gom.elist.types.*;

public class TestMap extends TestCase {

  private static int cnt;
  %include { elist/Elist.tom }
  %include { elist/_Elist.tom }
  %include { java/util/types/Collection.tom }

  public void testMap() {
    Elist subject = `Cons(a(),Cons(b(),Cons(b(),Cons(c(),Empty()))));
    Collection abag = new HashSet();
    Collection bbag = new HashSet();
    Collection cbag = new HashSet();
    MuStrategy maps = `mu(MuVar("x"),Choice(_Cons(Log(abag,bbag,cbag),MuVar("x")),_Empty()));
    maps.apply(subject);
    assertEquals(1,abag.size());
    assertEquals(2,bbag.size());
    assertEquals(1,cbag.size());
  }

  %strategy Log(abag:Collection,bbag:Collection,cbag:Collection) extends `Identity() {
    visit E {
      a@a() -> { abag.add(new Integer(++cnt)); }
      b@b() -> { bbag.add(new Integer(++cnt)); }
      c@c() -> { cbag.add(new Integer(++cnt)); }
    }
  }

  public void testBuiltin() {
    Elist subject = 
      `Cons(a(),
        Cons(f(f(c(),1,f(b(),5,a())),4,f(a(),2,b())),
          Cons(b(),
            Cons(c(),
              Empty()))));
    Collection abag = new HashSet();
    Collection bbag = new HashSet();
    Collection cbag = new HashSet();
    MuStrategy maps = `mu(MuVar("x"),
        Choice(
          _Cons(BottomUp(Log(abag,bbag,cbag)),MuVar("x")),
          _Empty()
        ));
    maps.apply(subject);
    assertEquals(3,abag.size());
    assertEquals(3,bbag.size());
    assertEquals(2,cbag.size());
  }

  public void testCut() {
    E subject = `f(f(a(),2,b()),4,f(f(c(),2,a()),1,g(b(),5,f(a(),3,f(b(),6,a())))));
    Collection abag = new HashSet();
    Collection bbag = new HashSet();
    Collection cbag = new HashSet();
    /* cutbu is a custom bottomup, that do not go in the left part of an f */
    MuStrategy cutbu = `mu(MuVar("x"),
        Sequence(
          Choice(_f(Identity(),Fail(),MuVar("x")),All(MuVar("x"))),
          Log(abag,bbag,cbag)
          ));
    cutbu.apply(subject);
    assertEquals("count a",1,abag.size());
    assertEquals("count b",1,bbag.size());
    assertEquals("count c",0,cbag.size());
  }

  public void testMatch() {
    E subject = `f(f(a(),1,b()),2,f(c(),3,d()));
    MuStrategy match = `_f(_f(_a(),Fail(),_b()),Fail(),_f(_c(),Fail(),_d()));
    boolean state = false;
    try {
      match.visit(subject);
      state = true;
    } catch (jjtraveler.VisitFailure e) {
      fail("the match should not fail");
    }
    assertTrue("The strategie pattern should match",state);
  }

  public void testMatchFailure() {
    E subject = `f(f(a(),1,b()),2,f(c(),3,d()));
    MuStrategy match = `_f(_f(_a(),Fail(),_b()),Fail(),_f(_c(),Fail(),_c()));
    boolean state = true;
    try {
      match.visit(subject);
      fail("the match should fail");
    } catch (jjtraveler.VisitFailure e) {
      state = false;
    }
    assertFalse("The strategie pattern not have matched",state);
  }

  %strategy replace_with_a() extends `Identity() {
    visit E {
      _ -> { return `a(); }
    }
  }

  public void testReplace() {
    Elist subject = 
      `Cons(a(),
        Cons(f(f(c(),1,f(b(),5,a())),4,f(a(),2,b())),
          Cons(b(),
            Cons(c(),
              Empty()))));
    MuStrategy maps = `mu(MuVar("x"),
        Choice(
          _Cons(
            Try(
              _f(
                _f(replace_with_a(),Identity(),Identity()),
                Identity(),
                _f(Identity(),Identity(),replace_with_a())
              )),MuVar("x")),
          _Empty()
        ));
    subject = (Elist) maps.apply(subject);
    Collection abag = new HashSet();
    Collection bbag = new HashSet();
    Collection cbag = new HashSet();
    `BottomUp(Log(abag,bbag,cbag)).apply(subject);
    assertEquals(5,abag.size());
    assertEquals(2,bbag.size());
    assertEquals(1,cbag.size());
  }

  public void testRewrite() {
    Elist subject = 
      `Cons(a(),
        Cons(f(f(c(),1,f(b(),5,a())),4,f(a(),2,b())),
          Cons(b(),
            Cons(c(),
              Empty()))));
    /* encode the rule f(_,_b()) -> a() */
    MuStrategy rule = `BottomUp(
        Try(
          Sequence(
            _f(Identity(),Identity(),_b()),
            replace_with_a())));
    subject = (Elist) rule.apply(subject);
    assertEquals(
        `Cons(a(),
          Cons(f(f(c(),1,f(b(),5,a())),4,a()),
            Cons(b(),
              Cons(c(),
                Empty())))),
        subject);
  }

  public void testInnermost() {
    E subject =
      `f(
        f(f(a(),0,a()),0,f(a(),1,a())),
        0,
        f(
          f(
            f(a(),1,a()),
            0,
            f(a(),1,f(f(a(),1,a()),0,f(a(),1,a())))
           ),
          0,
          f(f(a(),0,a()),0,f(a(),1,a()))
          )
        );
    MuStrategy rule =
      `Sequence(
          _f(_a(),Identity(),_a()),
            replace_with_a()
            );
    subject = (E) `Innermost(rule).apply(subject);
    assertEquals(`a(),subject);
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestMap.class));
  }
}
