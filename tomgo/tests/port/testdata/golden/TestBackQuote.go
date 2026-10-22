package testbackquote



import "testing"

func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_equal_term_E(t1  E , t2  E ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_E(t any) bool { return func() bool { _, ok := (t).(E); return ok }();  }; func tom_is_fun_sym_conc(t  L ) bool { return func() bool { _, ok := (t).(*ConcL); return ok }();  }; func tom_empty_list_conc()  L  { return (MakeConc()) }; func tom_cons_list_conc(e  E , l  L )  L  { return (func() L { s := (l).(*ConcL).Slots; return MakeConc(append([]E{e}, s...)...) }()) }; func tom_get_head_conc_L(l  L )  E  { return (l).(*ConcL).Slots[0];  }; func tom_get_tail_conc_L(l  L )  L  { return func() L { s := (l).(*ConcL).Slots; return MakeConc(s[1:]...) }();  }; func tom_is_empty_conc_L(l  L ) bool { return len((l).(*ConcL).Slots) == 0;  }; func tom_append_list_conc(l1  L , l2  L )  L  { if tom_is_empty_conc_L(l1) { return l2 } else if tom_is_empty_conc_L(l2) { return l1 } else if tom_is_empty_conc_L(tom_get_tail_conc_L(l1)) { return tom_cons_list_conc(tom_get_head_conc_L(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_L(l1), tom_append_list_conc(tom_get_tail_conc_L(l1), l2)) }; }; func tom_get_slice_conc(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_conc_L(end) || tom_equal_term_L(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_L(begin), tom_get_slice_conc(tom_get_tail_conc_L(begin), end, tail)) }; func tom_is_fun_sym_a(t  E ) bool { return func() bool { _, ok := (t).(*AE); return ok }();  }; func tom_make_a()  E  { return (MakeA()) }; func tom_is_fun_sym_b(t  E ) bool { return func() bool { _, ok := (t).(*BE); return ok }();  }; func tom_make_b()  E  { return (MakeB()) }; func tom_make_c()  E  { return (MakeC()) }; 














func abc() L {
	return tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))
}









func TestRoundTrip(t *testing.T) {
	l := tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())))))
	var l2 L
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( L )) { var tomMatch1_end_4  L  = any(l).( L ); _ = tomMatch1_end_4; for { { if !(tom_is_empty_conc_L(tomMatch1_end_4)) { if tom_is_fun_sym_b(any(tom_get_head_conc_L(tomMatch1_end_4)).( E )) { var tomMatch1_5  L  = tom_get_tail_conc_L(tomMatch1_end_4); _ = tomMatch1_5; var tomMatch1_end_8  L  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !(tom_is_empty_conc_L(tomMatch1_end_8)) { if tom_is_fun_sym_a(any(tom_get_head_conc_L(tomMatch1_end_8)).( E )) { 

			l2 = tom_append_list_conc(tom_get_slice_conc(any(l).( L ),tomMatch1_end_4,tom_empty_list_conc()),tom_cons_list_conc(tom_make_b(),tom_append_list_conc(tom_get_slice_conc(tomMatch1_5,tomMatch1_end_8,tom_empty_list_conc()),tom_cons_list_conc(tom_make_a(),tom_append_list_conc(tom_get_tail_conc_L(tomMatch1_end_8),tom_empty_list_conc()))))) };  }; if tom_is_empty_conc_L(tomMatch1_end_8) { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = tom_get_tail_conc_L(tomMatch1_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_8, tomMatch1_5))) { break; }; };  };  }; if tom_is_empty_conc_L(tomMatch1_end_4) { tomMatch1_end_4 = any(l).( L );  } else { tomMatch1_end_4 = tom_get_tail_conc_L(tomMatch1_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_4, any(l).( L )))) { break; }; };  };  };  };  }; 


	if !l.Equivalent(l2) {
		t.Errorf("round-trip mismatch:\n  got  %v\n  want %v", l2, l)
	}
}












func TestAbcDirect(t *testing.T) {
	want := tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))
	got := abc()
	if !want.Equivalent(got) {
		t.Errorf("abc() = %v, want %v", got, want)
	}
}
