package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.*;
import gom.javahook.types.*;

public class TestJavaHook extends TestCase {

  %include { javahook/JavaHook.tom }

  public static String newline = System.getProperty("line.separator");

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestJavaHook.class));
  }

  public void testEmptyMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    `EmptyTerm();
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()" + newline);
  }

  public void testUnaryMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Unary(EmptyTerm());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()"+newline+"Unary(EmptyTerm())" + newline);
  }

  public void testBinaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Binary(EmptyTerm(),EmptyTerm());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()" + newline + "EmptyTerm()" + newline + "Binary(EmptyTerm(),EmptyTerm())" + newline );
  }

  public void testTernaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())));
    System.setOut(tmpOut);
    assertEquals(res.toString(),
        "EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"Unary(EmptyTerm())"+newline +"Binary(EmptyTerm(),Unary(EmptyTerm()))"+newline +"Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())))" + newline);
  }

  public void testVaryMakeInsertHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Vary(EmptyTerm(),EmptyTerm(),EmptyTerm(),EmptyTerm());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "inserting(EmptyTerm(),Vary())" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm(),EmptyTerm()))" + newline);
  }
}
