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

public class TomCompiler extends TomBase {

  public TomCompiler(jtom.TomEnvironment environment) {
    super(environment);
  }

// ------------------------------------------------------------
                                                                                                                                                             
// ------------------------------------------------------------

    /* 
     * pass2_1:
     *
     * replaces MakeTerm
     * transforms RuleSet into Function + Match + MakeTerm
     * abstract list-matching patterns
     * rename non-linear patterns
     */

  private static int matchNumber = 0;

  private Option option() {
    return ast().makeOption();
  }
  
  public TomTerm pass2_1(TomTerm subject) throws TomException {
      //%variable
      //System.out.println("pass2_1 subject: " + subject);
    Replace replace_pass2_1 = new Replace() {
        public ATerm apply(ATerm t) throws TomException { return pass2_1((TomTerm)t); }
      }; 
    Replace replace_pass2_1_makeTerm = new Replace() {
        public ATerm apply(ATerm t) throws TomException {
          TomTerm subject = (TomTerm)t;
          return pass2_1(tom_make_MakeTerm(subject) );
        }
      }; 

     { TomTerm tom_match1_1 = null; tom_match1_1 = (TomTerm) subject;matchlab_match1_pattern1: { TomList l = null; if(tom_is_fun_sym_Tom(tom_match1_1)) { TomList tom_match1_1_1 = null; tom_match1_1_1 = (TomList) tom_get_slot_Tom_list(tom_match1_1); l = (TomList) tom_match1_1_1;
 
        return tom_make_Tom(tomListMap(l, replace_pass2_1)) ;
       }}matchlab_match1_pattern2: { TomName name = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_Variable(tom_match1_1_1)) { Option tom_match1_1_1_1 = null; TomName tom_match1_1_1_2 = null; TomType tom_match1_1_1_3 = null; tom_match1_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match1_1_1); tom_match1_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match1_1_1); tom_match1_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match1_1_1); name = (TomName) tom_match1_1_1_2;

 
        statistics().numberMakeTermReplaced++;
        return tom_make_BuildVariable(name) ;
       } }}matchlab_match1_pattern3: { TomName name = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_VariableStar(tom_match1_1_1)) { Option tom_match1_1_1_1 = null; TomName tom_match1_1_1_2 = null; TomType tom_match1_1_1_3 = null; tom_match1_1_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match1_1_1); tom_match1_1_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match1_1_1); tom_match1_1_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match1_1_1); name = (TomName) tom_match1_1_1_2;

 
        statistics().numberMakeTermReplaced++;
        return tom_make_BuildVariableStar(name) ;
       } }}matchlab_match1_pattern4: { TomList termArgs = null; TomName name = null; String tomName = null; OptionList optionList = null; if(tom_is_fun_sym_MakeTerm(tom_match1_1)) { TomTerm tom_match1_1_1 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_MakeTerm_kid1(tom_match1_1); if(tom_is_fun_sym_Appl(tom_match1_1_1)) { Option tom_match1_1_1_1 = null; TomName tom_match1_1_1_2 = null; TomList tom_match1_1_1_3 = null; tom_match1_1_1_1 = (Option) tom_get_slot_Appl_option(tom_match1_1_1); tom_match1_1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match1_1_1); tom_match1_1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match1_1_1); if(tom_is_fun_sym_Option(tom_match1_1_1_1)) { OptionList tom_match1_1_1_1_1 = null; tom_match1_1_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match1_1_1_1); optionList = (OptionList) tom_match1_1_1_1_1; if(tom_is_fun_sym_Name(tom_match1_1_1_2)) { String tom_match1_1_1_2_1 = null; tom_match1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_1_2); name = (TomName) tom_match1_1_1_2; tomName = (String) tom_match1_1_1_2_1; termArgs = (TomList) tom_match1_1_1_3;

 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        statistics().numberMakeTermReplaced++;
        TomList newTermArgs = tomListMap(termArgs,replace_pass2_1_makeTerm);

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
       } } } }}matchlab_match1_pattern5: { String tomName = null; TomList tail = null; TomList ruleList = null; if(tom_is_fun_sym_RuleSet(tom_match1_1)) { Option tom_match1_1_1 = null; TomList tom_match1_1_2 = null; tom_match1_1_1 = (Option) tom_get_slot_RuleSet_option(tom_match1_1); tom_match1_1_2 = (TomList) tom_get_slot_RuleSet_list(tom_match1_1); if(tom_is_fun_sym_Cons(tom_match1_1_2)) { TomTerm tom_match1_1_2_1 = null; TomList tom_match1_1_2_2 = null; tom_match1_1_2_1 = (TomTerm) tom_get_slot_Cons_head(tom_match1_1_2); tom_match1_1_2_2 = (TomList) tom_get_slot_Cons_tail(tom_match1_1_2); ruleList = (TomList) tom_match1_1_2; if(tom_is_fun_sym_RewriteRule(tom_match1_1_2_1)) { TomTerm tom_match1_1_2_1_1 = null; TomTerm tom_match1_1_2_1_2 = null; Option tom_match1_1_2_1_3 = null; tom_match1_1_2_1_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match1_1_2_1); tom_match1_1_2_1_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match1_1_2_1); tom_match1_1_2_1_3 = (Option) tom_get_slot_RewriteRule_orgTrack(tom_match1_1_2_1); if(tom_is_fun_sym_Term(tom_match1_1_2_1_1)) { TomTerm tom_match1_1_2_1_1_1 = null; tom_match1_1_2_1_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match1_1_2_1_1); if(tom_is_fun_sym_Appl(tom_match1_1_2_1_1_1)) { Option tom_match1_1_2_1_1_1_1 = null; TomName tom_match1_1_2_1_1_1_2 = null; TomList tom_match1_1_2_1_1_1_3 = null; tom_match1_1_2_1_1_1_1 = (Option) tom_get_slot_Appl_option(tom_match1_1_2_1_1_1); tom_match1_1_2_1_1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match1_1_2_1_1_1); tom_match1_1_2_1_1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match1_1_2_1_1_1); if(tom_is_fun_sym_Name(tom_match1_1_2_1_1_1_2)) { String tom_match1_1_2_1_1_1_2_1 = null; tom_match1_1_2_1_1_1_2_1 = (String) tom_get_slot_Name_string(tom_match1_1_2_1_1_1_2); tomName = (String) tom_match1_1_2_1_1_1_2_1; tail = (TomList) tom_match1_1_2_2;


 

        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        TomName name = tomSymbol.getAstName();
        TomList typesList = tomSymbol.getTypesToType().getList();
        
	statistics().numberRuleSetsTransformed++;

          //System.out.println("*** RuleSet");
        
        TomList path = empty();
        TomList matchArgumentsList = empty();
        TomList patternActionList  = empty();
        TomTerm variable;
        int index = 0;

        path = append(tom_make_RuleVar() ,path);
        
        while(!typesList.isEmpty()) {
          TomType subtermType = typesList.getHead().getAstType();
          variable = tom_make_Variable(option(),tom_make_PositionName(append(makeNumber(index), path)),subtermType) ;
          matchArgumentsList = append(variable,matchArgumentsList);
          typesList = typesList.getTail();
          index++;
        }
        
        while(!ruleList.isEmpty()) {
          TomTerm rule = ruleList.getHead();
           { TomTerm tom_match2_1 = null; tom_match2_1 = (TomTerm) rule;matchlab_match2_pattern1: { TomList matchPatternsList = null; TomTerm rhsTerm = null; if(tom_is_fun_sym_RewriteRule(tom_match2_1)) { TomTerm tom_match2_1_1 = null; TomTerm tom_match2_1_2 = null; Option tom_match2_1_3 = null; tom_match2_1_1 = (TomTerm) tom_get_slot_RewriteRule_lhs(tom_match2_1); tom_match2_1_2 = (TomTerm) tom_get_slot_RewriteRule_rhs(tom_match2_1); tom_match2_1_3 = (Option) tom_get_slot_RewriteRule_orgTrack(tom_match2_1); if(tom_is_fun_sym_Term(tom_match2_1_1)) { TomTerm tom_match2_1_1_1 = null; tom_match2_1_1_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match2_1_1); if(tom_is_fun_sym_Appl(tom_match2_1_1_1)) { Option tom_match2_1_1_1_1 = null; TomName tom_match2_1_1_1_2 = null; TomList tom_match2_1_1_1_3 = null; tom_match2_1_1_1_1 = (Option) tom_get_slot_Appl_option(tom_match2_1_1_1); tom_match2_1_1_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match2_1_1_1); tom_match2_1_1_1_3 = (TomList) tom_get_slot_Appl_args(tom_match2_1_1_1); matchPatternsList = (TomList) tom_match2_1_1_1_3; if(tom_is_fun_sym_Term(tom_match2_1_2)) { TomTerm tom_match2_1_2_1 = null; tom_match2_1_2_1 = (TomTerm) tom_get_slot_Term_kid1(tom_match2_1_2); rhsTerm = (TomTerm) tom_match2_1_2_1;
 
              
              TomTerm newRhs = pass2_1(tom_make_MakeTerm(rhsTerm) );
              TomList rhsList = empty();
              if(Flags.supportedBlock) {
                rhsList = append(tom_make_OpenBlock() ,rhsList);
              }
              rhsList = append(tom_make_Return(newRhs) ,rhsList);
              if(Flags.supportedBlock) {
                rhsList = append(tom_make_CloseBlock() ,rhsList);
              }
              patternActionList = append(tom_make_PatternAction(tom_make_TermList(matchPatternsList),tom_make_Tom(rhsList)) ,patternActionList);
             } } } }} }
  
          ruleList = ruleList.getTail();
        }
        
        TomTerm subjectListAST = tom_make_SubjectList(matchArgumentsList) ;
        TomTerm makeFunctionBeginAST = tom_make_MakeFunctionBegin(name,subjectListAST) ;
        TomTerm matchAST = tom_make_Match(option(),tom_make_SubjectList(matchArgumentsList),tom_make_PatternList(patternActionList))

 ;
        TomTerm buildAST = tom_make_Return(tom_make_BuildTerm(name,tomListMap(matchArgumentsList, replace_pass2_1_makeTerm))) ;
        TomList l = empty();
        if(Flags.eCode) {
          l = append(makeFunctionBeginAST,l);
          l = append(tom_make_LocalVariable() ,l);
          l = append(tom_make_EndLocalVariable() ,l);
          l = append(matchAST,l);
          l = append(buildAST,l);
          l = append(tom_make_MakeFunctionEnd() ,l);
        } else {
          l = append(makeFunctionBeginAST,l);
          l = append(matchAST,l);
          l = append(buildAST,l);
          l = append(tom_make_MakeFunctionEnd() ,l);
        }
        return pass2_1(tom_make_Tom(l) );
       } } } } } }}matchlab_match1_pattern6: { TomTerm tom = null; TomList actionList = null; TomTerm tl = null; TomList termList = null; if(tom_is_fun_sym_PatternAction(tom_match1_1)) { TomTerm tom_match1_1_1 = null; TomTerm tom_match1_1_2 = null; tom_match1_1_1 = (TomTerm) tom_get_slot_PatternAction_termList(tom_match1_1); tom_match1_1_2 = (TomTerm) tom_get_slot_PatternAction_tom(tom_match1_1); if(tom_is_fun_sym_TermList(tom_match1_1_1)) { TomList tom_match1_1_1_1 = null; tom_match1_1_1_1 = (TomList) tom_get_slot_TermList_list(tom_match1_1_1); tl = (TomTerm) tom_match1_1_1; termList = (TomList) tom_match1_1_1_1; if(tom_is_fun_sym_Tom(tom_match1_1_2)) { TomList tom_match1_1_2_1 = null; tom_match1_1_2_1 = (TomList) tom_get_slot_Tom_list(tom_match1_1_2); tom = (TomTerm) tom_match1_1_2; actionList = (TomList) tom_match1_1_2_1;

 
        TomTerm newPatternAction = tom_make_PatternAction(tl,pass2_1(tom)) ;
        return newPatternAction;
       } } }}matchlab_match1_pattern7: { Option option = null; TomList l1 = null; TomList l2 = null; if(tom_is_fun_sym_Match(tom_match1_1)) { Option tom_match1_1_1 = null; TomTerm tom_match1_1_2 = null; TomTerm tom_match1_1_3 = null; tom_match1_1_1 = (Option) tom_get_slot_Match_option(tom_match1_1); tom_match1_1_2 = (TomTerm) tom_get_slot_Match_subjectList(tom_match1_1); tom_match1_1_3 = (TomTerm) tom_get_slot_Match_patternList(tom_match1_1); option = (Option) tom_match1_1_1; if(tom_is_fun_sym_SubjectList(tom_match1_1_2)) { TomList tom_match1_1_2_1 = null; tom_match1_1_2_1 = (TomList) tom_get_slot_SubjectList_list(tom_match1_1_2); l1 = (TomList) tom_match1_1_2_1; if(tom_is_fun_sym_PatternList(tom_match1_1_3)) { TomList tom_match1_1_3_1 = null; tom_match1_1_3_1 = (TomList) tom_get_slot_PatternList_list(tom_match1_1_3); l2 = (TomList) tom_match1_1_3_1;

 
        TomList newPatternList = empty();
        while(!l2.isEmpty()) {
          TomTerm elt = pass2_1(l2.getHead());
          TomTerm newPatternAction = elt;
          
          matchBlock: {
             { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) elt;matchlab_match3_pattern1: { TomList actionList = null; TomList termList = null; if(tom_is_fun_sym_PatternAction(tom_match3_1)) { TomTerm tom_match3_1_1 = null; TomTerm tom_match3_1_2 = null; tom_match3_1_1 = (TomTerm) tom_get_slot_PatternAction_termList(tom_match3_1); tom_match3_1_2 = (TomTerm) tom_get_slot_PatternAction_tom(tom_match3_1); if(tom_is_fun_sym_TermList(tom_match3_1_1)) { TomList tom_match3_1_1_1 = null; tom_match3_1_1_1 = (TomList) tom_get_slot_TermList_list(tom_match3_1_1); termList = (TomList) tom_match3_1_1_1; if(tom_is_fun_sym_Tom(tom_match3_1_2)) { TomList tom_match3_1_2_1 = null; tom_match3_1_2_1 = (TomList) tom_get_slot_Tom_list(tom_match3_1_2); actionList = (TomList) tom_match3_1_2_1;
 
                TomList newTermList = empty();
                TomList newActionList = actionList;

                  // generate equality checks
                ArrayList equalityCheck = new ArrayList();
                TomList renamedTermList = linearizePattern(termList,equalityCheck);
                if(equalityCheck.size() > 0) {
                  Expression cond = tom_make_TrueTL() ;
                  Iterator it = equalityCheck.iterator();
                  while(it.hasNext()) {
                    Expression equality = (Expression)it.next();
                    cond = tom_make_And(equality,cond) ;
                  }
                  newActionList = cons(tom_make_IfThenElse(cond,actionList,empty()) ,empty());
                  newPatternAction = tom_make_PatternAction(tom_make_TermList(renamedTermList),tom_make_Tom(newActionList)) ;        
                    //System.out.println("\nnewPatternAction = " + newPatternAction);
                }

                  // abstract patterns
                ArrayList abstractedPattern  = new ArrayList();
                ArrayList introducedVariable = new ArrayList();
                newTermList = abstractPatternList(renamedTermList, abstractedPattern, introducedVariable);
                if(abstractedPattern.size() > 0) {
                    // generate a new match construct
                  TomTerm generatedPatternAction =
                    tom_make_PatternAction(tom_make_TermList(ast().makeList(abstractedPattern)),tom_make_Tom(newActionList)) ;        
                  
                  TomTerm generatedMatch =
                    tom_make_Match(option(),tom_make_SubjectList(ast().makeList(introducedVariable)),tom_make_PatternList(cons(generatedPatternAction, empty())))

 ;
                  
                  generatedMatch = pass2_1(generatedMatch);
                  newPatternAction =
                    tom_make_PatternAction(tom_make_TermList(newTermList),tom_make_Tom(cons(generatedMatch, empty()))) ;

                    //System.out.println("newPatternAction = " + newPatternAction);
                }
                  // do nothing
                break matchBlock;
               } } }}matchlab_match3_pattern2: {

 
                System.out.println("pass2_1: strange PatternAction: " + elt);
                System.exit(1);
              } }
 
          } // end matchBlock

          newPatternList = append(newPatternAction,newPatternList);
          l2 = l2.getTail();
        }

        TomTerm newMatch = tom_make_Match(option,tom_make_SubjectList(l1),tom_make_PatternList(newPatternList))

 ;
        return newMatch;
       } } }}matchlab_match1_pattern8: { TomTerm t = null; t = (TomTerm) tom_match1_1;


 
          //System.out.println("pass2_1 default: " + t);
        return t;
      } }
 
  }

  private TomTerm renameVariable(TomTerm subject,
                                 HashMap multiplicityMap,
                                 ArrayList equalityCheck) {
    TomTerm renamedTerm = subject;
    
     { TomTerm tom_match4_1 = null; tom_match4_1 = (TomTerm) subject;matchlab_match4_pattern1: { TomType type = null; TomName name = null; if(tom_is_fun_sym_Variable(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomType tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_Variable_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match4_1); tom_match4_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match4_1); name = (TomName) tom_match4_1_2; type = (TomType) tom_match4_1_3;
 
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomList path = empty();
          path = append(tom_make_RenamedVar(name) ,path);
          path = append(makeNumber(mult),path);
          renamedTerm = tom_make_Variable(option(),tom_make_PositionName(path),type) ;

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = tom_make_EqualTerm(subject,renamedTerm) ;
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
       }}matchlab_match4_pattern2: { TomName name = null; TomType type = null; if(tom_is_fun_sym_VariableStar(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomType tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match4_1); tom_match4_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match4_1); name = (TomName) tom_match4_1_2; type = (TomType) tom_match4_1_3;

 
        Integer multiplicity = (Integer)multiplicityMap.get(name);
        int mult = multiplicity.intValue();
        if(mult > 1) {
          mult = mult-1;
          multiplicityMap.put(name,new Integer(mult));
          
          TomList path = empty();
          path = append(tom_make_RenamedVar(name) ,path);
          path = append(makeNumber(mult),path);
          renamedTerm = tom_make_VariableStar(option(),tom_make_PositionName(path),type) ;

            //System.out.println("renamedTerm = " + renamedTerm);

          Expression newEquality = tom_make_EqualTerm(subject,renamedTerm) ;
          equalityCheck.add(newEquality);
        }
        return renamedTerm;
       }}matchlab_match4_pattern3: { OptionList optionList = null; TomList args = null; TomName name = null; if(tom_is_fun_sym_Appl(tom_match4_1)) { Option tom_match4_1_1 = null; TomName tom_match4_1_2 = null; TomList tom_match4_1_3 = null; tom_match4_1_1 = (Option) tom_get_slot_Appl_option(tom_match4_1); tom_match4_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match4_1); tom_match4_1_3 = (TomList) tom_get_slot_Appl_args(tom_match4_1); if(tom_is_fun_sym_Option(tom_match4_1_1)) { OptionList tom_match4_1_1_1 = null; tom_match4_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match4_1_1); optionList = (OptionList) tom_match4_1_1_1; name = (TomName) tom_match4_1_2; args = (TomList) tom_match4_1_3;

 
        TomList newArgs = empty();
        while(!args.isEmpty()) {
          TomTerm elt = args.getHead();
          TomTerm newElt = renameVariable(elt,multiplicityMap,equalityCheck);
          newArgs = append(newElt,newArgs);
          args = args.getTail();
        }

        ArrayList list = new ArrayList();
        while(!optionList.isEmptyOptionList()) {
          Option optElt = optionList.getHead();
          Option newOptElt = optElt;
           { Option tom_match5_1 = null; tom_match5_1 = (Option) optElt;matchlab_match5_pattern1: { TomTerm var = null; if(tom_is_fun_sym_TomTermToOption(tom_match5_1)) { TomTerm tom_match5_1_1 = null; tom_match5_1_1 = (TomTerm) tom_get_slot_TomTermToOption_astTerm(tom_match5_1); if(tom_is_fun_sym_Variable(tom_match5_1_1)) { Option tom_match5_1_1_1 = null; TomName tom_match5_1_1_2 = null; TomType tom_match5_1_1_3 = null; tom_match5_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match5_1_1); tom_match5_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match5_1_1); tom_match5_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match5_1_1); var = (TomTerm) tom_match5_1_1;
 
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

  private TomList linearizePattern(TomList subject, ArrayList equalityCheck) throws TomException {

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

  
  private TomTerm abstractPattern(TomTerm subject,
                                  ArrayList abstractedPattern,
                                  ArrayList introducedVariable) {
    TomTerm abstractedTerm = subject;
     { TomTerm tom_match6_1 = null; tom_match6_1 = (TomTerm) subject;matchlab_match6_pattern1: { TomList args = null; String tomName = null; TomName name = null; Option option = null; if(tom_is_fun_sym_Appl(tom_match6_1)) { Option tom_match6_1_1 = null; TomName tom_match6_1_2 = null; TomList tom_match6_1_3 = null; tom_match6_1_1 = (Option) tom_get_slot_Appl_option(tom_match6_1); tom_match6_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match6_1); tom_match6_1_3 = (TomList) tom_get_slot_Appl_args(tom_match6_1); option = (Option) tom_match6_1_1; if(tom_is_fun_sym_Name(tom_match6_1_2)) { String tom_match6_1_2_1 = null; tom_match6_1_2_1 = (String) tom_get_slot_Name_string(tom_match6_1_2); name = (TomName) tom_match6_1_2; tomName = (String) tom_match6_1_2_1; args = (TomList) tom_match6_1_3;
 
        TomSymbol tomSymbol = symbolTable().getSymbol(tomName);
        
        TomList newArgs = empty();
        if(isListOperator(tomSymbol) || isArrayOperator(tomSymbol)) {
          while(!args.isEmpty()) {
            TomTerm elt = args.getHead();
            TomTerm newElt = elt;
             { TomTerm tom_match7_1 = null; tom_match7_1 = (TomTerm) elt;matchlab_match7_pattern1: { String tomName2 = null; TomTerm appl = null; if(tom_is_fun_sym_Appl(tom_match7_1)) { Option tom_match7_1_1 = null; TomName tom_match7_1_2 = null; TomList tom_match7_1_3 = null; tom_match7_1_1 = (Option) tom_get_slot_Appl_option(tom_match7_1); tom_match7_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match7_1); tom_match7_1_3 = (TomList) tom_get_slot_Appl_args(tom_match7_1); appl = (TomTerm) tom_match7_1; if(tom_is_fun_sym_Name(tom_match7_1_2)) { String tom_match7_1_2_1 = null; tom_match7_1_2_1 = (String) tom_get_slot_Name_string(tom_match7_1_2); tomName2 = (String) tom_match7_1_2_1;
 
                TomSymbol tomSymbol2 = symbolTable().getSymbol(tomName2);
                TomType type2 = tomSymbol2.getTypesToType().getCodomain();
                  //System.out.println("Abstract: " + appl);
                abstractedPattern.add(appl);

                TomList path = empty();
                path = append(tom_make_AbsVar(makeNumber(introducedVariable.size())) ,path);
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
  
    /* 
     * pass2_2:
     *
     * compiles Match into and automaton
     */
 
  public TomTerm pass2_2(TomTerm subject) throws TomException {
      //%variable
    Replace replace_pass2_2 = new Replace() {
        public ATerm apply(ATerm t) throws TomException { return pass2_2((TomTerm)t); }
      }; 

     { TomTerm tom_match8_1 = null; tom_match8_1 = (TomTerm) subject;matchlab_match8_pattern1: { TomList l = null; if(tom_is_fun_sym_Tom(tom_match8_1)) { TomList tom_match8_1_1 = null; tom_match8_1_1 = (TomList) tom_get_slot_Tom_list(tom_match8_1); l = (TomList) tom_match8_1_1;
 
        return tom_make_Tom(tomListMap(l, replace_pass2_2)) ;
       }}matchlab_match8_pattern2: { Option optionMatch = null; TomList l2 = null; TomList l1 = null; if(tom_is_fun_sym_Match(tom_match8_1)) { Option tom_match8_1_1 = null; TomTerm tom_match8_1_2 = null; TomTerm tom_match8_1_3 = null; tom_match8_1_1 = (Option) tom_get_slot_Match_option(tom_match8_1); tom_match8_1_2 = (TomTerm) tom_get_slot_Match_subjectList(tom_match8_1); tom_match8_1_3 = (TomTerm) tom_get_slot_Match_patternList(tom_match8_1); optionMatch = (Option) tom_match8_1_1; if(tom_is_fun_sym_SubjectList(tom_match8_1_2)) { TomList tom_match8_1_2_1 = null; tom_match8_1_2_1 = (TomList) tom_get_slot_SubjectList_list(tom_match8_1_2); l1 = (TomList) tom_match8_1_2_1; if(tom_is_fun_sym_PatternList(tom_match8_1_3)) { TomList tom_match8_1_3_1 = null; tom_match8_1_3_1 = (TomList) tom_get_slot_PatternList_list(tom_match8_1_3); l2 = (TomList) tom_match8_1_3_1;

 
        statistics().numberMatchCompiledIntoAutomaton++;

        TomList termList, actionList;
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
           { TomTerm tom_match9_1 = null; tom_match9_1 = (TomTerm) tlVariable;matchlab_match9_pattern1: { TomType variableType = null; Option option = null; if(tom_is_fun_sym_Variable(tom_match9_1)) { Option tom_match9_1_1 = null; TomName tom_match9_1_2 = null; TomType tom_match9_1_3 = null; tom_match9_1_1 = (Option) tom_get_slot_Variable_option(tom_match9_1); tom_match9_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match9_1); tom_match9_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match9_1); option = (Option) tom_match9_1_1; variableType = (TomType) tom_match9_1_3;
 
              TomTerm variable = tom_make_Variable(option,tom_make_PositionName(append(makeNumber(index), path)),variableType) ;
              matchDeclarationList = append(tom_make_Declaration(variable) ,matchDeclarationList);
              matchAssignementList = append(tom_make_Assign(variable,tom_make_TomTermToExpression(tlVariable)) ,matchAssignementList);
             }} }
  
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
          termList = pa.getTermList().getList();
          actionList = pa.getTom().getList();

            //System.out.println("termList   = " + termList);
            //System.out.println("actionList = " + actionList);
          if(termList==null || actionList==null) {
            System.out.println("TomCompiler: null value");
            System.exit(1);
          }

            // compile nested match constructs
          actionList = tomListMap(actionList,replace_pass2_2);
              
            //System.out.println("termList      = " + termList);
            //System.out.println("actionNumber  = " + actionNumber);
            //System.out.println("action        = " + actionList);
          TomList patternsDeclarationList = empty();
          Collection variableCollection = new HashSet();
          collectVariable(variableCollection,tom_make_Tom(termList) );
          
          Iterator it = variableCollection.iterator();
          while(it.hasNext()) {
            TomTerm tmpsubject = (TomTerm)it.next();
            patternsDeclarationList = append(tom_make_Declaration(tmpsubject) ,patternsDeclarationList);
              //System.out.println("*** " + patternsDeclarationList);
          }

          TomList numberList = append(tom_make_PatternNumber(makeNumber(actionNumber)) ,path);
          TomList instructionList;
          instructionList = genTermListMatchingAutomata(termList,path,1,actionList,true);
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

        TomList astAutomataList = automataListPass2_2List(automataList);
        return tom_make_CompiledMatch(matchDeclarationList,astAutomataList) ;
       } } }}matchlab_match8_pattern3: { TomTerm t = null; t = (TomTerm) tom_match8_1;


 
          //System.out.println("default: " + t);
        return t;
      } }
 
  }

  private TomList automataListPass2_2List(TomList automataList) {
      //%variable
    
     { TomList tom_match10_1 = null; tom_match10_1 = (TomList) automataList;matchlab_match10_pattern1: { if(tom_is_fun_sym_Empty(tom_match10_1)) {


  return empty();  }}matchlab_match10_pattern2: { TomList l = null; TomList instList = null; TomList numberList = null; if(tom_is_fun_sym_Cons(tom_match10_1)) { TomTerm tom_match10_1_1 = null; TomList tom_match10_1_2 = null; tom_match10_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match10_1); tom_match10_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match10_1); if(tom_is_fun_sym_Automata(tom_match10_1_1)) { TomList tom_match10_1_1_1 = null; TomList tom_match10_1_1_2 = null; tom_match10_1_1_1 = (TomList) tom_get_slot_Automata_numberList(tom_match10_1_1); tom_match10_1_1_2 = (TomList) tom_get_slot_Automata_instList(tom_match10_1_1); numberList = (TomList) tom_match10_1_1_1; instList = (TomList) tom_match10_1_1_2; l = (TomList) tom_match10_1_2;
 
        TomList newList = automataListPass2_2List(l);
        
        if(Flags.supportedGoto) {
          return cons(tom_make_NamedBlock(getBlockName(numberList),instList) , newList);
        } else {
          TomList result = empty();
          TomTerm variableAST = getBlockVariable(numberList);
          result = append(tom_make_Declaration(variableAST) ,result);
          result = append(tom_make_Assign(variableAST,tom_make_TrueTL()) ,result);
          if(Flags.supportedBlock) { // Test
            result = append(tom_make_OpenBlock() ,result);
          }
          result = concat(result,instList);
          if(Flags.supportedBlock) { // Test
            result = append(tom_make_CloseBlock() ,result);
          }
          result = concat(result,newList);
          return result;
        }
       } }} }
 
    return null;
  }

    /* 
     * pass3: passCompiledTermTransformation
     *
     * transform a compiledTerm
     * 2 phases:
     *   - collection of Declaration
     *   - replace LocalVariable and remove Declaration
     */

  public TomTerm pass3(TomTerm subject) throws TomException {
    TomTerm res;
    ArrayList list = new ArrayList();
    traversalCollectDeclaration(list,subject);
      //System.out.println("list size = " + list.size());
    res = traversalReplaceLocalVariable(list,subject);
    return res;
  }
    
  private TomTerm traversalCollectDeclaration(ArrayList list, TomTerm subject) throws TomException{
      //%variable
     { TomTerm tom_match11_1 = null; tom_match11_1 = (TomTerm) subject;matchlab_match11_pattern1: { TomList l = null; if(tom_is_fun_sym_Tom(tom_match11_1)) { TomList tom_match11_1_1 = null; tom_match11_1_1 = (TomList) tom_get_slot_Tom_list(tom_match11_1); l = (TomList) tom_match11_1_1;
 
        return tom_make_Tom(traversalCollectDeclarationList(list, l)) ;
       }}matchlab_match11_pattern2: { if(tom_is_fun_sym_LocalVariable(tom_match11_1)) {

 
          //System.out.println("Detect LocalVariable");
        
        Collection c = new HashSet();
        list.add(c);
        collectDeclaration(c,subject);
        return null;
       }}matchlab_match11_pattern3: { TomTerm t = null; t = (TomTerm) tom_match11_1;

 
          //System.out.println("default: " + t);
        if(!list.isEmpty()) {
          Collection c = (Collection) list.get(list.size()-1);
          collectDeclaration(c,subject);
        }
        return t;
      } }
 
  }
    

  private TomList traversalCollectDeclarationList(ArrayList list,TomList subject) throws TomException {
      //%variable
    if(subject.isEmpty()) {
      return subject;
    }
    TomTerm t = subject.getHead();
    TomList l = subject.getTail();
    return cons(traversalCollectDeclaration(list,t),
                traversalCollectDeclarationList(list,l));
  }

  private boolean removeDeclaration = false;
  private TomTerm traversalReplaceLocalVariable(ArrayList list, TomTerm subject) {
      //%variable
     { TomTerm tom_match12_1 = null; tom_match12_1 = (TomTerm) subject;matchlab_match12_pattern1: { TomList l = null; if(tom_is_fun_sym_Tom(tom_match12_1)) { TomList tom_match12_1_1 = null; tom_match12_1_1 = (TomList) tom_get_slot_Tom_list(tom_match12_1); l = (TomList) tom_match12_1_1;
 
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
       }}matchlab_match12_pattern3: { TomTerm t = null; t = (TomTerm) tom_match12_1;






 
        TomTerm res = t;
          //res = removeDeclaration(t);
        
        if(removeDeclaration) {
          res = removeDeclaration(t);
        }
        
          //System.out.println("\ndefault:\nt   = " + t + "\nres = " + res);
        return res;
      } }
 
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
  
    // ------------------------------------------------------------
  public void collectVariable(final Collection collection, TomTerm subject) throws TomException {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) throws TomException {
            //%variable
          if(t instanceof TomTerm) {
            TomTerm annotedVariable = null;
             { TomTerm tom_match13_1 = null; tom_match13_1 = (TomTerm) t;matchlab_match13_pattern1: { OptionList optionList = null; if(tom_is_fun_sym_Variable(tom_match13_1)) { Option tom_match13_1_1 = null; TomName tom_match13_1_2 = null; TomType tom_match13_1_3 = null; tom_match13_1_1 = (Option) tom_get_slot_Variable_option(tom_match13_1); tom_match13_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match13_1); tom_match13_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match13_1); if(tom_is_fun_sym_Option(tom_match13_1_1)) { OptionList tom_match13_1_1_1 = null; tom_match13_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match13_1_1); optionList = (OptionList) tom_match13_1_1_1;
 
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match13_pattern2: { OptionList optionList = null; if(tom_is_fun_sym_VariableStar(tom_match13_1)) { Option tom_match13_1_1 = null; TomName tom_match13_1_2 = null; TomType tom_match13_1_3 = null; tom_match13_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match13_1); tom_match13_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match13_1); tom_match13_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match13_1); if(tom_is_fun_sym_Option(tom_match13_1_1)) { OptionList tom_match13_1_1_1 = null; tom_match13_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match13_1_1); optionList = (OptionList) tom_match13_1_1_1;

 
                collection.add(t);
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match13_pattern3: { OptionList optionList = null; if(tom_is_fun_sym_UnamedVariable(tom_match13_1)) { Option tom_match13_1_1 = null; TomType tom_match13_1_2 = null; tom_match13_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match13_1); tom_match13_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match13_1); if(tom_is_fun_sym_Option(tom_match13_1_1)) { OptionList tom_match13_1_1_1 = null; tom_match13_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match13_1_1); optionList = (OptionList) tom_match13_1_1_1;

 
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match13_pattern4: { TomList subterms = null; OptionList optionList = null; if(tom_is_fun_sym_Appl(tom_match13_1)) { Option tom_match13_1_1 = null; TomName tom_match13_1_2 = null; TomList tom_match13_1_3 = null; tom_match13_1_1 = (Option) tom_get_slot_Appl_option(tom_match13_1); tom_match13_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match13_1); tom_match13_1_3 = (TomList) tom_get_slot_Appl_args(tom_match13_1); if(tom_is_fun_sym_Option(tom_match13_1_1)) { OptionList tom_match13_1_1_1 = null; tom_match13_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match13_1_1); optionList = (OptionList) tom_match13_1_1_1; subterms = (TomList) tom_match13_1_3;


 
                collectVariable(collection,tom_make_Tom(subterms) );
                annotedVariable = getAnnotedVariable(optionList);
                if(annotedVariable!=null) {
                  collection.add(annotedVariable);
                }
                return false;
               } }}matchlab_match13_pattern5: {

  return true; } }
 
          } else {
            return true;
          }
        } // end apply
      }; // end new
    
    genericCollect(subject, collect); 
  } 

  public void collectDeclaration(final Collection collection, TomTerm subject) throws TomException {
    Collect collect = new Collect() { 
        public boolean apply(ATerm t) {
            //%variable
           { TomTerm tom_match14_1 = null; tom_match14_1 = (TomTerm) t;matchlab_match14_pattern1: { if(tom_is_fun_sym_Declaration(tom_match14_1)) { TomTerm tom_match14_1_1 = null; tom_match14_1_1 = (TomTerm) tom_get_slot_Declaration_kid1(tom_match14_1);
 
              collection.add(t);
              return false;
             }}matchlab_match14_pattern2: {
  return true; } }
 
        } 
      }; // end new
    
    genericCollect(subject, collect); 
  } 

    // ------------------------------------------------------------
  
  public TomTerm removeDeclaration(TomTerm subject) {
    TomTerm res = subject;
      //System.out.println("*** removeDeclaration");
                  
    Replace replace = new Replace() { 
        public ATerm apply(ATerm t) {
            //%variable
           { TomTerm tom_match15_1 = null; tom_match15_1 = (TomTerm) t;matchlab_match15_pattern1: { if(tom_is_fun_sym_Declaration(tom_match15_1)) { TomTerm tom_match15_1_1 = null; tom_match15_1_1 = (TomTerm) tom_get_slot_Declaration_kid1(tom_match15_1);
 
                //System.out.println("Remove Declaration");
              return tom_make_Tom(empty()) ;
             }}matchlab_match15_pattern2: { TomTerm other = null; other = (TomTerm) tom_match15_1;

 
              System.out.println("removeDeclaration this = " + this);
                //return other;
              return (TomTerm) genericTraversal(other,this);
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

  private TomTerm getAnnotedVariable(OptionList subjectList) {
      //%variable
    while(!subjectList.isEmptyOptionList()) {
      Option subject = subjectList.getHead();
       { Option tom_match16_1 = null; tom_match16_1 = (Option) subject;matchlab_match16_pattern1: { TomName name = null; Option option = null; TomTerm var = null; TomType type = null; if(tom_is_fun_sym_TomTermToOption(tom_match16_1)) { TomTerm tom_match16_1_1 = null; tom_match16_1_1 = (TomTerm) tom_get_slot_TomTermToOption_astTerm(tom_match16_1); if(tom_is_fun_sym_Variable(tom_match16_1_1)) { Option tom_match16_1_1_1 = null; TomName tom_match16_1_1_2 = null; TomType tom_match16_1_1_3 = null; tom_match16_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match16_1_1); tom_match16_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match16_1_1); tom_match16_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match16_1_1); var = (TomTerm) tom_match16_1_1; option = (Option) tom_match16_1_1_1; name = (TomName) tom_match16_1_1_2; type = (TomType) tom_match16_1_1_3;
 
          return var;
         } }} }
 
      subjectList = subjectList.getTail();
    }
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
  
  private boolean isDefinedSymbol(TomSymbol subject) {
      //%variable
    if(subject==null) {
      return false;
    }
     { TomSymbol tom_match17_1 = null; tom_match17_1 = (TomSymbol) subject;matchlab_match17_pattern1: { String name1 = null; TargetLanguage tlCode1 = null; TomList typeList = null; OptionList optionList = null; TomType type1 = null; if(tom_is_fun_sym_Symbol(tom_match17_1)) { TomName tom_match17_1_1 = null; TomType tom_match17_1_2 = null; SlotList tom_match17_1_3 = null; Option tom_match17_1_4 = null; TargetLanguage tom_match17_1_5 = null; tom_match17_1_1 = (TomName) tom_get_slot_Symbol_astName(tom_match17_1); tom_match17_1_2 = (TomType) tom_get_slot_Symbol_typesToType(tom_match17_1); tom_match17_1_3 = (SlotList) tom_get_slot_Symbol_slotList(tom_match17_1); tom_match17_1_4 = (Option) tom_get_slot_Symbol_option(tom_match17_1); tom_match17_1_5 = (TargetLanguage) tom_get_slot_Symbol_tlCode(tom_match17_1); if(tom_is_fun_sym_Name(tom_match17_1_1)) { String tom_match17_1_1_1 = null; tom_match17_1_1_1 = (String) tom_get_slot_Name_string(tom_match17_1_1); name1 = (String) tom_match17_1_1_1; if(tom_is_fun_sym_TypesToType(tom_match17_1_2)) { TomList tom_match17_1_2_1 = null; TomType tom_match17_1_2_2 = null; tom_match17_1_2_1 = (TomList) tom_get_slot_TypesToType_list(tom_match17_1_2); tom_match17_1_2_2 = (TomType) tom_get_slot_TypesToType_codomain(tom_match17_1_2); typeList = (TomList) tom_match17_1_2_1; type1 = (TomType) tom_match17_1_2_2; if(tom_is_fun_sym_Option(tom_match17_1_4)) { OptionList tom_match17_1_4_1 = null; tom_match17_1_4_1 = (OptionList) tom_get_slot_Option_optionList(tom_match17_1_4); optionList = (OptionList) tom_match17_1_4_1; tlCode1 = (TargetLanguage) tom_match17_1_5;
 
        while(!optionList.isEmptyOptionList()) {
          Option opt = optionList.getHead();
           { Option tom_match18_1 = null; tom_match18_1 = (Option) opt;matchlab_match18_pattern1: { if(tom_is_fun_sym_DefinedSymbol(tom_match18_1)) {
  return true;  }} }
 
          optionList = optionList.getTail();
        }
        return false;
       } } } }}matchlab_match17_pattern2: {

 
        System.out.println("isDefinedSymbol: strange case: '" + subject + "'");
        System.exit(1);
      } }
 
    return false;
  }

    /*
     * ------------------------------------------------------------
     * Generate a matching automaton
     * ------------------------------------------------------------
     */

  TomList genTermListMatchingAutomata(TomList termList,
                                      TomList path,
                                      int indexTerm,
                                      TomList actionList,
                                      boolean gsa) {
    TomList result = empty();
      //%variable
    if(termList.isEmpty()) {
      if(gsa) {
          // insert the semantic action
        TomTerm action = tom_make_Action(actionList) ;
        result = append(action,result);
      }
    } else {
      TomTerm head = termList.getHead();
      TomList tail = termList.getTail();
      TomList newSubActionList = genTermListMatchingAutomata(tail,path,indexTerm+1,actionList,gsa);
      TomList newPath          = append(makeNumber(indexTerm),path);
      TomList newActionList    = genTermMatchingAutomata(head,newPath,newSubActionList,gsa);
      result                   = concat(result,newActionList);
    }
    return result;
  }

  TomList genTermMatchingAutomata(TomTerm term,
                                  TomList path,
                                  TomList actionList,
                                  boolean gsa) {
    TomList result = empty();
      //%variable
    matchBlock: {
       { TomTerm tom_match19_1 = null; tom_match19_1 = (TomTerm) term;matchlab_match19_pattern1: { OptionList optionList = null; TomTerm var = null; TomType termType = null; if(tom_is_fun_sym_Variable(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomType tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_Variable_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match19_1); tom_match19_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match19_1); var = (TomTerm) tom_match19_1; if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; termType = (TomType) tom_match19_1_3;
 
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          TomTerm assignement = tom_make_Assign(var,tom_make_TomTermToExpression(tom_make_Variable(option(),tom_make_PositionName(path),termType))) ;
          result = append(assignement,result);

          if(annotedVariable != null) {
            assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(var)) ; 
            result = append(assignement,result);
          }
          if(gsa) {
            result = append(tom_make_Action(actionList) ,result);
          }
          
          break matchBlock;
         } }}matchlab_match19_pattern2: { TomType termType = null; OptionList optionList = null; if(tom_is_fun_sym_UnamedVariable(tom_match19_1)) { Option tom_match19_1_1 = null; TomType tom_match19_1_2 = null; tom_match19_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match19_1); tom_match19_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match19_1); if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; termType = (TomType) tom_match19_1_2;

 
          TomTerm annotedVariable = getAnnotedVariable(optionList);
          if(annotedVariable != null) {
            TomTerm assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(tom_make_Variable(option(),tom_make_PositionName(path),termType))) ;
            result = append(assignement,result);
          }
          if(gsa) {
            result = append(tom_make_Action(actionList) ,result);
          }

          break matchBlock;
         } }}matchlab_match19_pattern3: { String tomName = null; OptionList optionList = null; TomList termArgs = null; if(tom_is_fun_sym_Appl(tom_match19_1)) { Option tom_match19_1_1 = null; TomName tom_match19_1_2 = null; TomList tom_match19_1_3 = null; tom_match19_1_1 = (Option) tom_get_slot_Appl_option(tom_match19_1); tom_match19_1_2 = (TomName) tom_get_slot_Appl_astName(tom_match19_1); tom_match19_1_3 = (TomList) tom_get_slot_Appl_args(tom_match19_1); if(tom_is_fun_sym_Option(tom_match19_1_1)) { OptionList tom_match19_1_1_1 = null; tom_match19_1_1_1 = (OptionList) tom_get_slot_Option_optionList(tom_match19_1_1); optionList = (OptionList) tom_match19_1_1_1; if(tom_is_fun_sym_Name(tom_match19_1_2)) { String tom_match19_1_2_1 = null; tom_match19_1_2_1 = (String) tom_get_slot_Name_string(tom_match19_1_2); tomName = (String) tom_match19_1_2_1; termArgs = (TomList) tom_match19_1_3;

 
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
            
            TomTerm assignement  = tom_make_Assign(newVariableAST,getSubtermAST) ;
            assignementList      = append(assignement,assignementList);
            
            indexSubterm++;
            termTypeList = termTypeList.getTail();
          }
          
            // generate an assignement for annoted variables
          if(annotedVariable != null) {
            TomTerm assignement = tom_make_Assign(annotedVariable,tom_make_TomTermToExpression(subjectVariableAST)) ;
            annotedAssignementList   = append(assignement,annotedAssignementList);
          }
          
          TomList automataList  = null;
          TomList succesList    = empty();
          TomList failureList   = empty();

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
            automataList = genTermListMatchingAutomata(termArgs,path,1,actionList,gsa);

            succesList = concat(succesList,declarationList);
            succesList = concat(succesList,assignementList);
          }
          succesList = concat(succesList,annotedAssignementList);
          succesList = concat(succesList,automataList);
          
          Expression cond = tom_make_EqualFunctionSymbol(subjectVariableAST,term) ;
          TomTerm test = tom_make_IfThenElse(cond,succesList,failureList) ;
          result = append(test,result);
          
          break matchBlock;
         } } }}matchlab_match19_pattern4: {

 
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
      
       { TomTerm tom_match20_1 = null; tom_match20_1 = (TomTerm) subjectListName;matchlab_match20_pattern1: { Option option = null; TomType termType = null; if(tom_is_fun_sym_Variable(tom_match20_1)) { Option tom_match20_1_1 = null; TomName tom_match20_1_2 = null; TomType tom_match20_1_3 = null; tom_match20_1_1 = (Option) tom_get_slot_Variable_option(tom_match20_1); tom_match20_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match20_1); tom_match20_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match20_1); option = (Option) tom_match20_1_1; termType = (TomType) tom_match20_1_3;
 
          variableListAST = tom_make_Variable(option(),tom_make_PositionName(pathList),termType) ;
         }} }
 
      result = append(tom_make_Declaration(variableListAST) ,result);
      result = append(tom_make_Assign(variableListAST,tom_make_TomTermToExpression(subjectListName)) ,result);

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
       { TomList tom_match21_1 = null; tom_match21_1 = (TomList) termList;matchlab_match21_pattern1: { if(tom_is_fun_sym_Empty(tom_match21_1)) {


 
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
              subList = append(tom_make_Action(actionList) ,subList);
            }
            Expression cond = tom_make_IsEmptyList(subjectListName) ;
            TomTerm test = tom_make_IfThenElse(cond,subList,empty()) ;
            result = append(test,result);
            break matchBlock;
          }
         }}matchlab_match21_pattern2: { TomList termTail = null; TomType termType = null; TomTerm var = null; Option option = null; if(tom_is_fun_sym_Cons(tom_match21_1)) { TomTerm tom_match21_1_1 = null; TomList tom_match21_1_2 = null; tom_match21_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match21_1); tom_match21_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match21_1); if(tom_is_fun_sym_Variable(tom_match21_1_1)) { Option tom_match21_1_1_1 = null; TomName tom_match21_1_1_2 = null; TomType tom_match21_1_1_3 = null; tom_match21_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match21_1_1); tom_match21_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match21_1_1); tom_match21_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match21_1_1); var = (TomTerm) tom_match21_1_1; option = (Option) tom_match21_1_1_1; termType = (TomType) tom_match21_1_1_3; termTail = (TomList) tom_match21_1_2;


 
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
            assignementList = append(tom_make_Assign(var,tom_make_GetHead(subjectListName)) ,assignementList);
            assignementList = append(tom_make_Assign(subjectListName,tom_make_GetTail(subjectListName)) ,assignementList);
            
            if(generateSemanticAction) {
              subList = append(tom_make_Action(actionList) ,subList);
            }
            
            Expression cond = tom_make_IsEmptyList(subjectListName) ;
            TomTerm test = tom_make_IfThenElse(cond,subList,empty()) ;
            TomList succesList = append(test,concat(declarationList,assignementList));
            
            cond = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = append(test,result);
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
            assignementList = append(tom_make_Assign(var,tom_make_GetHead(subjectListName)) ,assignementList);
            assignementList = append(tom_make_Assign(subjectListName,tom_make_GetTail(subjectListName)) ,assignementList);
            
            TomList succesList = concat(concat(declarationList,assignementList),subList);
            Expression cond = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            TomTerm test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = append(test,result);
            break matchBlock;
          }
         } }}matchlab_match21_pattern3: { TomList termTail = null; Option option = null; if(tom_is_fun_sym_Cons(tom_match21_1)) { TomTerm tom_match21_1_1 = null; TomList tom_match21_1_2 = null; tom_match21_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match21_1); tom_match21_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match21_1); if(tom_is_fun_sym_UnamedVariable(tom_match21_1_1)) { Option tom_match21_1_1_1 = null; TomType tom_match21_1_1_2 = null; tom_match21_1_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match21_1_1); tom_match21_1_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match21_1_1); option = (Option) tom_match21_1_1_1; termTail = (TomList) tom_match21_1_2;


 
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
         } }}matchlab_match21_pattern4: { TomList termTail = null; TomType termType = null; TomTerm var = null; Option option = null; if(tom_is_fun_sym_Cons(tom_match21_1)) { TomTerm tom_match21_1_1 = null; TomList tom_match21_1_2 = null; tom_match21_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match21_1); tom_match21_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match21_1); if(tom_is_fun_sym_VariableStar(tom_match21_1_1)) { Option tom_match21_1_1_1 = null; TomName tom_match21_1_1_2 = null; TomType tom_match21_1_1_3 = null; tom_match21_1_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match21_1_1); tom_match21_1_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match21_1_1); tom_match21_1_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match21_1_1); var = (TomTerm) tom_match21_1_1; option = (Option) tom_match21_1_1_1; termType = (TomType) tom_match21_1_1_3; termTail = (TomList) tom_match21_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = subjectList;
               * ...
               */
            
            if(generateSemanticAction) {
              subList = append(tom_make_Action(actionList) ,subList);
            }
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            TomTerm assignement = tom_make_Assign(var,tom_make_TomTermToExpression(subjectListName)) ;
            result = concat(append(assignement,result),subList);
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
            assignementList = append(tom_make_Assign(variableBeginAST,tom_make_TomTermToExpression(subjectListName)) ,assignementList);
            assignementList = append(tom_make_Assign(variableEndAST,tom_make_TomTermToExpression(subjectListName)) ,assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            TomList doList = empty();
            doList = append(tom_make_Assign(var,tom_make_GetSliceList(symbol.getAstName(),variableBeginAST,variableEndAST)) ,doList);
            
            doList = concat(doList,subList);

            Expression cond1 = tom_make_Not(tom_make_IsEmptyList(variableEndAST)) ;
            TomTerm test1 = tom_make_IfThenElse(cond1,cons(tom_make_Assign(variableEndAST,tom_make_GetTail(variableEndAST)), empty()),empty()) ;
            doList = append(test1,doList);
            doList = append(tom_make_Assign(subjectListName,tom_make_TomTermToExpression(variableEndAST)) ,doList);
            
            Expression cond2 = tom_make_Not(tom_make_IsEmptyList(subjectListName)) ;
            TomTerm doWhile = tom_make_DoWhile(doList,cond2) ;
            
            result = append(doWhile,concat(concat(declarationList,result),assignementList));

            break matchBlock;

          }

         } }}matchlab_match21_pattern5: {

 
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
         { TomTerm tom_match22_1 = null; tom_match22_1 = (TomTerm) subjectListName;matchlab_match22_pattern1: { Option option = null; TomType termType = null; if(tom_is_fun_sym_Variable(tom_match22_1)) { Option tom_match22_1_1 = null; TomName tom_match22_1_2 = null; TomType tom_match22_1_3 = null; tom_match22_1_1 = (Option) tom_get_slot_Variable_option(tom_match22_1); tom_match22_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match22_1); tom_match22_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match22_1); option = (Option) tom_match22_1_1; termType = (TomType) tom_match22_1_3;
 
            variableListAST = tom_make_Variable(option(),tom_make_PositionName(pathList),termType) ;
              // TODO: other termType
            variableIndexAST = tom_make_Variable(option(),tom_make_PositionName(pathIndex),getIntType()) ;
            break matchBlock;
           }}matchlab_match22_pattern2: {
 
            System.out.println("GenArrayMatchingAutomata strange subjectListName: " + subjectListName);
            System.exit(1);
          } }
 
      }
      result = append(tom_make_Declaration(variableListAST) ,result);
      result = append(tom_make_Declaration(variableIndexAST) ,result);
      result = append(tom_make_Assign(variableListAST,tom_make_TomTermToExpression(subjectListName)) ,result);
      result = append(tom_make_Assign(variableIndexAST,glZero) ,result);

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
       { TomList tom_match23_1 = null; tom_match23_1 = (TomList) termList;matchlab_match23_pattern1: { TomType termType = null; Option option = null; TomTerm var = null; TomList termTail = null; if(tom_is_fun_sym_Cons(tom_match23_1)) { TomTerm tom_match23_1_1 = null; TomList tom_match23_1_2 = null; tom_match23_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match23_1); tom_match23_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match23_1); if(tom_is_fun_sym_Variable(tom_match23_1_1)) { Option tom_match23_1_1_1 = null; TomName tom_match23_1_1_2 = null; TomType tom_match23_1_1_3 = null; tom_match23_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match23_1_1); tom_match23_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match23_1_1); tom_match23_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match23_1_1); var = (TomTerm) tom_match23_1_1; option = (Option) tom_match23_1_1_1; termType = (TomType) tom_match23_1_1_3; termTail = (TomList) tom_match23_1_2;


 
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
            assignementList = append(tom_make_Assign(var,tom_make_GetElement(subjectListName,subjectListIndex)) ,assignementList);
            assignementList = append(tom_make_Increment(subjectListIndex) ,assignementList);
            
            if(generateSemanticAction) {
              subList = append(tom_make_Action(actionList) ,subList);
            }
            
            Expression cond = tom_make_IsEmptyArray(subjectListName,subjectListIndex) ;
            TomTerm test = tom_make_IfThenElse(cond,subList,empty()) ;
            TomList succesList = append(test,concat(declarationList,assignementList));
            
            cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            test = tom_make_IfThenElse(cond,succesList,empty()) ;
            result = append(test,result);
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
            
            assignementList = append(tom_make_Assign(var,tom_make_GetElement(subjectListName,subjectListIndex)) ,assignementList);
            assignementList = append(tom_make_Increment(subjectListIndex) ,assignementList);
            
            succesList = concat(concat(concat(succesList,declarationList),assignementList),subList);
            
            Expression cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            TomTerm test = tom_make_IfThenElse(cond,succesList,empty()) ;
            
            result = append(test,result);
            break matchBlock;
          }
         } }}matchlab_match23_pattern2: { TomList termTail = null; Option option = null; if(tom_is_fun_sym_Cons(tom_match23_1)) { TomTerm tom_match23_1_1 = null; TomList tom_match23_1_2 = null; tom_match23_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match23_1); tom_match23_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match23_1); if(tom_is_fun_sym_UnamedVariable(tom_match23_1_1)) { Option tom_match23_1_1_1 = null; TomType tom_match23_1_1_2 = null; tom_match23_1_1_1 = (Option) tom_get_slot_UnamedVariable_option(tom_match23_1_1); tom_match23_1_1_2 = (TomType) tom_get_slot_UnamedVariable_astType(tom_match23_1_1); option = (Option) tom_match23_1_1_1; termTail = (TomList) tom_match23_1_2;


 
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
         } }}matchlab_match23_pattern3: { Option option = null; TomList termTail = null; TomTerm var = null; TomType termType = null; if(tom_is_fun_sym_Cons(tom_match23_1)) { TomTerm tom_match23_1_1 = null; TomList tom_match23_1_2 = null; tom_match23_1_1 = (TomTerm) tom_get_slot_Cons_head(tom_match23_1); tom_match23_1_2 = (TomList) tom_get_slot_Cons_tail(tom_match23_1); if(tom_is_fun_sym_VariableStar(tom_match23_1_1)) { Option tom_match23_1_1_1 = null; TomName tom_match23_1_1_2 = null; TomType tom_match23_1_1_3 = null; tom_match23_1_1_1 = (Option) tom_get_slot_VariableStar_option(tom_match23_1_1); tom_match23_1_1_2 = (TomName) tom_get_slot_VariableStar_astName(tom_match23_1_1); tom_match23_1_1_3 = (TomType) tom_get_slot_VariableStar_astType(tom_match23_1_1); var = (TomTerm) tom_match23_1_1; option = (Option) tom_match23_1_1_1; termType = (TomType) tom_match23_1_1_3; termTail = (TomList) tom_match23_1_2;


 
          if(termTail.isEmpty()) {
              /*
               * generate:
               * ---------
               * E_n = GET_SLICE_L(subjectList,subjectIndex,GET_SIZE_L(subjectList));
               * ...
               */
            
            if(generateSemanticAction) {
              subList = append(tom_make_Action(actionList) ,subList);
            }
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            
            TomTerm assignement = tom_make_Assign(var,tom_make_GetSliceArray(symbol.getAstName(),subjectListName,subjectListIndex,tom_make_ExpressionToTomTerm(tom_make_GetSize(subjectListName))))



 ;
            result = concat(append(assignement,result),subList);
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
            assignementList = append(tom_make_Assign(variableBeginAST,tom_make_TomTermToExpression(subjectListIndex)) ,assignementList);
            assignementList = append(tom_make_Assign(variableEndAST,tom_make_TomTermToExpression(subjectListIndex)) ,assignementList);
            
            TomList path = append(makeNumber(indexTerm),oldPath);
            TomTerm variableAST = tom_make_Variable(option(),tom_make_PositionName(path),termType) ;
            TomList doList = empty();
            doList = append(tom_make_Assign(var,tom_make_GetSliceArray(symbol.getAstName(),subjectListName,variableBeginAST,variableEndAST))

 ,doList);
            doList = concat(doList,subList);
            doList = append(tom_make_Increment(variableEndAST) ,doList);
            doList = append(tom_make_Assign(subjectListIndex,tom_make_TomTermToExpression(variableEndAST)) ,doList); 
            
            Expression cond = tom_make_Not(tom_make_IsEmptyArray(subjectListName,subjectListIndex)) ;
            TomTerm doWhile = tom_make_DoWhile(doList,cond) ;
            
            TomList tmpResult = empty();
            if(Flags.supportedBlock) {
              tmpResult = append(tom_make_OpenBlock() ,tmpResult);
            }
            tmpResult = concat(tmpResult,declarationList);
            tmpResult = concat(tmpResult,result);
            tmpResult = concat(tmpResult,assignementList);
            tmpResult = append(doWhile,tmpResult);
            if(Flags.supportedBlock) {
              tmpResult = append(tom_make_CloseBlock() ,tmpResult);
            }
            result = tmpResult;
            break matchBlock;
          }
         } }}matchlab_match23_pattern4: {

 
          System.out.println("GenArrayMatchingAutomata strange termList: " + termList);
          System.exit(1);
        } }
 
    } // end matchBlock
    return result;
  }
  
} // end of class
  
                  
    
