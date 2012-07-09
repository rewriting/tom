/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import static org.junit.Assert.assertEquals;
import org.junit.*;
import sort.types.expr.*;

/**
 *
 * @author hubert
 */
public class AlgebraicTest {

  Scope scope;
  Algebraic nat;
  Algebraic tree;
  Algebraic tree2;
  Algebraic forest;
  Algebraic circ;
  Algebraic typetest;
  Algebraic a, b, c;

  public AlgebraicTest() {
    scope = new Scope();

    nat = new Algebraic(scope, "nat");
    Algebraic[] tab = {nat};
    nat.addConstructor(tab);
    Algebraic[] vide = new Algebraic[0];
    nat.addConstructor(vide);

    tree = new Algebraic(scope, "tree");
    Algebraic[] tab2 = {tree, tree};
    tree.addConstructor(tab2);
    tree.addConstructor(vide);

    typetest = new Algebraic(scope, "typetest");
    Algebraic[] cons_test1 = {tree};
    Algebraic[] cons_test2 = {nat};
    typetest.addConstructor(cons_test1);
    typetest.addConstructor(cons_test2);


    forest = new Algebraic(scope, "forest");
    tree2 = new Algebraic(scope, "tree2");
    Algebraic[] cons_tree1 = {nat};
    Algebraic[] cons_tree2 = {forest};
    Algebraic[] cons_forest1 = {tree2};
    forest.addConstructor(cons_forest1);
    tree2.addConstructor(cons_tree1);
    tree2.addConstructor(cons_tree2);

    circ = new Algebraic(scope, "circ");
    Algebraic[] cons_circ = {circ};
    circ.addConstructor(cons_circ);

    a = new Algebraic(scope, "a");
    b = new Algebraic(scope, "b");
    c = new Algebraic(scope, "c");
    Algebraic[] cons_a1 = {};
    Algebraic[] cons_a2 = {b};
    Algebraic[] cons_b = {c};
    Algebraic[] cons_c = {a};
    a.addConstructor(cons_a1);
    a.addConstructor(cons_a2);
    b.addConstructor(cons_b);
    c.addConstructor(cons_c);

    scope.setDependances();
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
   * Test of isRec method, of class Algebraic.
   */
  @Test
  public void testIsRecA() {
    System.out.println("isRec A");
    boolean expResult = true;
    boolean result = a.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecB() {
    System.out.println("isRec B");
    boolean expResult = true;
    boolean result = b.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecC() {
    System.out.println("isRec C");
    boolean expResult = true;
    boolean result = c.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecCirc() {
    System.out.println("isRec Circ");
    boolean expResult = true;
    boolean result = circ.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecForest() {
    System.out.println("isRec Forest");
    boolean expResult = true;
    boolean result = forest.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecNat() {
    System.out.println("isRec Nat");
    boolean expResult = true;
    boolean result = nat.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTree() {
    System.out.println("isRec Tree");
    boolean expResult = true;
    boolean result = tree.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTree2() {
    System.out.println("isRec Tree2");
    boolean expResult = true;
    boolean result = tree2.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTypeTest() {
    System.out.println("isRec TypeTest");
    boolean expResult = false;
    boolean result = typetest.isRec();
    assertEquals(expResult, result);
  }

  /**
   * Test of getDimention method, of class Algebraic.
   */
  @Test
  public void testGetDimentionNat() {
    System.out.println("getDimention Nat");
    int expResult = 1;
    int result = nat.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTree() {
    System.out.println("getDimention tree");
    int expResult = 1;
    int result = tree.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTypeTest() {
    System.out.println("getDimention TypeTest");
    int expResult = 1;
    int result = typetest.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTree2() {
    System.out.println("getDimention Tree2");
    int expResult = 2;
    int result = tree2.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionForest() {
    System.out.println("getDimention Forest");
    int expResult = 2;
    int result = forest.getDimention();
    assertEquals(expResult, result);
    assertEquals(2, tree2.getDimention());
  }

  @Test
  public void testGetDimentionCirc() {
    System.out.println("getDimention Circ");
    int expResult = 1;
    int result = circ.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionA() {
    System.out.println("getDimention A");
    int expResult = 1;
    int result = a.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionB() {
    System.out.println("getDimention B");
    int expResult = 1;
    int result = b.getDimention();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionC() {
    System.out.println("getDimention C");
    int expResult = 1;
    int result = c.getDimention();
    assertEquals(expResult, result);
  }

  /**
   * Test of dstToLeaf method, of class Algebraic.
   */
  @Test
  public void testDstToLeafNat() {
    System.out.println("dstToLeaf Nat");
    int expResult = 0;
    int result = nat.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTree() {
    System.out.println("dstToLeaf Tree");
    int expResult = 0;
    int result = tree.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTypeTest() {
    System.out.println("dstToLeaf TypeTest");
    int expResult = 1;
    int result = typetest.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafForest() {
    System.out.println("dstToLeaf Forest");
    int expResult = 2;
    int result = forest.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTree2() {
    System.out.println("dstToLeaf Tree2");
    int expResult = 1;
    int result = tree2.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafCirc() {
    System.out.println("dstToLeaf Circ");
    int expResult = Integer.MAX_VALUE;
    int result = circ.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafB() {
    System.out.println("dstToLeaf B");
    int expResult = 2;
    int result = b.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafA() {
    System.out.println("dstToLeaf A");
    int expResult = 0;
    int result = a.dstToLeaf();
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafC() {
    System.out.println("dstToLeaf C");
    int expResult = 1;
    int result = c.dstToLeaf();
    assertEquals(expResult, result);
  }
  
  @Test
  public void testAddConstructorClass() {
    System.out.println("testAddConstructorClass");
    Scope newscope = new Scope();
    Algebraic expr = new Algebraic(newscope, "sort.types.Expr");
    expr.addConstructor(mult.class);
    expr.addConstructor(plus.class);
    expr.addConstructor(un.class);
    expr.addConstructor(zero.class);
    System.out.println(expr);
    int expResult = 1;
    int result = c.dstToLeaf();
    assertEquals(expResult, result);
  }
}
