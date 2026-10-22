package gom
















import (
  "testing"
)

  func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_Element(t1  Element , t2  Element ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Element(t any) bool { return func() bool { _, ok := (t).(Element); return ok }();  }; func tom_equal_term_Sorted(t1  Sorted , t2  Sorted ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Sorted(t any) bool { return func() bool { _, ok := (t).(Sorted); return ok }();  }; func tom_make_Int(i  int64 )  Element  { return (MakeInt(i)) }; func tom_make_Str(s  string )  Element  { return (MakeStr(s)) }; func tom_is_fun_sym_MinMax(t  Sorted ) bool { return func() bool { _, ok := (t).(*MinMaxSorted); return ok }();  }; func tom_empty_list_MinMax()  Sorted  { return (EmptyMinMax()) }; func tom_cons_list_MinMax(e  int64 , l  Sorted )  Sorted  { return (ConsMinMax(e,l)) }; func tom_get_head_MinMax_Sorted(l  Sorted )  int64  { return (l).GetHeadMinMax();  }; func tom_get_tail_MinMax_Sorted(l  Sorted )  Sorted  { return (l).GetTailMinMax();  }; func tom_is_empty_MinMax_Sorted(l  Sorted ) bool { return (l).IsEmptyMinMax();  }; func tom_append_list_MinMax(l1  Sorted , l2  Sorted )  Sorted  { if tom_is_empty_MinMax_Sorted(l1) { return l2 } else if tom_is_empty_MinMax_Sorted(l2) { return l1 } else if tom_is_empty_MinMax_Sorted(tom_get_tail_MinMax_Sorted(l1)) { return tom_cons_list_MinMax(tom_get_head_MinMax_Sorted(l1), l2) } else { return tom_cons_list_MinMax(tom_get_head_MinMax_Sorted(l1), tom_append_list_MinMax(tom_get_tail_MinMax_Sorted(l1), l2)) }; }; func tom_get_slice_MinMax(begin  Sorted , end  Sorted , tail  Sorted )  Sorted  { if tom_equal_term_Sorted(begin, end) { return tail } else if tom_equal_term_Sorted(end, tail) && (tom_is_empty_MinMax_Sorted(end) || tom_equal_term_Sorted(end, tom_empty_list_MinMax())) { return begin }; return tom_cons_list_MinMax(tom_get_head_MinMax_Sorted(begin), tom_get_slice_MinMax(tom_get_tail_MinMax_Sorted(begin), end, tail)) }; func tom_is_fun_sym_MaxMin(t  Sorted ) bool { return func() bool { _, ok := (t).(*MaxMinSorted); return ok }();  }; func tom_empty_list_MaxMin()  Sorted  { return (EmptyMaxMin()) }; func tom_cons_list_MaxMin(e  int64 , l  Sorted )  Sorted  { return (ConsMaxMin(e,l)) }; func tom_get_head_MaxMin_Sorted(l  Sorted )  int64  { return (l).GetHeadMaxMin();  }; func tom_get_tail_MaxMin_Sorted(l  Sorted )  Sorted  { return (l).GetTailMaxMin();  }; func tom_is_empty_MaxMin_Sorted(l  Sorted ) bool { return (l).IsEmptyMaxMin();  }; func tom_append_list_MaxMin(l1  Sorted , l2  Sorted )  Sorted  { if tom_is_empty_MaxMin_Sorted(l1) { return l2 } else if tom_is_empty_MaxMin_Sorted(l2) { return l1 } else if tom_is_empty_MaxMin_Sorted(tom_get_tail_MaxMin_Sorted(l1)) { return tom_cons_list_MaxMin(tom_get_head_MaxMin_Sorted(l1), l2) } else { return tom_cons_list_MaxMin(tom_get_head_MaxMin_Sorted(l1), tom_append_list_MaxMin(tom_get_tail_MaxMin_Sorted(l1), l2)) }; }; func tom_get_slice_MaxMin(begin  Sorted , end  Sorted , tail  Sorted )  Sorted  { if tom_equal_term_Sorted(begin, end) { return tail } else if tom_equal_term_Sorted(end, tail) && (tom_is_empty_MaxMin_Sorted(end) || tom_equal_term_Sorted(end, tom_empty_list_MaxMin())) { return begin }; return tom_cons_list_MaxMin(tom_get_head_MaxMin_Sorted(begin), tom_get_slice_MaxMin(tom_get_tail_MaxMin_Sorted(begin), end, tail)) }; func tom_is_fun_sym_NoDup(t  Sorted ) bool { return func() bool { _, ok := (t).(*NoDupSorted); return ok }();  }; func tom_empty_list_NoDup()  Sorted  { return (EmptyNoDup()) }; func tom_cons_list_NoDup(e  int64 , l  Sorted )  Sorted  { return (ConsNoDup(e,l)) }; func tom_get_head_NoDup_Sorted(l  Sorted )  int64  { return (l).GetHeadNoDup();  }; func tom_get_tail_NoDup_Sorted(l  Sorted )  Sorted  { return (l).GetTailNoDup();  }; func tom_is_empty_NoDup_Sorted(l  Sorted ) bool { return (l).IsEmptyNoDup();  }; func tom_append_list_NoDup(l1  Sorted , l2  Sorted )  Sorted  { if tom_is_empty_NoDup_Sorted(l1) { return l2 } else if tom_is_empty_NoDup_Sorted(l2) { return l1 } else if tom_is_empty_NoDup_Sorted(tom_get_tail_NoDup_Sorted(l1)) { return tom_cons_list_NoDup(tom_get_head_NoDup_Sorted(l1), l2) } else { return tom_cons_list_NoDup(tom_get_head_NoDup_Sorted(l1), tom_append_list_NoDup(tom_get_tail_NoDup_Sorted(l1), l2)) }; }; func tom_get_slice_NoDup(begin  Sorted , end  Sorted , tail  Sorted )  Sorted  { if tom_equal_term_Sorted(begin, end) { return tail } else if tom_equal_term_Sorted(end, tail) && (tom_is_empty_NoDup_Sorted(end) || tom_equal_term_Sorted(end, tom_empty_list_NoDup())) { return begin }; return tom_cons_list_NoDup(tom_get_head_NoDup_Sorted(begin), tom_get_slice_NoDup(tom_get_tail_NoDup_Sorted(begin), end, tail)) }; func tom_is_fun_sym_Mix(t  Sorted ) bool { return func() bool { _, ok := (t).(*MixSorted); return ok }();  }; func tom_empty_list_Mix()  Sorted  { return (EmptyMix()) }; func tom_cons_list_Mix(e  Element , l  Sorted )  Sorted  { return (ConsMix(e,l)) }; func tom_get_head_Mix_Sorted(l  Sorted )  Element  { return (l).GetHeadMix();  }; func tom_get_tail_Mix_Sorted(l  Sorted )  Sorted  { return (l).GetTailMix();  }; func tom_is_empty_Mix_Sorted(l  Sorted ) bool { return (l).IsEmptyMix();  }; func tom_append_list_Mix(l1  Sorted , l2  Sorted )  Sorted  { if tom_is_empty_Mix_Sorted(l1) { return l2 } else if tom_is_empty_Mix_Sorted(l2) { return l1 } else if tom_is_empty_Mix_Sorted(tom_get_tail_Mix_Sorted(l1)) { return tom_cons_list_Mix(tom_get_head_Mix_Sorted(l1), l2) } else { return tom_cons_list_Mix(tom_get_head_Mix_Sorted(l1), tom_append_list_Mix(tom_get_tail_Mix_Sorted(l1), l2)) }; }; func tom_get_slice_Mix(begin  Sorted , end  Sorted , tail  Sorted )  Sorted  { if tom_equal_term_Sorted(begin, end) { return tail } else if tom_equal_term_Sorted(end, tail) && (tom_is_empty_Mix_Sorted(end) || tom_equal_term_Sorted(end, tom_empty_list_Mix())) { return begin }; return tom_cons_list_Mix(tom_get_head_Mix_Sorted(begin), tom_get_slice_Mix(tom_get_tail_Mix_Sorted(begin), end, tail)) };   func init() {   HookMakeInsertMinMax = func(head int64, tail Sorted) (Sorted, int64, Sorted) {     tom__r := func() Sorted {     {     ; { { var tomMatch1_0  Sorted  = RealMakeConsMinMax(head,tail); _ = tomMatch1_0; if tom_is_sort_Sorted(tomMatch1_0) { if tom_is_fun_sym_MinMax(any(tomMatch1_0).( Sorted )) { if !(tom_is_empty_MinMax_Sorted(any(tomMatch1_0).( Sorted ))) { var tom___x  int64  = tom_get_head_MinMax_Sorted(any(tomMatch1_0).( Sorted )); _ = tom___x; var tomMatch1_2  Sorted  = tom_get_tail_MinMax_Sorted(any(tomMatch1_0).( Sorted )); _ = tomMatch1_2; var tomMatch1_end_5  Sorted  = tomMatch1_2; _ = tomMatch1_end_5; for { { if !(tom_is_empty_MinMax_Sorted(tomMatch1_end_5)) { var tom___y  int64  = tom_get_head_MinMax_Sorted(tomMatch1_end_5); _ = tom___y; if tom___x > tom___y {  return tom_cons_list_MinMax(tom___y,tom_append_list_MinMax(tom_get_slice_MinMax(tomMatch1_2,tomMatch1_end_5,tom_empty_list_MinMax()),tom_cons_list_MinMax(tom___x,tom_append_list_MinMax(tom_get_tail_MinMax_Sorted(tomMatch1_end_5),tom_empty_list_MinMax()))));  };  }; if tom_is_empty_MinMax_Sorted(tomMatch1_end_5) { tomMatch1_end_5 = tomMatch1_2;  } else { tomMatch1_end_5 = tom_get_tail_MinMax_Sorted(tomMatch1_end_5);  };  };  if !(!(tom_equal_term_Sorted(tomMatch1_end_5, tomMatch1_2))) { break; }; };  };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };    func init() {   HookMakeInsertMaxMin = func(head int64, tail Sorted) (Sorted, int64, Sorted) {     tom__r := func() Sorted {     {     ; { { var tomMatch2_0  Sorted  = RealMakeConsMaxMin(head,tail); _ = tomMatch2_0; if tom_is_sort_Sorted(tomMatch2_0) { if tom_is_fun_sym_MaxMin(any(tomMatch2_0).( Sorted )) { if !(tom_is_empty_MaxMin_Sorted(any(tomMatch2_0).( Sorted ))) { var tom___x  int64  = tom_get_head_MaxMin_Sorted(any(tomMatch2_0).( Sorted )); _ = tom___x; var tomMatch2_2  Sorted  = tom_get_tail_MaxMin_Sorted(any(tomMatch2_0).( Sorted )); _ = tomMatch2_2; var tomMatch2_end_5  Sorted  = tomMatch2_2; _ = tomMatch2_end_5; for { { if !(tom_is_empty_MaxMin_Sorted(tomMatch2_end_5)) { var tom___y  int64  = tom_get_head_MaxMin_Sorted(tomMatch2_end_5); _ = tom___y; if tom___x < tom___y {  return tom_cons_list_MaxMin(tom___y,tom_append_list_MaxMin(tom_get_slice_MaxMin(tomMatch2_2,tomMatch2_end_5,tom_empty_list_MaxMin()),tom_cons_list_MaxMin(tom___x,tom_append_list_MaxMin(tom_get_tail_MaxMin_Sorted(tomMatch2_end_5),tom_empty_list_MaxMin()))));  };  }; if tom_is_empty_MaxMin_Sorted(tomMatch2_end_5) { tomMatch2_end_5 = tomMatch2_2;  } else { tomMatch2_end_5 = tom_get_tail_MaxMin_Sorted(tomMatch2_end_5);  };  };  if !(!(tom_equal_term_Sorted(tomMatch2_end_5, tomMatch2_2))) { break; }; };  };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };    func init() {   HookMakeInsertNoDup = func(head int64, tail Sorted) (Sorted, int64, Sorted) {     tom__r := func() Sorted {     {     ; { { var tomMatch3_0  Sorted  = RealMakeConsNoDup(head,tail); _ = tomMatch3_0; if tom_is_sort_Sorted(tomMatch3_0) { if tom_is_fun_sym_NoDup(any(tomMatch3_0).( Sorted )) { if !(tom_is_empty_NoDup_Sorted(any(tomMatch3_0).( Sorted ))) { var tom___x  int64  = tom_get_head_NoDup_Sorted(any(tomMatch3_0).( Sorted )); _ = tom___x; var tomMatch3_2  Sorted  = tom_get_tail_NoDup_Sorted(any(tomMatch3_0).( Sorted )); _ = tomMatch3_2; var tomMatch3_end_5  Sorted  = tomMatch3_2; _ = tomMatch3_end_5; for { { if !(tom_is_empty_NoDup_Sorted(tomMatch3_end_5)) { if tom_equal_term_int(tom_get_head_NoDup_Sorted(tomMatch3_end_5), tom___x) {  return tom_append_list_NoDup(tom_get_slice_NoDup(tomMatch3_2,tomMatch3_end_5,tom_empty_list_NoDup()),tom_cons_list_NoDup(tom___x,tom_append_list_NoDup(tom_get_tail_NoDup_Sorted(tomMatch3_end_5),tom_empty_list_NoDup())));  };  }; if tom_is_empty_NoDup_Sorted(tomMatch3_end_5) { tomMatch3_end_5 = tomMatch3_2;  } else { tomMatch3_end_5 = tom_get_tail_NoDup_Sorted(tomMatch3_end_5);  };  };  if !(!(tom_equal_term_Sorted(tomMatch3_end_5, tomMatch3_2))) { break; }; };  };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };    func init() {   HookMakeInsertMix = func(head Element, tail Sorted) (Sorted, Element, Sorted) {     tom__r := func() Sorted {     {     ; { { var tomMatch4_0  Sorted  = RealMakeConsMix(head,tail); _ = tomMatch4_0; if tom_is_sort_Sorted(tomMatch4_0) { if tom_is_fun_sym_Mix(any(tomMatch4_0).( Sorted )) { if !(tom_is_empty_Mix_Sorted(any(tomMatch4_0).( Sorted ))) { var tom___x  Element  = tom_get_head_Mix_Sorted(any(tomMatch4_0).( Sorted )); _ = tom___x; var tomMatch4_2  Sorted  = tom_get_tail_Mix_Sorted(any(tomMatch4_0).( Sorted )); _ = tomMatch4_2; var tomMatch4_end_5  Sorted  = tomMatch4_2; _ = tomMatch4_end_5; for { { if !(tom_is_empty_Mix_Sorted(tomMatch4_end_5)) { var tom___y  Element  = tom_get_head_Mix_Sorted(tomMatch4_end_5); _ = tom___y; if (true == greaterThan(tom___x,tom___y)) {  return tom_cons_list_Mix(tom___y,tom_append_list_Mix(tom_get_slice_Mix(tomMatch4_2,tomMatch4_end_5,tom_empty_list_Mix()),tom_cons_list_Mix(tom___x,tom_append_list_Mix(tom_get_tail_Mix_Sorted(tomMatch4_end_5),tom_empty_list_Mix()))));  };  }; if tom_is_empty_Mix_Sorted(tomMatch4_end_5) { tomMatch4_end_5 = tomMatch4_2;  } else { tomMatch4_end_5 = tom_get_tail_Mix_Sorted(tomMatch4_end_5);  };  };  if !(!(tom_equal_term_Sorted(tomMatch4_end_5, tomMatch4_2))) { break; }; };  };  };  };  };  };       };     return nil;     }();     return tom__r, head, tail;   }; };           func greaterThan(o1 Element, o2 Element) bool {           if (o1.CompareToLPO(o2)>0) {             return true           };           return false;         }         ;  






























func TestSorta(t *testing.T) {
    var l Sorted = tom_cons_list_MinMax(4,tom_cons_list_MinMax(6,tom_cons_list_MinMax(2,tom_cons_list_MinMax(0,tom_cons_list_MinMax(-2,tom_cons_list_MinMax(3,tom_empty_list_MinMax()))))))
    assertEquals(t, int64(-2),l.GetHeadMinMax())
}



func TestSortMaxMin(t *testing.T) {
    var l Sorted = tom_cons_list_MaxMin(4,tom_cons_list_MaxMin(6,tom_cons_list_MaxMin(2,tom_cons_list_MaxMin(0,tom_cons_list_MaxMin(-2,tom_cons_list_MaxMin(3,tom_empty_list_MaxMin()))))))
    assertEquals(t, int64(6),l.GetHeadMaxMin())
}



func TestSortNoDup(t *testing.T) {
    var l Sorted = tom_cons_list_NoDup(4,tom_cons_list_NoDup(6,tom_cons_list_NoDup(2,tom_cons_list_NoDup(0,tom_cons_list_NoDup(-2,tom_cons_list_NoDup(3,tom_empty_list_NoDup()))))))
    var dup Sorted = tom_append_list_NoDup(l,tom_append_list_NoDup(l,tom_empty_list_NoDup()))
    assertEquals(t, l,dup)
}



func TestSortMix(t *testing.T) {
    var l1 Sorted = tom_cons_list_Mix(tom_make_Int(3),tom_cons_list_Mix(tom_make_Str("a"),tom_cons_list_Mix(tom_make_Int(6),tom_cons_list_Mix(tom_make_Str("foo"),tom_cons_list_Mix(tom_make_Str("bar"),tom_empty_list_Mix())))))
    var l2 Sorted = tom_cons_list_Mix(tom_make_Str("foo"),tom_cons_list_Mix(tom_make_Str("bar"),tom_cons_list_Mix(tom_make_Int(6),tom_cons_list_Mix(tom_make_Str("a"),tom_cons_list_Mix(tom_make_Int(3),tom_empty_list_Mix())))))
    assertEquals(t, l1,l2)
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
