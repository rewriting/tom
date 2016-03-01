/*
 * Gom
 *
 * Copyright (c) 2006-2016, Universite de Lorraine, Inria
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

package tom.gom.compiler;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.gom.tools.GomEnvironment;
import tom.gom.adt.gom.*;
import tom.gom.adt.gom.types.*;
import tom.gom.tools.error.GomRuntimeException;

import tom.gom.adt.objects.*;
import tom.gom.adt.objects.types.*;

import tom.library.sl.*;

public class HookCompiler {

         private static   tom.gom.adt.gom.types.HookDeclList  tom_append_list_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList l1,  tom.gom.adt.gom.types.HookDeclList  l2) {     if( l1.isEmptyConcHookDecl() ) {       return l2;     } else if( l2.isEmptyConcHookDecl() ) {       return l1;     } else if(  l1.getTailConcHookDecl() .isEmptyConcHookDecl() ) {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,l2) ;     } else {       return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( l1.getHeadConcHookDecl() ,tom_append_list_ConcHookDecl( l1.getTailConcHookDecl() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.HookDeclList  tom_get_slice_ConcHookDecl( tom.gom.adt.gom.types.HookDeclList  begin,  tom.gom.adt.gom.types.HookDeclList  end, tom.gom.adt.gom.types.HookDeclList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHookDecl()  ||  (end== tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl.make( begin.getHeadConcHookDecl() ,( tom.gom.adt.gom.types.HookDeclList )tom_get_slice_ConcHookDecl( begin.getTailConcHookDecl() ,end,tail)) ;   }      private static   tom.gom.adt.gom.types.SlotList  tom_append_list_ConcSlot( tom.gom.adt.gom.types.SlotList l1,  tom.gom.adt.gom.types.SlotList  l2) {     if( l1.isEmptyConcSlot() ) {       return l2;     } else if( l2.isEmptyConcSlot() ) {       return l1;     } else if(  l1.getTailConcSlot() .isEmptyConcSlot() ) {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,l2) ;     } else {       return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( l1.getHeadConcSlot() ,tom_append_list_ConcSlot( l1.getTailConcSlot() ,l2)) ;     }   }   private static   tom.gom.adt.gom.types.SlotList  tom_get_slice_ConcSlot( tom.gom.adt.gom.types.SlotList  begin,  tom.gom.adt.gom.types.SlotList  end, tom.gom.adt.gom.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlot()  ||  (end== tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.gom.types.slotlist.ConsConcSlot.make( begin.getHeadConcSlot() ,( tom.gom.adt.gom.types.SlotList )tom_get_slice_ConcSlot( begin.getTailConcSlot() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.HookList  tom_append_list_ConcHook( tom.gom.adt.objects.types.HookList l1,  tom.gom.adt.objects.types.HookList  l2) {     if( l1.isEmptyConcHook() ) {       return l2;     } else if( l2.isEmptyConcHook() ) {       return l1;     } else if(  l1.getTailConcHook() .isEmptyConcHook() ) {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,l2) ;     } else {       return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( l1.getHeadConcHook() ,tom_append_list_ConcHook( l1.getTailConcHook() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.HookList  tom_get_slice_ConcHook( tom.gom.adt.objects.types.HookList  begin,  tom.gom.adt.objects.types.HookList  end, tom.gom.adt.objects.types.HookList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcHook()  ||  (end== tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.hooklist.ConsConcHook.make( begin.getHeadConcHook() ,( tom.gom.adt.objects.types.HookList )tom_get_slice_ConcHook( begin.getTailConcHook() ,end,tail)) ;   }      private static   tom.gom.adt.objects.types.SlotFieldList  tom_append_list_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList l1,  tom.gom.adt.objects.types.SlotFieldList  l2) {     if( l1.isEmptyConcSlotField() ) {       return l2;     } else if( l2.isEmptyConcSlotField() ) {       return l1;     } else if(  l1.getTailConcSlotField() .isEmptyConcSlotField() ) {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,l2) ;     } else {       return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( l1.getHeadConcSlotField() ,tom_append_list_ConcSlotField( l1.getTailConcSlotField() ,l2)) ;     }   }   private static   tom.gom.adt.objects.types.SlotFieldList  tom_get_slice_ConcSlotField( tom.gom.adt.objects.types.SlotFieldList  begin,  tom.gom.adt.objects.types.SlotFieldList  end, tom.gom.adt.objects.types.SlotFieldList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyConcSlotField()  ||  (end== tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( begin.getHeadConcSlotField() ,( tom.gom.adt.objects.types.SlotFieldList )tom_get_slice_ConcSlotField( begin.getTailConcSlotField() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Sequence )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;       } else {         return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Sequence.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.Choice )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;       } else {         return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.Choice.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.SequenceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.SequenceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( l1 instanceof tom.library.sl.ChoiceId )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;       } else {         return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;       }     } else {       return  tom.library.sl.ChoiceId.make(l1,l2) ;     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;   }      private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ));} private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { return (  tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) )  );}private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ) );}private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ) );}   




  public static final String UNEXPECTED_STRATEGY_FAILURE = "Unexpected strategy failure!";

  // myadd-begin
  private GomEnvironment gomEnvironment;

  public HookCompiler(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }

  /* private GomEnvironment environment() {
  //  return GomEnvironment.getInstance();
  }*/

  public GomEnvironment getGomEnvironment() {
    return this.gomEnvironment;
  }

  public void setGomEnvironment(GomEnvironment gomEnvironment) {
    this.gomEnvironment = gomEnvironment;
  }
  // myadd-end

  // Map sortClassNameForSortDecl = environment().builtinSortClassMap();
  //mymodif
  // private static Map sortClassNameForSortDecl;
  private Map sortClassNameForSortDecl;
  HookCompiler(Map sortClassNameForSortDecl) {
    this.sortClassNameForSortDecl = sortClassNameForSortDecl;
  }

  // myadd-begin
  public Map getSortClassNameForSortDecl() {
    return sortClassNameForSortDecl;
  }

  public void setSortClassNameForSortDecl(Map mySortClassNameForSortDecl) {
    this.sortClassNameForSortDecl = mySortClassNameForSortDecl;
  }
  // myadd-end

  /**
    * Process the hooks, and attach them to the correct classes.
    */
  public GomClassList compile(
      HookDeclList declList,
      GomClassList classes,
      Map declToClassName) {
    /* for each hook, find the class, and attach the hook */
    {{if ( (declList instanceof tom.gom.adt.gom.types.HookDeclList) ) {if ( (((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )declList)) instanceof tom.gom.adt.gom.types.hookdecllist.ConsConcHookDecl) || ((( tom.gom.adt.gom.types.HookDeclList )(( tom.gom.adt.gom.types.HookDeclList )declList)) instanceof tom.gom.adt.gom.types.hookdecllist.EmptyConcHookDecl)) ) { tom.gom.adt.gom.types.HookDeclList  tomMatch624_end_4=(( tom.gom.adt.gom.types.HookDeclList )declList);do {{if (!( tomMatch624_end_4.isEmptyConcHookDecl() )) { tom.gom.adt.gom.types.HookDecl  tom_hook= tomMatch624_end_4.getHeadConcHookDecl() ;

        Decl decl = tom_hook.getPointcut();
        {{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutModule) ) {

            ClassName clsName = (ClassName) declToClassName.get( (( tom.gom.adt.gom.types.Decl )decl).getMDecl() );
            try {
              classes = tom_make_TopDown(tom_make_AttachModuleHook(clsName,tom_hook,this)).visit(classes);
                //`TopDown(AttachModuleHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutSort) ) {

            ClassName clsName = (ClassName) declToClassName.get( (( tom.gom.adt.gom.types.Decl )decl).getSort() );
            try {
              classes = tom_make_TopDown(tom_make_AttachSortHook(clsName,tom_hook,this)).visit(classes);
                //`TopDown(AttachSortHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutOperator) ) {

            ClassName clsName = (ClassName) declToClassName.get( (( tom.gom.adt.gom.types.Decl )decl).getODecl() );
            try {
              classes = tom_make_TopDown(tom_make_AttachOperatorHook(clsName,tom_hook,this)).visit(classes);
                //`TopDown(AttachOperatorHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }}}}{if ( (decl instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )decl) instanceof tom.gom.adt.gom.types.Decl) ) {if ( ((( tom.gom.adt.gom.types.Decl )(( tom.gom.adt.gom.types.Decl )decl)) instanceof tom.gom.adt.gom.types.decl.CutFutureOperator) ) { tom.gom.adt.gom.types.Future  tom_consornil= (( tom.gom.adt.gom.types.Decl )decl).getConsOrNil() ;

            ClassName clsName = (ClassName) declToClassName.get( (( tom.gom.adt.gom.types.Decl )decl).getODecl() );
            String prefix = "";
            {{if ( (tom_consornil instanceof tom.gom.adt.gom.types.Future) ) {if ( ((( tom.gom.adt.gom.types.Future )tom_consornil) instanceof tom.gom.adt.gom.types.Future) ) {if ( ((( tom.gom.adt.gom.types.Future )(( tom.gom.adt.gom.types.Future )tom_consornil)) instanceof tom.gom.adt.gom.types.future.FutureNil) ) {
 prefix = "Empty"; }}}}{if ( (tom_consornil instanceof tom.gom.adt.gom.types.Future) ) {if ( ((( tom.gom.adt.gom.types.Future )tom_consornil) instanceof tom.gom.adt.gom.types.Future) ) {if ( ((( tom.gom.adt.gom.types.Future )(( tom.gom.adt.gom.types.Future )tom_consornil)) instanceof tom.gom.adt.gom.types.future.FutureCons) ) {
 prefix = "Cons"; }}}}}

            clsName = clsName.setName(prefix + clsName.getName());
            try {
              classes = tom_make_TopDown(tom_make_AttachOperatorHook(clsName,tom_hook,this)).visit(classes);
                //`TopDown(AttachOperatorHook(clsName,hook)).visit(classes);
            } catch (tom.library.sl.VisitFailure e) {
              throw new GomRuntimeException(HookCompiler.UNEXPECTED_STRATEGY_FAILURE);
            }
          }}}}}

      }if ( tomMatch624_end_4.isEmptyConcHookDecl() ) {tomMatch624_end_4=(( tom.gom.adt.gom.types.HookDeclList )declList);} else {tomMatch624_end_4= tomMatch624_end_4.getTailConcHookDecl() ;}}} while(!( (tomMatch624_end_4==(( tom.gom.adt.gom.types.HookDeclList )declList)) ));}}}}

    return classes;
  }

  //%strategy AttachModuleHook(cName:ClassName,hook:HookDecl)
  public static class AttachModuleHook extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.adt.objects.types.ClassName  cName;private  tom.gom.adt.gom.types.HookDecl  hook;private  tom.gom.compiler.HookCompiler  hc;public AttachModuleHook( tom.gom.adt.objects.types.ClassName  cName,  tom.gom.adt.gom.types.HookDecl  hook,  tom.gom.compiler.HookCompiler  hc) {super(( new tom.library.sl.Identity() ));this.cName=cName;this.hook=hook;this.hc=hc;}public  tom.gom.adt.objects.types.ClassName  getcName() {return cName;}public  tom.gom.adt.gom.types.HookDecl  gethook() {return hook;}public  tom.gom.compiler.HookCompiler  gethc() {return hc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.objects.types.GomClass) ) {return ((T)visit_GomClass((( tom.gom.adt.objects.types.GomClass )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  _visit_GomClass( tom.gom.adt.objects.types.GomClass  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.objects.types.GomClass )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  visit_GomClass( tom.gom.adt.objects.types.GomClass  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tom__arg) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )tom__arg)) instanceof tom.gom.adt.objects.types.gomclass.AbstractTypeClass) ) {



          if ( (( tom.gom.adt.objects.types.GomClass )tom__arg).getClassName() == cName) {
            return
              (( tom.gom.adt.objects.types.GomClass )tom__arg).setHooks( tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook( (( tom.gom.adt.objects.types.GomClass )tom__arg).getHooks() , tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }}}}}return _visit_GomClass(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_AttachModuleHook( tom.gom.adt.objects.types.ClassName  t0,  tom.gom.adt.gom.types.HookDecl  t1,  tom.gom.compiler.HookCompiler  t2) { return new AttachModuleHook(t0,t1,t2);}



  //%strategy AttachSortHook(cName:ClassName,hook:HookDecl)
  public static class AttachSortHook extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.adt.objects.types.ClassName  cName;private  tom.gom.adt.gom.types.HookDecl  hook;private  tom.gom.compiler.HookCompiler  hc;public AttachSortHook( tom.gom.adt.objects.types.ClassName  cName,  tom.gom.adt.gom.types.HookDecl  hook,  tom.gom.compiler.HookCompiler  hc) {super(( new tom.library.sl.Identity() ));this.cName=cName;this.hook=hook;this.hc=hc;}public  tom.gom.adt.objects.types.ClassName  getcName() {return cName;}public  tom.gom.adt.gom.types.HookDecl  gethook() {return hook;}public  tom.gom.compiler.HookCompiler  gethc() {return hc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.objects.types.GomClass) ) {return ((T)visit_GomClass((( tom.gom.adt.objects.types.GomClass )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  _visit_GomClass( tom.gom.adt.objects.types.GomClass  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.objects.types.GomClass )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  visit_GomClass( tom.gom.adt.objects.types.GomClass  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tom__arg) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )tom__arg)) instanceof tom.gom.adt.objects.types.gomclass.SortClass) ) {



          if ( (( tom.gom.adt.objects.types.GomClass )tom__arg).getClassName() == cName) {
            return
              (( tom.gom.adt.objects.types.GomClass )tom__arg).setHooks( tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook( (( tom.gom.adt.objects.types.GomClass )tom__arg).getHooks() , tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }}}}}return _visit_GomClass(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_AttachSortHook( tom.gom.adt.objects.types.ClassName  t0,  tom.gom.adt.gom.types.HookDecl  t1,  tom.gom.compiler.HookCompiler  t2) { return new AttachSortHook(t0,t1,t2);}public static class AttachOperatorHook extends tom.library.sl.AbstractStrategyBasic {private  tom.gom.adt.objects.types.ClassName  cName;private  tom.gom.adt.gom.types.HookDecl  hook;private  tom.gom.compiler.HookCompiler  hc;public AttachOperatorHook( tom.gom.adt.objects.types.ClassName  cName,  tom.gom.adt.gom.types.HookDecl  hook,  tom.gom.compiler.HookCompiler  hc) {super(( new tom.library.sl.Identity() ));this.cName=cName;this.hook=hook;this.hc=hc;}public  tom.gom.adt.objects.types.ClassName  getcName() {return cName;}public  tom.gom.adt.gom.types.HookDecl  gethook() {return hook;}public  tom.gom.compiler.HookCompiler  gethc() {return hc;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChildren = new tom.library.sl.Visitable[getChildCount()];stratChildren[0] = super.getChildAt(0);return stratChildren;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.gom.adt.objects.types.GomClass) ) {return ((T)visit_GomClass((( tom.gom.adt.objects.types.GomClass )v),introspector));}if (!(  null ==environment )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  _visit_GomClass( tom.gom.adt.objects.types.GomClass  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!(  null ==environment )) {return (( tom.gom.adt.objects.types.GomClass )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);}}@SuppressWarnings("unchecked")public  tom.gom.adt.objects.types.GomClass  visit_GomClass( tom.gom.adt.objects.types.GomClass  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tom__arg) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )tom__arg)) instanceof tom.gom.adt.objects.types.gomclass.VariadicOperatorClass) ) { tom.gom.adt.objects.types.HookList  tom_oldHooks= (( tom.gom.adt.objects.types.GomClass )tom__arg).getHooks() ; tom.gom.adt.objects.types.GomClass  tom_emptyClass= (( tom.gom.adt.objects.types.GomClass )tom__arg).getEmpty() ; tom.gom.adt.objects.types.GomClass  tom_consClass= (( tom.gom.adt.objects.types.GomClass )tom__arg).getCons() ; tom.gom.adt.objects.types.GomClass  tom_obj=(( tom.gom.adt.objects.types.GomClass )tom__arg);








            if ( (( tom.gom.adt.objects.types.GomClass )tom__arg).getClassName() == cName) {
              /* We may want to attach the hook to the cons or empty */
              if (hook.isMakeHookDecl()) {
                if (hook.getSlotArgs() !=  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) {
                  HookList oldConsHooks = tom_consClass.getHooks();
                  GomClass newCons =
                    tom_consClass.setHooks(
                         tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(oldConsHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                        //`ConcHook(makeHooksFromHookDecl(hook),oldConsHooks*));
                  return tom_obj.setCons(newCons);
                } else if (hook.getSlotArgs() ==  tom.gom.adt.gom.types.slotlist.EmptyConcSlot.make() ) {
                  HookList oldEmptyHooks = tom_emptyClass.getHooks();
                  GomClass newEmpty =
                    tom_emptyClass.setHooks(
                         tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(oldEmptyHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                        //`ConcHook(makeHooksFromHookDecl(hook),oldEmptyHooks*));
                  return tom_obj.setEmpty(newEmpty);
                }
              } else if (hook.isImportHookDecl()) {
                /* We will want to attach the hook directly to the 3 classes */
                /* in case we use these imports for the corresponding Make hooks */
                HookList oldConsHooks = tom_consClass.getHooks();
                GomClass newCons =
                  tom_consClass.setHooks(
                       tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(oldConsHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                      //`ConcHook(makeHooksFromHookDecl(hook),oldConsHooks*));
                HookList oldEmptyHooks = tom_emptyClass.getHooks();
                GomClass newEmpty =
                  tom_emptyClass.setHooks(
                       tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(oldEmptyHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                      //`ConcHook(makeHooksFromHookDecl(hook),oldEmptyHooks*));
                GomClass newobj = tom_obj.setEmpty(newEmpty);
                newobj = newobj.setCons(newCons);
                return newobj.setHooks( tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(tom_oldHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                //return `newobj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
              } else {
                return
                  tom_obj.setHooks( tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook(tom_oldHooks, tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
                  //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
              }
            }
          }}}}{if ( (tom__arg instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )tom__arg) instanceof tom.gom.adt.objects.types.GomClass) ) {if ( ((( tom.gom.adt.objects.types.GomClass )(( tom.gom.adt.objects.types.GomClass )tom__arg)) instanceof tom.gom.adt.objects.types.gomclass.OperatorClass) ) {

          if ( (( tom.gom.adt.objects.types.GomClass )tom__arg).getClassName() == cName) {
            return
              (( tom.gom.adt.objects.types.GomClass )tom__arg).setHooks( tom.gom.adt.objects.types.hooklist.ConsConcHook.make(hc.makeHooksFromHookDecl(hook),tom_append_list_ConcHook( (( tom.gom.adt.objects.types.GomClass )tom__arg).getHooks() , tom.gom.adt.objects.types.hooklist.EmptyConcHook.make() )) );
              //`obj.setHooks(`ConcHook(makeHooksFromHookDecl(hook),oldHooks*));
          }
        }}}}}return _visit_GomClass(tom__arg,introspector);}}private static  tom.library.sl.Strategy  tom_make_AttachOperatorHook( tom.gom.adt.objects.types.ClassName  t0,  tom.gom.adt.gom.types.HookDecl  t1,  tom.gom.compiler.HookCompiler  t2) { return new AttachOperatorHook(t0,t1,t2);}



  // private static Hook makeHooksFromHookDecl(HookDecl hookDecl) {
  private Hook makeHooksFromHookDecl(HookDecl hookDecl) {
    {{if ( (hookDecl instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )hookDecl) instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )(( tom.gom.adt.gom.types.HookDecl )hookDecl)) instanceof tom.gom.adt.gom.types.hookdecl.MakeHookDecl) ) {

        SlotFieldList newArgs = makeSlotFieldListFromSlotList( (( tom.gom.adt.gom.types.HookDecl )hookDecl).getSlotArgs() );
        return  tom.gom.adt.objects.types.hook.MakeHook.make(newArgs,  (( tom.gom.adt.gom.types.HookDecl )hookDecl).getCode() ,  (( tom.gom.adt.gom.types.HookDecl )hookDecl).getHasTomCode() ) ;
      }}}}{if ( (hookDecl instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )hookDecl) instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )(( tom.gom.adt.gom.types.HookDecl )hookDecl)) instanceof tom.gom.adt.gom.types.hookdecl.BlockHookDecl) ) {

        return  tom.gom.adt.objects.types.hook.BlockHook.make( (( tom.gom.adt.gom.types.HookDecl )hookDecl).getCode() ,  (( tom.gom.adt.gom.types.HookDecl )hookDecl).getHasTomCode() ) ;
      }}}}{if ( (hookDecl instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )hookDecl) instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )(( tom.gom.adt.gom.types.HookDecl )hookDecl)) instanceof tom.gom.adt.gom.types.hookdecl.InterfaceHookDecl) ) {

        return  tom.gom.adt.objects.types.hook.InterfaceHook.make( (( tom.gom.adt.gom.types.HookDecl )hookDecl).getCode() ) ;
      }}}}{if ( (hookDecl instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )hookDecl) instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )(( tom.gom.adt.gom.types.HookDecl )hookDecl)) instanceof tom.gom.adt.gom.types.hookdecl.ImportHookDecl) ) {

        return  tom.gom.adt.objects.types.hook.ImportHook.make( (( tom.gom.adt.gom.types.HookDecl )hookDecl).getCode() ) ;
      }}}}{if ( (hookDecl instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )hookDecl) instanceof tom.gom.adt.gom.types.HookDecl) ) {if ( ((( tom.gom.adt.gom.types.HookDecl )(( tom.gom.adt.gom.types.HookDecl )hookDecl)) instanceof tom.gom.adt.gom.types.hookdecl.MappingHookDecl) ) {

        return  tom.gom.adt.objects.types.hook.MappingHook.make( (( tom.gom.adt.gom.types.HookDecl )hookDecl).getCode() ) ;
      }}}}}

    throw new GomRuntimeException(
        "Hook declaration " + hookDecl+ " not processed");
  }

  // private static SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
  private SlotFieldList makeSlotFieldListFromSlotList(SlotList args) {
    SlotFieldList newArgs =  tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ;
    while(!args.isEmptyConcSlot()) {
      Slot arg = args.getHeadConcSlot();
      args = args.getTailConcSlot();
      {{if ( (arg instanceof tom.gom.adt.gom.types.Slot) ) {if ( ((( tom.gom.adt.gom.types.Slot )arg) instanceof tom.gom.adt.gom.types.Slot) ) {if ( ((( tom.gom.adt.gom.types.Slot )(( tom.gom.adt.gom.types.Slot )arg)) instanceof tom.gom.adt.gom.types.slot.Slot) ) {

          ClassName slotClassName = (ClassName)
            // sortClassNameForSortDecl.get(`sortDecl);
            getSortClassNameForSortDecl().get( (( tom.gom.adt.gom.types.Slot )arg).getSort() );
          newArgs = tom_append_list_ConcSlotField(newArgs, tom.gom.adt.objects.types.slotfieldlist.ConsConcSlotField.make( tom.gom.adt.objects.types.slotfield.SlotField.make( (( tom.gom.adt.gom.types.Slot )arg).getName() , slotClassName) , tom.gom.adt.objects.types.slotfieldlist.EmptyConcSlotField.make() ) );
        }}}}}

    }
    return newArgs;
  }
}
