package gom












import (
  "testing"
)

  func tom_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_make_a()  Term  { return (MakeA()) }; func tom_make_b()  Term  { return (MakeB()) }; func tom_make_c()  Term  { return (MakeC()) }; func tom_make_f(t  Term )  Term  { return (MakeF(t)) }; func tom_make_g(t1  Term , t2  Term )  Term  { return (MakeG(t1,t2)) }; func tom_make_h(t1  Term , t2  Term , t3  Term )  Term  { return (MakeH(t1,t2,t3)) };   func init() {   HookMakeF = func(arg_1 Term) (Term, Term) {     tom__r := func() Term {     {     ; { { if tom_is_sort_Term(arg_1) { var tom___x  Term  = any(arg_1).( Term ); _ = tom___x; if tom_equal_term_Term(tom_make_a(), tom___x) {  return tom_make_a();  }; if tom_equal_term_Term(tom_make_b(), tom___x) {  return tom_make_a();  };  };  };  };       };     return nil;     }();     return tom__r, arg_1;   }; };    func init() {   HookMakeG = func(arg_1 Term, arg_2 Term) (Term, Term, Term) {     tom__r := func() Term {     {     ; { { if tom_is_sort_Term(arg_1) { if tom_is_sort_Term(arg_2) { if tom_equal_term_Term(tom_make_a(), any(arg_1).( Term )) { if tom_equal_term_Term(tom_make_b(), any(arg_2).( Term )) {  return tom_make_a();  };  };  };  };  }; { if tom_is_sort_Term(arg_1) { if tom_is_sort_Term(arg_2) { var tom___y  Term  = any(arg_2).( Term ); _ = tom___y; if tom_equal_term_Term(tom_make_a(), any(arg_1).( Term )) { if !(tom_equal_term_Term(tom_make_b(), tom___y)) { if !(tom_equal_term_Term(tom_make_c(), tom___y)) {  return tom_make_c();  };  };  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1, arg_2;   }; };    func init() {   HookMakeH = func(arg_1 Term, arg_2 Term, arg_3 Term) (Term, Term, Term, Term) {     tom__r := func() Term {     {     ; { { if tom_is_sort_Term(arg_1) { if tom_is_sort_Term(arg_2) { var tom___y  Term  = any(arg_2).( Term ); _ = tom___y; if tom_is_sort_Term(arg_3) { if tom_equal_term_Term(tom_make_a(), any(arg_1).( Term )) { if !(tom_equal_term_Term(tom_make_b(), tom___y)) {  return tom_make_a();  };  }; if !(tom_equal_term_Term(tom_make_c(), tom___y)) { if tom_equal_term_Term(tom_make_a(), any(arg_3).( Term )) {  return tom_make_a();  };  };  };  };  };  };  };       };     return nil;     }();     return tom__r, arg_1, arg_2, arg_3;   }; };  
















func TestRuleFalseCond(t *testing.T) {
    var test Term = tom_make_g(tom_make_b(),tom_make_b())
    assertEquals(t, test,tom_make_g(tom_make_b(),tom_make_b()))
}



func TestRuleAnd(t *testing.T) {
    var test Term = tom_make_g(tom_make_a(),tom_make_b())
    assertEquals(t, test,tom_make_a())
}



func TestRuleOra(t *testing.T) {
    var test Term = tom_make_f(tom_make_a())
    assertEquals(t, test,tom_make_a())
}



func TestRuleOrb(t *testing.T) {
    var test Term = tom_make_f(tom_make_b())
    assertEquals(t, test,tom_make_a())
}



func TestRuleOrc(t *testing.T) {
    var test Term = tom_make_f(tom_make_c())
    assertEquals(t, test,tom_make_f(tom_make_c()))
}



func TestRuleAndAnd(t *testing.T) {
    var test Term = tom_make_g(tom_make_a(),tom_make_a())
    assertEquals(t, test,tom_make_c())
}



func TestRuleParena(t *testing.T) {
    var test Term = tom_make_h(tom_make_a(),tom_make_a(),tom_make_a())
    assertSame(t, test,tom_make_a())
}
func TestRuleParenb(t *testing.T) {
    var test Term = tom_make_h(tom_make_a(),tom_make_c(),tom_make_c())
    assertSame(t, test,tom_make_a())
}
func TestRuleParenc(t *testing.T) {
    var test Term = tom_make_h(tom_make_b(),tom_make_b(),tom_make_a())
    assertSame(t, test,tom_make_a())
}
func TestRuleParend(t *testing.T) {
    var test Term = tom_make_h(tom_make_b(),tom_make_b(),tom_make_b())
    assertNotSame(t, test,tom_make_a())
}
func TestRuleParene(t *testing.T) {
    var test Term = tom_make_h(tom_make_b(),tom_make_a(),tom_make_c())
    assertNotSame(t, test,tom_make_a())
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func assertSame(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected same:<%v> was not:<%v>", expected, actual)
  }
}




func assertNotSame(t *testing.T, unexpected, actual any) {
  t.Helper()
  if unexpected == actual {
    t.Fatalf("expected not same")
  }
}
