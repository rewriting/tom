package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.importing.types.*;
import gom.imported.types.*;

public class TestFrom extends TestCase {

  %include { importing/Importing.tom }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestFrom.class));
  }

  public void testFromTerm() {
    aterm.ATerm trm =
      aterm.pure.SingletonFactory.getInstance().parse(
          "Loop(Loop(Pack(Atom),Element(Atom)),Element(Element(Atom)))"); 
    Out test = Out.fromTerm(trm);
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }

  public void testFromString() {
    String s = "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))"; 
    Out test = Out.fromString(s);
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }

  public void testFromStream() {
    String s = "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))"; 
    java.io.InputStream stream = null;
    Out test = null;
    try {
      stream = new java.io.DataInputStream(new java.io.StringBufferInputStream(s));
      test = Out.fromStream(stream);
    } catch (java.io.IOException e) {
      fail("Streaming problem");
    }
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }
}
