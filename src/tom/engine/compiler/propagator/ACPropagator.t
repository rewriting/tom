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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.code.types.*;
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
    result = `RepeatId(TopDown(RemoveNonVariableStar(this))).visitLight(result);		
    result = `RepeatId(TopDown(RemoveNonLinearVariableStar(this))).visitLight(result);		
    result = `RepeatId(TopDown(PerformAbstraction(this))).visitLight(result);		
    result = `TopDown(CleanSingleVariable()).visitLight(result);		
    //Constraint res = `TopDownWhenConstraint(ACMatching(this)).visitLight(constraint);		
    if(result!=constraint) {
      /*System.out.println("after propagate:");*/
      /*System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));*/
      /*System.out.println();*/
    }
    return result;
  }	

  /**
   * remove a term which is not a VariableStar
   * f(t,...) <<ac s -> (X1*,t,X2*) <<a s /\ f(...) <<ac f(X1*,X2*)
   */
  %strategy RemoveNonVariableStar(acp: ACPropagator) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=RecordAppl[
          Options=optWithAC@concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*),
          NameList=namelist@concTomName(Name(tomName)),
          Slots=slots],Subject=subject,AstType=aType] -> {
        OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);

        %match(slots) {
          /*
           * f(t,...) <<ac s -> (X1*,t,X2*) <<a s /\ f(...) <<ac f(X1*,X2*)
           */
          concSlot(C1*, slot@PairSlotAppl[SlotName=slotname, Appl=!VariableStar[]], C2*) -> {
            /*System.out.println("case F(t,...): " + `slots);*/
            /*System.out.println("slot: " + `slot);*/

            //generate f(X1*, slot, X2*) << s and modify s <- f(X1*,X2*)
            BQTerm X1 = acp.getCompiler().getFreshVariableStar(`aType);			
            BQTerm X2 = acp.getCompiler().getFreshVariableStar(`aType);
            BQTerm X3 = acp.getCompiler().getFreshVariableStar(`aType);
            Constraint c1 = `MatchConstraint(RecordAppl(optWithoutAC,
                    namelist,
                    concSlot( PairSlotAppl(slotname, TomBase.convertFromBQVarToVar(X1)), slot, PairSlotAppl(slotname, TomBase.convertFromBQVarToVar(X2))),
                    concConstraint()),subject,aType);

            //generate f(...) << f(X1*,X2*)
            TomSymbol tomSymbol = acp.getCompiler().getSymbolTable().getSymbolFromName(`tomName);
            BQTerm newSubject = null;
            if(TomBase.isListOperator(tomSymbol)) {
              newSubject = ASTFactory.buildList(`Name(tomName),`concBQTerm(X1,X2),acp.getCompiler().getSymbolTable());
            } else if(TomBase.isArrayOperator(tomSymbol)) {
              newSubject = ASTFactory.buildArray(`Name(tomName),`concBQTerm(X1,X2),acp.getCompiler().getSymbolTable());
            }
            Constraint c2 =
              `MatchConstraint(RecordAppl(optWithAC,namelist,concSlot(C1*,C2*),concConstraint()),
                  newSubject,aType);

            Constraint result = `AndConstraint(c1,c2);
            /*System.out.println("result: " + result);*/
            /*System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));*/
            return result;
          }
        }
      }
    }
  }
  
  /**
   * use abstraction to compile non-linear variables
   *
   * at this stage, we only have Variable en VariableStar
   * f(X1,X2^a2,...,Xn^an) <<ac s -> f(Z,Xn^an) <<ac s ^ f(X1,X2^a2,...,Xn-1^an-1) <<ac Z
   */
  %strategy RemoveNonLinearVariableStar(acp: ACPropagator) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=pattern@RecordAppl[
        Options=optWithAC@concOption(_*,MatchingTheory(concElementaryTheory(_*,AC(),_*)),_*),
        NameList=namelist@concTomName(Name[]),
        Slots=slots],Subject=subject,AstType=aType] -> {
          if(`slots.length() > 2) {
            %match(slots) {
              /*
               * f(X1,X2^a2,...,Xn^an) <<ac s -> f(Z,Xn^an) <<ac s ^ f(X1,X2^a2,...,Xn-1^an-1) <<ac Z
               */
              concSlot(C1*, Xn@PairSlotAppl[SlotName=slotname, Appl=VariableStar[AstName=Xname]], C2*,
                  PairSlotAppl[SlotName=slotname, Appl=VariableStar[AstName=Xname]], C3*)
                -> {
                  //System.out.println("remove non linear X*: " + `Xn);
                  //System.out.println(`slots);

                  SlotList slotXn = `concSlot();
                  SlotList slotContext = `concSlot();
                  for(Slot s:`slots.getCollectionconcSlot()) {
                    //System.out.println("slot: " + `s);
                    %match(s) {
                      PairSlotAppl[SlotName=slotname2, Appl=VariableStar[AstName=Xname2]] -> {
                        if(`slotname == `slotname2 && `Xname==`Xname2) {
                          // remove all occurences of Xn
                          slotXn = `concSlot(s,slotXn*);
                        } else {
                          // rebuild C1,C2,C3 without Xn
                          slotContext = `concSlot(slotContext*,s);
                        }
                      }
                    }
                  }

                  //generate: f(Z,Xn) <<ac s 
                  TomType listType = acp.getCompiler().getTermTypeFromTerm(`pattern);
                  BQTerm Z = acp.getCompiler().getFreshVariableStar(listType);				
                  Constraint c1 = 
                    `MatchConstraint(RecordAppl(optWithAC,
                          namelist,concSlot(PairSlotAppl(slotname,TomBase.convertFromBQVarToVar(Z)),slotXn*),
                          concConstraint()),subject,aType);
                  //generate: f(slotContext) <<ac Z
                  Constraint c2 = `MatchConstraint(RecordAppl(optWithAC,
                        namelist, slotContext*, concConstraint()),Z,aType);
                  Constraint result = `AndConstraint(c1,c2);

                  if(`slotContext.length() > 1) {
                    /*System.out.println("remove non linear X*: " + `Xn);*/
                    /*System.out.println("result: ");*/
                    /*System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));*/
                    return result;
                  }
                }
            }
          }
        }
    }
  }

  /**
   * use abstraction to compile  
   * 
   * f(Z*,...) <<ac s -> (Z*,X1*) <<ac s ^ f(...) <<ac X1* IF Z* linear 
   */
  %strategy PerformAbstraction(acp: ACPropagator) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=pattern@RecordAppl[
          Options=optWithAC@concOption(_*,MatchingTheory(concElementaryTheory(_*,AC(),_*)),_*),
          NameList=namelist@concTomName(Name[]),
          Slots=slots],Subject=subject,AstType=aType] -> {
        if(`slots.length() > 2) {
          %match(slots) {
            /*
             * f(Z*,...) <<ac s -> (Z*,X1*) <<ac s ^ f(...) <<ac X1* IF Z* linear 
             */
            concSlot(C1*, Z@PairSlotAppl[SlotName=slotname, Appl=VariableStar[]], C2*)
        && !concSlot(_*,    PairSlotAppl[SlotName=slotname, Appl=VariableStar[]], _*) << concSlot(C1*,C2*)
            -> {
              /*System.out.println("case F(Z*,...): " + `slots);*/
              //generate: f(Z,X1) <<ac s 
              TomType listType = acp.getCompiler().getTermTypeFromTerm(`pattern);
              BQTerm X1 = acp.getCompiler().getFreshVariableStar(listType);				
              Constraint c1 = 
                `MatchConstraint(RecordAppl(optWithAC,
                      namelist,concSlot(Z,PairSlotAppl(slotname,TomBase.convertFromBQVarToVar(X1))),
                      concConstraint()),subject,aType);
              //generate: f(...) <<ac X1
              Constraint c2 = `MatchConstraint(RecordAppl(optWithAC,
                    namelist, concSlot(C1*,C2*), concConstraint()),X1,aType);
              Constraint result = `AndConstraint(c1,c2);

              /*System.out.println("result: " + result);*/
              System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));
              return result;
            }
          }
        }
      }

    }
  }

  /**
   * transform AC matching into A matching when the pattern is reduced to 
   * an empty-list or a single variable
   * f()   <<ac s => f() <<a s
   * f(X*) <<ac s => f(X*) <<a s
   */
  %strategy CleanSingleVariable() extends Identity() {
    visit Constraint {
      /*
       * f()   <<ac s => f() <<a s
       * f(X*) <<ac s => f(X*) <<a s
       */
      MatchConstraint[Pattern=RecordAppl[
          Options=concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*),
          NameList=namelist@concTomName(Name[]), 
          Slots=slots],Subject=subject,AstType=aType] &&
        (concSlot()<<slots || concSlot(PairSlotAppl[Appl=VariableStar[]])<<slots) -> {
          /*System.out.println("case f(X*) <<ac s => f(X*) <<a s");*/
          /*System.out.println("case f()   <<ac s => f()   <<a s: " + `slots);*/
          OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);
          Constraint result = `MatchConstraint(
              RecordAppl(optWithoutAC, namelist, slots, concConstraint()),
              subject,aType);
          /*System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));*/
          return result;
        }
    }
  }

}
