/*
 * Copyright (c) 2004-2011, INPL, INRIA
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

package strategy;

import strategy.proba3.term.*;
import strategy.proba3.term.types.*;
import tom.library.sl.*;

import java.util.*;

public class Proba3 {

  %gom {
    module Term
    abstract syntax
		Term = a()
		     | b()
				 | c()
				 | f(s1:Term, s2:Term)
				 | g(s1:Term)
   }

  %include { sl.tom }

	%typeterm Collection {
		implement { Collection }
    is_sort(t)     { t instanceof Collection }
	}
	
	private static Random random = new Random();

	public final static void main(String[] args) {
    Term subject = `f(a(),f(b(),c()));
		List c = new ArrayList();

    Strategy S  = `mu(MuVar("x"),Choice(OmegaU(MuVar("x")),R1()));

    try {
      System.out.println("subject       = " + subject);
      Term s = (Term) S.visitLight(subject);
      System.out.println("s = " + s);

			`TopDown(GetPosition(c)).visitLight(subject);
			System.out.println("set[pos] = " + c);
			int index = Math.abs(random.nextInt()) % c.size();
			Position pos = (Position)c.get(index);
			Term pTerm = (Term)pos.getSubterm().visitLight(subject);
			System.out.println("pos = " + pos + " --> " + pTerm);
			
    } catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
    }


      System.out.println("----------------------");
		
      System.out.println("subject       = " + subject);
			Term result = pRewriteSubterm(subject, `Try(R1()));
      System.out.println("result        = " + result);

  }

	public static Term pRewriteSubterm(Term subject, Strategy v) {
		List c = new ArrayList();
		try {
			`TopDown(GetPosition(c)).visit(subject);
			Position pos = (Position)c.get(Math.abs(random.nextInt()) % c.size());
			Term res = (Term)pos.getOmega(v).visit(subject);
			System.out.println("selected pos = " + pos);
			return res;	
		} catch(VisitFailure e) {
      System.out.println("reduction failed on: " + subject);
		}
		return subject;
	}
	
  %strategy R1() extends `Fail() { 
    visit Term {
			a() -> { return `b(); } 
			b() -> { return `c(); } 
		}
	}
  
	%strategy GetPosition(c:Collection) extends `Identity() { 
    visit Term {
			_ -> { c.add(getEnvironment().getPosition()); }
		}
	}

}

 
