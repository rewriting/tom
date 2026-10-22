package teststrategy










import (
  "container/list"
  "testing"

  "tom/library/sl"
)

func tom_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_is_fun_sym_Sequence(l1) { if tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), l2) } else { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_cons_list_Sequence(l1, l2) }; }; func tom_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Sequence_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Sequence())) { return begin }; return tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_head_Sequence_Strategy(begin) }; return begin }(), tom_get_slice_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_tail_Sequence_Strategy(begin) }; return tom_empty_list_Sequence() }(), end, tail)) }; func tom_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_SequenceId(l1) { if tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), l2) } else { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_cons_list_SequenceId(l1, l2) }; }; func tom_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_SequenceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_SequenceId())) { return begin }; return tom_cons_list_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_get_slice_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_tail_SequenceId_Strategy(begin) }; return tom_empty_list_SequenceId() }(), end, tail)) }; func tom_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Choice_Strategy(l1) { return l2 } else if tom_is_empty_Choice_Strategy(l2) { return l1 } else if tom_is_fun_sym_Choice(l1) { if tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), l2) } else { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_cons_list_Choice(l1, l2) }; }; func tom_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Choice_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Choice())) { return begin }; return tom_cons_list_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_head_Choice_Strategy(begin) }; return begin }(), tom_get_slice_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_tail_Choice_Strategy(begin) }; return tom_empty_list_Choice() }(), end, tail)) }; func tom_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_ChoiceId(l1) { if tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_cons_list_ChoiceId(l1, l2) }; }; func tom_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_ChoiceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_ChoiceId())) { return begin }; return tom_cons_list_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_get_slice_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_tail_ChoiceId_Strategy(begin) }; return tom_empty_list_ChoiceId() }(), end, tail)) }; func tom_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) }; func tom_make_BottomUp(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(v,tom_empty_list_Sequence())))) }; func tom_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_is_sort_boolean(t bool) bool { return true;  }; func tom_equal_term_long(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_long(t int64) bool { return true;  }; 
















type Hashtable map[any]any

func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_Term1(t1  Term1 , t2  Term1 ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term1(t any) bool { return func() bool { _, ok := (t).(Term1); return ok }();  }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_equal_term_Pf(t1  Pf , t2  Pf ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Pf(t any) bool { return func() bool { _, ok := (t).(Pf); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_f(s1  Term )  Term  { return (MakeF(s1)) }; func tom_is_fun_sym_g(t  Term ) bool { return func() bool { _, ok := (t).(*GTerm); return ok }();  }; func tom_make_g(s1  Term , s2  Term )  Term  { return (MakeG(s1,s2)) }; func tom_get_slot_g_s1(t  Term )  Term  { return (t).(*GTerm).S1;  }; func tom_get_slot_g_s2(t  Term )  Term  { return (t).(*GTerm).S2;  }; func tom_is_fun_sym_e(t  Term1 ) bool { return func() bool { _, ok := (t).(*ETerm1); return ok }();  }; func tom_make_e()  Term1  { return (MakeE()) }; func tom_is_fun_sym_Unary(t  Pf ) bool { return func() bool { _, ok := (t).(*UnaryPf); return ok }();  }; func tom_make_Unary()  Pf  { return (MakeUnary()) }; func tom_is_fun_sym_Variadic(t  Pf ) bool { return func() bool { _, ok := (t).(*VariadicPf); return ok }();  }; func tom_empty_list_Variadic()  Pf  { return (MakeVariadic()) }; func tom_cons_list_Variadic(e  Pf , l  Pf )  Pf  { return (func() Pf { s := (l).(*VariadicPf).Slots; return MakeVariadic(append([]Pf{e}, s...)...) }()) }; func tom_get_head_Variadic_Pf(l  Pf )  Pf  { return (l).(*VariadicPf).Slots[0];  }; func tom_get_tail_Variadic_Pf(l  Pf )  Pf  { return func() Pf { s := (l).(*VariadicPf).Slots; return MakeVariadic(s[1:]...) }();  }; func tom_is_empty_Variadic_Pf(l  Pf ) bool { return len((l).(*VariadicPf).Slots) == 0;  }; func tom_append_list_Variadic(l1  Pf , l2  Pf )  Pf  { if tom_is_empty_Variadic_Pf(l1) { return l2 } else if tom_is_empty_Variadic_Pf(l2) { return l1 } else if tom_is_fun_sym_Variadic(l1) { if tom_is_empty_Variadic_Pf(tom_get_tail_Variadic_Pf(l1)) { return tom_cons_list_Variadic(tom_get_head_Variadic_Pf(l1), l2) } else { return tom_cons_list_Variadic(tom_get_head_Variadic_Pf(l1), tom_append_list_Variadic(tom_get_tail_Variadic_Pf(l1), l2)) } } else { return tom_cons_list_Variadic(l1, l2) }; }; func tom_get_slice_Variadic(begin  Pf , end  Pf , tail  Pf )  Pf  { if tom_equal_term_Pf(begin, end) { return tail } else if tom_equal_term_Pf(end, tail) && (tom_is_empty_Variadic_Pf(end) || tom_equal_term_Pf(end, tom_empty_list_Variadic())) { return begin }; return tom_cons_list_Variadic(func()  Pf  { if tom_is_fun_sym_Variadic(begin) { return tom_get_head_Variadic_Pf(begin) }; return begin }(), tom_get_slice_Variadic(func()  Pf  { if tom_is_fun_sym_Variadic(begin) { return tom_get_tail_Variadic_Pf(begin) }; return tom_empty_list_Variadic() }(), end, tail)) }; 

















var bool0 bool = true



func TestS(t *testing.T) {

  var hashtable Hashtable = Hashtable{}
  var linkedList *list.List = list.New()
  var bool bool = true
  var along int64 = 1
  var string string = ""
  var i int64 = 0

  var rule0 sl.Strategy = tom_make_S0()
  var rule1 sl.Strategy = tom_make_S1(bool)
  var rule2 sl.Strategy = tom_make_S2(along,string)
  var rule3 sl.Strategy = tom_make_S3(i)
  var rule4 sl.Strategy = tom_make_S4(hashtable,linkedList)
  var rule5 sl.Strategy = tom_make_S5()
  var rule6 sl.Strategy = tom_make_S6()
  var rule7 sl.Strategy = tom_make_S7()
  var rule8 sl.Strategy = tom_make_S8(bool)
  var rule9 sl.Strategy = tom_make_S9(i)

  
  
  
  defer func() {
    if r := recover(); r != nil {
      if _, ok := r.(visitFailure); !ok {
        panic(r)
      }
      println("tom.library.sl.VisitFailure()")
    }
  }()
  assertSame(t, "g(a,a) return a", visitLight(rule0, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "a return b", visitLight(rule1, tom_make_a()), tom_make_b())
  assertSame(t, "g(a,a) return a", visitLight(rule2, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,a) return a", visitLight(rule3, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,b) return g(b,a)", visitLight(rule4, tom_make_g(tom_make_a(),tom_make_b())), tom_make_g(tom_make_b(),tom_make_a()))
  assertSame(t, "g(a,a) return a", visitLight(rule5, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,a) return a", visitLight(rule6, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,a) return a", visitLight(rule7, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,a) return a", visitLight(rule8, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
  assertSame(t, "g(a,a) return a", visitLight(rule9, tom_make_g(tom_make_a(),tom_make_a())), tom_make_a())
}






func TestPosition(tt *testing.T) {
    
    var positions Hashtable = Hashtable{}
    var t Term = tom_make_f(tom_make_a())
    var tBis Term = tom_make_f(tom_make_b())
    var getPos1 sl.Strategy = tom_make_GetPositionA(positions,"p1")
    var getPos2 sl.Strategy = tom_make_GetPositionA(positions,"p2")
    var getPos3 sl.Strategy = tom_make_GetPositionA(positions,"p3")

    visit(tt, tom_make_BottomUp(getPos1), t)
    visit(tt, tom_make_BottomUp(getPos2), t)
    visit(tt, tom_make_BottomUp(getPos3), tBis)

    
    
    p1, _ := positions["p1"].(*sl.Position)
    p2, _ := positions["p2"].(*sl.Position)
    p3, _ := positions["p3"].(*sl.Position)
    assertTrue(tt, "equality on Position", p1.Equals(p2))
    assertFalse(tt, "inequality on Position", p1.Equals(p3))
}

  type GetPositionA struct { *sl.AbstractStrategyBasic; positions  Hashtable ; posName  string ; }; func tom_new_GetPositionA(positions  Hashtable , posName  string ) *GetPositionA { tom__strat := &GetPositionA{positions: positions, posName: posName, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *GetPositionA) getpositions()  Hashtable  { return tom__strat.positions }; func (tom__strat *GetPositionA) getposName()  string  { return tom__strat.posName }; func (tom__strat *GetPositionA) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *GetPositionA) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *GetPositionA) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *GetPositionA) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *GetPositionA) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *GetPositionA) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *GetPositionA) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_a(any(tom__arg).( Term )) { 




tom__strat.positions[tom__strat.posName] = tom__strat.GetEnvironment().GetPosition() };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_GetPositionA(t1  Hashtable , t2  string )  sl.Strategy  { return (tom_new_GetPositionA(t1,t2)) }; type S0 struct { *sl.AbstractStrategyBasic; }; func tom_new_S0() *S0 { tom__strat := &S0{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S0) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S0) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S0) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S0) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S0) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S0) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S0) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 





 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S0()  sl.Strategy  { return (tom_new_S0()) }; type S1 struct { *sl.AbstractStrategyBasic; b  bool ; }; func tom_new_S1(b  bool ) *S1 { tom__strat := &S1{b: b, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S1) getb()  bool  { return tom__strat.b }; func (tom__strat *S1) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S1) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S1) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S1) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S1) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S1) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S1) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_a(any(tom__arg).( Term )) { 






 return tom_make_b();  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S1(t1  bool )  sl.Strategy  { return (tom_new_S1(t1)) }; type S2 struct { *sl.AbstractStrategyBasic; l  int64 ; s  string ; }; func tom_new_S2(l  int64 , s  string ) *S2 { tom__strat := &S2{l: l, s: s, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S2) getl()  int64  { return tom__strat.l }; func (tom__strat *S2) gets()  string  { return tom__strat.s }; func (tom__strat *S2) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S2) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S2) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S2) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S2) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S2) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S2) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 






 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S2(t1  int64 , t2  string )  sl.Strategy  { return (tom_new_S2(t1,t2)) }; type S3 struct { *sl.AbstractStrategyBasic; i  int64 ; }; func tom_new_S3(i  int64 ) *S3 { tom__strat := &S3{i: i, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S3) geti()  int64  { return tom__strat.i }; func (tom__strat *S3) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S3) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S3) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S3) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S3) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S3) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S3) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 






 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S3(t1  int64 )  sl.Strategy  { return (tom_new_S3(t1)) }; type S4 struct { *sl.AbstractStrategyBasic; hashtable  Hashtable ; linkedList  *list.List ; }; func tom_new_S4(hashtable  Hashtable , linkedList  *list.List ) *S4 { tom__strat := &S4{hashtable: hashtable, linkedList: linkedList, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S4) gethashtable()  Hashtable  { return tom__strat.hashtable }; func (tom__strat *S4) getlinkedList()  *list.List  { return tom__strat.linkedList }; func (tom__strat *S4) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S4) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S4) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S4) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S4) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S4) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S4) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { 







 tom__strat.linkedList.PushBack("ok"); return tom_make_g(tom_get_slot_g_s2(any(tom__arg).( Term )),tom_get_slot_g_s1(any(tom__arg).( Term ))); };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S4(t1  Hashtable , t2  *list.List )  sl.Strategy  { return (tom_new_S4(t1,t2)) }; type S5 struct { *sl.AbstractStrategyBasic; }; func tom_new_S5() *S5 { tom__strat := &S5{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S5) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S5) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S5) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S5) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S5) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term1(v) { return any(tom__strat.visit_Term1(any(v).( Term1 ),introspector)).(any);  }; if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S5) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S5) _visit_Term1(arg  Term1 , introspector sl.Introspector)  Term1  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term1 );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term1 );  };  }; func (tom__strat *S5) visit_Term1(tom__arg  Term1 , introspector sl.Introspector)  Term1  { ; { { if tom_is_sort_Term1(tom__arg) { if tom_is_fun_sym_e(any(tom__arg).( Term1 )) { 









 return tom_make_e();  };  };  };  }; return tom__strat._visit_Term1(tom__arg,introspector);  }; func (tom__strat *S5) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) {  return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S5()  sl.Strategy  { return (tom_new_S5()) }; type S6 struct { *sl.AbstractStrategyBasic; }; func tom_new_S6() *S6 { tom__strat := &S6{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S6) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S6) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S6) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S6) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S6) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term1(v) { return any(tom__strat.visit_Term1(any(v).( Term1 ),introspector)).(any);  }; if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S6) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S6) _visit_Term1(arg  Term1 , introspector sl.Introspector)  Term1  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term1 );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term1 );  };  }; func (tom__strat *S6) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 









 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func (tom__strat *S6) visit_Term1(tom__arg  Term1 , introspector sl.Introspector)  Term1  { ; { { if tom_is_sort_Term1(tom__arg) { if tom_is_fun_sym_e(any(tom__arg).( Term1 )) {  return tom_make_e();  };  };  };  }; return tom__strat._visit_Term1(tom__arg,introspector);  }; func tom_make_S6()  sl.Strategy  { return (tom_new_S6()) }; type S7 struct { *sl.AbstractStrategyBasic; }; func tom_new_S7() *S7 { tom__strat := &S7{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_S8(bool0)); return tom__strat }; func (tom__strat *S7) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S7) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S7) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S7) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S7) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S7) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S7) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 





 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S7()  sl.Strategy  { return (tom_new_S7()) }; type S8 struct { *sl.AbstractStrategyBasic; b  bool ; }; func tom_new_S8(b  bool ) *S8 { tom__strat := &S8{b: b, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_S9(3)); return tom__strat }; func (tom__strat *S8) getb()  bool  { return tom__strat.b }; func (tom__strat *S8) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S8) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S8) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S8) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S8) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S8) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S8) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 




 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S8(t1  bool )  sl.Strategy  { return (tom_new_S8(t1)) }; type S9 struct { *sl.AbstractStrategyBasic; i  int64 ; }; func tom_new_S9(i  int64 ) *S9 { tom__strat := &S9{i: i, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S9) geti()  int64  { return tom__strat.i }; func (tom__strat *S9) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S9) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S9) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S9) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S9) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Term(v) { return any(tom__strat.visit_Term(any(v).( Term ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S9) _visit_Term(arg  Term , introspector sl.Introspector)  Term  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Term );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Term );  };  }; func (tom__strat *S9) visit_Term(tom__arg  Term , introspector sl.Introspector)  Term  { ; { { if tom_is_sort_Term(tom__arg) { if tom_is_fun_sym_g(any(tom__arg).( Term )) { var tom___x  Term  = tom_get_slot_g_s1(any(tom__arg).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_g_s2(any(tom__arg).( Term ))) { 





 return tom___x;  };  };  };  };  }; return tom__strat._visit_Term(tom__arg,introspector);  }; func tom_make_S9(t1  int64 )  sl.Strategy  { return (tom_new_S9(t1)) }; type fnc struct { *sl.AbstractStrategyBasic; }; func tom_new_fnc() *fnc { tom__strat := &fnc{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *fnc) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *fnc) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *fnc) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *fnc) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *fnc) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Pf(v) { return any(tom__strat.visit_Pf(any(v).( Pf ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *fnc) _visit_Pf(arg  Pf , introspector sl.Introspector)  Pf  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( Pf );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( Pf );  };  }; func (tom__strat *fnc) visit_Pf(tom__arg  Pf , introspector sl.Introspector)  Pf  { ; { { if tom_is_sort_Pf(tom__arg) { if tom_is_fun_sym_Unary(any(tom__arg).( Pf )) { 





 return tom_cons_list_Variadic(tom_cons_list_Variadic(tom_make_Unary(),tom_empty_list_Variadic()),tom_empty_list_Variadic()); };  };  };  }; return tom__strat._visit_Pf(tom__arg,introspector);  }; func tom_make_fnc()  sl.Strategy  { return (tom_new_fnc()) }; 



func TestFnc(t *testing.T) {
  r, err := tom_make_fnc().VisitLight(tom_make_Unary(), sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  assertEquals(t, r, tom_cons_list_Variadic(tom_cons_list_Variadic(tom_make_Unary(),tom_empty_list_Variadic()),tom_empty_list_Variadic()))
}

  
  
  


  

  













type visitFailure struct{ err error }




func visitLight(s sl.Strategy, subject any) any {
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    panic(visitFailure{err})
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




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Errorf("%s", message)
  }
}




func assertFalse(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Errorf("%s", message)
  }
}




func assertSame(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
