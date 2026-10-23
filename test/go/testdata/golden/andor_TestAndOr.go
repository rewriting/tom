package andor
















import (
  "testing"
)

  func tom_TestAndOr_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestAndOr_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestAndOr_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_TestAndOr_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_TestAndOr_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_TestAndOr_make_a()  Term  { return (MakeA()) }; func tom_TestAndOr_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_TestAndOr_make_b()  Term  { return (MakeB()) }; func tom_TestAndOr_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_TestAndOr_make_c()  Term  { return (MakeC()) }; func tom_TestAndOr_is_fun_sym_list(t  Term ) bool { return func() bool { _, ok := (t).(*ListTerm); return ok }();  }; func tom_TestAndOr_empty_list_list()  Term  { return (EmptyList()) }; func tom_TestAndOr_cons_list_list(e  Term , l  Term )  Term  { return (ConsList(e,l)) }; func tom_TestAndOr_get_head_list_Term(l  Term )  Term  { return (l).GetHeadlist();  }; func tom_TestAndOr_get_tail_list_Term(l  Term )  Term  { return (l).GetTaillist();  }; func tom_TestAndOr_is_empty_list_Term(l  Term ) bool { return (l).IsEmptylist();  }; func tom_TestAndOr_append_list_list(l1  Term , l2  Term )  Term  { if tom_TestAndOr_is_empty_list_Term(l1) { return l2 } else if tom_TestAndOr_is_empty_list_Term(l2) { return l1 } else if tom_TestAndOr_is_fun_sym_list(l1) { if tom_TestAndOr_is_empty_list_Term(tom_TestAndOr_get_tail_list_Term(l1)) { return tom_TestAndOr_cons_list_list(tom_TestAndOr_get_head_list_Term(l1), l2) } else { return tom_TestAndOr_cons_list_list(tom_TestAndOr_get_head_list_Term(l1), tom_TestAndOr_append_list_list(tom_TestAndOr_get_tail_list_Term(l1), l2)) } } else { return tom_TestAndOr_cons_list_list(l1, l2) }; }; func tom_TestAndOr_get_slice_list(begin  Term , end  Term , tail  Term )  Term  { if tom_TestAndOr_equal_term_Term(begin, end) { return tail } else if tom_TestAndOr_equal_term_Term(end, tail) && (tom_TestAndOr_is_empty_list_Term(end) || tom_TestAndOr_equal_term_Term(end, tom_TestAndOr_empty_list_list())) { return begin }; return tom_TestAndOr_cons_list_list(func()  Term  { if tom_TestAndOr_is_fun_sym_list(begin) { return tom_TestAndOr_get_head_list_Term(begin) }; return begin }(), tom_TestAndOr_get_slice_list(func()  Term  { if tom_TestAndOr_is_fun_sym_list(begin) { return tom_TestAndOr_get_tail_list_Term(begin) }; return tom_TestAndOr_empty_list_list() }(), end, tail)) }; func tom_TestAndOr_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_TestAndOr_make_f(t1  Term , t2  Term )  Term  { return (MakeF(t1,t2)) }; func tom_TestAndOr_get_slot_f_t1(t  Term )  Term  { return (t).(*FTerm).T1;  }; func tom_TestAndOr_get_slot_f_t2(t  Term )  Term  { return (t).(*FTerm).T2;  }; func tom_TestAndOr_is_fun_sym_g(t  Term ) bool { return func() bool { _, ok := (t).(*GTerm); return ok }();  }; func tom_TestAndOr_make_g(t  Term )  Term  { return (MakeG(t)) }; func tom_TestAndOr_get_slot_g_t(t  Term )  Term  { return (t).(*GTerm).T;  }; 













func Test1(t *testing.T) {
    ; { { var tomMatch1_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch1_0; if tom_TestAndOr_is_sort_Term(tomMatch1_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch1_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch1_0).( Term )); _ = tom____x; var tomMatch1_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch1_1; if tom_TestAndOr_is_sort_Term(tomMatch1_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch1_1).( Term )) { 

        return
      ;  };  }; var tomMatch1_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch1_2; if tom_TestAndOr_is_sort_Term(tomMatch1_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch1_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test2(t *testing.T) {
    ; { { var tomMatch2_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_b(),tom_TestAndOr_make_b()); _ = tomMatch2_0; if tom_TestAndOr_is_sort_Term(tomMatch2_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch2_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch2_0).( Term )); _ = tom____x; var tomMatch2_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch2_1; if tom_TestAndOr_is_sort_Term(tomMatch2_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch2_1).( Term )) { 

        return
      ;  };  }; var tomMatch2_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch2_2; if tom_TestAndOr_is_sort_Term(tomMatch2_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch2_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test3(t *testing.T) {
    ; { { var tomMatch3_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_c(),tom_TestAndOr_make_b()); _ = tomMatch3_0; if tom_TestAndOr_is_sort_Term(tomMatch3_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch3_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch3_0).( Term )); _ = tom____x; var tomMatch3_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch3_1; if tom_TestAndOr_is_sort_Term(tomMatch3_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch3_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch3_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch3_2; if tom_TestAndOr_is_sort_Term(tomMatch3_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch3_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test4(t *testing.T) {
    ; { { var tomMatch4_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_c(),tom_TestAndOr_make_b()); _ = tomMatch4_0; if tom_TestAndOr_is_sort_Term(tomMatch4_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch4_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch4_0).( Term )); _ = tom____x; var tomMatch4_1  Term  = tom_TestAndOr_make_g(tom_TestAndOr_make_a()); _ = tomMatch4_1; if tom_TestAndOr_is_sort_Term(tomMatch4_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch4_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch4_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch4_2; if tom_TestAndOr_is_sort_Term(tomMatch4_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch4_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test5(t *testing.T) {
    ; { { var tomMatch5_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_c(),tom_TestAndOr_make_b()); _ = tomMatch5_0; if tom_TestAndOr_is_sort_Term(tomMatch5_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch5_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch5_0).( Term )); _ = tom____x; var tomMatch5_1  Term  = tom_TestAndOr_make_g(tom_TestAndOr_make_a()); _ = tomMatch5_1; if tom_TestAndOr_is_sort_Term(tomMatch5_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch5_1).( Term )) { 

        fail(t)
      ;  };  }; var tomMatch5_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch5_2; if tom_TestAndOr_is_sort_Term(tomMatch5_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch5_2).( Term )) {          fail(t);       ;  };  };  };  };  };  }; 

}



func Test6(t *testing.T) {
    ; { { var tomMatch6_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); _ = tomMatch6_0; if tom_TestAndOr_is_sort_Term(tomMatch6_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch6_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch6_0).( Term )); _ = tom____x; var tomMatch6_1  Term  = tom_TestAndOr_make_g(tom_TestAndOr_make_a()); _ = tomMatch6_1; if tom_TestAndOr_is_sort_Term(tomMatch6_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch6_1).( Term )) { 

        return
      ;  };  }; var tomMatch6_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch6_2; if tom_TestAndOr_is_sort_Term(tomMatch6_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch6_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test7(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_a(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) { 


        fail(t)
      ;  };  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_b(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) {          fail(t);       ;  };  };  };  };  }; 

    return

}



func Test8(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tomMatch8_2  Term  = tom_TestAndOr_get_slot_f_t1(any(s).( Term )); _ = tomMatch8_2; if tom_TestAndOr_is_fun_sym_g(any(tomMatch8_2).( Term )) { if tom_TestAndOr_is_fun_sym_a(any(tom_TestAndOr_get_slot_g_t(tomMatch8_2)).( Term )) { 


        return
      ;  };  };  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_b(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) {          return;       ;  };  };  };  };  }; 

    fail(t)
}



func Test9(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_b(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) { 


        return
      ;  };  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tomMatch9_8  Term  = tom_TestAndOr_get_slot_f_t1(any(s).( Term )); _ = tomMatch9_8; if tom_TestAndOr_is_fun_sym_g(any(tomMatch9_8).( Term )) { if tom_TestAndOr_is_fun_sym_a(any(tom_TestAndOr_get_slot_g_t(tomMatch9_8)).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}

  



func Test10(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b())
    counter := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tomMatch10_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch10_1; if tom_TestAndOr_is_sort_Term(tomMatch10_1) { if tom_TestAndOr_is_fun_sym_a(any(tomMatch10_1).( Term )) { 

        counter++
      ;  };  }; var tomMatch10_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch10_2; if tom_TestAndOr_is_sort_Term(tomMatch10_2) { if tom_TestAndOr_is_fun_sym_b(any(tomMatch10_2).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test11(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b())
    counter := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(s).( Term )); _ = tom____x; var tomMatch11_1  Term  = tom_TestAndOr_make_g(tom_TestAndOr_make_g(tom_TestAndOr_make_a())); _ = tomMatch11_1; if tom_TestAndOr_is_sort_Term(tomMatch11_1) { if tom_TestAndOr_is_fun_sym_g(any(tomMatch11_1).( Term )) { if tom_TestAndOr_equal_term_Term(tom____x, tom_TestAndOr_get_slot_g_t(any(tomMatch11_1).( Term ))) { 

        counter++
      ;  };  };  }; var tomMatch11_2  Term  = tom_TestAndOr_make_g(tom_TestAndOr_make_a()); _ = tomMatch11_2; if tom_TestAndOr_is_sort_Term(tomMatch11_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch11_2).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test12(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_g(tom_TestAndOr_make_b()))
    counter := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { 

        counter++
      ;  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_g(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) { if tom_TestAndOr_is_fun_sym_g(any(tom_TestAndOr_get_slot_f_t2(any(s).( Term ))).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 2){
      fail(t)
    }
}



func Test13(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_g(tom_TestAndOr_make_b()))
    counter := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { 

        counter++
      ;  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tomMatch13_7  Term  = tom_TestAndOr_get_slot_f_t1(any(s).( Term )); _ = tomMatch13_7; var tomMatch13_8  Term  = tom_TestAndOr_get_slot_f_t2(any(s).( Term )); _ = tomMatch13_8; if tom_TestAndOr_is_fun_sym_g(any(tomMatch13_7).( Term )) { if tom_TestAndOr_is_fun_sym_a(any(tom_TestAndOr_get_slot_g_t(tomMatch13_7)).( Term )) { if tom_TestAndOr_is_fun_sym_g(any(tomMatch13_8).( Term )) { if tom_TestAndOr_is_fun_sym_b(any(tom_TestAndOr_get_slot_g_t(tomMatch13_8)).( Term )) {          counter++;       ;  };  };  };  };  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { if tom_TestAndOr_is_fun_sym_g(any(tom_TestAndOr_get_slot_f_t1(any(s).( Term ))).( Term )) { if tom_TestAndOr_is_fun_sym_g(any(tom_TestAndOr_get_slot_f_t2(any(s).( Term ))).( Term )) {          counter++;       ;  };  };  };  };  };  }; 

    if (counter != 3){
      fail(t)
    }
}



func Test14(t *testing.T) {
    var s Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_g(tom_TestAndOr_make_b()))
    counter := 0; _ = counter
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_f(any(s).( Term )) { var tomMatch14_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch14_1; if tom_TestAndOr_is_sort_Term(tomMatch14_1) { var tom___z  Term  = any(tomMatch14_1).( Term ); _ = tom___z; 


        if (tom___z != tom_TestAndOr_make_a() && tom___z != tom_TestAndOr_make_b()){
          fail(t)
        };  }; var tomMatch14_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch14_2; if tom_TestAndOr_is_sort_Term(tomMatch14_2) { var tom___z  Term  = any(tomMatch14_2).( Term ); _ = tom___z;          if (tom___z != tom_TestAndOr_make_a() && tom___z != tom_TestAndOr_make_b()){           fail(t);         };  };  };  };  };  }; 


}



func Test15(tt *testing.T) {
    var t Term = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); { _ = t; { if tom_TestAndOr_is_sort_Term(t) { if tom_TestAndOr_is_fun_sym_f(any(t).( Term )) { var tom___x  Term  = tom_TestAndOr_get_slot_f_t1(any(t).( Term )); _ = tom___x; if tom_TestAndOr_is_sort_Term(tom___x) { if tom_TestAndOr_is_fun_sym_a(any(tom___x).( Term )) { 


        if (tom___x != tom_TestAndOr_make_a()){
          fail(tt)
        }
        return
      ;  };  }; if tom_TestAndOr_is_sort_Term(tom___x) { if tom_TestAndOr_is_fun_sym_b(any(tom___x).( Term )) {          if (tom___x != tom_TestAndOr_make_a()){           fail(tt);         };         return;       ;  };  };  };  };  };  }; 

    fail(tt)
}



func Test16(tt *testing.T) {
    var t Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); { _ = t; { if tom_TestAndOr_is_sort_Term(t) { if tom_TestAndOr_is_fun_sym_f(any(t).( Term )) { var tom___x  Term  = tom_TestAndOr_get_slot_f_t1(any(t).( Term )); _ = tom___x; if tom_TestAndOr_is_sort_Term(tom___x) { if tom_TestAndOr_is_fun_sym_g(any(tom___x).( Term )) { 


        if (tom_TestAndOr_get_slot_g_t(any(tom___x).( Term )) != tom_TestAndOr_make_a()){
          fail(tt)
        }
        return
      ;  };  };  };  };  };  }; 

    fail(tt)
}



func Test17(tt *testing.T) {
    var t Term = tom_TestAndOr_make_f(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_make_b()); { _ = t; { if tom_TestAndOr_is_sort_Term(t) { if tom_TestAndOr_is_sort_Term(t) { var tomMatch17_6  Term  = tom_TestAndOr_get_slot_f_t1(any(t).( Term )); _ = tomMatch17_6; if tom_TestAndOr_is_sort_Term(tomMatch17_6) { if tom_TestAndOr_is_fun_sym_g(any(tomMatch17_6).( Term )) { if tom_TestAndOr_is_fun_sym_f(any(t).( Term )) { 


        if (tom_TestAndOr_get_slot_g_t(any(tomMatch17_6).( Term )) != tom_TestAndOr_make_a()){
          fail(tt)
        }
        return
      ;  };  };  };  };  };  };  }; 

    fail(tt)
}



func Test18(t *testing.T) {
    var l Term = tom_TestAndOr_cons_list_list(tom_TestAndOr_make_a(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_b(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_c(),tom_TestAndOr_empty_list_list())))); { _ = l; { if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_list(any(l).( Term )) { var tomMatch18_end_5  Term  = any(l).( Term ); _ = tomMatch18_end_5; for { { if !( (tom_TestAndOr_is_empty_list_Term(tomMatch18_end_5) || tom_TestAndOr_equal_term_Term(tomMatch18_end_5, tom_TestAndOr_empty_list_list())) ) { if tom_TestAndOr_is_fun_sym_b(any(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOr_get_head_list_Term(tomMatch18_end_5) }; return tomMatch18_end_5 }()).( Term )) { var tomMatch18_1  Term  = tom_TestAndOr_append_list_list(tom_TestAndOr_get_slice_list(any(l).( Term ),tomMatch18_end_5,tom_TestAndOr_empty_list_list()),tom_TestAndOr_append_list_list(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOr_get_tail_list_Term(tomMatch18_end_5) }; return tom_TestAndOr_empty_list_list() }(),tom_TestAndOr_empty_list_list())); _ = tomMatch18_1; if tom_TestAndOr_is_sort_Term(tomMatch18_1) { 


        if (any(tomMatch18_1).( Term ) != tom_TestAndOr_cons_list_list(tom_TestAndOr_make_a(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_c(),tom_TestAndOr_empty_list_list())))){
          fail(t)
        }
        return
      ;  };  };  }; if  (tom_TestAndOr_is_empty_list_Term(tomMatch18_end_5) || tom_TestAndOr_equal_term_Term(tomMatch18_end_5, tom_TestAndOr_empty_list_list()))  { tomMatch18_end_5 = any(l).( Term );  } else { tomMatch18_end_5 = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch18_end_5) { return tom_TestAndOr_get_tail_list_Term(tomMatch18_end_5) }; return tom_TestAndOr_empty_list_list() }();  };  };  if !(!(tom_TestAndOr_equal_term_Term(tomMatch18_end_5, any(l).( Term )))) { break; }; };  };  };  };  }; 

    fail(t)
}



func Test19(t *testing.T) {
    var l Term = tom_TestAndOr_cons_list_list(tom_TestAndOr_make_a(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_b(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_c(),tom_TestAndOr_empty_list_list())))); { _ = l; { if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_list(any(l).( Term )) { var tomMatch19_end_5  Term  = any(l).( Term ); _ = tomMatch19_end_5; for { { if !( (tom_TestAndOr_is_empty_list_Term(tomMatch19_end_5) || tom_TestAndOr_equal_term_Term(tomMatch19_end_5, tom_TestAndOr_empty_list_list())) ) { if tom_TestAndOr_is_fun_sym_b(any(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOr_get_head_list_Term(tomMatch19_end_5) }; return tomMatch19_end_5 }()).( Term )) { var tomMatch19_1  Term  = tom_TestAndOr_append_list_list(tom_TestAndOr_get_slice_list(any(l).( Term ),tomMatch19_end_5,tom_TestAndOr_empty_list_list()),tom_TestAndOr_append_list_list(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOr_get_tail_list_Term(tomMatch19_end_5) }; return tom_TestAndOr_empty_list_list() }(),tom_TestAndOr_empty_list_list())); _ = tomMatch19_1; if tom_TestAndOr_is_sort_Term(tomMatch19_1) { if tom_TestAndOr_is_fun_sym_list(any(tomMatch19_1).( Term )) { var tomMatch19_end_11  Term  = any(tomMatch19_1).( Term ); _ = tomMatch19_end_11; for { { if !( (tom_TestAndOr_is_empty_list_Term(tomMatch19_end_11) || tom_TestAndOr_equal_term_Term(tomMatch19_end_11, tom_TestAndOr_empty_list_list())) ) { var tomMatch19_17  Term  = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch19_end_11) { return tom_TestAndOr_get_head_list_Term(tomMatch19_end_11) }; return tomMatch19_end_11 }(); _ = tomMatch19_17; if tom_TestAndOr_is_fun_sym_g(any(tomMatch19_17).( Term )) { 


        if (tom_TestAndOr_get_slot_g_t(tomMatch19_17) != tom_TestAndOr_make_a()){
          fail(t)
        }
        return
      ;  };  }; if  (tom_TestAndOr_is_empty_list_Term(tomMatch19_end_11) || tom_TestAndOr_equal_term_Term(tomMatch19_end_11, tom_TestAndOr_empty_list_list()))  { tomMatch19_end_11 = any(tomMatch19_1).( Term );  } else { tomMatch19_end_11 = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch19_end_11) { return tom_TestAndOr_get_tail_list_Term(tomMatch19_end_11) }; return tom_TestAndOr_empty_list_list() }();  };  };  if !(!(tom_TestAndOr_equal_term_Term(tomMatch19_end_11, any(tomMatch19_1).( Term )))) { break; }; };  };  };  };  }; if  (tom_TestAndOr_is_empty_list_Term(tomMatch19_end_5) || tom_TestAndOr_equal_term_Term(tomMatch19_end_5, tom_TestAndOr_empty_list_list()))  { tomMatch19_end_5 = any(l).( Term );  } else { tomMatch19_end_5 = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch19_end_5) { return tom_TestAndOr_get_tail_list_Term(tomMatch19_end_5) }; return tom_TestAndOr_empty_list_list() }();  };  };  if !(!(tom_TestAndOr_equal_term_Term(tomMatch19_end_5, any(l).( Term )))) { break; }; };  };  };  };  }; 

    fail(t)
}



func Test20(t *testing.T) {
    var s Term = tom_TestAndOr_make_a()
    cnt := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_a(any(s).( Term )) { 
 cnt++; ;  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_b(any(s).( Term )) {  cnt++; ;  };  };  };  }; 

    if (cnt > 1) {
      fail(t)
    }
}



func Test21(t *testing.T) {
    var s Term = tom_TestAndOr_make_a()
    cnt := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { 

        if (any(s).( Term ) != tom_TestAndOr_make_a()) {
          fail(t)
        }
        cnt++
      ;  }; if tom_TestAndOr_is_sort_Term(s) {          if (any(s).( Term ) != tom_TestAndOr_make_a()) {           fail(t);         };         cnt++;       ;  };  };  }; 

    if (cnt != 2) {
      fail(t)
    }
}



func Test22(t *testing.T) {
    var s Term = tom_TestAndOr_make_g(tom_TestAndOr_make_a())
    cnt := 0
    ; { _ = s; { if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_g(any(s).( Term )) { 

        if (tom_TestAndOr_get_slot_g_t(any(s).( Term )) != tom_TestAndOr_make_a()) {
          fail(t)
        }
        cnt++
      ;  };  }; if tom_TestAndOr_is_sort_Term(s) { if tom_TestAndOr_is_fun_sym_g(any(s).( Term )) {          if (tom_TestAndOr_get_slot_g_t(any(s).( Term )) != tom_TestAndOr_make_a()) {           fail(t);         };         cnt++;       ;  };  };  };  }; 

    if (cnt != 2) {
      fail(t)
    }
}

  


func Test23(t *testing.T) {
    ; { { var tomMatch23_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch23_0; if tom_TestAndOr_is_sort_Term(tomMatch23_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch23_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch23_0).( Term )); _ = tom____x; var tomMatch23_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch23_1; if tom_TestAndOr_is_sort_Term(tomMatch23_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch23_1).( Term )) { 

        return
      ;  };  }; var tomMatch23_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch23_2; if tom_TestAndOr_is_sort_Term(tomMatch23_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch23_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test24(t *testing.T) {
    ; { { var tomMatch24_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch24_0; if tom_TestAndOr_is_sort_Term(tomMatch24_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch24_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch24_0).( Term )); _ = tom____x; var tomMatch24_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch24_1; if tom_TestAndOr_is_sort_Term(tomMatch24_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch24_1).( Term )) { 

        return
      ;  };  }; var tomMatch24_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch24_2; if tom_TestAndOr_is_sort_Term(tomMatch24_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch24_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test25(t *testing.T) {
    ; { { var tomMatch25_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch25_0; if tom_TestAndOr_is_sort_Term(tomMatch25_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch25_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch25_0).( Term )); _ = tom____x; var tomMatch25_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch25_1; if tom_TestAndOr_is_sort_Term(tomMatch25_1) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch25_1).( Term )) { 

        return
      ;  };  }; var tomMatch25_2  Term  = tom_TestAndOr_make_b(); _ = tomMatch25_2; if tom_TestAndOr_is_sort_Term(tomMatch25_2) { if tom_TestAndOr_equal_term_Term(tom____x, any(tomMatch25_2).( Term )) {          return;       ;  };  };  };  };  };  }; 

    fail(t)
}



func Test26(t *testing.T) {
    ; { { var tomMatch26_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch26_0; if tom_TestAndOr_is_sort_Term(tomMatch26_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch26_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch26_0).( Term )); _ = tom____x; var tomMatch26_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch26_1; if tom_TestAndOr_is_sort_Term(tomMatch26_1) { var tomMatch26_freshSubject_1  Term  = any(tomMatch26_1).( Term ); _ = tomMatch26_freshSubject_1; if tom_TestAndOr_equal_term_Term(tom____x, tomMatch26_freshSubject_1) { var tomMatch26_2  Term  = tom_TestAndOr_make_a(); _ = tomMatch26_2; if tom_TestAndOr_is_sort_Term(tomMatch26_2) { tomMatch26_freshSubject_1 = any(tomMatch26_2).( Term ); if tom_TestAndOr_equal_term_Term(tom____x, tomMatch26_freshSubject_1) { 

        return
      ;  };  };  };  }; var tomMatch26_3  Term  = tom_TestAndOr_make_b(); _ = tomMatch26_3; if tom_TestAndOr_is_sort_Term(tomMatch26_3) { var tomMatch26_freshSubject_2  Term  = any(tomMatch26_3).( Term ); _ = tomMatch26_freshSubject_2; if tom_TestAndOr_equal_term_Term(tom____x, tomMatch26_freshSubject_2) { var tomMatch26_4  Term  = tom_TestAndOr_make_b(); _ = tomMatch26_4; if tom_TestAndOr_is_sort_Term(tomMatch26_4) { tomMatch26_freshSubject_2 = any(tomMatch26_4).( Term ); if tom_TestAndOr_equal_term_Term(tom____x, tomMatch26_freshSubject_2) {          return;       ;  };  };  };  };  };  };  };  }; 

    fail(t)
}





func Test27(t *testing.T) {
    ; { { var tomMatch27_0  Term  = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); _ = tomMatch27_0; if tom_TestAndOr_is_sort_Term(tomMatch27_0) { if tom_TestAndOr_is_fun_sym_f(any(tomMatch27_0).( Term )) { var tom____x  Term  = tom_TestAndOr_get_slot_f_t1(any(tomMatch27_0).( Term )); _ = tom____x; var tomMatch27_1  Term  = tom_TestAndOr_make_a(); _ = tomMatch27_1; if tom_TestAndOr_is_sort_Term(tomMatch27_1) { var tomMatch27_freshSubject_1  Term  = any(tomMatch27_1).( Term ); _ = tomMatch27_freshSubject_1; if tom_TestAndOr_equal_term_Term(tom____x, tomMatch27_freshSubject_1) { var tomMatch27_2  Term  = tom_TestAndOr_make_a(); _ = tomMatch27_2; if tom_TestAndOr_is_sort_Term(tomMatch27_2) { tomMatch27_freshSubject_1 = any(tomMatch27_2).( Term ); if tom_TestAndOr_equal_term_Term(tom____x, tomMatch27_freshSubject_1) { 

        return
      ;  };  };  };  }; var tomMatch27_3  Term  = tom_TestAndOr_make_b(); _ = tomMatch27_3; if tom_TestAndOr_is_sort_Term(tomMatch27_3) { var tomMatch27_freshSubject_2  Term  = any(tomMatch27_3).( Term ); _ = tomMatch27_freshSubject_2; if tom_TestAndOr_equal_term_Term(tom____x, tomMatch27_freshSubject_2) { var tomMatch27_4  Term  = tom_TestAndOr_make_b(); _ = tomMatch27_4; if tom_TestAndOr_is_sort_Term(tomMatch27_4) { tomMatch27_freshSubject_2 = any(tomMatch27_4).( Term ); if tom_TestAndOr_equal_term_Term(tom____x, tomMatch27_freshSubject_2) {          return;       ;  };  };  };  };  };  };  };  }; 

    fail(t)
}



func Test28(t *testing.T) {
    var l Term = tom_TestAndOr_cons_list_list(tom_TestAndOr_make_a(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_b(),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_g(tom_TestAndOr_make_a()),tom_TestAndOr_cons_list_list(tom_TestAndOr_make_c(),tom_TestAndOr_empty_list_list())))); { _ = l; { if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_list(any(l).( Term )) { var tomMatch28_end_5  Term  = any(l).( Term ); _ = tomMatch28_end_5; for { { if !( (tom_TestAndOr_is_empty_list_Term(tomMatch28_end_5) || tom_TestAndOr_equal_term_Term(tomMatch28_end_5, tom_TestAndOr_empty_list_list())) ) { if tom_TestAndOr_is_fun_sym_b(any(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch28_end_5) { return tom_TestAndOr_get_head_list_Term(tomMatch28_end_5) }; return tomMatch28_end_5 }()).( Term )) { 


        return
      ;  };  }; if  (tom_TestAndOr_is_empty_list_Term(tomMatch28_end_5) || tom_TestAndOr_equal_term_Term(tomMatch28_end_5, tom_TestAndOr_empty_list_list()))  { tomMatch28_end_5 = any(l).( Term );  } else { tomMatch28_end_5 = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch28_end_5) { return tom_TestAndOr_get_tail_list_Term(tomMatch28_end_5) }; return tom_TestAndOr_empty_list_list() }();  };  };  if !(!(tom_TestAndOr_equal_term_Term(tomMatch28_end_5, any(l).( Term )))) { break; }; };  };  }; if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_list(any(l).( Term )) { var tomMatch28_end_11  Term  = any(l).( Term ); _ = tomMatch28_end_11; for { { if !( (tom_TestAndOr_is_empty_list_Term(tomMatch28_end_11) || tom_TestAndOr_equal_term_Term(tomMatch28_end_11, tom_TestAndOr_empty_list_list())) ) { if tom_TestAndOr_is_fun_sym_c(any(func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch28_end_11) { return tom_TestAndOr_get_head_list_Term(tomMatch28_end_11) }; return tomMatch28_end_11 }()).( Term )) {          return;       ;  };  }; if  (tom_TestAndOr_is_empty_list_Term(tomMatch28_end_11) || tom_TestAndOr_equal_term_Term(tomMatch28_end_11, tom_TestAndOr_empty_list_list()))  { tomMatch28_end_11 = any(l).( Term );  } else { tomMatch28_end_11 = func()  Term  { if tom_TestAndOr_is_fun_sym_list(tomMatch28_end_11) { return tom_TestAndOr_get_tail_list_Term(tomMatch28_end_11) }; return tom_TestAndOr_empty_list_list() }();  };  };  if !(!(tom_TestAndOr_equal_term_Term(tomMatch28_end_11, any(l).( Term )))) { break; }; };  };  };  };  }; 

    fail(t)
}



func Test29(t *testing.T) {
    var l Term = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_a()); { _ = l; { if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_f(any(l).( Term )) { if tom_TestAndOr_equal_term_Term(tom_TestAndOr_get_slot_f_t2(any(l).( Term )), tom_TestAndOr_get_slot_f_t1(any(l).( Term ))) { 


        return
      ;  };  };  };  };  }; 

    fail(t)
}



func Test30(t *testing.T) {
    var l Term = tom_TestAndOr_make_f(tom_TestAndOr_make_a(),tom_TestAndOr_make_b()); { _ = l; { if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_g(any(l).( Term )) { 


        fail(t)
      ;  };  }; if tom_TestAndOr_is_sort_Term(l) { if tom_TestAndOr_is_fun_sym_f(any(l).( Term )) { if tom_TestAndOr_equal_term_Term(tom_TestAndOr_get_slot_f_t1(any(l).( Term )), tom_TestAndOr_get_slot_f_t2(any(l).( Term ))) {          fail(t);       ;  };  };  };  };  }; 

    return

}




func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
