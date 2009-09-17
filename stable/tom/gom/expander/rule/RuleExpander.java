/*
 *
 * GOM
 *
 * Copyright (c) 2007-2009, INRIA
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

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;
import tom.gom.adt.rule.RuleRuleAdaptor;
import java.util.logging.Level;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;
import tom.gom.GomMessage;
import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.types.*;
import tom.gom.tools.error.GomRuntimeException;

public class RuleExpander {

         private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }       private static   tom.gom.adt.rule.types.Condition  tom_append_list_CondAnd( tom.gom.adt.rule.types.Condition  l1,  tom.gom.adt.rule.types.Condition  l2) {     if( l1.isEmptyCondAnd() ) {       return l2;     } else if( l2.isEmptyCondAnd() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (l1 instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) ) {       if(  l1.getTailCondAnd() .isEmptyCondAnd() ) {         return  tom.gom.adt.rule.types.condition.ConsCondAnd.make( l1.getHeadCondAnd() ,l2) ;       } else {         return  tom.gom.adt.rule.types.condition.ConsCondAnd.make( l1.getHeadCondAnd() ,tom_append_list_CondAnd( l1.getTailCondAnd() ,l2)) ;       }     } else {       return  tom.gom.adt.rule.types.condition.ConsCondAnd.make(l1,l2) ;     }   }   private static   tom.gom.adt.rule.types.Condition  tom_get_slice_CondAnd( tom.gom.adt.rule.types.Condition  begin,  tom.gom.adt.rule.types.Condition  end, tom.gom.adt.rule.types.Condition  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCondAnd()  ||  (end== tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.condition.ConsCondAnd.make((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )? begin.getHeadCondAnd() :begin),( tom.gom.adt.rule.types.Condition )tom_get_slice_CondAnd((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )? begin.getTailCondAnd() : tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ),end,tail)) ;   }      private static   tom.gom.adt.rule.types.Condition  tom_append_list_CondOr( tom.gom.adt.rule.types.Condition  l1,  tom.gom.adt.rule.types.Condition  l2) {     if( l1.isEmptyCondOr() ) {       return l2;     } else if( l2.isEmptyCondOr() ) {       return l1;     } else if( ((l1 instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (l1 instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) ) {       if(  l1.getTailCondOr() .isEmptyCondOr() ) {         return  tom.gom.adt.rule.types.condition.ConsCondOr.make( l1.getHeadCondOr() ,l2) ;       } else {         return  tom.gom.adt.rule.types.condition.ConsCondOr.make( l1.getHeadCondOr() ,tom_append_list_CondOr( l1.getTailCondOr() ,l2)) ;       }     } else {       return  tom.gom.adt.rule.types.condition.ConsCondOr.make(l1,l2) ;     }   }   private static   tom.gom.adt.rule.types.Condition  tom_get_slice_CondOr( tom.gom.adt.rule.types.Condition  begin,  tom.gom.adt.rule.types.Condition  end, tom.gom.adt.rule.types.Condition  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyCondOr()  ||  (end== tom.gom.adt.rule.types.condition.EmptyCondOr.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.condition.ConsCondOr.make((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )? begin.getHeadCondOr() :begin),( tom.gom.adt.rule.types.Condition )tom_get_slice_CondOr((( ((begin instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || (begin instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )? begin.getTailCondOr() : tom.gom.adt.rule.types.condition.EmptyCondOr.make() ),end,tail)) ;   }      private static   tom.gom.adt.rule.types.RuleList  tom_append_list_RuleList( tom.gom.adt.rule.types.RuleList l1,  tom.gom.adt.rule.types.RuleList  l2) {     if( l1.isEmptyRuleList() ) {       return l2;     } else if( l2.isEmptyRuleList() ) {       return l1;     } else if(  l1.getTailRuleList() .isEmptyRuleList() ) {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,tom_append_list_RuleList( l1.getTailRuleList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.RuleList  tom_get_slice_RuleList( tom.gom.adt.rule.types.RuleList  begin,  tom.gom.adt.rule.types.RuleList  end, tom.gom.adt.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyRuleList()  ||  (end== tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( begin.getHeadRuleList() ,( tom.gom.adt.rule.types.RuleList )tom_get_slice_RuleList( begin.getTailRuleList() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.TermList  tom_append_list_TermList( tom.gom.adt.rule.types.TermList l1,  tom.gom.adt.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.TermList  tom_get_slice_TermList( tom.gom.adt.rule.types.TermList  begin,  tom.gom.adt.rule.types.TermList  end, tom.gom.adt.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end== tom.gom.adt.rule.types.termlist.EmptyTermList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( tom.gom.adt.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}   



  private ModuleList moduleList;

  public RuleExpander(ModuleList data) {
    this.moduleList = data;
  }

  public HookDeclList expandRules(String ruleCode) {
    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    RuleList rulelist =  tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ;
    try {
      Tree ast = (Tree) parser.ruleset().getTree();
      rulelist = (RuleList) RuleRuleAdaptor.getTerm(ast);
    } catch (org.antlr.runtime.RecognitionException e) {
      getLogger().log(Level.SEVERE, "Cannot parse rules",
          new Object[]{});
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    }
    return expand(rulelist);
  }

  protected HookDeclList expand(RuleList rulelist) {
    HookDeclList hookList =  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    /* collect all rules for a given symbol */
    Map<OperatorDecl,RuleList> rulesForOperator =
      new HashMap<OperatorDecl,RuleList>();
    {{if ( (rulelist instanceof tom.gom.adt.rule.types.RuleList) ) {if ( (((( tom.gom.adt.rule.types.RuleList )rulelist) instanceof tom.gom.adt.rule.types.rulelist.ConsRuleList) || ((( tom.gom.adt.rule.types.RuleList )rulelist) instanceof tom.gom.adt.rule.types.rulelist.EmptyRuleList)) ) { tom.gom.adt.rule.types.RuleList  tomMatch226__end__4=(( tom.gom.adt.rule.types.RuleList )rulelist);do {{if (!( tomMatch226__end__4.isEmptyRuleList() )) { tom.gom.adt.rule.types.Rule  tomMatch226_8= tomMatch226__end__4.getHeadRuleList() ;boolean tomMatch226_11= false ; tom.gom.adt.rule.types.Term  tomMatch226_7= null ;if ( (tomMatch226_8 instanceof tom.gom.adt.rule.types.rule.Rule) ) {{tomMatch226_11= true ;tomMatch226_7= tomMatch226_8.getlhs() ;}} else {if ( (tomMatch226_8 instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {{tomMatch226_11= true ;tomMatch226_7= tomMatch226_8.getlhs() ;}}}if (tomMatch226_11) {if ( (tomMatch226_7 instanceof tom.gom.adt.rule.types.term.Appl) ) { tom.gom.adt.rule.types.Rule  tom_rl= tomMatch226__end__4.getHeadRuleList() ;

        OperatorDecl decl = getOperatorDecl( tomMatch226_7.getsymbol() );
        if (null != decl) {
          RuleList rules = rulesForOperator.get(decl);
          if (null == rules) {
            rulesForOperator.put(decl, tom.gom.adt.rule.types.rulelist.ConsRuleList.make(tom_rl, tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) );
          } else {
            rulesForOperator.put(decl,tom_append_list_RuleList(rules, tom.gom.adt.rule.types.rulelist.ConsRuleList.make(tom_rl, tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) ));
          }
        } else {
          getLogger().log(Level.WARNING, "Discard rule \"{0}\"",
              new Object[]{/*XXX:prettyprint*/(tom_rl)});
        }
      }}}if ( tomMatch226__end__4.isEmptyRuleList() ) {tomMatch226__end__4=(( tom.gom.adt.rule.types.RuleList )rulelist);} else {tomMatch226__end__4= tomMatch226__end__4.getTailRuleList() ;}}} while(!( (tomMatch226__end__4==(( tom.gom.adt.rule.types.RuleList )rulelist)) ));}}}}

    /* Generate a construction hook for each constructor */
    for (OperatorDecl opDecl : rulesForOperator.keySet()) {
      TypedProduction prod = opDecl.getProd();
      {{if ( (prod instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )prod) instanceof tom.gom.adt.gom.types.typedproduction.Slots) ) {


          SlotList args = opArgs( (( tom.gom.adt.gom.types.TypedProduction )prod).getSlots() ,1);
          String hookCode =
            generateHookCode(args, rulesForOperator.get(opDecl));
          hookList =
            tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make( tom.gom.adt.gom.types.decl.CutOperator.make(opDecl) , args,  tom.gom.adt.code.types.code.Code.make(hookCode) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("rules") ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )
;
        }}}{if ( (prod instanceof tom.gom.adt.gom.types.TypedProduction) ) {if ( ((( tom.gom.adt.gom.types.TypedProduction )prod) instanceof tom.gom.adt.gom.types.typedproduction.Variadic) ) {


          RuleList rules = rulesForOperator.get(opDecl);
          /* Handle rules for empty: there should be at least one */
          int count = 0;
          RuleList nonEmptyRules = rules;
          {{if ( (rules instanceof tom.gom.adt.rule.types.RuleList) ) {if ( (((( tom.gom.adt.rule.types.RuleList )rules) instanceof tom.gom.adt.rule.types.rulelist.ConsRuleList) || ((( tom.gom.adt.rule.types.RuleList )rules) instanceof tom.gom.adt.rule.types.rulelist.EmptyRuleList)) ) { tom.gom.adt.rule.types.RuleList  tomMatch228__end__4=(( tom.gom.adt.rule.types.RuleList )rules);do {{if (!( tomMatch228__end__4.isEmptyRuleList() )) { tom.gom.adt.rule.types.Rule  tomMatch228_8= tomMatch228__end__4.getHeadRuleList() ;boolean tomMatch228_12= false ; tom.gom.adt.rule.types.Term  tomMatch228_7= null ;if ( (tomMatch228_8 instanceof tom.gom.adt.rule.types.rule.Rule) ) {{tomMatch228_12= true ;tomMatch228_7= tomMatch228_8.getlhs() ;}} else {if ( (tomMatch228_8 instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {{tomMatch228_12= true ;tomMatch228_7= tomMatch228_8.getlhs() ;}}}if (tomMatch228_12) {if ( (tomMatch228_7 instanceof tom.gom.adt.rule.types.term.Appl) ) { tom.gom.adt.rule.types.TermList  tomMatch228_9= tomMatch228_7.getargs() ;if ( ((tomMatch228_9 instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || (tomMatch228_9 instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if ( tomMatch228_9.isEmptyTermList() ) {



              count++;
              nonEmptyRules = tom_append_list_RuleList(tom_get_slice_RuleList((( tom.gom.adt.rule.types.RuleList )rules),tomMatch228__end__4, tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ),tom_append_list_RuleList( tomMatch228__end__4.getTailRuleList() , tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ));
              String hookCode =
                generateHookCode( tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() , tom.gom.adt.rule.types.rulelist.ConsRuleList.make( tomMatch228__end__4.getHeadRuleList() , tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) );
              hookList =
                tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make( tom.gom.adt.gom.types.decl.CutOperator.make(opDecl) ,  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ,  tom.gom.adt.code.types.code.Code.make(hookCode) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("rules") ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )
;
            }}}}}if ( tomMatch228__end__4.isEmptyRuleList() ) {tomMatch228__end__4=(( tom.gom.adt.rule.types.RuleList )rules);} else {tomMatch228__end__4= tomMatch228__end__4.getTailRuleList() ;}}} while(!( (tomMatch228__end__4==(( tom.gom.adt.rule.types.RuleList )rules)) ));}}}}

          if (count>1) {
            getLogger().log(Level.WARNING, "Multiple rules for empty {0}",
                new Object[]{ opDecl.getName() });
          }
          /* Then handle rules for insert */
          if (!nonEmptyRules.isEmptyRuleList()) {
            SlotList args =  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("head",  (( tom.gom.adt.gom.types.TypedProduction )prod).getSort() ) , tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("tail", opDecl.getSort()) , tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) ) ;
            String hookCode =
              generateVariadicHookCode(args, nonEmptyRules);
            hookList =
              tom_append_list_ConcHookDecl(hookList, tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.MakeHookDecl.make( tom.gom.adt.gom.types.decl.CutOperator.make(opDecl) , args,  tom.gom.adt.code.types.code.Code.make(hookCode) ,  tom.gom.adt.gom.types.hookkind.HookKind.make("rules") ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )
;
          }
        }}}}

    }
    return hookList;
  }

  private String generateHookCode(SlotList slotList, RuleList ruleList) {
    StringBuilder output = new StringBuilder();
    if(slotList.isEmptyConcSlot()) {
      while(!ruleList.isEmptyRuleList()) {
        Rule rule = ruleList.getHeadRuleList();
        ruleList = ruleList.getTailRuleList();
        {{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.Rule) ) {if ( ( (( tom.gom.adt.rule.types.Rule )rule).getlhs()  instanceof tom.gom.adt.rule.types.term.Appl) ) {

            output.append("    return `");
            genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
            output.append(";\n");
          }}}}{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {if ( ( (( tom.gom.adt.rule.types.Rule )rule).getlhs()  instanceof tom.gom.adt.rule.types.term.Appl) ) {

            output.append("    %match{\n");
            genCondition( (( tom.gom.adt.rule.types.Rule )rule).getcond() ,output);
            output.append(" -> { return `");
            genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
            output.append("; }\n");
            output.append("}\n");
          }}}}}

      }

    } else {
      output.append("    %match(");
      matchArgs(slotList,output,1);
      output.append(") {\n");
      while(!ruleList.isEmptyRuleList()) {
        Rule rule = ruleList.getHeadRuleList();
        ruleList = ruleList.getTailRuleList();
        {{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.Rule) ) { tom.gom.adt.rule.types.Term  tomMatch230_1= (( tom.gom.adt.rule.types.Rule )rule).getlhs() ;if ( (tomMatch230_1 instanceof tom.gom.adt.rule.types.term.Appl) ) {

            genTermList( tomMatch230_1.getargs() ,output);
            output.append(" -> { return `");
            genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
            output.append("; }\n");
          }}}}{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) { tom.gom.adt.rule.types.Term  tomMatch230_7= (( tom.gom.adt.rule.types.Rule )rule).getlhs() ;if ( (tomMatch230_7 instanceof tom.gom.adt.rule.types.term.Appl) ) {

            genTermList( tomMatch230_7.getargs() ,output);
            output.append(" && ");
            genCondition( (( tom.gom.adt.rule.types.Rule )rule).getcond() ,output);
            output.append(" -> { return `");
            genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
            output.append("; }\n");
          }}}}}

      }
      output.append("    }\n");
    }
    return output.toString();
  }

  private String generateVariadicHookCode(SlotList slotList, RuleList ruleList) {
    StringBuilder output = new StringBuilder();
    output.append("    %match(realMake(head,tail)) {\n");
    while(!ruleList.isEmptyRuleList()) {
      Rule rule = ruleList.getHeadRuleList();
      ruleList = ruleList.getTailRuleList();
      {{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {boolean tomMatch231_11= false ; tom.gom.adt.rule.types.Term  tomMatch231_1= null ;if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.Rule) ) {{tomMatch231_11= true ;tomMatch231_1= (( tom.gom.adt.rule.types.Rule )rule).getlhs() ;}} else {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {{tomMatch231_11= true ;tomMatch231_1= (( tom.gom.adt.rule.types.Rule )rule).getlhs() ;}}}if (tomMatch231_11) {if ( (tomMatch231_1 instanceof tom.gom.adt.rule.types.term.Appl) ) { tom.gom.adt.rule.types.TermList  tomMatch231_4= tomMatch231_1.getargs() ;if ( ((tomMatch231_4 instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || (tomMatch231_4 instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if (!( tomMatch231_4.isEmptyTermList() )) { tom.gom.adt.rule.types.Term  tomMatch231_9= tomMatch231_4.getHeadTermList() ;boolean tomMatch231_10= false ;if ( (tomMatch231_9 instanceof tom.gom.adt.rule.types.term.UnnamedVarStar) ) {tomMatch231_10= true ;} else {if ( (tomMatch231_9 instanceof tom.gom.adt.rule.types.term.VarStar) ) {tomMatch231_10= true ;}}if (tomMatch231_10) { tom.gom.adt.rule.types.Term  tom_var= tomMatch231_4.getHeadTermList() ;

            String varname = "_";
            {{if ( (tom_var instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )tom_var) instanceof tom.gom.adt.rule.types.term.VarStar) ) {
 varname =  (( tom.gom.adt.rule.types.Term )tom_var).getname() ; }}}}
 
            getLogger().log(Level.WARNING, GomMessage.variadicRuleStartingWithStar.getMessage(),
                    new Object[]{( tomMatch231_1.getsymbol() ),varname});
        }}}}}}}}{{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.Rule) ) {



          genTerm( (( tom.gom.adt.rule.types.Rule )rule).getlhs() ,output);
          output.append(" -> { return `");
          genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
          output.append("; }\n");
        }}}{if ( (rule instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )rule) instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {

          genTerm( (( tom.gom.adt.rule.types.Rule )rule).getlhs() ,output);
          output.append(" && ");
          genCondition( (( tom.gom.adt.rule.types.Rule )rule).getcond() ,output);
          output.append(" -> { return `");
          genTerm( (( tom.gom.adt.rule.types.Rule )rule).getrhs() ,output);
          output.append("; }\n");
        }}}}

    }
    output.append("    }\n");
    return output.toString();
  }
  private void genTermList(TermList list, StringBuilder output) {
    {{if ( (list instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )list) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )list) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if ( (( tom.gom.adt.rule.types.TermList )list).isEmptyTermList() ) {
 return; }}}}{if ( (list instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )list) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )list) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if (!( (( tom.gom.adt.rule.types.TermList )list).isEmptyTermList() )) { tom.gom.adt.rule.types.TermList  tom_t= (( tom.gom.adt.rule.types.TermList )list).getTailTermList() ;

        genTerm( (( tom.gom.adt.rule.types.TermList )list).getHeadTermList() ,output);
        if (!tom_t.isEmptyTermList()) {
          output.append(", ");
        }
        genTermList(tom_t,output);
      }}}}}

  }

  private void genTerm(Term termArg, StringBuilder output) {
    {{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.Appl) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).getsymbol() );
        output.append("(");
        genTermList( (( tom.gom.adt.rule.types.Term )termArg).getargs() , output);
        output.append(")");
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.At) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).getname() );
        output.append("@");
        genTerm( (( tom.gom.adt.rule.types.Term )termArg).getterm() ,output);
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.Anti) ) {

        output.append("!");
        genTerm( (( tom.gom.adt.rule.types.Term )termArg).getterm() ,output);
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.UnnamedVar) ) {

        output.append("_");
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.UnnamedVarStar) ) {

        output.append("_*");
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.Var) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).getname() );
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.VarStar) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).getname() );
        output.append("*");
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.BuiltinInt) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).geti() );
      }}}{if ( (termArg instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )termArg) instanceof tom.gom.adt.rule.types.term.BuiltinString) ) {

        output.append( (( tom.gom.adt.rule.types.Term )termArg).gets() );
      }}}}

  }

  private void genCondition(Condition cond, StringBuilder output) {
    {{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondEquals) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" == ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondNotEquals) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" != ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondLessEquals) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" <= ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondLessThan) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" < ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondGreaterEquals) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" >= ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondGreaterThan) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" > ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.CondMatch) ) {

        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett1() ,output);
        output.append(" << ");
        genTerm( (( tom.gom.adt.rule.types.Condition )cond).gett2() ,output);
      }}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) ) {if (!( (  (( tom.gom.adt.rule.types.Condition )cond).isEmptyCondAnd()  ||  ((( tom.gom.adt.rule.types.Condition )cond)== tom.gom.adt.rule.types.condition.EmptyCondAnd.make() )  ) )) { tom.gom.adt.rule.types.Condition  tom_head=(( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )?( (( tom.gom.adt.rule.types.Condition )cond).getHeadCondAnd() ):((( tom.gom.adt.rule.types.Condition )cond))); tom.gom.adt.rule.types.Condition  tom_tail=(( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondAnd) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondAnd)) )?( (( tom.gom.adt.rule.types.Condition )cond).getTailCondAnd() ):( tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ));

        if(tom_tail !=  tom.gom.adt.rule.types.condition.EmptyCondAnd.make() ) {
          output.append("(");
          genCondition(tom_head,output);
          output.append(" && ");
          genCondition(tom_tail,output);
          output.append(")");
        } else {
          genCondition(tom_head,output);
        }
      }}}}{if ( (cond instanceof tom.gom.adt.rule.types.Condition) ) {if ( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) ) {if (!( (  (( tom.gom.adt.rule.types.Condition )cond).isEmptyCondOr()  ||  ((( tom.gom.adt.rule.types.Condition )cond)== tom.gom.adt.rule.types.condition.EmptyCondOr.make() )  ) )) { tom.gom.adt.rule.types.Condition  tom_head=(( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )?( (( tom.gom.adt.rule.types.Condition )cond).getHeadCondOr() ):((( tom.gom.adt.rule.types.Condition )cond))); tom.gom.adt.rule.types.Condition  tom_tail=(( (((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.ConsCondOr) || ((( tom.gom.adt.rule.types.Condition )cond) instanceof tom.gom.adt.rule.types.condition.EmptyCondOr)) )?( (( tom.gom.adt.rule.types.Condition )cond).getTailCondOr() ):( tom.gom.adt.rule.types.condition.EmptyCondOr.make() ));

        if(tom_tail !=  tom.gom.adt.rule.types.condition.EmptyCondOr.make() ) {
          output.append("(");
          genCondition(tom_head,output);
          output.append(" || ");
          genCondition(tom_tail,output);
          output.append(")");
        } else {
          genCondition(tom_head,output);
        }
      }}}}}

  }

  private void matchArgs(SlotList sl, StringBuilder output, int count) {
    {{if ( (sl instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )sl) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )sl) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if ( (( tom.gom.adt.gom.types.SlotList )sl).isEmptyConcSlot() ) {
 return; }}}}{if ( (sl instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )sl) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )sl) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if (!( (( tom.gom.adt.gom.types.SlotList )sl).isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch237_7= (( tom.gom.adt.gom.types.SlotList )sl).getHeadConcSlot() ;if ( (tomMatch237_7 instanceof tom.gom.adt.gom.types.slot.Slot) ) { tom.gom.adt.gom.types.SortDecl  tom_sort= tomMatch237_7.getSort() ; tom.gom.adt.gom.types.SlotList  tom_t= (( tom.gom.adt.gom.types.SlotList )sl).getTailConcSlot() ;{{if ( (tom_sort instanceof tom.gom.adt.gom.types.SortDecl) ) {boolean tomMatch238_3= false ; String  tomMatch238_1= "" ;if ( ((( tom.gom.adt.gom.types.SortDecl )tom_sort) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) {{tomMatch238_3= true ;tomMatch238_1= (( tom.gom.adt.gom.types.SortDecl )tom_sort).getName() ;}} else {if ( ((( tom.gom.adt.gom.types.SortDecl )tom_sort) instanceof tom.gom.adt.gom.types.sortdecl.BuiltinSortDecl) ) {{tomMatch238_3= true ;tomMatch238_1= (( tom.gom.adt.gom.types.SortDecl )tom_sort).getName() ;}}}if (tomMatch238_3) {



            output.append(tomMatch238_1);
            output.append(" arg_"+count);
          }}}}

        if (!tom_t.isEmptyConcSlot()) {
          output.append(", ");
        }
        matchArgs(tom_t,output,count+1);
      }}}}}}

  }

  private SlotList opArgs(SlotList slots, int count) {
    {{if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if ( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() ) {

        return  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ;
      }}}}{if ( (slots instanceof tom.gom.adt.gom.types.SlotList) ) {if ( (((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.ConsConcSlot) || ((( tom.gom.adt.gom.types.SlotList )slots) instanceof tom.gom.adt.gom.types.slotlist.EmptyConcSlot)) ) {if (!( (( tom.gom.adt.gom.types.SlotList )slots).isEmptyConcSlot() )) { tom.gom.adt.gom.types.Slot  tomMatch239_7= (( tom.gom.adt.gom.types.SlotList )slots).getHeadConcSlot() ;if ( (tomMatch239_7 instanceof tom.gom.adt.gom.types.slot.Slot) ) {

        SlotList tail = opArgs( (( tom.gom.adt.gom.types.SlotList )slots).getTailConcSlot() ,count+1);
        return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( tom.gom.adt.gom.types.slot.Slot.make("arg_"+count,  tomMatch239_7.getSort() ) ,tom_append_list_ConcSlot(tail, tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() )) ;
      }}}}}}

    throw new GomRuntimeException("RuleExpander:opArgs failed "+slots);
  }
  protected OperatorDecl getOperatorDecl(String name) {
    OperatorDecl decl = null;
    OpRef ref = new OpRef();
    try {
      tom_make_TopDown(tom_make_GetOperatorDecl(ref,name)).visit(moduleList);
    } catch (tom.library.sl.VisitFailure e) {
      throw new GomRuntimeException("Unexpected strategy failure!");
    }
    if (ref.val == null) {
      getLogger().log(Level.SEVERE, "Unknown constructor {0}",
          new Object[]{name});
    }
    return ref.val;
  }
  static class OpRef { OperatorDecl val; }
  public static class GetOperatorDecl extends tom.library.sl.AbstractStrategyBasic {private  OpRef  opref;private  String  opName;public GetOperatorDecl( OpRef  opref,  String  opName) {super(( new tom.library.sl.Identity() ));this.opref=opref;this.opName=opName;}public  OpRef  getopref() {return opref;}public  String  getopName() {return opName;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.gom.types.OperatorDecl) ) {if ( ((( tom.gom.adt.gom.types.OperatorDecl )tom__arg) instanceof tom.gom.adt.gom.types.operatordecl.OperatorDecl) ) {






        if ( (( tom.gom.adt.gom.types.OperatorDecl )tom__arg).getName() .equals(opName)) {
          opref.val = (( tom.gom.adt.gom.types.OperatorDecl )tom__arg);
        }
      }}}}return _visit_OperatorDecl(tom__arg,introspector);}@SuppressWarnings("unchecked")public  tom.gom.adt.gom.types.OperatorDecl  _visit_OperatorDecl( tom.gom.adt.gom.types.OperatorDecl  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.gom.types.OperatorDecl )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.gom.types.OperatorDecl) ) {return ((T)visit_OperatorDecl((( tom.gom.adt.gom.types.OperatorDecl )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}}private static  tom.library.sl.Strategy  tom_make_GetOperatorDecl( OpRef  t0,  String  t1) { return new GetOperatorDecl(t0,t1);}



  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }
}
