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
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;

import jtom.exception.*;
import jtom.adt.*;
import jtom.runtime.*;

public class TomExpander extends TomBase {

  public TomExpander(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
                                                                                                                                                                 
// ------------------------------------------------------------

    /*
     * The 'expandTomSyntax' phase replaces each 'RecordAppl'
     * by its expanded term form:
     *   - unused slots a replaced by placeholders
     *   - BackQuoteTerm are compiled
     */
  
  public TomTerm expandTomSyntax(TomTerm subject) {
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm subject) {
          if(subject instanceof TomTerm) {
             { TomTerm tom_match1_1 = null; tom_match1_1 = (TomTerm) subject;matchlab_match1_pattern1: { TomTerm t = null; if(tom_is_fun_sym_BackQuoteTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; Option tom_match1_1_2 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_BackQuoteTerm_term(tom_match1_1); tom_match1_1_2 = (Option) tom_get_slot_BackQuoteTerm_option(tom_match1_1); t = (TomTerm) tom_match1_1_1;
 
                return expandBackQuoteTerm(t);
               }}matchlab_match1_pattern2: { String tomName = null; TomList args = null; Option option = null; if(tom_is_fun_sym_RecordAppl(tom_match1_1)) { Option tom_match1_1_1 = null; TomName tom_match1_1_2 = null; TomList tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match1_1); tom_match1_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match1_1); tom_match1_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match1_1); option = (Option) tom_match1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_2)) { String tom_match1_1_2_1 = null; tom_match1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_2); tomName = (String) tom_match1_1_2_1; args = (TomList) tom_match1_1_3;

 
                return expandRecordAppl(option,tomName,args);
               } }}matchlab_match1_pattern3: {

 
                return traversal().genericTraversal(subject,this);
              } }
  // end match
          } else {
            return traversal().genericTraversal(subject,this);
          }
        } // end apply
      }; // end new
    
    return (TomTerm) replace.apply(subject); 
  }

  private TomTerm expandRecordAppl(Option option, String tomName, TomList args) {
    TomSymbol tomSymbol = getSymbol(tomName);
    SlotList slotList = tomSymbol.getSlotList();
    TomList subtermList = empty();
      // For each slotName (from tomSymbol)
    while(!slotList.isEmptySlotList()) {
      TomName slotName = slotList.getHeadSlotList().getSlotName();
        //debugPrintln("\tslotName  = " + slotName);
      TomList pairList = args;
      TomTerm newSubterm = null;
      if(!slotName.isEmptyName()) {
          // look for a same name (from record)
        whileBlock: {
          while(newSubterm==null && !pairList.isEmpty()) {
            TomTerm pairSlotName = pairList.getHead();
             { TomName tom_match2_1 = null; TomTerm tom_match2_2 = null; tom_match2_1 = (TomName) slotName; tom_match2_2 = (TomTerm) pairSlotName;matchlab_match2_pattern1: { String tom_renamedvar_name_1 = null; TomTerm slotSubterm = null; String name = null; if(tom_is_fun_sym_Name(tom_match2_1)) { String tom_match2_1_1 = null; tom_match2_1_1 = (String) tom_get_slot_Name_string(tom_match2_1); tom_renamedvar_name_1 = (String) tom_match2_1_1; if(tom_is_fun_sym_PairSlotAppl(tom_match2_2)) { TomName tom_match2_2_1 = null; TomTerm tom_match2_2_2 = null; tom_match2_2_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match2_2); tom_match2_2_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match2_2); if(tom_is_fun_sym_Name(tom_match2_2_1)) { String tom_match2_2_1_1 = null; tom_match2_2_1_1 = (String) tom_get_slot_Name_string(tom_match2_2_1); name = (String) tom_match2_2_1_1; slotSubterm = (TomTerm) tom_match2_2_2; if(tom_terms_equal_String(name, tom_renamedvar_name_1) &&  true ) {
 
                  // bingo
                statistics().numberSlotsExpanded++;
                newSubterm = expandTomSyntax(slotSubterm);
                break whileBlock;
               } } } }} }
 
            pairList = pairList.getTail();
          }
        } // end whileBlock
      }
      
      if(newSubterm == null) {
        newSubterm = tom_make_Placeholder(ast().makeOption()) ;
      }
      subtermList = append(newSubterm,subtermList);
      slotList = slotList.getTailSlotList();
    }
    
    return tom_make_Appl(option,tom_make_Name(tomName),subtermList) ;
  }
  
  private TomTerm expandBackQuoteTerm(TomTerm t) {
    Replace1 replaceSymbol = new Replace1() {
        public ATerm apply(ATerm t) {
          if(t instanceof TomTerm) {
             { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) t;matchlab_match3_pattern1: { TomList l = null; OptionList optionList = null; TomName name = null; String tomName = null; if(tom_is_fun_sym_Appl(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomList tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_Appl_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match3_1); tom_match3_1_3 = (TomList) tom_get_slot_Appl_args(tom_match3_1); if(tom_is_fun_sym_Option(tom_match3_1_1)) { OptionList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match3_1_1); optionList = (OptionList) tom_match3_1_1_1; if(tom_is_fun_sym_Name(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_Name_string(tom_match3_1_2); name = (TomName) tom_match3_1_2; tomName = (String) tom_match3_1_2_1; l = (TomList) tom_match3_1_3;
 
                TomSymbol tomSymbol = getSymbol(tomName);
                TomList args  = (TomList) traversal().genericTraversal(l,this);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return tom_make_BuildList(name,args) ;
                  } else if(isArrayOperator(tomSymbol)) {
                    return tom_make_BuildArray(name,args) ;
                  } else if(isIntegerOperator(tomSymbol)) {
                    return tom_make_BuildBuiltin(name) ;
                  } else {
                    return tom_make_BuildTerm(name,args) ;
                  }
                } else if(args.isEmpty() && !hasConstructor(optionList)) {
                  return tom_make_BuildVariable(name) ;
                } else {
                  return tom_make_FunctionCall(name,args) ;
                }
               } } }}matchlab_match3_pattern2: { TomTerm t1 = null; TomTerm t2 = null; if(tom_is_fun_sym_DotTerm(tom_match3_1)) { TomTerm tom_match3_1_1 = null; TomTerm tom_match3_1_2 = null; tom_match3_1_1 = (TomTerm) tom_get_slot_DotTerm_kid1(tom_match3_1); tom_match3_1_2 = (TomTerm) tom_get_slot_DotTerm_kid2(tom_match3_1); t1 = (TomTerm) tom_match3_1_1; t2 = (TomTerm) tom_match3_1_2;

 
                TomTerm tt1 = (TomTerm) this.apply(t1);
                TomTerm tt2 = (TomTerm) this.apply(t2);
                return tom_make_DotTerm(tt1,tt2) ;
               }}matchlab_match3_pattern3: { TomTerm var = null; TomName name = null; if(tom_is_fun_sym_VariableStar(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomType tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match3_1); tom_match3_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match3_1); var = (TomTerm) tom_match3_1; name = (TomName) tom_match3_1_2;

 
                  //return `BuildVariableStar(name); 
                  return var;
               }}matchlab_match3_pattern4: {

 
                System.out.println("replaceSymbol: strange case: '" + t + "'");
                System.exit(1);
              } }
 
            return t;
          } else {
            return traversal().genericTraversal(t,this);
          }
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }
  
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
             { TomType tom_match4_1 = null; tom_match4_1 = (TomType) subject;matchlab_match4_pattern1: { String tomType = null; if(tom_is_fun_sym_TomTypeAlone(tom_match4_1)) { String tom_match4_1_1 = null; tom_match4_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match4_1); tomType = (String) tom_match4_1_1;
 
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
        
         { TomTerm tom_match5_1 = null; TomTerm tom_match5_2 = null; tom_match5_1 = (TomTerm) contextSubject; tom_match5_2 = (TomTerm) subject;matchlab_match5_pattern1: { TomType tomType = null; TomType type = null; TomType glType = null; TomName name = null; String strName = null; OptionList optionList = null; TomTerm appl = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match5_1)) { TomType tom_match5_1_1 = null; tom_match5_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match5_1); if(tom_is_fun_sym_Type(tom_match5_1_1)) { TomType tom_match5_1_1_1 = null; TomType tom_match5_1_1_2 = null; tom_match5_1_1_1 = (TomType) tom_get_slot_Type_tomType(tom_match5_1_1); tom_match5_1_1_2 = (TomType) tom_get_slot_Type_tlType(tom_match5_1_1); type = (TomType) tom_match5_1_1; tomType = (TomType) tom_match5_1_1_1; glType = (TomType) tom_match5_1_1_2; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); appl = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_Empty(tom_match5_2_3)) {
 
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
           } } } } } }}matchlab_match5_pattern2: { TomTerm appl = null; Option option1 = null; String strName = null; OptionList optionList = null; TomName name = null; TomType type1 = null; TomName name1 = null; if(tom_is_fun_sym_Variable(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomType tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_1); option1 = (Option) tom_match5_1_1; name1 = (TomName) tom_match5_1_2; type1 = (TomType) tom_match5_1_3; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); appl = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_Empty(tom_match5_2_3)) {

 
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
           } } } } }}matchlab_match5_pattern3: { TomType glType = null; TomTerm p = null; OptionList optionList = null; TomType tomType = null; TomType type = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match5_1)) { TomType tom_match5_1_1 = null; tom_match5_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match5_1); if(tom_is_fun_sym_Type(tom_match5_1_1)) { TomType tom_match5_1_1_1 = null; TomType tom_match5_1_1_2 = null; tom_match5_1_1_1 = (TomType) tom_get_slot_Type_tomType(tom_match5_1_1); tom_match5_1_1_2 = (TomType) tom_get_slot_Type_tlType(tom_match5_1_1); type = (TomType) tom_match5_1_1; tomType = (TomType) tom_match5_1_1_1; glType = (TomType) tom_match5_1_1_2; if(tom_is_fun_sym_Placeholder(tom_match5_2)) { Option tom_match5_2_1 = null; tom_match5_2_1 = (Option) tom_get_slot_Placeholder_option(tom_match5_2); p = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1;

 
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type, orgTrack)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type) ;
           } } } }}matchlab_match5_pattern4: { OptionList optionList = null; TomName name1 = null; TomType type1 = null; Option option1 = null; TomTerm p = null; if(tom_is_fun_sym_Variable(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomType tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_1); option1 = (Option) tom_match5_1_1; name1 = (TomName) tom_match5_1_2; type1 = (TomType) tom_match5_1_3; if(tom_is_fun_sym_Placeholder(tom_match5_2)) { Option tom_match5_2_1 = null; tom_match5_2_1 = (Option) tom_get_slot_Placeholder_option(tom_match5_2); p = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1;

 
            Option orgTrack = findOriginTracking(optionList);
            Option option = tom_make_Option(replaceAnnotedName(optionList, type1, orgTrack)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type1) ;
           } } }}matchlab_match5_pattern5: { TomTerm context = null; TomTerm appl = null; OptionList optionList = null; TomName name = null; String tomName = null; TomList l = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); appl = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; tomName = (String) tom_match5_2_2_1; l = (TomList) tom_match5_2_3;

 
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
           } } }}matchlab_match5_pattern6: { String strName = null; String tomType = null; Option option = null; TomTerm context = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Variable(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomType tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Variable_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_2); tom_match5_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_2); option = (Option) tom_match5_2_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match5_2_3)) { String tom_match5_2_3_1 = null; tom_match5_2_3_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match5_2_3); tomType = (String) tom_match5_2_3_1;

 
              // create a variable
            TomType localType = getType(tomType);
            return tom_make_Variable(option,tom_make_Name(strName),localType) ;
           } } }}matchlab_match5_pattern7: { String tomType = null; TomTerm context = null; String strName = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_TLVar(tom_match5_2)) { String tom_match5_2_1 = null; TomType tom_match5_2_2 = null; tom_match5_2_1 = (String) tom_get_slot_TLVar_strName(tom_match5_2); tom_match5_2_2 = (TomType) tom_get_slot_TLVar_astType(tom_match5_2); strName = (String) tom_match5_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match5_2_2); tomType = (String) tom_match5_2_2_1;

 
              //debugPrintln("expandVariable.8: TLVar(" + strName + "," + tomType + ")");
              // create a variable: its type is ensured by checker
            TomType localType = getType(tomType);
            Option option = ast().makeOption();
            return tom_make_Variable(option,tom_make_Name(strName),localType) ;
           } }}matchlab_match5_pattern8: { TomList subjectList = null; TomList l1 = null; if(tom_is_fun_sym_SubjectList(tom_match5_1)) { TomList tom_match5_1_1 = null; tom_match5_1_1 = (TomList) tom_get_slot_SubjectList_list(tom_match5_1); l1 = (TomList) tom_match5_1_1; if(tom_is_fun_sym_TermList(tom_match5_2)) { TomList tom_match5_2_1 = null; tom_match5_2_1 = (TomList) tom_get_slot_TermList_list(tom_match5_2); subjectList = (TomList) tom_match5_2_1;

 
            //debugPrintln("expandVariable.9: TermList(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(expandVariable(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return tom_make_TermList(ast().makeList(list)) ;
           } }}matchlab_match5_pattern9: { TomTerm context = null; TomTerm tomSubjectList = null; Option option = null; TomTerm patternList = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Match(tom_match5_2)) { Option tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; TomTerm tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Match_option(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_Match_subjectList(tom_match5_2); tom_match5_2_3 = (TomTerm) tom_get_slot_Match_patternList(tom_match5_2); option = (Option) tom_match5_2_1; tomSubjectList = (TomTerm) tom_match5_2_2; patternList = (TomTerm) tom_match5_2_3;

 
            //debugPrintln("expandVariable.10: Match(" + tomSubjectList + "," + patternList + ")");
            TomTerm newSubjectList = expandVariable(context,tomSubjectList);
            TomTerm newPatternList = expandVariable(newSubjectList,patternList);
            return tom_make_Match(option,newSubjectList,newPatternList) ;
           }}matchlab_match5_pattern10: { TomTerm rhs = null; TomList varList = null; String rhsName = null; TomTerm lhs = null; String lhsName = null; if(tom_is_fun_sym_Tom(tom_match5_1)) { TomList tom_match5_1_1 = null; tom_match5_1_1 = (TomList) tom_get_slot_Tom_list(tom_match5_1); varList = (TomList) tom_match5_1_1; if(tom_is_fun_sym_MatchingCondition(tom_match5_2)) { TomTerm tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; tom_match5_2_1 = (TomTerm) tom_get_slot_MatchingCondition_lhs(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_MatchingCondition_rhs(tom_match5_2); if(tom_is_fun_sym_Appl(tom_match5_2_1)) { Option tom_match5_2_1_1 = null; TomName tom_match5_2_1_2 = null; TomList tom_match5_2_1_3 = null; tom_match5_2_1_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_1); tom_match5_2_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_1); tom_match5_2_1_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_1); lhs = (TomTerm) tom_match5_2_1; if(tom_is_fun_sym_Name(tom_match5_2_1_2)) { String tom_match5_2_1_2_1 = null; tom_match5_2_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_1_2); lhsName = (String) tom_match5_2_1_2_1; if(tom_is_fun_sym_Appl(tom_match5_2_2)) { Option tom_match5_2_2_1 = null; TomName tom_match5_2_2_2 = null; TomList tom_match5_2_2_3 = null; tom_match5_2_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_2); tom_match5_2_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_2); tom_match5_2_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_2); rhs = (TomTerm) tom_match5_2_2; if(tom_is_fun_sym_Name(tom_match5_2_2_2)) { String tom_match5_2_2_2_1 = null; tom_match5_2_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2_2); rhsName = (String) tom_match5_2_2_2_1;


 
            TomSymbol lhsSymbol = getSymbol(lhsName);
            TomSymbol rhsSymbol = getSymbol(rhsName);
            TomType type;

            if(lhsSymbol != null) {
              type = getSymbolCodomain(lhsSymbol);
            } else if(rhsSymbol != null) {
              type = getSymbolCodomain(rhsSymbol);
            } else {
                // both lhs and rhs are variables
                // since lhs is a fresh variable, we look for rhs
              type = getTypeFromVariableList(tom_make_Name(rhsName) ,varList);
            }

            TomTerm newLhs = expandVariable(tom_make_TomTypeToTomTerm(type), lhs) ;
            TomTerm newRhs = expandVariable(tom_make_TomTypeToTomTerm(type), rhs) ;
            return tom_make_MatchingCondition(newLhs,newRhs) ;
           } } } } } }}matchlab_match5_pattern11: { String lhsName = null; String rhsName = null; TomTerm rhs = null; TomList varList = null; TomTerm lhs = null; if(tom_is_fun_sym_Tom(tom_match5_1)) { TomList tom_match5_1_1 = null; tom_match5_1_1 = (TomList) tom_get_slot_Tom_list(tom_match5_1); varList = (TomList) tom_match5_1_1; if(tom_is_fun_sym_EqualityCondition(tom_match5_2)) { TomTerm tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; tom_match5_2_1 = (TomTerm) tom_get_slot_EqualityCondition_lhs(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_EqualityCondition_rhs(tom_match5_2); if(tom_is_fun_sym_Appl(tom_match5_2_1)) { Option tom_match5_2_1_1 = null; TomName tom_match5_2_1_2 = null; TomList tom_match5_2_1_3 = null; tom_match5_2_1_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_1); tom_match5_2_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_1); tom_match5_2_1_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_1); lhs = (TomTerm) tom_match5_2_1; if(tom_is_fun_sym_Name(tom_match5_2_1_2)) { String tom_match5_2_1_2_1 = null; tom_match5_2_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_1_2); lhsName = (String) tom_match5_2_1_2_1; if(tom_is_fun_sym_Appl(tom_match5_2_2)) { Option tom_match5_2_2_1 = null; TomName tom_match5_2_2_2 = null; TomList tom_match5_2_2_3 = null; tom_match5_2_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_2); tom_match5_2_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_2); tom_match5_2_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_2); rhs = (TomTerm) tom_match5_2_2; if(tom_is_fun_sym_Name(tom_match5_2_2_2)) { String tom_match5_2_2_2_1 = null; tom_match5_2_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2_2); rhsName = (String) tom_match5_2_2_2_1;


 
            TomSymbol lhsSymbol = getSymbol(lhsName);
            TomSymbol rhsSymbol = getSymbol(rhsName);
            TomType type;

            if(lhsSymbol != null) {
              type = getSymbolCodomain(lhsSymbol);
            } else if(rhsSymbol != null) {
              type = getSymbolCodomain(rhsSymbol);
            } else {
                // both lhs and rhs are variables
              type = getTypeFromVariableList(tom_make_Name(lhsName) ,varList);
            }

              //System.out.println("EqualityCondition type = " + type);

            TomTerm newLhs = expandVariable(tom_make_TomTypeToTomTerm(type), lhs) ;
            TomTerm newRhs = expandVariable(tom_make_TomTypeToTomTerm(type), rhs) ;

              //System.out.println("lhs    = " + lhs);
              //System.out.println("newLhs = " + newLhs);

            return tom_make_EqualityCondition(newLhs,newRhs) ;
           } } } } } }}matchlab_match5_pattern12: { TomList l = null; TomTerm rhs = null; TomTerm lhs = null; String tomName = null; TomList condList = null; TomTerm context = null; Option orgTrack = null; OptionList optionList = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_RewriteRule(tom_match5_2)) { TomTerm tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; TomList tom_match5_2_3 = null; Option tom_match5_2_4 = null; tom_match5_2_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_RewriteRule_condList(tom_match5_2); tom_match5_2_4 = (Option) tom_get_slot_RewriteRule_orgTrack(tom_match5_2); if(tom_is_fun_sym_Term(tom_match5_2_1)) { TomTerm tom_match5_2_1_1 = null; tom_match5_2_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match5_2_1); if(tom_is_fun_sym_Appl(tom_match5_2_1_1)) { Option tom_match5_2_1_1_1 = null; TomName tom_match5_2_1_1_2 = null; TomList tom_match5_2_1_1_3 = null; tom_match5_2_1_1_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_1_1); tom_match5_2_1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_1_1); tom_match5_2_1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_1_1); lhs = (TomTerm) tom_match5_2_1_1; if(tom_is_fun_sym_Option(tom_match5_2_1_1_1)) { OptionList tom_match5_2_1_1_1_1 = null; tom_match5_2_1_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1_1_1); optionList = (OptionList) tom_match5_2_1_1_1_1; if(tom_is_fun_sym_Name(tom_match5_2_1_1_2)) { String tom_match5_2_1_1_2_1 = null; tom_match5_2_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_1_1_2); tomName = (String) tom_match5_2_1_1_2_1; l = (TomList) tom_match5_2_1_1_3; if(tom_is_fun_sym_Term(tom_match5_2_2)) { TomTerm tom_match5_2_2_1 = null; tom_match5_2_2_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match5_2_2); rhs = (TomTerm) tom_match5_2_2_1; condList = (TomList) tom_match5_2_3; orgTrack = (Option) tom_match5_2_4;




  
              //debugPrintln("expandVariable.13: Rule(" + lhs + "," + rhs + ")");
            TomSymbol tomSymbol = getSymbol(tomName);
            TomType symbolType = getSymbolCodomain(tomSymbol);
            TomTerm newLhs = tom_make_Term(expandVariable(context, lhs)) ;
            TomTerm newRhs = tom_make_Term(expandVariable(tom_make_TomTypeToTomTerm(symbolType), rhs)) ;

              // build the list of variables that occur in the lhs
            HashSet set = new HashSet();
            collectVariable(set,newLhs);
            TomList varList = ast().makeList(set);
            TomList newCondList = empty();
            while(!condList.isEmpty()) {
              TomTerm cond = condList.getHead();
              TomTerm newCond = expandVariable(tom_make_Tom(varList) ,cond);
              newCondList = append(newCond,newCondList);
              collectVariable(set,newCond);
              varList = ast().makeList(set);
              condList = condList.getTail();
            }

            return tom_make_RewriteRule(newLhs,newRhs,newCondList,orgTrack) ;
           } } } } } }}matchlab_match5_pattern13: { TomTerm t = null; TomTerm context = null; context = (TomTerm) tom_match5_1; t = (TomTerm) tom_match5_2;


 
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
     { TomSymbol tom_match6_1 = null; tom_match6_1 = (TomSymbol) subject;matchlab_match6_pattern1: { TomType codomainType = null; TomList typeList = null; TomSymbol symb = null; if(tom_is_fun_sym_Symbol(tom_match6_1)) { TomName tom_match6_1_1 = null; TomType tom_match6_1_2 = null; SlotList tom_match6_1_3 = null; Option tom_match6_1_4 = null; TargetLanguage tom_match6_1_5 = null; tom_match6_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match6_1); tom_match6_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match6_1); tom_match6_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match6_1); tom_match6_1_4 = (Option) tom_get_slot_Symbol_option(tom_match6_1); tom_match6_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match6_1); symb = (TomSymbol) tom_match6_1; if(tom_is_fun_sym_TypesToType(tom_match6_1_2)) { TomList tom_match6_1_2_1 = null; TomType tom_match6_1_2_2 = null; tom_match6_1_2_1 = (TomList) tom_get_slot_TypesToType_list(tom_match6_1_2); tom_match6_1_2_2 = (TomType) tom_get_slot_TypesToType_codomain(tom_match6_1_2); typeList = (TomList) tom_match6_1_2_1; codomainType = (TomType) tom_match6_1_2_2;
 
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
               { TomTerm tom_match7_1 = null; tom_match7_1 = (TomTerm) subterm;matchlab_match7_pattern1: { Option voption = null; TomName name = null; if(tom_is_fun_sym_VariableStar(tom_match7_1)) { Option tom_match7_1_1 = null; TomName tom_match7_1_2 = null; TomType tom_match7_1_3 = null; tom_match7_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match7_1); tom_match7_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match7_1); tom_match7_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match7_1); voption = (Option) tom_match7_1_1; name = (TomName) tom_match7_1_2;
 
                  list.add(tom_make_VariableStar(voption,name,codomainType) );
                    //System.out.println("*** break: " + subterm);
                  break matchBlock;
                 }}matchlab_match7_pattern2: {

 
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
       } }}matchlab_match6_pattern2: {

 
        System.out.println("expandVariableList: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return null;
  }

    /*
     * updateSymbol is called after the expansion phase
     * this phase updates the symbolTable according to the typeTable
     * this is performed by recursively traversing each symbol
     * each TomTypeAlone is replace by the corresponding TomType
     */
  public void updateSymbol() {
    Iterator it = symbolTable().keySymbolIterator();
    while(it.hasNext()) {
      String tomName = (String)it.next();
      TomTerm emptyContext = null;
      TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
      tomSymbol = expandVariable(emptyContext,tom_make_TomSymbolToTomTerm(tomSymbol) ).getAstSymbol();
      symbolTable().putSymbol(tomName,tomSymbol);
    }
  }
  
  private TomSymbol getSymbol(String tomName) {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    return tomSymbol;
  }

  private TomType getType(String tomName) {
    TomType tomType = symbolTable().getType(tomName);
    return tomType;
  }

  private TomType getTypeFromVariableList(TomName name, TomList list) {

      //System.out.println("name = " + name);
      //System.out.println("list = " + list);
    
     { TomName tom_match8_1 = null; TomList tom_match8_2 = null; tom_match8_1 = (TomName) name; tom_match8_2 = (TomList) list;matchlab_match8_pattern1: { if(tom_is_fun_sym_Empty(tom_match8_2)) {
 
        System.out.println("getTypeFromVariableList. Stange case '" + name + "' not found");
        System.exit(1);
       }}matchlab_match8_pattern2: { TomList tail = null; TomType type = null; TomName tom_renamedvar_varName_1 = null; TomName varName = null; tom_renamedvar_varName_1 = (TomName) tom_match8_1; if(tom_is_fun_sym_Cons(tom_match8_2)) { TomTerm tom_match8_2_1 = null; TomList tom_match8_2_2 = null; tom_match8_2_1 = (TomTerm) tom_get_slot_Cons_head(tom_match8_2); tom_match8_2_2 = (TomList) tom_get_slot_Cons_tail(tom_match8_2); if(tom_is_fun_sym_Variable(tom_match8_2_1)) { Option tom_match8_2_1_1 = null; TomName tom_match8_2_1_2 = null; TomType tom_match8_2_1_3 = null; tom_match8_2_1_1 = (Option) tom_get_slot_Variable_option(tom_match8_2_1); tom_match8_2_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_2_1); tom_match8_2_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_2_1); varName = (TomName) tom_match8_2_1_2; if(tom_is_fun_sym_Type(tom_match8_2_1_3)) { TomType tom_match8_2_1_3_1 = null; TomType tom_match8_2_1_3_2 = null; tom_match8_2_1_3_1 = (TomType) tom_get_slot_Type_tomType(tom_match8_2_1_3); tom_match8_2_1_3_2 = (TomType) tom_get_slot_Type_tlType(tom_match8_2_1_3); type = (TomType) tom_match8_2_1_3; tail = (TomList) tom_match8_2_2; if(tom_terms_equal_TomName(varName, tom_renamedvar_varName_1) &&  true ) {

  return type;  } } } }}matchlab_match8_pattern3: { TomList tail = null; TomType type = null; TomName tom_renamedvar_varName_1 = null; TomName varName = null; tom_renamedvar_varName_1 = (TomName) tom_match8_1; if(tom_is_fun_sym_Cons(tom_match8_2)) { TomTerm tom_match8_2_1 = null; TomList tom_match8_2_2 = null; tom_match8_2_1 = (TomTerm) tom_get_slot_Cons_head(tom_match8_2); tom_match8_2_2 = (TomList) tom_get_slot_Cons_tail(tom_match8_2); if(tom_is_fun_sym_VariableStar(tom_match8_2_1)) { Option tom_match8_2_1_1 = null; TomName tom_match8_2_1_2 = null; TomType tom_match8_2_1_3 = null; tom_match8_2_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match8_2_1); tom_match8_2_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match8_2_1); tom_match8_2_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match8_2_1); varName = (TomName) tom_match8_2_1_2; if(tom_is_fun_sym_Type(tom_match8_2_1_3)) { TomType tom_match8_2_1_3_1 = null; TomType tom_match8_2_1_3_2 = null; tom_match8_2_1_3_1 = (TomType) tom_get_slot_Type_tomType(tom_match8_2_1_3); tom_match8_2_1_3_2 = (TomType) tom_get_slot_Type_tlType(tom_match8_2_1_3); type = (TomType) tom_match8_2_1_3; tail = (TomList) tom_match8_2_2; if(tom_terms_equal_TomName(varName, tom_renamedvar_varName_1) &&  true ) {
  return type;  } } } }}matchlab_match8_pattern4: { TomList tail = null; TomTerm t = null; if(tom_is_fun_sym_Cons(tom_match8_2)) { TomTerm tom_match8_2_1 = null; TomList tom_match8_2_2 = null; tom_match8_2_1 = (TomTerm) tom_get_slot_Cons_head(tom_match8_2); tom_match8_2_2 = (TomList) tom_get_slot_Cons_tail(tom_match8_2); t = (TomTerm) tom_match8_2_1; tail = (TomList) tom_match8_2_2;
  return getTypeFromVariableList(name,tail);  }} }

 
    return null;
  }
  
    /*
     * TODO: X1*,name@Name(_),X2* -> { return name; }
     */
  private TomName getAnnotedName(TomList subjectList) {
    //%variable
    while(!subjectList.isEmpty()) {
      TomTerm subject = subjectList.getHead();
       { TomTerm tom_match9_1 = null; tom_match9_1 = (TomTerm) subject;matchlab_match9_pattern1: { TomName name = null; if(tom_is_fun_sym_TomNameToTomTerm(tom_match9_1)) { TomName tom_match9_1_1 = null; tom_match9_1_1 = (TomName) tom_get_slot_TomNameToTomTerm_astName(tom_match9_1); if(tom_is_fun_sym_Name(tom_match9_1_1)) { String tom_match9_1_1_1 = null; tom_match9_1_1_1 = (String) tom_get_slot_Name_string(tom_match9_1_1); name = (TomName) tom_match9_1_1;
  return name;  } }} }
 
      subjectList = subjectList.getTail();
    }
    return null;
  }

  private OptionList replaceAnnotedName(OptionList subjectList, TomType type, Option orgTrack) {
    //%variable
     { OptionList tom_match10_1 = null; tom_match10_1 = (OptionList) subjectList;matchlab_match10_pattern1: { if(tom_is_fun_sym_EmptyOptionList(tom_match10_1)) {
  return subjectList;  }}matchlab_match10_pattern2: { OptionList l = null; TomName name = null; if(tom_is_fun_sym_ConsOptionList(tom_match10_1)) { Option tom_match10_1_1 = null; OptionList tom_match10_1_2 = null; tom_match10_1_1 = (Option) tom_get_slot_ConsOptionList_head(tom_match10_1); tom_match10_1_2 = (OptionList) tom_get_slot_ConsOptionList_tail(tom_match10_1); if(tom_is_fun_sym_TomNameToOption(tom_match10_1_1)) { TomName tom_match10_1_1_1 = null; tom_match10_1_1_1 = (TomName) tom_get_slot_TomNameToOption_astName(tom_match10_1_1); if(tom_is_fun_sym_Name(tom_match10_1_1_1)) { String tom_match10_1_1_1_1 = null; tom_match10_1_1_1_1 = (String) tom_get_slot_Name_string(tom_match10_1_1_1); name = (TomName) tom_match10_1_1_1; l = (OptionList) tom_match10_1_2;
 
        return tom_make_ConsOptionList(tom_make_TomTermToOption(tom_make_Variable(ast().makeOption(orgTrack),name,type)),replaceAnnotedName(l, type, orgTrack))

 ;
       } } }}matchlab_match10_pattern3: { OptionList l = null; Option t = null; if(tom_is_fun_sym_ConsOptionList(tom_match10_1)) { Option tom_match10_1_1 = null; OptionList tom_match10_1_2 = null; tom_match10_1_1 = (Option) tom_get_slot_ConsOptionList_head(tom_match10_1); tom_match10_1_2 = (OptionList) tom_get_slot_ConsOptionList_tail(tom_match10_1); t = (Option) tom_match10_1_1; l = (OptionList) tom_match10_1_2;
 
        return tom_make_ConsOptionList(t,replaceAnnotedName(l, type, orgTrack)) ;
       }} }
 
    return null;
  }
  
} // Class Tom Expander
