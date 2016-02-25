package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class RewriteSystem {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Var( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Var) ;}private static  sa.rule.types.Term  tom_make_Var( String  t0) { return  sa.rule.types.term.Var.make(t0) ;}private static  String  tom_get_slot_Var_name( sa.rule.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_Anti( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Anti) ;}private static  sa.rule.types.Term  tom_make_Anti( sa.rule.types.Term  t0) { return  sa.rule.types.term.Anti.make(t0) ;}private static  sa.rule.types.Term  tom_get_slot_Anti_term( sa.rule.types.Term  t) {return  t.getterm() ;}private static boolean tom_is_fun_sym_At( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.At) ;}private static  sa.rule.types.Term  tom_make_At( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.At.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_At_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_At_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Add( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Add) ;}private static  sa.rule.types.Term  tom_make_Add( sa.rule.types.AddList  t0) { return  sa.rule.types.term.Add.make(t0) ;}private static  sa.rule.types.AddList  tom_get_slot_Add_addlist( sa.rule.types.Term  t) {return  t.getaddlist() ;}private static boolean tom_is_fun_sym_Sub( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Sub) ;}private static  sa.rule.types.Term  tom_make_Sub( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.Sub.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Inter( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Inter) ;}private static  sa.rule.types.Term  tom_make_Inter( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.Inter.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Inter_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Inter_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Match( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Match) ;}private static  sa.rule.types.Term  tom_make_Match( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.Match.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Match_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Match_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_TrueMatch( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.TrueMatch) ;}private static  sa.rule.types.Term  tom_make_TrueMatch() { return  sa.rule.types.term.TrueMatch.make() ;}private static boolean tom_is_fun_sym_Empty( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Empty) ;}private static  sa.rule.types.Term  tom_make_Empty() { return  sa.rule.types.term.Empty.make() ;}private static boolean tom_is_fun_sym_Otrs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Otrs) ;}private static  sa.rule.types.Trs  tom_make_Otrs( sa.rule.types.RuleList  t0) { return  sa.rule.types.trs.Otrs.make(t0) ;}private static  sa.rule.types.RuleList  tom_get_slot_Otrs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static boolean tom_is_fun_sym_Trs( sa.rule.types.Trs  t) {return  (t instanceof sa.rule.types.trs.Trs) ;}private static  sa.rule.types.Trs  tom_make_Trs( sa.rule.types.RuleList  t0) { return  sa.rule.types.trs.Trs.make(t0) ;}private static  sa.rule.types.RuleList  tom_get_slot_Trs_list( sa.rule.types.Trs  t) {return  t.getlist() ;}private static boolean tom_is_fun_sym_Rule( sa.rule.types.Rule  t) {return  (t instanceof sa.rule.types.rule.Rule) ;}private static  sa.rule.types.Rule  tom_make_Rule( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.rule.Rule.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Rule_lhs( sa.rule.types.Rule  t) {return  t.getlhs() ;}private static  sa.rule.types.Term  tom_get_slot_Rule_rhs( sa.rule.types.Rule  t) {return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConcAdd( sa.rule.types.AddList  t) {return  ((t instanceof sa.rule.types.addlist.ConsConcAdd) || (t instanceof sa.rule.types.addlist.EmptyConcAdd)) ;}private static  sa.rule.types.AddList  tom_empty_list_ConcAdd() { return  sa.rule.types.addlist.EmptyConcAdd.make() ;}private static  sa.rule.types.AddList  tom_cons_list_ConcAdd( sa.rule.types.Term  e,  sa.rule.types.AddList  l) { return  sa.rule.types.addlist.ConsConcAdd.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getHeadConcAdd() ;}private static  sa.rule.types.AddList  tom_get_tail_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getTailConcAdd() ;}private static boolean tom_is_empty_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.isEmptyConcAdd() ;}   private static   sa.rule.types.AddList  tom_append_list_ConcAdd( sa.rule.types.AddList l1,  sa.rule.types.AddList  l2) {     if( l1.isEmptyConcAdd() ) {       return l2;     } else if( l2.isEmptyConcAdd() ) {       return l1;     } else if(  l1.getTailConcAdd() .isEmptyConcAdd() ) {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,l2) ;     } else {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,tom_append_list_ConcAdd( l1.getTailConcAdd() ,l2)) ;     }   }   private static   sa.rule.types.AddList  tom_get_slice_ConcAdd( sa.rule.types.AddList  begin,  sa.rule.types.AddList  end, sa.rule.types.AddList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAdd()  ||  (end==tom_empty_list_ConcAdd()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.addlist.ConsConcAdd.make( begin.getHeadConcAdd() ,( sa.rule.types.AddList )tom_get_slice_ConcAdd( begin.getTailConcAdd() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_make_Try(tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence())))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())),tom_empty_list_Sequence()))) );}   private static boolean tom_equal_term_HashSet(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_HashSet(Object t) {return  t instanceof java.util.HashSet ;} 




  private static long timeSubElim;
  private static long timeAddElim;
  private static long timeExpand;
  private static long timeSubsumtion;
  private static long timeMinimize;

  private static void debug(String ruleName, Term input, Term res) {
    if(Main.options.debug && Main.options.verbose) {
      debugVerbose(ruleName,input,res);
    } else if(Main.options.debug) {
      System.out.println(ruleName);
    }

  }

  private static void debugVerbose(String ruleName, Term input, Term res) {
    System.out.println(ruleName + ": " + Pretty.toString(input) + " --> " + Pretty.toString(res));
  }

  /*
   * Transform a list of ordered rules into a TRS; rule by rule
   * input can contains: linear lhs rules with nested AP
   * result is LINEAR, without AP
   */
  public static Trs trsRule(Trs trs, Signature eSig) {
    assert Tools.isLhsLinear(trs) : "check lhs-linear";
    boolean ordered = trs.isOtrs();
    long startChrono;

    RuleList res = tom_empty_list_ConcRule();
    {{if (tom_is_sort_Trs(trs)) {boolean tomMatch24_15= false ; sa.rule.types.RuleList  tomMatch24_1= null ; sa.rule.types.Trs  tomMatch24_3= null ; sa.rule.types.Trs  tomMatch24_4= null ;if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Trs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) {{tomMatch24_15= true ;tomMatch24_3=(( sa.rule.types.Trs )trs);tomMatch24_1=tom_get_slot_Trs_list(tomMatch24_3);}} else {if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Otrs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) {{tomMatch24_15= true ;tomMatch24_4=(( sa.rule.types.Trs )trs);tomMatch24_1=tom_get_slot_Otrs_list(tomMatch24_4);}}}}}if (tomMatch24_15) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )tomMatch24_1))) { sa.rule.types.RuleList  tomMatch24_end_8=tomMatch24_1;do {{ sa.rule.types.RuleList  tom_C1=tom_get_slice_ConcRule(tomMatch24_1,tomMatch24_end_8,tom_empty_list_ConcRule());if (!(tom_is_empty_ConcRule_RuleList(tomMatch24_end_8))) { sa.rule.types.Rule  tomMatch24_13=tom_get_head_ConcRule_RuleList(tomMatch24_end_8);if (tom_is_sort_Rule(tomMatch24_13)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch24_13))) { sa.rule.types.Term  tom_rhs=tom_get_slot_Rule_rhs(tomMatch24_13);

        // build the collection of all prev = l1+l2+...+ln   before  rule=Rule(lhs,rhs)
        // the lhs of rule in the unordered TRS = lhs - prev
        AddList prev = tom_empty_list_ConcAdd();
        if(ordered) {
          {{if (tom_is_sort_RuleList(tom_C1)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )tom_C1)))) { sa.rule.types.RuleList  tomMatch25_end_4=(( sa.rule.types.RuleList )tom_C1);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch25_end_4))) { sa.rule.types.Rule  tomMatch25_9=tom_get_head_ConcRule_RuleList(tomMatch25_end_4);if (tom_is_sort_Rule(tomMatch25_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch25_9))) {

              prev = tom_cons_list_ConcAdd(tom_get_slot_Rule_lhs(tomMatch25_9),tom_append_list_ConcAdd(prev,tom_empty_list_ConcAdd()));
            }}}if (tom_is_empty_ConcRule_RuleList(tomMatch25_end_4)) {tomMatch25_end_4=(( sa.rule.types.RuleList )tom_C1);} else {tomMatch25_end_4=tom_get_tail_ConcRule_RuleList(tomMatch25_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch25_end_4, (( sa.rule.types.RuleList )tom_C1))));}}}}

        }
        Term pattern = tom_make_Sub(tom_get_slot_Rule_lhs(tomMatch24_13),tom_make_Add(prev));
        if(Main.options.verbose) {
          System.out.println("\nPATTERN : " + Pretty.toString(pattern));
        }

        // transform lhs - prev into a collection of patterns matching exactly the same terms
        //Term t = `reduce(pattern,eSig);
        Term t = pattern;


        try {
          // pre-treatment: remove AP
          t = tom_make_InnermostId(tom_make_ExpandAP()).visitLight(t);
        } catch(VisitFailure e) {
          System.out.println("failure on: " + t);
        }

        t = simplifySub(t,eSig);
        if(Main.options.verbose) {
          System.out.println("NO SUB = " + Pretty.toString(t));
        }

        startChrono = System.currentTimeMillis();
        t = simplifySubsumtion(t);
        timeSubsumtion += (System.currentTimeMillis()-startChrono);
        if(Main.options.verbose) {
          System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));
        }

        if(Main.options.minimize) {
          // test new idea
          // quite slow
          t = simplifyAbstraction(t,eSig);
          t = simplifySubsumtion(t);
        }
        //assert onlyTopLevelAdd(t) : "check only top-level Add";

        if(Main.options.verbose) {
          System.out.println("REDUCED : " + Pretty.toString(t));
        }

        RuleList rules = tom_empty_list_ConcRule();
        // put back the rhs
        boolean flag = false;
        {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { sa.rule.types.AddList  tomMatch26_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )t));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch26_1))) { sa.rule.types.AddList  tomMatch26_end_7=tomMatch26_1;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch26_end_7))) {

            rules = tom_append_list_ConcRule(rules,tom_cons_list_ConcRule(tom_make_Rule(tom_get_head_ConcAdd_AddList(tomMatch26_end_7),tom_rhs),tom_empty_list_ConcRule()));
            flag = true;
          }if (tom_is_empty_ConcAdd_AddList(tomMatch26_end_7)) {tomMatch26_end_7=tomMatch26_1;} else {tomMatch26_end_7=tom_get_tail_ConcAdd_AddList(tomMatch26_end_7);}}} while(!(tom_equal_term_AddList(tomMatch26_end_7, tomMatch26_1)));}}}}}{if (tom_is_sort_Term(t)) {boolean tomMatch26_15= false ; sa.rule.types.Term  tomMatch26_12= null ; sa.rule.types.Term  tomMatch26_14= null ; sa.rule.types.Term  tomMatch26_13= null ;if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {{tomMatch26_15= true ;tomMatch26_12=(( sa.rule.types.Term )t);}} else {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {{tomMatch26_15= true ;tomMatch26_13=(( sa.rule.types.Term )t);}} else {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {{tomMatch26_15= true ;tomMatch26_14=(( sa.rule.types.Term )t);}}}}}}}if (tomMatch26_15) {


            rules = tom_append_list_ConcRule(rules,tom_cons_list_ConcRule(tom_make_Rule((( sa.rule.types.Term )t),tom_rhs),tom_empty_list_ConcRule()));
            flag = true;
          }}}{if (tom_is_sort_Term(t)) {


            if(!flag) {
              System.out.println("WARNING NOT PUT BACK : " + Pretty.toString(t));
            }
          }}}


        // TODO: normalize name of variables
        // remove x@t
        RuleCompiler ruleCompiler = new RuleCompiler(eSig, eSig);
        rules = ruleCompiler.expandAt(rules);
        assert !Tools.containsAt(rules) : "check contain no AT";

          // minimize the set of rules
          startChrono = System.currentTimeMillis();
          rules = removeRedundantRule(rules,eSig);
          timeMinimize += (System.currentTimeMillis()-startChrono);
          res = tom_append_list_ConcRule(res,tom_append_list_ConcRule(rules,tom_empty_list_ConcRule()));

      }}}if (tom_is_empty_ConcRule_RuleList(tomMatch24_end_8)) {tomMatch24_end_8=tomMatch24_1;} else {tomMatch24_end_8=tom_get_tail_ConcRule_RuleList(tomMatch24_end_8);}}} while(!(tom_equal_term_RuleList(tomMatch24_end_8, tomMatch24_1)));}}}}}



    if(Main.options.verbose) {
      for(Rule rule:res.getCollectionConcRule()) {
        System.out.println(Pretty.toString(rule));
      }
      System.out.println("size = " + res.length());
    }


    // new algo for minimizing
    /*
    RuleList res2 = res;
    %match(res) {
      ConcRule(C1*,r,C2*) -> {
        HashSet<Rule> bag = new HashSet<Rule>();
        searchAbstraction(bag,`r,res,eSig);
        for(Rule r:bag) {
          res2 = `ConcRule(r,res2*);
        }
      }
    }
    System.out.println("#res2 = " + `res2.length());
    */




    if(Main.options.verbose) {
      for(Rule rule:res.getCollectionConcRule()) {
        System.out.println(Pretty.toString(rule));
      }
      System.out.println("size = " + res.length());
      System.out.println("subElim:       " + timeSubElim + " ms");
      System.out.println("addElim:       " + timeAddElim + " ms");
      System.out.println("expand:        " + timeExpand + " ms");
      System.out.println("subSubsumtion: " + timeSubsumtion + " ms");
      System.out.println("subMinimize:   " + timeMinimize + " ms");
    }

    assert !Tools.containsAP(res) : "check contain no AP";
    assert Tools.isLhsLinear(res) : "check lhs-linear";
    if(ordered) {
      return tom_make_Otrs(res);
    } else {
      return tom_make_Trs(res);
    }
  }

   /*
    * Transform a term which contains Sub/Add into a term without Sub
    */
  private static LRUCache<Term,Term> cache = new LRUCache<Term,Term>(1000);
  private static Term simplifySub(Term t, Signature eSig) {
    Term res = cache.get(t);
    if(res!=null) {
      //System.out.println("cache hit");
      return res;
    } else {
      //System.out.println("nohit");
    }

    long startChrono = System.currentTimeMillis();
    try {
      Strategy S1 = tom_cons_list_ChoiceId(tom_make_PropagateEmpty(),tom_cons_list_ChoiceId(tom_make_SimplifySub(eSig),tom_cons_list_ChoiceId(tom_make_DistributeAdd(),tom_empty_list_ChoiceId())));
      //Strategy S1 = `ChoiceId(PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      //Strategy S1 = `ChoiceId(PropagateEmpty(),SimplifySub(eSig));
      //Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig),DistributeAdd());
      //Strategy S1 = `ChoiceId(CleanAdd(),PropagateEmpty(),SimplifySub(eSig));
      res = tom_make_InnermostId(S1).visitLight(t);
      cache.put(t,res);
      timeSubElim += (System.currentTimeMillis()-startChrono);
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }
    return res;
  }

  /*
   * Transform a term which contains Sub/Add into a list (top-level Add) of (linear) terms without Sub/Add
   * input may be non linear: X \ X for instance
   * result may also be non linear: f(X) + g(X)
   */
  private static Term reduce(Term t, Signature eSig) {
   long startChrono;
    try {
      // DistributeAdd needed if we can start with terms like X \ f(a+b) or X \ (f(X)\f(f(_)))
      startChrono = System.currentTimeMillis();
      // pre-treatment: remove AP
      t = tom_make_InnermostId(tom_make_ExpandAP()).visitLight(t);

      t = simplifySub(t,eSig);
      if(Main.options.verbose) {
        System.out.println("NO SUB = " + Pretty.toString(t));
      }

      //startChrono = System.currentTimeMillis();
      //Strategy S2 = `ChoiceId(CleanAdd(),PropagateEmpty(), VarAdd(), FactorizeAdd());
      //t = `InnermostId(S2).visitLight(t); // can be replaced by DistributeAdd() in S1
      //timeAddElim += (System.currentTimeMillis()-startChrono);
      //if(Main.options.verbose) {
      //  System.out.println("NO ADD = " + Pretty.toString(t));
      //}
    } catch(VisitFailure e) {
      System.out.println("failure on: " + t);
    }

    //startChrono = System.currentTimeMillis();
    // note that expandAdd normalize variable's name
    //t = expandAdd(t); // can be replaced by DistributeAdd() in S1
    //timeExpand += (System.currentTimeMillis()-startChrono);
    //if(Main.options.verbose) {
    //  System.out.println("EXPAND = " + Pretty.toString(t));
    //}

    startChrono = System.currentTimeMillis();
    t = simplifySubsumtion(t);
    timeSubsumtion += (System.currentTimeMillis()-startChrono);
    if(Main.options.verbose) {
      System.out.println("REMOVE SUBSUMTION = " + Pretty.toString(t));
    }

    if(Main.options.minimize) {
      // test new idea
      // quite slow
      t = simplifyAbstraction(t,eSig);
      t = simplifySubsumtion(t);
    }

    assert onlyTopLevelAdd(t) : "check only top-level Add";
    return t;
  }

  /*
   * remove redundant rules using a very smart matching algorithm :-)
   */
  // TODO: remove variables only once (and no longer in canBeRemoved2) to speedup the process
  private static RuleList removeRedundantRule(RuleList candidates, Signature eSig) {
    RuleList kernel  = tom_empty_list_ConcRule();
    AddList pkernel  = tom_empty_list_ConcAdd();
    RuleList start  = tom_empty_list_ConcRule();
    {{if (tom_is_sort_RuleList(candidates)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )candidates)))) { sa.rule.types.RuleList  tomMatch27_end_4=(( sa.rule.types.RuleList )candidates);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch27_end_4))) { sa.rule.types.Rule  tomMatch27_9=tom_get_head_ConcRule_RuleList(tomMatch27_end_4);if (tom_is_sort_Rule(tomMatch27_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch27_9))) { sa.rule.types.Rule  tom_r=tom_get_head_ConcRule_RuleList(tomMatch27_end_4);

        boolean b = canBeRemoved3(tom_r, tom_append_list_ConcRule(tom_get_slice_ConcRule((( sa.rule.types.RuleList )candidates),tomMatch27_end_4,tom_empty_list_ConcRule()),tom_append_list_ConcRule(tom_get_tail_ConcRule_RuleList(tomMatch27_end_4),tom_empty_list_ConcRule())), eSig);
        if(!b) {
          kernel = tom_cons_list_ConcRule(tom_r,tom_append_list_ConcRule(kernel,tom_empty_list_ConcRule()));
          pkernel = tom_cons_list_ConcAdd(tom_get_slot_Rule_lhs(tomMatch27_9),tom_append_list_ConcAdd(pkernel,tom_empty_list_ConcAdd()));
        } else {
          start = tom_cons_list_ConcRule(tom_r,tom_append_list_ConcRule(start,tom_empty_list_ConcRule()));
        }
      }}}if (tom_is_empty_ConcRule_RuleList(tomMatch27_end_4)) {tomMatch27_end_4=(( sa.rule.types.RuleList )candidates);} else {tomMatch27_end_4=tom_get_tail_ConcRule_RuleList(tomMatch27_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch27_end_4, (( sa.rule.types.RuleList )candidates))));}}}}


    /*
     * build the list
     * [q1\(q2\...\(k1+...+kn)), q2\...\kernel, ..., qm\(k1+...+kn)]
     */

    TermList deltak = tom_empty_list_TermList();
    Term d = tom_make_Add(pkernel);
    RuleList rstart = start.reverse();
    {{if (tom_is_sort_RuleList(rstart)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )rstart)))) { sa.rule.types.RuleList  tomMatch28_end_4=(( sa.rule.types.RuleList )rstart);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch28_end_4))) { sa.rule.types.Rule  tomMatch28_9=tom_get_head_ConcRule_RuleList(tomMatch28_end_4);if (tom_is_sort_Rule(tomMatch28_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch28_9))) {

        d = simplifySub(tom_make_Sub(tom_get_slot_Rule_lhs(tomMatch28_9),d),eSig);
        deltak = tom_cons_list_TermList(d,tom_append_list_TermList(deltak,tom_empty_list_TermList()));
      }}}if (tom_is_empty_ConcRule_RuleList(tomMatch28_end_4)) {tomMatch28_end_4=(( sa.rule.types.RuleList )rstart);} else {tomMatch28_end_4=tom_get_tail_ConcRule_RuleList(tomMatch28_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch28_end_4, (( sa.rule.types.RuleList )rstart))));}}}}


    RuleList result = removeRedundantRuleAux(start,kernel,eSig);
    //RuleList result = removeRedundantRuleAux2(start,kernel,deltak,eSig);
    return result;
  }

  private static RuleList removeRedundantRuleAux(RuleList candidates, RuleList kernel, Signature eSig) {
    assert Tools.isLhsLinear(candidates) : "check lhs-linear" ;
    RuleList res = kernel;

    {{if (tom_is_sort_RuleList(candidates)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )candidates)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )candidates)))) { sa.rule.types.Rule  tom_head=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )candidates)); sa.rule.types.RuleList  tom_tail=tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )candidates));

        // try with the head kept in kernel
        RuleList branch1 = removeRedundantRuleAux(tom_tail, tom_append_list_ConcRule(kernel,tom_cons_list_ConcRule(tom_head,tom_empty_list_ConcRule())), eSig);
        res = branch1;

        boolean b = canBeRemoved3(tom_head, tom_append_list_ConcRule(kernel,tom_append_list_ConcRule(tom_tail,tom_empty_list_ConcRule())), eSig);
        if(b) {
          if(Main.options.verbose) {
            System.out.println("REMOVE: " + Pretty.toString(tom_head));
          }
          // try with the head removed
          RuleList branch2 = removeRedundantRuleAux(tom_tail, kernel, eSig);
          if(branch2.length() < branch1.length()) {
            res = branch2;
          }
        }
      }}}}}

    return res;
  }

  private static RuleList removeRedundantRuleAux2(RuleList candidates, RuleList kernel, TermList deltak, Signature eSig) {
    assert Tools.isLhsLinear(candidates) : "check lhs-linear" ;
    System.out.println("DELTA KERNEL = " + Pretty.toString(deltak));
    RuleList res = kernel;

    {{if (tom_is_sort_RuleList(candidates)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )candidates)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )candidates)))) { sa.rule.types.Rule  tomMatch30_10=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )candidates));if (tom_is_sort_Rule(tomMatch30_10)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch30_10))) { sa.rule.types.Rule  tom_head=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )candidates)); sa.rule.types.RuleList  tom_tail=tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )candidates));if (tom_is_sort_TermList(deltak)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )deltak)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )deltak)))) { sa.rule.types.TermList  tom_tailk=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )deltak));

        // try with the head kept in kernel
        TermList mapTailk = mapSub(tom_get_slot_Rule_lhs(tomMatch30_10),tom_tailk);
        RuleList branch1 = removeRedundantRuleAux2(tom_tail,tom_append_list_ConcRule(kernel,tom_cons_list_ConcRule(tom_head,tom_empty_list_ConcRule())),tom_tailk,eSig);
        res = branch1;

        boolean b = (tom_get_head_TermList_TermList((( sa.rule.types.TermList )deltak))== tom_make_Empty());
        if(b) {
          if(Main.options.verbose) {
            System.out.println("REMOVE2: " + Pretty.toString(tom_head));
          }
          // try with the head removed
          RuleList branch2 = removeRedundantRuleAux2(tom_tail,kernel,tom_tailk,eSig);
          if(branch2.length() < branch1.length()) {
            res = branch2;
          }
        }
      }}}}}}}}}}

    return res;
  }

  private static TermList mapSub(Term t, TermList l) {
    {{if (tom_is_sort_TermList(l)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )l)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )l))) {
 return l; }}}}{if (tom_is_sort_TermList(l)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )l)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )l)))) {

        TermList tmp = mapSub(t,tom_get_tail_TermList_TermList((( sa.rule.types.TermList )l)));
        return tom_cons_list_TermList(tom_make_Sub(tom_get_head_TermList_TermList((( sa.rule.types.TermList )l)),t),tom_append_list_TermList(tmp,tom_empty_list_TermList()));
      }}}}}

    return l;
  }


  public static class PropagateEmpty extends tom.library.sl.AbstractStrategyBasic {public PropagateEmpty() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.TermList  tomMatch32_2=tom_get_slot_Appl_args((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch32_2))) { sa.rule.types.TermList  tomMatch32_end_8=tomMatch32_2;do {{if (!(tom_is_empty_TermList_TermList(tomMatch32_end_8))) { sa.rule.types.Term  tomMatch32_11=tom_get_head_TermList_TermList(tomMatch32_end_8);if (tom_is_sort_Term(tomMatch32_11)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch32_11))) { sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);



        assert !Tools.containsSub(tom_s) : tom_s;
        Term res = tom_make_Empty();
        debug("propagate empty",tom_s,res);
        return res;
      }}}if (tom_is_empty_TermList_TermList(tomMatch32_end_8)) {tomMatch32_end_8=tomMatch32_2;} else {tomMatch32_end_8=tom_get_tail_TermList_TermList(tomMatch32_end_8);}}} while(!(tom_equal_term_TermList(tomMatch32_end_8, tomMatch32_2)));}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch32_15=tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch32_15)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch32_15))) { sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);




        assert !Tools.containsSub(tom_s) : tom_s;
        Term res = tom_make_Empty();
        debug("_@empty",tom_s,res);
        return res;
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_PropagateEmpty() { return new PropagateEmpty();}




  /*
   * no longer needed
   * done in hooks
   */
  public static class CleanAdd extends tom.library.sl.AbstractStrategyBasic {public CleanAdd() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch33_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch33_1))) {if (tom_is_empty_ConcAdd_AddList(tomMatch33_1)) {



        Term res = tom_make_Empty();
        debug("elim ()",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch33_6=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch33_6))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch33_6))) {if (tom_is_empty_ConcAdd_AddList(tom_get_tail_ConcAdd_AddList(tomMatch33_6))) {



        Term res = tom_get_head_ConcAdd_AddList(tomMatch33_6);
        debug("flatten2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch33_12=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch33_12))) { sa.rule.types.AddList  tomMatch33_end_18=tomMatch33_12;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch33_end_18))) { sa.rule.types.Term  tomMatch33_21=tom_get_head_ConcAdd_AddList(tomMatch33_end_18);if (tom_is_sort_Term(tomMatch33_21)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch33_21))) { sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);



        assert !Tools.containsSub(tom_s) : tom_s;
        Term res = tom_make_Add(tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch33_12,tomMatch33_end_18,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tom_get_tail_ConcAdd_AddList(tomMatch33_end_18),tom_empty_list_ConcAdd())));
        debug("elim empty",tom_s,res);
        return res;
      }}}if (tom_is_empty_ConcAdd_AddList(tomMatch33_end_18)) {tomMatch33_end_18=tomMatch33_12;} else {tomMatch33_end_18=tom_get_tail_ConcAdd_AddList(tomMatch33_end_18);}}} while(!(tom_equal_term_AddList(tomMatch33_end_18, tomMatch33_12)));}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch33_24=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch33_24))) { sa.rule.types.AddList  tomMatch33_end_30=tomMatch33_24;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch33_end_30))) { sa.rule.types.Term  tomMatch33_34=tom_get_head_ConcAdd_AddList(tomMatch33_end_30);if (tom_is_sort_Term(tomMatch33_34)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch33_34))) { sa.rule.types.AddList  tomMatch33_33=tom_get_slot_Add_addlist(tomMatch33_34);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch33_33))) {



        Term res = tom_make_Add(tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch33_24,tomMatch33_end_30,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tomMatch33_33,tom_append_list_ConcAdd(tom_get_tail_ConcAdd_AddList(tomMatch33_end_30),tom_empty_list_ConcAdd()))));
        debug("flatten1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}if (tom_is_empty_ConcAdd_AddList(tomMatch33_end_30)) {tomMatch33_end_30=tomMatch33_24;} else {tomMatch33_end_30=tom_get_tail_ConcAdd_AddList(tomMatch33_end_30);}}} while(!(tom_equal_term_AddList(tomMatch33_end_30, tomMatch33_24)));}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CleanAdd() { return new CleanAdd();}public static class FactorizeAdd extends tom.library.sl.AbstractStrategyBasic {public FactorizeAdd() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch34_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch34_1))) { sa.rule.types.AddList  tomMatch34_end_7=tomMatch34_1;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch34_end_7))) { sa.rule.types.Term  tomMatch34_16=tom_get_head_ConcAdd_AddList(tomMatch34_end_7);if (tom_is_sort_Term(tomMatch34_16)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch34_16))) { String  tom_f=tom_get_slot_Appl_symbol(tomMatch34_16); sa.rule.types.AddList  tomMatch34_8=tom_get_tail_ConcAdd_AddList(tomMatch34_end_7); sa.rule.types.AddList  tomMatch34_end_11=tomMatch34_8;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch34_end_11))) { sa.rule.types.Term  tomMatch34_20=tom_get_head_ConcAdd_AddList(tomMatch34_end_11);if (tom_is_sort_Term(tomMatch34_20)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch34_20))) {if (tom_equal_term_String(tom_f, tom_get_slot_Appl_symbol(tomMatch34_20))) {









        TermList tl = addUniqueTi(tom_get_slot_Appl_args(tomMatch34_16),tom_get_slot_Appl_args(tomMatch34_20));
        if(tl != null) {
          Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Appl(tom_f,tl),tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch34_1,tomMatch34_end_7,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch34_8,tomMatch34_end_11,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tom_get_tail_ConcAdd_AddList(tomMatch34_end_11),tom_empty_list_ConcAdd())))));
          debug("add merge",(( sa.rule.types.Term )tom__arg),res);
          return res;
        } else {
          //System.out.println("add merge failed");
        }
      }}}}if (tom_is_empty_ConcAdd_AddList(tomMatch34_end_11)) {tomMatch34_end_11=tomMatch34_8;} else {tomMatch34_end_11=tom_get_tail_ConcAdd_AddList(tomMatch34_end_11);}}} while(!(tom_equal_term_AddList(tomMatch34_end_11, tomMatch34_8)));}}}if (tom_is_empty_ConcAdd_AddList(tomMatch34_end_7)) {tomMatch34_end_7=tomMatch34_1;} else {tomMatch34_end_7=tom_get_tail_ConcAdd_AddList(tomMatch34_end_7);}}} while(!(tom_equal_term_AddList(tomMatch34_end_7, tomMatch34_1)));}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_FactorizeAdd() { return new FactorizeAdd();}public static class VarAdd extends tom.library.sl.AbstractStrategyBasic {public VarAdd() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch35_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch35_1))) { sa.rule.types.AddList  tomMatch35_end_7=tomMatch35_1;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch35_end_7))) { sa.rule.types.Term  tomMatch35_10=tom_get_head_ConcAdd_AddList(tomMatch35_end_7);if (tom_is_sort_Term(tomMatch35_10)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch35_10))) {








        Term res = tom_get_head_ConcAdd_AddList(tomMatch35_end_7);
        debug("a + x + b -> x",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}if (tom_is_empty_ConcAdd_AddList(tomMatch35_end_7)) {tomMatch35_end_7=tomMatch35_1;} else {tomMatch35_end_7=tom_get_tail_ConcAdd_AddList(tomMatch35_end_7);}}} while(!(tom_equal_term_AddList(tomMatch35_end_7, tomMatch35_1)));}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_VarAdd() { return new VarAdd();}public static class ExpandAP extends tom.library.sl.AbstractStrategyBasic {public ExpandAP() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {







        Term Z = tom_make_Var(Tools.getName("Z"));
        Term res = tom_make_Sub(Z,tom_get_slot_Anti_term((( sa.rule.types.Term )tom__arg)));
        debugVerbose("AP",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandAP() { return new ExpandAP();}public static class SimplifySub extends tom.library.sl.AbstractStrategyBasic {private  Signature  eSig;public SimplifySub( Signature  eSig) {super(tom_make_Identity());this.eSig=eSig;}public  Signature  geteSig() {return eSig;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_2=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_2)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch37_2))) {








        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Empty();
        debug("t - x -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_9=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_9)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch37_9))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_t;
        debug("t - empty -> t",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_16=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_16)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch37_16))) { sa.rule.types.AddList  tomMatch37_19=tom_get_slot_Add_addlist(tomMatch37_16);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch37_19))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch37_19))) { sa.rule.types.Term  tom_head=tom_get_head_ConcAdd_AddList(tomMatch37_19);



        assert !Tools.containsSub(tom_t) : tom_t;
        assert !Tools.containsSub(tom_head) : tom_head;
        Term res = tom_make_Sub(tom_make_Sub(tom_t,tom_head),tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch37_19)));
        debug("sub distrib1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_26=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch37_27=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_26)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch37_26))) { sa.rule.types.Term  tom_X=tomMatch37_26;if (tom_is_sort_Term(tomMatch37_27)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_27))) { String  tom_f=tom_get_slot_Appl_symbol(tomMatch37_27); sa.rule.types.Term  tom_t=tomMatch37_27; sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);



        if(false && isPlainTerm(tom_t)) { // desactivate old version of X \ t
          RuleCompiler ruleCompiler = new RuleCompiler(eSig, eSig); // gSig
          RuleList rl = ruleCompiler.expandAntiPatterns(tom_cons_list_ConcRule(tom_make_Rule(tom_make_Anti(tom_t),tom_make_Var("_")),tom_empty_list_ConcRule()));
          //System.out.println("rl = " + Pretty.toString(rl));
          AddList tl = tom_empty_list_ConcAdd();
          GomType codomain = eSig.getCodomain(tom_f);
          {{if (tom_is_sort_RuleList(rl)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )rl)))) { sa.rule.types.RuleList  tomMatch38_end_4=(( sa.rule.types.RuleList )rl);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch38_end_4))) { sa.rule.types.Rule  tomMatch38_9=tom_get_head_ConcRule_RuleList(tomMatch38_end_4);if (tom_is_sort_Rule(tomMatch38_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch38_9))) { sa.rule.types.Term  tomMatch38_7=tom_get_slot_Rule_lhs(tomMatch38_9);if (tom_is_sort_Term(tomMatch38_7)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch38_7))) {

              tl = tom_cons_list_ConcAdd(tomMatch38_7,tom_append_list_ConcAdd(tl,tom_empty_list_ConcAdd())); // order not preserved
            }}}}}if (tom_is_empty_ConcRule_RuleList(tomMatch38_end_4)) {tomMatch38_end_4=(( sa.rule.types.RuleList )rl);} else {tomMatch38_end_4=tom_get_tail_ConcRule_RuleList(tomMatch38_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch38_end_4, (( sa.rule.types.RuleList )rl))));}}}}

          Term res = tom_make_Add(tl);
          res = eliminateIllTyped(res, codomain, eSig);
          debug("expand AP",tom_s,res);
          // generate X@Add(tl)
          res = tom_make_At(tom_X,res);
          return res;
        } else {
          // replace X \ t by X@((a+b+g(_)+f(_,_)) \ t)
          // this version is as efficient as the previous one, but simpler
          assert !Tools.containsSub(tom_t) : tom_t;
          AddList al = tom_empty_list_ConcAdd();
          GomType codomain = eSig.getCodomain(tom_f); // use codomain to generate well typed terms
          for(String name: eSig.getConstructors(codomain)) {
            int arity = eSig.getArity(name);
            Term expand = Tools.genAbstractTerm(name,arity, Tools.getName("Z"));
            al = tom_cons_list_ConcAdd(expand,tom_append_list_ConcAdd(al,tom_empty_list_ConcAdd()));
          }
          Term res = tom_make_Sub(tom_make_Add(al),tom_t);
          debug("expand AP2", tom_s,res);
          // generate X@Add(tl)
          res = tom_make_At(tom_X,res);
          return res;
        }
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_37=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch37_38=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_37)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch37_37))) {if (tom_is_sort_Term(tomMatch37_38)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_38))) { sa.rule.types.Term  tom_u=tomMatch37_38;



        assert !Tools.containsSub(tom_u) : tom_u;
        Term res = tom_make_Empty();
        debug("empty - f(...) -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_48=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch37_49=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_48)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch37_48))) { sa.rule.types.AddList  tomMatch37_52=tom_get_slot_Add_addlist(tomMatch37_48);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch37_52))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch37_52))) { sa.rule.types.Term  tom_head=tom_get_head_ConcAdd_AddList(tomMatch37_52);if (tom_is_sort_Term(tomMatch37_49)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_49))) { sa.rule.types.Term  tom_t=tomMatch37_49;



        assert !Tools.containsSub(tom_head) : tom_head;
        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Sub(tom_head,tom_t),tom_cons_list_ConcAdd(tom_make_Sub(tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch37_52)),tom_t),tom_empty_list_ConcAdd())));
        debug("sub distrib2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_63=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch37_64=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_63)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_63))) { sa.rule.types.Term  tom_t=tomMatch37_63;if (tom_is_sort_Term(tomMatch37_64)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_64))) { sa.rule.types.Term  tom_u=tomMatch37_64;if (!(tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch37_64), tom_get_slot_Appl_symbol(tomMatch37_63)))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        assert !Tools.containsSub(tom_u) : tom_u;
        Term res = tom_t;
        debug("sub elim1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_76=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch37_77=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_76)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_76))) { sa.rule.types.Term  tom_t1=tomMatch37_76;if (tom_is_sort_Term(tomMatch37_77)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch37_77))) {if (tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch37_76), tom_get_slot_Appl_symbol(tomMatch37_77))) { sa.rule.types.Term  tom_t2=tomMatch37_77;



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        //Term res = `sub(t1,t2);
        Term res = subopt(tom_t1,tom_t2,eSig);
        debug("sub1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_90=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_90)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch37_90))) { sa.rule.types.Term  tom_t1=tom_get_slot_At_term2(tomMatch37_90); sa.rule.types.Term  tom_t2=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        Term res = tom_make_At(tom_get_slot_At_term1(tomMatch37_90),tom_make_Sub(tom_t1,tom_t2));
        debug("at",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch37_100=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t1=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch37_100)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch37_100))) { sa.rule.types.Term  tom_t2=tom_get_slot_At_term2(tomMatch37_100);



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        Term res = tom_make_Sub(tom_t1,tom_t2);
        debug("at2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_SimplifySub( Signature  t0) { return new SimplifySub(t0);}




  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  private static Term sub(Term t1,Term t2) {
    {{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) { String  tom_f=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t1));if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {if (tom_equal_term_String(tom_f, tom_get_slot_Appl_symbol((( sa.rule.types.Term )t2)))) {

        TermList tl1 = tom_get_slot_Appl_args((( sa.rule.types.Term )t1));
        TermList tl2 = tom_get_slot_Appl_args((( sa.rule.types.Term )t2));
        int len = tl1.length();
        TermList args[] = new TermList[len];
        for(int i=0 ; i<len ; i++) {
          args[i] = tom_empty_list_TermList();
        }
        int cpt = 0;
        assert tl1.length() == tl2.length();
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();

          for(int i=0 ; i<len ; i++) {
            TermList tl = args[i]; // cannot use [] in `
            if(i==cpt) {
              tl = tom_append_list_TermList(tl,tom_cons_list_TermList(tom_make_Sub(h1,h2),tom_empty_list_TermList()));
            } else {
              tl = tom_append_list_TermList(tl,tom_cons_list_TermList(h1,tom_empty_list_TermList()));
            }
            args[i] = tl;
          }

          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
          cpt++;
        }

        AddList sum = tom_empty_list_ConcAdd();
        for(int i=0 ; i<len ; i++) {
          TermList tl = args[i]; // cannot use [] in `
          sum = tom_cons_list_ConcAdd(tom_make_Appl(tom_f,tl),tom_append_list_ConcAdd(sum,tom_empty_list_ConcAdd()));;
        }

        return tom_make_Add(sum);
      }}}}}}}}}

    System.out.println("cannot sub " + t1 + " and " + t2);
    return null;
  }

  // f(a1,...,an) - f(b1,...,bn) -> f(a1-b1,..., an) + ... + f(a1,...,an-bn)
  private static Term subopt(Term t1,Term t2, Signature eSig) {
    {{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) { String  tom_f=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t1));if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {if (tom_equal_term_String(tom_f, tom_get_slot_Appl_symbol((( sa.rule.types.Term )t2)))) {

        TermList tl1 = tom_get_slot_Appl_args((( sa.rule.types.Term )t1));
        TermList tl2 = tom_get_slot_Appl_args((( sa.rule.types.Term )t2));
        int len = tl1.length();
        TermList args[] = new TermList[len];
        for(int i=0 ; i<len ; i++) {
          args[i] = tom_empty_list_TermList();
        }
        int cpt = 0;
        assert tl1.length() == tl2.length();
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();
          Term sub = simplifySub(tom_make_Sub(h1,h2),eSig); // do not use reduce because variables are normalized

          if(sub==h1) {
            return t1;
          }

          for(int i=0 ; i<len ; i++) {
            TermList tl = args[i]; // cannot use [] in `
            if(i==cpt) {
              tl = tom_append_list_TermList(tl,tom_cons_list_TermList(sub,tom_empty_list_TermList()));
            } else {
              tl = tom_append_list_TermList(tl,tom_cons_list_TermList(h1,tom_empty_list_TermList()));
            }
            args[i] = tl;
          }

          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
          cpt++;
        }

        AddList sum = tom_empty_list_ConcAdd();
        for(int i=0 ; i<len ; i++) {
          TermList tl = args[i]; // cannot use [] in `
          sum = tom_cons_list_ConcAdd(tom_make_Appl(tom_f,tl),tom_append_list_ConcAdd(sum,tom_empty_list_ConcAdd()));;
        }

        return tom_make_Add(sum);
      }}}}}}}}}

    System.out.println("cannot sub " + t1 + " and " + t2);
    return null;
  }

  // (a1,...,an) + (b1,...,bn) -> (a1,..., ai+bi,..., an)
  // for the unique i such that ai != bi
  private static TermList addUniqueTi(TermList l1,TermList l2) {
    TermList tl1=l1;
    TermList tl2=l2;
    if(l1==l2) {
      return l1;
    }
    TermList tl = tom_empty_list_TermList();
    int cpt = 0;
    while(!tl1.isEmptyTermList() && cpt <= 1) {
      Term h1 = tl1.getHeadTermList();
      Term h2 = tl2.getHeadTermList();
      // we have to compare modulo renaming and AT
      if(h1 == h2) {
        tl = tom_append_list_TermList(tl,tom_cons_list_TermList(h1,tom_empty_list_TermList()));
      } else if(matchConstraint(h1,h2)==tom_make_TrueMatch() && matchConstraint(h2,h1)==tom_make_TrueMatch()) {
        // match(h1,h2) && match(h2,h1) is more efficient than removeVar(h1) == removeVar(h2)
        // PEM: check that we can keep h1 only
        tl = tom_append_list_TermList(tl,tom_cons_list_TermList(h1,tom_empty_list_TermList()));
      } else {
        tl = tom_append_list_TermList(tl,tom_cons_list_TermList(tom_make_Add(tom_cons_list_ConcAdd(h1,tom_cons_list_ConcAdd(h2,tom_empty_list_ConcAdd()))),tom_empty_list_TermList()));
        cpt++;
      }

      tl1 = tl1.getTailTermList();
      tl2 = tl2.getTailTermList();
    }
    if(cpt <= 1) {
      return tl;
    } else {
      //System.out.println("cannot add " + l1 + " and " + l2);
      return null;
    }
  }


  /*
   * given a term (which contains Add and Sub) and a type
   * returns a term where badly typed terms are replaced by Empty()
   */
  private static Term eliminateIllTyped(Term t, GomType type, Signature eSig) {
    //System.out.println(Pretty.toString(t) + ":" + type.getName());
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

        return (( sa.rule.types.Term )t);
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tom_f=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t));


        if(eSig.getCodomain(tom_f) == type) {
          GomTypeList domain = eSig.getDomain(tom_f);
          TermList tail = tom_get_slot_Appl_args((( sa.rule.types.Term )t));
          TermList new_args = tom_empty_list_TermList();
          while(!tail.isEmptyTermList()) {
            Term head = tail.getHeadTermList();
            GomType arg_type = domain.getHeadConcGomType();

            Term new_arg = eliminateIllTyped(head,arg_type,eSig);
            if(new_arg == tom_make_Empty()) {
              // propagate Empty for any term which contains Empty
              return tom_make_Empty();
            }

            new_args = tom_append_list_TermList(new_args,tom_cons_list_TermList(new_arg,tom_empty_list_TermList()));
            tail = tail.getTailTermList();
            domain = domain.getTailConcGomType();
          }
          return tom_make_Appl(tom_f,new_args);
        } else {
          return tom_make_Empty();
        }
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return tom_make_Sub(eliminateIllTyped(tom_get_slot_Sub_term1((( sa.rule.types.Term )t)),type,eSig),eliminateIllTyped(tom_get_slot_Sub_term2((( sa.rule.types.Term )t)),type,eSig));
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        AddList tail = tom_get_slot_Add_addlist((( sa.rule.types.Term )t));
        AddList res = tom_empty_list_ConcAdd();
        while(!tail.isEmptyConcAdd()) {
          Term head = tail.getHeadConcAdd();
          res = tom_cons_list_ConcAdd(eliminateIllTyped(head,type,eSig),tom_append_list_ConcAdd(res,tom_empty_list_ConcAdd()));
          tail = tail.getTailConcAdd();
        }
        return tom_make_Add(res);
      }}}}}



    System.out.println("eliminateIllTyped Should not be there: " + t);
    return t;
  }

  public static class DistributeAdd extends tom.library.sl.AbstractStrategyBasic {public DistributeAdd() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch42_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch42_1))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch42_1))) {if (tom_is_empty_ConcAdd_AddList(tom_get_tail_ConcAdd_AddList(tomMatch42_1))) {




        Term res = tom_get_head_ConcAdd_AddList(tomMatch42_1);
        //debug("flatten2",`s,res);
        return res;
      }}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch42_8=tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_Z=tom_get_slot_At_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch42_8)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch42_8))) { sa.rule.types.AddList  tomMatch42_11=tom_get_slot_Add_addlist(tomMatch42_8);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch42_11))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch42_11))) {



        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_At(tom_Z,tom_get_head_ConcAdd_AddList(tomMatch42_11)),tom_cons_list_ConcAdd(tom_make_At(tom_Z,tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch42_11))),tom_empty_list_ConcAdd())));
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.TermList  tomMatch42_19=tom_get_slot_Appl_args((( sa.rule.types.Term )tom__arg)); String  tom_f=tom_get_slot_Appl_symbol((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch42_19))) { sa.rule.types.TermList  tomMatch42_end_25=tomMatch42_19;do {{ sa.rule.types.TermList  tom_C1=tom_get_slice_TermList(tomMatch42_19,tomMatch42_end_25,tom_empty_list_TermList());if (!(tom_is_empty_TermList_TermList(tomMatch42_end_25))) { sa.rule.types.Term  tomMatch42_29=tom_get_head_TermList_TermList(tomMatch42_end_25);if (tom_is_sort_Term(tomMatch42_29)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch42_29))) { sa.rule.types.AddList  tomMatch42_28=tom_get_slot_Add_addlist(tomMatch42_29);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch42_28))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch42_28))) { sa.rule.types.AddList  tomMatch42_32=tom_get_tail_ConcAdd_AddList(tomMatch42_28);if (!(tom_is_empty_ConcAdd_AddList(tomMatch42_32))) { sa.rule.types.AddList  tom_A2=tom_get_tail_ConcAdd_AddList(tomMatch42_32); sa.rule.types.TermList  tom_C2=tom_get_tail_TermList_TermList(tomMatch42_end_25);















        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Appl(tom_f,tom_append_list_TermList(tom_C1,tom_cons_list_TermList(tom_make_Add(tom_cons_list_ConcAdd(tom_get_head_ConcAdd_AddList(tomMatch42_28),tom_append_list_ConcAdd(tom_A2,tom_empty_list_ConcAdd()))),tom_append_list_TermList(tom_C2,tom_empty_list_TermList())))),tom_cons_list_ConcAdd(tom_make_Appl(tom_f,tom_append_list_TermList(tom_C1,tom_cons_list_TermList(tom_make_Add(tom_cons_list_ConcAdd(tom_get_head_ConcAdd_AddList(tomMatch42_32),tom_append_list_ConcAdd(tom_A2,tom_empty_list_ConcAdd()))),tom_append_list_TermList(tom_C2,tom_empty_list_TermList())))),tom_empty_list_ConcAdd())))
;
        //debug("distribute add",`s,res);
        return res;
      }}}}}}if (tom_is_empty_TermList_TermList(tomMatch42_end_25)) {tomMatch42_end_25=tomMatch42_19;} else {tomMatch42_end_25=tom_get_tail_TermList_TermList(tomMatch42_end_25);}}} while(!(tom_equal_term_TermList(tomMatch42_end_25, tomMatch42_19)));}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_DistributeAdd() { return new DistributeAdd();}





  /*
   * Transform a term which contains Add into
   * a sum of terms which do no longer contain any Add
   * (more efficient implementation)
   */
  private static Term expandAdd(Term t) {
    HashSet<Term> bag = new HashSet<Term>();
    expandAddAux(bag,t);
    AddList tl = tom_empty_list_ConcAdd();
    for(Term e:bag) {
      tl = tom_cons_list_ConcAdd(e,tom_append_list_ConcAdd(tl,tom_empty_list_ConcAdd()));
    }
    return tom_make_Add(tl);
  }

  // Transform a term which contains Add into a set of plain terms
  private static void expandAddAux(HashSet<Term> c, Term subject) {
    HashSet<Term> todo = new HashSet<Term>(); // terms to expand
    HashSet<Term> todo2 = new HashSet<Term>();
    HashSet<Term> tmpC = new HashSet<Term>(); // working memory when expanding a single term
    todo.add(subject);
    while(todo.size() > 0) {
      todo2.clear();
      for(Term t:todo) {
        tmpC.clear();
        try {
           // TopDownCollect: apply s1 in a top-down way, s should extends the identity
           // a failure stops the top-down process under this current node
          tom_make_TopDownCollect(tom_make_ExpandAdd(tmpC,t)).visit(t);
        } catch(VisitFailure e) {
        }
        if(tmpC.isEmpty()) {
          // t is a plain term
          t = Tools.normalizeVariable(t);
          c.add(t);
        } else {
          for(Term e:tmpC) {
            if(isPlainTerm(e)) {
              e = Tools.normalizeVariable(e);
              c.add(e);
            } else {
              // todo list for the next round
              todo2.add(e);
            }
          }
        }
      }
      // all terms of toto have been expanded
      // swap todo, todo2
      HashSet<Term> tmp = todo;
      todo = todo2; // new terms which have to be expanded
      todo2 = tmp;  // reuse toto set, will be cleared next round
      //System.out.println("size(c) = " + c.size());
    }
  }

  public static class ExpandAdd extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashSet  c;private  sa.rule.types.Term  subject;public ExpandAdd( java.util.HashSet  c,  sa.rule.types.Term  subject) {super(tom_make_Identity());this.c=c;this.subject=subject;}public  java.util.HashSet  getc() {return c;}public  sa.rule.types.Term  getsubject() {return subject;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch43_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch43_1))) { sa.rule.types.AddList  tomMatch43_end_7=tomMatch43_1;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch43_end_7))) {


        Term newt = (Term) getEnvironment().getPosition().getReplace(tom_get_head_ConcAdd_AddList(tomMatch43_end_7)).visit(subject);
        c.add(newt);
      }if (tom_is_empty_ConcAdd_AddList(tomMatch43_end_7)) {tomMatch43_end_7=tomMatch43_1;} else {tomMatch43_end_7=tom_get_tail_ConcAdd_AddList(tomMatch43_end_7);}}} while(!(tom_equal_term_AddList(tomMatch43_end_7, tomMatch43_1)));}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch43_11=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch43_11))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch43_11))) {


        // remove the term which contains Add(ConcAdd(...))
        c.remove(subject);
        // fails to stop the TopDownCollect, and thus do not expand deeper terms
        tom_make_Fail().visitLight(subject);
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandAdd( java.util.HashSet  t0,  sa.rule.types.Term  t1) { return new ExpandAdd(t0,t1);}



  /*
   * Given a list of terms
   * remove those which are subsumed by another ones
   */
  private static Term simplifySubsumtion(Term t) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

        return tom_make_Add(simplifySubsumtionAux(tom_get_slot_Add_addlist((( sa.rule.types.Term )t)),tom_empty_list_ConcAdd()));
      }}}}}

    return t;
  }

  private static AddList simplifySubsumtionAux(AddList candidates, AddList kernel) {
    {{if (tom_is_sort_AddList(candidates)) {if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )(( sa.rule.types.AddList )candidates)))) {if (!(tom_is_empty_ConcAdd_AddList((( sa.rule.types.AddList )candidates)))) { sa.rule.types.Term  tom_head=tom_get_head_ConcAdd_AddList((( sa.rule.types.AddList )candidates)); sa.rule.types.AddList  tom_tail=tom_get_tail_ConcAdd_AddList((( sa.rule.types.AddList )candidates));

        boolean b = matchFromList(tom_head, tom_append_list_ConcAdd(tom_tail,tom_append_list_ConcAdd(kernel,tom_empty_list_ConcAdd())));
        if(b) {
          return simplifySubsumtionAux(tom_tail, kernel);
        } else {
          return simplifySubsumtionAux(tom_tail, tom_cons_list_ConcAdd(tom_head,tom_append_list_ConcAdd(kernel,tom_empty_list_ConcAdd())));
        }
      }}}}}

    return kernel;
  }

  /*
   * simple but inefficient version
   *
  private static AddList simplifySubsumtion(AddList tl) {
    %match(tl) {
      ConcAdd(C1*,t1,C2*,t2,C3*) -> {
        if(`match(t1,t2)) {
          return simplifySubsumtion(`ConcAdd(t1,C1*,C2*,C3*));
        } else if(`match(t2,t1)) {
          return simplifySubsumtion(`ConcAdd(t2,C1*,C2*,C3*));
        }
      }
    }
    return tl;
  }
  */

  /*
   * return true is t can be match by a term of al
   */
  private static boolean matchFromList(Term t, AddList al) {
    {{if (tom_is_sort_AddList(al)) {if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )(( sa.rule.types.AddList )al)))) { sa.rule.types.AddList  tomMatch46_end_4=(( sa.rule.types.AddList )al);do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch46_end_4))) {

        /*
         * to use matchConstraint algorithm we need to activate hooks for
         * PropagateTrueMatch and PropagateEmpty
         */
        if(matchConstraint(tom_get_head_ConcAdd_AddList(tomMatch46_end_4),t) == tom_make_TrueMatch()) { // use this more general algorithm to factorize code
          return true;
        }
      }if (tom_is_empty_ConcAdd_AddList(tomMatch46_end_4)) {tomMatch46_end_4=(( sa.rule.types.AddList )al);} else {tomMatch46_end_4=tom_get_tail_ConcAdd_AddList(tomMatch46_end_4);}}} while(!(tom_equal_term_AddList(tomMatch46_end_4, (( sa.rule.types.AddList )al))));}}}}

    return false;
  }

  /*
   * 1st idea to remove redundant patterns
   *
   * expand once all occurence of _ in a term
   */

/*
  private static Term expandVar(Term t, Signature eSig) {
    HashSet<Position> bag = new HashSet<Position>();
    HashSet<Term> res = new HashSet<Term>();

    GomType codomain = null;
    %match(t) {
      Appl(f,_) -> {
          codomain = eSig.getCodomain(`f);
      }
    }

    res.add(t);
    try {
      `TopDown(CollectVarPosition(bag)).visit(t);

      for(Position omega:bag) {
        HashSet<Term> todo = new HashSet<Term>();
        System.out.println("res.size  = " + res.size());
        for(Term subject:res) {
          // add g(Z1,...) ... h(Z1,...)
          for(String name: eSig.getConstructors()) {
            int arity = eSig.getArity(name);
            Term expand = Tools.genAbstractTerm(name,arity, "_");
            expand =  (Term) Tools.removeVar(expand);

            Term newt = (Term) omega.getReplace(expand).visit(subject);

            System.out.println("newt1 = " + Pretty.toString(newt));
            newt = eliminateIllTyped(newt, codomain, eSig);
            System.out.println("newt2 = " + Pretty.toString(newt));
            if(newt != `Empty()) {
              todo.add(newt);
            }
          }
          System.out.println("todo.size = " + todo.size());

        }
        res=todo;
      }
    } catch(VisitFailure e) {
      System.out.println("expandVar failed");
    }

    AddList al = `ConcAdd();
    for(Term newt:res) {
      al = `ConcAdd(newt,al*);
    }

    return `Add(al);
  }

  %strategy CollectVarPosition(c:HashSet) extends Identity() {
    visit Term {
      Var[] -> {
        c.add(getEnvironment().getPosition());
      }
    }
  }

  public static boolean canBeRemoved1(Rule rule, RuleList ruleList, Signature eSig) {
    %match(rule) {
      Rule(lhs,rhs) -> {
        System.out.println("CAN BE REMOVED 1 = " + Pretty.toString(`lhs));
        Term t = expandVar(`lhs,eSig);
        System.out.println("Expanded REMOVED 1 = " + Pretty.toString(t));
        %match(t) {
          Add(ConcAdd(_*,et,_*)) -> {
            boolean foundMatch = false;
            %match(ruleList) {
              ConcRule(_*,Rule(lhs,_),_*) -> {
                if(match(`lhs,`et)) {
                  foundMatch = true;
                }
              }
            }
            if(!foundMatch) {
              return false;
            }
          }
        }
      }
    }

    System.out.println("BINGO 1 = ");
    return true;
  }
*/

  /*
   * 2nd idea to remove redundant patterns
   *
   * introduce matching constraints inside patterns
   */

  public static class SimplifyMatch extends tom.library.sl.AbstractStrategyBasic {public SimplifyMatch() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {if (tom_equal_term_Term(tom_get_slot_Match_term1((( sa.rule.types.Term )tom__arg)), tom_get_slot_Match_term2((( sa.rule.types.Term )tom__arg)))) {



        Term res = tom_make_TrueMatch();
        debug("optim match delete",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch47_7=tom_get_slot_Match_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch47_8=tom_get_slot_Match_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch47_7)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_7))) { sa.rule.types.TermList  tomMatch47_12=tom_get_slot_Appl_args(tomMatch47_7);if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch47_12))) {if (tom_is_empty_TermList_TermList(tomMatch47_12)) {if (tom_is_sort_Term(tomMatch47_8)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_8))) { sa.rule.types.TermList  tomMatch47_16=tom_get_slot_Appl_args(tomMatch47_8);if (tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch47_7), tom_get_slot_Appl_symbol(tomMatch47_8))) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch47_16))) {if (tom_is_empty_TermList_TermList(tomMatch47_16)) {



        Term res = tom_make_TrueMatch();
        debug("match delete",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch47_23=tom_get_slot_Match_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch47_24=tom_get_slot_Match_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch47_23)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_23))) { String  tom_f=tom_get_slot_Appl_symbol(tomMatch47_23);if (tom_is_sort_Term(tomMatch47_24)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_24))) {if (tom_equal_term_String(tom_f, tom_get_slot_Appl_symbol(tomMatch47_24))) {



        TermList tl1 = tom_get_slot_Appl_args(tomMatch47_23);
        TermList tl2 = tom_get_slot_Appl_args(tomMatch47_24);
        TermList newarg = tom_empty_list_TermList();
        while(!tl1.isEmptyTermList()) {
          Term h1 = tl1.getHeadTermList();
          Term h2 = tl2.getHeadTermList();
          newarg = tom_cons_list_TermList(tom_make_Match(h1,h2),tom_append_list_TermList(newarg,tom_empty_list_TermList())); // build in reverse order
          tl1 = tl1.getTailTermList();
          tl2 = tl2.getTailTermList();
        }
        newarg = newarg.reverse();
        Term res = tom_make_Appl(tom_f,newarg);
        debug("match decompose",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch47_37=tom_get_slot_Match_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch47_38=tom_get_slot_Match_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch47_37)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch47_37))) {boolean tomMatch47_46= false ; sa.rule.types.Term  tomMatch47_44= null ; sa.rule.types.Term  tomMatch47_45= null ;if (tom_is_sort_Term(tomMatch47_38)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_38))) {{tomMatch47_46= true ;tomMatch47_44=tomMatch47_38;}} else {if (tom_is_sort_Term(tomMatch47_38)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch47_38))) {{tomMatch47_46= true ;tomMatch47_45=tomMatch47_38;}}}}}if (tomMatch47_46) {



        Term res = tom_make_TrueMatch();
        debug("match",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch47_48=tom_get_slot_Match_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch47_49=tom_get_slot_Match_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch47_48)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_48))) {if (tom_is_sort_Term(tomMatch47_49)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch47_49))) {if (!(tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch47_49), tom_get_slot_Appl_symbol(tomMatch47_48)))) {



        Term res = tom_make_Empty();
        debug("match symbol clash",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_SimplifyMatch() { return new SimplifyMatch();}public static class PropagateTrueMatch extends tom.library.sl.AbstractStrategyBasic {public PropagateTrueMatch() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.TermList  tomMatch48_2=tom_get_slot_Appl_args((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch48_2))) {if (!(tom_is_empty_TermList_TermList(tomMatch48_2))) {







 // at least one argument
        boolean ok = true;
        TermList tl = tomMatch48_2;
        while(ok && !tl.isEmptyTermList()) {
          ok &= (tl.getHeadTermList() == tom_make_TrueMatch());
          tl = tl.getTailTermList();
        }
        if(ok) {
          Term res = tom_make_TrueMatch();
          debug("propagate match true",(( sa.rule.types.Term )tom__arg),res);
          return res;
        }
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_PropagateTrueMatch() { return new PropagateTrueMatch();}public static class SimplifyAddMatch extends tom.library.sl.AbstractStrategyBasic {public SimplifyAddMatch() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) { sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);







        assert !Tools.containsNamedVar(tom_s) : tom_s;
      }}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch49_2=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch49_2))) { sa.rule.types.AddList  tomMatch49_end_8=tomMatch49_2;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch49_end_8))) { sa.rule.types.Term  tomMatch49_11=tom_get_head_ConcAdd_AddList(tomMatch49_end_8);if (tom_is_sort_Term(tomMatch49_11)) {if (tom_is_fun_sym_TrueMatch((( sa.rule.types.Term )tomMatch49_11))) {



        Term res = tom_make_TrueMatch();
        debug("elim TrueMatch",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}if (tom_is_empty_ConcAdd_AddList(tomMatch49_end_8)) {tomMatch49_end_8=tomMatch49_2;} else {tomMatch49_end_8=tom_get_tail_ConcAdd_AddList(tomMatch49_end_8);}}} while(!(tom_equal_term_AddList(tomMatch49_end_8, tomMatch49_2)));}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch49_14=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch49_14))) { sa.rule.types.AddList  tomMatch49_end_20=tomMatch49_14;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch49_end_20))) { sa.rule.types.Term  tomMatch49_29=tom_get_head_ConcAdd_AddList(tomMatch49_end_20);if (tom_is_sort_Term(tomMatch49_29)) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )tomMatch49_29))) { sa.rule.types.Term  tomMatch49_27=tom_get_slot_Match_term1(tomMatch49_29); sa.rule.types.Term  tomMatch49_28=tom_get_slot_Match_term2(tomMatch49_29); sa.rule.types.Term  tom_t1=tomMatch49_27;if (tom_is_sort_Term(tomMatch49_28)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch49_28))) { String  tomMatch49_31=tom_get_slot_Var_name(tomMatch49_28);if ( true ) {if (tom_equal_term_String("_", tomMatch49_31)) { sa.rule.types.AddList  tomMatch49_21=tom_get_tail_ConcAdd_AddList(tomMatch49_end_20); sa.rule.types.AddList  tomMatch49_end_24=tomMatch49_21;do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch49_end_24))) { sa.rule.types.Term  tomMatch49_38=tom_get_head_ConcAdd_AddList(tomMatch49_end_24);if (tom_is_sort_Term(tomMatch49_38)) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )tomMatch49_38))) { sa.rule.types.Term  tomMatch49_36=tom_get_slot_Match_term1(tomMatch49_38); sa.rule.types.Term  tomMatch49_37=tom_get_slot_Match_term2(tomMatch49_38); sa.rule.types.Term  tom_t2=tomMatch49_36;if (tom_is_sort_Term(tomMatch49_37)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch49_37))) { String  tomMatch49_40=tom_get_slot_Var_name(tomMatch49_37);if ( true ) {if (tom_equal_term_String("_", tomMatch49_40)) {boolean tomMatch49_52= false ;if (tom_is_sort_Term(tomMatch49_36)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch49_36))) {if (tom_equal_term_Term(tom_t2, tomMatch49_36)) {tomMatch49_52= true ;}}}if (!(tomMatch49_52)) {boolean tomMatch49_51= false ;if (tom_is_sort_Term(tomMatch49_27)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch49_27))) {if (tom_equal_term_Term(tom_t1, tomMatch49_27)) {tomMatch49_51= true ;}}}if (!(tomMatch49_51)) {



        Term match = matchConstraint(tom_make_Add(tom_cons_list_ConcAdd(tom_t1,tom_cons_list_ConcAdd(tom_t2,tom_empty_list_ConcAdd()))),tomMatch49_28);
        Term res = tom_make_Add(tom_cons_list_ConcAdd(match,tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch49_14,tomMatch49_end_20,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tom_get_slice_ConcAdd(tomMatch49_21,tomMatch49_end_24,tom_empty_list_ConcAdd()),tom_append_list_ConcAdd(tom_get_tail_ConcAdd_AddList(tomMatch49_end_24),tom_empty_list_ConcAdd())))));
        debug("simplify add match",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}if (tom_is_empty_ConcAdd_AddList(tomMatch49_end_24)) {tomMatch49_end_24=tomMatch49_21;} else {tomMatch49_end_24=tom_get_tail_ConcAdd_AddList(tomMatch49_end_24);}}} while(!(tom_equal_term_AddList(tomMatch49_end_24, tomMatch49_21)));}}}}}}}if (tom_is_empty_ConcAdd_AddList(tomMatch49_end_20)) {tomMatch49_end_20=tomMatch49_14;} else {tomMatch49_end_20=tom_get_tail_ConcAdd_AddList(tomMatch49_end_20);}}} while(!(tom_equal_term_AddList(tomMatch49_end_20, tomMatch49_14)));}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_SimplifyAddMatch() { return new SimplifyAddMatch();}



  // check abstraction
  // a + b + g(_) + f(_) == signature ==> X
  public static class TryAbstraction extends tom.library.sl.AbstractStrategyBasic {private  Signature  eSig;public TryAbstraction( Signature  eSig) {super(tom_make_Identity());this.eSig=eSig;}public  Signature  geteSig() {return eSig;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.AddList  tomMatch50_1=tom_get_slot_Add_addlist((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch50_1))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch50_1))) { sa.rule.types.Term  tomMatch50_9=tom_get_head_ConcAdd_AddList(tomMatch50_1);if (tom_is_sort_Term(tomMatch50_9)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch50_9))) {


        GomType codomain = eSig.getCodomain(tom_get_slot_Appl_symbol(tomMatch50_9));
        if(codomain != null) {
          Set<String> foundConstructors = new HashSet<String>();
          for(Term t: tomMatch50_1.getCollectionConcAdd()) {
            // check that t is composed of variables only
            {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { sa.rule.types.TermList  tomMatch51_2=tom_get_slot_Appl_args((( sa.rule.types.Term )t));boolean tomMatch51_13= false ;if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch51_2))) { sa.rule.types.TermList  tomMatch51_end_8=tomMatch51_2;do {{if (!(tom_is_empty_TermList_TermList(tomMatch51_end_8))) {boolean tomMatch51_14= false ; sa.rule.types.Term  tomMatch51_11=tom_get_head_TermList_TermList(tomMatch51_end_8);if (tom_is_sort_Term(tomMatch51_11)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch51_11))) {tomMatch51_14= true ;}}if (!(tomMatch51_14)) {tomMatch51_13= true ;}}if (tom_is_empty_TermList_TermList(tomMatch51_end_8)) {tomMatch51_end_8=tomMatch51_2;} else {tomMatch51_end_8=tom_get_tail_TermList_TermList(tomMatch51_end_8);}}} while(!(tom_equal_term_TermList(tomMatch51_end_8, tomMatch51_2)));}if (!(tomMatch51_13)) {

                // add f to set of names
                foundConstructors.add(tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)));
              }}}}}}

          }

          if(foundConstructors.equals(eSig.getConstructors(codomain))) {
            //System.out.println("OPS = " + ops + " al = " + `al);
            Term res = tom_make_Var("_");
            debug("abstraction",(( sa.rule.types.Term )tom__arg),res);
            return res;
          }
        } else {
          // do nothing
          //System.out.println("f -> null " + `f);
        }
      }}}}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_TryAbstraction( Signature  t0) { return new TryAbstraction(t0);}



  public static boolean canBeRemoved2(Rule rule, RuleList ruleList, Signature eSig) {
    boolean res = false;

    rule = (Rule) Tools.removeVar(rule);
    ruleList = (RuleList) Tools.removeVar(ruleList);

    {{if (tom_is_sort_Rule(rule)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )rule))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )rule)))) {

        AddList constraint = tom_empty_list_ConcAdd();
        {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch53_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch53_end_4))) { sa.rule.types.Rule  tomMatch53_9=tom_get_head_ConcRule_RuleList(tomMatch53_end_4);if (tom_is_sort_Rule(tomMatch53_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch53_9))) {

            Term mc = matchConstraint(tom_get_slot_Rule_lhs(tomMatch53_9), tom_get_slot_Rule_lhs((( sa.rule.types.Rule )rule)));
            if(mc == tom_make_TrueMatch()) {
              res = true;
              return res;
            } else {
              constraint = tom_cons_list_ConcAdd(mc,tom_append_list_ConcAdd(constraint,tom_empty_list_ConcAdd()));
            }
          }}}if (tom_is_empty_ConcRule_RuleList(tomMatch53_end_4)) {tomMatch53_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch53_end_4=tom_get_tail_ConcRule_RuleList(tomMatch53_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch53_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}

        Term matchingProblem = tom_make_Add(constraint);
        if(matchingProblem == tom_make_Empty()) {
          res = false;
          return res;
        }
        try {
          Strategy S2 = tom_cons_list_ChoiceId(tom_make_CleanAdd(),tom_cons_list_ChoiceId(tom_make_PropagateEmpty(),tom_cons_list_ChoiceId(tom_make_PropagateTrueMatch(),tom_cons_list_ChoiceId(tom_make_SimplifyAddMatch(),tom_cons_list_ChoiceId(tom_make_VarAdd(),tom_cons_list_ChoiceId(tom_make_FactorizeAdd(),tom_cons_list_ChoiceId(tom_make_SimplifyMatch(),tom_cons_list_ChoiceId(tom_make_TryAbstraction(eSig),tom_empty_list_ChoiceId()))))))));
          Term sol = tom_make_InnermostId(S2).visitLight(matchingProblem);
          //sol = `RepeatId(OnceTopDownId(S2)).visitLight(matchingProblem);
          //System.out.println("case = " + Pretty.toString(`lhs));
          //System.out.println("matchingProblem = " + Pretty.toString(matchingProblem));
          if(sol == tom_make_TrueMatch()) {
            res = true;
          }
        } catch(VisitFailure e) {
          System.out.println("can be removed2 failure");
        }

      }}}}}


    return res;
  }

  public static boolean canBeRemoved3(Rule rule, RuleList ruleList, Signature eSig) {
    boolean res = false;

    rule = (Rule) Tools.removeVar(rule);
    ruleList = (RuleList) Tools.removeVar(ruleList);

    {{if (tom_is_sort_Rule(rule)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )rule))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )rule)))) {

        AddList sum = tom_empty_list_ConcAdd();
        {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch55_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch55_end_4))) { sa.rule.types.Rule  tomMatch55_9=tom_get_head_ConcRule_RuleList(tomMatch55_end_4);if (tom_is_sort_Rule(tomMatch55_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch55_9))) {

            sum = tom_cons_list_ConcAdd(tom_get_slot_Rule_lhs(tomMatch55_9),tom_append_list_ConcAdd(sum,tom_empty_list_ConcAdd()));
          }}}if (tom_is_empty_ConcRule_RuleList(tomMatch55_end_4)) {tomMatch55_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch55_end_4=tom_get_tail_ConcRule_RuleList(tomMatch55_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch55_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}

        Term problem = tom_make_Sub(tom_get_slot_Rule_lhs((( sa.rule.types.Rule )rule)),tom_make_Add(sum));
        //if(Main.options.verbose) {
        //  System.out.println("\nPROBLEM : " + Pretty.toString(problem));
        //}
        Term t = simplifySub(problem,eSig);
        //if(Main.options.verbose) {
        //  System.out.println("PROBLEM REDUCED : " + Pretty.toString(t));
        //}

        if(t == tom_make_Empty()) {
          res = true;
          return res;
        }
      }}}}}


    return res;
  }


  public static void searchAbstractionRule(HashSet<Rule> c, Rule rule, RuleList ruleList, Signature eSig) {
    {{if (tom_is_sort_Rule(rule)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )rule))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )rule)))) {

        AddList sum = tom_empty_list_ConcAdd();
        {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch57_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch57_end_4))) { sa.rule.types.Rule  tomMatch57_9=tom_get_head_ConcRule_RuleList(tomMatch57_end_4);if (tom_is_sort_Rule(tomMatch57_9)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch57_9))) {

            sum = tom_cons_list_ConcAdd(tom_get_slot_Rule_lhs(tomMatch57_9),tom_append_list_ConcAdd(sum,tom_empty_list_ConcAdd()));
          }}}if (tom_is_empty_ConcRule_RuleList(tomMatch57_end_4)) {tomMatch57_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch57_end_4=tom_get_tail_ConcRule_RuleList(tomMatch57_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch57_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}


        HashSet<Term> saturate = new HashSet<Term>();
        generateAbstraction(saturate,tom_get_slot_Rule_lhs((( sa.rule.types.Rule )rule)));
        //System.out.println("#saturate = " + saturate.size());
        for(Term newlhs:saturate) {
          Term problem = tom_make_Sub(newlhs,tom_make_Add(sum));
          if(simplifySub(problem,eSig) == tom_make_Empty()) {
            newlhs = Tools.normalizeVariable(newlhs);
            Rule newrule = tom_make_Rule(newlhs,tom_get_slot_Rule_rhs((( sa.rule.types.Rule )rule)));
            System.out.println("rule: " + Pretty.toString(rule));
            System.out.println("new candidate: " + Pretty.toString(newrule));
            c.add(newrule);
          }
        }

      }}}}}

  }

  private static Term simplifyAbstraction(Term sum, Signature eSig) {
    System.out.println("simplifyAbstraction");
    HashSet<Term> bag = new HashSet<Term>();
    {{if (tom_is_sort_Term(sum)) {if (tom_is_sort_Term((( sa.rule.types.Term )sum))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )sum)))) { sa.rule.types.AddList  tom_tl=tom_get_slot_Add_addlist((( sa.rule.types.Term )sum));

        AddList res = tom_tl;
        {{if (tom_is_sort_AddList(tom_tl)) {if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )(( sa.rule.types.AddList )tom_tl)))) { sa.rule.types.AddList  tomMatch59_end_4=(( sa.rule.types.AddList )tom_tl);do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch59_end_4))) {

            searchAbstractionTerm(bag,tom_get_head_ConcAdd_AddList(tomMatch59_end_4),sum,eSig);
          }if (tom_is_empty_ConcAdd_AddList(tomMatch59_end_4)) {tomMatch59_end_4=(( sa.rule.types.AddList )tom_tl);} else {tomMatch59_end_4=tom_get_tail_ConcAdd_AddList(tomMatch59_end_4);}}} while(!(tom_equal_term_AddList(tomMatch59_end_4, (( sa.rule.types.AddList )tom_tl))));}}}}

        for(Term t:bag) {
          System.out.println("new candidate: " + Pretty.toString(t));
          res = tom_cons_list_ConcAdd(t,tom_append_list_ConcAdd(res,tom_empty_list_ConcAdd()));
        }
        return tom_make_Add(res);
      }}}}}

    return sum;
  }

  /*
   * given subject and sum
   * search abstractions of subject such that [subject] \subseteq [sum]
   */
  public static void searchAbstractionTerm(HashSet<Term> c, Term subject, Term sum, Signature eSig) {
    HashSet<Term> saturate = new HashSet<Term>();
    generateAbstraction(saturate,subject);
    //System.out.println("#saturate = " + saturate.size());
    // TODO: order saturate to start with least general terms
    List<Term> list = new ArrayList<Term>(saturate);
    java.util.Comparator<Term> cmp = new java.util.Comparator<Term>() {
      public int compare(Term t1, Term t2) {
        if(matchConstraint(t1,t2)==tom_make_TrueMatch()) {
          return 1;
        } else if(matchConstraint(t2,t1)==tom_make_TrueMatch()) {
          return -1;
        } else {
          return 0;
        }
      }
    };

    java.util.Collections.sort(list, cmp);

    subject = (Term)Tools.removeAt(subject);
    subject = Tools.normalizeVariable(subject);
    //System.out.println("for: = " + Pretty.toString(subject));

    for(Term t:list) { // saturate
      t = (Term)Tools.removeAt(t);
      t = Tools.normalizeVariable(t);
      Term problem = tom_make_Sub(t,sum);
      //System.out.println("try: " + Pretty.toString(t));
      Term res = simplifySub(problem,eSig);
      res = (Term)Tools.removeAt(res);
      res = Tools.normalizeVariable(res);
      //System.out.println("res = " + Pretty.toString(res));
      if(res == tom_make_Empty()) {
        //System.out.println("new candidate: " + Pretty.toString(t));
        c.add(t);
        // continue the search for a more general term
      } else {
        // stop the search
        //return;
      }
    }



  }

  /*
   * given subject
   * generate all possible abstractions (no order)
   */
  private static void generateAbstraction(HashSet<Term> c, Term subject) {
    HashSet<Term> todo = new HashSet<Term>(); // terms to expand
    HashSet<Term> todo2 = new HashSet<Term>();
    HashSet<Term> tmpC = new HashSet<Term>(); // working memory when expanding a single term
    todo.add(subject);
    while(todo.size() > 0) {
      todo2.clear();
      for(Term t:todo) {
        tmpC.clear();
        try {
          // TopDownCollect: apply s1 in a top-down way, s should extends the identity
          // a failure stops the top-down process under this current node
          tom_make_TopDownCollect(tom_make_GenerateAbstraction(tmpC,t)).visit(t);
        } catch(VisitFailure e) {
        }
        if(tmpC.isEmpty()) {
          // t is a plain term
          c.add(t);
        } else {
          for(Term e:tmpC) {
            if(isPlainTerm(e)) {
              c.add(e);
            } else {
              // todo list for the next round
              todo2.add(e);
            }
          }
        }
      }
      // all terms of toto have been expanded
      // swap todo, todo2
      HashSet<Term> tmp = todo;
      todo = todo2; // new terms which have to be expanded
      todo2 = tmp;  // reuse toto set, will be cleared next round
      //System.out.println("size(c) = " + c.size());
    }

  }

  public static class GenerateAbstraction extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashSet  c;private  sa.rule.types.Term  subject;public GenerateAbstraction( java.util.HashSet  c,  sa.rule.types.Term  subject) {super(tom_make_Identity());this.c=c;this.subject=subject;}public  java.util.HashSet  getc() {return c;}public  sa.rule.types.Term  getsubject() {return subject;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        Term newt = (Term) getEnvironment().getPosition().getReplace(tom_make_Var(Tools.getName("Z"))).visit(subject);
        c.add(newt);
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_GenerateAbstraction( java.util.HashSet  t0,  sa.rule.types.Term  t1) { return new GenerateAbstraction(t0,t1);}



/*
 * more efficient version of SimplifyMatch (factor 2!)
 */

  /*
   * Return TrueMatch() if t1 matches t2
   *        Empty()     if t1 does not match t2
   *        a constraint if no decision can be taken
   */
  // TODO: can be  used to replace both the function match(Term,Term) and the strategy SimplifyMatch()
  private static boolean newVersion = true;
  private static Term matchConstraint(Term t1, Term t2) {
    if(!newVersion) {
      return tom_make_Match(t1,t2);
    }

    {{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) {if (tom_is_sort_Term(t2)) {

 return matchConstraint(tom_get_slot_At_term2((( sa.rule.types.Term )t1)),(( sa.rule.types.Term )t2)); }}}}}{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {
 return matchConstraint((( sa.rule.types.Term )t1),tom_get_slot_At_term2((( sa.rule.types.Term )t2))); }}}}}{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) { sa.rule.types.TermList  tomMatch61_15=tom_get_slot_Appl_args((( sa.rule.types.Term )t1));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch61_15))) {if (tom_is_empty_TermList_TermList(tomMatch61_15)) {if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) { sa.rule.types.TermList  tomMatch61_19=tom_get_slot_Appl_args((( sa.rule.types.Term )t2));if (tom_equal_term_String(tom_get_slot_Appl_symbol((( sa.rule.types.Term )t1)), tom_get_slot_Appl_symbol((( sa.rule.types.Term )t2)))) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch61_19))) {if (tom_is_empty_TermList_TermList(tomMatch61_19)) {


 return tom_make_TrueMatch(); }}}}}}}}}}}}{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) { String  tom_name=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t1));if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {if (tom_equal_term_String(tom_name, tom_get_slot_Appl_symbol((( sa.rule.types.Term )t2)))) {

 return propagateMatchConstraint(tom_make_Appl(tom_name,decomposeListConstraint(tom_get_slot_Appl_args((( sa.rule.types.Term )t1)),tom_get_slot_Appl_args((( sa.rule.types.Term )t2)),false))); }}}}}}}}{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) {if (tom_is_sort_Term(t2)) {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {if (!(tom_equal_term_String(tom_get_slot_Appl_symbol((( sa.rule.types.Term )t2)), tom_get_slot_Appl_symbol((( sa.rule.types.Term )t1))))) {

 return tom_make_Empty(); }}}}}}}}{if (tom_is_sort_Term(t1)) {if (tom_is_sort_Term((( sa.rule.types.Term )t1))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t1)))) {if (tom_is_sort_Term(t2)) {boolean tomMatch61_53= false ; sa.rule.types.Term  tomMatch61_51= null ; sa.rule.types.Term  tomMatch61_52= null ;if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {{tomMatch61_53= true ;tomMatch61_51=(( sa.rule.types.Term )t2);}} else {if (tom_is_sort_Term((( sa.rule.types.Term )t2))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t2)))) {{tomMatch61_53= true ;tomMatch61_52=(( sa.rule.types.Term )t2);}}}}}if (tomMatch61_53) {

 return tom_make_TrueMatch(); }}}}}}}

    return tom_make_Match(t1,t2);
  }

  private static Term propagateMatchConstraint(Term t) {
    try {
      Strategy S = tom_cons_list_ChoiceId(tom_make_PropagateEmpty(),tom_cons_list_ChoiceId(tom_make_PropagateTrueMatch(),tom_empty_list_ChoiceId()));
      return tom_make_InnermostId(S).visitLight(t);
    } catch(VisitFailure e) {
    }
    return t;
  }

  /*
   * return a list of matching constraints
   * TermList(Empty(),...,Empty()) when one of the constaint is Empty()
   */
  private static TermList decomposeListConstraint(TermList tl1, TermList tl2, boolean propagateEmpty) {
    {{if (tom_is_sort_TermList(tl1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl1)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl1))) {if (tom_is_sort_TermList(tl2)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl2)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl2))) {
 return tom_empty_list_TermList(); }}}}}}}{if (tom_is_sort_TermList(tl1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl1)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl1)))) { sa.rule.types.TermList  tom_tail1=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )tl1));if (tom_is_sort_TermList(tl2)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl2)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl2)))) { sa.rule.types.TermList  tom_tail2=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )tl2));

        if(propagateEmpty) {
          //return `TermList(Empty()); // optim: can be removed
          TermList tail = decomposeListConstraint(tom_tail1,tom_tail2,true);
          return tom_cons_list_TermList(tom_make_Empty(),tom_append_list_TermList(tail,tom_empty_list_TermList()));
        }

        Term res = matchConstraint(tom_get_head_TermList_TermList((( sa.rule.types.TermList )tl1)),tom_get_head_TermList_TermList((( sa.rule.types.TermList )tl2)));
        if(res == tom_make_Empty()) {
          //return `TermList(Empty()); // optim: can be removed
          TermList tail = decomposeListConstraint(tom_tail1,tom_tail2,true);
          return tom_cons_list_TermList(tom_make_Empty(),tom_append_list_TermList(tail,tom_empty_list_TermList()));
        }

        TermList tail = decomposeListConstraint(tom_tail1,tom_tail2,propagateEmpty);
        return tom_cons_list_TermList(res,tom_append_list_TermList(tail,tom_empty_list_TermList()));
      }}}}}}}}

    assert false : "should not be there";
    return null;
  }

  /*
   * Transform an ordered TRS with non-linear lhs rules into an ordered TRS with left-LINEAR rules
   */
  public static Trs transformNLOTRSintoLOTRS(Trs trs, Signature gSig) {

    {{if (tom_is_sort_Trs(trs)) {if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Otrs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) { sa.rule.types.RuleList  tomMatch63_1=tom_get_slot_Otrs_list((( sa.rule.types.Trs )trs));if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )tomMatch63_1))) {if (tom_is_empty_ConcRule_RuleList(tomMatch63_1)) {

        return (( sa.rule.types.Trs )trs);
      }}}}}}{if (tom_is_sort_Trs(trs)) {if (tom_is_sort_Trs((( sa.rule.types.Trs )trs))) {if (tom_is_fun_sym_Otrs((( sa.rule.types.Trs )(( sa.rule.types.Trs )trs)))) { sa.rule.types.RuleList  tomMatch63_6=tom_get_slot_Otrs_list((( sa.rule.types.Trs )trs));if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )tomMatch63_6))) {if (!(tom_is_empty_ConcRule_RuleList(tomMatch63_6))) { sa.rule.types.Rule  tomMatch63_14=tom_get_head_ConcRule_RuleList(tomMatch63_6);if (tom_is_sort_Rule(tomMatch63_14)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch63_14))) { sa.rule.types.Term  tom_lhs=tom_get_slot_Rule_lhs(tomMatch63_14); sa.rule.types.Term  tom_rhs=tom_get_slot_Rule_rhs(tomMatch63_14); sa.rule.types.RuleList  tom_tail=tom_get_tail_ConcRule_RuleList(tomMatch63_6);


        TermList result = Tools.linearize(tom_lhs, gSig);
        {{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.TermList  tomMatch64_2=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch64_2))) { sa.rule.types.Term  tomMatch64_6=tom_get_head_TermList_TermList(tomMatch64_2);if (tom_is_sort_Term(tomMatch64_6)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch64_6))) { String  tomMatch64_4=tom_get_slot_Appl_symbol(tomMatch64_6); sa.rule.types.TermList  tomMatch64_5=tom_get_slot_Appl_args(tomMatch64_6);if ( true ) {if (tom_equal_term_String("True", tomMatch64_4)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch64_5))) {if (tom_is_empty_TermList_TermList(tomMatch64_5)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch64_2))) {

            // lhs is linear
            RuleList newTail = transformNLOTRSintoLOTRS(tom_make_Otrs(tom_tail),gSig).getlist();
            RuleList res = tom_cons_list_ConcRule(tom_make_Rule(tom_lhs,tom_rhs),tom_append_list_ConcRule(newTail,tom_empty_list_ConcRule()));
            assert Tools.isLhsLinear(res);
            return tom_make_Otrs(res);
          }}}}}}}}}}}}{if (tom_is_sort_TermList(result)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )result)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )result)))) { sa.rule.types.Term  tomMatch64_17=tom_get_head_TermList_TermList((( sa.rule.types.TermList )result));if (tom_is_sort_Term(tomMatch64_17)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch64_17))) { String  tom_f=tom_get_slot_Appl_symbol(tomMatch64_17); sa.rule.types.TermList  tom_f_args=tom_get_slot_Appl_args(tomMatch64_17); sa.rule.types.TermList  tomMatch64_13=tom_get_tail_TermList_TermList((( sa.rule.types.TermList )result));if (!(tom_is_empty_TermList_TermList(tomMatch64_13))) { sa.rule.types.Term  tom_cond=tom_get_head_TermList_TermList(tomMatch64_13);boolean tomMatch64_27= false ; sa.rule.types.Term  tomMatch64_21=tom_get_head_TermList_TermList(tomMatch64_13);if (tom_is_sort_Term(tomMatch64_21)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch64_21))) { String  tomMatch64_19=tom_get_slot_Appl_symbol(tomMatch64_21); sa.rule.types.TermList  tomMatch64_20=tom_get_slot_Appl_args(tomMatch64_21);if ( true ) {if (tom_equal_term_String("True", tomMatch64_19)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch64_20))) {if (tom_is_empty_TermList_TermList(tomMatch64_20)) {if (tom_equal_term_Term(tom_cond, tom_get_head_TermList_TermList(tomMatch64_13))) {tomMatch64_27= true ;}}}}}}}if (!(tomMatch64_27)) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch64_13))) {


            /*
             * lhs is non linear
             * f(t1_1,...,t1_n) -> rhs_1
             * ...
             * f(tm_1,...,tm_n) -> rhs_m
             * becomes (with t1'_1,...,t1'_n linear):
             *   f(t1'_1,...,t1'_n) -> f_1(t1'_1,...,t1'_n, true ^ constraint on non linear variables)
             *   f_1(t1'_1,...,t1'_n, true) -> rhs_1
             *   f_1(t2_1,...,t1_n, false) -> rhs_2  \
             *   ...                                  | apply the algorithm recursively on these rules
             *   f_1(tm_1,...,tm_n, false) -> rhs_m  /
             */
            String f_1 = Tools.getName(Tools.addAuxExtension(tom_f));
            GomTypeList f_domain = gSig.getDomain(tom_f);
            GomType f_codomain = gSig.getCodomain(tom_f);
            gSig.addFunctionSymbol(f_1,tom_append_list_ConcGomType(f_domain,tom_cons_list_ConcGomType(Signature.TYPE_BOOLEAN,tom_empty_list_ConcGomType())),f_codomain);
            Term newRhs = tom_make_Appl(f_1,tom_append_list_TermList(tom_f_args,tom_cons_list_TermList(tom_cond,tom_empty_list_TermList())));
            int arity = gSig.getArity(tom_f);
            RuleList newTail = transformNLOTRSintoLOTRS(tom_make_Otrs(transformHeadSymbol(tom_tail,tom_f,f_1,arity)),gSig).getlist();
            Rule trueCase = tom_make_Rule(tom_make_Appl(f_1,tom_append_list_TermList(tom_f_args,tom_cons_list_TermList(tom_make_Appl("True",tom_empty_list_TermList()),tom_empty_list_TermList()))),tom_rhs);
            RuleList res = tom_cons_list_ConcRule(tom_make_Rule(tom_get_head_TermList_TermList((( sa.rule.types.TermList )result)),newRhs),tom_cons_list_ConcRule(trueCase,tom_append_list_ConcRule(newTail,tom_empty_list_ConcRule())));
            assert Tools.isLhsLinear(res);
            return tom_make_Otrs(res);
          }}}}}}}}}}


      }}}}}}}}}


    assert Tools.isLhsLinear(trs);
    return trs;
  }

  private static RuleList transformHeadSymbol(RuleList ruleList, String oldSymbol, String newSymbol, int old_arity) {
    {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList))) {

        return ruleList;
      }}}}{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.Rule  tomMatch65_8=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList));if (tom_is_sort_Rule(tomMatch65_8)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch65_8))) { sa.rule.types.Term  tomMatch65_6=tom_get_slot_Rule_lhs(tomMatch65_8);if (tom_is_sort_Term(tomMatch65_6)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch65_6))) {


        RuleList newTail = transformHeadSymbol(tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),oldSymbol, newSymbol, old_arity);
        if(tom_get_slot_Appl_symbol(tomMatch65_6)== oldSymbol) {
          return tom_cons_list_ConcRule(tom_make_Rule(tom_make_Appl(newSymbol,tom_append_list_TermList(tom_get_slot_Appl_args(tomMatch65_6),tom_cons_list_TermList(tom_make_Appl("False",tom_empty_list_TermList()),tom_empty_list_TermList()))),tom_get_slot_Rule_rhs(tomMatch65_8)),tom_append_list_ConcRule(newTail,tom_empty_list_ConcRule()));
        } else {
          return tom_cons_list_ConcRule(tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),tom_append_list_ConcRule(newTail,tom_empty_list_ConcRule()));

        }
      }}}}}}}}{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) {if (!(tom_is_empty_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.Rule  tomMatch65_20=tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList));if (tom_is_sort_Rule(tomMatch65_20)) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )tomMatch65_20))) { sa.rule.types.Term  tomMatch65_18=tom_get_slot_Rule_lhs(tomMatch65_20);if (tom_is_sort_Term(tomMatch65_18)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch65_18))) {





        RuleList newTail = transformHeadSymbol(tom_get_tail_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),oldSymbol, newSymbol, old_arity);
        TermList args = tom_cons_list_TermList(tom_make_Appl("False",tom_empty_list_TermList()),tom_empty_list_TermList());
        for(int i=0 ; i<old_arity ; i++) {
          args = tom_cons_list_TermList(tom_make_Var(Tools.getName("Z")),tom_append_list_TermList(args,tom_empty_list_TermList()));
        }
        return tom_cons_list_ConcRule(tom_make_Rule(tom_make_Appl(newSymbol,args),tom_get_slot_Rule_rhs(tomMatch65_20)),tom_cons_list_ConcRule(tom_get_head_ConcRule_RuleList((( sa.rule.types.RuleList )ruleList)),tom_append_list_ConcRule(newTail,tom_empty_list_ConcRule())));
      }}}}}}}}}


    return ruleList;
  }

  /*
   * predicates for assert
   */

  /*
   * returns true if the term does not contain any Add or Sub
   */
  private static boolean isPlainTerm(Term t) {
    try {
      tom_make_TopDown(tom_make_PlainTerm()).visitLight(t);
    } catch(VisitFailure e) {
      return false;
    }
    return true;
  }

  public static class PlainTerm extends tom.library.sl.AbstractStrategyBasic {public PlainTerm() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {boolean tomMatch66_4= false ; sa.rule.types.Term  tomMatch66_3= null ; sa.rule.types.Term  tomMatch66_2= null ;if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {{tomMatch66_4= true ;tomMatch66_2=(( sa.rule.types.Term )tom__arg);}} else {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {{tomMatch66_4= true ;tomMatch66_3=(( sa.rule.types.Term )tom__arg);}}}}}if (tomMatch66_4) {tom_make_Fail()


.visitLight((( sa.rule.types.Term )tom__arg));
      }}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_PlainTerm() { return new PlainTerm();}




  /*
   * returns true if contains no Add/Sub (except at top level)
   */
  private static boolean onlyTopLevelAdd(Term subject) {
    {{if (tom_is_sort_Term(subject)) {if (tom_is_sort_Term((( sa.rule.types.Term )subject))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )subject)))) {

        boolean res = true;
        for(Term t:tom_get_slot_Add_addlist((( sa.rule.types.Term )subject)).getCollectionConcAdd()) {
          res &= isPlainTerm(t);
        }
        return res;
      }}}}{if (tom_is_sort_Term(subject)) {if (tom_is_sort_Term((( sa.rule.types.Term )subject))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )subject)))) {

        return false;
      }}}}}

    return true;
  }

  /**
    * experiments with set
    */

  public static class SimplifySet extends tom.library.sl.AbstractStrategyBasic {private  Signature  eSig;public SimplifySet( Signature  eSig) {super(tom_make_Identity());this.eSig=eSig;}public  Signature  geteSig() {return eSig;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {



        Term res = tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));
        debug("remove at",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_7=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_7)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch68_7))) {



        Term res = tom_make_Empty();
        debug("t ^ empty -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_13=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_13)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch68_13))) {



        Term res = tom_make_Empty();
        debug("empty ^ t -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_21=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_21)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch68_21))) {



        Term res = tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg));
        debug("t ^ x -> t",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_27=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_27)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch68_27))) {



        Term res = tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));
        debug("x ^ t -> t",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_34=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_35=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_34)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_34))) {if (tom_is_sort_Term(tomMatch68_35)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_35))) {if (!(tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch68_35), tom_get_slot_Appl_symbol(tomMatch68_34)))) {



        Term res = tom_make_Empty();
        debug("inter fail",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_47=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_48=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_47)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_47))) { String  tom_f=tom_get_slot_Appl_symbol(tomMatch68_47);if (tom_is_sort_Term(tomMatch68_48)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_48))) {if (tom_equal_term_String(tom_f, tom_get_slot_Appl_symbol(tomMatch68_48))) {



        Term res = tom_make_Appl(tom_f,zipInter(tom_get_slot_Appl_args(tomMatch68_47),tom_get_slot_Appl_args(tomMatch68_48)));
        debug("inter match",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_62=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_62)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch68_62))) { sa.rule.types.AddList  tomMatch68_65=tom_get_slot_Add_addlist(tomMatch68_62);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch68_65))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch68_65))) {



        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Inter(tom_t,tom_get_head_ConcAdd_AddList(tomMatch68_65)),tom_cons_list_ConcAdd(tom_make_Inter(tom_t,tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch68_65))),tom_empty_list_ConcAdd())));
        debug("inter distrib1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_72=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_73=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_72)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch68_72))) { sa.rule.types.AddList  tomMatch68_76=tom_get_slot_Add_addlist(tomMatch68_72);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch68_76))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch68_76))) {if (tom_is_sort_Term(tomMatch68_73)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_73))) { sa.rule.types.Term  tom_t=tomMatch68_73;



        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Inter(tom_get_head_ConcAdd_AddList(tomMatch68_76),tom_t),tom_cons_list_ConcAdd(tom_make_Inter(tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch68_76)),tom_t),tom_empty_list_ConcAdd())));
        debug("inter distrib2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_85=tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_85)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch68_85))) {



        Term res = tom_make_At(tom_get_slot_At_term1(tomMatch68_85),tom_make_Inter(tom_get_slot_At_term2(tomMatch68_85),tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg))));
        debug("at ^",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_95=tom_get_slot_Inter_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_95)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch68_95))) {



        Term res = tom_make_At(tom_get_slot_At_term1(tomMatch68_95),tom_make_Inter(tom_get_slot_Inter_term1((( sa.rule.types.Term )tom__arg)),tom_get_slot_At_term2(tomMatch68_95)));
        debug("at2 ^",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_104=tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_104)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch68_104))) {




        Term res = tom_make_Empty();
        debug("elim at",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_111=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_111)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch68_111))) {





        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Empty();
        debug("t - x -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_118=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_118)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch68_118))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_t;
        debug("t - empty -> t",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_124=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_124)) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )tomMatch68_124))) {



        Term res = tom_make_Empty();
        debug("empty - t -> empty",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_132=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_132)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch68_132))) { sa.rule.types.AddList  tomMatch68_135=tom_get_slot_Add_addlist(tomMatch68_132);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch68_135))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch68_135))) { sa.rule.types.Term  tom_head=tom_get_head_ConcAdd_AddList(tomMatch68_135);



        assert !Tools.containsSub(tom_t) : tom_t;
        assert !Tools.containsSub(tom_head) : tom_head;
        Term res = tom_make_Inter(tom_make_Sub(tom_t,tom_head),tom_make_Sub(tom_t,tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch68_135))));
        debug("sub distrib1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_142=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_143=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_142)) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )tomMatch68_142))) { sa.rule.types.AddList  tomMatch68_146=tom_get_slot_Add_addlist(tomMatch68_142);if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch68_146))) {if (!(tom_is_empty_ConcAdd_AddList(tomMatch68_146))) { sa.rule.types.Term  tom_head=tom_get_head_ConcAdd_AddList(tomMatch68_146);if (tom_is_sort_Term(tomMatch68_143)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_143))) { sa.rule.types.Term  tom_t=tomMatch68_143;



        assert !Tools.containsSub(tom_head) : tom_head;
        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Sub(tom_head,tom_t),tom_cons_list_ConcAdd(tom_make_Sub(tom_make_Add(tom_get_tail_ConcAdd_AddList(tomMatch68_146)),tom_t),tom_empty_list_ConcAdd())));
        debug("sub distrib4",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_158=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_158)) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )tomMatch68_158))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Sub(tom_t,tom_get_slot_Inter_term1(tomMatch68_158)),tom_cons_list_ConcAdd(tom_make_Sub(tom_t,tom_get_slot_Inter_term2(tomMatch68_158)),tom_empty_list_ConcAdd())));
        debug("sub distrib2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_167=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_167)) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )tomMatch68_167))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        Term res = tom_make_Add(tom_cons_list_ConcAdd(tom_make_Inter(tom_t,tom_get_slot_Sub_term1(tomMatch68_167)),tom_cons_list_ConcAdd(tom_make_Sub(tom_t,tom_get_slot_Sub_term2(tomMatch68_167)),tom_empty_list_ConcAdd())));
        debug("sub distrib3",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_175=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_176=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_175)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch68_175))) {if (tom_is_sort_Term(tomMatch68_176)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_176))) { sa.rule.types.Term  tom_t=tomMatch68_176;




        // replace X \ t by X@((a+b+g(_)+f(_,_)) \ t)
        // this version is as efficient as the previous one, but simpler
        assert !Tools.containsSub(tom_t) : tom_t;
        AddList al = tom_empty_list_ConcAdd();
        GomType codomain = eSig.getCodomain(tom_get_slot_Appl_symbol(tomMatch68_176)); // use codomain to generate well typed terms
        for(String name: eSig.getConstructors(codomain)) {
          int arity = eSig.getArity(name);
          Term expand = Tools.genAbstractTerm(name,arity, Tools.getName("Z"));
          al = tom_cons_list_ConcAdd(expand,tom_append_list_ConcAdd(al,tom_empty_list_ConcAdd()));
        }
        Term res = tom_make_Sub(tom_make_Add(al),tom_t);
        debug("expand AP2", (( sa.rule.types.Term )tom__arg),res);
        // generate X@Add(tl)
        res = tom_make_At(tomMatch68_175,res);
        return res;
      }}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_186=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_187=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_186)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_186))) { sa.rule.types.Term  tom_t=tomMatch68_186;if (tom_is_sort_Term(tomMatch68_187)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_187))) { sa.rule.types.Term  tom_u=tomMatch68_187;if (!(tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch68_187), tom_get_slot_Appl_symbol(tomMatch68_186)))) {



        assert !Tools.containsSub(tom_t) : tom_t;
        assert !Tools.containsSub(tom_u) : tom_u;
        Term res = tom_t;
        debug("sub elim1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_199=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tomMatch68_200=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_199)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_199))) { sa.rule.types.Term  tom_t1=tomMatch68_199;if (tom_is_sort_Term(tomMatch68_200)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch68_200))) {if (tom_equal_term_String(tom_get_slot_Appl_symbol(tomMatch68_199), tom_get_slot_Appl_symbol(tomMatch68_200))) { sa.rule.types.Term  tom_t2=tomMatch68_200;



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        //Term res = `sub(t1,t2);
        Term res = subopt(tom_t1,tom_t2,eSig);
        debug("sub1",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_213=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_213)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch68_213))) { sa.rule.types.Term  tom_t1=tom_get_slot_At_term2(tomMatch68_213); sa.rule.types.Term  tom_t2=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg));



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        Term res = tom_make_At(tom_get_slot_At_term1(tomMatch68_213),tom_make_Sub(tom_t1,tom_t2));
        debug("at",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch68_223=tom_get_slot_Sub_term2((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_t1=tom_get_slot_Sub_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch68_223)) {if (tom_is_fun_sym_At((( sa.rule.types.Term )tomMatch68_223))) { sa.rule.types.Term  tom_t2=tom_get_slot_At_term2(tomMatch68_223);



        assert !Tools.containsSub(tom_t1):tom_t1;
        assert !Tools.containsSub(tom_t2):tom_t2;
        Term res = tom_make_Sub(tom_t1,tom_t2);
        debug("at2",(( sa.rule.types.Term )tom__arg),res);
        return res;
      }}}}}}}return _visit_Term(tom__arg,introspector);}}




  private static TermList zipInter(TermList tl1, TermList tl2) {
    {{if (tom_is_sort_TermList(tl1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl1)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl1))) {if (tom_is_sort_TermList(tl2)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl2)))) {if (tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl2))) {
 return tl1; }}}}}}}{if (tom_is_sort_TermList(tl1)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl1)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl1)))) {if (tom_is_sort_TermList(tl2)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tl2)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tl2)))) {

        TermList tmp = zipInter(tom_get_tail_TermList_TermList((( sa.rule.types.TermList )tl1)),tom_get_tail_TermList_TermList((( sa.rule.types.TermList )tl2)));
        return tom_cons_list_TermList(tom_make_Inter(tom_get_head_TermList_TermList((( sa.rule.types.TermList )tl1)),tom_get_head_TermList_TermList((( sa.rule.types.TermList )tl2))),tom_append_list_TermList(tmp,tom_empty_list_TermList()));
      }}}}}}}}

    return null;
  }



}
