/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2010, INPL, INRIA
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
 * Cláudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 *
 **/


//TODO

package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomslot.types.slotlist.*;
import tom.engine.adt.tomslot.types.slot.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqtermlist.concBQTerm;

import tom.engine.adt.typeconstraints.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class NewKernelTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../adt/code/_Code.tom}
//  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../adt/tomsignature/TomSignature.tom }
//  %include { ../../library/mapping/java/util/types/HashMap.tom}

  %typeterm NewKernelTyper {
    implement { NewKernelTyper }
    is_sort(t) { ($t instanceof NewKernelTyper) }
  }

//  %typeterm SubstitutionList { implement { HashMap<TomType,TomType> } }

  private SymbolTable symbolTable;
  private int freshTypeVarCounter = 0;
  // List for variables of pattern (match constraints)
  private TomList varPatternList;
  // List for variables of subject and of numeric constraints
  private BQTermList varList;
  // List for type constraints (for fresh type variables)
  private TypeConstraintList typeConstraints;
  // Set of pairs (freshVar,groundVar)
  private HashMap<TomType,TomType> substitutions;

  public NewKernelTyper() {
    super();
  }

  public SymbolTable getSymbolTable() {
    return this.symbolTable;
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  protected TomType getSymbolCodomain(TomSymbol tomSymbol) {
    return TomBase.getSymbolCodomain(tomSymbol);
  }

  protected TomSymbol getSymbolFromTerm(TomTerm tomTerm) {
    return TomBase.getSymbolFromTerm(tomTerm, getSymbolTable());
  }

  protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
    return TomBase.getSymbolFromTerm(bqTerm, getSymbolTable());
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, getSymbolTable());
  }

  protected TomSymbol getSymbolFromType(TomType type) {
    %match(type) {
      TypeWithSymbol[TomType=tomType, TlType=tlType] -> {
        return TomBase.getSymbolFromType(`Type(tomType,tlType), getSymbolTable()); 
      }
    }
    return TomBase.getSymbolFromType(type, getSymbolTable()); 
  }

  protected TomType getType(TomTerm term) {
    %match(term) {
      (Variable|VariableStar)[AstType=type] -> {
        return `type;
      }
      RecordAppl[NameList=(Name(name),_*)] -> {
        if(`name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        TomSymbol tomSymbol = getSymbolFromName(`name);
        return getSymbolCodomain(tomSymbol);
      }
    } 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  protected TomType getType(BQTerm term) {
    %match(term) {
      (BQVariable|BQVariableStar)[AstType=type] -> {
        return `type;
      }
      BQAppl[AstName=Name(name)] -> {
        if(`name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        TomSymbol tomSymbol = getSymbolFromName(`name);
        return getSymbolCodomain(tomSymbol);
      }
    } 
    throw new TomRuntimeException("getType(BQTerm): should not be here.");
  }

  protected TomType getFreshTypeVar() {
    return `TypeVar(freshTypeVarCounter++);
  }

  protected void addConstraint(TypeConstraint constraint) {
    this.typeConstraints = `concTypeConstraint(constraint,typeConstraints*);
  }

  protected void addTomTerm(TomTerm term) {
    ((`concTomTerm) varPatternList).append(term);
  }

  protected void addBQTerm(BQTerm term) {
    ((`concBQTerm) varList).append(term);
  }

  //Before resetting varPatternList, we need to check if varList contains
  //a corresponding BQTerm, and also remove it from varList
  protected void resetVarPatternList() {
    try {
      `TopDown(removeFromVarList(this)).visitLight(varList);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("resetVarPatternList: failure on " + varList);
    }
    varPatternList = `concTomTerm();
  }

  protected void initBQTerm() {
    varList = `concBQTerm();
  }

  %strategy removeFromVarList(nkt:NewKernelTyper) extends Identity() {
    visit BQTermList {
      list -> {
        %match(nkt.varPatternList,list) {
          concTomTerm(_*,(Variable|VariableStar)[AstName=name],_*),concBQTerm(x*,(BQVariable|BQVariableStar)[AstName=name],y*) -> {
            return `concBQTerm(x*,y*);
          }
        }
      }
    }
  }

  private void init() {
    varPatternList = `concTomTerm();
    varList = `concBQTerm();
    typeConstraints = `concTypeConstraint();
    substitutions = new HashMap<TomType,TomType>();
  }

  public Code inferTypeCode(Code code) {
    try {
      init();
      System.out.println("\n Test pour inferTypeCode -- ligne 1.");
      //return `TopDownStopOnSuccess(splitConstraintInstruction(this)).visitLight(code);
      //return `TopDown(_InstructionToCode(inferInstruction(this))).visitLight(code);
      return `TopDown(inferCode(this)).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferTypeCode: failure on " + code);
    }
  }

  %strategy inferCode(nkt:NewKernelTyper) extends Identity() {
    visit Code {
      InstructionToCode(instruction) -> {
        try {
          System.out.println("\n Test pour inferCode -- ligne 1.");
          Instruction result = `RepeatId(inferInstruction(nkt)).visitLight(`instruction);
          return `InstructionToCode(result);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferCode: failure on " + `instruction);
        }
      }
    }
  }  

  %strategy inferInstruction(nkt:NewKernelTyper) extends Identity() {
    visit Instruction {
      Match(constraintInstructionList,options) -> {
      System.out.println("\n Test pour inferInstruction -- ligne 1.");
        try {
          // Generate type constraints for a %match
          ConstraintInstructionList result = nkt.inferConstraintInstructionList(`constraintInstructionList);
          `RepeatId(solveConstraints(nkt)).visitLight(nkt.typeConstraints);
          result = nkt.replaceInConstraintInstructionList(`result);
          nkt.printGeneratedConstraints(nkt.typeConstraints);
          nkt.init(); // Reset all lists for the next independent match block 
          return `Match(result,options);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferInstruction: failure on " + nkt.typeConstraints);
        }
      }
    } 
  }

  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList cilist) {
    System.out.println("\n Test pour inferConstraintInstructionList -- ligne 1.");
    %match(cilist) {
      concConstraintInstruction() -> { return cilist; }
      concConstraintInstruction(headCIList@ConstraintInstruction(constraint,action,_),tailCIList*) -> {
        try {
          System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 2.");
          // Collect variables and type them with fresh type variables
          // Rename variables of pattern that already exist in varPatternList
          ConstraintInstruction typedHead =
            `TopDownCollect(CollectVars(this)).visitLight(`headCIList);
          System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 3.");
          `TopDown(inferConstraint(this)).visitLight(`constraint);
          System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 4.");
          // In inferInstruction we can call this method
          // inferConstraintInstructionList 
          //`TopDown(_concInstruction(inferInstruction(this))).visitLight(`action);
          `TopDown(_concInstruction(inferInstruction(this))).visitLight(`action);
          System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 5.");
          resetVarPatternList();
          ConstraintInstructionList typedTail =
            `inferConstraintInstructionList(tailCIList);
          return `concConstraintInstruction(typedHead, typedTail*);

        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintInstructionList: failure on " + `headCIList);

        }
      }
    }
    throw new TomRuntimeException("inferConstraintInstructionList: should not be here.");
  }

  %strategy CollectVars(nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      var -> {
        %match(var,nkt.varPatternList) {
          // Case of Variable
          // If the variable already exists in varPatternList
          Variable[Option=option,AstName=name,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2,_),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
              //nkt.printGeneratedConstraints();//DEBUG
            }
            return `Variable(option,name,type2,cilist);
          }
          Variable[Option=option,AstName=name1,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name2@!name1,_,_),_*) -> {
            TomTerm newVar = `var;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `Variable(option,name1,freshType,cilist);
            }
            nkt.addTomTerm(newVar);
            return newVar;
          }

          // Case of VariableStar
          VariableStar[Option=option,AstName=name,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2,_),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `VariableStar(option,name,type2,cilist);
          }
          VariableStar[Option=option,AstName=name1,AstType=type1,Constraints=cilist], 
          concTomTerm(_*,(Variable|VariableStar)(_,name2@!name1,_,_),_*) -> {
            TomTerm newVar = `var;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `VariableStar(option,name1,freshType,cilist);
            }
            nkt.addTomTerm(newVar);
            return newVar;
          }
        }
      }
    }
    visit BQTerm {
      bqvar -> {
        %match(bqvar,nkt.varList) {
          // Case of Variable
          // If the variable already exists in varPatternList
          BQVariable[Option=option,AstName=name,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `BQVariable(option,name,type2);
          }
          BQVariable[Option=option,AstName=name1,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name2@!name1,_),_*) -> {
            BQTerm newVar = `bqvar;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `BQVariable(option,name1,freshType);
            }
            nkt.addBQTerm(newVar);
            return newVar;
          }

          // Case of VariableStar
          BQVariableStar[Option=option,AstName=name,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2),_*) -> {
            if(`type1 != SymbolTable.TYPE_UNKNOWN) {
              nkt.addConstraint(`Equation(type1,type2));
            }
            return `BQVariableStar(option,name,type2);
          }
          BQVariableStar[Option=option,AstName=name1,AstType=type1], 
          concBQTerm(_*,(BQVariable|BQVariableStar)(_,name2@!name1,_),_*) -> {
            BQTerm newVar = `bqvar;
            if(`type1 == SymbolTable.TYPE_UNKNOWN) {
              TomType freshType = nkt.getFreshTypeVar();
              newVar = `BQVariableStar(option,name1,freshType);
            }
            nkt.addBQTerm(newVar);
            return newVar;
          }
        }
      }
    }
  } 

  %strategy inferConstraint(nkt:NewKernelTyper) extends Identity() {
    visit Constraint {
      constraint -> {
        %match {
          MatchConstraint(pattern,subject) << constraint &&
          concTomTerm(_*,pattern,_*) << nkt.varPatternList &&
          concBQTerm(_*,subject,_*) << nkt.varList -> {
            TomType freshType1 = nkt.getFreshTypeVar();
            TomType freshType2 = nkt.getFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(pattern)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(subject)));
            nkt.inferTomTerm(`pattern,freshType1);
            nkt.inferBQTerm(`subject,freshType2);
          }
          NumericConstraint(left,right,_) << constraint &&
          concBQTerm(_*,left,_*) << nkt.varList &&
          concBQTerm(_*,right,_*) << nkt.varList -> {
            TomType freshType1 = nkt.getFreshTypeVar();
            TomType freshType2 = nkt.getFreshTypeVar();
            // It will be useful for subtyping
            // TomType freshType3 = nkt.getFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(left)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(right)));
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferBQTerm(`left,freshType1);
            nkt.inferBQTerm(`right,freshType2);
          }
/*          AliasTo(tomterm) << constraint &&
          concTomTerm(_*,tomterm,_*) << nkt.varPatternList -> {
            TomType freshType = nkt.getFreshTypeVar();
            nkt.addConstraint(`Equation(freshType,nkt.getType(tomterm)));
            nkt.inferTomTerm(`tomterm,freshType);
          }*/
        }
      }
    }  
  } 

  private void inferTomTerm(TomTerm term, TomType freshType) {
    %match(term) {
      /**
       * Type a variable
       * CT-VAR rule: 
       * IF found "x:A" and "x:T" already exists in SymbolTable 
       * THEN add a type constraint "A = T"
       */
      (Variable|VariableStar)[AstType=type] -> {
        addConstraint(`Equation(type,freshType));  
      }

      /**
       * CT-FUN rule:
       * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
       * THEN infers type of arguments and add a type constraint "A = T" and
       *      calls the TypeVariableList method which adds a type constraint "Ai =
       *      Ti" for each argument, where Ai is a fresh type variable
       *
       * CT-EMPTY rule:
       * IF found "l():AA" and "l:T*->TT" exists in SymbolTable
       * THEN add a type constraint "AA = TT"       
       *
       * CT-ELEM rule:
       * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
       * THEN infers type of both sublist "l(e1,...,en)" and last argument
       *      "e" and adds a type constraint "AA = TT" and calls the
       *      TypeVariableList method which adds a type constraint "A =T"
       *      for the last argument, where A is a fresh type variable and
       *      "e" does not represent a list with head symbol "l"
       *
       * CT-MERGE rule:
       * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
       * THEN infers type of both sublist "l(e1,...,en)" and last argument
       *      "e" and adds a type constraint "AA = TT" and calls the
       *      TypeVariableList method, where "e" represents a list with
       *      head symbol "l"
       *
       * CT-STAR rule:
       * Equals to CT-MERGE but with a star variable "x*" instead of "e"
       * This rule is necessary because it differed from CT-MERGE in the
       * sense of the type of the last argument ("x*" here) is unknown 
       */
      RecordAppl[NameList=(Name(tomName),_*),Slots=slotList] -> {
        // Do we need to test if the nameList is equal to ""???
        TomSymbol tomSymbol = getSymbolFromName(`tomName);
        if (tomSymbol != `emptySymbol()) { // Do we REALLY need this test???
          TomType codomain = getSymbolCodomain(tomSymbol);
          addConstraint(`Equation(codomain,freshType));
          if (!`slotList.isEmptyconcSlot()) {
            inferSlotList(`slotList,`tomSymbol,freshType);
          }
        }
      }
    }
  }

  private void inferBQTerm(BQTerm term, TomType freshType) {
    %match(term) {
      (BQVariable|BQVariableStar)[AstType=type] -> {
        addConstraint(`Equation(type,freshType));  
      }

      BQAppl[AstName=Name(tomName),Args=bqTList] -> {
        // Do we need to test if the nameList is equal to ""???
        TomSymbol tomSymbol = getSymbolFromName(`tomName);
        if (tomSymbol != `emptySymbol()) { // Do we REALLY need this test???
          TomType codomain = getSymbolCodomain(tomSymbol);
          addConstraint(`Equation(codomain,freshType));
          if (!`bqTList.isEmptyconcBQTerm()) {
            inferBQTermList(`bqTList,`tomSymbol,freshType);
          }
        }
      }
    }
  }

  private void inferSlotList(SlotList slotList, TomSymbol tomSymbol, TomType freshType) {
    %match(slotList,tomSymbol) {
      concSlot(PairSlotAppl[Appl=term],tailSList*),Symbol[TypesToType=TypesToType(domain@concTomType(headTTList,_*),_)] -> {
        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(`tomSymbol) || TomBase.isArrayOperator(`tomSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(`term);
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            /**
             * Continuation of CT-ELEM rule (applying to premises which are
             * not lists):
             * IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
             * THEN infers type of both sublist "l(e1,...,en)" and last argument
             *      "e" and adds a type constraint "A = T" for the last
             *      argument, where "A" is a fresh type variable  and
             *      "e" does not represent a list with head symbol "l"
             */
            argFreshType = getFreshTypeVar();
            addConstraint(`Equation(argFreshType,headTTList));
          }
          /**
           * Continuation of CT-MERGE rule (applying to premises):
           * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "e", where "e" represents a list with
           *      head symbol "l"
           *
           * Continuation of CT-STAR rule (applying to premises):
           * IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "x", where "x" represents a list with
           *      head symbol "l"
           */
          `inferTomTerm(term,argFreshType);
          `inferSlotList(tailSList,tomSymbol,freshType);
        } else {
          // In case of a function
          TomType argType;
          TomTerm argTerm;
          TomTypeList symDomain = `domain;
          /**
           * Continuation of CT-FUN rule (applying to premises):
           * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
           * THEN infers type of arguments and adds a type constraint "Ai =
           *      Ti" for each argument, where "Ai" is a fresh type variable
           */
          for(Slot arg : `slotList.getCollectionconcSlot()) {
            argTerm = arg.getAppl();
            argType = symDomain.getHeadconcTomType();
            argFreshType = getFreshTypeVar();
            addConstraint(`Equation(argFreshType,argType));
            `inferTomTerm(argTerm,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }
    }
  }

  private void inferBQTermList(BQTermList bqTList, TomSymbol tomSymbol, TomType
      freshType) {
    %match(bqTList,tomSymbol) {
      concBQTerm(term,tailBQTList*),Symbol[TypesToType=TypesToType(domain@concTomType(headTTList,_*),_)] -> {
        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(`tomSymbol) || TomBase.isArrayOperator(`tomSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(`term);
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            argFreshType = getFreshTypeVar();
            addConstraint(`Equation(argFreshType,headTTList));
          }
          `inferBQTerm(term,argFreshType);
          `inferBQTermList(tailBQTList,tomSymbol,freshType);
        } else {
          // In case of a function
          TomType argType;
          TomTypeList symDomain = `domain;
          for(BQTerm arg : `bqTList.getCollectionconcBQTerm()) {
            argType = symDomain.getHeadconcTomType();
            argFreshType = getFreshTypeVar();
            addConstraint(`Equation(argFreshType,argType));
            `inferBQTerm(arg,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }
    }
  }

  // TODO
  //private void solveConstraints() {
    // Add a new substitution in "substitutions"
  //}

  %strategy solveConstraints(nkt:NewKernelTyper) extends Identity() {
    visit TypeConstraintList {
      concTypeConstraint(_*,Equation(Type(tName1,_),Type(tName2@!tName1,_)),_*) -> {
        throw new RuntimeException("solveConstraints: failure on " + `tName1
            + " = " + `tName2);
      }

      concTypeConstraint(leftTCList*,Equation(typeVar@TypeVar(_),type),rightTCList*) -> {
        nkt.substitutions.put(`typeVar,`type);
        TypeConstraintList lSubTCList = `leftTCList;
        TypeConstraintList rSubTCList = `rightTCList;
        if(nkt.findTypeVars(`typeVar,lSubTCList)) {
          lSubTCList = nkt.applySubstitution(`typeVar,`type,lSubTCList);
        }
        if(nkt.findTypeVars(`typeVar,rSubTCList)) {
          rSubTCList = nkt.applySubstitution(`typeVar,`type,rSubTCList);
        }
        return `concTypeConstraint(lSubTCList*,Equation(type,type),rSubTCList*);
      }

      concTypeConstraint(leftTCList*,Equation(type,typeVar@TypeVar(_)),rightTCList*) -> {
        nkt.substitutions.put(`typeVar,`type);
        TypeConstraintList lSubTCList = `leftTCList;
        TypeConstraintList rSubTCList = `rightTCList;
        if(nkt.findTypeVars(`typeVar,lSubTCList)) {
          lSubTCList = nkt.applySubstitution(`typeVar,`type,lSubTCList);
        }
        if(nkt.findTypeVars(`typeVar,rSubTCList)) {
          rSubTCList = nkt.applySubstitution(`typeVar,`type,rSubTCList);
        }
        return `concTypeConstraint(lSubTCList*,Equation(type,type),rSubTCList*);
      }
    }
  }

  private boolean findTypeVars(TomType typeVar, TypeConstraintList
      tcList) {
    %match {
      concTypeConstraint(_*,Equation(type1,type2),_*) << tcList &&
      (type1 == typeVar || type2 == typeVar) -> {
        return true;
      }
    }
    return false;
  }

  private TypeConstraintList applySubstitution(TomType oldtt, TomType newtt,
      TypeConstraintList tcList) {
    try {
      return (TypeConstraintList)
        `TopDown(replaceFreshTypeVar(oldtt,newtt,this)).visitLight(tcList);
    } catch(tom.library.sl.VisitFailure e) {
      throw new RuntimeException("applySubstitution: should not be here.");
    }
  }

  private ConstraintInstructionList
    replaceInConstraintInstructionList(ConstraintInstructionList ciList) {
    TomType newtt;
    ConstraintInstructionList replacedCIList = ciList;
    try {
      for(TomType ttToSubstitute : substitutions.keySet()) {
        newtt = substitutions.get(ttToSubstitute);
        replacedCIList =
          `TopDown(replaceFreshTypeVar(ttToSubstitute,newtt,this)).visitLight(ciList);
      }
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInConstraintInstructionList: failure on " +
          replacedCIList);
    }
    return replacedCIList;
  }

  %strategy replaceFreshTypeVar(oldtt:TomType,newtt:TomType,nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar && (typeVar == oldtt) -> { return newtt; }
    }
  }

/*
  %strategy replaceFreshTypeVar(oldtt:TomType,newtt:TomType,nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      term -> {
        %match {
          Variable[Option=options,AstName=name,AstType=type,Constraints=constraints] << term && (type == oldtt) -> {
            return `Variable(options,name,newtt,constraints);
          }
          VariableStar[Option=options,AstName=name,AstType=type,Constraints=constraints] << term && (type == oldtt) -> {
            return `VariableStar(options,name,newtt,constraints);
          }
        }
      }
    }
    visit BQTerm {
      term -> {
        %match {
          BQVariable[Option=options,AstName=name,AstType=type] << term && (type == oldtt) -> {
            return `BQVariable(options,name,newtt);
          }
          BQVariableStar[Option=options,AstName=name,AstType=type] << term && (type == oldtt) -> {
            return `BQVariableStar(options,name,newtt);
          }
        }
      }
    }
  }
*/

  public void printGeneratedConstraints(TypeConstraintList TCList) {
    %match(TCList) {
      !concTypeConstraint() -> { 
        System.out.println("\n------ Type Constraints : \n {");
        printEachConstraint(TCList);
        System.out.println("}");
      }
    }
  }
  public void printEachConstraint(TypeConstraintList TCList) {
    %match(TCList) {
      concTypeConstraint(Equation(type1,type2),tailTCList*) -> {
        printType(`type1);
        System.out.println(" = ");
        printType(`type2);
        if (`tailTCList != `concTypeConstraint()) {
            System.out.println(", "); 
            printEachConstraint(`tailTCList);
        }
      }
    }
  }
    
  public void printType(TomType type) {
    %match(type) {
      Type[TomType=nameType] -> { System.out.println(`nameType); }
      TypeVar[Index=i] -> { System.out.println("x" + `i); }
    }
  }
} // NewKernelTyper
