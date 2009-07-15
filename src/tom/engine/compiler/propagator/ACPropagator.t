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
    return `BottomUp(ACMatching(this)).visitLight(constraint);		
  }	

  %strategy ACMatching(acp: ACPropagator) extends Identity() {
    visit Constraint {
      // here we handle all the cases of the AC match
      // that are supposed to be solved by programm transformation 
      // (basically to reduce all the cases to f(X*,Y*))
      c@MatchConstraint(pattern@RecordAppl[NameList=(Name(tomName)), Slots=slots],subject) -> {
        //decompose the pattern to only f(X*,Y*) matching constraints
        if(TomBase.hasTheory(`pattern,`AC())) {
          return acp.decompose(`c);
        }
      }
    }
  }

  public Constraint decompose(Constraint input) {
    Constraint result = `AndConstraint();
    %match(input) {
      MatchConstraint(pattern@RecordAppl[Option=optWithAC@concOption(T1*,MatchingTheory(concElementaryTheory(T2*,AC(),T3*)),T4*), NameList=symbname@(Name(tomName)), Slots=slots],subject) -> {
        //use this option for using list maching
        OptionList optWithoutAC = `concOption(T1*,MatchingTheory(concElementaryTheory(T2*,T3*)),T4*);
        // get fresh variables
        TomType listType = getCompiler().getTermTypeFromTerm(`pattern);
        TomTerm X1, X2;
        TomTerm s = `subject;

        %match(slots) {
          concSlot(_*, slot@PairSlotAppl[SlotName=slotname, Appl=!VariableStar[]], _*) -> {
            //generate f(X1*, slot, X2*) << s
            X1 = getCompiler().getFreshVariableStar(listType);				
            X2 = getCompiler().getFreshVariableStar(listType);
            Constraint c = 
              `MatchConstraint(RecordAppl(optWithoutAC,
                    symbname,concSlot(PairSlotAppl(slotname,
                        UnamedVariableStar(concOption(), listType, concConstraint())),slot,PairSlotAppl(slotname,
                        UnamedVariableStar(concOption(), listType, concConstraint()))),concConstraint()),s);
            result =  `AndConstraint(result*, c);
            //update s to f(X1*,X2*) 
            s =  `RecordAppl(optWithAC,
                symbname,concSlot(PairSlotAppl(slotname,X1),PairSlotAppl(slotname,X2)),
                concConstraint());
          }

          concSlot(_*, vstar@PairSlotAppl[SlotName=slotname, Appl=VariableStar[]], _*) -> {
            //generate: f(vstar,X1) << s 
            X1 = getCompiler().getFreshVariableStar(listType);				
            Constraint c = 
              `MatchConstraint(RecordAppl(optWithAC,
                    symbname,concSlot(vstar,PairSlotAppl(slotname,X1)),
                    concConstraint()),s);
            result = `AndConstraint(result*, c);
            //update s to X1
            s =  X1;
          }
        }
        result = `AndConstraint(result*, EmptyListConstraint(Name(tomName),s));
      }
    }
    System.out.println(TomConstraintPrettyPrinter.prettyPrint(result));
    return result; 
  }

}
