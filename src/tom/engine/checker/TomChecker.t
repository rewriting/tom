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
 
import java.util.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.exception.*;
import jtom.adt.*;

public class TomChecker extends TomBase {
  
  private ArrayList alreadyStudiedSymbol =  new ArrayList();
  private ArrayList alreadyStudiedType =  new ArrayList();  
  private Option currentTomStructureOrgTrack;
  private Integer nullInteger = new Integer(-1);

  public TomChecker(jtom.TomEnvironment environment) { 
    super(environment);
  }
  
    // ------------------------------------------------------------
  %include { Tom.signature }
    // ------------------------------------------------------------
  
    // Main entry point: We check all interesting Tom Structure
  public void checkSyntax(TomTerm parsedTerm) throws TomException {
    if(!Flags.doCheck) return;
    Collect collectAndVerify = new Collect() 
      {  
        public boolean apply(ATerm term) throws TomException {
          if(term instanceof TomTerm) {
            %match(TomTerm term) {
              Match(orgTrack, SubjectList(matchArgsList), PatternList(patternActionList)) -> {  
                currentTomStructureOrgTrack = orgTrack;
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
              RuleSet(orgTrack, list) -> {
                currentTomStructureOrgTrack = orgTrack;
                verifyRule(list);
                return false;
              }
              BackQuoteTerm(t,orgTrack)  -> {
                currentTomStructureOrgTrack = orgTrack;
                permissiveVerify(t);
                return false;
              }
              _ -> { return true; }
            }
          } else {
            return true;
          }
        }// end apply
      }; // end new
    genericCollect(parsedTerm, collectAndVerify);
  }

  public void permissiveVerify(TomTerm term) throws TomException {
    Collect permissiveCollectAndVerify = new Collect() 
      {  
        public boolean apply(ATerm term) throws TomException {
          if(term instanceof TomTerm) {
            %match(TomTerm term) {
              BackQuoteTerm[term=t] -> {
                permissiveVerify(t);
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
    genericCollect(term, permissiveCollectAndVerify);
  }

    /////////////////////////////////
    // MATCH VERIFICATION CONCERNS //
    /////////////////////////////////
  
      // Given a subject list, we test types in match signature and then number and type of slots found in each pattern
  private void verifyMatch(TomList subjectList, TomList patternList) throws TomException {
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
    
    while( !patternList.isEmpty()) {
      statistics().numberMatchesTested++;
      TomTerm terms = patternList.getHead();
      verifyMatchPattern(terms, typeMatchArgs);
      patternList = patternList.getTail();
    }
  }
    // For each Pattern we count and collect type information but also we test that terms are well formed
    // No top variable star are allowed
  private void verifyMatchPattern(TomTerm pattern, ArrayList typeMatchArgs) throws TomException {
    int nbExpectedArgs = typeMatchArgs.size();
    TomList termList = pattern.getTermList().getList();
    ArrayList foundTypeMatch = new ArrayList();
    Integer line = nullInteger;
    int nbFoundArgs = 0;
    
    while( !termList.isEmpty() ) {
      TomTerm termAppl = termList.getHead();
      
      %match(TomTerm termAppl) {
        Appl[astName=Name[string=name], option=Option(list)] -> {
          line = findOriginTrackingLine(list);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
        }
        Placeholder[] -> {
          nbFoundArgs++;
          foundTypeMatch.add((TomTerm) null);
        }
          // alone stared variable is impossible
        VariableStar[option=Option(list), astName=Name[string=name]] -> { 
          line = findOriginTrackingLine(name,list);
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
      //  nb elements in %match subject = nb elements in the pattern-action ?
    if(nbExpectedArgs != nbFoundArgs) {
      messageMatchErrorNumberArgument(nbExpectedArgs, nbFoundArgs, line); 
    }
      // we test the type egality between arguments and pattern-action, if it is not a variable => type is null
    for( int slot = 0; slot < nbExpectedArgs; slot++ ) {
      if ( (foundTypeMatch.get(slot) != typeMatchArgs.get(slot)) && (foundTypeMatch.get(slot) != null))
      { 	
        messageMatchErrorTypeArgument(slot+1, (String) typeMatchArgs.get(slot), (String) foundTypeMatch.get(slot), line); 
      }
    }
  }

  private String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }
  
  private void messageMatchErrorNumberArgument(int nbExpectedVar, int nbFoundVar, Integer line) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Bad number of arguments: "+nbExpectedVar+" argument(s) required but "+nbFoundVar+" found in %match structure declared line "+declLine; 
    messageError(line,s);
  }
  
  private void messageMatchErrorTypeArgument(int slotNumber, String expectedType, String givenType, Integer line) {
    if(Flags.noWarning) return;
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Warning *** Bad type in %match declared line "+declLine);
    System.out.println(" *** For slot "+ slotNumber +" :Type '"+expectedType+"' required but Type '"+givenType+"' found"+" - Line : "+line);
  }
  
  private void messageMatchTypeVariableError(String name, String type) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Variable '" + name + "' has an unknown type '"+type+"' in %match construct declared line "+declLine;
    messageError(declLine,s);
  }
  
private void messageMatchErrorVariableStar(String nameVariableStar, Integer line) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable "+nameVariableStar+"* is not allowed on left most part of %match structure declared line "+declLine;
    messageError(line,s);
  }
  
    //////////////////////////////
    // SYMBOL AND TYPE CONCERNS //
    //////////////////////////////
  
  private void verifyDeclaration(Declaration declaration) throws TomException {
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
      System.exit(1);
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
    else
      alreadyStudiedType.add(name);
  }

  private void messageTypeErrorYetDefined(String name) {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Warning *** Multiple definition of type at line "+declLine);
    System.out.println(" *** Type '"+ name +"' is already defined");
    Flags.findErrors = true;
  }

    /////////////////////////////////
    // SYMBOL DECLARATION CONCERNS //
    /////////////////////////////////
  private void verifySymbol(String symbolType, TomSymbol tomSymbol) throws TomException {
    int nbArgs=0;
    OptionList optionList = tomSymbol.getOption().getOptionList();
      // We save first the origin tracking of the symbol declaration
    currentTomStructureOrgTrack = findOriginTracking(optionList);
    TomList l = getSymbolDomain(tomSymbol);
    TomType type = getSymbolCodomain(tomSymbol);
    String name = tomSymbol.getAstName().getString();
    Integer line  = findOriginTrackingLine(optionList);
    SlotList slotList = tomSymbol.getSlotList();
    verifyMultipleDefinitionOfSymbol(name, line);
    verifySymbolCodomain(type.getString(), name, line);
    nbArgs = verifyAndCountSymbolArguments(l, name, line);
    verifySymbolOptions(symbolType, optionList, nbArgs);
  }
  
  private void verifyMultipleDefinitionOfSymbol(String name, Integer line) {
    if(alreadyStudiedSymbol.contains(name)) {
      messageOperatorErrorYetDefined(name,line);
    }
    else
      alreadyStudiedSymbol.add(name);
  }
  
  private void messageOperatorErrorYetDefined(String name, Integer line) {
    System.out.println("\n *** Warning *** Multiple definition of operator at line "+line);
    System.out.println(" *** Operator '"+ name +"' is already defined");
    Flags.findErrors = true;
  }
  
  private void verifySymbolCodomain(String returnTypeName, String symbName, Integer symbLine) throws TomException {
    if (symbolTable().getType(returnTypeName) == null){
      messageTypeOperatorError(returnTypeName, symbName, symbLine);
    }
  }
  
  private void messageTypeOperatorError(String type, String name, Integer line) throws TomException {
    String s = "Operator '" + name + "' has an unknown return type: '" + type + "'";
    messageError(line,s);
  }
  
  private int verifyAndCountSymbolArguments(TomList args, String symbName, Integer symbLine) throws TomException {
    TomTerm type;
    int nbArgs=0;
    while(!args.isEmpty()) {
      type = args.getHead();
      %match(TomTerm type) {
        TomTypeToTomTerm(type1) -> {
          nbArgs++;
          verifyTypeExist(type1.getString(), nbArgs, symbName, symbLine);
        }
      }
      args = args.getTail();
    }
    return nbArgs;
  }
  
  private void verifyTypeExist(String typeName, int slotPosition, String symbName, Integer symbLine) throws TomException {
    if (symbolTable().getType(typeName) == null){
      messageTypesOperatorError(typeName, slotPosition, symbName, symbLine);
    }
  }
  
  private void messageTypesOperatorError(String type, int slotPosition, String name, Integer line) throws TomException {
    String s = "Slot position "+slotPosition + " of operator '"+ name + "' has an unknown type: '" + type + "'";
    messageError(line,s);
  }
  
  private void verifySymbolOptions(String symbType, OptionList list, int expectedNbMakeArgs) throws TomException {
    statistics().numberOperatorDefinitionsTested++;
    ArrayList verifyList = new ArrayList();
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
      System.exit(1);
    }
    
    while(!list.isEmptyOptionList()) {
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
          verifyMakeDeclArgs(makeArgsList, expectedNbMakeArgs, orgTrack, symbType);
        }
      }
      list = list.getTail();
    }
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbType, verifyList);
    }
  }

  private void verifyMakeDeclArgs(TomList argsList, int nbMakeArgs, Option orgTrack, String symbType) throws TomException {
      // we test the necessity to use different names for each variable-parameter.
    ArrayList listVar = new ArrayList();
    int nbArgsFound =0;
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
      nbArgsFound++;
    }
    if(nbArgsFound != nbMakeArgs) {
      messageNumberArgumentsError(nbMakeArgs,nbArgsFound,orgTrack, symbType);
    }
  }
  private void messageNumberArgumentsError(int nbArg, int nbArg2, Option orgTrack, String symbType) throws TomException {
    Integer line = nullInteger, declLine = nullInteger;
    String name = "unknown", nameDecl = "unknown";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking(Name(orgName), orgLine), OriginTracking(Name(declOrgName),declOrgLine) -> {
        declLine = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
      }
    }
    String s = "Bad number of arguments: "+ nbArg + " argument(s) are required but " + nbArg2 + " found in method '" + name + "' in '"+symbType+" "+nameDecl+"' declared at line "+declLine;
    messageError(line,s);
  }	
  
  private void messageErrorVariableStar(String nameVariableStar, String nameMethod ,Integer line) throws TomException {
    String s = "List variable '" + nameVariableStar + "' cannot be used in '" + nameMethod + "'";
    messageError(line,s);
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
    Integer line = nullInteger, declLine = nullInteger;
    String name = "unknown", nameDecl = "unknown";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking(Name(orgName),orgLine), OriginTracking(Name(declOrgName),declOrgLine) -> {
        declLine = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
      }
    } 
    System.out.println("\n *** Repeated macro-functions in '"+declType+" "+nameDecl+"' declared at line "+declLine);
    System.out.println(" *** Repeated function :'" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  } 
  private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType) {
     Integer line = nullInteger, declLine = nullInteger;
    String name = "unknown", nameDecl = "unknown";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking(Name(orgName),orgLine), OriginTracking(Name(declOrgName),declOrgLine) -> {
        declLine = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
      }
    }
    System.out.println("\n *** Arguments must be linear in method '"+nameFunction+"' of '"+declType+" "+nameDecl+"' declared at line "+declLine);
    System.out.println(" *** Variable '"+nameVar+"' is repeated - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
    Integer line = nullInteger;
    String name = "unknown";
    %match(Option currentTomStructureOrgTrack) {
      OriginTracking(Name(orgName), orgLine) -> {
        line = orgLine;
        name = orgName;
      }
    }
    System.out.println("\n *** Missing macro-functions in '"+nameConstruct+" "+name+"' declared at line "+line);
    System.out.println(" *** Missing function(s) : "+list);
    Flags.findErrors = true;
  }
  
    //////////////////////
    // RECORDS CONCERNS //
    //////////////////////
  
  private void verifyRecordStructure(OptionList option, String tomName, TomList args) throws TomException {
    TomSymbol symbol = symbolTable().getSymbol(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
        // list operator with [] no allowed
      if(slotList.isEmptySlotList() || (args.isEmpty() && (isListOperator(symbol) ||  isArrayOperator(symbol)))) {
        messageBracketError(tomName, option);
      }
      verifyRecordSlots(args,slotList, tomName);
    } else {
      messageSymbolError(tomName, option);
    }
  }
  
    // We test the existence of slotName contained in pairSlotAppl. and then the associated term
  private void verifyRecordSlots(TomList listPair, SlotList slotList, String methodName) throws TomException {
    ArrayList slotIndex = new ArrayList();
    while( !listPair.isEmpty() ) {
      TomTerm pair = listPair.getHead();
      TomName name = pair.getSlotName();
      int index = getSlotIndex(slotList,name);
      Integer index2 = new Integer(index);
      if(index < 0) {
        messageSlotNameError(pair,slotList, methodName); 
      } else {
        if(slotIndex.contains(index2)) {
          messageSlotRepeatedError(pair, methodName);
        }
        slotIndex.add(index2);
      }
      listPair = listPair.getTail();
    }
  }

  private void messageSlotNameError(TomTerm pairSlotName, SlotList slotList, String methodName) {
    ArrayList listOfPossibleSlot = new ArrayList();
    while ( !slotList.isEmptySlotList() ) {
      TomName name = slotList.getHeadSlotList().getSlotName();
      if ( !name.isEmptyName()) listOfPossibleSlot.add(name.getString());
      slotList = slotList.getTailSlotList();
    }
    %match( TomTerm pairSlotName ) {
      PairSlotAppl[slotName=Name(name),appl=Appl[option=Option(list)]] -> {
        Integer line = findOriginTrackingLine(list);
        System.out.println("\n *** Slot Name '" + name + "' is not correct: See method '"+methodName+ "' -  Line : "+line);
      }
    }
    System.out.println(" *** Possible Slot Names are : "+listOfPossibleSlot);
    Flags.findErrors = true;
  }
  
  private void messageSlotRepeatedError(TomTerm pairSlotName, String methodName) {
    %match( TomTerm pairSlotName ) {
      PairSlotAppl(Name(name), Appl[option=Option(list)]) -> {
        Integer line = findOriginTrackingLine(list);
        System.out.println("\n *** Same slot names can not be used several times: See method "+methodName+ "' -  Line : "+line);
        System.out.println(" *** Repeated slot Name : '"+name+"'");
      }
    }
    Flags.findErrors = true;
  }
  
  private void messageBracketError(String name, OptionList optionList) throws TomException {
    Integer line = findOriginTrackingLine(name,optionList);
    String s = "[] are not allowed on lists or arrays nor constants, see: "+name;
    messageError(line,s);
  }

    //////////////////////
    // APPL CONCERNS    //
    //////////////////////
  
  private void verifyApplStructure(OptionList optionList, String name, TomList argsList) throws TomException {
    statistics().numberApplStructuresTested++;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null  && (getLRParen(optionList)!=null || !argsList.isEmpty())) {
      messageSymbolError(name, optionList);
    } else {
      if(!argsList.isEmpty()) {
          //in case of oparray or oplist, the number of arguments is not tested
        if ( !isListOperator(symbol) &&  !isArrayOperator(symbol) ) {
            // We test the number of args vs its definition
          int nbExpectedArgs = length(getSymbolDomain(symbol));
          int nbFoundArgs = length(argsList);
          if (nbExpectedArgs != nbFoundArgs) {
            Integer line = findOriginTrackingLine(name,optionList);
            messageNumberArgumentsError(nbExpectedArgs, nbFoundArgs, name, line);
          }
        }
      }
    }
  }
  
  private void permissiveVerifyApplStructure(OptionList optionList, String name, TomList argsList) throws TomException {
    statistics().numberApplStructuresTested++;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null && !argsList.isEmpty()) {
      messageWarningSymbol(name, optionList);
    } else {
      if(!argsList.isEmpty()) {
          //in case of oparray or oplist, the number of arguments is not tested
        if ( !isListOperator(symbol) &&  !isArrayOperator(symbol) ) {
            // We test the number of args vs its definition
          int nbExpectedArgs = length(getSymbolDomain(symbol));
          int nbFoundArgs = length(argsList);
          if (nbExpectedArgs != nbFoundArgs) {
            Integer line = findOriginTrackingLine(name,optionList);
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
    while(!list.isEmptyOptionList()) {
      Option term = list.getHead();
      %match(Option term ) {
        DeclarationToOption(MakeDecl[args=makeArgsList, orgTrack=orgTrack]) -> {
          return true;
        }
        DefinedSymbol -> {return true;}
      }
      list = list.getTail();
    }
    return false;
  }

  private void messageSymbolError(String name, OptionList optionList) throws TomException {
    Integer line = findOriginTrackingLine(name, optionList);
    String s = "Symbol method : '" + name + "' not found";
    messageError(line,s);
  }
  
  private void messageWarningSymbol(String name, OptionList optionList) {
    if(Flags.noWarning) return;
    String nameDecl = "unknown";
    Integer declLine = nullInteger;
    %match (Option currentTomStructureOrgTrack) {
       OriginTracking(Name(declOrgName),declOrgLine) -> {
        declLine = declOrgLine;nameDecl = declOrgName;
      }
    }
    Integer line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Warning *** possible error in structure "+nameDecl+" declared line "+declLine);
    System.out.println(" *** Unknown method '"+name+"' : Ensure the type coherence by yourself line : " + line);
  }
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, String name, Integer line) throws TomException {
    String s = "Bad number of arguments for method '" + name + "':" + nbArg + " arguments are required but " + nbArg2 + " are given";
    messageError(line,s);
  }

    ////////////////////////////////
    // RULE VERIFICATION CONCERNS //
    ////////////////////////////////
    /* for each rewrite rule we make test on: 
     * Rhs shall have no underscore, be a var*?, be a record?
     * Each Lhs shall start with the same prodction name
     * This symbol name shall not be already declared
     * Lhs and Rhs shall have the same return type
     * Used variable on rhs shall be coherent and declared on lhs
     */ 
  private void verifyRule(TomList ruleList) throws TomException {
    int i = 0;
    TomTerm currentRule;
    String name = "Unknown";
    while(!ruleList.isEmpty()) {
      currentRule = ruleList.getHead();
      matchBlock: {
        %match(TomTerm currentRule) {
          RewriteRule(Term(lhs),Term(rhs),orgTrack) -> {
            statistics().numberRulesTested++;
            name = verifyLhsRuleAndConstructorEgality(lhs, name, i);
            if(i == 0) {
                // update the root of lhs: it becomes a defined symbol
              ast().updateDefinedSymbol(symbolTable(),lhs);
            }
            verifyRhsRuleStructure(rhs);
            verifyRuleVariable(lhs,rhs,orgTrack);
            break matchBlock;
          }
          _             -> {
            System.out.println("Strange rule:\n" + currentRule);
            System.exit(1);
          }
        }
      }
      ruleList = ruleList.getTail();
      i++;
    }
  }
  
  private String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  ruleName, int ruleNumber) throws TomException {
    String methodName = "";
    OptionList options = tsf().makeOptionList_EmptyOptionList();
    %match(TomTerm lhs) {
      appl@Appl(Option(optionList),Name(name), args) -> {
        // No alone variable noor simple constructor
        if( args.isEmpty() && getLRParen(optionList)==null) {
          Integer line = findOriginTrackingLine(optionList);
          messageRuleErrorVariable(name, line);
        }
        checkSyntax(appl);
          // lhs outermost symbol shall have a corresponding make
        if ( !findMakeDeclOrDefSymbol(symbolTable().getSymbol(name).getOption().getOptionList())) {
          messageNoMakeForSymbol(name, optionList);
        }
        options = optionList;
        methodName = name;
      }
      VariableStar[option=Option(optionList), astName=Name(name1)] -> { 
        Integer line = findOriginTrackingLine(name1,optionList);
        messageRuleErrorVariableStar(name1, line); 
      }
      Placeholder[option=Option(t)] -> { 
        messageRuleErrorLhsImpossiblePlaceHolder(t); 
      }
      rec@RecordAppl(Option(optionList),Name(name),args) -> {
        checkSyntax(rec);
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

  private void messageNoMakeForSymbol(String name, OptionList optionList) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    Integer line = findOriginTrackingLine(optionList);
    String s = "Symbol '" +name+ "' has no 'make' method associated in structure declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorVariable(String nameVariableStar, Integer line) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Alone variable " +nameVariableStar+ " is not allowed on left hand side of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorVariableStar(String nameVariableStar, Integer line) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable " +nameVariableStar+ "* is not allowed on left hand side of structure %rule declared line "+declLine;
    messageError(line,s);
  }

  private void messageRuleErrorLhsImpossiblePlaceHolder(OptionList optionList) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    Integer line = findOriginTrackingLine(optionList);
    String s = "Alone placeholder is not allowed in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorConstructorEgality(String  name, String nameExpected, OptionList optionList) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    Integer line = findOriginTrackingLine(name, optionList);
    String s = "Left most symbol name '" + nameExpected + "' expected, but '" + name + "' found in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
  private void verifyRhsRuleStructure(TomTerm ruleRhs) throws TomException {
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
          System.exit(1);
        }
      }
    }
  }

  private void messageRuleErrorRhsImpossiblePlaceholder(OptionList optionList) throws TomException {
    Integer line = findOriginTrackingLine(optionList);
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = " *** Placeholder is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorRhsImpossibleRecord(OptionList optionList, String name) throws TomException {
    Integer line = findOriginTrackingLine(optionList);
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "\n *** Record '"+name+"[...]' is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  private void messageRuleErrorRhsImpossibleVarStar(OptionList optionList, String name) throws TomException {
    Integer line = findOriginTrackingLine(optionList);
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable '"+name+"*' is not allowed in right hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
    // We test the non existence of variables from the right part of '->' in the left part of '->'.
  private void verifyRuleVariable(TomTerm lhs, TomTerm rhs, Option rewriteRuleOrgTrack) throws TomException {
      // We extract variable informations of the left part.
    Collection variableCollectionLhs = new HashSet();
    collectVariable(variableCollectionLhs, lhs);
    Collection variableCollectionRhs = new HashSet();
    collectVariable(variableCollectionRhs, rhs);
    if( !variableCollectionLhs.containsAll(variableCollectionRhs) ) {
      Iterator it = variableCollectionLhs.iterator();
      while(it.hasNext()) {
        variableCollectionRhs.remove(it.next());
      }
      messageRuleErrorUnknownVariable(variableCollectionRhs, rewriteRuleOrgTrack);
    }
  }
  
  private void messageRuleErrorUnknownVariable(Collection variableCollectionRhs, Option rewriteRuleOrgTrack) throws TomException {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    Integer line = findOriginTrackingLine(rewriteRuleOrgTrack);
    String s = "Unknown variable(s) " +variableCollectionRhs+ " used on right part of %rule declared line "+declLine;
    messageError(line,s);
  }
  
  private void collectVariable(final Collection collection, TomTerm subject) throws TomException {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) throws TomException {
            //%variable
          if(t instanceof TomTerm) {
            String annotedVariable = null;
              //System.out.println(t);
            %match(TomTerm t) { 
                // to collect annoted nodes but avoid collect variables in optionSymbol
              appl@Appl(Option(optionList), Name(name), subterms) -> {
                if( subterms.isEmpty() && getLRParen(optionList)==null && symbolTable().getSymbol(name)==null) 
                  collection.add(name);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return true;
              }
              RecordAppl(Option(optionList),Name(tomName),pairs) -> {
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return true;
              }
              VariableStar[option=Option(optionList)] -> {
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
              Placeholder[option=Option(optionList)] -> {
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
              }
              _ -> { return true; }
            }
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    genericCollect(subject, collect); 
  }
  
  private String getAnnotedVariable(OptionList subjectList) {
    while(!subjectList.isEmptyOptionList()) {
      Option subject = subjectList.getHead();
      %match(Option subject) {
        TomNameToOption(Name(name1)) -> {
          return name1;
        }
      }
      subjectList = subjectList.getTail();
    }
    return null;
  }
  
  private void messageRuleErrorUnknownVariable(String name, Integer line) {
    Integer declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Variables used on right part of rule have to be declared in left part of rule declared line "+declLine);
    System.out.println("Variable '"+name+"' not decared - Line : "+line);
    Flags.findErrors = true;
  }

  
    /////////////
    // GLOBALS //
    /////////////
  // findOriginTrackingLine(_) method returns the first number of line (stocked in optionList).
  private Integer findOriginTrackingLine(OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        OriginTracking[line=line] -> {
          return line;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    System.exit(1);return null;
  }

    // findOriginTracking(_) return the option containing OriginTracking information
  private Option findOriginTracking(OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        orgTrack@OriginTracking[] -> {
          return orgTrack;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    System.exit(1);return null;
  }

  private Integer findOriginTrackingLine(Option option) {
    return option.getLine();
  }

} //class verify
