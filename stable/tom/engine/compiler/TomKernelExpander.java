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

*/

package jtom.compiler;
  
import java.util.*;
import java.io.*;

import aterm.*;

import jtom.*;
import jtom.tools.*;
import jtom.adt.*;
import jtom.runtime.*;

public class TomKernelExpander extends TomBase {

  public TomKernelExpander(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
                                                                                                                                                                         
// ------------------------------------------------------------
  
    /*
     * replace Name by Symbol
     * replace Name by Variable
     */
  private Replace2 replaceVariable = new Replace2() { 
      public ATerm apply(ATerm subject, Object arg1) {
        TomTerm contextSubject = (TomTerm)arg1;

          //System.out.println("expandVariable:\n\t" + subject );
        
        if(!(subject instanceof TomTerm)) {
            //debugPrintln("expandVariable not a tomTerm: " );
            //System.out.println("expandVariable not a tomTerm:\n\t" + subject );
          if(subject instanceof TomType) {
             {  TomType tom_match1_1 = null; tom_match1_1 = ( TomType) subject;matchlab_match1_pattern1: {  String tomType = null; if(tom_is_fun_sym_TomTypeAlone(tom_match1_1)) {  String tom_match1_1_1 = null; tom_match1_1_1 = ( String) tom_get_slot_TomTypeAlone_string(tom_match1_1); tomType = ( String) tom_match1_1_1;
 
                TomType type = getType(tomType);
                if(type != null) {
                  return type;
                } else {
                  return subject; // useful for TomTypeAlone("unknown type")
                }
               }} }
 
          }
          return traversal().genericTraversal(subject,this,contextSubject);
        }

          //System.out.println("expandVariable is a tomTerm:\n\t" + subject );
        
         {  TomTerm tom_match2_1 = null;  TomTerm tom_match2_2 = null; tom_match2_1 = ( TomTerm) contextSubject; tom_match2_2 = ( TomTerm) subject;matchlab_match2_pattern1: {  TomTerm appl = null;  String strName = null;  OptionList optionList = null;  TomType tomType = null;  TomType type = null;  TomType glType = null;  TomName name = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match2_1)) {  TomType tom_match2_1_1 = null; tom_match2_1_1 = ( TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match2_1); if(tom_is_fun_sym_Type(tom_match2_1_1)) {  TomType tom_match2_1_1_1 = null;  TomType tom_match2_1_1_2 = null; tom_match2_1_1_1 = ( TomType) tom_get_slot_Type_tomType(tom_match2_1_1); tom_match2_1_1_2 = ( TomType) tom_get_slot_Type_tlType(tom_match2_1_1); type = ( TomType) tom_match2_1_1; tomType = ( TomType) tom_match2_1_1_1; glType = ( TomType) tom_match2_1_1_2; if(tom_is_fun_sym_Appl(tom_match2_2)) {  Option tom_match2_2_1 = null;  TomName tom_match2_2_2 = null;  TomList tom_match2_2_3 = null; tom_match2_2_1 = ( Option) tom_get_slot_Appl_option(tom_match2_2); tom_match2_2_2 = ( TomName) tom_get_slot_Appl_astName(tom_match2_2); tom_match2_2_3 = ( TomList) tom_get_slot_Appl_args(tom_match2_2); appl = ( TomTerm) tom_match2_2; if(tom_is_fun_sym_Option(tom_match2_2_1)) {  OptionList tom_match2_2_1_1 = null; tom_match2_2_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_2_1); optionList = ( OptionList) tom_match2_2_1_1; if(tom_is_fun_sym_Name(tom_match2_2_2)) {  String tom_match2_2_2_1 = null; tom_match2_2_2_1 = ( String) tom_get_slot_Name_string(tom_match2_2_2); name = ( TomName) tom_match2_2_2; strName = ( String) tom_match2_2_2_1; if(tom_is_fun_sym_Empty(tom_match2_2_3)) {
 
              //debugPrintln("expandVariable.1: Type(" + tomType + "," + glType + ")");
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type, orgTrack)) ;
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return tom_make_Appl(option,name,tom_make_Empty()) ;
            } else {
              statistics().numberVariablesDetected++;
              return tom_make_Variable(option,name,type) ;
            }
           } } } } } }}matchlab_match2_pattern2: {  TomName name1 = null;  TomTerm appl = null;  OptionList optionList = null;  String strName = null;  TomName name = null;  TomType type1 = null;  Option option1 = null; if(tom_is_fun_sym_Variable(tom_match2_1)) {  Option tom_match2_1_1 = null;  TomName tom_match2_1_2 = null;  TomType tom_match2_1_3 = null; tom_match2_1_1 = ( Option) tom_get_slot_Variable_option(tom_match2_1); tom_match2_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match2_1); tom_match2_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match2_1); option1 = ( Option) tom_match2_1_1; name1 = ( TomName) tom_match2_1_2; type1 = ( TomType) tom_match2_1_3; if(tom_is_fun_sym_Appl(tom_match2_2)) {  Option tom_match2_2_1 = null;  TomName tom_match2_2_2 = null;  TomList tom_match2_2_3 = null; tom_match2_2_1 = ( Option) tom_get_slot_Appl_option(tom_match2_2); tom_match2_2_2 = ( TomName) tom_get_slot_Appl_astName(tom_match2_2); tom_match2_2_3 = ( TomList) tom_get_slot_Appl_args(tom_match2_2); appl = ( TomTerm) tom_match2_2; if(tom_is_fun_sym_Option(tom_match2_2_1)) {  OptionList tom_match2_2_1_1 = null; tom_match2_2_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_2_1); optionList = ( OptionList) tom_match2_2_1_1; if(tom_is_fun_sym_Name(tom_match2_2_2)) {  String tom_match2_2_2_1 = null; tom_match2_2_2_1 = ( String) tom_get_slot_Name_string(tom_match2_2_2); name = ( TomName) tom_match2_2_2; strName = ( String) tom_match2_2_2_1; if(tom_is_fun_sym_Empty(tom_match2_2_3)) {

 
              //debugPrintln("expandVariable.3: Variable(" + option1 + "," + name1 + "," + type1 + ")");
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type1, orgTrack)) ;
              // under a match construct
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return tom_make_Appl(option,name,tom_make_Empty()) ;
            } else {
              statistics().numberVariablesDetected++;
              return tom_make_Variable(option,name,type1) ;
            }
           } } } } }}matchlab_match2_pattern3: {  TomType type = null;  TomType tomType = null;  TomTerm p = null;  OptionList optionList = null;  TomType glType = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match2_1)) {  TomType tom_match2_1_1 = null; tom_match2_1_1 = ( TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match2_1); if(tom_is_fun_sym_Type(tom_match2_1_1)) {  TomType tom_match2_1_1_1 = null;  TomType tom_match2_1_1_2 = null; tom_match2_1_1_1 = ( TomType) tom_get_slot_Type_tomType(tom_match2_1_1); tom_match2_1_1_2 = ( TomType) tom_get_slot_Type_tlType(tom_match2_1_1); type = ( TomType) tom_match2_1_1; tomType = ( TomType) tom_match2_1_1_1; glType = ( TomType) tom_match2_1_1_2; if(tom_is_fun_sym_Placeholder(tom_match2_2)) {  Option tom_match2_2_1 = null; tom_match2_2_1 = ( Option) tom_get_slot_Placeholder_option(tom_match2_2); p = ( TomTerm) tom_match2_2; if(tom_is_fun_sym_Option(tom_match2_2_1)) {  OptionList tom_match2_2_1_1 = null; tom_match2_2_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_2_1); optionList = ( OptionList) tom_match2_2_1_1;

 
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type, orgTrack)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type) ;
           } } } }}matchlab_match2_pattern4: {  TomName name1 = null;  Option option1 = null;  TomTerm p = null;  OptionList optionList = null;  TomType type1 = null; if(tom_is_fun_sym_Variable(tom_match2_1)) {  Option tom_match2_1_1 = null;  TomName tom_match2_1_2 = null;  TomType tom_match2_1_3 = null; tom_match2_1_1 = ( Option) tom_get_slot_Variable_option(tom_match2_1); tom_match2_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match2_1); tom_match2_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match2_1); option1 = ( Option) tom_match2_1_1; name1 = ( TomName) tom_match2_1_2; type1 = ( TomType) tom_match2_1_3; if(tom_is_fun_sym_Placeholder(tom_match2_2)) {  Option tom_match2_2_1 = null; tom_match2_2_1 = ( Option) tom_get_slot_Placeholder_option(tom_match2_2); p = ( TomTerm) tom_match2_2; if(tom_is_fun_sym_Option(tom_match2_2_1)) {  OptionList tom_match2_2_1_1 = null; tom_match2_2_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_2_1); optionList = ( OptionList) tom_match2_2_1_1;

 
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type1, orgTrack)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type1) ;
           } } }}matchlab_match2_pattern5: {  TomTerm appl = null;  TomList l = null;  TomName name = null;  String tomName = null;  OptionList optionList = null;  TomTerm context = null; context = ( TomTerm) tom_match2_1; if(tom_is_fun_sym_Appl(tom_match2_2)) {  Option tom_match2_2_1 = null;  TomName tom_match2_2_2 = null;  TomList tom_match2_2_3 = null; tom_match2_2_1 = ( Option) tom_get_slot_Appl_option(tom_match2_2); tom_match2_2_2 = ( TomName) tom_get_slot_Appl_astName(tom_match2_2); tom_match2_2_3 = ( TomList) tom_get_slot_Appl_args(tom_match2_2); appl = ( TomTerm) tom_match2_2; if(tom_is_fun_sym_Option(tom_match2_2_1)) {  OptionList tom_match2_2_1_1 = null; tom_match2_2_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_2_1); optionList = ( OptionList) tom_match2_2_1_1; if(tom_is_fun_sym_Name(tom_match2_2_2)) {  String tom_match2_2_2_1 = null; tom_match2_2_2_1 = ( String) tom_get_slot_Name_string(tom_match2_2_2); name = ( TomName) tom_match2_2_2; tomName = ( String) tom_match2_2_2_1; l = ( TomList) tom_match2_2_3;

 
              //debugPrintln("expandVariable.6: Appl(Name(" + tomName + ")," + l + ")");
              // create a  symbol
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomList subterm = expandVariableList(tomSymbol, l);
                //System.out.println("***** expandVariable.6: expandVariableList = " + subterm);
              Option orgTrack = findOriginTracking(optionList);
              Option option = tom_make_Option(replaceAnnotedName(optionList, getSymbolCodomain(tomSymbol), orgTrack)) ;
              return tom_make_Appl(option,name,subterm) ;
            }
           } } }}matchlab_match2_pattern6: {  String strName = null;  Option option = null;  String tomType = null;  TomTerm context = null; context = ( TomTerm) tom_match2_1; if(tom_is_fun_sym_Variable(tom_match2_2)) {  Option tom_match2_2_1 = null;  TomName tom_match2_2_2 = null;  TomType tom_match2_2_3 = null; tom_match2_2_1 = ( Option) tom_get_slot_Variable_option(tom_match2_2); tom_match2_2_2 = ( TomName) tom_get_slot_Variable_astName(tom_match2_2); tom_match2_2_3 = ( TomType) tom_get_slot_Variable_astType(tom_match2_2); option = ( Option) tom_match2_2_1; if(tom_is_fun_sym_Name(tom_match2_2_2)) {  String tom_match2_2_2_1 = null; tom_match2_2_2_1 = ( String) tom_get_slot_Name_string(tom_match2_2_2); strName = ( String) tom_match2_2_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match2_2_3)) {  String tom_match2_2_3_1 = null; tom_match2_2_3_1 = ( String) tom_get_slot_TomTypeAlone_string(tom_match2_2_3); tomType = ( String) tom_match2_2_3_1;

 
              // create a variable
            TomType localType = getType(tomType);
            return tom_make_Variable(option,tom_make_Name(strName),localType) ;
           } } }}matchlab_match2_pattern7: {  String tomType = null;  String strName = null;  TomTerm context = null; context = ( TomTerm) tom_match2_1; if(tom_is_fun_sym_TLVar(tom_match2_2)) {  String tom_match2_2_1 = null;  TomType tom_match2_2_2 = null; tom_match2_2_1 = ( String) tom_get_slot_TLVar_strName(tom_match2_2); tom_match2_2_2 = ( TomType) tom_get_slot_TLVar_astType(tom_match2_2); strName = ( String) tom_match2_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match2_2_2)) {  String tom_match2_2_2_1 = null; tom_match2_2_2_1 = ( String) tom_get_slot_TomTypeAlone_string(tom_match2_2_2); tomType = ( String) tom_match2_2_2_1;

 
              //debugPrintln("expandVariable.8: TLVar(" + strName + "," + tomType + ")");
              // create a variable: its type is ensured by checker
            TomType localType = getType(tomType);
            Option option = ast().makeOption();
            return tom_make_Variable(option,tom_make_Name(strName),localType) ;
           } }}matchlab_match2_pattern8: {  TomList l1 = null;  TomList subjectList = null; if(tom_is_fun_sym_SubjectList(tom_match2_1)) {  TomList tom_match2_1_1 = null; tom_match2_1_1 = ( TomList) tom_get_slot_SubjectList_list(tom_match2_1); l1 = ( TomList) tom_match2_1_1; if(tom_is_fun_sym_TermList(tom_match2_2)) {  TomList tom_match2_2_1 = null; tom_match2_2_1 = ( TomList) tom_get_slot_TermList_list(tom_match2_2); subjectList = ( TomList) tom_match2_2_1;

 
            //debugPrintln("expandVariable.9: TermList(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(expandVariable(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return tom_make_TermList(ast().makeList(list)) ;
           } }}matchlab_match2_pattern9: {  TomTerm context = null;  TomTerm patternList = null;  TomTerm tomSubjectList = null;  Option option = null; context = ( TomTerm) tom_match2_1; if(tom_is_fun_sym_Match(tom_match2_2)) {  TomTerm tom_match2_2_1 = null;  TomTerm tom_match2_2_2 = null;  Option tom_match2_2_3 = null; tom_match2_2_1 = ( TomTerm) tom_get_slot_Match_subjectList(tom_match2_2); tom_match2_2_2 = ( TomTerm) tom_get_slot_Match_patternList(tom_match2_2); tom_match2_2_3 = ( Option) tom_get_slot_Match_option(tom_match2_2); tomSubjectList = ( TomTerm) tom_match2_2_1; patternList = ( TomTerm) tom_match2_2_2; option = ( Option) tom_match2_2_3;

 
            //debugPrintln("expandVariable.10: Match(" + tomSubjectList + "," + patternList + ")");
            TomTerm newSubjectList = expandVariable(context,tomSubjectList);
            TomTerm newPatternList = expandVariable(newSubjectList,patternList);
            return tom_make_Match(newSubjectList,newPatternList,option) ;
           }}matchlab_match2_pattern10: {  TomTerm context = null;  TomTerm t = null; context = ( TomTerm) tom_match2_1; t = ( TomTerm) tom_match2_2;


 
            //debugPrintln("expandVariable.11 default: " );
              //System.out.println("expandVariable default:\n\t" + subject );
            return traversal().genericTraversal(subject,this,contextSubject);
          } }
  // end match
      } // end apply
    }; // end new

  public TomTerm expandVariable(TomTerm contextSubject, TomTerm subject) {
    return (TomTerm) replaceVariable.apply(subject,contextSubject); 
  }

  public TomList expandVariableList(TomSymbol subject, TomList subjectList) {
    //%variable
     {  TomSymbol tom_match3_1 = null; tom_match3_1 = ( TomSymbol) subject;matchlab_match3_pattern1: {  TomList typeList = null;  TomType codomainType = null;  TomSymbol symb = null; if(tom_is_fun_sym_Symbol(tom_match3_1)) {  TomName tom_match3_1_1 = null;  TomType tom_match3_1_2 = null;  SlotList tom_match3_1_3 = null;  Option tom_match3_1_4 = null;  TargetLanguage tom_match3_1_5 = null; tom_match3_1_1 = ( TomName) tom_get_slot_Symbol_astName(tom_match3_1); tom_match3_1_2 = ( TomType) tom_get_slot_Symbol_typesToType(tom_match3_1); tom_match3_1_3 = ( SlotList) tom_get_slot_Symbol_slotList(tom_match3_1); tom_match3_1_4 = ( Option) tom_get_slot_Symbol_option(tom_match3_1); tom_match3_1_5 = ( TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match3_1); symb = ( TomSymbol) tom_match3_1; if(tom_is_fun_sym_TypesToType(tom_match3_1_2)) {  TomList tom_match3_1_2_1 = null;  TomType tom_match3_1_2_2 = null; tom_match3_1_2_1 = ( TomList) tom_get_slot_TypesToType_list(tom_match3_1_2); tom_match3_1_2_2 = ( TomType) tom_get_slot_TypesToType_codomain(tom_match3_1_2); typeList = ( TomList) tom_match3_1_2_1; codomainType = ( TomType) tom_match3_1_2_2;
 
        //debugPrintln("expandVariableList.1: " + subjectList);
          
          // process a list of subterms and a list of types
        TomList result = null;
        ArrayList list = new ArrayList();
        if(isListOperator(symb) || isArrayOperator(symb)) {
          /*
           * TODO:
           * when the symbol is an associative operator,
           * the signature has the form: List conc( Element* )
           * the list of types is reduced to the singleton { Element }
           *
           * consider a pattern: conc(E1*,x,E2*,y,E3*)
           *  assign the type "Element" to each subterm: x and y
           *  assign the type "List" to each subtermList: E1*,E2* and E3*
           */

          //System.out.println("listOperator: " + symb);
          
          TomTerm domainType = typeList.getHead();
          while(!subjectList.isEmpty()) {
            TomTerm subterm = subjectList.getHead();

            matchBlock: {
               {  TomTerm tom_match4_1 = null; tom_match4_1 = ( TomTerm) subterm;matchlab_match4_pattern1: {  Option voption = null;  TomName name = null; if(tom_is_fun_sym_VariableStar(tom_match4_1)) {  Option tom_match4_1_1 = null;  TomName tom_match4_1_2 = null;  TomType tom_match4_1_3 = null; tom_match4_1_1 = ( Option) tom_get_slot_VariableStar_option(tom_match4_1); tom_match4_1_2 = ( TomName) tom_get_slot_VariableStar_astName(tom_match4_1); tom_match4_1_3 = ( TomType) tom_get_slot_VariableStar_astType(tom_match4_1); voption = ( Option) tom_match4_1_1; name = ( TomName) tom_match4_1_2;
 
                  list.add(tom_make_VariableStar(voption,name,codomainType) );
                    //System.out.println("*** break: " + subterm);
                  break matchBlock;
                 }}matchlab_match4_pattern2: {

 
                  list.add(expandVariable(domainType, subterm));
                  break matchBlock;
                } }
 
            }
            subjectList = subjectList.getTail();
          }
        } else {
          while(!subjectList.isEmpty()) {
            list.add(expandVariable(typeList.getHead(), subjectList.getHead()));
            subjectList = subjectList.getTail();
            typeList    = typeList.getTail();
          }
        }
       
        result = ast().makeList(list);
        return result;
       } }}matchlab_match3_pattern2: {

 
        System.out.println("expandVariableList: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return null;
  }

    /*
     * updateSymbol is called after a first syntax expansion phase
     * this phase updates the symbolTable according to the typeTable
     * this is performed by recursively traversing each symbol
     * each TomTypeAlone is replace by the corresponding TomType
     */
  public void updateSymbolTable() {
    Iterator it = symbolTable().keySymbolIterator();
    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = null;
      TomSymbol tomSymbol = getSymbol(tomName);
      tomSymbol = expandVariable(emptyContext,tom_make_TomSymbolToTomTerm(tomSymbol) ).getAstSymbol();
      symbolTable().putSymbol(tomName,tomSymbol);
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = symbolTable().getType(tomName);
    return tomType;
  }


  private OptionList replaceAnnotedName(OptionList subjectList, TomType type, Option orgTrack) {
    //%variable
     {  OptionList tom_match5_1 = null; tom_match5_1 = ( OptionList) subjectList;matchlab_match5_pattern1: { if(tom_is_fun_sym_EmptyOptionList(tom_match5_1)) {
  return subjectList;  }}matchlab_match5_pattern2: {  OptionList l = null;  TomName name = null; if(tom_is_fun_sym_ConsOptionList(tom_match5_1)) {  Option tom_match5_1_1 = null;  OptionList tom_match5_1_2 = null; tom_match5_1_1 = ( Option) tom_get_slot_ConsOptionList_head(tom_match5_1); tom_match5_1_2 = ( OptionList) tom_get_slot_ConsOptionList_tail(tom_match5_1); if(tom_is_fun_sym_TomNameToOption(tom_match5_1_1)) {  TomName tom_match5_1_1_1 = null; tom_match5_1_1_1 = ( TomName) tom_get_slot_TomNameToOption_astName(tom_match5_1_1); if(tom_is_fun_sym_Name(tom_match5_1_1_1)) {  String tom_match5_1_1_1_1 = null; tom_match5_1_1_1_1 = ( String) tom_get_slot_Name_string(tom_match5_1_1_1); name = ( TomName) tom_match5_1_1_1; l = ( OptionList) tom_match5_1_2;
 
        return tom_make_ConsOptionList(tom_make_TomTermToOption(tom_make_Variable(ast().makeOption(orgTrack),name,type)),replaceAnnotedName(l, type, orgTrack))

 ;
       } } }}matchlab_match5_pattern3: {  OptionList l = null;  Option t = null; if(tom_is_fun_sym_ConsOptionList(tom_match5_1)) {  Option tom_match5_1_1 = null;  OptionList tom_match5_1_2 = null; tom_match5_1_1 = ( Option) tom_get_slot_ConsOptionList_head(tom_match5_1); tom_match5_1_2 = ( OptionList) tom_get_slot_ConsOptionList_tail(tom_match5_1); t = ( Option) tom_match5_1_1; l = ( OptionList) tom_match5_1_2;
 
        return tom_make_ConsOptionList(t,replaceAnnotedName(l, type, orgTrack)) ;
       }} }
 
    return null;
  }
  
} // Class TomKernelExpander
