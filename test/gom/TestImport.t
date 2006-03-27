package gom;

import java.io.*;
import java.util.*;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.importing.types.*;
import gom.imported.types.*;

public class TestImport extends TestCase {

  %include { mutraveler.tom }
  %include { importing/Importing.tom }
  %typeterm Collection {
    implement { Collection }
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestImport.class));
  }

  public void testMake() {
    Out test = `Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())));
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom),Element(Atom)),Element(Element(Atom)))");
  }

  public void testStrat() {
    Out test = `Loop(Loop(Pack(Atom()),LeafSlot(Leaf())),Element(Element(Atom())));
    Collection set = new ArrayList(); /* Make sure we count all inserts */
    try {
      `BottomUp(Count(set)).visit(test);
    } catch (Exception e) {
      fail(e + " catched");
    }
    assertEquals(4,set.size());
  }

  %strategy Count(col:Collection) extends `Identity() {
    visit Inner {
      a@Atom() -> { col.add(`a); }
    }
    visit Out {
      l@Loop[] -> { col.add(`l); }
    }
  }
}
