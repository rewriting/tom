import java.io.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import importing.types.*;
import imported.types.*;

public class TestImport extends TestCase {

  %include { importing/Importing.tom }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestImport.class));
  }

  public void testMake() {
    Out test = `Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())));
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom),Element(Atom)),Element(Element(Atom)))");
  }
}
