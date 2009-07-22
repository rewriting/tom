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
package tom.engine.compiler.generator;

import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomexpression.types.expression.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.compiler.*;
import tom.engine.compiler.Compiler;


// TODO : move all this in the constraintgenerator.
// we should only generate the functions once per operator

/**
 * AC Generator
 */
public class ACGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  %typeterm ACGenerator {
    implement { ACGenerator }
    is_sort(t) { ($t instanceof ACGenerator) }
  }

  private Compiler compiler; 
  private ConstraintGenerator constraintGenerator; 
  
  public Compiler getCompiler() {
    return this.compiler;
  }

  public ACGenerator(Compiler compiler, ConstraintGenerator generator) {
    this.compiler = compiler;
    this.constraintGenerator = generator;
  }
 
  public Expression generate(Expression expression) throws VisitFailure {
    return `TopDown(Generator(this)).visitLight(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator(acg:ACGenerator) extends Identity() {
    visit Expression {
      ConstraintToExpression(MatchConstraint(pattern@RecordAppl[Slots=concSlot(PairSlotAppl[Appl=VariableStar[]],PairSlotAppl[Appl=VariableStar[]])],subject)) -> {

        if(TomBase.hasTheory(`pattern,`AC())) {
          // TODO - generate all the functions
          return `ACMatchLoop(pattern, subject);
        }
      }
    } // end visit
  } // end strategy  

}
