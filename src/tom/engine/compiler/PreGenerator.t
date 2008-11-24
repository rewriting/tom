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

  private ConstraintGenerator constraintGenerator;

  public PreGenerator(ConstraintGenerator myConstraintGenerator) {
    this.constraintGenerator = myConstraintGenerator;
  } 

  public ConstraintGenerator getConstraintGenerator() {
    return this.constraintGenerator;
  }

  public Expression performPreGenerationTreatment(Constraint constraint) throws VisitFailure {
    constraint = orderConstraints(constraint);
    return constraintsToExpressions(constraint);
  }

  private Constraint orderConstraints(Constraint constraint) {
    %match(constraint) {
      !AndConstraint(_*,OrConstraint(_*),_*) && AndConstraint(_*) << constraint  -> {
        return orderAndConstraint(constraint);
      }
      AndConstraint(X*,or@OrConstraint(_*),Y*) -> {
        return orderAndConstraint(`AndConstraint(X*,orderConstraints(or),Y*));
      }
      /*
      OrConstraint(andC@AndConstraint(_*)) -> {
        return `OrConstraint(orderConstraints(andC));
      }
      or@OrConstraint(!AndConstraint(_*)) -> {
        return `or;
      }
      */
    }
    return constraint;
  }
/*
  private Constraint repeatOrdering(Constraint constraint) {
    Constraint result = constraint;
    do {
      constraint = result;
      result = orderAndConstraint(constraint);
    } while (result != constraint);
    return result;
  }
  */

  /**
   * Puts the constraints in the good order
   * We use a loop and two nested match to be more efficient
   *
   */
  private Constraint orderAndConstraint(Constraint constraint) {
    Constraint toOrder = constraint;
    do {
      constraint = toOrder;
block: %match(constraint) {
         AndConstraint(X*,first,Y*,second,Z*) -> {
           %match(first,second) {
             /*
              * SwitchSymbolOf
              *
              * z << subterm(i,g) /\ S /\ f = SymbolOf(g) -> f = SymbolOf(g) /\ S /\ z << subterm(i,g)
              *
              */
             MatchConstraint(_,Subterm[GroundTerm=g]),MatchConstraint(_,SymbolOf(g)) -> {
               toOrder =  `AndConstraint(X*,second,Y*,first,Z*);
               break block;
             }

             /*
              * SwitchSymbolOf2
              *
              * A = SymbolOf(g) /\ S /\ g << B -> g << B /\ S /\ A = SymbolOf(g)
              *
              */
             MatchConstraint(_,SymbolOf(var@(Variable|VariableStar)[])),MatchConstraint(var,_) -> {
               toOrder = `AndConstraint(X*,second,Y*,first,Z*);
               break block;
             }
             /*
              * SwitchAnti
              *
              * an antimatch should be always at the end, after the match constraints
              * ex: for f(!x,x) << t -> we should generate x << t_2 /\ !x << t_1 and
              * not !x << t_1 /\ x << t_2 because at the generation the free x should
              * be propagated and not the other one
              */
             AntiMatchConstraint[],MatchConstraint[] -> {
               toOrder = `AndConstraint(X*,Y*,second,first,Z*);
               break block;
             }

             /*
              * SwitchEmpty - lists
              *
              * EmptyList(z) /\ S /\ z << t -> z << t /\ S /\ EmptyList(z)
              * Negate(EmptyList(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyList(z))
              */
             _,MatchConstraint(v,_)
               && ( EmptyListConstraint[Variable=v] << first || Negate(EmptyListConstraint[Variable=v]) << first ) -> {
                 toOrder = `AndConstraint(X*,second,Y*,first,Z*);
                 break block;
               }

             /*
              * SwitchEmpty - arrays
              *
              * EmptyArray(z) /\ S /\ z << t -> z << t /\ S /\ EmptyArray(z)
              * Negate(EmptyArray(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyArray(z))
              */
             _,MatchConstraint(idx,_)
               && ( EmptyArrayConstraint[Index=idx] << first || Negate(EmptyArrayConstraint[Index=idx]) << first ) -> {
                 toOrder = `AndConstraint(X*,second,Y*,first,Z*);
                 break block;
               }

             /*
              * SwitchVar
              * TODO : replace with constraints when the or bugs in the optimizer are solved + the or is correctly handled for lists, i.e. with the duplication of the action
              * p << Context[z] /\ S /\ z << t -> z << t /\ S /\ p << Context[z]
              */
             (MatchConstraint|NumericConstraint)[Subject=rhs],MatchConstraint(v@(Variable|VariableStar)[],_) -> {
               try {
                 `TopDown(HasTerm(v)).visitLight(`rhs);
               } catch(VisitFailure ex) {
                 toOrder = `AndConstraint(X*,second,first,Y*,Z*);
                 break block;
               }
             }

             (MatchConstraint|NumericConstraint)[Subject=rhs],OrConstraintDisjunction(AndConstraint?(_*,MatchConstraint(v@(Variable|VariableStar)[],_),_*),_*) -> {
               try {
                 `TopDown(HasTerm(v)).visitLight(`rhs);
               } catch(VisitFailure ex) {
                 toOrder = `AndConstraint(X*,second,first,Y*,Z*);
                 break block;
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
             MatchConstraint(_,(ListHead|ListTail)[Variable=v]),_
               && ( Negate(EmptyListConstraint[Variable=v]) << second || EmptyListConstraint[Variable=v] << second ) -> {
                 toOrder = `AndConstraint(X*,second,Y*,first,Z*);
                 break block;
               }

             /*
              * p << GetElement(z) /\ S /\ Negate(EmptyArray(z)) -> Negate(EmptyArray(z)) /\ S /\ p << GetElement(z)
              * p << GetElement(z) /\ S /\ EmptyArray(z) -> EmptyArray(z) /\ S /\ p << GetElement(z)
              */
             MatchConstraint(_,ExpressionToTomTerm(GetElement[Variable=v])),_
               && ( Negate(EmptyArrayConstraint[Index=v]) << second || EmptyArrayConstraint[Index=v] << second ) -> {
                 toOrder = `AndConstraint(X*,second,Y*,first,Z*);
                 break block;
               }

             /*
              * p << e /\ S /\ VariableHeadList(b,e) -> VariableHeadList(b,e) /\ S /\ p << e
              * p << e /\ S /\ VariableHeadArray(b,e) -> VariableHeadArray(b,e) /\ S /\ p << e
              */
             MatchConstraint(_,v@VariableStar[]),MatchConstraint(_,subjectSecond)
               && (VariableHeadList[End=v] << subjectSecond || VariableHeadArray[EndIndex=v] << subjectSecond ) -> {
                 toOrder = `AndConstraint(X*,second,Y*,first,Z*);
                 break block;
               }

             /*
              * SwitchNumericConstraints
              *
              * an numeric constraint on a variable x should be always imediately after the
              * instantiation of the variable x, as it may improve the efficiency by abandoning the tests earlier
              * 
              * it should not go upper than the declaration of one of its variables (the last 2 conditions)
              */
             MatchConstraint[Pattern=matchP@(Variable|VariableStar)[]],NumericConstraint[Pattern=x,Subject=y]
               && (matchP << TomTerm x || matchP << TomTerm y) 
               /* we need '?' because Y* can be reduced to a single element */
               && !AndConstraint?(_*,MatchConstraint[Pattern=x],_*) << Y 
               && !AndConstraint?(_*,MatchConstraint[Pattern=y],_*) << Y -> {
                 toOrder = `AndConstraint(X*,first,second,Y*,Z*);
                 break block;
               }

             /*
              * SwitchIsSort
              *
              *  Match(_,subject) /\ IsSort(subject) -> IsSort(subject) /\ Match(_,subject)
              *
              *  IsSort(var) /\ Match(var,_) -> Match(var,_) /\ IsSort(var)
              *
              */
             MatchConstraint[Subject=ExpressionToTomTerm(Cast[Source=TomTermToExpression(sub)])],IsSortConstraint[TomTerm=sub] -> {
               toOrder = `AndConstraint(X*,second,Y*,first,Z*);
               break block;
             }
             IsSortConstraint[TomTerm=var@(Variable|VariableStar)[]],MatchConstraint(var,_) -> {
               toOrder = `AndConstraint(X*,second,Y*,first,Z*);
               break block;
             }

             /*
              * SwitchTestVars
              *
              * tests generated by replace shoud be after the variable has been instanciated
              */
             MatchConstraint(TestVar(x),_),MatchConstraint(x,_) -> {        
               toOrder = `AndConstraint(X*,second,Y*,first,Z*);
               break block;
             }

           }
         }

         _ -> {
           return constraint;
         }
       }
    } while (toOrder != constraint);
    return constraint;
  }

  /**
   * Checks to see if the term is inside
   */
  %strategy HasTerm(term:TomTerm) extends Identity() {
    visit TomTerm {
      x -> {
        if(`x == term) { throw new VisitFailure(); }
      }
    }
  }

  /**
   * Translates constraints into expressions
   */
  private Expression constraintsToExpressions(Constraint constraint) {
    %match(constraint) {
      AndConstraint(m,X*) -> {
        return `And(constraintsToExpressions(m), constraintsToExpressions(X*));
      }
      OrConstraint(m,X*) -> {
        return `OrConnector(constraintsToExpressions(m), constraintsToExpressions(X*));
      }
      OrConstraintDisjunction(m,X*) -> {
        return `OrExpressionDisjunction(constraintsToExpressions(m), constraintsToExpressions(X*));
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
      EmptyListConstraint(opName,variable) -> {
        return getConstraintGenerator().genIsEmptyList(`opName,`variable);
      }
      EmptyArrayConstraint(opName,variable,index) -> {
        return `IsEmptyArray(opName,variable,index);
      }
      IsSortConstraint(type,tomTerm) -> {
        return `IsSort(type,tomTerm);
      }
    }
    throw new TomRuntimeException("PreGenerator.constraintsToExpressions - strange constraint:" + constraint);
  }
}
