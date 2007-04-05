/*
 * Copyright (C) 2006-2007, INRIA
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

import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.testfl.l.types.*;

public class TestFL extends TestCase {

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
    junit.textui.TestRunner.run(new TestSuite(TestFL.class));
  }

  public void testFlatten1() {
    assertEquals(`conc(conc(conc()),conc(conc())),`conc());
    assertEquals(`conc(conc(),a(),conc()),`conc(a()));
    assertEquals(`conc(conc(),conc(conc(),a()),conc(),b(),conc()),`conc(a(),b()));
    assertEquals(`conc(conc(a(),a()),conc(b(),b())),`conc(a(),a(),b(),b()));
  }

  public void testFlatten2() {
    T t = `conc();
    assertTrue(t.isEmptyconc());
    t = `conc(t,a());
    assertFalse(t.isEmptyconc());
    assertTrue(t.isConsconc());
    assertEquals(t.getHeadconc(),`a());
    assertTrue(t.getTailconc().isEmptyconc());
    t = `conc(t,t,t);
    assertEquals(t,`conc(a(),a(),a()));
  }

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

  public void testMatch1() {
    T t = `conc(conc(conc(),b()),conc(conc()),conc(b(),conc()));
    int cnt = 0;
    %match(t) {
      conc(b(),y*) -> { cnt++; }
      conc(x*,b()) -> { cnt++; }
      conc(b(),y) -> { cnt++; }
      conc(x,b()) -> { cnt++; }
      conc(x,y) -> { cnt++; }
      conc(b(),x*,b()) -> { if(`x==`conc()) cnt++; }
    }
    assertEquals("Incomplete matching",cnt,6);
  }

  public void testHook1() {
    assertEquals(`and(t(),t()),`and(t()));
    assertEquals(`and(t(),f()),`and(f()));
    assertEquals(`and(f(),t()),`and(f()));
    assertEquals(`and(f(),f()),`and(f()));
    assertEquals(`and(t()),`and());

    assertEquals(`or(t(),t()),`or(t()));
    assertEquals(`or(t(),f()),`or(t()));
    assertEquals(`or(f(),t()),`or(t()));
    assertEquals(`or(f(),f()),`or(f()));
    assertEquals(`or(f()),`or());

    assertEquals(`and(or(f(),t()),or(t(),f())),`and(t()));
    assertEquals(`or(and(f(),t()),and(t(),f())),`or(f()));
  }
}
