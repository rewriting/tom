/*
 * Copyright (c) 2004-2011, INPL, INRIA
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
import antipattern.*;

import java.util.*;

import antipattern.associative.termaso.*;
import antipattern.associative.termaso.types.*;
import tom.library.sl.*;



//simple algorithm implementing the associative matching
public class ClassicalAssociativity {

  %include{ termaso/TermAso.tom }	
  %include{ sl.tom }
  %include{ java/util/types/Collection.tom}

  private static int globalCounter = 0; 

  public Constraint simplifyAndSolve(Constraint c, Collection solution) {
    try {
      return (Constraint)`InnermostId(PerformAssociativeMatching()).visit(c);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("Failure");
    }
  }

  %strategy PerformAssociativeMatching() extends Identity(){

    visit Constraint{		
      // EqTransform
      Match(p,s) -> {
        return `Equal(p,s);
      }

      // Decompose 1 & 2
      eq@Equal(f@Appl(name,a1),g@Appl(name,a2)) -> {

        if (`name.startsWith("e_")) {	        	
          return `eq; // do nothing
        }

        Constraint l = `And();
        TermList args1 = `a1;
        TermList args2 = `a2;
        while(!args1.isEmptyconcTerm()) {
          l = `And(l*,Equal(args1.getHeadconcTerm(),args2.getHeadconcTerm()));
          args1 = args1.getTailconcTerm();
          args2 = args2.getTailconcTerm();
        }
        // if we do not have an associative symbol
        if ( !isAssociative(`f) ){
          return `And(l);
        }
        // ///// Decompose 2 ////////////////////////////
        // reinitialize
        args1 = `a1;
        args2 = `a2;

        Term p_1 = args1.getHeadconcTerm(); // first elem
        Term p_2 = args1.getTailconcTerm().getHeadconcTerm(); // second elem

        Term t_1 = args2.getHeadconcTerm(); // first elem
        Term t_2 = args2.getTailconcTerm().getHeadconcTerm(); // second elem

        Term x_1 = `Variable("x" + ++globalCounter + "_a");
        Term x_2 = `Variable("x" + ++globalCounter + "_a");

        Constraint secondTerm = `Exists(x_1,Exists(x_2,
            And( Equal(p_1,x_1), 
                Equal(p_2,Appl(name,concTerm(x_2,t_2))),
                Equal(Appl(name,concTerm(x_1,x_2)),t_1)
            )
        ));
        Constraint thirdTerm = `Exists(x_1,Exists(x_2,
            And( Equal(p_1,Appl(name,concTerm(t_1,x_1))), 
                Equal(p_2,x_2),
                Equal(Appl(name,concTerm(x_1,x_2)),t_2)
            )
        ));

        return `Or(And(l),secondTerm,thirdTerm);
      }

      // Decompose 3
      eq@Equal(f@Appl(name1,a1),g@Appl(name2,a2)) -> {

        if (`name1.startsWith("e_")) {	        	
          return `eq; // do nothing
        }

        if (`name1 != `name2 && isAssociative(`f)){	        		
          Term p_1 = `a1.getHeadconcTerm(); // first elem
          Term p_2 = `a1.getTailconcTerm().getHeadconcTerm(); // second elem

          Constraint firstTerm =  `And(
              Equal(p_1,getNeutralElem(f)),
              Equal(p_2,g)
          );
          Constraint secondTerm =  `And(
              Equal(p_1,g),
              Equal(p_2,getNeutralElem(f))
          );

          return `Or(firstTerm,secondTerm);
        }
      }

      // SymbolClash 1
      Equal(f@Appl(name1,args1),Appl(name2,args2)) -> {
        if(`name1 != `name2 && !isAssociative(`f)) {
          return `False();
        }
      }

//    // SymbolClash 2
//    Equal(v@Variable(_),f@Appl[]) -> {
//    if(!isAssociative(`v) && isAssociative(`f)) {
//    return `False();
//    }
//    }

      // ConstrainedVar 1,2
      Equal(ConsVariable(v@var,parentName),f@Appl[]) -> {

        int sizeF = getSize(`parentName,`f);	     

        if ( sizeF == 1){	        		
          return `Equal(v,f);	        	  
        }else if(sizeF != -1){
          return `False();
        }
      }        

      // Delete
      Equal(t1@Appl(name,_),t2@Appl(name,_)) -> {
        if (`t1 == `t2) {  
          return `True();        	
        }
      }

      // PropagateClash 1
      And(_*,False(),_*) -> {
        return `False();
      }

      // Distribution of And and Or
      And(X*,cOr@Or(Z*),Y*) ->{
        Constraint result = `Or();        
        %match(cOr){
          Or(_*,x,_*) ->{ result = `Or(result*,And(X*,x,Y*)); }
        }        
        return `Or(result);
      }

      // Exists
      Exists(var,x) ->{
        // if the variable does not exists - remove the Exists
        // if the variable exists only in one eq, just replace with true the
        // eq
        if (nbOccurences(`var,`x) <= 1){
          return (Constraint)`TopDown(ReplaceEquality(var)).visit(`x);
        }
      }

      // Exists
      Exists(v@Variable(var),l@Or(_*)) ->{        
        Constraint result = `Or();
        %match(l){
          Or(_*,x,_*) ->{ result = `Or(Exists(v,x),result*); }
        }
        return result;
      }

      // Replace
      input@And(X*,equal@Equal(var@Variable(name),s),Y*) -> {				
        Constraint toApplyOn = `And(X*,Y*);				
        Constraint res = (Constraint)`TopDown(ReplaceStrat(var,s)).visit(toApplyOn);
        if (res != toApplyOn){					
          return `And(equal,res);
        }
      }

      // cleaning
      Or(X*,x,Y*,x,Z*)->{
        return `Or(X*,x,Y*,Z*);
      }

    } // end visit
  }

  private int counter = 0;	
  private int nbOccurences(Term t, Constraint toSearchIn){
    counter = 0;
    try {
      `TopDown(CheckOccurence(t)).visit(toSearchIn);
    } catch(VisitFailure e) {
      throw new tom.engine.exception.TomRuntimeException("Failure");
    }
    return counter;
  }	

  %strategy CheckOccurence(t:Term) extends `Identity(){
    visit Term {
      x -> {
        if (t == `x){
          counter++;
        }
      }
    }
  }

  private int getSize(String parentName, Term t){
    try{
      return size(parentName,t);
    }catch(Exception e){
      return -1;
    }
  }

  private int size(String parentName, Term t){		
    %match(t){
      Appl(name,childs) ->{
        // System.out.println("name=" + `name);
        // System.out.println("parentName=" + parentName);
        if (`name.startsWith("e_")) { return 0; }
        if (!`name.endsWith("_a") || `name != parentName) { return 1; }
        if (`name.endsWith("_a")){
          int finalRes = 0;
          %match(childs){
            concTerm(_*,x,_*) ->{
              // System.out.println("computing for:" +`x);
              finalRes += size(parentName,`x);
            }
          }
          return finalRes;
        }
      }
    }// end match

    throw new RuntimeException("Cannot compute the size of t=" + t);
  }


  private boolean isAssociative(Term t){
    %match(t){
      Appl(name,_) -> {
        if (`name.endsWith("_a") /* || `name.startsWith("e_") */) {
          return true;
        }else{
          return false;
        }
      }

//    Variable(name) -> {
//    if (`name.endsWith("_a")) {
//    return true;
//    }else{
//    return false;
//    }
//    }
    }

    return false;
  }

  private Term getNeutralElem(Term t){
    %match(t){
      Appl(name,_) -> {
        return `Appl("e_" + ((String)name).substring(0,name.indexOf("_")),concTerm());
      }
    }		
    throw new RuntimeException("Cannot compute neutral elem for:" + t);
  }

  %strategy ReplaceStrat(var:Term, value:Term) extends `Identity(){
    visit Term {
      x -> {
        if (`x == var) { return value; }  
      }
    }
  }

  %strategy ReplaceEquality(var:Term) extends `Identity(){
    visit Constraint {
      Equal(x,_) -> {
        if (`x == var) { return `True(); }  
      }
    }
  }

}// end class
