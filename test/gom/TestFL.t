/*
 * Copyright (c) 2006-2014, Universite de Lorraine, Inria
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
import gom.testfl.l.types.*;

public class TestFL {

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
        t() -> { if(!l.isEmptyor()) return `or(t()); }
        f() -> { return l; }
      }
    }

    and:make_insert(e,l) {
      %match(e) {
        or() -> { return l; } // do not change the value
        or(t()) -> { return `and(t(),l); }
        t() -> { return l; }
        f() -> { if(!l.isEmptyand()) return `and(f()); }
      }
    }

  }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestFL.class.getName());
  }

  @Test
  public void testFlatten1_1() {
    assertEquals(`conc(conc(conc()),conc(conc())),`conc());
  }
  @Test
  public void testFlatten1_2() {
    assertEquals(`conc(conc(),a(),conc()),`conc(a()));
  }
  @Test
  public void testFlatten1_3() {
    assertEquals(`conc(conc(),conc(conc(),a()),conc(),b(),conc()),`conc(a(),b()));
  }
  @Test
  public void testFlatten1_4() {
    assertEquals(`conc(conc(a(),a()),conc(b(),b())),`conc(a(),a(),b(),b()));
  }

  @Test
  public void testFlatten2_1() {
    T t = `conc();
    assertTrue(t.isEmptyconc());
  }
  @Test
  public void testFlatten2_2() {
    T t = `conc(conc(),a());
    assertFalse(t.isEmptyconc());
  }
  @Test
  public void testFlatten2_3() {
    T t = `conc(conc(conc(),a()),a());
    assertTrue(t.isConsconc());
  }
  @Test
  public void testFlatten2_4() {
    T t = `conc(conc(conc(conc(),a()),a()),a());
    assertEquals(t.getHeadconc(),`a());
  }
  @Test
  public void testFlatten2_5() {
    T t = `conc(conc(),a());
    assertTrue(t.getTailconc().isEmptyconc());
  }
  @Test
  public void testFlatten2_6() {
    T t = `conc(
        conc(conc(conc(conc(conc(),a()),a()),a()),a()),
        conc(conc(conc(conc(conc(),a()),a()),a()),a()),
        conc(conc(conc(conc(conc(),a()),a()),a()),a()));
    assertEquals(t,`conc(a(),a(),a(),a(),a(),a(),a(),a(),a(),a(),a(),a()));
  }

  @Test
  public void testFlatten3() {
    T t = `conc();
    for(int i=0 ; i<10 ; i++) {
      t = gom.testfl.l.types.t.Consconc.make(t,`a());
    }
    int cpt = 0;
    while(!t.isEmptyconc()) {
      assertEquals(t.getHeadconc(),`a());
      cpt++;
      t = t.getTailconc();
    }
    assertEquals(cpt,10);
  }

  @Test
  public void testMatch1() {
    T t = `conc(conc(conc(),b()),conc(conc()),conc(b(),conc()));
    int cnt = 0;
    %match(t) {
      conc(b(),_y*) -> { cnt++; }
      conc(_x*,b()) -> { cnt++; }
      conc(b(),_y) -> { cnt++; }
      conc(_x,b()) -> { cnt++; }
      conc(_x,_y) -> { cnt++; }
      conc(b(),x*,b()) -> { if(`x==`conc()) cnt++; }
    }
    assertEquals("Incomplete matching",cnt,6);
  }

  @Test
  public void testHook1_1() {
    assertEquals(`and(t(),t()),`and(t()));
  }
  @Test
  public void testHook1_2() {
    assertEquals(`and(t(),f()),`and(f()));
  }
  @Test
  public void testHook1_3() {
    assertEquals(`and(f(),t()),`and(f()));
  }
  @Test
  public void testHook1_4() {
    assertEquals(`and(f(),f()),`and(f()));
  }
  @Test
  public void testHook1_5() {
    assertEquals(`and(t()),`and());
  }
  @Test
  public void testHook1_6() {
    assertEquals(`or(t(),t()),`or(t()));
  }
  @Test
  public void testHook1_7() {
    assertEquals(`or(t(),f()),`or(t()));
  }
  @Test
  public void testHook1_8() {
    assertEquals(`or(f(),t()),`or(t()));
  }
  @Test
  public void testHook1_9() {
    assertEquals(`or(f(),f()),`or(f()));
  }
  @Test
  public void testHook1_10() {
    assertEquals(`or(f()),`or());
  }
  @Test
  public void testHook1_11() {
    assertEquals(`and(or(f(),t()),or(t(),f())),`and(t()));
  }
  @Test
  public void testHook1_12() {
    assertEquals(`or(and(f(),t()),and(t(),f())),`or(f()));
  }
}
