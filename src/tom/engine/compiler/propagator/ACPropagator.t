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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.exception.TomRuntimeException;
import java.util.ArrayList;
import tom.engine.compiler.Compiler;
import tom.engine.tools.TomConstraintPrettyPrinter;
import tom.engine.tools.ASTFactory;

/**
 * AC propagator
 */
public class ACPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  %typeterm ACPropagator {
    implement { ACPropagator }
    is_sort(t) { ($t instanceof ACPropagator) }
  }

  private Compiler compiler;  
  private ConstraintPropagator constraintPropagator; 
 
  public Compiler getCompiler() {
    return this.compiler;
  }
 
  public ACPropagator(Compiler compiler, ConstraintPropagator constraintPropagator) {
    this.compiler = compiler;
    this.constraintPropagator = constraintPropagator;
  }

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    Constraint result = constraint;
    //return `TopDownIdStopOnSuccess(ACMatching(this)).visitLight(constraint);		
    result = `RepeatId(TopDown(RemoveNonVariable(this))).visitLight(result);		
    result = `RepeatId(TopDown(PerformAbstraction(this))).visitLight(result);		
    result = `TopDown(CleanSingleVariable()).visitLight(result);		
    //Constraint res =  `TopDownWhenConstraint(ACMatching(this)).visitLight(constraint);		
    return result;
  }	

  %strategy RemoveNonVariable(acp: ACPropagator) extends Identity() {
    visit Constraint {
      m@MatchConstraint(pattern@RecordAppl[
          Option=optWithAC@concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*),
          NameList=namelist@(Name(tomName)), Slots=slots],subject) -> {
        OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);

        %match(slots) {
          /*
           * f(t,X,...) <<ac s -> (X1*,t,X2*) <<a s ^ f(X,...) <<ac f(X1*,X2*)
           */
          concSlot(C1*, slot@PairSlotAppl[SlotName=slotname, Appl=!VariableStar[]], C2*) -> {
            System.out.println("case F(t,X*,...): " + `slots);
            //System.out.println("slot: " + `slot);

            //generate f(X1*, slot, X2*) << s and modify s <- f(X1*,X2*)
            TomType listType = acp.getCompiler().getTermTypeFromTerm(`pattern);
            TomTerm X1 = acp.getCompiler().getFreshVariableStar(listType);				
            TomTerm X2 = acp.getCompiler().getFreshVariableStar(listType);
            TomTerm X3 = acp.getCompiler().getFreshVariableStar(listType);
            Constraint c1 = `MatchConstraint(RecordAppl(optWithoutAC,
                    namelist,
                    concSlot( PairSlotAppl(slotname, X1), slot, PairSlotAppl(slotname, X2)),
                    concConstraint()),subject);

            //generate f(X,...) << f(X1*,X2*)
            TomSymbol tomSymbol = acp.getCompiler().getSymbolTable().getSymbolFromName(`tomName);
            TomTerm newSubject = null;
            if(TomBase.isListOperator(tomSymbol)) {
              newSubject = ASTFactory.buildList(`Name(tomName),`concTomTerm(X1,X2),acp.getCompiler().getSymbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              newSubject = ASTFactory.buildArray(`Name(tomName),`concTomTerm(X1,X2),acp.getCompiler().getSymbolTable());
            }
            Constraint c2 = `MatchConstraint(RecordAppl(optWithAC,namelist,concSlot(C1*,C2*),concConstraint()), newSubject);

            Constraint result = `AndConstraint(c1,c2);
            //System.out.println("result: " + result);
            System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));
            return result;
          }
        }
      }
    }
  }

  %strategy CleanSingleVariable() extends Identity() {
    visit Constraint {
      /*
       * f(X*) <<ac s => f(X*) <<a s
       */
      m@MatchConstraint(pattern@RecordAppl[
          Option=optWithAC@concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*),
          NameList=namelist@(Name(tomName)), 
          Slots=slots@concSlot(PairSlotAppl[SlotName=slotname, Appl=VariableStar[]])],subject) -> {
        System.out.println("case f(X*) <<ac s => f(X*) <<a s: " + `slots);
        OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);
        Constraint result = `MatchConstraint(
            RecordAppl(optWithoutAC, namelist, slots, concConstraint()),
            subject);
        System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));
        return result;
      }
    }
  }

  %strategy PerformAbstraction(acp: ACPropagator) extends Identity() {
    visit Constraint {
      m@MatchConstraint(pattern@RecordAppl[
          Option=optWithAC@concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*),
          NameList=namelist@(Name(tomName)), Slots=slots],subject) -> {
        OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);
        if(`slots.length() > 2) {
          %match(slots) {
            /*
             * f(Z,X,...) <<ac s -> (Z,X1) <<ac s ^ f(X,...) <<ac X1
             */
            concSlot(C1*, vstar@PairSlotAppl[SlotName=slotname, Appl=VariableStar[]], C2*) -> {
              System.out.println("case F(Z*,X*,...): " + `slots);
              //generate: f(vstar,X1) << s 
              TomType listType = acp.getCompiler().getTermTypeFromTerm(`pattern);
              TomTerm X1 = acp.getCompiler().getFreshVariableStar(listType);				
              Constraint c1 = 
                `MatchConstraint(RecordAppl(optWithAC,
                      namelist,concSlot(vstar,PairSlotAppl(slotname,X1)),
                      concConstraint()),subject);
              //generate: f(X,...) << X1
              Constraint c2 = `MatchConstraint(RecordAppl(optWithAC,
                    namelist, concSlot(C1*,C2*), concConstraint()),X1);
              Constraint result = `AndConstraint(c1,c2);

              //System.out.println("result: " + result);
              System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));
              return result;
            }
          }
        }
      }

    }
  }

}
