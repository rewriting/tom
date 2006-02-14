/*
 *   
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2006, INRIA
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

package tom.engine.checker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import tom.engine.TomMessage;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.exception.TomRuntimeException;
import tom.engine.xml.Constants;
import tom.library.traversal.Collect1;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { adt/tomsignature/TomSignature.tom }

  /** the declared options string */
  public static final String DECLARED_OPTIONS = "<options><boolean name='noSyntaxCheck' altName='' description='Do not perform syntax checking' value='false'/></options>";

  /** op and type declarator */
  private final static String OPERATOR    = "Operator";
  private final static String CONSTRUCTOR = "%op";
  private final static String OP_ARRAY    = "%oparray";
  private final static String OP_LIST     = "%oplist";
  private final static String TYPE        = "Type";  
  private final static String TYPE_TERM   = "%typeterm";

  /** type function symbols */
  private final static String EQUALS      = "equals";
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
                  Arrays.asList(new String[]{ TomSyntaxChecker.EQUALS }));
  
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
        getLogger().log(Level.INFO, TomMessage.tomSyntaxCheckingPhase.getMessage(),
                        new Integer((int)(System.currentTimeMillis()-startChrono)));      
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFile().getName(),
                                     e.getMessage() });
        e.printStackTrace();
      }
    } else {
      // syntax checker desactivated
      getLogger().log(Level.INFO, TomMessage.syntaxCheckerInactivated.getMessage());
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
          if(subject instanceof Declaration) {
            // TOM Declaration
            `verifyDeclaration((Declaration)subject);
            return false;
          } else if(subject instanceof Instruction) {
            %match(Instruction subject) {
              Match(SubjectList(matchArgsList), patternInstructionList, list) -> {
                /*  TOM MATCH STRUCTURE*/
                `verifyMatch(matchArgsList, patternInstructionList, list);
                return true;//case when nested %match
              }
              Strategy(sName, visitList, orgTrack) -> {
                /*  STRATEGY MATCH STRUCTURE*/
                `verifyStrategy(sName, visitList, orgTrack);
                return true;//case when %match in %strategy
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
        TypeTermDecl(Name(tomName), declarationList, orgTrack) -> {
          `verifyTypeDecl(TomSyntaxChecker.TYPE_TERM, tomName, declarationList, orgTrack);
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
  private void verifyTypeDecl(String declType, String tomName, DeclarationList listOfDeclaration, Option typeOrgTrack) {
    currentTomStructureOrgTrack = typeOrgTrack;
    // ensure first definition
    verifyMultipleDefinition(tomName, declType, TYPE);
    // verify Macro functions
    ArrayList verifyList = new ArrayList(TomSyntaxChecker.TypeTermSignature);
    
    %match(DeclarationList listOfDeclaration) {
      (_*, d, _*) -> { // for each Declaration
        Declaration decl=`d;
        matchblock:{
          %match(Declaration decl) {
            // Common Macro functions
            TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.EQUALS,verifyList,orgTrack,name1,name2, declType);
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
            GetElementDecl[variable=Variable[astName=Name(name1)],index=Variable[astName=Name(name2)],orgTrack=orgTrack] -> { 
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
                   TomMessage.multipleSymbolDefinitionError.getMessage(),
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
                   TomMessage.macroFunctionRepeated.getMessage(),
                   new Object[]{function});
    }
  } //checkField
   
  private void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      messageError(orgTrack.getLine(), 
                   symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.nonLinearMacroFunction.getMessage(),
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
        verifySymbolPairNameDeclList(tomSymbol.getPairNameDeclList(), symbolType);
        }*/
  } //verifySymbol

  private void verifySymbolCodomain(String codomain, String symbName, String symbolType) {
    if(!testTypeExistence(codomain)) {
      messageError(currentTomStructureOrgTrack.getLine(), 
                   symbolType+" "+symbName, 
                   TomMessage.symbolCodomainError.getMessage(),
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
                         TomMessage.symbolDomainError.getMessage(),
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
                         TomMessage.listSymbolDomainError.getMessage(),
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
                             TomMessage.macroFunctionRepeated.getMessage(),
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
                       TomMessage.nonLinearMacroFunction.getMessage(),
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
                   TomMessage.badMakeDefinition.getMessage(),
                   new Object[]{new Integer(nbArgs), new Integer(domainLength)});     
    }
  } //verifyMakeDeclArgs
  
  private void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
      // we test the existence of 2 same slot names
    ArrayList listSlot = new ArrayList();
    %match(PairNameDeclList pairNameDeclList) {
      (_*, PairNameDecl[slotName=Name(name)], _*) -> { // for each Slot
        if(listSlot.contains(`name)) {
            //TODO
            //messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
        } else {
          listSlot.add(`name);
        }
      }
    }
  } //verifySymbolPairNameDeclList
   
  private void messageMissingMacroFunctions(String symbolType, ArrayList list) {
    StringBuffer listOfMissingMacros = new StringBuffer();
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros.append(list.get(i) + ",  ");
    } 
    String stringListOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    messageError(currentTomStructureOrgTrack.getLine(), 
                 symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(),
                 TomMessage.missingMacroFunctions.getMessage(),
                 new Object[]{stringListOfMissingMacros});
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
                       TomMessage.unknownMatchArgumentTypeInSignature.getMessage(),
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
                         TomMessage.repeatedMatchArgumentName.getMessage(),
                         new Object[]{`(name)});
        }
      } 
    }
    int nbExpectedArgs = typeMatchArgs.size();
    // we now compare pattern to its definition
    %match(PatternInstructionList patternInstructionList) {
      concPatternInstruction(_*, PatternInstruction[pattern=Pattern[tomList=terms,guards=guards]], _*) -> {
        // control each pattern vs the match definition
        `verifyMatchPattern(terms, typeMatchArgs, nbExpectedArgs);
        `verifyWhenPattern(guards);
      }
    }
  }
  
  // each patternList shall have the expected length and each term shall be valid
  private void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    int nbFoundArgs = termList.getLength();
    if(nbFoundArgs != nbExpectedArgs) {
      messageError(findOriginTrackingLine(termList.getHead().getOption()),
                   TomMessage.badMatchNumberArgument.getMessage(),
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
                       TomMessage.incorrectVariableStarInMatch.getMessage(), 
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

  // each term shall be valid
  private void verifyWhenPattern(TomList termList) {
    %match(TomList termList) {
      concTomTerm(_*, term, _*) -> {
        // the type is boolean, no variablestar, toplevel and permisive
        `validateTerm(term, TomTypeAlone("boolean") , false, true, true);
      }
    }
  }

  ///////////////////////////////// 
  //STRATEGY VERIFICATION CONCERNS /
  /////////////////////////////////
  private void verifyStrategy(TomName sName, TomVisitList visitList, Option orgTrack) {
    currentTomStructureOrgTrack = orgTrack;
    while(!visitList.isEmpty()) {
      TomVisit visit = visitList.getHead();
      verifyVisit(visit);
      // next visit
      visitList = visitList.getTail();
    }
  }

  private void verifyVisit(TomVisit visit){
    ArrayList typeMatchArgs = new ArrayList();
    %match(TomVisit visit) {
      VisitTerm(type,patternInstructionList) -> {
        typeMatchArgs.add(`type);
        // we now compare pattern to its definition
        %match(PatternInstructionList patternInstructionList) {
          concPatternInstruction(_*, PatternInstruction[pattern=Pattern[tomList=terms,guards=guards]], _*) -> {
            // control each pattern vs the match definition
            //always 1 expected argument in visit
            `verifyMatchPattern(terms, typeMatchArgs, 1);
            `verifyWhenPattern(guards);
          }      
        }
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
      // We support only TermAppl and RecordAppl
    int termClass = getClass(lhs);
    if(  termClass != TERM_APPL && termClass != RECORD_APPL) {
      String termName;
      if (termClass == XML_APPL) { 
        termName = "XML construct "+getName(lhs);
      } else if (termClass ==  APPL_DISJUNCTION || termClass == RECORD_APPL_DISJUNCTION) {
        termName = "Disjunction";
      } else {
        termName = getName(lhs);
      } 
      messageError(findOriginTrackingLine(lhs.getOption()), 
                   TomMessage.incorrectRuleLHSClass.getMessage(), new Object[]{termName});
      return null;
    }
    
    currentHeadSymbolName = getName(lhs);
    if(ruleNumber == 0) {
      // update the root of lhs: it becomes a defined symbol
      symbol = getAstFactory().updateDefinedSymbol(symbolTable(),lhs);
      if( symbol == null ) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.unknownSymbol.getMessage(), 
                     new Object[]{currentHeadSymbolName});
        // We can not continue anymore
        return null;
      }
      //ensure we are able to construct this symbol
      if ( !findMakeDecl(symbol.getOption())) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.noRuleMakeDecl.getMessage(), 
                     new Object[]{currentHeadSymbolName});
      }
      
      if(alreadyStudiedRule.contains(currentHeadSymbolName)) {
        messageError(currentTomStructureOrgTrack.getLine(),
                     TomMessage.multipleRuleDefinition.getMessage(),
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
                     TomMessage.differentRuleConstructor.getMessage(), 
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
    if(  termClass != TERM_APPL) {
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
                   TomMessage.incorrectRuleRHSClass.getMessage(), new Object[]{termName});
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
                     TomMessage.incorrectRuleRHSType.getMessage(), new Object[]{rhsTypeName, lhsType.getString()});
      }
    }
  }
  
  /**
   * Analyse a term given an expected type and re-enter recursively on childs
   */
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel, boolean permissive) {
    String termName = "emptyName";
    TomType type = null;
    int termClass=-1;
		int decLine=-1;
    Option orgTrack;
    matchblock:{
      %match(TomTerm term) {
        TermAppl[option=options, nameList=(Name("")), args=args] -> {
          decLine = findOriginTrackingLine(`options);
          termClass = UNAMED_APPL;
            // there shall be only one list symbol with expectedType as Codomain
            // else ensureValidUnamedList returns null
          TomSymbol symbol = ensureValidUnamedList(expectedType, decLine);
          if(symbol == null) {
            break matchblock;
          } else {
              //there is only one list symbol and its type is the expected one (ensure by ensureValidUnamedList call)
            type = expectedType;
            termName = symbol.getAstName().getString();
              // whatever the arity is, we continue recursively and there is only one element in the Domain
            validateListOperatorArgs(`args, symbol.getTypesToType().getDomain().getHead(),permissive);
            if(permissive) { System.out.println("UnamedList but permissive");}
            break matchblock;
          }
        }
        
        TermAppl[option=options, nameList=nameList, args=arguments] -> {
          TomList args = `arguments;
          decLine = findOriginTrackingLine(`options);
          termClass = TERM_APPL;
          
          TomSymbol symbol = ensureValidApplDisjunction(`nameList, expectedType, decLine,hasConstructor(`options), args.isEmpty(), permissive, topLevel);
          if(symbol == null) {
            validateTermThrough(term,permissive);
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
              messageError(decLine, TomMessage.symbolNumberArgument.getMessage(), 
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
        
        rec@RecordAppl[option=options,nameList=nameList,slots=slotList] ->{
          if(permissive) {
            messageError(findOriginTrackingLine(`options), TomMessage.incorrectRuleRHSClass.getMessage(), 
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
              verifyRecordStructure(`options, `name, `slotList, decLine);
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
            messageError(decLine, TomMessage.incorrectRuleRHSClass.getMessage(), 
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
            messageError(decLine, TomMessage.invalidVariableStarArgument.getMessage(), 
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
            messageError(decLine, TomMessage.invalidVariableStarArgument.getMessage(), 
                         new Object[]{termName});
          }
          if(permissive) {
            messageError(decLine, TomMessage.incorrectRuleRHSClass.getMessage(), 
                         new Object[]{termName});
          }
          break matchblock;
        }
      }
			System.out.println("Strange term in pattern "+term);
			throw new TomRuntimeException("Strange Term "+term);
    }
    return new TermDescription(termClass, termName, decLine, type); 
  }
  
  private void validateTermThrough(TomTerm term, boolean permissive) {
    %match(TomTerm term) {
			TermAppl[args=arguments] -> {
				TomList args = `arguments;
				while(!args.isEmpty()) {
					TomTerm child = args.getHead();
					TomSymbol sym = getSymbolFromName(getName(child));
					if(sym != null) {
						validateTerm(child,sym.getTypesToType().getCodomain(),false,false,permissive);
					} else {
						validateTermThrough(child,permissive);
					}
					args = args.getTail();
				}
			}
    }
  }

  public TermDescription analyseTerm(TomTerm term) {
    matchblock:{
      %match(TomTerm term) {
        TermAppl[option=options, nameList=(Name(str))] -> {
          if (`str.equals("")) {
            return new TermDescription(UNAMED_APPL, `str, findOriginTrackingLine(`options), 
                                       null);
              // TODO
          } else {
            return new TermDescription(TERM_APPL, `str, findOriginTrackingLine(`options), 
                                       getSymbolCodomain(getSymbolFromName(`str)));
          }
        }
        TermAppl[option=options, nameList=(Name(name), _*)] -> {
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
      }
			System.out.println("Strange term "+term);
			throw new TomRuntimeException("Strange Term "+term);
    }
  }

  private TomSymbol ensureValidUnamedList(TomType expectedType, int decLine) {
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
                   TomMessage.unknownUnamedList.getMessage(),
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTail().isEmpty()) {
      StringBuffer symbolsString = new StringBuffer();
      while(!filteredList.isEmpty()) {
        symbolsString .append(" " + filteredList.getHead().getAstName().getString());
        filteredList= filteredList.getTail();
      }
      messageError(decLine,
                   TomMessage.ambigousUnamedList.getMessage(),
                   new Object[]{expectedType.getString(), symbolsString.toString()});
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
                         TomMessage.unknownSymbol.getMessage(),
                         new Object[]{res});
          } else {
            messageWarning(decLine,
                         TomMessage.unknownPermissiveSymbol.getMessage(),
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
            messageError(decLine, 
                         TomMessage.ambigousSymbolWithoutConstructor.getMessage(),
                           new Object[]{res});
          }
        }
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, decLine)) {
            return null;
          }
        }
      }
      return symbol;
    }
      //  this is a disjunction
    if(permissive) { 
      messageError(decLine, 
                   TomMessage.impossiblePermissiveAndDisjunction.getMessage(),
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
                       TomMessage.unknownSymbolInDisjunction.getMessage(),
                       new Object[]{`(dijName)});
          return null;
        }
        if ( strictType  || !topLevel ) {
            // ensure codomain is correct
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, decLine)) {
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
                         TomMessage.invalidDisjunctionDomain.getMessage(),
                         new Object[]{`(dijName)});
            return null;
          }
        }
      }
    }
    return symbol;
  }
  
  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, int decLine) {
    if(currentCodomain != expectedType) {
      //System.out.println(currentCodomain+"!="+expectedType);
      messageError(decLine, 
                   msg.getMessage(),
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
                     TomMessage.unknownSymbol.getMessage(),
                     new Object[]{res});
        return null;    
      } else { // known symbol
          // ensure type correctness if necessary
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, decLine)) {
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
  private void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
        // constants have an emptyPairNameDeclList
        // the length of the pairNameDeclList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotList.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)) ) {
        messageError(decLine,
                     TomMessage.bracketOnListSymbol.getMessage(),
                     new Object[]{tomName});
      }
        // TODO verify type
      verifyRecordSlots(slotList,symbol, getSymbolDomain(symbol), tomName, decLine);
    } else {
      messageError(decLine,
                   TomMessage.unknownSymbol.getMessage(),
                   new Object[]{tomName});
    }
  }
  
    // We test the existence/repetition of slotName contained in pairSlotAppl
    // and then the associated term
  private void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, int decLine) {
  TomName pairSlotName = null;
  ArrayList listOfPossibleSlot = null;
  ArrayList studiedSlotIndexList = new ArrayList();
    //for each pair slotName <=> Appl
  while( !slotList.isEmpty() ) {
      pairSlotName = slotList.getHead().getSlotName();
        // First check for slot name correctness
      int index = getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
          listOfPossibleSlot = new ArrayList();
          PairNameDeclList listOfSlots = tomSymbol.getPairNameDeclList();
          while ( !listOfSlots.isEmpty() ) {
            TomName sname = listOfSlots.getHead().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTail();
          }
        }
        messageError(decLine,
                     TomMessage.badSlotName.getMessage(), 
                     new Object[]{pairSlotName.getString(), methodName, listOfPossibleSlot.toString()});
        return; //break analyses
      } else { // then check for repeated good slot name
        Integer integerIndex = new Integer(index);
        if(studiedSlotIndexList.contains(integerIndex)) {
            // Error: repeated slot
          messageError(decLine,
                       TomMessage.slotRepeated.getMessage(),
                       new Object[]{methodName, pairSlotName.getString()});
          return; //break analyses
        }
        studiedSlotIndexList.add(integerIndex);
      }
      
        // Now analyses associated term 
      PairNameDeclList listOfSlots =  tomSymbol.getPairNameDeclList();
      TomTypeList listOfTypes = typeList;
      while(!listOfSlots.isEmpty()) {
        SlotList listOfPair = slotList;
        TomName slotName = listOfSlots.getHead().getSlotName();
        TomType expectedType = listOfTypes.getHead();
        if(!slotName.isEmptyName()) {
          // look for a same name (from record)
          whileBlock: {
            while(!listOfPair.isEmpty()) {
              Slot pairSlotTerm = listOfPair.getHead();
              %match(TomName slotName, Slot pairSlotTerm) {
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
      
      slotList = slotList.getTail();
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
