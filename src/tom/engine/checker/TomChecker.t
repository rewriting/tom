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

import aterm.*;

import java.util.*;

import jtom.*;
import jtom.adt.tomsignature.types.*;
import jtom.runtime.Collect1;
import jtom.xml.Constants;
import jtom.exception.*;
import jtom.TomMessage;

abstract public class TomChecker extends TomGenericPlugin //Base 
{
  
    // ------------------------------------------------------------
  %include { ../adt/TomSignature.tom }
    // ------------------------------------------------------------
  
  class TermDescription {
    int termClass, decLine;
    String name ="";
    TomType tomType = null;
    public TermDescription(int termClass, String name, int decLine, TomType tomType) {
      this.termClass = termClass;
      this.decLine = decLine;
      this.name = name;
      this.tomType = tomType;
    }
    public String type() {
      if(tomType != null && !tomType.isEmptyType()) {
        return tomType.getString();
      } else {
        return null;
      }
    }
  }
    // Different kind of structures
  private final static int APPL = 0;
  private final static int UNAMED_APPL = 1;
  private final static int APPL_DISJUNCTION = 2;
  private final static int RECORD_APPL = 3;
  private final static int RECORD_APPL_DISJUNCTION = 4;
  private final static int XML_APPL = 5;
  private final static int VARIABLE_STAR = 6;
  private final static int UNAMED_VARIABLE_STAR = 7;
  private final static int PLACE_HOLDER = 8;
  
  
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
  private ArrayList alreadyStudiedRule =  new ArrayList();
  private Option currentTomStructureOrgTrack;
    
  public TomChecker(String name) {
    super(name);
  }

  protected void reinit() {
    alreadyStudiedTypes   = new ArrayList();
    alreadyStudiedSymbols = new ArrayList();
    alreadyStudiedRule    = new ArrayList();
    currentTomStructureOrgTrack = null;
  }

  public void initProcess() {
    verbose    =   getServer().getOptionBooleanValue("verbose");
    strictType = ! getServer().getOptionBooleanValue("lazyType");
    warningAll =   getServer().getOptionBooleanValue("Wall");
    noWarning  =   getServer().getOptionBooleanValue("noWarning");
  } 
    /**
     * Main type checking entry point:
     * We check all Match and RuleSet instructions
     */
  protected void checkTypeInference(TomTerm expandedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm term) {
          if(term instanceof Instruction) {
            %match(Instruction term) {
              Match(_, PatternList(list), oplist) -> {  
                currentTomStructureOrgTrack = findOriginTracking(`oplist);
                verifyMatchVariable(`list);
                return false;
              }
              RuleSet(list, orgTrack) -> {
                currentTomStructureOrgTrack = `orgTrack;
                verifyRuleVariable(`list);
                return false;
              }
            }
          } 
          return true;
        }// end apply
      }; // end new
    traversal().genericCollect(expandedTerm, collectAndVerify);
  } //checkTypeInference
  
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
  } //verifyMatchVariable
  
  private void verifyRuleVariable(TomRuleList list) {
    while(!list.isEmpty()) {
      TomRule rewriteRule = list.getHead();
      TomTerm lhs = rewriteRule.getLhs();
      TomTerm rhs = rewriteRule.getRhs();
      InstructionList condList = rewriteRule.getCondList();
      Option orgTrack = findOriginTracking(rewriteRule.getOption());
       
      ArrayList variableLhs = new ArrayList();
      collectVariable(variableLhs, lhs);
        // System.out.println("lhs: "+variableLhs);
      HashSet lhsSet = verifyVariableType(variableLhs);
       
      ArrayList variableRhs = new ArrayList();
      collectVariable(variableRhs, rhs);
        // System.out.println("rhs: "+variableRhs);
      HashSet rhsSet = verifyVariableType(variableRhs);
       
      ArrayList variableCond = new ArrayList();
        /*%match(TomList condList) {
          (_*, cond, _*) -> {
          %match(TomTerm cond) {
          matching@MatchingCondition[] -> {
          System.out.println(matching);
          collectVariable(variableCond, matching);                      
          }       
          eq@EqualityCondition[] -> { 
          System.out.println(eq);
          collectVariable(variableCond, eq);  
          }
          }
          }
          }*/
      collectVariable(variableCond, condList);
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
        messageError(orgTrack.getLine(),
                     TomMessage.getString("UnknownVariable"),
                     new Object[]{rhsSet.toString()});
      }
        // case of rhs is a single variable
      %match (TomTerm rhs) {
        Term(Variable[astName=Name(name)]) -> {
          String methodName = "";
          %match(TomTerm lhs) {
            Term(Appl[nameList=(Name(name1))]) -> {
              methodName = `name1;
            }
            Term(RecordAppl[nameList=(Name(name1))]) -> {
              methodName = `name1;
            }
          }
          TomType typeRhs = getSymbolCodomain(getSymbol(methodName));
          Iterator it = variableLhs.iterator();
          while(it.hasNext()) {
            TomTerm term = (TomTerm)it.next();
            if(term.getAstName().getString() == `name) {
              TomType typeLhs = term.getAstType();
              if(typeLhs != typeRhs) {
                messageError(orgTrack.getLine(),
                             TomMessage.getString("BadVariableType"),
                             new Object[]{`name, typeRhs.getTomType().getString(), typeLhs.getTomType().getString()});
              }
            }
          }
        }
      }
      list = list.getTail();
    }
  } //verifyRuleVariable
  
  private HashSet verifyVariableType(ArrayList list) {
      // compute multiplicities
    HashSet set = new HashSet();
    HashMap map = new HashMap();
    Iterator it = list.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
       
      if(set.contains(name.getString())) {
        TomTerm var = (TomTerm)map.get(name);
        TomType type = var.getAstType();
        TomType type2 = variable.getAstType();
        if(!(type==type2)) {
          messageError(findOriginTrackingLine(variable.getOption()),
                       TomMessage.getString("IncoherentVariable"),
                       new Object[]{name.getString(), type.getTomType().getString(), type2.getTomType().getString()});
        }
      } else {
        map.put(name, variable);
        set.add(name.getString());
      }
    }
    return set;
  }  //verifyVariableType
  
    /**
     * Syntax checking entry point:
     * Catch and verify all type and operator declaration,
     * Match and RuleSet instructions
     */
  protected void checkSyntax(TomTerm parsedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm subject) {
          if(subject instanceof TomTerm) {
            %match(TomTerm subject) {
              DeclarationToTomTerm(declaration) -> {
                `verifyDeclaration(declaration);
                return false;
              }
            }
          } else if(subject instanceof Instruction) {
            %match(Instruction subject) {
              Match(SubjectList(matchArgsList), PatternList(patternActionList), list) -> {  
                `verifyMatch(matchArgsList, patternActionList, list);
                return false;
              }
              RuleSet(list, orgTrack) -> {
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
  
    /**
     *  SYMBOL AND TYPE CONCERNS 
     */
  private void verifyDeclaration(Declaration declaration) { 
    %match (Declaration declaration) {
        // Types
      TypeTermDecl(Name(tomName), tomList, orgTrack) -> {
        `verifyTypeDecl(TYPE_TERM, tomName, tomList, orgTrack);
      }
      TypeListDecl(Name(tomName), tomList, orgTrack) -> {
        `verifyTypeDecl(TYPE_LIST, tomName, tomList, orgTrack);
      }
      TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
        `verifyTypeDecl(TYPE_ARRAY, tomName, tomList, orgTrack);
      }
        // Symbols
      SymbolDecl(Name(tomName))      -> { `verifySymbol(CONSTRUCTOR, getSymbol(tomName)); }
      ArraySymbolDecl(Name(tomName)) -> { `verifySymbol(OP_ARRAY, getSymbol(tomName)); }
      ListSymbolDecl(Name(tomName))  -> { `verifySymbol(OP_LIST, getSymbol(tomName)); }
    
    }
  } //verifyDeclaration

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
    
    if(declType == TYPE_TERM) {
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
      (_*, DeclarationToTomTerm(d), _*) -> { // for each Declaration
        Declaration decl=`d;
        %match(Declaration decl) {
            // Common Macro functions
          GetFunctionSymbolDecl[orgTrack=orgTrack] -> {
            `checkField(GET_FUN_SYM,verifyList,orgTrack, declType);
          }
          CompareFunctionSymbolDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
            `checkFieldAndLinearArgs(CMP_FUN_SYM,verifyList,orgTrack,name1,name2, declType);
          }
          TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
            `checkFieldAndLinearArgs(EQUALS,verifyList,orgTrack,name1,name2, declType);
          }
            // Term specific Macro functions
          GetSubtermDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> {
            `checkFieldAndLinearArgs(GET_SUBTERM,verifyList,orgTrack,name1,name2, declType);
          }
            // List specific Macro functions
          GetHeadDecl[orgTrack=orgTrack] -> {
            `checkField(GET_HEAD,verifyList,orgTrack, declType);
          }
          GetTailDecl[orgTrack=orgTrack] -> {
            `checkField(GET_TAIL,verifyList,orgTrack, declType);
          }
          IsEmptyDecl[orgTrack=orgTrack] -> {
            `checkField(IS_EMPTY,verifyList,orgTrack, declType);
          }
            // Array specific Macro functions
          GetElementDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack) -> { 
            `checkFieldAndLinearArgs(GET_ELEMENT,verifyList,orgTrack,name1,name2, declType);
          }
          GetSizeDecl[orgTrack=orgTrack] -> {
            `checkField(GET_SIZE,verifyList,orgTrack, declType);
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
  } //verifyTypeDecl
   
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
  } //verifySymbol

  private void verifySymbolCodomain(String codomain, String symbName, String symbolType) {
    if(!testTypeExistence(codomain)) {
      messageError(currentTomStructureOrgTrack.getLine(), 
                   symbolType+" "+symbName, 
                   TomMessage.getString("SymbolCodomainError"),
                   new Object[]{symbName, codomain});
    }
  } //verifySymbolCodomain
   
  private int verifySymbolDomain(TomTypeList args, String symbName, String symbolType) {
    int position = 1;
    if(symbolType == CONSTRUCTOR) {
      %match(TomTypeList args) {
        (_*,  TomTypeAlone(typeName),_*) -> { // for each symbol types
          if(!testTypeExistence(`typeName)) {
            messageError(currentTomStructureOrgTrack.getLine(), 
                         symbolType+" "+symbName, 
                         TomMessage.getString("SymbolDomainError"),
                         new Object[]{new Integer(position), symbName, `typeName});
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
                         TomMessage.getString("ListSymbolDomainError"),
                         new Object[]{symbName, `typeName});
          }
        }
      } //match
      return 1;
    }
  } //verifySymbolDomain
  
  private boolean testTypeExistence(String typeName) {
    return symbolTable().getType(typeName) != null;
  } //testTypeExistence
  
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
      (_*, DeclarationToOption(d), _*) -> { // for each Declaration
        Declaration decl=`d;
        %match(Declaration decl ) {
            // for a array symbol 
          MakeEmptyArray[orgTrack=orgTrack] -> { 
            `checkField(MAKE_EMPTY,verifyList,orgTrack, symbolType);
          }
          MakeAddArray[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
            `checkFieldAndLinearArgs(MAKE_APPEND, verifyList, orgTrack, name1, name2, symbolType);
          }
            // for a List symbol
          MakeEmptyList[orgTrack=orgTrack] -> {
            `checkField(MAKE_EMPTY,verifyList,orgTrack, symbolType);         
          }
          MakeAddList[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack] -> {
            `checkFieldAndLinearArgs(MAKE_INSERT, verifyList, orgTrack, name1, name2, symbolType);
          }
            // for a symbol
          MakeDecl[args=makeArgsList, orgTrack=orgTrack] -> {
            if (!foundOpMake) {
              foundOpMake = true;
              `verifyMakeDeclArgs(makeArgsList, domainLength, orgTrack, symbolType);
            } else {
              messageError(`orgTrack.getLine(), 
                           symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                           TomMessage.getString("MacroFunctionRepeated"),
                           new Object[]{MAKE});
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
                       TomMessage.getString("NonLinearMacroFunction"),
                       new Object[]{MAKE, `name});
        } else {
          listVar.add(`name);
        }
        nbArgs++;
      }
    }
    if(nbArgs != domainLength) {
      messageError(orgTrack.getLine(), 
                   symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.getString("BadMakeDefinition"),
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
                   TomMessage.getString("MultipleSymbolDefinitionError"),
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
                   TomMessage.getString("MacroFunctionRepeated"),
                   new Object[]{function});
    }
  } //checkField
   
  private void checkFieldAndLinearArgs(String function, ArrayList foundFunctions, Option orgTrack, String name1, String name2, String symbolType) {
    checkField(function,foundFunctions, orgTrack, symbolType);
    if(name1.equals(name2)) {
      messageError(orgTrack.getLine(), 
                   symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(), 
                   TomMessage.getString("NonLinearMacroFunction"),
                   new Object[]{function, name1});
    }
  } //checkFieldAndLinearArgs
   
  private void messageMissingMacroFunctions(String symbolType, ArrayList list) {
    String listOfMissingMacros = "";
    for(int i=0;i<list.size();i++) {
      listOfMissingMacros+= list.get(i)+",  ";
    }
    listOfMissingMacros = listOfMissingMacros.substring(0, listOfMissingMacros.length()-3);
    messageError(currentTomStructureOrgTrack.getLine(), 
                 symbolType+" "+currentTomStructureOrgTrack.getAstName().getString(),
                 TomMessage.getString("MissingMacroFunctions"),
                 new Object[]{listOfMissingMacros});
  } //messageMissingMacroFunctions
   
    /** 
     * MATCH VERIFICATION CONCERNS 
     */
    // Given a MatchConstruct's subject list and pattern-action list
  private void verifyMatch(TomList subjectList, TomList patternList, OptionList list) {
    currentTomStructureOrgTrack = findOriginTracking(list);
    ArrayList typeMatchArgs = new ArrayList(), nameMatchArgs = new ArrayList();
      // From the subjects list(match definition), we test each used type and keep them in memory
    %match(TomList subjectList) {
      concTomTerm(_*, TLVar(name, tomType@TomTypeAlone(type)), _*) -> { // for each Match args
        if (!testTypeExistence(`type)) {
          messageError(currentTomStructureOrgTrack.getLine(),
                       TomMessage.getString("UnknownMatchArgumentTypeInSignature"),
                       new Object[]{`name, `type});
          typeMatchArgs.add(null);
        } else {
          typeMatchArgs.add(`tomType);
        }
        if(nameMatchArgs.indexOf(`name) == -1) {
          nameMatchArgs.add(`name);
        } else {
            // Maybe its an error to have the 2 same name variable in the match definition: warn the user
          messageWarning(currentTomStructureOrgTrack.getLine(),
												 TomMessage.getString("RepeatedMatchArgumentName"),
                         new Object[]{`name});
        }
      } 
    }
    int nbExpectedArgs = typeMatchArgs.size();
      // we now compare pattern to its definition
    %match(TomList patternList) {
      concTomTerm(_*, PatternAction[termList=TermList(terms)], _*) -> { // control each pattern vs the match definition
        `verifyMatchPattern(terms, typeMatchArgs, nbExpectedArgs);
      }
    }
  }
  
    // each patternList shall have the expected length and each term shall be valid
  private void verifyMatchPattern(TomList termList, ArrayList typeMatchArgs, int nbExpectedArgs) {
    int nbFoundArgs = termList.getLength();
    if(nbFoundArgs != nbExpectedArgs) {
      messageError(findOriginTrackingLine(termList.getHead().getOption()),
                   TomMessage.getString("BadMatchNumberArgument"),
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
                       TomMessage.getString("IncorrectVariableStarInMatch"), 
                       new Object[]{termDesc.name});
        } else {    // Analyse of the term if expectedType != null  
          expectedType = (TomType)typeMatchArgs.get(counter);
          if (expectedType != null) { // the type is known
            validateTerm(`term, expectedType, false, true, false);
          }
        }
        counter++;
      }
    }
  }

    /** 
     * RULE VERIFICATION CONCERNS 
     */
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
                   TomMessage.getString("IncorrectRuleLHSClass"), new Object[]{termName});
      return null;
    }
    
    currentHeadSymbolName = getName(lhs);
    if(ruleNumber == 0) {
        // update the root of lhs: it becomes a defined symbol
      symbol = ast().updateDefinedSymbol(symbolTable(),lhs);
      if( symbol == null ) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.getString("UnknownSymbol"), 
                     new Object[]{currentHeadSymbolName});
          // We can not continue anymore
        return null;
      }
        //ensure we are able to construct this symbol
      if ( !findMakeDecl(symbol.getOption())) {
        messageError(findOriginTrackingLine(lhs.getOption()),
                     TomMessage.getString("NoRuleMakeDecl"), 
                     new Object[]{currentHeadSymbolName});
      }
      
      if(alreadyStudiedRule.contains(currentHeadSymbolName)) {
        messageError(currentTomStructureOrgTrack.getLine(),
                     TomMessage.getString("MultipleRuleDefinition"),
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
                     TomMessage.getString("DifferentRuleConstructor"), 
                     new Object[]{headSymbolName, currentHeadSymbolName}); 
      }
    }
    symbol = getSymbol(currentHeadSymbolName);
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
  

    //Rhs shall have no underscore, be a var* nor _*, nor a RecordAppl
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
                   TomMessage.getString("IncorrectRuleRHSClass"), new Object[]{termName});
      return;
    }
    
    TomSymbol symbol = getSymbol(lhsHeadSymbolName);
    TomType lhsType = getSymbolCodomain(symbol);
    TermDescription termDesc = validateTerm(rhs, lhsType, isListOperator(symbol)||isArrayOperator(symbol), true, true);
    TomType rhsType = termDesc.tomType;
    if(rhsType != null) {
      if(rhsType != lhsType) {
        String rhsTypeName;
        if(rhsType.isEmptyType()) {rhsTypeName = "Not Type Found";} else {rhsTypeName = rhsType.getString();}
        messageError(findOriginTrackingLine(rhs.getOption()), 
                     TomMessage.getString("IncorrectRuleRHSType"), new Object[]{rhsTypeName, lhsType.getString()});
      }
    }
  }

    // Analyse a term given an expected type and re-enter recursively on childs
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
          if (listOp) {
              // whatever the arity is, we continue recursively and there is only one element in the Domain
            validateListOperatorArgs(args, symbol.getTypesToType().getDomain().getHead(),permissive);
          } else {
              // the arity is important also there are different types in Domain
            TomTypeList  types = symbol.getTypesToType().getDomain();
            int nbArgs = args.getLength();
            int nbExpectedArgs = types.getLength();
            if(nbArgs != nbExpectedArgs) {
              messageError(decLine, TomMessage.getString("SymbolNumberArgument"), 
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
            messageError(findOriginTrackingLine(`options), TomMessage.getString("IncorrectRuleRHSClass"), 
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
          type = getSymbolCodomain(getSymbol(Constants.ELEMENT_NODE));
          termName = Constants.ELEMENT_NODE;
          
          TomList args = `childList;
          /*
           * we cannot use the following expression
           *   TomType TNodeType = symbolTable().getType(Constants.TNODE);
           * because TNodeType should be a TomTypeAlone and not an expanded type
           */
          TomType TNodeType = getSymbolCodomain(symbolTable().getSymbol(Constants.ELEMENT_NODE));
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
            messageError(decLine, TomMessage.getString("IncorrectRuleRHSClass"), 
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
            messageError(decLine, TomMessage.getString("InvalidVariableStarArgument"), 
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
            messageError(decLine, TomMessage.getString("InvalidVariableStarArgument"), 
                         new Object[]{termName});
          }
          if(permissive) {
            messageError(decLine, TomMessage.getString("IncorrectRuleRHSClass"), 
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
  
  public void validateListOperatorArgs(TomList args, TomType expectedType, boolean permissive) {
    while(!args.isEmpty()) {
      validateTerm(args.getHead(), expectedType, true, false, permissive);
      args = args.getTail();
    }
  }

  public int getClass(TomTerm term) {
    %match(TomTerm term) {
      Appl[nameList=(Name(""))] -> { return UNAMED_APPL;}
      Appl[nameList=(Name(_))] -> { return APPL;}
      Appl[nameList=(Name(_), _*)] -> { return APPL_DISJUNCTION;}
      RecordAppl[nameList=(Name(_))] -> { return RECORD_APPL;}
      RecordAppl[nameList=(Name(_), _*)] -> { return RECORD_APPL_DISJUNCTION;}
      XMLAppl[] -> { return XML_APPL;}
      Placeholder[] -> { return PLACE_HOLDER;}
      VariableStar[] -> { return VARIABLE_STAR;}
      UnamedVariableStar[] -> { return UNAMED_VARIABLE_STAR;}
      _ -> {throw new TomRuntimeException("Invalid Term");}
    }
  }
  
  public String getName(TomTerm term) {
    String dijunctionName = "";
    %match(TomTerm term) {
      Appl[nameList=(Name(name))] -> { return `name;}
      Appl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      RecordAppl[nameList=(Name(name))] -> { return `name;}
      RecordAppl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      XMLAppl[nameList=(Name(name), _*)] ->{ return `name;}
      XMLAppl[nameList=nameList] -> {
        String head;
        dijunctionName = `nameList.getHead().getString();
        while(!`nameList.isEmpty()) {
          head = `nameList.getHead().getString();
          dijunctionName = ( dijunctionName.compareTo(head) > 0)?dijunctionName:head;
          `nameList = `nameList.getTail();
        }
        return dijunctionName;
      }
      Placeholder[]               ->{ return "_";}
      VariableStar[astName=Name(name)]              ->{ return `name+"*";}
      UnamedVariableStar[] ->{ return "_*";}
      _                               ->{throw new TomRuntimeException("Invalid Term");}
    }
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
                                       getSymbolCodomain(getSymbol(`str)));
          }
        }
        Appl[option=options, nameList=(Name(name), _*)] -> {
          return new TermDescription(APPL_DISJUNCTION, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbol(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name))] ->{
          return new TermDescription(RECORD_APPL, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbol(`name)));
        }
        RecordAppl[option=options,nameList=(Name(name), _*)] ->{
          return new TermDescription(RECORD_APPL_DISJUNCTION, `name, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbol(`name)));
        }
        XMLAppl[option=options] -> {
          return new TermDescription(XML_APPL, Constants.ELEMENT_NODE, findOriginTrackingLine(`options), 
                                     getSymbolCodomain(getSymbol(Constants.ELEMENT_NODE)));
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
    SymbolList symbolList = symbolTable().getSymbol(expectedType);
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
                   TomMessage.getString("UnknownUnamedList"),
                   new Object[]{expectedType.getString()});
      return null;
    } else if(!filteredList.getTail().isEmpty()) {
      String symbolsString = "";
      while(!filteredList.isEmpty()) {
        symbolsString += " "+filteredList.getHead().getAstName().getString();
        filteredList= filteredList.getTail();
      }
      messageError(decLine,
                   TomMessage.getString("AmbigousUnamedList"),
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
      symbol  =  getSymbol(res);
      if (symbol == null ) {
        if((constructor || !emptyChilds)) {
            // this correspond to aterm like 'unknown()' or unknown(s1, s2, ...)
          if(!permissive) {
            messageError(decLine,
                         TomMessage.getString("UnknownSymbol"),
                         new Object[]{res});
          } else {
            messageWarning(decLine,
                         TomMessage.getString("UnknownPermissiveSymbol"),
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
                         TomMessage.getString("AmbigousSymbolWithoutConstructor"),
                           new Object[]{res});
          }
        }
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getString("InvalidCodomain"), res, decLine)) {
            return null;
          }
        }
      }
      return symbol;
    }
      //  this is a disjunction
    if(permissive) { 
      messageError(decLine, 
                   TomMessage.getString("ImpossiblePermissiveAndDisjunction"),
                   new Object[]{});
    }
   
    // this part is common between Appl and records with multiple head symbols
    boolean first = true; // the first symbol give the expected type
    %match(NameList nameList) {
      (_*, Name(dijName), _*) -> { // for each SymbolName
        symbol =  getSymbol(`dijName);
        if (symbol == null) {
            // In disjunction we can only have known symbols
          messageError(decLine,
                       TomMessage.getString("UnknownSymbolInDisjunction"),
                       new Object[]{`dijName});
          return null;
        }
        if ( strictType  || !topLevel ) {
            // ensure codomain is correct
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getString("InvalidDisjunctionCodomain"), `dijName, decLine)) {
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
                         TomMessage.getString("InvalidDisjunctionDomain"),
                         new Object[]{`dijName});
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
      TomSymbol symbol =  getSymbol(res);
      if (symbol == null ) { // this correspond to: unknown[]
          // it is not correct to use Record an unknown symbols
        messageError(decLine,
                     TomMessage.getString("UnknownSymbol"),
                     new Object[]{res});
        return null;    
      } else { // known symbol
          // ensure type correctness if necessary
        if ( strictType  || !topLevel ) {
          if (!ensureSymbolCodomain(getSymbolCodomain(symbol), expectedType, TomMessage.getString("InvalidCodomain"), res, decLine)) {
            return null;
          }
        }
      }
      return symbol;
    } else {
      return ensureValidApplDisjunction(nameList, expectedType, decLine, false, false, false, topLevel);
    }
  }
    /**
     * RECORDS CONCERNS
     */
  private void verifyRecordStructure(OptionList option, String tomName, TomList slotTermPairs, int decLine)  {
    TomSymbol symbol = getSymbol(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotTermPairs.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)) ) {
        messageError(decLine,
                     TomMessage.getString("BracketOnListSymbol"),
                     new Object[]{tomName});
      }
        // TODO verify type
      verifyRecordSlots(slotTermPairs,slotList, getSymbolDomain(symbol), tomName, decLine);
    } else {
      messageError(decLine,
                   TomMessage.getString("UnknownSymbol"),
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
                     TomMessage.getString("BadSlotName"), 
                     new Object[]{pairSlotName.getString(), methodName, listOfPossibleSlot.toString()});
        return; //break analyses
      } else { // then check for repeated good slot name
        Integer integerIndex = new Integer(index);
        if(studiedSlotIndexList.contains(integerIndex)) {
            // Error: repeated slot
          messageError(decLine,
                       TomMessage.getString("SlotRepeated"),
                       new Object[]{methodName, pairSlotName.getString()});
          return; //break analyses
        }
        studiedSlotIndexList.add(integerIndex);
      }
      
        // Now analyses associated term 
      SlotList listOfSlots = slotList;
      TomTypeList listOfTypes = typeList;
      TomList listOfPair = pairList;
      while(!listOfSlots.isEmpty()) {
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
  
    /**
     * Message Functions
     */

  private void messageError(int errorLine, String msg, Object[] msgArg) {
    String structName = currentTomStructureOrgTrack.getAstName().getString();
    messageError(errorLine, structName, msg, msgArg);
  }
  
  private void messageError(int errorLine, String structInfo, String msg, Object[] msgArgs) {
    String fileName = currentTomStructureOrgTrack.getFileName().getString();
    int structDeclLine = currentTomStructureOrgTrack.getLine();
    environment().messageError(errorLine, fileName, structInfo, structDeclLine, msg, msgArgs);
  }

  private void messageWarning(int errorLine, String msg, Object[] msgArg) {
    String structName = currentTomStructureOrgTrack.getAstName().getString();
    messageWarning(errorLine, structName, msg, msgArg);
  }
  
  private void messageWarning(int errorLine, String structInfo, String msg, Object[] msgArgs) {
    String fileName = currentTomStructureOrgTrack.getFileName().getString();
    int structDeclLine = currentTomStructureOrgTrack.getLine();
    environment().messageWarning(errorLine, fileName, structInfo, structDeclLine, msg, msgArgs);
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
      concOption(_*,OriginTracking[line=line],_*) -> { return `line; }
    }
    return -1;
  }

  private void ensureOriginTrackingLine(int line) {
    if(line < 0) {
      environment().messageError("findOriginTrackingLine:  not found", environment().getInputFile().getName(), TomMessage.DEFAULT_ERROR_LINE_NUMBER);
      System.out.println("findOriginTrackingLine: not found ");
    }
  }
}  //Class TomChecker
