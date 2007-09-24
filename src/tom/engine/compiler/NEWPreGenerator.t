/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2007, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Radu Kopetz e-mail: Radu.Kopetz@loria.fr
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/
package tom.engine.compiler;

import java.util.ArrayList;
import java.util.Iterator;

import com.sun.org.apache.xpath.internal.operations.And;

import tom.engine.TomBase;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.library.sl.*;


/**
 * This class is in charge with all the pre treatments for generation needed
 * after the propagation process
 * 1. make sure that the constraints are in the good order
 * 2. translate constraints into expressions ...
 */
public class PreGenerator {

  // ------------------------------------------------------------
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom}
  // ------------------------------------------------------------

  public static Expression performPreGenerationTreatment(Constraint constraint) throws VisitFailure{
    //constraint = (Constraint)`TopDown(OrderConstraints()).visitLight(constraint);    
    constraint = orderConstraints(constraint);
    return constraintsToExpressions(constraint);
  }

  /**
   * Puts the constraints in the good order
   *
   */
  %strategy OrderConstraints() extends Identity(){
    visit Constraint { 
      andC@AndConstraint(X*) -> {
        System.out.println("X=" + `X);
        //return arrageAndConstraint(`andC);
      }
    }
  }

  private static Constraint orderConstraints(Constraint constraint){
    //System.out.println("Ordering:" + constraint);
    %match(constraint){
      andC@!AndConstraint(X*,OrConstraint(XX*),Y*) && AndConstraint(_*) << constraint  -> {        
        return arrageAndConstraint(`andC);
      }
      andC@AndConstraint(X*,or@OrConstraint(XX*),Y*) -> {        
        return `arrageAndConstraint(AndConstraint(X*,orderConstraints(or),Y*));
      }
      or@OrConstraint(andC@AndConstraint(XX*)) -> {        
        return `OrConstraint(orderConstraints(andC));
      }
      or@OrConstraint(!AndConstraint(XX*)) -> {        
        return `or;
      }
    }    
    //System.out.println("DEFAULT");
    return constraint;
  }
  /**
   * TODO
   */
  private static Constraint arrageAndConstraint(Constraint andConstraint){
    Constraint[] result = new Constraint[((AndConstraint)andConstraint).length()];
    int elemInResult = 0;
    // flag indicating that a position was found in the vector to insert the constraint
    boolean foundBiggerValue = false;
    %match(andConstraint){
      AndConstraint(_*,x,_*) -> {        
        foundBiggerValue = false;
        int i;
forLoop:for(i = 0; i < elemInResult ; i++){
          // TODO
          // - replace %match(x) with %match{  ( when this will be available )
          // - shorter code with tom variables on the right side ( when this will be available )
          // - replace resultI with result[i] ( when this will be available )
          Constraint resultI = result[i];
          %match(x){
            /*
             * SwitchSymbolOf
             *
             * z << subterm(i,g) /\ S /\ f = SymbolOf(g) -> f = SymbolOf(g) /\ S /\ z << subterm(i,g)
             *
             */
            _ && MatchConstraint(_,Subterm[GroundTerm=g]) << resultI && MatchConstraint(_,SymbolOf(g)) << x -> {              
              foundBiggerValue = true;
              break forLoop;
            }
            /*
             * SwitchSymbolOf2
             *
             * A = SymbolOf(g) /\ S /\ g << B -> g << B /\ S /\ A = SymbolOf(g)
             * TODO - use disjunctions below
             */
            _ && MatchConstraint(_,SymbolOf(rhs)) << resultI && MatchConstraint(v,_) << x -> {
              boolean varFound = false;
        match:%match(rhs,TomTerm v) {
                var@(Variable|VariableStar)[],var  -> { varFound = true;break match; }
                ListHead[Variable=var],var -> { varFound = true;break match; }
                ListTail[Variable=var],var -> { varFound = true;break match; }
                ExpressionToTomTerm(GetSliceArray[VariableBeginAST=var]), var -> { varFound = true;break match; }
              }
              if (varFound) {                
                foundBiggerValue = true;
                break forLoop;
              }
            }
            /*
             * SwitchAnti
             *
             * an antimatch should be always at the end, after the match constraints
             * ex: for f(!x,x) << t -> we should generate x << t_2 /\ !x << t_1 and
             * not !x << t_1 /\ x << t_2 because at the generation the free x should
             * be propagated and not the other one
             */
            _ && AntiMatchConstraint[] << resultI && MatchConstraint[] << x  -> {              
              foundBiggerValue = true;
              break forLoop;
            }
           /*
            * SwitchEmpty - lists
            *
            * EmptyList(z) /\ S /\ z << t -> z << t /\ S /\ EmptyList(z)
            *
            * Negate(EmptyList(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyList(z))
            */
            _ && { EmptyListConstraint[Variable=v] << resultI || Negate(EmptyListConstraint[Variable=v]) << resultI }
                  && MatchConstraint(v,_) << x -> {                    
             foundBiggerValue = true;
             break forLoop;
           }
           /*
            * SwitchEmpty - arrays
            *
            * EmptyArray(z) /\ S /\ z << t -> z << t /\ S /\ EmptyArray(z)
            *
            * Negate(EmptyArray(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyArray(z))
            */
           _ && { EmptyArrayConstraint[Index=idx] << resultI || Negate(EmptyArrayConstraint[Index=idx]) << resultI }
               && MatchConstraint(idx,_) << x -> {
             foundBiggerValue = true;
             break forLoop;
           }
           /*
            * SwitchVar
            *
            * p << Context[z] /\ S /\ z << t -> z << t /\ S /\ p << Context[z]
            */
           _ && m@MatchConstraint(_,rhs) << resultI && MatchConstraint(v@(Variable|VariableStar)[],_) << x -> {
             try{
               `TopDown(HasTerm(v)).visitLight(`rhs);
             }catch(VisitFailure ex){     
               foundBiggerValue = true;
               break forLoop;
             }
           }
           _ && MatchConstraint(_,rhs) << resultI && OrConstraintDisjunction(AndConstraint(_*,MatchConstraint(v@(Variable|VariableStar)[],_),_*),_*) << x -> {
             try{
               `TopDown(HasTerm(v)).visitLight(`rhs);
             }catch(VisitFailure ex){               
               foundBiggerValue = true;
               break forLoop;
             }
           }
           /*
            * p << ListHead(z) /\ S /\ Negate(Empty(z)) -> Negate(Empty(z)) /\ S /\ p << ListHead(z)
            * p << ListTail(z) /\ S /\ Negate(Empty(z)) -> Negate(Empty(z)) /\ S /\ p << ListTail(z)
            *
            */
           _ && MatchConstraint(_,(ListHead|ListTail)[Variable=v]) << resultI
                  && { Negate(EmptyListConstraint[Variable=v]) << x || EmptyListConstraint[Variable=v] << x  } -> {                    
             foundBiggerValue = true;
             break forLoop;
           }
           /*
            * p << GetElement(z) /\ S /\ EmptyArray(z) -> EmptyArray(z) /\ S /\ p << GetElement(z)
            * p << GetElement(z) /\ S /\ Negate(EmptyArray(z)) -> Negate(EmptyArray(z)) /\ S /\ p << GetElement(z)
            */
           _ && m@MatchConstraint(_,ExpressionToTomTerm(GetElement[Kid2=v])) << resultI
                  && { EmptyArrayConstraint[Index=v] << x || Negate(EmptyArrayConstraint[Index=v]) << x } -> {
             foundBiggerValue = true;
             break forLoop;
           }
           /*
            * p << e /\ S /\ VariableHeadList(b,e) -> VariableHeadList(b,e) /\ S /\ p << e
            * p << e /\ S /\ VariableHeadArray(b,e) -> VariableHeadArray(b,e) /\ S /\ p << e
            *
            */
           _ && MatchConstraint(_,v@VariableStar[]) << resultI
                && { MatchConstraint(_,VariableHeadList[End=v]) << x || MatchConstraint(_,VariableHeadArray[EndIndex=v]) << x } -> {                  
             foundBiggerValue = true;
             break forLoop;
           }
           /*
            * SwitchNumericConstraints
            *
            * a numeric constraint should be always at the end, after the match constraints
            * because the numeric constraints never instantiate variables
            */
            _ && NumericConstraint[] << resultI && MatchConstraint[] << x -> {              
              foundBiggerValue = true;
              break forLoop;
            }
          } // match
        } // for
        if (foundBiggerValue){          
          insertConstraint(`x, result, i, elemInResult);
          elemInResult++;
        }else{
          // put the constraint at the end
          result[elemInResult] = `x;
          elemInResult++;
        }
      }
    }    
    andConstraint = `AndConstraint();
    for(Constraint c:result){      
      andConstraint = `AndConstraint(andConstraint*,c);
    }
    return andConstraint;
  }

  /**
   * TODO
   */
  private static Constraint[] insertConstraint(Constraint constraint, Constraint[] array, int index, int firstFreePosition){
    // move all constraints (from index) one position
    for(int i = firstFreePosition; i > index; i--){
      array[i] = array[i-1];
    }
    array[index] = constraint;
    return array;
  }

  /**
   * Checks to see if the term is inside
   */
  %strategy HasTerm(term:TomTerm) extends Identity(){
    visit TomTerm {
      x -> {
        if (`x == `term) { throw new VisitFailure(); }
      }
    }
  }

  /**
   * Translates constraints into expressions
   */
  private static Expression constraintsToExpressions(Constraint constraint){
    %match(constraint){
      AndConstraint(m,X*) -> {
        return `And(constraintsToExpressions(m),
            constraintsToExpressions(AndConstraint(X*)));
      }
      OrConstraint(m,X*) -> {
        return `OrConnector(constraintsToExpressions(m),
            constraintsToExpressions(OrConstraint(X*)));
      }
      OrConstraintDisjunction(m,X*) -> {
        return `OrExpressionDisjunction(constraintsToExpressions(m),
            constraintsToExpressions(OrConstraintDisjunction(X*)));
      }
      m@(MatchConstraint|NumericConstraint)[] -> {
        return `ConstraintToExpression(m);
      }
      AntiMatchConstraint(constr) -> {
        return `AntiMatchExpression(constraintsToExpressions(constr));
      }
      Negate(c) -> {
        return `Negation(constraintsToExpressions(c));
      }
      EmptyListConstraint(opName,variable) ->{
        return ConstraintGenerator.genIsEmptyList(`opName,`variable);
      }
      EmptyArrayConstraint(opName,variable,index) ->{
        return `IsEmptyArray(opName,variable,index);
      }
    }
    throw new TomRuntimeException("PreGenerator.constraintsToExpressions - strange constraint:" + constraint);
  }
}















