package gom;

import gom.ying.types.*;
import gom.yang.types.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class TestYingYang {

  %include { ying/Ying.tom }

  @Test
  public void testToString() {
    Moon m = `ping(pong(ping(shi()))); 
    assertEquals("ping(pong(ping(shi())))",m.toString());
  }
  
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestYingYang.class);
  }
}
