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

package jtom.checker;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.tools.*;

import jtom.exception.*;
import jtom.adt.*;

public class TomChecker extends TomBase {

  private Option optionMatchTypeVariable;
  
  public TomChecker(jtom.TomEnvironment environment) {
	super(environment);
  }

  protected interface ReplaceContext {
    ATerm apply(ATerm context,ATerm t) throws TomException;
  }

    /*
     * Apply a function to each element of a list
     */
  protected ATermList genericMapContext(ATerm context, ATermList subject, ReplaceContext replace) {
    ATermList res = subject;
    try {
      if(!subject.isEmpty()) {
        ATerm term = replace.apply(context,subject.getFirst());
        ATermList list = genericMapContext(context,subject.getNext(),replace);
        res = list.insert(term);
      }
    } catch(Exception e) {
      System.out.println("genericMapContext error: " + e);
      System.exit(0);
    }
    return res;
  }

    /*
     * Apply a function to each subterm of a term
     */
  protected ATermAppl genericMaptermContext(ATerm context, ATermAppl subject, ReplaceContext replace) {
    try {
      ATerm newSubterm;
      for(int i=0 ; i<subject.getArity() ; i++) {
        newSubterm = replace.apply(context,subject.getArgument(i));
        if(newSubterm != subject.getArgument(i)) {
          subject = subject.setArgument(newSubterm,i);
        }
      }
    } catch(Exception e) {
      System.out.println("genericMaptermContext error: " + e);
      e.printStackTrace();
      System.exit(0);
    }

    return subject;
  }

    /*
     * Traverse a subject and replace
     */
  private int counter = 0;
  protected ATerm genericTraversalContext(ATerm context, ATerm subject, ReplaceContext replace) {
    ATerm res = subject;
    try {
      if(subject instanceof ATermAppl) {
        res = genericMaptermContext(context,(ATermAppl) subject,replace);
      } else if(subject instanceof ATermList) {
        res = genericMapContext(context,(ATermList) subject,replace);
      }
    } catch(Exception e) {
      System.out.println("genericTraversalContext error: " + e);
      System.exit(0);
    }
    return res;
  } 
  

// ------------------------------------------------------------
                                                                                                                                                              
// ------------------------------------------------------------

    /*
     * The 'expand' phase replaces a 'RecordAppl' by its classical term form:
     * The unused slots a replaced by placeholders
     */
  
  public TomTerm expand(TomTerm subject) throws TomException {
    Replace replace = new Replace() { 
        public ATerm apply(ATerm subject) throws TomException {
          if(subject instanceof TomTerm) {
             { TomTerm tom_match1_1 = null; tom_match1_1 = (TomTerm) subject;matchlab_match1_pattern1: { TomTerm t = null; if(tom_is_fun_sym_BackQuoteTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; Option tom_match1_1_2 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_BackQuoteTerm_term(tom_match1_1); tom_match1_1_2 = (Option) tom_get_slot_BackQuoteTerm_option(tom_match1_1); t = (TomTerm) tom_match1_1_1;
 
                return expandBackQuoteTerm(t);
               }}matchlab_match1_pattern2: { String tomName = null; TomList args = null; Option option = null; if(tom_is_fun_sym_RecordAppl(tom_match1_1)) { Option tom_match1_1_1 = null; TomName tom_match1_1_2 = null; TomList tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match1_1); tom_match1_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match1_1); tom_match1_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match1_1); option = (Option) tom_match1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_2)) { String tom_match1_1_2_1 = null; tom_match1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_2); tomName = (String) tom_match1_1_2_1; args = (TomList) tom_match1_1_3;

 
                return expandRecordAppl(option,tomName,args);
               } }}matchlab_match1_pattern3: {

 
                  //System.out.println("expand this = " + this);
                return genericTraversal(subject,this);
              } }
  // end match
          } else {
            return genericTraversal(subject,this);
          }
        } // end apply
      }; // end new
    
    return (TomTerm) replace.apply(subject); 
  }

  private TomTerm expandRecordAppl(Option option, String tomName, TomList args)
    throws TomException {
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
             { TomName tom_match2_1 = null; TomTerm tom_match2_2 = null; tom_match2_1 = (TomName) slotName; tom_match2_2 = (TomTerm) pairSlotName;matchlab_match2_pattern1: { String name = null; TomTerm slotSubterm = null; String tom_renamedvar_name_1 = null; if(tom_is_fun_sym_Name(tom_match2_1)) { String tom_match2_1_1 = null; tom_match2_1_1 = (String) tom_get_slot_Name_string(tom_match2_1); tom_renamedvar_name_1 = (String) tom_match2_1_1; if(tom_is_fun_sym_PairSlotAppl(tom_match2_2)) { TomName tom_match2_2_1 = null; TomTerm tom_match2_2_2 = null; tom_match2_2_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match2_2); tom_match2_2_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match2_2); if(tom_is_fun_sym_Name(tom_match2_2_1)) { String tom_match2_2_1_1 = null; tom_match2_2_1_1 = (String) tom_get_slot_Name_string(tom_match2_2_1); name = (String) tom_match2_2_1_1; slotSubterm = (TomTerm) tom_match2_2_2; if(tom_terms_equal_String(name, tom_renamedvar_name_1) &&  true ) {
 
                  // bingo
                statistics().numberSlotsExpanded++;
                newSubterm = expand(slotSubterm);
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
  
  private TomTerm expandBackQuoteTerm(TomTerm t) throws TomException {
    Replace replaceSymbol = new Replace() {
        public ATerm apply(ATerm t) throws TomException {
          if(t instanceof TomTerm) {
             { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) t;matchlab_match3_pattern1: { TomName name = null; TomList l = null; String tomName = null; OptionList optionList = null; if(tom_is_fun_sym_Appl(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomList tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_Appl_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match3_1); tom_match3_1_3 = (TomList) tom_get_slot_Appl_args(tom_match3_1); if(tom_is_fun_sym_Option(tom_match3_1_1)) { OptionList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match3_1_1); optionList = (OptionList) tom_match3_1_1_1; if(tom_is_fun_sym_Name(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_Name_string(tom_match3_1_2); name = (TomName) tom_match3_1_2; tomName = (String) tom_match3_1_2_1; l = (TomList) tom_match3_1_3;
 
                TomSymbol tomSymbol = getSymbol(tomName);
                  //TomList args  = tomListMap(l,this);
                TomList args  = (TomList) genericTraversal(l,this);

                if(tomSymbol != null) {
                  if(isListOperator(tomSymbol)) {
                    return tom_make_BuildList(name,args) ;
                  } else if(isArrayOperator(tomSymbol)) {
                    return tom_make_BuildArray(name,args) ;
                  } else {
                    return tom_make_BuildTerm(name,args) ;
                  }
                } else if(args.isEmpty() && getLRParen(optionList)==null) {
                  return tom_make_BuildVariable(name) ;
                } else {
                  return tom_make_FunctionCall(name,args) ;
                }
               } } }}matchlab_match3_pattern2: { TomTerm t2 = null; TomTerm t1 = null; if(tom_is_fun_sym_DotTerm(tom_match3_1)) { TomTerm tom_match3_1_1 = null; TomTerm tom_match3_1_2 = null; tom_match3_1_1 = (TomTerm) tom_get_slot_DotTerm_kid1(tom_match3_1); tom_match3_1_2 = (TomTerm) tom_get_slot_DotTerm_kid2(tom_match3_1); t1 = (TomTerm) tom_match3_1_1; t2 = (TomTerm) tom_match3_1_2;

 
                TomTerm tt1 = (TomTerm) this.apply(t1);
                TomTerm tt2 = (TomTerm) this.apply(t2);
                return tom_make_DotTerm(tt1,tt2) ;
               }}matchlab_match3_pattern3: { TomName name = null; if(tom_is_fun_sym_VariableStar(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomType tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match3_1); tom_match3_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match3_1); name = (TomName) tom_match3_1_2;

 
                return tom_make_BuildVariableStar(name) ;
               }}matchlab_match3_pattern4: {

 
                System.out.println("replaceSymbol: strange case: '" + t + "'");
                System.exit(1);
              } }
 
            return t;
          } else {
            return genericTraversal(t,this);
          }
        } // end apply
      }; // end replaceSymbol
    return (TomTerm) replaceSymbol.apply(t);
  }
  
    /*
     * replace Name by Symbol
     * replace Name by Variable
     */

  private ReplaceContext replacePass1 = new ReplaceContext() { 
      public ATerm apply(ATerm contextSubject,ATerm subject) throws TomException {

        if(!(subject instanceof TomTerm)) {
          //debugPrintln("pass1 not a tomTerm: " );
            //System.out.println("pass1 not a tomTerm:\n\t" + subject );
          
          if(subject instanceof TomType) {
             { TomType tom_match4_1 = null; tom_match4_1 = (TomType) subject;matchlab_match4_pattern1: { String tomType = null; if(tom_is_fun_sym_TomTypeAlone(tom_match4_1)) { String tom_match4_1_1 = null; tom_match4_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match4_1); tomType = (String) tom_match4_1_1;
  return getType(tomType);  }} }
 
          }
          return genericTraversalContext(contextSubject,subject,this);
        }

          //System.out.println("pass1 is a tomTerm:\n\t" + subject );
        
         { TomTerm tom_match5_1 = null; TomTerm tom_match5_2 = null; tom_match5_1 = (TomTerm) contextSubject; tom_match5_2 = (TomTerm) subject;matchlab_match5_pattern1: { TomType type = null; TomName name = null; String strName = null; OptionList optionList = null; TomType tomType = null; TomTerm appl = null; TomType glType = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match5_1)) { TomType tom_match5_1_1 = null; tom_match5_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match5_1); if(tom_is_fun_sym_Type(tom_match5_1_1)) { TomType tom_match5_1_1_1 = null; TomType tom_match5_1_1_2 = null; tom_match5_1_1_1 = (TomType) tom_get_slot_Type_tomType(tom_match5_1_1); tom_match5_1_1_2 = (TomType) tom_get_slot_Type_tlType(tom_match5_1_1); type = (TomType) tom_match5_1_1; tomType = (TomType) tom_match5_1_1_1; glType = (TomType) tom_match5_1_1_2; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); appl = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_Empty(tom_match5_2_3)) {
 
            //debugPrintln("pass1.1: Type(" + tomType + "," + glType + ")");
            //debugPrintln("appl = " + appl);
                
            Option option = tom_make_Option(replaceAnnotedName(optionList, type)) ;
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return tom_make_Appl(option,name,tom_make_Empty()) ;
            } else {
              statistics().numberVariablesDetected++;
              testVariableWithoutParen(option,strName);
              return tom_make_Variable(option,name,type) ;
            }
           } } } } } }}matchlab_match5_pattern2: { TomName name1 = null; TomName name = null; String strName = null; TomType type1 = null; TomTerm appl = null; Option option1 = null; OptionList optionList = null; if(tom_is_fun_sym_Variable(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomType tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_1); option1 = (Option) tom_match5_1_1; name1 = (TomName) tom_match5_1_2; type1 = (TomType) tom_match5_1_3; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); appl = (TomTerm) tom_match5_2; if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_Empty(tom_match5_2_3)) {

 
            //debugPrintln("pass1.3: Variable(" + option1 + "," + name1 + "," + type1 + ")");
            //debugPrintln("appl = " + appl);
                
            Option option = tom_make_Option(replaceAnnotedName(optionList, type1)) ;
              // under a match construct
              // create a constant or a variable
            TomSymbol tomSymbol = getSymbol(strName);
            if(tomSymbol != null) {
              return tom_make_Appl(option,name,tom_make_Empty()) ;
            } else {
              statistics().numberVariablesDetected++;
              testVariableWithoutParen(option,strName);
              return tom_make_Variable(option,name,type1) ;
            }
           } } } } }}matchlab_match5_pattern3: { TomType type = null; TomType tomType = null; OptionList optionList = null; TomType glType = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match5_1)) { TomType tom_match5_1_1 = null; tom_match5_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match5_1); if(tom_is_fun_sym_Type(tom_match5_1_1)) { TomType tom_match5_1_1_1 = null; TomType tom_match5_1_1_2 = null; tom_match5_1_1_1 = (TomType) tom_get_slot_Type_tomType(tom_match5_1_1); tom_match5_1_1_2 = (TomType) tom_get_slot_Type_tlType(tom_match5_1_1); type = (TomType) tom_match5_1_1; tomType = (TomType) tom_match5_1_1_1; glType = (TomType) tom_match5_1_1_2; if(tom_is_fun_sym_Placeholder(tom_match5_2)) { Option tom_match5_2_1 = null; tom_match5_2_1 = (Option) tom_get_slot_Placeholder_option(tom_match5_2); if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1;

 
            Option option = tom_make_Option(replaceAnnotedName(optionList, type)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type) ;
           } } } }}matchlab_match5_pattern4: { TomType type1 = null; OptionList optionList = null; Option option1 = null; TomName name1 = null; if(tom_is_fun_sym_Variable(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomType tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_1); option1 = (Option) tom_match5_1_1; name1 = (TomName) tom_match5_1_2; type1 = (TomType) tom_match5_1_3; if(tom_is_fun_sym_Placeholder(tom_match5_2)) { Option tom_match5_2_1 = null; tom_match5_2_1 = (Option) tom_get_slot_Placeholder_option(tom_match5_2); if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1;

 
            Option option = tom_make_Option(replaceAnnotedName(optionList, type1)) ;
              // create an unamed variable
            return tom_make_UnamedVariable(option,type1) ;
           } } }}matchlab_match5_pattern5: { TomList l = null; TomName name = null; TomTerm context = null; OptionList optionList = null; String tomName = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Appl(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomList tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Appl_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2); tom_match5_2_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2); if(tom_is_fun_sym_Option(tom_match5_2_1)) { OptionList tom_match5_2_1_1 = null; tom_match5_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1); optionList = (OptionList) tom_match5_2_1_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); name = (TomName) tom_match5_2_2; tomName = (String) tom_match5_2_2_1; l = (TomList) tom_match5_2_3;

 
            //debugPrintln("pass1.6: Appl(Name(" + tomName + ")," + l + ")");
            //debugPrintln("\tcontext = " + context);
                
              // create a  symbol
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomList subterm = pass1List(tomSymbol, l);
                //System.out.println("***** pass1.6: pass1List = " + subterm);
              Option option = tom_make_Option(replaceAnnotedName(optionList, getSymbolCodomain(tomSymbol))) ;
              return tom_make_Appl(option,name,subterm) ;
            }
           } } }}matchlab_match5_pattern6: { String strName = null; Option option = null; TomTerm context = null; String tomType = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Variable(tom_match5_2)) { Option tom_match5_2_1 = null; TomName tom_match5_2_2 = null; TomType tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Variable_option(tom_match5_2); tom_match5_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_2); tom_match5_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_2); option = (Option) tom_match5_2_1; if(tom_is_fun_sym_Name(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_2); strName = (String) tom_match5_2_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match5_2_3)) { String tom_match5_2_3_1 = null; tom_match5_2_3_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match5_2_3); tomType = (String) tom_match5_2_3_1;

 
              // create a variable
            TomType localType = getType(tomType);
            if(localType == null) { 
              messageMatchTypeVariableError(strName, tomType);
            } else {
              return tom_make_Variable(option,tom_make_Name(strName),localType) ;
            }
           } } }}matchlab_match5_pattern7: { String strName = null; String tomType = null; TomTerm context = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_GLVar(tom_match5_2)) { String tom_match5_2_1 = null; TomType tom_match5_2_2 = null; tom_match5_2_1 = (String) tom_get_slot_GLVar_strName(tom_match5_2); tom_match5_2_2 = (TomType) tom_get_slot_GLVar_astType(tom_match5_2); strName = (String) tom_match5_2_1; if(tom_is_fun_sym_TomTypeAlone(tom_match5_2_2)) { String tom_match5_2_2_1 = null; tom_match5_2_2_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match5_2_2); tomType = (String) tom_match5_2_2_1;

 
            //debugPrintln("pass1.8: GLVar(" + strName + "," + tomType + ")");
                
              // create a variable
            TomType localType = getType(tomType);
            if ( localType == null ) { 
              messageMatchTypeVariableError(strName, tomType);
            } else {
              Option option = ast().makeOption();
              return tom_make_Variable(option,tom_make_Name(strName),localType) ;
            }
           } }}matchlab_match5_pattern8: { TomList subjectList = null; TomList l1 = null; if(tom_is_fun_sym_SubjectList(tom_match5_1)) { TomList tom_match5_1_1 = null; tom_match5_1_1 = (TomList) tom_get_slot_SubjectList_list(tom_match5_1); l1 = (TomList) tom_match5_1_1; if(tom_is_fun_sym_TermList(tom_match5_2)) { TomList tom_match5_2_1 = null; tom_match5_2_1 = (TomList) tom_get_slot_TermList_list(tom_match5_2); subjectList = (TomList) tom_match5_2_1;

 
            //debugPrintln("pass1.9: TermList(" + subjectList + ")");
                
              // process a list of subterms
            ArrayList list = new ArrayList();
            while(!subjectList.isEmpty()) {
              list.add(pass1(l1.getHead(), subjectList.getHead()));
              subjectList = subjectList.getTail();
              l1 = l1.getTail();
            }
            return tom_make_TermList(ast().makeList(list)) ;
           } }}matchlab_match5_pattern9: { TomTerm patternList = null; TomTerm tomSubjectList = null; TomTerm context = null; Option option = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_Match(tom_match5_2)) { Option tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; TomTerm tom_match5_2_3 = null; tom_match5_2_1 = (Option) tom_get_slot_Match_option(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_Match_subjectList(tom_match5_2); tom_match5_2_3 = (TomTerm) tom_get_slot_Match_patternList(tom_match5_2); option = (Option) tom_match5_2_1; tomSubjectList = (TomTerm) tom_match5_2_2; patternList = (TomTerm) tom_match5_2_3;

 
            //debugPrintln("pass1.10: Match(" + tomSubjectList + "," + patternList + ")");
            optionMatchTypeVariable = option;
            TomTerm newSubjectList = pass1(context,tomSubjectList);
            TomTerm newPatternList = pass1(newSubjectList,patternList);
            return tom_make_Match(option,newSubjectList,newPatternList) ;
           }}matchlab_match5_pattern10: { String tomName = null; TomList l = null; OptionList optionList = null; TomTerm rhs = null; TomTerm context = null; TomTerm lhs = null; context = (TomTerm) tom_match5_1; if(tom_is_fun_sym_RewriteRule(tom_match5_2)) { TomTerm tom_match5_2_1 = null; TomTerm tom_match5_2_2 = null; tom_match5_2_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match5_2); tom_match5_2_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match5_2); if(tom_is_fun_sym_Term(tom_match5_2_1)) { TomTerm tom_match5_2_1_1 = null; tom_match5_2_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match5_2_1); if(tom_is_fun_sym_Appl(tom_match5_2_1_1)) { Option tom_match5_2_1_1_1 = null; TomName tom_match5_2_1_1_2 = null; TomList tom_match5_2_1_1_3 = null; tom_match5_2_1_1_1 = (Option) tom_get_slot_Appl_option(tom_match5_2_1_1); tom_match5_2_1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_2_1_1); tom_match5_2_1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match5_2_1_1); lhs = (TomTerm) tom_match5_2_1_1; if(tom_is_fun_sym_Option(tom_match5_2_1_1_1)) { OptionList tom_match5_2_1_1_1_1 = null; tom_match5_2_1_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_2_1_1_1); optionList = (OptionList) tom_match5_2_1_1_1_1; if(tom_is_fun_sym_Name(tom_match5_2_1_1_2)) { String tom_match5_2_1_1_2_1 = null; tom_match5_2_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_2_1_1_2); tomName = (String) tom_match5_2_1_1_2_1; l = (TomList) tom_match5_2_1_1_3; if(tom_is_fun_sym_Term(tom_match5_2_2)) { TomTerm tom_match5_2_2_1 = null; tom_match5_2_2_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match5_2_2); rhs = (TomTerm) tom_match5_2_2_1;









  
            //debugPrintln("pass1.13: Rule(" + lhs + "," + rhs + ")");
            TomSymbol tomSymbol = getSymbol(tomName);
            if(tomSymbol != null) {
              TomType symbolType = getSymbolCodomain(tomSymbol);
              TomTerm newLhs = tom_make_Term(pass1(context, lhs)) ;
              TomTerm newRhs = tom_make_Term(pass1(tom_make_TomTypeToTomTerm(symbolType), rhs)) ;
              return tom_make_RewriteRule(newLhs,newRhs) ;
            } else {
                //verifier().messageRuleSymbolError(tomName, optionList);
            }
           } } } } } }}matchlab_match5_pattern11: { TomTerm t = null; TomTerm context = null; context = (TomTerm) tom_match5_1; t = (TomTerm) tom_match5_2;


 
            //debugPrintln("pass1.11 default: " );
              //System.out.println("pass1 default:\n\t" + subject );
            return genericTraversalContext(contextSubject,subject,this);
          } }
  // end match
      } // end apply
    }; // end new

  public TomTerm pass1(TomTerm contextSubject, TomTerm subject) throws TomException {
    return (TomTerm) replacePass1.apply(contextSubject,subject); 
  }

  public TomList pass1List(TomSymbol subject, TomList subjectList) throws TomException {
    //%variable
     { TomSymbol tom_match6_1 = null; tom_match6_1 = (TomSymbol) subject;matchlab_match6_pattern1: { TomType codomainType = null; TomList typeList = null; TomSymbol symb = null; if(tom_is_fun_sym_Symbol(tom_match6_1)) { TomName tom_match6_1_1 = null; TomType tom_match6_1_2 = null; SlotList tom_match6_1_3 = null; Option tom_match6_1_4 = null; TargetLanguage tom_match6_1_5 = null; tom_match6_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match6_1); tom_match6_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match6_1); tom_match6_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match6_1); tom_match6_1_4 = (Option) tom_get_slot_Symbol_option(tom_match6_1); tom_match6_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match6_1); symb = (TomSymbol) tom_match6_1; if(tom_is_fun_sym_TypesToType(tom_match6_1_2)) { TomList tom_match6_1_2_1 = null; TomType tom_match6_1_2_2 = null; tom_match6_1_2_1 = (TomList) tom_get_slot_TypesToType_list(tom_match6_1_2); tom_match6_1_2_2 = (TomType) tom_get_slot_TypesToType_codomain(tom_match6_1_2); typeList = (TomList) tom_match6_1_2_1; codomainType = (TomType) tom_match6_1_2_2;
 
        //debugPrintln("pass1List.1: " + subjectList);
          
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
               { TomTerm tom_match7_1 = null; tom_match7_1 = (TomTerm) subterm;matchlab_match7_pattern1: { TomType type = null; TomName name = null; Option voption = null; if(tom_is_fun_sym_VariableStar(tom_match7_1)) { Option tom_match7_1_1 = null; TomName tom_match7_1_2 = null; TomType tom_match7_1_3 = null; tom_match7_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match7_1); tom_match7_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match7_1); tom_match7_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match7_1); voption = (Option) tom_match7_1_1; name = (TomName) tom_match7_1_2; type = (TomType) tom_match7_1_3;
 
                  list.add(tom_make_VariableStar(voption,name,codomainType) );
                    //System.out.println("*** break: " + subterm);
                  break matchBlock;
                 }}matchlab_match7_pattern2: {

 
                  list.add(pass1(domainType, subterm));
                  break matchBlock;
                } }
 
            }
            subjectList = subjectList.getTail();
          }
        } else {
          while(!subjectList.isEmpty()) {
            list.add(pass1(typeList.getHead(), subjectList.getHead()));
            subjectList = subjectList.getTail();
            typeList    = typeList.getTail();
          }
        }
       
        result = ast().makeList(list);
        return result;
       } }}matchlab_match6_pattern2: {

 
        System.out.println("pass1List: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return null;
  }

    /*
     * updateSymbolPass1 is called after the expansion phase
     * this phase updates the symbolTable according to the typeTable
     * this is performed by recursively traversing each symbol
     * each TomTypeAlone is replace by the corresponding TomType
     */
  public void updateSymbolPass1() {
    try {
      Iterator it = symbolTable().keySymbolIterator();
      while(it.hasNext()) {
        String tomName = (String)it.next();
        TomTerm emptyContext = null;
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        tomSymbol = pass1(emptyContext,tom_make_TomSymbolToTomTerm(tomSymbol) ).getAstSymbol();
        symbolTable().putSymbol(tomName,tomSymbol);
      }
    } catch(TomException e) {
      System.out.println("updateSymbolPass1 error");
      System.exit(1);
    }
  }
  
  private TomSymbol getSymbol(String tomName) throws TomException {
    TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
    return tomSymbol;
  }

  private TomType getType(String tomName) {
    TomType tomType = symbolTable().getType(tomName);
    return tomType;
  }

    /*
     * TODO: X1*,name@Name(_),X2* -> { return name; }
     */
  private TomName getAnnotedName(TomList subjectList) {
    //%variable
    while(!subjectList.isEmpty()) {
      TomTerm subject = subjectList.getHead();
       { TomTerm tom_match8_1 = null; tom_match8_1 = (TomTerm) subject;matchlab_match8_pattern1: { TomName name = null; if(tom_is_fun_sym_TomNameToTomTerm(tom_match8_1)) { TomName tom_match8_1_1 = null; tom_match8_1_1 = (TomName) tom_get_slot_TomNameToTomTerm_astName(tom_match8_1); if(tom_is_fun_sym_Name(tom_match8_1_1)) { String tom_match8_1_1_1 = null; tom_match8_1_1_1 = (String) tom_get_slot_Name_string(tom_match8_1_1); name = (TomName) tom_match8_1_1;
  return name;  } }} }
 
      subjectList = subjectList.getTail();
    }
    return null;
  }

  private OptionList replaceAnnotedName(OptionList subjectList, TomType type) {
    //%variable
     { OptionList tom_match9_1 = null; tom_match9_1 = (OptionList) subjectList;matchlab_match9_pattern1: { if(tom_is_fun_sym_EmptyOptionList(tom_match9_1)) {
  return subjectList;  }}matchlab_match9_pattern2: { OptionList l = null; TomName name = null; if(tom_is_fun_sym_ConsOptionList(tom_match9_1)) { Option tom_match9_1_1 = null; OptionList tom_match9_1_2 = null; tom_match9_1_1 = (Option) tom_get_slot_ConsOptionList_head(tom_match9_1); tom_match9_1_2 = (OptionList) tom_get_slot_ConsOptionList_tail(tom_match9_1); if(tom_is_fun_sym_TomNameToOption(tom_match9_1_1)) { TomName tom_match9_1_1_1 = null; tom_match9_1_1_1 = (TomName) tom_get_slot_TomNameToOption_astName(tom_match9_1_1); if(tom_is_fun_sym_Name(tom_match9_1_1_1)) { String tom_match9_1_1_1_1 = null; tom_match9_1_1_1_1 = (String) tom_get_slot_Name_string(tom_match9_1_1_1); name = (TomName) tom_match9_1_1_1; l = (OptionList) tom_match9_1_2;
 
        return tom_make_ConsOptionList(tom_make_TomTermToOption(tom_make_Variable(ast().makeOption(),name,type)),replaceAnnotedName(l, type))

 ;
       } } }}matchlab_match9_pattern3: { Option t = null; OptionList l = null; if(tom_is_fun_sym_ConsOptionList(tom_match9_1)) { Option tom_match9_1_1 = null; OptionList tom_match9_1_2 = null; tom_match9_1_1 = (Option) tom_get_slot_ConsOptionList_head(tom_match9_1); tom_match9_1_2 = (OptionList) tom_get_slot_ConsOptionList_tail(tom_match9_1); t = (Option) tom_match9_1_1; l = (OptionList) tom_match9_1_2;
 
        return tom_make_ConsOptionList(t,replaceAnnotedName(l, type)) ;
       }} }
 
    return null;
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
  private void testVariableWithoutParen(Option option, String name) throws TomException {
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

  private void messageMatchTypeVariableError(String name, String type) throws TomException {
    OptionList optionList = optionMatchTypeVariable.getOptionList();
    String line = findOriginTrackingLine("Match", optionList);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct";
    messageError(line,s);
  }
  private void messageVariableWithParenError( String  name, String line ) {
    if(Flags.noWarning) return;
    System.out.println("\n *** Warning *** Variable with () is not recommanded");
    System.out.println(" *** Variable '"+name+"' has () - Line : "+line);
  }
}
