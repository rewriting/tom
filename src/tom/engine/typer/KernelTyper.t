/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.typer;

import java.util.*;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/util/types/HashMap.tom}

  %typeterm KernelTyper {
    implement { KernelTyper }
    is_sort(t) { ($t instanceof KernelTyper) }
  }

  /** few attributes */
  private TypeConstraintList constraintsToTypeVariable = `concTypeConstraint();
  private SymbolTable symbolTable;
  private int freshTypeVarCounter = 0;

  public KernelTyper() {
    super();
  }

  /** Accessor methods */
  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  }

  public void setSymbolTable(SymbolTable newSymbolTable) {
    this.symbolTable = newSymbolTable;
  }

  public TomType getFreshTypeVar() {
    return `TypeVar(freshTypeVarCounter++);
  }

  public TypeConstraintList getConstraints() {
    return this.constraintsToTypeVariable;
  }

  public void addConstraints(TypeConstraint newConstraint) {
    TypeConstraintList auxList = this.constraintsToTypeVariable;
    this.constraintsToTypeVariable=
      `concTypeConstraint(newConstraint,auxList*); 
  }

  public TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, getSymbolTable());
  }

  public TomSymbol getSymbolFromType(TomType type) {
    %match(type) {
      TypeWithSymbol[TomType=tomType, TlType=tlType] -> {
        return TomBase.getSymbolFromType(`Type(tomType,tlType), getSymbolTable()); 
      }
    }
    return TomBase.getSymbolFromType(type, getSymbolTable()); 
  }

  /*
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   * The types of subjects are inferred from the patterns
   * Variable is typed in the TomTerm case
   */

  public tom.library.sl.Visitable typeVariable(TomType contextType, tom.library.sl.Visitable subject) {
    if(contextType == null) {
      throw new TomRuntimeException("typeVariable: null contextType");
    }
    try {
      //System.out.println("typeVariable: " + contextType);
      //System.out.println("typeVariable subject: " + subject);
      tom.library.sl.Visitable res = `TopDownStopOnSuccess(replace_typeVariable(contextType,this)).visitLight(subject);
      //System.out.println("res: " + res);
      return res;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeVariable: failure on " + subject);
    }
  }

  %strategy replace_typeVariable(contextType:TomType,kernelTyper:KernelTyper) extends Fail() {

    visit Option {
      subject@OriginTracking[] -> { return `subject; }
    }

   // Treat java code?? 
    visit TargetLanguage {
      subject@TL[] -> { return `subject; }
      subject@ITL[] -> { return `subject; }
      subject@Comment[] -> { return `subject; }
    }

    // Return type found on symbolTable 
    visit TomType {
      Type(tomType,EmptyType()) -> {
        return kernelTyper.getType(`tomType);
      }
    }

    visit TomVisit {
      VisitTerm(type,constraintInstructionList,options) -> {
        TomType newType = (TomType)`kernelTyper.typeVariable(contextType,`type);
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        // Collect all match and numeric match (with explicit declaration of
        // type)
        `TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        // Type a list of matchs
        return `VisitTerm(newType, kernelTyper.typeConstraintInstructionList(newType,constraintInstructionList,matchAndNumericConstraints),options);
      }
    }

    visit Instruction {
      /*
       * Expansion of a Match construct
       * to add types in subjects
       * to add types in variables of patterns and rhs
       * constraintInstructionList is a list of pair (condition,action)
       */
      Match(constraintInstructionList, options) -> {
        TomType newType = contextType;
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        `TopDownCollect(CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(`constraintInstructionList);
        return `Match(kernelTyper.typeConstraintInstructionList(newType,constraintInstructionList,matchAndNumericConstraints),options);
      }
    }

    visit TomTerm {
      // Type a function or a list 
      RecordAppl[Option=option,NameList=nameList@(Name(tomName),_*),Slots=slotList,Constraints=constraints] -> {
        TomSymbol tomSymbol = null;
        if(`tomName.equals("")) {
          try {
            tomSymbol = kernelTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            `nameList = `concTomName(tomSymbol.getAstName());
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTyper.getSymbolFromName(`tomName);
        }

        // ADD: CT-Fun
        if(tomSymbol != null) {
          SlotList subterm =
            kernelTyper.typeVariableList(tomSymbol,`slotList);//send domain
          ConstraintList newConstraints = (ConstraintList)kernelTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),`constraints);
          return `RecordAppl(option,nameList,subterm,newConstraints);
        } else {
          //System.out.println("contextType = " + contextType);
          %match(contextType) {
            type@(Type|TypeWithSymbol)[] -> {
              SlotList subterm = kernelTyper.typeVariableList(`emptySymbol(), `slotList);
              ConstraintList newConstraints = (ConstraintList)kernelTyper.typeVariable(`type,`constraints);
              return `RecordAppl(option,nameList,subterm,newConstraints);
            }

            _ -> {
              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }
          }
        }
      }
      // FIXME: it seems to be useless to match against a "UnamedVariable"
      // since the desugarer has already replaced unknown variables by fresh
      // variables
      /*
       * Type a variable
       * CT-VAR rule: 
       * found "x:A" and "x:T" already exists in SymbolTable 
       * -> add a type constraint "A = T"
       */
      (Variable|UnamedVariable)[AstType=localType@Type(tomType,EmptyType()),Constraints=constraints] -> {
        //The variable will always have a type: a primitive type or an unknown
        //type (a fresh type variable)
        TomType globalType = kernelTyper.getType(`tomType);
        kernelTyper.addConstraints(`Equation(localType,globalType));
      }
    }
  }

  /*
   *
   * @param contextType
   * @param constraintInstructionList a list of ConstraintInstruction
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   */
  private ConstraintInstructionList typeConstraintInstructionList(TomType contextType, ConstraintInstructionList constraintInstructionList, Collection<Constraint> matchAndNumericConstraints) {
    // TODO
    return `concConstraintInstruction(); 
  }

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  protected Code propagateVariablesTypes(Code workingTerm){
    try{
      return `TopDown(ProcessRhsForVarTypePropagation()).visitLight(workingTerm);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + workingTerm);
    }
  }

  %strategy ProcessRhsForVarTypePropagation() extends Identity() {
    visit ConstraintInstruction {
      ConstraintInstruction(constr,action,option) -> {
        HashMap<String,TomType> varTypes = new HashMap<String,TomType>();
        `TopDown(CollectAllVariablesTypes(varTypes)).visitLight(`constr);        
        Constraint c = `TopDown(PropagateVariablesTypes(varTypes)).visitLight(`constr);        
        return `ConstraintInstruction(c,action,option);
      }
    }
  }  

  %strategy CollectAllVariablesTypes(HashMap map) extends Identity() {
    visit TomTerm {       
      //Variable[AstName=Name(name),AstType=type] && !EmptyType[] << type  -> {
        //if(`type!=SymbolTable.TYPE_UNKNOWN) {
      Variable[AstName=Name(name),AstType=type] && !EmptyType[] << type && !TypeVar[] << type -> {
          map.put(`name,`type);
      }
    }
  }

  %strategy PropagateVariablesTypes(HashMap map) extends Identity() {
    visit BQTerm {
      //v@BQVariable[AstName=Name(name),AstType=type] -> {
        //if(`type==SymbolTable.TYPE_UNKNOWN || `type.isEmptyType()) {
      v@BQVariable[AstName=Name(name),AstType=type] && EmptyType[] << type && TypeVar[] << type -> {
        if (map.containsKey(`name)) {
          return `v.setAstType((TomType)map.get(`name)); 
        }
      }
    }
  }
  
  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

  /**
   * Collect the constraints (match and numeric)
   * NumericConstraint are %match with explicit type declaration
   */
  %strategy CollectMatchAndNumericConstraints(constrList:Collection) extends Identity() {
    visit Constraint {
      c@(MatchConstraint|NumericConstraint)[] -> {        
        constrList.add(`c);
        throw new VisitFailure();// to stop the top-down
      }      
    }
  }

  private SlotList typeVariableList(TomSymbol symbol, SlotList subtermList) {
    // TODO 
    return `concSlot();
  }
}
 
