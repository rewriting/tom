package gom
















import (
  "bytes"
  "fmt"
  "io"
  "testing"

  . "fixture/importing"
  "tom/library/aterm"
)

  func tom_equal_term_String(t1 string, t2 string) bool { return t1 == t2;  }; func tom_is_sort_String(t string) bool { return true;  }; func tom_equal_term_Leaf(t1  Leaf , t2  Leaf ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Leaf(t any) bool { return func() bool { _, ok := (t).(Leaf); return ok }();  }; func tom_equal_term_Inner(t1  Inner , t2  Inner ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Inner(t any) bool { return func() bool { _, ok := (t).(Inner); return ok }();  }; func tom_equal_term_Out(t1  Out , t2  Out ) bool { return (t1).Equivalent(t2);  }; func tom_is_sort_Out(t any) bool { return func() bool { _, ok := (t).(Out); return ok }();  }; 

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




func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}




func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
