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
  private Option optionMatchTypeVariable;  
  
  public TomVerifier(jtom.TomEnvironment environment) { 
    super(environment);
  }
  
    // ------------------------------------------------------------
  %include { Tom.signature }
    // ------------------------------------------------------------
  
  public void verify(TomTerm parsedTerm) throws TomException {
    if(!Flags.doVerify) return;
    Collect collectAndVerify = new Collect() 
      {  
        public boolean apply(ATerm term) {
          if(term instanceof TomTerm) {
            %match(TomTerm term) { 
              Match(orgTrack, SubjectList(matchArgsList), PatternList(patternActionList)) -> {  
                try{
                  verifyMatch(orgTrack, matchArgsList, patternActionList);
                }catch (TomException e) {
                  System.out.println("TomVerifier catch:" + e);
                  System.exit(1);
                }
                return false;
              }
              RuleSet(orgTrack, list) -> {
                try{verifyRule(orgTrack, list);
                return false;
                }catch (TomException e) {
                  System.out.println("TomVerifier catch:" + e);
                  System.exit(1);
                }
              }
              DeclarationToTomTerm(declaration) -> {
                try{
                  verifyDeclaration(declaration);
                  return false;
                }catch (TomException e) {
                  System.out.println("TomVerifier catch:" + e);
                  System.exit(1);
                }
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
  private void verifyMatch(Option orgTrack, TomList subjectList, TomList patternList) throws TomException {
    ArrayList typeMatchArgs = new ArrayList();
    while( !subjectList.isEmpty()) {
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
      verifyMatchPattern(terms, typeMatchArgs, orgTrack);
      patternList = patternList.getTail();
    }
  }
  
  private void verifyMatchPattern(TomTerm pattern, ArrayList typeMatchArgs, Option orgTrack) throws TomException {
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
          messageErrorMatchVariableStar(name, line, orgTrack); 
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
      messageNumberArgumentMatchError(nbExpectedArgs, nbFoundArgs, line, orgTrack); 
    }
      // we test the types egality between arguments and pattern-action
    for( int slot = 0; slot < nbExpectedArgs; slot++ ) {
      if ( (foundTypeMatch.get(slot) != typeMatchArgs.get(slot)) && (foundTypeMatch.get(slot) != null))
      { 	
        messageTypeArgumentMatchError(slot+1, (String) typeMatchArgs.get(slot), (String) foundTypeMatch.get(slot), line, orgTrack); 
      }
    }
  }
  
    ////////////////////////////////
    // RULE VERIFICATION CONCERNS //
    ////////////////////////////////
  private void verifyRule(Option orgTrack, TomList ruleList) throws TomException {
    int i =0;
    TomTerm currentRule;
    ArrayList nameAndType = new ArrayList();
    while(!ruleList.isEmpty()) {
      currentRule = ruleList.getHead();
      matchBlock: {
        %match(TomTerm currentRule) {
          RewriteRule(Term(lhs),Term(rhs)) -> {
            verifyNoUnderscoreRuleRhs(rhs, orgTrack);
            nameAndType = verifyLhsRuleTypeAndConstructorEgality(lhs, nameAndType, i, orgTrack);
            verifyLhsAndRhsRuleTypeEgality(rhs, lhs, nameAndType, orgTrack);
            verifyRuleVariable(lhs,rhs);
            if(i ==0) verifyMultipleDefinitionOfSymbol((String)nameAndType.get(0), findOriginTrackingLine(orgTrack));
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
  
  private void verifyNoUnderscoreRuleRhs(TomTerm ruleRhs, Option orgTrack) {
    matchBlock: {
      %match(TomTerm ruleRhs) {
        Appl[args=argsList] -> {
          while(!argsList.isEmpty()) {
            TomTerm oneArgs = argsList.getHead();
            verifyNoUnderscoreRuleRhs(oneArgs, orgTrack);
            argsList = argsList.getTail();
          }
          break matchBlock;
        }
        Placeholder[option=Option(t)] -> {
          messageImpossibleUnderscore(t, orgTrack);
          break matchBlock;
        }
        VariableStar[option=Option(list), astName=Name[string=name1]] -> {
          break matchBlock;
        }
        RecordAppl(option,Name(tomName),args) -> {
          System.out.println("Warning prefer f() instead of f[] or Message for impossible record on rhs of rule");
          System.exit(1);
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
      lhs is the left part of one '->'. nameType contains name and type of needed constructor.
      ruleNumber is used to known if it is the first rule of the readed %rule.
      NB : another test, in order to forbid alone variables in the left part of '->', is already made in
      'context,RewriteRule' case of 'pass1' method in TomChecker.t and runs 'messageRuleSymbolError' method. 
      That's why the case of alone variables is not made here.
    */
  private ArrayList verifyLhsRuleTypeAndConstructorEgality(TomTerm lhs, ArrayList nameType, int ruleNumber, Option orgTrack) throws TomException {
    %match(TomTerm lhs) {
      Appl[option=Option(list),astName=Name[string=name]] -> {
        TomType term = typeOut(symbolTable().getSymbol(name));
        %match(TomType term) {
          TomTypeAlone[string=t] -> { 
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.add(name);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name != nameType.get(0) ) ) {
                messageRuleTypeAndConstructorEgality(name,(String)nameType.get(0),t,(String)nameType.get(1),list);
              }
            }
          }
        }
      }
      VariableStar[option=Option(list), astName=Name[string=name1]] -> { 
        String line = findOriginTrackingLine(name1,list);
        messageErrorRuleVariableStar(name1, line, orgTrack); 
      }
      Placeholder[option=Option(t)] -> { 
        messageImpossibleInRule(t, orgTrack); 
      }
      RecordAppl(option,Name(tomName),args) -> {
        System.out.println("What to do with Record on lhs of rule");
        System.exit(1);
      }
    }
    return nameType;
  }
  
    // We test the non existence of variables from the right part of '->' in the left part of '->'.
  private void verifyRuleVariable(TomTerm lhs, TomTerm rhs) throws TomException {
      // We extract variable informations of the left part.
    ArrayList nameVariableIn = new ArrayList();
    ArrayList lineVariableIn = new ArrayList();
    testApplStructure(lhs);
    extractVariable(lhs,nameVariableIn,lineVariableIn);
      // We extract variable informations of the right part.
    ArrayList nameVariableOut = new ArrayList();
    ArrayList lineVariableOut = new ArrayList();
    testApplStructure(rhs);	
    extractVariable(rhs,nameVariableOut,lineVariableOut);
      // We test the existence of the right part in left part.
    int n = nameVariableOut.size();
    for(int i = 0; i < n; i++) {
      if(!nameVariableIn.contains(nameVariableOut.get(i))) {
        messageVariableError((String)nameVariableOut.get(i),(String)lineVariableOut.get(i));
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
  private void verifyLhsAndRhsRuleTypeEgality(TomTerm rhs, TomTerm lhs, ArrayList nameAndTypeOfRule, Option orgTrack) throws TomException {
    statistics().numberRulesTested++;
    String expectedTypeName = (String)nameAndTypeOfRule.get(1);
    String foundTypeName = "Not found?";
    
    if ( !nameAndTypeOfRule.isEmpty() ) {
        // we test if the type of the right part is the same that the left part
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
                //TomType typeFind = findTypeOf(name1, lhs);
              TomType typeFind = extractTypeOf(name1, lhs);
              %match(TomType typeFind) {
                TomTypeAlone[string=t] -> {  foundTypeName = t;}
              }
            }
          }
          if ( foundTypeName != nameAndTypeOfRule.get(1) ) {
            messageRuleTypeEgality(name1, foundTypeName, expectedTypeName, list, orgTrack);
          }
        }
      } 
    } else {
      System.out.println("Is it possible??");
      System.exit(1);
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
        verifyMultipleDefinitionOfType(tomName, orgTrack);
        verifyTypeDecl("%typeterm", tomList, orgTrack);
      }
      
      TypeListDecl(Name(tomName), tomList, orgTrack) -> {
        verifyMultipleDefinitionOfType(tomName, orgTrack);
        verifyTypeDecl("%typelist", tomList, orgTrack);
      }
      
      TypeArrayDecl(Name(tomName), tomList, orgTrack) -> {
        verifyMultipleDefinitionOfType(tomName, orgTrack);
        verifyTypeDecl("%typearray", tomList, orgTrack);
      }
    }
  }
  
    ///////////////////////////////
    // TYPE DECLARATION CONCERNS //
    ///////////////////////////////
  private void verifyTypeDecl(String declType, TomList listOfDeclaration, Option declOrgTrack) throws TomException {
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
          checkField("get_fun_sym",verifyList,orgTrack, declType, declOrgTrack);
        }
        DeclarationToTomTerm(CompareFunctionSymbolDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
          checkFieldAndLinearArgs("cmp_fun_sym",verifyList,orgTrack,name1,name2, declType, declOrgTrack);
        }
        DeclarationToTomTerm(TermsEqualDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
          checkFieldAndLinearArgs("equals",verifyList,orgTrack,name1,name2, declType, declOrgTrack);
        }
          /*Specific to typeterm*/
        DeclarationToTomTerm(GetSubtermDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> {
          checkFieldAndLinearArgs("get_subterm",verifyList,orgTrack,name1,name2, declType, declOrgTrack);
        }
          /*Specific to typeList*/
        DeclarationToTomTerm(GetHeadDecl[orgTrack=orgTrack]) -> {
          checkField("get_head",verifyList,orgTrack, declType, declOrgTrack);
        }
        DeclarationToTomTerm(GetTailDecl[orgTrack=orgTrack]) -> {
          checkField("get_tail",verifyList,orgTrack, declType, declOrgTrack);
        }
        DeclarationToTomTerm(IsEmptyDecl[orgTrack=orgTrack]) -> {
          checkField("is_empty",verifyList,orgTrack, declType, declOrgTrack);
        }
          /*Specific to typeArray*/
        DeclarationToTomTerm(GetElementDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> { 
          checkFieldAndLinearArgs("get_element",verifyList,orgTrack,name1,name2, declType, declOrgTrack);
        }
        DeclarationToTomTerm(GetSizeDecl[orgTrack=orgTrack]) -> {
          checkField("get_size",verifyList,orgTrack, declType, declOrgTrack);
        }
      }
      listOfDeclaration = listOfDeclaration.getTail();
    }
    
    if(verifyList.contains("equals")) {
      verifyList.remove(verifyList.indexOf("equals"));
        // Maybe a warning ???
    }    
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(declType, verifyList, declOrgTrack);
    }
  }
  
  private void verifyMultipleDefinitionOfType(String name, Option orgTrack) {
    if(alreadyStudiedType.contains(name)) {
      String line = findOriginTrackingLine(orgTrack);
      messageTypeYetDefined(name,line);
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
    TomList l = getSymbolDomain(tomSymbol);
    TomType type = getSymbolCodomain(tomSymbol);
    String name = tomSymbol.getAstName().getString();
    String line  = findOriginTrackingLine(optionList);
    Option orgTrack = findOriginTracking(optionList);
    verifyMultipleDefinitionOfSymbol(name, line);
    verifySymbolCodomain(type.getString(), name, line);
    nbArgs = verifyAndCountSymbolArguments(l, name, line);
    verifySymbolOptions(symbolType, optionList, nbArgs, orgTrack);
  }
  
  private void verifyMultipleDefinitionOfSymbol(String name, String line) {
    if(alreadyStudiedSymbol.contains(name)) {
      messageOperatorYetDefined(name,line);
    }
    else
      alreadyStudiedSymbol.add(name);
  }
  
  private void verifySymbolCodomain(String returnTypeName, String symbName, String symbLine) throws TomException  {
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
  
  private void verifySymbolOptions(String symbType, OptionList list, int expectedNbMakeArgs, Option declOrgTrack) throws TomException {
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
        DeclarationToOption(MakeEmptyArray[orgTrack=orgTrackVal]) -> { 
          checkField("make_empty",verifyList,orgTrackVal, symbType, declOrgTrack);
        }
        DeclarationToOption(MakeAddArray[varList=Variable[astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]], orgTrack=orgTrackVal]) -> {
          checkFieldAndLinearArgs("make_append", verifyList, orgTrackVal, name1, name2, symbType, declOrgTrack);
        }
          /*for a List symbol*/
        DeclarationToOption(MakeEmptyList[orgTrack=orgTrackVal]) -> {
          checkField("make_empty",verifyList,orgTrackVal, symbType, declOrgTrack);
          
        }
        DeclarationToOption(MakeAddList[varList=Variable[astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]], orgTrack=orgTrackVal]) -> {
          checkFieldAndLinearArgs("make_insert", verifyList, orgTrackVal, name1, name2, symbType, declOrgTrack);
        }
          /*for a symbol*/
        DeclarationToOption(MakeDecl[astName=Name[string=nameSymbol],args=makeArgsList, orgTrack=orgTrackVal]) -> {
          checkField("make", verifyList, orgTrackVal, symbType, declOrgTrack);
          verifyMakeDeclArgs(makeArgsList, expectedNbMakeArgs, orgTrackVal, symbType, declOrgTrack);
        }
      }
      list = list.getTail();
    }
    
    if(!verifyList.isEmpty()) {
      messageMissingMacroFunctions(symbType, verifyList, declOrgTrack);
    }
  }

  private void verifyMakeDeclArgs(TomList argsList, int nbMakeArgs, Option orgTrackVal, String symbType, Option declOrgTrack)  throws TomException {
      // we test the necessity to use different names for each variable-paremeter.
    ArrayList listVar = new ArrayList();
    int nbArgsFound =0;
    while(!argsList.isEmpty()) {
      TomTerm termVar = argsList.getHead();
      %match(TomTerm termVar) {
        Variable[option=Option(listOption), astName=Name[string=name]] -> {
          if(listVar.contains(name)) {
            messageTwoSameNameVariableError("make", name, orgTrackVal, symbType, declOrgTrack);
          } else {
            listVar.add(name);
          }
        }
      }
      argsList = argsList.getTail();
      nbArgsFound++;
    }
    if(nbArgsFound != nbMakeArgs) {
      messageNumberArgumentsError(nbMakeArgs,nbArgsFound,orgTrackVal, symbType, declOrgTrack);
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
  private void testApplStructure(OptionList list, String name, TomList argsList) throws TomException {
    statistics().numberApplStructuresTested++;
      //in case of oparray or oplist, the number of arguments is not tested
    boolean arrayOrList = false;
    TomSymbol symbol = symbolTable().getSymbol(name);
    if(symbol==null && !argsList.isEmpty()) {
      System.out.println("The Appl structure contains an unknown symbol: function call? ensure it is correct");
        //messageSymbolError(name, list);
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
      System.out.println("testRecordStructure no symbol found: no check possible: Symbol error ?");
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
  private void testNumberAndRepeatedSlotName(TomList pairList, TomList declaredSlotNameList) throws TomException {
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
  
    /*
      testVariableWithoutParen is used in 'pass1' method of TomChecker.t.
      It is called before to transform Appl into Variable. Indeed when we create an Appl
      in 'PlainTerm' method of TomParser.jj, we do not known if it will be a variable or not.
      But variable with () is not a recommanded structure for a variable. So we add informations
      thanks to 'ast().makeLRParen(name.image))' which is added to options in
      the case of () [in 'PlainTerm' method of TomParser.jj]. When we transform Appl in Variable (when
      it is necessary), we test if 'LRParen(_)' is in the option structure. 
    */
  public void testVariableWithoutParen(Option option, String name) throws TomException {
    if(!Flags.doVerify) return;
    
    OptionList optionList = option.getOptionList();
    Option lrParen = getLRParen(optionList);
    if(lrParen!=null) {
      String nameLrParen = lrParen.getAstName().getString();
      if(name.equals(nameLrParen)) {
        String line = findOriginTrackingLine(name, optionList);
        messageVariableWithParenError(name,line);
      }
    }
  }
  
    /////////////////////////
    //   MESSAGE METHODS   //
    /////////////////////////
    /********************************
     * Error messages which generate immediatly an exit
     ********************************/
  private void messageError(String line, String msg) throws TomException {
    if(!Flags.doVerify) return;
    String s = "-- line: " + line + "\n-- " + msg; 
    throw new CheckErrorException(s);
  }
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, String name, String line) throws TomException {
    String s = "Bad number of arguments for method '" + name + "':" +
      nbArg + " arguments are required but " + nbArg2 + " are given";
    messageError(line,s);
  }	
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, Option orgTrack, String symbType, Option declOrgTrack) throws TomException {
    String line = "not found";
    String name = "unknown";
    String lineDecl = "not found";
    String nameDecl = "unknown";
    %match(Option orgTrack, Option declOrgTrack) {
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
  
  private void messageImpossibleInRule(OptionList optionList, Option orgTrack) throws TomException {
    String declLine = findOriginTrackingLine(orgTrack);
    String line = findOriginTrackingLine(optionList);
    String s = "Single '_' are not allowed in %rule declared line " +declLine;
    messageError(line,s);
  }
  private void messageErrorVariableStar(String nameVariableStar, String nameMethod ,String line) throws TomException {
    String s = "List variable '" + nameVariableStar + "' cannot be used in '" + nameMethod + "'";
    messageError(line,s);
  }
  
  private void messageErrorRuleVariableStar(String nameVariableStar, String line, Option orgTrack) throws TomException {
    String lineDecl = findOriginTrackingLine(orgTrack);
    String s = "Single list variable (" + nameVariableStar + "*) is not allowed in %rule declared line "+lineDecl;
    messageError(line,s);
  }
  
  private void messageErrorMatchVariableStar(String nameVariableStar, String line, Option orgTrack) throws TomException {
    String lineDecl = findOriginTrackingLine(orgTrack);
    String s = "Single list variable (" + nameVariableStar + "*) is not allowed in %match declared line "+lineDecl;
    messageError(line,s);
  }
  
  private void messageNumberArgumentMatchError( int nbExpectedVar, int nbFoundVar, String line, Option orgTrack) throws TomException {
    String lineDecl = findOriginTrackingLine(orgTrack);
    String s = "Bad number of arguments in %match declared line "+lineDecl+": " + nbExpectedVar + 
      " argument(s) required but " + nbFoundVar + " found"; 
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
  
  public void messageSymbolError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name, optionList);
    String s = "Symbol method : '" + name + "' not found";
    messageError(line,s);
  }
  
  public void messageRuleSymbolError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name, optionList);
    String s = "Single variable is not allowed in left part of '%rule': " + name;
    messageError(line,s);
  }
  
// messageMatchTypeVariableError is called by 'contest,GlVar' case in 'pass1' method of TomChecker.t.
  public void messageMatchTypeVariableError(String name, String type) throws TomException {
    OptionList optionList = optionMatchTypeVariable.getOptionList();
    String line = findOriginTrackingLine("Match", optionList);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct";
    messageError(line,s);
  }
  
  public void messageBracketError(TomTerm subject) throws TomException {
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
  
  private void messageVariableError(String name, String line) {
    System.out.println("\n"+" *** Variable '"+name+"' is strange - Line : "+line+" ***");
    Flags.findErrors = true;
  }
  
  private void messageRuleTypeAndConstructorEgality(String  name, String nameExpected, String type, String typeExpected, OptionList optionList) {
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Error in %rule before '->' - Line : "+line);
    System.out.println(" *** '" + nameExpected + "' of type '" + typeExpected +
                       "' is expected, but '" + name + "' of type '" + type +
                       "' is given");
    Flags.findErrors = true;
  }
  
  private void messageImpossibleUnderscore(OptionList optionList, Option orgTrack) {
    String line = findOriginTrackingLine(optionList);
    String declLine = findOriginTrackingLine(orgTrack);
    System.out.println("\n *** Underscores are not allowed in right part of rules. See %rule declared line "+declLine);
    System.out.println(" *** '_' is impossible - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void messageMacroFunctionRepeated(String nameFunction, String line) {
    System.out.println("\n *** Repeated macro-functions : ");
    System.out.println(" *** '" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  }
  
  private void messageMacroFunctionRepeated(String nameFunction, Option orgTrack, String declType, Option declOrgTrack) {
    String line = "not found";
    String name = "unknown";    
    String lineDecl = "not found";
    String nameDecl = "unknown";
    %match(Option orgTrack, Option declOrgTrack) {
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

  private void messageMissingMacroFunctions(String nameConstruct, ArrayList list, Option orgTrack) {
    String line = "not found";
    String name = "unknown";
    %match(Option orgTrack) {
      OriginTracking[astName=Name[string=orgName],line=Line[string=orgLine]] -> {
        line = orgLine;
        name = orgName;
      }
    }
    System.out.println("\n *** Missing macro-functions for '"+nameConstruct+" "+name+"' declared at line "+line);
    System.out.println(" *** Missing functions : "+list);
    Flags.findErrors = true;
  }
  
  private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType, Option declOrgTrack) {
    String line = "not found";
    String name = "unknown";
    String lineDecl = "not found";
    String nameDecl = "unknown";
    %match(Option orgTrack, Option declOrgTrack) {
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
  
  private void messageTypeArgumentMatchError(int slotNumber, String expectedType, String givenType, String line, Option orgTrack) {
    if(Flags.noWarning) return;
    String orgLine = findOriginTrackingLine(orgTrack);
    System.out.println("\n"+" *** Warning *** Bad type in %match declared line "+orgLine);
    System.out.println(" *** For slot "+ slotNumber +" :Type '"+expectedType+"' required but Type '"+givenType+"' found"+" - Line : "+line);
  }
  
  
  private void messageTypeArgumentMethodError( String name, TomType oneIn, TomType oneOut, String oneOutName, int numArg, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning ***");
    System.out.println(" *** Bad type for argument number '" + numArg + "' in method '" + name + "' - Line : " + line);
    String out = oneOut.getTomType().getString();
    String in  = oneIn.getTomType().getString();
    System.out.println(" *** '" + oneOutName +
                       "' returns an object of type '" + out +
                       "' but type '" + in + "' is required");
  }
  
  private void messageRepeatedVariableError( String name, String typeFind, String typeExpected, String line) {
    if(Flags.noWarning) return;
    
    System.out.println("\n"+" *** Warning ***");
    System.out.println(" *** Repeated variable with different types - Line : "+line);
    System.out.println(" *** Variable '" + name + "' has two types : '"
                       + typeFind + "' and '" + typeExpected + "'");
  }
  
  private void messageRuleTypeEgality(String name, String type, String typeExpected, OptionList optionList, Option orgTrack) {
    if(Flags.noWarning) return;
    String declLine = findOriginTrackingLine(orgTrack);
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n"+" *** Warning *** Error in %rule declared line "+declLine+" after '->' - Line : " + line);
    System.out.println(" *** Type '" + typeExpected + "' expected but '" + name + "' found: ensure its type by yourself");
  }
  
  private void messageVariableWithParenError( String  name, String line ) {
    if(Flags.noWarning) return;
    System.out.println("\n *** Warning *** Variable with () is not recommanded");
    System.out.println(" *** Variable '"+name+"' has () - Line : "+line);
  }
  
  private void messageOperatorYetDefined(String name, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning *** Multiple definition of operator");
    System.out.println(" *** Operator '"+ name +"' is already defined - Line : "+line+" ***");
  }
  
  private void messageTypeYetDefined(String name, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning *** Multiple definition of type");
    System.out.println(" *** Type '"+ name +"' is already defined - Line : "+line+" ***");
  }
  
    /*************************************************
     ***  In this part, there are global functions  ***
     *************************************************/
  private void checkField(String field, ArrayList findFunctions, Option orgTrack, String declType, Option declOrgTrack) throws TomException {
    if(findFunctions.contains(field)) {
      findFunctions.remove(findFunctions.indexOf(field)); 
    } else {
      messageMacroFunctionRepeated(field, orgTrack, declType, declOrgTrack);
    }
  }
  
  private void checkFieldAndLinearArgs(String field, ArrayList findFunctions, Option orgTrack, String name1, String name2, String declType, Option declOrgTrack) throws TomException {
    checkField(field,findFunctions, orgTrack, declType, declOrgTrack);
    if(name1.equals(name2)) { 
      messageTwoSameNameVariableError(field, name1, orgTrack, declType, declOrgTrack);
    }
  }
    /*
      findOriginTrackingLine(_,_) method returns the line (stocked in optionList)  of object 'name'.
      Information about line is stocked in the TomList contained in Option().
      optionList is the TomList contained in Option() and name is the name of the
      object of which we search line.
      Information about line appears in a structure like :
      OriginTracking(Name(name),Line(line))
    */
  private String findOriginTrackingLine(String name, OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
      %match(Option subject) {
        OriginTracking[astName=Name[string=origName],line=Line[string=line]] -> {
          if(name.equals(origName)) {
            return line;
          }
        }
      }
      optionList = optionList.getTail();
    }
    
    System.out.println("findOriginTrackingLine: '" + name + "' not found");
    System.exit(0);
    return null;
  }
  
    /*
      findOriginTrackingLine(_) method returns the last number of line (stocked in optionList).
      Information about line is stocked in the TomList contained in Option().
      optionList is the TomList contained in Option().
      Information about line appears in a structure like :
      OriginTracking(Name(name),Line(line))
    */
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
    System.exit(0);
    return null;
  }
  
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
    System.exit(0);
    return null;
  }
  
  private String findOriginTrackingLine(Option option) {
    %match(Option option) {
      OriginTracking[line=Line[string=line]] -> {
        return line;
      }
    }
    System.out.println("findOriginTrackingLine:  not found:\n"+ option);
    System.exit(0);
    return null;
  }
  
    /*
      It is easy : we have a structure contained only an TomList and
      we return this ATermList.
    */
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
  
    /*
      affectOptionMatchTypeVariable is used in 'context,Match' case in 'pass1' method of TomChecker.t
      It permits to memorize informations in order to known line error for messageMatchTypeVariableError
    */
  public void affectOptionMatchTypeVariable(Option option) {
    optionMatchTypeVariable = option;
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

  private TomType extractTypeOf(String searchVarName, TomTerm lhs) {
    ArrayList nameVariable = new ArrayList();
    ArrayList lineVariable = new ArrayList();
    extractVariable(lhs,nameVariable,lineVariable);
    if(nameVariable.contains(searchVarName)) {
      System.out.println(searchVarName + "found in lhs of rule but what it is type");
      return `EmptyType();
    } else {
      return `EmptyType();
    }
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
    }
    return type;
  }
}
