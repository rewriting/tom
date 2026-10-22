package gom

// Port of test/gom/TestYingYang.t.
// port: `import gom.ying.types.*;` and `import gom.yang.types.*;` are
// the dot import of the Go package that gomgo generates from Ying.gom:
// Go packages cannot import each other cyclically, so the mutually
// importing modules Ying and Yang are generated in the same package
// (sub-directory ying).
// port: m.toString() is fmt.Sprint(m).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "testing"

  . "fixture/ying"
)

  %include { ying/Ying.tom }

func TestToString(t *testing.T) {
    var m Moon = `ping(pong(ping(shi()))) 
    assertEquals(t, "ping(pong(ping(shi())))",fmt.Sprint(m))
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}
