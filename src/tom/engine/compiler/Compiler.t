/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2008, INRIA
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
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.library.sl.*;
import tom.engine.tools.SymbolTable;
import tom.engine.exception.TomRuntimeException;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.TomBase;
import tom.engine.adt.tomconstraint.types.*;
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
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/ArrayList.tom}
  %include { ../../library/mapping/java/util/types/Collection.tom}

  %typeterm Compiler { implement { Compiler } }

  private static SymbolTable symbolTable = null;
  private static TomNumberList rootpath = null;
  // keeps track of the match number to insure distinct variables' 
  // names for distinct match constructs
  private static int matchNumber = 0;
  // keeps track of the subject number to insure distinct variables' 
  // names when renaming subjects
  private static int freshSubjectCounter = 0;	
  private static int freshVarCounter = 0;
  private static final String freshVarPrefix = "_freshVar_";
  private static final String freshBeginPrefix = "_begin_";
  private static final String freshEndPrefix = "_end_";

  /** some output suffixes */
  public static final String COMPILED_SUFFIX = ".tfix.compiled";

  /** the declared options string*/
  public static final String DECLARED_OPTIONS = "<options>" +
    "<boolean name='compile' altName='' description='Compiler (activated by default)' value='true'/>" +
    "</options>";

  /** Constructor */
  public Compiler() {
    super("Compiler");
  }

  public void run() {
    long startChrono = System.currentTimeMillis();
    boolean intermediate = getOptionBooleanValue("intermediate");    
    try {
      TomTerm compiledTerm = Compiler.compile((TomTerm)getWorkingTerm(),getStreamManager().getSymbolTable());
      //System.out.println("compiledTerm = \n" + compiledTerm);            
      Collection hashSet = new HashSet();
      TomTerm renamedTerm = (TomTerm) `TopDownIdStopOnSuccess(findRenameVariable(hashSet)).visitLight(compiledTerm);
      setWorkingTerm(renamedTerm);
      if(intermediate) {
        Tools.generateOutput(getStreamManager().getOutputFileName() + COMPILED_SUFFIX, renamedTerm);
      }
      getLogger().log(Level.INFO, TomMessage.tomCompilationPhase.getMessage(),
          Integer.valueOf((int)(System.currentTimeMillis()-startChrono)) );
    } catch (Exception e) {
      getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{getStreamManager().getInputFileName(), "Compiler", e.getMessage()} );
      e.printStackTrace();
    }
  }

  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(Compiler.DECLARED_OPTIONS);
  }

  public static TomTerm compile(TomTerm termToCompile,SymbolTable symbolTable) throws VisitFailure {
    Compiler.symbolTable = symbolTable;
    // we use TopDown  and not TopDownIdStopOnSuccess to compile nested-match
    return (TomTerm) `TopDown(CompileMatch()).visitLight(termToCompile);		
  }

  // looks for a 'Match' instruction:
  // 1. transforms each sequence of patterns into a conjuction of MatchConstraint
  // 2. launch PropagationManager
  // 3. launch PreGenerator
  // 4. launch GenerationManager
  // 5. launch PostGenerator  
  // 6. transforms resulted expression into a CompiledMatch
  %strategy CompileMatch() extends Identity() {
    visit Instruction {			
      Match(constraintInstructionList, matchOptionList)  -> {        
        matchNumber++;
        rootpath = `concTomNumber(MatchNumber(matchNumber));
        freshSubjectCounter = 0;
        freshVarCounter = 0;
        int actionNumber = 0;
        TomList automataList = `concTomTerm();	
        ArrayList<TomTerm> subjectList = new ArrayList<TomTerm>();
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
              Constraint newConstraint = (Constraint) `TopDownIdStopOnSuccess(renameSubjects(subjectList,renamedSubjects)).visitLight(`constraint);

              Constraint propagationResult = ConstraintPropagator.performPropagations(newConstraint);
              Expression preGeneratedExpr = PreGenerator.performPreGenerationTreatment(propagationResult);
              Instruction matchingAutomata = ConstraintGenerator.performGenerations(preGeneratedExpr, `action);
              Instruction postGenerationAutomata = PostGenerator.performPostGenerationTreatment(matchingAutomata);
              TomNumberList numberList = `concTomNumber(rootpath*,PatternNumber(actionNumber));
              TomTerm automata = `Automata(optionList,newConstraint,numberList,postGenerationAutomata);
              automataList = `concTomTerm(automataList*,automata); //append(automata,automataList);
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
   * Match(p,s) -> Match(object,s) /\ IsSort(object) /\ Match(freshSubj,Cast(object)) /\ Match(p,freshSubj) 
   * 
   * @param subjectList the list of old subjects
   */
  %strategy renameSubjects(ArrayList subjectList,ArrayList renamedSubjects) extends Identity() {
    visit Constraint {
      constr@MatchConstraint[Pattern=pattern, Subject=subject] -> {
        if(renamedSubjects.contains(`pattern) || renamedSubjects.contains(`subject) ) {
          // make sure we don't process generated contraints
          return `constr; 
        }        
        // test if we already renamed this subject 
        if(subjectList.contains(`subject)) {
          TomTerm renamedSubj = (TomTerm) renamedSubjects.get(subjectList.indexOf(`subject));
          Constraint newConstraint = `constr.setSubject(renamedSubj);
          TomType freshSubjectType = ((Variable)renamedSubj).getAstType();
          TomTerm freshVar = getUniversalObjectForSubject(freshSubjectType);
          return `AndConstraint(
              MatchConstraint(freshVar,subject),
              IsSortConstraint(freshSubjectType,freshVar),
              MatchConstraint(renamedSubj,ExpressionToTomTerm(Cast(freshSubjectType,TomTermToExpression(freshVar)))),
              newConstraint);
        }
        TomName freshSubjectName  = `PositionName(concTomNumber(rootpath*,NameNumber(Name("_freshSubject_" + (++freshSubjectCounter)))));
        TomType freshSubjectType = `EmptyType();
        %match(subject) {
          (Variable|VariableStar)[AstType=variableType] -> { 
            freshSubjectType = `variableType;
          }          
          sv@(BuildTerm|FunctionCall|BuildConstant|BuildEmptyList|BuildConsList|BuildAppendList|BuildEmptyArray|BuildConsArray|BuildAppendArray)[AstName=Name(tomName)] -> {
            TomSymbol tomSymbol = symbolTable.getSymbolFromName(`tomName);                      
            if(tomSymbol != null) {
              freshSubjectType = TomBase.getSymbolCodomain(tomSymbol);
            } else if(`sv.isFunctionCall()) {
              freshSubjectType =`sv.getAstType();
            }
          }
        }
        TomTerm renamedVar = `Variable(concOption(),freshSubjectName,freshSubjectType,concConstraint());
        subjectList.add(`subject);
        renamedSubjects.add(renamedVar);
        Constraint newConstraint = `constr.setSubject(renamedVar);   
        TomTerm freshVar = getUniversalObjectForSubject(freshSubjectType);
        return `AndConstraint(
            MatchConstraint(freshVar,subject),
            IsSortConstraint(freshSubjectType,freshVar),
            MatchConstraint(renamedVar,ExpressionToTomTerm(Cast(freshSubjectType,TomTermToExpression(freshVar)))),
            newConstraint);
      }
    }
  }
  private static TomTerm getUniversalObjectForSubject(TomType subjectType){    
    if (symbolTable.isBuiltinType(TomBase.getTomType(subjectType))) {
      return getFreshVariable(subjectType);
    } else {
      return getFreshVariable(symbolTable.getUniversalType());
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

  public static TomNumberList getRootpath() {
    return rootpath;
  }

  public static SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public static TomType getTermTypeFromName(TomName tomName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = symbolTable.getSymbolFromName(stringName);    
    return tomSymbol.getTypesToType().getCodomain();
  }

  public static TomType getSlotType(TomName tomName, TomName slotName) {
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = symbolTable.getSymbolFromName(stringName);
    return TomBase.getSlotType(tomSymbol,slotName);    
  } 

  // [pem] really useful ?
  public static TomType getIntType() {
    return symbolTable.getIntType();
  }

  // [pem] really useful ?
  public static TomType getBooleanType() {
    return symbolTable.getBooleanType();
  }

  public static TomType getTermTypeFromTerm(TomTerm tomTerm) {    
    return TomBase.getTermType(tomTerm,symbolTable);    
  }

  public static TomTerm getFreshVariable(TomType type) {
    return getFreshVariable(freshVarPrefix + (freshVarCounter++), type);    
  }

  public static TomTerm getFreshVariable(String name, TomType type) {
    TomNumberList path = getRootpath();
    TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name(name))));
    return `Variable(concOption(),freshVarName,type,concConstraint());
  }

  public static TomTerm getFreshVariableStar(TomType type) {
    return getFreshVariableStar(freshVarPrefix + (freshVarCounter++), type);
  }

  public static TomTerm getFreshVariableStar(String name, TomType type) {
    TomNumberList path = getRootpath();
    TomName freshVarName  = `PositionName(concTomNumber(path*,NameNumber(Name(name))));
    return `VariableStar(concOption(),freshVarName,type,concConstraint());
  }

  public static TomTerm getBeginVariableStar(TomType type) {
    return getFreshVariableStar(freshBeginPrefix + (freshVarCounter++),type);
  }

  public static TomTerm getEndVariableStar(TomType type) {
    return Compiler.getFreshVariableStar(freshEndPrefix + (freshVarCounter++),type);
  }

  /*
   * add a prefix (tom_) to back-quoted variables which comes from the lhs
   */
  %strategy findRenameVariable(context:Collection) extends Identity() {
    visit TomTerm {
      var@(Variable|VariableStar)[AstName=astName@Name(name)] -> {
        if(context.contains(`astName)) {          
          return `var.setAstName(`Name(ASTFactory.makeTomVariableName(name)));
        }
      }
    }

    visit Instruction {
      CompiledPattern(patternList,instruction) -> {
        // only variables found in LHS have to be renamed (this avoids that the JAVA ones are renamed)
        Collection newContext = new ArrayList();
        `TopDownCollect(CollectLHSVars(newContext)).visitLight(`patternList);        
        newContext.addAll(context);
        return (Instruction)`TopDownIdStopOnSuccess(findRenameVariable(newContext)).visitLight(`instruction);
      }
    }  
  }  

  %strategy CollectLHSVars(Collection bag) extends Identity() {
    visit Constraint {
      MatchConstraint(p,_) -> {        
        Map map = TomBase.collectMultiplicity(`p);
        Collection newContext = new HashSet(map.keySet());
        bag.addAll(newContext);
        throw new VisitFailure();// to stop the top-down
      }
    }
  }


}
