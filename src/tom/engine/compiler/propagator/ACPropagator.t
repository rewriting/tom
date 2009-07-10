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

/**
 * AC propagator
 */
public class ACPropagator implements IBasePropagator {

//--------------------------------------------------------	
  %include { ../../adt/tomsignature/TomSignature.tom }	
  %include { ../../../library/mapping/java/sl.tom}
//--------------------------------------------------------

  public Constraint propagate(Constraint constraint) throws VisitFailure {
    return (Constraint)`BottomUp(ACMatching()).visitLight(constraint);		
  }	

  %strategy ACMatching() extends Identity() {
    visit Constraint {
      // here we handle all the cases of the AC match
      // that are supposed to be solved by programm transformation 
      // (basically to reduce all the cases to f(X*,Y*))
      c@MatchConstraint(pattern@RecordAppl[NameList=(Name(tomName)), Slots=slots],subject) -> {
        if (TomBase.isACOperator(Compiler.getSymbolTable().getSymbolFromName(`tomName))) { 
          //decompose the pattern to only f(X*,Y*) matching constraints
          System.out.println("decompose AC ");
          return decompose(`c); 
        }
      }
    }
  }

  private static Constraint decompose(Constraint c) {
    %match(c) {
      MatchConstraint(pattern@RecordAppl[Option=option, NameList=(Name(tomName)), Slots=slots],subject) -> {
        %match(slots) {
          concSlot(slot@!PairSlotAppl[Appl=VariableStar[]],tail*) -> {

            // get fresh variables
            TomType listType = Compiler.getTermTypeFromTerm(`pattern);
            TomTerm X1 = Compiler.getFreshVariableStar(listType);				
            TomName X1_Name = X1.getAstName();				
            TomTerm X2 = Compiler.getFreshVariableStar(listType);
            TomName X2_Name = X2.getAstName();				

            //generate: f(slot) << X1 && f(X1,X2) << subject && decompose(f(tail) << X2) 
            Constraint c1 = 
              `MatchConstraint(RecordAppl(option, concTomName(Name(tomName)),concSlot(slot),concConstraint()),X1);
            Constraint c2 = 
              `MatchConstraint(RecordAppl(option, concTomName(Name(tomName)),concSlot(PairSlotAppl(X1_Name,X1),PairSlotAppl(X2_Name,X2)), concConstraint()),subject);
            Constraint c3 = decompose(`MatchConstraint(RecordAppl(option, concTomName(Name(tomName)),tail,concConstraint()),X2));
            return `AndConstraint(c3, c2, c1);
          }

         concSlot(vstar@PairSlotAppl[Appl=VariableStar[]], tail*) -> {
           // get fresh variables
           TomType listType = Compiler.getTermTypeFromTerm(`pattern);
           TomTerm X1 = Compiler.getFreshVariableStar(listType);				
           TomName X1_Name = X1.getAstName();				

           //generate: f(vstar,X1) << subject && decompose(f(tail) << X1) 
           Constraint c1 = 
             `MatchConstraint(RecordAppl(option, concTomName(Name(tomName)),concSlot(vstar,PairSlotAppl(X1_Name,X1)), concConstraint()),subject);
           Constraint c2 = decompose(`MatchConstraint(RecordAppl(option, concTomName(Name(tomName)),tail,concConstraint()),X1));
           return `AndConstraint(c2, c1);
         }

         concSlot() -> {
           return `TrueConstraint();
         }
        }
      }
    }
    return c; 
  }

}
