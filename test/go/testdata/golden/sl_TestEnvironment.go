package sl












import (
  "testing"

  "tom/library/sl"
)

  func tom_TestEnvironment_equal_term_Term(t1  Term , t2  Term ) bool { return (t1).Equivalent(t2);  }; func tom_TestEnvironment_is_sort_Term(t any) bool { return func() bool { _, ok := (t).(Term); return ok }();  }; func tom_TestEnvironment_make_f(lhs  Term , rhs  Term )  Term  { return (MakeF(lhs,rhs)) }; func tom_TestEnvironment_make_a()  Term  { return (MakeA()) }; func tom_TestEnvironment_make_b()  Term  { return (MakeB()) }; 






func TestEqual(t *testing.T){
    var e1 *sl.Environment = sl.NewEnvironment()
    var e2 *sl.Environment = sl.NewEnvironment()
    assertEquals(t, e1,e2)
    var subject Term = tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b())
    e1.SetSubject(subject)
    e2.SetSubject(subject)
    assertEquals(t, e1,e2)
    e1.Down(2)
    e2.Down(2)
    assertEquals(t, e1,e2)
    e1.Up()
    e2.Up()
    e1.Down(1)
    e2.Down(1)
    assertEquals(t, e1,e2)
}





func TestUpAndDown(t *testing.T){
    var e1 *sl.Environment = sl.NewEnvironment()
    var e2 *sl.Environment = sl.NewEnvironment()
    var subject Term = tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b())
    e1.SetSubject(subject)
    e2.SetSubject(subject)
    e1.Down(2)
    e2.Down(2)
    
    assertEquals(t, e1.GetRoot(),tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b()))
    assertEquals(t, e1.GetPosition(), sl.MakePositionFromArray([]int{2}))
    assertEquals(t, e1.GetSubject(),tom_TestEnvironment_make_b())
    e1.Up()
    e2.Up()
    
    assertEquals(t, e1.GetRoot(),tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b()))
    assertEquals(t, e1.GetPosition(), sl.MakePosition())
    assertEquals(t, e1.GetSubject(),tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b()))
    e1.Down(1)
    e2.Down(1)
    
    assertEquals(t, e1.GetRoot(),tom_TestEnvironment_make_f(tom_TestEnvironment_make_a(),tom_TestEnvironment_make_b()))
    assertEquals(t, e1.GetPosition(),sl.MakePositionFromArray([]int{1}))
    assertEquals(t, e1.GetSubject(),tom_TestEnvironment_make_a())
}








func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  equal := expected == actual
  if e, ok := expected.(interface{ Equals(any) bool }); ok {
    equal = e.Equals(actual)
  }
  if !equal {
    t.Errorf("expected:<%v> but was:<%v>", expected, actual)
  }
}
