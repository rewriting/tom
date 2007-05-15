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
import tom.engine.compiler.generator.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.library.sl.*;


/**
 * This class is in charge with all the post treatments needed after the generation process
 * 1. make sure that no variable is declared twice
 * 2. make sure Ref is used for deferencing variables when needed ( for CAML ) 
 * ... 
 */
public class PostGenerator {
	
//------------------------------------------------------------  
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom}
//------------------------------------------------------------  
 
  public static TomTerm performPostGenerationTreatment(TomTerm term) 
       throws VisitFailure{
    term = (TomTerm)`TopDown(ChangeVarDeclarations()).visit(term);
    return (TomTerm)`InnermostId(AddRef()).visit(term);
    //return term;
  }

  
  %strategy AddRef() extends Identity(){    
    visit Expression { 
      EqualTerm(type,var@(Variable|VariableStar)[],t) -> {        
        return `EqualTerm(type,Ref(var),t);
      }
      
      EqualTerm(type,e,var@(Variable|VariableStar)[]) -> {        
        return `EqualTerm(type,e,Ref(var));
      }
      
      IsEmptyList(opName, var@!Ref[]) -> {
        return `IsEmptyList(opName, Ref(var));
      }
      
      IsEmptyArray(opName,variable@!Ref[],index@!Ref[]) -> {
        return `IsEmptyArray(opName,Ref(variable),Ref(index));
      }
      
      Conditional(condition,ifAction,TomTermToExpression(var@!Ref[])) -> {        
        return `Conditional(condition,ifAction,TomTermToExpression(Ref(var)));
      }
      
      GetHead(opName, type, var@!Ref[]) -> {        
        return `GetHead(opName, type, Ref(var));
      } 
      
      GetTail(opName, var@!Ref[]) -> {        
        return `GetTail(opName, Ref(var));
      }      
      
      IsFsym(opName,var@!Ref[]) -> {
        return `IsFsym(opName,Ref(var));
      }
      
      GetSlot(type,name,slotName,var@!Ref[]) -> {
        return `GetSlot(type,name,slotName,Ref(var));
      }
      
      GetSliceList(name,begin@!Ref[],end,tail) -> {
        return `GetSliceList(name,Ref(begin),Ref(end),tail);
      }
      
      GetSliceArray(name,subjectListName,begin@!Ref[],end) -> {
        return `GetSliceArray(name,subjectListName,Ref(begin),Ref(end));
      }
    }
    
    visit Instruction {      
      // flag test
      If(EqualTerm(type,flag@!Ref[],ExpressionToTomTerm(value@(TrueTL|FalseTL)[])),action,Nop()) -> {        
        return `If(EqualTerm(type,Ref(flag),ExpressionToTomTerm(value)),action,Nop());
      }                
      // if we have a variable introduced in the match process, we generate LetRef
      // otherwise, let - for the variables of the initial pattern
      LetRef(var@(Variable|VariableStar)[],src@TomTermToExpression(source@!Ref[]),instruction) -> {
        TomName name = `var.getAstName(); 
        %match(name){
          PositionName[] -> {
            return `LetRef(var,TomTermToExpression(Ref(source)),instruction);    
          }
        }
        %match(source){
          (Variable|VariableStar)[] -> {
            return `Let(var,TomTermToExpression(Ref(source)),instruction);    
          }
          _ ->{
            // we generate let for the variables of the initial pattern comming from lists
            return `Let(var,src,instruction);    
          }
        }        
      }      

      LetAssign(var,TomTermToExpression(source@(Variable|VariableStar)[]),instruction) -> {        
        return `LetAssign(var,TomTermToExpression(Ref(source)),instruction);
      }
 
    }// end visit
  }
  
  /**
   * Makes sure that no variable is declared if the same variable was declared above  
   */
  %strategy ChangeVarDeclarations() extends Identity(){
    visit Instruction{
      LetRef(var@(Variable|VariableStar)[AstName=name],source,instruction) ->{
        Visitable root = getEnvironment().getRoot();
        if (root != getEnvironment().getSubject()) {
          try {
            getEnvironment().getPosition().getOmegaPath(`CheckVarExistence(name)).visit(root); 
          } catch (VisitFailure e) {
            return `LetAssign(var,source,instruction);
          }
        }
      }
    }// end visit
  }// end strategy

  %strategy CheckVarExistence(varName:TomName) extends Identity(){
    visit Instruction {
      LetRef[Variable=v@(Variable|VariableStar)[AstName=name]] -> {
        if (varName == (`name) ){
          throw new VisitFailure();
        }
      }
    } // end visit
  }// end strategy
  
}
