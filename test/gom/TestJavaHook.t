package gom;

import java.io.*;
import gom.javahook.types.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestJavaHook {

  %include { javahook/JavaHook.tom }

  public static String newline = System.getProperty("line.separator");

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.runClasses(TestJavaHook.class);
  }

  @Test
  public void testEmptyMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    `EmptyTerm();
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()" + newline);
  }

  @Test
  public void testUnaryMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Unary(EmptyTerm());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()"+newline+"Unary(EmptyTerm())" + newline);
  }

  @Test
  public void testBinaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Binary(EmptyTerm(),EmptyTerm());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"EmptyTerm()" + newline + "EmptyTerm()" + newline + "Binary(EmptyTerm(),EmptyTerm())" + newline );
  }

  @Test
  public void testTernaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())));
    System.setOut(tmpOut);
    assertEquals(res.toString(),
        "EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"Unary(EmptyTerm())"+newline +"Binary(EmptyTerm(),Unary(EmptyTerm()))"+newline +"Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())))" + newline);
  }

  @Test
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
