/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import aterm.ATerm;
import aterm.ATermInt;
import aterm.pure.PureFactory;
import definitions.Scope;
import definitions.SortGom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.model.DomainInterpretation;
import logic.model.PredicateInterpretation;
import logic.model.SignatureInterpretation;
import gen.ExamplesFormula; // generated
import gen.Interpretation; // generated
import gen.TestGen;
import org.junit.*;
import sort.types.IntList;
import sort.types.Leaf;
import sort.types.intlist.consList;
import sort.types.intlist.nill;
import system.types.Formula; // generated

/**
 *
 * @author hubert
 */
public class LogicTest {

  private Formula formula = ExamplesFormula.f1;
  // Interger interpretation
  private Interpretation interp_int;
  private PredicateInterpretation P_int;
  private DomainInterpretation D_int;
  // List interpretation
  private Interpretation interp_list;
  private PredicateInterpretation P_list;
  private DomainInterpretation D_list;

  public LogicTest() {

    P_int = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        ATermInt n = (ATermInt) args.get(0);
        return (n.getInt() % 2) == 0;
      }
    };

    P_list = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        IntList list = IntList.fromTerm(args.get(0));
        return list.equals(TestGen.reverse(TestGen.reverse(list)));
      }
    };

    D_int = new DomainInterpretation() {

      @Override
      public ATerm chooseElement() {
        PureFactory factory = new PureFactory();
        return factory.makeInt((int) (Math.random() * 1000));
      }
    };
    D_list = new DomainInterpretation() {

      @Override
      public ATerm chooseElement() {

        Scope scope = new Scope();

        SortGom leaf = new SortGom(scope, Leaf.class);
        SortGom list = new SortGom(scope, IntList.class);
        list.addConstructor(nill.class);
        list.addConstructor(consList.class);
        leaf.addConstructor("a");
        leaf.addConstructor("b");
        leaf.addConstructor("c");

        scope.setDependances();
        
        ATerm res = list.generate(20);
        return res;
      }
    };

    Map<String, PredicateInterpretation> map_pre_int = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig_int = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom_int = new HashMap<String, DomainInterpretation>();
    
    Map<String, PredicateInterpretation> map_pre_list = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig_list = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom_list = new HashMap<String, DomainInterpretation>();

    map_pre_int.put("P", P_int);
    map_dom_int.put("D", D_int);
    
    map_pre_list.put("P", P_list);
    map_dom_list.put("D", D_list);

    interp_int = new Interpretation(map_pre_int, map_sig_int, map_dom_int);
    interp_list = new Interpretation(map_pre_list, map_sig_list, map_dom_list);
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

  @Test
  public void testInterpInt() {
    assert interp_int.validateFormula(formula, new HashMap<String, ATerm>());
  }

  @Test
  public void testInterpIntCE() {
    String res = interp_int.validateFormulaWithCE(formula, new HashMap<String, ATerm>()).toString();
    System.out.println(res);
    assert res.equals("NoCE()");
  }
  
  @Test
  public void testInterpListCE() {
    String res = interp_list.validateFormulaWithCE(formula, new HashMap<String, ATerm>()).toString();
    assert res.equals("NoCE()");
  }
}
