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
  
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import jtom.TomBase;
import jtom.adt.Expression;
import jtom.adt.Instruction;
import jtom.adt.Option;
import jtom.adt.OptionList;
import jtom.adt.TargetLanguage;
import jtom.adt.TomList;
import jtom.adt.TomName;
import jtom.adt.TomNumberList;
import jtom.adt.TomRule;
import jtom.adt.TomRuleList;
import jtom.adt.TomSymbol;
import jtom.adt.TomTerm;
import jtom.adt.TomType;
import jtom.adt.TomTypeList;
import jtom.runtime.Replace1;
import jtom.tools.Flags;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import aterm.ATerm;

public class TomCompiler extends TomBase implements TomTask {
  private TomTask nextTask;
  TomKernelCompiler tomKernelCompiler;
  private String debugKey = null;
  public TomCompiler(jtom.TomEnvironment environment,
                     TomKernelCompiler tomKernelCompiler) {
    super(environment);
    this.tomKernelCompiler = tomKernelCompiler;
  }

// ------------------------------------------------------------
    
// ------------------------------------------------------------

  public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  public void process(TomTaskInput input) {
  	try {
  	  System.out.println("Processing TomCompiler Task");
  	  TomTerm preCompiledTerm = preProcessing(input.getTerm());
  	  TomTerm compiledTerm = tomKernelCompiler.compileMatching(preCompiledTerm);
      compiledTerm = tomKernelCompiler.postProcessing(compiledTerm);
      input.setTerm(compiledTerm);
    } catch (Exception e) {
    }
    if(nextTask != null) {
      nextTask.process(input);
    }
  }
  
  public TomTask getTask() {
  	return nextTask;
  }
    /* 
     * preProcessing:
     *
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

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

     {  TomTerm tom_match1_1 = null; tom_match1_1 = ( TomTerm) subject;matchlab_match1_pattern1: {  TomList l = null; if(tom_is_fun_sym_Tom(tom_match1_1)) {  TomList tom_match1_1_1 = null; tom_match1_1_1 = ( TomList) tom_get_slot_Tom_tomList(tom_match1_1); l = ( TomList) tom_match1_1_1;
 
        return tom_make_Tom(tomListMap(l, replace_preProcessing)) ;
       }}matchlab_match1_pattern2: {  TomRuleList tail = null;  Option orgTrack = null;  TomRuleList ruleList = null;  String tomName = null; if(tom_is_fun_sym_RuleSet(tom_match1_1)) {  TomRuleList tom_match1_1_1 = null;  Option tom_match1_1_2 = null; tom_match1_1_1 = ( TomRuleList) tom_get_slot_RuleSet_ruleList(tom_match1_1); tom_match1_1_2 = ( Option) tom_get_slot_RuleSet_orgTrack(tom_match1_1); if(tom_is_fun_sym_manyTomRuleList(tom_match1_1_1)) {  TomRule tom_match1_1_1_1 = null;  TomRuleList tom_match1_1_1_2 = null; tom_match1_1_1_1 = ( TomRule) tom_get_slot_manyTomRuleList_head(tom_match1_1_1); tom_match1_1_1_2 = ( TomRuleList) tom_get_slot_manyTomRuleList_tail(tom_match1_1_1); ruleList = ( TomRuleList) tom_match1_1_1; if(tom_is_fun_sym_RewriteRule(tom_match1_1_1_1)) {  TomTerm tom_match1_1_1_1_1 = null;  TomTerm tom_match1_1_1_1_2 = null;  TomList tom_match1_1_1_1_3 = null;  Option tom_match1_1_1_1_4 = null; tom_match1_1_1_1_1 = ( TomTerm) tom_get_slot_RewriteRule_lhs(tom_match1_1_1_1); tom_match1_1_1_1_2 = ( TomTerm) tom_get_slot_RewriteRule_rhs(tom_match1_1_1_1); tom_match1_1_1_1_3 = ( TomList) tom_get_slot_RewriteRule_condList(tom_match1_1_1_1); tom_match1_1_1_1_4 = ( Option) tom_get_slot_RewriteRule_option(tom_match1_1_1_1); if(tom_is_fun_sym_Term(tom_match1_1_1_1_1)) {  TomTerm tom_match1_1_1_1_1_1 = null; tom_match1_1_1_1_1_1 = ( TomTerm) tom_get_slot_Term_tomTerm(tom_match1_1_1_1_1); if(tom_is_fun_sym_Appl(tom_match1_1_1_1_1_1)) {  Option tom_match1_1_1_1_1_1_1 = null;  TomName tom_match1_1_1_1_1_1_2 = null;  TomList tom_match1_1_1_1_1_1_3 = null; tom_match1_1_1_1_1_1_1 = ( Option) tom_get_slot_Appl_option(tom_match1_1_1_1_1_1); tom_match1_1_1_1_1_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match1_1_1_1_1_1); tom_match1_1_1_1_1_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match1_1_1_1_1_1); if(tom_is_fun_sym_Name(tom_match1_1_1_1_1_1_2)) {  String tom_match1_1_1_1_1_1_2_1 = null; tom_match1_1_1_1_1_1_2_1 = ( String) tom_get_slot_Name_string(tom_match1_1_1_1_1_1_2); tomName = ( String) tom_match1_1_1_1_1_1_2_1; tail = ( TomRuleList) tom_match1_1_1_2; orgTrack = ( Option) tom_match1_1_2;


 
        statistics().numberRuleSetsTransformed++;
        if(Flags.debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
        }
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName name = tomSymbol.getAstName();
        TomTypeList typesList = tomSymbol.getTypesToType().getDomain();        
        TomNumberList path = tsf().makeTomNumberList();
        TomList matchArgumentsList = empty();
        TomList patternActionList  = empty();
        TomTerm variable;
        int index = 0;

        path = (TomNumberList) path.append(tom_make_RuleVar() );
        
        while(!typesList.isEmpty()) {
          TomType subtermType = typesList.getHead();
          variable = tom_make_Variable(option(),tom_make_PositionName(appendNumber(index, path)),subtermType) ;
          matchArgumentsList = append(variable,matchArgumentsList);
          typesList = typesList.getTail();
          index++;
        }
        
        while(!ruleList.isEmpty()) {
          TomRule rule = ruleList.getHead();
           {  TomRule tom_match2_1 = null; tom_match2_1 = ( TomRule) rule;matchlab_match2_pattern1: {  TomList matchPatternsList = null;  TomList condList = null;  TomTerm rhsTerm = null;  Option option = null; if(tom_is_fun_sym_RewriteRule(tom_match2_1)) {  TomTerm tom_match2_1_1 = null;  TomTerm tom_match2_1_2 = null;  TomList tom_match2_1_3 = null;  Option tom_match2_1_4 = null; tom_match2_1_1 = ( TomTerm) tom_get_slot_RewriteRule_lhs(tom_match2_1); tom_match2_1_2 = ( TomTerm) tom_get_slot_RewriteRule_rhs(tom_match2_1); tom_match2_1_3 = ( TomList) tom_get_slot_RewriteRule_condList(tom_match2_1); tom_match2_1_4 = ( Option) tom_get_slot_RewriteRule_option(tom_match2_1); if(tom_is_fun_sym_Term(tom_match2_1_1)) {  TomTerm tom_match2_1_1_1 = null; tom_match2_1_1_1 = ( TomTerm) tom_get_slot_Term_tomTerm(tom_match2_1_1); if(tom_is_fun_sym_Appl(tom_match2_1_1_1)) {  Option tom_match2_1_1_1_1 = null;  TomName tom_match2_1_1_1_2 = null;  TomList tom_match2_1_1_1_3 = null; tom_match2_1_1_1_1 = ( Option) tom_get_slot_Appl_option(tom_match2_1_1_1); tom_match2_1_1_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match2_1_1_1); tom_match2_1_1_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match2_1_1_1); matchPatternsList = ( TomList) tom_match2_1_1_1_3; if(tom_is_fun_sym_Term(tom_match2_1_2)) {  TomTerm tom_match2_1_2_1 = null; tom_match2_1_2_1 = ( TomTerm) tom_get_slot_Term_tomTerm(tom_match2_1_2); rhsTerm = ( TomTerm) tom_match2_1_2_1; condList = ( TomList) tom_match2_1_3; option = ( Option) tom_match2_1_4;



 
              
              TomTerm newRhs = preProcessing(tom_make_MakeTerm(rhsTerm) );
              TomList rhsList = empty();
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(tom_make_OpenBlock() ,rhsList);
              }
              if(Flags.debugMode) {
                TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.patternSuccess(\""+debugKey+"\");\n");
                rhsList = append(tom_make_TargetLanguageToTomTerm(tl) , rhsList);
              }
              
              rhsList = appendInstruction(tom_make_Return(newRhs) ,rhsList);
              if(Flags.supportedBlock) {
                rhsList = appendInstruction(tom_make_CloseBlock() ,rhsList);
              }
              
              TomList newRhsList = buildCondition(condList,rhsList);
             
              patternActionList = append(tom_make_PatternAction(tom_make_TermList(matchPatternsList),tom_make_Tom(newRhsList),option) ,patternActionList);
             } } } }} }
  
          ruleList = ruleList.getTail();
        }
        
        TomTerm subjectListAST = tom_make_SubjectList(matchArgumentsList) ;
        TomTerm makeFunctionBeginAST = tom_make_MakeFunctionBegin(name,subjectListAST) ;
        ArrayList optionList = new ArrayList();
        optionList.add(orgTrack);
          //optionList.add(tsf().makeOption_GeneratedMatch());
        Option generatedOptions = ast().makeOption(ast().makeOptionList(optionList));
        TomTerm matchAST = tom_make_Match(tom_make_SubjectList(matchArgumentsList),tom_make_PatternList(patternActionList),generatedOptions)

 ;
        Instruction buildAST = tom_make_Return(tom_make_BuildTerm(name,tomListMap(matchArgumentsList, replace_preProcessing_makeTerm))) ;
        TomList l = empty();
        if(Flags.eCode) {
          l = append(makeFunctionBeginAST,l);
          l = append(tom_make_LocalVariable() ,l);
          l = append(tom_make_EndLocalVariable() ,l);
          l = append(matchAST,l);
          l = appendInstruction(buildAST,l);
          l = append(tom_make_MakeFunctionEnd() ,l);
        } else {
          l = append(makeFunctionBeginAST,l);
          l = append(matchAST,l);
          l = appendInstruction(buildAST,l);
          l = append(tom_make_MakeFunctionEnd() ,l);
        }
        return preProcessing(tom_make_Tom(l) );
       } } } } } }}matchlab_match1_pattern3: {  TomTerm tl = null;  TomTerm tom = null;  Option option = null; if(tom_is_fun_sym_PatternAction(tom_match1_1)) {  TomTerm tom_match1_1_1 = null;  TomTerm tom_match1_1_2 = null;  Option tom_match1_1_3 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_PatternAction_termList(tom_match1_1); tom_match1_1_2 = ( TomTerm) tom_get_slot_PatternAction_tom(tom_match1_1); tom_match1_1_3 = ( Option) tom_get_slot_PatternAction_option(tom_match1_1); tl = ( TomTerm) tom_match1_1_1; tom = ( TomTerm) tom_match1_1_2; option = ( Option) tom_match1_1_3;

 
        return tom_make_PatternAction(tl,preProcessing(tom),option) ;
       }}matchlab_match1_pattern4: {  TomTerm tom = null;  Option option = null;  TomTerm termList = null; if(tom_is_fun_sym_DefaultPatternAction(tom_match1_1)) {  TomTerm tom_match1_1_1 = null;  TomTerm tom_match1_1_2 = null;  Option tom_match1_1_3 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_DefaultPatternAction_termList(tom_match1_1); tom_match1_1_2 = ( TomTerm) tom_get_slot_DefaultPatternAction_tom(tom_match1_1); tom_match1_1_3 = ( Option) tom_get_slot_DefaultPatternAction_option(tom_match1_1); termList = ( TomTerm) tom_match1_1_1; tom = ( TomTerm) tom_match1_1_2; option = ( Option) tom_match1_1_3;

 
        return tom_make_DefaultPatternAction(termList,preProcessing(tom),option) ;
       }}matchlab_match1_pattern5: {  TomList l1 = null;  TomList l2 = null;  Option matchOption = null;  OptionList list = null; if(tom_is_fun_sym_Match(tom_match1_1)) {  TomTerm tom_match1_1_1 = null;  TomTerm tom_match1_1_2 = null;  Option tom_match1_1_3 = null; tom_match1_1_1 = ( TomTerm) tom_get_slot_Match_subjectList(tom_match1_1); tom_match1_1_2 = ( TomTerm) tom_get_slot_Match_patternList(tom_match1_1); tom_match1_1_3 = ( Option) tom_get_slot_Match_option(tom_match1_1); if(tom_is_fun_sym_SubjectList(tom_match1_1_1)) {  TomList tom_match1_1_1_1 = null; tom_match1_1_1_1 = ( TomList) tom_get_slot_SubjectList_tomList(tom_match1_1_1); l1 = ( TomList) tom_match1_1_1_1; if(tom_is_fun_sym_PatternList(tom_match1_1_2)) {  TomList tom_match1_1_2_1 = null; tom_match1_1_2_1 = ( TomList) tom_get_slot_PatternList_tomList(tom_match1_1_2); l2 = ( TomList) tom_match1_1_2_1; if(tom_is_fun_sym_Option(tom_match1_1_3)) {  OptionList tom_match1_1_3_1 = null; tom_match1_1_3_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match1_1_3); matchOption = ( Option) tom_match1_1_3; list = ( OptionList) tom_match1_1_3_1;

 
        Option orgTrack = findOriginTracking(list);
        if(Flags.debugMode) {
          debugKey = orgTrack.getFileName().getString() + orgTrack.getLine();
        }
        TomList newPatternList = empty();
        while(!l2.isEmpty()) {
          TomTerm elt = preProcessing(l2.getHead());
          TomTerm newPatternAction = elt;
          
          matchBlock: {
             {  TomTerm tom_match3_1 = null; tom_match3_1 = ( TomTerm) elt;matchlab_match3_pattern1: {  TomList actionList = null;  Option option = null;  TomList termList = null; if(tom_is_fun_sym_PatternAction(tom_match3_1)) {  TomTerm tom_match3_1_1 = null;  TomTerm tom_match3_1_2 = null;  Option tom_match3_1_3 = null; tom_match3_1_1 = ( TomTerm) tom_get_slot_PatternAction_termList(tom_match3_1); tom_match3_1_2 = ( TomTerm) tom_get_slot_PatternAction_tom(tom_match3_1); tom_match3_1_3 = ( Option) tom_get_slot_PatternAction_option(tom_match3_1); if(tom_is_fun_sym_TermList(tom_match3_1_1)) {  TomList tom_match3_1_1_1 = null; tom_match3_1_1_1 = ( TomList) tom_get_slot_TermList_tomList(tom_match3_1_1); termList = ( TomList) tom_match3_1_1_1; if(tom_is_fun_sym_Tom(tom_match3_1_2)) {  TomList tom_match3_1_2_1 = null; tom_match3_1_2_1 = ( TomList) tom_get_slot_Tom_tomList(tom_match3_1_2); actionList = ( TomList) tom_match3_1_2_1; option = ( Option) tom_match3_1_3;

 
                TomList newTermList = empty();
                TomList newActionList = actionList;

                  /* generate equality checks */
                ArrayList equalityCheck = new ArrayList();
                TomList renamedTermList = linearizePattern(termList,equalityCheck);
                if(equalityCheck.size() > 0) {
                  Expression cond = tom_make_TrueTL() ;
                  Iterator it = equalityCheck.iterator();
                  while(it.hasNext()) {
                    Expression equality = (Expression)it.next();
                    cond = tom_make_And(equality,cond) ;
                  }
                  TomList elsePart = empty();
                  if(Flags.debugMode) {
                    TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.linearizationFail(\""+debugKey+"\");\n");
                    elsePart   = cons(tom_make_InstructionToTomTerm(tom_make_Action(cons(tom_make_TargetLanguageToTomTerm(tl), empty()))), empty()) ;
                  }
                    
                  newActionList = cons(tom_make_InstructionToTomTerm(tom_make_IfThenElse(cond,actionList,elsePart)) ,empty());
                  newPatternAction = tom_make_PatternAction(tom_make_TermList(renamedTermList),tom_make_Tom(newActionList),option) ;        
                    /*System.out.println("\nnewPatternAction = " + newPatternAction);*/
                }

                  /* abstract patterns */
                ArrayList abstractedPattern  = new ArrayList();
                ArrayList introducedVariable = new ArrayList();
                newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);
                if(abstractedPattern.size() > 0) {
                    /* generate a new match construct */

                  TomTerm generatedPatternAction =
                    tom_make_PatternAction(tom_make_TermList(ast().makeList(abstractedPattern)),tom_make_Tom(newActionList),option) ;        
                    /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/
                  ArrayList optionList = new ArrayList();
                  optionList.add(orgTrack);
                  optionList.add(tsf().makeOption_GeneratedMatch());
                  Option generatedOptions = ast().makeOption(ast().makeOptionList(optionList));
                  TomTerm generatedMatch =
                    tom_make_Match(tom_make_SubjectList(ast().makeList(introducedVariable)),tom_make_PatternList(cons(generatedPatternAction, empty())),generatedOptions)

 ;
                    /*System.out.println("Generate new Match"+generatedMatch); */
                  generatedMatch = preProcessing(generatedMatch);
                  newPatternAction =
                    tom_make_PatternAction(tom_make_TermList(newTermList),tom_make_Tom(cons(generatedMatch, empty())),option()) ;

                    /*System.out.println("newPatternAction = " + newPatternAction); */
                }
                  /* do nothing */
                break matchBlock;
               } } }}matchlab_match3_pattern2: {  Option option = null;  TomList actionList = null;  TomList termList = null; if(tom_is_fun_sym_DefaultPatternAction(tom_match3_1)) {  TomTerm tom_match3_1_1 = null;  TomTerm tom_match3_1_2 = null;  Option tom_match3_1_3 = null; tom_match3_1_1 = ( TomTerm) tom_get_slot_DefaultPatternAction_termList(tom_match3_1); tom_match3_1_2 = ( TomTerm) tom_get_slot_DefaultPatternAction_tom(tom_match3_1); tom_match3_1_3 = ( Option) tom_get_slot_DefaultPatternAction_option(tom_match3_1); if(tom_is_fun_sym_TermList(tom_match3_1_1)) {  TomList tom_match3_1_1_1 = null; tom_match3_1_1_1 = ( TomList) tom_get_slot_TermList_tomList(tom_match3_1_1); termList = ( TomList) tom_match3_1_1_1; if(tom_is_fun_sym_Tom(tom_match3_1_2)) {  TomList tom_match3_1_2_1 = null; tom_match3_1_2_1 = ( TomList) tom_get_slot_Tom_tomList(tom_match3_1_2); actionList = ( TomList) tom_match3_1_2_1; option = ( Option) tom_match3_1_3;                  TomList newTermList = empty();                 TomList newActionList = actionList;                    /* generate equality checks */                 ArrayList equalityCheck = new ArrayList();                 TomList renamedTermList = linearizePattern(termList,equalityCheck);                 if(equalityCheck.size() > 0) {                   Expression cond = tom_make_TrueTL() ;                   Iterator it = equalityCheck.iterator();                   while(it.hasNext()) {                     Expression equality = (Expression)it.next();                     cond = tom_make_And(equality,cond) ;                   }                   TomList elsePart = empty();                   if(Flags.debugMode) {                     TargetLanguage tl = tsf().makeTargetLanguage_ITL("jtom.debug.TomDebugger.debugger.linearizationFail(\""+debugKey+"\");\n");                     elsePart   = cons(tom_make_InstructionToTomTerm(tom_make_Action(cons(tom_make_TargetLanguageToTomTerm(tl), empty()))), empty()) ;                   }                                        newActionList = cons(tom_make_InstructionToTomTerm(tom_make_IfThenElse(cond,actionList,elsePart)) ,empty());                   newPatternAction = tom_make_PatternAction(tom_make_TermList(renamedTermList),tom_make_Tom(newActionList),option) ;                             /*System.out.println("\nnewPatternAction = " + newPatternAction);*/                 }                    /* abstract patterns */                 ArrayList abstractedPattern  = new ArrayList();                 ArrayList introducedVariable = new ArrayList();                 newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);                 if(abstractedPattern.size() > 0) {                     /* generate a new match construct */                    TomTerm generatedPatternAction =                     tom_make_PatternAction(tom_make_TermList(ast().makeList(abstractedPattern)),tom_make_Tom(newActionList),option) ;                             /* We reconstruct only a list of option with orgTrack and GeneratedMatch*/                   ArrayList optionList = new ArrayList();                   optionList.add(orgTrack);                   optionList.add(tsf().makeOption_GeneratedMatch());                   Option generatedOptions = ast().makeOption(ast().makeOptionList(optionList));                   TomTerm generatedMatch =                     tom_make_Match(tom_make_SubjectList(ast().makeList(introducedVariable)),tom_make_PatternList(cons(generatedPatternAction, empty())),generatedOptions) ;                     /*System.out.println("Generate new Match"+generatedMatch); */                   generatedMatch = preProcessing(generatedMatch);                   newPatternAction =                     tom_make_PatternAction(tom_make_TermList(newTermList),tom_make_Tom(cons(generatedMatch, empty())),option()) ;                      /*System.out.println("newPatternAction = " + newPatternAction); */                 }                   /* do nothing */                 break matchBlock;                } } }}matchlab_match3_pattern3: {

 
                System.out.println("preProcessingeuuu: strange PatternAction: " + elt);
                  //System.out.println("termList = " + elt.getTermList());
                  //System.out.println("tom      = " + elt.getTom()); 
                System.exit(1);
              } }
 
          } // end matchBlock

          newPatternList = append(newPatternAction,newPatternList);
          l2 = l2.getTail();
        }

        TomTerm newMatch = tom_make_Match(tom_make_SubjectList(l1),tom_make_PatternList(newPatternList),matchOption)

 ;
        return newMatch;
       } } } }}matchlab_match1_pattern6: {  TomTerm t = null; t = ( TomTerm) tom_match1_1;


 
        return tomKernelCompiler.preProcessing(t);
      } }
 
  }
  
  private TomList buildCondition(TomList condList, TomList actionList) {
     {  TomList tom_match4_1 = null; tom_match4_1 = ( TomList) condList;matchlab_match4_pattern1: { if(tom_is_fun_sym_emptyTomList(tom_match4_1)) {
  return actionList;  }}matchlab_match4_pattern2: {  TomTerm subject = null;  TomTerm pattern = null;  TomList tail = null; if(tom_is_fun_sym_manyTomList(tom_match4_1)) {  TomTerm tom_match4_1_1 = null;  TomList tom_match4_1_2 = null; tom_match4_1_1 = ( TomTerm) tom_get_slot_manyTomList_head(tom_match4_1); tom_match4_1_2 = ( TomList) tom_get_slot_manyTomList_tail(tom_match4_1); if(tom_is_fun_sym_MatchingCondition(tom_match4_1_1)) {  TomTerm tom_match4_1_1_1 = null;  TomTerm tom_match4_1_1_2 = null; tom_match4_1_1_1 = ( TomTerm) tom_get_slot_MatchingCondition_lhs(tom_match4_1_1); tom_match4_1_1_2 = ( TomTerm) tom_get_slot_MatchingCondition_rhs(tom_match4_1_1); pattern = ( TomTerm) tom_match4_1_1_1; subject = ( TomTerm) tom_match4_1_1_2; tail = ( TomList) tom_match4_1_2;

 
        System.out.println(pattern);
        TomType subjectType = getTermType(pattern);
        TomNumberList path = tsf().makeTomNumberList();
        path = (TomNumberList) path.append(tom_make_RuleVar() );
        TomTerm newSubject = preProcessing(tom_make_MakeTerm(subject) );
    
          //TomTerm introducedVariable = `Variable(option(),PositionName(path),subjectType);
        TomTerm introducedVariable = newSubject;
        
          // introducedVariable = subject
          // Declare and Assign 

        TomList newActionList = buildCondition(tail,actionList);

        TomTerm generatedPatternAction =
          tom_make_PatternAction(tom_make_TermList(cons(pattern, empty())),tom_make_Tom(newActionList),option()) ;        

          // Warning: The options are not good
        TomTerm generatedMatch =
          tom_make_Match(tom_make_SubjectList(cons(introducedVariable, empty())),tom_make_PatternList(cons(generatedPatternAction, empty())),option())

 ;
    
    
          //System.out.println("buildCondition: generatedMatch =\n\t" + generatedMatch);
        TomList conditionList = cons(generatedMatch,empty());
    
        return conditionList;

       } }}matchlab_match4_pattern3: {  TomTerm lhs = null;  TomTerm rhs = null;  TomList tail = null; if(tom_is_fun_sym_manyTomList(tom_match4_1)) {  TomTerm tom_match4_1_1 = null;  TomList tom_match4_1_2 = null; tom_match4_1_1 = ( TomTerm) tom_get_slot_manyTomList_head(tom_match4_1); tom_match4_1_2 = ( TomList) tom_get_slot_manyTomList_tail(tom_match4_1); if(tom_is_fun_sym_EqualityCondition(tom_match4_1_1)) {  TomTerm tom_match4_1_1_1 = null;  TomTerm tom_match4_1_1_2 = null; tom_match4_1_1_1 = ( TomTerm) tom_get_slot_EqualityCondition_lhs(tom_match4_1_1); tom_match4_1_1_2 = ( TomTerm) tom_get_slot_EqualityCondition_rhs(tom_match4_1_1); lhs = ( TomTerm) tom_match4_1_1_1; rhs = ( TomTerm) tom_match4_1_1_2; tail = ( TomList) tom_match4_1_2;

 
        TomTerm newLhs = preProcessing(tom_make_MakeTerm(lhs) );
        TomTerm newRhs = preProcessing(tom_make_MakeTerm(rhs) );

        Expression equality = tom_make_EqualTerm(newLhs,newRhs) ;
        TomList newActionList = buildCondition(tail,actionList);
        TomTerm generatedTest = tom_make_InstructionToTomTerm(tom_make_IfThenElse(equality,newActionList,empty())) ;
        TomList conditionList = cons(generatedTest,empty());
        return conditionList;
       } }}matchlab_match4_pattern4: {

 
        System.out.println("buildCondition strange term: " + condList);
        System.exit(1);
        return null;
      } }

 
  }
  
  private TomTerm renameVariable(TomTerm subject,
                                 HashMap multiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
     {  TomTerm tom_match5_1 = null; tom_match5_1 = ( TomTerm) subject;matchlab_match5_pattern1: {  TomName name = null;  TomType type = null; if(tom_is_fun_sym_Variable(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomType tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match5_1); name = ( TomName) tom_match5_1_2; type = ( TomType) tom_match5_1_3;
 
        Integer multiplicity = (Integer) multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(tom_make_RenamedVar(name) );
          path = (TomNumberList) path.append(makeNumber(mult));
          renamedTerm = tom_make_Variable(option(),tom_make_PositionName(path),type) ;

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = tom_make_EqualTerm(subject,renamedTerm) ;
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
       }}matchlab_match5_pattern2: {  TomName name = null;  TomType type = null; if(tom_is_fun_sym_VariableStar(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomType tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_VariableStar_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_VariableStar_astName(tom_match5_1); tom_match5_1_3 = ( TomType) tom_get_slot_VariableStar_astType(tom_match5_1); name = ( TomName) tom_match5_1_2; type = ( TomType) tom_match5_1_3;

 
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomNumberList path = tsf().makeTomNumberList();
          path = (TomNumberList) path.append(tom_make_RenamedVar(name) );
          path = appendNumber(mult,path);
          renamedTerm = tom_make_VariableStar(option(),tom_make_PositionName(path),type) ;

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = tom_make_EqualTerm(subject,renamedTerm) ;
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
       }}matchlab_match5_pattern3: {  TomName name = null;  OptionList optionList = null;  TomList args = null; if(tom_is_fun_sym_Appl(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomList tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Appl_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match5_1); tom_match5_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match5_1); if(tom_is_fun_sym_Option(tom_match5_1_1)) {  OptionList tom_match5_1_1_1 = null; tom_match5_1_1_1 = ( OptionList) tom_get_slot_Option_optionList(tom_match5_1_1); optionList = ( OptionList) tom_match5_1_1_1; name = ( TomName) tom_match5_1_2; args = ( TomList) tom_match5_1_3;

 
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }

        ArrayList list = new ArrayList();
        while(!optionList.isEmpty()) {
          Option optElt = optionList.getHead();
          Option newOptElt = optElt;
           {  Option tom_match6_1 = null; tom_match6_1 = ( Option) optElt;matchlab_match6_pattern1: {  TomTerm var = null; if(tom_is_fun_sym_TomTermToOption(tom_match6_1)) {  TomTerm tom_match6_1_1 = null; tom_match6_1_1 = ( TomTerm) tom_get_slot_TomTermToOption_astTerm(tom_match6_1); if(tom_is_fun_sym_Variable(tom_match6_1_1)) {  Option tom_match6_1_1_1 = null;  TomName tom_match6_1_1_2 = null;  TomType tom_match6_1_1_3 = null; tom_match6_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match6_1_1); tom_match6_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match6_1_1); tom_match6_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match6_1_1); var = ( TomTerm) tom_match6_1_1;
 
              newOptElt = tom_make_TomTermToOption(renameVariable(var, multiplicityMap, equalityCheck)) ;
             } }} }
 
          list.add(newOptElt);
          optionList = optionList.getTail();
        }
        OptionList newOptionList = ast().makeOptionList(list);
        renamedTerm = tom_make_Appl(tom_make_Option(newOptionList),name,newArgs) ;
        return renamedTerm;
       } }} }
 
    return renamedTerm;
  }

  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) {
    
      // collect variables
    ArrayList variableList = new ArrayList();
    collectVariable(variableList,tom_make_PatternList(subject) );
    
      // compute multiplicities
    HashMap multiplicityMap = new HashMap();
    Iterator it = variableList.iterator();
    while(it.hasNext()) {
      TomTerm variable = (TomTerm)it.next();
      TomName name = variable.getAstName();
      if(multiplicityMap.containsKey(name)) {
        Integer value = (Integer)multiplicityMap.get(name);
        multiplicityMap.put(name, new Integer(1+value.intValue()));
      } else {
        multiplicityMap.put(name, new Integer(1));
      }
    }
    
      // perform the renaming and generate equality checks
    TomList newList = empty();
    while(!subject.isEmpty()) {
      TomTerm elt = subject.getHead();
      TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
      
        // System.out.println("\nelt = " + elt + "\n --> newELt = " + newElt);
      
      newList = append(newElt,newList);
      subject = subject.getTail();
    }
    return newList;
  }
  
  private static int absVarNumber = 0;
  
  private TomTerm abstractPattern(TomTerm subject,
                                  ArrayList abstractedPattern,
                                  ArrayList introducedVariable) {
    TomTerm abstractedTerm = subject;
     {  TomTerm tom_match7_1 = null; tom_match7_1 = ( TomTerm) subject;matchlab_match7_pattern1: {  TomList args = null;  Option option = null;  TomName name = null;  String tomName = null; if(tom_is_fun_sym_Appl(tom_match7_1)) {  Option tom_match7_1_1 = null;  TomName tom_match7_1_2 = null;  TomList tom_match7_1_3 = null; tom_match7_1_1 = ( Option) tom_get_slot_Appl_option(tom_match7_1); tom_match7_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match7_1); tom_match7_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match7_1); option = ( Option) tom_match7_1_1; if(tom_is_fun_sym_Name(tom_match7_1_2)) {  String tom_match7_1_2_1 = null; tom_match7_1_2_1 = ( String) tom_get_slot_Name_string(tom_match7_1_2); name = ( TomName) tom_match7_1_2; tomName = ( String) tom_match7_1_2_1; args = ( TomList) tom_match7_1_3;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        
        TomList newArgs = empty();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          while(!args.isEmpty()) {
            TomTerm elt = args.getHead();
            TomTerm newElt = elt;
             {  TomTerm tom_match8_1 = null; tom_match8_1 = ( TomTerm) elt;matchlab_match8_pattern1: {  TomTerm appl = null;  String tomName2 = null; if(tom_is_fun_sym_Appl(tom_match8_1)) {  Option tom_match8_1_1 = null;  TomName tom_match8_1_2 = null;  TomList tom_match8_1_3 = null; tom_match8_1_1 = ( Option) tom_get_slot_Appl_option(tom_match8_1); tom_match8_1_2 = ( TomName) tom_get_slot_Appl_astName(tom_match8_1); tom_match8_1_3 = ( TomList) tom_get_slot_Appl_args(tom_match8_1); appl = ( TomTerm) tom_match8_1; if(tom_is_fun_sym_Name(tom_match8_1_2)) {  String tom_match8_1_2_1 = null; tom_match8_1_2_1 = ( String) tom_get_slot_Name_string(tom_match8_1_2); tomName2 = ( String) tom_match8_1_2_1;
 
                TomSymbol tomSymbol2 = symbolTable().getSymbol(tomName2);
                TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  //System.out.println("Abstract: " + appl);
                abstractedPattern.add(appl);

                TomNumberList path = tsf().makeTomNumberList();
                  //path = append(`AbsVar(makeNumber(introducedVariable.size())),path);
                absVarNumber++;
                path = (TomNumberList) path.append(tom_make_AbsVar(makeNumber(absVarNumber)) );
                  
                TomTerm newVariable = tom_make_Variable(option(),tom_make_PositionName(path),type2) ;

                  //System.out.println("newVariable = " + newVariable);
                                
                introducedVariable.add(newVariable);
                newElt = newVariable;
               } }} }
 
            newArgs = append(newElt,newArgs);
            args = args.getTail();
          }
        } else {
          newArgs = abstractPatternList(args,abstractedPattern,introducedVariable);
        }
        abstractedTerm = tom_make_Appl(option,name,newArgs) ;
       } }} }
  // end match
    return abstractedTerm;
  }

  private TomList abstractPatternList(TomList subjectList,
                                      ArrayList abstractedPattern,
                                      ArrayList introducedVariable) {
    TomList newList = empty();
    while(!subjectList.isEmpty()) {
      TomTerm elt = subjectList.getHead();
      TomTerm newElt = abstractPattern(elt,abstractedPattern,introducedVariable);
      newList = append(newElt,newList);
      subjectList = subjectList.getTail();
    }
    return newList;
  }
  
} // end of class
  
                  
    
