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
 * Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
 * Jean-Christophe Bach e-mail: Jeanchristophe.Bach@inria.fr
 *
 **/

package tom.engine.typer;

import java.util.*;
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

  /*
   * pem: why use a state variable here ?
   */
  // List for variables of pattern (match constraints)
  private TomList varPatternList;
  // List for variables of subject and of numeric constraints
  private BQTermList varList;

  // List for type variables of patterns 
  private TomTypeList inputTVarList;
  // List for equation constraints (for fresh type variables)
  private TypeConstraintList equationConstraints;
  // List for subtype constraints (for fresh type variables)
  private TypeConstraintList subtypeConstraints;
  // bag used to efficiently check that a constraint is already in a list
  private Collection<TypeConstraint> constraintBag;

  // Set of pairs (freshVar,type)
  private Substitution substitutions;
  // Set of supertypes for each type
  private HashMap<String,TomTypeList> dependencies = new HashMap<String,TomTypeList>();

  private SymbolTable symbolTable;

  private String currentInputFileName;

  protected void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
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


  /**
   * The method <code>getType</code> gets the type of a term by consulting the
   * SymbolTable.
   * @param bqTerm  the BQTerm requesting a type
   * @return        the type of the BQTerm
   */
/*
  protected TomType getType(BQTerm bqTerm) {
    %match(bqTerm) {
      (BQVariable|BQVariableStar|FunctionCall)[AstType=aType] -> { return `aType; }
      (BQAppl|BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name[String=name]] -> {
        TomSymbol tSymbol = getSymbolFromName(`name);
        return getCodomain(tSymbol);
      }
    } 
    throw new TomRuntimeException("getType(BQTerm): should not be here: " + bqTerm);
  }
*/

  /**
   * The method <code>getType</code> gets the type of a term by consulting the
   * SymbolTable
   * @param tTerm the TomTerm requesting a type
   * @return      the type of the TomTerm
   */
/*
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
*/

  /**
   * The method <code>getInfoFromTomTerm</code> creates a pair
   * (name,information) for a given term by consulting its attributes.
   * @param tTerm  the TomTerm requesting the informations
   * @return       the information about the TomTerm
   */
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
    throw new TomRuntimeException("getInfoFromTomTerm: should not be here: " + tTerm);
    //return `PairNameOptions(Name(""),concOption()); 
  }

  /**
   * The method <code>getInfoFromBQTerm</code> creates a pair
   * (name,information) for a given term by consulting its attributes.
   * @param bqTerm the BQTerm requesting the informations
   * @return       the information about the BQTerm
   */
  protected Info getInfoFromBQTerm(BQTerm bqTerm) {
    %match(bqTerm) {
      (BQVariable|BQVariableStar|BQAppl)[Options=optionList,AstName=aName] -> { 
        return `PairNameOptions(aName,optionList); 
      }

      Composite(_*,CompositeBQTerm[term=cBQTerm],_*) -> {
        return getInfoFromBQTerm(`cBQTerm);
      }

      (BuildConstant|BuildTerm|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=aName] -> {
        return `PairNameOptions(aName,concOption()); 
      }
    } 
    throw new TomRuntimeException("getInfoFromTomBQTerm: should not be here: " + bqTerm);
    //return `PairNameOptions(Name(""),concOption()); 
  }

  /**
   * The method <code>setLimTVarSymbolTable</code> sets the lower bound of the
   * counter of type variables. This methods is called by the TyperPlugin
   * after replacing all unknown types of the SymbolTable by type variables and
   * before start the type inference.
   * @param freshTVarSymbolTable  the lower bound of the counter of type
   *                              variables
   */
  private int limTVarSymbolTable;
  protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
    limTVarSymbolTable = freshTVarSymbolTable;
    globalTypeVarCounter = freshTVarSymbolTable; 
  }

  // Counters for verbose mode 
  private int globalEqConstraintCounter = 0;
  private int globalSubConstraintCounter = 0;
  private int globalTypeVarCounter = 0;
  protected int getEqCounter() { return globalEqConstraintCounter; } 
  protected int getSubCounter() { return globalSubConstraintCounter; } 
  protected int getTVarCounter() { return globalTypeVarCounter; }

  /**
   * The method <code>getFreshTlTIndex</code> increments the counter of type variables. 
   * @return  the incremented counter of type variables
   */
  private int freshTypeVarCounter;
  private int getFreshTlTIndex() {
    globalTypeVarCounter++;
    return freshTypeVarCounter++;
  }

  /**
   * The method <code>getUnknownFreshTypeVar</code> generates a fresh type
   * variable (by considering the global counter of type variables)
   * @return  a new (fresh) type variable
   */
  protected TomType getUnknownFreshTypeVar() {
    TomType tType = symbolTable.TYPE_UNKNOWN;
    %match(tType) {
      Type[TomType=tomType] -> { return `TypeVar(tomType,getFreshTlTIndex()); }
    }
    throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
  }


  protected TypeConstraintList addConstraintSlow(TypeConstraint tConstraint, TypeConstraintList tCList) {
    if(!containsConstraint(tConstraint,tCList)) {
      %match(tConstraint) {
        (Equation|Subtype)[Type1=t1@!EmptyType(),Type2=t2@!EmptyType()] && (t1 != t2) -> { 
          tCList = `concTypeConstraint(tConstraint,tCList*);
        }
      }
    }
    return tCList;
  }

  /**
   * The method <code>containsConstraint</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints. 
   * slow code used only by addConstraintSlow (for compatibility reasons)
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if the constraint already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    %match(tConstraint,tCList) {
      Subtype[Type1=t1,Type2=t2], concTypeConstraint(_*,(Subtype|Equation)[Type1=t1,Type2=t2],_*) -> { return true; }

      Equation[Type1=t1,Type2=t2], concTypeConstraint(_*,Equation[Type1=t3,Type2=t4],_*) -> {
        if(`(t1==t3 && t2==t4) || `(t1==t4 && t2==t3)) {
          return true; 
        }
      }
    }
    return containsConstraintModuloEqDecoratedSort(tConstraint,tCList);
  } 
  
  /**
   * The method <code>containsConstraintModuloEqDecoratedSort</code> checks if a given constraint
   * already exists in a constraint type list. The method considers symmetry for
   * equation constraints and equality of decorated sorts in other to allow the
   * following:
   * insert((A = T^?),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {A = T^?} U C,  otherwise
   *
   * insert((T^? = A),C)	->	C,              if (A = T^c) in C \/ (T^c = A) in C 
   *                      ->  {T^? = A} U C,  otherwise
   * 
   * slow code used only by containsConstraint (for compatibility reasons)
   * @param tConstraint the constraint to be considered
   * @param tCList      the type constraint list to be traversed
   * @return            'true' if an equal constraint modulo EqOfDecSort already exists in the list
   *                    'false' otherwise            
   */
  protected boolean containsConstraintModuloEqDecoratedSort(TypeConstraint tConstraint, TypeConstraintList tCList) {
    %match(tConstraint) {
      Equation[Type1=tVar@TypeVar[],Type2=Type[TypeOptions=tOptions,TomType=tType]] &&
        !concTypeOption(_*,WithSymbol[],_*) << tOptions &&
        concTypeConstraint(_*,Equation[Type1=tVar,Type2=Type[TypeOptions=decoratedtOptions,TomType=tType]],_*) << tCList && 
        concTypeOption(_*,WithSymbol[],_*) << decoratedtOptions -> { return true; }

      Equation[Type1=tVar@TypeVar[],Type2=Type[TypeOptions=tOptions,TomType=tType]] &&
        !concTypeOption(_*,WithSymbol[],_*) << tOptions &&
        concTypeConstraint(_*,Equation[Type1=Type[TypeOptions=decoratedtOptions,TomType=tType],Type2=tVar],_*) << tCList && 
        concTypeOption(_*,WithSymbol[],_*) << decoratedtOptions -> { return true; }

      Equation[Type1=Type[TypeOptions=tOptions,TomType=tType],Type2=tVar@TypeVar[]] &&
        !concTypeOption(_*,WithSymbol[],_*) << tOptions &&
        concTypeConstraint(_*,Equation[Type1=tVar,Type2=Type[TypeOptions=decoratedtOptions,TomType=tType]],_*) << tCList && 
        concTypeOption(_*,WithSymbol[],_*) << decoratedtOptions -> { return true; }


      Equation[Type1=Type[TypeOptions=tOptions,TomType=tType],Type2=tVar@TypeVar[]] &&
        !concTypeOption(_*,WithSymbol[],_*) << tOptions &&
        concTypeConstraint(_*,Equation[Type1=Type[TypeOptions=decoratedtOptions,TomType=tType],Type2=tVar],_*) << tCList && 
        concTypeOption(_*,WithSymbol[],_*) << decoratedtOptions -> { return true; }
    }
    return false;
  }


  /*
   * pem: use if(...==... && typeConstraints.contains(...))
   */
  /**
   * The method <code>addConstraint</code> insert a constraint (an equation or
   * a subtype constraint) into a given type constraint list only if this
   * constraint does not yet exist into the list and if it does not contains
   * "EmptyTypes". 
   * @param tConstraint the constraint to be inserted into the type constraint list
   * @param tCList      the constraint type list where the constraint will be
   *                    inserted
   * @return            the list resulting of the insertion
   */
  protected TypeConstraintList addConstraint(TypeConstraint tConstraint, TypeConstraintList tCList) {
    TypeConstraint constraint = null;
    TypeOptionList emptyOptionList = `concTypeOption();
    TargetLanguageType emptyTargetLanguageType = `EmptyTargetLanguageType();
    Info emptyInfo = `PairNameOptions(EmptyName(),concOption());

    %match(tConstraint) {
      Equation[Type1=type1,Type2=type2] -> {
        constraint         = `Equation(type1,type2,emptyInfo);
      }
      Equation[Type1=tVar@TypeVar[],Type2=Type[TomType=tType]] -> {
        constraint         = `Equation(tVar,Type(emptyOptionList,tType,emptyTargetLanguageType),emptyInfo);
      }
      Equation[Type1=Type[TomType=tType],Type2=tVar@TypeVar[]] -> {
        constraint         = `Equation(Type(emptyOptionList,tType,emptyTargetLanguageType),tVar,emptyInfo);
      }

      Equation[Type1=Type[TypeOptions=ol1,TomType=tType1],Type2=Type[TypeOptions=ol2,TomType=tType2]] -> {
        constraint         = `Equation(Type(ol1,tType1,emptyTargetLanguageType),
                                       Type(ol2,tType2,emptyTargetLanguageType),emptyInfo);
      }

      Subtype[Type1=type1,Type2=type2] -> {
        constraint         = `Subtype(type1,type2,emptyInfo);
      }
      Subtype[Type1=type,Type2=Type[TypeOptions=ol,TomType=tType]] -> {
        constraint         = `Subtype(type,Type(ol,tType,emptyTargetLanguageType),emptyInfo);
      }
      Subtype[Type1=Type[TypeOptions=ol,TomType=tType],Type2=type] -> {
        constraint         = `Subtype(Type(ol,tType,emptyTargetLanguageType),type,emptyInfo);
      }
      Subtype[Type1=Type[TypeOptions=ol1,TomType=tType1],Type2=Type[TypeOptions=ol2,TomType=tType2]] -> {
        constraint         = `Subtype(Type(ol1,tType1,emptyTargetLanguageType),
                                      Type(ol2,tType2,emptyTargetLanguageType),emptyInfo);
      }

      (Equation|Subtype)[Type1=type,Type2=type] -> {
        // do not add tautology
        constraint         = null;
      }
      (Equation|Subtype)[Type1=EmptyType[],Type2=type2] -> {
        // do not add tautology
        constraint         = null;
      }
      (Equation|Subtype)[Type1=type1,Type2=EmptyType[]] -> {
        // do not add tautology
        constraint         = null;
      }

    }

    if(constraint == null || constraintBag.contains(constraint)) {
      return tCList;
    } else {
      constraintBag.add(constraint);
      %match(constraint) {
        Equation(type1,type2,emptyInfo) -> {
          constraintBag.add(`Equation(type2,type1,emptyInfo));
        }
      }

      return `concTypeConstraint(tConstraint,tCList*);
    }
   
  }


  /**
   * The method <code>generateDependencies</code> generates a
   * hashMap called "dependencies" having pairs (typeName,supertypesList), where
   * supertypeslist is a list with all the related proper supertypes for each
   * ground type used into a code. The list is obtained by reflexive and
   * transitive closure over the direct supertype relation defined by the user
   * when defining the mappings.
   * For example, to generate the supertypes of T2, where T2 is a ground type, we
   * follow two steps:
   * <p>
   * STEP 1:  a) get the list supertypes_T3, put(T2,({T3} U supertype_T3)) and go
   *          to step 2, if there exists a declaration of form T2<:T3
   *          b) put(T2,{}), otherwise 
   * <p>
   * STEP 2:  put(T1,(supertypes_T1 U supertypes_T2)), for each (T1,{...,T2,...}) in dependencies
   */
  // TODO: optimization 1 - add a test before adding a new pair in the hash map "dependencies" 
  // TODO: optimization 2 - generate the dependencies once at the beginning of
  // type inference and for all types of symbol table instead of only "used
  // types".
  protected void generateDependencies() {
    for(TomType currentType:symbolTable.getUsedTypes()) {
      String currentTypeName = currentType.getTomType();
      TomTypeList superTypes = `concTomType();
      %match(currentType) {
        /* STEP 1 */
        Type[TypeOptions=concTypeOption(_*,SubtypeDecl[TomType=supTypeName],_*),TomType=tName] -> {
          //DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
          //DEBUG     + `supTypeName + " and supType = " + supType);
          if(dependencies.containsKey(`supTypeName)) {
            superTypes = dependencies.get(`supTypeName); 
          }
          TomType supType = symbolTable.getType(`supTypeName);
          if(supType != null) {
            superTypes = `concTomType(supType,superTypes*);  

            /* STEP 2 */
            for(String subType:dependencies.keySet()) {
              TomTypeList supOfSubTypes = dependencies.get(`subType);
              //DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
              //DEBUG     supOfSubTypes);
              %match {
                // The same tName of currentType
                concTomType(_*,Type[TomType=suptName],_*) << supOfSubTypes && (suptName == tName) -> {
                    /* 
                     * Replace list of superTypes of "subType" by a new one
                     * containing the superTypes of "currentType" which is also a
                     * superType 
                     */
                    dependencies.put(subType,`concTomType(supOfSubTypes*,superTypes*));
                  }
              }
            }
          } else {
            TomMessage.error(logger,getCurrentInputFileName(),0,
                TomMessage.typetermNotDefined,`supTypeName);
          }
        }
      }
      //DEBUG System.out.println("In generateDependencies -- end: superTypes = " +
      //DEBUG     superTypes);
      dependencies.put(currentTypeName,superTypes);
    }
  }


  protected void addPositiveTVar(TomType tType) {
    inputTVarList = `concTomType(tType,inputTVarList*);
  }

  /**
   * The method <code>addTomTerm</code> insert a TomTerm into the global
   * <code>varPatternList</code>.
   * @param tTerm  the TomTerm to be inserted
   */
  protected void addTomTerm(TomTerm tTerm) {
    varPatternList = `concTomTerm(tTerm,varPatternList*);
  }

  /**
   * The method <code>addBQTerm</code> insert a BQTerm into the global
   * <code>varList</code>.
   * @param bqTerm  the BQTerm to be inserted
   */
  protected void addBQTerm(BQTerm bqTerm) {
    varList = `concBQTerm(bqTerm,varList*);
  }

  /**
   * The method <code>resetVarList</code> checks if <code>varList</code> contains
   * a BQTerm which is in the local context (see page 137 of Claudia's thesis) but is not 
   * in the global context, i.e. <code>varPatternList</code>\<code>globalVarPatternList</code>.
   * Then, this BQTerm is removed from <code>varList</code>.
   * e.g. :
   * gC = global context / lC = local context
   * Suppose the following match block
   *   %match {					// l1
   *     f(x) << v1 && (x == a()) -> { `x; }	// l2
   *     x << v2 && (x != b() -> { `x; }	// l3
   *   }
   * where A := a() and B := b()
   *  		gC	    |	lC
   * --------------------------------
   * l1		{}	    |	{}
   * l2		{v1}	  |	{x}
   * l3		{v1,v2}	|	{x}
   * But the "x" of lC at l2 is not the same of that of lC at l3. Although the
   * "x" occurring in the numeric condition of l2 is in varList, it must be
   * removed to avoid variable capture when the condition of l3 is considered.
   * Thus, instead of performing variable renaming, we assume that pattern
   * variables are only "valid" in the current rule cond -> action.
   * pem: remove from varList, all variables that belong to varPatternList, which are not in globalVarPatternList
   * @param globalVarPatternList   the TomList to be reset
   */
  protected void resetVarList(TomList globalVarPatternList) {
    for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {
      %match(tTerm,globalVarPatternList,varList) {
        test@(Variable|VariableStar)[AstName=aName],
        !concTomTerm(_*,test,_*),
        concBQTerm(x*,(BQVariable|BQVariableStar)[AstName=aName],y*)
          -> {
            //System.out.println("*** resetVarList remove: " + `aName);
            varList = `concBQTerm(x*,y*);
          }
      }
    }
  }

  /**
   * The method <code>init</code> reset the counter of type variables
   * <code>freshTypeVarCounter</code> and empties all global lists and hashMaps which means to
   * empty <code>varPatternList</code>, <code>varList</code>,
   * <code>equationConstraints</code>, <code>subtypeConstraints</code> and <code>substitutions</code>
   */
  public void init() {
    freshTypeVarCounter = limTVarSymbolTable;
    varPatternList = `concTomTerm();
    varList = `concBQTerm();
    inputTVarList = `concTomType();
    equationConstraints = `concTypeConstraint();
    subtypeConstraints = `concTypeConstraint();
    constraintBag = new HashSet<TypeConstraint>();
    substitutions = new Substitution();
  }

  /**
   * The method <code>collectKnownTypesFromCode</code> creates an instance of
   * the class <code>CollectKnownTypes</code> and calls its method
   * <code>visitLight</code> to traverse a code. 
   * @param  subject the code to be traversed/transformed
   * @return         the code resulting of a transformation
   */
  private Code collectKnownTypesFromCode(Code subject) {
    try {
      return `TopDownIdStopOnSuccess(CollectKnownTypes(this)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
    }
  }

  /**
   * The class <code>CollectKnownTypes</code> is generated from a strategy which
   * initially types all terms by using their correspondent type in symbol table
   * or a fresh type variable :
   * CASE 1 : Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if
   * name is in TypeTable
   * CASE 2 : Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i))
   * if name is not in TypeTable
   * @param nkt an instance of object NewKernelTyper
   * @return    the code resulting of a transformation
   */
  %strategy CollectKnownTypes(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      Type[TomType=tomType,TlType=EmptyTargetLanguageType()] -> {
        TomType newType = nkt.symbolTable.getType(`tomType);
        if(newType == null) {
          /*
           * This happens when :
           * tomType != unknown type AND (newType == null)
           * tomType == unknown type
           */
          newType = `TypeVar(tomType,nkt.getFreshTlTIndex());
        }
        return newType;
      }
    }
  }

  /**
   * The method <code>inferAllTypes</code> is the start-up of the inference
   * process. It is a generic method and it is called for the first time by the
   * NewTyper.
   * @param term        the expression needing type inference
   * @param contextType the type related to the current expression
   * @return            the resulting typed expression 
   */
  public <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType contextType) {
    try {
      //System.out.println("inferAllTypes: " + term + " --- " + contextType);
      return `TopDownStopOnSuccess(inferTypes(contextType,this)).visitLight(term); 
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("inferAllTypes: failure on " + term);
    }
  }

  /**
   * The class <code>inferTypes</code> is generated from a strategy which
   * tries to infer types of all variables on a given expression
   * <p> 
   * It starts by searching for a Code <code>Tom</code> or
   * <code>TomInclude</code> and calling <code>inferCodeList</code> in order to
   * apply rule CT-BLOCK for each block of ConstraintInstruction.
   * <p>
   * Then it searches for a Instruction
   * <code>Match(constraintInstructionList,option)</code> and calls
   * <code>inferConstraintInstructionList</code> in order to apply rule CT-RULE
   * for each single constraintInstruction
   * <p>
   * Then it searches for variables and star variables (TomTerms and BQTerms) and
   * applies rules CT-ALIAS, CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
   * CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) or a
   * "subject" (a BQTerm) in order to infer types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * CT-ALIAS rule:
   * IF found "x@e:A and "x:T" already exists in context
   * THEN adds a type constraint "A = T" and infers type of "e:A"
   * <p>
   * CT-ANTI rule:
   * IF found "!(e):A"
   * THEN infers type of "e:A"
   * <p>
   * CT-VAR rule (resp. CT-SVAR): 
   * IF found "x:A" (resp. "x*:A") and "x:A1" (resp. "x*:A1") already exists in
   *    context 
   * THEN adds a type constraint "A1 <: A"
   * <p>
   * CT-FUN rule:
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * CT-EMPTY rule:
   * IF found "l():A" and "l:T1*->T" exists in SymbolTable
   * THEN adds a type constraint "T <: A"       
   * <p>
   * CT-ELEM rule:
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * CT-MERGE rule:
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
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
    /*
     * We put "returns" for each "case" of a visit in order to interrupt the
     * flow of the strategy.
     * e.g. for "f(g(x))" the strategy will be applied over "x" three times (1
     * when visiting "f(g(x))" + 1 when visiting "g(x)" + 1 when visiting "x")
     */

    visit Code {
      code@(Tom|TomInclude)[CodeList=cList] -> {
        nkt.generateDependencies();
        CodeList newCList = nkt.inferCodeList(`cList);
        return `code.setCodeList(newCList);
      }
    }

    visit Instruction {
      Match[ConstraintInstructionList=ciList,Options=optionList] -> {
        /*BQTermList BQTList = nkt.varList;*/
        ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList(`ciList);
        /*nkt.varList = BQTList;*/
        return `Match(newCIList,optionList);
      }      
    }

    visit TomVisit {
      VisitTerm[VNode=vNode,AstConstraintInstructionList=ciList,Options=optionList] -> {
        /*BQTermList BQTList = nkt.varList;*/
        ConstraintInstructionList newCIList = nkt.inferConstraintInstructionList(`ciList);
        /*nkt.varList = BQTList;*/
        return `VisitTerm(vNode,newCIList,optionList);
      }
    }

    visit TomTerm {
      //AntiTerm[TomTerm=atomicTerm] -> { 
      //  TomTerm tmp = nkt.inferAllTypes(`atomicTerm,contextType); 
        /* pem: there is not return: can be removed! */
      //}

      var@Variable[Options=optionList,AstName=aName,AstType=aType,Constraints=cList] -> {
        nkt.checkNonLinearityOfVariables(`var);
        nkt.subtypeConstraints =
          nkt.addConstraint(`Subtype(aType,contextType,PairNameOptions(aName,optionList)),nkt.subtypeConstraints);  
        %match(cList) {
          /* How many "AliasTo" constructors can concConstraint have? */
          concConstraint(AliasTo(boundTerm)) -> {
            nkt.equationConstraints =
              nkt.addConstraint(`Equation(TomBase.getTermType(boundTerm,nkt.symbolTable),aType,nkt.getInfoFromTomTerm(boundTerm)),nkt.equationConstraints); 
          }
        }
        return `var.setConstraints(`cList);
      }

      varStar@VariableStar[Options=optionList,AstName=aName,AstType=aType,Constraints=cList] -> {
        nkt.checkNonLinearityOfVariables(`varStar);
        nkt.equationConstraints =
          nkt.addConstraint(`Equation(aType,contextType,PairNameOptions(aName,optionList)),nkt.equationConstraints);  
        %match(cList) {
          /* How many "AliasTo" constructors can concConstraint have? */
          concConstraint(AliasTo(boundTerm)) -> {
            nkt.equationConstraints =
              nkt.addConstraint(`Equation(TomBase.getTermType(boundTerm,nkt.symbolTable),aType,nkt.getInfoFromTomTerm(boundTerm)),nkt.equationConstraints); 
          }
        }
        return `varStar.setConstraints(`cList);
      }

      RecordAppl[Options=optionList,NameList=nList@concTomName(aName@Name(tomName),_*),Slots=sList,Constraints=cList] -> {
        /* In case of a String, tomName is "" for ("a","b") */
        TomSymbol tSymbol = nkt.getSymbolFromName(`tomName);
        TomType codomain = contextType;

        /* IF_3 */
        if(tSymbol == null) {
          tSymbol = `EmptySymbol();
        } else {
          /* This code cannot be moved to IF_2 because tSymbol may don't be
           "null" since the begginning and then does not enter into neither IF_1 nor */

          /* IF_2 */
          %match(tSymbol) {
            Symbol[AstName=symName,TypesToType=TypesToType[Domain=domain,Codomain=tsCodomain@Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain]],PairNameDeclList=pndList,Options=options] -> { 
              codomain = `tsCodomain;
              if(TomBase.isListOperator(`tSymbol) || TomBase.isArrayOperator(`tSymbol)) {
                /* Apply decoration for types of list operators */
                TypeOptionList newTOptions = `concTypeOption(WithSymbol(symName),tOptions*);
                codomain = `Type(newTOptions,tomCodomain,tlCodomain);
                tSymbol = `Symbol(symName,TypesToType(domain,codomain),pndList,options); 
              }  
            }
          }
          nkt.subtypeConstraints = nkt.addConstraint(`Subtype(codomain,contextType,PairNameOptions(aName,optionList)),nkt.subtypeConstraints);
        }

        %match(cList) {
          /* How many "AliasTo" constructors can concConstraint have? */
          concConstraint(AliasTo(boundTerm)) -> {
            nkt.equationConstraints =
              nkt.addConstraint(`Equation(TomBase.getTermType(boundTerm,nkt.symbolTable),codomain,nkt.getInfoFromTomTerm(boundTerm)),nkt.equationConstraints); 
          }
        }

        SlotList newSList = `concSlot();
        if(!`sList.isEmptyconcSlot()) {
          `newSList = nkt.inferSlotList(`sList,tSymbol,contextType);
        }
        return `RecordAppl(optionList,nList,newSList,cList);
      }
    }

    visit BQTerm {
      bqVar@BQVariable[Options=optionList,AstName=aName,AstType=aType] -> {
        nkt.checkNonLinearityOfBQVariables(`bqVar);
        nkt.subtypeConstraints = nkt.addConstraint(`Subtype(aType,contextType,PairNameOptions(aName,optionList)),nkt.subtypeConstraints);  
        return `bqVar;
      }

      bqVarStar@BQVariableStar[Options=optionList,AstName=aName,AstType=aType] -> {
        nkt.checkNonLinearityOfBQVariables(`bqVarStar);
        nkt.equationConstraints =
          nkt.addConstraint(`Equation(aType,contextType,PairNameOptions(aName,optionList)),nkt.equationConstraints);
        return `bqVarStar;
      }

      BQAppl[Options=optionList,AstName=aName@Name(name),Args=bqTList] -> {
        TomSymbol tSymbol = nkt.getSymbolFromName(`name);
        TomType codomain = contextType;
        if (tSymbol == null) {
          tSymbol = `EmptySymbol();
          //DEBUG System.out.println("name = " + `name);
          //DEBUG System.out.println("context = " + contextType);
          BQTermList newBQTList = nkt.inferBQTermList(`bqTList,`EmptySymbol(),contextType);
          /* PEM: why contextType ? */
          return `FunctionCall(aName,contextType,newBQTList); 
        } else {
          %match(tSymbol) {
            Symbol[AstName=symName,TypesToType=TypesToType[Domain=domain,Codomain=tsCodomain@Type[TypeOptions=tOptions,TomType=tomCodomain,TlType=tlCodomain]],PairNameDeclList=pndList,Options=options] -> { 
              codomain = `tsCodomain;
              if(TomBase.isListOperator(tSymbol) || TomBase.isArrayOperator(tSymbol)) {
                /* Apply decoration for types of list operators */
                TypeOptionList newTOptions = `concTypeOption(WithSymbol(symName),tOptions*);
                codomain = `Type(newTOptions,tomCodomain,tlCodomain);
                tSymbol = `Symbol(symName,TypesToType(domain,codomain),pndList,options); 
              }  
            }
          }
          nkt.subtypeConstraints = nkt.addConstraint(`Subtype(codomain,contextType,PairNameOptions(aName,optionList)),nkt.subtypeConstraints);

          BQTermList newBQTList = nkt.inferBQTermList(`bqTList,`tSymbol,contextType);
          return `BQAppl(optionList,aName,newBQTList);
        }
      }
    }
  }

  /**
   * The method <code>checkNonLinearityOfVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "TomTerm" that already exists in <code>varPatternList</code> or in
   * <code>varList</code>, a type constraint is added to
   * <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the <code>varList</code> since a Variable/VariableStar can have
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
        (concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << varPatternList ||
         concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << varList) -> {
          equationConstraints = addConstraint(`Equation(aType1,aType2,PairNameOptions(aName,optionList)),equationConstraints);
        }

      (Variable|VariableStar)[AstName=aName,AstType=aType] << var &&
        concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType],_*) << varPatternList -> {
          //DEBUG System.out.println("Add type '" + `aType + "' of var '" + `var +"'\n");
          addPositiveTVar(`aType);
        }
    }
  }

  /**
   * The method <code>checkNonLinearityOfBQVariables</code> searches for variables
   * occurring more than once in a condition.
   * <p>
   * For each variable of type
   * "BQTerm" that already exists in <code>varPatternList</code> or in
   * <code>varList</code>, a type constraint is added to
   * <code>equationConstraints</code> to ensure that  both
   * variables have same type (this happens in case of non-linearity).
   * <p>
   * OBS.: we also need to check the <code>varPatternList</code> since a
   * BQVariable/BQVariableStar can have occurred in a previous condition as a
   * Variable/VariableStar, in the case of a composed condition or of a inner match
   * e.g. f(x) << e && (x < 10 ) -> { action } 
   * @param bqvar the variable to have the linearity checked
   */
  private void checkNonLinearityOfBQVariables(BQTerm bqvar) {
    TomType newType = null;
    %match(bqvar) {
      // If the backquote variable already exists in varPatternList 
      // (in the case of a inner match) or in varList
      (BQVariable|BQVariableStar)[Options=optionList,AstName=aName,AstType=aType1] &&
        (
         concBQTerm(_*,(BQVariable|BQVariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << varList 
         ||
         concTomTerm(_*,(Variable|VariableStar)[AstName=aName,AstType=aType2@!aType1],_*) << varPatternList
        ) -> {
          /* 
           * this is a hack for the new parser which add more types in the AST 
           */
          %match() {
            (Type[] << aType1 || Type[] << aType2) -> {
              if(newType == null) {
                newType = getUnknownFreshTypeVar();
              }
              subtypeConstraints =
                addConstraint(`Subtype(aType1,newType,PairNameOptions(aName,optionList)),subtypeConstraints); 
              subtypeConstraints =
                addConstraint(`Subtype(aType2,newType,PairNameOptions(aName,optionList)),subtypeConstraints); 
            }
            (!Type[] << aType1 && !Type[] << aType2) -> {
              equationConstraints =
                  addConstraint(`Equation(aType1,aType2,PairNameOptions(aName,optionList)),equationConstraints); 
            }
          }
        }
    }
  }

  /**
   * The method <code>inferCodeList</code> applies rule CT-BLOCK. It starts
   * inference process which takes one code at a time
   * <ul>
   *  <li> all lists and hashMaps are reset
   *  <li> each code is typed with fresh type variables
   *  <li> each code is traversed in order to generate type constraints
   *  <li> the type constraints of "equationConstraints" and
   *        "subtypeConstraints" lists are solved at the end
   *        of the current code generating a mapping (a set of
   *        substitutions for each type variable)
   *  <li> the mapping is applied over the code and the symbol table
   * </ul>
   * <p>
   * CT-BLOCK rule:
   * IF found "(rule_1,...,rule_n)" where "rule_i" are ConstraintInstructions
   * THEN infers types of each ConstraintInstruction
   * @param cList the tom code list to be type inferred
   * @return      the tom typed code list
   */
  private CodeList inferCodeList(CodeList cList) {
    CodeList newCList = `concCode();
    for(Code code : cList.getCollectionconcCode()) {
      //init(); // should not be called here when Tom(...) constructs are nested
      code =  collectKnownTypesFromCode(`code);
      //System.out.println("------------- Code typed with typeVar:\n code = " + `code);
      code = inferAllTypes(code,`EmptyType());
      //DEBUG printGeneratedConstraints(subtypeConstraints);
      //printGeneratedConstraints(equationConstraints);
      solveConstraints();
      //System.out.println("substitutions = " + substitutions);
      code = replaceInCode(code);
      //System.out.println("------------- Code typed with substitutions:\n code = " + `code);
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
   * IF found "cond --> action)" where "action" is a list of terms or a code to
   *    be type inferred
   * THEN infers types of condition (by calling <code>inferConstraint</code>
   *      method) and action (by calling <code>inferAllTypes</code>) 
   * @param ciList  the list of pairs "condition -> action" to be type inferred 
   * @return        the typed list resulting
   */
  private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
    ConstraintInstructionList newCIList = `concConstraintInstruction();
    BQTermList BQTList = varList;
    for(ConstraintInstruction cInst:ciList.getCollectionconcConstraintInstruction()) {
      try {
        %match(cInst) {
          ConstraintInstruction(constraint,action,optionList) -> {
            // Store variable lists in new variables and reinitialize them
            BQTermList globalVarList = varList;
            TomList globalVarPatternList = varPatternList;

            `TopDownCollect(CollectVars(this)).visitLight(`constraint);

            Constraint newConstraint = inferConstraint(`constraint);
            //DEBUG System.out.println("inferConstraintInstructionList: action " +
            //DEBUG     `action);
            Instruction newAction = `inferAllTypes(action,EmptyType());

            resetVarList(globalVarPatternList);
            varPatternList = globalVarPatternList;
            newCIList =
              `concConstraintInstruction(ConstraintInstruction(newConstraint,newAction,optionList),newCIList*);
          } 
        }
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("inferConstraintInstructionList: failure on " + `cInst);
      }
    }
    varList = BQTList;
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
      var@(Variable|VariableStar)[] -> { 
        nkt.addTomTerm(`var);
      }
    }

    visit BQTerm {
      bqvar@(BQVariable|BQVariableStar)[] -> { 
        nkt.addBQTerm(`bqvar);
      }
    }
  }

  /**
   * The method <code>inferConstraint</code> applies rule CT-MATCH, CT-NUM, CT-CONJ,
   * or CT-DISJ to a "condition" in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * CT-MATCH rule:
   * IF found "e1 << [A] e2" 
   * THEN infers type of e1 and e2 and add the type constraints "T1 = A" and "A
   * <: T2"
   * <p>
   * CT-NUM rule:
   * IF found "e1 == e2", "e1 <= e2", "e1 < e2", "e1 >= e2" or "e1 > e2"
   * THEN infers type of e1 and e2 and add the type constraints "A <:T1" and "A
   * <: T2", where "A" is a fresh type variable   
   * <p>
   * CT-CONJ rule (resp. CT-DISJ):
   * IF found "cond1 && cond2" (resp. "cond1 || cond2")
   * THEN infers type of cond1 and cond2 (by calling
   * <code>inferConstraint</code> for each condition)
   * @param constraint  the "condition" to be type inferred 
   * @return            the typed condition resulting
   */
  private Constraint inferConstraint(Constraint constraint) {
    %match(constraint) {
      MatchConstraint[Pattern=pattern,Subject=subject,AstType=aType] -> { 
        //System.out.println("inferConstraint l1 -- subject = " + `subject);
        TomType tPattern = TomBase.getTermType(`pattern,symbolTable);
        TomType tSubject = TomBase.getTermType(`subject,symbolTable);
        if (tPattern == null || tPattern == `EmptyType()) {
          tPattern = getUnknownFreshTypeVar();
        }
        if (tSubject == null || tSubject == `EmptyType()) {
          tSubject = getUnknownFreshTypeVar();
        }
        //System.out.println("inferConstraint: match -- constraint " + tPattern + " = " + tSubject);
        %match(aType) {
          (Type|TypeVar)[] -> {
            /* T_pattern = T_cast and T_cast <: T_subject */
            equationConstraints =
              addConstraint(`Equation(tPattern,aType,getInfoFromTomTerm(pattern)),equationConstraints);
            subtypeConstraints = addConstraint(`Subtype(tPattern,tSubject,getInfoFromBQTerm(subject)),subtypeConstraints);
          }
        }
        TomTerm newPattern = `inferAllTypes(pattern,tPattern);
        BQTerm newSubject = `inferAllTypes(subject,tSubject);
        //System.out.println("inferConstraint: newPattern: " + newPattern);
        return `MatchConstraint(newPattern,newSubject,aType);
      }

      NumericConstraint(left,right,kind) -> {
        TomType tLeft = TomBase.getTermType(`left,symbolTable);
        TomType tRight = TomBase.getTermType(`right,symbolTable);
        if (tLeft == null || tLeft == `EmptyType()) {
          tLeft = getUnknownFreshTypeVar();
        }
        if (tRight == null || tRight == `EmptyType()) {
          tRight = getUnknownFreshTypeVar();
        }
        //DEBUG System.out.println("inferConstraint: match -- constraint " +
        //DEBUG     tLeft + " = " + tRight);

        // To represent the relationship between both argument types
        TomType lowerType = getUnknownFreshTypeVar();
        subtypeConstraints =
          addConstraint(`Subtype(lowerType,tLeft,getInfoFromBQTerm(left)),subtypeConstraints);
        subtypeConstraints =
          addConstraint(`Subtype(lowerType,tRight,getInfoFromBQTerm(right)),subtypeConstraints);
        BQTerm newLeft = inferAllTypes(`left,tLeft);
        BQTerm newRight = inferAllTypes(`right,tRight);
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
   * to each argument in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * @param sList       a list of arguments of a list/function
   * @param tSymbol     the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   * @return            the typed list of arguments resulting
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
                //DEBUG System.out.println("InferSlotList getUnknownFreshTypeVar = " +
                //DEBUG     `argType);
              }
            }
          }
          argTerm = `inferAllTypes(argTerm,argType);
          newSList = `concSlot(PairSlotAppl(argName,argTerm),newSList*);
        }
        return newSList.reverse(); 
      }

      /* Cases CT-STAR, CT-ELEM and CT-MERGE */
      Symbol[AstName=symName,TypesToType=TypesToType[Domain=concTomType(headTTList,_*),Codomain=codomain@Type[TypeOptions=concTypeOption(_*,WithSymbol[],_*)]]] -> {
        TomTerm argTerm;
        TomSymbol argSymb;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argTerm = slot.getAppl();
          argSymb = getSymbolFromTerm(argTerm);
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            %match(argTerm) {
              VariableStar[] -> {
                /* Case CT-STAR rule (applying to premises) */
                argType = `codomain;
              }
              !VariableStar[] -> { 
                //DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
                /* Case CT-ELEM rule (applying to premises which are not lists) */
                argType = `headTTList;
              }
            }
          } else if (`symName != argSymb.getAstName()) {
            /*
             * Case CT-ELEM rule where premise is a list
             * A list with a sublist whose constructor is different
             * e.g. 
             * A = ListA(A*) | a() and B = ListB(A*)
             * ListB(ListA(a()))
             */
            argType = `headTTList;
          } 

          /* Case CT-MERGE rule (applying to premises) */
          argTerm = `inferAllTypes(argTerm,argType);
          newSList = `concSlot(PairSlotAppl(slot.getSlotName(),argTerm),newSList*);
        }
        return newSList.reverse(); 
      }

      /* Case CT-FUN rule (applying to premises) */
      Symbol[TypesToType=TypesToType[Codomain=Type[TypeOptions=!concTypeOption(_*,WithSymbol[],_*)]]] -> {
        TomTerm argTerm;
        TomName argName;
        for (Slot slot : sList.getCollectionconcSlot()) {
          argName = slot.getSlotName();
          argType = TomBase.getSlotType(tSymbol,argName);
          argTerm = `inferAllTypes(slot.getAppl(),argType);
          newSList = `concSlot(PairSlotAppl(argName,argTerm),newSList*);
          //DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
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
   * to each argument in order to infer the types of its variables.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * Continuation of CT-STAR rule (applying to premises):
   * IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "x", where "x" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-ELEM rule (applying to premises which are
   * not lists):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
   * <p>
   * Continuation of CT-MERGE rule (applying to premises which are lists with
   * the same operator):
   * IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
   * THEN infers type of both sublist "l(e1,...,en)" and last argument
   *      "e" and adds a type constraint "T <: A", where "e" represents a list with
   *      head symbol "l"
   * <p>
   * Continuation of CT-FUN rule (applying to premises):
   * IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
   * THEN infers type of arguments and add a type constraint "T <:A"
   * <p>
   * @param bqList      a list of arguments of a list/function/method
   * @param tSymbol     the TomSymbol related to the list/function
   * @param contextType the codomain of the list/function 
   * @return            the typed list of arguments resulting
   */
  private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType contextType) {

    if(tSymbol == null) {
      throw new TomRuntimeException("inferBQTermList: null symbol");
    }

    if(bqTList.isEmptyconcBQTerm()) {
      return `concBQTerm();
    }

    %match(tSymbol) {
      EmptySymbol() -> {
        BQTermList newBQTList = `concBQTerm();
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          argTerm = `inferAllTypes(argTerm,EmptyType());
          newBQTList = `concBQTerm(argTerm,newBQTList*);
        }
        return newBQTList.reverse(); 
      }

      /* Cases CT-STAR, CT-ELEM and CT-MERGE */
      Symbol[AstName=symName,TypesToType=TypesToType[Domain=concTomType(headTTList,tailTTList*),Codomain=codomain@Type[TypeOptions=concTypeOption(_*,WithSymbol[],_*)]]] -> {

        if(!`tailTTList.isEmptyconcTomType()) {
          throw new TomRuntimeException("should be empty list: " + `tailTTList);
        }

        BQTermList newBQTList = `concBQTerm();
        for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
          TomSymbol argSymb = getSymbolFromTerm(argTerm);
          TomType argType = contextType;
          if(!(TomBase.isListOperator(`argSymb) || TomBase.isArrayOperator(`argSymb))) {
            %match(argTerm) {
              Composite(_*) -> {
                /*
                 * We don't know what is into the Composite
                 * It can be a BQVariableStar or a list operator or a list of
                 * CompositeBQTerm or something else
                 */
                argType = `EmptyType();
              }

              BQVariableStar[] -> {
                /* Case CT-STAR rule (applying to premises) */
                argType = `codomain;
              }

              //TO VERIFY : which constructors must be tested here?
              /* Case CT-ELEM rule (applying to premises which are not lists) */
              (BQVariable|BQAppl)[] -> {
                argType = `headTTList;
              }
            }
          } else if (`symName != argSymb.getAstName()) {
            /*
             * Case CT-ELEM rule which premise is a list
             * A list with a sublist whose constructor is different
             * e.g. 
             * A = ListA(A*) and B = ListB(A*) | b()
             * ListB(ListA(a()))
             */
            argType = `headTTList;
          }

          /* Case CT-MERGE rule (applying to premises) */
          argTerm = `inferAllTypes(argTerm,argType);
          newBQTList = `concBQTerm(argTerm,newBQTList*);
        }
        return newBQTList.reverse(); 
      }

      /* Case CT-FUN rule (applying to premises) */
      Symbol[AstName=symName,TypesToType=TypesToType[Domain=domain,Codomain=Type[TypeOptions=!concTypeOption(_*,WithSymbol[],_*)]],PairNameDeclList=pNDList,Options=oList] -> {
        if(`pNDList.length() != bqTList.length()) {
          Option option = TomBase.findOriginTracking(`oList);
          %match(option) {
            OriginTracking(_,line,fileName) -> {
              TomMessage.error(logger,`fileName, `line,
                  TomMessage.symbolNumberArgument,`symName.getString(),`pNDList.length(),bqTList.length());
            }
            noOption() -> {
              TomMessage.error(logger,null,0,
                  TomMessage.symbolNumberArgument,`symName.getString(),`pNDList.length(),bqTList.length());
            }
          }
        } else {
          TomTypeList symDomain = `domain;
          BQTermList newBQTList = `concBQTerm();
          for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
            TomType argType = symDomain.getHeadconcTomType();
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
              * PEM 19/12/2017: Composite(_*,_,_*,_,_*) -> { argType = `EmptyType(); }
               */
              Composite(_,_,_*) -> { argType = `EmptyType(); }
            }
            argTerm = `inferAllTypes(argTerm,argType);
            newBQTList = `concBQTerm(argTerm,newBQTList*);
            symDomain = symDomain.getTailconcTomType();
            //DEBUG System.out.println("InferBQTermList CT-FUN -- end of for with bqappl = " + `argTerm);
          }
          return newBQTList.reverse(); 
        }
      }
    }
    throw new TomRuntimeException("inferBQTermList: failure on " + `bqTList);
  }

  /**
   * The method <code>solveConstraints</code> calls
   * <code>solveEquationConstraints</code> to solve the list of equation
   * constraints and generate a set of substitutions for type variables. Then
   * the substitutions are applied to the list of subtype constraints and the
   * method <code>solveEquationConstraints</code> to solve this list. 
   */
  private void solveConstraints() {
    //DEBUG System.out.println("\nsolveConstraints 1:");
    //DEBUG printGeneratedConstraints(equationConstraints);
    //DEBUG printGeneratedConstraints(subtypeConstraints);
    boolean errorFound = solveEquationConstraints(equationConstraints);
    if (!errorFound) {
      %match {
        !concTypeConstraint() << subtypeConstraints -> {
          TypeConstraintList simplifiedConstraints = replaceInSubtypingConstraints(subtypeConstraints);
          //DEBUG printGeneratedConstraints(simplifiedConstraints);
            solveSubtypingConstraints(simplifiedConstraints);
        }
      }
    }
    checkAllPatterns();
  }

  /**
   * The method <code>checkAllPatterns</code> check if all variables in patterns
   * have their types inferred. If it does not hold, then the
   * <code>equationConstraints</code> list is traversed to given the necessary
   * informations about the variables. Note that this verification is performed for
   * the <code>subtypeConstraints</code> list in the
   * <code>garbageCollecting</code> method
   */
  private void checkAllPatterns() {
    for (TomType pType : inputTVarList.getCollectionconcTomType()) {
      TomType sType = substitutions.get(pType);
      %match {
        tVar@TypeVar[] << sType &&
          concTypeConstraint(_*,Equation[Type1=tVar,Info=info],_*) << equationConstraints -> {
            `printErrorGuessMatch(info);
          } 

        tVar@TypeVar[] << sType &&
          concTypeConstraint(_*,Equation[Type2=tVar,Info=info],_*) << equationConstraints -> {
//System.out.println("checkAllPatterns tvar = " + `tVar);
//System.out.println("checkAllPatterns eq = " + equationConstraints);
            `printErrorGuessMatch(info);
          } 
      }
    }
  }

  /**
   * The method <code>solveEquationConstraints</code> tries to solve all
   * equations constraints collected during the inference process.
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
   *  a) Map(A1) does not exist   --> {(A,T1)} U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> {(A2,T1)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 6: tCList = {(T1^c = A1),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 7: tCList = {(A1 = T1),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1)} U Map 
   *  b) Map(A1) = T2             --> detectFail(T1 = T2)
   *  c) Map(A1) = A2             --> {(A2,A1)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 8: tCList = {(A1 = T1^c),...)} and Map 
   *  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
   *  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
   *  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
   *                                  then Map(A2) does not exist 
   * <p>
   * CASE 9: tCList = {(A1 = A2),...)} and Map
   *  a) Map(A1) = T1 (or T1^a) and
   *    i)    Map(A2) does not exist    --> {(A2,T1)} U Map (or {(A2,T1^a)} U Map) 
   *    ii)   Map(A2) = T2 (or T2^b)    --> detectFail(T1 = T2) (or detectFail(T1^a = T2^b))
   *    iii)  Map(A2) = A3              --> {(A3,T1)} U Map (or {(A3,T1^a)} U Map), since Map is saturated and
   *                                        then Map(A3) does not exist 
   *
   *  b) Map(A1) = A3 and
   *    i)    Map(A2) does not exist    --> {(A2,A3)} U Map 
   *    ii)   Map(A2) = T2 (or T2^b)    --> {(A3,T2)} U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *    iii)  Map(A2) = A4              --> {(A3,A4)} U Map, since Map is saturated and
   *                                        then Map(A3) does not exist 
   *  c) Map(A1) does not exist and
   *    i)    Map(A2) does not exist    --> {(A1,A2)} U Map
   *    ii)   Map(A2) = T1 (or T1^a)    --> {(A1,T1)} U Map (or {(A1,T1^a)} U Map)
   *    iii)  Map(A2) = A3              --> {(A1,A3)} U Map 
   * @param tCList the equation constraint list to be replaced
   * @return       the status of the list, if errors were found or not
   */
  private boolean solveEquationConstraints(TypeConstraintList tCList) {
    boolean errorFound = false;
    for (TypeConstraint tConstraint :
        tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
      {
        %match {
          // CASES 1, 2, 3 and 4 :
          eConstraint@Equation[Type1=groundType1@!TypeVar[],Type2=groundType2@!TypeVar[]] <<
            tConstraint && (groundType1 != groundType2) -> {
              //DEBUG System.out.println("In solveEquationConstraints:" + `groundType1 +
              //DEBUG     " = " + `groundType2);
              errorFound = (errorFound || `detectFail(eConstraint));
              break matchBlockAdd;
            }
          // CASES 5 and 6 :
          Equation[Type1=groundType@!TypeVar[],Type2=typeVar@TypeVar[],Info=info] <<
            tConstraint -> {
              if (substitutions.containsKey(`typeVar)) {
                TomType mapTypeVar = substitutions.get(`typeVar);
                if (!isTypeVar(mapTypeVar)) {
                  errorFound = (errorFound || 
                      `detectFail(Equation(groundType,mapTypeVar,info)));
                } else {
                  // if (isTypeVar(mapTypeVar))
                  substitutions.addSubstitution(mapTypeVar,`groundType);
                }
              } else {
                substitutions.addSubstitution(`typeVar,`groundType);
              }
              break matchBlockAdd;
            }

          // CASES 7 and 8 :
          Equation[Type1=typeVar@TypeVar[],Type2=groundType@!TypeVar[],Info=info] << tConstraint -> {
            if (substitutions.containsKey(`typeVar)) {
              TomType mapTypeVar = substitutions.get(`typeVar);
              if (!isTypeVar(mapTypeVar)) {
                errorFound = (errorFound || 
                    `detectFail(Equation(mapTypeVar,groundType,info)));
              } else {
                // if (isTypeVar(mapTypeVar))
                substitutions.addSubstitution(mapTypeVar,`groundType);
              }
            } else {
              substitutions.addSubstitution(`typeVar,`groundType);
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
                  substitutions.addSubstitution(mapTypeVar1,mapTypeVar2);
                } else {
                  if (isTypeVar(mapTypeVar2)) {
                    substitutions.addSubstitution(mapTypeVar2,mapTypeVar1);
                  } else {
                    errorFound = (errorFound || 
                        `detectFail(Equation(mapTypeVar1,mapTypeVar2,info)));
                  }
                }
                break matchBlockAdd;
              } else if (substitutions.containsKey(`typeVar1)) {
                mapTypeVar1 = substitutions.get(`typeVar1);
                substitutions.addSubstitution(`typeVar2,mapTypeVar1);
                break matchBlockAdd;
              } else if (substitutions.containsKey(`typeVar2)){
                mapTypeVar2 = substitutions.get(`typeVar2);
                substitutions.addSubstitution(`typeVar1,mapTypeVar2);
                break matchBlockAdd;
              } else {
                substitutions.addSubstitution(`typeVar1,`typeVar2);
                break matchBlockAdd;
              }
            }
        }
      }
    }
    return errorFound;
  }

  /**
   * The method <code>detectFail</code> checks if a type constraint
   * relating two ground types has a solution. In the negative case, an
   * 'incompatible types' message error is printed.  
   * <p> 
   * There exists 2 kinds of ground types: ground types Ti and
   * ground types Ti^c which are decorated with a given symbol c. Considering
   * type constraints as pairs (type1,type2) representing an equation or
   * a subtype relation between two ground types, them the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: tCList = {(T1 = T2),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 2: tCList = {(T1 = T2^c),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 3: tCList = {(T1^c = T2),...)} and Map
   *  a) --> true if T1 is different from T2
   *  b) --> false if T1 is equals to T2
   * <p>
   * CASE 4: tCList = {(T1^a = T2^b),...)} and Map
   *  a) --> true if T1 is different from T2 or 'a' is different from 'b'
   *  b) --> false if T1 is equals to T2 and 'a' is equals to 'b'
   * <p>
   * CASE 5: tCList = {(T1 <: T2),...)} and Map
   *  a) --> true if T1 is not subtype of T2
   *  b) --> false if T1 is subtype of T2
   * <p>
   * CASE 6: tCList = {(T1 <: T2^c),...)} and Map
   *   --> true
   * <p>
   * CASE 7: tCList = {(T1^c <: T2),...)} and Map
   *  a) --> true if T1 is not subtype of T2
   *  b) --> false if T1 is subtype of T2
   * <p>
   * CASE 8: tCList = {(T1^a <: T2^b),...)} and Map
   *  a) --> true if T1 is not subtype of T2 or 'a' is different from 'b'
   *  b) --> false if T1 is subtype of T2 and 'a' is equals to 'b'
   * <p>
   * @param tConstraint the type constraint to be verified 
   * @return            the status of the list, if errors were found or not
   */
  private boolean detectFail(TypeConstraint tConstraint) {
    %match {
      /* CASES 1a, 2a and 3a */
      Equation[Type1=Type[TomType=tName1],Type2=Type[TomType=tName2@!tName1]]
        << tConstraint && (tName1 != "unknown type") && (tName2 != "unknown type")  -> {
          printErrorIncompatibility(`tConstraint);
          return true;
        }

      /* CASE 4a */  
      Equation[Type1=Type[TypeOptions=tOptions1,TomType=tName1],Type2=Type[TypeOptions=tOptions2@!tOptions1,TomType=tName1]]
        << tConstraint
        && concTypeOption(_*,WithSymbol[RootSymbolName=rsName1],_*) << tOptions1
        && concTypeOption(_*,WithSymbol[RootSymbolName=rsName2@!rsName1],_*) << tOptions2 -> {
          printErrorIncompatibility(`tConstraint);
          return true;
        }

      Subtype[Type1=t1,Type2=t2@!t1] << tConstraint -> {
        if (!isSubtypeOf(`t1,`t2)) {
          printErrorIncompatibility(`tConstraint);
          return true;
        }
      }
    }
    return false;
  }

  /**
   * The method <code>replaceInSubtypingConstraints</code> applies the
   * substitutions to a given type constraint list.
   * @param tCList the type constraint list to be replaced
   * @return       the type constraint list resulting of replacement
   */
  private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList tCList) {
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
          if (mapT1 != mapT2) {
            replacedtCList = `addConstraintSlow(Subtype(mapT1,mapT2,info),replacedtCList);
          }
        }
      }

    }
    return replacedtCList;
  }


  /**
   * The method <code>solveSubtypingConstraints</code> do constraint propagation
   * by calling simplification subtyping constraints algorithms to solve the list of
   * subtyping constraints. Then if no errors were found, another algorithm is
   * called in order to generate solutions for the list. This combination of
   * algorithms is done until the list is empty.
   * Otherwise, the simplification is stopped and all error messages are printed  
   * <p>
   * Propagation of constraints:
   *  - PHASE 1: Simplification in equations
   *  - PHASE 2: Reduction in closed form
   *  - PHASE 3: Verification of type inconsistencies 
   *  - PHASE 4: Canonization
   * Generation of solutions
   * Garbage collection: remove typeVars which are not input types 
   * @param tCList  the subtyping constraint list to be replaced
   * @return        the empty solved list or the list that has no solutions
   */
  private void solveSubtypingConstraints(TypeConstraintList tCList) {
    TypeConstraintList solvedConstraints = tCList;
    TypeConstraintList simplifiedConstraints = `concTypeConstraint();
tryBlock:
    {
      try {
        solvedConstraints = `RepeatId(simplificationAndClosure(this)).visitLight(solvedConstraints);
        //calculatePolarities(simplifiedConstraints);
        //solvedConstraints = simplifiedConstraints;
        //while (!solvedConstraints.isEmptyconcTypeConstraint()){
        while (solvedConstraints != simplifiedConstraints) {
          simplifiedConstraints = incompatibilityDetection(solvedConstraints);
          //System.out.println("list0.size = " + simplifiedConstraints.length());
          %match {
            concTypeConstraint(_*,FalseTypeConstraint(),_*) << simplifiedConstraints -> {
              //DEBUG System.out.println("Error!!");
              break tryBlock;
            }
          }

          TypeConstraintList tmpsimplifiedConstraints = simplifiedConstraints;
          //System.out.println("list1.size = " + simplifiedConstraints.length());
          simplifiedConstraints = computeCanonization(simplifiedConstraints);
          //System.out.println("list2.size = " + simplifiedConstraints.length());
          //System.out.println("list2 = " + simplifiedConstraints);

          //tmpsimplifiedConstraints = `RepeatId(applyCanonization(this)).visitLight(tmpsimplifiedConstraints);
          //System.out.println("list3.size = " + tmpsimplifiedConstraints.length());
          //System.out.println("list3 = " + tmpsimplifiedConstraints);

          //if(simplifiedConstraints.length() != tmpsimplifiedConstraints.length()) {
            //System.out.println("list2.size = " + simplifiedConstraints.length());
            //System.out.println("list2 = " + simplifiedConstraints);
            //System.out.println("list3.size = " + tmpsimplifiedConstraints.length());
            //System.out.println("list3 = " + tmpsimplifiedConstraints);
          //}

          solvedConstraints = generateSolutions(simplifiedConstraints);
          //System.out.println("list4.size = " + solvedConstraints.length());
        }
        garbageCollection(solvedConstraints);
        //DEBUG System.out.println("\nResulting subtype constraints!!");
        //DEBUG printGeneratedConstraints(solvedConstraints);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("solveSubtypingConstraints: failure on " +
            solvedConstraints);
      }
    }
  }

  /**
   * The method <code>simplificationAndClosure</code> is generated by a
   * strategy which simplifies a subtype constraints list.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * PHASE 1: Simplification in equations (apply anti-symetry of the partial
   * order "<:"): 
   * tCList = {T1 <: T2, T2 <: T1} U tCList' and Map 
   *  --> solveEquationConstraints(T1 = T2) U tCList' and Map
   * tCList = {A1 <: A2, A2 <: A1} U tCList' and Map 
   *  --> solveEquationConstraints(A1 = A2) U tCList' and Map
   * <p>
   * PHASE 2: Reduction in closed form (apply transitivity of the partial
   * order "<:"):
   * tCList = {T1 <: A,A <: T2} U tCList' and Map --> {T1 <: T2} U tCList and Map
   * tCList = {A1 <:A,A <: A2} U tCList' and Map --> {A1 <: A2} U tCList and Map
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
  %strategy simplificationAndClosure(nkt:NewKernelTyper) extends Identity() {
    visit TypeConstraintList {
      /* PHASE 1 */
      tcl@concTypeConstraint(tcl1*,Subtype[Type1=t1,Type2=t2,Info=info],tcl2*,Subtype[Type1=t2,Type2=t1],tcl3*) -> {
        nkt.solveEquationConstraints(`concTypeConstraint(Equation(t1,t2,info)));
        return `concTypeConstraint(tcl1*,tcl2*,tcl3*);
      }

      /*
       * We test if "t1 <: t2" already exist in tcl to avoid apply the strategy
       * unnecessarily
       */
      /* PHASE 2 */
      tcl@concTypeConstraint(_*,Subtype[Type1=t1,Type2=tVar@TypeVar[]],_*,Subtype[Type1=tVar,Type2=t2,Info=info],_*)
        && !concTypeConstraint(_*,Subtype[Type1=t1,Type2=t2],_*) << tcl -> {
          //DEBUG System.out.println("\nsolve2a: tcl =" + `tcl);
          return
            nkt.addConstraintSlow(`Subtype(t1,t2,info),`tcl);
        }
      tcl@concTypeConstraint(_*,Subtype[Type1=tVar@TypeVar[],Type2=t2,Info=info],_*,Subtype[Type1=t1,Type2=tVar],_*)
        && !concTypeConstraint(_*,Subtype[Type1=t1,Type2=t2],_*) << tcl -> {
          //DEBUG System.out.println("\nsolve2b: tcl = " + `tcl);
          return
            nkt.addConstraintSlow(`Subtype(t1,t2,info),`tcl);
        }
    }
  }

  /**
   * The method <code>incompatibilityDetection</code> verify if there are type
   * inconsistencies. This is done for each type constraint of tCList.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * tCList = {T1 <: T2} U tCList' and Map --> detectFail(T1 <: T2) U tCList and Map
   * Note that the constraint "{T1 <: T2}" must be keeped in tCList to avoid an
   * infinity loop when re-applying <code>simplificationAndClosure</code> method.
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
  private TypeConstraintList incompatibilityDetection(TypeConstraintList tCList) {
    /* PHASE 3 */
    boolean errorFound = false;
    TypeConstraintList simplifiedtCList = `concTypeConstraint();
    for(TypeConstraint tConstraint : tCList.getCollectionconcTypeConstraint()) {
matchBlock :
      {
        %match {
          Subtype[Type1=!TypeVar[],Type2=!TypeVar[]] << tConstraint -> {
            //DEBUG System.out.println("\nsolve3: tConstraint=" + `tConstraint);
            errorFound = (errorFound || detectFail(`tConstraint));
            break matchBlock;
          }
          /*_ << tConstraint -> {
            simplifiedtCList = `concTypeConstraint(tConstraint,simplifiedtCList*);
          }*/
        }
        simplifiedtCList = `concTypeConstraint(tConstraint,simplifiedtCList*);
      }
    }
    if (errorFound) { 
      simplifiedtCList = `concTypeConstraint(FalseTypeConstraint(),simplifiedtCList*);
    }
    return simplifiedtCList;
  }

  /**
   * The method <code>applyCanonization</code> is generated by a
   * strategy which simplifies a subtype constraints list by looking for exactly
   * one non-variable lower and one non-variable upper bound for each type
   * variable, i.e. put tCList in a canonical form.
   * <p>
   * Let 'Ai' and 'Ti' be type variables and ground types, respectively:
   * <p>
   * - Upper bound:
   * tCList = {A <: T1, A <:T2} U tCList' and Map 
   *      --> {A <: T1} U tCList' and Map
   *       if T1 <: T2 
   *      --> {A <: T2} U tCList' and Map
   *       if T2 <: T1
   *      --> {False} U tCList' and Map
   *       if T1 and T2 are not comparable
   * - Lower bound:
   * tCList = {T1 <: A, T2 <: A} U tCList' and Map
   *      --> {T <: A} U tCList' and Map
   *       if there exists T such that T is the lowest upper bound of T1 and T2
   *      --> {False} U tCList' and Map
   *       if there does not exist T such that T is the lowest upper bound of T1 and T2
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */

  private TypeConstraintList computeCanonization(TypeConstraintList subject) {
    if(subject.isEmptyconcTypeConstraint()) {
      return subject;
    }
    List<TypeConstraint> list = new ArrayList<TypeConstraint>();
    
    for(TypeConstraint tc: ((tom.engine.adt.typeconstraints.types.typeconstraintlist.concTypeConstraint)subject)) {
      list.add(tc);
    }

    Collections.sort(list,new ConstraintComparator());
    //for(TypeConstraint tc: list) { System.out.println(tc); }

    boolean finished = false;
    int index = 0;
    while(!finished) {
      if(list.size()>=index+2) {
        TypeConstraint first = list.get(index);
        TypeConstraint second = list.get(index+1);

        %match(first,second) {
          Subtype[Type1=tVar@TypeVar[],Type2=t1@!TypeVar[],Info=info], 
          Subtype[Type1=tVar,Type2=t2@!TypeVar[]] -> {
            TomType lowerType = `minType(t1,t2);
            if (lowerType == `EmptyType()) {
              printErrorIncompatibility(`Subtype(t1,t2,info));
              list.remove(index);
              list.set(index,`FalseTypeConstraint());
              //System.out.println("remove1: " + index);
            } else {
              list.remove(index);
              list.set(index,`Subtype(tVar,lowerType,info));
              //System.out.println("remove2: " + index);
            }
          }

          Subtype[Type1=t1@!TypeVar[],Type2=tVar@TypeVar[],Info=info], 
          Subtype[Type1=t2@!TypeVar[],Type2=tVar] -> {
            TomType supType = `supType(t1,t2);
            if (supType == `EmptyType()) {
              printErrorIncompatibility(`Subtype(t1,t2,info));
              list.remove(index);
              list.set(index,`FalseTypeConstraint());
              //System.out.println("remove3: " + index);
            } else {
              list.remove(index);
              list.set(index,`Subtype(supType,tVar,info));
              //System.out.println("remove4: " + index);
            }
          }

        }

        if(first == list.get(index)) {
          if(index+1<list.size() && second == list.get(index+1)) {
            index++;
            //System.out.println("index++: " + index);
          }
        }

      } else { 
        finished = true;
      }

    }

    TypeConstraintList result = `concTypeConstraint();
    for(TypeConstraint tc: list) {
      result = `concTypeConstraint(tc,result*);
    }
    return result;
  }

  private class ConstraintComparator implements Comparator {

    public int compare(Object o1,Object o2) {
      TypeConstraint t1 = (TypeConstraint)o1;
      TypeConstraint t2 = (TypeConstraint)o2;
      int result = 0;
block: {
      %match(t1,t2) {
          Subtype[Type1=TypeVar(name1,index1), Type2=st1@!TypeVar[]], 
          Subtype[Type1=TypeVar(name2,index2), Type2=st2@!TypeVar[]] -> {
            int res = `name1.compareTo(`name2);
            if(res==0) {
              result = `index2-`index1;
              if(result==0) {
                result = `st1.compareToLPO(`st2);
              }
              break block;
            } else {
              result = res;
              break block;
            }
          }

          Subtype[Type1=st1@!TypeVar[], Type2=TypeVar(name1,index1)], 
          Subtype[Type1=st2@!TypeVar[], Type2=TypeVar(name2,index2)] -> {
            int res = `name1.compareTo(`name2);
            if(res==0) {
              result = `index2-`index1;
              if(result==0) {
                result = `st1.compareToLPO(`st2);
              }
              break block;
            } else {
              result = res;
              break block;
            }
          }
          
          Subtype[Type1=TypeVar[],Type2=!TypeVar[]], 
          Subtype[Type1=TypeVar[],Type2=TypeVar[]] -> {
            result = -1;
            break block;
          }
          Subtype[Type1=TypeVar[],Type2=TypeVar[]], 
          Subtype[Type1=TypeVar[],Type2=!TypeVar[]] -> {
            result = +1;
            break block;
          }

          Subtype[Type1=TypeVar[],Type2=!TypeVar[]], 
          Subtype[Type1=!TypeVar[],Type2=_] -> {
            result = -1;
            break block;
          }
          Subtype[Type1=!TypeVar[],Type2=_],
          Subtype[Type1=TypeVar[],Type2=!TypeVar[]] -> {
            result = +1;
            break block;
          }
      }
      result = t1.compareToLPO(t2);
       } // end block

       //System.out.println(t1 + " <--> \n" + t2 + "  = " + result);
       return result;

    }
  }

  /*
   * this is dead code, just for debbuging purpose
   * compring results with computeCanonization
   */
  %strategy applyCanonization(nkt:NewKernelTyper) extends Identity() {
    visit TypeConstraintList {
      /* PHASE 4 */

      concTypeConstraint(tcl1*,c1@Subtype[],tcl2*,c2@Subtype[],tcl3*) -> {
        %match(c1,c2) {
          Subtype[Type1=tVar@TypeVar[],Type2=t1@!TypeVar[],Info=info], Subtype[Type1=tVar,Type2=t2@!TypeVar[]] -> {

            TomType lowerType = nkt.minType(`t1,`t2);

            if (lowerType == `EmptyType()) {
              nkt.printErrorIncompatibility(`Subtype(t1,t2,info));
              return `concTypeConstraint(FalseTypeConstraint(),tcl1*,tcl2*,tcl3*); 
            }

            return nkt.addConstraintSlow(`Subtype(tVar,lowerType,info),`concTypeConstraint(tcl1*,tcl2*,tcl3*));
          }

          Subtype[Type1=t1@!TypeVar[],Type2=tVar@TypeVar[],Info=info], Subtype[Type1=t2@!TypeVar[],Type2=tVar] -> {
            TomType supType = nkt.supType(`t1,`t2);

            if (supType == `EmptyType()) {
              nkt.printErrorIncompatibility(`Subtype(t1,t2,info));
              return `concTypeConstraint(FalseTypeConstraint(),tcl1*,tcl2*,tcl3*); 
            }

            return nkt.addConstraintSlow(`Subtype(supType,tVar,info),`concTypeConstraint(tcl1*,tcl2*,tcl3*));
          }
        }
      }

    }
  }

  /**
   * The method <code>minType</code> tries to find the common lowertype
   * between two given ground types.
   * @param t1 a type
   * @param t2 another type
   * @return    the lowertype between 't1' and 't2' if the subtype relation
   *            holds between them (since a multiple inheritance is forbidden)
   *            the 'EmptyType()' otherwise
   */
  private TomType minType(TomType t1, TomType t2) {
    //DEBUG System.out.println("minType: t1 = " + t1 + " and t2 = " + t2);
    //DEBUG System.out.println("minType: isSubtypeOf(t1,t2) = " + isSubtypeOf(t1,t2));
    if (isSubtypeOf(t1,t2)) { return t1; } 
    if (isSubtypeOf(t2,t1)) { return t2; }
    //DEBUG System.out.println("case 3");
    return `EmptyType();
  }

  /**
   * The method <code>isSubtypeOf</code> checks if type t1 is a subtype of type
   * t2 considering symmetry and type decorations of both t1 and t2.
   * <p> 
   * There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
   * ground types Ti^c which are decorated with a given symbol c. Considering a
   * partial order over ground types "<:" as a binary relation where "T1^c1 <: T2^c2"
   * is equivalent to "T1 is subtype of T2 and (c1 == c2 or c2 == ?)", then the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: T1^? <: T2^?
   *  a) --> true if T1 == T2 or T1 is a proper subtype of T2
   *  b) --> false otherwise
   * <p>
   * CASE 2: T1^? <: T2^c
   *   --> false
   * <p>
   * CASE 3: T1^c <: T2^?
   *  a) --> true if T1 == T2 or T1 is a proper subtype of T2
   *  b) --> false otherwise
   * <p>
   * CASE 4: T1^c1 <: T2^c2
   *  a) --> true if 'a' is equals to 'b' and (T1 == T2 or T1 is a proper
   *  subtype of T2)
   *  b) --> false otherwise
   * <p>
   * @param t1 a ground (decorated) type
   * @param t2 another ground (decorated) type
   * @return   true if t1 <: t2 and false otherwise
   */
  private boolean isSubtypeOf(TomType t1, TomType t2) {
    //DEBUG System.out.println("isSubtypeOf: t1 = " + t1 + " and t2 = " + t2);
    %match {
      Type[TypeOptions=tOptions1,TomType=tName1] << t1  
   && Type[TypeOptions=tOptions2,TomType=tName2] << t2 -> {
        TomTypeList supTypet1 = dependencies.get(`tName1);
        %match {
          // CASES 1a and 3a
          !concTypeOption(_*,WithSymbol[],_*) << tOptions2 && (tName1 == tName2) -> {
              //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - i");
              return true; 
            }
          !concTypeOption(_*,WithSymbol[],_*) << tOptions2 && concTomType(_*,Type[TomType=tNameSup],_*) << supTypet1 && (tName2 == tNameSup)-> {
              //DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - ii");
              return true; 
            }

          // CASE 4a
          concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) << tOptions1 && concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) << tOptions2 && (tName1 == tName2) -> {
              //DEBUG System.out.println("isSubtypeOf: case 4a - i");
              return true; 
            }
          concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) << tOptions1
            && concTypeOption(_*,WithSymbol[RootSymbolName=rsName],_*) << tOptions2 
            && concTomType(_*,Type[TomType=tNameSup],_*) <<
            supTypet1 && (tName2 == tNameSup) -> {
              //DEBUG System.out.println("isSubtypeOf: case 4a - ii");
              return true; 
            }
        }
        return false;
      }
    }
    // Remain cases
    return false;
  }

  /**
   * The method <code>supType</code> tries to find the lowest common uppertype
   * of two given ground types.
   * <p>
   * There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
   * ground types Ti^c which are decorated with a given symbol c. Considering
   * them by pairs, then the set of all possibilities of arrangement between
   * ground types is a sequence with repetition. Then, we have 4 possible cases (since
   * 2^2 = 4).
   * <p>
   * CASE 1: T1^? and  T2^?
   *  a) --> T2^? if T1 is a subtype of T2
   *  b) --> T1^? if T2 is a subtype of T1
   *  c) --> emptyType otherwise
   * <p>
   * CASE 2: T1^? and T2^c
   *  a) --> T1^? if T2 is a subtype of T1
   *  b) --> T3^? if T3 is the join between T1 and T2
   *  c) --> emptyType otherwise
   * <p>
   * CASE 3: T1^c and T2^? 
   *  similar to CASE 2.
   * <p>
   * CASE 4: T1^c1 and T2^c2
   *  - 4.1 : T1 == T2
   *      --> T1^?
   *  - 4.2 : T1 != T2
   *      a) --> T2^c1 if T1 is a subtype of T2 and c1 == c2
   *      b) --> T1^c1 if T2 is a subtype of T1 and c1 == c2
   *      c) --> T3^? if T3 is the join between T1 and T2 
   *      d) --> emptyType otherwise
   * <p>
   * OBS.: when the subtype relation does not hold between two types 'T1' and
   * 'T2', the method considers the intersection of the supertypes lists
   * supertypes_T1 and supertypes_T2 and searches for the 'immediate' common
   * supertype 'Ti' (i.e. the one which has the bigger supertypes_Ti list) 
   * @param t1  a type
   * @param t2  another type
   * @return    the uppertype between 't1' and 't2' if the subtype relation
   *            holds between them or the lowest common uppertype of them if
   *            they are not in subtype relation 
   *            the 'EmptyType()' otherwise
   */
  private TomType supType(TomType t1, TomType t2) {
    /* CASES 1a, 1b, 2a, 3a, 4.2a and 4.2b */
    if (isSubtypeOf(t1,t2)) { return t2; } 
    if (isSubtypeOf(t2,t1)) { return t1; }
    TomTypeList supTypes1 = dependencies.get(t1.getTomType());
    TomTypeList supTypes2 = dependencies.get(t2.getTomType());
    %match {
      /* CASE 4.1 */
      Type[TypeOptions=concTypeOption(_*,WithSymbol[RootSymbolName=rsName1],_*),TomType=tName] << t1 
        &&
        Type[TypeOptions=concTypeOption(_*,WithSymbol[RootSymbolName=!rsName1],_*),TomType=tName] << t2 -> {
          //DEBUG System.out.println("\nIn supType: case 4.1");
          // Return the equivalent groudn type without decoration
          return symbolTable.getType(`tName); 
        }

      /* CASES 2b, 3b and 4.2c */
      !concTomType() << supTypes1 && !concTomType() << supTypes2 -> {
        int st1Size = `supTypes1.length();
        int st2Size = `supTypes2.length();

        int currentIntersectionSize = -1;
        int commonTypeIntersectionSize = -1;
        TomType lowestCommonType = `EmptyType();
        //DEBUG System.out.println("\nIn supType: cases 2b, 3b, 4.2c");
        for (TomType currentType:`supTypes1.getCollectionconcTomType()) {
          currentIntersectionSize = dependencies.get(currentType.getTomType()).length();
          if (`supTypes2.getCollectionconcTomType().contains(currentType) &&
              (currentIntersectionSize > commonTypeIntersectionSize)) {
            commonTypeIntersectionSize = currentIntersectionSize;
            lowestCommonType = currentType;
          }
        }
        return lowestCommonType;
      }
    }
    /* Remaining CASES */
    return `EmptyType();
  }

  /**
   * The method <code>generateSolutions</code> chooses a solution for a subtype
   * constraint list when many possibilities are available. The algorithm picks
   * the lowest possible upper bound proposed for each type variable. For this
   * reason, the order of rules is important.
   * <p>
   * A subtype constraint can be written by 3 formats:
   * F1 - A <: T
   * F2 - T <: A
   * F3 - A1 <: A2
   * where 'Ai' and 'Ti' are respectively type variables and ground types (and
   * the constraint 'T1 <: T2' was already handled by garbage collecting in
   * <code>solveSubtypeConstraints</code> method.
   * These constraints are enumerated by cases:
   * <p>
   * CASE 1:
   * tCList = {A <: T} U tCList' and Map 
   *  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
   * <p>
   * CASE 2:
   * tCList = {T <: A} U tCList' and Map 
   *  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
   * <p>
   * CASE 3:
   * tCList = {A1 <: A2} U tCList' and Map 
   *  -->  tCList = tCList' and {(A1,A2)} U Map if Ai are not in tCList'
   * <p>
   * Considering combination of {F1 x F3}, {F2 x F3}, {F3 x F3} and {F1 x F2} (since 
   * {F1 x F1} and {F2 x F2} are treated by PHASE 4 of
   * <code>solveSubtypingConstraints</code> or by previous cases):
   * <p>
   * CASE 4 {F1 x F3} and {F3 x F1}:
   *    a) tCList = {A <: T, A <: A1} U tCList' and Map
   *      -->  tCList = {T <: A1} U [A/T]tCList' and {(A,T)} U Map
   *    TODO : verify rule b
   *    b) tCList = {A <: T, A1 <: A} U tCList' and Map 
   *      --> tCList = tClist' and Map or do nothing?
   *    c) tCList = {A <: T, A1 <: A2} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 3 for A1 <: A2 
   * <p>
   * CASE 5 {F2 x F3} and {F3 x F2}:
   *    a) tCList = {T <: A, A1 <: A} U tCList' and Map 
   *      -->  tCList = {A1 <: T} U [A/T]tCList' and {(A,T)} U Map
   *    TODO : verify rule b
   *    b) tCList = {T <: A, A <: A1} U tCList' and Map 
   *      --> tCList = tClist' and Map or do nothing?
   *    c) tCList = {T <: A, A1 <: A2} U tCList' and Map 
   *      --> CASE 2 for T <: A and CASE 3 for A1 <: A2
   * <p>
   * CASE 6 {F1 x F2} and {F2 x F1}:
   *    a) tCList = {A <: T, T <: A1} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 2 for T <: A1 
   *    b) tCList = {A <: T, T1 <: A1} U tCList' and Map 
   *      --> CASE 1 for A <: T and CASE 2 for T1 <: A1 
   *    c) tCList = {A <: T, T1 <: A} U tCList' and Map 
   *      -->  tCList = {T1 <: T} U [A/T]tCList' and {(A,T)} U Map
   *     Note: in this case T1 is different from T (since the case where T1 is
   *     equals to T is treated by PHASE 1 of
   *     <code>solveSubtypingConstraints</code>.
   * <p>
   * CASE 7 {F3 x F3}:
   *    a) tCList = {A <: A1, A <: A2} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    b) tCList = {A <: A1, A2 <: A3} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    c) tCList = {A1 <: A, A2 <: A} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   *    d) tCList = {A1 <: A, A <: A2} U tCList' and Map 
   *      --> Nothing, java must infer the right type 
   * <p>
   * @param nkt an instance of object NewKernelTyper
   * @return    the subtype constraint list resulting
   */
   private TypeConstraintList generateSolutions(TypeConstraintList tCList) {
     TypeConstraintList newtCList = tCList;
matchBlockSolve :
     {
       %match(tCList) {
         /* CASE 1 */
         concTypeConstraint(leftTCL*,c1@Subtype[Type1=tVar@TypeVar[],Type2=groundType@!TypeVar[]],rightTCL*) -> {
           // Same code of cases 7 and 8 of solveEquationConstraints
           substitutions.addSubstitution(`tVar,`groundType);
           newtCList = `replaceInSubtypingConstraints(concTypeConstraint(leftTCL*,rightTCL*));
           break matchBlockSolve;
         }

         /* CASE 2 */
         concTypeConstraint(leftTCL*,c1@Subtype[Type1=groundType@!TypeVar[],Type2=tVar@TypeVar[]],rightTCL*) -> {
           substitutions.addSubstitution(`tVar,`groundType);
           newtCList = `replaceInSubtypingConstraints(concTypeConstraint(leftTCL*,rightTCL*));
           break matchBlockSolve;
         }
       }
     }
     return newtCList;
   }

  private void garbageCollection(TypeConstraintList tCList) {
    TypeConstraintList newtCList = tCList;
    %match(tCList) {
      /* CASE 3 */
      concTypeConstraint(_*,Subtype[Type1=tVar1@TypeVar[],Type2=tVar2@TypeVar[],Info=info],_*) -> {
        %match {
          concTomType(_*,tVar,_*) << inputTVarList && 
            (tVar == tVar1 || tVar == tVar2) -> {
            `printErrorGuessMatch(info);
            newtCList = `concTypeConstraint(FalseTypeConstraint(),newtCList*);
          }
        }
      }
    }
  }

  private void printErrorGuessMatch(Info info) {
    %match {
      PairNameOptions(Name(termName),optionList) << info -> {
        Option option = TomBase.findOriginTracking(`optionList);
        %match(option) {
          OriginTracking(_,line,fileName) -> {
            TomMessage.error(logger,`fileName, `line,
                TomMessage.cannotGuessMatchType,`termName); 
          }
          noOption() -> {
            TomMessage.error(logger,null,0,
                TomMessage.cannotGuessMatchType,`termName); 
          }
        }
      }
    }
  }

  /**
   * The method <code>isTypeVar</code> checks if a given type is a type variable.
   * @param type the type to be checked
   * @return     'true' if teh type is a variable type
   *             'false' otherwise
   */
  private boolean isTypeVar(TomType type) {
    %match(type) {
      TypeVar[] -> { return true; }
    }
    return false;
  }

  /**
   * The method <code>printErrorIncompatibility</code> prints an 'incompatible types' message
   * enriched by informations about a given type constraint.
   * @param tConstraint  the type constraint to be printed
   */
  private void printErrorIncompatibility(TypeConstraint tConstraint) {
    %match {
      (Equation|Subtype)[Type1=tType1,Type2=tType2,Info=info] << tConstraint &&
        Type[TomType=tName1] << tType1 &&
        Type[TomType=tName2] << tType2 &&
        PairNameOptions(Name(termName),optionList) << info
        -> {
          Option option = TomBase.findOriginTracking(`optionList);
          %match(option) {
            OriginTracking(_,line,fileName) -> {
              TomMessage.error(logger,`fileName, `line,
                  TomMessage.incompatibleTypes,`tName1,`tName2,`termName); 
            }
            noOption()-> {
              TomMessage.error(logger,null,0,
                  TomMessage.incompatibleTypes,`tName1,`tName2,`termName); 
            }
          }
        }
    }
  }

  /**
   * The method <code>replaceInCode</code> calls the strategy
   * <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
   * a given Code.
   * @param code the code to be replaced
   * @return     the replaced code resulting
   */
  private Code replaceInCode(Code code) {
    try {
      code = `InnermostId(replaceFreshTypeVar(this)).visitLight(code);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInCode: failure on " + code);
    }
    return code;
  }

  /**
   * The method <code>replaceInSymbolTable</code> calls the strategy
   * <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
   * the Symbol Table.
   */
  private void replaceInSymbolTable() {
    for(String tomName:symbolTable.keySymbolIterable()) {
      TomSymbol tSymbol = getSymbolFromName(tomName);
      try {
        tSymbol = `InnermostId(replaceFreshTypeVar(this)).visitLight(tSymbol);
      } catch(tom.library.sl.VisitFailure e) {
        throw new TomRuntimeException("replaceInSymbolTable: failure on " + tSymbol);
      }
      symbolTable.putSymbol(tomName,tSymbol);
    }
  }

  /**
   * The class <code>replaceFreshTypeVar</code> is generated from a strategy
   * which replace each type variable occurring in a given expression by its corresponding substitution. 
   * @param nkt an instance of object NewKernelTyper
   * @return    the expression resulting of a transformation
   */
  %strategy replaceFreshTypeVar(nkt:NewKernelTyper) extends Identity() {
    visit TomType {
      typeVar@TypeVar[] -> {
        if(nkt.substitutions.containsKey(`typeVar)) {
          return nkt.substitutions.get(`typeVar);
        } 
      }
    }
  }

  /**
   * The method <code>printGeneratedConstraints</code> prints braces and calls the method
   * <code>printEachConstraint</code> for a given list.
   * @param tCList the type constraint list to be printed
   */
  public void printGeneratedConstraints(TypeConstraintList tCList) {
    %match(tCList) {
      !concTypeConstraint() -> { 
        System.out.print("\n------ Type Constraints : \n {");
        printEachConstraint(tCList);
        System.out.print("}");
      }
    }
  }

  /**
   * The method <code>printEachConstraint</code> prints symbols '=' and '<:' and print
   * each type occurring in a given type constraint.
   * @param tCList the type constraint list to be printed
   */
  public void printEachConstraint(TypeConstraintList tCList) {
    %match(tCList) {
      concTypeConstraint(Equation(type1,type2,_),tailtCList*) -> {
        System.out.print(`type1);
        System.out.print(" = ");
        System.out.print(`type2);
        if(`tailtCList != `concTypeConstraint()) {
          System.out.print(", "); 
          printEachConstraint(`tailtCList);
        }
      }

      concTypeConstraint(Subtype(type1,type2,_),tailtCList*) -> {
        System.out.print(`type1);
        System.out.print(" <: ");
        System.out.print(`type2);
        if(`tailtCList != `concTypeConstraint()) {
          System.out.print(", "); 
          printEachConstraint(`tailtCList);
        }
      }
    }
  }

} // NewKernelTyper
