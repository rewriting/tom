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

/**
 * Tom compiler based on constraints.
 * 
 * It controls different phases of compilation:
 * - propagation of constraints
 * - instruction generation from constraints
 * - ...   
 */
public class TomConstraintCompiler extends TomBase {

  %include { adt/tomsignature/TomSignature.tom }
  %include { sl.tom }

  private static SymbolTable symbolTable = null;
  private static TomNumberList rootpath = null;
  // keeps track of the match number to insure distinct variables' 
  // names for distinct match constructs
  private static short matchNumber = 0;
  // keeps track of the subject number to insure distinct variables' 
  // names when renaming subjects
  private static short freshSubjectCounter = 0;	

  public static TomTerm compile(TomTerm termToCompile,SymbolTable symbolTable){
    TomConstraintCompiler.symbolTable = symbolTable;    
    return  (TomTerm)((Strategy)`InnermostId(CompileMatch())).fire(termToCompile);		
  }

  // looks for a 'Match' instruction:
  // 1. transforms each sequence of patterns into a conjuction of MatchConstraint
  // 2. launch PropagationManager
  // 3. launch GenerationManager
  // 4. transforms resulted expression into a CompiledMatch
  %strategy CompileMatch() extends Identity(){
    visit Instruction {			
      Match(SubjectList(subjectList),patternInstructionList, matchOptionList)  -> {				
        matchNumber++;
        rootpath = `concTomNumber(MatchNumber(matchNumber));
        freshSubjectCounter = 0;
        short actionNumber = 0;
        TomList automataList = `concTomTerm();	
        // get the new names for subjects ( for further casts if needed - especially for lists)
        TomList renamedSubjects = renameSubjects(`subjectList);
        // for each pattern action <term>,...,<term> -> { action }
        // build a matching automata
        %match(patternInstructionList){
          concPatternInstruction(_*,PatternInstruction(Pattern[TomList=patternList],action,optionList),_*) ->{            
            Constraint constraint = TomConstraintCompiler.buildConstraintConjunction(`patternList,renamedSubjects);            
            try{
              actionNumber++;              
              Constraint propagationResult = TomPropagationManager.performPropagations(constraint);              
              Instruction matchingAutomata = TomGenerationManager.performGenerations(propagationResult, `action);
              
              TomNumberList numberList = `concTomNumber(rootpath*,PatternNumber(actionNumber));
              TomTerm automata = `Automata(optionList,patternList,numberList,matchingAutomata);
              automataList = append(automata,automataList);
            }catch(Exception e){
              throw new TomRuntimeException("Propagation or generation exception:" + e);
            }																	    						
          }
        }// end %match				
        /*
         * return the compiled Match construction
         */
        InstructionList astAutomataList = TomConstraintCompiler.automataListCompileMatchingList(automataList);
        return `CompiledMatch(collectVariableFromSubjectList(subjectList,renamedSubjects,AbstractBlock(astAutomataList)), matchOptionList);
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
      concTomTerm(subjectVar@Variable[Option=option,AstType=variableType],tail*) -> {
        body = collectVariableFromSubjectList(`tail,renamedSubjects.getTailconcTomTerm(),body);		        
        Expression source = `Cast(variableType,TomTermToExpression(subjectVar));
        Instruction let = `LetRef(renamedSubjects.getHeadconcTomTerm(),source,body);
        // Check that the matched variable has the correct type
        return `If(IsSort(variableType,subjectVar),let,Nop());
      }			
      // if we do not have a variable ( we have a BuildTerm or FunctionCall or BuildConstant)
      concTomTerm(subjectVar@!Variable[],tail*) -> {
        body = collectVariableFromSubjectList(`tail,renamedSubjects.getTailconcTomTerm(),body);
        Expression source = `Cast(((Variable)renamedSubjects.getHeadconcTomTerm()).getAstType(),TomTermToExpression(subjectVar));
        return `LetRef(renamedSubjects.getHeadconcTomTerm(),source,body);
      }
    }
    throw new TomRuntimeException("collectVariableFromSubjectList: strange term: " + `subjectList);
  }

  /**
   * given the list of match variables (from match(t1,...,tn)), it renames them and returns the new list 
   */
  private static TomList renameSubjects(TomList subjectList){		
    TomList renamedSubjects = `concTomTerm();
    %match(subjectList) { 
      concTomTerm(_*,subject,_*) ->{				
        TomName freshSubjectName  = `PositionName(concTomNumber(rootpath*,NameNumber(Name("freshSubject_" + (++freshSubjectCounter)))));
        TomType freshSubjectType = `EmptyType();
        %match(subject){
          Variable[AstType=variableType] ->{ freshSubjectType = `variableType; }
          sv@(BuildTerm|FunctionCall|BuildConstant)[AstName=Name(tomName)] ->{
            TomSymbol tomSymbol = symbolTable.getSymbolFromName(`tomName);				        
            if(tomSymbol != null) {
              freshSubjectType = TomBase.getSymbolCodomain(tomSymbol);
            } else if(`sv.isFunctionCall()) {
              freshSubjectType =`sv.getAstType();
            }
          }
        }// end match
        TomTerm renamedVar = `Variable(concOption(),freshSubjectName,freshSubjectType,concConstraint());  
        renamedSubjects = `concTomTerm(renamedSubjects*,renamedVar);
      }
    }// end match
    return renamedSubjects;
  }

  /**
   * takes a list of patterns (p1...pn) and a list of subjects (s1...sn)
   * and generates p1 << s1 /\ .... /\ pn << sn
   */
  private static Constraint buildConstraintConjunction(TomList patternList, TomList subjectList){
    Constraint constraint = `AndConstraint();
    while(!`patternList.isEmptyconcTomTerm()) {
      TomTerm pattern = `patternList.getHeadconcTomTerm();
      TomTerm subject = `subjectList.getHeadconcTomTerm();
      
      constraint = `AndConstraint(MatchConstraint(pattern, subject),constraint*);

      `patternList = `patternList.getTailconcTomTerm();
      `subjectList = `subjectList.getTailconcTomTerm();
    }// end while
    return constraint;
  }	

  /**
   * builds a list of instructions from a list of automata
   */
  private static InstructionList automataListCompileMatchingList(TomList automataList) {
    %match(automataList) {
      concTomTerm() -> { return `concInstruction(); }
      concTomTerm(Automata(optionList,patternList,_,instruction),l*)  -> {
        InstructionList newList = automataListCompileMatchingList(`l);				
        // if a label is assigned to a pattern (label:pattern ->
        // action) we generate corresponding labeled-block				 
        %match(optionList) {
          concOption(_*,Label(name@Name[]),_*) -> { 
            `instruction = `NamedBlock(name.getString(),concInstruction(instruction));
          }
        }				
        return `concInstruction(CompiledPattern(patternList,instruction), newList*);
      }
    }
    return null;
  }

  public static TomNumberList getRootpath(){
    return rootpath;
  }

  public static SymbolTable getSymbolTable(){
    return symbolTable;
  }

  public static TomType getTermTypeFromName(TomName tomName){
    String stringName = ((Name)tomName).getString();
    TomSymbol tomSymbol = symbolTable.getSymbolFromName(stringName);
    return tomSymbol.getTypesToType().getCodomain();
  }
  
  public static TomType getIntType(){
    return symbolTable.getIntType();
  }

  public static TomType getTermTypeFromTerm(TomTerm tomTerm){
    %match(tomTerm){
      RecordAppl[NameList=nameList@(headName,_*)] ->{
        return getTermTypeFromName(`headName);
      }
      (Variable|UnamedVariable)[AstType=type] ->{
        return `type;
      }
      Subterm(constructorName, slotName, term) ->{
        TomSymbol tomSymbol = symbolTable.getSymbolFromName(((TomName)`constructorName).getString());
        return TomBase.getSlotType(tomSymbol, `slotName);
      }
    }
    throw new TomRuntimeException("getTermTypeFromTerm: cannot find the type for: " + tomTerm);
  }
}