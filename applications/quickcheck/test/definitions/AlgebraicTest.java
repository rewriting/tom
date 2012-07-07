/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import static org.junit.Assert.*;
import org.junit.*;
import tom.library.sl.Strategy;

/**
 *
 * @author hubert
 */
public class AlgebraicTest {
  
  public AlgebraicTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of addConstructor method, of class Algebraic.
   */
  @Test
  public void testAddConstructor() {
    System.out.println("addConstructor");
    Typable[] listTypes = null;
    Algebraic instance = null;
    Algebraic expResult = null;
    Algebraic result = instance.addConstructor(listTypes);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of updateDependances method, of class Algebraic.
   */
  @Test
  public void testUpdateDependances() {
    System.out.println("updateDependances");
    Algebraic instance = null;
    boolean expResult = false;
    boolean result = instance.updateDependances();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isRec method, of class Algebraic.
   */
  @Test
  public void testIsRec() {
    System.out.println("isRec");
    Algebraic instance = null;
    boolean expResult = false;
    boolean result = instance.isRec();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of getDimention method, of class Algebraic.
   */
  @Test
  public void testGetDimention() {
    System.out.println("getDimention");
    Algebraic instance = null;
    int expResult = 0;
    int result = instance.getDimention();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of dependsOn method, of class Algebraic.
   */
  @Test
  public void testDependsOn() {
    System.out.println("dependsOn");
    Typable t = null;
    Algebraic instance = null;
    boolean expResult = false;
    boolean result = instance.dependsOn(t);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of makeGenerator method, of class Algebraic.
   */
  @Test
  public void testMakeGenerator() {
    System.out.println("makeGenerator");
    Request request = null;
    Algebraic instance = null;
    Strategy expResult = null;
    Strategy result = instance.makeGenerator(request);
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of isLeafable method, of class Algebraic.
   */
  @Test
  public void testIsLeafable() {
    System.out.println("isLeafable");
    Algebraic instance = null;
    int expResult = 0;
    int result = instance.isLeafable();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
