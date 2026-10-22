package gom

// Port of test/gom/TestFrom.t: Java Gom's fromTerm, fromString and
// fromStream.
// port: `import gom.importing.types.*;` and `import
// gom.imported.types.*;` are the dot import of the Go package that gomgo
// generates from Importing.gom and the modules it imports (sub-directory
// importing).
// port: aterm.pure.SingletonFactory.getInstance() is
// aterm.GetInstance(); the static Out.fromTerm, Out.fromString and
// Out.fromStream are OutFromTerm, OutFromString and OutFromStream (whose
// error is Java's IOException); test.toString() is fmt.Sprint(test);
// java.io.ByteArrayInputStream is bytes.Reader.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "bytes"
  "fmt"
  "io"
  "testing"

  . "fixture/importing"
  "tom/library/aterm"
)

  %include { importing/Importing.tom }

func TestFromTerm(t *testing.T) {
    var trm aterm.ATerm =
      aterm.GetInstance().Parse(
          "Loop(Loop(Pack(Atom),Element(Atom)),Element(Element(Atom)))")
    var test Out = OutFromTerm(trm)
    assertEquals(t, fmt.Sprint(test),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))")
}

func TestFromString(t *testing.T) {
    var s string = "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))"
    var test Out = OutFromString(s)
    assertEquals(t, fmt.Sprint(test),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))")
}

func TestFromStream(t *testing.T) {
    var s string = "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))"
    var stream io.Reader = nil
    var test Out = nil
    stream = bytes.NewReader([]byte(s))
    if r, err := OutFromStream(stream); err == nil {
      test = r
    } else {
      fail(t, "Streaming problem")
    }
    assertEquals(t, fmt.Sprint(test),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))")
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
