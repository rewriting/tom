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

package jtom.backend;
  
import java.util.*;
import java.io.*;

import aterm.*;
import aterm.pure.*;

import jtom.*;
import jtom.runtime.*;
import jtom.tools.*;
import jtom.adt.*;

public class TomOptimizer extends TomBase {

  private int numberCompiledMatchFound = 0;
  private int numberCompiledPatternFound = 0;
  private int numberVarFound = 0;
  private int numberVarRemoved = 0;

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
  }
  
  // ------------------------------------------------------------

                                                                                                                                                                         

  // ------------------------------------------------------------
    
  private final boolean debug = true;

  private void optimDebug(String s) {
    if (debug)
      System.out.println(s);
  }

  private void exitWithMesg(String mesg) {
    System.out.println(mesg);
    System.exit(1);
  }

  public TomTerm optimize(TomTerm subject) {
    TomTerm optimizedTerm;
    optimizedTerm = optimize_pass_1(subject);
    System.out.println("numberCompiledMatchFound :"+numberCompiledMatchFound);
    System.out.println("numberCompiledPatternFound :"+numberCompiledPatternFound);
    System.out.println("numberVarFound :"+numberVarFound);
    System.out.println("numberVarRemoved :"+numberVarRemoved);
    return optimizedTerm;
  }

  /* 
   * optimize_pass_1 : remove useless intermediate variables from a CompiledMatch.
   */

  private TomTerm optimize_pass_1(TomTerm subject) {
    return replaceCompiledMatch(subject);
  }


  private TomList declVarList; // one for each CompiledMatch
  private TomList avList; // avList = AssignedVariableList; one for each CompiledPattern

  private TomTerm replaceCompiledMatch(TomTerm subject) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match1_1 = null; tom_match1_1 = ( TomTerm) t;matchlab_match1_pattern1: {  TomList decls = null;  Option option = null;  TomList automata = null; if(tom_is_fun_sym_CompiledMatch(tom_match1_1)) {  TomList tom_match1_1_1 = null;  TomList tom_match1_1_2 = null;  Option tom_match1_1_3 = null; tom_match1_1_1 = ( TomList) tom_get_slot_CompiledMatch_decls(tom_match1_1); tom_match1_1_2 = ( TomList) tom_get_slot_CompiledMatch_automataList(tom_match1_1); tom_match1_1_3 = ( Option) tom_get_slot_CompiledMatch_option(tom_match1_1); decls = ( TomList) tom_match1_1_1; automata = ( TomList) tom_match1_1_2; option = ( Option) tom_match1_1_3;
 
		numberCompiledMatchFound++;
		declVarList = traversalCollectDecls(decls);
		TomList newAutomata = collectNReplace(automata);
		//optimDebug("declVarList: \n"+declVarList.toString());
		TomList newDecls = replaceDeclList(decls);
		return tom_make_CompiledMatch(newDecls,newAutomata,option) ;
	       }}matchlab_match1_pattern2: {
 
		return traversal().genericTraversal(t,this);
	      } }
  // match
	  } else
	    return traversal().genericTraversal(t,this);
	} //apply
      };
    return (TomTerm) replace.apply(subject);
  }

  private TomList traversalCollectDecls(TomList declsList) {
    if(declsList.isEmpty()) {
      return empty();
    }
    TomTerm t = declsList.getHead();
    TomList l = declsList.getTail();
     {  TomTerm tom_match2_1 = null; tom_match2_1 = ( TomTerm) t;matchlab_match2_pattern1: {  TomList l1 = null;  Expression source = null; if(tom_is_fun_sym_InstructionToTomTerm(tom_match2_1)) {  Instruction tom_match2_1_1 = null; tom_match2_1_1 = ( Instruction) tom_get_slot_InstructionToTomTerm_astInstruction(tom_match2_1); if(tom_is_fun_sym_Assign(tom_match2_1_1)) {  TomTerm tom_match2_1_1_1 = null;  Expression tom_match2_1_1_2 = null; tom_match2_1_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match2_1_1); tom_match2_1_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match2_1_1); if(tom_is_fun_sym_Variable(tom_match2_1_1_1)) {  Option tom_match2_1_1_1_1 = null;  TomName tom_match2_1_1_1_2 = null;  TomType tom_match2_1_1_1_3 = null; tom_match2_1_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match2_1_1_1); tom_match2_1_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match2_1_1_1); tom_match2_1_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match2_1_1_1); if(tom_is_fun_sym_PositionName(tom_match2_1_1_1_2)) {  TomList tom_match2_1_1_1_2_1 = null; tom_match2_1_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match2_1_1_1_2); l1 = ( TomList) tom_match2_1_1_1_2_1; source = ( Expression) tom_match2_1_1_2;
 
	String name = "tom"+numberListToIdentifier(l1);
	Integer zero = new Integer(0);
	//optimDebug("Var "+name+" found in decls");
	numberVarFound++;
	return cons(tom_make_AssignedVariable(name,source,zero) ,
		    traversalCollectDecls(l));
       } } } }}matchlab_match2_pattern2: {
  
	return traversalCollectDecls(l);
      } }
 
  }

  private TomList updateDeclVarList(TomList varList) {
    if(varList.isEmpty()) {
      return empty();
    }
    TomTerm t = varList.getHead();
    TomList l = varList.getTail();
    
    TomTerm av;
    TomList list;
    list = avList;
    while(!list.isEmpty()) {
      av = list.getHead();
       {  TomTerm tom_match3_1 = null;  TomTerm tom_match3_2 = null; tom_match3_1 = ( TomTerm) t; tom_match3_2 = ( TomTerm) av;matchlab_match3_pattern1: {  String name = null;  String tom_renamedvar_name_1 = null; if(tom_is_fun_sym_AssignedVariable(tom_match3_1)) {  String tom_match3_1_1 = null;  Expression tom_match3_1_2 = null;  Integer tom_match3_1_3 = null; tom_match3_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match3_1); tom_match3_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match3_1); tom_match3_1_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match3_1); tom_renamedvar_name_1 = ( String) tom_match3_1_1; if(tom_is_fun_sym_AssignedVariable(tom_match3_2)) {  String tom_match3_2_1 = null;  Expression tom_match3_2_2 = null;  Integer tom_match3_2_3 = null; tom_match3_2_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match3_2); tom_match3_2_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match3_2); tom_match3_2_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match3_2); name = ( String) tom_match3_2_1; if(tom_terms_equal_String(name, tom_renamedvar_name_1) &&  true ) {
 
	  return cons(av,updateDeclVarList(l));
	 } } }} }
 
      list = list.getTail();
    }
    exitWithMesg("We should be there");
    return null;
  }

  private TomList collectNReplace(TomList automata) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match4_1 = null; tom_match4_1 = ( TomTerm) t;matchlab_match4_pattern1: {  TomList instList = null; if(tom_is_fun_sym_CompiledPattern(tom_match4_1)) {  TomList tom_match4_1_1 = null; tom_match4_1_1 = ( TomList) tom_get_slot_CompiledPattern_instList(tom_match4_1); instList = ( TomList) tom_match4_1_1;
 
		//optimDebug("Entering CP");
		numberCompiledPatternFound++;
		avList = declVarList;
		collectCompiledPattern(instList); // fill varList with all the variable in the CP.
		//optimDebug("Collecting CP completed :");
		//optimDebug(avList.toString());
		TomList newInstList = replaceCompiledPattern(instList);
		declVarList = updateDeclVarList(declVarList);
		return tom_make_CompiledPattern(newInstList) ;
	       }}matchlab_match4_pattern2: {
 
		return traversal().genericTraversal(t,this);
	      } }
 
	  } else
	    return traversal().genericTraversal(t,this);
	}
      };
    //optimDebug(automata.toString());
    return  (TomList) traversal().genericTraversal(automata,replace);
  }

  private void collectCompiledPattern(TomList subject) {
     Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match5_1 = null; tom_match5_1 = ( TomTerm) t;matchlab_match5_pattern1: {  TomList l1 = null; if(tom_is_fun_sym_Variable(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomType tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match5_1); if(tom_is_fun_sym_PositionName(tom_match5_1_2)) {  TomList tom_match5_1_2_1 = null; tom_match5_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match5_1_2); l1 = ( TomList) tom_match5_1_2_1;
 
		avList = addOneUse("tom"+numberListToIdentifier(l1));
		return false; // no need to go deeper in a Variable
	       } }}matchlab_match5_pattern2: { if(tom_is_fun_sym_CompiledMatch(tom_match5_1)) {  TomList tom_match5_1_1 = null;  TomList tom_match5_1_2 = null;  Option tom_match5_1_3 = null; tom_match5_1_1 = ( TomList) tom_get_slot_CompiledMatch_decls(tom_match5_1); tom_match5_1_2 = ( TomList) tom_get_slot_CompiledMatch_automataList(tom_match5_1); tom_match5_1_3 = ( Option) tom_get_slot_CompiledMatch_option(tom_match5_1);
 
		// we will inspect this CompiledMatch when replacing the CompiledMatch parent.
		return false;
	       }}matchlab_match5_pattern3: { if(tom_is_fun_sym_Declaration(tom_match5_1)) {  TomTerm tom_match5_1_1 = null; tom_match5_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match5_1);
 
		return false;
	       }}matchlab_match5_pattern4: {
 return true;} }
 
	  } else if (t instanceof Instruction) {
	     {  Instruction tom_match6_1 = null; tom_match6_1 = ( Instruction) t;matchlab_match6_pattern1: {  Expression source = null;  TomList l1 = null; if(tom_is_fun_sym_Assign(tom_match6_1)) {  TomTerm tom_match6_1_1 = null;  Expression tom_match6_1_2 = null; tom_match6_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match6_1); tom_match6_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match6_1); if(tom_is_fun_sym_Variable(tom_match6_1_1)) {  Option tom_match6_1_1_1 = null;  TomName tom_match6_1_1_2 = null;  TomType tom_match6_1_1_3 = null; tom_match6_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match6_1_1); tom_match6_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match6_1_1); tom_match6_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match6_1_1); if(tom_is_fun_sym_PositionName(tom_match6_1_1_2)) {  TomList tom_match6_1_1_2_1 = null; tom_match6_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match6_1_1_2); l1 = ( TomList) tom_match6_1_1_2_1; source = ( Expression) tom_match6_1_2;
 
		String name = "tom"+numberListToIdentifier(l1);
		if (!isAssigned(name)) {
		  Integer minusOne = new Integer(-1);
		  //optimDebug("Var "+name+" found in CP");
		  avList = append(tom_make_AssignedVariable(name,source,minusOne) ,
				  avList);
		  numberVarFound++;
		} else {
		  avList = addOneUse(name);
		}	
		return true;// we go deeper to search in the 'source'	    
	       } } }}matchlab_match6_pattern2: {
 return true;} }
  //match
	  } else {
	    return true;
	  } //if
	} //apply
      };
    traversal().genericCollect(subject, collect);
  }

  private TomList replaceCompiledPattern(TomList subject) {
    //optimDebug("Entering replaceUselessVarAuto");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  //optimDebug("Starting apply on a :");
	  //printTerm(t);
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match7_1 = null; tom_match7_1 = ( TomTerm) t;matchlab_match7_pattern1: {  TomList l1 = null; if(tom_is_fun_sym_Declaration(tom_match7_1)) {  TomTerm tom_match7_1_1 = null; tom_match7_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match7_1); if(tom_is_fun_sym_Variable(tom_match7_1_1)) {  Option tom_match7_1_1_1 = null;  TomName tom_match7_1_1_2 = null;  TomType tom_match7_1_1_3 = null; tom_match7_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match7_1_1); tom_match7_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match7_1_1); tom_match7_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match7_1_1); if(tom_is_fun_sym_PositionName(tom_match7_1_1_2)) {  TomList tom_match7_1_1_2_1 = null; tom_match7_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match7_1_1_2); l1 = ( TomList) tom_match7_1_1_2_1;
 
		String name = "tom"+numberListToIdentifier(l1);
		//optimDebug("Declaration of "+name1+" found");
		if (getNbUse(name) <= 1)
		  return tom_make_Tom(empty()) ;
		else
		  return t;
	       } } }}matchlab_match7_pattern2: {  TomList l1 = null; if(tom_is_fun_sym_Variable(tom_match7_1)) {  Option tom_match7_1_1 = null;  TomName tom_match7_1_2 = null;  TomType tom_match7_1_3 = null; tom_match7_1_1 = ( Option) tom_get_slot_Variable_option(tom_match7_1); tom_match7_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match7_1); tom_match7_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match7_1); if(tom_is_fun_sym_PositionName(tom_match7_1_2)) {  TomList tom_match7_1_2_1 = null; tom_match7_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match7_1_2); l1 = ( TomList) tom_match7_1_2_1;
 
		String name = "tom"+numberListToIdentifier(l1); 
		if (getNbUse(name) == 1) { // the variable is found, so used at least one time
		  Expression expr = getSource(name);
		   {  Expression tom_match8_1 = null; tom_match8_1 = ( Expression) expr;matchlab_match8_pattern1: {  TomTerm tomTerm = null; if(tom_is_fun_sym_TomTermToExpression(tom_match8_1)) {  TomTerm tom_match8_1_1 = null; tom_match8_1_1 = ( TomTerm) tom_get_slot_TomTermToExpression_astTerm(tom_match8_1); tomTerm = ( TomTerm) tom_match8_1_1;
 
		      return tomTerm;
		     }}matchlab_match8_pattern2: {
 
		       return tom_make_ExpressionToTomTerm(expr) ;
		    } }
  // match
		} else
		  return t;
	       } }}matchlab_match7_pattern3: { if(tom_is_fun_sym_CompiledMatch(tom_match7_1)) {  TomList tom_match7_1_1 = null;  TomList tom_match7_1_2 = null;  Option tom_match7_1_3 = null; tom_match7_1_1 = ( TomList) tom_get_slot_CompiledMatch_decls(tom_match7_1); tom_match7_1_2 = ( TomList) tom_get_slot_CompiledMatch_automataList(tom_match7_1); tom_match7_1_3 = ( Option) tom_get_slot_CompiledMatch_option(tom_match7_1);
 
		// saving context
		TomList declVarListSav = declVarList;
		TomList avListSav = avList;
		// computing the new CompiledMatch
		TomTerm newCompiledMatch = replaceCompiledMatch((TomTerm) t);
		// restoring the context
		declVarList = declVarListSav;
		avList = avListSav;
		return newCompiledMatch;
	       }}matchlab_match7_pattern4: {
 
		return traversal().genericTraversal(t, this);
	      } }
  //match
	  } else if (t instanceof Instruction) {
	     {  Instruction tom_match9_1 = null; tom_match9_1 = ( Instruction) t;matchlab_match9_pattern1: {  Expression source = null;  TomTerm var = null;  TomList l1 = null; if(tom_is_fun_sym_Assign(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  Expression tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match9_1); if(tom_is_fun_sym_Variable(tom_match9_1_1)) {  Option tom_match9_1_1_1 = null;  TomName tom_match9_1_1_2 = null;  TomType tom_match9_1_1_3 = null; tom_match9_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match9_1_1); tom_match9_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match9_1_1); tom_match9_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match9_1_1); var = ( TomTerm) tom_match9_1_1; if(tom_is_fun_sym_PositionName(tom_match9_1_1_2)) {  TomList tom_match9_1_1_2_1 = null; tom_match9_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match9_1_1_2); l1 = ( TomList) tom_match9_1_1_2_1; source = ( Expression) tom_match9_1_2;
 	      
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) <= 1) {
		  numberVarRemoved++;
		  return tom_make_Action(empty()) ;
		} else {
		  Expression expr = (Expression) traversal().genericTraversal(source, this);
		  return tom_make_Assign(var,expr) ;
		}
	       } } }}matchlab_match9_pattern2: {  TomList l1 = null;  TomTerm var = null; if(tom_is_fun_sym_Assign(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  Expression tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match9_1); var = ( TomTerm) tom_match9_1_1; if(tom_is_fun_sym_TomTermToExpression(tom_match9_1_2)) {  TomTerm tom_match9_1_2_1 = null; tom_match9_1_2_1 = ( TomTerm) tom_get_slot_TomTermToExpression_astTerm(tom_match9_1_2); if(tom_is_fun_sym_Variable(tom_match9_1_2_1)) {  Option tom_match9_1_2_1_1 = null;  TomName tom_match9_1_2_1_2 = null;  TomType tom_match9_1_2_1_3 = null; tom_match9_1_2_1_1 = ( Option) tom_get_slot_Variable_option(tom_match9_1_2_1); tom_match9_1_2_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match9_1_2_1); tom_match9_1_2_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match9_1_2_1); if(tom_is_fun_sym_PositionName(tom_match9_1_2_1_2)) {  TomList tom_match9_1_2_1_2_1 = null; tom_match9_1_2_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match9_1_2_1_2); l1 = ( TomList) tom_match9_1_2_1_2_1;
      
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) <= 1) 
		  return tom_make_Assign(var,getSource(name)) ;
		else
		  return t;
	       } } } }}matchlab_match9_pattern3: {
 
		return traversal().genericTraversal(t, this);
	      } }
  //match
	  } 
	  /* ******************************** */
	  /* This part will be remove when the 
	   * EqualFunctionSymbol generation will be 
	   * extended. 
	   */
	  else if (t instanceof Expression) {
	     {  Expression tom_match10_1 = null; tom_match10_1 = ( Expression) t;matchlab_match10_pattern1: { if(tom_is_fun_sym_EqualFunctionSymbol(tom_match10_1)) {  TomTerm tom_match10_1_1 = null;  TomTerm tom_match10_1_2 = null; tom_match10_1_1 = ( TomTerm) tom_get_slot_EqualFunctionSymbol_kid1(tom_match10_1); tom_match10_1_2 = ( TomTerm) tom_get_slot_EqualFunctionSymbol_kid2(tom_match10_1);
 
		return t;
	       }}matchlab_match10_pattern2: {
 
		return traversal().genericTraversal(t, this);
	      } }
 
	  }
	  /* ******************************** */
	  else {
	    return  traversal().genericTraversal(t, this);
	  }
	} //apply
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private boolean isAssigned(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
       {  TomTerm tom_match11_1 = null; tom_match11_1 = ( TomTerm) t;matchlab_match11_pattern1: {  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match11_1)) {  String tom_match11_1_1 = null;  Expression tom_match11_1_2 = null;  Integer tom_match11_1_3 = null; tom_match11_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match11_1); tom_match11_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match11_1); tom_match11_1_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match11_1); name = ( String) tom_match11_1_1;
 
	  if (name.equals(varName))
	    return true;
	 }} }
 
      tmpList = tmpList.getTail();
    }
    return false;
  }

  private TomList addOneUse (final String name) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match12_1 = null; tom_match12_1 = ( TomTerm) t;matchlab_match12_pattern1: {  Integer nbUse = null;  Expression source = null;  String varName = null; if(tom_is_fun_sym_AssignedVariable(tom_match12_1)) {  String tom_match12_1_1 = null;  Expression tom_match12_1_2 = null;  Integer tom_match12_1_3 = null; tom_match12_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match12_1); tom_match12_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match12_1); tom_match12_1_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match12_1); varName = ( String) tom_match12_1_1; source = ( Expression) tom_match12_1_2; nbUse = ( Integer) tom_match12_1_3;
 
		if (varName.equals(name)) {
		  Integer newNbUse = new Integer(nbUse.intValue() + 1);
		  return tom_make_AssignedVariable(varName,source,newNbUse) ;
		} else
		  return t;
	       }}matchlab_match12_pattern2: {
 
		optimDebug("in rest of match: "+t.toString());
		exitWithMesg("We should find only AssignedVariable");
		return null;
	      } }
 
	  } else 
	    return traversal().genericTraversal(t,this);
	} //apply
      };
    //optimDebug("Adding one use to "+name);
    return (TomList) traversal().genericTraversal(avList, replace);
  }

  private int getNbUse(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
       {  TomTerm tom_match13_1 = null; tom_match13_1 = ( TomTerm) t;matchlab_match13_pattern1: {  String name = null;  Integer n = null; if(tom_is_fun_sym_AssignedVariable(tom_match13_1)) {  String tom_match13_1_1 = null;  Expression tom_match13_1_2 = null;  Integer tom_match13_1_3 = null; tom_match13_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match13_1); tom_match13_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match13_1); tom_match13_1_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match13_1); name = ( String) tom_match13_1_1; n = ( Integer) tom_match13_1_3;
 
	  if (name.equals(varName))
	    return n.intValue();
	 }} }
 
      tmpList = tmpList.getTail();
    }
    exitWithMesg(varName+" is not in avList");
    return -1;
  }

  private Expression getSource(String varName) {
    TomList tmpList = avList;
    TomTerm t;
    while (!tmpList.isEmpty()) {
      t = tmpList.getHead();
       {  TomTerm tom_match14_1 = null; tom_match14_1 = ( TomTerm) t;matchlab_match14_pattern1: {  Expression s = null;  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match14_1)) {  String tom_match14_1_1 = null;  Expression tom_match14_1_2 = null;  Integer tom_match14_1_3 = null; tom_match14_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match14_1); tom_match14_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match14_1); tom_match14_1_3 = ( Integer) tom_get_slot_AssignedVariable_nbUse(tom_match14_1); name = ( String) tom_match14_1_1; s = ( Expression) tom_match14_1_2;
 
	  if (name.equals(varName))
	    return s;
	 }} }
 
      tmpList = tmpList.getTail();
    }
    exitWithMesg(varName+" is not in avList");
    return null;
  }

  /*
   * The following method will be FULLY usefull
   * when the EqualFunctionSymbol generation will be extended.
   * We will extend it to the one-time-used variables.
   */
  private TomList replaceDeclList(TomList declList) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match15_1 = null; tom_match15_1 = ( TomTerm) t;matchlab_match15_pattern1: {  TomList l1 = null; if(tom_is_fun_sym_Declaration(tom_match15_1)) {  TomTerm tom_match15_1_1 = null; tom_match15_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match15_1); if(tom_is_fun_sym_Variable(tom_match15_1_1)) {  Option tom_match15_1_1_1 = null;  TomName tom_match15_1_1_2 = null;  TomType tom_match15_1_1_3 = null; tom_match15_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match15_1_1); tom_match15_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match15_1_1); tom_match15_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match15_1_1); if(tom_is_fun_sym_PositionName(tom_match15_1_1_2)) {  TomList tom_match15_1_1_2_1 = null; tom_match15_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match15_1_1_2); l1 = ( TomList) tom_match15_1_1_2_1;
 
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) == 0) //  <= 1 if no problem with code generation
		  return tom_make_Tom(empty()) ;
		else
		  return t;
	       } } }}matchlab_match15_pattern2: {
 
		return traversal().genericTraversal(t, this);
	      } }
 
	  } else if (t instanceof Instruction) {
	     {  Instruction tom_match16_1 = null; tom_match16_1 = ( Instruction) t;matchlab_match16_pattern1: {  Expression source = null;  TomList l1 = null; if(tom_is_fun_sym_Assign(tom_match16_1)) {  TomTerm tom_match16_1_1 = null;  Expression tom_match16_1_2 = null; tom_match16_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match16_1); tom_match16_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match16_1); if(tom_is_fun_sym_Variable(tom_match16_1_1)) {  Option tom_match16_1_1_1 = null;  TomName tom_match16_1_1_2 = null;  TomType tom_match16_1_1_3 = null; tom_match16_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match16_1_1); tom_match16_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match16_1_1); tom_match16_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match16_1_1); if(tom_is_fun_sym_PositionName(tom_match16_1_1_2)) {  TomList tom_match16_1_1_2_1 = null; tom_match16_1_1_2_1 = ( TomList) tom_get_slot_PositionName_numberList(tom_match16_1_1_2); l1 = ( TomList) tom_match16_1_1_2_1; source = ( Expression) tom_match16_1_2;
 
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) == 0) { // <= 1 if no problem with code generation
		  numberVarRemoved++;
		  return tom_make_Action(empty()) ;
		}
		else
		  return t;
	       } } }}matchlab_match16_pattern2: {
 
		return traversal().genericTraversal(t, this);
	      } }
 
	  } else
	    return traversal().genericTraversal(t, this);
	} // apply
      };//replace      
    return (TomList) traversal().genericTraversal(declList,replace);
  }
      
  
} //Class TomOptimizer
