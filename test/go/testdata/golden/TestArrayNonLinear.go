






package testarraynonlinear



import "testing"

func tom_TestArrayNonLinear_equal_term_ArrayList(l1  []any , l2  []any ) bool { return func(l1, l2 []any) bool { var eq func(l1, l2 []any) bool; eq = func(l1, l2 []any) bool { if len(l1) != len(l2) { return false }; for i := range l1 { a, ok1 := l1[i].([]any); b, ok2 := l2[i].([]any); if ok1 || ok2 { if !ok1 || !ok2 || !eq(a, b) { return false }; continue }; if l1[i] != l2[i] { return false } }; return true }; return eq(l1, l2) }(l1, l2);  }; func tom_TestArrayNonLinear_is_sort_ArrayList(t any) bool { return t != nil;  }; func tom_TestArrayNonLinear_equal_term_Object(o1  any , o2  any ) bool { return o1 == o2;  }; func tom_TestArrayNonLinear_is_sort_Object(t any) bool { return true;  }; func tom_TestArrayNonLinear_is_fun_sym_concArrayList(l  []any ) bool { return (l != nil);  }; func tom_TestArrayNonLinear_empty_array_concArrayList(n int64)  []any  { return (make([]any, 0, n)) }; func tom_TestArrayNonLinear_cons_array_concArrayList(o  any , l  []any )  []any  { return (func(o any, l []any) []any { res := make([]any, len(l), len(l)+1); copy(res, l); return append(res, o) }(o,l)) }; func tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(l  []any , n int64)  any  { return l[n];  }; func tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(l  []any ) int64 { return int64(len(l));  }; func tom_TestArrayNonLinear_get_slice_concArrayList(subject  []any , begin int64, end int64)  []any  { var result  []any  = tom_TestArrayNonLinear_empty_array_concArrayList(end-begin); for begin != end { result = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(subject, begin), result); begin++ }; return result }; func tom_TestArrayNonLinear_append_array_concArrayList(l2  []any , l1  []any )  []any  { var size1 int64 = tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(l1); var size2 int64 = tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(l2); var index int64; var result  []any  = tom_TestArrayNonLinear_empty_array_concArrayList(size1+size2); index = size1; for index > 0 { result = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(l2, size2-index), result); index-- }; return result }; func tom_TestArrayNonLinear_make_a()  any  { return ("a") }; func tom_TestArrayNonLinear_make_b()  any  { return ("b") }; func tom_TestArrayNonLinear_make_c()  any  { return ("c") }; func tom_TestArrayNonLinear_is_fun_sym_F(t  any ) bool { return isF(t);  }; func tom_TestArrayNonLinear_make_F(t  []any )  any  { return (&F{t}) }; func tom_TestArrayNonLinear_get_slot_F_l(t  any )  []any  { return t.(*F).l;  }; 





















func Test1(t *testing.T) {
    var l1 []any = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_c(),tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_b(),tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_a(),tom_TestArrayNonLinear_empty_array_concArrayList(3))))
    var l2 []any = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_c(),tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_b(),tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_a(),tom_TestArrayNonLinear_empty_array_concArrayList(3))))
    var subject []any = tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_F(l2),tom_TestArrayNonLinear_cons_array_concArrayList(tom_TestArrayNonLinear_make_F(l1),tom_TestArrayNonLinear_empty_array_concArrayList(2))); { _ = subject; { if tom_TestArrayNonLinear_is_sort_ArrayList(subject) { if tom_TestArrayNonLinear_is_fun_sym_concArrayList(any(subject).( []any )) { var tomMatch1_2 int64 = 0; _ = tomMatch1_2; if !(tomMatch1_2 >= tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(any(subject).( []any ))) { var tomMatch1_6  any  = tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(any(subject).( []any ),tomMatch1_2); _ = tomMatch1_6; if tom_TestArrayNonLinear_is_fun_sym_F(any(tomMatch1_6).( any )) { var tomMatch1_5  []any  = tom_TestArrayNonLinear_get_slot_F_l(tomMatch1_6); _ = tomMatch1_5; if tom_TestArrayNonLinear_is_fun_sym_concArrayList(any(tomMatch1_5).( []any )) { var tomMatch1_end_15 int64 = 0; _ = tomMatch1_end_15; for { { var tomMatch1_3 int64 = tomMatch1_2 + 1; _ = tomMatch1_3; if !(tomMatch1_3 >= tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(any(subject).( []any ))) { var tomMatch1_9  any  = tom_TestArrayNonLinear_get_element_concArrayList_ArrayList(any(subject).( []any ),tomMatch1_3); _ = tomMatch1_9; if tom_TestArrayNonLinear_is_fun_sym_F(any(tomMatch1_9).( any )) { var tomMatch1_8  []any  = tom_TestArrayNonLinear_get_slot_F_l(tomMatch1_9); _ = tomMatch1_8; if tom_TestArrayNonLinear_is_fun_sym_concArrayList(any(tomMatch1_8).( []any )) { var tomMatch1_18 int64 = 0; _ = tomMatch1_18; if !(tomMatch1_18 >= tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(tomMatch1_8)) { if tom_TestArrayNonLinear_equal_term_ArrayList(tom_TestArrayNonLinear_get_slice_concArrayList(tomMatch1_5,tomMatch1_end_15,tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(tomMatch1_5)), tom_TestArrayNonLinear_get_slice_concArrayList(tomMatch1_8,tomMatch1_18 + 1,tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(tomMatch1_8))) { if tomMatch1_3 + 1 >= tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(any(subject).( []any )) { 


        return
      ;  };  };  };  };  };  }; tomMatch1_end_15 = tomMatch1_end_15 + 1;  };  if !(!(tomMatch1_end_15 > tom_TestArrayNonLinear_get_size_concArrayList_ArrayList(tomMatch1_5))) { break; }; };  };  };  };  };  };  };  }; 

    t.Fatal("failure")
  }



type F struct {
 l []any
}




func isF(t any) bool { _, ok := t.(*F); return ok }
