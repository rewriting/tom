/*
 * Copyright (c) 2006-2007, INRIA
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
 *
 * Antoine Reilles   e-mail: Antoine.Reilles@loria.fr
 */
package gom;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import gom.b.u.i.l.t.i.n.builtin.types.*;

public class TestFromMethods extends TestCase {

  %include { b/u/i/l/t/i/n/builtin/Builtin.tom }

  public static void main(String[] args) {
    junit.textui.TestRunner.run(suite());
  }

  public static Test suite() {
    Wrapper[] TESTS = new Wrapper[] {
      `Char('a'),
      `Int(21),
      `Int(java.lang.Integer.MAX_VALUE-2),
      `Int(java.lang.Integer.MAX_VALUE),
      `Real(21),
      `Real((float)20.3),
      `Double(21),
      `Double(20.3),
      `Bool(true()),
      `Bool(false()),
      `Long(23),
      `Long(java.lang.Integer.MAX_VALUE-3),
      `Long(java.lang.Integer.MAX_VALUE-1),
      `Long(java.lang.Integer.MAX_VALUE),
      `Long(java.lang.Long.MAX_VALUE-10),
      `Long(java.lang.Long.MAX_VALUE-1),
      `Long(java.lang.Long.MAX_VALUE),
      `Name("who?"),
      `Node(aterm.pure.SingletonFactory.getInstance().parse("f(g,(h(<a>,b),b),c)")),
      `concWrap(Int(1),Int(2),Name("toto"),Name("blop")),
      `concInt(1,2,3,4,5,6,7,8,9,0),
      `concLong(10000,123455,23445556),
      `concWrap(Int(1),concWrap(Name("a"),Name("b"),Name("c")),Int(3)),
      `concBool(true(),false(),true(),false()),
      `concChar('t','o','m')
    };
    TestSuite suite = new TestSuite();
    for (int i = 0; i<TESTS.length;i++) {
      suite.addTest(new TestFromMethods("testFromString",TESTS[i]));
      suite.addTest(new TestFromMethods("testFromTerm",TESTS[i]));
    }
    return suite;
  }

  private Wrapper testSubject;
  public TestFromMethods(String method, Wrapper wr) {
    super(method);
    testSubject = wr; 
  }

  public void testFromTerm() {
    Wrapper newObj = Wrapper.fromTerm(testSubject.toATerm());
    assertEquals(testSubject,newObj);
  }

  public void testFromString() {
    Wrapper newObj = Wrapper.fromString(testSubject.toString());
    assertEquals(testSubject,newObj);
  }
}
