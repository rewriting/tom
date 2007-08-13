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
package tom.engine.compiler.propagator;

import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomname.types.*;
import tom.library.sl.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.tools.SymbolTable;
import tom.engine.compiler.*;
import tom.engine.TomBase;
import tom.engine.tools.SymbolTable;
import java.util.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;

/**
 * A propagator that contains rules that don't depend on the theory (or that are applicable for more than one)
 */
public class GeneralPurposePropagator implements IBasePropagator {

//--------------------------------------------------------
  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return  (Constraint)`TopDown(GeneralPropagations()).visitLight(constraint);
  }	

  %strategy GeneralPropagations() extends `Identity() {
    visit Constraint {      
      /**
       * Antipattern
       * 
       * an anti-pattern: just transform this into a AntiMatchConstraint and detach the constraints:
       * a@!g(...) << t -> AntiMatch(g(...) << t) /\ a << t 
       */
      MatchConstraint(AntiTerm(term@(Variable|RecordAppl)[]),s) -> {        
        return `AndConstraint(AntiMatchConstraint(MatchConstraint(term,s)),
            ConstraintPropagator.performDetach(MatchConstraint(term,s)));
      }
      
      /**
       * Merge for star variables (we only deal with the variables of the pattern, ignoring the introduced ones)
       * X* = p1 /\ Context( X* = p2 ) -> X* = p1 /\ Context( freshVar = p2 /\ freshVar == X* ) 
       */
      AndConstraint(X*,eq@MatchConstraint(VariableStar[AstName=x@!PositionName[],AstType=type],_),Y*) -> {
        Constraint toApplyOn = `AndConstraint(Y*);        
        TomTerm freshVar = ConstraintCompiler.getFreshVariableStar(`type);
        Constraint res = (Constraint)`OnceTopDownId(ReplaceMatchConstraint(x,freshVar)).visitLight(toApplyOn);
        if(res != toApplyOn) {
          return `AndConstraint(X*,eq,res);
        }
      }  
      /**
       * 
       * Detach annotations (for the symbols that were not detached in other propagators) 
       *  
       *  a@...b@f(...) << t -> f(...) << t /\ a << t /\ ... /\ b << t
       */
      m@MatchConstraint(term@(Variable|VariableStar|UnamedVariableStar|UnamedVariable)[Constraints = !concConstraint()],g) -> {
        Constraint result = ConstraintPropagator.performDetach(`m);
        if (`term.isVariable()) {
          result = `AndConstraint(MatchConstraint(term.setConstraints(concConstraint()),g),result);
        }
        return result;
      }

    }
  }// end %strategy
  
  /**
   * Detach sublists
   * 
   * Make sure that the sublists in a list are replaced by star variables - this is only happening 
   * when the lists and the sublists have the same name
   * 
   * conc(X*,conc(some_pattern),Y*) << t -> conc(X*,Z*,Y*) << t /\ conc(some_pattern) << Z*  
   * 
   */ 
  public static Constraint detachSublists(Constraint constraint) {
    // will hold the new slots of t
    SlotList newSlots = `concSlot();
    Constraint constraintList = `AndConstraint();
    %match(constraint) {      
      MatchConstraint(t@RecordAppl[NameList=(name@Name[]),Slots=slots@!concSlot()],g) -> {      
      %match(slots) { 
        concSlot(_*,slot,_*) -> {
matchSlot:  %match(slot,TomName name) {
            // if we find a child with the same name, we abstract
            ps@PairSlotAppl[Appl=appl@RecordAppl[NameList=(childName)]],childName -> {
              TomTerm freshVariable = ConstraintCompiler.getFreshVariableStar(ConstraintCompiler.getTermTypeFromTerm(`t));                
              constraintList = `AndConstraint(MatchConstraint(appl,freshVariable),constraintList*);
              newSlots = `concSlot(newSlots*,ps.setAppl(freshVariable));
              break matchSlot;
            }
            // the child can be an antiTerm - in this case, do as above
            ps@PairSlotAppl[Appl=appl@AntiTerm(RecordAppl[NameList=(childName)])],childName -> {
              TomTerm freshVariable = ConstraintCompiler.getFreshVariableStar(ConstraintCompiler.getTermTypeFromTerm(`t)); 
              constraintList = `AndConstraint(MatchConstraint(appl,freshVariable),constraintList*);
              newSlots = `concSlot(newSlots*,ps.setAppl(freshVariable));
              break matchSlot;
            }
            // else we just add the slot back to the list
            x,_ -> {
              newSlots = `concSlot(newSlots*,x);
            }
          }            
        }
      }  
      return `AndConstraint(MatchConstraint(t.setSlots(newSlots),g),constraintList*);   
    }
   }
    // never gets here
    return null;
  }
  
  
  %strategy ReplaceMatchConstraint(varName:TomName, freshVar:TomTerm) extends `Identity() {
    visit Constraint {
      MatchConstraint(v@VariableStar[AstName=name],p) -> {        
        if(`name == varName) {                                  
          return `AndConstraint(MatchConstraint(freshVar,p),MatchConstraint(TestVar(freshVar),v));
        }                                 
      }
    }
  }

}
