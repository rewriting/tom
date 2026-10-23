package testbackquote





import "testing"

func tom_TestBackQuote_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_TestBackQuote_is_sort_char(t rune) bool { return true;  }; func tom_TestBackQuote_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_TestBackQuote_is_sort_String(t string) bool { return true;  }; func tom_TestBackQuote_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_TestBackQuote_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_TestBackQuote_equal_term_E(t1  E , t2  E ) bool { return (t1).Equivalent(t2);  }; func tom_TestBackQuote_is_sort_E(t any) bool { return func() bool { _, ok := (t).(E); return ok }();  }; func tom_TestBackQuote_is_fun_sym_conc(t  L ) bool { return func() bool { _, ok := (t).(*ConcL); return ok }();  }; func tom_TestBackQuote_empty_list_conc()  L  { return (EmptyConc()) }; func tom_TestBackQuote_cons_list_conc(e  E , l  L )  L  { return (ConsConc(e,l)) }; func tom_TestBackQuote_get_head_conc_L(l  L )  E  { return (l).GetHeadconc();  }; func tom_TestBackQuote_get_tail_conc_L(l  L )  L  { return (l).GetTailconc();  }; func tom_TestBackQuote_is_empty_conc_L(l  L ) bool { return (l).IsEmptyconc();  }; func tom_TestBackQuote_append_list_conc(l1  L , l2  L )  L  { if tom_TestBackQuote_is_empty_conc_L(l1) { return l2 } else if tom_TestBackQuote_is_empty_conc_L(l2) { return l1 } else if tom_TestBackQuote_is_empty_conc_L(tom_TestBackQuote_get_tail_conc_L(l1)) { return tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_get_head_conc_L(l1), l2) } else { return tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_get_head_conc_L(l1), tom_TestBackQuote_append_list_conc(tom_TestBackQuote_get_tail_conc_L(l1), l2)) }; }; func tom_TestBackQuote_get_slice_conc(begin  L , end  L , tail  L )  L  { if tom_TestBackQuote_equal_term_L(begin, end) { return tail } else if tom_TestBackQuote_equal_term_L(end, tail) && (tom_TestBackQuote_is_empty_conc_L(end) || tom_TestBackQuote_equal_term_L(end, tom_TestBackQuote_empty_list_conc())) { return begin }; return tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_get_head_conc_L(begin), tom_TestBackQuote_get_slice_conc(tom_TestBackQuote_get_tail_conc_L(begin), end, tail)) }; func tom_TestBackQuote_is_fun_sym_a(t  E ) bool { return func() bool { _, ok := (t).(*AE); return ok }();  }; func tom_TestBackQuote_make_a()  E  { return (MakeA()) }; func tom_TestBackQuote_is_fun_sym_b(t  E ) bool { return func() bool { _, ok := (t).(*BE); return ok }();  }; func tom_TestBackQuote_make_b()  E  { return (MakeB()) }; func tom_TestBackQuote_make_c()  E  { return (MakeC()) }; 









func Test1(t *testing.T) {
  var l L = tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_a(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_b(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_c(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_a(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_b(),tom_TestBackQuote_empty_list_conc())))))
  var l2 L = tom_TestBackQuote_empty_list_conc(); { _ = l; { if tom_TestBackQuote_is_sort_L(l) { if tom_TestBackQuote_is_fun_sym_conc(any(l).( L )) { var tomMatch1_end_4  L  = any(l).( L ); _ = tomMatch1_end_4; for { { if !(tom_TestBackQuote_is_empty_conc_L(tomMatch1_end_4)) { if tom_TestBackQuote_is_fun_sym_b(any(tom_TestBackQuote_get_head_conc_L(tomMatch1_end_4)).( E )) { var tomMatch1_5  L  = tom_TestBackQuote_get_tail_conc_L(tomMatch1_end_4); _ = tomMatch1_5; var tomMatch1_end_8  L  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !(tom_TestBackQuote_is_empty_conc_L(tomMatch1_end_8)) { if tom_TestBackQuote_is_fun_sym_a(any(tom_TestBackQuote_get_head_conc_L(tomMatch1_end_8)).( E )) { 










      l2 = tom_TestBackQuote_append_list_conc(tom_TestBackQuote_get_slice_conc(any(l).( L ),tomMatch1_end_4,tom_TestBackQuote_empty_list_conc()),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_b(),tom_TestBackQuote_append_list_conc(tom_TestBackQuote_get_slice_conc(tomMatch1_5,tomMatch1_end_8,tom_TestBackQuote_empty_list_conc()),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_a(),tom_TestBackQuote_append_list_conc(tom_TestBackQuote_get_tail_conc_L(tomMatch1_end_8),tom_TestBackQuote_empty_list_conc())))));  };  }; if tom_TestBackQuote_is_empty_conc_L(tomMatch1_end_8) { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = tom_TestBackQuote_get_tail_conc_L(tomMatch1_end_8);  };  };  if !(!(tom_TestBackQuote_equal_term_L(tomMatch1_end_8, tomMatch1_5))) { break; }; };  };  }; if tom_TestBackQuote_is_empty_conc_L(tomMatch1_end_4) { tomMatch1_end_4 = any(l).( L );  } else { tomMatch1_end_4 = tom_TestBackQuote_get_tail_conc_L(tomMatch1_end_4);  };  };  if !(!(tom_TestBackQuote_equal_term_L(tomMatch1_end_4, any(l).( L )))) { break; }; };  };  };  };  }; 







  _ = l2
  return
}




func abc() L {
  return tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_a(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_b(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_c(),tom_TestBackQuote_empty_list_conc())))
}



func Test2(t *testing.T) {
  assertEquals(t, tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_a(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_b(),tom_TestBackQuote_cons_list_conc(tom_TestBackQuote_make_c(),tom_TestBackQuote_empty_list_conc()))),tom_TestBackQuote_append_list_conc(abc(),tom_TestBackQuote_empty_list_conc())) 
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
