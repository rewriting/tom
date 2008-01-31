/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2008, INRIA
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

  public static Expression performPreGenerationTreatment(Constraint constraint) throws VisitFailure {
    constraint = orderConstraints(constraint);
    return constraintsToExpressions(constraint);
  }

  private static Constraint orderConstraints(Constraint constraint) {
    %match(constraint) {
      !AndConstraint(_*,OrConstraint(_*),_*) && AndConstraint(_*) << constraint  -> {
        return repeatOrdering(constraint);
      }
      AndConstraint(X*,or@OrConstraint(_*),Y*) -> {
        return repeatOrdering(`AndConstraint(X*,orderConstraints(or),Y*));
      }
      OrConstraint(andC@AndConstraint(_*)) -> {
        return `OrConstraint(orderConstraints(andC));
      }
      or@OrConstraint(!AndConstraint(_*)) -> {
        return `or;
      }
    }
    return constraint;
  }

  private static Constraint repeatOrdering(Constraint constraint) {
    Constraint result = constraint;
    do{
      constraint = result;
      result = orderAndConstraint(constraint);
    } while (result != constraint);
    return result;
  }

  /**
   * Puts the constraints in the good order
   *
   */
  private static Constraint orderAndConstraint(Constraint constraint) {
    %match(constraint) {
      /*
       * SwitchSymbolOf
       *
       * z << subterm(i,g) /\ S /\ f = SymbolOf(g) -> f = SymbolOf(g) /\ S /\ z << subterm(i,g)
       *
       */
      AndConstraint(X*,subterm@MatchConstraint(_,Subterm[GroundTerm=g]),Y*,symbolOf@MatchConstraint(_,SymbolOf(g)),Z*) -> {
        return `AndConstraint(X*,symbolOf,Y*,subterm,Z*);
      }

      /*
       * SwitchSymbolOf2
       *
       * A = SymbolOf(g) /\ S /\ g << B -> g << B /\ S /\ A = SymbolOf(g)
       *
       */
      AndConstraint(X*,first@MatchConstraint(_,SymbolOf(rhs)),Y*,second@MatchConstraint(var,_),Z*)
         && ( var@(Variable|VariableStar)[] << rhs || ListHead[Variable=var] << rhs || ListTail[Variable=var] << rhs || ExpressionToTomTerm(GetSliceArray[VariableBeginAST=var]) << rhs ) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);
      }
      /*
       * SwitchAnti
       *
       * an antimatch should be always at the end, after the match constraints
       * ex: for f(!x,x) << t -> we should generate x << t_2 /\ !x << t_1 and
       * not !x << t_1 /\ x << t_2 because at the generation the free x should
       * be propagated and not the other one
       */
      AndConstraint(X*,antiMatch@AntiMatchConstraint[],Y*,match@MatchConstraint[],Z*) -> {
        return `AndConstraint(X*,Y*,match,antiMatch,Z*);
      }

      /*
       * SwitchEmpty - lists
       *
       * EmptyList(z) /\ S /\ z << t -> z << t /\ S /\ EmptyList(z)
       * Negate(EmptyList(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyList(z))
       */
      AndConstraint(X*,first,Y*,second@MatchConstraint(v,_),Z*)
         && ( EmptyListConstraint[Variable=v] << first || Negate(EmptyListConstraint[Variable=v]) << first ) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);
      }

      /*
       * SwitchEmpty - arrays
       *
       * EmptyArray(z) /\ S /\ z << t -> z << t /\ S /\ EmptyArray(z)
       * Negate(EmptyArray(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyArray(z))
       */
      AndConstraint(X*,first,Y*,second@MatchConstraint(idx,_),Z*)
         && ( EmptyArrayConstraint[Index=idx] << first || Negate(EmptyArrayConstraint[Index=idx]) << first ) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);
      }

      /*
       * SwitchVar
       * TODO : replace with constraints when the or bugs in the optimizer are solved
       * p << Context[z] /\ S /\ z << t -> z << t /\ S /\ p << Context[z]
       */
       AndConstraint(X*,first@(MatchConstraint|NumericConstraint)[Subject=rhs],Y*,second@MatchConstraint(v@(Variable|VariableStar)[],_),Z*) -> {
         try {
           `TopDown(HasTerm(v)).visitLight(`rhs);
         }catch(VisitFailure ex){
           return `AndConstraint(X*,second,first,Y*,Z*);
         }
       }
       AndConstraint(X*,first@(MatchConstraint|NumericConstraint)[Subject=rhs],Y*,second@OrConstraintDisjunction(AndConstraint(_*,MatchConstraint(v@(Variable|VariableStar)[],_),_*),_*),Z*) -> {
         try {
           `TopDown(HasTerm(v)).visitLight(`rhs);
         }catch(VisitFailure ex){
           return `AndConstraint(X*,second,first,Y*,Z*);
         }
       }

      /*
       * p << ListHead(z) /\ S /\ Negate(Empty(z)) -> Negate(Empty(z)) /\ S /\ p << ListHead(z)
       * p << ListTail(z) /\ S /\ Negate(Empty(z)) -> Negate(Empty(z)) /\ S /\ p << ListTail(z)
       *
       * p << ListHead(z) /\ S /\ Empty(z) -> Empty(z) /\ S /\ p << ListHead(z)
       * p << ListTail(z) /\ S /\ Empty(z) -> Empty(z) /\ S /\ p << ListTail(z)
       *
       */
      AndConstraint(X*,first@MatchConstraint(_,(ListHead|ListTail)[Variable=v]),Y*,second,Z*)
          && ( Negate(EmptyListConstraint[Variable=v]) << second || EmptyListConstraint[Variable=v] << second ) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);
      }

      /*
       * p << GetElement(z) /\ S /\ Negate(EmptyArray(z)) -> Negate(EmptyArray(z)) /\ S /\ p << GetElement(z)
       * p << GetElement(z) /\ S /\ EmptyArray(z) -> EmptyArray(z) /\ S /\ p << GetElement(z)
       */
      AndConstraint(X*,first@MatchConstraint(_,ExpressionToTomTerm(GetElement[Variable=v])),Y*,second,Z*)
              && ( Negate(EmptyArrayConstraint[Index=v]) << second || EmptyArrayConstraint[Index=v] << second ) -> {
        return `AndConstraint(X*,second,Y*,first,Z*);
      }

      /*
       * p << e /\ S /\ VariableHeadList(b,e) -> VariableHeadList(b,e) /\ S /\ p << e
       * p << e /\ S /\ VariableHeadArray(b,e) -> VariableHeadArray(b,e) /\ S /\ p << e
       */
      AndConstraint(X*,first@MatchConstraint(_,v@VariableStar[]),Y*,second@MatchConstraint(_,subjectSecond),Z*)
        && (VariableHeadList[End=v] << subjectSecond || VariableHeadArray[EndIndex=v] << subjectSecond ) -> {
         return `AndConstraint(X*,second,Y*,first,Z*);
      }

      /*
       * SwitchNumericConstraints
       *
       * an numeric constraint on a variable x should be always imediately after the
       * instantiation of the variable x, as it may improve the efficiency by abandoning the tests earlier
       * 
       * it should not go upper than the declaration of one of its variables (the last 2 conditions)
       */
      AndConstraint(X*,match@MatchConstraint[Pattern=matchP@(Variable|VariableStar)[]],Y*,numeric@NumericConstraint[Pattern=x,Subject=y],Z*)
                      && (matchP << TomTerm x || matchP << TomTerm y) 
                      && !AndConstraint(_*,MatchConstraint[Pattern=x],_*) << AndConstraint(Y*)
                      && !AndConstraint(_*,MatchConstraint[Pattern=y],_*) << AndConstraint(Y*) -> {  
        return `AndConstraint(X*,match,numeric,Y*,Z*);
      }

      /*
       * SwitchIsSort
       *
       *  Match(_,subject) /\ IsSort(subject) -> IsSort(subject) /\ Match(_,subject)
       *
       *  IsSort(var) /\ Match(var,_) -> Match(var,_) /\ IsSort(var)
       *
       */
      AndConstraint(X*,match@MatchConstraint[Subject=ExpressionToTomTerm(Cast[Source=TomTermToExpression(sub)])],Y*,isSort@IsSortConstraint[TomTerm=sub],Z*) -> {
        return `AndConstraint(X*,isSort,Y*,match,Z*);
      }
      AndConstraint(X*,isSort@IsSortConstraint[TomTerm=var@(Variable|VariableStar)[]],Y*,match@MatchConstraint(var,_),Z*) -> {
        return `AndConstraint(X*,match,Y*,isSort,Z*);
      }

      /*
       * SwitchTestVars
       *
       * tests generated by replace shoud be after the variable has been instanciated
       */
      AndConstraint(X*,first@MatchConstraint(TestVar(x),_),Y*,second@MatchConstraint(x,_),Z*) -> {        
        return `AndConstraint(X*,second,Y*,first,Z*);
      }
    } // end visit
    return constraint;
  }// end strategy

  /**
   * Checks to see if the term is inside
   */
  %strategy HasTerm(term:TomTerm) extends Identity(){
    visit TomTerm {
      x -> {
        if (`x == `term) { throw new VisitFailure(); }
      }
    }// end visit
  }// end strategy

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
      IsSortConstraint(type,tomTerm) ->{
        return `IsSort(type,tomTerm);
      }
    }
    throw new TomRuntimeException("PreGenerator.constraintsToExpressions - strange constraint:" + constraint);
  }
}
