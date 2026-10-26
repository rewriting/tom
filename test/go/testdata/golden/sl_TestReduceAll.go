package sl













import (
  "testing"

  "tom/library/sl"
)

  func tom_TestReduceAll_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_TestReduceAll_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_TestReduceAll_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_TestReduceAll_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_TestReduceAll_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_TestReduceAll_is_sort_char(t rune) bool { return true;  }; func tom_TestReduceAll_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_TestReduceAll_is_sort_String(t string) bool { return true;  }; func tom_TestReduceAll_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_TestReduceAll_is_sort_int(t int64) bool { return true;  }; func tom_TestReduceAll_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_TestReduceAll_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_TestReduceAll_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_TestReduceAll_make_Fail()  sl.Strategy  { return (sl.NewFail()) }; func tom_TestReduceAll_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_TestReduceAll_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_TestReduceAll_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_TestReduceAll_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_TestReduceAll_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_TestReduceAll_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_TestReduceAll_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReduceAll_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReduceAll_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReduceAll_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_TestReduceAll_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_TestReduceAll_is_fun_sym_Sequence(l1) { if tom_TestReduceAll_is_empty_Sequence_Strategy(tom_TestReduceAll_get_tail_Sequence_Strategy(l1)) { return tom_TestReduceAll_cons_list_Sequence(tom_TestReduceAll_get_head_Sequence_Strategy(l1), l2) } else { return tom_TestReduceAll_cons_list_Sequence(tom_TestReduceAll_get_head_Sequence_Strategy(l1), tom_TestReduceAll_append_list_Sequence(tom_TestReduceAll_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_TestReduceAll_cons_list_Sequence(l1, l2) }; }; func tom_TestReduceAll_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_equal_term_Strategy(begin, end) { return tail } else if tom_TestReduceAll_equal_term_Strategy(end, tail) && (tom_TestReduceAll_is_empty_Sequence_Strategy(end) || tom_TestReduceAll_equal_term_Strategy(end, tom_TestReduceAll_empty_list_Sequence())) { return begin }; return tom_TestReduceAll_cons_list_Sequence(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_Sequence(begin) { return tom_TestReduceAll_get_head_Sequence_Strategy(begin) }; return begin }(), tom_TestReduceAll_get_slice_Sequence(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_Sequence(begin) { return tom_TestReduceAll_get_tail_Sequence_Strategy(begin) }; return tom_TestReduceAll_empty_list_Sequence() }(), end, tail)) }; func tom_TestReduceAll_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_TestReduceAll_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_TestReduceAll_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_TestReduceAll_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReduceAll_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReduceAll_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReduceAll_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_TestReduceAll_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_TestReduceAll_is_fun_sym_SequenceId(l1) { if tom_TestReduceAll_is_empty_SequenceId_Strategy(tom_TestReduceAll_get_tail_SequenceId_Strategy(l1)) { return tom_TestReduceAll_cons_list_SequenceId(tom_TestReduceAll_get_head_SequenceId_Strategy(l1), l2) } else { return tom_TestReduceAll_cons_list_SequenceId(tom_TestReduceAll_get_head_SequenceId_Strategy(l1), tom_TestReduceAll_append_list_SequenceId(tom_TestReduceAll_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_TestReduceAll_cons_list_SequenceId(l1, l2) }; }; func tom_TestReduceAll_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_equal_term_Strategy(begin, end) { return tail } else if tom_TestReduceAll_equal_term_Strategy(end, tail) && (tom_TestReduceAll_is_empty_SequenceId_Strategy(end) || tom_TestReduceAll_equal_term_Strategy(end, tom_TestReduceAll_empty_list_SequenceId())) { return begin }; return tom_TestReduceAll_cons_list_SequenceId(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_SequenceId(begin) { return tom_TestReduceAll_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_TestReduceAll_get_slice_SequenceId(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_SequenceId(begin) { return tom_TestReduceAll_get_tail_SequenceId_Strategy(begin) }; return tom_TestReduceAll_empty_list_SequenceId() }(), end, tail)) }; func tom_TestReduceAll_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_TestReduceAll_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_TestReduceAll_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_TestReduceAll_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReduceAll_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReduceAll_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReduceAll_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_is_empty_Choice_Strategy(l1) { return l2 } else if tom_TestReduceAll_is_empty_Choice_Strategy(l2) { return l1 } else if tom_TestReduceAll_is_fun_sym_Choice(l1) { if tom_TestReduceAll_is_empty_Choice_Strategy(tom_TestReduceAll_get_tail_Choice_Strategy(l1)) { return tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_get_head_Choice_Strategy(l1), l2) } else { return tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_get_head_Choice_Strategy(l1), tom_TestReduceAll_append_list_Choice(tom_TestReduceAll_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_TestReduceAll_cons_list_Choice(l1, l2) }; }; func tom_TestReduceAll_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_equal_term_Strategy(begin, end) { return tail } else if tom_TestReduceAll_equal_term_Strategy(end, tail) && (tom_TestReduceAll_is_empty_Choice_Strategy(end) || tom_TestReduceAll_equal_term_Strategy(end, tom_TestReduceAll_empty_list_Choice())) { return begin }; return tom_TestReduceAll_cons_list_Choice(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_Choice(begin) { return tom_TestReduceAll_get_head_Choice_Strategy(begin) }; return begin }(), tom_TestReduceAll_get_slice_Choice(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_Choice(begin) { return tom_TestReduceAll_get_tail_Choice_Strategy(begin) }; return tom_TestReduceAll_empty_list_Choice() }(), end, tail)) }; func tom_TestReduceAll_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_TestReduceAll_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_TestReduceAll_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_TestReduceAll_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReduceAll_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReduceAll_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReduceAll_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_TestReduceAll_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_TestReduceAll_is_fun_sym_ChoiceId(l1) { if tom_TestReduceAll_is_empty_ChoiceId_Strategy(tom_TestReduceAll_get_tail_ChoiceId_Strategy(l1)) { return tom_TestReduceAll_cons_list_ChoiceId(tom_TestReduceAll_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_TestReduceAll_cons_list_ChoiceId(tom_TestReduceAll_get_head_ChoiceId_Strategy(l1), tom_TestReduceAll_append_list_ChoiceId(tom_TestReduceAll_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_TestReduceAll_cons_list_ChoiceId(l1, l2) }; }; func tom_TestReduceAll_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReduceAll_equal_term_Strategy(begin, end) { return tail } else if tom_TestReduceAll_equal_term_Strategy(end, tail) && (tom_TestReduceAll_is_empty_ChoiceId_Strategy(end) || tom_TestReduceAll_equal_term_Strategy(end, tom_TestReduceAll_empty_list_ChoiceId())) { return begin }; return tom_TestReduceAll_cons_list_ChoiceId(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_ChoiceId(begin) { return tom_TestReduceAll_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_TestReduceAll_get_slice_ChoiceId(func()  sl.Strategy  { if tom_TestReduceAll_is_fun_sym_ChoiceId(begin) { return tom_TestReduceAll_get_tail_ChoiceId_Strategy(begin) }; return tom_TestReduceAll_empty_list_ChoiceId() }(), end, tail)) }; func tom_TestReduceAll_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_cons_list_Choice(s,tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_make_Identity(),tom_TestReduceAll_empty_list_Choice()))) }; func tom_TestReduceAll_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_make_mu(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_cons_list_Sequence(s,tom_TestReduceAll_cons_list_Sequence(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_empty_list_Sequence())),tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_make_Identity(),tom_TestReduceAll_empty_list_Choice())))) }; func tom_TestReduceAll_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_make_mu(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_cons_list_SequenceId(v,tom_TestReduceAll_cons_list_SequenceId(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_empty_list_SequenceId())))) }; func tom_TestReduceAll_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_make_mu(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_cons_list_Choice(v,tom_TestReduceAll_cons_list_Choice(tom_TestReduceAll_make_One(tom_TestReduceAll_make_MuVar("_x")),tom_TestReduceAll_empty_list_Choice())))) }; func tom_TestReduceAll_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_make_mu(tom_TestReduceAll_make_MuVar("_x"),tom_TestReduceAll_cons_list_ChoiceId(v,tom_TestReduceAll_cons_list_ChoiceId(tom_TestReduceAll_make_OneId(tom_TestReduceAll_make_MuVar("_x")),tom_TestReduceAll_empty_list_ChoiceId())))) }; func tom_TestReduceAll_make_Outermost(v  sl.Strategy )  sl.Strategy  { return (tom_TestReduceAll_make_Repeat(tom_TestReduceAll_make_OnceTopDown(v))) }; func tom_TestReduceAll_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestReduceAll_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestReduceAll_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_TestReduceAll_make_f(lhs  Term , rhs  Term )  Term  { return (MakeF(lhs,rhs)) }; func tom_TestReduceAll_get_slot_f_lhs(t  Term )  Term  { return (t).(*FTerm).Lhs;  }; func tom_TestReduceAll_get_slot_f_rhs(t  Term )  Term  { return (t).(*FTerm).Rhs;  }; func tom_TestReduceAll_is_fun_sym_g(t  Term ) bool { return func() bool { _, ok := (t).(*GTerm); return ok }();  }; func tom_TestReduceAll_make_g(arg  Term )  Term  { return (MakeG(arg)) }; func tom_TestReduceAll_get_slot_g_arg(t  Term )  Term  { return (t).(*GTerm).Arg;  }; func tom_TestReduceAll_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_TestReduceAll_make_a()  Term  { return (MakeA()) }; func tom_TestReduceAll_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_TestReduceAll_make_b()  Term  { return (MakeB()) }; func tom_TestReduceAll_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_TestReduceAll_make_c()  Term  { return (MakeC()) };   func init() {   HookMakeF = func(x Term, y Term) (Term, Term, Term) {     tom__r := func() Term {     {        ; { { if tom_TestReduceAll_is_sort_Term(x) { if tom_TestReduceAll_is_fun_sym_b(any(x).( Term )) { if tom_TestReduceAll_is_sort_Term(y) { if tom_TestReduceAll_is_fun_sym_b(any(y).( Term )) {  return tom_TestReduceAll_make_a(); ;  };  };  };  };  };  };           };     return nil;     }();     return tom__r, x, y;   }; };  func tom_TestReduceAll_make__f(arg0  sl.Strategy , arg1  sl.Strategy )  sl.Strategy  { return (NewVisitF(arg0, arg1)) }; type AB struct { *sl.AbstractStrategyBasic; }; func tom_new_AB() *AB { tom__strat := &AB{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReduceAll_make_Fail()); return tom__strat }; func (tom__strat *AB) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *AB) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *AB) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *AB) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *AB) visitLight(v any, introspector sl.Introspector) any { if tom_TestReduceAll_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *AB) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *AB) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_TestReduceAll_is_sort_Term(tom__arg) { if tom_TestReduceAll_is_fun_sym_a(any(tom__arg).( Term )) { 


















 return tom_TestReduceAll_make_b(); ;  };  };  }; { if tom_TestReduceAll_is_sort_Term(tom__arg) { if tom_TestReduceAll_is_fun_sym_b(any(tom__arg).( Term )) { 
 return tom_TestReduceAll_make_c(); ;  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_TestReduceAll_make_AB()  sl.Strategy  { return (tom_new_AB()) }; 



func TestOneRed(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_b())
    var s sl.Strategy = tom_TestReduceAll_make_One(tom_TestReduceAll_make_AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,tom_TestReduceAll_make_a())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,tom_TestReduceAll_make_a())
    assertEquals(t, resJ,resS)
}



func TestAllRed_Visit(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_b())
    var s sl.Strategy = tom_TestReduceAll_make_All(tom_TestReduceAll_make_AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,tom_TestReduceAll_make_f(tom_TestReduceAll_make_b(),tom_TestReduceAll_make_c()))
}



func TestAllRed_Fire(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_b())
    var s sl.Strategy = tom_TestReduceAll_make_All(tom_TestReduceAll_make_AB())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,tom_TestReduceAll_make_f(tom_TestReduceAll_make_b(),tom_TestReduceAll_make_c()))
}



func TestCongruenceRed_Visit(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_b())
    var s sl.Strategy = tom_TestReduceAll_make__f(tom_TestReduceAll_make_AB(),tom_TestReduceAll_make_AB())
    var resJ Term = visitLight(t, s, subject).(Term)
    assertEquals(t, resJ,tom_TestReduceAll_make_f(tom_TestReduceAll_make_b(),tom_TestReduceAll_make_c()))
}



func TestCongruenceRed_Fire(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_b())
    var s sl.Strategy = tom_TestReduceAll_make__f(tom_TestReduceAll_make_AB(),tom_TestReduceAll_make_AB())
    var resS Term = visit(t, s, subject).(Term)
    assertEquals(t, resS,tom_TestReduceAll_make_f(tom_TestReduceAll_make_b(),tom_TestReduceAll_make_c()))
}

  type G struct { *sl.AbstractStrategyBasic; }; func tom_new_G() *G { tom__strat := &G{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReduceAll_make_Fail()); return tom__strat }; func (tom__strat *G) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *G) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *G) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *G) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *G) visitLight(v any, introspector sl.Introspector) any { if tom_TestReduceAll_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *G) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *G) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_TestReduceAll_is_sort_Term(tom__arg) { if tom_TestReduceAll_is_fun_sym_a(any(tom__arg).( Term )) { 

 return tom_TestReduceAll_make_b(); ;  };  };  }; { if tom_TestReduceAll_is_sort_Term(tom__arg) { if tom_TestReduceAll_is_fun_sym_g(any(tom__arg).( Term )) { if tom_TestReduceAll_is_fun_sym_a(any(tom_TestReduceAll_get_slot_g_arg(any(tom__arg).( Term ))).( Term )) { 
 return tom_TestReduceAll_make_b(); ;  };  };  };  }; { if tom_TestReduceAll_is_sort_Term(tom__arg) { if tom_TestReduceAll_is_fun_sym_g(any(tom__arg).( Term )) { var tomMatch3_10  Term  = tom_TestReduceAll_get_slot_g_arg(any(tom__arg).( Term )); if tom_TestReduceAll_is_fun_sym_f(any(tomMatch3_10).( Term )) { if tom_TestReduceAll_is_fun_sym_c(any(tom_TestReduceAll_get_slot_f_lhs(tomMatch3_10)).( Term )) { if tom_TestReduceAll_is_fun_sym_c(any(tom_TestReduceAll_get_slot_f_rhs(tomMatch3_10)).( Term )) { 
 return tom_TestReduceAll_make_c(); ;  };  };  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_TestReduceAll_make_G()  sl.Strategy  { return (tom_new_G()) }; 



func TestOutermost(t *testing.T) {
    var subject Term = tom_TestReduceAll_make_g(tom_TestReduceAll_make_f(tom_TestReduceAll_make_a(),tom_TestReduceAll_make_a()))
    var s sl.Strategy = tom_TestReduceAll_make_Outermost(tom_TestReduceAll_make_G())
    var res1 Term = visit(t, s, subject).(Term)
    assertEquals(t, res1,tom_TestReduceAll_make_b())
    var res2 Term = visitLight(t, s, subject).(Term)
    assertEquals(t, res2,tom_TestReduceAll_make_b())
}






func visitLight(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}




func visit(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
