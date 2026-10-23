








package main



import (
  "fmt"
  "strings"

  "tom/library/aterm"
)



var factory *aterm.PureFactory

  func tom_equal_term_E(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return (t1 == t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_equal_term_L(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  *aterm.ATermList ) bool { return t != nil;  }; func tom_empty_list_conc()  *aterm.ATermList  { return (factory.MakeListEmpty()) }; func tom_cons_list_conc(e  aterm.ATerm , l  *aterm.ATermList )  *aterm.ATermList  { return (any(l).(*aterm.ATermList).Insert(e)) }; func tom_get_head_conc_L(l  *aterm.ATermList )  aterm.ATerm  { return any(l).(*aterm.ATermList).GetFirst();  }; func tom_get_tail_conc_L(l  *aterm.ATermList )  *aterm.ATermList  { return any(l).(*aterm.ATermList).GetNext();  }; func tom_is_empty_conc_L(l  *aterm.ATermList ) bool { return any(l).(*aterm.ATermList).IsEmpty();  }; func tom_append_list_conc(l1  *aterm.ATermList , l2  *aterm.ATermList )  *aterm.ATermList  { if tom_is_empty_conc_L(l1) { return l2 } else if tom_is_empty_conc_L(l2) { return l1 } else if tom_is_empty_conc_L(tom_get_tail_conc_L(l1)) { return tom_cons_list_conc(tom_get_head_conc_L(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_L(l1), tom_append_list_conc(tom_get_tail_conc_L(l1), l2)) }; }; func tom_get_slice_conc(begin  *aterm.ATermList , end  *aterm.ATermList , tail  *aterm.ATermList )  *aterm.ATermList  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_conc_L(end) || tom_equal_term_L(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_L(begin), tom_get_slice_conc(tom_get_tail_conc_L(begin), end, tail)) }; func tom_is_fun_sym_a(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "a";  }; func tom_make_a()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("a", 0, false))) }; func tom_make_b()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("b", 0, false))) }; func tom_make_c()  aterm.ATerm  { return (factory.MakeAppl(factory.MakeAFun("c", 0, false))) }; func tom_make_id(l  *aterm.ATermList )  *aterm.ATermList  { return (l) }; 








































type TomList struct{}



func newTomList(f *aterm.PureFactory) *TomList {
    factory = f
    return &TomList{}
  }



func main() {
    newTomList(aterm.NewPureFactory(16))
    testList1()
    testList2()
    fmt.Println("TomList test has successfully terminated.")
  }



func sort1(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch1_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch1_end_4; for { { if !(tom_is_empty_conc_L(tomMatch1_end_4)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch1_end_4); _ = tom___x; var tomMatch1_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch1_end_4); _ = tomMatch1_5; var tomMatch1_end_8  *aterm.ATermList  = tomMatch1_5; _ = tomMatch1_end_8; for { { if !(tom_is_empty_conc_L(tomMatch1_end_8)) { var tom___y  aterm.ATerm  = tom_get_head_conc_L(tomMatch1_end_8); _ = tom___y; 

        xname := (tom___x).(*aterm.ATermAppl).GetName()
        yname := (tom___y).(*aterm.ATermAppl).GetName()
        
        if strings.Compare(xname, yname) > 0 { 
          return sort1(
            tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch1_end_4,tom_empty_list_conc()).
            Append(tom___y).
            Concat(tom_get_slice_conc(tomMatch1_5,tomMatch1_end_8,tom_empty_list_conc())).
            Append(tom___x).
            Concat(tom_get_tail_conc_L(tomMatch1_end_8)),
            )
         };  }; if tom_is_empty_conc_L(tomMatch1_end_8) { tomMatch1_end_8 = tomMatch1_5;  } else { tomMatch1_end_8 = tom_get_tail_conc_L(tomMatch1_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_8, tomMatch1_5))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch1_end_4) { tomMatch1_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch1_end_4 = tom_get_tail_conc_L(tomMatch1_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch1_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 


		return l 
  }



func double1(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch2_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch2_end_4; for { { if !(tom_is_empty_conc_L(tomMatch2_end_4)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch2_end_4); _ = tom___x; var tomMatch2_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch2_end_4); _ = tomMatch2_5; var tomMatch2_end_8  *aterm.ATermList  = tomMatch2_5; _ = tomMatch2_end_8; for { { if !(tom_is_empty_conc_L(tomMatch2_end_8)) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch2_end_8)) { 

        return double1(
          tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch2_end_4,tom_empty_list_conc()).
          Concat(tom_get_slice_conc(tomMatch2_5,tomMatch2_end_8,tom_empty_list_conc())).
          Append(tom___x).
          Concat(tom_get_tail_conc_L(tomMatch2_end_8)),
          )
       };  }; if tom_is_empty_conc_L(tomMatch2_end_8) { tomMatch2_end_8 = tomMatch2_5;  } else { tomMatch2_end_8 = tom_get_tail_conc_L(tomMatch2_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch2_end_8, tomMatch2_5))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch2_end_4) { tomMatch2_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch2_end_4 = tom_get_tail_conc_L(tomMatch2_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch2_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 

		return l 
  }



func sort2(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch3_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch3_end_4; for { { if !(tom_is_empty_conc_L(tomMatch3_end_4)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch3_end_4); _ = tom___x; var tomMatch3_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch3_end_4); _ = tomMatch3_5; if !(tom_is_empty_conc_L(tomMatch3_5)) { var tom___y  aterm.ATerm  = tom_get_head_conc_L(tomMatch3_5); _ = tom___y; 

        xname := (tom___x).(*aterm.ATermAppl).GetName()
        yname := (tom___y).(*aterm.ATermAppl).GetName()
        if strings.Compare(xname, yname) > 0 { 
          return sort2(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch3_end_4,tom_empty_list_conc()),tom_cons_list_conc(tom___y,tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch3_5),tom_empty_list_conc()))))) };  };  }; if tom_is_empty_conc_L(tomMatch3_end_4) { tomMatch3_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch3_end_4 = tom_get_tail_conc_L(tomMatch3_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch3_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 



		return l 
  }



func double2(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch4_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch4_end_4; for { { if !(tom_is_empty_conc_L(tomMatch4_end_4)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch4_end_4); _ = tom___x; var tomMatch4_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch4_end_4); _ = tomMatch4_5; var tomMatch4_end_8  *aterm.ATermList  = tomMatch4_5; _ = tomMatch4_end_8; for { { if !(tom_is_empty_conc_L(tomMatch4_end_8)) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch4_end_8)) { 

        return double2(tom_make_id(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch4_end_4,tom_empty_list_conc()),tom_append_list_conc(tom_get_slice_conc(tomMatch4_5,tomMatch4_end_8,tom_empty_list_conc()),tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch4_end_8),tom_empty_list_conc())))))) };  }; if tom_is_empty_conc_L(tomMatch4_end_8) { tomMatch4_end_8 = tomMatch4_5;  } else { tomMatch4_end_8 = tom_get_tail_conc_L(tomMatch4_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_8, tomMatch4_5))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch4_end_4) { tomMatch4_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch4_end_4 = tom_get_tail_conc_L(tomMatch4_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 


		return l 
  }



func double4(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch5_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch5_end_4; for { { if !(tom_is_empty_conc_L(tomMatch5_end_4)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch5_end_4); _ = tom___x; var tomMatch5_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch5_end_4); _ = tomMatch5_5; var tomMatch5_end_8  *aterm.ATermList  = tomMatch5_5; _ = tomMatch5_end_8; for { { if !(tom_is_empty_conc_L(tomMatch5_end_8)) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch5_end_8)) { 
 return double4(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch5_end_4,tom_empty_list_conc()),tom_append_list_conc(tom_get_slice_conc(tomMatch5_5,tomMatch5_end_8,tom_empty_list_conc()),tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch5_end_8),tom_empty_list_conc()))))) };  }; if tom_is_empty_conc_L(tomMatch5_end_8) { tomMatch5_end_8 = tomMatch5_5;  } else { tomMatch5_end_8 = tom_get_tail_conc_L(tomMatch5_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_8, tomMatch5_5))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch5_end_4) { tomMatch5_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch5_end_4 = tom_get_tail_conc_L(tomMatch5_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 

		return l 
  }



func double5(l *aterm.ATermList) *aterm.ATermList {
    ; { { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch6_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch6_end_4; for { { if !(tom_is_empty_conc_L(tomMatch6_end_4)) { if tom_is_fun_sym_a(any(tom_get_head_conc_L(tomMatch6_end_4)).( aterm.ATerm )) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch6_end_4); _ = tom___x; var tomMatch6_5  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch6_end_4); _ = tomMatch6_5; var tomMatch6_end_8  *aterm.ATermList  = tomMatch6_5; _ = tomMatch6_end_8; for { { if !(tom_is_empty_conc_L(tomMatch6_end_8)) { if tom_is_fun_sym_a(any(tom_get_head_conc_L(tomMatch6_end_8)).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch6_end_8)) { 
 return double5(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch6_end_4,tom_empty_list_conc()),tom_append_list_conc(tom_get_slice_conc(tomMatch6_5,tomMatch6_end_8,tom_empty_list_conc()),tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch6_end_8),tom_empty_list_conc()))))) };  };  }; if tom_is_empty_conc_L(tomMatch6_end_8) { tomMatch6_end_8 = tomMatch6_5;  } else { tomMatch6_end_8 = tom_get_tail_conc_L(tomMatch6_end_8);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_8, tomMatch6_5))) { break; }; };  };  }; if tom_is_empty_conc_L(tomMatch6_end_4) { tomMatch6_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch6_end_4 = tom_get_tail_conc_L(tomMatch6_end_4);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch6_end_20  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch6_end_20; for { { if !(tom_is_empty_conc_L(tomMatch6_end_20)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch6_end_20); _ = tom___x; var tomMatch6_21  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch6_end_20); _ = tomMatch6_21; var tomMatch6_end_24  *aterm.ATermList  = tomMatch6_21; _ = tomMatch6_end_24; for { { if !(tom_is_empty_conc_L(tomMatch6_end_24)) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch6_end_24)) { 
 return double5(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch6_end_20,tom_empty_list_conc()),tom_append_list_conc(tom_get_slice_conc(tomMatch6_21,tomMatch6_end_24,tom_empty_list_conc()),tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch6_end_24),tom_empty_list_conc()))))) };  }; if tom_is_empty_conc_L(tomMatch6_end_24) { tomMatch6_end_24 = tomMatch6_21;  } else { tomMatch6_end_24 = tom_get_tail_conc_L(tomMatch6_end_24);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_24, tomMatch6_21))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch6_end_20) { tomMatch6_end_20 = any(l).( *aterm.ATermList );  } else { tomMatch6_end_20 = tom_get_tail_conc_L(tomMatch6_end_20);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_20, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_is_sort_L(l) { if tom_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch6_end_32  *aterm.ATermList  = any(l).( *aterm.ATermList ); _ = tomMatch6_end_32; for { { if !(tom_is_empty_conc_L(tomMatch6_end_32)) { var tom___x  aterm.ATerm  = tom_get_head_conc_L(tomMatch6_end_32); _ = tom___x; var tomMatch6_33  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch6_end_32); _ = tomMatch6_33; var tomMatch6_end_36  *aterm.ATermList  = tomMatch6_33; _ = tomMatch6_end_36; for { { if !(tom_is_empty_conc_L(tomMatch6_end_36)) { if tom_equal_term_E(tom___x, tom_get_head_conc_L(tomMatch6_end_36)) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch6_end_32), tom_get_head_conc_L(tomMatch6_end_36)) { 
 return double5(tom_append_list_conc(tom_get_slice_conc(any(l).( *aterm.ATermList ),tomMatch6_end_32,tom_empty_list_conc()),tom_append_list_conc(tom_get_slice_conc(tomMatch6_33,tomMatch6_end_36,tom_empty_list_conc()),tom_cons_list_conc(tom___x,tom_append_list_conc(tom_get_tail_conc_L(tomMatch6_end_36),tom_empty_list_conc()))))) };  };  }; if tom_is_empty_conc_L(tomMatch6_end_36) { tomMatch6_end_36 = tomMatch6_33;  } else { tomMatch6_end_36 = tom_get_tail_conc_L(tomMatch6_end_36);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_36, tomMatch6_33))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch6_end_32) { tomMatch6_end_32 = any(l).( *aterm.ATermList );  } else { tomMatch6_end_32 = tom_get_tail_conc_L(tomMatch6_end_32);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_32, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 

		return l 
  }



func testList1() {
    var ta aterm.ATerm = factory.MakeAppl(factory.MakeAFun("a", 0, false))
    var tb aterm.ATerm = factory.MakeAppl(factory.MakeAFun("b", 0, false))
    var tc aterm.ATerm = factory.MakeAppl(factory.MakeAFun("c", 0, false))
    l := factory.MakeListEmpty().
      Append(ta).Append(tb).Append(tc).
      Append(ta).Append(tb).Append(tc).
      Append(ta)

    res := factory.MakeListEmpty().
      Append(ta).Append(ta).Append(ta).
      Append(tb).Append(tb).Append(tc).
      Append(tc)
    
    assertTrue(sort1(l) == res)
    assertTrue(sort2(l) == res)
    fmt.Println("testList1 has successfully terminated.")
  }
 

func testList2() {
    var l *aterm.ATermList   = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_cons_list_conc(tom_make_a(),tom_empty_list_conc())))))))
    var res *aterm.ATermList = tom_cons_list_conc(tom_make_a(),tom_cons_list_conc(tom_make_b(),tom_cons_list_conc(tom_make_c(),tom_empty_list_conc())))
    
    assertTrue(double1(sort1(l)) == res)

    assertTrue(double2(sort2(l)) == res)
    assertTrue(double4(sort2(l)) == res)
    assertTrue(double5(sort2(l)) == res)


      
      
      
    fmt.Println("testList2 has successfully terminated.")
  }
  

func assertTrue(condition bool) {
    if !condition {
      panic("assertion failed.")
    }
  }
