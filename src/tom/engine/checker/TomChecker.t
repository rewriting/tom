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
import java.util.Map;

import jtom.TomBase;
import jtom.adt.Declaration;
import jtom.adt.Option;
import jtom.adt.OptionList;
import jtom.adt.SlotList;
import jtom.adt.TargetLanguage;
import jtom.adt.TomList;
import jtom.adt.TomName;
import jtom.adt.TomRule;
import jtom.adt.TomRuleList;
import jtom.adt.TomSymbol;
import jtom.adt.TomTerm;
import jtom.adt.TomType;
import jtom.adt.TomTypeList;
import jtom.runtime.Collect1;
import jtom.tools.TomTask;
import aterm.ATerm;
import jtom.TomEnvironment;
import jtom.exception.CheckErrorException;

abstract class TomChecker extends TomBase implements TomTask {
  
  private ArrayList alreadyStudiedSymbol =  new ArrayList();
  private ArrayList alreadyStudiedType =  new ArrayList();  
  protected Option currentTomStructureOrgTrack;
  private int nullInteger = -1;
  private List errorMessage = new ArrayList();
  protected TomTask nextTask;
  protected boolean strictType = false, warningAll = false, noWarning = false;
  
  public TomChecker(TomEnvironment environment) { 
    super(environment);
  }
  
    // ------------------------------------------------------------
  %include { Tom.signature }
    // ------------------------------------------------------------
  
  public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  
  public TomTask getTask() {
    return nextTask;
  }
  
  public int getNumberFoundError() {
    return errorMessage.size();
  }
  
  public String getMessage(int n) {
    return (String)errorMessage.get(n);
  }
  
     // Main syntax checking entry point: We check all interesting Tom Structure
  protected void syntaxCheck(TomTerm parsedTerm) {
    Collect1 collectAndVerify = new Collect1() {  
        public boolean apply(ATerm term) {
          if(term instanceof TomTerm) {
            %match(TomTerm term) {
              Match(SubjectList(matchArgsList), PatternList(patternActionList), Option(list)) -> {  
                currentTomStructureOrgTrack = findOriginTracking(list);
                verifyMatch(matchArgsList, patternActionList);
                return true;
              }
              DeclarationToTomTerm(declaration) -> {
                verifyDeclaration(declaration);
                return false;
              }
              Appl(Option(options),Name(name),args) -> {
                verifyApplStructure(options, name, args);
                return true;
              }
              RecordAppl(Option(options),Name(name),args) ->{
                verifyRecordStructure(options, name, args);
                return true;
              }
              RuleSet(list, orgTrack) -> {
                currentTomStructureOrgTrack = orgTrack;
                verifyRule(list);
                return false;
              }
              backQuoteTerm@BackQuoteAppl(Option(options),Name(name),args) -> {
                permissiveVerify(backQuoteTerm);
                return false;
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
  
    // Main type checking entry point: We check all interesting Tom Structure
  protected void typeCheck(TomTerm expandedTerm) {
    Collect1 collectAndVerify = new Collect1() 
      {  
        public boolean apply(ATerm term) {
          if(term instanceof TomTerm) {
            %match(TomTerm term) {
              Match(_, PatternList(list), Option(oplist)) -> {  
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
      Option orgTrack = findOriginTracking(rewriteRule.getOption().getOptionList());
      
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
          TomType typeRhs = getSymbolCodomain(symbolTable().getSymbol(methodName));
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
          messageErrorIncoherentVariable(name.getString(), type.getTomType().getString(), type2.getTomType().getString(), variable.getOption().getOptionList()); 
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
  
  private void permissiveVerify(TomTerm term) {
    Collect1 permissiveCollectAndVerify = new Collect1() 
      {  
        public boolean apply(ATerm term) {
          if(term instanceof TomTerm) {
            %match(TomTerm term) {
              BackQuoteAppl(Option(options),Name(name),args) -> {
                permissiveVerifyApplStructure(options, name, args);
                return true;
              }
              Appl(Option(options),Name(name),args) -> {
                permissiveVerifyApplStructure(options, name, args);
                return true;
              }
              RecordAppl[option=Option(options), astName=Name(name)] ->{
                messageRuleErrorRhsImpossibleRecord(options, name);
                return true;
              }
              Placeholder(Option(orgTrack)) -> {
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
  
    /////////////////////////////////
    // MATCH VERIFICATION CONCERNS //
    /////////////////////////////////
  
    // Given a subject list, we test types in match signature
    // and then number and type of slots found in each pattern
  private void verifyMatch(TomList subjectList, TomList patternList) {
    ArrayList typeMatchArgs = new ArrayList();
    while( !subjectList.isEmpty() ) {
      TomTerm term = subjectList.getHead();
      %match( TomTerm term ) {
        TLVar(name, TomTypeAlone(type)) -> {
          if(symbolTable().getType(type) == null) {
            messageMatchTypeVariableError(name, type);
          }
          typeMatchArgs.add(type);
        }
      }
      subjectList = subjectList.getTail();
    }
    
    while(!patternList.isEmpty()) {
      statistics().numberMatchesTested++;
      TomTerm terms = patternList.getHead();
      verifyMatchPattern(terms, typeMatchArgs);
      patternList = patternList.getTail();
    }
  }
    // For each Pattern we count and collect type information
    // but also we test that terms are well formed
    // No top variable star are allowed
  private void verifyMatchPattern(TomTerm pattern, ArrayList typeMatchArgs) {
    int nbExpectedArgs = typeMatchArgs.size();
    TomList termList = pattern.getTermList().getTomList();
    ArrayList foundTypeMatch = new ArrayList();
    int line = nullInteger;
    int nbFoundArgs = 0;
    
    while( !termList.isEmpty() ) {
      TomTerm termAppl = termList.getHead();
      
      %match(TomTerm termAppl) {
        Appl[astName=Name[string=name], option=Option(list)] -> {
          line = findOriginTrackingLine(list);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
        }
        Placeholder[option=Option(list)] -> {
          line = findOriginTrackingLine(list);
          nbFoundArgs++;
          foundTypeMatch.add((TomTerm) null);
        }
          // alone stared variable is impossible
        VariableStar[option=Option(list), astName=Name[string=name]] -> { 
          line = findOriginTrackingLine(name,list);
          nbFoundArgs++;
          foundTypeMatch.add((TomTerm) null);
          messageMatchErrorVariableStar(name, line); 
        }
        RecordAppl(Option(options),Name(name),args) ->{
          line = findOriginTrackingLine(options);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
        }
      }
      termList = termList.getTail();
    }
      // nb elements in %match subject = nb elements in the pattern-action ?
    if(nbExpectedArgs != nbFoundArgs) {
      messageMatchErrorNumberArgument(nbExpectedArgs, nbFoundArgs, line);
      return;
    }
      // we test the type egality between arguments and pattern-action,
      // if it is not a variable  => type is null
    if(!strictType) return;
    for( int slot = 0; slot < nbExpectedArgs; slot++ ) {
      if ( (foundTypeMatch.get(slot) !=  typeMatchArgs.get(slot)) && (foundTypeMatch.get(slot) != null))
      { 	
        messageMatchErrorTypeArgument(slot+1, (String) typeMatchArgs.get(slot), (String) foundTypeMatch.get(slot), line); 
      }
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
    String s = "Single list variable '"+nameVariableStar+"*' is not allowed on left most part of %match structure declared line "+declLine;
    messageError(line,s);
  }
  
    //////////////////////////////
    // SYMBOL AND TYPE CONCERNS //
    //////////////////////////////
  
  private void verifyDeclaration(Declaration declaration) {
    %match (Declaration declaration) {
      SymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%op", tomSymbol);
      }
      ArraySymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oparray", tomSymbol);
      }
      ListSymbolDecl(Name(tomName)) -> {
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oplist", tomSymbol);
      }
      TypeTermDecl(Name(tomName), tomList, orgTrack) -> {
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typeterm", tomList);
      }
      
      TypeListDecl(Name(tomName), tomList, orgTrack) -> {
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typelist", tomList);
      }
      
      TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typearray", tomList);
      }
    }
  }
  
    ///////////////////////////////
    // TYPE DECLARATION CONCERNS //
    ///////////////////////////////
  private void verifyTypeDecl(String declType, TomList listOfDeclaration) {
    statistics().numberTypeDefinitonsTested++;
    ArrayList verifyList = new ArrayList();
    verifyList.add("get_fun_sym");
    verifyList.add("cmp_fun_sym");
    verifyList.add("equals");
    if(declType == "%typeterm")
    {
      verifyList.add("get_subterm");
    } else if(declType == "%typearray") {
      verifyList.add("get_element");
      verifyList.add("get_size");
    }
    else if(declType == "%typelist") {
      verifyList.add("get_head");
      verifyList.add("get_tail");
      verifyList.add("is_empty");
    }
    else {
      System.out.println("Invalid verifyTypeDecl parameter: "+declType);
      //throw new CheckErrorException();
    }
    
    while(!listOfDeclaration.isEmpty()) {
      TomTerm term = listOfDeclaration.getHead();
      %match (TomTerm term) {
        DeclarationToTomTerm(GetFunctionSymbolDecl[orgTrack=orgTrack]) -> {
          checkField("get_fun_sym",verifyList,orgTrack, declType);
        }
        DeclarationToTomTerm(CompareFunctionSymbolDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
          checkFieldAndLinearArgs("cmp_fun_sym",verifyList,orgTrack,name1,name2, declType);
        }
        DeclarationToTomTerm(TermsEqualDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
          checkFieldAndLinearArgs("equals",verifyList,orgTrack,name1,name2, declType);
        }
          /*Specific to typeterm*/
        DeclarationToTomTerm(GetSubtermDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> {
          checkFieldAndLinearArgs("get_subterm",verifyList,orgTrack,name1,name2, declType);
        }
          /*Specific to typeList*/
        DeclarationToTomTerm(GetHeadDecl[orgTrack=orgTrack]) -> {
          checkField("get_head",verifyList,orgTrack, declType);
        }
        DeclarationToTomTerm(GetTailDecl[orgTrack=orgTrack]) -> {
          checkField("get_tail",verifyList,orgTrack, declType);
        }
        DeclarationToTomTerm(IsEmptyDecl[orgTrack=orgTrack]) -> {
          checkField("is_empty",verifyList,orgTrack, declType);
        }
          /*Specific to typeArray*/
        DeclarationToTomTerm(GetElementDecl(Variable[astName=Name(name1)],Variable[astName=Name(name2)],_, orgTrack)) -> { 
          checkFieldAndLinearArgs("get_element",verifyList,orgTrack,name1,name2, declType);
        }
        DeclarationToTomTerm(GetSizeDecl[orgTrack=orgTrack]) -> {
          checkField("get_size",verifyList,orgTrack, declType);
        }
      }
      listOfDeclaration = listOfDeclaration.getTail();
    }
    
    if(verifyList.contains("equals")) {
      verifyList.remove(verifyList.indexOf("equals"));
    }    
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList);
    }
  }
  
  private void verifyMultipleDefinitionOfType(String name) {
    if(alreadyStudiedType.contains(name)) {
      messageTypeErrorYetDefined(name);
    }
    else {
      alreadyStudiedType.add(name);
    }
  }
  
  private void messageTypeErrorYetDefined(String name) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Multiple definition of type: Type '"+ name +"' is already defined";
    messageError(declLine, s);
  }
  
    /////////////////////////////////
    // SYMBOL DECLARATION CONCERNS //
    /////////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol){
    int nbArgs=0;
    OptionList optionList = tomSymbol.getOption().getOptionList();
      // We save first the origin tracking of the symbol declaration
    currentTomStructureOrgTrack = findOriginTracking(optionList);
    TomTypeList l = getSymbolDomain(tomSymbol);
    TomType type = getSymbolCodomain(tomSymbol);
    String name = tomSymbol.getAstName().getString();
    int line  = findOriginTrackingLine(optionList);
    SlotList slotList = tomSymbol.getSlotList();
    verifyMultipleDefinitionOfSymbol(name, line);
    verifySymbolCodomain(type.getString(), name, line);
    verifySymbolArguments(l, name, line);
    verifySymbolOptions(symbolType, optionList);
  }
  
  private void verifyMultipleDefinitionOfSymbol(String name, int line) {
    if(alreadyStudiedSymbol.contains(name)) {
      messageOperatorErrorYetDefined(name,line);
    }
    else {
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
    statistics().numberOperatorDefinitionsTested++;
    ArrayList verifyList = new ArrayList();
    boolean foundOpMake = false;
    if(symbType == "%op"){
    } else if(symbType == "%oparray" ) {
      verifyList.add("make_empty");
      verifyList.add("make_append");
    }
    else if(symbType == "%oplist") {
      verifyList.add("make_empty");
      verifyList.add("make_insert"); 
    }
    else {
      System.out.println("Invalid verifySymbolOptions parameter: "+symbType);
      //throw new CheckErrorException();
    }
    
    while(!list.isEmpty()) {
      Option term = list.getHead();
      %match(Option term ) {
          /* for a array symbol */
        DeclarationToOption(MakeEmptyArray[orgTrack=orgTrack]) -> { 
          checkField("make_empty",verifyList,orgTrack, symbType);
        }
        DeclarationToOption(MakeAddArray[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack]) -> {
          checkFieldAndLinearArgs("make_append", verifyList, orgTrack, name1, name2, symbType);
        }
          /*for a List symbol*/
        DeclarationToOption(MakeEmptyList[orgTrack=orgTrack]) -> {
          checkField("make_empty",verifyList,orgTrack, symbType);
          
        }
        DeclarationToOption(MakeAddList[varList=Variable[astName=Name(name1)], varElt=Variable[astName=Name(name2)], orgTrack=orgTrack]) -> {
          checkFieldAndLinearArgs("make_insert", verifyList, orgTrack, name1, name2, symbType);
        }
          /*for a symbol*/
        DeclarationToOption(MakeDecl[args=makeArgsList, orgTrack=orgTrack]) -> {
          if (!foundOpMake) {
            foundOpMake = true;
            verifyMakeDeclArgs(makeArgsList, orgTrack, symbType);
          }
          else {messageMacroFunctionRepeated("make", orgTrack, symbType);}
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
        Variable[option=Option(listOption), astName=Name(name)] -> {
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
  
    //////////////////////
    // RECORDS CONCERNS //
    //////////////////////
  
  private void verifyRecordStructure(OptionList option, String tomName, TomList args) {
    TomSymbol symbol = symbolTable().getSymbol(tomName);
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
      if ( !name.isEmptyName()) listOfPossibleSlot.add(name.getString());
      slotList = slotList.getTail();
    }
    int line = nullInteger;
    String s = "";
    %match( TomTerm pairSlotName ) {
      PairSlotAppl[slotName=Name(name),appl=Appl[option=Option(list)]] -> {
        line = findOriginTrackingLine(list);
        s += "Slot Name '" + name + "' is not correct: See method '"+methodName+ "' -  Line : "+line;
      }
    }
    s += "\nPossible slot names are : "+listOfPossibleSlot;
    messageError(line, s);
  }
  
  private void messageSlotRepeatedError(TomTerm pairSlotName, String methodName) {
    %match( TomTerm pairSlotName ) {
      PairSlotAppl(Name(name), Appl[option=Option(list)]) -> {
        int line = findOriginTrackingLine(list);
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

    //////////////////////
    // APPL CONCERNS    //
    //////////////////////
  
  private void verifyApplStructure(OptionList optionList, String name, TomList argsList) {
    statistics().numberApplStructuresTested++;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null  && (hasConstructor(optionList) || !argsList.isEmpty())) {
      messageSymbolError(name, optionList);
    } else {
      if(!argsList.isEmpty()) {
          //in case of oparray or oplist, the number of arguments is not tested
        boolean listOrArray =  (isListOperator(symbol) ||  isArrayOperator(symbol));
        int nbFoundArgs = 0;
        ArrayList foundType = new ArrayList();
        Map optionMap  = new HashMap();
          // we shall test also the type of each args
        while(!argsList.isEmpty()) {
          TomTerm term = argsList.getHead();
          %match(TomTerm term) {
            Appl[option=Option(options),astName=Name[string=name1]] -> {
              optionMap.put(new Integer(nbFoundArgs), options);
              nbFoundArgs++;
              foundType.add(extractType(symbolTable().getSymbol(name1)));
            }
            RecordAppl[option=Option(options),astName=Name[string=name1]] -> {
              optionMap.put(new Integer(nbFoundArgs), options);
              nbFoundArgs++;
              foundType.add(extractType(symbolTable().getSymbol(name1)));
            }
            VariableStar[] -> {nbFoundArgs++;foundType.add((TomTerm)null);}
            Placeholder[option=Option(options)] -> {
              if (listOrArray) {
                messageImpossiblePlaceHolderInListStructure(name,options);
              } else {
                nbFoundArgs++;foundType.add((TomTerm)null);
              }
            }
          }
          argsList =  argsList.getTail();
        }

        TomTypeList l = getSymbolDomain(symbol);
        if (!listOrArray) {
          int nbExpectedArgs = l.getLength();
            // We test the number of args vs its definition
          if (nbExpectedArgs != nbFoundArgs) {
            int line = findOriginTrackingLine(name,optionList);
            messageNumberArgumentsError(nbExpectedArgs, nbFoundArgs, name, line);
            return;
          }        
          for( int slot = 0; slot < nbExpectedArgs; slot++ ) {
            String s = getTomType(l.getHead());
            if ( (foundType.get(slot) != s) && (foundType.get(slot) != null))
            {
              int line = findOriginTrackingLine((OptionList) optionMap.get(new Integer(slot)));
              messageApplErrorTypeArgument(name, slot+1, s, (String) foundType.get(slot), line); 
            }
            l = l.getTail();
          }
        } else {
            // We worry only about returned type
          String s = getTomType(l.getHead());
          for( int slot = 0; slot <foundType.size() ; slot++ ) {
            if ( (foundType.get(slot) != s) && (foundType.get(slot) != null)) {
              int line = findOriginTrackingLine((OptionList) optionMap.get(new Integer(slot)));
              messageApplErrorTypeArgument(name, slot+1, s, (String) foundType.get(slot), line);
            }
          }
        }
      }
    }
  }

  private void  messageImpossiblePlaceHolderInListStructure(String listApplName, OptionList optionList) {
    int line = findOriginTrackingLine(optionList);
    String s = "Placeholder is not allowed in list operator '"+listApplName+"'";
    messageError(line,s);
  }
  
  private void  messageApplErrorTypeArgument(String applName, int slotNumber, String expectedType, String givenType, int line) {
    String s = "Bad type of argument: Argument "+slotNumber+" of method '" + applName + "' has type '"+expectedType+"' required but type '"+givenType+"' found";
    messageError(line,s);
  }
    
  private void permissiveVerifyApplStructure(OptionList optionList, String name, TomList argsList) {
    statistics().numberApplStructuresTested++;
    TomSymbol symbol = symbolTable().getSymbol(name);
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
          if ( !findMakeDeclOrDefSymbol(symbol.getOption().getOptionList()) ) {
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
        DefinedSymbol -> {return true;}
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

    ////////////////////////////////
    // RULE VERIFICATION CONCERNS //
    ////////////////////////////////
    /* for each rewrite rule we make test on: 
     * Rhs shall have no underscore, be a var*?, be a record?
     * Each Lhs shall start with the same production name
     * This symbol name shall not be already declared
     * Lhs and Rhs shall have the same return type
     * Used variable on rhs shall be coherent and declared on lhs
     */ 
  private void verifyRule(TomRuleList ruleList) {
    int i = 0;
    TomRule currentRule;
    String name = "Unknown";
    while(!ruleList.isEmpty()) {
      currentRule = ruleList.getHead();
      matchBlock: {
        %match(TomRule currentRule) {
          RewriteRule(Term(lhs),Term(rhs),condList,option) -> {
            statistics().numberRulesTested++;
            name = verifyLhsRuleAndConstructorEgality(lhs, name, i);
            if(i == 0) {
                // update the root of lhs: it becomes a defined symbol
              ast().updateDefinedSymbol(symbolTable(),lhs);
            }
            verifyRhsRuleStructure(rhs);
              // TODO: verify the list of conditions
            break matchBlock;
          }
          _             -> {
            System.out.println("Strange rule:\n" + currentRule);
            //throw new CheckErrorException();
          }
        }
      }
      ruleList = ruleList.getTail();
      i++;
    }
  }
  
  private String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  ruleName, int ruleNumber) {
    String methodName = "";
    OptionList options = tsf().makeOptionList();
    %match(TomTerm lhs) {
      appl@Appl(Option(optionList),Name(name), args) -> {
        // No alone variable nor simple constructor
        if( args.isEmpty() && !hasConstructor(optionList)) {
          int line = findOriginTrackingLine(optionList);
          messageRuleErrorVariable(name, line);
        }
        syntaxCheck(appl);
          // lhs outermost symbol shall have a corresponding make
        TomSymbol symb = symbolTable().getSymbol(name);
        if (symb == null) {
          messageRuleErrorUnknownSymbol(name);
          return methodName;
        }
        if ( !findMakeDeclOrDefSymbol(symbolTable().getSymbol(name).getOption().getOptionList())) {
          messageNoMakeForSymbol(name, optionList);
        }
        options = optionList;
        methodName = name;
      }
      VariableStar[option=Option(optionList), astName=Name(name1)] -> {
        int line = findOriginTrackingLine(name1,optionList);    
        messageRuleErrorVariableStar(name1, line);
        return ruleName;
      }
      Placeholder[option=Option(options2)] -> { 
        messageRuleErrorLhsImpossiblePlaceHolder(options2);
        return ruleName;
      }
      rec@RecordAppl(Option(optionList),Name(name),args) -> {
        syntaxCheck(rec);
        options = optionList;
        methodName = name;
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
    String s = "Symbol '" +symbolName+ "' has no been declared in rule declared line "+line;
    messageError(line,s); 
  }
  
  private void messageNoMakeForSymbol(String name, OptionList optionList) {
    int declLine = currentTomStructureOrgTrack.getLine();
    int line = findOriginTrackingLine(optionList);
    String s = "Symbol '" +name+ "' has no 'make' method associated in structure declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorVariable(String nameVariableStar, int line) {
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Alone variable " +nameVariableStar+ " is not allowed on left hand side of structure %rule declared line "+declLine;
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
    String s = "Alone placeholder is not allowed in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorConstructorEgality(String  name, String nameExpected, OptionList optionList) {
    int declLine = currentTomStructureOrgTrack.getLine();
    int line = findOriginTrackingLine(name, optionList);
    String s = "Left most symbol name '" + nameExpected + "' expected, but '" + name + "' found in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
  private void verifyRhsRuleStructure(TomTerm ruleRhs) {
    matchBlock: {
      %match(TomTerm ruleRhs) {
        appl@Appl(Option(options),Name(name),args) -> {
          permissiveVerify(appl);
          break matchBlock;
        }
        Placeholder[option=Option(option)] -> {
          messageRuleErrorRhsImpossiblePlaceholder(option);
          break matchBlock;
        }
        VariableStar[option=Option(option), astName=Name(name)] -> {
          messageRuleErrorRhsImpossibleVarStar(option, name);
          break matchBlock;
        }
        RecordAppl(Option(option), Name(tomName), _) -> {
          messageRuleErrorRhsImpossibleRecord(option, tomName);
          break matchBlock;
        }
        _ -> {
          System.out.println("Strange rule rhs:\n" + ruleRhs);
          //throw new CheckErrorException();
        }
      }
    }
  }

  private void messageRuleErrorRhsImpossiblePlaceholder(OptionList optionList) {
    int line = findOriginTrackingLine(optionList);
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Placeholder is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorRhsImpossibleRecord(OptionList optionList, String name) {
    int line = findOriginTrackingLine(optionList);
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Record '"+name+"[...]' is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  private void messageRuleErrorRhsImpossibleVarStar(OptionList optionList, String name) {
    int line = findOriginTrackingLine(optionList);
    int declLine = currentTomStructureOrgTrack.getLine();
    String s = "Single list variable '"+name+"*' is not allowed in right hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
    
    /////////////
    // GLOBALS //
    /////////////
  
  private String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }

  private void messageError(int line, String msg) {
    String s = "\n"+msg+"\n-- Error occured at line: " + line +" in file: "+currentTomStructureOrgTrack.getFileName().getString()+"\n";
    errorMessage.add(s);
  }
  
    // findOriginTrackingLine(_,_) method returns the line (stocked in optionList)  of object 'name'.
  private int findOriginTrackingLine(String name, OptionList optionList) {
    while(!optionList.isEmpty()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        OriginTracking[astName=Name[string=origName],line=line] -> {
          if(name.equals(origName)) {
            return line;
          }
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine: '" + name + "' not found in " + optionList);
    return -1;
    //throw new CheckErrorException();
  }
  
    // findOriginTrackingLine(_) method returns the first number of line (stocked in optionList).
  private int findOriginTrackingLine(OptionList optionList) {
    while(!optionList.isEmpty()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        OriginTracking[line=line] -> {
          return line;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    return -1;
    //throw new CheckErrorException();
  }
  
} //class TomChecker
