package sl;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Collection;
import java.util.HashSet;
import sl.testgetsubterm.test.types.*;
import tom.library.sl.*;
import tom.library.sl.Position;

public class TestGetSubterm extends TestCase {
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestGetSubterm.class));
  }
  
  %include { sl.tom }
  %gom {
    module test
    imports
    abstract syntax
    Term = a()
         | b()
         | c()
         | f(t1:Term,t2:Term)
  }

  public void test1() throws VisitFailure{
    Collection<Position> bag = new HashSet<Position>();
    Term t = `f(a(),a());
    try {
      t = (Term)`TopDown(Bug(bag)).visit(t);
      assertEquals("There are two a()",2,bag.size());
    } catch (VisitFailure f) {
      fail("It should not fail");
    }
    for(Position p : bag) {
      Strategy sts = p.getSubterm();
      Term st = (Term) sts.visit(t);
      assertEquals(`a(),st);
    }
    for(Position p : bag) {
      Strategy sts = p.getSubterm();
      try {
        Term st = (Term) sts.visitLight(t);
        assertEquals(`a(),st);
      } catch (tom.library.sl.VisitFailure f) {
        fail("getsubterm should not fail there");
      }
    }
  }

  %typeterm PositionCollection {
    implement { Collection<Position> }
    is_sort(t) { t instanceof Collection<?> }
  }
  %strategy Bug(bag:PositionCollection) extends Identity() {
    visit Term {
      a() -> { bag.add(getEnvironment().getPosition()); }
    }
  }
}
