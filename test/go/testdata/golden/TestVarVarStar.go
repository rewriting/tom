





























package testvarvarstar



import "testing"

func tom_equal_term_A(t1  A , t2  A ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_A(t any) bool { return func() bool { _, ok := (t).(A); return ok }();  }; func tom_equal_term_B(t1  B , t2  B ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_B(t any) bool { return func() bool { _, ok := (t).(B); return ok }();  }; func tom_equal_term_BList(t1  BList , t2  BList ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_BList(t any) bool { return func() bool { _, ok := (t).(BList); return ok }();  }; func tom_is_fun_sym_a(t  A ) bool { return func() bool { _, ok := (t).(*AA); return ok }();  }; func tom_make_a(s1  BList , s2  BList )  A  { return (MakeA(s1,s2)) }; func tom_get_slot_a_s1(t  A )  BList  { return (t).(*AA).S1;  }; func tom_get_slot_a_s2(t  A )  BList  { return (t).(*AA).S2;  }; func tom_make_b()  B  { return (MakeB()) }; func tom_is_fun_sym_conc(t  BList ) bool { return func() bool { _, ok := (t).(*ConcBList); return ok }();  }; func tom_empty_list_conc()  BList  { return (EmptyConc()) }; func tom_cons_list_conc(e  B , l  BList )  BList  { return (ConsConc(e,l)) }; func tom_get_head_conc_BList(l  BList )  B  { return (l).GetHeadconc();  }; func tom_get_tail_conc_BList(l  BList )  BList  { return (l).GetTailconc();  }; func tom_is_empty_conc_BList(l  BList ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  BList , l2  BList )  BList  { if tom_is_empty_conc_BList(l1) { return l2 } else if tom_is_empty_conc_BList(l2) { return l1 } else if tom_is_empty_conc_BList(tom_get_tail_conc_BList(l1)) { return tom_cons_list_conc(tom_get_head_conc_BList(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_BList(l1), tom_append_list_conc(tom_get_tail_conc_BList(l1), l2)) }; }; func tom_get_slice_conc(begin  BList , end  BList , tail  BList )  BList  { if tom_equal_term_BList(begin, end) { return tail } else if tom_equal_term_BList(end, tail) && (tom_is_empty_conc_BList(end) || tom_equal_term_BList(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_BList(begin), tom_get_slice_conc(tom_get_tail_conc_BList(begin), end, tail)) }; 









func Test1(t *testing.T) {
  var a A = tom_make_a(tom_empty_list_conc(),tom_empty_list_conc()); { { if tom_is_sort_A(a) { if tom_is_fun_sym_a(any(a).( A )) { var tomMatch1_1  BList  = tom_get_slot_a_s1(any(a).( A )); _ = tomMatch1_1; var tomMatch1_2  BList  = tom_get_slot_a_s2(any(a).( A )); _ = tomMatch1_2; if tom_is_sort_BList(tomMatch1_1) { if tom_is_fun_sym_conc(any(tomMatch1_2).( BList )) { if tom_equal_term_BList(tomMatch1_1, tomMatch1_2) { 

 return  };  };  };  };  };  };  }; 

  t.FailNow()
}



func Test2(t *testing.T) {
  var a A = tom_make_a(tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc())); { { if tom_is_sort_A(a) { if tom_is_fun_sym_a(any(a).( A )) { var tomMatch2_1  BList  = tom_get_slot_a_s1(any(a).( A )); _ = tomMatch2_1; var tomMatch2_2  BList  = tom_get_slot_a_s2(any(a).( A )); _ = tomMatch2_2; if tom_is_sort_BList(tomMatch2_1) { if tom_is_fun_sym_conc(any(tomMatch2_2).( BList )) { if tom_equal_term_BList(tomMatch2_1, tomMatch2_2) { 

 return  };  };  };  };  };  };  }; 

  t.FailNow()
}



func Test3(t *testing.T) {
  counter := 0
  _ = counter
  var a A = tom_make_a(tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_b(),tom_empty_list_conc()))); { { if tom_is_sort_A(a) { if tom_is_fun_sym_a(any(a).( A )) { var tomMatch3_1  BList  = tom_get_slot_a_s1(any(a).( A )); _ = tomMatch3_1; var tomMatch3_2  BList  = tom_get_slot_a_s2(any(a).( A )); _ = tomMatch3_2; if tom_is_sort_BList(tomMatch3_1) { if tom_is_fun_sym_conc(any(tomMatch3_2).( BList )) { if tom_equal_term_BList(tomMatch3_1, tomMatch3_2) { 

 t.FailNow() };  };  };  };  };  };  }; 

  return

}
