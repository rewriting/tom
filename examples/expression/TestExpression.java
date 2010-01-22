/*
 * Copyright (c) 2010, INPL, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 * 	- Redistributions of source code must retain the above copyright
 * 	notice, this list of conditions and the following disclaimer.  
 * 	- Redistributions in binary form must reproduce the above copyright
 * 	notice, this list of conditions and the following disclaimer in the
 * 	documentation and/or other materials provided with the distribution.
 * 	- Neither the name of the INRIA nor the names of its
 * 	contributors may be used to endorse or promote products derived from
 * 	this software without specific prior written permission.
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

package expression;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

public class TestExpression {
  private static Record record;
  private static RecordStrict recordStrict;

	public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestExpression.class.getName());
	}

  @BeforeClass
  public static void setUp() {
    record = new Record();
    recordStrict = new RecordStrict();
  }

  @AfterClass
  public static void teadDown() {
    record = null;
    recordStrict = null;
  }

  @Test public void recordE1PP() {
    Exp e = record.buildExp1();
    Assert.assertEquals("Mult(Plus(2,3),4)", record.prettyPrint(e));
  }

  @Test public void recordE2PP() {
    Exp e = record.buildExp2();
    Assert.assertEquals("Mult(Plus(a,0),1)", record.prettyPrint(e));
  }

  @Test public void recordE3PP() {
    Exp e = record.buildExp3();
    Assert.assertEquals(
        "Plus(Mult(Plus(a,0),1),Uminus(a))",
        record.prettyPrint(e));
  }

  @Test public void recordE1PPI() {
    Exp e = record.buildExp1();
    Assert.assertEquals("2 3 Plus 4 Mult", record.prettyPrintInv(e));
  }

  @Test public void recordE2PPI() {
    Exp e = record.buildExp2();
    Assert.assertEquals("a 0 Plus 1 Mult", record.prettyPrintInv(e));
  }

  @Test public void recordE3PPI() {
    Exp e = record.buildExp3();
    Assert.assertEquals(
        "a 0 Plus 1 Mult a Uminus Plus",
        record.prettyPrintInv(e));
  }

  @Test public void recordE1sim() {
    Exp e = record.buildExp1();
    Assert.assertEquals("20",
        record.prettyPrint(record.traversalSimplify(e)));
  }

  @Test public void recordE2sim() {
    Exp e = record.buildExp2();
    Assert.assertEquals("a",
        record.prettyPrint(record.traversalSimplify(e)));
  }

  @Test public void recordE3sim() {
    Exp e = record.buildExp3();
    Assert.assertEquals("0",
        record.prettyPrint(record.traversalSimplify(e)));
  }

  @Test public void recordStrictE1PP() {
    Exp e = recordStrict.buildExp1();
    Assert.assertEquals("Mult(Plus(2,3),4)", recordStrict.prettyPrint(e));
  }

  @Test public void recordStrictE2PP() {
    Exp e = recordStrict.buildExp2();
    Assert.assertEquals("Mult(Plus(a,0),1)", recordStrict.prettyPrint(e));
  }

  @Test public void recordStrictE3PP() {
    Exp e = recordStrict.buildExp3();
    Assert.assertEquals(
        "Plus(Mult(Plus(a,0),1),Uminus(a))",
        recordStrict.prettyPrint(e));
  }

  @Test public void recordStrictE1PPI() {
    Exp e = recordStrict.buildExp1();
    Assert.assertEquals("2 3 Plus 4 Mult", recordStrict.prettyPrintInv(e));
  }

  @Test public void recordStrictE2PPI() {
    Exp e = recordStrict.buildExp2();
    Assert.assertEquals("a 0 Plus 1 Mult", recordStrict.prettyPrintInv(e));
  }

  @Test public void recordStrictE3PPI() {
    Exp e = recordStrict.buildExp3();
    Assert.assertEquals(
        "a 0 Plus 1 Mult a Uminus Plus",
        recordStrict.prettyPrintInv(e));
  }

  @Test public void recordStrictE1sim() {
    Exp e = recordStrict.buildExp1();
    Assert.assertEquals("20",
        recordStrict.prettyPrint(recordStrict.traversalSimplify(e)));
  }

  @Test public void recordStrictE2sim() {
    Exp e = recordStrict.buildExp2();
    Assert.assertEquals("a",
        recordStrict.prettyPrint(recordStrict.traversalSimplify(e)));
  }

  @Test public void recordStrictE3sim() {
    Exp e = recordStrict.buildExp3();
    Assert.assertEquals("0",
        recordStrict.prettyPrint(recordStrict.traversalSimplify(e)));
  }

  @Test public void eqE1pp() {
    Assert.assertEquals(
        record.prettyPrint(record.buildExp1()),
        recordStrict.prettyPrint(recordStrict.buildExp1()));
  }

  @Test public void eqE2pp() {
    Assert.assertEquals(
        record.prettyPrint(record.buildExp2()),
        recordStrict.prettyPrint(recordStrict.buildExp2()));
  }

  @Test public void eqE3pp() {
    Assert.assertEquals(
        record.prettyPrint(record.buildExp3()),
        recordStrict.prettyPrint(recordStrict.buildExp3()));
  }

  @Test public void eqE1ppi() {
    Assert.assertEquals(
        record.prettyPrintInv(record.buildExp1()),
        recordStrict.prettyPrintInv(recordStrict.buildExp1()));
  }

  @Test public void eqE2ppi() {
    Assert.assertEquals(
        record.prettyPrintInv(record.buildExp2()),
        recordStrict.prettyPrintInv(recordStrict.buildExp2()));
  }

  @Test public void eqE3ppi() {
    Assert.assertEquals(
        record.prettyPrintInv(record.buildExp3()),
        recordStrict.prettyPrintInv(recordStrict.buildExp3()));
  }

  @Test public void eqE1ppt() {
    Assert.assertEquals(
        record.prettyPrint(record.traversalSimplify(record.buildExp1())),
        recordStrict.prettyPrint(recordStrict.traversalSimplify(recordStrict.buildExp1())));
  }

  @Test public void eqE2ppt() {
    Assert.assertEquals(
        record.prettyPrint(record.traversalSimplify(record.buildExp2())),
        recordStrict.prettyPrint(recordStrict.traversalSimplify(recordStrict.buildExp2())));
  }

  @Test public void eqE3ppt() {
    Assert.assertEquals(
        record.prettyPrint(record.traversalSimplify(record.buildExp3())),
        recordStrict.prettyPrint(recordStrict.traversalSimplify(recordStrict.buildExp3())));
  }
}
