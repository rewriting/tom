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

public class TomKernelCompiler extends TomBase {
  private String debugKey = null;
  public TomKernelCompiler(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
                                                                                                                                                                         
// ------------------------------------------------------------

    /* 
     * preProcessing:
     *
     * replaces MakeTerm
     */

  private static int matchNumber = 0;

  private Option option() {
    return ast().makeOption();
  }
  
  Replace1 replace_preProcessing = new Replace1() {
      public ATerm apply(ATerm t) { return preProcessing((TomTerm)t); }
    };
  
  Replace1 replace_preProcessing_makeTerm = new Replace1() {
      public ATerm apply(ATerm t) {
        TomTerm subject = (TomTerm)t;
        return preProcessing(tom_make_MakeTerm(subject) );
      }
    }; 

  public TomTerm preProcessing(TomTerm subject) {
      //%variable
      //System.out.println("preProcessing subject: " + subject);
     {  TomTerm tom_match1_1 = null; tom_match1_1 = ( TomTerm) subject;matchlab_match1_pattern1: {  TomList l = null; if(tom_is_fun_sym_Tom(tom_match1_1)) {  TomList tom_match1_1_1 = null; tom_match1_1_1 = ( TomList) tom_get_slot_Tom_list(tom_match1_1); l = ( TomList) tom_match1_1_1;
 
        return tom_make_Tom(tomListMap(l, replace_preProcessing)) ;
       }}matchlab_match1_pattern2: {  TomTerm var = null;  TomName name = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) {  TomTerm tom_match1_1_1 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_Variable(tom_match1_1_1)) {  Option tom_match1_1_1_1 = null;  TomName tom_match1_1_1_2 = null;  TomType tom_match1_1_1_3 = null; tom_match1_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match1_1_1); tom_match1_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match1_1_1); tom_match1_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match1_1_1); var = ( TomTerm) tom_match1_1_1; name = ( TomName) tom_match1_1_1_2;

 
        statistics().numberMakeTermReplaced++;
        return var;
       } }}matchlab_match1_pattern3: {  TomTerm var = null;  TomName name = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) {  TomTerm tom_match1_1_1 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_VariableStar(tom_match1_1_1)) {  Option tom_match1_1_1_1 = null;  TomName tom_match1_1_1_2 = null;  TomType tom_match1_1_1_3 = null; tom_match1_1_1_1 = ( Option) tom_get_slot_VariableStar_option(tom_match1_1_1); tom_match1_1_1_2 = ( TomName) tom_get_slot_VariableStar_astName(tom_match1_1_1); tom_match1_1_1_3 = ( TomType) tom_get_slot_VariableStar_astType(tom_match1_1_1); var = ( TomTerm) tom_match1_1_1; name = ( TomName) tom_match1_1_1_2;

 
        statistics().numberMakeTermReplaced++;
        return var;
       } }}matchlab_match1_pattern4: {  TomList termArgs = null;  OptionList optionList = null;  TomName name = null;  String tomName = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) {  TomTerm tom_match1_1_1 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_Appl(tom_match1_1_1)) {  Option tom_match1_1_1_1 = null;  TomName tom_match1_1_1_2 = null;  TomList tom_match1_1_1_3 = null; tom_match1_1_1_1 = ( Option) tom_get_slot_Appl_option(tom_match1_1_1); tom_match1_1_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match1_1_1); tom_match1_1_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match1_1_1); if(tom_is_fun_sym_Option(tom_match1_1_1_1)) {  OptionList tom_match1_1_1_1_1 = null; tom_match1_1_1_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match1_1_1_1); optionList = ( OptionList) tom_match1_1_1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_1_2)) {  String tom_match1_1_1_2_1 = null; tom_match1_1_1_2_1 = ( String) tom_get_slot_Name_string(tom_match1_1_1_2); name = ( TomName) tom_match1_1_1_2; tomName = ( String) tom_match1_1_1_2_1; termArgs = ( TomList) tom_match1_1_1_3;

 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        statistics().numberMakeTermReplaced++;
        TomList newTermArgs = tomListMap(termArgs,replace_preProcessing_makeTerm);

        if(tomSymbol==null || isDefinedSymbol(tomSymbol)) {
          return tom_make_FunctionCall(name,newTermArgs) ;
        } else {
          if(isListOperator(tomSymbol)) {
            return tom_make_BuildList(name,newTermArgs) ;
          } else if(isArrayOperator(tomSymbol)) {
            return tom_make_BuildArray(name,newTermArgs) ;
          } else {
            return tom_make_BuildTerm(name,newTermArgs) ;
          }
        }
       } } } }}matchlab_match1_pattern5: {  TomTerm t = null; t = ( TomTerm) tom_match1_1;


 
          //System.out.println("preProcessing default: " + t);
        return t;
      } }
 
  }

    /* 
     * compileMatching:
     *
     * compiles Match into and automaton
     */
 
  public TomTerm compileMatching(TomTerm subject) {
      //%variable
    Replace1 replace_compileMatching = new Replace1() {
        public ATerm apply(ATerm t) { return compileMatching((TomTerm)t); }
      }; 

     {  TomTerm tom_match2_1 = null; tom_match2_1 = ( TomTerm) subject;matchlab_match2_pattern1: {  TomList l = null; if(tom_is_fun_sym_Tom(tom_match2_1)) {  TomList tom_match2_1_1 = null; tom_match2_1_1 = ( TomList) tom_get_slot_Tom_list(tom_match2_1); l = ( TomList) tom_match2_1_1;
 
        return tom_make_Tom(tomListMap(l, replace_compileMatching)) ;
       }}matchlab_match2_pattern2: {  TomList l1 = null;  OptionList optionList = null;  Option matchOption = null;  TomList l2 = null; if(tom_is_fun_sym_Match(tom_match2_1)) {  TomTerm tom_match2_1_1 = null;  TomTerm tom_match2_1_2 = null;  Option tom_match2_1_3 = null; tom_match2_1_1 = ( TomTerm) tom_get_slot_Match_subjectList(tom_match2_1); tom_match2_1_2 = ( TomTerm) tom_get_slot_Match_patternList(tom_match2_1); tom_match2_1_3 = ( Option) tom_get_slot_Match_option(tom_match2_1); if(tom_is_fun_sym_SubjectList(tom_match2_1_1)) {  TomList tom_match2_1_1_1 = null; tom_match2_1_1_1 = ( TomList) tom_get_slot_SubjectList_list(tom_match2_1_1); l1 = ( TomList) tom_match2_1_1_1; if(tom_is_fun_sym_PatternList(tom_match2_1_2)) {  TomList tom_match2_1_2_1 = null; tom_match2_1_2_1 = ( TomList) tom_get_slot_PatternList_list(tom_match2_1_2); l2 = ( TomList) tom_match2_1_2_1; if(tom_is_fun_sym_Option(tom_match2_1_3)) {  OptionList tom_match2_1_3_1 = null; tom_match2_1_3_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match2_1_3); matchOption = ( Option) tom_match2_1_3; optionList = ( OptionList) tom_match2_1_3_1;

 
        statistics().numberMatchCompiledIntoAutomaton++;
        boolean generatedMatch = false;
        if(Flags.debugMode) {
          generatedMatch = hasGeneratedMatch(optionList);
          Option orgTrack = findOriginTracking(optionList);
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine().toString();
        }
        
        TomList patternList, actionList;
        TomList automataList = empty();
        ArrayList list;
        TomList path = empty();

        matchNumber++;
        path = append(tom_make_MatchNumber(makeNumber(matchNumber)) ,path);
        
          /*
           * create a list of declaration
           * collect and declare TOM variables (from patterns)
           * collect match variables (from match(t1,...,tn))
           * declare and assign intern variable (_1 = t1,..., _n=tn)
           */
        TomList matchDeclarationList = empty();
        TomList matchAssignementList = empty();
        int index = 1;

        while(!l1.isEmpty()) {
          TomTerm tlVariable = l1.getHead();
          matchBlock: {
             {  TomTerm tom_match3_1 = null; tom_match3_1 = ( TomTerm) tlVariable;matchlab_match3_pattern1: {  TomType variableType = null;  Option option = null; if(tom_is_fun_sym_Variable(tom_match3_1)) {  Option tom_match3_1_1 = null;  TomName tom_match3_1_2 = null;  TomType tom_match3_1_3 = null; tom_match3_1_1 = ( Option) tom_get_slot_Variable_option(tom_match3_1); tom_match3_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match3_1); tom_match3_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match3_1); option = ( Option) tom_match3_1_1; variableType = ( TomType) tom_match3_1_3;
 
                TomTerm variable = tom_make_Variable(option,tom_make_PositionName(append(makeNumber(index), path)),variableType) ;
                matchDeclarationList = append(tom_make_Declaration(variable) ,matchDeclarationList);
                if (!generatedMatch) {
                  matchAssignementList = appendInstruction(tom_make_AssignMatchSubject(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);
                } else {
                  matchAssignementList = appendInstruction(tom_make_Assign(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);
                }
                break matchBlock;
               }}matchlab_match3_pattern2: {  String tomName = null; if(tom_is_fun_sym_BuildTerm(tom_match3_1)) {  TomName tom_match3_1_1 = null;  TomList tom_match3_1_2 = null; tom_match3_1_1 = ( TomName) tom_get_slot_BuildTerm_astName(tom_match3_1); tom_match3_1_2 = ( TomList) tom_get_slot_BuildTerm_args(tom_match3_1); if(tom_is_fun_sym_Name(tom_match3_1_1)) {  String tom_match3_1_1_1 = null; tom_match3_1_1_1 = ( String) tom_get_slot_Name_string(tom_match3_1_1); tomName = ( String) tom_match3_1_1_1;



 
                TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
                TomType tomType = getSymbolCodomain(tomSymbol);
                TomTerm variable = tom_make_Variable(option(),tom_make_PositionName(append(makeNumber(index), path)),tomType) ;
                matchDeclarationList = append(tom_make_Declaration(variable) ,matchDeclarationList);
                matchAssignementList = appendInstruction(tom_make_Assign(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);
                break matchBlock;
               } }}matchlab_match3_pattern3: {  String tomName = null; if(tom_is_fun_sym_BuildList(tom_match3_1)) {  TomName tom_match3_1_1 = null;  TomList tom_match3_1_2 = null; tom_match3_1_1 = ( TomName) tom_get_slot_BuildList_astName(tom_match3_1); tom_match3_1_2 = ( TomList) tom_get_slot_BuildList_args(tom_match3_1); if(tom_is_fun_sym_Name(tom_match3_1_1)) {  String tom_match3_1_1_1 = null; tom_match3_1_1_1 = ( String) tom_get_slot_Name_string(tom_match3_1_1); tomName = ( String) tom_match3_1_1_1;                  TomSymbol tomSymbol = symbolTable().getSymbol(tomName);                 TomType tomType = getSymbolCodomain(tomSymbol);                 TomTerm variable = tom_make_Variable(option(),tom_make_PositionName(append(makeNumber(index), path)),tomType) ;                 matchDeclarationList = append(tom_make_Declaration(variable) ,matchDeclarationList);                 matchAssignementList = appendInstruction(tom_make_Assign(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);                 break matchBlock;                } }}matchlab_match3_pattern4: {  String tomName = null; if(tom_is_fun_sym_FunctionCall(tom_match3_1)) {  TomName tom_match3_1_1 = null;  TomList tom_match3_1_2 = null; tom_match3_1_1 = ( TomName) tom_get_slot_FunctionCall_astName(tom_match3_1); tom_match3_1_2 = ( TomList) tom_get_slot_FunctionCall_args(tom_match3_1); if(tom_is_fun_sym_Name(tom_match3_1_1)) {  String tom_match3_1_1_1 = null; tom_match3_1_1_1 = ( String) tom_get_slot_Name_string(tom_match3_1_1); tomName = ( String) tom_match3_1_1_1;                  TomSymbol tomSymbol = symbolTable().getSymbol(tomName);                 TomType tomType = getSymbolCodomain(tomSymbol);                 TomTerm variable = tom_make_Variable(option(),tom_make_PositionName(append(makeNumber(index), path)),tomType) ;                 matchDeclarationList = append(tom_make_Declaration(variable) ,matchDeclarationList);                 matchAssignementList = appendInstruction(tom_make_Assign(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);                 break matchBlock;                } }}matchlab_match3_pattern5: {

 
                System.out.println("compileMatching: stange term: " + tlVariable);
                break matchBlock;
              } }
 
          } // end matchBlock
          index++;
          l1 = l1.getTail();
        }
        
        matchDeclarationList = concat(matchDeclarationList,matchAssignementList);
  
          /*
           * for each pattern action (<term> -> <action>)
           * build a matching automata
           */
        int actionNumber = 0;
        boolean firstCall=true;
        while(!l2.isEmpty()) {
          actionNumber++;
          TomTerm pa = l2.getHead();
          patternList = pa.getTermList().getList();
          actionList = pa.getTom().getList();

            //System.out.println("patternList   = " + patternList);
            //System.out.println("actionList = " + actionList);
          if(patternList==null || actionList==null) {
            System.out.println("TomKernelCompiler: null value");
            System.exit(1);
          }
          
            // compile nested match constructs
          actionList = tomListMap(actionList,replace_compileMatching);
            //System.out.println("patternList      = " + patternList);
            //System.out.println("actionNumber  = " + actionNumber);
            //System.out.println("action        = " + actionList);
          TomList patternsDeclarationList = empty();
          Collection variableCollection = new HashSet();
          collectVariable(variableCollection,tom_make_Tom(patternList) );

          Iterator it = variableCollection.iterator();
          while(it.hasNext()) {
            TomTerm tmpsubject = (TomTerm)it.next();
            patternsDeclarationList = append(tom_make_Declaration(tmpsubject) ,patternsDeclarationList);
              //System.out.println("*** " + patternsDeclarationList);
          }

          TomList numberList = append(tom_make_PatternNumber(makeNumber(actionNumber)) ,path);
          TomList instructionList;
          instructionList = genMatchingAutomataFromPatternList(patternList,path,1,actionList,true);
            //firstCall = false;
          TomList declarationInstructionList; 
          declarationInstructionList = concat(patternsDeclarationList,instructionList);
          TomTerm automata = tom_make_Automata(numberList,declarationInstructionList) ;
            //System.out.println("automata = " + automata);
    
          automataList = append(automata,automataList);
          l2 = l2.getTail();
        }

          /*
           * return the compiled MATCH construction
           */

        TomList astAutomataList = automataListCompileMatchingList(automataList, generatedMatch);
        return tom_make_CompiledMatch(matchDeclarationList,astAutomataList,matchOption) ;
       } } } }}matchlab_match2_pattern3: {  TomTerm t = null; t = ( TomTerm) tom_match2_1;


 
          //System.out.println("default: " + t);
        return t;
      } }
 
  }

  private TomList automataListCompileMatchingList(TomList automataList, boolean generatedMatch) {
      //%variable
    
     {  TomList tom_match4_1 = null; tom_match4_1 = ( TomList) automataList;matchlab_match4_pattern1: { if(tom_is_fun_sym_Empty(tom_match4_1)) {


  return empty();  }}matchlab_match4_pattern2: {  TomList l = null;  TomList instList = null;  TomList numberList = null; if(tom_is_fun_sym_Cons(tom_match4_1)) {  TomTerm tom_match4_1_1 = null;  TomList tom_match4_1_2 = null; tom_match4_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match4_1); tom_match4_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match4_1); if(tom_is_fun_sym_Automata(tom_match4_1_1)) {  TomList tom_match4_1_1_1 = null;  TomList tom_match4_1_1_2 = null; tom_match4_1_1_1 = ( TomList) tom_get_slot_Automata_numberList(tom_match4_1_1); tom_match4_1_1_2 = ( TomList) tom_get_slot_Automata_instList(tom_match4_1_1); numberList = ( TomList) tom_match4_1_1_1; instList = ( TomList) tom_match4_1_1_2; l = ( TomList) tom_match4_1_2;
 
        TomList newList = automataListCompileMatchingList(l, generatedMatch);
        if(Flags.supportedGoto) {
          if(!generatedMatch && Flags.debugMode) {
            TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.enteringPattern(\""+debugKey+"\");\n");
            instList = cons(tom_make_TargetLanguageToTomTerm(tl), instList) ;
            tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.leavingPattern(\""+debugKey+"\");\n");
            TomList list = cons(tom_make_TargetLanguageToTomTerm(tl), tom_make_Empty()) ;
            instList = concat(instList, list);
          }
          TomTerm compiledPattern = tom_make_CompiledPattern(cons(tom_make_InstructionToTomTerm(tom_make_NamedBlock(getBlockName(numberList),instList)), empty())) ;

          return cons(compiledPattern, newList);
        } else {
          TomList result = empty();
          TomTerm variableAST = getBlockVariable(numberList);
          result = append(tom_make_Declaration(variableAST) ,result);
          result = appendInstruction(tom_make_Assign(variableAST,tom_make_TrueTL()) ,result);
          if(Flags.supportedBlock) { // Test
            result = appendInstruction(tom_make_OpenBlock() ,result);
          }
          result = concat(result,instList);
          if(Flags.supportedBlock) { // Test
            result = appendInstruction(tom_make_CloseBlock() ,result);
          }
          result = cons(tom_make_CompiledPattern(result) ,newList);
          return result;
        }
       } }} }
 
    return null;
  }

  private String getBlockName(TomList numberList) {
    String name = "matchlab" + numberListToIdentifier(numberList);
    return name;
  }

  private TomTerm getBlockVariable(TomList numberList) {
    String name = "matchlab" + numberListToIdentifier(numberList);
    return tom_make_Variable(option(),tom_make_Name(name),getBoolType()) ;
  }
  
    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */

  TomList genMatchingAutomataFromPatternList(TomList termList,
                                                   TomList path,
                                                   int indexTerm,
                                                   TomList actionList,
                                                   boolean gsa) {
    TomList result = empty();
      //%variable
    if(termList.isEmpty()) {
      if(gsa) {
          // insert the semantic action
        Instruction action = tom_make_Action(actionList) ;
        result = appendInstruction(action,result);
      }
    } else {
      TomTerm head = termList.getHead();
      TomList tail = termList.getTail();
      TomList newSubActionList = genMatchingAutomataFromPatternList(tail,path,indexTerm+1,actionList,gsa);
      TomList newPath          = append(makeNumber(indexTerm),path);
      TomList newActionList    = genMatchingAutomataFromPattern(head,newPath,newSubActionList,gsa);
      result                   = concat(result,newActionList);
    }
    return result;
  }

  TomList genMatchingAutomataFromPattern(TomTerm term,
                                         TomList path,
                                         TomList actionList,
                                         boolean gsa) {
    TomList result = empty();
      //%variable
    matchBlock: {
       {  TomTerm tom_match5_1 = null; tom_match5_1 = ( TomTerm) term;matchlab_match5_pattern1: {  TomTerm var = null;  TomType termType = null;  OptionList optionList = null; if(tom_is_fun_sym_Variable(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomType tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match5_1); var = ( TomTerm) tom_match5_1; if(tom_is_fun_sym_Option(tom_match5_1_1)) {  OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); optionList = ( OptionList) tom_match5_1_1_1; termType = ( TomType) tom_match5_1_3;
 
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          Instruction assignement = tom_make_Assign(var,tom_make_TomTermToExpression(tom_make_Variable(option(),tom_make_PositionName(path),termType))) ;
          result = appendInstruction(assignement,result);

          if(annotedVariable != null) {
            assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(var)) ; 
            result = appendInstruction(assignement,result);
          }
          if(gsa) {
            result = appendInstruction(tom_make_Action(actionList) ,result);
          }
          
          break matchBlock;
         } }}matchlab_match5_pattern2: {  TomType termType = null;  OptionList optionList = null; if(tom_is_fun_sym_UnamedVariable(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomType tom_match5_1_2 = null; tom_match5_1_1 = ( Option) tom_get_slot_UnamedVariable_option(tom_match5_1); tom_match5_1_2 = ( TomType) tom_get_slot_UnamedVariable_astType(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) {  OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); optionList = ( OptionList) tom_match5_1_1_1; termType = ( TomType) tom_match5_1_2;

 
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          if(annotedVariable != null) {
            Instruction assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(tom_make_Variable(option(),tom_make_PositionName(path),termType))) ;
            result = appendInstruction(assignement,result);
          }
          if(gsa) {
            result = appendInstruction(tom_make_Action(actionList) ,result);
          }

          break matchBlock;
         } }}matchlab_match5_pattern3: {  OptionList optionList = null;  String tomName = null;  TomList termArgs = null; if(tom_is_fun_sym_Appl(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomList tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Appl_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match5_1); tom_match5_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) {  OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); optionList = ( OptionList) tom_match5_1_1_1; if(tom_is_fun_sym_Name(tom_match5_1_2)) {  String tom_match5_1_2_1 = null; tom_match5_1_2_1 = ( String) tom_get_slot_Name_string(tom_match5_1_2); tomName = ( String) tom_match5_1_2_1; termArgs = ( TomList) tom_match5_1_3;

 
          TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
          TomName termNameAST = tomSymbol.getAstName();
          TomList termTypeList = tomSymbol.getTypesToType().getList();
          TomType termType = tomSymbol.getTypesToType().getCodomain();
          OptionList termOptionList = tomSymbol.getOption().getOptionList();
          
          TomTerm annotedVariable = getAnnotedVariable(optionList);

            // SUCCES
          TomList declarationList = empty();
          TomList assignementList = empty();
          TomList annotedAssignementList = empty();
          
          int indexSubterm = 0;
          TomTerm subjectVariableAST =  tom_make_Variable(option(),tom_make_PositionName(path),termType) ; 
          while(!termTypeList.isEmpty()) {
            TomType subtermType = termTypeList.getHead().getAstType();
            TomList newPath = append(makeNumber(indexSubterm+1),path);
            TomTerm newVariableAST = tom_make_Variable(option(),tom_make_PositionName(newPath),subtermType) ;
            TomTerm declaration    = tom_make_Declaration(newVariableAST) ;
            declarationList      = append(declaration,declarationList);
            
            Expression getSubtermAST;

            TomName slotName = getSlotName(tomSymbol, indexSubterm);
            if(slotName == null) {
              getSubtermAST = tom_make_GetSubterm(subjectVariableAST,makeNumber(indexSubterm)) ;
            } else {
              getSubtermAST = tom_make_GetSlot(termNameAST,slotName.getString(),subjectVariableAST) ;
            }
            
            Instruction assignement  = tom_make_Assign(newVariableAST,getSubtermAST) ;
            assignementList      = appendInstruction(assignement,assignementList);
            
            indexSubterm++;
            termTypeList = termTypeList.getTail();
          }
          
            // generate an assignement for annoted variables
          if(annotedVariable != null) {
            Instruction assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(subjectVariableAST)) ;
            annotedAssignementList   = appendInstruction(assignement,annotedAssignementList);
          }
          
          TomList automataList  = null;
          TomList succesList    = empty();
          
          if(isListOperator(tomSymbol)) {
            int tmpIndexSubterm = 1;
            automataList = genListMatchingAutomata(tomSymbol,
                                                   termArgs,path,actionList,
                                                   gsa,subjectVariableAST, tmpIndexSubterm);
          } else if(isArrayOperator(tomSymbol)) {
            int tmpIndexSubterm = 1;
            automataList = genArrayMatchingAutomata(tomSymbol,
                                                    termArgs,path,actionList,
                                                    gsa,subjectVariableAST, subjectVariableAST,
                                                    tmpIndexSubterm);
          } else {
            automataList = genMatchingAutomataFromPatternList(termArgs,path,1,actionList,gsa);

            succesList = concat(succesList,declarationList);
            succesList = concat(succesList,assignementList);
          }
          succesList = concat(succesList,annotedAssignementList);
          succesList = concat(succesList,automataList);
          
          Expression cond = tom_make_EqualFunctionSymbol(subjectVariableAST,term) ;
          Instruction test = tom_make_IfThenElse(cond,succesList,empty()) ;
          result = appendInstruction(test,result);
          
          break matchBlock;
         } } }}matchlab_match5_pattern4: {

 
          System.out.println("GenTermMatchingAutomata strange term: " + term);
          System.exit(1);
        } }
 
    } // end matchBlock 

      //System.out.println("*** result = " + result);
    return result;
  }
  
  TomList genListMatchingAutomata(TomSymbol symbol,
                                  TomList termList,
                                  TomList oldPath,
                                  TomList actionList,
                                  boolean generateSemanticAction,
                                  TomTerm subjectListName,
                                  int indexTerm) {
    TomTerm term;
    TomList result = empty();
      //%variable

//     if(termList.isEmpty() && indexTerm >1) {
//       return result;
//     }
    
    TomTerm variableListAST = null;
    if(indexTerm > 1) {
      variableListAST = subjectListName;
    } else {
      TomList pathList = append(tom_make_ListNumber(makeNumber(indexTerm)) ,oldPath);
      
       {  TomTerm tom_match6_1 = null; tom_match6_1 = ( TomTerm) subjectListName;matchlab_match6_pattern1: {  Option option = null;  TomType termType = null; if(tom_is_fun_sym_Variable(tom_match6_1)) {  Option tom_match6_1_1 = null;  TomName tom_match6_1_2 = null;  TomType tom_match6_1_3 = null; tom_match6_1_1 = ( Option) tom_get_slot_Variable_option(tom_match6_1); tom_match6_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match6_1); tom_match6_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match6_1); option = ( Option) tom_match6_1_1; termType = ( TomType) tom_match6_1_3;
 
          variableListAST = tom_make_Variable(option(),tom_make_PositionName(pathList),termType) ;
         }} }
 
      result = append(tom_make_Declaration(variableListAST) ,result);
      result = appendInstruction(tom_make_Assign(variableListAST,tom_make_TomTermToExpression(subjectListName)) ,result);

      subjectListName = variableListAST;
    } 
    
    TomList subList;
    if(termList.isEmpty()) {
      subList = empty();
    } else {
      subList = genListMatchingAutomata(symbol,
                                        termList.getTail(), oldPath, actionList,
                                        generateSemanticAction,variableListAST,indexTerm+1);
    }
      //System.out.println("\ntermList = " + termList);
      //System.out.println("*** genListMatchingAutomata"); 
    matchBlock: {
       {  TomList tom_match7_1 = null; tom_match7_1 = ( TomList) termList;matchlab_match7_pattern1: { if(tom_is_fun_sym_Empty(tom_match7_1)) {


 
            /*
             * generate:
             * ---------
             * if(IS_EMPTY_TomList(subjectList)) {
             *   ...
             * }
             */
          if(indexTerm > 1) {
            break matchBlock;
          } else {
            if(generateSemanticAction) {
              subList = appendInstruction(tom_make_Action(actionList) ,subList);
            }
            Expression cond = tom_make_IsEmptyList(subjectListName) ;
            Instruction test = tom_make_IfThenElse(cond,subList,empty()) ;
            result = appendInstruction(test,result);
            break matchBlock;
          }
         }}matchlab_match7_pattern2: {  TomType termType = null;  Option option = null;  TomTerm var = null;  TomList termTail = null; if(tom_is_fun_sym_Cons(tom_match7_1)) {  TomTerm tom_match7_1_1 = null;  TomList tom_match7_1_2 = null; tom_match7_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match7_1); tom_match7_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match7_1); if(tom_is_fun_sym_Variable(tom_match7_1_1)) {  Option tom_match7_1_1_1 = null;  TomName tom_match7_1_1_2 = null;  TomType tom_match7_1_1_3 = null; tom_match7_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match7_1_1); tom_match7_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match7_1_1); tom_match7_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match7_1_1); var = ( TomTerm) tom_match7_1_1; option = ( Option) tom_match7_1_1_1; termType = ( TomType) tom_match7_1_1_3; termTail = ( TomList) tom_match7_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList)) {
               *   TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
               *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
               *   if(IS_EMPTY_TomList(subjectList)) {
               *     ...
               *   }
               * }
               */            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(tom_make_Assign(var,tom_make_GetHead(subjectListName)) ,assignementList);
            assignementList = appendInstruction(tom_make_Assign(subjectListName,tom_make_GetTail(subjectListName)) ,assignementList);
            
            if(generateSemanticAction) {
              subList = appendInstruction(tom_make_Action(actionList) ,subList);
            }
            
            Expression cond = tom_make_IsEmptyList(subjectListName) ;
            
            Instruction test = tom_make_IfThenElse(cond,subList,empty()) ;
            TomList succesList = appendInstruction(test,concat(declarationList,assignementList));
            
            cond = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = appendInstruction(test,result);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList)) {
               *   TomTerm x_j = (TomTerm) GET_HEAD_TomList(subjectList);
               *   subjectList =  (TomList) GET_TAIL_TomList(subjectList);
               *   ...
               * }
               */
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(tom_make_Assign(var,tom_make_GetHead(subjectListName)) ,assignementList);
            assignementList = appendInstruction(tom_make_Assign(subjectListName,tom_make_GetTail(subjectListName)) ,assignementList);
            
            TomList succesList = concat(concat(declarationList,assignementList),subList);
            Expression cond = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            Instruction test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = appendInstruction(test,result);
            break matchBlock;
          }
         } }}matchlab_match7_pattern3: {  Option option = null;  TomList termTail = null; if(tom_is_fun_sym_Cons(tom_match7_1)) {  TomTerm tom_match7_1_1 = null;  TomList tom_match7_1_2 = null; tom_match7_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match7_1); tom_match7_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match7_1); if(tom_is_fun_sym_UnamedVariable(tom_match7_1_1)) {  Option tom_match7_1_1_1 = null;  TomType tom_match7_1_1_2 = null; tom_match7_1_1_1 = ( Option) tom_get_slot_UnamedVariable_option(tom_match7_1_1); tom_match7_1_1_2 = ( TomType) tom_get_slot_UnamedVariable_astType(tom_match7_1_1); option = ( Option) tom_match7_1_1_1; termTail = ( TomList) tom_match7_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), termTail): not yet implemented");
            System.exit(1);
            break matchBlock;
          }
         } }}matchlab_match7_pattern4: {  TomList termTail = null;  Option option = null;  TomTerm var = null;  TomType termType = null; if(tom_is_fun_sym_Cons(tom_match7_1)) {  TomTerm tom_match7_1_1 = null;  TomList tom_match7_1_2 = null; tom_match7_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match7_1); tom_match7_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match7_1); if(tom_is_fun_sym_VariableStar(tom_match7_1_1)) {  Option tom_match7_1_1_1 = null;  TomName tom_match7_1_1_2 = null;  TomType tom_match7_1_1_3 = null; tom_match7_1_1_1 = ( Option) tom_get_slot_VariableStar_option(tom_match7_1_1); tom_match7_1_1_2 = ( TomName) tom_get_slot_VariableStar_astName(tom_match7_1_1); tom_match7_1_1_3 = ( TomType) tom_get_slot_VariableStar_astType(tom_match7_1_1); var = ( TomTerm) tom_match7_1_1; option = ( Option) tom_match7_1_1_1; termType = ( TomType) tom_match7_1_1_3; termTail = ( TomList) tom_match7_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = subjectList;
               * ...
               */
            if(generateSemanticAction) {
              subList = appendInstruction(tom_make_Action(actionList) ,subList);
            }
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            Instruction assignement = tom_make_Assign(var,tom_make_TomTermToExpression(subjectListName)) ;
            result = concat(appendInstruction(assignement,result),subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TomList begin_i = subjectList;
               * TomList end_i   = subjectList;
               * do {
               *   // SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(begin_i,end_i);
               *   ...
               *   if(!IS_EMPTY_TomList(end_i) )
               *     end_i = (TomList) GET_TAIL_TomList(end_i);
               *   subjectList = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */
            TomList pathBegin = append(tom_make_Begin(makeNumber(indexTerm)) ,oldPath);
            TomList pathEnd = append(tom_make_End(makeNumber(indexTerm)) ,oldPath);
            TomTerm variableBeginAST = tom_make_Variable(option(),tom_make_PositionName(pathBegin),termType) ;
            TomTerm variableEndAST   = tom_make_Variable(option(),tom_make_PositionName(pathEnd),termType) ;
            TomList declarationList = empty();
            declarationList = append(tom_make_Declaration(variableBeginAST) ,declarationList);
            declarationList = append(tom_make_Declaration(variableEndAST) ,declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(tom_make_Assign(variableBeginAST,tom_make_TomTermToExpression(subjectListName)) ,assignementList);
            assignementList = appendInstruction(tom_make_Assign(variableEndAST,tom_make_TomTermToExpression(subjectListName)) ,assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            TomList doList = empty();
            doList = appendInstruction(tom_make_Assign(var,tom_make_GetSliceList(symbol.getAstName(),variableBeginAST,variableEndAST)) ,doList);
            
            doList = concat(doList,subList);

            Expression cond1 = tom_make_Not(tom_make_IsEmptyList(variableEndAST)) ;
            Instruction test1 = tom_make_IfThenElse(cond1,cons(tom_make_InstructionToTomTerm(tom_make_Assign(variableEndAST,tom_make_GetTail(variableEndAST))), empty()),empty()) ;
            doList = appendInstruction(test1,doList);
            doList = appendInstruction(tom_make_Assign(subjectListName,tom_make_TomTermToExpression(variableEndAST)) ,doList);
            
            Expression cond2 = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            Instruction doWhile = tom_make_DoWhile(doList,cond2) ;
            
            result = appendInstruction(doWhile,concat(concat(declarationList,result),assignementList));

            break matchBlock;

          }

         } }}matchlab_match7_pattern5: {

 
          System.out.println("GenListMatchingAutomata strange termList: " + termList);
          System.exit(1);
        } }
 
    } // end matchBlock
    return result;
  }

  TomList genArrayMatchingAutomata(TomSymbol symbol,
                                   TomList termList,
                                   TomList oldPath,
                                   TomList actionList,
                                   boolean generateSemanticAction,
                                   TomTerm subjectListName,
                                   TomTerm subjectListIndex,
                                   int indexTerm) {
    TomTerm term;
    TomList result = empty();
      //%variable
    
    if(termList.isEmpty()) {
      return result;
    }
    
    TomTerm variableListAST = null;
    TomTerm variableIndexAST = null;
    String szero = "0";
    Expression glZero = tom_make_TomTermToExpression(tom_make_TargetLanguageToTomTerm(tom_make_ITL(szero))) ;
    if(indexTerm > 1) {
      variableListAST = subjectListName;
      variableIndexAST = subjectListIndex;
    } else {
      TomList pathList = append(tom_make_ListNumber(makeNumber(indexTerm)) ,oldPath);
      TomList pathIndex = append(tom_make_IndexNumber(makeNumber(indexTerm)) ,oldPath);

      matchBlock: {
         {  TomTerm tom_match8_1 = null; tom_match8_1 = ( TomTerm) subjectListName;matchlab_match8_pattern1: {  TomType termType = null;  Option option = null; if(tom_is_fun_sym_Variable(tom_match8_1)) {  Option tom_match8_1_1 = null;  TomName tom_match8_1_2 = null;  TomType tom_match8_1_3 = null; tom_match8_1_1 = ( Option) tom_get_slot_Variable_option(tom_match8_1); tom_match8_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match8_1); tom_match8_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match8_1); option = ( Option) tom_match8_1_1; termType = ( TomType) tom_match8_1_3;
 
            variableListAST = tom_make_Variable(option(),tom_make_PositionName(pathList),termType) ;
              // TODO: other termType
            variableIndexAST = tom_make_Variable(option(),tom_make_PositionName(pathIndex),getIntType()) ;
            break matchBlock;
           }}matchlab_match8_pattern2: {
 
            System.out.println("GenArrayMatchingAutomata strange subjectListName: " + subjectListName);
            System.exit(1);
          } }
 
      }
      result = append(tom_make_Declaration(variableListAST) ,result);
      result = append(tom_make_Declaration(variableIndexAST) ,result);
      result = appendInstruction(tom_make_Assign(variableListAST,tom_make_TomTermToExpression(subjectListName)) ,result);
      result = appendInstruction(tom_make_Assign(variableIndexAST,glZero) ,result);

      subjectListName  = variableListAST;
      subjectListIndex = variableIndexAST;
    } 
    
    TomList subList = genArrayMatchingAutomata(symbol,
                                               termList.getTail(), oldPath, actionList,
                                               generateSemanticAction,
                                               variableListAST,variableIndexAST,indexTerm+1);
      //System.out.println("\ntermList = " + termList);
      //System.out.println("*** genArrayMatchingAutomata");
    matchBlock: {
       {  TomList tom_match9_1 = null; tom_match9_1 = ( TomList) termList;matchlab_match9_pattern1: {  TomType termType = null;  TomList termTail = null;  TomTerm var = null;  Option option = null; if(tom_is_fun_sym_Cons(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  TomList tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match9_1); tom_match9_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match9_1); if(tom_is_fun_sym_Variable(tom_match9_1_1)) {  Option tom_match9_1_1_1 = null;  TomName tom_match9_1_1_2 = null;  TomType tom_match9_1_1_3 = null; tom_match9_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match9_1_1); tom_match9_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match9_1_1); tom_match9_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match9_1_1); var = ( TomTerm) tom_match9_1_1; option = ( Option) tom_match9_1_1_1; termType = ( TomType) tom_match9_1_1_3; termTail = ( TomList) tom_match9_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * if(_match1_1_index_1 < GET_SIZE_L(_match1_1_list_1)) {
               *   TomTerm x_j = (TomTerm) GET_ELEMENT_L(_match1_1_list_1,_match1_1_index_1);
               *   _match1_1_index_1++;;
               *   if(_match1_1_index_1 = GET_SIZE_L(_match1_1_list_1)) {
               *     ...
               *   }
               * }
               */
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            assignementList = appendInstruction(tom_make_Assign(var,tom_make_GetElement(subjectListName,subjectListIndex)) ,assignementList);
            assignementList = appendInstruction(tom_make_Increment(subjectListIndex) ,assignementList);
            
            if(generateSemanticAction) {
              subList = appendInstruction(tom_make_Action(actionList) ,subList);
            }
            
            Expression cond = tom_make_IsEmptyArray(subjectListName,subjectListIndex) ;
            Instruction test = tom_make_IfThenElse(cond,subList,empty()) ;
            TomList succesList = appendInstruction(test,concat(declarationList,assignementList));
            
            cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = appendInstruction(test,result);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * if(!IS_EMPTY_TomList(subjectList,subjectIndex)) {
               *   TomTerm x_j = (TomTerm) GET_ELEMENT_TomList(subjectList,subjectIndex);
               *   subjectIndex++;
               *   ...
               * }
               */
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            TomList declarationList = empty();
            TomList assignementList = empty();
            TomList succesList      = empty();
            
            assignementList = appendInstruction(tom_make_Assign(var,tom_make_GetElement(subjectListName,subjectListIndex)) ,assignementList);
            assignementList = appendInstruction(tom_make_Increment(subjectListIndex) ,assignementList);
            
            succesList = concat(concat(concat(succesList,declarationList),assignementList),subList);
            Expression cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            Instruction test = tom_make_IfThenElse(cond,succesList,empty()) ;
            
            result = appendInstruction(test,result);
            break matchBlock;
          }
         } }}matchlab_match9_pattern2: {  Option option = null;  TomList termTail = null; if(tom_is_fun_sym_Cons(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  TomList tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match9_1); tom_match9_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match9_1); if(tom_is_fun_sym_UnamedVariable(tom_match9_1_1)) {  Option tom_match9_1_1_1 = null;  TomType tom_match9_1_1_2 = null; tom_match9_1_1_1 = ( Option) tom_get_slot_UnamedVariable_option(tom_match9_1_1); tom_match9_1_1_2 = ( TomType) tom_get_slot_UnamedVariable_astType(tom_match9_1_1); option = ( Option) tom_match9_1_1_1; termTail = ( TomList) tom_match9_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * TODO
               */
            System.out.println("cons(UnamedVariable(option,_), empty): not yet implemented");
            System.exit(1);
            break matchBlock;
          }
         } }}matchlab_match9_pattern3: {  Option option = null;  TomList termTail = null;  TomTerm var = null;  TomType termType = null; if(tom_is_fun_sym_Cons(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  TomList tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Cons_head(tom_match9_1); tom_match9_1_2 = ( TomList) tom_get_slot_Cons_tail(tom_match9_1); if(tom_is_fun_sym_VariableStar(tom_match9_1_1)) {  Option tom_match9_1_1_1 = null;  TomName tom_match9_1_1_2 = null;  TomType tom_match9_1_1_3 = null; tom_match9_1_1_1 = ( Option) tom_get_slot_VariableStar_option(tom_match9_1_1); tom_match9_1_1_2 = ( TomName) tom_get_slot_VariableStar_astName(tom_match9_1_1); tom_match9_1_1_3 = ( TomType) tom_get_slot_VariableStar_astType(tom_match9_1_1); var = ( TomTerm) tom_match9_1_1; option = ( Option) tom_match9_1_1_1; termType = ( TomType) tom_match9_1_1_3; termTail = ( TomList) tom_match9_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = GET_SLICE_L(subjectList,subjectIndex,GET_SIZE_L(subjectList));
               * ...
               */
            if(generateSemanticAction) {
              subList = appendInstruction(tom_make_Action(actionList) ,subList);
            }
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            Instruction assignement = tom_make_Assign(var,tom_make_GetSliceArray(symbol.getAstName(),subjectListName,subjectListIndex,tom_make_ExpressionToTomTerm(tom_make_GetSize(subjectListName))))



 ;
            result = concat(appendInstruction(assignement,result),subList);
            break matchBlock;
          } else {
              /*
               * generate:
               * ---------
               * int begin_i = subjectIndex;
               * int end_i   = subjectIndex;
               * do {
               *   // SUBSTITUTION: E_i
               *   TomList E_i = GET_SLICE_TomList(subjectList,begin_i,end_i);
               *   ...
               *   end_i++;
               *   subjectIndex = end_i;
               * } while( !IS_EMPTY_TomList(subjectList) )
               */

            TomList pathBegin = append(tom_make_Begin(makeNumber(indexTerm)) ,oldPath);
            TomList pathEnd = append(tom_make_End(makeNumber(indexTerm)) ,oldPath);
              // TODO: termType
            TomTerm variableBeginAST = tom_make_Variable(option(),tom_make_PositionName(pathBegin),getIntType()) ;
            TomTerm variableEndAST   = tom_make_Variable(option(),tom_make_PositionName(pathEnd),getIntType()) ;
            TomList declarationList = empty();
            declarationList = append(tom_make_Declaration(variableBeginAST) ,declarationList);
            declarationList = append(tom_make_Declaration(variableEndAST) ,declarationList);
            TomList assignementList = empty();
            assignementList = appendInstruction(tom_make_Assign(variableBeginAST,tom_make_TomTermToExpression(subjectListIndex)) ,assignementList);
            assignementList = appendInstruction(tom_make_Assign(variableEndAST,tom_make_TomTermToExpression(subjectListIndex)) ,assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            TomList doList = empty();
            doList = appendInstruction(tom_make_Assign(var,tom_make_GetSliceArray(symbol.getAstName(),subjectListName,variableBeginAST,variableEndAST))

 ,doList);
            doList = concat(doList,subList);
            doList = appendInstruction(tom_make_Increment(variableEndAST) ,doList);
            doList = appendInstruction(tom_make_Assign(subjectListIndex,tom_make_TomTermToExpression(variableEndAST)) ,doList); 
            
            Expression cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            Instruction doWhile = tom_make_DoWhile(doList,cond) ;
            
            TomList tmpResult = empty();
            if(Flags.supportedBlock) {
              tmpResult = appendInstruction(tom_make_OpenBlock() ,tmpResult);
            }
            tmpResult = concat(tmpResult,declarationList);
            tmpResult = concat(tmpResult,result);
            tmpResult = concat(tmpResult,assignementList);
            tmpResult = appendInstruction(doWhile,tmpResult);
            if(Flags.supportedBlock) {
              tmpResult = appendInstruction(tom_make_CloseBlock() ,tmpResult);
            }
            result = tmpResult;
            break matchBlock;
          }
         } }}matchlab_match9_pattern4: {

 
          System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
          System.exit(1);
        } }
 
    } // end matchBlock
    return result;
  }


     /* 
     * postProcessing: passCompiledTermTransformation
     *
     * transform a compiledTerm
     * 2 phases:
     *   - collection of Declaration
     *   - replace LocalVariable and remove Declaration
     */

  public TomTerm postProcessing(TomTerm subject) {
    TomTerm res;
    ArrayList list = new ArrayList();
    traversalCollectDeclaration(list,subject);
      //System.out.println("list size = " + list.size());
    res = traversalReplaceLocalVariable(list,subject);
    return res;
  }
    
  private TomTerm traversalCollectDeclaration(ArrayList list, TomTerm subject) {
      //%variable
     {  TomTerm tom_match10_1 = null; tom_match10_1 = ( TomTerm) subject;matchlab_match10_pattern1: {  TomList l = null; if(tom_is_fun_sym_Tom(tom_match10_1)) {  TomList tom_match10_1_1 = null; tom_match10_1_1 = ( TomList) tom_get_slot_Tom_list(tom_match10_1); l = ( TomList) tom_match10_1_1;
 
        return tom_make_Tom(traversalCollectDeclarationList(list, l)) ;
       }}matchlab_match10_pattern2: { if(tom_is_fun_sym_LocalVariable(tom_match10_1)) {

 
          //System.out.println("Detect LocalVariable");
        
        Collection c = new HashSet();
        list.add(c);
        collectDeclaration(c,subject);
        return null;
       }}matchlab_match10_pattern3: {  TomTerm t = null; t = ( TomTerm) tom_match10_1;

 
          //System.out.println("default: " + t);
        if(!list.isEmpty()) {
          Collection c = (Collection) list.get(list.size()-1);
          collectDeclaration(c,subject);
        }
        return t;
      } }
 
  }

  public void collectDeclaration(final Collection collection, TomTerm subject) {
    Collect1 collect = new Collect1() { 
        public boolean apply(ATerm t) {
            //%variable
           {  TomTerm tom_match11_1 = null; tom_match11_1 = ( TomTerm) t;matchlab_match11_pattern1: { if(tom_is_fun_sym_Declaration(tom_match11_1)) {  TomTerm tom_match11_1_1 = null; tom_match11_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match11_1);
 
              collection.add(t);
              return false;
             }}matchlab_match11_pattern2: {
  return true; } }
 
        } 
      }; // end new
    
    traversal().genericCollect(subject, collect); 
  } 

  private boolean removeDeclaration = false;
  private TomTerm traversalReplaceLocalVariable(ArrayList list, TomTerm subject) {
      //%variable
     {  TomTerm tom_match12_1 = null; tom_match12_1 = ( TomTerm) subject;matchlab_match12_pattern1: {  TomList l = null; if(tom_is_fun_sym_Tom(tom_match12_1)) {  TomList tom_match12_1_1 = null; tom_match12_1_1 = ( TomList) tom_get_slot_Tom_list(tom_match12_1); l = ( TomList) tom_match12_1_1;
 
        return tom_make_Tom(traversalReplaceLocalVariableList(list, l)) ;
       }}matchlab_match12_pattern2: { if(tom_is_fun_sym_LocalVariable(tom_match12_1)) {

 
          //System.out.println("Replace LocalVariable");

        Map map = (Map)list.get(0);
        list.remove(0);

        Collection c = map.values();
        Iterator it = c.iterator();
        TomList declarationList = empty();
        while(it.hasNext()) {
          declarationList = cons((TomTerm)it.next(),declarationList);
        }

          //System.out.println("declarationList = " + declarationList);
        removeDeclaration = true;
        return tom_make_Tom(declarationList) ;
       }}matchlab_match12_pattern3: {  TomTerm t = null; t = ( TomTerm) tom_match12_1;






 
        TomTerm res = t;
          //res = removeDeclaration(t);
        
        if(removeDeclaration) {
          res = removeDeclaration(t);
        }
        
          //System.out.println("\ndefault:\nt   = " + t + "\nres = " + res);
        return res;
      } }
 
  }

    private TomList traversalCollectDeclarationList(ArrayList list,TomList subject) {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalCollectDeclaration(list,t),
                traversalCollectDeclarationList(list,l));
  }

  private TomList traversalReplaceLocalVariableList(ArrayList list,TomList subject) {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalReplaceLocalVariable(list,t),
                traversalReplaceLocalVariableList(list,l));
  }
  
  public TomTerm removeDeclaration(TomTerm subject) {
    TomTerm res = subject;
      //System.out.println("*** removeDeclaration");
                  
    Replace1 replace = new Replace1() { 
        public ATerm apply(ATerm t) {
            //%variable
           {  TomTerm tom_match13_1 = null; tom_match13_1 = ( TomTerm) t;matchlab_match13_pattern1: { if(tom_is_fun_sym_Declaration(tom_match13_1)) {  TomTerm tom_match13_1_1 = null; tom_match13_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match13_1);
 
                //System.out.println("Remove Declaration");
              return tom_make_Tom(empty()) ;
             }}matchlab_match13_pattern2: {  TomTerm other = null; other = ( TomTerm) tom_match13_1;

 
              System.out.println("removeDeclaration this = " + this);
                //return other;
              return (TomTerm) traversal().genericTraversal(other,this);
            } }
 
        } 
      }; // end new
    
      //return genericReplace(subject, replace);
    try {
      res = (TomTerm) replace.apply(subject);
    } catch(Exception e) {
      System.out.println("removeDeclaration: error");
      System.exit(0);
    }
    return res;
  }
  
} // end of class
  
                  
    
