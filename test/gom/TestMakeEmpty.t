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
import gom.testmakeempty.l.types.*;

public class TestMakeEmpty extends TestCase {

  %gom {
    module l
    abstract syntax
    T = a()
      | b()
      | conc(T*)
    conc:make_empty() {
      return `a();
    }
    L = list(T*)
      | c()
    list:make_empty() {
      return `c();
    }
  }
  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestMakeEmpty.class));
  }

  public void testEmptySameDoCo() {
    assertEquals(`a(),`conc());
  }

  public void testEmptyNotSameDoCo() {
    assertEquals(`c(),`list());
  }

}
