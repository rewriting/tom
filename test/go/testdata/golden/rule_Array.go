package main








import (
  "strings"

  "tom/library/aterm"
)



var factory = aterm.GetInstance()



var fzero, fsuc, fplus, ffib *aterm.AFun
var tzero *aterm.ATermAppl

func tom_equal_term_L(l1  []aterm.ATerm , l2  []aterm.ATerm ) bool { return listEquals(l1, l2);  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  []aterm.ATerm ) bool { return t != nil;  }; func tom_empty_array_conc(n int64)  []aterm.ATerm  { return (make([]aterm.ATerm, 0, n)) }; func tom_cons_array_conc(e  aterm.ATerm , l  []aterm.ATerm )  []aterm.ATerm  { return (myAdd(e,l)) }; func tom_get_element_conc_L(l  []aterm.ATerm , n int64)  aterm.ATerm  { return l[n];  }; func tom_get_size_conc_L(l  []aterm.ATerm ) int64 { return int64(len(l));  }; func tom_get_slice_conc(subject  []aterm.ATerm , begin int64, end int64)  []aterm.ATerm  { var result  []aterm.ATerm  = tom_empty_array_conc(end-begin); for begin != end { result = tom_cons_array_conc(tom_get_element_conc_L(subject, begin), result); begin++ }; return result }; func tom_append_array_conc(l2  []aterm.ATerm , l1  []aterm.ATerm )  []aterm.ATerm  { var size1 int64 = tom_get_size_conc_L(l1); var size2 int64 = tom_get_size_conc_L(l2); var index int64; var result  []aterm.ATerm  = tom_empty_array_conc(size1+size2); index = size1; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l1, size1-index), result); index-- }; index = size2; for index > 0 { result = tom_cons_array_conc(tom_get_element_conc_L(l2, size2-index), result); index-- }; return result }; 















func myAdd(e aterm.ATerm, l []aterm.ATerm) []aterm.ATerm {
  return append(l, e)
}




func listEquals(l1, l2 []aterm.ATerm) bool {
  if len(l1) != len(l2) {
    return false
  }
  for i := range l1 {
    if l1[i] != l2[i] {
      return false
    }
  }
  return true
}

func tom_equal_term_E(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return (t1 == t2);  }; func tom_is_sort_E(t any) bool { return false;  }; func tom_is_fun_sym_a(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "a";  }; func tom_make_a()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("a", 0, false))) }; func tom_make_b()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("b", 0, false))) }; func tom_make_c()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("c", 0, false))) }; 
























func main() {
  testArray1()
}



func testArray1() {
  var ta aterm.ATerm = factory.MakeAppl(factory.MakeAFun("a", 0, false))
  var tb aterm.ATerm = factory.MakeAppl(factory.MakeAFun("b", 0, false))
  var tc aterm.ATerm = factory.MakeAppl(factory.MakeAFun("c", 0, false))
  l := []aterm.ATerm{}
  l = append(l, ta)
  l = append(l, tb)
  l = append(l, tc)
  l = append(l, ta)
  l = append(l, tb)
  l = append(l, tc)

  res := []aterm.ATerm{}
  res = append(res, ta)
  res = append(res, ta)
  res = append(res, tb)
  res = append(res, tb)
  res = append(res, tc)
  res = append(res, tc)

  assertTrue(listEquals(sort1(l), res))
  assertTrue(listEquals(sort2(l), res))
}



func testArray2() {
  var l   []aterm.ATerm = tom_cons_array_conc(tom_make_a(),tom_cons_array_conc(tom_make_c(),tom_cons_array_conc(tom_make_b(),tom_cons_array_conc(tom_make_a(),tom_cons_array_conc(tom_make_c(),tom_cons_array_conc(tom_make_b(),tom_cons_array_conc(tom_make_a(),tom_empty_array_conc(7))))))))
  var res []aterm.ATerm = tom_cons_array_conc(tom_make_c(),tom_cons_array_conc(tom_make_b(),tom_cons_array_conc(tom_make_a(),tom_empty_array_conc(3))))

  assertTrue(listEquals(double1(sort1(l)), res))
  assertTrue(listEquals(double2(sort2(l)), res))
  
  assertTrue(listEquals(double4(sort2(l)), res))
  assertTrue(listEquals(double5(sort2(l)), res))
}



func sort1(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch1_2 int64 = 0; _ = tomMatch1_2; var tomMatch1_end_5 int64 = tomMatch1_2; _ = tomMatch1_end_5; for { { if !(tomMatch1_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch1_end_5); _ = tom___x; var tomMatch1_6 int64 = tomMatch1_end_5 + 1; _ = tomMatch1_6; var tomMatch1_end_9 int64 = tomMatch1_6; _ = tomMatch1_end_9; for { { if !(tomMatch1_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___y  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch1_end_9); _ = tom___y; 

      xname := any(tom___x).(*aterm.ATermAppl).GetName()
      yname := any(tom___y).(*aterm.ATermAppl).GetName()
      if strings.Compare(xname, yname) > 0 { 
        result := tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch1_2,tomMatch1_end_5)
        result = append(result, tom___y)
        result = append(result, (tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch1_6,tomMatch1_end_9))...)
        result = append(result, tom___x)
        result = append(result, (tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch1_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))))...)
        return sort1(result)
       };  }; tomMatch1_end_9 = tomMatch1_end_9 + 1;  };  if !(!(tomMatch1_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch1_end_5 = tomMatch1_end_5 + 1;  };  if !(!(tomMatch1_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 


	return l
}



func double1(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch2_2 int64 = 0; _ = tomMatch2_2; var tomMatch2_end_5 int64 = tomMatch2_2; _ = tomMatch2_end_5; for { { if !(tomMatch2_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch2_end_5); _ = tom___x; var tomMatch2_6 int64 = tomMatch2_end_5 + 1; _ = tomMatch2_6; var tomMatch2_end_9 int64 = tomMatch2_6; _ = tomMatch2_end_9; for { { if !(tomMatch2_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch2_end_9)) { 

      result := tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch2_2,tomMatch2_end_5)
      result = append(result, (tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch2_6,tomMatch2_end_9))...)
      result = append(result, tom___x)
      result = append(result, (tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch2_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))))...)
      return double1(result)
     };  }; tomMatch2_end_9 = tomMatch2_end_9 + 1;  };  if !(!(tomMatch2_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch2_end_5 = tomMatch2_end_5 + 1;  };  if !(!(tomMatch2_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 

	return l
}



func sort2(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch3_2 int64 = 0; _ = tomMatch3_2; var tomMatch3_end_5 int64 = tomMatch3_2; _ = tomMatch3_end_5; for { { if !(tomMatch3_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch3_end_5); _ = tom___x; var tomMatch3_6 int64 = tomMatch3_end_5 + 1; _ = tomMatch3_6; var tomMatch3_end_9 int64 = tomMatch3_6; _ = tomMatch3_end_9; for { { if !(tomMatch3_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___y  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch3_end_9); _ = tom___y; 

      xname := any(tom___x).(*aterm.ATermAppl).GetName()
      yname := any(tom___y).(*aterm.ATermAppl).GetName()
      if strings.Compare(xname, yname) > 0 { 
        return sort2(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch3_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch3_6,tomMatch3_end_9),tom_cons_array_conc(tom___y,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch3_2,tomMatch3_end_5),tom_empty_array_conc(5))))))) };  }; tomMatch3_end_9 = tomMatch3_end_9 + 1;  };  if !(!(tomMatch3_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch3_end_5 = tomMatch3_end_5 + 1;  };  if !(!(tomMatch3_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 



	return l
}



func double2(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch4_2 int64 = 0; _ = tomMatch4_2; var tomMatch4_end_5 int64 = tomMatch4_2; _ = tomMatch4_end_5; for { { if !(tomMatch4_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch4_end_5); _ = tom___x; var tomMatch4_6 int64 = tomMatch4_end_5 + 1; _ = tomMatch4_6; var tomMatch4_end_9 int64 = tomMatch4_6; _ = tomMatch4_end_9; for { { if !(tomMatch4_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch4_end_9)) { 

      return double2(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch4_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch4_6,tomMatch4_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch4_2,tomMatch4_end_5),tom_empty_array_conc(4)))))) };  }; tomMatch4_end_9 = tomMatch4_end_9 + 1;  };  if !(!(tomMatch4_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch4_end_5 = tomMatch4_end_5 + 1;  };  if !(!(tomMatch4_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 


	return l
}





func double4(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch5_2 int64 = 0; _ = tomMatch5_2; var tomMatch5_end_5 int64 = tomMatch5_2; _ = tomMatch5_end_5; for { { if !(tomMatch5_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch5_end_5); _ = tom___x; var tomMatch5_6 int64 = tomMatch5_end_5 + 1; _ = tomMatch5_6; var tomMatch5_end_9 int64 = tomMatch5_6; _ = tomMatch5_end_9; for { { if !(tomMatch5_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch5_end_9)) { 
 return double4(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch5_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch5_6,tomMatch5_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch5_2,tomMatch5_end_5),tom_empty_array_conc(4)))))) };  }; tomMatch5_end_9 = tomMatch5_end_9 + 1;  };  if !(!(tomMatch5_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch5_end_5 = tomMatch5_end_5 + 1;  };  if !(!(tomMatch5_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 

	return l
}



func double5(l []aterm.ATerm) []aterm.ATerm {
  ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch6_2 int64 = 0; _ = tomMatch6_2; var tomMatch6_end_5 int64 = tomMatch6_2; _ = tomMatch6_end_5; for { { if !(tomMatch6_end_5 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_is_fun_sym_a(any(tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_5)).( aterm.ATerm )) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_5); _ = tom___x; var tomMatch6_6 int64 = tomMatch6_end_5 + 1; _ = tomMatch6_6; var tomMatch6_end_9 int64 = tomMatch6_6; _ = tomMatch6_end_9; for { { if !(tomMatch6_end_9 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_is_fun_sym_a(any(tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_9)).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_9)) { 
 return double5(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_end_9 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_6,tomMatch6_end_9),tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_2,tomMatch6_end_5),tom_empty_array_conc(4)))))) };  };  }; tomMatch6_end_9 = tomMatch6_end_9 + 1;  };  if !(!(tomMatch6_end_9 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  }; tomMatch6_end_5 = tomMatch6_end_5 + 1;  };  if !(!(tomMatch6_end_5 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch6_19 int64 = 0; _ = tomMatch6_19; var tomMatch6_end_22 int64 = tomMatch6_19; _ = tomMatch6_end_22; for { { if !(tomMatch6_end_22 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_22); _ = tom___x; var tomMatch6_23 int64 = tomMatch6_end_22 + 1; _ = tomMatch6_23; var tomMatch6_end_26 int64 = tomMatch6_23; _ = tomMatch6_end_26; for { { if !(tomMatch6_end_26 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_26)) { 
 return double5(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_end_26 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_23,tomMatch6_end_26),tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_19,tomMatch6_end_22),tom_empty_array_conc(4)))))) };  }; tomMatch6_end_26 = tomMatch6_end_26 + 1;  };  if !(!(tomMatch6_end_26 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch6_end_22 = tomMatch6_end_22 + 1;  };  if !(!(tomMatch6_end_22 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( []aterm.ATerm )) { var tomMatch6_32 int64 = 0; _ = tomMatch6_32; var tomMatch6_end_35 int64 = tomMatch6_32; _ = tomMatch6_end_35; for { { if !(tomMatch6_end_35 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { var tom___x  aterm.ATerm  = tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_35); _ = tom___x; var tomMatch6_36 int64 = tomMatch6_end_35 + 1; _ = tomMatch6_36; var tomMatch6_end_39 int64 = tomMatch6_36; _ = tomMatch6_end_39; for { { if !(tomMatch6_end_39 >= tom_get_size_conc_L(any(l).( []aterm.ATerm ))) { if tom_equal_term_E(tom___x, tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_39)) { if tom_equal_term_E(tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_35), tom_get_element_conc_L(any(l).( []aterm.ATerm ),tomMatch6_end_39)) { 
 return double5(tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_end_39 + 1,tom_get_size_conc_L(any(l).( []aterm.ATerm ))),tom_cons_array_conc(tom___x,tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_36,tomMatch6_end_39),tom_append_array_conc(tom_get_slice_conc(any(l).( []aterm.ATerm ),tomMatch6_32,tomMatch6_end_35),tom_empty_array_conc(4)))))) };  };  }; tomMatch6_end_39 = tomMatch6_end_39 + 1;  };  if !(!(tomMatch6_end_39 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  }; tomMatch6_end_35 = tomMatch6_end_35 + 1;  };  if !(!(tomMatch6_end_35 > tom_get_size_conc_L(any(l).( []aterm.ATerm )))) { break; }; };  };  };  };  }; 

	return l
}



func assertTrue(condition bool) {
  if !condition {
    panic("assertion failed.")
  }
}
