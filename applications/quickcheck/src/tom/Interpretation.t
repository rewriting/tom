package gen;

import aterm.ATerm;
import aterm.ATermList;
import aterm.pure.PureFactory;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jjtraveler.Visitable;
import logic.model.DomainInterpretation;
import logic.model.PredicateInterpretation;
import logic.model.SignatureInterpretation;
import system.types.Args;
import system.types.CounterExample;
import system.types.Formula;
import system.types.Term;

public class Interpretation {

  %include{ ../system/system.tom}
  //private Map<String, Object> valuation;
  private Map<String, PredicateInterpretation> interp_pre;
  private Map<String, SignatureInterpretation> interp_sig;
  private Map<String, DomainInterpretation> domain_map;

  public Interpretation(Map<String, PredicateInterpretation> interp_pre,
      Map<String, SignatureInterpretation> interp_sig, 
      Map<String, DomainInterpretation> domain_map) {
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

  private ATerm evaluateTerm(Term term, Map<String, ATerm> valuation) {
    %match(term){
      Var(name) -> {
        ATerm res = valuation.get(`name);
        if(res == null){
          throw new UnsupportedOperationException("Variable " + `name + " has no valuation.");
        }
        return res;}
      Sig(name, args) -> {
        SignatureInterpretation interpretation = interp_sig.get(`name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Signature " + `name + " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(`args, valuation);
        return interpretation.compute(argsEvaluations);
      }
    }
    return null; // unreachable
  }
 
  /* =================================================================== */
  /*                            ValidateForall                           */
  /* =================================================================== */


  private boolean validateForall(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    DomainInterpretation domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    ATerm term = domain.chooseElement();
    valuation.put(varName, term);
    boolean res = validateFormula(f, valuation);
    valuation.remove(varName);
    return res;
  }

  private CounterExample validateForallWithCE(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    DomainInterpretation domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    ATerm term = domain.chooseElement();
    valuation.put(varName, term);
    CounterExample cef = validateFormulaWithCE(f, valuation);
    %match(cef){
      NoCE() -> {
        valuation.remove(varName);
        return `NoCE();
      }
      _ -> {
        valuation.remove(varName);
        return `CEForall(varName, term, cef);
      }
    }
    return null; // unreachable
  }

  private ATermList filterList(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation) {
    if (list.isEmpty()) {
      return list;
    }
    ATerm head = list.getFirst();
    valuation.put(varName, head);
    boolean res = validateFormula(f, valuation);
    valuation.remove(varName);
    if (res) {
      return filterList(varName, list.getNext(), domain, f, valuation);
    } else {
      return filterList(varName, list.getNext(), domain, f, valuation).insert(head);
    }
  }

  private ATermList s1(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation){
    ATermList res = filterList(varName, Shrink.s1(list, domain), domain, f, valuation);
    if (res.equals(list)) {
      return list;
    } else {
      return s1(varName, res, domain, f, valuation);
    }
  }

  private ATermList s2(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation){
    ATermList res = filterList(varName, logic.model.Shrink.s2(list, domain), domain, f, valuation);
    if (res.equals(list)) {
      return list;
    } else {
      return s2(varName, res, domain, f, valuation);
    }
  }

  private int sizeATerm(Visitable term){
    int n = term.getChildCount();
    int res = 0;
    for (int i = 0; i < n; i++) {
      res += sizeATerm(term.getChildAt(i));
    }
    return res + 1;
  }

  private ATerm minATerm(ATermList list){
    ATerm term = null;
    ATermList current = list;
    int sizeMin = Integer.MAX_VALUE;
    while (current.isEmpty()) {
      int size = sizeATerm(current.getFirst());
      if (size < sizeMin) {
        term = current.getFirst();
        sizeMin = size;
      }
      current = current.getNext();
    }
    return term;
  }

  private ATerm shrink(String varName, ATerm term, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation){
    PureFactory factory = new PureFactory();
    ATermList l0 = factory.makeList(term);
    ATermList l1 = s1(varName, l0, domain, f, valuation);
    ATermList l2 = s2(varName, l1, domain, f, valuation);
    return minATerm(l2);
  }


  private CounterExample validateForallWithShrunkCE(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    DomainInterpretation domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    ATerm term = domain.chooseElement();
    valuation.put(varName, term);
    boolean res = validateFormula(f, valuation);
    valuation.remove(varName);
    if (res) {
      return `NoCE();
    } else {
      ATerm shrunkTerm = shrink(varName, term, domain, f, valuation);
      valuation.put(varName, shrunkTerm);
      CounterExample cef = validateFormulaWithCE(f, valuation);
      valuation.remove(varName);
      return `CEForall(varName, shrunkTerm, cef);
    }
  }

  /* =================================================================== */
  /*                            ValidateFormula                          */
  /* =================================================================== */

  public boolean validateFormula(Formula f, Map<String, ATerm> valuation) {
    %match(f){
      Predicate(name, args) -> {
        PredicateInterpretation interpretation = interp_pre.get(`name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Predicate " + `name + " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(`args, valuation);
        return interpretation.isTrue(argsEvaluations);
      }
      And(f1, f2) -> {return validateFormula(`f1, valuation) && validateFormula(`f2, valuation);}
      Or(f1, f2) -> {return validateFormula(`f1, valuation) || validateFormula(`f2, valuation);}
      Imply(f1, f2) -> {return (!validateFormula(`f1, valuation)) || validateFormula(`f2, valuation);}
      Not(f1) -> {return !validateFormula(`f1, valuation);}
      Forall(varname, domain, f1) -> {return validateForall(`varname, `domain, `f1, valuation);}
      Exists(varname, domain, f1) -> {return !validateForall(`varname, `domain, `Not(f1), valuation);}
    }
    return false; // unreachable
  }

  public CounterExample validateFormulaWithCE(Formula f, Map<String, ATerm> valuation) {
    %match(f){
      Predicate(name, args) -> {
        PredicateInterpretation interpretation = interp_pre.get(`name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Predicate " + `name + " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(`args, valuation);
        boolean isValide = interpretation.isTrue(argsEvaluations);
        if(isValide){
          return `NoCE();
        } else {
          return `CEPredicate(name, args);
        }
      }
      And(f1, f2) -> {
        CounterExample cef1 = validateFormulaWithCE(`f1, valuation);
        %match(cef1){
          NoCE() -> {
            CounterExample cef2 = validateFormulaWithCE(`f2, valuation);
            %match(cef2){
              NoCE() -> {return `NoCE();}
              _ -> {return `CEAnd(cef2);}
            }
          }
          _ -> {return `CEAnd(cef1);}
        }
      }
      Or(f1, f2) -> {
        CounterExample cef1 = validateFormulaWithCE(`f1, valuation);
        %match(cef1){
          NoCE() -> {return `NoCE();}
          _ -> {
            CounterExample cef2 = validateFormulaWithCE(`f2, valuation);
            %match(cef2){
              NoCE() -> {return `NoCE();}
              _ -> {return `CEOr(cef1, cef2);}
            }
          }
        }
      }
      Imply(f1, f2) -> {
        boolean valide = validateFormula(`f1, valuation);
        if(valide){
          CounterExample cef2 = validateFormulaWithCE(`f2, valuation);
          %match(cef2){
            NoCE() -> {return `NoCE();}
            _ -> {return `CEImply(cef2);}
          }
        } else {
          return `NoCE();
        }
      }
      Not(f1) -> {
        boolean valide = validateFormula(`f1, valuation);
        if(valide){
          return `CENot();
        } else {
          return `NoCE();
        }
      }
      Forall(varname, domain, f1) -> {return validateForallWithCE(`varname, `domain, `f1, valuation);}
      Exists(varname, domain, f1) -> {throw new UnsupportedOperationException("Exists logic is not yet implemented");}
    }
    return null; // unreachable
  }

}
