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

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.extensions.ActiveTestSuite;
import junit.extensions.RepeatedTest;
import gom.testgommultithread.term.types.*;

public class TestGomMultithread extends TestCase {

  %gom() {
    module term
    abstract syntax
    T = a()
      | b()
      | c()
      | f(One:T,Two:T,Three:T,Four:T)
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public TestGomMultithread(String name) {
    super(name);
  }

  public static Test suite() {
    TestSuite suite = new ActiveTestSuite();
    for (int i = 0; i<10; ++i) {
      suite.addTest(new  RepeatedTest(new TestGomMultithread("testBuild"),10));
    }
    return suite;
  }

  private static int DEPTH = 4;
  public void testBuild() {
    T res = `a();
    for(int i = 0; i < DEPTH; ++i) {
      res = `f(res, b(), c(), res);
    }
    T res2 = `a();
    for(int i = 0; i < DEPTH; ++i) {
      res2 = `f(res2, b(), c(), res2);
    }
    assertSame("Should be equal", res,res2);
  }
}
