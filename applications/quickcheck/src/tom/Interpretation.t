package gen;

import system.types.*;
import system.types.args.*;
import system.types.formula.*;
import system.types.term.*;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import logic.model.*;

import aterm.ATerm;

public class Interpretation {
  
  %include{system/system.tom}
  //private Map<String, Object> valuation;
  private Map<String, PredicateInterpretation> interp_pre;
  private Map<String, SignatureInterpretation> interp_sig;
  private Map<String, Domain> domain_map;

  public Interpretation(Map<String, PredicateInterpretation> interp_pre,
      Map<String, SignatureInterpretation> interp_sig, 
      Map<String, Domain> domain_map) {
    this.interp_pre = interp_pre;
    this.interp_sig = interp_sig;
    this.domain_map = domain_map;
  }

  private List<ATerm> evaluateListTerm(Args args, Map<String, ATerm> valuation){
    %match(args){
      ListArgs() -> {return new LinkedList<ATerm>();}
      ListArgs(hd*, tl) -> {
        List<ATerm> l = evaluateListTerm(`hd*, valuation);
        l.add(evaluateTerm(`tl, valuation));
        return l;
      }
    }
    return null; // unreachable
  }

  public ATerm evaluateTerm(Term term, Map<String, ATerm> valuation) {
    %match(term){
      Var(name) -> {return valuation.get(`name);}
      Sig(name, args) -> {
        SignatureInterpretation interpretation = interp_sig.get(`name);
        List<ATerm> argsEvaluations = evaluateListTerm(`args, valuation);
        return interpretation.compute(argsEvaluations);
      }
    }
    return null; // unreachable
  }


  public boolean valideFormula(Formula f, Map<String, ATerm> valuation) {
    %match(f){
      Predicate(name, args) -> {
        PredicateInterpretation interpretation = interp_pre.get(`name);
        List<ATerm> argsEvaluations = evaluateListTerm(`args, valuation);
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
