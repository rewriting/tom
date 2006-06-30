package gom;

import gom.ying.types.*;
import gom.yang.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestYingYang extends TestCase {

  %include { ying/Ying.tom }

  public void testToString() {
    Moon m = `ping(pong(ping(shi()))); 
    assertEquals("ping(pong(ping(shi())))",m.toString());
  }
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestYingYang.class));
  }
}
