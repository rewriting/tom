package main



import "fmt"






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

func tom_equal_term_L(l1  []*Term , l2  []*Term ) bool { return equalsList(l1, l2);  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  []*Term ) bool { return t != nil;  }; func tom_empty_array_conc(n int64)  []*Term  { return (make([]*Term, 0, n)) }; func tom_cons_array_conc(e  *Term , l  []*Term )  []*Term  { return (append(l, e)) }; func tom_get_element_conc_L(l  []*Term , n int64)  *Term  { return (l)[n];  }; func tom_get_size_conc_L(l  []*Term ) int64 { return int64(len(l));  }; func tom_get_slice_conc(subject  []*Term , begin int64, end int64)  []*Term  { var result  []*Term  = tom_empty_array_conc(end-begin); for begin != end { result = tom_cons_array_conc(tom_get_element_conc_L(subject, begin), result); begin++ }; return result }; func tom_append_array_conc(l2  []*Term , l1  []*Term )  []*Term  { var size1 int64 = tom_get_size_conc_L(l1); var size2 int64 = tom_get_size_conc_L(l2); var index int64; var result  []*Term  = tom_empty_array_conc(size1+size2); index = size1; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l2, size2-index), result); index-- }; return result }; func tom_equal_term_E(t1  *Term , t2  *Term ) bool { return (t1).Equals(t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_make_a()  *Term  { return (&Term{Name: "a"}) }; func tom_make_b()  *Term  { return (&Term{Name: "b"}) }; func tom_make_c()  *Term  { return (&Term{Name: "c"}) }; 







































func sort1(l []*Term) []*Term {
	
{ { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch1_2 int64 = 0; _ = tomMatch1_2; var tomMatch1_end_5 int64 = tomMatch1_2; _ = tomMatch1_end_5; for { { if !(tomMatch1_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch1_end_5); _ = tom___x; var tomMatch1_6 int64 = tomMatch1_end_5 + 1; _ = tomMatch1_6; var tomMatch1_end_9 int64 = tomMatch1_6; _ = tomMatch1_end_9; for { { if !(tomMatch1_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___y  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch1_end_9); _ = tom___y; 

			if tom___x.Name > tom___y.Name { 
				return sort1(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_6,tomMatch1_end_9),tom_cons_array_conc(tom___y,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch1_2,tomMatch1_end_5),tom_empty_array_conc(5)))))))
			 };  }; tomMatch1_end_9 = tomMatch1_end_9 + 1;  };  if !(!(tomMatch1_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch1_end_5 = tomMatch1_end_5 + 1;  };  if !(!(tomMatch1_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 


	return l
}




func sort2(l []*Term) []*Term {
	
{ { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch2_2 int64 = 0; _ = tomMatch2_2; var tomMatch2_end_5 int64 = tomMatch2_2; _ = tomMatch2_end_5; for { { if !(tomMatch2_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch2_end_5); _ = tom___x; var tomMatch2_6 int64 = tomMatch2_end_5 + 1; _ = tomMatch2_6; var tomMatch2_end_9 int64 = tomMatch2_6; _ = tomMatch2_end_9; for { { if !(tomMatch2_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___y  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch2_end_9); _ = tom___y; 

			if tom___x.Name > tom___y.Name { 
				return sort2(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_6,tomMatch2_end_9),tom_cons_array_conc(tom___y,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch2_2,tomMatch2_end_5),tom_empty_array_conc(5)))))))
			 };  }; tomMatch2_end_9 = tomMatch2_end_9 + 1;  };  if !(!(tomMatch2_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch2_end_5 = tomMatch2_end_5 + 1;  };  if !(!(tomMatch2_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 


	return l
}










func double1(l []*Term) []*Term {
	
{ { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []*Term )) { var tomMatch3_2 int64 = 0; _ = tomMatch3_2; var tomMatch3_end_5 int64 = tomMatch3_2; _ = tomMatch3_end_5; for { { if !(tomMatch3_end_5 >= tom_get_size_conc_L(any(l).( []*Term ))) { var tom___x  *Term  = tom_get_element_conc_L(any(l).( []*Term ),tomMatch3_end_5); _ = tom___x; var tomMatch3_6 int64 = tomMatch3_end_5 + 1; _ = tomMatch3_6; var tomMatch3_end_9 int64 = tomMatch3_6; _ = tomMatch3_end_9; for { { if !(tomMatch3_end_9 >= tom_get_size_conc_L(any(l).( []*Term ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []*Term ),tomMatch3_end_9)) { 

			return double1(tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_end_9 + 1,tom_get_size_conc_L(any(l).( []*Term ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_6,tomMatch3_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []*Term ),tomMatch3_2,tomMatch3_end_5),tom_empty_array_conc(4))))))
		 };  }; tomMatch3_end_9 = tomMatch3_end_9 + 1;  };  if !(!(tomMatch3_end_9 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  }; tomMatch3_end_5 = tomMatch3_end_5 + 1;  };  if !(!(tomMatch3_end_5 > tom_get_size_conc_L(any(l).( []*Term )))) { break; }; };  };  };  };  }; 

	return l
}



func main() {
	ta := tom_make_a()
	tb := tom_make_b()
	tc := tom_make_c()

	l := []*Term{ta, tb, tc, ta, tb, tc}
	res := []*Term{ta, ta, tb, tb, tc, tc}

	if !equalsList(sort1(l), res) {
		fmt.Println("FAIL: sort1 didn't sort.")
		return
	}
	if !equalsList(sort2(l), res) {
		fmt.Println("FAIL: sort2 didn't sort.")
		return
	}
	
	
	
	if got := double1([]*Term{ta, tb, tc, ta, tb, tc}); !equalsList(got, []*Term{ta, tb, tc}) {
		fmt.Printf("FAIL: double1 = %v, want [a, b, c]\n", got)
		return
	}
	fmt.Println("Array test has successfully terminated.")
}


