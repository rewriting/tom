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
    
    {
       TomTerm  tom_match1_1 = null;
      tom_match1_1 = ( TomTerm ) term;
matchlab_match1_pattern1: {
         OptionList  list = null;
         TomList  argsList = null;
         String  name = null;
        if(tom_is_fun_sym_Appl(tom_match1_1)) {
           Option  tom_match1_1_1 = null;
           TomName  tom_match1_1_2 = null;
           TomList  tom_match1_1_3 = null;
          tom_match1_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match1_1);
          tom_match1_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match1_1);
          tom_match1_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match1_1);
          if(tom_is_fun_sym_Option(tom_match1_1_1)) {
             OptionList  tom_match1_1_1_1 = null;
            tom_match1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match1_1_1);
            list = ( OptionList ) tom_match1_1_1_1;
            if(tom_is_fun_sym_Name(tom_match1_1_2)) {
               String  tom_match1_1_2_1 = null;
              tom_match1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match1_1_2);
              name = ( String ) tom_match1_1_2_1;
              argsList = ( TomList ) tom_match1_1_3;
              
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
    
    {
       TomTerm  tom_match2_1 = null;
      tom_match2_1 = ( TomTerm ) lhs;
matchlab_match2_pattern1: {
         OptionList  list = null;
         String  name1 = null;
        if(tom_is_fun_sym_Appl(tom_match2_1)) {
           Option  tom_match2_1_1 = null;
           TomName  tom_match2_1_2 = null;
           TomList  tom_match2_1_3 = null;
          tom_match2_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match2_1);
          tom_match2_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match2_1);
          tom_match2_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match2_1);
          if(tom_is_fun_sym_Option(tom_match2_1_1)) {
             OptionList  tom_match2_1_1_1 = null;
            tom_match2_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match2_1_1);
            list = ( OptionList ) tom_match2_1_1_1;
            if(tom_is_fun_sym_Name(tom_match2_1_2)) {
               String  tom_match2_1_2_1 = null;
              tom_match2_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match2_1_2);
              name1 = ( String ) tom_match2_1_2_1;
              
        TomType term = typeOut(symbolTable().getSymbol(name1));
        
              {
                 TomType  tom_match3_1 = null;
                tom_match3_1 = ( TomType ) term;
matchlab_match3_pattern1: {
                   String  t = null;
                  if(tom_is_fun_sym_TomTypeAlone(tom_match3_1)) {
                     String  tom_match3_1_1 = null;
                    tom_match3_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match3_1);
                    t = ( String ) tom_match3_1_1;
                     
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
              
      
            }
          }
        }
}
matchlab_match2_pattern2: {
         String  name1 = null;
         OptionList  list = null;
        if(tom_is_fun_sym_VariableStar(tom_match2_1)) {
           Option  tom_match2_1_1 = null;
           TomName  tom_match2_1_2 = null;
           TomType  tom_match2_1_3 = null;
          tom_match2_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match2_1);
          tom_match2_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match2_1);
          tom_match2_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match2_1);
          if(tom_is_fun_sym_Option(tom_match2_1_1)) {
             OptionList  tom_match2_1_1_1 = null;
            tom_match2_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match2_1_1);
            list = ( OptionList ) tom_match2_1_1_1;
            if(tom_is_fun_sym_Name(tom_match2_1_2)) {
               String  tom_match2_1_2_1 = null;
              tom_match2_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match2_1_2);
              name1 = ( String ) tom_match2_1_2_1;
               
        String line = findOriginTrackingLine(name1,list);
        messageErrorVariableStarBis(name1, line); 
      
            }
          }
        }
}
matchlab_match2_pattern3: {
         OptionList  t = null;
        if(tom_is_fun_sym_Placeholder(tom_match2_1)) {
           Option  tom_match2_1_1 = null;
          tom_match2_1_1 = ( Option ) tom_get_slot_Placeholder_option(tom_match2_1);
          if(tom_is_fun_sym_Option(tom_match2_1_1)) {
             OptionList  tom_match2_1_1_1 = null;
            tom_match2_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match2_1_1);
            t = ( OptionList ) tom_match2_1_1_1;
             
        messageImpossibleInRule(t, "_"); 
      
          }
        }
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
    
    {
       TomTerm  tom_match4_1 = null;
      tom_match4_1 = ( TomTerm ) rhs;
matchlab_match4_pattern1: {
         TomList  argsList = null;
        if(tom_is_fun_sym_Appl(tom_match4_1)) {
           Option  tom_match4_1_1 = null;
           TomName  tom_match4_1_2 = null;
           TomList  tom_match4_1_3 = null;
          tom_match4_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match4_1);
          tom_match4_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match4_1);
          tom_match4_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match4_1);
          argsList = ( TomList ) tom_match4_1_3;
          
        while(!argsList.isEmpty()) {
          TomTerm oneArgs = argsList.getHead();
          testNoUnderscore(oneArgs,false);
          argsList = argsList.getTail();
        }
      
        }
}
matchlab_match4_pattern2: {
         OptionList  t = null;
        if(tom_is_fun_sym_Placeholder(tom_match4_1)) {
           Option  tom_match4_1_1 = null;
          tom_match4_1_1 = ( Option ) tom_get_slot_Placeholder_option(tom_match4_1);
          if(tom_is_fun_sym_Option(tom_match4_1_1)) {
             OptionList  tom_match4_1_1_1 = null;
            tom_match4_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match4_1_1);
            t = ( OptionList ) tom_match4_1_1_1;
             
        if(!bool) {
          messageImpossibleUnderscore(t);
        } 
      
          }
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
      
    {
       TomTerm  tom_match5_1 = null;
      tom_match5_1 = ( TomTerm ) rhs;
matchlab_match5_pattern1: {
         String  name1 = null;
         OptionList  list = null;
        if(tom_is_fun_sym_Appl(tom_match5_1)) {
           Option  tom_match5_1_1 = null;
           TomName  tom_match5_1_2 = null;
           TomList  tom_match5_1_3 = null;
          tom_match5_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match5_1);
          tom_match5_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match5_1);
          tom_match5_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match5_1);
          if(tom_is_fun_sym_Option(tom_match5_1_1)) {
             OptionList  tom_match5_1_1_1 = null;
            tom_match5_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match5_1_1);
            list = ( OptionList ) tom_match5_1_1_1;
            if(tom_is_fun_sym_Name(tom_match5_1_2)) {
               String  tom_match5_1_2_1 = null;
              tom_match5_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match5_1_2);
              name1 = ( String ) tom_match5_1_2_1;
              
            /*
              We look the type of the resulted object of this Appl.
              If resulted object has no type, typeOut returns EmptyType) (for example
              if it is a variable).
            */
          TomType term = typeOut(symbolTable().getSymbol(name1));
          
              {
                 TomType  tom_match6_1 = null;
                tom_match6_1 = ( TomType ) term;
matchlab_match6_pattern1: {
                   String  t = null;
                  if(tom_is_fun_sym_TomTypeAlone(tom_match6_1)) {
                     String  tom_match6_1_1 = null;
                    tom_match6_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match6_1);
                    t = ( String ) tom_match6_1_1;
                     
                /* types are not the same */
              if ( t != nameType.get(1) ) {
                messageRuleTypeEgality(name1,t,(String)nameType.get(1),list);
              }
            
                  }
}
matchlab_match6_pattern2: {
                  if(tom_is_fun_sym_EmptyType(tom_match6_1)) {
                     
                /*
                  we look if an object with name 'name1' exists in lhs 
                  and we set its type.
                */
              TomType typeFind = findTypeOf(name1, lhs);
              
                    {
                       TomType  tom_match7_1 = null;
                      tom_match7_1 = ( TomType ) typeFind;
matchlab_match7_pattern1: {
                         String  tm = null;
                        if(tom_is_fun_sym_TomTypeAlone(tom_match7_1)) {
                           String  tom_match7_1_1 = null;
                          tom_match7_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match7_1);
                          tm = ( String ) tom_match7_1_1;
                           
                  if ( tm != nameType.get(1) ) {
                    messageRuleTypeEgality(name1,tm,(String)nameType.get(1),list);
                  }
                
                        }
}
                    }
                    
            
                  }
}
              }
              
        
            }
          }
        }
}
matchlab_match5_pattern2: {
         OptionList  list = null;
         String  name1 = null;
        if(tom_is_fun_sym_VariableStar(tom_match5_1)) {
           Option  tom_match5_1_1 = null;
           TomName  tom_match5_1_2 = null;
           TomType  tom_match5_1_3 = null;
          tom_match5_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match5_1);
          tom_match5_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match5_1);
          tom_match5_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match5_1);
          if(tom_is_fun_sym_Option(tom_match5_1_1)) {
             OptionList  tom_match5_1_1_1 = null;
            tom_match5_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match5_1_1);
            list = ( OptionList ) tom_match5_1_1_1;
            if(tom_is_fun_sym_Name(tom_match5_1_2)) {
               String  tom_match5_1_2_1 = null;
              tom_match5_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match5_1_2);
              name1 = ( String ) tom_match5_1_2_1;
               
          String line = findOriginTrackingLine(name1,list);
          messageErrorVariableStarBis(name1, line); 
        
            }
          }
        }
}
matchlab_match5_pattern3: {
         OptionList  t = null;
        if(tom_is_fun_sym_Placeholder(tom_match5_1)) {
           Option  tom_match5_1_1 = null;
          tom_match5_1_1 = ( Option ) tom_get_slot_Placeholder_option(tom_match5_1);
          if(tom_is_fun_sym_Option(tom_match5_1_1)) {
             OptionList  tom_match5_1_1_1 = null;
            tom_match5_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match5_1_1);
            t = ( OptionList ) tom_match5_1_1_1;
             
          messageImpossibleInRule(t, "_"); 
        
          }
        }
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
      
    {
       TomTerm  tom_match8_1 = null;
      tom_match8_1 = ( TomTerm ) term;
matchlab_match8_pattern1: {
         String  type = null;
         String  name = null;
        if(tom_is_fun_sym_GLVar(tom_match8_1)) {
           String  tom_match8_1_1 = null;
           TomType  tom_match8_1_2 = null;
          tom_match8_1_1 = ( String ) tom_get_slot_GLVar_strName(tom_match8_1);
          tom_match8_1_2 = ( TomType ) tom_get_slot_GLVar_astType(tom_match8_1);
          name = ( String ) tom_match8_1_1;
          if(tom_is_fun_sym_TomTypeAlone(tom_match8_1_2)) {
             String  tom_match8_1_2_1 = null;
            tom_match8_1_2_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match8_1_2);
            type = ( String ) tom_match8_1_2_1;
            
          nbGlVar = nbGlVar + 1; 
          typeGlVar.add(type);
        
          }
        }
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
      
    {
       TomTerm  tom_match9_1 = null;
      tom_match9_1 = ( TomTerm ) term;
matchlab_match9_pattern1: {
         TomTerm  t = null;
        if(tom_is_fun_sym_PatternAction(tom_match9_1)) {
           TomTerm  tom_match9_1_1 = null;
           TomTerm  tom_match9_1_2 = null;
          tom_match9_1_1 = ( TomTerm ) tom_get_slot_PatternAction_termList(tom_match9_1);
          tom_match9_1_2 = ( TomTerm ) tom_get_slot_PatternAction_tom(tom_match9_1);
          t = ( TomTerm ) tom_match9_1_1;
           termBis= t; 
        }
}
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
      
    {
       TomTerm  tom_match10_1 = null;
      tom_match10_1 = ( TomTerm ) termApplLine;
matchlab_match10_pattern1: {
         OptionList  list = null;
        if(tom_is_fun_sym_Appl(tom_match10_1)) {
           Option  tom_match10_1_1 = null;
           TomName  tom_match10_1_2 = null;
           TomList  tom_match10_1_3 = null;
          tom_match10_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match10_1);
          tom_match10_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match10_1);
          tom_match10_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match10_1);
          if(tom_is_fun_sym_Option(tom_match10_1_1)) {
             OptionList  tom_match10_1_1_1 = null;
            tom_match10_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match10_1_1);
            list = ( OptionList ) tom_match10_1_1_1;
            
          line = findOriginTrackingLine(list);
        
          }
        }
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
        
    {
       TomTerm  tom_match11_1 = null;
      tom_match11_1 = ( TomTerm ) termAppl;
matchlab_match11_pattern1: {
         String  name = null;
        if(tom_is_fun_sym_Appl(tom_match11_1)) {
           Option  tom_match11_1_1 = null;
           TomName  tom_match11_1_2 = null;
           TomList  tom_match11_1_3 = null;
          tom_match11_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match11_1);
          tom_match11_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match11_1);
          tom_match11_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match11_1);
          if(tom_is_fun_sym_Name(tom_match11_1_2)) {
             String  tom_match11_1_2_1 = null;
            tom_match11_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match11_1_2);
            name = ( String ) tom_match11_1_2_1;
            
            nbAppl = nbAppl + 1;
            typeAppl.add(extractType(symbolTable().getSymbol(name)));
          
          }
        }
}
matchlab_match11_pattern2: {
        if(tom_is_fun_sym_Placeholder(tom_match11_1)) {
           Option  tom_match11_1_1 = null;
          tom_match11_1_1 = ( Option ) tom_get_slot_Placeholder_option(tom_match11_1);
           nbAppl = nbAppl + 1; typeAppl.add((TomTerm) null); 
        }
}
matchlab_match11_pattern3: {
         OptionList  list = null;
         String  name1 = null;
        if(tom_is_fun_sym_VariableStar(tom_match11_1)) {
           Option  tom_match11_1_1 = null;
           TomName  tom_match11_1_2 = null;
           TomType  tom_match11_1_3 = null;
          tom_match11_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match11_1);
          tom_match11_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match11_1);
          tom_match11_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match11_1);
          if(tom_is_fun_sym_Option(tom_match11_1_1)) {
             OptionList  tom_match11_1_1_1 = null;
            tom_match11_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match11_1_1);
            list = ( OptionList ) tom_match11_1_1_1;
            if(tom_is_fun_sym_Name(tom_match11_1_2)) {
               String  tom_match11_1_2_1 = null;
              tom_match11_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match11_1_2);
              name1 = ( String ) tom_match11_1_2_1;
               
            line = findOriginTrackingLine(name1,list);
            messageErrorVariableStarBis(name1, line); 
          
            }
          }
        }
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
        
    {
       TomTerm  tom_match12_1 = null;
      tom_match12_1 = ( TomTerm ) pairSlotName;
matchlab_match12_pattern1: {
         TomTerm  term = null;
        if(tom_is_fun_sym_Pair(tom_match12_1)) {
           TomTerm  tom_match12_1_1 = null;
           TomTerm  tom_match12_1_2 = null;
          tom_match12_1_1 = ( TomTerm ) tom_get_slot_Pair_slotName(tom_match12_1);
          tom_match12_1_2 = ( TomTerm ) tom_get_slot_Pair_appl(tom_match12_1);
          term = ( TomTerm ) tom_match12_1_1;
          
            if ( term == termSlotName ) { findSlotNameEquivalent = true; }
          
        }
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
      
    {
       TomTerm  tom_match13_1 = null;
      tom_match13_1 = ( TomTerm ) oneSlot;
matchlab_match13_pattern1: {
         String  slotName = null;
        if(tom_is_fun_sym_SlotName(tom_match13_1)) {
           String  tom_match13_1_1 = null;
          tom_match13_1_1 = ( String ) tom_get_slot_SlotName_string(tom_match13_1);
          slotName = ( String ) tom_match13_1_1;
           slotPossible.add(" " + slotName +" "); 
        }
}
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
      
    {
       TomTerm  tom_match14_1 = null;
      tom_match14_1 = ( TomTerm ) onePair;
matchlab_match14_pattern1: {
         String  slotName = null;
        if(tom_is_fun_sym_Pair(tom_match14_1)) {
           TomTerm  tom_match14_1_1 = null;
           TomTerm  tom_match14_1_2 = null;
          tom_match14_1_1 = ( TomTerm ) tom_get_slot_Pair_slotName(tom_match14_1);
          tom_match14_1_2 = ( TomTerm ) tom_get_slot_Pair_appl(tom_match14_1);
          if(tom_is_fun_sym_SlotName(tom_match14_1_1)) {
             String  tom_match14_1_1_1 = null;
            tom_match14_1_1_1 = ( String ) tom_get_slot_SlotName_string(tom_match14_1_1);
            slotName = ( String ) tom_match14_1_1_1;
             slotGiven.add(" " + slotName + " "); 
          }
        }
}
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
    
    {
       TomTerm  tom_match15_1 = null;
      tom_match15_1 = ( TomTerm ) termAppl;
matchlab_match15_pattern1: {
         TomList  argsList = null;
         String  name = null;
         OptionList  list = null;
        if(tom_is_fun_sym_Appl(tom_match15_1)) {
           Option  tom_match15_1_1 = null;
           TomName  tom_match15_1_2 = null;
           TomList  tom_match15_1_3 = null;
          tom_match15_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match15_1);
          tom_match15_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match15_1);
          tom_match15_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match15_1);
          if(tom_is_fun_sym_Option(tom_match15_1_1)) {
             OptionList  tom_match15_1_1_1 = null;
            tom_match15_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match15_1_1);
            list = ( OptionList ) tom_match15_1_1_1;
            if(tom_is_fun_sym_Name(tom_match15_1_2)) {
               String  tom_match15_1_2_1 = null;
              tom_match15_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match15_1_2);
              name = ( String ) tom_match15_1_2_1;
              argsList = ( TomList ) tom_match15_1_3;
              
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
            
              {
                 TomTerm  tom_match16_1 = null;
                tom_match16_1 = ( TomTerm ) term;
matchlab_match16_pattern1: {
                   String  name1 = null;
                  if(tom_is_fun_sym_Appl(tom_match16_1)) {
                     Option  tom_match16_1_1 = null;
                     TomName  tom_match16_1_2 = null;
                     TomList  tom_match16_1_3 = null;
                    tom_match16_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match16_1);
                    tom_match16_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match16_1);
                    tom_match16_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match16_1);
                    if(tom_is_fun_sym_Name(tom_match16_1_2)) {
                       String  tom_match16_1_2_1 = null;
                      tom_match16_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match16_1_2);
                      name1 = ( String ) tom_match16_1_2_1;
                      
                tabTypeOut[i] = typeOut(symbolTable().getSymbol(name1));
                tabNameOut[i] = name1;
              
                    }
                  }
}
matchlab_match16_pattern2: {
                  if(tom_is_fun_sym_Placeholder(tom_match16_1)) {
                     Option  tom_match16_1_1 = null;
                    tom_match16_1_1 = ( Option ) tom_get_slot_Placeholder_option(tom_match16_1);
                     
                tabTypeOut[i] = 
tom_make_EmptyType()                    ;
                tabNameOut[i] = noNameTypeOut;
              
                  }
}
matchlab_match16_pattern3: {
                   String  name1 = null;
                  if(tom_is_fun_sym_VariableStar(tom_match16_1)) {
                     Option  tom_match16_1_1 = null;
                     TomName  tom_match16_1_2 = null;
                     TomType  tom_match16_1_3 = null;
                    tom_match16_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match16_1);
                    tom_match16_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match16_1);
                    tom_match16_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match16_1);
                    if(tom_is_fun_sym_Name(tom_match16_1_2)) {
                       String  tom_match16_1_2_1 = null;
                      tom_match16_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match16_1_2);
                      name1 = ( String ) tom_match16_1_2_1;
                      
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
              }
              
          }

            /*
              we test the correspondence of arguments between needed types (stocked in
              infoTypeIn) and given types (stocked in infoTypeOut)
            */
          for(int i=0 ; i<arity ; i++) {
            TomType oneIn = infoTypeIn.getHead().getAstType();
            if (tabTypeOut[i] != 
tom_make_EmptyType()               && oneIn!=tabTypeOut[i]) {
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
      
    {
       Option  tom_match17_1 = null;
      tom_match17_1 = ( Option ) term;
matchlab_match17_pattern1: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match17_1)) {
           Declaration  tom_match17_1_1 = null;
          tom_match17_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match17_1);
          if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match17_1_1)) {
             TomTerm  tom_match17_1_1_1 = null;
             TomTerm  tom_match17_1_1_2 = null;
            tom_match17_1_1_1 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match17_1_1);
            tom_match17_1_1_2 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match17_1_1);
            if(tom_is_fun_sym_Variable(tom_match17_1_1_1)) {
               Option  tom_match17_1_1_1_1 = null;
               TomName  tom_match17_1_1_1_2 = null;
               TomType  tom_match17_1_1_1_3 = null;
              tom_match17_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_1);
              tom_match17_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_1);
              tom_match17_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_1);
              if(tom_is_fun_sym_Option(tom_match17_1_1_1_1)) {
                 OptionList  tom_match17_1_1_1_1_1 = null;
                tom_match17_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match17_1_1_1_1);
                listOption = ( OptionList ) tom_match17_1_1_1_1_1;
                
          checkField("get_fun_sym",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match17_pattern2: {
         String  name2 = null;
         String  name1 = null;
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match17_1)) {
           Declaration  tom_match17_1_1 = null;
          tom_match17_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match17_1);
          if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match17_1_1)) {
             TomTerm  tom_match17_1_1_1 = null;
             TomTerm  tom_match17_1_1_2 = null;
             TomTerm  tom_match17_1_1_3 = null;
            tom_match17_1_1_1 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match17_1_1);
            tom_match17_1_1_2 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match17_1_1);
            tom_match17_1_1_3 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match17_1_1);
            if(tom_is_fun_sym_Variable(tom_match17_1_1_1)) {
               Option  tom_match17_1_1_1_1 = null;
               TomName  tom_match17_1_1_1_2 = null;
               TomType  tom_match17_1_1_1_3 = null;
              tom_match17_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_1);
              tom_match17_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_1);
              tom_match17_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_1);
              if(tom_is_fun_sym_Option(tom_match17_1_1_1_1)) {
                 OptionList  tom_match17_1_1_1_1_1 = null;
                tom_match17_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match17_1_1_1_1);
                listOption = ( OptionList ) tom_match17_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match17_1_1_1_2)) {
                   String  tom_match17_1_1_1_2_1 = null;
                  tom_match17_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_1_2);
                  name1 = ( String ) tom_match17_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match17_1_1_2)) {
                     Option  tom_match17_1_1_2_1 = null;
                     TomName  tom_match17_1_1_2_2 = null;
                     TomType  tom_match17_1_1_2_3 = null;
                    tom_match17_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_2);
                    tom_match17_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_2);
                    tom_match17_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match17_1_1_2_2)) {
                       String  tom_match17_1_1_2_2_1 = null;
                      tom_match17_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_2_2);
                      name2 = ( String ) tom_match17_1_1_2_2_1;
                      
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match17_pattern3: {
         String  name1 = null;
         OptionList  listOption = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match17_1)) {
           Declaration  tom_match17_1_1 = null;
          tom_match17_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match17_1);
          if(tom_is_fun_sym_GetSubtermDecl(tom_match17_1_1)) {
             TomTerm  tom_match17_1_1_1 = null;
             TomTerm  tom_match17_1_1_2 = null;
             TomTerm  tom_match17_1_1_3 = null;
            tom_match17_1_1_1 = ( TomTerm ) tom_get_slot_GetSubtermDecl_termArg(tom_match17_1_1);
            tom_match17_1_1_2 = ( TomTerm ) tom_get_slot_GetSubtermDecl_numberArg(tom_match17_1_1);
            tom_match17_1_1_3 = ( TomTerm ) tom_get_slot_GetSubtermDecl_tlCode(tom_match17_1_1);
            if(tom_is_fun_sym_Variable(tom_match17_1_1_1)) {
               Option  tom_match17_1_1_1_1 = null;
               TomName  tom_match17_1_1_1_2 = null;
               TomType  tom_match17_1_1_1_3 = null;
              tom_match17_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_1);
              tom_match17_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_1);
              tom_match17_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_1);
              if(tom_is_fun_sym_Option(tom_match17_1_1_1_1)) {
                 OptionList  tom_match17_1_1_1_1_1 = null;
                tom_match17_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match17_1_1_1_1);
                listOption = ( OptionList ) tom_match17_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match17_1_1_1_2)) {
                   String  tom_match17_1_1_1_2_1 = null;
                  tom_match17_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_1_2);
                  name1 = ( String ) tom_match17_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match17_1_1_2)) {
                     Option  tom_match17_1_1_2_1 = null;
                     TomName  tom_match17_1_1_2_2 = null;
                     TomType  tom_match17_1_1_2_3 = null;
                    tom_match17_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_2);
                    tom_match17_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_2);
                    tom_match17_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match17_1_1_2_2)) {
                       String  tom_match17_1_1_2_2_1 = null;
                      tom_match17_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_2_2);
                      name2 = ( String ) tom_match17_1_1_2_2_1;
                      
          checkFieldLinearArgs("get_subterm",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match17_pattern4: {
         String  name1 = null;
         String  name2 = null;
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match17_1)) {
           Declaration  tom_match17_1_1 = null;
          tom_match17_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match17_1);
          if(tom_is_fun_sym_TermsEqualDecl(tom_match17_1_1)) {
             TomTerm  tom_match17_1_1_1 = null;
             TomTerm  tom_match17_1_1_2 = null;
             TomTerm  tom_match17_1_1_3 = null;
            tom_match17_1_1_1 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg1(tom_match17_1_1);
            tom_match17_1_1_2 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg2(tom_match17_1_1);
            tom_match17_1_1_3 = ( TomTerm ) tom_get_slot_TermsEqualDecl_tlCode(tom_match17_1_1);
            if(tom_is_fun_sym_Variable(tom_match17_1_1_1)) {
               Option  tom_match17_1_1_1_1 = null;
               TomName  tom_match17_1_1_1_2 = null;
               TomType  tom_match17_1_1_1_3 = null;
              tom_match17_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_1);
              tom_match17_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_1);
              tom_match17_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_1);
              if(tom_is_fun_sym_Option(tom_match17_1_1_1_1)) {
                 OptionList  tom_match17_1_1_1_1_1 = null;
                tom_match17_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match17_1_1_1_1);
                listOption = ( OptionList ) tom_match17_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match17_1_1_1_2)) {
                   String  tom_match17_1_1_1_2_1 = null;
                  tom_match17_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_1_2);
                  name1 = ( String ) tom_match17_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match17_1_1_2)) {
                     Option  tom_match17_1_1_2_1 = null;
                     TomName  tom_match17_1_1_2_2 = null;
                     TomType  tom_match17_1_1_2_3 = null;
                    tom_match17_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match17_1_1_2);
                    tom_match17_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match17_1_1_2);
                    tom_match17_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match17_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match17_1_1_2_2)) {
                       String  tom_match17_1_1_2_2_1 = null;
                      tom_match17_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match17_1_1_2_2);
                      name2 = ( String ) tom_match17_1_1_2_2_1;
                      
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
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
      
    {
       Option  tom_match18_1 = null;
      tom_match18_1 = ( Option ) term;
matchlab_match18_pattern1: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                 
          checkField("get_fun_sym",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match18_pattern2: {
         String  name1 = null;
         OptionList  listOption = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
             TomTerm  tom_match18_1_1_3 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match18_1_1);
            tom_match18_1_1_3 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match18_1_1_1_2)) {
                   String  tom_match18_1_1_1_2_1 = null;
                  tom_match18_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match18_1_1_1_2);
                  name1 = ( String ) tom_match18_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match18_1_1_2)) {
                     Option  tom_match18_1_1_2_1 = null;
                     TomName  tom_match18_1_1_2_2 = null;
                     TomType  tom_match18_1_1_2_3 = null;
                    tom_match18_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_2);
                    tom_match18_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_2);
                    tom_match18_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match18_1_1_2_2)) {
                       String  tom_match18_1_1_2_2_1 = null;
                      tom_match18_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match18_1_1_2_2);
                      name2 = ( String ) tom_match18_1_1_2_2_1;
                      
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match18_pattern3: {
         OptionList  listOption = null;
         String  name2 = null;
         String  name1 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_TermsEqualDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
             TomTerm  tom_match18_1_1_3 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg1(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg2(tom_match18_1_1);
            tom_match18_1_1_3 = ( TomTerm ) tom_get_slot_TermsEqualDecl_tlCode(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match18_1_1_1_2)) {
                   String  tom_match18_1_1_1_2_1 = null;
                  tom_match18_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match18_1_1_1_2);
                  name1 = ( String ) tom_match18_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match18_1_1_2)) {
                     Option  tom_match18_1_1_2_1 = null;
                     TomName  tom_match18_1_1_2_2 = null;
                     TomType  tom_match18_1_1_2_3 = null;
                    tom_match18_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_2);
                    tom_match18_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_2);
                    tom_match18_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match18_1_1_2_2)) {
                       String  tom_match18_1_1_2_2_1 = null;
                      tom_match18_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match18_1_1_2_2);
                      name2 = ( String ) tom_match18_1_1_2_2_1;
                      
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match18_pattern4: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_GetHeadDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_GetHeadDecl_kid1(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_GetHeadDecl_kid2(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                
          checkField("get_head",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match18_pattern5: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_GetTailDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_GetTailDecl_kid1(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_GetTailDecl_kid2(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                
          checkField("get_tail",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match18_pattern6: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match18_1)) {
           Declaration  tom_match18_1_1 = null;
          tom_match18_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match18_1);
          if(tom_is_fun_sym_IsEmptyDecl(tom_match18_1_1)) {
             TomTerm  tom_match18_1_1_1 = null;
             TomTerm  tom_match18_1_1_2 = null;
            tom_match18_1_1_1 = ( TomTerm ) tom_get_slot_IsEmptyDecl_kid1(tom_match18_1_1);
            tom_match18_1_1_2 = ( TomTerm ) tom_get_slot_IsEmptyDecl_kid2(tom_match18_1_1);
            if(tom_is_fun_sym_Variable(tom_match18_1_1_1)) {
               Option  tom_match18_1_1_1_1 = null;
               TomName  tom_match18_1_1_1_2 = null;
               TomType  tom_match18_1_1_1_3 = null;
              tom_match18_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match18_1_1_1);
              tom_match18_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match18_1_1_1);
              tom_match18_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match18_1_1_1);
              if(tom_is_fun_sym_Option(tom_match18_1_1_1_1)) {
                 OptionList  tom_match18_1_1_1_1_1 = null;
                tom_match18_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match18_1_1_1_1);
                listOption = ( OptionList ) tom_match18_1_1_1_1_1;
                
          checkField("is_empty",findFunctions,listOption);
        
              }
            }
          }
        }
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
      
    {
       Option  tom_match19_1 = null;
      tom_match19_1 = ( Option ) term;
matchlab_match19_pattern1: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match19_1)) {
           Declaration  tom_match19_1_1 = null;
          tom_match19_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match19_1);
          if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match19_1_1)) {
             TomTerm  tom_match19_1_1_1 = null;
             TomTerm  tom_match19_1_1_2 = null;
            tom_match19_1_1_1 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match19_1_1);
            tom_match19_1_1_2 = ( TomTerm ) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match19_1_1);
            if(tom_is_fun_sym_Variable(tom_match19_1_1_1)) {
               Option  tom_match19_1_1_1_1 = null;
               TomName  tom_match19_1_1_1_2 = null;
               TomType  tom_match19_1_1_1_3 = null;
              tom_match19_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_1);
              tom_match19_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_1);
              tom_match19_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_1);
              if(tom_is_fun_sym_Option(tom_match19_1_1_1_1)) {
                 OptionList  tom_match19_1_1_1_1_1 = null;
                tom_match19_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match19_1_1_1_1);
                listOption = ( OptionList ) tom_match19_1_1_1_1_1;
                
          checkField("get_fun_sym",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match19_pattern2: {
         String  name1 = null;
         OptionList  listOption = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match19_1)) {
           Declaration  tom_match19_1_1 = null;
          tom_match19_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match19_1);
          if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match19_1_1)) {
             TomTerm  tom_match19_1_1_1 = null;
             TomTerm  tom_match19_1_1_2 = null;
             TomTerm  tom_match19_1_1_3 = null;
            tom_match19_1_1_1 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match19_1_1);
            tom_match19_1_1_2 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match19_1_1);
            tom_match19_1_1_3 = ( TomTerm ) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match19_1_1);
            if(tom_is_fun_sym_Variable(tom_match19_1_1_1)) {
               Option  tom_match19_1_1_1_1 = null;
               TomName  tom_match19_1_1_1_2 = null;
               TomType  tom_match19_1_1_1_3 = null;
              tom_match19_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_1);
              tom_match19_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_1);
              tom_match19_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_1);
              if(tom_is_fun_sym_Option(tom_match19_1_1_1_1)) {
                 OptionList  tom_match19_1_1_1_1_1 = null;
                tom_match19_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match19_1_1_1_1);
                listOption = ( OptionList ) tom_match19_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match19_1_1_1_2)) {
                   String  tom_match19_1_1_1_2_1 = null;
                  tom_match19_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_1_2);
                  name1 = ( String ) tom_match19_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match19_1_1_2)) {
                     Option  tom_match19_1_1_2_1 = null;
                     TomName  tom_match19_1_1_2_2 = null;
                     TomType  tom_match19_1_1_2_3 = null;
                    tom_match19_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_2);
                    tom_match19_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_2);
                    tom_match19_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match19_1_1_2_2)) {
                       String  tom_match19_1_1_2_2_1 = null;
                      tom_match19_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_2_2);
                      name2 = ( String ) tom_match19_1_1_2_2_1;
                      
          checkFieldLinearArgs("cmp_fun_sym",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match19_pattern3: {
         OptionList  listOption = null;
         String  name1 = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match19_1)) {
           Declaration  tom_match19_1_1 = null;
          tom_match19_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match19_1);
          if(tom_is_fun_sym_TermsEqualDecl(tom_match19_1_1)) {
             TomTerm  tom_match19_1_1_1 = null;
             TomTerm  tom_match19_1_1_2 = null;
             TomTerm  tom_match19_1_1_3 = null;
            tom_match19_1_1_1 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg1(tom_match19_1_1);
            tom_match19_1_1_2 = ( TomTerm ) tom_get_slot_TermsEqualDecl_termArg2(tom_match19_1_1);
            tom_match19_1_1_3 = ( TomTerm ) tom_get_slot_TermsEqualDecl_tlCode(tom_match19_1_1);
            if(tom_is_fun_sym_Variable(tom_match19_1_1_1)) {
               Option  tom_match19_1_1_1_1 = null;
               TomName  tom_match19_1_1_1_2 = null;
               TomType  tom_match19_1_1_1_3 = null;
              tom_match19_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_1);
              tom_match19_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_1);
              tom_match19_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_1);
              if(tom_is_fun_sym_Option(tom_match19_1_1_1_1)) {
                 OptionList  tom_match19_1_1_1_1_1 = null;
                tom_match19_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match19_1_1_1_1);
                listOption = ( OptionList ) tom_match19_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match19_1_1_1_2)) {
                   String  tom_match19_1_1_1_2_1 = null;
                  tom_match19_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_1_2);
                  name1 = ( String ) tom_match19_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match19_1_1_2)) {
                     Option  tom_match19_1_1_2_1 = null;
                     TomName  tom_match19_1_1_2_2 = null;
                     TomType  tom_match19_1_1_2_3 = null;
                    tom_match19_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_2);
                    tom_match19_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_2);
                    tom_match19_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match19_1_1_2_2)) {
                       String  tom_match19_1_1_2_2_1 = null;
                      tom_match19_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_2_2);
                      name2 = ( String ) tom_match19_1_1_2_2_1;
                      
          checkFieldLinearArgs("equals",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match19_pattern4: {
         String  name1 = null;
         OptionList  listOption = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match19_1)) {
           Declaration  tom_match19_1_1 = null;
          tom_match19_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match19_1);
          if(tom_is_fun_sym_GetElementDecl(tom_match19_1_1)) {
             TomTerm  tom_match19_1_1_1 = null;
             TomTerm  tom_match19_1_1_2 = null;
             TomTerm  tom_match19_1_1_3 = null;
            tom_match19_1_1_1 = ( TomTerm ) tom_get_slot_GetElementDecl_kid1(tom_match19_1_1);
            tom_match19_1_1_2 = ( TomTerm ) tom_get_slot_GetElementDecl_kid2(tom_match19_1_1);
            tom_match19_1_1_3 = ( TomTerm ) tom_get_slot_GetElementDecl_kid3(tom_match19_1_1);
            if(tom_is_fun_sym_Variable(tom_match19_1_1_1)) {
               Option  tom_match19_1_1_1_1 = null;
               TomName  tom_match19_1_1_1_2 = null;
               TomType  tom_match19_1_1_1_3 = null;
              tom_match19_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_1);
              tom_match19_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_1);
              tom_match19_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_1);
              if(tom_is_fun_sym_Option(tom_match19_1_1_1_1)) {
                 OptionList  tom_match19_1_1_1_1_1 = null;
                tom_match19_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match19_1_1_1_1);
                listOption = ( OptionList ) tom_match19_1_1_1_1_1;
                if(tom_is_fun_sym_Name(tom_match19_1_1_1_2)) {
                   String  tom_match19_1_1_1_2_1 = null;
                  tom_match19_1_1_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_1_2);
                  name1 = ( String ) tom_match19_1_1_1_2_1;
                  if(tom_is_fun_sym_Variable(tom_match19_1_1_2)) {
                     Option  tom_match19_1_1_2_1 = null;
                     TomName  tom_match19_1_1_2_2 = null;
                     TomType  tom_match19_1_1_2_3 = null;
                    tom_match19_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_2);
                    tom_match19_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_2);
                    tom_match19_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_2);
                    if(tom_is_fun_sym_Name(tom_match19_1_1_2_2)) {
                       String  tom_match19_1_1_2_2_1 = null;
                      tom_match19_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match19_1_1_2_2);
                      name2 = ( String ) tom_match19_1_1_2_2_1;
                       
          checkFieldLinearArgs("get_element",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
}
matchlab_match19_pattern5: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match19_1)) {
           Declaration  tom_match19_1_1 = null;
          tom_match19_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match19_1);
          if(tom_is_fun_sym_GetSizeDecl(tom_match19_1_1)) {
             TomTerm  tom_match19_1_1_1 = null;
             TomTerm  tom_match19_1_1_2 = null;
            tom_match19_1_1_1 = ( TomTerm ) tom_get_slot_GetSizeDecl_kid1(tom_match19_1_1);
            tom_match19_1_1_2 = ( TomTerm ) tom_get_slot_GetSizeDecl_kid2(tom_match19_1_1);
            if(tom_is_fun_sym_Variable(tom_match19_1_1_1)) {
               Option  tom_match19_1_1_1_1 = null;
               TomName  tom_match19_1_1_1_2 = null;
               TomType  tom_match19_1_1_1_3 = null;
              tom_match19_1_1_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match19_1_1_1);
              tom_match19_1_1_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match19_1_1_1);
              tom_match19_1_1_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match19_1_1_1);
              if(tom_is_fun_sym_Option(tom_match19_1_1_1_1)) {
                 OptionList  tom_match19_1_1_1_1_1 = null;
                tom_match19_1_1_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match19_1_1_1_1);
                listOption = ( OptionList ) tom_match19_1_1_1_1_1;
                
          checkField("get_size",findFunctions,listOption);
        
              }
            }
          }
        }
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
      
    {
       Option  tom_match20_1 = null;
      tom_match20_1 = ( Option ) term;
matchlab_match20_pattern1: {
         OptionList  listOption = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match20_1)) {
           Declaration  tom_match20_1_1 = null;
          tom_match20_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match20_1);
          if(tom_is_fun_sym_MakeEmptyArray(tom_match20_1_1)) {
             TomName  tom_match20_1_1_1 = null;
             TomTerm  tom_match20_1_1_2 = null;
             TomTerm  tom_match20_1_1_3 = null;
            tom_match20_1_1_1 = ( TomName ) tom_get_slot_MakeEmptyArray_astName(tom_match20_1_1);
            tom_match20_1_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyArray_varSize(tom_match20_1_1);
            tom_match20_1_1_3 = ( TomTerm ) tom_get_slot_MakeEmptyArray_tlCode(tom_match20_1_1);
            if(tom_is_fun_sym_Variable(tom_match20_1_1_2)) {
               Option  tom_match20_1_1_2_1 = null;
               TomName  tom_match20_1_1_2_2 = null;
               TomType  tom_match20_1_1_2_3 = null;
              tom_match20_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match20_1_1_2);
              tom_match20_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match20_1_1_2);
              tom_match20_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match20_1_1_2);
              if(tom_is_fun_sym_Option(tom_match20_1_1_2_1)) {
                 OptionList  tom_match20_1_1_2_1_1 = null;
                tom_match20_1_1_2_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match20_1_1_2_1);
                listOption = ( OptionList ) tom_match20_1_1_2_1_1;
                 
          checkField("make_empty",findFunctions,listOption);
        
              }
            }
          }
        }
}
matchlab_match20_pattern2: {
         String  name1 = null;
         OptionList  listOption = null;
         String  name2 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match20_1)) {
           Declaration  tom_match20_1_1 = null;
          tom_match20_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match20_1);
          if(tom_is_fun_sym_MakeAddArray(tom_match20_1_1)) {
             TomName  tom_match20_1_1_1 = null;
             TomTerm  tom_match20_1_1_2 = null;
             TomTerm  tom_match20_1_1_3 = null;
             TomTerm  tom_match20_1_1_4 = null;
            tom_match20_1_1_1 = ( TomName ) tom_get_slot_MakeAddArray_astName(tom_match20_1_1);
            tom_match20_1_1_2 = ( TomTerm ) tom_get_slot_MakeAddArray_varElt(tom_match20_1_1);
            tom_match20_1_1_3 = ( TomTerm ) tom_get_slot_MakeAddArray_varList(tom_match20_1_1);
            tom_match20_1_1_4 = ( TomTerm ) tom_get_slot_MakeAddArray_tlCode(tom_match20_1_1);
            if(tom_is_fun_sym_Variable(tom_match20_1_1_2)) {
               Option  tom_match20_1_1_2_1 = null;
               TomName  tom_match20_1_1_2_2 = null;
               TomType  tom_match20_1_1_2_3 = null;
              tom_match20_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match20_1_1_2);
              tom_match20_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match20_1_1_2);
              tom_match20_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match20_1_1_2);
              if(tom_is_fun_sym_Name(tom_match20_1_1_2_2)) {
                 String  tom_match20_1_1_2_2_1 = null;
                tom_match20_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match20_1_1_2_2);
                name2 = ( String ) tom_match20_1_1_2_2_1;
                if(tom_is_fun_sym_Variable(tom_match20_1_1_3)) {
                   Option  tom_match20_1_1_3_1 = null;
                   TomName  tom_match20_1_1_3_2 = null;
                   TomType  tom_match20_1_1_3_3 = null;
                  tom_match20_1_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match20_1_1_3);
                  tom_match20_1_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match20_1_1_3);
                  tom_match20_1_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match20_1_1_3);
                  if(tom_is_fun_sym_Option(tom_match20_1_1_3_1)) {
                     OptionList  tom_match20_1_1_3_1_1 = null;
                    tom_match20_1_1_3_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match20_1_1_3_1);
                    listOption = ( OptionList ) tom_match20_1_1_3_1_1;
                    if(tom_is_fun_sym_Name(tom_match20_1_1_3_2)) {
                       String  tom_match20_1_1_3_2_1 = null;
                      tom_match20_1_1_3_2_1 = ( String ) tom_get_slot_Name_string(tom_match20_1_1_3_2);
                      name1 = ( String ) tom_match20_1_1_3_2_1;
                      
          checkFieldLinearArgs("make_add",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
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
      
    {
       Option  tom_match21_1 = null;
      tom_match21_1 = ( Option ) term;
matchlab_match21_pattern1: {
         String  name = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match21_1)) {
           Declaration  tom_match21_1_1 = null;
          tom_match21_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match21_1);
          if(tom_is_fun_sym_MakeEmptyList(tom_match21_1_1)) {
             TomName  tom_match21_1_1_1 = null;
             TomTerm  tom_match21_1_1_2 = null;
            tom_match21_1_1_1 = ( TomName ) tom_get_slot_MakeEmptyList_astName(tom_match21_1_1);
            tom_match21_1_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyList_tlCode(tom_match21_1_1);
            if(tom_is_fun_sym_Name(tom_match21_1_1_1)) {
               String  tom_match21_1_1_1_1 = null;
              tom_match21_1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match21_1_1_1);
              name = ( String ) tom_match21_1_1_1_1;
              
          if(findFunctions.contains("make_empty")) {
            findFunctions.remove(findFunctions.indexOf("make_empty")); 
          } else {
              /* process as it because we have any informatiom about line */
            messageMacroFunctionRepeated("make_empty' in '" + name + "' of construct '%oplist", " - ");
          }
        
            }
          }
        }
}
matchlab_match21_pattern2: {
         String  name2 = null;
         OptionList  listOption = null;
         String  name1 = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match21_1)) {
           Declaration  tom_match21_1_1 = null;
          tom_match21_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match21_1);
          if(tom_is_fun_sym_MakeAddList(tom_match21_1_1)) {
             TomName  tom_match21_1_1_1 = null;
             TomTerm  tom_match21_1_1_2 = null;
             TomTerm  tom_match21_1_1_3 = null;
             TomTerm  tom_match21_1_1_4 = null;
            tom_match21_1_1_1 = ( TomName ) tom_get_slot_MakeAddList_astName(tom_match21_1_1);
            tom_match21_1_1_2 = ( TomTerm ) tom_get_slot_MakeAddList_varElt(tom_match21_1_1);
            tom_match21_1_1_3 = ( TomTerm ) tom_get_slot_MakeAddList_varList(tom_match21_1_1);
            tom_match21_1_1_4 = ( TomTerm ) tom_get_slot_MakeAddList_tlCode(tom_match21_1_1);
            if(tom_is_fun_sym_Variable(tom_match21_1_1_2)) {
               Option  tom_match21_1_1_2_1 = null;
               TomName  tom_match21_1_1_2_2 = null;
               TomType  tom_match21_1_1_2_3 = null;
              tom_match21_1_1_2_1 = ( Option ) tom_get_slot_Variable_option(tom_match21_1_1_2);
              tom_match21_1_1_2_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match21_1_1_2);
              tom_match21_1_1_2_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match21_1_1_2);
              if(tom_is_fun_sym_Name(tom_match21_1_1_2_2)) {
                 String  tom_match21_1_1_2_2_1 = null;
                tom_match21_1_1_2_2_1 = ( String ) tom_get_slot_Name_string(tom_match21_1_1_2_2);
                name2 = ( String ) tom_match21_1_1_2_2_1;
                if(tom_is_fun_sym_Variable(tom_match21_1_1_3)) {
                   Option  tom_match21_1_1_3_1 = null;
                   TomName  tom_match21_1_1_3_2 = null;
                   TomType  tom_match21_1_1_3_3 = null;
                  tom_match21_1_1_3_1 = ( Option ) tom_get_slot_Variable_option(tom_match21_1_1_3);
                  tom_match21_1_1_3_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match21_1_1_3);
                  tom_match21_1_1_3_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match21_1_1_3);
                  if(tom_is_fun_sym_Option(tom_match21_1_1_3_1)) {
                     OptionList  tom_match21_1_1_3_1_1 = null;
                    tom_match21_1_1_3_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match21_1_1_3_1);
                    listOption = ( OptionList ) tom_match21_1_1_3_1_1;
                    if(tom_is_fun_sym_Name(tom_match21_1_1_3_2)) {
                       String  tom_match21_1_1_3_2_1 = null;
                      tom_match21_1_1_3_2_1 = ( String ) tom_get_slot_Name_string(tom_match21_1_1_3_2);
                      name1 = ( String ) tom_match21_1_1_3_2_1;
                      
          checkFieldLinearArgs("make_add",findFunctions,listOption,name1,name2);
        
                    }
                  }
                }
              }
            }
          }
        }
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
      
    {
       Option  tom_match22_1 = null;
      tom_match22_1 = ( Option ) term;
matchlab_match22_pattern1: {
         String  nameSymbol = null;
         TomList  listM = null;
        if(tom_is_fun_sym_DeclarationToOption(tom_match22_1)) {
           Declaration  tom_match22_1_1 = null;
          tom_match22_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match22_1);
          if(tom_is_fun_sym_MakeDecl(tom_match22_1_1)) {
             TomName  tom_match22_1_1_1 = null;
             TomType  tom_match22_1_1_2 = null;
             TomList  tom_match22_1_1_3 = null;
             TomTerm  tom_match22_1_1_4 = null;
            tom_match22_1_1_1 = ( TomName ) tom_get_slot_MakeDecl_astName(tom_match22_1_1);
            tom_match22_1_1_2 = ( TomType ) tom_get_slot_MakeDecl_astType(tom_match22_1_1);
            tom_match22_1_1_3 = ( TomList ) tom_get_slot_MakeDecl_args(tom_match22_1_1);
            tom_match22_1_1_4 = ( TomTerm ) tom_get_slot_MakeDecl_tlCode(tom_match22_1_1);
            if(tom_is_fun_sym_Name(tom_match22_1_1_1)) {
               String  tom_match22_1_1_1_1 = null;
              tom_match22_1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match22_1_1_1);
              nameSymbol = ( String ) tom_match22_1_1_1_1;
              listM = ( TomList ) tom_match22_1_1_3;
              
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
            
              {
                 TomTerm  tom_match23_1 = null;
                tom_match23_1 = ( TomTerm ) termVar;
matchlab_match23_pattern1: {
                   String  name = null;
                   OptionList  listOption = null;
                  if(tom_is_fun_sym_Variable(tom_match23_1)) {
                     Option  tom_match23_1_1 = null;
                     TomName  tom_match23_1_2 = null;
                     TomType  tom_match23_1_3 = null;
                    tom_match23_1_1 = ( Option ) tom_get_slot_Variable_option(tom_match23_1);
                    tom_match23_1_2 = ( TomName ) tom_get_slot_Variable_astName(tom_match23_1);
                    tom_match23_1_3 = ( TomType ) tom_get_slot_Variable_astType(tom_match23_1);
                    if(tom_is_fun_sym_Option(tom_match23_1_1)) {
                       OptionList  tom_match23_1_1_1 = null;
                      tom_match23_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match23_1_1);
                      listOption = ( OptionList ) tom_match23_1_1_1;
                      if(tom_is_fun_sym_Name(tom_match23_1_2)) {
                         String  tom_match23_1_2_1 = null;
                        tom_match23_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match23_1_2);
                        name = ( String ) tom_match23_1_2_1;
                        
                if(listVar.contains(name)) {
                  String line = findOriginTrackingLine(listOption);
                  messageTwoSameNameVariableError("make",name,line);
                } else {
                  listVar.add(name);
                }
              
                      }
                    }
                  }
}
              }
              
            listM = listM.getTail();
          }
        
            }
          }
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
      
    {
       TomTerm  tom_match24_1 = null;
      tom_match24_1 = ( TomTerm ) ruleTerm;
matchlab_match24_pattern1: {
         TomTerm  rhsTerm = null;
         TomTerm  lhsTerm = null;
        if(tom_is_fun_sym_RewriteRule(tom_match24_1)) {
           TomTerm  tom_match24_1_1 = null;
           TomTerm  tom_match24_1_2 = null;
          tom_match24_1_1 = ( TomTerm ) tom_get_slot_RewriteRule_lhs(tom_match24_1);
          tom_match24_1_2 = ( TomTerm ) tom_get_slot_RewriteRule_rhs(tom_match24_1);
          if(tom_is_fun_sym_Term(tom_match24_1_1)) {
             TomTerm  tom_match24_1_1_1 = null;
            tom_match24_1_1_1 = ( TomTerm ) tom_get_slot_Term_kid1(tom_match24_1_1);
            lhsTerm = ( TomTerm ) tom_match24_1_1_1;
            if(tom_is_fun_sym_Term(tom_match24_1_2)) {
               TomTerm  tom_match24_1_2_1 = null;
              tom_match24_1_2_1 = ( TomTerm ) tom_get_slot_Term_kid1(tom_match24_1_2);
              rhsTerm = ( TomTerm ) tom_match24_1_2_1;
              
          testMakeDefineAppl(lhsTerm, yetStudied);
          testMakeDefineAppl(rhsTerm, yetStudied);
        
            }
          }
        }
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

    
    {
       TomTerm  tom_match25_1 = null;
      tom_match25_1 = ( TomTerm ) ruleTerm;
matchlab_match25_pattern1: {
         TomList  argsList = null;
         String  name = null;
        if(tom_is_fun_sym_Appl(tom_match25_1)) {
           Option  tom_match25_1_1 = null;
           TomName  tom_match25_1_2 = null;
           TomList  tom_match25_1_3 = null;
          tom_match25_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match25_1);
          tom_match25_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match25_1);
          tom_match25_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match25_1);
          if(tom_is_fun_sym_Name(tom_match25_1_2)) {
             String  tom_match25_1_2_1 = null;
            tom_match25_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match25_1_2);
            name = ( String ) tom_match25_1_2_1;
            argsList = ( TomList ) tom_match25_1_3;
            
          // if it is the first time that we have this method, we make tests
        if(!yetStudied.contains(name)) {
          yetStudied.add(name);
            // we test the occurrences of 'make' for symbol 'name'
          TomSymbol info = symbolTable().getSymbol(name);
          if(info!=null) {
            
            {
               TomSymbol  tom_match26_1 = null;
              tom_match26_1 = ( TomSymbol ) info;
matchlab_match26_pattern1: {
                 OptionList  optionList = null;
                if(tom_is_fun_sym_Symbol(tom_match26_1)) {
                   TomName  tom_match26_1_1 = null;
                   TomType  tom_match26_1_2 = null;
                   Option  tom_match26_1_3 = null;
                   TomTerm  tom_match26_1_4 = null;
                  tom_match26_1_1 = ( TomName ) tom_get_slot_Symbol_astName(tom_match26_1);
                  tom_match26_1_2 = ( TomType ) tom_get_slot_Symbol_typesToType(tom_match26_1);
                  tom_match26_1_3 = ( Option ) tom_get_slot_Symbol_option(tom_match26_1);
                  tom_match26_1_4 = ( TomTerm ) tom_get_slot_Symbol_tlCode(tom_match26_1);
                  if(tom_is_fun_sym_Option(tom_match26_1_3)) {
                     OptionList  tom_match26_1_3_1 = null;
                    tom_match26_1_3_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match26_1_3);
                    optionList = ( OptionList ) tom_match26_1_3_1;
                    
                boolean makeFind = false;
                String line = findOriginTrackingLine(name,optionList);
                while(!optionList.isEmptyOptionList() && !makeFind) {
                  Option optionTerm = optionList.getHead();
                  
                    {
                       Option  tom_match27_1 = null;
                      tom_match27_1 = ( Option ) optionTerm;
matchlab_match27_pattern1: {
                         String  name1 = null;
                        if(tom_is_fun_sym_DeclarationToOption(tom_match27_1)) {
                           Declaration  tom_match27_1_1 = null;
                          tom_match27_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match27_1);
                          if(tom_is_fun_sym_MakeDecl(tom_match27_1_1)) {
                             TomName  tom_match27_1_1_1 = null;
                             TomType  tom_match27_1_1_2 = null;
                             TomList  tom_match27_1_1_3 = null;
                             TomTerm  tom_match27_1_1_4 = null;
                            tom_match27_1_1_1 = ( TomName ) tom_get_slot_MakeDecl_astName(tom_match27_1_1);
                            tom_match27_1_1_2 = ( TomType ) tom_get_slot_MakeDecl_astType(tom_match27_1_1);
                            tom_match27_1_1_3 = ( TomList ) tom_get_slot_MakeDecl_args(tom_match27_1_1);
                            tom_match27_1_1_4 = ( TomTerm ) tom_get_slot_MakeDecl_tlCode(tom_match27_1_1);
                            if(tom_is_fun_sym_Name(tom_match27_1_1_1)) {
                               String  tom_match27_1_1_1_1 = null;
                              tom_match27_1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match27_1_1_1);
                              name1 = ( String ) tom_match27_1_1_1_1;
                              
                      if ( name1 == name ) { makeFind = true; }
                    
                            }
                          }
                        }
}
matchlab_match27_pattern2: {
                         String  name1 = null;
                        if(tom_is_fun_sym_DeclarationToOption(tom_match27_1)) {
                           Declaration  tom_match27_1_1 = null;
                          tom_match27_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match27_1);
                          if(tom_is_fun_sym_MakeEmptyArray(tom_match27_1_1)) {
                             TomName  tom_match27_1_1_1 = null;
                             TomTerm  tom_match27_1_1_2 = null;
                             TomTerm  tom_match27_1_1_3 = null;
                            tom_match27_1_1_1 = ( TomName ) tom_get_slot_MakeEmptyArray_astName(tom_match27_1_1);
                            tom_match27_1_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyArray_varSize(tom_match27_1_1);
                            tom_match27_1_1_3 = ( TomTerm ) tom_get_slot_MakeEmptyArray_tlCode(tom_match27_1_1);
                            if(tom_is_fun_sym_Name(tom_match27_1_1_1)) {
                               String  tom_match27_1_1_1_1 = null;
                              tom_match27_1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match27_1_1_1);
                              name1 = ( String ) tom_match27_1_1_1_1;
                              
                      if ( name1 == name ) { makeFind = true; }
                    
                            }
                          }
                        }
}
matchlab_match27_pattern3: {
                         String  name1 = null;
                        if(tom_is_fun_sym_DeclarationToOption(tom_match27_1)) {
                           Declaration  tom_match27_1_1 = null;
                          tom_match27_1_1 = ( Declaration ) tom_get_slot_DeclarationToOption_astDeclaration(tom_match27_1);
                          if(tom_is_fun_sym_MakeEmptyList(tom_match27_1_1)) {
                             TomName  tom_match27_1_1_1 = null;
                             TomTerm  tom_match27_1_1_2 = null;
                            tom_match27_1_1_1 = ( TomName ) tom_get_slot_MakeEmptyList_astName(tom_match27_1_1);
                            tom_match27_1_1_2 = ( TomTerm ) tom_get_slot_MakeEmptyList_tlCode(tom_match27_1_1);
                            if(tom_is_fun_sym_Name(tom_match27_1_1_1)) {
                               String  tom_match27_1_1_1_1 = null;
                              tom_match27_1_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match27_1_1_1);
                              name1 = ( String ) tom_match27_1_1_1_1;
                               
                      if ( name1 == name ) { makeFind = true; }
                    
                            }
                          }
                        }
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
      
    {
       TomSymbol  tom_match28_1 = null;
      tom_match28_1 = ( TomSymbol ) info;
matchlab_match28_pattern1: {
         OptionList  optionList = null;
        if(tom_is_fun_sym_Symbol(tom_match28_1)) {
           TomName  tom_match28_1_1 = null;
           TomType  tom_match28_1_2 = null;
           Option  tom_match28_1_3 = null;
           TomTerm  tom_match28_1_4 = null;
          tom_match28_1_1 = ( TomName ) tom_get_slot_Symbol_astName(tom_match28_1);
          tom_match28_1_2 = ( TomType ) tom_get_slot_Symbol_typesToType(tom_match28_1);
          tom_match28_1_3 = ( Option ) tom_get_slot_Symbol_option(tom_match28_1);
          tom_match28_1_4 = ( TomTerm ) tom_get_slot_Symbol_tlCode(tom_match28_1);
          if(tom_is_fun_sym_Option(tom_match28_1_3)) {
             OptionList  tom_match28_1_3_1 = null;
            tom_match28_1_3_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match28_1_3);
            optionList = ( OptionList ) tom_match28_1_3_1;
            
          String line = findOriginTrackingLine(optionList);
          messageOperatorYetDefined(name,line);
        
          }
        }
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
    
    {
       TomTerm  tom_match29_1 = null;
      tom_match29_1 = ( TomTerm ) subject;
matchlab_match29_pattern1: {
         String  name = null;
         OptionList  list = null;
        if(tom_is_fun_sym_RecordAppl(tom_match29_1)) {
           Option  tom_match29_1_1 = null;
           TomName  tom_match29_1_2 = null;
           TomList  tom_match29_1_3 = null;
          tom_match29_1_1 = ( Option ) tom_get_slot_RecordAppl_option(tom_match29_1);
          tom_match29_1_2 = ( TomName ) tom_get_slot_RecordAppl_astName(tom_match29_1);
          tom_match29_1_3 = ( TomList ) tom_get_slot_RecordAppl_args(tom_match29_1);
          if(tom_is_fun_sym_Option(tom_match29_1_1)) {
             OptionList  tom_match29_1_1_1 = null;
            tom_match29_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match29_1_1);
            list = ( OptionList ) tom_match29_1_1_1;
            if(tom_is_fun_sym_Name(tom_match29_1_2)) {
               String  tom_match29_1_2_1 = null;
              tom_match29_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match29_1_2);
              name = ( String ) tom_match29_1_2_1;
              
        line = findOriginTrackingLine(name,list);
        s += "'"  + name + "'";
      
            }
          }
        }
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
    
    {
       TomTerm  tom_match30_1 = null;
      tom_match30_1 = ( TomTerm ) pairSlotName;
matchlab_match30_pattern1: {
         OptionList  list = null;
        if(tom_is_fun_sym_Pair(tom_match30_1)) {
           TomTerm  tom_match30_1_1 = null;
           TomTerm  tom_match30_1_2 = null;
          tom_match30_1_1 = ( TomTerm ) tom_get_slot_Pair_slotName(tom_match30_1);
          tom_match30_1_2 = ( TomTerm ) tom_get_slot_Pair_appl(tom_match30_1);
          if(tom_is_fun_sym_Appl(tom_match30_1_2)) {
             Option  tom_match30_1_2_1 = null;
             TomName  tom_match30_1_2_2 = null;
             TomList  tom_match30_1_2_3 = null;
            tom_match30_1_2_1 = ( Option ) tom_get_slot_Appl_option(tom_match30_1_2);
            tom_match30_1_2_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match30_1_2);
            tom_match30_1_2_3 = ( TomList ) tom_get_slot_Appl_args(tom_match30_1_2);
            if(tom_is_fun_sym_Option(tom_match30_1_2_1)) {
               OptionList  tom_match30_1_2_1_1 = null;
              tom_match30_1_2_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match30_1_2_1);
              list = ( OptionList ) tom_match30_1_2_1_1;
              
        String line = findOriginTrackingLine(list);
        System.out.println(" *** Slot Name : '"+name+"' - Line : "+line);
      
            }
          }
        }
}
    }
    
    Flags.findErrors = true;
  }

  public void messageSlotNameError(TomTerm pairSlotName, TomList listOfPossibleSlot) {
    ArrayList slotPossible = new ArrayList();
    while( !listOfPossibleSlot.isEmpty() ) {
      TomTerm oneSlot = listOfPossibleSlot.getHead();
      
    {
       TomTerm  tom_match31_1 = null;
      tom_match31_1 = ( TomTerm ) oneSlot;
matchlab_match31_pattern1: {
         String  slotName = null;
        if(tom_is_fun_sym_SlotName(tom_match31_1)) {
           String  tom_match31_1_1 = null;
          tom_match31_1_1 = ( String ) tom_get_slot_SlotName_string(tom_match31_1);
          slotName = ( String ) tom_match31_1_1;
           slotPossible.add(" " + slotName + " "); 
        }
}
    }
    
      listOfPossibleSlot = listOfPossibleSlot.getTail();
    }
    
    {
       TomTerm  tom_match32_1 = null;
      tom_match32_1 = ( TomTerm ) pairSlotName;
matchlab_match32_pattern1: {
         OptionList  list = null;
         String  name = null;
        if(tom_is_fun_sym_Pair(tom_match32_1)) {
           TomTerm  tom_match32_1_1 = null;
           TomTerm  tom_match32_1_2 = null;
          tom_match32_1_1 = ( TomTerm ) tom_get_slot_Pair_slotName(tom_match32_1);
          tom_match32_1_2 = ( TomTerm ) tom_get_slot_Pair_appl(tom_match32_1);
          if(tom_is_fun_sym_SlotName(tom_match32_1_1)) {
             String  tom_match32_1_1_1 = null;
            tom_match32_1_1_1 = ( String ) tom_get_slot_SlotName_string(tom_match32_1_1);
            name = ( String ) tom_match32_1_1_1;
            if(tom_is_fun_sym_Appl(tom_match32_1_2)) {
               Option  tom_match32_1_2_1 = null;
               TomName  tom_match32_1_2_2 = null;
               TomList  tom_match32_1_2_3 = null;
              tom_match32_1_2_1 = ( Option ) tom_get_slot_Appl_option(tom_match32_1_2);
              tom_match32_1_2_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match32_1_2);
              tom_match32_1_2_3 = ( TomList ) tom_get_slot_Appl_args(tom_match32_1_2);
              if(tom_is_fun_sym_Option(tom_match32_1_2_1)) {
                 OptionList  tom_match32_1_2_1_1 = null;
                tom_match32_1_2_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match32_1_2_1);
                list = ( OptionList ) tom_match32_1_2_1_1;
                
        String line = findOriginTrackingLine(list);
        System.out.println("\n"+" *** Slot Name '" + name + "' is strange"+" -  Line : "+line);
      
              }
            }
          }
        }
}
    }
    
    
    System.out.println(" *** Possible Slot Names are : "+slotPossible);

    System.out.println("pairSlotName = " + pairSlotName);
    Flags.findErrors = true;
  }

  public void messageSlotNumberError(TomTerm pairSlotName, int nbSlot, int nbPair) {
    System.out.println("\n"+" *** Bad number of Slot Name");
    
    {
       TomTerm  tom_match33_1 = null;
      tom_match33_1 = ( TomTerm ) pairSlotName;
matchlab_match33_pattern1: {
         OptionList  list = null;
        if(tom_is_fun_sym_Pair(tom_match33_1)) {
           TomTerm  tom_match33_1_1 = null;
           TomTerm  tom_match33_1_2 = null;
          tom_match33_1_1 = ( TomTerm ) tom_get_slot_Pair_slotName(tom_match33_1);
          tom_match33_1_2 = ( TomTerm ) tom_get_slot_Pair_appl(tom_match33_1);
          if(tom_is_fun_sym_Appl(tom_match33_1_2)) {
             Option  tom_match33_1_2_1 = null;
             TomName  tom_match33_1_2_2 = null;
             TomList  tom_match33_1_2_3 = null;
            tom_match33_1_2_1 = ( Option ) tom_get_slot_Appl_option(tom_match33_1_2);
            tom_match33_1_2_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match33_1_2);
            tom_match33_1_2_3 = ( TomList ) tom_get_slot_Appl_args(tom_match33_1_2);
            if(tom_is_fun_sym_Option(tom_match33_1_2_1)) {
               OptionList  tom_match33_1_2_1_1 = null;
              tom_match33_1_2_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match33_1_2_1);
              list = ( OptionList ) tom_match33_1_2_1_1;
              
        String line = findOriginTrackingLine(list);
        System.out.println(" *** "+nbSlot+" are possible, but "+nbPair+" are given"+" - Line : "+line);
      
            }
          }
        }
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
      
    {
       Option  tom_match34_1 = null;
      tom_match34_1 = ( Option ) subject;
matchlab_match34_pattern1: {
         String  line = null;
         String  origName = null;
        if(tom_is_fun_sym_OriginTracking(tom_match34_1)) {
           TomName  tom_match34_1_1 = null;
           TomTerm  tom_match34_1_2 = null;
          tom_match34_1_1 = ( TomName ) tom_get_slot_OriginTracking_astName(tom_match34_1);
          tom_match34_1_2 = ( TomTerm ) tom_get_slot_OriginTracking_line(tom_match34_1);
          if(tom_is_fun_sym_Name(tom_match34_1_1)) {
             String  tom_match34_1_1_1 = null;
            tom_match34_1_1_1 = ( String ) tom_get_slot_Name_string(tom_match34_1_1);
            origName = ( String ) tom_match34_1_1_1;
            if(tom_is_fun_sym_Line(tom_match34_1_2)) {
               String  tom_match34_1_2_1 = null;
              tom_match34_1_2_1 = ( String ) tom_get_slot_Line_string(tom_match34_1_2);
              line = ( String ) tom_match34_1_2_1;
              
          if(name.equals(origName)) {
            return line;
          }
        
            }
          }
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
      
    {
       Option  tom_match35_1 = null;
      tom_match35_1 = ( Option ) subject;
matchlab_match35_pattern1: {
         String  line = null;
        if(tom_is_fun_sym_OriginTracking(tom_match35_1)) {
           TomName  tom_match35_1_1 = null;
           TomTerm  tom_match35_1_2 = null;
          tom_match35_1_1 = ( TomName ) tom_get_slot_OriginTracking_astName(tom_match35_1);
          tom_match35_1_2 = ( TomTerm ) tom_get_slot_OriginTracking_line(tom_match35_1);
          if(tom_is_fun_sym_Line(tom_match35_1_2)) {
             String  tom_match35_1_2_1 = null;
            tom_match35_1_2_1 = ( String ) tom_get_slot_Line_string(tom_match35_1_2);
            line = ( String ) tom_match35_1_2_1;
            
          return line;
        
          }
        }
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
    
    {
       TomTerm  tom_match36_1 = null;
      tom_match36_1 = ( TomTerm ) t;
matchlab_match36_pattern1: {
         TomList  list = null;
        if(tom_is_fun_sym_SubjectList(tom_match36_1)) {
           TomList  tom_match36_1_1 = null;
          tom_match36_1_1 = ( TomList ) tom_get_slot_SubjectList_list(tom_match36_1);
          list = ( TomList ) tom_match36_1_1;
           return list; 
        }
}
matchlab_match36_pattern2: {
         TomList  list = null;
        if(tom_is_fun_sym_PatternList(tom_match36_1)) {
           TomList  tom_match36_1_1 = null;
          tom_match36_1_1 = ( TomList ) tom_get_slot_PatternList_list(tom_match36_1);
          list = ( TomList ) tom_match36_1_1;
           return list; 
        }
}
matchlab_match36_pattern3: {
         TomList  list = null;
        if(tom_is_fun_sym_TermList(tom_match36_1)) {
           TomList  tom_match36_1_1 = null;
          tom_match36_1_1 = ( TomList ) tom_get_slot_TermList_list(tom_match36_1);
          list = ( TomList ) tom_match36_1_1;
           return list; 
        }
}
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
    
    {
       TomTerm  tom_match37_1 = null;
      tom_match37_1 = ( TomTerm ) term;
matchlab_match37_pattern1: {
         OptionList  optionList = null;
         TomList  l = null;
         String  name1 = null;
        if(tom_is_fun_sym_Appl(tom_match37_1)) {
           Option  tom_match37_1_1 = null;
           TomName  tom_match37_1_2 = null;
           TomList  tom_match37_1_3 = null;
          tom_match37_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match37_1);
          tom_match37_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match37_1);
          tom_match37_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match37_1);
          if(tom_is_fun_sym_Option(tom_match37_1_1)) {
             OptionList  tom_match37_1_1_1 = null;
            tom_match37_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match37_1_1);
            optionList = ( OptionList ) tom_match37_1_1_1;
            if(tom_is_fun_sym_Name(tom_match37_1_2)) {
               String  tom_match37_1_2_1 = null;
              tom_match37_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match37_1_2);
              name1 = ( String ) tom_match37_1_2_1;
              l = ( TomList ) tom_match37_1_3;
              
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
          }
        }
}
matchlab_match37_pattern2: {
         return; 
}
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
    
    {
       TomSymbol  tom_match38_1 = null;
      tom_match38_1 = ( TomSymbol ) term;
matchlab_match38_pattern1: {
         TomType  t = null;
        if(tom_is_fun_sym_Symbol(tom_match38_1)) {
           TomName  tom_match38_1_1 = null;
           TomType  tom_match38_1_2 = null;
           Option  tom_match38_1_3 = null;
           TomTerm  tom_match38_1_4 = null;
          tom_match38_1_1 = ( TomName ) tom_get_slot_Symbol_astName(tom_match38_1);
          tom_match38_1_2 = ( TomType ) tom_get_slot_Symbol_typesToType(tom_match38_1);
          tom_match38_1_3 = ( Option ) tom_get_slot_Symbol_option(tom_match38_1);
          tom_match38_1_4 = ( TomTerm ) tom_get_slot_Symbol_tlCode(tom_match38_1);
          if(tom_is_fun_sym_TypesToType(tom_match38_1_2)) {
             TomList  tom_match38_1_2_1 = null;
             TomType  tom_match38_1_2_2 = null;
            tom_match38_1_2_1 = ( TomList ) tom_get_slot_TypesToType_list(tom_match38_1_2);
            tom_match38_1_2_2 = ( TomType ) tom_get_slot_TypesToType_codomain(tom_match38_1_2);
            t = ( TomType ) tom_match38_1_2_2;
             return getTomType(t); 
          }
        }
}
    }
    
    return null;
  }

    /*
      term is an element extract of SymbolTable.
      typeIn returns the type structure of arguments for concerned element.
    */
  public TomList typeIn( TomSymbol term ) {
    
    {
       TomSymbol  tom_match39_1 = null;
      tom_match39_1 = ( TomSymbol ) term;
matchlab_match39_pattern1: {
         TomList  list = null;
        if(tom_is_fun_sym_Symbol(tom_match39_1)) {
           TomName  tom_match39_1_1 = null;
           TomType  tom_match39_1_2 = null;
           Option  tom_match39_1_3 = null;
           TomTerm  tom_match39_1_4 = null;
          tom_match39_1_1 = ( TomName ) tom_get_slot_Symbol_astName(tom_match39_1);
          tom_match39_1_2 = ( TomType ) tom_get_slot_Symbol_typesToType(tom_match39_1);
          tom_match39_1_3 = ( Option ) tom_get_slot_Symbol_option(tom_match39_1);
          tom_match39_1_4 = ( TomTerm ) tom_get_slot_Symbol_tlCode(tom_match39_1);
          if(tom_is_fun_sym_TypesToType(tom_match39_1_2)) {
             TomList  tom_match39_1_2_1 = null;
             TomType  tom_match39_1_2_2 = null;
            tom_match39_1_2_1 = ( TomList ) tom_get_slot_TypesToType_list(tom_match39_1_2);
            tom_match39_1_2_2 = ( TomType ) tom_get_slot_TypesToType_codomain(tom_match39_1_2);
            list = ( TomList ) tom_match39_1_2_1;
             return list; 
          }
        }
}
    }
    
    return empty();
  }

    /*
      term is an element extract of SymbolTable.
      typeOut returns the result type structure of concerned element.
    */
  public TomType typeOut(TomSymbol term ) {
    
    {
       TomSymbol  tom_match40_1 = null;
      tom_match40_1 = ( TomSymbol ) term;
matchlab_match40_pattern1: {
         TomType  t = null;
        if(tom_is_fun_sym_Symbol(tom_match40_1)) {
           TomName  tom_match40_1_1 = null;
           TomType  tom_match40_1_2 = null;
           Option  tom_match40_1_3 = null;
           TomTerm  tom_match40_1_4 = null;
          tom_match40_1_1 = ( TomName ) tom_get_slot_Symbol_astName(tom_match40_1);
          tom_match40_1_2 = ( TomType ) tom_get_slot_Symbol_typesToType(tom_match40_1);
          tom_match40_1_3 = ( Option ) tom_get_slot_Symbol_option(tom_match40_1);
          tom_match40_1_4 = ( TomTerm ) tom_get_slot_Symbol_tlCode(tom_match40_1);
          if(tom_is_fun_sym_TypesToType(tom_match40_1_2)) {
             TomList  tom_match40_1_2_1 = null;
             TomType  tom_match40_1_2_2 = null;
            tom_match40_1_2_1 = ( TomList ) tom_get_slot_TypesToType_list(tom_match40_1_2);
            tom_match40_1_2_2 = ( TomType ) tom_get_slot_TypesToType_codomain(tom_match40_1_2);
            t = ( TomType ) tom_match40_1_2_2;
             return t; 
          }
        }
}
    }
    
    return 
tom_make_EmptyType()    ;
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
    TomType type = 
tom_make_EmptyType()    ;
    
    {
       TomTerm  tom_match41_1 = null;
      tom_match41_1 = ( TomTerm ) inTerm;
matchlab_match41_pattern1: {
         TomList  list = null;
         String  name1 = null;
        if(tom_is_fun_sym_Appl(tom_match41_1)) {
           Option  tom_match41_1_1 = null;
           TomName  tom_match41_1_2 = null;
           TomList  tom_match41_1_3 = null;
          tom_match41_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match41_1);
          tom_match41_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match41_1);
          tom_match41_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match41_1);
          if(tom_is_fun_sym_Name(tom_match41_1_2)) {
             String  tom_match41_1_2_1 = null;
            tom_match41_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match41_1_2);
            name1 = ( String ) tom_match41_1_2_1;
            list = ( TomList ) tom_match41_1_3;
            
        TomList typeList = empty();
        int numArg = 0;
        boolean find = false;
          /*
            we look if 'name' is an argument and we memorize the position of this argument
          */
        for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
          numArg++;
          TomTerm t = l.getHead();
          
            {
               TomTerm  tom_match42_1 = null;
              tom_match42_1 = ( TomTerm ) t;
matchlab_match42_pattern1: {
                 String  name2 = null;
                if(tom_is_fun_sym_Appl(tom_match42_1)) {
                   Option  tom_match42_1_1 = null;
                   TomName  tom_match42_1_2 = null;
                   TomList  tom_match42_1_3 = null;
                  tom_match42_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match42_1);
                  tom_match42_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match42_1);
                  tom_match42_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match42_1);
                  if(tom_is_fun_sym_Name(tom_match42_1_2)) {
                     String  tom_match42_1_2_1 = null;
                    tom_match42_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match42_1_2);
                    name2 = ( String ) tom_match42_1_2_1;
                    
              find = (name2.equals(name));
            
                  }
                }
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
            
            {
               TomType  tom_match43_1 = null;
              tom_match43_1 = ( TomType ) type;
matchlab_match43_pattern1: {
                if(tom_is_fun_sym_EmptyType(tom_match43_1)) {
                   find = false; 
                }
}
            }
            
          }
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
      
    {
       TomTerm  tom_match44_1 = null;
      tom_match44_1 = ( TomTerm ) term;
matchlab_match44_pattern1: {
         String  name1 = null;
         TomList  l = null;
         OptionList  optionList = null;
        if(tom_is_fun_sym_Appl(tom_match44_1)) {
           Option  tom_match44_1_1 = null;
           TomName  tom_match44_1_2 = null;
           TomList  tom_match44_1_3 = null;
          tom_match44_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match44_1);
          tom_match44_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match44_1);
          tom_match44_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match44_1);
          if(tom_is_fun_sym_Option(tom_match44_1_1)) {
             OptionList  tom_match44_1_1_1 = null;
            tom_match44_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match44_1_1);
            optionList = ( OptionList ) tom_match44_1_1_1;
            if(tom_is_fun_sym_Name(tom_match44_1_2)) {
               String  tom_match44_1_2_1 = null;
              tom_match44_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match44_1_2);
              name1 = ( String ) tom_match44_1_2_1;
              l = ( TomList ) tom_match44_1_3;
              
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
              
              {
                 TomTerm  tom_match45_1 = null;
                tom_match45_1 = ( TomTerm ) termL;
matchlab_match45_pattern1: {
                   TomList  l1 = null;
                   OptionList  optionList1 = null;
                   String  name2 = null;
                  if(tom_is_fun_sym_Appl(tom_match45_1)) {
                     Option  tom_match45_1_1 = null;
                     TomName  tom_match45_1_2 = null;
                     TomList  tom_match45_1_3 = null;
                    tom_match45_1_1 = ( Option ) tom_get_slot_Appl_option(tom_match45_1);
                    tom_match45_1_2 = ( TomName ) tom_get_slot_Appl_astName(tom_match45_1);
                    tom_match45_1_3 = ( TomList ) tom_get_slot_Appl_args(tom_match45_1);
                    if(tom_is_fun_sym_Option(tom_match45_1_1)) {
                       OptionList  tom_match45_1_1_1 = null;
                      tom_match45_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match45_1_1);
                      optionList1 = ( OptionList ) tom_match45_1_1_1;
                      if(tom_is_fun_sym_Name(tom_match45_1_2)) {
                         String  tom_match45_1_2_1 = null;
                        tom_match45_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match45_1_2);
                        name2 = ( String ) tom_match45_1_2_1;
                        l1 = ( TomList ) tom_match45_1_3;
                        
                  TomSymbol infoTable1 = symbolTable().getSymbol(name2);
                  if(infoTable1==null && l1.isEmpty()) {
                      /*
                       * if it is an unknown variable,
                       * we memorize it with the needed type
                       */
                    if ( !nameVar.contains(name2) ) {
                      nameVar.add(name2);
                      
                        {
                           TomType  tom_match46_1 = null;
                          tom_match46_1 = ( TomType ) termTypeIn;
matchlab_match46_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_TomTypeAlone(tom_match46_1)) {
                               String  tom_match46_1_1 = null;
                              tom_match46_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match46_1);
                              type = ( String ) tom_match46_1_1;
                               typeVar.add(type); 
                            }
}
matchlab_match46_pattern2: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match46_1)) {
                               TomType  tom_match46_1_1 = null;
                               TomType  tom_match46_1_2 = null;
                              tom_match46_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match46_1);
                              tom_match46_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match46_1);
                              if(tom_is_fun_sym_TomType(tom_match46_1_1)) {
                                 String  tom_match46_1_1_1 = null;
                                tom_match46_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match46_1_1);
                                type = ( String ) tom_match46_1_1_1;
                                 typeVar.add(type); 
                              }
                            }
}
matchlab_match46_pattern3: {
                             typeVar.add("EmptyType"); 
}
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
                        
                        {
                           TomType  tom_match47_1 = null;
                          tom_match47_1 = ( TomType ) termTypeIn;
matchlab_match47_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_TomTypeAlone(tom_match47_1)) {
                               String  tom_match47_1_1 = null;
                              tom_match47_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match47_1);
                              type = ( String ) tom_match47_1_1;
                               typeVar.add(type); 
                            }
}
matchlab_match47_pattern2: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match47_1)) {
                               TomType  tom_match47_1_1 = null;
                               TomType  tom_match47_1_2 = null;
                              tom_match47_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match47_1);
                              tom_match47_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match47_1);
                              if(tom_is_fun_sym_TomType(tom_match47_1_1)) {
                                 String  tom_match47_1_1_1 = null;
                                tom_match47_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match47_1_1);
                                type = ( String ) tom_match47_1_1_1;
                                 typeVar.add(type); 
                              }
                            }
}
matchlab_match47_pattern3: {
                             typeVar.add("EmptyType"); 
}
                        }
                        
                      } else  { 
                        
                        {
                           TomType  tom_match48_1 = null;
                          tom_match48_1 = ( TomType ) termTypeIn;
matchlab_match48_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_TomTypeAlone(tom_match48_1)) {
                               String  tom_match48_1_1 = null;
                              tom_match48_1_1 = ( String ) tom_get_slot_TomTypeAlone_string(tom_match48_1);
                              type = ( String ) tom_match48_1_1;
                               
                            if ( !typeMem.equals(type) ) {
                              String line = findOriginTrackingLine(name2,optionList1);
                              messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                          
                            }
}
matchlab_match48_pattern2: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match48_1)) {
                               TomType  tom_match48_1_1 = null;
                               TomType  tom_match48_1_2 = null;
                              tom_match48_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match48_1);
                              tom_match48_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match48_1);
                              if(tom_is_fun_sym_TomType(tom_match48_1_1)) {
                                 String  tom_match48_1_1_1 = null;
                                tom_match48_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match48_1_1);
                                type = ( String ) tom_match48_1_1_1;
                                
                            if ( !typeMem.equals(type) ) {
                              String line = findOriginTrackingLine(name2,optionList1);
                              messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                          
                              }
                            }
}
                        }
                        
                      }
                    }
                  }
                
                      }
                    }
                  }
}
matchlab_match45_pattern2: {
                   OptionList  optionList1 = null;
                   TomType  t = null;
                   String  name2 = null;
                  if(tom_is_fun_sym_VariableStar(tom_match45_1)) {
                     Option  tom_match45_1_1 = null;
                     TomName  tom_match45_1_2 = null;
                     TomType  tom_match45_1_3 = null;
                    tom_match45_1_1 = ( Option ) tom_get_slot_VariableStar_option(tom_match45_1);
                    tom_match45_1_2 = ( TomName ) tom_get_slot_VariableStar_astName(tom_match45_1);
                    tom_match45_1_3 = ( TomType ) tom_get_slot_VariableStar_astType(tom_match45_1);
                    if(tom_is_fun_sym_Option(tom_match45_1_1)) {
                       OptionList  tom_match45_1_1_1 = null;
                      tom_match45_1_1_1 = ( OptionList ) tom_get_slot_Option_optionList(tom_match45_1_1);
                      optionList1 = ( OptionList ) tom_match45_1_1_1;
                      if(tom_is_fun_sym_Name(tom_match45_1_2)) {
                         String  tom_match45_1_2_1 = null;
                        tom_match45_1_2_1 = ( String ) tom_get_slot_Name_string(tom_match45_1_2);
                        name2 = ( String ) tom_match45_1_2_1;
                        t = ( TomType ) tom_match45_1_3;
                        
                    /*
                     * VariableStars have elready created : so we must consider these
                     */
                  TomList infoTypeIn = typeIn(infoTable);
                  TomType theType = infoTypeIn.getHead().getAstType();
                  if ( !nameVar.contains(name2) ) {
                    nameVar.add(name2);
                    
                        {
                           TomType  tom_match49_1 = null;
                          tom_match49_1 = ( TomType ) theType;
matchlab_match49_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match49_1)) {
                               TomType  tom_match49_1_1 = null;
                               TomType  tom_match49_1_2 = null;
                              tom_match49_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match49_1);
                              tom_match49_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match49_1);
                              if(tom_is_fun_sym_TomType(tom_match49_1_1)) {
                                 String  tom_match49_1_1_1 = null;
                                tom_match49_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match49_1_1);
                                type = ( String ) tom_match49_1_1_1;
                                 typeVar.add(type); 
                              }
                            }
}
matchlab_match49_pattern2: {
                             typeVar.add("EmptyType"); 
}
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
                      
                        {
                           TomType  tom_match50_1 = null;
                          tom_match50_1 = ( TomType ) theType;
matchlab_match50_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match50_1)) {
                               TomType  tom_match50_1_1 = null;
                               TomType  tom_match50_1_2 = null;
                              tom_match50_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match50_1);
                              tom_match50_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match50_1);
                              if(tom_is_fun_sym_TomType(tom_match50_1_1)) {
                                 String  tom_match50_1_1_1 = null;
                                tom_match50_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match50_1_1);
                                type = ( String ) tom_match50_1_1_1;
                                 typeVar.add(type); 
                              }
                            }
}
matchlab_match50_pattern2: {
                             typeVar.add("EmptyType"); 
}
                        }
                        
                    } else  { 
                      
                        {
                           TomType  tom_match51_1 = null;
                          tom_match51_1 = ( TomType ) theType;
matchlab_match51_pattern1: {
                             String  type = null;
                            if(tom_is_fun_sym_Type(tom_match51_1)) {
                               TomType  tom_match51_1_1 = null;
                               TomType  tom_match51_1_2 = null;
                              tom_match51_1_1 = ( TomType ) tom_get_slot_Type_tomType(tom_match51_1);
                              tom_match51_1_2 = ( TomType ) tom_get_slot_Type_tlType(tom_match51_1);
                              if(tom_is_fun_sym_TomType(tom_match51_1_1)) {
                                 String  tom_match51_1_1_1 = null;
                                tom_match51_1_1_1 = ( String ) tom_get_slot_TomType_string(tom_match51_1_1);
                                type = ( String ) tom_match51_1_1_1;
                                
                          if ( !typeMem.equals(type) ) {
                            String line = findOriginTrackingLine(name2,optionList1);
                            messageRepeatedVariableError(name2, type,(String)typeVar.get(i), line); }
                        
                              }
                            }
}
                        }
                        
                    }
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
    
    }
  }
}



