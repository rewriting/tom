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

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestMap.class));
  }
}
