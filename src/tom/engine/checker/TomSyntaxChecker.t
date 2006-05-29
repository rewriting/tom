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
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;
import tom.engine.tools.ASTFactory;

import tom.library.strategy.mutraveler.MuTraveler;
import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * The TomSyntaxChecker plugin.
 */
public class TomSyntaxChecker extends TomChecker {

  %include { adt/tomsignature/TomSignature.tom }
  %include { mutraveler.tom }

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
  private  ArrayList alreadyStudiedTypes =  new ArrayList();
  /** the list of already studied and declared Symbol */
  private  ArrayList alreadyStudiedSymbols =  new ArrayList();
  /** the list of already studied and declared Rule Symbol */
  private  ArrayList alreadyStudiedRule =  new ArrayList();

  /** List of expected functional declaration in each type declaration */
  private final static ArrayList TypeTermSignature =
    new ArrayList( Arrays.asList(new String[]{ TomSyntaxChecker.EQUALS }));

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
        try {
          MuTraveler.init(`mu(MuVar("x"),Try(Sequence(checkSyntax(this),All(MuVar("x")))))).visit((TomTerm)getWorkingTerm());
        } catch(jjtraveler.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        getLogger().log(Level.INFO, TomMessage.tomSyntaxCheckingPhase.getMessage(),
            new Integer((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        getLogger().log(Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
                        new Object[]{getClass().getName(),
                                     getStreamManager().getInputFileName(),
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
  %typeterm TomSyntaxChecker { implement { TomSyntaxChecker } }
  %strategy checkSyntax(tsc:TomSyntaxChecker) extends `Identity() {
    visit Declaration {
      Strategy[visitList = list,orgTrack=origin] -> {
        if(`list.isEmpty()) {
          %match(Option `origin) {
            OriginTracking[fileName=fileName,line=line] -> { 
              tsc.messageError(`fileName,`line,TomMessage.emptyStrategy,new Object[]{});
            }
          }
          tsc.messageError("unknown",-1,TomMessage.emptyStrategy,new Object[]{});
        }
        /*  STRATEGY MATCH STRUCTURE*/
        tsc.verifyStrategy(`list);
      }
      RuleSet(list, optionList) -> {
        /*  TOM RULE STRUCTURE*/
        tsc.verifyRule(`list, `optionList);
        `Fail().visit(null);
      }
      // Types
      TypeTermDecl(Name(tomName), declarationList, orgTrack) -> {
        tsc.verifyTypeDecl(TomSyntaxChecker.TYPE_TERM, `tomName, `declarationList, `orgTrack);
        `Fail().visit(null);
      }
      // Symbols
      SymbolDecl(Name(tomName)) -> {
        tsc.verifySymbol(TomSyntaxChecker.CONSTRUCTOR, tsc.getSymbolFromName(`tomName));
        `Fail().visit(null);
      }
      ArraySymbolDecl(Name(tomName)) -> {
        tsc.verifySymbol(TomSyntaxChecker.OP_ARRAY, tsc.getSymbolFromName(`tomName));
        `Fail().visit(null);
      }
      ListSymbolDecl(Name(tomName))  -> {
        tsc.verifySymbol(TomSyntaxChecker.OP_LIST, tsc.getSymbolFromName(`tomName));
        `Fail().visit(null);
      }
    }

   visit Instruction {
     Match(SubjectList(matchArgsList), patternInstructionList, list) -> {
       /*  TOM MATCH STRUCTURE*/
       tsc.verifyMatch(`matchArgsList, `patternInstructionList, `list);
     }
   }
  }

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
        Declaration decl = `d;
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
      messageError(currentTomStructureOrgTrack.getFileName(),
          currentTomStructureOrgTrack.getLine(),
          TomMessage.multipleSymbolDefinitionError,
          new Object[]{name});
    } else {
      list.add(name);
    }
  } //verifyMultipleDefinition

  private  void checkField(String function, ArrayList foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function));
    } else {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.macroFunctionRepeated,
                   new Object[]{function});
    }
  } //checkField

  private  void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.nonLinearMacroFunction,
                   new Object[]{function, name1});
    }
  } //checkFieldAndLinearArgs

  /////////////////////////////////
  // SYMBOL DECLARATION CONCERNS //
  /////////////////////////////////
  private  void verifySymbol(String symbolType, TomSymbol tomSymbol){
    int domainLength;
    String symbStrName = tomSymbol.getAstName().getString();
    OptionList optionList = tomSymbol.getOption();
    // We save first the origin tracking of the symbol declaration
    currentTomStructureOrgTrack = findOriginTracking(optionList);

      // ensure first definition then Codomain, Domain, Macros and Slots (Simple operator)
    verifyMultipleDefinition(symbStrName, symbolType, TomSyntaxChecker.OPERATOR);
    verifySymbolCodomain(getSymbolCodomain(tomSymbol), symbStrName, symbolType);
    domainLength = verifySymbolDomain(getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
  } //verifySymbol

  private  void verifySymbolCodomain(TomType codomain, String symbName, String symbolType) {
    %match(TomType codomain) {
      Codomain(Name(opName)) -> {
        if(symbolTable().getSymbolFromName(`opName) == null) {
          messageError(currentTomStructureOrgTrack.getFileName(),currentTomStructureOrgTrack.getLine(),
              TomMessage.symbolCodomainError,
              new Object[]{symbName, codomain});
        }
        return;
      }

      _ -> {
        if(!testTypeExistence(codomain.getString())) {
          messageError(currentTomStructureOrgTrack.getFileName(),currentTomStructureOrgTrack.getLine(),
              TomMessage.symbolCodomainError,
              new Object[]{symbName, codomain});
        }
      }
    }
  }

  private  int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType == TomSyntaxChecker.CONSTRUCTOR) {
      %match(TomTypeList args) {
        (_*,  TomTypeAlone(typeName),_*) -> { // for each symbol types
          if(!testTypeExistence(`typeName)) {
            messageError(currentTomStructureOrgTrack.getFileName(),
                currentTomStructureOrgTrack.getLine(),
                TomMessage.symbolDomainError,
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
            messageError(currentTomStructureOrgTrack.getFileName(),
                currentTomStructureOrgTrack.getLine(),
                TomMessage.listSymbolDomainError,
                new Object[]{symbName, `(typeName)});
          }
        }
      } //match
      return 1;
    }
  } //verifySymbolDomain

  private  void verifySymbolMacroFunctions(OptionList list, int domainLength, String symbolType) {
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
            MakeDecl[args=makeArgsList, orgTrack=og@OriginTracking[fileName=fileName,line=line]] -> {
              if (!foundOpMake) {
                foundOpMake = true;
                `verifyMakeDeclArgs(makeArgsList, domainLength, og, symbolType);
              } else {
                messageError(`fileName, `line,
                             TomMessage.macroFunctionRepeated,
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

  private  void verifyMakeDeclArgs(TomList argsList, int domainLength, Option orgTrack, String symbolType){
      // we test the necessity to use different names for each variable-parameter.
    int nbArgs = 0;
    ArrayList listVar = new ArrayList();
    %match(TomList argsList) {
      (_*, Variable[astName=Name(name)] ,_*) -> { // for each Macro variable
        if(listVar.contains(`name)) {
          messageError(orgTrack.getFileName(),orgTrack.getLine(),
                       TomMessage.nonLinearMacroFunction,
                       new Object[]{TomSyntaxChecker.MAKE, `(name)});
        } else {
          listVar.add(`name);
        }
        nbArgs++;
      }
    }
    if(nbArgs != domainLength) {
      messageError(orgTrack.getFileName(),orgTrack.getLine(),
                   TomMessage.badMakeDefinition,
                   new Object[]{new Integer(nbArgs), new Integer(domainLength)});
    }
  } //verifyMakeDeclArgs

  private  void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
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

  private  void messageMissingMacroFunctions(String symbolType, ArrayList list) {
    StringBuffer listOfMissingMacros = new StringBuffer();
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros.append(list.get(i) + ",  ");
    }
    String stringListOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    messageError(currentTomStructureOrgTrack.getFileName(),
        currentTomStructureOrgTrack.getLine(),
                 TomMessage.missingMacroFunctions,
                 new Object[]{stringListOfMissingMacros});
  } //messageMissingMacroFunctions

  //////////////////////////////// /
  // MATCH VERIFICATION CONCERNS ///
  //////////////////////////////////

  /*
   * Given a MatchConstruct's subject list and pattern-action list
   */
  private  void verifyMatch(TomList subjectList, PatternInstructionList patternInstructionList, OptionList list) {
    currentTomStructureOrgTrack = findOriginTracking(list);
    ArrayList typeMatchArgs = new ArrayList(),
      nameMatchArgs = new ArrayList();
    // From the subjects list(match definition), we test each used type and keep them in memory
    %match(TomList subjectList) {
      concTomTerm(_*, TLVar(name, tomType@TomTypeAlone(type)), _*) -> { // for each Match args
        if (!testTypeExistence(`type)) {
          messageError(currentTomStructureOrgTrack.getFileName(),
              currentTomStructureOrgTrack.getLine(),
                       TomMessage.unknownMatchArgumentTypeInSignature,
                       new Object[]{`name, `(type)});
          typeMatchArgs.add(null);
        } else {
          typeMatchArgs.add(`tomType);
        }
        if(nameMatchArgs.indexOf(`name) == -1) {
          nameMatchArgs.add(`name);
        } else {
          // Maybe its an error to have the 2 same name variable in the match definition: warn the user
          messageWarning(currentTomStructureOrgTrack.getFileName(),
              currentTomStructureOrgTrack.getLine(),
              TomMessage.repeatedMatchArgumentName,
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
  private  void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    int nbFoundArgs = termList.getLength();
    if(nbFoundArgs != nbExpectedArgs) {
      OptionList og = termList.getHead().getOption();
      messageError(findOriginTrackingFileName(og),findOriginTrackingLine(og),
                   TomMessage.badMatchNumberArgument,
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
        if(termDesc.getTermClass() == UNAMED_VARIABLE_STAR || termDesc.getTermClass() == VARIABLE_STAR) {
          messageError(termDesc.getFileName(),termDesc.getLine(),
                       TomMessage.incorrectVariableStarInMatch,
                       new Object[]{termDesc.getName()});
        } else {    // Analyse the term if expectedType != null
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
  private  void verifyWhenPattern(TomList termList) {
    %match(TomList termList) {
      concTomTerm(_*, term, _*) -> {
        // the type is boolean, no variablestar, toplevel and permissive
        `validateTerm(term, TomTypeAlone("boolean") , false, true, true);
      }
    }
  }

  /////////////////////////////////
  //STRATEGY VERIFICATION CONCERNS /
  /////////////////////////////////
  private  void verifyStrategy(TomVisitList visitList){
    while(!visitList.isEmpty()) {
      TomVisit visit = visitList.getHead();
      verifyVisit(visit);
      // next visit
      visitList = visitList.getTail();
    }
  }

  private  void verifyVisit(TomVisit visit){
    ArrayList typeMatchArgs = new ArrayList();
    %match(TomVisit visit) {
      VisitTerm(type,patternInstructionList,_) -> {
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
  private  void verifyRule(TomRuleList ruleList, OptionList optionList) {
    int ruleNumber = 0;
    currentTomStructureOrgTrack = findOriginTracking(optionList);
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

  private  String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  headSymbolName, int ruleNumber) {
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
      messageError(findOriginTrackingFileName(lhs.getOption()),
          findOriginTrackingLine(lhs.getOption()),
          TomMessage.incorrectRuleLHSClass, new Object[]{termName});
      return null;
    }

    currentHeadSymbolName = getName(lhs);
    if(ruleNumber == 0) {
      // update the root of lhs: it becomes a defined symbol
      symbol = ASTFactory.updateDefinedSymbol(symbolTable(),lhs);
      if( symbol == null ) {
        messageError(findOriginTrackingFileName(lhs.getOption()),
            findOriginTrackingLine(lhs.getOption()),
            TomMessage.unknownSymbol,
            new Object[]{currentHeadSymbolName});
        // We can not continue anymore
        return null;
      }
      //ensure we are able to construct this symbol
      if ( !findMakeDecl(symbol.getOption())) {
        messageError(findOriginTrackingFileName(lhs.getOption()),
            findOriginTrackingLine(lhs.getOption()),
                     TomMessage.noRuleMakeDecl,
                     new Object[]{currentHeadSymbolName});
      }

      if(alreadyStudiedRule.contains(currentHeadSymbolName)) {
        messageError(currentTomStructureOrgTrack.getFileName(),
          currentTomStructureOrgTrack.getLine(),
                     TomMessage.multipleRuleDefinition,
                     new Object[]{currentHeadSymbolName});
        return null;
      } else {
        alreadyStudiedRule.add(currentHeadSymbolName);
      }
    } else { //  ruleNumber > 0
      // Test constructor equality
      String newName = getName(lhs);
      if (!headSymbolName.equals(currentHeadSymbolName)) {
        messageError(findOriginTrackingFileName(lhs.getOption()),
          findOriginTrackingLine(lhs.getOption()),
                     TomMessage.differentRuleConstructor,
                     new Object[]{headSymbolName, currentHeadSymbolName});
      }
    }
    symbol = getSymbolFromName(currentHeadSymbolName);
    lhsType = getSymbolCodomain(symbol);
    // analyse the term
    validateTerm(lhs, lhsType,
        isListOperator(symbol)||isArrayOperator(symbol), true, false);
    return currentHeadSymbolName;
  }

  private static boolean findMakeDecl(OptionList list) {
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
  private  void verifyRhsRuleStructure(TomTerm rhs, String lhsHeadSymbolName) {
    int termClass = getClass(rhs); 
    if(termClass != TERM_APPL && termClass != VARIABLE) {
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
      messageError(findOriginTrackingFileName(rhs.getOption()),
          findOriginTrackingLine(rhs.getOption()),
                   TomMessage.incorrectRuleRHSClass, new Object[]{termName});
      return;
    }

    TomSymbol symbol = getSymbolFromName(lhsHeadSymbolName);
    TomType lhsType = getSymbolCodomain(symbol);
    TermDescription termDesc = validateTerm(rhs, lhsType, isListOperator(symbol)||isArrayOperator(symbol), true, true);
    TomType rhsType = termDesc.getType();
    if(termClass == TERM_APPL && rhsType != lhsType) {
        String rhsTypeName;
        if(rhsType.isEmptyType()) {
          rhsTypeName = "No Type Found";
        } else {
          rhsTypeName = rhsType.getString();
			messageError(findOriginTrackingFileName(rhs.getOption()),
					findOriginTrackingLine(rhs.getOption()),
					TomMessage.incorrectRuleRHSType,
					new Object[]{rhsTypeName, lhsType.getString()});
        }
		}
	}

  /**
   * Analyse a term given an expected type and re-enter recursively on children
   */
  public  TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel, boolean permissive) {
    String termName = "emptyName";
    TomType type = null;
    int termClass=-1;
    String fileName = "unknown";
    int decLine=-1;
    Option orgTrack;
    matchblock:{
      %match(TomTerm term) {
        TermAppl[option=options, nameList=(Name("")), args=args] -> {
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          termClass = UNAMED_APPL;
            // there shall be only one list symbol with expectedType as Codomain
            // else ensureValidUnamedList returns null
          TomSymbol symbol = ensureValidUnamedList(expectedType, fileName,decLine);
          if(symbol == null) {
            break matchblock;
          } else {
            //there is only one list symbol and its type is the expected one
            // (ensure by ensureValidUnamedList call)
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
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          termClass = TERM_APPL;

          TomSymbol symbol = ensureValidApplDisjunction(`nameList, expectedType, fileName, decLine, permissive, topLevel);

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
              messageError(fileName,decLine, TomMessage.symbolNumberArgument,
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

        rec@RecordAppl[option=options,nameList=nameList,slots=slotList] -> {
          if(permissive) {
            // Record are not allowed in a rhs
            messageError(findOriginTrackingFileName(`options),findOriginTrackingLine(`options), TomMessage.incorrectRuleRHSClass,
                         new Object[]{getName(`rec)+"[...]"});
          }
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          termClass = RECORD_APPL;

          TomSymbol symbol = ensureValidRecordDisjunction(`nameList, expectedType, fileName, decLine, true);
          if(symbol == null) {
            break matchblock;
          }

          boolean first = true;
          %match(NameList nameList) {
            /* 
             * We perform tests as we have different RecordAppls: 
             * they all must be valid and have the expected return type
             */
            (_*, Name(name), _*) -> {
              verifyRecordStructure(`options, `name, `slotList, fileName,decLine);
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
          fileName = findOriginTrackingFileName(`options);
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
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = "_";
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass,
                         new Object[]{termName});
          }
          break matchblock;
        }

        Variable[option=options, astName=Name(name)] -> {
          termClass = VARIABLE;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = `name;
          break matchblock;
        }

        VariableStar[option=options, astName=Name(name)] -> {
          termClass = VARIABLE_STAR;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = `name+"*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument,
                         new Object[]{termName});
          }
          break matchblock;
        }

        UnamedVariableStar[option=options] -> {
          termClass = UNAMED_VARIABLE_STAR;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = "_*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument,
                         new Object[]{termName});
          }
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass,
                         new Object[]{termName});
          }
          break matchblock;
        }
      }
      System.out.println("Strange term in pattern "+term);
      throw new TomRuntimeException("Strange Term "+term);
    }
    return new TermDescription(termClass, termName, fileName,decLine, type);
  }

  private  void validateTermThrough(TomTerm term, boolean permissive) {
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

  public  TermDescription analyseTerm(TomTerm term) {
    matchblock:{
      %match(TomTerm term) {
        TermAppl[option=options, nameList=(Name(str))] -> {
          if (`str.equals("")) {
            return new TermDescription(UNAMED_APPL, `str,
                findOriginTrackingFileName(`options),
                findOriginTrackingLine(`options), 
                null);
              // TODO
          } else {
            return new TermDescription(TERM_APPL, `str,
                findOriginTrackingFileName(`options),
                findOriginTrackingLine(`options),
                getSymbolCodomain(getSymbolFromName(`str)));
          }
        }
        TermAppl[option=options, nameList=(Name(name), _*)] -> {
          return new TermDescription(APPL_DISJUNCTION, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name))] ->{
          return new TermDescription(RECORD_APPL, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name), _*)] ->{
          return new TermDescription(RECORD_APPL_DISJUNCTION,`name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        XMLAppl[option=options] -> {
          return new TermDescription(XML_APPL, Constants.ELEMENT_NODE,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE)));
        }
        Placeholder[option=options] -> {
          return new TermDescription(PLACE_HOLDER, "_",
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        Variable[option=options, astName=Name(name)] -> {
          return new TermDescription(VARIABLE, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        VariableStar[option=options, astName=Name(name)] -> {
          return new TermDescription(VARIABLE_STAR, `name+"*",
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        UnamedVariableStar[option=options] -> {
          return new TermDescription(UNAMED_VARIABLE_STAR, "_*",
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
      }
      System.out.println("Strange term "+term);
      throw new TomRuntimeException("Strange Term "+term);
    }
  }

  private  TomSymbol ensureValidUnamedList(TomType expectedType, String fileName,int decLine) {
    SymbolList symbolList = symbolTable().getSymbolFromType(expectedType);
    SymbolList filteredList = `emptySymbolList();
    %match(SymbolList symbolList) {
      (_*, symbol , _*) -> {
        if(isArrayOperator(`symbol) || isListOperator(`symbol)) {
          filteredList = `manySymbolList(symbol,filteredList);
        }
      }
    }

    if(filteredList.isEmpty()) {
      messageError(fileName,decLine,
                   TomMessage.unknownUnamedList,
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTail().isEmpty()) {
      StringBuffer symbolsString = new StringBuffer();
      while(!filteredList.isEmpty()) {
        symbolsString .append(" " + filteredList.getHead().getAstName().getString());
        filteredList= filteredList.getTail();
      }
      messageError(fileName,decLine,
                   TomMessage.ambigousUnamedList,
                   new Object[]{expectedType.getString(), symbolsString.toString()});
      return null;
    } else {
      return filteredList.getHead();
    }
  }

  private  TomSymbol ensureValidApplDisjunction(NameList nameList, TomType expectedType, String fileName, int decLine,
                                               boolean permissive, boolean topLevel) {
    TomTypeList domainReference = null, currentDomain = null;
    TomSymbol symbol = null;

    if(nameList.isSingle()) { // Valid but has it a good type?
      String res = nameList.getHead().getString();
      symbol  =  getSymbolFromName(res);
      if (symbol == null ) {
        // this correspond to a term like 'unknown()' or unknown(s1, s2, ...)
        if(!permissive) {
          messageError(fileName,decLine,
              TomMessage.unknownSymbol,
              new Object[]{res});
        } else {
          messageWarning(fileName,decLine,
              TomMessage.unknownPermissiveSymbol,
              new Object[]{res});
        }
      } else { //known symbol
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
            return null;
          }
        }
      }
      return symbol;
    }
      //  this is a disjunction
    if(permissive) {
      messageError(fileName,decLine,
                   TomMessage.impossiblePermissiveAndDisjunction,
                   new Object[]{});
    }

    // this part is common between Appl and records with multiple head symbols
    boolean first = true; // the first symbol give the expected type
    %match(NameList nameList) {
      (_*, Name(dijName), _*) -> { // for each SymbolName
        symbol =  getSymbolFromName(`dijName);
        if (symbol == null) {
            // In disjunction we can only have known symbols
          messageError(fileName,decLine,
                       TomMessage.unknownSymbolInDisjunction,
                       new Object[]{`(dijName)});
          return null;
        }
        if ( strictType  || !topLevel ) {
            // ensure codomain is correct
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, fileName,decLine)) {
            return null;
          }
        }
        currentDomain = getSymbolDomain(symbol);
        if (first) { // save Domain reference
          domainReference = currentDomain;
        } else {
          first = false;
          if(currentDomain != domainReference) {
            messageError(fileName,decLine,
                         TomMessage.invalidDisjunctionDomain,
                         new Object[]{`(dijName)});
            return null;
          }
        }
      }
    }
    return symbol;
  }

  private  boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, String fileName,int decLine) {
    if(currentCodomain != expectedType) {
      //System.out.println(currentCodomain+"!="+expectedType);
      messageError(fileName,decLine,
                   msg,
                   new Object[]{symbolName, currentCodomain.getString(), expectedType.getString()});
      return false;
    }
    return true;
  }

  private  TomSymbol ensureValidRecordDisjunction(NameList nameList, TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(nameList.isSingle()) { // Valid but has it a good type?
      String res = nameList.getHead().getString();
      TomSymbol symbol =  getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
          // it is not correct to use Record with unknown symbols
        messageError(fileName,decLine,
                     TomMessage.unknownSymbol,
                     new Object[]{res});
        return null;
      } else { // known symbol
          // ensure type correctness if necessary
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      return ensureValidApplDisjunction(nameList, expectedType, fileName, decLine, false, topLevel);
    }
  }

  ///////////////////////
  // RECORDS CONCERNS ///
  ///////////////////////
  private  void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, String fileName, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
        // constants have an emptyPairNameDeclList
        // the length of the pairNameDeclList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotList.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)) ) {
        messageError(fileName,decLine,
                     TomMessage.bracketOnListSymbol,
                     new Object[]{tomName});
      }
        // TODO verify type
      verifyRecordSlots(slotList,symbol, getSymbolDomain(symbol), tomName, fileName, decLine);
    } else {
      messageError(fileName,decLine,
                   TomMessage.unknownSymbol,
                   new Object[]{tomName});
    }
  }

    // We test the existence/repetition of slotName contained in pairSlotAppl
    // and then the associated term
  private  void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, String fileName, int decLine) {
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
        messageError(fileName,decLine,
                     TomMessage.badSlotName,
                     new Object[]{pairSlotName.getString(), methodName, listOfPossibleSlot.toString()});
        return; //break analyses
      } else { // then check for repeated good slot name
        Integer integerIndex = new Integer(index);
        if(studiedSlotIndexList.contains(integerIndex)) {
            // Error: repeated slot
          messageError(fileName,decLine,
                       TomMessage.slotRepeated,
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

  public  void validateListOperatorArgs(TomList args, TomType expectedType, boolean permissive) {
    while(!args.isEmpty()) {
      validateTerm(args.getHead(), expectedType, true, false, permissive);
      args = args.getTail();
    }
  }

  private  boolean testTypeExistence(String typeName) {
    return symbolTable().getType(typeName) != null;
  }

  protected static class TermDescription {
    private int termClass;
    private String fileName;
    private int decLine;
    private String name ="";
    private TomType tomType = null;

    public TermDescription(int termClass, String name, String fileName, int decLine, TomType tomType) {
      this.termClass = termClass;
      this.fileName = fileName;
      this.decLine = decLine;
      this.name = name;
      this.tomType = tomType;
    }

    public int getTermClass() {
      return termClass;
    }

    public String getName() {
      return name;
    }
    
    public String getFileName() {
      return fileName;
    }

    public int getLine() {
      return decLine;
    }

    public TomType getType() {
      if(tomType != null && !tomType.isEmptyType()) {
        return tomType;
      } else {
        return `EmptyType();
      }
    }
  }
}
