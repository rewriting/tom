/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (C) 2000-2004 INRIA
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
 * Julien Guyon
 *
 **/

package jtom.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import jtom.TomMessage;
import jtom.adt.tomsignature.types.Declaration;
import jtom.adt.tomsignature.types.Instruction;
import jtom.adt.tomsignature.types.NameList;
import jtom.adt.tomsignature.types.Option;
import jtom.adt.tomsignature.types.OptionList;
import jtom.adt.tomsignature.types.PatternInstructionList;
import jtom.adt.tomsignature.types.SlotList;
import jtom.adt.tomsignature.types.SymbolList;
import jtom.adt.tomsignature.types.TomList;
import jtom.adt.tomsignature.types.TomName;
import jtom.adt.tomsignature.types.TomRuleList;
import jtom.adt.tomsignature.types.TomSymbol;
import jtom.adt.tomsignature.types.TomTerm;
import jtom.adt.tomsignature.types.TomType;
import jtom.adt.tomsignature.types.TomTypeList;
import jtom.exception.TomRuntimeException;
import jtom.xml.Constants;
import tom.library.traversal.Collect1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { adt/TomSignature.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='noSyntaxCheck' altName='' description='Do not perform syntax checking' value='false'/></options>";

  /** op and type declarator */
  private final static String OPERATOR    = "Operator";
  private final static String CONSTRUCTOR = "%op";
  private final static String OP_ARRAY    = "%oparray";
  private final static String OP_LIST     = "%oplist";
  private final static String TYPE        = "Type";  
  private final static String TYPE_TERM   = "%typeterm";
  private final static String TYPE_ARRAY  = "%typearray";
  private final static String TYPE_LIST   = "%typelist";

  /** type function symbols */
  private final static String GET_FUN_SYM = "get_fun_sym";
  private final static String CMP_FUN_SYM = "cmp_fun_sym";
  private final static String EQUALS      = "equals";
  private final static String GET_SUBTERM = "get_subterm";
  private final static String GET_ELEMENT = "get_element";
  private final static String GET_SIZE    = "get_size";
  private final static String GET_HEAD    = "get_head";
  private final static String GET_TAIL    = "get_tail";
  private final static String IS_EMPTY    = "is_empty";
  /** operator function symbols */
  private final static String MAKE_APPEND = "make_append";
  private final static String MAKE_EMPTY  = "make_empty";
  private final static String MAKE_INSERT = "make_insert";
  private final static String MAKE        = "make";
  
  /** the list of already studied and declared Types */
  private ArrayList alreadyStudiedTypes =  new ArrayList();
  /** the list of already studied and declared Symbol */
  private ArrayList alreadyStudiedSymbols =  new ArrayList();
  /** the list of already studied and declared Rule Symbol */
  private ArrayList alreadyStudiedRule =  new ArrayList();
  
  /** List of expected functional dclaration in each type declaration */
  private final static ArrayList TypeTermSignature =
    new ArrayList(
                  Arrays.asList(new String[]{TomSyntaxChecker.GET_FUN_SYM,
                                             TomSyntaxChecker.CMP_FUN_SYM,
                                             TomSyntaxChecker.EQUALS,
                                             TomSyntaxChecker.GET_SUBTERM}));
  private final static ArrayList TypeArraySignature =
    new ArrayList(
                  Arrays.asList(new String[]{TomSyntaxChecker.GET_FUN_SYM,
                                             TomSyntaxChecker.CMP_FUN_SYM,
                                             TomSyntaxChecker.EQUALS,
                                             TomSyntaxChecker.GET_ELEMENT,
                                             TomSyntaxChecker.GET_SIZE}));
  private final static ArrayList TypeListSignature =
    new ArrayList(
                  Arrays.asList(new String[]{TomSyntaxChecker.GET_FUN_SYM,
                                             TomSyntaxChecker.CMP_FUN_SYM,
                                             TomSyntaxChecker.EQUALS,
                                             TomSyntaxChecker.GET_HEAD,
                                             TomSyntaxChecker.GET_TAIL,
                                             TomSyntaxChecker.IS_EMPTY}));
  
  /** Constructor */
  public TomSyntaxChecker() {
    super("TomSyntaxChecker");
    reinit();
  }

  /**
   * inherited from OptionOwner interface (plugin) 
   */
  public PlatformOptionList getDeclaredOptionList() {
    return OptionParser.xmlToOptionList(TomSyntaxChecker.DECLARED_OPTIONS);
  }
  
  protected void reinit() {
    super.reinit();
    alreadyStudiedTypes   = new ArrayList();
    alreadyStudiedSymbols = new ArrayList();
    alreadyStudiedRule    = new ArrayList();
    }
  
  public void run() {
    if(isActivated()) {
      strictType = !getOptionBooleanValue("lazyType");
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        checkSyntax((TomTerm)getWorkingTerm());
        // verbose
        getLogger().log(Level.INFO, "TomSyntaxCheckingPhase",
                        new Integer((int)(System.currentTimeMillis()-startChrono)));      
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, "ExceptionMessage",
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFile().getName(),
                                     e.getMessage() });
        e.printStackTrace();
      }
    } else {
      // syntax checker desactivated
      getLogger().log(Level.INFO, "SyntaxCheckerInactivated");
    }
  }
  
  private boolean isActivated() {
    return !getOptionBooleanValue("noSyntaxCheck");
  }

  /**
   * Syntax checking entry point:
   * Catch and verify all type and operator declaration,
   * Match and RuleSet instructions
   */
  private void checkSyntax(TomTerm parsedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              DeclarationToTomTerm(declaration) -> {
                // TOM Declaration
                `verifyDeclaration(declaration);
                return false;
              }
            }
          } else if(subject instanceof Instruction) {
            %match(Instruction subject) {
              Match(SubjectList(matchArgsList), patternInstructionList, list) -> {
                /*  TOM MATCH STRUCTURE*/
                `verifyMatch(matchArgsList, patternInstructionList, list);
                return false;
              }
              RuleSet(list, orgTrack) -> {
                /*  TOM RULE STRUCTURE*/
                `verifyRule(list, orgTrack);
                return false;
              }
            }
          } 
          return true;
        }
      }; // end new Collect1()
    
    // use a traversal to get all interesting subtree
    traversal().genericCollect(parsedTerm, collectAndVerify);   
  } //checkSyntax
  
  /*
   *  SYMBOL AND TYPE CONCERNS STARTS
   */
  private void verifyDeclaration(Declaration declaration) { 
    matchblock:{
      %match (Declaration declaration) {
        // Types
        TypeTermDecl(Name(tomName), tomList, orgTrack) -> {
          `verifyTypeDecl(TomSyntaxChecker.TYPE_TERM, tomName, tomList, orgTrack);
          break matchblock;
        }
        TypeListDecl(Name(tomName), tomList, orgTrack) -> {
          `verifyTypeDecl(TomSyntaxChecker.TYPE_LIST, tomName, tomList, orgTrack);
          break matchblock;
        }
        TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
          `verifyTypeDecl(TomSyntaxChecker.TYPE_ARRAY, tomName, tomList, orgTrack);
          break matchblock;
        }
        // Symbols
        SymbolDecl(Name(tomName))      -> {
          `verifySymbol(TomSyntaxChecker.CONSTRUCTOR, getSymbolFromName(tomName));
          break matchblock;
        }
        ArraySymbolDecl(Name(tomName)) -> {
          `verifySymbol(TomSyntaxChecker.OP_ARRAY, getSymbolFromName(tomName));
          break matchblock;
        }
        ListSymbolDecl(Name(tomName))  -> {
          `verifySymbol(TomSyntaxChecker.OP_LIST, getSymbolFromName(tomName));
          break matchblock;
        }
      }
    }
  } //verifyDeclaration
  
  ///////////////////////////////
  // TYPE DECLARATION CONCERNS //
  //////////////////////////////
  private void verifyTypeDecl(String declType, String tomName, TomList listOfDeclaration, Option typeOrgTrack) {
    currentTomStructureOrgTrack = typeOrgTrack;
    // ensure first definition
    verifyMultipleDefinition(tomName, declType, TYPE);
    // verify Macro functions
    ArrayList verifyList;
    if(declType == TomSyntaxChecker.TYPE_TERM) {
      verifyList = new ArrayList(TomSyntaxChecker.TypeTermSignature);
    } else if(declType == TomSyntaxChecker.TYPE_ARRAY) {
      verifyList = new ArrayList(TomSyntaxChecker.TypeArraySignature);
    } else {//if(declType == TomSyntaxChecker.TYPE_LIST) {
      verifyList = new ArrayList(TomSyntaxChecker.TypeListSignature);
    }
    
    %match(TomList listOfDeclaration) {
      (_*, DeclarationToTomTerm(d), _*) -> { // for each Declaration
        Declaration decl=`d;
        matchblock:{
          %match(Declaration decl) {
            // Common Macro functions
            GetFunctionSymbolDecl[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_FUN_SYM,verifyList,orgTrack, declType);
              break matchblock;
            }
            CompareFunctionSymbolDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.CMP_FUN_SYM,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.EQUALS,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            // Term specific Macro functions
            GetSubtermDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.GET_SUBTERM,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            // List specific Macro functions
            GetHeadDecl[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_HEAD,verifyList,orgTrack, declType);
              break matchblock;
            }
            GetTailDecl[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_TAIL,verifyList,orgTrack, declType);
              break matchblock;
            }
            IsEmptyDecl[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.IS_EMPTY,verifyList,orgTrack, declType);
              break matchblock;
            }
            // Array specific Macro functions
            GetElementDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> { 
              `checkFieldAndLinearArgs(TomSyntaxChecker.GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            GetSizeDecl[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_SIZE,verifyList,orgTrack, declType);
              break matchblock;
            }
          }
        }
      }
    }
    // remove non mandatory functions
    if(verifyList.contains(TomSyntaxChecker.EQUALS)) {
      verifyList.remove(verifyList.indexOf(TomSyntaxChecker.EQUALS));
    }    
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  } //verifyTypeDecl
  
  private void verifyMultipleDefinition(String name, String symbolType, String OperatorOrType) {
    ArrayList list;
    if (OperatorOrType.equals(TomSyntaxChecker.OPERATOR)) {
      list = alreadyStudiedSymbols;
    } else {
      list = alreadyStudiedTypes;
    }
    if(list.contains(name)) {
      messageError(currentTomStructureOrgTrack.getLine(), 
                   symbolType+" "+name, 
                   TomMessage.getMessage("MultipleSymbolDefinitionError"),
                   new Object[]{name});
    } else {
      list.add(name);
    }
  } //verifyMultipleDefinition
  
  private void checkField(String function, ArrayList foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function)); 
    } else {
      messageError(orgTrack.getLine(), 
                   "structure "+symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.getMessage("MacroFunctionRepeated"),
                   new Object[]{function});
    }
  } //checkField
   
  private void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      messageError(orgTrack.getLine(), 
                   symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.getMessage("NonLinearMacroFunction"),
                   new Object[]{function, name1});
    }
  } //checkFieldAndLinearArgs
  
  /////////////////////////////////
  // SYMBOL DECLARATION CONCERNS //
  /////////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol){
    int domainLength;
    String symbStrName = tomSymbol.getAstName().getString();
    OptionList optionList = tomSymbol.getOption();
    // We save first the origin tracking of the symbol declaration
    currentTomStructureOrgTrack = findOriginTracking(optionList);
    
      // ensure first definition then Codomain, Domain, Macros and Slots (Simple operator)
    verifyMultipleDefinition(symbStrName, symbolType, TomSyntaxChecker.OPERATOR);
    verifySymbolCodomain(getSymbolCodomain(tomSymbol).getString(), symbStrName, symbolType);
    domainLength = verifySymbolDomain(getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
      /*if(symbolType == CONSTRUCTOR) {
        verifySymbolSlotList(tomSymbol.getSlotList(), symbolType);
        }*/
  } //verifySymbol

  private void verifySymbolCodomain(String codomain, String symbName, String symbolType) {
    if(!testTypeExistence(codomain)) {
      messageError(currentTomStructureOrgTrack.getLine(), 
                   symbolType+" "+symbName, 
                   TomMessage.getMessage("SymbolCodomainError"),
                   new Object[]{symbName, codomain});
    }
  } //verifySymbolCodomain
   
  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType == TomSyntaxChecker.CONSTRUCTOR) {
      %match(TomTypeList args) {
        (_*,  TomTypeAlone(typeName),_*) -> { // for each symbol types
          if(!testTypeExistence(`typeName)) {
            messageError(currentTomStructureOrgTrack.getLine(), 
                         symbolType+" "+symbName, 
                         TomMessage.getMessage("SymbolDomainError"),
                         new Object[]{new Integer(position), symbName, `(typeName)});
          }
          position++;
        }
      }
      return (position-1);
    } else { // OPARRAY and OPLIST
      %match(TomTypeList args) {
        (TomTypeAlone(typeName)) -> {
          if(!testTypeExistence(`typeName)) {
            messageError(currentTomStructureOrgTrack.getLine(), 
                         symbolType+" "+symbName, 
                         TomMessage.getMessage("ListSymbolDomainError"),
                         new Object[]{symbName, `(typeName)});
          }
        }
      } //match
      return 1;
    }
  } //verifySymbolDomain
  
  private void verifySymbolMacroFunctions(OptionList list, int domainLength, String symbolType) {
    ArrayList verifyList = new ArrayList();
    boolean foundOpMake = false;
    if(symbolType == TomSyntaxChecker.CONSTRUCTOR){ //Nothing absolutely necessary
    } else if(symbolType == TomSyntaxChecker.OP_ARRAY ) {
      verifyList.add(TomSyntaxChecker.MAKE_EMPTY);
      verifyList.add(TomSyntaxChecker.MAKE_APPEND);
    } else if(symbolType == TomSyntaxChecker.OP_LIST) {
      verifyList.add(TomSyntaxChecker.MAKE_EMPTY);
      verifyList.add(TomSyntaxChecker.MAKE_INSERT); 
    }
  
    %match(OptionList list) {
      (_*, DeclarationToOption(d), _*) -> { // for each Declaration
        Declaration decl=`d;
        matchblock:{
          %match(Declaration decl ) {
            // for a array symbol 
            MakeEmptyArray[orgTrack=orgTrack] -> { 
              `checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList,orgTrack, symbolType);
              break matchblock;
            }
            MakeAddArray[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_APPEND, verifyList, orgTrack, name1, name2, symbolType);
              break matchblock;
            }
            // for a List symbol
            MakeEmptyList[orgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList,orgTrack, symbolType);         
              break matchblock;
            }
            MakeAddList[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_INSERT, verifyList, orgTrack, name1, name2, symbolType);
              break matchblock;
            }
            // for a symbol
            MakeDecl[args=makeArgsList, orgTrack=orgTrack] -> {
              if (!foundOpMake) {
                foundOpMake = true;
                `verifyMakeDeclArgs(makeArgsList, domainLength, orgTrack, symbolType);
              } else {
                messageError(`orgTrack.getLine(), 
                             symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                             TomMessage.getMessage("MacroFunctionRepeated"),
                             new Object[]{TomSyntaxChecker.MAKE});
              }
              break matchblock;
            }
          }
        }
      }
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbolType, verifyList);
    }
  }  //verifySymbolMacroFunctions
  
  private void verifyMakeDeclArgs(TomList argsList, int domainLength, Option orgTrack, String symbolType){
      // we test the necessity to use different names for each variable-parameter.
    int nbArgs = 0;
    ArrayList listVar = new ArrayList();
    %match(TomList argsList) {
      (_*, Variable[astName=Name(name)] ,_*) -> { // for each Macro variable
        if(listVar.contains(`name)) {
          messageError(orgTrack.getLine(), 
                       symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                       TomMessage.getMessage("NonLinearMacroFunction"),
                       new Object[]{TomSyntaxChecker.MAKE, `(name)});
        } else {
          listVar.add(`name);
        }
        nbArgs++;
      }
    }
    if(nbArgs != domainLength) {
      messageError(orgTrack.getLine(), 
                   symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.getMessage("BadMakeDefinition"),
                   new Object[]{new Integer(nbArgs), new Integer(domainLength)});     
    }
  } //verifyMakeDeclArgs
  
  private void verifySymbolSlotList(SlotList slotList, String symbolType) {
      // we test the existence of 2 same slot names
    ArrayList listSlot = new ArrayList();
    %match(SlotList slotList) {
      (_*, Slot[slotName=Name(name)], _*) -> { // for each Slot
        if(listSlot.contains(`name)) {
            //TODO
            //messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
        } else {
          listSlot.add(`name);
        }
      }
    }
  } //verifySymbolSlotList
   
  private void messageMissingMacroFunctions(String symbolType, ArrayList list) {
    String listOfMissingMacros = "";
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros+= list.get(i)+",  ";
    }
    listOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    messageError(currentTomStructureOrgTrack.getLine(), 
                 symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(),
                 TomMessage.getMessage("MissingMacroFunctions"),
                 new Object[]{listOfMissingMacros});
  } //messageMissingMacroFunctions

  //////////////////////////////// /
  // MATCH VERIFICATION CONCERNS ///
  //////////////////////////////////

  /*
   * Given a MatchConstruct's subject list and pattern-action list
   */
  private void verifyMatch(TomList subjectList, PatternInstructionList patternInstructionList, OptionList list) {
    currentTomStructureOrgTrack = findOriginTracking(list);
    ArrayList typeMatchArgs = new ArrayList(),
      nameMatchArgs = new ArrayList();
    // From the subjects list(match definition), we test each used type and keep them in memory
    %match(TomList subjectList) {
      concTomTerm(_*, TLVar(name, tomType@TomTypeAlone(type)), _*) -> { // for each Match args
        if (!testTypeExistence(`type)) {
          messageError(currentTomStructureOrgTrack.getLine(),
                       TomMessage.getMessage("UnknownMatchArgumentTypeInSignature"),
                       new Object[]{`name, `(type)});
          typeMatchArgs.add(null);
        } else {
          typeMatchArgs.add(`tomType);
        }
        if(nameMatchArgs.indexOf(`name) == -1) {
          nameMatchArgs.add(`name);
        } else {
          // Maybe its an error to have the 2 same name variable in the match definition: warn the user
          messageWarning(currentTomStructureOrgTrack.getLine(),
												 TomMessage.getMessage("RepeatedMatchArgumentName"),
                         new Object[]{`(name)});
        }
      } 
    }
    int nbExpectedArgs = typeMatchArgs.size();
    // we now compare pattern to its definition
    %match(PatternInstructionList patternInstructionList) {
      concPatternInstruction(_*, PatternInstruction[pattern=Pattern(terms)], _*) -> {
        // control each pattern vs the match definition
        `verifyMatchPattern(terms, typeMatchArgs, nbExpectedArgs);
      }
    }
  }
  
  // each patternList shall have the expected length and each term shall be valid
  private void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    int nbFoundArgs = termList.getLength();
    if(nbFoundArgs != nbExpectedArgs) {
      messageError(findOriginTrackingLine(termList.getHead().getOption()),
                   TomMessage.getMessage("BadMatchNumberArgument"),
                   new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)});
      // we can not continue because we will use the fact that each element of the pattern
      // has the expected type declared in the Match definition
      return ;
    }
    
    TomType expectedType;
    int counter = 0;
    %match(TomList termList) {
      concTomTerm(_*, term, _*) -> { // no term can be a  Var* nor _*: not allowed as top leftmost symbol
        TermDescription termDesc = analyseTerm(`term);
        if(termDesc.termClass == UNAMED_VARIABLE_STAR || termDesc.termClass == VARIABLE_STAR) {
          messageError(termDesc.decLine, 
                       TomMessage.getMessage("IncorrectVariableStarInMatch"), 
                       new Object[]{termDesc.name});
        } else {    // Analyse of the term if expectedType != null  
          expectedType = (TomType)typeMatchArgs.get(counter);
          if (expectedType != null) {
            // the type is known and found in the match signature
            validateTerm(`term, expectedType, false, true, false);
          }
        }
        counter++;
      }
    }
  }

  ///////////////////////////////// 
  // RULE VERIFICATION CONCERNS ///
  /////////////////////////////////
  private void verifyRule(TomRuleList ruleList, Option orgTrack) {
    int ruleNumber = 0;
    currentTomStructureOrgTrack = orgTrack;
    String headSymbolName = "Unknown return type";
    %match(TomRuleList ruleList) {  // for each rewrite rule
      b1: concTomRule(_*, RewriteRule(Term(lhs),Term(rhs),_,_),_*) -> {
        headSymbolName = `verifyLhsRuleAndConstructorEgality(lhs, headSymbolName, ruleNumber);
        if( headSymbolName == null ) { return; }
        `verifyRhsRuleStructure(rhs, headSymbolName);
        ruleNumber++;
      }
    }
  }
  
  private String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  headSymbolName, int ruleNumber) {
    String currentHeadSymbolName;
    TomType lhsType  = null;
    TomSymbol symbol = null;
      // We support only Appl and RecordAppl
    int termClass = getClass(lhs);
    if(  termClass != APPL && termClass != RECORD_APPL) {
      String termName;
      if (termClass == XML_APPL) { 
        termName = "XML construct "+getName(lhs);
      } else if (termClass ==  APPL_DISJUNCTION || termClass == RECORD_APPL_DISJUNCTION) {
        termName = "Disjunction";
      } else {
        termName = getName(lhs);
      } 
      messageError(findOriginTrackingLine(lhs.getOption()), 
                   TomMessage.getMessage("IncorrectRuleLHSClass"), new Object[]{termName});
      return null;
    }
    
    currentHeadSymbolName = getName(lhs);
    if(ruleNumber == 0) {
      // update the root of lhs: it becomes a defined symbol
      symbol = getAstFactory().updateDefinedSymbol(symbolTable(),lhs);
      if( symbol == null ) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.getMessage("UnknownSymbol"), 
                     new Object[]{currentHeadSymbolName});
        // We can not continue anymore
        return null;
      }
      //ensure we are able to construct this symbol
      if ( !findMakeDecl(symbol.getOption())) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.getMessage("NoRuleMakeDecl"), 
                     new Object[]{currentHeadSymbolName});
      }
      
      if(alreadyStudiedRule.contains(currentHeadSymbolName)) {
        messageError(currentTomStructureOrgTrack.getLine(),
                     TomMessage.getMessage("MultipleRuleDefinition"),
                     new Object[]{currentHeadSymbolName});
        return null;
      } else {
        alreadyStudiedRule.add(currentHeadSymbolName);
      }
    } else { //  ruleNumber > 0
      // Test constructor equality
      String newName = getName(lhs);
      if (!headSymbolName.equals(currentHeadSymbolName)) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.getMessage("DifferentRuleConstructor"), 
                     new Object[]{headSymbolName, currentHeadSymbolName}); 
      }
    }
    symbol = getSymbolFromName(currentHeadSymbolName);
    lhsType = getSymbolCodomain(symbol);
    // analyse the term
    validateTerm(lhs, lhsType, isListOperator(symbol)||isArrayOperator(symbol), true, false);
    return currentHeadSymbolName;
  }
  
  private boolean findMakeDecl(OptionList list) {
    %match(OptionList list) {
      (_*, DeclarationToOption(MakeDecl[]), _*) -> {
        return true;
      }
    }
    return false;
  }
  
  
  /**
   * Rhs shall have no underscore, be a var* nor _*, nor a RecordAppl
   */
  private void verifyRhsRuleStructure(TomTerm rhs, String lhsHeadSymbolName) {
    int termClass = getClass(rhs); //TermDescription termDesc = analyseTerm(lhs);
    if(  termClass != APPL) {
      String termName;
      if (termClass == XML_APPL) { 
        termName = "XML construct "+getName(rhs);
      } else if (termClass ==  APPL_DISJUNCTION || termClass == RECORD_APPL_DISJUNCTION) {
        termName = "Disjunction";
      } else if (termClass == RECORD_APPL) {
        termName = getName(rhs)+"[...]";
      } else {
        termName = getName(rhs);
      } 
      messageError(findOriginTrackingLine(rhs.getOption()), 
                   TomMessage.getMessage("IncorrectRuleRHSClass"), new Object[]{termName});
      return;
    }
    
    TomSymbol symbol = getSymbolFromName(lhsHeadSymbolName);
    TomType lhsType = getSymbolCodomain(symbol);
    TermDescription termDesc = validateTerm(rhs, lhsType, isListOperator(symbol)||isArrayOperator(symbol), true, true);
    TomType rhsType = termDesc.tomType;
    if(rhsType != null) {
      if(rhsType != lhsType) {
        String rhsTypeName;
        if(rhsType.isEmptyType()) {rhsTypeName = "Not Type Found";} else {rhsTypeName = rhsType.getString();}
        messageError(findOriginTrackingLine(rhs.getOption()), 
                     TomMessage.getMessage("IncorrectRuleRHSType"), new Object[]{rhsTypeName, lhsType.getString()});
      }
    }
  }
  
  /**
   * Analyse a term given an expected type and re-enter recursively on childs
   */
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel, boolean permissive) {
    String termName = "emptyName";
    TomType type = null;
    int termClass, decLine;
    Option orgTrack;
    matchblock:{
      %match(TomTerm term) {
        Appl[option=options, nameList=(Name("")), args=args] -> {
          decLine = findOriginTrackingLine(`options);
          termClass = UNAMED_APPL;
            // there shall be only one list symbol with expectedType as Codomain
            // else ensureValideUnamedList returns null
          TomSymbol symbol = ensureValideUnamedList(expectedType, decLine);
          if(symbol == null) {
            break matchblock;
          } else {
              //there is only one list symbol and its type is the expected one (ensure by ensureValideUnamedList call)
            type = expectedType;
            termName = symbol.getAstName().getString();
              // whatever the arity is, we continue recursively and there is only one element in the Domain
            validateListOperatorArgs(`args, symbol.getTypesToType().getDomain().getHead(),permissive);
            if(permissive) { System.out.println("UnamedList but permissive");}
            break matchblock;
          }
        }
        
        Appl[option=options, nameList=nameList, args=arguments] -> {
          TomList args = `arguments;
          decLine = findOriginTrackingLine(`options);
          termClass = APPL;
          
          TomSymbol symbol = ensureValidApplDisjunction(`nameList, expectedType, decLine,hasConstructor(`options), args.isEmpty(), permissive, topLevel);
          if(symbol == null) {
            break matchblock;
          }
            // Type is OK
          type = expectedType;     
          termName = `nameList.getHead().getString();
          boolean listOp = (isListOperator(symbol) || isArrayOperator(symbol));
          if(listOp) {
              // whatever the arity is, we continue recursively and there is only one element in the Domain
            validateListOperatorArgs(args, symbol.getTypesToType().getDomain().getHead(),permissive);
          } else {
              // the arity is important also there are different types in Domain
            TomTypeList  types = symbol.getTypesToType().getDomain();
            int nbArgs = args.getLength();
            int nbExpectedArgs = types.getLength();
            if(nbArgs != nbExpectedArgs) {
              messageError(decLine, TomMessage.getMessage("SymbolNumberArgument"), 
                           new Object[]{termName, new Integer(nbExpectedArgs), new Integer(nbArgs)});
              break matchblock;
            }
            while(!args.isEmpty()) {
                // repeat analyse with associated expected type and control arity
              validateTerm(args.getHead(), types.getHead(), listOp/*false*/, false, permissive);
              args = args.getTail();
              types = types.getTail();
            }
          }
          break matchblock;
        }
        
        rec@RecordAppl[option=options,nameList=nameList,args=pairSlotAppls] ->{
          if(permissive) {
            messageError(findOriginTrackingLine(`options), TomMessage.getMessage("IncorrectRuleRHSClass"), 
                         new Object[]{getName(`rec)+"[...]"});
          }
          decLine = findOriginTrackingLine(`options);
          termClass = RECORD_APPL;
  
          TomSymbol symbol = ensureValidRecordDisjunction(`nameList, expectedType, decLine, true);
          if(symbol == null) {
            break matchblock;
          }
          
          boolean first = true;
          %match(NameList nameList) { // We perform tests as we have different RecordAppls: they all must be valid
              // and have the expected return type
            (_*, Name(name), _*) -> {
              verifyRecordStructure(`options, `name, `pairSlotAppls, decLine);
            }
          }
          
          type = expectedType;     
          termName = `nameList.getHead().getString();
          break matchblock;
        }
        
        XMLAppl[option=options, nameList=(_*, Name(_), _*), childList=childList] -> {
            // TODO: can we do it
            // ensureValidDisjunction(nameList); ??????????
          termClass = XML_APPL;
          decLine = findOriginTrackingLine(`options);
          type = getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE));
          termName = Constants.ELEMENT_NODE;
          
          TomList args = `childList;
          /*
           * we cannot use the following expression
           *   TomType TNodeType = symbolTable().getType(Constants.TNODE);
           * because TNodeType should be a TomTypeAlone and not an expanded type
           */
          TomType TNodeType = getSymbolCodomain(symbolTable().getSymbolFromName(Constants.ELEMENT_NODE));
          //System.out.println("TNodeType = " + TNodeType);
          while(!args.isEmpty()) {
            // repeat analyse with associated expected type and control arity
            validateTerm(args.getHead(), TNodeType, true, false, permissive);
            args = args.getTail();
          }

          break matchblock;
        }
        
        Placeholder[option=options] -> {
          termClass = PLACE_HOLDER;
          decLine = findOriginTrackingLine(`options);
          type = null;     
          termName = "_";
          if(permissive) {
            messageError(decLine, TomMessage.getMessage("IncorrectRuleRHSClass"), 
                         new Object[]{termName});
          }
          break matchblock;
        }
        
        VariableStar[option=options, astName=Name(name)] -> { 
          termClass = VARIABLE_STAR;
          decLine = findOriginTrackingLine(`options);
          type = null;     
          termName = `name+"*";
          if(!listSymbol) {
            messageError(decLine, TomMessage.getMessage("InvalidVariableStarArgument"), 
                         new Object[]{termName});
          }
          break matchblock;
        }
        
        UnamedVariableStar[option=options] -> {
          termClass = UNAMED_VARIABLE_STAR;
          decLine = findOriginTrackingLine(`options);
          type = null;     
          termName = "_*";
          if(!listSymbol) {
            messageError(decLine, TomMessage.getMessage("InvalidVariableStarArgument"), 
                         new Object[]{termName});
          }
          if(permissive) {
            messageError(decLine, TomMessage.getMessage("IncorrectRuleRHSClass"), 
                         new Object[]{termName});
          }
          break matchblock;
        }
        
        _ -> {
          System.out.println("Strange term in pattern "+term);
          throw new TomRuntimeException("Strange Term "+term);
        }
      }
    }
    return new TermDescription(termClass, termName, decLine, type); 
  }

    public TermDescription analyseTerm(TomTerm term) {
    matchblock:{
      %match(TomTerm term) {
        Appl[option=options, nameList=(Name(str))] -> {
          if (`str.equals("")) {
            return new TermDescription(UNAMED_APPL, `str, findOriginTrackingLine(`options), 
                                       null);
              // TODO
          } else {
            return new TermDescription(APPL, `str, findOriginTrackingLine(`options), 
                                       getSymbolCodomain(getSymbolFromName(`str)));
          }
        }
        Appl[option=options, nameList=(Name(name), _*)] -> {
          return new TermDescription(APPL_DISJUNCTION, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name))] ->{
          return new TermDescription(RECORD_APPL, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name), _*)] ->{
          return new TermDescription(RECORD_APPL_DISJUNCTION, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbolFromName(`name)));
        }
        XMLAppl[option=options] -> {
          return new TermDescription(XML_APPL, Constants.ELEMENT_NODE, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE)));
        }
        Placeholder[option=options] -> {
          return new TermDescription(PLACE_HOLDER, "_", findOriginTrackingLine(`options),  null);
        }
        VariableStar[option=options, astName=Name(name)] -> { 
          return new TermDescription(VARIABLE_STAR, `name+"*", findOriginTrackingLine(`options),  null);
        }
        UnamedVariableStar[option=options] -> {
          return new TermDescription(UNAMED_VARIABLE_STAR, "_*", findOriginTrackingLine(`options),  null);
        }
        _ -> {
          System.out.println("Strange term "+term);
          throw new TomRuntimeException("Strange Term "+term);
        }
      }
    }
  }
  
  private TomSymbol ensureValideUnamedList(TomType expectedType, int decLine) {
    SymbolList symbolList = symbolTable().getSymbolFromType(expectedType);
    SymbolList filteredList = `emptySymbolList();
    %match(SymbolList symbolList) {
      (_*, symbol , _*) -> {  // for each symbol
        if(isArrayOperator(`symbol) || isListOperator(`symbol)) {
          filteredList = `manySymbolList(symbol,filteredList);
        }
      }
    }
    
    if(filteredList.isEmpty()) {
      messageError(decLine,
                   TomMessage.getMessage("UnknownUnamedList"),
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTail().isEmpty()) {
      String symbolsString = "";
      while(!filteredList.isEmpty()) {
        symbolsString += " "+filteredList.getHead().getAstName().getString();
        filteredList= filteredList.getTail();
      }
      messageError(decLine,
                   TomMessage.getMessage("AmbigousUnamedList"),
                   new Object[]{expectedType.getString(), symbolsString});
      return null;
    } else { 
      return filteredList.getHead();
    }
  }
  
  private TomSymbol ensureValidApplDisjunction(NameList nameList, TomType expectedType, int decLine,
                                               boolean constructor, boolean emptyChilds, boolean permissive, boolean topLevel) {
    TomTypeList domainReference = null, currentDomain = null;
    TomSymbol symbol = null;
    
    if(nameList.isSingle()) { // Valid but has is a good type
      String res = nameList.getHead().getString();
      symbol  =  getSymbolFromName(res);
      if (symbol == null ) {
        if((constructor || !emptyChilds)) {
            // this correspond to aterm like 'unknown()' or unknown(s1, s2, ...)
          if(!permissive) {
            messageError(decLine,
                         TomMessage.getMessage("UnknownSymbol"),
                         new Object[]{res});
          } else {
            messageWarning(decLine,
                         TomMessage.getMessage("UnknownPermissiveSymbol"),
                           new Object[]{res});
          }
        }
      } else { //known symbol     
        if(emptyChilds && !constructor ) { // this correspond to: known
          //  we know the symbol but it is not called has a constructor and argsList is empty
          // it is a builtin type (String, int, char, double, ...)
          // WARNING consider as a symbol and not a variable
          String codomain = getTomType(getSymbolCodomain(symbol));
          if( !symbolTable().isBuiltinType(codomain) ) {
            messageWarning(decLine, 
                         TomMessage.getMessage("AmbigousSymbolWithoutConstructor"),
                           new Object[]{res});
          }
        }
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getMessage("InvalidCodomain"), res, decLine)) {
            return null;
          }
        }
      }
      return symbol;
    }
      //  this is a disjunction
    if(permissive) { 
      messageError(decLine, 
                   TomMessage.getMessage("ImpossiblePermissiveAndDisjunction"),
                   new Object[]{});
    }
   
    // this part is common between Appl and records with multiple head symbols
    boolean first = true; // the first symbol give the expected type
    %match(NameList nameList) {
      (_*, Name(dijName), _*) -> { // for each SymbolName
        symbol =  getSymbolFromName(`dijName);
        if (symbol == null) {
            // In disjunction we can only have known symbols
          messageError(decLine,
                       TomMessage.getMessage("UnknownSymbolInDisjunction"),
                       new Object[]{`(dijName)});
          return null;
        }
        if ( strictType  || !topLevel ) {
            // ensure codomain is correct
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getMessage("InvalidDisjunctionCodomain"), `dijName, decLine)) {
            return null;
          }
        }
        currentDomain = getSymbolDomain(symbol);
        if (first) { // save Domain reference
          domainReference = currentDomain;
        } else {
          first = false;
          if(currentDomain != domainReference) {
            messageError(decLine, 
                         TomMessage.getMessage("InvalidDisjunctionDomain"),
                         new Object[]{`(dijName)});
            return null;
          }
        }
      }
    }
    return symbol;
  }
  
  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, String msg, String symbolName, int decLine) {
    if(currentCodomain != expectedType) {
      //System.out.println(currentCodomain+"!="+expectedType);
      messageError(decLine, 
                   msg,
                   new Object[]{symbolName, currentCodomain.getString(), expectedType.getString()});
      return false;
    }
    return true;
  }
        
  private TomSymbol ensureValidRecordDisjunction(NameList nameList, TomType expectedType, int decLine, boolean topLevel) {
    if(nameList.isSingle()) { // Valid but has is a good type
      String res = nameList.getHead().getString();
      TomSymbol symbol =  getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
          // it is not correct to use Record an unknown symbols
        messageError(decLine,
                     TomMessage.getMessage("UnknownSymbol"),
                     new Object[]{res});
        return null;    
      } else { // known symbol
          // ensure type correctness if necessary
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getMessage("InvalidCodomain"), res, decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      return ensureValidApplDisjunction(nameList, expectedType, decLine, false, false, false, topLevel);
    }
  }

  ///////////////////////
  // RECORDS CONCERNS ///
  ///////////////////////
  private void verifyRecordStructure(OptionList option, String tomName, TomList slotTermPairs, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotTermPairs.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)) ) {
        messageError(decLine,
                     TomMessage.getMessage("BracketOnListSymbol"),
                     new Object[]{tomName});
      }
        // TODO verify type
      verifyRecordSlots(slotTermPairs,slotList, getSymbolDomain(symbol), tomName, decLine);
    } else {
      messageError(decLine,
                   TomMessage.getMessage("UnknownSymbol"),
                   new Object[]{tomName});
    }
  }
  
    // We test the existence/repetition of slotName contained in pairSlotAppl
    // and then the associated term
  private void verifyRecordSlots(TomList pairList, SlotList slotList, TomTypeList typeList, String methodName, int decLine) {
  TomName pairSlotName = null;
  ArrayList listOfPossibleSlot = null;
  ArrayList studiedSlotIndexList = new ArrayList();
    //for each pair slotName <=> Appl
  while( !pairList.isEmpty() ) {
      pairSlotName = pairList.getHead().getSlotName();
        // First check for slot name correctness
      int index = getSlotIndex(slotList,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
          listOfPossibleSlot = new ArrayList();
          SlotList listOfSlots = slotList;
          while ( !listOfSlots.isEmpty() ) {
            TomName sname = listOfSlots.getHead().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTail();
          }
        }
        messageError(decLine,
                     TomMessage.getMessage("BadSlotName"), 
                     new Object[]{pairSlotName.getString(), methodName, listOfPossibleSlot.toString()});
        return; //break analyses
      } else { // then check for repeated good slot name
        Integer integerIndex = new Integer(index);
        if(studiedSlotIndexList.contains(integerIndex)) {
            // Error: repeated slot
          messageError(decLine,
                       TomMessage.getMessage("SlotRepeated"),
                       new Object[]{methodName, pairSlotName.getString()});
          return; //break analyses
        }
        studiedSlotIndexList.add(integerIndex);
      }
      
        // Now analyses associated term 
      SlotList listOfSlots = slotList;
      TomTypeList listOfTypes = typeList;
      while(!listOfSlots.isEmpty()) {
        TomList listOfPair = pairList;
        TomName slotName = listOfSlots.getHead().getSlotName();
        TomType expectedType = listOfTypes.getHead();
        if(!slotName.isEmptyName()) {
          // look for a same name (from record)
          whileBlock: {
            while(!listOfPair.isEmpty()) {
              TomTerm pairSlotTerm = listOfPair.getHead();
              %match(TomName slotName, TomTerm pairSlotTerm) {
                Name[string=name1], PairSlotAppl(Name[string=name1],slotSubterm) -> {
                     // bingo
                   validateTerm(`slotSubterm ,expectedType, false, true, false);
                   break whileBlock;
                 }
                _ , _ -> {listOfPair = listOfPair.getTail();}
              }
            }
          }
        }
        // prepare next step
        listOfSlots = listOfSlots.getTail();
        listOfTypes = listOfTypes.getTail();
      }
      
      pairList = pairList.getTail();
    }
  }

  public void validateListOperatorArgs(TomList args, TomType expectedType, boolean permissive) {
    while(!args.isEmpty()) {
      validateTerm(args.getHead(), expectedType, true, false, permissive);
      args = args.getTail();
    }
  }

  protected boolean testTypeExistence(String typeName) {
    return symbolTable().getType(typeName) != null;
  } //testTypeExistence
  
} // class TomSyntaxChecker
