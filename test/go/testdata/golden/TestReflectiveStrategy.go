















package testreflectivestrategy



import (
  "testing"

  "tom/library/sl"
)

  func tom_equal_term_Strategy(t1  sl.Strategy , t2  sl.Strategy ) bool { return t1 == t2;  }; func tom_is_sort_Strategy(t any) bool { return sl.IsStrategy(t);  }; func tom_equal_term_Position(t1  *sl.Position , t2  *sl.Position ) bool { return (t1).Equals(t2);  }; func tom_is_sort_Position(t any) bool { return sl.IsPosition(t);  }; func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_make_mu(name  sl.Strategy , v  sl.Strategy )  sl.Strategy  { return (sl.NewMu((name).(*sl.MuVar), v)) }; func tom_make_MuVar(name  string )  sl.Strategy  { return (sl.NewMuVar(name)) }; func tom_is_fun_sym_Identity(t  sl.Strategy ) bool { return sl.IsIdentity(t);  }; func tom_make_Identity()  sl.Strategy  { return (sl.NewIdentity()) }; func tom_is_fun_sym_Fail(t  sl.Strategy ) bool { return sl.IsFail(t);  }; func tom_make_Fail()  sl.Strategy  { return (sl.NewFail()) }; func tom_is_fun_sym_All(t  sl.Strategy ) bool { return sl.IsAll(t);  }; func tom_make_All(v  sl.Strategy )  sl.Strategy  { return (sl.NewAll(v)) }; func tom_get_slot_All_s1(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_make_One(v  sl.Strategy )  sl.Strategy  { return (sl.NewOne(v)) }; func tom_make_OneId(v  sl.Strategy )  sl.Strategy  { return (sl.NewOneId(v)) }; func tom_is_fun_sym_Sequence(t  sl.Strategy ) bool { return sl.IsSequence(t);  }; func tom_empty_list_Sequence()  sl.Strategy  { return (nil) }; func tom_cons_list_Sequence(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequence(head, tail)) }; func tom_get_head_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Sequence_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Sequence_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Sequence(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Sequence_Strategy(l1) { return l2 } else if tom_is_empty_Sequence_Strategy(l2) { return l1 } else if tom_is_fun_sym_Sequence(l1) { if tom_is_empty_Sequence_Strategy(tom_get_tail_Sequence_Strategy(l1)) { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), l2) } else { return tom_cons_list_Sequence(tom_get_head_Sequence_Strategy(l1), tom_append_list_Sequence(tom_get_tail_Sequence_Strategy(l1), l2)) } } else { return tom_cons_list_Sequence(l1, l2) }; }; func tom_get_slice_Sequence(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Sequence_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Sequence())) { return begin }; return tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_head_Sequence_Strategy(begin) }; return begin }(), tom_get_slice_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(begin) { return tom_get_tail_Sequence_Strategy(begin) }; return tom_empty_list_Sequence() }(), end, tail)) }; func tom_is_fun_sym_SequenceId(t  sl.Strategy ) bool { return sl.IsSequenceId(t);  }; func tom_empty_list_SequenceId()  sl.Strategy  { return (nil) }; func tom_cons_list_SequenceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewSequenceId(head, tail)) }; func tom_get_head_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_SequenceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_SequenceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_SequenceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_SequenceId_Strategy(l1) { return l2 } else if tom_is_empty_SequenceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_SequenceId(l1) { if tom_is_empty_SequenceId_Strategy(tom_get_tail_SequenceId_Strategy(l1)) { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), l2) } else { return tom_cons_list_SequenceId(tom_get_head_SequenceId_Strategy(l1), tom_append_list_SequenceId(tom_get_tail_SequenceId_Strategy(l1), l2)) } } else { return tom_cons_list_SequenceId(l1, l2) }; }; func tom_get_slice_SequenceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_SequenceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_SequenceId())) { return begin }; return tom_cons_list_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_head_SequenceId_Strategy(begin) }; return begin }(), tom_get_slice_SequenceId(func()  sl.Strategy  { if tom_is_fun_sym_SequenceId(begin) { return tom_get_tail_SequenceId_Strategy(begin) }; return tom_empty_list_SequenceId() }(), end, tail)) }; func tom_is_fun_sym_Choice(t  sl.Strategy ) bool { return sl.IsChoice(t);  }; func tom_empty_list_Choice()  sl.Strategy  { return (nil) }; func tom_cons_list_Choice(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoice(head, tail)) }; func tom_get_head_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_Choice_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_Choice_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_Choice(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_Choice_Strategy(l1) { return l2 } else if tom_is_empty_Choice_Strategy(l2) { return l1 } else if tom_is_fun_sym_Choice(l1) { if tom_is_empty_Choice_Strategy(tom_get_tail_Choice_Strategy(l1)) { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), l2) } else { return tom_cons_list_Choice(tom_get_head_Choice_Strategy(l1), tom_append_list_Choice(tom_get_tail_Choice_Strategy(l1), l2)) } } else { return tom_cons_list_Choice(l1, l2) }; }; func tom_get_slice_Choice(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_Choice_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_Choice())) { return begin }; return tom_cons_list_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_head_Choice_Strategy(begin) }; return begin }(), tom_get_slice_Choice(func()  sl.Strategy  { if tom_is_fun_sym_Choice(begin) { return tom_get_tail_Choice_Strategy(begin) }; return tom_empty_list_Choice() }(), end, tail)) }; func tom_is_fun_sym_ChoiceId(t  sl.Strategy ) bool { return sl.IsChoiceId(t);  }; func tom_empty_list_ChoiceId()  sl.Strategy  { return (nil) }; func tom_cons_list_ChoiceId(head  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { return (sl.NewChoiceId(head, tail)) }; func tom_get_head_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(0);  }; func tom_get_tail_ChoiceId_Strategy(t  sl.Strategy )  sl.Strategy  { return (t).ChildAt(1);  }; func tom_is_empty_ChoiceId_Strategy(s  sl.Strategy ) bool { return s == nil;  }; func tom_append_list_ChoiceId(l1  sl.Strategy , l2  sl.Strategy )  sl.Strategy  { if tom_is_empty_ChoiceId_Strategy(l1) { return l2 } else if tom_is_empty_ChoiceId_Strategy(l2) { return l1 } else if tom_is_fun_sym_ChoiceId(l1) { if tom_is_empty_ChoiceId_Strategy(tom_get_tail_ChoiceId_Strategy(l1)) { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), l2) } else { return tom_cons_list_ChoiceId(tom_get_head_ChoiceId_Strategy(l1), tom_append_list_ChoiceId(tom_get_tail_ChoiceId_Strategy(l1), l2)) } } else { return tom_cons_list_ChoiceId(l1, l2) }; }; func tom_get_slice_ChoiceId(begin  sl.Strategy , end  sl.Strategy , tail  sl.Strategy )  sl.Strategy  { if tom_equal_term_Strategy(begin, end) { return tail } else if tom_equal_term_Strategy(end, tail) && (tom_is_empty_ChoiceId_Strategy(end) || tom_equal_term_Strategy(end, tom_empty_list_ChoiceId())) { return begin }; return tom_cons_list_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_head_ChoiceId_Strategy(begin) }; return begin }(), tom_get_slice_ChoiceId(func()  sl.Strategy  { if tom_is_fun_sym_ChoiceId(begin) { return tom_get_tail_ChoiceId_Strategy(begin) }; return tom_empty_list_ChoiceId() }(), end, tail)) }; func tom_make_Try(s  sl.Strategy )  sl.Strategy  { return (tom_cons_list_Choice(s,tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice()))) }; func tom_make_Repeat(s  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(tom_cons_list_Sequence(s,tom_cons_list_Sequence(tom_make_MuVar("_x"),tom_empty_list_Sequence())),tom_cons_list_Choice(tom_make_Identity(),tom_empty_list_Choice())))) }; func tom_make_RepeatId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_SequenceId(v,tom_cons_list_SequenceId(tom_make_MuVar("_x"),tom_empty_list_SequenceId())))) }; func tom_make_TopDown(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence())))) }; func tom_make_BottomUp(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_cons_list_Sequence(v,tom_empty_list_Sequence())))) }; func tom_make_OnceTopDown(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_Choice(v,tom_cons_list_Choice(tom_make_One(tom_make_MuVar("_x")),tom_empty_list_Choice())))) }; func tom_make_OnceTopDownId(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_cons_list_ChoiceId(v,tom_cons_list_ChoiceId(tom_make_OneId(tom_make_MuVar("_x")),tom_empty_list_ChoiceId())))) }; func tom_make_TopDownCollect(v  sl.Strategy )  sl.Strategy  { return (tom_make_mu(tom_make_MuVar("_x"),tom_make_Try(tom_cons_list_Sequence(v,tom_cons_list_Sequence(tom_make_All(tom_make_MuVar("_x")),tom_empty_list_Sequence()))))) }; 

  
func setUp() {
  }

  





func tearDown() {
  }



func Test1(t *testing.T) {
    assertTrue(t, "matchIdentity should return true with `Identity()", matchIdentity(tom_make_Identity()))
  }



func Test2(t *testing.T) {
    assertFalse(t, "matchIdentity should return false with `Fail()", matchIdentity(tom_make_Fail()))
  }



func Test3(t *testing.T) {
    assertTrue(t, "matchS1 should return true with `S1()", matchS1(tom_make_S1()))
  }



func Test4(t *testing.T) {
    assertFalse(t, "matchS1 should return false with `Identity()", matchS1(tom_make_Identity()))
  }



func Test5(t *testing.T) {
    assertTrue(t, "matchSequence1 should return true with `Sequence(Identity(), Fail())", matchSequence1(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_Fail(),tom_empty_list_Sequence()))))
  }



func Test6(t *testing.T) {
    assertFalse(t, "matchSequence1 should return false with `Sequence(Identity(), S1())", matchSequence1(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_S1(),tom_empty_list_Sequence()))))
  }



func Test7(t *testing.T) {
    assertTrue(t, "matchSequence2 should return true with `Sequence(Identity(), S1())", matchSequence2(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_S1(),tom_empty_list_Sequence()))))
  }



func Test8(t *testing.T) {
    assertFalse(t, "matchSequence2 should return false with `Sequence(Identity(), Fail())", matchSequence2(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_Fail(),tom_empty_list_Sequence()))))
  }



func Test9(t *testing.T) {
    assertTrue(t, "matchSequence3 should return true with `Sequence(Identity(), Fail())", matchSequence3(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_Fail(),tom_empty_list_Sequence()))))
  }



func Test10(t *testing.T) {
    assertFalse(t, "matchSequence3 should return false with `Sequence(Identity(), S1())", matchSequence3(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_S1(),tom_empty_list_Sequence()))))
  }



func Test11(t *testing.T) {
    assertTrue(t, "matchSequence4 should return true with `Sequence(Identity(), S1())", matchSequence4(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_S1(),tom_empty_list_Sequence()))))
  }



func Test12(t *testing.T) {
    assertFalse(t, "matchSequence4 should return false with `Sequence(Identity(), Fail())", matchSequence4(tom_cons_list_Sequence(tom_make_Identity(),tom_cons_list_Sequence(tom_make_Fail(),tom_empty_list_Sequence()))))
  }



func Test13(t *testing.T) {
    assertEquals(t, "matchS2 should return 1 with `S2(0, \"msg\", Identity())", 1, matchS2(tom_make_S2(0,"msg",tom_make_Identity())))
  }



func Test14(t *testing.T) {
    assertEquals(t, "matchS2 should return 2 with `S2(1, \"msg\", Identity())", 2, matchS2(tom_make_S2(1,"msg",tom_make_Identity())))
  }



func Test15(t *testing.T) {
    assertEquals(t, "matchS2 should return 3 with `S2(1, \"\", Identity())", 3, matchS2(tom_make_S2(1,"",tom_make_Identity())))
  }



func Test16(t *testing.T) {
    assertEquals(t, "matchS2 should return 4 with `S2(1, \"\", S1())", 4, matchS2(tom_make_S2(1,"",tom_make_S1())))
  }



func Test17(t *testing.T) {
    assertEquals(t, "matchS2 should return 0 with `S2(1, \"\", Fail())", 0, matchS2(tom_make_S2(1,"",tom_make_Fail())))
  }



func Test18(t *testing.T) {
    assertEquals(t, "matchAll should return 0 with `Identity()", 0, matchAll(tom_make_Identity()))
  }



func Test19(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `All(Identity())", 1, matchAll(tom_make_All(tom_make_Identity())))
  }



func Test20(t *testing.T) {
    assertEquals(t, "matchAll should return 2 with `All(All(Identity()))", 2, matchAll(tom_make_All(tom_make_All(tom_make_Identity()))))
  }



func Test21(t *testing.T) {
    assertTrue(t, "matchIdentity should return true with `S3().visitLight(`Identity())", matchIdentity(visitLight(t, tom_make_S3(), tom_make_Identity()).(sl.Strategy)))
  }



func Test22(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `S3().visitLight(`All(Identity())", 1, matchAll(visitLight(t, tom_make_S3(), tom_make_All(tom_make_Identity())).(sl.Strategy)))
  }



func Test23(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `S3().visitLight(`All(All(Identity())))", 1, matchAll(visitLight(t, tom_make_S3(), tom_make_All(tom_make_All(tom_make_Identity()))).(sl.Strategy)))
  }



func Test24(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `TopDown(S3()).visitLight(`All(All(All(All(Identity())))))", 2, matchAll(visitLight(t, tom_make_TopDown(tom_make_S3()), tom_make_All(tom_make_All(tom_make_All(tom_make_All(tom_make_Identity()))))).(sl.Strategy)))
  }



func Test25(t *testing.T) {
    assertEquals(t, "matchAll should return 1 with `BottomUp(S3()).visitLight(`All(All(All(All(Identity())))))", 1, matchAll(visitLight(t, tom_make_BottomUp(tom_make_S3()), tom_make_All(tom_make_All(tom_make_All(tom_make_All(tom_make_Identity()))))).(sl.Strategy)))
  }



func Test26(t *testing.T) {
    assertEquals(t, "countAll should return 3 with `All(S2(0, \"\", All(All(Identity()))))", 3, countAll(t, tom_make_All(tom_make_S2(0,"",tom_make_All(tom_make_All(tom_make_Identity()))))))
  }



func Test27(t *testing.T) {
    assertEquals(t, "countAll should return 2 with `BottomUp(S3()).visitLight(`All(S2(0, \"\", All(All(Identity())))))", 2, countAll(t, visitLight(t, tom_make_BottomUp(tom_make_S3()), tom_make_All(tom_make_S2(0,"",tom_make_All(tom_make_All(tom_make_Identity()))))).(sl.Strategy)))
  }



func Test28(t *testing.T) {
    assertEquals(t, "countAll should return 6 with `All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 6, countAll(t, tom_make_All(tom_make_S4(tom_make_All(tom_make_All(tom_make_Identity())),0,tom_make_All(tom_make_All(tom_make_All(tom_make_Identity())))))))
  }



func Test29(t *testing.T) {
    assertEquals(t, "countAll should return 3 with `BottomUp(S3()).visitLight(`All(S4(All(All(Identity())), 0, All(All(All(Identity()))))))", 3, countAll(t, visitLight(t, tom_make_BottomUp(tom_make_S3()), tom_make_All(tom_make_S4(tom_make_All(tom_make_All(tom_make_Identity())),0,tom_make_All(tom_make_All(tom_make_All(tom_make_Identity())))))).(sl.Strategy)))
  }



func Test30(t *testing.T) {
    assertEquals(t, "countAll should return 2 with `BottomUp(S5()).visitLight(`S4(Identity(), 0, Fail()))", 2, countAll(t, visitLight(t, tom_make_BottomUp(tom_make_S5()), tom_make_S4(tom_make_Identity(),0,tom_make_Fail())).(sl.Strategy)))
  }
  

func Test31(t *testing.T) {
    c := &Counter{}
    var s sl.Strategy = tom_make_mu(tom_make_MuVar("x"),tom_make_TopDownCollect(tom_make_CollectExceptFirst(tom_make_MuVar("x"),c)))
    visitLight(t, s, tom_cons_list_Sequence(tom_make_S1(),tom_cons_list_Sequence(tom_cons_list_Sequence(tom_cons_list_Choice(tom_make_S1(),tom_cons_list_Choice(tom_make_S1(),tom_empty_list_Choice())),tom_cons_list_Sequence(tom_cons_list_Choice(tom_make_S1(),tom_cons_list_Choice(tom_make_S1(),tom_empty_list_Choice())),tom_empty_list_Sequence())),tom_empty_list_Sequence())))
    assertEquals(t, "collectExceptFirst should return 5 when applied on `Sequence(S1(),Sequence(Choice(S1(),S1()),Choice(S1(),S1())))", 5, c.count)
  }

  
  func matchIdentity(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_Identity(any(s).( sl.Strategy )) { 
 return true  };  };  };  }; 

    return false
  }

  
  func matchS1(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_S1(any(s).( sl.Strategy )) { 
 return true  };  };  };  }; 

    return false
  }

  
  func matchSequence1(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_equal_term_Strategy(any(s).( sl.Strategy ), tom_empty_list_Sequence())) ) { if tom_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch3_2  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_empty_list_Sequence() }(); _ = tomMatch3_2; if !( (tom_is_empty_Sequence_Strategy(tomMatch3_2) || tom_equal_term_Strategy(tomMatch3_2, tom_empty_list_Sequence())) ) { if tom_is_fun_sym_Fail(any(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch3_2) { return tom_get_head_Sequence_Strategy(tomMatch3_2) }; return tomMatch3_2 }()).( sl.Strategy )) { var tomMatch3_3  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch3_2) { return tom_get_tail_Sequence_Strategy(tomMatch3_2) }; return tom_empty_list_Sequence() }(); _ = tomMatch3_3; if  (tom_is_empty_Sequence_Strategy(tomMatch3_3) || tom_equal_term_Strategy(tomMatch3_3, tom_empty_list_Sequence()))  { 
 return true  };  };  };  };  };  };  };  };  }; 

    return false
  }

  
  func matchSequence2(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_equal_term_Strategy(any(s).( sl.Strategy ), tom_empty_list_Sequence())) ) { if tom_is_fun_sym_Identity(any(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }()).( sl.Strategy )) { var tomMatch4_2  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_empty_list_Sequence() }(); _ = tomMatch4_2; if !( (tom_is_empty_Sequence_Strategy(tomMatch4_2) || tom_equal_term_Strategy(tomMatch4_2, tom_empty_list_Sequence())) ) { if tom_is_fun_sym_S1(any(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch4_2) { return tom_get_head_Sequence_Strategy(tomMatch4_2) }; return tomMatch4_2 }()).( sl.Strategy )) { var tomMatch4_3  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch4_2) { return tom_get_tail_Sequence_Strategy(tomMatch4_2) }; return tom_empty_list_Sequence() }(); _ = tomMatch4_3; if  (tom_is_empty_Sequence_Strategy(tomMatch4_3) || tom_equal_term_Strategy(tomMatch4_3, tom_empty_list_Sequence()))  { 
 return true  };  };  };  };  };  };  };  };  }; 

    return false
  }

  
  func matchSequence3(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_equal_term_Strategy(any(s).( sl.Strategy ), tom_empty_list_Sequence())) ) { var tomMatch5_2  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_empty_list_Sequence() }(); _ = tomMatch5_2; if !( (tom_is_empty_Sequence_Strategy(tomMatch5_2) || tom_equal_term_Strategy(tomMatch5_2, tom_empty_list_Sequence())) ) { var tomMatch5_3  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch5_2) { return tom_get_tail_Sequence_Strategy(tomMatch5_2) }; return tom_empty_list_Sequence() }(); _ = tomMatch5_3; if  (tom_is_empty_Sequence_Strategy(tomMatch5_3) || tom_equal_term_Strategy(tomMatch5_3, tom_empty_list_Sequence()))  { 
 return matchSequence1(tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }(),tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch5_2) { return tom_get_head_Sequence_Strategy(tomMatch5_2) }; return tomMatch5_2 }(),tom_empty_list_Sequence())))  };  };  };  };  };  };  }; 

    return false
  }

  
  func matchSequence4(s sl.Strategy) bool {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { if !( (tom_is_empty_Sequence_Strategy(any(s).( sl.Strategy )) || tom_equal_term_Strategy(any(s).( sl.Strategy ), tom_empty_list_Sequence())) ) { var tomMatch6_2  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_tail_Sequence_Strategy(any(s).( sl.Strategy )) }; return tom_empty_list_Sequence() }(); _ = tomMatch6_2; if !( (tom_is_empty_Sequence_Strategy(tomMatch6_2) || tom_equal_term_Strategy(tomMatch6_2, tom_empty_list_Sequence())) ) { var tomMatch6_3  sl.Strategy  = func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch6_2) { return tom_get_tail_Sequence_Strategy(tomMatch6_2) }; return tom_empty_list_Sequence() }(); _ = tomMatch6_3; if  (tom_is_empty_Sequence_Strategy(tomMatch6_3) || tom_equal_term_Strategy(tomMatch6_3, tom_empty_list_Sequence()))  { 
 return matchSequence2(tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(s).( sl.Strategy )) { return tom_get_head_Sequence_Strategy(any(s).( sl.Strategy )) }; return any(s).( sl.Strategy ) }(),tom_cons_list_Sequence(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(tomMatch6_2) { return tom_get_head_Sequence_Strategy(tomMatch6_2) }; return tomMatch6_2 }(),tom_empty_list_Sequence())))  };  };  };  };  };  };  }; 

    return false
  }

  
  func matchS2(s sl.Strategy) int {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_S2(any(s).( sl.Strategy )) { var tom___s1  sl.Strategy  = tom_get_slot_S2_s(any(s).( sl.Strategy )); _ = tom___s1; 

        if tom_get_slot_S2_i(any(s).( sl.Strategy )) == 0 { 
          return 1
         }  else if tom_get_slot_S2_str(any(s).( sl.Strategy )) == "msg" { 
          return 2
         }  else if matchIdentity(tom___s1) { 
          return 3
         }  else if matchS1(tom___s1) { 
          return 4
         };  };  };  };  }; 


    return 0
  }

  
  func matchAll(s sl.Strategy) int {
    ; { { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_All(any(s).( sl.Strategy )) { if tom_is_fun_sym_All(any(tom_get_slot_All_s1(any(s).( sl.Strategy ))).( sl.Strategy )) { 
 return 2  };  };  };  }; { if tom_is_sort_Strategy(s) { if tom_is_fun_sym_All(any(s).( sl.Strategy )) { 
 return 1  };  };  };  }; 

    return 0
  }


  type S1 struct { *sl.AbstractStrategyBasic; }; func tom_new_S1() *S1 { tom__strat := &S1{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S1) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S1) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S1) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S1) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S1) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S1) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S1) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 

 return tom_make_Fail();  };  };  }; { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 
 return tom_make_Identity();  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_is_fun_sym_S1(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*S1); return ok }();  }; func tom_make_S1()  sl.Strategy  { return (tom_new_S1()) }; type S2 struct { *sl.AbstractStrategyBasic; i  int64 ; str  string ; s  sl.Strategy ; }; func tom_new_S2(i  int64 , str  string , s  sl.Strategy ) *S2 { tom__strat := &S2{i: i, str: str, s: s, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S2) geti()  int64  { return tom__strat.i }; func (tom__strat *S2) getstr()  string  { return tom__strat.str }; func (tom__strat *S2) gets()  sl.Strategy  { return tom__strat.s }; func (tom__strat *S2) ChildCount() int { return 2 }; func (tom__strat *S2) ChildAt(i int) sl.Strategy { switch i { case 0: return tom__strat.AbstractStrategyBasic.ChildAt(0); case 1: return tom__strat.s; }; panic("S2: no such child") }; func (tom__strat *S2) SetChildAt(i int, child sl.Strategy) { switch i { case 0: tom__strat.AbstractStrategyBasic.SetChildAt(0, child); return; case 1: tom__strat.s = child; return; }; panic("S2: no such child") }; func (tom__strat *S2) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S2) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S2) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S2) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S2) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S2) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S2) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 





 return tom_make_Fail();  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_is_fun_sym_S2(t  sl.Strategy ) bool { return func() bool { _, ok := (t).(*S2); return ok }();  }; func tom_make_S2(t1  int64 , t2  string , t3  sl.Strategy )  sl.Strategy  { return (tom_new_S2(t1,t2,t3)) }; func tom_get_slot_S2_i(t  sl.Strategy )  int64  { return (t).(*S2).geti();  }; func tom_get_slot_S2_str(t  sl.Strategy )  string  { return (t).(*S2).getstr();  }; func tom_get_slot_S2_s(t  sl.Strategy )  sl.Strategy  { return (t).(*S2).gets();  }; type S3 struct { *sl.AbstractStrategyBasic; }; func tom_new_S3() *S3 { tom__strat := &S3{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S3) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S3) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S3) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S3) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S3) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S3) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S3) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_All(any(tom__arg).( sl.Strategy )) { var tomMatch11_1  sl.Strategy  = tom_get_slot_All_s1(any(tom__arg).( sl.Strategy )); _ = tomMatch11_1; if tom_is_fun_sym_All(any(tomMatch11_1).( sl.Strategy )) { 





 return tom_make_All(tom_get_slot_All_s1(tomMatch11_1));  };  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_make_S3()  sl.Strategy  { return (tom_new_S3()) }; type S4 struct { *sl.AbstractStrategyBasic; s1  sl.Strategy ; i  int64 ; s2  sl.Strategy ; }; func tom_new_S4(s1  sl.Strategy , i  int64 , s2  sl.Strategy ) *S4 { tom__strat := &S4{s1: s1, i: i, s2: s2, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Fail()); return tom__strat }; func (tom__strat *S4) gets1()  sl.Strategy  { return tom__strat.s1 }; func (tom__strat *S4) geti()  int64  { return tom__strat.i }; func (tom__strat *S4) gets2()  sl.Strategy  { return tom__strat.s2 }; func (tom__strat *S4) ChildCount() int { return 3 }; func (tom__strat *S4) ChildAt(i int) sl.Strategy { switch i { case 0: return tom__strat.AbstractStrategyBasic.ChildAt(0); case 1: return tom__strat.s1; case 2: return tom__strat.s2; }; panic("S4: no such child") }; func (tom__strat *S4) SetChildAt(i int, child sl.Strategy) { switch i { case 0: tom__strat.AbstractStrategyBasic.SetChildAt(0, child); return; case 1: tom__strat.s1 = child; return; case 2: tom__strat.s2 = child; return; }; panic("S4: no such child") }; func (tom__strat *S4) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S4) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S4) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S4) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S4) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S4) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S4) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Identity(any(tom__arg).( sl.Strategy )) { 





 return tom__strat.s1;  };  };  }; { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 
 return tom_make_All(tom__strat.s2);  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_make_S4(t1  sl.Strategy , t2  int64 , t3  sl.Strategy )  sl.Strategy  { return (tom_new_S4(t1,t2,t3)) }; type S5 struct { *sl.AbstractStrategyBasic; }; func tom_new_S5() *S5 { tom__strat := &S5{}; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *S5) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *S5) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *S5) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *S5) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *S5) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *S5) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *S5) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Fail(any(tom__arg).( sl.Strategy )) { 





 return tom_make_All(tom_make_Identity());  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_make_S5()  sl.Strategy  { return (tom_new_S5()) }; 



  
  type Counter struct{ count int }
  


  
  
  func countAll(t *testing.T, s sl.Strategy) int {
    c := &Counter{}
    visitLight(t, tom_make_TopDown(tom_make_incAll(c)), s)
    return c.count
  }
  type incAll struct { *sl.AbstractStrategyBasic; c  *Counter ; }; func tom_new_incAll(c  *Counter ) *incAll { tom__strat := &incAll{c: c, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *incAll) getc()  *Counter  { return tom__strat.c }; func (tom__strat *incAll) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *incAll) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *incAll) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *incAll) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *incAll) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *incAll) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *incAll) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_All(any(tom__arg).( sl.Strategy )) { 

 tom__strat.c.count++  };  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_make_incAll(t1  *Counter )  sl.Strategy  { return (tom_new_incAll(t1)) }; type CollectExceptFirst struct { *sl.AbstractStrategyBasic; current  sl.Strategy ; c  *Counter ; }; func tom_new_CollectExceptFirst(current  sl.Strategy , c  *Counter ) *CollectExceptFirst { tom__strat := &CollectExceptFirst{current: current, c: c, }; tom__strat.AbstractStrategyBasic = sl.NewAbstractStrategyBasic(tom_make_Identity()); return tom__strat }; func (tom__strat *CollectExceptFirst) getcurrent()  sl.Strategy  { return tom__strat.current }; func (tom__strat *CollectExceptFirst) getc()  *Counter  { return tom__strat.c }; func (tom__strat *CollectExceptFirst) ChildCount() int { return 2 }; func (tom__strat *CollectExceptFirst) ChildAt(i int) sl.Strategy { switch i { case 0: return tom__strat.AbstractStrategyBasic.ChildAt(0); case 1: return tom__strat.current; }; panic("CollectExceptFirst: no such child") }; func (tom__strat *CollectExceptFirst) SetChildAt(i int, child sl.Strategy) { switch i { case 0: tom__strat.AbstractStrategyBasic.SetChildAt(0, child); return; case 1: tom__strat.current = child; return; }; panic("CollectExceptFirst: no such child") }; func (tom__strat *CollectExceptFirst) VisitLight(v any, introspector sl.Introspector) (tom__res any, tom__err error) { defer sl.RecoverVisitFailure(&tom__err); return tom__strat.visitLight(v, introspector), nil }; func (tom__strat *CollectExceptFirst) VisitEnv(introspector sl.Introspector) int { return sl.VisitBasic(tom__strat, introspector) }; func (tom__strat *CollectExceptFirst) Visit(v any, introspector sl.Introspector) (any, error) { return sl.VisitStrategy(tom__strat, v, introspector) }; func (tom__strat *CollectExceptFirst) VisitWith(env *sl.Environment, introspector sl.Introspector) (any, error) { return sl.VisitStrategyWith(tom__strat, env, introspector) }; func (tom__strat *CollectExceptFirst) visitLight(v any, introspector sl.Introspector) any { if tom_is_sort_Strategy(v) { return any(tom__strat.visit_Strategy(any(v).( sl.Strategy ),introspector)).(any);  }; if tom__strat.HasEnvironment() { return sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector));  } else { return sl.MustVisit(tom__strat.Any().VisitLight(v,introspector));  };  }; func (tom__strat *CollectExceptFirst) _visit_Strategy(arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { if tom__strat.HasEnvironment() { return any(sl.MustVisit(tom__strat.Any().VisitWith(tom__strat.GetEnvironment(),introspector))).( sl.Strategy );  } else { return any(sl.MustVisit(tom__strat.Any().VisitLight(arg,introspector))).( sl.Strategy );  };  }; func (tom__strat *CollectExceptFirst) visit_Strategy(tom__arg  sl.Strategy , introspector sl.Introspector)  sl.Strategy  { ; { { if tom_is_sort_Strategy(tom__arg) { if tom_is_fun_sym_Sequence(any(tom__arg).( sl.Strategy )) { if !( (tom_is_empty_Sequence_Strategy(any(tom__arg).( sl.Strategy )) || tom_equal_term_Strategy(any(tom__arg).( sl.Strategy ), tom_empty_list_Sequence())) ) { 







      sl.MustVisit(tom__strat.current.VisitLight(func()  sl.Strategy  { if tom_is_fun_sym_Sequence(any(tom__arg).( sl.Strategy )) { return tom_get_tail_Sequence_Strategy(any(tom__arg).( sl.Strategy )) }; return tom_empty_list_Sequence() }(), sl.VisitableIntrospector{  } ))
      sl.ThrowVisitFailure() 
       };  };  };  }; { if tom_is_sort_Strategy(tom__arg) { 

 tom__strat.c.count++  };  };  }; return tom__strat._visit_Strategy(tom__arg,introspector);  }; func tom_make_CollectExceptFirst(t1  sl.Strategy , t2  *Counter )  sl.Strategy  { return (tom_new_CollectExceptFirst(t1,t2)) }; 





func visitLight(t *testing.T, s sl.Strategy, subject any) any {
  t.Helper()
  r, err := s.VisitLight(subject, sl.VisitableIntrospector{})
  if err != nil {
    t.Fatal("tom.library.sl.VisitFailure()")
  }
  return r
}




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}



func assertFalse(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Error(message)
  }
}



func assertEquals(t *testing.T, message string, expected, actual int) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected:<%d> but was:<%d>", message, expected, actual)
  }
}
