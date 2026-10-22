package antipatterns












import (
  "testing"
)

  func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_equal_term_Result(t1  Result , t2  Result ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Result(t any) bool { return func() bool { _, ok := (t).(Result); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_c()  Term  { return (MakeC()) }; func tom_make_d()  Term  { return (MakeD()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(x1  Term , x2  Term )  Term  { return (MakeF(x1,x2)) }; func tom_get_slot_f_x1(t  Term )  Term  { return (t).(*FTerm).X1;  }; func tom_get_slot_f_x2(t  Term )  Term  { return (t).(*FTerm).X2;  }; func tom_make_g(pred  Term )  Term  { return (MakeG(pred)) }; func tom_is_fun_sym_l(t  Term ) bool { return func() bool { _, ok := (t).(*LTerm); return ok }();  }; func tom_make_l(list  TermList )  Term  { return (MakeL(list)) }; func tom_get_slot_l_list(t  Term )  TermList  { return (t).(*LTerm).List;  }; func tom_is_fun_sym_lst(t  Term ) bool { return func() bool { _, ok := (t).(*LstTerm); return ok }();  }; func tom_empty_list_lst()  Term  { return (EmptyLst()) }; func tom_cons_list_lst(e  Term , l  Term )  Term  { return (ConsLst(e,l)) }; func tom_get_head_lst_Term(l  Term )  Term  { return (l).GetHeadlst();  }; func tom_get_tail_lst_Term(l  Term )  Term  { return (l).GetTaillst();  }; func tom_is_empty_lst_Term(l  Term ) bool { return (l).IsEmptylst();  }; func tom_append_list_lst(l1  Term , l2  Term )  Term  { if tom_is_empty_lst_Term(l1) { return l2 } else if tom_is_empty_lst_Term(l2) { return l1 } else if tom_is_fun_sym_lst(l1) { if tom_is_empty_lst_Term(tom_get_tail_lst_Term(l1)) { return tom_cons_list_lst(tom_get_head_lst_Term(l1), l2) } else { return tom_cons_list_lst(tom_get_head_lst_Term(l1), tom_append_list_lst(tom_get_tail_lst_Term(l1), l2)) } } else { return tom_cons_list_lst(l1, l2) }; }; func tom_get_slice_lst(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_lst_Term(end) || tom_equal_term_Term(end, tom_empty_list_lst())) { return begin }; return tom_cons_list_lst(func()  Term  { if tom_is_fun_sym_lst(begin) { return tom_get_head_lst_Term(begin) }; return begin }(), tom_get_slice_lst(func()  Term  { if tom_is_fun_sym_lst(begin) { return tom_get_tail_lst_Term(begin) }; return tom_empty_list_lst() }(), end, tail)) }; func tom_is_fun_sym_concTerm(t  TermList ) bool { return func() bool { _, ok := (t).(*ConcTermTermList); return ok }();  }; func tom_empty_list_concTerm()  TermList  { return (EmptyConcTerm()) }; func tom_cons_list_concTerm(e  Term , l  TermList )  TermList  { return (ConsConcTerm(e,l)) }; func tom_get_head_concTerm_TermList(l  TermList )  Term  { return (l).GetHeadconcTerm();  }; func tom_get_tail_concTerm_TermList(l  TermList )  TermList  { return (l).GetTailconcTerm();  }; func tom_is_empty_concTerm_TermList(l  TermList ) bool { return (l).IsEmptyconcTerm();  }; func tom_append_list_concTerm(l1  TermList , l2  TermList )  TermList  { if tom_is_empty_concTerm_TermList(l1) { return l2 } else if tom_is_empty_concTerm_TermList(l2) { return l1 } else if tom_is_empty_concTerm_TermList(tom_get_tail_concTerm_TermList(l1)) { return tom_cons_list_concTerm(tom_get_head_concTerm_TermList(l1), l2) } else { return tom_cons_list_concTerm(tom_get_head_concTerm_TermList(l1), tom_append_list_concTerm(tom_get_tail_concTerm_TermList(l1), l2)) }; }; func tom_get_slice_concTerm(begin  TermList , end  TermList , tail  TermList )  TermList  { if tom_equal_term_TermList(begin, end) { return tail } else if tom_equal_term_TermList(end, tail) && (tom_is_empty_concTerm_TermList(end) || tom_equal_term_TermList(end, tom_empty_list_concTerm())) { return begin }; return tom_cons_list_concTerm(tom_get_head_concTerm_TermList(begin), tom_get_slice_concTerm(tom_get_tail_concTerm_TermList(begin), end, tail)) }; func tom_make_True()  Result  { return (MakeTrue()) }; func tom_make_False()  Result  { return (MakeFalse()) }; 


























func match1(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch1_end_4  TermList  = any(subject).( TermList ); _ = tomMatch1_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch1_end_4)) { if tom_is_fun_sym_a(any(tom_get_head_concTerm_TermList(tomMatch1_end_4)).( Term )) { 

        return tom_make_True() };  }; if tom_is_empty_concTerm_TermList(tomMatch1_end_4) { tomMatch1_end_4 = any(subject).( TermList );  } else { tomMatch1_end_4 = tom_get_tail_concTerm_TermList(tomMatch1_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch1_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp1(t *testing.T) {
    assertTrue(t, match1(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match1(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
}



func match2(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { var tomMatch2_9 bool =  false ; _ = tomMatch2_9; if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch2_end_4  TermList  = any(subject).( TermList ); _ = tomMatch2_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch2_end_4)) { if tom_is_fun_sym_a(any(tom_get_head_concTerm_TermList(tomMatch2_end_4)).( Term )) { tomMatch2_9 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch2_end_4) { tomMatch2_end_4 = any(subject).( TermList );  } else { tomMatch2_end_4 = tom_get_tail_concTerm_TermList(tomMatch2_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch2_end_4, any(subject).( TermList )))) { break; }; };  }; if !(tomMatch2_9) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp2(t *testing.T) {
    assertTrue(t, match2(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match2(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match2(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_b(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
}



func match3(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch3_end_4  TermList  = any(subject).( TermList ); _ = tomMatch3_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch3_end_4)) { var tomMatch3_9 bool =  false ; _ = tomMatch3_9; if tom_is_fun_sym_a(any(tom_get_head_concTerm_TermList(tomMatch3_end_4)).( Term )) { tomMatch3_9 =  true ;  }; if !(tomMatch3_9) { 

        return tom_make_True() };  }; if tom_is_empty_concTerm_TermList(tomMatch3_end_4) { tomMatch3_end_4 = any(subject).( TermList );  } else { tomMatch3_end_4 = tom_get_tail_concTerm_TermList(tomMatch3_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch3_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp3(t *testing.T) {
    assertTrue(t, match3(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match3(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match3(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match3(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match3(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
}



func match4(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { var tomMatch4_9 bool =  false ; _ = tomMatch4_9; if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch4_end_4  TermList  = any(subject).( TermList ); _ = tomMatch4_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch4_end_4)) { var tomMatch4_10 bool =  false ; _ = tomMatch4_10; if tom_is_fun_sym_a(any(tom_get_head_concTerm_TermList(tomMatch4_end_4)).( Term )) { tomMatch4_10 =  true ;  }; if !(tomMatch4_10) { tomMatch4_9 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch4_end_4) { tomMatch4_end_4 = any(subject).( TermList );  } else { tomMatch4_end_4 = tom_get_tail_concTerm_TermList(tomMatch4_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch4_end_4, any(subject).( TermList )))) { break; }; };  }; if !(tomMatch4_9) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp4(t *testing.T) {
    assertTrue(t, match4(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match4(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
}



func match4_1(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch5_end_4  TermList  = any(subject).( TermList ); _ = tomMatch5_end_4; for { { if tom_equal_term_TermList(tom_get_slice_concTerm(any(subject).( TermList ),tomMatch5_end_4,tom_empty_list_concTerm()), tomMatch5_end_4) { 

        return tom_make_True() }; if tom_is_empty_concTerm_TermList(tomMatch5_end_4) { tomMatch5_end_4 = any(subject).( TermList );  } else { tomMatch5_end_4 = tom_get_tail_concTerm_TermList(tomMatch5_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch5_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp4_1(t *testing.T) {
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match4_1(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm()))))) == tom_make_True())
}



func match5(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { var tomMatch6_7 bool =  false ; _ = tomMatch6_7; if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch6_end_4  TermList  = any(subject).( TermList ); _ = tomMatch6_end_4; for { { if tom_equal_term_TermList(tom_get_slice_concTerm(any(subject).( TermList ),tomMatch6_end_4,tom_empty_list_concTerm()), tomMatch6_end_4) { tomMatch6_7 =  true ;  }; if tom_is_empty_concTerm_TermList(tomMatch6_end_4) { tomMatch6_end_4 = any(subject).( TermList );  } else { tomMatch6_end_4 = tom_get_tail_concTerm_TermList(tomMatch6_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch6_end_4, any(subject).( TermList )))) { break; }; };  }; if !(tomMatch6_7) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp5(t *testing.T) {
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match5(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm()))))) == tom_make_False())
}



func match6(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { var tomMatch7_12 bool =  false ; _ = tomMatch7_12; if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch7_end_4  TermList  = any(subject).( TermList ); _ = tomMatch7_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch7_end_4)) { var tomMatch7_5  TermList  = tom_get_tail_concTerm_TermList(tomMatch7_end_4); _ = tomMatch7_5; var tomMatch7_end_8  TermList  = tomMatch7_5; _ = tomMatch7_end_8; for { { if !(tom_is_empty_concTerm_TermList(tomMatch7_end_8)) { if tom_equal_term_Term(tom_get_head_concTerm_TermList(tomMatch7_end_4), tom_get_head_concTerm_TermList(tomMatch7_end_8)) { tomMatch7_12 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch7_end_8) { tomMatch7_end_8 = tomMatch7_5;  } else { tomMatch7_end_8 = tom_get_tail_concTerm_TermList(tomMatch7_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch7_end_8, tomMatch7_5))) { break; }; };  }; if tom_is_empty_concTerm_TermList(tomMatch7_end_4) { tomMatch7_end_4 = any(subject).( TermList );  } else { tomMatch7_end_4 = tom_get_tail_concTerm_TermList(tomMatch7_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch7_end_4, any(subject).( TermList )))) { break; }; };  }; if !(tomMatch7_12) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp6(t *testing.T) {
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match6(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_False())
}



func match7(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch8_end_4  TermList  = any(subject).( TermList ); _ = tomMatch8_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch8_end_4)) { var tomMatch8_5  TermList  = tom_get_tail_concTerm_TermList(tomMatch8_end_4); _ = tomMatch8_5; var tomMatch8_end_8  TermList  = tomMatch8_5; _ = tomMatch8_end_8; for { { if !(tom_is_empty_concTerm_TermList(tomMatch8_end_8)) { var tomMatch8_12 bool =  false ; _ = tomMatch8_12; if tom_equal_term_Term(tom_get_head_concTerm_TermList(tomMatch8_end_4), tom_get_head_concTerm_TermList(tomMatch8_end_8)) { tomMatch8_12 =  true ;  }; if !(tomMatch8_12) { 

        return tom_make_True() };  }; if tom_is_empty_concTerm_TermList(tomMatch8_end_8) { tomMatch8_end_8 = tomMatch8_5;  } else { tomMatch8_end_8 = tom_get_tail_concTerm_TermList(tomMatch8_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch8_end_8, tomMatch8_5))) { break; }; };  }; if tom_is_empty_concTerm_TermList(tomMatch8_end_4) { tomMatch8_end_4 = any(subject).( TermList );  } else { tomMatch8_end_4 = tom_get_tail_concTerm_TermList(tomMatch8_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch8_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp7(t *testing.T) {
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_True())
    assertTrue(t, match7(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
}



func match8(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { var tomMatch9_12 bool =  false ; _ = tomMatch9_12; if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch9_end_4  TermList  = any(subject).( TermList ); _ = tomMatch9_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch9_end_4)) { var tomMatch9_5  TermList  = tom_get_tail_concTerm_TermList(tomMatch9_end_4); _ = tomMatch9_5; var tomMatch9_end_8  TermList  = tomMatch9_5; _ = tomMatch9_end_8; for { { if !(tom_is_empty_concTerm_TermList(tomMatch9_end_8)) { var tomMatch9_13 bool =  false ; _ = tomMatch9_13; if tom_equal_term_Term(tom_get_head_concTerm_TermList(tomMatch9_end_4), tom_get_head_concTerm_TermList(tomMatch9_end_8)) { tomMatch9_13 =  true ;  }; if !(tomMatch9_13) { tomMatch9_12 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch9_end_8) { tomMatch9_end_8 = tomMatch9_5;  } else { tomMatch9_end_8 = tom_get_tail_concTerm_TermList(tomMatch9_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch9_end_8, tomMatch9_5))) { break; }; };  }; if tom_is_empty_concTerm_TermList(tomMatch9_end_4) { tomMatch9_end_4 = any(subject).( TermList );  } else { tomMatch9_end_4 = tom_get_tail_concTerm_TermList(tomMatch9_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch9_end_4, any(subject).( TermList )))) { break; }; };  }; if !(tomMatch9_12) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp8(t *testing.T) {
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match8(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_True())



}



func match9(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch10_end_4  TermList  = any(subject).( TermList ); _ = tomMatch10_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch10_end_4)) { var tomMatch10_5  TermList  = tom_get_tail_concTerm_TermList(tomMatch10_end_4); _ = tomMatch10_5; var tomMatch10_end_8  TermList  = tomMatch10_5; _ = tomMatch10_end_8; for { { if !(tom_is_empty_concTerm_TermList(tomMatch10_end_8)) { var tomMatch10_11 bool =  false ; _ = tomMatch10_11; tomMatch10_11 =  true ; if !(tomMatch10_11) { 

        return tom_make_True() };  }; if tom_is_empty_concTerm_TermList(tomMatch10_end_8) { tomMatch10_end_8 = tomMatch10_5;  } else { tomMatch10_end_8 = tom_get_tail_concTerm_TermList(tomMatch10_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch10_end_8, tomMatch10_5))) { break; }; };  }; if tom_is_empty_concTerm_TermList(tomMatch10_end_4) { tomMatch10_end_4 = any(subject).( TermList );  } else { tomMatch10_end_4 = tom_get_tail_concTerm_TermList(tomMatch10_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch10_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp9(t *testing.T) {
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_f(tom_make_a(),tom_make_c()),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm())))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_b(),tom_empty_list_concTerm()))))) == tom_make_False())
    assertTrue(t, match9(tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_a(),tom_empty_list_concTerm()))))) == tom_make_False())
}



func match10(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch11_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch11_2; if tom_is_fun_sym_l(any(tomMatch11_2).( Term )) { var tomMatch11_5  TermList  = tom_get_slot_l_list(tomMatch11_2); _ = tomMatch11_5; if tom_is_fun_sym_concTerm(any(tomMatch11_5).( TermList )) { var tomMatch11_end_11  TermList  = tomMatch11_5; _ = tomMatch11_end_11; for { { if !(tom_is_empty_concTerm_TermList(tomMatch11_end_11)) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_head_concTerm_TermList(tomMatch11_end_11)) { 

        return tom_make_True() };  }; if tom_is_empty_concTerm_TermList(tomMatch11_end_11) { tomMatch11_end_11 = tomMatch11_5;  } else { tomMatch11_end_11 = tom_get_tail_concTerm_TermList(tomMatch11_end_11);  };  };  if !(!(tom_equal_term_TermList(tomMatch11_end_11, tomMatch11_5))) { break; }; };  };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp10(t *testing.T) {
    assertTrue(t, match10(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match10(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match10(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match10(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_g(tom_make_a()),tom_empty_list_concTerm())))))) == tom_make_True())
}



func match10_1(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch12_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch12_2; if tom_is_fun_sym_l(any(tomMatch12_2).( Term )) { var tomMatch12_5  TermList  = tom_get_slot_l_list(tomMatch12_2); _ = tomMatch12_5; var tomMatch12_15 bool =  false ; _ = tomMatch12_15; if tom_is_fun_sym_concTerm(any(tomMatch12_5).( TermList )) { var tomMatch12_end_11  TermList  = tomMatch12_5; _ = tomMatch12_end_11; for { { if !(tom_is_empty_concTerm_TermList(tomMatch12_end_11)) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_head_concTerm_TermList(tomMatch12_end_11)) { tomMatch12_15 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch12_end_11) { tomMatch12_end_11 = tomMatch12_5;  } else { tomMatch12_end_11 = tom_get_tail_concTerm_TermList(tomMatch12_end_11);  };  };  if !(!(tom_equal_term_TermList(tomMatch12_end_11, tomMatch12_5))) { break; }; };  }; if !(tomMatch12_15) { 

        return tom_make_True() };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp10_1(t *testing.T) {
    assertTrue(t, match10_1(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match10_1(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match10_1(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match10_1(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_g(tom_make_a()),tom_empty_list_concTerm())))))) == tom_make_False())
}



func match11(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch13_15 bool =  false ; _ = tomMatch13_15; if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch13_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch13_2; if tom_is_fun_sym_l(any(tomMatch13_2).( Term )) { var tomMatch13_5  TermList  = tom_get_slot_l_list(tomMatch13_2); _ = tomMatch13_5; if tom_is_fun_sym_concTerm(any(tomMatch13_5).( TermList )) { var tomMatch13_end_11  TermList  = tomMatch13_5; _ = tomMatch13_end_11; for { { if !(tom_is_empty_concTerm_TermList(tomMatch13_end_11)) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_head_concTerm_TermList(tomMatch13_end_11)) { tomMatch13_15 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch13_end_11) { tomMatch13_end_11 = tomMatch13_5;  } else { tomMatch13_end_11 = tom_get_tail_concTerm_TermList(tomMatch13_end_11);  };  };  if !(!(tom_equal_term_TermList(tomMatch13_end_11, tomMatch13_5))) { break; }; };  };  };  }; if !(tomMatch13_15) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp11(t *testing.T) {
    assertTrue(t, match11(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match11(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match11(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match11(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_g(tom_make_a()),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match11(tom_make_a()) == tom_make_True())
}



func match12(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch14_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch14_2; var tomMatch14_15 bool =  false ; _ = tomMatch14_15; if tom_is_fun_sym_l(any(tomMatch14_2).( Term )) { var tomMatch14_5  TermList  = tom_get_slot_l_list(tomMatch14_2); _ = tomMatch14_5; if tom_is_fun_sym_concTerm(any(tomMatch14_5).( TermList )) { var tomMatch14_end_11  TermList  = tomMatch14_5; _ = tomMatch14_end_11; for { { if !(tom_is_empty_concTerm_TermList(tomMatch14_end_11)) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_head_concTerm_TermList(tomMatch14_end_11)) { tomMatch14_15 =  true ;  };  }; if tom_is_empty_concTerm_TermList(tomMatch14_end_11) { tomMatch14_end_11 = tomMatch14_5;  } else { tomMatch14_end_11 = tom_get_tail_concTerm_TermList(tomMatch14_end_11);  };  };  if !(!(tom_equal_term_TermList(tomMatch14_end_11, tomMatch14_5))) { break; }; };  };  }; if !(tomMatch14_15) { 

        return tom_make_True() };  };  };  };  }; 


    return tom_make_False()
}



func TestAp12(t *testing.T) {
    assertTrue(t, match12(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_a(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match12(tom_make_f(tom_make_a(),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match12(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_c(),tom_empty_list_concTerm())))))) == tom_make_True())
    assertTrue(t, match12(tom_make_f(tom_make_g(tom_make_a()),tom_make_l(tom_cons_list_concTerm(tom_make_b(),tom_cons_list_concTerm(tom_make_c(),tom_cons_list_concTerm(tom_make_g(tom_make_a()),tom_empty_list_concTerm())))))) == tom_make_False())
    assertTrue(t, match12(tom_make_a()) == tom_make_False())
    assertTrue(t, match12(tom_make_f(tom_make_g(tom_make_a()),tom_make_b())) == tom_make_True())
}

  


func match13(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_lst(any(subject).( Term )) { var tomMatch15_end_6  Term  = any(subject).( Term ); _ = tomMatch15_end_6; for { { var tomMatch15_end_9  Term  = tomMatch15_end_6; _ = tomMatch15_end_9; for { { var tomMatch15_1  Term  = tom_get_slice_lst(tomMatch15_end_6,tomMatch15_end_9,tom_empty_list_lst()); _ = tomMatch15_1; var tomMatch15_end_12  Term  = tomMatch15_end_9; _ = tomMatch15_end_12; for { { var tomMatch15_2  Term  = tom_get_slice_lst(tomMatch15_end_9,tomMatch15_end_12,tom_empty_list_lst()); _ = tomMatch15_2; if tom_is_fun_sym_lst(any(tomMatch15_2).( Term )) { var tomMatch15_end_17  Term  = tomMatch15_2; _ = tomMatch15_end_17; for { { if !( (tom_is_empty_lst_Term(tomMatch15_end_17) || tom_equal_term_Term(tomMatch15_end_17, tom_empty_list_lst())) ) { var tomMatch15_28  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_17) { return tom_get_head_lst_Term(tomMatch15_end_17) }; return tomMatch15_end_17 }(); _ = tomMatch15_28; if tom_is_fun_sym_f(any(tomMatch15_28).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_x1(tomMatch15_28)).( Term )) { var tom___x  Term  = tom_get_slot_f_x2(tomMatch15_28); _ = tom___x; if tom_is_fun_sym_lst(any(tomMatch15_1).( Term )) { var tomMatch15_end_23  Term  = tomMatch15_1; _ = tomMatch15_end_23; for { { if !( (tom_is_empty_lst_Term(tomMatch15_end_23) || tom_equal_term_Term(tomMatch15_end_23, tom_empty_list_lst())) ) { var tomMatch15_34  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_23) { return tom_get_head_lst_Term(tomMatch15_end_23) }; return tomMatch15_end_23 }(); _ = tomMatch15_34; if tom_is_fun_sym_f(any(tomMatch15_34).( Term )) { var tomMatch15_33  Term  = tom_get_slot_f_x2(tomMatch15_34); _ = tomMatch15_33; if tom_is_fun_sym_a(any(tom_get_slot_f_x1(tomMatch15_34)).( Term )) { if tom_equal_term_Term(tom___x, tomMatch15_33) { var tomMatch15_42 bool =  false ; _ = tomMatch15_42; if tom_is_fun_sym_a(any(tomMatch15_33).( Term )) { if tom_equal_term_Term(tom___x, tomMatch15_33) { tomMatch15_42 =  true ;  };  }; if !(tomMatch15_42) { 

        return tom_make_True() };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch15_end_23) || tom_equal_term_Term(tomMatch15_end_23, tom_empty_list_lst()))  { tomMatch15_end_23 = tomMatch15_1;  } else { tomMatch15_end_23 = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_23) { return tom_get_tail_lst_Term(tomMatch15_end_23) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch15_end_23, tomMatch15_1))) { break; }; };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch15_end_17) || tom_equal_term_Term(tomMatch15_end_17, tom_empty_list_lst()))  { tomMatch15_end_17 = tomMatch15_2;  } else { tomMatch15_end_17 = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_17) { return tom_get_tail_lst_Term(tomMatch15_end_17) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch15_end_17, tomMatch15_2))) { break; }; };  }; if  (tom_is_empty_lst_Term(tomMatch15_end_12) || tom_equal_term_Term(tomMatch15_end_12, tom_empty_list_lst()))  { tomMatch15_end_12 = tomMatch15_end_9;  } else { tomMatch15_end_12 = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_12) { return tom_get_tail_lst_Term(tomMatch15_end_12) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch15_end_12, tomMatch15_end_9))) { break; }; }; if  (tom_is_empty_lst_Term(tomMatch15_end_9) || tom_equal_term_Term(tomMatch15_end_9, tom_empty_list_lst()))  { tomMatch15_end_9 = tomMatch15_end_6;  } else { tomMatch15_end_9 = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_9) { return tom_get_tail_lst_Term(tomMatch15_end_9) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch15_end_9, tomMatch15_end_6))) { break; }; }; if  (tom_is_empty_lst_Term(tomMatch15_end_6) || tom_equal_term_Term(tomMatch15_end_6, tom_empty_list_lst()))  { tomMatch15_end_6 = any(subject).( Term );  } else { tomMatch15_end_6 = func()  Term  { if tom_is_fun_sym_lst(tomMatch15_end_6) { return tom_get_tail_lst_Term(tomMatch15_end_6) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch15_end_6, any(subject).( Term )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}

  



func match14(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_lst(any(subject).( Term )) { var tomMatch16_end_6  Term  = any(subject).( Term ); _ = tomMatch16_end_6; for { { var tomMatch16_end_9  Term  = tomMatch16_end_6; _ = tomMatch16_end_9; for { { var tomMatch16_1  Term  = tom_get_slice_lst(tomMatch16_end_6,tomMatch16_end_9,tom_empty_list_lst()); _ = tomMatch16_1; var tomMatch16_end_12  Term  = tomMatch16_end_9; _ = tomMatch16_end_12; for { { var tomMatch16_2  Term  = tom_get_slice_lst(tomMatch16_end_9,tomMatch16_end_12,tom_empty_list_lst()); _ = tomMatch16_2; if tom_is_fun_sym_lst(any(tomMatch16_2).( Term )) { var tomMatch16_end_17  Term  = tomMatch16_2; _ = tomMatch16_end_17; for { { if !( (tom_is_empty_lst_Term(tomMatch16_end_17) || tom_equal_term_Term(tomMatch16_end_17, tom_empty_list_lst())) ) { var tomMatch16_28  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_17) { return tom_get_head_lst_Term(tomMatch16_end_17) }; return tomMatch16_end_17 }(); _ = tomMatch16_28; if tom_is_fun_sym_f(any(tomMatch16_28).( Term )) { var tomMatch16_27  Term  = tom_get_slot_f_x2(tomMatch16_28); _ = tomMatch16_27; if tom_is_fun_sym_a(any(tom_get_slot_f_x1(tomMatch16_28)).( Term )) { var tom___x  Term  = tomMatch16_27; _ = tom___x; if tom_is_fun_sym_lst(any(tomMatch16_1).( Term )) { var tomMatch16_end_23  Term  = tomMatch16_1; _ = tomMatch16_end_23; for { { if !( (tom_is_empty_lst_Term(tomMatch16_end_23) || tom_equal_term_Term(tomMatch16_end_23, tom_empty_list_lst())) ) { var tomMatch16_34  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_23) { return tom_get_head_lst_Term(tomMatch16_end_23) }; return tomMatch16_end_23 }(); _ = tomMatch16_34; if tom_is_fun_sym_f(any(tomMatch16_34).( Term )) { var tomMatch16_33  Term  = tom_get_slot_f_x2(tomMatch16_34); _ = tomMatch16_33; if tom_is_fun_sym_a(any(tom_get_slot_f_x1(tomMatch16_34)).( Term )) { if tom_equal_term_Term(tom___x, tomMatch16_33) { var tomMatch16_46 bool =  false ; _ = tomMatch16_46; if tom_is_fun_sym_a(any(tomMatch16_33).( Term )) { if tom_equal_term_Term(tom___x, tomMatch16_33) { tomMatch16_46 =  true ;  };  }; if !(tomMatch16_46) { var tomMatch16_45 bool =  false ; _ = tomMatch16_45; if tom_is_fun_sym_a(any(tomMatch16_27).( Term )) { if tom_equal_term_Term(tom___x, tomMatch16_27) { tomMatch16_45 =  true ;  };  }; if !(tomMatch16_45) { 

        return tom_make_True() };  };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch16_end_23) || tom_equal_term_Term(tomMatch16_end_23, tom_empty_list_lst()))  { tomMatch16_end_23 = tomMatch16_1;  } else { tomMatch16_end_23 = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_23) { return tom_get_tail_lst_Term(tomMatch16_end_23) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch16_end_23, tomMatch16_1))) { break; }; };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch16_end_17) || tom_equal_term_Term(tomMatch16_end_17, tom_empty_list_lst()))  { tomMatch16_end_17 = tomMatch16_2;  } else { tomMatch16_end_17 = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_17) { return tom_get_tail_lst_Term(tomMatch16_end_17) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch16_end_17, tomMatch16_2))) { break; }; };  }; if  (tom_is_empty_lst_Term(tomMatch16_end_12) || tom_equal_term_Term(tomMatch16_end_12, tom_empty_list_lst()))  { tomMatch16_end_12 = tomMatch16_end_9;  } else { tomMatch16_end_12 = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_12) { return tom_get_tail_lst_Term(tomMatch16_end_12) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch16_end_12, tomMatch16_end_9))) { break; }; }; if  (tom_is_empty_lst_Term(tomMatch16_end_9) || tom_equal_term_Term(tomMatch16_end_9, tom_empty_list_lst()))  { tomMatch16_end_9 = tomMatch16_end_6;  } else { tomMatch16_end_9 = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_9) { return tom_get_tail_lst_Term(tomMatch16_end_9) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch16_end_9, tomMatch16_end_6))) { break; }; }; if  (tom_is_empty_lst_Term(tomMatch16_end_6) || tom_equal_term_Term(tomMatch16_end_6, tom_empty_list_lst()))  { tomMatch16_end_6 = any(subject).( Term );  } else { tomMatch16_end_6 = func()  Term  { if tom_is_fun_sym_lst(tomMatch16_end_6) { return tom_get_tail_lst_Term(tomMatch16_end_6) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch16_end_6, any(subject).( Term )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func match15(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_lst(any(subject).( Term )) { var tomMatch17_end_4  Term  = any(subject).( Term ); _ = tomMatch17_end_4; for { { if !( (tom_is_empty_lst_Term(tomMatch17_end_4) || tom_equal_term_Term(tomMatch17_end_4, tom_empty_list_lst())) ) { var tomMatch17_13  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch17_end_4) { return tom_get_head_lst_Term(tomMatch17_end_4) }; return tomMatch17_end_4 }(); _ = tomMatch17_13; if tom_is_fun_sym_f(any(tomMatch17_13).( Term )) { var tomMatch17_12  Term  = tom_get_slot_f_x2(tomMatch17_13); _ = tomMatch17_12; var tomMatch17_5  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch17_end_4) { return tom_get_tail_lst_Term(tomMatch17_end_4) }; return tom_empty_list_lst() }(); _ = tomMatch17_5; var tomMatch17_end_8  Term  = tomMatch17_5; _ = tomMatch17_end_8; for { { if !( (tom_is_empty_lst_Term(tomMatch17_end_8) || tom_equal_term_Term(tomMatch17_end_8, tom_empty_list_lst())) ) { var tomMatch17_17  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch17_end_8) { return tom_get_head_lst_Term(tomMatch17_end_8) }; return tomMatch17_end_8 }(); _ = tomMatch17_17; if tom_is_fun_sym_f(any(tomMatch17_17).( Term )) { if tom_equal_term_Term(tom_get_slot_f_x1(tomMatch17_13), tom_get_slot_f_x1(tomMatch17_17)) { var tomMatch17_23 bool =  false ; _ = tomMatch17_23; if tom_is_fun_sym_a(any(tomMatch17_12).( Term )) { if tom_equal_term_Term(tomMatch17_12, tomMatch17_12) { tomMatch17_23 =  true ;  };  }; if !(tomMatch17_23) { 



        return tom_make_True() };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch17_end_8) || tom_equal_term_Term(tomMatch17_end_8, tom_empty_list_lst()))  { tomMatch17_end_8 = tomMatch17_5;  } else { tomMatch17_end_8 = func()  Term  { if tom_is_fun_sym_lst(tomMatch17_end_8) { return tom_get_tail_lst_Term(tomMatch17_end_8) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch17_end_8, tomMatch17_5))) { break; }; };  };  }; if  (tom_is_empty_lst_Term(tomMatch17_end_4) || tom_equal_term_Term(tomMatch17_end_4, tom_empty_list_lst()))  { tomMatch17_end_4 = any(subject).( Term );  } else { tomMatch17_end_4 = func()  Term  { if tom_is_fun_sym_lst(tomMatch17_end_4) { return tom_get_tail_lst_Term(tomMatch17_end_4) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch17_end_4, any(subject).( Term )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp15(t *testing.T) {
    var s Term = tom_cons_list_lst(tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_b()),tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_a()),tom_empty_list_lst()))),tom_cons_list_lst(tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_b()),tom_cons_list_lst(tom_make_f(tom_make_a(),tom_make_a()),tom_empty_list_lst()))),tom_empty_list_lst()))
    assertTrue(t, match15(s) == tom_make_True())
}



func match16(subject TermList) Result {
    ; { { if tom_is_sort_TermList(subject) { if tom_is_fun_sym_concTerm(any(subject).( TermList )) { var tomMatch18_end_4  TermList  = any(subject).( TermList ); _ = tomMatch18_end_4; for { { if !(tom_is_empty_concTerm_TermList(tomMatch18_end_4)) { var tomMatch18_8  Term  = tom_get_head_concTerm_TermList(tomMatch18_end_4); _ = tomMatch18_8; if tom_is_fun_sym_lst(any(tomMatch18_8).( Term )) { var tomMatch18_end_11  Term  = tomMatch18_8; _ = tomMatch18_end_11; for { { if !( (tom_is_empty_lst_Term(tomMatch18_end_11) || tom_equal_term_Term(tomMatch18_end_11, tom_empty_list_lst())) ) { var tomMatch18_22  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch18_end_11) { return tom_get_head_lst_Term(tomMatch18_end_11) }; return tomMatch18_end_11 }(); _ = tomMatch18_22; if tom_is_fun_sym_f(any(tomMatch18_22).( Term )) { var tomMatch18_21  Term  = tom_get_slot_f_x2(tomMatch18_22); _ = tomMatch18_21; var tomMatch18_5  TermList  = tom_get_tail_concTerm_TermList(tomMatch18_end_4); _ = tomMatch18_5; if !(tom_is_empty_concTerm_TermList(tomMatch18_5)) { var tomMatch18_14  Term  = tom_get_head_concTerm_TermList(tomMatch18_5); _ = tomMatch18_14; if tom_is_fun_sym_lst(any(tomMatch18_14).( Term )) { var tomMatch18_end_17  Term  = tomMatch18_14; _ = tomMatch18_end_17; for { { if !( (tom_is_empty_lst_Term(tomMatch18_end_17) || tom_equal_term_Term(tomMatch18_end_17, tom_empty_list_lst())) ) { var tomMatch18_26  Term  = func()  Term  { if tom_is_fun_sym_lst(tomMatch18_end_17) { return tom_get_head_lst_Term(tomMatch18_end_17) }; return tomMatch18_end_17 }(); _ = tomMatch18_26; if tom_is_fun_sym_f(any(tomMatch18_26).( Term )) { if tom_equal_term_Term(tom_get_slot_f_x1(tomMatch18_22), tom_get_slot_f_x1(tomMatch18_26)) { if tom_is_fun_sym_a(any(tom_get_slot_f_x2(tomMatch18_26)).( Term )) { var tomMatch18_34 bool =  false ; _ = tomMatch18_34; if tom_is_fun_sym_a(any(tomMatch18_21).( Term )) { if tom_equal_term_Term(tomMatch18_21, tomMatch18_21) { tomMatch18_34 =  true ;  };  }; if !(tomMatch18_34) { 

        return tom_make_True() };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch18_end_17) || tom_equal_term_Term(tomMatch18_end_17, tom_empty_list_lst()))  { tomMatch18_end_17 = tomMatch18_14;  } else { tomMatch18_end_17 = func()  Term  { if tom_is_fun_sym_lst(tomMatch18_end_17) { return tom_get_tail_lst_Term(tomMatch18_end_17) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch18_end_17, tomMatch18_14))) { break; }; };  };  };  };  }; if  (tom_is_empty_lst_Term(tomMatch18_end_11) || tom_equal_term_Term(tomMatch18_end_11, tom_empty_list_lst()))  { tomMatch18_end_11 = tomMatch18_8;  } else { tomMatch18_end_11 = func()  Term  { if tom_is_fun_sym_lst(tomMatch18_end_11) { return tom_get_tail_lst_Term(tomMatch18_end_11) }; return tom_empty_list_lst() }();  };  };  if !(!(tom_equal_term_Term(tomMatch18_end_11, tomMatch18_8))) { break; }; };  };  }; if tom_is_empty_concTerm_TermList(tomMatch18_end_4) { tomMatch18_end_4 = any(subject).( TermList );  } else { tomMatch18_end_4 = tom_get_tail_concTerm_TermList(tomMatch18_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch18_end_4, any(subject).( TermList )))) { break; }; };  };  };  };  }; 


    return tom_make_False()
}



func TestAp16(t *testing.T) {
    var tLst TermList = tom_cons_list_concTerm(tom_cons_list_lst(tom_make_f(tom_make_b(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_c(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_d(),tom_make_a()),tom_empty_list_lst()))),tom_cons_list_concTerm(tom_cons_list_lst(tom_make_f(tom_make_b(),tom_make_b()),tom_cons_list_lst(tom_make_f(tom_make_c(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_d(),tom_make_a()),tom_empty_list_lst()))),tom_cons_list_concTerm(tom_cons_list_lst(tom_make_f(tom_make_b(),tom_make_b()),tom_cons_list_lst(tom_make_f(tom_make_c(),tom_make_a()),tom_cons_list_lst(tom_make_f(tom_make_d(),tom_make_c()),tom_empty_list_lst()))),tom_cons_list_concTerm(tom_cons_list_lst(tom_make_f(tom_make_b(),tom_make_c()),tom_cons_list_lst(tom_make_f(tom_make_c(),tom_make_c()),tom_cons_list_lst(tom_make_f(tom_make_d(),tom_make_a()),tom_empty_list_lst()))),tom_cons_list_concTerm(tom_cons_list_lst(tom_make_f(tom_make_b(),tom_make_b()),tom_cons_list_lst(tom_make_f(tom_make_c(),tom_make_d()),tom_cons_list_lst(tom_make_f(tom_make_d(),tom_make_d()),tom_empty_list_lst()))),tom_empty_list_concTerm())))))







    assertTrue(t, match16(tLst) == tom_make_True())
}




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}
