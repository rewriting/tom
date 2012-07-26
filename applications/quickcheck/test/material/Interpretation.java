package material;

import system.types.*;
import system.types.args.*;
import system.types.formula.*;
import system.types.term.*;
import system.types.counterexample.*;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import logic.model.*;

import aterm.ATerm;

public class Interpretation {

  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_boolean(boolean t1, boolean t2) {return  t1==t2 ;}private static boolean tom_is_sort_boolean(boolean t) {return  true ;} private static boolean tom_equal_term_ATerm(Object t1, Object t2) {return  t1 == t2 ;}private static boolean tom_is_sort_ATerm(Object t) {return  t instanceof aterm.ATerm ;}private static boolean tom_equal_term_ATermList(Object l1, Object l2) {return  l1==l2 ;}private static boolean tom_is_sort_ATermList(Object t) {return  t instanceof aterm.ATermList ;}private static boolean tom_equal_term_AFun(Object t1, Object t2) {return  t1 == t2 ;}private static boolean tom_is_sort_AFun(Object t) {return  t instanceof aterm.AFun ;} private static boolean tom_equal_term_CounterExample(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_CounterExample(Object t) {return  (t instanceof system.types.CounterExample) ;}private static boolean tom_equal_term_Formula(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Formula(Object t) {return  (t instanceof system.types.Formula) ;}private static boolean tom_equal_term_Args(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Args(Object t) {return  (t instanceof system.types.Args) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof system.types.Term) ;}private static  system.types.CounterExample  tom_make_CEAnd( system.types.CounterExample  t0) { return  system.types.counterexample.CEAnd.make(t0) ;}private static  system.types.CounterExample  tom_make_CEOr( system.types.CounterExample  t0,  system.types.CounterExample  t1) { return  system.types.counterexample.CEOr.make(t0, t1) ;}private static  system.types.CounterExample  tom_make_CEImply( system.types.CounterExample  t0) { return  system.types.counterexample.CEImply.make(t0) ;}private static  system.types.CounterExample  tom_make_CENot() { return  system.types.counterexample.CENot.make() ;}private static  system.types.CounterExample  tom_make_CEForall( String  t0,  aterm.ATerm  t1,  system.types.CounterExample  t2) { return  system.types.counterexample.CEForall.make(t0, t1, t2) ;}private static  system.types.CounterExample  tom_make_CEPredicate( String  t0,  system.types.Args  t1) { return  system.types.counterexample.CEPredicate.make(t0, t1) ;}private static boolean tom_is_fun_sym_NoCE( system.types.CounterExample  t) {return  (t instanceof system.types.counterexample.NoCE) ;}private static  system.types.CounterExample  tom_make_NoCE() { return  system.types.counterexample.NoCE.make() ;}private static boolean tom_is_fun_sym_Predicate( system.types.Formula  t) {return  (t instanceof system.types.formula.Predicate) ;}private static  String  tom_get_slot_Predicate_name( system.types.Formula  t) {return  t.getname() ;}private static  system.types.Args  tom_get_slot_Predicate_args( system.types.Formula  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_And( system.types.Formula  t) {return  (t instanceof system.types.formula.And) ;}private static  system.types.Formula  tom_get_slot_And_f1( system.types.Formula  t) {return  t.getf1() ;}private static  system.types.Formula  tom_get_slot_And_f2( system.types.Formula  t) {return  t.getf2() ;}private static boolean tom_is_fun_sym_Or( system.types.Formula  t) {return  (t instanceof system.types.formula.Or) ;}private static  system.types.Formula  tom_get_slot_Or_f1( system.types.Formula  t) {return  t.getf1() ;}private static  system.types.Formula  tom_get_slot_Or_f2( system.types.Formula  t) {return  t.getf2() ;}private static boolean tom_is_fun_sym_Imply( system.types.Formula  t) {return  (t instanceof system.types.formula.Imply) ;}private static  system.types.Formula  tom_get_slot_Imply_f1( system.types.Formula  t) {return  t.getf1() ;}private static  system.types.Formula  tom_get_slot_Imply_f2( system.types.Formula  t) {return  t.getf2() ;}private static boolean tom_is_fun_sym_Not( system.types.Formula  t) {return  (t instanceof system.types.formula.Not) ;}private static  system.types.Formula  tom_make_Not( system.types.Formula  t0) { return  system.types.formula.Not.make(t0) ;}private static  system.types.Formula  tom_get_slot_Not_f( system.types.Formula  t) {return  t.getf() ;}private static boolean tom_is_fun_sym_Forall( system.types.Formula  t) {return  (t instanceof system.types.formula.Forall) ;}private static  String  tom_get_slot_Forall_var( system.types.Formula  t) {return  t.getvar() ;}private static  String  tom_get_slot_Forall_domain( system.types.Formula  t) {return  t.getdomain() ;}private static  system.types.Formula  tom_get_slot_Forall_f( system.types.Formula  t) {return  t.getf() ;}private static boolean tom_is_fun_sym_Exists( system.types.Formula  t) {return  (t instanceof system.types.formula.Exists) ;}private static  String  tom_get_slot_Exists_var( system.types.Formula  t) {return  t.getvar() ;}private static  String  tom_get_slot_Exists_domain( system.types.Formula  t) {return  t.getdomain() ;}private static  system.types.Formula  tom_get_slot_Exists_f( system.types.Formula  t) {return  t.getf() ;}private static boolean tom_is_fun_sym_Var( system.types.Term  t) {return  (t instanceof system.types.term.Var) ;}private static  String  tom_get_slot_Var_name( system.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_Sig( system.types.Term  t) {return  (t instanceof system.types.term.Sig) ;}private static  String  tom_get_slot_Sig_name( system.types.Term  t) {return  t.getname() ;}private static  system.types.Args  tom_get_slot_Sig_args( system.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_ListArgs( system.types.Args  t) {return  ((t instanceof system.types.args.ConsListArgs) || (t instanceof system.types.args.EmptyListArgs)) ;}private static  system.types.Args  tom_empty_list_ListArgs() { return  system.types.args.EmptyListArgs.make() ;}private static  system.types.Args  tom_cons_list_ListArgs( system.types.Term  e,  system.types.Args  l) { return  system.types.args.ConsListArgs.make(e,l) ;}private static  system.types.Term  tom_get_head_ListArgs_Args( system.types.Args  l) {return  l.getHeadListArgs() ;}private static  system.types.Args  tom_get_tail_ListArgs_Args( system.types.Args  l) {return  l.getTailListArgs() ;}private static boolean tom_is_empty_ListArgs_Args( system.types.Args  l) {return  l.isEmptyListArgs() ;}   private static   system.types.Args  tom_append_list_ListArgs( system.types.Args l1,  system.types.Args  l2) {     if( l1.isEmptyListArgs() ) {       return l2;     } else if( l2.isEmptyListArgs() ) {       return l1;     } else if(  l1.getTailListArgs() .isEmptyListArgs() ) {       return  system.types.args.ConsListArgs.make( l1.getHeadListArgs() ,l2) ;     } else {       return  system.types.args.ConsListArgs.make( l1.getHeadListArgs() ,tom_append_list_ListArgs( l1.getTailListArgs() ,l2)) ;     }   }   private static   system.types.Args  tom_get_slice_ListArgs( system.types.Args  begin,  system.types.Args  end, system.types.Args  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyListArgs()  ||  (end==tom_empty_list_ListArgs()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  system.types.args.ConsListArgs.make( begin.getHeadListArgs() ,( system.types.Args )tom_get_slice_ListArgs( begin.getTailListArgs() ,end,tail)) ;   }    
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
    {{if (tom_is_sort_Args(((Object)args))) {if (tom_is_fun_sym_ListArgs((( system.types.Args )(( system.types.Args )((Object)args))))) {if (tom_is_empty_ListArgs_Args((( system.types.Args )((Object)args)))) {
return new LinkedList<ATerm>();}}}}{if (tom_is_sort_Args(((Object)args))) {if (tom_is_fun_sym_ListArgs((( system.types.Args )(( system.types.Args )((Object)args))))) { system.types.Args  tomMatch1__end__6=(( system.types.Args )((Object)args));do {{if (!(tom_is_empty_ListArgs_Args(tomMatch1__end__6))) {if (tom_is_empty_ListArgs_Args(tom_get_tail_ListArgs_Args(tomMatch1__end__6))) {

        List<ATerm> l = evaluateListTerm(tom_get_slice_ListArgs((( system.types.Args )((Object)args)),tomMatch1__end__6,tom_empty_list_ListArgs()), valuation);
        l.add(evaluateTerm(tom_get_head_ListArgs_Args(tomMatch1__end__6), valuation));
        return l;
      }}if (tom_is_empty_ListArgs_Args(tomMatch1__end__6)) {tomMatch1__end__6=(( system.types.Args )((Object)args));} else {tomMatch1__end__6=tom_get_tail_ListArgs_Args(tomMatch1__end__6);}}} while(!(tom_equal_term_Args(tomMatch1__end__6, (( system.types.Args )((Object)args)))));}}}}

    return null; // unreachable
  }

  private ATerm evaluateTerm(Term term, Map<String, ATerm> valuation) {
    {{if (tom_is_sort_Term(((Object)term))) {if (tom_is_sort_Term((( system.types.Term )((Object)term)))) {if (tom_is_fun_sym_Var((( system.types.Term )(( system.types.Term )((Object)term))))) { String  tom_name=tom_get_slot_Var_name((( system.types.Term )((Object)term)));

        ATerm res = valuation.get(tom_name);
        if(res == null){
          throw new UnsupportedOperationException("Variable " + tom_name+ " has no valuation.");
        }
        return res;}}}}{if (tom_is_sort_Term(((Object)term))) {if (tom_is_sort_Term((( system.types.Term )((Object)term)))) {if (tom_is_fun_sym_Sig((( system.types.Term )(( system.types.Term )((Object)term))))) { String  tom_name=tom_get_slot_Sig_name((( system.types.Term )((Object)term)));

        SignatureInterpretation interpretation = interp_sig.get(tom_name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Signature " + tom_name+ " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(tom_get_slot_Sig_args((( system.types.Term )((Object)term))), valuation);
        return interpretation.compute(argsEvaluations);
      }}}}}

    return null; // unreachable
  }


  private boolean validateForall(String varName, String domainName, Formula f, Map<String, ATerm> valuation){
    Domain domain = domain_map.get(domainName);
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
    Domain domain = domain_map.get(domainName);
    if(domain == null){
      throw new UnsupportedOperationException("Domain " + domainName + " has no interpretation.");
    }
    ATerm term = domain.chooseElement();
    valuation.put(varName, term);
    CounterExample cef = validateFormulaWithCE(f, valuation);
    {{if (tom_is_sort_CounterExample(((Object)cef))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef))))) {

        valuation.remove(varName);
        return tom_make_NoCE();
      }}}}{if (tom_is_sort_CounterExample(((Object)cef))) {

        valuation.remove(varName);
        return tom_make_CEForall(varName,term,cef);
      }}}

    return null; // unreachable
  }

  public boolean validateFormula(Formula f, Map<String, ATerm> valuation) {
    {{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Predicate((( system.types.Formula )(( system.types.Formula )((Object)f))))) { String  tom_name=tom_get_slot_Predicate_name((( system.types.Formula )((Object)f)));

        PredicateInterpretation interpretation = interp_pre.get(tom_name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Predicate " + tom_name+ " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(tom_get_slot_Predicate_args((( system.types.Formula )((Object)f))), valuation);
        return interpretation.isTrue(argsEvaluations);
      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_And((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return validateFormula(tom_get_slot_And_f1((( system.types.Formula )((Object)f))), valuation) && validateFormula(tom_get_slot_And_f2((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Or((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return validateFormula(tom_get_slot_Or_f1((( system.types.Formula )((Object)f))), valuation) || validateFormula(tom_get_slot_Or_f2((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Imply((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return (!validateFormula(tom_get_slot_Imply_f1((( system.types.Formula )((Object)f))), valuation)) || validateFormula(tom_get_slot_Imply_f2((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Not((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return !validateFormula(tom_get_slot_Not_f((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Forall((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return validateForall(tom_get_slot_Forall_var((( system.types.Formula )((Object)f))), tom_get_slot_Forall_domain((( system.types.Formula )((Object)f))), tom_get_slot_Forall_f((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Exists((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return !validateForall(tom_get_slot_Exists_var((( system.types.Formula )((Object)f))), tom_get_slot_Exists_domain((( system.types.Formula )((Object)f))), tom_make_Not(tom_get_slot_Exists_f((( system.types.Formula )((Object)f)))), valuation);}}}}}

    return false; // unreachable
  }

  public CounterExample validateFormulaWithCE(Formula f, Map<String, ATerm> valuation) {
    {{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Predicate((( system.types.Formula )(( system.types.Formula )((Object)f))))) { String  tom_name=tom_get_slot_Predicate_name((( system.types.Formula )((Object)f))); system.types.Args  tom_args=tom_get_slot_Predicate_args((( system.types.Formula )((Object)f)));

        PredicateInterpretation interpretation = interp_pre.get(tom_name);
        if(interpretation == null){
          throw new UnsupportedOperationException("Predicate " + tom_name+ " has no interpretation.");
        }
        List<ATerm> argsEvaluations = evaluateListTerm(tom_args, valuation);
        boolean isValide = interpretation.isTrue(argsEvaluations);
        if(isValide){
          return tom_make_NoCE();
        } else {
          return tom_make_CEPredicate(tom_name,tom_args);
        }
      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_And((( system.types.Formula )(( system.types.Formula )((Object)f))))) {

        CounterExample cef1 = validateFormulaWithCE(tom_get_slot_And_f1((( system.types.Formula )((Object)f))), valuation);
        {{if (tom_is_sort_CounterExample(((Object)cef1))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef1)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef1))))) {

            CounterExample cef2 = validateFormulaWithCE(tom_get_slot_And_f2((( system.types.Formula )((Object)f))), valuation);
            {{if (tom_is_sort_CounterExample(((Object)cef2))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef2)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef2))))) {
return tom_make_NoCE();}}}}{if (tom_is_sort_CounterExample(((Object)cef2))) {
return tom_make_CEAnd(cef2);}}}

          }}}}{if (tom_is_sort_CounterExample(((Object)cef1))) {
return tom_make_CEAnd(cef1);}}}

      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Or((( system.types.Formula )(( system.types.Formula )((Object)f))))) {

        CounterExample cef1 = validateFormulaWithCE(tom_get_slot_Or_f1((( system.types.Formula )((Object)f))), valuation);
        {{if (tom_is_sort_CounterExample(((Object)cef1))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef1)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef1))))) {
return tom_make_NoCE();}}}}{if (tom_is_sort_CounterExample(((Object)cef1))) {

            CounterExample cef2 = validateFormulaWithCE(tom_get_slot_Or_f2((( system.types.Formula )((Object)f))), valuation);
            {{if (tom_is_sort_CounterExample(((Object)cef2))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef2)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef2))))) {
return tom_make_NoCE();}}}}{if (tom_is_sort_CounterExample(((Object)cef2))) {
return tom_make_CEOr(cef1,cef2);}}}

          }}}

      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Imply((( system.types.Formula )(( system.types.Formula )((Object)f))))) {

        boolean valide = validateFormula(tom_get_slot_Imply_f1((( system.types.Formula )((Object)f))), valuation);
        if(valide){
          CounterExample cef2 = validateFormulaWithCE(tom_get_slot_Imply_f2((( system.types.Formula )((Object)f))), valuation);
          {{if (tom_is_sort_CounterExample(((Object)cef2))) {if (tom_is_sort_CounterExample((( system.types.CounterExample )((Object)cef2)))) {if (tom_is_fun_sym_NoCE((( system.types.CounterExample )(( system.types.CounterExample )((Object)cef2))))) {
return tom_make_NoCE();}}}}{if (tom_is_sort_CounterExample(((Object)cef2))) {
return tom_make_CEImply(cef2);}}}

        } else {
          return tom_make_NoCE();
        }
      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Not((( system.types.Formula )(( system.types.Formula )((Object)f))))) {

        boolean valide = validateFormula(tom_get_slot_Not_f((( system.types.Formula )((Object)f))), valuation);
        if(valide){
          return tom_make_CENot();
        } else {
          return tom_make_NoCE();
        }
      }}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Forall((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return validateForallWithCE(tom_get_slot_Forall_var((( system.types.Formula )((Object)f))), tom_get_slot_Forall_domain((( system.types.Formula )((Object)f))), tom_get_slot_Forall_f((( system.types.Formula )((Object)f))), valuation);}}}}{if (tom_is_sort_Formula(((Object)f))) {if (tom_is_sort_Formula((( system.types.Formula )((Object)f)))) {if (tom_is_fun_sym_Exists((( system.types.Formula )(( system.types.Formula )((Object)f))))) {
return validateForallWithCE(tom_get_slot_Exists_var((( system.types.Formula )((Object)f))), tom_get_slot_Exists_domain((( system.types.Formula )((Object)f))), tom_get_slot_Exists_f((( system.types.Formula )((Object)f))), valuation);}}}}}

    return null; // unreachable
  }

}
