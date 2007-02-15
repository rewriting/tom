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
package antipattern;

import aterm.*;
import aterm.pure.*;

import antipattern.term.*;
import antipattern.term.types.*;

import jjtraveler.VisitFailure;
import jjtraveler.reflective.VisitableVisitor;

public class SimplifySystem extends antipattern.term.TermBasicStrategy {
	
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected boolean isIdentity;
	
	public SimplifySystem(VisitableVisitor vis) {
      super(vis);
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
    		  true : false ); 
    }
    
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
        // AntiMatch
        Match(Anti(p),s) -> {
          return `Neg(Match(p,s));
        }
        
        // Delete
        Match(Appl(name,concTerm()),Appl(name,concTerm())) -> {
          return `True();
        }

        // Decompose
        Match(Appl(name,a1),Appl(name,a2)) -> {
          AConstraintList l = `concAnd();
          TermList args1 = `a1;
          TermList args2 = `a2;
          while(!args1.isEmptyconcTerm()) {
            l = `concAnd(Match(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
            args1 = args1.getTailconcTerm();
            args2 = args2.getTailconcTerm();
          }
          return `And(l);
        }
        
        // SymbolClash
        Match(Appl(name1,args1),Appl(name2,args2)) -> {
          if(`name1 != `name2) {
            return `False();
          }
        }
       
        // PropagateClash
        And(concAnd(_*,False(),_*)) -> {
          return `False();
        }
        
        // PropagateSuccess
        And(concAnd()) -> {
          return `True();
        }
        And(concAnd(x)) -> {
          return `x;
        }
        And(concAnd(X*,True(),Y*)) -> {
          return `And(concAnd(X*,Y*));
        }

        // BooleanSimplification
        Neg(Neg(x)) -> { return `x; }
        Neg(True()) -> { return `False(); }
        Neg(False()) -> { return `True(); }
        And(concAnd(X*,c,Y*,c,Z*)) -> {
          return `And(concAnd(X*,c,Y*,Z*));
        }

      }
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  }
