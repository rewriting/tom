package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.*;

public class Pretty {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Var( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Var) ;}private static  String  tom_get_slot_Var_name( sa.rule.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_BuiltinInt( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.BuiltinInt) ;}private static  int  tom_get_slot_BuiltinInt_i( sa.rule.types.Term  t) {return  t.geti() ;}private static boolean tom_is_fun_sym_Anti( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Anti) ;}private static  sa.rule.types.Term  tom_get_slot_Anti_term( sa.rule.types.Term  t) {return  t.getterm() ;}private static boolean tom_is_fun_sym_At( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.At) ;}private static  sa.rule.types.Term  tom_get_slot_At_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_At_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Add( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Add) ;}private static  sa.rule.types.AddList  tom_get_slot_Add_addlist( sa.rule.types.Term  t) {return  t.getaddlist() ;}private static boolean tom_is_fun_sym_Sub( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Sub) ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Sub_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Inter( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Inter) ;}private static  sa.rule.types.Term  tom_get_slot_Inter_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Inter_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Match( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Match) ;}private static  sa.rule.types.Term  tom_get_slot_Match_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_Match_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_TrueMatch( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.TrueMatch) ;}private static boolean tom_is_fun_sym_Empty( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Empty) ;}private static boolean tom_is_fun_sym_Rule( sa.rule.types.Rule  t) {return  (t instanceof sa.rule.types.rule.Rule) ;}private static  sa.rule.types.Term  tom_get_slot_Rule_lhs( sa.rule.types.Rule  t) {return  t.getlhs() ;}private static  sa.rule.types.Term  tom_get_slot_Rule_rhs( sa.rule.types.Rule  t) {return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConcAdd( sa.rule.types.AddList  t) {return  ((t instanceof sa.rule.types.addlist.ConsConcAdd) || (t instanceof sa.rule.types.addlist.EmptyConcAdd)) ;}private static  sa.rule.types.AddList  tom_empty_list_ConcAdd() { return  sa.rule.types.addlist.EmptyConcAdd.make() ;}private static  sa.rule.types.AddList  tom_cons_list_ConcAdd( sa.rule.types.Term  e,  sa.rule.types.AddList  l) { return  sa.rule.types.addlist.ConsConcAdd.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getHeadConcAdd() ;}private static  sa.rule.types.AddList  tom_get_tail_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getTailConcAdd() ;}private static boolean tom_is_empty_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.isEmptyConcAdd() ;}   private static   sa.rule.types.AddList  tom_append_list_ConcAdd( sa.rule.types.AddList l1,  sa.rule.types.AddList  l2) {     if( l1.isEmptyConcAdd() ) {       return l2;     } else if( l2.isEmptyConcAdd() ) {       return l1;     } else if(  l1.getTailConcAdd() .isEmptyConcAdd() ) {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,l2) ;     } else {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,tom_append_list_ConcAdd( l1.getTailConcAdd() ,l2)) ;     }   }   private static   sa.rule.types.AddList  tom_get_slice_ConcAdd( sa.rule.types.AddList  begin,  sa.rule.types.AddList  end, sa.rule.types.AddList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAdd()  ||  (end==tom_empty_list_ConcAdd()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.addlist.ConsConcAdd.make( begin.getHeadConcAdd() ,( sa.rule.types.AddList )tom_get_slice_ConcAdd( begin.getTailConcAdd() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(v,tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}   private static boolean tom_equal_term_Collection(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Collection(Object t) {return  t instanceof java.util.Collection ;} 



  private static String generateJavaName(String name) {
    name = name.replace('-','_');
    return name;
  }

  /*
  public static String toString(Expression e) {
    StringBuffer sb = new StringBuffer();
    %match(e) {
      Set(rulelist) -> {
        sb.append("{ ");
        %match(rulelist) {
          ConcRule(_*,x,end*) -> {
            sb.append(toString(`x));
            if(!`end.isEmptyConcRule()) {
              sb.append(", ");
            }
          }
        }
        sb.append(" }");
      }

      Strat(s) -> {
        sb.append(`s);
      }
    }
    return sb.toString();
  }
  */

  public static String toString(Rule r) {
    {{if (tom_is_sort_Rule(r)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )r))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )r)))) {

        return toString(tom_get_slot_Rule_lhs((( sa.rule.types.Rule )r))) + " -> " + toString(tom_get_slot_Rule_rhs((( sa.rule.types.Rule )r)));
      }}}}}

    return "toString(Rule): error";
  }

  public static String toString(RuleList rules) {
    String res = "";
    for(Rule r: rules.getCollectionConcRule()) {
      res += toString(r) + "\n";
    }
    return res;
  }

  public static String addBrace(String name) {
    if(Main.options.aprove || Main.options.timbuk) {
      return name;
    } else {
      return name + "()";
    }
  }

  public static String toString(TermList t) {
    return toStringAux(t, ",");
  }

  private static String toStringAux(TermList t, String sep) {
    StringBuffer sb = new StringBuffer();
    {{if (tom_is_sort_TermList(t)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )t)))) { sa.rule.types.TermList  tomMatch17_end_4=(( sa.rule.types.TermList )t);do {{if (!(tom_is_empty_TermList_TermList(tomMatch17_end_4))) {

        sb.append(toString(tom_get_head_TermList_TermList(tomMatch17_end_4)));
        if(!tom_get_tail_TermList_TermList(tomMatch17_end_4).isEmptyTermList()) {
          sb.append(sep);
        }
      }if (tom_is_empty_TermList_TermList(tomMatch17_end_4)) {tomMatch17_end_4=(( sa.rule.types.TermList )t);} else {tomMatch17_end_4=tom_get_tail_TermList_TermList(tomMatch17_end_4);}}} while(!(tom_equal_term_TermList(tomMatch17_end_4, (( sa.rule.types.TermList )t))));}}}}

    return sb.toString();
  }

  private static String toString(AddList t) {
    StringBuffer sb = new StringBuffer();
    {{if (tom_is_sort_AddList(t)) {if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )(( sa.rule.types.AddList )t)))) { sa.rule.types.AddList  tomMatch18_end_4=(( sa.rule.types.AddList )t);do {{if (!(tom_is_empty_ConcAdd_AddList(tomMatch18_end_4))) {

        sb.append(toString(tom_get_head_ConcAdd_AddList(tomMatch18_end_4)));
        if(!tom_get_tail_ConcAdd_AddList(tomMatch18_end_4).isEmptyConcAdd()) {
          sb.append("+");
        }
      }if (tom_is_empty_ConcAdd_AddList(tomMatch18_end_4)) {tomMatch18_end_4=(( sa.rule.types.AddList )t);} else {tomMatch18_end_4=tom_get_tail_ConcAdd_AddList(tomMatch18_end_4);}}} while(!(tom_equal_term_AddList(tomMatch18_end_4, (( sa.rule.types.AddList )t))));}}}}

    return sb.toString();
  }

  public static String toString(Term t) {
    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {
 return tom_get_slot_Var_name((( sa.rule.types.Term )t)); }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_BuiltinInt((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

 return ""+tom_get_slot_BuiltinInt_i((( sa.rule.types.Term )t)); }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

 return "!" + toString(tom_get_slot_Anti_term((( sa.rule.types.Term )t))); }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {

 return toString(tom_get_slot_At_term1((( sa.rule.types.Term )t))) + "@" + toString(tom_get_slot_At_term2((( sa.rule.types.Term )t))); }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { sa.rule.types.TermList  tom_args=tom_get_slot_Appl_args((( sa.rule.types.Term )t));


        String name = generateJavaName(tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)));
        if(tom_args.isEmptyTermList()) {
          return addBrace(name);
        } else {
          return name + "(" + toString(tom_args) + ")";
        }
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Add((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { sa.rule.types.AddList  tomMatch19_23=tom_get_slot_Add_addlist((( sa.rule.types.Term )t));if (tom_is_fun_sym_ConcAdd((( sa.rule.types.AddList )tomMatch19_23))) {


        return "(" + toString(tomMatch19_23) + ")";
      }}}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Sub((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return "(" + toString(tom_get_slot_Sub_term1((( sa.rule.types.Term )t))) + " \\ " + toString(tom_get_slot_Sub_term2((( sa.rule.types.Term )t))) + ")";
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Inter((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return "(" + toString(tom_get_slot_Inter_term1((( sa.rule.types.Term )t))) + " ^ " + toString(tom_get_slot_Inter_term2((( sa.rule.types.Term )t))) + ")";
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Empty((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return "empty";
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_TrueMatch((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return "TrueMatch";
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Match((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) {


        return "(" + toString(tom_get_slot_Match_term1((( sa.rule.types.Term )t))) + " << " + toString(tom_get_slot_Match_term2((( sa.rule.types.Term )t))) + ")";
      }}}}}


    return "toString(Term): error";
  }

  // Collect the variables
  public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  varSet;public CollectVars( java.util.Collection  varSet) {super(tom_make_Identity());this.varSet=varSet;}public  java.util.Collection  getvarSet() {return varSet;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        varSet.add(tom_get_slot_Var_name((( sa.rule.types.Term )tom__arg)));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectVars( java.util.Collection  t0) { return new CollectVars(t0);}



  public static String generateAprove(RuleList ruleList, boolean innermost)
    throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    rulesb.append("\n(RULES\n");
    {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch21_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch21_end_4))) { sa.rule.types.Rule  tom_r=tom_get_head_ConcRule_RuleList(tomMatch21_end_4);tom_make_BottomUp(tom_make_CollectVars(varSet))

.visit(tom_r);
        rulesb.append("        " + toString(tom_r) + "\n");
      }if (tom_is_empty_ConcRule_RuleList(tomMatch21_end_4)) {tomMatch21_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch21_end_4=tom_get_tail_ConcRule_RuleList(tomMatch21_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch21_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}

    rulesb.append(")\n");

    StringBuffer varsb = new StringBuffer();
    if(innermost) {
      varsb.append("(STRATEGY INNERMOST)\n");
    }
    varsb.append("(VAR ");
    for(String name: varSet) {
      varsb.append(name + " ");
    }
    varsb.deleteCharAt(varsb.length()-1);
    varsb.append(")");

    return varsb.toString()+rulesb.toString();
  }

  public static String generateTimbuk(RuleList ruleList, Signature generatedSignature) throws VisitFailure {
    StringBuffer rulesb = new StringBuffer();
    StringBuffer opsb = new StringBuffer();
    StringBuffer varsb = new StringBuffer();
    Collection<String> varSet = new HashSet<String>();

    opsb.append("\nOps\n");
    for(String name: generatedSignature.getSymbols()) {
      opsb.append(generateJavaName(name)  + ":" + generatedSignature.getArity(name) + " ");
    }

    rulesb.append("\nTRS R\n");
    {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch22_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch22_end_4))) { sa.rule.types.Rule  tom_r=tom_get_head_ConcRule_RuleList(tomMatch22_end_4);tom_make_BottomUp(tom_make_CollectVars(varSet))

.visit(tom_r);
        rulesb.append("        " + toString(tom_r) + "\n");
      }if (tom_is_empty_ConcRule_RuleList(tomMatch22_end_4)) {tomMatch22_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch22_end_4=tom_get_tail_ConcRule_RuleList(tomMatch22_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch22_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}


    varsb.append("\nVars\n");
    for(String name: varSet) {
      varsb.append(name + " ");
    }
    varsb.deleteCharAt(varsb.length()-1);

    return opsb.toString() + "\n" + varsb.toString() + "\n" + rulesb.toString();
  }

  public static String generateTom(Set<String> strategyNames, RuleList ruleList, Signature esig, Signature gsig) {
    String classname = Main.options.classname;
    boolean isTyped = Main.options.withType;
    System.out.println("--------- TOM ----------------------");
    //     System.out.println("RULEs: " + toString(ruleList));

    StringBuffer sb = new StringBuffer();
    String lowercaseClassname = classname.toLowerCase();
    sb.append(
        "\nimport "+lowercaseClassname+".m.types.*;\nimport java.io.*;\npublic class "+classname+" {\n  %gom {\n    module m\n      abstract syntax\n"






);
    // generate signature
    for(GomType codomain: gsig.getCodomains()) {
      sb.append("      " + codomain.getName() + " = \n");
      for(String name: gsig.getSymbols(codomain)) {
        int arity = gsig.getArity(name);
        GomTypeList domain = gsig.getDomain(name);
        String args = "";
        int i = 0;
        while(!domain.isEmptyConcGomType()) {
          String argTypeName = domain.getHeadConcGomType().getName();
          args += "kid" + i + "_" + argTypeName + ":" + argTypeName;
          if(i+1<arity) { args += ","; }
          domain = domain.getTailConcGomType();
          i++;
        }
        sb.append("        | " + generateJavaName(name) + "(" + args + ")\n");
      }
    }

    // generate rules
    sb.append("      module m:rules() {\n");
    {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch23_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch23_end_4))) {

        sb.append("        " + toString(tom_get_head_ConcRule_RuleList(tomMatch23_end_4)) + "\n");
      }if (tom_is_empty_ConcRule_RuleList(tomMatch23_end_4)) {tomMatch23_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch23_end_4=tom_get_tail_ConcRule_RuleList(tomMatch23_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch23_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}


    // generate main
    sb.append("\n    }\n  }\n\n  public static void main(String[] args) {\n    try {\n      BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));\n      Object input = "+classname+".fromString(reader.readLine());\n      long start = System.currentTimeMillis();\n      Object t = "+classname+".mainStrat(input);\n      System.out.println(t);\n      long stop = System.currentTimeMillis();\n      System.out.println(\"time1 (ms): \" + ((stop-start)));\n    } catch (IOException e) {\n      e.printStackTrace();\n    }\n  }\n  "
















);

    for(String strategyName:strategyNames) {
      // generate strategyName
      sb.append("\n  public static Object "+strategyName+"(Object t) {"
);

      if(isTyped) {
        for(GomType codomain: esig.getCodomains()) {
      sb.append("\n    if(t instanceof "+codomain.getName()+") {\n      return `"+strategyName+"_"+codomain.getName()+"(("+codomain.getName()+")t);\n    }"


);
      }
      sb.append("\n    throw new RuntimeException(\"cannot find a mainstrat for: \" + t);"
);
      }

      if(isTyped) {
        // generate nothing
      } else if(Main.options.metalevel) {
        sb.append("\n  \treturn `decode("+strategyName+"(encode((Term)t)));"
);
      } else {
        sb.append("\n    return `"+strategyName+"((Term) t);"
);
      }
      sb.append("\n  }\n  "

); // end strategyName
    }

  // generate fromString
  sb.append("\n  public static Object fromString(String s) {\n  "

);

  if(isTyped) {
    for(GomType codomain: esig.getCodomains()) {
      sb.append("\n    try {\n      return "+codomain.getName()+".fromString(s);\n    } catch(IllegalArgumentException e) {\n    }\n      "




);
    }

    sb.append("\n    throw new RuntimeException(\"cannot find a valid type for: \" + s);\n    "

);

  } else {
    sb.append("\n    return Term.fromString(s);\n    "

);

  }

  sb.append("\n  }\n  "

); // end fromString


  sb.append("\n}\n  "

); // end class

    return sb.toString();
  }

}
