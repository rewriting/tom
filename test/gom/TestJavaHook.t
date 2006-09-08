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
    `Empty();
    System.setOut(tmpOut);   
    assertEquals(res.toString(),"Empty()" + newline);
  }

  public void testUnaryMakeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Unary(Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()"+newline+"Unary(Empty())" + newline);
  }

  public void testBinaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Binary(Empty(),Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()" + newline + "Empty()" + newline + "Binary(Empty(),Empty())" + newline );
  }

  public void testTernaryMakeBeforeHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Ternary(Empty(),Empty(),Binary(Empty(),Unary(Empty())));
    System.setOut(tmpOut);
    assertEquals(res.toString(),
        "Empty()"+newline +"Empty()"+newline +"Empty()"+newline +"Empty()"+newline +"Unary(Empty())"+newline +"Binary(Empty(),Unary(Empty()))"+newline +"Ternary(Empty(),Empty(),Binary(Empty(),Unary(Empty())))" + newline);
  }

  public void testVaryMakeInsertHook() {
    PrintStream tmpOut = System.out;
    ByteArrayOutputStream res = new ByteArrayOutputStream();
    System.setOut(new PrintStream(res));
    Term empty = `Vary(Empty(),Empty(),Empty(),Empty());
    System.setOut(tmpOut);
    assertEquals(res.toString(),"Empty()"+ newline +
        "Empty()"+ newline +
        "Empty()"+ newline +
        "Empty()"+ newline +
        "Vary(Empty(),EmptyVary())" + newline +
        "Vary(Empty(),ConsVary(Empty(),EmptyVary()))" + newline +
        "Vary(Empty(),ConsVary(Empty(),ConsVary(Empty(),EmptyVary())))" + newline +
        "Vary(Empty(),ConsVary(Empty(),ConsVary(Empty(),ConsVary(Empty(),EmptyVary()))))" + newline);
  }
}
