/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic.model;

import java.util.Map;
import logic.system.*;

/**
 *
 * @author hubert
 */
public class Interpretation {

  private Map<Variable, Object> valuation;
  private Map<Predicate, PredicateInterpretation> interp_pre;
  private Map<Signature, SignatureInterpretation> interp_sig;

  public Interpretation(Map<Predicate, PredicateInterpretation> interp_pre,
          Map<Signature, SignatureInterpretation> interp_sig,
          Map<Variable, Object> valuation) {
    this.valuation = valuation;
    this.interp_pre = interp_pre;
    this.interp_sig = interp_sig;
  }

  private boolean evaluatePredicate(Predicate pre) {
    PredicateInterpretation interpretation = interp_pre.get(pre);
    Term[] args = pre.getArgs();
    Object[] argsEvaluations = new Object[args.length];
    for (int i = 0; i < argsEvaluations.length; i++) {
      argsEvaluations[i] = evaluateTerm(args[i]);
    }
    return interpretation.isTrue(argsEvaluations);
  }

  public Object evaluateTerm(Term term) { // prototype -> TOM
    if (term instanceof Variable) {

      return valuation.get((Variable) term);

    } else if (term instanceof Signature) {

      SignatureInterpretation interpretation = interp_sig.get((Signature) term);
      Term[] args = ((Signature) term).getArgs();
      Object[] argsEvaluations = new Object[args.length];
      for (int i = 0; i < argsEvaluations.length; i++) {
        argsEvaluations[i] = evaluateTerm(args[i]);
      }
      return interpretation.compute(argsEvaluations);
    }
    return null; //unreachable
  }

  public boolean evaluateFormula(Formula f) {
    if (f instanceof Predicate) {
      return evaluatePredicate((Predicate) f);
    } else if (f instanceof And) {
      throw new UnsupportedOperationException("");
    }
    return false; // unreachable
  }
}
