/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

import org.junit.Test;
import org.junit.Assert;

import tom.library.sl.*;

import java.util.*;
import gom.rond.types.*;
import gom.rond.*;

public class TestCarre {

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestCarre.class.getName());
  }

  %include { java/util/types/ArrayList.tom }
  %include { rond/Rond.tom }
  %include { sl.tom }

  %typeterm Carre {
    implement { Carre }
    is_sort(t) { $t instanceof Carre }
    equals(t1,t2) { $t1.equals($t2) }
  }

  %op Carre Carre(r1:Rond, r2:Rond) {
    is_fsym(t) { $t instanceof Carre }
    get_slot(r1, t) { $t.r1 }
    get_slot(r2, t) { $t.r2 }
    make(t0, t1) { new Carre($t0, $t1)}
  }

  @Test
  public void testPrint() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    try {
      `BottomUp(Print(list)).visitLight(subject, new LocalIntrospector());
    } catch (VisitFailure e) {
      Assert.fail("catched VisitFailure");
    }
    // This is not really a robust way to test
    Assert.assertEquals("[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]",list.toString());
  }

  %strategy Print(list:ArrayList) extends `Identity() {
    visit Point {
      x -> {
        list.add(`x);
        return `x;
      }
    }
  }

  @Test
  public void testShowCarre() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    try {
      `BottomUp(ShowCarre(list)).visitLight(subject, new LocalIntrospector());
    } catch (VisitFailure e) {
      Assert.fail("catched VisitFailure");
    }
    Assert.assertEquals("[Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14)))]",list.toString());
  }

  %strategy ShowCarre(list:ArrayList) extends `Identity() {
    visit Carre {
      arg@Carre(l,r) -> {
        list.add("Carre("+`l+", "+`r+")");
        return `arg;
      }
    }
  }

  @Test
  public void testCombin() {
    Carre subject = `Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)),Cercle(Point(9,10),Point(11,12),Point(13,14)));
    ArrayList list = new ArrayList();
    Strategy comb = `ChoiceId(Print(list),ShowCarre(list));

    try {
      `BottomUp(comb).visitLight(subject, new LocalIntrospector());
      `TopDown(comb).visitLight(subject, new LocalIntrospector());
    } catch (VisitFailure e) {
      Assert.fail("catched VisitFailure");
    }
    Assert.assertEquals("[Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Carre(Cercle(Point(1,0),Point(3,7),Point(4,9)), Cercle(Point(9,10),Point(11,12),Point(13,14))), Point(1,0), Point(3,7), Point(4,9), Point(9,10), Point(11,12), Point(13,14)]",list.toString());
  }
}
