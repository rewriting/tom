package testlist








import (
  "os"
  "testing"

  "tom/library/aterm"
)



var ok, fail aterm.ATerm
var factory *aterm.PureFactory = nil



func TestMain(m *testing.M) {
  setUp()
  code := m.Run()
  tearDown()
  os.Exit(code)
}



func setUp() {
  factory = aterm.GetInstance()
  ok      = factory.Parse("ok")
  fail    = factory.Parse("fail")
}



func tearDown() {
  factory = nil
  ok = nil
  fail = nil
}

func tom_TestList_equal_term_L(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_TestList_is_sort_L(t any) bool { return t != nil;  }; func tom_TestList_is_fun_sym_conc(t  *aterm.ATermList ) bool { return t != nil;  }; func tom_TestList_empty_list_conc()  *aterm.ATermList  { return (factory.MakeListEmpty()) }; func tom_TestList_cons_list_conc(e  aterm.ATerm , l  *aterm.ATermList )  *aterm.ATermList  { return (any(l).(*aterm.ATermList).Insert(e)) }; func tom_TestList_get_head_conc_L(l  *aterm.ATermList )  aterm.ATerm  { return any(l).(*aterm.ATermList).GetFirst();  }; func tom_TestList_get_tail_conc_L(l  *aterm.ATermList )  *aterm.ATermList  { return any(l).(*aterm.ATermList).GetNext();  }; func tom_TestList_is_empty_conc_L(l  *aterm.ATermList ) bool { return any(l).(*aterm.ATermList).IsEmpty();  }; func tom_TestList_append_list_conc(l1  *aterm.ATermList , l2  *aterm.ATermList )  *aterm.ATermList  { if tom_TestList_is_empty_conc_L(l1) { return l2 } else if tom_TestList_is_empty_conc_L(l2) { return l1 } else if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(l1)) { return tom_TestList_cons_list_conc(tom_TestList_get_head_conc_L(l1), l2) } else { return tom_TestList_cons_list_conc(tom_TestList_get_head_conc_L(l1), tom_TestList_append_list_conc(tom_TestList_get_tail_conc_L(l1), l2)) }; }; func tom_TestList_get_slice_conc(begin  *aterm.ATermList , end  *aterm.ATermList , tail  *aterm.ATermList )  *aterm.ATermList  { if tom_TestList_equal_term_L(begin, end) { return tail } else if tom_TestList_equal_term_L(end, tail) && (tom_TestList_is_empty_conc_L(end) || tom_TestList_equal_term_L(end, tom_TestList_empty_list_conc())) { return begin }; return tom_TestList_cons_list_conc(tom_TestList_get_head_conc_L(begin), tom_TestList_get_slice_conc(tom_TestList_get_tail_conc_L(begin), end, tail)) }; func tom_TestList_equal_term_E(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return (t1 == t2);  }; func tom_TestList_is_sort_E(t any) bool { return t != nil;  }; func tom_TestList_is_fun_sym_a(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "a";  }; func tom_TestList_is_fun_sym_b(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "b";  }; func tom_TestList_is_fun_sym_c(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "c";  }; func tom_TestList_is_fun_sym_f(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "f";  }; func tom_TestList_get_slot_f_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_TestList_is_fun_sym_g(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "g";  }; func tom_TestList_get_slot_g_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_TestList_is_fun_sym_l(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "l";  }; func tom_TestList_get_slot_l_sl(t  aterm.ATerm )  *aterm.ATermList  { return any(t).(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermList);  }; func tom_TestList_is_fun_sym_h(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "h";  }; func tom_TestList_get_slot_h_s1(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(0);  }; func tom_TestList_get_slot_h_s2(t  aterm.ATerm )  aterm.ATerm  { return any(t).(*aterm.ATermAppl).GetArgument(1);  }; 

























































func TestMatch1(t *testing.T) {
  for _, d := range match1Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch1 : expected "+answer+" for term "+question +"",
      match1(factory.Parse(question)),
      factory.Parse(answer))
  }
}



func match1Data() [][2]string {
  return [][2]string {
      { "[f(a)]",                  "pattern1" },
      { "[a,b,f(a)]",              "pattern2" },
      { "[a,f(a),b,f(b)]",         "pattern3" },
      { "[a,f(a),f(b)]",           "pattern3" },
      { "[f(a),f(b)]",             "pattern3" },
      { "[a,f(a),b,f(b),c]",       "pattern4" },
      { "[a,f(a),f(b),c]",         "pattern4" },
      { "[f(a),f(b),c]",           "pattern4" },
      { "[f(b),f(b)]",             "pattern5" },
      { "[f(b),f(b),c]",           "pattern5" },
      { "[f(b),a,f(b)]",           "pattern5" },
      { "[f(b),a,f(b),c]",         "pattern5" },
      { "[f(c),f(b)]",             "pattern6" },
      { "[f(a),f(c),a,f(c)]",          "fail" },
      { "[f(a),f(c),a,f(c),f(c)]", "pattern6" },
      { "[]",                      "pattern7" },
  }
}



func match1(t aterm.ATerm) aterm.ATerm {
  res := fail
  l := t.(*aterm.ATermList)
  ; { { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(any(l).( *aterm.ATermList ))) { var tomMatch1_4  aterm.ATerm  = tom_TestList_get_head_conc_L(any(l).( *aterm.ATermList )); if tom_TestList_is_fun_sym_f(any(tomMatch1_4).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch1_4)).( aterm.ATerm )) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(any(l).( *aterm.ATermList ))) { 
 return factory.Parse("pattern1") ;  };  };  };  };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch1_end_12  *aterm.ATermList  = any(l).( *aterm.ATermList ); for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_12)) { var tomMatch1_15  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_12); if tom_TestList_is_fun_sym_f(any(tomMatch1_15).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch1_15)).( aterm.ATerm )) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(tomMatch1_end_12)) { 
 return factory.Parse("pattern2") ;  };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_12) { tomMatch1_end_12 = any(l).( *aterm.ATermList );  } else { tomMatch1_end_12 = tom_TestList_get_tail_conc_L(tomMatch1_end_12);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_12, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch1_end_23  *aterm.ATermList  = any(l).( *aterm.ATermList ); for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_23)) { var tomMatch1_30  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_23); if tom_TestList_is_fun_sym_f(any(tomMatch1_30).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch1_30)).( aterm.ATerm )) { var tomMatch1_24  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch1_end_23); var tomMatch1_end_27  *aterm.ATermList  = tomMatch1_24; for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_27)) { var tomMatch1_35  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_27); if tom_TestList_is_fun_sym_f(any(tomMatch1_35).( aterm.ATerm )) { if tom_TestList_is_fun_sym_b(any(tom_TestList_get_slot_f_s1(tomMatch1_35)).( aterm.ATerm )) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(tomMatch1_end_27)) { 
 return factory.Parse("pattern3") ;  };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_27) { tomMatch1_end_27 = tomMatch1_24;  } else { tomMatch1_end_27 = tom_TestList_get_tail_conc_L(tomMatch1_end_27);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_27, tomMatch1_24))) { break; }; };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_23) { tomMatch1_end_23 = any(l).( *aterm.ATermList );  } else { tomMatch1_end_23 = tom_TestList_get_tail_conc_L(tomMatch1_end_23);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_23, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch1_end_43  *aterm.ATermList  = any(l).( *aterm.ATermList ); for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_43)) { var tomMatch1_51  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_43); if tom_TestList_is_fun_sym_f(any(tomMatch1_51).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch1_51)).( aterm.ATerm )) { var tomMatch1_44  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch1_end_43); var tomMatch1_end_47  *aterm.ATermList  = tomMatch1_44; for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_47)) { var tomMatch1_56  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_47); if tom_TestList_is_fun_sym_f(any(tomMatch1_56).( aterm.ATerm )) { if tom_TestList_is_fun_sym_b(any(tom_TestList_get_slot_f_s1(tomMatch1_56)).( aterm.ATerm )) { 
 return factory.Parse("pattern4") ;  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_47) { tomMatch1_end_47 = tomMatch1_44;  } else { tomMatch1_end_47 = tom_TestList_get_tail_conc_L(tomMatch1_end_47);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_47, tomMatch1_44))) { break; }; };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_43) { tomMatch1_end_43 = any(l).( *aterm.ATermList );  } else { tomMatch1_end_43 = tom_TestList_get_tail_conc_L(tomMatch1_end_43);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_43, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(any(l).( *aterm.ATermList ))) { var tomMatch1_69  aterm.ATerm  = tom_TestList_get_head_conc_L(any(l).( *aterm.ATermList )); if tom_TestList_is_fun_sym_f(any(tomMatch1_69).( aterm.ATerm )) { if tom_TestList_is_fun_sym_b(any(tom_TestList_get_slot_f_s1(tomMatch1_69)).( aterm.ATerm )) { var tomMatch1_62  *aterm.ATermList  = tom_TestList_get_tail_conc_L(any(l).( *aterm.ATermList )); var tomMatch1_end_65  *aterm.ATermList  = tomMatch1_62; for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_65)) { var tomMatch1_74  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_65); if tom_TestList_is_fun_sym_f(any(tomMatch1_74).( aterm.ATerm )) { if tom_TestList_is_fun_sym_b(any(tom_TestList_get_slot_f_s1(tomMatch1_74)).( aterm.ATerm )) { 
 return factory.Parse("pattern5") ;  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_65) { tomMatch1_end_65 = tomMatch1_62;  } else { tomMatch1_end_65 = tom_TestList_get_tail_conc_L(tomMatch1_end_65);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_65, tomMatch1_62))) { break; }; };  };  };  };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch1_end_82  *aterm.ATermList  = any(l).( *aterm.ATermList ); for { { if !(tom_TestList_is_empty_conc_L(tomMatch1_end_82)) { var tomMatch1_87  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch1_end_82); if tom_TestList_is_fun_sym_f(any(tomMatch1_87).( aterm.ATerm )) { if tom_TestList_is_fun_sym_c(any(tom_TestList_get_slot_f_s1(tomMatch1_87)).( aterm.ATerm )) { var tomMatch1_83  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch1_end_82); if !(tom_TestList_is_empty_conc_L(tomMatch1_83)) { if tom_TestList_is_fun_sym_f(any(tom_TestList_get_head_conc_L(tomMatch1_83)).( aterm.ATerm )) { 
 return factory.Parse("pattern6") ;  };  };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch1_end_82) { tomMatch1_end_82 = any(l).( *aterm.ATermList );  } else { tomMatch1_end_82 = tom_TestList_get_tail_conc_L(tomMatch1_end_82);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch1_end_82, any(l).( *aterm.ATermList )))) { break; }; };  };  };  }; { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { if tom_TestList_is_empty_conc_L(any(l).( *aterm.ATermList )) { 
 return factory.Parse("pattern7") ;  };  };  };  };  }; 

  return res
}





func TestMatch2(t *testing.T) {
  for _, d := range match2Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch2 : expected "+answer+" for term "+question +"",
      match2(factory.Parse(question)),
      factory.Parse(answer))
  }
}



func match2Data() [][2]string {
  return [][2]string {
      { "h(a,l([f(a)]))"                  , "pattern1" },
      { "h(l([f(a)]),a)"                  , "pattern2" },
      { "h(l([a,b,a,b]),a)"               , "pattern3" },
      { "h(l([a,b,a,b]),l([a,a,b,a]))"    , "fail"     },
      { "h(l([a,b,a,b]),l([a,a,b,a,b]))"  , "pattern4" },
      { "l([b,b,l([a]),a,l([a,c]),b,c])"  , "fail"     },
      { "l([a,b,l([a]),a,l([a,c]),b,c])"  , "pattern5" },
      { "l([a,b,l([c]),a,b,l([a,c]),b,c])", "fail"     },
      { "l([a,b,l([c]),a,c,l([a,c]),b,c])", "pattern5" },
  }
}



func match2(t aterm.ATerm) aterm.ATerm {
  res := fail
  ; { { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_2  aterm.ATerm  = tom_TestList_get_slot_h_s2(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_h_s1(any(t).( aterm.ATerm ))).( aterm.ATerm )) { if tom_TestList_is_fun_sym_l(any(tomMatch2_2).( aterm.ATerm )) { var tomMatch2_7  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_2); if tom_TestList_is_fun_sym_conc(any(tomMatch2_7).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(tomMatch2_7)) { var tomMatch2_13  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch2_7); if tom_TestList_is_fun_sym_f(any(tomMatch2_13).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch2_13)).( aterm.ATerm )) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(tomMatch2_7)) { 
 return factory.Parse("pattern1") ;  };  };  };  };  };  };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_18  aterm.ATerm  = tom_TestList_get_slot_h_s1(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_l(any(tomMatch2_18).( aterm.ATerm )) { var tomMatch2_22  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_18); if tom_TestList_is_fun_sym_conc(any(tomMatch2_22).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(tomMatch2_22)) { var tomMatch2_30  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch2_22); if tom_TestList_is_fun_sym_f(any(tomMatch2_30).( aterm.ATerm )) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_f_s1(tomMatch2_30)).( aterm.ATerm )) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(tomMatch2_22)) { if tom_TestList_is_fun_sym_a(any(tom_TestList_get_slot_h_s2(any(t).( aterm.ATerm ))).( aterm.ATerm )) { 
 return factory.Parse("pattern2") ;  };  };  };  };  };  };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_35  aterm.ATerm  = tom_TestList_get_slot_h_s1(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_l(any(tomMatch2_35).( aterm.ATerm )) { var tomMatch2_39  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_35); if tom_TestList_is_fun_sym_conc(any(tomMatch2_39).( *aterm.ATermList )) { var tomMatch2_end_45  *aterm.ATermList  = tomMatch2_39; for { { if !(tom_TestList_is_empty_conc_L(tomMatch2_end_45)) { var tomMatch2_46  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch2_end_45); if !(tom_TestList_is_empty_conc_L(tomMatch2_46)) { 
 if tom_TestList_get_head_conc_L(tomMatch2_46)==tom_TestList_get_slot_h_s2(any(t).( aterm.ATerm )){ return factory.Parse("pattern3") };  };  }; if tom_TestList_is_empty_conc_L(tomMatch2_end_45) { tomMatch2_end_45 = tomMatch2_39;  } else { tomMatch2_end_45 = tom_TestList_get_tail_conc_L(tomMatch2_end_45);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_45, tomMatch2_39))) { break; }; };  };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_h(any(t).( aterm.ATerm )) { var tomMatch2_50  aterm.ATerm  = tom_TestList_get_slot_h_s1(any(t).( aterm.ATerm )); var tomMatch2_51  aterm.ATerm  = tom_TestList_get_slot_h_s2(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_l(any(tomMatch2_50).( aterm.ATerm )) { var tomMatch2_54  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_50); if tom_TestList_is_fun_sym_conc(any(tomMatch2_54).( *aterm.ATermList )) { var tomMatch2_end_63  *aterm.ATermList  = tomMatch2_54; for { { if !(tom_TestList_is_empty_conc_L(tomMatch2_end_63)) { var tom___X2  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch2_end_63); if tom_TestList_is_fun_sym_l(any(tomMatch2_51).( aterm.ATerm )) { var tomMatch2_57  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_51); if tom_TestList_is_fun_sym_conc(any(tomMatch2_57).( *aterm.ATermList )) { var tomMatch2_end_69  *aterm.ATermList  = tomMatch2_57; for { { if !(tom_TestList_is_empty_conc_L(tomMatch2_end_69)) { 
 if tom___X2==tom_TestList_get_tail_conc_L(tomMatch2_end_69) && !(tom___X2).IsEmpty() {
                  return factory.Parse("pattern4") };  }; if tom_TestList_is_empty_conc_L(tomMatch2_end_69) { tomMatch2_end_69 = tomMatch2_57;  } else { tomMatch2_end_69 = tom_TestList_get_tail_conc_L(tomMatch2_end_69);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_69, tomMatch2_57))) { break; }; };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch2_end_63) { tomMatch2_end_63 = tomMatch2_54;  } else { tomMatch2_end_63 = tom_TestList_get_tail_conc_L(tomMatch2_end_63);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_63, tomMatch2_54))) { break; }; };  };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_l(any(t).( aterm.ATerm )) { var tomMatch2_73  *aterm.ATermList  = tom_TestList_get_slot_l_sl(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_conc(any(tomMatch2_73).( *aterm.ATermList )) { var tomMatch2_end_79  *aterm.ATermList  = tomMatch2_73; for { { var tomMatch2_end_82  *aterm.ATermList  = tomMatch2_end_79; for { { var tomMatch2_end_85  *aterm.ATermList  = tomMatch2_end_82; for { { if !(tom_TestList_is_empty_conc_L(tomMatch2_end_85)) { var tomMatch2_89  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch2_end_85); if tom_TestList_is_fun_sym_l(any(tomMatch2_89).( aterm.ATerm )) { var tomMatch2_88  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch2_89); if tom_TestList_is_fun_sym_conc(any(tomMatch2_88).( *aterm.ATermList )) { 
 if tom_TestList_get_slice_conc(tomMatch2_end_79,tomMatch2_end_82,tom_TestList_empty_list_conc())==tomMatch2_88{ return factory.Parse("pattern5") };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch2_end_85) { tomMatch2_end_85 = tomMatch2_end_82;  } else { tomMatch2_end_85 = tom_TestList_get_tail_conc_L(tomMatch2_end_85);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_85, tomMatch2_end_82))) { break; }; }; if tom_TestList_is_empty_conc_L(tomMatch2_end_82) { tomMatch2_end_82 = tomMatch2_end_79;  } else { tomMatch2_end_82 = tom_TestList_get_tail_conc_L(tomMatch2_end_82);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_82, tomMatch2_end_79))) { break; }; }; if tom_TestList_is_empty_conc_L(tomMatch2_end_79) { tomMatch2_end_79 = tomMatch2_73;  } else { tomMatch2_end_79 = tom_TestList_get_tail_conc_L(tomMatch2_end_79);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch2_end_79, tomMatch2_73))) { break; }; };  };  };  };  };  }; 

  return res
}





func TestMatch3(t *testing.T) {
  for _, d := range match3Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch3 : expected "+answer+" for term "+question +"",
      match3(factory.Parse(question)),
      factory.Parse(answer))
  }
}



func match3Data() [][2]string {
  return [][2]string {
      { "l([f(a)])"                     , "pattern1" },
      { "l([f(a),f(b),f(a),f(c)])"      , "pattern2" },
      { "l([g(f(a)),f(b),g(f(a)),f(c)])", "pattern3" },
  }
}



func match3(t aterm.ATerm) aterm.ATerm {
  res := fail
  ; { { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_l(any(t).( aterm.ATerm )) { var tomMatch3_1  *aterm.ATermList  = tom_TestList_get_slot_l_sl(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_conc(any(tomMatch3_1).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(tomMatch3_1)) { if tom_TestList_is_empty_conc_L(tom_TestList_get_tail_conc_L(tomMatch3_1)) { 
 if tomMatch3_1==factory.Parse("[f(a)]") { return factory.Parse("pattern1") };  };  };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_l(any(t).( aterm.ATerm )) { var tomMatch3_7  *aterm.ATermList  = tom_TestList_get_slot_l_sl(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_conc(any(tomMatch3_7).( *aterm.ATermList )) { var tomMatch3_end_13  *aterm.ATermList  = tomMatch3_7; for { { if !(tom_TestList_is_empty_conc_L(tomMatch3_end_13)) { if tom_TestList_is_fun_sym_f(any(tom_TestList_get_head_conc_L(tomMatch3_end_13)).( aterm.ATerm )) { var tomMatch3_14  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch3_end_13); var tomMatch3_end_17  *aterm.ATermList  = tomMatch3_14; for { { if !(tom_TestList_is_empty_conc_L(tomMatch3_end_17)) { if tom_TestList_is_fun_sym_f(any(tom_TestList_get_head_conc_L(tomMatch3_end_17)).( aterm.ATerm )) { 
 if tom_TestList_get_head_conc_L(tomMatch3_end_13)==tom_TestList_get_head_conc_L(tomMatch3_end_17){ return factory.Parse("pattern2") };  };  }; if tom_TestList_is_empty_conc_L(tomMatch3_end_17) { tomMatch3_end_17 = tomMatch3_14;  } else { tomMatch3_end_17 = tom_TestList_get_tail_conc_L(tomMatch3_end_17);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch3_end_17, tomMatch3_14))) { break; }; };  };  }; if tom_TestList_is_empty_conc_L(tomMatch3_end_13) { tomMatch3_end_13 = tomMatch3_7;  } else { tomMatch3_end_13 = tom_TestList_get_tail_conc_L(tomMatch3_end_13);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch3_end_13, tomMatch3_7))) { break; }; };  };  };  };  }; { if tom_TestList_is_sort_E(t) { if tom_TestList_is_fun_sym_l(any(t).( aterm.ATerm )) { var tomMatch3_27  *aterm.ATermList  = tom_TestList_get_slot_l_sl(any(t).( aterm.ATerm )); if tom_TestList_is_fun_sym_conc(any(tomMatch3_27).( *aterm.ATermList )) { var tomMatch3_end_33  *aterm.ATermList  = tomMatch3_27; for { { if !(tom_TestList_is_empty_conc_L(tomMatch3_end_33)) { var tomMatch3_41  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch3_end_33); if tom_TestList_is_fun_sym_g(any(tomMatch3_41).( aterm.ATerm )) { var tomMatch3_40  aterm.ATerm  = tom_TestList_get_slot_g_s1(tomMatch3_41); if tom_TestList_is_fun_sym_f(any(tomMatch3_40).( aterm.ATerm )) { var tomMatch3_34  *aterm.ATermList  = tom_TestList_get_tail_conc_L(tomMatch3_end_33); var tomMatch3_end_37  *aterm.ATermList  = tomMatch3_34; for { { if !(tom_TestList_is_empty_conc_L(tomMatch3_end_37)) { var tomMatch3_47  aterm.ATerm  = tom_TestList_get_head_conc_L(tomMatch3_end_37); if tom_TestList_is_fun_sym_g(any(tomMatch3_47).( aterm.ATerm )) { var tomMatch3_46  aterm.ATerm  = tom_TestList_get_slot_g_s1(tomMatch3_47); if tom_TestList_is_fun_sym_f(any(tomMatch3_46).( aterm.ATerm )) { 
 if tomMatch3_40==tomMatch3_46{ return factory.Parse("pattern3") };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch3_end_37) { tomMatch3_end_37 = tomMatch3_34;  } else { tomMatch3_end_37 = tom_TestList_get_tail_conc_L(tomMatch3_end_37);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch3_end_37, tomMatch3_34))) { break; }; };  };  };  }; if tom_TestList_is_empty_conc_L(tomMatch3_end_33) { tomMatch3_end_33 = tomMatch3_27;  } else { tomMatch3_end_33 = tom_TestList_get_tail_conc_L(tomMatch3_end_33);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch3_end_33, tomMatch3_27))) { break; }; };  };  };  };  };  }; 

  return res
}





func TestMatch4(t *testing.T) {
  nbSol := 0
  l := factory.Parse("[a,b]").(*aterm.ATermList)
    ; { { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { var tomMatch4_end_4  *aterm.ATermList  = any(l).( *aterm.ATermList ); for { { var tomMatch4_end_7  *aterm.ATermList  = tomMatch4_end_4; for { { 

      nbSol++
      
    ; if tom_TestList_is_empty_conc_L(tomMatch4_end_7) { tomMatch4_end_7 = tomMatch4_end_4;  } else { tomMatch4_end_7 = tom_TestList_get_tail_conc_L(tomMatch4_end_7);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch4_end_7, tomMatch4_end_4))) { break; }; }; if tom_TestList_is_empty_conc_L(tomMatch4_end_4) { tomMatch4_end_4 = any(l).( *aterm.ATermList );  } else { tomMatch4_end_4 = tom_TestList_get_tail_conc_L(tomMatch4_end_4);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch4_end_4, any(l).( *aterm.ATermList )))) { break; }; };  };  };  };  }; 

  assertTrue(t, "TestMatch4",nbSol==6)
}



func TestMatch5(t *testing.T) {
  nbSol := 0
  l := factory.Parse("[l([a,b]),a,b]").(*aterm.ATermList)
  ; { { if tom_TestList_is_sort_L(l) { if tom_TestList_is_fun_sym_conc(any(l).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(any(l).( *aterm.ATermList ))) { var tomMatch5_9  aterm.ATerm  = tom_TestList_get_head_conc_L(any(l).( *aterm.ATermList )); if tom_TestList_is_fun_sym_l(any(tomMatch5_9).( aterm.ATerm )) { var tomMatch5_8  *aterm.ATermList  = tom_TestList_get_slot_l_sl(tomMatch5_9); if tom_TestList_is_fun_sym_conc(any(tomMatch5_8).( *aterm.ATermList )) { var tomMatch5_end_14  *aterm.ATermList  = tomMatch5_8; for { { var tomMatch5_2  *aterm.ATermList  = tom_TestList_get_tail_conc_L(any(l).( *aterm.ATermList )); var tomMatch5_end_5  *aterm.ATermList  = tomMatch5_2; for { { if !(tom_TestList_is_empty_conc_L(tomMatch5_end_5)) { 

      nbSol++
      
    ;  }; if tom_TestList_is_empty_conc_L(tomMatch5_end_5) { tomMatch5_end_5 = tomMatch5_2;  } else { tomMatch5_end_5 = tom_TestList_get_tail_conc_L(tomMatch5_end_5);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch5_end_5, tomMatch5_2))) { break; }; }; if tom_TestList_is_empty_conc_L(tomMatch5_end_14) { tomMatch5_end_14 = tomMatch5_8;  } else { tomMatch5_end_14 = tom_TestList_get_tail_conc_L(tomMatch5_end_14);  };  };  if !(!(tom_TestList_equal_term_L(tomMatch5_end_14, tomMatch5_8))) { break; }; };  };  };  };  };  };  };  }; 

  assertTrue(t, "TestMatch5",nbSol==6)
}





func TestMatch6(t *testing.T) {
  for _, d := range match6Data() {
    question, answer := d[0], d[1]
    assertSame(t,
      "TestMatch6 : expected "+answer+" for term "+question +"",
      match6(factory.Parse(question)),
      factory.Parse(answer))
  }
}



func match6Data() [][2]string {
  return [][2]string {
      { "[]"                         , "fail" },
      { "[f(a)]"                     , "ok"   },
      { "[f(a),f(b),f(a),f(c)]"      , "ok"   },
      { "[g(f(a)),f(b),g(f(a)),f(c)]", "ok"   },
  }
}



func match6(l aterm.ATerm) aterm.ATerm {
  ll := l.(*aterm.ATermList)
  ; { { if tom_TestList_is_sort_L(ll) { if tom_TestList_is_fun_sym_conc(any(ll).( *aterm.ATermList )) { if !(tom_TestList_is_empty_conc_L(any(ll).( *aterm.ATermList ))) { 

      return factory.Parse("ok")
    ;  };  };  };  };  }; 

  return factory.Parse("fail")
}




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Error(message)
  }
}



func assertSame(t *testing.T, message string, expected, actual aterm.ATerm) {
  t.Helper()
  if expected != actual {
    t.Errorf("%s expected same:<%v> was not:<%v>", message, expected, actual)
  }
}
