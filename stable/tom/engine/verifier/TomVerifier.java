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

package jtom.verifier;
 
import java.util.*;

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
                return true;
               } } }}matchlab_match1_pattern2: { Declaration declaration = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match1_1)) { Declaration tom_match1_1_1 = null; tom_match1_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match1_1); declaration = (Declaration) tom_match1_1_1;
 
                verifyDeclaration(declaration);
                return false;
               }}matchlab_match1_pattern3: { String name = null; TomList args = null; OptionList options = null; if(tom_is_fun_sym_Appl(tom_match1_1)) { Option tom_match1_1_1 = null; TomName tom_match1_1_2 = null; TomList tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_Appl_option(tom_match1_1); tom_match1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match1_1); tom_match1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match1_1); if(tom_is_fun_sym_Option(tom_match1_1_1)) { OptionList tom_match1_1_1_1 = null; tom_match1_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match1_1_1); options = (OptionList) tom_match1_1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_2)) { String tom_match1_1_2_1 = null; tom_match1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_2); name = (String) tom_match1_1_2_1; args = (TomList) tom_match1_1_3;
 
                verifyApplStructure(options, name, args);
                return true;
               } } }}matchlab_match1_pattern4: { OptionList options = null; String name = null; TomList args = null; if(tom_is_fun_sym_RecordAppl(tom_match1_1)) { Option tom_match1_1_1 = null; TomName tom_match1_1_2 = null; TomList tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match1_1); tom_match1_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match1_1); tom_match1_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match1_1); if(tom_is_fun_sym_Option(tom_match1_1_1)) { OptionList tom_match1_1_1_1 = null; tom_match1_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match1_1_1); options = (OptionList) tom_match1_1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_2)) { String tom_match1_1_2_1 = null; tom_match1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_2); name = (String) tom_match1_1_2_1; args = (TomList) tom_match1_1_3;
 
                verifyRecordStructure(options, name, args);
                return true;
               } } }}matchlab_match1_pattern5: { TomList list = null; Option orgTrack = null; if(tom_is_fun_sym_RuleSet(tom_match1_1)) { Option tom_match1_1_1 = null; TomList tom_match1_1_2 = null; tom_match1_1_1 = (Option) tom_get_slot_RuleSet_option(tom_match1_1); tom_match1_1_2 = (TomList) tom_get_slot_RuleSet_list(tom_match1_1); orgTrack = (Option) tom_match1_1_1; list = (TomList) tom_match1_1_2;
 
                currentTomStructureOrgTrack = orgTrack;
                verifyRule(list);
                return false;
               }}matchlab_match1_pattern6: { TomTerm t = null; Option orgTrack = null; if(tom_is_fun_sym_BackQuoteTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; Option tom_match1_1_2 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_BackQuoteTerm_term(tom_match1_1); tom_match1_1_2 = (Option) tom_get_slot_BackQuoteTerm_option(tom_match1_1); t = (TomTerm) tom_match1_1_1; orgTrack = (Option) tom_match1_1_2;
 
                currentTomStructureOrgTrack = orgTrack;
                permissiveVerify(t);
                return false;
               }}matchlab_match1_pattern7: {
  return true; } }
 
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
             { TomTerm tom_match2_1 = null; tom_match2_1 = (TomTerm) term;matchlab_match2_pattern1: { TomTerm t = null; if(tom_is_fun_sym_BackQuoteTerm(tom_match2_1)) { TomTerm tom_match2_1_1 = null; Option tom_match2_1_2 = null; tom_match2_1_1 = (TomTerm) tom_get_slot_BackQuoteTerm_term(tom_match2_1); tom_match2_1_2 = (Option) tom_get_slot_BackQuoteTerm_option(tom_match2_1); t = (TomTerm) tom_match2_1_1;
 
                permissiveVerify(t);
               }}matchlab_match2_pattern2: { TomList args = null; String name = null; OptionList options = null; if(tom_is_fun_sym_Appl(tom_match2_1)) { Option tom_match2_1_1 = null; TomName tom_match2_1_2 = null; TomList tom_match2_1_3 = null; tom_match2_1_1 = (Option) tom_get_slot_Appl_option(tom_match2_1); tom_match2_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match2_1); tom_match2_1_3 = (TomList) tom_get_slot_Appl_args(tom_match2_1); if(tom_is_fun_sym_Option(tom_match2_1_1)) { OptionList tom_match2_1_1_1 = null; tom_match2_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match2_1_1); options = (OptionList) tom_match2_1_1_1; if(tom_is_fun_sym_Name(tom_match2_1_2)) { String tom_match2_1_2_1 = null; tom_match2_1_2_1 = (String) tom_get_slot_Name_string(tom_match2_1_2); name = (String) tom_match2_1_2_1; args = (TomList) tom_match2_1_3;
 
                permissiveVerifyApplStructure(options, name, args);
                return true;
               } } }}matchlab_match2_pattern3: { OptionList options = null; String name = null; if(tom_is_fun_sym_RecordAppl(tom_match2_1)) { Option tom_match2_1_1 = null; TomName tom_match2_1_2 = null; TomList tom_match2_1_3 = null; tom_match2_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match2_1); tom_match2_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match2_1); tom_match2_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match2_1); if(tom_is_fun_sym_Option(tom_match2_1_1)) { OptionList tom_match2_1_1_1 = null; tom_match2_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match2_1_1); options = (OptionList) tom_match2_1_1_1; if(tom_is_fun_sym_Name(tom_match2_1_2)) { String tom_match2_1_2_1 = null; tom_match2_1_2_1 = (String) tom_get_slot_Name_string(tom_match2_1_2); name = (String) tom_match2_1_2_1;
 
                messageRuleErrorRhsImpossibleRecord(options, name);
                return true;
               } } }}matchlab_match2_pattern4: { OptionList orgTrack = null; if(tom_is_fun_sym_Placeholder(tom_match2_1)) { Option tom_match2_1_1 = null; tom_match2_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match2_1); if(tom_is_fun_sym_Option(tom_match2_1_1)) { OptionList tom_match2_1_1_1 = null; tom_match2_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match2_1_1); orgTrack = (OptionList) tom_match2_1_1_1;
 
                messageRuleErrorRhsImpossiblePlaceholder(orgTrack);
               } }}matchlab_match2_pattern5: {
  return true; } }
 
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
       { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) term;matchlab_match3_pattern1: { String type = null; String name = null; if(tom_is_fun_sym_GLVar(tom_match3_1)) { String tom_match3_1_1 = null; TomType tom_match3_1_2 = null; tom_match3_1_1 = (String) tom_get_slot_GLVar_strName(tom_match3_1); tom_match3_1_2 = (TomType) tom_get_slot_GLVar_astType(tom_match3_1); name = (String) tom_match3_1_1; if(tom_is_fun_sym_TomTypeAlone(tom_match3_1_2)) { String tom_match3_1_2_1 = null; tom_match3_1_2_1 = (String) tom_get_slot_TomTypeAlone_string(tom_match3_1_2); type = (String) tom_match3_1_2_1;
 
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
      
       { TomTerm tom_match4_1 = null; tom_match4_1 = (TomTerm) termAppl;matchlab_match4_pattern1: { OptionList list = null; String name = null; if(tom_is_fun_sym_Appl(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomList tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_Appl_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match4_1); tom_match4_1_3 = (TomList) tom_get_slot_Appl_args(tom_match4_1); if(tom_is_fun_sym_Option(tom_match4_1_1)) { OptionList tom_match4_1_1_1 = null; tom_match4_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match4_1_1); list = (OptionList) tom_match4_1_1_1; if(tom_is_fun_sym_Name(tom_match4_1_2)) { String tom_match4_1_2_1 = null; tom_match4_1_2_1 = (String) tom_get_slot_Name_string(tom_match4_1_2); name = (String) tom_match4_1_2_1;
 
          line = findOriginTrackingLine(list);
          nbFoundArgs++;
          foundTypeMatch.add(extractType(symbolTable().getSymbol(name)));
         } } }}matchlab_match4_pattern2: { if(tom_is_fun_sym_Placeholder(tom_match4_1)) { Option tom_match4_1_1 = null; tom_match4_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match4_1);
 
          nbFoundArgs++;
          foundTypeMatch.add((TomTerm) null);
         }}matchlab_match4_pattern3: { String name = null; OptionList list = null; if(tom_is_fun_sym_VariableStar(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomType tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match4_1); tom_match4_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match4_1); if(tom_is_fun_sym_Option(tom_match4_1_1)) { OptionList tom_match4_1_1_1 = null; tom_match4_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match4_1_1); list = (OptionList) tom_match4_1_1_1; if(tom_is_fun_sym_Name(tom_match4_1_2)) { String tom_match4_1_2_1 = null; tom_match4_1_2_1 = (String) tom_get_slot_Name_string(tom_match4_1_2); name = (String) tom_match4_1_2_1;

  
          line = findOriginTrackingLine(name,list);
          messageMatchErrorVariableStar(name, line); 
         } } }}matchlab_match4_pattern4: { OptionList options = null; TomList args = null; String name = null; if(tom_is_fun_sym_RecordAppl(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomList tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match4_1); tom_match4_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match4_1); if(tom_is_fun_sym_Option(tom_match4_1_1)) { OptionList tom_match4_1_1_1 = null; tom_match4_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match4_1_1); options = (OptionList) tom_match4_1_1_1; if(tom_is_fun_sym_Name(tom_match4_1_2)) { String tom_match4_1_2_1 = null; tom_match4_1_2_1 = (String) tom_get_slot_Name_string(tom_match4_1_2); name = (String) tom_match4_1_2_1; args = (TomList) tom_match4_1_3;
 
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

  private String extractType(TomSymbol symbol) {
    TomType type = getSymbolCodomain(symbol);
    return getTomType(type);
  }
  
  private void messageMatchErrorNumberArgument(int nbExpectedVar, int nbFoundVar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Bad number of arguments: " +nbExpectedVar+" argument(s) required but "+nbFoundVar+" found in %match structure declared line "+lineDecl; 
    messageError(line,s);
  }
  
  private void messageMatchErrorTypeArgument(int slotNumber, String expectedType, String givenType, String line) {
    if(Flags.noWarning) return;
    String orgLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Warning *** Bad type in %match declared line "+orgLine);
    System.out.println(" *** For slot "+ slotNumber +" :Type '"+expectedType+"' required but Type '"+givenType+"' found"+" - Line : "+line);
  }
  
  private void messageMatchTypeVariableError(String name, String type) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Variable '" + name + "' has an unknown type:  '" + type + "' in %match construct declared line "+lineDecl;
    messageError(lineDecl,s);
  }
  
private void messageMatchErrorVariableStar(String nameVariableStar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable "+nameVariableStar+"* : Not allowed in on top of %match structure declared line "+lineDecl;
    messageError(line,s);
  }
  
    //////////////////////////////
    // SYMBOL AND TYPE CONCERNS //
    //////////////////////////////
  
  private void verifyDeclaration(Declaration declaration) throws TomException {
     { Declaration tom_match5_1 = null; tom_match5_1 = (Declaration) declaration;matchlab_match5_pattern1: { String tomName = null; if(tom_is_fun_sym_SymbolDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; tom_match5_1_1 = (TomName) tom_get_slot_SymbolDecl_astName(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%op", tomSymbol);
       } }}matchlab_match5_pattern2: { String tomName = null; if(tom_is_fun_sym_ArraySymbolDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; tom_match5_1_1 = (TomName) tom_get_slot_ArraySymbolDecl_astName(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oparray", tomSymbol);
       } }}matchlab_match5_pattern3: { String tomName = null; if(tom_is_fun_sym_ListSymbolDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; tom_match5_1_1 = (TomName) tom_get_slot_ListSymbolDecl_astName(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        verifySymbol("%oplist", tomSymbol);
       } }}matchlab_match5_pattern4: { Option orgTrack = null; String tomName = null; TomList tomList = null; if(tom_is_fun_sym_TypeTermDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; TomList tom_match5_1_2 = null; Option tom_match5_1_3 = null; tom_match5_1_1 = (TomName) tom_get_slot_TypeTermDecl_astName(tom_match5_1); tom_match5_1_2 = (TomList) tom_get_slot_TypeTermDecl_keywordList(tom_match5_1); tom_match5_1_3 = (Option) tom_get_slot_TypeTermDecl_orgTrack(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1; tomList = (TomList) tom_match5_1_2; orgTrack = (Option) tom_match5_1_3;
 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typeterm", tomList);
       } }}matchlab_match5_pattern5: { String tomName = null; TomList tomList = null; Option orgTrack = null; if(tom_is_fun_sym_TypeListDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; TomList tom_match5_1_2 = null; Option tom_match5_1_3 = null; tom_match5_1_1 = (TomName) tom_get_slot_TypeListDecl_astName(tom_match5_1); tom_match5_1_2 = (TomList) tom_get_slot_TypeListDecl_keywordList(tom_match5_1); tom_match5_1_3 = (Option) tom_get_slot_TypeListDecl_orgTrack(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1; tomList = (TomList) tom_match5_1_2; orgTrack = (Option) tom_match5_1_3;

 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typelist", tomList);
       } }}matchlab_match5_pattern6: { Option orgTrack = null; TomList tomList = null; String tomName = null; if(tom_is_fun_sym_TypeArrayDecl(tom_match5_1)) { TomName tom_match5_1_1 = null; TomList tom_match5_1_2 = null; Option tom_match5_1_3 = null; tom_match5_1_1 = (TomName) tom_get_slot_TypeArrayDecl_astName(tom_match5_1); tom_match5_1_2 = (TomList) tom_get_slot_TypeArrayDecl_keywordList(tom_match5_1); tom_match5_1_3 = (Option) tom_get_slot_TypeArrayDecl_orgTrack(tom_match5_1); if(tom_is_fun_sym_Name(tom_match5_1_1)) { String tom_match5_1_1_1 = null; tom_match5_1_1_1 = (String) tom_get_slot_Name_string(tom_match5_1_1); tomName = (String) tom_match5_1_1_1; tomList = (TomList) tom_match5_1_2; orgTrack = (Option) tom_match5_1_3;

 
	currentTomStructureOrgTrack = orgTrack;
        verifyMultipleDefinitionOfType(tomName);
        verifyTypeDecl("%typearray", tomList);
       } }} }
 
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
       { TomTerm tom_match6_1 = null; tom_match6_1 = (TomTerm) term;matchlab_match6_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetFunctionSymbolDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TargetLanguage tom_match6_1_1_2 = null; Option tom_match6_1_1_3 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetFunctionSymbolDecl_termArg(tom_match6_1_1); tom_match6_1_1_2 = (TargetLanguage) tom_get_slot_GetFunctionSymbolDecl_tlCode(tom_match6_1_1); tom_match6_1_1_3 = (Option) tom_get_slot_GetFunctionSymbolDecl_orgTrack(tom_match6_1_1); orgTrack = (Option) tom_match6_1_1_3;
 
          checkField("get_fun_sym",verifyList,orgTrack, declType);
         } }}matchlab_match6_pattern2: { String name2 = null; Option orgTrack = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_CompareFunctionSymbolDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TomTerm tom_match6_1_1_2 = null; TargetLanguage tom_match6_1_1_3 = null; Option tom_match6_1_1_4 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_CompareFunctionSymbolDecl_symbolArg1(tom_match6_1_1); tom_match6_1_1_2 = (TomTerm) tom_get_slot_CompareFunctionSymbolDecl_symbolArg2(tom_match6_1_1); tom_match6_1_1_3 = (TargetLanguage) tom_get_slot_CompareFunctionSymbolDecl_tlCode(tom_match6_1_1); tom_match6_1_1_4 = (Option) tom_get_slot_CompareFunctionSymbolDecl_orgTrack(tom_match6_1_1); if(tom_is_fun_sym_Variable(tom_match6_1_1_1)) { Option tom_match6_1_1_1_1 = null; TomName tom_match6_1_1_1_2 = null; TomType tom_match6_1_1_1_3 = null; tom_match6_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_1); tom_match6_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_1); tom_match6_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_1); if(tom_is_fun_sym_Name(tom_match6_1_1_1_2)) { String tom_match6_1_1_1_2_1 = null; tom_match6_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_1_2); name1 = (String) tom_match6_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match6_1_1_2)) { Option tom_match6_1_1_2_1 = null; TomName tom_match6_1_1_2_2 = null; TomType tom_match6_1_1_2_3 = null; tom_match6_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_2); tom_match6_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_2); tom_match6_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_2); if(tom_is_fun_sym_Name(tom_match6_1_1_2_2)) { String tom_match6_1_1_2_2_1 = null; tom_match6_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_2_2); name2 = (String) tom_match6_1_1_2_2_1; orgTrack = (Option) tom_match6_1_1_4;
 
          checkFieldAndLinearArgs("cmp_fun_sym",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match6_pattern3: { String name2 = null; Option orgTrack = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_TermsEqualDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TomTerm tom_match6_1_1_2 = null; TargetLanguage tom_match6_1_1_3 = null; Option tom_match6_1_1_4 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_TermsEqualDecl_termArg1(tom_match6_1_1); tom_match6_1_1_2 = (TomTerm) tom_get_slot_TermsEqualDecl_termArg2(tom_match6_1_1); tom_match6_1_1_3 = (TargetLanguage) tom_get_slot_TermsEqualDecl_tlCode(tom_match6_1_1); tom_match6_1_1_4 = (Option) tom_get_slot_TermsEqualDecl_orgTrack(tom_match6_1_1); if(tom_is_fun_sym_Variable(tom_match6_1_1_1)) { Option tom_match6_1_1_1_1 = null; TomName tom_match6_1_1_1_2 = null; TomType tom_match6_1_1_1_3 = null; tom_match6_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_1); tom_match6_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_1); tom_match6_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_1); if(tom_is_fun_sym_Name(tom_match6_1_1_1_2)) { String tom_match6_1_1_1_2_1 = null; tom_match6_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_1_2); name1 = (String) tom_match6_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match6_1_1_2)) { Option tom_match6_1_1_2_1 = null; TomName tom_match6_1_1_2_2 = null; TomType tom_match6_1_1_2_3 = null; tom_match6_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_2); tom_match6_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_2); tom_match6_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_2); if(tom_is_fun_sym_Name(tom_match6_1_1_2_2)) { String tom_match6_1_1_2_2_1 = null; tom_match6_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_2_2); name2 = (String) tom_match6_1_1_2_2_1; orgTrack = (Option) tom_match6_1_1_4;
 
          checkFieldAndLinearArgs("equals",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match6_pattern4: { Option orgTrack = null; String name2 = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetSubtermDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TomTerm tom_match6_1_1_2 = null; TargetLanguage tom_match6_1_1_3 = null; Option tom_match6_1_1_4 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetSubtermDecl_termArg(tom_match6_1_1); tom_match6_1_1_2 = (TomTerm) tom_get_slot_GetSubtermDecl_numberArg(tom_match6_1_1); tom_match6_1_1_3 = (TargetLanguage) tom_get_slot_GetSubtermDecl_tlCode(tom_match6_1_1); tom_match6_1_1_4 = (Option) tom_get_slot_GetSubtermDecl_orgTrack(tom_match6_1_1); if(tom_is_fun_sym_Variable(tom_match6_1_1_1)) { Option tom_match6_1_1_1_1 = null; TomName tom_match6_1_1_1_2 = null; TomType tom_match6_1_1_1_3 = null; tom_match6_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_1); tom_match6_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_1); tom_match6_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_1); if(tom_is_fun_sym_Name(tom_match6_1_1_1_2)) { String tom_match6_1_1_1_2_1 = null; tom_match6_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_1_2); name1 = (String) tom_match6_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match6_1_1_2)) { Option tom_match6_1_1_2_1 = null; TomName tom_match6_1_1_2_2 = null; TomType tom_match6_1_1_2_3 = null; tom_match6_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_2); tom_match6_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_2); tom_match6_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_2); if(tom_is_fun_sym_Name(tom_match6_1_1_2_2)) { String tom_match6_1_1_2_2_1 = null; tom_match6_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_2_2); name2 = (String) tom_match6_1_1_2_2_1; orgTrack = (Option) tom_match6_1_1_4;

 
          checkFieldAndLinearArgs("get_subterm",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match6_pattern5: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetHeadDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TargetLanguage tom_match6_1_1_2 = null; Option tom_match6_1_1_3 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetHeadDecl_var(tom_match6_1_1); tom_match6_1_1_2 = (TargetLanguage) tom_get_slot_GetHeadDecl_tlcode(tom_match6_1_1); tom_match6_1_1_3 = (Option) tom_get_slot_GetHeadDecl_orgTrack(tom_match6_1_1); orgTrack = (Option) tom_match6_1_1_3;

 
          checkField("get_head",verifyList,orgTrack, declType);
         } }}matchlab_match6_pattern6: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetTailDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TargetLanguage tom_match6_1_1_2 = null; Option tom_match6_1_1_3 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetTailDecl_var(tom_match6_1_1); tom_match6_1_1_2 = (TargetLanguage) tom_get_slot_GetTailDecl_tlcode(tom_match6_1_1); tom_match6_1_1_3 = (Option) tom_get_slot_GetTailDecl_orgTrack(tom_match6_1_1); orgTrack = (Option) tom_match6_1_1_3;
 
          checkField("get_tail",verifyList,orgTrack, declType);
         } }}matchlab_match6_pattern7: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_IsEmptyDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TargetLanguage tom_match6_1_1_2 = null; Option tom_match6_1_1_3 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_IsEmptyDecl_var(tom_match6_1_1); tom_match6_1_1_2 = (TargetLanguage) tom_get_slot_IsEmptyDecl_tlcode(tom_match6_1_1); tom_match6_1_1_3 = (Option) tom_get_slot_IsEmptyDecl_orgTrack(tom_match6_1_1); orgTrack = (Option) tom_match6_1_1_3;
 
          checkField("is_empty",verifyList,orgTrack, declType);
         } }}matchlab_match6_pattern8: { Option orgTrack = null; String name2 = null; String name1 = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetElementDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TomTerm tom_match6_1_1_2 = null; TargetLanguage tom_match6_1_1_3 = null; Option tom_match6_1_1_4 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetElementDecl_kid1(tom_match6_1_1); tom_match6_1_1_2 = (TomTerm) tom_get_slot_GetElementDecl_kid2(tom_match6_1_1); tom_match6_1_1_3 = (TargetLanguage) tom_get_slot_GetElementDecl_tlCode(tom_match6_1_1); tom_match6_1_1_4 = (Option) tom_get_slot_GetElementDecl_orgTrack(tom_match6_1_1); if(tom_is_fun_sym_Variable(tom_match6_1_1_1)) { Option tom_match6_1_1_1_1 = null; TomName tom_match6_1_1_1_2 = null; TomType tom_match6_1_1_1_3 = null; tom_match6_1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_1); tom_match6_1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_1); tom_match6_1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_1); if(tom_is_fun_sym_Name(tom_match6_1_1_1_2)) { String tom_match6_1_1_1_2_1 = null; tom_match6_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_1_2); name1 = (String) tom_match6_1_1_1_2_1; if(tom_is_fun_sym_Variable(tom_match6_1_1_2)) { Option tom_match6_1_1_2_1 = null; TomName tom_match6_1_1_2_2 = null; TomType tom_match6_1_1_2_3 = null; tom_match6_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1_2); tom_match6_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1_2); tom_match6_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1_2); if(tom_is_fun_sym_Name(tom_match6_1_1_2_2)) { String tom_match6_1_1_2_2_1 = null; tom_match6_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_1_2_2); name2 = (String) tom_match6_1_1_2_2_1; orgTrack = (Option) tom_match6_1_1_4;

  
          checkFieldAndLinearArgs("get_element",verifyList,orgTrack,name1,name2, declType);
         } } } } } }}matchlab_match6_pattern9: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToTomTerm(tom_match6_1)) { Declaration tom_match6_1_1 = null; tom_match6_1_1 = (Declaration) tom_get_slot_DeclarationToTomTerm_astDeclaration(tom_match6_1); if(tom_is_fun_sym_GetSizeDecl(tom_match6_1_1)) { TomTerm tom_match6_1_1_1 = null; TargetLanguage tom_match6_1_1_2 = null; Option tom_match6_1_1_3 = null; tom_match6_1_1_1 = (TomTerm) tom_get_slot_GetSizeDecl_kid1(tom_match6_1_1); tom_match6_1_1_2 = (TargetLanguage) tom_get_slot_GetSizeDecl_tlCode(tom_match6_1_1); tom_match6_1_1_3 = (Option) tom_get_slot_GetSizeDecl_orgTrack(tom_match6_1_1); orgTrack = (Option) tom_match6_1_1_3;
 
          checkField("get_size",verifyList,orgTrack, declType);
         } }} }
 
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
    if(Flags.noWarning) return;
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Warning *** Multiple definition of type");
    System.out.println(" *** Type '"+ name +"' is already defined - Line : "+declLine);
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
    SlotList slotList = tomSymbol.getSlotList();
    verifyMultipleDefinitionOfSymbol(name, line);
    verifySymbolCodomain(type.getString(), name, line);
    nbArgs = verifyAndCountSymbolArguments(l, name, line);
    verifySymbolOptions(symbolType, optionList, nbArgs);
  }
  
  private void verifyMultipleDefinitionOfSymbol(String name, String line) {
    if(alreadyStudiedSymbol.contains(name)) {
      messageOperatorErrorYetDefined(name,line);
    }
    else
      alreadyStudiedSymbol.add(name);
  }
  
  private void messageOperatorErrorYetDefined(String name, String line) {
    if(Flags.noWarning) return;
    System.out.println(" \n *** Warning *** Multiple definition of operator");
    System.out.println(" *** Operator '"+ name +"' is already defined - Line : "+line);
  }
  
  private void verifySymbolCodomain(String returnTypeName, String symbName, String symbLine) throws TomException {
    if (symbolTable().getType(returnTypeName) == null){
      messageTypeOperatorError(returnTypeName, symbName, symbLine);
    }
  }
  
  private void messageTypeOperatorError(String type, String name, String line) throws TomException {
    String s = "Operator '" + name + "' has an unknown return type: '" + type + "'";
    messageError(line,s);
  }
  
  private int verifyAndCountSymbolArguments(TomList args, String symbName, String symbLine) throws TomException {
    TomTerm type;
    int nbArgs=0;
    while(!args.isEmpty()) {
      type = args.getHead();
       { TomTerm tom_match7_1 = null; tom_match7_1 = (TomTerm) type;matchlab_match7_pattern1: { TomType type1 = null; if(tom_is_fun_sym_TomTypeToTomTerm(tom_match7_1)) { TomType tom_match7_1_1 = null; tom_match7_1_1 = (TomType) tom_get_slot_TomTypeToTomTerm_astType(tom_match7_1); type1 = (TomType) tom_match7_1_1;
 
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
  
  private void messageTypesOperatorError(String type, int slotPosition, String name, String line) throws TomException {
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
       { Option tom_match8_1 = null; tom_match8_1 = (Option) term;matchlab_match8_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match8_1)) { Declaration tom_match8_1_1 = null; tom_match8_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match8_1); if(tom_is_fun_sym_MakeEmptyArray(tom_match8_1_1)) { TomName tom_match8_1_1_1 = null; TomTerm tom_match8_1_1_2 = null; TargetLanguage tom_match8_1_1_3 = null; Option tom_match8_1_1_4 = null; tom_match8_1_1_1 = (TomName) tom_get_slot_MakeEmptyArray_astName(tom_match8_1_1); tom_match8_1_1_2 = (TomTerm) tom_get_slot_MakeEmptyArray_varSize(tom_match8_1_1); tom_match8_1_1_3 = (TargetLanguage) tom_get_slot_MakeEmptyArray_tlCode(tom_match8_1_1); tom_match8_1_1_4 = (Option) tom_get_slot_MakeEmptyArray_orgTrack(tom_match8_1_1); orgTrack = (Option) tom_match8_1_1_4;

  
          checkField("make_empty",verifyList,orgTrack, symbType);
         } }}matchlab_match8_pattern2: { Option orgTrack = null; String name1 = null; String name2 = null; if(tom_is_fun_sym_DeclarationToOption(tom_match8_1)) { Declaration tom_match8_1_1 = null; tom_match8_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match8_1); if(tom_is_fun_sym_MakeAddArray(tom_match8_1_1)) { TomName tom_match8_1_1_1 = null; TomTerm tom_match8_1_1_2 = null; TomTerm tom_match8_1_1_3 = null; TargetLanguage tom_match8_1_1_4 = null; Option tom_match8_1_1_5 = null; tom_match8_1_1_1 = (TomName) tom_get_slot_MakeAddArray_astName(tom_match8_1_1); tom_match8_1_1_2 = (TomTerm) tom_get_slot_MakeAddArray_varElt(tom_match8_1_1); tom_match8_1_1_3 = (TomTerm) tom_get_slot_MakeAddArray_varList(tom_match8_1_1); tom_match8_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddArray_tlCode(tom_match8_1_1); tom_match8_1_1_5 = (Option) tom_get_slot_MakeAddArray_orgTrack(tom_match8_1_1); if(tom_is_fun_sym_Variable(tom_match8_1_1_2)) { Option tom_match8_1_1_2_1 = null; TomName tom_match8_1_1_2_2 = null; TomType tom_match8_1_1_2_3 = null; tom_match8_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match8_1_1_2); tom_match8_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_1_1_2); tom_match8_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_1_1_2); if(tom_is_fun_sym_Name(tom_match8_1_1_2_2)) { String tom_match8_1_1_2_2_1 = null; tom_match8_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match8_1_1_2_2); name2 = (String) tom_match8_1_1_2_2_1; if(tom_is_fun_sym_Variable(tom_match8_1_1_3)) { Option tom_match8_1_1_3_1 = null; TomName tom_match8_1_1_3_2 = null; TomType tom_match8_1_1_3_3 = null; tom_match8_1_1_3_1 = (Option) tom_get_slot_Variable_option(tom_match8_1_1_3); tom_match8_1_1_3_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_1_1_3); tom_match8_1_1_3_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_1_1_3); if(tom_is_fun_sym_Name(tom_match8_1_1_3_2)) { String tom_match8_1_1_3_2_1 = null; tom_match8_1_1_3_2_1 = (String) tom_get_slot_Name_string(tom_match8_1_1_3_2); name1 = (String) tom_match8_1_1_3_2_1; orgTrack = (Option) tom_match8_1_1_5;
 
          checkFieldAndLinearArgs("make_append", verifyList, orgTrack, name1, name2, symbType);
         } } } } } }}matchlab_match8_pattern3: { Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match8_1)) { Declaration tom_match8_1_1 = null; tom_match8_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match8_1); if(tom_is_fun_sym_MakeEmptyList(tom_match8_1_1)) { TomName tom_match8_1_1_1 = null; TargetLanguage tom_match8_1_1_2 = null; Option tom_match8_1_1_3 = null; tom_match8_1_1_1 = (TomName) tom_get_slot_MakeEmptyList_astName(tom_match8_1_1); tom_match8_1_1_2 = (TargetLanguage) tom_get_slot_MakeEmptyList_tlCode(tom_match8_1_1); tom_match8_1_1_3 = (Option) tom_get_slot_MakeEmptyList_orgTrack(tom_match8_1_1); orgTrack = (Option) tom_match8_1_1_3;

 
          checkField("make_empty",verifyList,orgTrack, symbType);
          
         } }}matchlab_match8_pattern4: { Option orgTrack = null; String name2 = null; String name1 = null; if(tom_is_fun_sym_DeclarationToOption(tom_match8_1)) { Declaration tom_match8_1_1 = null; tom_match8_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match8_1); if(tom_is_fun_sym_MakeAddList(tom_match8_1_1)) { TomName tom_match8_1_1_1 = null; TomTerm tom_match8_1_1_2 = null; TomTerm tom_match8_1_1_3 = null; TargetLanguage tom_match8_1_1_4 = null; Option tom_match8_1_1_5 = null; tom_match8_1_1_1 = (TomName) tom_get_slot_MakeAddList_astName(tom_match8_1_1); tom_match8_1_1_2 = (TomTerm) tom_get_slot_MakeAddList_varElt(tom_match8_1_1); tom_match8_1_1_3 = (TomTerm) tom_get_slot_MakeAddList_varList(tom_match8_1_1); tom_match8_1_1_4 = (TargetLanguage) tom_get_slot_MakeAddList_tlCode(tom_match8_1_1); tom_match8_1_1_5 = (Option) tom_get_slot_MakeAddList_orgTrack(tom_match8_1_1); if(tom_is_fun_sym_Variable(tom_match8_1_1_2)) { Option tom_match8_1_1_2_1 = null; TomName tom_match8_1_1_2_2 = null; TomType tom_match8_1_1_2_3 = null; tom_match8_1_1_2_1 = (Option) tom_get_slot_Variable_option(tom_match8_1_1_2); tom_match8_1_1_2_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_1_1_2); tom_match8_1_1_2_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_1_1_2); if(tom_is_fun_sym_Name(tom_match8_1_1_2_2)) { String tom_match8_1_1_2_2_1 = null; tom_match8_1_1_2_2_1 = (String) tom_get_slot_Name_string(tom_match8_1_1_2_2); name2 = (String) tom_match8_1_1_2_2_1; if(tom_is_fun_sym_Variable(tom_match8_1_1_3)) { Option tom_match8_1_1_3_1 = null; TomName tom_match8_1_1_3_2 = null; TomType tom_match8_1_1_3_3 = null; tom_match8_1_1_3_1 = (Option) tom_get_slot_Variable_option(tom_match8_1_1_3); tom_match8_1_1_3_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_1_1_3); tom_match8_1_1_3_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_1_1_3); if(tom_is_fun_sym_Name(tom_match8_1_1_3_2)) { String tom_match8_1_1_3_2_1 = null; tom_match8_1_1_3_2_1 = (String) tom_get_slot_Name_string(tom_match8_1_1_3_2); name1 = (String) tom_match8_1_1_3_2_1; orgTrack = (Option) tom_match8_1_1_5;
 
          checkFieldAndLinearArgs("make_insert", verifyList, orgTrack, name1, name2, symbType);
         } } } } } }}matchlab_match8_pattern5: { Option orgTrack = null; TomList makeArgsList = null; if(tom_is_fun_sym_DeclarationToOption(tom_match8_1)) { Declaration tom_match8_1_1 = null; tom_match8_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match8_1); if(tom_is_fun_sym_MakeDecl(tom_match8_1_1)) { TomName tom_match8_1_1_1 = null; TomType tom_match8_1_1_2 = null; TomList tom_match8_1_1_3 = null; TargetLanguage tom_match8_1_1_4 = null; Option tom_match8_1_1_5 = null; tom_match8_1_1_1 = (TomName) tom_get_slot_MakeDecl_astName(tom_match8_1_1); tom_match8_1_1_2 = (TomType) tom_get_slot_MakeDecl_astType(tom_match8_1_1); tom_match8_1_1_3 = (TomList) tom_get_slot_MakeDecl_args(tom_match8_1_1); tom_match8_1_1_4 = (TargetLanguage) tom_get_slot_MakeDecl_tlCode(tom_match8_1_1); tom_match8_1_1_5 = (Option) tom_get_slot_MakeDecl_orgTrack(tom_match8_1_1); makeArgsList = (TomList) tom_match8_1_1_3; orgTrack = (Option) tom_match8_1_1_5;

 
          verifyMakeDeclArgs(makeArgsList, expectedNbMakeArgs, orgTrack, symbType);
         } }} }
 
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
       { TomTerm tom_match9_1 = null; tom_match9_1 = (TomTerm) termVar;matchlab_match9_pattern1: { String name = null; OptionList listOption = null; if(tom_is_fun_sym_Variable(tom_match9_1)) { Option tom_match9_1_1 = null; TomName tom_match9_1_2 = null; TomType tom_match9_1_3 = null; tom_match9_1_1 = (Option) tom_get_slot_Variable_option(tom_match9_1); tom_match9_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match9_1); tom_match9_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match9_1); if(tom_is_fun_sym_Option(tom_match9_1_1)) { OptionList tom_match9_1_1_1 = null; tom_match9_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match9_1_1); listOption = (OptionList) tom_match9_1_1_1; if(tom_is_fun_sym_Name(tom_match9_1_2)) { String tom_match9_1_2_1 = null; tom_match9_1_2_1 = (String) tom_get_slot_Name_string(tom_match9_1_2); name = (String) tom_match9_1_2_1;
 
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
  private void messageNumberArgumentsError(int nbArg, int nbArg2, Option orgTrack, String symbType) throws TomException {
    String line = "not found", name = "unknown", lineDecl = "not found",nameDecl = "unknown";
     { Option tom_match10_1 = null; Option tom_match10_2 = null; tom_match10_1 = (Option) orgTrack; tom_match10_2 = (Option) currentTomStructureOrgTrack;matchlab_match10_pattern1: { String declOrgName = null; String declOrgLine = null; String orgLine = null; String orgName = null; if(tom_is_fun_sym_OriginTracking(tom_match10_1)) { TomName tom_match10_1_1 = null; TomTerm tom_match10_1_2 = null; tom_match10_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match10_1); tom_match10_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match10_1); if(tom_is_fun_sym_Name(tom_match10_1_1)) { String tom_match10_1_1_1 = null; tom_match10_1_1_1 = (String) tom_get_slot_Name_string(tom_match10_1_1); orgName = (String) tom_match10_1_1_1; if(tom_is_fun_sym_Line(tom_match10_1_2)) { String tom_match10_1_2_1 = null; tom_match10_1_2_1 = (String) tom_get_slot_Line_string(tom_match10_1_2); orgLine = (String) tom_match10_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match10_2)) { TomName tom_match10_2_1 = null; TomTerm tom_match10_2_2 = null; tom_match10_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match10_2); tom_match10_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match10_2); if(tom_is_fun_sym_Name(tom_match10_2_1)) { String tom_match10_2_1_1 = null; tom_match10_2_1_1 = (String) tom_get_slot_Name_string(tom_match10_2_1); declOrgName = (String) tom_match10_2_1_1; if(tom_is_fun_sym_Line(tom_match10_2_2)) { String tom_match10_2_2_1 = null; tom_match10_2_2_1 = (String) tom_get_slot_Line_string(tom_match10_2_2); declOrgLine = (String) tom_match10_2_2_1;
 
        lineDecl = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
       } } } } } }} }
 
    String s = "Bad number of arguments in method '" + name + "' for '"+symbType+" "+nameDecl+"' declared at line "+lineDecl +": \n" + nbArg + " argument(s) are required but " + nbArg2 + " found";
    messageError(line,s);
  }	
  
  private void messageErrorVariableStar(String nameVariableStar, String nameMethod ,String line) throws TomException {
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
    String line = "not found", name = "unknown", nameDecl = "unknown", lineDecl = "not found";
     { Option tom_match11_1 = null; Option tom_match11_2 = null; tom_match11_1 = (Option) orgTrack; tom_match11_2 = (Option) currentTomStructureOrgTrack;matchlab_match11_pattern1: { String declOrgName = null; String orgLine = null; String orgName = null; String declOrgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match11_1)) { TomName tom_match11_1_1 = null; TomTerm tom_match11_1_2 = null; tom_match11_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match11_1); tom_match11_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match11_1); if(tom_is_fun_sym_Name(tom_match11_1_1)) { String tom_match11_1_1_1 = null; tom_match11_1_1_1 = (String) tom_get_slot_Name_string(tom_match11_1_1); orgName = (String) tom_match11_1_1_1; if(tom_is_fun_sym_Line(tom_match11_1_2)) { String tom_match11_1_2_1 = null; tom_match11_1_2_1 = (String) tom_get_slot_Line_string(tom_match11_1_2); orgLine = (String) tom_match11_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match11_2)) { TomName tom_match11_2_1 = null; TomTerm tom_match11_2_2 = null; tom_match11_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match11_2); tom_match11_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match11_2); if(tom_is_fun_sym_Name(tom_match11_2_1)) { String tom_match11_2_1_1 = null; tom_match11_2_1_1 = (String) tom_get_slot_Name_string(tom_match11_2_1); declOrgName = (String) tom_match11_2_1_1; if(tom_is_fun_sym_Line(tom_match11_2_2)) { String tom_match11_2_2_1 = null; tom_match11_2_2_1 = (String) tom_get_slot_Line_string(tom_match11_2_2); declOrgLine = (String) tom_match11_2_2_1;
 
        lineDecl = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
       } } } } } }} }
  
    System.out.println("\n *** Repeated macro-functions in declaration '"+declType+" "+nameDecl+"' declared at line "+lineDecl);
    System.out.println(" *** '" + nameFunction + "' - Line : " + line);
    Flags.findErrors = true;
  } 
  private void messageTwoSameNameVariableError(String nameFunction, String nameVar, Option orgTrack, String declType) {
    String line = "not found", name = "unknown", nameDecl = "unknown", lineDecl = "not found";
     { Option tom_match12_1 = null; Option tom_match12_2 = null; tom_match12_1 = (Option) orgTrack; tom_match12_2 = (Option) currentTomStructureOrgTrack;matchlab_match12_pattern1: { String declOrgName = null; String orgName = null; String declOrgLine = null; String orgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match12_1)) { TomName tom_match12_1_1 = null; TomTerm tom_match12_1_2 = null; tom_match12_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match12_1); tom_match12_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match12_1); if(tom_is_fun_sym_Name(tom_match12_1_1)) { String tom_match12_1_1_1 = null; tom_match12_1_1_1 = (String) tom_get_slot_Name_string(tom_match12_1_1); orgName = (String) tom_match12_1_1_1; if(tom_is_fun_sym_Line(tom_match12_1_2)) { String tom_match12_1_2_1 = null; tom_match12_1_2_1 = (String) tom_get_slot_Line_string(tom_match12_1_2); orgLine = (String) tom_match12_1_2_1; if(tom_is_fun_sym_OriginTracking(tom_match12_2)) { TomName tom_match12_2_1 = null; TomTerm tom_match12_2_2 = null; tom_match12_2_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match12_2); tom_match12_2_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match12_2); if(tom_is_fun_sym_Name(tom_match12_2_1)) { String tom_match12_2_1_1 = null; tom_match12_2_1_1 = (String) tom_get_slot_Name_string(tom_match12_2_1); declOrgName = (String) tom_match12_2_1_1; if(tom_is_fun_sym_Line(tom_match12_2_2)) { String tom_match12_2_2_1 = null; tom_match12_2_2_1 = (String) tom_get_slot_Line_string(tom_match12_2_2); declOrgLine = (String) tom_match12_2_2_1;
 
        lineDecl = declOrgLine;nameDecl = declOrgName;line = orgLine;name = orgName;
       } } } } } }} }
 
    System.out.println("\n *** Arguments must be linear in declaration '"+nameFunction+"' for '"+declType+" "+nameDecl+"' declared at line "+lineDecl);
    System.out.println(" *** Variable '"+nameVar+"' is repeated - Line : "+line);
    Flags.findErrors = true;
  }
  
  private void messageMissingMacroFunctions(String nameConstruct, ArrayList list) {
    String line = "not found";
    String name = "unknown";
     { Option tom_match13_1 = null; tom_match13_1 = (Option) currentTomStructureOrgTrack;matchlab_match13_pattern1: { String orgLine = null; String orgName = null; if(tom_is_fun_sym_OriginTracking(tom_match13_1)) { TomName tom_match13_1_1 = null; TomTerm tom_match13_1_2 = null; tom_match13_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match13_1); tom_match13_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match13_1); if(tom_is_fun_sym_Name(tom_match13_1_1)) { String tom_match13_1_1_1 = null; tom_match13_1_1_1 = (String) tom_get_slot_Name_string(tom_match13_1_1); orgName = (String) tom_match13_1_1_1; if(tom_is_fun_sym_Line(tom_match13_1_2)) { String tom_match13_1_2_1 = null; tom_match13_1_2_1 = (String) tom_get_slot_Line_string(tom_match13_1_2); orgLine = (String) tom_match13_1_2_1;
 
        line = orgLine;
        name = orgName;
       } } }} }
 
    System.out.println("\n *** Missing macro-functions for '"+nameConstruct+" "+name+"' declared at line "+line);
    System.out.println(" *** Missing functions : "+list);
    Flags.findErrors = true;
  }
  
    //////////////////////
    // RECORDS CONCERNS //
    //////////////////////
  
  private void verifyRecordStructure(OptionList option, String tomName, TomList args) throws TomException {
    TomSymbol symbol = symbolTable().getSymbol(tomName);
    if(symbol != null) {
      SlotList slotList = symbol.getSlotList();
        // list operator and constants have an emptySlotList
        // the length of the slotList corresponds to the arity of the operator
      if(slotList.isEmptySlotList()) {
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
     { TomTerm tom_match14_1 = null; tom_match14_1 = (TomTerm) pairSlotName;matchlab_match14_pattern1: { OptionList list = null; String name = null; if(tom_is_fun_sym_PairSlotAppl(tom_match14_1)) { TomName tom_match14_1_1 = null; TomTerm tom_match14_1_2 = null; tom_match14_1_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match14_1); tom_match14_1_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match14_1); if(tom_is_fun_sym_Name(tom_match14_1_1)) { String tom_match14_1_1_1 = null; tom_match14_1_1_1 = (String) tom_get_slot_Name_string(tom_match14_1_1); name = (String) tom_match14_1_1_1; if(tom_is_fun_sym_Appl(tom_match14_1_2)) { Option tom_match14_1_2_1 = null; TomName tom_match14_1_2_2 = null; TomList tom_match14_1_2_3 = null; tom_match14_1_2_1 = (Option) tom_get_slot_Appl_option(tom_match14_1_2); tom_match14_1_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match14_1_2); tom_match14_1_2_3 = (TomList) tom_get_slot_Appl_args(tom_match14_1_2); if(tom_is_fun_sym_Option(tom_match14_1_2_1)) { OptionList tom_match14_1_2_1_1 = null; tom_match14_1_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match14_1_2_1); list = (OptionList) tom_match14_1_2_1_1;
 
        String line = findOriginTrackingLine(list);
        System.out.println("\n *** Slot Name '" + name + "' is not correct: See method '"+methodName+ "' -  Line : "+line);
       } } } }} }
 
    System.out.println(" *** Possible Slot Names are : "+listOfPossibleSlot);
    Flags.findErrors = true;
  }
  
  private void messageSlotRepeatedError(TomTerm pairSlotName, String methodName) {
     { TomTerm tom_match15_1 = null; tom_match15_1 = (TomTerm) pairSlotName;matchlab_match15_pattern1: { OptionList list = null; String name = null; if(tom_is_fun_sym_PairSlotAppl(tom_match15_1)) { TomName tom_match15_1_1 = null; TomTerm tom_match15_1_2 = null; tom_match15_1_1 = (TomName) tom_get_slot_PairSlotAppl_slotName(tom_match15_1); tom_match15_1_2 = (TomTerm) tom_get_slot_PairSlotAppl_appl(tom_match15_1); if(tom_is_fun_sym_Name(tom_match15_1_1)) { String tom_match15_1_1_1 = null; tom_match15_1_1_1 = (String) tom_get_slot_Name_string(tom_match15_1_1); name = (String) tom_match15_1_1_1; if(tom_is_fun_sym_Appl(tom_match15_1_2)) { Option tom_match15_1_2_1 = null; TomName tom_match15_1_2_2 = null; TomList tom_match15_1_2_3 = null; tom_match15_1_2_1 = (Option) tom_get_slot_Appl_option(tom_match15_1_2); tom_match15_1_2_2 = (TomName) tom_get_slot_Appl_astName(tom_match15_1_2); tom_match15_1_2_3 = (TomList) tom_get_slot_Appl_args(tom_match15_1_2); if(tom_is_fun_sym_Option(tom_match15_1_2_1)) { OptionList tom_match15_1_2_1_1 = null; tom_match15_1_2_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match15_1_2_1); list = (OptionList) tom_match15_1_2_1_1;
 
        String line = findOriginTrackingLine(list);
        System.out.println("\n *** Same slot names can not be used several times: See method "+methodName+ "' -  Line : "+line);
        System.out.println(" *** Repeated slot Name : '"+name+"'");
       } } } }} }
 
    Flags.findErrors = true;
  }
  
  private void messageBracketError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name,optionList);
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
            String line = findOriginTrackingLine(name,optionList);
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
            String line = findOriginTrackingLine(name,optionList);
            messageNumberArgumentsError(nbExpectedArgs, nbFoundArgs, name, line);
          }
            // We ensure that the symbol is defined or has a Make declaration
          if ( !findMakeDeclOrDefSymbol(symbol.getOption().getOptionList()) )
             System.out.println("No make def found for "+name);
        }
      }
    }
  }
  private boolean findMakeDeclOrDefSymbol(OptionList list) {
    while(!list.isEmptyOptionList()) {
      Option term = list.getHead();
       { Option tom_match16_1 = null; tom_match16_1 = (Option) term;matchlab_match16_pattern1: { TomList makeArgsList = null; Option orgTrack = null; if(tom_is_fun_sym_DeclarationToOption(tom_match16_1)) { Declaration tom_match16_1_1 = null; tom_match16_1_1 = (Declaration) tom_get_slot_DeclarationToOption_astDeclaration(tom_match16_1); if(tom_is_fun_sym_MakeDecl(tom_match16_1_1)) { TomName tom_match16_1_1_1 = null; TomType tom_match16_1_1_2 = null; TomList tom_match16_1_1_3 = null; TargetLanguage tom_match16_1_1_4 = null; Option tom_match16_1_1_5 = null; tom_match16_1_1_1 = (TomName) tom_get_slot_MakeDecl_astName(tom_match16_1_1); tom_match16_1_1_2 = (TomType) tom_get_slot_MakeDecl_astType(tom_match16_1_1); tom_match16_1_1_3 = (TomList) tom_get_slot_MakeDecl_args(tom_match16_1_1); tom_match16_1_1_4 = (TargetLanguage) tom_get_slot_MakeDecl_tlCode(tom_match16_1_1); tom_match16_1_1_5 = (Option) tom_get_slot_MakeDecl_orgTrack(tom_match16_1_1); makeArgsList = (TomList) tom_match16_1_1_3; orgTrack = (Option) tom_match16_1_1_5;
 
          return true;
         } }}matchlab_match16_pattern2: { if(tom_is_fun_sym_DefinedSymbol(tom_match16_1)) {
 return true; }} }
 
      list = list.getTail();
    }
    return false;
  }

  private void messageSymbolError(String name, OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(name, optionList);
    String s = "Symbol method : '" + name + "' not found";
    messageError(line,s);
  }
  
  private void messageWarningSymbol(String name, OptionList optionList) {
    if(Flags.noWarning) return;
    String lineDecl = "not found",nameDecl = "unknown";
     { Option tom_match17_1 = null; tom_match17_1 = (Option) currentTomStructureOrgTrack;matchlab_match17_pattern1: { String declOrgName = null; String declOrgLine = null; if(tom_is_fun_sym_OriginTracking(tom_match17_1)) { TomName tom_match17_1_1 = null; TomTerm tom_match17_1_2 = null; tom_match17_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match17_1); tom_match17_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match17_1); if(tom_is_fun_sym_Name(tom_match17_1_1)) { String tom_match17_1_1_1 = null; tom_match17_1_1_1 = (String) tom_get_slot_Name_string(tom_match17_1_1); declOrgName = (String) tom_match17_1_1_1; if(tom_is_fun_sym_Line(tom_match17_1_2)) { String tom_match17_1_2_1 = null; tom_match17_1_2_1 = (String) tom_get_slot_Line_string(tom_match17_1_2); declOrgLine = (String) tom_match17_1_2_1;
 
        lineDecl = declOrgLine;nameDecl = declOrgName;
       } } }} }
 
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String line = findOriginTrackingLine(name, optionList);
    System.out.println("\n *** Warning *** possible error in structure "+nameDecl+" declared line "+lineDecl);
    System.out.println(" *** Unknown method '"+name+"' : Ensure the type coherence by yourself line : " + line);
  }
  
  private void messageNumberArgumentsError(int nbArg, int nbArg2, String name, String line) throws TomException {
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
         { TomTerm tom_match18_1 = null; tom_match18_1 = (TomTerm) currentRule;matchlab_match18_pattern1: { TomTerm lhs = null; TomTerm rhs = null; if(tom_is_fun_sym_RewriteRule(tom_match18_1)) { TomTerm tom_match18_1_1 = null; TomTerm tom_match18_1_2 = null; tom_match18_1_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match18_1); tom_match18_1_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match18_1); if(tom_is_fun_sym_Term(tom_match18_1_1)) { TomTerm tom_match18_1_1_1 = null; tom_match18_1_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match18_1_1); lhs = (TomTerm) tom_match18_1_1_1; if(tom_is_fun_sym_Term(tom_match18_1_2)) { TomTerm tom_match18_1_2_1 = null; tom_match18_1_2_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match18_1_2); rhs = (TomTerm) tom_match18_1_2_1;
 
            statistics().numberRulesTested++;
            name = verifyLhsRuleAndConstructorEgality(lhs, name, i);
            if(i == 0) {
                // update the root of lhs: it becomes a defined symbol
              ast().updateDefinedSymbol(symbolTable(),lhs);
            }
            verifyRhsRuleStructure(rhs);
            verifyRuleVariable(lhs,rhs);
            break matchBlock;
           } } }}matchlab_match18_pattern2: {
 
            System.out.println("Strange rule:\n" + currentRule);
            System.exit(1);
          } }
 
      }
      ruleList = ruleList.getTail();
      i++;
    }
  }
  
  private String verifyLhsRuleAndConstructorEgality(TomTerm lhs, String  ruleName, int ruleNumber) throws TomException {
    String methodName = "";
    OptionList options = tsf().makeOptionList_EmptyOptionList();
     { TomTerm tom_match19_1 = null; tom_match19_1 = (TomTerm) lhs;matchlab_match19_pattern1: { String name = null; OptionList optionList = null; TomTerm appl = null; TomList args = null; if(tom_is_fun_sym_Appl(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomList tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_Appl_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match19_1); tom_match19_1_3 = (TomList) tom_get_slot_Appl_args(tom_match19_1); appl = (TomTerm) tom_match19_1; if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; if(tom_is_fun_sym_Name(tom_match19_1_2)) { String tom_match19_1_2_1 = null; tom_match19_1_2_1 = (String) tom_get_slot_Name_string(tom_match19_1_2); name = (String) tom_match19_1_2_1; args = (TomList) tom_match19_1_3;
 
        // No alone variable noor simple constructor
        if( args.isEmpty() ) {
          String line = findOriginTrackingLine(optionList);
          messageRuleErrorVariable(name, line);
        }
        verify(appl);
        options = optionList;
        methodName = name;
       } } }}matchlab_match19_pattern2: { OptionList optionList = null; String name1 = null; if(tom_is_fun_sym_VariableStar(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomType tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match19_1); tom_match19_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match19_1); if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; if(tom_is_fun_sym_Name(tom_match19_1_2)) { String tom_match19_1_2_1 = null; tom_match19_1_2_1 = (String) tom_get_slot_Name_string(tom_match19_1_2); name1 = (String) tom_match19_1_2_1;
  
        String line = findOriginTrackingLine(name1,optionList);
        messageRuleErrorVariableStar(name1, line); 
       } } }}matchlab_match19_pattern3: { OptionList t = null; if(tom_is_fun_sym_Placeholder(tom_match19_1)) { Option tom_match19_1_1 = null; tom_match19_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match19_1); if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); t = (OptionList) tom_match19_1_1_1;
  
        messageRuleErrorLhsImpossiblePlaceHolder(t); 
       } }}matchlab_match19_pattern4: { TomTerm rec = null; TomList args = null; String name = null; OptionList optionList = null; if(tom_is_fun_sym_RecordAppl(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomList tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match19_1); tom_match19_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match19_1); rec = (TomTerm) tom_match19_1; if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; if(tom_is_fun_sym_Name(tom_match19_1_2)) { String tom_match19_1_2_1 = null; tom_match19_1_2_1 = (String) tom_get_slot_Name_string(tom_match19_1_2); name = (String) tom_match19_1_2_1; args = (TomList) tom_match19_1_3;
 
        System.out.println("Warning prefer f() instead of f[] or Message for impossible record on lhs of rule?"+rec);
        verify(rec);
        options = optionList;
        methodName = name;
       } } }} }
 
    if ( ruleNumber != 0 && !methodName.equals(ruleName)) {   
      messageRuleErrorConstructorEgality(methodName, ruleName, options);
      return ruleName;
    }
    return methodName;
  }

  private void messageRuleErrorVariable(String nameVariableStar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Alone variable or operator " +nameVariableStar+ " is not allowed on left hand side of structure %rule declared line "+lineDecl;
    messageError(line,s);
  }
  
  private void messageRuleErrorVariableStar(String nameVariableStar, String line) throws TomException {
    String lineDecl = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable " +nameVariableStar+ "* is not allowed on left hand side of structure %rule declared line "+lineDecl;
    messageError(line,s);
  }

  private void messageRuleErrorLhsImpossiblePlaceHolder(OptionList optionList) throws TomException {
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String line = findOriginTrackingLine(optionList);
    String s = "Alone placeholder : Not allowed in left hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorConstructorEgality(String  name, String nameExpected, OptionList optionList) throws TomException {
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String line = findOriginTrackingLine(name, optionList);
    String s = "Error in left hand side of structure %rule declared line " +declLine;
    s += "*** Method name '" + nameExpected + "' expected, but '" + name + "' found";
    messageError(line,s);
  }
  
  private void verifyRhsRuleStructure(TomTerm ruleRhs) throws TomException {
    matchBlock: {
       { TomTerm tom_match20_1 = null; tom_match20_1 = (TomTerm) ruleRhs;matchlab_match20_pattern1: { String name = null; OptionList options = null; TomTerm appl = null; TomList args = null; if(tom_is_fun_sym_Appl(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomList tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_Appl_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match20_1); tom_match20_1_3 = (TomList) tom_get_slot_Appl_args(tom_match20_1); appl = (TomTerm) tom_match20_1; if(tom_is_fun_sym_Option(tom_match20_1_1)) { OptionList tom_match20_1_1_1 = null; tom_match20_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match20_1_1); options = (OptionList) tom_match20_1_1_1; if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); name = (String) tom_match20_1_2_1; args = (TomList) tom_match20_1_3;
 
          permissiveVerify(appl);
          break matchBlock;
         } } }}matchlab_match20_pattern2: { OptionList option = null; if(tom_is_fun_sym_Placeholder(tom_match20_1)) { Option tom_match20_1_1 = null; tom_match20_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match20_1); if(tom_is_fun_sym_Option(tom_match20_1_1)) { OptionList tom_match20_1_1_1 = null; tom_match20_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match20_1_1); option = (OptionList) tom_match20_1_1_1;
 
          messageRuleErrorRhsImpossiblePlaceholder(option);
          break matchBlock;
         } }}matchlab_match20_pattern3: { OptionList option = null; String name = null; if(tom_is_fun_sym_VariableStar(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomType tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match20_1); tom_match20_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match20_1); if(tom_is_fun_sym_Option(tom_match20_1_1)) { OptionList tom_match20_1_1_1 = null; tom_match20_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match20_1_1); option = (OptionList) tom_match20_1_1_1; if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); name = (String) tom_match20_1_2_1;
 
          messageRuleErrorRhsImpossibleVarStar(option, name);
          break matchBlock;
         } } }}matchlab_match20_pattern4: { OptionList option = null; String tomName = null; if(tom_is_fun_sym_RecordAppl(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomList tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match20_1); tom_match20_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match20_1); if(tom_is_fun_sym_Option(tom_match20_1_1)) { OptionList tom_match20_1_1_1 = null; tom_match20_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match20_1_1); option = (OptionList) tom_match20_1_1_1; if(tom_is_fun_sym_Name(tom_match20_1_2)) { String tom_match20_1_2_1 = null; tom_match20_1_2_1 = (String) tom_get_slot_Name_string(tom_match20_1_2); tomName = (String) tom_match20_1_2_1;
 
          messageRuleErrorRhsImpossibleRecord(option, tomName);
          break matchBlock;
         } } }}matchlab_match20_pattern5: {
 
          System.out.println("Strange rule rhs:\n" + ruleRhs);
          System.exit(1);
        } }
 
    }
  }

  private void messageRuleErrorRhsImpossiblePlaceholder(OptionList optionList) throws TomException {
    String line = findOriginTrackingLine(optionList);
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = " *** Placeholder is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  
  private void messageRuleErrorRhsImpossibleRecord(OptionList optionList, String name) throws TomException {
    String line = findOriginTrackingLine(optionList);
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "\n *** Record '"+name+"[...]' is not allowed on right part of structure %rule declared line "+declLine;
    messageError(line,s);
  }
  private void messageRuleErrorRhsImpossibleVarStar(OptionList optionList, String name) throws TomException {
    String line = findOriginTrackingLine(optionList);
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    String s = "Single list variable '"+name+"*' is not allowed in right hand side of structure %rule declared line " +declLine;
    messageError(line,s);
  }
  
    // We test the non existence of variables from the right part of '->' in the left part of '->'.
  private void verifyRuleVariable(TomTerm lhs, TomTerm rhs) throws TomException {
      // We extract variable informations of the left part.
    Collection variableCollectionLhs = new HashSet();
    collectVariable(variableCollectionLhs, lhs);
    Collection variableCollectionRhs = new HashSet();
    collectVariable(variableCollectionRhs, rhs);
   
    if( !variableCollectionLhs.containsAll(variableCollectionRhs) ) {
//      messageRuleErrorUnknownVariable((String)nameVariableOut.get(i),(String)lineVariableOut.get(i));
      System.out.println("Missing var decl");
      System.exit(1);
    }
  }
  
  public void collectVariable(final Collection collection, TomTerm subject) throws TomException {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) throws TomException {
            //%variable
          if(t instanceof TomTerm) {
            String annotedVariable = null;
              //System.out.println(t);
             { TomTerm tom_match21_1 = null; tom_match21_1 = (TomTerm) t;matchlab_match21_pattern1: { String name = null; OptionList optionList = null; TomTerm appl = null; TomList subterms = null; if(tom_is_fun_sym_Appl(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomList tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_Appl_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match21_1); tom_match21_1_3 = (TomList) tom_get_slot_Appl_args(tom_match21_1); appl = (TomTerm) tom_match21_1; if(tom_is_fun_sym_Option(tom_match21_1_1)) { OptionList tom_match21_1_1_1 = null; tom_match21_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match21_1_1); optionList = (OptionList) tom_match21_1_1_1; if(tom_is_fun_sym_Name(tom_match21_1_2)) { String tom_match21_1_2_1 = null; tom_match21_1_2_1 = (String) tom_get_slot_Name_string(tom_match21_1_2); name = (String) tom_match21_1_2_1; subterms = (TomList) tom_match21_1_3;

 
                if( subterms.isEmpty() && getLRParen(optionList)==null && symbolTable().getSymbol(name)==null) 
                  collection.add(name);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return true;
               } } }}matchlab_match21_pattern2: { String tomName = null; OptionList optionList = null; TomList pairs = null; if(tom_is_fun_sym_RecordAppl(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomList tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_RecordAppl_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_RecordAppl_astName(tom_match21_1); tom_match21_1_3 = (TomList) tom_get_slot_RecordAppl_args(tom_match21_1); if(tom_is_fun_sym_Option(tom_match21_1_1)) { OptionList tom_match21_1_1_1 = null; tom_match21_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match21_1_1); optionList = (OptionList) tom_match21_1_1_1; if(tom_is_fun_sym_Name(tom_match21_1_2)) { String tom_match21_1_2_1 = null; tom_match21_1_2_1 = (String) tom_get_slot_Name_string(tom_match21_1_2); tomName = (String) tom_match21_1_2_1; pairs = (TomList) tom_match21_1_3;
 
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return true;
               } } }}matchlab_match21_pattern3: { OptionList optionList = null; if(tom_is_fun_sym_VariableStar(tom_match21_1)) { Option tom_match21_1_1 = null; TomName tom_match21_1_2 = null; TomType tom_match21_1_3 = null; tom_match21_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match21_1); tom_match21_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match21_1); tom_match21_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match21_1); if(tom_is_fun_sym_Option(tom_match21_1_1)) { OptionList tom_match21_1_1_1 = null; tom_match21_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match21_1_1); optionList = (OptionList) tom_match21_1_1_1;
 
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match21_pattern4: { OptionList optionList = null; if(tom_is_fun_sym_Placeholder(tom_match21_1)) { Option tom_match21_1_1 = null; tom_match21_1_1 = (Option) tom_get_slot_Placeholder_option(tom_match21_1); if(tom_is_fun_sym_Option(tom_match21_1_1)) { OptionList tom_match21_1_1_1 = null; tom_match21_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match21_1_1); optionList = (OptionList) tom_match21_1_1_1;
 
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match21_pattern5: {
  return true; } }
 
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
       { Option tom_match22_1 = null; tom_match22_1 = (Option) subject;matchlab_match22_pattern1: { String name1 = null; if(tom_is_fun_sym_TomNameToOption(tom_match22_1)) { TomName tom_match22_1_1 = null; tom_match22_1_1 = (TomName) tom_get_slot_TomNameToOption_astName(tom_match22_1); if(tom_is_fun_sym_Name(tom_match22_1_1)) { String tom_match22_1_1_1 = null; tom_match22_1_1_1 = (String) tom_get_slot_Name_string(tom_match22_1_1); name1 = (String) tom_match22_1_1_1;
 
          return name1;
         } }} }
 
      subjectList = subjectList.getTail();
    }
    return null;
  }
  
  private void messageRuleErrorUnknownVariable(String name, String line) {
    String declLine = findOriginTrackingLine(currentTomStructureOrgTrack);
    System.out.println("\n *** Variables used on right part of rule have to be declared in left part of rule declared line "+declLine);
    System.out.println("Variable '"+name+"' not decared - Line : "+line);
    Flags.findErrors = true;
  }

  
    /////////////
    // GLOBALS //
    /////////////
  // findOriginTrackingLine(_) method returns the first number of line (stocked in optionList).
  private String findOriginTrackingLine(OptionList optionList) {
    while(!optionList.isEmptyOptionList()) {
      Option subject = optionList.getHead();
       { Option tom_match23_1 = null; tom_match23_1 = (Option) subject;matchlab_match23_pattern1: { String line = null; if(tom_is_fun_sym_OriginTracking(tom_match23_1)) { TomName tom_match23_1_1 = null; TomTerm tom_match23_1_2 = null; tom_match23_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match23_1); tom_match23_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match23_1); if(tom_is_fun_sym_Line(tom_match23_1_2)) { String tom_match23_1_2_1 = null; tom_match23_1_2_1 = (String) tom_get_slot_Line_string(tom_match23_1_2); line = (String) tom_match23_1_2_1;
 
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
       { Option tom_match24_1 = null; tom_match24_1 = (Option) subject;matchlab_match24_pattern1: { Option orgTrack = null; if(tom_is_fun_sym_OriginTracking(tom_match24_1)) { TomName tom_match24_1_1 = null; TomTerm tom_match24_1_2 = null; tom_match24_1_1 = (TomName) tom_get_slot_OriginTracking_astName(tom_match24_1); tom_match24_1_2 = (TomTerm) tom_get_slot_OriginTracking_line(tom_match24_1); orgTrack = (Option) tom_match24_1;
 
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

} //class verify
