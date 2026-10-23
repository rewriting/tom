package backquote
















import (
  "testing"

  "fixture/term"
)

func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_Term(t1  term.Term , t2  term.Term ) bool { return (t1==t2);  }; func tom_is_sort_Term(t any) bool { return func() bool { _, ok := any(t).(term.Term); return ok }();  }; func tom_make_a()  term.Term  { return (term.MakeA()) }; func tom_make_b()  term.Term  { return (term.MakeB()) }; func tom_make_f(t0  term.Term )  term.Term  { return (term.MakeF(t0)) }; func tom_get_default_f_arg1()  term.Term  { return tom_make_a();  }; func tom_make_g(t0  term.Term , t1  term.Term )  term.Term  { return (term.MakeG(t0, t1)) }; func tom_get_default_g_arg1()  term.Term  { return tom_make_a();  }; func tom_get_default_g_arg2()  term.Term  { return tom_make_b();  }; func tom_make_h(t0  term.Term , t1  term.Term , t2  term.Term )  term.Term  { return (term.MakeH(t0, t1, t2)) }; func tom_get_default_h_arg1()  term.Term  { return tom_make_a();  }; func tom_get_default_h_arg2()  term.Term  { return tom_make_g(tom_get_default_g_arg1(),tom_get_default_g_arg2());  }; func tom_get_default_h_arg3()  term.Term  { return tom_make_f(tom_get_default_f_arg1());  }; 










































































func TestDefault1(t *testing.T) {
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_f(tom_get_default_f_arg1()).Getarg1(), tom_make_a() )
}



func TestDefault2(t *testing.T) {
    assertEquals(t, tom_make_g(tom_make_a(),tom_get_default_g_arg2()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_g(tom_make_a(),tom_get_default_g_arg2()).Getarg2(), tom_make_b() )
    assertEquals(t, tom_make_g(tom_make_a(),tom_get_default_g_arg2()).Getarg2(), tom_make_b() )
    assertEquals(t, tom_make_g(tom_make_a(),tom_get_default_g_arg2()).Getarg2(), tom_make_b() )
    assertEquals(t, tom_make_g(tom_make_a(),tom_get_default_g_arg2()).Getarg2(), tom_make_b() )
}



func TestDefault3(t *testing.T) {
    assertEquals(t, tom_make_h(tom_get_default_h_arg1(),tom_get_default_h_arg2(),tom_get_default_h_arg3()).Getarg1(), tom_make_a() )
    assertEquals(t, tom_make_h(tom_get_default_h_arg1(),tom_get_default_h_arg2(),tom_get_default_h_arg3()).Getarg2(), tom_make_g(tom_get_default_g_arg1(),tom_get_default_g_arg2()) )
    assertEquals(t, tom_make_h(tom_get_default_h_arg1(),tom_get_default_h_arg2(),tom_get_default_h_arg3()).Getarg2(), tom_make_g(tom_make_a(),tom_make_b()) )
    assertEquals(t, tom_make_h(tom_get_default_h_arg1(),tom_get_default_h_arg2(),tom_get_default_h_arg3()).Getarg3(), tom_make_f(tom_get_default_f_arg1()) )
    assertEquals(t, tom_make_h(tom_get_default_h_arg1(),tom_get_default_h_arg2(),tom_get_default_h_arg3()).Getarg3(), tom_make_f(tom_make_a()) )
}



func TestDefault4(t *testing.T) {
    assertEquals(t, tom_make_f(tom_make_g(tom_get_default_g_arg1(),tom_get_default_g_arg2())).Getarg1(), tom_make_g(tom_get_default_g_arg1(),tom_get_default_g_arg2()) )
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
