package newbehaviour
















import (
  . "fixture/testsl"
  "tom/library/sl"
)




type NewBehaviour struct{}

  func tom_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_make_Fail()  sl.Strategy  { return (sl.NewFail()) }; func tom_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_make_Omega(pos  int64 , v  sl.Strategy )  sl.Strategy  { return (sl.NewOmega(int(pos), v)) }; func tom_make_IfThenElse(condition  sl.Strategy , trueCase  sl.Strategy , falseCase  sl.Strategy )  sl.Strategy  { return (sl.NewIfThenElse(condition, trueCase, falseCase)) }; func tom_make_Not(v  sl.Strategy )  sl.Strategy  { return (sl.NewNot(v)) }; func tom_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_is_fun_sym_Sequence(l1) { if tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), l2) } else { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_cons_list_Sequence(l1, l2) }; }; func tom_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Sequence_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Sequence())) { return begin }; return tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_head_Sequence_Strategy(begin) }; return begin }(), tom_get_slice_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_tail_Sequence_Strategy(begin) }; return tom_empty_list_Sequence() }(), end, tail)) }; func tom_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_SequenceId(l1) { if tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), l2) } else { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_cons_list_SequenceId(l1, l2) }; }; func tom_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_SequenceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_SequenceId())) { return begin }; return tom_cons_list_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_get_slice_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_tail_SequenceId_Strategy(begin) }; return tom_empty_list_SequenceId() }(), end, tail)) }; func tom_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Choice_Strategy(l1) { return l2 } else if tom_is_empty_Choice_Strategy(l2) { return l1 } else if tom_is_fun_sym_Choice(l1) { if tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), l2) } else { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_cons_list_Choice(l1, l2) }; }; func tom_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Choice_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Choice())) { return begin }; return tom_cons_list_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_head_Choice_Strategy(begin) }; return begin }(), tom_get_slice_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_tail_Choice_Strategy(begin) }; return tom_empty_list_Choice() }(), end, tail)) }; func tom_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_ChoiceId(l1) { if tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_cons_list_ChoiceId(l1, l2) }; }; func tom_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_ChoiceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_ChoiceId())) { return begin }; return tom_cons_list_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_get_slice_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_tail_ChoiceId_Strategy(begin) }; return tom_empty_list_ChoiceId() }(), end, tail)) }; func tom_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) }; func tom_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())))) }; func tom_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())))) }; func tom_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice())))) }; func tom_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId())))) }; func tom_make_Innermost(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(tom_make_Try(tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence()))),tom_empty_list_Sequence())))) }; func tom_make_OnceBottomUp(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_cons_list_Choice(v,tom_empty_list_Choice())))) }; func tom_make_Outermost(v  sl.Strategy )  sl.Strategy  { return (tom_make_Repeat(tom_make_OnceTopDown(v))) }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_c()  Term  { return (MakeC()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(arg1  Term )  Term  { return (MakeF(arg1)) }; func tom_get_slot_f_arg1(t  Term )  Term  { return (t).(*FTerm).Arg1;  }; func tom_make_g(arg1  Term , arg2  Term )  Term  { return (MakeG(arg1,arg2)) }; func tom_is_fun_sym_l(t  Term ) bool { return func() bool { _, ok := (t).(*LTerm); return ok }();  }; func tom_empty_list_l()  Term  { return (MakeL()) }; func tom_cons_list_l(e  Term , l  Term )  Term  { return (func() Term { s := (l).(*LTerm).Slots; return MakeL(append([]Term{e}, s...)...) }()) }; func tom_get_head_l_Term(l  Term )  Term  { return (l).(*LTerm).Slots[0];  }; func tom_get_tail_l_Term(l  Term )  Term  { return func() Term { s := (l).(*LTerm).Slots; return MakeL(s[1:]...) }();  }; func tom_is_empty_l_Term(l  Term ) bool { return len((l).(*LTerm).Slots) == 0;  }; func tom_append_list_l(l1  Term , l2  Term )  Term  { if tom_is_empty_l_Term(l1) { return l2 } else if tom_is_empty_l_Term(l2) { return l1 } else if tom_is_fun_sym_l(l1) { if tom_is_empty_l_Term(tom_get_tail_l_Term(l1)) { return tom_cons_list_l(tom_get_head_l_Term(l1), l2) } else { return tom_cons_list_l(tom_get_head_l_Term(l1), tom_append_list_l(tom_get_tail_l_Term(l1), l2)) } } else { return tom_cons_list_l(l1, l2) }; }; func tom_get_slice_l(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_l_Term(end) || tom_equal_term_Term(end, tom_empty_list_l())) { return begin }; return tom_cons_list_l(func()  Term  { if tom_is_fun_sym_l(begin) { return tom_get_head_l_Term(begin) }; return begin }(), tom_get_slice_l(func()  Term  { if tom_is_fun_sym_l(begin) { return tom_get_tail_l_Term(begin) }; return tom_empty_list_l() }(), end, tail)) }; func tom_make_When_f(s  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(&IsF{}, s)) }; func tom_make__g(arg0  sl.Strategy , arg1  sl.Strategy )  sl.Strategy  { return (NewVisitG(arg0, arg1)) }; func tom_make__l(sub  sl.Strategy )  sl.Strategy  { return (NewVisitL(sub)) }; type R1 struct { *sl.AbstractStrategyBasic; }; func tom_new_R1() *R1 { tom__strat := &R1{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *R1) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R1) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R1) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R1) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R1) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R1) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R1) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_b());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_b(any(tom__arg).( Term )) { 
 return tom_make_c();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R1()  sl.Strategy  { return (tom_new_R1()) }; type R2 struct { *sl.AbstractStrategyBasic; }; func tom_new_R2() *R2 { tom__strat := &R2{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Fail()); return tom__strat }; func (tom__strat *R2) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R2) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R2) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R2) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R2) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R2) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R2) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_b());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_b(any(tom__arg).( Term )) { 
 return tom_make_c();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R2()  sl.Strategy  { return (tom_new_R2()) }; type Builda struct { *sl.AbstractStrategyBasic; }; func tom_new_Builda() *Builda { tom__strat := &Builda{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *Builda) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *Builda) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *Builda) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *Builda) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *Builda) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *Builda) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *Builda) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { 




 return tom_make_a();  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_Builda()  sl.Strategy  { return (tom_new_Builda()) }; type Buildb struct { *sl.AbstractStrategyBasic; }; func tom_new_Buildb() *Buildb { tom__strat := &Buildb{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *Buildb) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *Buildb) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *Buildb) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *Buildb) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *Buildb) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *Buildb) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *Buildb) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { 




 return tom_make_b();  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_Buildb()  sl.Strategy  { return (tom_new_Buildb()) }; 



  func (nb *NewBehaviour) Test1() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_Identity()
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test2() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = (tom_make_R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test3() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_One(tom_make_R1())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test4() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_OnceBottomUp((tom_make_R2()))
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test5() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_All((tom_make_R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestChoice() (Term, error) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_cons_list_Choice(tom_make_R2(),tom_cons_list_Choice(tom_make_R1(),tom_empty_list_Choice()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestChoiceSideEffect() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_Choice(tom_cons_list_Sequence(tom_make_R1(),tom_cons_list_Sequence(tom_make_R2(),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_R1(),tom_empty_list_Choice()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestNot() (Term, error) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_Not(tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestNotSideEffect() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_Not(tom_cons_list_Sequence(tom_make_R1(),tom_cons_list_Sequence(tom_make_R2(),tom_empty_list_Sequence())))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test6() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_cons_list_Sequence(tom_make_All(tom_make_R2()),tom_cons_list_Sequence(tom_make_All(tom_make_All(tom_make_R1())),tom_empty_list_Sequence()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test7() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(1,tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test8() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(2,tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test9() (Term, error) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_IfThenElse(tom_make_R2(),tom_make_Builda(),tom_make_Buildb())
    return visit(s, subject)
  }
  
  func (nb *NewBehaviour) TestITESideEffect() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_IfThenElse(tom_make_R2(),tom_make_Identity(),tom_make_Buildb())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test10() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_IfThenElse(tom_make_R2(),tom_make_Builda(),tom_make_Buildb())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test11() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_OnceTopDownId((tom_make_R1()))
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    { r, err := visit(s, subject); if err != nil { return nil, err }; subject = r }
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test12() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Innermost((tom_make_R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) Test13() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Outermost((tom_make_R2()))
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestWhen1() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_When_f(tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruence1() (Term, error) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make__g(tom_make_R1(),tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruence2() (Term, error) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make__g(tom_make_R1(),tom_make_R2())
    return visit(s, subject)
  }

  func (nb *NewBehaviour) TestCongruenceList() (Term, error) {
    var subject Term = tom_cons_list_l(tom_make_a(),tom_cons_list_l(tom_make_b(),tom_empty_list_l()))
    var s sl.Strategy = tom_make__l(tom_make_R1())
    return visit(s, subject)
  }






func visit(s sl.Strategy, subject Term) (Term, error) {
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    return nil, err
  }
  return r.(Term), nil
}
