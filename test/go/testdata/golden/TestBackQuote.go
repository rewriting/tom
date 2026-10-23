package testbackquote





import "testing"

func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_equal_term_E(t1  E , t2  E ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_E(t any) bool { return func() bool { _, ok := (t).(E); return ok }();  }; func tom_is_fun_sym_conc(t  L ) bool { return func() bool { _, ok := (t).(*ConcL); return ok }();  }; func tom_empty_list_conc()  L  { return (EmptyConc()) }; func tom_cons_list_conc(e  E , l  L )  L  { return (ConsConc(e,l)) }; func tom_get_head_conc_L(l  L )  E  { return (l).GetHeadconc();  }; func tom_get_tail_conc_L(l  L )  L  { return (l).GetTailconc();  }; func tom_is_empty_conc_L(l  L ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  L , l2  L )  L  { if tom_is_empty_conc_L(l1) { return l2 } else if tom_is_empty_conc_L(l2) { return l1 } else if tom_is_empty_conc_L(tom_get_tail_conc_L(l1)) { return tom_cons_list_conc(tom_get_head_conc_L(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_L(l1), tom_append_list_conc(tom_get_tail_conc_L(l1), l2)) }; }; func tom_get_slice_conc(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_conc_L(end) || tom_equal_term_L(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_L(begin), tom_get_slice_conc(tom_get_tail_conc_L(begin), end, tail)) }; func tom_is_fun_sym_a(t  E ) bool { return func() bool { _, ok := (t).(*AE); return ok }();  }; func tom_make_a()  E  { return (MakeA()) }; func tom_is_fun_sym_b(t  E ) bool { return func() bool { _, ok := (t).(*BE); return ok }();  }; func tom_make_b()  E  { return (MakeB()) }; func tom_make_c()  E  { return (MakeC()) }; 









func Test1(t *testing.T) {
  var l L = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())))))
  var l2 L = tom_empty_list_conc(); { _ = l; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( L )) { var tomMatch1_end_4  L  = any(l).( L ); _ = tomMatch1_end_4; for { { if !(tom_is_empty_conc_L(tomMatch1_end_4)) { if tom_is_fun_sym_b(any(tom_get_head_conc_L(tomMatch1_end_4)).( E )) { var tomMatch1_5  L  = tom_get_tail_conc_L(tomMatch1_end_4); _ = tomMatch1_5; var tomMatch1_end_8  L  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !(tom_is_empty_conc_L(tomMatch1_end_8)) { if tom_is_fun_sym_a(any(tom_get_head_conc_L(tomMatch1_end_8)).( E )) { 










      l2 = tom_append_list_conc(tom_get_slice_conc(any(l).( L ),tomMatch1_end_4,tom_empty_list_conc()),tom_cons_list_conc(tom_make_b(),tom_append_list_conc(tom_get_slice_conc(tomMatch1_5,tomMatch1_end_8,tom_empty_list_conc()),tom_cons_list_conc(tom_make_a(),tom_append_list_conc(tom_get_tail_conc_L(tomMatch1_end_8),tom_empty_list_conc())))));  };  }; if tom_is_empty_conc_L(tomMatch1_end_8) { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = tom_get_tail_conc_L(tomMatch1_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_8, tomMatch1_5))) { break; }; };  };  }; if tom_is_empty_conc_L(tomMatch1_end_4) { tomMatch1_end_4 = any(l).( L );  } else { tomMatch1_end_4 = tom_get_tail_conc_L(tomMatch1_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_4, any(l).( L )))) { break; }; };  };  };  };  }; 







  _ = l2
  return
}




func abc() L {
  return tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))
}



func Test2(t *testing.T) {
  assertEquals(t, tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc()))),tom_append_list_conc(abc(),tom_empty_list_conc())) 
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
