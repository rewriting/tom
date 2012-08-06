/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import aterm.ATerm;
import aterm.ATermInt;
import aterm.pure.PureFactory;
import examples.Examples;
import examples.TestGen;
import examples.sort.types.IntList;
import java.util.*;
import logic.model.*;
import logic.system.types.Formula;
import org.junit.*;

/**
 *
 * @author hubert
 */
public class LogicTest {

  private Formula formula = ExamplesFormula.f1;
  private Formula even_odd = ExamplesFormula.odd_even;
  // Interger interpretation
  private PredicateInterpretation ODD;
  private PredicateInterpretation EVEN;
  private SignatureInterpretation succ;
  private DomainInterpretation Integer;
  private DomainInterpretation EvenInteger;
  // List interpretation
  private PredicateInterpretation REV_REV;
  private DomainInterpretation List;

  public LogicTest() {

    EVEN = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        ATermInt n = (ATermInt) args.get(0);
        return (n.getInt() % 2) == 0;
      }
    };

    ODD = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        ATermInt n = (ATermInt) args.get(0);
        return (n.getInt() % 2) == 1;
      }
    };

    succ = new SignatureInterpretation() {

      @Override
      public ATerm compute(List<ATerm> args) {
        PureFactory factory = new PureFactory();
        return factory.makeInt(((ATermInt) (args.get(0))).getInt() + 1);
      }
    };


    REV_REV = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        IntList list = IntList.fromTerm(args.get(0));
        return list.equals(TestGen.reverse(TestGen.reverse(list)));
      }
    };

    EvenInteger = new DomainInterpretation() {

      @Override
      public ATerm chooseElement() {
        PureFactory factory = new PureFactory();
        return factory.makeInt(((int) (Math.random() * 1000)) * 2);
      }

      @Override
      public boolean includes(ATerm term) {
        if (!(term instanceof ATermInt)) {
          return false;
        }
        return ((ATermInt) term).getInt() % 2 == 0;
      }

      @Override
      public DomainInterpretation[] getDepsDomains() {
        return new DomainInterpretation[0];
      }

      @Override
      public Iterator<ATerm> lighten(ATerm term) {
        List<ATerm> res = new ArrayList<ATerm>();
        res.add(term);
        return res.iterator();
      }
    };

    Integer = new DomainInterpretation() {

      @Override
      public ATerm chooseElement() {
        PureFactory factory = new PureFactory();
        return factory.makeInt((int) (Math.random() * 1000));
      }

      @Override
      public boolean includes(ATerm term) {
        return term instanceof ATermInt;
      }

      @Override
      public DomainInterpretation[] getDepsDomains() {
        return new DomainInterpretation[0];
      }

      @Override
      public Iterator<ATerm> lighten(ATerm term) {
        List<ATerm> res = new ArrayList<ATerm>();
        res.add(term);
        return res.iterator();
      }
    };

    List = new BuildableDomain(Examples.listABC);
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
    Examples.init();
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    System.out.println("---------------------------");
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testInterpInt() {

    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom = new HashMap<String, DomainInterpretation>();

    map_pre.put("P", EVEN);
    map_dom.put("D", EvenInteger);

    Interpretation interp_int = new Interpretation(map_pre, map_sig, map_dom);

    assert interp_int.validateFormula(formula, new HashMap<String, ATerm>());
  }

  @Test
  public void testInterpIntCE() {

    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom = new HashMap<String, DomainInterpretation>();

    map_pre.put("P", ODD);

    DomainInterpretation EvenInteger_cheat = new DomainInterpretation() {

      @Override
      public ATerm chooseElement() {
        PureFactory factory = new PureFactory();
        return factory.makeInt(4);
      }

      @Override
      public boolean includes(ATerm term) {
        return term instanceof ATermInt && ((ATermInt) term).getInt() == 4;
      }

      @Override
      public DomainInterpretation[] getDepsDomains() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      @Override
      public Iterator<ATerm> lighten(ATerm term) {
        List<ATerm> res = new ArrayList<ATerm>();
        res.add(term);
        return res.iterator();
      }
    };

    map_dom.put("D", EvenInteger_cheat);

    Interpretation interp_int = new Interpretation(map_pre, map_sig, map_dom);

    String res = interp_int.validateFormulaWithCE(formula, new HashMap<String, ATerm>()).toString();
    assert res.equals("CEForall(\"x\",4,CEPredicate(\"P\",ListArgs(Var(\"x\"))))");
  }

  @Test
  public void testInterpListCE() {

    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom = new HashMap<String, DomainInterpretation>();

    map_pre.put("P", REV_REV);
    map_dom.put("D", List);

    Interpretation interp_list = new Interpretation(map_pre, map_sig, map_dom);

    String res = interp_list.validateFormulaWithCE(formula, new HashMap<String, ATerm>()).toString();
    assert res.equals("NoCE()");
  }

  @Test
  public void testEVEN_ODD_CE() {

    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom = new HashMap<String, DomainInterpretation>();

    map_pre.put("EVEN", EVEN);
    map_pre.put("ODD", ODD);

    map_dom.put("int", Integer);

    map_sig.put("succ", succ);

    Interpretation interp_list = new Interpretation(map_pre, map_sig, map_dom);

    String res = interp_list.validateFormulaWithCE(even_odd, new HashMap<String, ATerm>()).toString();
    assert res.equals("NoCE()");
  }
}
