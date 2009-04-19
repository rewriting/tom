package sl;

import static org.junit.Assert.*;
import org.junit.Test;
import tom.library.sl.*;
import sl.testsl.types.*;

public class TestImportStrat {
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestImportStrat.class.getName());
  }
  
  %include { sl.tom }
  %include { testsl/testsl.tom }

  %op Strategy R() {
    make() { new CommonStrat.R() }
  }

  @Test
  public void test1() throws VisitFailure {
    Term t = `a();
    try {
      t = `R().visit(t);
      assertEquals("t==b()",`b(),t);
    } catch (VisitFailure f) {
      fail("It should not fail");
    }
  }
}
