/*
 *
 * GOM
 *
 * Copyright (c) 2007-2012, INPL, INRIA
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
 * Emilie Balland e-mail: Emilie.Balland@loria.fr
 *
 **/

package tom.gom.expander.rule;

import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.tree.Tree;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import tom.gom.tools.error.GomRuntimeException;
import tom.gom.tools.GomEnvironment;
import tom.gom.SymbolTable;
import tom.gom.GomMessage;

import tom.gom.adt.gom.types.*;
import tom.gom.adt.rule.RuleAdaptor;
import tom.gom.adt.rule.types.*;
import tom.gom.adt.rule.types.term.*;
import tom.gom.adt.objects.types.ClassName;

import tom.library.sl.*;

public class GraphRuleExpander {

         private static   tom.gom.adt.gom.types.ModuleList  tom_append_list_ConcModule( tom.gom.adt.gom.types.ModuleList l1,  tom.gom.adt.gom.types.ModuleList  l2) {     if( l1.isEmptyConcModule() ) {       return l2;     } else if( l2.isEmptyConcModule() ) {       return l1;     } else if(  l1.getTailConcModule() .isEmptyConcModule() ) {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,l2) ;     } else {       return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( l1.getHeadConcModule() ,tom_append_list_ConcModule( l1.getTailConcModule() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.ModuleList  tom_get_slice_ConcModule( tom.gom.adt.gom.types.ModuleList  begin,  tom.gom.adt.gom.types.ModuleList  end, tom.gom.adt.gom.types.ModuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcModule()  ||  (end== tom.gom.adt.gom.types.modulelist.EmptyConcModule.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.modulelist.ConsConcModule.make( begin.getHeadConcModule() ,( tom.gom.adt.gom.types.ModuleList )tom_get_slice_ConcModule( begin.getTailConcModule() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SortList  tom_append_list_ConcSort( tom.gom.adt.gom.types.SortList l1,  tom.gom.adt.gom.types.SortList  l2) {     if( l1.isEmptyConcSort() ) {       return l2;     } else if( l2.isEmptyConcSort() ) {       return l1;     } else if(  l1.getTailConcSort() .isEmptyConcSort() ) {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,l2) ;     } else {       return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( l1.getHeadConcSort() ,tom_append_list_ConcSort( l1.getTailConcSort() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SortList  tom_get_slice_ConcSort( tom.gom.adt.gom.types.SortList  begin,  tom.gom.adt.gom.types.SortList  end, tom.gom.adt.gom.types.SortList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSort()  ||  (end== tom.gom.adt.gom.types.sortlist.EmptyConcSort.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.sortlist.ConsConcSort.make( begin.getHeadConcSort() ,( tom.gom.adt.gom.types.SortList )tom_get_slice_ConcSort( begin.getTailConcSort() ,end,tail)) ;   }       private static   tom.gom.adt.rule.types.RuleList  tom_append_list_RuleList( tom.gom.adt.rule.types.RuleList l1,  tom.gom.adt.rule.types.RuleList  l2) {     if( l1.isEmptyRuleList() ) {       return l2;     } else if( l2.isEmptyRuleList() ) {       return l1;     } else if(  l1.getTailRuleList() .isEmptyRuleList() ) {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( l1.getHeadRuleList() ,tom_append_list_RuleList( l1.getTailRuleList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.RuleList  tom_get_slice_RuleList( tom.gom.adt.rule.types.RuleList  begin,  tom.gom.adt.rule.types.RuleList  end, tom.gom.adt.rule.types.RuleList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyRuleList()  ||  (end== tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.rulelist.ConsRuleList.make( begin.getHeadRuleList() ,( tom.gom.adt.rule.types.RuleList )tom_get_slice_RuleList( begin.getTailRuleList() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.TermList  tom_append_list_TermList( tom.gom.adt.rule.types.TermList l1,  tom.gom.adt.rule.types.TermList  l2) {     if( l1.isEmptyTermList() ) {       return l2;     } else if( l2.isEmptyTermList() ) {       return l1;     } else if(  l1.getTailTermList() .isEmptyTermList() ) {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,l2) ;     } else {       return  tom.gom.adt.rule.types.termlist.ConsTermList.make( l1.getHeadTermList() ,tom_append_list_TermList( l1.getTailTermList() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.TermList  tom_get_slice_TermList( tom.gom.adt.rule.types.TermList  begin,  tom.gom.adt.rule.types.TermList  end, tom.gom.adt.rule.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyTermList()  ||  (end== tom.gom.adt.rule.types.termlist.EmptyTermList.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.termlist.ConsTermList.make( begin.getHeadTermList() ,( tom.gom.adt.rule.types.TermList )tom_get_slice_TermList( begin.getTailTermList() ,end,tail)) ;   }      private static   tom.gom.adt.rule.types.Term  tom_append_list_PathTerm( tom.gom.adt.rule.types.Term l1,  tom.gom.adt.rule.types.Term  l2) {     if( l1.isEmptyPathTerm() ) {       return l2;     } else if( l2.isEmptyPathTerm() ) {       return l1;     } else if(  l1.getTailPathTerm() .isEmptyPathTerm() ) {       return  tom.gom.adt.rule.types.term.ConsPathTerm.make( l1.getHeadPathTerm() ,l2) ;     } else {       return  tom.gom.adt.rule.types.term.ConsPathTerm.make( l1.getHeadPathTerm() ,tom_append_list_PathTerm( l1.getTailPathTerm() ,l2)) ;     }   }   private static   tom.gom.adt.rule.types.Term  tom_get_slice_PathTerm( tom.gom.adt.rule.types.Term  begin,  tom.gom.adt.rule.types.Term  end, tom.gom.adt.rule.types.Term  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyPathTerm()  ||  (end== tom.gom.adt.rule.types.term.EmptyPathTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.rule.types.term.ConsPathTerm.make( begin.getHeadPathTerm() ,( tom.gom.adt.rule.types.Term )tom_get_slice_PathTerm( begin.getTailPathTerm() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_InnermostIdSeq( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.AllSeq(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make( tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) , null ) ) ) ) );}      static class MapEntry {   private Object key;   private Object val;   public Object getKey() { return key; }   public Object getVal() { return val; }   public MapEntry(Object key, Object val) {     this.key = key;     this.val = val;   } }       @SuppressWarnings("unchecked") private static java.util.HashMap hashMapAppend(MapEntry e, java.util.HashMap m) {   java.util.HashMap res = (java.util.HashMap) m.clone();   res.put(e.getKey(), e.getVal());   return res; }  @SuppressWarnings("unchecked") private static MapEntry hashMapGetHead(java.util.HashMap m) {   java.util.Set es = m.entrySet();   java.util.Iterator it = es.iterator();   java.util.Map.Entry e = (java.util.Map.Entry) it.next();   return new MapEntry(e.getKey(), e.getValue()); }  @SuppressWarnings("unchecked") private static java.util.HashMap hashMapGetTail(java.util.HashMap m) {   java.util.HashMap res = (java.util.HashMap) m.clone();   java.util.Set es = m.entrySet();   java.util.Iterator it = es.iterator();   java.util.Map.Entry e = (java.util.Map.Entry) it.next();   res.remove(e.getKey());   return res; }   




  private ModuleList moduleList;
  private String sortname;
  private String moduleName;
  private String pkgName;
  private GomEnvironment gomEnvironment;

  public GraphRuleExpander(ModuleList data, GomEnvironment gomEnvironment) {
    moduleList = data;
    this.gomEnvironment = gomEnvironment;
  }

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  private static String fullClassName(ClassName clsName) {
    {{if ( (((Object)clsName) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )((Object)clsName)) instanceof tom.gom.adt.objects.types.ClassName) ) {if ( ((( tom.gom.adt.objects.types.ClassName )(( tom.gom.adt.objects.types.ClassName )((Object)clsName))) instanceof tom.gom.adt.objects.types.classname.ClassName) ) { String  tom_pkgPrefix= (( tom.gom.adt.objects.types.ClassName )((Object)clsName)).getPkg() ; String  tom_name= (( tom.gom.adt.objects.types.ClassName )((Object)clsName)).getName() ;

        if(tom_pkgPrefix.length()==0) {
          return tom_name;
        } else {
          return tom_pkgPrefix+"."+tom_name;
        }
      }}}}}

    throw new GomRuntimeException(
        "GomReferenceExpander:fullClassName got a strange ClassName "+clsName);
  }

  public HookDeclList expandGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    this.sortname = sortname;
    {{if ( (((Object)sdecl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )((Object)sdecl)) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )((Object)sdecl))) instanceof tom.gom.adt.gom.types.decl.CutSort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch645_1= (( tom.gom.adt.gom.types.Decl )((Object)sdecl)).getSort() ;if ( (tomMatch645_1 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch645_1) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch645_4= tomMatch645_1.getModuleDecl() ;if ( (tomMatch645_4 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch645_4) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch645_7= tomMatch645_4.getModuleName() ;if ( (tomMatch645_7 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch645_7) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) {

        this.moduleName =  tomMatch645_7.getName() ;
        this.pkgName =  tomMatch645_4.getPkg() ;
      }}}}}}}}}}}


    RuleLexer lexer = new RuleLexer(new ANTLRStringStream(ruleCode));
    CommonTokenStream tokens = new CommonTokenStream(lexer);
    RuleParser parser = new RuleParser(tokens);
    RuleList rulelist =  tom.gom.adt.rule.types.rulelist.EmptyRuleList.make() ;
    try {
      Tree ast = (Tree)parser.graphruleset().getTree();
      rulelist = (RuleList) RuleAdaptor.getTerm(ast);
    } catch (org.antlr.runtime.RecognitionException e) {
      GomMessage.error(getLogger(), null, 0, GomMessage.rulesParsingFailure);
      return  tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ;
    }
    return expand(rulelist,stratname,defaultstrat,sdecl);
  }

  public HookDeclList expandFirstGraphRules(String sortname, String stratname, String defaultstrat, String ruleCode, Decl sdecl) {
    HookDeclList expandedrules = expandGraphRules(sortname,stratname,defaultstrat,ruleCode, sdecl);
    HookDeclList commonpart = expandFirst(sdecl);
    return tom_append_list_ConcHookDecl(commonpart,tom_append_list_ConcHookDecl(expandedrules, tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ));
  }

  //add the common methods, includes and imports for all graphrule strategies of a sort
  protected HookDeclList expandFirst(Decl sdecl) {
    ClassName abstractType =  tom.gom.adt.objects.types.classname.ClassName.make(pkgName+"."+moduleName.toLowerCase(), moduleName+"AbstractType") ;
    ClassName visitor =  tom.gom.adt.objects.types.classname.ClassName.make(pkgName+"."+moduleName.toLowerCase(), moduleName+"Visitor") ;

    StringBuilder output = new StringBuilder();
    output.append(
        "\n%include { sl.tom }\n%include { java/util/ArrayList.tom}\n%typeterm tom_StringList {\n  implement      { java.util.List<String> }\n  is_sort(t)     { $t instanceof java.util.List }\n  equals(l1,l2)  { $l1.equals($l2) }\n}\n%typeterm tom_StringPositionMap {\n  implement      { java.util.Map<String,Position> }\n  is_sort(t)      { $t instanceof java.util.Map }\n  equals(l1,l2)  { $l1.equals($l2) }\n}\n\n%typeterm tom_SharedLabel {\n  implement {SharedLabel}\n  is_sort(t) { ($t instanceof SharedLabel) }\n}\n\nstatic class SharedLabel {\n  public Position posLhs;\n  public Position posRhs;\n  public String label;\n\n  public SharedLabel(String label, Position posLhs, Position posRhs) {\n    this.label = label;\n    this.posLhs = posLhs;\n    this.posRhs = posRhs;\n  }\n}\n\n%op tom_"+abstractType.getName()+" Subst(global:tom_"+abstractType.getName()+",subst:tom_"+abstractType.getName()+") {\n  is_fsym(t) {( $t instanceof Subst )}\n  make(t1,t2) {( new Subst($t1,$t2) )}\n}\n\n%typeterm tom_"+abstractType.getName()+" {\n  implement { "+fullClassName(abstractType)+" }\n  is_sort(t) {( $t instanceof "+fullClassName(abstractType)+" )}\n}\n\nstatic class Subst extends "+fullClassName(abstractType)+" {\n\n  "+fullClassName(abstractType)+" substitution,globalterm;\n\n  public Subst("+fullClassName(abstractType)+" globalterm, "+fullClassName(abstractType)+" substitution) {\n    this.substitution = substitution;\n    this.globalterm = globalterm;\n  }\n\n  //abstract methods from the abstractType which are trivially implemented\n  //they must never be used\n\n  public aterm.ATerm toATerm() {\n    return null;\n  }\n\n  public String symbolName() { return \"@\"; }\n\n  public void toStringBuilder(java.lang.StringBuilder buffer) {\n    buffer.append(\"@(\");\n    buffer.append(globalterm);\n    buffer.append(\",\");\n    buffer.append(substitution);\n    buffer.append(\")\");\n  }\n\n  public int compareTo(Object o) { return 0; }\n\n  public int compareToLPO(Object o) { return 0; }\n\n  public final int hashCode() {\n    return 0;\n  }\n\n  public final boolean equivalent(shared.SharedObject obj) {\n    return false;\n  }\n\n  public shared.SharedObject duplicate() {\n    return this;\n  }\n\n  //implementation of the Visitable interface\n  public Visitable setChildren(Visitable[] children) {\n    if (children.length == 2){\n      return new Subst(("+fullClassName(abstractType)+")children[0],("+fullClassName(abstractType)+")children[1]);\n    } else {\n      throw new IndexOutOfBoundsException();\n    }\n  }\n\n  public Visitable[] getChildren() {\n    return new Visitable[]{globalterm,substitution};\n  }\n\n  public Visitable getChildAt(int i) {\n    switch(i) {\n      case 0: return globalterm;\n      case 1: return substitution;\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n  public Visitable setChildAt(int i, Visitable child) {\n    switch(i) {\n      case 0: return new Subst(("+fullClassName(abstractType)+")child,substitution);\n      case 1: return new Subst(globalterm,("+fullClassName(abstractType)+")child);\n      default: throw new IndexOutOfBoundsException();\n    }\n  }\n\n    public int getChildCount() {\n      return 2;\n    }\n\n}\n\n  protected static java.util.List<SharedLabel> getSharedLabels("+fullClassName(abstractType)+" labelledLhs, "+fullClassName(abstractType)+" labelledRhs) {\n    java.util.ArrayList<SharedLabel> sharedlabels = new java.util.ArrayList<SharedLabel>();\n    java.util.HashMap<String,Position> lhsLabels = labelledLhs.getMapFromLabelToPositionAndRemoveLabels();\n    java.util.HashMap<String,Position> rhsLabels = labelledRhs.getMapFromLabelToPositionAndRemoveLabels();\n    for (String labelRhs: rhsLabels.keySet()) {\n      if (lhsLabels.containsKey(labelRhs)) {\n        sharedlabels.add(new SharedLabel(labelRhs,lhsLabels.get(labelRhs),rhsLabels.get(labelRhs)));\n      }\n    }\n   return sharedlabels;\n  }\n\n %strategy FromVarToPath(lhs:tom_"+abstractType.getName()+",omega:Position) extends Identity() {\n\n"



































































































































);

  {{if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch646__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch646__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch646_8= tomMatch646__end__4.getHeadConcModule() ;if ( (tomMatch646_8 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch646_8) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch646_7= tomMatch646_8.getSorts() ;if ( (((( tom.gom.adt.gom.types.SortList )tomMatch646_7) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch646_7) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch646__end__13=tomMatch646_7;do {{if (!( tomMatch646__end__13.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch646_17= tomMatch646__end__13.getHeadConcSort() ;if ( (tomMatch646_17 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch646_17) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch646_16= tomMatch646_17.getDecl() ;if ( (tomMatch646_16 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch646_16) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { String  tom_name= tomMatch646_16.getName() ;

    output.append(
        "\n    visit "+tom_name+" {\n      Var"+tom_name+"(name) -> {\n        Position wl = getVarPos(lhs,`name);\n        Position wr = getEnvironment().getPosition();\n        Position wwl = (Position) (Position.makeFromArray(new int[]{1})).add(omega).add(wl);\n        Position wwr = (Position) (Position.makeFromArray(new int[]{2})).add(wr);\n        Position res = (Position) wwl.sub(wwr);\n        return Path"+tom_name+".make(res);\n      }\n    }\n"










);
      }}}}}if ( tomMatch646__end__13.isEmptyConcSort() ) {tomMatch646__end__13=tomMatch646_7;} else {tomMatch646__end__13= tomMatch646__end__13.getTailConcSort() ;}}} while(!( (tomMatch646__end__13==tomMatch646_7) ));}}}}if ( tomMatch646__end__4.isEmptyConcModule() ) {tomMatch646__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch646__end__4= tomMatch646__end__4.getTailConcModule() ;}}} while(!( (tomMatch646__end__4==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}


  output.append("\n  }\n\n  private static Position getVarPos("+fullClassName(abstractType)+" term, String varname) {\n    ArrayList<Position> list = new ArrayList<Position>();\n    try {\n      `OnceTopDown(GetVarPos(list,varname)).visit(term);\n      return list.get(0);\n    } catch (VisitFailure e) {\n      throw new RuntimeException(\"Unexpected strategy failure!\");\n      }\n  }\n\n  %strategy GetVarPos(ArrayList l, String varname) extends Fail() {\n"













);

 {{if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch647__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch647__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch647_8= tomMatch647__end__4.getHeadConcModule() ;if ( (tomMatch647_8 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch647_8) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch647_7= tomMatch647_8.getSorts() ;if ( (((( tom.gom.adt.gom.types.SortList )tomMatch647_7) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch647_7) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch647__end__13=tomMatch647_7;do {{if (!( tomMatch647__end__13.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch647_17= tomMatch647__end__13.getHeadConcSort() ;if ( (tomMatch647_17 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch647_17) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch647_16= tomMatch647_17.getDecl() ;if ( (tomMatch647_16 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch647_16) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { String  tom_name= tomMatch647_16.getName() ;

 output.append(
        "\n    visit "+tom_name+" {\n      v@Var"+tom_name+"(name) -> {\n        if(`name.equals(varname)) {\n          l.add(Position.makeFromPath(getEnvironment().getPosition()));\n          return `v;\n        }\n      }\n    }\n   "








);
   }}}}}if ( tomMatch647__end__13.isEmptyConcSort() ) {tomMatch647__end__13=tomMatch647_7;} else {tomMatch647__end__13= tomMatch647__end__13.getTailConcSort() ;}}} while(!( (tomMatch647__end__13==tomMatch647_7) ));}}}}if ( tomMatch647__end__4.isEmptyConcModule() ) {tomMatch647__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch647__end__4= tomMatch647__end__4.getTailConcModule() ;}}} while(!( (tomMatch647__end__4==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}


  output.append("\n  }\n"

);

 output.append(
        "\n  %strategy InlinePath() extends Identity() {\n"

);

  {{if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch648__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch648__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch648_8= tomMatch648__end__4.getHeadConcModule() ;if ( (tomMatch648_8 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch648_8) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.SortList  tomMatch648_7= tomMatch648_8.getSorts() ;if ( (((( tom.gom.adt.gom.types.SortList )tomMatch648_7) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch648_7) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch648__end__13=tomMatch648_7;do {{if (!( tomMatch648__end__13.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch648_17= tomMatch648__end__13.getHeadConcSort() ;if ( (tomMatch648_17 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch648_17) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch648_16= tomMatch648_17.getDecl() ;if ( (tomMatch648_16 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch648_16) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { String  tom_name= tomMatch648_16.getName() ;

    output.append(
        "\n    visit "+tom_name+" {\n    //match non empty paths\n      p1@Path"+tom_name+"(_,_*) -> {\n        getEnvironment().followPath((Path)`p1);\n        "+tom_name+" pointedSubterm = ("+tom_name+") getEnvironment().getSubject();\n        %match(pointedSubterm) {\n          p2@Path"+tom_name+"(_,_*) -> {\n            getEnvironment().followPath(((Path)`p1).inverse());\n            return ("+tom_name+") ((Path)`p1).add((Path)`p2).getCanonicalPath();\n          }\n        }\n        getEnvironment().followPath(((Path)`p1).inverse());\n      }\n   }\n"














);
    }}}}}if ( tomMatch648__end__13.isEmptyConcSort() ) {tomMatch648__end__13=tomMatch648_7;} else {tomMatch648__end__13= tomMatch648__end__13.getTailConcSort() ;}}} while(!( (tomMatch648__end__13==tomMatch648_7) ));}}}}if ( tomMatch648__end__4.isEmptyConcModule() ) {tomMatch648__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch648__end__4= tomMatch648__end__4.getTailConcModule() ;}}} while(!( (tomMatch648__end__4==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}


  output.append("\n  }\n"

);

  String imports = "\nimport tom.library.sl.*;\nimport java.util.ArrayList;\n   "


;

  //import all the constructors Path<Sort> of the module
  {{if ( (((Object)moduleList) instanceof tom.gom.adt.gom.types.ModuleList) ) {if ( (((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.ConsConcModule) || ((( tom.gom.adt.gom.types.ModuleList )(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) instanceof tom.gom.adt.gom.types.modulelist.EmptyConcModule)) ) { tom.gom.adt.gom.types.ModuleList  tomMatch649__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));do {{if (!( tomMatch649__end__4.isEmptyConcModule() )) { tom.gom.adt.gom.types.Module  tomMatch649_9= tomMatch649__end__4.getHeadConcModule() ;if ( (tomMatch649_9 instanceof tom.gom.adt.gom.types.Module) ) {if ( ((( tom.gom.adt.gom.types.Module )tomMatch649_9) instanceof tom.gom.adt.gom.types.module.Module) ) { tom.gom.adt.gom.types.ModuleDecl  tomMatch649_7= tomMatch649_9.getMDecl() ; tom.gom.adt.gom.types.SortList  tomMatch649_8= tomMatch649_9.getSorts() ;if ( (tomMatch649_7 instanceof tom.gom.adt.gom.types.ModuleDecl) ) {if ( ((( tom.gom.adt.gom.types.ModuleDecl )tomMatch649_7) instanceof tom.gom.adt.gom.types.moduledecl.ModuleDecl) ) { tom.gom.adt.gom.types.GomModuleName  tomMatch649_11= tomMatch649_7.getModuleName() ;if ( (tomMatch649_11 instanceof tom.gom.adt.gom.types.GomModuleName) ) {if ( ((( tom.gom.adt.gom.types.GomModuleName )tomMatch649_11) instanceof tom.gom.adt.gom.types.gommodulename.GomModuleName) ) { String  tom_pkg= tomMatch649_7.getPkg() ;if ( (((( tom.gom.adt.gom.types.SortList )tomMatch649_8) instanceof tom.gom.adt.gom.types.sortlist.ConsConcSort) || ((( tom.gom.adt.gom.types.SortList )tomMatch649_8) instanceof tom.gom.adt.gom.types.sortlist.EmptyConcSort)) ) { tom.gom.adt.gom.types.SortList  tomMatch649__end__21=tomMatch649_8;do {{if (!( tomMatch649__end__21.isEmptyConcSort() )) { tom.gom.adt.gom.types.Sort  tomMatch649_25= tomMatch649__end__21.getHeadConcSort() ;if ( (tomMatch649_25 instanceof tom.gom.adt.gom.types.Sort) ) {if ( ((( tom.gom.adt.gom.types.Sort )tomMatch649_25) instanceof tom.gom.adt.gom.types.sort.Sort) ) { tom.gom.adt.gom.types.SortDecl  tomMatch649_24= tomMatch649_25.getDecl() ;if ( (tomMatch649_24 instanceof tom.gom.adt.gom.types.SortDecl) ) {if ( ((( tom.gom.adt.gom.types.SortDecl )tomMatch649_24) instanceof tom.gom.adt.gom.types.sortdecl.SortDecl) ) { String  tom_name= tomMatch649_24.getName() ;


  String prefix = ((tom_pkg=="")?"":tom_pkg+".")+moduleName.toLowerCase();
  imports += "\nimport "+prefix+".types."+tom_name.toLowerCase()+".Path"+tom_name+";\n  "

;
    }}}}}if ( tomMatch649__end__21.isEmptyConcSort() ) {tomMatch649__end__21=tomMatch649_8;} else {tomMatch649__end__21= tomMatch649__end__21.getTailConcSort() ;}}} while(!( (tomMatch649__end__21==tomMatch649_8) ));}}}}}}}}if ( tomMatch649__end__4.isEmptyConcModule() ) {tomMatch649__end__4=(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList));} else {tomMatch649__end__4= tomMatch649__end__4.getTailConcModule() ;}}} while(!( (tomMatch649__end__4==(( tom.gom.adt.gom.types.ModuleList )((Object)moduleList))) ));}}}}


   return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(sdecl,  tom.gom.adt.code.types.code.Code.make(output.toString()) ,  true ) , tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.ImportHookDecl.make(sdecl,  tom.gom.adt.code.types.code.Code.make(imports) ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ) ;
  }

  protected HookDeclList expand(RuleList rulelist, String stratname, String defaultstrat, Decl sdecl) {
    ClassName abstractType =  tom.gom.adt.objects.types.classname.ClassName.make(pkgName+"."+moduleName.toLowerCase(), moduleName+"AbstractType") ;

    StringBuilder output = new StringBuilder();
    output.append("\n  public static Strategy "+stratname+"() {\n    return `"+stratname+"();\n  }\n\n  %strategy "+stratname+"() extends "+defaultstrat+"() {\n    visit "+sortname+" {\n      "






);

        {{if ( (((Object)rulelist) instanceof tom.gom.adt.rule.types.RuleList) ) {if ( (((( tom.gom.adt.rule.types.RuleList )(( tom.gom.adt.rule.types.RuleList )((Object)rulelist))) instanceof tom.gom.adt.rule.types.rulelist.ConsRuleList) || ((( tom.gom.adt.rule.types.RuleList )(( tom.gom.adt.rule.types.RuleList )((Object)rulelist))) instanceof tom.gom.adt.rule.types.rulelist.EmptyRuleList)) ) { tom.gom.adt.rule.types.RuleList  tomMatch650__end__4=(( tom.gom.adt.rule.types.RuleList )((Object)rulelist));do {{if (!( tomMatch650__end__4.isEmptyRuleList() )) { tom.gom.adt.rule.types.Rule  tomMatch650_9= tomMatch650__end__4.getHeadRuleList() ;boolean tomMatch650_12= false ; tom.gom.adt.rule.types.Term  tomMatch650_8= null ; tom.gom.adt.rule.types.Rule  tomMatch650_10= null ; tom.gom.adt.rule.types.Rule  tomMatch650_11= null ; tom.gom.adt.rule.types.Term  tomMatch650_7= null ;if ( (tomMatch650_9 instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )tomMatch650_9) instanceof tom.gom.adt.rule.types.rule.Rule) ) {{tomMatch650_12= true ;tomMatch650_10=tomMatch650_9;tomMatch650_7= tomMatch650_10.getlhs() ;tomMatch650_8= tomMatch650_10.getrhs() ;}} else {if ( (tomMatch650_9 instanceof tom.gom.adt.rule.types.Rule) ) {if ( ((( tom.gom.adt.rule.types.Rule )tomMatch650_9) instanceof tom.gom.adt.rule.types.rule.ConditionalRule) ) {{tomMatch650_12= true ;tomMatch650_11=tomMatch650_9;tomMatch650_7= tomMatch650_11.getlhs() ;tomMatch650_8= tomMatch650_11.getrhs() ;}}}}}if (tomMatch650_12) { tom.gom.adt.rule.types.Term  tom_lhs=tomMatch650_7;

            //TODO: verify that the lhs of the rules are of the good sort
            //TODO: verify the linearity of lhs and rhs
            output.append("\n                "+genTerm(tom_lhs)+" -> {\n\n                /* 1. set needed positions */\n                Position omega = getEnvironment().getPosition();\n                Position posFinal = Position.makeFromArray(new int[]{1});\n                Position posRhs = Position.makeFromArray(new int[]{2});\n                Position newomega = (Position) posFinal.add(omega);\n\n                /* 2. go to the root and get the global term-graph */\n                getEnvironment().followPath(omega.inverse());\n                "+fullClassName(abstractType)+" subject = ("+fullClassName(abstractType)+") getEnvironment().getSubject();\n\n                /* 2. construct at compile-time the lhs and rhs */\n                "+fullClassName(abstractType)+" labelledLhs = `"+genTermWithExplicitVar(tom_lhs,"root",0)+".normalizeWithLabels();\n                "+fullClassName(abstractType)+" labelledRhs = `"+genTermWithExplicitVar(tomMatch650_8,"root",0)+".normalizeWithLabels();\n\n               /* 3. construct t = SubstTerm(subject',r') */\n                "+fullClassName(abstractType)+" rhs = labelledRhs.label2path();\n                "+fullClassName(abstractType)+" lhs = labelledLhs.label2path();\n                rhs = `TopDown(FromVarToPath(lhs,omega)).visit(rhs);\n                "+fullClassName(abstractType)+" t = `Subst(subject,rhs);\n                //replace in subject every pointer to the position newomega by\n                //a pointer to the position 2  and if in position 2 there is also a\n                //pointer inline the paths.\n                t = posFinal.getOmega(`TopDown(Sequence(globalRedirection(newomega,posRhs),InlinePath()))).visit(t);\n                //inline paths in the intermediate r\n                t = posRhs.getOmega(`TopDown(InlinePath())).visit(t);\n\n                //compute the list of all shared labels\n                java.util.List<SharedLabel> sharedlabels = getSharedLabels(labelledLhs,labelledRhs);\n\n                //redirect paths for shared labels\n                for (SharedLabel sharedlabel: sharedlabels) {\n                  Position l = (Position) newomega.add(sharedlabel.posLhs);\n                  Position r = (Position) posRhs.add(sharedlabel.posRhs);\n                  t =  t.applyGlobalRedirection(l,r);\n                }\n\n                /* 4. set the global term to swap(t,1.w,2) */\n                t = t.swap(newomega,posRhs);\n\n                /* 5. set the global term to swap(t,1.w,2) */\n                t = t.normalize();\n\n                /* 6. get the first child */\n                t = posFinal.getSubterm().visit(t);\n\n                //expand the subject to remove labels from the rhs\n                getEnvironment().setSubject(t.expand());\n\n                /* 5. go to the position w */\n                getEnvironment().followPath(omega);\n                return ("+sortname+") getEnvironment().getSubject();\n                }\n                "






















































);
          }}if ( tomMatch650__end__4.isEmptyRuleList() ) {tomMatch650__end__4=(( tom.gom.adt.rule.types.RuleList )((Object)rulelist));} else {tomMatch650__end__4= tomMatch650__end__4.getTailRuleList() ;}}} while(!( (tomMatch650__end__4==(( tom.gom.adt.rule.types.RuleList )((Object)rulelist))) ));}}}}


        output.append("\n    }\n  }\n            "


);

          return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( tom.gom.adt.gom.types.hookdecl.BlockHookDecl.make(sdecl,  tom.gom.adt.code.types.code.Code.make(output.toString()) ,  true ) , tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) ;
  }

  private String genTerm(Term term) {
    StringBuilder output = new StringBuilder();
    term = expand(term);
    {{if ( (((Object)term) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if (!( (( tom.gom.adt.rule.types.Term )((Object)term)).isEmptyPathTerm() )) { tom.gom.adt.rule.types.Term  tom_tail= (( tom.gom.adt.rule.types.Term )((Object)term)).getTailPathTerm() ;

        output.append("Path"+sortname);
        output.append("(");
        output.append( (( tom.gom.adt.rule.types.Term )((Object)term)).getHeadPathTerm() );
        {{if ( (((Object)tom_tail) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) { tom.gom.adt.rule.types.Term  tomMatch652__end__4=(( tom.gom.adt.rule.types.Term )((Object)tom_tail));do {{if (!( tomMatch652__end__4.isEmptyPathTerm() )) {

            output.append(",");
            output.append( tomMatch652__end__4.getHeadPathTerm() );
          }if ( tomMatch652__end__4.isEmptyPathTerm() ) {tomMatch652__end__4=(( tom.gom.adt.rule.types.Term )((Object)tom_tail));} else {tomMatch652__end__4= tomMatch652__end__4.getTailPathTerm() ;}}} while(!( (tomMatch652__end__4==(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) ));}}}}

        output.append(")");
      }}}}{if ( (((Object)term) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)term)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.Appl) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)term)).getsymbol() );
        output.append("(");
        output.append(genTermList( (( tom.gom.adt.rule.types.Term )((Object)term)).getargs() ));
        output.append(")");
      }}}}{if ( (((Object)term) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)term)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.Var) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)term)).getname() );
      }}}}{if ( (((Object)term) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)term)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.BuiltinInt) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)term)).geti() );
      }}}}{if ( (((Object)term) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)term)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)term))) instanceof tom.gom.adt.rule.types.term.BuiltinString) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)term)).gets() );
      }}}}}

    return output.toString();
  }

  private String genTermList(TermList list) {
    StringBuilder output = new StringBuilder();
    {{if ( (((Object)list) instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if ( (( tom.gom.adt.rule.types.TermList )((Object)list)).isEmptyTermList() ) {
 return ""; }}}}{if ( (((Object)list) instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if (!( (( tom.gom.adt.rule.types.TermList )((Object)list)).isEmptyTermList() )) { tom.gom.adt.rule.types.TermList  tom_t= (( tom.gom.adt.rule.types.TermList )((Object)list)).getTailTermList() ;

        output.append(genTerm( (( tom.gom.adt.rule.types.TermList )((Object)list)).getHeadTermList() ));
        if (!tom_t.isEmptyTermList()) {
          output.append(", ");
        }
        output.append(genTermList(tom_t));
      }}}}}

    return output.toString();
  }

  private String genTermWithExplicitVar(Term termArg, String fathersymbol, int omega) {
    StringBuilder output = new StringBuilder();
    {{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.LabTerm) ) {

        output.append("Lab"+sortname);
        output.append("(");
        output.append("\""+ (( tom.gom.adt.rule.types.Term )((Object)termArg)).getl() +"\"");
        output.append(",");
        output.append(genTermWithExplicitVar( (( tom.gom.adt.rule.types.Term )((Object)termArg)).gett() ,fathersymbol,omega));
        output.append(")");
      }}}}{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.RefTerm) ) {

        output.append("Ref"+sortname);
        output.append("(");
        output.append("\""+ (( tom.gom.adt.rule.types.Term )((Object)termArg)).getl() +"\"");
        output.append(")");
      }}}}{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.Appl) ) { String  tom_symbol= (( tom.gom.adt.rule.types.Term )((Object)termArg)).getsymbol() ;

        output.append(tom_symbol);
        output.append("(");
        output.append(genTermListWithExplicitVar(tom_symbol, (( tom.gom.adt.rule.types.Term )((Object)termArg)).getargs() ));
        output.append(")");
      }}}}{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.Var) ) { String  tom_name= (( tom.gom.adt.rule.types.Term )((Object)termArg)).getname() ;

        //the variable must be replaced by a meta representation of the var
        //in the signature of the corresponding  sort
        //test if the variable is not at the root position
        if(omega!=0) {
          String sortvar = getGomEnvironment().getSymbolTable().getChildSort(fathersymbol,omega);
          output.append("Var"+sortvar+"(\""+tom_name+"\")");
        } else {
          //it is necessary of the sort declared for the strategy
          output.append("Var"+sortname+"(\""+tom_name+"\")");
        }
      }}}}{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.BuiltinInt) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)termArg)).geti() );
      }}}}{if ( (((Object)termArg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)termArg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)termArg))) instanceof tom.gom.adt.rule.types.term.BuiltinString) ) {

        output.append( (( tom.gom.adt.rule.types.Term )((Object)termArg)).gets() );
      }}}}}

    return output.toString();
  }

  private String genTermListWithExplicitVar(String fathersymbol, TermList list) {
    StringBuilder output = new StringBuilder();
    int omega=1;
    {{if ( (((Object)list) instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) {if ( (( tom.gom.adt.rule.types.TermList )((Object)list)).isEmptyTermList() ) {
 return ""; }}}}{if ( (((Object)list) instanceof tom.gom.adt.rule.types.TermList) ) {if ( (((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.ConsTermList) || ((( tom.gom.adt.rule.types.TermList )(( tom.gom.adt.rule.types.TermList )((Object)list))) instanceof tom.gom.adt.rule.types.termlist.EmptyTermList)) ) { tom.gom.adt.rule.types.TermList  tomMatch655__end__6=(( tom.gom.adt.rule.types.TermList )((Object)list));do {{if (!( tomMatch655__end__6.isEmptyTermList() )) {

        output.append(genTermWithExplicitVar( tomMatch655__end__6.getHeadTermList() ,fathersymbol,omega));
        omega++;
        if (! tomMatch655__end__6.getTailTermList() .isEmptyTermList()) {
          output.append(", ");
        }
      }if ( tomMatch655__end__6.isEmptyTermList() ) {tomMatch655__end__6=(( tom.gom.adt.rule.types.TermList )((Object)list));} else {tomMatch655__end__6= tomMatch655__end__6.getTailTermList() ;}}} while(!( (tomMatch655__end__6==(( tom.gom.adt.rule.types.TermList )((Object)list))) ));}}}}

    return output.toString();
  }


  public static Term expand(Term t) {
    java.util.HashMap map = new java.util.HashMap();
    Term tt = null;
    try {
      tt = tom_make_InnermostIdSeq(tom_make_NormalizeLabel(map)).visit(t);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
    return label2path(tt);
  }

  




  static class Info {
    public Position omegaRef;
    public Term sharedTerm;
  }

  public static class CollectSubterm extends tom.library.sl.AbstractStrategyBasic {private  String  label;private Info info;public CollectSubterm( String  label, Info info) {super(( new tom.library.sl.Fail() ));this.label=label;this.info=info;}public  String  getlabel() {return label;}public Info getinfo() {return info;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)tom__arg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.LabTerm) ) { String  tom_label= (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getl() ;


        Position current = getEnvironment().getPosition();
        if (label.equals(tom_label)) {
          //test if it is not a cycle
          if (!info.omegaRef.hasPrefix(current)) {
            //return a ref
            info.sharedTerm =  (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).gett() ;
            return  tom.gom.adt.rule.types.term.RefTerm.make(tom_label) ;
          }
          else {
            //do not return a ref and stop to collect
            return (( tom.gom.adt.rule.types.Term )((Object)tom__arg));
          }
        }
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectSubterm( String  t0, Info t1) { return new CollectSubterm(t0,t1);}public static class NormalizeLabel extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public NormalizeLabel( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)tom__arg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.RefTerm) ) { String  tom_label= (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getl() ;






        if (! map.containsKey(tom_label)){
          Position old = getEnvironment().getPosition();
          Position rootpos = Position.make();
          Info info = new Info();
          info.omegaRef = old;
          getEnvironment().followPath(rootpos.sub(getEnvironment().getPosition()));
          tom_make_OnceTopDown(tom_make_CollectSubterm(tom_label,info)).visit(getEnvironment());
          getEnvironment().followPath(old.sub(getEnvironment().getPosition()));
          //test if is is not a ref to a cycle
          if (info.sharedTerm!=null) {
            map.put(tom_label,old);
            return  tom.gom.adt.rule.types.term.LabTerm.make(tom_label, info.sharedTerm) ;
          }
        }
      }}}}{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)tom__arg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.LabTerm) ) {

        map.put( (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getl() ,getEnvironment().getPosition());
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_NormalizeLabel( java.util.HashMap  t0) { return new NormalizeLabel(t0);}



  public static Term label2path(Term t) {
    java.util.HashMap map = new java.util.HashMap();
    try {
      return  tom.library.sl.Sequence.make(tom_make_RepeatId(tom_make_OnceTopDownId(tom_make_CollectLabels(map))), tom.library.sl.Sequence.make(tom_make_TopDown(tom_make_Label2Path(map)), null ) ) .visit(t);
    } catch (tom.library.sl.VisitFailure e) {
      throw new tom.gom.tools.error.GomRuntimeException("Unexpected strategy failure!");
    }
  }

  public static class CollectLabels extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public CollectLabels( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)tom__arg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.LabTerm) ) {


        map.put( (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getl() ,getEnvironment().getPosition());
        return  (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).gett() ;
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_CollectLabels( java.util.HashMap  t0) { return new CollectLabels(t0);}public static class PathForPattern extends tom.library.sl.AbstractStrategyBasic {public PathForPattern() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if (!( (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).isEmptyPathTerm() )) { int  tomMatch659_4= (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getHeadPathTerm() ;if ( true ) {if ( -1==tomMatch659_4 ) {







        return tom_make_PathForPattern().visit( (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getTailPathTerm() );
      }}}}}}{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__12=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));do {{ tom.gom.adt.rule.types.Term  tomMatch659_8=tom_get_slice_PathTerm((( tom.gom.adt.rule.types.Term )((Object)tom__arg)),tomMatch659__end__12, tom.gom.adt.rule.types.term.EmptyPathTerm.make() );if (!( tomMatch659__end__12.isEmptyPathTerm() )) { int  tomMatch659_15= tomMatch659__end__12.getHeadPathTerm() ;if ( true ) {if ( -1==tomMatch659_15 ) { tom.gom.adt.rule.types.Term  tom_sublist=tomMatch659_8;if ( (((Object)tom_sublist) instanceof tom.gom.adt.rule.types.Term) ) {boolean tomMatch659_28= false ;if ( (((( tom.gom.adt.rule.types.Term )tomMatch659_8) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )tomMatch659_8) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (tom_sublist==tomMatch659_8) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__20=tomMatch659_8;do {{if (!( tomMatch659__end__20.isEmptyPathTerm() )) {boolean tomMatch659_29= false ; int  tomMatch659_25= tomMatch659__end__20.getHeadPathTerm() ;if ( true ) {if ( -2==tomMatch659_25 ) {tomMatch659_29= true ;}}if (!(tomMatch659_29)) {tomMatch659_28= true ;}}if ( tomMatch659__end__20.isEmptyPathTerm() ) {tomMatch659__end__20=tomMatch659_8;} else {tomMatch659__end__20= tomMatch659__end__20.getTailPathTerm() ;}}} while(!( (tomMatch659__end__20==tomMatch659_8) ));}}if (!(tomMatch659_28)) {boolean tomMatch659_27= false ;if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_sublist)).isEmptyPathTerm() ) {tomMatch659_27= true ;}}if (!(tomMatch659_27)) {



        //TODO: avoid the compilation warning
        // because it generates a  disjunction which is not fully supported with associative operators
        int upcount = tom_sublist.length();
        Term newtail = tom_make_PathForPattern().visit( tomMatch659__end__12.getTailPathTerm() );
        return  tom.gom.adt.rule.types.term.ConsPathTerm.make(-upcount,tom_append_list_PathTerm(newtail, tom.gom.adt.rule.types.term.EmptyPathTerm.make() )) ;
      }}}}}}if ( tomMatch659__end__12.isEmptyPathTerm() ) {tomMatch659__end__12=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));} else {tomMatch659__end__12= tomMatch659__end__12.getTailPathTerm() ;}}} while(!( (tomMatch659__end__12==(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) ));}}}{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__39=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));do {{ tom.gom.adt.rule.types.Term  tomMatch659_35=tom_get_slice_PathTerm((( tom.gom.adt.rule.types.Term )((Object)tom__arg)),tomMatch659__end__39, tom.gom.adt.rule.types.term.EmptyPathTerm.make() ); tom.gom.adt.rule.types.Term  tom_tail=tomMatch659__end__39; tom.gom.adt.rule.types.Term  tom_sublist=tomMatch659_35;if ( (((Object)tom_tail) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if (!( (( tom.gom.adt.rule.types.Term )((Object)tom_tail)).isEmptyPathTerm() )) { int  tomMatch659_48= (( tom.gom.adt.rule.types.Term )((Object)tom_tail)).getHeadPathTerm() ;if ( true ) {if ( 1==tomMatch659_48 ) {if ( (((Object)tom_sublist) instanceof tom.gom.adt.rule.types.Term) ) {boolean tomMatch659_63= false ;if ( (((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (tom_sublist==tomMatch659_35) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__55=tomMatch659_35;do {{if (!( tomMatch659__end__55.isEmptyPathTerm() )) {boolean tomMatch659_64= false ; int  tomMatch659_60= tomMatch659__end__55.getHeadPathTerm() ;if ( true ) {if ( -2==tomMatch659_60 ) {tomMatch659_64= true ;}}if (!(tomMatch659_64)) {tomMatch659_63= true ;}}if ( tomMatch659__end__55.isEmptyPathTerm() ) {tomMatch659__end__55=tomMatch659_35;} else {tomMatch659__end__55= tomMatch659__end__55.getTailPathTerm() ;}}} while(!( (tomMatch659__end__55==tomMatch659_35) ));}}if (!(tomMatch659_63)) {boolean tomMatch659_62= false ;if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_sublist)).isEmptyPathTerm() ) {tomMatch659_62= true ;}}if (!(tomMatch659_62)) {




        int downcount = tom_sublist.length();
        Term newtail = tom_make_PathForPattern().visit(tom_tail);
        return  tom.gom.adt.rule.types.term.ConsPathTerm.make(-downcount,tom_append_list_PathTerm(newtail, tom.gom.adt.rule.types.term.EmptyPathTerm.make() )) ;
      }}}}}}}}if ( (((Object)tom_tail) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if (!( (( tom.gom.adt.rule.types.Term )((Object)tom_tail)).isEmptyPathTerm() )) { int  tomMatch659_50= (( tom.gom.adt.rule.types.Term )((Object)tom_tail)).getHeadPathTerm() ;if ( true ) {if ( 2==tomMatch659_50 ) {if ( (((Object)tom_sublist) instanceof tom.gom.adt.rule.types.Term) ) {boolean tomMatch659_63= false ;if ( (((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (tom_sublist==tomMatch659_35) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__55=tomMatch659_35;do {{if (!( tomMatch659__end__55.isEmptyPathTerm() )) {boolean tomMatch659_64= false ; int  tomMatch659_60= tomMatch659__end__55.getHeadPathTerm() ;if ( true ) {if ( -2==tomMatch659_60 ) {tomMatch659_64= true ;}}if (!(tomMatch659_64)) {tomMatch659_63= true ;}}if ( tomMatch659__end__55.isEmptyPathTerm() ) {tomMatch659__end__55=tomMatch659_35;} else {tomMatch659__end__55= tomMatch659__end__55.getTailPathTerm() ;}}} while(!( (tomMatch659__end__55==tomMatch659_35) ));}}if (!(tomMatch659_63)) {boolean tomMatch659_62= false ;if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_sublist)).isEmptyPathTerm() ) {tomMatch659_62= true ;}}if (!(tomMatch659_62)) {         int downcount = tom_sublist.length();         Term newtail = tom_make_PathForPattern().visit(tom_tail);         return  tom.gom.adt.rule.types.term.ConsPathTerm.make(-downcount,tom_append_list_PathTerm(newtail, tom.gom.adt.rule.types.term.EmptyPathTerm.make() )) ;       }}}}}}}}if ( (((Object)tom_tail) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_tail))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_tail)).isEmptyPathTerm() ) {if ( (((Object)tom_sublist) instanceof tom.gom.adt.rule.types.Term) ) {boolean tomMatch659_63= false ;if ( (((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )tomMatch659_35) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (tom_sublist==tomMatch659_35) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__55=tomMatch659_35;do {{if (!( tomMatch659__end__55.isEmptyPathTerm() )) {boolean tomMatch659_64= false ; int  tomMatch659_60= tomMatch659__end__55.getHeadPathTerm() ;if ( true ) {if ( -2==tomMatch659_60 ) {tomMatch659_64= true ;}}if (!(tomMatch659_64)) {tomMatch659_63= true ;}}if ( tomMatch659__end__55.isEmptyPathTerm() ) {tomMatch659__end__55=tomMatch659_35;} else {tomMatch659__end__55= tomMatch659__end__55.getTailPathTerm() ;}}} while(!( (tomMatch659__end__55==tomMatch659_35) ));}}if (!(tomMatch659_63)) {boolean tomMatch659_62= false ;if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_sublist)).isEmptyPathTerm() ) {tomMatch659_62= true ;}}if (!(tomMatch659_62)) {         int downcount = tom_sublist.length();         Term newtail = tom_make_PathForPattern().visit(tom_tail);         return  tom.gom.adt.rule.types.term.ConsPathTerm.make(-downcount,tom_append_list_PathTerm(newtail, tom.gom.adt.rule.types.term.EmptyPathTerm.make() )) ;       }}}}}}if ( tomMatch659__end__39.isEmptyPathTerm() ) {tomMatch659__end__39=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));} else {tomMatch659__end__39= tomMatch659__end__39.getTailPathTerm() ;}}} while(!( (tomMatch659__end__39==(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) ));}}}{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__71=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));do {{ tom.gom.adt.rule.types.Term  tomMatch659_67=tom_get_slice_PathTerm((( tom.gom.adt.rule.types.Term )((Object)tom__arg)),tomMatch659__end__71, tom.gom.adt.rule.types.term.EmptyPathTerm.make() );if (!( tomMatch659__end__71.isEmptyPathTerm() )) { int  tomMatch659_74= tomMatch659__end__71.getHeadPathTerm() ;if ( true ) {if ( 1==tomMatch659_74 ) { tom.gom.adt.rule.types.Term  tom_sublist=tomMatch659_67;if ( (((Object)tom_sublist) instanceof tom.gom.adt.rule.types.Term) ) {boolean tomMatch659_87= false ;if ( (((( tom.gom.adt.rule.types.Term )tomMatch659_67) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )tomMatch659_67) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (tom_sublist==tomMatch659_67) ) { tom.gom.adt.rule.types.Term  tomMatch659__end__79=tomMatch659_67;do {{if (!( tomMatch659__end__79.isEmptyPathTerm() )) {boolean tomMatch659_88= false ; int  tomMatch659_84= tomMatch659__end__79.getHeadPathTerm() ;if ( true ) {if ( 2==tomMatch659_84 ) {tomMatch659_88= true ;}}if (!(tomMatch659_88)) {tomMatch659_87= true ;}}if ( tomMatch659__end__79.isEmptyPathTerm() ) {tomMatch659__end__79=tomMatch659_67;} else {tomMatch659__end__79= tomMatch659__end__79.getTailPathTerm() ;}}} while(!( (tomMatch659__end__79==tomMatch659_67) ));}}if (!(tomMatch659_87)) {boolean tomMatch659_86= false ;if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom_sublist))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) {if ( (( tom.gom.adt.rule.types.Term )((Object)tom_sublist)).isEmptyPathTerm() ) {tomMatch659_86= true ;}}if (!(tomMatch659_86)) {



        int downcount = tom_sublist.length();
        Term newtail = tom_make_PathForPattern().visit( tomMatch659__end__71.getTailPathTerm() );
        return  tom.gom.adt.rule.types.term.ConsPathTerm.make(downcount,tom_append_list_PathTerm(newtail, tom.gom.adt.rule.types.term.EmptyPathTerm.make() )) ;
      }}}}}}if ( tomMatch659__end__71.isEmptyPathTerm() ) {tomMatch659__end__71=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));} else {tomMatch659__end__71= tomMatch659__end__71.getTailPathTerm() ;}}} while(!( (tomMatch659__end__71==(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) ));}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_PathForPattern() { return new PathForPattern();}public static class Normalize extends tom.library.sl.AbstractStrategyBasic {public Normalize() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( (((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.ConsPathTerm) || ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.EmptyPathTerm)) ) { tom.gom.adt.rule.types.Term  tomMatch660__end__4=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));do {{if (!( tomMatch660__end__4.isEmptyPathTerm() )) { tom.gom.adt.rule.types.Term  tomMatch660_5= tomMatch660__end__4.getTailPathTerm() ;if (!( tomMatch660_5.isEmptyPathTerm() )) {






        if ( tomMatch660__end__4.getHeadPathTerm() ==- tomMatch660_5.getHeadPathTerm() ) {
          return tom_append_list_PathTerm(tom_get_slice_PathTerm((( tom.gom.adt.rule.types.Term )((Object)tom__arg)),tomMatch660__end__4, tom.gom.adt.rule.types.term.EmptyPathTerm.make() ),tom_append_list_PathTerm( tomMatch660_5.getTailPathTerm() , tom.gom.adt.rule.types.term.EmptyPathTerm.make() ));
        }
      }}if ( tomMatch660__end__4.isEmptyPathTerm() ) {tomMatch660__end__4=(( tom.gom.adt.rule.types.Term )((Object)tom__arg));} else {tomMatch660__end__4= tomMatch660__end__4.getTailPathTerm() ;}}} while(!( (tomMatch660__end__4==(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) ));}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_Normalize() { return new Normalize();}public static class Label2Path extends tom.library.sl.AbstractStrategyBasic {private  java.util.HashMap  map;public Label2Path( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.rule.types.Term) ) {return ((T)visit_Term((( tom.gom.adt.rule.types.Term )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  _visit_Term( tom.gom.adt.rule.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.rule.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.rule.types.Term  visit_Term( tom.gom.adt.rule.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (((Object)tom__arg) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )((Object)tom__arg)) instanceof tom.gom.adt.rule.types.Term) ) {if ( ((( tom.gom.adt.rule.types.Term )(( tom.gom.adt.rule.types.Term )((Object)tom__arg))) instanceof tom.gom.adt.rule.types.term.RefTerm) ) { String  tom_label= (( tom.gom.adt.rule.types.Term )((Object)tom__arg)).getl() ;






        if (map.containsKey(tom_label)) {
          Path target = (Path) map.get(tom_label);
          Path ref = target.sub(getEnvironment().getPosition());
          //warning: do not normalize ref because we need to transform paths that go to the root
          //construct the PathTerm corresponding to the position ref
          Term path =  tom.gom.adt.rule.types.term.EmptyPathTerm.make() ;
          int head;
          while(ref.length()!=0) {
            head = ref.getHead();
            ref  = ref.getTail();
            path = tom_append_list_PathTerm(path, tom.gom.adt.rule.types.term.ConsPathTerm.make(head, tom.gom.adt.rule.types.term.EmptyPathTerm.make() ) );
          }
          //transform the path to obtain the corresponding one in the pattern
          return  tom.library.sl.Sequence.make(tom_make_PathForPattern(), tom.library.sl.Sequence.make(tom_make_RepeatId(tom_make_Normalize()), null ) ) .visitLight(path);
        }
      }}}}}return _visit_Term(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_Label2Path( java.util.HashMap  t0) { return new Label2Path(t0);}




  private Logger getLogger() {
    return Logger.getLogger(getClass().getName());
  }

}
