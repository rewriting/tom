package logic.model;

import aterm.ATerm;
import aterm.ATermFactory;
import aterm.ATermIterator;
import aterm.ATermIteratorFromList;
import aterm.ATermList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import jjtraveler.Visitable;
import logic.system.types.Args;
import logic.system.types.CounterExample;
import logic.system.types.Formula;
import logic.system.types.Term;

public class Interpretation {

  %include{../system/system.tom}
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
    for(int i = 0; i<20; i++){
      ATerm term = domain.chooseElement(i);
      valuation.put(varName, term);
      boolean res = validateFormula(f, valuation);
      valuation.remove(varName);
      if(!res){
        return false;
      }
    }
    return true;
  }

  private CounterExample validateForallWithCE(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    DomainInterpretation domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    for(int i = 0; i<20; i++){
      ATerm term = domain.chooseElement(i);
      valuation.put(varName, term);
      CounterExample cef = validateFormulaWithCE(f, valuation);
      valuation.remove(varName);
      %match(cef){
        !NoCE() -> {return `CEForall(varName, term, cef);}
      }
    }
    return `NoCE();
  }

  /* =================================================================== */
  /*                                 Shrink                              */
  /* =================================================================== */


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

  private class FilterList_class extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="filterList">

    private ATerm current = null;
    private ATermIterator list;
    //
    private String varName;
    private DomainInterpretation domain;
    private Formula f;
    private Map<String, ATerm> valuation;
    
    @Override
    public FilterList_class clone(){
      FilterList_class res = (FilterList_class) super.clone();
      res.list = this.list.clone();
      return res;
    }

    public FilterList_class(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation) {
      this.varName = varName;
      this.list = list;
      this.domain = domain;
      this.f = f;
      this.valuation = valuation;
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(!list.hasNext()) {
        return false;
      }
      ATerm head = list.next();
      valuation.put(varName, head);
      boolean isValide = validateFormula(f, valuation);
      valuation.remove(varName);
      if(isValide) {
        return hasNext();
      } else {
        current = head;
        return true;
      }
    }

    @Override
    public ATerm next() {
      if(current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if(hasNext()) {
        System.out.println("WARNING : the use of the methode next() is not preceded by hasNext().");
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }
    //</editor-fold>
  }

  private ATermIterator filterList(final String varName, final ATermIterator list, DomainInterpretation domain, final Formula f, final Map<String, ATerm> valuation) {
    return new FilterList_class(varName, list, domain, f, valuation);
  }

  private ATermList s1_aux(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth){
    if(list.isEmpty()) {
      return list;
    }
    ATerm head = list.getFirst();
    ATermList shrunkHead = filterList(varName, Shrink.s1WithDepthStrict(head, domain, depth), domain, f, valuation);
    if(shrunkHead.isEmpty()){
      return s1_aux(varName, list.getNext(), domain, f, valuation, depth).insert(head);
    } else {
      return shrunkHead.concat(s1_aux(varName, list.getNext(), domain, f, valuation, depth));
    }
  }

  private class S1_aux_class extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="s1_aux">

    private ATerm current = null;
    private ATermIterator shrunkHead = null;
    //
    private ATermIterator list;
    private String varName;
    private DomainInterpretation domain;
    private int depth;
    private Formula f;
    private Map<String, ATerm> valuation;
    
    @Override
    public ATermIterator clone(){
      return new S1_aux_class(list.clone(), varName, domain, depth, f, valuation);
//      S1_aux_class res = (S1_aux_class) super.clone();
//      if(shrunkHead == null) {
//        res.shrunkHead = null;
//      } else {
//        res.shrunkHead = this.shrunkHead.clone();
//      }
//      res.list = this.list.clone();
//      return res;
    }
    
    public S1_aux_class(ATermIterator list, String varName, DomainInterpretation domain, int depth, Formula f, Map<String, ATerm> valuation){
      this.list = list;
      this.valuation = valuation;
      this.varName = varName;
      this.domain = domain;
      this.depth = depth;
      this.f = f;
      this.valuation = valuation;
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(shrunkHead == null) {
        if(!list.hasNext()) {
          return false;
        }
        ATerm head = list.next();
        shrunkHead = filterList(varName, ShrinkIterator.s1WithDepthStrict(head, domain, depth), domain, f, valuation);
        if(!shrunkHead.hasNext()) {
          List<ATerm> tmp = new LinkedList<ATerm>();
          tmp.add(head);
          shrunkHead = new ATermIteratorFromList(tmp);
        }
      }
      if(!shrunkHead.hasNext()) {
        shrunkHead = null;
        return hasNext();
      }
      current = shrunkHead.next();
      return true;
    }

    @Override
    public ATerm next() {
      if(current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if(hasNext()) {
        System.out.println("WARNING : the use of the methode next() is not preceded by hasNext().");
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }
    //</editor-fold>
  }

  private ATermIterator s1_aux(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth) {
    return new S1_aux_class(list, varName, domain, depth, f, valuation);
  }

  private ATermList s2_aux(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth){
    if(list.isEmpty()) {
      return list;
    }
    ATerm head = list.getFirst();
    ATermList shrunkHead = filterList(varName, Shrink.s2WithDepthStrict(head, domain, depth), domain, f, valuation);
    if(shrunkHead.isEmpty()){
      return s2_aux(varName, list.getNext(), domain, f, valuation, depth).insert(head);
    } else {
      return shrunkHead.concat(s2_aux(varName, list.getNext(), domain, f, valuation, depth));
    }
  }

  private class S2_aux_class extends ATermIterator {
    //<editor-fold defaultstate="collapsed" desc="s2_aux">

    private ATerm current = null;
    private ATermIterator shrunkHead = null;
    //
    private ATermIterator list;
    private String varName;
    private DomainInterpretation domain;
    private int depth;
    private Formula f;
    private Map<String, ATerm> valuation;
    
    @Override
    public ATermIterator clone(){
      return new S2_aux_class(list.clone(), varName, domain, depth, f, valuation);
//      S2_aux_class res = (S2_aux_class) super.clone();
//      if(shrunkHead == null) {
//        res.shrunkHead = null;
//      } else {
//        res.shrunkHead = this.shrunkHead.clone();
//      }
//      res.list = this.list.clone();
//      return res;
    }
    
    public S2_aux_class(ATermIterator list, String varName, DomainInterpretation domain, int depth, Formula f, Map<String, ATerm> valuation){
      this.list = list;
      this.valuation = valuation;
      this.varName = varName;
      this.domain = domain;
      this.depth = depth;
      this.f = f;
      this.valuation = valuation;
    }

    @Override
    public boolean hasNext() {
      if(current != null) {
        return true;
      }
      if(shrunkHead == null) {
        if(!list.hasNext()) {
          return false;
        }
        ATerm head = list.next();
        shrunkHead = filterList(varName, ShrinkIterator.s2WithDepthStrict(head, domain, depth), domain, f, valuation);
        if(!shrunkHead.hasNext()) {
          List<ATerm> tmp = new LinkedList<ATerm>();
          tmp.add(head);
          shrunkHead = new ATermIteratorFromList(tmp);
        }
      }
      if(!shrunkHead.hasNext()) {
        shrunkHead = null;
        return hasNext();
      }
      current = shrunkHead.next();
      return true;
    }

    @Override
    public ATerm next() {
      if(current != null) {
        ATerm res = current;
        current = null;
        return res;
      } else if(hasNext()) {
        System.out.println("WARNING : the use of the methode next() is not preceded by hasNext().");
        ATerm res = current;
        current = null;
        return res;
      } else {
        throw new NoSuchElementException();
      }
    }
    //</editor-fold>
  }

  private ATermIterator s2_aux(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth) {
    return new S2_aux_class(list, varName, domain, depth, f, valuation);
  }

  private ATermList s1(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth){
    ATermList res = s1_aux(varName, list, domain, f, valuation, depth);
    if (res.equals(list)) {
      return list;
    } else {
      return s1(varName, res, domain, f, valuation, depth);
    }
  }

  private ATermIterator s1(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth) {
    ATermIterator res = s1_aux(varName, list, domain, f, valuation, depth);
    if(res.equals(list)) {
      return list;
    } else {
      return s1(varName, res, domain, f, valuation, depth);
    }
  }

  private ATermList s2(String varName, ATermList list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth){
    ATermList res = s2_aux(varName, list, domain, f, valuation, depth);
    return res;
  }

  private ATermIterator s2(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation, int depth) {
    ATermIterator res = s2_aux(varName, list, domain, f, valuation, depth);
    return res;
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
    while (!current.isEmpty()) {
      int size = sizeATerm(current.getFirst());
      if (size < sizeMin) {
        term = current.getFirst();
        sizeMin = size;
      }
      current = current.getNext();
    }
    return term;
  }

  private ATerm minATerm(ATermIterator list) {
    ATerm res = null;
    int sizeMin = Integer.MAX_VALUE;
    while(list.hasNext()){
      ATerm term = list.next();
      int size = sizeATerm(term);
      if(size < sizeMin) {
        res = term;
        sizeMin = size;
      }
    }
    return res;
  }

  private int depth(Visitable term){
    int res = -1;
    int n = term.getChildCount();
    for (int i = 0; i < n; i++) {
      res = Math.max(res, depth(term.getChildAt(i)));
    }
    return res + 1;
  }

  private ATerm shrink(String varName, ATerm term, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation) {
//    ATermFactory factory = term.getFactory();
//    ATermList l0 = factory.makeList(term);
    List<ATerm> list = new LinkedList<ATerm>();
    list.add(term);
    ATermIterator l0 = new ATermIteratorFromList(list);
    int depth = depth(term);
    for(int i = 0; i <= depth; i++) {
      l0 = s1(varName, l0, domain, f, valuation, i);
      l0 = s2(varName, l0, domain, f, valuation, i);
    }
    return minATerm(l0);
  }

  private CounterExample validateForallWithShrunkCE(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    DomainInterpretation domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    for(int i = 0; i<20; i++){
      ATerm term = domain.chooseElement(i);
      valuation.put(varName, term);
      boolean res = validateFormula(f, valuation);
      valuation.remove(varName);
      if(!res){
        ATerm shrunkTerm = shrink(varName, term, domain, f, valuation);
        valuation.put(varName, shrunkTerm);
        CounterExample cef = validateFormulaWithCE(f, valuation);
        valuation.remove(varName);
        return `CEForall(varName, shrunkTerm, cef);
      }
    }
    return `NoCE();
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
      Forall(varname, domain, f1) -> {return validateForallWithShrunkCE(`varname, `domain, `f1, valuation);}
      Exists(varname, domain, f1) -> {throw new UnsupportedOperationException("Exists logic is not yet implemented");}
    }
    return null; // unreachable
  }


  /* =================================================================== */
  /*                            Test                                     */
  /* =================================================================== */

  ATermIterator getFilter(String varName, ATermIterator list, DomainInterpretation domain, Formula f, Map<String, ATerm> valuation){
    return new FilterList_class(varName, list, domain, f, valuation);
  }
  
  ATermIterator getS1(ATermIterator list, String varName, DomainInterpretation domain, int depth, Formula f, Map<String, ATerm> valuation){
    return new S1_aux_class(list, varName, domain, depth, f, valuation);
  }
  
  ATermIterator getS2(ATermIterator list, String varName, DomainInterpretation domain, int depth, Formula f, Map<String, ATerm> valuation){
    return new S2_aux_class(list, varName, domain, depth, f, valuation);
  }
}
