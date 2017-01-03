/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

  /**
   * Makes sure that no variable is declared if the same variable was declared above  
   */
  static <T> T changeVarDeclarations(T instr) throws VisitFailure {
    return (T)`TopDown(ChangeVarDeclarations()).visit((Visitable)instr);
  } 

   public static Instruction performPostGenerationTreatment(Instruction instruction) throws VisitFailure {
    // Warning: BottomUp cannot be replaced by TopDown
    // otherwise, the code with getPostion is not correct
    //System.out.println("ins1: " + instruction);
    instruction = `BuiltinBottomUp(ChangeVarDeclarations()).visit(instruction);
    //System.out.println("ins2: " + instruction);
    //return `TopDown(AddRef()).visitLight(instruction);
    return instruction;
   }
 
  /**
   * Makes sure that no variable is declared if the same variable was declared above  
   */
  %strategy ChangeVarDeclarations() extends Identity() {
    visit Instruction {
      LetRef(var@(BQVariable|BQVariableStar)[AstName=name],exp,body) -> {
        /*
         * when there is an identical LetRef between the root and the current LetRef
         * the current LetRef is replaced by an Assign
         */
        //DEBUG System.out.println("ChangeVarDeclarations: name = " + `name);
        Visitable root = (Visitable) getEnvironment().getRoot();
        if(getEnvironment().depth()>0) { // we are not at the root
          //DEBUG System.out.println("ChangeVarDeclarations: root = " + `root);
          try {
            getPosition().getOmegaPath(`CheckLetRefExistence(name)).visit(root); 
          } catch (VisitFailure e) {
            return `AbstractBlock(concInstruction(Assign(var,exp),body));
          }
        } 

        /*
         * when there is no LetRef before the current LetRef 
         * if there is no Assign, not LetRef in the body
         * the current LetRef is replaced by a Let
         */
          //DEBUG System.out.println("ChangeVarDeclarations: body = " + `body);
        try {
          `Not(TopDown(ChoiceId(CheckAssignExistence(name),CheckLetRefExistence(name)))).visitLight(`body);
        } catch (VisitFailure e) {
          return `Let(var,exp,body);
        }
      }
    }
  }

  %strategy CheckLetRefExistence(varName:TomName) extends Identity() {
    visit Instruction {
      LetRef[Variable=(BQVariable|BQVariableStar)[AstName=name]] -> {
        //DEBUG System.out.println("CheckLetRefExistence: name = " + `name);
        //DEBUG System.out.println("CheckLetRefExistence: varName = " + `varName);
        if(varName == `name ) {
          //DEBUG System.out.println("CheckLetRefExistence: " + `name + " already exists.");
          throw new VisitFailure();
        }
      }
    }
  }

  %strategy CheckAssignExistence(varName:TomName) extends Identity() {
    visit Instruction {
      (Assign|AssignArray)[Variable=(BQVariable|BQVariableStar)[AstName=name]] -> {
        if(varName == `name ) {
          throw new VisitFailure();
        }
      }
    }
  }

}
