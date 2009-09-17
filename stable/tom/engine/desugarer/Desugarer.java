/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2009, INRIA
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
 * Pierre-Etienne Moreau  e-mail: Pierre-Etienne.Moreau@loria.fr
 *
 **/

package tom.engine.desugarer;

import java.util.Map;
import java.util.logging.Level;
import java.util.Iterator;
import java.util.ArrayList;

import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomname.types.tomname.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.tomtype.types.tomtypelist.concTomType;
import tom.engine.adt.tomterm.types.tomlist.concTomTerm;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.tools.ASTFactory;
import tom.engine.tools.TomGenericPlugin;
import tom.engine.tools.Tools;
import tom.engine.tools.SymbolTable;
import tom.engine.xml.Constants;
import tom.platform.OptionParser;
import tom.platform.adt.platformoption.types.PlatformOptionList;
import aterm.ATerm;

import tom.library.sl.*;

/**
 * The Desugarer plugin.
 * Perform syntax expansion and more.
 */
public class Desugarer extends TomGenericPlugin {

        private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.ChoiceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ):new tom.library.sl.ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.ChoiceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin):new tom.library.sl.ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( (begin instanceof tom.library.sl.ChoiceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.ChoiceId(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.ChoiceId(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) ;}   


  public Desugarer() {
    super("Desugarer");
  }

  public void run(Map informationTracker) {
    // long startChrono = System.currentTimeMillis();
    try {

      Code syntaxExpandedTerm = (Code) getWorkingTerm();

      // replace underscores by fresh variables
      syntaxExpandedTerm = 
        tom_make_TopDown(tom_make_DesugarUnderscore(this)).visitLight(syntaxExpandedTerm);

      //replace TermAppl and XmlAppl by RecordAppl
      syntaxExpandedTerm = 
        tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this)).visitLight(syntaxExpandedTerm);

      setWorkingTerm(syntaxExpandedTerm);      

    } catch (Exception e) {
      getLogger().log( Level.SEVERE, TomMessage.exceptionMessage.getMessage(),
          new Object[]{
          getClass().getName(), 
          getStreamManager().getInputFileName(), 
          e.getMessage()} );
      e.printStackTrace();
      return;
    }
  }

  // FIXME : generate truly fresh variables
  private int freshCounter = 0;
  private TomName getFreshVariable() {
    freshCounter++;
    return  tom.engine.adt.tomname.types.tomname.Name.make("_f_r_e_s_h_v_a_r_" + freshCounter) ;
  }

  

  /* replaces  _  by a fresh variable
     _* by a fresh varstar    */
  public static class DesugarUnderscore extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.Desugarer  desugarer;public DesugarUnderscore( tom.engine.desugarer.Desugarer  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.Desugarer  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {


        return  tom.engine.adt.tomterm.types.tomterm.Variable.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() , desugarer.getFreshVariable(),  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ) ;
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {

        return  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() , desugarer.getFreshVariable(),  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ) ;
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_DesugarUnderscore( tom.engine.desugarer.Desugarer  t0) { return new DesugarUnderscore(t0);}



  /*
   * The 'replaceTermApplTomSyntax' phase replaces:
   * - each 'TermAppl' by its typed record form:
   *    placeholders are not removed
   *    slotName are attached to arguments
   */
  public static class replaceTermApplTomSyntax extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.desugarer.Desugarer  desugarer;public replaceTermApplTomSyntax( tom.engine.desugarer.Desugarer  desugarer) {super(( new tom.library.sl.Identity() ));this.desugarer=desugarer;}public  tom.engine.desugarer.Desugarer  getdesugarer() {return desugarer;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {


        return desugarer.replaceTermAppl( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getArgs() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() );
      }}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {


        //System.out.println("replaceXML in:\n" + subject);
        return desugarer.replaceXMLAppl( (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAttrList() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getChildList() , (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() );
      }}}}return _visit_TomTerm(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replaceTermApplTomSyntax( tom.engine.desugarer.Desugarer  t0) { return new replaceTermApplTomSyntax(t0);}



  /*
   * replace 'TermAppl' by its 'RecordAppl' form
   * when no slotName exits, the position becomes the slotName
   */
  protected TomTerm replaceTermAppl(OptionList option, TomNameList nameList, TomList args, ConstraintList constraints) {
    TomName headName = nameList.getHeadconcTomName();
    if(headName instanceof AntiName) {
      headName = ((AntiName)headName).getName();
    }
    String opName = headName.getString();
    TomSymbol tomSymbol = getSymbolFromName(opName);


    //System.out.println("replaceTermAppl: " + tomSymbol);
    //System.out.println("  nameList = " + nameList);

    if(tomSymbol==null && args.isEmptyconcTomTerm()) {
      return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(option, nameList,  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() , constraints) ;
    }

    SlotList slotList =  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    Strategy typeStrategy = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this));
    if(opName.equals("") || tomSymbol==null || TomBase.isListOperator(tomSymbol) || TomBase.isArrayOperator(tomSymbol)) {
      for(TomTerm arg:(concTomTerm)args) {
        try {
          TomTerm subterm = typeStrategy.visitLight(arg);
          TomName slotName =  tom.engine.adt.tomname.types.tomname.EmptyName.make() ;
          /*
           * we cannot optimize when subterm.isUnamedVariable
           * since it can be constrained
           */	  
          slotList = tom_append_list_concSlot(slotList, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slotName, subterm) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
    } else {
      PairNameDeclList pairNameDeclList = tomSymbol.getPairNameDeclList();
      for(TomTerm arg:(concTomTerm)args) {
        try{
          TomTerm subterm = typeStrategy.visitLight(arg);
          TomName slotName = pairNameDeclList.getHeadconcPairNameDecl().getSlotName();
          /*
           * we cannot optimize when subterm.isUnamedVariable
           * since it can be constrained
           */	  
          slotList = tom_append_list_concSlot(slotList, tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slotName, subterm) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) );
          pairNameDeclList = pairNameDeclList.getTailconcPairNameDecl();
        } catch(tom.library.sl.VisitFailure e) {
          System.out.println("should not be there");
        }
      }
    }

    return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(option, nameList, slotList, constraints) ;
  }

  protected TomTerm replaceXMLAppl(OptionList optionList, TomNameList nameList,
      TomList attrList, TomList childList, ConstraintList constraints) {
    boolean implicitAttribute = TomBase.hasImplicitXMLAttribut(optionList);
    boolean implicitChild     = TomBase.hasImplicitXMLChild(optionList);

    TomList newAttrList  =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    TomList newChildList =  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
    TomTerm star =  tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar.make(convertOriginTracking("_*",optionList), symbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
    if(implicitAttribute) { newAttrList  =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }
    if(implicitChild)     { newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ; }

    /*
     * the list of attributes should not be typed before the sort
     * the sortAttribute is extended to compare RecordAppl
     */

    //System.out.println("attrList = " + attrList);
    attrList = sortAttributeList(attrList);
    //System.out.println("sorted attrList = " + attrList);

    /*
     * Attributes: go from implicit notation to explicit notation
     */
    Strategy typeStrategy = tom_make_TopDownIdStopOnSuccess(tom_make_replaceTermApplTomSyntax(this));
    for(TomTerm attr:(concTomTerm)attrList) {
      try {
        TomTerm newPattern = typeStrategy.visitLight(attr);
        newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
        if(implicitAttribute) {
          newAttrList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newAttrList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
        }
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
    }
    newAttrList = newAttrList.reverse();

    /*
     * Childs: go from implicit notation to explicit notation
     */
    for(TomTerm child:(concTomTerm)childList) {
      try {
        TomTerm newPattern = typeStrategy.visitLight(child);
        newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
        if(implicitChild) {
          if(newPattern.isVariableStar()) {
            // remove the previously inserted pattern
            newChildList = newChildList.getTailconcTomTerm();
            if(newChildList.getHeadconcTomTerm().isUnamedVariableStar()) {
              // remove the previously inserted star
              newChildList = newChildList.getTailconcTomTerm();
            }
            // re-insert the pattern
            newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(newPattern,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          } else {
            newChildList =  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(star,tom_append_list_concTomTerm(newChildList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
          }
        }
      } catch(tom.library.sl.VisitFailure e) {
        System.out.println("should not be there");
      }
    }
    newChildList = newChildList.reverse();

    /*
     * encode the name and put it into the table of symbols
     */
    TomNameList newNameList =  tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ;
matchBlock: 
    {
      {{if ( (nameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )nameList).isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch156NameNumber_freshVar_4= (( tom.engine.adt.tomname.types.TomNameList )nameList).getHeadconcTomName() ;if ( (tomMatch156NameNumber_freshVar_4 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if ( "_".equals( tomMatch156NameNumber_freshVar_4.getString() ) ) {if (  (( tom.engine.adt.tomname.types.TomNameList )nameList).getTailconcTomName() .isEmptyconcTomName() ) {

          break matchBlock;
        }}}}}}}{if ( (nameList instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )nameList) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch156NameNumber_end_10=(( tom.engine.adt.tomname.types.TomNameList )nameList);do {{if (!( tomMatch156NameNumber_end_10.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch156NameNumber_freshVar_14= tomMatch156NameNumber_end_10.getHeadconcTomName() ;if ( (tomMatch156NameNumber_freshVar_14 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


          newNameList = tom_append_list_concTomName(newNameList, tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(ASTFactory.encodeXMLString(symbolTable(), tomMatch156NameNumber_freshVar_14.getString() )) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) );
        }}if ( tomMatch156NameNumber_end_10.isEmptyconcTomName() ) {tomMatch156NameNumber_end_10=(( tom.engine.adt.tomname.types.TomNameList )nameList);} else {tomMatch156NameNumber_end_10= tomMatch156NameNumber_end_10.getTailconcTomName() ;}}} while(!( (tomMatch156NameNumber_end_10==(( tom.engine.adt.tomname.types.TomNameList )nameList)) ));}}}}

    }

    /*
     * a single "_" is converted into an UnamedVariable to match
     * any XML node
     */
    TomTerm xmlHead;

    if(newNameList.isEmptyconcTomName()) {
      xmlHead =  tom.engine.adt.tomterm.types.tomterm.UnamedVariable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() , symbolTable().TYPE_UNKNOWN,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
    } else {
      xmlHead =  tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking(newNameList.getHeadconcTomName().getString(),optionList), newNameList,  tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
    }
    try {
      SlotList newArgs =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_NAME) , typeStrategy.visitLight(xmlHead)) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_ATTRLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newAttrList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.SLOT_CHILDLIST) , typeStrategy.visitLight( tom.engine.adt.tomterm.types.tomterm.TermAppl.make(convertOriginTracking("CONC_TNODE",optionList),  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.CONC_TNODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newChildList,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) , tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) ) ) 





;

      TomTerm result =  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(optionList,  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( tom.engine.adt.tomname.types.tomname.Name.make(Constants.ELEMENT_NODE) , tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) , newArgs, constraints) ;

      //System.out.println("replaceXML out:\n" + result);
      return result;
    } catch(tom.library.sl.VisitFailure e) {
      //must never be executed
      return star;
    }
  }

  /* auxilliary methods */

  private TomList sortAttributeList(TomList attrList) {
    {{if ( (attrList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if ( (( tom.engine.adt.tomterm.types.TomList )attrList).isEmptyconcTomTerm() ) {
 return attrList; }}}}{if ( (attrList instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )attrList) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch157NameNumber_end_6=(( tom.engine.adt.tomterm.types.TomList )attrList);do {{ tom.engine.adt.tomterm.types.TomList  tom_X1=tom_get_slice_concTomTerm((( tom.engine.adt.tomterm.types.TomList )attrList),tomMatch157NameNumber_end_6, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch157NameNumber_end_6.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e1= tomMatch157NameNumber_end_6.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch157NameNumber_freshVar_7= tomMatch157NameNumber_end_6.getTailconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tomMatch157NameNumber_end_10=tomMatch157NameNumber_freshVar_7;do {{ tom.engine.adt.tomterm.types.TomList  tom_X2=tom_get_slice_concTomTerm(tomMatch157NameNumber_freshVar_7,tomMatch157NameNumber_end_10, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() );if (!( tomMatch157NameNumber_end_10.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tom_e2= tomMatch157NameNumber_end_10.getHeadconcTomTerm() ; tom.engine.adt.tomterm.types.TomList  tom_X3= tomMatch157NameNumber_end_10.getTailconcTomTerm() ;{{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch158NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getArgs() ;if ( ((tomMatch158NameNumber_freshVar_2 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch158NameNumber_freshVar_2 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch158NameNumber_freshVar_2.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_13= tomMatch158NameNumber_freshVar_2.getHeadconcTomTerm() ;if ( (tomMatch158NameNumber_freshVar_13 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_12= tomMatch158NameNumber_freshVar_13.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_12 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_12.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_21= tomMatch158NameNumber_freshVar_12.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_21 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_12.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch158NameNumber_freshVar_4= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getArgs() ;if ( ((tomMatch158NameNumber_freshVar_4 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch158NameNumber_freshVar_4 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch158NameNumber_freshVar_4.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_15= tomMatch158NameNumber_freshVar_4.getHeadconcTomTerm() ;if ( (tomMatch158NameNumber_freshVar_15 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_14= tomMatch158NameNumber_freshVar_15.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_14 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_14 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_14.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_23= tomMatch158NameNumber_freshVar_14.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_23 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_14.getTailconcTomName() .isEmptyconcTomName() ) {




              if( tomMatch158NameNumber_freshVar_21.getString() .compareTo( tomMatch158NameNumber_freshVar_23.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch158NameNumber_freshVar_26= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getArgs() ;if ( ((tomMatch158NameNumber_freshVar_26 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch158NameNumber_freshVar_26 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch158NameNumber_freshVar_26.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_37= tomMatch158NameNumber_freshVar_26.getHeadconcTomTerm() ;if ( (tomMatch158NameNumber_freshVar_37 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_36= tomMatch158NameNumber_freshVar_37.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_36 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_36 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_36.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_45= tomMatch158NameNumber_freshVar_36.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_45 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_36.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomterm.types.TomList  tomMatch158NameNumber_freshVar_28= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getArgs() ;if ( ((tomMatch158NameNumber_freshVar_28 instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || (tomMatch158NameNumber_freshVar_28 instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {if (!( tomMatch158NameNumber_freshVar_28.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_39= tomMatch158NameNumber_freshVar_28.getHeadconcTomTerm() ;if ( (tomMatch158NameNumber_freshVar_39 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_38= tomMatch158NameNumber_freshVar_39.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_38 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_38 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_38.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_47= tomMatch158NameNumber_freshVar_38.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_47 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_38.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch158NameNumber_freshVar_45.getString() .compareTo( tomMatch158NameNumber_freshVar_47.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch158NameNumber_freshVar_50= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getSlots() ;if ( ((tomMatch158NameNumber_freshVar_50 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch158NameNumber_freshVar_50 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch158NameNumber_freshVar_50.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch158NameNumber_freshVar_62= tomMatch158NameNumber_freshVar_50.getHeadconcSlot() ;if ( (tomMatch158NameNumber_freshVar_62 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_61= tomMatch158NameNumber_freshVar_62.getAppl() ;if ( (tomMatch158NameNumber_freshVar_61 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_63= tomMatch158NameNumber_freshVar_61.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_63 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_63 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_63.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_76= tomMatch158NameNumber_freshVar_63.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_76 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_63.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch158NameNumber_freshVar_52= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getSlots() ;if ( ((tomMatch158NameNumber_freshVar_52 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch158NameNumber_freshVar_52 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch158NameNumber_freshVar_52.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch158NameNumber_freshVar_67= tomMatch158NameNumber_freshVar_52.getHeadconcSlot() ;if ( (tomMatch158NameNumber_freshVar_67 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_66= tomMatch158NameNumber_freshVar_67.getAppl() ;if ( ( tomMatch158NameNumber_freshVar_67.getSlotName() == tomMatch158NameNumber_freshVar_62.getSlotName() ) ) {if ( (tomMatch158NameNumber_freshVar_66 instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_68= tomMatch158NameNumber_freshVar_66.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_68 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_68 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_68.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_78= tomMatch158NameNumber_freshVar_68.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_78 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_68.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch158NameNumber_freshVar_76.getString() .compareTo( tomMatch158NameNumber_freshVar_78.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}{if ( (tom_e1 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e1) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch158NameNumber_freshVar_81= (( tom.engine.adt.tomterm.types.TomTerm )tom_e1).getSlots() ;if ( ((tomMatch158NameNumber_freshVar_81 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch158NameNumber_freshVar_81 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch158NameNumber_freshVar_81.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch158NameNumber_freshVar_93= tomMatch158NameNumber_freshVar_81.getHeadconcSlot() ;if ( (tomMatch158NameNumber_freshVar_93 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_92= tomMatch158NameNumber_freshVar_93.getAppl() ;if ( (tomMatch158NameNumber_freshVar_92 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_94= tomMatch158NameNumber_freshVar_92.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_94 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_94 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_94.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_107= tomMatch158NameNumber_freshVar_94.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_107 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_94.getTailconcTomName() .isEmptyconcTomName() ) {if ( (tom_e2 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_e2) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch158NameNumber_freshVar_83= (( tom.engine.adt.tomterm.types.TomTerm )tom_e2).getSlots() ;if ( ((tomMatch158NameNumber_freshVar_83 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch158NameNumber_freshVar_83 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( tomMatch158NameNumber_freshVar_83.isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch158NameNumber_freshVar_98= tomMatch158NameNumber_freshVar_83.getHeadconcSlot() ;if ( (tomMatch158NameNumber_freshVar_98 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch158NameNumber_freshVar_97= tomMatch158NameNumber_freshVar_98.getAppl() ;if ( ( tomMatch158NameNumber_freshVar_98.getSlotName() == tomMatch158NameNumber_freshVar_93.getSlotName() ) ) {if ( (tomMatch158NameNumber_freshVar_97 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch158NameNumber_freshVar_99= tomMatch158NameNumber_freshVar_97.getNameList() ;if ( ((tomMatch158NameNumber_freshVar_99 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch158NameNumber_freshVar_99 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch158NameNumber_freshVar_99.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch158NameNumber_freshVar_109= tomMatch158NameNumber_freshVar_99.getHeadconcTomName() ;if ( (tomMatch158NameNumber_freshVar_109 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {if (  tomMatch158NameNumber_freshVar_99.getTailconcTomName() .isEmptyconcTomName() ) {



              if( tomMatch158NameNumber_freshVar_107.getString() .compareTo( tomMatch158NameNumber_freshVar_109.getString() ) > 0) {
                return sortAttributeList(tom_append_list_concTomTerm(tom_X1, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e2,tom_append_list_concTomTerm(tom_X2, tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tom_e1,tom_append_list_concTomTerm(tom_X3, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) )) ));
              }
            }}}}}}}}}}}}}}}}}}}}}}}

      }if ( tomMatch157NameNumber_end_10.isEmptyconcTomTerm() ) {tomMatch157NameNumber_end_10=tomMatch157NameNumber_freshVar_7;} else {tomMatch157NameNumber_end_10= tomMatch157NameNumber_end_10.getTailconcTomTerm() ;}}} while(!( (tomMatch157NameNumber_end_10==tomMatch157NameNumber_freshVar_7) ));}if ( tomMatch157NameNumber_end_6.isEmptyconcTomTerm() ) {tomMatch157NameNumber_end_6=(( tom.engine.adt.tomterm.types.TomList )attrList);} else {tomMatch157NameNumber_end_6= tomMatch157NameNumber_end_6.getTailconcTomTerm() ;}}} while(!( (tomMatch157NameNumber_end_6==(( tom.engine.adt.tomterm.types.TomList )attrList)) ));}}}}

    return attrList;
  }

  private OptionList convertOriginTracking(String name,OptionList optionList) {
    Option originTracking = TomBase.findOriginTracking(optionList);
    {{if ( (originTracking instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )originTracking) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

        return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( tom.engine.adt.tomoption.types.option.OriginTracking.make( tom.engine.adt.tomname.types.tomname.Name.make(name) ,  (( tom.engine.adt.tomoption.types.Option )originTracking).getLine() ,  (( tom.engine.adt.tomoption.types.Option )originTracking).getFileName() ) , tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ;
      }}}}

    System.out.println("Warning: no OriginTracking information");
    return  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ;
  }


}
