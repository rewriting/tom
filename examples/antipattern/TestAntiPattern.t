/*
 * Copyright (c) 2004-2007, INRIA
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

package antipattern;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import aterm.pure.*;

import java.util.Iterator;
import java.util.HashSet;

import antipattern.term.*;
import antipattern.term.types.*;

public class TestAntiPattern extends TestCase {
	
	private Matching4 testM4;	
	private Tools tools;
	private Constraint cons;
	
	%include{ term/Term.tom }
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestAntiPattern.class));
	}
	
	public void setUp() {
		testM4 = new Matching4();
		tools = new Tools();
	}
	
	public void testMatching4_1() {		
		
		System.out.println("Simple match");
	/*	
		cons = tools.atermToConstraint("match(a, a)");		
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(a, b)");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		cons = tools.atermToConstraint("match(anti(a), a)");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		*/
		cons = tools.atermToConstraint("match(anti(a), b)");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
	}
	
	public void testMatching4_2() {
		
		System.out.println("match(f(a,anti(b))");

		cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,c))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(f(a,anti(b)), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(f(a,anti(b)), f(b,c))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
	}
	
	public void testMatching4_3() {

		System.out.println("match(anti(f(a,anti(b)))");
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,c))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(b,c))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), g(b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(a,anti(b))), f(b,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
	}
	
	public void testMatching4_4() {
		
		System.out.println("match(f(X,X), f(a,a))");

		cons = tools.atermToConstraint("match(f(X,X), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`Match(Variable("X"),Appl("a",concTerm())));
		
		cons = tools.atermToConstraint("match(f(X,X), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
	}
	
	public void testMatching4_5() {
		
		System.out.println("match(anti(f(X,X))");

		cons = tools.atermToConstraint("match(anti(f(X,X)), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(X,X)), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(X,X)), g(a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
	}
	
	public void testMatching4_6() {
		
		System.out.println("match(f(X,anti(g(X)))");

		cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`Match(Variable("X"),Appl("a",concTerm())));
		
		cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(a,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`Match(Variable("X"),Appl("a",concTerm())));
		
		cons = tools.atermToConstraint("match(f(X,anti(g(X))), f(b,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(f(X,anti(g(X))), g(b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
	}
	
	public void testMatching4_7() {
		
		System.out.println("match(anti(f(X,anti(g(X))))");

		cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(a,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), f(b,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(X,anti(g(X)))), g(b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(anti(f(X,anti(g(Y)))), f(b,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
		
		cons = tools.atermToConstraint("match(f(a,anti(g(Y))), f(b,g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(f(a,anti(g(Y))), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
	}
	
	public void testMatching4_8() {
		
		System.out.println("match(f(X,anti(X))");

		cons = tools.atermToConstraint("match(f(X,anti(X)), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(f(X,anti(X)), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`Match(Variable("X"),Appl("a",concTerm())));
	}
	
	public void testMatching4_9() {

		System.out.println("match(f(anti(g(X)),anti(g(X)))");
		
		cons = tools.atermToConstraint("match(f(anti(g(X)),anti(g(X))),f(g(a),g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(anti(f(anti(g(X)),anti(g(X)))),f(g(a),g(b)))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`True());
	}
	
	public void testMatching4_10() {
		
		System.out.println("match(f(anti(X),anti(X))");
		
		cons = tools.atermToConstraint("match(f(anti(X),anti(X)), f(a,a))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());
		
		cons = tools.atermToConstraint("match(f(anti(X),anti(X)), f(a,b))");
		assertEquals(testM4.simplifyAndSolve(cons,null),`False());		
		
	}
}
