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

import java.util.Collection;

import tom.library.strategy.mutraveler.MuTraveler;

public class SolveSystem extends antipattern.term.TermBasicStrategy {
    
	%include{ term/Term.tom }
	%include{ mutraveler.tom }

	protected Collection solution; 
	protected boolean isIdentity;
	
    public SolveSystem(Collection c,VisitableVisitor vis) {
      super(vis);
      this.solution = c;
      this.isIdentity = (vis.getClass().equals(`Identity().getClass()) ? 
  		  true : false );      
    }
   
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
        match@Match(var@Variable(name),s) -> {
            solution.add(`match); 
            Constraint res = `True();
            /* System.out.println("[solve1] -> [" + match + "," + res + "]"); */
            return res;
        }
        Neg(match@Match(var@Variable(name),s)) -> {
            solution.add(`match); 
            Constraint res = `False();
            /* System.out.println("[solve1] -> [" + match + "," + res + "]"); */
            return res;
        }
        And(concAnd(X*,match@Match(var@Variable(name),s),Y*)) -> {
            solution.add(`match);
            VisitableVisitor rule,ruleStrategy;            
            if (isIdentity){
            	rule = new ReplaceSystem(`var,`s, `Identity());
            	ruleStrategy = `InnermostId(rule);
            }else{
            	rule = new ReplaceSystem(`var,`s, `Fail());
            	ruleStrategy = `Innermost(rule);
            }            
            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`And(concAnd(X*,Y*)));
            /* System.out.println("[solve3] -> [" + match + "," + res + "]"); */
            return res;
        }
        Neg(And(concAnd(X*,match@Match(var@Variable(name),s),Y*))) -> {
            solution.add(`match);
            VisitableVisitor rule,ruleStrategy;            
            if (isIdentity){
            	rule = new ReplaceSystem(`var,`s, `Identity());
            	ruleStrategy = `InnermostId(rule);
            }else{
            	rule = new ReplaceSystem(`var,`s, `Fail());
            	ruleStrategy = `Innermost(rule);
            }
            Constraint res = (Constraint) MuTraveler.init(ruleStrategy).visit(`Neg(And(concAnd(X*,Y*))));
            /* System.out.println("[solve4] -> [" + match + "," + res + "]"); */
            return res;
        }
      }
      
      return (isIdentity ? arg : (Constraint)`Fail().visit(arg));
    }
  
}
