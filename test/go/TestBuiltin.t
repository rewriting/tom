// Port of test/TestBuiltin.t: the Java aterm library is replaced by its
// Go port tom/library/aterm (SingletonFactory.getInstance() is
// aterm.GetInstance()); ATermAppl/ATermInt/ATermReal casts are type
// assertions.
// port: Tom's int and long are int64, char is rune, double is float64
// (the Go mappings); (int)t is int(t), (char)value is rune(value).
// port: the instance methods are functions; assertTrue/assertEquals are
// helpers; "x".equals(s) is "x" == s.
// port: no main — `go test` runs the tests (the original main only launched JUnit).
package testbuiltin

import (
  "testing"

  "tom/library/aterm"
)

var factory *aterm.PureFactory = aterm.GetInstance()
  
  %include { int.tom }  
  %include { long.tom }  
  %include { double.tom }  
  %include { string.tom }  

  %typeterm E {
    implement { aterm.ATerm }
    is_sort(t) { $t != nil }
    equals(t1, t2)      { ($t1 == $t2) }
  }

  %op E string(stringSlot:String) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "string" }
    get_slot(stringSlot,t) { getString($t) }
    make(t) { makeString($t)  }
  }

  %op E char(charSlot:char) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "char" }
    get_slot(charSlot,t) { getChar($t) }
    make(t) { makeChar($t)  }
  }

  %op E int(intSlot:int) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "int" }
    get_slot(intSlot,t) { getInt($t) }
    make(t) { makeInt($t) }
  }

  %op E long(longSlot:long) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "long" }
    get_slot(longSlot,t) { getLong($t) }
    make(t) { makeLong($t) }
  }

  %op E double(doubleSlot:double) {
    is_fsym(t) { any($t).(*aterm.ATermAppl).GetName() == "double" }
    get_slot(doubleSlot,t) { getDouble($t) }
    make(t) { makeDouble($t) }
  }

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
    %match(int t) {
      0 -> { return 1 }
      1 -> { return 1 }
      n -> { return fib(`n - 1) + fib(`n - 2) }
    }
		return -1
  }

func fibE(t aterm.ATerm) int64 {
    %match(E t) {
      int(0) -> { return 1 }
      int(1) -> { return 1 }
      int(n) -> {
        n1 := `n - 1
        n2 := `n - 2
        return fibE(`int(n1)) + fibE(`int(n2)) }
    }
    return -1
  }

func fibLong(t int64) int64 {
    %match(long t) {
      0l -> { return 1 }
      1L -> { return 1 }
      n -> { return fibLong(`n - 1) + fibLong(`n - 2) }
    }
		return -1
  }

func fibLongE(t aterm.ATerm) int64 {
    %match(E t) {
      long(0l) -> { return 1 }
      long(1L) -> { return 1 }
      long(n) -> {
        n1 := `n - 1
        n2 := `n - 2
        return fibLongE(`long(n1)) + fibLongE(`long(n2)) }
    }
    return -1
  }

func matchString(t string) string {
    %match(String t) {
        "Albert" -> { return "Albert" }
        "Roger"  -> { return "Roger" }
    }
		return "Unknown" 
  }

func matchStringE(t aterm.ATerm) string {
    %match(E t) {
        string("Albert") -> { return "Albert" }
        string("Roger")  -> { return "Roger" }
        string(_)        -> { return "Unknown" }
    }
		return "Unknown" 
  }

func matchChar(t rune) rune {
    %match(char t) {
      'a' -> { return 'a' }
      'b' -> { return 'b' }
    }
		return '-' 
  }

func matchCharE(t aterm.ATerm) rune {
    %match(E t) {
      char('a') -> { return 'a' }
      char('b') -> { return 'b' }
    }
		return '-' 
  }

func matchInt(t int64) int64 {
    %match(int t) {
      -1 -> { return t }
      -2 -> { return -2 }
    }
		return 0 
  }
func matchDouble(t float64) float64 {
    %match(double t) {
        1.23 -> { return 1.23 }
        3.14 -> { return 3.14 }
    }
		return 0 
  }

func matchDoubleE(t aterm.ATerm) float64 {
    %match(E t) {
        double(1.23) -> { return 1.23 }
        double(3.14) -> { return 3.14 }
    }
		return 0 
  }

func Test1(t *testing.T) {
    assertTrue(t, 89 == fib(10))
  }

func Test2(t *testing.T) {
    assertTrue(t, 89 == `fibE(int(10)))
  }
  
func Test3(t *testing.T) {
    assertTrue(t, 89 == fibLong(10))
  }

func Test4(t *testing.T) {
    assertTrue(t, 89 == `fibLongE(long(10)))
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
    %match(s) {
      concString(_*,'a',_*) -> { a++ }
      concString(_*,'b',_*) -> { b++ }
      concString(_*,'c',_*) -> { c++ }
      concString(_*,'a',_*,'b',_*) -> { a_b++ }
      concString(_*,'a',_*,'c',_*) -> { a_c++ }
      concString(_*,'b',_*,'c',_*) -> { b_c++ }
      concString(_*,'ab',_*) -> { ab++ }
      concString(_*,'bc',_*) -> { bc++ }
      concString(_*,'abc',_*) -> { abc++ }
    }
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
    %match(s) {
      concString(_*,'\'',_*) -> { a++ }
      concString(_*,'"',_*) -> { b++ }
      concString(_*,'\\',_*) -> { c++ }
      concString(_*,'\'\'',_*) -> { aa++ }
    }
    assertEquals(t, a,4)
    assertEquals(t, b,1)
    assertEquals(t, c,1)
    assertEquals(t, aa,2)
  }

func TestMatchStringDefault(t *testing.T) {
    assertTrue(t, "Unknown" == matchString("Marcel"))
  }

func TestMatchStringE1(t *testing.T) {
    assertTrue(t, "Albert" == matchStringE(`string("Albert")))
  }

func TestMatchStringE2(t *testing.T) {
    assertTrue(t, "Roger" == matchStringE(`string("Roger")))
  }

func TestMatchStringEdefault(t *testing.T) {
    assertTrue(t, "Unknown" == matchStringE(`string("Marcel")))
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
    assertTrue(t, 1.23 == matchDoubleE(`double(1.23)))
  }

func TestDoubleE2(t *testing.T) {
    assertTrue(t, 3.14 == matchDoubleE(`double(3.14)))
  }

func TestDoubleEdefault(t *testing.T) {
    assertTrue(t, 0 == matchDoubleE(`double(2.71)))
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
    assertTrue(t, 'a' == matchCharE(`char('a')))
  }

func TestCharE2(t *testing.T) {
    assertTrue(t, 'b' == matchCharE(`char('b')))
  }

func TestCharEdefault(t *testing.T) {
    assertTrue(t, '-' == matchCharE(`char('k')))
  }

func TestInt(t *testing.T) {
    assertTrue(t, -1 == matchInt(`(-1)))
    assertTrue(t, -2 == matchInt(`(-2)))
    assertTrue(t, 0 == matchInt(`(-3)))
  }

// org.junit.Assert.assertTrue / assertEquals
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
