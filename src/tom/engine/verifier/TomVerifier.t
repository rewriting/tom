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
  public TomVerifier(jtom.TomEnvironment environment) { 
    super(environment);
  }
    
    // ------------------------------------------------------------
  %include { Tom.signature }
    // ------------------------------------------------------------

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
      testArg is used in 'KeywordMake' function of TomParser.jj :
      - nbArg is the number of needed arguments,
      - nbArg2 is the number of given arguments,
      - bool permits to known if we are readed all arguments.
      When we have readed one argument we make a test.
    */
  public void testArg(int nbArg, int nbArg2, boolean bool, String name, String line ) throws TomException {
    if(!Flags.doVerify) return;
	
    if((!bool && (nbArg2 >  nbArg))
       ||( bool && (nbArg2 != nbArg)) ) {
      try {
        messageNumberArgumentsError(nbArg,nbArg2,name,line);
      } catch (CheckErrorException e) {
        System.out.println("TomVerifier catch:" + e);
        System.exit(1);
      }
    }
  }

    /*
      testRuleSymbolInSymbolTable is used in 'RuleConstruct' function of TomParser.jj.
      We test if name of 'term' exits in symbolTable.
    */
  public void testRuleSymbolInSymbolTable(TomTerm term) throws TomException {
    if(!Flags.doVerify) return;
    %match(TomTerm term) {
      Appl[option=Option(list),astName=Name[string=name],args=argsList] -> {
        TomSymbol symbol = symbolTable().getSymbol(name);
        if(symbol==null) {
          if(!argsList.isEmpty()) {
            messageSymbolError(name,list);
          } else {
            messageRuleSymbolError(name,list);
          }
        }
      }
    }
  }

    /*
      These 3 lists are used in testTypeOperator. There are completed thanks to 
      'addTypeOperator' method in TomParser.jj through methods : Operator, OperatorList, OperatorArray.
      For a same index i, we have, in the 3 lists, informations about the same object :
      type (typeOperatorList) / name (nameOperatorList) / line (lineOperatorList)
    */
  private ArrayList typeOperatorList = new ArrayList(); 
  private ArrayList nameOperatorList = new ArrayList();
  private ArrayList lineOperatorList = new ArrayList();

    /*
      testTypeOperator is used in 'startParsing' method of TomParser.jj.
      We test if the types (in typeOperatorList) of one method (of name in 
      nameOperatorList, at the line in lineOperatorList) exists.
      We test the types of arguments and of returns.
      We are obliged to stock info about operators in order to test them after the knownledge 
      (so the definition) of all types. That's why we test only in startParsing 
      (all operators and types have been readed). Moreover, as we have no assurance 
      that all operators are used in the future; since information about type is stored 
      in Symbol Table in 'Operator', 'OperatorList' and 'OperatorArray' methods, 
      it seems advantageous to stock informations after reading and to make a global
      test directly after the read of all operators.
    */
  public void testTypeOperator() throws TomException {
    if(!Flags.doVerify) return;
    for(int i = 0; i < typeOperatorList.size(); i++) {
      String line = (String) lineOperatorList.get(i);
      String name = (String) nameOperatorList.get(i);
      String type = (String) typeOperatorList.get(i);
      if(symbolTable().getType(type) == null) {
        messageTypeOperatorError(line,name,type);
      }
    }
  }

    /*
      testRuleVariable is used in TomChecker.t, in 'context,RewriteRule' case of 'pass1' method.
      We test the existence of variables from the right part of '->' in the left part of '->'.
      We have : lhs -> rhs
    */
  public void testRuleVariable(TomTerm lhs, TomTerm rhs) throws TomException {
    if(!Flags.doVerify) return;
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
  public ArrayList testRuleTypeAndConstructorEgality(TomTerm lhs, ArrayList nameType, int ruleNumber) throws TomException {
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
      testNoUnderscore is used in 'RuleConstruct' method of TomParser.jj.
      We test if the right-side hand of a rule in %rule contains or not '_' 
      We have lhs -> rhs.
      bool says if rhs is the global rhs or a part of it. Indeed, alone '_'
      in a global rhs is already detected.
    */
  public void testNoUnderscore(TomTerm rhs, boolean bool) throws TomException {
    if(!Flags.doVerify) return;
    %match(TomTerm rhs) {
      Appl[args=argsList] -> {
        while(!argsList.isEmpty()) {
          TomTerm oneArgs = argsList.getHead();
          testNoUnderscore(oneArgs,false);
          argsList = argsList.getTail();
        }
      }
      Placeholder[option=Option(t)] -> { 
        if(!bool) {
          messageImpossibleUnderscore(t);
        } 
      }
    }
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
  public void testRuleTypeEgality(TomTerm rhs, ArrayList nameType, TomTerm lhs) throws TomException {
    if(!Flags.doVerify) return;
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
      testMatchTypeCompatibility is used in 'context,Match' case in 'pass1' method of TomChecker.t.
      1. We test the egality between the number and the type of elements in %match subject 
      and the number and the type of arguments in %match pattern-actions.
      2. We test the type egality for a same name variable in a pattern-action.
      3. We verifie that alone variableStar is impossible
      4. Verification of Appl structures
      subjectList is the arguments list of a %match.
      patternList is the pattern-actions list of this %match.
    */
  public void testMatchTypeCompatibility(TomTerm subjectList, TomTerm patternList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we extract [_] of constructor([_])
      */
    TomList glVarList = extractList(subjectList);
    TomList patternActionList = extractList(patternList);
    int nbGlVar = 0;
    ArrayList typeGlVar = new ArrayList();
      /*
        We extract types (in typeGlVar list)
        and number (in nbGlVar int) of elements in %match subject
      */
    while( !glVarList.isEmpty() ) {
      TomTerm term = glVarList.getHead();
      %match( TomTerm term ) {
        GLVar[strName=name, astType=TomTypeAlone(type)] -> {
          nbGlVar = nbGlVar + 1; 
          typeGlVar.add(type);
        }
      }
      glVarList = glVarList.getTail();
    }
      /*
        we work on pattern-actions list
      */
    while( !patternActionList.isEmpty() ) {
      statistics().numberMatchesTested++;
        /*
          we initialize list of names and types for tested variables
        */
      ArrayList nameVar = new ArrayList();
      ArrayList typeVar = new ArrayList();
      TomTerm term = patternActionList.getHead();
      TomTerm termBis = null;
        /*
          we extract the TermList of this pattern
        */
      %match( TomTerm term ) {
        PatternAction[termList=t] -> { termBis= t; }
      }
        /*
          we extract [_] of constructor([_])
        */
      TomList applList = extractList(termBis);
        /*
          we extract the line number (about current pattern-action).
          Here variables are Appl methods still.
        */
      TomTerm termApplLine = applList.getHead();
      String line = " - ";
      %match(TomTerm termApplLine) {
        Appl[option=Option(list)] -> {
          line = findOriginTrackingLine(list);
        }
      }
      int n2 = typeGlVar.size();
      int nbAppl = 0;
      int nbPassInWhile = 0;
      ArrayList typeAppl = new ArrayList();
        /*
          we work on all Appls (elements) of this pattern
        */
      while( !applList.isEmpty() ) {
        TomTerm termAppl = applList.getHead();
          /*
            we test if the number of Appl terms (number of elements)
            in one pattern-action is <= to the number of elements in %match subject
          */
        if ( n2 > nbPassInWhile ) {
          repeatedVariable(termAppl, nameVar, typeVar, (String) typeGlVar.get(nbPassInWhile));
        }
        else
            /*
              test of type egality for repeated variables
            */
        { repeatedVariable(termAppl, nameVar, typeVar, " "); }
          /*
            we test the validity of the current Appl structure
          */
        testApplStructure(termAppl);
          /*
            we extract type of the current element of pattern-action
          */
        %match( TomTerm termAppl ) {
          Appl[astName=Name[string=name]] -> {
            nbAppl = nbAppl + 1;
            typeAppl.add(extractType(symbolTable().getSymbol(name)));
          }
          Placeholder[] -> { nbAppl = nbAppl + 1; typeAppl.add((TomTerm) null); }
            /*
              alone stared variable is impossible
            */
          VariableStar[option=Option(list), astName=Name[string=name1]] -> { 
            line = findOriginTrackingLine(name1,list);
            messageErrorVariableStarBis(name1, line); 
          }
        }
        applList = applList.getTail();
        nbPassInWhile++;
      }
        /*
          nb elements in %match subject = nb elements in the pattern-action ?
        */
      if(nbGlVar != nbAppl) {
        messageNumberArgumentMatchError(nbGlVar, nbAppl, line); 
      }
      int n1 = typeAppl.size();
      int n;
        /*
          we select the minimum between typeAppl.size() and typeGlVar.size() in order to
          make the test on types even if nb arguments != nb elements in the pattern
        */
      if ( n1 > n2 ) { n = n2; }
      else { n = n1; }
      int nbOfError = 0;
        /*
          we test the types egality between arguments and pattern-action
        */
      for( int i = 0; i < n; i++ ) {
          /*
            if typeAppl.get(i) == null then it is a variable : so no problem
          */
        if ( ( typeAppl.get(i) != typeGlVar.get(i) ) && ( typeAppl.get(i) != null ) )
        { 	
          nbOfError = nbOfError + 1;
          messageTypeArgumentMatchError( (String) typeGlVar.get(i), (String) typeAppl.get(i), line, nbOfError ); 
        } 
      }
      patternActionList = patternActionList.getTail();
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
        /*
          we test if slotName in pairSlotName is in slotList
        */
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
        /*
          if slotName is unknown we generate a message which propose all possible slots for this case
        */
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
      /*
        we generate a formated list of possible slotNames
      */

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
  public void testApplStructure(TomTerm termAppl) throws TomException {
    if(!Flags.doVerify) return;

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
    
  private void checkField(String field, ArrayList findFunctions, OptionList optionList) throws TomException {
    if(findFunctions.contains(field)) {
      findFunctions.remove(findFunctions.indexOf(field)); 
    } else {
      String line = findOriginTrackingLine(optionList);
      messageMacroFunctionRepeated(field,line);
    }
  }

  private void checkFieldLinearArgs(String field, ArrayList findFunctions, OptionList optionList, String name1, String name2) throws TomException {
    checkField(field,findFunctions,optionList);
    if(name1.equals(name2)) { 
      String line = findOriginTrackingLine(optionList);
      messageTwoSameNameVariableError("cmp_fun_sym",name1,line);
    }
  }
  
    /*
     * testTypeTerm is used in 'TypeTerm' method in TomParser.jj. 
     * We test the used macro functions in %typeterm : repetetion, missing.
     * We test also the paremeters of macro functions : there must
     * have different names.
     * It is the parser which work on bad macro functions,
     * or on bad number of parameters,
     * or on 'implement' macro-function.
     * list is the list of macro functions given in %typeterm.
     */
  public void testTypeTerm(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
       * we define possible macro functions in %typeterm
       */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("get_fun_sym");
    findFunctions.add("cmp_fun_sym");
    findFunctions.add("get_subterm");
    findFunctions.add("equals");

    statistics().numberTypeDefinitonsTested++;

      /*
       * we make tests : if one given macro function is in findFunctions,
       * we remove it of the findFunctions list.
       * So if same macro function is used, as it is not yet in
       * findFunctions list, we generate a message with repeated macro
       * function.
       * When all given macro functions are processed, if findFunctions
       * contains only " equals ", all is all right; else we generate
       * a message with missing macro functions, so the remaining elements,
       * excepted " equals ", of findFunctions list.
       */
    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(GetFunctionSymbolDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("get_fun_sym",findFunctions,listOption);
        }
        DeclarationToOption(CompareFunctionSymbolDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(GetSubtermDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("get_subterm",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(TermsEqualDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        }
      }
      optionList = optionList.getTail();
    }

    if(findFunctions.contains("equals")) {
      findFunctions.remove(findFunctions.indexOf("equals"));
    }
    
    if(!findFunctions.isEmpty()) {
      messageMissingMacroFunctions("%typeterm", findFunctions);
    }
  }


    /*
     * testTypeList is used in 'TypeList' method in TomParser.jj.
     * We test the used macro functions in %typelist : repetetion, missing.
     * We test also the paremeters of macro functions : there must have
     * different names.
     * It is the parser which work on bad macro functions, or on bad
     * number of parameters, or on 'implement' macro-function.
     * list is the list of macro functions given in %typelist.
     */
  public void testTypeList(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we define possible macro functions in %typelist
      */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("get_fun_sym");
    findFunctions.add("cmp_fun_sym");
    findFunctions.add("get_head");
    findFunctions.add("get_tail");
    findFunctions.add("is_empty");
    findFunctions.add("equals");

    statistics().numberTypeDefinitonsTested++;
    
      /*
       *  we make tests : if one given macro function is in findFunctions,
       * we remove it of the findFunctions list. So if same macro function
       * is used, as it is not yet in findFunctions list, we generate a
       * message with repeated macro function.
       * When all given macro functions are processed, if findFunctions
       * is not empty (except "Equals"), we generate a message with
       * missing macro functions,
       * so the remaining elements of findFunctions list.
       */
    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(GetFunctionSymbolDecl(Variable[option=Option(listOption)],_)) -> { 
          checkField("get_fun_sym",findFunctions,listOption);
        }
        DeclarationToOption(CompareFunctionSymbolDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(TermsEqualDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(GetHeadDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("get_head",findFunctions,listOption);
        }
        DeclarationToOption(GetTailDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("get_tail",findFunctions,listOption);
        }
        DeclarationToOption(IsEmptyDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("is_empty",findFunctions,listOption);
        }
      }
      optionList = optionList.getTail();
    }

    if(findFunctions.contains("equals")) {
      findFunctions.remove(findFunctions.indexOf("equals"));
    }

    if(!findFunctions.isEmpty()) {
      messageMissingMacroFunctions("%typelist", findFunctions);
    }
  }

    /*
      testTypeArray is used in 'TypeArray' method in TomParser.jj.
      We test the used macro functions in %typearray : repetetion, missing.
      We test also the paremeters of macro functions : there must have different names.
      It is the parser which work on bad macro functions, or on bad number of parameters,
      or on 'implement' macro-function.
      list is the list of macro functions given in %typearray.
    */
  public void testTypeArray(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we define possible macro functions in %typearray
      */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("get_fun_sym");
    findFunctions.add("cmp_fun_sym");
    findFunctions.add("get_element");
    findFunctions.add("get_size");
    findFunctions.add("equals");

    statistics().numberTypeDefinitonsTested++;
      /*
        we make tests : if one given macro function is in findFunctions, we remove it
        of the findFunctions list. So if same macro function is used, as it is not yet
        in findFunctions list, we generate a message with repeated macro function.
        When all given macro functions are processed, if findFunctions is not empty,
        we generate a message with missing macro functions, so the
        remaining elements of findFunctions list.
      */

    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(GetFunctionSymbolDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("get_fun_sym",findFunctions,listOption);
        }
        DeclarationToOption(CompareFunctionSymbolDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(TermsEqualDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> {
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(GetElementDecl(Variable[option=Option(listOption), astName=Name[string=name1]],Variable[astName=Name[string=name2]],_)) -> { 
          checkFieldLinearArgs("get_element",findFunctions,listOption,name1,name2);
        }
        DeclarationToOption(GetSizeDecl(Variable[option=Option(listOption)],_)) -> {
          checkField("get_size",findFunctions,listOption);
        }
      }
      optionList = optionList.getTail();
    }

    if(findFunctions.contains("equals")) {
      findFunctions.remove(findFunctions.indexOf("equals"));
    }
    
    if(!findFunctions.isEmpty()) {
      messageMissingMacroFunctions("%typearray", findFunctions);
    }
  }

    /*
      testOperatorArray is used in 'OperatorArray' method in TomParser.jj.
      We test the required occurrences of constructors : missing and repetition.
      We test also the paremeters of macro functions : there must have different names.
      It is the parser which work on bad macro functions, or on bad number of parameters,
      or on 'fsym' macro-function.
      list is the list of macro functions given in %oparray. 
    */
  public void testOperatorArray(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we define possible macro functions in %oparray
      */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("make_empty");
    findFunctions.add("make_add");

    statistics().numberOperatorDefinitionsTested++;
      /*
        we make tests : if one given macro function is in findFunctions, we remove it
        of the findFunctions list. So if same macro function is used, as it is not yet
        in findFunctions list, we generate a message with repeated macro function.
        When all given macro functions are processed, if findFunctions is not empty,
        we generate a message with missing macro functions, so the
        remaining elements of findFunctions list.
      */
    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(MakeEmptyArray[varSize=Variable[option=Option(listOption)]]) -> { 
          checkField("make_empty",findFunctions,listOption);
        }
        DeclarationToOption(MakeAddArray[varList=Variable[option=Option(listOption), astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]], varIdx=Variable[astName=Name[string=name3]]]) -> {
          checkFieldLinearArgs("make_add",findFunctions,listOption,name1,name2);
          if( name1 == name3 || name2 == name3 ) { 
            String line = findOriginTrackingLine(listOption);
            messageTwoSameNameVariableError("make_add",name3,line);
          }
        }
      }
      optionList = optionList.getTail();
    }
    if(!findFunctions.isEmpty()) {
      messageMissingMacroFunctions("%oparray", findFunctions);
    }
  }

    /*
      testOperatorList is used in 'OperatorList' method in TomParser.jj.
      We test the required occurrences of constructors : missing and repetition.
      We test also the paremeters of macro functions : there must have different names.
      It is the parser which work on bad macro functions, or on bad number of parameters,
      or on 'fsym' macro-function.
      list is the list of macro functions given in %oplist.
    */
  public void testOperatorList(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we define possible macro functions in %oplist
      */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("make_empty");
    findFunctions.add("make_add");

    statistics().numberOperatorDefinitionsTested++;
      /*
        we make tests : if one given macro function is in findFunctions, we remove it
        of the findFunctions list. So if same macro function is used, as it is not yet
        in findFunctions list, we generate a message with repeated macro function.
        When all given macro functions are processed, if findFunctions is not empty,
        we generate a message with missing macro functions, so the
        remaining elements of findFunctions list.
      */
    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(MakeEmptyList[astName=Name(name)]) -> {
          if(findFunctions.contains("make_empty")) {
            findFunctions.remove(findFunctions.indexOf("make_empty")); 
          } else {
              /* process as it because we have any informatiom about line */
            messageMacroFunctionRepeated("make_empty' in '" + name + "' of construct '%oplist", " - ");
          }
        }
        DeclarationToOption(MakeAddList[varList=Variable[option=Option(listOption), astName=Name[string=name1]], varElt=Variable[astName=Name[string=name2]]]) -> {
          checkFieldLinearArgs("make_add",findFunctions,listOption,name1,name2);
        }
      }
      optionList = optionList.getTail();
    }
    if( !findFunctions.isEmpty() ) {
      messageMissingMacroFunctions("%oplist", findFunctions);
    }
  }

    /*
      testOperator is used in 'Operator' method in TomParser.jj.
      We test the required occurrences of constructors : repetition.
      We test also the paremeters of macro functions : there must have different names.
      It is the parser which work on bad macro functions, or on 'fsym' macro-function.
      list is the list of macro functions given in %op.
    */
  public void testOperator(OptionList optionList) throws TomException {
    if(!Flags.doVerify) return;
      /*
        we define possible macro functions in %op
      */
    ArrayList findFunctions = new ArrayList();
    findFunctions.add("make");

    statistics().numberOperatorDefinitionsTested++;

      /*
        We test only the repetition of macro function "make" 
        and the  necessity to use different names for each variable.
        The need "make" is due to the use of %rule using the method concerned by this make :
        we test it in 'testMakeDefine' method.
      */
    while(!optionList.isEmptyOptionList()) {
      Option term = optionList.getHead();
      %match(Option term ) {
        DeclarationToOption(MakeDecl[astName=Name[string=nameSymbol],args=listM]) -> {
          if(findFunctions.contains("make")) {
            findFunctions.remove(findFunctions.indexOf("make")); 
          } else {
              /*
                we process as it because we have any informatiom about line
              */
            messageMacroFunctionRepeated("make' in '" + nameSymbol + "' of construct '%op", " - ");
          }
            /*
              we test the necessity to use different names for each variable-paremeter.
            */
          ArrayList listVar = new ArrayList();
          while(!listM.isEmpty()) {
            TomTerm termVar = listM.getHead();
            %match(TomTerm termVar) {
              Variable[option=Option(listOption), astName=Name[string=name]] -> {
                if(listVar.contains(name)) {
                  String line = findOriginTrackingLine(listOption);
                  messageTwoSameNameVariableError("make",name,line);
                } else {
                  listVar.add(name);
                }
              }
            }
            listM = listM.getTail();
          }
        }
      }
      optionList = optionList.getTail();
    }
  }

    /*
      testMakeDefine is used in 'RuleConstruct' method of TomParser.jj.
      We test here the occurrences of 'make' constructor for methods used in %rule.
      rulelist is the list of all (lhs,rhs) in this %rule (lhs -> rhs ).
    */
  public void testMakeDefine(ArrayList ruleList) throws TomException {
    if(!Flags.doVerify) return;

    int index = ruleList.size();
      /*
        yetStudied is the list of yet tested constructors. So if one constructor has no 'make',
        it generate only one message for this constructor, even if it is used several times.
      */
    ArrayList yetStudied = new ArrayList();
      /*
        we make tests on lhs and rhs parts thanks to using 'testMakeDefineAppl' method.
      */
    while( index != 0 ) {
      TomTerm ruleTerm = (TomTerm) ruleList.get(index - 1);
      %match( TomTerm ruleTerm ) {
        RewriteRule[lhs=Term(lhsTerm),rhs=Term(rhsTerm)] -> {
          testMakeDefineAppl(lhsTerm, yetStudied);
          testMakeDefineAppl(rhsTerm, yetStudied);
        }
      }		
      index--;
    }
  }


    /*
      testMakeDefineAppl is used only in 'testMakeDefine' method of Tomverifier.t
      We test here the occurrences of 'make' constructor for methods used in %rule.
      ruleTerm is one part of %rule : it has ever an Appl structure.
      yetStudied contains the name of methods yet studied by testMakeDefine.
    */
  public void testMakeDefineAppl(TomTerm ruleTerm, ArrayList yetStudied) throws TomException {
    if(!Flags.doVerify) return;

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
      testOperatorYetDefined is used in 'OperatorArray', 'Operator' and 'OperatorList' methods of TomParser.jj
      Here we test if a name of operator (intruced by %op, %oparray or %oplist) is defined only one time. 
      If it is defined several times we generate a warning. 
      The case of %typeterm, %typelist and %typearray is already tested by the initial program.
      name is the name of tested operator.
    */
  public void testOperatorYetDefined(String name) throws TomException {
    if(!Flags.doVerify) return;

    TomSymbol info = symbolTable().getSymbol(name);
      /*
        if info == null, it is the first time that this name is introduced.
      */
    if ( info != null ) {
      %match(TomSymbol info) {
        Symbol[option=Option(optionList)] -> {
          String line = findOriginTrackingLine(optionList);
          messageOperatorYetDefined(name,line);
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
  
  public void messageNumberArgumentsError(int nbArg, int nbArg2, String name, String line) throws TomException {
    String s = "Bad number of arguments for method '" + name + "':" +
      nbArg + " arguments are required but " + nbArg2 + " are given";
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

    /*
      messageMatchTypeVariableError is called by 'contest,GlVar' case
      in 'pass1' method of TomChecker.t.
      optionMatchTypeVariable contains informations about line error.
    */
  public void messageMatchTypeVariableError(String name, String type) throws TomException {
    OptionList optionList = optionMatchTypeVariable.getOptionList();
    String line = findOriginTrackingLine("Match", optionList);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct";
    messageError(line,s);
  }	

  public void messageTypeOperatorError(String line, String name, String type) throws TomException {
    String s = "Slot '" + name + "' has a wrong type: '" + type + "'";
    messageError(line,s);
  }

  public void messageImpossibleInRule(OptionList optionList, String name) throws TomException {
    String line = findOriginTrackingLine(optionList);
    String s = "Single '_' are not allowed in %rule" + "'" + name + "' is not correct";
    messageError(line,s);
  }

  public void messageErrorVariableStar(String nameVariableStar, String nameMethod ,String line) throws TomException {
    String s = "List variable '" + nameVariableStar + "' cannot be used in '" + nameMethod + "'";
    messageError(line,s);
  }
 
  public void messageErrorVariableStarBis(String nameVariableStar, String line) throws TomException {
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

  public void messageNumberArgumentMatchError( int nbGlVar, int nbAppl, String line) throws TomException {
    String s = "Bad number of arguments in match: " + nbGlVar + 
      " arguments are required but " + nbAppl + " are given"; 
    messageError(line,s);
  }

    /************************************
     * Error messages which generate     *
     * no exit, but the program is wrong *
     ************************************/

  public void messageVariableError(String name, String line) {
    System.out.println("\n"+" *** Variable '"+name+"' is strange - Line : "+line+" ***");
    Flags.findErrors = true;
  }

  public void messageRuleTypeAndConstructorEgality(String  name, String nameExpected, String type, String typeExpected, OptionList optionList) {
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Error in %rule before '->' - Line : "+line);
    System.out.println(" *** '" + nameExpected + "' of type '" + typeExpected +
                       "' is expected, but '" + name + "' of type '" + type +
                       "' is given");
    Flags.findErrors = true;
  }

  public void messageImpossibleUnderscore(OptionList optionList) {
    String line = findOriginTrackingLine(optionList);
    System.out.println("\n *** Underscores are not allowed in the right side of '->' in %rule");
    System.out.println(" *** '_' is impossible - Line : "+line);
    Flags.findErrors = true;
  }

  public void messageMacroFunctionRepeated(String nameFunction, String line) {
    System.out.println("\n *** Repeated macro-functions : ");
    System.out.println(" *** '" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  }

  public void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
    System.out.println("\n *** Missing macro-functions in one of construct '"+nameConstruct+"'");
    System.out.println(" *** Missing functions : "+list);
    Flags.findErrors = true;
  }
	
  public void messageTwoSameNameVariableError(String nameFunction, String nameVar, String line) {
    System.out.println("\n *** Arguments must be linear in function '" + nameFunction + "'");
    System.out.println(" *** Variable '"+nameVar+"' is repeated - Line : "+line);
    Flags.findErrors = true;
  }

  public void  messageMakeNotFoundForRule( String name, String line ) {
    System.out.println("\n *** Make declaration not found for operator '"+name+"' - Line : "+line);
    Flags.findErrors = true;
  }

  public void messageSlotRepeatedError(TomTerm pairSlotName, String name) {
    System.out.println("\n"+" *** Same slot names can not be used in same method");
    %match( TomTerm pairSlotName ) {
      Pair[appl=Appl[option=Option(list)]] -> {
        String line = findOriginTrackingLine(list);
        System.out.println(" *** Slot Name : '"+name+"' - Line : "+line);
      }
    }
    Flags.findErrors = true;
  }

  public void messageSlotNameError(TomTerm pairSlotName, TomList listOfPossibleSlot) {
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

  public void messageSlotNumberError(TomTerm pairSlotName, int nbSlot, int nbPair) {
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

  public void messageTypeArgumentMatchError( String typeAsked, String typeGiven, String line, int nbOfError ) {
    if(Flags.noWarning) return;
    if ( nbOfError == 1 )
    { System.out.println("\n"+" *** Warning *** Possible bad type in match"); }
    System.out.println(" *** Type '"+typeAsked+"' is required but Type '"+typeGiven+"' is given"+" - Line : "+line);
  }

  public void messageTypeArgumentMethodError( String name, TomType oneIn, TomType oneOut, String oneOutName, int numArg, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning ***");
    System.out.println(" *** Bad type for argument number '" + numArg + "' in method '" + name + "' - Line : " + line);
    String out = oneOut.getTomType().getString();
    String in  = oneIn.getTomType().getString();
    System.out.println(" *** '" + oneOutName +
                       "' returns an object of type '" + out +
                       "' but type '" + in + "' is required");
  }

  public void messageRepeatedVariableError( String name, String typeFind, String typeExpected, String line) {
    if(Flags.noWarning) return;
	
    System.out.println("\n"+" *** Warning ***");
    System.out.println(" *** Repeated variable with different types - Line : "+line);
    System.out.println(" *** Variable '" + name + "' has two types : '"
                       + typeFind + "' and '" + typeExpected + "'");
  }

  public void messageRuleTypeEgality(String name, String type, String typeExpected, OptionList optionList) {
    if(Flags.noWarning) return;
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n"+" *** Warning *** Error in %rule after '->' - Line : " + line);
    System.out.println(" *** Type '" + typeExpected + "' is expected, but '" + name + "' of type '" + type + "' is given");
  }

  public void messageVariableWithParenError( String  name, String line ) {
    if(Flags.noWarning) return;
    System.out.println("\n *** Warning *** Variable with () is not recommanded");
    System.out.println(" *** Variable '"+name+"' has () - Line : "+line);
  }

  public void messageOperatorYetDefined(String name, String line) {
    if(Flags.noWarning) return;
    System.out.println("\n"+" *** Warning *** Multiple definition of operator");
    System.out.println(" *** Operator '"+ name +"' is already defined - Line : "+line+" ***");
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
  public String findOriginTrackingLine(String name, OptionList optionList) {
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
  public String findOriginTrackingLine(OptionList optionList) {
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

    /*
      It is easy : we have a structure contained only an TomList and
      we return this ATermList.
    */
  public TomList extractList(TomTerm t) {
    %match( TomTerm t ) {
      SubjectList(list) -> { return list; }
      PatternList(list) -> { return list; }
      TermList(list)    -> { return list; }
    }
    return empty();
  } 

    /*
      typeOperatorList, lineOperatorList and nameOperatorList are global ArrayList. 
      These lists are used in testTypeOperator. There are completed thanks to 'addTypeOperator'
      method in 'Operator', 'OperatorList', 'OperatorArray' methods of TomParser.jj.
      For a same index i, we have, in the 3 lists, informations about the same object :
      type (typeOperatorList) / name (nameOperatorList) / line (lineOperatorList) 
    */
  public void addTypeOperator(String type, String line, String name) {
    if(!Flags.doVerify) return;

    if(symbolTable().getType(type)==null) { 	
      typeOperatorList.add(type); 
      lineOperatorList.add(line);
      nameOperatorList.add(name);
    }
  }

    /*
      extractVariable is used in 'testRuleVariable' method of Tomverifier.t.
      We extract informations about variables (i.e. for object as symbolTable().getSymbol(_) == null)
      which appear in term. 
      term has an Appl structure.
      nameVariable and lineVariable stock informations about these variables.
      For a same index, we have informations about same variable.
    */
  public void extractVariable(TomTerm term, ArrayList nameVariable, ArrayList lineVariable) {
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
  public void extractVariableList(TomList termList, ArrayList nameVariable, ArrayList lineVariable){
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
  public String extractType(TomSymbol term) {
    %match(TomSymbol term) {
      Symbol[typesToType=TypesToType(_,t)] -> { return getTomType(t); }
    }
    return null;
  }

    /*
      term is an element extract of SymbolTable.
      typeIn returns the type structure of arguments for concerned element.
    */
  public TomList typeIn( TomSymbol term ) {
    %match(TomSymbol term) {
      Symbol[typesToType=TypesToType(list,_)] -> { return list; }
    }
    return empty();
  }

    /*
      term is an element extract of SymbolTable.
      typeOut returns the result type structure of concerned element.
    */
  public TomType typeOut(TomSymbol term ) {
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
  public TomType findTypeOf(String name, TomTerm inTerm) {
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
  public void repeatedVariable( TomTerm term, ArrayList nameVar, ArrayList typeVar, String typeGlVar ) {
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


