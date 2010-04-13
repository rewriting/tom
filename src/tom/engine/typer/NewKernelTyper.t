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
 * Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
 *
 **/



package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;

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

  private int freshTypeVarCounter = 0;
  /*
   * pem: why use a state variable here ?
   */
  // List for variables of pattern (match constraints)
  private TomList varPatternList;
  // List for variables of subject and of numeric constraints
  private BQTermList varList;
  /*
   * pem: why use a state variable here ?
   */
  // List for type constraints (for fresh type variables)
  private TypeConstraintList typeConstraints;
  // Set of pairs (freshVar,groundVar)
  private HashMap<TomType,TomType> substitutions;

  private SymbolTable symbolTable;

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  protected SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected TomType getCodomain(TomSymbol tSymbol) {
    //DEBUG System.out.println("getCodomain = " + TomBase.getSymbolCodomain(tSymbol));
    return TomBase.getSymbolCodomain(tSymbol);
  }

  protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
    return TomBase.getSymbolFromTerm(tTerm, getSymbolTable());
   }

  protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
    return TomBase.getSymbolFromTerm(bqTerm,getSymbolTable());
  }

  protected TomSymbol getSymbolFromName(String tName) {
    return TomBase.getSymbolFromName(tName, getSymbolTable());
   }

  protected TomSymbol getSymbolFromType(TomType tType) {
    %match(tType) {
       TypeWithSymbol[TomType=tomType, TlType=tlType] -> {
         return TomBase.getSymbolFromType(`Type(tomType,tlType), getSymbolTable()); 
       }
     }
    return TomBase.getSymbolFromType(tType,getSymbolTable()); 
   }

  protected TomType getType(String tName) {
    return getSymbolTable().getType(tName); 
   }

  protected TomType getType(TomTerm tTerm) {
    %match(tTerm) {
      (Variable|VariableStar)[AstType=aType] -> { return `aType; }

      RecordAppl[NameList=(Name(name),_*)] -> {
        if(`name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        //DEBUG System.out.println("\n------- getType : name = " + `name +'\n');
        TomSymbol tSymbol = getSymbolFromName(`name);
        //DEBUG System.out.println("\n------- after getSymbolFromName");
        return getCodomain(tSymbol);
      }
      AntiTerm(atomicTerm) -> { return getType(`atomicTerm); }
    } 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  protected TomType getType(BQTerm bqTerm) {
    %match(bqTerm) {
      (BQVariable|BQVariableStar|BuildReducedTerm)[AstType=aType] -> {
        return `aType;
      }

      // Which constructors of BQTerm we need to treat?? 
      (BQAppl|FunctionCall|BuildConstant)[AstName=Name(name)] -> {
        if(`name.equals("")) {
          // Maybe we need to discover the symbol using the context type
          // information (i.e. the type of subject)
          throw new TomRuntimeException("No symbol found because there is no name.");
        }
        TomSymbol tSymbol = getSymbolFromName(`name);
        return getCodomain(tSymbol);
      }
    } 
    throw new TomRuntimeException("getType(BQTerm): should not be here.");
  }
/* TO REPLACE getFreshTypeVar()
  protected int getFreshTlTIndex() {
    return freshTypeVarCounter++;
   }
*/
  protected TargetLanguageType getFreshTypeVar() {
    return `TypeVar(freshTypeVarCounter++);
  }

/* TO REPLACE getUnknownFreshTypeVar()  
   protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.UNKNOWN_TYPE;
    %match(tType) {
      Type[TomType=tomType] -> { return `TypeVar(tomType,getFreshTlTIndex())}
    }
    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
   }
*/

  protected TomType getUnknownFreshTypeVar() {
    return `Type("unknown type",TypeVar(freshTypeVarCounter++));
  }

  /*
     * pem: use if(...==... && typeConstraints.contains(...))
     */
  protected void addConstraint(TypeConstraint tConstraint) {
    %match {
      !concTypeConstraint(_*,typeConstraint,_*) << typeConstraints &&
      typeConstraint << TypeConstraint tConstraint -> {
        typeConstraints = `concTypeConstraint(tConstraint,typeConstraints*);
      }
    }
  }

  protected void addTomTerm(TomTerm tTerm) {
    varPatternList = `concTomTerm(tTerm,varPatternList*);
   }

  protected void addBQTerm(BQTerm bqTerm) {
    varList = `concBQTerm(bqTerm,varList*);
   }

  /**
   * The method <code>resetVarPatternList</code> empties varPatternList after
   * checking if <code>varList</code> contains
   * a corresponding BQTerm in order to remove it from <code>varList</code. too
   */
  protected void resetVarPatternList() {
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      %match(tTerm,varList) {
        (Variable|VariableStar)[AstName=aName],concBQTerm(x*,(BQVariable|BQVariableStar)[AstName=aName],y*)
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
    //DEBUG System.out.println("\n Original Code = \n" + code + '\n');
    %match(code) {
      Tom(codes@concCode(_,_*)) -> {
        boolean flagInnerMatch = false;
        CodeList codeResult = `concCode();
        for(Code headCodeList : `codes.getCollectionconcCode()) {
          %match(headCodeList) {
            InstructionToCode(Match(constraintInstructionList,_)) -> {
              try {
                flagInnerMatch = false;
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 1.");
                // Generate type constraints for a %match
                //DEBUG System.out.println("CIList avant = " + `constraintInstructionList);
                inferConstraintInstructionList(`constraintInstructionList,flagInnerMatch);
                //DEBUG System.out.println("CIList apr?s= " + result + "\n\n");
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 2.");
                //DEBUG System.out.println("\n typeConstraints before solve = " + typeConstraints);
                typeConstraints = `RepeatId(solveConstraints(this)).visitLight(typeConstraints);
                //DEBUG System.out.println("\n typeConstraints aftersolve = " + typeConstraints);
                System.out.println("\n substitutions= " + substitutions);
                //DEBUG printGeneratedConstraints(typeConstraints);
                //DEBUG System.out.println("\n Test pour inferCode -- ligne 5.");
              } catch(tom.library.sl.VisitFailure e) {
                throw new TomRuntimeException("inferCode: failure on " +
                    headCodeList);
              } 
            }
          }
          Code headCodeResult = replaceInCode(`headCodeList);
          codeResult = `concCode(codeResult*,headCodeResult);
          replaceInSymbolTable();
          init();
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
            //DEBUG System.out.println("\n Test pour inferConstraintInstructionList apr?s reset.");
            //DEBUG System.out.println("\n varPatternList avant = " + `varPatternList);
            //DEBUG System.out.println("\n varList avant = " + `varList); 
            resetVarPatternList();
            //DEBUG System.out.println("\n varPatternList apr?s = " + `varPatternList);
            //DEBUG System.out.println("\n varList apr?s = " + `varList);
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

  /*
   * pem: simplify the following code to do only one loop
   */
  %strategy CollectVars(nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      var -> {
        %match {
          // --- Case of Variable
          // If the variable already exists in varPatternList
          Variable[AstName=aName,AstType=aType1] << var &&
            concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varPatternList -> {
              //maybe this is not necessary since the variable generated has aType2
              nkt.addConstraint(`Equation(aType1,aType2));
              //DEBUG System.out.println("CollectVars -- constraint = " + `aType1
              //+ " = " + `aType2);
            }

          // If the variable doesn't exist in varPatternList
          // (or if varPatternlist is empty)
          Variable[AstName=aName] << var &&
          !concTomTerm(_*,(Variable|VariableStar)[AstName=aName],_*) << nkt.varPatternList -> {
            nkt.addTomTerm(`var);
          }

          // --- Case of VariableStar
          // If the star variable already exists in varPatternList 
          VariableStar[AstName=aName,AstType=aType1] << var &&
          concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varPatternList -> {
            nkt.addConstraint(`Equation(aType1,aType2));
            //DEBUG System.out.println("CollectVars -- constraint = " + `type1 + " = " + `type2);
          }
          // If the star variable doesn't exist in varPatternList
          // (or if varPatternList is empty)
          VariableStar[AstName=aName] << var &&
          !concTomTerm(_*,(Variable|VariableStar)[AstName=aName],_*) << nkt.varPatternList -> {
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
          BQVariable[Option=option,AstName=aName,AstType=aType1] << bqvar &&
          (concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varList ||
          concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varPatternList) -> {
            nkt.addConstraint(`Equation(aType1,aType2));
            newBQVar = `BQVariable(option,aName,aType2);
         }
          // If the backquote variable exists neither in varPatternList nor in
          // varList (or if one of them, or both, are empty) 
          BQVariable[AstName=aName] << bqvar &&
          !concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName],_*) << nkt.varList &&
          !concTomTerm(_*,(Variable|VariableStar)[AstName=aName],_*) << nkt.varPatternList -> {
            nkt.addBQTerm(`bqvar);
          }

          // --- Case of VariableStar
          // If the backquote star variable already exists in varPatternList 
          // (in the case of a inner match) or in varList        
          BQVariableStar[Option=option,AstName=aName,AstType=aType1] << bqvar &&
          (concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varList ||
          concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << nkt.varPatternList) -> {
            nkt.addConstraint(`Equation(aType1,aType2));
            newBQVar = `BQVariable(option,aName,aType2);
          }
           // If the backquote star variable exists neither in varPatternList nor in
          // varList (or if one of them, or both, are empty)
          BQVariable[AstName=aName] << bqvar &&
          !concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName],_*) << nkt.varList &&
          !concTomTerm(_*,(Variable|VariableStar)[AstName=aName],_*) << nkt.varPatternList -> {
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
        //DEBUG System.out.println("inferConstraint : " + freshType1 + " = " + freshType2);
        addConstraint(`Equation(freshType1,freshType2));
        inferTomTerm(`pattern,freshType1);
        inferBQTerm(`subject,freshType2);
      }

      NumericConstraint(left,right,_) -> {
        TomType freshType1 = getUnknownFreshTypeVar();
        TomType freshType2 = getUnknownFreshTypeVar();
        // It will be useful for subtyping
        // TomType freshType3 = getUnknownFreshTypeVar();
        //addConstraint(`Equation(freshType1,getType(left)));
        //DEBUG System.out.println("inferConstraint l1 - typeConstraints = " + typeConstraints);
        //addConstraint(`Equation(freshType2,getType(right)));
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
   * @param tTerm      the TomTerm to be inferred
   * @param freshType the fresh generated previously and attributed to the TomTerm
   */
  private void inferTomTerm(TomTerm tTerm, TomType freshType) {
    //DEBUG System.out.println("inferTomTerm -- tTerm: " + tTerm);
    //DEBUG System.out.println("\n Test pour inferTomTerm. Term = " + tTerm);
    %match(tTerm) {
      AntiTerm(atomicTerm) -> { inferTomTerm(`atomicTerm,freshType); }

      (Variable|VariableStar)[AstType=type,Constraints=constraintList] -> {
        addConstraint(`Equation(type,freshType));  
        //DEBUG System.out.println("InferTomTerm -- constraint = " + `type + " = " + freshType);
        %match(constraintList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(boundTerm)) -> {
            //DEBUG System.out.println("InferTomTerm -- constraint = " +
            //DEBUG     getType(`boundTerm) + " = " + `freshType);
            addConstraint(`Equation(getType(boundTerm),freshType)); }
        }
      }

      //TODO Extends this case to TermAppl and XMLAppl
      //(TermAppl|RecordAppl|XMLAppl)[NameList=(Name(tomName),_*),Slots=slotList] -> {
      RecordAppl[NameList=concTomName(Name(tomName),_*),Slots=slotList,Constraints=constraintList] -> {
        // Do we need to test if the nameList is equal to ""???
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. tomName = " + `tomName);
        TomSymbol tSymbol = getSymbolFromName(`tomName);
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl.
        //tSymbol = " + tSymbol);
  
        %match(constraintList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(boundTerm)) -> {
            //DEBUG System.out.println("InferTomTerm -- constraint = " +
            //DEBUG     getType(`boundTerm) + " = " + freshType);
            addConstraint(`Equation(getType(boundTerm),freshType)); }
        }

        TomType codomain = getCodomain(tSymbol);
        //DEBUG System.out.println("\n Test pour inferTomTerm in RecordAppl. codomain = " + codomain);
        //DEBUG System.out.println("inferTomTerm: " + `codomain + " = " + freshType);
        addConstraint(`Equation(codomain,freshType));
        //DEBUG System.out.println("InferTomTerm -- constraint = " + codomain + " = " + freshType);
        if (!`slotList.isEmptyconcSlot()) {
          inferSlotList(`slotList,`tSymbol,freshType);
        }
      }
    }
  }

  private void inferBQTerm(BQTerm bqTerm, TomType freshType) {
    //DEBUG System.out.println("inferBQTerm -- bqTerm: " + bqTerm);
    %match(bqTerm) {
      (BQVariable|BQVariableStar)[AstType=aType] -> {
        //DEBUG System.out.println("inferBQTerm: " + `aType + " = " + freshType);
        addConstraint(`Equation(aType,freshType));  
        //DEBUG System.out.println("InferBQTerm -- constraint = " + `aType + " = " + freshType);
      }

      // Special case : when there is a block "rules" in %gom and "provisional
      // subjects" with name "realMake" are created but corresponding to none of
      // symbols existing in SymbolTable (i.e. having a "null" symbol)
      BQAppl[Option=option,AstName=aName@Name("realMake"),Args=concBQTerm(BQVariable[AstName=Name("head")],BQVariable[AstName=Name("tail")])]
        -> {
          TomSymbol tSymbol = getSymbolFromName("realMake");
          if (tSymbol == null) {
            tSymbol =
              `Symbol(aName,TypesToType(concTomType(freshType),freshType),concPairNameDecl(),option);
            getSymbolTable().putSymbol("realMake",tSymbol);
          }
        }

      BQAppl[AstName=Name(name),Args=bqTList] -> {
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. tomName = " + `tomName);
        TomSymbol tSymbol = getSymbolFromName(`name);
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. tSymbol=
        //" + `tSymbol);
        TomType codomain = getCodomain(tSymbol);
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. codomain = " + codomain + '\n');
        //DEBUG System.out.println("\n Test pour inferBQTerm in BQAppl. freshType = " + freshType + '\n');
        //DEBUG System.out.println("inferBQTerm: " + `codomain + " = " + freshType);
        addConstraint(`Equation(codomain,freshType));
        //DEBUG System.out.println("InferBQTerm -- constraint = " + `codomain + " = " + freshType);
        if (!`bqTList.isEmptyconcBQTerm()) {
          inferBQTermList(`bqTList,`tSymbol,freshType);
        }
      }
    }
  }

  private void inferSlotList(SlotList slotList, TomSymbol tSymbol, TomType freshType) {
    %match(slotList,tSymbol) {
      concSlot(PairSlotAppl[Appl=tTerm],tailSList*),Symbol[AstName=symName,TypesToType=TypesToType(domain@concTomType(headTTList,_*),Type(tomCodomain,tlCodomain))] -> {
        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(`tSymbol) || TomBase.isArrayOperator(`tSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(`tTerm);
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
            //DEBUG System.out.println("InferSlotList CT-ELEM -- constraint = " + argFreshType +
            //DEBUG     " = " + `headTTList);
          }
          
          /**
           * Continuation of CT-STAR rule (applying to premises):
           * IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "x", where "x" represents a list with
           *      head symbol "l"
           */
          TomTerm newTerm = `tTerm;
          %match(tTerm) {
            VariableStar[Option=options,AstName=name,Constraints=constraints] -> {
              newTerm =
                `VariableStar(options,name,TypeWithSymbol(tomCodomain,tlCodomain,symName),constraints);  
            }
          }
          /**
           * Continuation of CT-MERGE rule (applying to premises):
           * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
           * THEN infers type of both sublist "l(e1,...,en)" and last argument
           *      "e", where "e" represents a list with
           *      head symbol "l"
           */
          `inferTomTerm(newTerm,argFreshType);
          `inferSlotList(tailSList,tSymbol,freshType);
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
            //DEBUG System.out.println("InferSlotList CT-FUN -- constraint = " + argFreshType +
            //DEBUG     " = " + argType);
            `inferTomTerm(argTerm,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }
    }
  }

  private void inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
      freshType) {
matchL:    %match(bqTList,tSymbol) {
      concBQTerm(headBQTerm@BQVariable[AstName=Name("head")],tailBQTerm@BQVariable[AstName=Name("tail")]),
        Symbol[AstName=Name("realMake")]
        -> {
          //DEBUG System.out.println("What???? headBQTerm = " + `headBQTerm);
          inferBQTerm(`headBQTerm,freshType);
          //DEBUG System.out.println("What???? tailBQTerm = " + `tailBQTerm);
          inferBQTerm(`tailBQTerm,freshType);
          break matchL;
        }

      concBQTerm(bqTerm,tailBQTList*),Symbol[AstName=symName,TypesToType=TypesToType(domain@concTomType(headTTList,_*),Type(tomCodomain,tlCodomain))] -> {
        TomType argFreshType = freshType;
        // In case of a list
        if(TomBase.isListOperator(`tSymbol) || TomBase.isArrayOperator(`tSymbol)) {
          TomSymbol argSymb = getSymbolFromTerm(`bqTerm);
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            argFreshType = getUnknownFreshTypeVar();
            addConstraint(`Equation(argFreshType,headTTList));
            //DEBUG System.out.println("InferBQTermList -- constraint = " + argFreshType +
            //DEBUG     " = " + `headTTList);
          }
          BQTerm newTerm = `bqTerm;
          %match(bqTerm) {
            BQVariableStar[Option=options,AstName=name] -> {
              newTerm =
                `BQVariableStar(options,name,TypeWithSymbol(tomCodomain,tlCodomain,symName));  
            }
          }
          `inferBQTerm(bqTerm,argFreshType);
          `inferBQTermList(tailBQTList,tSymbol,freshType);
        } else {
          // In case of a function
          TomType argType;
          TomTypeList symDomain = `domain;
          for(BQTerm arg : `bqTList.getCollectionconcBQTerm()) {
            argType = symDomain.getHeadconcTomType();
            argFreshType = getUnknownFreshTypeVar();
            addConstraint(`Equation(argFreshType,argType));
            //DEBUG System.out.println("InferBQTermList -- constraint = " + argFreshType +
            //DEBUG     " = " + argType);
            `inferBQTerm(arg,argFreshType);
            symDomain = symDomain.getTailconcTomType();
          }
        }
      }
    }
  }


  %strategy solveConstraints(nkt:NewKernelTyper) extends Identity() {
    visit TypeConstraintList {
      /**
       * Equation(groundListType,groundType) U TCList and Map 
       *    --> Fail
       * if groundListType = tName1^g and groundType = tName2^? 
       * and tName1 != tName2
       */
      // E.g. Equation(Type("A",TypeVar(0)),Type("B",TLType(" test.test.types.B ")))
      concTypeConstraint(_*,Equation(t1@(Type|TypeWithSymbol)[TomType=tName1],t2@Type[TomType=tName2@!tName1]),_*) &&
      (tName1 != "unknown type") && (tName2 != "unknown type")  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }

      /**
       * Equation(groundType,groundListType) U TCList and Map 
       *    --> Fail
       * if groundType = tName1^? and groundListType = tName2^g 
       * and tName1 != tName2
       */
      // E.g. Equation(Type("A",TypeVar(0)),Type("B",TLType(" test.test.types.B ")))
      concTypeConstraint(_*,Equation(t1@Type[TomType=tName1],t2@(Type|TypeWithSymbol)[TomType=tName2@!tName1]),_*) &&
      (tName1 != "unknown type") && (tName2 != "unknown type")  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }

      /**
       * Equation(groundListType,groundType) U TCList and Map 
       *    --> Fail
       * if groundListType has "java type" = tType1
       * and groundType has "java type" = tType2
       * and tType1 != tType2
       */
      // E.g. Equation(Type("A",TLType("A")),Type("B",TLType("B")))
      concTypeConstraint(_*,Equation(t1@(Type|TypeWithSymbol)[TlType=tType1@TLType(_)],t2@Type[TlType=tType2@TLType(_)]),_*) &&
        (tType1 != tType2)  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }
      
      /**
       * Equation(groundType,groundListType) U TCList and Map 
       *    --> Fail
       * if groundType has "java type" = tType1
       * and groundListType has "java type" = tType2
       * and tType1 != tType2
       */
      // E.g. Equation(Type("A",TLType("A")),Type("B",TLType("B")))
      concTypeConstraint(_*,Equation(t1@Type[TlType=tType1@TLType(_)],t2@(Type|TypeWithSymbol)[TlType=tType2@TLType(_)]),_*) &&
        (tType1 != tType2)  -> {
        throw new RuntimeException("solveConstraints: failure on " + `t1
            + " = " + `t2);
      }

      /**
       * Equation(groundListType1,groundListType2) U TCList and Map 
       *    --> Fail
       * if groundListType1 != groundListType2
       */
      // E.g. Equation(Type("A",TLType("A")),Type("B",TLType("B")))
      concTypeConstraint(_*,Equation(tLType1@TypeWithSymbol(_,_,_),tLType2@TypeWithSymbol(_,_,_)),_*) &&
        (tLType1 != tLType2)  -> {
          throw new RuntimeException("solveConstraints: failure on " + `tLType1
              + " = " + `tLType2);
        }

      /**
       * Equation(typeVar,type) U TCList and Map 
       *    --> Equation(type,type) U [typeVar/type]TCList and
       *        [typeVar/type]Map
       */
      // TO REPLACE next line
      //concTypeConstraint(leftTCList*,Equation(typeVar@TypeVar(_,_),type@!typeVar),rightTCList*) -> {

      concTypeConstraint(leftTCList*,Equation(typeVar@(Type|TypeWithSymbol)[TlType=TypeVar(_)],type@!typeVar),rightTCList*) -> {
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

      /**
       * Equation(groundType,typeVar) U TCList and Map 
       *    --> Equation(groundType,groundType) U [typeVar/groundType]TCList and
       *        [typeVar/groundType]Map
       */
      /* TO REPLACE next block
      concTypeConstraint(leftTCList*,Equation(groundType@!TypeVar(_,_),typeVar@TypeVar(_,_)),rightTCList*) -> {
        nkt.substitutions.put(`typeVar,`groundType);
        %match {
          !concTypeConstraint() << leftTCList -> {
            if(nkt.findTypeVars(`typeVar,`leftTCList)) {
              `leftTCList = nkt.applySubstitution(`typeVar,`groundType,`leftTCList);
            }
          }

          !concTypeConstraint() << rightTCList -> {
            if(nkt.findTypeVars(`typeVar,`rightTCList)) {
              `rightTCList = nkt.applySubstitution(`typeVar,`groundType,`rightTCList);
            }
          }
        }
        return `concTypeConstraint(leftTCList*,Equation(groundType,groundType),rightTCList*);
      }
*/
      concTypeConstraint(leftTCList*,Equation(type@(Type|TypeWithSymbol)[TlType=!TypeVar(_)],typeVar@(Type|TypeWithSymbol)[TlType=TypeVar(_)]),rightTCList*) -> {
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
          `TopDown(replaceTypeConstraints(oldtt,newtt)).visitLight(tcList);
    } catch(tom.library.sl.VisitFailure e) {
      throw new RuntimeException("applySubstitution: should not be here.");
    }
  }

  %strategy replaceTypeConstraints(oldtt:TomType, newtt:TomType) extends Identity() {
    visit TomType {
      typeVar && (oldtt == typeVar) -> {
          return newtt; 
      }
    }
  }
 
  private Code replaceInCode(Code code) {
    Code replacedCode = code;
    try {
      replacedCode =
        `RepeatId(TopDown(replaceFreshTypeVar(this))).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " +
          replacedCode);
    }
    return replacedCode;
  }


  private void replaceInSymbolTable() {
    for(String tomName:getSymbolTable().keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomType type = getType(tomName);
      %match(type) {
        Type(typeName,typeVar@TypeVar(_)) -> {
          if (substitutions.containsKey(`typeVar)) {
            type = substitutions.get(`typeVar);
          } else {
            System.out.println("\n----- There is no mapping for " + `typeVar +'\n');
            type = `Type(typeName,EmptyTargetLanguageType());
          }    
          getSymbolTable().putType(`typeName,type);
        }
      }
    }

    for(String tomName:symbolTable.keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomSymbol tSymbol = getSymbolFromName(tomName);
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
      //DEBUG     + tSymbol);
      try {
        tSymbol = `RepeatId(TopDown(replaceFreshTypeVar(this))).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
            tSymbol);
      }
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
      //DEBUG     + tSymbol);
      getSymbolTable().putSymbol(tomName,tSymbol);
    }
  }

  %strategy replaceFreshTypeVar(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar && Type(typeName,TypeVar(_)) << typeVar -> {
        if (nkt.substitutions.containsKey(`typeVar)) {
          return nkt.substitutions.get(`typeVar);
        } else {
          System.out.println("\n----- There is no mapping for " + `typeVar +'\n');
          return `Type(typeName,EmptyTargetLanguageType());
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
