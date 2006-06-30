package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.Visitable;
import jjtraveler.VisitFailure;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;

import gom.vlist.types.*;

public class TestStrat extends TestCase {
  private static int max = 4;

  %include { mutraveler.tom }

  %include { vlist/VList.tom }

  public VList genere(int n) {
    if(n>2) {
      VList l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public void testToString() {
    VList subject = genere(max);
    assertEquals("Consconc(4,Consconc(3,Consconc(2,Emptyconc())))",subject.toString());
  }

  public void testOncebottomUp() {
    VList subject = genere(max);
    VisitableVisitor rule = new RewriteSystem();
    VList result = null;
    try {
      result = (VList) MuTraveler.init(`OnceBottomUp(rule)).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(result.toString(),"Consconc(4,Consconc(3,Consconc(3,Emptyconc)))");
  }

  public void testBottomUp() {
    VList subject = genere(max);
    VisitableVisitor rule = new RewriteSystem();
    VList result = null;
    try {
      result = (VList) MuTraveler.init(`BottomUp(Try(rule))).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals("Consconc(5,Consconc(4,Consconc(3,Emptyconc())))",result.toString());
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStrat.class));
  }

  class RewriteSystem extends gom.vlist.VListBasicStrategy {
    public RewriteSystem() {
      super(`Fail());
    }

    public VList visit_VList(VList arg) throws VisitFailure {
      %match(VList arg) {
        conc(h,t*) -> {
          int v = `h+1;
          return `conc(v,t*);
        }
      }
      return (VList)`Fail().visit(arg);
    }
  }
}
