package gen;

import system.types.*;
import system.types.args.*;
import system.types.formula.*;
import system.types.term.*;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import logic.model.*;

public class Interpretation {
  
  %include{system/system.tom}
  //private Map<String, Object> valuation;
  private Map<String, PredicateInterpretation> interp_pre;
  private Map<String, SignatureInterpretation> interp_sig;

  public Interpretation(Map<String, PredicateInterpretation> interp_pre,
      Map<String, SignatureInterpretation> interp_sig) {
    this.interp_pre = interp_pre;
    this.interp_sig = interp_sig;
  }

  private List<Object> evaluateListTerm(Args args, Map<String, Object> valuation){
    %match(args){
      ListArgs() -> {return new LinkedList<Object>();}
      ListArgs(hd*, tl) -> {
        List<Object> l = evaluateListTerm(`hd*, valuation);
        l.add(evaluateTerm(`tl, valuation));
        return l;
      }
    }
    return null; // unreachable
  }

  public Object evaluateTerm(Term term, Map<String, Object> valuation) {
    %match(term){
      Var(name) -> {return valuation.get(`name);}
      Sig(name, args) -> {
        SignatureInterpretation interpretation = interp_sig.get(`name);
        List<Object> argsEvaluations = evaluateListTerm(`args, valuation);
        return interpretation.compute(argsEvaluations);
      }
    }
    return null; // unreachable
  }


  public boolean valideFormula(Formula f, Map<String, Object> valuation) {
    %match(f){
      Predicate(name, args) -> {
        PredicateInterpretation interpretation = interp_pre.get(`name);
        List<Object> argsEvaluations = evaluateListTerm(`args, valuation);
        return interpretation.isTrue(argsEvaluations);
      }
      And(f1, f2) -> {return valideFormula(`f1, valuation) && valideFormula(`f2, valuation);}
      Or(f1, f2) -> {return valideFormula(`f1, valuation) || valideFormula(`f2, valuation);}
      Imply(f1, f2) -> {return (!valideFormula(`f1, valuation)) || valideFormula(`f2, valuation);}
      Not(f1) -> {return !valideFormula(`f1, valuation);}
      True() -> {return true;}
      False() -> {return false;}
      Forall(varname, domain, f1) -> {;}
      Exists(varname, domain, f1) -> {;} // passer par la négation + forall
    }
    return false; // unreachable
  }

}
