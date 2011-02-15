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



//another algorithm for solving anti-pattern problems
//applies the trasnformation rule, but after that it does not
//use disunification, but rather some propagations and cleaning
public class ApElimination implements Matching{

  %include{ sl.tom }        
  %include{ boolean.tom }	
  %include{ ../term/Term.tom }

  public static int varCounter = 0;
  public Tools tools = new Tools();

  private int antiCounter = 0;

  // temporary list that holds variables that need to
  // be quantified
  private ArrayList quantifiedVarList = new ArrayList();

  private ArrayList freeVarList = new ArrayList();

  // used to count the number of rules that are applied
  private int rulesCounter = 0;

  public Constraint simplifyAndSolve(Constraint c,Collection solution) {

    quantifiedVarList.clear();
    freeVarList.clear();

    Constraint transformedMatch = null;
    varCounter = 0;		

    // replace the match with =
    label:%match(Constraint c) {
      Match(p,s) -> { 
        transformedMatch = `Equal(p,s/* GenericGroundTerm("SUBJECT") */);
        // collect free variables
        try {
          `TopDown(AnalyzeTerm(p,false)).visit(`p);
        } catch(VisitFailure e) {
          throw new tom.engine.exception.TomRuntimeException("Failure");
        }
        break label;
      }
      _ -> {
        throw new RuntimeException("Abnormal term received in simplifyAndSolve:" + c);
      }
    }		

    // eliminate anti
    Constraint noAnti = null;;
    try{
      noAnti = applyMainRule(transformedMatch);
    }catch(VisitFailure e) {
      System.out.println("1. reduction failed on: " + transformedMatch);
      e.printStackTrace();
    }

//  System.out.println("Free vars: " + freeVarList);
//  System.out.println("Quantified vars: " + quantifiedVarList);

//  System.out.println("Result after main rule: " +
//  tools.formatConstraint(noAnti));

    Constraint classicalMatch = null; 
    Constraint replacedVariables = null;
    Constraint quantifierFree = null;
    Constraint result = null;

    rulesCounter = 0;
    long timeStart = System.currentTimeMillis();


    try {		
      classicalMatch = (Constraint) `InnermostId(ClassicalPatternMatching()).visitLight(noAnti);
//    System.out.println("After classical match: " +
//    tools.formatConstraint(classicalMatch));
      replacedVariables = (Constraint) `TopDown(ReplaceVariables()).visitLight(classicalMatch);
//    System.out.println("After variable replacement: " +
//    tools.formatConstraint(replacedVariables));
      quantifierFree = (Constraint) `TopDown(EliminateQuantifiedVars()).visitLight(replacedVariables);
//    System.out.println("After quantified vars' elimination: " +
//    tools.formatConstraint(quantifierFree));
      result = (Constraint) `InnermostId(Cleaning()).visitLight(quantifierFree);			

    } catch (VisitFailure e) {
      System.out.println("3. reduction failed on: " + c);
      e.printStackTrace();
    }

//  System.out.println("Final result formated: " +
//  tools.formatConstraint(result));

    return result;
  }	

  // applies the main rule that transforms ap problems
  // into dis-unification ones
  public Constraint applyMainRule(Constraint c) throws VisitFailure{

    Term pattern = null;
    Term subject = null;

    %match(Constraint c) {
      Equal(p,s) -> {
        pattern = `p;
        subject = `s;
      }
    }

    // first get the constraint without the anti
    Constraint cNoAnti =  `Equal((Term)OnceTopDownId(ElimAnti()).visitLight(pattern),subject);
    // if nothing changed, time to exit
    if (cNoAnti == c) {
      return c;
    }
    // get the constraint with a variable instead of anti
    Constraint cAntiReplaced =  `Equal((Term) OnceTopDownId(ReplaceAnti()).visitLight(pattern),subject);

    quantifiedVarList.add(`Variable("v" + antipattern.disunification.ApAndDisunification1.varCounter));

    // recursiv call
    cAntiReplaced = applyMainRule(cAntiReplaced);		
    cNoAnti = `Neg(applyMainRule(cNoAnti));		

    `OnceTopDownId(ApplyStrategy()).visitLight(pattern);

    // System.out.println("antiCounter=" + antiCounter + " cNoAnti=" +
    // tools.formatConstraint(cNoAnti));

    return `And(concAnd(cAntiReplaced,cNoAnti));
    // return `And(concAnd(cAntiReplaced));
    // return `And(concAnd(cNoAnti));
  }


  // quantifies the variables in positive positions
  %strategy AnalyzeTerm(subject:Term,checkQuantified:boolean) extends `Identity() {		

    visit Term {
      v@Variable(x) -> {

        antiCounter = 0;

//      System.out.println("Analyzing " + `v + " position=" + getPosition() );

        Strategy useOmegaPath = (Strategy)getEnvironment().getPosition().getOmegaPath(`CountAnti());				

        useOmegaPath.visitLight(subject);

//      System.out.println("After analyzing counter=" + antiCounter);

        ArrayList listToUpdate = null;

        listToUpdate = checkQuantified ? quantifiedVarList: freeVarList;

        // if no anti-symbol found, than the variable can be quantified
        if (antiCounter == 0 && !listToUpdate.contains(`v)) {
          listToUpdate.add(`v);
        }

      }
    }		
  }

  // counts the anti symbols
  %strategy CountAnti() extends `Identity() {
    visit Term {
      Anti(_) -> {
        antiCounter++;				
      }
    }
  }

  // returns a term without the first negation that it finds
  %strategy ElimAnti() extends `Identity() {		
    visit Term {		 
      Anti(p) -> {
        return `p;
      }
    }
  }

  // returns a term with the first negation that it finds
  // replaced by a fresh variable
  %strategy ReplaceAnti() extends `Identity() {

    visit Term {
      // main rule
      a@Anti(_) -> {
        return `Variable("v" + (++antipattern.disunification.ApAndDisunification1.varCounter) );
      }
    }
  }

  // the strategy that handles the variables inside an anti
  // symbol for beeing qunatified
  %strategy ApplyStrategy() extends `Identity() {

    visit Term {
      // main rule
      anti@Anti(p) -> {
        `InnermostId(AnalyzeTerm(p,true)).visitLight(`p);				
        // now it has to stop
        return `p;
      }
    }
  }

  // collects the free variable of term
  // NB: can be improved not to further search for a variable
  // if an anti symbol was found on that branch
//%strategy CollectFreeVariables() extends `Identity() {

//visit Term {
//// main rule
//var@Variable[] -> {
//Term t = (Term)MuTraveler.init(`InnermostId(AnalyzeTerm(p,false))).visitLight(p);
//}
//}
//}

  %strategy ClassicalPatternMatching() extends `Identity() {

    visit Constraint{
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
        // rulesCounter++;
        l = l.reverse();
        return `And(l/* .reverseConstraintList() */);
      }

      // Merge
      And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(Variable(z),u),Z*)) -> {
        // rulesCounter++;
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(t,u),Z*));
      }

      // Delete
      Equal(a,a) -> {
        // rulesCounter++;
        return `True();
      }

      // SymbolClash
      Equal(Appl(name1,_),Appl(name2,_)) -> {
        if(`name1 != `name2) {
          // rulesCounter++;
          return `False();
        }
      }

      // PropagateClash
      And(concAnd(_*,False(),_*)) -> {
        return `False();
      }		

      // PropagateSuccess
      And(concAnd(X*,True(),Y*)) -> {
        return `And(concAnd(X*,Y*));
      }

      // clean
      And(concAnd()) -> {
        return `True();
      }
      And(concAnd(t)) -> {
        return `t;
      }
      And(concAnd(X*,And(concAnd(Y*)),Z*)) -> {
        return `And(concAnd(X*,Y*,Z*));
      }
    }
  }	

  %strategy ReplaceVariables() extends `Identity() {

    visit Constraint{
      // Replace
      And(concAnd(X*,eq@Equal(var@Variable[],s),Y*)) -> {

        Constraint res = (Constraint)`BottomUp(ReplaceTerm(var,s)).visitLight(`And(concAnd(X*,Y*)));
        // if we replaced something
        if (res != `And(concAnd(X*,Y*))) {
          return `And(concAnd(eq,res));
        }
      }
    }
  }

  // replaces all equalities that contain
  // quantified variables with true
  %strategy EliminateQuantifiedVars() extends `Identity() {

    visit Constraint{
      Equal(var@Variable[],s) -> {				
        if (quantifiedVarList.contains(`var) 
            && !freeVarList.contains(`var)) {
          return `True();
        }            
      }
    }
  }

  %strategy Cleaning() extends `Identity() {

    visit Constraint{

      // Delete - equalities created by replace
      Equal(a,a) -> {
        // rulesCounter++;
        return `True();
      }

      // SymbolClash
      Equal(Appl(name1,_),Appl(name2,_)) -> {
        if(`name1 != `name2) {
          // rulesCounter++;
          return `False();
        }
      }

      // clean
      And(concAnd()) -> {
        return `True();
      }

      // clean
      Neg(True()) -> {
        return `False();
      }

      // clean
      Neg(False()) -> {
        return `True();
      }

      // PropagateClash
      And(concAnd(_*,False(),_*)) -> {
        return `False();
      }		

      // PropagateSuccess
      And(concAnd(X*,True(),Y*)) -> {
        return `And(concAnd(X*,Y*));
      }
    }

  }

  %strategy ReplaceTerm(variable:Term,value:Term) extends `Identity() {
    visit Term {
      t -> {
        if (`t == variable) {
          return value;
        }
      }
    }// end visit
  }

} // end class
