import org.junit.*;
import org.junit.Assert.*;
import tom.engine.prettyprinter.ppeditor.*;
import org.junit.Test;

public class TestPPTextPosition{

  PPTextPosition pptextposition;

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPPTextPosition.class.getName());
  }

  @Before
  public void createPPTextPosition() {
    this.pptextposition = new PPTextPosition(14,8);
  }
  
  @After
  public void end() {}

  @After
  public void endTest() {
  }

  @Test
  public void testGetLine() {
    Assert.assertEquals("Line is 14", 14, pptextposition.getLine());
  }

  @Test
  public void testGetColumn() {
    Assert.assertEquals("Column is 8", 8, pptextposition.getColumn());
  }

}
