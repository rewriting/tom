/*
  
    TOM - To One Matching Compiler

    Copyright (C) 2000-2003  LORIA (CNRS, INPL, INRIA, UHP, U-Nancy 2)
			     Nancy, France.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA

    Pierre-Etienne Moreau	e-mail: Pierre-Etienne.Moreau@loria.fr
    Christophe Mayer            ESIAL Student
    Julien Guyon

*/

package jtom.checker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jtom.TomBase;
import jtom.adt.*;
import jtom.runtime.Collect1;
import jtom.tools.TomTask;
import aterm.ATerm;
import jtom.TomEnvironment;
import jtom.tools.TomTaskInput;
import jtom.xml.Constants;
import jtom.exception.*;
import java.lang.Throwable;

abstract class TomChecker extends TomBase implements TomTask {
	// ------------------------------------------------------------
%include { ../adt/TomSignature.tom }
	// ------------------------------------------------------------
	class TermDescription {
		int termClass, decLine;
		String name, type;
		public TermDescription(int termClass, String name, int decLine, String type) {
			this.termClass = termClass;
			this.decLine = decLine;
			this.name = name;
			this.type = type;
		}
	}
	
		// TomTask Interface 
	public void addTask(TomTask task) { this.nextTask = task; }
	public TomTask getTask() { return nextTask; }

	
	protected TomTaskInput input;
	protected TomTask nextTask;
	protected boolean strictType = false, warningAll = false, noWarning = false;
  private Option currentTomStructureOrgTrack;
  private int nullInteger = -1;
  private List errorMessage = new ArrayList();
	private ArrayList alreadyStudiedSymbol =  new ArrayList();
	private ArrayList alreadyStudiedType =  new ArrayList();  
  private final static int APPL = 0;
	private final static int RECORD_APPL = 1;
	private final static int XML_APPL = 2;
	private final static int VARIABLE_STAR = 3;
	private final static int UNAMED_VARIABLE_STAR = 4;
	private final static int PLACE_HOLDER = 5;
	private final static String OPERATOR = "%op";
	private final static String OP_ARRAY = "%oparray";
	private final static String OP_LIST = "%oplist";
	private final static String TYPE_TERM = "%typeterm";
	private final static String TYPE_ARRAY = "%typearray";
	private final static String TYPE_LIST = "%typelist";
	
	private final static String GET_FUN_SYM = "get_fun_sym";
	private final static String CMP_FUN_SYM = "cmp_fun_sym";
	private final static String EQUALS = "equals";
	private final static String GET_SUBTERM = "get_subterm";
	private final static String GET_ELEMENT = "get_element";
	private final static String GET_SIZE = "get_size";
	private final static String GET_HEAD = "get_head";
	private final static String GET_TAIL = "get_tail";
	private final static String IS_EMPTY = "is_empty";
	private final static String MAKE_APPEND = "make_append";
	private final static String MAKE_EMPTY = "make_empty";
	private final static String MAKE_INSERT = "make_insert";
	private final static String MAKE = "make";
  
  public TomChecker(TomEnvironment environment) { 
    super(environment);
  }
 
  public int getNumberFoundError() {
    return errorMessage.size();
  }
  
  public String getMessage(int n) {
    return (String)errorMessage.get(n);
  }
  
		/** Main syntax checking entry point: We check all interesting Tom Structure */
  protected void checkSyntax(TomTerm parsedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
							DeclarationToTomTerm(declaration) -> { verifyDeclaration(declaration); return false; }
              Match(SubjectList(matchArgsList), PatternList(patternActionList), list) -> {  
                currentTomStructureOrgTrack = findOriginTracking(list);
                verifyMatch(matchArgsList, patternActionList); return true;
              }
							RuleSet(list, orgTrack) -> {
								currentTomStructureOrgTrack = orgTrack;
								verifyRule(list); return false;
							}
							backquote@BackQuoteAppl[] -> { 
								permissiveVerify(backquote); return false; 
							}
              Appl(options,Name(name),args) -> { 
              	verifyApplStructure(options, name, args); return true; 
              }
              RecordAppl(options,Name(name),args) ->{
                verifyRecordStructure(options, name, args); return true;
              }
							XMLAppl(options,Name(name), list1, list2) ->{
								verifyXMLApplStructure(options, name, list1, list2); return true;
							}
              _ -> { return true; }
            }
          } else {
            return true;
          }
        }// end apply
      }; // end new
    traversal().genericCollect(parsedTerm, collectAndVerify);   
  }

		/** SYMBOL AND TYPE CONCERNS */
  private void verifyDeclaration(Declaration declaration) {
  	
		%match (Declaration declaration) {
			SymbolDecl(Name(tomName)) -> {
				TomSymbol tomSymbol = getSymbol(tomName);
				verifySymbol(OPERATOR, tomSymbol);
			}
			ArraySymbolDecl(Name(tomName)) -> {
				TomSymbol tomSymbol = getSymbol(tomName);
				verifySymbol(OP_ARRAY, tomSymbol);
			}
			ListSymbolDecl(Name(tomName)) -> {
				TomSymbol tomSymbol = getSymbol(tomName);
				verifySymbol(OP_LIST, tomSymbol);
			}
			TypeTermDecl(Name(tomName), tomList, orgTrack) -> {
				currentTomStructureOrgTrack = orgTrack;
				verifyMultipleDefinitionOfType(tomName,alreadyStudiedType);
				verifyTypeDecl(TYPE_TERM, tomList);
			}
 	     
			TypeListDecl(Name(tomName), tomList, orgTrack) -> {
				currentTomStructureOrgTrack = orgTrack;
				verifyMultipleDefinitionOfType(tomName,alreadyStudiedType);
				verifyTypeDecl(TYPE_LIST, tomList);
			}
 	     
			TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
				currentTomStructureOrgTrack = orgTrack;
				verifyMultipleDefinitionOfType(tomName,alreadyStudiedType);
				verifyTypeDecl(TYPE_ARRAY, tomList);
			}
		}
	}

		/** TYPE DECLARATION CONCERNS */
	private void verifyTypeDecl(String declType, TomList listOfDeclaration) {
		ArrayList verifyList = new ArrayList();
		verifyList.add(GET_FUN_SYM);
		verifyList.add(CMP_FUN_SYM);
		verifyList.add(EQUALS);
		
		if(declType == TYPE_TERM)	{
			verifyList.add(GET_SUBTERM);
		} else if(declType == TYPE_ARRAY) {
			verifyList.add(GET_ELEMENT);
			verifyList.add(GET_SIZE);
		} else if(declType == TYPE_LIST) {
			verifyList.add(GET_HEAD);
			verifyList.add(GET_TAIL);
			verifyList.add(IS_EMPTY);
		} else {
			System.out.println("Invalid verifyTypeDecl parameter: "+declType);return;
		}
 	   
		while(!listOfDeclaration.isEmpty()) {
			TomTerm term = listOfDeclaration.getHead();
			%match (TomTerm term) {
				DeclarationToTomTerm(GetFunctionSymbolDecl[orgTrack=orgTrack]) -> {
					checkField(GET_FUN_SYM,verifyList,orgTrack, declType);
				}
				DeclarationToTomTerm(CompareFunctionSymbolDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
					checkFieldAndLinearArgs(CMP_FUN_SYM,verifyList,orgTrack,name1,name2, declType);
				}
				DeclarationToTomTerm(TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
					checkFieldAndLinearArgs(EQUALS,verifyList,orgTrack,name1,name2, declType);
				}
					/*Specific to typeterm*/
				DeclarationToTomTerm(GetSubtermDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
					checkFieldAndLinearArgs(GET_SUBTERM,verifyList,orgTrack,name1,name2, declType);
				}
					/*Specific to typeList*/
				DeclarationToTomTerm(GetHeadDecl[orgTrack=orgTrack]) -> {
					checkField(GET_HEAD,verifyList,orgTrack, declType);
				}
				DeclarationToTomTerm(GetTailDecl[orgTrack=orgTrack]) -> {
					checkField(GET_TAIL,verifyList,orgTrack, declType);
				}
				DeclarationToTomTerm(IsEmptyDecl[orgTrack=orgTrack]) -> {
					checkField(IS_EMPTY,verifyList,orgTrack, declType);
				}
					/*Specific to typeArray*/
				DeclarationToTomTerm(GetElementDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> { 
					checkFieldAndLinearArgs(GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
				}
				DeclarationToTomTerm(GetSizeDecl[orgTrack=orgTrack]) -> {
					checkField(GET_SIZE,verifyList,orgTrack, declType);
				}
			}
			listOfDeclaration = listOfDeclaration.getTail();
		}
 	   
		if(verifyList.contains(EQUALS)) {
			verifyList.remove(verifyList.indexOf(EQUALS));
		}    
		if(!verifyList.isEmpty()) {
			messageMissingMacroFunctions(declType, verifyList);
		}
	}
  
	private void verifyMultipleDefinitionOfType(String name, ArrayList alreadyStudiedType) {
		if(alreadyStudiedType.contains(name)) {
			messageTypeErrorYetDefined(name);
		} else {
			alreadyStudiedType.add(name);
		}
	}
 	 
	private void messageTypeErrorYetDefined(String name) {
		int declLine = currentTomStructureOrgTrack.getLine();
		String s = "Multiple definition of type: Type '"+ name +"' is already defined";
		messageError(declLine, s);
	}
 	 
		/** SYMBOL DECLARATION CONCERNS */
	private void verifySymbol(String symbolType, TomSymbol tomSymbol){
		int nbArgs=0;
		OptionList optionList = tomSymbol.getOption();
			// We save first the origin tracking of the symbol declaration
		currentTomStructureOrgTrack = findOriginTracking(optionList);
		TomTypeList l = getSymbolDomain(tomSymbol);
		TomType type = getSymbolCodomain(tomSymbol);
		String name = tomSymbol.getAstName().getString();
		int line  = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		SlotList slotList = tomSymbol.getSlotList();
		verifyMultipleDefinitionOfSymbol(name, line, alreadyStudiedSymbol);
		verifySymbolCodomain(type.getString(), name, line);
		verifySymbolArguments(l, name, line);
		verifySymbolOptions(symbolType, optionList);
	}
 	 
	private void verifyMultipleDefinitionOfSymbol(String name, int line, ArrayList alreadyStudiedSymbol) {
		if(alreadyStudiedSymbol.contains(name)) {
			messageOperatorErrorYetDefined(name,line);
		} else {
			alreadyStudiedSymbol.add(name);
		}
	}
 	 
	private void messageOperatorErrorYetDefined(String name, int line) {
		String s = "Multiple definition of operator: Operator '"+ name +"' is already defined";
		messageError(line, s);
	}
 	 
	private void verifySymbolCodomain(String returnTypeName, String symbName, int symbLine) {
		if (symbolTable().getType(returnTypeName) == null){
			messageTypeOperatorError(returnTypeName, symbName, symbLine);
		}
	}
 	 
	private void messageTypeOperatorError(String type, String name, int line) {
		String s = "Operator '" + name + "' has an unknown return type: '" + type + "'";
		messageError(line,s);
	}
 	 
	private void verifySymbolArguments(TomTypeList args, String symbName, int symbLine) {
		TomType type;
		int nbArgs = 0;
		while(!args.isEmpty()) {
			type = args.getHead();
			verifyTypeExist(type.getString(), nbArgs, symbName, symbLine);
			nbArgs++;
			args = args.getTail();
		}
	}
 	 
	private void verifyTypeExist(String typeName, int slotPosition, String symbName, int symbLine) {
		if (symbolTable().getType(typeName) == null){
			messageTypesOperatorError(typeName, slotPosition, symbName, symbLine);
		}
	}
 	 
	private void messageTypesOperatorError(String type, int slotPosition, String name, int line) {
		String s = "Slot "+slotPosition + " in operator '"+ name + "' signature has an unknown type: '" + type + "'";
		messageError(line,s);
	}
 	 
	private void verifySymbolOptions(String symbType, OptionList list) {
		ArrayList verifyList = new ArrayList();
		boolean foundOpMake = false;
		if(symbType == OPERATOR){
		} else if(symbType == OP_ARRAY ) {
			verifyList.add(MAKE_EMPTY);
			verifyList.add(MAKE_APPEND);
		} else if(symbType == OP_LIST) {
			verifyList.add(MAKE_EMPTY);
			verifyList.add(MAKE_INSERT); 
		} else {
			System.out.println("Invalid verifySymbolOptions parameter: "+symbType);
		}
 	   
		while(!list.isEmpty()) {
			Option term = list.getHead();
			%match(Option term ) {
					/* for a array symbol */
				DeclarationToOption(MakeEmptyArray[orgTrack=orgTrack]) -> { 
					checkField(MAKE_EMPTY,verifyList,orgTrack, symbType);
				}
				DeclarationToOption(MakeAddArray[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack]) -> {
					checkFieldAndLinearArgs(MAKE_APPEND, verifyList, orgTrack, name1, name2, symbType);
				}
					/*for a List symbol*/
				DeclarationToOption(MakeEmptyList[orgTrack=orgTrack]) -> {
					checkField(MAKE_EMPTY,verifyList,orgTrack, symbType);         
				}
				DeclarationToOption(MakeAddList[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack]) -> {
					checkFieldAndLinearArgs(MAKE_INSERT, verifyList, orgTrack, name1, name2, symbType);
				}
					/*for a symbol*/
				DeclarationToOption(MakeDecl[args=makeArgsList, orgTrack=orgTrack]) -> {
					if (!foundOpMake) {
						foundOpMake = true;
						verifyMakeDeclArgs(makeArgsList, orgTrack, symbType);
					} else {
						messageMacroFunctionRepeated(MAKE, orgTrack, symbType);
					}
				}
				_ -> {      list = list.getTail();}
			}
		}
		if(!verifyList.isEmpty()) {
			messageMissingMacroFunctions(symbType, verifyList);
		}
	}
	
	private void verifyMakeDeclArgs(TomList argsList, Option orgTrack, String symbType){
		// we test the necessity to use different names for each variable-parameter.
		ArrayList listVar = new ArrayList();
		while(!argsList.isEmpty()) {
			TomTerm termVar = argsList.getHead();
			%match(TomTerm termVar) {
				Variable[option=listOption, astName=Name(name)] -> {
					if(listVar.contains(name)) {
						messageTwoSameNameVariableError("make", name, orgTrack, symbType);
					} else {
						listVar.add(name);
					}
				}
			}
			argsList = argsList.getTail();
		}
	}
	
	private void checkField(String field, ArrayList findFunctions, Option orgTrack, String declType) {
		if(findFunctions.contains(field)) {
			findFunctions.remove(findFunctions.indexOf(field)); 
		} else {
			messageMacroFunctionRepeated(field, orgTrack, declType);
		}
	}
 	 
	private void checkFieldAndLinearArgs(String field, ArrayList findFunctions, Option orgTrack, String name1, String name2, String declType) {
		checkField(field,findFunctions, orgTrack, declType);
		if(name1.equals(name2)) { 
			messageTwoSameNameVariableError(field, name1, orgTrack, declType);
		}
	}
 	 
	private void messageMacroFunctionRepeated(String nameFunction, Option orgTrack, String declType) {
		int line = orgTrack.getLine(), declLine = currentTomStructureOrgTrack.getLine();
		String nameDecl = currentTomStructureOrgTrack.getAstName().getString();
		String s = "Repeated macro-function '"+nameFunction+"' in '"+declType+" "+nameDecl+"' declared line "+declLine;
		messageError(line, s);
	}
 	 
	private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType) {
		int line = orgTrack.getLine(), declLine = currentTomStructureOrgTrack.getLine();
		String nameDecl = currentTomStructureOrgTrack.getAstName().getString();
		String s =  "Arguments must be linear in method '"+nameFunction+"' of '"+declType+" "+nameDecl+"' declared at line "+declLine+" :Variable '"+nameVar+"' is repeated";
		messageError(line, s);
	}
 	 
	private void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
		int line = currentTomStructureOrgTrack.getLine();
		String name = currentTomStructureOrgTrack.getAstName().getString();
		String s = "Missing macro-function(s) "+list+" in '"+nameConstruct+" "+name+"'";
		messageError(line, s);
	}
 	 
    /** MATCH VERIFICATION CONCERNS */
    // Given a MatchConstruct's subject list and pattern-action list
  private void verifyMatch(TomList subjectList, TomList patternList) {
    ArrayList typeMatchArgs = new ArrayList();
			// From the subjects list(match definition), we test each used type and keep them in memory
		%match(TomList subjectList) {
			concTomTerm(_*, TLVar(name, TomTypeAlone(type)), _*) -> {
				if(symbolTable().getType(type) == null) {
					messageMatchTypeVariableError(name, type);
				}
				typeMatchArgs.add(type);				
			}
		}	
		int nbExpectedArgs = typeMatchArgs.size();
		// Then control each pattern vs the match definition
		%match(TomList patternList) {
			concTomTerm(_*, PatternAction[termList=TermList(terms)], _*) -> {
				verifyMatchPattern(terms, typeMatchArgs, nbExpectedArgs);
			}
		}
  }

    // For each Pattern we count and collect type information
    // but also we test that terms are well formed
    // No top variable star are allowed
  private void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    ArrayList foundTypeMatch = new ArrayList();
    int line = nullInteger;
    int nbFoundArgs = 0;
    
		%match(TomList termList) {
			concTomTerm(_*, term, _*) -> {
				line = findOriginTrackingLine(term.getOption());
                                ensureOriginTrackingLine(line);
				nbFoundArgs++;
				if(nbFoundArgs > nbExpectedArgs) {
					messageMatchErrorNumberArgument(nbExpectedArgs, nbFoundArgs, line);
			 		return;
				}
				TermDescription termDesc = analyseTomTerm(term);
				// Var* and _* are not allowed as top leftmost symbol
				if(termDesc.termClass == UNAMED_VARIABLE_STAR || termDesc.termClass == VARIABLE_STAR) {
					messageMatchErrorVariableStar(termDesc.name, termDesc.decLine);
				}
				// ensure type coherence if strictType
				if(strictType) {
					if(termDesc.type != null && !termDesc.type.equals(typeMatchArgs.get(nbFoundArgs-1)) ) {
						messageMatchErrorTypeArgument(nbFoundArgs, (String)typeMatchArgs.get(nbFoundArgs-1), termDesc.type, termDesc.decLine); 
					}
				}
			}
		}
		if(nbFoundArgs < nbExpectedArgs) {
			messageMatchErrorNumberArgument(nbExpectedArgs, nbFoundArgs, line);
		}
  }
  
  private void messageMatchErrorNumberArgument(int nbExpectedVar, int nbFoundVar, int line) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Bad number of arguments: "+nbExpectedVar+" argument(s) required but "+nbFoundVar+" found in %match structure declared line "+declLine; 
    messageError(line,s);
  }
  
  private void messageMatchErrorTypeArgument(int slotNumber, String expectedType, String givenType, int line) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s =  "Bad type for slot "+ slotNumber +" :Type '"+expectedType+"' required but Type '"+givenType+"' found in %match declared line "+declLine;
    messageError(line, s);
  }
  
  private void messageMatchTypeVariableError(String name, String type) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Variable '" + name + "' has an unknown type '"+type+"' in %match construct declared line "+declLine;
    messageError(declLine,s);
  }
  
  private void messageMatchErrorVariableStar(String nameVariableStar, int line) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Single list variable '"+nameVariableStar+"' is not allowed on left most part of %match structure declared line "+declLine;
    messageError(line,s);
  }
  
	/** RULE VERIFICATION CONCERNS */
	private void verifyRule(TomRuleList ruleList) {
		int ruleNumber = 0;
		String name = "Unknown return type";
		
		%match(TomRuleList ruleList) {	// for each rewrite rule
			concTomRule(_*, RewriteRule(Term(lhs),Term(rhs),condList,option),_*) -> {
				//Each Lhs shall start with the same production name
				name = verifyLhsRuleAndConstructorEgality(lhs, name, ruleNumber);
				if(ruleNumber == 0) {
						// update the root of lhs: it becomes a defined symbol
					ast().updateDefinedSymbol(symbolTable(),lhs);
				}
				TomType lhsType = getSymbolCodomain(getSymbol(name));
				verifyRhsRuleStructure(rhs, lhsType);
				ruleNumber++;
			}
		}
	}
  
	private String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  ruleName, int ruleNumber) {
		String methodName = "";
		OptionList options = null;

		%match(TomTerm lhs) {
			Appl[option=optionList, astName=Name(name)] |
			RecordAppl[option=optionList, astName=Name(name)] -> {
				checkSyntax(lhs);
					/* lhs outermost symbol shall have a corresponding make */
				TomSymbol symb = getSymbol(name);
				if (symb == null) {
					messageRuleErrorUnknownSymbol(name);
					return methodName;
				}
				if ( !findMakeDeclOrDefSymbol(getSymbol(name).getOption())) {
					messageNoMakeForSymbol(name, optionList);
				}
				options = optionList;
				methodName = name;
			}
			
			VariableStar[option=optionList, astName=Name(name1)] -> {
				int line = findOriginTrackingLine(name1,optionList);    
				messageRuleErrorVariableStar(name1, line);
				return ruleName;
			}

			UnamedVariableStar[option=optionList] |
			Placeholder[option=optionList] -> { 
				messageRuleErrorLhsImpossiblePlaceHolder(optionList);
				return ruleName;
			}
			XMLAppl[option=optionList] -> {
				messageRuleErrorLhsImpossibleXML(optionList);
				return ruleName;
			}
		}
		
		if ( ruleNumber != 0 && !methodName.equals(ruleName)) {   
			messageRuleErrorConstructorEgality(methodName, ruleName, options);
			return ruleName;
		}
		return methodName;
	}

	private void messageRuleErrorUnknownSymbol(String symbolName) {
		int line = currentTomStructureOrgTrack.getLine();
		String s = "Symbol '" +symbolName+ "' has no been declared: see rule declared line "+line;
		messageError(line,s); 
	}
  
	private void messageNoMakeForSymbol(String name, OptionList optionList) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
                String s = "Symbol '" +name+ "' has no 'make' method associated in structure declared line "+declLine;
		messageError(line,s);
	}
 	 
	private void messageRuleErrorVariableStar(String nameVariableStar, int line) {
		int declLine = currentTomStructureOrgTrack.getLine();
		String s = "Single list variable '" +nameVariableStar+ "*' is not allowed on left hand side of structure %rule declared line "+declLine;
		messageError(line,s);
	}
	
	private void messageRuleErrorLhsImpossiblePlaceHolder(OptionList optionList) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		String s = "Alone placeholder is not allowed in left hand side of structure %rule declared line " +declLine;
		messageError(line,s);
	}
	
	private void messageRuleErrorLhsImpossibleXML(OptionList optionList) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		String s = "XML is not allowed in left hand side of structure %rule declared line " +declLine;
		messageError(line,s);		
	}
 	 
	private void messageRuleErrorConstructorEgality(String  name, String nameExpected, OptionList optionList) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(name, optionList);
                String s = "Left most symbol name '" + nameExpected + "' expected, but '" + name + "' found in left hand side of structure %rule declared line " +declLine;
		messageError(line,s);
	}

	  //Rhs shall have no underscore, be a var* nor _*, nor a RecordAppl
	private void verifyRhsRuleStructure(TomTerm ruleRhs, TomType lhsType) {
		matchBlock: {
			%match(TomTerm ruleRhs) {
				appl@Appl(options,Name(name),args) -> {
					permissiveVerify(appl);
					TomType rhsType = getSymbolCodomain(getSymbol(name));
					if(rhsType != null) {
						if(rhsType != lhsType) {
							messageRuleErrorBadRhsType(options, lhsType, rhsType);
						}
					}
					break matchBlock;
				}
				UnamedVariableStar[option=option] |
				Placeholder[option=option] -> {
					messageRuleErrorRhsImpossiblePlaceholder(option);
					break matchBlock;
				}
				VariableStar[option=option, astName=Name(name)] -> {
					messageRuleErrorRhsImpossibleVarStar(option, name);
					break matchBlock;
				}
				RecordAppl(option, Name(tomName), _) -> {
					messageRuleErrorRhsImpossibleRecord(option, tomName);
					break matchBlock;
				}
				XMLAppl[option=optionList] -> {
					messageRuleErrorRhsImpossibleXML(optionList);
					break matchBlock;
				}
				_ -> {
					throw new TomRuntimeException(new Throwable("Strange rule rhs:\n" + ruleRhs));
				}
			}
		}
	}
	
	private void messageRuleErrorBadRhsType(OptionList optionList, TomType lhsType, TomType rhsType) {
		if(!warningAll || noWarning) return;
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		String rhsTypeName, lhsTypeName;
		if(rhsType.isEmptyType()) {rhsTypeName = "Not Type Found";} else {rhsTypeName = rhsType.getString();}
		if(lhsType.isEmptyType()) {lhsTypeName = "Not Type Found";} else {lhsTypeName = lhsType.getString();}
		String s = "Bad right hand side type: `"+rhsTypeName+"` instead of `"+lhsTypeName+"` in structure %rule declared line " +declLine;
		//messageError(line,s);	
		System.out.println(s);
	}
	
	private void messageRuleErrorRhsImpossibleXML(OptionList optionList) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		String s = "XML is not allowed in right hand side of structure %rule declared line " +declLine;
		messageError(line,s);		
	}
	private void messageRuleErrorRhsImpossiblePlaceholder(OptionList optionList) {
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		int declLine = currentTomStructureOrgTrack.getLine();
		String s = "Placeholder is not allowed on right part of structure %rule declared line "+declLine;
		messageError(line,s);
	}
  
	private void messageRuleErrorRhsImpossibleRecord(OptionList optionList, String name) {
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		int declLine = currentTomStructureOrgTrack.getLine();
		String s = "Record '"+name+"[...]' is not allowed on right part of structure %rule declared line "+declLine;
		messageError(line,s);
	}
	
	private void messageRuleErrorRhsImpossibleVarStar(OptionList optionList, String name) {
		int line = findOriginTrackingLine(optionList);
                ensureOriginTrackingLine(line);
		int declLine = currentTomStructureOrgTrack.getLine();
		String s = "Single list variable '"+name+"*' is not allowed in right hand side of structure %rule declared line " +declLine;
		messageError(line,s);
	}
 	     
 	   /** RECORDS CONCERNS */
  private void verifyRecordStructure(OptionList option, String tomName, TomList args)  {
    TomSymbol symbol = getSymbol(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotList.isEmpty() || (args.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)))) {
        messageBracketError(tomName, option);
      }
      verifyRecordSlots(args,slotList, tomName);
    } else {
      messageSymbolError(tomName, option);
    }
  }
  
    // We test the existence of slotName contained in pairSlotAppl
    // and then the associated term
  private void verifyRecordSlots(TomList listPair, SlotList slotList, String methodName) {
    ArrayList slotIndex = new ArrayList();
    while( !listPair.isEmpty() ) {
      TomTerm pair = listPair.getHead();
      TomName name = pair.getSlotName();
      int index = getSlotIndex(slotList,name);
      if(index < 0) {
        messageSlotNameError(pair,slotList, methodName); 
      } else {
        Integer integerIndex = new Integer(index);
        if(slotIndex.contains(integerIndex)) {
          messageSlotRepeatedError(pair, methodName);
        }
        slotIndex.add(integerIndex);
      }
      listPair = listPair.getTail();
    }
  }

  private void messageSlotNameError(TomTerm pairSlotName, SlotList slotList, String methodName) {
    ArrayList listOfPossibleSlot = new ArrayList();
    while ( !slotList.isEmpty() ) {
      TomName name = slotList.getHead().getSlotName();
      if(!name.isEmptyName()) {
        listOfPossibleSlot.add(name.getString());
      }
      slotList = slotList.getTail();
    }
    int line = nullInteger;
    String s = "";
    %match( TomTerm pairSlotName ) {
      PairSlotAppl[slotName=Name(name),appl=Appl[option=list]] -> {
        line = findOriginTrackingLine(list);
        ensureOriginTrackingLine(line);
        s += "Slot Name '" + name + "' is not correct: See method '"+methodName+ "' -  Line : "+line;
      }
    }
    s += "\nPossible slot names are : "+listOfPossibleSlot;
    messageError(line, s);
  }
  
  private void messageSlotRepeatedError(TomTerm pairSlotName, String methodName) {
    %match( TomTerm pairSlotName ) {
      PairSlotAppl(Name(name), Appl[option=list]) -> {
        int line = findOriginTrackingLine(list);
        ensureOriginTrackingLine(line);
        String s = "Same slot names can not be used several times: See method '"+methodName+ "' -  Line : "+line;
        s += "Repeated slot Name : '"+name+"'";
        messageError(line, s);
      }
    }
  }
  
  private void messageBracketError(String name, OptionList optionList) {
    int line = findOriginTrackingLine(name,optionList);
    String s = "[] are not allowed on lists or arrays nor constants, see: "+name;
    messageError(line,s);
  }

	 /** XMLAPPL CONCERNS */
	private void verifyXMLApplStructure(OptionList optionList, String name, TomList attrList, TomList childList) {
		// TODO Testing XML Appl
		System.out.println("Need verifyXMLApplStructure");
	}

    /** APPL CONCERNS */
  private void verifyApplStructure(OptionList optionList, String name, TomList argsList) {
    if(name.equals("")) {
        // unamed list
      System.out.println("verifyApplStructure: Unamed List");
      return;
    }
    
    TomSymbol symbol = getSymbol(name);
    if(symbol==null) {
      if(hasConstructor(optionList) || !argsList.isEmpty()) {
          //we dont know the symbol but it is called as a constructor or has children
        messageSymbolError(name, optionList);
      }
    } else {
      TomTypeList domain = getSymbolDomain(symbol);
      int nbExpectedArgs = domain.getLength();
      boolean listOrArray =  (isListOperator(symbol) ||  isArrayOperator(symbol));
			
      if(argsList.isEmpty()) {
        if(nbExpectedArgs>0 && !listOrArray) {
            //we know the symbol it is not a list operator 
          int line = findOriginTrackingLine(name,optionList);
          messageNumberArgumentsError(nbExpectedArgs, 0, name, line);
        } else if (!hasConstructor(optionList)) {
            //we know the symbol but it is not called has a constructor and argsList is empty
            // it is not a string or int or double
          String codomain = getTomType(getSymbolCodomain(symbol));
          if( !codomain.equals("String") && !codomain.equals("double") &&
              !codomain.equals("int")) {
            int line = findOriginTrackingLine(name,optionList);
            messageVariableWithConstructorNameError(name, line);
          }
        }
      } else {
          // in case of oparray or oplist, the number of arguments is not tested
          // and no _* nor var * are allowed in the other case
        
        int nbFoundArgs = 0;
        ArrayList foundType = new ArrayList();
        ArrayList foundOptionList = new ArrayList();
          //Map optionMap  = new HashMap();
          // we shall now test the type of each args
        %match(TomList argsList) {
          concTomTerm(_*, term, _*) -> {
            nbFoundArgs++;
            TermDescription termDesc = analyseTomTerm(term);
              // Var* and _* are not allowed in non list symbol
            if(!listOrArray &&(termDesc.termClass == UNAMED_VARIABLE_STAR || termDesc.termClass == VARIABLE_STAR)) {
              int line = findOriginTrackingLine(optionList);
              ensureOriginTrackingLine(line);
              messageVariableStarInNonListOperator(nbFoundArgs, termDesc.name ,name, line);
            }
            foundType.add(termDesc.type);
            foundOptionList.add(term.getOption());
          }
        }
        if (!listOrArray) {
            // We test the number of args vs its definition
          if (nbExpectedArgs != nbFoundArgs) {
            int line = findOriginTrackingLine(name,optionList);
            messageNumberArgumentsError(nbExpectedArgs, nbFoundArgs, name, line);
            return;
          }        
            // and the types
          nbFoundArgs = 0;
          %match(TomTypeList domain) {
            concTomType(_*, type, _*) -> {
              String s = getTomType(type);
              if ( (foundType.get(nbFoundArgs) != s) && (foundType.get(nbFoundArgs) != null)) {
                int line = findOriginTrackingLine((OptionList)foundOptionList.get(nbFoundArgs));
                ensureOriginTrackingLine(line);
                messageApplErrorTypeArgument(name, nbFoundArgs+1, s, (String) foundType.get(nbFoundArgs), line); 
              }
              nbFoundArgs++;
            }
          }
        } else {
            // We worry only about returned types that should always be the same
          String s = getTomType(domain.getHead());  
          for( int slot = 0; slot <foundType.size() ; slot++ ) {
            if ( (foundType.get(slot) != s) && (foundType.get(slot) != null)) {
              int line = findOriginTrackingLine((OptionList) foundOptionList.get(slot));
              ensureOriginTrackingLine(line);
              messageApplErrorTypeArgument(name, slot+1, s, (String) foundType.get(slot), line);
            }
          }
        }
      }
    }
  }

  private void  messageApplErrorTypeArgument(String applName, int slotNumber, String expectedType, String givenType, int line) {
    String s = "Bad type of argument: Argument "+slotNumber+" of method '" + applName + "' has type '"+expectedType+"' required but type '"+givenType+"' found";
    messageError(line,s);
  }
	
	private void messageVariableStarInNonListOperator(int slot, String typeStar, String applName, int line) {
		String s = "Impossible variable star "+typeStar +" as argument "+slot+" of non list operator method '" + applName + "'";
		messageError(line,s);
	}
   
	private void permissiveVerify(TomTerm term) {
		 Collect1 permissiveCollectAndVerify = new Collect1() 
			 {  
				 public boolean apply(ATerm term) {
					 if(term instanceof TomTerm) {
						 %match(TomTerm term) {
							 BackQuoteAppl(options,Name(name),args) -> {
								 permissiveVerifyApplStructure(options, name, args);
								 return true;
							 }
							 Appl(options,Name(name),args) -> {
								 permissiveVerifyApplStructure(options, name, args);
								 return true;
							 }
							 RecordAppl[option=options, astName=Name(name)] ->{
								 messageRuleErrorRhsImpossibleRecord(options, name);
								 return true;
							 }
							 Placeholder(orgTrack) -> {
								 messageRuleErrorRhsImpossiblePlaceholder(orgTrack);
							 }
							 _ -> { return true; }
						 }
					 } else {
						 return true;
					 }
				 }// end apply
			 }; // end new
		 traversal().genericCollect(term, permissiveCollectAndVerify);
	 }
	  
  private void permissiveVerifyApplStructure(OptionList optionList, String name, TomList argsList) {
    TomSymbol symbol = getSymbol(name);
    if(symbol==null && !argsList.isEmpty()) {
      messageWarningSymbol(name, optionList);
    } else {
      if(!argsList.isEmpty()) {
          //in case of oparray or oplist, the number of arguments is not tested
        if ( !isListOperator(symbol) &&  !isArrayOperator(symbol) ) {
            // We test the number of args vs its definition
          int nbExpectedArgs = getSymbolDomain(symbol).getLength();
          int nbFoundArgs = length(argsList);
          if (nbExpectedArgs != nbFoundArgs) {
            int line = findOriginTrackingLine(name,optionList);
            messageNumberArgumentsError(nbExpectedArgs, nbFoundArgs, name, line);
          }
        }
          // We ensure that the symbol has a Make declaration if not list nor array
        if (!isListOperator(symbol) &&  !isArrayOperator(symbol)) {
          if ( !findMakeDeclOrDefSymbol(symbol.getOption()) ) {
            messageNoMakeForSymbol(name, optionList);
          }
        }
      }
    }
  }

  private boolean findMakeDeclOrDefSymbol(OptionList list) {
    while(!list.isEmpty()) {
      Option term = list.getHead();
      %match(Option term ) {
        DeclarationToOption(MakeDecl[args=makeArgsList, orgTrack=orgTrack]) -> {
          return true;
        }
        DefinedSymbol() -> {return true;}
        _ -> {list = list.getTail();}
      }
    }
    return false;
  }

  private void messageSymbolError(String name, OptionList optionList) {
    int line = findOriginTrackingLine(name, optionList);
    String s = "Symbol method : '" + name + "' not found";
    messageError(line,s);
  }
  
  private void messageWarningSymbol(String name, OptionList optionList) {
    if(!warningAll || noWarning) return;
    String nameDecl = currentTomStructureOrgTrack.getAstName().getString();
    int declLine = currentTomStructureOrgTrack.getLine();
    int line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Warning *** Possible error in structure "+nameDecl+" declared line "+declLine);
    System.out.println(" *** Unknown method '"+name+"' : Ensure the type coherence by yourself line : " + line);
  }
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, String name, int line) {
    String s = "Bad number of arguments for method '" + name + "':" + nbArg + " arguments are required but " + nbArg2 + " are given";
    messageError(line,s);
  }
  
	private void messageVariableWithConstructorNameError(String name, int line) {
		String s = "Ambiguous variable name. Is '" + name  + "' a variable or a constructor? Use " + name + "() if it is a constructor.";
		messageError(line,s);
	}

		/** Main type checking entry point: We check all interesting Tom Structure */
	protected void checkTypeInference(TomTerm expandedTerm) {
		Collect1 collectAndVerify = new Collect1() {  
			public boolean apply(ATerm term) {
				if(term instanceof TomTerm) {
					%match(TomTerm term) {
						Match(_, PatternList(list), oplist) -> {  
							currentTomStructureOrgTrack = findOriginTracking(oplist);
							verifyMatchVariable(list);
							return false;
						}
						RuleSet(list, orgTrack) -> {
							currentTomStructureOrgTrack = orgTrack;
							verifyRuleVariable(list);
							return false;
						}
						_ -> { return true; }
					}
				} else {
					return true;
				}
			}// end apply
		}; // end new
		traversal().genericCollect(expandedTerm, collectAndVerify);
	}
  
	private void verifyMatchVariable(TomList patternList) {
		while(!patternList.isEmpty()) {
			TomTerm pa = patternList.getHead();
			TomTerm patterns = pa.getTermList();
				// collect variables
			ArrayList variableList = new ArrayList();
			collectVariable(variableList, patterns);      
			verifyVariableType(variableList);
			patternList = patternList.getTail();
		}
	}
  
	private void verifyRuleVariable(TomRuleList list) {
		while(!list.isEmpty()) {
			TomRule rewriteRule = list.getHead();
			TomTerm lhs = rewriteRule.getLhs();
			TomTerm rhs = rewriteRule.getRhs();
			TomList condList = rewriteRule.getCondList();
			Option orgTrack = findOriginTracking(rewriteRule.getOption());
 	     
			ArrayList variableLhs = new ArrayList();
			collectVariable(variableLhs, lhs);
			HashSet lhsSet = verifyVariableType(variableLhs);
 	     
			ArrayList variableRhs = new ArrayList();
			collectVariable(variableRhs, rhs);
			HashSet rhsSet = verifyVariableType(variableRhs);
 	     
			ArrayList variableCond = new ArrayList();
			collectVariable(variableCond, `Tom(condList));
			HashSet condSet = verifyVariableType(variableCond);
 	     
			lhsSet.addAll(condSet);
			if(!condSet.isEmpty()) {
				System.out.println("Warning: improve verifyRuleVariable for matchingCondition");
			}
 	     
			if( !lhsSet.containsAll(rhsSet) ) {
				Iterator it = lhsSet.iterator();
				while(it.hasNext()) {
					rhsSet.remove(it.next());
				}
				messageRuleErrorUnknownVariable(rhsSet, orgTrack);
			}
				// case of rhs is a single variable
			%match (TomTerm rhs) {
				Term(Variable[astName=Name(name)]) -> {
					String methodName = "";
					%match(TomTerm lhs) {
						Term(Appl[astName=Name(name1)]) -> {
							methodName = name1;
						}
						Term(RecordAppl[astName=Name(name1)]) -> {
							methodName = name1;
						}
					}
					TomType typeRhs = getSymbolCodomain(getSymbol(methodName));
					Iterator it = variableLhs.iterator();
					while(it.hasNext()) {
						TomTerm term = (TomTerm)it.next();
						if(term.getAstName().getString() == name) {
							TomType typeLhs = term.getAstType();
							if(typeLhs != typeRhs) {
								messageRuleErrorBadRhsVariable(name, typeRhs.getTomType().getString(), typeLhs.getTomType().getString(), orgTrack);
							}
						}
					}
				}
			}
			list = list.getTail();
		}
	}
  
	private HashSet verifyVariableType(ArrayList list) {
			// compute multiplicities
		HashSet set = new HashSet();
		HashMap multiplicityMap = new HashMap();
		Iterator it = list.iterator();
		while(it.hasNext()) {
				TomTerm variable = (TomTerm)it.next();
			TomName name = variable.getAstName();
   	   
			if(set.contains(name)) {
				TomTerm var = (TomTerm)multiplicityMap.get(name);
				TomType type = var.getAstType();
				TomType type2 = variable.getAstType();
				if(!(type==type2)) {
					messageErrorIncoherentVariable(name.getString(), type.getTomType().getString(), type2.getTomType().getString(), variable.getOption()); 
				}
			} else {
				multiplicityMap.put(name, variable);
				set.add(name);
			}
		}
	return set;
	}
  
	private void messageErrorIncoherentVariable(String name, String type, String type2, OptionList options) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = findOriginTrackingLine(options);
                ensureOriginTrackingLine(line);
		String s = "Bad variable type for '"+name+"': it has both type '"+type+"' and '"+type2+"' in structure declared line "+declLine;
		messageError(line,s);
	}
 	 
	private void messageRuleErrorUnknownVariable(Collection variableCollectionRhs, Option rewriteRuleOrgTrack) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = rewriteRuleOrgTrack.getLine();
		String s = "Unknown variable(s) " +variableCollectionRhs+ " used in right part of %rule declared line "+declLine;
		messageError(line,s);
	}
 	 
	private void messageRuleErrorBadRhsVariable(String name, String type, String type2, Option rewriteRuleOrgTrack) {
		int declLine = currentTomStructureOrgTrack.getLine();
		int line = rewriteRuleOrgTrack.getLine();    
		String s = "Alone variable '"+name+"' has type '"+type+"' instead of type '"+type2+"' in right part of %rule declared line "+declLine;
		messageError(line,s);
	}		

    /** GLOBALS */
  private String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }

  private void messageError(int line, String msg) {
    String s = "\n"+msg+"\n-- Error occured at line: " + line +" in file: "+currentTomStructureOrgTrack.getFileName().getString()+"\n";
    errorMessage.add(s);
    addError(input, msg, currentTomStructureOrgTrack.getFileName().getString(), line, 0);
  }
    
  private int findOriginTrackingLine(String name, OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,OriginTracking[astName=Name[string=str],line=line],_*) -> { if(str.equals(name)) {return line;} }
    }
    addError(input, "findOriginTrackingLine:  not found", input.getInputFileName(), 0, 0);
    System.out.println("findOriginTrackingLine: '" + name + "' not found in " + optionList);
    return -1;
  }
  
  private int findOriginTrackingLine(OptionList optionList) {
    %match(OptionList optionList) {
      concOption(_*,OriginTracking[line=line],_*) -> { return line; }
    }
    return -1;
  }

  private void ensureOriginTrackingLine(int line) {
    if(line < 0) {
      addError(input, "findOriginTrackingLine:  not found", input.getInputFileName(), 0, 0);
      System.out.println("findOriginTrackingLine: not found ");
        //throw new TomRuntimeException(new Throwable("foo"));

    }
  }
  
  public TermDescription analyseTomTerm(TomTerm term) {
    String termName, type;
    int termClass, decLine;
    matchblock:{
      %match(TomTerm term) {
        Appl[option=options, astName=Name(name)] -> {
          termClass = APPL;
          decLine = findOriginTrackingLine(options);
          type = extractType(getSymbol(name));     
          termName = name;
          break matchblock;
        }
        RecordAppl[option=options,astName=Name(name)] ->{
          termClass = RECORD_APPL;
          decLine = findOriginTrackingLine(options);
          type = extractType(getSymbol(name));     
          termName = name;
          break matchblock;
        }
        XMLAppl[option=options] -> {
          termClass = XML_APPL;
          decLine = findOriginTrackingLine(options);
          type = extractType(getSymbol(Constants.ELEMENT_NODE));
          termName = Constants.ELEMENT_NODE;
          break matchblock;
        }
        Placeholder[option=options] -> {
          termClass = PLACE_HOLDER;
          decLine = findOriginTrackingLine(options);
          type = null;     
          termName = "*";
          break matchblock;
        }
        VariableStar[option=options, astName=Name(name)] -> { 
          termClass = VARIABLE_STAR;
          decLine = findOriginTrackingLine(options);
          type = null;     
          termName = name+"*";
          break matchblock;
        }
        UnamedVariableStar[option=options] -> {
          termClass = UNAMED_VARIABLE_STAR;
          decLine = findOriginTrackingLine(options);
          type = null;     
          termName = "_*";
          break matchblock;
        }
        _ -> {
          System.out.println("Strange term in pattern "+term);
          throw new TomRuntimeException(new Throwable("Strange Term "+term));
        }
      }
    }
    return new TermDescription(termClass, termName, decLine, type);	
  }
  
} //class TomChecker
