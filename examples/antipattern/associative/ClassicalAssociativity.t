/*
 * Copyright (c) 2004-2006, INRIA
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
package antipattern.associative;

import aterm.*;
import aterm.pure.*;

import java.util.*;

import antipattern.term.*;
import antipattern.term.types.*;

import tom.library.strategy.mutraveler.MuStrategy;
import jjtraveler.VisitFailure;

// simple algorithm implementing the associative matching
public class ClassicalAssociativity implements Matching {
	
	%include{ term/Term.tom }
	%include{ mustrategy.tom }
	%include { java/util/types/Collection.tom}
	
	public Constraint simplifyAndSolve(Constraint c, Collection solution) {
		return (Constraint)`InnermostId(PerformAssociativeMatching()).apply(c);
	}
	
	%strategy PerformAssociativeMatching() extends Identity(){
		
		visit Constraint{		
			//	EqTransform		
			Match(p,s) -> {
				return `Equal(p,s);
			}
			
	        // Decompose 1 & 2
	        Equal(f@Appl(name,a1),g@Appl(name,a2)) -> {
	    		AConstraintList l = `concAnd();
	            TermList args1 = `a1;
	            TermList args2 = `a2;
	            while(!args1.isEmptyconcTerm()) {
	              l = `concAnd(Equal(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
	              args1 = args1.getTailconcTerm();
	              args2 = args2.getTailconcTerm();
	            }
	            
	            l = l.reverse();            
	        	// if we do not have an associative symbol                
	            if ( !isAssociative(`f) ){
	            	return `And(l);
	            }
	            // reinitialize
	            args1 = `a1;
	            args2 = `a2;
	            
	            Term p_1 = args1.getHeadconcTerm(); // first elem
	            Term p_2 = args1.getTailconcTerm().getHeadconcTerm(); // second elem
	            
	            Term t_1 = args2.getHeadconcTerm(); // first elem
	            Term t_2 = args2.getTailconcTerm().getHeadconcTerm(); // second elem
	            
	            Term x_1 = `Variable("x_1");
	            Term x_2 = `Variable("x_2");
	            
	            Constraint secondTerm = `And( concAnd(
	            							Equal(p_1,x_1), 
	            							Equal(p_2,Appl(name,concTerm(x_2,t_2))),
	            							Equal(Appl(name,concTerm(x_1,x_2)),t_1)
	            							));
	            
	            return `Or(concOr(And(l),secondTerm));
	        }
	        
	        // Decompose 3
	        Equal(f@Appl(name1,a1),g@Appl(name2,a2)) -> {
	        	
	        	if (`name1 != `name2 && isAssociative(`f)){
	        		Term p_1 = `a1.getHeadconcTerm(); // first elem
	                Term p_2 = `a1.getTailconcTerm().getHeadconcTerm(); // second elem
	                
	                Constraint firstTerm =  `And(concAnd(
	                							Equal(p_1,Variable("e_" + getNeutralElem(f))),
	                							Equal(p_2,g)
	                							));
	                Constraint secondTerm =  `And(concAnd(
												Equal(p_1,g),
												Equal(p_2,Variable("e_" + getNeutralElem(f)))
												));
	                
	                return `Or(concOr(firstTerm,secondTerm));
	        	}
	        }
	      
	        // SymbolClash 1
	        Equal(f@Appl(name1,args1),Appl(name2,args2)) -> {
	          if(`name1 != `name2 && !isAssociative(`f)) {
	            return `False();
	          }
	        }
	        
	        // SymbolClash 2
	        Equal(v@Variable(_),f) -> {
	          if(!isAssociative(`v) && isAssociative(`f)) {
	            return `False();
	          }
	        }
	        
	        // Replace
			input@And(concAnd(X*,equal@Equal(var@Variable(name),s),Y*)) -> {
				System.out.println("Equal=" + `equal);
				Constraint toApplyOn = `And(concAnd(X*,Y*));
				System.out.println("TO=" + `toApplyOn);
				Constraint res = (Constraint)`TopDown(ReplaceStrat(var,s)).apply(toApplyOn);
				if (res != toApplyOn){
					return `And(concAnd(equal,toApplyOn));
				}
	        }
			
	        //	Delete
	        Equal(t1@Appl(name,_),t2@Appl(name,_)) -> {
	        	if (`t1 == `t2) {  
	        		return `True();        	
	        	}
	        }
	       
	        // PropagateClash 1
	        And(concAnd(_*,False(),_*)) -> {
	          return `False();
	        }
	        
	        // PropagateClash 2
	        Or(concOr(X*,False(),Y*)) -> {
	          return `Or(concOr(X*,Y*));
	        }        
	        
	        // PropagateSuccess
	        And(concAnd(X*,True(),Y*)) -> {
	          return `And(concAnd(X*,Y*));
	        }
	        
	        // Distribution of And and Or
	        And(concAnd(X*,Or(concOr(Z*)),Y*)) ->{
	        	OConstraintList result = `concOr();
	        	OConstraintList cOr = `Z*;
	        	while(!cOr.isEmptyconcOr()){
	        		result = `concOr(result*,And(concAnd(X*,cOr.getHeadconcOr(),Y*)));
	        		cOr = cOr.getTailconcOr();
	        	}
	        	
	        	return `Or(result);
	        }
	        
		} // end visit    
	}
	
	private boolean isAssociative(Term t){
		%match(t){
			Appl(name,_) -> {
				if (`name.endsWith("_a")) {
					return true;
				}else{
					return false;
				}
			}
			
			Variable(name) -> {				
				if (`name.endsWith("_a")) {
					return true;
				}else{
					return false;
				}
			}
		}
		
		return false;
	}
	
	private String getNeutralElem(Term t){
		%match(t){
			Appl(name,_) -> {
				return ((String)`name).substring(0,`name.indexOf("_"));
			}
		}
		
		return "XXX";
	}
	
	%strategy ReplaceStrat(var:Term, value:Term) extends `Identity(){
		visit Term {
			x -> {
				if (`x == var) { return value; }  
			}
		}
	}
	
}// end class
