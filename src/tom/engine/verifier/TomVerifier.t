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
              Match(option, SubjectList(matchArgsList), PatternList(patternActionList)) -> {  
                try{verifyMatch(option, matchArgsList, patternActionList);
                return false;
                }catch (TomException e) {
                  System.out.println("TomVerifier catch:" + e);
                  System.exit(1);
                }
              }
              RuleSet(list) -> {
                try{verifyRule(list);
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
        } // end apply
      }; // end new
    genericCollect(parsedTerm, collectAndVerify);
  }
  
  private void verifyMatch(Option option, TomList subjectList, TomList patternList) throws TomException {
    ArrayList typeTlVarArgs = new ArrayList();
    while( !subjectList.isEmpty() ) {
      TomTerm term = subjectList.getHead();
      %match( TomTerm term ) {
        GLVar[strName=name, astType=TomTypeAlone(type)] -> {
          typeTlVarArgs.add(type);
        }
      }
      subjectList = subjectList.getTail();
    }
    
    while( !patternList.isEmpty() ) {
      statistics().numberMatchesTested++;
      TomTerm terms = patternList.getHead();
      verifyMatchPatterns(terms, typeTlVarArgs);
      patternList = patternList.getTail();
    }
  }
  
  private void verifyMatchPatterns(TomTerm pattern, ArrayList typeTlVarArgs) throws TomException {
    int nbTlVar = typeTlVarArgs.size();
    TomList applList = pattern.getTermList().getList();
      // we initialize list of names and types for tested variables
    ArrayList nameVar = new ArrayList();
    ArrayList typeVar = new ArrayList();
    String line = " - ";
    int n2 = nbTlVar;
    int nbAppl = 0;
    int nbPassInWhile = 0;
    ArrayList typeAppl = new ArrayList();
    
    while( !applList.isEmpty() ) {
      TomTerm termAppl = applList.getHead();
      
      %match(TomTerm termAppl) {
        Appl[option=Option(list), astName=Name[string=name]] -> {
            //  we test the validity of the current Appl structure
          testApplStructure(termAppl);
            // we extract the line number (about current pattern-action) .Here variables are Appl methods still
          line = findOriginTrackingLine(list);
          nbAppl = nbAppl + 1;
          typeAppl.add(extractType(symbolTable().getSymbol(name)));
        }
        Placeholder[] -> {
          nbAppl = nbAppl + 1;
          typeAppl.add((TomTerm) null);
        }
// alone stared variable is impossible
        VariableStar[option=Option(list), astName=Name[string=name]] -> { 
          line = findOriginTrackingLine(name,list);
          messageErrorVariableStarBis(name, line); 
        }
      }
      
/*        //  we test if the number of Appl terms (number of elements) in one pattern-action is <= to the number of elements in %match subject
          if ( n2 > nbPassInWhile ) {
          repeatedVariable(termAppl, nameVar, typeVar, (String) typeTlVarArgs.get(nbPassInWhile));
          }  else {
            // test of type egality for repeated variables
            repeatedVariable(termAppl, nameVar, typeVar, " ");
            }*/
      applList = applList.getTail();
      nbPassInWhile++;
    }
      //  nb elements in %match subject = nb elements in the pattern-action ?
    if(nbTlVar != nbAppl) {
      messageNumberArgumentMatchError(nbTlVar, nbAppl, line); 
    }
      // we test the types egality between arguments and pattern-action
    for( int i = 0; i < nbTlVar; i++ ) {
      if ( (typeAppl.get(i) != typeTlVarArgs.get(i)) && (typeAppl.get(i) != null))
      { 	
        messageTypeArgumentMatchError( (String) typeTlVarArgs.get(i), (String) typeAppl.get(i), line, 1 ); 
      } 
    }
  }

  
  private void verifyRule(TomList ruleList) throws TomException {
    TomTerm currentRule;
    int i =0;
    ArrayList nameAndType = new ArrayList();
    ArrayList yetStudied = new ArrayList();
    while(!ruleList.isEmpty()) {
      currentRule = ruleList.getHead();
      matchBlock: {
        %match(TomTerm currentRule) {
          RewriteRule(lhs,rhs) -> {
            verifyNoUnderscoreRuleRhs(rhs);
            nameAndType = testRuleTypeAndConstructorEgality(lhs, nameAndType, i);
            testRuleTypeEgality(lhs, nameAndType, rhs);
            testMakeDefineAppl(lhs, yetStudied);
            testMakeDefineAppl(rhs, yetStudied);
            testRuleVariable(lhs,rhs);
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

  private void verifyNoUnderscoreRuleRhs(TomTerm ruleRhs) {
    %match(TomTerm ruleRhs) {
      Appl[args=argsList] -> {
        while(!argsList.isEmpty()) {
          TomTerm oneArgs = argsList.getHead();
          verifyNoUnderscoreRuleRhs(oneArgs);
          argsList = argsList.getTail();
        }
      }
      Placeholder[option=Option(t)] -> { 
        messageImpossibleUnderscore(t);
      }
    }
  }

  
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
        DeclarationToTomTerm(GetFunctionSymbolDecl(_,_, orgTrack)) -> {
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
        DeclarationToTomTerm(GetHeadDecl(_,_, orgTrack)) -> {
          checkField("get_head",verifyList,orgTrack, declType, declOrgTrack);
        }
        DeclarationToTomTerm(GetTailDecl(_,_, orgTrack)) -> {
          checkField("get_tail",verifyList,orgTrack, declType, declOrgTrack);
        }
        DeclarationToTomTerm(IsEmptyDecl(_,_, orgTrack)) -> {
          checkField("is_empty",verifyList,orgTrack, declType, declOrgTrack);
        }
        /*Specific to typeArray*/
         DeclarationToTomTerm(GetElementDecl(Variable[astName=Name[string=name1]],Variable[astName=Name[string=name2]],_, orgTrack)) -> { 
          checkFieldAndLinearArgs("get_element",verifyList,orgTrack,name1,name2, declType, declOrgTrack);
        }
        DeclarationToTomTerm(GetSizeDecl(_,_, orgTrack)) -> {
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
  
    /**
     ** TomVerifier.t is composed by three parts. 
     **
     **   The first contains the tests : these functions are
     ** called by the functions of other classes (excepted certains calls
     ** for messages). In the mesure of possible, every needed tests for the traitement
     ** are realised in this file. The most of them have a void type return and theirs uses are
     ** transparent. We make only tests in TomParser.jj and in TomChecker.t.
     ** The name of these functions have a type like : test_
     **
     **   The second part contains all messages : warnings and errors. Indeed, the
     ** tests generated messages thanks to calling these message functions. 
     ** The name of these functions have a type like : message_
     **            
     **   The thirst part contains methods which are used by test and message parts.        
     **/

    /*************************************
     ***  In this part, we make tests  ***
     *************************************/
  
    /*
      testRuleVariable is used in TomChecker.t, in 'context,RewriteRule' case of 'pass1' method.
      We test the existence of variables from the right part of '->' in the left part of '->'.
      We have : lhs -> rhs
    */
  private void testRuleVariable(TomTerm lhs, TomTerm rhs) throws TomException {
      /*
        We extract variable informations of the left part.
        For a same index, we have informations about same variable.
      */
    ArrayList nameVariableIn = new ArrayList();
    ArrayList lineVariableIn = new ArrayList();
    testApplStructure(lhs);
    extractVariable(lhs,nameVariableIn,lineVariableIn);
      /*
        We extract variable informations of the right part.
        For a same index, we have informations about same variable.
      */
    ArrayList nameVariableOut = new ArrayList();
    ArrayList lineVariableOut = new ArrayList();
    testApplStructure(rhs);	
    extractVariable(rhs,nameVariableOut,lineVariableOut);
      /*
        We test the existence of the right part in left part.
      */
    int n = nameVariableOut.size();
    for(int i = 0; i < n; i++) {
      if(!nameVariableIn.contains(nameVariableOut.get(i))) {
        messageVariableError((String)nameVariableOut.get(i),(String)lineVariableOut.get(i));
      }
    }
  }
    

    /*
      testRuleTypeAndConstructorEgality is used in 'RuleConstruct' method of TomParser.jj.
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
  private ArrayList testRuleTypeAndConstructorEgality(TomTerm lhs, ArrayList nameType, int ruleNumber) throws TomException {
    %match(TomTerm lhs) {
      Appl[option=Option(list),astName=Name[string=name1]] -> {
        TomType term = typeOut(symbolTable().getSymbol(name1));
        %match(TomType term) {
          TomTypeAlone[string=t] -> { 
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.add(name1);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name1 != nameType.get(0) ) ) {
                messageRuleTypeAndConstructorEgality(name1,(String)nameType.get(0),t,(String)nameType.get(1),list);
              }
            }
          }
        }
      }
      VariableStar[option=Option(list), astName=Name[string=name1]] -> { 
        String line = findOriginTrackingLine(name1,list);
        messageErrorVariableStarBis(name1, line); 
      }
      Placeholder[option=Option(t)] -> { 
        messageImpossibleInRule(t, "_"); 
      }
    }
    return nameType;
  }

    /*
      testRuleTypeEgality is used in 'RuleConstruct' method of TomParser.jj.
      1. We test the obligation for '->' in %rule to have the same type in left
      and right parts. For this, we use nameType defined thanks to 'testRuleTypeAndConstructorEgality' method
      which returns name and type of constructor in left part.
      2. We test the type egality (in right and left parts) of variables with same name.
      3. the use of variableStar not yet implemented ? => forbiden
      We have lhs -> rhs and nameType difined thanks to 'testRuleTypeAndConstructorEgality' method
      which returns name and type of the constructor in left part.
    */
  private void testRuleTypeEgality(TomTerm rhs, ArrayList nameType, TomTerm lhs) throws TomException {
    statistics().numberRulesTested++;
    ArrayList nameVar = new ArrayList();
    ArrayList typeVar = new ArrayList();
      /* We test the egality of types and names in the right and left parts of '->' */
    repeatedVariable(rhs, nameVar, typeVar, " ");
    repeatedVariable(lhs, nameVar, typeVar, " ");
      /* if rule contains one or more variables */
    if ( !nameType.isEmpty() ) {
        /*
          we test if the type of the right part is the same that the left part
        */
      %match(TomTerm rhs) {
        Appl[option=Option(list),astName=Name[string=name1]] -> {
            /*
              We look the type of the resulted object of this Appl.
              If resulted object has no type, typeOut returns EmptyType) (for example
              if it is a variable).
            */
          TomType term = typeOut(symbolTable().getSymbol(name1));
          %match(TomType term) {
            TomTypeAlone[string=t] -> { 
                /* types are not the same */
              if ( t != nameType.get(1) ) {
                messageRuleTypeEgality(name1,t,(String)nameType.get(1),list);
              }
            }
              /* result wihtout type */
            EmptyType() -> { 
                /*
                  we look if an object with name 'name1' exists in lhs 
                  and we set its type.
                */
              TomType typeFind = findTypeOf(name1, lhs);
              %match(TomType typeFind) {
                  /* types are the same ? */
                TomTypeAlone[string=tm] -> { 
                  if ( tm != nameType.get(1) ) {
                    messageRuleTypeEgality(name1,tm,(String)nameType.get(1),list);
                  }
                }
              }
            }
          }
        }
          /*
            the use of variableStar not yet implemented ?
            if we do not forbid its utilisation, we have message : GenTermMatchingAutomata strange term.
          */
        VariableStar[option=Option(list), astName=Name[string=name1]] -> { 
          String line = findOriginTrackingLine(name1,list);
          messageErrorVariableStarBis(name1, line); 
        }
          /*
            '_' are forbiden
          */
        Placeholder[option=Option(t)] -> { 
          messageImpossibleInRule(t, "_"); 
        }
      }
    }
  }
        
    /*
      testPairSlotName is used in 'expand' method of TomChecker.t.
      We test the existence of one slot, contained in pairSlotName.
      pairSlotName is one slot given in argument.
      slotList is the list of possible slots.
    */
  public void testPairSlotName(TomList listPairSlotName, TomList slotList) throws TomException {
    if(!Flags.doVerify) return;
    while( !listPairSlotName.isEmpty() ) {
      TomTerm pairSlotName = listPairSlotName.getHead();  
      boolean findSlotNameEquivalent = false;
      TomList slotListBis = empty();
      slotListBis = slotList;
        // we test if slotName in pairSlotName is in slotList
      while( !slotListBis.isEmpty() && !findSlotNameEquivalent )
      {
        TomTerm termSlotName = slotListBis.getHead();
        %match(TomTerm pairSlotName) {
          Pair[slotName=term] -> {
            if ( term == termSlotName ) { findSlotNameEquivalent = true; }
          }
        }
        slotListBis = slotListBis.getTail();
      }
        // if slotName is unknown we generate a message which propose all possible slots for this case
      if(!findSlotNameEquivalent) {
        messageSlotNameError(pairSlotName,slotList); 
      }
      listPairSlotName = listPairSlotName.getTail();
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
  public void testNumberAndRepeatedSlotName(TomList pairSlotList, TomList slotList) throws TomException {
    if(!Flags.doVerify) return;
      // we generate a formated list of possible slotNames

    ArrayList slotPossible = new ArrayList();
    while( !slotList.isEmpty() ) {
      TomTerm oneSlot = slotList.getHead();
      %match( TomTerm oneSlot) {
        SlotName[string=slotName] -> { slotPossible.add(" " + slotName +" "); }	
      }
      slotList = slotList.getTail();
    }
      /*
        We memorize the first slotName in order to have information about line
      */
    TomTerm pairSlotName = null;

    if(!pairSlotList.isEmpty()) {
       pairSlotName = pairSlotList.getHead();
    }
      /*
        we generate a formated list of given slotNames
      */
    ArrayList slotGiven = new ArrayList();
    while( !pairSlotList.isEmpty() ) {
      TomTerm onePair = pairSlotList.getHead();
      %match( TomTerm onePair) {
        Pair[slotName=SlotName[string=slotName]] -> { slotGiven.add(" " + slotName + " "); }
      }
      pairSlotList = pairSlotList.getTail();
    }
      /*
        the list of given slotNames must be less or egal in dimension than the list of possible slotNames
      */
    if(slotGiven.size() > slotPossible.size()) {
      messageSlotNumberError(pairSlotName, slotPossible.size(), slotGiven.size());
    } else {
      int index = slotGiven.size(); 
        /*
          findFunctions contains the list of already verified slotNames
        */
      ArrayList findFunctions = new ArrayList();
        /*
          we test all given slotNames
        */
      while( index != 0 ) {
        String nameSlot = (String) slotGiven.get(index - 1);
          /*
            we test if nameSlot has been already readed in given slotNames
          */
        if( findFunctions.contains(nameSlot) ) {
          messageSlotRepeatedError(pairSlotName,nameSlot);
        } else {
            /*
              nameSlot is use for the first time, so we add it in findFunctions
            */
          findFunctions.add(nameSlot);
        }
        index--;
      }
    }
  }
    
    /*
      testApplStructure is used only in TomVerifier.t : it is used in 'testMatchTypeCompatibility' method
      and in 'testRuleVariable' method. Indeed Appl structure is used only in %rule and %match.
      1. We test if the arguments number of one method is right wrt. its definition.
      2. We test if the types of arguments in one method is right wrt. its definition.
      3. We test if a VariableStar is authorized when it is used.
      termAppl is the Appl structure in question.
    */
  private void testApplStructure(TomTerm termAppl) throws TomException {
    statistics().numberApplStructuresTested++;
      /*
        we generate an arrayOrList information about a method in order to known if it is
        an %oplist or an %oparray. Indeed, in this case, the number of arguments is unspecified
      */
    boolean arrayOrList = false;
    %match(TomTerm termAppl) {
      Appl[option=Option(list),astName=Name[string=name],args=argsList] -> {
        TomSymbol symbol = symbolTable().getSymbol(name);
          // we verify if symbol method 'name' exists in SymbolTable
        if(symbol==null && !argsList.isEmpty()) {
          messageSymbolError(name, list);
        }
          // we extract the needed types for the arguments of this Appl.
        TomList infoTypeIn = typeIn(symbol);
          // we test if it is an %oparray or an %oplist.
        arrayOrList = ( isListOperator(symbol) ||  isArrayOperator(symbol) );
          // if arguments are given
        if(!argsList.isEmpty()) {
            /*
              We test the egality between number of needed arguments and of given arguments.
              Of course, we use informations about %oparray or %oplist : in this case there
              are no problem
            */
          
          int arity = length(argsList);
          if(arity!=length(infoTypeIn) && !arrayOrList) {
            String line = findOriginTrackingLine(name,list);
            messageNumberArgumentsError(length(infoTypeIn), arity, name, line);
          }

            /*
              We examine the complete list of given arguments in order to extract their result
              type -listTemp- and their name constructor (for error message) -listTempName-.
            */
          TomType[] tabTypeOut = new TomType[arity];
          String[] tabNameOut = new String[arity];
          String noNameTypeOut = "NoName";
          for(int i=0; !argsList.isEmpty() ; i++,argsList = argsList.getTail()) {
            TomTerm term = argsList.getHead();
            testApplStructure(term);
            %match(TomTerm term) {
              Appl[astName=Name[string=name1]] -> {
                tabTypeOut[i] = typeOut(symbolTable().getSymbol(name1));
                tabNameOut[i] = name1;
              }
                /*
                  if it is an '_' : type and name have no importance
                */
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
  } 	

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
      testMakeDefineAppl is used only in 'testMakeDefine' method of Tomverifier.t
      We test here the occurrences of 'make' constructor for methods used in %rule.
      ruleTerm is one part of %rule : it has ever an Appl structure.
      yetStudied contains the name of methods yet studied by testMakeDefine.
    */
  private void testMakeDefineAppl(TomTerm ruleTerm, ArrayList yetStudied) throws TomException {
    %match( TomTerm ruleTerm ) {
      Appl[astName=Name[string=name],args=argsList] -> {
          // if it is the first time that we have this method, we make tests
        if(!yetStudied.contains(name)) {
          yetStudied.add(name);
            // we test the occurrences of 'make' for symbol 'name'
          TomSymbol info = symbolTable().getSymbol(name);
          if(info!=null) {
            %match(TomSymbol info) {
              Symbol[option=Option(optionList)] -> {
                boolean makeFind = false;
                String line = findOriginTrackingLine(name,optionList);
                while(!optionList.isEmptyOptionList() && !makeFind) {
                  Option optionTerm = optionList.getHead();
                  %match(Option optionTerm) {
                    DeclarationToOption(MakeDecl[astName=Name[string=name1]]) -> {
                      if ( name1 == name ) { makeFind = true; }
                    }
                      /*
                        for %oplist and %oparray, no problem : construtor ot make type is obligatory.
                        So we are sure to find it
                      */
                    DeclarationToOption(MakeEmptyArray[astName=Name[string=name1]]) -> {
                      if ( name1 == name ) { makeFind = true; }
                    }
                    DeclarationToOption(MakeEmptyList[astName=Name(name1)]) -> { 
                      if ( name1 == name ) { makeFind = true; }
                    }
                  }
                  optionList = optionList.getTail();
                }
                if(!makeFind) {
                  messageMakeNotFoundForRule(name, line);
                }
              }
            }
          }
        }
          /* we make the tests on arguments too */
        while(!argsList.isEmpty()) {
          TomTerm oneArgs = argsList.getHead();
          testMakeDefineAppl(oneArgs,yetStudied);
          argsList = argsList.getTail();
        }
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

    /*****************************************************
     ***  In this part, we generate messages and exits  ***
     *****************************************************/

    /********************************
     * Error messages which generate *
     * immediatly an exit            *
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
  
  public void messageTypesOperatorError(String type, int slotPosition, String name, String line) throws TomException {
    String s = "Slot position "+slotPosition + " of operator '"+ name + "' has an unknown type: '" + type + "'";
    messageError(line,s);
  }

  public void messageTypeOperatorError(String type, String name, String line) throws TomException {
    String s = "Operator '" + name + "' has an unknown return type: '" + type + "'";
    messageError(line,s);
  }
    /* messageMatchTypeVariableError is called by 'contest,GlVar' case
       in 'pass1' method of TomChecker.t.
       optionMatchTypeVariable contains informations about line error.
    */
  public void messageMatchTypeVariableError(String name, String type) throws TomException {
    OptionList optionList = optionMatchTypeVariable.getOptionList();
    String line = findOriginTrackingLine("Match", optionList);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct";
    messageError(line,s);
  }	

  private void messageImpossibleInRule(OptionList optionList, String name) throws TomException {
    String line = findOriginTrackingLine(optionList);
    String s = "Single '_' are not allowed in %rule" + "'" + name + "' is not correct";
    messageError(line,s);
  }

  private void messageErrorVariableStar(String nameVariableStar, String nameMethod ,String line) throws TomException {
    String s = "List variable '" + nameVariableStar + "' cannot be used in '" + nameMethod + "'";
    messageError(line,s);
  }
 
  private void messageErrorVariableStarBis(String nameVariableStar, String line) throws TomException {
    String s = "Single list variable (" + nameVariableStar + "*) is not allowed";
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

  private void messageNumberArgumentMatchError( int nbGlVar, int nbAppl, String line) throws TomException {
    String s = "Bad number of arguments in match: " + nbGlVar + 
      " arguments are required but " + nbAppl + " are given"; 
    messageError(line,s);
  }

    /************************************
     * Error messages which generate     *
     * no exit, but the program is wrong *
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

  private void messageImpossibleUnderscore(OptionList optionList) {
    String line = findOriginTrackingLine(optionList);
    System.out.println("\n *** Underscores are not allowed in the right side of '->' in %rule");
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

  private void messageSlotNameError(TomTerm pairSlotName, TomList listOfPossibleSlot) {
    ArrayList slotPossible = new ArrayList();
    while( !listOfPossibleSlot.isEmpty() ) {
      TomTerm oneSlot = listOfPossibleSlot.getHead();
      %match( TomTerm oneSlot ) {
        SlotName[string=slotName] -> { slotPossible.add(" " + slotName + " "); }
      }
      listOfPossibleSlot = listOfPossibleSlot.getTail();
    }
    %match( TomTerm pairSlotName ) {
      Pair[slotName=SlotName(name),appl=Appl[option=Option(list)]] -> {
        String line = findOriginTrackingLine(list);
        System.out.println("\n"+" *** Slot Name '" + name + "' is strange"+" -  Line : "+line);
      }
    }
    
    System.out.println(" *** Possible Slot Names are : "+slotPossible);

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
     * Warning message error : maybe an error  *
     * Dangerous situation                     *
     ******************************************/

    /*
      Here we test first if user wants warning message error.
      If he do not say "noWarning", we print warning message errors.
    */

  private void messageTypeArgumentMatchError( String typeAsked, String typeGiven, String line, int nbOfError ) {
    if(Flags.noWarning) return;
    if ( nbOfError == 1 )
    { System.out.println("\n"+" *** Warning *** Possible bad type in match"); }
    System.out.println(" *** Type '"+typeAsked+"' is required but Type '"+typeGiven+"' is given"+" - Line : "+line);
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

  private void messageRuleTypeEgality(String name, String type, String typeExpected, OptionList optionList) {
    if(Flags.noWarning) return;
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n"+" *** Warning *** Error in %rule after '->' - Line : " + line);
    System.out.println(" *** Type '" + typeExpected + "' is expected, but '" + name + "' of type '" + type + "' is given");
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
    System.out.println("findOriginTrackingLine:  not found");
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
      extractVariable is used in 'testRuleVariable' method of Tomverifier.t.
      We extract informations about variables (i.e. for object as symbolTable().getSymbol(_) == null)
      which appear in term. 
      term has an Appl structure.
      nameVariable and lineVariable stock informations about these variables.
      For a same index, we have informations about same variable.
    */
  private void extractVariable(TomTerm term, ArrayList nameVariable, ArrayList lineVariable) {
    %match(TomTerm term) {
      Appl[option=Option(optionList),astName=Name[string=name1],args=l] -> {
          /*
            if l != [] then name1 could not be a variable.
          */
        if ( symbolTable().getSymbol(name1)==null && l.isEmpty() ) { 
          nameVariable.add(name1);
          lineVariable.add(findOriginTrackingLine(name1,optionList));
        }
          /*
            we extract variables of arguments of this Appl struture.
          */
        extractVariableList(l, nameVariable, lineVariable);
        return;
      }
      _ -> { return; }
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
      this global variable permits to memorize informations 
      in order to known line error for messageMatchTypeVariableError
    */
  protected Option optionMatchTypeVariable;

    /*
      affectOptionMatchTypeVariable is used in 'context,Match' case in 'pass1' method of TomChecker.t
      It permits to memorize informations in order to known line error for messageMatchTypeVariableError
    */
  public void affectOptionMatchTypeVariable(Option option) {
    optionMatchTypeVariable = option;
  }


    /*
      extractType is used in 'testMatchTypeCompatibility' method of TomVerifier.t
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
      /*
        if no type for 'name' is found, we return EmptyType
      */
    TomType type = `EmptyType();
    %match( TomTerm inTerm ) {
      Appl[astName=Name[string=name1],args=list] -> {
        TomList typeList = empty();
        int numArg = 0;
        boolean find = false;
          /*
            we look if 'name' is an argument and we memorize the position of this argument
          */
        for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
          numArg++;
          TomTerm t = l.getHead();
          %match(TomTerm t) {
            Appl[astName=Name[string=name2]] -> {
              find = (name2.equals(name));
            }
          }
        }
          /*
            if 'name' is an argument, we search the needed argument type
            thanks to the memorized position.
          */
        if(find) {
          typeList = typeIn(symbolTable().getSymbol(name1));
          for(int i = 1; i < numArg; i++ ) {
            typeList = typeList.getTail();
          }
          type = typeList.getHead().getAstType();
        } else {
            /*
              if 'name' is not an argument, we look if 'name' is an argument
              of methods given in argument.
            */
          for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
            TomTerm t = l.getHead();
            type = findTypeOf(name, t);
            find = true;
            %match(TomType type) {
                /*
                  no type for 'name' found
                */
              EmptyType() -> { find = false; }
            }
          }
        }
      }
    }
    return type;
  } 
	
    /*
     * repeatedVariable is used in 'testRuleTypeEgality' and
     * 'testMatchTypeCompatibility' methods of TomVerifier.t 
     * This method is used to test the type compatibility of same named
     * variables used in a same structure.
     * term is the Appl structure to test.
     * nameVar and typeVar are names and types of variables already found.
     * For a same index, we have informations about the same variable.
     * typeGlVar is the type of the variable, if term is a variable.
     * If typeGlVar == " ",
     * we have no information about the possible variable representing by term.
     */						
  private void repeatedVariable( TomTerm term, ArrayList nameVar, ArrayList typeVar, String typeGlVar ) {
    if(false) {
      %match(TomTerm term) {
        Appl[option=Option(optionList),astName=Name[string=name1],args=l] -> {
          TomSymbol infoTable = symbolTable().getSymbol(name1);
            /* if Appl representing 'name1' is a variable */
          if(infoTable==null && l.isEmpty()) {
              /* if it is the first time that we look this variable */
            if( !nameVar.contains(name1) ) {
              nameVar.add(name1);
                /*
                 * if we have no information about type.
                 * If the inherited information about type contained
                 * in typeGlVar gives no help
                 */
              if( typeGlVar == " " ) {
                  /* EmptyType means that we have no information about type. */
                typeVar.add("EmptyType");
              } else {
                typeVar.add(typeGlVar);
              }
                /*
                 * if we have already find this variable,
                 * we verifie that types coincide : type in typeVar and
                 * type in typeGlVar must be equal.
                 * If type in typeVar gives no information, we replace this
                 * type by typeGlVar. Of course, we make this if typeGlVar
                 * gives information (if typeGlVar != " ").
                 */
            } else {
              if( typeGlVar != " " ) {
                int i = nameVar.indexOf(name1);
                String typeMem = (String) typeVar.get(i);
                if ( typeMem == "EmptyType" ) {
                  nameVar.remove(i);
                  typeVar.remove(i);
                  nameVar.add(name1);
                  typeVar.add(typeGlVar);
                } else {
                  if ( typeMem != typeGlVar ) {
                    String line = findOriginTrackingLine(name1,optionList);
                    messageRepeatedVariableError(name1, typeGlVar, typeMem, line); 
                  }
                }
              }   
            }
          } else {
              /*
               * if Appl representing 'name1' is not a variable,
               * it is a method.
               * So we search variables in its argument list 'l'.
               * we stock the needed types of arguments, there are
               * given by the definition of 'name1' method.
               */
            TomList typeInAppl = typeIn(infoTable);
              /* we look all arguments */
            if(length(typeInAppl) != length(l)) {
              System.out.println("typeInAppl = " + typeInAppl);
              System.out.println("l          = " + l);
              System.out.println(length(typeInAppl) + " <--> " + length(l));
              System.exit(0);
            }
            while( !l.isEmpty() ) {
                /* we get one element and its needed type */
              TomTerm termL = l.getHead();
              TomType termTypeIn = typeInAppl.getHead().getAstType();
                /* we look if this element is a variable. */
              %match(TomTerm termL) {
                Appl[option=Option(optionList1),astName=Name[string=name2],args=l1] -> {
                  TomSymbol infoTable1 = symbolTable().getSymbol(name2);
                  if(infoTable1==null && l1.isEmpty()) {
                      /*
                       * if it is an unknown variable,
                       * we memorize it with the needed type
                       */
                    if ( !nameVar.contains(name2) ) {
                      nameVar.add(name2);
                      %match(TomType termTypeIn) {
                        TomTypeAlone[string=type] -> { typeVar.add(type); }
                        Type(TomType(type),_) -> { typeVar.add(type); }
                        _ -> { typeVar.add("EmptyType"); }
                      }
                    } else {
                        /*
                         * if we have already find this variable,
                         * we verifie that types coincide : 
                         * type in typeVar and type in termTypeIn must be equal. 
                         * If type in typeVar give no information, we replace this type
                         * by type in termTypeIn.
                         */
                      int i = nameVar.indexOf(name2);
                      String typeMem = (String)typeVar.get(i);
                      if ( typeMem == "EmptyType" ) {
                        nameVar.remove(i);
                        typeVar.remove(i);
                        nameVar.add(name2);
                        %match(TomType termTypeIn) {
                          TomTypeAlone[string=type] -> { typeVar.add(type); } 
                          Type(TomType(type),_) -> { typeVar.add(type); }
                          _ -> { typeVar.add("EmptyType"); }
                        }
                      } else  { 
                        %match(TomType termTypeIn) {
                          TomTypeAlone[string=type] -> { 
                            if ( !typeMem.equals(type) ) {
                              String line = findOriginTrackingLine(name2,optionList1);
                              messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                          }
                          Type(TomType(type),_) -> {
                            if ( !typeMem.equals(type) ) {
                              String line = findOriginTrackingLine(name2,optionList1);
                              messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                          }
                        }
                      }
                    }
                  }
                }
                VariableStar[option=Option(optionList1), astName=Name[string=name2], astType=t] -> {
                    /*
                     * VariableStars have elready created : so we must consider these
                     */
                  TomList infoTypeIn = typeIn(infoTable);
                  TomType theType = infoTypeIn.getHead().getAstType();
                  if ( !nameVar.contains(name2) ) {
                    nameVar.add(name2);
                    %match(TomType theType) {
                      Type(TomType(type),_) -> { typeVar.add(type); }
                      _ -> { typeVar.add("EmptyType"); }
                    }
                  } else {                
                      /*
                        if we have already find this variable, we verifie that types coincide : 
                        type in typeVar and type in termTypeIn must be equal. 
                        If type in typeVar give no information, we replace this type
                        by type in termTypeIn.
                      */
                    int i = nameVar.indexOf(name2);
                    String typeMem = (String)typeVar.get(i);
                    if ( (typeMem == "EmptyType") || (typeMem == "unknown type") ) {
                      nameVar.remove(i);
                      typeVar.remove(i);
                      nameVar.add(name2);
                      %match(TomType theType) {
                        Type(TomType(type),_) -> { typeVar.add(type); }
                        _ -> { typeVar.add("EmptyType"); }
                      }
                    } else  { 
                      %match(TomType theType) {
                        Type(TomType(type),_) -> {
                          if ( !typeMem.equals(type) ) {
                            String line = findOriginTrackingLine(name2,optionList1);
                            messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                        }
                      }
                    }
                  }
                }
              }
                /*
                  we test repeated variables in arguments List. But it is necessary to
                  known types of arguments, so we apply repeatedVariable on termL in order to extract
                  informations about needed arguments.
                */
              repeatedVariable(termL, nameVar, typeVar, " ");
              l = l.getTail();
              typeInAppl = typeInAppl.getTail();
            }
          }
        }
      }
    }
  }
}


