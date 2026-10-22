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


// Port of test/gom/TestAU.t.
// port: the generated %gom types live in this package, so there is no
// counterpart to `import gom.testau.l.types.*;`.
// port: `x == y` on terms is == on the Go interfaces (identity of the
// shared terms, as in Java).
// port: the *testing.T parameter is named tt where the original's local
// `T t` would shadow it.
// port: no main — `go test` runs the tests (the original main only launched JUnit).

import (
  "testing"
)

  %gom {
    module l
    abstract syntax
    /* p is variadic, but not AU */
    NAU = m()
        | n()
        | p(NAU*)
    /* conc is AU, with the default neutral */
    T = a()
      | b()
      | concAU(T*)
      | conc(T*)
    concAU:AU() {}
    conc:Free() {}
    /* list is AU, with aa() as neutral */
    L = aa()
      | bb()
      | listAU(L*)
      | list(L*)
    listAU:AU() { `aa() }
    list:Free() { }

//    module l:rules() {
//      conc(conc(),x) -> x 
//      conc(x,conc()) -> x 
//   }
  }

func TestFlatten1(t *testing.T) {
    assertEquals(t, `concAU(a(),b(),a()),`concAU(a(),concAU(b()),a()))
}

func TestFlatten2(t *testing.T) {
    var l1 L = `listAU(bb(),bb())
    var l2 L = `listAU(bb(),bb())
    assertEquals(t, `listAU(l1,l2),`listAU(bb(),bb(),bb(),bb()))
}

func TestFlatten3(t *testing.T) {
    var l1 L = `listAU(bb(),bb())
    var l2 L = `listAU(bb(),bb())
    assertEquals(t, `listAU(l1,l2),`listAU(l1*,l2*))
}

func TestFlatten4(t *testing.T) {
    var l1 L = `listAU(bb(),bb())
    assertEquals(t, `ConslistAU(l1,bb()),`listAU(l1*,bb()))
}

func TestNeutral1(t *testing.T) {
    assertEquals(t, `listAU(),`aa())
}

func TestNeutral2(t *testing.T) {
    assertEquals(t, `listAU(aa(),aa()),`aa())
}

func TestNeutral3(t *testing.T) {
    var l L = `bb()
    cnt := 0
    %match(l) {
      listAU?(x*,y*) -> {
        if(`x == `bb() && `y == `aa()) {
          cnt += 2
        }
        if(`x == `aa() && `y == `bb()) {
          cnt += 3
        }
      }
    }
    if (0 == cnt) {
      fail(t, "listAU(x,y) should match bb()")
    }
    assertEqualsMsg(t, "Incomplete matching",cnt,5)
}

func TestNeutral4(tt *testing.T) {
    var t T = `b()
    cnt := 0
    %match(t) {
      concAU?(x*,y*) -> {
        if (`x == `b() && `y == `concAU()) {
          cnt += 2
        }
        if (`x == `concAU() && `y == `b()) {
          cnt += 3
        }
      }
    }
    if (0 == cnt) {
      fail(tt, " concAU(x,y) should match b()")
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

func TestMatch1(t *testing.T) {
    var l L = `listAU(listAU(aa(),bb()),listAU(aa()),listAU(bb(),aa()))
    cnt := 0
    %match(l) {
      listAU(bb(),_y*) -> { cnt++ }
      listAU(_x*,bb()) -> { cnt++ }
      listAU(bb(),_y) -> { cnt++ }
      listAU(_x,bb()) -> { cnt++ }
      listAU(_x,_y) -> { cnt++ }
      listAU(bb(),x*,bb()) -> { if(`x==`aa()) { cnt++ } }
    }
    assertEqualsMsg(t, "Incomplete matching",cnt,6)
}

  /*
     * I am not sure we want this to be correct

  public void testMatch1() {
    L l = `listAU(bb(),bb());
    int cnt = 0;
    %match(l) {
      listAU(aa(),y*) -> { cnt++; }
      listAU(x*,aa()) -> { cnt++; }
    }
    if (0 == cnt) {
      fail("listAU(aa,y) should match listAU(bb(),bb())");
    }
    assertEquals("Incomplete matching",cnt,2);
  }

  public void testMatch2() {
    L l = `bb();
    int cnt = 0;
    %match(l) {
      listAU?(aa(),y*) -> { cnt++; }
      listAU?(x*,aa()) -> { cnt++; }
    }
    if (0 == cnt) {
      fail("listAU?(aa,y) should match listAU(bb())");
    }
    assertEquals("Incomplete matching",cnt,2);
  }
  */

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
