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
    Julien Guyon

*/

package jtom.checker;

import aterm.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedList;

import jtom.TomBase;
import jtom.TomEnvironment;
import jtom.adt.*;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.runtime.Collect1;
import jtom.xml.Constants;
import jtom.exception.*;

abstract public class TomChecker extends TomBase implements TomTask {
	
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
	
	private final static int APPL = 0;
	private final static int RECORD_APPL = 1;
	private final static int XML_APPL = 2;
	private final static int VARIABLE_STAR = 3;
	private final static int UNAMED_VARIABLE_STAR = 4;
	private final static int PLACE_HOLDER = 5;
	
	private final static String OPERATOR = "Operator";
	private final static String CONSTRUCTOR = "%op";
	private final static String OP_ARRAY = "%oparray";
	private final static String OP_LIST = "%oplist";
	private final static String TYPE = "Type";	
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
	
	protected TomTaskInput input;
	protected TomTask nextTask;
	protected boolean strictType = false, warningAll = false, noWarning = false;
	//private ArrayList alreadyStudiedTypesAndSymbols =  new ArrayList();
	private ArrayList alreadyStudiedTypes =  new ArrayList();
	private ArrayList alreadyStudiedSymbols =  new ArrayList();
	private Option currentTomStructureOrgTrack;
	
	private ArrayList errorMessage = new ArrayList();
	public int getNumberFoundError() {
		return errorMessage.size();
	}
  
	public String getMessage(int n) {
		return (String)errorMessage.get(n);
	}
	
  public TomChecker(TomEnvironment tomEnvironment) {
    super(tomEnvironment);
  }

	/*
	 * Common TomTask Interface 
	 */
	public void addTask(TomTask task) { this.nextTask = task; }
	public TomTask getTask() { return nextTask; }

	/**
	 *  Syntax checking entry point
	 */
	protected void checkSyntax(TomTerm parsedTerm) {
		Collect1 collectAndVerify = new Collect1() {  
			public boolean apply(ATerm subject) {
				if(subject instanceof TomTerm) {
					%match(TomTerm subject) {
						DeclarationToTomTerm(declaration) -> { verifyDeclaration(declaration); return false; }
						
						Match(SubjectList(matchArgsList), PatternList(patternActionList), list) -> {  
							verifyMatch(matchArgsList, patternActionList, list); return true;
						}/*
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
						}*/
						_ -> { return true; }
					}
				} else {
					return true;
				}
			}
		}; // end new Collect1()
			
		// use a traversal to get all interesting subtree
		traversal().genericCollect(parsedTerm, collectAndVerify);   
	} //checkSyntax

	protected void checkTypeInference(TomTerm expandedTerm) {
	
	}
	
	/**
	 *  SYMBOL AND TYPE CONCERNS 
	 */
	private void verifyDeclaration(Declaration declaration) {	
	%match (Declaration declaration) {
			// Types
		TypeTermDecl(Name(tomName), tomList, orgTrack) -> {
			verifyTypeDecl(TYPE_TERM, tomName, tomList, orgTrack);
		}
		TypeListDecl(Name(tomName), tomList, orgTrack) -> {
			verifyTypeDecl(TYPE_LIST, tomName, tomList, orgTrack);
		}
		TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
			verifyTypeDecl(TYPE_ARRAY, tomName, tomList, orgTrack);
		}
			// Symbols
		SymbolDecl(Name(tomName))         -> { verifySymbol(CONSTRUCTOR, getSymbol(tomName)); }
		ArraySymbolDecl(Name(tomName)) -> { verifySymbol(OP_ARRAY, getSymbol(tomName)); }
		ListSymbolDecl(Name(tomName))    -> {	verifySymbol(OP_LIST, getSymbol(tomName)); }
		
	}
}

	/** 
	 * TYPE DECLARATION CONCERNS 
	 */
	private void verifyTypeDecl(String declType, String tomName, TomList listOfDeclaration, Option typeOrgTrack) {
		currentTomStructureOrgTrack = typeOrgTrack;
		  // ensure first definition
		verifyMultipleDefinition(tomName, declType, TYPE);
			// verify Macro functions
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
		}
 	  %match(TomList listOfDeclaration) {
 	   		(_*, DeclarationToTomTerm(decl), _*) -> { // for each Declaration
					%match (Declaration decl) {
							// Common Macro functions
						GetFunctionSymbolDecl[orgTrack=orgTrack] -> {
							checkField(GET_FUN_SYM,verifyList,orgTrack, declType);
						}
						CompareFunctionSymbolDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
							checkFieldAndLinearArgs(CMP_FUN_SYM,verifyList,orgTrack,name1,name2, declType);
						}
					 TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
							checkFieldAndLinearArgs(EQUALS,verifyList,orgTrack,name1,name2, declType);
						}
							// Term specific Macro functions
						GetSubtermDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
							checkFieldAndLinearArgs(GET_SUBTERM,verifyList,orgTrack,name1,name2, declType);
						}
							// List specific Macro functions
						GetHeadDecl[orgTrack=orgTrack] -> {
							checkField(GET_HEAD,verifyList,orgTrack, declType);
						}
						GetTailDecl[orgTrack=orgTrack] -> {
							checkField(GET_TAIL,verifyList,orgTrack, declType);
						}
						IsEmptyDecl[orgTrack=orgTrack] -> {
							checkField(IS_EMPTY,verifyList,orgTrack, declType);
						}
							// Array specific Macro functions
						GetElementDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> { 
							checkFieldAndLinearArgs(GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
						}
						GetSizeDecl[orgTrack=orgTrack] -> {
							checkField(GET_SIZE,verifyList,orgTrack, declType);
						}
 	   			}
 	   		}
 	  }
 	  // remove non mandatory functions
		if(verifyList.contains(EQUALS)) {
			verifyList.remove(verifyList.indexOf(EQUALS));
		}    
		if(!verifyList.isEmpty()) {
			messageMissingMacroFunctions(declType, verifyList);
		}
	}
 	 
	/** 
	 * SYMBOL DECLARATION CONCERNS
	 */
	private void verifySymbol(String symbolType, TomSymbol tomSymbol){
		int domainLength;
		String symbStrName = tomSymbol.getAstName().getString();
		OptionList optionList = tomSymbol.getOption();
			// We save first the origin tracking of the symbol declaration
		currentTomStructureOrgTrack = findOriginTracking(optionList);
		
		// ensure first definition then Codomain, Domain, Macros and Slots (Simple operator)
		verifyMultipleDefinition(symbStrName, symbolType, OPERATOR);
		verifySymbolCodomain(getSymbolCodomain(tomSymbol).getString(), symbStrName, symbolType);
		domainLength = verifySymbolDomain(getSymbolDomain(tomSymbol), symbStrName, symbolType);
		verifySymbolMacroFunctions(optionList, domainLength, symbolType);
		/*if(symbolType == CONSTRUCTOR) {
			verifySymbolSlotList(tomSymbol.getSlotList(), symbolType);
		}*/
	}

	private void verifySymbolCodomain(String codomain, String symbName, String symbolType) {
		if(!testTypeExistence(codomain)) {
			messageError(currentTomStructureOrgTrack.getLine(), 
														symbolType+" "+symbName, 
														TomCheckerMessage.SymbolCodomainError,
														new Object[]{symbName, codomain});
		}
	}
 	 
	private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
		int position = 1;
		if(symbolType == CONSTRUCTOR) {
			%match(TomTypeList args) {
					(_*,  TomTypeAlone(typeName),_*) -> { // for each symbol types
						if(!testTypeExistence(typeName)) {
							messageError(currentTomStructureOrgTrack.getLine(), 
																		symbolType+" "+symbName, 
																		TomCheckerMessage.SymbolDomainError,
																		new Object[]{new Integer(position), symbName, typeName});
						}
						position++;
					}
			}
			return (position-1);
		} else { // OPARRAY and OPLIST
			%match(TomTypeList args) {
				(TomTypeAlone(typeName)) -> {
					if(!testTypeExistence(typeName)) {
						messageError(currentTomStructureOrgTrack.getLine(), 
																	symbolType+" "+symbName, 
																	TomCheckerMessage.ListSymbolDomainError,
																	new Object[]{symbName, typeName});
					}
				}
			} //match
			return 1;
		}
	}
 	
 	private boolean testTypeExistence(String typeName) {
		return symbolTable().getType(typeName) != null;
 	}
 	
	private void verifySymbolMacroFunctions(OptionList list, int domainLength, String symbolType) {
		ArrayList verifyList = new ArrayList();
		boolean foundOpMake = false;
		if(symbolType == CONSTRUCTOR){ //Nothing
		} else if(symbolType == OP_ARRAY ) {
			verifyList.add(MAKE_EMPTY);
			verifyList.add(MAKE_APPEND);
		} else if(symbolType == OP_LIST) {
			verifyList.add(MAKE_EMPTY);
			verifyList.add(MAKE_INSERT); 
		}
 	
		%match(OptionList list) {
			(_*, DeclarationToOption(decl), _*) -> { // for each Declaration
				%match(Declaration decl ) {
						// for a array symbol 
					MakeEmptyArray[orgTrack=orgTrack] -> { 
						checkField(MAKE_EMPTY,verifyList,orgTrack, symbolType);
					}
					MakeAddArray[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
						checkFieldAndLinearArgs(MAKE_APPEND, verifyList, orgTrack, name1, name2, symbolType);
					}
						// for a List symbol
					MakeEmptyList[orgTrack=orgTrack] -> {
						checkField(MAKE_EMPTY,verifyList,orgTrack, symbolType);         
					}
					MakeAddList[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
						checkFieldAndLinearArgs(MAKE_INSERT, verifyList, orgTrack, name1, name2, symbolType);
					}
					// for a symbol
					MakeDecl[args=makeArgsList, orgTrack=orgTrack] -> {
						if (!foundOpMake) {
							foundOpMake = true;
							verifyMakeDeclArgs(makeArgsList, domainLength, orgTrack, symbolType);
						} else {
							messageError(orgTrack.getLine(), 
																		symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
																		TomCheckerMessage.MacroFunctionRepeated,
																		new Object[]{MAKE});
						}
					}
				}
			}
		}
		if(!verifyList.isEmpty()) {
			messageMissingMacroFunctions(symbolType, verifyList);
		}
	}
	
	private void verifyMakeDeclArgs(TomList argsList, int domainLength, Option orgTrack, String symbolType){
			// we test the necessity to use different names for each variable-parameter.
		int nbArgs = 0;
		ArrayList listVar = new ArrayList();
		%match(TomList argsList) {
			(_*, Variable[option=listOption, astName=Name(name)] ,_*) -> { // for each Macro variable
				if(listVar.contains(name)) {
					messageError(orgTrack.getLine(), 
								symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
								TomCheckerMessage.NonLinearMacroFunction,
								new Object[]{MAKE, name});
				} else {
					listVar.add(name);
					nbArgs++;
				}
			}
		}
		if(nbArgs != domainLength) {
			messageError(orgTrack.getLine(), 
						symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
						TomCheckerMessage.BadMakeDefinition,
						new Object[]{new Integer(nbArgs), new Integer(domainLength)});			
		}
	}
	
	private void verifySymbolSlotList(SlotList slotList, String symbolType) {
		// we test the existence of 2 same slot names
	ArrayList listSlot = new ArrayList();
	%match(SlotList slotList) {
		(_*, Slot[slotName=Name(name)], _*) -> { // for each Slot
			if(listSlot.contains(name)) {
				System.out.println("Tchotchou");
				//messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
			} else {
				listSlot.add(name);
			}
		}
	}
	}
	
	private void verifyMultipleDefinition(String name, String symbolType, String OperatorOrType) {
		ArrayList list;
		if (OperatorOrType.equals(OPERATOR)) {
			list = alreadyStudiedSymbols;
		} else {
			list = alreadyStudiedTypes;
		}
		if(list.contains(name)) {
			messageError(currentTomStructureOrgTrack.getLine(), 
														symbolType+" "+name, 
														TomCheckerMessage.MultipleSymbolDefinitionError,
														new Object[]{name});
		} else {
			list.add(name);
		}
	}
	
	private void checkField(String function, ArrayList foundFunctions, Option orgTrack, String symbolType) {
		if(foundFunctions.contains(function)) {
			foundFunctions.remove(foundFunctions.indexOf(function)); 
		} else {
			messageError(orgTrack.getLine(), 
											"structure "+symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
											TomCheckerMessage.MacroFunctionRepeated,
											new Object[]{function});
		}
	}
 	 
	private void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
		checkField(function,foundFunctions, orgTrack, symbolType);
		if(name1.equals(name2)) {
			messageError(orgTrack.getLine(), 
											symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
											TomCheckerMessage.NonLinearMacroFunction,
											new Object[]{function, name1});
		}
	}
 	 
	private void messageMissingMacroFunctions(String symbolType, ArrayList list) {
		String listOfMissingMacros = "";
		for(int i=0;i<list.size();i++) {
			listOfMissingMacros+= list.get(i)+",  ";
		}
		listOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
		messageError(currentTomStructureOrgTrack.getLine(), 
													symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(),
													TomCheckerMessage.MissingMacroFunctions,
													new Object[]{listOfMissingMacros});
	}
 	 
	/** 
	 * MATCH VERIFICATION CONCERNS 
	 */
		// Given a MatchConstruct's subject list and pattern-action list
	private void verifyMatch(TomList subjectList, TomList patternList, OptionList list) {
		currentTomStructureOrgTrack = findOriginTracking(list);
		ArrayList typeMatchArgs = new ArrayList();
		ArrayList nameMatchArgs = new ArrayList();
			// From the subjects list(match definition), we test each used type and keep them in memory
		%match(TomList subjectList) {
			concTomTerm(_*, TLVar(name, TomTypeAlone(type)), _*) -> { // for each Match args
				if (!testTypeExistence(type)) {
					messageError(currentTomStructureOrgTrack.getLine(), TomCheckerMessage.UnknownMatchArgumentTypeInSignature, new Object[]{name, type});
					typeMatchArgs.add(null);
				} else {
					typeMatchArgs.add(type);
				}
				if(nameMatchArgs.indexOf(name) == -1) {
					nameMatchArgs.add(name);
				} else {
					messageError(currentTomStructureOrgTrack.getLine(), TomCheckerMessage.RepeatedMatchArgumentName, new Object[]{name});
				}
						
			}	
		}	
			// Then control each pattern vs the match definition
		%match(TomList patternList) {
			concTomTerm(_*, PatternAction[termList=TermList(terms)], _*) -> { // for each PatternAction
				verifyMatchPattern(terms, typeMatchArgs);
			}
		}
	}

		// For each Pattern we count and collect type information
		// but also we test that terms are well formed
		// No top variable/underscore star are allowed
	private void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs ) {
		ArrayList foundTypeMatch = new ArrayList();
		int line = -1;
		int nbFoundArgs = 0;
		int nbExpectedArgs = typeMatchArgs.size();
    
		%match(TomList termList) {
			concTomTerm(_*, term, _*) -> { // for each Term
				nbFoundArgs++;
				if(nbFoundArgs > nbExpectedArgs) {
					// breaking analyses
					messageError(line, TomCheckerMessage.BadMatchNumberArgument, new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)});
					return;
				}
				
				TermDescription termDesc = analyseTerm(term, (String)typeMatchArgs.get(nbFoundArgs-1), true);
				line = termDesc.decLine;

				// Var* and _* are not allowed as top leftmost symbol
				if(termDesc.termClass == UNAMED_VARIABLE_STAR || termDesc.termClass == VARIABLE_STAR) {
					messageError(line, TomCheckerMessage.MatchVariableStar, new Object[]{termDesc.name});
				} else {
					//TODO: Analyse the terms again type (!= null)
				}
				// ensure type coherence if strictType
				if(strictType) {
					if(termDesc.type != null && !termDesc.type.equals(typeMatchArgs.get(nbFoundArgs-1)) ) {
						messageError(line, TomCheckerMessage.WrongMatchArgumentTypeInPattern, new Object[]{new Integer(nbFoundArgs), (String)typeMatchArgs.get(nbFoundArgs-1), termDesc.type });
					}
				}
			}
		}
		if(nbFoundArgs < nbExpectedArgs) {
			messageError(line, TomCheckerMessage.BadMatchNumberArgument, new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)});
		}
	}

	public TermDescription analyseTerm(TomTerm term, String expectedType, boolean topLevel) {
		String termName = "" , type = "";
		int termClass, decLine;
		matchblock:{
			%match(TomTerm term) {
				Appl[option=options, nameList=nameList] -> {
					currentTomStructureOrgTrack = findOriginTracking(options);
					String name = ensureValidDisjunction(nameList);
					termClass = APPL;
					decLine = findOriginTrackingLine(options);
					type = extractType(getSymbol(name));     
					termName = name;
					break matchblock;
				}
				Appl[option=options, nameList=nameList@(Name(""))] -> {
					currentTomStructureOrgTrack = findOriginTracking(options);
					TomSymbol symbol = ensureValideUnamedList(expectedType);
					termClass = APPL;
					decLine = findOriginTrackingLine(options);										
					if(symbol != null) {
						type = extractType(symbol);
						termName = symbol.getAstName().getString();
					}
					break matchblock;
				}
				RecordAppl(options,nameList, pairSlotAppls) ->{
					currentTomStructureOrgTrack = findOriginTracking(options);
					//ensureValidRecordDisjunction(nameList, pairSlotAppls);
					String name2 ="";
					boolean first = true;
					%match(NameList nameList) { // 
						(_*, Name(name1), _*) -> {
							//verifyRecordStructure(options, name1, pairSlotAppls);
							if(first) {
								name2 =  name1;
								first =false;
							}
						}
					}
					termClass = RECORD_APPL;
					decLine = findOriginTrackingLine(options);
					type = extractType(getSymbol(name2));     
					termName = name2;
					break matchblock;
				}
				XMLAppl[option=options, nameList=nameList@(_*, Name(name), _*)] -> {
					// TODO: can we do it
					// ensureValidDisjunction(nameList); ??????????
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
	
	/*private void ensureValidRecordDisjunction(NameList recorApplNameList, TomList pairSlotApplList) {
		TomSymbol symbol;
		ArrayList usedSlotList;
		%match(TomList pairSlotApplList) {
			(_*, PairSlotAppl[slotName=Name(slot)], _*) -> { // for each PairSlotAppl
				usedSlotList.add(slot);
			}
		}
		
		SlotList slotList;
		%match(NameList nameList) {
					(_*, Name(dijName), _*) -> { // for each Record SymbolName
						symbol = getSymbol(dijName);
						if(currentType == null) { 
								System.out.println("Bad Record Disjunction Name");
								return;
						}
						slotList = symbol.getSlotList();
						%match(SlotList slotList) {
							()
						}
					}
	}*/
	
	private String ensureValidDisjunction(NameList nameList) {
		if(nameList.isSingle()) { // Valid
			return nameList.getHead().getString();
		}
		
	  boolean first = true; // the first symbol give the expected 
	  TomType type = null, currentType = null;
		String res = "";
		%match(NameList nameList) {
			(_*, Name(dijName), _*) -> { // for each SymbolName
				currentType = getSymbolCodomain(getSymbol(dijName));
				if(currentType == null) {
					System.out.println("Bad Disjunction Name");
					return res;
				}
				if (first) { 
					type = currentType; 
					res = dijName;
				} 
				else {
					first = false;
					if(type ==currentType) {
						System.out.println("Bad Disjunction Codomain");
						return res;
					}
				}
			}
		}
		return res;
	}
	
	private TomSymbol ensureValideUnamedList(String expectedType) {
		SymbolList symbolList = symbolTable().getSymbol(`ASTTomType(expectedType));
		SymbolList filteredList = `emptySymbolList();
		%match(SymbolList symbolList) {
		  (_*, symbol , _*) -> {  // for each symbol
					if(isArrayOperator(symbol) || isListOperator(symbol)) {
						filteredList = `manySymbolList(symbol,filteredList);
					}
		  }
		}
		
		if(filteredList.isEmpty()) {
			  // TODO
		  System.out.println("getSymbol: symbol not found: " + expectedType);
		  return null;
		} else if(!filteredList.getTail().isEmpty()) {
			  // TODO
			System.out.println("getSymbol: ambiguities: " + filteredList);
			return null;
		} else { 
			return filteredList.getHead();
		}
	}
	
	
	 /** RECORDS CONCERNS */
/*private void verifyRecordStructure(OptionList option, String tomName, TomList args)  {
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
		messageError(tomName, option);
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
}*/

	
	/**
	 * Message Functions
	 */

	private void messageError(int errorLine, String structInfo, String msg, Object[] msgArg) {
		String fileName = currentTomStructureOrgTrack.getFileName().getString();
		int declLine = currentTomStructureOrgTrack.getLine();
		msg = MessageFormat.format(msg, msgArg);
		String s = MessageFormat.format(TomCheckerMessage.MainErrorMessage, new Object[]{new Integer(errorLine), structInfo, new Integer(declLine), fileName, msg});
		errorMessage.add(s);
		addError(input, msg, currentTomStructureOrgTrack.getFileName().getString(), errorLine, 0);
	}
	
	private void messageError(int errorLine, String msg, Object[] msgArg) {
		String structName = currentTomStructureOrgTrack.getAstName().getString();
		messageError(errorLine, structName, msg, msgArg);
	}
	
	/**
	 * Global Functions 
	 */
	private String extractType(TomSymbol symbol) {
		TomType type = getSymbolCodomain(symbol);
		return getTomType(type);
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
}  //Class TomChecker
