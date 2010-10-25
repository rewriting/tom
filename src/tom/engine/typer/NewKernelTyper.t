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
import java.util.logging.Logger;

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

  private static Logger logger = Logger.getLogger("tom.engine.typer.NewKernelTyper");

  %typeterm NewKernelTyper {
    implement { NewKernelTyper }
    is_sort(t) { ($t instanceof NewKernelTyper) }
  }

  private int freshTypeVarCounter;
  private int limTVarSymbolTable;

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
  private TypeConstraintList equationConstraints;
  private TypeConstraintList subtypingConstraints;
  // Set of pairs (freshVar,groundVar)
  private HashMap<TomType,TomType> substitutions;
  // Set of supertypes for each type
  private HashMap<TomType,TomTypeList> dependencies = new
    HashMap<TomType,TomTypeList>();

  private SymbolTable symbolTable;

  private String currentInputFileName;
  private boolean lazyType = false;

  protected void setLazyType() {
    lazyType = true;
  }

  protected void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  protected SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected void setCurrentInputFileName(String currentInputFileName) {
    this.currentInputFileName = currentInputFileName;
  }

  protected String getCurrentInputFileName() {
    return currentInputFileName;
  }
  protected TomType getCodomain(TomSymbol tSymbol) {
    return TomBase.getSymbolCodomain(tSymbol);
  }

  protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
    return TomBase.getSymbolFromTerm(tTerm, symbolTable);
   }

  protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
    return TomBase.getSymbolFromTerm(bqTerm,symbolTable);
  }

  protected TomSymbol getSymbolFromName(String tName) {
    return TomBase.getSymbolFromName(tName, symbolTable);
   }

  protected TomSymbol getSymbolFromType(TomType tType) {
    return TomBase.getSymbolFromType(tType,symbolTable); 
  }

  /**
   * The method <code>addSubstitution</code> adds a substitutions (i.e. a pair
   * (type1,type2) where type2 is the substitution for type1) into the
   * global list "substitutions" and saturate it.
   * For example, to add a pair (X,Y) where X is a type variable and Y is a type
   * which can be a type variable or a ground type, we follow two steps:
   * <p>
   * STEP 1:  a) put(X,Z) if (Y,Z) is in substitutions or
   *          b) put(X,Y) otherwise
   * <p>
   * STEP 2:  a) put(W,Z) after step 1.a or put(W,Y) after step 1.b
   *             if there exist (W,X) in substitutions for each (W,X) in substitutions
   *          b) do nothing otherwise
   * @param key   the first argument of the pair to be inserted (i.e. the type1) 
   * @param value the second argument of the pair to be inserted (i.e. the type2) 
   */
  private void addSubstitution(TomType key, TomType value) {
    // STEP 1  
    TomType newValue = value;
    if (substitutions.containsKey(value)) {
      newValue = substitutions.get(value); 
    } 
    substitutions.put(key,newValue);

    // STEP 2
    if (substitutions.containsValue(key)) {
      TomType valueOfCurrentKey;
      for (TomType currentKey : substitutions.keySet()) {
        valueOfCurrentKey = substitutions.get(currentKey);
        if (valueOfCurrentKey == key) {
          substitutions.put(currentKey,newValue);
        }
      }
    }
  }

  /**
   * The method <code>hasUndeclaredType</code> checks if a term has an
   * undeclared type by verifying if a term has type
   * Type(name,EmptyTargetLanguage()), where name is not
   * UNKNOWN_TYPE.
   */
  protected void hasUndeclaredType(String typeName, OptionList oList) {
    String fileName = currentInputFileName;
    int line = 0;
    //DEBUG System.out.println("hasUndeclaredType: subject = " + subject);
    if (typeName != symbolTable.TYPE_UNKNOWN.getTomType()) {
      Option option = TomBase.findOriginTracking(`oList);
      %match(option) {
        OriginTracking(_,line,fileName) -> {
          TomMessage.error(logger,`fileName, `line,
              TomMessage.unknownSymbol,`typeName); 
        }
      }
    }
  }

  /*
  protected void hasUndeclaredType(BQTerm subject) {
    String fileName = currentInputFileName;
    int line = 0;
    //DEBUG System.out.println("hasUndeclaredType: subject = " + subject);
    %match {
      (BQVariable|BQVariableStar)[Options=oList,AstType=aType] << subject && 
        TypeVar(tomType,_) << aType &&
        (tomType != symbolTable.TYPE_UNKNOWN.getTomType()) -> {
          Option option = TomBase.findOriginTracking(`oList);
          %match(option) {
            OriginTracking(_,line,fileName) -> {
              TomMessage.error(logger,`fileName, `line,
                  TomMessage.unknownSymbol,`tomType); 
            }
          }
        }
    }
  }
  */
 
  protected TomType getType(BQTerm bqTerm) {
    %match(bqTerm) {
      (BQVariable|BQVariableStar|FunctionCall)[AstType=aType] -> { return `aType; }
      (BQAppl|BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name[String=name]] -> {
        TomSymbol tSymbol = getSymbolFromName(`name);
        //DEBUG System.out.println("In getType with BQAppl " + `bqTerm + "\n");
        //DEBUG System.out.println("In getType with type " + getCodomain(tSymbol) + "\n");
        return getCodomain(tSymbol);
      }
    } 
    throw new TomRuntimeException("getType(BQTerm): should not be here.");
  }

  protected TomType getType(TomTerm tTerm) {
    %match(tTerm) {
      AntiTerm[TomTerm=atomicTerm] -> { return getType(`atomicTerm); }
      (Variable|VariableStar)[AstType=aType] -> { return `aType; }
      RecordAppl[NameList=concTomName(Name[String=name],_*)] -> {
        TomSymbol tSymbol = getSymbolFromName(`name);
        return getCodomain(tSymbol);
      }
    } 
    throw new TomRuntimeException("getType(TomTerm): should not be here.");
  }

  protected Info getInfoFromTomTerm(TomTerm tTerm) {
    %match(tTerm) {
      AntiTerm[TomTerm=atomicTerm] -> { return getInfoFromTomTerm(`atomicTerm); }
      (Variable|VariableStar)[Options=optionList,AstName=aName] -> { 
        return `PairNameOptions(aName,optionList); 
      }
      RecordAppl[Options=optionList,NameList=concTomName(aName,_*)] ->  { 
        return `PairNameOptions(aName,optionList); 
      }
    } 
    return `PairNameOptions(Name(""),concOption()); 
  }

  protected Info getInfoFromBQTerm(BQTerm bqTerm) {
    %match(bqTerm) {
      (BQVariable|BQVariableStar|BQAppl)[Options=optionList,AstName=aName] -> { 
        return `PairNameOptions(aName,optionList); 
      }
    } 
    return `PairNameOptions(Name(""),concOption()); 
  }

  protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
    limTVarSymbolTable = freshTVarSymbolTable;
  }

  protected int getFreshTlTIndex() {
    return freshTypeVarCounter++;
  }

  protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.TYPE_UNKNOWN;
    %match(tType) {
      Type[TomType=tomType] -> { return `TypeVar(tomType,getFreshTlTIndex()); }
    }
    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
   }

  /*
     * pem: use if(...==... && typeConstraints.contains(...))
     */
  /**
   * The method <code>addEqConstraint</code> adds an equation (i.e. a type constraint) into the
   * global list "TypeConstraints" if this equation does not contains
   * "EmptyTypes". The global list is ordered inserting equations
   * containing (one or both) ground type(s) into the beginning of the list.
   * @param tConstraint the equation to be inserted into the list "TypeConstraints"
   */
  protected TypeConstraintList addEqConstraint(TypeConstraint tConstraint,
      TypeConstraintList tCList) {
    %match {
      !concTypeConstraint(_*,typeConstraint,_*) << tCList &&
        typeConstraint << TypeConstraint tConstraint -> {
          %match {
            Equation[Type1=t1@!EmptyType(),Type2=t2@!EmptyType()] <<
              typeConstraint && (t1 != t2) -> { 
                return `concTypeConstraint(typeConstraint,tCList*);
              }
          }
        }
    }
    return tCList;
  }

  protected TypeConstraintList addSubConstraint(TypeConstraint tConstraint,
      TypeConstraintList tCList) {
    %match {
      !concTypeConstraint(_*,typeConstraint,_*) << tCList &&
        typeConstraint << TypeConstraint tConstraint -> {
          %match {
            Subtype[Type1=t1@!EmptyType(),Type2=t2@!EmptyType()] <<
              typeConstraint && (t1 != t2) -> { 
                return`concTypeConstraint(typeConstraint,tCList*);
                //return closedForm(tConstraint,closedtCList);
              }
          }
        }
    }
    return tCList;
  }
/*
  protected TypeConstraintList closedForm(TypeConstraint tConstraint,
  TypeConstraintList tCList) {
    %match {
      concTypeConstraint(_*,typeConstraint,_*) << tCList &&
        Subtype[Type1=t1,Type2=tVar@TypeVar[],Info=info] << tConstraint &&
        Subtype[Type1=tVar,Type2=t2] << typeConstraint  -> {
          // TODO : eliminate insertion in queue
          return `concTypeConstraint(Subtype(t1,t2,info),tCList*);
        }

      concTypeConstraint(_*,typeConstraint,_*) << tCList &&
        Subtype[Type1=tVar@TypeVar[],Type2=t2,Info=info] << tConstraint &&
        Subtype[Type1=t1,Type2=tVar] << typeConstraint  -> {
          // TODO : eliminate insertion in queue
          return `concTypeConstraint(Subtype(t1,t2,info),tCList*);
        }
    }
  }
*/
  protected void generateDependencies() {
    TomTypeList superTypes;
    TomTypeList supOfSubTypes;
    for(TomType currentType:symbolTable.getUsedTypes()) {
      superTypes = `concTomType();
      //DEBUG System.out.println("In generateDependencies -- for 1 : currentType = " +
      //DEBUG    currentType);
      %match {
        Type[TypeOptions=concTypeOption(_*,SubtypeDecl[TomType=supTypeName],_*)] << currentType -> {
          TomType supType = symbolTable.getType(`supTypeName);
          //DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
          //DEBUG     + `supTypeName + " and supType = " +
          //DEBUG    supType);
          if (dependencies.containsKey(supType)) {
            //DEBUG System.out.println("In generateDependencies -- if : supType = " +
            //DEBUG     supType);
            superTypes = dependencies.get(supType); 
          }
          superTypes = `concTomType(supType,superTypes*);  

          for(TomType subType:dependencies.keySet()) {
            supOfSubTypes = dependencies.get(`subType);
            //DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
            //DEBUG     supOfSubTypes);
            %match {
              concTomType(_*,type,_*) << supOfSubTypes && (type == currentType) -> {
                // Replace list of superTypes of "subType" by a new one
                // containing the superTypes of "currentType" which is also a
                // superType
                dependencies.put(subType,`concTomType(supOfSubTypes*,superTypes*));
              }
            }
          }
        }
      }
      //DEBUG System.out.println("In generateDependencies -- end: superTypes = " +
      //DEBUG     superTypes);
      dependencies.put(`currentType,superTypes);
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
   * <code>equationConstraints</code>, <code>subtypingConstraints</code> and <code>substitutions</code>
   */
  private void init() {
    freshTypeVarCounter = limTVarSymbolTable;
    varPatternList = `concTomTerm();
    varList = `concBQTerm();
    equationConstraints = `concTypeConstraint();
    subtypingConstraints = `concTypeConstraint();
    substitutions = new HashMap<TomType,TomType>();
  }

  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  // TODO: keep only one CollectKnownTypes method (instead of that from NewTyper
  // and this one)
  /**
   * The class <code>CollectKnownTypes</code> is generated from a strategy which
   * initially types all terms by using their correspondent type in symbol table
   * or a fresh type variable :
   * CASE 1 : Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if
   * name is in TypeTable
   * CASE 2 : Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i))
   * if name is not in TypeTable
   * @param nkt an instance of object NewKernelTyper
   */
  %strategy CollectKnownTypes(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      Type[TomType=tomType,TlType=EmptyTargetLanguageType()] -> {
        TomType newType = nkt.symbolTable.getType(`tomType);
        if (newType == null) {
          // This happens when :
          // * tomType != unknown type AND (newType == null)
          // * tomType == unknown type
          newType = `TypeVar(tomType,nkt.getFreshTlTIndex());
        }
        return newType;
      }
    }
  }

  /**
   * The method <code>inferAllTypes</code> is the start-up of the inference
   * process. It is a generic method and it is called for the first time by the
   * NewTyper
   */
  public <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType contextType) {
    try {
      return
        `TopDownStopOnSuccess(inferTypes(contextType,this)).visitLight(term); 
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferAllTypes: failure on " + term);
    }
  }

  /**
   * The class <code>inferTypes</code> is generated from a strategy which
   * tries to infer types of all variables
   * <p> 
   * It starts by searching for a Instruction
   * <code>Match(constraintInstructionList,option)</code> and calling
   * <code>inferConstraintInstructionList</code> in order to apply rule CT-RULE
   * for each single constraintInstruction
   * <p>
   * It searches for variables and star variables (TomTerms and BQTerms) and
   * applies rule CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
   * CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) or a
   * "subject" (a BQTerm) in order to infer its type.
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
   * @param contextType the fresh generated previously and attributed to the term
   * @param nkt an instance of object NewKernelTyper
   */
  %strategy inferTypes(contextType:TomType,nkt:NewKernelTyper) extends Fail() {
    // We put "returns" for each "case" of a visit in order to interrupt the
    // flow of the strategy.
    // e.g. for "f(g(x))" the strategy will be applied over "x" three times (1
    // when visiting "f(g(x))" + 1 when visiting "g(x)" + 1 when visiting "x")
    
    visit Code {
      code@(Tom|TomInclude)[CodeList=cList] -> {
        nkt.generateDependencies();
        System.out.println("Dependencies: " + nkt.dependencies);
        //DEBUG System.out.println("Code with term = " + `code + " and contextType = " +
        //DEBUG     contextType);
        CodeList newCList = nkt.inferCodeList(`cList);
        return `code.setCodeList(newCList);
      }
    }

    visit Instruction {
      Match[ConstraintInstructionList=ciList,Options=optionList] -> {
        BQTermList BQTList = nkt.varList;
        ConstraintInstructionList newCIList =
          nkt.inferConstraintInstructionList(`ciList);
        nkt.varList = BQTList;
        return `Match(newCIList,optionList);
      }      
    }
    
    visit TomVisit {
      VisitTerm[VNode=vNode,AstConstraintInstructionList=ciList,Options=optionList] -> {
        BQTermList BQTList = nkt.varList;
        ConstraintInstructionList newCIList =
          nkt.inferConstraintInstructionList(`ciList);
        nkt.varList = BQTList;
        return `VisitTerm(vNode,newCIList,optionList);
      }
    }
   
    visit TomTerm {
      AntiTerm[TomTerm=atomicTerm] -> { nkt.inferAllTypes(`atomicTerm,contextType); }

      var@(Variable|VariableStar)[Options=optionList,AstName=aName,AstType=aType,Constraints=cList] -> {
        //DEBUG System.out.println("InferTypes:TomTerm var = " + `var);
        nkt.checkNonLinearityOfVariables(`var);
        TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
        nkt.subtypingConstraints =
          nkt.addSubConstraint(`Subtype(aType,contextType,PairNameOptions(aName,optionList)),newSubConstraints);  
        //DEBUG System.out.println("InferTypes:TomTerm var -- constraint = " +
        //DEBUG `aType + " = " + contextType);
        ConstraintList newCList = `cList;
        %match(cList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(boundTerm)) -> {
            //DEBUG System.out.println("InferTypes:TomTerm aliasvar -- constraint = " +
            //DEBUG   nkt.getType(`boundTerm) + " = " + `contextType);
            //nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
            TypeConstraintList newEqConstraints = nkt.equationConstraints;
            nkt.equationConstraints =
              nkt.addEqConstraint(`Equation(nkt.getType(boundTerm),aType,nkt.getInfoFromTomTerm(boundTerm)),newEqConstraints); 
          }
        }
        return `var.setConstraints(newCList);
      }

      RecordAppl[Options=optionList,NameList=nList@concTomName(aName@Name(tomName),_*),Slots=sList,Constraints=cList] -> {
        // In case of a String, tomName is "" for ("a","b")
        TomSymbol tSymbol = nkt.getSymbolFromName(`tomName);
        // IF_1
        if (tSymbol == null) {
          //The contextType is used here, so it must be a ground type, not a
          //type variable
          tSymbol = nkt.getSymbolFromType(contextType);

          // IF_2
          if (tSymbol != null) {
            // In case of contextType is "TypeVar(name,i)"
            `nList = `concTomName(tSymbol.getAstName());
          } 
        }
        //DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. tSymbol = " + `tSymbol);
        //DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. astName = " +`concTomName(tSymbol.getAstName()));
        //DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl.
        //tSymbol = " + tSymbol);

        TomType codomain = contextType;

        // IF_3
        if (tSymbol == null) {
          //DEBUG System.out.println("tSymbol is still null!");
          tSymbol = `EmptySymbol();
        } else {
          // This code can not be moved to IF_2 because tSymbol may don't be
          // "null" since the begginning and then does not enter into neither IF_1 nor
          // IF_2
          codomain = nkt.getCodomain(tSymbol);
          //DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. codomain = " + codomain);
          TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
          nkt.subtypingConstraints = nkt.addSubConstraint(`Subtype(codomain,contextType,PairNameOptions(aName,optionList)),newSubConstraints);
          //DEBUG System.out.println("InferTypes:TomTerm recordappl -- constraint" + codomain + " = " + contextType);
        }

        ConstraintList newCList = `cList;
        %match(cList) {
          // How many "AliasTo" constructors can concConstraint have?
          concConstraint(AliasTo(boundTerm)) -> {
            //DEBUG System.out.println("InferTypes:TomTerm aliasrecordappl -- constraint = " +
            //DEBUG     nkt.getType(`boundTerm) + " = " + contextType);
            //nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
            TypeConstraintList newEqConstraints = nkt.equationConstraints;
            nkt.equationConstraints =
              nkt.addEqConstraint(`Equation(nkt.getType(boundTerm),codomain,nkt.getInfoFromTomTerm(boundTerm)),newEqConstraints); 
          }
        }

        SlotList newSList = `concSlot();
        if (!`sList.isEmptyconcSlot()) {
          // TODO : verify if we pass codomain or contextType
          `newSList =
            nkt.inferSlotList(`sList,tSymbol,codomain);
        }
        return `RecordAppl(optionList,nList,newSList,newCList);
      }
    }

    visit BQTerm {
      bqVar@(BQVariable|BQVariableStar)[Options=optionList,AstName=aName,AstType=aType] -> {
        //DEBUG System.out.println("InferTypes:BQTerm bqVar -- contextType = " +
        //DEBUG     contextType);
        nkt.checkNonLinearityOfBQVariables(`bqVar);
        TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
          nkt.subtypingConstraints = nkt.addSubConstraint(`Subtype(aType,contextType,PairNameOptions(aName,optionList)),newSubConstraints);  
        //DEBUG System.out.println("InferTypes:BQTerm bqVar -- constraint = " +
        //DEBUG `aType + " = " + contextType);
        return `bqVar;
      }

      BQAppl[Options=optionList,AstName=aName@Name(name),Args=bqTList] -> {
        //DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. tomName = " + `name);
        TomSymbol tSymbol = nkt.getSymbolFromName(`name);
        if (tSymbol == null) {
          //The contextType is used here, so it must be a ground type, not a
          //type variable
          //DEBUG System.out.println("visit contextType = " + contextType);
          tSymbol = nkt.getSymbolFromType(contextType);
          if (tSymbol != null && `name.equals("")) {
            // In case of contextType is "TypeVar(name,i)"
            `aName = tSymbol.getAstName();
          }
        }

        TomType codomain = contextType;
        if (tSymbol == null) {
          tSymbol = `EmptySymbol();
        } else {
          codomain = nkt.getCodomain(tSymbol);
          TypeConstraintList newSubConstraints = nkt.subtypingConstraints;
          nkt.subtypingConstraints = nkt.addSubConstraint(`Subtype(codomain,contextType,PairNameOptions(aName,optionList)),newSubConstraints);
          //DEBUG System.out.println("InferTypes:BQTerm bqappl -- constraint = "
          //DEBUG + `codomain + " = " + contextType);
        }
        
        BQTermList newBQTList = `bqTList;
        if (!`bqTList.isEmptyconcBQTerm()) {
          //DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. bqTList = " + `bqTList);
          // TODO : verify if we pass codomain or contextType
          newBQTList =
            nkt.inferBQTermList(`bqTList,`tSymbol,codomain);
        }
      
        // TO VERIFY
        %match(tSymbol) {
          EmptySymbol() -> {
            return `FunctionCall(aName,contextType,newBQTList); 
          }
        }
        return `BQAppl(optionList,aName,newBQTList);
      }
    }
  }
    
  /**
   * The method <code>checkNonLinearityOfVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "TomTerm" that already exists in varPatternList or in varList, a type
   * constraint is added to <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the varList since a Variable/VariableStar can have
   * occurred in a previous condition as a BQVariable/BQVariableStar, in the
   * case of a composed condition
   * e.g. (x < 10 ) && f(x) << e -> { action }
   * @param var the variable to have the linearity checked
   */
  private void checkNonLinearityOfVariables(TomTerm var) {
    %match {
      // If the variable already exists in varPatternList or in varList (in
      // the case of a bad order of the conditions of a
      // conjunction/disjunction
      (Variable|VariableStar)[Options=optionList,AstName=aName,AstType=aType1] << var &&
        (concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*)
         << varPatternList ||
         concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*)
         << varList) -> {
          TypeConstraintList newEqConstraints = equationConstraints;
          equationConstraints =
            addEqConstraint(`Equation(aType1,aType2,PairNameOptions(aName,optionList)),newEqConstraints);}
    }
  }

  /**
   * The method <code>checkNonLinearityOfBQVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "BQTerm" that already exists in varPatternList or in varList, a type
   * constraint is added to <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the varPatternList since a BQVariable/BQVariableStar can have
   * occurred in a previous condition as a Variable/VariableStar, in the
   * case of a composed condition or of a inner match
   * e.g. f(x) << e && (x < 10 ) -> { action } 
   * @param bqvar the variable to have the linearity checked
   */
  private void checkNonLinearityOfBQVariables(BQTerm bqvar) {
    %match {
      // If the backquote variable already exists in varPatternList 
      // (in the case of a inner match) or in varList
      (BQVariable|BQVariableStar)[Options=optionList,AstName=aName,AstType=aType1] << bqvar &&
        (concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*)
         << varList ||
         concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*)
         << varPatternList) -> {
          TypeConstraintList newEqConstraints = equationConstraints;
          equationConstraints =
            addEqConstraint(`Equation(aType1,aType2,PairNameOptions(aName,optionList)),newEqConstraints); }
    }
  }

  /**
   * The method <code>inferCodeList</code> starts inference process which takes one
   * code at a time
   * <ul>
   *  <li> all lists and hashMaps are reset
   *  <li> each code is typed with fresh type variables
   *  <li> each code is traversed in order to generate type constraints
   *  <li> the type constraints of "equationConstraints" and
   *        "subtypingConstraints" lists are solved at the end
   *        of the current code generating a mapping (a set of
   *        substitutions for each type variable)
   *  <li> the mapping is applied over the code and the symbol table
   * </ul>
   * @param cList the tom code list to be type inferred
   * @return      the tom typed code list
   */
  private CodeList inferCodeList(CodeList cList) {
    CodeList newCList = `concCode();
    for (Code code : cList.getCollectionconcCode()) {
      init();
      code =  collectKnownTypesFromCode(`code);
      int limInputTypeVar = freshTypeVarCounter;
      System.out.println("------------- Code typed with typeVar:\n code = " +
          `code);
      code = inferAllTypes(code,`EmptyType());
      //DEBUG printGeneratedConstraints(subtypingConstraints);
      solveConstraints(limInputTypeVar);
      System.out.println("limInputTypeVar = " + limInputTypeVar);
      System.out.println("substitutions = " + substitutions);
      code = replaceInCode(code);
      System.out.println("------------- Code typed with substitutions:\n code = " +
          `code);
      replaceInSymbolTable();
      newCList = `concCode(code,newCList*);
    }
    return newCList.reverse();
  }

  /**
   * The method <code>inferConstraintInstructionList</code> applies rule CT-RULE
   * to a pair "condition -> action" in order to collect all variables occurring
   * in the condition and put them into <code>varPatternList</code> (for those
   * variables occurring in match constraints) and <code>varList</code> (for
   * those variables occurring in numeric constraints) to be able to handle
   * non-linearity.
   * <p>
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
  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
    ConstraintInstructionList newCIList = `concConstraintInstruction();
    for (ConstraintInstruction cInst :
        ciList.getCollectionconcConstraintInstruction()) {
      try {
        %match(cInst) {
          ConstraintInstruction(constraint,action,optionList) -> {
            TomList TTList = varPatternList;
            `TopDownCollect(CollectVars(this)).visitLight(`constraint);
            Constraint newConstraint = inferConstraint(`constraint);
            //DEBUG System.out.println("inferConstraintInstructionList: action " +
            //DEBUG     `action);
            Instruction newAction = `inferAllTypes(action,EmptyType());
            varPatternList = TTList;
            newCIList =
              `concConstraintInstruction(ConstraintInstruction(newConstraint,newAction,optionList),newCIList*);
          } 
        }
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("inferConstraintInstructionList: failure on " + `cInst);
      }
    }
    return newCIList.reverse();
  }

  /**
   * The class <code>CollectVars</code> is generated from a strategy which
   * collect all variables (Variable, VariableStar, BQVariable, BQVariableStar
   * occurring in a condition.
   * @param nkt an instance of object NewKernelTyper
   */
  %strategy CollectVars(nkt:NewKernelTyper) extends Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[] -> { nkt.addTomTerm(`var); }
    }

    visit BQTerm {
      bqvar@(BQVariable|BQVariableStar)[] -> { nkt.addBQTerm(`bqvar); }
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
  private Constraint inferConstraint(Constraint constraint) {
    %match(constraint) {
      MatchConstraint[Pattern=pattern,Subject=subject,AstType=aType] -> { 
        //DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
        TomType tPattern = getType(`pattern);
        TomType tSubject = getType(`subject);
        if (tPattern == null || tPattern == `EmptyType()) {
          tPattern = getUnknownFreshTypeVar();
        }
        if (tSubject == null || tSubject == `EmptyType()) {
          tSubject = getUnknownFreshTypeVar();
        }
        //DEBUG System.out.println("inferConstraint: match -- constraint " +
        //DEBUG     tPattern + " = " + tSubject);
        %match(aType) {
          TypeVar[TomType=typeName] -> {
            `hasUndeclaredType(typeName,getInfoFromTomTerm(pattern).getOptions()); 
            /* There is no explicit type, so T_pattern = T_subject */
            // TODO verify if we replace it by same code of case "Type[]"
            TypeConstraintList newEqConstraints = equationConstraints;
            equationConstraints =
              addEqConstraint(`Equation(tPattern,tSubject,getInfoFromTomTerm(pattern)),newEqConstraints);
          }
          Type[] -> {
            /* T_pattern = T_cast and T_cast <: T_subject */
            TypeConstraintList newEqConstraints = equationConstraints;
            TypeConstraintList newSubConstraints = subtypingConstraints;
            equationConstraints =
              addEqConstraint(`Equation(tPattern,aType,getInfoFromTomTerm(pattern)),newEqConstraints);
            subtypingConstraints = addSubConstraint(`Subtype(aType,tSubject,getInfoFromBQTerm(subject)),newSubConstraints);
          }
        }
        TomTerm newPattern = `inferAllTypes(pattern,tPattern);
        BQTerm newSubject = `inferAllTypes(subject,tSubject);
        //hasUndeclaredType(newSubject);
        return `MatchConstraint(newPattern,newSubject,aType);
      }

      NumericConstraint(left,right,kind) -> {
        TomType tLeft = getType(`left);
        TomType tRight = getType(`right);
        if (tLeft == null || tLeft == `EmptyType()) {
          tLeft = getUnknownFreshTypeVar();
        }
        if (tRight == null || tRight == `EmptyType()) {
          tRight = getUnknownFreshTypeVar();
        }
        //DEBUG System.out.println("inferConstraint: match -- constraint " +
        //DEBUG     tLeft + " = " + tRight);

        // To represent the relationshipo between both argument types
        TomType lowerType = getUnknownFreshTypeVar();
        TypeConstraintList newSubConstraints = subtypingConstraints;
        newSubConstraints =
          addSubConstraint(`Subtype(lowerType,tLeft,getInfoFromBQTerm(left)),newSubConstraints);
        subtypingConstraints =
          addSubConstraint(`Subtype(lowerType,tRight,getInfoFromBQTerm(right)),newSubConstraints);
        BQTerm newLeft = inferAllTypes(`left,tLeft);
        BQTerm newRight = inferAllTypes(`right,tRight);
        //hasUndeclaredType(newLeft);
        //hasUndeclaredType(newRight);
        return `NumericConstraint(newLeft,newRight,kind);
      }

      AndConstraint(headCList,tailCList*) -> {
        ConstraintList cList = `concConstraint(headCList,tailCList);
        Constraint newAConstraint = `AndConstraint();
        for (Constraint cArg : cList.getCollectionconcConstraint()) {
          cArg = inferConstraint(cArg);
          newAConstraint = `AndConstraint(newAConstraint,cArg);
        }
        return newAConstraint;
      }

      OrConstraint(headCList,tailCList*) -> {
        ConstraintList cList = `concConstraint(headCList,tailCList);
        Constraint newOConstraint = `OrConstraint();
        for (Constraint cArg : cList.getCollectionconcConstraint()) {
          cArg = inferConstraint(cArg);
          newOConstraint = `OrConstraint(newOConstraint,cArg);
        }
        return newOConstraint;
      }
    }
    return constraint;
  }

  /**
   * The method <code>inferSlotList</code> infers types of the arguments of
   * lists and functions (which are TomTerms) 
   * <p> 
   * It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
   * to each argument in order to infer its type.
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "A = T" for the last
   *      argument, where "A" is a fresh type variable  and
   *      "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and adds a type constraint "Ai =
   *      Ti" for each argument, where "Ai" is a fresh type variable
   * <p>
   * @param sList a list of arguments of a list/function
   * @param tSymbol the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   */
  private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
      contextType) {
    TomType argType = contextType;
    SlotList newSList = `concSlot();
    %match(tSymbol) {
      EmptySymbol() -> {
        TomName argName;
        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argName = slot.getSlotName();
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            %match(argTerm) {
              !VariableStar[] -> { 
                //DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
                argType = getUnknownFreshTypeVar();
              }
            }
          }
          argTerm = `inferAllTypes(argTerm,argType);
          newSList = `concSlot(PairSlotAppl(argName,argTerm),newSList*);
        }
        return newSList.reverse(); 
      }

      Symbol[AstName=symName,TypesToType=TypesToType(concTomType(headTTList,_*),Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain])] -> {
        TomTerm argTerm;
        if(TomBase.isListOperator(`tSymbol) || TomBase.isArrayOperator(`tSymbol)) {
          TomSymbol argSymb;
          for (Slot slot : sList.getCollectionconcSlot()) {
            argTerm = slot.getAppl();
            argSymb = getSymbolFromTerm(argTerm);
            if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
              %match(argTerm) {
                VariableStar[] -> {
                  TypeOptionList newTOptions = `tOptions;
                  // Is this test really necessary?
                  %match {
                    concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) <<
                      tOptions && (rsName != symName) -> {
                        throw new TomRuntimeException("typeVariableList: symbol '"
                            + `tSymbol+ "' with more than one constructor (rootsymbolname)");
                      }
                    concTypeOption(_*,!WithSymbol[],_*) << tOptions -> {
                      newTOptions =
                        `concTypeOption(WithSymbol(symName),tOptions*);
                    }
                  }

                  // Case CT-STAR rule (applying to premises):
                  argType = `Type(newTOptions,tomCodomain,tlCodomain);
                }

                !VariableStar[] -> { 
                  // Case CT-ELEM rule (applying to premises which are not lists)
                  //argType = getUnknownFreshTypeVar();
                  argType = `headTTList;
                  //DEBUG System.out.println("inferSlotList: !VariableStar -- constraint "
                  //DEBUG     + `headTTList + " = " + argType);
                  //addConstraint(`Equation(argType,headTTList,getInfoFromTomTerm(argTerm)));
                }
              }
            } else if (`symName != argSymb.getAstName()) {
              // TODO: improve this code! It is like CT-ELEM
              /*
               * Case CT-ELEM rule which premise is a list
               * A list with a sublist whose constructor is different
               * e.g. 
               * A = ListA(A*) and B = ListB(A*) | b()
               * ListB(ListA(a()))
               */
              //argType = getUnknownFreshTypeVar();
              argType = `headTTList;
              //DEBUG System.out.println("inferSlotList: symName != argSymbName -- constraint "
              //DEBUG     + `headTTList + " = " + argType);
              //addConstraint(`Equation(argType,headTTList,getInfoFromTomTerm(argTerm)));
            }

            // Case CT-MERGE rule (applying to premises):
            argTerm = `inferAllTypes(argTerm,argType);
            newSList = `concSlot(PairSlotAppl(slot.getSlotName(),argTerm),newSList*);
          }
        } else {
          // In case of a function
          // Case CT-FUN rule (applying to premises):

          TomName argName;
          for (Slot slot : sList.getCollectionconcSlot()) {
            argName = slot.getSlotName();
            argType = TomBase.getSlotType(tSymbol,argName);
            argTerm = `inferAllTypes(slot.getAppl(),argType);
            newSList = `concSlot(PairSlotAppl(argName,argTerm),newSList*);
            //DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
          }
        }
        return newSList.reverse(); 
      }
    }
    throw new TomRuntimeException("inferSlotList: failure on " + `sList);
  }

  /**
   * The method <code>inferBQTermList</code> infers types of the arguments of
   * lists, functions and calls of methods (which are BQTerms) 
   * <p> 
   * It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
   * to each argument in order to infer its type.
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "A = T" for the last
   *      argument, where "A" is a fresh type variable  and
   *      "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):AA" and "l:T*->TT" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and adds a type constraint "Ai =
   *      Ti" for each argument, where "Ai" is a fresh type variable
   * <p>
   * @param bqList a list of arguments of a list/function/method
   * @param tSymbol the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   */
  private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
      contextType) {
    TomType argType = contextType;
    BQTermList newBQTList = `concBQTerm();
    %match(tSymbol) {
      EmptySymbol() -> {
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          argTerm = `inferAllTypes(argTerm,EmptyType());
          newBQTList = `concBQTerm(argTerm,newBQTList*);
        }
        return newBQTList.reverse(); 
      }

      Symbol[AstName=symName,TypesToType=TypesToType(domain@concTomType(headTTList,_*),Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain]),PairNameDeclList=pNDList,Options=oList] -> {
        TomTypeList symDomain = `domain;
        TomSymbol argSymb;
        if(TomBase.isListOperator(`tSymbol) || TomBase.isArrayOperator(`tSymbol)) {
          for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
            argSymb = getSymbolFromTerm(argTerm);
            if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
              %match(argTerm) {
                Composite(_*) -> {
                  // We don't know what is into the Composite
                  // It can be a BQVariableStar or a list operator or a list of
                  // CompositeBQTerm or something else
                  argType = `EmptyType();
                }

                BQVariableStar[] -> {
                  TypeOptionList newTOptions = `tOptions;
                  // Is this test really necessary?
                  %match {
                    concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) <<
                      tOptions && (rsName != symName) -> {
                        throw new TomRuntimeException("typeVariableList: symbol '"
                            + `tSymbol+ "' with more than one constructor (rootsymbolname)");
                      }
                    concTypeOption(_*,!WithSymbol[],_*) << tOptions -> {
                      newTOptions =
                        `concTypeOption(WithSymbol(symName),tOptions*);
                    }
                  }



                  // Case CT-STAR rule (applying to premises):
                  argType = `Type(newTOptions,tomCodomain,tlCodomain);
                }

                //TO VERIFY : which constructors must be tested here?
                (BQVariable|BQAppl)[] -> {
                  // Case CT-ELEM rule (applying to premises which are not lists)
                  //argType = getUnknownFreshTypeVar();
                  argType = `headTTList;
                  //DEBUG System.out.println("inferBQTermList: !BQVariableStar -- constraint "
                  //DEBUG     + argType + " = " + `headTTList);
                  //addConstraint(`Equation(argType,headTTList,getInfoFromBQTerm(argTerm)));
                }
              }
            } else if (`symName != argSymb.getAstName()) {
              // TODO: improve this code! It is like CT-ELEM
              /*
               * Case CT-ELEM rule which premise is a list
               * A list with a sublist whose constructor is different
               * e.g. 
               * A = ListA(A*) and B = ListB(A*) | b()
               * ListB(ListA(a()))
               */
              //argType = getUnknownFreshTypeVar();
              argType = `headTTList;
              //DEBUG System.out.println("inferSlotList: symName != argSymbName -- constraint "
              //DEBUG     + `headTTList + " = " + argType);
              //addConstraint(`Equation(argType,headTTList,getInfoFromBQTerm(argTerm)));
            }

            // Case CT-MERGE rule (applying to premises):
            argTerm = `inferAllTypes(argTerm,argType);
            newBQTList = `concBQTerm(argTerm,newBQTList*);
          }
        } else {
          // In case of a function
          // Case CT-FUN rule (applying to premises):
          if(`pNDList.length() != bqTList.length()) {
            Option option = TomBase.findOriginTracking(`oList);
            %match(option) {
              OriginTracking(_,line,fileName) -> {
                TomMessage.error(logger,`fileName, `line,
                    TomMessage.symbolNumberArgument,`symName.getString(),`pNDList.length(),bqTList.length());
              }
            }
          } else {
            for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
              argType = symDomain.getHeadconcTomType();
              %match(argTerm) {
                /*
                 * We don't know what is into the Composite
                 * If it is a "composed" Composite with more than one element and
                 * representing the argument of a BQAppl "f", so we can not give the same
                 * type of the domain of "f" for all elements of the Composite
                 * e.g.:
                 *    `b(n.getvalue()) is represented by
                 * BQAppl(
                 *    concOption(...),
                 *    Name("b"),
                 *    concBQTerm(
                 *      Composite(
                 *        CompositeBQTerm(BQVariable(concOption(...),Name("n"),TypeVar("unknown type",0))),
                 *        CompositeTL(ITL(".")),
                 *        CompositeBQTerm(Composite(CompositeBQTerm(BQAppl(concOption(...),Name("getvalue"),concBQTerm())))))))
                 */
                Composite(_*,_,_*,_,_*) -> { argType = `EmptyType(); }
              }
              argTerm = `inferAllTypes(argTerm,argType);
              newBQTList = `concBQTerm(argTerm,newBQTList*);
              symDomain = symDomain.getTailconcTomType();
              //DEBUG System.out.println("InferBQTermList CT-FUN -- end of for with bqappl = " + `argTerm);
            }
          }
        }
        return newBQTList.reverse(); 
      }
    }
    throw new TomRuntimeException("inferBQTermList: failure on " + `bqTList);
  }

  private void solveConstraints(int limInputTypeVar) {
    try {
      //DEBUG System.out.println("\nsolveConstraints 1:");
      //DEBUG printGeneratedConstraints(equationConstraints);
      //DEBUG printGeneratedConstraints(subtypingConstraints);
      solveEquationConstraints(equationConstraints);
      TypeConstraintList simplifiedConstraints =
        replaceInSubtypingConstraints(subtypingConstraints);
      //DEBUG System.out.println("\nsolveConstraints 2:");
      //System.out.println("limTVarSymbolTable = " + limTVarSymbolTable);
      printGeneratedConstraints(simplifiedConstraints);
      simplifiedConstraints = 
        `RepeatId(solveSubtypingConstraints(this,limInputTypeVar)).visitLight(simplifiedConstraints);
      //DEBUG System.out.println("\nsolveConstraints 3:");
      //DEBUG printGeneratedConstraints(simplifiedConstraints);
      
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("solveConstraints: failure on " +
          subtypingConstraints);
    }
  }


  /**
   * The method <code>solveEquationConstraints</code> tries to solve all type
   * constraints collected during the inference
   * <p> 
   * There exists 3 kinds of types : variable types Ai, ground types Ti and
   * ground types Ti^c which are decorated with a given symbol c. Since a type
   * constraints is a pair (type1,type2) representing an equation relation
   * between two types, them the set of all possibilities of arrangement between
   * types is a sequence with repetition. Then, we have 9 possible cases (since
   * 3^2 = 9).
   * <p>
   * CASE 1: tCList = {(T1 = T2),...)} and Map
   *  --> detectFail(T1 = T2) to verify if T1 is equals to T2 
   * <p>
   * CASE 2: tCList = {(T1 = T2^c),...)} and Map
   *  --> detectFail(T1 = T2^c) to verify if T1 is equals to T2 
   * <p>
   * CASE 3: tCList = {(T1^c = T2),...)} and Map
   *  --> detectFail(T1^c = T2) to verify if T1 is equals to T2 
   * <p>
   * CASE 4: tCList = {(T1^a = T2^b),...)} and Map
   *  --> detectFail(T1^a = T2^b) to verify if T1^a is equals to T2^b 
   * <p>
   * CASE 5: tCList = {(T1 = A1),...)} and Map 
   *  a) Map(A1) does not exist   --> (A,T1) U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> (A2,T1) U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 6: tCList = {(T1^c = A1),...)} and Map 
   *  a) Map(A1) does not exist   --> (A1,T1^c) U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> (A2,T1^c) U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 7: tCList = {(A1 = T1),...)} and Map 
   *  a) Map(A1) does not exist   --> (A1,T1) U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> (A2,A1) U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 8: tCList = {(A1 = T1^c),...)} and Map 
   *  a) Map(A1) does not exist   --> (A1,T1^c) U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> (A2,T1^c) U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 9: tCList = {(A1 = A2),...)} and Map
   *  a) Map(A1) = T1 (or T1^a) and
   *    i)    Map(A2) does not exist    --> (A2,T1) U Map (or (A2,T1^a) U Map) 
   *    ii)   Map(A2) = T2 (or T2^b)    --> detectFail(T1 = T2) (or detectFail(T1^a = T2^b))
   *    iii)  Map(A2) = A3              --> (A3,T1) U Map (or (A3,T1^a) U Map), since Map is saturated and
   *                                        then Map(A3) does not exist 
   *
   *  b) Map(A1) = A3 and
   *    i)    Map(A2) does not exist    --> (A2,A3) U Map 
   *    ii)   Map(A2) = T2 (or T2^b)    --> (A3,T2) U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *    iii)  Map(A2) = A4              --> (A3,A4) U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *  c) Map(A1) does not exist and
   *    i)    Map(A2) does not exist    --> (A1,A2) U Map
   *    ii)   Map(A2) = T1 (or T1^a)    --> (A1,T1) U Map (or (A1,T1^a) U Map)
   *    iii)  Map(A2) = A3              --> (A1,A3) U Map 
   */
  private TypeConstraintList solveEquationConstraints(TypeConstraintList tCList) {
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
      {
        %match {
          // CASES 1, 2, 3 and 4 :
          eConstraint@Equation[Type1=groundType1@!TypeVar[],Type1=groundType2@!TypeVar[]] <<
            tConstraint && (groundType1 != groundType2) -> {
              `detectFail(eConstraint);
              break matchBlockAdd;
            }
          // CASES 5 and 6 :
          Equation[Type1=groundType@!TypeVar[],Type2=typeVar@TypeVar[],Info=info] <<
            tConstraint -> {
              if (substitutions.containsKey(`typeVar)) {
                TomType mapTypeVar = substitutions.get(`typeVar);
                if (!isTypeVar(mapTypeVar)) {
                  `detectFail(Equation(groundType,mapTypeVar,info));
                } else {
                // if (isTypeVar(mapTypeVar))
                addSubstitution(mapTypeVar,`groundType);
                }
              } else {
                addSubstitution(`typeVar,`groundType);
              }
              break matchBlockAdd;
            }

          // CASES 7 and 8 :
          Equation[Type1=typeVar@TypeVar[],Type2=groundType@!TypeVar[],Info=info] << tConstraint -> {
            if (substitutions.containsKey(`typeVar)) {
              TomType mapTypeVar = substitutions.get(`typeVar);
              if (!isTypeVar(mapTypeVar)) {
                `detectFail(Equation(mapTypeVar,groundType,info));
              } else {
                // if (isTypeVar(mapTypeVar))
                addSubstitution(mapTypeVar,`groundType);
              }
            } else {
              addSubstitution(`typeVar,`groundType);
            }
            break matchBlockAdd;
          }

          // CASE 9 :
          Equation[Type1=typeVar1@TypeVar[],Type2=typeVar2@TypeVar[],Info=info] << tConstraint
            && (typeVar1 != typeVar2) -> {
              TomType mapTypeVar1;
              TomType mapTypeVar2;
              if (substitutions.containsKey(`typeVar1) && substitutions.containsKey(`typeVar2)) {
                mapTypeVar1 = substitutions.get(`typeVar1);
                mapTypeVar2 = substitutions.get(`typeVar2);
                if (isTypeVar(mapTypeVar1)) {
                  addSubstitution(mapTypeVar1,mapTypeVar2);
                } else {
                  if (isTypeVar(mapTypeVar2)) {
                    addSubstitution(mapTypeVar2,mapTypeVar1);
                  } else {
                    `detectFail(Equation(mapTypeVar1,mapTypeVar2,info));
                  }
                }
                break matchBlockAdd;
              } else if (substitutions.containsKey(`typeVar1)) {
                mapTypeVar1 = substitutions.get(`typeVar1);
                addSubstitution(`typeVar2,mapTypeVar1);
                break matchBlockAdd;
              } else if (substitutions.containsKey(`typeVar2)){
                mapTypeVar2 = substitutions.get(`typeVar2);
                addSubstitution(`typeVar1,mapTypeVar2);
                break matchBlockAdd;
              } else {
                addSubstitution(`typeVar1,`typeVar2);
                break matchBlockAdd;
              }
            }
        }
      }
    }
    return tCList;
  }

  /**
   * The method <code>detectFail</code> 
   * tries to solve all type constraints collected during the inference
   * <p> 
   * There exists 3 kinds of types : variable types Ai, ground types Ti and
   * ground types Ti^c which are decorated with a given symbol c. Since a type
   * constraints is a pair (type1,type2) representing an equation relation
   * between two types, them the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: tCList = {(T1 = T2),...)} and Map
   *  a) --> Fail if T1 is different from T2
   *  b) --> Nothing if T1 is equals to T2
   * <p>
   * CASE 2: tCList = {(T1 = T2^c),...)} and Map
   *  a) --> Fail if T1 is different from T2
   *  b) --> Nothing if T1 is equals to T2
   * <p>
   * CASE 3: tCList = {(T1^c = T2),...)} and Map
   *  a) --> Fail if T1 is different from T2
   *  b) --> Nothing if T1 is equals to T2
   * <p>
   * CASE 4: tCList = {(T1^a = T2^b),...)} and Map
   *  a) --> Fail if T1 is different from T2 and/or "a" is different from "b"
   *  b) --> Nothing if T1 is equals to T2
   * <p>
   * @param tConstraint the type constraint to be verified 
   */
  private void detectFail(TypeConstraint tConstraint) {
matchBlockFail : 
    {
      %match {
        // CASE 1a, 2a and 3a :
        Equation(Type[TomType=tName1],Type[TomType=tName2@!tName1],_)
          << tConstraint && (tName1 != "unknown type") && (tName2 != "unknown type")  -> {
            //DEBUG System.out.println("In solveConstraints 1a/3a -- tConstraint  = " + `tConstraint);
            printError(`tConstraint);
            break matchBlockFail;
          }

        // CASE 4a :  
        Equation(Type[TypeOptions=tOptions1,TomType=tName1],Type[TypeOptions=tOptions2@!tOptions1,TomType=tName1],_)
          << tConstraint
          && concTypeOption(_*,WithSymbol[RootSymbolName=rsName1],_*) << tOptions1
          && concTypeOption(_*,WithSymbol[RootSymbolName=rsName2@rsName1],_*) << tOptions2 -> {
            //DEBUG System.out.println("In solveConstraints 4a -- tConstraint  = " + `tConstraint);
            printError(`tConstraint);
            break matchBlockFail;
          }
        // TODO: add treatment to Subtype[]
      }
    }
  }

  private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList
      tCList) {
    TypeConstraintList replacedtCList = `concTypeConstraint();
    TomType mapT1;
    TomType mapT2;
    for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {
      %match(tConstraint) {
        Subtype[Type1=t1,Type2=t2,Info=info] -> {
          mapT1 = substitutions.get(`t1);
          mapT2 = substitutions.get(`t2); 
          if (mapT1 == null) {
            mapT1 = `t1;
          }
          if (mapT2 == null) {
            mapT2 = `t2;
          }
          replacedtCList =
            `concTypeConstraint(Subtype(mapT1,mapT2,info),replacedtCList*);
        }
      }

    }
    return replacedtCList;
  }

  /**
   * The method <code>solveSubtypingConstraints</code> is generated by a
   * strategy which simplifies subtyping constraints replacing them by equations
   * or detecting type inconsistency.
   * <p>
   * PHASE 1: Simplification in equations: 
   * tCList = {T1 <: T2, T2 <: T1} U tCList' and Map -->  {T1 = T2} U tCList' and Map
   * tCList = {A1 <: A2, A2 <: A1} U tCList' and Map -->  {A1 = A2} U tCList' and Map
   * <p>
   * PHASE 2: Reduction in closed form:
   * tCList = {T1 <: A,A <: T2} U tCList' and Map --> {T1 <: T2} U tCList and Map
   * tCList = {A1 <:A,A <: A2} U tCList' and Map --> {A1 <: A2} U tCList and Map
   * <p>
   * PHASE 3: Garbage collection:
   * tCList = {T1 <: T2} U tCList' and Map --> detectFail(T1 <: T2)
   * tCList = {A <: T} U tCList' and Map
   *   --> {A = T} U tCList' and Map if A is not in Var(tCList')
   * tCList = {T <: A} U tCList' and Map
   *   --> {A = T} U tCList' and Map if A is not in Var(tCList')
   * <p>
   * PHASE 4: Reduction in canonical form:
   * tCList = {A <: T1,A <:T2} U tCList' and Map 
   *   --> {A <: lowerType(T1,T2)} U tCList' and Map
   * tCList = {T1 <: A,T2 <: A} U tCList' and Map
   *   --> {upperType(T1,T2) <: A} U tCList' and Map
   */
  %strategy solveSubtypingConstraints(nkt:NewKernelTyper,limInputTypeVar:int) extends Identity() {
    visit TypeConstraintList {
      // PHASE 1
      tcl@concTypeConstraint(tcl1*,Subtype[Type1=t1,Type2=t2,Info=info],tcl2*,Subtype[Type1=t2,Type2=t1],tcl3*) -> {
        // TODO : test if Eq(t1,t2,info) already exists in concTypeConstraint
        System.out.println("\nsolve1: " + `tcl);
        return
          nkt.`addEqConstraint(Equation(t1,t2,info),concTypeConstraint(tcl1,tcl2,tcl3));
      }

      // PHASE 2
      tcl@concTypeConstraint(_*,Subtype[Type1=t1,Type2=tVar@TypeVar[],Info=info],_*,Subtype[Type1=tVar,Type2=t2],_*) -> {
        System.out.println("\nsolve2: " + `tcl);
        return
          nkt.`addSubConstraint(Subtype(t1,t2,info),tcl);
      }
      tcl@concTypeConstraint(_*,Subtype[Type1=tVar,Type2=t2,Info=info],_*,Subtype[Type1=t1,Type2=tVar@TypeVar[]],_*) -> {
        // TODO : verify why the stratgy doesn't continue when addSubConstraint
        // returns the same typeconstraintlist
        System.out.println("\nsolve2: " + `tcl);
        return
          nkt.`addSubConstraint(Subtype(t1,t2,info),tcl);
      }

      // PHASE 3
      tcl@concTypeConstraint(_*,sConstraint@Subtype[Type1=!TypeVar[],Type2=!TypeVar[]],_*) -> {
        System.out.println("\nsolve3: " + `tcl);
        nkt.detectFail(`sConstraint);
      }
      concTypeConstraint(leftTCL*,c1@Subtype[Type1=tVar@TypeVar[],Type2=groundType@!TypeVar[]],rightTCL*) -> {
        System.out.println("\nsolve4: " + `c1);
        TypeConstraintList newLeftTCL = `leftTCL;
        TypeConstraintList newRightTCL = `rightTCL;
        if (!nkt.`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
          // Same code of cases 7 and 8 of solveEquationConstraints
          nkt.addSubstitution(`tVar,`groundType);
          return `concTypeConstraint(newLeftTCL*,newRightTCL*);
        }
      }
      concTypeConstraint(leftTCL*,c1@Subtype[Type1=groundType@!TypeVar[],Type2=tVar@TypeVar[]],rightTCL*) -> {
        System.out.println("\nsolve5: " + `c1);
        TypeConstraintList newLeftTCL = `leftTCL;
        TypeConstraintList newRightTCL = `rightTCL;
        if (!nkt.`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
          nkt.addSubstitution(`tVar,`groundType);
          return `concTypeConstraint(newLeftTCL*,newRightTCL*);
        }
      }

      // PHASE 4
      concTypeConstraint(tcl1*,constraint@Subtype[Type1=tVar@TypeVar[],Type2=t1@!TypeVar[],Info=info],tcl2*,c2@Subtype[Type1=tVar,Type2=t2@!TypeVar[]],tcl3*) -> {
        System.out.println("\nsolve6: " + `constraint + " and " + `c2);
        TomType lowerType = nkt.`minType(t1,t2);
        System.out.println("\nminType(" + `t1.getTomType() + "," +
            `t2.getTomType() + ") = " + lowerType);

        if (lowerType == `EmptyType()) {
          // TODO fix print (bad message and arguments)
          nkt.printError(`constraint);
          return `concTypeConstraint(tcl1,tcl2,tcl3); 
        }

        return
          nkt.`addSubConstraint(Subtype(tVar,lowerType,info),concTypeConstraint(tcl1,tcl2,tcl3));
      }
      concTypeConstraint(tcl1*,constraint@Subtype[Type1=t1@!TypeVar[],Type2=tVar@TypeVar[],Info=info],tcl2*,c2@Subtype[Type1=t2@!TypeVar[],Type2=tVar],tcl3*) -> {
        System.out.println("\nsolve7: " + `constraint + " and " + `c2);
        TomType upperType = nkt.`maxType(t1,t2);
        System.out.println("\nmaxType(" + `t1.getTomType() + "," +
            `t2.getTomType() + ") = " + upperType);

        if (upperType == `EmptyType()) {
          // TODO fix print (bad message and arguments)
          nkt.printError(`constraint);
          return `concTypeConstraint(tcl1,tcl2,tcl3); 
        }

        return
          nkt.`addSubConstraint(Subtype(upperType,tVar,info),concTypeConstraint(tcl1,tcl2,tcl3));
      }
      tcl -> {System.out.println("None of preview. tcl = " + `tcl);}
    }
  }

  private TypeConstraintList garbageCollect(TomType tType, TypeConstraintList tCList) {
    TypeConstraintList simplifiedTCList = `concTypeConstraint();
    for (TypeConstraint tConstraint : tCList.getCollectionconcTypeConstraint()) {
      %match {
        Subtype[Type1=t1,Type2=t2] << tConstraint && (t1 != tType) && (t2 !=
            tType) -> {
          simplifiedTCList = `concTypeConstraint(tConstraint,simplifiedTCList*);
        } 
      }
    }
    return simplifiedTCList;
  }

  private boolean findVar(TomType tVar, TypeConstraintList tCList) {
    %match {
      concTypeConstraint(_*,(Equation|Subtype)[Type1=t1],_*) << tCList &&
        t1 << TomType tVar -> { return true; }

      concTypeConstraint(_*,(Equation|Subtype)[Type2=t2],_*) << tCList &&
        t2 << TomType tVar -> { return true; }
    }
    return false;
  }

  private TomType minType(TomType t1, TomType t2) {
    TomTypeList supTypes1 = dependencies.get(t1);
    TomTypeList supTypes2 = dependencies.get(t2);
    %match {
      concTomType(_*,type,_*) << supTypes1 && (type == t2) -> {
        return t1;
      }
      concTomType(_*,type,_*) << supTypes2 && (type == t1) -> {
        return t2;
      }
    }
    return `EmptyType();
  }

  private TomType maxType(TomType t1, TomType t2) {
    TomTypeList supTypes1 = dependencies.get(t1);
    TomTypeList supTypes2 = dependencies.get(t2);
    %match {
      concTomType(_*,type,_*) << supTypes1 && (type == t2) -> {
        return t2;
      }
      concTomType(_*,type,_*) << supTypes2 && (type == t1) -> {
        return t1;
      }
      concTomType(_*,type,_*) << supTypes1 && concTomType(_*,type,_*) << supTypes2 -> {
        return `type;
      }
    }
    return `EmptyType();
  }


  private boolean isTypeVar(TomType type) {
    %match(type) {
      TypeVar(_,_) -> { return true; }
    }
    return false;
  }

  private void printError(TypeConstraint tConstraint) {
    %match {
      (Equation|Subtype)[Type1=tType1,Type2=tType2,Info=info] << tConstraint &&
        Type[TomType=tName1] << tType1 &&
        Type[TomType=tName2] << tType2 &&
        PairNameOptions(Name(termName),optionList) << info
        -> {
          Option option = TomBase.findOriginTracking(`optionList);
          %match(option) {
            OriginTracking(_,line,fileName) -> {
              if(lazyType==false) {
                TomMessage.error(logger,`fileName, `line,
                    TomMessage.incompatibleTypes,`tName1,`tName2,`termName); 
              }
            }
          }
        }
    }
  }

  private Code replaceInCode(Code code) {
    Code replacedCode = code;
    try {
      replacedCode = `InnermostId(replaceFreshTypeVar(this)).visitLight(code);
      `InnermostId(checkTypeOfBQVariables(this)).visitLight(replacedCode);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " +
          replacedCode);
    }
    return replacedCode;
  }

  private void replaceInSymbolTable() {
    for(String tomName:symbolTable.keySymbolIterable()) {
      //DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
      TomSymbol tSymbol = getSymbolFromName(tomName);
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
      //DEBUG     + tSymbol);
      try {
        tSymbol = `InnermostId(replaceFreshTypeVar(this)).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " +
            tSymbol);
      }
      //DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
      //DEBUG     + tSymbol);
      symbolTable.putSymbol(tomName,tSymbol);
    }
  }

  %strategy replaceFreshTypeVar(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar@TypeVar(_,_) -> {
        if (nkt.substitutions.containsKey(`typeVar)) {
          return nkt.substitutions.get(`typeVar);
        } 
      }
    }
  }


  %strategy checkTypeOfBQVariables(nkt:NewKernelTyper) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=Variable[],Subject=BQVariable[Options=oList,AstName=Name(name),AstType=TypeVar(_,_)]] -> {
        Option option = TomBase.findOriginTracking(`oList);
        %match(option) {
          OriginTracking(_,line,fileName) -> {
            TomMessage.error(logger,`fileName, `line,
                TomMessage.cannotGuessMatchType,`name); 
          }
        }
      }
    }
  }
  
  public void printGeneratedConstraints(TypeConstraintList tCList) {
    %match(tCList) {
      !concTypeConstraint() -> { 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(tCList);
        System.out.print("}");
      }
    }
  }

  public void printEachConstraint(TypeConstraintList tCList) {
    %match(tCList) {
      concTypeConstraint(Equation(type1,type2,_),tailtCList*) -> {
        printType(`type1);
        System.out.print(" = ");
        printType(`type2);
        if (`tailtCList != `concTypeConstraint()) {
            System.out.print(", "); 
            printEachConstraint(`tailtCList);
        }
      }

      concTypeConstraint(Subtype(type1,type2,_),tailtCList*) -> {
        printType(`type1);
        System.out.print(" <: ");
        printType(`type2);
        if (`tailtCList != `concTypeConstraint()) {
            System.out.print(", "); 
            printEachConstraint(`tailtCList);
        }
      }
    }
  }
    
  public void printType(TomType type) {
    System.out.print(type);
  }
} // NewKernelTyper
