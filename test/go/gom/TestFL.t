package gom

/*
 * Copyright (c) 2006-2015, Universite de Lorraine, Inria
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/


// Port of test/gom/TestFL.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testfl.l.types.*;`.
// port: the hook bodies of the %gom block are Go: `if(c) return x;` is
// `if c { return x }`; the Java Gom methods isEmptyconc, isConsconc,
// getHeadconc, getTailconc, isEmptyor are IsEmptyconc, IsConsconc,
// GetHeadconc, GetTailconc, IsEmptyor; Consconc.make(t, x) is
// ConsConc(t, x).
// port: the *testing.T parameter is named tt where the original's local
// `T t` would shadow it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module l
    abstract syntax
    /* conc is FL, with the default neutral */
    T = a()
      | b()
      | c()
      | conc(T*)
      | and(T*)
      | or(T*)
      | t() | f()
    conc:FL() {}
    and:FL() {}
    or:FL() {}

    or:make_insert(e,l) {
      %match(e) {
        and() -> { return l; } // do not change the value
        and(f()) -> { return `or(f(),l); }
        t() -> { if !l.IsEmptyor() { return `or(t()) } }
        f() -> { return l; }
      }
    }

    and:make_insert(e,l) {
      %match(e) {
        or() -> { return l; } // do not change the value
        or(t()) -> { return `and(t(),l); }
        t() -> { return l; }
        f() -> { if !l.IsEmptyand() { return `and(f()) } }
      }
    }

  }

func TestFlatten1_1(t *testing.T) {
    assertEquals(t, `conc(conc(conc()),conc(conc())),`conc())
}
func TestFlatten1_2(t *testing.T) {
    assertEquals(t, `conc(conc(),a(),conc()),`conc(a()))
}
func TestFlatten1_3(t *testing.T) {
    assertEquals(t, `conc(conc(),conc(conc(),a()),conc(),b(),conc()),`conc(a(),b()))
}
func TestFlatten1_4(t *testing.T) {
    assertEquals(t, `conc(conc(a(),a()),conc(b(),b())),`conc(a(),a(),b(),b()))
}

func TestFlatten2_1(tt *testing.T) {
    var t T = `conc()
    assertTrue(tt, t.IsEmptyconc())
}
func TestFlatten2_2(tt *testing.T) {
    var t T = `conc(conc(),a())
    assertFalse(tt, t.IsEmptyconc())
}
func TestFlatten2_3(tt *testing.T) {
    var t T = `conc(conc(conc(),a()),a())
    assertTrue(tt, t.IsConsconc())
}
func TestFlatten2_4(tt *testing.T) {
    var t T = `conc(conc(conc(conc(),a()),a()),a())
    assertEquals(tt, t.GetHeadconc(),`a())
}
func TestFlatten2_5(tt *testing.T) {
    var t T = `conc(conc(),a())
    assertTrue(tt, t.GetTailconc().IsEmptyconc())
}
func TestFlatten2_6(tt *testing.T) {
    var t T = `conc(
        conc(conc(conc(conc(conc(),a()),a()),a()),a()),
        conc(conc(conc(conc(conc(),a()),a()),a()),a()),
        conc(conc(conc(conc(conc(),a()),a()),a()),a()))
    assertEquals(tt, t,`conc(a(),a(),a(),a(),a(),a(),a(),a(),a(),a(),a(),a()))
}

func TestFlatten3(tt *testing.T) {
    var t T = `conc()
    for i:=0 ; i<10 ; i++ {
      t = ConsConc(t,`a())
    }
    cpt := 0
    for !t.IsEmptyconc() {
      assertEquals(tt, t.GetHeadconc(),`a())
      cpt++
      t = t.GetTailconc()
    }
    assertEquals(tt, cpt,10)
}

func TestMatch1(tt *testing.T) {
    var t T = `conc(conc(conc(),b()),conc(conc()),conc(b(),conc()))
    cnt := 0
    %match(t) {
      conc(b(),_y*) -> { cnt++ }
      conc(_x*,b()) -> { cnt++ }
      conc(b(),_y) -> { cnt++ }
      conc(_x,b()) -> { cnt++ }
      conc(_x,_y) -> { cnt++ }
      conc(b(),x*,b()) -> { if(`x==`conc()) { cnt++ } }
    }
    assertEqualsMsg(tt, "Incomplete matching",cnt,6)
}

func TestHook1_1(t *testing.T) {
    assertEquals(t, `and(t(),t()),`and(t()))
}
func TestHook1_2(t *testing.T) {
    assertEquals(t, `and(t(),f()),`and(f()))
}
func TestHook1_3(t *testing.T) {
    assertEquals(t, `and(f(),t()),`and(f()))
}
func TestHook1_4(t *testing.T) {
    assertEquals(t, `and(f(),f()),`and(f()))
}
func TestHook1_5(t *testing.T) {
    assertEquals(t, `and(t()),`and())
}
func TestHook1_6(t *testing.T) {
    assertEquals(t, `or(t(),t()),`or(t()))
}
func TestHook1_7(t *testing.T) {
    assertEquals(t, `or(t(),f()),`or(t()))
}
func TestHook1_8(t *testing.T) {
    assertEquals(t, `or(f(),t()),`or(t()))
}
func TestHook1_9(t *testing.T) {
    assertEquals(t, `or(f(),f()),`or(f()))
}
func TestHook1_10(t *testing.T) {
    assertEquals(t, `or(f()),`or())
}
func TestHook1_11(t *testing.T) {
    assertEquals(t, `and(or(f(),t()),or(t(),f())),`and(t()))
}
func TestHook1_12(t *testing.T) {
    assertEquals(t, `or(and(f(),t()),and(t(),f())),`or(f()))
}

// org.junit.Assert.assertEquals(expected, actual): the terms are
// maximally shared, so equals is ==.
func assertEquals(t *testing.T, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("expected:<%v> but was:<%v>", expected, actual)
  }
}

// org.junit.Assert.assertEquals(message, expected, actual)
func assertEqualsMsg(t *testing.T, message string, expected, actual any) {
  t.Helper()
  if expected != actual {
    t.Fatalf("%s expected:<%v> but was:<%v>", message, expected, actual)
  }
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
