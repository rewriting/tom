/*
 * Copyright (c) 2004-2015, Universite de Lorraine, Inria
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
 * 	- Neither the name of the Inria nor the names of its
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

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import antipatterns.testantipatternassociative.antipatternassociative.types.*;

public class TestAntiPatternAssociative { 
	
  %gom {
    module AntiPatternAssociative
      imports String
      abstract syntax
      Term = 
      | a()
      | b()
      | c()
      | d()
      | f(x1:Term, x2:Term) 
      | g(pred:Term)
      | l(list:TermList)
      | lst(Term*)

      TermList = concTerm(Term*)

      Result = 
      | True()
      | False()
      | And(Result*)
      | Or(Result*)
      | Equal(x1:String,x2:Term)
      | NEqual(x1:String,x2:Term)
  }

  public static void main(String[] args) {
    org.junit.runner.JUnitCore.main(TestAntiPatternAssociative.class.getName());
  }

  @Before
  public void setUp() {
  }

	private Result match1(TermList subject) {
		%match(TermList subject) {
			concTerm(_*,a(),_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp1() {		
		assertTrue(match1(`concTerm(b(),a(),c())) == `True());
		assertTrue(match1(`concTerm(b(),f(a(),c()),c())) == `False());
	}
	
	private Result match2(TermList subject) {
		%match(TermList subject) {
			!concTerm(_*,a(),_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp2() {		
		assertTrue(match2(`concTerm(b(),a(),c())) == `False());
		assertTrue(match2(`concTerm(b(),f(a(),c()),c())) == `True());
		assertTrue(match2(`concTerm(b(),f(b(),c()),c())) == `True());
	}
	
	private Result match3(TermList subject) {
		%match(TermList subject) {
			concTerm(_*,!a(),_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp3() {		
		assertTrue(match3(`concTerm(b(),a(),c())) == `True());
		assertTrue(match3(`concTerm(b(),f(a(),c()),c())) == `True());
		assertTrue(match3(`concTerm(a(),f(a(),c()),a())) == `True());
		assertTrue(match3(`concTerm(a(),a(),c(),a())) == `True());
		assertTrue(match3(`concTerm(a(),a(),a(),a())) == `False());
	}
	
	private Result match4(TermList subject) {
		%match(TermList subject){
			!concTerm(_*,!a(),_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp4() {		
		assertTrue(match4(`concTerm(b(),a(),c())) == `False());
		assertTrue(match4(`concTerm(b(),f(a(),c()),c())) == `False());
		assertTrue(match4(`concTerm(a(),f(a(),c()),a())) == `False());
		assertTrue(match4(`concTerm(a(),a(),c(),a())) == `False());
		assertTrue(match4(`concTerm(a(),a(),a(),a())) == `True());
	}
	
	private Result match4_1(TermList subject) {
		%match(TermList subject) {
			concTerm(X*,X*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp4_1() {		
		assertTrue(match4_1(`concTerm(b(),a(),c())) == `False());
		assertTrue(match4_1(`concTerm(b(),f(a(),c()),c())) == `False());
		assertTrue(match4_1(`concTerm(a(),f(a(),c()),a())) == `False());
		assertTrue(match4_1(`concTerm(a(),a(),c(),a())) == `False());
		assertTrue(match4_1(`concTerm(a(),a(),a(),a())) == `True());
		assertTrue(match4_1(`concTerm(a(),a(),b(),b())) == `False());
		assertTrue(match4_1(`concTerm(a(),c(),a(),c())) == `True());
	}
	
	private Result match5(TermList subject) {
		%match(TermList subject) {
			!concTerm(X*,X*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp5() {		
		assertTrue(match5(`concTerm(b(),a(),c())) == `True());
		assertTrue(match5(`concTerm(b(),f(a(),c()),c())) == `True());
		assertTrue(match5(`concTerm(a(),f(a(),c()),a())) == `True());
		assertTrue(match5(`concTerm(a(),a(),c(),a())) == `True());
		assertTrue(match5(`concTerm(a(),a(),a(),a())) == `False());
		assertTrue(match5(`concTerm(a(),a(),b(),b())) == `True());
		assertTrue(match5(`concTerm(a(),c(),a(),c())) == `False());
	}
	
	private Result match6(TermList subject) {
		%match(TermList subject) {
			!concTerm(_*,x,_*,x,_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp6() {		
		assertTrue(match6(`concTerm(b(),a(),c())) == `True());
		assertTrue(match6(`concTerm(b(),f(a(),c()),c())) == `True());
		assertTrue(match6(`concTerm(a(),f(a(),c()),a())) == `False());
		assertTrue(match6(`concTerm(a(),a(),c(),a())) == `False());
		assertTrue(match6(`concTerm(b(),a(),c(),a())) == `False());
		assertTrue(match6(`concTerm(a(),a(),b(),b())) == `False());
	}
	
	private Result match7(TermList subject) {
		%match(TermList subject) {
			concTerm(_*,x,_*,!x,_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp7() {		
		assertTrue(match7(`concTerm(b(),a(),c())) == `True());
		assertTrue(match7(`concTerm(b(),f(a(),c()),c())) == `True());
		assertTrue(match7(`concTerm(a(),f(a(),c()),a())) == `True());
		assertTrue(match7(`concTerm(a(),a(),c(),a())) == `True());
		assertTrue(match7(`concTerm(b(),a(),c(),a())) == `True());
		assertTrue(match7(`concTerm(a(),a(),a(),b())) == `True());
		assertTrue(match7(`concTerm(a(),a(),a(),a())) == `False());
	}
	
	private Result match8(TermList subject) {
		%match(TermList subject) {
			!concTerm(_*,x,_*,!x,_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp8() {		
		assertTrue(match8(`concTerm(b(),a(),c())) == `False());
		assertTrue(match8(`concTerm(b(),f(a(),c()),c())) == `False());
		assertTrue(match8(`concTerm(a(),f(a(),c()),a())) == `False());
		assertTrue(match8(`concTerm(a(),a(),c(),a())) == `False());
		assertTrue(match8(`concTerm(b(),a(),c(),a())) == `False());
		assertTrue(match8(`concTerm(a(),a(),a(),b())) == `False());
		assertTrue(match8(`concTerm(a(),a(),a(),a())) == `True());
		
	}

	private Result match9(TermList subject) {
		%match(TermList subject) {
			concTerm(_*,_x,_*,!_y,_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp9() {		
		assertTrue(match9(`concTerm(b(),a(),c())) == `False());
		assertTrue(match9(`concTerm(b(),f(a(),c()),c())) == `False());
		assertTrue(match9(`concTerm(a(),f(a(),c()),a())) == `False());
		assertTrue(match9(`concTerm(a(),a(),c(),a())) == `False());
		assertTrue(match9(`concTerm(b(),a(),c(),a())) == `False());
		assertTrue(match9(`concTerm(a(),a(),a(),b())) == `False());
		assertTrue(match9(`concTerm(a(),a(),a(),a())) == `False());		
	}	
	
	private Result match10(Term subject) {
		%match(Term subject) {
			f(x,l(concTerm(_*,x,_*))) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp10() {
		assertTrue(match10(`f(a(),l(concTerm(b(),a(),c())))) == `True());
		assertTrue(match10(`f(a(),l(concTerm(b(),c(),c())))) == `False());
		assertTrue(match10(`f(g(a()),l(concTerm(b(),c(),c())))) == `False());
		assertTrue(match10(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `True());
	}
	
	private Result match10_1(Term subject) {
		%match(Term subject) {
			f(x,l(!concTerm(_*,x,_*))) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp10_1() {		
		assertTrue(match10_1(`f(a(),l(concTerm(b(),a(),c())))) == `False());
		assertTrue(match10_1(`f(a(),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match10_1(`f(g(a()),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match10_1(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False());
	}
	
	private Result match11(Term subject) {
		%match(Term subject) {
			!f(x,l(concTerm(_*,x,_*)))-> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp11() {
		assertTrue(match11(`f(a(),l(concTerm(b(),a(),c())))) == `False());
		assertTrue(match11(`f(a(),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match11(`f(g(a()),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match11(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False());
		assertTrue(match11(`a()) == `True());
	}
	
	private Result match12(Term subject) {
		%match(Term subject) {
			f(x,!l(concTerm(_*,x,_*)))-> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp12() {
		assertTrue(match12(`f(a(),l(concTerm(b(),a(),c())))) == `False());
		assertTrue(match12(`f(a(),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match12(`f(g(a()),l(concTerm(b(),c(),c())))) == `True());
		assertTrue(match12(`f(g(a()),l(concTerm(b(),c(),g(a()))))) == `False());
		assertTrue(match12(`a()) == `False());
		assertTrue(match12(`f(g(a()),b())) == `True());
	}
	
	// used to generate a compilation error
	private Result match13(Term subject) {
		%match(Term subject) {
			lst(_*,lst(_*,f(a(),x@!a()),_*),lst(_*,f(a(),x),_*),_*)-> {
				return `True();
			}
		}
		return `False();
	}
	
	// used to generate a compilation error
	private Result match14(Term subject) {
		%match(Term subject) {
			lst(_*,lst(_*,f(a(),x@!a()),_*),lst(_*,f(a(),x@!a()),_*),_*)-> {
				return `True();
			}
		}
		return `False();
	}

	private Result match15(Term subject) {
		%match(Term subject) {
			//used to be before automatic FL:
      // lst(_*,lst(_*,f(y,x@!a()),_*),lst(_*,f(y,_),_*),_*) -> {
			lst(_*,f(y,x@!a()),_*,f(y,_),_*) -> {
				return `True();
			}
		}
		return `False();
	}
	
  @Test
	public void testAp15() {
    Term s = `lst(lst(f(a(),a()),f(a(),b()),f(a(),a())),lst(f(a(),a()),f(a(),b()),f(a(),a())));
		assertTrue(match15(s) == `True());
	}
	
	private Result match16(TermList subject) {
		%match( subject ) {
			concTerm(_before*,lst(_begin0*,f(_col0,_x@!a()),_end0*),lst(_begin1*,f(_col0,a()),_end1*),_after*) -> {
				return `True();
			}
		}
		return `False();	
	}

  @Test
	public void testAp16() {
		TermList tLst = `concTerm(
				lst(f(b(),a()),f(c(),a()),f(d(),a())),
				lst(f(b(),b()),f(c(),a()),f(d(),a())),
				lst(f(b(),b()),f(c(),a()),f(d(),c())),
				lst(f(b(),c()),f(c(),c()),f(d(),a())),
				lst(f(b(),b()),f(c(),d()),f(d(),d()))
		);

		assertTrue(match16(tLst) == `True());
	} 

}
