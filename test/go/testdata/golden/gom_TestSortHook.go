package gom
















import (
  "testing"
)

  func tom_TestSortHook_equal_term_Sort1(t1  Sort1 , t2  Sort1 ) bool { return (t1).Equivalent(t2);  }; func tom_TestSortHook_is_sort_Sort1(t any) bool { return func() bool { _, ok := (t).(Sort1); return ok }();  }; func tom_TestSortHook_equal_term_Sort2(t1  Sort2 , t2  Sort2 ) bool { return (t1).Equivalent(t2);  }; func tom_TestSortHook_is_sort_Sort2(t any) bool { return func() bool { _, ok := (t).(Sort2); return ok }();  }; func tom_TestSortHook_make_a()  Sort1  { return (MakeA()) }; func tom_TestSortHook_make_b()  Sort1  { return (MakeB()) }; func tom_TestSortHook_is_fun_sym_c(t  Sort2 ) bool { return func() bool { _, ok := (t).(*CSort2); return ok }();  }; func tom_TestSortHook_make_c()  Sort2  { return (MakeC()) }; func tom_TestSortHook_is_fun_sym_d(t  Sort2 ) bool { return func() bool { _, ok := (t).(*DSort2); return ok }();  }; func tom_TestSortHook_make_d()  Sort2  { return (MakeD()) };          func hookSort1(this Sort1) string {         return "1"+this.SymbolName();       };               func getEmptyMap(this Sort1) map[any]any {         return map[any]any{};       }       ;         func hookSort2(this Sort2) string {         ; { { if tom_TestSortHook_is_sort_Sort2(this) { if tom_TestSortHook_is_fun_sym_c(any(this).( Sort2 )) {  return "2c"; ;  };  };  }; { if tom_TestSortHook_is_sort_Sort2(this) { if tom_TestSortHook_is_fun_sym_d(any(this).( Sort2 )) {  return "2d"; ;  };  };  };  }; ;         return "plonk";       }       ;  






























func TestHook1a(t *testing.T) {
    var my Sort1 = tom_TestSortHook_make_a()
    var res string = hookSort1(my)
    assertEquals(t, "1a",res)
}



func TestHook1b(t *testing.T) {
    var my Sort1 = tom_TestSortHook_make_b()
    var res string = hookSort1(my)
    assertEquals(t, "1b",res)
}



func TestHook2c(t *testing.T) {
    var my Sort2 = tom_TestSortHook_make_c()
    var res string = hookSort2(my)
    assertEquals(t, "2c",res)
}



func TestHook2d(t *testing.T) {
    var my Sort2 = tom_TestSortHook_make_d()
    var res string = hookSort2(my)
    assertEquals(t, "2d",res)
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
