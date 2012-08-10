/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import examples.Examples;
import org.junit.*;

/**
 *
 * @author hubert
 */
public class ConstructorTest {

  public ConstructorTest() {
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
   * Test of isSubCons method, of class Constructor.
   */
  @Test
  public void testIsSubCons() {
    System.out.println("isSubCons");
    Buildable[] fields1 = {Examples.a, Examples.b};
    Buildable[] fields2 = {Examples.a, Examples.b, Examples.a};
    Constructor cons = new Constructor("cons", fields2);
    Constructor instance = new Constructor("instance", fields1);
    boolean expResult = true;
    boolean result = instance.isSubCons(cons);
    assert instance.isSubCons(cons);
    assert instance.isSubCons(instance);
    assert !cons.isSubCons(instance);
    assert cons.isSubCons(cons);
  }
}
