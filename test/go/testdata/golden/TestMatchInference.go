





package testmatchinference



import "testing"
  
func tom_TestMatchInference_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_TestMatchInference_is_sort_char(t rune) bool { return true;  }; func tom_TestMatchInference_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_TestMatchInference_is_sort_String(t string) bool { return true;  }; func tom_TestMatchInference_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_TestMatchInference_is_sort_int(t int64) bool { return true;  }; func tom_TestMatchInference_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestMatchInference_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestMatchInference_is_fun_sym_a(t  Term ) bool { return func() bool { _, ok := (t).(*ATerm); return ok }();  }; func tom_TestMatchInference_make_a()  Term  { return (MakeA()) }; func tom_TestMatchInference_is_fun_sym_f(t  Term ) bool { return func() bool { _, ok := (t).(*FTerm); return ok }();  }; func tom_TestMatchInference_make_f(s  Term )  Term  { return (MakeF(s)) }; func tom_TestMatchInference_get_slot_f_s(t  Term )  Term  { return (t).(*FTerm).S;  }; 








func Test1(t *testing.T) {
    ; { { var tomMatch1_0  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(4, any(tomMatch1_0).( int64 )) { 
 t.Fatal("4 does not match") ;  };  };  }; { var tomMatch1_3  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(5, any(tomMatch1_3).( int64 )) { 
 return ;  };  };  }; { var tomMatch1_6  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(6, any(tomMatch1_6).( int64 )) { 
 t.Fatal("6 does not match") ;  };  };  };  }; 

    t.Fatal("5 should match")
  }



func Test2(t *testing.T) {
    ; { { var tomMatch2_0  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(5, any(tomMatch2_0).( int64 )) { var tomMatch2_1  string  = "foo"; if  true  { if tom_TestMatchInference_equal_term_String("goo", any(tomMatch2_1).( string )) { 
 t.Fatal("5,goo does not match") ;  };  };  };  };  }; { var tomMatch2_6  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(5, any(tomMatch2_6).( int64 )) { var tomMatch2_7  string  = "foo"; if  true  { if tom_TestMatchInference_equal_term_String("bar", any(tomMatch2_7).( string )) { 
 t.Fatal("5,bar does not match") ;  };  };  };  };  }; { var tomMatch2_12  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(5, any(tomMatch2_12).( int64 )) { var tomMatch2_13  string  = "foo"; if  true  { if tom_TestMatchInference_equal_term_String("foo", any(tomMatch2_13).( string )) { 
 return ;  };  };  };  };  }; { var tomMatch2_18  int64  = 5; if  true  { if tom_TestMatchInference_equal_term_int(6, any(tomMatch2_18).( int64 )) { var tomMatch2_19  string  = "foo"; if  true  { if tom_TestMatchInference_equal_term_String("foo", any(tomMatch2_19).( string )) { 
 t.Fatal("6,foo does not match") ;  };  };  };  };  };  }; 

    t.Fatal("5,foo should match")
  }



func ff(x Term) Term { return tom_TestMatchInference_make_f(x) }
func Test3(t *testing.T) {
    ; { { var tomMatch3_0  Term  = tom_TestMatchInference_make_f(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_0) { if tom_TestMatchInference_is_fun_sym_f(any(tomMatch3_0).( Term )) { if tom_TestMatchInference_is_fun_sym_a(any(tom_TestMatchInference_get_slot_f_s(any(tomMatch3_0).( Term ))).( Term )) { var tomMatch3_1  Term  = ff(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_1) { if tom_TestMatchInference_is_fun_sym_a(any(tomMatch3_1).( Term )) { 
 t.Fatal() ;  };  };  };  };  };  }; { var tomMatch3_9  Term  = tom_TestMatchInference_make_f(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_9) { if tom_TestMatchInference_is_fun_sym_a(any(tomMatch3_9).( Term )) { var tomMatch3_10  Term  = ff(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_10) { if tom_TestMatchInference_is_fun_sym_f(any(tomMatch3_10).( Term )) { if tom_TestMatchInference_is_fun_sym_a(any(tom_TestMatchInference_get_slot_f_s(any(tomMatch3_10).( Term ))).( Term )) { 
 t.Fatal() ;  };  };  };  };  };  }; { var tomMatch3_18  Term  = tom_TestMatchInference_make_f(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_18) { if tom_TestMatchInference_is_fun_sym_f(any(tomMatch3_18).( Term )) { if tom_TestMatchInference_is_fun_sym_a(any(tom_TestMatchInference_get_slot_f_s(any(tomMatch3_18).( Term ))).( Term )) { var tomMatch3_19  Term  = ff(tom_TestMatchInference_make_a()); if tom_TestMatchInference_is_sort_Term(tomMatch3_19) { if tom_TestMatchInference_is_fun_sym_f(any(tomMatch3_19).( Term )) { 
 return ;  };  };  };  };  };  };  }; 

    t.Fatal("a,f(a) hould match")
  }
