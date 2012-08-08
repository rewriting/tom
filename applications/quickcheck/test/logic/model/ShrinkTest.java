/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermList;
import aterm.pure.PureFactory;
import definitions.Buildable;
import examples.Examples;
import java.util.Iterator;
import libtests.LibTests;
import logic.model.BuildableDomain;
import logic.model.DomainInterpretation;
import logic.model.Shrink;
import logic.model.Shrink_java;
import org.junit.*;

/**
 *
 * @author hubert
 */
public class ShrinkTest {

  public ShrinkTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    System.out.println("---------------------------");
    Examples.init();
  }

  @After
  public void tearDown() {
  }

  /**
   * Tests of fundamental properties of iterators.
   */
  @Test
  public void testOneConsIter() {
    System.out.println("test oneConsIter");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    DomainInterpretation domain = new BuildableDomain(sort);
    Iterator<ATerm> ite = Shrink_java.getS1Iterator(term, domain);
    LibTests.testIterator(ite, 10, false);
  }

  /**
   * Test of s1 method, of class Shrink.
   */
  @Test
  public void testS1Tom() {
    System.out.println("test s1 ATermList");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1(term, domain);
    System.out.println(result);
//    assertEquals(expResult, result);
  }

  @Test
  public void testS1Iterator() {
    System.out.println("tes s1 Iterator");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    System.out.println(term.getChildAt(0));
    System.out.println(term.getChildAt(1));
    PureFactory factory = new PureFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink_java.s1(factory.makeList(term), domain);
    System.out.println(result.getLength() + " " + result);
//    assertEquals(expResult, result);
  }

  @Test
  public void testS2Iterator() {
    System.out.println("tes s2 Iterator");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    PureFactory factory = new PureFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink_java.s2(factory.makeList(term), domain);
    System.out.println(result);
//    assertEquals(expResult, result);
  }
}
