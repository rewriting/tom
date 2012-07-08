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
   * Test of getDimention method, of class Algebraic.
   */
  @Test
  public void testGetDimentionNat() {
    System.out.println("getDimention Nat");

    Scope scope = new Scope();

    Algebraic nat = new Algebraic(scope);
    Algebraic[] tab = {nat};
    nat.addConstructor(tab);
    Algebraic[] vide = new Algebraic[0];
    nat.addConstructor(vide);

    scope.setDependances();

    int expResult = 1;
    int result = nat.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTree() {
    System.out.println("getDimention tree");

    Scope scope = new Scope();

    Algebraic tree = new Algebraic(scope);
    Algebraic[] tab2 = {tree, tree};
    Algebraic[] vide = new Algebraic[0];
    tree.addConstructor(tab2);
    tree.addConstructor(vide);

    scope.setDependances();

    int expResult = 1;
    int result = tree.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTypeTest() {
    System.out.println("getDimention TypeTest");

    Scope scope = new Scope();
    
    Algebraic nat = new Algebraic(scope);
    Algebraic[] tab = {nat};
    nat.addConstructor(tab);
    Algebraic[] vide = new Algebraic[0];
    nat.addConstructor(vide);
    
    Algebraic tree = new Algebraic(scope);
    Algebraic[] tab2 = {tree, tree};
    tree.addConstructor(tab2);
    tree.addConstructor(vide);

    Algebraic typetest = new Algebraic(scope);
    Algebraic[] cons_test1 = {tree};
    Algebraic[] cons_test2 = {nat};
    typetest.addConstructor(cons_test1);
    typetest.addConstructor(cons_test2);

    scope.setDependances();

    int expResult = 1;
    int result = typetest.getDimention();
    assertEquals(expResult, result);
  }
  
  @Test
  public void testGetDimentionForest() {
    System.out.println("getDimention Forest");

    Scope scope = new Scope();
    
    Algebraic nat = new Algebraic(scope);
    Algebraic[] tab = {nat};
    nat.addConstructor(tab);
    Algebraic[] vide = new Algebraic[0];
    nat.addConstructor(vide);

    Algebraic forest = new Algebraic(scope);
    Algebraic tree = new Algebraic(scope);
    Algebraic[] cons_tree1 = {nat};
    Algebraic[] cons_tree2 = {forest};
    Algebraic[] cons_forest1 = {tree};
    forest.addConstructor(cons_forest1);
    tree.addConstructor(cons_tree1);
    tree.addConstructor(cons_tree2);
    
    scope.setDependances();

    int expResult = 2;
    int result = forest.getDimention();
    assertEquals(expResult, result);
    
    assertEquals(2, tree.getDimention());
  }
  
   @Test
  public void testGetDimentionCirc() {
    System.out.println("getDimention Circ");

    Scope scope = new Scope();

    Algebraic circ = new Algebraic(scope);
    Algebraic[] cons_circ = {circ};
    circ.addConstructor(cons_circ);

    scope.setDependances();

    int expResult = 1;
    int result = circ.getDimention();
    assertEquals(expResult, result);
  }

  /**
   * Test of dstToLeaf method, of class Algebraic.
   */
  @Test
  public void testDstToLeaf() {
    System.out.println("dstToLeaf");
    Algebraic instance = null;
    int expResult = 0;
    int result = instance.dstToLeaf();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }
}
