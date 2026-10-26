package sl


















import (
  "fmt"
  "testing"

  . "fixture/testsl"
  "tom/library/sl"
)

  func tom_TestReflective_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_TestReflective_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_TestReflective_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_TestReflective_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_TestReflective_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_TestReflective_is_sort_char(t rune) bool { return true;  }; func tom_TestReflective_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_TestReflective_is_sort_String(t string) bool { return true;  }; func tom_TestReflective_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_TestReflective_is_sort_int(t int64) bool { return true;  }; func tom_TestReflective_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_TestReflective_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_TestReflective_is_fun_sym_Identity(t  sl.Strategy ) bool { return sl.IsIdentity(t);  }; func tom_TestReflective_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_TestReflective_is_fun_sym_Fail(t  sl.Strategy ) bool { return sl.IsFail(t);  }; func tom_TestReflective_make_Fail()  sl.Strategy  { return (sl.NewFail()) }; func tom_TestReflective_is_fun_sym_All(t  sl.Strategy ) bool { return sl.IsAll(t);  }; func tom_TestReflective_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_TestReflective_get_slot_All_s1(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReflective_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_TestReflective_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_TestReflective_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_TestReflective_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_TestReflective_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_TestReflective_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReflective_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReflective_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReflective_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReflective_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_TestReflective_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_TestReflective_is_fun_sym_Sequence(l1) { if tom_TestReflective_is_empty_Sequence_Strategy(tom_TestReflective_get_tail_Sequence_Strategy(l1)) { return tom_TestReflective_cons_list_Sequence(tom_TestReflective_get_head_Sequence_Strategy(l1), l2) } else { return tom_TestReflective_cons_list_Sequence(tom_TestReflective_get_head_Sequence_Strategy(l1), tom_TestReflective_append_list_Sequence(tom_TestReflective_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_TestReflective_cons_list_Sequence(l1, l2) }; }; func tom_TestReflective_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReflective_equal_term_Strategy(begin, end) { return tail } else if tom_TestReflective_equal_term_Strategy(end, tail) && (tom_TestReflective_is_empty_Sequence_Strategy(end) || tom_TestReflective_equal_term_Strategy(end, tom_TestReflective_empty_list_Sequence())) { return begin }; return tom_TestReflective_cons_list_Sequence(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(begin) { return tom_TestReflective_get_head_Sequence_Strategy(begin) }; return begin }(), tom_TestReflective_get_slice_Sequence(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(begin) { return tom_TestReflective_get_tail_Sequence_Strategy(begin) }; return tom_TestReflective_empty_list_Sequence() }(), end, tail)) }; func tom_TestReflective_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_TestReflective_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_TestReflective_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_TestReflective_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReflective_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReflective_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReflective_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReflective_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_TestReflective_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_TestReflective_is_fun_sym_SequenceId(l1) { if tom_TestReflective_is_empty_SequenceId_Strategy(tom_TestReflective_get_tail_SequenceId_Strategy(l1)) { return tom_TestReflective_cons_list_SequenceId(tom_TestReflective_get_head_SequenceId_Strategy(l1), l2) } else { return tom_TestReflective_cons_list_SequenceId(tom_TestReflective_get_head_SequenceId_Strategy(l1), tom_TestReflective_append_list_SequenceId(tom_TestReflective_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_TestReflective_cons_list_SequenceId(l1, l2) }; }; func tom_TestReflective_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReflective_equal_term_Strategy(begin, end) { return tail } else if tom_TestReflective_equal_term_Strategy(end, tail) && (tom_TestReflective_is_empty_SequenceId_Strategy(end) || tom_TestReflective_equal_term_Strategy(end, tom_TestReflective_empty_list_SequenceId())) { return begin }; return tom_TestReflective_cons_list_SequenceId(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_SequenceId(begin) { return tom_TestReflective_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_TestReflective_get_slice_SequenceId(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_SequenceId(begin) { return tom_TestReflective_get_tail_SequenceId_Strategy(begin) }; return tom_TestReflective_empty_list_SequenceId() }(), end, tail)) }; func tom_TestReflective_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_TestReflective_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_TestReflective_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_TestReflective_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReflective_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReflective_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReflective_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReflective_is_empty_Choice_Strategy(l1) { return l2 } else if tom_TestReflective_is_empty_Choice_Strategy(l2) { return l1 } else if tom_TestReflective_is_fun_sym_Choice(l1) { if tom_TestReflective_is_empty_Choice_Strategy(tom_TestReflective_get_tail_Choice_Strategy(l1)) { return tom_TestReflective_cons_list_Choice(tom_TestReflective_get_head_Choice_Strategy(l1), l2) } else { return tom_TestReflective_cons_list_Choice(tom_TestReflective_get_head_Choice_Strategy(l1), tom_TestReflective_append_list_Choice(tom_TestReflective_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_TestReflective_cons_list_Choice(l1, l2) }; }; func tom_TestReflective_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReflective_equal_term_Strategy(begin, end) { return tail } else if tom_TestReflective_equal_term_Strategy(end, tail) && (tom_TestReflective_is_empty_Choice_Strategy(end) || tom_TestReflective_equal_term_Strategy(end, tom_TestReflective_empty_list_Choice())) { return begin }; return tom_TestReflective_cons_list_Choice(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Choice(begin) { return tom_TestReflective_get_head_Choice_Strategy(begin) }; return begin }(), tom_TestReflective_get_slice_Choice(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Choice(begin) { return tom_TestReflective_get_tail_Choice_Strategy(begin) }; return tom_TestReflective_empty_list_Choice() }(), end, tail)) }; func tom_TestReflective_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_TestReflective_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_TestReflective_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_TestReflective_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_TestReflective_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_TestReflective_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_TestReflective_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_TestReflective_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_TestReflective_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_TestReflective_is_fun_sym_ChoiceId(l1) { if tom_TestReflective_is_empty_ChoiceId_Strategy(tom_TestReflective_get_tail_ChoiceId_Strategy(l1)) { return tom_TestReflective_cons_list_ChoiceId(tom_TestReflective_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_TestReflective_cons_list_ChoiceId(tom_TestReflective_get_head_ChoiceId_Strategy(l1), tom_TestReflective_append_list_ChoiceId(tom_TestReflective_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_TestReflective_cons_list_ChoiceId(l1, l2) }; }; func tom_TestReflective_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_TestReflective_equal_term_Strategy(begin, end) { return tail } else if tom_TestReflective_equal_term_Strategy(end, tail) && (tom_TestReflective_is_empty_ChoiceId_Strategy(end) || tom_TestReflective_equal_term_Strategy(end, tom_TestReflective_empty_list_ChoiceId())) { return begin }; return tom_TestReflective_cons_list_ChoiceId(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_ChoiceId(begin) { return tom_TestReflective_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_TestReflective_get_slice_ChoiceId(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_ChoiceId(begin) { return tom_TestReflective_get_tail_ChoiceId_Strategy(begin) }; return tom_TestReflective_empty_list_ChoiceId() }(), end, tail)) }; func tom_TestReflective_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_cons_list_Choice(s,tom_TestReflective_cons_list_Choice(tom_TestReflective_make_Identity(),tom_TestReflective_empty_list_Choice()))) }; func tom_TestReflective_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_Choice(tom_TestReflective_cons_list_Sequence(s,tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_empty_list_Sequence())),tom_TestReflective_cons_list_Choice(tom_TestReflective_make_Identity(),tom_TestReflective_empty_list_Choice())))) }; func tom_TestReflective_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_SequenceId(v,tom_TestReflective_cons_list_SequenceId(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_empty_list_SequenceId())))) }; func tom_TestReflective_make_TopDown(v  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_Sequence(v,tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_All(tom_TestReflective_make_MuVar("_x")),tom_TestReflective_empty_list_Sequence())))) }; func tom_TestReflective_make_BottomUp(v  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_All(tom_TestReflective_make_MuVar("_x")),tom_TestReflective_cons_list_Sequence(v,tom_TestReflective_empty_list_Sequence())))) }; func tom_TestReflective_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_Choice(v,tom_TestReflective_cons_list_Choice(tom_TestReflective_make_One(tom_TestReflective_make_MuVar("_x")),tom_TestReflective_empty_list_Choice())))) }; func tom_TestReflective_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_TestReflective_make_mu(tom_TestReflective_make_MuVar("_x"),tom_TestReflective_cons_list_ChoiceId(v,tom_TestReflective_cons_list_ChoiceId(tom_TestReflective_make_OneId(tom_TestReflective_make_MuVar("_x")),tom_TestReflective_empty_list_ChoiceId())))) }; func tom_TestReflective_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestReflective_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestReflective_make_a()  Term  { return (MakeA()) }; func tom_TestReflective_make_Make_a()  sl.Strategy  { return (NewBuildA()) }; func tom_TestReflective_is_fun_sym_Make_Consl(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*BuildConsL); return ok }();  }; func tom_TestReflective_make_Make_Consl(a0  sl.Strategy , a1  sl.Strategy )  sl.Strategy  { return (NewBuildConsL(a0, a1)) }; func tom_TestReflective_get_slot_Make_Consl_Make_Conslarg0(t  sl.Strategy )  sl.Strategy  { return (t).(*BuildConsL).Arg0;  }; func tom_TestReflective_get_slot_Make_Consl_Make_Conslarg1(t  sl.Strategy )  sl.Strategy  { return (t).(*BuildConsL).Arg1;  }; func tom_TestReflective_is_fun_sym_Make_Emptyl(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*BuildEmptyL); return ok }();  }; func tom_TestReflective_make_Make_Emptyl()  sl.Strategy  { return (NewBuildEmptyL()) }; func tom_TestReflective_is_fun_sym_Make_m(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*BuildM); return ok }();  }; func tom_TestReflective_make_Make_m(a0  int64 )  sl.Strategy  { return (NewBuildM(a0)) }; func tom_TestReflective_get_slot_Make_m_Make_marg0(t  sl.Strategy )  int64  { return (t).(*BuildM).Arg0;  }; 


func TestMatchId(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Identity(); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Identity(any(s).( sl.Strategy )) { 

 return ;  };  };  };  }; 

    fail(t, "Identity should have matched")
  }



func TestMatchIdFail(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Fail(); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Identity(any(s).( sl.Strategy )) { 

 fail(t, "Fail should not match") ;  };  };  };  }; 

  }



func TestMatchS1(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_S1(); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_S1(any(s).( sl.Strategy )) { 

 return ;  };  };  };  }; 

    fail(t, "S1 should have matched")
  }



func TestMatchS1Fail(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Fail(); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_S1(any(s).( sl.Strategy )) { 

 fail(t, "Fail should not match") ;  };  };  };  }; 

  }



func TestMatchSequence1(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Fail(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch5_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch5_2) || tom_TestReflective_equal_term_Strategy(tomMatch5_2, tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Fail(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch5_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch5_2) }; return tomMatch5_2 }()).( sl.Strategy )) { var tomMatch5_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch5_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch5_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch5_3) || tom_TestReflective_equal_term_Strategy(tomMatch5_3, tom_TestReflective_empty_list_Sequence()))  { 

 return ;  };  };  };  };  };  };  };  };  }; 

    fail(t, "match should success")
  }



func TestMatchSequence2(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_S1(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch6_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch6_2) || tom_TestReflective_equal_term_Strategy(tomMatch6_2, tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Fail(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch6_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch6_2) }; return tomMatch6_2 }()).( sl.Strategy )) { var tomMatch6_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch6_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch6_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch6_3) || tom_TestReflective_equal_term_Strategy(tomMatch6_3, tom_TestReflective_empty_list_Sequence()))  { 

 fail(t, "should not match") ;  };  };  };  };  };  };  };  };  }; 

  }



func TestMatchSequence3(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_S1(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch7_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch7_2) || tom_TestReflective_equal_term_Strategy(tomMatch7_2, tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_S1(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch7_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch7_2) }; return tomMatch7_2 }()).( sl.Strategy )) { var tomMatch7_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch7_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch7_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch7_3) || tom_TestReflective_equal_term_Strategy(tomMatch7_3, tom_TestReflective_empty_list_Sequence()))  { 

 return ;  };  };  };  };  };  };  };  };  }; 

    fail(t, "match should success")
  }



func TestMatchSequence4(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Fail(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch8_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch8_2) || tom_TestReflective_equal_term_Strategy(tomMatch8_2, tom_TestReflective_empty_list_Sequence())) ) { if tom_TestReflective_is_fun_sym_S1(any(func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch8_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch8_2) }; return tomMatch8_2 }()).( sl.Strategy )) { var tomMatch8_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch8_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch8_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch8_3) || tom_TestReflective_equal_term_Strategy(tomMatch8_3, tom_TestReflective_empty_list_Sequence()))  { 

 fail(t, "match should not success") ;  };  };  };  };  };  };  };  };  }; 

  }



func TestMatchSequenceVar(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Fail(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { var tom___s1  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }(); var tomMatch9_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch9_2) || tom_TestReflective_equal_term_Strategy(tomMatch9_2, tom_TestReflective_empty_list_Sequence())) ) { var tom___s2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch9_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch9_2) }; return tomMatch9_2 }(); var tomMatch9_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch9_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch9_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch9_3) || tom_TestReflective_equal_term_Strategy(tomMatch9_3, tom_TestReflective_empty_list_Sequence()))  { ; { { if tom_TestReflective_is_sort_Strategy(tom___s1) { if tom_TestReflective_is_fun_sym_Identity(any(tom___s1).( sl.Strategy )) { if tom_TestReflective_is_sort_Strategy(tom___s2) { if tom_TestReflective_is_fun_sym_Fail(any(tom___s2).( sl.Strategy )) { 




            return
          ;  };  };  };  };  };  }; ;  };  };  };  };  };  };  }; 



    fail(t, "match should success")
  }



func TestSequenceVarS1(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_Identity(),tom_TestReflective_cons_list_Sequence(tom_TestReflective_make_S1(),tom_TestReflective_empty_list_Sequence())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_TestReflective_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_TestReflective_equal_term_Strategy(any(s).( sl.Strategy ), tom_TestReflective_empty_list_Sequence())) ) { var tom___s1  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }(); var tomMatch11_2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_TestReflective_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_TestReflective_empty_list_Sequence() }(); if !( (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch11_2) || tom_TestReflective_equal_term_Strategy(tomMatch11_2, tom_TestReflective_empty_list_Sequence())) ) { var tom___s2  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch11_2) { return tom_TestReflective_get_head_Sequence_Strategy(tomMatch11_2) }; return tomMatch11_2 }(); var tomMatch11_3  sl.Strategy  = func()  sl.Strategy  { if tom_TestReflective_is_fun_sym_Sequence(tomMatch11_2) { return tom_TestReflective_get_tail_Sequence_Strategy(tomMatch11_2) }; return tom_TestReflective_empty_list_Sequence() }(); if  (tom_TestReflective_is_empty_Sequence_Strategy(tomMatch11_3) || tom_TestReflective_equal_term_Strategy(tomMatch11_3, tom_TestReflective_empty_list_Sequence()))  { ; { { if tom_TestReflective_is_sort_Strategy(tom___s1) { if tom_TestReflective_is_fun_sym_Identity(any(tom___s1).( sl.Strategy )) { if tom_TestReflective_is_sort_Strategy(tom___s2) { if tom_TestReflective_is_fun_sym_S1(any(tom___s2).( sl.Strategy )) { 



 return ;  };  };  };  };  };  }; ;  };  };  };  };  };  };  }; 



    fail(t, "match should success")
  }



func TestMatchS2(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_S2(0,"msg",tom_TestReflective_make_Identity()); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_S2(any(s).( sl.Strategy )) { 


        if (tom_TestReflective_get_slot_S2_i(any(s).( sl.Strategy )) == 0) {
          
          if(tom_TestReflective_get_slot_S2_str(any(s).( sl.Strategy )) == "msg") {
            return
          }};  };  };  };  }; 



    fail(t, "match should success")
  }



func TestMatchS2in(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_S2(1,"msg",tom_TestReflective_make_Identity()); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_S2(any(s).( sl.Strategy )) { if tom_TestReflective_equal_term_int(1, tom_TestReflective_get_slot_S2_i(any(s).( sl.Strategy ))) { if tom_TestReflective_equal_term_String("msg", tom_TestReflective_get_slot_S2_str(any(s).( sl.Strategy ))) { var tom___s1  sl.Strategy  = tom_TestReflective_get_slot_S2_s(any(s).( sl.Strategy )); ; { { if tom_TestReflective_is_sort_Strategy(tom___s1) { if tom_TestReflective_is_fun_sym_Identity(any(tom___s1).( sl.Strategy )) { 




            return
          ;  };  };  };  }; ;  };  };  };  };  };  }; 



    fail(t, "match should success")
  }



func TestMatchS2Fail(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_S2(1,"msg",tom_TestReflective_make_S1()); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_S2(any(s).( sl.Strategy )) { if tom_TestReflective_equal_term_int(1, tom_TestReflective_get_slot_S2_i(any(s).( sl.Strategy ))) { if tom_TestReflective_equal_term_String("msg", tom_TestReflective_get_slot_S2_str(any(s).( sl.Strategy ))) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_S2_s(any(s).( sl.Strategy ))).( sl.Strategy )) { 


        fail(t, "match should not succeed")
      ;  };  };  };  };  };  };  }; 

  }



func TestMatchAll1(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Identity(); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_All(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 

 fail(t, "no !") ;  };  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { 
 fail(t, "no !") ;  };  };  };  }; 

  }



func TestMatchAll2(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_Identity()); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_All(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 

 fail(t, "no !") ;  };  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { 
 return ;  };  };  };  }; 

    fail(t, "no !")
  }



func TestMatchAll3(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_All(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 

 return ;  };  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { 
 fail(t, "no !") ;  };  };  };  }; 

    fail(t, "no !")
  }



func TestS1Id(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S1(), tom_TestReflective_make_Identity()).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Identity(any(s).( sl.Strategy )) { 
 fail(t, "Id should rewrite to fail") ;  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Fail(any(s).( sl.Strategy )) { 
 return ;  };  };  };  }; 

    fail(t, "should not be here")
  }



func TestS1Fail(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S1(), tom_TestReflective_make_Fail()).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Fail(any(s).( sl.Strategy )) { 
 fail(t, "Fail should rewrite to Identity") ;  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Identity(any(s).( sl.Strategy )) { 
 return ;  };  };  };  }; 

    fail(t, "should not be here")
  }



func TestS3Id(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S3(), tom_TestReflective_make_Identity()).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Identity(any(s).( sl.Strategy )) { 
 return ;  };  };  };  }; 

    fail(t, "should not be here")
  }



func TestS3All(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S3(), tom_TestReflective_make_All(tom_TestReflective_make_Identity())).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { 
 return ;  };  };  };  }; 

    fail(t, "should not be here")
  }



func TestS3AllAllvisit(t *testing.T) {
    if r, err := tom_TestReflective_make_S3().VisitLight(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())), sl.VisitableIntrospector{}); err == nil {
      var s sl.Strategy = r.(sl.Strategy)
      ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { var tomMatch24_1  sl.Strategy  = tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy )); if tom_TestReflective_is_fun_sym_All(any(tomMatch24_1).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(tomMatch24_1)).( sl.Strategy )) { 
 fail(t, "S3 did not rewrite s") ;  };  };  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 
 return ;  };  };  };  };  }; 

    } else {
      fail(t, "should not catch exception")
    }
    fail(t, "should not be here")
  }



func TestS3AllAll(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S3(), tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity()))).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { var tomMatch25_1  sl.Strategy  = tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy )); if tom_TestReflective_is_fun_sym_All(any(tomMatch25_1).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(tomMatch25_1)).( sl.Strategy )) { 
 fail(t, "S3 did not rewrite s") ;  };  };  };  };  }; { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 
 return ;  };  };  };  };  }; 

    fail(t, "should not be here with "+fmt.Sprint(s))
  }



func TestS3Allllll(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_S3(), tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity()))))).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 
 fail(t, "should not be here") ;  };  };  };  };  }; 

  }



func TestAlllllBU(t *testing.T) {
    var s sl.Strategy = visit(t, tom_TestReflective_make_BottomUp(tom_TestReflective_make_S3()), tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity()))))).(sl.Strategy)
    ; { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Identity(any(tom_TestReflective_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 
 return ;  };  };  };  };  }; 

    fail(t, "should not be here with "+fmt.Sprint(s))
  }



func TestCountAll1(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_S2(0,"",tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity()))))
    assertEquals(t, "countAll should return 3 with "+fmt.Sprint(s), 3, countAll(t, s))
  }



func TestCountAll2(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_S2(0,"",tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity()))))
    assertEquals(t,
        "countAll should return 2 with `BottomUp(S3()).visit(`"+fmt.Sprint(s)+")", 2,
        countAll(t, visit(t, tom_TestReflective_make_BottomUp(tom_TestReflective_make_S3()), s).(sl.Strategy)))
  }



func TestStratNoStrat(t *testing.T) {
    assertEquals(t, "S3 on something that is not a strat fails back to Id",
        tom_TestReflective_make_a(),visit(t, tom_TestReflective_make_S3(), tom_TestReflective_make_a()))
  }



func TestStratNoStratFail(t *testing.T) {
    if _, err := tom_TestReflective_make_S4(tom_TestReflective_make_S1(),0,tom_TestReflective_make_S1()).Visit(tom_TestReflective_make_a(), sl.VisitableIntrospector{}); err == nil {
      fail(t, "This application should fail")
    } else {
      return
    }
    fail(t, "The Exception should have been catched")
  }



func TestCountAll3(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_S4(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())),0,tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())))))
    assertEquals(t, "countAll should return 6 with `"+fmt.Sprint(s), 6, countAll(t, s))
  }



func TestCountAll4(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_All(tom_TestReflective_make_S4(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())),0,tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_All(tom_TestReflective_make_Identity())))))
    assertEquals(t,
        "countAll should return 3 with `BottomUp(S3()).visit("+fmt.Sprint(s)+")", 3,
        countAll(t, visit(t, tom_TestReflective_make_BottomUp(tom_TestReflective_make_S3()), s).(sl.Strategy)))
  }



func TestCountAll5(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_S4(tom_TestReflective_make_Identity(),0,tom_TestReflective_make_Fail())
    assertEquals(t,
        "countAll should return 2 with `BottomUp(S5()).visit(`"+fmt.Sprint(s)+")", 2,
        countAll(t, visit(t, tom_TestReflective_make_BottomUp(tom_TestReflective_make_S5()), s).(sl.Strategy)))
  }



func TestMake(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Make_Consl(tom_TestReflective_make_Make_a(),tom_TestReflective_make_Make_Emptyl()); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Make_Consl(any(s).( sl.Strategy )) { if tom_TestReflective_is_fun_sym_Make_Emptyl(any(tom_TestReflective_get_slot_Make_Consl_Make_Conslarg1(any(s).( sl.Strategy ))).( sl.Strategy )) { 

 return ;  };  };  };  };  }; 

    fail(t, "match should success")
  }



func TestMakeWithBuiltin(t *testing.T) {
    var s sl.Strategy = tom_TestReflective_make_Make_m(1); { { if tom_TestReflective_is_sort_Strategy(s) { if tom_TestReflective_is_fun_sym_Make_m(any(s).( sl.Strategy )) { 

 return ;  };  };  };  }; 
  
    fail(t, "match should success")
  }

  type S1 struct { *sl.AbstractStrategyBasic; }; func tom_new_S1() *S1 { tom__strat := &S1{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Identity()); return tom__strat }; func (tom__strat *S1) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S1) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S1) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S1) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S1) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S1) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S1) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 

 return tom_TestReflective_make_Fail(); ;  };  };  }; { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 
 return tom_TestReflective_make_Identity(); ;  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_is_fun_sym_S1(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*S1); return ok }();  }; func tom_TestReflective_make_S1()  sl.Strategy  { return (tom_new_S1()) }; type S2 struct { *sl.AbstractStrategyBasic; i  int64 ; str  string ; s  sl.Strategy ; }; func tom_new_S2(i  int64 , str  string , s  sl.Strategy ) *S2 { tom__strat := &S2{i: i, str: str, s: s, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Identity()); return tom__strat }; func (tom__strat *S2) geti()  int64  { return tom__strat.i }; func (tom__strat *S2) getstr()  string  { return tom__strat.str }; func (tom__strat *S2) gets()  sl.Strategy  { return tom__strat.s }; func (tom__strat *S2) ChildCount() int { return 2 }; func (tom__strat *S2) ChildAt(i int) sl.Strategy { switch i { case 0: return tom__strat.AbstractStrategyBasic.ChildAt(0); case 1: return tom__strat.s; }; panic("S2: no such child") }; func (tom__strat *S2) SetChildAt(i int, child sl.Strategy) { switch i { case 0: tom__strat.AbstractStrategyBasic.SetChildAt(0, child); return; case 1: tom__strat.s = child; return; }; panic("S2: no such child") }; func (tom__strat *S2) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S2) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S2) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S2) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S2) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S2) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S2) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 





 return tom_TestReflective_make_Fail(); ;  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_is_fun_sym_S2(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*S2); return ok }();  }; func tom_TestReflective_make_S2(t1  int64 , t2  string , t3  sl.Strategy )  sl.Strategy  { return (tom_new_S2(t1,t2,t3)) }; func tom_TestReflective_get_slot_S2_i(t  sl.Strategy )  int64  { return (t).(*S2).geti();  }; func tom_TestReflective_get_slot_S2_str(t  sl.Strategy )  string  { return (t).(*S2).getstr();  }; func tom_TestReflective_get_slot_S2_s(t  sl.Strategy )  sl.Strategy  { return (t).(*S2).gets();  }; type S3 struct { *sl.AbstractStrategyBasic; }; func tom_new_S3() *S3 { tom__strat := &S3{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Identity()); return tom__strat }; func (tom__strat *S3) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S3) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S3) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S3) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S3) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S3) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S3) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_All(any(tom__arg).( sl.Strategy )) { var tomMatch32_1  sl.Strategy  = tom_TestReflective_get_slot_All_s1(any(tom__arg).( sl.Strategy )); if tom_TestReflective_is_fun_sym_All(any(tomMatch32_1).( sl.Strategy )) { 





 return tom_TestReflective_make_All(tom_TestReflective_get_slot_All_s1(tomMatch32_1)); ;  };  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_make_S3()  sl.Strategy  { return (tom_new_S3()) }; type S4 struct { *sl.AbstractStrategyBasic; s1  sl.Strategy ; i  int64 ; s2  sl.Strategy ; }; func tom_new_S4(s1  sl.Strategy , i  int64 , s2  sl.Strategy ) *S4 { tom__strat := &S4{s1: s1, i: i, s2: s2, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Fail()); return tom__strat }; func (tom__strat *S4) gets1()  sl.Strategy  { return tom__strat.s1 }; func (tom__strat *S4) geti()  int64  { return tom__strat.i }; func (tom__strat *S4) gets2()  sl.Strategy  { return tom__strat.s2 }; func (tom__strat *S4) ChildCount() int { return 3 }; func (tom__strat *S4) ChildAt(i int) sl.Strategy { switch i { case 0: return tom__strat.AbstractStrategyBasic.ChildAt(0); case 1: return tom__strat.s1; case 2: return tom__strat.s2; }; panic("S4: no such child") }; func (tom__strat *S4) SetChildAt(i int, child sl.Strategy) { switch i { case 0: tom__strat.AbstractStrategyBasic.SetChildAt(0, child); return; case 1: tom__strat.s1 = child; return; case 2: tom__strat.s2 = child; return; }; panic("S4: no such child") }; func (tom__strat *S4) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S4) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S4) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S4) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S4) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S4) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S4) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 





 return tom__strat.s1; ;  };  };  }; { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 
 return tom_TestReflective_make_All(tom__strat.s2); ;  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_make_S4(t1  sl.Strategy , t2  int64 , t3  sl.Strategy )  sl.Strategy  { return (tom_new_S4(t1,t2,t3)) }; type S5 struct { *sl.AbstractStrategyBasic; }; func tom_new_S5() *S5 { tom__strat := &S5{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Identity()); return tom__strat }; func (tom__strat *S5) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S5) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S5) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S5) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S5) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S5) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S5) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 





 return tom_TestReflective_make_All(tom_TestReflective_make_Identity()); ;  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_make_S5()  sl.Strategy  { return (tom_new_S5()) }; 



  
  type Counter struct{ count int }
  func tom_TestReflective_is_sort_Counter(t any) bool { return func() bool { _, ok := (t).(*Counter); return ok }();  }; 



  
  
  func countAll(t *testing.T, s sl.Strategy) int {
    var c *Counter = &Counter{}
    visit(t, tom_TestReflective_make_TopDown(tom_TestReflective_make_incAll(c)), s)
    return c.count
  }

  type incAll struct { *sl.AbstractStrategyBasic; c  *Counter ; }; func tom_new_incAll(c  *Counter ) *incAll { tom__strat := &incAll{c: c, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_TestReflective_make_Identity()); return tom__strat }; func (tom__strat *incAll) getc()  *Counter  { return tom__strat.c }; func (tom__strat *incAll) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *incAll) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *incAll) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *incAll) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *incAll) visitLight(v any, introspector sl.Introspector) any { if tom_TestReflective_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *incAll) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *incAll) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_TestReflective_is_sort_Strategy(tom__arg) { if tom_TestReflective_is_fun_sym_All(any(tom__arg).( sl.Strategy )) { 



 tom__strat.c.count++ ;  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_TestReflective_make_incAll(t1  *Counter )  sl.Strategy  { return (tom_new_incAll(t1)) }; 




func visit(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.Visit(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}




func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
