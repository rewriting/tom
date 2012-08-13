/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.ATermIterator;
import aterm.ATermIteratorFromList;
import aterm.ATermList;
import definitions.Buildable;
import examples.Examples;
import examples.ExamplesTerms;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import libtests.LibTests;
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
    ExamplesTerms.init();
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
    ATermIterator ite = ShrinkIterator.s1Strict(term, domain);
    LibTests.testIterator(ite, 10000000, false);
  }
  
  @Test
  public void testFundamentalCloneS1IteratorOneArg() {
    System.out.println("test fundamental Clone s1 iterator one arg");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermIterator ite = ShrinkIterator.s1Strict(term, domain);
    ATermIterator ite2 = ite.clone();
    ATermList l1 = ShrinkIterator.toATermList(ite, term.getFactory());
    ATermList l2 = ShrinkIterator.toATermList(ite2, term.getFactory());
    System.out.println(l2.getLength());
    assert l1.equals(l2);
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
    ATermIterator ite = ShrinkIterator.s1Large(new ATermIteratorFromList(list), domain);
    LibTests.testIterator(ite, 10000000, false);
  }
  
  @Ignore @Test
  public void testFundamentalCloneS1IteratorList() {
    System.out.println("test fundamental clone s1 iterator list");
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
    ATermIterator ite = ShrinkIterator.s1Large(new ATermIteratorFromList(list), domain);
    ATermIterator ite2 = ite.clone();
    ATermList l1 = ShrinkIterator.toATermList(ite, t1.getFactory());
    ATermList l2 = ShrinkIterator.toATermList(ite2, t1.getFactory());
    System.out.println(l1.getLength());
    System.out.println(l2.getLength());
    assert l1.equals(l2);
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
    ATermIterator ite = ShrinkIterator.s2Large(new ATermIteratorFromList(list), domain);
    LibTests.testIterator(ite, 10000000, false);
  }

  @Test
  public void testFundamentalS1WithDepthStrict() {
    System.out.println("test fundamental s1WithDepthStrict");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermIterator ite = ShrinkIterator.s1WithDepthStrict(term, domain, 1);
    LibTests.testIterator(ite, 10000000, false);
  }
  
  @Test
  public void testFundamentalCloneS1WithDepthStrict() {
    System.out.println("test fundamental clone s1WithDepthStrict");
    Buildable sort = Examples.expr;
    ATerm term = sort.generate(10);
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermIterator ite = ShrinkIterator.s1WithDepthStrict(term, domain, 1);
    ATermIterator ite2 = ite.clone();
    ATermList l1 = ShrinkIterator.toATermList(ite, term.getFactory());
    ATermList l2 = ShrinkIterator.toATermList(ite2, term.getFactory());
    System.out.println(l1.getLength());
    System.out.println(l2.getLength());
    assert l1.equals(l2);
  }
  
//  @Test
//  public void testFundamentalFilter() {
//    System.out.println("test fundamental s1 iterator one arg");
//    Buildable sort = Examples.expr;
//    ATerm term = sort.generate(10);
//    DomainInterpretation domain = new BuildableDomain(sort);
//    ATermIterator ite = 
//    LibTests.testIterator(ite, 10000000, false);
//  }

  /**
   * Tests of s1Strict method, of class Shrink.
   */
  @Test
  public void testS1StrictList() {
    System.out.println("test s1 strict ATermList");
    Buildable sort = Examples.expr;
    ATerm term = ExamplesTerms.expr_T1;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1Strict(term, domain);
    assert result.getChildCount() == 2;
    assert result.getChildAt(0).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(1).equals(ExamplesTerms.expr_T1Right);
  }

  @Test
  public void testS1LargeList() {
    System.out.println("test s1 large ATermList");
    Buildable sort = Examples.expr;
    ATerm t1 = ExamplesTerms.expr_T1;
    ATerm t2 = ExamplesTerms.expr_T1;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermFactory factory = t1.getFactory();
    ATermList result = Shrink.s1Large(factory.makeList(t1).append(t2), domain);
    assert result.getChildCount() == 4;
    assert result.getChildAt(0).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(1).equals(ExamplesTerms.expr_T1Right);
    assert result.getChildAt(2).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(3).equals(ExamplesTerms.expr_T1Right);
  }

  @Test
  public void testS1StrictListNonShrinkable() {
    System.out.println("test s1 strict list non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1Strict(term, domain);
    assert result.getChildCount() == 0;
  }

  @Test
  public void testS1LargeListNonShrinkable() {
    System.out.println("test s1 large list non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    ATermFactory factory = term.getFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1Large(factory.makeList(term), domain);
    assert result.getChildCount() == 1;
    assert result.getChildAt(0).equals(ExamplesTerms.nonS1_2_Shinkable);
  }

  /**
   * Tests of shrink algorithm S1 using iterator.
   */
  @Test
  public void testS1StrictIterator() {
    System.out.println("test s1 strict Iterator");
    Buildable sort = Examples.expr;
    ATerm term = ExamplesTerms.expr_T1;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s1Strict(term, domain), term.getFactory());
    assert result.getChildCount() == 2;
    assert result.getChildAt(0).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(1).equals(ExamplesTerms.expr_T1Right);
  }

  @Test
  public void testS1LargeIterator() {
    System.out.println("test s1 large Iterator");
    Buildable sort = Examples.expr;
    ATerm t1 = ExamplesTerms.expr_T1;
    ATerm t2 = ExamplesTerms.expr_T1;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermFactory factory = t1.getFactory();
    ATermList result = ShrinkIterator.s1Large(factory.makeList(t1).append(t2), domain);
    assert result.getChildCount() == 4;
    assert result.getChildAt(0).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(1).equals(ExamplesTerms.expr_T1Right);
    assert result.getChildAt(2).equals(ExamplesTerms.expr_T1Left);
    assert result.getChildAt(3).equals(ExamplesTerms.expr_T1Right);
  }

  @Test
  public void testS1StrictIteratorNonShrinkable() {
    System.out.println("test s1 strict Iterator non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s1Strict(term, domain), term.getFactory());
    assert result.getChildCount() == 0;
  }

  @Test
  public void testS1LargeIteratorNonShrinkable() {
    System.out.println("test s1 large Iterator non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermFactory factory = term.getFactory();
    ATermList result = ShrinkIterator.s1Large(factory.makeList(term), domain);
    assert result.getChildCount() == 1;
    assert result.getChildAt(0).equals(ExamplesTerms.nonS1_2_Shinkable);
  }

  /**
   * Tests of shrink algorithm S2 using iterator.
   */
  @Test
  public void testS2StrictIterator() {
    System.out.println("test s2 Iterator");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s2Strict(term, domain), term.getFactory());
    assert result.getChildCount() == 8;
    for(int i = 0; i < 8; i++) {
      assert result.getChildAt(i).equals(ExamplesTerms.sortTestShrink2_s2test_children[i]);
    }
  }

  @Test
  public void testS2LargeIterator() {
    System.out.println("test s2 Iterator");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    ATermFactory factory = term.getFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.s2Large(factory.makeList(term), domain);
    assert result.getChildCount() == 8;
    for(int i = 0; i < 8; i++) {
      assert result.getChildAt(i).equals(ExamplesTerms.sortTestShrink2_s2test_children[i]);
    }
  }

  @Test
  public void testS2StrictIteratorNonShrinkable() {
    System.out.println("test s2 Iterator non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s2Strict(term, domain), term.getFactory());
    assert result.getChildCount() == 0;
  }

  @Test
  public void testS2LargeIteratorNonShrinkable() {
    System.out.println("test s2 Iterator non shrinkable");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.nonS1_2_Shinkable;
    ATermFactory factory = term.getFactory();
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.s2Large(factory.makeList(term), domain);
    assert result.getChildCount() == 1;
    assert result.getChildAt(0).equals(ExamplesTerms.nonS1_2_Shinkable);
  }

  /**
   * Tests of shrink in depth.
   */
  @Test
  public void testDepthS1StrictList() {
    System.out.println("test s1 strict depth list");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s1WithDepthStrict(term, domain, 1);
    assert result.getLength() == 6;
    for(int i = 0; i < result.getLength(); i++) {
      assert result.getChildAt(i).equals(ExamplesTerms.sortTestShrink2_s1depth_children[i]);
    }
  }

  @Test
  public void testDepthS2StrictList() {
    System.out.println("test s2 strict depth list");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = Shrink.s2WithDepthStrict(term, domain, 1);
    assert result.getLength() == 2;
    for(int i = 0; i < result.getLength(); i++) {
      assert result.getChildAt(i).equals(ExamplesTerms.sortTestShrink2_s2depth_children[i]);
    }
  }

  @Test
  public void testDepth0S2StrictList() {
    System.out.println("test s2 strict depth 0 list");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result1 = Shrink.s2WithDepthStrict(term, domain, 0);
    ATermList result2 = ShrinkIterator.toATermList(ShrinkIterator.s2Strict(term, domain), term.getFactory());
    assert result1.equals(result2);
  }
  
  @Test
  public void testDepthS1StrictIterator() {
    System.out.println("test s1 strict depth iterator");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s1WithDepthStrict(term, domain, 1), term.getFactory());
    assert result.getLength() == 6;
    for(int i = 0; i < result.getLength(); i++) {
      assert result.getChildAt(result.getLength() - 1 - i).equals(ExamplesTerms.sortTestShrink2_s1depth_children[i]);
    }
  }

  @Test
  public void testDepthS2StrictIterator() {
    System.out.println("test s2 strict depth iterator");
    Buildable sort = Examples.sortTestShrink2;
    ATerm term = ExamplesTerms.sortTestShrink2_s2test;
    DomainInterpretation domain = new BuildableDomain(sort);
    ATermList result = ShrinkIterator.toATermList(ShrinkIterator.s2WithDepthStrict(term, domain, 1), term.getFactory());
    assert result.getLength() == 2;
    for(int i = 0; i < result.getLength(); i++) {
      assert result.getChildAt(result.getLength() - 1 - i).equals(ExamplesTerms.sortTestShrink2_s2depth_children[i]);
    }
  }
}
