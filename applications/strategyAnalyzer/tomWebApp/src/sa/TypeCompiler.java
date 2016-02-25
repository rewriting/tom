package sa;

import sa.rule.types.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import tom.library.sl.*;
/*import aterm.*;
import aterm.pure.*;*/

public class TypeCompiler {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Var( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Var) ;}private static  String  tom_get_slot_Var_name( sa.rule.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_Rule( sa.rule.types.Rule  t) {return  (t instanceof sa.rule.types.rule.Rule) ;}private static  sa.rule.types.Rule  tom_make_Rule( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.rule.Rule.make(t0, t1) ;}private static  sa.rule.types.Term  tom_get_slot_Rule_lhs( sa.rule.types.Rule  t) {return  t.getlhs() ;}private static  sa.rule.types.Term  tom_get_slot_Rule_rhs( sa.rule.types.Rule  t) {return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}   

//   %include { java/util/types/Map.tom }
//   %include { java/util/types/List.tom }
//   %include { java/util/types/ArrayList.tom }

  

  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The typed signature
  private Signature typedSignature;
  // The generated (ordered) TRS
  private RuleList generatedRules;

  public TypeCompiler(Signature extractedSignature) {
    this.extractedSignature = extractedSignature;
    this.typedSignature = new Signature(extractedSignature);
    this.generatedRules = tom_empty_list_ConcRule();
  }

  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }

  public Signature getTypedSignature() {
    return this.typedSignature;
  }

  public RuleList getGeneratedRules() {
    return this.generatedRules;
  }

  /**
   * Transform each rewrite rule to a list of well-typed rules with the same behaviour.
   **/
  public void typeRules(RuleList untypedRules) {
    this.generatedRules = tom_empty_list_ConcRule();

    {{if (tom_is_sort_RuleList(untypedRules)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )untypedRules)))) { sa.rule.types.RuleList  tomMatch102_end_4=(( sa.rule.types.RuleList )untypedRules);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch102_end_4))) { sa.rule.types.Rule  tom_rule=tom_get_head_ConcRule_RuleList(tomMatch102_end_4);

        Map<String,GomType> env = new HashMap<String,GomType>();
        //System.out.println("RULE: " + Pretty.toString(`rule));
        {{if (tom_is_sort_Rule(tom_rule)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )tom_rule))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )tom_rule)))) { sa.rule.types.Term  tomMatch103_1=tom_get_slot_Rule_lhs((( sa.rule.types.Rule )tom_rule));if (tom_is_sort_Term(tomMatch103_1)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch103_1))) { sa.rule.types.TermList  tomMatch103_6=tom_get_slot_Appl_args(tomMatch103_1); String  tom_stratOp=tom_get_slot_Appl_symbol(tomMatch103_1);if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch103_6))) {if (!(tom_is_empty_TermList_TermList(tomMatch103_6))) { sa.rule.types.Term  tom_lhs=tomMatch103_1;

            // get the possible codomain(s) for the head operator of the LHS
            // - boolean operators: Bool
            // - operators of the form ALL-f... : codomain of f
            // - operators of the form CHOICE... (no ...-f) : codomain of its first argument
            Set<GomType> types = new HashSet<GomType>();
            if(getExtractedSignature().isBooleanOperator(tom_stratOp)) {
              // if head opearator of the rule is EQ or AND then the codomain should be BOOL
              types.add(Signature.TYPE_BOOLEAN);
            } else {
              if(Tools.getOperatorName(tom_stratOp) != null) {
                // if symbol of the form ALL-f... the codomain is given by the codomain of f
                types = this.getTypes(env,tom_lhs);
              } else {
                // otherwise the codomain is given by the codomain of its argument
                types = this.getTypes(env,tom_get_head_TermList_TermList(tomMatch103_6));
              }
            }
            // normally shouldn't happen
            if(types.size() == 0) {
              throw new UntypableTermException("RULE OMITTED for " + tom_stratOp+ "  because no possible codomain");
            }


            // for each potential codomain generate a new rule
            // (the potential codomain is unique for all operators but for BOTTOM
            // which can be of any type of the declared (ie extracted) signature
            for(GomType type: types) {
              try {
                env = new HashMap<String,GomType>(); // start with fresh env for each rule generation
                Term typedLhs = this.propagateType(env,tom_lhs,type); // type of all variables is inferred in env
                // head symbol of LHS is added to the typed signature
                Term typedRhs = this.propagateType(env,tom_get_slot_Rule_rhs((( sa.rule.types.Rule )tom_rule)),type);
                Rule newRule = tom_make_Rule(typedLhs,typedRhs);
                //                 this.generatedRules.add(newRule);
                generatedRules = tom_append_list_ConcRule(generatedRules,tom_cons_list_ConcRule(newRule,tom_empty_list_ConcRule())); // must preserve the order
              } catch(TypeMismatchException typeExc) {
                System.err.println("RULE OMITTED for " + tom_stratOp+ "  because of " + typeExc.getMessage());
              }
            }
          }}}}}}}}}

      }if (tom_is_empty_ConcRule_RuleList(tomMatch102_end_4)) {tomMatch102_end_4=(( sa.rule.types.RuleList )untypedRules);} else {tomMatch102_end_4=tom_get_tail_ConcRule_RuleList(tomMatch102_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch102_end_4, (( sa.rule.types.RuleList )untypedRules))));}}}}

  }

  /** Get the potential types of a term by looking at its head symbol
   *  The head symbol can be a symbol from the extracted signature or Bottom or a boolean operator
   *  (symbol can't be a generated)
   */
  private Set<GomType> getTypes(Map<String,GomType> env, Term term) {
    Set<GomType> types = new HashSet<GomType>();
    Signature eSig = this.getExtractedSignature();
    {{if (tom_is_sort_Term(term)) {if (tom_is_sort_Term((( sa.rule.types.Term )term))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )term)))) { String  tom_symbol=tom_get_slot_Appl_symbol((( sa.rule.types.Term )term));

          if(tom_symbol== Signature.BOTTOM) { // for BOTTOM add all possible types
            types.addAll(eSig.getCodomains());
          } else if(eSig.isBooleanOperator(tom_symbol)) {
            // for boolean ops (in fact only TRUE or FALSE can occur) add BOOLEAN
            types.add(Signature.TYPE_BOOLEAN);
          } else if(Tools.getOperatorName(tom_symbol) != null) {
            // for symbol of the form ALL-f add the type of f
            types.add(eSig.getCodomain(Tools.getOperatorName(tom_symbol)));
          } else if(eSig.getCodomain(tom_symbol) != null) {
            // for symbols of the original signature add their type
            types.add(eSig.getCodomain(tom_symbol));
          }
        }}}}{if (tom_is_sort_Term(term)) {if (tom_is_sort_Term((( sa.rule.types.Term )term))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )term)))) { String  tom_name=tom_get_slot_Var_name((( sa.rule.types.Term )term));

 // for VAR get the type from the environment
          //System.out.println("getTypes: " + `name + " --> " + env.get(`name));
          if(env.get(tom_name) != null) {
            types.add(env.get(tom_name));
          } else { // if we don't know it's type than it could be any type (eg for mainstrat)
            types.addAll(eSig.getCodomains());
          }
        }}}}}

    return types;
  }

  /**
   * Propagate the type information in the names of the symbols. Term t of the form:
   * - all_39(s1)
   * - all_39-f(s1,...,sn,s) with n=ar(f)
   * - one_42(s1)
   * - one_42-f(s1,...,sn) with n=ar(f)
   * - ...
   */
  private Term propagateType(Map<String,GomType> env, Term t, GomType type) {
    Term typedTerm = t;
    Signature eSig = getExtractedSignature();

    {{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tom_name=tom_get_slot_Appl_symbol((( sa.rule.types.Term )t)); sa.rule.types.TermList  tom_args=tom_get_slot_Appl_args((( sa.rule.types.Term )t));

        if(eSig.isBooleanOperatorExceptEQ(tom_name)) {
          // for AND, TRUE, FALSE we override the "type" imposed by propagateType: always BOOLEAN
          type = Signature.TYPE_BOOLEAN;
        } else if(eSig.isBooleanOperatorEQ(tom_name)) {
          // for EQ we override the "type" imposed by propagateType: the type of its first argument
          {{if (tom_is_sort_TermList(tom_args)) {if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )(( sa.rule.types.TermList )tom_args)))) {if (!(tom_is_empty_TermList_TermList((( sa.rule.types.TermList )tom_args)))) { sa.rule.types.Term  tom_arg=tom_get_head_TermList_TermList((( sa.rule.types.TermList )tom_args));

              if(this.getTypes(env,tom_arg).size() == 1) { // at least one type (ie well-typed) and only one (ie no Bottom)
                //                 type = this.getTypes(env,`arg).get(0);
                type = this.getTypes(env,tom_arg).iterator().next(); // get the "first" (and only) element
              } else {  // normally shouldn't happen
                throw new UntypableTermException("No possible type for "+tom_arg+" in term "+t);
              }
            }}}}}

        }

        GomTypeList domain = null;
        String fun = null;
        String typedName = null;

        // build the symbol name by adding the type information
        if(eSig.getCodomain(tom_name) != null) {
          // if term in the extracted signature then first check if it's well-typed
          fun = tom_name;
          if(eSig.getCodomain(tom_name) != type) {
            // if type mismatch than the rule should be eventually removed
            throw new TypeMismatchException("BAD ARG: " + tom_name+ " TRY TYPE " + type + " IN TERM "+t);
          }
          // don't change its name if symbol from original (extracted) signature
          typedName = tom_name;
        } else {
          // retrieve fun from name of the form symbolName-fun_typeName
          fun = Tools.getOperatorName(tom_name);
          typedName = Tools.addTypeName(tom_name,type.getName()); // add type information to symbol name
        }

        String nameMain = Tools.getSymbolNameMain(tom_name);
        // build the domain of the typed symbol
        if(fun != null) {
          // if a composite symbol (e.g. all-f_...)  or symbol from original signature (eg  f, g, ...)
          domain = eSig.getDomain(fun);
          if(domain == null) {
            // normally should'n happen (unless wrongly built symbol name)
            throw new UntypableTermException("Type of symbol "+ fun +"cannot be determined");
          }
          // for all_f add the type of f(...) at the end
          if(StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.ALL) {
            domain = tom_append_list_ConcGomType(domain,tom_cons_list_ConcGomType(type,tom_empty_list_ConcGomType()));
          }
        } else {
          // if Boolean or Generated symbol or Bottom
          domain = tom_empty_list_ConcGomType();
          if(!nameMain.equals(Signature.TRUE) && !nameMain.equals(Signature.FALSE)) { // if any arguments
            domain = tom_cons_list_ConcGomType(type,tom_empty_list_ConcGomType()); // first argument has always the same type as the term
            if(eSig.isBooleanOperator(tom_name) ||
               (Tools.isSymbolNameAux(tom_name) && StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.SEQ)) {
              // if boolean operator (EQ or AND) or if aux symbol for SEQ (ie seqAux_...) then there is a second parameter of the same type
              domain = tom_cons_list_ConcGomType(type,tom_cons_list_ConcGomType(type,tom_empty_list_ConcGomType()));
            } else if(Tools.isSymbolNameAux(tom_name) && StrategyOperator.getStrategyOperator(nameMain)==StrategyOperator.RULE) {
              // if aux symbol for RULE (originating from a non-linear rule compilation) then there is a second parameter of type Bool
              domain = tom_cons_list_ConcGomType(type,tom_cons_list_ConcGomType(Signature.TYPE_BOOLEAN,tom_empty_list_ConcGomType()));
            }
          }
        }

        // propagate the type to the subterms
        TermList args = tom_args;
        TermList newArgs = tom_empty_list_TermList();
        GomTypeList tl = domain;
        while(!args.isEmptyTermList()) {
          Term arg = args.getHeadTermList();
          GomType arg_type = tl.getHeadConcGomType();
          Term typedArg = propagateType(env, arg, arg_type);
          if(typedArg != null) {
            newArgs = tom_append_list_TermList(newArgs,tom_cons_list_TermList(typedArg,tom_empty_list_TermList()));
          } else {
            throw new UntypableTermException("Can't type argument " + arg +" in "+t);
          }
          args = args.getTailTermList();
          tl = tl.getTailConcGomType();
        }
        typedTerm = tom_make_Appl(typedName,newArgs);

        // add info into typed signature
        if(tom_name== Signature.EQ) {
          typedSignature.addFunctionSymbol(typedName,domain,Signature.TYPE_BOOLEAN);
        } else {
          if(typedSignature.getSymbols().contains(typedName)) {
            // do not add symbols which are already in the signature
            // at least: should not be added as a function
            //typedSignature.addSymbol(typedName,domain,type);
          } else {
            typedSignature.addFunctionSymbol(typedName,domain,type);
          }
        }
        // TODO: build domain exactly as it should be (problem for TRUE, ruleAUX, ...?)
      }}}}{if (tom_is_sort_Term(t)) {if (tom_is_sort_Term((( sa.rule.types.Term )t))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )t)))) { String  tom_name=tom_get_slot_Var_name((( sa.rule.types.Term )t));


        // set the type in the environment
        GomType varType = env.get(tom_name);
        if(varType != null) { // if type already set
          if(varType != type) { // if try to assign a different type
            throw new RuntimeException("Attemp to type variable with different type: " + tom_name);
          }
        } else { // set type if not already done
          env.put(tom_name,type);
        }
        // dont't change it
        typedTerm = (( sa.rule.types.Term )t);
      }}}}}

    return typedTerm;
  }

}


  /********************************************************************************
   *     END
   ********************************************************************************/
