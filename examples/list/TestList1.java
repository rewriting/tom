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

package list;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import aterm.ATermList;
import aterm.pure.SingletonFactory;

public class TestList1 extends TestCase {
  private List1 test;

  public static void main(String[] args) {
    junit.textui.TestRunner.run(new TestSuite(TestList1.class));
  }

  public void setUp() {
    test = new List1(SingletonFactory.getInstance());
  }

	public void testMakeSubject() {
		ATermList subject = test.makeSubject();
		assertEquals("Bad initialisation of subject",subject,
								 test.getFactory().parse(
									 "[a,b,c,a,b,c,a]"));
	}

	public void testSwapSort() {
		ATermList subject = test.makeSubject();
		ATermList res = test.swapSort(subject);
		assertEquals("Swapsort should to sort",
								 res,
								 test.getFactory().parse("[a,a,a,b,b,c,c]"));
	}

	public void testRemoveDouble() {
		ATermList subject = test.makeSubject();
		ATermList res = test.removeDouble(subject);
		assertEquals("Removedouble on the fully unsorded list do nothing",
								 res,
								 subject);
	}

	public void testSortAndRemoveDouble() {
		ATermList subject = test.makeSubject();
		ATermList res1 = test.swapSort(subject);
		ATermList res2 = test.removeDouble(res1);
		assertEquals("Removedouble on the sorded list ",
								 res2,
								 test.getFactory().parse("[a,b,c]"));
	}
    
}

