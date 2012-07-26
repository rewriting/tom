/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import aterm.ATerm;
import aterm.ATermInt;
import aterm.pure.PureFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.model.Domain;
import logic.model.PredicateInterpretation;
import logic.model.SignatureInterpretation;
import material.ExamplesFormula; // generated + moved
import material.Interpretation; // generated + moved
import org.junit.*;
import system.types.Formula; // generated

/**
 *
 * @author hubert
 */
public class LogicTest {

  private Formula formula = ExamplesFormula.f1;
  private Interpretation interp_int;
  private PredicateInterpretation P_int;
  private Domain D_int;

  public LogicTest() {
    
    P_int = new PredicateInterpretation() {

      @Override
      public boolean isTrue(List<ATerm> args) {
        ATermInt n = (ATermInt) args.get(0);
        return (n.getInt() % 2) == 0;
      }
    };

    D_int = new Domain() {

      @Override
      public ATerm chooseElement() {
        PureFactory factory = new PureFactory();
        return factory.makeInt(6537);
      }
    };

    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, Domain> map_dom = new HashMap<String, Domain>();

    map_pre.put("P", P_int);
    map_dom.put("D", D_int);

    interp_int = new Interpretation(map_pre, map_sig, map_dom);
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
    System.out.println(interp_int.validateFormulaWithCE(formula, new HashMap<String, ATerm>()));
    assert true;
  }
}
