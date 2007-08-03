/*
 *
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2007, INRIA
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

/**
 * Tom compiler based on constraints.
 * 
 * It controls different phases of compilation:
 * - propagation of constraints
 * - instruction generation from constraints
 * - ...   
 */
public class ConstraintCompiler {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../library/mapping/java/sl.tom}
  %include { ../../library/mapping/java/util/types/ArrayList.tom}

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

  public static TomTerm compile(TomTerm termToCompile,SymbolTable symbolTable) throws VisitFailure {
    ConstraintCompiler.symbolTable = symbolTable;
    return  (TomTerm)`TopDown(CompileMatch()).visitLight(termToCompile);		
  }
  
  // looks for a 'Match' instruction:
  // 1. transforms each sequence of patterns into a conjuction of MatchConstraint
  // 2. launch PropagationManager
  // 3. launch PreGenerator
  // 4. launch GenerationManager
  // 5. launch PostGenerator  
  // 6. transforms resulted expression into a CompiledMatch
  %strategy CompileMatch() extends Identity(){
    visit Instruction {			
      Match(constraintInstructionList, matchOptionList)  -> {        
        matchNumber++;
        rootpath = `concTomNumber(MatchNumber(matchNumber));
        freshSubjectCounter = 0;
        freshVarCounter = 0;
        int actionNumber = 0;
        TomList automataList = `concTomTerm();	
        ArrayList subjectList = new ArrayList();
        ArrayList renamedSubjects = new ArrayList();
        // for each pattern action <term>,...,<term> -> { action }
        // build a matching automata
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction(constraint,action,optionList),_*) -> {                        
            try {
              actionNumber++;
              // get the new names for subjects (for further casts if needed - especially for lists)
              // this is performed here, and not above, because in the case of nested matches, we do not want 
              // to go in the action and collect from there                      
              `constraint = (Constraint)`TopDown(renameSubjects(subjectList,renamedSubjects)).visitLight(`constraint);
              
              Constraint propagationResult = ConstraintPropagator.performPropagations(`constraint);
              Expression preGeneratedExpr = PreGenerator.performPreGenerationTreatment(propagationResult);
              Instruction matchingAutomata = ConstraintGenerator.performGenerations(preGeneratedExpr, `action);
              Instruction postGenerationAutomata = PostGenerator.performPostGenerationTreatment(matchingAutomata);              
                            
              TomNumberList numberList = `concTomNumber(rootpath*,PatternNumber(actionNumber));
              TomTerm automata = `Automata(optionList,constraint,numberList,postGenerationAutomata);
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
        InstructionList astAutomataList = ConstraintCompiler.automataListCompileMatchingList(automataList);
        return `CompiledMatch(collectVariableFromSubjectList(ASTFactory.makeList(subjectList),ASTFactory.makeList(renamedSubjects),AbstractBlock(astAutomataList)), matchOptionList);
      }
    }// end visit
  }// end strategy

  /**
   * collects match variables (from match(t1,...,tn)) and
   * 1. checks their instance type 
   * 2. create new renamed subjects with corresponding casts	 
   */
  private static Instruction collectVariableFromSubjectList(TomList subjectList, TomList renamedSubjects, Instruction body) {
    %match(subjectList) { 
      concTomTerm() -> { return body; }
      concTomTerm(subjectVar@Variable[AstType=variableType],tail*) -> {
        body = collectVariableFromSubjectList(`tail,renamedSubjects.getTailconcTomTerm(),body);		        
        Expression source = `Cast(variableType,TomTermToExpression(subjectVar));
        Instruction let = `LetRef(renamedSubjects.getHeadconcTomTerm(),source,body);
        // Check that the matched variable has the correct type
        return `If(IsSort(variableType,subjectVar),let,Nop());
      }			
      // if we do not have a variable (we have a BuildTerm or FunctionCall or BuildConstant)
      concTomTerm(subjectVar@!Variable[],tail*) -> {
        body = collectVariableFromSubjectList(`tail,renamedSubjects.getTailconcTomTerm(),body);
        Expression source = `Cast(((Variable)renamedSubjects.getHeadconcTomTerm()).getAstType(),TomTermToExpression(subjectVar));
        return `LetRef(renamedSubjects.getHeadconcTomTerm(),source,body);
      }
    }
    throw new TomRuntimeException("collectVariableFromSubjectList: strange term: " + `subjectList);
  }

  /**
   * Takes all MatchConstraints and renames the subjects; collects the old and the new subjects in the received bags
   * 
   * @param subjectList the list of old subjects
   * @param renamedSubjects the list of renamed subjects
   */
  %strategy renameSubjects(ArrayList subjectList,ArrayList renamedSubjects) extends Identity(){
    visit Constraint {
      MatchConstraint(pattern,subject) -> {
        // test if we already renamed this subject
        if (subjectList.contains(`subject)) {
          return `MatchConstraint(pattern,(TomTerm)renamedSubjects.get(subjectList.indexOf(subject))); 
        }
        TomName freshSubjectName  = `PositionName(concTomNumber(rootpath*,NameNumber(Name("freshSubject_" + (++freshSubjectCounter)))));
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
        }// end match
        TomTerm renamedVar = `Variable(concOption(),freshSubjectName,freshSubjectType,concConstraint());
        subjectList.add(`subject);
        renamedSubjects.add(renamedVar);
        return `MatchConstraint(pattern,renamedVar);
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
            `instruction = `NamedBlock(name,concInstruction(instruction));
          }
        }				
        return `concInstruction(CompiledPattern(constraint,instruction), newList*);
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

  public static SymbolTable getSymbolTable(){
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
    return ConstraintCompiler.getFreshVariableStar(freshEndPrefix + (freshVarCounter++),type);
  }
}
