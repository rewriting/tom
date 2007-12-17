package gom;

import java.io.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testtomhook.bool.types.*;

public class TestTomHook extends TestCase {

  %gom {
    module Bool
    abstract syntax
    Bool = True()
         | False()
         | Not(b:Bool)
         | And(l:Bool,r:Bool)
         | Or(l:Bool,r:Bool)
    Not:make(b) {
      %match(Bool b) {
        And(l,r) -> { return `Or(Not(l),Not(r)); }
        Or(l,r)  -> { return `And(Not(l),Not(r)); }
        True()   -> { return `False(); }
        False()  -> { return `True(); }
      }
    }
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestTomHook.class));
  }

  public void testTrue() {
    Bool test = `Not(True());
    assertEquals(test,`False());
  }

  public void testFalse() {
    Bool test = `Not(False());
    assertEquals(test,`True());
  }

  public void testNotAndHook() {
    Bool test = `Not(And(True(),True()));
    assertEquals(test,`Or(False(),False()));
  }

  public void testNotOrHook() {
    Bool test = `Not(Or(True(),True()));
    assertEquals(test,`And(False(),False()));
  }
}
