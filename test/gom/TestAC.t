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
import gom.testac.ac.types.*;

public class TestAC extends TestCase {

  %gom {
    module ac 
    abstract syntax
    /* p is variadic, but not AC */
    NAU = m()
        | n()
        | p(NAU*)
    /* conc is AC, with the default neutral */
    T = a()
      | b()
      | c()
      | d()
      | conc(T*)
    conc:AC() {}
    /* list is AC, with aa() as neutral */
    L = aa()
      | bb()
      | cc()
      | dd()
      | list(L*)
    list:AC() { `aa() }
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestAC.class));
  }

  /* First make sure simple test of the AU functionality do pass */
  public void testFlatten1() {
    assertEquals(`conc(a(),b(),a()),`conc(a(),conc(b()),a()));
  }

  public void testFlatten2() {
    L l1 = `list(bb(),bb());
    L l2 = `list(bb(),bb());
    assertEquals(`list(l1,l2),`list(bb(),bb(),bb(),bb()));
  }

  public void testFlatten3() {
    L l1 = `list(bb(),bb());
    L l2 = `list(bb(),bb());
    assertEquals(`list(l1,l2),`list(l1*,l2*));
  }

  public void testFlatten4() {
    L l1 = `list(bb(),bb());
    assertEquals(`Conslist(l1,bb()),`list(l1*,bb()));
  }

  public void testNeutral1() {
    assertEquals(`list(),`aa());
  }

  public void testNeutral2() {
    assertEquals(`list(aa(),aa()),`aa());
  }

  public void testNeutral3() {
    L l = `bb();
    int cnt = 0;
    %match(l) {
      list?(x*,y*) -> {
        if(`x == `bb() && `y == `aa()) {
          cnt += 2;
        }
        if(`x == `aa() && `y == `bb()) {
          cnt += 3;
        }
      }
    }
    if (0 == cnt) {
      fail("list(x,y) should match bb()");
    }
    assertEquals("Incomplete matching",cnt,5);
  }

  public void testNeutral4() {
    T t = `b();
    int cnt = 0;
    %match(t) {
      conc?(x*,y*) -> {
        if (`x == `b() && `y == `conc()) {
          cnt += 2;
        }
        if (`x == `conc() && `y == `b()) {
          cnt += 3;
        }
      }
    }
    if (0 == cnt) {
      fail(" conc(x,y) should match b()");
    }
    assertEquals("Incomplete matching",cnt,5);
  }

  public void testNeutral5() {
    NAU t = `m();
    int cnt = 0;
    %match(t) {
      p(x*,y*) -> {
        cnt += 2;
      }
    }
    assertEquals("m() does not match p(x*,y*)",cnt,0);
  }

  public void testOrder() {
    T t1 = `conc(a(), b(), c(), d());    
    T t2 = `conc(b(), d(), a(), c());    
    T t3 = `conc(d(), b(), c(), a());    
    assertEquals(t1,t2);
    assertEquals(t2,t3);
  }

  public void testOrderFlatten() {
    T t1 = `conc(a(), b(), conc(b(), a(), c()), c(), d());    
    T t2 = `conc(b(), conc(a(), b(), c()), d(), a(), c());    
    T t3 = `conc(d(), b(), c(), a(), conc(c(), b(), a()));    
    assertEquals(t1,t2);
    assertEquals(t2,t3);
  }

  public void testOrderN() {
    L t1 = `list(aa(), bb(), cc(), dd());    
    L t2 = `list(bb(), dd(), aa(), cc());    
    L t3 = `list(dd(), bb(), cc(), aa());    
    assertEquals(t1,t2);
    assertEquals(t2,t3);
  }
}
