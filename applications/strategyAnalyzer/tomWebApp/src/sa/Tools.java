package sa;

import sa.rule.types.*;
import java.util.*;
import tom.library.sl.*;
/*import aterm.*;
import aterm.pure.*;*/
import com.google.common.collect.HashMultiset;

public class Tools {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_StratDecl( sa.rule.types.StratDecl  t) {return  (t instanceof sa.rule.types.stratdecl.StratDecl) ;}private static  String  tom_get_slot_StratDecl_Name( sa.rule.types.StratDecl  t) {return  t.getName() ;}private static  sa.rule.types.ParamList  tom_get_slot_StratDecl_ParamList( sa.rule.types.StratDecl  t) {return  t.getParamList() ;}private static  sa.rule.types.Strat  tom_get_slot_StratDecl_Body( sa.rule.types.StratDecl  t) {return  t.getBody() ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Var( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Var) ;}private static  sa.rule.types.Term  tom_make_Var( String  t0) { return  sa.rule.types.term.Var.make(t0) ;}private static  String  tom_get_slot_Var_name( sa.rule.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_Anti( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Anti) ;}private static  sa.rule.types.Term  tom_make_Anti( sa.rule.types.Term  t0) { return  sa.rule.types.term.Anti.make(t0) ;}private static  sa.rule.types.Term  tom_get_slot_Anti_term( sa.rule.types.Term  t) {return  t.getterm() ;}private static boolean tom_is_fun_sym_At( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.At) ;}private static  sa.rule.types.Term  tom_make_At( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.At.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_At_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_At_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Sub( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Sub) ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Otrs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Otrs) ;}private static  sa.rule.types.RuleList  tom_get_slot_Otrs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static boolean tom_is_fun_sym_Trs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Trs) ;}private static  sa.rule.types.RuleList  tom_get_slot_Trs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static  sa.rule.types.Rule  tom_make_Rule( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.rule.Rule.make(t0, t1) ;}private static boolean tom_is_fun_sym_Program( sa.rule.types.Program  t) {return  (t instanceof sa.rule.types.program.Program) ;}private static  sa.rule.types.ProductionList  tom_get_slot_Program_productionList( sa.rule.types.Program  t) {return  t.getproductionList() ;}private static  sa.rule.types.ProductionList  tom_get_slot_Program_functionList( sa.rule.types.Program  t) {return  t.getfunctionList() ;}private static  sa.rule.types.StratDeclList  tom_get_slot_Program_stratList( sa.rule.types.Program  t) {return  t.getstratList() ;}private static  sa.rule.types.Trs  tom_get_slot_Program_trs( sa.rule.types.Program  t) {return  t.gettrs() ;}private static boolean tom_is_fun_sym_ConcStratDecl( sa.rule.types.StratDeclList  t) {return  ((t instanceof sa.rule.types.stratdecllist.ConsConcStratDecl) || (t instanceof sa.rule.types.stratdecllist.EmptyConcStratDecl)) ;}private static  sa.rule.types.StratDeclList  tom_empty_list_ConcStratDecl() { return  sa.rule.types.stratdecllist.EmptyConcStratDecl.make() ;}private static  sa.rule.types.StratDeclList  tom_cons_list_ConcStratDecl( sa.rule.types.StratDecl  e,  sa.rule.types.StratDeclList  l) { return  sa.rule.types.stratdecllist.ConsConcStratDecl.make(e,l) ;}private static  sa.rule.types.StratDecl  tom_get_head_ConcStratDecl_StratDeclList( sa.rule.types.StratDeclList  l) {return  l.getHeadConcStratDecl() ;}private static  sa.rule.types.StratDeclList  tom_get_tail_ConcStratDecl_StratDeclList( sa.rule.types.StratDeclList  l) {return  l.getTailConcStratDecl() ;}private static boolean tom_is_empty_ConcStratDecl_StratDeclList( sa.rule.types.StratDeclList  l) {return  l.isEmptyConcStratDecl() ;}   private static   sa.rule.types.StratDeclList  tom_append_list_ConcStratDecl( sa.rule.types.StratDeclList l1,  sa.rule.types.StratDeclList  l2) {     if( l1.isEmptyConcStratDecl() ) {       return l2;     } else if( l2.isEmptyConcStratDecl() ) {       return l1;     } else if(  l1.getTailConcStratDecl() .isEmptyConcStratDecl() ) {       return  sa.rule.types.stratdecllist.ConsConcStratDecl.make( l1.getHeadConcStratDecl() ,l2) ;     } else {       return  sa.rule.types.stratdecllist.ConsConcStratDecl.make( l1.getHeadConcStratDecl() ,tom_append_list_ConcStratDecl( l1.getTailConcStratDecl() ,l2)) ;     }   }   private static   sa.rule.types.StratDeclList  tom_get_slice_ConcStratDecl( sa.rule.types.StratDeclList  begin,  sa.rule.types.StratDeclList  end, sa.rule.types.StratDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcStratDecl()  ||  (end==tom_empty_list_ConcStratDecl()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.stratdecllist.ConsConcStratDecl.make( begin.getHeadConcStratDecl() ,( sa.rule.types.StratDeclList )tom_get_slice_ConcStratDecl( begin.getTailConcStratDecl() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())),tom_empty_list_Sequence()))) );}   

  /*%include { aterm.tom }*/
  private static boolean tom_equal_term_Collection(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Collection(Object t) {return  t instanceof java.util.Collection ;} private static boolean tom_equal_term_Map(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Map(Object t) {return  t instanceof java.util.Map ;} private static boolean tom_equal_term_HashSet(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_HashSet(Object t) {return  t instanceof java.util.HashSet ;} private static boolean tom_equal_term_Set(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Set(Object t) {return  t instanceof java.util.Set ;} 







  private static final String AUX = "Aux";   // extension for auxiliary symbols
  private static int phiNumber = 0;          // sequence number for symbol (names)

  /*** helpers to build and decompose symbol names ***/

  /**
   * Builds a unique symbol (name)
   */
  public static String getName(String name) {
    return name + "_" + (phiNumber++);
  }

  public static boolean isGeneratedVariableName(String name) {
    return name.contains("_");
  }

  /**
   * Given symbolName and operatorName
   * returns symbolName-operatorName
   */
  public static String addOperatorName(String symbolName, String operatorName) {
    return symbolName + "-" + operatorName;
  }

  /**
   * given symbolName and typeName
   * returns symbolName_typeName
   */
  public static String addTypeName(String symbol, String typeName) {
    return symbol + "_" + typeName;
  }

  /**
   * Given symbolName
   * returns symbolNameAUX
   */
  public static String addAuxExtension(String symbol) {
    return symbol + AUX;
  }

  /**
   * A symbol is of the form:
   * - symbolName[AUX]
   * - symbolName[AUX][_<number>]
   * - symbolName[AUX][_<number>]_typeName
   * - symbolName[AUX][_<number>]-operatorName[_<number>]_typeName
   * @returns symbolName[AUX]
   */
  public static String getSymbolName(String symbol) {
    int last = symbol.indexOf('_');
    if(last == -1) {
      return symbol; //last=symbol.length();
    }
    return symbol.substring(0,last);
  }

  /**
   * Determines if auxiliary symbol
   * @returns true if of the form symbolNameAUX; false otherwise
   */
  public static boolean isSymbolNameAux(String symbol) {
    //boolean res = false;
    //String name = getSymbolName(symbol);
    //if(name.length() > AUX.length()) {
    //  res = name.substring(name.length()-AUX.length(),name.length()).equals(AUX);
    //}
    //return res;
    return getSymbolName(symbol).endsWith(AUX);
  }

  /**
   * Retuns the name of the strategy it has been generated for (strips of the AUX)
   * @returns symbolName
   */
  public static String getSymbolNameMain(String symbol) {
    String name = getSymbolName(symbol);
    if(isSymbolNameAux(symbol)) {
      name = name.substring(0,name.length()-AUX.length());
    }
    return name;
  }

  /**
   * A symbol is expected to be of the form:
   * - symbolName[AUX][_<number>]_typeName
   * - symbolName[AUX][_<number>]-operatorName[_<number>]_typeName
   * @returns symbolName[AUX]
   */
  public static String getTypeName(String symbol) {
    int last = symbol.lastIndexOf('_');
    if(last == -1) { // nothing if type not specified in the symbol name
      return "";
    }
    return symbol.substring(last+1,symbol.length());
  }

  /**
   * Given a symbol name of the form
   * - symbolName[AUX][_<number>]-operatorName[_<number>][_typeName]
   * @returns operatorName
   */
  public static String getOperatorName(String symbol) {
    String aux = null;
    int last = symbol.indexOf('-');
    if(last != -1) { // if containing a composite
      int funLast = symbol.indexOf('_',last+1);
      if(funLast == -1) { //  if no other information after composite
        funLast = symbol.length();
      }
      aux = symbol.substring(last+1,funLast);
    }
    return aux;
  }

  private static boolean isVariableName(String name, Signature signature) {
    return signature.getCodomain(name) == null;
  }


  /*** helpers to build AST ***/
  public static Term Var(String name) { return tom_make_Var(name); }
  public static Term Anti(Term t) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

        Term antiterm = Anti(tom_get_slot_At_term2((( sa.rule.types.Term )t)));
        return tom_make_At(tom_get_slot_At_term1((( sa.rule.types.Term )t)),antiterm);
      }}}}}

    return tom_make_Anti(t);
  }
  public static Term At(Term t1, Term t2) { return tom_make_At(t1,t2); }
  public static Term Bottom(Term t) { return _appl(Signature.BOTTOM,t); }
  public static Term Bottom2(Term t1,Term t2) { return _appl(Signature.BOTTOM2,t1,t2); }
  public static Term BottomList(Term t) { return _appl(Signature.BOTTOMLIST,t); }
  public static Term True() { return _appl(Signature.TRUE); }
  public static Term False() { return _appl(Signature.FALSE); }
  public static Term And(Term t1, Term t2) { return _appl(Signature.AND,t1,t2); }
  public static Term Eq(Term t1, Term t2) { return _appl(Signature.EQ,t1,t2); }
  public static Term Appl(Term t1, Term t2) { return _appl(Signature.APPL,t1,t2); }
  public static Rule Rule(Term lhs, Term rhs) { return tom_make_Rule(lhs,rhs); }
  public static Term Nil() { return _appl(Signature.NIL); }
  public static Term Cons(Term t1, Term t2) { return _appl(Signature.CONS,t1,t2); }
  public static Term _appl(String name, Term... args) {
    TermList tl = tom_empty_list_TermList();
    for(Term t:args) {
      tl = tom_append_list_TermList(tl,tom_cons_list_TermList(t,tom_empty_list_TermList()));
    }
    return tom_make_Appl(name,tl);
  }


  /**
    * metaVars: add a "var_" to all variables
    * used to have the same name for variable in the conditions generated when linearing a term
    */
  public static Term metaEncodeVars(Term t, Signature signature) {
    if(Main.options.metalevel) {
      return encodeVars(t,signature);
    } else {
      throw new RuntimeException("metaEncodeVars can only be used with meta-level active");
    }
  }

  private static Term encodeVars(Term t, Signature signature) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

        return tom_make_Appl(tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)),encodeVars(tom_get_slot_Appl_args((( sa.rule.types.Term )t)),signature));
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return Var("var_"+tom_get_slot_Var_name((( sa.rule.types.Term )t)));
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return Anti(encodeVars(tom_get_slot_Anti_term((( sa.rule.types.Term )t)),signature));
      }}}}}

    return null;
  }

  private static TermList encodeVars(TermList t, Signature signature) {
    {{if (tom_is_sort_TermList(t)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )t)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )t)))) {

        Term he = encodeVars(tom_get_head_TermList_TermList((( sa.rule.types.TermList )t)),signature);
        TermList ta = encodeVars(tom_get_tail_TermList_TermList((( sa.rule.types.TermList )t)),signature);
        return tom_cons_list_TermList(he,tom_append_list_TermList(ta,tom_empty_list_TermList()));
      }}}}{if (tom_is_sort_TermList(t)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )t)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )t))) {

        return tom_empty_list_TermList();
      }}}}}

    return null;
  }

  /**
    * metaEncodeConsNil: transforms a Term representation into a generic term representation
    * for instance, the term f(b()) (implemented by Appl("f",TermList(Appl("b",TermList()))))
    * is transformed into the term Appl(symb_f,Cons(Appl(symb_b,Nil()),Nil()))
    * implemented by Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
    */
  public static Term metaEncodeConsNil(Term t, Signature signature) {
    if(Main.options.metalevel) {
      return encodeConsNil(t,signature);
    } else {
      throw new RuntimeException("metaEncodeConsNil can only be used with meta-level active");
    }
  }

  private static Term encodeConsNil(Term t, Signature signature) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

        String symbName = "symb_" + tom_get_slot_Appl_symbol((( sa.rule.types.Term )t));
        signature.addSymbol(symbName,tom_empty_list_ConcGomType(),Signature.TYPE_METASYMBOL);
        return Appl(_appl(symbName), encodeConsNil(tom_get_slot_Appl_args((( sa.rule.types.Term )t)),signature));
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return Var("var_"+tom_get_slot_Var_name((( sa.rule.types.Term )t)));
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return Anti(encodeConsNil(tom_get_slot_Anti_term((( sa.rule.types.Term )t)),signature));
      }}}}}

    return null;
  }

  private static Term encodeConsNil(TermList t, Signature signature) {
    {{if (tom_is_sort_TermList(t)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )t)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )t)))) {

        return Cons(encodeConsNil(tom_get_head_TermList_TermList((( sa.rule.types.TermList )t)),signature),encodeConsNil(tom_get_tail_TermList_TermList((( sa.rule.types.TermList )t)),signature));
      }}}}{if (tom_is_sort_TermList(t)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )t)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )t))) {

        return Nil();
      }}}}}

    return null;
  }

  /*
   * go from meta-level to term level
   * Appl("Appl",TermList(Appl("symb_f",TermList()),Appl("Cons",TermList(Appl("Appl",TermList(Appl("symb_a",TermList()),Appl("Nil",TermList()))),Appl("Nil",TermList())))))
   * is decoded to Appl("f",TermList(Appl("b",TermList())))
   *
   */
  public static Term metaDecodeConsNil(Term t) {
    if(Main.options.metalevel) {
      return decodeConsNil(t);
    } else {
      throw new RuntimeException("metaDecodeConsNil can only be used with meta-level active");
    }
  }

  private static Term decodeConsNil(Term t) {
    //System.out.println("IN DECODE = "+ `t);
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tomMatch89_1=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)); sa.rule.types.TermList  tomMatch89_2=tom_get_slot_Appl_args((( sa.rule.types.Term )t));if ( true ) {if (tom_equal_term_String("Appl", tomMatch89_1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch89_2))) {if (!(tom_is_empty_TermList_TermList(tomMatch89_2))) { sa.rule.types.Term  tomMatch89_12=tom_get_head_TermList_TermList(tomMatch89_2);if (tom_is_sort_Term(tomMatch89_12)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch89_12))) { sa.rule.types.TermList  tomMatch89_11=tom_get_slot_Appl_args(tomMatch89_12);if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch89_11))) {if (tom_is_empty_TermList_TermList(tomMatch89_11)) { sa.rule.types.TermList  tomMatch89_8=tom_get_tail_TermList_TermList(tomMatch89_2);if (!(tom_is_empty_TermList_TermList(tomMatch89_8))) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch89_8))) {

          String name = tom_get_slot_Appl_symbol(tomMatch89_12).substring("symb_".length());
          return tom_make_Appl(name,decodeConsNilList(tom_get_head_TermList_TermList(tomMatch89_8)));
      }}}}}}}}}}}}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        String name = tom_get_slot_Var_name((( sa.rule.types.Term )t)).substring("var_".length());
        return tom_make_Var(name);
      }}}}}

    // identity to not decode an already decoded term
    return t;
  }

  private static TermList decodeConsNilList(Term t) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tomMatch90_1=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)); sa.rule.types.TermList  tomMatch90_2=tom_get_slot_Appl_args((( sa.rule.types.Term )t));if ( true ) {if (tom_equal_term_String("Cons", tomMatch90_1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch90_2))) {if (!(tom_is_empty_TermList_TermList(tomMatch90_2))) { sa.rule.types.TermList  tomMatch90_8=tom_get_tail_TermList_TermList(tomMatch90_2);if (!(tom_is_empty_TermList_TermList(tomMatch90_8))) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch90_8))) {

        TermList newTail = decodeConsNilList(tom_get_head_TermList_TermList(tomMatch90_8));
        return tom_cons_list_TermList(decodeConsNil(tom_get_head_TermList_TermList(tomMatch90_2)),tom_append_list_TermList(newTail,tom_empty_list_TermList()));
      }}}}}}}}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tomMatch90_11=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)); sa.rule.types.TermList  tomMatch90_12=tom_get_slot_Appl_args((( sa.rule.types.Term )t));if ( true ) {if (tom_equal_term_String("Nil", tomMatch90_11)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch90_12))) {if (tom_is_empty_TermList_TermList(tomMatch90_12)) {


        return tom_empty_list_TermList();
      }}}}}}}}}

    throw new RuntimeException("should not be there");
  }

  /*
   * generate a term for the form f(Z1,...,Zn)
   * @param name the symbol name
   * @param arity the arity of the symbol
   * @return the Term that represents the term
   */
  public static Term genAbstractTerm(String name, int arity, String varname) {
    TermList args = tom_empty_list_TermList();
    for(int i=arity ; i>= 1 ; i--) {
      Term var = tom_make_Var(varname+"_"+i);
      args = tom_cons_list_TermList(var,tom_append_list_TermList(args,tom_empty_list_TermList()));
    }
    return tom_make_Appl(name,args);
  }

  /*
   * tools for manipulating Program
   */
  public static StratDecl getStratDecl(String name, Program program) {
    {{if (tom_is_sort_Program(program)) {if (tom_is_sort_Program((( sa.rule.types.Program )program))) {if (tom_is_fun_sym_Program((( sa.rule.types.Program )(( sa.rule.types.Program )program)))) { sa.rule.types.StratDeclList  tomMatch91_1=tom_get_slot_Program_stratList((( sa.rule.types.Program )program));if (tom_is_fun_sym_ConcStratDecl((( sa.rule.types.StratDeclList )tomMatch91_1))) { sa.rule.types.StratDeclList  tomMatch91_end_7=tomMatch91_1;do {{if (!(tom_is_empty_ConcStratDecl_StratDeclList(tomMatch91_end_7))) { sa.rule.types.StratDecl  tomMatch91_13=tom_get_head_ConcStratDecl_StratDeclList(tomMatch91_end_7);if (tom_is_sort_StratDecl(tomMatch91_13)) {if (tom_is_fun_sym_StratDecl((( sa.rule.types.StratDecl )tomMatch91_13))) {

        if(tom_get_slot_StratDecl_Name(tomMatch91_13).equals(name)) {
          return tom_get_head_ConcStratDecl_StratDeclList(tomMatch91_end_7);
        }
      }}}if (tom_is_empty_ConcStratDecl_StratDeclList(tomMatch91_end_7)) {tomMatch91_end_7=tomMatch91_1;} else {tomMatch91_end_7=tom_get_tail_ConcStratDecl_StratDeclList(tomMatch91_end_7);}}} while(!(tom_equal_term_StratDeclList(tomMatch91_end_7, tomMatch91_1)));}}}}}}

    return null;
  }

  /**
   * Transform lhs into linear-lhs + true ^ constraint on non linear variables
   */
  public static TermList linearize(Term lhs, Signature signature) {
    Map<String,String> mapToOldName = new HashMap<String,String>();
    HashMultiset<String> bag = collectVariableMultiplicity(lhs);

    Set<String> elements = new HashSet<String>(bag.elementSet());
    for(String name:elements) {
      if(bag.count(name) == 1) {
        bag.remove(name);
      }
    }

    try {
      lhs = tom_make_TopDown(tom_make_ReplaceWithFreshVar(signature,bag,mapToOldName)).visitLight(lhs);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }

    Term constraint = tom_make_Appl(Signature.TRUE,tom_empty_list_TermList());
    for(String name:mapToOldName.keySet()) {
      String oldName = mapToOldName.get(name);
      constraint = tom_make_Appl(Signature.AND,tom_cons_list_TermList(tom_make_Appl(Signature.EQ,tom_cons_list_TermList(tom_make_Var(oldName),tom_cons_list_TermList(tom_make_Var(name),tom_empty_list_TermList()))),tom_cons_list_TermList(constraint,tom_empty_list_TermList())));
    }
    return tom_cons_list_TermList(lhs,tom_cons_list_TermList(constraint,tom_empty_list_TermList()));

  }

  /*
   * Replace a named variable by an underscore
   */
  public static class RemoveVar extends tom.library.sl.AbstractStrategyBasic {public RemoveVar() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        return tom_make_Var("_");
      }}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        return tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_RemoveVar() { return new RemoveVar();}



  public static tom.library.sl.Visitable removeVar(tom.library.sl.Visitable t) {
    try {
      return tom_make_TopDown(tom_make_RemoveVar()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }

  public static class RemoveAt extends tom.library.sl.AbstractStrategyBasic {public RemoveAt() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        return tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_RemoveAt() { return new RemoveAt();}



  public static tom.library.sl.Visitable removeAt(tom.library.sl.Visitable t) {
    try {
      return tom_make_InnermostId(tom_make_RemoveAt()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }

  public static boolean isLinear(Term t) {
    HashMultiset<String> bag = collectVariableMultiplicity(t);
    for(String name:bag.elementSet()) {
      if(bag.count(name) > 1) {
        return false;
      }
    }
    return true;
  }

  public static boolean isLhsLinear(RuleList rules) {
    boolean res = true;
    for(Rule r: rules.getCollectionConcRule()) {
      res &= isLinear(r.getlhs());
    }
    return res;
  }

  public static boolean isLhsLinear(Trs trs) {
    boolean res = true;
    {{if (tom_is_sort_Trs(trs)) {boolean tomMatch94_5= false ; sa.rule.types.RuleList  tomMatch94_1= null ; sa.rule.types.Trs  tomMatch94_3= null ; sa.rule.types.Trs  tomMatch94_4= null ;if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Trs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) {{tomMatch94_5= true ;tomMatch94_3=(( sa.rule.types.Trs )trs);tomMatch94_1=tom_get_slot_Trs_list(tomMatch94_3);}} else {if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Otrs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) {{tomMatch94_5= true ;tomMatch94_4=(( sa.rule.types.Trs )trs);tomMatch94_1=tom_get_slot_Otrs_list(tomMatch94_4);}}}}}if (tomMatch94_5) {

        res = isLhsLinear(tomMatch94_1);
      }}}}

    return res;
  }

  public static boolean containsNamedVar(tom.library.sl.Visitable t) {
    HashMultiset<String> bag = collectVariableMultiplicity(t);
    for(String name:bag.elementSet()) {
      if(name != "_") {
        return true;
      }
    }

    return false;
  }

  public static boolean containsAt(tom.library.sl.Visitable t) {
    HashMultiset<Term> bag = HashMultiset.create();
    try {
      tom_make_TopDown(tom_make_CollectAt(bag)).visitLight(t);
    } catch(VisitFailure e) {
    }
     return !bag.isEmpty();
  }

  // search all At symbols
  public static class CollectAt extends tom.library.sl.AbstractStrategyBasic {private  HashMultiset  bag;public CollectAt( HashMultiset  bag) {super(tom_make_Identity());this.bag=bag;}public  HashMultiset  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        bag.add((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectAt( HashMultiset  t0) { return new CollectAt(t0);}



  public static boolean containsAP(tom.library.sl.Visitable t) {
    HashMultiset<Term> bag = HashMultiset.create();
    try {
      tom_make_TopDown(tom_make_CollectAP(bag)).visitLight(t);
    } catch(VisitFailure e) {
    }
     return !bag.isEmpty();
  }

  // search all APs
  public static class CollectAP extends tom.library.sl.AbstractStrategyBasic {private  HashMultiset  bag;public CollectAP( HashMultiset  bag) {super(tom_make_Identity());this.bag=bag;}public  HashMultiset  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        bag.add((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectAP( HashMultiset  t0) { return new CollectAP(t0);}




  public static boolean containsEqAnd(tom.library.sl.Visitable t) {
    try {
      tom_make_TopDown(tom_make_ContainsEqAnd()).visitLight(t);
      return false;
    } catch(VisitFailure e) {
      return true;
    }
  }

  public static class ContainsEqAnd extends tom.library.sl.AbstractStrategyBasic {public ContainsEqAnd() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { String  tomMatch97_1=tom_get_slot_Appl_symbol((( sa.rule.types.Term )tom__arg));if ( true ) {if (tom_equal_term_String("eq", tomMatch97_1)) {tom_make_Fail()


.visitLight((( sa.rule.types.Term )tom__arg));
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { String  tomMatch97_8=tom_get_slot_Appl_symbol((( sa.rule.types.Term )tom__arg));if ( true ) {if (tom_equal_term_String("and", tomMatch97_8)) {tom_make_Fail()


.visitLight((( sa.rule.types.Term )tom__arg));
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ContainsEqAnd() { return new ContainsEqAnd();}



  public static boolean containsSub(tom.library.sl.Visitable t) {
    try {
      tom_make_TopDown(tom_make_ContainsSub()).visitLight(t);
      return false;
    } catch(VisitFailure e) {
      return true;
    }
  }

  public static class ContainsSub extends tom.library.sl.AbstractStrategyBasic {public ContainsSub() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {tom_make_Fail()


.visitLight((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ContainsSub() { return new ContainsSub();}




  /**
   * Returns a Map which associates to each variable name an integer
   * representing the number of occurences of the variable in the
   * (Visitable) Term
   */
  private static HashMultiset<String> collectVariableMultiplicity(tom.library.sl.Visitable subject) {
    // collect variables
    HashMultiset<String> bag = HashMultiset.create();
    try {
      tom_make_TopDown(tom_make_CollectVars(bag)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }

    return bag;
  }

  // search all Var and store their values
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  bag;public CollectVars( java.util.Collection  bag) {super(tom_make_Identity());this.bag=bag;}public  java.util.Collection  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        bag.add(tom_get_slot_Var_name((( sa.rule.types.Term )tom__arg)));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVars( java.util.Collection  t0) { return new CollectVars(t0);}



  // for Main.options.metalevel we need the (generated)signature
  public static class ReplaceWithFreshVar extends tom.library.sl.AbstractStrategyBasic {private  Signature  signature;private  HashMultiset  bag;private  java.util.Map  map;public ReplaceWithFreshVar( Signature  signature,  HashMultiset  bag,  java.util.Map  map) {super(tom_make_Identity());this.signature=signature;this.bag=bag;this.map=map;}public  Signature  getsignature() {return signature;}public  HashMultiset  getbag() {return bag;}public  java.util.Map  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { String  tom_n=tom_get_slot_Var_name((( sa.rule.types.Term )tom__arg));


        if(bag.count(tom_n) > 1) {
          bag.remove(tom_n);
          String z = Tools.getName("Z");
          map.put(z,tom_n);
          Term newt = tom_make_Var(z);
          // HC: why should we encode it?
//           if(Main.options.metalevel) {
//             newt = Tools.metaEncodeConsNil(newt,signature);
//           }
          return newt;
        }
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ReplaceWithFreshVar( Signature  t0,  HashMultiset  t1,  java.util.Map  t2) { return new ReplaceWithFreshVar(t0,t1,t2);}



  public static RuleList fromListOfRule(List<Rule> l) {
    RuleList res = tom_empty_list_ConcRule();
    for(Rule r:l) {
      res = tom_cons_list_ConcRule(r,tom_append_list_ConcRule(res,tom_empty_list_ConcRule()));
    }
    return res.reverse();
  }

  /*
   * rename variables into x1,...,xn (using a topdown traversal)
   */
  public static Term normalizeVariable(Term subject) {
    ArrayList<String> list = new ArrayList<String>();
    Map<String,String> mapToNewName = new HashMap<String,String>();
    int cpt = 0;
    try {
      tom_make_TopDown(tom_make_CollectVars(list)).visitLight(subject);
      for(String name:list) {

        if(mapToNewName.get(name) == null) {
          String newName = "x_" + (cpt++);
          mapToNewName.put(name,newName);
        }
      }
      subject = tom_make_TopDown(tom_make_RenameVars(mapToNewName)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return subject;
  }

  public static class RenameVars extends tom.library.sl.AbstractStrategyBasic {private  java.util.Map  map;public RenameVars( java.util.Map  map) {super(tom_make_Identity());this.map=map;}public  java.util.Map  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { String  tom_n=tom_get_slot_Var_name((( sa.rule.types.Term )tom__arg));


        if(isGeneratedVariableName(tom_n)) {
          String newName = (String) map.get(tom_n);
          if(newName != null) {
            return tom_make_Var(newName);
          }
        }
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_RenameVars( java.util.Map  t0) { return new RenameVars(t0);}



}
