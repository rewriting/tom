






package testmatch



import (
  "os"
  "testing"

  "tom/library/aterm"
)



var ok, fail aterm.ATerm
var pattern1, pattern2, pattern3, pattern4, pattern5 aterm.ATerm
 

var factory *aterm.PureFactory = aterm.GetInstance()



func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  clean()
  os.Exit(code)
}
	

func setUp() {
    ok   = factory.Parse("ok")
    fail = factory.Parse("fail")
    pattern1 = factory.Parse("pattern1")
    pattern2 = factory.Parse("pattern2")
    pattern3 = factory.Parse("pattern3")
    pattern4 = factory.Parse("pattern4")
    pattern5 = factory.Parse("pattern5")
  }



func clean() {
    ok   = nil
    fail = nil
    pattern1 = nil
    pattern2 = nil
    pattern3 = nil
    pattern4 = nil
    pattern5 = nil
  }

  func tom_equal_term_L(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_is_sort_L(t any) bool { return t != nil;  }; func tom_is_fun_sym_conc(t  *aterm.ATermList ) bool { return t != nil;  }; func tom_empty_list_conc()  *aterm.ATermList  { return (factory.MakeListEmpty()) }; func tom_cons_list_conc(e  aterm.ATerm , l  *aterm.ATermList )  *aterm.ATermList  { return (any(l).(*aterm.ATermList).Insert(e)) }; func tom_get_head_conc_L(l  *aterm.ATermList )  aterm.ATerm  { return any(l).(*aterm.ATermList).GetFirst();  }; func tom_get_tail_conc_L(l  *aterm.ATermList )  *aterm.ATermList  { return any(l).(*aterm.ATermList).GetNext();  }; func tom_is_empty_conc_L(l  *aterm.ATermList ) bool { return any(l).(*aterm.ATermList).IsEmpty();  }; func tom_append_list_conc(l1  *aterm.ATermList , l2  *aterm.ATermList )  *aterm.ATermList  { if tom_is_empty_conc_L(l1) { return l2 } else if tom_is_empty_conc_L(l2) { return l1 } else if tom_is_empty_conc_L(tom_get_tail_conc_L(l1)) { return tom_cons_list_conc(tom_get_head_conc_L(l1), l2) } else { return tom_cons_list_conc(tom_get_head_conc_L(l1), tom_append_list_conc(tom_get_tail_conc_L(l1), l2)) }; }; func tom_get_slice_conc(begin  *aterm.ATermList , end  *aterm.ATermList , tail  *aterm.ATermList )  *aterm.ATermList  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_conc_L(end) || tom_equal_term_L(end, tom_empty_list_conc())) { return begin }; return tom_cons_list_conc(tom_get_head_conc_L(begin), tom_get_slice_conc(tom_get_tail_conc_L(begin), end, tail)) }; func tom_equal_term_E(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return (t1 == t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_is_fun_sym_a(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "a";  }; func tom_is_fun_sym_b(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "b";  }; func tom_is_fun_sym_c(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "c";  }; func tom_is_fun_sym_d(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "d";  }; func tom_is_fun_sym_f(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "f";  }; func tom_get_slot_f_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_is_fun_sym_ff(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "ff";  }; func tom_get_slot_ff_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_is_fun_sym_g(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "g";  }; func tom_get_slot_g_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_get_slot_g_s2(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(1);  }; func tom_is_fun_sym_l(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "l";  }; func tom_get_slot_l_sl(t  aterm.ATerm )  *aterm.ATermList  { return any(t).(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermList);  }; func tom_is_fun_sym_h(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "h";  }; func tom_get_slot_h_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_get_slot_h_s2(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(1);  }; func tom_is_fun_sym_k(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "k";  }; func tom_get_slot_k_s2(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_get_slot_k_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(1);  }; 





































































type TestData struct {
		question string
		answer string
	}



type TestData2 struct {
		question1 string
		question2 string
		answer string
	}

 func Test1(t *testing.T) {
		TEST := []TestData {
			{"g(a,a)"                     ,"pattern1"},
			{"g(a,b)"                     ,"fail    "},
			{"h(f(b),f(b))"               ,"pattern2"},
			{"h(f(a),f(b))"               ,"fail    "},
			{"h(f(f(g(a,b))),g(b,g(a,b)))","pattern3"},
			{"h(f(f(g(a,b))),g(b,g(b,b)))","fail    "},
			{"h(b,g(b,b))"                ,"pattern4"},
			{"h(a,g(b,b))"                ,"fail    "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch1 expected "+TEST[i].answer+" for match1("+TEST[i].question+")",
				match1(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
			assertSame(t,
				"TestMatch1 expected "+TEST[i].answer+" for match1bis("+TEST[i].question+")",
				match1bis(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match1(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_g(any(t).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_g_s1(any(t).( aterm.ATerm )), tom_get_slot_g_s2(any(t).( aterm.ATerm ))) { 
 return pattern1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch1_7  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch1_7; var tomMatch1_8  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch1_8; if tom_is_fun_sym_f(any(tomMatch1_7).( aterm.ATerm )) { if tom_is_fun_sym_f(any(tomMatch1_8).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_f_s1(tomMatch1_7), tom_get_slot_f_s1(tomMatch1_8)) { 
 return pattern2  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch1_19  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch1_19; var tomMatch1_20  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch1_20; if tom_is_fun_sym_f(any(tomMatch1_19).( aterm.ATerm )) { var tomMatch1_23  aterm.ATerm  = tom_get_slot_f_s1(tomMatch1_19); _ = tomMatch1_23; if tom_is_fun_sym_f(any(tomMatch1_23).( aterm.ATerm )) { if tom_is_fun_sym_g(any(tomMatch1_20).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_f_s1(tomMatch1_23), tom_get_slot_g_s2(tomMatch1_20)) { 
 return pattern3  };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch1_36  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch1_36; var tom___x  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tom___x; if tom_is_fun_sym_g(any(tomMatch1_36).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_slot_g_s1(tomMatch1_36)) { if tom_equal_term_E(tom___x, tom_get_slot_g_s2(tomMatch1_36)) { 
 return pattern4  };  };  };  };  };  };  }; 

    return res
  }

  func match1bis(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_g(any(t).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_g_s1(any(t).( aterm.ATerm )), tom_get_slot_g_s2(any(t).( aterm.ATerm ))) { 
 return pattern1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_7  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch2_7; var tomMatch2_8  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch2_8; if tom_is_fun_sym_f(any(tomMatch2_7).( aterm.ATerm )) { if tom_is_fun_sym_f(any(tomMatch2_8).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_f_s1(tomMatch2_7), tom_get_slot_f_s1(tomMatch2_8)) { 
 return pattern2  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_19  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch2_19; var tomMatch2_20  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch2_20; if tom_is_fun_sym_f(any(tomMatch2_19).( aterm.ATerm )) { var tomMatch2_23  aterm.ATerm  = tom_get_slot_f_s1(tomMatch2_19); _ = tomMatch2_23; if tom_is_fun_sym_f(any(tomMatch2_23).( aterm.ATerm )) { if tom_is_fun_sym_g(any(tomMatch2_20).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_f_s1(tomMatch2_23), tom_get_slot_g_s2(tomMatch2_20)) { 
 return pattern3  };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_36  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch2_36; var tom___x  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tom___x; if tom_is_fun_sym_g(any(tomMatch2_36).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_slot_g_s1(tomMatch2_36)) { if tom_equal_term_E(tom___x, tom_get_slot_g_s2(tomMatch2_36)) { 
 return pattern4  };  };  };  };  };  }; { if tom_is_sort_E(t) { 
 return res  };  };  }; 

    return res
  }

 func Test2(t *testing.T) {
		TEST := []TestData2 {
			{"g(a,b)"      ,"f(a)","pattern1 "},
			{"g(b,b)"      ,"f(a)","fail     "},
			{"g(f(a),f(a))","f(a)","pattern2 "},
			{"g(f(b),f(a))","f(a)","fail     "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch2 expected "+TEST[i].answer+" for match2("+TEST[i].question1+","+TEST[i].question2+")",
				match2(factory.Parse(TEST[i].question1),factory.Parse(TEST[i].question2)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match2(t1, t2 aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t1) { if tom_is_fun_sym_g(any(t1).( aterm.ATerm )) { if tom_is_sort_E(t2) { if tom_is_fun_sym_f(any(t2).( aterm.ATerm )) { if tom_equal_term_E(tom_get_slot_g_s1(any(t1).( aterm.ATerm )), tom_get_slot_f_s1(any(t2).( aterm.ATerm ))) { 
 return pattern1  };  };  };  };  };  }; { if tom_is_sort_E(t1) { if tom_is_fun_sym_g(any(t1).( aterm.ATerm )) { var tomMatch3_12  aterm.ATerm  = tom_get_slot_g_s1(any(t1).( aterm.ATerm )); _ = tomMatch3_12; var tomMatch3_13  aterm.ATerm  = tom_get_slot_g_s2(any(t1).( aterm.ATerm )); _ = tomMatch3_13; if tom_is_fun_sym_f(any(tomMatch3_12).( aterm.ATerm )) { var tom___x  aterm.ATerm  = tom_get_slot_f_s1(tomMatch3_12); _ = tom___x; if tom_is_fun_sym_f(any(tomMatch3_13).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_slot_f_s1(tomMatch3_13)) { if tom_is_sort_E(t2) { if tom_is_fun_sym_f(any(t2).( aterm.ATerm )) { if tom_equal_term_E(tom___x, tom_get_slot_f_s1(any(t2).( aterm.ATerm ))) { 
 return pattern2  };  };  };  };  };  };  };  };  };  }; 

    return res
  }



func Test3(t *testing.T) {
		TEST := []TestData {
			{"h(l([a,b,a,b]),a)"               ,"pattern1"},
			{"h(l([a,b,a,b]),l([a,a,b,a]))"    ,"fail    "},
			{"h(l([a,b,a,b]),l([a,a,b,a,b]))"  ,"pattern2"},
			{"l([b,b,l([a]),a,l([a,c]),b,c])"  ,"fail    "},
			{"l([a,b,l([a]),a,l([a,c]),b,c])"  ,"pattern3"},
			{"l([a,b,l([c]),a,b,l([a,c]),b,c])","fail    "},
			{"l([a,b,l([c]),a,c,l([a,c]),b,c])","pattern3"},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch3 expected "+TEST[i].answer+" for match3("+TEST[i].question+")",
				match3(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match3(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch4_1  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch4_1; if tom_is_fun_sym_l(any(tomMatch4_1).( aterm.ATerm )) { var tomMatch4_5  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch4_1); _ = tomMatch4_5; if tom_is_fun_sym_conc(any(tomMatch4_5).( *aterm.ATermList )) { var tomMatch4_end_11  *aterm.ATermList  = tomMatch4_5; _ = tomMatch4_end_11; for { { if !(tom_is_empty_conc_L(tomMatch4_end_11)) { var tomMatch4_12  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch4_end_11); _ = tomMatch4_12; if !(tom_is_empty_conc_L(tomMatch4_12)) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch4_12), tom_get_slot_h_s2(any(t).( aterm.ATerm ))) { 
 return pattern1  };  };  }; if tom_is_empty_conc_L(tomMatch4_end_11) { tomMatch4_end_11 = tomMatch4_5;  } else { tomMatch4_end_11 = tom_get_tail_conc_L(tomMatch4_end_11);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_11, tomMatch4_5))) { break; }; };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch4_17  aterm.ATerm  = tom_get_slot_h_s1(any(t).( aterm.ATerm )); _ = tomMatch4_17; var tomMatch4_18  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch4_18; if tom_is_fun_sym_l(any(tomMatch4_17).( aterm.ATerm )) { var tomMatch4_21  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch4_17); _ = tomMatch4_21; if tom_is_fun_sym_conc(any(tomMatch4_21).( *aterm.ATermList )) { var tomMatch4_end_30  *aterm.ATermList  = tomMatch4_21; _ = tomMatch4_end_30; for { { if !(tom_is_empty_conc_L(tomMatch4_end_30)) { var tom___X2  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch4_end_30); _ = tom___X2; if tom_is_fun_sym_l(any(tomMatch4_18).( aterm.ATerm )) { var tomMatch4_24  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch4_18); _ = tomMatch4_24; if tom_is_fun_sym_conc(any(tomMatch4_24).( *aterm.ATermList )) { var tomMatch4_end_36  *aterm.ATermList  = tomMatch4_24; _ = tomMatch4_end_36; for { { if !(tom_is_empty_conc_L(tomMatch4_end_36)) { if tom_equal_term_L(tom___X2, tom_get_tail_conc_L(tomMatch4_end_36)) { 
 if !(tom___X2).IsEmpty() {  return pattern2  };  };  }; if tom_is_empty_conc_L(tomMatch4_end_36) { tomMatch4_end_36 = tomMatch4_24;  } else { tomMatch4_end_36 = tom_get_tail_conc_L(tomMatch4_end_36);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_36, tomMatch4_24))) { break; }; };  };  };  }; if tom_is_empty_conc_L(tomMatch4_end_30) { tomMatch4_end_30 = tomMatch4_21;  } else { tomMatch4_end_30 = tom_get_tail_conc_L(tomMatch4_end_30);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_30, tomMatch4_21))) { break; }; };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_l(any(t).( aterm.ATerm )) { var tomMatch4_41  *aterm.ATermList  = tom_get_slot_l_sl(any(t).( aterm.ATerm )); _ = tomMatch4_41; if tom_is_fun_sym_conc(any(tomMatch4_41).( *aterm.ATermList )) { var tomMatch4_end_47  *aterm.ATermList  = tomMatch4_41; _ = tomMatch4_end_47; for { { var tomMatch4_end_50  *aterm.ATermList  = tomMatch4_end_47; _ = tomMatch4_end_50; for { { var tomMatch4_end_53  *aterm.ATermList  = tomMatch4_end_50; _ = tomMatch4_end_53; for { { if !(tom_is_empty_conc_L(tomMatch4_end_53)) { var tomMatch4_57  aterm.ATerm  = tom_get_head_conc_L(tomMatch4_end_53); _ = tomMatch4_57; if tom_is_fun_sym_l(any(tomMatch4_57).( aterm.ATerm )) { var tomMatch4_56  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch4_57); _ = tomMatch4_56; if tom_is_fun_sym_conc(any(tomMatch4_56).( *aterm.ATermList )) { if tom_equal_term_L(tom_get_slice_conc(tomMatch4_end_47,tomMatch4_end_50,tom_empty_list_conc()), tomMatch4_56) { 
 return pattern3  };  };  };  }; if tom_is_empty_conc_L(tomMatch4_end_53) { tomMatch4_end_53 = tomMatch4_end_50;  } else { tomMatch4_end_53 = tom_get_tail_conc_L(tomMatch4_end_53);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_53, tomMatch4_end_50))) { break; }; }; if tom_is_empty_conc_L(tomMatch4_end_50) { tomMatch4_end_50 = tomMatch4_end_47;  } else { tomMatch4_end_50 = tom_get_tail_conc_L(tomMatch4_end_50);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_50, tomMatch4_end_47))) { break; }; }; if tom_is_empty_conc_L(tomMatch4_end_47) { tomMatch4_end_47 = tomMatch4_41;  } else { tomMatch4_end_47 = tom_get_tail_conc_L(tomMatch4_end_47);  };  };  if !(!(tom_equal_term_L(tomMatch4_end_47, tomMatch4_41))) { break; }; };  };  };  };  };  }; 

    return res
  }

 func Test4(t *testing.T) {
		TEST := []TestData {
			{"h(a, l([a,b,f(b),a]))",     "pattern1"},
			{"h(a, l([a,b,f(a),b]))",     "fail    "},
			{"h(b, l([a,b,f(a),a]))",     "pattern2"},
			{"h(b, l([b,b,f(a),b]))",     "fail    "},
			{"h(c, l([a,f(a),f(a),a]))",  "pattern3"},
			{"h(c, l([a,f(a),f(b),a]))",  "fail    "},
			{"h(d, l([f(a),f(b),f(a)]))", "pattern4"},
		  {"h(d, l([a,f(a),f(b),a]))",  "fail    "},
		};

		for i := 0; i<len(TEST);i++ {
			assertSame(t,
				"TestMatch4 expected "+TEST[i].answer+" for match4("+TEST[i].question+")",
				match4(factory.Parse(TEST[i].question)),
				factory.Parse(TEST[i].answer),
				)
		}
  }

  func match4(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch5_2  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch5_2; if tom_is_fun_sym_a(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch5_2).( aterm.ATerm )) { var tomMatch5_7  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch5_2); _ = tomMatch5_7; if tom_is_fun_sym_conc(any(tomMatch5_7).( *aterm.ATermList )) { var tomMatch5_end_13  *aterm.ATermList  = tomMatch5_7; _ = tomMatch5_end_13; for { { if !(tom_is_empty_conc_L(tomMatch5_end_13)) { var tomMatch5_14  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch5_end_13); _ = tomMatch5_14; if !(tom_is_empty_conc_L(tomMatch5_14)) { var tomMatch5_18  aterm.ATerm  = tom_get_head_conc_L(tomMatch5_14); _ = tomMatch5_18; if tom_is_fun_sym_f(any(tomMatch5_18).( aterm.ATerm )) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch5_end_13), tom_get_slot_f_s1(tomMatch5_18)) { 
 return pattern1  };  };  };  }; if tom_is_empty_conc_L(tomMatch5_end_13) { tomMatch5_end_13 = tomMatch5_7;  } else { tomMatch5_end_13 = tom_get_tail_conc_L(tomMatch5_end_13);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_13, tomMatch5_7))) { break; }; };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch5_23  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch5_23; if tom_is_fun_sym_b(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch5_23).( aterm.ATerm )) { var tomMatch5_28  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch5_23); _ = tomMatch5_28; if tom_is_fun_sym_conc(any(tomMatch5_28).( *aterm.ATermList )) { var tomMatch5_end_34  *aterm.ATermList  = tomMatch5_28; _ = tomMatch5_end_34; for { { if !(tom_is_empty_conc_L(tomMatch5_end_34)) { var tomMatch5_35  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch5_end_34); _ = tomMatch5_35; var tomMatch5_end_38  *aterm.ATermList  = tomMatch5_35; _ = tomMatch5_end_38; for { { if !(tom_is_empty_conc_L(tomMatch5_end_38)) { var tomMatch5_42  aterm.ATerm  = tom_get_head_conc_L(tomMatch5_end_38); _ = tomMatch5_42; if tom_is_fun_sym_f(any(tomMatch5_42).( aterm.ATerm )) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch5_end_34), tom_get_slot_f_s1(tomMatch5_42)) { 
 return pattern2  };  };  }; if tom_is_empty_conc_L(tomMatch5_end_38) { tomMatch5_end_38 = tomMatch5_35;  } else { tomMatch5_end_38 = tom_get_tail_conc_L(tomMatch5_end_38);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_38, tomMatch5_35))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch5_end_34) { tomMatch5_end_34 = tomMatch5_28;  } else { tomMatch5_end_34 = tom_get_tail_conc_L(tomMatch5_end_34);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_34, tomMatch5_28))) { break; }; };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch5_47  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch5_47; if tom_is_fun_sym_c(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch5_47).( aterm.ATerm )) { var tomMatch5_52  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch5_47); _ = tomMatch5_52; if tom_is_fun_sym_conc(any(tomMatch5_52).( *aterm.ATermList )) { var tomMatch5_end_58  *aterm.ATermList  = tomMatch5_52; _ = tomMatch5_end_58; for { { if !(tom_is_empty_conc_L(tomMatch5_end_58)) { var tomMatch5_59  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch5_end_58); _ = tomMatch5_59; if !(tom_is_empty_conc_L(tomMatch5_59)) { if tom_is_fun_sym_f(any(tom_get_head_conc_L(tomMatch5_59)).( aterm.ATerm )) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch5_end_58), tom_get_head_conc_L(tomMatch5_59)) { 
 return pattern3  };  };  };  }; if tom_is_empty_conc_L(tomMatch5_end_58) { tomMatch5_end_58 = tomMatch5_52;  } else { tomMatch5_end_58 = tom_get_tail_conc_L(tomMatch5_end_58);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_58, tomMatch5_52))) { break; }; };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch5_68  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch5_68; if tom_is_fun_sym_d(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch5_68).( aterm.ATerm )) { var tomMatch5_73  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch5_68); _ = tomMatch5_73; if tom_is_fun_sym_conc(any(tomMatch5_73).( *aterm.ATermList )) { var tomMatch5_end_79  *aterm.ATermList  = tomMatch5_73; _ = tomMatch5_end_79; for { { if !(tom_is_empty_conc_L(tomMatch5_end_79)) { var tomMatch5_80  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch5_end_79); _ = tomMatch5_80; var tomMatch5_end_83  *aterm.ATermList  = tomMatch5_80; _ = tomMatch5_end_83; for { { if !(tom_is_empty_conc_L(tomMatch5_end_83)) { if tom_is_fun_sym_f(any(tom_get_head_conc_L(tomMatch5_end_83)).( aterm.ATerm )) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch5_end_79), tom_get_head_conc_L(tomMatch5_end_83)) { 
 return pattern4  };  };  }; if tom_is_empty_conc_L(tomMatch5_end_83) { tomMatch5_end_83 = tomMatch5_80;  } else { tomMatch5_end_83 = tom_get_tail_conc_L(tomMatch5_end_83);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_83, tomMatch5_80))) { break; }; };  }; if tom_is_empty_conc_L(tomMatch5_end_79) { tomMatch5_end_79 = tomMatch5_73;  } else { tomMatch5_end_79 = tom_get_tail_conc_L(tomMatch5_end_79);  };  };  if !(!(tom_equal_term_L(tomMatch5_end_79, tomMatch5_73))) { break; }; };  };  };  };  };  };  };  }; 

    return res
  }

 func Test5(t *testing.T) {
    assertTrue(t, pattern1 == match5(factory.Parse("h(a, l([f(a),f(b),f(a),f(c)]))")))
    assertTrue(t, fail == match5(factory.Parse("h(a, l([f(a),f(b),f(c)]))")))

    assertTrue(t, pattern2 == match5(factory.Parse("h(b, l([ff(f(a)),f(b),ff(f(a)),f(c)]))")))
    assertTrue(t, fail == match5(factory.Parse("h(b, l([ff(f(a)),f(b),ff(f(b)),f(c)]))")))
  }

  func match5(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch6_2  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch6_2; if tom_is_fun_sym_a(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch6_2).( aterm.ATerm )) { var tomMatch6_7  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch6_2); _ = tomMatch6_7; if tom_is_fun_sym_conc(any(tomMatch6_7).( *aterm.ATermList )) { var tomMatch6_end_13  *aterm.ATermList  = tomMatch6_7; _ = tomMatch6_end_13; for { { if !(tom_is_empty_conc_L(tomMatch6_end_13)) { if tom_is_fun_sym_f(any(tom_get_head_conc_L(tomMatch6_end_13)).( aterm.ATerm )) { var tomMatch6_14  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch6_end_13); _ = tomMatch6_14; var tomMatch6_end_17  *aterm.ATermList  = tomMatch6_14; _ = tomMatch6_end_17; for { { if !(tom_is_empty_conc_L(tomMatch6_end_17)) { if tom_is_fun_sym_f(any(tom_get_head_conc_L(tomMatch6_end_17)).( aterm.ATerm )) { if tom_equal_term_E(tom_get_head_conc_L(tomMatch6_end_13), tom_get_head_conc_L(tomMatch6_end_17)) { 
 return pattern1  };  };  }; if tom_is_empty_conc_L(tomMatch6_end_17) { tomMatch6_end_17 = tomMatch6_14;  } else { tomMatch6_end_17 = tom_get_tail_conc_L(tomMatch6_end_17);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_17, tomMatch6_14))) { break; }; };  };  }; if tom_is_empty_conc_L(tomMatch6_end_13) { tomMatch6_end_13 = tomMatch6_7;  } else { tomMatch6_end_13 = tom_get_tail_conc_L(tomMatch6_end_13);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_13, tomMatch6_7))) { break; }; };  };  };  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch6_29  aterm.ATerm  = tom_get_slot_h_s2(any(t).( aterm.ATerm )); _ = tomMatch6_29; if tom_is_fun_sym_b(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_is_fun_sym_l(any(tomMatch6_29).( aterm.ATerm )) { var tomMatch6_34  *aterm.ATermList  = tom_get_slot_l_sl(tomMatch6_29); _ = tomMatch6_34; if tom_is_fun_sym_conc(any(tomMatch6_34).( *aterm.ATermList )) { var tomMatch6_end_40  *aterm.ATermList  = tomMatch6_34; _ = tomMatch6_end_40; for { { if !(tom_is_empty_conc_L(tomMatch6_end_40)) { var tomMatch6_48  aterm.ATerm  = tom_get_head_conc_L(tomMatch6_end_40); _ = tomMatch6_48; if tom_is_fun_sym_ff(any(tomMatch6_48).( aterm.ATerm )) { var tomMatch6_47  aterm.ATerm  = tom_get_slot_ff_s1(tomMatch6_48); _ = tomMatch6_47; if tom_is_fun_sym_f(any(tomMatch6_47).( aterm.ATerm )) { var tomMatch6_41  *aterm.ATermList  = tom_get_tail_conc_L(tomMatch6_end_40); _ = tomMatch6_41; var tomMatch6_end_44  *aterm.ATermList  = tomMatch6_41; _ = tomMatch6_end_44; for { { if !(tom_is_empty_conc_L(tomMatch6_end_44)) { var tomMatch6_54  aterm.ATerm  = tom_get_head_conc_L(tomMatch6_end_44); _ = tomMatch6_54; if tom_is_fun_sym_ff(any(tomMatch6_54).( aterm.ATerm )) { var tomMatch6_53  aterm.ATerm  = tom_get_slot_ff_s1(tomMatch6_54); _ = tomMatch6_53; if tom_is_fun_sym_f(any(tomMatch6_53).( aterm.ATerm )) { if tom_equal_term_E(tomMatch6_47, tomMatch6_53) { 
 return pattern2  };  };  };  }; if tom_is_empty_conc_L(tomMatch6_end_44) { tomMatch6_end_44 = tomMatch6_41;  } else { tomMatch6_end_44 = tom_get_tail_conc_L(tomMatch6_end_44);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_44, tomMatch6_41))) { break; }; };  };  };  }; if tom_is_empty_conc_L(tomMatch6_end_40) { tomMatch6_end_40 = tomMatch6_34;  } else { tomMatch6_end_40 = tom_get_tail_conc_L(tomMatch6_end_40);  };  };  if !(!(tom_equal_term_L(tomMatch6_end_40, tomMatch6_34))) { break; }; };  };  };  };  };  };  };  }; 

    return res
  }

 func Test6(t *testing.T) {
    res := match6(factory.Parse("h(a(),b())"))
    assertTrue2(t, "slot s1 of \"h\" is a(), it should match, but got "+res.String(),
        pattern2 == res)
    res = match6(factory.Parse("k(a(),b())"))
    
    assertTrue2(t, "slot s1 of \"k\" is b(), it should not match, but got "+res.String(),
        fail     == res)
    res = match6(factory.Parse("k(b(),a())"))
    assertTrue2(t, "slot s1 of \"k\" is a(), it should match, but got "+res.String(),
        pattern3 == res)
  }

  func match6(t aterm.ATerm) aterm.ATerm {
    res := fail
    ; { { if tom_is_sort_E(t) { var tomMatch7_7 bool =  false ; _ = tomMatch7_7; var tomMatch7_4  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch7_4; var tomMatch7_1  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch7_1; var tomMatch7_3  aterm.ATerm  =  *new(aterm.ATerm) ; _ = tomMatch7_3; if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { { tomMatch7_7 =  true ; tomMatch7_3 = any(t).( aterm.ATerm ); tomMatch7_1 = tom_get_slot_h_s1(tomMatch7_3);  };  } else { if tom_is_fun_sym_k(any(t).( aterm.ATerm )) { { tomMatch7_7 =  true ; tomMatch7_4 = any(t).( aterm.ATerm ); tomMatch7_1 = tom_get_slot_k_s1(tomMatch7_4);  };  };  }; if tomMatch7_7 { if tom_is_fun_sym_a(any(tomMatch7_1).( aterm.ATerm )) { ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_h(any(t).( aterm.ATerm )) { if tom_is_fun_sym_a(any(tom_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { 


 return pattern2  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_k(any(t).( aterm.ATerm )) { if tom_is_fun_sym_a(any(tom_get_slot_k_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { 
 return pattern3  };  };  };  };  }; 

        return pattern1
       };  };  };  };  }; 

    return res
  }






func assertSame(t *testing.T, message string, expected, actual aterm.ATerm) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Error()
  }
}




func assertTrue2(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}
