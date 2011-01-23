import org.junit.*;
import org.juni.Assert.*;
import tom.engine.prettyprinter.ppeditor;

public class TestPPTextPosition{

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestPPTextPosition.class.getName());
  }

  PPTextPosition pptextposition;
  @Before
  public void createPPTextPosition(){
    this.pptextposition = new PPTextPosition(14,8);
  }

  @Test
  public void testGetLine(){
    Assert.assertEquals("Line is 14",14,pptextposition.getLine());
  }

  @Test
  public void testGetColumn(){
    Assert.assertEquels("Column is 8",8,pptextposition.getColumn());
  }
  
}
