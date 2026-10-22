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


// Port of test/gom/TestACU.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testacu.acu.types.*;`.
// port: `x == y` on terms is == on the Go interfaces (identity of the
// shared terms, as in Java).
// port: the *testing.T parameter is named tt where the original's local
// `T t` would shadow it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module acu
    abstract syntax
    /* p is variadic, but not ACU */
    NAU = m()
        | n()
        | p(NAU*)
    /* conc is ACU, with the default neutral */
    T = a()
      | b()
      | c()
      | d()
      | conc(T*)
    conc:ACU() {}
    /* list is ACU, with aa() as neutral */
    L = aa()
      | bb()
      | cc()
      | dd()
      | list(L*)
    list:ACU() { `aa() }
  }

  /* First make sure simple test of the AU functionality do pass */
func TestFlatten1(t *testing.T) {
    assertEquals(t, `conc(a(),b(),a()),`conc(a(),conc(b()),a()))
}

func TestFlatten2(t *testing.T) {
    var l1 L = `list(bb(),bb())
    var l2 L = `list(bb(),bb())
    assertEquals(t, `list(l1,l2),`list(bb(),bb(),bb(),bb()))
}

func TestFlatten3(t *testing.T) {
    var l1 L = `list(bb(),bb())
    var l2 L = `list(bb(),bb())
    assertEquals(t, `list(l1,l2),`list(l1*,l2*))
}

func TestFlatten4(t *testing.T) {
    var l1 L = `list(bb(),bb())
    assertEquals(t, `Conslist(l1,bb()),`list(l1*,bb()))
}

func TestNeutral1(t *testing.T) {
    assertEquals(t, `list(),`aa())
}

func TestNeutral2(t *testing.T) {
    assertEquals(t, `list(aa(),aa()),`aa())
}

func TestNeutral3(t *testing.T) {
    var l L = `bb()
    cnt := 0
    %match(l) {
      list?(x*,y*) -> {
        if(`x == `bb() && `y == `aa()) {
          cnt += 2
        }
        if(`x == `aa() && `y == `bb()) {
          cnt += 3
        }
      }
    }
    if (0 == cnt) {
      fail(t, "list(x,y) should match bb()")
    }
    assertEqualsMsg(t, "Incomplete matching",cnt,5)
}

func TestNeutral4(tt *testing.T) {
    var t T = `b()
    cnt := 0
    %match(t) {
      conc?(x*,y*) -> {
        if (`x == `b() && `y == `conc()) {
          cnt += 2
        }
        if (`x == `conc() && `y == `b()) {
          cnt += 3
        }
      }
    }
    if (0 == cnt) {
      fail(tt, " conc(x,y) should match b()")
    }
    assertEqualsMsg(tt, "Incomplete matching",cnt,5)
}

func TestNeutral5(tt *testing.T) {
    var t NAU = `m()
    cnt := 0
    %match(t) {
      p(_x*,_y*) -> {
        cnt += 2
      }
    }
    assertEqualsMsg(tt, "m() does not match p(x*,y*)",cnt,0)
}

func TestOrder1(t *testing.T) {
    var t1 T = `conc(a(), b(), c(), d())    
    var t2 T = `conc(b(), d(), a(), c())    
    assertEquals(t, t1,t2)
}

func TestOrder2(t *testing.T) {
    var t2 T = `conc(b(), d(), a(), c())    
    var t3 T = `conc(d(), b(), c(), a())    
    assertEquals(t, t2,t3)
}

func TestOrderFlatten1(t *testing.T) {
    var t1 T = `conc(a(), b(), conc(b(), a(), c()), c(), d())    
    var t2 T = `conc(b(), conc(a(), b(), c()), d(), a(), c())    
    assertEquals(t, t1,t2)
}

func TestOrderFlatten2(t *testing.T) {
    var t2 T = `conc(b(), conc(a(), b(), c()), d(), a(), c())    
    var t3 T = `conc(d(), b(), c(), a(), conc(c(), b(), a()))    
    assertEquals(t, t2,t3)
}

func TestOrderN1(t *testing.T) {
    var t1 L = `list(aa(), bb(), cc(), dd())    
    var t2 L = `list(bb(), dd(), aa(), cc())    
    assertEquals(t, t1,t2)
}

func TestOrderN2(t *testing.T) {
    var t2 L = `list(bb(), dd(), aa(), cc())    
    var t3 L = `list(dd(), bb(), cc(), aa())    
    assertEquals(t, t2,t3)
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

// org.junit.Assert.fail(message)
func fail(t *testing.T, message string) {
  t.Helper()
  t.Fatal(message)
}
