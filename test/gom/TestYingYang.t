
import ying.types.*;
import yang.types.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestYingYang extends TestCase {

  %include { ying/Ying.tom }

  public void testToString() {
    Moon m = `ping(pong(ping(shi()))); 
    assertTrue("ping(pong(ping(shi())))".equals(m.toString()));
  }
  
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestYingYang.class));
  }
}
