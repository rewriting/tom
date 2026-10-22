package sl










import (
  "testing"

  "tom/library/sl"
)



func TestNormalize(t *testing.T){
  var p *sl.Position = sl.MakePositionFromArray([]int{1,-1,2,-2,1,2,3,-3,1,1})
  var pp sl.Path = p.GetCanonicalPath()
  assertEquals(t, pp,sl.MakePositionFromArray([]int{1,2,1,1}))
}



func TestAdd(t *testing.T){
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1,2,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,2,1,2,1})
  var r sl.Path = p2.Add(p1)
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{1,2,1,2,1,1,1,2,1}))
}



func TestInverse(t *testing.T){
  var p *sl.Position = sl.MakePositionFromArray([]int{1,1,2})
  var r sl.Path = p.Inverse()
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{-2,-1,-1}))
}



func TestSub(t *testing.T){
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1,2,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,2,1,2,1})
  var r sl.Path = p2.Sub(p1)
  r = r.GetCanonicalPath()
  assertEquals(t, r,sl.MakePositionFromArray([]int{-1, -2, -1, 2, 1, 2, 1}))
}



func TestEquals(t *testing.T) {
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  assertEqualsMsg(t, "Similar objects should be equal" ,p1,p2)
}



func TestHashCode(t *testing.T) {
  var p1 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  var p2 *sl.Position = sl.MakePositionFromArray([]int{1,1})
  assertEqualsMsg(t, "HashCode for similar objects should be equal",
      p1.HashCode(),p2.HashCode())
}






func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  assertEqualsMsg(t, "", expected, actual)
}




func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  equal := expected == actual
  if e, ok := expected.(interface{ Equals(any) bool }); ok {
    equal = e.Equals(actual)
  }
  if !equal {
    t.Errorf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
