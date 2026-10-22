package antipatterns












import (
  "testing"
)

  func tom_equal_term_Nat(t1  Nat , t2  Nat ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Nat(t any) bool { return func() bool { _, ok := (t).(Nat); return ok }();  }; func tom_is_fun_sym_zero(t  Nat ) bool { return func() bool { _, ok := (t).(*ZeroNat); return ok }();  }; func tom_make_zero()  Nat  { return (MakeZero()) }; func tom_is_fun_sym_suc(t  Nat ) bool { return func() bool { _, ok := (t).(*SucNat); return ok }();  }; func tom_make_suc(pred  Nat )  Nat  { return (MakeSuc(pred)) }; func tom_get_slot_suc_pred(t  Nat )  Nat  { return (t).(*SucNat).Pred;  }; func tom_is_fun_sym_plus(t  Nat ) bool { return func() bool { _, ok := (t).(*PlusNat); return ok }();  }; func tom_make_plus(x1  Nat , x2  Nat )  Nat  { return (MakePlus(x1,x2)) }; func tom_get_slot_plus_x1(t  Nat )  Nat  { return (t).(*PlusNat).X1;  }; func tom_get_slot_plus_x2(t  Nat )  Nat  { return (t).(*PlusNat).X2;  }; 







func evaluate(n Nat) map[int]bool {
    bag := map[int]bool{}
    ; { { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_6 bool =  false ; _ = tomMatch1_6; tomMatch1_6 =  true ; if !(tomMatch1_6) { var tomMatch1_5 bool =  false ; _ = tomMatch1_5; tomMatch1_5 =  true ; if !(tomMatch1_5) { 
 bag[1] = true  };  };  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_zero(any(n).( Nat )) { 
 bag[2] = true  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_18 bool =  false ; _ = tomMatch1_18; if tom_is_fun_sym_suc(any(tom_get_slot_plus_x2(any(n).( Nat ))).( Nat )) { tomMatch1_18 =  true ;  }; if !(tomMatch1_18) { 
 bag[3] = true  };  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_21  Nat  = tom_get_slot_plus_x2(any(n).( Nat )); _ = tomMatch1_21; var tomMatch1_28 bool =  false ; _ = tomMatch1_28; if tom_is_fun_sym_suc(any(tomMatch1_21).( Nat )) { if tom_equal_term_Nat(tom_get_slot_plus_x1(any(n).( Nat )), tom_get_slot_suc_pred(tomMatch1_21)) { tomMatch1_28 =  true ;  };  }; if !(tomMatch1_28) { 
 bag[4] = true  };  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { 
 bag[5] = true  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { if tom_equal_term_Nat(tom_get_slot_plus_x1(any(n).( Nat )), tom_get_slot_plus_x2(any(n).( Nat ))) { 
 bag[6] = true  };  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_46 bool =  false ; _ = tomMatch1_46; if tom_equal_term_Nat(tom_get_slot_plus_x1(any(n).( Nat )), tom_get_slot_plus_x2(any(n).( Nat ))) { tomMatch1_46 =  true ;  }; if !(tomMatch1_46) { 
 bag[7] = true  };  };  };  }; { if tom_is_sort_Nat(n) { var tomMatch1_55 bool =  false ; _ = tomMatch1_55; if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_56 bool =  false ; _ = tomMatch1_56; if tom_is_fun_sym_suc(any(tom_get_slot_plus_x2(any(n).( Nat ))).( Nat )) { tomMatch1_56 =  true ;  }; if !(tomMatch1_56) { tomMatch1_55 =  true ;  };  }; if !(tomMatch1_55) { 
 bag[8] = true  };  };  }; { if tom_is_sort_Nat(n) { var tomMatch1_68 bool =  false ; _ = tomMatch1_68; if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_59  Nat  = tom_get_slot_plus_x2(any(n).( Nat )); _ = tomMatch1_59; var tomMatch1_69 bool =  false ; _ = tomMatch1_69; if tom_is_fun_sym_suc(any(tomMatch1_59).( Nat )) { if tom_is_fun_sym_suc(any(tom_get_slot_suc_pred(tomMatch1_59)).( Nat )) { tomMatch1_69 =  true ;  };  }; if !(tomMatch1_69) { tomMatch1_68 =  true ;  };  }; if !(tomMatch1_68) { 
 bag[9] = true  };  };  }; { if tom_is_sort_Nat(n) { if tom_is_fun_sym_plus(any(n).( Nat )) { var tomMatch1_72  Nat  = tom_get_slot_plus_x2(any(n).( Nat )); _ = tomMatch1_72; if tom_is_fun_sym_suc(any(tomMatch1_72).( Nat )) { var tomMatch1_75  Nat  = tom_get_slot_suc_pred(tomMatch1_72); _ = tomMatch1_75; if tom_is_fun_sym_suc(any(tomMatch1_75).( Nat )) { var tomMatch1_78  Nat  = tom_get_slot_suc_pred(tomMatch1_75); _ = tomMatch1_78; if tom_is_fun_sym_plus(any(tomMatch1_78).( Nat )) { var tomMatch1_86 bool =  false ; _ = tomMatch1_86; if tom_equal_term_Nat(tom_get_slot_plus_x1(any(n).( Nat )), tom_get_slot_plus_x2(tomMatch1_78)) { tomMatch1_86 =  true ;  }; if !(tomMatch1_86) { 
 bag[10] = true  };  };  };  };  };  };  };  }; 

    return bag
}



func Test1(t *testing.T) {
    bag := evaluate(tom_make_zero())
    assertTrue(t, !bag[1])
    assertTrue(t, bag[2])
    assertTrue(t, !bag[3])
    assertTrue(t, !bag[4])
    assertTrue(t, !bag[5])
    assertTrue(t, !bag[6])
    assertTrue(t, !bag[7])
    assertTrue(t, bag[8])
    assertTrue(t, bag[9])
    assertTrue(t, !bag[10])
}



func Test2(t *testing.T) {
    bag := evaluate(tom_make_plus(tom_make_zero(),tom_make_zero()))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",bag[3])
    assertTrueMsg(t, "4",bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",bag[6])
    assertTrueMsg(t, "7",!bag[7])
    assertTrueMsg(t, "8",!bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}



func Test3(t *testing.T) {
    bag := evaluate(tom_make_plus(tom_make_zero(),tom_make_suc(tom_make_zero())))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",!bag[3])
    assertTrueMsg(t, "4",!bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}



func Test4(t *testing.T) {
    bag := evaluate(tom_make_plus(tom_make_suc(tom_make_zero()),tom_make_zero()))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",bag[3])
    assertTrueMsg(t, "4",bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",!bag[8])
    assertTrueMsg(t, "9",!bag[9])
    assertTrueMsg(t, "10",!bag[10])
}



func Test5(t *testing.T) {
    bag := evaluate(tom_make_plus(tom_make_suc(tom_make_zero()),tom_make_suc(tom_make_suc(tom_make_zero()))))
    assertTrueMsg(t, "1",!bag[1])
    assertTrueMsg(t, "2",!bag[2])
    assertTrueMsg(t, "3",!bag[3])
    assertTrueMsg(t, "4",!bag[4])
    assertTrueMsg(t, "5",bag[5])
    assertTrueMsg(t, "6",!bag[6])
    assertTrueMsg(t, "7",bag[7])
    assertTrueMsg(t, "8",bag[8])
    assertTrueMsg(t, "9",bag[9])
    assertTrueMsg(t, "10",!bag[10])
}




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}




func assertTrueMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}
