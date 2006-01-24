/*
 * Copyright (c) 2004-2006, INRIA
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

package propp;

import aterm.*;
import aterm.pure.*;
import java.util.*;
import tom.library.traversal.*;
import propp.seq.*;
import propp.seq.types.*;
import java.io.*;
import antlr.CommonAST;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestPropp extends TestCase {
  private Propp1 test;
  private static Factory factory;

	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestPropp.class));
	}

  public void setUp() {
		if (factory == null) {
			factory = Factory.getInstance(new PureFactory());
		}
    test = new Propp1(factory);
  }

  %include { seq/seq.tom }

  public Factory getSeqFactory() {
    return factory;
  }

  public void testMakeQuery() {
		assertEquals("makeQuery should builds the corresponding sequent tree",
								 `seq(concPred(impl(A,wedge(B,C))),concPred(vee(C,D))),
								 test.makeQuery("seq([impl(A,wedge(B,C))],[vee(C,D)])"));
  }

	public void testComparePair1() {
		assertEquals("pair(3,\"something\") is greater than pair(2,\"anything\")",
								 1,
								 test.comparePair(`pair(3,"something"),`pair(2,"anything")));
	}

	public void testComparePair2() {
		assertEquals("pair(4,\"something\") is greater than pair(8,\"anything\")",
								 -1,
								 test.comparePair(`pair(4,"something"),`pair(8,"anything")));
	}

}
