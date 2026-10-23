package gom














import (
  "fmt"
  "io"
  "os"
  "testing"

  . "fixture/javahook"
)

  func tom_TestJavaHook_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestJavaHook_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestJavaHook_make_EmptyTerm()  Term  { return (MakeEmptyTerm()) }; func tom_TestJavaHook_make_Unary(slot  Term )  Term  { return (MakeUnary(slot)) }; func tom_TestJavaHook_make_Binary(ls  Term , rs  Term )  Term  { return (MakeBinary(ls,rs)) }; func tom_TestJavaHook_make_Ternary(ls  Term , cs  Term , rs  Term )  Term  { return (MakeTernary(ls,cs,rs)) }; func tom_TestJavaHook_is_fun_sym_Vary(t  Term ) bool { return func() bool { _, ok := (t).(*VaryTerm); return ok }();  }; func tom_TestJavaHook_empty_list_Vary()  Term  { return (EmptyVary()) }; func tom_TestJavaHook_cons_list_Vary(e  Term , l  Term )  Term  { return (ConsVary(e,l)) }; func tom_TestJavaHook_get_head_Vary_Term(l  Term )  Term  { return (l).GetHeadVary();  }; func tom_TestJavaHook_get_tail_Vary_Term(l  Term )  Term  { return (l).GetTailVary();  }; func tom_TestJavaHook_is_empty_Vary_Term(l  Term ) bool { return (l).IsEmptyVary();  }; func tom_TestJavaHook_append_list_Vary(l1  Term , l2  Term )  Term  { if tom_TestJavaHook_is_empty_Vary_Term(l1) { return l2 } else if tom_TestJavaHook_is_empty_Vary_Term(l2) { return l1 } else if tom_TestJavaHook_is_fun_sym_Vary(l1) { if tom_TestJavaHook_is_empty_Vary_Term(tom_TestJavaHook_get_tail_Vary_Term(l1)) { return tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_get_head_Vary_Term(l1), l2) } else { return tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_get_head_Vary_Term(l1), tom_TestJavaHook_append_list_Vary(tom_TestJavaHook_get_tail_Vary_Term(l1), l2)) } } else { return tom_TestJavaHook_cons_list_Vary(l1, l2) }; }; func tom_TestJavaHook_get_slice_Vary(begin  Term , end  Term , tail  Term )  Term  { if tom_TestJavaHook_equal_term_Term(begin, end) { return tail } else if tom_TestJavaHook_equal_term_Term(end, tail) && (tom_TestJavaHook_is_empty_Vary_Term(end) || tom_TestJavaHook_equal_term_Term(end, tom_TestJavaHook_empty_list_Vary())) { return begin }; return tom_TestJavaHook_cons_list_Vary(func()  Term  { if tom_TestJavaHook_is_fun_sym_Vary(begin) { return tom_TestJavaHook_get_head_Vary_Term(begin) }; return begin }(), tom_TestJavaHook_get_slice_Vary(func()  Term  { if tom_TestJavaHook_is_fun_sym_Vary(begin) { return tom_TestJavaHook_get_tail_Vary_Term(begin) }; return tom_TestJavaHook_empty_list_Vary() }(), end, tail)) };   func init() {   HookMakeEmptyTerm = func() (Term) {     tom__r := func() Term {     {    fmt.Println("EmptyTerm()");      };     return nil;     }();     return tom__r;   }; };    func init() {   HookMakeUnary = func(a Term) (Term, Term) {     tom__r := func() Term {     {    fmt.Println("Unary("+fmt.Sprint(a)+")");      };     return nil;     }();     return tom__r, a;   }; };    func init() {   HookMakeBinary = func(l Term, r Term) (Term, Term, Term) {     tom__r := func() Term {     {    fmt.Println("Binary("+fmt.Sprint(l)+","+fmt.Sprint(r)+")");      };     return nil;     }();     return tom__r, l, r;   }; };    func init() {   HookMakeTernary = func(t1 Term, t2 Term, t3 Term) (Term, Term, Term, Term) {     tom__r := func() Term {     {    fmt.Println("Ternary("+fmt.Sprint(t1)+","+fmt.Sprint(t2)+","+fmt.Sprint(t3)+")");      };     return nil;     }();     return tom__r, t1, t2, t3;   }; };    func init() {   HookMakeInsertVary = func(e Term, l Term) (Term, Term, Term) {     tom__r := func() Term {     {    fmt.Println("inserting("+fmt.Sprint(e)+","+fmt.Sprint(l)+")");      };     return nil;     }();     return tom__r, e, l;   }; };  

  var newline string = "\n"






func captureStdout(t *testing.T, f func()) string {
  t.Helper()
  tmpOut := os.Stdout
  r, w, err := os.Pipe()
  if err != nil {
    t.Fatal(err)
  }
  os.Stdout = w
  done := make(chan string)
  go func() { b, _ := io.ReadAll(r); done <- string(b) }()
  f()
  w.Close()
  os.Stdout = tmpOut
  return <-done
}



func TestEmptyMakeHook(t *testing.T) {
    res := captureStdout(t, func() {
      _ = tom_TestJavaHook_make_EmptyTerm()
    })
    assertEquals(t, res,"EmptyTerm()" + newline)
}



func TestUnaryMakeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = tom_TestJavaHook_make_Unary(tom_TestJavaHook_make_EmptyTerm())
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()"+newline+"Unary(EmptyTerm())" + newline)
}



func TestBinaryMakeBeforeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = tom_TestJavaHook_make_Binary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_make_EmptyTerm())
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()" + newline + "EmptyTerm()" + newline + "Binary(EmptyTerm(),EmptyTerm())" + newline )
}



func TestTernaryMakeBeforeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = tom_TestJavaHook_make_Ternary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_make_Binary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_make_Unary(tom_TestJavaHook_make_EmptyTerm())))
      _ = empty
    })
    assertEquals(t, res,
        "EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"Unary(EmptyTerm())"+newline +"Binary(EmptyTerm(),Unary(EmptyTerm()))"+newline +"Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())))" + newline)
}



func TestVaryMakeInsertHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_cons_list_Vary(tom_TestJavaHook_make_EmptyTerm(),tom_TestJavaHook_empty_list_Vary()))))
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "inserting(EmptyTerm(),Vary())" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm(),EmptyTerm()))" + newline)
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

