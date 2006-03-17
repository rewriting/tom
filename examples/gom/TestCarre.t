package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;

import java.util.*;
import gom.rond.types.*;
import gom.rond.*;

public class TestCarre extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestCarre.class));
  }

  %include { mutraveler.tom }
  %include { rond/Rond.tom }

  %typeterm Carre {
    implement { gom.Carre }
    equals(t1,t2) { t1.equals(t2) }
    visitor_fwd { CarreBasicStrategy }
  }
  %op Carre Carre(r1:Rond, r2:Rond) {
    is_fsym(t) { (t!=null) && (t instanceof Carre) }
    get_slot(r1, t) { t.r1 }
    get_slot(r2, t) { t.r2 }
    make(t0, t1) { new Carre(t0, t1)}
  }

  public void testPrint() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    VisitableVisitor print = makePrint(list);

    try {
      MuTraveler.init(`BottomUp(print)).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    // This is not really a robust way to test
    assertEquals(list.toString(),"[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]");
  }
  VisitableVisitor makePrint(ArrayList list) {
    return new Print(list);
  }
  %strategy Print(list:ArrayList) extends `Identity() {
    visit Point {
      x -> {
        list.add(`x);
        return `x;
      }
    }
  }

  public void testShowCarre() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    VisitableVisitor show = makeShowCarre(list);

    try {
      MuTraveler.init(`BottomUp(show)).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(list.toString(),"[Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14)))]");
  }
  VisitableVisitor makeShowCarre(ArrayList list) {
    return new ShowCarre(list);
  }
  %strategy ShowCarre(list:ArrayList) extends `Identity() {
    visit Carre {
      arg@Carre(l,r) -> {
        list.add("Carre("+`l+", "+`r+")");
        return `arg;
      }
    }
  }

  public void testCombin() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    VisitableVisitor comb = `ChoiceId(makePrint(list),makeShowCarre(list));

    try {
      MuTraveler.init(`BottomUp(comb)).visit(subject);
      MuTraveler.init(`TopDown(comb)).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(list.toString(),"[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]");
  }

}
