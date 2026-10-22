package gom










import (
  "testing"

  . "fixture/dotted"
)

  func tom_equal_term_Nop(t1  Nop , t2  Nop ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Nop(t any) bool { return func() bool { _, ok := (t).(Nop); return ok }();  }; func tom_is_fun_sym_EmptyNop(t  Nop ) bool { return func() bool { _, ok := (t).(*EmptyNopNop); return ok }();  }; func tom_make_EmptyNop()  Nop  { return (MakeEmptyNop()) }; func tom_is_fun_sym_UnaryNop(t  Nop ) bool { return func() bool { _, ok := (t).(*UnaryNopNop); return ok }();  }; func tom_make_UnaryNop(slot  Nop )  Nop  { return (MakeUnaryNop(slot)) }; func tom_get_slot_UnaryNop_slot(t  Nop )  Nop  { return (t).(*UnaryNopNop).Slot;  }; func tom_is_fun_sym_BinaryNop(t  Nop ) bool { return func() bool { _, ok := (t).(*BinaryNopNop); return ok }();  }; func tom_make_BinaryNop(ls  Nop , rs  Nop )  Nop  { return (MakeBinaryNop(ls,rs)) }; func tom_get_slot_BinaryNop_ls(t  Nop )  Nop  { return (t).(*BinaryNopNop).Ls;  }; func tom_get_slot_BinaryNop_rs(t  Nop )  Nop  { return (t).(*BinaryNopNop).Rs;  }; 

func Test1(t *testing.T) {
    var n Nop = tom_make_BinaryNop(tom_make_UnaryNop(tom_make_EmptyNop()),tom_make_UnaryNop(tom_make_EmptyNop())); { { if tom_is_sort_Nop(n) { if tom_is_fun_sym_BinaryNop(any(n).( Nop )) { var tomMatch1_1  Nop  = tom_get_slot_BinaryNop_ls(any(n).( Nop )); _ = tomMatch1_1; var tomMatch1_2  Nop  = tom_get_slot_BinaryNop_rs(any(n).( Nop )); _ = tomMatch1_2; if tom_is_fun_sym_UnaryNop(any(tomMatch1_1).( Nop )) { if tom_is_fun_sym_EmptyNop(any(tom_get_slot_UnaryNop_slot(tomMatch1_1)).( Nop )) { if tom_is_fun_sym_UnaryNop(any(tomMatch1_2).( Nop )) { if tom_is_fun_sym_EmptyNop(any(tom_get_slot_UnaryNop_slot(tomMatch1_2)).( Nop )) { 


        return
       };  };  };  };  };  };  };  }; 

    fail(t, "Should have matched")
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
