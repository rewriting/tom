package gom





















import (
  "fmt"
  "strings"
  "testing"
  "unsafe"

  . "fixture/b/u/i/l/t/i/n/builtin"
  "tom/library/aterm"
)

  func tom_equal_term_boolean(t1 bool, t2 bool) bool { return t1 == t2;  }; func tom_is_sort_boolean(t bool) bool { return true;  }; func tom_is_fun_sym_true(t  bool ) bool { return t == true;  }; func tom_make_true()  bool  { return (true) }; func tom_is_fun_sym_false(t  bool ) bool { return t == false;  }; func tom_make_false()  bool  { return (false) }; func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_double(t1 float64, t2 float64) bool { return t1 == t2;  }; func tom_is_sort_double(t float64) bool { return true;  }; func tom_equal_term_long(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_long(t int64) bool { return true;  }; func tom_equal_term_float(t1 float32, t2 float32) bool { return t1 == t2;  }; func tom_is_sort_float(t float32) bool { return true;  }; func tom_equal_term_ATerm(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return t1 == t2;  }; func tom_is_sort_ATerm(t any) bool { return func() bool { _, ok := any(t).(aterm.ATerm); return ok }();  }; func tom_equal_term_ATermList(l1  *aterm.ATermList , l2  *aterm.ATermList ) bool { return l1 == l2;  }; func tom_is_sort_ATermList(t any) bool { return func() bool { _, ok := any(t).(*aterm.ATermList); return ok }();  }; func tom_equal_term_AFun(t1  *aterm.AFun , t2  *aterm.AFun ) bool { return t1 == t2;  }; func tom_is_sort_AFun(t any) bool { return func() bool { _, ok := any(t).(*aterm.AFun); return ok }();  }; func tom_equal_term_Wrapper(t1  Wrapper , t2  Wrapper ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Wrapper(t any) bool { return func() bool { _, ok := (t).(Wrapper); return ok }();  }; func tom_equal_term_ListInt(t1  ListInt , t2  ListInt ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListInt(t any) bool { return func() bool { _, ok := (t).(ListInt); return ok }();  }; func tom_equal_term_ListLong(t1  ListLong , t2  ListLong ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListLong(t any) bool { return func() bool { _, ok := (t).(ListLong); return ok }();  }; func tom_equal_term_ListBool(t1  ListBool , t2  ListBool ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListBool(t any) bool { return func() bool { _, ok := (t).(ListBool); return ok }();  }; func tom_equal_term_ListChar(t1  ListChar , t2  ListChar ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_ListChar(t any) bool { return func() bool { _, ok := (t).(ListChar); return ok }();  }; func tom_is_fun_sym_Int(t  Wrapper ) bool { return func() bool { _, ok := (t).(*IntWrapper); return ok }();  }; func tom_make_Int(i  int64 )  Wrapper  { return (MakeInt(i)) }; func tom_get_slot_Int_i(t  Wrapper )  int64  { return (t).(*IntWrapper).I;  }; func tom_is_fun_sym_IntBis(t  Wrapper ) bool { return func() bool { _, ok := (t).(*IntBisWrapper); return ok }();  }; func tom_get_slot_IntBis_i(t  Wrapper )  int64  { return (t).(*IntBisWrapper).I;  }; func tom_is_fun_sym_Name(t  Wrapper ) bool { return func() bool { _, ok := (t).(*NameWrapper); return ok }();  }; func tom_make_Name(n  string )  Wrapper  { return (MakeName(n)) }; func tom_get_slot_Name_n(t  Wrapper )  string  { return (t).(*NameWrapper).N;  }; func tom_is_fun_sym_NameBis(t  Wrapper ) bool { return func() bool { _, ok := (t).(*NameBisWrapper); return ok }();  }; func tom_get_slot_NameBis_n(t  Wrapper )  string  { return (t).(*NameBisWrapper).N;  }; func tom_make_Node(aterm  aterm.ATerm )  Wrapper  { return (MakeNode(aterm)) }; func tom_is_fun_sym_Long(t  Wrapper ) bool { return func() bool { _, ok := (t).(*LongWrapper); return ok }();  }; func tom_make_Long(l  int64 )  Wrapper  { return (MakeLong(l)) }; func tom_get_slot_Long_l(t  Wrapper )  int64  { return (t).(*LongWrapper).L;  }; func tom_is_fun_sym_Char(t  Wrapper ) bool { return func() bool { _, ok := (t).(*CharWrapper); return ok }();  }; func tom_make_Char(c  rune )  Wrapper  { return (MakeChar(c)) }; func tom_get_slot_Char_c(t  Wrapper )  rune  { return (t).(*CharWrapper).C;  }; func tom_is_fun_sym_Bool(t  Wrapper ) bool { return func() bool { _, ok := (t).(*BoolWrapper); return ok }();  }; func tom_make_Bool(b  bool )  Wrapper  { return (MakeBool(b)) }; func tom_get_slot_Bool_b(t  Wrapper )  bool  { return (t).(*BoolWrapper).B;  }; func tom_make_Double(d  float64 )  Wrapper  { return (MakeDouble(d)) }; 

  func constructNode(nodeName string, nodeLine int) aterm.ATerm {
    var factory *aterm.Factory = aterm.GetInstance()
    return factory.MakeAppl(factory.MakeAFun(nodeName,1,false),factory.MakeInt(nodeLine))
  }

  func constructList(e1 int, e2 int, e3 int) *aterm.ATermList {
    var factory *aterm.Factory = aterm.GetInstance()
    return factory.MakeList(factory.MakeInt(e1),factory.MakeList(factory.MakeInt(e2),factory.MakeList(factory.MakeInt(e3),factory.MakeListEmpty())))
  }



func TestInt(t *testing.T) {
    var t1 Wrapper = tom_make_Int(10)
    var t2 Wrapper = tom_make_Int(10)
    assertTrue(t, t1 == t2)
}



func TestString(t *testing.T) {
    var a string = "Germain"
    var b string = strings.Clone("Germain")
    assertFalse(t, unsafe.StringData(a) == unsafe.StringData(b))
    var na Wrapper = tom_make_Name(a)
    var nb Wrapper = tom_make_Name(b)
    assertTrue(t, na == nb)
}



func TestDouble(t *testing.T) {
    var a float64 = 1.23
    var b float64 = 1.23
    var na Wrapper = tom_make_Double(a)
    var nb Wrapper = tom_make_Double(b)
    assertTrue(t, na == nb)
}



func TestATerm(t *testing.T) {
    var a aterm.ATerm = constructNode("n1",2)
    var b aterm.ATerm = constructNode("n1",2)
    assertTrue(t, a == b)
    var na Wrapper = tom_make_Node(a)
    var nb Wrapper = tom_make_Node(b)
    assertTrue(t, na == nb)
}



func TestATermList(t *testing.T) {
    var a *aterm.ATermList = constructList(1,2,3)
    var b *aterm.ATermList = constructList(1,2,3)
    assertTrue(t, a == b)
}



func TestMatchInt(tt *testing.T) {
    
    
    var res string = ""
    var t Wrapper = tom_make_Int(32); { { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Int(any(t).( Wrapper )) { if tom_equal_term_int(10, tom_get_slot_Int_i(any(t).( Wrapper ))) { 

 res += "10"  };  };  };  }; { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Int(any(t).( Wrapper )) { if tom_equal_term_int(32, tom_get_slot_Int_i(any(t).( Wrapper ))) { 
 res += "32"  };  };  };  };  }; 

    assertTrue(tt, res == "32")
}



func TestMatchLong(tt *testing.T) {
    var res string = ""
    var t Wrapper = tom_make_Long(1234567890); { { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Long(any(t).( Wrapper )) { if tom_equal_term_long(32, tom_get_slot_Long_l(any(t).( Wrapper ))) { 

 res += "32l"  };  };  };  }; { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Long(any(t).( Wrapper )) { if tom_equal_term_long(1234567890, tom_get_slot_Long_l(any(t).( Wrapper ))) { 
 res += "1234567890l"  };  };  };  };  }; 

    assertTrue(tt, res == "1234567890l")
}



func TestMatchChar(tt *testing.T) {
    var res string = ""
    var t Wrapper = tom_make_Char('b'); { { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Char(any(t).( Wrapper )) { if tom_equal_term_char('a', tom_get_slot_Char_c(any(t).( Wrapper ))) { 

 res += "a"  };  };  };  }; { if tom_is_sort_Wrapper(t) { if tom_is_fun_sym_Char(any(t).( Wrapper )) { if tom_equal_term_char('b', tom_get_slot_Char_c(any(t).( Wrapper ))) { 
 res += "b"  };  };  };  };  }; 

    assertTrue(tt, res == "b")
}



func TestMatchString(t *testing.T) {
    var res string = ""
    var nm Wrapper = tom_make_Name("Germain"); { { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Name(any(nm).( Wrapper )) { if tom_equal_term_String("Pem", tom_get_slot_Name_n(any(nm).( Wrapper ))) { 

 res += "pem"  };  };  };  }; { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Name(any(nm).( Wrapper )) { if tom_equal_term_String("Germain", tom_get_slot_Name_n(any(nm).( Wrapper ))) { 
 res += "G"  };  };  };  };  }; 

    assertTrue(t, res == "G")
}



func TestDisjunctionInt(t *testing.T) {
    var res bool = false
    var wi Wrapper = tom_make_Int(42); { { if tom_is_sort_Wrapper(wi) { var tomMatch5_7 bool =  false ; _ = tomMatch5_7; var tomMatch5_4  Wrapper  =  *new(Wrapper) ; _ = tomMatch5_4; var tomMatch5_1  int64  =  0 ; _ = tomMatch5_1; var tomMatch5_3  Wrapper  =  *new(Wrapper) ; _ = tomMatch5_3; if tom_is_fun_sym_Int(any(wi).( Wrapper )) { { tomMatch5_7 =  true ; tomMatch5_3 = any(wi).( Wrapper ); tomMatch5_1 = tom_get_slot_Int_i(tomMatch5_3);  };  } else { if tom_is_fun_sym_IntBis(any(wi).( Wrapper )) { { tomMatch5_7 =  true ; tomMatch5_4 = any(wi).( Wrapper ); tomMatch5_1 = tom_get_slot_IntBis_i(tomMatch5_4);  };  };  }; if tomMatch5_7 { if tom_equal_term_int(42, tomMatch5_1) { 

 res = true  };  };  };  };  }; 

    assertTrue(t, res)
}



func TestDisjunctionString(t *testing.T) {
    var res bool = false
    var wi Wrapper = tom_make_Name("42"); { { if tom_is_sort_Wrapper(wi) { var tomMatch6_7 bool =  false ; _ = tomMatch6_7; var tomMatch6_3  Wrapper  =  *new(Wrapper) ; _ = tomMatch6_3; var tomMatch6_1  string  =  "" ; _ = tomMatch6_1; var tomMatch6_4  Wrapper  =  *new(Wrapper) ; _ = tomMatch6_4; if tom_is_fun_sym_Name(any(wi).( Wrapper )) { { tomMatch6_7 =  true ; tomMatch6_3 = any(wi).( Wrapper ); tomMatch6_1 = tom_get_slot_Name_n(tomMatch6_3);  };  } else { if tom_is_fun_sym_NameBis(any(wi).( Wrapper )) { { tomMatch6_7 =  true ; tomMatch6_4 = any(wi).( Wrapper ); tomMatch6_1 = tom_get_slot_NameBis_n(tomMatch6_4);  };  };  }; if tomMatch6_7 { if tom_equal_term_String("42", tomMatch6_1) { 

 res = true  };  };  };  };  }; 

    assertTrue(t, res)
}



func TestBoolean(t *testing.T) {
    var t1 Wrapper = tom_make_Bool(true)
    var t2 Wrapper = tom_make_Bool(true)
    assertTrue(t, t1 == t2)

    t1 = tom_make_Bool(false)
    t2 = tom_make_Bool(false)
    assertTrue(t, t1 == t2)
}



func TestMatchBoolean(t *testing.T) {
    var res bool = false
    var nm Wrapper = tom_make_Bool(tom_make_true()); { { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Bool(any(nm).( Wrapper )) { if tom_is_fun_sym_false(tom_get_slot_Bool_b(any(nm).( Wrapper ))) { 

 res = false  };  };  };  }; { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Bool(any(nm).( Wrapper )) { if tom_is_fun_sym_true(tom_get_slot_Bool_b(any(nm).( Wrapper ))) { 
 res = true  };  };  };  };  }; 

    assertTrueMsg(t, "Match in the second branch",res)

    nm = tom_make_Bool(tom_make_false()); { { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Bool(any(nm).( Wrapper )) { if tom_is_fun_sym_false(tom_get_slot_Bool_b(any(nm).( Wrapper ))) { 

 res = false  };  };  };  }; { if tom_is_sort_Wrapper(nm) { if tom_is_fun_sym_Bool(any(nm).( Wrapper )) { if tom_is_fun_sym_true(tom_get_slot_Bool_b(any(nm).( Wrapper ))) { 
 res = true  };  };  };  };  }; 

    assertFalseMsg(t, "Match in the first branch",res)
}



func TestFromString(t *testing.T) {
    var s string = "Bool(1)"
    var test Wrapper = WrapperFromString(s)
    assertEquals(t, "Terms are equals", test, tom_make_Bool(tom_make_true()))
    assertEquals(t, "String representations are equals",fmt.Sprint(test),
        "Bool(1)")
}



func TestFrom_int(t *testing.T) {
    var test Wrapper = tom_make_Int(123456)
    var testBis Wrapper = WrapperFromString(fmt.Sprint(test))
    assertEquals(t, "", test,testBis)
}




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}




func assertFalse(t *testing.T, condition bool) {
  t.Helper()
  if condition {
    t.Fatal()
  }
}




func assertTrueMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}




func assertFalseMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Fatal(message)
  }
}






func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
