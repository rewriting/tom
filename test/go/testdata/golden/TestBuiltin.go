








package testbuiltin



import (
  "testing"

  "tom/library/aterm"
)



var factory *aterm.PureFactory = aterm.GetInstance()
  
  func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_long(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_long(t int64) bool { return true;  }; func tom_equal_term_double(t1 float64, t2 float64) bool { return t1 == t2;  }; func tom_is_sort_double(t float64) bool { return true;  }; func tom_equal_term_char(t1 rune, t2 rune) bool { return t1 == t2;  }; func tom_is_sort_char(t rune) bool { return true;  }; func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_is_fun_sym_concString(t  string ) bool { return true;  }; func tom_empty_list_concString()  string  { return ("") }; func tom_cons_list_concString(c  rune , s  string )  string  { return (string(c)+s) }; func tom_get_head_concString_String(s  string )  rune  { return tom_stringHead(s);  }; func tom_get_tail_concString_String(s  string )  string  { return tom_stringTail(s);  }; func tom_is_empty_concString_String(s  string ) bool { return len(s)==0;  }; func tom_append_list_concString(l1  string , l2  string )  string  { if tom_is_empty_concString_String(l1) { return l2 } else if tom_is_empty_concString_String(l2) { return l1 } else if tom_is_empty_concString_String(tom_get_tail_concString_String(l1)) { return tom_cons_list_concString(tom_get_head_concString_String(l1), l2) } else { return tom_cons_list_concString(tom_get_head_concString_String(l1), tom_append_list_concString(tom_get_tail_concString_String(l1), l2)) }; }; func tom_get_slice_concString(begin  string , end  string , tail  string )  string  { if tom_equal_term_String(begin, end) { return tail } else if tom_equal_term_String(end, tail) && (tom_is_empty_concString_String(end) || tom_equal_term_String(end, tom_empty_list_concString())) { return begin }; return tom_cons_list_concString(tom_get_head_concString_String(begin), tom_get_slice_concString(tom_get_tail_concString_String(begin), end, tail)) };   func tom_stringHead(s string) rune { for _, c := range s { return c }; return 0 };    func tom_stringTail(s string) string { for i := range s { if i > 0 { return s[i:] } }; return "" }; func tom_equal_term_E(t1  aterm.ATerm , t2  aterm.ATerm ) bool { return (t1 == t2);  }; func tom_is_sort_E(t any) bool { return t != nil;  }; func tom_is_fun_sym_string(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "string";  }; func tom_make_string(t  string )  aterm.ATerm  { return (makeString(t)) }; func tom_get_slot_string_stringSlot(t  aterm.ATerm )  string  { return getString(t);  }; func tom_is_fun_sym_char(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "char";  }; func tom_make_char(t  rune )  aterm.ATerm  { return (makeChar(t)) }; func tom_get_slot_char_charSlot(t  aterm.ATerm )  rune  { return getChar(t);  }; func tom_is_fun_sym_int(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "int";  }; func tom_make_int(t  int64 )  aterm.ATerm  { return (makeInt(t)) }; func tom_get_slot_int_intSlot(t  aterm.ATerm )  int64  { return getInt(t);  }; func tom_is_fun_sym_long(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "long";  }; func tom_make_long(t  int64 )  aterm.ATerm  { return (makeLong(t)) }; func tom_get_slot_long_longSlot(t  aterm.ATerm )  int64  { return getLong(t);  }; func tom_is_fun_sym_double(t  aterm.ATerm ) bool { return any(t).(*aterm.ATermAppl).GetName() == "double";  }; func tom_make_double(t  float64 )  aterm.ATerm  { return (makeDouble(t)) }; func tom_get_slot_double_doubleSlot(t  aterm.ATerm )  float64  { return getDouble(t);  }; 








































func makeString(t string) aterm.ATerm {
    return factory.MakeAppl(factory.MakeAFun("string", 1, false),
                            factory.MakeAppl(factory.MakeAFun(t, 0, true)))
  }



func makeChar(t rune) aterm.ATerm {
    return factory.MakeAppl(factory.MakeAFun("char", 1, false),
                            factory.MakeInt(int(t)))
  }



func makeInt(t int64) aterm.ATerm {
    return factory.MakeAppl(factory.MakeAFun("int", 1, false),
                            factory.MakeInt(int(t)))
  }



func makeLong(t int64) aterm.ATerm {
    return factory.MakeAppl(factory.MakeAFun("long", 1, false),
                            factory.MakeInt(int(t)))
  }



func makeDouble(t float64) aterm.ATerm {
    return factory.MakeAppl(factory.MakeAFun("double", 1, false),
                            factory.MakeReal(t))
  }



func getString(t aterm.ATerm) string {
    subterm := t.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermAppl)
    name := subterm.GetName()
    return name
  }
  

func getInt(t aterm.ATerm) int64 {
    subterm := t.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermInt)
    value := int64(subterm.GetInt())
    return value
  }



func getLong(t aterm.ATerm) int64 {
    subterm := t.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermInt)
    value := int64(subterm.GetInt())
    return value
  }



func getChar(t aterm.ATerm) rune {
    subterm := t.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermInt)
    value := subterm.GetInt()
    return rune(value)
  }



func getDouble(t aterm.ATerm) float64 {
    subterm := t.(*aterm.ATermAppl).GetArgument(0).(*aterm.ATermReal)
    value := subterm.GetReal()
    return value
  }



func fib(t int64) int64 {
    ; { { if  true  { if tom_equal_term_int(0, any(t).( int64 )) { 
 return 1  };  };  }; { if  true  { if tom_equal_term_int(1, any(t).( int64 )) { 
 return 1  };  };  }; { if  true  { var tom___n  int64  = any(t).( int64 ); _ = tom___n; 
 return fib(tom___n - 1) + fib(tom___n - 2)  };  };  }; 

		return -1
  }



func fibE(t aterm.ATerm) int64 {
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_int(any(t).( aterm.ATerm )) { if tom_equal_term_int(0, tom_get_slot_int_intSlot(any(t).( aterm.ATerm ))) { 
 return 1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_int(any(t).( aterm.ATerm )) { if tom_equal_term_int(1, tom_get_slot_int_intSlot(any(t).( aterm.ATerm ))) { 
 return 1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_int(any(t).( aterm.ATerm )) { var tom___n  int64  = tom_get_slot_int_intSlot(any(t).( aterm.ATerm )); _ = tom___n; 

        n1 := tom___n - 1
        n2 := tom___n - 2
        return fibE(tom_make_int(n1)) + fibE(tom_make_int(n2))  };  };  };  }; 

    return -1
  }



func fibLong(t int64) int64 {
    ; { { if  true  { if tom_equal_term_long(0, any(t).( int64 )) { 
 return 1  };  };  }; { if  true  { if tom_equal_term_long(1, any(t).( int64 )) { 
 return 1  };  };  }; { if  true  { var tom___n  int64  = any(t).( int64 ); _ = tom___n; 
 return fibLong(tom___n - 1) + fibLong(tom___n - 2)  };  };  }; 

		return -1
  }



func fibLongE(t aterm.ATerm) int64 {
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_long(any(t).( aterm.ATerm )) { if tom_equal_term_long(0, tom_get_slot_long_longSlot(any(t).( aterm.ATerm ))) { 
 return 1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_long(any(t).( aterm.ATerm )) { if tom_equal_term_long(1, tom_get_slot_long_longSlot(any(t).( aterm.ATerm ))) { 
 return 1  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_long(any(t).( aterm.ATerm )) { var tom___n  int64  = tom_get_slot_long_longSlot(any(t).( aterm.ATerm )); _ = tom___n; 

        n1 := tom___n - 1
        n2 := tom___n - 2
        return fibLongE(tom_make_long(n1)) + fibLongE(tom_make_long(n2))  };  };  };  }; 

    return -1
  }



func matchString(t string) string {
    ; { { if  true  { if tom_equal_term_String("Albert", any(t).( string )) { 
 return "Albert"  };  };  }; { if  true  { if tom_equal_term_String("Roger", any(t).( string )) { 
 return "Roger"  };  };  };  }; 

		return "Unknown" 
  }



func matchStringE(t aterm.ATerm) string {
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_string(any(t).( aterm.ATerm )) { if tom_equal_term_String("Albert", tom_get_slot_string_stringSlot(any(t).( aterm.ATerm ))) { 
 return "Albert"  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_string(any(t).( aterm.ATerm )) { if tom_equal_term_String("Roger", tom_get_slot_string_stringSlot(any(t).( aterm.ATerm ))) { 
 return "Roger"  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_string(any(t).( aterm.ATerm )) { 
 return "Unknown"  };  };  };  }; 

		return "Unknown" 
  }



func matchChar(t rune) rune {
    ; { { if  true  { if tom_equal_term_char('a', any(t).( rune )) { 
 return 'a'  };  };  }; { if  true  { if tom_equal_term_char('b', any(t).( rune )) { 
 return 'b'  };  };  };  }; 

		return '-' 
  }



func matchCharE(t aterm.ATerm) rune {
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_char(any(t).( aterm.ATerm )) { if tom_equal_term_char('a', tom_get_slot_char_charSlot(any(t).( aterm.ATerm ))) { 
 return 'a'  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_char(any(t).( aterm.ATerm )) { if tom_equal_term_char('b', tom_get_slot_char_charSlot(any(t).( aterm.ATerm ))) { 
 return 'b'  };  };  };  };  }; 

		return '-' 
  }



func matchInt(t int64) int64 {
    ; { { if  true  { if tom_equal_term_int(-1, any(t).( int64 )) { 
 return t  };  };  }; { if  true  { if tom_equal_term_int(-2, any(t).( int64 )) { 
 return -2  };  };  };  }; 

		return 0 
  }
func matchDouble(t float64) float64 {
    ; { { if  true  { if tom_equal_term_double(1.23, any(t).( float64 )) { 
 return 1.23  };  };  }; { if  true  { if tom_equal_term_double(3.14, any(t).( float64 )) { 
 return 3.14  };  };  };  }; 

		return 0 
  }



func matchDoubleE(t aterm.ATerm) float64 {
    ; { { if tom_is_sort_E(t) { if tom_is_fun_sym_double(any(t).( aterm.ATerm )) { if tom_equal_term_double(1.23, tom_get_slot_double_doubleSlot(any(t).( aterm.ATerm ))) { 
 return 1.23  };  };  };  }; { if tom_is_sort_E(t) { if tom_is_fun_sym_double(any(t).( aterm.ATerm )) { if tom_equal_term_double(3.14, tom_get_slot_double_doubleSlot(any(t).( aterm.ATerm ))) { 
 return 3.14  };  };  };  };  }; 

		return 0 
  }



func Test1(t *testing.T) {
    assertTrue(t, 89 == fib(10))
  }



func Test2(t *testing.T) {
    assertTrue(t, 89 == fibE(tom_make_int(10)))
  }
  

func Test3(t *testing.T) {
    assertTrue(t, 89 == fibLong(10))
  }



func Test4(t *testing.T) {
    assertTrue(t, 89 == fibLongE(tom_make_long(10)))
  }



func TestMatchString1(t *testing.T) {
    assertTrue(t, "Albert" == matchString("Albert"))
  }



func TestMatchString2(t *testing.T) {
    assertTrue(t, "Roger" == matchString("Roger"))
  }
 

func TestMatchString3(t *testing.T) {
    s := "abcaabbccabc";
    a := 0
    b := 0
    c := 0
    a_b := 0
    a_c := 0
    b_c := 0
    ab := 0
    bc := 0
    abc := 0
    ; { { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_4  string  = any(s).( string ); _ = tomMatch12_end_4; for { { if !(tom_is_empty_concString_String(tomMatch12_end_4)) { if tom_equal_term_char('a', tom_get_head_concString_String(tomMatch12_end_4)) { 
 a++  };  }; if tom_is_empty_concString_String(tomMatch12_end_4) { tomMatch12_end_4 = any(s).( string );  } else { tomMatch12_end_4 = tom_get_tail_concString_String(tomMatch12_end_4);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_4, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_13  string  = any(s).( string ); _ = tomMatch12_end_13; for { { if !(tom_is_empty_concString_String(tomMatch12_end_13)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_end_13)) { 
 b++  };  }; if tom_is_empty_concString_String(tomMatch12_end_13) { tomMatch12_end_13 = any(s).( string );  } else { tomMatch12_end_13 = tom_get_tail_concString_String(tomMatch12_end_13);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_13, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_22  string  = any(s).( string ); _ = tomMatch12_end_22; for { { if !(tom_is_empty_concString_String(tomMatch12_end_22)) { if tom_equal_term_char('c', tom_get_head_concString_String(tomMatch12_end_22)) { 
 c++  };  }; if tom_is_empty_concString_String(tomMatch12_end_22) { tomMatch12_end_22 = any(s).( string );  } else { tomMatch12_end_22 = tom_get_tail_concString_String(tomMatch12_end_22);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_22, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_31  string  = any(s).( string ); _ = tomMatch12_end_31; for { { if !(tom_is_empty_concString_String(tomMatch12_end_31)) { if tom_equal_term_char('a', tom_get_head_concString_String(tomMatch12_end_31)) { var tomMatch12_32  string  = tom_get_tail_concString_String(tomMatch12_end_31); _ = tomMatch12_32; var tomMatch12_end_35  string  = tomMatch12_32; _ = tomMatch12_end_35; for { { if !(tom_is_empty_concString_String(tomMatch12_end_35)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_end_35)) { 
 a_b++  };  }; if tom_is_empty_concString_String(tomMatch12_end_35) { tomMatch12_end_35 = tomMatch12_32;  } else { tomMatch12_end_35 = tom_get_tail_concString_String(tomMatch12_end_35);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_35, tomMatch12_32))) { break; }; };  };  }; if tom_is_empty_concString_String(tomMatch12_end_31) { tomMatch12_end_31 = any(s).( string );  } else { tomMatch12_end_31 = tom_get_tail_concString_String(tomMatch12_end_31);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_31, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_46  string  = any(s).( string ); _ = tomMatch12_end_46; for { { if !(tom_is_empty_concString_String(tomMatch12_end_46)) { if tom_equal_term_char('a', tom_get_head_concString_String(tomMatch12_end_46)) { var tomMatch12_47  string  = tom_get_tail_concString_String(tomMatch12_end_46); _ = tomMatch12_47; var tomMatch12_end_50  string  = tomMatch12_47; _ = tomMatch12_end_50; for { { if !(tom_is_empty_concString_String(tomMatch12_end_50)) { if tom_equal_term_char('c', tom_get_head_concString_String(tomMatch12_end_50)) { 
 a_c++  };  }; if tom_is_empty_concString_String(tomMatch12_end_50) { tomMatch12_end_50 = tomMatch12_47;  } else { tomMatch12_end_50 = tom_get_tail_concString_String(tomMatch12_end_50);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_50, tomMatch12_47))) { break; }; };  };  }; if tom_is_empty_concString_String(tomMatch12_end_46) { tomMatch12_end_46 = any(s).( string );  } else { tomMatch12_end_46 = tom_get_tail_concString_String(tomMatch12_end_46);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_46, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_61  string  = any(s).( string ); _ = tomMatch12_end_61; for { { if !(tom_is_empty_concString_String(tomMatch12_end_61)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_end_61)) { var tomMatch12_62  string  = tom_get_tail_concString_String(tomMatch12_end_61); _ = tomMatch12_62; var tomMatch12_end_65  string  = tomMatch12_62; _ = tomMatch12_end_65; for { { if !(tom_is_empty_concString_String(tomMatch12_end_65)) { if tom_equal_term_char('c', tom_get_head_concString_String(tomMatch12_end_65)) { 
 b_c++  };  }; if tom_is_empty_concString_String(tomMatch12_end_65) { tomMatch12_end_65 = tomMatch12_62;  } else { tomMatch12_end_65 = tom_get_tail_concString_String(tomMatch12_end_65);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_65, tomMatch12_62))) { break; }; };  };  }; if tom_is_empty_concString_String(tomMatch12_end_61) { tomMatch12_end_61 = any(s).( string );  } else { tomMatch12_end_61 = tom_get_tail_concString_String(tomMatch12_end_61);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_61, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_77  string  = any(s).( string ); _ = tomMatch12_end_77; for { { var tomMatch12_end_80  string  = tomMatch12_end_77; _ = tomMatch12_end_80; for { { var tomMatch12_73  string  = tom_get_slice_concString(tomMatch12_end_77,tomMatch12_end_80,tom_empty_list_concString()); _ = tomMatch12_73; if tom_is_fun_sym_concString(tomMatch12_73) { if !(tom_is_empty_concString_String(tomMatch12_73)) { if tom_equal_term_char('a', tom_get_head_concString_String(tomMatch12_73)) { var tomMatch12_83  string  = tom_get_tail_concString_String(tomMatch12_73); _ = tomMatch12_83; if !(tom_is_empty_concString_String(tomMatch12_83)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_83)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch12_83)) { 
 ab++  };  };  };  };  };  }; if tom_is_empty_concString_String(tomMatch12_end_80) { tomMatch12_end_80 = tomMatch12_end_77;  } else { tomMatch12_end_80 = tom_get_tail_concString_String(tomMatch12_end_80);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_80, tomMatch12_end_77))) { break; }; }; if tom_is_empty_concString_String(tomMatch12_end_77) { tomMatch12_end_77 = any(s).( string );  } else { tomMatch12_end_77 = tom_get_tail_concString_String(tomMatch12_end_77);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_77, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_94  string  = any(s).( string ); _ = tomMatch12_end_94; for { { var tomMatch12_end_97  string  = tomMatch12_end_94; _ = tomMatch12_end_97; for { { var tomMatch12_90  string  = tom_get_slice_concString(tomMatch12_end_94,tomMatch12_end_97,tom_empty_list_concString()); _ = tomMatch12_90; if tom_is_fun_sym_concString(tomMatch12_90) { if !(tom_is_empty_concString_String(tomMatch12_90)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_90)) { var tomMatch12_100  string  = tom_get_tail_concString_String(tomMatch12_90); _ = tomMatch12_100; if !(tom_is_empty_concString_String(tomMatch12_100)) { if tom_equal_term_char('c', tom_get_head_concString_String(tomMatch12_100)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch12_100)) { 
 bc++  };  };  };  };  };  }; if tom_is_empty_concString_String(tomMatch12_end_97) { tomMatch12_end_97 = tomMatch12_end_94;  } else { tomMatch12_end_97 = tom_get_tail_concString_String(tomMatch12_end_97);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_97, tomMatch12_end_94))) { break; }; }; if tom_is_empty_concString_String(tomMatch12_end_94) { tomMatch12_end_94 = any(s).( string );  } else { tomMatch12_end_94 = tom_get_tail_concString_String(tomMatch12_end_94);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_94, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch12_end_111  string  = any(s).( string ); _ = tomMatch12_end_111; for { { var tomMatch12_end_114  string  = tomMatch12_end_111; _ = tomMatch12_end_114; for { { var tomMatch12_107  string  = tom_get_slice_concString(tomMatch12_end_111,tomMatch12_end_114,tom_empty_list_concString()); _ = tomMatch12_107; if tom_is_fun_sym_concString(tomMatch12_107) { if !(tom_is_empty_concString_String(tomMatch12_107)) { if tom_equal_term_char('a', tom_get_head_concString_String(tomMatch12_107)) { var tomMatch12_117  string  = tom_get_tail_concString_String(tomMatch12_107); _ = tomMatch12_117; if !(tom_is_empty_concString_String(tomMatch12_117)) { if tom_equal_term_char('b', tom_get_head_concString_String(tomMatch12_117)) { var tomMatch12_118  string  = tom_get_tail_concString_String(tomMatch12_117); _ = tomMatch12_118; if !(tom_is_empty_concString_String(tomMatch12_118)) { if tom_equal_term_char('c', tom_get_head_concString_String(tomMatch12_118)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch12_118)) { 
 abc++  };  };  };  };  };  };  };  }; if tom_is_empty_concString_String(tomMatch12_end_114) { tomMatch12_end_114 = tomMatch12_end_111;  } else { tomMatch12_end_114 = tom_get_tail_concString_String(tomMatch12_end_114);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_114, tomMatch12_end_111))) { break; }; }; if tom_is_empty_concString_String(tomMatch12_end_111) { tomMatch12_end_111 = any(s).( string );  } else { tomMatch12_end_111 = tom_get_tail_concString_String(tomMatch12_end_111);  };  };  if !(!(tom_equal_term_String(tomMatch12_end_111, any(s).( string )))) { break; }; };  };  };  };  }; 

    assertEquals(t, a,4)
    assertEquals(t, b,4)
    assertEquals(t, c,4)
    assertEquals(t, a_b,11)
    assertEquals(t, a_c,11)
    assertEquals(t, b_c,11)
    assertEquals(t, ab,3)
    assertEquals(t, bc,3)
    assertEquals(t, abc,2)
  }



func TestMatchString4(t *testing.T) {
    s := "a'b\"c\\d'''e";
    a := 0
    b := 0
    c := 0
    aa := 0
    ; { { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch13_end_5  string  = any(s).( string ); _ = tomMatch13_end_5; for { { var tomMatch13_end_8  string  = tomMatch13_end_5; _ = tomMatch13_end_8; for { { var tomMatch13_1  string  = tom_get_slice_concString(tomMatch13_end_5,tomMatch13_end_8,tom_empty_list_concString()); _ = tomMatch13_1; if tom_is_fun_sym_concString(tomMatch13_1) { if !(tom_is_empty_concString_String(tomMatch13_1)) { if tom_equal_term_char('\'', tom_get_head_concString_String(tomMatch13_1)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch13_1)) { 
 a++  };  };  };  }; if tom_is_empty_concString_String(tomMatch13_end_8) { tomMatch13_end_8 = tomMatch13_end_5;  } else { tomMatch13_end_8 = tom_get_tail_concString_String(tomMatch13_end_8);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_8, tomMatch13_end_5))) { break; }; }; if tom_is_empty_concString_String(tomMatch13_end_5) { tomMatch13_end_5 = any(s).( string );  } else { tomMatch13_end_5 = tom_get_tail_concString_String(tomMatch13_end_5);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_5, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch13_end_18  string  = any(s).( string ); _ = tomMatch13_end_18; for { { if !(tom_is_empty_concString_String(tomMatch13_end_18)) { if tom_equal_term_char('"', tom_get_head_concString_String(tomMatch13_end_18)) { 
 b++  };  }; if tom_is_empty_concString_String(tomMatch13_end_18) { tomMatch13_end_18 = any(s).( string );  } else { tomMatch13_end_18 = tom_get_tail_concString_String(tomMatch13_end_18);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_18, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch13_end_28  string  = any(s).( string ); _ = tomMatch13_end_28; for { { var tomMatch13_end_31  string  = tomMatch13_end_28; _ = tomMatch13_end_31; for { { var tomMatch13_24  string  = tom_get_slice_concString(tomMatch13_end_28,tomMatch13_end_31,tom_empty_list_concString()); _ = tomMatch13_24; if tom_is_fun_sym_concString(tomMatch13_24) { if !(tom_is_empty_concString_String(tomMatch13_24)) { if tom_equal_term_char('\\', tom_get_head_concString_String(tomMatch13_24)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch13_24)) { 
 c++  };  };  };  }; if tom_is_empty_concString_String(tomMatch13_end_31) { tomMatch13_end_31 = tomMatch13_end_28;  } else { tomMatch13_end_31 = tom_get_tail_concString_String(tomMatch13_end_31);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_31, tomMatch13_end_28))) { break; }; }; if tom_is_empty_concString_String(tomMatch13_end_28) { tomMatch13_end_28 = any(s).( string );  } else { tomMatch13_end_28 = tom_get_tail_concString_String(tomMatch13_end_28);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_28, any(s).( string )))) { break; }; };  };  };  }; { if  true  { if tom_is_fun_sym_concString(any(s).( string )) { var tomMatch13_end_42  string  = any(s).( string ); _ = tomMatch13_end_42; for { { var tomMatch13_end_45  string  = tomMatch13_end_42; _ = tomMatch13_end_45; for { { var tomMatch13_38  string  = tom_get_slice_concString(tomMatch13_end_42,tomMatch13_end_45,tom_empty_list_concString()); _ = tomMatch13_38; if tom_is_fun_sym_concString(tomMatch13_38) { if !(tom_is_empty_concString_String(tomMatch13_38)) { if tom_equal_term_char('\'', tom_get_head_concString_String(tomMatch13_38)) { var tomMatch13_48  string  = tom_get_tail_concString_String(tomMatch13_38); _ = tomMatch13_48; if !(tom_is_empty_concString_String(tomMatch13_48)) { if tom_equal_term_char('\'', tom_get_head_concString_String(tomMatch13_48)) { if tom_is_empty_concString_String(tom_get_tail_concString_String(tomMatch13_48)) { 
 aa++  };  };  };  };  };  }; if tom_is_empty_concString_String(tomMatch13_end_45) { tomMatch13_end_45 = tomMatch13_end_42;  } else { tomMatch13_end_45 = tom_get_tail_concString_String(tomMatch13_end_45);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_45, tomMatch13_end_42))) { break; }; }; if tom_is_empty_concString_String(tomMatch13_end_42) { tomMatch13_end_42 = any(s).( string );  } else { tomMatch13_end_42 = tom_get_tail_concString_String(tomMatch13_end_42);  };  };  if !(!(tom_equal_term_String(tomMatch13_end_42, any(s).( string )))) { break; }; };  };  };  };  }; 

    assertEquals(t, a,4)
    assertEquals(t, b,1)
    assertEquals(t, c,1)
    assertEquals(t, aa,2)
  }



func TestMatchStringDefault(t *testing.T) {
    assertTrue(t, "Unknown" == matchString("Marcel"))
  }



func TestMatchStringE1(t *testing.T) {
    assertTrue(t, "Albert" == matchStringE(tom_make_string("Albert")))
  }



func TestMatchStringE2(t *testing.T) {
    assertTrue(t, "Roger" == matchStringE(tom_make_string("Roger")))
  }



func TestMatchStringEdefault(t *testing.T) {
    assertTrue(t, "Unknown" == matchStringE(tom_make_string("Marcel")))
  }



func TestDouble1(t *testing.T) {
    assertTrue(t, 1.23 == matchDouble(1.23))
  }



func TestDouble2(t *testing.T) {
    assertTrue(t, 3.14 == matchDouble(3.14))
  }



func TestDoubleDefault(t *testing.T) {
    assertTrue(t, 0 == matchDouble(2.71))
  }



func TestDoubleE1(t *testing.T) {
    assertTrue(t, 1.23 == matchDoubleE(tom_make_double(1.23)))
  }



func TestDoubleE2(t *testing.T) {
    assertTrue(t, 3.14 == matchDoubleE(tom_make_double(3.14)))
  }



func TestDoubleEdefault(t *testing.T) {
    assertTrue(t, 0 == matchDoubleE(tom_make_double(2.71)))
  }



func TestChar1(t *testing.T) {
    assertTrue(t, 'a' == matchChar('a'))
  }



func TestChar2(t *testing.T) {
    assertTrue(t, 'b' == matchChar('b'))
  }



func TestCharDefault(t *testing.T) {
    assertTrue(t, '-' == matchChar('z'))
  }



func TestCharE1(t *testing.T) {
    assertTrue(t, 'a' == matchCharE(tom_make_char('a')))
  }



func TestCharE2(t *testing.T) {
    assertTrue(t, 'b' == matchCharE(tom_make_char('b')))
  }



func TestCharEdefault(t *testing.T) {
    assertTrue(t, '-' == matchCharE(tom_make_char('k')))
  }



func TestInt(t *testing.T) {
    assertTrue(t, -1 == matchInt((-1)))
    assertTrue(t, -2 == matchInt((-2)))
    assertTrue(t, 0 == matchInt((-3)))
  }




func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Error()
  }
}



func assertEquals(t *testing.T, expected, actual int) {
  t.Helper()
  if expected != actual {
    t.Errorf("expected:<%d> but was:<%d>", expected, actual)
  }
}
