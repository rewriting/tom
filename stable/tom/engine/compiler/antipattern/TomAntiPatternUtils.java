/* Generated by TOM (version 2.4alpha): Do not edit this file *//*
 * Copyright (c) 2005-2006, INRIA
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met: 
 *  - Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.  
 *  - Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  - Neither the name of the INRIA nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package tom.engine.compiler.antipattern;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.library.strategy.mutraveler.MuTraveler;

import tom.engine.exception.*;

import jjtraveler.reflective.VisitableVisitor;
import jjtraveler.VisitFailure;

/**
 * Class that contains utility functions used for antipattern compilation
 */
public class TomAntiPatternUtils{
	
//	------------------------------------------------------------
	/* Generated by TOM (version 2.4alpha): Do not edit this file *//* Generated by TOM (version 2.4alpha): Do not edit this file *//* Generated by TOM (version 2.4alpha): Do not edit this file */ private static boolean tom_terms_equal_String( String  t1,  String  t2) {  return  (t1.equals(t2))  ;}  /* Generated by TOM (version 2.4alpha): Do not edit this file */ /* Generated by TOM (version 2.4alpha): Do not edit this file */ /* Generated by TOM (version 2.4alpha): Do not edit this file */ /* Generated by TOM (version 2.4alpha): Do not edit this file */ private static boolean tom_terms_equal_Instruction(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_terms_equal_TomTerm(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_terms_equal_Pattern(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_terms_equal_TomList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_terms_equal_PatternList(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_terms_equal_Constraint(Object t1, Object t2) {  return  t1.equals(t2)  ;}private static boolean tom_is_fun_sym_TypedAction( tom.engine.adt.tominstruction.types.Instruction  t) {  return  (t!=null) && t.isTypedAction()  ;}private static  tom.engine.adt.tominstruction.types.Instruction  tom_get_slot_TypedAction_AstInstruction( tom.engine.adt.tominstruction.types.Instruction  t) {  return  t.getAstInstruction()  ;}private static  tom.engine.adt.tomterm.types.Pattern  tom_get_slot_TypedAction_PositivePattern( tom.engine.adt.tominstruction.types.Instruction  t) {  return  t.getPositivePattern()  ;}private static  tom.engine.adt.tomterm.types.PatternList  tom_get_slot_TypedAction_NegativePatternList( tom.engine.adt.tominstruction.types.Instruction  t) {  return  t.getNegativePatternList()  ;}private static boolean tom_is_fun_sym_AntiTerm( tom.engine.adt.tomterm.types.TomTerm  t) {  return  (t!=null) && t.isAntiTerm()  ;}private static  tom.engine.adt.tomterm.types.TomTerm  tom_get_slot_AntiTerm_TomTerm( tom.engine.adt.tomterm.types.TomTerm  t) {  return  t.getTomTerm()  ;}private static boolean tom_is_fun_sym_Pattern( tom.engine.adt.tomterm.types.Pattern  t) {  return  (t!=null) && t.isPattern()  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_get_slot_Pattern_SubjectList( tom.engine.adt.tomterm.types.Pattern  t) {  return  t.getSubjectList()  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_get_slot_Pattern_TomList( tom.engine.adt.tomterm.types.Pattern  t) {  return  t.getTomList()  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_get_slot_Pattern_Guards( tom.engine.adt.tomterm.types.Pattern  t) {  return  t.getGuards()  ;}private static  tom.engine.adt.tomconstraint.types.Constraint  tom_make_EqualConstraint( tom.engine.adt.tomterm.types.TomTerm  t0,  tom.engine.adt.tomterm.types.TomTerm  t1) { return  tom.engine.adt.tomconstraint.types.constraint.EqualConstraint.make(t0, t1); }private static boolean tom_is_fun_sym_concTomTerm( tom.engine.adt.tomterm.types.TomList  t) {  return  t instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm || t instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_empty_list_concTomTerm() { return  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ; }private static  tom.engine.adt.tomterm.types.TomList  tom_cons_list_concTomTerm( tom.engine.adt.tomterm.types.TomTerm  e,  tom.engine.adt.tomterm.types.TomList  l) { return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(e,l) ; }private static  tom.engine.adt.tomterm.types.TomTerm  tom_get_head_concTomTerm_TomList( tom.engine.adt.tomterm.types.TomList  l) {  return  l.getHeadconcTomTerm()  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_get_tail_concTomTerm_TomList( tom.engine.adt.tomterm.types.TomList  l) {  return  l.getTailconcTomTerm()  ;}private static boolean tom_is_empty_concTomTerm_TomList( tom.engine.adt.tomterm.types.TomList  l) {  return  l.isEmptyconcTomTerm()  ;}private static  tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList  l1,  tom.engine.adt.tomterm.types.TomList  l2) {    if(tom_is_empty_concTomTerm_TomList(l1)) {     return l2;    } else if(tom_is_empty_concTomTerm_TomList(l2)) {     return l1;    } else if(tom_is_empty_concTomTerm_TomList(( tom.engine.adt.tomterm.types.TomList )tom_get_tail_concTomTerm_TomList(l1))) {     return ( tom.engine.adt.tomterm.types.TomList )tom_cons_list_concTomTerm(( tom.engine.adt.tomterm.types.TomTerm )tom_get_head_concTomTerm_TomList(l1),l2);    } else {      return ( tom.engine.adt.tomterm.types.TomList )tom_cons_list_concTomTerm(( tom.engine.adt.tomterm.types.TomTerm )tom_get_head_concTomTerm_TomList(l1),tom_append_list_concTomTerm(( tom.engine.adt.tomterm.types.TomList )tom_get_tail_concTomTerm_TomList(l1),l2));    }   }  private static  tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end) {    if(tom_terms_equal_TomList(begin,end)) {      return ( tom.engine.adt.tomterm.types.TomList )tom_empty_list_concTomTerm();    } else {      return ( tom.engine.adt.tomterm.types.TomList )tom_cons_list_concTomTerm(( tom.engine.adt.tomterm.types.TomTerm )tom_get_head_concTomTerm_TomList(begin),( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm(( tom.engine.adt.tomterm.types.TomList )tom_get_tail_concTomTerm_TomList(begin),end));    }   }   /* Generated by TOM (version 2.4alpha): Do not edit this file */private static boolean tom_terms_equal_Strategy(Object t1, Object t2) {  return t1.equals(t2) ;}private static  jjtraveler.reflective.VisitableVisitor  tom_make_mu( jjtraveler.reflective.VisitableVisitor  var,  jjtraveler.reflective.VisitableVisitor  v) { return  new tom.library.strategy.mutraveler.Mu(var,v) ; }/* Generated by TOM (version 2.4alpha): Do not edit this file */private static  jjtraveler.reflective.VisitableVisitor  tom_make_Identity() { return  new tom.library.strategy.mutraveler.Identity() ; }private static  jjtraveler.reflective.VisitableVisitor  tom_make_MuVar( String  name) { return  new tom.library.strategy.mutraveler.MuVar(name) ; }private static  jjtraveler.reflective.VisitableVisitor  tom_make_OneId( jjtraveler.reflective.VisitableVisitor  v) { return  new tom.library.strategy.mutraveler.OneId(v) ; }private static  jjtraveler.reflective.VisitableVisitor  tom_make_ChoiceId( jjtraveler.reflective.VisitableVisitor  first,  jjtraveler.reflective.VisitableVisitor  then) { return  new tom.library.strategy.mutraveler.ChoiceId(first,then) ; }private static  jjtraveler.reflective.VisitableVisitor  tom_make_OnceTopDownId( jjtraveler.reflective.VisitableVisitor  v) { return tom_make_mu(tom_make_MuVar("x"),tom_make_ChoiceId(v,tom_make_OneId(tom_make_MuVar("x")))) ; }   

//	------------------------------------------------------------
	
	private static int antiCounter = 0;
	
	/**
	 * Checks to see if the parameter received contains antipatterns
	 * 
	 * @param tomTerm
	 *            The TomTerm to search
	 * @return true if tomTerm contains anti-symbols false otherwise
	 */	
	public static boolean hasAntiTerms(TomTerm tomTerm){
		
		antiCounter = 0;
		
		try{		
			VisitableVisitor countAnti = tom_make_OnceTopDownId(tom_make_CountAnti());
			MuTraveler.init(countAnti).visit(tomTerm);
		}catch(Exception e){
			throw new TomRuntimeException("Cannot count the number of anti symbols in : " + tomTerm 
					+ "\n Exception:" + e.getMessage());
		}
		
		return (antiCounter > 0 ? true:false);
	}
	
	/**
	 * counts the anti symbols
	 */  
	 private static class CountAnti  extends  tom.engine.adt.tomsignature.TomSignatureBasicStrategy   { public CountAnti( ) { super(tom_make_Identity() ); } public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm(  tom.engine.adt.tomterm.types.TomTerm  tom__arg )  throws jjtraveler.VisitFailure { if(tom__arg instanceof  tom.engine.adt.tomterm.types.TomTerm ) { { tom.engine.adt.tomterm.types.TomTerm  tom_match1_1=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg); if (tom_is_fun_sym_AntiTerm(tom_match1_1) ||  false ) { { tom.engine.adt.tomterm.types.TomTerm  tom_match1_1_TomTerm=tom_get_slot_AntiTerm_TomTerm(tom_match1_1); { tom.engine.adt.tomterm.types.TomTerm  tom_t=tom_match1_1_TomTerm; if ( true ) {


				antiCounter++;
				return tom_t;
			 } } } } } } return super.visit_TomTerm(tom__arg) ;  } }private static  jjtraveler.reflective.VisitableVisitor  tom_make_CountAnti() { return new CountAnti(); }


	
	/**
	 * Compiles the anti-pattern matching problem that it received
	 * 
	 * @param action
	 * @param tomTerm
	 * @param rootpath
	 * @param moduleName
	 * @return compiled expresion
	 */	
	public static Expression getAntiPatternMatchInstruction(Instruction action,
			TomTerm tomTerm,
			TomNumberList rootpath,
			String moduleName) {
		
		// subject to be matched
		TomTerm subject = null;
		
		// extract the subject
		// TODO - the guards
		 if(action instanceof  tom.engine.adt.tominstruction.types.Instruction ) { { tom.engine.adt.tominstruction.types.Instruction  tom_match2_1=(( tom.engine.adt.tominstruction.types.Instruction )action); if (tom_is_fun_sym_TypedAction(tom_match2_1) ||  false ) { { tom.engine.adt.tominstruction.types.Instruction  tom_match2_1_AstInstruction=tom_get_slot_TypedAction_AstInstruction(tom_match2_1); { tom.engine.adt.tomterm.types.Pattern  tom_match2_1_PositivePattern=tom_get_slot_TypedAction_PositivePattern(tom_match2_1); { tom.engine.adt.tomterm.types.PatternList  tom_match2_1_NegativePatternList=tom_get_slot_TypedAction_NegativePatternList(tom_match2_1); if (tom_is_fun_sym_Pattern(tom_match2_1_PositivePattern) ||  false ) { { tom.engine.adt.tomterm.types.TomList  tom_match2_1_PositivePattern_SubjectList=tom_get_slot_Pattern_SubjectList(tom_match2_1_PositivePattern); { tom.engine.adt.tomterm.types.TomList  tom_match2_1_PositivePattern_TomList=tom_get_slot_Pattern_TomList(tom_match2_1_PositivePattern); { tom.engine.adt.tomterm.types.TomList  tom_match2_1_PositivePattern_Guards=tom_get_slot_Pattern_Guards(tom_match2_1_PositivePattern); if (tom_is_fun_sym_concTomTerm(tom_match2_1_PositivePattern_SubjectList) ||  false ) { { tom.engine.adt.tomterm.types.TomList  tom_match2_1_PositivePattern_SubjectList_list1=tom_match2_1_PositivePattern_SubjectList; if (!(tom_is_empty_concTomTerm_TomList(tom_match2_1_PositivePattern_SubjectList_list1))) { { tom.engine.adt.tomterm.types.TomTerm  tom_subject=tom_get_head_concTomTerm_TomList(tom_match2_1_PositivePattern_SubjectList_list1);tom_match2_1_PositivePattern_SubjectList_list1=tom_get_tail_concTomTerm_TomList(tom_match2_1_PositivePattern_SubjectList_list1); if ( true ) {

				subject = tom_subject;
			 } } } } } } } } } } } } } } }

		
		// transform the anti-pattern match problem into 
		// a disunification one
		Constraint disunificationProblem = TomAntiPatternTransform.transform(
				tom_make_EqualConstraint(tomTerm,subject));
		// launch the constraint compiler
//		Contraint compiledApProblem = TomConstraintCompiler.compile(disunificationProblem);
		
//		return EqualTrueAntiPatternMatch("functionToBeCalled", compiledApProblem);
		
		System.out.println("Action:" + action);
		System.out.println("TomTerm:" + tomTerm);
		System.out.println("RootPath:" + rootpath);
		System.out.println("ModuleName:" + moduleName);
		
		return null;
	}
}