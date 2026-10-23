package gom











import (
  "testing"

  . "fixture/rulebool"
)

  func tom_TestRuleBool_equal_term_Bool(t1  Bool , t2  Bool ) bool { return (t1).Equivalent(t2);  }; func tom_TestRuleBool_is_sort_Bool(t any) bool { return func() bool { _, ok := (t).(Bool); return ok }();  }; func tom_TestRuleBool_is_fun_sym_True(t  Bool ) bool { return func() bool { _, ok := (t).(*TrueBool); return ok }();  }; func tom_TestRuleBool_make_True()  Bool  { return (MakeTrue()) }; func tom_TestRuleBool_is_fun_sym_False(t  Bool ) bool { return func() bool { _, ok := (t).(*FalseBool); return ok }();  }; func tom_TestRuleBool_make_False()  Bool  { return (MakeFalse()) }; func tom_TestRuleBool_make_Not(b  Bool )  Bool  { return (MakeNot(b)) }; func tom_TestRuleBool_is_fun_sym_And(t  Bool ) bool { return func() bool { _, ok := (t).(*AndBool); return ok }();  }; func tom_TestRuleBool_make_And(l  Bool , r  Bool )  Bool  { return (MakeAnd(l,r)) }; func tom_TestRuleBool_get_slot_And_l(t  Bool )  Bool  { return (t).(*AndBool).L;  }; func tom_TestRuleBool_get_slot_And_r(t  Bool )  Bool  { return (t).(*AndBool).R;  }; func tom_TestRuleBool_is_fun_sym_Or(t  Bool ) bool { return func() bool { _, ok := (t).(*OrBool); return ok }();  }; func tom_TestRuleBool_make_Or(l  Bool , r  Bool )  Bool  { return (MakeOr(l,r)) }; func tom_TestRuleBool_get_slot_Or_l(t  Bool )  Bool  { return (t).(*OrBool).L;  }; func tom_TestRuleBool_get_slot_Or_r(t  Bool )  Bool  { return (t).(*OrBool).R;  };   func init() {   HookMakeNot = func(arg_1 Bool) (Bool, Bool) {     tom__r := func() Bool {     {     ; { _ = arg_1; { if tom_TestRuleBool_is_sort_Bool(arg_1) { if tom_TestRuleBool_is_fun_sym_And(any(arg_1).( Bool )) {  return tom_TestRuleBool_make_Or(tom_TestRuleBool_make_Not(tom_TestRuleBool_get_slot_And_l(any(arg_1).( Bool ))),tom_TestRuleBool_make_Not(tom_TestRuleBool_get_slot_And_r(any(arg_1).( Bool )))); ;  };  };  }; { if tom_TestRuleBool_is_sort_Bool(arg_1) { if tom_TestRuleBool_is_fun_sym_Or(any(arg_1).( Bool )) {  return tom_TestRuleBool_make_And(tom_TestRuleBool_make_Not(tom_TestRuleBool_get_slot_Or_l(any(arg_1).( Bool ))),tom_TestRuleBool_make_Not(tom_TestRuleBool_get_slot_Or_r(any(arg_1).( Bool )))); ;  };  };  }; { if tom_TestRuleBool_is_sort_Bool(arg_1) { if tom_TestRuleBool_is_fun_sym_True(any(arg_1).( Bool )) {  return tom_TestRuleBool_make_False(); ;  };  };  }; { if tom_TestRuleBool_is_sort_Bool(arg_1) { if tom_TestRuleBool_is_fun_sym_False(any(arg_1).( Bool )) {  return tom_TestRuleBool_make_True(); ;  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };  

func TestNot(t *testing.T) {
    var test Bool = tom_TestRuleBool_make_Not(tom_TestRuleBool_make_And(tom_TestRuleBool_make_True(),tom_TestRuleBool_make_True()))
    assertEquals(t, test,tom_TestRuleBool_make_Or(tom_TestRuleBool_make_False(),tom_TestRuleBool_make_False()))
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
