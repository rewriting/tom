package gom


















import (
  "testing"
)

  func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_T(t1  T , t2  T ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_T(t any) bool { return func() bool { _, ok := (t).(T); return ok }();  }; func tom_equal_term_L(t1  L , t2  L ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_L(t any) bool { return func() bool { _, ok := (t).(L); return ok }();  }; func tom_make_a()  T  { return (MakeA()) }; func tom_make_b()  T  { return (MakeB()) }; func tom_make_c()  T  { return (MakeC()) }; func tom_is_fun_sym_f(t  T ) bool { return func() bool { _, ok := (t).(*FT); return ok }();  }; func tom_empty_list_f()  T  { return (EmptyF()) }; func tom_cons_list_f(e  T , l  T )  T  { return (ConsF(e,l)) }; func tom_get_head_f_T(l  T )  T  { return (l).GetHeadf();  }; func tom_get_tail_f_T(l  T )  T  { return (l).GetTailf();  }; func tom_is_empty_f_T(l  T ) bool { return (l).IsEmptyf();  }; func tom_append_list_f(l1  T , l2  T )  T  { if tom_is_empty_f_T(l1) { return l2 } else if tom_is_empty_f_T(l2) { return l1 } else if tom_is_fun_sym_f(l1) { if tom_is_empty_f_T(tom_get_tail_f_T(l1)) { return tom_cons_list_f(tom_get_head_f_T(l1), l2) } else { return tom_cons_list_f(tom_get_head_f_T(l1), tom_append_list_f(tom_get_tail_f_T(l1), l2)) } } else { return tom_cons_list_f(l1, l2) }; }; func tom_get_slice_f(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_f_T(end) || tom_equal_term_T(end, tom_empty_list_f())) { return begin }; return tom_cons_list_f(func()  T  { if tom_is_fun_sym_f(begin) { return tom_get_head_f_T(begin) }; return begin }(), tom_get_slice_f(func()  T  { if tom_is_fun_sym_f(begin) { return tom_get_tail_f_T(begin) }; return tom_empty_list_f() }(), end, tail)) }; func tom_is_fun_sym_g(t  T ) bool { return func() bool { _, ok := (t).(*GT); return ok }();  }; func tom_empty_list_g()  T  { return (EmptyG()) }; func tom_cons_list_g(e  T , l  T )  T  { return (ConsG(e,l)) }; func tom_get_head_g_T(l  T )  T  { return (l).GetHeadg();  }; func tom_get_tail_g_T(l  T )  T  { return (l).GetTailg();  }; func tom_is_empty_g_T(l  T ) bool { return (l).IsEmptyg();  }; func tom_append_list_g(l1  T , l2  T )  T  { if tom_is_empty_g_T(l1) { return l2 } else if tom_is_empty_g_T(l2) { return l1 } else if tom_is_fun_sym_g(l1) { if tom_is_empty_g_T(tom_get_tail_g_T(l1)) { return tom_cons_list_g(tom_get_head_g_T(l1), l2) } else { return tom_cons_list_g(tom_get_head_g_T(l1), tom_append_list_g(tom_get_tail_g_T(l1), l2)) } } else { return tom_cons_list_g(l1, l2) }; }; func tom_get_slice_g(begin  T , end  T , tail  T )  T  { if tom_equal_term_T(begin, end) { return tail } else if tom_equal_term_T(end, tail) && (tom_is_empty_g_T(end) || tom_equal_term_T(end, tom_empty_list_g())) { return begin }; return tom_cons_list_g(func()  T  { if tom_is_fun_sym_g(begin) { return tom_get_head_g_T(begin) }; return begin }(), tom_get_slice_g(func()  T  { if tom_is_fun_sym_g(begin) { return tom_get_tail_g_T(begin) }; return tom_empty_list_g() }(), end, tail)) }; func tom_is_fun_sym_cons(t  L ) bool { return func() bool { _, ok := (t).(*ConsL); return ok }();  }; func tom_empty_list_cons()  L  { return (EmptyCons()) }; func tom_cons_list_cons(e  T , l  L )  L  { return (ConsCons(e,l)) }; func tom_get_head_cons_L(l  L )  T  { return (l).GetHeadcons();  }; func tom_get_tail_cons_L(l  L )  L  { return (l).GetTailcons();  }; func tom_is_empty_cons_L(l  L ) bool { return (l).IsEmptycons();  }; func tom_append_list_cons(l1  L , l2  L )  L  { if tom_is_empty_cons_L(l1) { return l2 } else if tom_is_empty_cons_L(l2) { return l1 } else if tom_is_empty_cons_L(tom_get_tail_cons_L(l1)) { return tom_cons_list_cons(tom_get_head_cons_L(l1), l2) } else { return tom_cons_list_cons(tom_get_head_cons_L(l1), tom_append_list_cons(tom_get_tail_cons_L(l1), l2)) }; }; func tom_get_slice_cons(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_cons_L(end) || tom_equal_term_L(end, tom_empty_list_cons())) { return begin }; return tom_cons_list_cons(tom_get_head_cons_L(begin), tom_get_slice_cons(tom_get_tail_cons_L(begin), end, tail)) }; func tom_is_fun_sym_consint(t  L ) bool { return func() bool { _, ok := (t).(*ConsintL); return ok }();  }; func tom_empty_list_consint()  L  { return (EmptyConsint()) }; func tom_cons_list_consint(e  int64 , l  L )  L  { return (ConsConsint(e,l)) }; func tom_get_head_consint_L(l  L )  int64  { return (l).GetHeadconsint();  }; func tom_get_tail_consint_L(l  L )  L  { return (l).GetTailconsint();  }; func tom_is_empty_consint_L(l  L ) bool { return (l).IsEmptyconsint();  }; func tom_append_list_consint(l1  L , l2  L )  L  { if tom_is_empty_consint_L(l1) { return l2 } else if tom_is_empty_consint_L(l2) { return l1 } else if tom_is_empty_consint_L(tom_get_tail_consint_L(l1)) { return tom_cons_list_consint(tom_get_head_consint_L(l1), l2) } else { return tom_cons_list_consint(tom_get_head_consint_L(l1), tom_append_list_consint(tom_get_tail_consint_L(l1), l2)) }; }; func tom_get_slice_consint(begin  L , end  L , tail  L )  L  { if tom_equal_term_L(begin, end) { return tail } else if tom_equal_term_L(end, tail) && (tom_is_empty_consint_L(end) || tom_equal_term_L(end, tom_empty_list_consint())) { return begin }; return tom_cons_list_consint(tom_get_head_consint_L(begin), tom_get_slice_consint(tom_get_tail_consint_L(begin), end, tail)) }; 














func contains(col []any, o any) bool {
  for _, x := range col {
    if x == o {
      return true
    }
  }
  return false
}



func Test1(t *testing.T) {
    var l *FT = tom_cons_list_f(tom_make_a(),tom_cons_list_f(tom_make_b(),tom_empty_list_f())).(*FT)
    assertTrue(t, contains(l.ToArray(), tom_make_a()))
    assertTrue(t, contains(l.ToArray(), tom_make_b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==tom_make_a()) { a = true }
      if(o==tom_make_b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)
    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, tom_make_c())
    c := false
    for _, o := range col {
      if(o==tom_make_c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}



func Test2(t *testing.T) {
    var l *ConsL = tom_cons_list_cons(tom_make_a(),tom_cons_list_cons(tom_make_b(),tom_empty_list_cons())).(*ConsL)
    assertTrue(t, contains(l.ToArray(), tom_make_a()))
    assertTrue(t, contains(l.ToArray(), tom_make_b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==tom_make_a()) { a = true }
      if(o==tom_make_b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)

    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, tom_make_c())
    c := false
    for _, o := range col {
      if(o==tom_make_c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}



func Test3(t *testing.T) {
    var l *ConsintL = tom_cons_list_consint(1,tom_cons_list_consint(2,tom_empty_list_consint())).(*ConsintL)
    assertTrue(t, contains(l.ToArray(), int64(1)))
    assertTrue(t, contains(l.ToArray(), int64(2)))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o.(int64)==1) { a = true }
      if(o.(int64)==2) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)

    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))

    }
    var col []any = l.ToArray()
    col = append(col, int64(3))
    c := false
    for _, o := range col {
      if(o.(int64)==3) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}



func Test4(t *testing.T) {
    var l *FT = tom_cons_list_f(tom_make_a(),tom_cons_list_f(tom_make_b(),tom_cons_list_f(tom_make_c(),tom_empty_list_f()))).(*FT)
    var array []any = l.ToArray()
    assertTrue(t, array[0]==tom_make_a())
    assertTrue(t, array[1]==tom_make_b())
    assertTrue(t, array[2]==tom_make_c())

    var col []any = l.ToArray()
    var colarray []any = col
    assertTrue(t, colarray[0]==tom_make_a())
    assertTrue(t, colarray[1]==tom_make_b())
    assertTrue(t, colarray[2]==tom_make_c())



}



func Test5(t *testing.T) {
    var l *GT = tom_cons_list_g(tom_make_a(),tom_cons_list_g(tom_make_b(),tom_empty_list_g())).(*GT)
    assertTrue(t, contains(l.ToArray(), tom_make_a()))
    assertTrue(t, contains(l.ToArray(), tom_make_b()))
    a:=false
    b:=false
    for _, o := range l.ToArray() {
      if(o==tom_make_a()) { a = true }
      if(o==tom_make_b()) { b = true }
    }
    assertTrue(t, a)
    assertTrue(t, b)
    for _, x := range l.ToArray() {
      assertTrue(t, contains(l.ToArray(), x))
    }

    var col []any = l.ToArray()
    col = append(col, tom_make_c())
    c := false
    for _, o := range col {
      if(o==tom_make_c()) { c = true }
    }
    assertTrue(t, c)

    for _, x := range col {
      assertTrue(t, contains(col, x))
    }
}



func Test6(t *testing.T) {
    var l *GT = tom_cons_list_g(tom_make_a(),tom_cons_list_g(tom_make_b(),tom_cons_list_g(tom_make_c(),tom_empty_list_g()))).(*GT)
    var array []any = l.ToArray()
    assertEquals(t, tom_make_a(),array[0])
    assertEquals(t, tom_make_b(),array[1])
    assertEquals(t, tom_make_c(),array[2])

    var col []any = l.ToArray()
    var colarray []any = col
    assertEquals(t, tom_make_a(),colarray[0])
    assertEquals(t, tom_make_b(),colarray[1])
    assertEquals(t, tom_make_c(),colarray[2])

    var tcolarray []T = make([]T, 3)
    for i, o := range col {
      tcolarray[i] = o.(T)
    }
    var myarray []T = tcolarray
    assertEquals(t, tom_make_a(),myarray[0])
    assertEquals(t, tom_make_b(),myarray[1])
    assertEquals(t, tom_make_c(),myarray[2])

    var revl *GT = l.Reverse().(*GT)
    var revarray []any = revl.ToArray()
    assertEquals(t, tom_make_c(),revarray[0])
    assertEquals(t, tom_make_b(),revarray[1])
    assertEquals(t, tom_make_a(),revarray[2])
}




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
