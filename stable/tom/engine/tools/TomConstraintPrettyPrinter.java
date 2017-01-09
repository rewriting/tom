/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2017, Universite de Lorraine, Inria
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

package tom.engine.tools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import tom.engine.TomBase;
import tom.engine.adt.tomsignature.*;
import tom.engine.tools.TomGenericPlugin;

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
import tom.engine.adt.code.types.*;

import tom.library.sl.*;

public class TomConstraintPrettyPrinter {

        private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {     if( l1.isEmptyconcBQTerm() ) {       return l2;     } else if( l2.isEmptyconcBQTerm() ) {       return l1;     } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;     } else {       return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;     }   }   private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }    


  public static String prettyPrint(Constraint subject) {
    
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {


        return "AliasTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVar() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AssignPositionTo) ) {


        return "AssignPositionTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.TrueConstraint) ) {


        return "true";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.FalseConstraint) ) {


        return "false";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {


        return "!"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getc() ); 
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {


        return "IsSort("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getAstType() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getBQTerm() )+")";      
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadAndConstraint() )+" &\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailAndConstraint() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadOrConstraint() )+" ||\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailOrConstraint() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadOrConstraintDisjunction() )+" | "+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailOrConstraintDisjunction() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom___tPattern= (( tom.engine.adt.tomconstraint.types.Constraint )subject).getPattern() ; tom.engine.adt.code.types.BQTerm  tom___tSubject= (( tom.engine.adt.tomconstraint.types.Constraint )subject).getSubject() ;


        if(TomBase.hasTheory(tom___tPattern, tom.engine.adt.theory.types.elementarytheory.AC.make() )) {
          return prettyPrint(tom___tPattern)+" <<_AC "+prettyPrint(tom___tSubject);
        } else {
          return prettyPrint(tom___tPattern)+" << "+prettyPrint(tom___tSubject);
        }
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {


        return "Anti("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getConstraint() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getLeft() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getType() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getRight() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {


        return "IsEmptyList("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {


        return "IsEmptyArray("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getIndex() )+")";
      }}}}
 
    return subject.toString();
  }


  public static String prettyPrint(NumericConstraintType subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan) ) {

        return "<";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan) ) {

        return "<=";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan) ) {

        return ">";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan) ) {

        return ">=";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent) ) {

        return "<>";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual) ) {

        return "==";
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomTerm subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ) + "*";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() )+"("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getSlots() )+")"; 
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(Expression subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() )+") "+ prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() ); 
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {


        return "GetSliceList("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) {


        return "GetSliceArray("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {


        return "GetSize("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {


        return "GetElement("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() )+")";
      }}}}
 
    return subject.toString();
  }


  public static String prettyPrint(TomName subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return "t"+ TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )subject).getNumberList() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  (( tom.engine.adt.tomname.types.TomName )subject).getString() ;
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomNameList subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )subject).isEmptyconcTomName() )) {if (  (( tom.engine.adt.tomname.types.TomNameList )subject).getTailconcTomName() .isEmptyconcTomName() ) {

        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getHeadconcTomName() );
      }}}}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( ((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getHeadconcTomName() )+"."+prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getTailconcTomName() );
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomType subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )subject) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  (( tom.engine.adt.tomtype.types.TomType )subject).getTomType() ; }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomNumber subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Position) ) {

        return "" +  (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.NameNumber) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNumber )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.ListNumber) ) {


        return "listNumber"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Begin) ) {


        return "begin"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.End) ) {


        return "end"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(Slot subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )subject) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {

        return prettyPrint( (( tom.engine.adt.tomslot.types.Slot )subject).getAppl() );
      }}}}
 
    return subject.toString();
  }

  public static String prettyPrint(SlotList subject) {
    String s = "";
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subject) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subject) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch373_end_4=(( tom.engine.adt.tomslot.types.SlotList )subject);do {{ /* unamed block */if (!( tomMatch373_end_4.isEmptyconcSlot() )) {

        s += prettyPrint( tomMatch373_end_4.getHeadconcSlot() )+",";
      }if ( tomMatch373_end_4.isEmptyconcSlot() ) {tomMatch373_end_4=(( tom.engine.adt.tomslot.types.SlotList )subject);} else {tomMatch373_end_4= tomMatch373_end_4.getTailconcSlot() ;}}} while(!( (tomMatch373_end_4==(( tom.engine.adt.tomslot.types.SlotList )subject)) ));}}}}

    if (! s.equals("")) { return s.substring(0,s.length()-1); }

    return subject.toString();
  }

  public static String prettyPrint(BQTerm subject) {
    { /* unamed block */{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() ) + "*";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getExp() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ListHead) ) {


        return "ListHead("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.ListTail) ) {


        return "ListTail("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getVariable() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {


        return "SymbolOf("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getGroundTerm() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.Subterm) ) {


        return "Subterm("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getSlotName() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getGroundTerm() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.VariableHeadList) ) {


        return "VariableHeadList("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getBegin() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getEnd() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.VariableHeadArray) ) {


        return "VariableHeadArray("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getSubject() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getBeginIndex() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getEndIndex() )+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() );
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) { tom.engine.adt.code.types.BQTermList  tom___Args= (( tom.engine.adt.code.types.BQTerm )subject).getArgs() ;


        String s = "";
        int min=0;
        { /* unamed block */{ /* unamed block */if ( (tom___Args instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )tom___Args) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )tom___Args) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch375_end_4=(( tom.engine.adt.code.types.BQTermList )tom___Args);do {{ /* unamed block */if (!( tomMatch375_end_4.isEmptyconcBQTerm() )) {

            s += ","+prettyPrint( tomMatch375_end_4.getHeadconcBQTerm() );
            min=1;
          }if ( tomMatch375_end_4.isEmptyconcBQTerm() ) {tomMatch375_end_4=(( tom.engine.adt.code.types.BQTermList )tom___Args);} else {tomMatch375_end_4= tomMatch375_end_4.getTailconcBQTerm() ;}}} while(!( (tomMatch375_end_4==(( tom.engine.adt.code.types.BQTermList )tom___Args)) ));}}}}

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"("+s.substring(min, s.length())+")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"()";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"(" + prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getHeadTerm() ) + ", " + prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getTailTerm() ) +")";
      }}}{ /* unamed block */if ( (subject instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )subject) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getAstName() )+"(" + prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getTailTerm() ) + ", " + prettyPrint( (( tom.engine.adt.code.types.BQTerm )subject).getHeadTerm() ) +")";
      }}}}


    return subject.toString();
  }
}
