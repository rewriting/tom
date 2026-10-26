





























package testsublists



import "testing"

func tom_TestSublists_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestSublists_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestSublists_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_TestSublists_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_TestSublists_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_TestSublists_make_a()  Term  { return (MakeA()) }; func tom_TestSublists_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_TestSublists_make_b()  Term  { return (MakeB()) }; func tom_TestSublists_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_TestSublists_make_c()  Term  { return (MakeC()) }; func tom_TestSublists_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_TestSublists_empty_list_list()  Term  { return (EmptyList()) }; func tom_TestSublists_cons_list_list(e  Term , l  Term )  Term  { return (ConsList(e,l)) }; func tom_TestSublists_get_head_list_Term(l  Term )  Term  { return (l).GetHeadlist();  }; func tom_TestSublists_get_tail_list_Term(l  Term )  Term  { return (l).GetTaillist();  }; func tom_TestSublists_is_empty_list_Term(l  Term ) bool { return (l).IsEmptylist();  }; func tom_TestSublists_append_list_list(l1  Term , l2  Term )  Term  { if tom_TestSublists_is_empty_list_Term(l1) { return l2 } else if tom_TestSublists_is_empty_list_Term(l2) { return l1 } else if tom_TestSublists_is_fun_sym_list(l1) { if tom_TestSublists_is_empty_list_Term(tom_TestSublists_get_tail_list_Term(l1)) { return tom_TestSublists_cons_list_list(tom_TestSublists_get_head_list_Term(l1), l2) } else { return tom_TestSublists_cons_list_list(tom_TestSublists_get_head_list_Term(l1), tom_TestSublists_append_list_list(tom_TestSublists_get_tail_list_Term(l1), l2)) } } else { return tom_TestSublists_cons_list_list(l1, l2) }; }; func tom_TestSublists_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_TestSublists_equal_term_Term(begin, end) { return tail } else if tom_TestSublists_equal_term_Term(end, tail) && (tom_TestSublists_is_empty_list_Term(end) || tom_TestSublists_equal_term_Term(end, tom_TestSublists_empty_list_list())) { return begin }; return tom_TestSublists_cons_list_list(func()  Term  { if tom_TestSublists_is_fun_sym_list(begin) { return tom_TestSublists_get_head_list_Term(begin) }; return begin }(), tom_TestSublists_get_slice_list(func()  Term  { if tom_TestSublists_is_fun_sym_list(begin) { return tom_TestSublists_get_tail_list_Term(begin) }; return tom_TestSublists_empty_list_list() }(), end, tail)) }; 














func Test1(t *testing.T) {
  var res Term = tom_TestSublists_cons_list_list(tom_TestSublists_make_a(),tom_TestSublists_cons_list_list(tom_TestSublists_make_b(),tom_TestSublists_cons_list_list(tom_TestSublists_make_c(),tom_TestSublists_empty_list_list()))); { { if tom_TestSublists_is_sort_Term(res) { if tom_TestSublists_is_fun_sym_list(any(res).( Term )) { var tomMatch1_end_5  Term  = any(res).( Term ); for { { var tomMatch1_end_8  Term  = tomMatch1_end_5; for { { var tomMatch1_1  Term  = tom_TestSublists_get_slice_list(tomMatch1_end_5,tomMatch1_end_8,tom_TestSublists_empty_list_list()); if tom_TestSublists_is_fun_sym_list(any(tomMatch1_1).( Term )) { if !( (tom_TestSublists_is_empty_list_Term(tomMatch1_1) || tom_TestSublists_equal_term_Term(tomMatch1_1, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_a(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_1) { return tom_TestSublists_get_head_list_Term(tomMatch1_1) }; return tomMatch1_1 }()).( Term )) { var tomMatch1_11  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_1) { return tom_TestSublists_get_tail_list_Term(tomMatch1_1) }; return tom_TestSublists_empty_list_list() }(); if !( (tom_TestSublists_is_empty_list_Term(tomMatch1_11) || tom_TestSublists_equal_term_Term(tomMatch1_11, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_b(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_11) { return tom_TestSublists_get_head_list_Term(tomMatch1_11) }; return tomMatch1_11 }()).( Term )) { var tomMatch1_12  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_11) { return tom_TestSublists_get_tail_list_Term(tomMatch1_11) }; return tom_TestSublists_empty_list_list() }(); if !( (tom_TestSublists_is_empty_list_Term(tomMatch1_12) || tom_TestSublists_equal_term_Term(tomMatch1_12, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_c(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_12) { return tom_TestSublists_get_head_list_Term(tomMatch1_12) }; return tomMatch1_12 }()).( Term )) { var tomMatch1_13  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_12) { return tom_TestSublists_get_tail_list_Term(tomMatch1_12) }; return tom_TestSublists_empty_list_list() }(); if  (tom_TestSublists_is_empty_list_Term(tomMatch1_13) || tom_TestSublists_equal_term_Term(tomMatch1_13, tom_TestSublists_empty_list_list()))  { 


      return
    ;  };  };  };  };  };  };  };  }; if  (tom_TestSublists_is_empty_list_Term(tomMatch1_end_8) || tom_TestSublists_equal_term_Term(tomMatch1_end_8, tom_TestSublists_empty_list_list()))  { tomMatch1_end_8 = tomMatch1_end_5;  } else { tomMatch1_end_8 = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_end_8) { return tom_TestSublists_get_tail_list_Term(tomMatch1_end_8) }; return tom_TestSublists_empty_list_list() }();  };  };  if !(!(tom_TestSublists_equal_term_Term(tomMatch1_end_8, tomMatch1_end_5))) { break; }; }; if  (tom_TestSublists_is_empty_list_Term(tomMatch1_end_5) || tom_TestSublists_equal_term_Term(tomMatch1_end_5, tom_TestSublists_empty_list_list()))  { tomMatch1_end_5 = any(res).( Term );  } else { tomMatch1_end_5 = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch1_end_5) { return tom_TestSublists_get_tail_list_Term(tomMatch1_end_5) }; return tom_TestSublists_empty_list_list() }();  };  };  if !(!(tom_TestSublists_equal_term_Term(tomMatch1_end_5, any(res).( Term )))) { break; }; };  };  };  };  }; 

  t.FailNow()
}



func Test2(t *testing.T) {
  var res Term = tom_TestSublists_cons_list_list(tom_TestSublists_make_a(),tom_TestSublists_cons_list_list(tom_TestSublists_make_b(),tom_TestSublists_cons_list_list(tom_TestSublists_make_c(),tom_TestSublists_empty_list_list()))); { { if tom_TestSublists_is_sort_Term(res) { if tom_TestSublists_is_fun_sym_list(any(res).( Term )) { var tomMatch2_end_5  Term  = any(res).( Term ); for { { var tomMatch2_end_8  Term  = tomMatch2_end_5; for { { var tomMatch2_1  Term  = tom_TestSublists_get_slice_list(tomMatch2_end_5,tomMatch2_end_8,tom_TestSublists_empty_list_list()); if tom_TestSublists_is_fun_sym_list(any(tomMatch2_1).( Term )) { if !( (tom_TestSublists_is_empty_list_Term(tomMatch2_1) || tom_TestSublists_equal_term_Term(tomMatch2_1, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_a(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_1) { return tom_TestSublists_get_head_list_Term(tomMatch2_1) }; return tomMatch2_1 }()).( Term )) { var tomMatch2_11  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_1) { return tom_TestSublists_get_tail_list_Term(tomMatch2_1) }; return tom_TestSublists_empty_list_list() }(); if !( (tom_TestSublists_is_empty_list_Term(tomMatch2_11) || tom_TestSublists_equal_term_Term(tomMatch2_11, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_b(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_11) { return tom_TestSublists_get_head_list_Term(tomMatch2_11) }; return tomMatch2_11 }()).( Term )) { var tomMatch2_12  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_11) { return tom_TestSublists_get_tail_list_Term(tomMatch2_11) }; return tom_TestSublists_empty_list_list() }(); if !( (tom_TestSublists_is_empty_list_Term(tomMatch2_12) || tom_TestSublists_equal_term_Term(tomMatch2_12, tom_TestSublists_empty_list_list())) ) { if tom_TestSublists_is_fun_sym_c(any(func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_12) { return tom_TestSublists_get_head_list_Term(tomMatch2_12) }; return tomMatch2_12 }()).( Term )) { var tomMatch2_13  Term  = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_12) { return tom_TestSublists_get_tail_list_Term(tomMatch2_12) }; return tom_TestSublists_empty_list_list() }(); if  (tom_TestSublists_is_empty_list_Term(tomMatch2_13) || tom_TestSublists_equal_term_Term(tomMatch2_13, tom_TestSublists_empty_list_list()))  { 


      return
    ;  };  };  };  };  };  };  };  }; if  (tom_TestSublists_is_empty_list_Term(tomMatch2_end_8) || tom_TestSublists_equal_term_Term(tomMatch2_end_8, tom_TestSublists_empty_list_list()))  { tomMatch2_end_8 = tomMatch2_end_5;  } else { tomMatch2_end_8 = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_end_8) { return tom_TestSublists_get_tail_list_Term(tomMatch2_end_8) }; return tom_TestSublists_empty_list_list() }();  };  };  if !(!(tom_TestSublists_equal_term_Term(tomMatch2_end_8, tomMatch2_end_5))) { break; }; }; if  (tom_TestSublists_is_empty_list_Term(tomMatch2_end_5) || tom_TestSublists_equal_term_Term(tomMatch2_end_5, tom_TestSublists_empty_list_list()))  { tomMatch2_end_5 = any(res).( Term );  } else { tomMatch2_end_5 = func()  Term  { if tom_TestSublists_is_fun_sym_list(tomMatch2_end_5) { return tom_TestSublists_get_tail_list_Term(tomMatch2_end_5) }; return tom_TestSublists_empty_list_list() }();  };  };  if !(!(tom_TestSublists_equal_term_Term(tomMatch2_end_5, any(res).( Term )))) { break; }; };  };  };  };  }; 

  t.FailNow()
}


