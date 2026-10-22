package gom












import (
  "fmt"
  "testing"

  . "fixture/ying"
)

  func tom_equal_term_Sun(t1  Sun , t2  Sun ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Sun(t any) bool { return func() bool { _, ok := (t).(Sun); return ok }();  }; func tom_make_pong(st  Moon )  Sun  { return (MakePong(st)) }; func tom_make_shi()  Sun  { return (MakeShi()) }; func tom_equal_term_Moon(t1  Moon , t2  Moon ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Moon(t any) bool { return func() bool { _, ok := (t).(Moon); return ok }();  }; func tom_make_ping(sl  Sun )  Moon  { return (MakePing(sl)) }; 

func TestToString(t *testing.T) {
    var m Moon = tom_make_ping(tom_make_pong(tom_make_ping(tom_make_shi()))) 
    assertEquals(t, "ping(pong(ping(shi())))",fmt.Sprint(m))
}




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
