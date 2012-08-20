/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package printParse;

import aterm.ATerm;
import examples.sort.types.Expr;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logic.model.BuildableDomain;
import logic.model.DomainInterpretation;
import logic.model.Interpretation;
import logic.model.PredicateInterpretation;
import logic.model.SignatureInterpretation;
import logic.system.types.CounterExample;
import logic.system.types.Formula;

/**
 *
 * @author hubert
 */
public class TestPrintParse {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    examples.Examples.init();
    Formula formula = examples.ExamplesFormula.f1;
    Map<String, PredicateInterpretation> map_pre = new HashMap<String, PredicateInterpretation>();
    Map<String, SignatureInterpretation> map_sig = new HashMap<String, SignatureInterpretation>();
    Map<String, DomainInterpretation> map_dom = new HashMap<String, DomainInterpretation>();

    PredicateInterpretation pred_int = new PredicateInterpretation() {
      @Override
      public boolean isTrue(List<ATerm> args) {
        Expr e;
        try {
          e = Expr.fromTerm(args.get(0));
        } catch (IllegalArgumentException ex) {
          System.out.println(ex);
          return false;
        }
        String s1 = e.toATerm().toString();
        String s2 = args.get(0).toString();
//        System.out.println(s1);
//        System.out.println(s2);
        return s1.equals(s2);
      }
    };

    DomainInterpretation dom_int = new BuildableDomain(examples.Examples.expr);

    map_pre.put("P", pred_int);
    map_dom.put("D", dom_int);

    Interpretation interp_int = new Interpretation(map_pre, map_sig, map_dom);

    CounterExample ce = interp_int.validateFormulaWithCE(formula, new HashMap<String, ATerm>(), 1000);
    System.out.println(ce);
  }
}
