package andor


















import (
  "testing"
)

  func tom_TestAndOrConstraintOnly_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestAndOrConstraintOnly_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestAndOrConstraintOnly_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_TestAndOrConstraintOnly_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_TestAndOrConstraintOnly_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_TestAndOrConstraintOnly_make_a()  Term  { return (MakeA()) }; func tom_TestAndOrConstraintOnly_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_TestAndOrConstraintOnly_make_b()  Term  { return (MakeB()) }; func tom_TestAndOrConstraintOnly_make_c()  Term  { return (MakeC()) }; func tom_TestAndOrConstraintOnly_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_TestAndOrConstraintOnly_empty_list_list()  Term  { return (EmptyList()) }; func tom_TestAndOrConstraintOnly_cons_list_list(e  Term , l  Term )  Term  { return (ConsList(e,l)) }; func tom_TestAndOrConstraintOnly_get_head_list_Term(l  Term )  Term  { return (l).GetHeadlist();  }; func tom_TestAndOrConstraintOnly_get_tail_list_Term(l  Term )  Term  { return (l).GetTaillist();  }; func tom_TestAndOrConstraintOnly_is_empty_list_Term(l  Term ) bool { return (l).IsEmptylist();  }; func tom_TestAndOrConstraintOnly_append_list_list(l1  Term , l2  Term )  Term  { if tom_TestAndOrConstraintOnly_is_empty_list_Term(l1) { return l2 } else if tom_TestAndOrConstraintOnly_is_empty_list_Term(l2) { return l1 } else if tom_TestAndOrConstraintOnly_is_fun_sym_list(l1) { if tom_TestAndOrConstraintOnly_is_empty_list_Term(tom_TestAndOrConstraintOnly_get_tail_list_Term(l1)) { return tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_get_head_list_Term(l1), l2) } else { return tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_get_head_list_Term(l1), tom_TestAndOrConstraintOnly_append_list_list(tom_TestAndOrConstraintOnly_get_tail_list_Term(l1), l2)) } } else { return tom_TestAndOrConstraintOnly_cons_list_list(l1, l2) }; }; func tom_TestAndOrConstraintOnly_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_TestAndOrConstraintOnly_equal_term_Term(begin, end) { return tail } else if tom_TestAndOrConstraintOnly_equal_term_Term(end, tail) && (tom_TestAndOrConstraintOnly_is_empty_list_Term(end) || tom_TestAndOrConstraintOnly_equal_term_Term(end, tom_TestAndOrConstraintOnly_empty_list_list())) { return begin }; return tom_TestAndOrConstraintOnly_cons_list_list(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(begin) { return tom_TestAndOrConstraintOnly_get_head_list_Term(begin) }; return begin }(), tom_TestAndOrConstraintOnly_get_slice_list(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(begin) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(begin) }; return tom_TestAndOrConstraintOnly_empty_list_list() }(), end, tail)) }; func tom_TestAndOrConstraintOnly_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_TestAndOrConstraintOnly_make_f(t1  Term , t2  Term )  Term  { return (MakeF(t1,t2)) }; func tom_TestAndOrConstraintOnly_get_slot_f_t1(t  Term )  Term  { return (t).(*FTerm).T1;  }; func tom_TestAndOrConstraintOnly_get_slot_f_t2(t  Term )  Term  { return (t).(*FTerm).T2;  }; func tom_TestAndOrConstraintOnly_is_fun_sym_g(t  Term ) bool { return func() bool { _, ok := (t).(*GTerm); return ok }();  }; func tom_TestAndOrConstraintOnly_make_g(t1  Term )  Term  { return (MakeG(t1)) }; func tom_TestAndOrConstraintOnly_get_slot_g_t1(t  Term )  Term  { return (t).(*GTerm).T1;  }; func tom_TestAndOrConstraintOnly_is_fun_sym_termList(t  TermList ) bool { return func() bool { _, ok := (t).(*TermListTermList); return ok }();  }; func tom_TestAndOrConstraintOnly_empty_list_termList()  TermList  { return (EmptyTermList()) }; func tom_TestAndOrConstraintOnly_cons_list_termList(e  Term , l  TermList )  TermList  { return (ConsTermList(e,l)) }; func tom_TestAndOrConstraintOnly_get_head_termList_TermList(l  TermList )  Term  { return (l).GetHeadtermList();  }; func tom_TestAndOrConstraintOnly_get_tail_termList_TermList(l  TermList )  TermList  { return (l).GetTailtermList();  }; func tom_TestAndOrConstraintOnly_is_empty_termList_TermList(l  TermList ) bool { return (l).IsEmptytermList();  }; func tom_TestAndOrConstraintOnly_append_list_termList(l1  TermList , l2  TermList )  TermList  { if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(l1) { return l2 } else if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(l2) { return l1 } else if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tom_TestAndOrConstraintOnly_get_tail_termList_TermList(l1)) { return tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_get_head_termList_TermList(l1), l2) } else { return tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_get_head_termList_TermList(l1), tom_TestAndOrConstraintOnly_append_list_termList(tom_TestAndOrConstraintOnly_get_tail_termList_TermList(l1), l2)) }; }; func tom_TestAndOrConstraintOnly_get_slice_termList(begin  TermList , end  TermList , tail  TermList )  TermList  { if tom_TestAndOrConstraintOnly_equal_term_TermList(begin, end) { return tail } else if tom_TestAndOrConstraintOnly_equal_term_TermList(end, tail) && (tom_TestAndOrConstraintOnly_is_empty_termList_TermList(end) || tom_TestAndOrConstraintOnly_equal_term_TermList(end, tom_TestAndOrConstraintOnly_empty_list_termList())) { return begin }; return tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_get_head_termList_TermList(begin), tom_TestAndOrConstraintOnly_get_slice_termList(tom_TestAndOrConstraintOnly_get_tail_termList_TermList(begin), end, tail)) }; 













func Test1(t *testing.T) {
    ; { { var tomMatch1_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch1_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch1_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch1_0).( Term )); var tomMatch1_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch1_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch1_1).( Term )) { 

        return
      ;  };  }; var tomMatch1_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch1_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch1_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test2(t *testing.T) {
    ; { { var tomMatch2_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch2_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch2_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch2_0).( Term )); var tomMatch2_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch2_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch2_1).( Term )) { 

        return
      ;  };  }; var tomMatch2_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch2_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch2_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test3(t *testing.T) {
    ; { { var tomMatch3_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch3_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch3_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch3_0).( Term )); var tomMatch3_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch3_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch3_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch3_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch3_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch3_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test4(t *testing.T) {
    ; { { var tomMatch4_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch4_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch4_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch4_0).( Term )); var tomMatch4_1  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch4_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch4_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch4_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch4_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch4_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test5(t *testing.T) {
    ; { { var tomMatch5_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch5_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch5_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch5_0).( Term )); var tomMatch5_1  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch5_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch5_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch5_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch5_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch5_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test6(t *testing.T) {
    ; { { var tomMatch6_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch6_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch6_0).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch6_0).( Term )); var tomMatch6_1  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch6_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch6_1).( Term )) { 

        return
      ;  };  }; var tomMatch6_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch6_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch6_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test7(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) { 


        fail(t)
      ;  };  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) {          fail(t);       ;  };  };  };  };  }; 

    return

}



func Test8(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tomMatch8_2  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term )); if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch8_2).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch8_2)).( Term )) { 


        return
      ;  };  };  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) {          return;       ;  };  };  };  };  }; 

    fail(t)
}



func Test9(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) { 


        return
      ;  };  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tomMatch9_8  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term )); if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch9_8).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch9_8)).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}

  



func Test10(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b())
    counter := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tomMatch10_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch10_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch10_1).( Term )) { 

        counter++
      ;  };  }; var tomMatch10_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch10_2) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tomMatch10_2).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test11(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b())
    counter := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tom____x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term )); var tomMatch11_1  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a())); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch11_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch11_1).( Term )) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, tom_TestAndOrConstraintOnly_get_slot_g_t1(any(tomMatch11_1).( Term ))) { 

        counter++
      ;  };  };  }; var tomMatch11_2  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch11_2) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom____x, any(tomMatch11_2).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test12(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_b()))
    counter := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { 

        counter++
      ;  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(s).( Term ))).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test13(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_b()))
    counter := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { 

        counter++
      ;  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tomMatch13_7  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term )); var tomMatch13_8  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t2(any(s).( Term )); if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch13_7).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch13_7)).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch13_8).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch13_8)).( Term )) {          counter++;       ;  };  };  };  };  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tom_TestAndOrConstraintOnly_get_slot_f_t1(any(s).( Term ))).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(s).( Term ))).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 3){
      fail(t)
    }
}



func Test14(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_b()))
    counter := 0; _ = counter
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(s).( Term )) { var tomMatch14_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch14_1) { var tom___z  Term  = any(tomMatch14_1).( Term ); 


        if (tom___z != tom_TestAndOrConstraintOnly_make_a() && tom___z != tom_TestAndOrConstraintOnly_make_b()){
          fail(t)
        };  }; var tomMatch14_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch14_2) { var tom___z  Term  = any(tomMatch14_2).( Term );          if (tom___z != tom_TestAndOrConstraintOnly_make_a() && tom___z != tom_TestAndOrConstraintOnly_make_b()){           fail(t);         };  };  };  };  };  }; 


}



func Test15(tt *testing.T) {
    var t Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(t) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(t).( Term )) { var tom___x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(t).( Term )); if tom_TestAndOrConstraintOnly_is_sort_Term(tom___x) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom___x).( Term )) { 


        if (tom___x != tom_TestAndOrConstraintOnly_make_a()){
          fail(tt)
        }
        return
      ;  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(tom___x) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tom___x).( Term )) {          if (tom___x != tom_TestAndOrConstraintOnly_make_a()){           fail(tt);         };         return;       ;  };  };  };  };  };  }; 

    fail(tt)
}



func Test16(tt *testing.T) {
    var t Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(t) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(t).( Term )) { var tom___x  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(t).( Term )); if tom_TestAndOrConstraintOnly_is_sort_Term(tom___x) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tom___x).( Term )) { 


        if (tom_TestAndOrConstraintOnly_get_slot_g_t1(any(tom___x).( Term )) != tom_TestAndOrConstraintOnly_make_a()) {
          fail(tt)
        }
        return
      ;  };  };  };  };  };  }; 

    fail(tt)
}



func Test17(tt *testing.T) {
    var t Term = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_make_b()); { { if tom_TestAndOrConstraintOnly_is_sort_Term(t) { var tomMatch17_freshSubject_0  Term  = any(t).( Term ); if tom_TestAndOrConstraintOnly_is_sort_Term(t) { tomMatch17_freshSubject_0 = any(t).( Term ); var tomMatch17_6  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t1(tomMatch17_freshSubject_0); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch17_6) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch17_6).( Term )) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch17_freshSubject_0).( Term )) { 


        if (tom_TestAndOrConstraintOnly_get_slot_g_t1(any(tomMatch17_6).( Term )) != tom_TestAndOrConstraintOnly_make_a()){
          fail(tt)
        }
        return
      ;  };  };  };  };  };  };  }; 

    fail(tt)
}



func Test18(t *testing.T) {
    var l Term = tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_empty_list_list())))); { { if tom_TestAndOrConstraintOnly_is_sort_Term(l) { if tom_TestAndOrConstraintOnly_is_fun_sym_list(any(l).( Term )) { var tomMatch18_end_5  Term  = any(l).( Term ); for { { if !( (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch18_end_5) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch18_end_5, tom_TestAndOrConstraintOnly_empty_list_list())) ) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOrConstraintOnly_get_head_list_Term(tomMatch18_end_5) }; return tomMatch18_end_5 }()).( Term )) { var tomMatch18_1  Term  = tom_TestAndOrConstraintOnly_append_list_list(tom_TestAndOrConstraintOnly_get_slice_list(any(l).( Term ),tomMatch18_end_5,tom_TestAndOrConstraintOnly_empty_list_list()),tom_TestAndOrConstraintOnly_append_list_list(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(tomMatch18_end_5) }; return tom_TestAndOrConstraintOnly_empty_list_list() }(),tom_TestAndOrConstraintOnly_empty_list_list())); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch18_1) { 


        if (any(tomMatch18_1).( Term ) != tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_empty_list_list())))){
          fail(t)
        }
        return
      ;  };  };  }; if  (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch18_end_5) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch18_end_5, tom_TestAndOrConstraintOnly_empty_list_list()))  { tomMatch18_end_5 = any(l).( Term );  } else { tomMatch18_end_5 = func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(tomMatch18_end_5) }; return tom_TestAndOrConstraintOnly_empty_list_list() }();  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch18_end_5, any(l).( Term )))) { break; }; };  };  };  };  }; 

    fail(t)
}



func Test19(t *testing.T) {
    var l Term = tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()),tom_TestAndOrConstraintOnly_cons_list_list(tom_TestAndOrConstraintOnly_make_c(),tom_TestAndOrConstraintOnly_empty_list_list())))); { { if tom_TestAndOrConstraintOnly_is_sort_Term(l) { if tom_TestAndOrConstraintOnly_is_fun_sym_list(any(l).( Term )) { var tomMatch19_end_5  Term  = any(l).( Term ); for { { if !( (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch19_end_5) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_5, tom_TestAndOrConstraintOnly_empty_list_list())) ) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOrConstraintOnly_get_head_list_Term(tomMatch19_end_5) }; return tomMatch19_end_5 }()).( Term )) { var tomMatch19_1  Term  = tom_TestAndOrConstraintOnly_append_list_list(tom_TestAndOrConstraintOnly_get_slice_list(any(l).( Term ),tomMatch19_end_5,tom_TestAndOrConstraintOnly_empty_list_list()),tom_TestAndOrConstraintOnly_append_list_list(func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(tomMatch19_end_5) }; return tom_TestAndOrConstraintOnly_empty_list_list() }(),tom_TestAndOrConstraintOnly_empty_list_list())); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch19_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_list(any(tomMatch19_1).( Term )) { var tomMatch19_end_11  Term  = any(tomMatch19_1).( Term ); for { { if !( (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch19_end_11) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_11, tom_TestAndOrConstraintOnly_empty_list_list())) ) { var tomMatch19_17  Term  = func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch19_end_11) { return tom_TestAndOrConstraintOnly_get_head_list_Term(tomMatch19_end_11) }; return tomMatch19_end_11 }(); if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch19_17).( Term )) { 


        if (tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch19_17) != tom_TestAndOrConstraintOnly_make_a()){
          fail(t)
        }
        return
      ;  };  }; if  (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch19_end_11) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_11, tom_TestAndOrConstraintOnly_empty_list_list()))  { tomMatch19_end_11 = any(tomMatch19_1).( Term );  } else { tomMatch19_end_11 = func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch19_end_11) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(tomMatch19_end_11) }; return tom_TestAndOrConstraintOnly_empty_list_list() }();  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_11, any(tomMatch19_1).( Term )))) { break; }; };  };  };  };  }; if  (tom_TestAndOrConstraintOnly_is_empty_list_Term(tomMatch19_end_5) || tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_5, tom_TestAndOrConstraintOnly_empty_list_list()))  { tomMatch19_end_5 = any(l).( Term );  } else { tomMatch19_end_5 = func()  Term  { if tom_TestAndOrConstraintOnly_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOrConstraintOnly_get_tail_list_Term(tomMatch19_end_5) }; return tom_TestAndOrConstraintOnly_empty_list_list() }();  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_Term(tomMatch19_end_5, any(l).( Term )))) { break; }; };  };  };  };  }; 

    fail(t)
}



func Test20(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_a()
    cnt := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(s).( Term )) { 
 cnt++; ;  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(s).( Term )) {  cnt++; ;  };  };  };  }; 

    if (cnt > 1) {
      fail(t)
    }
}



func Test21(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_a()
    cnt := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { 

        if (any(s).( Term ) != tom_TestAndOrConstraintOnly_make_a()) {
          fail(t)
        }
        cnt++
      ;  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) {          if (any(s).( Term ) != tom_TestAndOrConstraintOnly_make_a()) {           fail(t);         };         cnt++;       ;  };  };  }; 

    if (cnt != 2) {
      fail(t)
    }
}



func Test22(t *testing.T) {
    var s Term = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a())
    cnt := 0
    ; { { if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(s).( Term )) { 

        if (tom_TestAndOrConstraintOnly_get_slot_g_t1(any(s).( Term )) != tom_TestAndOrConstraintOnly_make_a()) {
          fail(t)
        }
        cnt++
      ;  };  }; if tom_TestAndOrConstraintOnly_is_sort_Term(s) { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(s).( Term )) {          if (tom_TestAndOrConstraintOnly_get_slot_g_t1(any(s).( Term )) != tom_TestAndOrConstraintOnly_make_a()) {           fail(t);         };         cnt++;       ;  };  };  };  }; 

    if (cnt != 2) {
      fail(t)
    }
}




func Test23(t *testing.T) {
    ; { { var tomMatch23_0  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch23_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch23_0).( Term )) { 

        return
      ;  };  }; var tomMatch23_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch23_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch23_1).( Term )) { var tomMatch23_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch23_2) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch23_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test24(t *testing.T) {
    ; { { var tomMatch24_0  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch24_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch24_0).( Term )) { var tomMatch24_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch24_2) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch24_2).( Term )) { 

        fail(t)
      ;  };  };  };  }; var tomMatch24_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch24_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch24_1).( Term )) { var tomMatch24_2  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch24_2) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch24_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test25(t *testing.T) {
    ; { { var tomMatch25_0  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch25_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch25_0).( Term )) { var tomMatch25_1  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch25_1) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch25_1).( Term )) { 

        return
      ;  };  };  };  }; var tomMatch25_2  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch25_2) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch25_2).( Term )) { var tomMatch25_3  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch25_3) { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tomMatch25_3).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test26(t *testing.T) {
    cpt := 0
    ; { { var tomMatch26_0  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch26_0) { var tomMatch26_4 bool =  false ; var tomMatch26_3  Term  =  *new(Term) ; _ = tomMatch26_3; var tomMatch26_2  Term  =  *new(Term) ; _ = tomMatch26_2; if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch26_0).( Term )) { { tomMatch26_4 =  true ; tomMatch26_2 = any(tomMatch26_0).( Term );  };  } else { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tomMatch26_0).( Term )) { { tomMatch26_4 =  true ; tomMatch26_3 = any(tomMatch26_0).( Term );  };  };  }; if tomMatch26_4 { 
 cpt++; ;  };  };  }; { var tomMatch26_5  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch26_5) { var tomMatch26_10 bool =  false ; var tomMatch26_6  Term  =  *new(Term) ; _ = tomMatch26_6; var tomMatch26_9  Term  =  *new(Term) ; var tomMatch26_8  Term  =  *new(Term) ; if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch26_5).( Term )) { { tomMatch26_10 =  true ; tomMatch26_8 = any(tomMatch26_5).( Term ); tomMatch26_6 = tom_TestAndOrConstraintOnly_get_slot_f_t1(tomMatch26_8);  };  } else { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch26_5).( Term )) { { tomMatch26_10 =  true ; tomMatch26_9 = any(tomMatch26_5).( Term ); tomMatch26_6 = tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch26_9);  };  };  }; if tomMatch26_10 { 
 cpt++; ;  };  };  }; { var tomMatch26_11  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch26_11) { var tomMatch26_15 bool =  false ; var tomMatch26_13  Term  =  *new(Term) ; _ = tomMatch26_13; var tomMatch26_14  Term  =  *new(Term) ; _ = tomMatch26_14; if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch26_11).( Term )) { { tomMatch26_15 =  true ; tomMatch26_13 = any(tomMatch26_11).( Term );  };  } else { if tom_TestAndOrConstraintOnly_is_fun_sym_b(any(tomMatch26_11).( Term )) { { tomMatch26_15 =  true ; tomMatch26_14 = any(tomMatch26_11).( Term );  };  };  }; if tomMatch26_15 { 
 cpt++; ;  };  };  }; { var tomMatch26_16  Term  = tom_TestAndOrConstraintOnly_make_g(tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch26_16) { var tomMatch26_21 bool =  false ; var tomMatch26_17  Term  =  *new(Term) ; _ = tomMatch26_17; var tomMatch26_19  Term  =  *new(Term) ; var tomMatch26_20  Term  =  *new(Term) ; if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch26_16).( Term )) { { tomMatch26_21 =  true ; tomMatch26_19 = any(tomMatch26_16).( Term ); tomMatch26_17 = tom_TestAndOrConstraintOnly_get_slot_f_t1(tomMatch26_19);  };  } else { if tom_TestAndOrConstraintOnly_is_fun_sym_g(any(tomMatch26_16).( Term )) { { tomMatch26_21 =  true ; tomMatch26_20 = any(tomMatch26_16).( Term ); tomMatch26_17 = tom_TestAndOrConstraintOnly_get_slot_g_t1(tomMatch26_20);  };  };  }; if tomMatch26_21 { 
 cpt++; ;  };  };  };  }; 

    if(cpt!=4) {
      fail(t)
    }
}



func Test27(t *testing.T) {
    cpt := 0
    ; { { var tomMatch27_0  TermList  = tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_empty_list_termList())); if tom_TestAndOrConstraintOnly_is_sort_TermList(tomMatch27_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_termList(any(tomMatch27_0).( TermList )) { var tomMatch27_end_4  TermList  = any(tomMatch27_0).( TermList ); for { { if !(tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_4)) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_head_termList_TermList(tomMatch27_end_4)).( Term )) { 
 cpt++; ;  };  }; if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_4) { tomMatch27_end_4 = any(tomMatch27_0).( TermList );  } else { tomMatch27_end_4 = tom_TestAndOrConstraintOnly_get_tail_termList_TermList(tomMatch27_end_4);  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_TermList(tomMatch27_end_4, any(tomMatch27_0).( TermList )))) { break; }; };  };  };  }; { var tomMatch27_9  TermList  = tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_empty_list_termList())); if tom_TestAndOrConstraintOnly_is_sort_TermList(tomMatch27_9) { if tom_TestAndOrConstraintOnly_is_fun_sym_termList(any(tomMatch27_9).( TermList )) { var tomMatch27_end_13  TermList  = any(tomMatch27_9).( TermList ); for { { if !(tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_13)) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_head_termList_TermList(tomMatch27_end_13)).( Term )) { 
 cpt++; ;  };  }; if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_13) { tomMatch27_end_13 = any(tomMatch27_9).( TermList );  } else { tomMatch27_end_13 = tom_TestAndOrConstraintOnly_get_tail_termList_TermList(tomMatch27_end_13);  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_TermList(tomMatch27_end_13, any(tomMatch27_9).( TermList )))) { break; }; };  };  };  }; { var tomMatch27_18  TermList  = tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_cons_list_termList(tom_TestAndOrConstraintOnly_make_b(),tom_TestAndOrConstraintOnly_empty_list_termList())); if tom_TestAndOrConstraintOnly_is_sort_TermList(tomMatch27_18) { if tom_TestAndOrConstraintOnly_is_fun_sym_termList(any(tomMatch27_18).( TermList )) { var tomMatch27_end_22  TermList  = any(tomMatch27_18).( TermList ); for { { if !(tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_22)) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tom_TestAndOrConstraintOnly_get_head_termList_TermList(tomMatch27_end_22)).( Term )) { 
 cpt++; ;  };  }; if tom_TestAndOrConstraintOnly_is_empty_termList_TermList(tomMatch27_end_22) { tomMatch27_end_22 = any(tomMatch27_18).( TermList );  } else { tomMatch27_end_22 = tom_TestAndOrConstraintOnly_get_tail_termList_TermList(tomMatch27_end_22);  };  };  if !(!(tom_TestAndOrConstraintOnly_equal_term_TermList(tomMatch27_end_22, any(tomMatch27_18).( TermList )))) { break; }; };  };  };  };  }; 

    if(cpt!=3) {
      fail(t)
    }
}




func Test28(t *testing.T) {
    ; { { var tomMatch28_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch28_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch28_0).( Term )) { 

        var tmp any = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch28_0).( Term )); _ = tmp
        return
      ;  };  }; var tomMatch28_1  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch28_1) {          var tmp any = any(tomMatch28_1).( Term ); _ = tmp;         return;       ;  };  };  }; 

    fail(t)
}



func Test29(t *testing.T) {
    ; { { var tomMatch29_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch29_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch29_0).( Term )) { var tomMatch29_1  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch29_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(tomMatch29_0).( Term )), any(tomMatch29_1).( Term )) { 

        var tmp any = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch29_0).( Term )); _ = tmp
        return
      ;  };  };  };  }; var tomMatch29_2  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch29_2) {          var tmp any = any(tomMatch29_2).( Term ); _ = tmp;         return;       ;  };  };  }; 

    fail(t)
}



func Test30(t *testing.T) {
    ; { { var tomMatch30_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch30_0).( Term )) { var tomMatch30_1  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_1) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(tomMatch30_0).( Term )), any(tomMatch30_1).( Term )) { 

        var tmp any = tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch30_0).( Term )); _ = tmp
        return
      ;  };  };  };  }; var tomMatch30_2  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_2) {          var tmp any = any(tomMatch30_2).( Term ); _ = tmp;         return;       ;  };  }; { var tomMatch30_8  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_8) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch30_8).( Term )) { var tomMatch30_9  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_9) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(tomMatch30_8).( Term )), any(tomMatch30_9).( Term )) { 

        return
      ;  };  };  };  }; var tomMatch30_10  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_10) { if tom_TestAndOrConstraintOnly_is_fun_sym_a(any(tomMatch30_10).( Term )) {          return;       ;  };  };  }; { var tomMatch30_18  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_b()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_18) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch30_18).( Term )) { var tom___y  Term  = tom_TestAndOrConstraintOnly_get_slot_f_t2(any(tomMatch30_18).( Term )); var tomMatch30_19  Term  = tom_TestAndOrConstraintOnly_make_b(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_19) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom___y, any(tomMatch30_19).( Term )) { 

        var tmp any = tom___y; _ = tmp
        return
      ;  };  };  };  }; var tomMatch30_20  Term  = tom_TestAndOrConstraintOnly_make_a(); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch30_20) {          var tmp any = any(tomMatch30_20).( Term ); _ = tmp;         return;       ;  };  };  }; 

    fail(t)
}



func Test31(t *testing.T) {
    ; { { var tomMatch31_0  Term  = tom_TestAndOrConstraintOnly_make_f(tom_TestAndOrConstraintOnly_make_a(),tom_TestAndOrConstraintOnly_make_a()); if tom_TestAndOrConstraintOnly_is_sort_Term(tomMatch31_0) { if tom_TestAndOrConstraintOnly_is_fun_sym_f(any(tomMatch31_0).( Term )) { if tom_TestAndOrConstraintOnly_equal_term_Term(tom_TestAndOrConstraintOnly_get_slot_f_t2(any(tomMatch31_0).( Term )), tom_TestAndOrConstraintOnly_get_slot_f_t1(any(tomMatch31_0).( Term ))) { 

        return
      ;  };  };  };  };  }; 

    fail(t)
}




func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
