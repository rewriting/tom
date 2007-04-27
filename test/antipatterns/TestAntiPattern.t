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

package antipatterns;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import antipatterns.testantipattern.antipattern.types.*;

public class TestAntiPattern extends TestCase {	  
	
  %gom {
    module AntiPattern
      imports String int
      abstract syntax
      Term = a()
      | b()
      | c()
      | f(x1:Term, x2:Term) 
      | g(pred:Term)
      | ff(x1:Term, x2:Term)
      | i(val:int)
      | j(val:int)

      Result = True()
      | False()
      | And(Result*)
      | Or(Result*)
      | Equal(x1:String,x2:Term)
      | NEqual(x1:String,x2:Term)
  }
	
	public static void main(String[] args) {
		junit.textui.TestRunner.run(new TestSuite(TestAntiPattern.class));
	}
	
	public void setUp() {
		
	}

	private Result match11(Term subject){
		%match(Term subject){
			f(!g(x),!g(x)) ->{
				return `True();
			}
		}
		return `False();
	}
    
    public void testAp11() {		
		
		assertTrue(match11(`f(g(a()),g(b()))) == `False());						
	}	

}
