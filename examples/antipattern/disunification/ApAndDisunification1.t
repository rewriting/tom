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

package antipattern.disunification;

import aterm.*;
import aterm.pure.*;

import java.io.*;
import java.util.*;

import antipattern.*;
import antipattern.term.*;
import antipattern.term.types.*;

import tom.library.sl.*;

public class ApAndDisunification1 implements Matching {

  %include{ sl.tom }	
  %include{ ../term/Term.tom }

  public static int varCounter = 0;
  public Tools tools = new Tools();

  private int antiCounter = 0;

  // temporary list that holds variables that need to
  // be quantified
  private ArrayList quantifiedVarList = new ArrayList();

  // used to count the number of rules that are applied
  private int rulesCounter = 0;

  public Constraint simplifyAndSolve(Constraint c,Collection solution) {

    Constraint transformedMatch = null;
    varCounter = 0;		

    // replace the match with =
    label:%match(Constraint c) {
      Match(p,s) -> { 
        transformedMatch = `Equal(p,GenericGroundTerm("SUBJECT"));

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

//  System.out.println("Result after main rule: " +
//  tools.formatConstraint(noAnti));

    // transform the problem into a disunification one
    Strategy transInDisunif = `TransformIntoDisunification();
    Constraint disunifProblem = null;
    try {		
      disunifProblem = (Constraint) `InnermostId(transInDisunif).visitLight(noAnti);			
    } catch (VisitFailure e) {
      System.out.println("2. reduction failed on: " + noAnti);
      e.printStackTrace();
    }

//  System.out.println("Disunification problem: " +
//  tools.formatConstraint(disunifProblem));

    // apply the disunification rules
    Constraint compiledConstraint = null, solvedConstraint = null;

    rulesCounter = 0;
    long timeStart = System.currentTimeMillis();

    Strategy simplifyRule = `SimplifyWithDisunification();
//  Strategy simplifyRule = `SimplifyWithDisunificationAll();
    Strategy decomposeTerms = `DecomposeTerms();
    Strategy solve = `SolveRes();

    try {		
      compiledConstraint = (Constraint)`InnermostId(simplifyRule).visitLight(disunifProblem);
//    solvedConstraint = (Constraint)
//    MuTraveler.init(`SequenceId(InnermostId(decomposeTerms),InnermostId(solve))).visitLight(compiledConstraint);
    } catch (VisitFailure e) {
      System.out.println("3. reduction failed on: " + c);
      e.printStackTrace();
    }

//  System.out.println("Final result: " + compiledConstraint);
    System.out.println("Final result formated: " + tools.formatConstraint(compiledConstraint));
//  System.out.println("Final result solved: " +
//  tools.formatConstraint(solvedConstraint));

//  System.out.println("Number of rules applied: " + rulesCounter);
//  System.out.println("Time passed: " + (System.currentTimeMillis() - timeStart)
//  + " ms");

//  return solvedConstraint;
    return compiledConstraint;
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
    Constraint cNoAnti =  `Equal((Term) OnceTopDownId(ElimAnti()).visitLight(pattern),subject);
    // if nothing changed, time to exit
    if (cNoAnti == c) {
      return c;
    }
    // get the constraint with a variable instead of anti
    Constraint cAntiReplaced =  `Equal((Term) OnceTopDownId(ReplaceAnti()).visitLight(pattern),subject);

    cAntiReplaced = `Exists(Variable("v" + antipattern.disunification.ApAndDisunification1.varCounter),
        applyMainRule(cAntiReplaced));

    cNoAnti = applyMainRule(cNoAnti);

//  System.out.println("Asta e 'c':" + tools.formatConstraint(c));

    cNoAnti = `Neg(cNoAnti);

    quantifiedVarList.clear();

    `OnceTopDownId(ApplyStrategy()).visitLight(c);

    Iterator it = quantifiedVarList.iterator();
    while(it.hasNext()) {
      Term t = (Term)it.next();
      cNoAnti = `ForAll(t,cNoAnti);
    }

    // System.out.println("antiCounter=" + antiCounter + " cNoAnti=" +
    // tools.formatConstraint(cNoAnti));

    // recursive call to itself
    return `And(concAnd(cAntiReplaced,cNoAnti));
    // return `And(concAnd(cAntiReplaced));
    // return `And(concAnd(cNoAnti));
  }


  // quantifies the variables in positive positions
  %strategy AnalyzeTerm(subject:Term) extends `Identity() {		

    visit Term {
      v@Variable(x) -> {

        antiCounter = 0;

//      System.out.println("Analyzing " + `v + " position=" + getPosition() );

        Strategy useOmegaPath = (Strategy)getEnvironment().getPosition().getOmegaPath(`CountAnti());				

        useOmegaPath.visitLight(subject);

//      System.out.println("After analyzing counter=" + antiCounter);
        // if no anti-symbol found, than the variable can be quantified
        if (antiCounter == 0) {
          quantifiedVarList.add(`v);
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
        Term t = (Term)`InnermostId(AnalyzeTerm(p)).visitLight(`p);				
        // now it has to stop
        return `p;
      }
    }
  }

  // applies symple logic rules for eliiminating
  // the not and thus creating a real disunification problem
  %strategy TransformIntoDisunification() extends `Identity() {

    visit Constraint {

      // some simplification stuff
      Exists(a,Exists(a,b)) -> {
        return `Exists(a,b);
      }

      // producing or - de morgan 1
      Neg(And(a)) -> {

        AConstraintList l = `a;
        OConstraintList result = `concOr();			

        while(!l.isEmptyconcAnd()) {
          result = `concOr(Neg(l.getHeadconcAnd()),result*);
          l = l.getTailconcAnd();
        }				

        return `Or(result.reverse());
      }

      // make Neg go down - de morgan 2
      n@Neg(Or(a)) -> {

        OConstraintList l = `a;
        AConstraintList result = `concAnd();

        while(!l.isEmptyconcOr()) {
          result = `concAnd(Neg(l.getHeadconcOr()),result*);
          l = l.getTailconcOr();
        }

        return `And(result.reverse());
      }

      // BooleanSimplification
      Neg(Neg(x)) -> { return `x; }

      // replace Neg(Equal) with NEqual
      Neg(Equal(a,b)) -> { return `NEqual(a,b);}

      // replace Neg(NEqual) with Equal
      Neg(NEqual(a,b)) -> { return `Equal(a,b);}

      // replace Neg(Exists) with ForAll
      Neg(Exists(v,c)) -> {			
        return `ForAll(v,Neg(c));			
      }

      // replace Neg(ForAll) with Exists
      Neg(ForAll(v,c)) -> {			
        return `Exists(v,Neg(c));			
      }
    }
  }

  %strategy SimplifyWithDisunification() extends `Identity() {

    visit Constraint {			

      // simple rules with exists and forall
      Exists(Variable(a),Equal(Variable(a),_)) -> {				
        return `True();
      }						
      Exists(Variable(a),NEqual(Variable(a),_)) -> {				
        return `True();
      }
      Exists(v@Variable(name),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable

        if (!tools.containsVariable(`constr,`v)) {				
          return `constr;
        }

      }			
      ForAll(Variable(a),Equal(Variable(a),_)) -> {
        return `False();
      }
      ForAll(Variable(a),NEqual(Variable(a),_)) -> {
        return `False();
      }
      ForAll(v@Variable(_),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable

        if (!tools.containsVariable(`constr,`v)) {
          return `constr;
        }				
      }

      // ///////////////////////////////////////////////////

      // distribution of exists/forall in and and or - allows
      // simplification with the rules above
      e@Exists(v@Variable(_),And(list)) -> {

        AConstraintList l = `list;
        AConstraintList result = `concAnd();
        AConstraintList result1 = `concAnd();

        while(!l.isEmptyconcAnd()) {

          Constraint c = l.getHeadconcAnd();

          // if the c doesn't contain the variable, we
          // can put it outside the expresion that is quantified
          if ( !tools.containsVariable(`c,`v) ) {
            result = `concAnd(c,result*);
          }else{
            result1 = `concAnd(c,result1*);
          }				

          // result = `concAnd(Exists(v,l.getHeadconcAnd()),result*);
          l = l.getTailconcAnd();
        }

        // if couldn't do anything, return the same thing
        if (result.isEmptyconcAnd()) {
          return `e;
        }

        result = result.reverse();
        result1 = result1.reverse();

        // if not all were separated
        if (!result1.isEmptyconcAnd()) {
          result = `concAnd(Exists(v,And(result1)),result*);
        }

        return `And(result);
      }			
      Exists(v@Variable(var),Or(list)) -> {

        OConstraintList l = `list;
        OConstraintList result = `concOr();

        while(!l.isEmptyconcOr()) {
          result = `concOr(Exists(v,l.getHeadconcOr()),result*);
          l = l.getTailconcOr();
        }

        return `Or(result);
      }
      ForAll(v@Variable(var),And(list)) -> {

        AConstraintList l = `list;
        AConstraintList result = `concAnd();

        while(!l.isEmptyconcAnd()) {
          result = `concAnd(ForAll(v,l.getHeadconcAnd()),result*);
          l = l.getTailconcAnd();
        }

        return `And(result);
      }			

      f@ForAll(v@Variable(_),Or(list)) -> {

        OConstraintList l = `list;
        OConstraintList result = `concOr();
        OConstraintList result1 = `concOr();

        while(!l.isEmptyconcOr()) {

          Constraint c = l.getHeadconcOr();

          // if the c doesn't contain the variable, we
          // can put it outside the expresion that is quantified
          if ( !tools.containsVariable(`c,`v) ) {					
            result = `concOr(c,result*);
          }else{
            result1 = `concOr(c,result1*);
          }			

          l = l.getTailconcOr();
        }

        // if couldn't do anything, return the same thing
        if (result.isEmptyconcOr()) {
          return `f;
        }

        result = result.reverse();
        result1 = result1.reverse();

        // if not all were separated
        if (!result1.isEmptyconcOr()) {
          result = `concOr(ForAll(v,Or(result1)),result*);
        }

        return `Or(result);
      }			

      // ///////////////////////////////////////////////////////////////////

      // Delete
      And(concAnd(_*,Equal(a,b),_*,NEqual(a,b),_*)) -> {				
        return `False();
      }			
      And(concAnd(_*,NEqual(a,b),_*,Equal(a,b),_*)) -> {				
        return `False();
      }		

      // Elimination of trivial equations and disequations
      Equal(a,a) -> {
        return `True();
      }			
      NEqual(a,a) -> {
        return `False();
      }

      // Special cleaning
      Or(concOr(X*,NEqual(x,a),Y*,And(concAnd(T*,Equal(x,a),U*)),Z*)) -> {
        return `Or(concOr(X*,NEqual(x,a),Y*,And(concAnd(T*,U*)),Z*));
      }
      And(concAnd(X*,Equal(x,a),Y*,Or(concOr(T*,NEqual(x,a),U*)),Z*)) -> {
        return `And(concAnd(X*,Equal(x,a),Y*,Or(concOr(T*,U*)),Z*));
      }

      // PropagateClash
      And(concAnd(_*,False(),_*)) -> {
        return `False();
      }

      Or(concOr(X*,False(),Y*)) -> {
        return `Or(concOr(X*,Y*));
      }

      // PropagateSuccess
      And(concAnd(X*,True(),Y*)) -> {
        return `And(concAnd(X*,Y*));
      }

      Or(concOr(_*,True(),_*)) -> {			
        return `True();
      }

      // cleaning the result
      And(concAnd(X*,And(concAnd(Y*)),Z*)) -> {
        return `And(concAnd(X*,Y*,Z*));
      }			

      Or(concOr(X*,Or(concOr(Y*)),Z*)) -> {
        return `Or(concOr(X*,Y*,Z*));
      }					

      And(concAnd(X*,a,Y*,a,Z*)) -> {				
        return `And(concAnd(X*,a,Y*,Z*));
      }

      Or(concOr(X*,a,Y*,a,Z*)) -> {				
        return `Or(concOr(X*,a,Y*,Z*));
      }

      And(concAnd()) -> {
        return `True();
      }

      Or(concOr()) -> {
        return `False();
      }

      And(concAnd(x)) -> {
        return `x;
      }

      Or(concOr(x)) -> {
        return `x;
      }

      // ///////////////////////////////////////////////////

      // Replace 1
      And(concAnd(X*,eq@Equal(var@Variable(name),s),Y*)) -> {
        // And(concAnd(X*,eq@Equal(var,s),Y*)) -> {

        Constraint res = (Constraint)`BottomUp(ReplaceTerm(var,s)).visitLight(`And(concAnd(X*,Y*)));
        if (res != `And(concAnd(X*,Y*))) {
          return `And(concAnd(eq,res));
        }
      }

      // Replace 2
      Or(concOr(X*,eq@NEqual(var@Variable(name),s),Y*)) -> {
        // And(concAnd(X*,eq@Equal(var,s),Y*)) -> {

        Constraint res = (Constraint)`BottomUp(ReplaceTerm(var,s)).visitLight(`Or(concOr(X*,Y*)));
        if (res != `Or(concOr(X*,Y*))) {
          return `Or(concOr(eq,res));
        }
      }

      // ////////////////////////////////////////////////////

      // Decompose
      e@Equal(Appl(name,a1),g) -> {

        %match(Term g) {
          SymbolOf(_) -> {return `e;}
        }				

        AConstraintList l = `concAnd();
        TermList args1 = `a1;

        int counter = 0;

        while(!args1.isEmptyconcTerm()) {
          l = `concAnd(Equal(args1.getHeadconcTerm(),Subterm(++counter,g)),l*);					
          args1 = args1.getTailconcTerm();										
        }

        l = l.reverse();

        l = `concAnd(Equal(Appl(name,concTerm()),SymbolOf(g)),l*);

        return `And(l);
      }

      e@NEqual(Appl(name,a1),g) -> {

        %match(Term g) {
          SymbolOf(_) -> {return `e;}
        }				

        OConstraintList l = `concOr();
        TermList args1 = `a1;

        int counter = 0;

        while(!args1.isEmptyconcTerm()) {
          l = `concOr(NEqual(args1.getHeadconcTerm(),Subterm(++counter,g)),l*);					
          args1 = args1.getTailconcTerm();										
        }

        l = l.reverse();

        l = `concOr(NEqual(Appl(name,concTerm()),SymbolOf(g)),l*);

        return `Or(l);
      }

      // //////////////////////

      // merging rules - Comon and Lescanne

      // m1
      And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(Variable(z),u),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(t,u),Z*));
      }			
      // m2
      Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m3
      And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      And(concAnd(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m4
      Or(concOr(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }
      Or(concOr(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }			

    } // end visit
  } // end strategy

  %strategy ReplaceTerm(variable:Term,value:Term) extends `Identity() {
    visit Term {
      t -> {
        if (`t == variable) {
          return value;
        }
      }
    }// end visit
  }

  %strategy DecomposeTerms() extends `Identity() {

    visit Term {

      SymbolOf(Appl(name,_)) -> {
        return `Appl(name,concTerm());
      }

      Subterm(no,Appl(name,list)) -> {

        TermList tl = `list;
        Term tmp = null;				

        try{								
          for (int i = 1; i <= `no; i++,tl=tl.getTailconcTerm()) {
            tmp = tl.getHeadconcTerm();						
          }
        }catch(UnsupportedOperationException e) {
          return `FalseTerm(); // decomposition not possible
        }

        return tmp;
      }
    }
  }

  %strategy SolveRes() extends `Identity() {

    visit Constraint {

//    simple rules with exists and forall
      Exists(Variable(a),Equal(Variable(a),_)) -> {				
        return `True();
      }						
      Exists(Variable(a),NEqual(Variable(a),_)) -> {				
        return `True();
      }
      Exists(v@Variable(_),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable

        if (!tools.containsVariable(`constr,`v)) {
          return `constr;
        }					
      }			
      ForAll(Variable(a),Equal(Variable(a),_)) -> {
        return `False();
      }
      ForAll(Variable(a),NEqual(Variable(a),_)) -> {
        return `False();
      }
      ForAll(v@Variable(_),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable
        if (!tools.containsVariable(`constr,`v)) {
          return `constr;
        }					
      }

      // ground terms' equality
      Equal(p@Appl(_,_),g@Appl(_,_)) -> {
        return (`g == `p ? `True() : `False() );
      }
      NEqual(p@Appl(_,_),g@Appl(_,_)) -> {
        return (`g != `p ? `True() : `False() );
      }			

      Equal(_,FalseTerm()) -> {
        return `False();
      }
      NEqual(_,FalseTerm()) -> {
        return `True();
      }

      // PropagateClash
      And(concAnd(_*,False(),_*)) -> {
        return `False();
      }

      Or(concOr(X*,False(),Y*)) -> {
        return `Or(concOr(X*,Y*));
      }

      // PropagateSuccess
      And(concAnd(X*,True(),Y*)) -> {
        return `And(concAnd(X*,Y*));
      }			

      Or(concOr(_*,True(),_*)) -> {
        return `True();
      }			

      And(concAnd()) -> {
        return `True();
      }

      Or(concOr()) -> {
        return `False();
      }

      // cleaning
      And(concAnd(X*,a,Y*,a,Z*)) -> {
        return `And(concAnd(X*,a,Y*,Z*));
      }

      Or(concOr(X*,a,Y*,a,Z*)) -> {
        return `Or(concOr(X*,a,Y*,Z*));
      }

      And(concAnd(x)) -> {
        return `x;
      }

      Or(concOr(x)) -> {
        return `x;
      }

      // merging rules - Comon and Lescanne

      // m1
      And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(Variable(z),u),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(t,u),Z*));
      }			
      // m2
      Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m3
      And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      And(concAnd(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m4
      Or(concOr(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }
      Or(concOr(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }
    }
  }


  // ///////////////////////////////////////////////////////////////////////////////////
  // / All in one strategy

  %strategy SimplifyWithDisunificationAll() extends `Identity() {

    visit Constraint {		

      // simple rules with exists and forall
      Exists(Variable(a),Equal(Variable(a),_)) -> {				
        rulesCounter++;
        return `True();
      }						
      Exists(Variable(a),NEqual(Variable(a),_)) -> {
        rulesCounter++;
        return `True();
      }
      Exists(v@Variable(name),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable

        if (!tools.containsVariable(`constr,`v)) {
          rulesCounter++;
          return `constr;
        }

      }			
      ForAll(Variable(a),Equal(Variable(a),_)) -> {
        rulesCounter++;
        return `False();
      }
      ForAll(Variable(a),NEqual(Variable(a),_)) -> {
        rulesCounter++;
        return `False();
      }
      ForAll(v@Variable(_),constr)-> {
        // eliminates the quantificator when the
        // constraint does not contains the variable

        if (!tools.containsVariable(`constr,`v)) {
          rulesCounter++;
          return `constr;
        }				
      }

      // ///////////////////////////////////////////////////

      // distribution of exists/forall in and and or - allows
      // simplification with the rules above
      e@Exists(v@Variable(_),And(list)) -> {

        AConstraintList l = `list;
        AConstraintList result = `concAnd();
        AConstraintList result1 = `concAnd();

        while(!l.isEmptyconcAnd()) {

          Constraint c = l.getHeadconcAnd();

          // if the c doesn't contain the variable, we
          // can put it outside the expresion that is quantified
          if ( !tools.containsVariable(`c,`v) ) {
            result = `concAnd(Exists(v,c),result*);
          }else{
            result1 = `concAnd(c,result1*);
          }				

          // result = `concAnd(Exists(v,l.getHeadconcAnd()),result*);
          l = l.getTailconcAnd();
        }

        // if couldn't do anything, return the same thing
        if (result.isEmptyconcAnd()) {
          return `e;
        }

        result = result.reverse();
        result1 = result1.reverse();

        // if not all were separated
        if (!result1.isEmptyconcAnd()) {
          result = `concAnd(Exists(v,And(result1)),result*);
        }
        rulesCounter++;
        return `And(result);
      }			
      Exists(v@Variable(var),Or(list)) -> {

        OConstraintList l = `list;
        OConstraintList result = `concOr();

        while(!l.isEmptyconcOr()) {
          result = `concOr(Exists(v,l.getHeadconcOr()),result*);
          l = l.getTailconcOr();
        }
        rulesCounter++;
        return `Or(result);
      }
      ForAll(v@Variable(var),And(list)) -> {

        AConstraintList l = `list;
        AConstraintList result = `concAnd();

        while(!l.isEmptyconcAnd()) {
          result = `concAnd(ForAll(v,l.getHeadconcAnd()),result*);
          l = l.getTailconcAnd();
        }
        rulesCounter++;
        return `And(result);
      }			

      f@ForAll(v@Variable(_),Or(list)) -> {

        OConstraintList l = `list;
        OConstraintList result = `concOr();
        OConstraintList result1 = `concOr();

        while(!l.isEmptyconcOr()) {

          Constraint c = l.getHeadconcOr();

          // if the c doesn't contain the variable, we
          // can put it outside the expresion that is quantified
          if ( !tools.containsVariable(`c,`v) ) {					
            result = `concOr(ForAll(v,c),result*);
          }else{
            result1 = `concOr(c,result1*);
          }			

          l = l.getTailconcOr();
        }

        // if couldn't do anything, return the same thing
        if (result.isEmptyconcOr()) {
          return `f;
        }

        result = result.reverse();
        result1 = result1.reverse();

        // if not all were separated
        if (!result1.isEmptyconcOr()) {
          result = `concOr(ForAll(v,Or(result1)),result*);
        }
        rulesCounter++;
        return `Or(result);
      }			

      // ///////////////////////////////////////////////////////////////////

      // Delete
      And(concAnd(_*,Equal(a,b),_*,NEqual(a,b),_*)) -> {
        rulesCounter++;
        return `False();
      }			
      And(concAnd(_*,NEqual(a,b),_*,Equal(a,b),_*)) -> {
        rulesCounter++;
        return `False();
      }		

      // Elimination of trivial equations and disequations
      Equal(a,a) -> {
        rulesCounter++;
        return `True();
      }			
      NEqual(a,a) -> {
        rulesCounter++;
        return `False();
      }

      // PropagateClash
      And(concAnd(_*,False(),_*)) -> {
        rulesCounter++;
        return `False();
      }

      Or(concOr(X*,False(),Y*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,Y*));
      }

      // PropagateSuccess
      And(concAnd(X*,True(),Y*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,Y*));
      }

      Or(concOr(_*,True(),_*)) -> {
        rulesCounter++;
        return `True();
      }

      // cleaning the result
      And(concAnd(X*,And(concAnd(Y*)),Z*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,Y*,Z*));
      }			

      Or(concOr(X*,Or(concOr(Y*)),Z*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,Y*,Z*));
      }

      And(concAnd(X*,a,Y*,a,Z*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,a,Y*,Z*));
      }

      Or(concOr(X*,a,Y*,a,Z*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,a,Y*,Z*));
      }

      And(concAnd()) -> {
        rulesCounter++;
        return `True();
      }

      Or(concOr()) -> {
        rulesCounter++;
        return `False();
      }

      And(concAnd(x)) -> {
        rulesCounter++;
        return `x;
      }

      Or(concOr(x)) -> {
        rulesCounter++;
        return `x;
      }

      // ////////////////////////////////////////////////////

      // SymbolClash
      Equal(Appl(name1,_),Appl(name2,_)) -> {
        if(`name1 != `name2) {
          rulesCounter++;
          return `False();
        }
      }

      NEqual(Appl(name1,_),Appl(name2,_)) -> {
        if(`name1 != `name2) {
          rulesCounter++;
          return `True();
        }
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
        rulesCounter++;
        l = l.reverse();
        return `And(l/* .reverseConstraintList() */);
      }

      NEqual(Appl(name,a1),Appl(name,a2)) -> {

        OConstraintList l = `concOr();
        TermList args1 = `a1;
        TermList args2 = `a2;
        while(!args1.isEmptyconcTerm()) {
          l = `concOr(NEqual(args1.getHeadconcTerm(),args2.getHeadconcTerm()),l*);
          args1 = args1.getTailconcTerm();
          args2 = args2.getTailconcTerm();					
        }
        rulesCounter++;
        l = l.reverse();
        return `Or(l/* .reverseConstraintList() */);
      }			

      // //////////////////////

      // merging rules - Comon and Lescanne

      // m1
      And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(Variable(z),u),Z*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,Equal(t,u),Z*));
      }			
      // m2
      Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,NEqual(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m3
      And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      And(concAnd(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        rulesCounter++;
        return `And(concAnd(X*,Equal(Variable(z),t),Y*,NEqual(t,u),Z*));
      }
      // m4
      Or(concOr(X*,Equal(Variable(z),t),Y*,NEqual(Variable(z),u),Z*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }
      Or(concOr(X*,NEqual(Variable(z),u),Y*,Equal(Variable(z),t),Z*)) -> {
        rulesCounter++;
        return `Or(concOr(X*,Equal(t,u),Y*,NEqual(Variable(z),u),Z*));
      }

    } // end visit
  } // end strategy


} // end class
