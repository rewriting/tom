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


class Matching1 {
  private TermFactory factory;

  //%include{ atermmapping.tom }
  %include{ term/term.tom }
  %include{ mutraveler.tom }

  private final TermFactory getTermFactory() {
    return factory;
  }
  
  private final PureFactory getPureFactory() {
    return factory.getPureFactory();
  }

  Matching1() {
    this(TermFactory.getInstance(SingletonFactory.getInstance()));;
  }
  
  Matching1(TermFactory factory) {
    this.factory = factory;
  }

  public static void main(String[] args) {
    Matching1 test = new Matching1();
    test.run();
  }

  public void run() {

    String[] queries = {
      "match(a, a)",
      "match(a, b)",
      "match(anti(a), a)",
      "match(anti(a), b)",
      "-",
      "match(f(a,anti(b)), f(a,a))",
      "match(f(a,anti(b)), f(a,c))",
      "match(f(a,anti(b)), f(a,b))",
      "match(f(a,anti(b)), f(b,c))",
      "-",
      "match(anti(f(a,anti(b))), f(a,a))",
      "match(anti(f(a,anti(b))), f(a,c))",
      "match(anti(f(a,anti(b))), f(a,b))",
      "match(anti(f(a,anti(b))), f(b,c))",
      "match(anti(f(a,anti(b))), g(b))",
      "match(anti(f(a,anti(b))), f(a,b))",
      "match(anti(f(a,anti(b))), f(b,b))",
      "-",
      "match(f(X,X), f(a,a))",
      "match(f(X,X), f(a,b))",
      "-",
      "match(anti(f(X,X)), f(a,a))",
      "match(anti(f(X,X)), f(a,b))",
      "match(anti(f(X,X)), g(a))",
      "-",
      "match(f(X,anti(g(X))), f(a,b))",
      "match(f(X,anti(g(X))), f(a,g(b)))",
      "match(f(X,anti(g(X))), f(b,g(b)))",
      "match(f(X,anti(g(X))), g(b))",
      "-",
      "match(anti(f(X,anti(g(X)))), f(a,b))",
      "match(anti(f(X,anti(g(X)))), f(a,g(b)))",
      "match(anti(f(X,anti(g(X)))), f(b,g(b)))",
      "match(anti(f(X,anti(g(X)))), g(b))",
    };
   
    for(int i=0 ; i<queries.length ; i++) {
      String s = queries[i];
      if(s.equals("-")) {
        System.out.println("---------------------------------------");
      } else {
        ATerm at = getPureFactory().parse(s);
        Constraint c = atermToConstraint(at);
        System.out.println(s);
        Constraint simplifiedConstraint = simplifyAndSolve(c);
        //System.out.println(" --> " + simplifiedConstraint);
      }
    }

  }

  private Term stringToTerm(String t) {
    ATerm at = getPureFactory().parse(t);
    Term res = atermToTerm(at);
    System.out.println(t + " --> " + res);
    return res;
  }

  private Term atermToTerm(ATerm at) {
    if(at instanceof ATermAppl) {
      ATermAppl appl = (ATermAppl) at;
      AFun afun = appl.getAFun();
      String name = afun.getName();
      if(name.equals("anti") && afun.getArity()==1) {
        ATerm subterm = appl.getArgument(0);
        return `Anti(atermToTerm(subterm));
      } else if(Character.isUpperCase(name.charAt(0)) && afun.getArity()==0) {
        return `Variable(name);
      } else {
        return `Appl(name,atermListToTermList(appl.getArguments()));
      }
    }

    throw new RuntimeException("error on: " + at);
  }

  private TermList atermListToTermList(ATerm at) {
    if(at instanceof ATermList) {
      ATermList atl = (ATermList) at;
      TermList l = `emptyTermList();
      while(!atl.isEmpty()) {
        l = `manyTermList(atermToTerm(atl.getFirst()),l);
        atl = atl.getNext();
      }
      return l;
    }

    throw new RuntimeException("error on: " + at);
  }

  private Constraint atermToConstraint(ATerm at) {
    if(at instanceof ATermAppl) {
      ATermAppl appl = (ATermAppl) at;
      String name = appl.getName();
      if(name.equals("match")) {
        ATerm pattern = appl.getArgument(0);
        ATerm subject = appl.getArgument(1);
        return `Match(atermToTerm(pattern),atermToTerm(subject));
      } else if(name.equals("neg")) {
        ATerm term = appl.getArgument(0);
        return `Neg(atermToConstraint(term));
      } 
    }

    throw new RuntimeException("error on: " + at);
  }  
 
  public Constraint simplifyAndSolve(Constraint c) {
   VisitableVisitor simplifyRule = new SimplifySystem();
   Collection solution = new HashSet();
   VisitableVisitor solveRule = new SolveSystem(solution);
   try { 
     Constraint res1 = (Constraint) MuTraveler.init(`Innermost(simplifyRule)).visit(c);
     System.out.println("simplified = " + res1); 

    // res1 = (Constraint) MuTraveler.init( `Repeat(Choice(solveRule,Innermost(simplifyRule)))).visit(res1);
     while(res1 != `True() && res1 != `False()){
    	 res1 = (Constraint) MuTraveler.init( solveRule).visit(res1);
    	 res1 = (Constraint) MuTraveler.init( `Innermost(simplifyRule)).visit(res1);
     }
     
        System.out.println("--> normal form = " + res1 + " solution = " + solution); 
         return res1;
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
            System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(match@Match(var@Variable(name),s)) -> {
            solution.add(match); 
            Constraint res = `False();
            System.out.println("[solve1] -> [" + match + "," + res + "]");
            return res;
        }
        And(concConstraint(X*,match@Match(var@Variable(name),s),Y*)) -> {
            solution.add(match); 
            VisitableVisitor rule = new ReplaceSystem(var,s);
            Constraint res = (Constraint) MuTraveler.init(`Innermost(rule)).visit(`And(concConstraint(X*,Y*)));
            System.out.println("[solve2] -> [" + match + "," + res + "]");
            return res;
        }
        Neg(And(concConstraint(X*,match@Match(var@Variable(name),s),Y*))) -> {
            solution.add(match); 
            VisitableVisitor rule = new ReplaceSystem(var,s);
            Constraint res = (Constraint) MuTraveler.init(`Innermost(rule)).visit(`Neg(And(concConstraint(X*,Y*))));
            System.out.println("[solve2] -> [" + match + "," + res + "]");
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
