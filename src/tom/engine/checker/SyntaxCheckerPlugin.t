
/*
 *
 * TOM - To One Matching Compiler
 *
 * Copyright (c) 2000-2017, Inria
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
 **/

package tom.engine.checker;

import java.util.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.exception.TomRuntimeException;

import tom.platform.adt.platformoption.types.PlatformOptionList;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.SymbolTable;
import tom.engine.tools.TomConstraintPrettyPrinter;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomterm.types.tomterm.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.theory.types.*;
import tom.engine.adt.code.types.*;


import tom.library.sl.*;

/**
 * The syntax checker plugin.
 */
public class SyntaxCheckerPlugin extends TomGenericPlugin {

  %include { ../adt/tomsignature/TomSignature.tom }
  %include { ../../platform/adt/platformoption/PlatformOption.tom }
  %include { ../../library/mapping/java/sl.tom }
  %include { ../../library/mapping/java/util/types/Collection.tom}
  %include { ../../library/mapping/java/util/types/List.tom}
  %include { ../../library/mapping/java/util/types/Map.tom}

  %typeterm SyntaxCheckerPlugin { implement { SyntaxCheckerPlugin } }

  // Different kind of structures
  protected final static int TERM_APPL               = 0;
  protected final static int APPL_DISJUNCTION        = 2;
  protected final static int RECORD_APPL             = 3;
  protected final static int RECORD_APPL_DISJUNCTION = 4;
  protected final static int VARIABLE_STAR           = 6;
  protected final static int VARIABLE                = 7;
  protected final static int UNAMED_VARIABLE         = 8;
  protected final static int UNAMED_VARIABLE_STAR    = 9;

  private Option currentTomStructureOrgTrack;

  /** the declared options string */
  public static final PlatformOptionList PLATFORM_OPTIONS =
    `concPlatformOption(
        PluginOption("noSyntaxCheck", "", "Do not perform syntax checking", BooleanValue(False()), "")
        );
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
  private  Collection<String> alreadyStudiedTypes =  null;
  /** the list of already studied and declared Symbol */
  private  Collection<String> alreadyStudiedSymbols =  null;

  /** List of expected functional declaration in each type declaration */
  private final static List<String> TypeTermSignature =
    new ArrayList<String>(Arrays.asList(new String[]{ SyntaxCheckerPlugin.EQUALS }));

  /** Constructor */
  public SyntaxCheckerPlugin() {
    super("SyntaxCheckerPlugin");
    reinit();
  }

  protected Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

  private Option getCurrentTomStructureOrgTrack() {
    return currentTomStructureOrgTrack;
  }

  private void setCurrentTomStructureOrgTrack(Option currentTomStructureOrgTrack) {
    this.currentTomStructureOrgTrack = currentTomStructureOrgTrack;
  }

  /**
   * inherited from OptionOwner interface (plugin)
   */
  public PlatformOptionList getDeclaredOptionList() {
    return PLATFORM_OPTIONS; 
  }

  protected void reinit() {
    currentTomStructureOrgTrack = null;
    alreadyStudiedTypes   = new HashSet<String>();
    alreadyStudiedSymbols = new HashSet<String>();
  }
  
  /**
   * Shared Functions 
   */

  protected String findOriginTrackingFileName(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[FileName=fileName],_*) -> { return `fileName; }
    }
    return "unknown filename";
  }

  protected int findOriginTrackingLine(OptionList optionList) {
    %match(optionList) {
      concOption(_*,OriginTracking[Line=line],_*) -> { return `line; }
    }
    return -1;
  }

  public void run(Map informationTracker) {
    //System.out.println("(debug) I'm in the Tom SyntaxChecker : TSM"+getStreamManager().toString());
    if(isActivated()) {
      long startChrono = System.currentTimeMillis();
      try {
        // clean up internals
        reinit();
        // perform analyse
        try {
          Code code = (Code)getWorkingTerm();
          //System.out.println("code = " + code);
          `TopDownCollect(CheckSyntax(this)).visitLight(code);
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("strategy failed");
        }
        // verbose
        TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
            TomMessage.tomSyntaxCheckingPhase,
            Integer.valueOf((int)(System.currentTimeMillis()-startChrono)));
      } catch (Exception e) {
        TomMessage.error(getLogger(), 
            getStreamManager().getInputFileName(), 0,
            TomMessage.exceptionMessage,
            getClass().getName(), 
            getStreamManager().getInputFileName(),
            e.getMessage() );
        e.printStackTrace();
      }
    } else {
      // syntax checker desactivated
      TomMessage.info(getLogger(), getStreamManager().getInputFileName(), 0,
          TomMessage.syntaxCheckerInactivated);
    }
  }

  private boolean isActivated() {
    return !getOptionBooleanValue("noSyntaxCheck");
  }

  /**
   * Syntax checking entry point: Catch and verify all type and operator
   * declaration, Match instruction
   */
  %strategy CheckSyntax(scp:SyntaxCheckerPlugin) extends Identity() {
    visit Declaration {
      /*
       * %strategy
       * error if there is no visit
       */
      s@Strategy[VisitList=list, OrgTrack=origin] -> {
        if(`list.isEmptyconcTomVisit()) {
          %match(origin) {
            OriginTracking[FileName=fileName,Line=line] -> { 
              TomMessage.error(scp.getLogger(), `fileName, `line, TomMessage.emptyStrategy);
              return `s;
            }
          }
          TomMessage.error(scp.getLogger(), null, -1, TomMessage.emptyStrategy);
          return `s;
        }
        /* STRATEGY MATCH STRUCTURE */
        scp.verifyStrategy(`list);
        throw new tom.library.sl.VisitFailure();/* stop the top-down */
      }
      /*
       * %typeterm
       */
      TypeTermDecl(Name(tomName), declarationList, orgTrack) -> {
        scp.verifyTypeDecl(SyntaxCheckerPlugin.TYPE_TERM, `tomName, `declarationList, `orgTrack);
        throw new tom.library.sl.VisitFailure();/* stop the top-down */
      }
      // Symbols
      SymbolDecl(Name(tomName)) -> {
        scp.verifySymbol(SyntaxCheckerPlugin.CONSTRUCTOR, scp.getSymbolFromName(`tomName));
        throw new tom.library.sl.VisitFailure();/* stop the top-down */
      }
      ArraySymbolDecl(Name(tomName)) -> {
        scp.verifySymbol(SyntaxCheckerPlugin.OP_ARRAY, scp.getSymbolFromName(`tomName));
        throw new tom.library.sl.VisitFailure();/* stop the top-down */
      }
      ListSymbolDecl(Name(tomName))  -> {
        scp.verifySymbol(SyntaxCheckerPlugin.OP_LIST, scp.getSymbolFromName(`tomName));
        throw new tom.library.sl.VisitFailure();/* stop the top-down */
      }
      /*
       * %transformation
       */
//      t@Transformation[TName=name,WithToList=wtlist,OrgTrack=ot] -> {
//        /*test if a %transformtion is empty (not valid)*/
//        if(`wtlist.isEmptyconcTomWithTo()) {
//          %match(ot) {
//            OriginTracking[FileName=fileName,Line=line] -> { 
//              TomMessage.error(scp.getLogger(), `fileName, `line, TomMessage.emptyTransformation);
//              return `t;
//            }
//          }
//          TomMessage.error(scp.getLogger(), null, -1, TomMessage.emptyTransformation);
//          return `t;
//        }
//        //TODO : verify transformation structure?
//        //scp.verifyTransformation(`wtlist);
//        throw new tom.library.sl.VisitFailure();/* stop the top-down*/
//      }

    }

    visit Instruction {
      Match(constraintInstructionList, optionList) -> {
        /* TOM MATCH STRUCTURE */
        scp.verifyMatch(`constraintInstructionList, `optionList);
      }
    }

    visit BQTerm {
      subject@BQAppl[] -> {
        scp.verifyBQAppl(`subject);
      }
    }
  }

  // /////////////////////////////
  // TYPE DECLARATION CONCERNS //
  // ////////////////////////////
  private void verifyTypeDecl(String declType, String tomName, DeclarationList listOfDeclaration, Option typeOrgTrack) {
    setCurrentTomStructureOrgTrack(typeOrgTrack);
    // ensure first definition
    verifyMultipleDefinition(tomName, declType, TYPE);
    // verify Macro functions
    List<String> verifyList = new ArrayList<String>(SyntaxCheckerPlugin.TypeTermSignature);


    %match(DeclarationList listOfDeclaration) {
      concDeclaration(_*, d, _*) -> { // for each Declaration
matchblock:{
             %match(d) {
               // Common Macro functions
               EqualTermDecl(BQVariable[AstName=Name(name1)],BQVariable[AstName=Name(name2)],_, orgTrack) -> {
                 `checkFieldAndLinearArgs(SyntaxCheckerPlugin.EQUALS,verifyList,orgTrack,name1,name2, declType);
                 break matchblock;
               }
               // List specific Macro functions
               GetHeadDecl[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.GET_HEAD,verifyList,orgTrack, declType);
                 break matchblock;
               }
               GetTailDecl[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.GET_TAIL,verifyList,orgTrack, declType);
                 break matchblock;
               }
               IsEmptyDecl[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.IS_EMPTY,verifyList,orgTrack, declType);
                 break matchblock;
               }
               // Array specific Macro functions
               GetElementDecl[Variable=BQVariable[AstName=Name(name1)],Index=BQVariable[AstName=Name(name2)],OrgTrack=orgTrack] -> {
                 `checkFieldAndLinearArgs(SyntaxCheckerPlugin.GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
                 break matchblock;
               }
               GetSizeDecl[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.GET_SIZE,verifyList,orgTrack, declType);
                 break matchblock;
               }
             }
           }
      }
    }

    // remove non mandatory functions
    if(verifyList.contains(SyntaxCheckerPlugin.EQUALS)) {
      verifyList.remove(verifyList.indexOf(SyntaxCheckerPlugin.EQUALS));
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  } // verifyTypeDecl

  private void verifyMultipleDefinition(String name, String symbolType, String OperatorOrType) {
    List list;
    if(OperatorOrType.equals(SyntaxCheckerPlugin.OPERATOR)) {
      if(alreadyStudiedSymbols.contains(name)) {
        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSymbolDefinitionError, name);
      } else {
        alreadyStudiedSymbols.add(name);
      }
    } else {
      if(alreadyStudiedTypes.contains(name)) {
        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.multipleSortDefinitionError,
            name);
      } else {
        alreadyStudiedTypes.add(name);
      }
    }
  } // verifyMultipleDefinition

  private void checkField(String function, List foundFunctions, Option orgTrack, String symbolType) {
    if(foundFunctions.contains(function)) {
      foundFunctions.remove(foundFunctions.indexOf(function));
    } else {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.macroFunctionRepeated,
          function);
    }
  } // checkField

  private  void checkFieldAndLinearArgs(String function, List foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      TomMessage.error(getLogger(), orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.nonLinearMacroFunction,
          function, name1);
    }
  } // checkFieldAndLinearArgs

  // ///////////////////////////////
  // SYMBOL DECLARATION CONCERNS //
  // ///////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol) {
    int domainLength;

    String symbStrName = tomSymbol.getAstName().getString();

    OptionList optionList = tomSymbol.getOptions();
    // We save first the origin tracking of the symbol declaration
    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));

    // ensure first definition then Codomain, Domain, Macros and Slots (Simple
    // operator)
    verifyMultipleDefinition(symbStrName, symbolType, SyntaxCheckerPlugin.OPERATOR);
    verifySymbolCodomain(TomBase.getSymbolCodomain(tomSymbol), symbStrName, symbolType);
    domainLength = verifySymbolDomain(TomBase.getSymbolDomain(tomSymbol), symbStrName, symbolType);
    verifySymbolMacroFunctions(optionList, domainLength, symbolType);
  } // verifySymbol

  private void verifySymbolCodomain(TomType codomain, String symbName, String symbolType) {
    %match(codomain) {
      Codomain(opName) -> {
        if(getSymbolTable().getSymbolFromName(`opName) == null) {
          TomMessage.error(getLogger(), 
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, codomain);
        }
        return;
      }

      Type(_,typeName,EmptyTargetLanguageType()) -> {
        if(!testTypeExistence(`typeName)) {
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.symbolCodomainError,
              symbName, `typeName);
          if(`typeName.equals("Strategy")) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.missingIncludeSL);
          }
        }
        return;
      }

      EmptyType() -> {
        TomMessage.error(getLogger(),
            getCurrentTomStructureOrgTrack().getFileName(),
            getCurrentTomStructureOrgTrack().getLine(),
            TomMessage.symbolCodomainError,
            symbName, "");
        return;
      }
    }
    throw new TomRuntimeException("Strange codomain " + codomain);
  }

  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) {
      %match(TomTypeList args) {
        concTomType(_*,  Type(_,typeName,EmptyTargetLanguageType()),_*) -> { // for each symbol types
          if(!testTypeExistence(`typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.symbolDomainError,
                Integer.valueOf(position), symbName, `typeName);
          }
          position++;
        }
      }
      return (position-1);
    } else { // OPARRAY and OPLIST
      %match(TomTypeList args) {
        concTomType(Type(_,typeName,EmptyTargetLanguageType())) -> {
          if(!testTypeExistence(`typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.listSymbolDomainError,
                symbName, `typeName);
          }
        }
      } // match
      return 1;
    }
  } // verifySymbolDomain

  private void verifySymbolMacroFunctions(OptionList option, int domainLength, String symbolType) {
    List<String> verifyList = new ArrayList<String>();
    boolean foundOpMake = false;
    if(symbolType.equals(SyntaxCheckerPlugin.CONSTRUCTOR)) { // Nothing absolutely
      // necessary
    } else if(symbolType == SyntaxCheckerPlugin.OP_ARRAY ) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_APPEND);
    } else if(symbolType == SyntaxCheckerPlugin.OP_LIST) {
      verifyList.add(SyntaxCheckerPlugin.MAKE_EMPTY);
      verifyList.add(SyntaxCheckerPlugin.MAKE_INSERT);
    }


    %match(OptionList option) {
      concOption(_*, DeclarationToOption(d), _*) -> { // for each Declaration
matchblock:{
             %match(d) {
               // for a array symbol
               MakeEmptyArray[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList,orgTrack, symbolType);
                 break matchblock;
               }
               MakeAddArray[VarList=BQVariable[AstName=Name(name1)], VarElt=BQVariable[AstName=Name(name2)], OrgTrack=orgTrack] -> {
                 `checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_APPEND, verifyList, orgTrack, name1, name2, symbolType);
                 break matchblock;
               }
               // for a List symbol
               MakeEmptyList[OrgTrack=orgTrack] -> {
                 `checkField(SyntaxCheckerPlugin.MAKE_EMPTY,verifyList,orgTrack, symbolType);
                 break matchblock;
               }
               MakeAddList[VarList=BQVariable[AstName=Name(name1)], VarElt=BQVariable[AstName=Name(name2)], OrgTrack=orgTrack] -> {
                 `checkFieldAndLinearArgs(SyntaxCheckerPlugin.MAKE_INSERT, verifyList, orgTrack, name1, name2, symbolType);
                 break matchblock;
               }
               // for a symbol
               MakeDecl[Args=makeArgsList, OrgTrack=og@OriginTracking[FileName=fileName,Line=line]] -> {
                 if(!foundOpMake) {
                   foundOpMake = true;
                   `verifyMakeDeclArgs(makeArgsList, domainLength, og, symbolType);
                 } else {
                   TomMessage.error(getLogger(), `fileName, `line, 
                       TomMessage.macroFunctionRepeated,
                       SyntaxCheckerPlugin.MAKE);
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
  }  // verifySymbolMacroFunctions

  private void verifyMakeDeclArgs(BQTermList argsList, int domainLength, Option orgTrack, String symbolType) {
    // we test the necessity to use different names for each
    // variable-parameter.
    int nbArgs = 0;
    Collection<String> listVar = new HashSet<String>();
    %match(BQTermList argsList) {
      concBQTerm(_*, BQVariable[AstName=Name(name)] ,_*) -> { // for each Macro variable
        if(listVar.contains(`name)) {
          TomMessage.error(getLogger(),
              orgTrack.getFileName(),orgTrack.getLine(),
              TomMessage.nonLinearMacroFunction,
              SyntaxCheckerPlugin.MAKE, `name);
        } else {
          listVar.add(`name);
        }
        nbArgs++;
      }
    }
    if(nbArgs != domainLength) {
      TomMessage.error(getLogger(),
          orgTrack.getFileName(),
          orgTrack.getLine(),
          TomMessage.badMakeDefinition,
          Integer.valueOf(nbArgs), Integer.valueOf(domainLength));
    }
  } // verifyMakeDeclArgs

  private void verifySymbolPairNameDeclList(PairNameDeclList pairNameDeclList, String symbolType) {
    // we test the existence of 2 same slot names
    Collection<String> listSlot = new HashSet<String>();
    %match(PairNameDeclList pairNameDeclList) {
      concPairNameDecl(_*, PairNameDecl[SlotName=Name(name)], _*) -> { // for each Slot
        if(listSlot.contains(`name)) {
          // TODO
          // messageWarningTwoSameSlotDeclError(name, orgTrack, symbolType);
        } else {
          listSlot.add(`name);
        }
      }
    }
  } // verifySymbolPairNameDeclList

  private void messageMissingMacroFunctions(String symbolType, List list) {
    StringBuilder listOfMissingMacros = new StringBuilder();
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros.append(list.get(i) + ",  ");
    }
    String stringListOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    TomMessage.error(getLogger(),
        getCurrentTomStructureOrgTrack().getFileName(),
        getCurrentTomStructureOrgTrack().getLine(),
        TomMessage.missingMacroFunctions,
        stringListOfMissingMacros);
  } // messageMissingMacroFunctions

  // ////////////////////////////// /
  // MATCH VERIFICATION CONCERNS ///
  // ////////////////////////////////
  /**
   * Verifies the match construct
   * 
   * 0. checks that are not any circular dependencies
   * 1. Verifies all MatchConstraints
   * 2. Verifies all NumericConstraints 
   * 3. Verifies that in an OrConstraint, all the members have the same free variables
   */
  private void verifyMatch(ConstraintInstructionList constraintInstructionList, OptionList optionList) throws VisitFailure {

    setCurrentTomStructureOrgTrack(TomBase.findOriginTracking(optionList));
    Collection<Constraint> constraints = new HashSet<Constraint>();    
    Map<TomName, Collection<TomName>> varRelationsMap = new HashMap<TomName, Collection<TomName>>();
    Map<Constraint, Instruction> orActionMap = new HashMap<Constraint, Instruction>();
    Collection tmp = new HashSet();
    `TopDownCollect(CollectConstraints(constraints, tmp, orActionMap)).visitLight(constraintInstructionList);
    for(Constraint constr: constraints) {
matchLbl: %match(constr) {// TODO : add something to test the astType
            MatchConstraint(pattern,subject,astType) -> {
              Collection<TomName> patternVars = new HashSet<TomName>();
              Collection<TomName> subjectVars = new HashSet<TomName>();
              `TopDownCollect(CollectVariables(patternVars)).visitLight(`pattern);
              `TopDownCollect(CollectVariables(subjectVars)).visitLight(`subject);
              computeDependencies(varRelationsMap,patternVars,subjectVars);

              //System.out.println("varRelationsMap: " + varRelationsMap);
              //System.out.println("patternVars: " + patternVars);
              //System.out.println("subjectVars: " + subjectVars);

              if(`astType == SymbolTable.TYPE_UNKNOWN) {

                if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
                  // TODO: remove this test when newtyper will be the only typer
                  TomType typeMatch = getSubjectType(`subject,constraints);
                  if(typeMatch == null) {
                    %match(subject) {
                      BQVariable[AstName=Name(stringName)] -> {
                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                            `stringName);
                        return;
                      }
                      BQAppl[AstName=Name(stringName)] -> {
                        TomMessage.error(getLogger(),
                            getCurrentTomStructureOrgTrack().getFileName(),
                            getCurrentTomStructureOrgTrack().getLine(),
                            TomMessage.cannotGuessMatchType,
                            `stringName);
                        return;
                      }
                      BuildConstant[AstName=Name(_)] -> {
                        // do not throw an error message because Constant have no type
                      }
                    }
                    return;
                  }
                }
                verifyMatchPattern(`pattern, getPatternType(`pattern));
              } else {
                // astType is known
                if (!testTypeExistence(`astType.getTomType())) {
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.unknownType,
                      `astType.getTomType());
                }
                // we now compare the pattern to its definition
                verifyMatchPattern(`pattern, `astType);
              }
            }

            // The lhs or rhs can only be TermAppl or Variable
            // (if we have the type, check that it is the same)
            // 1. no annotations
            // 2. no annonymous vars
            // 3. no anti-patterns
            // 4. no implicit notation
            //NumericConstraint[Pattern=left,Subject=right] -> 
            NumericConstraint[Left=left,Right=right] -> {
              // the lhs can only be a BQAppl, a BQVariable, or a BuildConstant
              %match(left) {
                !(BQVariable|BQAppl|BuildConstant)[] -> {
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericLeft,
                      getName(`left));
                  return;
                }
              }        
              // the rhs can only be a BQAppl, a BQVariable, or a BuildConstant
              %match(right) {
                !(BQVariable|BQAppl|BuildConstant)[] -> {
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.termOrVariableNumericRight,
                      getName(`right));
                  return;
                }
              }
              // if we have the type, check that it is the same
              TomType leftType = TomBase.getTermType(`left,getSymbolTable());
              TomType rightType = TomBase.getTermType(`right,getSymbolTable());
              // if the types are not available, leave the error to be raised by java
              if(leftType != null && leftType != SymbolTable.TYPE_UNKNOWN && leftType != `EmptyType() 
                  && rightType != null && rightType != SymbolTable.TYPE_UNKNOWN && rightType != `EmptyType() 
                  && (leftType != rightType)) {
                TomMessage.error(getLogger(),
                    getCurrentTomStructureOrgTrack().getFileName(),
                    getCurrentTomStructureOrgTrack().getLine(),
                    TomMessage.invalidTypesNumeric,
                    `leftType,getName(`left),`rightType,getName(`right)); 
                return;
              }
            }

            oc@OrConstraint(_*) -> {
              if(!verifyOrConstraint(`oc,orActionMap.get(`oc))) {
                return;
              }
            }
          }
    } // for

    checkVarDependencies(varRelationsMap);
  } //verifyMatch

  /**
   * Puts all the variables in the list patternVars in relation with all the variables in subjectVars
   */
  private void computeDependencies(Map<TomName, Collection<TomName>> varRelationsMap, Collection<TomName> patternVars, Collection<TomName> subjectVars) {
    for(TomName x:patternVars) {      
      if(!varRelationsMap.keySet().contains(x)) {
        varRelationsMap.put(x,subjectVars);
      } else { // add the rest of the variables
        Collection<TomName> tmp = new HashSet<TomName>(subjectVars);
        tmp.addAll(varRelationsMap.get(x));
        varRelationsMap.put(x,tmp);
      }
    }
  }

  /**
   * Checks that there is not a circular reference of a variable
   */
  private void checkVarDependencies(Map<TomName, Collection<TomName>> varRelationsMap) {
    for(TomName var:varRelationsMap.keySet()) {
      isVariablePresent(var, varRelationsMap.get(var), varRelationsMap, new HashSet<TomName>());
    }
  }

  private void isVariablePresent(TomName var, Collection<TomName> associatedList, Map<TomName, Collection<TomName>> varRelationsMap, Collection<TomName> checked) {    
    if(associatedList.contains(var)) {
      TomMessage.error(getLogger(),
          getCurrentTomStructureOrgTrack().getFileName(),
          getCurrentTomStructureOrgTrack().getLine(),
          TomMessage.circularReferences,
          ((Name)var).getString());       
    } else {
      for(TomName tn: associatedList) {
        if(checked.contains(tn)) { return; }
        checked.add(tn);
        Collection<TomName> lst = varRelationsMap.get(tn);
        if(lst != null) { 
          isVariablePresent(var, lst, varRelationsMap, checked);
        }
      }
    }    
  }

  /**
   * Verifies that in an OrConstraint, all the members have the same free variables 
   * (only the match constraints have free variables - because only this type
   * of constraint can generate assignments)
   */
  private boolean verifyOrConstraint(Constraint orConstraint, Instruction action) throws VisitFailure {
    Collection<TomTerm> freeVarList1 = new HashSet<TomTerm>();
    Collection<TomTerm> freeVarList2 = new HashSet<TomTerm>();
    %match(orConstraint) {
      OrConstraint(_*,x,_*) -> {
        if(`x != `FalseConstraint()) {
          // we collect the free vars only from match constraints
          // and we check these variables for numeric constraints also
          // ex: 'y << a() || x > 3' should generate an error 
          Strategy collect = `Sequence(
              TopDownCollect(CollectAliasVar(freeVarList2)),
              TopDownCollect(CollectFreeVar(freeVarList2)));

          %match(Constraint x) {
            MatchConstraint(pattern,_,_) -> { 
              collect.visitLight(`pattern);
            }
            AndConstraint(_*,MatchConstraint(pattern,_,_),_*) -> { 
              collect.visitLight(`pattern);
            }
          }
          //System.out.println("freeVar1 = " + freeVarList1);
          //System.out.println("freeVar2 = " + freeVarList2);

          if(!freeVarList1.isEmpty()) {
            for(TomTerm term:freeVarList2) {
              if(!containsVariable(term,freeVarList1)) {
                if(containsVariable(term, action)) {              
                  String varName = term.getAstName().getString();
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.freeVarNotPresentInOr,
                      varName);
                  return false;
                }
              }
            }
            for(TomTerm term:freeVarList1) {
              if(!containsVariable(term,freeVarList2)) {
                if(containsVariable(term, action))  {
                  String varName = term.getAstName().getString();
                  TomMessage.error(getLogger(),
                      getCurrentTomStructureOrgTrack().getFileName(),
                      getCurrentTomStructureOrgTrack().getLine(),
                      TomMessage.freeVarNotPresentInOr,
                      varName);
                  return false;
                }
              }
            }          
          }
          freeVarList1 = new HashSet<TomTerm>(freeVarList2);
          freeVarList2.clear();
        }
      }
    }
    return true;
  }

  private boolean containsVariable(TomTerm var, Collection<TomTerm> list) {
    for(TomTerm t:list) {
      %match(var,t) {
        Variable[AstName=name], Variable[AstName=name] -> { return true; }
        VariableStar[AstName=name], VariableStar[AstName=name] -> { return true; }
      }
    }
    return false;
  }

  private boolean containsVariable(TomTerm var, Instruction action) {
    try {
      `TopDown(ContainsVariable(var)).visitLight(action);
    } catch(VisitFailure e) {
      return true;
    }
    return false;
  }

  %strategy ContainsVariable(TomTerm var) extends Identity() {     
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {
        //System.out.println("name = " + `name);
        //System.out.println("var.name = " + var.getAstName());
        if(`name==var.getAstName()) {
          throw new VisitFailure();
        }
      }
    }
  }

  /**
   * Collect the free variables in an constraint (do not inspect under a anti)  
   */
  %strategy CollectFreeVar(varList:Collection) extends Identity() {     
    visit TomTerm {
      v@(Variable|VariableStar)[] -> {
        TomTerm newvar = `v.setOptions(`concOption()); // to avoid problems related to line numbers
        if(!varList.contains(newvar)) { 
          varList.add(`v); 
        }
        throw new VisitFailure();/* stop the top-down */
      }
      AntiTerm[] -> {        
        throw new VisitFailure();/* stop the top-down */
      }
    }
  }

  /**
   * Collect the anoted variables in an constraint (should inspect under a anti)  
   */
  %strategy CollectAliasVar(varList:Collection) extends Identity() {     
    visit Constraint {
      AliasTo(v@(Variable|VariableStar)[]) -> {
        TomTerm newvar = `v.setOptions(`concOption()); // to avoid problems related to line numbers
        if(!varList.contains(newvar)) { 
          varList.add(`v); 
        }
        throw new VisitFailure();/* stop the top-down */
      }
    }
  }

  /**
   * Collect the variables' names   
   */
  %strategy CollectVariables(Collection varList) extends Identity() {     
    visit TomTerm {
      (Variable|VariableStar)[AstName=name] -> {        
        if(!varList.contains(`name)) {
          varList.add(`name); 
        }
        throw new VisitFailure();/* stop the top-down */
      }
    }
    visit BQTerm {
      (BQVariable|BQVariableStar)[AstName=name] -> {        
        if(!varList.contains(`name)) {
          varList.add(`name); 
        }
        throw new VisitFailure();/* stop the top-down */
      }
    }
  }

  /**
   * tries to give the type of the tomTerm received as parameter
   */
  private TomType getSubjectType(BQTerm subject, Collection<Constraint> constraints) {
    %match(subject) {
      BuildConstant[AstName=Name(name)] -> {        
        try {
          Integer.parseInt(`name);
          return getSymbolTable().getIntType();
        } catch(java.lang.NumberFormatException e) {
          return getSymbolTable().getStringType();
        }
      }

      BQVariable[AstName=Name(name),AstType=tomType@Type(_,type,EmptyTargetLanguageType())] -> { 
        if(`tomType==SymbolTable.TYPE_UNKNOWN) {
          // try to guess
          return guessSubjectType(`subject,constraints);
        } else if(testTypeExistence(`type)) {
          return `tomType;
        } else {
          TomMessage.error(getLogger(),
              getCurrentTomStructureOrgTrack().getFileName(),
              getCurrentTomStructureOrgTrack().getLine(),
              TomMessage.unknownMatchArgumentTypeInSignature,
              `name, `type);                
        }
      }

      BQAppl[AstName=Name(name)] -> {
        TomSymbol symbol = getSymbolFromName(`name);
        if(symbol!=null) {
          TomType type = TomBase.getSymbolCodomain(symbol);
          String typeName = TomBase.getTomType(`type);
          if(!testTypeExistence(typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                `name, typeName);
          }          
          //verifyBQAppl(`term);
          return type;
        } else {
          // try to guess
          return guessSubjectType(`subject,constraints);
        }
      }
    }
    return null;
  }

  private TomType getPatternType(TomTerm pattern) {
    %match(pattern) {
      AntiTerm(p) -> { pattern = `p; }
    }
    %match(pattern) {
      (TermAppl|RecordAppl)[NameList=concTomName(Name(name),_*)] -> {        
        TomSymbol symbol = getSymbolFromName(`name);
        if(symbol!=null) {
          TomType type = TomBase.getSymbolCodomain(symbol);
          // System.out.println("type = " + type);            
          String typeName = TomBase.getTomType(`type);
          if(!testTypeExistence(typeName)) {
            TomMessage.error(getLogger(),
                getCurrentTomStructureOrgTrack().getFileName(),
                getCurrentTomStructureOrgTrack().getLine(),
                TomMessage.unknownMatchArgumentTypeInSignature,
                `name, typeName);
          }
          return type;
        }
      }
      // TOBE CONTINUED            
      //            var@Variable[] -> {
      //              TomType type = getVarTypeFromConstraints(var,constraints);
      //              if ( type != null ) {
      //                return type;
      //              }
      //            }
      // TOBE CONTINUED        
      //        NumericConstraint[Left=left,Right=right] -> {
      //          // we want two terms to be equal even if their option is different 
      //          //( because of their possition for example )
      //          if ((`right.setOptions(`concOption())) != (subject.setOptions(`concOption()))) { continue; }
      //          if (`left.isVariable()) {
      //            TomType type = guessVarTypeFromConstraints(var,matchConstraints);
      //            if ( type != null ) {
      //              return type;
      //            }
      //          }
      //        }
    }

    return null;
  }

  /**
   * if a type is not specified 
   * 1. we look for a type in all match constraints where we can find this subject
   * 2. TODO: if the subject is in a constraint with a variable (the pattern is a variable for instance),
   * try to see if a variable with the same name already exists and can be typed, and if yes, get that type
   */
  private TomType guessSubjectType(BQTerm subject,Collection<Constraint> constraints) {
    for(Constraint constr:constraints) {
      %match(Constraint constr) {
        MatchConstraint(patt,s,_) -> {
          // we want two terms to be equal even if their option is different 
          //( because of their position for example )
matchL:  %match(subject,s) {
           BQVariable[AstName=astName,AstType=tomType],BQVariable[AstName=astName,AstType=tomType] -> {break matchL;}
           BQAppl[AstName=tomName,Args=tomList],BQAppl[AstName=tomName,Args=tomList] -> {break matchL;}
           _,_ -> { continue; }
         }

         TomType type = getPatternType(`patt);
         if(type!=null) {
           return type;
         }
        }
      }
    }// for    
    return null;
  }

  //  /**
  //   * trys to guess the type of the variable by looking into all constraints 
  //   * if it can find it somewhere where it is typed
  //   */
  //  private TomType guessVarTypeFromConstraints(TomTerm var, ArrayList<Constraint> constraints){
  //    
  //  }

  /**
   * Collect the matchConstraints in a list of constraints   
   */
  %strategy CollectMatchConstraints(constrList:List) extends Identity() {
    visit Constraint {
      m@MatchConstraint[] -> {        
        constrList.add(`m);         
        throw new VisitFailure();/* stop the top-down */
      }      
    }
  }

  /**
   * Collect the constraints (match and numeric)
   * For Or constraints, collect also the associated action
   */
  %strategy CollectConstraints(Collection constrList, Collection lastAction, Map orActionMap) extends Identity() {
    visit ConstraintInstruction {
      ConstraintInstruction[Action=action] -> {
        lastAction.clear();
        lastAction.add(`action);
      }
    }
    visit Constraint {
      c@(MatchConstraint|NumericConstraint)[] -> {        
        constrList.add(`c);         
        throw new VisitFailure();/* stop the top-down */
      }      
      oc@OrConstraint(_*) -> {
        constrList.add(`oc);
        Iterator it = lastAction.iterator();
        orActionMap.put(`oc,it.next());
      }
    }
  }

  /**
   * check the lhs of a rule
   */
  private void verifyMatchPattern(TomTerm term, TomType type) {      
    // the term cannot be a Var* nor a _*
    
    //System.out.println("verifyMatchPattern: " + term);

    %match(term) {
      VariableStar[Options=options, AstName=EmptyName()] -> {
        String fileName = findOriginTrackingFileName(`options);
        int decLine = findOriginTrackingLine(`options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch, "_*");
        return;
      }

      VariableStar[Options=options, AstName=Name(name)] -> {
        String fileName = findOriginTrackingFileName(`options);
        int decLine = findOriginTrackingLine(`options);
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.incorrectVariableStarInMatch, `name);
        return;
      }
    }
    // Analyse the term if type != null
    //if(type != null) {
      // the type is known and found in the match signature
    // 'type' may be null
      validateTerm(`term, type, false, true);
    //}
  }

  /*
   * verify the structure of a %strategy
   */
  private void verifyStrategy(TomVisitList visitList) throws VisitFailure {
    %match(visitList) {
      concTomVisit(_*,VisitTerm(type,constraintInstructionList,option),_*) -> {
        List<MatchConstraint> matchConstraints = new ArrayList<MatchConstraint>();
        %match(constraintInstructionList) {
          concConstraintInstruction(_*,ConstraintInstruction[Constraint=constraint],_*) -> {
            matchConstraints.clear();
            `TopDownCollect(CollectMatchConstraints(matchConstraints)).visitLight(`constraint);   
            // for the first constraint, check that the type is conrform to the type specified in visit
            // the order is important, this is why we used an ArrayList.
            // TopDown is supposed to traverse from left-to-right
            MatchConstraint firstMatchConstr = matchConstraints.iterator().next(); 
            verifyMatchPattern(firstMatchConstr.getPattern(), `type);
          }
        }    
        // check the rest of the constraints
        `verifyMatch(constraintInstructionList,option);
      }
    }
  }

  /*
   * Verify structure of a %transformation
   */
//  private void verifyTransformation(TomWithToList wtlist) throws VisitFailure {
    //TODO:
    // - test if a withTerm exists (+args) -> here ?
    // - test toTerm structure ?
//  }

  /**
   * Analyse a term given an expected type and re-enter recursively on children
   */
  public TermDescription validateTerm(TomTerm term, TomType expectedType, boolean listSymbol, boolean topLevel) {
    String termName = "emptyName";
    TomType type = null;
    int termClass = -1;
    String fileName = "unknown";
    int decLine = -1;
    Option orgTrack;

    //System.out.println("validateTerm: " + term);

matchblock:{
    %match(term) {
      TermAppl[Options=options, NameList=symbolNameList, Args=arguments] -> {
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        termClass = TERM_APPL;

        TomSymbol symbol = ensureValidApplDisjunction(`symbolNameList, expectedType, fileName, decLine,  topLevel);

        if(symbol == null) {
          // null means that an error occured
          break matchblock;
        }
        // Type is OK
        type = expectedType;
        TomName headName = `symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) {
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        boolean listOp = (TomBase.isListOperator(symbol) || TomBase.isArrayOperator(symbol));
        if(listOp) {
          // whatever the arity is, we continue recursively and there is
          // only one element in the Domain
          // - we can also have children that are sublists
          validateListOperatorArgs(`arguments, symbol.getTypesToType().getDomain().getHeadconcTomType(),
              symbol.getTypesToType().getCodomain());
        } else {
          // the arity is important also there are different types in Domain
          TomTypeList types = symbol.getTypesToType().getDomain();
          int nbArgs = `arguments.length();
          int nbExpectedArgs = types.length();
          if(nbArgs != nbExpectedArgs) {
            TomMessage.error(getLogger(),
                fileName, decLine, TomMessage.symbolNumberArgument,
                termName, Integer.valueOf(nbExpectedArgs), Integer.valueOf(nbArgs));
            break matchblock;
          }
          TomList args = `arguments;
          while(!args.isEmptyconcTomTerm()) {
            // repeat analyse with associated expected type and control
            // arity
            validateTerm(args.getHeadconcTomTerm(), types.getHeadconcTomType(), listOp/* false */, false);
            args = args.getTailconcTomTerm();
            types = types.getTailconcTomType();
          }
        }
        break matchblock;
      }

      RecordAppl[Options=options,NameList=symbolNameList,Slots=slotList] -> {
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        termClass = RECORD_APPL;
        TomSymbol symbol = ensureValidRecordDisjunction(`symbolNameList, `slotList, expectedType, fileName, decLine, true);
        if(symbol == null) {
          break matchblock;
        }

        %match(TomNameList symbolNameList) {
          /*
           * We perform tests as we have different RecordAppls: they all must
           * be valid and have the expected return type
           */
          concTomName(_*,Name(name), _*) -> {
            verifyRecordStructure(`options, `name, `slotList, fileName,decLine);
          }
        }

        type = expectedType;
        TomName headName = `symbolNameList.getHeadconcTomName();
        if(headName instanceof AntiName) {
          headName = ((AntiName)headName).getName();
        }
        termName = headName.getString();
        break matchblock;
      }

      Variable[Options=options, AstName=Name(name)] -> {
        termClass = VARIABLE;
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        type = null;
        termName = `name;
        break matchblock;
      }

      VariableStar[Options=options, AstName=Name(name)] -> {
        termClass = VARIABLE_STAR;
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        type = null;
        termName = `name+"*";
        if(!listSymbol) {
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }
      Variable[Options=options, AstName=EmptyName()] -> {
        termClass = UNAMED_VARIABLE;
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        type = null;
        termName = "unamed";
        break matchblock;
      }

      VariableStar[Options=options, AstName=EmptyName()] -> {
        termClass = UNAMED_VARIABLE_STAR;
        fileName = findOriginTrackingFileName(`options);
        decLine = findOriginTrackingLine(`options);
        type = null;
        termName = "unamed*";
        if(!listSymbol) {
          TomMessage.error(getLogger(),
              fileName,
              decLine,
              TomMessage.invalidVariableStarArgument,
              termName);
        }
        break matchblock;
      }

    }
    throw new TomRuntimeException("Strange Term "+term);
    } // end matchblock
    return new TermDescription(termClass, termName, fileName,decLine, type);
  }

  private TomSymbol ensureValidApplDisjunction(TomNameList symbolNameList, TomType expectedType, 
      String fileName, int decLine,  boolean topLevel) {

    //System.out.println("symbolNameList = " + symbolNameList);

    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);

      //System.out.println("symbol = " + symbol);

      if(symbol == null ) {
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { // known symbol
        if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
          if( !topLevel ) {
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
              return null;
            }
          }
        }
      }
      return symbol;
    } else {
      // this is a disjunction
      TomSymbol symbol = null;
      TomTypeList domainReference = null;
      PairNameDeclList slotReference = null;
      String nameReference = null;
      %match(symbolNameList) {
        concTomName(_*, Name(dijName), _*) -> { // for each SymbolName
          symbol = getSymbolFromName(`dijName);
          if(symbol == null) {
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction,`dijName);
            return null;
          }
          if( !topLevel ) {
            // ensure codomain is correct
            if(!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, fileName,decLine)) {
              return null;
            }
          }

          if(domainReference == null) { // save Domain reference
            domainReference = TomBase.getSymbolDomain(symbol);
            slotReference = symbol.getPairNameDeclList();
            nameReference = `dijName;
          } else {
            if(TomBase.getSymbolDomain(symbol) != domainReference) {
              TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, `dijName);
              return null;
            }
            if(symbol.getPairNameDeclList() != slotReference) {
              PairNameDeclList l1 = slotReference;
              PairNameDeclList l2 = symbol.getPairNameDeclList();
              while(!l1.isEmptyconcPairNameDecl()) {
                if(l1.getHeadconcPairNameDecl().getSlotName() != l2.getHeadconcPairNameDecl().getSlotName()) {
                  TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, nameReference, `dijName);
                  return null;
                }
                l1=l1.getTailconcPairNameDecl();
                l2=l2.getTailconcPairNameDecl();
              }
            }
          }
        }
      }
      return symbol;
    }
  } //ensureValidApplDisjunction

  private boolean ensureSymbolCodomain(TomType currentCodomain, TomType expectedType, TomMessage msg, String symbolName, String fileName,int decLine) {
    %match(currentCodomain, expectedType) {
      Type[TomType=type],Type[TomType=type] -> {
        return true;
      }
    }
    //System.out.println(currentCodomain+"!="+expectedType);
    TomMessage.error(getLogger(),fileName,decLine, msg,
        symbolName, currentCodomain.getTomType(), expectedType.getTomType());
    return false;
  } //ensureSymbolCodomain

  private TomSymbol ensureValidRecordDisjunction(TomNameList symbolNameList, SlotList slotList, 
      TomType expectedType, String fileName, int decLine, boolean topLevel) {
    if(symbolNameList.length()==1) { // Valid but has it a good type?
      String res = symbolNameList.getHeadconcTomName().getString();
      TomSymbol symbol = getSymbolFromName(res);
      if (symbol == null ) { // this correspond to: unknown[]
        // it is not correct to use Record with unknown symbols
        TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbol, res);
        return null;
      } else { // known symbol
        // ensure type correctness if necessary
        if (!getOptionBooleanValue("newtyper")) {//case of subtyping (-nt option activated)
          if ( !topLevel ) {
            if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidCodomain, res, fileName,decLine)) {
              return null;
            }
          }
        }
      }
      return symbol;
    } else {
      TomSymbol symbol = null;
      TomSymbol referenceSymbol = null;
      TomTypeList referenceDomain = null;
      String referenceName = null;
      %match(TomNameList symbolNameList) {
        concTomName(_*, Name(dijName), _*) -> { // for each SymbolName
          symbol =  getSymbolFromName(`dijName);
          if(symbol == null) {
            // In disjunction we can only have known symbols
            TomMessage.error(getLogger(),fileName,decLine, TomMessage.unknownSymbolInDisjunction, `dijName);
            return null;
          }
          if( !topLevel ) {
            // ensure codomain is correct
            if (!ensureSymbolCodomain(TomBase.getSymbolCodomain(symbol), expectedType, TomMessage.invalidDisjunctionCodomain, `dijName, fileName,decLine)) {
              return null;
            }
          }
          // System.out.println("domain = " + getSymbolDomain(symbol));

          if(referenceDomain == null) { // save Domain reference
            referenceSymbol = symbol;
            referenceName = `dijName;
            referenceDomain = TomBase.getSymbolDomain(symbol);
          } else {
            // check that domains are compatible
            TomTypeList currentDomain = TomBase.getSymbolDomain(symbol);
            // restrict the domain to the record
            while(!slotList.isEmptyconcSlot()) {
              Slot slot = slotList.getHeadconcSlot();
              TomName slotName = slot.getSlotName();
              int currentSlotIndex = TomBase.getSlotIndex(symbol,slotName);
              int referenceSlotIndex = TomBase.getSlotIndex(referenceSymbol,slotName);

              // System.out.println("index1 = " + currentSlotIndex);
              // System.out.println("type1 = " +
              // TomBase.elementAt(currentDomain,currentSlotIndex));
              // System.out.println("index2 = " + referenceSlotIndex);
              // System.out.println("type2 = " +
              // TomBase.elementAt(referenceDomain,referenceSlotIndex));

              if (referenceSlotIndex == -1){
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, referenceName,((Name)slotName).getString());
                return null;                
              }

              if (currentSlotIndex == -1){
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionSlotName, `dijName,((Name)slotName).getString());
                return null;                
              }

              if(TomBase.elementAt(currentDomain,currentSlotIndex) != TomBase.elementAt(referenceDomain,referenceSlotIndex)) {
                TomMessage.error(getLogger(),fileName,decLine, TomMessage.invalidDisjunctionDomain, referenceName, `dijName);
                return null;
              }

              slotList = slotList.getTailconcSlot();
            }

          }
        }
      }
      return symbol;
    }
  } //ensureValidRecordDisjunction

  // /////////////////////
  // RECORDS CONCERNS ///
  // /////////////////////
  private void verifyRecordStructure(OptionList option, String tomName, SlotList slotList, String fileName, int decLine)  {
    TomSymbol symbol = getSymbolFromName(tomName);
    if(symbol != null) {
      // constants have an emptyPairNameDeclList
      // the length of the pairNameDeclList corresponds to the arity of the
      // operator

      // Note: cannot detect conc[], because the concrete syntax [] is no longer there
      //if(slotList.isEmptyconcSlot() && (TomBase.isListOperator(symbol) ||  TomBase.isArrayOperator(symbol)) ) {
      //  TomMessage.error(getLogger(),fileName,decLine, TomMessage.bracketOnListSymbol, tomName);
      //}
      // TODO verify type
      verifyRecordSlots(slotList,symbol, TomBase.getSymbolDomain(symbol), tomName, fileName, decLine);
    } else {
      TomMessage.error(getLogger(),fileName,decLine,
          TomMessage.unknownSymbol,
          tomName);
    }
  } //verifyRecordStructure

  // We test the existence/repetition of slotName contained in pairSlotAppl
  // and then the associated term
  private void verifyRecordSlots(SlotList slotList, TomSymbol tomSymbol, TomTypeList typeList, String methodName, String fileName, int decLine) {
    //System.out.println("verifyRecordSlot: " + `slotList);
    Collection<String> listOfPossibleSlot = null;
    Collection<Integer> studiedSlotIndexList = new HashSet<Integer>();
    // for each pair slotName <=> Appl
    while( !slotList.isEmptyconcSlot() ) {
      TomName pairSlotName = slotList.getHeadconcSlot().getSlotName();
      // First check for slot name correctness
      int index = TomBase.getSlotIndex(tomSymbol,pairSlotName);
      if(index < 0) {// Error: bad slot name
        if(listOfPossibleSlot == null) {
          // calculate list of possible slot names..
          listOfPossibleSlot = new HashSet<String>();
          PairNameDeclList listOfSlots = tomSymbol.getPairNameDeclList();
          while ( !listOfSlots.isEmptyconcPairNameDecl() ) {
            TomName sname = listOfSlots.getHeadconcPairNameDecl().getSlotName();
            if(!sname.isEmptyName()) {
              listOfPossibleSlot.add(sname.getString());
            }
            listOfSlots = listOfSlots.getTailconcPairNameDecl();
          }
        }
        TomMessage.error(getLogger(),fileName,decLine,
            TomMessage.badSlotName,
            pairSlotName.getString(), methodName, listOfPossibleSlot.toString());
        return; // break analyses
      } else { // then check for repeated good slot name
        //Integer integerIndex = Integer.valueOf(index);
        if(pairSlotName.isName() && studiedSlotIndexList.contains(index)) {
          // we are not insterrested in EmptyName (which come from ListOperator with Empty-SlotName)
          // Error: repeated slot
          TomMessage.error(getLogger(),fileName,decLine,
              TomMessage.slotRepeated,
              methodName, pairSlotName.getString());
          return; // break analyses
        }
        studiedSlotIndexList.add(index);
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
                    validateTerm(`slotSubterm ,expectedType, false, true);
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
  } //verifyRecordSlots

  public void validateListOperatorArgs(TomList args, TomType expectedType, TomType parentListCodomain) {
    %match(args) {
      concTomTerm(_*,arg@(TermAppl|RecordAppl)[],_*) -> {
        TomSymbol argSymbol = getSymbolFromName(getName(`arg));
        // if we have a sublist 
        if(TomBase.isListOperator(argSymbol)) {
          // we can have two cases:
          // 1. the sublist has the codomain = parentListCodomain
          // 2. the sublist has the codomain = expectedType
          if(argSymbol.getTypesToType().getCodomain() == parentListCodomain) {
            validateTerm(`arg, parentListCodomain, true, false);            
          } else {
            validateTerm(`arg, expectedType, true, false);    
          }        
        } else {
          validateTerm(`arg, expectedType, true, false);
        }
      }
    }
  } //validateListOperatorArgs

  private boolean testTypeExistence(String typeName) {
    return getSymbolTable().getType(typeName) != null;
  }

  protected class TermDescription {
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
  } //class TermDescription

  /**
   * verify a BackQuoteAppl term
   * @param subject the backquote term
   * 1. arity
   */
  private void verifyBQAppl(BQTerm subject) throws VisitFailure {
    %match(subject) {
      BQAppl[Options=options,AstName=Name(name),Args=args] -> {
        int decLine = findOriginTrackingLine(`options);
        String fileName = findOriginTrackingFileName(`options);
        TomSymbol symbol = getSymbolFromName(`name);
        if(symbol==null) {
          // cannot report an error because unknown funtion calls are allowed
          //TomMessage.error(getLogger(), fileName, decLine, TomMessage.unknownSymbol, `name);
        } else {
          //System.out.println("symbol = " + symbol);
          // check the arity (for syntacti operators)
          if(!TomBase.isArrayOperator(`symbol) && !TomBase.isListOperator(`symbol)) {
            TomTypeList types = symbol.getTypesToType().getDomain();
            int nbExpectedArgs = types.length();
            int nbArgs = `args.length();
            if(nbArgs != nbExpectedArgs) {
              TomMessage.error(getLogger(), fileName, decLine,
                  TomMessage.symbolNumberArgument,
                  `name, nbExpectedArgs, nbArgs);
            }
          }
        }
      }
    }
  }

  private String getName(TomTerm term) {
    %match(term) {
      TermAppl[NameList=concTomName(Name(name))] -> { return `name;}
      TermAppl[NameList=nameList] -> {
        String dijunctionName = `nameList.getHeadconcTomName().getString();
        while(!`nameList.isEmptyconcTomName()) {
          String head = `nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTailconcTomName();
        }
        return dijunctionName;
      }
      RecordAppl[NameList=concTomName(Name(name))] -> { return `name;}
      RecordAppl[NameList=nameList] -> {
        String dijunctionName = `nameList.getHeadconcTomName().getString();
        while(!`nameList.isEmptyconcTomName()) {
          String head = `nameList.getHeadconcTomName().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTailconcTomName();
        }
        return dijunctionName;
      }
      Variable[AstName=Name(name)] -> { return `name;}
      VariableStar[AstName=Name(name)] -> { return `name+"*";}
      AntiTerm(t) -> { return getName(`t); }
    }
    throw new TomRuntimeException("Invalid Term:" + term);
  }

  private String getName(BQTerm term) {
    %match(term) {
      BQAppl[AstName=Name(name)] -> { return `name;}
      BQVariable[AstName=Name(name)] -> { return `name;}
      BQVariableStar[AstName=Name(name)] -> { return `name+"*";}
      BuildConstant[AstName=Name(name)] -> { return `name;}
    }
    throw new TomRuntimeException("Invalid Term:" + term);
  }
} //class
