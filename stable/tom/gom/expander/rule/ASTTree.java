/* Generated by TOM (version 2.5rc2): Do not edit this file *//*
 *
 * GOM
 *
 * Copyright (C) 2007, INRIA
 * Nancy, France.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 *
 * Antoine Reilles  e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.gom.expander.rule;

import tom.gom.adt.rule.RuleAbstractType;
import tom.gom.adt.rule.types.*;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.*;

public class ASTTree extends CommonTree {

  /* Generated by TOM (version 2.5rc2): Do not edit this file *//* Generated by TOM (version 2.5rc2): Do not edit this file *//* Generated by TOM (version 2.5rc2): Do not edit this file */ private static boolean tom_equal_term_String(String t1, String t2) { return  (t1.equals(t2)) ;}private static boolean tom_is_sort_String(String t) { return  t instanceof String ;}  /* Generated by TOM (version 2.5rc2): Do not edit this file */private static boolean tom_equal_term_int(int t1, int t2) { return  (t1==t2) ;}private static boolean tom_is_sort_int(int t) { return  true ;} private static boolean tom_equal_term_Rule(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Rule(Object t) { return  t instanceof tom.gom.adt.rule.types.Rule ;}private static boolean tom_equal_term_RuleList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_RuleList(Object t) { return  t instanceof tom.gom.adt.rule.types.RuleList ;}private static boolean tom_equal_term_TermList(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_TermList(Object t) { return  t instanceof tom.gom.adt.rule.types.TermList ;}private static boolean tom_equal_term_Condition(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Condition(Object t) { return  t instanceof tom.gom.adt.rule.types.Condition ;}private static boolean tom_equal_term_Term(Object t1, Object t2) { return  t1.equals(t2) ;}private static boolean tom_is_sort_Term(Object t) { return  t instanceof tom.gom.adt.rule.types.Term ;}private static boolean tom_is_fun_sym_Rule( tom.gom.adt.rule.types.Rule  t) { return  t instanceof tom.gom.adt.rule.types.rule.Rule ;}private static  tom.gom.adt.rule.types.Rule  tom_make_Rule( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.rule.Rule.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_Rule_lhs( tom.gom.adt.rule.types.Rule  t) { return  t.getlhs() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_Rule_rhs( tom.gom.adt.rule.types.Rule  t) { return  t.getrhs() ;}private static boolean tom_is_fun_sym_ConditionalRule( tom.gom.adt.rule.types.Rule  t) { return  t instanceof tom.gom.adt.rule.types.rule.ConditionalRule ;}private static  tom.gom.adt.rule.types.Rule  tom_make_ConditionalRule( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1,  tom.gom.adt.rule.types.Condition  t2) { return  tom.gom.adt.rule.types.rule.ConditionalRule.make(t0, t1, t2) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_ConditionalRule_lhs( tom.gom.adt.rule.types.Rule  t) { return  t.getlhs() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_ConditionalRule_rhs( tom.gom.adt.rule.types.Rule  t) { return  t.getrhs() ;}private static  tom.gom.adt.rule.types.Condition  tom_get_slot_ConditionalRule_cond( tom.gom.adt.rule.types.Rule  t) { return  t.getcond() ;}private static boolean tom_is_fun_sym_CondTerm( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondTerm ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondTerm( tom.gom.adt.rule.types.Term  t0) { return  tom.gom.adt.rule.types.condition.CondTerm.make(t0) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondTerm_t( tom.gom.adt.rule.types.Condition  t) { return  t.gett() ;}private static boolean tom_is_fun_sym_CondEquals( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondEquals ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondEquals( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondEquals.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondEquals_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondEquals_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondNotEquals( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondNotEquals ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondNotEquals( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondNotEquals.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondNotEquals_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondNotEquals_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondLessEquals( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondLessEquals ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondLessEquals( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondLessEquals.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondLessEquals_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondLessEquals_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondLessThan( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondLessThan ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondLessThan( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondLessThan.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondLessThan_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondLessThan_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondGreaterEquals( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondGreaterEquals ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondGreaterEquals( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondGreaterEquals.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondGreaterEquals_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondGreaterEquals_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondGreaterThan( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondGreaterThan ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondGreaterThan( tom.gom.adt.rule.types.Term  t0,  tom.gom.adt.rule.types.Term  t1) { return  tom.gom.adt.rule.types.condition.CondGreaterThan.make(t0, t1) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondGreaterThan_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondGreaterThan_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_CondMethod( tom.gom.adt.rule.types.Condition  t) { return  t instanceof tom.gom.adt.rule.types.condition.CondMethod ;}private static  tom.gom.adt.rule.types.Condition  tom_make_CondMethod( tom.gom.adt.rule.types.Term  t0,  String  t1,  tom.gom.adt.rule.types.Term  t2) { return  tom.gom.adt.rule.types.condition.CondMethod.make(t0, t1, t2) ; }private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondMethod_t1( tom.gom.adt.rule.types.Condition  t) { return  t.gett1() ;}private static  String  tom_get_slot_CondMethod_name( tom.gom.adt.rule.types.Condition  t) { return  t.getname() ;}private static  tom.gom.adt.rule.types.Term  tom_get_slot_CondMethod_t2( tom.gom.adt.rule.types.Condition  t) { return  t.gett2() ;}private static boolean tom_is_fun_sym_Appl( tom.gom.adt.rule.types.Term  t) { return  t instanceof tom.gom.adt.rule.types.term.Appl ;}private static  tom.gom.adt.rule.types.Term  tom_make_Appl( String  t0,  tom.gom.adt.rule.types.TermList  t1) { return  tom.gom.adt.rule.types.term.Appl.make(t0, t1) ; }private static  String  tom_get_slot_Appl_symbol( tom.gom.adt.rule.types.Term  t) { return  t.getsymbol() ;}private static  tom.gom.adt.rule.types.TermList  tom_get_slot_Appl_args( tom.gom.adt.rule.types.Term  t) { return  t.getargs() ;}private static  tom.gom.adt.rule.types.Term  tom_make_Var( String  t0) { return  tom.gom.adt.rule.types.term.Var.make(t0) ; }private static  tom.gom.adt.rule.types.Term  tom_make_BuiltinInt( int  t0) { return  tom.gom.adt.rule.types.term.BuiltinInt.make(t0) ; }private static  tom.gom.adt.rule.types.Term  tom_make_BuiltinString( String  t0) { return  tom.gom.adt.rule.types.term.BuiltinString.make(t0) ; }private static boolean tom_is_fun_sym_RuleList( tom.gom.adt.rule.types.RuleList  t) { return  t instanceof tom.gom.adt.rule.types.rulelist.ConsRuleList || t instanceof tom.gom.adt.rule.types.rulelist.EmptyRuleList ;}private static  tom.gom.adt.rule.types.RuleList  tom_empty_list_RuleList() { return  tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ; }private static  tom.gom.adt.rule.types.RuleList  tom_cons_list_RuleList( tom.gom.adt.rule.types.Rule  e,  tom.gom.adt.rule.types.RuleList  l) { return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make(e,l) ; }private static  tom.gom.adt.rule.types.Rule  tom_get_head_RuleList_RuleList( tom.gom.adt.rule.types.RuleList  l) { return  l.getHeadRuleList() ;}private static  tom.gom.adt.rule.types.RuleList  tom_get_tail_RuleList_RuleList( tom.gom.adt.rule.types.RuleList  l) { return  l.getTailRuleList() ;}private static boolean tom_is_empty_RuleList_RuleList( tom.gom.adt.rule.types.RuleList  l) { return  l.isEmptyRuleList() ;}   private static   tom.gom.adt.rule.types.RuleList  tom_append_list_RuleList( tom.gom.adt.rule.types.RuleList l1,  tom.gom.adt.rule.types.RuleList  l2) {     if(tom_is_empty_RuleList_RuleList(l1)) {       return l2;     } else if(tom_is_empty_RuleList_RuleList(l2)) {       return l1;     } else if(tom_is_empty_RuleList_RuleList(tom_get_tail_RuleList_RuleList(l1))) {       return ( tom.gom.adt.rule.types.RuleList )tom_cons_list_RuleList(tom_get_head_RuleList_RuleList(l1),l2);     } else {       return ( tom.gom.adt.rule.types.RuleList )tom_cons_list_RuleList(tom_get_head_RuleList_RuleList(l1),tom_append_list_RuleList(tom_get_tail_RuleList_RuleList(l1),l2));     }   }   private static   tom.gom.adt.rule.types.RuleList  tom_get_slice_RuleList( tom.gom.adt.rule.types.RuleList  begin,  tom.gom.adt.rule.types.RuleList  end, tom.gom.adt.rule.types.RuleList  tail) {     if(tom_equal_term_RuleList(begin,end)) {       return tail;     } else {       return ( tom.gom.adt.rule.types.RuleList )tom_cons_list_RuleList(tom_get_head_RuleList_RuleList(begin),( tom.gom.adt.rule.types.RuleList )tom_get_slice_RuleList(tom_get_tail_RuleList_RuleList(begin),end,tail));     }   }   private static boolean tom_is_fun_sym_TermList( tom.gom.adt.rule.types.TermList  t) { return  t instanceof tom.gom.adt.rule.types.termlist.ConsTermList || t instanceof tom.gom.adt.rule.types.termlist.EmptyTermList ;}private static  tom.gom.adt.rule.types.TermList  tom_empty_list_TermList() { return  tom.gom.adt.rule.types.termlist.EmptyTermList.make() ; }private static  tom.gom.adt.rule.types.TermList  tom_cons_list_TermList( tom.gom.adt.rule.types.Term  e,  tom.gom.adt.rule.types.TermList  l) { return  tom.gom.adt.rule.types.termlist.ConsTermList.make(e,l) ; }private static  tom.gom.adt.rule.types.Term  tom_get_head_TermList_TermList( tom.gom.adt.rule.types.TermList  l) { return  l.getHeadTermList() ;}private static  tom.gom.adt.rule.types.TermList  tom_get_tail_TermList_TermList( tom.gom.adt.rule.types.TermList  l) { return  l.getTailTermList() ;}private static boolean tom_is_empty_TermList_TermList( tom.gom.adt.rule.types.TermList  l) { return  l.isEmptyTermList() ;}   private static   tom.gom.adt.rule.types.TermList  tom_append_list_TermList( tom.gom.adt.rule.types.TermList l1,  tom.gom.adt.rule.types.TermList  l2) {     if(tom_is_empty_TermList_TermList(l1)) {       return l2;     } else if(tom_is_empty_TermList_TermList(l2)) {       return l1;     } else if(tom_is_empty_TermList_TermList(tom_get_tail_TermList_TermList(l1))) {       return ( tom.gom.adt.rule.types.TermList )tom_cons_list_TermList(tom_get_head_TermList_TermList(l1),l2);     } else {       return ( tom.gom.adt.rule.types.TermList )tom_cons_list_TermList(tom_get_head_TermList_TermList(l1),tom_append_list_TermList(tom_get_tail_TermList_TermList(l1),l2));     }   }   private static   tom.gom.adt.rule.types.TermList  tom_get_slice_TermList( tom.gom.adt.rule.types.TermList  begin,  tom.gom.adt.rule.types.TermList  end, tom.gom.adt.rule.types.TermList  tail) {     if(tom_equal_term_TermList(begin,end)) {       return tail;     } else {       return ( tom.gom.adt.rule.types.TermList )tom_cons_list_TermList(tom_get_head_TermList_TermList(begin),( tom.gom.adt.rule.types.TermList )tom_get_slice_TermList(tom_get_tail_TermList_TermList(begin),end,tail));     }   }    
  private RuleAbstractType inAstTerm = null;
  private int childIndex = 0;

	public ASTTree(CommonTree node) {
		super(node);
		this.token = node.token;
    initAstTerm(node.token);
	}

	public ASTTree(Token t) {
		this.token = t;
    initAstTerm(t);
	}

  public RuleAbstractType getTerm() {
    return inAstTerm;
  }

  private void initAstTerm(Token t) {
    if(null==t) {
      return;
    }
    switch (t.getType()) {
      case RuleParser.RULELIST:
        inAstTerm = tom_empty_list_RuleList();
        break;
      case RuleParser.RULE:
        inAstTerm = tom_make_Rule(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDRULE:
        inAstTerm =
          tom_make_ConditionalRule(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1),tom_make_CondTerm(tom_make_BuiltinInt(-1)))
;
        break;
      case RuleParser.CONDTERM:
        inAstTerm =
          tom_make_CondTerm(tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDEQUALS:
        inAstTerm =
          tom_make_CondEquals(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDNOTEQUALS:
        inAstTerm =
          tom_make_CondNotEquals(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDLEQ:
        inAstTerm =
          tom_make_CondLessEquals(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDLT:
        inAstTerm =
          tom_make_CondLessThan(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDGEQ:
        inAstTerm =
          tom_make_CondGreaterEquals(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDGT:
        inAstTerm =
          tom_make_CondGreaterThan(tom_make_BuiltinInt(-1),tom_make_BuiltinInt(-1));
        break;
      case RuleParser.CONDMETHOD:
        inAstTerm =
          tom_make_CondMethod(tom_make_BuiltinInt(-1),"-1",tom_make_BuiltinInt(-1));
        break;
      case RuleParser.INT:
        inAstTerm = tom_make_BuiltinInt(Integer.parseInt(token.getText()));
        break;
      case RuleParser.APPL:
        inAstTerm = tom_make_Appl(token.getText(),tom_empty_list_TermList());
        break;
      case RuleParser.STRING:
        inAstTerm = tom_make_BuiltinString(token.getText());
        break;
      case RuleParser.ID:
        inAstTerm = tom_make_Var(token.getText());
        break;
      case RuleParser.RPAR:
      case RuleParser.IF:
      case RuleParser.WS:
      case RuleParser.LPAR:
      case RuleParser.EOF:
      case RuleParser.ARROW:
      case RuleParser.COMA:
      case RuleParser.ESC:
    }
  }

  public void addChild(Tree t) {
    super.addChild(t);
    if (null==t) {
      return;
    }
    ASTTree tree = (ASTTree) t;
    RuleAbstractType trm = tree.getTerm();
    if (null==trm || inAstTerm==null) {
      return;
    }
    if (tom_is_sort_RuleList(inAstTerm)) {{  tom.gom.adt.rule.types.RuleList  tomMatch452NameNumberfreshSubject_1=(( tom.gom.adt.rule.types.RuleList )inAstTerm);if (tom_is_fun_sym_RuleList(tomMatch452NameNumberfreshSubject_1)) {{  tom.gom.adt.rule.types.RuleList  tomMatch452NameNumber_freshVar_0=tomMatch452NameNumberfreshSubject_1;if ( true ) {

        Rule rl = (Rule) trm;
        inAstTerm = tom_append_list_RuleList(tomMatch452NameNumber_freshVar_0,tom_cons_list_RuleList(rl,tom_empty_list_RuleList()));
      }}}}}if (tom_is_sort_Rule(inAstTerm)) {{  tom.gom.adt.rule.types.Rule  tomMatch453NameNumberfreshSubject_1=(( tom.gom.adt.rule.types.Rule )inAstTerm);if (tom_is_fun_sym_Rule(tomMatch453NameNumberfreshSubject_1)) {if ( true ) {



        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }}if (tom_is_fun_sym_ConditionalRule(tomMatch453NameNumberfreshSubject_1)) {if ( true ) {

        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }}}}if (tom_is_sort_Condition(inAstTerm)) {{  tom.gom.adt.rule.types.Condition  tomMatch454NameNumberfreshSubject_1=(( tom.gom.adt.rule.types.Condition )inAstTerm);{ boolean tomMatch454NameNumber_freshVar_0= false ;if (tom_is_fun_sym_CondTerm(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondEquals(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondNotEquals(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondLessEquals(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondLessThan(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondGreaterEquals(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;} else {if (tom_is_fun_sym_CondGreaterThan(tomMatch454NameNumberfreshSubject_1)) {tomMatch454NameNumber_freshVar_0= true ;}}}}}}}if ((tomMatch454NameNumber_freshVar_0 ==  true )) {if ( true ) {




        inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(childIndex,trm);
        childIndex++;
      }}}if (tom_is_fun_sym_CondMethod(tomMatch454NameNumberfreshSubject_1)) {{  tom.gom.adt.rule.types.Term  tomMatch454NameNumber_freshVar_1=tom_get_slot_CondMethod_t1(tomMatch454NameNumberfreshSubject_1);{  String  tomMatch454NameNumber_freshVar_2=tom_get_slot_CondMethod_name(tomMatch454NameNumberfreshSubject_1);{  tom.gom.adt.rule.types.Term  tomMatch454NameNumber_freshVar_3=tom_get_slot_CondMethod_t2(tomMatch454NameNumberfreshSubject_1);if ( true ) {

        if(childIndex == 1) {
          Term tm = (Term) trm;
          inAstTerm = tom_make_CondMethod(tomMatch454NameNumber_freshVar_1,tm.getname(),tomMatch454NameNumber_freshVar_3);
        } else {
          int idx = childIndex;
          if(childIndex >= 2)
            idx--;
          inAstTerm = (RuleAbstractType) inAstTerm.setChildAt(idx,trm);
        }
        childIndex++;
      }}}}}}}if (tom_is_sort_Term(inAstTerm)) {{  tom.gom.adt.rule.types.Term  tomMatch455NameNumberfreshSubject_1=(( tom.gom.adt.rule.types.Term )inAstTerm);if (tom_is_fun_sym_Appl(tomMatch455NameNumberfreshSubject_1)) {{  String  tomMatch455NameNumber_freshVar_0=tom_get_slot_Appl_symbol(tomMatch455NameNumberfreshSubject_1);{  tom.gom.adt.rule.types.TermList  tomMatch455NameNumber_freshVar_1=tom_get_slot_Appl_args(tomMatch455NameNumberfreshSubject_1);if (tom_is_fun_sym_TermList(tomMatch455NameNumber_freshVar_1)) {{  tom.gom.adt.rule.types.TermList  tomMatch455NameNumber_freshVar_2=tomMatch455NameNumber_freshVar_1;if ( true ) {



        Term tm = (Term) trm;
        if (childIndex == 0 ) {
          /* This first child has to be an ID, which is the symbol name */
          inAstTerm = tom_make_Appl(tm.getname(),tom_empty_list_TermList());
        } else {
          inAstTerm = tom_make_Appl(tomMatch455NameNumber_freshVar_0,tom_append_list_TermList(tomMatch455NameNumber_freshVar_2,tom_cons_list_TermList(tm,tom_empty_list_TermList())));
        }
        childIndex++;
      }}}}}}}}

  }
}
