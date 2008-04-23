/*
 * Copyright (c) 2006-2008, INRIA
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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.extensions.ActiveTestSuite;
import junit.extensions.RepeatedTest;
import gom.testgomwithoutmaximalsharing.term.types.*;
import tom.library.sl.*;

public class TestGomWithoutMaximalSharing extends TestCase {

  %gom(--nosharing) {
    module term
    abstract syntax
    T = a()
      | b()
      | c()
      | f(One:T,Two:T)
      | g(One:T)
  }

  %include{ sl.tom }

  public final static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestGomWithoutMaximalSharing.class));
  }

  public void testBuild() {
    T t1 = `f(g(a()),g(b()));
    T t2 = `f(g(a()),g(b()));
    assertNotSame("Should be not the same", t1, t2);
    assertEquals("Should be equal", t1, t2);
  }


  public void testMatch() {
    T t = `f(g(a()),g(b()));
    %match (t) {
      f(x,y) -> {
        assertEquals("Should be equal", `x, `g(a()));
        assertEquals("Should be equal", `y, `g(b()));
      }
    }
  }


  %strategy Transform() extends Identity() {
    visit T {
      a() -> c()
      b() -> c()
    }
  }


  public void testStrategy() {
    T t = `f(g(a()),g(b()));
    try {
    assertEquals("Should be equal", `TopDown(Transform()).visit(t), `f(g(c()),g(c())));
    assertEquals("Should be equal", `TopDown(Transform()).visitLight(t), `f(g(c()),g(c())));
    } catch (VisitFailure e) {
      fail();
    }
  }


  public void testIdentity() {
    T t = `f(g(a()),g(b()));
    try {
      assertSame("Should be equal", `TopDown(Identity()).visit(t), t);
      assertSame("Should be equal", `TopDown(Identity()).visitLight(t), t); 
    } catch (VisitFailure e) {
      fail();
    }
  }

}
