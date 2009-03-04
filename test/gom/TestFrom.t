package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.importing.types.*;
import gom.imported.types.*;

public class TestFrom {

  %include { importing/Importing.tom }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestFrom.class );
  }

  @Test
  public void testFromTerm() {
    aterm.ATerm trm =
      aterm.pure.SingletonFactory.getInstance().parse(
          "Loop(Loop(Pack(Atom),Element(Atom)),Element(Element(Atom)))");
    Out test = Out.fromTerm(trm);
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }

  @Test
  public void testFromString() {
    String s = "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))";
    Out test = Out.fromString(s);
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }

  @Test
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
