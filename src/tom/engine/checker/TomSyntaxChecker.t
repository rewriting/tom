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

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

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
      Strategy[VisitList = list,OrgTrack=origin] -> {
        if(`list.isEmptyconcTomVisit()) {
          %match(Option `origin) {
            OriginTracking[FileName=fileName,Line=line] -> { 
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
            TermsEqualDecl(Variable[AstName=Name(name1)],Variable[AstName=Name(name2)],_, orgTrack) -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.EQUALS,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            // List specific Macro functions
            GetHeadDecl[OrgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_HEAD,verifyList,orgTrack, declType);
              break matchblock;
            }
            GetTailDecl[OrgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.GET_TAIL,verifyList,orgTrack, declType);
              break matchblock;
            }
            IsEmptyDecl[OrgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.IS_EMPTY,verifyList,orgTrack, declType);
              break matchblock;
            }
            // Array specific Macro functions
            GetElementDecl[Variable=Variable[AstName=Name(name1)],Index=Variable[AstName=Name(name2)],OrgTrack=orgTrack] -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
              break matchblock;
            }
            GetSizeDecl[OrgTrack=orgTrack] -> {
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

  private  void verifySymbolMacroFunctions(OptionList option, int domainLength, String symbolType) {
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

    %match(OptionList option) {
      (_*, DeclarationToOption(d), _*) -> { // for each Declaration
        Declaration decl=`d;
        matchblock:{
          %match(Declaration decl ) {
            // for a array symbol
            MakeEmptyArray[OrgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList,orgTrack, symbolType);
              break matchblock;
            }
            MakeAddArray[VarList=Variable[AstName=Name(name1)], VarElt=Variable[AstName=Name(name2)], OrgTrack=orgTrack] -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_APPEND, verifyList, orgTrack, name1, name2, symbolType);
              break matchblock;
            }
            // for a List symbol
            MakeEmptyList[OrgTrack=orgTrack] -> {
              `checkField(TomSyntaxChecker.MAKE_EMPTY,verifyList,orgTrack, symbolType);
              break matchblock;
            }
            MakeAddList[VarList=Variable[AstName=Name(name1)], VarElt=Variable[AstName=Name(name2)], OrgTrack=orgTrack] -> {
              `checkFieldAndLinearArgs(TomSyntaxChecker.MAKE_INSERT, verifyList, orgTrack, name1, name2, symbolType);
              break matchblock;
            }
            // for a symbol
            MakeDecl[Args=makeArgsList, OrgTrack=og@OriginTracking[FileName=fileName,Line=line]] -> {
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
      (_*, Variable[AstName=Name(name)] ,_*) -> { // for each Macro variable
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
      (_*, PairNameDecl[SlotName=Name(name)], _*) -> { // for each Slot
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
  private void verifyMatch(TomList subjectList, PatternInstructionList patternInstructionList, OptionList option) {
    currentTomStructureOrgTrack = findOriginTracking(option);
    ArrayList typeMatchArgs = new ArrayList();
    ArrayList subjectMatchArgs = new ArrayList();
    int nbExpectedArgs = 0;
    // From the subjects list(match definition), we test each used type and keep them in memory
    while(!subjectList.isEmptyconcTomTerm()) {
      TomTerm subject = subjectList.getHeadconcTomTerm();
      // for each Match args
      %match(TomTerm `subject) {
        Variable[AstName=Name(name),AstType=tomType@TomTypeAlone(type)] -> {
	  nbExpectedArgs++;
	  subjectMatchArgs.add(`name);
          if(`type.equals("unknown type")) {
            typeMatchArgs.add(null);
          } else if(testTypeExistence(`type)) {
            typeMatchArgs.add(`tomType);
          } else {
            messageError(currentTomStructureOrgTrack.getFileName(),
                currentTomStructureOrgTrack.getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                new Object[]{`name, `(type)});
            typeMatchArgs.add(null);
          }
        }

        term@TermAppl[NameList=concTomName(Name(name))] -> {
	  nbExpectedArgs++;
	  subjectMatchArgs.add(`name);
	  TomSymbol symbol = getSymbolFromName(`name);
	  if(symbol!=null) {
	    TomType type = getSymbolCodomain(symbol);
	    typeMatchArgs.add(type);
            validateTerm(`term, type, false, true, true);
	  } else {
	    typeMatchArgs.add(null);
	  }
        }
      }
      subjectList = subjectList.getTailconcTomTerm();
    }

    /*
     * if a type is not specified in the subjectList
     * we look for a type in a column and we update typeMatchArgs
     */
    for(int i=0 ; i<typeMatchArgs.size() ; i++) {
      //System.out.println("i = " + i);
block: {
         if(typeMatchArgs.get(i) == null) {
           %match(PatternInstructionList patternInstructionList) {
             concPatternInstruction(_*, PatternInstruction[
                 Pattern=Pattern[TomList=concTomTerm(X*,(TermAppl|RecordAppl|ListAppl)[NameList=concTomName(Name(name),_*)],_*)]], _*) -> {
               //System.out.println("X.length = " + `X*.length());
               if(`X*.length() == i) {
                 TomSymbol symbol = getSymbolFromName(`name);
                 //System.out.println("name = " + `name);
                 if(symbol!=null) {
                   TomType type = getSymbolCodomain(symbol);
                   //System.out.println("type = " + type);
                   typeMatchArgs.set(i,type);
                   break block;
                 }
               }
             }
           }
         }
       }
       if(typeMatchArgs.get(i) == null) {
            messageError(currentTomStructureOrgTrack.getFileName(),
                currentTomStructureOrgTrack.getLine(),
                TomMessage.cannotGuessMatchType,
                new Object[]{subjectMatchArgs.get(i)});
       }
    }

    // we now compare pattern to its definition
    %match(PatternInstructionList patternInstructionList) {
      concPatternInstruction(_*, PatternInstruction[Pattern=Pattern[TomList=terms,Guards=guards]], _*) -> {
        // control each pattern vs the match definition
        `verifyMatchPattern(terms, typeMatchArgs, nbExpectedArgs);
        `verifyWhenPattern(guards);
      }
    }
  }

  // each patternList shall have the expected length and each term shall be valid
  private  void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    int nbFoundArgs = termList.length();
    if(nbFoundArgs != nbExpectedArgs) {
      OptionList og = termList.getHeadconcTomTerm().getOption();
      messageError(findOriginTrackingFileName(og),findOriginTrackingLine(og),
                   TomMessage.badMatchNumberArgument,
                   new Object[]{new Integer(nbExpectedArgs), new Integer(nbFoundArgs)});
      // we cannot continue because we will use the fact that each element of the pattern
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
    while(!visitList.isEmptyconcTomVisit()) {
      TomVisit visit = visitList.getHeadconcTomVisit();
      verifyVisit(visit);
      // next visit
      visitList = visitList.getTailconcTomVisit();
    }
  }

  private  void verifyVisit(TomVisit visit){
    %match(TomVisit visit) {
      VisitTerm(type,patternInstructionList,_) -> {
        ArrayList typeMatchArgs = new ArrayList();
        typeMatchArgs.add(`type);
        // we now compare pattern to its definition
        %match(PatternInstructionList patternInstructionList) {
          concPatternInstruction(_*, PatternInstruction[Pattern=Pattern[TomList=terms,Guards=guards]], _*) -> {
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

  private static boolean findMakeDecl(OptionList option) {
    %match(OptionList option) {
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
        TermAppl[Option=options, NameList=(Name("")), Args=args] -> {
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
            validateListOperatorArgs(`args, symbol.getTypesToType().getDomain().getHeadconcTomType(),permissive);
            if(permissive) { System.out.println("UnamedList but permissive");}
            break matchblock;
          }
        }

        TermAppl[Option=options, NameList=symbolNameList, Args=arguments] -> {
          TomList args = `arguments;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          termClass = TERM_APPL;

          TomSymbol symbol = ensureValidApplDisjunction(`symbolNameList, expectedType, fileName, decLine, permissive, topLevel);

          if(symbol == null) {
            validateTermThrough(term,permissive);
            break matchblock;
          }
            // Type is OK
          type = expectedType;
          termName = `symbolNameList.getHeadconcTomName().getString();
          boolean listOp = (isListOperator(symbol) || isArrayOperator(symbol));
          if(listOp) {
              // whatever the arity is, we continue recursively and there is only one element in the Domain
            validateListOperatorArgs(args, symbol.getTypesToType().getDomain().getHeadconcTomType(),permissive);
          } else {
            // the arity is important also there are different types in Domain
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbArgs = args.length();
            int nbExpectedArgs = types.length();
            if(nbArgs != nbExpectedArgs) {
              messageError(fileName,decLine, TomMessage.symbolNumberArgument,
                  new Object[]{termName, new Integer(nbExpectedArgs), new Integer(nbArgs)});
              break matchblock;
            }
            while(!args.isEmptyconcTomTerm()) {
                // repeat analyse with associated expected type and control arity
              validateTerm(args.getHeadconcTomTerm(), types.getHeadconcTomType(), listOp/*false*/, false, permissive);
              args = args.getTailconcTomTerm();
              types = types.getTailconcTomType();
            }
          }
          break matchblock;
        }

        rec@RecordAppl[Option=options,NameList=symbolNameList,Slots=slotList] -> {
          if(permissive) {
            // Record are not allowed in a rhs
            messageError(findOriginTrackingFileName(`options),findOriginTrackingLine(`options), TomMessage.incorrectRuleRHSClass, new Object[]{getName(`rec)+"[...]"});
          }
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          termClass = RECORD_APPL;

          TomSymbol symbol = ensureValidRecordDisjunction(`symbolNameList, `slotList, expectedType, fileName, decLine, true);
          if(symbol == null) {
            break matchblock;
          }

          %match(TomNameList symbolNameList) {
            /* 
             * We perform tests as we have different RecordAppls: 
             * they all must be valid and have the expected return type
             */
            (_*, Name(name), _*) -> {
              verifyRecordStructure(`options, `name, `slotList, fileName,decLine);
            }
          }

          type = expectedType;
          termName = `symbolNameList.getHeadconcTomName().getString();
          break matchblock;
        }

        XMLAppl[Option=options, NameList=(_*, Name(_), _*), ChildList=childList] -> {
            // TODO: can we do it
            // ensureValidDisjunction(symbolNameList); ??????????
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
          while(!args.isEmptyconcTomTerm()) {
            // repeat analyse with associated expected type and control arity
            validateTerm(args.getHeadconcTomTerm(), TNodeType, true, false, permissive);
            args = args.getTailconcTomTerm();
          }

          break matchblock;
        }

        Variable[Option=options, AstName=Name(name)] -> {
          termClass = VARIABLE;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = `name;
          break matchblock;
        }

        VariableStar[Option=options, AstName=Name(name)] -> {
          termClass = VARIABLE_STAR;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = `name+"*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument, new Object[]{termName});
          }
          break matchblock;
        }

        UnamedVariable[Option=options] -> {
          termClass = UNAMED_VARIABLE;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = "_";
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass, new Object[]{termName});
          }
          break matchblock;
        }

        UnamedVariableStar[Option=options] -> {
          termClass = UNAMED_VARIABLE_STAR;
          fileName = findOriginTrackingFileName(`options);
          decLine = findOriginTrackingLine(`options);
          type = null;
          termName = "_*";
          if(!listSymbol) {
            messageError(fileName,decLine, TomMessage.invalidVariableStarArgument, new Object[]{termName});
          }
          if(permissive) {
            messageError(fileName,decLine, TomMessage.incorrectRuleRHSClass, new Object[]{termName});
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
      TermAppl[Args=arguments] -> {
        TomList args = `arguments;
        while(!args.isEmptyconcTomTerm()) {
          TomTerm child = args.getHeadconcTomTerm();
          TomSymbol sym = getSymbolFromName(getName(child));
          if(sym != null) {
            validateTerm(child,sym.getTypesToType().getCodomain(),false,false,permissive);
          } else {
            validateTermThrough(child,permissive);
          }
          args = args.getTailconcTomTerm();
        }
      }
    }
  }

  public TermDescription analyseTerm(TomTerm term) {
    matchblock:{
      %match(TomTerm term) {
        TermAppl[Option=options, NameList=(Name(str))] -> {
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
        TermAppl[Option=options, NameList=(Name(name), _*)] -> {
          return new TermDescription(APPL_DISJUNCTION, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[Option=options,NameList=(Name(name))] ->{
          return new TermDescription(RECORD_APPL, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        RecordAppl[Option=options,NameList=(Name(name), _*)] ->{
          return new TermDescription(RECORD_APPL_DISJUNCTION,`name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(`name)));
        }
        XMLAppl[Option=options] -> {
          return new TermDescription(XML_APPL, Constants.ELEMENT_NODE,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),
              getSymbolCodomain(getSymbolFromName(Constants.ELEMENT_NODE)));
        }
        Variable[Option=options, AstName=Name(name)] -> {
          return new TermDescription(VARIABLE, `name,
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        VariableStar[Option=options, AstName=Name(name)] -> {
          return new TermDescription(VARIABLE_STAR, `name+"*",
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        UnamedVariable[Option=options] -> {
          return new TermDescription(UNAMED_VARIABLE, "_",
                findOriginTrackingFileName(`options),
              findOriginTrackingLine(`options),  null);
        }
        UnamedVariableStar[Option=options] -> {
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
    TomSymbolList symbolList = symbolTable().getSymbolFromType(expectedType);
    TomSymbolList filteredList = `concTomSymbol();
    %match(TomSymbolList symbolList) {
      (_*, symbol , _*) -> {
        if(isArrayOperator(`symbol) || isListOperator(`symbol)) {
          filteredList = `concTomSymbol(symbol,filteredList*);
        }
      }
    }

    if(filteredList.isEmptyconcTomSymbol()) {
      messageError(fileName,decLine,
                   TomMessage.unknownUnamedList,
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTailconcTomSymbol().isEmptyconcTomSymbol()) {
      StringBuffer symbolsString = new StringBuffer();
      while(!filteredList.isEmptyconcTomSymbol()) {
        symbolsString .append(" " + filteredList.getHeadconcTomSymbol().getAstName().getString());
        filteredList= filteredList.getTailconcTomSymbol();
      }
      messageError(fileName,decLine,
                   TomMessage.ambigousUnamedList,
                   new Object[]{expectedType.getString(), symbolsString.toString()});
      return null;
    } else {
      return filteredList.getHeadconcTomSymbol();
    }
  }

  private TomSymbol ensureValidApplDisjunction(TomNameList symbolNameList, TomType expectedType, 
      String fileName, int decLine, boolean permissive, boolean topLevel) {

    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);
      if (symbol == null ) {
        // this correspond to a term like 'unknown()' or unknown(s1, s2, ...)
        if(!permissive) {
          messageError(fileName,decLine, TomMessage.unknownSymbol, new Object[]{res});
        } else {
          messageWarning(fileName,decLine, TomMessage.unknownPermissiveSymbol, new Object[]{res});
        }
      } else { //known symbol
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      //  this is a disjunction
      if(permissive) {
	messageError(fileName,decLine, TomMessage.impossiblePermissiveAndDisjunction, new Object[]{});
      }

      TomSymbol symbol = null;
      TomTypeList domainReference = null;
      String nameReference = null;
      %match(TomNameList symbolNameList) {
	(_*, Name(dijName), _*) -> { // for each SymbolName
	  symbol =  getSymbolFromName(`dijName);
	  if (symbol == null) {
	    // In disjunction we can only have known symbols
	    messageError(fileName,decLine, TomMessage.unknownSymbolInDisjunction, new Object[]{`(dijName)});
	    return null;
	  }
	  if ( strictType  || !topLevel ) {
	    // ensure codomain is correct
	    if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, fileName,decLine)) {
	      return null;
	    }
	  }

	  if (domainReference == null) { // save Domain reference
	    domainReference = getSymbolDomain(symbol);
	    nameReference = `dijName;
	  } else {
	    if(getSymbolDomain(symbol) != domainReference) {
	      messageError(fileName,decLine, TomMessage.invalidDisjunctionDomain, new Object[]{nameReference, `(dijName) });
	      return null;
	    }
	  }
	}
      }
      return symbol;
    }
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

  private  TomSymbol ensureValidRecordDisjunction(TomNameList symbolNameList, SlotList slotList, 
      TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol =  getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
          // it is not correct to use Record with unknown symbols
        messageError(fileName,decLine, TomMessage.unknownSymbol, new Object[]{res});
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
      TomSymbol symbol = null;
      TomTypeList referenceDomain = null;
      String referenceName = null;
      %match(TomNameList symbolNameList) {
	(_*, Name(dijName), _*) -> { // for each SymbolName
	  symbol =  getSymbolFromName(`dijName);
	  if (symbol == null) {
	    // In disjunction we can only have known symbols
	    messageError(fileName,decLine, TomMessage.unknownSymbolInDisjunction, new Object[]{`(dijName)});
	    return null;
	  }
	  if ( strictType  || !topLevel ) {
	    // ensure codomain is correct
	    if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, fileName,decLine)) {
	      return null;
	    }
	  }
	  //System.out.println("domain = " + getSymbolDomain(symbol));

	  if (referenceDomain == null) { // save Domain reference
	    referenceName = `dijName;
	    referenceDomain = getSymbolDomain(symbol);

	  } else {
	    // check that domains are compatible
	    TomTypeList currentDomain = getSymbolDomain(symbol);
	    // restrict the domain to the record
	    while(!slotList.isEmptyconcSlot()) {
	      Slot slot = slotList.getHeadconcSlot();
	      TomName slotName = slot.getSlotName();
	      int slotIndex = TomBase.getSlotIndex(symbol,slotName);

	      //System.out.println("type1 = " + TomBase.elementAt(currentDomain,slotIndex));
	      //System.out.println("type2 = " + TomBase.elementAt(referenceDomain,slotIndex));
	      if(TomBase.elementAt(currentDomain,slotIndex) != TomBase.elementAt(referenceDomain,slotIndex)) {
		messageError(fileName,decLine, TomMessage.invalidDisjunctionDomain, new Object[]{referenceName, `(dijName) });
		return null;
	      }

	      slotList = slotList.getTailconcSlot();
	    }

	  }
	}
      }
      return symbol;
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
      if(slotList.isEmptyconcSlot() && (isListOperator(symbol) ||  isArrayOperator(symbol)) ) {
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
  while( !slotList.isEmptyconcSlot() ) {
      pairSlotName = slotList.getHeadconcSlot().getSlotName();
        // First check for slot name correctness
      int index = getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
          listOfPossibleSlot = new ArrayList();
          PairNameDeclList listOfSlots = tomSymbol.getPairNameDeclList();
          while ( !listOfSlots.isEmptyconcPairNameDecl() ) {
            TomName sname = listOfSlots.getHeadconcPairNameDecl().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTailconcPairNameDecl();
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
      while(!listOfSlots.isEmptyconcPairNameDecl()) {
        SlotList listOfPair = slotList;
        TomName slotName = listOfSlots.getHeadconcPairNameDecl().getSlotName();
        TomType expectedType = listOfTypes.getHeadconcTomType();
        if(!slotName.isEmptyName()) {
          // look for a same name (from record)
          whileBlock: {
            while(!listOfPair.isEmptyconcSlot()) {
              Slot pairSlotTerm = listOfPair.getHeadconcSlot();
              %match(TomName slotName, Slot pairSlotTerm) {
                Name[String=name1], PairSlotAppl(Name[String=name1],slotSubterm) -> {
                   validateTerm(`slotSubterm ,expectedType, false, true, false);
                   break whileBlock;
                 }
                _ , _ -> {listOfPair = listOfPair.getTailconcSlot();}
              }
            }
          }
        }
        // prepare next step
        listOfSlots = listOfSlots.getTailconcPairNameDecl();
        listOfTypes = listOfTypes.getTailconcTomType();
      }

      slotList = slotList.getTailconcSlot();
    }
  }

  public  void validateListOperatorArgs(TomList args, TomType expectedType, boolean permissive) {
    while(!args.isEmptyconcTomTerm()) {
      validateTerm(args.getHeadconcTomTerm(), expectedType, true, false, permissive);
      args = args.getTailconcTomTerm();
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
