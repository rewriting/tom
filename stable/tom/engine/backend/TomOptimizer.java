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

  public TomOptimizer(jtom.TomEnvironment environment) {
    super(environment);
    numberMatchFound = 0;
    numberVarFound = 0;
  }
  
  // ------------------------------------------------------------
                                                                                                                                                                  
  // ------------------------------------------------------------
    
  private final boolean debug = true;

  private void optimDebug(String s) {
    if (debug)
      System.out.println(s);
  }

  private int numberMatchFound;
  private int numberVarFound;

  public TomTerm optimize(TomTerm subject) {
    TomTerm optimizedTerm;
    optimizedTerm = optimize_pass_1(subject);
    return optimizedTerm;
  }


  private TomTerm optimize_pass_1(TomTerm subject) {
    //optimDebug("Starting pass 1 of the optimizer");
    ArrayList varList = new ArrayList();
    collectCompiledMatch(subject, varList,"");
    /*
    System.out.println("Nb match :"+numberMatchFound);
    System.out.println("Nb TOM var found :"+numberVarFound);
    System.out.println("Nb var in list :"+varList.size());
    System.out.println(varList);*/
    // Some list manipulations
    /*  System.out.println(empty());
       Integer deux = new Integer(2);
       System.out.println(l);
      System.out.println(cons(`Number(deux),empty()));
       // System.out.println(cons(`Number(deux),null)); -> fails
       System.out.println(cons(null,cons(`Number(deux),empty())));
    */
    return replaceCompiledMatch(subject, varList);
    //return subject;
  }

  private void collectCompiledMatch(TomTerm subject, final Collection list, final String blockName) {
    //optimDebug("Entering collectCompiledMatch");
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     { TomTerm tom_match1_1 = null; tom_match1_1 = (TomTerm) t;matchlab_match1_pattern1: { TomList decl = null; TomList automata = null; if(tom_is_fun_sym_CompiledMatch(tom_match1_1)) { TomList tom_match1_1_1 = null; TomList tom_match1_1_2 = null; tom_match1_1_1 = (TomList) tom_get_slot_CompiledMatch_decls(tom_match1_1); tom_match1_1_2 = (TomList) tom_get_slot_CompiledMatch_automataList(tom_match1_1); decl = (TomList) tom_match1_1_1; automata = (TomList) tom_match1_1_2;
 
		collectVariableDecl((TomList) decl, list);
		collectVariableAuto((TomList) automata, list, blockName);
		numberMatchFound++;
		return false;
	       }}matchlab_match1_pattern2: {
 return true;} }
  //match
	  } else 
	    return true;
	}//apply
      };
    traversal().genericCollect(subject, collect);
  }


  private void collectVariableDecl(TomList subject, final Collection list) {
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof Instruction) {
	     { Instruction tom_match2_1 = null; tom_match2_1 = (Instruction) t;matchlab_match2_pattern1: { TomList l1 = null; Expression source = null; if(tom_is_fun_sym_Assign(tom_match2_1)) { TomTerm tom_match2_1_1 = null; Expression tom_match2_1_2 = null; tom_match2_1_1 = (TomTerm) tom_get_slot_Assign_kid1(tom_match2_1); tom_match2_1_2 = (Expression) tom_get_slot_Assign_source(tom_match2_1); if(tom_is_fun_sym_Variable(tom_match2_1_1)) { Option tom_match2_1_1_1 = null; TomName tom_match2_1_1_2 = null; TomType tom_match2_1_1_3 = null; tom_match2_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match2_1_1); tom_match2_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match2_1_1); tom_match2_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match2_1_1); if(tom_is_fun_sym_PositionName(tom_match2_1_1_2)) { TomList tom_match2_1_1_2_1 = null; tom_match2_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match2_1_1_2); l1 = (TomList) tom_match2_1_1_2_1; source = (Expression) tom_match2_1_2;
 
		if (length(l1) != 2) {
		  System.err.println("Such variable should have only 2 number id :"+t);
		  System.exit(1);
		}
		AssignedVariable av = 
		  new AssignedVariable(":tom"+numberListToIdentifier(l1),source,0);
		if (!list.contains(av)) {
		  list.add(av);
		  numberVarFound++;
		} else {
		  System.err.println("By construction a variable cannot be assigned twice : "+t);
		  System.exit(1);
		}
		return false;		    
	       } } }}matchlab_match2_pattern2: {
  return true; } }
  // match
	  } else
	    return true;
	} //apply
      };
    traversal().genericCollect(subject, collect);
  }

  private void collectVariableAuto(TomList subject, final Collection list, final String blockName) {
    //optimDebug("Entering collectVariableAuto");
 
    // Actually we can't handle non-TOM variables: they could be used outside TOM
	
    Collect1 collect = new Collect1() {
	public boolean apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     { TomTerm tom_match3_1 = null; tom_match3_1 = (TomTerm) t;matchlab_match3_pattern1: { TomList l1 = null; if(tom_is_fun_sym_Declaration(tom_match3_1)) { TomTerm tom_match3_1_1 = null; tom_match3_1_1 = (TomTerm) tom_get_slot_Declaration_kid1(tom_match3_1); if(tom_is_fun_sym_Variable(tom_match3_1_1)) { Option tom_match3_1_1_1 = null; TomName tom_match3_1_1_2 = null; TomType tom_match3_1_1_3 = null; tom_match3_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match3_1_1); tom_match3_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match3_1_1); tom_match3_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match3_1_1); if(tom_is_fun_sym_PositionName(tom_match3_1_1_2)) { TomList tom_match3_1_1_2_1 = null; tom_match3_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match3_1_1_2); l1 = (TomList) tom_match3_1_1_2_1;
  
		if (l1.getHead().isRenamedVar()) {
		  AssignedVariable av = 
		    new AssignedVariable(blockName+":tom"+numberListToIdentifier(l1),
					 tom_make_TomTermToExpression(tom_make_Tom(empty())) ,-1);
		  if (!list.contains(av)) {
		    list.add(av);
		    numberVarFound++;
		  }
		}
		return false;
	       } } }}matchlab_match3_pattern2: { TomList l1 = null; if(tom_is_fun_sym_Variable(tom_match3_1)) { Option tom_match3_1_1 = null; TomName tom_match3_1_2 = null; TomType tom_match3_1_3 = null; tom_match3_1_1 = (Option) tom_get_slot_Variable_option(tom_match3_1); tom_match3_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match3_1); tom_match3_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match3_1); if(tom_is_fun_sym_PositionName(tom_match3_1_2)) { TomList tom_match3_1_2_1 = null; tom_match3_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match3_1_2); l1 = (TomList) tom_match3_1_2_1;
 
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    av.numberOfUse++;
		    return false;
		  } 
		}
		System.err.println("Variable used never assigned : "+ name1);
		System.exit(1);
		return false; // no need to go deeper in a Variable
	       } }}matchlab_match3_pattern3: { if(tom_is_fun_sym_CompiledMatch(tom_match3_1)) { TomList tom_match3_1_1 = null; TomList tom_match3_1_2 = null; tom_match3_1_1 = (TomList) tom_get_slot_CompiledMatch_decls(tom_match3_1); tom_match3_1_2 = (TomList) tom_get_slot_CompiledMatch_automataList(tom_match3_1);
 
		collectCompiledMatch((TomTerm)t,list,blockName);
		return false;
	       }}matchlab_match3_pattern4: {
 return true;} }
 
	  } else if (t instanceof Instruction) {
	     { Instruction tom_match4_1 = null; tom_match4_1 = (Instruction) t;matchlab_match4_pattern1: { TomList l1 = null; Expression source = null; if(tom_is_fun_sym_Assign(tom_match4_1)) { TomTerm tom_match4_1_1 = null; Expression tom_match4_1_2 = null; tom_match4_1_1 = (TomTerm) tom_get_slot_Assign_kid1(tom_match4_1); tom_match4_1_2 = (Expression) tom_get_slot_Assign_source(tom_match4_1); if(tom_is_fun_sym_Variable(tom_match4_1_1)) { Option tom_match4_1_1_1 = null; TomName tom_match4_1_1_2 = null; TomType tom_match4_1_1_3 = null; tom_match4_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match4_1_1); tom_match4_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match4_1_1); tom_match4_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match4_1_1); if(tom_is_fun_sym_PositionName(tom_match4_1_1_2)) { TomList tom_match4_1_1_2_1 = null; tom_match4_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match4_1_1_2); l1 = (TomList) tom_match4_1_1_2_1; source = (Expression) tom_match4_1_2;
 
		if (length(l1) <= 2) {
		  System.err.println("Such variable should be find in decl of a CompiledMatch :"+t);
		  System.exit(1);
		}
		AssignedVariable av = 
		  new AssignedVariable(blockName+":tom"+numberListToIdentifier(l1),
				       source,-1);
		if (!list.contains(av)) {
		  list.add(av);
		  numberVarFound++;
		} else {
		  System.err.println("By construction a variable cannot be assigned twice :"+t);
		  System.exit(1);
		}
		return true;	// we go deeper to search in the 'source'		    
	       } } }}matchlab_match4_pattern2: { TomList instList = null; String name = null; if(tom_is_fun_sym_NamedBlock(tom_match4_1)) { String tom_match4_1_1 = null; TomList tom_match4_1_2 = null; tom_match4_1_1 = (String) tom_get_slot_NamedBlock_blockName(tom_match4_1); tom_match4_1_2 = (TomList) tom_get_slot_NamedBlock_instList(tom_match4_1); name = (String) tom_match4_1_1; instList = (TomList) tom_match4_1_2;
 
		//optimDebug("Entering block '"+name+"'");
		collectVariableAuto(instList,list,name);
		//optimDebug("Exiting block '"+name+"'");
		return false;
	       }}matchlab_match4_pattern3: {
 return true;} }
  //match
	  } else {
	    return true;
	  } //if
	} //apply
      };
    traversal().genericCollect(subject, collect);
  }

  private void printTerm(ATerm s) {
    String str = s.toString();
    int index = str.indexOf("(");
    if (index>=0)
      System.out.println(str.substring(0,index));
    else
      System.out.println(str);
  }

  private TomTerm replaceCompiledMatch(TomTerm subject, final Collection varList) {
    //optimDebug("Entering replaceCompiledMatch");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  if (t instanceof TomTerm) {
	     { TomTerm tom_match5_1 = null; tom_match5_1 = (TomTerm) t;matchlab_match5_pattern1: { TomList decl = null; TomList automata = null; if(tom_is_fun_sym_CompiledMatch(tom_match5_1)) { TomList tom_match5_1_1 = null; TomList tom_match5_1_2 = null; tom_match5_1_1 = (TomList) tom_get_slot_CompiledMatch_decls(tom_match5_1); tom_match5_1_2 = (TomList) tom_get_slot_CompiledMatch_automataList(tom_match5_1); decl = (TomList) tom_match5_1_1; automata = (TomList) tom_match5_1_2;
 
		//System.out.println("Match found");
		TomList newDecl = replaceUselessVarDecl(decl, varList);
		TomList newAutomata = replaceUselessVarAuto(automata, varList, "");
		return tom_make_CompiledMatch(newDecl,newAutomata) ; 
	       }}matchlab_match5_pattern2: {
 return traversal().genericTraversal(t,this);} }
 
	  } else {
	    return traversal().genericTraversal(t,this);
	  }
	} //apply 
      };
    return (TomTerm) replace.apply(subject);
  }

  private TomList replaceUselessVarDecl(TomList subject, final Collection varList) {
    //optimDebug("Entering replaceUselessVarDecl");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  if (t instanceof Instruction) {
	     { Instruction tom_match6_1 = null; tom_match6_1 = (Instruction) t;matchlab_match6_pattern1: { Expression source = null; TomList l1 = null; if(tom_is_fun_sym_Assign(tom_match6_1)) { TomTerm tom_match6_1_1 = null; Expression tom_match6_1_2 = null; tom_match6_1_1 = (TomTerm) tom_get_slot_Assign_kid1(tom_match6_1); tom_match6_1_2 = (Expression) tom_get_slot_Assign_source(tom_match6_1); if(tom_is_fun_sym_Variable(tom_match6_1_1)) { Option tom_match6_1_1_1 = null; TomName tom_match6_1_1_2 = null; TomType tom_match6_1_1_3 = null; tom_match6_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match6_1_1); tom_match6_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match6_1_1); tom_match6_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match6_1_1); if(tom_is_fun_sym_PositionName(tom_match6_1_1_2)) { TomList tom_match6_1_1_2_1 = null; tom_match6_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match6_1_1_2); l1 = (TomList) tom_match6_1_1_2_1; source = (Expression) tom_match6_1_2;
 
		if (length(l1) != 2) {
		  System.err.println("Such variable should have only 2 number id :"+t);
		  System.exit(1);
		}	      
		String name1 = ":tom"+numberListToIdentifier(l1);
		//optimDebug("Assignement of "+name1+" found");
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    //optimDebug("Var "+name1+" found");
		    if (av.numberOfUse==0) { // TO CHANGE TO <= 1 when replacing in 'Equal' work
		      //System.out.println(av.numberOfUse);
		      //optimDebug("Removing assignement of var "+name1);
		      return tom_make_Action(empty()) ;
		    } else 
		      return t;
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	       } } }}matchlab_match6_pattern2: {
  return traversal().genericTraversal(t, this);} }
 
	  } else if (t instanceof TomTerm) {
	     { TomTerm tom_match7_1 = null; tom_match7_1 = (TomTerm) t;matchlab_match7_pattern1: { TomList l1 = null; if(tom_is_fun_sym_Declaration(tom_match7_1)) { TomTerm tom_match7_1_1 = null; tom_match7_1_1 = (TomTerm) tom_get_slot_Declaration_kid1(tom_match7_1); if(tom_is_fun_sym_Variable(tom_match7_1_1)) { Option tom_match7_1_1_1 = null; TomName tom_match7_1_1_2 = null; TomType tom_match7_1_1_3 = null; tom_match7_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match7_1_1); tom_match7_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match7_1_1); tom_match7_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match7_1_1); if(tom_is_fun_sym_PositionName(tom_match7_1_1_2)) { TomList tom_match7_1_1_2_1 = null; tom_match7_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match7_1_1_2); l1 = (TomList) tom_match7_1_1_2_1;
 
		Iterator iter = varList.iterator();
		String name1 = ":tom"+numberListToIdentifier(l1);
		//optimDebug("Declaration of "+name1+" found");
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse==0) { // var is unused : no need to be declared
		      //optimDebug("Removing declaration of "+name1);
		      return tom_make_Tom(empty()) ;
		    } else {
		      return t;
		    }
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
		 } } }}matchlab_match7_pattern2: {
  return traversal().genericTraversal(t, this);} }
 
	  } else
	    return traversal().genericTraversal(t,this);
	}
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private TomList replaceUselessVarAuto(TomList subject, final Collection varList, final String blockName) {
    //optimDebug("Entering replaceUselessVarAuto");
    Replace1 replace = new Replace1() {
	public ATerm apply(ATerm t) {
	  //optimDebug("Starting apply on a :");
	  //printTerm(t);
	  if (t instanceof TomTerm) {
	     { TomTerm tom_match8_1 = null; tom_match8_1 = (TomTerm) t;matchlab_match8_pattern1: { TomList l1 = null; if(tom_is_fun_sym_Declaration(tom_match8_1)) { TomTerm tom_match8_1_1 = null; tom_match8_1_1 = (TomTerm) tom_get_slot_Declaration_kid1(tom_match8_1); if(tom_is_fun_sym_Variable(tom_match8_1_1)) { Option tom_match8_1_1_1 = null; TomName tom_match8_1_1_2 = null; TomType tom_match8_1_1_3 = null; tom_match8_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match8_1_1); tom_match8_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match8_1_1); tom_match8_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match8_1_1); if(tom_is_fun_sym_PositionName(tom_match8_1_1_2)) { TomList tom_match8_1_1_2_1 = null; tom_match8_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match8_1_1_2); l1 = (TomList) tom_match8_1_1_2_1;
 
		Iterator iter = varList.iterator();
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		//optimDebug("Declaration of "+name1+" found");
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse==1) { // var is used 1 time : it will be removed
		      //optimDebug("Removing declaration of "+name1);
		      return tom_make_InstructionToTomTerm(tom_make_Action(empty())) ;
		    } else {
		      return t;
		    }
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	       } } }}matchlab_match8_pattern2: { if(tom_is_fun_sym_CompiledMatch(tom_match8_1)) { TomList tom_match8_1_1 = null; TomList tom_match8_1_2 = null; tom_match8_1_1 = (TomList) tom_get_slot_CompiledMatch_decls(tom_match8_1); tom_match8_1_2 = (TomList) tom_get_slot_CompiledMatch_automataList(tom_match8_1);



























 
		return replaceCompiledMatch((TomTerm) t, varList);
	       }}matchlab_match8_pattern3: {
 return traversal().genericTraversal(t, this);} }
  //match
	  } else if (t instanceof Instruction) {
	     { Instruction tom_match9_1 = null; tom_match9_1 = (Instruction) t;matchlab_match9_pattern1: { Expression source = null; TomTerm var = null; TomList l1 = null; if(tom_is_fun_sym_Assign(tom_match9_1)) { TomTerm tom_match9_1_1 = null; Expression tom_match9_1_2 = null; tom_match9_1_1 = (TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = (Expression) tom_get_slot_Assign_source(tom_match9_1); if(tom_is_fun_sym_Variable(tom_match9_1_1)) { Option tom_match9_1_1_1 = null; TomName tom_match9_1_1_2 = null; TomType tom_match9_1_1_3 = null; tom_match9_1_1_1 = (Option) tom_get_slot_Variable_option(tom_match9_1_1); tom_match9_1_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match9_1_1); tom_match9_1_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match9_1_1); var = (TomTerm) tom_match9_1_1; if(tom_is_fun_sym_PositionName(tom_match9_1_1_2)) { TomList tom_match9_1_1_2_1 = null; tom_match9_1_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match9_1_1_2); l1 = (TomList) tom_match9_1_1_2_1; source = (Expression) tom_match9_1_2;
 
		if (length(l1) <= 2) {
		  System.err.println("Such variable should be find in decl of a CompiledMatch :"+t);
		  System.exit(1);
		}	      
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		//optimDebug("Assignement of "+name1+" found");
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    //optimDebug("Var "+name1+" found");
		    if (av.numberOfUse<=1) {
		      //System.out.println(av.numberOfUse);
		      //optimDebug("Removing assignement of var "+name1);
		      return tom_make_Action(empty()) ;
		    } else {
		      Expression expr = (Expression) traversal().genericTraversal(source, this);
			return tom_make_Assign(var,expr) ;
		    } 
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	       } } }}matchlab_match9_pattern2: { TomList l1 = null; TomTerm var = null; if(tom_is_fun_sym_Assign(tom_match9_1)) { TomTerm tom_match9_1_1 = null; Expression tom_match9_1_2 = null; tom_match9_1_1 = (TomTerm) tom_get_slot_Assign_kid1(tom_match9_1); tom_match9_1_2 = (Expression) tom_get_slot_Assign_source(tom_match9_1); var = (TomTerm) tom_match9_1_1; if(tom_is_fun_sym_TomTermToExpression(tom_match9_1_2)) { TomTerm tom_match9_1_2_1 = null; tom_match9_1_2_1 = (TomTerm) tom_get_slot_TomTermToExpression_astTerm(tom_match9_1_2); if(tom_is_fun_sym_Variable(tom_match9_1_2_1)) { Option tom_match9_1_2_1_1 = null; TomName tom_match9_1_2_1_2 = null; TomType tom_match9_1_2_1_3 = null; tom_match9_1_2_1_1 = (Option) tom_get_slot_Variable_option(tom_match9_1_2_1); tom_match9_1_2_1_2 = (TomName) tom_get_slot_Variable_astName(tom_match9_1_2_1); tom_match9_1_2_1_3 = (TomType) tom_get_slot_Variable_astType(tom_match9_1_2_1); if(tom_is_fun_sym_PositionName(tom_match9_1_2_1_2)) { TomList tom_match9_1_2_1_2_1 = null; tom_match9_1_2_1_2_1 = (TomList) tom_get_slot_PositionName_numberList(tom_match9_1_2_1_2); l1 = (TomList) tom_match9_1_2_1_2_1;
      
		String name1 = ":tom"+numberListToIdentifier(l1);
		if (length(l1) > 2) // the var isn't a 'decl' var : we have to add the blockname
		  name1 = blockName+name1;
		Iterator iter = varList.iterator();
		while (iter.hasNext()) {
		  AssignedVariable av = (AssignedVariable) iter.next();
		  if (av.name.equals(name1)) {
		    if (av.numberOfUse<=1) {
		      return tom_make_Assign(var,av.source) ;
		    } else {
		      return t;
		    } 
		  }
		}	
		System.err.println("The variable wasn't added to the list when collecting...");
		System.exit(1);
		return null;
	       } } } }}matchlab_match9_pattern3: { TomList instList = null; String name = null; if(tom_is_fun_sym_NamedBlock(tom_match9_1)) { String tom_match9_1_1 = null; TomList tom_match9_1_2 = null; tom_match9_1_1 = (String) tom_get_slot_NamedBlock_blockName(tom_match9_1); tom_match9_1_2 = (TomList) tom_get_slot_NamedBlock_instList(tom_match9_1); name = (String) tom_match9_1_1; instList = (TomList) tom_match9_1_2;
 
		//optimDebug("Entering block '"+name+"'");
		TomList newInstList = replaceUselessVarAuto(instList,varList,name);
		//optimDebug("Exiting block '"+name+"'");
		return tom_make_NamedBlock(name,newInstList) ;
	       }}matchlab_match9_pattern4: {
 return traversal().genericTraversal(t, this);} }
  //match
	  } else {
	    return  traversal().genericTraversal(t, this);
	  }
	} //apply
      };
    return (TomList) traversal().genericTraversal(subject, replace);
  }

  private class AssignedVariable {
	
    /* 
     * private variables to give only read 
     * access with the homonymous methods 
     */
    private Expression source;
    private String name;
    private int numberOfUse;

    public AssignedVariable (String name, Expression source, int init) {
      this.source = source;
      this.name = name;
      this.numberOfUse = init;
    }

    public boolean equals(Object o) {
      if (o==null)
	return false;
      if (o instanceof AssignedVariable) {
	AssignedVariable av = (AssignedVariable) o;
	return this.name.equals(av.name);
      }
      return false;
    }

    public String toString() {
      return "Var '"+name+"' used "+numberOfUse+" times";
    }

  } // class AssignedVariable


} //Class TomOptimizer
