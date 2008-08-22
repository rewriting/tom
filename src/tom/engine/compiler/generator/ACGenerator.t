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

  public Expression generate(Expression expression) throws VisitFailure {
    return (Expression)`TopDown(Generator()).visitLight(expression);
  }

  // If we find ConstraintToExpression it means that this constraint was not processed	
  %strategy Generator() extends Identity() {
    visit Expression {
      
      ce@ConstraintToExpression(MatchConstraint(pattern@RecordAppl[NameList=(Name(tomName))],subject)) -> {
        // if this is not an AC symbol, nothing to do
        if(!TomBase.isACOperator(Compiler.getSymbolTable().getSymbolFromName(`tomName))) { return `ce; }
        
        // TODO - generate all the functions
       
        
        System.out.println("Pattern:" + `pattern);
        
        return `ACMatchLoop(pattern, subject);
      }
    } // end visit
  } // end strategy  
  
  /**
   *    // Generates the PIL for the following function
   * 
        private int[] getMultiplicities(Term subj, int length) {
          int[] mult = new int[length];
          Term oldElem = null;
          // if we realy have a list
          // TODO: is this really necessary ?
          if (subj.isConsf()) {      
            oldElem = subj.getHeadf();      
          } else {      
            mult[0] = 1;
            return mult;      
          }
          int counter = 0;  
          // = subj.length;
          while(true) {
            Term elem = subj.getHeadf();        
            // another element of this type
            if (elem.equals(oldElem)){
              mult[counter] += 1; 
            } else {
              counter++;
              oldElem = elem;
              mult[counter] = 1;
            }
            subj = subj.getTailf();
            // if we got to the end of the list
            if(!subj.isConsf()) {
              if (subj.equals(oldElem)){
                mult[counter] += 1; 
              } else {
                counter++;          
                mult[counter] = 1;
              }
              break; // break the while
            } 
          }
          return mult;
        }
   */
  private generateGetMultiplicities(){
    TomType intType = Compiler.getIntType();
    SymbolTable symbolTable = Compiler.getSymbolTable();
    TomType intArrayType = symbolTable.getIntArrayType();
    // a 0
    Expression zero = `Integer(0);
    // the name of the int[] operator
    TomName intArrayName = `Name(symbolTable.getIntArrayOp());
    
    TomTerm mult = Compiler.getFreshVariable(intArrayType);
    TomTerm tempSol = Compiler.getFreshVariable(intArrayType);
    TomTerm position = Compiler.getFreshVariable(intType);
    TomTerm length = Compiler.getFreshVariable(intType);
  }
  
  /**
   * // Generates the PIL for the following function
   * 
   * private int computeLength(Term subj) {
   *  // a single element
   *  if(!subj.isConsf()) {
   *    return 1;
   *  }
   *  Term old = null;
   *  int counter = 0;
   *  while(true) {
   *    Term elem = subj.getHeadf();
   *    // a new element
   *    if (!elem.equals(old)){
   *      counter++;
   *      old = elem;
   *    } 
   *    subj = subj.getTailf();
   *    // if we got to the end of the list
   *    if(!subj.isConsf()) {
   *      if (!subj.equals(old)) { counter++; }
   *      break; // break the while
   *    } 
   *  }     
   *  return counter;    
   * }
   */
  private Declaration getPILforComputeLength(String opNameString, TomName opName, TomType opType) {    
    // all the variables
    TomTerm subject = `Variable(concOption(),"subject",opType,concConstraint());    
    TomTerm old = `Variable(concOption(),"old",opType,concConstraint());       
    TomTerm counter = `Variable(concOption(),"counter",Compiler.getIntType(),concConstraint());
    TomTerm elem = `Variable(concOption(),"elem",opType,concConstraint());    
    // test if a new element
    Instruction isNewElem = `If(Not(EqualTerm(elem,old)), UnamedBlock(concInstruction(
        AddOne(counter),LetRef(old,elem,Nop()))),Nop());    

    // test if end of list
    Instruction isEndList = `If(Not(IsFsym(opName,subject)), UnamedBlock(concInstruction(
        If(Not(EqualTerm(subject,old)),AddOne(counter),Nop()),Break())),Nop());
    
    Instruction whileBlock = `UnamedBlock(concInstruction(
        LetRef(elem,GetHead(opName,opType,subject),isNewElem),
        LetRef(subject,GetTail(opName,subject),isEndList)));    
    Instruction whileLoop = `WhileDo(TrueTL(),whileBlock);
    
    // test if subj is consOpName
    Instruction isConsOpName = `If(Not(IsFsym(opName,subject)),Return(Integer(1)),Nop());
    
    Instruction functionBody = `UnamedBlock(concInstruction(
        isConsOpName,
        LetRef(old,Bottom(),`LetRef(counter,Integer(0),whileLoop))
        Return(counter)));
        
    return `FunctionDef(Name(ConstraintGenerator.computeLengthFuncName+opNameString),
        concTomTerm(subject),Compiler.getIntType(),EmptyType(),functionBody);
  }  
   
}
