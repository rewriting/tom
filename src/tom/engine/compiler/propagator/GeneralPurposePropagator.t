/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.code.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.tools.SymbolTable;
import java.util.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.compiler.Compiler;

/**
 * A propagator that contains rules that don't depend on the theory (or that are applicable for more than one)
 */
public class GeneralPurposePropagator implements IBasePropagator {

//--------------------------------------------------------
  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom }	
  %include { constraintstrategies.tom }	
//--------------------------------------------------------

  %typeterm GeneralPurposePropagator {
    implement { GeneralPurposePropagator }
    is_sort(t) { ($t instanceof GeneralPurposePropagator) }
  }

  private Compiler compiler;  
  private ConstraintPropagator constraintPropagator; 

  public GeneralPurposePropagator(Compiler myCompiler, ConstraintPropagator myConstraintPropagator) {
    this.compiler = myCompiler;
    this.constraintPropagator = myConstraintPropagator;
  }

  public Compiler getCompiler() {
    return this.compiler;
  }

  public ConstraintPropagator getConstraintPropagator() {
    return this.constraintPropagator;
  }

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return `TopDownWhenConstraint(GeneralPropagations(this)).visitLight(constraint);
  }	

  %strategy GeneralPropagations(gpp:GeneralPurposePropagator) extends Identity() {
    visit Constraint {      
      /**
       * Antipattern
       * 
       * an anti-pattern: just transform this into a AntiMatchConstraint and detach the constraints:
       * a@!g(...) << t -> AntiMatch(g(...) << t) /\ a << t 
       */
      MatchConstraint[Pattern=AntiTerm(term@(Variable|RecordAppl)[]),Subject=s,AstType=aType] -> {        
        return `AndConstraint(AntiMatchConstraint(MatchConstraint(term,s,aType)),
            gpp.getConstraintPropagator().performDetach(MatchConstraint(term,s,aType)));
      }      
      /**
       * SwitchAnti : here is just for efficiency reasons, and not for ordering, 
       * because now the replace can be applied left-right; the ordering is done anyway in the pre-generator
       *       
       * AntiMatchConstraint[] /\ ... /\ MatchConstraint[] ->  MatchConstraint[] /\ ... /\ AntiMatchConstraint[] 
       */
      AndConstraint(X*,antiMatch@AntiMatchConstraint[],Y*,match@MatchConstraint[],Z*) -> {
        //TODO : rename variables
        return `AndConstraint(X*,Y*,match,antiMatch,Z*);        
      }      
      /**
       * Merge for variables (we only deal with the variables of the pattern, ignoring the introduced ones)
       * 
       * X* = p1 /\ Context( X* = p2 ) -> X* = p1 /\ Context( freshVar = p2)
       * x = p1 /\ Context( x = p2 ) -> x = p1 /\ Context( freshVar = p2)
       */
      /*
      AndConstraint(X*,antiMatch@AntiMatchConstraint[Constraint=MatchConstraint[Pattern=(Variable|VariableStar)[AstName=varName@!PositionName[]]]],Y*) -> {
        // we cannot cache already renamed variables, because disjunctions have to be taken into account
        // for example: g(x) || f(x,x) -> ...
        Constraint res = (Constraint)`TopDownWhenConstraint(ReplaceAntiMatchConstraint(varName,gpp)).visitLight(`Y*);
        if(res != `Y*) {
          return `AndConstraint(X*,antiMatch,res*);
        }
      }
      */
      /**
       * Merge for variables (we only deal with the variables of the pattern, ignoring the introduced ones)
       * 
       * X* = p1 /\ Context( X* = p2 ) -> X* = p1 /\ Context( freshVar = p2 /\ freshVar == X* )
       * x = p1 /\ Context( x = p2 ) -> x = p1 /\ Context( freshVar = p2 /\ freshVar == x )
       */
      AndConstraint(X*,eq@MatchConstraint[Pattern=(Variable|VariableStar)[AstName=varName@!PositionName[]]],Y*) -> {
        // we cannot cache already renamed variables, because disjunctions have to be taken into account
        // for example: g(x) || f(x,x) -> ...
        Constraint res = (Constraint)`TopDownWhenConstraint(ReplaceMatchConstraint(varName,gpp)).visitLight(`Y*);
        if(res != `Y*) {
          return `AndConstraint(X*,eq,res*);
        }
      }  
      /**
       * 
       * Detach annotations (for the symbols that were not detached in other propagators) 
       *  
       *  a@...b@f(...) << t -> f(...) << t /\ a << t /\ ... /\ b << t
       */
      m@MatchConstraint[Pattern=term@(Variable|VariableStar)[Constraints =
        !concConstraint()],Subject=g,AstType=aType] -> {
        Constraint result = gpp.getConstraintPropagator().performDetach(`m);
        if(`term.isVariable()) {
          result =
            `AndConstraint(MatchConstraint(term.setConstraints(concConstraint()),g,aType),result);
        }
        return result;
      }
      /**
       * a << a -> true 
       */
      /*MatchConstraint(x,x) -> {
        return `TrueConstraint();
      }*/

    }
  }// end %strategy

  /**
   * Detach sublists
   * 
   * Make sure that the sublists in a list are replaced by star variables 
   * this is only happening when the lists and the sublists have the same name
   * 
   * conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
   * 
   */ 
  public Constraint detachSublists(Constraint constraint) {
    // will hold the new slots of t
    SlotList newSlots = `concSlot();
    Constraint constraintList = `AndConstraint();
    %match(constraint) {      
      MatchConstraint[Pattern=t@RecordAppl[NameList=concTomName(name@Name[]),Slots=slots@!concSlot()],Subject=g,AstType=aType] -> {

        %match(slots) { 
          concSlot(_*,slot,_*) -> {
matchSlot:  %match(slot, TomName name) {
              ps@PairSlotAppl[Appl=appl],childName &&  
                (RecordAppl[NameList=concTomName(childName)] << appl || AntiTerm(RecordAppl[NameList=concTomName(childName)]) << appl) -> {
                  BQTerm freshVariable = getCompiler().getFreshVariableStar(getCompiler().getTermTypeFromTerm(`t));                
                  constraintList =
                    `AndConstraint(MatchConstraint(appl,freshVariable,aType),constraintList*);
                  newSlots = `concSlot(newSlots*,ps.setAppl(TomBase.convertFromBQVarToVar(freshVariable)));
                  break matchSlot;
                }
              // else we just add the slot back to the list
              x,_ -> {
                newSlots = `concSlot(newSlots*,x);
              }
            }
          }
        }
        return `AndConstraint(MatchConstraint(t.setSlots(newSlots),g,aType),constraintList*);   
      }
    }
    // never gets here
    throw new TomRuntimeException("GeneralPurposePropagator:detachSublists - unexpected result");
  }

  /*
   * x << s -> fresh << s ^ fresh==x
   */
  %strategy ReplaceMatchConstraint(varName:TomName,gpp:GeneralPurposePropagator) extends Identity() {
    visit Constraint {
      // we can have the same variable both as variableStar and as variable
      // we know that this is ok, because the type checker authorized it
      MatchConstraint[Pattern=var@(Variable|VariableStar)[AstName=name,AstType=type],Subject=subject,AstType=aType] && name == varName -> {        
        BQTerm freshVar = `var.isVariable() ? gpp.getCompiler().getFreshVariable(`type) : gpp.getCompiler().getFreshVariableStar(`type);
        return
          `AndConstraint(MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,aType),MatchConstraint(TestVar(TomBase.convertFromBQVarToVar(freshVar)),TomBase.convertFromVarToBQVar(var),aType));
      }
    }
  }

  /*
   * x << s -> fresh << s 
   */
  %strategy ReplaceAntiMatchConstraint(varName:TomName,gpp:GeneralPurposePropagator) extends Identity() {
    visit Constraint {
      // we can have the same variable both as variableStar and as variable
      // we know that this is ok, because the type checker authorized it
      AntiMatchConstraint[Constraint=MatchConstraint[Pattern=var@(Variable|VariableStar)[AstName=name,AstType=type],Subject=subject,AstType=aType]] && name == varName -> {        
        BQTerm freshVar = `var.isVariable() ? gpp.getCompiler().getFreshVariable(`type) : gpp.getCompiler().getFreshVariableStar(`type);
        return
          `AntiMatchConstraint(MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,aType));
      }
    }
  }

}
