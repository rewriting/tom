





























package testvarvarstar



import "testing"

func tom_TestVarVarStar_equal_term_A(t1  A , t2  A ) bool { return (t1).Equivalent(t2);  }; func tom_TestVarVarStar_is_sort_A(t any) bool { return func() bool { _, ok := (t).(A); return ok }();  }; func tom_TestVarVarStar_equal_term_B(t1  B , t2  B ) bool { return (t1).Equivalent(t2);  }; func tom_TestVarVarStar_is_sort_B(t any) bool { return func() bool { _, ok := (t).(B); return ok }();  }; func tom_TestVarVarStar_equal_term_BList(t1  BList , t2  BList ) bool { return (t1).Equivalent(t2);  }; func tom_TestVarVarStar_is_sort_BList(t any) bool { return func() bool { _, ok := (t).(BList); return ok }();  }; func tom_TestVarVarStar_is_fun_sym_a(t  A ) bool { return func() bool { _, ok := (t).(*AA); return ok }();  }; func tom_TestVarVarStar_make_a(s1  BList , s2  BList )  A  { return (MakeA(s1,s2)) }; func tom_TestVarVarStar_get_slot_a_s1(t  A )  BList  { return (t).(*AA).S1;  }; func tom_TestVarVarStar_get_slot_a_s2(t  A )  BList  { return (t).(*AA).S2;  }; func tom_TestVarVarStar_make_b()  B  { return (MakeB()) }; func tom_TestVarVarStar_is_fun_sym_conc(t  BList ) bool { return func() bool { _, ok := (t).(*ConcBList); return ok }();  }; func tom_TestVarVarStar_empty_list_conc()  BList  { return (EmptyConc()) }; func tom_TestVarVarStar_cons_list_conc(e  B , l  BList )  BList  { return (ConsConc(e,l)) }; func tom_TestVarVarStar_get_head_conc_BList(l  BList )  B  { return (l).GetHeadconc();  }; func tom_TestVarVarStar_get_tail_conc_BList(l  BList )  BList  { return (l).GetTailconc();  }; func tom_TestVarVarStar_is_empty_conc_BList(l  BList ) bool { return (l).IsEmptyconc();  }; func tom_TestVarVarStar_append_list_conc(l1  BList , l2  BList )  BList  { if tom_TestVarVarStar_is_empty_conc_BList(l1) { return l2 } else if tom_TestVarVarStar_is_empty_conc_BList(l2) { return l1 } else if tom_TestVarVarStar_is_empty_conc_BList(tom_TestVarVarStar_get_tail_conc_BList(l1)) { return tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_get_head_conc_BList(l1), l2) } else { return tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_get_head_conc_BList(l1), tom_TestVarVarStar_append_list_conc(tom_TestVarVarStar_get_tail_conc_BList(l1), l2)) }; }; func tom_TestVarVarStar_get_slice_conc(begin  BList , end  BList , tail  BList )  BList  { if tom_TestVarVarStar_equal_term_BList(begin, end) { return tail } else if tom_TestVarVarStar_equal_term_BList(end, tail) && (tom_TestVarVarStar_is_empty_conc_BList(end) || tom_TestVarVarStar_equal_term_BList(end, tom_TestVarVarStar_empty_list_conc())) { return begin }; return tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_get_head_conc_BList(begin), tom_TestVarVarStar_get_slice_conc(tom_TestVarVarStar_get_tail_conc_BList(begin), end, tail)) }; 









func Test1(t *testing.T) {
  var a A = tom_TestVarVarStar_make_a(tom_TestVarVarStar_empty_list_conc(),tom_TestVarVarStar_empty_list_conc()); { { if tom_TestVarVarStar_is_sort_A(a) { if tom_TestVarVarStar_is_fun_sym_a(any(a).( A )) { var tomMatch1_1  BList  = tom_TestVarVarStar_get_slot_a_s1(any(a).( A )); var tomMatch1_2  BList  = tom_TestVarVarStar_get_slot_a_s2(any(a).( A )); if tom_TestVarVarStar_is_sort_BList(tomMatch1_1) { if tom_TestVarVarStar_is_fun_sym_conc(any(tomMatch1_2).( BList )) { if tom_TestVarVarStar_equal_term_BList(tomMatch1_1, tomMatch1_2) { 

 return ;  };  };  };  };  };  };  }; 

  t.FailNow()
}



func Test2(t *testing.T) {
  var a A = tom_TestVarVarStar_make_a(tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_make_b(),tom_TestVarVarStar_empty_list_conc()),tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_make_b(),tom_TestVarVarStar_empty_list_conc())); { { if tom_TestVarVarStar_is_sort_A(a) { if tom_TestVarVarStar_is_fun_sym_a(any(a).( A )) { var tomMatch2_1  BList  = tom_TestVarVarStar_get_slot_a_s1(any(a).( A )); var tomMatch2_2  BList  = tom_TestVarVarStar_get_slot_a_s2(any(a).( A )); if tom_TestVarVarStar_is_sort_BList(tomMatch2_1) { if tom_TestVarVarStar_is_fun_sym_conc(any(tomMatch2_2).( BList )) { if tom_TestVarVarStar_equal_term_BList(tomMatch2_1, tomMatch2_2) { 

 return ;  };  };  };  };  };  };  }; 

  t.FailNow()
}



func Test3(t *testing.T) {
  counter := 0
  _ = counter
  var a A = tom_TestVarVarStar_make_a(tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_make_b(),tom_TestVarVarStar_empty_list_conc()),tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_make_b(),tom_TestVarVarStar_cons_list_conc(tom_TestVarVarStar_make_b(),tom_TestVarVarStar_empty_list_conc()))); { { if tom_TestVarVarStar_is_sort_A(a) { if tom_TestVarVarStar_is_fun_sym_a(any(a).( A )) { var tomMatch3_1  BList  = tom_TestVarVarStar_get_slot_a_s1(any(a).( A )); var tomMatch3_2  BList  = tom_TestVarVarStar_get_slot_a_s2(any(a).( A )); if tom_TestVarVarStar_is_sort_BList(tomMatch3_1) { if tom_TestVarVarStar_is_fun_sym_conc(any(tomMatch3_2).( BList )) { if tom_TestVarVarStar_equal_term_BList(tomMatch3_1, tomMatch3_2) { 

 t.FailNow();  };  };  };  };  };  };  }; 

  return

}
