package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.*;
import gom.javahook.types.*;

public class TestJavaHook extends TestCase {

  %include { javahook/JavaHook.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestJavaHook.class));
  }

  public void testEmptyMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Empty();
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()\n");
  }

  public void testUnaryMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Unary(Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()\nUnary(Empty)\n");
  }

  public void testBinaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Binary(Empty(),Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()\nEmpty()\nBinary(Empty, Empty)\n");
  }

  public void testTernaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Ternary(Empty(),Empty(),Binary(Empty(),Unary(Empty())));
    System.setOut(tmpOut);
    assertEquals(res.toString(),
        "Empty()\nEmpty()\nEmpty()\nEmpty()\nUnary(Empty)\nBinary(Empty, Unary(Empty))\nTernary(Empty, Empty, Binary(Empty,Unary(Empty)))\n");
  }

  public void testVaryMakeInsertHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Vary(Empty(),Empty(),Empty(),Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()\n"+
        "Empty()\n"+
        "Empty()\n"+
        "Empty()\n"+
        "Vary(Empty, EmptyVary)\n"+
        "Vary(Empty, ConsVary(Empty,EmptyVary))\n"+
        "Vary(Empty, ConsVary(Empty,ConsVary(Empty,EmptyVary)))\n"+
        "Vary(Empty, ConsVary(Empty,ConsVary(Empty,ConsVary(Empty,EmptyVary))))\n");
  }
}
