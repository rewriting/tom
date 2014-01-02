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

import java.io.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Test;
import gom.importing.types.*;
import gom.imported.types.*;

public class TestImport {

  %include { sl.tom }
  %include { importing/Importing.tom }
  %typeterm Collection {
    implement { Collection }
    is_sort(t) { t instanceof Collection } 
  }
  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestImport.class.getName());
  }

  @Test
  public void testMake() {
    Out test = `Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())));
    assertEquals(test.toString(),
        "Loop(Loop(Pack(Atom()),Element(Atom())),Element(Element(Atom())))");
  }

  @Test
  public void testStrat() {
    Out test = `Loop(Loop(Pack(Atom()),LeafSlot(Leaf())),Element(Element(Atom())));
    Collection set = new ArrayList(); /* Make sure we count all inserts */
    try {
      `BottomUp(Count(set)).visitLight(test);
    } catch (Exception e) {
      fail(e + " catched");
    }
    assertEquals(4,set.size());
  }

  %strategy Count(col:Collection) extends `Identity() {
    visit Inner {
      a@Atom() -> { col.add(`a); }
    }
    visit Out {
      l@Loop[] -> { col.add(`l); }
    }
  }
}
