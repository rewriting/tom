package gen;

import system.types.*
import system.types.args.*
import system.types.formula.*
import system.types.term.*

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import logic.system.*;

public class Interpretation {
  
  %include{system/system.tom}
  private Map<String, Object> valuation;
  private Map<String, PredicateInterpretation> interp_pre;
  private Map<String, SignatureInterpretation> interp_sig;

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

  private List<Object> evaluateListTerm(Args args){
    %match(args){
      ListArgs() -> {return new LinkedList<Object>;}
      ListArgs(hd*, tl) -> {
        List<Object> l = evaluateListTerm(`hd*);
        l.add(evaluateTerm(`tl));
        return l;
      }
    }
  }

  public Object evaluateTerm(Term term) {
    %match(term){
      Var(name) -> {return valuation.get(name);}
      Sig(name, args) -> {
        SignatureInterpretation interpretation = interp_sig.get(`name);
        List<Object> argsEvaluations = evaluateListTerm(`args);
        return interpretation.compute(argsEvaluations);
      }
    }
    return null // unreachable
  }


  public boolean valideFormula(Formula f) {
    %match(f){
      Predicate(name, args) -> {
        PredicateInterpretation interpretation = interp_pre.get(`name);
        List<Object> argsEvaluations = evaluateListTerm(`args);
        return interpretation.isTrue(argsEvaluations);
      }
      And(f1, f2) -> {return valideFormula(`f1) && valideFormula(`f2);}
      Or(f1, f2) -> {return valideFormula(`f1) || valideFormula(`f2);}
      Imply(f1, f2) -> {return (!valideFormula(`f1)) || valideFormula(`f2);}
      Not(f) -> {return !valideFormula(`f);}
      True() -> {return true;}
      False() -> {return false;}
      Forall(var, f) -> {}
      Exists(var, f) -> {} // passer par la négation + forall

    }
    return false; // unreachable
  }

}
