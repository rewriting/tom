/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.ATermList;
import aterm.pure.PureFactory;
import definitions.Buildable;
import examples.Examples;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import libtests.LibTests;
import logic.model.BuildableDomain;
import logic.model.DomainInterpretation;
import logic.model.Shrink;
import logic.model.ShrinkIterator;
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
  public void testFundamentalS1IteratorOneArg() {
    System.out.println("test fundamental s1 iterator one arg");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    DomainInterpretation domain = new BuildableDomain(sort);
    Iterator<ATerm> ite = ShrinkIterator.getS1Iterator(term, domain);
    LibTests.testIterator(ite, 10, false);
  }
  
  @Test
  public void testFundamentalS1IteratorList() {
    System.out.println("test fundamental s1 iterator list");
    Buildable sort = Examples.expr;
    ATerm t1 = sort.generate(10);
    ATerm t2 = sort.generate(10);
    ATerm t3 = sort.generate(10);
    ATerm t4 = sort.generate(10);
    ATerm t5 = sort.generate(10);
    List<ATerm> list = new LinkedList<ATerm>();
    list.add(t1);
    list.add(t2);
    list.add(t3);
    list.add(t4);
    list.add(t5);
    DomainInterpretation domain = new BuildableDomain(sort);
    Iterator<ATerm> ite = ShrinkIterator.s1(list.iterator(), domain);
    LibTests.testIterator(ite, 100, false);
  }
  
  @Test
  public void testFundamentalS2IteratorList() {
    System.out.println("test fundamental s2 iterator list");
    Buildable sort = Examples.expr;
    ATerm t1 = sort.generate(10);
    ATerm t2 = sort.generate(10);
    ATerm t3 = sort.generate(10);
    ATerm t4 = sort.generate(10);
    ATerm t5 = sort.generate(10);
    List<ATerm> list = new LinkedList<ATerm>();
    list.add(t1);
    list.add(t2);
    list.add(t3);
    list.add(t4);
    list.add(t5);
    DomainInterpretation domain = new BuildableDomain(sort);
    Iterator<ATerm> ite = ShrinkIterator.s2(list.iterator(), domain);
    LibTests.testIterator(ite, 100, false);
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
    ATermFactory factory = term.getFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.s1(factory.makeList(term), domain);
    System.out.println(result.getLength() + " " + result);
//    assertEquals(expResult, result);
  }

  @Test
  public void testS2Iterator() {
    System.out.println("tes s2 Iterator");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    ATermFactory factory = term.getFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.s2(factory.makeList(term), domain);
    System.out.println(result);
//    assertEquals(expResult, result);
  }
}
