package gom















import (
  "fmt"
  "testing"
)

  func tom_TestPeanoRule_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestPeanoRule_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestPeanoRule_is_fun_sym_Zero(t  Term ) bool { return func() bool { _, ok := (t).(*ZeroTerm); return ok }();  }; func tom_TestPeanoRule_make_Zero()  Term  { return (MakeZero()) }; func tom_TestPeanoRule_is_fun_sym_Suc(t  Term ) bool { return func() bool { _, ok := (t).(*SucTerm); return ok }();  }; func tom_TestPeanoRule_make_Suc(pred  Term )  Term  { return (MakeSuc(pred)) }; func tom_TestPeanoRule_get_slot_Suc_pred(t  Term )  Term  { return (t).(*SucTerm).Pred;  }; func tom_TestPeanoRule_make_Plus(s1  Term , s2  Term )  Term  { return (MakePlus(s1,s2)) }; func tom_TestPeanoRule_make_Fib1(t  Term )  Term  { return (MakeFib1(t)) }; func tom_TestPeanoRule_make_Fib5(t  Term )  Term  { return (MakeFib5(t)) }; func tom_TestPeanoRule_make_Fib6(t  Term )  Term  { return (MakeFib6(t)) }; func tom_TestPeanoRule_make_NonZ(t  Term )  Term  { return (MakeNonZ(t)) };   func init() {   HookMakePlus = func(arg_1 Term, arg_2 Term) (Term, Term, Term) {     tom__r := func() Term {     {     ; { _ = arg_1; _ = arg_2; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_sort_Term(arg_2) { if tom_TestPeanoRule_is_fun_sym_Zero(any(arg_2).( Term )) {  return any(arg_1).( Term ); ;  };  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_sort_Term(arg_2) { if tom_TestPeanoRule_is_fun_sym_Suc(any(arg_2).( Term )) {  return tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Plus(any(arg_1).( Term ),tom_TestPeanoRule_get_slot_Suc_pred(any(arg_2).( Term )))); ;  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1, arg_2;   }; };    func init() {   HookMakeFib1 = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { _ = arg_1; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Zero(any(arg_1).( Term )) {  return tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()); ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Suc(any(arg_1).( Term )) { if tom_TestPeanoRule_is_fun_sym_Zero(any(tom_TestPeanoRule_get_slot_Suc_pred(any(arg_1).( Term ))).( Term )) {  return any(arg_1).( Term ); ;  };  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Suc(any(arg_1).( Term )) { var tomMatch2_10  Term  = tom_TestPeanoRule_get_slot_Suc_pred(any(arg_1).( Term )); _ = tomMatch2_10; if tom_TestPeanoRule_is_fun_sym_Suc(any(tomMatch2_10).( Term )) { var tom___x  Term  = tom_TestPeanoRule_get_slot_Suc_pred(tomMatch2_10); _ = tom___x;  return tom_TestPeanoRule_make_Plus(tom_TestPeanoRule_make_Fib1(tom___x),tom_TestPeanoRule_make_Fib1(tom_TestPeanoRule_make_Suc(tom___x))); ;  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeFib5 = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { _ = arg_1; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Zero(any(arg_1).( Term )) {  return tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()); ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { var tom___x  Term  = any(arg_1).( Term ); _ = tom___x; if (true == eq(tom___x,tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))) {  return tom___x; ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Suc(any(arg_1).( Term )) { var tomMatch3_5  Term  = tom_TestPeanoRule_get_slot_Suc_pred(any(arg_1).( Term )); _ = tomMatch3_5; if tom_TestPeanoRule_is_fun_sym_Suc(any(tomMatch3_5).( Term )) { var tom___x  Term  = tom_TestPeanoRule_get_slot_Suc_pred(tomMatch3_5); _ = tom___x;  return tom_TestPeanoRule_make_Plus(tom_TestPeanoRule_make_Fib5(tom___x),tom_TestPeanoRule_make_Fib5(tom_TestPeanoRule_make_Suc(tom___x))); ;  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeFib6 = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { _ = arg_1; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Zero(any(arg_1).( Term )) {  return tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()); ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { var tom___x  Term  = any(arg_1).( Term ); _ = tom___x; if tom_TestPeanoRule_equal_term_Term(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()), tom___x) {  return tom___x; ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) { if tom_TestPeanoRule_is_fun_sym_Suc(any(arg_1).( Term )) { var tomMatch4_5  Term  = tom_TestPeanoRule_get_slot_Suc_pred(any(arg_1).( Term )); _ = tomMatch4_5; if tom_TestPeanoRule_is_fun_sym_Suc(any(tomMatch4_5).( Term )) { var tom___x  Term  = tom_TestPeanoRule_get_slot_Suc_pred(tomMatch4_5); _ = tom___x;  return tom_TestPeanoRule_make_Plus(tom_TestPeanoRule_make_Fib6(tom___x),tom_TestPeanoRule_make_Fib6(tom_TestPeanoRule_make_Suc(tom___x))); ;  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeNonZ = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { _ = arg_1; { if tom_TestPeanoRule_is_sort_Term(arg_1) { var tom___x  Term  = any(arg_1).( Term ); _ = tom___x; if !(tom_TestPeanoRule_equal_term_Term(tom___x, tom_TestPeanoRule_make_Zero())) {  return tom___x; ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(arg_1) {  return tom_TestPeanoRule_make_Suc(any(arg_1).( Term )); ;  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };         func eq(o1 any, o2 any) bool {         return o1 == o2;       }       ;  










































func TestPlus(t *testing.T) {
    for i:=0 ; i<100 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing Plus with N ="+fmt.Sprint(N)+": ",
          peano2int(tom_TestPeanoRule_make_Plus(N,N)) == (i+i) )
    }
}



func TestFib1(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing Fib1 with N ="+fmt.Sprint(N)+": ",
          peano2int(tom_TestPeanoRule_make_Fib1(N)) == fibint(i) )
    }
}



func TestFib5(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing fib5 with N ="+fmt.Sprint(N)+": ",
          peano2int(tom_TestPeanoRule_make_Fib5(N)) == fibint(i) )
    }
}



func TestFib5bis(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))))))))))
    assertTrue(t, "Testing fib5 with N =" + fmt.Sprint(peano2int(N)) + ": ",
               peano2int(tom_TestPeanoRule_make_Fib5(N)) == fibint(peano2int(N)))
}



func TestFib5ter(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))))))))))
    assertTrue(t, "Testing fib5 with N =" + fmt.Sprint(peano2int(N))+"1" + ": ",
               peano2int(tom_TestPeanoRule_make_Fib5(tom_TestPeanoRule_make_Suc(N))) == fibint(peano2int(N)+1))
}



func TestFib6(t *testing.T) {
    for i:=0 ; i<15 ; i++ {
      var N Term = int2peano(i)
      assertTrue(t, "Testing fib6 with N ="+fmt.Sprint(N)+": ",
          peano2int(tom_TestPeanoRule_make_Fib6(N)) == fibint(i) )
    }
}



func TestFib6bis(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))))))))))
    assertTrue(t, "Testing fib6 with N =" + fmt.Sprint(peano2int(N)) + ": ",
               peano2int(tom_TestPeanoRule_make_Fib6(N)) == fibint(peano2int(N)))
}



func TestFib6ter(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))))))))))
    assertTrue(t, "Testing fib6 with N =" + fmt.Sprint(peano2int(N))+"1" + ": ",
               peano2int(tom_TestPeanoRule_make_Fib6(tom_TestPeanoRule_make_Suc(N))) == fibint(peano2int(N)+1))
}



func TestNonZ1(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Zero()
    assertEquals(t, "NonZ is not zero", tom_TestPeanoRule_make_NonZ(N), tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))
}



func TestNonZ2(t *testing.T) {
    var N Term = tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero())
    assertEquals(t, "NonZ is id for non Zero", tom_TestPeanoRule_make_NonZ(N), N)
}

  func fibint(n int) int {
    if(n<=1) {
      return 1
    } else {
      return fibint(n-1)+fibint(n-2)
    }
  }



func TestInt2p(t *testing.T) {
    assertEquals(t, "int2peano should return sssss(tzero) for 5",
        int2peano(5),tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero()))))))
}

  func int2peano(n int) Term {
    var N Term = tom_TestPeanoRule_make_Zero()
    for i:=0 ; i<n ; i++ {
      N = tom_TestPeanoRule_make_Suc(N)
    }
    return N
  }



func TestPeano2int(t *testing.T) {
    assertEquals(t, "peano2int should return 5 for sssss(tzero)",
        peano2int(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Suc(tom_TestPeanoRule_make_Zero())))))),5)
}

  func peano2int(N Term) int {
    ; { _ = N; { if tom_TestPeanoRule_is_sort_Term(N) { if tom_TestPeanoRule_is_fun_sym_Zero(any(N).( Term )) { 
 return 0 ;  };  };  }; { if tom_TestPeanoRule_is_sort_Term(N) { if tom_TestPeanoRule_is_fun_sym_Suc(any(N).( Term )) { 
return 1+peano2int(tom_TestPeanoRule_get_slot_Suc_pred(any(N).( Term ))) ;  };  };  };  }; 

    return 0
  }




func assertTrue(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}






func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
