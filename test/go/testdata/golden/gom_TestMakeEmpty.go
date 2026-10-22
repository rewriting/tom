package gom












import (
  "testing"
)

  func tom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_make_a()  T  { return (MakeA()) }; func tom_is_fun_sym_conc(t  T ) bool { return func() bool { _, ok := (t).(*ConcT); return ok }();  }; func tom_empty_list_conc()  T  { return (EmptyConc()) }; func tom_cons_list_conc(e  T , l  T )  T  { return (ConsConc(e,l)) }; func tom_get_head_conc_T(l  T )  T  { return (l).GetHeadconc();  }; func tom_get_tail_conc_T(l  T )  T  { return (l).GetTailconc();  }; func tom_is_empty_conc_T(l  T ) bool { return (l).IsEmptyconc();  }; func tom_append_list_conc(l1  T , l2  T )  T  { if tom_is_empty_conc_T(l1) { return l2 } else if tom_is_empty_conc_T(l2) { return l1 } else if tom_is_fun_sym_conc(l1) { if tom_is_empty_conc_T(tom_get_tail_conc_T(l1)) { return tom_cons_list_conc(tom_get_head_conc_T(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_T(l1), tom_append_list_conc(tom_get_tail_conc_T(l1), l2)) } } else { return tom_cons_list_conc(l1, l2) }; }; func tom_get_slice_conc(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_conc_T(end) || tom_equal_term_T(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(func()  T  { if tom_is_fun_sym_conc(begin) { return tom_get_head_conc_T(begin) }; return begin }(), tom_get_slice_conc(func()  T  { if tom_is_fun_sym_conc(begin) { return tom_get_tail_conc_T(begin) }; return tom_empty_list_conc() }(), end, tail)) }; func tom_is_fun_sym_list(t  L ) bool { return func() bool { _, ok := (t).(*ListL); return ok }();  }; func tom_empty_list_list()  L  { return (EmptyList()) }; func tom_cons_list_list(e  T , l  L )  L  { return (ConsList(e,l)) }; func tom_get_head_list_L(l  L )  T  { return (l).GetHeadlist();  }; func tom_get_tail_list_L(l  L )  L  { return (l).GetTaillist();  }; func tom_is_empty_list_L(l  L ) bool { return (l).IsEmptylist();  }; func tom_append_list_list(l1  L , l2  L )  L  { if tom_is_empty_list_L(l1) { return l2 } else if tom_is_empty_list_L(l2) { return l1 } else if tom_is_empty_list_L(tom_get_tail_list_L(l1)) { return tom_cons_list_list(tom_get_head_list_L(l1), l2) } else { return tom_cons_list_list(tom_get_head_list_L(l1), tom_append_list_list(tom_get_tail_list_L(l1), l2)) }; }; func tom_get_slice_list(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_list_L(end) || tom_equal_term_L(end, tom_empty_list_list())) { return begin }; return tom_cons_list_list(tom_get_head_list_L(begin), tom_get_slice_list(tom_get_tail_list_L(begin), end, tail)) }; func tom_make_c()  L  { return (MakeC()) };   func init() {   HookMakeEmptyConc = func() (T) {     tom__r := func() T {     {        return tom_make_a();          };     return nil;     }();     return tom__r;   }; };    func init() {   HookMakeEmptyList = func() (L) {     tom__r := func() L {     {        return tom_make_c();          };     return nil;     }();     return tom__r;   }; };  
















func TestEmptySameDoCo(t *testing.T) {
    assertSame(t, tom_make_a(),tom_empty_list_conc())
}



func TestEmptyNotSameDoCo(t *testing.T) {
    assertSame(t, tom_make_c(),tom_empty_list_list())
}

  
  
  

  
  
  












func assertSame(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected same:<%v> was not:<%v>", expected, actual)
  }
}
