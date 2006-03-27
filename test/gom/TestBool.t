package gom;

import java.io.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.bool.types.*;

public class TestBool extends TestCase {

  %include { bool/Bool.tom }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestBool.class));
  }

  public void testNotHook() {
    Bool test = `Not(And(True(),True()));
    assertEquals(test,`Or(False(),False()));
  }
}
