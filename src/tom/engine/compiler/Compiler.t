/*
 * 
 * TOM - To One Matching Expander
 * 
 * Copyright (c) 2000-2016, Universite de Lorraine, Inria
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

import tom.engine.tools.TomGenericPlugin;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.code.types.bqterm.BQVariable;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.library.sl.*;
import tom.engine.TomBase;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import java.util.*;

import tom.engine.tools.ASTFactory;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.platform.OptionParser;
import tom.engine.tools.Tools;
import java.util.logging.Level;
import tom.engine.TomMessage;

/**
 * Tom compiler based on constraints.
 * 
 * It controls different phases of compilation:
 * - propagation of constraints
 * - instruction generation from constraints
 * - ...   
 */
public class Compiler extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/ArrayList.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/HashSet.tom}

  %typeterm Compiler { implement { Compiler } }

  private static final String freshVarPrefix = "_freshVar_";
  private static final String freshBeginPrefix = "_begin_";
  private static final String freshEndPrefix = "end";

  private CompilerEnvironment compilerEnvironment;

  public CompilerEnvironment getCompilerEnvironment() {
    return compilerEnvironment;
  }

  private class CompilerEnvironment {

    /** few attributes */
    private SymbolTable symbolTable;
    private TomNumberList rootpath = null;
    // keeps track of the match number to insure distinct variables' 
    // names for distinct match constructs
    private int matchNumber = 0;
    // keeps track of the subject number to insure distinct variables' 
    // names when renaming subjects
    private int freshSubjectCounter = 0;
    private int freshVarCounter = 0;

    private ConstraintPropagator constraintPropagator; 
    private ConstraintGenerator constraintGenerator; 

    /** Constructor */
    public CompilerEnvironment() {
      super();
      this.constraintPropagator = new ConstraintPropagator(Compiler.this); 
      this.constraintGenerator = new ConstraintGenerator(Compiler.this); 
    }

    public void nextMatch() {
      matchNumber++;
      rootpath = `concTomNumber(MatchNumber(matchNumber));
      freshSubjectCounter=0;
      freshVarCounter=0;
    }

    /** Accessor methods */
    public SymbolTable getSymbolTable() {
      return this.symbolTable;
    }

    public void setSymbolTable(SymbolTable symbolTable) {
      this.symbolTable = symbolTable;
    }

    public TomNumberList getRootpath() {
      if(this.rootpath==null) {
        return `concTomNumber();
      } else {
        return this.rootpath;
      }
    }

    public int getMatchNumber() {
      return this.matchNumber;
    }

    public int genFreshSubjectCounter() {
      return this.freshSubjectCounter++;
    }

    public int genFreshVarCounter() {
      return this.freshVarCounter++;
    }

    public void setFreshSubjectCounter(int freshSubjectCounter) {
      this.freshSubjectCounter = freshSubjectCounter;
    }

    public void setFreshVarCounter(int freshVarCounter) {
      this.freshVarCounter = freshVarCounter;
    }

    public ConstraintPropagator getConstraintPropagator() {
      return this.constraintPropagator;
    }

    public ConstraintGenerator getConstraintGenerator() {
      return this.constraintGenerator;
    }

    /** need more routines ? */

  } // class CompilerEnvironment


  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options>" +
    "<boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/>" +
    "</options>";
  
  public static final PlatformOptionList PLATFORM_OPTIONS =
    `concPlatformOption(
        PluginOption("compile", "", "Compiler (activated by default)", BooleanValue(True()), "")
        );

  /** Constructor */
  public Compiler() {
    super("Compiler");
    compilerEnvironment = new CompilerEnvironment();
  }

  public void run(Map informationTracker) {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");
    try {
      getCompilerEnvironment().setSymbolTable(getStreamManager().getSymbolTable());

      Code code = (Code)getWorkingTerm();
      code = addACFunctions(code);      

      // we use TopDown and not TopDownIdStopOnSuccess to compile nested-match
      Code compiledTerm = `TopDown(CompileMatch(this)).visitLight(code);

      //System.out.println("compiledTerm = \n" + compiledTerm);            
      Collection hashSet = new HashSet();
      Code renamedTerm =
        `TopDownIdStopOnSuccess(findRenameVariable(hashSet)).visitLight(compiledTerm);
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + COMPILED_SUFFIX, renamedTerm);
      }
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.tomCompilationPhase,
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
    } catch (Exception e) {
      String fileName = getStreamManager().getInputFileName();
        TomMessage.error(getLogger(),
            fileName, 0,
            TomMessage.exceptionMessage, 
            fileName, "Compiler", e.getMessage());
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Compiler.DECLARED_OPTIONS);
    //return PLATFORM_OPTIONS; 
  }

  // looks for a 'Match' instruction:
  // 1. transforms each sequence of patterns into a conjuction of MatchConstraint
  // 2. launch PropagationManager
  // 3. launch PreGenerator
  // 4. launch GenerationManager
  // 5. launch PostGenerator  
  // 6. transforms resulted expression into a CompiledMatch
  %strategy CompileMatch(compiler:Compiler) extends Identity() {
    visit Instruction {			
      Match(constraintInstructionList, matchOptionList)  -> {
        compiler.getCompilerEnvironment().nextMatch();
        int actionNumber = 0;
        TomList automataList = `concTomTerm();	
        ArrayList<BQTerm> subjectList = new ArrayList<BQTerm>();
        ArrayList<TomTerm> renamedSubjects = new ArrayList<TomTerm>();
        // for each pattern action <term>,...,<term> -> { action }
        // build a matching automata
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionList),_*) -> {
            actionNumber++;
            try {
              // get the new names for subjects and generates casts -- needed especially for lists
              // this is performed here, and not above, because in the case of nested matches, we do not want 
              // to go in the action and collect from there              
              Constraint newConstraint = `TopDownIdStopOnSuccess(renameSubjects(subjectList,renamedSubjects,compiler)).visitLight(`constraint);

              Constraint propagationResult = compiler.getCompilerEnvironment().getConstraintPropagator().performPropagations(newConstraint);
              PreGenerator preGenerator = new PreGenerator(compiler.getCompilerEnvironment().getConstraintGenerator());
              Expression preGeneratedExpr = preGenerator.performPreGenerationTreatment(propagationResult);
              Instruction matchingAutomata = compiler.getCompilerEnvironment().getConstraintGenerator().performGenerations(preGeneratedExpr, `action);
              Instruction postGenerationAutomata = PostGenerator.performPostGenerationTreatment(matchingAutomata);
              TomNumberList path = compiler.getCompilerEnvironment().getRootpath();
              TomNumberList numberList = `concTomNumber(path*,PatternNumber(actionNumber));
              TomTerm automata = `Automata(optionList,newConstraint,numberList,postGenerationAutomata);
              automataList = `concTomTerm(automataList*,automata);
            } catch(Exception e) {
              e.printStackTrace();
              throw new TomRuntimeException("Propagation or generation exception:" + e);
            }																	    						
          }
        }// end %match				
        /*
         * return the compiled Match construction
         */        
        InstructionList astAutomataList = Compiler.automataListCompileMatchingList(automataList);
        // the block is useful in case we have a label on the %match: we would like it to be on the whole Match instruction 
        return `UnamedBlock(concInstruction(CompiledMatch(AbstractBlock(astAutomataList), matchOptionList)));
      }
    }// end visit
  }// end strategy  

  /**
   * Takes all MatchConstraints and renames the subjects;
   * (this ensures that the subject is not constructed more than once) 
   * Match(p,s,castType) -> Match(x,s,castType) /\ IsSort(castType,x) /\
   *                        Match(y,Cast(x),castType) /\ Match(p,y,castType) 
   * 
   * @param subjectList the list of old subjects
   */
  %strategy renameSubjects(ArrayList subjectList,ArrayList renamedSubjects, Compiler compiler) extends Identity() {
    visit Constraint {
      constr@MatchConstraint[Pattern=pattern,Subject=subject,AstType=castType] -> {
        if(renamedSubjects.contains(`pattern) || ( `(subject) instanceof BQVariable && renamedSubjects.contains(TomBase.convertFromBQVarToVar(`subject))) ) {
          // make sure we don't process generated contraints
          return `constr; 
        } 
        TomType freshSubjectType = compiler.getTermTypeFromTerm(`subject);
        //System.out.println("freshSubjectType = " + freshSubjectType);

        // test if we already renamed this subject 
        if(subjectList.contains(`subject)) {
          TomTerm renamedSubject = (TomTerm) renamedSubjects.get(subjectList.indexOf(`subject));

          if (freshSubjectType == compiler.getSymbolTable().TYPE_UNKNOWN) {
            freshSubjectType = renamedSubject.getAstType();
          } 
          renamedSubject = renamedSubject.setAstType(freshSubjectType);

          BQTerm freshVar = compiler.getFreshVariable(freshSubjectType);

          Constraint newConstraint = `constr.setSubject(TomBase.convertFromVarToBQVar(renamedSubject));

          //System.out.println("freshSubjectType = " + freshSubjectType);
          //System.out.println("castType         = " + `castType);

          //if(freshSubjectType == `castType) {
          //  return `AndConstraint(
          //      MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,freshSubjectType),
          //      IsSortConstraint(castType,freshVar),
          //      MatchConstraint(renamedSubject,freshVar,freshSubjectType),
          //      newConstraint);

          //} else {
            return `AndConstraint(
                MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,freshSubjectType),
                IsSortConstraint(castType,freshVar),
                MatchConstraint(renamedSubject,ExpressionToBQTerm(Cast(freshSubjectType,BQTermToExpression(freshVar))),freshSubjectType),
                newConstraint);
          //}
        }

        TomNumberList path = compiler.getCompilerEnvironment().getRootpath();
        TomName freshSubjectName  = `PositionName(concTomNumber(path*,NameNumber(Name("_freshSubject_" + compiler.getCompilerEnvironment().genFreshSubjectCounter()))));
        if (freshSubjectType == compiler.getSymbolTable().TYPE_UNKNOWN) {
          %match(subject) {
            (BQVariable|BQVariableStar)[AstType=variableType] -> {
              freshSubjectType = `variableType;
            }
            sv@(BuildTerm|FunctionCall|BuildConstant|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name(tomName)] -> {
              TomSymbol tomSymbol = compiler.getSymbolTable().getSymbolFromName(`tomName);
              if(tomSymbol != null) {
                freshSubjectType = TomBase.getSymbolCodomain(tomSymbol);
              } else if(`sv.isFunctionCall()) {
                freshSubjectType =`sv.getAstType();
              }
            }
          }
        }

        TomTerm renamedVar = `Variable(concOption(),freshSubjectName,freshSubjectType,concConstraint());
        subjectList.add(`subject);
        renamedSubjects.add(renamedVar);
        Constraint newConstraint = `constr.setSubject(TomBase.convertFromVarToBQVar(renamedVar));
        BQTerm freshVar = compiler.getFreshVariable(freshSubjectType);

        //System.out.println("freshSubjectType2 = " + freshSubjectType);
        //System.out.println("castType2         = " + `castType);
        //if(freshSubjectType == `castType) {
        //  return `AndConstraint(
        //      MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,freshSubjectType),
        //      IsSortConstraint(castType,freshVar),
        //      MatchConstraint(renamedVar,freshVar,freshSubjectType),
        //      newConstraint);
        //} else {
          return `AndConstraint(
              MatchConstraint(TomBase.convertFromBQVarToVar(freshVar),subject,freshSubjectType),
              IsSortConstraint(castType,freshVar),
              MatchConstraint(renamedVar,ExpressionToBQTerm(Cast(freshSubjectType,BQTermToExpression(freshVar))),freshSubjectType),
              newConstraint);
        //}

      }
    }
  }

  /**
   * builds a list of instructions from a list of automata
   */
  private static InstructionList automataListCompileMatchingList(TomList automataList) {
    %match(automataList) {
      concTomTerm() -> { return `concInstruction(); }
      concTomTerm(Automata(optionList,constraint,_,instruction),l*)  -> {
        InstructionList newList = automataListCompileMatchingList(`l);				
        // if a label is assigned to a pattern (label:pattern ->
        // action) we generate corresponding labeled-block				 
        %match(optionList) {
          concOption(_*,Label(Name(name)),_*) -> {
            // UnamedBlock(concInstruction(...)) to put patterns/actions in a fresh environment
            return `concInstruction(UnamedBlock(concInstruction(CompiledPattern(constraint,NamedBlock(name,concInstruction(instruction))))), newList*);
          }
        }
        // UnamedBlock(concInstruction(...)) to put patterns/actions in a fresh environment
        return `concInstruction(UnamedBlock(concInstruction(CompiledPattern(constraint,instruction))), newList*);
      }
    }
    return null;
  }

  /**
   * helper functions - mostly related to free var generation
   */

  public SymbolTable getSymbolTable() {
    return getCompilerEnvironment().getSymbolTable();
  }

  // used in generator/SyntacticGenerator.t code
  public TomType getTermTypeFromName(TomName tomName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(stringName);    
    if(tomSymbol == null) {
      throw new TomRuntimeException("Unknown symbol:" + stringName);
    }
    return tomSymbol.getTypesToType().getCodomain();
  }


  // used in propagator/SyntacticPropagator.t code
  public TomType getSlotType(TomName tomName, TomName slotName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = getSymbolTable().getSymbolFromName(stringName);
    if(tomSymbol == null) {
      throw new TomRuntimeException("Unknown symbol:" + stringName);
    }
    return TomBase.getSlotType(tomSymbol,slotName);    
  } 

  public TomType getTermTypeFromTerm(TomTerm tomTerm) {    
    return TomBase.getTermType(tomTerm,getCompilerEnvironment().getSymbolTable());    
  }
 
  public TomType getTermTypeFromTerm(BQTerm tomTerm) {    
    return TomBase.getTermType(tomTerm,getCompilerEnvironment().getSymbolTable());    
  }

  public BQTerm getFreshVariable(TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+n);
    return `BQVariable(concOption(),newName,type);
  }

  public BQTerm getFreshVariable(String name, TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+name+"_"+n);
    return `BQVariable(concOption(),newName,type);
  }

  public BQTerm getFreshVariableStar(TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+n);
    return `BQVariableStar(concOption(),newName,type);
  }

  public BQTerm getFreshVariableStar(String name, TomType type) {
    int n = getCompilerEnvironment().genFreshVarCounter();
    TomName newName = buildVariableName("_"+name+"_"+n);
    return `BQVariableStar(concOption(),newName,type);
  }

  public BQTerm getBeginVariableStar(TomType type) {
    return getFreshVariableStar(freshBeginPrefix,type);
  }

  public BQTerm getEndVariableStar(TomType type) {
    return getFreshVariableStar(freshEndPrefix,type);
  }

  private TomName buildVariableName(String name) {
    TomNumberList path = getCompilerEnvironment().getRootpath();
    TomName freshVarName = `PositionName(concTomNumber(path*,NameNumber(Name(name))));
    return freshVarName;
  }

  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  private static int findRenameVariableCounter = 0;
  %strategy findRenameVariable(context:Collection) extends Identity() {
    visit BQTerm {
      var@(BQVariable|BQVariableStar)[AstName=astName@Name(name)] -> {
        if(context.contains(`astName)) {
          String prefix = "tom";
          return `var.setAstName(`Name(ASTFactory.makeTomVariableName(prefix,name)));
        }
      }
    }

    visit Instruction {
      CompiledPattern(patternList,instruction) -> {
        // only variables found in LHS that are not already used in some constraint's RHS 
        // have to be renamed (this avoids that the JAVA ones are renamed)
        Collection newContext = new HashSet();
        Collection rhsContext = new HashSet();
        `TopDownCollect(CollectLHSVars(newContext,rhsContext)).visitLight(`patternList);        
        newContext.addAll(context);
        return `TopDownIdStopOnSuccess(findRenameVariable(newContext)).visitLight(`instruction);
      }
    }  
  }  

  %strategy CollectLHSVars(Collection bag, Collection alreadyInRhs) extends Identity() {
    visit Constraint {
      MatchConstraint[Pattern=p,Subject=s] -> {          
        //TODO : checky this code
        Map rhsMap = TomBase.collectMultiplicity(`s);
        alreadyInRhs.addAll(rhsMap.keySet());
        
        Map map = TomBase.collectMultiplicity(`p);
        Collection newContext = new HashSet(map.keySet());
        for (Object o:newContext) {
          if (!alreadyInRhs.contains(o)) {
            bag.add(o);       
          }
        }        
        throw new VisitFailure();/* to stop the top-down */
      }
    }
  }

  /******************************************************************************/

  /**
   * AC methods
   * TODO:
   *   DONE: generate correct code for f(X,Y) << f() [empty case]
   *   DONE: generate correct code for f(X,Y) << f(a()) [singleton case]
   *   DONE: fix the inliner (some code is not generated because a GetHead is used before its definition)
   *   generate correct code for F(X,X,Y,Y) [two non-linear variables]
   *   the AC match is OK only for Gom data-structure with AC hook
   *   make it work with FL hook
   *   adapt compiler for %oparray mapping (and not just %oplist mapping)
   */

  private static String next_minimal_extract = 
        %[
          public boolean next_minimal_extract(int multiplicity, int total, int E[], int sol[]) {
            int pos = total-1;
            while(pos>=0 && (sol[pos]+multiplicity)>E[pos]) {
              sol[pos]=0;
              pos--;
            }
            if(pos<0) {
              return false;
            }
            sol[pos]+=multiplicity;
            return true;
          }
        ]%;

  /**
   * Adds the necessary functions to the ADT of the program
   * 
   * @param subject the AST of the program
   */
  private Code addACFunctions(Code subject) throws VisitFailure {
    // we use the symbol table as all AC the operators were marked as
    // used when the loop was generated
    HashSet<String> bag = new HashSet<String>();
    `TopDown(CollectACSymbols(bag)).visitLight(subject);
    if (! bag.isEmpty()) {
      //force the use of the concInt operator
      //TODO: should work without this code
      getSymbolTable().setUsedSymbolConstructor(getSymbolTable().getIntArrayOp());
      getSymbolTable().setUsedSymbolDestructor(getSymbolTable().getIntArrayOp());
    }
    CodeList l = `concCode();
    boolean generatedNextMinimalExtract = false;
    for(String op:bag) {

      TomSymbol opSymbol = getSymbolTable().getSymbolFromName(op);
      // gen all
      TomType opType = opSymbol.getTypesToType().getCodomain();        
      // 1. computeLength
      l = `concCode(DeclarationToCode(getPILforComputeLength(op,opType)),l*);
      // 2. getMultiplicities
      l = `concCode(DeclarationToCode(getPILforGetMultiplicities(op,opType)),l*);
      // 3. getTerm        
      l = `concCode(DeclarationToCode(getPILforGetTermForMultiplicity(op,opType)),l*);
      // 4. next_minimal_extract
      if(!generatedNextMinimalExtract) {
        l = `concCode(TargetLanguageToCode(ITL(next_minimal_extract)),l*);
        generatedNextMinimalExtract = true;
      }
    }
    // make sure the variables are correctly defined
    l = PostGenerator.changeVarDeclarations(`l);
    subject = `OnceTopDownId(InsertDeclarations(l)).visitLight(subject);          
    return subject;
  }

  %strategy CollectACSymbols(HashSet bag) extends Identity() {
    visit TomTerm {
      RecordAppl[NameList=concTomName(Name(headName),_*),Options=concOption(_*,MatchingTheory(concElementaryTheory(_*,AC(),_*)),_*)] -> { 
        bag.add(`headName);
      }
    }
  }

  %strategy InsertDeclarations(CodeList l) extends Identity() {
    visit CodeList {
      concCode(X*,d@DeclarationToCode[],Y*) -> {        
        %match(l) {
          concCode(Z*) -> { return `concCode(X*,Z*,d,Y*); }
        }         
      }
    }
    visit InstructionList {
      concInstruction(X*,d@CodeToInstruction(DeclarationToCode[]),Y*) -> {        
        InstructionList il = `concInstruction();
        %match(l) {
          concCode(_*,z,_*) -> { il = `concInstruction(il*,CodeToInstruction(z)); }
        }         
        return `concInstruction(X*,il*,d,Y*);
      }
    }
  }

  /**
   *    // Generates the PIL for the following function (used by the AC algorithm)
   * 
   *     private int[] getMultiplicities(Term subj) {
   *       int length = computeLenght(subj);
   *       int[] mult = new int[length];
   *       Term oldElem = null;
   *       // if we realy have a list
   *       // TODO: is this really necessary ?
   *       if (subj.isConsf()) {      
   *         oldElem = subj.getHeadf();      
   *       } else {      
   *         mult[0] = 1;
   *         return mult;      
   *       }
   *       int counter = 0;  
   *       // = subj.length;
   *       while(subj.isConsf()) {
   *         Term elem = subj.getHeadf();        
   *         // another element of this type
   *         if (elem.equals(oldElem)){
   *           mult[counter] += 1; 
   *         } else {
   *           counter++;
   *           oldElem = elem;
   *           mult[counter] = 1;
   *         }
   *         subj = subj.getTailf();
   *         // if we got to the end of the list
   *         if(!subj.isConsf()) {
   *           if (subj.equals(oldElem)){
   *             mult[counter] += 1; 
   *           } else {
   *             counter++;          
   *             mult[counter] = 1;
   *           }
   *           // break; // break the while
   *         } 
   *       }
   *       return mult;
   *     }
   */
  private Declaration getPILforGetMultiplicities(String opNameString, TomType opType) {
    TomType intType = getSymbolTable().getIntType();
    TomType intArrayType = getSymbolTable().getIntArrayType();
    // the name of the int[] operator
    TomName intArrayName = `Name(getSymbolTable().getIntArrayOp());    

    BQTerm subject = `BQVariable(concOption(),Name("subject"),opType);
    BQTerm length = getFreshVariable("length",intType);
    BQTerm mult = getFreshVariable("mult",intArrayType);
    BQTerm oldElem = `BQVariable(concOption(),Name("oldElem"),opType);

    TomName opName = `Name(opNameString);
    /*
     * empty list => return array
     * is_fun_sym => oldElem = getHead
     * else       => array[0]=1; return array
     */
    Instruction ifList = `
      If(EqualBQTerm(opType,subject,BuildEmptyList(opName)),Return(mult),
      If(IsFsym(opName,subject),
        LetRef(oldElem,GetHead(opName,opType,subject),Nop()),
        AbstractBlock(concInstruction(
            AssignArray(mult,ExpressionToBQTerm(Integer(0)),Integer(1)), 
            Return(mult)))));

    // the two ifs
    BQTerm elem = `BQVariable(concOption(),Name("elem"),opType);
    BQTerm counter = `BQVariable(concOption(),Name("counter"),getSymbolTable().getIntType());

    Instruction ifAnotherElem = `If(EqualBQTerm(opType, elem, oldElem),
        AssignArray(mult,counter,AddOne(ExpressionToBQTerm(GetElement(intArrayName,intType,mult,counter)))),
        LetRef(counter,AddOne(counter),LetRef(oldElem,BQTermToExpression(elem),AssignArray(mult, counter, Integer(1)))));

    Instruction ifEndList = `If(Negation(IsFsym(opName,subject)),
        If(EqualBQTerm(opType, subject, oldElem),
          AssignArray(mult,counter,AddOne(ExpressionToBQTerm(GetElement(intArrayName,intType,mult,counter)))),
          LetRef(counter,AddOne(counter),AssignArray(mult,counter,Integer(1)))),
        Nop());

    Instruction whileBlock = `UnamedBlock(concInstruction(
          LetRef(elem,GetHead(opName,opType,subject),ifAnotherElem),
          AbstractBlock(concInstruction(
              Assign(subject,GetTail(opName,subject)),
              ifEndList)))); // subject is the method's argument     
    Expression notEmptySubj = `Negation(EqualBQTerm(opType,subject,BuildEmptyList(opName)));
    Instruction whileLoop = `WhileDo(And(IsFsym(opName,subject),notEmptySubj),whileBlock);
    //old : Instruction whileLoop = `WhileDo(IsFsym(opName,subject),whileBlock);

    // var declarations + ifList + counter declaration + the while + return
    Instruction functionBody = `LetRef(length, BQTermToExpression(FunctionCall(
            Name(ConstraintGenerator.computeLengthFuncName + "_" + opNameString),
            intType,concBQTerm(subject))),
        LetRef(mult,BQTermToExpression(BuildEmptyArray(intArrayName,length)),
          LetRef(oldElem,Bottom(opType),
            UnamedBlock(concInstruction(
                ifList,
                LetRef(counter,Integer(0),whileLoop),
                Return(mult))))));

    return `MethodDef(Name(ConstraintGenerator.multiplicityFuncName+"_" + opNameString),
        concBQTerm(subject),intArrayType,EmptyType(),functionBody);
  }

  /**
   * // Generates the PIL for the following function (used by the AC algorithm)
   * 
   * private int computeLength(Term subj) {
   *  // a single element
   *  if(!subj.isConsf()) {
   *    return 1;
   *  }
   *  Term old = null;
   *  int counter = 0;
   *  while(subj.isConsf()) {
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
   *      // break; // break the while - the while stops due to its condition
   *    } 
   *  }     
   *  return counter;    
   * }
   */
  private Declaration getPILforComputeLength(String opNameString, TomType opType) {    
    // all the variables
    BQTerm subject = `BQVariable(concOption(),Name("subject"),opType);    
    BQTerm old = `BQVariable(concOption(),Name("old"),opType);       
    BQTerm counter = `BQVariable(concOption(),Name("counter"),getSymbolTable().getIntType());
    BQTerm elem = `BQVariable(concOption(),Name("elem"),opType);    
    // test if a new element
    Instruction isNewElem = `If(Negation(EqualBQTerm(opType,elem,old)), UnamedBlock(concInstruction(
            LetRef(counter,AddOne(counter),LetRef(old,BQTermToExpression(elem),Nop())))),Nop());    

    TomName opName = `Name(opNameString);
    // test if end of list
    Instruction isEndList = `If(Negation(IsFsym(opName,subject)), 
        If(Negation(EqualBQTerm(opType,subject,old)),LetRef(counter,AddOne(counter),Nop()),Nop()),Nop());

    Instruction whileBlock = `UnamedBlock(concInstruction(
          LetRef(elem,GetHead(opName,opType,subject),isNewElem),
          AbstractBlock(
            concInstruction(
              Assign(subject,GetTail(opName,subject)),
              isEndList))
          )); // subject is the method's argument    
    Expression notEmptySubj = `Negation(EqualBQTerm(opType,subject,BuildEmptyList(opName)));
    Instruction whileLoop = `WhileDo(And(IsFsym(opName,subject),notEmptySubj),whileBlock);
    //old : Instruction whileLoop = `WhileDo(IsFsym(opName,subject),whileBlock);

    // test if subj is consOpName
    Instruction isConsOpName = `If(Negation(IsFsym(opName,subject)),Return(ExpressionToBQTerm(Integer(1))),Nop());

    Instruction functionBody = `UnamedBlock(concInstruction(
          isConsOpName,
          LetRef(old,Bottom(opType),LetRef(counter,Integer(0),
              UnamedBlock(concInstruction(
                  whileLoop,
                  Return(counter)))))));

    return `MethodDef(Name(ConstraintGenerator.computeLengthFuncName + "_" + opNameString),
        concBQTerm(subject),getSymbolTable().getIntType(),EmptyType(),functionBody);
  }  

  /**
   * Generates the PIL for the following function (used by the AC algorithm):
   * (tempSol contains the multiplicities of the elements of the current solution, 
   *  while alpha contains all the multiplicities )
   *  
   * ex: 
   *   subject = f(a,a,b,b,b);
   *   alpha = [2,3]
   *   tempSol = [1,2]; 
   *   => the function should return f(a,b,b) if isComplement=false 
   *                                 f(a,b) if isComplement=true                         
   *    
   * private OpType getTerm(int[] tempSol, int[] alpha, OpType subject, bool isComplement){
   *  Term result = EmptyList();
   *  Term old = null;
   *  Term elem = null;
   *  int elemCounter = 0;
   *  int tempSolIndex = -1;
   *  while(subj != EmptyList) {    
   *    // the end of the list
   *    if(subj.isConsf()) { 
   *      elem = subj.getHeadf();
   *      subj = subj.getTailf();
   *    } else {
   *      elem = subj;
   *      subj = EmptyList();
   *    }
   *    // a new element
   *    if (!elem.equals(old)){
   *      tempSolIndex++;
   *      old = elem;
   *      elemCounter=0;
   *    } 
   *    
   *    int tempSolVal = tempSol[tempSolIndex];
   *    if (isComplement) {
   *      tempSolVal = alpha[tempSolIndex] - tempSolVal;         
   *    }       
   *    if (tempSolVal != 0 && elemCounter < tempSolVal) {
   *      // we take this element
   *      result = conc(result*,elem);
   *      elemCounter++;
   *    }
   *  }     
   *  return result;
   * }    
   * 
   */
  private Declaration getPILforGetTermForMultiplicity(String opNameString, TomType opType) {

    TomType intArrayType = getSymbolTable().getIntArrayType();
    TomType boolType = getSymbolTable().getBooleanType();
    TomType intType = getSymbolTable().getIntType();

    // the variables      
    BQTerm tempSol = `BQVariable(concOption(),Name("tempSol"),intArrayType);      
    BQTerm subject = `BQVariable(concOption(),Name("subject"),opType);
    BQTerm elem = `BQVariable(concOption(),Name("elem"),opType);

    BQTerm elemCounter = `BQVariable(concOption(),Name("elemCounter"),intType);

    // test if subj is consOpName
    TomName opName = `Name(opNameString);
    Instruction isConsOpName = `If(IsFsym(opName,subject),
        AbstractBlock(concInstruction(
            Assign(elem,GetHead(opName,opType,subject)),
            Assign(subject,GetTail(opName,subject))
            )),
        AbstractBlock(concInstruction(
            Assign(elem,BQTermToExpression(subject)),
            Assign(subject,BQTermToExpression(BuildEmptyList(opName))))));
    BQTerm tempSolIndex = `BQVariable(concOption(),Name("tempSolIndex"),intType);
    BQTerm old = `BQVariable(concOption(),Name("old"),opType);
    Instruction isNewElem = `If(Negation(EqualBQTerm(opType,elem,old)),
        AbstractBlock(concInstruction(
            Assign(tempSolIndex,AddOne(tempSolIndex)),
            Assign(old,BQTermToExpression(elem)),
            Assign(elemCounter,Integer(0)))),
        Nop());  
    // the if for the complement
    BQTerm tempSolVal = `BQVariable(concOption(),Name("tempSolVal"),intType);
    BQTerm alpha = `BQVariable(concOption(),Name("alpha"),intArrayType); 
    BQTerm isComplement = `BQVariable(concOption(),Name("isComplement"),boolType);
    TomName intArrayName = `Name(getSymbolTable().getIntArrayOp());
    Instruction ifIsComplement = `If(BQTermToExpression(isComplement),
        Assign(tempSolVal,
          Substract(ExpressionToBQTerm(GetElement(intArrayName,intType,alpha,tempSolIndex)),
            tempSolVal)),
        Nop());

    // if (tempSolVal != 0 && elemCounter < tempSolVal)      
    Expression ifCond = `And(Negation(EqualBQTerm(intType,tempSolVal,ExpressionToBQTerm(Integer(0)))),
        LessThan(BQTermToExpression(elemCounter),BQTermToExpression(tempSolVal)));
    BQTerm result = `BQVariable(concOption(),Name("result"),opType);
    Instruction ifTakeElem = `If(ifCond,
        AbstractBlock(concInstruction(
            Assign(result,BQTermToExpression(BuildAppendList(opName,result,elem))),
            Assign(elemCounter,AddOne(elemCounter)))),
        Nop());
    //declaration of tempSolVal      
    Instruction tempSolValBlock = `LetRef(tempSolVal,
        GetElement(intArrayName,intType,tempSol,tempSolIndex),
        UnamedBlock(concInstruction(ifIsComplement,ifTakeElem)));
    // the while
    Expression notEmptySubj = `Negation(EqualBQTerm(opType,BuildEmptyList(opName), subject));
    Instruction whileBlock = `UnamedBlock(concInstruction(
          isConsOpName,isNewElem,tempSolValBlock));                  
    Instruction whileLoop = `WhileDo(notEmptySubj,whileBlock);

    Instruction functionBody = `LetRef(result,BQTermToExpression(BuildEmptyList(opName)),
        LetRef(old,Bottom(opType),
          LetRef(elem,Bottom(opType),
            LetRef(elemCounter,Integer(0),
              LetRef(tempSolIndex,Integer(-1),
                UnamedBlock(concInstruction(whileLoop,Return(result)))))))); 

    return `MethodDef(Name(ConstraintGenerator.getTermForMultiplicityFuncName + "_" + opNameString),
        concBQTerm(tempSol,alpha,subject,isComplement),opType,EmptyType(),functionBody);
  } 
}
