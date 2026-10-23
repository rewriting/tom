package sl


















import (
  "fmt"
  "testing"

  . "fixture/testsl"
  "tom/library/sl"
)

  func tom_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_make_Fail()  sl.Strategy  { return (sl.NewFail()) }; func tom_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_make_Omega(pos  int64 , v  sl.Strategy )  sl.Strategy  { return (sl.NewOmega(int(pos), v)) }; func tom_make_IfThenElse(condition  sl.Strategy , trueCase  sl.Strategy , falseCase  sl.Strategy )  sl.Strategy  { return (sl.NewIfThenElse(condition, trueCase, falseCase)) }; func tom_make_Not(v  sl.Strategy )  sl.Strategy  { return (sl.NewNot(v)) }; func tom_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_is_fun_sym_Sequence(l1) { if tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), l2) } else { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_cons_list_Sequence(l1, l2) }; }; func tom_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Sequence_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Sequence())) { return begin }; return tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_head_Sequence_Strategy(begin) }; return begin }(), tom_get_slice_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_tail_Sequence_Strategy(begin) }; return tom_empty_list_Sequence() }(), end, tail)) }; func tom_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_SequenceId(l1) { if tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), l2) } else { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_cons_list_SequenceId(l1, l2) }; }; func tom_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_SequenceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_SequenceId())) { return begin }; return tom_cons_list_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_get_slice_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_tail_SequenceId_Strategy(begin) }; return tom_empty_list_SequenceId() }(), end, tail)) }; func tom_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Choice_Strategy(l1) { return l2 } else if tom_is_empty_Choice_Strategy(l2) { return l1 } else if tom_is_fun_sym_Choice(l1) { if tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), l2) } else { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_cons_list_Choice(l1, l2) }; }; func tom_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Choice_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Choice())) { return begin }; return tom_cons_list_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_head_Choice_Strategy(begin) }; return begin }(), tom_get_slice_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_tail_Choice_Strategy(begin) }; return tom_empty_list_Choice() }(), end, tail)) }; func tom_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_ChoiceId(l1) { if tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_cons_list_ChoiceId(l1, l2) }; }; func tom_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_ChoiceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_ChoiceId())) { return begin }; return tom_cons_list_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_get_slice_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_tail_ChoiceId_Strategy(begin) }; return tom_empty_list_ChoiceId() }(), end, tail)) }; func tom_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) }; func tom_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())))) }; func tom_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())))) }; func tom_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice())))) }; func tom_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId())))) }; func tom_make_OnceBottomUp(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_cons_list_Choice(v,tom_empty_list_Choice())))) }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_make_b()  Term  { return (MakeB()) }; func tom_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_make_c()  Term  { return (MakeC()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(arg1  Term )  Term  { return (MakeF(arg1)) }; func tom_get_slot_f_arg1(t  Term )  Term  { return (t).(*FTerm).Arg1;  }; func tom_make_g(arg1  Term , arg2  Term )  Term  { return (MakeG(arg1,arg2)) }; func tom_make_Make_a()  sl.Strategy  { return (NewBuildA()) }; func tom_make_Make_b()  sl.Strategy  { return (NewBuildB()) }; func tom_make_When_f(s  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(&IsF{}, s)) }; func tom_make_Make_f(a0  sl.Strategy )  sl.Strategy  { return (NewBuildF(a0)) }; func tom_make__g(arg0  sl.Strategy , arg1  sl.Strategy )  sl.Strategy  { return (NewVisitG(arg0, arg1)) }; func tom_make_Make_g(a0  sl.Strategy , a1  sl.Strategy )  sl.Strategy  { return (NewBuildG(a0, a1)) }; type R1 struct { *sl.AbstractStrategyBasic; }; func tom_new_R1() *R1 { tom__strat := &R1{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *R1) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R1) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R1) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R1) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R1) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R1) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R1) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_b());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_b(any(tom__arg).( Term )) { 
 return tom_make_c();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R1()  sl.Strategy  { return (tom_new_R1()) }; type R2 struct { *sl.AbstractStrategyBasic; }; func tom_new_R2() *R2 { tom__strat := &R2{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Fail()); return tom__strat }; func (tom__strat *R2) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R2) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R2) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R2) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R2) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R2) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R2) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_b());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_b(any(tom__arg).( Term )) { 
 return tom_make_c();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R2()  sl.Strategy  { return (tom_new_R2()) }; type R3 struct { *sl.AbstractStrategyBasic; }; func tom_new_R3() *R3 { tom__strat := &R3{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Fail()); return tom__strat }; func (tom__strat *R3) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R3) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R3) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R3) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R3) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R3) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R3) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_c());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_c(any(tom__arg).( Term )) { 
 return tom_make_a();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R3()  sl.Strategy  { return (tom_new_R3()) }; type R4 struct { *sl.AbstractStrategyBasic; }; func tom_new_R4() *R4 { tom__strat := &R4{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *R4) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *R4) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *R4) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *R4) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *R4) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *R4) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *R4) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_f(any(tom__arg).( Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_arg1(any(tom__arg).( Term ))).( Term )) { 





 return tom_make_f(tom_make_c());  };  };  };  }; { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_c(any(tom__arg).( Term )) { 
 return tom_make_a();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_R4()  sl.Strategy  { return (tom_new_R4()) }; 



  func TestIdentity(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_Identity()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Identity do not change subject",subject,resJ)
    } else {
      fail(t, "Identity.visit should not fail")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Identity do not change subject",subject,resS)
    } else {
      fail(t, "Identity.fire should not fail")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_R1()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "R1.visit should not fail on f(a)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "R1.fire should not fail on f(a)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR1Fail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_R1()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule : fail is identity",resJ,subject)
    } else {
      fail(t, "R1.visit should not throw failure on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,subject)
    } else {
      fail(t, "R1.fire should not throw failure on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_R2()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "R2.visit should not fail on f(a)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "R2.fire should not fail on f(a)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestR2Fail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_R2()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "R2.visit should fail on f(b)")
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "R2.fire should fail on f(b)")
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOne(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_One(tom_make_R1())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_f(tom_make_c()))
    } else {
      fail(t, "One(R1).visit should not fail on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_c()))
    } else {
      fail(t, "One(R1).fire should not fail on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestOneFail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_One(tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "One(R2).visit should fail on f(a)")
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "One(R2).fire should fail on f(a)")
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOnceBottomUp(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_OnceBottomUp((tom_make_R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_b()),tom_make_b()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_b()),tom_make_b()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_c()),tom_make_b()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_c()),tom_make_b()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_c()),tom_make_c()))
    } else {
      fail(t, "OnceBottomUp(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_c()),tom_make_c()))
    } else {
      fail(t, "OnceBottomUp(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    subject = resJ
    
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "OnceBottomUp(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      resJ = nil
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "OnceBottomUp(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      resS = nil
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestAll(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_All((tom_make_R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_b()),tom_make_c()))
    } else {
      fail(t, "All(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_b()),tom_make_c()))
    } else {
      fail(t, "All(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
    
    subject = resJ
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "All(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      resJ = nil
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "All(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      resS = nil
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestAllFail(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_b())
    var s sl.Strategy = tom_make_All((tom_make_R2()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "All(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "All(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resS)
    assertNull(t, resJ)
  }

  func TestOmega0(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_Omega(0,tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Omega(0,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Omega(0,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega1(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(1,tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_b()),tom_make_b()))
    } else {
      fail(t, "Omega(1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_b()),tom_make_b()))
    } else {
      fail(t, "Omega(1,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega2(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(2,tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_a()),tom_make_c()))
    } else {
      fail(t, "Omega(2,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_a()),tom_make_c()))
    } else {
      fail(t, "Omega(2,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestOmega1Fail(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(1,tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Omega(1,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Omega(1,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestOmegaAritOverflow(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_b()),tom_make_b())
    var s sl.Strategy = tom_make_Omega(3,tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Omega(3,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Omega(3,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestMake_a(t *testing.T) {
    var subject Term = tom_make_f(tom_make_c())
    var s sl.Strategy = tom_make_Make_a()
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_a())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_a())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestMake_gfab(t *testing.T) {
    var subject Term = tom_make_f(tom_make_c())
    var s sl.Strategy = tom_make_Make_g(tom_make_Make_f(tom_make_Make_a()),tom_make_Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_g(tom_make_f(tom_make_a()),tom_make_b()))
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_a()),tom_make_b()))
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestIfThenElse1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_IfThenElse(tom_make_R2(),tom_make_Make_a(),tom_make_Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_a())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_a())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestIfThenElse2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_IfThenElse(tom_make_R2(),tom_make_Make_a(),tom_make_Make_b())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_b())
    } else {
      fail(t, "IfThenElse.visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_b())
    } else {
      fail(t, "IfThenElse.fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequence(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_Sequence(tom_make_R2(),tom_cons_list_Sequence(tom_make_R3(),tom_empty_list_Sequence()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied sequence",resJ,tom_make_f(tom_make_c()))
    } else {
      fail(t, "Sequence(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_c()))
    } else {
      fail(t, "Sequence(R2,R3).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequenceFail1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_c())
    var s sl.Strategy = tom_cons_list_Sequence(tom_make_R2(),tom_cons_list_Sequence(tom_make_Identity(),tom_empty_list_Sequence()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Sequence(R2,id).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Sequence(R2,id).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestSequenceFail2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_c())
    var s sl.Strategy = tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_R3(),tom_empty_list_Sequence()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Sequence(Identity,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Sequence(Identity,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestWhen(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_When_f(tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied sequence",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "When_f(R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "When_f(R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestWhenFail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_When_f(tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "When_f(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "When_f(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestCongruence(t *testing.T) {
    var subject Term = tom_make_g(tom_make_f(tom_make_a()),tom_make_b())
    var s sl.Strategy = tom_make__g(tom_make_R1(),tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied congruence",resJ,tom_make_g(tom_make_f(tom_make_b()),tom_make_c()))
    } else {
      fail(t, "_g(R1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_g(tom_make_f(tom_make_b()),tom_make_c()))
    } else {
      fail(t, "_g(R1,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestNotR2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_make_Not(tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Not(R2).visit should not fail on f(b)")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied a rule",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Not(R2).fire should not fail on f(b)")
    }
    assertEquals(t, resJ,resS)
  }

  func TestNotR2Fail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_make_Not(tom_make_R2())
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Not(R2).visit should fail on "+fmt.Sprint(subject))
    } else {
      assertNull(t, resJ)
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Not(R2).fire should fail on "+fmt.Sprint(subject))
    } else {
      assertNull(t, resS)
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestSequenceId(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_SequenceId(tom_make_R1(),tom_cons_list_SequenceId(tom_make_R4(),tom_empty_list_SequenceId()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied SequenceId",resJ,tom_make_f(tom_make_c()))
    } else {
      fail(t, "SequenceId(R1,R4).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied SequenceId",resS,tom_make_f(tom_make_c()))
    } else {
      fail(t, "SequenceId(R1,R4).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestSequenceIdFail1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_c())
    var s sl.Strategy = tom_cons_list_SequenceId(tom_make_R1(),tom_cons_list_SequenceId(tom_make_Identity(),tom_empty_list_SequenceId()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEquals(t, resJ,subject)
    } else {
      fail(t, "SequenceId(R1,id).visit should never throw failure")
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEquals(t, resS,subject)
    } else {
      fail(t, "SequenceId(R1,id).visit should never throw failure")
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoice1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_Choice(tom_make_R2(),tom_cons_list_Choice(tom_make_R3(),tom_empty_list_Choice()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choice",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Choice(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choice",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Choice(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoice2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_Choice(tom_make_R3(),tom_cons_list_Choice(tom_make_R2(),tom_empty_list_Choice()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choice",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Choice(R3,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choice",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "Choice(R3,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceFail(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_Choice(tom_make_R3(),tom_cons_list_Choice(tom_make_R3(),tom_empty_list_Choice()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "Choice(R3,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "Choice(R3,R3).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }

  func TestChoiceId1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_ChoiceId(tom_make_R2(),tom_cons_list_ChoiceId(tom_make_R3(),tom_empty_list_ChoiceId()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "ChoiceId(R2,R3).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "ChoiceId(R2,R3).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceId2(t *testing.T) {
    var subject Term = tom_make_f(tom_make_a())
    var s sl.Strategy = tom_cons_list_ChoiceId(tom_make_Identity(),tom_cons_list_ChoiceId(tom_make_R2(),tom_empty_list_ChoiceId()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resJ,tom_make_f(tom_make_b()))
    } else {
      fail(t, "ChoiceId(Identity,R2).visit should not fail on "+fmt.Sprint(subject))
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      assertEqualsMsg(t, "Applied choiceid",resS,tom_make_f(tom_make_b()))
    } else {
      fail(t, "ChoiceId(Identity,R2).fire should not fail on "+fmt.Sprint(subject))
    }
    assertEquals(t, resJ,resS)
  }

  func TestChoiceIdFail1(t *testing.T) {
    var subject Term = tom_make_f(tom_make_b())
    var s sl.Strategy = tom_cons_list_ChoiceId(tom_make_R2(),tom_cons_list_ChoiceId(tom_make_R2(),tom_empty_list_ChoiceId()))
    var resJ Term = nil
    var resS Term = nil
    if r, err := s.VisitLight(subject, sl.VisitableIntrospector{}); err == nil {
      resJ = r.(Term)
      fail(t, "ChoiceId(R2,R2).visit should fail on "+fmt.Sprint(subject))
    } else {
    }
    if r, err := s.Visit(subject, sl.VisitableIntrospector{}); err == nil {
      resS = r.(Term)
      fail(t, "ChoiceId(R2,R2).fire should fail on "+fmt.Sprint(subject))
    } else {
    }
    assertNull(t, resJ)
    assertNull(t, resS)
  }








func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  assertEqualsMsg(t, "", expected, actual)
}




func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}




func assertNull(t *testing.T, object Term) {
  t.Helper()
  if object != nil {
    t.Errorf("expected null, but was:<%v>", object)
  }
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
