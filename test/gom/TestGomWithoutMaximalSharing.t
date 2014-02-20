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
import gom.testgomwithoutmaximalsharing.term.types.*;
import tom.library.sl.*;

public class TestGomWithoutMaximalSharing {

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
    org.junit.runner.JUnitCore.main(TestGomWithoutMaximalSharing.class.getName());
  }

  @Test
  public void testBuild() {
    T t1 = `f(g(a()),g(b()));
    T t2 = `f(g(a()),g(b()));
    assertNotSame("Should be not the same", t1, t2);
    assertTrue("Should be equivalent", t1.deepEquals(t2));
  }


  @Test
  public void testMatch() {
    T t = `f(g(a()),g(b()));
    %match (t) {
      f(x,y) -> {
        assertTrue("Should be equivalent", `x.deepEquals(`g(a())));
        assertTrue("Should be equivalent", `y.deepEquals(`g(b())));
      }
    }
  }


  %strategy Transform() extends Identity() {
    visit T {
      a() -> c()
      b() -> c()
    }
  }


  @Test
  public void testStrategy() {
    T t = `f(g(a()),g(b()));
    try {
    assertTrue("Should be equivalent", `f(g(c()),g(c())).deepEquals(`TopDown(Transform()).visit(t)));
    assertTrue("Should be equivalent", `f(g(c()),g(c())).deepEquals(`TopDown(Transform()).visitLight(t)));
    } catch (VisitFailure e) {
      fail();
    }
  }


  @Test
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
