package testsublists



import "testing"

func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_make_b()  Term  { return (MakeB()) }; func tom_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_make_c()  Term  { return (MakeC()) }; func tom_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_empty_list_list()  Term  { return (MakeList()) }; func tom_cons_list_list(e  Term , l  Term )  Term  { return (func() Term { s := (l).(*ListTerm).Slots; return MakeList(append([]Term{e}, s...)...) }()) }; func tom_get_head_list_Term(l  Term )  Term  { return (l).(*ListTerm).Slots[0];  }; func tom_get_tail_list_Term(l  Term )  Term  { return func() Term { s := (l).(*ListTerm).Slots; return MakeList(s[1:]...) }();  }; func tom_is_empty_list_Term(l  Term ) bool { return len((l).(*ListTerm).Slots) == 0;  }; func tom_append_list_list(l1  Term , l2  Term )  Term  { if tom_is_empty_list_Term(l1) { return l2 } else if tom_is_empty_list_Term(l2) { return l1 } else if tom_is_fun_sym_list(l1) { if tom_is_empty_list_Term(tom_get_tail_list_Term(l1)) { return tom_cons_list_list(tom_get_head_list_Term(l1), l2) } else { return tom_cons_list_list(tom_get_head_list_Term(l1), tom_append_list_list(tom_get_tail_list_Term(l1), l2)) } } else { return tom_cons_list_list(l1, l2) }; }; func tom_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_list_Term(end) || tom_equal_term_Term(end, tom_empty_list_list())) { return begin }; return tom_cons_list_list(func()  Term  { if tom_is_fun_sym_list(begin) { return tom_get_head_list_Term(begin) }; return begin }(), tom_get_slice_list(func()  Term  { if tom_is_fun_sym_list(begin) { return tom_get_tail_list_Term(begin) }; return tom_empty_list_list() }(), end, tail)) }; func tom_is_fun_sym_termList(t  TermList ) bool { return func() bool { _, ok := (t).(*TermListTermList); return ok }();  }; func tom_empty_list_termList()  TermList  { return (MakeTermList()) }; func tom_cons_list_termList(e  Term , l  TermList )  TermList  { return (func() TermList { s := (l).(*TermListTermList).Slots; return MakeTermList(append([]Term{e}, s...)...) }()) }; func tom_get_head_termList_TermList(l  TermList )  Term  { return (l).(*TermListTermList).Slots[0];  }; func tom_get_tail_termList_TermList(l  TermList )  TermList  { return func() TermList { s := (l).(*TermListTermList).Slots; return MakeTermList(s[1:]...) }();  }; func tom_is_empty_termList_TermList(l  TermList ) bool { return len((l).(*TermListTermList).Slots) == 0;  }; func tom_append_list_termList(l1  TermList , l2  TermList )  TermList  { if tom_is_empty_termList_TermList(l1) { return l2 } else if tom_is_empty_termList_TermList(l2) { return l1 } else if tom_is_empty_termList_TermList(tom_get_tail_termList_TermList(l1)) { return tom_cons_list_termList(tom_get_head_termList_TermList(l1), l2) } else { return tom_cons_list_termList(tom_get_head_termList_TermList(l1), tom_append_list_termList(tom_get_tail_termList_TermList(l1), l2)) }; }; func tom_get_slice_termList(begin  TermList , end  TermList , tail  TermList )  TermList  { if tom_equal_term_TermList(begin, end) { return tail } else if tom_equal_term_TermList(end, tail) && (tom_is_empty_termList_TermList(end) || tom_equal_term_TermList(end, tom_empty_list_termList())) { return begin }; return tom_cons_list_termList(tom_get_head_termList_TermList(begin), tom_get_slice_termList(tom_get_tail_termList_TermList(begin), end, tail)) }; 














func TestSublist1(t *testing.T) {
	var res Term = tom_cons_list_list(tom_make_a(),tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_c(),tom_empty_list_list())))
	matched := false
	; { { if tom_is_sort_Term(res) { if tom_is_fun_sym_list(any(res).( Term )) { var tomMatch1_end_5  Term  = any(res).( Term ); _ = tomMatch1_end_5; for { { var tomMatch1_end_8  Term  = tomMatch1_end_5; _ = tomMatch1_end_8; for { { var tomMatch1_1  Term  = tom_get_slice_list(tomMatch1_end_5,tomMatch1_end_8,tom_empty_list_list()); _ = tomMatch1_1; if tom_is_fun_sym_list(any(tomMatch1_1).( Term )) { if !( (tom_is_empty_list_Term(tomMatch1_1) || tom_equal_term_Term(tomMatch1_1, tom_empty_list_list())) ) { if tom_is_fun_sym_a(any(func()  Term  { if tom_is_fun_sym_list(tomMatch1_1) { return tom_get_head_list_Term(tomMatch1_1) }; return tomMatch1_1 }()).( Term )) { var tomMatch1_11  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch1_1) { return tom_get_tail_list_Term(tomMatch1_1) }; return tom_empty_list_list() }(); _ = tomMatch1_11; if !( (tom_is_empty_list_Term(tomMatch1_11) || tom_equal_term_Term(tomMatch1_11, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch1_11) { return tom_get_head_list_Term(tomMatch1_11) }; return tomMatch1_11 }()).( Term )) { var tomMatch1_12  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch1_11) { return tom_get_tail_list_Term(tomMatch1_11) }; return tom_empty_list_list() }(); _ = tomMatch1_12; if !( (tom_is_empty_list_Term(tomMatch1_12) || tom_equal_term_Term(tomMatch1_12, tom_empty_list_list())) ) { if tom_is_fun_sym_c(any(func()  Term  { if tom_is_fun_sym_list(tomMatch1_12) { return tom_get_head_list_Term(tomMatch1_12) }; return tomMatch1_12 }()).( Term )) { var tomMatch1_13  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch1_12) { return tom_get_tail_list_Term(tomMatch1_12) }; return tom_empty_list_list() }(); _ = tomMatch1_13; if  (tom_is_empty_list_Term(tomMatch1_13) || tom_equal_term_Term(tomMatch1_13, tom_empty_list_list()))  { 
 matched = true  };  };  };  };  };  };  };  }; if  (tom_is_empty_list_Term(tomMatch1_end_8) || tom_equal_term_Term(tomMatch1_end_8, tom_empty_list_list()))  { tomMatch1_end_8 = tomMatch1_end_5;  } else { tomMatch1_end_8 = func()  Term  { if tom_is_fun_sym_list(tomMatch1_end_8) { return tom_get_tail_list_Term(tomMatch1_end_8) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch1_end_8, tomMatch1_end_5))) { break; }; }; if  (tom_is_empty_list_Term(tomMatch1_end_5) || tom_equal_term_Term(tomMatch1_end_5, tom_empty_list_list()))  { tomMatch1_end_5 = any(res).( Term );  } else { tomMatch1_end_5 = func()  Term  { if tom_is_fun_sym_list(tomMatch1_end_5) { return tom_get_tail_list_Term(tomMatch1_end_5) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch1_end_5, any(res).( Term )))) { break; }; };  };  };  };  }; 

	if !matched {
		t.Errorf("test1: list(_*, list(a,b,c), _*) should match")
	}
}



func TestSublist2(t *testing.T) {
	var res Term = tom_cons_list_list(tom_make_a(),tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_c(),tom_empty_list_list())))
	matched := false
	; { { if tom_is_sort_Term(res) { if tom_is_fun_sym_list(any(res).( Term )) { var tomMatch2_end_5  Term  = any(res).( Term ); _ = tomMatch2_end_5; for { { var tomMatch2_end_8  Term  = tomMatch2_end_5; _ = tomMatch2_end_8; for { { var tomMatch2_1  Term  = tom_get_slice_list(tomMatch2_end_5,tomMatch2_end_8,tom_empty_list_list()); _ = tomMatch2_1; if tom_is_fun_sym_list(any(tomMatch2_1).( Term )) { if !( (tom_is_empty_list_Term(tomMatch2_1) || tom_equal_term_Term(tomMatch2_1, tom_empty_list_list())) ) { if tom_is_fun_sym_a(any(func()  Term  { if tom_is_fun_sym_list(tomMatch2_1) { return tom_get_head_list_Term(tomMatch2_1) }; return tomMatch2_1 }()).( Term )) { var tomMatch2_11  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch2_1) { return tom_get_tail_list_Term(tomMatch2_1) }; return tom_empty_list_list() }(); _ = tomMatch2_11; if !( (tom_is_empty_list_Term(tomMatch2_11) || tom_equal_term_Term(tomMatch2_11, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch2_11) { return tom_get_head_list_Term(tomMatch2_11) }; return tomMatch2_11 }()).( Term )) { var tomMatch2_12  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch2_11) { return tom_get_tail_list_Term(tomMatch2_11) }; return tom_empty_list_list() }(); _ = tomMatch2_12; if !( (tom_is_empty_list_Term(tomMatch2_12) || tom_equal_term_Term(tomMatch2_12, tom_empty_list_list())) ) { if tom_is_fun_sym_c(any(func()  Term  { if tom_is_fun_sym_list(tomMatch2_12) { return tom_get_head_list_Term(tomMatch2_12) }; return tomMatch2_12 }()).( Term )) { var tomMatch2_13  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch2_12) { return tom_get_tail_list_Term(tomMatch2_12) }; return tom_empty_list_list() }(); _ = tomMatch2_13; if  (tom_is_empty_list_Term(tomMatch2_13) || tom_equal_term_Term(tomMatch2_13, tom_empty_list_list()))  { 
 matched = true  };  };  };  };  };  };  };  }; if  (tom_is_empty_list_Term(tomMatch2_end_8) || tom_equal_term_Term(tomMatch2_end_8, tom_empty_list_list()))  { tomMatch2_end_8 = tomMatch2_end_5;  } else { tomMatch2_end_8 = func()  Term  { if tom_is_fun_sym_list(tomMatch2_end_8) { return tom_get_tail_list_Term(tomMatch2_end_8) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch2_end_8, tomMatch2_end_5))) { break; }; }; if  (tom_is_empty_list_Term(tomMatch2_end_5) || tom_equal_term_Term(tomMatch2_end_5, tom_empty_list_list()))  { tomMatch2_end_5 = any(res).( Term );  } else { tomMatch2_end_5 = func()  Term  { if tom_is_fun_sym_list(tomMatch2_end_5) { return tom_get_tail_list_Term(tomMatch2_end_5) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch2_end_5, any(res).( Term )))) { break; }; };  };  };  };  }; 

	if !matched {
		t.Errorf("test2: annotated sublist pattern should match")
	}
}



func TestSublist3(t *testing.T) {
	var res Term = tom_cons_list_list(tom_make_a(),tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_c(),tom_empty_list_list()))))
	matched := false
	bindings_ok := false
	; { { if tom_is_sort_Term(res) { if tom_is_fun_sym_list(any(res).( Term )) { var tomMatch3_end_5  Term  = any(res).( Term ); _ = tomMatch3_end_5; for { { var tomMatch3_end_8  Term  = tomMatch3_end_5; _ = tomMatch3_end_8; for { { var tomMatch3_1  Term  = tom_get_slice_list(tomMatch3_end_5,tomMatch3_end_8,tom_empty_list_list()); _ = tomMatch3_1; if tom_is_fun_sym_list(any(tomMatch3_1).( Term )) { if !( (tom_is_empty_list_Term(tomMatch3_1) || tom_equal_term_Term(tomMatch3_1, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch3_1) { return tom_get_head_list_Term(tomMatch3_1) }; return tomMatch3_1 }()).( Term )) { var tomMatch3_11  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch3_1) { return tom_get_tail_list_Term(tomMatch3_1) }; return tom_empty_list_list() }(); _ = tomMatch3_11; if !( (tom_is_empty_list_Term(tomMatch3_11) || tom_equal_term_Term(tomMatch3_11, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch3_11) { return tom_get_head_list_Term(tomMatch3_11) }; return tomMatch3_11 }()).( Term )) { var tomMatch3_12  Term  = func()  Term  { if tom_is_fun_sym_list(tomMatch3_11) { return tom_get_tail_list_Term(tomMatch3_11) }; return tom_empty_list_list() }(); _ = tomMatch3_12; if  (tom_is_empty_list_Term(tomMatch3_12) || tom_equal_term_Term(tomMatch3_12, tom_empty_list_list()))  { 

			matched = true
			if tomMatch3_1 == tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_b(),tom_empty_list_list())) && tom_get_slice_list(any(res).( Term ),tomMatch3_end_5,tom_empty_list_list()) == tom_cons_list_list(tom_make_a(),tom_empty_list_list()) && tomMatch3_end_8 == tom_cons_list_list(tom_make_c(),tom_empty_list_list()){ 
				bindings_ok = true
			 };  };  };  };  };  };  }; if  (tom_is_empty_list_Term(tomMatch3_end_8) || tom_equal_term_Term(tomMatch3_end_8, tom_empty_list_list()))  { tomMatch3_end_8 = tomMatch3_end_5;  } else { tomMatch3_end_8 = func()  Term  { if tom_is_fun_sym_list(tomMatch3_end_8) { return tom_get_tail_list_Term(tomMatch3_end_8) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch3_end_8, tomMatch3_end_5))) { break; }; }; if  (tom_is_empty_list_Term(tomMatch3_end_5) || tom_equal_term_Term(tomMatch3_end_5, tom_empty_list_list()))  { tomMatch3_end_5 = any(res).( Term );  } else { tomMatch3_end_5 = func()  Term  { if tom_is_fun_sym_list(tomMatch3_end_5) { return tom_get_tail_list_Term(tomMatch3_end_5) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch3_end_5, any(res).( Term )))) { break; }; };  };  };  };  }; 


	if !matched {
		t.Errorf("test3: anchored sublist should match")
	}
	if !bindings_ok {
		t.Errorf("test3: p/X/Y bindings wrong")
	}
}







func TestSublist5(t *testing.T) {
	var res TermList = tom_cons_list_termList(tom_make_a(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_c(),tom_empty_list_termList())))); { { if tom_is_sort_TermList(res) { if tom_is_fun_sym_termList(any(res).( TermList )) { var tomMatch4_end_4  TermList  = any(res).( TermList ); _ = tomMatch4_end_4; for { { if !(tom_is_empty_termList_TermList(tomMatch4_end_4)) { var tomMatch4_7  Term  = tom_get_head_termList_TermList(tomMatch4_end_4); _ = tomMatch4_7; if tom_is_fun_sym_list(any(tomMatch4_7).( Term )) { var tomMatch4_end_10  Term  = tomMatch4_7; _ = tomMatch4_end_10; for { { if !( (tom_is_empty_list_Term(tomMatch4_end_10) || tom_equal_term_Term(tomMatch4_end_10, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch4_end_10) { return tom_get_head_list_Term(tomMatch4_end_10) }; return tomMatch4_end_10 }()).( Term )) { 


			t.Errorf("test5: inner list pattern shouldn't match atom elements")
		 };  }; if  (tom_is_empty_list_Term(tomMatch4_end_10) || tom_equal_term_Term(tomMatch4_end_10, tom_empty_list_list()))  { tomMatch4_end_10 = tomMatch4_7;  } else { tomMatch4_end_10 = func()  Term  { if tom_is_fun_sym_list(tomMatch4_end_10) { return tom_get_tail_list_Term(tomMatch4_end_10) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch4_end_10, tomMatch4_7))) { break; }; };  };  }; if tom_is_empty_termList_TermList(tomMatch4_end_4) { tomMatch4_end_4 = any(res).( TermList );  } else { tomMatch4_end_4 = tom_get_tail_termList_TermList(tomMatch4_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch4_end_4, any(res).( TermList )))) { break; }; };  };  };  };  }; 

}



func TestSublist6(t *testing.T) {
	var res TermList = tom_cons_list_termList(tom_make_a(),tom_cons_list_termList(tom_cons_list_list(tom_make_b(),tom_cons_list_list(tom_make_b(),tom_empty_list_list())),tom_cons_list_termList(tom_make_c(),tom_empty_list_termList())))
	matched := false
	; { { if tom_is_sort_TermList(res) { if tom_is_fun_sym_termList(any(res).( TermList )) { var tomMatch5_end_4  TermList  = any(res).( TermList ); _ = tomMatch5_end_4; for { { if !(tom_is_empty_termList_TermList(tomMatch5_end_4)) { var tomMatch5_7  Term  = tom_get_head_termList_TermList(tomMatch5_end_4); _ = tomMatch5_7; if tom_is_fun_sym_list(any(tomMatch5_7).( Term )) { var tomMatch5_end_10  Term  = tomMatch5_7; _ = tomMatch5_end_10; for { { if !( (tom_is_empty_list_Term(tomMatch5_end_10) || tom_equal_term_Term(tomMatch5_end_10, tom_empty_list_list())) ) { if tom_is_fun_sym_b(any(func()  Term  { if tom_is_fun_sym_list(tomMatch5_end_10) { return tom_get_head_list_Term(tomMatch5_end_10) }; return tomMatch5_end_10 }()).( Term )) { 
 matched = true  };  }; if  (tom_is_empty_list_Term(tomMatch5_end_10) || tom_equal_term_Term(tomMatch5_end_10, tom_empty_list_list()))  { tomMatch5_end_10 = tomMatch5_7;  } else { tomMatch5_end_10 = func()  Term  { if tom_is_fun_sym_list(tomMatch5_end_10) { return tom_get_tail_list_Term(tomMatch5_end_10) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_Term(tomMatch5_end_10, tomMatch5_7))) { break; }; };  };  }; if tom_is_empty_termList_TermList(tomMatch5_end_4) { tomMatch5_end_4 = any(res).( TermList );  } else { tomMatch5_end_4 = tom_get_tail_termList_TermList(tomMatch5_end_4);  };  };  if !(!(tom_equal_term_TermList(tomMatch5_end_4, any(res).( TermList )))) { break; }; };  };  };  };  }; 

	if !matched {
		t.Errorf("test6: inner list pattern should match the list element")
	}
}



func TestSublist7(t *testing.T) {
	var res TermList = tom_cons_list_termList(tom_make_a(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_c(),tom_empty_list_termList()))))
	matched := false
	; { { if tom_is_sort_TermList(res) { if tom_is_fun_sym_termList(any(res).( TermList )) { var tomMatch6_end_5  TermList  = any(res).( TermList ); _ = tomMatch6_end_5; for { { var tomMatch6_end_8  TermList  = tomMatch6_end_5; _ = tomMatch6_end_8; for { { var tomMatch6_1  TermList  = tom_get_slice_termList(tomMatch6_end_5,tomMatch6_end_8,tom_empty_list_termList()); _ = tomMatch6_1; if tom_is_fun_sym_termList(any(tomMatch6_1).( TermList )) { var tomMatch6_end_13  TermList  = tomMatch6_1; _ = tomMatch6_end_13; for { { if !(tom_is_empty_termList_TermList(tomMatch6_end_13)) { if tom_is_fun_sym_b(any(tom_get_head_termList_TermList(tomMatch6_end_13)).( Term )) { 
 matched = true  };  }; if tom_is_empty_termList_TermList(tomMatch6_end_13) { tomMatch6_end_13 = tomMatch6_1;  } else { tomMatch6_end_13 = tom_get_tail_termList_TermList(tomMatch6_end_13);  };  };  if !(!(tom_equal_term_TermList(tomMatch6_end_13, tomMatch6_1))) { break; }; };  }; if tom_is_empty_termList_TermList(tomMatch6_end_8) { tomMatch6_end_8 = tomMatch6_end_5;  } else { tomMatch6_end_8 = tom_get_tail_termList_TermList(tomMatch6_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch6_end_8, tomMatch6_end_5))) { break; }; }; if tom_is_empty_termList_TermList(tomMatch6_end_5) { tomMatch6_end_5 = any(res).( TermList );  } else { tomMatch6_end_5 = tom_get_tail_termList_TermList(tomMatch6_end_5);  };  };  if !(!(tom_equal_term_TermList(tomMatch6_end_5, any(res).( TermList )))) { break; }; };  };  };  };  }; 

	if !matched {
		t.Errorf("test7: termList sub-range should match")
	}
}





func TestSublist9(t *testing.T) {
	var res TermList = tom_cons_list_termList(tom_make_a(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_c(),tom_empty_list_termList()))))
	matched := false
	binding_ok := false
	; { { if tom_is_sort_TermList(res) { if tom_is_fun_sym_termList(any(res).( TermList )) { var tomMatch7_end_5  TermList  = any(res).( TermList ); _ = tomMatch7_end_5; for { { var tomMatch7_end_8  TermList  = tomMatch7_end_5; _ = tomMatch7_end_8; for { { var tomMatch7_1  TermList  = tom_get_slice_termList(tomMatch7_end_5,tomMatch7_end_8,tom_empty_list_termList()); _ = tomMatch7_1; if tom_is_fun_sym_termList(any(tomMatch7_1).( TermList )) { if !(tom_is_empty_termList_TermList(tomMatch7_1)) { if tom_is_fun_sym_b(any(tom_get_head_termList_TermList(tomMatch7_1)).( Term )) { var tomMatch7_11  TermList  = tom_get_tail_termList_TermList(tomMatch7_1); _ = tomMatch7_11; if !(tom_is_empty_termList_TermList(tomMatch7_11)) { if tom_is_fun_sym_b(any(tom_get_head_termList_TermList(tomMatch7_11)).( Term )) { if tom_is_empty_termList_TermList(tom_get_tail_termList_TermList(tomMatch7_11)) { 

			matched = true
			if tomMatch7_1 == tom_cons_list_termList(tom_make_b(),tom_cons_list_termList(tom_make_b(),tom_empty_list_termList())){ 
				binding_ok = true
			 };  };  };  };  };  };  }; if tom_is_empty_termList_TermList(tomMatch7_end_8) { tomMatch7_end_8 = tomMatch7_end_5;  } else { tomMatch7_end_8 = tom_get_tail_termList_TermList(tomMatch7_end_8);  };  };  if !(!(tom_equal_term_TermList(tomMatch7_end_8, tomMatch7_end_5))) { break; }; }; if tom_is_empty_termList_TermList(tomMatch7_end_5) { tomMatch7_end_5 = any(res).( TermList );  } else { tomMatch7_end_5 = tom_get_tail_termList_TermList(tomMatch7_end_5);  };  };  if !(!(tom_equal_term_TermList(tomMatch7_end_5, any(res).( TermList )))) { break; }; };  };  };  };  }; 


	if !matched {
		t.Errorf("test9: annotated termList sub-range should match")
	}
	if !binding_ok {
		t.Errorf("test9: p binding wrong")
	}
}




