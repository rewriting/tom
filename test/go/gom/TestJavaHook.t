package gom

// Port of test/gom/TestJavaHook.t.
// port: `import gom.javahook.types.*;` is the dot import of the Go
// package that gomgo generates from JavaHook.gom (sub-directory
// javahook); its hooks are installed by the included mapping. The hook
// bodies of JavaHook.gom are Java host code: its copy has them in Go
// (System.out.println(x) is fmt.Println(x), "…"+t is
// "…"+fmt.Sprint(t)), the rest of the file is unchanged.
// port: System.setOut(new PrintStream(res)) redirects os.Stdout to a
// pipe (captureStdout below); line.separator is "\n".
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "io"
  "os"
  "testing"

  . "fixture/javahook"
)

  %include { javahook/JavaHook.tom }

  var newline string = "\n"

// captureStdout runs f with os.Stdout redirected and returns what f
// printed (System.setOut(new PrintStream(res)) … System.setOut(tmpOut)).
func captureStdout(t *testing.T, f func()) string {
  t.Helper()
  tmpOut := os.Stdout
  r, w, err := os.Pipe()
  if err != nil {
    t.Fatal(err)
  }
  os.Stdout = w
  done := make(chan string)
  go func() { b, _ := io.ReadAll(r); done <- string(b) }()
  f()
  w.Close()
  os.Stdout = tmpOut
  return <-done
}

func TestEmptyMakeHook(t *testing.T) {
    res := captureStdout(t, func() {
      _ = `EmptyTerm()
    })
    assertEquals(t, res,"EmptyTerm()" + newline)
}

func TestUnaryMakeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = `Unary(EmptyTerm())
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()"+newline+"Unary(EmptyTerm())" + newline)
}

func TestBinaryMakeBeforeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = `Binary(EmptyTerm(),EmptyTerm())
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()" + newline + "EmptyTerm()" + newline + "Binary(EmptyTerm(),EmptyTerm())" + newline )
}

func TestTernaryMakeBeforeHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = `Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())))
      _ = empty
    })
    assertEquals(t, res,
        "EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"EmptyTerm()"+newline +"Unary(EmptyTerm())"+newline +"Binary(EmptyTerm(),Unary(EmptyTerm()))"+newline +"Ternary(EmptyTerm(),EmptyTerm(),Binary(EmptyTerm(),Unary(EmptyTerm())))" + newline)
}

func TestVaryMakeInsertHook(t *testing.T) {
    res := captureStdout(t, func() {
      var empty Term = `Vary(EmptyTerm(),EmptyTerm(),EmptyTerm(),EmptyTerm())
      _ = empty
    })
    assertEquals(t, res,"EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "EmptyTerm()"+ newline +
        "inserting(EmptyTerm(),Vary())" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm()))" + newline +
        "inserting(EmptyTerm(),Vary(EmptyTerm(),EmptyTerm(),EmptyTerm()))" + newline)
}

// org.junit.Assert.assertEquals(expected, actual)
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

