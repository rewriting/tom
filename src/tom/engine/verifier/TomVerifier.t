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

*/

package jtom.verifier;
 
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;
import jtom.exception.*;
import jtom.adt.*;

public class TomVerifier extends TomBase {
  
  private ArrayList alreadyStudiedSymbol =  new ArrayList();
  private ArrayList alreadyStudiedType =  new ArrayList();  
  private Option currentTomStructureOrgTrack;

  public TomVerifier(jtom.TomEnvironment environment) { 
    super(environment);
  }
  
    // ------------------------------------------------------------
  %include { Tom.signature }
    // ------------------------------------------------------------
  
    // Main entry point: We verify all interesting Tom Structure
  public void verify(TomTerm parsedTerm) throws TomException {
    if(!Flags.doVerify) return;
    Collect collectAndVerify = new Collect() 
      {  
        public boolean apply(ATerm term) throws TomException {
          if(term instanceof TomTerm) {
            %match(TomTerm term) { 
              Match(orgTrack, SubjectList(matchArgsList), PatternList(patternActionList)) -> {  
                currentTomStructureOrgTrack = orgTrack;
                verifyMatch(matchArgsList, patternActionList);
                return false;
              }
              RuleSet(orgTrack, list) -> {
                currentTomStructureOrgTrack = orgTrack;
                verifyRule(list);
                return false;
              }
              DeclarationToTomTerm(declaration) -> {
                verifyDeclaration(declaration);
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
  
    /////////////////////////////////
    // MATCH VERIFICATION CONCERNS //
    /////////////////////////////////
    // Given a subject list, we test number and type of slots found in each pattern
  private void verifyMatch(TomList subjectList, TomList patternList) throws TomException {
    ArrayList typeMatchArgs = new ArrayList();
    while( !subjectList.isEmpty() ) {
      TomTerm term = subjectList.getHead();
      %match( TomTerm term ) {
        GLVar[astType=TomTypeAlone(type)] -> {
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
    String line = " - ";
    int nbFoundArgs = 0;
    
    while( !termList.isEmpty() ) {
      TomTerm termAppl = termList.getHead();
      
      %match(TomTerm termAppl) {
        Appl[option=Option(list), astName=Name[string=name]] -> {
            //  we test the validity of the current Appl structure
          testApplStructure(termAppl);
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
        record@RecordAppl(Option(options),Name(name),args) ->{
            //  we test the validity of the current record structure
          testRecordStructure(record, options, name, args);
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
    ArrayList nameAndType = new ArrayList();
    nameAndType.add("Unknown");
    nameAndType.add("Unknown");
    while(!ruleList.isEmpty()) {
      currentRule = ruleList.getHead();
      matchBlock: {
        %match(TomTerm currentRule) {
          RewriteRule(Term(lhs),Term(rhs)) -> {
            if(i == 0) {
                // update the root of lhs: it becomes a defined symbol
              ast().updateDefinedSymbol(symbolTable(),lhs);
            }
            verifyNoUnderscoreAndStructureRuleRhs(rhs, true);
            nameAndType = verifyLhsRuleTypeAndConstructorEgality(lhs, nameAndType, i);
            verifyLhsAndRhsRuleTypeEgality(rhs, lhs, (String)nameAndType.get(1));
            verifyRuleVariable(lhs,rhs);
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
  
  private void verifyNoUnderscoreAndStructureRuleRhs(TomTerm ruleRhs, boolean firstLevel) throws TomException {
    matchBlock: {
      %match(TomTerm ruleRhs) {
        Appl[args=argsList] -> {
          testApplStructureRhsRule(ruleRhs);
          while(!argsList.isEmpty()) {
            TomTerm oneArgs = argsList.getHead();
            verifyNoUnderscoreAndStructureRuleRhs(oneArgs, false);
            argsList = argsList.getTail();
          }
          break matchBlock;
        }
        Placeholder[option=Option(option)] -> {
          messageRuleErrorRhsImpossiblePlaceholder(option);
          break matchBlock;
        }
        VariableStar[option=Option(list), astName=Name[string=name1]] -> {
          if(firstLevel) System.out.println("VariableStar on rhs of rule: Allowed?");
          break matchBlock;
        }
        RecordAppl(Option(option),Name(tomName),args) -> {
          System.out.println("What to do with Record on rhs of rule");
          messageRuleErrorRhsImpossiblePlaceholder(option);
        }
        _ -> {
          System.out.println("Strange rule rhs:\n" + ruleRhs);
          System.exit(1);
        }
      }
    }
  }
  
    /*
      In this method, we test the fact that, in %rule declaration, all objects in the left part of '->'
      have the same constructor, no '_' and no alone variable or variableStar.
      In order to test this, we returns the name and the type of needed constructor which will be
      the sames for the next rule of %rule.
      nameType contains name and type of needed constructor.
      ruleNumber is used to known if it is the first rule of the readed %rule.
      
NB : another test, in order to forbid alone variables in the left part of '->', is already made in
      'context,RewriteRule' case of 'pass1' method in TomChecker.t and runs 'messageRuleSymbolError' method. 
      That's why the case of alone variables is not made here.
    */
  private ArrayList verifyLhsRuleTypeAndConstructorEgality(TomTerm lhs, ArrayList nameType, int ruleNumber) throws TomException {
    %match(TomTerm lhs) {
      Appl[option=Option(optionList),astName=Name[string=name]] -> {
        testApplStructure(lhs);
        TomType term = typeOut(symbolTable().getSymbol(name));
        %match(TomType term) {
          TomTypeAlone[string=t] -> { 
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.clear();
              nameType.add(name);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name != nameType.get(0) ) ) {
                messageErrorRuleTypeAndConstructorEgality(name, (String)nameType.get(0), t, (String)nameType.get(1), optionList);
              }
            }
          }
          EmptyType() -> {
            messageSymbolError(name, optionList);
          }
        }
      }
      VariableStar[option=Option(optionList), astName=Name[string=name1]] -> { 
        String line = findOriginTrackingLine(name1,optionList);
        messageErrorRuleVariableStar(name1, line); 
      }
      Placeholder[option=Option(t)] -> { 
        messageRuleErrorLhsImpossiblePlaceHolder(t); 
      }
      RecordAppl(Option(optionList),Name(name),args) -> {
        testRecordStructure(lhs);
        TomType term = typeOut(symbolTable().getSymbol(name));
        %match(TomType term) {
          TomTypeAlone[string=t] -> { 
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.clear();
              nameType.add(name);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name != nameType.get(0) ) ) {
                messageErrorRuleTypeAndConstructorEgality(name, (String)nameType.get(0), t, (String)nameType.get(1), optionList);
              }
            }
          }
          EmptyType() -> {
            messageSymbolError(name, optionList);
          }
        }
	System.out.println("Warning prefer f() instead of f[] or Message for impossible record on lhs of rule?");
      }
    }
    return nameType;
  }
  
    // We test the non existence of variables from the right part of '->' in the left part of '->'.
  private void verifyRuleVariable(TomTerm lhs, TomTerm rhs) {
      // We extract variable informations of the left part.
    ArrayList nameVariableIn = new ArrayList();
    ArrayList lineVariableIn = new ArrayList();
    extractVariable(lhs,nameVariableIn,lineVariableIn);
      // We extract variable informations of the right part.
    ArrayList nameVariableOut = new ArrayList();
    ArrayList lineVariableOut = new ArrayList();
    extractVariable(rhs,nameVariableOut,lineVariableOut);
      // We test the existence of the right part in left part.
    int n = nameVariableOut.size();
    for(int i = 0; i < n; i++) {
      if(!nameVariableIn.contains(nameVariableOut.get(i))) {
        messageRuleErrorUnknownVariable((String)nameVariableOut.get(i),(String)lineVariableOut.get(i));
      }
    }
  }
    /*
      1. We test the obligation for '->' in %rule to have the same type in left
      and right parts. For this, we use nameType defined thanks to 'testRuleTypeAndConstructorEgality' method
      which returns name and type of constructor in left part.
      2. We test the type egality (in right and left parts) of variables with same name.
      We have lhs -> rhs and nameType defined thanks to 'verifyRuleTypeAndConstructorEgality' method
      which returns name and type of the constructor in left part.
      
      WE TEST ONLY APPL BECAUSE VAR* AND _ ARE ALREADY CATCH BEFORE
    */
  private void verifyLhsAndRhsRuleTypeEgality(TomTerm rhs, TomTerm lhs, String typeNameOfRule) {
    statistics().numberRulesTested++;
    String foundTypeName = "Not found?";
    %match(TomTerm rhs) {
      Appl[option=Option(list),astName=Name[string=name1]] -> {
          /*
            We look the type of the resulted object of this Appl.
            If resulted object has no type, typeOut returns EmptyType) (for example
            if it is a variable).
          */
        TomType term = typeOut(symbolTable().getSymbol(name1));
        %match(TomType term) {
          TomTypeAlone[string=t] -> { foundTypeName = t;}
          
          EmptyType() -> { 
              //  we look if an object with name 'name1' exists in lhs and we set its type.
            TomType typeFind = findTypeOf(name1, lhs);
            %match(TomType typeFind) {
              TomTypeAlone[string=t] -> {  foundTypeName = t;}
            }
          }
        }
        if ( foundTypeName != typeNameOfRule ) {
          messageRuleErrorTypeEgality(name1, foundTypeName, typeNameOfRule, list);
        }
      }
    } 
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
        DeclarationToTomTerm(CompareFunctionSymbolDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
          checkFieldAndLinearArgs("cmp_fun_sym",verifyList,orgTrack,name1,name2, declType);
        }
        DeclarationToTomTerm(TermsEqualDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
          checkFieldAndLinearArgs("equals",verifyList,orgTrack,name1,name2, declType);
        }
          /*Specific to typeterm*/
        DeclarationToTomTerm(GetSubtermDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
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
        DeclarationToTomTerm(GetElementDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> { 
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
        // Maybe a warning ???
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
    String line  = findOriginTrackingLine(optionList);
    verifyMultipleDefinitionOfSymbol(name, line);
    verifySymbolCodomain(type.getString(), name, line);
    nbArgs = verifyAndCountSymbolArguments(l, name, line);
    verifySymbolOptions(symbolType, optionList, nbArgs);
  }
  
  private void verifyMultipleDefinitionOfSymbol(String name, String line) {
    if(alreadyStudiedSymbol.contains(name)) {
      messageOperatorYetDefined(name,line);
    }
    else
      alreadyStudiedSymbol.add(name);
  }
  
  private void verifySymbolCodomain(String returnTypeName, String symbName, String symbLine) throws TomException {
    if (symbolTable().getType(returnTypeName) == null){
      messageTypeOperatorError(returnTypeName, symbName, symbLine);
    }
  }
  
  private int verifyAndCountSymbolArguments(TomList args, String symbName, String symbLine) throws TomException {
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
  
  private void verifyTypeExist(String typeName, int slotPosition, String symbName, String symbLine) throws TomException {
    if (symbolTable().getType(typeName) == null){
      messageTypesOperatorError(typeName, slotPosition, symbName, symbLine);
    }
  }
  
  private void verifySymbolOptions(String symbType, OptionList list, int expectedNbMakeArgs) throws TomException {
    statistics().numberOperatorDefinitionsTested++;
    ArrayList verifyList = new ArrayList();
    if(symbType == "%op")
    {
      verifyList.add("make");
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
        DeclarationToOption(MakeAddArray[varList=Variable[astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]], orgTrack=orgTrack]) -> {
          checkFieldAndLinearArgs("make_append", verifyList, orgTrack, name1, name2, symbType);
        }
          /*for a List symbol*/
        DeclarationToOption(MakeEmptyList[orgTrack=orgTrack]) -> {
          checkField("make_empty",verifyList,orgTrack, symbType);
          
        }
        DeclarationToOption(MakeAddList[varList=Variable[astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]], orgTrack=orgTrack]) -> {
          checkFieldAndLinearArgs("make_insert", verifyList, orgTrack, name1, name2, symbType);
        }
          /*for a symbol*/
        DeclarationToOption(MakeDecl[astName=Name[string=nameSymbol],args=makeArgsList, orgTrack=orgTrack]) -> {
          checkField("make", verifyList, orgTrack, symbType);
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
      // we test the necessity to use different names for each variable-paremeter.
    ArrayList listVar = new ArrayList();
    int nbArgsFound =0;
    while(!argsList.isEmpty()) {
      TomTerm termVar = argsList.getHead();
      %match(TomTerm termVar) {
        Variable[option=Option(listOption), astName=Name[string=name]] -> {
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
  
  
    ////////////////////
    // TERM  CONCERNS //
    ////////////////////
  private void testTerm(TomTerm term) throws TomException {
    %match(TomTerm term) {
      Appl[] -> {
        testApplStructure(term);
        return;
      }
      RecordAppl[] -> {
        testRecordStructure(term);
        return;
      }
      _ -> {
        System.out.println("Strange testTerm "+ term);
      }
    }
  }
  
    /*
      1. We test if the arguments number of one method is right wrt. its definition.
      2. We test if the types of arguments in one method is right wrt. its definition.
      3. We test if a VariableStar is authorized when it is used.
      termAppl is the Appl structure in question.
    */
  private void testApplStructure(TomTerm termAppl) throws TomException {
    %match(TomTerm termAppl) {
      Appl(Option(list),Name[string=name],argsList) -> {
          testApplStructure(list, name, argsList);
      }
    }
  }
  private void testApplStructureRhsRule(TomTerm termAppl) throws TomException {
    %match(TomTerm termAppl) {
      Appl(Option(list),Name[string=name],argsList) -> {
          testApplStructureRhsRule(list, name, argsList);
      }
    }
  }
  private void testApplStructure(OptionList list, String name, TomList argsList) throws TomException {
    statistics().numberApplStructuresTested++;
      //in case of oparray or oplist, the number of arguments is not tested
    boolean arrayOrList = false;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null && argsList.isEmpty() ) {
    } else if( symbol==null && !argsList.isEmpty() ) {
      messageSymbolError(name, list);
    } else {
        // we extract the needed types for the arguments of this Appl.
      TomList infoTypeIn = typeIn(symbol);
      arrayOrList = ( isListOperator(symbol) ||  isArrayOperator(symbol) );
        // if arguments are given
      if(!argsList.isEmpty()) {
          // We test the egality between number of needed arguments and of given arguments.
        int arity = length(argsList);
        if(arity!=length(infoTypeIn) && !arrayOrList) {
          String line = findOriginTrackingLine(name,list);
          messageNumberArgumentsError(length(infoTypeIn), arity, name, line);
        }
          /*
            We examine the complete list of given arguments in order to extract their result
            type and their name constructor (for error message)
          */
        TomType[] tabTypeOut = new TomType[arity];
        String[] tabNameOut = new String[arity];
        String noNameTypeOut = "NoName";
        for(int i=0; !argsList.isEmpty() ; i++,argsList = argsList.getTail()) {
          TomTerm term = argsList.getHead();
          %match(TomTerm term) {
            Appl[astName=Name[string=name1]] -> {
              testApplStructure(term);
              tabTypeOut[i] = typeOut(symbolTable().getSymbol(name1));
              tabNameOut[i] = name1;
            }
              // If it is an '_' : type and name have no importance
            Placeholder[] -> { 
              tabTypeOut[i] = `EmptyType();
              tabNameOut[i] = noNameTypeOut;
            }
              /*
                if the constructor is not defined as an %oparray or an %oplist,
                variableStar is forbiden.
              */
            VariableStar[astName=Name[string=name1]] -> {
              if(arrayOrList ) {
                tabTypeOut[i] = infoTypeIn.getHead().getAstType();
                tabNameOut[i] = name1;
              } else {
                String line = findOriginTrackingLine(name,list);
                messageErrorVariableStar(name1,name,line);
              }
            }
            record@RecordAppl(Option(options),Name(name2),args) ->{
              testRecordStructure(record, options, name2, args);
              tabTypeOut[i] = typeOut(symbolTable().getSymbol(name2));
              tabNameOut[i] = name;
            }
          }
        }
        
          /*
            we test the correspondence of arguments between needed types (stocked in
            infoTypeIn) and given types (stocked in infoTypeOut)
          */
        for(int i=0 ; i<arity ; i++) {
          TomType oneIn = infoTypeIn.getHead().getAstType();
          if (tabTypeOut[i] != `EmptyType() && oneIn!=tabTypeOut[i]) {
            String line = findOriginTrackingLine(name,list);
            messageTypeArgumentMethodError(name,oneIn,tabTypeOut[i],tabNameOut[i],i+1,line);
          }
          if(!arrayOrList) {
            infoTypeIn=infoTypeIn.getTail();
          }
        }
      } else if(infoTypeIn!=null && !arrayOrList ) { 
          /*
            if the list of given arguments is empty, and if it is not an %oparray or %oplist,
            and if needed arguments are required => error
          */
        if( length(infoTypeIn) != 0 ) {
          String line = findOriginTrackingLine(name,list);
          messageNumberArgumentsError(length(infoTypeIn), 0, name, line);
        }
      }
    }
  }

  private void testApplStructureRhsRule(OptionList list, String name, TomList argsList) throws TomException {
    statistics().numberApplStructuresTested++;
      //in case of oparray or oplist, the number of arguments is not tested
    boolean arrayOrList = false;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null && argsList.isEmpty() ) {
    } else if( symbol==null && !argsList.isEmpty() ) {
        //System.out.println("Warning: Rhs of rule contains a function call? ensure it is correct"+name);
    } else {
        // we extract the needed types for the arguments of this Appl.
      TomList infoTypeIn = typeIn(symbol);
      arrayOrList = ( isListOperator(symbol) ||  isArrayOperator(symbol) );
        // if arguments are given
      if(!argsList.isEmpty()) {
          // We test the egality between number of needed arguments and of given arguments.
        int arity = length(argsList);
        if(arity!=length(infoTypeIn) && !arrayOrList) {
          String line = findOriginTrackingLine(name,list);
          messageNumberArgumentsError(length(infoTypeIn), arity, name, line);
        }
          /*
            We examine the complete list of given arguments in order to extract their result
            type and their name constructor (for error message)
          */
        TomType[] tabTypeOut = new TomType[arity];
        String[] tabNameOut = new String[arity];
        String noNameTypeOut = "NoName";
        for(int i=0; !argsList.isEmpty() ; i++,argsList = argsList.getTail()) {
          TomTerm term = argsList.getHead();
          %match(TomTerm term) {
            Appl[astName=Name[string=name1]] -> {
              testApplStructure(term);
              tabTypeOut[i] = typeOut(symbolTable().getSymbol(name1));
              tabNameOut[i] = name1;
            }
              // If it is an '_' : type and name have no importance
            Placeholder[] -> { 
              tabTypeOut[i] = `EmptyType();
              tabNameOut[i] = noNameTypeOut;
            }
              /*
                if the constructor is not defined as an %oparray or an %oplist,
                variableStar is forbiden.
              */
            VariableStar[astName=Name[string=name1]] -> {
              if(arrayOrList ) {
                tabTypeOut[i] = infoTypeIn.getHead().getAstType();
                tabNameOut[i] = name1;
              } else {
                String line = findOriginTrackingLine(name,list);
                messageErrorVariableStar(name1,name,line);
              }
            }
            record@RecordAppl(Option(options),Name(name2),args) ->{
              testRecordStructure(record, options, name2, args);
              tabTypeOut[i] = typeOut(symbolTable().getSymbol(name2));
              tabNameOut[i] = name;
            }
          }
        }
        
          /*
            we test the correspondence of arguments between needed types (stocked in
            infoTypeIn) and given types (stocked in infoTypeOut)
          */
        for(int i=0 ; i<arity ; i++) {
          TomType oneIn = infoTypeIn.getHead().getAstType();
          if (tabTypeOut[i] != `EmptyType() && oneIn!=tabTypeOut[i]) {
            String line = findOriginTrackingLine(name,list);
            messageTypeArgumentMethodError(name,oneIn,tabTypeOut[i],tabNameOut[i],i+1,line);
          }
          if(!arrayOrList) {
            infoTypeIn=infoTypeIn.getTail();
          }
        }
      } else if(infoTypeIn!=null && !arrayOrList ) { 
          /*
            if the list of given arguments is empty, and if it is not an %oparray or %oplist,
            and if needed arguments are required => error
          */
        if( length(infoTypeIn) != 0 ) {
          String line = findOriginTrackingLine(name,list);
          messageNumberArgumentsError(length(infoTypeIn), 0, name, line);
        }
      }
    }
  }

  private void testRecordStructure(TomTerm record) throws TomException {
    %match (TomTerm record) {
      rec@RecordAppl(Option(options),Name(name),args) -> {testRecordStructure(rec, options, name, args); }
    }
  }
  private void testRecordStructure(TomTerm record, OptionList option, String tomName, TomList args) throws TomException {
    TomSymbol symbol = symbolTable().getSymbol(tomName);
    if(symbol != null) {
      TomList slotList = getSlotList(symbol.getOption().getOptionList());
      if( slotList == null ) {
        messageBracketError(record);
      }
        // System.out.println("Name:"+tomName+"\n slotList:"+slotList+"\n args:"+args);
      testNumberAndRepeatedSlotName(args,slotList);
      testPairSlotNameAndTerm(args,slotList);
    } else {
      messageSymbolError(tomName, option);
    }
  }
  
  
    /*
      We test the existence of a slot, contained in pairSlotName.
      pairSlotName is one slot given in argument.
      slotList is the list of possible slots.
    */
  private void testPairSlotNameAndTerm(TomList listPair, TomList declaredSlotNamesList) throws TomException {
    boolean findSlotNameEquivalent;
    int nbDeclaredSlotName, i;
    TomTerm testedPair, associatedTerm;
    String testedSlotName;
    ArrayList declaredSlotNames = new ArrayList();

    while( !declaredSlotNamesList.isEmpty() ) {
      TomTerm oneSlot = declaredSlotNamesList.getHead();
      declaredSlotNames.add(oneSlot.getString());
      declaredSlotNamesList = declaredSlotNamesList.getTail();
    }
    nbDeclaredSlotName = declaredSlotNames.size();
    while( !listPair.isEmpty() ) {
      testedPair = listPair.getHead();
      
      testedSlotName = testedPair.getSlotName().getString();
      findSlotNameEquivalent =false;
      for(i = 0;i<nbDeclaredSlotName;i++) {
        if ( testedSlotName == declaredSlotNames.get(i) ) { findSlotNameEquivalent = true; break;}
      } 
        // if slotName is unknown we generate a message which propose all possible slots for this case
      if(!findSlotNameEquivalent) {
        messageSlotNameError(testedPair, declaredSlotNames); 
      }
        // At the end we test the term associated to this slot
      associatedTerm = testedPair.getAppl();
      testTerm(associatedTerm);
      listPair = listPair.getTail();
    }
  }
  
    /*
      testNumberAndRepeatedSlotName is used in 'expand' method of TomChecker.t.
      1. We test the fact that on slotName can be use only one time in a same level.
      2. We test the number of slotName which must be less or egal that the arguments number
      of concerned constructor.
      slotList is the list of possible slotName.
      pairSlotList is the list of PairSlotName given in arguments 
    */
  private void testNumberAndRepeatedSlotName(TomList pairList, TomList declaredSlotNameList) {
      // we generate formated list of declared slotNames and testedSlotNames
    ArrayList declaredSlotNames = new ArrayList();
    ArrayList testedSlotNames = new ArrayList();
    while( !declaredSlotNameList.isEmpty() ) {
      TomTerm oneSlot = declaredSlotNameList.getHead();
      declaredSlotNames.add(oneSlot.getString());
      declaredSlotNameList = declaredSlotNameList.getTail();
    }
      // We memorize the first slotName in order to have information about line
    TomTerm pairSlotName = null;
    if(!pairList.isEmpty()) {
      pairSlotName = pairList.getHead();
    }
    
    while( !pairList.isEmpty() ) {
      TomTerm onePair = pairList.getHead();
      testedSlotNames.add(onePair.getSlotName().getString());
      pairList = pairList.getTail();
    }
    
      // The list of given slotNames must be less or egal in dimension than the list of possible slotNames
    if(testedSlotNames.size() > declaredSlotNames.size()) {
      messageSlotNumberError(pairSlotName, declaredSlotNames.size(), testedSlotNames.size());
    } else {
      int index = testedSlotNames.size(); 
        // foundSlots contains the list of already verified slotNames
      ArrayList foundSlots = new ArrayList();
        // We test all given slotNames
      while( index != 0 ) {
        String nameSlot = (String) testedSlotNames.get(index - 1);
        if( foundSlots.contains(nameSlot) ) {
          messageSlotRepeatedError(pairSlotName,nameSlot);
        } else {
          foundSlots.add(nameSlot);
        }
        index--;
      }
    }
  }
  
    /////////////////////////
    //   MESSAGE METHODS   //
    /////////////////////////

    /********************************
     * Error messages which generate immediatly an exit
     ********************************/
   // Link directly to Match structure
  private void messageMatchErrorVariableStar(String nameVariableStar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable "+nameVariableStar+"* : Not allowed in %match structure declared line "+lineDecl;
    messageError(line,s);
  }
  private void messageMatchErrorNumberArgument(int nbExpectedVar, int nbFoundVar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Bad number of arguments: " +nbExpectedVar+" argument(s) required but "+nbFoundVar+" found in %match structure declared line "+lineDecl; 
    messageError(line,s);
  }

   // Link directly to Match structure  
   // this one could be parameter with string %rule or %match
  private void messageErrorRuleVariableStar(String nameVariableStar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable " +nameVariableStar+ "* : Not allowed in %rule declared line "+lineDecl;
    messageError(line,s);
  }
  private void messageRuleErrorLhsImpossiblePlaceHolder(OptionList optionList) throws TomException {
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String line = findOriginTrackingLine(optionList);
    String s = "Alone placeholder : Not allowed in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }




  private void messageNumberArgumentsError(int nbArg, int nbArg2, String name, String line) throws TomException {
    String s = "Bad number of arguments for method '" + name + "':" +
      nbArg + " arguments are required but " + nbArg2 + " are given";
    messageError(line,s);
  }	
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, Option orgTrack, String symbType) throws TomException {
    String line = "not found";
    String name = "unknown";
    String lineDecl = "not found";
    String nameDecl = "unknown";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking[astName=Name[string=orgName],line=Line[string=orgLine]], OriginTracking[astName=Name[string=declOrgName],line=Line[string=declOrgLine]] -> {
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
      }
    }
    String s = "Bad number of arguments in method '" + name + "' for '"+symbType+" "+nameDecl+"' declared at line "+lineDecl +": \n" + nbArg + " argument(s) are required but " + nbArg2 + " found";
    messageError(line,s);
  }	
  
  private void messageErrorVariableStar(String nameVariableStar, String nameMethod ,String line) throws TomException {
    String s = "List variable '" + nameVariableStar + "' cannot be used in '" + nameMethod + "'";
    messageError(line,s);
  }
  
  private void messageTypesOperatorError(String type, int slotPosition, String name, String line) throws TomException {
    String s = "Slot position "+slotPosition + " of operator '"+ name + "' has an unknown type: '" + type + "'";
    messageError(line,s);
  }
  
  private void messageTypeOperatorError(String type, String name, String line) throws TomException {
    String s = "Operator '" + name + "' has an unknown return type: '" + type + "'";
    messageError(line,s);
  }
  
  private void messageSymbolError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name, optionList);
    String s = "Symbol method : '" + name + "' not found";
    messageError(line,s);
  }
  
  private void messageRuleSymbolError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name, optionList);
    String s = "Single variable is not allowed in left part of '%rule': " + name;
    messageError(line,s);
  }
  
  private void messageBracketError(TomTerm subject) throws TomException {
    String s = "[] are not allowed in lists, arrays and constants:";
    String line = "";
    %match(TomTerm subject) {
      RecordAppl[option=Option(list),astName=Name[string=name]] -> {
        line = findOriginTrackingLine(name,list);
        s += "'"  + name + "'";
      }
    }
    messageError(line,s);
  }
  
    /************************************
     * Error messages which generate no exit, but the program is wrong    *
     ************************************/
  
  private void messageRuleErrorUnknownVariable(String name, String line) {
    System.out.println(" *** Variable '"+name+"' is strange - Line : "+line+" ***");
    Flags.findErrors = true;
  }
  
  private void messageErrorRuleTypeAndConstructorEgality(String  name, String nameExpected, String type, String typeExpected, OptionList optionList) {
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Error in %rule before '->' - Line : "+line);
    System.out.println(" *** '" + nameExpected + "' of type '" + typeExpected +
                       "' is expected, but '" + name + "' of type '" + type +
                       "' is given");
    System.out.println("This should be a warning only !!!");
    Flags.findErrors = true;
  }
  
  private void messageRuleErrorRhsImpossiblePlaceholder(OptionList optionList) {
    String line = findOriginTrackingLine(optionList);
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Underscores are not allowed in right part of rules. See %rule declared line "+declLine);
    System.out.println(" *** '_' is impossible - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void messageMacroFunctionRepeated(String nameFunction, Option orgTrack, String declType) {
    String line = "not found", name = "unknown", nameDecl = "unknown", lineDecl = "not found";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking[astName=Name[string=orgName],line=Line[string=orgLine]], OriginTracking[astName=Name[string=declOrgName],line=Line[string=declOrgLine]] -> {
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
      }
    } 
    System.out.println("\n *** Repeated macro-functions in declaration '"+declType+" "+nameDecl+"' declared at line "+lineDecl);
    System.out.println(" *** '" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  }   

  private void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
    String line = "not found";
    String name = "unknown";
    %match(Option currentTomStructureOrgTrack) {
      OriginTracking[astName=Name[string=orgName],line=Line[string=orgLine]] -> {
        line = orgLine;
        name = orgName;
      }
    }
    System.out.println("\n *** Missing macro-functions for '"+nameConstruct+" "+name+"' declared at line "+line);
    System.out.println(" *** Missing functions : "+list);
    Flags.findErrors = true;
  }
  
  private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType) {
    String line = "not found", name = "unknown", nameDecl = "unknown", lineDecl = "not found";
    %match(Option orgTrack, Option currentTomStructureOrgTrack) {
      OriginTracking[astName=Name[string=orgName],line=Line[string=orgLine]], OriginTracking[astName=Name[string=declOrgName],line=Line[string=declOrgLine]] -> {
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
      }
    }
    System.out.println("\n *** Arguments must be linear in declaration '"+nameFunction+"' for '"+declType+" "+nameDecl+"' declared at line "+lineDecl);
    System.out.println(" *** Variable '"+nameVar+"' is repeated - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void  messageMakeNotFoundForRule( String name, String line ) {
    System.out.println("\n *** Make declaration not found for operator '"+name+"' - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void messageSlotRepeatedError(TomTerm pairSlotName, String name) {
    System.out.println("\n"+" *** Same slot names can not be used in same method");
    %match( TomTerm pairSlotName ) {
      Pair[appl=Appl[option=Option(list)]] -> {
        String line = findOriginTrackingLine(list);
        System.out.println(" *** Slot Name : '"+name+"' - Line : "+line);
      }
    }
    Flags.findErrors = true;
  }
  
  private void messageSlotNameError(TomTerm pairSlotName, ArrayList listOfPossibleSlot) {
    %match( TomTerm pairSlotName ) {
      Pair[slotName=SlotName(name),appl=Appl[option=Option(list)]] -> {
        String line = findOriginTrackingLine(list);
        System.out.println("\n"+" *** Slot Name '" + name + "' is strange"+" -  Line : "+line);
      }
    }
    System.out.println(" *** Possible Slot Names are : "+listOfPossibleSlot);
    System.out.println("pairSlotName = " + pairSlotName);
    Flags.findErrors = true;
  }
  
  private void messageSlotNumberError(TomTerm pairSlotName, int nbSlot, int nbPair) {
    System.out.println("\n"+" *** Bad number of Slot Name");
    %match( TomTerm pairSlotName ) {
      Pair[appl=Appl[option=Option(list)]] -> {
        String line = findOriginTrackingLine(list);
        System.out.println(" *** "+nbSlot+" are possible, but "+nbPair+" are given"+" - Line : "+line);
      }
    }
    Flags.findErrors = true;
  }
  
    /******************************************
     * Warning message error : maybe an error Dangerous situation   *
     ******************************************/
   // Link directly to Match structure
  private void messageMatchErrorTypeArgument(int slotNumber, String expectedType, String givenType, String line) {
    if(Flags.noWarning) return;
    String orgLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("*** Warning *** Bad type in %match declared line "+orgLine);
    System.out.println("*** For slot "+ slotNumber +" :Type '"+expectedType+"' required but Type '"+givenType+"' found"+" - Line : "+line);
  }

    // Link directly to Match structure
  private void messageRuleErrorTypeEgality(String name, String type, String typeExpected, OptionList optionList) {
    if(Flags.noWarning) return;
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String line = findOriginTrackingLine(name, optionList);
    System.out.println(" *** Warning *** Error in right part of %rule declared line "+declLine);
    System.out.println(" *** Type '" + typeExpected + "' expected but '" + name + "' found: Ensure the type coherence by yourself line : " + line);
  }  
  
    // Link directly to Type structure
  private void messageTypeErrorYetDefined(String name) {
    if(Flags.noWarning) return;
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println(" *** Warning *** Multiple definition of type");
    System.out.println(" *** Type '"+ name +"' is already defined - Line : "+declLine+" ***");
  }
  private void messageOperatorYetDefined(String name, String line) {
    if(Flags.noWarning) return;
    System.out.println(" *** Warning *** Multiple definition of operator");
    System.out.println(" *** Operator '"+ name +"' is already defined - Line : "+line+" ***");
  }

  private void messageTypeArgumentMethodError( String name, TomType oneIn, TomType oneOut, String oneOutName, int numArg, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning *** Bad method arument type");
    String out = oneOut.getTomType().getString();
    String in  = oneIn.getTomType().getString();
    System.out.println(" *** '" + oneOutName + "' returns an object of type '" + out + "' but type '" + in + "' is required");
  }
  
  private void messageRepeatedVariableError( String name, String typeFind, String typeExpected, String line) {
    if(Flags.noWarning) return;
    
    System.out.println("\n"+" *** Warning ***");
    System.out.println(" *** Repeated variable with different types - Line : "+line);
    System.out.println(" *** Variable '" + name + "' has two types : '" + typeFind + "' and '" + typeExpected + "'");
  }

    /////////////////////////
    //   GLOBAL METHODS    //
    /////////////////////////
  
    // findOriginTrackingLine(_) method returns the first number of line (stocked in optionList).
  private String findOriginTrackingLine(OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        OriginTracking[line=Line[string=line]] -> {
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
        orgTrack@OriginTracking[line=Line[string=line]] -> {
          return orgTrack;
        }
      }
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    System.exit(1);return null;
  }

    // findOriginTrackingLine(_) method returns the first number of line (stocked in option)
  private String findOriginTrackingLine(Option option) {
    return option.getLine().getString();
  }
  
    // We have a structure contained only an TomList and we return this ATermList.
  private TomList extractList(TomTerm t) {
    %match( TomTerm t ) {
      SubjectList(list) -> { return list; }
      PatternList(list) -> { return list; }
      TermList(list)    -> { return list; }
    }
    return empty();
  } 
  
    /*
      We extract informations about variables (i.e. for object as symbolTable().getSymbol(_) == null)
      which appear in term. 
      term has an Appl structure.
      nameVariable and lineVariable stock informations about these variables.
      For a same index, we have informations about same variable.
    */
  private void extractVariable(TomTerm term, ArrayList nameVariable, ArrayList lineVariable) {
    %match(TomTerm term) {
      Appl[option=Option(optionList),astName=Name[string=name1],args=l] -> {
          // If l != [] then name1 could not be a variable.
        if ( symbolTable().getSymbol(name1)==null && l.isEmpty() ) { 
          nameVariable.add(name1);
          lineVariable.add(findOriginTrackingLine(name1,optionList));
        }
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
          // we extract variables of arguments of this Appl struture.
        extractVariableList(l, nameVariable, lineVariable);
        return;
      }
      RecordAppl(Option(optionList),Name(tomName),pair) -> {
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
          // we extract variables of arguments of this Appl struture.
        extractVariablePair(pair, nameVariable, lineVariable);        
      }
      _ -> { return; }
    }
  }
  
  private void extractAnnotedVariable(OptionList options, ArrayList nameVariable, ArrayList lineVariable) {
    while(!options.isEmptyOptionList()) {
      Option subject = options.getHead();
      %match(Option subject) {
        TomNameToOption(Name(name1)) -> {
          nameVariable.add(name1);
          lineVariable.add("Tchou tchou unknown");
        }
      }
      options = options.getTail();
    }
  }
  
    /*
      this method completes extractVariable.
      It applies extractVariable on each term of termList.
      We transmit nameVariable and lineVariable in order to stock informations about these variables.
    */
  private void extractVariableList(TomList termList, ArrayList nameVariable, ArrayList lineVariable){
    while( !termList.isEmpty() ) {
      TomTerm term = termList.getHead(); 	
      extractVariable(term, nameVariable, lineVariable);
      termList = termList.getTail();
    }
  }
  private void extractVariablePair(TomList pairList, ArrayList nameVariable, ArrayList lineVariable){
    while( !pairList.isEmpty() ) {
      TomTerm pair = pairList.getHead(); 	
      extractVariable(pair.getAppl(), nameVariable, lineVariable);
      pairList = pairList.getTail();
    }
  }
  
    /*
      term is an element extract of SymbolTable.
      We returns the result TomType of concerned element.
    */
  private String extractType(TomSymbol term) {
    %match(TomSymbol term) {
      s@Symbol[typesToType=TypesToType(_,t)] -> {
        return getTomType(t);
      }
    }
    return null;
  }
  
    /*
      term is an element extract of SymbolTable.
      typeIn returns the type structure of arguments for concerned element.
    */
  private TomList typeIn( TomSymbol term ) {
    %match(TomSymbol term) {
      Symbol[typesToType=TypesToType(list,_)] -> { return list; }
    }
    return empty();
  }

    /*
      term is an element extract of SymbolTable.
      typeOut returns the result type structure of concerned element.
    */
  private TomType typeOut(TomSymbol term ) {
    %match(TomSymbol term) {
      Symbol[typesToType=TypesToType(_,t)] -> { return t; }
    }
    return `EmptyType();
  } 

    /*
      findTypeOf is used in 'testRuleTypeEgality' method of TomVerifier.t.
      we look if an object with name 'name' exists in 'inTerm' which has an Appl Structure.
      and we returns its type. This is used when we have any information about type of 'name' object.
      So we are obliged to deduce the type thanks to the structure in which this object is used.
      If 'name' is used in 'inTerm' structure : it is an arguments.
    */		
  private TomType findTypeOf(String name, TomTerm inTerm) {
      // if no type for 'name' is found, we return EmptyType
    TomType type = `EmptyType();
    %match( TomTerm inTerm ) {
      Appl[astName=Name[string=name1],args=list] -> {
        TomList typeList = empty();
        int numArg = 0;
        boolean find = false;
          // we look if 'name' is an argument and we memorize the position of this argument
        for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
          numArg++;
          TomTerm t = l.getHead();
          %match(TomTerm t) {
            Appl[astName=Name[string=name2]] -> {
              find = (name2.equals(name));
            }
          }
        }
          // if 'name' is an argument, we search the needed argument type thanks to the memorized position.
        if(find) {
          typeList = typeIn(symbolTable().getSymbol(name1));
          for(int i = 1; i < numArg; i++ ) {
            typeList = typeList.getTail();
          }
          type = typeList.getHead().getAstType();
        } else {
            // if 'name' is not an argument, we look if 'name' is an argument of methods given in argument.
          for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
            TomTerm t = l.getHead();
            type = findTypeOf(name, t);
            find = true;
            %match(TomType type) {
                // no type for 'name' found
              EmptyType() -> { find = false; }
            }
          }
        }
      }
      RecordAppl[] -> {
        System.out.println("findTypeOf called on record\n"+name+" : "+ inTerm);
      }
    }
    return type;
  }
}
