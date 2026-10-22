package gom














import (
  "testing"

  . "fixture/rulelist"
)

  func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_List(t1  List , t2  List ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_List(t any) bool { return func() bool { _, ok := (t).(List); return ok }();  }; func tom_equal_term_Bool(t1  Bool , t2  Bool ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Bool(t any) bool { return func() bool { _, ok := (t).(Bool); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_make_b()  Term  { return (MakeB()) }; func tom_is_fun_sym_c(t  Term ) bool { return func() bool { _, ok := (t).(*CTerm); return ok }();  }; func tom_make_c()  Term  { return (MakeC()) }; func tom_make_d()  Term  { return (MakeD()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(x  Term )  Term  { return (MakeF(x)) }; func tom_get_slot_f_x(t  Term )  Term  { return (t).(*FTerm).X;  }; func tom_make_g(l  List )  Term  { return (MakeG(l)) }; func tom_make_h(x  Term )  Term  { return (MakeH(x)) }; func tom_is_fun_sym_k(t  Term ) bool { return func() bool { _, ok := (t).(*KTerm); return ok }();  }; func tom_empty_list_k()  Term  { return (EmptyK()) }; func tom_cons_list_k(e  Term , l  Term )  Term  { return (ConsK(e,l)) }; func tom_get_head_k_Term(l  Term )  Term  { return (l).GetHeadk();  }; func tom_get_tail_k_Term(l  Term )  Term  { return (l).GetTailk();  }; func tom_is_empty_k_Term(l  Term ) bool { return (l).IsEmptyk();  }; func tom_append_list_k(l1  Term , l2  Term )  Term  { if tom_is_empty_k_Term(l1) { return l2 } else if tom_is_empty_k_Term(l2) { return l1 } else if tom_is_fun_sym_k(l1) { if tom_is_empty_k_Term(tom_get_tail_k_Term(l1)) { return tom_cons_list_k(tom_get_head_k_Term(l1), l2) } else { return tom_cons_list_k(tom_get_head_k_Term(l1), tom_append_list_k(tom_get_tail_k_Term(l1), l2)) } } else { return tom_cons_list_k(l1, l2) }; }; func tom_get_slice_k(begin  Term , end  Term , tail  Term )  Term  { if tom_equal_term_Term(begin, end) { return tail } else if tom_equal_term_Term(end, tail) && (tom_is_empty_k_Term(end) || tom_equal_term_Term(end, tom_empty_list_k())) { return begin }; return tom_cons_list_k(func()  Term  { if tom_is_fun_sym_k(begin) { return tom_get_head_k_Term(begin) }; return begin }(), tom_get_slice_k(func()  Term  { if tom_is_fun_sym_k(begin) { return tom_get_tail_k_Term(begin) }; return tom_empty_list_k() }(), end, tail)) }; func tom_is_fun_sym_conc(t  List ) bool { return func() bool { _, ok := (t).(*ConcList); return ok }();  }; func tom_empty_list_conc()  List  { return (EmptyConc()) }; func tom_cons_list_conc(e  Term , l  List )  List  { return (ConsConc(e,l)) }; func tom_get_head_conc_List(l  List )  Term  { return (l).GetHeadconc();  }; func tom_get_tail_conc_List(l  List )  List  { return (l).GetTailconc();  }; func tom_is_empty_conc_List(l  List ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  List , l2  List )  List  { if tom_is_empty_conc_List(l1) { return l2 } else if tom_is_empty_conc_List(l2) { return l1 } else if tom_is_empty_conc_List(tom_get_tail_conc_List(l1)) { return tom_cons_list_conc(tom_get_head_conc_List(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_List(l1), tom_append_list_conc(tom_get_tail_conc_List(l1), l2)) }; }; func tom_get_slice_conc(begin  List , end  List , tail  List )  List  { if tom_equal_term_List(begin, end) { return tail } else if tom_equal_term_List(end, tail) && (tom_is_empty_conc_List(end) || tom_equal_term_List(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_List(begin), tom_get_slice_conc(tom_get_tail_conc_List(begin), end, tail)) };   func init() {   HookMakeF = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { { if tom_is_sort_Term(arg_1) { if tom_is_fun_sym_a(any(arg_1).( Term )) {  return tom_make_f(tom_make_b());  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeG = func(arg_1 List) (Term, List) {     tom__r := func() Term {     {     ; { { if tom_is_sort_List(arg_1) { if tom_is_fun_sym_conc(any(arg_1).( List )) { var tomMatch2_end_4  List  = any(arg_1).( List ); _ = tomMatch2_end_4; for { { if !(tom_is_empty_conc_List(tomMatch2_end_4)) { var tomMatch2_8  Term  = tom_get_head_conc_List(tomMatch2_end_4); _ = tomMatch2_8; if tom_is_fun_sym_f(any(tomMatch2_8).( Term )) { if tom_is_fun_sym_c(any(tom_get_slot_f_x(tomMatch2_8)).( Term )) {  return tom_make_g(tom_append_list_conc(tom_get_slice_conc(any(arg_1).( List ),tomMatch2_end_4,tom_empty_list_conc()),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_append_list_conc(tom_get_tail_conc_List(tomMatch2_end_4),tom_empty_list_conc()))));  };  };  }; if tom_is_empty_conc_List(tomMatch2_end_4) { tomMatch2_end_4 = any(arg_1).( List );  } else { tomMatch2_end_4 = tom_get_tail_conc_List(tomMatch2_end_4);  };  };  if !(!(tom_equal_term_List(tomMatch2_end_4, any(arg_1).( List )))) { break; }; };  };  };  }; { if tom_is_sort_List(arg_1) { if tom_is_fun_sym_conc(any(arg_1).( List )) { var tomMatch2_end_16  List  = any(arg_1).( List ); _ = tomMatch2_end_16; for { { if !(tom_is_empty_conc_List(tomMatch2_end_16)) { var tomMatch2_20  Term  = tom_get_head_conc_List(tomMatch2_end_16); _ = tomMatch2_20; if tom_is_fun_sym_f(any(tomMatch2_20).( Term )) { if tom_is_fun_sym_c(any(tom_get_slot_f_x(tomMatch2_20)).( Term )) {  return tom_make_g(tom_append_list_conc(tom_get_slice_conc(any(arg_1).( List ),tomMatch2_end_16,tom_empty_list_conc()),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_append_list_conc(tom_get_tail_conc_List(tomMatch2_end_16),tom_empty_list_conc()))));  };  };  }; if tom_is_empty_conc_List(tomMatch2_end_16) { tomMatch2_end_16 = any(arg_1).( List );  } else { tomMatch2_end_16 = tom_get_tail_conc_List(tomMatch2_end_16);  };  };  if !(!(tom_equal_term_List(tomMatch2_end_16, any(arg_1).( List )))) { break; }; };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeH = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { { if tom_is_sort_Term(arg_1) {  return tom_make_a();  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeInsertK = func(head Term, tail Term) (Term, Term, Term) {     tom__r := func() Term {     {     ; { { var tomMatch4_0  Term  = RealMakeConsK(head,tail); _ = tomMatch4_0; if tom_is_sort_Term(tomMatch4_0) { if tom_is_fun_sym_k(any(tomMatch4_0).( Term )) { var tomMatch4_end_4  Term  = any(tomMatch4_0).( Term ); _ = tomMatch4_end_4; for { { if !( (tom_is_empty_k_Term(tomMatch4_end_4) || tom_equal_term_Term(tomMatch4_end_4, tom_empty_list_k())) ) { if tom_is_fun_sym_a(any(func()  Term  { if tom_is_fun_sym_k(tomMatch4_end_4) { return tom_get_head_k_Term(tomMatch4_end_4) }; return tomMatch4_end_4 }()).( Term )) {  return tom_make_a();  };  }; if  (tom_is_empty_k_Term(tomMatch4_end_4) || tom_equal_term_Term(tomMatch4_end_4, tom_empty_list_k()))  { tomMatch4_end_4 = any(tomMatch4_0).( Term );  } else { tomMatch4_end_4 = func()  Term  { if tom_is_fun_sym_k(tomMatch4_end_4) { return tom_get_tail_k_Term(tomMatch4_end_4) }; return tom_empty_list_k() }();  };  };  if !(!(tom_equal_term_Term(tomMatch4_end_4, any(tomMatch4_0).( Term )))) { break; }; };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };    func init() {   HookMakeInsertConc = func(head Term, tail List) (List, Term, List) {     tom__r := func() List {     {     ; { { var tomMatch5_0  List  = RealMakeConsConc(head,tail); _ = tomMatch5_0; if tom_is_sort_List(tomMatch5_0) { if tom_is_fun_sym_conc(any(tomMatch5_0).( List )) { if !(tom_is_empty_conc_List(any(tomMatch5_0).( List ))) { if tom_is_fun_sym_a(any(tom_get_head_conc_List(any(tomMatch5_0).( List ))).( Term )) { if tom_is_empty_conc_List(tom_get_tail_conc_List(any(tomMatch5_0).( List ))) {  return tom_cons_list_conc(tom_make_b(),tom_empty_list_conc());  };  };  };  };  };  }; { var tomMatch5_5  List  = RealMakeConsConc(head,tail); _ = tomMatch5_5; if tom_is_sort_List(tomMatch5_5) { if tom_is_fun_sym_conc(any(tomMatch5_5).( List )) { if !(tom_is_empty_conc_List(any(tomMatch5_5).( List ))) { var tomMatch5_7  List  = tom_get_tail_conc_List(any(tomMatch5_5).( List )); _ = tomMatch5_7; if !(tom_is_empty_conc_List(tomMatch5_7)) { if tom_is_fun_sym_a(any(tom_get_head_conc_List(tomMatch5_7)).( Term )) { if tom_is_empty_conc_List(tom_get_tail_conc_List(tomMatch5_7)) {  return tom_cons_list_conc(tom_get_head_conc_List(any(tomMatch5_5).( List )),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()));  };  };  };  };  };  };  }; { var tomMatch5_11  List  = RealMakeConsConc(head,tail); _ = tomMatch5_11; if tom_is_sort_List(tomMatch5_11) { if tom_is_fun_sym_conc(any(tomMatch5_11).( List )) { var tomMatch5_end_15  List  = any(tomMatch5_11).( List ); _ = tomMatch5_end_15; for { { if !(tom_is_empty_conc_List(tomMatch5_end_15)) { var tomMatch5_19  Term  = tom_get_head_conc_List(tomMatch5_end_15); _ = tomMatch5_19; if tom_is_fun_sym_f(any(tomMatch5_19).( Term )) { if tom_is_fun_sym_b(any(tom_get_slot_f_x(tomMatch5_19)).( Term )) {  return tom_append_list_conc(tom_get_slice_conc(any(tomMatch5_11).( List ),tomMatch5_end_15,tom_empty_list_conc()),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_append_list_conc(tom_get_tail_conc_List(tomMatch5_end_15),tom_empty_list_conc())));  };  };  }; if tom_is_empty_conc_List(tomMatch5_end_15) { tomMatch5_end_15 = any(tomMatch5_11).( List );  } else { tomMatch5_end_15 = tom_get_tail_conc_List(tomMatch5_end_15);  };  };  if !(!(tom_equal_term_List(tomMatch5_end_15, any(tomMatch5_11).( List )))) { break; }; };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };  

func TestF1(t *testing.T) {
    assertEquals(t, tom_make_f(tom_make_b()),tom_make_f(tom_make_a()))
}



func TestConc1(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))
}



func TestConcsingle(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))
}



func TestFlat1(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))
}



func TestFlat2(t *testing.T) {
    assertNotSame(t, tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))
}



func TestFlat3(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))
}



func TestFlat4(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_f(tom_make_c()),tom_empty_list_conc()),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_empty_list_conc()))
}



func TestFlat5(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))
}



func Test2(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_f(tom_make_c()),tom_empty_list_conc()),tom_cons_list_conc(tom_make_f(tom_make_b()),tom_empty_list_conc()))
}



func Test3(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_empty_list_conc())),tom_cons_list_conc(tom_make_f(tom_make_b()),tom_cons_list_conc(tom_make_f(tom_make_b()),tom_empty_list_conc())))
}



func Test4(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_empty_list_conc())),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_empty_list_conc())))
}



func Test5(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))))),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))))))
}



func Test6(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_empty_list_conc())),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_b()),tom_empty_list_conc())))
}



func Test7(t *testing.T) {
    
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))))),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_c()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))))))
}



func Test31(t *testing.T) {
    assertEquals(t, tom_make_g(tom_cons_list_conc(tom_make_f(tom_make_d()),tom_empty_list_conc())),tom_make_g(tom_cons_list_conc(tom_make_f(tom_make_a()),tom_empty_list_conc())))
    

}



func Test32(t *testing.T) {
    assertEquals(t, tom_make_g(tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_empty_list_conc()))),tom_make_g(tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_empty_list_conc()))))
}



func Test33(t *testing.T) {
    assertEquals(t, tom_make_g(tom_cons_list_conc(tom_make_f(tom_make_d()),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_empty_list_conc()))),tom_make_g(tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_empty_list_conc()))))
}



func Test34(t *testing.T) {
    
    assertEquals(t, tom_make_g(tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_f(tom_make_d()),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())))))),tom_make_g(tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_f(tom_make_a()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))))))
}



func TestUnderscore_1(t *testing.T) {
    assertEquals(t, tom_make_a(),tom_make_h(tom_make_b()))
}



func TestUnderscore_2(t *testing.T) {
    assertEquals(t, tom_make_a(),tom_cons_list_k(tom_make_b(),tom_cons_list_k(tom_make_a(),tom_cons_list_k(tom_make_b(),tom_cons_list_k(tom_make_b(),tom_empty_list_k())))))
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func assertNotSame(t *testing.T, unexpected, actual any) {
  t.Helper()
  if unexpected == actual {
    t.Fatalf("expected not same")
  }
}
