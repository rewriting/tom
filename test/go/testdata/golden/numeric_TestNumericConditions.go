package numeric







import "testing"

  func tom_TestNumericConditions_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_TestNumericConditions_is_sort_int(t int64) bool { return true;  }; func tom_TestNumericConditions_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestNumericConditions_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestNumericConditions_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_TestNumericConditions_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_TestNumericConditions_make_a()  Term  { return (MakeA()) }; func tom_TestNumericConditions_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_TestNumericConditions_make_f(t1  Term , t2  int64 )  Term  { return (MakeF(t1,t2)) }; func tom_TestNumericConditions_get_slot_f_t1(t  Term )  Term  { return (t).(*FTerm).T1;  }; func tom_TestNumericConditions_get_slot_f_t2(t  Term )  int64  { return (t).(*FTerm).T2;  }; 















func Test1(t *testing.T) {
    ; { { var tomMatch1_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch1_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch1_0).( Term )) { if tom_TestNumericConditions_get_slot_f_t2(any(tomMatch1_0).( Term )) > 5 { 
        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func Test2(t *testing.T) {
    ; { { var tomMatch2_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch2_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch2_0).( Term )) { if tom_TestNumericConditions_get_slot_f_t2(any(tomMatch2_0).( Term )) >= 6 { 
        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func Test3(t *testing.T) {
    ; { { var tomMatch3_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),3); if tom_TestNumericConditions_is_sort_Term(tomMatch3_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch3_0).( Term )) { if tom_TestNumericConditions_get_slot_f_t2(any(tomMatch3_0).( Term )) < 6 { 
        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func Test4(t *testing.T) {
    ; { { var tomMatch4_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),4); if tom_TestNumericConditions_is_sort_Term(tomMatch4_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch4_0).( Term )) { if tom_TestNumericConditions_get_slot_f_t2(any(tomMatch4_0).( Term )) <= 3 { 
        
        t.FailNow()
      ;  };  };  };  };  }; 

  return

}



func Test5(t *testing.T) {
    ; { { var tomMatch5_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),4); if tom_TestNumericConditions_is_sort_Term(tomMatch5_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch5_0).( Term )) { if !(tom_TestNumericConditions_equal_term_int(4, tom_TestNumericConditions_get_slot_f_t2(any(tomMatch5_0).( Term )))) { 
        
        t.FailNow()
      ;  };  };  };  };  }; 

  return

}



func Test6(t *testing.T) {
    ; { { var tomMatch6_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),4); if tom_TestNumericConditions_is_sort_Term(tomMatch6_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch6_0).( Term )) { if tom_TestNumericConditions_equal_term_int(4, tom_TestNumericConditions_get_slot_f_t2(any(tomMatch6_0).( Term ))) { 
        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func Test7(t *testing.T) {
    ; { { var tomMatch7_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),4); if tom_TestNumericConditions_is_sort_Term(tomMatch7_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch7_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch7_0).( Term )); if tom___y > 2 { if tom___y < 5 { 
        
        return
      ;  };  };  };  };  };  }; 

  t.FailNow()
}



func Test8(t *testing.T) {
    ; { { var tomMatch8_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch8_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch8_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch8_0).( Term )); if tom___y > 2 { if tom___y < 5 { 
        
        t.FailNow()
      ;  };  };  };  };  };  }; 

  return

}



func Test9(t *testing.T) {
    ; { { var tomMatch9_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch9_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch9_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch9_0).( Term )); if tom___y > 2 { 
        
        return
      ;  }; if tom___y < 5 {                  return;       ;  };  };  };  };  }; 

  t.FailNow()
}



func Test10(t *testing.T) {
    ; { { var tomMatch10_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch10_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch10_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch10_0).( Term )); if !(tom_TestNumericConditions_equal_term_int(6, tom___y)) { 
        
        t.FailNow()
      ;  }; if tom___y < 5 {                  t.FailNow();       ;  };  };  };  };  }; 

  return

}



func Test11(t *testing.T) {
    ; { { var tomMatch11_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch11_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch11_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch11_0).( Term )); if tom_TestNumericConditions_equal_term_int(6, tom___y) { 
        
        return
      ;  }; if tom___y < 5 {                  return;       ;  };  };  };  };  }; 

  t.FailNow()
}



func Test12(t *testing.T) {
    ; { { var tomMatch12_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch12_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch12_0).( Term )) { var tom___y  int64  = tom_TestNumericConditions_get_slot_f_t2(any(tomMatch12_0).( Term )); if !(tom_TestNumericConditions_equal_term_int(6, tom___y)) { 
        
        return
      ;  }; if tom___y >= 5 {                  return;       ;  };  };  };  };  }; 

  t.FailNow()
}



func Test13(t *testing.T) {
    ; { { var tomMatch13_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch13_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch13_0).( Term )) { if tom_TestNumericConditions_get_slot_f_t2(any(tomMatch13_0).( Term )) > getIntegerValue() { 


        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func Test14(t *testing.T) {
    ; { { var tomMatch14_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch14_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch14_0).( Term )) { if tom_TestNumericConditions_equal_term_int(getIntegerValue(), tom_TestNumericConditions_get_slot_f_t2(any(tomMatch14_0).( Term ))) { 


        
        t.FailNow()
      ;  };  };  };  };  }; 

  return

}
  

func Test15(t *testing.T) {
  var m int64 = 6 
    ; { { var tomMatch15_0  Term  = tom_TestNumericConditions_make_f(tom_TestNumericConditions_make_a(),6); if tom_TestNumericConditions_is_sort_Term(tomMatch15_0) { if tom_TestNumericConditions_is_fun_sym_f(any(tomMatch15_0).( Term )) { if tom_TestNumericConditions_equal_term_int(m, tom_TestNumericConditions_get_slot_f_t2(any(tomMatch15_0).( Term ))) { 


        
        return
      ;  };  };  };  };  }; 

  t.FailNow()
}



func getIntegerValue() int64 {
  return 5
}
