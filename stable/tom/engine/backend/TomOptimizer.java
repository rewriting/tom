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
  
import jtom.TomBase;
import jtom.adt.Expression;
import jtom.adt.Instruction;
import jtom.adt.Option;
import jtom.adt.TomList;
import jtom.adt.TomName;
import jtom.adt.TomNumberList;
import jtom.adt.TomTerm;
import jtom.adt.TomType;
import jtom.runtime.Collect1;
import jtom.runtime.Replace1;
import jtom.tools.TomTask;
import jtom.tools.TomTaskInput;
import jtom.tools.Tools;
import aterm.ATerm;

public class TomOptimizer extends TomBase implements TomTask {

  private TomTask nextTask;
  private int numberCompiledMatchFound = 0;
  private int numberCompiledPatternFound = 0;
  private int numberVarFound = 0;
  private int numberVarRemoved = 0;

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
  }
  
  // ------------------------------------------------------------
    
  // ------------------------------------------------------------
    
 public void addTask(TomTask task) {
  	this.nextTask = task;
  }
  public void process(TomTaskInput input) {
    try {
	  long startChrono = 0;
	  boolean verbose = input.isVerbose(), intermediate = input.isIntermediate();
	  if(verbose) {
		startChrono = System.currentTimeMillis();
	  }
	  TomTerm optimizedTerm = optimize(input.getTerm());
	  if(verbose) {
		System.out.println("TOM optimization phase (" + (System.currentTimeMillis()-startChrono)+ " ms)");
	  } 
      if(intermediate) {
          Tools.generateOutput(input.baseInputFileName + input.optimizedSuffix, optimizedTerm);
	  }
	  input.setTerm(optimizedTerm);
    } catch (Exception e) {
      e.printStackTrace();
      return;
    }
    if(nextTask != null) {
      nextTask.process(input);
    }
  }
  
  public TomTask getTask() {
  	return nextTask;
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
  private boolean insideDoWhile = false; //true when collecting inside a DoWhile

  private TomTerm replaceCompiledMatch(TomTerm subject) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match1_1 = null; tom_match1_1 = ( TomTerm) t;matchlab_match1_pattern1: {  TomList automata = null;  TomList decls = null;  Option option = null; if(tom_is_fun_sym_CompiledMatch(tom_match1_1)) {  TomList tom_match1_1_1 = null;  TomList tom_match1_1_2 = null;  Option tom_match1_1_3 = null; tom_match1_1_1 = ( TomList) tom_get_slot_CompiledMatch_decls(tom_match1_1); tom_match1_1_2 = ( TomList) tom_get_slot_CompiledMatch_automataList(tom_match1_1); tom_match1_1_3 = ( Option) tom_get_slot_CompiledMatch_option(tom_match1_1); decls = ( TomList) tom_match1_1_1; automata = ( TomList) tom_match1_1_2; option = ( Option) tom_match1_1_3;
 
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
     {  TomTerm tom_match2_1 = null; tom_match2_1 = ( TomTerm) t;matchlab_match2_pattern1: {  Expression source = null;  TomNumberList l1 = null; if(tom_is_fun_sym_InstructionToTomTerm(tom_match2_1)) {  Instruction tom_match2_1_1 = null; tom_match2_1_1 = ( Instruction) tom_get_slot_InstructionToTomTerm_astInstruction(tom_match2_1); if(tom_is_fun_sym_AssignMatchSubject(tom_match2_1_1)) {  TomTerm tom_match2_1_1_1 = null;  Expression tom_match2_1_1_2 = null; tom_match2_1_1_1 = ( TomTerm) tom_get_slot_AssignMatchSubject_kid1(tom_match2_1_1); tom_match2_1_1_2 = ( Expression) tom_get_slot_AssignMatchSubject_source(tom_match2_1_1); if(tom_is_fun_sym_Variable(tom_match2_1_1_1)) {  Option tom_match2_1_1_1_1 = null;  TomName tom_match2_1_1_1_2 = null;  TomType tom_match2_1_1_1_3 = null; tom_match2_1_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match2_1_1_1); tom_match2_1_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match2_1_1_1); tom_match2_1_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match2_1_1_1); if(tom_is_fun_sym_PositionName(tom_match2_1_1_1_2)) {  TomNumberList tom_match2_1_1_1_2_1 = null; tom_match2_1_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match2_1_1_1_2); l1 = ( TomNumberList) tom_match2_1_1_1_2_1; source = ( Expression) tom_match2_1_1_2;
 
	String name = "tom"+numberListToIdentifier(l1);
	numberVarFound++;
	return cons(tom_make_AssignedVariable(name,source, 0,tom_make_FalseTL(),tom_make_TrueTL()) ,
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
       {  TomTerm tom_match3_1 = null;  TomTerm tom_match3_2 = null; tom_match3_1 = ( TomTerm) t; tom_match3_2 = ( TomTerm) av;matchlab_match3_pattern1: {  String tom_renamedvar_name_1 = null;  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match3_1)) {  String tom_match3_1_1 = null;  Expression tom_match3_1_2 = null;  int tom_match3_1_3;  Expression tom_match3_1_4 = null;  Expression tom_match3_1_5 = null; tom_match3_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match3_1); tom_match3_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match3_1); tom_match3_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match3_1); tom_match3_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match3_1); tom_match3_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match3_1); tom_renamedvar_name_1 = ( String) tom_match3_1_1; if(tom_is_fun_sym_AssignedVariable(tom_match3_2)) {  String tom_match3_2_1 = null;  Expression tom_match3_2_2 = null;  int tom_match3_2_3;  Expression tom_match3_2_4 = null;  Expression tom_match3_2_5 = null; tom_match3_2_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match3_2); tom_match3_2_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match3_2); tom_match3_2_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match3_2); tom_match3_2_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match3_2); tom_match3_2_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match3_2); name = ( String) tom_match3_2_1; if(tom_terms_equal_String(name, tom_renamedvar_name_1) &&  true ) {
 
	  return cons(av,updateDeclVarList(l));
	 } } }} }
 
      list = list.getTail();
    }
    exitWithMesg("We should not be there");
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
		collectVariables(instList); // fill varList with all the variable in the CP.
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

  private void collectVariables(TomList subject) {
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match5_1 = null; tom_match5_1 = ( TomTerm) t;matchlab_match5_pattern1: {  TomNumberList l1 = null; if(tom_is_fun_sym_Variable(tom_match5_1)) {  Option tom_match5_1_1 = null;  TomName tom_match5_1_2 = null;  TomType tom_match5_1_3 = null; tom_match5_1_1 = ( Option) tom_get_slot_Variable_option(tom_match5_1); tom_match5_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match5_1); tom_match5_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match5_1); if(tom_is_fun_sym_PositionName(tom_match5_1_2)) {  TomNumberList tom_match5_1_2_1 = null; tom_match5_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match5_1_2); l1 = ( TomNumberList) tom_match5_1_2_1;
 
		avList = addOneUse("tom"+numberListToIdentifier(l1),false);
		return false; // no need to go deeper in a Variable
	       } }}matchlab_match5_pattern2: { if(tom_is_fun_sym_CompiledMatch(tom_match5_1)) {  TomList tom_match5_1_1 = null;  TomList tom_match5_1_2 = null;  Option tom_match5_1_3 = null; tom_match5_1_1 = ( TomList) tom_get_slot_CompiledMatch_decls(tom_match5_1); tom_match5_1_2 = ( TomList) tom_get_slot_CompiledMatch_automataList(tom_match5_1); tom_match5_1_3 = ( Option) tom_get_slot_CompiledMatch_option(tom_match5_1);
 
		// we will inspect this CompiledMatch when replacing the CompiledMatch parent.
		return false;
	       }}matchlab_match5_pattern3: { if(tom_is_fun_sym_Declaration(tom_match5_1)) {  TomTerm tom_match5_1_1 = null; tom_match5_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match5_1);
 
		return false;
	       }}matchlab_match5_pattern4: {
 return true;} }
 
	  } else if (t instanceof Instruction) {
	     {  Instruction tom_match6_1 = null; tom_match6_1 = ( Instruction) t;matchlab_match6_pattern1: {  Expression source = null;  TomNumberList l1 = null; if(tom_is_fun_sym_Assign(tom_match6_1)) {  TomTerm tom_match6_1_1 = null;  Expression tom_match6_1_2 = null; tom_match6_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match6_1); tom_match6_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match6_1); if(tom_is_fun_sym_Variable(tom_match6_1_1)) {  Option tom_match6_1_1_1 = null;  TomName tom_match6_1_1_2 = null;  TomType tom_match6_1_1_3 = null; tom_match6_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match6_1_1); tom_match6_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match6_1_1); tom_match6_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match6_1_1); if(tom_is_fun_sym_PositionName(tom_match6_1_1_2)) {  TomNumberList tom_match6_1_1_2_1 = null; tom_match6_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match6_1_1_2); l1 = ( TomNumberList) tom_match6_1_1_2_1; source = ( Expression) tom_match6_1_2;
 
		String name = "tom"+numberListToIdentifier(l1);
		if (!isAssigned(name)) {
		  int minusOne = -1;
		  //optimDebug("Var "+name+" found in CP");
		  avList = append(tom_make_AssignedVariable(name,source,minusOne,tom_make_FalseTL(),tom_make_TrueTL()) ,
		  avList);
		  numberVarFound++;
		} else {
		  avList = addOneUse(name,true);
		}	
		return true;// we go deeper to search in the 'source'	    
	       } } }}matchlab_match6_pattern2: {  TomList list = null; if(tom_is_fun_sym_DoWhile(tom_match6_1)) {  TomList tom_match6_1_1 = null;  Expression tom_match6_1_2 = null; tom_match6_1_1 = ( TomList) tom_get_slot_DoWhile_instList(tom_match6_1); tom_match6_1_2 = ( Expression) tom_get_slot_DoWhile_condition(tom_match6_1); list = ( TomList) tom_match6_1_1;
 
		boolean savedInsideDoWhile = insideDoWhile;
		insideDoWhile = true;
		//optimDebug("Entering DoWhile");
		collectVariables(list);
		//optimDebug("Exiting DoWhile");
		insideDoWhile = savedInsideDoWhile;
		return false; // no need to go deeper, it has just been inspected
	       }}matchlab_match6_pattern3: {
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
	     {  TomTerm tom_match7_1 = null; tom_match7_1 = ( TomTerm) t;matchlab_match7_pattern1: {  TomNumberList l1 = null; if(tom_is_fun_sym_Declaration(tom_match7_1)) {  TomTerm tom_match7_1_1 = null; tom_match7_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match7_1); if(tom_is_fun_sym_Variable(tom_match7_1_1)) {  Option tom_match7_1_1_1 = null;  TomName tom_match7_1_1_2 = null;  TomType tom_match7_1_1_3 = null; tom_match7_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match7_1_1); tom_match7_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match7_1_1); tom_match7_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match7_1_1); if(tom_is_fun_sym_PositionName(tom_match7_1_1_2)) {  TomNumberList tom_match7_1_1_2_1 = null; tom_match7_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match7_1_1_2); l1 = ( TomNumberList) tom_match7_1_1_2_1;
 
		String name = "tom"+numberListToIdentifier(l1);
		//optimDebug("Declaration of "+name+" found");
		if (getNbUse(name) <= 1)
		  return tom_make_Tom(empty()) ;
		else
		  return t;
	       } } }}matchlab_match7_pattern2: {  TomNumberList l1 = null; if(tom_is_fun_sym_Variable(tom_match7_1)) {  Option tom_match7_1_1 = null;  TomName tom_match7_1_2 = null;  TomType tom_match7_1_3 = null; tom_match7_1_1 = ( Option) tom_get_slot_Variable_option(tom_match7_1); tom_match7_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match7_1); tom_match7_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match7_1); if(tom_is_fun_sym_PositionName(tom_match7_1_2)) {  TomNumberList tom_match7_1_2_1 = null; tom_match7_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match7_1_2); l1 = ( TomNumberList) tom_match7_1_2_1;
 
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
	     {  Instruction tom_match9_1 = null; tom_match9_1 = ( Instruction) t;matchlab_match9_pattern1: {  TomTerm var = null;  TomNumberList l1 = null;  Expression source = null; if(tom_is_fun_sym_Assign(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  Expression tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match9_1); if(tom_is_fun_sym_Variable(tom_match9_1_1)) {  Option tom_match9_1_1_1 = null;  TomName tom_match9_1_1_2 = null;  TomType tom_match9_1_1_3 = null; tom_match9_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match9_1_1); tom_match9_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match9_1_1); tom_match9_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match9_1_1); var = ( TomTerm) tom_match9_1_1; if(tom_is_fun_sym_PositionName(tom_match9_1_1_2)) {  TomNumberList tom_match9_1_1_2_1 = null; tom_match9_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match9_1_1_2); l1 = ( TomNumberList) tom_match9_1_1_2_1; source = ( Expression) tom_match9_1_2;
 	      
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) <= 1) {
		  numberVarRemoved++;
		  return tom_make_Action(empty()) ;
		} else {
		  Expression expr = (Expression) traversal().genericTraversal(source, this);
		  return tom_make_Assign(var,expr) ;
		}
	       } } }}matchlab_match9_pattern2: {  TomTerm var = null;  TomNumberList l1 = null; if(tom_is_fun_sym_Assign(tom_match9_1)) {  TomTerm tom_match9_1_1 = null;  Expression tom_match9_1_2 = null; tom_match9_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match9_1); var = ( TomTerm) tom_match9_1_1; if(tom_is_fun_sym_TomTermToExpression(tom_match9_1_2)) {  TomTerm tom_match9_1_2_1 = null; tom_match9_1_2_1 = ( TomTerm) tom_get_slot_TomTermToExpression_astTerm(tom_match9_1_2); if(tom_is_fun_sym_Variable(tom_match9_1_2_1)) {  Option tom_match9_1_2_1_1 = null;  TomName tom_match9_1_2_1_2 = null;  TomType tom_match9_1_2_1_3 = null; tom_match9_1_2_1_1 = ( Option) tom_get_slot_Variable_option(tom_match9_1_2_1); tom_match9_1_2_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match9_1_2_1); tom_match9_1_2_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match9_1_2_1); if(tom_is_fun_sym_PositionName(tom_match9_1_2_1_2)) {  TomNumberList tom_match9_1_2_1_2_1 = null; tom_match9_1_2_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match9_1_2_1_2); l1 = ( TomNumberList) tom_match9_1_2_1_2_1;
      
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
       {  TomTerm tom_match11_1 = null; tom_match11_1 = ( TomTerm) t;matchlab_match11_pattern1: {  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match11_1)) {  String tom_match11_1_1 = null;  Expression tom_match11_1_2 = null;  int tom_match11_1_3;  Expression tom_match11_1_4 = null;  Expression tom_match11_1_5 = null; tom_match11_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match11_1); tom_match11_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match11_1); tom_match11_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match11_1); tom_match11_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match11_1); tom_match11_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match11_1); name = ( String) tom_match11_1_1;
 
	  if (name.equals(varName))
	    return true;
	 }} }
 
      tmpList = tmpList.getTail();
    }
    return false;
  }

  private TomList addOneUse (final String name, final boolean inAssign) {
    Replace1 replace = new Replace1 () {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     {  TomTerm tom_match12_1 = null; tom_match12_1 = ( TomTerm) t;matchlab_match12_pattern1: {  String varName = null;  int nbUse;  Expression source = null; if(tom_is_fun_sym_AssignedVariable(tom_match12_1)) {  String tom_match12_1_1 = null;  Expression tom_match12_1_2 = null;  int tom_match12_1_3;  Expression tom_match12_1_4 = null;  Expression tom_match12_1_5 = null; tom_match12_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match12_1); tom_match12_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match12_1); tom_match12_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match12_1); tom_match12_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match12_1); tom_match12_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match12_1); varName = ( String) tom_match12_1_1; source = ( Expression) tom_match12_1_2; nbUse = ( int) tom_match12_1_3; if(tom_is_fun_sym_TrueTL(tom_match12_1_4)) { if(tom_is_fun_sym_FalseTL(tom_match12_1_5)) {
 
		if (varName.equals(name)) {
		  int newNbUse = nbUse + 1;
		  return tom_make_AssignedVariable(varName,source,newNbUse,tom_make_TrueTL(),tom_make_FalseTL()) ;
		} else
		  return t;
	       } } }}matchlab_match12_pattern2: {  String varName = null;  Expression source = null;  int nbUse; if(tom_is_fun_sym_AssignedVariable(tom_match12_1)) {  String tom_match12_1_1 = null;  Expression tom_match12_1_2 = null;  int tom_match12_1_3;  Expression tom_match12_1_4 = null;  Expression tom_match12_1_5 = null; tom_match12_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match12_1); tom_match12_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match12_1); tom_match12_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match12_1); tom_match12_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match12_1); tom_match12_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match12_1); varName = ( String) tom_match12_1_1; source = ( Expression) tom_match12_1_2; nbUse = ( int) tom_match12_1_3; if(tom_is_fun_sym_TrueTL(tom_match12_1_4)) { if(tom_is_fun_sym_TrueTL(tom_match12_1_5)) {
 
		if (varName.equals(name)) {
		  int newNbUse = nbUse+ 1;
		  if (inAssign && insideDoWhile) { // the variable is usedInDoWhile and reaffected in DoWhile : it can't be removed
		    //optimDebug(varName+" has been set unremovable");
		    return tom_make_AssignedVariable(varName,source,newNbUse,tom_make_TrueTL(),tom_make_FalseTL()) ;
		  }	else
		    return tom_make_AssignedVariable(varName,source,newNbUse,tom_make_TrueTL(),tom_make_TrueTL()) ;
		} else
		  return t;
	       } } }}matchlab_match12_pattern3: {  int nbUse;  String varName = null;  Expression source = null; if(tom_is_fun_sym_AssignedVariable(tom_match12_1)) {  String tom_match12_1_1 = null;  Expression tom_match12_1_2 = null;  int tom_match12_1_3;  Expression tom_match12_1_4 = null;  Expression tom_match12_1_5 = null; tom_match12_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match12_1); tom_match12_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match12_1); tom_match12_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match12_1); tom_match12_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match12_1); tom_match12_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match12_1); varName = ( String) tom_match12_1_1; source = ( Expression) tom_match12_1_2; nbUse = ( int) tom_match12_1_3; if(tom_is_fun_sym_FalseTL(tom_match12_1_4)) { if(tom_is_fun_sym_TrueTL(tom_match12_1_5)) {
 
		if (varName.equals(name)) {
		  int newNbUse = nbUse + 1;
		  if (!inAssign && insideDoWhile) {				
		    //optimDebug(varName+" has been set 'usedInDoWhile'");
		    return tom_make_AssignedVariable(varName,source,newNbUse,tom_make_TrueTL(),tom_make_TrueTL()) ;
		  }	else
		    return tom_make_AssignedVariable(varName,source,newNbUse,tom_make_FalseTL(),tom_make_TrueTL()) ;
		} else
		  return t;
	       } } }}matchlab_match12_pattern4: {

 
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
       {  TomTerm tom_match13_1 = null; tom_match13_1 = ( TomTerm) t;matchlab_match13_pattern1: {  int n;  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match13_1)) {  String tom_match13_1_1 = null;  Expression tom_match13_1_2 = null;  int tom_match13_1_3;  Expression tom_match13_1_4 = null;  Expression tom_match13_1_5 = null; tom_match13_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match13_1); tom_match13_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match13_1); tom_match13_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match13_1); tom_match13_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match13_1); tom_match13_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match13_1); name = ( String) tom_match13_1_1; n = ( int) tom_match13_1_3; if(tom_is_fun_sym_TrueTL(tom_match13_1_5)) {
 
	  if (name.equals(varName))
	    return n;
	 } }}matchlab_match13_pattern2: { if(tom_is_fun_sym_AssignedVariable(tom_match13_1)) {  String tom_match13_1_1 = null;  Expression tom_match13_1_2 = null;  int tom_match13_1_3;  Expression tom_match13_1_4 = null;  Expression tom_match13_1_5 = null; tom_match13_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match13_1); tom_match13_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match13_1); tom_match13_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match13_1); tom_match13_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match13_1); tom_match13_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match13_1); if(tom_is_fun_sym_FalseTL(tom_match13_1_5)) {
 
	  return 2; // 2 or any value that prevent a variable from being removed
	 } }} }
 
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
       {  TomTerm tom_match14_1 = null; tom_match14_1 = ( TomTerm) t;matchlab_match14_pattern1: {  Expression s = null;  String name = null; if(tom_is_fun_sym_AssignedVariable(tom_match14_1)) {  String tom_match14_1_1 = null;  Expression tom_match14_1_2 = null;  int tom_match14_1_3;  Expression tom_match14_1_4 = null;  Expression tom_match14_1_5 = null; tom_match14_1_1 = ( String) tom_get_slot_AssignedVariable_varName(tom_match14_1); tom_match14_1_2 = ( Expression) tom_get_slot_AssignedVariable_source(tom_match14_1); tom_match14_1_3 = ( int) tom_get_slot_AssignedVariable_nbUse(tom_match14_1); tom_match14_1_4 = ( Expression) tom_get_slot_AssignedVariable_usedInDoWhile(tom_match14_1); tom_match14_1_5 = ( Expression) tom_get_slot_AssignedVariable_removable(tom_match14_1); name = ( String) tom_match14_1_1; s = ( Expression) tom_match14_1_2;
 
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
	     {  TomTerm tom_match15_1 = null; tom_match15_1 = ( TomTerm) t;matchlab_match15_pattern1: {  TomNumberList l1 = null; if(tom_is_fun_sym_Declaration(tom_match15_1)) {  TomTerm tom_match15_1_1 = null; tom_match15_1_1 = ( TomTerm) tom_get_slot_Declaration_kid1(tom_match15_1); if(tom_is_fun_sym_Variable(tom_match15_1_1)) {  Option tom_match15_1_1_1 = null;  TomName tom_match15_1_1_2 = null;  TomType tom_match15_1_1_3 = null; tom_match15_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match15_1_1); tom_match15_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match15_1_1); tom_match15_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match15_1_1); if(tom_is_fun_sym_PositionName(tom_match15_1_1_2)) {  TomNumberList tom_match15_1_1_2_1 = null; tom_match15_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match15_1_1_2); l1 = ( TomNumberList) tom_match15_1_1_2_1;
 
		String name = "tom"+numberListToIdentifier(l1);
		if (getNbUse(name) == 0) //  <= 1 if no problem with code generation
		  return tom_make_Tom(empty()) ;
		else
		  return t;
	       } } }}matchlab_match15_pattern2: {
 
		return traversal().genericTraversal(t, this);
	      } }
 
	  } else if (t instanceof Instruction) {
	     {  Instruction tom_match16_1 = null; tom_match16_1 = ( Instruction) t;matchlab_match16_pattern1: {  Expression source = null;  TomNumberList l1 = null; if(tom_is_fun_sym_Assign(tom_match16_1)) {  TomTerm tom_match16_1_1 = null;  Expression tom_match16_1_2 = null; tom_match16_1_1 = ( TomTerm) tom_get_slot_Assign_kid1(tom_match16_1); tom_match16_1_2 = ( Expression) tom_get_slot_Assign_source(tom_match16_1); if(tom_is_fun_sym_Variable(tom_match16_1_1)) {  Option tom_match16_1_1_1 = null;  TomName tom_match16_1_1_2 = null;  TomType tom_match16_1_1_3 = null; tom_match16_1_1_1 = ( Option) tom_get_slot_Variable_option(tom_match16_1_1); tom_match16_1_1_2 = ( TomName) tom_get_slot_Variable_astName(tom_match16_1_1); tom_match16_1_1_3 = ( TomType) tom_get_slot_Variable_astType(tom_match16_1_1); if(tom_is_fun_sym_PositionName(tom_match16_1_1_2)) {  TomNumberList tom_match16_1_1_2_1 = null; tom_match16_1_1_2_1 = ( TomNumberList) tom_get_slot_PositionName_numberList(tom_match16_1_1_2); l1 = ( TomNumberList) tom_match16_1_1_2_1; source = ( Expression) tom_match16_1_2;
 
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
