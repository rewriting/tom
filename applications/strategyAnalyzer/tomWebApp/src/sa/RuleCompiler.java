package sa;

import sa.rule.types.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import tom.library.sl.*;
/*import aterm.*;
import aterm.pure.*;*/
import com.google.common.collect.HashMultiset;

import static sa.Tools._appl;
import static sa.Tools.Var;
import static sa.Tools.At;
import static sa.Tools.Bottom;
import static sa.Tools.Bottom2;


public class RuleCompiler {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static boolean tom_is_fun_sym_Appl( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Appl) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  String  tom_get_slot_Appl_symbol( sa.rule.types.Term  t) {return  t.getsymbol() ;}private static  sa.rule.types.TermList  tom_get_slot_Appl_args( sa.rule.types.Term  t) {return  t.getargs() ;}private static boolean tom_is_fun_sym_Var( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Var) ;}private static  String  tom_get_slot_Var_name( sa.rule.types.Term  t) {return  t.getname() ;}private static boolean tom_is_fun_sym_Anti( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.Anti) ;}private static  sa.rule.types.Term  tom_make_Anti( sa.rule.types.Term  t0) { return  sa.rule.types.term.Anti.make(t0) ;}private static  sa.rule.types.Term  tom_get_slot_Anti_term( sa.rule.types.Term  t) {return  t.getterm() ;}private static boolean tom_is_fun_sym_At( sa.rule.types.Term  t) {return  (t instanceof sa.rule.types.term.At) ;}private static  sa.rule.types.Term  tom_get_slot_At_term1( sa.rule.types.Term  t) {return  t.getterm1() ;}private static  sa.rule.types.Term  tom_get_slot_At_term2( sa.rule.types.Term  t) {return  t.getterm2() ;}private static boolean tom_is_fun_sym_Rule( sa.rule.types.Rule  t) {return  (t instanceof sa.rule.types.rule.Rule) ;}private static  sa.rule.types.Term  tom_get_slot_Rule_lhs( sa.rule.types.Rule  t) {return  t.getlhs() ;}private static  sa.rule.types.Term  tom_get_slot_Rule_rhs( sa.rule.types.Rule  t) {return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_BottomUp( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(v,tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}   private static boolean tom_equal_term_Map(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_Map(Object t) {return  t instanceof java.util.Map ;} private static boolean tom_equal_term_List(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_List(Object t) {return  t instanceof java.util.List ;} private static boolean tom_equal_term_ArrayList(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_ArrayList(Object t) {return  t instanceof java.util.ArrayList ;} 








  // The extracted (concrete) signature
  private Signature extractedSignature;
  // The generated (concrete) signature
  private Signature generatedSignature;
  // The generated (ordered) TRS
  private List<Rule> generatedRules;

  public RuleCompiler(Signature extractedSignature, Signature generatedSignature) {
    this.extractedSignature=extractedSignature;
    this.generatedSignature=generatedSignature;
  }

  /********************************************************************************
   *     Transform a list of LINEAR rules (with only one anti-pattern)
   *     in a list of rules with no anti-patterns. The order of rules
   *     is preserved.
   ********************************************************************************/
  /**
   * Expand an anti-pattern in each of the LINEAR rules (with only one anti-pattern) in the list
   * - the rules replacing the orginal are at the same position in the list
   * @param rules the rules to expand
   * @return the list of generatedRules (with no anti-paterns left)
   */
  public RuleList expandAntiPatterns(RuleList rules) {
    return expandAntiPatternsAux(rules, true);
  }

  /*
   * Remove nested anti-patterns
   * @param rules the list of rules to expand
   */
  public RuleList expandGeneralAntiPatterns(RuleList rules, String nextRuleSymbol) {
    rules = expandAntiPatternsAux(rules, false);
    if(nextRuleSymbol == null){ //if we don't know what to do with the Bottom2 (than change to Bottom)
      rules = eliminateBottom2(rules);
    }else{ // chain to the next call
      rules = changeBottom2(rules,nextRuleSymbol);
    }
    return rules;
  }

  /*
   * Remove nested anti-patterns
   * @param rules the list of rules to expand
   * @param postTreatment true to remove only top-level anti-patterns
   * @returns a (ordered) list of rules
   */
  public RuleList expandAntiPatternsAux(RuleList rules, boolean postTreatment) {
    RuleList newRules = tom_empty_list_ConcRule();
    for(Rule rule:rules.getCollectionConcRule()) {
      RuleList genRules = this.expandAntiPatternInRule(rule, postTreatment);
      // add the generated rules for rule to the result (list of rule)
      newRules = tom_append_list_ConcRule(newRules,tom_append_list_ConcRule(genRules,tom_empty_list_ConcRule()));
    }
    return newRules;
  }

  /**
   * Expand a rule (with anti-patterns) in list of LINEAR rules (with only one anti-pattern)
   * @param rule the rule to expand
   * @param postTreatment true to remove only top-level anti-pattern
   * @return the list of generated rules
   */
  private RuleList expandAntiPatternInRule(Rule rule, boolean postTreatment) {
    RuleList genRules = tom_empty_list_ConcRule();
    try {
      // collect the anti-patterns in a multiset
      HashMultiset<Term> antiBag = HashMultiset.create();
      Term lhs = rule.getlhs();
      tom_make_TopDown(tom_make_CollectAnti(antiBag)).visitLight(lhs);
      int nbOfAnti = antiBag.size();
      if(nbOfAnti == 0) {
        // add the rule since it contains no anti-pattern
        genRules = tom_append_list_ConcRule(genRules,tom_cons_list_ConcRule(rule,tom_empty_list_ConcRule()));
      } else {
        List<Rule> ruleList = new ArrayList<Rule>();
        if(postTreatment) {
          if(true || Tools.isLinear(lhs)) {
            /*
             * case: rule is left-linear and there is only one negation
             * should only be done in post-treatment, not during compilation of strategies
             */
            tom_make_OnceTopDown(tom_make_ExpandAntiPatternPostTreatment(ruleList,rule,this.extractedSignature,this.generatedSignature)).visit(rule);
          } else {
            System.out.println("NON LIN: " + Pretty.toString(rule) );
            throw new RuntimeException("Should not be there");
          }
        } else {
          /*
           * General case
           */
          tom_make_OnceTopDown(tom_make_ExpandGeneralAntiPattern(ruleList,rule)).visit(rule);
        }

        // for each generated rule re-start the expansion
        for(Rule expandr:ruleList) {
          // add the list of rules generated for the expandr rule to the final result
          RuleList expandedRules = this.expandAntiPatternInRule(expandr,postTreatment);
          genRules = tom_append_list_ConcRule(genRules,tom_append_list_ConcRule(expandedRules,tom_empty_list_ConcRule()));
        }

      }
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return genRules;
  }

  /**
   * Perform one-step expansion for a LINEAR Rule
   * @param ruleList the resulting list of rules (extended by side effect)
   * @param subject the rule to expand
   * @param extractedSignature the extracted signature
   * @param generatedSignature the generated signature
   */
  public static class ExpandAntiPatternPostTreatment extends tom.library.sl.AbstractStrategyBasic {private  java.util.List  ruleList;private  sa.rule.types.Rule  subject;private  Signature  extractedSignature;private  Signature  generatedSignature;public ExpandAntiPatternPostTreatment( java.util.List  ruleList,  sa.rule.types.Rule  subject,  Signature  extractedSignature,  Signature  generatedSignature) {super(tom_make_Fail());this.ruleList=ruleList;this.subject=subject;this.extractedSignature=extractedSignature;this.generatedSignature=generatedSignature;}public  java.util.List  getruleList() {return ruleList;}public  sa.rule.types.Rule  getsubject() {return subject;}public  Signature  getextractedSignature() {return extractedSignature;}public  Signature  getgeneratedSignature() {return generatedSignature;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch70_1=tom_get_slot_Anti_term((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch70_1)) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )tomMatch70_1))) { sa.rule.types.Term  tom_t=tom_get_slot_Anti_term(tomMatch70_1);


        Rule newr = (Rule) getEnvironment().getPosition().getReplace(tom_t).visit(subject);
        ruleList.add(newr);
        return tom_t;
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tom_t=tom_get_slot_Anti_term((( sa.rule.types.Term )tom__arg));


        Term antiterm = (Main.options.metalevel)?Tools.metaDecodeConsNil(tom_t):tom_t;
        {{if (tom_is_sort_Term(antiterm)) {if (tom_is_sort_Term((( sa.rule.types.Term )antiterm))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )antiterm)))) { String  tom_name=tom_get_slot_Appl_symbol((( sa.rule.types.Term )antiterm));

            // add g(Z1,...) ... h(Z1,...)
            for(String otherName: extractedSignature.getConstructors()) {
              if(!tom_name.equals(otherName)) {
                int arity = extractedSignature.getArity(otherName);
                Term newt = Tools.genAbstractTerm(otherName,arity,Tools.getName("Z"));
                if(Main.options.metalevel) {
                  newt = Tools.metaEncodeConsNil(newt,generatedSignature);
                }
                Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
                ruleList.add(newr);
              }
            }

            // add f(!a1,...) ... f(a1,...,!an)
            sa.rule.types.termlist.TermList tl = (sa.rule.types.termlist.TermList) tom_get_slot_Appl_args((( sa.rule.types.Term )antiterm));
            //int arity = extractedSignature.getArity(`name); // arity(Bottom)=-1 instead of 1 !!!
            int arity = tl.length();
            //System.out.println(`name + " -- " + arity);

            Term[] arrayOfVariable = new Term[arity];
            Term[] arrayOfTerm = new Term[arity];
            arrayOfTerm = tl.toArray(arrayOfTerm);
            String Z = Tools.getName("Z");
            for(int i=1 ; i<=arity ; i++) {
              arrayOfVariable[i-1] = Var(Z +"_"+ i);
            }
            for(int i=1 ; i<=arity ; i++) {
              Term variable = arrayOfVariable[i-1];
              Term ti = arrayOfTerm[i-1];
              arrayOfVariable[i-1] = tom_make_Anti(ti);
              Term newt = tom_make_Appl(tom_name,sa.rule.types.termlist.TermList.fromArray(arrayOfVariable));
              arrayOfVariable[i-1] = variable;
              if(Main.options.metalevel) {
                newt = Tools.metaEncodeConsNil(newt,generatedSignature);
              }
              Rule newr = (Rule) getEnvironment().getPosition().getReplace(newt).visit(subject);
              ruleList.add(newr);
            }
          }}}}}

        return tom_t;
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandAntiPatternPostTreatment( java.util.List  t0,  sa.rule.types.Rule  t1,  Signature  t2,  Signature  t3) { return new ExpandAntiPatternPostTreatment(t0,t1,t2,t3);}



  /*
   * Perform one-step expansion
   *
   * @param orderedTRS the resulting list of rules
   * @param subject the rule to expand (may contain nested anti-pattern and be non-linear)
   */
  public static class ExpandGeneralAntiPattern extends tom.library.sl.AbstractStrategyBasic {private  java.util.List  orderedTRS;private  sa.rule.types.Rule  subject;public ExpandGeneralAntiPattern( java.util.List  orderedTRS,  sa.rule.types.Rule  subject) {super(tom_make_Fail());this.orderedTRS=orderedTRS;this.subject=subject;}public  java.util.List  getorderedTRS() {return orderedTRS;}public  sa.rule.types.Rule  getsubject() {return subject;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch72_1=tom_get_slot_Anti_term((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch72_1)) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )tomMatch72_1))) {


        Rule newr = (Rule) getEnvironment().getPosition().getReplace(tom_get_slot_Anti_term(tomMatch72_1)).visit(subject);
        orderedTRS.add(newr);
        return (( sa.rule.types.Term )tom__arg);
      }}}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tom_t=tom_get_slot_Anti_term((( sa.rule.types.Term )tom__arg)); sa.rule.types.Term  tom_s=(( sa.rule.types.Term )tom__arg);


        /*
         * x@q[!q'] -> bot(x,r) becomes   q[q'] -> r
         *                              x@q[z]  -> bot(x,r)
         *
         *   q[!q'] -> r        becomes x@q[q'] -> bot(x,r)
         *                                q[z]  -> r
         */

        // here: t is q'
        System.out.println("EXPAND AP: " + Pretty.toString(subject));
        {{if (tom_is_sort_Rule(subject)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )subject))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )subject)))) { sa.rule.types.Term  tomMatch73_2=tom_get_slot_Rule_rhs((( sa.rule.types.Rule )subject));if (tom_is_sort_Term(tomMatch73_2)) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )tomMatch73_2))) { sa.rule.types.TermList  tomMatch73_6=tom_get_slot_Appl_args(tomMatch73_2);if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch73_6))) {if (!(tom_is_empty_TermList_TermList(tomMatch73_6))) { sa.rule.types.TermList  tomMatch73_10=tom_get_tail_TermList_TermList(tomMatch73_6);if (!(tom_is_empty_TermList_TermList(tomMatch73_10))) { sa.rule.types.Term  tom_r=tom_get_head_TermList_TermList(tomMatch73_10);if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch73_10))) {if (tom_equal_term_String(Signature.BOTTOM2, tom_get_slot_Appl_symbol(tomMatch73_2))) {

            // here we generate x@q[q'] but x will be eliminated later
            Rule r1 = (Rule) getEnvironment().getPosition().getReplace(tom_t).visit(subject);
            r1 = r1.setrhs(tom_r);

            Term Z = Var(Tools.getName("Z"));
            Rule r2 = (Rule) getEnvironment().getPosition().getReplace(Z).visit(subject);
            r2 = r2.setrhs(Bottom2(tom_get_head_TermList_TermList(tomMatch73_6),tom_r));

            orderedTRS.add(r1);
            orderedTRS.add(r2);
            System.out.println("  case 1 ==> " + Pretty.toString(r1));
            System.out.println("  case 1 ==> " + Pretty.toString(r2));
            return tom_s;
          }}}}}}}}}}}{if (tom_is_sort_Rule(subject)) {if (tom_is_sort_Rule((( sa.rule.types.Rule )subject))) {if (tom_is_fun_sym_Rule((( sa.rule.types.Rule )(( sa.rule.types.Rule )subject)))) {


            Term X = Var(Tools.getName("X"));
            Rule r1 = (Rule) getEnvironment().getPosition().getReplace(tom_t).visit(subject);
            r1 = r1.setlhs(At(X,r1.getlhs()));
            r1 = r1.setrhs(Bottom2(X,r1.getrhs()));

            // here we generate x@q[z] but x will be eliminated later
            Term Z = Var(Tools.getName("Z"));
            Rule r2 = (Rule) getEnvironment().getPosition().getReplace(Z).visit(subject);

            orderedTRS.add(r1);
            orderedTRS.add(r2);
            System.out.println("  case 2 ==> " + Pretty.toString(r1));
            System.out.println("  case 2 ==> " + Pretty.toString(r2));
            return tom_s;
          }}}}}


      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ExpandGeneralAntiPattern( java.util.List  t0,  sa.rule.types.Rule  t1) { return new ExpandGeneralAntiPattern(t0,t1);}



  /*
   * replace Bottom2 by Bottom
   */
  public RuleList eliminateBottom2(RuleList subject) {
    RuleList res = subject;
    try {
      res = tom_make_TopDown(tom_make_EliminateBottom2()).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return res;
  }

  public static class EliminateBottom2 extends tom.library.sl.AbstractStrategyBasic {public EliminateBottom2() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.TermList  tomMatch74_2=tom_get_slot_Appl_args((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch74_2))) {if (!(tom_is_empty_TermList_TermList(tomMatch74_2))) { sa.rule.types.TermList  tomMatch74_6=tom_get_tail_TermList_TermList(tomMatch74_2);if (!(tom_is_empty_TermList_TermList(tomMatch74_6))) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch74_6))) {if (tom_equal_term_String(Signature.BOTTOM2, tom_get_slot_Appl_symbol((( sa.rule.types.Term )tom__arg)))) {


        return Bottom(tom_get_head_TermList_TermList(tomMatch74_2));
      }}}}}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_EliminateBottom2() { return new EliminateBottom2();}



  /*
   * replace Bottom2(x,_) by nextRuleSymbol(x)
   */
  public RuleList changeBottom2(RuleList subject, String nextRuleSymbol) {
    RuleList res = subject;
    try {
      res = tom_make_TopDown(tom_make_ChangeBottom2(nextRuleSymbol)).visitLight(subject);
    } catch(VisitFailure e) {
      throw new RuntimeException("Should not be there");
    }
    return res;
  }

  public static class ChangeBottom2 extends tom.library.sl.AbstractStrategyBasic {private  String  nextRuleSymbol;public ChangeBottom2( String  nextRuleSymbol) {super(tom_make_Identity());this.nextRuleSymbol=nextRuleSymbol;}public  String  getnextRuleSymbol() {return nextRuleSymbol;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Appl((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.TermList  tomMatch75_2=tom_get_slot_Appl_args((( sa.rule.types.Term )tom__arg));if (tom_is_fun_sym_TermList((( sa.rule.types.TermList )tomMatch75_2))) {if (!(tom_is_empty_TermList_TermList(tomMatch75_2))) { sa.rule.types.TermList  tomMatch75_6=tom_get_tail_TermList_TermList(tomMatch75_2);if (!(tom_is_empty_TermList_TermList(tomMatch75_6))) {if (tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(tomMatch75_6))) {if (tom_equal_term_String(Signature.BOTTOM2, tom_get_slot_Appl_symbol((( sa.rule.types.Term )tom__arg)))) {


        return _appl(nextRuleSymbol,tom_get_head_TermList_TermList(tomMatch75_2));
      }}}}}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ChangeBottom2( String  t0) { return new ChangeBottom2(t0);}



  /********************************************************************************
   *     Transform a list of rules with @ in a list of rules with the
   *     @ expanded accordingly. The order of rules is preserved.
   ********************************************************************************/
  /**
    * Transforms a list of rules that contain x@t into a list of rules without @
    * @param ruleList the list of rules to expand
    * @return a new list that contains the expanded rules
    */
  public RuleList expandAt(RuleList ruleList) {
    RuleList res = tom_empty_list_ConcRule();
    ArrayList<Rule> list = new ArrayList<Rule>();
    for(Rule rule:ruleList.getCollectionConcRule()) {
      Map<String,Term> map = new HashMap<String,Term>();
      try {
        tom_make_TopDown(tom_make_CollectAt(map)).visitLight(rule); // add x->t into map for each x@t
      } catch(VisitFailure e) {
      }

      //System.out.println("AT MAP: " + map);
      //System.out.println("RULE: " + rule);

      if(map.keySet().isEmpty()) {
        // if no AT in the rule just add it to the result
        assert !Tools.containsAt(rule): rule;
        //res = `ConcRule(res*,rule);
        list.add(rule);
      } else {
        // if some AT in the rule then build a new one
        Rule newRule = rule;
        for(String name:map.keySet()) {
          Term t = map.get(name);
          try {
            // replace the ATs with the corresponding expressions
            if(name != "_") { // special treatment for _@t: they will be removed by EliminateAt
              newRule = tom_make_BottomUp(tom_make_ReplaceVariable(name,t)).visitLight(newRule);
            }
            // and remove the ATs
            newRule = tom_make_BottomUp(tom_make_EliminateAt()).visitLight(newRule);
          } catch(VisitFailure e) {
          }
        }
        assert !Tools.containsAt(newRule): newRule;
        //res = `ConcRule(res*,newRule);
        list.add(newRule);
      }
    }
    for(Rule r:list) {
      res = tom_cons_list_ConcRule(r,tom_append_list_ConcRule(res,tom_empty_list_ConcRule()));
    }
    res = res.reverse();
    return res;
  }

  // search all AT and store their values
  public static class CollectAt extends tom.library.sl.AbstractStrategyBasic {private  java.util.Map  map;public CollectAt( java.util.Map  map) {super(tom_make_Identity());this.map=map;}public  java.util.Map  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch76_1=tom_get_slot_At_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch76_1)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch76_1))) {


        // we remove the AT from the term we store in the map
        map.put(tom_get_slot_Var_name(tomMatch76_1),removeAt(tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg))));
      }}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectAt( java.util.Map  t0) { return new CollectAt(t0);}



  private static tom.library.sl.Visitable removeAt(tom.library.sl.Visitable t) {
    try {
      return tom_make_TopDown(tom_make_RemoveAt()).visitLight(t);
    } catch(VisitFailure e) {
      throw new RuntimeException("should not be there");
    }
  }

  public static class RemoveAt extends tom.library.sl.AbstractStrategyBasic {public RemoveAt() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        return tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_RemoveAt() { return new RemoveAt();}



  // search all Anti symbols
  public static class CollectAnti extends tom.library.sl.AbstractStrategyBasic {private  HashMultiset  bag;public CollectAnti( HashMultiset  bag) {super(tom_make_Identity());this.bag=bag;}public  HashMultiset  getbag() {return bag;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Anti((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        bag.add((( sa.rule.types.Term )tom__arg));
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectAnti( HashMultiset  t0) { return new CollectAnti(t0);}



  // replace x by t, and thus x@t by t@t
  public static class ReplaceVariable extends tom.library.sl.AbstractStrategyBasic {private  String  name;private  sa.rule.types.Term  term;public ReplaceVariable( String  name,  sa.rule.types.Term  term) {super(tom_make_Identity());this.name=name;this.term=term;}public  String  getname() {return name;}public  sa.rule.types.Term  getterm() {return term;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) {


        if(tom_get_slot_Var_name((( sa.rule.types.Term )tom__arg))== name) {
          return term;
        }
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_ReplaceVariable( String  t0,  sa.rule.types.Term  t1) { return new ReplaceVariable(t0,t1);}



  // replace t@t by t
  public static class EliminateAt extends tom.library.sl.AbstractStrategyBasic {public EliminateAt() {super(tom_make_Identity());}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (tom_is_sort_Term(v)) {return ((T)visit_Term((( sa.rule.types.Term )v),introspector));}if (!(( null  == environment))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  _visit_Term( sa.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(( null  == environment))) {return (( sa.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  sa.rule.types.Term  visit_Term( sa.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tom_t=tom_get_slot_At_term1((( sa.rule.types.Term )tom__arg));if (tom_equal_term_Term(tom_t, tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg)))) {


        return tom_t;
      }}}}}{if (tom_is_sort_Term(tom__arg)) {if (tom_is_sort_Term((( sa.rule.types.Term )tom__arg))) {if (tom_is_fun_sym_At((( sa.rule.types.Term )(( sa.rule.types.Term )tom__arg)))) { sa.rule.types.Term  tomMatch80_7=tom_get_slot_At_term1((( sa.rule.types.Term )tom__arg));if (tom_is_sort_Term(tomMatch80_7)) {if (tom_is_fun_sym_Var((( sa.rule.types.Term )tomMatch80_7))) { String  tomMatch80_11=tom_get_slot_Var_name(tomMatch80_7);if ( true ) {if (tom_equal_term_String("_", tomMatch80_11)) {

        return tom_get_slot_At_term2((( sa.rule.types.Term )tom__arg));
      }}}}}}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_EliminateAt() { return new EliminateAt();}


  /********************************************************************************
   *     END
   ********************************************************************************/


  public Signature getExtractedSignature() {
    return this.extractedSignature;
  }
  public Signature getGeneratedSignature() {
    return this.generatedSignature;
  }

}
