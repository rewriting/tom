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

import gom.vlist.types.*;

public class TestStrat extends TestCase {
  private static int max = 4;

  %include { sl.tom }
  %include { vlist/VList.tom }

  public VList genere(int n) {
    if(n>2) {
      VList l = genere(n-1);
      return `conc(n,l*);
    } else {
      return `conc(2);
    }
  }

  public void testToString() {
    VList subject = genere(max);
    assertEquals("conc(4,3,2)",subject.toString());
  }

  public void testOncebottomUp() {
    VList subject = genere(max);
    Strategy rule = new RewriteSystem();
    VList result = null;
    try {
      result = (VList) `OnceBottomUp(rule).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals(result.toString(),"conc(4,3,3)");
  }

  public void testBottomUp() {
    VList subject = genere(max);
    Strategy rule = new RewriteSystem();
    VList result = null;
    try {
      result = (VList) `BottomUp(Try(rule)).visit(subject);
    } catch (VisitFailure e) {
      fail("catched VisitFailure");
    }
    assertEquals("conc(5,4,3)",result.toString());
  }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestStrat.class));
  }

  class RewriteSystem extends gom.vlist.VListBasicStrategy {
    public RewriteSystem() {
      super(`Fail());
    }

    public VList visit_VList(VList arg) throws VisitFailure{
      %match(VList arg) {
        conc(h,t*) -> {
          int v = `h+1;
          return `conc(v,t*);
        }
      }
      //fail
      throw new VisitFailure();
    }
  }
}
