/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gen;

import aterm.ATerm;
import aterm.ATermList;
import definitions.Buildable;
import examples.Examples;
import logic.model.BuildableDomain;
import logic.model.DomainInterpretation;
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
    Examples.init();
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of s1 method, of class Shrink.
   */
  @Test
  public void testS1() {
    System.out.println("s1");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1(term, domain);
    System.out.println(result);
//    assertEquals(expResult, result);
  }


  public static void main(String arg[]) {
    Examples.init();
    System.out.println("s1");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    System.out.println(term);
    DomainInterpretation domain = new BuildableDomain(sort);

    ATermList result = Shrink.s1(term, domain);
    System.out.println(result);
    System.out.println("s1bis: " + Shrink.s1bis(term, domain));
  }


}
