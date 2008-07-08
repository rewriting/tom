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

/**
 * AC Generator
 */
public class ACGenerator implements IBaseGenerator {

  %include { ../../adt/tomsignature/TomSignature.tom }
  %include { ../../../library/mapping/java/sl.tom}	

  public Expression generate(Expression expression) throws VisitFailure {
    return (Expression)`TopDown(Generator()).visitLight(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator() extends Identity() {
    visit Expression {
      // generate loop for AC - "kilometric counter"
      // for a pattern f(x,y)
      /*
       * int[] alpha = computeMultiplicities(subject);
       * int length = alpha.length;
       * int[] tempSol = new int[length];
       * int pos = lenght-1;
       *   
       *  while(true) {
       *   // reinitialize
       *   while(pos >= 0 && tempSol[pos] == alpha[pos] ) {        
       *     tempSol[pos] = 0;
       *     pos--;
       *   }
       *   if (pos >= 0) { // we didn't finish      
       *     tempSol[pos] += 1;      
       *     x = getTerm(tempSol,alpha,subject);
       *     y = getComplementTerm(tempSol,alpha,subject);
       *     ... // action
       *     pos = n-1;
       *   }  
       *  }    
       */
      ce@ConstraintToExpression(MatchConstraint(subject@RecordAppl[NameList=(Name(tomName)), SlotList=concSlot(x,y)],v)) -> {
        // if this is not an ac symbol, nothing to do
        if(!TomBase.isACOperator(Compiler.getSymbolTable().getSymbolFromName(`tomName))) { return `ce; }
        
        Instruction instruction = null;       
        
        // a variable 0
        TomTerm zero = Variable(concOption(),Name("0"),intType,concConstraint());
        // the name of the int[] operator
        TomName intArrayName = `Name(Compiler.getSymbolTable().getIntArrayOp());        
        TomType intArrayType = symbolTable.getIntArrayType();
        
        TomTerm alpha = Compiler.getFreshVariable(Compiler.getIntArrayType());
        TomTerm tempSol = Compiler.getFreshVariable(Compiler.getIntArrayType());
        TomTerm position = Compiler.getFreshVariable(Compiler.getIntType());
        TomTerm length = Compiler.getFreshVariable(Compiler.getIntType());
                
        instruction = `LetRef(position,SubstractOne(lenght),instruction);
        instruction = `LetRef(tempSol,BuildEmptyArray(intArrayName,lenght),instruction);
        instruction = `LetRef(lenght,GetSize(intArrayName,alpha),instruction);

        instruction = `LetRef(alpha,TomTermToExpression(FunctionCall(
            Name(ConstraintGenerator.multiplicityFuncName + "_" + tomName),
            intArrayType,concTomTerm(subject))),instruction);
        
        Expression positionGreaterOrEqThanZero = `GreaterOrEqualThan(TomTermToExpression(position),TomTermToExpression(zero));             
        
        Expression whileCond = `And(positionGreaterOrEqThanZero,
            EqualTerm(GetElement(intArrayName,intType,tempSol,position),GetElement(intArrayName,intType,alpha,position)));        
        Instruction reinitializationLoop = `WhileDo(whileCond,
            LetArray(tempSol,position,TomTermToExpression(zero),
             ExpressionToInstruction(SubstractOne(position))));
        
        // last if
        TomList getTermArgs = `concTomTerm(tempSol,alpha,subject);        
        TomType subtermType = Compiler.getTermTypeFromTerm(`x);        
        Instruction lastTest = `If(positionGreaterThanZero,
            LetArray(tempSol,position,AddOne(GetElement(intArrayName,intType,tempSol,position)),
                LetRef(x,FunctionCall(
                    Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + tomName),
                    subtermType,getTermArgs),
                LetRef(y,FunctionCall(
                    Name(ConstraintGenerator.getComplTermForMultiplicityFuncName + "_" + tomName),
                    subtermType,getTermArgs),
                    LetRef(position,SubstractOne(lenght),Nop())))),
            Nop());
        
        
        
        WhileDo(TrueTL(),DoInst:Instruction)
        
        
        
        
        		        		      
      }
    } // end visit
  } // end strategy  
}
