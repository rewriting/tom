package sa;

import sa.rule.types.*;
import tom.library.sl.*;
import java.util.HashSet;
import java.util.Set;

public class Pattern {
  private static boolean tom_equal_term_char(char t1, char t2) {return  t1==t2 ;}private static boolean tom_is_sort_char(char t) {return  true ;} private static boolean tom_equal_term_String(String t1, String t2) {return  t1.equals(t2) ;}private static boolean tom_is_sort_String(String t) {return  t instanceof String ;} private static boolean tom_equal_term_int(int t1, int t2) {return  t1==t2 ;}private static boolean tom_is_sort_int(int t) {return  true ;} private static boolean tom_equal_term_StratDecl(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDecl(Object t) {return  (t instanceof sa.rule.types.StratDecl) ;}private static boolean tom_equal_term_Field(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Field(Object t) {return  (t instanceof sa.rule.types.Field) ;}private static boolean tom_equal_term_ParamList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ParamList(Object t) {return  (t instanceof sa.rule.types.ParamList) ;}private static boolean tom_equal_term_GomType(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomType(Object t) {return  (t instanceof sa.rule.types.GomType) ;}private static boolean tom_equal_term_Strat(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Strat(Object t) {return  (t instanceof sa.rule.types.Strat) ;}private static boolean tom_equal_term_StratDeclList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratDeclList(Object t) {return  (t instanceof sa.rule.types.StratDeclList) ;}private static boolean tom_equal_term_TypeEnvironment(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TypeEnvironment(Object t) {return  (t instanceof sa.rule.types.TypeEnvironment) ;}private static boolean tom_equal_term_Param(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Param(Object t) {return  (t instanceof sa.rule.types.Param) ;}private static boolean tom_equal_term_AddList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AddList(Object t) {return  (t instanceof sa.rule.types.AddList) ;}private static boolean tom_equal_term_GomTypeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_GomTypeList(Object t) {return  (t instanceof sa.rule.types.GomTypeList) ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_RuleList(Object t) {return  (t instanceof sa.rule.types.RuleList) ;}private static boolean tom_equal_term_Term(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Term(Object t) {return  (t instanceof sa.rule.types.Term) ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Condition(Object t) {return  (t instanceof sa.rule.types.Condition) ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_TermList(Object t) {return  (t instanceof sa.rule.types.TermList) ;}private static boolean tom_equal_term_StratList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_StratList(Object t) {return  (t instanceof sa.rule.types.StratList) ;}private static boolean tom_equal_term_Trs(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Trs(Object t) {return  (t instanceof sa.rule.types.Trs) ;}private static boolean tom_equal_term_Rule(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Rule(Object t) {return  (t instanceof sa.rule.types.Rule) ;}private static boolean tom_equal_term_FieldList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_FieldList(Object t) {return  (t instanceof sa.rule.types.FieldList) ;}private static boolean tom_equal_term_AlternativeList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_AlternativeList(Object t) {return  (t instanceof sa.rule.types.AlternativeList) ;}private static boolean tom_equal_term_Symbol(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Symbol(Object t) {return  (t instanceof sa.rule.types.Symbol) ;}private static boolean tom_equal_term_Alternative(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Alternative(Object t) {return  (t instanceof sa.rule.types.Alternative) ;}private static boolean tom_equal_term_ProductionList(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_ProductionList(Object t) {return  (t instanceof sa.rule.types.ProductionList) ;}private static boolean tom_equal_term_Production(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Production(Object t) {return  (t instanceof sa.rule.types.Production) ;}private static boolean tom_equal_term_Program(Object t1, Object t2) {return  (t1==t2) ;}private static boolean tom_is_sort_Program(Object t) {return  (t instanceof sa.rule.types.Program) ;}private static  sa.rule.types.GomType  tom_make_GomType( String  t0) { return  sa.rule.types.gomtype.GomType.make(t0) ;}private static  sa.rule.types.Term  tom_make_Appl( String  t0,  sa.rule.types.TermList  t1) { return  sa.rule.types.term.Appl.make(t0, t1) ;}private static  sa.rule.types.Term  tom_make_Var( String  t0) { return  sa.rule.types.term.Var.make(t0) ;}private static  sa.rule.types.Term  tom_make_Add( sa.rule.types.AddList  t0) { return  sa.rule.types.term.Add.make(t0) ;}private static  sa.rule.types.Term  tom_make_Sub( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.term.Sub.make(t0, t1) ;}private static  sa.rule.types.Trs  tom_make_Otrs( sa.rule.types.RuleList  t0) { return  sa.rule.types.trs.Otrs.make(t0) ;}private static  sa.rule.types.Rule  tom_make_Rule( sa.rule.types.Term  t0,  sa.rule.types.Term  t1) { return  sa.rule.types.rule.Rule.make(t0, t1) ;}private static boolean tom_is_fun_sym_ConcAdd( sa.rule.types.AddList  t) {return  ((t instanceof sa.rule.types.addlist.ConsConcAdd) || (t instanceof sa.rule.types.addlist.EmptyConcAdd)) ;}private static  sa.rule.types.AddList  tom_empty_list_ConcAdd() { return  sa.rule.types.addlist.EmptyConcAdd.make() ;}private static  sa.rule.types.AddList  tom_cons_list_ConcAdd( sa.rule.types.Term  e,  sa.rule.types.AddList  l) { return  sa.rule.types.addlist.ConsConcAdd.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getHeadConcAdd() ;}private static  sa.rule.types.AddList  tom_get_tail_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.getTailConcAdd() ;}private static boolean tom_is_empty_ConcAdd_AddList( sa.rule.types.AddList  l) {return  l.isEmptyConcAdd() ;}   private static   sa.rule.types.AddList  tom_append_list_ConcAdd( sa.rule.types.AddList l1,  sa.rule.types.AddList  l2) {     if( l1.isEmptyConcAdd() ) {       return l2;     } else if( l2.isEmptyConcAdd() ) {       return l1;     } else if(  l1.getTailConcAdd() .isEmptyConcAdd() ) {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,l2) ;     } else {       return  sa.rule.types.addlist.ConsConcAdd.make( l1.getHeadConcAdd() ,tom_append_list_ConcAdd( l1.getTailConcAdd() ,l2)) ;     }   }   private static   sa.rule.types.AddList  tom_get_slice_ConcAdd( sa.rule.types.AddList  begin,  sa.rule.types.AddList  end, sa.rule.types.AddList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcAdd()  ||  (end==tom_empty_list_ConcAdd()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.addlist.ConsConcAdd.make( begin.getHeadConcAdd() ,( sa.rule.types.AddList )tom_get_slice_ConcAdd( begin.getTailConcAdd() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcGomType( sa.rule.types.GomTypeList  t) {return  ((t instanceof sa.rule.types.gomtypelist.ConsConcGomType) || (t instanceof sa.rule.types.gomtypelist.EmptyConcGomType)) ;}private static  sa.rule.types.GomTypeList  tom_empty_list_ConcGomType() { return  sa.rule.types.gomtypelist.EmptyConcGomType.make() ;}private static  sa.rule.types.GomTypeList  tom_cons_list_ConcGomType( sa.rule.types.GomType  e,  sa.rule.types.GomTypeList  l) { return  sa.rule.types.gomtypelist.ConsConcGomType.make(e,l) ;}private static  sa.rule.types.GomType  tom_get_head_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getHeadConcGomType() ;}private static  sa.rule.types.GomTypeList  tom_get_tail_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.getTailConcGomType() ;}private static boolean tom_is_empty_ConcGomType_GomTypeList( sa.rule.types.GomTypeList  l) {return  l.isEmptyConcGomType() ;}   private static   sa.rule.types.GomTypeList  tom_append_list_ConcGomType( sa.rule.types.GomTypeList l1,  sa.rule.types.GomTypeList  l2) {     if( l1.isEmptyConcGomType() ) {       return l2;     } else if( l2.isEmptyConcGomType() ) {       return l1;     } else if(  l1.getTailConcGomType() .isEmptyConcGomType() ) {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,l2) ;     } else {       return  sa.rule.types.gomtypelist.ConsConcGomType.make( l1.getHeadConcGomType() ,tom_append_list_ConcGomType( l1.getTailConcGomType() ,l2)) ;     }   }   private static   sa.rule.types.GomTypeList  tom_get_slice_ConcGomType( sa.rule.types.GomTypeList  begin,  sa.rule.types.GomTypeList  end, sa.rule.types.GomTypeList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcGomType()  ||  (end==tom_empty_list_ConcGomType()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.gomtypelist.ConsConcGomType.make( begin.getHeadConcGomType() ,( sa.rule.types.GomTypeList )tom_get_slice_ConcGomType( begin.getTailConcGomType() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_ConcRule( sa.rule.types.RuleList  t) {return  ((t instanceof sa.rule.types.rulelist.ConsConcRule) || (t instanceof sa.rule.types.rulelist.EmptyConcRule)) ;}private static  sa.rule.types.RuleList  tom_empty_list_ConcRule() { return  sa.rule.types.rulelist.EmptyConcRule.make() ;}private static  sa.rule.types.RuleList  tom_cons_list_ConcRule( sa.rule.types.Rule  e,  sa.rule.types.RuleList  l) { return  sa.rule.types.rulelist.ConsConcRule.make(e,l) ;}private static  sa.rule.types.Rule  tom_get_head_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getHeadConcRule() ;}private static  sa.rule.types.RuleList  tom_get_tail_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.getTailConcRule() ;}private static boolean tom_is_empty_ConcRule_RuleList( sa.rule.types.RuleList  l) {return  l.isEmptyConcRule() ;}   private static   sa.rule.types.RuleList  tom_append_list_ConcRule( sa.rule.types.RuleList l1,  sa.rule.types.RuleList  l2) {     if( l1.isEmptyConcRule() ) {       return l2;     } else if( l2.isEmptyConcRule() ) {       return l1;     } else if(  l1.getTailConcRule() .isEmptyConcRule() ) {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,l2) ;     } else {       return  sa.rule.types.rulelist.ConsConcRule.make( l1.getHeadConcRule() ,tom_append_list_ConcRule( l1.getTailConcRule() ,l2)) ;     }   }   private static   sa.rule.types.RuleList  tom_get_slice_ConcRule( sa.rule.types.RuleList  begin,  sa.rule.types.RuleList  end, sa.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcRule()  ||  (end==tom_empty_list_ConcRule()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.rulelist.ConsConcRule.make( begin.getHeadConcRule() ,( sa.rule.types.RuleList )tom_get_slice_ConcRule( begin.getTailConcRule() ,end,tail)) ;   }   private static boolean tom_is_fun_sym_TermList( sa.rule.types.TermList  t) {return  ((t instanceof sa.rule.types.termlist.ConsTermList) || (t instanceof sa.rule.types.termlist.EmptyTermList)) ;}private static  sa.rule.types.TermList  tom_empty_list_TermList() { return  sa.rule.types.termlist.EmptyTermList.make() ;}private static  sa.rule.types.TermList  tom_cons_list_TermList( sa.rule.types.Term  e,  sa.rule.types.TermList  l) { return  sa.rule.types.termlist.ConsTermList.make(e,l) ;}private static  sa.rule.types.Term  tom_get_head_TermList_TermList( sa.rule.types.TermList  l) {return  l.getHeadTermList() ;}private static  sa.rule.types.TermList  tom_get_tail_TermList_TermList( sa.rule.types.TermList  l) {return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( sa.rule.types.TermList  l) {return  l.isEmptyTermList() ;}   private static   sa.rule.types.TermList  tom_append_list_TermList( sa.rule.types.TermList l1,  sa.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  sa.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   sa.rule.types.TermList  tom_get_slice_TermList( sa.rule.types.TermList  begin,  sa.rule.types.TermList  end, sa.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end==tom_empty_list_TermList()) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  sa.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( sa.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }    private static boolean tom_equal_term_Strategy(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Strategy(Object t) {return  (t instanceof tom.library.sl.Strategy) ;} private static boolean tom_equal_term_Position(Object t1, Object t2) {return  (t1.equals(t2)) ;}private static boolean tom_is_sort_Position(Object t) {return  (t instanceof tom.library.sl.Position) ;} private static  tom.library.sl.Strategy  tom_make_mu( tom.library.sl.Strategy  var,  tom.library.sl.Strategy  v) { return ( new tom.library.sl.Mu(var,v) );}private static  tom.library.sl.Strategy  tom_make_MuVar( String  name) { return ( new tom.library.sl.MuVar(name) );}private static  tom.library.sl.Strategy  tom_make_Identity() { return ( new tom.library.sl.Identity() );}private static  tom.library.sl.Strategy  tom_make_One( tom.library.sl.Strategy  v) { return ( new tom.library.sl.One(v) );}private static  tom.library.sl.Strategy  tom_make_All( tom.library.sl.Strategy  v) { return ( new tom.library.sl.All(v) );}private static  tom.library.sl.Strategy  tom_make_Fail() { return ( new tom.library.sl.Fail() );}private static boolean tom_is_fun_sym_Sequence( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Sequence );}private static  tom.library.sl.Strategy  tom_empty_list_Sequence() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Sequence( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Sequence.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Sequence.THEN) );}private static boolean tom_is_empty_Sequence_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_Sequence())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):tom_empty_list_Sequence()),end,tail)) ;   }   private static boolean tom_is_fun_sym_Choice( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.Choice );}private static  tom.library.sl.Strategy  tom_empty_list_Choice() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_Choice( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.Choice.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_Choice_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.Choice.THEN) );}private static boolean tom_is_empty_Choice_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_Choice())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):tom_empty_list_Choice()),end,tail)) ;   }   private static boolean tom_is_fun_sym_SequenceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.SequenceId );}private static  tom.library.sl.Strategy  tom_empty_list_SequenceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_SequenceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.SequenceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.SequenceId.THEN) );}private static boolean tom_is_empty_SequenceId_Strategy( tom.library.sl.Strategy  t) {return ( t == null );}   private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(tom_empty_list_SequenceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):tom_empty_list_SequenceId()),end,tail)) ;   }   private static boolean tom_is_fun_sym_ChoiceId( tom.library.sl.Strategy  t) {return ( t instanceof tom.library.sl.ChoiceId );}private static  tom.library.sl.Strategy  tom_empty_list_ChoiceId() { return  null ;}private static  tom.library.sl.Strategy  tom_cons_list_ChoiceId( tom.library.sl.Strategy  head,  tom.library.sl.Strategy  tail) { return  tom.library.sl.ChoiceId.make(head,tail) ;}private static  tom.library.sl.Strategy  tom_get_head_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.FIRST) );}private static  tom.library.sl.Strategy  tom_get_tail_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( (tom.library.sl.Strategy)t.getChildAt(tom.library.sl.ChoiceId.THEN) );}private static boolean tom_is_empty_ChoiceId_Strategy( tom.library.sl.Strategy  t) {return ( t ==null );}   private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(tom_empty_list_ChoiceId())) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):tom_empty_list_ChoiceId()),end,tail)) ;   }   private static  tom.library.sl.Strategy  tom_make_OneId( tom.library.sl.Strategy  v) { return ( new tom.library.sl.OneId(v) );}   private static  tom.library.sl.Strategy  tom_make_AllSeq( tom.library.sl.Strategy  s) { return ( new tom.library.sl.AllSeq(s) );}private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_cons_list_Sequence(tom_make_One(tom_make_Identity()),tom_empty_list_Sequence())),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( tom_make_mu(tom_make_MuVar("x"),tom_cons_list_Choice(s2,tom_cons_list_Choice(tom_cons_list_Sequence(s1,tom_cons_list_Sequence(tom_make_One(tom_make_MuVar("x")),tom_empty_list_Sequence())),tom_empty_list_Choice()))));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return ( tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())) );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice()))) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId()))) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId()))) );}   private static boolean tom_equal_term_HashSet(Object l1, Object l2) {return  l1.equals(l2) ;}private static boolean tom_is_sort_HashSet(Object t) {return  t instanceof java.util.HashSet ;} 




  public static void main(String args[]) {
//     example1();
    // example2();
//     example3(); // numadd
//       example4(); // interp
//     example5(); // balance
//     example6(); // and-or
//    example7(); // simplest reduce
//     example7bis(); // simplest reduce with one type
//    example8(); // reduce deeper
//   example9(); // nested anti-pattern
//   example10(); // nested anti-pattern
   example11(); // exact cover
  }

  /*
   * examples
   */
  private static void example1() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    Term X = tom_make_Var("x");
    Term Y = tom_make_Var("y");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("g", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType()), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType()), tom_make_GomType("T") );
    eSig.addSymbol("h", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("T") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());
    Term ga =tom_make_Appl("g",tom_cons_list_TermList(a,tom_empty_list_TermList()));
    Term gv =tom_make_Appl("g",tom_cons_list_TermList(V,tom_empty_list_TermList()));

    Term fa =tom_make_Appl("f",tom_cons_list_TermList(a,tom_empty_list_TermList()));
    Term fga =tom_make_Appl("f",tom_cons_list_TermList(ga,tom_empty_list_TermList()));
    Term fgv =tom_make_Appl("f",tom_cons_list_TermList(gv,tom_empty_list_TermList()));
    Term fv =tom_make_Appl("f",tom_cons_list_TermList(V,tom_empty_list_TermList()));

    Term hxy =tom_make_Appl("h",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term hab =tom_make_Appl("h",tom_cons_list_TermList(a,tom_cons_list_TermList(b,tom_empty_list_TermList())));
    Term hba =tom_make_Appl("h",tom_cons_list_TermList(b,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fhxy =tom_make_Appl("f",tom_cons_list_TermList(hxy,tom_empty_list_TermList()));
    Term fhab =tom_make_Appl("f",tom_cons_list_TermList(hab,tom_empty_list_TermList()));
    Term fhba =tom_make_Appl("f",tom_cons_list_TermList(hba,tom_empty_list_TermList()));

    // f(g(x)) \ ( f(a) + f(g(a)) )
    //t = `Sub(fgv, Add(ConcAdd(fa,fga)));
    //t = `Sub(fv, Add(ConcAdd(fa,fga,fgv)));

    // X \ f(a + b)
    //     t = `Sub(V, Appl("f",TermList(Add(ConcAdd(a,b)))));
    //t = `Sub(V, Add(ConcAdd(fhba,fhab)));

//  t = (_ \ (f(h(b(),a())) + f(h(a(),b()))))
// t1 = (_ \ f((h(b(),a()) + h(a(),b()))))

    //type = `GomType("T");
    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());
    Term r1 = tom_make_Appl("rhr1",tom_empty_list_TermList());
    Term r2 = tom_make_Appl("rhs2",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(fhba,r0),tom_cons_list_ConcRule(tom_make_Rule(fhab,r1),tom_cons_list_ConcRule(tom_make_Rule(V,r2),tom_empty_list_ConcRule())))), eSig);
  }

  private static void example2() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("Nil", tom_empty_list_ConcGomType(), tom_make_GomType("List") );
    eSig.addSymbol("Cons", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("List"),tom_empty_list_ConcGomType())), tom_make_GomType("List") );
    eSig.addSymbol("sep", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("List"),tom_empty_list_ConcGomType())), tom_make_GomType("List") );
    eSig.setFunction("sep");

    Term y_ys = tom_make_Appl("Cons",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term x_y_ys = tom_make_Appl("Cons",tom_cons_list_TermList(V,tom_cons_list_TermList(y_ys,tom_empty_list_TermList())));
    Term p0 = tom_make_Appl("sep",tom_cons_list_TermList(V,tom_cons_list_TermList(x_y_ys,tom_empty_list_TermList())));
    Term p1 = tom_make_Appl("sep",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());
    Term r1 = tom_make_Appl("rhs1",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(p0,r0),tom_cons_list_ConcRule(tom_make_Rule(p1,r1),tom_empty_list_ConcRule()))), eSig);

  }

  private static void example3() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    eSig.addSymbol("Z", tom_empty_list_ConcGomType(), tom_make_GomType("Nat") );
    eSig.addSymbol("S", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("Nat") );
    eSig.addSymbol("C", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("TT") );
    eSig.addSymbol("Bound", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("TT") );
    eSig.addSymbol("Neg", tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_empty_list_ConcGomType()), tom_make_GomType("TT") );
    eSig.addSymbol("Add", tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_empty_list_ConcGomType())), tom_make_GomType("TT") );
    eSig.addSymbol("Sub", tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_empty_list_ConcGomType())), tom_make_GomType("TT") );
    eSig.addSymbol("Mul", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_empty_list_ConcGomType())), tom_make_GomType("TT") );
    eSig.addSymbol("numadd", tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_cons_list_ConcGomType(tom_make_GomType("TT"),tom_empty_list_ConcGomType())), tom_make_GomType("TT") );
    eSig.setFunction("numadd");

    Term pat = tom_make_Appl("Add",tom_cons_list_TermList(tom_make_Appl("Mul",tom_cons_list_TermList(V,tom_cons_list_TermList(tom_make_Appl("Bound",tom_cons_list_TermList(V,tom_empty_list_TermList())),tom_empty_list_TermList()))),tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term p1 = tom_make_Appl("numadd",tom_cons_list_TermList(pat,tom_cons_list_TermList(pat,tom_empty_list_TermList())));
    Term p2 = tom_make_Appl("numadd",tom_cons_list_TermList(pat,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term p3 = tom_make_Appl("numadd",tom_cons_list_TermList(V,tom_cons_list_TermList(pat,tom_empty_list_TermList())));
    Term p4 = tom_make_Appl("numadd",tom_cons_list_TermList(tom_make_Appl("C",tom_cons_list_TermList(V,tom_empty_list_TermList())),tom_cons_list_TermList(tom_make_Appl("C",tom_cons_list_TermList(V,tom_empty_list_TermList())),tom_empty_list_TermList())));
    Term p5 = tom_make_Appl("numadd",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));

    Term r1 = tom_make_Appl("rhs1",tom_empty_list_TermList());
    Term r2 = tom_make_Appl("rhs2",tom_empty_list_TermList());
    Term r3 = tom_make_Appl("rhs3",tom_empty_list_TermList());
    Term r4 = tom_make_Appl("rhs4",tom_empty_list_TermList());
    Term r5 = tom_make_Appl("rhs5",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(p1,r1),tom_cons_list_ConcRule(tom_make_Rule(p2,r2),tom_cons_list_ConcRule(tom_make_Rule(p3,r3),tom_cons_list_ConcRule(tom_make_Rule(p4,r4),tom_cons_list_ConcRule(tom_make_Rule(p5,r5),tom_empty_list_ConcRule())))))), eSig);

  }

  private static void example4() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    eSig.addSymbol("True", tom_empty_list_ConcGomType(), tom_make_GomType("Bool") );
    eSig.addSymbol("False", tom_empty_list_ConcGomType(), tom_make_GomType("Bool") );
    eSig.addSymbol("Z", tom_empty_list_ConcGomType(), tom_make_GomType("Nat") );
    eSig.addSymbol("S", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("Nat") );
    eSig.addSymbol("Nil", tom_empty_list_ConcGomType(), tom_make_GomType("List") );
    eSig.addSymbol("Cons", tom_cons_list_ConcGomType(tom_make_GomType("Val"),tom_cons_list_ConcGomType(tom_make_GomType("List"),tom_empty_list_ConcGomType())), tom_make_GomType("List") );
    eSig.addSymbol("Nv", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("Val") );
    eSig.addSymbol("Bv", tom_cons_list_ConcGomType(tom_make_GomType("Bool"),tom_empty_list_ConcGomType()), tom_make_GomType("Val") );
    eSig.addSymbol("Undef", tom_empty_list_ConcGomType(), tom_make_GomType("Val") );

    eSig.addSymbol("interp", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_cons_list_ConcGomType(tom_make_GomType("List"),tom_empty_list_ConcGomType())), tom_make_GomType("Val") );
    eSig.setFunction("interp");

    Term nat0 = tom_make_Appl("Z",tom_empty_list_TermList());
    Term nat1 = tom_make_Appl("S",tom_cons_list_TermList(nat0,tom_empty_list_TermList()));
    Term nat2 = tom_make_Appl("S",tom_cons_list_TermList(nat1,tom_empty_list_TermList()));
    Term nat3 = tom_make_Appl("S",tom_cons_list_TermList(nat2,tom_empty_list_TermList()));
    Term nat4 = tom_make_Appl("S",tom_cons_list_TermList(nat3,tom_empty_list_TermList()));
    Term nat5 = tom_make_Appl("S",tom_cons_list_TermList(nat4,tom_empty_list_TermList()));
    Term nat6 = tom_make_Appl("S",tom_cons_list_TermList(nat5,tom_empty_list_TermList()));
    Term nv = tom_make_Appl("Nv",tom_cons_list_TermList(V,tom_empty_list_TermList()));
    Term bv = tom_make_Appl("Bv",tom_cons_list_TermList(V,tom_empty_list_TermList()));
    Term nil = tom_make_Appl("Nil",tom_empty_list_TermList());

    Term p0 = tom_make_Appl("interp",tom_cons_list_TermList(nat0,tom_cons_list_TermList(nil,tom_empty_list_TermList())));
    Term p1 = tom_make_Appl("interp",tom_cons_list_TermList(nat1,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(nv,tom_cons_list_TermList(nil,tom_empty_list_TermList()))),tom_empty_list_TermList())));
    Term p2 = tom_make_Appl("interp",tom_cons_list_TermList(nat2,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(nv,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(nv,tom_cons_list_TermList(nil,tom_empty_list_TermList()))),tom_empty_list_TermList()))),tom_empty_list_TermList())));
    Term p3 = tom_make_Appl("interp",tom_cons_list_TermList(nat3,tom_cons_list_TermList(nil,tom_empty_list_TermList())));
    Term p4 = tom_make_Appl("interp",tom_cons_list_TermList(nat4,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(bv,tom_cons_list_TermList(nil,tom_empty_list_TermList()))),tom_empty_list_TermList())));
    Term p5 = tom_make_Appl("interp",tom_cons_list_TermList(nat5,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(bv,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(bv,tom_cons_list_TermList(nil,tom_empty_list_TermList()))),tom_empty_list_TermList()))),tom_empty_list_TermList())));
    Term p6 = tom_make_Appl("interp",tom_cons_list_TermList(nat6,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(nv,tom_cons_list_TermList(tom_make_Appl("Cons",tom_cons_list_TermList(nv,tom_cons_list_TermList(nil,tom_empty_list_TermList()))),tom_empty_list_TermList()))),tom_empty_list_TermList())));
    Term p7 = tom_make_Appl("interp",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());
    Term r1 = tom_make_Appl("rhs1",tom_empty_list_TermList());
    Term r2 = tom_make_Appl("rhs2",tom_empty_list_TermList());
    Term r3 = tom_make_Appl("rhs3",tom_empty_list_TermList());
    Term r4 = tom_make_Appl("rhs4",tom_empty_list_TermList());
    Term r5 = tom_make_Appl("rhs5",tom_empty_list_TermList());
    Term r6 = tom_make_Appl("rhs6",tom_empty_list_TermList());
    Term r7 = tom_make_Appl("rhs7",tom_empty_list_TermList());
    Trs res4 = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(p0,r0),tom_cons_list_ConcRule(tom_make_Rule(p1,r1),tom_cons_list_ConcRule(tom_make_Rule(p2,r2),tom_cons_list_ConcRule(tom_make_Rule(p3,r3),tom_cons_list_ConcRule(tom_make_Rule(p4,r4),tom_cons_list_ConcRule(tom_make_Rule(p5,r5),tom_cons_list_ConcRule(tom_make_Rule(p6,r6),tom_cons_list_ConcRule(tom_make_Rule(p7,r7),tom_empty_list_ConcRule())))))))))

,eSig);
    //Trs res4 = `trsRule(ConcRule(Rule(p0,r0),Rule(p1,r1), Rule(p7,r7)),eSig);

//     //interp(S(Z()),Cons(Undef(),Cons(_,_)))
//     //interp(S(Z()),Cons(Undef(),Nil()))
//     //interp(S(Z()),Cons(Undef(),_))
//     Term t1 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Cons",TermList(Var("_"),Var("_")))))));
//       Term t2 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Appl("Nil",TermList())))));
//       Term t3 = `Appl("interp",TermList(Appl("S",TermList(Appl("Z",TermList()))),Appl("Cons",TermList(Appl("Undef",TermList()),Var("_")))));

//     //`reduce(Add(ConcAdd(t1,t2,t3)),eSig);

  }

  private static Term T(Term t1, Term t2, Term t3, Term t4) {
    return tom_make_Appl("T",tom_cons_list_TermList(t1,tom_cons_list_TermList(t2,tom_cons_list_TermList(t3,tom_cons_list_TermList(t4,tom_empty_list_TermList())))));
  }

  private static void example5() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    eSig.addSymbol("Z", tom_empty_list_ConcGomType(), tom_make_GomType("Nat") );
    eSig.addSymbol("S", tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_empty_list_ConcGomType()), tom_make_GomType("Nat") );
    eSig.addSymbol("R", tom_empty_list_ConcGomType(), tom_make_GomType("Color") );
    eSig.addSymbol("B", tom_empty_list_ConcGomType(), tom_make_GomType("Color") );
    eSig.addSymbol("E", tom_empty_list_ConcGomType(), tom_make_GomType("Tree") );
    eSig.addSymbol("T", tom_cons_list_ConcGomType(tom_make_GomType("Color"),tom_cons_list_ConcGomType(tom_make_GomType("Tree"),tom_cons_list_ConcGomType(tom_make_GomType("Nat"),tom_cons_list_ConcGomType(tom_make_GomType("Tree"),tom_empty_list_ConcGomType())))), tom_make_GomType("Tree"));
    eSig.addSymbol("balance", tom_cons_list_ConcGomType(tom_make_GomType("Tree"),tom_empty_list_ConcGomType()), tom_make_GomType("Tree") );
    eSig.setFunction("balance");

    Term B = tom_make_Appl("B",tom_empty_list_TermList());
    Term R = tom_make_Appl("R",tom_empty_list_TermList());

    Term p0 = tom_make_Appl("balance",tom_cons_list_TermList(T(B,T(R,T(R,V,V,V),V,V),V,V),tom_empty_list_TermList()));
    Term p1 = tom_make_Appl("balance",tom_cons_list_TermList(T(B,T(R,V,V,T(R,V,V,V)),V,V),tom_empty_list_TermList()));
    Term p2 = tom_make_Appl("balance",tom_cons_list_TermList(T(B,V,V,T(R,T(R,V,V,V),V,V)),tom_empty_list_TermList()));
    Term p3 = tom_make_Appl("balance",tom_cons_list_TermList(T(B,V,V,T(R,V,V,T(R,V,V,V))),tom_empty_list_TermList()));
    Term p4 = tom_make_Appl("balance",tom_cons_list_TermList(V,tom_empty_list_TermList()));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());
    Term r1 = tom_make_Appl("rhs1",tom_empty_list_TermList());
    Term r2 = tom_make_Appl("rhs2",tom_empty_list_TermList());
    Term r3 = tom_make_Appl("rhs3",tom_empty_list_TermList());
    Term r4 = tom_make_Appl("rhs4",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(p0,r0),tom_cons_list_ConcRule(tom_make_Rule(p1,r1),tom_cons_list_ConcRule(tom_make_Rule(p2,r2),tom_cons_list_ConcRule(tom_make_Rule(p3,r3),tom_cons_list_ConcRule(tom_make_Rule(p4,r4),tom_empty_list_ConcRule()))))))
, eSig);
  }

  private static void example6() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");
    eSig.addSymbol("True", tom_empty_list_ConcGomType(), tom_make_GomType("Bool") );
    eSig.addSymbol("False", tom_empty_list_ConcGomType(), tom_make_GomType("Bool") );
    eSig.addSymbol("and", tom_cons_list_ConcGomType(tom_make_GomType("Bool"),tom_cons_list_ConcGomType(tom_make_GomType("Bool"),tom_empty_list_ConcGomType())), tom_make_GomType("Bool") );
    eSig.addSymbol("or",  tom_cons_list_ConcGomType(tom_make_GomType("Bool"),tom_cons_list_ConcGomType(tom_make_GomType("Bool"),tom_empty_list_ConcGomType())), tom_make_GomType("Bool") );
    eSig.setFunction("and");
    eSig.setFunction("or");

    Term True = tom_make_Appl("True",tom_empty_list_TermList());
    Term False = tom_make_Appl("False",tom_empty_list_TermList());

    Rule r0 = tom_make_Rule(tom_make_Appl("and",tom_cons_list_TermList(True,tom_cons_list_TermList(True,tom_empty_list_TermList()))),True);
    Rule r1 = tom_make_Rule(tom_make_Appl("and",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList()))),False);

    Rule r2 = tom_make_Rule(tom_make_Appl("or",tom_cons_list_TermList(False,tom_cons_list_TermList(False,tom_empty_list_TermList()))),False);
    //Rule r3 = `Rule(Appl("or", TermList(V,V)), True);
    Rule r3 = tom_make_Rule(tom_make_Add(tom_cons_list_ConcAdd(tom_make_Appl("or",tom_cons_list_TermList(False,tom_cons_list_TermList(True,tom_empty_list_TermList()))),tom_cons_list_ConcAdd(tom_make_Appl("or",tom_cons_list_TermList(True,tom_cons_list_TermList(False,tom_empty_list_TermList()))),tom_cons_list_ConcAdd(tom_make_Appl("or",tom_cons_list_TermList(True,tom_cons_list_TermList(True,tom_empty_list_TermList()))),tom_empty_list_ConcAdd())))),True)


;

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(r0,tom_cons_list_ConcRule(r1,tom_cons_list_ConcRule(r2,tom_cons_list_ConcRule(r3,tom_empty_list_ConcRule()))))), eSig);
  }

  private static void example7() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("U") );
    eSig.setFunction("f");

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());

    Term fav =tom_make_Appl("f",tom_cons_list_TermList(a,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fbv =tom_make_Appl("f",tom_cons_list_TermList(b,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fva =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(a,tom_empty_list_TermList())));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(fav,r0),tom_cons_list_ConcRule(tom_make_Rule(fbv,r0),tom_cons_list_ConcRule(tom_make_Rule(fva,r0),tom_empty_list_ConcRule())))), eSig);
  }

  private static void example7bis() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("T") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());

    Term fvv =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fav =tom_make_Appl("f",tom_cons_list_TermList(a,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fbv =tom_make_Appl("f",tom_cons_list_TermList(b,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term ffv =tom_make_Appl("f",tom_cons_list_TermList(fvv,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fva =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(a,tom_empty_list_TermList())));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(fav,r0),tom_cons_list_ConcRule(tom_make_Rule(ffv,r0),tom_cons_list_ConcRule(tom_make_Rule(fbv,r0),tom_cons_list_ConcRule(tom_make_Rule(fva,r0),tom_empty_list_ConcRule()))))), eSig);
  }


  private static void example8() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("g", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType()), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("U") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());

    Term gv =tom_make_Appl("g",tom_cons_list_TermList(V,tom_empty_list_TermList()));
    Term ga =tom_make_Appl("g",tom_cons_list_TermList(a,tom_empty_list_TermList()));
    Term gb =tom_make_Appl("g",tom_cons_list_TermList(b,tom_empty_list_TermList()));
    Term gg =tom_make_Appl("g",tom_cons_list_TermList(gv,tom_empty_list_TermList()));

    Term fgaa =tom_make_Appl("f",tom_cons_list_TermList(ga,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fgba =tom_make_Appl("f",tom_cons_list_TermList(gb,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fgga =tom_make_Appl("f",tom_cons_list_TermList(gg,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term faa =tom_make_Appl("f",tom_cons_list_TermList(a,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fba =tom_make_Appl("f",tom_cons_list_TermList(b,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fva =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(a,tom_empty_list_TermList())));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());

    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(fgba,r0),tom_cons_list_ConcRule(tom_make_Rule(fgaa,r0),tom_cons_list_ConcRule(tom_make_Rule(fgga,r0),tom_cons_list_ConcRule(tom_make_Rule(faa,r0),tom_cons_list_ConcRule(tom_make_Rule(fba,r0),tom_cons_list_ConcRule(tom_make_Rule(fva,r0),tom_empty_list_ConcRule()))))))), eSig);

  }

  private static void example9() {
    Signature eSig = new Signature();
    // !f(a,!g(y)) ==> Z \ f(a, Z' \ g(y) )

    Term V = tom_make_Var("_");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("g", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType()), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("T") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());
    Term gv =tom_make_Appl("g",tom_cons_list_TermList(V,tom_empty_list_TermList()));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());

    Term pattern = tom_make_Sub(V,tom_make_Appl("f",tom_cons_list_TermList(a,tom_cons_list_TermList(tom_make_Sub(V,gv),tom_empty_list_TermList()))));
    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(pattern,r0),tom_empty_list_ConcRule())), eSig);
  }

  private static void example10() {
    Signature eSig = new Signature();
    // !f(x,x) ==> Z \ f(x,x)

    Term V = tom_make_Var("_");
    Term X = tom_make_Var("x");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("b", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("g", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType()), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("T") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());
    Term b =tom_make_Appl("b",tom_empty_list_TermList());
    Term gv =tom_make_Appl("g",tom_cons_list_TermList(V,tom_empty_list_TermList()));

    Term r0 = tom_make_Appl("rhs0",tom_empty_list_TermList());

    Term pattern = tom_make_Sub(V,tom_make_Appl("f",tom_cons_list_TermList(X,tom_cons_list_TermList(X,tom_empty_list_TermList()))));
    Trs res = RewriteSystem.trsRule(tom_make_Otrs(tom_cons_list_ConcRule(tom_make_Rule(pattern,r0),tom_empty_list_ConcRule())), eSig);
  }
  
  private static void example11() {
    Signature eSig = new Signature();

    Term V = tom_make_Var("_");

    eSig.addSymbol("a", tom_empty_list_ConcGomType(), tom_make_GomType("T") );
    eSig.addSymbol("f", tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_cons_list_ConcGomType(tom_make_GomType("T"),tom_empty_list_ConcGomType())), tom_make_GomType("T") );

    Term a =tom_make_Appl("a",tom_empty_list_TermList());

    Term fav =tom_make_Appl("f",tom_cons_list_TermList(a,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fva =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(a,tom_empty_list_TermList())));
    Term fvv =tom_make_Appl("f",tom_cons_list_TermList(V,tom_cons_list_TermList(V,tom_empty_list_TermList())));
    Term fff =tom_make_Appl("f",tom_cons_list_TermList(fvv,tom_cons_list_TermList(fvv,tom_empty_list_TermList())));

    boolean b = RewriteSystem.canBeRemoved3(tom_make_Rule(fvv,a), tom_cons_list_ConcRule(tom_make_Rule(fav,a),tom_cons_list_ConcRule(tom_make_Rule(fva,a),tom_cons_list_ConcRule(tom_make_Rule(fff,a),tom_empty_list_ConcRule()))), eSig);
    System.out.println("b = " + b);

  }

  /*
   * Reduce a list of rules
   */
  public static RuleList reduceRules(RuleList ruleList, Signature eSig) {

    // test subsumtion idea
    {{if (tom_is_sort_RuleList(ruleList)) {if (tom_is_fun_sym_ConcRule((( sa.rule.types.RuleList )(( sa.rule.types.RuleList )ruleList)))) { sa.rule.types.RuleList  tomMatch1_end_4=(( sa.rule.types.RuleList )ruleList);do {{if (!(tom_is_empty_ConcRule_RuleList(tomMatch1_end_4))) {

        //RewriteSystem.canBeRemoved1(`rule, `ConcRule(C1*,C2*), eSig);
        RewriteSystem.canBeRemoved2(tom_get_head_ConcRule_RuleList(tomMatch1_end_4), tom_append_list_ConcRule(tom_get_slice_ConcRule((( sa.rule.types.RuleList )ruleList),tomMatch1_end_4,tom_empty_list_ConcRule()),tom_append_list_ConcRule(tom_get_tail_ConcRule_RuleList(tomMatch1_end_4),tom_empty_list_ConcRule())), eSig);
      }if (tom_is_empty_ConcRule_RuleList(tomMatch1_end_4)) {tomMatch1_end_4=(( sa.rule.types.RuleList )ruleList);} else {tomMatch1_end_4=tom_get_tail_ConcRule_RuleList(tomMatch1_end_4);}}} while(!(tom_equal_term_RuleList(tomMatch1_end_4, (( sa.rule.types.RuleList )ruleList))));}}}}


    return ruleList;
  }
/*
  private static Term nat(int n) {
    if(n==0) {
      return `Appl("Z",TermList());
    } else {
      Term t = nat(n-1);
      return `Appl("S",TermList(t));
    }
    //return `Empty();
  }

  private static void example11() {
    Signature eSig = new Signature();

    Term V = `Var("_");
    eSig.addSymbol("Z", `ConcGomType(), `GomType("Nat") );
    eSig.addSymbol("S", `ConcGomType(GomType("Nat")), `GomType("Nat") );

    Term A = `Add(ConcAdd(nat(1),nat(4),nat(7)));

    Term r = `Appl("rhs",TermList());

    RuleList candidates = `ConcRule(Rule(A,r));
    RuleList kernel = `ConcRule();
    RuleList res = RewriteSystem.removeRedundantRule(candidates,kernel,eSig);

    for(Rule rule:`res.getCollectionConcRule()) {
      System.out.println(Pretty.toString(rule));
    }
    System.out.println("size = " + `res.length());

  }
  */
}
