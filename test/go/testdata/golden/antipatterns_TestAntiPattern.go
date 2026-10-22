package antipatterns












import (
  "testing"
)

  func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_equal_term_Result(t1  Result , t2  Result ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Result(t any) bool { return func() bool { _, ok := (t).(Result); return ok }();  }; func tom_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_is_fun_sym_b(t  Term ) bool { return func() bool { _, ok := (t).(*BTerm); return ok }();  }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_c()  Term  { return (MakeC()) }; func tom_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_make_f(x1  Term , x2  Term )  Term  { return (MakeF(x1,x2)) }; func tom_get_slot_f_x1(t  Term )  Term  { return (t).(*FTerm).X1;  }; func tom_get_slot_f_x2(t  Term )  Term  { return (t).(*FTerm).X2;  }; func tom_is_fun_sym_g(t  Term ) bool { return func() bool { _, ok := (t).(*GTerm); return ok }();  }; func tom_make_g(pred  Term )  Term  { return (MakeG(pred)) }; func tom_get_slot_g_pred(t  Term )  Term  { return (t).(*GTerm).Pred;  }; func tom_is_fun_sym_ff(t  Term ) bool { return func() bool { _, ok := (t).(*FfTerm); return ok }();  }; func tom_make_ff(x1  Term , x2  Term )  Term  { return (MakeFf(x1,x2)) }; func tom_get_slot_ff_x1(t  Term )  Term  { return (t).(*FfTerm).X1;  }; func tom_get_slot_ff_x2(t  Term )  Term  { return (t).(*FfTerm).X2;  }; func tom_is_fun_sym_i(t  Term ) bool { return func() bool { _, ok := (t).(*ITerm); return ok }();  }; func tom_make_i(val  int64 )  Term  { return (MakeI(val)) }; func tom_get_slot_i_val(t  Term )  int64  { return (t).(*ITerm).Val;  }; func tom_make_j(val  int64 )  Term  { return (MakeJ(val)) }; func tom_make_True()  Result  { return (MakeTrue()) }; func tom_make_False()  Result  { return (MakeFalse()) }; func tom_is_fun_sym_And(t  Result ) bool { return func() bool { _, ok := (t).(*AndResult); return ok }();  }; func tom_empty_list_And()  Result  { return (EmptyAnd()) }; func tom_cons_list_And(e  Result , l  Result )  Result  { return (ConsAnd(e,l)) }; func tom_get_head_And_Result(l  Result )  Result  { return (l).GetHeadAnd();  }; func tom_get_tail_And_Result(l  Result )  Result  { return (l).GetTailAnd();  }; func tom_is_empty_And_Result(l  Result ) bool { return (l).IsEmptyAnd();  }; func tom_append_list_And(l1  Result , l2  Result )  Result  { if tom_is_empty_And_Result(l1) { return l2 } else if tom_is_empty_And_Result(l2) { return l1 } else if tom_is_fun_sym_And(l1) { if tom_is_empty_And_Result(tom_get_tail_And_Result(l1)) { return tom_cons_list_And(tom_get_head_And_Result(l1), l2) } else { return tom_cons_list_And(tom_get_head_And_Result(l1), tom_append_list_And(tom_get_tail_And_Result(l1), l2)) } } else { return tom_cons_list_And(l1, l2) }; }; func tom_get_slice_And(begin  Result , end  Result , tail  Result )  Result  { if tom_equal_term_Result(begin, end) { return tail } else if tom_equal_term_Result(end, tail) && (tom_is_empty_And_Result(end) || tom_equal_term_Result(end, tom_empty_list_And())) { return begin }; return tom_cons_list_And(func()  Result  { if tom_is_fun_sym_And(begin) { return tom_get_head_And_Result(begin) }; return begin }(), tom_get_slice_And(func()  Result  { if tom_is_fun_sym_And(begin) { return tom_get_tail_And_Result(begin) }; return tom_empty_list_And() }(), end, tail)) }; func tom_make_Equal(x1  string , x2  Term )  Result  { return (MakeEqual(x1,x2)) }; 






















func match1(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_a(any(subject).( Term )) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp1(t *testing.T) {
    assertTrue(t, match1(tom_make_a()) == tom_make_True())
    assertTrue(t, match1(tom_make_b()) == tom_make_False())
}



func match2(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch2_3 bool =  false ; _ = tomMatch2_3; if tom_is_fun_sym_a(any(subject).( Term )) { tomMatch2_3 =  true ;  }; if !(tomMatch2_3) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}





func TestAp2(t *testing.T) {
    assertTrue(t, match2(tom_make_a()) == tom_make_False())
    assertTrue(t, match2(tom_make_b()) == tom_make_True())
}



func match3(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_x1(any(subject).( Term ))).( Term )) { var tomMatch3_9 bool =  false ; _ = tomMatch3_9; if tom_is_fun_sym_b(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch3_9 =  true ;  }; if !(tomMatch3_9) { 

        return tom_make_True() };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp3(t *testing.T) {
    assertTrue(t, match3(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_True())
    assertTrue(t, match3(tom_make_f(tom_make_a(),tom_make_c())) == tom_make_True())
    assertTrue(t, match3(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
    assertTrue(t, match3(tom_make_f(tom_make_b(),tom_make_c())) == tom_make_False())
}



func match3_1(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch4_10 bool =  false ; _ = tomMatch4_10; if tom_is_fun_sym_a(any(tom_get_slot_f_x1(any(subject).( Term ))).( Term )) { tomMatch4_10 =  true ;  }; if !(tomMatch4_10) { var tomMatch4_9 bool =  false ; _ = tomMatch4_9; if tom_is_fun_sym_b(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch4_9 =  true ;  }; if !(tomMatch4_9) { 

        return tom_make_True() };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp3_1(t *testing.T) {
    assertTrue(t, match3_1(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match3_1(tom_make_f(tom_make_a(),tom_make_c())) == tom_make_False())
    assertTrue(t, match3_1(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
    assertTrue(t, match3_1(tom_make_f(tom_make_b(),tom_make_c())) == tom_make_True())
    assertTrue(t, match3_1(tom_make_f(tom_make_b(),tom_make_b())) == tom_make_False())
}



func match3_2(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch5_3 bool =  false ; _ = tomMatch5_3; if tom_is_fun_sym_f(any(subject).( Term )) { tomMatch5_3 =  true ;  }; if !(tomMatch5_3) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp3_2(t *testing.T) {
    assertTrue(t, match3_2(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match3_2(tom_make_a()) == tom_make_True())
}



func match3_3(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch6_5 bool =  false ; _ = tomMatch6_5; if tom_is_fun_sym_f(any(subject).( Term )) { tomMatch6_5 =  true ;  }; if !(tomMatch6_5) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp3_3(t *testing.T) {
    assertTrue(t, match3_3(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match3_3(tom_make_a()) == tom_make_True())
}



func match3_4(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch7_10 bool =  false ; _ = tomMatch7_10; var tomMatch7_12 bool =  false ; _ = tomMatch7_12; var tomMatch7_1  Term  =  *new(Term) ; _ = tomMatch7_1; var tomMatch7_5  Term  =  *new(Term) ; _ = tomMatch7_5; var tomMatch7_4  Term  =  *new(Term) ; _ = tomMatch7_4; var tomMatch7_2  Term  =  *new(Term) ; _ = tomMatch7_2; if tom_is_fun_sym_f(any(subject).( Term )) { { tomMatch7_12 =  true ; tomMatch7_4 = any(subject).( Term ); tomMatch7_1 = tom_get_slot_f_x1(tomMatch7_4); tomMatch7_2 = tom_get_slot_f_x2(tomMatch7_4);  };  } else { if tom_is_fun_sym_ff(any(subject).( Term )) { { tomMatch7_12 =  true ; tomMatch7_5 = any(subject).( Term ); tomMatch7_1 = tom_get_slot_ff_x1(tomMatch7_5); tomMatch7_2 = tom_get_slot_ff_x2(tomMatch7_5);  };  };  }; if tomMatch7_12 { if tom_is_fun_sym_a(any(tomMatch7_1).( Term )) { var tomMatch7_11 bool =  false ; _ = tomMatch7_11; if tom_is_fun_sym_b(any(tomMatch7_2).( Term )) { tomMatch7_11 =  true ;  }; if !(tomMatch7_11) { tomMatch7_10 =  true ;  };  };  }; if !(tomMatch7_10) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp3_4(t *testing.T) {
    assertTrue(t, match3_4(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match3_4(tom_make_ff(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match3_4(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_True())
    assertTrue(t, match3_4(tom_make_ff(tom_make_a(),tom_make_b())) == tom_make_True())
    assertTrue(t, match3_4(tom_make_f(tom_make_b(),tom_make_b())) == tom_make_True())
    assertTrue(t, match3_4(tom_make_ff(tom_make_b(),tom_make_b())) == tom_make_True())
    assertTrue(t, match3_4(tom_make_a()) == tom_make_True())
}





func match4(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch8_9 bool =  false ; _ = tomMatch8_9; if tom_is_fun_sym_f(any(subject).( Term )) { if tom_is_fun_sym_a(any(tom_get_slot_f_x1(any(subject).( Term ))).( Term )) { var tomMatch8_10 bool =  false ; _ = tomMatch8_10; if tom_is_fun_sym_b(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch8_10 =  true ;  }; if !(tomMatch8_10) { tomMatch8_9 =  true ;  };  };  }; if !(tomMatch8_9) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp4(t *testing.T) {
    assertTrue(t, match4(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match4(tom_make_f(tom_make_a(),tom_make_c())) == tom_make_False())
    assertTrue(t, match4(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_True())
    assertTrue(t, match4(tom_make_f(tom_make_b(),tom_make_c())) == tom_make_True())
    assertTrue(t, match4(tom_make_g(tom_make_b())) == tom_make_True())
    assertTrue(t, match4(tom_make_f(tom_make_b(),tom_make_b())) == tom_make_True())
}



func match5(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tom___x  Term  = tom_get_slot_f_x1(any(subject).( Term )); _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_f_x2(any(subject).( Term ))) { 

        return tom_make_Equal("x",tom___x) };  };  };  };  }; 


    return tom_make_False()
}



func TestAp5(t *testing.T) {
    assertTrue(t, match5(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_Equal("x",tom_make_a()))
    assertTrue(t, match5(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
}



func match7(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch10_6 bool =  false ; _ = tomMatch10_6; if tom_is_fun_sym_f(any(subject).( Term )) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_slot_f_x2(any(subject).( Term ))) { tomMatch10_6 =  true ;  };  }; if !(tomMatch10_6) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp7(t *testing.T) {
    assertTrue(t, match7(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match7(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_True())
    assertTrue(t, match7(tom_make_g(tom_make_a())) == tom_make_True())
}





func match8(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch11_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch11_2; var tom___x  Term  = tom_get_slot_f_x1(any(subject).( Term )); _ = tom___x; var tomMatch11_9 bool =  false ; _ = tomMatch11_9; if tom_is_fun_sym_g(any(tomMatch11_2).( Term )) { if tom_equal_term_Term(tom___x, tom_get_slot_g_pred(tomMatch11_2)) { tomMatch11_9 =  true ;  };  }; if !(tomMatch11_9) { 

        return tom_make_Equal("x",tom___x) };  };  };  };  }; 


    return tom_make_False()
}



func TestAp8(t *testing.T) {
    assertTrue(t, match8(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_Equal("x",tom_make_a()))
    assertTrue(t, match8(tom_make_f(tom_make_a(),tom_make_g(tom_make_b()))) == tom_make_Equal("x",tom_make_a()))
    assertTrue(t, match8(tom_make_f(tom_make_b(),tom_make_g(tom_make_b()))) == tom_make_False())
    assertTrue(t, match8(tom_make_g(tom_make_b())) == tom_make_False())
}



func match8_1(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch12_1  Term  = tom_get_slot_f_x1(any(subject).( Term )); _ = tomMatch12_1; var tom___x  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tom___x; var tomMatch12_9 bool =  false ; _ = tomMatch12_9; if tom_is_fun_sym_g(any(tomMatch12_1).( Term )) { if tom_equal_term_Term(tom___x, tom_get_slot_g_pred(tomMatch12_1)) { tomMatch12_9 =  true ;  };  }; if !(tomMatch12_9) { 

        return tom_make_Equal("x",tom___x) };  };  };  };  }; 


    return tom_make_False()
}



func TestAp8_1(t *testing.T) {
    assertTrue(t, match8_1(tom_make_f(tom_make_b(),tom_make_a())) == tom_make_Equal("x",tom_make_a()))
    assertTrue(t, match8_1(tom_make_f(tom_make_g(tom_make_b()),tom_make_a())) == tom_make_Equal("x",tom_make_a()))
    assertTrue(t, match8_1(tom_make_f(tom_make_g(tom_make_b()),tom_make_b())) == tom_make_False())
    assertTrue(t, match8_1(tom_make_g(tom_make_b())) == tom_make_False())
}



func match9(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch13_9 bool =  false ; _ = tomMatch13_9; if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch13_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch13_2; var tomMatch13_10 bool =  false ; _ = tomMatch13_10; if tom_is_fun_sym_g(any(tomMatch13_2).( Term )) { if tom_equal_term_Term(tom_get_slot_f_x1(any(subject).( Term )), tom_get_slot_g_pred(tomMatch13_2)) { tomMatch13_10 =  true ;  };  }; if !(tomMatch13_10) { tomMatch13_9 =  true ;  };  }; if !(tomMatch13_9) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp9(t *testing.T) {
    assertTrue(t, match9(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
    assertTrue(t, match9(tom_make_f(tom_make_a(),tom_make_g(tom_make_b()))) == tom_make_False())
    assertTrue(t, match9(tom_make_f(tom_make_b(),tom_make_g(tom_make_b()))) == tom_make_True())
    assertTrue(t, match9(tom_make_g(tom_make_b())) == tom_make_True())
}



func match9_1(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch14_8 bool =  false ; _ = tomMatch14_8; if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch14_9 bool =  false ; _ = tomMatch14_9; if tom_is_fun_sym_g(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch14_9 =  true ;  }; if !(tomMatch14_9) { tomMatch14_8 =  true ;  };  }; if !(tomMatch14_8) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp9_1(t *testing.T) {
    assertTrue(t, match9_1(tom_make_f(tom_make_b(),tom_make_g(tom_make_b()))) == tom_make_True())
    assertTrue(t, match9_1(tom_make_f(tom_make_a(),tom_make_g(tom_make_b()))) == tom_make_True())
    assertTrue(t, match9_1(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
}



func match10(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tom___x  Term  = tom_get_slot_f_x1(any(subject).( Term )); _ = tom___x; var tomMatch15_6 bool =  false ; _ = tomMatch15_6; if tom_equal_term_Term(tom___x, tom_get_slot_f_x2(any(subject).( Term ))) { tomMatch15_6 =  true ;  }; if !(tomMatch15_6) { 

        return tom_make_Equal("x",tom___x) };  };  };  };  }; 


    return tom_make_False()
}



func TestAp10(t *testing.T) {
    assertTrue(t, match10(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match10(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_Equal("x",tom_make_a()))
}



func match11(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch16_12 bool =  false ; _ = tomMatch16_12; if tom_is_fun_sym_g(any(tom_get_slot_f_x1(any(subject).( Term ))).( Term )) { tomMatch16_12 =  true ;  }; if !(tomMatch16_12) { var tomMatch16_11 bool =  false ; _ = tomMatch16_11; if tom_is_fun_sym_g(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch16_11 =  true ;  }; if !(tomMatch16_11) { 

        return tom_make_True() };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp11(t *testing.T) {
    assertTrue(t, match11(tom_make_f(tom_make_g(tom_make_a()),tom_make_g(tom_make_b()))) == tom_make_False())
}



func match12(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { var tomMatch17_11 bool =  false ; _ = tomMatch17_11; if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch17_13 bool =  false ; _ = tomMatch17_13; if tom_is_fun_sym_g(any(tom_get_slot_f_x1(any(subject).( Term ))).( Term )) { tomMatch17_13 =  true ;  }; if !(tomMatch17_13) { var tomMatch17_12 bool =  false ; _ = tomMatch17_12; if tom_is_fun_sym_g(any(tom_get_slot_f_x2(any(subject).( Term ))).( Term )) { tomMatch17_12 =  true ;  }; if !(tomMatch17_12) { tomMatch17_11 =  true ;  };  };  }; if !(tomMatch17_11) { 

        return tom_make_True() };  };  };  }; 


    return tom_make_False()
}



func TestAp12(t *testing.T) {
    assertTrue(t, match12(tom_make_f(tom_make_g(tom_make_a()),tom_make_g(tom_make_b()))) == tom_make_True())
}



func match13(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch18_6 bool =  false ; _ = tomMatch18_6; tomMatch18_6 =  true ; if !(tomMatch18_6) { var tomMatch18_5 bool =  false ; _ = tomMatch18_5; tomMatch18_5 =  true ; if !(tomMatch18_5) { 

        return tom_make_True() };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp13(t *testing.T) {
    assertTrue(t, match13(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match13(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
}



func match14(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { 

        return tom_make_True() };  };  }; 


    return tom_make_False()
}



func match15(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch20_5 bool =  false ; _ = tomMatch20_5; tomMatch20_5 =  true ; if !(tomMatch20_5) { 

        return tom_make_True() };  };  };  };  }; 


    return tom_make_False()
}



func TestAp15(t *testing.T) {
    assertTrue(t, match15(tom_make_f(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match15(tom_make_f(tom_make_a(),tom_make_b())) == tom_make_False())
    assertTrue(t, match15(tom_make_b()) == tom_make_False())
}



func match16(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_f(any(subject).( Term )) { var tomMatch21_2  Term  = tom_get_slot_f_x2(any(subject).( Term )); _ = tomMatch21_2; var tom___x  Term  = tom_get_slot_f_x1(any(subject).( Term )); _ = tom___x; if tom_is_fun_sym_f(any(tomMatch21_2).( Term )) { var tomMatch21_6  Term  = tom_get_slot_f_x2(tomMatch21_2); _ = tomMatch21_6; if tom_is_fun_sym_a(any(tom_get_slot_f_x1(tomMatch21_2)).( Term )) { if tom_is_fun_sym_f(any(tomMatch21_6).( Term )) { var tomMatch21_12  Term  = tom_get_slot_f_x2(tomMatch21_6); _ = tomMatch21_12; var tomMatch21_19 bool =  false ; _ = tomMatch21_19; if tom_is_fun_sym_g(any(tomMatch21_12).( Term )) { if tom_equal_term_Term(tom___x, tom_get_slot_g_pred(tomMatch21_12)) { tomMatch21_19 =  true ;  };  }; if !(tomMatch21_19) { 

        return tom_cons_list_And(tom_make_Equal("x",tom___x),tom_cons_list_And(tom_make_Equal("y",tom_get_slot_f_x1(tomMatch21_6)),tom_empty_list_And())) };  };  };  };  };  };  };  }; 


    return tom_make_False()
}





func TestAp16(t *testing.T) {
    assertTrue(t, match16(tom_make_f(tom_make_b(),tom_make_f(tom_make_a(),tom_make_f(tom_make_a(),tom_make_g(tom_make_b()))))) == tom_make_False())
    assertTrue(t, match16(tom_make_f(tom_make_b(),tom_make_f(tom_make_a(),tom_make_f(tom_make_a(),tom_make_g(tom_make_a()))))) == tom_cons_list_And(tom_make_Equal("x",tom_make_b()),tom_cons_list_And(tom_make_Equal("y",tom_make_a()),tom_empty_list_And())))
    assertTrue(t, match16(tom_make_f(tom_make_a(),tom_make_f(tom_make_a(),tom_make_f(tom_make_b(),tom_make_b())))) == tom_cons_list_And(tom_make_Equal("x",tom_make_a()),tom_cons_list_And(tom_make_Equal("y",tom_make_b()),tom_empty_list_And())))
    assertTrue(t, match16(tom_make_f(tom_make_b(),tom_make_f(tom_make_a(),tom_make_b()))) == tom_make_False())
}



func match17(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_i(any(subject).( Term )) { var tomMatch22_6 bool =  false ; _ = tomMatch22_6; if tom_equal_term_int(1, tom_get_slot_i_val(any(subject).( Term ))) { tomMatch22_6 =  true ;  }; if !(tomMatch22_6) { 

        return tom_make_True() };  };  };  };  }; 


    return tom_make_False()
}



func TestAp17(t *testing.T) {
    assertTrue(t, match17(tom_make_i(1)) == tom_make_False())
    assertTrue(t, match17(tom_make_i(2)) == tom_make_True())
}



func match18(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_i(any(subject).( Term )) { var tomMatch23_1  int64  = tom_get_slot_i_val(any(subject).( Term )); _ = tomMatch23_1; var tom___x  int64  = tomMatch23_1; _ = tom___x; var tomMatch23_7 bool =  false ; _ = tomMatch23_7; if tom_equal_term_int(1, tomMatch23_1) { if tom_equal_term_int(tom___x, tomMatch23_1) { tomMatch23_7 =  true ;  };  }; if !(tomMatch23_7) { 

        return tom_make_Equal("j(x)",tom_make_j(tom___x)) };  };  };  };  }; 


    return tom_make_False()
}



func TestAp18(t *testing.T) {
    assertTrue(t, match18(tom_make_i(1)) == tom_make_False())
    assertTrue(t, match18(tom_make_i(2)) == tom_make_Equal("j(x)",tom_make_j(2)))
}



func match19(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_ff(any(subject).( Term )) { var tomMatch24_1  Term  = tom_get_slot_ff_x1(any(subject).( Term )); _ = tomMatch24_1; var tomMatch24_2  Term  = tom_get_slot_ff_x2(any(subject).( Term )); _ = tomMatch24_2; if tom_is_fun_sym_i(any(tomMatch24_1).( Term )) { var tomMatch24_5  int64  = tom_get_slot_i_val(tomMatch24_1); _ = tomMatch24_5; var tom___x  int64  = tomMatch24_5; _ = tom___x; if tom_is_fun_sym_i(any(tomMatch24_2).( Term )) { if tom_equal_term_int(tom___x, tom_get_slot_i_val(tomMatch24_2)) { var tomMatch24_15 bool =  false ; _ = tomMatch24_15; if tom_equal_term_int(1, tomMatch24_5) { if tom_equal_term_int(tom___x, tomMatch24_5) { tomMatch24_15 =  true ;  };  }; if !(tomMatch24_15) { 

        return tom_make_Equal("j(x)",tom_make_j(tom___x)) };  };  };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp19(t *testing.T) {
    assertTrue(t, match19(tom_make_ff(tom_make_i(1),tom_make_i(2))) == tom_make_False())
    assertTrue(t, match19(tom_make_ff(tom_make_i(2),tom_make_i(2))) == tom_make_Equal("j(x)",tom_make_j(2)))
}



func match20(subject Term) Result {
    ; { { if tom_is_sort_Term(subject) { if tom_is_fun_sym_ff(any(subject).( Term )) { var tomMatch25_1  Term  = tom_get_slot_ff_x1(any(subject).( Term )); _ = tomMatch25_1; var tom___x  Term  = tomMatch25_1; _ = tom___x; if tom_equal_term_Term(tom___x, tom_get_slot_ff_x2(any(subject).( Term ))) { var tomMatch25_9 bool =  false ; _ = tomMatch25_9; if tom_is_fun_sym_a(any(tomMatch25_1).( Term )) { if tom_equal_term_Term(tom___x, tomMatch25_1) { tomMatch25_9 =  true ;  };  }; if !(tomMatch25_9) { 

        return tom_make_Equal("x",tom___x) };  };  };  };  };  }; 


    return tom_make_False()
}



func TestAp20(t *testing.T) {
    assertTrue(t, match20(tom_make_ff(tom_make_a(),tom_make_a())) == tom_make_False())
    assertTrue(t, match20(tom_make_ff(tom_make_a(),tom_make_b())) == tom_make_False())
    assertTrue(t, match20(tom_make_ff(tom_make_b(),tom_make_b())) == tom_make_Equal("x",tom_make_b()))
}


func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}
