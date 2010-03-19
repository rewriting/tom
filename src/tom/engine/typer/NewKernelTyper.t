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

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class NewKernelTyper {
  %include { ../../library/mapping/java/sl.tom}
  %include { ../adt/tomsignature/TomSignature.tom }

  %typeterm NewKernelTyper {
    implement { NewKernelTyper }
    is_sort(t) { ($t instanceof NewKernelTyper) }
  }

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
    return TomBase.getSymbolFromTerm(bqTerm,getSymbolTable());
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
    return TomBase.getSymbolFromType(type,getSymbolTable()); 
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
      AntiTerm(atomicTerm) -> { return getType(`atomicTerm); }
    } 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  protected TomType getType(BQTerm term) {
    %match(term) {
      (BQVariable|BQVariableStar|BuildReducedTerm)[AstType=type] -> {
        return `type;
      }
      // Which constructors of BQTerm we need to treat?? 
      (BQAppl|BuildConstant)[AstName=Name(name)] -> {
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
    %match {
      !concTypeConstraint(_*,tConstraint,_*) << typeConstraints &&
      tConstraint << TypeConstraint constraint -> {
        typeConstraints = `concTypeConstraint(constraint,typeConstraints*);
      }
    }
  }

  protected void addTomTerm(TomTerm term) {
    varPatternList = `concTomTerm(term,varPatternList*);
  }

  protected void addBQTerm(BQTerm term) {
    varList = `concBQTerm(term,varList*);
  }

  /**
   * The method <code>resetVarPatternList</code> empties varPatternList after
   * checking if <code>varList</code> contains
   * a corresponding BQTerm in order to remove it from <code>varList</code. too
   */
  protected void resetVarPatternList() {
    for(TomTerm tomTerm: varPatternList.getCollectionconcTomTerm()) {
      %match(tomTerm,varList) {
        (Variable|VariableStar)[AstName=name],concBQTerm(x*,(BQVariable|BQVariableStar)[AstName=name],y*)
          -> {
            varList = `concBQTerm(x*,y*);
          }
      }
    }
    varPatternList = `concTomTerm();
  }

  /**
   * The method <code>init</code> empties all global lists and hashMaps which means to
   * empty <code>varPatternList</code>, <code>varList</code>,
   * <code>typeConstraints</code> and <code>substitutions</code>
   */
  private void init() {
    varPatternList = `concTomTerm();
    varList = `concBQTerm();
    typeConstraints = `concTypeConstraint();
    substitutions = new HashMap<TomType,TomType>();
  }

  /**
   * The method <code>inferCode</code> starts inference process which takes one
   * <code>%match</code> instruction ("InstructionToCode(Match(...))") at a time
   * <ul>
   *  <li> each "constraintInstructionList" element corresponds to a pair
   *  "condition -> action"
   *  <li> each pair is traversed in order to generate type constraints
   *  <li> the type constraints of "typeConstraints" list are solved at the end
   *        of the current <code>%match</code> instruction generating a mapping (a set of
   *        substitutions for each type variable)
   *  <li> the mapping is applied over the whole <code>%match</code> instruction
   *  <li> all lists and hashMaps are reset
   * </ul>
   * @param code  the tom code to be type inferred
   * @return      the tom typed code
   */
  public Code inferCode(Code code) {
    init();
    //DEBUG System.out.println("\n Test pour inferCode -- ligne 1.");
    System.out.println("\n Original Code = \n" + code + '\n');
    %match(code) {
      Tom(codes@concCode(_,_*)) -> {
        boolean flagInnerMatch = false;
        CodeList codeResult = `concCode();
        for(Code headCodeList : `codes.getCollectionconcCode()) {
          %match(headCodeList) {
            InstructionToCode(Match(constraintInstructionList,options)) -> {
              try {
                flagInnerMatch = false;
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 1.");
                // Generate type constraints for a %match
                //DEBUG System.out.println("CIList avant = " + `constraintInstructionList);
                inferConstraintInstructionList(`constraintInstructionList,flagInnerMatch);
                //DEBUG System.out.println("CIList après= " + result + "\n\n");
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 2.");
                System.out.println("\n typeConstraints before solve = " + typeConstraints);
                typeConstraints = `RepeatId(solveConstraints(this)).visitLight(typeConstraints);
                System.out.println("\n typeConstraints aftersolve = " + typeConstraints);
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 3.");
                ConstraintInstructionList result =
                  replaceInConstraintInstructionList(`constraintInstructionList);
                replaceInSymbolTable();
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 4.");
                //DEBUG printGeneratedConstraints(typeConstraints);
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 5.");
                init(); // Reset all lists for the next independent match block 
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 6.");
                `headCodeList = `InstructionToCode(Match(result,options));
              } catch(tom.library.sl.VisitFailure e) {
                throw new TomRuntimeException("inferCode: failure on " +
                    `headCodeList);
              } 
            }
          }
          codeResult = `concCode(codeResult*,headCodeList);
        }
        return `Tom(codeResult);
      }
    }
    // If it is a ill-formed code (different from "Tom(...)")
    return code;
  }

  /**
   * The method <code>inferConstraintInstructionList</code> applies rule CT-RULE
   * to a pair "condition -> action" in order to collect all variables occurring
   * in the condition and put them into <code>varPatternList</code> (for those
   * variables occurring in match constraints) and <code>varList</code> (for
   * those variables occurring in numeric constraints) to be able to handle non-linearity.
   * The condition (left-hand side) is traversed and then the action (right-hand
   * side) is traversed in order to generate type constraints.
   * <p>
   * CT-RULE rule:
   * IF found "cond --> (e1,...,en)" where "ei" are backquote terms composing
   * the action
   * THEN infers types of condition (by calling <code>inferConstraint</code>
   * method) and action (by calling <code>inferConstraintList</code> for each
   * match constraint occurring in the action) 
   * @param ciList  the pair "condition -> action" to be type inferred 
   */
  private void inferConstraintInstructionList(ConstraintInstructionList ciList,
      boolean flagInnerMatch) {
    //DEBUG System.out.println("\n Test pour inferConstraintInstructionList -- ligne 1.");
    %match(ciList) {
      concConstraintInstruction(headCIList@ConstraintInstruction(constraint,action,_),tailCIList*) -> {
        try {
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 4.");
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 2.");
          // Collect variables and type them with fresh type variables
          // Rename variables of pattern that already exist in varPatternList
          //DEBUG System.out.println("\n Test pour inferConstraintInstructionList dans un match -- ligne 3.");
          //DEBUG System.out.println("\n varPatternList = " + `varPatternList);
          //DEBUG System.out.println("\n varList = " + `varList);
          //DEBUG System.out.println("\n Constraints = " + typeConstraints);
          inferConstraint(`constraint);
          inferAction(`action);
          if (!flagInnerMatch) {
            `TopDownCollect(CollectVars(this)).visitLight(`headCIList);
            System.out.println("\n Test pour inferConstraintInstructionList après reset.");
            System.out.println("\n varPatternList avant = " + `varPatternList);
            //DEBUG System.out.println("\n varList avant = " + `varList); 
            resetVarPatternList();
            System.out.println("\n varPatternList après = " + `varPatternList);
            //DEBUG System.out.println("\n varList après = " + `varList);
          }
          inferConstraintInstructionList(`tailCIList,flagInnerMatch);
        } catch(tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("inferConstraintInstructionList: failure on " + `headCIList);
        }
      }
    }
  }

  private void inferAction(Instruction action) {
    %match(action) {
      RawAction(AbstractBlock(instructions@concInstruction(_,_*))) -> {
        boolean flagInnerMatch = true;
        for(Instruction headInstruction : `instructions.getCollectionconcInstruction()) {
          %match(headInstruction) {
            Match(constraintInstructionList,_) -> {
              flagInnerMatch = true; 
              // Generate type constraints for a %match in action side
              inferConstraintInstructionList(`constraintInstructionList,flagInnerMatch);
              //DEBUG printGeneratedConstraints(typeConstraints);
            }
            //TODO : add case for java methods or bqvariables or bqapp 
          }
        }  
      }
    }
  }



  /**
   * The class <code>CollectVars</code> is generated from a strategy which
   * search for variables occurring in a condition:
   * <ul>
   * <li> variables of type "TomTerm" are adedd to the <code>varPatternList</code> (for those
   * variables occurring in match constraints) if they have not yet been added
   * there; otherwise, a type constraint is added to
   * <code>typeConstraints</code> to ensure that both variables have same type
   * (this happens in case of non-linearity)
   * <li> variables of type "BQTerm" are added to the <code>varList</code> (for
   * those variables occurring in numeric constraints) if they have been added
   * neither in <code>varPatternList</code> nor <code>varList</code> (since a
   * BQVariable/BQVariableStar can have occurred in a previous match constraint
   * as a Variable/VariableStar, in the case of a composed condition);
   * otherwise, a type constraint is added to
   * <code>typeConstraints</code> to ensure that both variables have same type
   * (this happens in case of non-linearity)
   * </ul>
   */
  %strategy CollectVars(nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      var -> {
        %match {
          // --- Case of Variable
          // If the variable already exists in varPatternList
          Variable[AstName=name,AstType=type1] << var &&
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList -> {
            //maybe this is not necessary since the variable generated has type2
            nkt.addConstraint(`Equation(type1,type2));
          }
          // If the variable doesn't exist in varPatternList
          // (or if varPatternlist is empty)
          Variable[AstName=name] << var &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) << nkt.varPatternList -> {
            nkt.addTomTerm(`var);
          }

          // --- Case of VariableStar
          // If the star variable already exists in varPatternList 
          VariableStar[AstName=name,AstType=type1] << var &&
          concTomTerm(_*,(Variable|VariableStar)(_,name,type2@!type1,_),_*) << nkt.varPatternList -> {
            nkt.addConstraint(`Equation(type1,type2));
          }
          // If the star variable doesn't exist in varPatternList
          // (or if varPatternList is empty)
          VariableStar[AstName=name] << var &&
          !concTomTerm(_*,(Variable|VariableStar)(_,name,_,_),_*) << nkt.varPatternList -> {
            nkt.addTomTerm(`var);
          }
        }
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

  /**
   * The method <code>inferConstraint</code> applies rule CT-MATCH, CT-EQ, CT-CONJ,
   * or CT-DISJ to a "condition" in order to infer its type.
   * <p>
   * CT-MATCH rule:
   * IF found "e1 << [T] e2" 
   * THEN infers type of e1 and e2 and add a type constraint "T1 = T2", where
   * "Ti" is a fresh type variable generated to "ei" (by
   * <code>inferBQTerm</code>, a type constraint "T = T2" will be added)
   * <p>
   * CT-EQ rule:
   * IF found "e1 == e2", "e1 <= e2", "e1 < e2", "e1 >= e2" or "e1 > e2"
   * THEN infers type of e1 and e2 and add a type constraint "T1 = T2", where
   * "Ti" is a fresh type variable generated to "ei"  
   * <p>
   * CT-CONJ rule (resp. CT-DISJ):
   * IF found "cond1 && cond2" (resp. "cond1 || cond2")
   * THEN infers type of cond1 and cond2 (by calling
   * <code>inferConstraint</code> for each condition)
   * @param constraint the "condition" to be type inferred 
   */
  private void inferConstraint(Constraint constraint) {
    %match(constraint) {
      MatchConstraint(pattern,subject) -> { 
        //DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
        TomType freshType1 = getUnknownFreshTypeVar();
        TomType freshType2 = getUnknownFreshTypeVar();
        addConstraint(`Equation(freshType1,getType(pattern)));
        addConstraint(`Equation(freshType2,getType(subject)));
        addConstraint(`Equation(freshType1,freshType2));
        inferTomTerm(`pattern,freshType1);
        inferBQTerm(`subject,freshType2);
      }

      NumericConstraint(left,right,_) -> {
        TomType freshType1 = getUnknownFreshTypeVar();
        TomType freshType2 = getUnknownFreshTypeVar();
        // It will be useful for subtyping
        // TomType freshType3 = getUnknownFreshTypeVar();
        addConstraint(`Equation(freshType1,getType(left)));
        //DEBUG System.out.println("inferConstraint l1 - typeConstraints = " + typeConstraints);
        addConstraint(`Equation(freshType2,getType(right)));
        //DEBUG System.out.println("inferConstraint l2 - typeConstraints = " + typeConstraints);
        addConstraint(`Equation(freshType1,freshType2));
        inferBQTerm(`left,freshType1);
        inferBQTerm(`right,freshType2);
      }

      AndConstraint(headCList,tailCList*) -> {
        ConstraintList cList = `concConstraint(headCList,tailCList);
        while(!cList.isEmptyconcConstraint()) {
          inferConstraint(cList.getHeadconcConstraint());
          cList = cList.getTailconcConstraint();
        }
      }

      OrConstraint(headCList,tailCList*) -> {
        ConstraintList cList = `concConstraint(headCList,tailCList);
        while(!cList.isEmptyconcConstraint()) {
          inferConstraint(cList.getHeadconcConstraint());
          cList = cList.getTailconcConstraint();
        }
      }
    }
  }

  /**
   * The method <code>inferTomTerm</code> applies rule CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
   * CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) in order
   * to infer its type.
   * <p>
   * CT-ANTI rule:
   * IF found "!(e):A"
   * THEN infers type of e
   * <p>
   * CT-VAR rule (resp. CT-SVAR): 
   * IF found "x:A" (resp. "x*:A") and "x:T" (resp. "x*:T") already exists in SymbolTable 
   * THEN add a type constraint "A = T"
   * <p>
   * CT-FUN rule:
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "A = T" and
   *      calls the TypeVariableList method which adds a type constraint "Ai =
   *      Ti" for each argument, where Ai is a fresh type variable
   * <p>
   * CT-EMPTY rule:
   * IF found "l():AA" and "l:T*->TT" exists in SymbolTable
   * THEN add a type constraint "AA = TT"       
   * <p>
   * CT-ELEM rule:
   * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "AA = TT" and calls the
   *      TypeVariableList method which adds a type constraint "A =T"
   *      for the last argument, where A is a fresh type variable and
   *      "e" does not represent a list with head symbol "l"
   * <p>
   * CT-MERGE rule:
   * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "AA = TT" and calls the
   *      TypeVariableList method, where "e" represents a list with
   *      head symbol "l"
   * <p>
   * CT-STAR rule:
   * Equals to CT-MERGE but with a star variable "x*" instead of "e"
   * This rule is necessary because it differed from CT-MERGE in the
   * sense of the type of the last argument ("x*" here) is unknown 
   * @param term      the TomTerm to be inferred
   * @param freshType the fresh generated previously and attributed to the TomTerm
   */
  private void inferTomTerm(TomTerm term, TomType freshType) {
    //DEBUG System.out.println("\n Test pour inferTomTerm. Term = " + term);
    %match(term) {
      AntiTerm(atomicTerm) -> { inferTomTerm(`atomicTerm,freshType); }

      (Variable|VariableStar)[AstType=type,Constraints=constraintList] -> {
        addConstraint(`Equation(type,freshType));  
        %match(constraintList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(term)) -> { addConstraint(`Equation(getType(term),freshType)); }
        }
      }

      //TODO Extends this case to TermAppl and XMLAppl
      //(TermAppl|RecordAppl|XMLAppl)[NameList=(Name(tomName),_*),Slots=slotList] -> {
      RecordAppl[NameList=concTomName(Name(tomName),_*),Slots=slotList,Constraints=constraintList] -> {
        // Do we need to test if the nameList is equal to ""???
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomName = " + `tomName);
        TomSymbol tomSymbol = getSymbolFromName(`tomName);
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomSymbol = " + tomSymbol);
  
        %match(constraintList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(term)) -> { addConstraint(`Equation(getType(term),freshType)); }
        }

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
        //TODO Do we need to test if the nameList is equal to ""???
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
