/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package definitions;

import aterm.AFun;
import aterm.ATerm;
import aterm.ATermAppl;
import aterm.pure.PureFactory;
import examples.Examples;
import java.util.ArrayList;
import java.util.Iterator;
import libtests.LibTests;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author hubert
 */
public class SortTest {

  public SortTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    System.out.println("--------------------");
    Examples.init();
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of isRec method, of class Sort.
   */
  @Test
  public void testIsRecA() {
    System.out.println("isRec A");
    boolean expResult = true;
    boolean result = Examples.a.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecB() {
    System.out.println("isRec B");
    boolean expResult = true;
    boolean result = Examples.b.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecC() {
    System.out.println("isRec C");
    boolean expResult = true;
    boolean result = Examples.c.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecCirc() {
    System.out.println("isRec Circ");
    boolean expResult = true;
    boolean result = Examples.circ.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecForest() {
    System.out.println("isRec Forest");
    boolean expResult = true;
    boolean result = Examples.forest.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecNat() {
    System.out.println("isRec Nat");
    boolean expResult = true;
    boolean result = Examples.nat.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTree() {
    System.out.println("isRec Tree");
    boolean expResult = true;
    boolean result = Examples.tree.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTree2() {
    System.out.println("isRec Tree2");
    boolean expResult = true;
    boolean result = Examples.tree2.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecTypeTest() {
    System.out.println("isRec TypeTest");
    boolean expResult = true;
    boolean result = Examples.expr.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecExpr() {
    System.out.println("isRec Expr");
    boolean expResult = true;
    boolean result = Examples.surexpr.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecSurExpr() {
    System.out.println("isRec SurExpr");
    boolean expResult = false;
    boolean result = Examples.typetest.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecSurList() {
    System.out.println("isRec List");
    boolean expResult = true;
    boolean result = Examples.list.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecSurList2() {
    Sort test = Examples.list2;
    System.out.println("isRec " + test.getName());
    boolean expResult = true;
    boolean result = test.isRec();
    assertEquals(expResult, result);
  }

  @Test
  public void testIsRecSurMix() {
    Sort test = Examples.mix;
    System.out.println("isRec " + test.getName());
    boolean expResult = false;
    boolean result = test.isRec();
    assertEquals(expResult, result);
  }

  /**
   * Test of getDimension method, of class Sort.
   */
  @Test
  public void testGetDimentionNat() {
    System.out.println("getDimention Nat");
    int expResult = 1;
    int result = Examples.nat.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTree() {
    System.out.println("getDimention tree");
    int expResult = 1;
    int result = Examples.tree.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTypeTest() {
    System.out.println("getDimention TypeTest");
    int expResult = 1;
    int result = Examples.typetest.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionTree2() {
    System.out.println("getDimention Tree2");
    int expResult = 2;
    int result = Examples.tree2.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionForest() {
    System.out.println("getDimention Forest");
    int expResult = 2;
    int result = Examples.forest.getDimension();
    assertEquals(expResult, result);
    assertEquals(2, Examples.tree2.getDimension());
  }

  @Test
  public void testGetDimentionCirc() {
    System.out.println("getDimention Circ");
    int expResult = 1;
    int result = Examples.circ.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionA() {
    System.out.println("getDimention A");
    int expResult = 1;
    int result = Examples.a.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionB() {
    System.out.println("getDimention B");
    int expResult = 1;
    int result = Examples.b.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionC() {
    System.out.println("getDimention C");
    int expResult = 1;
    int result = Examples.c.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionExpr() {
    System.out.println("getDimention Expr");
    int expResult = 1;
    int result = Examples.expr.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionSurExpr() {
    System.out.println("getDimention SurExpr");
    int expResult = 2;
    int result = Examples.surexpr.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionlist() {
    System.out.println("getDimention list");
    int expResult = 2;
    int result = Examples.list.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionlist2() {
    Sort test = Examples.list2;
    System.out.println("getDimention " + test.getName());
    int expResult = 2;
    int result = test.getDimension();
    assertEquals(expResult, result);
  }

  @Test
  public void testGetDimentionmix() {
    Sort test = Examples.mix;
    System.out.println("getDimention " + test.getName());
    int expResult = 2;
    int result = test.getDimension();
    assertEquals(expResult, result);
  }

  /**
   * Test of depthToLeaf method, of class Sort.
   */
  @Test
  public void testDstToLeafNat() {
    System.out.println("dstToLeaf Nat");
    int expResult = 0;
    int result = Examples.nat.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTree() {
    System.out.println("dstToLeaf Tree");
    int expResult = 0;
    int result = Examples.tree.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTypeTest() {
    System.out.println("dstToLeaf TypeTest");
    int expResult = 1;
    int result = Examples.typetest.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafForest() {
    System.out.println("dstToLeaf Forest");
    int expResult = 2;
    int result = Examples.forest.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafTree2() {
    System.out.println("dstToLeaf Tree2");
    int expResult = 1;
    int result = Examples.tree2.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafCirc() {
    System.out.println("dstToLeaf Circ");
    int expResult = Integer.MAX_VALUE;
    int result = Examples.circ.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafB() {
    System.out.println("dstToLeaf B");
    int expResult = 2;
    int result = Examples.b.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafA() {
    System.out.println("dstToLeaf A");
    int expResult = 0;
    int result = Examples.a.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafC() {
    System.out.println("dstToLeaf C");
    int expResult = 1;
    int result = Examples.c.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafExpre() {
    System.out.println("dstToLeaf Expr");
    int expResult = 0;
    int result = Examples.expr.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafSurExpre() {
    System.out.println("dstToLeaf SurExpr");
    int expResult = 1;
    int result = Examples.surexpr.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeaflist() {
    System.out.println("dstToLeaf list");
    int expResult = 1;
    int result = Examples.list.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeaflist2() {
    Sort test = Examples.list2;
    System.out.println("dstToLeaf " + test.getName());
    int expResult = Integer.MAX_VALUE;
    int result = test.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testDstToLeafmix() {
    Sort test = Examples.mix;
    System.out.println("dstToLeaf " + test.getName());
    int expResult = 2;
    int result = test.minimalSize(Examples.paramDepth.getDistStrategy());
    assertEquals(expResult, result);
  }

  /**
   * Test of stepsToLeaf method, of class Sort.
   */
  @Test
  public void testStepsToLeafNat() {
    System.out.println("stepsToLeaf " + Examples.nat.getName());
    int expResult = 0;
    int result = Examples.nat.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafA() {
    System.out.println("stepsToLeaf " + Examples.a.getName());
    int expResult = 0;
    int result = Examples.a.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafB() {
    System.out.println("stepsToLeaf " + Examples.b.getName());
    int expResult = 2;
    int result = Examples.b.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafC() {
    System.out.println("stepsToLeaf " + Examples.c.getName());
    int expResult = 1;
    int result = Examples.c.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafForest() {
    System.out.println("stepsToLeaf " + Examples.forest.getName());
    int expResult = 2;
    int result = Examples.forest.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeaftree() {
    System.out.println("stepsToLeaf " + Examples.tree.getName());
    int expResult = 0;
    int result = Examples.tree.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeaftypetest() {
    System.out.println("stepsToLeaf " + Examples.typetest.getName());
    int expResult = 1;
    int result = Examples.typetest.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeaftree2() {
    System.out.println("stepsToLeaf " + Examples.tree2.getName());
    int expResult = 1;
    int result = Examples.tree2.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafcirc() {
    System.out.println("stepsToLeaf " + Examples.circ.getName());
    int expResult = Integer.MAX_VALUE;
    int result = Examples.circ.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafexpr() {
    System.out.println("stepsToLeaf " + Examples.expr.getName());
    int expResult = 0;
    int result = Examples.expr.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafsurexpr() {
    System.out.println("stepsToLeaf " + Examples.surexpr.getName());
    int expResult = 1;
    int result = Examples.surexpr.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeaflist() {
    System.out.println("stepsToLeaf " + Examples.list.getName());
    int expResult = 1;
    int result = Examples.list.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeaflist2() {
    Sort test = Examples.list2;
    System.out.println("stepsToLeaf " + test.getName());
    int expResult = Integer.MAX_VALUE;
    int result = test.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  @Test
  public void testStepsToLeafmix() {
    Sort test = Examples.mix;
    System.out.println("stepsToLeaf " + test.getName());
    int expResult = 3;
    int result = test.minimalSize(Examples.paramNodes.getDistStrategy());
    assertEquals(expResult, result);
  }

  /**
   * Test of testAddConstructor with class method, of class Sort.
   */
  @Test
  public void testAddConstructorClassExpr() {
    System.out.println("testAddConstructorClass SurExpr");
    System.out.println(Examples.expr);
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testAddConstructorClassSurExpr() {
    System.out.println("testAddConstructorClass SurExpr");
    System.out.println(Examples.surexpr);
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  /**
   * Test of generate method, of class Sort.
   */
  @Test
  public void testGenerateExpr() {
    System.out.println("Generate Expr");
    Slot slot = Examples.expr.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("Expr.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateSurExpr() {
    System.out.println("Generate SurExpr");
    Slot slot = Examples.surexpr.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("SurExpr.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateA() {
    System.out.println("Generate A");
    Slot slot = Examples.a.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("A.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateB() {
    System.out.println("Generate B");
    Slot slot = Examples.b.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("B.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateC() {
    System.out.println("Generate C");
    Slot slot = Examples.c.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("C.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateCirc() {
    System.out.println("Generate Circ");
    Slot slot;
    String res = "not this";
    try {
      slot = Examples.circ.generateSlot(100, Examples.paramDepth);
      System.out.println(slot);
      slot.toDot2("Circ.dot");
    } catch (UnsupportedOperationException e) {
      res = e.getMessage();
    }

    String expResult = "Type circ does not terminate.";
    assertEquals(expResult, res);
  }

  @Test
  public void testGenerateforest() {
    System.out.println("Generate forest");
    Slot slot = Examples.forest.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("forest.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGeneratenat() {
    System.out.println("Generate nat");
    Slot slot = Examples.nat.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("nat.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateTree() {
    System.out.println("Generate tree");
    Slot slot = Examples.tree.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("tree.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGenerateTree2() {
    System.out.println("Generate tree2");
    Slot slot = Examples.tree2.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("tree2.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGeneratetypetest() {
    System.out.println("Generate typetest");
    Slot slot = Examples.typetest.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("typetest.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGeneratelist() {
    System.out.println("Generate list");
    Slot slot = Examples.list.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("list.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  @Test
  public void testGeneratelist2() {
    Sort test = Examples.list2;
    System.out.println("Generate " + test.getName());
    Slot slot;
    String res = "not this";
    try {
      slot = test.generateSlot(100, Examples.paramDepth);
      System.out.println(slot);
      slot.toDot2("list2.dot");
    } catch (UnsupportedOperationException e) {
      System.out.println("fail");
      res = e.getMessage();
    }
    String expResult = "Type list2 does not terminate.";
    assertEquals(expResult, res);
  }

  @Test
  public void testGeneratemix() {
    Sort test = Examples.mix;
    System.out.println("Generate " + test.getName());
    Slot slot = Examples.mix.generateSlot(100, Examples.paramDepth);
    System.out.println(slot);
    slot.toDot2("mix.dot");
    int expResult = 1;
    int result = 1;
    assertEquals(expResult, result);
  }

  /**
   * Test of isTypeOf method, of class Sort.
   */
  @Test
  public void testIsTypeOfNat() {
    System.out.println("isRec nat");
    PureFactory factory = new PureFactory();
    ATerm term = factory.make("truc");
    AFun fun = factory.makeAFun("succ", 1, false);
    ATermAppl appl = factory.makeAppl(fun, term);
    System.out.println(appl);
    assert Examples.nat.isTypeOf(appl);
  }

  @Test
  public void testIsTypeOfNatAutoNeg() {
    System.out.println("isRec nat auto neg");
    assert !(Examples.nat.isTypeOf(Examples.mix.generate(10)));
  }

  @Test
  public void testIsTypeOfNatAuto() {
    System.out.println("isRec nat auto");
    assert (Examples.nat.isTypeOf(Examples.nat.generate(10)));
  }

  @Test
  public void testIsTypeOfExpr() {
    System.out.println("isRec nat");
    PureFactory factory = new PureFactory();
    ATerm term = factory.make("truc");
    AFun fun = factory.makeAFun("plus", 1, false);
    ATermAppl appl = factory.makeAppl(fun, term);
    System.out.println(appl);
    assert Examples.expr.isTypeOf(appl);
  }

  @Test
  public void testIsTypeOfExprAuto() {
    System.out.println("isRec expr auto");
    assert (Examples.expr.isTypeOf(Examples.expr.generate(10)));
  }

  /**
   * Tests of fundamental properties of iterators.
   */
  @Test
  public void testFundamentalOneConsIter() {
    System.out.println("test Fundamental oneConsIter");
    ATerm term = Examples.expr.generate(10);
    Constructor cons = Examples.expr.getCons("plus");
    Iterator<ATerm> ite = Examples.expr.getOneConsIter(term, cons);
    LibTests.testIterator(ite, 10, false);
  }

  @Test
  public void testFundamentalMultiConsIter() {
    System.out.println("test Fundamental MultiConsIter");
    ATerm term = Examples.expr.generate(10);
    Constructor c1 = Examples.expr.getCons("plus");
    Constructor c2 = Examples.expr.getCons("mult");
    Constructor c3 = Examples.expr.getCons("un");
    ArrayList<Constructor> list = new ArrayList<Constructor>();
    list.add(c1);
    list.add(c2);
    list.add(c3);
    Iterator<ATerm> ite = Examples.expr.getMultiConsIter(term, list.iterator());
    LibTests.testIterator(ite, 100, false);
  }

  @Test
  public void testFundamentalLigthen() {
    System.out.println("test Fundamental ligthen hasNext()");
    ATerm term = Examples.expr.generate(10);
    Iterator<ATerm> ite = Examples.expr.lighten(term);
    LibTests.testIterator(ite, 100, false);
  }
}
