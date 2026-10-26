package gom











import (
  "testing"

  . "fixture/bool"
)

  func tom_TestBool_equal_term_Bool(t1  Bool , t2  Bool ) bool { return (t1).Equivalent(t2);  }; func tom_TestBool_is_sort_Bool(t any) bool { return func() bool { _, ok := (t).(Bool); return ok }();  }; func tom_TestBool_is_fun_sym_True(t  Bool ) bool { return func() bool { _, ok := (t).(*TrueBool); return ok }();  }; func tom_TestBool_make_True()  Bool  { return (MakeTrue()) }; func tom_TestBool_is_fun_sym_False(t  Bool ) bool { return func() bool { _, ok := (t).(*FalseBool); return ok }();  }; func tom_TestBool_make_False()  Bool  { return (MakeFalse()) }; func tom_TestBool_make_Not(b  Bool )  Bool  { return (MakeNot(b)) }; func tom_TestBool_is_fun_sym_And(t  Bool ) bool { return func() bool { _, ok := (t).(*AndBool); return ok }();  }; func tom_TestBool_make_And(l  Bool , r  Bool )  Bool  { return (MakeAnd(l,r)) }; func tom_TestBool_get_slot_And_l(t  Bool )  Bool  { return (t).(*AndBool).L;  }; func tom_TestBool_get_slot_And_r(t  Bool )  Bool  { return (t).(*AndBool).R;  }; func tom_TestBool_is_fun_sym_Or(t  Bool ) bool { return func() bool { _, ok := (t).(*OrBool); return ok }();  }; func tom_TestBool_make_Or(l  Bool , r  Bool )  Bool  { return (MakeOr(l,r)) }; func tom_TestBool_get_slot_Or_l(t  Bool )  Bool  { return (t).(*OrBool).L;  }; func tom_TestBool_get_slot_Or_r(t  Bool )  Bool  { return (t).(*OrBool).R;  };   func init() {   HookMakeNot = func(b Bool) (Bool, Bool) {     tom__r := func() Bool {     {    ; { { if tom_TestBool_is_sort_Bool(b) { if tom_TestBool_is_fun_sym_And(any(b).( Bool )) {  return tom_TestBool_make_Or(tom_TestBool_make_Not(tom_TestBool_get_slot_And_l(any(b).( Bool ))),tom_TestBool_make_Not(tom_TestBool_get_slot_And_r(any(b).( Bool )))); ;  };  };  }; { if tom_TestBool_is_sort_Bool(b) { if tom_TestBool_is_fun_sym_Or(any(b).( Bool )) {  return tom_TestBool_make_And(tom_TestBool_make_Not(tom_TestBool_get_slot_Or_l(any(b).( Bool ))),tom_TestBool_make_Not(tom_TestBool_get_slot_Or_r(any(b).( Bool )))); ;  };  };  }; { if tom_TestBool_is_sort_Bool(b) { if tom_TestBool_is_fun_sym_True(any(b).( Bool )) {  return tom_TestBool_make_False(); ;  };  };  }; { if tom_TestBool_is_sort_Bool(b) { if tom_TestBool_is_fun_sym_False(any(b).( Bool )) {  return tom_TestBool_make_True(); ;  };  };  };  };       };     return nil;     }();     return tom__r, b;   }; };  

func TestNotHook(t *testing.T) {
    var test Bool = tom_TestBool_make_Not(tom_TestBool_make_And(tom_TestBool_make_True(),tom_TestBool_make_True()))
    assertEquals(t, test,tom_TestBool_make_Or(tom_TestBool_make_False(),tom_TestBool_make_False()))
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
