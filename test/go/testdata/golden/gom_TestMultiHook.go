package gom















import (
  "testing"

  . "fixture/multihook"
)

  func tom_equal_term_int(t1 int64, t2 int64) bool { return t1 == t2;  }; func tom_is_sort_int(t int64) bool { return true;  }; func tom_equal_term_Wrapper(t1  Wrapper , t2  Wrapper ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Wrapper(t any) bool { return func() bool { _, ok := (t).(Wrapper); return ok }();  }; func tom_make_Int(i  int64 )  Wrapper  { return (MakeInt(i)) }; func tom_make_OInt(i  int64 )  Wrapper  { return (MakeOInt(i)) };   func init() {   HookMakeInt = func(i int64) (Wrapper, int64) {     var j int64;     tom__r := func() Wrapper {     {    i = i+1;      };     j = i;     {    j = j*2;      };     return nil;     }();     return tom__r, j;   }; };    func init() {   HookMakeOInt = func(i int64) (Wrapper, int64) {     tom__r := func() Wrapper {     {    i = i+1;      };     {    i = i*2;      };     return nil;     }();     return tom__r, i;   }; };  

func TestInt(tt *testing.T) {
    var t Wrapper = tom_make_Int(10)
    assertEquals(tt, t.(*IntWrapper).I,int64(22))
}



func TestOInt(tt *testing.T) {
    var t Wrapper = tom_make_OInt(10)
    assertEquals(tt, t.(*OIntWrapper).I,int64(22))
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
