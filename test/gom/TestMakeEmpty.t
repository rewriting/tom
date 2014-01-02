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
import gom.testmakeempty.l.types.*;

public class TestMakeEmpty {

  %gom {
    module l
    abstract syntax
    T = a()
      | b()
      | conc(T*)
    conc:FL() {}
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
    org.junit.runner.JUnitCore.main(TestMakeEmpty.class.getName());
  }

  @Test
  public void testEmptySameDoCo() {
    assertSame(`a(),`conc());
  }

  @Test
  public void testEmptyNotSameDoCo() {
    assertSame(`c(),`list());
  }

  //public void testOneElemSameDoCo() {
  //  assertSame(`conc(b(),a()),`conc(b()));
  //}

  //public void testOneElemNotSameDoCo() {
  //  assertSame(`list(b(),c()),`list(b()));
  //}
}
