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
                                                                                                                                                              
    // ------------------------------------------------------------
  
    // Main entry point: We verify all interesting Tom Structure
  public void verify(TomTerm parsedTerm) throws TomException {
    if(!Flags.doVerify) return;
    Collect collectAndVerify = new Collect() 
      {  
        public boolean apply(ATerm term) throws TomException {
          if(term instanceof TomTerm) {
             { TomTerm tom_match1_1 = null; tom_match1_1 = (TomTerm) term;matchlab_match1_pattern1: { Option orgTrack = null; TomList matchArgsList = null; TomList patternActionList = null; if(tom_is_fun_sym_Match(tom_match1_1)) { Option tom_match1_1_1 = null; TomTerm tom_match1_1_2 = null; TomTerm tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_Match_option(tom_match1_1); tom_match1_1_2 = (TomTerm) tom_get_slot_Match_subjectList(tom_match1_1); tom_match1_1_3 = (TomTerm) tom_get_slot_Match_patternList(tom_match1_1); orgTrack = (Option) tom_match1_1_1; if(tom_is_fun_sym_SubjectList(tom_match1_1_2)) { TomList tom_match1_1_2_1 = null; tom_match1_1_2_1 = (TomList) tom_get_slot_SubjectList_list(tom_match1_1_2); matchArgsList = (TomList) tom_match1_1_2_1; if(tom_is_fun_sym_PatternList(tom_match1_1_3)) { TomList tom_match1_1_3_1 = null; tom_match1_1_3_1 = (TomList) tom_get_slot_PatternList_list(tom_match1_1_3); patternActionList = (TomList) tom_match1_1_3_1;
   
                currentTomStructureOrgTrack = orgTrack;
                verifyMatch(matchArgsList, patternActionList);
                return false;
               } } }}matchlab_match1_pattern2: { TomList list = null; Option orgTrack = null; if(tom_is_fun_sym_RuleSet(tom_match1_1)) { Option tom_match1_1_1 = null; TomList tom_match1_1_2 = null; tom_match1_1_1 = (Option) tom_get_slot_RuleSet_option(tom_match1_1); tom_match1_1_2 = (TomList) tom_get_slot_RuleSet_list(tom_match1_1); orgTrack = (Option) tom_match1_1_1; list = (TomList) tom_match1_1_2;
 
                currentTomStructureOrgTrack = orgTrack;
                verifyRule(list);
                return false;
               }}matchlab_match1_pattern3: { Declaration declaration = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match1_1)) { Declaration tom_match1_1_1 = null; tom_match1_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match1_1); declaration = (Declaration) tom_match1_1_1;
 
                verifyDeclaration(declaration);
                return false;
               }}matchlab_match1_pattern4: {



  return true; } }
 
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
    // Given a subject list, we test types in match signature and then number and type of slots found in each pattern
  private void verifyMatch(TomList subjectList, TomList patternList) throws TomException {
    ArrayList typeMatchArgs = new ArrayList();
    while( !subjectList.isEmpty() ) {
      TomTerm term = subjectList.getHead();
       { TomTerm tom_match2_1 = null; tom_match2_1 = (TomTerm) term;matchlab_match2_pattern1: { String type = null; String name = null; if(tom_is_fun_sym_GLVar(tom_match2_1)) { String tom_match2_1_1 = null; TomType tom_match2_1_2 = null; tom_match2_1_1 = (String) tom_get_slot_GLVar_strName(tom_match2_1); tom_match2_1_2 = (TomType) tom_get_slot_GLVar_astType(tom_match2_1); name = (String) tom_match2_1_1; if(tom_is_fun_sym_TomTypeAlone(tom_match2_1_2)) { String tom_match2_1_2_1 = null; tom_match2_1_2_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match2_1_2); type = (String) tom_match2_1_2_1;
 
          if(symbolTable().getType(type) == null) {
            messageMatchTypeVariableError(name, type);
          }
          typeMatchArgs.add(type);
         } }} }
 
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
      
       { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) termAppl;matchlab_match3_pattern1: { String name = null; OptionList list = null; if(tom_is_fun_sym_Appl(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomList tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_Appl_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match3_1); tom_match3_1_3 = (TomList) tom_get_slot_Appl_args(tom_match3_1); if(tom_is_fun_sym_Option(tom_match3_1_1)) { OptionList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match3_1_1); list = (OptionList) tom_match3_1_1_1; if(tom_is_fun_sym_Name(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_Name_string(tom_match3_1_2); name = (String) tom_match3_1_2_1;
 
            //  we test the validity of the current Appl structure
          testApplStructure(termAppl);
          line = findOriginTrackingLine(list);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
         } } }}matchlab_match3_pattern2: { if(tom_is_fun_sym_Placeholder(tom_match3_1)) { Option tom_match3_1_1 = null; tom_match3_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match3_1);
 
          nbFoundArgs++;
          foundTypeMatch.add((TomTerm) null);
         }}matchlab_match3_pattern3: { String name = null; OptionList list = null; if(tom_is_fun_sym_VariableStar(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomType tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match3_1); tom_match3_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match3_1); if(tom_is_fun_sym_Option(tom_match3_1_1)) { OptionList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match3_1_1); list = (OptionList) tom_match3_1_1_1; if(tom_is_fun_sym_Name(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_Name_string(tom_match3_1_2); name = (String) tom_match3_1_2_1;

  
          line = findOriginTrackingLine(name,list);
          messageMatchErrorVariableStar(name, line); 
         } } }}matchlab_match3_pattern4: { TomList args = null; OptionList options = null; String name = null; if(tom_is_fun_sym_RecordAppl(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomList tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match3_1); tom_match3_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match3_1); if(tom_is_fun_sym_Option(tom_match3_1_1)) { OptionList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match3_1_1); options = (OptionList) tom_match3_1_1_1; if(tom_is_fun_sym_Name(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_Name_string(tom_match3_1_2); name = (String) tom_match3_1_2_1; args = (TomList) tom_match3_1_3;
 
            //  we test the validity of the current record structure
          testRecordStructure(options, name, args);
          line = findOriginTrackingLine(options);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
         } } }} }
 
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
         { TomTerm tom_match4_1 = null; tom_match4_1 = (TomTerm) currentRule;matchlab_match4_pattern1: { TomTerm lhs = null; TomTerm rhs = null; if(tom_is_fun_sym_RewriteRule(tom_match4_1)) { TomTerm tom_match4_1_1 = null; TomTerm tom_match4_1_2 = null; tom_match4_1_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match4_1); tom_match4_1_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match4_1); if(tom_is_fun_sym_Term(tom_match4_1_1)) { TomTerm tom_match4_1_1_1 = null; tom_match4_1_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match4_1_1); lhs = (TomTerm) tom_match4_1_1_1; if(tom_is_fun_sym_Term(tom_match4_1_2)) { TomTerm tom_match4_1_2_1 = null; tom_match4_1_2_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match4_1_2); rhs = (TomTerm) tom_match4_1_2_1;
 
            statistics().numberRulesTested++;
            if(i == 0) {
                // update the root of lhs: it becomes a defined symbol
              ast().updateDefinedSymbol(symbolTable(),lhs);
            }
            verifyRhsRuleStructure(rhs, true);
            nameAndType = verifyLhsRuleTypeAndConstructorEgality(lhs, nameAndType, i);
            verifyRuleVariable(lhs,rhs);
            verifyLhsAndRhsRuleTypeEgality(rhs, lhs, (String)nameAndType.get(1));
            break matchBlock;
           } } }}matchlab_match4_pattern2: {
 
            System.out.println("Strange rule:\n" + currentRule);
            System.exit(1);
          } }
 
      }
      ruleList = ruleList.getTail();
      i++;
    }
  }
  
  private void verifyRhsRuleStructure(TomTerm ruleRhs, boolean firstLevel) throws TomException {
    matchBlock: {
       { TomTerm tom_match5_1 = null; tom_match5_1 = (TomTerm) ruleRhs;matchlab_match5_pattern1: { TomList argsList = null; if(tom_is_fun_sym_Appl(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomList tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_Appl_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match5_1); tom_match5_1_3 = (TomList) tom_get_slot_Appl_args(tom_match5_1); argsList = (TomList) tom_match5_1_3;
 
          testApplStructureRhsRule(ruleRhs);
          while(!argsList.isEmpty()) {
            TomTerm oneArgs = argsList.getHead();
            verifyRhsRuleStructure(oneArgs, false);
            argsList = argsList.getTail();
          }
          break matchBlock;
         }}matchlab_match5_pattern2: { OptionList option = null; if(tom_is_fun_sym_Placeholder(tom_match5_1)) { Option tom_match5_1_1 = null; tom_match5_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) { OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); option = (OptionList) tom_match5_1_1_1;
 
          messageRuleErrorRhsImpossiblePlaceholder(option);
          break matchBlock;
         } }}matchlab_match5_pattern3: { String name1 = null; OptionList list = null; if(tom_is_fun_sym_VariableStar(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomType tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match5_1); tom_match5_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) { OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); list = (OptionList) tom_match5_1_1_1; if(tom_is_fun_sym_Name(tom_match5_1_2)) { String tom_match5_1_2_1 = null; tom_match5_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_1_2); name1 = (String) tom_match5_1_2_1;
 
          if(firstLevel) System.out.println("VariableStar on rhs of rule: Allowed?");
          break matchBlock;
         } } }}matchlab_match5_pattern4: { String tomName = null; OptionList option = null; TomList args = null; if(tom_is_fun_sym_RecordAppl(tom_match5_1)) { Option tom_match5_1_1 = null; TomName tom_match5_1_2 = null; TomList tom_match5_1_3 = null; tom_match5_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match5_1); tom_match5_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match5_1); tom_match5_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) { OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); option = (OptionList) tom_match5_1_1_1; if(tom_is_fun_sym_Name(tom_match5_1_2)) { String tom_match5_1_2_1 = null; tom_match5_1_2_1 = (String) tom_get_slot_Name_string(tom_match5_1_2); tomName = (String) tom_match5_1_2_1; args = (TomList) tom_match5_1_3;
 
          System.out.println("What to do with Record on rhs of rule");
          messageRuleErrorRhsImpossiblePlaceholder(option);
         } } }}matchlab_match5_pattern5: {
 
          System.out.println("Strange rule rhs:\n" + ruleRhs);
          System.exit(1);
        } }
 
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
     { TomTerm tom_match6_1 = null; tom_match6_1 = (TomTerm) lhs;matchlab_match6_pattern1: { OptionList optionList = null; String name = null; if(tom_is_fun_sym_Appl(tom_match6_1)) { Option tom_match6_1_1 = null; TomName tom_match6_1_2 = null; TomList tom_match6_1_3 = null; tom_match6_1_1 = (Option) tom_get_slot_Appl_option(tom_match6_1); tom_match6_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match6_1); tom_match6_1_3 = (TomList) tom_get_slot_Appl_args(tom_match6_1); if(tom_is_fun_sym_Option(tom_match6_1_1)) { OptionList tom_match6_1_1_1 = null; tom_match6_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match6_1_1); optionList = (OptionList) tom_match6_1_1_1; if(tom_is_fun_sym_Name(tom_match6_1_2)) { String tom_match6_1_2_1 = null; tom_match6_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_2); name = (String) tom_match6_1_2_1;
 
        testApplStructure(lhs);
        TomType term = getSymbolCodomain(symbolTable().getSymbol(name));
         { TomType tom_match7_1 = null; tom_match7_1 = (TomType) term;matchlab_match7_pattern1: { String t = null; if(tom_is_fun_sym_TomTypeAlone(tom_match7_1)) { String tom_match7_1_1 = null; tom_match7_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match7_1); t = (String) tom_match7_1_1;
  
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.clear();
              nameType.add(name);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name != nameType.get(0) ) ) {
                messageRuleErrorTypeAndConstructorEgality(name, (String)nameType.get(0), t, (String)nameType.get(1), optionList);
              }
            }
           }}matchlab_match7_pattern2: { if(tom_is_fun_sym_EmptyType(tom_match7_1)) {
 
            messageSymbolError(name, optionList);
           }} }
 
       } } }}matchlab_match6_pattern2: { String name1 = null; OptionList optionList = null; if(tom_is_fun_sym_VariableStar(tom_match6_1)) { Option tom_match6_1_1 = null; TomName tom_match6_1_2 = null; TomType tom_match6_1_3 = null; tom_match6_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match6_1); tom_match6_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match6_1); tom_match6_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match6_1); if(tom_is_fun_sym_Option(tom_match6_1_1)) { OptionList tom_match6_1_1_1 = null; tom_match6_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match6_1_1); optionList = (OptionList) tom_match6_1_1_1; if(tom_is_fun_sym_Name(tom_match6_1_2)) { String tom_match6_1_2_1 = null; tom_match6_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_2); name1 = (String) tom_match6_1_2_1;
  
        String line = findOriginTrackingLine(name1,optionList);
        messageRuleErrorVariableStar(name1, line); 
       } } }}matchlab_match6_pattern3: { OptionList t = null; if(tom_is_fun_sym_Placeholder(tom_match6_1)) { Option tom_match6_1_1 = null; tom_match6_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match6_1); if(tom_is_fun_sym_Option(tom_match6_1_1)) { OptionList tom_match6_1_1_1 = null; tom_match6_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match6_1_1); t = (OptionList) tom_match6_1_1_1;
  
        messageRuleErrorLhsImpossiblePlaceHolder(t); 
       } }}matchlab_match6_pattern4: { TomList args = null; OptionList optionList = null; String name = null; if(tom_is_fun_sym_RecordAppl(tom_match6_1)) { Option tom_match6_1_1 = null; TomName tom_match6_1_2 = null; TomList tom_match6_1_3 = null; tom_match6_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match6_1); tom_match6_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match6_1); tom_match6_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match6_1); if(tom_is_fun_sym_Option(tom_match6_1_1)) { OptionList tom_match6_1_1_1 = null; tom_match6_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match6_1_1); optionList = (OptionList) tom_match6_1_1_1; if(tom_is_fun_sym_Name(tom_match6_1_2)) { String tom_match6_1_2_1 = null; tom_match6_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_2); name = (String) tom_match6_1_2_1; args = (TomList) tom_match6_1_3;
 
        testRecordStructure(lhs);
        TomType term = getSymbolCodomain(symbolTable().getSymbol(name));
         { TomType tom_match8_1 = null; tom_match8_1 = (TomType) term;matchlab_match8_pattern1: { String t = null; if(tom_is_fun_sym_TomTypeAlone(tom_match8_1)) { String tom_match8_1_1 = null; tom_match8_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match8_1); t = (String) tom_match8_1_1;
  
            if ( ruleNumber == 0 ) { 
                /* it is the first call so we memorize name and type */
              nameType.clear();
              nameType.add(name);
              nameType.add(t); 
            } else { 
                /* We test type and name constructor egality */
              if ( ( t != nameType.get(1) ) || ( name != nameType.get(0) ) ) {
                messageRuleErrorTypeAndConstructorEgality(name, (String)nameType.get(0), t, (String)nameType.get(1), optionList);
              }
            }
           }}matchlab_match8_pattern2: { if(tom_is_fun_sym_EmptyType(tom_match8_1)) {
 
            messageSymbolError(name, optionList);
           }} }
 
	System.out.println("Warning prefer f() instead of f[] or Message for impossible record on lhs of rule?");
       } } }} }
 
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
    String foundTypeName = "Not found";
     { TomTerm tom_match9_1 = null; tom_match9_1 = (TomTerm) rhs;matchlab_match9_pattern1: { TomList argsList = null; OptionList options = null; String name1 = null; if(tom_is_fun_sym_Appl(tom_match9_1)) { Option tom_match9_1_1 = null; TomName tom_match9_1_2 = null; TomList tom_match9_1_3 = null; tom_match9_1_1 = (Option) tom_get_slot_Appl_option(tom_match9_1); tom_match9_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match9_1); tom_match9_1_3 = (TomList) tom_get_slot_Appl_args(tom_match9_1); if(tom_is_fun_sym_Option(tom_match9_1_1)) { OptionList tom_match9_1_1_1 = null; tom_match9_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match9_1_1); options = (OptionList) tom_match9_1_1_1; if(tom_is_fun_sym_Name(tom_match9_1_2)) { String tom_match9_1_2_1 = null; tom_match9_1_2_1 = (String) tom_get_slot_Name_string(tom_match9_1_2); name1 = (String) tom_match9_1_2_1; argsList = (TomList) tom_match9_1_3;
 
          // We look the type of the resulted object of this Appl.
          //  If resulted object has no type, getSymbolCodomain returns EmptyType) (for example
          //  if it is a variable).
        TomType term = getSymbolCodomain(symbolTable().getSymbol(name1));
         { TomType tom_match10_1 = null; tom_match10_1 = (TomType) term;matchlab_match10_pattern1: { String t = null; if(tom_is_fun_sym_TomTypeAlone(tom_match10_1)) { String tom_match10_1_1 = null; tom_match10_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match10_1); t = (String) tom_match10_1_1;
  foundTypeName = t; }}matchlab_match10_pattern2: { if(tom_is_fun_sym_EmptyType(tom_match10_1)) {
  
              // We look if a variable with name 'name1' exists in lhs and we get its type.
            if ( !argsList.isEmpty() ) { // it s a variable
              TomType type = findTypeOf(name1, lhs);
              System.out.println("findTypeof("+name1+") returns "+type);
               { TomType tom_match11_1 = null; tom_match11_1 = (TomType) type;matchlab_match11_pattern1: { String t = null; if(tom_is_fun_sym_TomTypeAlone(tom_match11_1)) { String tom_match11_1_1 = null; tom_match11_1_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match11_1); t = (String) tom_match11_1_1;
   foundTypeName = t; }} }
 
            }
           }} }
 
        if ( foundTypeName != typeNameOfRule ) {
          messageRuleErrorTypeEgality(name1, foundTypeName, typeNameOfRule, options);
        }
       } } }} }
  
  }
  
    //////////////////////////////
    // SYMBOL AND TYPE CONCERNS //
    //////////////////////////////
  
  private void verifyDeclaration(Declaration declaration) throws TomException {
     { Declaration tom_match12_1 = null; tom_match12_1 = (Declaration) declaration;matchlab_match12_pattern1: { String tomName = null; if(tom_is_fun_sym_SymbolDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; tom_match12_1_1 = (TomName) tom_get_slot_SymbolDecl_astName(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%op", tomSymbol);
       } }}matchlab_match12_pattern2: { String tomName = null; if(tom_is_fun_sym_ArraySymbolDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; tom_match12_1_1 = (TomName) tom_get_slot_ArraySymbolDecl_astName(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oparray", tomSymbol);
       } }}matchlab_match12_pattern3: { String tomName = null; if(tom_is_fun_sym_ListSymbolDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; tom_match12_1_1 = (TomName) tom_get_slot_ListSymbolDecl_astName(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oplist", tomSymbol);
       } }}matchlab_match12_pattern4: { TomList tomList = null; Option orgTrack = null; String tomName = null; if(tom_is_fun_sym_TypeTermDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; TomList tom_match12_1_2 = null; Option tom_match12_1_3 = null; tom_match12_1_1 = (TomName) tom_get_slot_TypeTermDecl_astName(tom_match12_1); tom_match12_1_2 = (TomList) tom_get_slot_TypeTermDecl_keywordList(tom_match12_1); tom_match12_1_3 = (Option) tom_get_slot_TypeTermDecl_orgTrack(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1; tomList = (TomList) tom_match12_1_2; orgTrack = (Option) tom_match12_1_3;
 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typeterm", tomList);
       } }}matchlab_match12_pattern5: { Option orgTrack = null; String tomName = null; TomList tomList = null; if(tom_is_fun_sym_TypeListDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; TomList tom_match12_1_2 = null; Option tom_match12_1_3 = null; tom_match12_1_1 = (TomName) tom_get_slot_TypeListDecl_astName(tom_match12_1); tom_match12_1_2 = (TomList) tom_get_slot_TypeListDecl_keywordList(tom_match12_1); tom_match12_1_3 = (Option) tom_get_slot_TypeListDecl_orgTrack(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1; tomList = (TomList) tom_match12_1_2; orgTrack = (Option) tom_match12_1_3;

 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typelist", tomList);
       } }}matchlab_match12_pattern6: { String tomName = null; TomList tomList = null; Option orgTrack = null; if(tom_is_fun_sym_TypeArrayDecl(tom_match12_1)) { TomName tom_match12_1_1 = null; TomList tom_match12_1_2 = null; Option tom_match12_1_3 = null; tom_match12_1_1 = (TomName) tom_get_slot_TypeArrayDecl_astName(tom_match12_1); tom_match12_1_2 = (TomList) tom_get_slot_TypeArrayDecl_keywordList(tom_match12_1); tom_match12_1_3 = (Option) tom_get_slot_TypeArrayDecl_orgTrack(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); tomName = (String) tom_match12_1_1_1; tomList = (TomList) tom_match12_1_2; orgTrack = (Option) tom_match12_1_3;

 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typearray", tomList);
       } }} }
 
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
       { TomTerm tom_match13_1 = null; tom_match13_1 = (TomTerm) term;matchlab_match13_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TargetLanguage tom_match13_1_1_2 = null; Option tom_match13_1_1_3 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match13_1_1); tom_match13_1_1_2 = (TargetLanguage) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match13_1_1); tom_match13_1_1_3 = (Option) tom_get_slot_GetFunctionSymbolDecl_orgTrack(tom_match13_1_1); orgTrack = (Option) tom_match13_1_1_3;
 
          checkField("get_fun_sym",verifyList,orgTrack, declType);
         } }}matchlab_match13_pattern2: { String name2 = null; String name1 = null; Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TomTerm tom_match13_1_1_2 = null; TargetLanguage tom_match13_1_1_3 = null; Option tom_match13_1_1_4 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match13_1_1); tom_match13_1_1_2 = (TomTerm) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match13_1_1); tom_match13_1_1_3 = (TargetLanguage) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match13_1_1); tom_match13_1_1_4 = (Option) tom_get_slot_CompareFunctionSymbolDecl_orgTrack(tom_match13_1_1); if(tom_is_fun_sym_Variable(tom_match13_1_1_1)) { Option tom_match13_1_1_1_1 = null; TomName tom_match13_1_1_1_2 = null; TomType tom_match13_1_1_1_3 = null; tom_match13_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_1); tom_match13_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_1); tom_match13_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_1); if(tom_is_fun_sym_Name(tom_match13_1_1_1_2)) { String tom_match13_1_1_1_2_1 = null; tom_match13_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_1_2); name1 = (String) tom_match13_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match13_1_1_2)) { Option tom_match13_1_1_2_1 = null; TomName tom_match13_1_1_2_2 = null; TomType tom_match13_1_1_2_3 = null; tom_match13_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_2); tom_match13_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_2); tom_match13_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_2); if(tom_is_fun_sym_Name(tom_match13_1_1_2_2)) { String tom_match13_1_1_2_2_1 = null; tom_match13_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_2_2); name2 = (String) tom_match13_1_1_2_2_1; orgTrack = (Option) tom_match13_1_1_4;
 
          checkFieldAndLinearArgs("cmp_fun_sym",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match13_pattern3: { Option orgTrack = null; String name1 = null; String name2 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_TermsEqualDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TomTerm tom_match13_1_1_2 = null; TargetLanguage tom_match13_1_1_3 = null; Option tom_match13_1_1_4 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_TermsEqualDecl_termArg1(tom_match13_1_1); tom_match13_1_1_2 = (TomTerm) tom_get_slot_TermsEqualDecl_termArg2(tom_match13_1_1); tom_match13_1_1_3 = (TargetLanguage) tom_get_slot_TermsEqualDecl_tlCode(tom_match13_1_1); tom_match13_1_1_4 = (Option) tom_get_slot_TermsEqualDecl_orgTrack(tom_match13_1_1); if(tom_is_fun_sym_Variable(tom_match13_1_1_1)) { Option tom_match13_1_1_1_1 = null; TomName tom_match13_1_1_1_2 = null; TomType tom_match13_1_1_1_3 = null; tom_match13_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_1); tom_match13_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_1); tom_match13_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_1); if(tom_is_fun_sym_Name(tom_match13_1_1_1_2)) { String tom_match13_1_1_1_2_1 = null; tom_match13_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_1_2); name1 = (String) tom_match13_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match13_1_1_2)) { Option tom_match13_1_1_2_1 = null; TomName tom_match13_1_1_2_2 = null; TomType tom_match13_1_1_2_3 = null; tom_match13_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_2); tom_match13_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_2); tom_match13_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_2); if(tom_is_fun_sym_Name(tom_match13_1_1_2_2)) { String tom_match13_1_1_2_2_1 = null; tom_match13_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_2_2); name2 = (String) tom_match13_1_1_2_2_1; orgTrack = (Option) tom_match13_1_1_4;
 
          checkFieldAndLinearArgs("equals",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match13_pattern4: { Option orgTrack = null; String name2 = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetSubtermDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TomTerm tom_match13_1_1_2 = null; TargetLanguage tom_match13_1_1_3 = null; Option tom_match13_1_1_4 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetSubtermDecl_termArg(tom_match13_1_1); tom_match13_1_1_2 = (TomTerm) tom_get_slot_GetSubtermDecl_numberArg(tom_match13_1_1); tom_match13_1_1_3 = (TargetLanguage) tom_get_slot_GetSubtermDecl_tlCode(tom_match13_1_1); tom_match13_1_1_4 = (Option) tom_get_slot_GetSubtermDecl_orgTrack(tom_match13_1_1); if(tom_is_fun_sym_Variable(tom_match13_1_1_1)) { Option tom_match13_1_1_1_1 = null; TomName tom_match13_1_1_1_2 = null; TomType tom_match13_1_1_1_3 = null; tom_match13_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_1); tom_match13_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_1); tom_match13_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_1); if(tom_is_fun_sym_Name(tom_match13_1_1_1_2)) { String tom_match13_1_1_1_2_1 = null; tom_match13_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_1_2); name1 = (String) tom_match13_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match13_1_1_2)) { Option tom_match13_1_1_2_1 = null; TomName tom_match13_1_1_2_2 = null; TomType tom_match13_1_1_2_3 = null; tom_match13_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_2); tom_match13_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_2); tom_match13_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_2); if(tom_is_fun_sym_Name(tom_match13_1_1_2_2)) { String tom_match13_1_1_2_2_1 = null; tom_match13_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_2_2); name2 = (String) tom_match13_1_1_2_2_1; orgTrack = (Option) tom_match13_1_1_4;

 
          checkFieldAndLinearArgs("get_subterm",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match13_pattern5: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetHeadDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TargetLanguage tom_match13_1_1_2 = null; Option tom_match13_1_1_3 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetHeadDecl_var(tom_match13_1_1); tom_match13_1_1_2 = (TargetLanguage) tom_get_slot_GetHeadDecl_tlcode(tom_match13_1_1); tom_match13_1_1_3 = (Option) tom_get_slot_GetHeadDecl_orgTrack(tom_match13_1_1); orgTrack = (Option) tom_match13_1_1_3;

 
          checkField("get_head",verifyList,orgTrack, declType);
         } }}matchlab_match13_pattern6: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetTailDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TargetLanguage tom_match13_1_1_2 = null; Option tom_match13_1_1_3 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetTailDecl_var(tom_match13_1_1); tom_match13_1_1_2 = (TargetLanguage) tom_get_slot_GetTailDecl_tlcode(tom_match13_1_1); tom_match13_1_1_3 = (Option) tom_get_slot_GetTailDecl_orgTrack(tom_match13_1_1); orgTrack = (Option) tom_match13_1_1_3;
 
          checkField("get_tail",verifyList,orgTrack, declType);
         } }}matchlab_match13_pattern7: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_IsEmptyDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TargetLanguage tom_match13_1_1_2 = null; Option tom_match13_1_1_3 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_IsEmptyDecl_var(tom_match13_1_1); tom_match13_1_1_2 = (TargetLanguage) tom_get_slot_IsEmptyDecl_tlcode(tom_match13_1_1); tom_match13_1_1_3 = (Option) tom_get_slot_IsEmptyDecl_orgTrack(tom_match13_1_1); orgTrack = (Option) tom_match13_1_1_3;
 
          checkField("is_empty",verifyList,orgTrack, declType);
         } }}matchlab_match13_pattern8: { String name2 = null; Option orgTrack = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetElementDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TomTerm tom_match13_1_1_2 = null; TargetLanguage tom_match13_1_1_3 = null; Option tom_match13_1_1_4 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetElementDecl_kid1(tom_match13_1_1); tom_match13_1_1_2 = (TomTerm) tom_get_slot_GetElementDecl_kid2(tom_match13_1_1); tom_match13_1_1_3 = (TargetLanguage) tom_get_slot_GetElementDecl_tlCode(tom_match13_1_1); tom_match13_1_1_4 = (Option) tom_get_slot_GetElementDecl_orgTrack(tom_match13_1_1); if(tom_is_fun_sym_Variable(tom_match13_1_1_1)) { Option tom_match13_1_1_1_1 = null; TomName tom_match13_1_1_1_2 = null; TomType tom_match13_1_1_1_3 = null; tom_match13_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_1); tom_match13_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_1); tom_match13_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_1); if(tom_is_fun_sym_Name(tom_match13_1_1_1_2)) { String tom_match13_1_1_1_2_1 = null; tom_match13_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_1_2); name1 = (String) tom_match13_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match13_1_1_2)) { Option tom_match13_1_1_2_1 = null; TomName tom_match13_1_1_2_2 = null; TomType tom_match13_1_1_2_3 = null; tom_match13_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match13_1_1_2); tom_match13_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1_1_2); tom_match13_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1_1_2); if(tom_is_fun_sym_Name(tom_match13_1_1_2_2)) { String tom_match13_1_1_2_2_1 = null; tom_match13_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match13_1_1_2_2); name2 = (String) tom_match13_1_1_2_2_1; orgTrack = (Option) tom_match13_1_1_4;

  
          checkFieldAndLinearArgs("get_element",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match13_pattern9: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match13_1)) { Declaration tom_match13_1_1 = null; tom_match13_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match13_1); if(tom_is_fun_sym_GetSizeDecl(tom_match13_1_1)) { TomTerm tom_match13_1_1_1 = null; TargetLanguage tom_match13_1_1_2 = null; Option tom_match13_1_1_3 = null; tom_match13_1_1_1 = (TomTerm) tom_get_slot_GetSizeDecl_kid1(tom_match13_1_1); tom_match13_1_1_2 = (TargetLanguage) tom_get_slot_GetSizeDecl_tlCode(tom_match13_1_1); tom_match13_1_1_3 = (Option) tom_get_slot_GetSizeDecl_orgTrack(tom_match13_1_1); orgTrack = (Option) tom_match13_1_1_3;
 
          checkField("get_size",verifyList,orgTrack, declType);
         } }} }
 
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
       { TomTerm tom_match14_1 = null; tom_match14_1 = (TomTerm) type;matchlab_match14_pattern1: { TomType type1 = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match14_1)) { TomType tom_match14_1_1 = null; tom_match14_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match14_1); type1 = (TomType) tom_match14_1_1;
 
          nbArgs++;
          verifyTypeExist(type1.getString(), nbArgs, symbName, symbLine);
         }} }
 
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
       { Option tom_match15_1 = null; tom_match15_1 = (Option) term;matchlab_match15_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match15_1)) { Declaration tom_match15_1_1 = null; tom_match15_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match15_1); if(tom_is_fun_sym_MakeEmptyArray(tom_match15_1_1)) { TomName tom_match15_1_1_1 = null; TomTerm tom_match15_1_1_2 = null; TargetLanguage tom_match15_1_1_3 = null; Option tom_match15_1_1_4 = null; tom_match15_1_1_1 = (TomName) tom_get_slot_MakeEmptyArray_astName(tom_match15_1_1); tom_match15_1_1_2 = (TomTerm) tom_get_slot_MakeEmptyArray_varSize(tom_match15_1_1); tom_match15_1_1_3 = (TargetLanguage) tom_get_slot_MakeEmptyArray_tlCode(tom_match15_1_1); tom_match15_1_1_4 = (Option) tom_get_slot_MakeEmptyArray_orgTrack(tom_match15_1_1); orgTrack = (Option) tom_match15_1_1_4;

  
          checkField("make_empty",verifyList,orgTrack, symbType);
         } }}matchlab_match15_pattern2: { Option orgTrack = null; String name1 = null; String name2 = null; if(tom_is_fun_sym_DeclarationToOption(tom_match15_1)) { Declaration tom_match15_1_1 = null; tom_match15_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match15_1); if(tom_is_fun_sym_MakeAddArray(tom_match15_1_1)) { TomName tom_match15_1_1_1 = null; TomTerm tom_match15_1_1_2 = null; TomTerm tom_match15_1_1_3 = null; TargetLanguage tom_match15_1_1_4 = null; Option tom_match15_1_1_5 = null; tom_match15_1_1_1 = (TomName) tom_get_slot_MakeAddArray_astName(tom_match15_1_1); tom_match15_1_1_2 = (TomTerm) tom_get_slot_MakeAddArray_varElt(tom_match15_1_1); tom_match15_1_1_3 = (TomTerm) tom_get_slot_MakeAddArray_varList(tom_match15_1_1); tom_match15_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddArray_tlCode(tom_match15_1_1); tom_match15_1_1_5 = (Option) tom_get_slot_MakeAddArray_orgTrack(tom_match15_1_1); if(tom_is_fun_sym_Variable(tom_match15_1_1_2)) { Option tom_match15_1_1_2_1 = null; TomName tom_match15_1_1_2_2 = null; TomType tom_match15_1_1_2_3 = null; tom_match15_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match15_1_1_2); tom_match15_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match15_1_1_2); tom_match15_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match15_1_1_2); if(tom_is_fun_sym_Name(tom_match15_1_1_2_2)) { String tom_match15_1_1_2_2_1 = null; tom_match15_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match15_1_1_2_2); name2 = (String) tom_match15_1_1_2_2_1; if(tom_is_fun_sym_Variable(tom_match15_1_1_3)) { Option tom_match15_1_1_3_1 = null; TomName tom_match15_1_1_3_2 = null; TomType tom_match15_1_1_3_3 = null; tom_match15_1_1_3_1 = (Option) tom_get_slot_Variable_option(tom_match15_1_1_3); tom_match15_1_1_3_2 = (TomName) tom_get_slot_Variable_astName(tom_match15_1_1_3); tom_match15_1_1_3_3 = (TomType) tom_get_slot_Variable_astType(tom_match15_1_1_3); if(tom_is_fun_sym_Name(tom_match15_1_1_3_2)) { String tom_match15_1_1_3_2_1 = null; tom_match15_1_1_3_2_1 = (String) tom_get_slot_Name_string(tom_match15_1_1_3_2); name1 = (String) tom_match15_1_1_3_2_1; orgTrack = (Option) tom_match15_1_1_5;
 
          checkFieldAndLinearArgs("make_append", verifyList, orgTrack, name1, name2, symbType);
         } } } } } }}matchlab_match15_pattern3: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match15_1)) { Declaration tom_match15_1_1 = null; tom_match15_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match15_1); if(tom_is_fun_sym_MakeEmptyList(tom_match15_1_1)) { TomName tom_match15_1_1_1 = null; TargetLanguage tom_match15_1_1_2 = null; Option tom_match15_1_1_3 = null; tom_match15_1_1_1 = (TomName) tom_get_slot_MakeEmptyList_astName(tom_match15_1_1); tom_match15_1_1_2 = (TargetLanguage) tom_get_slot_MakeEmptyList_tlCode(tom_match15_1_1); tom_match15_1_1_3 = (Option) tom_get_slot_MakeEmptyList_orgTrack(tom_match15_1_1); orgTrack = (Option) tom_match15_1_1_3;

 
          checkField("make_empty",verifyList,orgTrack, symbType);
          
         } }}matchlab_match15_pattern4: { String name2 = null; String name1 = null; Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match15_1)) { Declaration tom_match15_1_1 = null; tom_match15_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match15_1); if(tom_is_fun_sym_MakeAddList(tom_match15_1_1)) { TomName tom_match15_1_1_1 = null; TomTerm tom_match15_1_1_2 = null; TomTerm tom_match15_1_1_3 = null; TargetLanguage tom_match15_1_1_4 = null; Option tom_match15_1_1_5 = null; tom_match15_1_1_1 = (TomName) tom_get_slot_MakeAddList_astName(tom_match15_1_1); tom_match15_1_1_2 = (TomTerm) tom_get_slot_MakeAddList_varElt(tom_match15_1_1); tom_match15_1_1_3 = (TomTerm) tom_get_slot_MakeAddList_varList(tom_match15_1_1); tom_match15_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddList_tlCode(tom_match15_1_1); tom_match15_1_1_5 = (Option) tom_get_slot_MakeAddList_orgTrack(tom_match15_1_1); if(tom_is_fun_sym_Variable(tom_match15_1_1_2)) { Option tom_match15_1_1_2_1 = null; TomName tom_match15_1_1_2_2 = null; TomType tom_match15_1_1_2_3 = null; tom_match15_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match15_1_1_2); tom_match15_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match15_1_1_2); tom_match15_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match15_1_1_2); if(tom_is_fun_sym_Name(tom_match15_1_1_2_2)) { String tom_match15_1_1_2_2_1 = null; tom_match15_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match15_1_1_2_2); name2 = (String) tom_match15_1_1_2_2_1; if(tom_is_fun_sym_Variable(tom_match15_1_1_3)) { Option tom_match15_1_1_3_1 = null; TomName tom_match15_1_1_3_2 = null; TomType tom_match15_1_1_3_3 = null; tom_match15_1_1_3_1 = (Option) tom_get_slot_Variable_option(tom_match15_1_1_3); tom_match15_1_1_3_2 = (TomName) tom_get_slot_Variable_astName(tom_match15_1_1_3); tom_match15_1_1_3_3 = (TomType) tom_get_slot_Variable_astType(tom_match15_1_1_3); if(tom_is_fun_sym_Name(tom_match15_1_1_3_2)) { String tom_match15_1_1_3_2_1 = null; tom_match15_1_1_3_2_1 = (String) tom_get_slot_Name_string(tom_match15_1_1_3_2); name1 = (String) tom_match15_1_1_3_2_1; orgTrack = (Option) tom_match15_1_1_5;
 
          checkFieldAndLinearArgs("make_insert", verifyList, orgTrack, name1, name2, symbType);
         } } } } } }}matchlab_match15_pattern5: { String nameSymbol = null; Option orgTrack = null; TomList makeArgsList = null; if(tom_is_fun_sym_DeclarationToOption(tom_match15_1)) { Declaration tom_match15_1_1 = null; tom_match15_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match15_1); if(tom_is_fun_sym_MakeDecl(tom_match15_1_1)) { TomName tom_match15_1_1_1 = null; TomType tom_match15_1_1_2 = null; TomList tom_match15_1_1_3 = null; TargetLanguage tom_match15_1_1_4 = null; Option tom_match15_1_1_5 = null; tom_match15_1_1_1 = (TomName) tom_get_slot_MakeDecl_astName(tom_match15_1_1); tom_match15_1_1_2 = (TomType) tom_get_slot_MakeDecl_astType(tom_match15_1_1); tom_match15_1_1_3 = (TomList) tom_get_slot_MakeDecl_args(tom_match15_1_1); tom_match15_1_1_4 = (TargetLanguage) tom_get_slot_MakeDecl_tlCode(tom_match15_1_1); tom_match15_1_1_5 = (Option) tom_get_slot_MakeDecl_orgTrack(tom_match15_1_1); if(tom_is_fun_sym_Name(tom_match15_1_1_1)) { String tom_match15_1_1_1_1 = null; tom_match15_1_1_1_1 = (String) tom_get_slot_Name_string(tom_match15_1_1_1); nameSymbol = (String) tom_match15_1_1_1_1; makeArgsList = (TomList) tom_match15_1_1_3; orgTrack = (Option) tom_match15_1_1_5;

 
          checkField("make", verifyList, orgTrack, symbType);
          verifyMakeDeclArgs(makeArgsList, expectedNbMakeArgs, orgTrack, symbType);
         } } }} }
 
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
       { TomTerm tom_match16_1 = null; tom_match16_1 = (TomTerm) termVar;matchlab_match16_pattern1: { OptionList listOption = null; String name = null; if(tom_is_fun_sym_Variable(tom_match16_1)) { Option tom_match16_1_1 = null; TomName tom_match16_1_2 = null; TomType tom_match16_1_3 = null; tom_match16_1_1 = (Option) tom_get_slot_Variable_option(tom_match16_1); tom_match16_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match16_1); tom_match16_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match16_1); if(tom_is_fun_sym_Option(tom_match16_1_1)) { OptionList tom_match16_1_1_1 = null; tom_match16_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match16_1_1); listOption = (OptionList) tom_match16_1_1_1; if(tom_is_fun_sym_Name(tom_match16_1_2)) { String tom_match16_1_2_1 = null; tom_match16_1_2_1 = (String) tom_get_slot_Name_string(tom_match16_1_2); name = (String) tom_match16_1_2_1;
 
          if(listVar.contains(name)) {
            messageTwoSameNameVariableError("make", name, orgTrack, symbType);
          } else {
            listVar.add(name);
          }
         } } }} }
 
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
     { TomTerm tom_match17_1 = null; tom_match17_1 = (TomTerm) term;matchlab_match17_pattern1: { TomList argsList = null; String name = null; OptionList options = null; if(tom_is_fun_sym_Appl(tom_match17_1)) { Option tom_match17_1_1 = null; TomName tom_match17_1_2 = null; TomList tom_match17_1_3 = null; tom_match17_1_1 = (Option) tom_get_slot_Appl_option(tom_match17_1); tom_match17_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match17_1); tom_match17_1_3 = (TomList) tom_get_slot_Appl_args(tom_match17_1); if(tom_is_fun_sym_Option(tom_match17_1_1)) { OptionList tom_match17_1_1_1 = null; tom_match17_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match17_1_1); options = (OptionList) tom_match17_1_1_1; if(tom_is_fun_sym_Name(tom_match17_1_2)) { String tom_match17_1_2_1 = null; tom_match17_1_2_1 = (String) tom_get_slot_Name_string(tom_match17_1_2); name = (String) tom_match17_1_2_1; argsList = (TomList) tom_match17_1_3;
 
        testApplStructure(options, name, argsList);
        return;
       } } }}matchlab_match17_pattern2: { String name = null; OptionList options = null; TomList argsList = null; if(tom_is_fun_sym_RecordAppl(tom_match17_1)) { Option tom_match17_1_1 = null; TomName tom_match17_1_2 = null; TomList tom_match17_1_3 = null; tom_match17_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match17_1); tom_match17_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match17_1); tom_match17_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match17_1); if(tom_is_fun_sym_Option(tom_match17_1_1)) { OptionList tom_match17_1_1_1 = null; tom_match17_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match17_1_1); options = (OptionList) tom_match17_1_1_1; if(tom_is_fun_sym_Name(tom_match17_1_2)) { String tom_match17_1_2_1 = null; tom_match17_1_2_1 = (String) tom_get_slot_Name_string(tom_match17_1_2); name = (String) tom_match17_1_2_1; argsList = (TomList) tom_match17_1_3;
 
        testRecordStructure(options, name, argsList);
        return;
       } } }}matchlab_match17_pattern3: {
 
        System.out.println("Strange testTerm "+ term);
      } }
 
  }
  
    /*
      1. We test if the arguments number of one method is right wrt. its definition.
      2. We test if the types of arguments in one method is right wrt. its definition.
      3. We test if a VariableStar is authorized when it is used.
      termAppl is the Appl structure in question.
    */
  private void testApplStructure(TomTerm termAppl) throws TomException {
     { TomTerm tom_match18_1 = null; tom_match18_1 = (TomTerm) termAppl;matchlab_match18_pattern1: { String name = null; OptionList options = null; TomList argsList = null; if(tom_is_fun_sym_Appl(tom_match18_1)) { Option tom_match18_1_1 = null; TomName tom_match18_1_2 = null; TomList tom_match18_1_3 = null; tom_match18_1_1 = (Option) tom_get_slot_Appl_option(tom_match18_1); tom_match18_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match18_1); tom_match18_1_3 = (TomList) tom_get_slot_Appl_args(tom_match18_1); if(tom_is_fun_sym_Option(tom_match18_1_1)) { OptionList tom_match18_1_1_1 = null; tom_match18_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match18_1_1); options = (OptionList) tom_match18_1_1_1; if(tom_is_fun_sym_Name(tom_match18_1_2)) { String tom_match18_1_2_1 = null; tom_match18_1_2_1 = (String) tom_get_slot_Name_string(tom_match18_1_2); name = (String) tom_match18_1_2_1; argsList = (TomList) tom_match18_1_3;
 
          testApplStructure(options, name, argsList);
       } } }} }
 
  }
  private void testApplStructureRhsRule(TomTerm termAppl) throws TomException {
     { TomTerm tom_match19_1 = null; tom_match19_1 = (TomTerm) termAppl;matchlab_match19_pattern1: { OptionList options = null; TomList argsList = null; String name = null; if(tom_is_fun_sym_Appl(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomList tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_Appl_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match19_1); tom_match19_1_3 = (TomList) tom_get_slot_Appl_args(tom_match19_1); if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); options = (OptionList) tom_match19_1_1_1; if(tom_is_fun_sym_Name(tom_match19_1_2)) { String tom_match19_1_2_1 = null; tom_match19_1_2_1 = (String) tom_get_slot_Name_string(tom_match19_1_2); name = (String) tom_match19_1_2_1; argsList = (TomList) tom_match19_1_3;
 
          testApplStructureRhsRule(options, name, argsList);
       } } }} }
 
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
      TomList infoTypeIn = getSymbolDomain(symbol);
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
           { TomTerm tom_match20_1 = null; tom_match20_1 = (TomTerm) term;matchlab_match20_pattern1: { String name1 = null; if(tom_is_fun_sym_Appl(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomList tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_Appl_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match20_1); tom_match20_1_3 = (TomList) tom_get_slot_Appl_args(tom_match20_1); if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); name1 = (String) tom_match20_1_2_1;
 
              testApplStructure(term);
              tabTypeOut[i] = getSymbolCodomain(symbolTable().getSymbol(name1));
              tabNameOut[i] = name1;
             } }}matchlab_match20_pattern2: { if(tom_is_fun_sym_Placeholder(tom_match20_1)) { Option tom_match20_1_1 = null; tom_match20_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match20_1);

  
              tabTypeOut[i] = tom_make_EmptyType() ;
              tabNameOut[i] = noNameTypeOut;
             }}matchlab_match20_pattern3: { String name1 = null; if(tom_is_fun_sym_VariableStar(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomType tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match20_1); tom_match20_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match20_1); if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); name1 = (String) tom_match20_1_2_1;




 
              if(arrayOrList ) {
                tabTypeOut[i] = infoTypeIn.getHead().getAstType();
                tabNameOut[i] = name1;
              } else {
                String line = findOriginTrackingLine(name,list);
                messageErrorVariableStar(name1,name,line);
              }
             } }}matchlab_match20_pattern4: { String name2 = null; TomList args = null; OptionList options = null; if(tom_is_fun_sym_RecordAppl(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomList tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match20_1); tom_match20_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match20_1); if(tom_is_fun_sym_Option(tom_match20_1_1)) { OptionList tom_match20_1_1_1 = null; tom_match20_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match20_1_1); options = (OptionList) tom_match20_1_1_1; if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); name2 = (String) tom_match20_1_2_1; args = (TomList) tom_match20_1_3;
 
              testRecordStructure(options, name2, args);
              tabTypeOut[i] = getSymbolCodomain(symbolTable().getSymbol(name2));
              tabNameOut[i] = name;
             } } }} }
 
        }
        
          /*
            we test the correspondence of arguments between needed types (stocked in
            infoTypeIn) and given types (stocked in infoTypeOut)
          */
        for(int i=0 ; i<arity ; i++) {
          TomType oneIn = infoTypeIn.getHead().getAstType();
          if (tabTypeOut[i] != tom_make_EmptyType()  && oneIn!=tabTypeOut[i]) {
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
        // it is a variable
    } else if( symbol==null && !argsList.isEmpty() ) {
        //System.out.println("Warning: Rhs of rule contains a function call? ensure it is correct"+name);
    } else {
        // we extract the needed types for the arguments of this Appl.
      TomList infoTypeIn = getSymbolDomain(symbol);
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
           { TomTerm tom_match21_1 = null; tom_match21_1 = (TomTerm) term;matchlab_match21_pattern1: { String name1 = null; if(tom_is_fun_sym_Appl(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomList tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_Appl_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match21_1); tom_match21_1_3 = (TomList) tom_get_slot_Appl_args(tom_match21_1); if(tom_is_fun_sym_Name(tom_match21_1_2)) { String tom_match21_1_2_1 = null; tom_match21_1_2_1 = (String) tom_get_slot_Name_string(tom_match21_1_2); name1 = (String) tom_match21_1_2_1;
 
              testApplStructure(term);
              tabTypeOut[i] = getSymbolCodomain(symbolTable().getSymbol(name1));
              tabNameOut[i] = name1;
             } }}matchlab_match21_pattern2: { if(tom_is_fun_sym_Placeholder(tom_match21_1)) { Option tom_match21_1_1 = null; tom_match21_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match21_1);

  
              tabTypeOut[i] = tom_make_EmptyType() ;
              tabNameOut[i] = noNameTypeOut;
             }}matchlab_match21_pattern3: { String name1 = null; if(tom_is_fun_sym_VariableStar(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomType tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match21_1); tom_match21_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match21_1); if(tom_is_fun_sym_Name(tom_match21_1_2)) { String tom_match21_1_2_1 = null; tom_match21_1_2_1 = (String) tom_get_slot_Name_string(tom_match21_1_2); name1 = (String) tom_match21_1_2_1;




 
              if(arrayOrList ) {
                tabTypeOut[i] = infoTypeIn.getHead().getAstType();
                tabNameOut[i] = name1;
              } else {
                String line = findOriginTrackingLine(name,list);
                messageErrorVariableStar(name1,name,line);
              }
             } }}matchlab_match21_pattern4: { TomList args = null; OptionList options = null; String name2 = null; if(tom_is_fun_sym_RecordAppl(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomList tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match21_1); tom_match21_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match21_1); if(tom_is_fun_sym_Option(tom_match21_1_1)) { OptionList tom_match21_1_1_1 = null; tom_match21_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match21_1_1); options = (OptionList) tom_match21_1_1_1; if(tom_is_fun_sym_Name(tom_match21_1_2)) { String tom_match21_1_2_1 = null; tom_match21_1_2_1 = (String) tom_get_slot_Name_string(tom_match21_1_2); name2 = (String) tom_match21_1_2_1; args = (TomList) tom_match21_1_3;
 
              testRecordStructure(options, name2, args);
              tabTypeOut[i] = getSymbolCodomain(symbolTable().getSymbol(name2));
              tabNameOut[i] = name;
             } } }} }
 
        }
        
          /*
            we test the correspondence of arguments between needed types (stocked in
            infoTypeIn) and given types (stocked in infoTypeOut)
          */
        for(int i=0 ; i<arity ; i++) {
          TomType oneIn = infoTypeIn.getHead().getAstType();
          if (tabTypeOut[i] != tom_make_EmptyType()  && oneIn!=tabTypeOut[i]) {
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
     { TomTerm tom_match22_1 = null; tom_match22_1 = (TomTerm) record;matchlab_match22_pattern1: { OptionList options = null; String name = null; TomList args = null; if(tom_is_fun_sym_RecordAppl(tom_match22_1)) { Option tom_match22_1_1 = null; TomName tom_match22_1_2 = null; TomList tom_match22_1_3 = null; tom_match22_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match22_1); tom_match22_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match22_1); tom_match22_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match22_1); if(tom_is_fun_sym_Option(tom_match22_1_1)) { OptionList tom_match22_1_1_1 = null; tom_match22_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match22_1_1); options = (OptionList) tom_match22_1_1_1; if(tom_is_fun_sym_Name(tom_match22_1_2)) { String tom_match22_1_2_1 = null; tom_match22_1_2_1 = (String) tom_get_slot_Name_string(tom_match22_1_2); name = (String) tom_match22_1_2_1; args = (TomList) tom_match22_1_3;
 testRecordStructure(options, name, args);  } } }} }
 
  }
  private void testRecordStructure(OptionList option, String tomName, TomList args) throws TomException {
    TomSymbol symbol = symbolTable().getSymbol(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // list operator and constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
      if(slotList.isEmptySlotList()) {
        messageBracketError(tomName, option);
      }
      testPairSlotNameAndTerm(args,slotList);
    } else {
      messageSymbolError(tomName, option);
    }
  }
  
  
    // We test the existence of slotName contained in pairSlotAppl. and then the associated term
  private void testPairSlotNameAndTerm(TomList listPair, SlotList slotList) throws TomException {
    ArrayList slotIndex = new ArrayList();
    while( !listPair.isEmpty() ) {
      TomTerm pair = listPair.getHead();
      TomName name = pair.getSlotName();
      int index = getSlotIndex(slotList,name.getString());
      Integer index2 = new Integer(index);
      if(index < 0) {
        messageSlotNameError(pair,slotList); 
      } else {
        if(slotIndex.contains(index2)) {
          messageSlotRepeatedError(pair,name.getString());
        }
        slotIndex.add(index2);
        testTerm(pair.getAppl());
      }
      listPair = listPair.getTail();
    }
  }
  
    /////////////////////////
    //   MESSAGE METHODS   //
    /////////////////////////

    /********************************
     * Error messages which generate immediatly an exit
     ********************************/
   // Link directly to Match structure
  private void messageMatchTypeVariableError(String name, String type) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Variable '" + name + "' has a wrong type:  '" + type + "' in %match construct declared line "+lineDecl;
    messageError(lineDecl,s);
  }
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

   // Link directly to Rule structure  
   // this one could be parameter with string %rule or %match
  private void messageRuleErrorVariableStar(String nameVariableStar, String line) throws TomException {
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
    String s = "Bad number of arguments for method '" + name + "':" + nbArg + " arguments are required but " + nbArg2 + " are given";
    messageError(line,s);
  }	
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, Option orgTrack, String symbType) throws TomException {
    String line = "not found";
    String name = "unknown";
    String lineDecl = "not found";
    String nameDecl = "unknown";
     { Option tom_match23_1 = null; Option tom_match23_2 = null; tom_match23_1 = (Option) orgTrack; tom_match23_2 = (Option) currentTomStructureOrgTrack;matchlab_match23_pattern1: { String orgLine = null; String orgName = null; String declOrgName = null; String declOrgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match23_1)) { TomName tom_match23_1_1 = null; TomTerm tom_match23_1_2 = null; tom_match23_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match23_1); tom_match23_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match23_1); if(tom_is_fun_sym_Name(tom_match23_1_1)) { String tom_match23_1_1_1 = null; tom_match23_1_1_1 = (String) tom_get_slot_Name_string(tom_match23_1_1); orgName = (String) tom_match23_1_1_1; if(tom_is_fun_sym_Line(tom_match23_1_2)) { String tom_match23_1_2_1 = null; tom_match23_1_2_1 = (String) tom_get_slot_Line_string(tom_match23_1_2); orgLine = (String) tom_match23_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match23_2)) { TomName tom_match23_2_1 = null; TomTerm tom_match23_2_2 = null; tom_match23_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match23_2); tom_match23_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match23_2); if(tom_is_fun_sym_Name(tom_match23_2_1)) { String tom_match23_2_1_1 = null; tom_match23_2_1_1 = (String) tom_get_slot_Name_string(tom_match23_2_1); declOrgName = (String) tom_match23_2_1_1; if(tom_is_fun_sym_Line(tom_match23_2_2)) { String tom_match23_2_2_1 = null; tom_match23_2_2_1 = (String) tom_get_slot_Line_string(tom_match23_2_2); declOrgLine = (String) tom_match23_2_2_1;
 
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
       } } } } } }} }
 
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
  
  private void messageBracketError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name,optionList);
    String s = "[] are not allowed on lists or arrays nor constants, see: "+name;
    messageError(line,s);
  }
  
    /************************************
     * Error messages which generate no exit, but the program is wrong    *
     ************************************/
  
  private void messageRuleErrorUnknownVariable(String name, String line) {
    System.out.println(" *** Variable '"+name+"' is strange - Line : "+line+" ***");
    Flags.findErrors = true;
  }
  
  private void messageRuleErrorTypeAndConstructorEgality(String  name, String nameExpected, String type, String typeExpected, OptionList optionList) {
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Error in %rule before '->' - Line : "+line);
    System.out.println(" *** '" + nameExpected + "' of type '" + typeExpected +
                       "' is expected, but '" + name + "' of type '" + type +
                       "' is given");
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
     { Option tom_match24_1 = null; Option tom_match24_2 = null; tom_match24_1 = (Option) orgTrack; tom_match24_2 = (Option) currentTomStructureOrgTrack;matchlab_match24_pattern1: { String orgLine = null; String declOrgName = null; String orgName = null; String declOrgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match24_1)) { TomName tom_match24_1_1 = null; TomTerm tom_match24_1_2 = null; tom_match24_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match24_1); tom_match24_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match24_1); if(tom_is_fun_sym_Name(tom_match24_1_1)) { String tom_match24_1_1_1 = null; tom_match24_1_1_1 = (String) tom_get_slot_Name_string(tom_match24_1_1); orgName = (String) tom_match24_1_1_1; if(tom_is_fun_sym_Line(tom_match24_1_2)) { String tom_match24_1_2_1 = null; tom_match24_1_2_1 = (String) tom_get_slot_Line_string(tom_match24_1_2); orgLine = (String) tom_match24_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match24_2)) { TomName tom_match24_2_1 = null; TomTerm tom_match24_2_2 = null; tom_match24_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match24_2); tom_match24_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match24_2); if(tom_is_fun_sym_Name(tom_match24_2_1)) { String tom_match24_2_1_1 = null; tom_match24_2_1_1 = (String) tom_get_slot_Name_string(tom_match24_2_1); declOrgName = (String) tom_match24_2_1_1; if(tom_is_fun_sym_Line(tom_match24_2_2)) { String tom_match24_2_2_1 = null; tom_match24_2_2_1 = (String) tom_get_slot_Line_string(tom_match24_2_2); declOrgLine = (String) tom_match24_2_2_1;
 
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
       } } } } } }} }
  
    System.out.println("\n *** Repeated macro-functions in declaration '"+declType+" "+nameDecl+"' declared at line "+lineDecl);
    System.out.println(" *** '" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  }   

  private void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
    String line = "not found";
    String name = "unknown";
     { Option tom_match25_1 = null; tom_match25_1 = (Option) currentTomStructureOrgTrack;matchlab_match25_pattern1: { String orgLine = null; String orgName = null; if(tom_is_fun_sym_OriginTracking(tom_match25_1)) { TomName tom_match25_1_1 = null; TomTerm tom_match25_1_2 = null; tom_match25_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match25_1); tom_match25_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match25_1); if(tom_is_fun_sym_Name(tom_match25_1_1)) { String tom_match25_1_1_1 = null; tom_match25_1_1_1 = (String) tom_get_slot_Name_string(tom_match25_1_1); orgName = (String) tom_match25_1_1_1; if(tom_is_fun_sym_Line(tom_match25_1_2)) { String tom_match25_1_2_1 = null; tom_match25_1_2_1 = (String) tom_get_slot_Line_string(tom_match25_1_2); orgLine = (String) tom_match25_1_2_1;
 
        line = orgLine;
        name = orgName;
       } } }} }
 
    System.out.println("\n *** Missing macro-functions for '"+nameConstruct+" "+name+"' declared at line "+line);
    System.out.println(" *** Missing functions : "+list);
    Flags.findErrors = true;
  }
  
  private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType) {
    String line = "not found", name = "unknown", nameDecl = "unknown", lineDecl = "not found";
     { Option tom_match26_1 = null; Option tom_match26_2 = null; tom_match26_1 = (Option) orgTrack; tom_match26_2 = (Option) currentTomStructureOrgTrack;matchlab_match26_pattern1: { String declOrgName = null; String orgName = null; String declOrgLine = null; String orgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match26_1)) { TomName tom_match26_1_1 = null; TomTerm tom_match26_1_2 = null; tom_match26_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match26_1); tom_match26_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match26_1); if(tom_is_fun_sym_Name(tom_match26_1_1)) { String tom_match26_1_1_1 = null; tom_match26_1_1_1 = (String) tom_get_slot_Name_string(tom_match26_1_1); orgName = (String) tom_match26_1_1_1; if(tom_is_fun_sym_Line(tom_match26_1_2)) { String tom_match26_1_2_1 = null; tom_match26_1_2_1 = (String) tom_get_slot_Line_string(tom_match26_1_2); orgLine = (String) tom_match26_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match26_2)) { TomName tom_match26_2_1 = null; TomTerm tom_match26_2_2 = null; tom_match26_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match26_2); tom_match26_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match26_2); if(tom_is_fun_sym_Name(tom_match26_2_1)) { String tom_match26_2_1_1 = null; tom_match26_2_1_1 = (String) tom_get_slot_Name_string(tom_match26_2_1); declOrgName = (String) tom_match26_2_1_1; if(tom_is_fun_sym_Line(tom_match26_2_2)) { String tom_match26_2_2_1 = null; tom_match26_2_2_1 = (String) tom_get_slot_Line_string(tom_match26_2_2); declOrgLine = (String) tom_match26_2_2_1;
 
        lineDecl = declOrgLine;
        nameDecl = declOrgName;
        line = orgLine;
        name = orgName;
       } } } } } }} }
 
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
     { TomTerm tom_match27_1 = null; tom_match27_1 = (TomTerm) pairSlotName;matchlab_match27_pattern1: { OptionList list = null; if(tom_is_fun_sym_PairSlotAppl(tom_match27_1)) { TomName tom_match27_1_1 = null; TomTerm tom_match27_1_2 = null; tom_match27_1_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match27_1); tom_match27_1_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match27_1); if(tom_is_fun_sym_Appl(tom_match27_1_2)) { Option tom_match27_1_2_1 = null; TomName tom_match27_1_2_2 = null; TomList tom_match27_1_2_3 = null; tom_match27_1_2_1 = (Option) tom_get_slot_Appl_option(tom_match27_1_2); tom_match27_1_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match27_1_2); tom_match27_1_2_3 = (TomList) tom_get_slot_Appl_args(tom_match27_1_2); if(tom_is_fun_sym_Option(tom_match27_1_2_1)) { OptionList tom_match27_1_2_1_1 = null; tom_match27_1_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match27_1_2_1); list = (OptionList) tom_match27_1_2_1_1;
 
        String line = findOriginTrackingLine(list);
        System.out.println(" *** Slot Name : '"+name+"' - Line : "+line);
       } } }} }
 
    Flags.findErrors = true;
  }
  
  private void messageSlotNameError(TomTerm pairSlotName, SlotList slotList) {
    ArrayList listOfPossibleSlot = new ArrayList();
    while ( !slotList.isEmptySlotList() ) {
      TomName name = slotList.getHeadSlotList().getSlotName();
      if ( !name.isEmptyName()) listOfPossibleSlot.add(name.getString());
      slotList = slotList.getTailSlotList();
    }
     { TomTerm tom_match28_1 = null; tom_match28_1 = (TomTerm) pairSlotName;matchlab_match28_pattern1: { String name = null; OptionList list = null; if(tom_is_fun_sym_PairSlotAppl(tom_match28_1)) { TomName tom_match28_1_1 = null; TomTerm tom_match28_1_2 = null; tom_match28_1_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match28_1); tom_match28_1_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match28_1); if(tom_is_fun_sym_Name(tom_match28_1_1)) { String tom_match28_1_1_1 = null; tom_match28_1_1_1 = (String) tom_get_slot_Name_string(tom_match28_1_1); name = (String) tom_match28_1_1_1; if(tom_is_fun_sym_Appl(tom_match28_1_2)) { Option tom_match28_1_2_1 = null; TomName tom_match28_1_2_2 = null; TomList tom_match28_1_2_3 = null; tom_match28_1_2_1 = (Option) tom_get_slot_Appl_option(tom_match28_1_2); tom_match28_1_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match28_1_2); tom_match28_1_2_3 = (TomList) tom_get_slot_Appl_args(tom_match28_1_2); if(tom_is_fun_sym_Option(tom_match28_1_2_1)) { OptionList tom_match28_1_2_1_1 = null; tom_match28_1_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match28_1_2_1); list = (OptionList) tom_match28_1_2_1_1;
 
        String line = findOriginTrackingLine(list);
        System.out.println("\n"+" *** Slot Name '" + name + "' is strange"+" -  Line : "+line);
       } } } }} }
 
    System.out.println(" *** Possible Slot Names are : "+listOfPossibleSlot);
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
    System.out.println(" *** Warning *** possible error in right part of %rule declared line "+declLine);
    System.out.println(" *** Type '" + typeExpected + "' expected but symbol '" + name + "' found: Ensure the type coherence by yourself line : " + line);
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
    System.out.println("\n"+" *** Warning *** Bad method argument type");
    System.out.println(oneOut);
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
       { Option tom_match29_1 = null; tom_match29_1 = (Option) subject;matchlab_match29_pattern1: { String line = null; if(tom_is_fun_sym_OriginTracking(tom_match29_1)) { TomName tom_match29_1_1 = null; TomTerm tom_match29_1_2 = null; tom_match29_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match29_1); tom_match29_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match29_1); if(tom_is_fun_sym_Line(tom_match29_1_2)) { String tom_match29_1_2_1 = null; tom_match29_1_2_1 = (String) tom_get_slot_Line_string(tom_match29_1_2); line = (String) tom_match29_1_2_1;
 
          return line;
         } }} }
 
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    System.exit(1);return null;
  }

    // findOriginTracking(_) return the option containing OriginTracking information
  private Option findOriginTracking(OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
       { Option tom_match30_1 = null; tom_match30_1 = (Option) subject;matchlab_match30_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_OriginTracking(tom_match30_1)) { TomName tom_match30_1_1 = null; TomTerm tom_match30_1_2 = null; tom_match30_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match30_1); tom_match30_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match30_1); orgTrack = (Option) tom_match30_1;
 
          return orgTrack;
         }} }
 
      optionList = optionList.getTail();
    }
    System.out.println("findOriginTrackingLine:  not found");
    System.exit(1);return null;
  }

  private String findOriginTrackingLine(Option option) {
    return option.getLine().getString();
  }
  
    // We have a structure contained only an TomList and we return this ATermList.
  private TomList extractList(TomTerm t) {
     { TomTerm tom_match31_1 = null; tom_match31_1 = (TomTerm) t;matchlab_match31_pattern1: { TomList list = null; if(tom_is_fun_sym_SubjectList(tom_match31_1)) { TomList tom_match31_1_1 = null; tom_match31_1_1 = (TomList) tom_get_slot_SubjectList_list(tom_match31_1); list = (TomList) tom_match31_1_1;
  return list;  }}matchlab_match31_pattern2: { TomList list = null; if(tom_is_fun_sym_PatternList(tom_match31_1)) { TomList tom_match31_1_1 = null; tom_match31_1_1 = (TomList) tom_get_slot_PatternList_list(tom_match31_1); list = (TomList) tom_match31_1_1;
  return list;  }}matchlab_match31_pattern3: { TomList list = null; if(tom_is_fun_sym_TermList(tom_match31_1)) { TomList tom_match31_1_1 = null; tom_match31_1_1 = (TomList) tom_get_slot_TermList_list(tom_match31_1); list = (TomList) tom_match31_1_1;
  return list;  }} }
 
    return empty();
  } 
  
  private void extractVariable(TomTerm term, ArrayList nameVariable, ArrayList lineVariable) {
     { TomTerm tom_match32_1 = null; tom_match32_1 = (TomTerm) term;matchlab_match32_pattern1: { String name1 = null; OptionList optionList = null; TomList l = null; if(tom_is_fun_sym_Appl(tom_match32_1)) { Option tom_match32_1_1 = null; TomName tom_match32_1_2 = null; TomList tom_match32_1_3 = null; tom_match32_1_1 = (Option) tom_get_slot_Appl_option(tom_match32_1); tom_match32_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match32_1); tom_match32_1_3 = (TomList) tom_get_slot_Appl_args(tom_match32_1); if(tom_is_fun_sym_Option(tom_match32_1_1)) { OptionList tom_match32_1_1_1 = null; tom_match32_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match32_1_1); optionList = (OptionList) tom_match32_1_1_1; if(tom_is_fun_sym_Name(tom_match32_1_2)) { String tom_match32_1_2_1 = null; tom_match32_1_2_1 = (String) tom_get_slot_Name_string(tom_match32_1_2); name1 = (String) tom_match32_1_2_1; l = (TomList) tom_match32_1_3;
 
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
          // If l != [] then name1 could not be a variable.
        if ( symbolTable().getSymbol(name1)==null && l.isEmpty() ) { 
          nameVariable.add(name1);
          lineVariable.add(findOriginTrackingLine(name1,optionList));
        }
        else {
            // we extract variables of arguments of this Appl struture.
          extractVariableList(l, nameVariable, lineVariable);
        }        
        return;
       } } }}matchlab_match32_pattern2: { OptionList optionList = null; String tomName = null; TomList pair = null; if(tom_is_fun_sym_RecordAppl(tom_match32_1)) { Option tom_match32_1_1 = null; TomName tom_match32_1_2 = null; TomList tom_match32_1_3 = null; tom_match32_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match32_1); tom_match32_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match32_1); tom_match32_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match32_1); if(tom_is_fun_sym_Option(tom_match32_1_1)) { OptionList tom_match32_1_1_1 = null; tom_match32_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match32_1_1); optionList = (OptionList) tom_match32_1_1_1; if(tom_is_fun_sym_Name(tom_match32_1_2)) { String tom_match32_1_2_1 = null; tom_match32_1_2_1 = (String) tom_get_slot_Name_string(tom_match32_1_2); tomName = (String) tom_match32_1_2_1; pair = (TomList) tom_match32_1_3;
 
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
          // we extract variables of arguments of this Appl struture.
        extractVariablePair(pair, nameVariable, lineVariable);        
       } } }}matchlab_match32_pattern3: { OptionList optionList = null; if(tom_is_fun_sym_Placeholder(tom_match32_1)) { Option tom_match32_1_1 = null; tom_match32_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match32_1); if(tom_is_fun_sym_Option(tom_match32_1_1)) { OptionList tom_match32_1_1_1 = null; tom_match32_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match32_1_1); optionList = (OptionList) tom_match32_1_1_1;
 
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
       } }}matchlab_match32_pattern4: { OptionList optionList = null; if(tom_is_fun_sym_VariableStar(tom_match32_1)) { Option tom_match32_1_1 = null; TomName tom_match32_1_2 = null; TomType tom_match32_1_3 = null; tom_match32_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match32_1); tom_match32_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match32_1); tom_match32_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match32_1); if(tom_is_fun_sym_Option(tom_match32_1_1)) { OptionList tom_match32_1_1_1 = null; tom_match32_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match32_1_1); optionList = (OptionList) tom_match32_1_1_1;

  
        extractAnnotedVariable(optionList, nameVariable, lineVariable);
       } }}matchlab_match32_pattern5: {
  return; } }
 
  }
  
  private void extractAnnotedVariable(OptionList options, ArrayList nameVariable, ArrayList lineVariable) {
    while(!options.isEmptyOptionList()) {
      Option subject = options.getHead();
       { Option tom_match33_1 = null; tom_match33_1 = (Option) subject;matchlab_match33_pattern1: { String name1 = null; if(tom_is_fun_sym_TomNameToOption(tom_match33_1)) { TomName tom_match33_1_1 = null; tom_match33_1_1 = (TomName) tom_get_slot_TomNameToOption_astName(tom_match33_1); if(tom_is_fun_sym_Name(tom_match33_1_1)) { String tom_match33_1_1_1 = null; tom_match33_1_1_1 = (String) tom_get_slot_Name_string(tom_match33_1_1); name1 = (String) tom_match33_1_1_1;
 
          nameVariable.add(name1);
          lineVariable.add("No position info for Annotations");
          break;
         } }} }
 
      options = options.getTail();
    }
  }
  
    // It applies extractVariable on each term of termList.
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
  
    // We returns the result TomType of concerned element.
  private String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }

    /*
      findTypeOf is used in 'testRuleTypeEgality' method of TomVerifier.t.
      we look if an object with name 'name' exists in 'inTerm' which has an Appl/Record Structure.
      and we returns its type.
    */		
  private TomType findTypeOf(String name, TomTerm inTerm) {
      // if no type for 'name' is found, we return EmptyType
    TomType type = tom_make_EmptyType() ;
     { TomTerm tom_match34_1 = null; tom_match34_1 = (TomTerm) inTerm;matchlab_match34_pattern1: { OptionList options = null; String name1 = null; TomList list = null; if(tom_is_fun_sym_Appl(tom_match34_1)) { Option tom_match34_1_1 = null; TomName tom_match34_1_2 = null; TomList tom_match34_1_3 = null; tom_match34_1_1 = (Option) tom_get_slot_Appl_option(tom_match34_1); tom_match34_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match34_1); tom_match34_1_3 = (TomList) tom_get_slot_Appl_args(tom_match34_1); if(tom_is_fun_sym_Option(tom_match34_1_1)) { OptionList tom_match34_1_1_1 = null; tom_match34_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match34_1_1); options = (OptionList) tom_match34_1_1_1; if(tom_is_fun_sym_Name(tom_match34_1_2)) { String tom_match34_1_2_1 = null; tom_match34_1_2_1 = (String) tom_get_slot_Name_string(tom_match34_1_2); name1 = (String) tom_match34_1_2_1; list = (TomList) tom_match34_1_3;
 
          // first we look for the annotations
        type = findAnnotatedTypeof(name, options, name1);
        if ( !type.isEmptyType() ) return type;
          // else we continue
        TomList typeList = empty();
        int numArg = 0;
        boolean find = false;
          // we look if 'name' is an argument and we memorize the position of this argument
        for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
          numArg++;
          TomTerm t = l.getHead();
           { TomTerm tom_match35_1 = null; tom_match35_1 = (TomTerm) t;matchlab_match35_pattern1: { String name2 = null; if(tom_is_fun_sym_Appl(tom_match35_1)) { Option tom_match35_1_1 = null; TomName tom_match35_1_2 = null; TomList tom_match35_1_3 = null; tom_match35_1_1 = (Option) tom_get_slot_Appl_option(tom_match35_1); tom_match35_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match35_1); tom_match35_1_3 = (TomList) tom_get_slot_Appl_args(tom_match35_1); if(tom_is_fun_sym_Name(tom_match35_1_2)) { String tom_match35_1_2_1 = null; tom_match35_1_2_1 = (String) tom_get_slot_Name_string(tom_match35_1_2); name2 = (String) tom_match35_1_2_1;
 
              find = (name2.equals(name));
             } }} }
 
        }
          // if 'name' is an argument, we search the needed argument type thanks to the memorized position.
        if(find) {
          type = findTypeAtPosition(name1, numArg);
        } else {
            // if 'name' is not an argument, we look if 'name' is an argument of methods given in argument.
          for(TomList l=list ; !l.isEmpty() && !find; l=l.getTail()) {
            TomTerm t = l.getHead();
            type = findTypeOf(name, t);
              /*find = true;
                %match(TomType type) {
                  // no type for 'name' found
                  EmptyType() -> { find = false; }
                  }
              */
          }
        }
       } } }}matchlab_match34_pattern2: { String name1 = null; TomList list = null; OptionList options = null; if(tom_is_fun_sym_RecordAppl(tom_match34_1)) { Option tom_match34_1_1 = null; TomName tom_match34_1_2 = null; TomList tom_match34_1_3 = null; tom_match34_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match34_1); tom_match34_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match34_1); tom_match34_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match34_1); if(tom_is_fun_sym_Option(tom_match34_1_1)) { OptionList tom_match34_1_1_1 = null; tom_match34_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match34_1_1); options = (OptionList) tom_match34_1_1_1; if(tom_is_fun_sym_Name(tom_match34_1_2)) { String tom_match34_1_2_1 = null; tom_match34_1_2_1 = (String) tom_get_slot_Name_string(tom_match34_1_2); name1 = (String) tom_match34_1_2_1; list = (TomList) tom_match34_1_3;
 
          // first we look for the annotations
        type = findAnnotatedTypeof(name, options, name1);
        if ( !type.isEmptyType() ) return type;
          // else we continue
        System.out.println("findTypeOf called on record"+inTerm);
       } } }} }
 
    return type;
  }
  
  private TomType findTypeAtPosition(String symbolName, int position) {
    TomList typeList = getSymbolDomain(symbolTable().getSymbol(symbolName));
    for(int i = 1; i < position; i++ ) {
      typeList = typeList.getTail();
    }
    return typeList.getHead().getAstType();
  }
  
  private TomType findTypeAtSlotName(String symbolName, TomName slotName) {
    TomList slotNameTypeList = symbolTable().getSymbol(symbolName).getTypesToType().getList();
    while( !slotNameTypeList.isEmpty() ) {
      TomTerm testedSlotNameType = slotNameTypeList.getHead();
       { TomTerm tom_match36_1 = null; TomName tom_match36_2 = null; tom_match36_1 = (TomTerm) testedSlotNameType; tom_match36_2 = (TomName) slotName;matchlab_match36_pattern1: { TomType type = null; String name1 = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match36_1)) { TomType tom_match36_1_1 = null; tom_match36_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match36_1); type = (TomType) tom_match36_1_1; if(tom_is_fun_sym_Name(tom_match36_2)) { String tom_match36_2_1 = null; tom_match36_2_1 = (String) tom_get_slot_Name_string(tom_match36_2); name1 = (String) tom_match36_2_1;
 
//          if(name.equals(name1)) {
//            return type;
//          }
         } }} }
 
      slotNameTypeList = slotNameTypeList.getTail();
    }
    return tom_make_EmptyType() ;
  }
  
  private TomType findAnnotatedTypeof(String name, OptionList options, String symbolName) {
    while(!options.isEmptyOptionList()) {
      Option subject = options.getHead();
       { Option tom_match37_1 = null; tom_match37_1 = (Option) subject;matchlab_match37_pattern1: { String annotatedName = null; if(tom_is_fun_sym_TomNameToOption(tom_match37_1)) { TomName tom_match37_1_1 = null; tom_match37_1_1 = (TomName) tom_get_slot_TomNameToOption_astName(tom_match37_1); if(tom_is_fun_sym_Name(tom_match37_1_1)) { String tom_match37_1_1_1 = null; tom_match37_1_1_1 = (String) tom_get_slot_Name_string(tom_match37_1_1); annotatedName = (String) tom_match37_1_1_1;
 
          if(annotatedName == name){
            return getSymbolCodomain(symbolTable().getSymbol(symbolName));
          }
          else { break; }
         } }} }
 
      options = options.getTail();
    }
    return null;
  }
  
}
