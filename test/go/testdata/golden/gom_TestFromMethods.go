package gom




















import (
  "fmt"
  "math"
  "testing"

  . "fixture/b/u/i/l/t/i/n/builtin"
  "tom/library/aterm"
)

  func tom_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_is_sort_boolean(t bool) bool { return true;  }; func tom_make_true()  bool  { return (true) }; func tom_make_false()  bool  { return (false) }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_double(t1 float64, t2 float64) bool { return t1 == t2;  }; func tom_is_sort_double(t float64) bool { return true;  }; func tom_equal_term_long(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_long(t int64) bool { return true;  }; func tom_equal_term_float(t1 float32, t2 float32) bool { return t1 == t2;  }; func tom_is_sort_float(t float32) bool { return true;  }; func tom_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_equal_term_Wrapper(t1  Wrapper , t2  Wrapper ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Wrapper(t any) bool { return func() bool { _, ok := (t).(Wrapper); return ok }();  }; func tom_equal_term_ListInt(t1  ListInt , t2  ListInt ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListInt(t any) bool { return func() bool { _, ok := (t).(ListInt); return ok }();  }; func tom_equal_term_ListLong(t1  ListLong , t2  ListLong ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListLong(t any) bool { return func() bool { _, ok := (t).(ListLong); return ok }();  }; func tom_equal_term_ListBool(t1  ListBool , t2  ListBool ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListBool(t any) bool { return func() bool { _, ok := (t).(ListBool); return ok }();  }; func tom_equal_term_ListChar(t1  ListChar , t2  ListChar ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListChar(t any) bool { return func() bool { _, ok := (t).(ListChar); return ok }();  }; func tom_make_Int(i  int64 )  Wrapper  { return (MakeInt(i)) }; func tom_make_Name(n  string )  Wrapper  { return (MakeName(n)) }; func tom_make_Node(aterm  aterm.ATerm )  Wrapper  { return (MakeNode(aterm)) }; func tom_make_Long(l  int64 )  Wrapper  { return (MakeLong(l)) }; func tom_make_Char(c  rune )  Wrapper  { return (MakeChar(c)) }; func tom_make_Bool(b  bool )  Wrapper  { return (MakeBool(b)) }; func tom_is_fun_sym_concWrap(t  Wrapper ) bool { return func() bool { _, ok := (t).(*ConcWrapWrapper); return ok }();  }; func tom_empty_list_concWrap()  Wrapper  { return (EmptyConcWrap()) }; func tom_cons_list_concWrap(e  Wrapper , l  Wrapper )  Wrapper  { return (ConsConcWrap(e,l)) }; func tom_get_head_concWrap_Wrapper(l  Wrapper )  Wrapper  { return (l).GetHeadconcWrap();  }; func tom_get_tail_concWrap_Wrapper(l  Wrapper )  Wrapper  { return (l).GetTailconcWrap();  }; func tom_is_empty_concWrap_Wrapper(l  Wrapper ) bool { return (l).IsEmptyconcWrap();  }; func tom_append_list_concWrap(l1  Wrapper , l2  Wrapper )  Wrapper  { if tom_is_empty_concWrap_Wrapper(l1) { return l2 } else if tom_is_empty_concWrap_Wrapper(l2) { return l1 } else if tom_is_fun_sym_concWrap(l1) { if tom_is_empty_concWrap_Wrapper(tom_get_tail_concWrap_Wrapper(l1)) { return tom_cons_list_concWrap(tom_get_head_concWrap_Wrapper(l1), l2) } else { return tom_cons_list_concWrap(tom_get_head_concWrap_Wrapper(l1), tom_append_list_concWrap(tom_get_tail_concWrap_Wrapper(l1), l2)) } } else { return tom_cons_list_concWrap(l1, l2) }; }; func tom_get_slice_concWrap(begin  Wrapper , end  Wrapper , tail  Wrapper )  Wrapper  { if tom_equal_term_Wrapper(begin, end) { return tail } else if tom_equal_term_Wrapper(end, tail) && (tom_is_empty_concWrap_Wrapper(end) || tom_equal_term_Wrapper(end, tom_empty_list_concWrap())) { return begin }; return tom_cons_list_concWrap(func()  Wrapper  { if tom_is_fun_sym_concWrap(begin) { return tom_get_head_concWrap_Wrapper(begin) }; return begin }(), tom_get_slice_concWrap(func()  Wrapper  { if tom_is_fun_sym_concWrap(begin) { return tom_get_tail_concWrap_Wrapper(begin) }; return tom_empty_list_concWrap() }(), end, tail)) }; func tom_make_Real(r  float32 )  Wrapper  { return (MakeReal(r)) }; func tom_make_Double(d  float64 )  Wrapper  { return (MakeDouble(d)) }; func tom_make_WrapListInt(li  ListInt )  Wrapper  { return (MakeWrapListInt(li)) }; func tom_make_WrapListLong(ll  ListLong )  Wrapper  { return (MakeWrapListLong(ll)) }; func tom_make_WrapListBool(lb  ListBool )  Wrapper  { return (MakeWrapListBool(lb)) }; func tom_make_WrapListChar(lc  ListChar )  Wrapper  { return (MakeWrapListChar(lc)) }; func tom_is_fun_sym_concInt(t  ListInt ) bool { return func() bool { _, ok := (t).(*ConcIntListInt); return ok }();  }; func tom_empty_list_concInt()  ListInt  { return (EmptyConcInt()) }; func tom_cons_list_concInt(e  int64 , l  ListInt )  ListInt  { return (ConsConcInt(e,l)) }; func tom_get_head_concInt_ListInt(l  ListInt )  int64  { return (l).GetHeadconcInt();  }; func tom_get_tail_concInt_ListInt(l  ListInt )  ListInt  { return (l).GetTailconcInt();  }; func tom_is_empty_concInt_ListInt(l  ListInt ) bool { return (l).IsEmptyconcInt();  }; func tom_append_list_concInt(l1  ListInt , l2  ListInt )  ListInt  { if tom_is_empty_concInt_ListInt(l1) { return l2 } else if tom_is_empty_concInt_ListInt(l2) { return l1 } else if tom_is_empty_concInt_ListInt(tom_get_tail_concInt_ListInt(l1)) { return tom_cons_list_concInt(tom_get_head_concInt_ListInt(l1), l2) } else { return tom_cons_list_concInt(tom_get_head_concInt_ListInt(l1), tom_append_list_concInt(tom_get_tail_concInt_ListInt(l1), l2)) }; }; func tom_get_slice_concInt(begin  ListInt , end  ListInt , tail  ListInt )  ListInt  { if tom_equal_term_ListInt(begin, end) { return tail } else if tom_equal_term_ListInt(end, tail) && (tom_is_empty_concInt_ListInt(end) || tom_equal_term_ListInt(end, tom_empty_list_concInt())) { return begin }; return tom_cons_list_concInt(tom_get_head_concInt_ListInt(begin), tom_get_slice_concInt(tom_get_tail_concInt_ListInt(begin), end, tail)) }; func tom_is_fun_sym_concLong(t  ListLong ) bool { return func() bool { _, ok := (t).(*ConcLongListLong); return ok }();  }; func tom_empty_list_concLong()  ListLong  { return (EmptyConcLong()) }; func tom_cons_list_concLong(e  int64 , l  ListLong )  ListLong  { return (ConsConcLong(e,l)) }; func tom_get_head_concLong_ListLong(l  ListLong )  int64  { return (l).GetHeadconcLong();  }; func tom_get_tail_concLong_ListLong(l  ListLong )  ListLong  { return (l).GetTailconcLong();  }; func tom_is_empty_concLong_ListLong(l  ListLong ) bool { return (l).IsEmptyconcLong();  }; func tom_append_list_concLong(l1  ListLong , l2  ListLong )  ListLong  { if tom_is_empty_concLong_ListLong(l1) { return l2 } else if tom_is_empty_concLong_ListLong(l2) { return l1 } else if tom_is_empty_concLong_ListLong(tom_get_tail_concLong_ListLong(l1)) { return tom_cons_list_concLong(tom_get_head_concLong_ListLong(l1), l2) } else { return tom_cons_list_concLong(tom_get_head_concLong_ListLong(l1), tom_append_list_concLong(tom_get_tail_concLong_ListLong(l1), l2)) }; }; func tom_get_slice_concLong(begin  ListLong , end  ListLong , tail  ListLong )  ListLong  { if tom_equal_term_ListLong(begin, end) { return tail } else if tom_equal_term_ListLong(end, tail) && (tom_is_empty_concLong_ListLong(end) || tom_equal_term_ListLong(end, tom_empty_list_concLong())) { return begin }; return tom_cons_list_concLong(tom_get_head_concLong_ListLong(begin), tom_get_slice_concLong(tom_get_tail_concLong_ListLong(begin), end, tail)) }; func tom_is_fun_sym_concBool(t  ListBool ) bool { return func() bool { _, ok := (t).(*ConcBoolListBool); return ok }();  }; func tom_empty_list_concBool()  ListBool  { return (EmptyConcBool()) }; func tom_cons_list_concBool(e  bool , l  ListBool )  ListBool  { return (ConsConcBool(e,l)) }; func tom_get_head_concBool_ListBool(l  ListBool )  bool  { return (l).GetHeadconcBool();  }; func tom_get_tail_concBool_ListBool(l  ListBool )  ListBool  { return (l).GetTailconcBool();  }; func tom_is_empty_concBool_ListBool(l  ListBool ) bool { return (l).IsEmptyconcBool();  }; func tom_append_list_concBool(l1  ListBool , l2  ListBool )  ListBool  { if tom_is_empty_concBool_ListBool(l1) { return l2 } else if tom_is_empty_concBool_ListBool(l2) { return l1 } else if tom_is_empty_concBool_ListBool(tom_get_tail_concBool_ListBool(l1)) { return tom_cons_list_concBool(tom_get_head_concBool_ListBool(l1), l2) } else { return tom_cons_list_concBool(tom_get_head_concBool_ListBool(l1), tom_append_list_concBool(tom_get_tail_concBool_ListBool(l1), l2)) }; }; func tom_get_slice_concBool(begin  ListBool , end  ListBool , tail  ListBool )  ListBool  { if tom_equal_term_ListBool(begin, end) { return tail } else if tom_equal_term_ListBool(end, tail) && (tom_is_empty_concBool_ListBool(end) || tom_equal_term_ListBool(end, tom_empty_list_concBool())) { return begin }; return tom_cons_list_concBool(tom_get_head_concBool_ListBool(begin), tom_get_slice_concBool(tom_get_tail_concBool_ListBool(begin), end, tail)) }; func tom_is_fun_sym_concChar(t  ListChar ) bool { return func() bool { _, ok := (t).(*ConcCharListChar); return ok }();  }; func tom_empty_list_concChar()  ListChar  { return (EmptyConcChar()) }; func tom_cons_list_concChar(e  rune , l  ListChar )  ListChar  { return (ConsConcChar(e,l)) }; func tom_get_head_concChar_ListChar(l  ListChar )  rune  { return (l).GetHeadconcChar();  }; func tom_get_tail_concChar_ListChar(l  ListChar )  ListChar  { return (l).GetTailconcChar();  }; func tom_is_empty_concChar_ListChar(l  ListChar ) bool { return (l).IsEmptyconcChar();  }; func tom_append_list_concChar(l1  ListChar , l2  ListChar )  ListChar  { if tom_is_empty_concChar_ListChar(l1) { return l2 } else if tom_is_empty_concChar_ListChar(l2) { return l1 } else if tom_is_empty_concChar_ListChar(tom_get_tail_concChar_ListChar(l1)) { return tom_cons_list_concChar(tom_get_head_concChar_ListChar(l1), l2) } else { return tom_cons_list_concChar(tom_get_head_concChar_ListChar(l1), tom_append_list_concChar(tom_get_tail_concChar_ListChar(l1), l2)) }; }; func tom_get_slice_concChar(begin  ListChar , end  ListChar , tail  ListChar )  ListChar  { if tom_equal_term_ListChar(begin, end) { return tail } else if tom_equal_term_ListChar(end, tail) && (tom_is_empty_concChar_ListChar(end) || tom_equal_term_ListChar(end, tom_empty_list_concChar())) { return begin }; return tom_cons_list_concChar(tom_get_head_concChar_ListChar(begin), tom_get_slice_concChar(tom_get_tail_concChar_ListChar(begin), end, tail)) }; 

  func data() []Wrapper {
    return []Wrapper {
      tom_make_Char('a'),
      tom_make_Int(21),
      tom_make_Int(math.MaxInt32-2),
      tom_make_Int(math.MaxInt32),
      tom_make_Real(21),
      tom_make_Real(float32(20.3)),
      tom_make_Double(21),
      tom_make_Double(20.3),
      tom_make_Bool(tom_make_true()),
      tom_make_Bool(tom_make_false()),
      tom_make_Long(23),
      tom_make_Long(math.MaxInt32-3),
      tom_make_Long(math.MaxInt32-1),
      tom_make_Long(math.MaxInt32),
      tom_make_Long(math.MaxInt64-10),
      tom_make_Long(math.MaxInt64-1),
      tom_make_Long(math.MaxInt64),
      tom_make_Name("who?"),
      tom_make_Name("\"Hello\""),
      tom_make_Name("\"Hello\tWorld\n print-it\f end\""),
      tom_make_Node(aterm.GetInstance().Parse("f(g,(h(<a>,b),b),c)")),
      tom_cons_list_concWrap(tom_make_Int(1),tom_cons_list_concWrap(tom_make_Int(2),tom_cons_list_concWrap(tom_make_Name("toto"),tom_cons_list_concWrap(tom_make_Name("blop"),tom_empty_list_concWrap())))),
      tom_cons_list_concWrap(tom_make_Int(1),tom_cons_list_concWrap(tom_cons_list_concWrap(tom_make_Name("a"),tom_cons_list_concWrap(tom_make_Name("b"),tom_cons_list_concWrap(tom_make_Name("c"),tom_empty_list_concWrap()))),tom_cons_list_concWrap(tom_make_Int(3),tom_empty_list_concWrap()))),
      tom_make_WrapListInt(tom_cons_list_concInt(1,tom_cons_list_concInt(2,tom_cons_list_concInt(3,tom_cons_list_concInt(4,tom_cons_list_concInt(5,tom_cons_list_concInt(6,tom_cons_list_concInt(7,tom_cons_list_concInt(8,tom_cons_list_concInt(9,tom_cons_list_concInt(0,tom_empty_list_concInt()))))))))))),
      tom_make_WrapListLong(tom_cons_list_concLong(10000,tom_cons_list_concLong(123455,tom_cons_list_concLong(23445556,tom_empty_list_concLong())))),
      tom_make_WrapListBool(tom_cons_list_concBool(tom_make_true(),tom_cons_list_concBool(tom_make_false(),tom_cons_list_concBool(tom_make_true(),tom_cons_list_concBool(tom_make_false(),tom_empty_list_concBool()))))),
      tom_make_WrapListChar(tom_cons_list_concChar('t',tom_cons_list_concChar('o',tom_cons_list_concChar('m',tom_empty_list_concChar())))),
    }
  }



func TestFromTerm(t *testing.T) {
  for _, testSubject := range data() {
    var newObj Wrapper = WrapperFromTerm(testSubject.ToATerm())
    assertEquals(t, testSubject,newObj)
  }
}



func TestFromString(t *testing.T) {
  for _, testSubject := range data() {
    var newObj Wrapper = WrapperFromString(fmt.Sprint(testSubject))
    assertEquals(t, testSubject,newObj)
  }
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
