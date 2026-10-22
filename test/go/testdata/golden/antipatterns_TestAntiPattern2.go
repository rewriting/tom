package antipatterns












import (
  "testing"
)

  func tom_equal_term_TwoPath(t1  TwoPath , t2  TwoPath ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_TwoPath(t any) bool { return func() bool { _, ok := (t).(TwoPath); return ok }();  }; func tom_is_fun_sym_id(t  TwoPath ) bool { return func() bool { _, ok := (t).(*IdTwoPath); return ok }();  }; func tom_make_id()  TwoPath  { return (MakeId()) }; func tom_make_g()  TwoPath  { return (MakeG()) }; func tom_is_fun_sym_c0(t  TwoPath ) bool { return func() bool { _, ok := (t).(*C0TwoPath); return ok }();  }; func tom_empty_list_c0()  TwoPath  { return (EmptyC0()) }; func tom_cons_list_c0(e  TwoPath , l  TwoPath )  TwoPath  { return (ConsC0(e,l)) }; func tom_get_head_c0_TwoPath(l  TwoPath )  TwoPath  { return (l).GetHeadc0();  }; func tom_get_tail_c0_TwoPath(l  TwoPath )  TwoPath  { return (l).GetTailc0();  }; func tom_is_empty_c0_TwoPath(l  TwoPath ) bool { return (l).IsEmptyc0();  }; func tom_append_list_c0(l1  TwoPath , l2  TwoPath )  TwoPath  { if tom_is_empty_c0_TwoPath(l1) { return l2 } else if tom_is_empty_c0_TwoPath(l2) { return l1 } else if tom_is_fun_sym_c0(l1) { if tom_is_empty_c0_TwoPath(tom_get_tail_c0_TwoPath(l1)) { return tom_cons_list_c0(tom_get_head_c0_TwoPath(l1), l2) } else { return tom_cons_list_c0(tom_get_head_c0_TwoPath(l1), tom_append_list_c0(tom_get_tail_c0_TwoPath(l1), l2)) } } else { return tom_cons_list_c0(l1, l2) }; }; func tom_get_slice_c0(begin  TwoPath , end  TwoPath , tail  TwoPath )  TwoPath  { if tom_equal_term_TwoPath(begin, end) { return tail } else if tom_equal_term_TwoPath(end, tail) && (tom_is_empty_c0_TwoPath(end) || tom_equal_term_TwoPath(end, tom_empty_list_c0())) { return begin }; return tom_cons_list_c0(func()  TwoPath  { if tom_is_fun_sym_c0(begin) { return tom_get_head_c0_TwoPath(begin) }; return begin }(), tom_get_slice_c0(func()  TwoPath  { if tom_is_fun_sym_c0(begin) { return tom_get_tail_c0_TwoPath(begin) }; return tom_empty_list_c0() }(), end, tail)) }; func tom_is_fun_sym_c1(t  TwoPath ) bool { return func() bool { _, ok := (t).(*C1TwoPath); return ok }();  }; func tom_empty_list_c1()  TwoPath  { return (EmptyC1()) }; func tom_cons_list_c1(e  TwoPath , l  TwoPath )  TwoPath  { return (ConsC1(e,l)) }; func tom_get_head_c1_TwoPath(l  TwoPath )  TwoPath  { return (l).GetHeadc1();  }; func tom_get_tail_c1_TwoPath(l  TwoPath )  TwoPath  { return (l).GetTailc1();  }; func tom_is_empty_c1_TwoPath(l  TwoPath ) bool { return (l).IsEmptyc1();  }; func tom_append_list_c1(l1  TwoPath , l2  TwoPath )  TwoPath  { if tom_is_empty_c1_TwoPath(l1) { return l2 } else if tom_is_empty_c1_TwoPath(l2) { return l1 } else if tom_is_fun_sym_c1(l1) { if tom_is_empty_c1_TwoPath(tom_get_tail_c1_TwoPath(l1)) { return tom_cons_list_c1(tom_get_head_c1_TwoPath(l1), l2) } else { return tom_cons_list_c1(tom_get_head_c1_TwoPath(l1), tom_append_list_c1(tom_get_tail_c1_TwoPath(l1), l2)) } } else { return tom_cons_list_c1(l1, l2) }; }; func tom_get_slice_c1(begin  TwoPath , end  TwoPath , tail  TwoPath )  TwoPath  { if tom_equal_term_TwoPath(begin, end) { return tail } else if tom_equal_term_TwoPath(end, tail) && (tom_is_empty_c1_TwoPath(end) || tom_equal_term_TwoPath(end, tom_empty_list_c1())) { return begin }; return tom_cons_list_c1(func()  TwoPath  { if tom_is_fun_sym_c1(begin) { return tom_get_head_c1_TwoPath(begin) }; return begin }(), tom_get_slice_c1(func()  TwoPath  { if tom_is_fun_sym_c1(begin) { return tom_get_tail_c1_TwoPath(begin) }; return tom_empty_list_c1() }(), end, tail)) }; 









func Test1(t *testing.T) {
    var res TwoPath = tom_cons_list_c1(tom_cons_list_c0(tom_make_g(),tom_cons_list_c0(tom_make_id(),tom_empty_list_c0())),tom_cons_list_c1(tom_cons_list_c0(tom_make_id(),tom_cons_list_c0(tom_make_g(),tom_empty_list_c0())),tom_empty_list_c1())); { { if tom_is_sort_TwoPath(res) { if tom_is_fun_sym_c1(any(res).( TwoPath )) { if !( (tom_is_empty_c1_TwoPath(any(res).( TwoPath )) || tom_equal_term_TwoPath(any(res).( TwoPath ), tom_empty_list_c1())) ) { var tomMatch1_5  TwoPath  = func()  TwoPath  { if tom_is_fun_sym_c1(any(res).( TwoPath )) { return tom_get_head_c1_TwoPath(any(res).( TwoPath )) }; return any(res).( TwoPath ) }(); _ = tomMatch1_5; if tom_is_fun_sym_c0(any(tomMatch1_5).( TwoPath )) { var tomMatch1_end_8  TwoPath  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !( (tom_is_empty_c0_TwoPath(tomMatch1_end_8) || tom_equal_term_TwoPath(tomMatch1_end_8, tom_empty_list_c0())) ) { var tomMatch1_2  TwoPath  = func()  TwoPath  { if tom_is_fun_sym_c1(any(res).( TwoPath )) { return tom_get_tail_c1_TwoPath(any(res).( TwoPath )) }; return tom_empty_list_c1() }(); _ = tomMatch1_2; if !( (tom_is_empty_c1_TwoPath(tomMatch1_2) || tom_equal_term_TwoPath(tomMatch1_2, tom_empty_list_c1())) ) { var tomMatch1_11  TwoPath  = func()  TwoPath  { if tom_is_fun_sym_c1(tomMatch1_2) { return tom_get_head_c1_TwoPath(tomMatch1_2) }; return tomMatch1_2 }(); _ = tomMatch1_11; if tom_is_fun_sym_c0(any(tomMatch1_11).( TwoPath )) { var tomMatch1_end_14  TwoPath  = tomMatch1_11; _ = tomMatch1_end_14; for { { if !( (tom_is_empty_c0_TwoPath(tomMatch1_end_14) || tom_equal_term_TwoPath(tomMatch1_end_14, tom_empty_list_c0())) ) { var tom___g  TwoPath  = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_14) { return tom_get_head_c0_TwoPath(tomMatch1_end_14) }; return tomMatch1_end_14 }(); _ = tom___g; var tomMatch1_24 bool =  false ; _ = tomMatch1_24; if tom_is_fun_sym_id(any(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_14) { return tom_get_head_c0_TwoPath(tomMatch1_end_14) }; return tomMatch1_end_14 }()).( TwoPath )) { if tom_equal_term_TwoPath(tom___g, func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_14) { return tom_get_head_c0_TwoPath(tomMatch1_end_14) }; return tomMatch1_end_14 }()) { tomMatch1_24 =  true ;  };  }; if !(tomMatch1_24) { var tomMatch1_23 bool =  false ; _ = tomMatch1_23; if tom_is_fun_sym_id(any(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_8) { return tom_get_head_c0_TwoPath(tomMatch1_end_8) }; return tomMatch1_end_8 }()).( TwoPath )) { if tom_equal_term_TwoPath(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_8) { return tom_get_head_c0_TwoPath(tomMatch1_end_8) }; return tomMatch1_end_8 }(), func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_8) { return tom_get_head_c0_TwoPath(tomMatch1_end_8) }; return tomMatch1_end_8 }()) { tomMatch1_23 =  true ;  };  }; if !(tomMatch1_23) { 


        if(tom___g.Isid()) {  };  };  };  }; if  (tom_is_empty_c0_TwoPath(tomMatch1_end_14) || tom_equal_term_TwoPath(tomMatch1_end_14, tom_empty_list_c0()))  { tomMatch1_end_14 = tomMatch1_11;  } else { tomMatch1_end_14 = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_14) { return tom_get_tail_c0_TwoPath(tomMatch1_end_14) }; return tom_empty_list_c0() }();  };  };  if !(!(tom_equal_term_TwoPath(tomMatch1_end_14, tomMatch1_11))) { break; }; };  };  };  }; if  (tom_is_empty_c0_TwoPath(tomMatch1_end_8) || tom_equal_term_TwoPath(tomMatch1_end_8, tom_empty_list_c0()))  { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch1_end_8) { return tom_get_tail_c0_TwoPath(tomMatch1_end_8) }; return tom_empty_list_c0() }();  };  };  if !(!(tom_equal_term_TwoPath(tomMatch1_end_8, tomMatch1_5))) { break; }; };  };  };  };  };  };  }; 




}



func Test2(t *testing.T) {
    var tt TwoPath = tom_make_g(); { { if tom_is_sort_TwoPath(tt) { var tomMatch2_3 bool =  false ; _ = tomMatch2_3; if tom_is_fun_sym_id(any(tt).( TwoPath )) { tomMatch2_3 =  true ;  }; if !(tomMatch2_3) { 

 return;   };  };  };  }; 

    fail(t)
}



func Test3(t *testing.T) {
    var p1 TwoPath = tom_cons_list_c0(tom_make_g(),tom_cons_list_c0(tom_make_id(),tom_empty_list_c0()))
    var p2 TwoPath = tom_cons_list_c0(tom_make_id(),tom_cons_list_c0(tom_make_g(),tom_empty_list_c0())); { { if tom_is_sort_TwoPath(p1) { if tom_is_fun_sym_c0(any(p1).( TwoPath )) { var tomMatch3_end_5  TwoPath  = any(p1).( TwoPath ); _ = tomMatch3_end_5; for { { if !( (tom_is_empty_c0_TwoPath(tomMatch3_end_5) || tom_equal_term_TwoPath(tomMatch3_end_5, tom_empty_list_c0())) ) { if tom_is_sort_TwoPath(p2) { if tom_is_fun_sym_c0(any(p2).( TwoPath )) { var tomMatch3_end_11  TwoPath  = any(p2).( TwoPath ); _ = tomMatch3_end_11; for { { if !( (tom_is_empty_c0_TwoPath(tomMatch3_end_11) || tom_equal_term_TwoPath(tomMatch3_end_11, tom_empty_list_c0())) ) { var tom___g  TwoPath  = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_11) { return tom_get_head_c0_TwoPath(tomMatch3_end_11) }; return tomMatch3_end_11 }(); _ = tom___g; var tomMatch3_21 bool =  false ; _ = tomMatch3_21; if tom_is_fun_sym_id(any(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_11) { return tom_get_head_c0_TwoPath(tomMatch3_end_11) }; return tomMatch3_end_11 }()).( TwoPath )) { if tom_equal_term_TwoPath(tom___g, func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_11) { return tom_get_head_c0_TwoPath(tomMatch3_end_11) }; return tomMatch3_end_11 }()) { tomMatch3_21 =  true ;  };  }; if !(tomMatch3_21) { var tomMatch3_20 bool =  false ; _ = tomMatch3_20; if tom_is_fun_sym_id(any(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_5) { return tom_get_head_c0_TwoPath(tomMatch3_end_5) }; return tomMatch3_end_5 }()).( TwoPath )) { if tom_equal_term_TwoPath(func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_5) { return tom_get_head_c0_TwoPath(tomMatch3_end_5) }; return tomMatch3_end_5 }(), func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_5) { return tom_get_head_c0_TwoPath(tomMatch3_end_5) }; return tomMatch3_end_5 }()) { tomMatch3_20 =  true ;  };  }; if !(tomMatch3_20) { 


        if(tom___g.Isid()) {  };  };  };  }; if  (tom_is_empty_c0_TwoPath(tomMatch3_end_11) || tom_equal_term_TwoPath(tomMatch3_end_11, tom_empty_list_c0()))  { tomMatch3_end_11 = any(p2).( TwoPath );  } else { tomMatch3_end_11 = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_11) { return tom_get_tail_c0_TwoPath(tomMatch3_end_11) }; return tom_empty_list_c0() }();  };  };  if !(!(tom_equal_term_TwoPath(tomMatch3_end_11, any(p2).( TwoPath )))) { break; }; };  };  };  }; if  (tom_is_empty_c0_TwoPath(tomMatch3_end_5) || tom_equal_term_TwoPath(tomMatch3_end_5, tom_empty_list_c0()))  { tomMatch3_end_5 = any(p1).( TwoPath );  } else { tomMatch3_end_5 = func()  TwoPath  { if tom_is_fun_sym_c0(tomMatch3_end_5) { return tom_get_tail_c0_TwoPath(tomMatch3_end_5) }; return tom_empty_list_c0() }();  };  };  if !(!(tom_equal_term_TwoPath(tomMatch3_end_5, any(p1).( TwoPath )))) { break; }; };  };  };  };  }; 




}





func fail(t *testing.T) {
  t.Helper()
  t.Fatal()
}
