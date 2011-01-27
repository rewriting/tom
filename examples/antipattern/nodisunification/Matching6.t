/*
 * Copyright (c) 2005-2011, INPL, INRIA
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

package antipattern.nodisunification;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;

import tom.library.sl.*;




public class Matching6 implements Matching {
	
	%include{ sl.tom }
	%include{ ../term/Term.tom }
	%include { java/util/types/Collection.tom}
	
	public Constraint simplifyAndSolve(Constraint c, Collection solution) {
    try {
      return (Constraint)`SequenceId(InnermostId(EnrichedPatternMatching()),InnermostId(Cleaning())).visit(c);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("Failure");
    }
	}	
	
	%strategy EnrichedPatternMatching() extends `Identity(){
		visit Constraint{
			// EqTransform		
			Match(p,s) -> {
				return `Equal(p,s);
			}
			
			//	AntiMatch
	        Equal(Anti(p),s) -> {
	        	Collection quantifiedVarList = new ArrayList();
	            `TopDownCollect(CollectPositiveVariable(quantifiedVarList)).visit(`p);
	            
	            Constraint ret = `Equal(p,s);
	
	            Iterator it = quantifiedVarList.iterator();
	            //System.out.println("nr var:" + quantifiedVarList.size());
	            while(it.hasNext()) {
	              Term t = (Term)it.next();
	              ret = `Exists(t,ret);
	            }
	
	            return `Neg(ret);
	        }
	
	        // Decompose
	        Equal(Appl(name,a1),Appl(name,a2)) -> {
	          AConstraintList l = `concAnd();
	          TermList args1 = `a1;
	          TermList args2 = `a2;
	          while(!args1.isEmptyconcTerm()) {
	            l = `concAnd(Equal(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
	            args1 = args1.getTailconcTerm();
	            args2 = args2.getTailconcTerm();
	          }
	          return `And(l.reverse());
	        }
	        
	        // Replace
			input@And(concAnd(X*,equal@Equal(var@Variable(name),s),Y*)) -> {
				Constraint toApplyOn = `And(concAnd(Y*));
				Constraint res = (Constraint)`TopDown(ReplaceStrat(var,s)).visit(toApplyOn);
				if (res != toApplyOn){					
					return `And(concAnd(X*,equal,res));
				}
	        }
	        
	        // SymbolClash
	        Equal(Appl(name1,args1),Appl(name2,args2)) -> {
	          if(`name1 != `name2) {
	            return `False();
	          }
	        }	        

		}
	}
	
	%strategy Cleaning() extends `Identity(){
		visit Constraint{
	        //	Delete
	        Equal(Appl(name,concTerm()),Appl(name,concTerm())) -> {
	          return `True();
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
	//        And(concAnd(X*,c,Y*,c,Z*)) -> {
	//          return `And(concAnd(X*,c,Y*,Z*));
	//        }
	        
	        //Exists1
	        Exists(var,x) ->{
	        	if (!`contains(var,x)){	        
	        		return `x;
	        	}
	        }
	        
	        //Exists2
	        Exists(var,And(concAnd(X*,Equal(var,t),Y*))) ->{	        	
	        	if (!`contains(var,And(concAnd(X*,Y*)))){	        		
	        		return `And(concAnd(X*,Y*));
	        	}
	        }
	        
	        //Exists2'
	        Exists(var,Equal(var,t)) ->{	        	
	        	return `True();	        
	        }
		}
	}
	
	private boolean contains(Term t, Constraint c){
		
		containsFlag = false;
		
		try {
      `OnceTopDownId(CheckOccurence(t)).visit(c);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("Failure");
    }
		
		return containsFlag;
	}
	
	private boolean containsFlag = false;
	
	%strategy CheckOccurence(t:Term) extends `Identity(){
		visit Term {
			x -> {
				if (t == `x){
					containsFlag = true;
					// just to make it stop
					return `FalseTerm(); 
				}
			}
		}
	}
	
	%strategy ReplaceStrat(var:Term, value:Term) extends `Identity(){
		visit Term {
			x -> {
				if (`x == var) { return value; }  
			}
		}
	}
	
	// collect variables, a do not inspect under an AntiTerm
	%strategy CollectPositiveVariable(bag:Collection) extends `Identity() {		
	    visit Term {
	      Anti[] -> {
	        throw new VisitFailure();
	      }
	
	      v@Variable[] -> {
	        bag.add(`v);
	        throw new VisitFailure();
	      }
	    }
	}

}
