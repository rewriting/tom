package sa;

import sa.rule.types.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import tom.library.sl.*;
/*import aterm.*;
import aterm.pure.*;*/
import com.google.common.collect.HashMultiset;

import static sa.Tools.Var;
import static sa.Tools.Anti;
import static sa.Tools.At;
import static sa.Tools.Bottom;
import static sa.Tools.BottomList;
import static sa.Tools.True;
import static sa.Tools.False;
import static sa.Tools.And;
import static sa.Tools.Eq;
import static sa.Tools.Appl;
import static sa.Tools.Rule;
import static sa.Tools.Nil;
import static sa.Tools.Cons;
import static sa.Tools._appl;

public class Compiler {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_StratDecl( sa.rule.types.StratDecl  t) {return  (t instanceof sa.rule.types.stratdecl.StratDecl) ;}private static  String  tom_get_slot_StratDecl_Name( sa.rule.types.StratDecl  t) {return  t.getName() ;}private static  sa.rule.types.ParamList  tom_get_slot_StratDecl_ParamList( sa.rule.types.StratDecl  t) {return  t.getParamList() ;}private static  sa.rule.types.Strat  tom_get_slot_StratDecl_Body( sa.rule.types.StratDecl  t) {return  t.getBody() ;}private static boolean tom_is_fun_sym_StratName( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratName) ;}private static  sa.rule.types.Strat  tom_make_StratName( String  t0) { return  sa.rule.types.strat.StratName.make(t0) ;}private static  String  tom_get_slot_StratName_name( sa.rule.types.Strat  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_StratSequence( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratSequence) ;}private static  sa.rule.types.Strat  tom_get_slot_StratSequence_s1( sa.rule.types.Strat  t) {return  t.gets1() ;}private static  sa.rule.types.Strat  tom_get_slot_StratSequence_s2( sa.rule.types.Strat  t) {return  t.gets2() ;}private static boolean tom_is_fun_sym_StratChoice( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratChoice) ;}private static  sa.rule.types.Strat  tom_get_slot_StratChoice_s1( sa.rule.types.Strat  t) {return  t.gets1() ;}private static  sa.rule.types.Strat  tom_get_slot_StratChoice_s2( sa.rule.types.Strat  t) {return  t.gets2() ;}private static boolean tom_is_fun_sym_StratIdentity( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratIdentity) ;}private static  sa.rule.types.Strat  tom_make_StratIdentity() { return  sa.rule.types.strat.StratIdentity.make() ;}private static boolean tom_is_fun_sym_StratFail( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratFail) ;}private static boolean tom_is_fun_sym_StratAll( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratAll) ;}private static  sa.rule.types.Strat  tom_get_slot_StratAll_s( sa.rule.types.Strat  t) {return  t.gets() ;}private static boolean tom_is_fun_sym_StratOne( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratOne) ;}private static  sa.rule.types.Strat  tom_get_slot_StratOne_s( sa.rule.types.Strat  t) {return  t.gets() ;}private static boolean tom_is_fun_sym_StratTrs( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratTrs) ;}private static  sa.rule.types.Trs  tom_get_slot_StratTrs_trs( sa.rule.types.Strat  t) {return  t.gettrs() ;}private static boolean tom_is_fun_sym_StratMu( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratMu) ;}private static  String  tom_get_slot_StratMu_name( sa.rule.types.Strat  t) {return  t.getname() ;}private static  sa.rule.types.Strat  tom_get_slot_StratMu_s( sa.rule.types.Strat  t) {return  t.gets() ;}private static boolean tom_is_fun_sym_StratAppl( sa.rule.types.Strat  t) {return  (t instanceof sa.rule.types.strat.StratAppl) ;}private static  String  tom_get_slot_StratAppl_name( sa.rule.types.Strat  t) {return  t.getname() ;}private static  sa.rule.types.StratList  tom_get_slot_StratAppl_args( sa.rule.types.Strat  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Param( sa.rule.types.Param  t) {return  (t instanceof sa.rule.types.param.Param) ;}private static  sa.rule.types.Param  tom_make_Param( String  t0) { return  sa.rule.types.param.Param.make(t0) ;}private static  String  tom_get_slot_Param_Name( sa.rule.types.Param  t) {return  t.getName() ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static  sa.rule.types.Term  tom_make_Var( String  t0) { return  sa.rule.types.term.Var.make(t0) ;}private static boolean tom_is_fun_sym_Otrs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Otrs) ;}private static  sa.rule.types.RuleList  tom_get_slot_Otrs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static boolean tom_is_fun_sym_Trs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Trs) ;}private static  sa.rule.types.RuleList  tom_get_slot_Trs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static boolean tom_is_fun_sym_Rule( sa.rule.types.Rule  t) {return  (t instanceof sa.rule.types.rule.Rule) ;}private static  sa.rule.types.Rule  tom_make_Rule( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.rule.Rule.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Rule_lhs( sa.rule.types.Rule  t) {return  t.getlhs() ;}private static  sa.rule.types.Term  tom_get_slot_Rule_rhs( sa.rule.types.Rule  t) {return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConcParam( sa.rule.types.ParamList  t) {return  ((t instanceof sa.rule.types.paramlist.ConsConcParam) || (t instanceof sa.rule.types.paramlist.EmptyConcParam)) ;}private static  sa.rule.types.ParamList  tom_empty_list_ConcParam() { return  sa.rule.types.paramlist.EmptyConcParam.make() ;}private static  sa.rule.types.ParamList  tom_cons_list_ConcParam( sa.rule.types.Param  e,  sa.rule.types.ParamList  l) { return  sa.rule.types.paramlist.ConsConcParam.make(e,l) ;}private static  sa.rule.types.Param  tom_get_head_ConcParam_ParamList( sa.rule.types.ParamList  l) {return  l.getHeadConcParam() ;}private static  sa.rule.types.ParamList  tom_get_tail_ConcParam_ParamList( sa.rule.types.ParamList  l) {return  l.getTailConcParam() ;}private static boolean tom_is_empty_ConcParam_ParamList( sa.rule.types.ParamList  l) {return  l.isEmptyConcParam() ;}   private static   sa.rule.types.ParamList  tom_append_list_ConcParam( sa.rule.types.ParamList l1,  sa.rule.types.ParamList  l2) {     if( l1.isEmptyConcParam() ) {       return l2;     } else if( l2.isEmptyConcParam() ) {       return l1;     } else if(  l1.getTailConcParam() .isEmptyConcParam() ) {       return  sa.rule.types.paramlist.ConsConcParam.make( l1.getHeadConcParam() ,l2) ;     } else {       return  sa.rule.types.paramlist.ConsConcParam.make( l1.getHeadConcParam() ,tom_append_list_ConcParam( l1.getTailConcParam() ,l2)) ;     }   }   private static   sa.rule.types.ParamList  tom_get_slice_ConcParam( sa.rule.types.ParamList  begin,  sa.rule.types.ParamList  end, sa.rule.types.ParamList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcParam()  ||  (end==tom_empty_list_ConcParam()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.paramlist.ConsConcParam.make( begin.getHeadConcParam() ,( sa.rule.types.ParamList )tom_get_slice_ConcParam( begin.getTailConcParam() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}   private static boolean tom_equal_term_Map(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Map(Object t) {return  t instanceof java.util.Map ;} private static boolean tom_equal_term_List(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_List(Object t) {return  t instanceof java.util.List ;} private static boolean tom_equal_term_Set(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Set(Object t) {return  t instanceof java.util.Set ;} 








  public static Compiler instance = null;

  // initial AST
  private Program program;

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;

  // names of strategies already compiled
  private Set<String> generatedStrategy;

  private Map<Strat,List<Rule>> storedTRSs;
  private Map<Strat,String> strategySymbols;

  /**
   * initialize the TRS and set the (generated) symbol that should be
   * used to wrap the terms to reduce
   *
   */
  private Compiler() {
    this.generatedStrategy = new HashSet<String>();
    this.storedTRSs = new HashMap<Strat,List<Rule>>();;
    this.strategySymbols = new HashMap<Strat,String>();;
  }

  /**
   * get the instance of the Singleton
   *
   */
  public static Compiler getInstance() {
    if(instance == null) {
      instance = new Compiler();
    }
    return instance;
  }

  /**
   * get the names of the compiled strategies
   * @return the names of the compiled strategies
   */
  public List<String> getStrategyNames() {
    return new ArrayList(generatedStrategy);
  }

  public void setProgram(Program program) throws TypeMismatchException {
    this.extractedSignature = new Signature();
    this.extractedSignature.setSignature(program);
    this.generatedSignature = this.extractedSignature.expandSignature();

    this.program = program;
  }

  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }

  public Signature getGeneratedSignature() {
    return this.generatedSignature;
  }


  /**
   * Compile the strategy strategyName into a rewrite system.
   * @param strategyName the name of the strategy to compile
   * @return the TRS for strategyName
   */
  public RuleList compileStrategy(Set<String> strategyNames) {
    List<Rule> mutableList = new ArrayList<Rule>();

    for(String strategyName: strategyNames) {
      Strat strategy = this.expandStrategy(strategyName);
      assert strategy != null;

      if(!generatedStrategy.contains(strategyName)) {
        // if not generated yet
        String strategySymbol = this.compileStrat(strategy,mutableList);
        generateTriggerRule(strategyName,strategySymbol,mutableList);
        generatedStrategy.add(strategyName);
      } else {
        // do nothing
      }
    }

    RuleList ruleList = Tools.fromListOfRule(mutableList);

    if(!Tools.isLhsLinear(ruleList) || Tools.containsEqAnd(ruleList)) {
      ruleList = generateEquality(ruleList);
    }

    if(Main.options.metalevel) {
      // generate encode/decode only for Tom
      if(Main.options.classname != null) {
        ruleList = generateEncodeDecode(ruleList);
      }
    }
    return ruleList;
  }

  /*
   * Given a name, retrieve the corresponding StratDecl (which should not have parameter)
   * and return an expanded version of the body
   * The resulting strategy is self-contained
   */
  public Strat expandStrategy(String strategyName) {
    StratDecl sd = Tools.getStratDecl(strategyName, this.program);
    Strat res = tom_make_StratIdentity(); // identity if strategyName does not exist

    try {
      {{if (tom_is_sort_StratDecl(sd)) {if (tom_is_sort_StratDecl((( sa.rule.types.StratDecl )sd))) {if (tom_is_fun_sym_StratDecl((( sa.rule.types.StratDecl )(( sa.rule.types.StratDecl )sd)))) { sa.rule.types.ParamList  tomMatch1_2=tom_get_slot_StratDecl_ParamList((( sa.rule.types.StratDecl )sd));if (tom_is_fun_sym_ConcParam((( sa.rule.types.ParamList )tomMatch1_2))) {if (tom_is_empty_ConcParam_ParamList(tomMatch1_2)) {

            res = tom_make_RepeatId(tom_make_TopDown(tom_make_ExpandStratAppl(this))).visit(tom_get_slot_StratDecl_Body((( sa.rule.types.StratDecl )sd)));
        }}}}}}}

    } catch(VisitFailure e) {
    }

    return res;
  }

  /*
   * used by expandStrategy
   * for each StratAppl:
   * retrieve the corresponding StratDecl
   * rename with fresh variables
   * apply the macro expansion
   */
  public static class ExpandStratAppl extends tom.library.sl.AbstractStrategyBasic {private  Compiler  compiler;public ExpandStratAppl( Compiler  compiler) {super(tom_make_Identity());this.compiler=compiler;}public  Compiler  getcompiler() {return compiler;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Strat(v)) {return ((T)visit_Strat((( sa.rule.types.Strat )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  _visit_Strat( sa.rule.types.Strat  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Strat )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  visit_Strat( sa.rule.types.Strat  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Strat(tom__arg)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )tom__arg))) {if (tom_is_fun_sym_StratAppl((( sa.rule.types.Strat )(( sa.rule.types.Strat )tom__arg)))) {


        StratDecl sd = Tools.getStratDecl(tom_get_slot_StratAppl_name((( sa.rule.types.Strat )tom__arg)), compiler.program);
        Map map = new HashMap();
        sd = tom_make_TopDown(tom_make_FreshStratDecl(map)).visitLight(sd);
        Strat si = compiler.instantiateStrategy(sd, tom_get_slot_StratAppl_args((( sa.rule.types.Strat )tom__arg)));
        return si;
      }}}}}return _visit_Strat(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandStratAppl( Compiler  t0) { return new ExpandStratAppl(t0);}



  /*
   * Rename the variables of a StratDecl into fresh names
   */
  public static class FreshStratDecl extends tom.library.sl.AbstractStrategyBasic {private  java.util.Map  map;public FreshStratDecl( java.util.Map  map) {super(tom_make_Identity());this.map=map;}public  java.util.Map  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Param(v)) {return ((T)visit_Param((( sa.rule.types.Param )v),introspector));}if (tom_is_sort_Strat(v)) {return ((T)visit_Strat((( sa.rule.types.Strat )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  _visit_Strat( sa.rule.types.Strat  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Strat )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Param  _visit_Param( sa.rule.types.Param  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Param )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  visit_Strat( sa.rule.types.Strat  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Strat(tom__arg)) {boolean tomMatch3_5= false ; String  tomMatch3_1= "" ; sa.rule.types.Strat  tomMatch3_3= null ; sa.rule.types.Strat  tomMatch3_4= null ;if (tom_is_sort_Strat((( sa.rule.types.Strat )tom__arg))) {if (tom_is_fun_sym_StratName((( sa.rule.types.Strat )(( sa.rule.types.Strat )tom__arg)))) {{tomMatch3_5= true ;tomMatch3_3=(( sa.rule.types.Strat )tom__arg);tomMatch3_1=tom_get_slot_StratName_name(tomMatch3_3);}} else {if (tom_is_sort_Strat((( sa.rule.types.Strat )tom__arg))) {if (tom_is_fun_sym_StratMu((( sa.rule.types.Strat )(( sa.rule.types.Strat )tom__arg)))) {{tomMatch3_5= true ;tomMatch3_4=(( sa.rule.types.Strat )tom__arg);tomMatch3_1=tom_get_slot_StratMu_name(tomMatch3_4);}}}}}if (tomMatch3_5) { String  tom_n=tomMatch3_1;













        String newName = (String) map.get(tom_n);
        if(newName == null) {
          newName = Tools.getName("_"+tom_n);
          map.put(tom_n,newName);
        }
        return (( sa.rule.types.Strat )tom__arg).setname(newName);
      }}}}return _visit_Strat(tom__arg,introspector);}@SuppressWarnings("unchecked")public  sa.rule.types.Param  visit_Param( sa.rule.types.Param  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Param(tom__arg)) {if (tom_is_sort_Param((( sa.rule.types.Param )tom__arg))) {if (tom_is_fun_sym_Param((( sa.rule.types.Param )(( sa.rule.types.Param )tom__arg)))) { String  tom_n=tom_get_slot_Param_Name((( sa.rule.types.Param )tom__arg));         String newName = (String) map.get(tom_n);         if(newName == null) {           newName = Tools.getName("_"+tom_n);           map.put(tom_n,newName);         }         return tom_make_Param(newName);       }}}}}return _visit_Param(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_FreshStratDecl( java.util.Map  t0) { return new FreshStratDecl(t0);}




  /*
   * Given a StratDecl (name, params, body)
   * Replace the parameters by their effective values (args) in body
   * i.e. apply the substitution [param_1 -> arg_1, ..., param_n -> arg_n]
   */
  public Strat instantiateStrategy(StratDecl sd, StratList args) {
    Strat res = null;

    try {
      {{if (tom_is_sort_StratDecl(sd)) {if (tom_is_sort_StratDecl((( sa.rule.types.StratDecl )sd))) {if (tom_is_fun_sym_StratDecl((( sa.rule.types.StratDecl )(( sa.rule.types.StratDecl )sd)))) { sa.rule.types.ParamList  tom_params=tom_get_slot_StratDecl_ParamList((( sa.rule.types.StratDecl )sd));

          if(tom_params.length() == args.length()) {
            res = tom_make_TopDown(tom_make_ReplaceParameters(tom_params,args)).visit(tom_get_slot_StratDecl_Body((( sa.rule.types.StratDecl )sd)));
          }
        }}}}}

    } catch(VisitFailure e) {
    }

    return res;
  }

  /*
   * used by instantiateStrategy to apply the substitution
   */
  public static class ReplaceParameters extends tom.library.sl.AbstractStrategyBasic {private  sa.rule.types.ParamList  params;private  sa.rule.types.StratList  args;public ReplaceParameters( sa.rule.types.ParamList  params,  sa.rule.types.StratList  args) {super(tom_make_Identity());this.params=params;this.args=args;}public  sa.rule.types.ParamList  getparams() {return params;}public  sa.rule.types.StratList  getargs() {return args;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Strat(v)) {return ((T)visit_Strat((( sa.rule.types.Strat )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  _visit_Strat( sa.rule.types.Strat  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Strat )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  visit_Strat( sa.rule.types.Strat  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Strat(tom__arg)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )tom__arg))) {if (tom_is_fun_sym_StratName((( sa.rule.types.Strat )(( sa.rule.types.Strat )tom__arg)))) {


          //System.out.println("stratname = " + `n);
          //System.out.println("params = " + params);
          //System.out.println("args = " + args);
          ParamList plist = params;
          StratList slist = args;

        while(!plist.isEmptyConcParam() && !slist.isEmptyConcStrat()) {
          Param p = plist.getHeadConcParam();
          Strat s = slist.getHeadConcStrat();
          //System.out.println("param = " + p + " -- arg = " + s);
          {{if (tom_is_sort_Param(p)) {if (tom_is_sort_Param((( sa.rule.types.Param )p))) {if (tom_is_fun_sym_Param((( sa.rule.types.Param )(( sa.rule.types.Param )p)))) {if (tom_equal_term_String(tom_get_slot_Param_Name((( sa.rule.types.Param )p)), tom_get_slot_StratName_name((( sa.rule.types.Strat )tom__arg)))) {

              return s;
            }}}}}}

          plist = plist.getTailConcParam();
          slist = slist.getTailConcStrat();
        }
      }}}}}return _visit_Strat(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ReplaceParameters( sa.rule.types.ParamList  t0,  sa.rule.types.StratList  t1) { return new ReplaceParameters(t0,t1);}



  /**
   * compile a (ordered) list of rules
   * return the name of the top symbol (phi) introduced
   * @param ruleList the ordered list of rules to compile
   * @param generatedRules the (possibily ordered) list of rewrite rules generated
   * @param strategyName the strategy name to generate (if not null)
   * @return the symbol to be used for the compiled strategy
   */
  private String compileRuleList(RuleList ruleList, List<Rule> generatedRules, String strategyName) {
    Signature gSig = getGeneratedSignature();
    Signature eSig = getExtractedSignature();
    boolean ordered = Main.options.ordered;

    /*
     * Pre-treatment: remove anti-patterns (expandGeneralAntiPatterns)
     * move into compileStrategy ?
     */
    RuleList rList = ruleList;
    if(Main.options.withAP == false) {
      RuleCompiler ruleCompiler = new RuleCompiler(eSig,gSig);
      rList = ruleCompiler.expandGeneralAntiPatterns(rList,null);

      for(Rule rule: rList.getCollectionConcRule()) {
        System.out.println("EXPANDED AP RULE: " + Pretty.toString(rule) );
      }
    }

    ruleList = rList;

    /*
     * lhs -> rhs becomes
     * in the linear case:
     *   rule(lhs) -> rhs
     *   rule(X@!lhs) -> nextRule(X), could be Bottom(X) when it is the last rule
     * in the non-linear case:
     *   rule(X@linear-lhs) -> rule'(X, true ^ constraint on non linear variables)
     *   rule(X@!linear-lhs) -> nextRule(X), could be Bottom(X) when it is the last rule
     *   rule'(linear-lhs, true) -> rhs
     *   rule'(X@linear-lhs, false) -> nextRule(X), could be Bottom(X) when it is the last rule
     *
     * in the ordered case
     * in the linear case:
     *   rule(lhs) -> rhs
     *   rule(X) -> Bottom(X) after the last rule
     * in the non-linear case:
     *   rule(X@linear-lhs) -> rule'(X, true ^ constraint on non linear variables)
     *   rule'(linear-lhs, true) -> rhs
     *   rule(X) -> Bottom(X) after the last rule
     */

    Term X = Var(Tools.getName("X"));
    String rule = strategyName;
    if(rule == null || ordered==false) {
      // we generate a new fresh name for each rule in the non-ordered case
      rule = Tools.getName(StrategyOperator.RULE.getName());
    }

    String cr = Tools.getName(Tools.addAuxExtension(StrategyOperator.RULE.getName()));

    // used just to have in generatedRules the packages of rules in the order they are called
    List<Rule> localRules = new ArrayList<Rule>();


    if(!Main.options.metalevel) {
      // if declared strategy (i.e. defined name) use its name; otherwise generate fresh name
      gSig.addFunctionSymbol(rule,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
      gSig.addFunctionSymbol(cr,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_cons_list_ConcGomType(Signature.TYPE_BOOLEAN,tom_empty_list_ConcGomType())),Signature.TYPE_TERM);

      {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList))) {

          if(ordered) {
            // after the last rule: rule(Bottom(X)) -> Bottom(X)
            localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));
          }

          // after the last rule: rule(X) -> Bottom(X)
          localRules.add(Rule(_appl(rule,X), Bottom(X))); // X should be a term of the signature; morally X@!Bottom(_)
        }}}}{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.Rule  tomMatch8_8=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList));if (tom_is_sort_Rule(tomMatch8_8)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch8_8))) { sa.rule.types.Term  tom_lhs=tom_get_slot_Rule_lhs(tomMatch8_8); sa.rule.types.Term  tom_rhs=tom_get_slot_Rule_rhs(tomMatch8_8); sa.rule.types.Rule  tom_currentRule=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList));


          // if it is a rule with anti-patterns we need a fresh symbol even if it is an ordered compilation
          String nextRuleSymbol = rule;
          if(Tools.containsAP(tom_currentRule)) {
            nextRuleSymbol = null;
          }
          String nextRule = compileRuleList(tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),generatedRules,nextRuleSymbol);

          if(Main.options.withAP == false) {
            RuleCompiler ruleCompiler = new RuleCompiler(eSig,gSig);
            rList = ruleCompiler.expandGeneralAntiPatterns(tom_cons_list_ConcRule(tom_currentRule,tom_empty_list_ConcRule()),nextRule);

            for(Rule r: rList.getCollectionConcRule()) {
              System.out.println("CompileRuleList -> EXPANDED AP RULE: " + Pretty.toString(r) );
            }
          }

          TermList result = Tools.linearize(tom_lhs, this.generatedSignature );

          {{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.TermList  tomMatch9_2=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch9_2))) { sa.rule.types.Term  tomMatch9_6=tom_get_head_TermList_TermList(tomMatch9_2);if (tom_is_sort_Term(tomMatch9_6)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch9_6))) { String  tomMatch9_4=tom_get_slot_Appl_symbol(tomMatch9_6); sa.rule.types.TermList  tomMatch9_5=tom_get_slot_Appl_args(tomMatch9_6);if ( true ) {if (tom_equal_term_String("True", tomMatch9_4)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch9_5))) {if (tom_is_empty_TermList_TermList(tomMatch9_5)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch9_2))) {

              /*
               * if already linear lhs
               * rule(Bottom(X)) -> Bottom(X) propagate failure; if the rule is applied to the result of a strategy that failed then the result is a failure
               * rule(X@lhs) -> rhs
               * rule(X@!lhs) -> nextRule(X), could be Bottom(X) when it is the last rule
               * in the ordered case:
               * rule(lhs) -> rhs
               * rule(Bottom(X)) -> Bottom(X) after the last rule
               * rule(X) -> Bottom(X) after the last rule
               */
              //Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(`lhs)));
              //localRules.add(Rule(lhs, _appl(nextRule,X)) );
              if(!ordered) {
                // rule(Bottom(X)) -> Bottom(X)
                localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));
                localRules.add(Rule(_appl(rule,At(X,tom_lhs)), tom_rhs));
                localRules.add(Rule(_appl(rule,At(X,Anti(tom_lhs))), _appl(nextRule,X)) );
              } else {
                localRules.add(Rule(_appl(rule,At(X,tom_lhs)), tom_rhs));
              }
            }}}}}}}}}}}}{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.Term  tom_linearlhs=tom_get_head_TermList_TermList((( sa.rule.types.TermList )result)); sa.rule.types.TermList  tomMatch9_13=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch9_13))) { sa.rule.types.Term  tom_cond=tom_get_head_TermList_TermList(tomMatch9_13);boolean tomMatch9_23= false ; sa.rule.types.Term  tomMatch9_17=tom_get_head_TermList_TermList(tomMatch9_13);if (tom_is_sort_Term(tomMatch9_17)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch9_17))) { String  tomMatch9_15=tom_get_slot_Appl_symbol(tomMatch9_17); sa.rule.types.TermList  tomMatch9_16=tom_get_slot_Appl_args(tomMatch9_17);if ( true ) {if (tom_equal_term_String("True", tomMatch9_15)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch9_16))) {if (tom_is_empty_TermList_TermList(tomMatch9_16)) {if (tom_equal_term_Term(tom_cond, tom_get_head_TermList_TermList(tomMatch9_13))) {tomMatch9_23= true ;}}}}}}}if (!(tomMatch9_23)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch9_13))) {


              /*
               * if non-linear add rules for checking equality for corresponding arguments
               * rule(Bottom(X)) -> Bottom(X)
               * rule(X@linearlhs) -> cr(X,cond)
               * rule(X@!linearlhs) -> nextRule(X) // could be Bot(X) if only one rule, i.e. non next rule
               * cr(linearlhs, True) -> rhs
               * cr(X@linearlhs, False) -> nextRule(X) // could be Bot(X) if only one rule, i.e. non next rule
               * in the ordered case:
               * rule(X@linear-lhs) -> cr(X, cond)
               * cr(linear-lhs, true) -> rhs
               * rule(Bottom(X)) -> Bottom(X) after the last rule
               * rule(X) -> Bottom(X) after the last rule
               */
              //Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(`linearlhs)));
              //localRules.add(Rule(lhs, _appl(nextRule,X)) );

              if(!ordered) {
                localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));
                localRules.add(Rule(_appl(rule,At(X,tom_linearlhs)), _appl(cr, X, tom_cond)));
                localRules.add(Rule(_appl(rule,At(X,Anti(tom_linearlhs))), _appl(nextRule,X) ));
                localRules.add(Rule(_appl(cr,tom_linearlhs, True()), tom_rhs));
                localRules.add(Rule(_appl(cr,At(X,tom_linearlhs),False()), _appl(nextRule,X) ) );
              } else {
                localRules.add(Rule(_appl(rule,At(X,tom_linearlhs)), _appl(cr, X, tom_cond)));
                localRules.add(Rule(_appl(cr,tom_linearlhs, True()), tom_rhs));
              }
            }}}}}}}}

        }}}}}}}


    } else {
      // META-LEVEL
      gSig.addFunctionSymbol(rule,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
      gSig.addFunctionSymbol(cr,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_cons_list_ConcGomType(Signature.TYPE_BOOLEAN,tom_empty_list_ConcGomType())),Signature.TYPE_METATERM);

      {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.Rule  tomMatch10_6=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList));if (tom_is_sort_Rule(tomMatch10_6)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch10_6))) { sa.rule.types.Term  tom_lhs=tom_get_slot_Rule_lhs(tomMatch10_6);


          String nextRule = compileRuleList(tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),generatedRules,rule);

          TermList result = Tools.linearize(tom_lhs, this.generatedSignature);
          Term mlhs = Tools.metaEncodeConsNil(tom_lhs,generatedSignature);
          Term mrhs = Tools.metaEncodeConsNil(tom_get_slot_Rule_rhs(tomMatch10_6),generatedSignature);

          /*
           * propagate failure
           * rule(Bot(X)) -> Bot(X)
           */
          localRules.add(Rule(_appl(rule,Bottom(X)), Bottom(X)));

          {{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.TermList  tomMatch11_2=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch11_2))) { sa.rule.types.Term  tomMatch11_6=tom_get_head_TermList_TermList(tomMatch11_2);if (tom_is_sort_Term(tomMatch11_6)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch11_6))) { String  tomMatch11_4=tom_get_slot_Appl_symbol(tomMatch11_6); sa.rule.types.TermList  tomMatch11_5=tom_get_slot_Appl_args(tomMatch11_6);if ( true ) {if (tom_equal_term_String("True", tomMatch11_4)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch11_5))) {if (tom_is_empty_TermList_TermList(tomMatch11_5)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch11_2))) {

              /*
               * if already linear lhs
               * rule(X@mlhs) -> mrhs
               * rule(X@!mlhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,mlhs)), mrhs));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(mlhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(mlhs))), _appl(nextRule,X) ) );
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X) ) );
//               }
            }}}}}}}}}}}}{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.TermList  tomMatch11_13=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch11_13))) { sa.rule.types.Term  tom_cond=tom_get_head_TermList_TermList(tomMatch11_13);boolean tomMatch11_23= false ; sa.rule.types.Term  tomMatch11_17=tom_get_head_TermList_TermList(tomMatch11_13);if (tom_is_sort_Term(tomMatch11_17)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch11_17))) { String  tomMatch11_15=tom_get_slot_Appl_symbol(tomMatch11_17); sa.rule.types.TermList  tomMatch11_16=tom_get_slot_Appl_args(tomMatch11_17);if ( true ) {if (tom_equal_term_String("True", tomMatch11_15)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch11_16))) {if (tom_is_empty_TermList_TermList(tomMatch11_16)) {if (tom_equal_term_Term(tom_cond, tom_get_head_TermList_TermList(tomMatch11_13))) {tomMatch11_23= true ;}}}}}}}if (!(tomMatch11_23)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch11_13))) {


              // if non-linear add rules for checking equality for corresponding arguments
              Term mlinearlhs = Tools.metaEncodeConsNil(tom_get_head_TermList_TermList((( sa.rule.types.TermList )result)),generatedSignature);
              Term mcond = Tools.metaEncodeVars(tom_cond,generatedSignature);
              /*
               * rule(X@mlinearlhs) -> cr(X,cond)
               * rule(X@!mlinearlhs) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               * cr(mlinearlhs, True) -> mrhs
               * cr(X@mlinearlhs, False) -> nextRule(X)       // could be Bot(X) if only one rule, i.e. non next rule
               */
              localRules.add(Rule(_appl(rule,At(X,mlinearlhs)), _appl(cr,X,mcond)));
              Term lhs = Main.options.ordered ? _appl(rule,X) : _appl(rule,At(X,Anti(mlinearlhs)));
              localRules.add(Rule(lhs, _appl(nextRule,X)) );
//               if(!ordered){
//                 localRules.add(Rule(_appl(rule,At(X,Anti(mlinearlhs))), _appl(nextRule,X)));
//               }else{
//                 localRules.add(Rule(_appl(rule,X), _appl(nextRule,X)));
//               }
              localRules.add(Rule(_appl(cr,mlinearlhs, True()), mrhs));
              localRules.add(Rule(_appl(cr,At(X,mlinearlhs), False()), _appl(nextRule,X)));
            }}}}}}}}


          // TODO: non-linear anti-pattern
          // localRules.add(`Rule(Appl(rule,TermList(At(X,Anti(mlhs)))),botX));
        }}}}}}{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList))) {


          localRules.add(Rule(_appl(rule,X), Bottom(X))); // X should be a term of the signature; morally X@!Bottom(_)
        }}}}}


      for(String name:eSig.getConstructors()) {
        // add symb_a(), symb_b(), symb_f(), symb_g() in the signature
        gSig.addSymbol("symb_"+name,tom_empty_list_ConcGomType(),Signature.TYPE_METASYMBOL);
      }
    }

    // put the locally generated rules at the beginning
    // efficient for LinkedLists but not for ArrayLists
    generatedRules.addAll(0,localRules);

    return rule;
  }


  /**
   * compile a strategy built with strategy operator, ordered lists of rules;
   * rules can be non-linear and contain imbricated APs
   * (classical OR  using meta-representation)
   * return the name of the top symbol (phi) introduced
   * @param strat the strategy to compile
   * @param rules the list of rewrite rules generated for this strategy
   * @return the symbol to be used for the compiled strategy
   */
  private boolean generated_aux_functions = false;

  private String compileStrat(Strat strat, List<Rule> rules) {
    Signature gSig = getGeneratedSignature();
    Signature eSig = getExtractedSignature();

    // by default, if strategy can't be compiled, a meaningless name
    // TODO: change to exception ?
    List<Rule> generatedRules = null;

    // if the stategy has been already compiled and a symbol generated
    String strategySymbol = this.strategySymbols.get(strat);
    if(strategySymbol != null) {
      generatedRules = this.storedTRSs.get(strat);
      //System.out.println("ALREADY EXISTING: "+strat);
    } else {
      generatedRules = new ArrayList<Rule>();
      Term X = Var(Tools.getName("X"));
      Term Y = Var(Tools.getName("Y"));
      Term XX = Var(Tools.getName("XX"));
      Term YY = Var(Tools.getName("YY"));
      Term Z = Var(Tools.getName("Z"));
      Term Z0 = Var(Tools.getName("Z0"));
      Term Z1 = Var(Tools.getName("Z1"));
      Term Z2 = Var(Tools.getName("Z2"));
      Term Z3 = Var(Tools.getName("Z3"));

      {{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratTrs((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) { sa.rule.types.Trs  tomMatch12_1=tom_get_slot_StratTrs_trs((( sa.rule.types.Strat )strat));if (tom_is_sort_Trs(tomMatch12_1)) {if (tom_is_fun_sym_Trs((( sa.rule.types.Trs )tomMatch12_1))) {


          throw new RuntimeException("Not Yet Implemented");
        }}}}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratTrs((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) { sa.rule.types.Trs  tomMatch12_8=tom_get_slot_StratTrs_trs((( sa.rule.types.Strat )strat));if (tom_is_sort_Trs(tomMatch12_8)) {if (tom_is_fun_sym_Otrs((( sa.rule.types.Trs )tomMatch12_8))) {



          //if(Main.options.pattern) {
          //  System.out.println("pattern: " + `rulelist);
          //  RuleList lin = this.transformNLOTRSintoLOTRS(`rulelist, gSig);
          //  System.out.println("linear otrs: " + lin);
          //  RuleList res = Trs.trsRule(lin,eSig);
          //}

          strategySymbol = this.compileRuleList(tom_get_slot_Otrs_list(tomMatch12_8),generatedRules,null);
        }}}}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratMu((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) { sa.rule.types.Strat  tom_s=tom_get_slot_StratMu_s((( sa.rule.types.Strat )strat));





          try {
            String mu = Tools.getName(StrategyOperator.MU.getName());
            Strat newStrat = tom_make_TopDown(tom_make_ReplaceMuVar(tom_get_slot_StratMu_name((( sa.rule.types.Strat )strat)),mu)).visitLight(tom_s);
            String phi_s = compileStrat(newStrat,generatedRules);
            if(!Main.options.metalevel) {
              gSig.addFunctionSymbol(mu,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
              /*
               * mu(Bot(X)) -> Bot(X)
               * mu(X@!Bot(Y)) -> phi_s(X)
               */
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(mu,X) : _appl(mu,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, _appl(phi_s,X)));
            } else {
              // META-LEVEL
              gSig.addFunctionSymbol(mu,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
              /*
               * mu(Bot(X)) -> Bot(X)
               * mu(Appl(Y,Z)) -> phi_s(Appl(Y,Z))
               */
              generatedRules.add(Rule(_appl(mu,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(mu,Appl(Y,Z)), _appl(phi_s,Appl(Y,Z))));
            }
            strategySymbol = phi_s;
          } catch(VisitFailure e) {
            System.out.println("failure in StratMu on: " + tom_s);
          }
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratName((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {



          strategySymbol = tom_get_slot_StratName_name((( sa.rule.types.Strat )strat));
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratIdentity((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String id = Tools.getName(StrategyOperator.IDENTITY.getName());
          if(!Main.options.metalevel) {
            gSig.addFunctionSymbol(id,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * the rule cannot be applied on arguments containing fresh
               * variables but only on terms from the signature or Bottom
               * normally it will follow reduction in original TRS
               * id(Bot(X)) -> Bot(X)
               * id(X@!Bot(Y)) -> X
               */
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(id,X) : _appl(id,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, X));
            } else { // TODO: remove APPROX branch?
              /*
               * Bottom of Bottom is Bottom
               * this is not necessary if exact reduction - in this case Bottom is propagated immediately
               * id(X) -> X
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else {
            // Meta-LEVEL
            gSig.addFunctionSymbol(id,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * id(Bot(X)) -> Bot(X)
               * id(Appl(X,Y)) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(id,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(id,Appl(X,Y)), Appl(X,Y)));
            } else {  // TODO: remove APPROX branch?
              /*
               * id(X) -> X
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(id,X), X));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          }
          strategySymbol = id;
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratFail((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String fail = Tools.getName(StrategyOperator.FAIL.getName());
          if( !Main.options.metalevel ) {
            gSig.addFunctionSymbol(fail,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * fail(Bot(X)) -> Bot(X)
               * fail(X@!Bot(Y)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(fail,X) : _appl(fail,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, X));
            } else { // TODO: remove APPROX branch?
              /*
               * fail(X) -> Bot(X)
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }
          } else {
            // META-LEVEL
            gSig.addFunctionSymbol(fail,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * fail(Bot(X)) -> Bot(X)
               * fail(Appl(X,Y)) -> Bot(Appl(X,Y))
               */
              generatedRules.add(Rule(_appl(fail,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(fail,Appl(X,Y)), Bottom(Appl(X,Y))));
            } else { // TODO: remove APPROX branch?
              /*
               * fail(X) -> Bot(X)
               * Bot(Bot(X)) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(fail,X), Bottom(X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
            }

          }
          strategySymbol = fail;
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratSequence((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String n1 = compileStrat(tom_get_slot_StratSequence_s1((( sa.rule.types.Strat )strat)),generatedRules);
          String n2 = compileStrat(tom_get_slot_StratSequence_s2((( sa.rule.types.Strat )strat)),generatedRules);
          String seq = Tools.getName(StrategyOperator.SEQ.getName());
          String seq2 = Tools.getName(Tools.addAuxExtension(StrategyOperator.SEQ.getName()));
          if( !Main.options.metalevel ) {
            gSig.addFunctionSymbol(seq,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
            gSig.addFunctionSymbol(seq2,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType())),Signature.TYPE_TERM);
            if( !Main.options.approx ) {
              /*
               * the rule cannot be applied on arguments containing fresh variables but only on terms from the signature or Bottom
               * normally it will follow reduction in original TRS
               * seq(Bot(X)) -> Bot(X)
               * seq(X@!Bot(Y)) -> seq2(n2(n1(X)),X)
               * seq2(Bot(Y),X) -> Bot(X)
               * seq2(X@!Bot(Y),Z) -> X
               */
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              Term lhs = Main.options.ordered ? _appl(seq,X) : _appl(seq,At(X,Anti(Bottom(Y))));
              generatedRules.add(Rule(lhs, _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
              Term nlhs = Main.options.ordered ? _appl(seq2,X,Z) : _appl(seq2,At(X,Anti(Bottom(Y))),Z);
              generatedRules.add(Rule(nlhs,X));
            } else { // TODO: remove APPROX branch?
              /*
               * seq(X) -> seq2(n2(n1(X)),X)
               * Bot(Bot(X)) -> Bot(X)
               * seq2(X@!Bot(Y),Z) -> X
               * seq2(Bot(Y),X) -> Bot(X)
               */
              generatedRules.add(Rule(_appl(seq,X), _appl(seq2,_appl(n2,_appl(n1,X)),X)));
              generatedRules.add(Rule(Bottom(Bottom(X)), Bottom(X)));
              generatedRules.add(Rule(_appl(seq2,At(X,Anti(Bottom(Y))),Z), X));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
            }
          } else {
            // META-LEVEL
            gSig.addFunctionSymbol(seq,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            gSig.addFunctionSymbol(seq2,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType())),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * seq(Bot(X)) -> Bot(X)
               * seq(Appl(X,Y)) -> seq2(n2(n1(Appl(X,Y))),Appl(X,Y))
               * seq2(Bot(Y),X) -> Bot(X)
               * seq2(Appl(X,Y),Z) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(seq,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(seq,Appl(X,Y)), _appl(seq2,_appl(n2,_appl(n1,Appl(X,Y))),Appl(X,Y))));
              generatedRules.add(Rule(_appl(seq2,Bottom(Y),X), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(seq2,Appl(X,Y),Z), Appl(X,Y)));
            } else { // TODO: remove APPROX branch?
              /*
               * CHECK HERE
               * seq(X) -> n2(n1(X))
               */
              generatedRules.add(Rule(X, _appl(n2,_appl(n1,X))));
            }

          }
          strategySymbol = seq;
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratChoice((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String n1 = compileStrat(tom_get_slot_StratChoice_s1((( sa.rule.types.Strat )strat)),generatedRules);
          String n2 = compileStrat(tom_get_slot_StratChoice_s2((( sa.rule.types.Strat )strat)),generatedRules);
          String choice = Tools.getName(StrategyOperator.CHOICE.getName());
          String choice2 = Tools.getName(Tools.addAuxExtension(StrategyOperator.CHOICE.getName()));
          if( !Main.options.metalevel ) {
            gSig.addFunctionSymbol(choice,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
            gSig.addFunctionSymbol(choice2,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);
            /*
             * TODO [20/01/2015]: see if not exact is interesting
             * choice(Bot(X)) -> Bot(X)
             * choice(X@!Bot(Y)) -> choice2(n1(X))
             * choice2(Bot(X)) -> n2(X)
             * choice2(X@!Bot(Y)) -> X
             */
            generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
            Term lhs = Main.options.ordered ? _appl(choice,X) : _appl(choice,At(X,Anti(Bottom(Y))));
            generatedRules.add(Rule(lhs, _appl(choice2,_appl(n1,X))));
            generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
            Term nlhs = Main.options.ordered ? _appl(choice2,X) : _appl(choice2,At(X,Anti(Bottom(Y))));
            generatedRules.add(Rule(nlhs, X));
          } else {
            // META-LEVEL
            gSig.addFunctionSymbol(choice,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            gSig.addFunctionSymbol(choice2,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            if( !Main.options.approx ) {
              /*
               * choice(Bot(X)) -> Bot(X)
               * choice(Appl(X,Y)) -> choice2(n1(Appl(X,Y)))
               * choice2(Bot(X)) -> n2(X)
               * choice2(Appl(X,Y) -> Appl(X,Y)
               */
              generatedRules.add(Rule(_appl(choice,Bottom(X)), Bottom(X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(choice,Appl(X,Y)), _appl(choice2,_appl(n1,Appl(X,Y)))));
              generatedRules.add(Rule(_appl(choice2,Bottom(X)), _appl(n2,X)));
              // could use X instead of Appl(X,Y) but it wouldn't change too much (thanks to metalevel)
              generatedRules.add(Rule(_appl(choice2,Appl(X,Y)), Appl(X,Y)));
            }
          }
          strategySymbol = choice;
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratAll((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String phi_s = compileStrat(tom_get_slot_StratAll_s((( sa.rule.types.Strat )strat)),generatedRules);
          String all = Tools.getName(StrategyOperator.ALL.getName());
          if( !Main.options.metalevel ) {
            gSig.addFunctionSymbol(all,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * all(Bot(X)) -> Bot(X)
             */
            generatedRules.add(Rule(_appl(all,Bottom(X)), Bottom(X)));

            for(String name : eSig.getConstructors()) {
              int arity = gSig.getArity(name);
              int arity_all = arity+1;
              if(arity==0) {
                /*
                 * all(name) -> name
                 */
                generatedRules.add(Rule(_appl(all,_appl(name)), _appl(name)));
              } else {
                String all_n = Tools.addOperatorName(all,name);
                GomTypeList all_args = tom_empty_list_ConcGomType();
                for(int i=0; i<arity_all; i++){
                  all_args = tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_append_list_ConcGomType(all_args,tom_empty_list_ConcGomType()));
                }
                gSig.addFunctionSymbol(all_n,all_args,Signature.TYPE_TERM);
                /*
                 * main case
                 * all(f(x1,...,xn)) -> all_n(phi_s(x1),phi_s(x2),...,phi_s(xn),f(x1,...,xn))
                 */
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = tom_make_Var("X_" + i);
                  a_lx[i] = Xi;
                  a_rx[i] = _appl(phi_s, Xi);
                }
                a_rx[arity] = _appl(name, a_lx);
                generatedRules.add(Rule(_appl(all,_appl(name, a_lx)), _appl(all_n, a_rx)));
                /*
                 * generate failure rules
                 * phi_n(Bottom(_),_,...,_,Z) -> Bottom(Z)
                 * phi_n(...,Bottom(_),...,Z) -> Bottom(Z)
                 * phi_n(_,...,_,Bottom(_),Z) -> Bottom(Z)
                 */
                Term[] a_llx = new Term[arity+1];
                for(int i=0 ; i<arity ; i++) {
                  Term X0 = tom_make_Var("X0");
                  a_llx[0] = (i==0)?Bottom(X0):X0;
                  for(int j=1 ; i<arity ; i++) {
                    Term Xj = tom_make_Var("X_" + i);
                    if(j==i) {
                      a_llx[j] = Bottom(Xj);
                    } else {
                      a_llx[j] = Xj;
                    }
                  }
                  a_llx[arity] = Z;
                  generatedRules.add(Rule(_appl(all_n,a_llx), Bottom(Z)));
                }
                /*
                 * generate success rules
                 * all_g(X1@!Bottom(Y1),...,X_n@!Bottom(Y_n),_) -> g(X1,...,X_n)
                 */
                a_lx = new Term[arity+1];
                a_rx = new Term[arity];
                for(int i=0 ; i<arity ; i++) {
                  Term Xi = tom_make_Var("X_" + i);
                  Term Yi = tom_make_Var("Y" + i);
                  a_lx[i] = Main.options.ordered ? Xi : At(Xi,Anti(Bottom(Yi)));
                  a_rx[i] = Xi;
                }
                a_lx[arity] = Z;
                generatedRules.add(Rule(_appl(all_n,a_lx), _appl(name, a_rx)));
              }
            }
          } else {
            // META-LEVEL
            gSig.addFunctionSymbol(all,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            String all_1 = all+"_1";
            String all_2 = all+"_2";
            String all_3 = all+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addFunctionSymbol(all_1,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            generatedSignature.addFunctionSymbol(all_2,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType()),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(all_3,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType())))),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(append,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType())),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(reverse,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType()),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(rconcat,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType())),Signature.TYPE_METALIST);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * all(Bot(X)) -> Bot(X)
             */
            generatedRules.add(Rule(_appl(all,Bottom(X)), Bottom(X)));

            /*
             * all(Appl(Z0,Z1)) -> all_1(Appl(Z0,all_2(Z1)))
             * all_1(Appl(Z0, BottomList(Z))) -> Bottom(Appl(Z0,Z))
             * all_1(Appl(Z0, Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
             * all_1(Appl(Z0, Nil)) -> Appl(Z0,Nil)
             * all_2(Nil) -> Nil
             * all_2(Cons(Z1,Z2)) -> all_3(phi_s(Z1),Z2,Cons(Z1,Nil),Nil)
             * all_3(Bottom(X),todo,rargs,rs_args) -> BottomList(rconcat(rargs,todo))
             * all_3(Appl(X,Y),Nil,rargs,rs_args) -> reverse(Cons(Appl(X,Y),rs_args))
             * all_3(Appl(X,Y), Cons(XX,YY), rargs, rs_args) ->
             * all_3(phi_s(XX), YY, Cons(XX,rargs), Cons(Appl(X,Y),rs_args))
             */
            generatedRules.add(Rule(_appl(all,Appl(Z0,Z1)), _appl(all_1,Appl(Z0,_appl(all_2,Z1)))));
            generatedRules.add(Rule(_appl(all_1,Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z))));
            generatedRules.add(Rule(_appl(all_1,Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2))));
            generatedRules.add(Rule(_appl(all_1,Nil()), Appl(Z0,Nil())));
            generatedRules.add(Rule(_appl(all_2,Nil()), Nil()));
            generatedRules.add(Rule(_appl(all_2,Cons(Z1,Z2)), _appl(all_3,_appl(phi_s,Z1),Z2,Cons(Z1,Nil()),Nil())));

            generatedRules.add(Rule(_appl(all_3,Bottom(X),Z1,Z2,Z3), BottomList(_appl(rconcat,Z2,Z1))));
            generatedRules.add(Rule(_appl(all_3,Appl(X,Y),Nil(),Z2,Z3), _appl(reverse,Cons(Appl(X,Y),Z3))));
            generatedRules.add(Rule(_appl(all_3,Appl(X,Y),Cons(XX,YY),Z2,Z3),
                  _appl(all_3,_appl(phi_s,XX),YY,Cons(XX,Z2),Cons(Appl(X,Y),Z3))));

            if(!generated_aux_functions) {
              generated_aux_functions = true;
              /*
               * append(Nil,Z) -> Cons(Z,Nil)
               * append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
               * reverse(Nil) -> Nil
               * reverse(Cons(X,Y)) -> append(reverse(Y),X)
               * rconcat(Nil,Z) -> Z
               * rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))
               */
              generatedRules.add(Rule(_appl(append,Nil(),Z), Cons(Z,Nil())));
              generatedRules.add(Rule(_appl(append,Cons(X,Y),Z), Cons(X,_appl(append,Y,Z))));
              generatedRules.add(Rule(_appl(reverse,Nil()), Nil()));
              generatedRules.add(Rule(_appl(reverse,Cons(X,Y)), _appl(append,_appl(reverse,Y),X)));
              generatedRules.add(Rule(_appl(rconcat,Nil(),Z), Z));
              generatedRules.add(Rule(_appl(rconcat,Cons(X,Y),Z), _appl(rconcat,Y,Cons(X,Z))));

            }
          }
          strategySymbol = all;
        }}}}{if (tom_is_sort_Strat(strat)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )strat))) {if (tom_is_fun_sym_StratOne((( sa.rule.types.Strat )(( sa.rule.types.Strat )strat)))) {


          String phi_s = compileStrat(tom_get_slot_StratOne_s((( sa.rule.types.Strat )strat)),generatedRules);
          String one = Tools.getName(StrategyOperator.ONE.getName());
          if( !Main.options.metalevel ) {
            gSig.addFunctionSymbol(one,tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_empty_list_ConcGomType()),Signature.TYPE_TERM);

            /*
             * propagate Bottom  (otherwise not reduced and leads to bug in Sequence)
             * one(Bottom(X)) -> Bottom(X)
             */
            generatedRules.add(Rule(_appl(one,Bottom(X)), Bottom(X)));

            for(String name : eSig.getConstructors()) {
              int arity = eSig.getArity(name);
              if(arity==0) {
                /*
                 * one(name) -> Bottom(name)
                 */
                generatedRules.add(Rule(_appl(one,_appl(name)), Bottom(_appl(name))));
              } else {
                String one_n = Tools.addOperatorName(one,name);
                String one_n_1 = one_n + "_1";
                /*
                 * main case
                 * one(f(x1,...,xn)) -> one_n_1(phi_s(x1),x2,...,xn)
                 */
                Term[] a_lx = new Term[arity];
                Term[] a_rx = new Term[arity];
                for(int i=1 ; i<=arity ; i++) {
                  Term Xi = tom_make_Var("X_" + i);
                  a_lx[i-1] = Xi;
                  a_rx[i-1] = (i==1)?_appl(phi_s, Xi):Xi;
                }
                generatedRules.add(Rule(_appl(one,_appl(name, a_lx)), _appl(one_n_1, a_rx)));

                for(int i=1 ; i<=arity ; i++) {
                  String one_n_i = one_n + "_" + i;
                  String one_n_ii = one_n + "_"+(i+1);
                  GomTypeList one_n_args = tom_empty_list_ConcGomType();
                  GomTypeList one_n_ii_args = tom_empty_list_ConcGomType();
                  for(int ni=0; ni<arity; ni++) {
                    one_n_args = tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_append_list_ConcGomType(one_n_args,tom_empty_list_ConcGomType()));
                    one_n_ii_args = tom_cons_list_ConcGomType(Signature.TYPE_TERM,tom_append_list_ConcGomType(one_n_ii_args,tom_empty_list_ConcGomType()));
                  }
                  gSig.addFunctionSymbol(one_n_i,one_n_args,Signature.TYPE_TERM);
                  if(i<arity) {
                    gSig.addFunctionSymbol(one_n_ii,one_n_ii_args,Signature.TYPE_TERM);
                    /*
                     * one_f_i(Bottom(x1),...,Bottom(xi),xj,...,xn)
                     * -> one_f_(i+1)(Bottom(x1),...,Bottom(xi),phi_s(x_i+1),...,xn)
                     */
                    for(int j=1 ; j<=arity ; j++) {
                      Term Xj = Var("X_"+j);
                      if(j<=i) {
                        a_lx[j-1] = Bottom(Xj);
                        a_rx[j-1] = Bottom(Xj);
                      } else {
                        a_lx[j-1] = Xj;
                        a_rx[j-1] = (j==i+1)?_appl(phi_s,Xj):Xj;
                      }
                    }
                    /*
                     * one_n_i(lx) -> one_n_ii(rx)
                     */
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(one_n_ii, a_rx)));
                  } else {
                    /*
                     * one_f_n(Bottom(x1),...,Bottom(xn)) -> Bottom(f(x1,...,xn))
                     */
                    for(int j=1 ; j<=arity ; j++) {
                      Term Xj = Var("X_"+j);
                      a_lx[j-1] = Bottom(Xj);
                      a_rx[j-1] = Xj;
                    }
                    generatedRules.add(Rule(_appl(one_n_i,a_lx), Bottom(_appl(name, a_rx))));
                  }
                  /*
                   * one_f_i(Bottom(x1),...,xi@!Bottom(_),xj,...,xn)
                   * -> f(x1,...,xi,...,xn)
                   */
                  for(int j=1 ; j<=arity ; j++) {
                    Term Xj = Var("X_"+j);
                    if(j<i) {
                      a_lx[j-1] = Bottom(Xj);
                    } else if(j==i) {
                      a_lx[j-1] = Main.options.ordered ? Xj : At(Xj,Anti(Bottom(Y)));
                    } else {
                      a_lx[j-1] = Xj;
                    }
                    a_rx[j-1] = Xj;
                  }
                  /*
                   * one_n_i(lx) -> name(rx)
                   */
                  generatedRules.add(Rule(_appl(one_n_i,a_lx), _appl(name, a_rx)));

                }
              }
            }
          } else {
            // META-LEVEL
            gSig.addFunctionSymbol(one,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            String one_1 = one+"_1";
            String one_2 = one+"_2";
            String one_3 = one+"_3";
            String append = "append";
            String reverse = "reverse";
            String rconcat = "rconcat";
            generatedSignature.addFunctionSymbol(one_1,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType()),Signature.TYPE_METATERM);
            generatedSignature.addFunctionSymbol(one_2,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType()),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(one_3,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType()))),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(append,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METATERM,tom_empty_list_ConcGomType())),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(reverse,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType()),Signature.TYPE_METALIST);
            generatedSignature.addFunctionSymbol(rconcat,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_cons_list_ConcGomType(Signature.TYPE_METALIST,tom_empty_list_ConcGomType())),Signature.TYPE_METALIST);
            /*
             * one(Appl(Z0,Z1)) -> one_1(Appl(Z0,one_2(Z1)))
             */
            generatedRules.add(Rule(_appl(one,Appl(Z0,Z1)), _appl(one_1,Appl(Z0,_appl(one_2,Z1)))));
            /*
             * one_1(Appl(Z0,BottomList(Z))) -> Bottom(Appl(Z0,Z))
             * one_1(Appl(Z0,Cons(Z1,Z2))) -> Appl(Z0,Cons(Z1,Z2))
             */
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,BottomList(Z))), Bottom(Appl(Z0,Z))));
            generatedRules.add(Rule(_appl(one_1,Appl(Z0,Cons(Z1,Z2))), Appl(Z0,Cons(Z1,Z2))));
            /*
             * one_2(Nil) -> BottomList(Nil)
             * one_2(Cons(X,Y)) -> one_3(phi_s(X),Y,Cons(X,Nil))
             */
            generatedRules.add(Rule(_appl(one_2,Nil()), BottomList(Nil())));
            generatedRules.add(Rule(_appl(one_2,Cons(Z1,Z2)), _appl(one_3,_appl(phi_s,Z1),Z2,Cons(Z1,Nil()))));
            /*
             * one_3(Bottom(X),Nil,rargs) -> BottomList(reverse(rargs))
             * one_3(Bottom(X),Cons(head,tail),rargs) -> one_3(phi_s(head), tail, Cons(head,rargs))
             * one_3(Appl(X,Y),todo,Cons(last,rargs)) -> rconcat(rargs,Cons(Appl(X,Y),todo))
             */
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Nil(),Z2), BottomList(_appl(reverse,Z2))));
            generatedRules.add(Rule(_appl(one_3,Bottom(Z),Cons(XX,YY),Z2), _appl(one_3,_appl(phi_s,XX),YY,Cons(XX,Z2))));
            generatedRules.add(Rule(_appl(one_3,Appl(X,Y),Z1,Cons(Z2,Z3)), _appl(rconcat,Z3,Cons(Appl(X,Y),Z1))));

            if(!generated_aux_functions) {
              generated_aux_functions = true;
              /*
               * append(Nil,Z) -> Cons(Z,Nil)
               * append(Cons(X,Y),Z) -> Cons(X,append(Y,Z))
               * reverse(Nil) -> Nil
               * reverse(Cons(X,Y)) -> append(reverse(Y),X)
               * rconcat(Nil,Z) -> Z
               * rconcat(Cons(X,Y),Z) -> rconcat(Y,Cons(X,Z))
               */
              generatedRules.add(Rule(_appl(append,Nil(),Z), Cons(Z,Nil())));
              generatedRules.add(Rule(_appl(append,Cons(X,Y),Z), Cons(X,_appl(append,Y,Z))));
              generatedRules.add(Rule(_appl(reverse,Nil()), Nil()));
              generatedRules.add(Rule(_appl(reverse,Cons(X,Y)), _appl(append,_appl(reverse,Y),X)));
              generatedRules.add(Rule(_appl(rconcat,Nil(),Z), Z));
              generatedRules.add(Rule(_appl(rconcat,Cons(X,Y),Z), _appl(rconcat,Y,Cons(X,Z))));

            }
          }
          strategySymbol = one;
        }}}}}
 // match
      this.strategySymbols.put(strat,strategySymbol);
      this.storedTRSs.put(strat,generatedRules);
    } // strategy not yet compiled

    // add the generated rules to the rules for the global strategy only if not already existing
    for(Rule rule:generatedRules) {
      if(!rules.contains(rule)) {
        rules.add(rule);
      }
    }
    return strategySymbol;
  }

  public static class ReplaceMuVar extends tom.library.sl.AbstractStrategyBasic {private  String  name;private  String  appl;public ReplaceMuVar( String  name,  String  appl) {super(tom_make_Identity());this.name=name;this.appl=appl;}public  String  getname() {return name;}public  String  getappl() {return appl;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Strat(v)) {return ((T)visit_Strat((( sa.rule.types.Strat )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  _visit_Strat( sa.rule.types.Strat  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Strat )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Strat  visit_Strat( sa.rule.types.Strat  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Strat(tom__arg)) {if (tom_is_sort_Strat((( sa.rule.types.Strat )tom__arg))) {if (tom_is_fun_sym_StratName((( sa.rule.types.Strat )(( sa.rule.types.Strat )tom__arg)))) {if (tom_equal_term_String(name, tom_get_slot_StratName_name((( sa.rule.types.Strat )tom__arg)))) {


        return tom_make_StratName(appl);
      }}}}}}return _visit_Strat(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ReplaceMuVar( String  t0,  String  t1) { return new ReplaceMuVar(t0,t1);}




  /*
   * Generate equality rules:
   * f(x_1,...,x_n) = g(y_1,...,y_m) -> False
   * f(x_1,...,x_n) = f(y_1,...,y_n) -> x_1=y1 ^ ... ^ x_n=y_n ^ true
   */
  private RuleList generateEquality(RuleList generatedRules) {
    Signature eSig = getExtractedSignature();
    Signature dummySig = new Signature();

    generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(And(True(),True()),True()),tom_empty_list_ConcRule()));
    generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(And(True(),False()),False()),tom_empty_list_ConcRule()));
    generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(And(False(),True()),False()),tom_empty_list_ConcRule()));
    generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(And(False(),False()),False()),tom_empty_list_ConcRule()));

    Set<String> symbolNames = eSig.getConstructors();
    for(String f:symbolNames) {
      for(String g:symbolNames) {
        int arf = eSig.getArity(f);
        int arg = eSig.getArity(g);
        Term[] a_lx = new Term[arf];
        Term[] a_rx = new Term[arg];
        for(int i=1 ; i<=arf ; i++) {
          a_lx[i-1] = Var("X_" + i);
        }
        for(int i=1 ; i<=arg ; i++) {
          a_rx[i-1] = Var("Y" + i);
        }
        if(!f.equals(g)) {
          /*
           * eq(f(x1,...,xn),g(y1,...,ym)) -> False
           */
          if(!Main.options.metalevel){
            generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(Eq(_appl(f,a_lx),_appl(g,a_rx)),False()),tom_empty_list_ConcRule()));
          }else{
            generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(Eq(Tools.metaEncodeConsNil(_appl(f,a_lx),dummySig),Tools.metaEncodeConsNil(_appl(g,a_rx),dummySig)),False()),tom_empty_list_ConcRule()));
          }
        } else {
          /*
           * eq(f(x1,...,xn),f(y1,...,yn)) -> True ^ eq(x1,y1) ^ ... ^ eq(xn,yn)
           */
          Term scond = True();
          for(int i=1 ; i<=arf ; i++) {
            scond = And(Eq(Var("X_" + i),Var("Y" + i)),scond);
          }
          if(!Main.options.metalevel){
            generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(Eq(_appl(f,a_lx),_appl(g,a_rx)),scond),tom_empty_list_ConcRule()));
          }else{
            // TODO: could be factorized
            generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(Eq(Tools.metaEncodeConsNil(_appl(f,a_lx),dummySig),Tools.metaEncodeConsNil(_appl(g,a_rx),dummySig)),Tools.metaEncodeVars(scond,dummySig)),tom_empty_list_ConcRule()));
          }
        }
      }
    }
    return generatedRules;
  }


  /**
   * generates encode/decode functions for metalevel
   * encode(f(x1,...,xn)) -> Appl(symb_f, Cons(encode(x1), ..., Cons(encode(xn),Nil())))
   **/
  private RuleList generateEncodeDecode(RuleList generatedRules) {
    Signature eSig = getExtractedSignature();
    String x = Tools.getName("X");
    Set<String> symbolNames = eSig.getConstructors();
    for(String f:symbolNames) {
      int arf = eSig.getArity(f);

      Term args_encode = Nil();
      Term largs_decode = Nil();
      TermList rargs_decode = tom_empty_list_TermList();

      for(int i=arf ; i>=1 ; i--) {
        Term var = Var(x+"_"+i);
        args_encode = Cons(_appl(Signature.ENCODE,var),args_encode);
        largs_decode = Cons(var,largs_decode);
        rargs_decode = tom_cons_list_TermList(_appl(Signature.DECODE,var),tom_append_list_TermList(rargs_decode,tom_empty_list_TermList()));
      }

      Term lhs_encode = _appl(Signature.ENCODE, Tools.genAbstractTerm(f,arf,x));
      Term rhs_encode = Appl(_appl("symb_" + f), args_encode);

      Term lhs_decode = _appl(Signature.DECODE, Appl(_appl("symb_" + f), largs_decode));
      Term rhs_decode = tom_make_Appl(f,rargs_decode);

      generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(lhs_encode,rhs_encode),tom_empty_list_ConcRule()));
      generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(lhs_decode,rhs_decode),tom_empty_list_ConcRule()));
    }

    Term lhs_decode_bottom = _appl(Signature.DECODE, _appl(Signature.BOTTOM, Var(x)));
    Term rhs_decode_bottom = _appl(Signature.FAIL, _appl(Signature.DECODE, Var(x)));

    generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(tom_make_Rule(lhs_decode_bottom,rhs_decode_bottom),tom_empty_list_ConcRule()));

    return generatedRules;
  }


  /**
   * generates a rule of the form name(X) -> symbol(X)
   * name can be then see as an alias for symbol in terms of reduction
   **/
  private void generateTriggerRule(String name, String symbol, List<Rule> generatedRules) {
    Signature gSig = getGeneratedSignature();
    GomType codomain = gSig.getCodomain(symbol);
    GomTypeList domain = gSig.getDomain(symbol);
    gSig.addFunctionSymbol(name,domain,codomain);

    Term X = Var(Tools.getName("X"));
    /*
     * X matches anything but morally matches only symbols from the extracted signature
     * name(X) -> symbol(X)
     */
    generatedRules.add(tom_make_Rule(_appl(name,X),_appl(symbol,X)));
  }

  public static class CollectConstantStrategyName extends tom.library.sl.AbstractStrategyBasic {private  java.util.Set  c;public CollectConstantStrategyName( java.util.Set  c) {super(tom_make_Identity());this.c=c;}public  java.util.Set  getc() {return c;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_StratDecl(v)) {return ((T)visit_StratDecl((( sa.rule.types.StratDecl )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.StratDecl  _visit_StratDecl( sa.rule.types.StratDecl  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.StratDecl )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.StratDecl  visit_StratDecl( sa.rule.types.StratDecl  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_StratDecl(tom__arg)) {if (tom_is_sort_StratDecl((( sa.rule.types.StratDecl )tom__arg))) {if (tom_is_fun_sym_StratDecl((( sa.rule.types.StratDecl )(( sa.rule.types.StratDecl )tom__arg)))) { sa.rule.types.ParamList  tomMatch14_2=tom_get_slot_StratDecl_ParamList((( sa.rule.types.StratDecl )tom__arg));if (tom_is_fun_sym_ConcParam((( sa.rule.types.ParamList )tomMatch14_2))) {if (tom_is_empty_ConcParam_ParamList(tomMatch14_2)) {


        c.add(tom_get_slot_StratDecl_Name((( sa.rule.types.StratDecl )tom__arg)));
      }}}}}}}return _visit_StratDecl(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectConstantStrategyName( java.util.Set  t0) { return new CollectConstantStrategyName(t0);}



  public Set<String> collectConstantStrategyName(Program program) {
    Set<String> res = new HashSet<String>();
    try {
      tom_make_TopDown(tom_make_CollectConstantStrategyName(res)).visitLight(program);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + program);
    }
    return res;
  }

}
