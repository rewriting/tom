package testnumericconditions



import "testing"

func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_TermList(t1  TermList , t2  TermList ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_TermList(t any) bool { return func() bool { _, ok := (t).(TermList); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(t1  Term , t2  int64 )  Term  { return (MakeF(t1,t2)) }; func tom_get_slot_f_t1(t  Term )  Term  { return (t).(*FTerm).T1;  }; func tom_get_slot_f_t2(t  Term )  int64  { return (t).(*FTerm).T2;  }; 




















func TestGreater(t *testing.T) {
	matched := false
	; { { var tomMatch1_0  Term  = tom_make_f(tom_make_a(),6); _ = tomMatch1_0; if tom_is_sort_Term(tomMatch1_0) { if tom_is_fun_sym_f(any(tomMatch1_0).( Term )) { if tom_get_slot_f_t2(any(tomMatch1_0).( Term )) > 5 { 
 matched = true  };  };  };  };  }; 

	if !matched {
		t.Errorf("f(a(),6) && y > 5 should fire")
	}
}




func TestGreaterOrEqual(t *testing.T) {
	matched := false
	; { { var tomMatch2_0  Term  = tom_make_f(tom_make_a(),6); _ = tomMatch2_0; if tom_is_sort_Term(tomMatch2_0) { if tom_is_fun_sym_f(any(tomMatch2_0).( Term )) { if tom_get_slot_f_t2(any(tomMatch2_0).( Term )) >= 6 { 
 matched = true  };  };  };  };  }; 

	if !matched {
		t.Errorf("f(a(),6) && y >= 6 should fire")
	}
}




func TestLess(t *testing.T) {
	matched := false
	; { { var tomMatch3_0  Term  = tom_make_f(tom_make_a(),3); _ = tomMatch3_0; if tom_is_sort_Term(tomMatch3_0) { if tom_is_fun_sym_f(any(tomMatch3_0).( Term )) { if tom_get_slot_f_t2(any(tomMatch3_0).( Term )) < 6 { 
 matched = true  };  };  };  };  }; 

	if !matched {
		t.Errorf("f(a(),3) && y < 6 should fire")
	}
}




func TestNotEqual(t *testing.T) {
	matched := false
	; { { var tomMatch4_0  Term  = tom_make_f(tom_make_a(),4); _ = tomMatch4_0; if tom_is_sort_Term(tomMatch4_0) { if tom_is_fun_sym_f(any(tomMatch4_0).( Term )) { if !(tom_equal_term_int(4, tom_get_slot_f_t2(any(tomMatch4_0).( Term )))) { 
 matched = true  };  };  };  };  }; 

	if matched {
		t.Errorf("f(a(),4) && y != 4 should NOT fire")
	}
}




func TestEqual(t *testing.T) {
	matched := false
	; { { var tomMatch5_0  Term  = tom_make_f(tom_make_a(),4); _ = tomMatch5_0; if tom_is_sort_Term(tomMatch5_0) { if tom_is_fun_sym_f(any(tomMatch5_0).( Term )) { if tom_equal_term_int(4, tom_get_slot_f_t2(any(tomMatch5_0).( Term ))) { 
 matched = true  };  };  };  };  }; 

	if !matched {
		t.Errorf("f(a(),4) && y == 4 should fire")
	}
}




func TestChained(t *testing.T) {
	matched := false
	; { { var tomMatch6_0  Term  = tom_make_f(tom_make_a(),4); _ = tomMatch6_0; if tom_is_sort_Term(tomMatch6_0) { if tom_is_fun_sym_f(any(tomMatch6_0).( Term )) { var tom___y  int64  = tom_get_slot_f_t2(any(tomMatch6_0).( Term )); _ = tom___y; if tom___y > 2 { if tom___y < 5 { 
 matched = true  };  };  };  };  };  }; 

	if !matched {
		t.Errorf("f(a(),4) && y > 2 && y < 5 should fire")
	}
}
