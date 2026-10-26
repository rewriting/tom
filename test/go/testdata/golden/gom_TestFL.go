package gom


















import (
  "testing"
)

  func tom_TestFL_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_TestFL_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_TestFL_make_a()  T  { return (MakeA()) }; func tom_TestFL_is_fun_sym_b(t  T ) bool { return func() bool { _, ok := (t).(*BT); return ok }();  }; func tom_TestFL_make_b()  T  { return (MakeB()) }; func tom_TestFL_is_fun_sym_conc(t  T ) bool { return func() bool { _, ok := (t).(*ConcT); return ok }();  }; func tom_TestFL_empty_list_conc()  T  { return (EmptyConc()) }; func tom_TestFL_cons_list_conc(e  T , l  T )  T  { return (ConsConc(e,l)) }; func tom_TestFL_get_head_conc_T(l  T )  T  { return (l).GetHeadconc();  }; func tom_TestFL_get_tail_conc_T(l  T )  T  { return (l).GetTailconc();  }; func tom_TestFL_is_empty_conc_T(l  T ) bool { return (l).IsEmptyconc();  }; func tom_TestFL_append_list_conc(l1  T , l2  T )  T  { if tom_TestFL_is_empty_conc_T(l1) { return l2 } else if tom_TestFL_is_empty_conc_T(l2) { return l1 } else if tom_TestFL_is_fun_sym_conc(l1) { if tom_TestFL_is_empty_conc_T(tom_TestFL_get_tail_conc_T(l1)) { return tom_TestFL_cons_list_conc(tom_TestFL_get_head_conc_T(l1), l2) } else { return tom_TestFL_cons_list_conc(tom_TestFL_get_head_conc_T(l1), tom_TestFL_append_list_conc(tom_TestFL_get_tail_conc_T(l1), l2)) } } else { return tom_TestFL_cons_list_conc(l1, l2) }; }; func tom_TestFL_get_slice_conc(begin  T , end  T , tail  T )  T  { if tom_TestFL_equal_term_T(begin, end) { return tail } else if tom_TestFL_equal_term_T(end, tail) && (tom_TestFL_is_empty_conc_T(end) || tom_TestFL_equal_term_T(end, tom_TestFL_empty_list_conc())) { return begin }; return tom_TestFL_cons_list_conc(func()  T  { if tom_TestFL_is_fun_sym_conc(begin) { return tom_TestFL_get_head_conc_T(begin) }; return begin }(), tom_TestFL_get_slice_conc(func()  T  { if tom_TestFL_is_fun_sym_conc(begin) { return tom_TestFL_get_tail_conc_T(begin) }; return tom_TestFL_empty_list_conc() }(), end, tail)) }; func tom_TestFL_is_fun_sym_and(t  T ) bool { return func() bool { _, ok := (t).(*AndT); return ok }();  }; func tom_TestFL_empty_list_and()  T  { return (EmptyAnd()) }; func tom_TestFL_cons_list_and(e  T , l  T )  T  { return (ConsAnd(e,l)) }; func tom_TestFL_get_head_and_T(l  T )  T  { return (l).GetHeadand();  }; func tom_TestFL_get_tail_and_T(l  T )  T  { return (l).GetTailand();  }; func tom_TestFL_is_empty_and_T(l  T ) bool { return (l).IsEmptyand();  }; func tom_TestFL_append_list_and(l1  T , l2  T )  T  { if tom_TestFL_is_empty_and_T(l1) { return l2 } else if tom_TestFL_is_empty_and_T(l2) { return l1 } else if tom_TestFL_is_fun_sym_and(l1) { if tom_TestFL_is_empty_and_T(tom_TestFL_get_tail_and_T(l1)) { return tom_TestFL_cons_list_and(tom_TestFL_get_head_and_T(l1), l2) } else { return tom_TestFL_cons_list_and(tom_TestFL_get_head_and_T(l1), tom_TestFL_append_list_and(tom_TestFL_get_tail_and_T(l1), l2)) } } else { return tom_TestFL_cons_list_and(l1, l2) }; }; func tom_TestFL_get_slice_and(begin  T , end  T , tail  T )  T  { if tom_TestFL_equal_term_T(begin, end) { return tail } else if tom_TestFL_equal_term_T(end, tail) && (tom_TestFL_is_empty_and_T(end) || tom_TestFL_equal_term_T(end, tom_TestFL_empty_list_and())) { return begin }; return tom_TestFL_cons_list_and(func()  T  { if tom_TestFL_is_fun_sym_and(begin) { return tom_TestFL_get_head_and_T(begin) }; return begin }(), tom_TestFL_get_slice_and(func()  T  { if tom_TestFL_is_fun_sym_and(begin) { return tom_TestFL_get_tail_and_T(begin) }; return tom_TestFL_empty_list_and() }(), end, tail)) }; func tom_TestFL_is_fun_sym_or(t  T ) bool { return func() bool { _, ok := (t).(*OrT); return ok }();  }; func tom_TestFL_empty_list_or()  T  { return (EmptyOr()) }; func tom_TestFL_cons_list_or(e  T , l  T )  T  { return (ConsOr(e,l)) }; func tom_TestFL_get_head_or_T(l  T )  T  { return (l).GetHeador();  }; func tom_TestFL_get_tail_or_T(l  T )  T  { return (l).GetTailor();  }; func tom_TestFL_is_empty_or_T(l  T ) bool { return (l).IsEmptyor();  }; func tom_TestFL_append_list_or(l1  T , l2  T )  T  { if tom_TestFL_is_empty_or_T(l1) { return l2 } else if tom_TestFL_is_empty_or_T(l2) { return l1 } else if tom_TestFL_is_fun_sym_or(l1) { if tom_TestFL_is_empty_or_T(tom_TestFL_get_tail_or_T(l1)) { return tom_TestFL_cons_list_or(tom_TestFL_get_head_or_T(l1), l2) } else { return tom_TestFL_cons_list_or(tom_TestFL_get_head_or_T(l1), tom_TestFL_append_list_or(tom_TestFL_get_tail_or_T(l1), l2)) } } else { return tom_TestFL_cons_list_or(l1, l2) }; }; func tom_TestFL_get_slice_or(begin  T , end  T , tail  T )  T  { if tom_TestFL_equal_term_T(begin, end) { return tail } else if tom_TestFL_equal_term_T(end, tail) && (tom_TestFL_is_empty_or_T(end) || tom_TestFL_equal_term_T(end, tom_TestFL_empty_list_or())) { return begin }; return tom_TestFL_cons_list_or(func()  T  { if tom_TestFL_is_fun_sym_or(begin) { return tom_TestFL_get_head_or_T(begin) }; return begin }(), tom_TestFL_get_slice_or(func()  T  { if tom_TestFL_is_fun_sym_or(begin) { return tom_TestFL_get_tail_or_T(begin) }; return tom_TestFL_empty_list_or() }(), end, tail)) }; func tom_TestFL_is_fun_sym_t(t  T ) bool { return func() bool { _, ok := (t).(*TT); return ok }();  }; func tom_TestFL_make_t()  T  { return (MakeT()) }; func tom_TestFL_is_fun_sym_f(t  T ) bool { return func() bool { _, ok := (t).(*FT); return ok }();  }; func tom_TestFL_make_f()  T  { return (MakeF()) };   func init() {   HookMakeInsertAnd = func(e T, l T) (T, T, T) {     tom__r := func() T {     {        ; { { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_or(any(e).( T )) { if  (tom_TestFL_is_empty_or_T(any(e).( T )) || tom_TestFL_equal_term_T(any(e).( T ), tom_TestFL_empty_list_or()))  {  return l; ;  };  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_or(any(e).( T )) { if !( (tom_TestFL_is_empty_or_T(any(e).( T )) || tom_TestFL_equal_term_T(any(e).( T ), tom_TestFL_empty_list_or())) ) { if tom_TestFL_is_fun_sym_t(any(func()  T  { if tom_TestFL_is_fun_sym_or(any(e).( T )) { return tom_TestFL_get_head_or_T(any(e).( T )) }; return any(e).( T ) }()).( T )) { var tomMatch1_4  T  = func()  T  { if tom_TestFL_is_fun_sym_or(any(e).( T )) { return tom_TestFL_get_tail_or_T(any(e).( T )) }; return tom_TestFL_empty_list_or() }(); if  (tom_TestFL_is_empty_or_T(tomMatch1_4) || tom_TestFL_equal_term_T(tomMatch1_4, tom_TestFL_empty_list_or()))  {  return tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_cons_list_and(l,tom_TestFL_empty_list_and())); ;  };  };  };  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_t(any(e).( T )) {  return l; ;  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_f(any(e).( T )) {  if !l.IsEmptyand() { return tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and())};  };  };  };  };           };     return nil;     }();     return tom__r, e, l;   }; };    func init() {   HookMakeInsertOr = func(e T, l T) (T, T, T) {     tom__r := func() T {     {        ; { { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_and(any(e).( T )) { if  (tom_TestFL_is_empty_and_T(any(e).( T )) || tom_TestFL_equal_term_T(any(e).( T ), tom_TestFL_empty_list_and()))  {  return l; ;  };  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_and(any(e).( T )) { if !( (tom_TestFL_is_empty_and_T(any(e).( T )) || tom_TestFL_equal_term_T(any(e).( T ), tom_TestFL_empty_list_and())) ) { if tom_TestFL_is_fun_sym_f(any(func()  T  { if tom_TestFL_is_fun_sym_and(any(e).( T )) { return tom_TestFL_get_head_and_T(any(e).( T )) }; return any(e).( T ) }()).( T )) { var tomMatch2_4  T  = func()  T  { if tom_TestFL_is_fun_sym_and(any(e).( T )) { return tom_TestFL_get_tail_and_T(any(e).( T )) }; return tom_TestFL_empty_list_and() }(); if  (tom_TestFL_is_empty_and_T(tomMatch2_4) || tom_TestFL_equal_term_T(tomMatch2_4, tom_TestFL_empty_list_and()))  {  return tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_cons_list_or(l,tom_TestFL_empty_list_or())); ;  };  };  };  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_t(any(e).( T )) {  if !l.IsEmptyor() { return tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or())};  };  };  }; { if tom_TestFL_is_sort_T(e) { if tom_TestFL_is_fun_sym_f(any(e).( T )) {  return l; ;  };  };  };  };           };     return nil;     }();     return tom__r, e, l;   }; };  


































func TestFlatten1_1(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc()),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc()),tom_TestFL_empty_list_conc())),tom_TestFL_empty_list_conc())
}
func TestFlatten1_2(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc()))),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc()))
}
func TestFlatten1_3(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc()))))),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_empty_list_conc())))
}
func TestFlatten1_4(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_empty_list_conc())),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_empty_list_conc())))))
}



func TestFlatten2_1(tt *testing.T) {
    var t T = tom_TestFL_empty_list_conc()
    assertTrue(tt, t.IsEmptyconc())
}
func TestFlatten2_2(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc()))
    assertFalse(tt, t.IsEmptyconc())
}
func TestFlatten2_3(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc()))
    assertTrue(tt, t.IsConsconc())
}
func TestFlatten2_4(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc()))
    assertEquals(tt, t.GetHeadconc(),tom_TestFL_make_a())
}
func TestFlatten2_5(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc()))
    assertTrue(tt, t.GetTailconc().IsEmptyconc())
}
func TestFlatten2_6(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())),tom_TestFL_empty_list_conc())))



    assertEquals(tt, t,tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_cons_list_conc(tom_TestFL_make_a(),tom_TestFL_empty_list_conc())))))))))))))
}



func TestFlatten3(tt *testing.T) {
    var t T = tom_TestFL_empty_list_conc()
    for i:=0 ; i<10 ; i++ {
      t = ConsConc(t,tom_TestFL_make_a())
    }
    cpt := 0
    for !t.IsEmptyconc() {
      assertEquals(tt, t.GetHeadconc(),tom_TestFL_make_a())
      cpt++
      t = t.GetTailconc()
    }
    assertEquals(tt, cpt,10)
}



func TestMatch1(tt *testing.T) {
    var t T = tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_empty_list_conc())),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc()),tom_TestFL_cons_list_conc(tom_TestFL_cons_list_conc(tom_TestFL_make_b(),tom_TestFL_cons_list_conc(tom_TestFL_empty_list_conc(),tom_TestFL_empty_list_conc())),tom_TestFL_empty_list_conc())))
    cnt := 0
    ; { { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { if !( (tom_TestFL_is_empty_conc_T(any(t).( T )) || tom_TestFL_equal_term_T(any(t).( T ), tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_head_conc_T(any(t).( T )) }; return any(t).( T ) }()).( T )) { 
 cnt++ ;  };  };  };  };  }; { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { var tomMatch3_end_10  T  = any(t).( T ); for { { if !( (tom_TestFL_is_empty_conc_T(tomMatch3_end_10) || tom_TestFL_equal_term_T(tomMatch3_end_10, tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_10) { return tom_TestFL_get_head_conc_T(tomMatch3_end_10) }; return tomMatch3_end_10 }()).( T )) { var tomMatch3_11  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_10) { return tom_TestFL_get_tail_conc_T(tomMatch3_end_10) }; return tom_TestFL_empty_list_conc() }(); if  (tom_TestFL_is_empty_conc_T(tomMatch3_11) || tom_TestFL_equal_term_T(tomMatch3_11, tom_TestFL_empty_list_conc()))  { 
 cnt++ ;  };  };  }; if  (tom_TestFL_is_empty_conc_T(tomMatch3_end_10) || tom_TestFL_equal_term_T(tomMatch3_end_10, tom_TestFL_empty_list_conc()))  { tomMatch3_end_10 = any(t).( T );  } else { tomMatch3_end_10 = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_10) { return tom_TestFL_get_tail_conc_T(tomMatch3_end_10) }; return tom_TestFL_empty_list_conc() }();  };  };  if !(!(tom_TestFL_equal_term_T(tomMatch3_end_10, any(t).( T )))) { break; }; };  };  };  }; { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { if !( (tom_TestFL_is_empty_conc_T(any(t).( T )) || tom_TestFL_equal_term_T(any(t).( T ), tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_head_conc_T(any(t).( T )) }; return any(t).( T ) }()).( T )) { var tomMatch3_16  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_tail_conc_T(any(t).( T )) }; return tom_TestFL_empty_list_conc() }(); if !( (tom_TestFL_is_empty_conc_T(tomMatch3_16) || tom_TestFL_equal_term_T(tomMatch3_16, tom_TestFL_empty_list_conc())) ) { var tomMatch3_17  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_16) { return tom_TestFL_get_tail_conc_T(tomMatch3_16) }; return tom_TestFL_empty_list_conc() }(); if  (tom_TestFL_is_empty_conc_T(tomMatch3_17) || tom_TestFL_equal_term_T(tomMatch3_17, tom_TestFL_empty_list_conc()))  { 
 cnt++ ;  };  };  };  };  };  };  }; { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { if !( (tom_TestFL_is_empty_conc_T(any(t).( T )) || tom_TestFL_equal_term_T(any(t).( T ), tom_TestFL_empty_list_conc())) ) { var tomMatch3_22  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_tail_conc_T(any(t).( T )) }; return tom_TestFL_empty_list_conc() }(); if !( (tom_TestFL_is_empty_conc_T(tomMatch3_22) || tom_TestFL_equal_term_T(tomMatch3_22, tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_22) { return tom_TestFL_get_head_conc_T(tomMatch3_22) }; return tomMatch3_22 }()).( T )) { var tomMatch3_23  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_22) { return tom_TestFL_get_tail_conc_T(tomMatch3_22) }; return tom_TestFL_empty_list_conc() }(); if  (tom_TestFL_is_empty_conc_T(tomMatch3_23) || tom_TestFL_equal_term_T(tomMatch3_23, tom_TestFL_empty_list_conc()))  { 
 cnt++ ;  };  };  };  };  };  };  }; { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { if !( (tom_TestFL_is_empty_conc_T(any(t).( T )) || tom_TestFL_equal_term_T(any(t).( T ), tom_TestFL_empty_list_conc())) ) { var tomMatch3_28  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_tail_conc_T(any(t).( T )) }; return tom_TestFL_empty_list_conc() }(); if !( (tom_TestFL_is_empty_conc_T(tomMatch3_28) || tom_TestFL_equal_term_T(tomMatch3_28, tom_TestFL_empty_list_conc())) ) { var tomMatch3_29  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_28) { return tom_TestFL_get_tail_conc_T(tomMatch3_28) }; return tom_TestFL_empty_list_conc() }(); if  (tom_TestFL_is_empty_conc_T(tomMatch3_29) || tom_TestFL_equal_term_T(tomMatch3_29, tom_TestFL_empty_list_conc()))  { 
 cnt++ ;  };  };  };  };  };  }; { if tom_TestFL_is_sort_T(t) { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { if !( (tom_TestFL_is_empty_conc_T(any(t).( T )) || tom_TestFL_equal_term_T(any(t).( T ), tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_head_conc_T(any(t).( T )) }; return any(t).( T ) }()).( T )) { var tomMatch3_32  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(any(t).( T )) { return tom_TestFL_get_tail_conc_T(any(t).( T )) }; return tom_TestFL_empty_list_conc() }(); var tomMatch3_end_35  T  = tomMatch3_32; for { { if !( (tom_TestFL_is_empty_conc_T(tomMatch3_end_35) || tom_TestFL_equal_term_T(tomMatch3_end_35, tom_TestFL_empty_list_conc())) ) { if tom_TestFL_is_fun_sym_b(any(func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_35) { return tom_TestFL_get_head_conc_T(tomMatch3_end_35) }; return tomMatch3_end_35 }()).( T )) { var tomMatch3_36  T  = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_35) { return tom_TestFL_get_tail_conc_T(tomMatch3_end_35) }; return tom_TestFL_empty_list_conc() }(); if  (tom_TestFL_is_empty_conc_T(tomMatch3_36) || tom_TestFL_equal_term_T(tomMatch3_36, tom_TestFL_empty_list_conc()))  { 
 if(tom_TestFL_get_slice_conc(tomMatch3_32,tomMatch3_end_35,tom_TestFL_empty_list_conc())==tom_TestFL_empty_list_conc()) { cnt++ };  };  };  }; if  (tom_TestFL_is_empty_conc_T(tomMatch3_end_35) || tom_TestFL_equal_term_T(tomMatch3_end_35, tom_TestFL_empty_list_conc()))  { tomMatch3_end_35 = tomMatch3_32;  } else { tomMatch3_end_35 = func()  T  { if tom_TestFL_is_fun_sym_conc(tomMatch3_end_35) { return tom_TestFL_get_tail_conc_T(tomMatch3_end_35) }; return tom_TestFL_empty_list_conc() }();  };  };  if !(!(tom_TestFL_equal_term_T(tomMatch3_end_35, tomMatch3_32))) { break; }; };  };  };  };  };  };  }; 

    assertEqualsMsg(tt, "Incomplete matching",cnt,6)
}



func TestHook1_1(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and()))
}
func TestHook1_2(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and()))
}
func TestHook1_3(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and()))
}
func TestHook1_4(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and()))
}
func TestHook1_5(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and()),tom_TestFL_empty_list_and())
}
func TestHook1_6(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or()))
}
func TestHook1_7(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or()))
}
func TestHook1_8(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or()))
}
func TestHook1_9(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or()))
}
func TestHook1_10(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or()),tom_TestFL_empty_list_or())
}
func TestHook1_11(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_and(tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_and(tom_TestFL_cons_list_or(tom_TestFL_make_t(),tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or())),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and()))
}
func TestHook1_12(t *testing.T) {
    assertEquals(t, tom_TestFL_cons_list_or(tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_empty_list_and())),tom_TestFL_cons_list_or(tom_TestFL_cons_list_and(tom_TestFL_make_t(),tom_TestFL_cons_list_and(tom_TestFL_make_f(),tom_TestFL_empty_list_and())),tom_TestFL_empty_list_or())),tom_TestFL_cons_list_or(tom_TestFL_make_f(),tom_TestFL_empty_list_or()))
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




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}




func assertFalse(t *testing.T, condition bool) {
  t.Helper()
  if condition {
    t.Fatal()
  }
}
