package gom















import (
  "testing"
)

  func tom_equal_term_NAU(t1  NAU , t2  NAU ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_NAU(t any) bool { return func() bool { _, ok := (t).(NAU); return ok }();  }; func tom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_make_m()  NAU  { return (MakeM()) }; func tom_is_fun_sym_p(t  NAU ) bool { return func() bool { _, ok := (t).(*PNAU); return ok }();  }; func tom_empty_list_p()  NAU  { return (EmptyP()) }; func tom_cons_list_p(e  NAU , l  NAU )  NAU  { return (ConsP(e,l)) }; func tom_get_head_p_NAU(l  NAU )  NAU  { return (l).GetHeadp();  }; func tom_get_tail_p_NAU(l  NAU )  NAU  { return (l).GetTailp();  }; func tom_is_empty_p_NAU(l  NAU ) bool { return (l).IsEmptyp();  }; func tom_append_list_p(l1  NAU , l2  NAU )  NAU  { if tom_is_empty_p_NAU(l1) { return l2 } else if tom_is_empty_p_NAU(l2) { return l1 } else if tom_is_fun_sym_p(l1) { if tom_is_empty_p_NAU(tom_get_tail_p_NAU(l1)) { return tom_cons_list_p(tom_get_head_p_NAU(l1), l2) } else { return tom_cons_list_p(tom_get_head_p_NAU(l1), tom_append_list_p(tom_get_tail_p_NAU(l1), l2)) } } else { return tom_cons_list_p(l1, l2) }; }; func tom_get_slice_p(begin  NAU , end  NAU , tail  NAU )  NAU  { if tom_equal_term_NAU(begin, end) { return tail } else if tom_equal_term_NAU(end, tail) && (tom_is_empty_p_NAU(end) || tom_equal_term_NAU(end, tom_empty_list_p())) { return begin }; return tom_cons_list_p(func()  NAU  { if tom_is_fun_sym_p(begin) { return tom_get_head_p_NAU(begin) }; return begin }(), tom_get_slice_p(func()  NAU  { if tom_is_fun_sym_p(begin) { return tom_get_tail_p_NAU(begin) }; return tom_empty_list_p() }(), end, tail)) }; func tom_make_a()  T  { return (MakeA()) }; func tom_make_b()  T  { return (MakeB()) }; func tom_make_c()  T  { return (MakeC()) }; func tom_make_d()  T  { return (MakeD()) }; func tom_is_fun_sym_conc(t  T ) bool { return func() bool { _, ok := (t).(*ConcT); return ok }();  }; func tom_empty_list_conc()  T  { return (EmptyConc()) }; func tom_cons_list_conc(e  T , l  T )  T  { return (ConsConc(e,l)) }; func tom_get_head_conc_T(l  T )  T  { return (l).GetHeadconc();  }; func tom_get_tail_conc_T(l  T )  T  { return (l).GetTailconc();  }; func tom_is_empty_conc_T(l  T ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  T , l2  T )  T  { if tom_is_empty_conc_T(l1) { return l2 } else if tom_is_empty_conc_T(l2) { return l1 } else if tom_is_fun_sym_conc(l1) { if tom_is_empty_conc_T(tom_get_tail_conc_T(l1)) { return tom_cons_list_conc(tom_get_head_conc_T(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_T(l1), tom_append_list_conc(tom_get_tail_conc_T(l1), l2)) } } else { return tom_cons_list_conc(l1, l2) }; }; func tom_get_slice_conc(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_conc_T(end) || tom_equal_term_T(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(func()  T  { if tom_is_fun_sym_conc(begin) { return tom_get_head_conc_T(begin) }; return begin }(), tom_get_slice_conc(func()  T  { if tom_is_fun_sym_conc(begin) { return tom_get_tail_conc_T(begin) }; return tom_empty_list_conc() }(), end, tail)) }; func tom_make_aa()  L  { return (MakeAa()) }; func tom_make_bb()  L  { return (MakeBb()) }; func tom_make_cc()  L  { return (MakeCc()) }; func tom_make_dd()  L  { return (MakeDd()) }; func tom_make_Conslist(t0  L , t1  L )  L  { return (ConsList(t0, t1)) }; func tom_is_fun_sym_list(t  L ) bool { return func() bool { _, ok := (t).(*ListL); return ok }();  }; func tom_empty_list_list()  L  { return (EmptyList()) }; func tom_cons_list_list(e  L , l  L )  L  { return (ConsList(e,l)) }; func tom_get_head_list_L(l  L )  L  { return (l).GetHeadlist();  }; func tom_get_tail_list_L(l  L )  L  { return (l).GetTaillist();  }; func tom_is_empty_list_L(l  L ) bool { return (l).IsEmptylist();  }; func tom_append_list_list(l1  L , l2  L )  L  { if tom_is_empty_list_L(l1) { return l2 } else if tom_is_empty_list_L(l2) { return l1 } else if tom_is_fun_sym_list(l1) { if tom_is_empty_list_L(tom_get_tail_list_L(l1)) { return tom_cons_list_list(tom_get_head_list_L(l1), l2) } else { return tom_cons_list_list(tom_get_head_list_L(l1), tom_append_list_list(tom_get_tail_list_L(l1), l2)) } } else { return tom_cons_list_list(l1, l2) }; }; func tom_get_slice_list(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_list_L(end) || tom_equal_term_L(end, tom_empty_list_list())) { return begin }; return tom_cons_list_list(func()  L  { if tom_is_fun_sym_list(begin) { return tom_get_head_list_L(begin) }; return begin }(), tom_get_slice_list(func()  L  { if tom_is_fun_sym_list(begin) { return tom_get_tail_list_L(begin) }; return tom_empty_list_list() }(), end, tail)) }; 






















  
func TestFlatten1(t *testing.T) {
    assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))))
}



func TestFlatten2(t *testing.T) {
    var l1 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list()))
    var l2 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list()))
    assertEquals(t, tom_cons_list_list(l1,tom_cons_list_list(l2,tom_empty_list_list())),tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list())))))
}



func TestFlatten3(t *testing.T) {
    var l1 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list()))
    var l2 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list()))
    assertEquals(t, tom_cons_list_list(l1,tom_cons_list_list(l2,tom_empty_list_list())),tom_append_list_list(l1,tom_append_list_list(l2,tom_empty_list_list())))
}



func TestFlatten4(t *testing.T) {
    var l1 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_bb(),tom_empty_list_list()))
    assertEquals(t, tom_make_Conslist(l1,tom_make_bb()),tom_append_list_list(l1,tom_cons_list_list(tom_make_bb(),tom_empty_list_list())))
}



func TestNeutral1(t *testing.T) {
    assertEquals(t, tom_empty_list_list(),tom_make_aa())
}



func TestNeutral2(t *testing.T) {
    assertEquals(t, tom_cons_list_list(tom_make_aa(),tom_cons_list_list(tom_make_aa(),tom_empty_list_list())),tom_make_aa())
}



func TestNeutral3(t *testing.T) {
    var l L = tom_make_bb()
    cnt := 0
    ; { _ = l; { if tom_is_sort_L(l) { if tom_is_sort_L(any(l).( L )) { var tomMatch1_end_4  L  = any(l).( L ); _ = tomMatch1_end_4; for { { var tom___x  L  = tom_get_slice_list(any(l).( L ),tomMatch1_end_4,tom_empty_list_list()); _ = tom___x; var tom___y  L  = tomMatch1_end_4; _ = tom___y; 

        if(tom___x == tom_make_bb() && tom___y == tom_make_aa()) {
          cnt += 2
        }
        if(tom___x == tom_make_aa() && tom___y == tom_make_bb()) {
          cnt += 3
        }; if  (tom_is_empty_list_L(tomMatch1_end_4) || tom_equal_term_L(tomMatch1_end_4, tom_empty_list_list()))  { tomMatch1_end_4 = any(l).( L );  } else { tomMatch1_end_4 = func()  L  { if tom_is_fun_sym_list(tomMatch1_end_4) { return tom_get_tail_list_L(tomMatch1_end_4) }; return tom_empty_list_list() }();  };  };  if !(!(tom_equal_term_L(tomMatch1_end_4, any(l).( L )))) { break; }; };  };  };  };  }; 


    if (0 == cnt) {
      fail(t, "list(x,y) should match bb()")
    }
    assertEqualsMsg(t, "Incomplete matching",cnt,5)
}



func TestNeutral4(tt *testing.T) {
    var t T = tom_make_b()
    cnt := 0
    ; { _ = t; { if tom_is_sort_T(t) { if tom_is_sort_T(any(t).( T )) { var tomMatch2_end_4  T  = any(t).( T ); _ = tomMatch2_end_4; for { { var tom___x  T  = tom_get_slice_conc(any(t).( T ),tomMatch2_end_4,tom_empty_list_conc()); _ = tom___x; var tom___y  T  = tomMatch2_end_4; _ = tom___y; 

        if (tom___x == tom_make_b() && tom___y == tom_empty_list_conc()) {
          cnt += 2
        }
        if (tom___x == tom_empty_list_conc() && tom___y == tom_make_b()) {
          cnt += 3
        }; if  (tom_is_empty_conc_T(tomMatch2_end_4) || tom_equal_term_T(tomMatch2_end_4, tom_empty_list_conc()))  { tomMatch2_end_4 = any(t).( T );  } else { tomMatch2_end_4 = func()  T  { if tom_is_fun_sym_conc(tomMatch2_end_4) { return tom_get_tail_conc_T(tomMatch2_end_4) }; return tom_empty_list_conc() }();  };  };  if !(!(tom_equal_term_T(tomMatch2_end_4, any(t).( T )))) { break; }; };  };  };  };  }; 


    if (0 == cnt) {
      fail(tt, " conc(x,y) should match b()")
    }
    assertEqualsMsg(tt, "Incomplete matching",cnt,5)
}



func TestNeutral5(tt *testing.T) {
    var t NAU = tom_make_m()
    cnt := 0
    ; { _ = t; { if tom_is_sort_NAU(t) { if tom_is_fun_sym_p(any(t).( NAU )) { var tomMatch3_end_4  NAU  = any(t).( NAU ); _ = tomMatch3_end_4; for { { 

        cnt += 2
      ; if  (tom_is_empty_p_NAU(tomMatch3_end_4) || tom_equal_term_NAU(tomMatch3_end_4, tom_empty_list_p()))  { tomMatch3_end_4 = any(t).( NAU );  } else { tomMatch3_end_4 = func()  NAU  { if tom_is_fun_sym_p(tomMatch3_end_4) { return tom_get_tail_p_NAU(tomMatch3_end_4) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_NAU(tomMatch3_end_4, any(t).( NAU )))) { break; }; };  };  };  };  }; 

    assertEqualsMsg(tt, "m() does not match p(x*,y*)",cnt,0)
}



func TestOrder1(t *testing.T) {
    var t1 T = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_d(),tom_empty_list_conc()))))    
    var t2 T = tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))))    
    assertEquals(t, t1,t2)
}



func TestOrder2(t *testing.T) {
    var t2 T = tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))))    
    var t3 T = tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))))    
    assertEquals(t, t2,t3)
}



func TestOrderFlatten1(t *testing.T) {
    var t1 T = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_d(),tom_empty_list_conc())))))    
    var t2 T = tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))),tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))))    
    assertEquals(t, t1,t2)
}



func TestOrderFlatten2(t *testing.T) {
    var t2 T = tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))),tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))))    
    var t3 T = tom_cons_list_conc(tom_make_d(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc()))),tom_empty_list_conc())))))    
    assertEquals(t, t2,t3)
}



func TestOrderN1(t *testing.T) {
    var t1 L = tom_cons_list_list(tom_make_aa(),tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_cc(),tom_cons_list_list(tom_make_dd(),tom_empty_list_list()))))    
    var t2 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_dd(),tom_cons_list_list(tom_make_aa(),tom_cons_list_list(tom_make_cc(),tom_empty_list_list()))))    
    assertEquals(t, t1,t2)
}



func TestOrderN2(t *testing.T) {
    var t2 L = tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_dd(),tom_cons_list_list(tom_make_aa(),tom_cons_list_list(tom_make_cc(),tom_empty_list_list()))))    
    var t3 L = tom_cons_list_list(tom_make_dd(),tom_cons_list_list(tom_make_bb(),tom_cons_list_list(tom_make_cc(),tom_cons_list_list(tom_make_aa(),tom_empty_list_list()))))    
    assertEquals(t, t2,t3)
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
