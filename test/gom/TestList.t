package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testlist.list.types.*;
import gom.testlist.list.types.list.conc;

public class TestList extends TestCase {

  %gom {
    module list
    abstract syntax
    Element = a()
            | b()
    List = conc(Element*)
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestList.class));
  }

  public void testZeroLength() {
    List l = `conc();
    assertEquals(getListLength(l),((conc)l).length());
  }

  public void testOneLength() {
    List l = `conc(a());
    assertEquals(getListLength(l),((conc)l).length());
  }

  public void testTwoLength() {
    List l = `conc(a(),b());
    assertEquals(getListLength(l),((conc)l).length());
  }
  public void testnLength() {
    int n = 15;
    List l = `conc();
    for(int i = 0; i<n; i++) {
      l = `conc(a(),b(),l*);
    }
    assertEquals(getListLength(l),((conc)l).length());
  }

  public static int getListLength(List l) {
    %match(List l) {
      conc() -> { return 0; }
      conc(_,t*) -> { return 1+getListLength(`t*); }
    }
    return 0;
  }
}
