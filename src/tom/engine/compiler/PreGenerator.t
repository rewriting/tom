/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2015, Universite de Lorraine, Inria
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
import tom.engine.tools.TomConstraintPrettyPrinter;
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
    }
    return constraint;
  }

  /*
   * swap array[i] and array[j]
   * @return true if a modification has been performed
   */
  private boolean swap(Constraint[] array,int i,int j) {
    boolean res = array[i]!=array[j];
    Constraint tmp = array[i];
    array[i] = array[j];
    array[j] = tmp;
    return res;
  }

  /*
   * (X*,i,Y*,j,Z*) -> (X*,Y*,j,i,Z*)
   * @return true if a modification has been performed
   */
  private boolean buildXYjiZ(Constraint[] array,int i,int j) {
    boolean res = array[i]!=array[j];
    Constraint tmp = array[i];
    for(int k=i+1 ; k<j+1 ; k++) {
      array[k-1] = array[k];
      res |= (array[k]!= tmp);
    }
    array[j] = tmp;
    return res;
  }

  /*
   * (X*,i,Y*,j,Z*) -> (X*,j,i,Y*,Z*)
   * @return true if a modification has been performed
   */
  private boolean buildXjiYZ(Constraint[] array,int i,int j) {
    boolean res = array[i]!=array[j];
    Constraint tmp = array[i];
    array[i]=array[j];
    for(int k=j-1 ; k>i ; k--) {
      array[k+1] = array[k];
      res |= (array[k]!= tmp);
    }
    array[i+1]=tmp;
    return res;
  }

  /*
   * (X*,i,Y*,j,Z*) -> (X*,i,j,Y*,Z*)
   * @return true if a modification has been performed
   */
  private boolean buildXijYZ(Constraint[] array,int i,int j) {
    boolean res = array[i]!=array[j];
    Constraint tmp = array[j];
    for(int k=j-1 ; k>i ; k--) {
      array[k+1] = array[k];
      res |= (array[k]!= tmp);
    }
    array[i+1]=tmp;
    return res;
  }

  private Constraint buildAndConstraintFromArray(Constraint[] array) {
    Constraint list = `AndConstraint();
    for(int i=array.length-1; i>=0 ; i--) {
      Constraint tmp = array[i];
      list = `AndConstraint(tmp,list*);
    }
    return list;
  }

  /**
   * Puts the constraints in the good order
   * We use a loop and two nested match to be more efficient
   *
   */
  private Constraint orderAndConstraint(Constraint constraint) {
    Constraint[] array = new Constraint[constraint.length()];
    array = ((tom.engine.adt.tomconstraint.types.constraint.AndConstraint)constraint).toArray(array);
    boolean modification = false;
    do {
      //System.out.println("C = " + buildAndConstraintFromArray(array));
      //System.out.println("C = " + tom.engine.tools.TomConstraintPrettyPrinter.prettyPrint(buildAndConstraintFromArray(array)));
block: {
      /*
       * first version: 
       * start from 1 to ignore the first constraint which is only due to renaming subjects
       * not correct because some %match do not have a subject
       *
       * second version:
       * start from 0
       */
      for(int i=0 ; i<array.length-1 ; i++) {
loop_j: for(int j=i+1 ; j<array.length ; j++) {
          Constraint first = array[i];
          Constraint second = array[j];
          //System.out.println("first  = " + first);
          //System.out.println("second = " + second);
          %match(first,second) {
            /*
             * SwitchSymbolOf
             *
             * z << subterm(i,g) /\ S /\ f = SymbolOf(g) -> f = SymbolOf(g) /\ S /\ z << subterm(i,g)
             *
             */
            MatchConstraint[Subject=Subterm[GroundTerm=g]],MatchConstraint[Subject=SymbolOf(g)] -> {
              modification |= swap(array,i,j);
              break block;
            }

            /*
             * SwitchSymbolOf2
             *
             * A = SymbolOf(g) /\ S /\ g << B -> g << B /\ S /\ A = SymbolOf(g)
             *
             */
            MatchConstraint[Subject=SymbolOf(BQVariable[AstName=name])],MatchConstraint[Pattern=Variable[AstName=name]] -> {
              modification |= swap(array,i,j);
              break block;
            }
            MatchConstraint[Subject=SymbolOf(BQVariableStar[AstName=name])],MatchConstraint[Pattern=VariableStar[AstName=name]] -> {
              modification |= swap(array,i,j);
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
              modification |= buildXYjiZ(array,i,j);
              break block;
            }

            /*
             * SwitchEmpty - lists
             *
             * EmptyList(z) /\ S /\ z << t -> z << t /\ S /\ EmptyList(z)
             * Negate(EmptyList(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyList(z))
             */
            _,MatchConstraint[Pattern=VariableStar[AstName=name]]
              && ( EmptyListConstraint[Variable=BQVariableStar[AstName=name]] << first || Negate(EmptyListConstraint[Variable=BQVariableStar[AstName=name]]) << first ) -> {
                modification |= swap(array,i,j);
                break block;
              }

            /*
             * SwitchEmpty - arrays
             *
             * EmptyArray(z) /\ S /\ z << t -> z << t /\ S /\ EmptyArray(z)
             * Negate(EmptyArray(z)) /\ S /\ z << t -> z << t /\ S /\ Negate(EmptyArray(z))
             */
            _,MatchConstraint[Pattern=VariableStar[AstName=name]]
              && ( EmptyArrayConstraint[Index=BQVariableStar[AstName=name]] << first || Negate(EmptyArrayConstraint[Index=BQVariableStar[AstName=name]]) << first ) -> {
                modification |= swap(array,i,j);
                break block;
              }

            /*
             * SwitchVar
             * 
             * t /\ S /\ p << z -> p << z /\ S /\ t 
             * if p occurs in t when t is a NumericConstraint
             * or if p occurs in the subject of t when t is a MatchConstraint
             */
            t, MatchConstraint[Pattern=v@(Variable|VariableStar)[]] -> {
              try {
                `TopDown(HasBQTerm(v)).visitLight(`t);
              } catch(VisitFailure ex) {
                modification |= buildXjiYZ(array,i,j);
                break block;
              }
            }

            /*
             * SwitchVar
             * 
             * t /\ S /\ (f|g)[arg=p] << z -> (f|g)[arg=p]<< z /\ S /\ t 
             * if p occurs in t when t is a NumericConstraint
             * or if p occurs in the subject of t when t is a MatchConstraint
             */
            t,OrConstraintDisjunction(AndConstraint?(_*,MatchConstraint[Pattern=v@(Variable|VariableStar)[]],_*),_*) -> {
              try {
                `TopDown(HasBQTerm(v)).visitLight(`t);
              } catch(VisitFailure ex) {
                modification |= buildXjiYZ(array,i,j);
                break block;
              }
            }

            /*
             * SwitchVar
             * 
             * t /\ S /\ (p1 << z1 || p2 << z2) -> (p1 << z1 || p2 << z2) /\ S /\ t 
             * if p1 (or p2) occurs in t when t is a NumericConstraint
             * or if p1 (or p2) occurs in the subject of t when t is a MatchConstraint
             */
            t,OrConstraint(AndConstraint?(_*,MatchConstraint[Pattern=v@(Variable|VariableStar)[]],_*),_*) -> {
              try {
                `TopDown(HasBQTerm(v)).visitLight(`t);
              } catch(VisitFailure ex) {
                modification |= buildXjiYZ(array,i,j);
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
            MatchConstraint[Subject=(ListHead|ListTail)[Variable=v]],_
              && ( Negate(EmptyListConstraint[Variable=v]) << second || EmptyListConstraint[Variable=v] << second ) -> {
                modification |= swap(array,i,j);
                break block;
              }

            /*
             * p << GetElement(z) /\ S /\ Negate(EmptyArray(z)) -> Negate(EmptyArray(z)) /\ S /\ p << GetElement(z)
             * p << GetElement(z) /\ S /\ EmptyArray(z) -> EmptyArray(z) /\ S /\ p << GetElement(z)
             */
            MatchConstraint[Subject=ExpressionToBQTerm(GetElement[Variable=v])],_
              && ( Negate(EmptyArrayConstraint[Index=v]) << second || EmptyArrayConstraint[Index=v] << second ) -> {
                modification |= swap(array,i,j);
                break block;
              }

            /*
             * p << e /\ S /\ VariableHeadList(b,e) -> VariableHeadList(b,e) /\ S /\ p << e
             * p << e /\ S /\ VariableHeadArray(b,e) -> VariableHeadArray(b,e) /\ S /\ p << e
             */
            MatchConstraint[Subject=v@BQVariableStar[]],MatchConstraint[Subject=subjectSecond]
              && (VariableHeadList[End=v] << subjectSecond || VariableHeadArray[EndIndex=v] << subjectSecond ) -> {
                modification |= swap(array,i,j);
                break block;
              }

            /*
             * SwitchNumericConstraints
             *
             * a numeric constraint on a variable x should be always immediately after the
             * instantiation of the variable x, as it may improve the efficiency by abandoning the tests earlier
             * 
             * it should not go upper than the declaration of one of its variables (the last 2 conditions)
             */
           /* 
            MatchConstraint[Pattern=matchP@(Variable|VariableStar)[]],NumericConstraint[Pattern=x,Subject=y]
              && (matchP << TomTerm x || matchP << TomTerm y) 
              // we need '?' because Y* can be reduced to a single element
              //&& !AndConstraint?(_*,MatchConstraint[Pattern=x],_*) << Y 
              //&& !AndConstraint?(_*,MatchConstraint[Pattern=y],_*) << Y -> 
                -> {
                  boolean fire = true;
                  for(int k=i+1; k<j ; k++) { // go over Y
                    Constraint element = array[k];
                    System.out.println("element: " + element);
                    %match(element) {
                      (MatchConstraint|NumericConstraint)[Pattern=z] -> {
                        if(`z==`x || `z==`y) {
                          fire = false;
                          break loop_j;
                        }
                      }
                    }
                  }
                  if(fire && j-i>1) {
                    //System.out.println("rule 11: " + (j-i));
                    modification |= buildXijYZ(array,i,j);
                    break block;
                  }
              }
*/


            /*
             * SwitchIsSort
             *
             *  Match(_,subject) /\ IsSort(subject) -> IsSort(subject) /\ Match(_,subject)
             *
             *  IsSort(var) /\ Match(var,_) -> Match(var,_) /\ IsSort(var)
             *
             */
            MatchConstraint[Subject=ExpressionToBQTerm(Cast[Source=BQTermToExpression(sub)])],IsSortConstraint[BQTerm=sub] -> {
              modification |= swap(array,i,j);
              break block;
            }
            IsSortConstraint[BQTerm=BQVariable[AstName=name]],MatchConstraint[Pattern=Variable[AstName=name]] -> {
              modification |= swap(array,i,j);
              break block;
            }
            IsSortConstraint[BQTerm=BQVariableStar[AstName=name]],MatchConstraint[Pattern=VariableStar[AstName=name]] -> {
              modification |= swap(array,i,j);
              break block;
            }

            /*
             * SwitchTestVars
             *
             * tests generated by replace shoud be after the variable has been instanciated
             */
            MatchConstraint[Pattern=TestVar(x)],MatchConstraint[Pattern=x] -> {        
              modification |= swap(array,i,j);
              break block;
            }
          } // end %match
        }
      }
      //System.out.println("after ordering = " + tom.engine.tools.TomConstraintPrettyPrinter.prettyPrint(buildAndConstraintFromArray(array)));

      return buildAndConstraintFromArray(array);
       }// block
    } while (modification == true);
    return buildAndConstraintFromArray(array);
  }

  /**
   * Checks to see if the term is inside
   */

  %strategy HasBQTerm(term:TomTerm) extends Identity() {
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {
        %match(term) {
          (Variable|VariableStar)[AstName=n] -> {
            if(`name == `n) {
              throw new VisitFailure();
            }
          }
        }
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
