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

import java.util.ArrayList;
import java.util.LinkedList;

import jtom.TomEnvironment;
import jtom.adt.*;
import jtom.tools.TomTask;
import jtom.runtime.Collect1;
import jtom.xml.Constants;
import jtom.exception.*;

abstract public class TomChecker extends TomTask {
	
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
	

	protected boolean strictType = false, warningAll = false, noWarning = false, verbose = false;
	private ArrayList alreadyStudiedTypes =  new ArrayList();
	private ArrayList alreadyStudiedSymbols =  new ArrayList();
	private Option currentTomStructureOrgTrack;
		
  public TomChecker(String name, TomEnvironment tomEnvironment) {
    super(name, tomEnvironment);
  }

	public void initProcess() {
		verbose = getInput().isVerbose();
		strictType = getInput().isStrictType();
		warningAll = getInput().isWarningAll();
		noWarning = getInput().isNoWarning();
	} 
	
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
							verifyMatch(matchArgsList, patternActionList, list); return false;
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
														new Object[]{symbName, codomain}, TomCheckerMessage.TOM_ERROR);
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
																		new Object[]{new Integer(position), symbName, typeName}, TomCheckerMessage.TOM_ERROR);
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
																	new Object[]{symbName, typeName}, TomCheckerMessage.TOM_ERROR);
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
																		new Object[]{MAKE}, TomCheckerMessage.TOM_ERROR);
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
								new Object[]{MAKE, name}, TomCheckerMessage.TOM_ERROR);
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
						new Object[]{new Integer(nbArgs), new Integer(domainLength)}, TomCheckerMessage.TOM_ERROR);			
		}
	}
	
	private void verifySymbolSlotList(SlotList slotList, String symbolType) {
		// we test the existence of 2 same slot names
	ArrayList listSlot = new ArrayList();
	%match(SlotList slotList) {
		(_*, Slot[slotName=Name(name)], _*) -> { // for each Slot
			if(listSlot.contains(name)) {
				//TODO
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
														new Object[]{name}, TomCheckerMessage.TOM_ERROR);
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
											new Object[]{function}, TomCheckerMessage.TOM_ERROR);
		}
	}
 	 
	private void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
		checkField(function,foundFunctions, orgTrack, symbolType);
		if(name1.equals(name2)) {
			messageError(orgTrack.getLine(), 
											symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
											TomCheckerMessage.NonLinearMacroFunction,
											new Object[]{function, name1}, TomCheckerMessage.TOM_ERROR);
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
													new Object[]{listOfMissingMacros}, TomCheckerMessage.TOM_ERROR);
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
			concTomTerm(_*, TLVar(name, tomType@TomTypeAlone(type)), _*) -> { // for each Match args
				if (!testTypeExistence(type)) {
					messageError(currentTomStructureOrgTrack.getLine(), TomCheckerMessage.UnknownMatchArgumentTypeInSignature, new Object[]{name, type}, TomCheckerMessage.TOM_ERROR);
					typeMatchArgs.add(null);
				} else {
					typeMatchArgs.add(tomType);
				}
				if(nameMatchArgs.indexOf(name) == -1) {
					nameMatchArgs.add(name);
				} else {
					messageError(currentTomStructureOrgTrack.getLine(), TomCheckerMessage.RepeatedMatchArgumentName, new Object[]{name}, 1);
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
					messageError(line, TomCheckerMessage.BadMatchNumberArgument, new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)}, TomCheckerMessage.TOM_ERROR);
					return;
				}
				
				TermDescription termDesc = analyseTerm(term, (TomType)typeMatchArgs.get(nbFoundArgs-1), false, true);
				line = termDesc.decLine;

				// Var* and _* are not allowed as top leftmost symbol
				if(termDesc.termClass == UNAMED_VARIABLE_STAR || termDesc.termClass == VARIABLE_STAR) {
					messageError(line, TomCheckerMessage.MatchVariableStar, new Object[]{termDesc.name}, TomCheckerMessage.TOM_ERROR);
				}
				// ensure type coherence if strictType
				if(strictType) {
					if(termDesc.type != null && !termDesc.type.equals(((TomType)typeMatchArgs.get(nbFoundArgs-1)).getString()) ) {
						//System.out.println(termDesc.type+"!="+((TomType)typeMatchArgs.get(nbFoundArgs-1)).getString());
						messageError(line, TomCheckerMessage.WrongMatchArgumentTypeInPattern, new Object[]{new Integer(nbFoundArgs), (String)typeMatchArgs.get(nbFoundArgs-1), termDesc.type }, TomCheckerMessage.TOM_ERROR);
					}
				}
			}
		}
		if(nbFoundArgs < nbExpectedArgs) {
			messageError(line, TomCheckerMessage.BadMatchNumberArgument, new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)}, TomCheckerMessage.TOM_ERROR);
		}
	}

  // Analyse a term given an expected type and reenter recursively on childs
	public TermDescription analyseTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel) {
		String termName = "emptyName" , type = null;
		int termClass, decLine;
		Option orgTrack;
		matchblock:{
			%match(TomTerm term) {
       	Appl(options, nameList@(Name("")), args) -> {
					decLine = findOriginTrackingLine(options);
					termClass = APPL;
										
          TomSymbol symbol = ensureValideUnamedList(expectedType, decLine);
          if(symbol == null) {
						break matchblock;
          }
            //there is only one symbol and its type is the expected one
          type = expectedType.getString(); // extractType(symbol);
          termName = symbol.getAstName().getString();
          
          while(!args.isEmpty()) {
          	// whatever the arity is, we continue recursively and there is only one element in the Domain
						analyseTerm(args.getHead(), symbol.getTypesToType().getDomain().getHead(), true, false);
						args = args.getTail();
          }
          break matchblock;
        }
        
				Appl(options, nameList, args) -> {
					decLine = findOriginTrackingLine(options);
					termClass = APPL;
					
					String name = ensureValidDisjunction(nameList, expectedType, decLine,hasConstructor(options), args.isEmpty());
					if(name == null) {
						break matchblock;
					}
						// Type is OK
					type = expectedType.getString(); //extractType(getSymbol(name));     
					termName = name;
					TomSymbol symbol = getSymbol(name);
					boolean listOp = (isListOperator(symbol) || isArrayOperator(symbol));
					if (listOp) {
						while(!args.isEmpty()) {
							// whatever the arity is, we continue recursively and there is only one element in the Domain
							analyseTerm(args.getHead(), symbol.getTypesToType().getDomain().getHead(), listOp, false);
							args = args.getTail();
						}
					} else {
						  // the arity is important also there are different types in Domain
						TomTypeList  types = symbol.getTypesToType().getDomain();
						int nbArgs = args.getLength();
						int nbExpectedArgs = types.getLength();
						if(nbArgs != nbExpectedArgs) {
							messageError(decLine, TomCheckerMessage.SymbolNumberArgument, 
																			new Object[]{name, new Integer(nbExpectedArgs), new Integer(nbArgs)}, TomCheckerMessage.TOM_ERROR);
							break matchblock;
						}
						while(!args.isEmpty()) {
							// TODO: for each arg: repeat analyse with associated expected type and control arity
							analyseTerm(args.getHead(), types.getHead(), listOp, false);
							args = args.getTail();
							types = types.getTail();
						}
					}
					break matchblock;
				}
				
				RecordAppl(options,nameList, pairSlotAppls) ->{
					decLine = findOriginTrackingLine(options);
					termClass = RECORD_APPL;

					String name2 ="";
					boolean first = true;
					%match(NameList nameList) { // We test as we have different RecordAppl: they all must be valid
						// and have the expected return type
						(_*, Name(name1), _*) -> {
							verifyRecordStructure(options, name1, pairSlotAppls, decLine);
							if(first) {
								name2 =  name1;
								first =false;
							}
						}
					}

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
					if(!listSymbol) {
						messageError(decLine, TomCheckerMessage.InvalidVariableStarArgument, 
																	new Object[]{termName}, TomCheckerMessage.TOM_ERROR);
					}
					break matchblock;
				}
				
				UnamedVariableStar[option=options] -> {
					termClass = UNAMED_VARIABLE_STAR;
					decLine = findOriginTrackingLine(options);
					type = null;     
					termName = "_*";
					if(!listSymbol) {
						messageError(decLine, TomCheckerMessage.InvalidVariableStarArgument, 
																	new Object[]{termName}, TomCheckerMessage.TOM_ERROR);
					}
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
	
	private TomSymbol ensureValideUnamedList(TomType expectedType, int decLine) {
		SymbolList symbolList = symbolTable().getSymbol(expectedType);
		SymbolList filteredList = `emptySymbolList();
		%match(SymbolList symbolList) {
			(_*, symbol , _*) -> {  // for each symbol
					if(isArrayOperator(symbol) || isListOperator(symbol)) {
						filteredList = `manySymbolList(symbol,filteredList);
					}
			}
		}
		
		if(filteredList.isEmpty()) {
			messageError(decLine,
														TomCheckerMessage.UnknowUnamedList,
														new Object[]{expectedType.getString()}
														, TomCheckerMessage.TOM_ERROR
														);
			return null;
		} else if(!filteredList.getTail().isEmpty()) {
			String symbolsString = "";
			while(!filteredList.isEmpty()) {
				symbolsString += " "+filteredList.getHead().getAstName().getString();
				filteredList= filteredList.getTail();
			}
			messageError(decLine,
														TomCheckerMessage.AmbigousUnamedList,
														new Object[]{expectedType.getString(), symbolsString}, TomCheckerMessage.TOM_ERROR
														);
			return null;
		} else { 
			return filteredList.getHead();
		}
	}
	
	private String ensureValidDisjunction(NameList nameList, TomType expectedType, int decLine,
																													boolean constructor, boolean emptyChilds) {
		String res = "";
		TomType currentType = null;
		
		if(nameList.isSingle()) { // Valid but has is a good type
			res = nameList.getHead().getString();
			TomSymbol symbol =  getSymbol(res);
			if (symbol == null ) {
				if(constructor || !emptyChilds) {
					messageError(decLine, TomCheckerMessage.UnknownSymbol, new Object[]{res}, TomCheckerMessage.TOM_ERROR);
				}
				return null;
			} else {			
				if(emptyChilds && !constructor) {
				 		//			we know the symbol but it is not called has a constructor and argsList is empty
				  	// it is not a string or int or double
					String codomain = getTomType(getSymbolCodomain(symbol));
					if( !codomain.equals("String") && !codomain.equals("double") && !codomain.equals("int")) {
						messageError(decLine,TomCheckerMessage.VariableWithConstructorName, new Object[]{res}, TomCheckerMessage.TOM_WARNING);
						// only a warning
					}
				}
	
				currentType = getSymbolCodomain(symbol);
				if (currentType != null && currentType != expectedType ) {
					//System.out.println(currentType+"!="+expectedType);
					messageError(decLine, 
																TomCheckerMessage.InvalidDisjunctionCodomain, 
																new Object[]{res, currentType.getString(), expectedType.getString()}, TomCheckerMessage.TOM_ERROR);
					return null;
				}
			}
			return res;
		}
		
		boolean first = true; // the first symbol give the expected 

		%match(NameList nameList) {
			(_*, Name(dijName), _*) -> { // for each SymbolName
				TomSymbol symbol =  getSymbol(dijName);
				if (symbol == null) {
					messageError(decLine, TomCheckerMessage.UnknownSymbol, new Object[]{dijName}, TomCheckerMessage.TOM_ERROR);
					return null;
				}
				currentType = getSymbolCodomain(symbol);
				if(currentType == null) {
					// in disjunction we can only have known symbols
					messageError(decLine, TomCheckerMessage.UnknownSymbol, new Object[]{dijName}, TomCheckerMessage.TOM_ERROR);
					return null;
				}
				if (first) { 
					res = dijName;
				} 
				else {
					first = false;
					if(expectedType !=currentType) {
						System.out.println(currentType+"!="+expectedType);
						messageError(decLine, 
																	TomCheckerMessage.InvalidDisjunctionCodomain,
																	new Object[]{dijName, currentType.getString(), expectedType.getString()}, TomCheckerMessage.TOM_ERROR);
						return null;
					}
				}
			}
		}
		return res;
	}
	
	/**
	 * RECORDS CONCERNS
	 */
	/*
	 private void ensureValidRecordDisjunction(NameList recorApplNameList, TomList pairSlotApplList) {
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

  private void verifyRecordStructure(OptionList option, String tomName, TomList args, int decLine)  {
	TomSymbol symbol = getSymbol(tomName);
	 if(symbol != null) {
		 SlotList slotList = symbol.getSlotList();
				// constants have an emptySlotList
				// the length of the slotList corresponds to the arity of the operator
				// list operator with [] no allowed
			if(slotList.isEmpty() || (args.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)))) {
				messageError(decLine,TomCheckerMessage.BracketOnListSymbol, new Object[]{tomName}, TomCheckerMessage.TOM_ERROR);
			}
			// TODO verify type
			verifyRecordSlots(args,slotList, tomName, decLine);
		} else {
			messageError(decLine,TomCheckerMessage.UnknownSymbol, new Object[]{tomName}, TomCheckerMessage.TOM_ERROR);
		}
	}
  
		// We test the existence of slotName contained in pairSlotAppl
		// and then the associated term
	private void verifyRecordSlots(TomList listPair, SlotList slotList, String methodName, int decLine) {
		ArrayList slotIndex = new ArrayList();
		while( !listPair.isEmpty() ) {
			TomTerm pair = listPair.getHead();
			TomName name = pair.getSlotName();
			int index = getSlotIndex(slotList,name);
			if(index < 0) {
				// Error: bad slot name
				ArrayList listOfPossibleSlot = new ArrayList();
				while ( !slotList.isEmpty() ) {
					TomName sname = slotList.getHead().getSlotName();
					if(!sname.isEmptyName()) {
						listOfPossibleSlot.add(sname.getString());
					}
					slotList = slotList.getTail();
				}
				messageError(decLine,TomCheckerMessage.BadSlotName, 
																new Object[]{name.getString(), methodName, listOfPossibleSlot.toString()}, TomCheckerMessage.TOM_ERROR); 
			} else {
				Integer integerIndex = new Integer(index);
				if(slotIndex.contains(integerIndex)) {
					  // Error: repeated slot
					messageError(decLine, TomCheckerMessage.SlotRepeated, new Object[]{methodName, name.getString()}, TomCheckerMessage.TOM_ERROR);
				}
				slotIndex.add(integerIndex);
			}
			listPair = listPair.getTail();
		}
	}
	
	/**
	 * Message Functions
	 */

	private void messageError(int errorLine, String msg, Object[] msgArg, int level) {
		String structName = currentTomStructureOrgTrack.getAstName().getString();
		messageError(errorLine, structName, msg, msgArg, level);
	}
	
	private void messageError(int errorLine, String structInfo, String msg, Object[] msgArgs, int level) {
		String fileName = currentTomStructureOrgTrack.getFileName().getString();
		int structDeclLine = currentTomStructureOrgTrack.getLine();
		messageError(errorLine, fileName, structInfo, structDeclLine, msg, msgArgs, level);
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
			addError("findOriginTrackingLine:  not found", getInput().getInputFileName(), 0, TomCheckerMessage.TOM_ERROR);
			System.out.println("findOriginTrackingLine: not found ");
				//throw new TomRuntimeException(new Throwable("foo"));

		}
	}
}  //Class TomChecker
