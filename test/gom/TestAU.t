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

package gom;

import static org.junit.Assert.*;
import org.junit.Test;
import gom.testau.l.types.*;

public class TestAU {

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
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAU.class.getName());
  }

  @Test
  public void testFlatten1() {
    assertEquals(`concAU(a(),b(),a()),`concAU(a(),concAU(b()),a()));
  }

  @Test
  public void testFlatten2() {
    L l1 = `listAU(bb(),bb());
    L l2 = `listAU(bb(),bb());
    assertEquals(`listAU(l1,l2),`listAU(bb(),bb(),bb(),bb()));
  }

  @Test
  public void testFlatten3() {
    L l1 = `listAU(bb(),bb());
    L l2 = `listAU(bb(),bb());
    assertEquals(`listAU(l1,l2),`listAU(l1*,l2*));
  }

  @Test
  public void testFlatten4() {
    L l1 = `listAU(bb(),bb());
    assertEquals(`ConslistAU(l1,bb()),`listAU(l1*,bb()));
  }

  @Test
  public void testNeutral1() {
    assertEquals(`listAU(),`aa());
  }

  @Test
  public void testNeutral2() {
    assertEquals(`listAU(aa(),aa()),`aa());
  }

  @Test
  public void testNeutral3() {
    L l = `bb();
    int cnt = 0;
    %match(l) {
      listAU?(x*,y*) -> {
        if(`x == `bb() && `y == `aa()) {
          cnt += 2;
        }
        if(`x == `aa() && `y == `bb()) {
          cnt += 3;
        }
      }
    }
    if (0 == cnt) {
      fail("listAU(x,y) should match bb()");
    }
    assertEquals("Incomplete matching",cnt,5);
  }

  @Test
  public void testNeutral4() {
    T t = `b();
    int cnt = 0;
    %match(t) {
      concAU?(x*,y*) -> {
        if (`x == `b() && `y == `concAU()) {
          cnt += 2;
        }
        if (`x == `concAU() && `y == `b()) {
          cnt += 3;
        }
      }
    }
    if (0 == cnt) {
      fail(" concAU(x,y) should match b()");
    }
    assertEquals("Incomplete matching",cnt,5);
  }

  @Test
  public void testNeutral5() {
    NAU t = `m();
    int cnt = 0;
    %match(t) {
      p(_x*,_y*) -> {
        cnt += 2;
      }
    }
    assertEquals("m() does not match p(x*,y*)",cnt,0);
  }

  @Test
  public void testMatch1() {
    L l = `listAU(listAU(aa(),bb()),listAU(aa()),listAU(bb(),aa()));
    int cnt = 0;
    %match(l) {
      listAU(bb(),_y*) -> { cnt++; }
      listAU(_x*,bb()) -> { cnt++; }
      listAU(bb(),_y) -> { cnt++; }
      listAU(_x,bb()) -> { cnt++; }
      listAU(_x,_y) -> { cnt++; }
      listAU(bb(),x*,bb()) -> { if(`x==`aa()) cnt++; }
    }
    assertEquals("Incomplete matching",cnt,6);
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
}
