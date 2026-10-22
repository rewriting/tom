package gom















import (
  "testing"
)

  func tom_equal_term_NAU(t1  NAU , t2  NAU ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_NAU(t any) bool { return func() bool { _, ok := (t).(NAU); return ok }();  }; func tom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_make_m()  NAU  { return (MakeM()) }; func tom_is_fun_sym_p(t  NAU ) bool { return func() bool { _, ok := (t).(*PNAU); return ok }();  }; func tom_empty_list_p()  NAU  { return (EmptyP()) }; func tom_cons_list_p(e  NAU , l  NAU )  NAU  { return (ConsP(e,l)) }; func tom_get_head_p_NAU(l  NAU )  NAU  { return (l).GetHeadp();  }; func tom_get_tail_p_NAU(l  NAU )  NAU  { return (l).GetTailp();  }; func tom_is_empty_p_NAU(l  NAU ) bool { return (l).IsEmptyp();  }; func tom_append_list_p(l1  NAU , l2  NAU )  NAU  { if tom_is_empty_p_NAU(l1) { return l2 } else if tom_is_empty_p_NAU(l2) { return l1 } else if tom_is_fun_sym_p(l1) { if tom_is_empty_p_NAU(tom_get_tail_p_NAU(l1)) { return tom_cons_list_p(tom_get_head_p_NAU(l1), l2) } else { return tom_cons_list_p(tom_get_head_p_NAU(l1), tom_append_list_p(tom_get_tail_p_NAU(l1), l2)) } } else { return tom_cons_list_p(l1, l2) }; }; func tom_get_slice_p(begin  NAU , end  NAU , tail  NAU )  NAU  { if tom_equal_term_NAU(begin, end) { return tail } else if tom_equal_term_NAU(end, tail) && (tom_is_empty_p_NAU(end) || tom_equal_term_NAU(end, tom_empty_list_p())) { return begin }; return tom_cons_list_p(func()  NAU  { if tom_is_fun_sym_p(begin) { return tom_get_head_p_NAU(begin) }; return begin }(), tom_get_slice_p(func()  NAU  { if tom_is_fun_sym_p(begin) { return tom_get_tail_p_NAU(begin) }; return tom_empty_list_p() }(), end, tail)) }; func tom_make_a()  T  { return (MakeA()) }; func tom_make_b()  T  { return (MakeB()) }; func tom_is_fun_sym_concAU(t  T ) bool { return func() bool { _, ok := (t).(*ConcAUT); return ok }();  }; func tom_empty_list_concAU()  T  { return (EmptyConcAU()) }; func tom_cons_list_concAU(e  T , l  T )  T  { return (ConsConcAU(e,l)) }; func tom_get_head_concAU_T(l  T )  T  { return (l).GetHeadconcAU();  }; func tom_get_tail_concAU_T(l  T )  T  { return (l).GetTailconcAU();  }; func tom_is_empty_concAU_T(l  T ) bool { return (l).IsEmptyconcAU();  }; func tom_append_list_concAU(l1  T , l2  T )  T  { if tom_is_empty_concAU_T(l1) { return l2 } else if tom_is_empty_concAU_T(l2) { return l1 } else if tom_is_fun_sym_concAU(l1) { if tom_is_empty_concAU_T(tom_get_tail_concAU_T(l1)) { return tom_cons_list_concAU(tom_get_head_concAU_T(l1), l2) } else { return tom_cons_list_concAU(tom_get_head_concAU_T(l1), tom_append_list_concAU(tom_get_tail_concAU_T(l1), l2)) } } else { return tom_cons_list_concAU(l1, l2) }; }; func tom_get_slice_concAU(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_concAU_T(end) || tom_equal_term_T(end, tom_empty_list_concAU())) { return begin }; return tom_cons_list_concAU(func()  T  { if tom_is_fun_sym_concAU(begin) { return tom_get_head_concAU_T(begin) }; return begin }(), tom_get_slice_concAU(func()  T  { if tom_is_fun_sym_concAU(begin) { return tom_get_tail_concAU_T(begin) }; return tom_empty_list_concAU() }(), end, tail)) }; func tom_make_aa()  L  { return (MakeAa()) }; func tom_is_fun_sym_bb(t  L ) bool { return func() bool { _, ok := (t).(*BbL); return ok }();  }; func tom_make_bb()  L  { return (MakeBb()) }; func tom_make_ConslistAU(t0  L , t1  L )  L  { return (ConsListAU(t0, t1)) }; func tom_is_fun_sym_listAU(t  L ) bool { return func() bool { _, ok := (t).(*ListAUL); return ok }();  }; func tom_empty_list_listAU()  L  { return (EmptyListAU()) }; func tom_cons_list_listAU(e  L , l  L )  L  { return (ConsListAU(e,l)) }; func tom_get_head_listAU_L(l  L )  L  { return (l).GetHeadlistAU();  }; func tom_get_tail_listAU_L(l  L )  L  { return (l).GetTaillistAU();  }; func tom_is_empty_listAU_L(l  L ) bool { return (l).IsEmptylistAU();  }; func tom_append_list_listAU(l1  L , l2  L )  L  { if tom_is_empty_listAU_L(l1) { return l2 } else if tom_is_empty_listAU_L(l2) { return l1 } else if tom_is_fun_sym_listAU(l1) { if tom_is_empty_listAU_L(tom_get_tail_listAU_L(l1)) { return tom_cons_list_listAU(tom_get_head_listAU_L(l1), l2) } else { return tom_cons_list_listAU(tom_get_head_listAU_L(l1), tom_append_list_listAU(tom_get_tail_listAU_L(l1), l2)) } } else { return tom_cons_list_listAU(l1, l2) }; }; func tom_get_slice_listAU(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_listAU_L(end) || tom_equal_term_L(end, tom_empty_list_listAU())) { return begin }; return tom_cons_list_listAU(func()  L  { if tom_is_fun_sym_listAU(begin) { return tom_get_head_listAU_L(begin) }; return begin }(), tom_get_slice_listAU(func()  L  { if tom_is_fun_sym_listAU(begin) { return tom_get_tail_listAU_L(begin) }; return tom_empty_list_listAU() }(), end, tail)) }; 



























func TestFlatten1(t *testing.T) {
    assertEquals(t, tom_cons_list_concAU(tom_make_a(),tom_cons_list_concAU(tom_make_b(),tom_cons_list_concAU(tom_make_a(),tom_empty_list_concAU()))),tom_cons_list_concAU(tom_make_a(),tom_cons_list_concAU(tom_cons_list_concAU(tom_make_b(),tom_empty_list_concAU()),tom_cons_list_concAU(tom_make_a(),tom_empty_list_concAU()))))
}



func TestFlatten2(t *testing.T) {
    var l1 L = tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU()))
    var l2 L = tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU()))
    assertEquals(t, tom_cons_list_listAU(l1,tom_cons_list_listAU(l2,tom_empty_list_listAU())),tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU())))))
}



func TestFlatten3(t *testing.T) {
    var l1 L = tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU()))
    var l2 L = tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU()))
    assertEquals(t, tom_cons_list_listAU(l1,tom_cons_list_listAU(l2,tom_empty_list_listAU())),tom_append_list_listAU(l1,tom_append_list_listAU(l2,tom_empty_list_listAU())))
}



func TestFlatten4(t *testing.T) {
    var l1 L = tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU()))
    assertEquals(t, tom_make_ConslistAU(l1,tom_make_bb()),tom_append_list_listAU(l1,tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU())))
}



func TestNeutral1(t *testing.T) {
    assertEquals(t, tom_empty_list_listAU(),tom_make_aa())
}



func TestNeutral2(t *testing.T) {
    assertEquals(t, tom_cons_list_listAU(tom_make_aa(),tom_cons_list_listAU(tom_make_aa(),tom_empty_list_listAU())),tom_make_aa())
}



func TestNeutral3(t *testing.T) {
    var l L = tom_make_bb()
    cnt := 0
    ; { { if tom_is_sort_L(l) { if tom_is_sort_L(any(l).( L )) { var tomMatch1_end_4  L  = any(l).( L ); _ = tomMatch1_end_4; for { { var tom___x  L  = tom_get_slice_listAU(any(l).( L ),tomMatch1_end_4,tom_empty_list_listAU()); _ = tom___x; var tom___y  L  = tomMatch1_end_4; _ = tom___y; 

        if(tom___x == tom_make_bb() && tom___y == tom_make_aa()) { 
          cnt += 2
         }; 
        if(tom___x == tom_make_aa() && tom___y == tom_make_bb()) { 
          cnt += 3
         }; if  (tom_is_empty_listAU_L(tomMatch1_end_4) || tom_equal_term_L(tomMatch1_end_4, tom_empty_list_listAU()))  { tomMatch1_end_4 = any(l).( L );  } else { tomMatch1_end_4 = func()  L  { if tom_is_fun_sym_listAU(tomMatch1_end_4) { return tom_get_tail_listAU_L(tomMatch1_end_4) }; return tom_empty_list_listAU() }();  };  };  if !(!(tom_equal_term_L(tomMatch1_end_4, any(l).( L )))) { break; }; };  };  };  };  }; 


    if (0 == cnt) {
      fail(t, "listAU(x,y) should match bb()")
    }
    assertEqualsMsg(t, "Incomplete matching",cnt,5)
}



func TestNeutral4(tt *testing.T) {
    var t T = tom_make_b()
    cnt := 0
    ; { { if tom_is_sort_T(t) { if tom_is_sort_T(any(t).( T )) { var tomMatch2_end_4  T  = any(t).( T ); _ = tomMatch2_end_4; for { { var tom___x  T  = tom_get_slice_concAU(any(t).( T ),tomMatch2_end_4,tom_empty_list_concAU()); _ = tom___x; var tom___y  T  = tomMatch2_end_4; _ = tom___y; 

        if (tom___x == tom_make_b() && tom___y == tom_empty_list_concAU()) { 
          cnt += 2
         }; 
        if (tom___x == tom_empty_list_concAU() && tom___y == tom_make_b()) { 
          cnt += 3
         }; if  (tom_is_empty_concAU_T(tomMatch2_end_4) || tom_equal_term_T(tomMatch2_end_4, tom_empty_list_concAU()))  { tomMatch2_end_4 = any(t).( T );  } else { tomMatch2_end_4 = func()  T  { if tom_is_fun_sym_concAU(tomMatch2_end_4) { return tom_get_tail_concAU_T(tomMatch2_end_4) }; return tom_empty_list_concAU() }();  };  };  if !(!(tom_equal_term_T(tomMatch2_end_4, any(t).( T )))) { break; }; };  };  };  };  }; 


    if (0 == cnt) {
      fail(tt, " concAU(x,y) should match b()")
    }
    assertEqualsMsg(tt, "Incomplete matching",cnt,5)
}



func TestNeutral5(tt *testing.T) {
    var t NAU = tom_make_m()
    cnt := 0
    ; { { if tom_is_sort_NAU(t) { if tom_is_fun_sym_p(any(t).( NAU )) { var tomMatch3_end_4  NAU  = any(t).( NAU ); _ = tomMatch3_end_4; for { { 

        cnt += 2
      if  (tom_is_empty_p_NAU(tomMatch3_end_4) || tom_equal_term_NAU(tomMatch3_end_4, tom_empty_list_p()))  { tomMatch3_end_4 = any(t).( NAU );  } else { tomMatch3_end_4 = func()  NAU  { if tom_is_fun_sym_p(tomMatch3_end_4) { return tom_get_tail_p_NAU(tomMatch3_end_4) }; return tom_empty_list_p() }();  };  };  if !(!(tom_equal_term_NAU(tomMatch3_end_4, any(t).( NAU )))) { break; }; };  };  };  };  }; 

    assertEqualsMsg(tt, "m() does not match p(x*,y*)",cnt,0)
}



func TestMatch1(t *testing.T) {
    var l L = tom_cons_list_listAU(tom_cons_list_listAU(tom_make_aa(),tom_cons_list_listAU(tom_make_bb(),tom_empty_list_listAU())),tom_cons_list_listAU(tom_cons_list_listAU(tom_make_aa(),tom_empty_list_listAU()),tom_cons_list_listAU(tom_cons_list_listAU(tom_make_bb(),tom_cons_list_listAU(tom_make_aa(),tom_empty_list_listAU())),tom_empty_list_listAU())))
    cnt := 0
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { if !( (tom_is_empty_listAU_L(any(l).( L )) || tom_equal_term_L(any(l).( L ), tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_head_listAU_L(any(l).( L )) }; return any(l).( L ) }()).( L )) { 
 cnt++  };  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { var tomMatch4_end_10  L  = any(l).( L ); _ = tomMatch4_end_10; for { { if !( (tom_is_empty_listAU_L(tomMatch4_end_10) || tom_equal_term_L(tomMatch4_end_10, tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_10) { return tom_get_head_listAU_L(tomMatch4_end_10) }; return tomMatch4_end_10 }()).( L )) { var tomMatch4_11  L  = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_10) { return tom_get_tail_listAU_L(tomMatch4_end_10) }; return tom_empty_list_listAU() }(); _ = tomMatch4_11; if  (tom_is_empty_listAU_L(tomMatch4_11) || tom_equal_term_L(tomMatch4_11, tom_empty_list_listAU()))  { 
 cnt++  };  };  }; if  (tom_is_empty_listAU_L(tomMatch4_end_10) || tom_equal_term_L(tomMatch4_end_10, tom_empty_list_listAU()))  { tomMatch4_end_10 = any(l).( L );  } else { tomMatch4_end_10 = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_10) { return tom_get_tail_listAU_L(tomMatch4_end_10) }; return tom_empty_list_listAU() }();  };  };  if !(!(tom_equal_term_L(tomMatch4_end_10, any(l).( L )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { if !( (tom_is_empty_listAU_L(any(l).( L )) || tom_equal_term_L(any(l).( L ), tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_head_listAU_L(any(l).( L )) }; return any(l).( L ) }()).( L )) { var tomMatch4_16  L  = func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_tail_listAU_L(any(l).( L )) }; return tom_empty_list_listAU() }(); _ = tomMatch4_16; if !( (tom_is_empty_listAU_L(tomMatch4_16) || tom_equal_term_L(tomMatch4_16, tom_empty_list_listAU())) ) { var tomMatch4_17  L  = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_16) { return tom_get_tail_listAU_L(tomMatch4_16) }; return tom_empty_list_listAU() }(); _ = tomMatch4_17; if  (tom_is_empty_listAU_L(tomMatch4_17) || tom_equal_term_L(tomMatch4_17, tom_empty_list_listAU()))  { 
 cnt++  };  };  };  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { if !( (tom_is_empty_listAU_L(any(l).( L )) || tom_equal_term_L(any(l).( L ), tom_empty_list_listAU())) ) { var tomMatch4_22  L  = func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_tail_listAU_L(any(l).( L )) }; return tom_empty_list_listAU() }(); _ = tomMatch4_22; if !( (tom_is_empty_listAU_L(tomMatch4_22) || tom_equal_term_L(tomMatch4_22, tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(tomMatch4_22) { return tom_get_head_listAU_L(tomMatch4_22) }; return tomMatch4_22 }()).( L )) { var tomMatch4_23  L  = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_22) { return tom_get_tail_listAU_L(tomMatch4_22) }; return tom_empty_list_listAU() }(); _ = tomMatch4_23; if  (tom_is_empty_listAU_L(tomMatch4_23) || tom_equal_term_L(tomMatch4_23, tom_empty_list_listAU()))  { 
 cnt++  };  };  };  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { if !( (tom_is_empty_listAU_L(any(l).( L )) || tom_equal_term_L(any(l).( L ), tom_empty_list_listAU())) ) { var tomMatch4_28  L  = func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_tail_listAU_L(any(l).( L )) }; return tom_empty_list_listAU() }(); _ = tomMatch4_28; if !( (tom_is_empty_listAU_L(tomMatch4_28) || tom_equal_term_L(tomMatch4_28, tom_empty_list_listAU())) ) { var tomMatch4_29  L  = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_28) { return tom_get_tail_listAU_L(tomMatch4_28) }; return tom_empty_list_listAU() }(); _ = tomMatch4_29; if  (tom_is_empty_listAU_L(tomMatch4_29) || tom_equal_term_L(tomMatch4_29, tom_empty_list_listAU()))  { 
 cnt++  };  };  };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_listAU(any(l).( L )) { if !( (tom_is_empty_listAU_L(any(l).( L )) || tom_equal_term_L(any(l).( L ), tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_head_listAU_L(any(l).( L )) }; return any(l).( L ) }()).( L )) { var tomMatch4_32  L  = func()  L  { if tom_is_fun_sym_listAU(any(l).( L )) { return tom_get_tail_listAU_L(any(l).( L )) }; return tom_empty_list_listAU() }(); _ = tomMatch4_32; var tomMatch4_end_35  L  = tomMatch4_32; _ = tomMatch4_end_35; for { { if !( (tom_is_empty_listAU_L(tomMatch4_end_35) || tom_equal_term_L(tomMatch4_end_35, tom_empty_list_listAU())) ) { if tom_is_fun_sym_bb(any(func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_35) { return tom_get_head_listAU_L(tomMatch4_end_35) }; return tomMatch4_end_35 }()).( L )) { var tomMatch4_36  L  = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_35) { return tom_get_tail_listAU_L(tomMatch4_end_35) }; return tom_empty_list_listAU() }(); _ = tomMatch4_36; if  (tom_is_empty_listAU_L(tomMatch4_36) || tom_equal_term_L(tomMatch4_36, tom_empty_list_listAU()))  { 
 if(tom_get_slice_listAU(tomMatch4_32,tomMatch4_end_35,tom_empty_list_listAU())==tom_make_aa()) {  cnt++  };  };  };  }; if  (tom_is_empty_listAU_L(tomMatch4_end_35) || tom_equal_term_L(tomMatch4_end_35, tom_empty_list_listAU()))  { tomMatch4_end_35 = tomMatch4_32;  } else { tomMatch4_end_35 = func()  L  { if tom_is_fun_sym_listAU(tomMatch4_end_35) { return tom_get_tail_listAU_L(tomMatch4_end_35) }; return tom_empty_list_listAU() }();  };  };  if !(!(tom_equal_term_L(tomMatch4_end_35, tomMatch4_32))) { break; }; };  };  };  };  };  };  }; 

    assertEqualsMsg(t, "Incomplete matching",cnt,6)
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
