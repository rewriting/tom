package backquote

















import (
  "strconv"
  "testing"
)

  func tom_TestBackquoteGom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_TestBackquoteGom_is_sort_char(t rune) bool { return true;  }; func tom_TestBackquoteGom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_TestBackquoteGom_is_sort_String(t string) bool { return true;  }; func tom_TestBackquoteGom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_TestBackquoteGom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_TestBackquoteGom_make_f(s  string )  T  { return (MakeF(s)) }; 





  
func TestBQ1(t *testing.T) {
    i := 0
    assertEquals(t, tom_TestBackquoteGom_make_f("i="+str(i)), tom_TestBackquoteGom_make_f("i=0") )
    assertEquals(t, tom_TestBackquoteGom_make_f("i="+str(i)), tom_TestBackquoteGom_make_f("i=0") )
    assertEquals(t, tom_TestBackquoteGom_make_f("i="+str(postInc(&i))), tom_TestBackquoteGom_make_f("i=0") )
    assertEquals(t, tom_TestBackquoteGom_make_f("i="+str(preInc(&i))), tom_TestBackquoteGom_make_f("i=2") )
}




func str(i int) string { return strconv.Itoa(i) }




func postInc(i *int) int { v := *i; *i++; return v }




func preInc(i *int) int { *i++; return *i }






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
