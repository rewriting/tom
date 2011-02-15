import org.junit.Test;
import org.junit.*;
import org.junit.Assert.*;

public class TestPPOutputFormatter {

  private PPCursor fileCursor;
  private FileWriter fileWriter;
  private PPOutputFormatter ppoutputformatter;

  public static void main(String args[]) {
    org.junit.runner.JUnitCore.main(TestPPOutputFormatter.class.getName());
  }

  @Before
  public void initializeAttributes() {
    this.fileWriter=new FileWriter("./test.txt");
    this.fileCursor=new PPCursor(14,8);
    this.ppoutputformatter=new PPOutputFormatter(fileCursor, fileWriter);
  }

  @After
  public void endTest() {
    (new File("./test.txt")).delete();
  }

  @Test
  public void testGiveOnePPTextToPPCursor() {
    PPTextPosition start=new PPTextPosition(14,8);
    ppoutputformatter.giveOnePPTextToPPCursor("String",start); 
    Assert.assertEquals("String", fileCursor.readFileBuffer(new PPTextPosition(20,8)));
	}

	@Test
	public void testput(){
    PPTextPosition start=new PPTextPosition(14,8);
    ppoutputformatter.put("String",start); 
    Assert.assertEquals("String", fileCursor.readFileBuffer(new PPTextPosition(20,8)));
	}


}
