/*
 * Copyright (c) 2004-2007, INRIA
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gom;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import tom.library.sl.*;

import java.util.*;
import gom.rond.types.*;
import gom.rond.*;

public class TestCarre extends TestCase {

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestCarre.class));
  }

  %include { java/util/types/ArrayList.tom }
  %include { rond/Rond.tom }
  %include { sl.tom }

  %typeterm Carre {
    implement { gom.Carre }
    is_sort(t) { t instanceof gom.Carre }
    equals(t1,t2) { t1.equals(t2) }
    visitor_fwd { CarreBasicStrategy }
  }
  %op Carre Carre(r1:Rond, r2:Rond) {
    is_fsym(t) { (t!=null) && (t instanceof Carre) }
    get_slot(r1, t) { t.r1 }
    get_slot(r2, t) { t.r2 }
    make(t0, t1) { new Carre(t0, t1)}
  }

  public void testPrint() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    Strategy print = makePrint(list);

    try {
      `BottomUp(print).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    // This is not really a robust way to test
    assertEquals(list.toString(),"[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]");
  }
  Strategy makePrint(ArrayList list) {
    return new Print(list);
  }
  %strategy Print(list:ArrayList) extends `Identity() {
    visit Point {
      x -> {
        list.add(`x);
        return `x;
      }
    }
  }

  public void testShowCarre() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    Strategy show = makeShowCarre(list);

    try {
      `BottomUp(show).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(list.toString(),"[Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14)))]");
  }
  Strategy makeShowCarre(ArrayList list) {
    return new ShowCarre(list);
  }
  %strategy ShowCarre(list:ArrayList) extends `Identity() {
    visit Carre {
      arg@Carre(l,r) -> {
        list.add("Carre("+`l+", "+`r+")");
        return `arg;
      }
    }
  }

  public void testCombin() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    Strategy comb = `ChoiceId(makePrint(list),makeShowCarre(list));

    try {
      `BottomUp(comb).visit(subject);
      `TopDown(comb).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(list.toString(),"[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]");
  }

}
