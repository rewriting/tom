package array_test



import (
	"testing"
)



type Term struct {
	Name string
	Args []*Term
}



func (t *Term) Equals(o *Term) bool {
	if t == o {
		return true
	}
	if t == nil || o == nil || t.Name != o.Name {
		return false
	}
	return true
}



func equalsList(a, b []*Term) bool {
	if len(a) != len(b) {
		return false
	}
	for i := range a {
		if !a[i].Equals(b[i]) {
			return false
		}
	}
	return true
}

func tom_equal_term_L(l1  []*Term , l2  []*Term ) bool { return equalsList(l1, l2);  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  []*Term ) bool { return t != nil;  }; func tom_empty_array_conc(n int64)  []*Term  { return (make([]*Term, 0, n)) }; func tom_cons_array_conc(e  *Term , l  []*Term )  []*Term  { return (append(l, e)) }; func tom_get_element_conc_L(l  []*Term , n int64)  *Term  { return (l)[n];  }; func tom_get_size_conc_L(l  []*Term ) int64 { return int64(len(l));  }; func tom_get_slice_conc(subject  []*Term , begin int64, end int64)  []*Term  { var result  []*Term  = tom_empty_array_conc(end-begin); for begin != end { result = tom_cons_array_conc(tom_get_element_conc_L(subject, begin), result); begin++ }; return result }; func tom_append_array_conc(l2  []*Term , l1  []*Term )  []*Term  { var size1 int64 = tom_get_size_conc_L(l1); var size2 int64 = tom_get_size_conc_L(l2); var index int64; var result  []*Term  = tom_empty_array_conc(size1+size2); index = size1; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l2, size2-index), result); index-- }; return result }; func tom_equal_term_E(t1  *Term , t2  *Term ) bool { return (t1).Equals(t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_is_fun_sym_a(t  *Term ) bool { return (t).Name == "a";  }; func tom_make_a()  *Term  { return (&Term{Name: "a"}) }; func tom_make_b()  *Term  { return (&Term{Name: "b"}) }; func tom_make_c()  *Term  { return (&Term{Name: "c"}) }; 






































func sort1(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch1_2 int64 = 0; _ = tomMatch1_2; var tomMatch1_end_5 int64 = tomMatch1_2; _ = tomMatch1_end_5; for { { if !(tomMatch1_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch1_end_5); _ = tom___x; var tomMatch1_6 int64 = tomMatch1_end_5 + 1; _ = tomMatch1_6; var tomMatch1_end_9 int64 = tomMatch1_6; _ = tomMatch1_end_9; for { { if !(tomMatch1_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___y  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch1_end_9); _ = tom___y; 

			if tom___x.Name > tom___y.Name { 
				return sort1(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_6,tomMatch1_end_9),tom_cons_array_conc(tom___y,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_2,tomMatch1_end_5),tom_empty_array_conc(5)))))))
			 };  }; tomMatch1_end_9 = tomMatch1_end_9 + 1;  };  if !(!(tomMatch1_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch1_end_5 = tomMatch1_end_5 + 1;  };  if !(!(tomMatch1_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 


	return l
}




func sort2(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch2_2 int64 = 0; _ = tomMatch2_2; var tomMatch2_end_5 int64 = tomMatch2_2; _ = tomMatch2_end_5; for { { if !(tomMatch2_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch2_end_5); _ = tom___x; var tomMatch2_6 int64 = tomMatch2_end_5 + 1; _ = tomMatch2_6; var tomMatch2_end_9 int64 = tomMatch2_6; _ = tomMatch2_end_9; for { { if !(tomMatch2_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___y  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch2_end_9); _ = tom___y; 

			if tom___x.Name > tom___y.Name { 
				return sort2(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_6,tomMatch2_end_9),tom_cons_array_conc(tom___y,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_2,tomMatch2_end_5),tom_empty_array_conc(5)))))))
			 };  }; tomMatch2_end_9 = tomMatch2_end_9 + 1;  };  if !(!(tomMatch2_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch2_end_5 = tomMatch2_end_5 + 1;  };  if !(!(tomMatch2_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 


	return l
}




func double1(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch3_2 int64 = 0; _ = tomMatch3_2; var tomMatch3_end_5 int64 = tomMatch3_2; _ = tomMatch3_end_5; for { { if !(tomMatch3_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch3_end_5); _ = tom___x; var tomMatch3_6 int64 = tomMatch3_end_5 + 1; _ = tomMatch3_6; var tomMatch3_end_9 int64 = tomMatch3_6; _ = tomMatch3_end_9; for { { if !(tomMatch3_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch3_end_9)) { 

			return double1(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_6,tomMatch3_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_2,tomMatch3_end_5),tom_empty_array_conc(4))))))
		 };  }; tomMatch3_end_9 = tomMatch3_end_9 + 1;  };  if !(!(tomMatch3_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch3_end_5 = tomMatch3_end_5 + 1;  };  if !(!(tomMatch3_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	return l
}




func double2(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch4_2 int64 = 0; _ = tomMatch4_2; var tomMatch4_end_5 int64 = tomMatch4_2; _ = tomMatch4_end_5; for { { if !(tomMatch4_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch4_end_5); _ = tom___x; var tomMatch4_6 int64 = tomMatch4_end_5 + 1; _ = tomMatch4_6; var tomMatch4_end_9 int64 = tomMatch4_6; _ = tomMatch4_end_9; for { { if !(tomMatch4_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch4_end_9)) { 

			return double2(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch4_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch4_6,tomMatch4_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch4_2,tomMatch4_end_5),tom_empty_array_conc(4))))))
		 };  }; tomMatch4_end_9 = tomMatch4_end_9 + 1;  };  if !(!(tomMatch4_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch4_end_5 = tomMatch4_end_5 + 1;  };  if !(!(tomMatch4_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	return l
}




func double4(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch5_2 int64 = 0; _ = tomMatch5_2; var tomMatch5_end_5 int64 = tomMatch5_2; _ = tomMatch5_end_5; for { { if !(tomMatch5_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch5_end_5); _ = tom___x; var tomMatch5_6 int64 = tomMatch5_end_5 + 1; _ = tomMatch5_6; var tomMatch5_end_9 int64 = tomMatch5_6; _ = tomMatch5_end_9; for { { if !(tomMatch5_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch5_end_9)) { 

			return double4(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch5_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch5_6,tomMatch5_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch5_2,tomMatch5_end_5),tom_empty_array_conc(4))))))
		 };  }; tomMatch5_end_9 = tomMatch5_end_9 + 1;  };  if !(!(tomMatch5_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch5_end_5 = tomMatch5_end_5 + 1;  };  if !(!(tomMatch5_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	return l
}




func double5(l []*Term) []*Term {
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch6_2 int64 = 0; _ = tomMatch6_2; var tomMatch6_end_5 int64 = tomMatch6_2; _ = tomMatch6_end_5; for { { if !(tomMatch6_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_is_fun_sym_a(any(tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_5)).( *Term )) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_5); _ = tom___x; var tomMatch6_6 int64 = tomMatch6_end_5 + 1; _ = tomMatch6_6; var tomMatch6_end_9 int64 = tomMatch6_6; _ = tomMatch6_end_9; for { { if !(tomMatch6_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_is_fun_sym_a(any(tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_9)).( *Term )) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_9)) { 
 return double5(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_6,tomMatch6_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_2,tomMatch6_end_5),tom_empty_array_conc(4))))))  };  };  }; tomMatch6_end_9 = tomMatch6_end_9 + 1;  };  if !(!(tomMatch6_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  }; tomMatch6_end_5 = tomMatch6_end_5 + 1;  };  if !(!(tomMatch6_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch6_19 int64 = 0; _ = tomMatch6_19; var tomMatch6_end_22 int64 = tomMatch6_19; _ = tomMatch6_end_22; for { { if !(tomMatch6_end_22 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_22); _ = tom___x; var tomMatch6_23 int64 = tomMatch6_end_22 + 1; _ = tomMatch6_23; var tomMatch6_end_26 int64 = tomMatch6_23; _ = tomMatch6_end_26; for { { if !(tomMatch6_end_26 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch6_end_26)) { 
 return double5(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_end_26 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_23,tomMatch6_end_26),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch6_19,tomMatch6_end_22),tom_empty_array_conc(4))))))  };  }; tomMatch6_end_26 = tomMatch6_end_26 + 1;  };  if !(!(tomMatch6_end_26 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch6_end_22 = tomMatch6_end_22 + 1;  };  if !(!(tomMatch6_end_22 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	return l
}










func makeFixtures() (unsorted, sorted, withDoubles, withoutDoubles []*Term) {
	ta := tom_make_a()
	tb := tom_make_b()
	tc := tom_make_c()
	unsorted = []*Term{ta, tb, tc, ta, tb, tc}
	sorted = []*Term{ta, ta, tb, tb, tc, tc}
	withDoubles = []*Term{ta, tb, tc, ta, tb, tc, ta}
	withoutDoubles = []*Term{ta, tb, tc}
	return
}



func TestSort1(t *testing.T) {
	u, s, _, _ := makeFixtures()
	if got := sort1(u); !equalsList(got, s) {
		t.Errorf("sort1 = %v, want %v", got, s)
	}
}



func TestSort2(t *testing.T) {
	u, s, _, _ := makeFixtures()
	if got := sort2(u); !equalsList(got, s) {
		t.Errorf("sort2 = %v, want %v", got, s)
	}
}



func TestDouble1(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double1(sort1(wd)); !equalsList(got, wod) {
		t.Errorf("double1 = %v, want %v", got, wod)
	}
}



func TestDouble2(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double2(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double2 = %v, want %v", got, wod)
	}
}



func TestDouble4(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double4(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double4 = %v, want %v", got, wod)
	}
}



func TestDouble5(t *testing.T) {
	_, _, wd, wod := makeFixtures()
	if got := double5(sort2(wd)); !equalsList(got, wod) {
		t.Errorf("double5 = %v, want %v", got, wod)
	}
}



func TestVariableStar1(t *testing.T) {
	nbSol := 0
	l := []*Term{tom_make_a(), tom_make_b()}
	; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch7_end_5 int64 = 0; _ = tomMatch7_end_5; for { { var tomMatch7_end_8 int64 = tomMatch7_end_5; _ = tomMatch7_end_8; for { { 

			nbSol++
		tomMatch7_end_8 = tomMatch7_end_8 + 1;  };  if !(!(tomMatch7_end_8 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; }; tomMatch7_end_5 = tomMatch7_end_5 + 1;  };  if !(!(tomMatch7_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	if nbSol != 6 {
		t.Errorf("VariableStar split count = %d, want 6", nbSol)
	}
}
