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



package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomconstraint.types.constraintlist.concConstraint;
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
      AntiTerm(atomicTerm) -> {
        return getType(`atomicTerm);
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

  protected TomType getUnknownFreshTypeVar() {
    return `Type("unknown type",TypeVar(freshTypeVarCounter++));
  }

  protected void addConstraint(TypeConstraint constraint) {
    typeConstraints = `concTypeConstraint(constraint,typeConstraints*);
  }

  protected void addTomTerm(TomTerm term) {
    varPatternList = `concTomTerm(term,varPatternList*);
  }

  protected void addBQTerm(BQTerm term) {
    varList = `concBQTerm(term,varList*);
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
      //DEBUG System.out.println("\n Test pour inferTypeCode -- ligne 1.");
      System.out.println("\n Original Code = \n" + code + '\n');
      %match(code) {
        Tom(codes@concCode(_,_*)) -> {
          CodeList result = `_concCode(inferCode(this)).visitLight(`codes);
          //DEBUG System.out.println("\n Typed Code = \n" + `Tom(result) + '\n');
          return `Tom(result);
        }
      }
      return code;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferTypeCode: failure on " + code);
    }
  }

  %strategy inferCode(nkt:NewKernelTyper) extends Identity() {
    visit Code {
      code@InstructionToCode(Match(constraintInstructionList,options)) -> {
        try {
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 1.");
          // Generate type constraints for a %match
          ConstraintInstructionList result = nkt.inferConstraintInstructionList(`constraintInstructionList);
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 2.");
          System.out.println("\n typeConstraints before solve = " + nkt.typeConstraints);
          nkt.typeConstraints = `RepeatId(solveConstraints(nkt)).visitLight(nkt.typeConstraints);
          System.out.println("\n typeConstraints aftersolve = " + nkt.typeConstraints);
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 3.");
          result = nkt.replaceInConstraintInstructionList(result);
          nkt.replaceInSymbolTable();
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 4.");
          //DEBUG nkt.printGeneratedConstraints(nkt.typeConstraints);
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 5.");
          nkt.init(); // Reset all lists for the next independent match block 
          //DEBUG System.out.println("\n Test pour inferCode -- ligne 6.");
          return `InstructionToCode(Match(result,options));
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferCode: failure on " + `code);
        }
      }
    }
  }

  %strategy inferInstruction(nkt:NewKernelTyper) extends Identity() {
    visit Instruction {
      Match(constraintInstructionList,options) -> {
        //DEBUG System.out.println("\n Test pour inferInstruction -- ligne 1.");
        // Generate type constraints for a %match in action side
        ConstraintInstructionList result = nkt.inferConstraintInstructionList(`constraintInstructionList);
        nkt.printGeneratedConstraints(nkt.typeConstraints);
        return `Match(result,options);
      }
    } 
  }
  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList cilist) {
    //DEBUG System.out.println("\n Test pour inferConstraintInstructionList -- ligne 1.");
    %match(cilist) {
      concConstraintInstruction() -> { return cilist; }
      concConstraintInstruction(headCIList@ConstraintInstruction(constraint,action,_),tailCIList*) -> {
        try {
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 2.");
          // Collect variables and type them with fresh type variables
          // Rename variables of pattern that already exist in varPatternList
          ConstraintInstruction typedHead =
            `TopDownCollect(CollectVars(this)).visitLight(`headCIList);
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 3.");
          //DEBUG System.out.println("\n varPatternList = " + `varPatternList);
          //DEBUG System.out.println("\n varList = " + `varList);
          //DEBUG System.out.println("\n Constraints = " + typeConstraints);
          inferConstraintList(`constraint);
          //`RepeatId(inferConstraint(this)).visitLight(`constraint);
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 4.");
          %match(action) {
            RawAction(AbstractBlock(instructions@concInstruction(_,_*))) -> {
              InstructionList result = `_concInstruction(inferInstruction(this)).visitLight(`instructions);
              //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 5.");
            }
          }
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
        TomTerm newVar = `var;
        %match {
          // --- Case of Variable
          // If the variable already exists in varPatternList
          Variable[Option=option,AstName=name,AstType=type1,Constraints=CIList]
            << var &&
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList -> {
            //maybe this is not necessary since the variable generated has type2
            nkt.addConstraint(`Equation(type1,type2));
            `newVar = `Variable(option,name,type2,CIList);
          }
          // If the variable doesn't exist in varPatternList
          // (or if varPatternlist is empty)
          Variable[AstName=name] << var &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) <<
          nkt.varPatternList -> {
            nkt.addTomTerm(`var);
          }

          // --- Case of VariableStar
          // If the star variable already exists in varPatternList 
          VariableStar[Option=option,AstName=name,AstType=type1,Constraints=CIList]
            << var &&
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList -> {
            nkt.addConstraint(`Equation(type1,type2));
            `newVar = `VariableStar(option,name,type2,CIList);
          }
          // If the star variable doesn't exist in varPatternList
          // (or if varPatternList is empty)
          VariableStar[AstName=name] << var &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) << nkt.varPatternList -> {
            nkt.addTomTerm(`var);
          }
        }
        return newVar;
      }
    }

    visit BQTerm {
      bqvar -> {
        BQTerm newBQVar = `bqvar;
        %match {
          // --- Case of Variable
          // If the backquote variable already exists in varPatternList 
          // (in the case of a inner match) or in varList
          BQVariable[Option=option,AstName=name,AstType=type1] << bqvar &&
          (concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2@!type1),_*) << nkt.varList ||
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList) -> {
            nkt.addConstraint(`Equation(type1,type2));
            newBQVar = `BQVariable(option,name,type2);
          }
          // If the backquote variable exists neither in varPatternList nor in
          // varList (or if one of them, or both, are empty) 
          BQVariable[AstName=name] << bqvar &&
          !concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,_),_*) << nkt.varList &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) << nkt.varPatternList -> {
            nkt.addBQTerm(`bqvar);
          }

          // --- Case of VariableStar
          // If the backquote star variable already exists in varPatternList 
          // (in the case of a inner match) or in varList        
          BQVariableStar[Option=option,AstName=name,AstType=type1] << bqvar &&
          (concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,type2@!type1),_*) << nkt.varList ||
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList) -> {
            nkt.addConstraint(`Equation(type1,type2));
            newBQVar = `BQVariable(option,name,type2);
          }
           // If the backquote star variable exists neither in varPatternList nor in
          // varList (or if one of them, or both, are empty)
          BQVariable[AstName=name] << bqvar &&
          !concBQTerm(_*,(BQVariable|BQVariableStar)(_,name,_),_*) << nkt.varList &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) << nkt.varPatternList -> {
            nkt.addBQTerm(`bqvar);
          }
        }
        return newBQVar;
      }
    }
  }
  
  private void inferConstraintList(Constraint constraint) {
    %match {
      //TODO (AndConstraint|OrConstraint)(headCList,tailCList*)
      AndConstraint(headCList,tailCList*) << constraint -> {
        //DEBUG System.out.println("In inferConstraintList!!!");   
        //DEBUG System.out.println("headCList = " + `headCList);   
        //DEBUG System.out.println("tailCList = " + `tailCList);   
        ConstraintList CList = `concConstraint(headCList,tailCList);
        try {
          `_concConstraint(inferConstraint(this)).visitLight(`CList);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintList: failure on " + `CList);
        }
      }
      OrConstraint(headCList,tailCList*) << constraint -> {
        //DEBUG System.out.println("In inferConstraintList!!!");   
        //DEBUG System.out.println("headCList = " + `headCList);   
        //DEBUG System.out.println("tailCList = " + `tailCList);   
        ConstraintList CList = `concConstraint(headCList,tailCList);
        try {
          `_concConstraint(inferConstraint(this)).visitLight(`CList);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintList: failure on " + `CList);
        }
      }
      
      (MatchConstraint(_,_) << constraint || 
       NumericConstraint(_,_,_) << constraint) -> {
        try {
          `RepeatId(inferConstraint(this)).visitLight(`constraint);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintList: failure on " +
              `constraint);
        }
      }
    }
  }

/*
  private void inferConstraint(Constraint constraint) {
    %match {
      AndConstraint(CList*) << constraint -> {
        System.out.println("In inferConstraint!!!");   
        System.out.println("constraint = " + `constraint);   
        System.out.println("CList = " + `CList);   
        try {
          `_AndConstraint(inferConstraint1(this)).visitLight(`constraint);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraint: failure on " + `constraint);
        }
      }
    }
    throw new TomRuntimeException("inferConstraint: should not be here.");
  }

  
  private void inferConstraint(Constraint constraint) {
    %match {
      (AndConstraint|OrConstraint)(CList*) << constraint -> {
        try {
          `RepeatId(inferConstraint(this)).visitLight(`CList*);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraint: failure on " + `CList);
        }
      }
      (MatchConstraint(_,_) << constraint || 
       NumericConstraint(_,_,_) << constraint) -> {
        try {
          `RepeatId(inferConstraint(this)).visitLight(`constraint);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraint: failure on " +
              `constraint);
        }
      }
    }
    throw new TomRuntimeException("inferConstraint: should not be here.");
  }
*/
  %strategy inferConstraint(nkt:NewKernelTyper) extends Identity() {
    visit Constraint {
      constraint -> {
        //DEBUG System.out.println("\n Test pour inferConstraint in MatchConstraint -- ligne 1.");
        //DEBUG System.out.println("varPatternList = " + `nkt.varPatternList);
        //DEBUG System.out.println("varList = " + `nkt.varList);
        %match {
/*
          MatchConstraint(pattern,subject) << constraint &&
            !Variable(_*,_,_,_*) << pattern &&
          !VariableStar(_*,_,_,_*) << pattern -> {
            //DEBUG System.out.println("\n Test pour inferConstraint in MatchConstraint -- ligne 1.2.");
            TomType freshType1 = nkt.getUnknownFreshTypeVar();
            TomType freshType2 = nkt.getUnknownFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferTomTerm(`pattern,freshType1);
            nkt.inferBQTerm(`subject,freshType2);
          }

          // handle non-linearity
          MatchConstraint(pattern,subject) << constraint &&
          concTomTerm(_*,pattern,_*) << nkt.varPatternList &&
          concBQTerm(_*,subject,_*) << nkt.varList -> {
            //DEBUG System.out.println("\n Test pour inferConstraint in MatchConstraint -- ligne 2.");
            TomType freshType1 = nkt.getUnknownFreshTypeVar();
            TomType freshType2 = nkt.getUnknownFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(pattern)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(subject)));
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferTomTerm(`pattern,freshType1);
            nkt.inferBQTerm(`subject,freshType2);
          }
*/
          MatchConstraint(pattern,subject) << constraint -> { 
            TomType freshType1 = nkt.getUnknownFreshTypeVar();
            TomType freshType2 = nkt.getUnknownFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(pattern)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(subject)));
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferTomTerm(`pattern,freshType1);
            nkt.inferBQTerm(`subject,freshType2);
          }

          NumericConstraint(left,right,_) << constraint &&
          concBQTerm(_*,left,_*) << nkt.varList &&
          concBQTerm(_*,right,_*) << nkt.varList -> {
            TomType freshType1 = nkt.getUnknownFreshTypeVar();
            TomType freshType2 = nkt.getUnknownFreshTypeVar();
            // It will be useful for subtyping
            // TomType freshType3 = nkt.getUnknownFreshTypeVar();
            nkt.addConstraint(`Equation(freshType1,nkt.getType(left)));
            nkt.addConstraint(`Equation(freshType2,nkt.getType(right)));
            nkt.addConstraint(`Equation(freshType1,freshType2));
            nkt.inferBQTerm(`left,freshType1);
            nkt.inferBQTerm(`right,freshType2);
          }
        }
      }
    }  
  } 

  private void inferTomTerm(TomTerm term, TomType freshType) {
    //DEBUG System.out.println("\n Test pour inferTomTerm. Term = " + term);
    %match(term) {
      AntiTerm(atomicTerm) -> { inferTomTerm(`atomicTerm,freshType); }

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
      //TODO Extends this case to TermAppl and XMLAppl
      //(TermAppl|RecordAppl|XMLAppl)[NameList=(Name(tomName),_*),Slots=slotList] -> {
      RecordAppl[NameList=concTomName(Name(tomName),_*),Slots=slotList] -> {
        // Do we need to test if the nameList is equal to ""???
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomName = " + `tomName);
        TomSymbol tomSymbol = getSymbolFromName(`tomName);
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomSymbol = " + tomSymbol);
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
            argFreshType = getUnknownFreshTypeVar();
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
            argFreshType = getUnknownFreshTypeVar();
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
            argFreshType = getUnknownFreshTypeVar();
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
            argFreshType = getUnknownFreshTypeVar();
            addConstraint(`Equation(argFreshType,argType));
            `inferBQTerm(arg,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }
    }
  }


  %strategy solveConstraints(nkt:NewKernelTyper) extends Identity() {
    visit TypeConstraintList {
      // E.g. Equation(Type("A",TypeVar(0)),Type("B",TLType(" test.test.types.B ")))
      concTypeConstraint(_*,Equation(t1@Type(tName1,_),t2@Type(tName2@!tName1,_)),_*) &&
      (tName1 != "unknown type") && (tName2 != "unknown type")  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }

      //TODO take care about  TypeWithSymbol(TomType:String,TlType:TomType,RootSymbolName:TomName)
      // E.g. Equation(Type("unknown type",TypeVar(0)),Type("B",TypeVar(0)))
      concTypeConstraint(_*,Equation(t1@Type(_,tType1@TLType(_)),t2@Type(_,tType2@TLType(_))),_*) &&
      (tType1 != tType2)  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }

      //without Equation(type,type)
      concTypeConstraint(leftTCList*,Equation(typeVar@Type(_,TypeVar(_)),type@!typeVar),rightTCList*) -> {
        nkt.substitutions.put(`typeVar,`type);
        %match {
          !concTypeConstraint() << leftTCList -> {
            if(nkt.findTypeVars(`typeVar,`leftTCList)) {
              `leftTCList = nkt.applySubstitution(`typeVar,`type,`leftTCList);
            }
          }

          !concTypeConstraint() << rightTCList -> {
            if(nkt.findTypeVars(`typeVar,`rightTCList)) {
              `rightTCList = nkt.applySubstitution(`typeVar,`type,`rightTCList);
            }
          }
        }
        return `concTypeConstraint(leftTCList*,Equation(type,type),rightTCList*);
      }

      concTypeConstraint(leftTCList*,Equation(type@Type(_,TLType(_)),typeVar@Type(_,TypeVar(_))),rightTCList*) -> {
        nkt.substitutions.put(`typeVar,`type);
        %match {
          !concTypeConstraint() << leftTCList -> {
            if(nkt.findTypeVars(`typeVar,`leftTCList)) {
              `leftTCList = nkt.applySubstitution(`typeVar,`type,`leftTCList);
            }
          }

          !concTypeConstraint() << rightTCList -> {
            if(nkt.findTypeVars(`typeVar,`rightTCList)) {
              `rightTCList = nkt.applySubstitution(`typeVar,`type,`rightTCList);
            }
          }
        }
        return `concTypeConstraint(leftTCList*,Equation(type,type),rightTCList*);
      }
    }
  }

  private boolean findTypeVars(TomType typeVar, TypeConstraintList
      tcList) {
    //DEBUG System.out.println("\n Test pour findTypeVars -- ligne 1.");
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
        //`TopDown(replaceFreshTypeVar(oldtt,newtt,this)).visitLight(tcList);
        `TopDown(replaceFreshTypeVar(this)).visitLight(tcList);
    } catch(tom.library.sl.VisitFailure e) {
      throw new RuntimeException("applySubstitution: should not be here.");
    }
  }

  private ConstraintInstructionList
    replaceInConstraintInstructionList(ConstraintInstructionList ciList) {
      //DEBUG System.out.println("\n Test pour replaceInConstraintInstructionList -- ligne 1.");
      //DEBUG System.out.println("\n Substitutions = " + `substitutions);
      //DEBUG System.out.println("\n CIList = " + ciList);
      ConstraintInstructionList replacedCIList = ciList;
      try {
        replacedCIList =
          `RepeatId(TopDown(replaceFreshTypeVar(this))).visitLight(ciList);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInConstraintInstructionList: failure on " +
            replacedCIList);
      }
      return replacedCIList;
  }


  private void replaceInSymbolTable() {
    Iterator<String> it = symbolTable.keySymbolIterator();
    while(it.hasNext()) {
      String tomName = it.next();
      TomSymbol tomSymbol = getSymbolFromName(tomName);
      try {
        `RepeatId(TopDown(replaceFreshTypeVar(this))).visitLight(tomSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
          tomSymbol);
      }
      symbolTable.putSymbol(tomName,tomSymbol);
    }
  }

  %strategy replaceFreshTypeVar(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar && Type(_,TypeVar(_)) << typeVar -> {
        if (nkt.substitutions.containsKey(`typeVar)) {
          return nkt.substitutions.get(`typeVar);
        }    
      }
    }
  }
/*
  %strategy replaceFreshTypeVar(oldtt:TomType,newtt:TomType,nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar && (typeVar == oldtt) -> { return newtt; }
    }
  }
*/
  public void printGeneratedConstraints(TypeConstraintList TCList) {
    %match(TCList) {
      !concTypeConstraint() -> { 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(TCList);
        System.out.print("}");
      }
    }
  }
  public void printEachConstraint(TypeConstraintList TCList) {
    %match(TCList) {
      concTypeConstraint(Equation(type1,type2),tailTCList*) -> {
        printType(`type1);
        System.out.print(" = ");
        printType(`type2);
        if (`tailTCList != `concTypeConstraint()) {
            System.out.print(", "); 
            printEachConstraint(`tailTCList);
        }
      }
    }
  }
    
  public void printType(TomType type) {
    System.out.print(type);
  }
} // NewKernelTyper
