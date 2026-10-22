package gom

/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the Inria nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */

// Port of test/gom/TestBuiltin.t: the builtin sorts of Gom.
// port: `import gom.b.u.i.l.t.i.n.builtin.types.*;` is the dot import of
// the Go package that gomgo generates from Builtin.gom (sub-directory
// b/u/i/l/t/i/n/builtin); ATermFactory/SingletonFactory.getInstance()
// are aterm.Factory/aterm.GetInstance() (makeList(h,t) is MakeList,
// makeList() MakeListEmpty).
// port: Java's int and long are the Go int64 of Gom's int and long, char
// a rune, double a float64; new Double(1.23) is a float64 and
// a.doubleValue() is a.
// port: new String("Germain") is strings.Clone("Germain"), and Java's ==
// on two Strings (their identity) compares where their bytes are
// (unsafe.StringData), since Go strings have no identity.
// port: res.equals("32") is res == "32"; Wrapper.fromString is
// WrapperFromString; test.toString() is fmt.Sprint(test).
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "fmt"
  "strings"
  "testing"
  "unsafe"

  . "fixture/b/u/i/l/t/i/n/builtin"
  "tom/library/aterm"
)

  %include { b/u/i/l/t/i/n/builtin/Builtin.tom }

  func constructNode(nodeName string, nodeLine int) aterm.ATerm {
    var factory *aterm.Factory = aterm.GetInstance()
    return factory.MakeAppl(factory.MakeAFun(nodeName,1,false),factory.MakeInt(nodeLine))
  }

  func constructList(e1 int, e2 int, e3 int) *aterm.ATermList {
    var factory *aterm.Factory = aterm.GetInstance()
    return factory.MakeList(factory.MakeInt(e1),factory.MakeList(factory.MakeInt(e2),factory.MakeList(factory.MakeInt(e3),factory.MakeListEmpty())))
  }

func TestInt(t *testing.T) {
    var t1 Wrapper = `Int(10)
    var t2 Wrapper = `Int(10)
    assertTrue(t, t1 == t2)
}

func TestString(t *testing.T) {
    var a string = "Germain"
    var b string = strings.Clone("Germain")
    assertFalse(t, unsafe.StringData(a) == unsafe.StringData(b))
    var na Wrapper = `Name(a)
    var nb Wrapper = `Name(b)
    assertTrue(t, na == nb)
}

func TestDouble(t *testing.T) {
    var a float64 = 1.23
    var b float64 = 1.23
    var na Wrapper = `Double(a)
    var nb Wrapper = `Double(b)
    assertTrue(t, na == nb)
}

func TestATerm(t *testing.T) {
    var a aterm.ATerm = constructNode("n1",2)
    var b aterm.ATerm = constructNode("n1",2)
    assertTrue(t, a == b)
    var na Wrapper = `Node(a)
    var nb Wrapper = `Node(b)
    assertTrue(t, na == nb)
}

func TestATermList(t *testing.T) {
    var a *aterm.ATermList = constructList(1,2,3)
    var b *aterm.ATermList = constructList(1,2,3)
    assertTrue(t, a == b)
}

func TestMatchInt(tt *testing.T) {
    // port: the *testing.T parameter is named tt where the original's
    // local `Wrapper t` would shadow it.
    var res string = ""
    var t Wrapper = `Int(32)
    %match(Wrapper t) {
      Int(10) -> { res += "10" }
      Int(32) -> { res += "32" }
    }
    assertTrue(tt, res == "32")
}

func TestMatchLong(tt *testing.T) {
    var res string = ""
    var t Wrapper = `Long(1234567890)
    %match(Wrapper t) {
      Long(32l) -> { res += "32l" }
      Long(1234567890l) -> { res += "1234567890l" }
    }
    assertTrue(tt, res == "1234567890l")
}

func TestMatchChar(tt *testing.T) {
    var res string = ""
    var t Wrapper = `Char('b')
    %match(Wrapper t) {
      Char('a') -> { res += "a" }
      Char('b') -> { res += "b" }
    }
    assertTrue(tt, res == "b")
}

func TestMatchString(t *testing.T) {
    var res string = ""
    var nm Wrapper = `Name("Germain")
    %match(Wrapper nm) {
      Name("Pem") -> { res += "pem" }
      Name("Germain") -> { res += "G" }
    }
    assertTrue(t, res == "G")
}

func TestDisjunctionInt(t *testing.T) {
    var res bool = false
    var wi Wrapper = `Int(42)
    %match(Wrapper wi) {
      (Int|IntBis)(42) -> { res = true }
    }
    assertTrue(t, res)
}

func TestDisjunctionString(t *testing.T) {
    var res bool = false
    var wi Wrapper = `Name("42")
    %match(Wrapper wi) {
      (Name|NameBis)("42") -> { res = true }
    }
    assertTrue(t, res)
}

func TestBoolean(t *testing.T) {
    var t1 Wrapper = `Bool(true)
    var t2 Wrapper = `Bool(true)
    assertTrue(t, t1 == t2)

    t1 = `Bool(false)
    t2 = `Bool(false)
    assertTrue(t, t1 == t2)
}

func TestMatchBoolean(t *testing.T) {
    var res bool = false
    var nm Wrapper = `Bool(true())
    %match(Wrapper nm) {
      Bool(false()) -> { res = false }
      Bool(true()) -> { res = true }
    }
    assertTrueMsg(t, "Match in the second branch",res)

    nm = `Bool(false())
    %match(Wrapper nm) {
      Bool(false()) -> { res = false }
      Bool(true()) -> { res = true }
    }
    assertFalseMsg(t, "Match in the first branch",res)
}

func TestFromString(t *testing.T) {
    var s string = "Bool(1)"
    var test Wrapper = WrapperFromString(s)
    assertEquals(t, "Terms are equals", test, `Bool(true()))
    assertEquals(t, "String representations are equals",fmt.Sprint(test),
        "Bool(1)")
}

func TestFrom_int(t *testing.T) {
    var test Wrapper = `Int(123456)
    var testBis Wrapper = WrapperFromString(fmt.Sprint(test))
    assertEquals(t, "", test,testBis)
}

// org.junit.Assert.assertTrue(condition)
func assertTrue(t *testing.T, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertFalse(condition)
func assertFalse(t *testing.T, condition bool) {
  t.Helper()
  if condition {
    t.Fatal()
  }
}

// org.junit.Assert.assertTrue(message, condition)
func assertTrueMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if !condition {
    t.Fatal(message)
  }
}

// org.junit.Assert.assertFalse(message, condition)
func assertFalseMsg(t *testing.T, message string, condition bool) {
  t.Helper()
  if condition {
    t.Fatal(message)
  }
}

// org.junit.Assert.assertEquals(message, expected, actual): the terms
// are maximally shared, so equals is ==.
func assertEquals(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
}
