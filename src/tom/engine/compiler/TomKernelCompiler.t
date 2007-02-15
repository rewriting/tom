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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.compiler;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tominstruction.types.instruction.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.*;
import tom.engine.adt.tomoption.types.option.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;
import aterm.ATerm;
import java.util.logging.Level;
import java.util.logging.Logger;
import tom.library.strategy.mutraveler.MuTraveler;
import tom.library.strategy.mutraveler.Identity;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

import tom.platform.*;

import tom.engine.compiler.antipattern.*;
import tom.engine.adt.tomterm.types.tomterm.*;

public class TomKernelCompiler extends TomBase {

  private SymbolTable symbolTable;
  // boolean flag used for anti-pattern matching
  private static final String ANTI_FLAG_NAME = "tom_anti_constraints_status";
  
  private static TomType antiFlagType = null;
  private static TomTerm antiFlagVariable = null;
  
  public TomKernelCompiler(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
    // initialize anti flag
    antiFlagType = symbolTable.getBooleanType();     
  }

  private SymbolTable getSymbolTable(String moduleName) {
    //TODO//
    //Using of the moduleName
    ////////
    return symbolTable;
  }

// ------------------------------------------------------------
  %include { adt/tomsignature/TomSignature.tom }
  %include { mustrategy.tom}
// ------------------------------------------------------------
 
  %typeterm TomKernelCompiler {
    implement { TomKernelCompiler }
  }

  %op Strategy ChoiceTopDown(s1:Strategy) {
    make(v) { `mu(MuVar("x"),ChoiceId(v,All(MuVar("x")))) }
  }

  public int matchNumber = 0;

  private TomName getLabel(OptionList list) {
    %match(list) {
      concOption(_*,Label(name@Name[]),_*) -> { return `name; }
    }
    return null;
  }
  
    /* 
     * compileMatching:
     * compiles the Match construct into a matching automaton: CompiledMatch
     */

  %strategy replace_compileMatching(compiler:TomKernelCompiler) extends `Identity() {
    visit Instruction {
      Match(SubjectList(l1),patternInstructionList, optionList)  -> {    	  
	//TODO
	String moduleName = "default";
	TomNumberList rootpath = `concTomNumber();
	compiler.matchNumber++;
	rootpath = `concTomNumber(rootpath*,MatchNumber(Number(compiler.matchNumber)));

	/*
	 * for each pattern action (<term>,...,<term> -> <action>)
	 * build a matching automata
	 */
	TomList automataList = `concTomTerm();
	int actionNumber = 0;
	VisitableVisitor compileStrategy = `ChoiceTopDown(replace_compileMatching(compiler));
	while(!`patternInstructionList.isEmptyconcPatternInstruction()) {
	  actionNumber++;
	  PatternInstruction pa = `patternInstructionList.getHeadconcPatternInstruction();
	  SlotList patternList = tomListToSlotList(pa.getPattern().getTomList());
	  Instruction actionInst = pa.getAction();
	  if(patternList==null || actionInst==null) {
	    System.out.println("TomKernelCompiler: null value");
	    throw new TomRuntimeException("TomKernelCompiler: null value");
	  }

	  /*
	   * compile nested match constructs
     * given a list of pattern: we build a matching automaton
     */	  
	  actionInst = (Instruction) compileStrategy.visit(actionInst);
	  Instruction matchingAutomata = null;
	  // if we have anti-patterns, we should use a boolean flag
	  if (TomAntiPatternUtils.hasAntiTerms(patternList)){	  
	      // anti flag
	      TomName antiFlagName = `PositionName(concTomNumber(rootpath*,NameNumber(Name(ANTI_FLAG_NAME))));      
	      antiFlagVariable = `Variable(
            concOption(OriginTracking(antiFlagName,0,"")),
            antiFlagName,antiFlagType,concConstraint());
	      TomAntiPatternTransformNew.initialize();      
	      // final test
	      Instruction finalTest = `If(
            EqualTerm(
              antiFlagType,
              Ref(antiFlagVariable),
              ExpressionToTomTerm(TrueTL())),
            actionInst,
            Nop());
            //LetAssign(antiFlagVariable,TrueTL(),Nop()));      
	      matchingAutomata = compiler.genSyntacticMatchingAutomata(
            finalTest,
            `Nop(),
	    		  patternList,rootpath,moduleName,null);
        // glue the flag declaration
	      matchingAutomata = `LetRef(antiFlagVariable,TrueTL(),matchingAutomata);
	  } else {
		  matchingAutomata = compiler.genSyntacticMatchingAutomata(actionInst,`Nop(),
	    		  patternList,rootpath,moduleName,null);
	  }
      OptionList automataOptionList = `concOption();
      TomName label = compiler.getLabel(pa.getOption());
      if(label != null) {
        automataOptionList = `concOption(Label(label),automataOptionList*);
      }
      TomNumberList numberList = `concTomNumber(rootpath*,PatternNumber(Number(actionNumber)));
      TomTerm automata = `Automata(automataOptionList,slotListToTomList(patternList),numberList,matchingAutomata);

      automataList = append(automata,automataList);
      `patternInstructionList = `patternInstructionList.getTailconcPatternInstruction();
    }


	/*
	 * return the compiled Match construction
	 */
	InstructionList astAutomataList = compiler.automataListCompileMatchingList(automataList);
	SlotList slots = tomListToSlotList(`l1);
	Instruction astAutomata = compiler.collectVariableFromSubjectList(slots,rootpath,`AbstractBlock(astAutomataList),moduleName);
	//           System.out.println("Matching compiled: " + `CompiledMatch(astAutomata, optionList));
	return `CompiledMatch(astAutomata, optionList);
      }
    } // end match
  } 

  public TomTerm compileMatching(TomTerm subject) {
    try{
      return (TomTerm) `ChoiceTopDown(replace_compileMatching(this)).visit(subject);
    } catch(VisitFailure e) {
      return subject;
    }
  }

    /*
     * collect match variables (from match(t1,...,tn))
     * create a list of declaration/assignment: v1=t1 ... vn=tn in body
     * generate a check_stamp
     */
  private Instruction collectVariableFromSubjectList(SlotList subjectList, TomNumberList path, Instruction body, String moduleName) {
    %match(subjectList) { 
      concSlot() -> { return body; }
      concSlot(PairSlotAppl(slotName,subjectVar@Variable[Option=option,AstType=variableType]),tail*) -> {
        body = collectVariableFromSubjectList(`tail,path,body,moduleName);
				TomNumberList newPath = `concTomNumber(path*,NameNumber(slotName));
        TomTerm variable = `Variable(option,PositionName(newPath),variableType,concConstraint());
        Expression source = `Cast(variableType,TomTermToExpression(subjectVar));
          // the UnamedBlock encapsulation is needed for Caml
        Instruction let = `Let(variable,source,AbstractBlock(concatInstruction(CheckStamp(variable),body)));
        // Check that the matched variable has the correct type
        return `CheckInstance(variableType,TomTermToExpression(subjectVar),let);
      }

      concSlot(PairSlotAppl(slotName,subjectVar@(BuildTerm|FunctionCall|BuildConstant)[AstName=Name(tomName)]),tail*) -> {
        body = collectVariableFromSubjectList(`tail,path,body,moduleName);
        // ModuleName
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);
	TomType tomType = `EmptyType();
	if(tomSymbol!=null) {
	  tomType = getSymbolCodomain(tomSymbol);
	} else if(`subjectVar.isFunctionCall()) {
	  tomType=`subjectVar.getAstType();
	}
	TomNumberList newPath = `concTomNumber(path*,NameNumber(slotName));
        TomTerm variable = `Variable(concOption(),PositionName(newPath),tomType, concConstraint());
        Expression source = `TomTermToExpression(subjectVar);
        Instruction checkStamp = `CheckStamp(variable);
        return `Let(variable,source,AbstractBlock(concatInstruction(checkStamp,body)));
      }
    }
    throw new TomRuntimeException("collectVariableFromSubjectList: strange term: " + `subjectList);
  }

  private InstructionList concatInstruction(Instruction i1, Instruction i2) {
    %match(i1, i2) {
      AbstractBlock(l1), AbstractBlock(l2) -> { return `concInstruction(l1*,l2*); }
      AbstractBlock(l1), y -> { return `concInstruction(l1*,y); }
      x, AbstractBlock(l2) -> { return `concInstruction(x,l2*); }
    }
      return `concInstruction(i1,i2);
  }

    /*
     * build a list of instructions from a list of automata
     */
  private InstructionList automataListCompileMatchingList(TomList automataList) {
    %match(automataList) {
      concTomTerm() -> { return `concInstruction(); }
      concTomTerm(Automata(optionList,patternList,_,instruction),l*)  -> {
        InstructionList newList = automataListCompileMatchingList(`l);
        if(getLabel(`optionList) != null) {
            /*
             * if a label is assigned to a pattern (label:pattern -> action)
             * we generate corresponding labeled-block
             */
          `instruction = `NamedBlock(getLabel(optionList).getString(),
                                    concInstruction(instruction));
                     
        }
        return `concInstruction(CompiledPattern(patternList,instruction), newList*);
      }
    }
    return null;
  }

    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */
  
    /*
     * given a pattern, this function generates the discrimination test on the root symbol
     * and recursively calls the algorithm on subterms
     */
  Instruction genSyntacticMatchingAutomata(Instruction action,
		  								   Instruction elseAction,	
                                           SlotList termList,
                                           TomNumberList rootpath,
                                           String moduleName,
                                           TomTerm subject) {
	  
    %match(termList) {
      concSlot() -> {    	  
        return action;        
      } 
      // X or _,...  
      concSlot(PairSlotAppl(slotName,
                                var@(Variable|UnamedVariable)[AstType=termType]),termTail*) -> {        
    	Instruction subAction = genSyntacticMatchingAutomata(action,elseAction,`termTail,rootpath,moduleName,subject);       
        
        Expression source = null;
        if (subject !=null ){
        	source = `TomTermToExpression(subject);        	
        }else{        	
        	TomNumberList path  = `concTomNumber(rootpath*,NameNumber(slotName));
        	source = `TomTermToExpression(Variable(concOption(),PositionName(path),termType, concConstraint()));
        }
        return buildLet(`var, source, subAction, elseAction, moduleName);
      }
      // !X                                 
      concSlot(PairSlotAppl(slotName,
            AntiTerm(Variable(option,Name(name),_,_))),_*) -> {
        OriginTracking or = (OriginTracking)`option.getHeadconcOption();     	
    	// this will generate directly false
    	Logger.getLogger(getClass().getName()).log( 
    			new PlatformLogRecord(Level.WARNING, 
					TomMessage.noCodeGeneration, 
					new Object[]{("!" + `name)},
					or.getFileName(), 
					or.getLine()));
            	
    	return `Nop();		  
      }            
      // !f
      concSlot(PairSlotAppl(slotName,
              currentTerm@AntiTerm(RecordAppl[NameList=nameList@(Name(tomName),_*),
                                     Slots=termArgs])),termTail*) -> {
        // transform term to eliminate anti
        TomTerm transformedTerm = TomAntiPatternTransformNew.getConstrainedTerm(
        		`currentTerm,getSymbolTable(moduleName));                   	 
        // recursive call with the transformed term                            	 
        return genSyntacticMatchingAutomata(action,elseAction,`concSlot(PairSlotAppl(slotName,
        		transformedTerm),termTail*),rootpath,moduleName,subject);                            	 
      }                                                                                    
      // (f|g)[...]
      concSlot(PairSlotAppl(slotName,
                   currentTerm@RecordAppl[NameList=nameList@(headName,_*),
                   Slots=termArgs]),termTail*) -> {
        // handle the case when the head symbol has a negation
        String tomName = null;
        if(`(headName) instanceof AntiName) {
          tomName = ((AntiName)`headName).getName().getString();
        } else {
          tomName = ((Name)`headName).getString();
        }                                        	  
        // if the termList contains antipatterns then the term should  
        // be transformed - do not look for anti in the constraints
        if (TomAntiPatternUtils.hasAntiTerms(`currentTerm.setConstraints(`concConstraint()))){
          // transform term to eliminate anti
          TomTerm transformedTerm = TomAntiPatternTransformNew.getConstrainedTerm(
              `currentTerm,getSymbolTable(moduleName));
          // recursive call with the transformed term
          return genSyntacticMatchingAutomata(action,elseAction,`concSlot(PairSlotAppl(slotName,
                  transformedTerm),termTail*),rootpath,moduleName,subject);
        }                                       	  

        // recursively call the algorithm on termTail
        Instruction subAction = genSyntacticMatchingAutomata(action,elseAction,`termTail,rootpath,moduleName,subject);        

        // find the codomain of (f|g) [* should be the same *]
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);
        TomType codomain = tomSymbol.getTypesToType().getCodomain();

        // perform the compilation, according to 3 cases:
        // - (f|g) is a list operator
        // - (f|g) is an array operator
        // - (f|g) is a syntactic operator
        TomNumberList path  = `concTomNumber(rootpath*,NameNumber(slotName));
        TomTerm subjectVariableAST = `Variable(concOption(),PositionName(path),codomain,concConstraint());

        // if we have a subject, use that one
        if(subject != null) {
          subjectVariableAST = subject;
        }        
        // handle the constraints        
        Instruction	constraintAutomata = compileConstraint(`currentTerm,`TomTermToExpression(subjectVariableAST)
            ,subAction,elseAction,moduleName);
        Instruction automataInstruction;
        if(isListOperator(tomSymbol)) {
          // case: list operator
          /*
           * store the subject into an internal variable
           * call genListMatchingAutomata with the new internal variable
           */
          int indexSubterm = 1;
          TomNumberList newPath = `concTomNumber(path*,ListNumber(Number(indexSubterm)));
          TomTerm newSubjectVariableAST = `VariableStar(concOption(),PositionName(newPath),codomain,concConstraint());
          boolean ensureNotEmptyList = true;
          Instruction automata = genListMatchingAutomata(new MatchingParameter(
                tomSymbol,path,
                constraintAutomata,elseAction,
                newSubjectVariableAST,
                newSubjectVariableAST),
              `termArgs,
              indexSubterm,
              ensureNotEmptyList,
              moduleName);
          automataInstruction = `LetRef(newSubjectVariableAST,
              TomTermToExpression(subjectVariableAST),
              automata);
        } else if(isArrayOperator(tomSymbol)) {        	
          // case: array operator
          int indexSubterm = 1;
          TomNumberList newPathList = `concTomNumber(path*,ListNumber(Number(indexSubterm)));
          TomNumberList newPathIndex = `concTomNumber(path*,IndexNumber(Number(indexSubterm)));
          TomTerm newVariableListAST = `VariableStar(concOption(),PositionName(newPathList),codomain,concConstraint());
          TomTerm newVariableIndexAST = `Variable(concOption(),PositionName(newPathIndex),getSymbolTable(moduleName).getIntType(),concConstraint());
          boolean ensureNotEmptyList = true;
          Instruction automata = genArrayMatchingAutomata(new MatchingParameter(
                tomSymbol,path,constraintAutomata,elseAction,
                newVariableListAST, newVariableIndexAST),
              `termArgs,
              indexSubterm,
              ensureNotEmptyList,moduleName);
          Expression glZero = `TomTermToExpression(TargetLanguageToTomTerm(ITL("0")));
          automataInstruction = `LetRef(newVariableIndexAST,glZero,
              Let(newVariableListAST,
                TomTermToExpression(subjectVariableAST),
                automata));
        } else {
          // case: syntactic operator
          Instruction automata = genSyntacticMatchingAutomata(constraintAutomata,elseAction,`termArgs,path,moduleName,null);        
          // if there is no disjunction or no arguments
          if(`nameList.length()==1 || `termArgs.isEmptyconcSlot()) {        	  
            automataInstruction = `collectSubtermFromTomSymbol(termArgs,tomSymbol,subjectVariableAST,path,automata,moduleName);        	  
          } else {        	  
            automataInstruction = `collectSubtermFromSubjectList(currentTerm,subjectVariableAST,path,automata,moduleName);
            return automataInstruction;
          }
        }
        // generate is_fsym(t,f) || is_fsym(t,g)
        Expression cond = `expandDisjunction(EqualFunctionSymbol(codomain,subjectVariableAST,currentTerm),moduleName);
        return `If(cond,automataInstruction,elseAction);
      }
    } // end match
    System.out.println("GenSyntacticMatchingAutomata strange term: " + termList);
    throw new TomRuntimeException("GenSyntacticMatchingAutomata strange term: " + termList);
  }
  
//  /**
//   * compiles anti-pattern matching
//   */
//  Instruction getAntiPatternMatchInstruction(Instruction action,
//			TomTerm currentTerm,
//			TomNumberList rootpath,
//			TomName slotName,
//			String moduleName,			
//			Instruction subAction){
//
//	// get the compiled anti-pattern
//   	Expression compiledAntiPattern = TomAntiPatternUtils.getAntiPatternMatchExpression(action,
//   			currentTerm, rootpath, slotName, moduleName, getSymbolTable(moduleName), subAction, this);
//   	
//   	// if the result is false, no need to generate anything
//   	%match(compiledAntiPattern) {
//   		FalseTL() ->{
//   			OriginTracking or = (OriginTracking)currentTerm.getOption().getHeadconcOption();
//   			Logger.getLogger(getClass().getName()).log( 
//   					new PlatformLogRecord(Level.WARNING, 
//	   					TomMessage.noCodeGeneration, 
//	   					new Object[]{TomAntiPatternUtils.formatTerm(currentTerm)},
//	   					or.getFileName(), 
//	   					or.getLine()));
//   			return `Nop();
//   		}
//   	}
//   	
//   	Instruction varAssign = TomAntiPatternUtils.getVarAssignments(); 
//   	
//   	if ( varAssign == null){
//     	// bound the result with the result for the next term		
//     	return `If(compiledAntiPattern,subAction,Nop());
//   	}else{
//   		// bound the variables' assignment with the result for the next term     		
//   		return `If(compiledAntiPattern, varAssign,Nop());
//   	}
//  }

    /*
     * function which compiles list-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genListMatchingAutomata(MatchingParameter p,
                                      SlotList termList,
                                      int indexTerm,
                                      boolean ensureNotEmptyList,
                                      String moduleName) {
    %match(termList) {
      concSlot() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genCheckEmptyList(p.symbol, p.subjectListName, p.action, Nop());
      }
        
      concSlot(PairSlotAppl[Appl=var@(Variable|UnamedVariable)[AstType=termType]],termTail*) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true,moduleName);
        return genGetElementList(p, indexTerm, `var, `termType, subAction, ensureNotEmptyList, moduleName);
      }

      concSlot(PairSlotAppl[Appl=term@RecordAppl[NameList=(Name(tomName),_*)]],termTail*)  -> {
          /*
           * get an element and perform syntactic matching
           */
        Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,true,moduleName);

        TomName slotName = `PositionName(concTomNumber(Number(indexTerm)));
        subAction = `genSyntacticMatchingAutomata(subAction,p.elseAction,concSlot(PairSlotAppl(slotName,term)),p.path,moduleName,null);
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(concOption(),PositionName(newPath),termType,concConstraint());
        return genGetElementList(p, indexTerm, var, termType, subAction, ensureNotEmptyList, moduleName);
      }
      
      concSlot(PairSlotAppl[Appl=var@(VariableStar|UnamedVariableStar)[AstType=termType]],termTail*) -> {
          /*
           * 3 cases:
           * - tail = emptyList
           * - tail = only VariableStar or UnamedVariableStar
           * - tail = other
           */
        if(`termTail.isEmptyconcSlot()) {
            /*
             * generate:
             * ---------
             * Let E_n = subjectList;
             * ...
             */
          Expression source = `TomTermToExpression(Ref(p.subjectListName));
          return buildLet(`var, source, p.action, p.elseAction, moduleName);
        } else {
            /*
             * generate:
             * ---------
             * Let begin_i = subjectList;
             * LetRef end_i   = subjectList;
             * ...
             */
          Instruction subAction = genListMatchingAutomata(p,`termTail,indexTerm+1,false,moduleName);
	  TomNumberList ppath = p.path;
          TomNumberList pathBegin = `concTomNumber(ppath*,Begin(Number(indexTerm)));
          TomNumberList pathEnd = `concTomNumber(ppath*,End(Number(indexTerm)));
          TomTerm variableBeginAST = `Variable(concOption(),PositionName(pathBegin),termType,concConstraint());
          TomTerm variableEndAST   = `Variable(concOption(),PositionName(pathEnd),termType,concConstraint());

          Expression source = `GetSliceList(p.symbol.getAstName(),variableBeginAST,Ref(variableEndAST));
          Instruction let = buildLet(`var, source, subAction, p.elseAction, moduleName);
          Instruction tailExp = `Assign(variableEndAST,genGetTail(p.symbol,Ref(variableEndAST)));
          Instruction loop;
          if(containOnlyVariableStar(`termTail)) {
              /*
               * do {
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(IS_EMPTY_TomList(end_i) )
               *     end_i = begin_i
               *   else *** use this impossible value to indicate the end of the loop ***
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   subjectList = end_i;
               * } while( end_i != begin_i )  
               * *** subjectList is reseted to begin_i when the loop stops
               */
            Instruction stopIter = `Assign(variableEndAST,TomTermToExpression(variableBeginAST));
            Instruction assign1 = `genCheckEmptyList(p.symbol, Ref(variableEndAST),stopIter,tailExp);
            Instruction assign2 = `Assign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)));
            loop = `DoWhile(
                UnamedBlock(concInstruction(let,assign1,assign2)),
                Negation(EqualTerm(termType,Ref(variableEndAST),variableBeginAST)));
          } else {
              /*
               * case (X*,y,...)
               * no longer necessary to check if the list is not empty
               * to extract the 'y'
               */
            
              /*
               * while( !IS_EMPTY_TomList(end_i) ) {
               *   subjectList = end_i;
               *   * SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   end_i = (TomList) GET_TAIL_TomList(end_i);
               * }
               * subjectList = begin_i 
               */

            Instruction assign1 = tailExp;
            Instruction letAssign = `LetAssign(p.subjectListName,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,assign1)));
            loop = `WhileDo(Negation(genIsEmptyList(p.symbol,Ref(variableEndAST))),letAssign);
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListName,TomTermToExpression(variableBeginAST),Nop())));
          }

          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListName)),
                                       loop);
          Instruction letBegin = `Let(variableBeginAST,
                                      TomTermToExpression(Ref(p.subjectListName)),
                                      letEnd);
          return letBegin;
        }
      }
    }
    System.out.println("GenListMatchingAutomata strange termList: " + termList);
    throw new TomRuntimeException("GenListMatchingAutomata strange termList: " + termList);
  }

  private boolean containOnlyVariableStar(SlotList termList) {
    %match(SlotList termList) {
      !concSlot(_*,!PairSlotAppl[Appl=(VariableStar|UnamedVariableStar)[]],_*)->{
    	  return true;
      }
    }
    return false;
  }

  
  private Instruction genCheckEmptyList(TomSymbol tomSymbol, TomTerm subjectListName,
                                        Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList)) {
       *   ...
       * }
       */
    return `If(genIsEmptyList(tomSymbol, Ref(subjectListName)),succes,failure);
  }


  private Instruction genGetElementList(MatchingParameter p, int indexTerm, TomTerm var,
                                        TomType termType, Instruction subAction, boolean notEmptyList, String moduleName) {
      /*
       * generate:
       * ---------
       * Let save_i = subjectList
       * if(!IS_EMPTY_TomList(subjectList)) {
       *   Let TomTerm var = (TomTerm) GET_HEAD_TomList(subjectList);
       *   subjectList = (TomList) GET_TAIL_TomList(subjectList);
       *   ...
       * }
       * subjectList = save_i;
       */
    Instruction body = `LetAssign(p.subjectListName,genGetTail(p.symbol,Ref(p.subjectListName)),subAction);
    Expression source = genGetHead(p.symbol,termType,`Ref(p.subjectListName));
    Instruction let = buildLet(var, source, body, `Nop(), moduleName);
    if(notEmptyList) {
      let = `genCheckEmptyList(p.symbol, p.subjectListName,Nop(),let);
    }

    TomNumberList ppath = p.path;
    TomNumberList pathEnd = `concTomNumber(ppath*,End(Number(indexTerm)));
    TomType listType = p.symbol.getTypesToType().getCodomain();
    TomTerm variableEndAST = `Variable(concOption(),PositionName(pathEnd),listType,concConstraint());
    Instruction letRestore = `Assign(
        p.subjectListName,
        TomTermToExpression(variableEndAST));
    Instruction letSave = `Let(variableEndAST,
        TomTermToExpression(Ref(p.subjectListName)),
        UnamedBlock(concInstruction(let,letRestore)));
    return letSave;
  }
  
  private Expression genGetHead(TomSymbol tomSymbol, TomType type, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `GetHead(opNameAST, type, var);
  }

  private Expression genGetTail(TomSymbol tomSymbol, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `GetTail(opNameAST, var);
  }

  private Expression genIsEmptyList(TomSymbol tomSymbol, TomTerm var) {
    TomName opNameAST = tomSymbol.getAstName();
    return `IsEmptyList(opNameAST, var);
  }

    /*
     * function which compiles array-matching
     * 
     * p:         parameters (which are not modified during the matching process)
     * termList:  list of subterms
     * indexTerm: index of the considered subterm (indexTerm=1 for the first call)
     */
  Instruction genArrayMatchingAutomata(MatchingParameter p,
                                       SlotList termList,
                                       int indexTerm,
                                       boolean ensureNotEmptyList,
                                       String moduleName) {
    %match(termList) {
      concSlot() -> {
          /*
           * nothing to compile
           * just check that the subject is empty
           */
        return `genIsEmptyArray(p.symbol,p.subjectListName, p.subjectListIndex, p.action, Nop());
      }

      concSlot(PairSlotAppl[Appl=var@(Variable|UnamedVariable)[AstType=termType]],termTail*) -> {
          /*
           * get an element and store it
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true,moduleName);
        return genGetElementArray(p,indexTerm, `var, `termType, subAction, ensureNotEmptyList, moduleName);
      }

      concSlot(PairSlotAppl[Appl=term@RecordAppl[NameList=(Name(tomName),_*)]],termTail*)  -> {
          /*
           * get an element
           * perform syntactic matching
           */
        Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,true,moduleName);

        TomName slotName = `PositionName(concTomNumber(Number(indexTerm)));
        subAction = `genSyntacticMatchingAutomata(subAction,Nop(),concSlot(PairSlotAppl(slotName,term)),p.path,moduleName,null);
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);
        TomType termType = tomSymbol.getTypesToType().getCodomain();
        TomNumberList newPath  = appendNumber(indexTerm,p.path);
        TomTerm var =  `Variable(concOption(),PositionName(newPath),termType,concConstraint());

        return genGetElementArray(p, indexTerm, var, termType, subAction, ensureNotEmptyList, moduleName);
      }
        
      concSlot(PairSlotAppl[Appl=var@(VariableStar|UnamedVariableStar)[]],termTail*) -> {
          /*
           * 3 cases:
           * - tail = emptyList
           * - tail = only VariableStar or UnamedVariableStar
           * - tail = other
           */
        if(`termTail.isEmptyconcSlot()) {
            /*
             * generate:
             * ---------
             * Let E_n = GET_SLICE_L(subjectList,subjectIndex,GET_SIZE_L(subjectList));
             * ...
             */
          Expression source = `GetSliceArray(p.symbol.getAstName(),
                                             Ref(p.subjectListName),
                                             Ref(p.subjectListIndex),
                                             ExpressionToTomTerm(GetSize(p.symbol.getAstName(),p.subjectListName))
                                             );
          Instruction let = buildLet(`var, source, p.action, `Nop(), moduleName);
          return let;
        } else {
          /*
           * generate:
           * ---------
           * int begin_i = subjectIndex;
           * int end_i   = subjectIndex;
           * ...
           */
          Instruction subAction = genArrayMatchingAutomata(p,`termTail,indexTerm+1,false,moduleName);
          TomNumberList ppath = p.path;
          TomNumberList pathBegin = `concTomNumber(ppath*,Begin(Number(indexTerm)));
          TomNumberList pathEnd = `concTomNumber(ppath*,End(Number(indexTerm)));
          TomTerm variableBeginAST = `Variable(concOption(),PositionName(pathBegin),getSymbolTable(moduleName).getIntType(),concConstraint());
          TomTerm variableEndAST   = `Variable(concOption(),PositionName(pathEnd),getSymbolTable(moduleName).getIntType(),concConstraint());

          Expression source = `GetSliceArray(p.symbol.getAstName(),
                                             Ref(p.subjectListName),
                                             variableBeginAST,
                                             Ref(variableEndAST));

          Instruction let = buildLet(`var, source, subAction, `Nop(), moduleName);
          Instruction increment = `Assign(variableEndAST,AddOne(Ref(variableEndAST)));
          Instruction loop;
          if(containOnlyVariableStar(`termTail)) {
            /*
             * do {
             *   * SUBSTITUTION: E_i
             *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
             *   ...
             *   end_i++;
             *   subjectIndex = end_i;
             * } while( subjectIndex <= GET_SIZE(subjectList) )
             * subjectIndex = begin_i
             *
             * *** we need <= instead of < to make the algorithm complete ***
             */
            Instruction assign = `Assign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)));
            
            loop = `DoWhile(UnamedBlock(concInstruction(let,increment,assign)),
                            Negation(GreaterThan(TomTermToExpression(Ref(p.subjectListIndex)),GetSize(p.symbol.getAstName(),Ref(p.subjectListName)))));
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListIndex,TomTermToExpression(variableBeginAST),Nop())));
          } else {
            /*
             * while( !IS_EMPTY_TomList(end_i,subjectList) ) {
             *   subjectIndex = end_i;
             *   * SUBSTITUTION: E_i
             *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
             *   ...
             *   end_i++;
             * } 
             * subjectIndex = begin_i
             */
            Instruction letAssign = `LetAssign(p.subjectListIndex,TomTermToExpression(Ref(variableEndAST)),UnamedBlock(concInstruction(let,increment)));
            loop = `WhileDo(Negation(IsEmptyArray(p.symbol.getAstName(),Ref(p.subjectListName), Ref(variableEndAST))),
                            letAssign);
            loop = `UnamedBlock(concInstruction(loop,LetAssign(p.subjectListIndex,TomTermToExpression(variableBeginAST),Nop())));
          }
          Instruction letEnd = `LetRef(variableEndAST,
                                       TomTermToExpression(Ref(p.subjectListIndex)),
                                       loop);
          Instruction letBegin = `Let(variableBeginAST,
                                      TomTermToExpression(Ref(p.subjectListIndex)),
                                      letEnd);
          return letBegin;
        }
      }
    }
    System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
    throw new TomRuntimeException("GenArrayMatchingAutomata strange termList: " + termList);
  }

  private Instruction genIsEmptyArray(TomSymbol tomSymbol,
                                      TomTerm subjectListName,
                                      TomTerm subjectListIndex,
                                     Instruction succes, Instruction failure) {
      /*
       * generate:
       * ---------
       * if(IS_EMPTY_TomList(subjectList,subjectIndex)) {
       *   ...
       * }
       */
    TomName opNameAST = tomSymbol.getAstName();
    
    return `If(IsEmptyArray(opNameAST,Ref(subjectListName),Ref(subjectListIndex)),succes,failure);
  }


  private Instruction genGetElementArray(MatchingParameter p, int indexTerm, TomTerm var,
                                         TomType termType,
                                         Instruction subAction, 
                                         boolean notEmptyList,
                                         String moduleName) {
      /*
       * generate:
       * ---------
       * Let save_i = subjectListIndex
       * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
       *   Let TomTerm var = (TomTerm) GET_ELEMENT(subjectList, sibjectListIndex);
       *   subjectListIndex++;
       *   ...
       * }
       * subjectListIndex = save_i;
       */
    Instruction body = `LetAssign(p.subjectListIndex,AddOne(Ref(p.subjectListIndex)),subAction);
    Expression source = `GetElement(p.symbol.getAstName(),termType,p.subjectListName,Ref(p.subjectListIndex));
    Instruction let = buildLet(var, source, body, `Nop(), moduleName);
    if(notEmptyList) {
      let = `genIsEmptyArray(p.symbol,p.subjectListName,p.subjectListIndex,Nop(),let);
    }

    TomNumberList ppath = p.path;
    TomNumberList pathEnd = `concTomNumber(ppath*,End(Number(indexTerm)));
    TomType intType = getSymbolTable(moduleName).getIntType();
    TomTerm variableEndAST = `Variable(concOption(),PositionName(pathEnd),intType,concConstraint());
    Instruction letRestore = `Assign(
        p.subjectListIndex,
        TomTermToExpression(variableEndAST));
    Instruction letSave = `Let(variableEndAST,
        TomTermToExpression(Ref(p.subjectListIndex)),
        UnamedBlock(concInstruction(let,letRestore)));
    return letSave;

  }


    /*
     * given a list of slots [s1=t1],...,[sn=tn]
     * declare/assign internal matching variables: match_path_i = ti
     */
  private Instruction collectSubtermFromSubjectList(TomTerm currentTerm,
                                                    TomTerm subjectVariableAST, 
                                                    TomNumberList path, Instruction body, String moduleName) {
    %match(currentTerm) {
      RecordAppl[NameList=nameList@(Name(tomName),_*), Slots=termArgList] -> {
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);

        // check that variables are no longer Bottom 
        TomType booleanType = getSymbolTable(moduleName).getBooleanType();
        TomTerm booleanVariable = `Variable(concOption(),PositionName(concTomNumber(NameNumber(Name("bool")),path*)),booleanType,concConstraint());
        Instruction ifBody = `collectSubtermIf(nameList,booleanVariable,currentTerm,termArgList,subjectVariableAST,path,moduleName);
        Instruction checkBody = `If(TomTermToExpression(booleanVariable),body,Nop()); 
        Instruction newBody = `collectSubtermLetRefBottom(termArgList,tomSymbol,path,AbstractBlock(concatInstruction(ifBody,checkBody)));

        return `LetRef(booleanVariable,FalseTL(),newBody);

      }
    }
    return body;
  }

    /*
     * given a list of symbol names
     * generated nested if, for each symbol name
     */
  private Instruction collectSubtermIf(TomNameList nameList,
                                       TomTerm booleanVariable,
                                       TomTerm currentTerm,
                                       SlotList termArgList,
                                       TomTerm subjectVariableAST, 
                                       TomNumberList path,
                                       String moduleName) {
    %match(nameList) {  
      concTomName() -> {
        return `Nop();
      }

      concTomName(name@Name(tomName),tail*) -> {
        TomSymbol tomSymbol = getSymbolTable(moduleName).getSymbolFromName(`tomName);
        TomType codomain = tomSymbol.getTypesToType().getCodomain();
        Instruction elseBody = `collectSubtermIf(tail,booleanVariable,currentTerm,termArgList,subjectVariableAST,path,moduleName);
        Instruction assign = `collectSubtermLetAssign(termArgList,tomSymbol,subjectVariableAST,path,Nop(),moduleName);
        Expression cond = `EqualFunctionSymbol(codomain,subjectVariableAST,currentTerm.setNameList(concTomName(name)));
        return  `If(cond,LetAssign(booleanVariable,TrueTL(),assign),elseBody);
      }
    }
    return `Nop();
  }

   /*
    * given a list of slots
    * generate assignements for each subterm
    */
  private Instruction collectSubtermLetAssign(SlotList termArgList,
                                              TomSymbol tomSymbol,
                                              TomTerm subjectVariableAST, 
                                              TomNumberList path, Instruction body,
                                              String moduleName) {
    TomName opNameAST = tomSymbol.getAstName();
    %match(termArgList) { 
      concSlot() -> { return body; }
     
      concSlot(PairSlotAppl(slotName,_),tail*) -> {
        body = collectSubtermLetAssign(`tail,tomSymbol,subjectVariableAST,path,body,moduleName);
        TomType subtermType = getSlotType(tomSymbol,`slotName);

        if(!isDefinedGetSlot(tomSymbol,`slotName)) {
          Logger.getLogger(getClass().getName()).log( Level.SEVERE,
              TomMessage.errorMissingSlotDecl.getMessage(),
              new Object[]{tomSymbol.getAstName().getString(),`slotName.getString()});
        }

        Expression getSlotAST = `GetSlot(subtermType,opNameAST,slotName.getString(),subjectVariableAST);

        TomNumberList newPath  = `concTomNumber(path*,NameNumber(slotName));
        TomTerm newVariableAST = `Variable(concOption(),PositionName(newPath),subtermType,concConstraint());
        return `LetAssign(newVariableAST,getSlotAST,body);
      }
    }
    return `Nop();
  }
 
  /*
   * given a list of slot
   * declare/initialize each slot to "bottom"
   */
  private Instruction collectSubtermLetRefBottom(SlotList termArgList,
                                                 TomSymbol tomSymbol,
                                                 TomNumberList path,
                                                 Instruction body) {
    %match(termArgList) { 
      concSlot() -> {
        return body;
      }
     
      concSlot(PairSlotAppl(slotName,_),tail*) -> {
        body = collectSubtermLetRefBottom(`tail,tomSymbol,path,body);
        TomType subtermType = getSlotType(tomSymbol,`slotName);
        TomNumberList newPath  = `concTomNumber(path*,NameNumber(slotName));
        TomTerm newVariableAST = `Variable(concOption(),PositionName(newPath),subtermType,concConstraint());
        return `LetRef(newVariableAST,Bottom(subtermType),body);
      }
    }
    return `Nop();
  }

    /*
     * given a list of subject t1,...,tn
     * declare/assign internal matching variables: match_path_i = ti
     */
  private Instruction collectSubtermFromTomSymbol(SlotList termArgList, TomSymbol tomSymbol,
      TomTerm subjectVariableAST, TomNumberList path, Instruction body, String moduleName) {	  
    TomName opNameAST = tomSymbol.getAstName();
    %match(termArgList) { 
      concSlot() -> { return body; }
      
      concSlot(PairSlotAppl(slotName,_),tail*) -> {
        body = collectSubtermFromTomSymbol(`tail,tomSymbol,subjectVariableAST,path,body,moduleName);
        TomType subtermType = getSlotType(tomSymbol,`slotName);
        if(!isDefinedGetSlot(tomSymbol,`slotName)) {
          Logger.getLogger(getClass().getName()).log( Level.SEVERE,
              TomMessage.errorMissingSlotDecl.getMessage(),
              new Object[]{tomSymbol.getAstName().getString(),`slotName.getString()});
        }

        Expression getSubtermAST = `GetSlot(subtermType,opNameAST,slotName.getString(),subjectVariableAST);
        TomNumberList newPath  = `concTomNumber(path*,NameNumber(slotName));
        TomTerm newVariableAST = `Variable(concOption(),PositionName(newPath),subtermType,concConstraint());
        return `Let(newVariableAST,getSubtermAST,body);
      }
    }
    return `Nop();
  }

  public Expression expandDisjunction(Expression exp, String moduleName) {
    Expression cond = `FalseTL();
    %match(exp) {
      EqualFunctionSymbol(termType,exp1,RecordAppl[Option=option,NameList=nameList,Slots=l]) -> {
        while(!`nameList.isEmptyconcTomName()) {
          TomName name = `nameList.getHeadconcTomName();
          boolean isAnti = (name instanceof AntiName);
          if (isAnti) {
        	  name = name.getName();
          }
          Expression check = `EqualFunctionSymbol(termType,exp1,RecordAppl(option,concTomName(name),l,concConstraint()));
          
          if ( isAnti ){
        	  check = `Negation(check);
          }
          cond = `Or(check,cond);
          `nameList = `nameList.getTailconcTomName();
        }
      }
    }
    return cond;
  }

  private Instruction buildLet(TomTerm dest,
                               Expression source,
                               Instruction body,
                               Instruction elseBody,
                               String moduleName) {
      // Take care of constraints	 
    body = compileConstraint(dest,source,body,elseBody,moduleName);    
    //return `Let(dest,source,body);
    /*
     * this optimisation is not good since it avoids some optimisations
     * in particular, f(x,y) and f(x,_) cannot be merged
     *
     */
    if(dest.isUnamedVariable() || dest.isUnamedVariableStar()) {
      // This is an optimisation 
      // Do not assign an UnamedVariable or an UnamedVariableStar
      return body;
    } else {
      return `Let(dest,source,body);
    }
    
  }

  private Instruction compileConstraint(TomTerm subject, Expression source, 
		  Instruction body, Instruction elseBody, String moduleName) {	 
//	if (antiConstraintFirstTime){	
//		  antiConstraintFirstTime = false;      
//		  // add flag variable declaration
//		  return `LetRef(antiFlagVariable,TrueTL(),compileConstraint(subject, source, 
//    		  body, elseBody, moduleName));
//		  
//	}
    %match(subject) {
      (Variable|VariableStar)[Constraints=constraints] -> {
        return buildConstraint(`constraints,`TomTermToExpression(subject.setConstraints(concConstraint())),body,elseBody,moduleName);
      }

      (UnamedVariable|UnamedVariableStar)[Constraints=constraints] -> {
        return buildConstraint(`constraints,source,body,elseBody,moduleName);
      }

      RecordAppl[Constraints=constraints] -> {
        return buildConstraint(`constraints,source,body,elseBody,moduleName);
      }
    }
    throw new TomRuntimeException("compileConstraint: strange subject: " + subject);
  }

  private Instruction buildConstraint(ConstraintList constraints, Expression source, 
		  Instruction body, Instruction elseBody, String moduleName) {
    %match(constraints) {
      concConstraint() -> {
        return body;
      }

      concConstraint(Equal(var) ,tail*) -> {
        //System.out.println("constraint: " + source + " EqualTo " + `var);
        Instruction subBody = compileConstraint(`var,source,body,elseBody,moduleName);
        return `buildConstraint(tail,source,If(EqualTerm(getTermType(var, getSymbolTable(moduleName)),
        		var,ExpressionToTomTerm(source)),subBody,elseBody),elseBody,moduleName);
      }

      concConstraint(AssignTo(var@(Variable|VariableStar)[]) ,tail*) -> {
        //System.out.println("constraint: " + source + " AssignTo " + `var);
        Instruction subBody = compileConstraint(`var,source,body,elseBody,moduleName);
        return `buildConstraint(tail,source,Let(var,source,subBody),elseBody,moduleName);
      }

      concConstraint(Ensure(exp) ,tail*) -> {
        //System.out.println("constraint: " + source + " Ensure " + `exp);
        //Instruction subBody = compileConstraint(`exp,source,body);
        TomType type = getSymbolTable(moduleName).getBooleanType();
        Expression equality = `EqualTerm(type,ExpressionToTomTerm(TrueTL()),exp);
        Instruction generatedTest = `If(equality,body,elseBody);
        return `buildConstraint(tail,source,generatedTest,elseBody,moduleName);
      }
      
      concConstraint(c@AndAntiConstraint(_*), tail*) -> {
    	 
    	Instruction antiMatchBlock = null;    	  
    	antiMatchBlock = buildAntiMatchBlockConstraint((AndAntiConstraint)`c,source,body,moduleName);
    	return `buildConstraint(tail,source,antiMatchBlock,elseBody,moduleName);
      }

      concConstraint(head,_*) -> {
        throw new TomRuntimeException("buildConstraint: unknown constraint: " + `head);
      }
    }    
    throw new TomRuntimeException("buildConstraint: unknown constraints: " + constraints);
  } 
  
  
  private Instruction buildAntiMatchBlockConstraint(AndAntiConstraint consList, 
      Expression source, Instruction body, String moduleName) {

    InstructionList instructionList = null;
    Instruction antiInstruction = null; 
    Constraint antiMatchConstraint = null;

    Instruction assignAntiFlagTrue = `LetAssign(
        antiFlagVariable,
        And(TrueTL(),TomTermToExpression(Ref(antiFlagVariable))),
        Nop());
    Instruction assignAntiFlagFalse = `LetAssign(
        antiFlagVariable,
        And(FalseTL(),TomTermToExpression(Ref(antiFlagVariable))),
        Nop());

    // actions that will be performed on if and on else
    Instruction ifAction = null, elseAction = null;

    // compute all anti constraints	  
    antiMatchConstraint = consList.getHeadAndAntiConstraint();
    boolean actionOnIf = antiMatchConstraint.getActionOnIf() == 1;
    if (actionOnIf){
      ifAction = assignAntiFlagTrue;
      elseAction = assignAntiFlagFalse;
    }else{
      ifAction = assignAntiFlagFalse;
      elseAction = assignAntiFlagTrue;
    }

    antiInstruction = buildAntiMatchConstraint((AntiMatchConstraint)antiMatchConstraint,
	source,ifAction,elseAction,moduleName);
    instructionList = `concInstruction(antiInstruction);
    consList = (AndAntiConstraint)consList.getTailAndAntiConstraint();
    // this evaluates all constraints at the same 'not' level as the above one
    while(!consList.isEmptyAndAntiConstraint()){
      antiMatchConstraint = consList.getHeadAndAntiConstraint();
      antiInstruction = buildAntiMatchConstraint((AntiMatchConstraint)antiMatchConstraint,
	  source, ifAction, elseAction, moduleName);

      if (antiMatchConstraint.getActionOnIf() == 0) {
        antiInstruction = `If(
            EqualTerm(
              antiFlagType,
              Ref(antiFlagVariable),
              ExpressionToTomTerm(TrueTL())),
            antiInstruction,
            Nop());
      } else {
        antiInstruction = `If(
            EqualTerm(
              antiFlagType,
              Ref(antiFlagVariable),
              ExpressionToTomTerm(TrueTL())),
            Nop(),
            antiInstruction);
      }		  
      // add intruction to the list
      instructionList = `concInstruction(instructionList*,antiInstruction);

      consList = (AndAntiConstraint)consList.getTailAndAntiConstraint();		  					  
    }
    // add the body to the list if the body is not the flag assignement
    // quite ugly ... should be changed
    if (!(body instanceof LetAssign)) {
    	// also reset the flag
    	Instruction flagReset = `LetAssign(antiFlagVariable,TrueTL(),Nop());
    	instructionList = `concInstruction(instructionList*,body,flagReset);
    }
    return `UnamedBlock(instructionList);
  }

  private Instruction buildAntiMatchConstraint(AntiMatchConstraint constraint, 
      Expression source, Instruction ifAction, Instruction elseAction, String moduleName) {
    //SlotList slotList = tomListToSlotList(`concTomTerm(constraint.getPattern())); 
    TomNumberList rootpath = `concTomNumber();
    this.matchNumber++;
    rootpath = `concTomNumber(rootpath*,MatchNumber(Number(this.matchNumber)));

    return genSyntacticMatchingAutomata(ifAction,elseAction,
	`concSlot(PairSlotAppl(Name("ANTI"),constraint.getPattern())),rootpath,moduleName,
	constraint.getSubject());       
  }
  
  private static class MatchingParameter {
      /*
       * This object is used by matching-algorithms to store common parameters
       * which are not modified during the matching process
       *
       * symbol:           root symbol
       * path:             path up-to the root symbol
       * action:           list of actions to be fired when matching
       * subjectListName:  name of the internal variable supposed to store the subject
       * subjectListIndex: name of the internal variable supposed to store the index
       */
    public TomSymbol symbol;
    public TomNumberList path;
    public Instruction action;
    public Instruction elseAction;
    public TomTerm subjectListName;
    public TomTerm subjectListIndex;

    MatchingParameter(TomSymbol symbol, 
                      TomNumberList path,
                      Instruction action,
                      Instruction elseAction,
                      TomTerm subjectListName,
                      TomTerm subjectListIndex) {
      this.symbol=symbol;
      this.path=path;
      this.action=action;
      this.elseAction=elseAction;
      this.subjectListName=subjectListName;
      this.subjectListIndex=subjectListIndex;
    }

  }
  
} // end of class
