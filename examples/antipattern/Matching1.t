/*
 * Copyright (c) 2005-2006, INRIA
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

//import aterm.pure.SingletonFactory;
import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.term.*;
import antipattern.term.types.*;

import tom.library.strategy.mutraveler.MuTraveler;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;


public class Matching1 implements Matching {
  private TermFactory factory;

  //%include{ atermmapping.tom }
  %include{ term/term.tom }
  %include{ mutraveler.tom }

  private final TermFactory getTermFactory() {
    return factory;
  }
  
  Matching1() {
    this(TermFactory.getInstance(SingletonFactory.getInstance()));;
  }
  
  public Matching1(TermFactory factory) {
    this.factory = factory;
  }

  public Constraint simplifyAndSolve(Constraint c,Collection solution) {
   VisitableVisitor simplifyRule = new SimplifySystem();
   VisitableVisitor solveRule = new SolveSystem(solution);
   try { 
     return (Constraint) MuTraveler.init(
         `Sequence(Innermost(simplifyRule),
           Repeat( Sequence( solveRule, Innermost(simplifyRule)))
    				 )).visit(c);
    } catch (VisitFailure e) {
      System.out.println("reduction failed on: " + c);
      //e.printStackTrace();
    }
   return `False();
  } 

  class SimplifySystem extends antipattern.term.TermVisitableFwd {
    public SimplifySystem() {
      super(`Fail());
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
        Match(Appl(name,args1),Appl(name,args2)) -> {
          ConstraintList l = `emptyConstraintList();
          while(!args1.isEmpty()) {
            l = `manyConstraintList(Match(args1.getHead(),args2.getHead()),l);
            args1 = args1.getTail();
            args2 = args2.getTail();
          }
          return `And(l);
        }
        
        // SymbolClash
        Match(Appl(name1,args1),Appl(name2,args2)) -> {
          if(name1 != name2) {
            return `False();
          }
        }
       
        // PropagateClash
        And(concConstraint(_*,False(),_*)) -> {
          return `False();
        }
        
        // PropagateSuccess
        And(concConstraint()) -> {
          return `True();
        }
        And(concConstraint(x)) -> {
          return `x;
        }
        And(concConstraint(X*,True(),Y*)) -> {
          return `And(concConstraint(X*,Y*));
        }

        // BooleanSimplification
        Neg(Neg(x)) -> { return `x; }
        Neg(True()) -> { return `False(); }
        Neg(False()) -> { return `True(); }
        And(concConstraint(X*,c,Y*,c,Z*)) -> {
          return `And(concConstraint(X*,c,Y*,Z*));
        }

      }
      return (Constraint)`Fail().visit(arg);
      //throw new VisitFailure();
    }
  }
  
  class SolveSystem extends antipattern.term.TermVisitableFwd {
    private Collection solution; 
    public SolveSystem(Collection c) {
      super(`Fail());
      this.solution = c;
    }
   
    public Constraint visit_Constraint(Constraint arg) throws VisitFailure {
    	
      %match(Constraint arg) {
        match@Match(var@Variable(name),s) -> {
            solution.add(match); 
            Constraint res = `True();
            //System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(match@Match(var@Variable(name),s)) -> {
            solution.add(match); 
            Constraint res = `False();
            //System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        And(concConstraint(X*,match@Match(var@Variable(name),s),Y*)) -> {
            solution.add(match); 
            VisitableVisitor rule = new ReplaceSystem(var,s);
            Constraint res = (Constraint) MuTraveler.init(`Innermost(rule)).visit(`And(concConstraint(X*,Y*)));
            //System.out.println("[solve3] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(And(concConstraint(X*,match@Match(var@Variable(name),s),Y*))) -> {
            solution.add(match); 
            VisitableVisitor rule = new ReplaceSystem(var,s);
            Constraint res = (Constraint) MuTraveler.init(`Innermost(rule)).visit(`Neg(And(concConstraint(X*,Y*))));
            //System.out.println("[solve4] -> [" + match + "," + res + "]");
            return res;
        }
      }
      
      return (Constraint)`Fail().visit(arg);
    }
  
  }    
 
  class ReplaceSystem extends antipattern.term.TermVisitableFwd {
    private Term variable;
    private Term value;

    public ReplaceSystem(Term variable, Term value) {
      super(`Fail());
      this.variable = variable;
      this.value = value;
    }
   
    public Term visit_Term(Term arg) throws VisitFailure { 
      if(arg==variable) {
        return value;
      } 
      return (Term)`Fail().visit(arg);
    }
  }    
}
