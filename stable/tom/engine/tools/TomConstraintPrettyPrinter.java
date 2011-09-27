/*
 * 
 * TOM - To One Matching Compiler
 * 
 * Copyright (c) 2000-2011, INPL, INRIA
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
    
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {


        return "AliasTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getVar() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.AssignPositionTo) ) {


        return "AssignPositionTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getVariable() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.TrueConstraint) ) {


        return "true";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.FalseConstraint) ) {


        return "false";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {


        return "!"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getc() ); 
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {


        return "IsSort("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getAstType() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getBQTerm() )+")";      
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getHeadAndConstraint() )+" &\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getTailAndConstraint() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getHeadOrConstraint() )+" ||\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getTailOrConstraint() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getHeadOrConstraintDisjunction() )+" | "+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getTailOrConstraintDisjunction() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_tPattern= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getPattern() ; tom.engine.adt.code.types.BQTerm  tom_tSubject= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getSubject() ;


        if(TomBase.hasTheory(tom_tPattern, tom.engine.adt.theory.types.elementarytheory.AC.make() )) {
          return prettyPrint(tom_tPattern)+" <<_AC "+prettyPrint(tom_tSubject);
        } else {
          return prettyPrint(tom_tPattern)+" << "+prettyPrint(tom_tSubject);
        }
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {


        return "Anti("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getConstraint() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getLeft() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getType() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getRight() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {


        return "IsEmptyList("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getVariable() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {


        return "IsEmptyArray("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)subject)).getIndex() )+")";
      }}}}}
 
    return subject.toString();
  }


  public static String prettyPrint(NumericConstraintType subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan) ) {

        return "<";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan) ) {

        return "<=";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan) ) {

        return ">";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan) ) {

        return ">=";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent) ) {

        return "<>";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject)) instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )(( tom.engine.adt.tomconstraint.types.NumericConstraintType )((Object)subject))) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual) ) {

        return "==";
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomTerm subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)).getAstName() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)).getAstName() ) + "*";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)subject))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)).getNameList() )+"("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )((Object)subject)).getSlots() )+")"; 
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(Expression subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.BQTermToExpression) ) {

        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstTerm() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstType() )+") "+ prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSource() ); 
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {


        return "GetSliceList("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableEndAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getTail() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) {


        return "GetSliceArray("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getSubjectListName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariableEndAST() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {


        return "GetSize("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )((Object)subject)) instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )(( tom.engine.adt.tomexpression.types.Expression )((Object)subject))) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {


        return "GetElement("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )((Object)subject)).getIndex() )+")";
      }}}}}
 
    return subject.toString();
  }


  public static String prettyPrint(TomName subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )(( tom.engine.adt.tomname.types.TomName )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return "t"+ TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )((Object)subject)).getNumberList() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )(( tom.engine.adt.tomname.types.TomName )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  (( tom.engine.adt.tomname.types.TomName )((Object)subject)).getString() ;
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomNameList subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )((Object)subject)).isEmptyconcTomName() )) {if (  (( tom.engine.adt.tomname.types.TomNameList )((Object)subject)).getTailconcTomName() .isEmptyconcTomName() ) {

        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )((Object)subject)).getHeadconcTomName() );
      }}}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( ((( tom.engine.adt.tomname.types.TomNameList )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( ((( tom.engine.adt.tomname.types.TomNameList )(( tom.engine.adt.tomname.types.TomNameList )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )((Object)subject)).getHeadconcTomName() )+"."+prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )((Object)subject)).getTailconcTomName() );
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomType subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)subject)) instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)subject))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 return  (( tom.engine.adt.tomtype.types.TomType )((Object)subject)).getTomType() ; }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(TomNumber subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )(( tom.engine.adt.tomname.types.TomNumber )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnumber.Position) ) {

        return "" +  (( tom.engine.adt.tomname.types.TomNumber )((Object)subject)).getInteger() ;
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )(( tom.engine.adt.tomname.types.TomNumber )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnumber.NameNumber) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNumber )((Object)subject)).getAstName() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )(( tom.engine.adt.tomname.types.TomNumber )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnumber.ListNumber) ) {


        return "listNumber"+ (( tom.engine.adt.tomname.types.TomNumber )((Object)subject)).getInteger() ;
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )(( tom.engine.adt.tomname.types.TomNumber )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnumber.Begin) ) {


        return "begin"+ (( tom.engine.adt.tomname.types.TomNumber )((Object)subject)).getInteger() ;
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )((Object)subject)) instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )(( tom.engine.adt.tomname.types.TomNumber )((Object)subject))) instanceof tom.engine.adt.tomname.types.tomnumber.End) ) {


        return "end"+ (( tom.engine.adt.tomname.types.TomNumber )((Object)subject)).getInteger() ;
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(Slot subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )((Object)subject)) instanceof tom.engine.adt.tomslot.types.Slot) ) {if ( ((( tom.engine.adt.tomslot.types.Slot )(( tom.engine.adt.tomslot.types.Slot )((Object)subject))) instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {

        return prettyPrint( (( tom.engine.adt.tomslot.types.Slot )((Object)subject)).getAppl() );
      }}}}}
 
    return subject.toString();
  }

  public static String prettyPrint(SlotList subject) {
    String s = "";
    {{if ( (((Object)subject) instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )(( tom.engine.adt.tomslot.types.SlotList )((Object)subject))) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )(( tom.engine.adt.tomslot.types.SlotList )((Object)subject))) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) { tom.engine.adt.tomslot.types.SlotList  tomMatch337__end__4=(( tom.engine.adt.tomslot.types.SlotList )((Object)subject));do {{if (!( tomMatch337__end__4.isEmptyconcSlot() )) {

        s += prettyPrint( tomMatch337__end__4.getHeadconcSlot() )+",";
      }if ( tomMatch337__end__4.isEmptyconcSlot() ) {tomMatch337__end__4=(( tom.engine.adt.tomslot.types.SlotList )((Object)subject));} else {tomMatch337__end__4= tomMatch337__end__4.getTailconcSlot() ;}}} while(!( (tomMatch337__end__4==(( tom.engine.adt.tomslot.types.SlotList )((Object)subject))) ));}}}}

    if (! s.equals("")) { return s.substring(0,s.length()-1); }

    return subject.toString();
  }

  public static String prettyPrint(BQTerm subject) {
    {{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() ) + "*";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ExpressionToBQTerm) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getExp() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ListHead) ) {


        return "ListHead("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getVariable() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.ListTail) ) {


        return "ListTail("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getVariable() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.SymbolOf) ) {


        return "SymbolOf("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getGroundTerm() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.Subterm) ) {


        return "Subterm("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getSlotName() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getGroundTerm() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.VariableHeadList) ) {


        return "VariableHeadList("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getOpname() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getBegin() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getEnd() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.VariableHeadArray) ) {


        return "VariableHeadArray("+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getOpname() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getSubject() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getBeginIndex() )+","+prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getEndIndex() )+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() );
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) { tom.engine.adt.code.types.BQTermList  tom_Args= (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getArgs() ;


        String s = "";
        int min=0;
        {{if ( (((Object)tom_Args) instanceof tom.engine.adt.code.types.BQTermList) ) {if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)tom_Args))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)tom_Args))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) { tom.engine.adt.code.types.BQTermList  tomMatch339__end__4=(( tom.engine.adt.code.types.BQTermList )((Object)tom_Args));do {{if (!( tomMatch339__end__4.isEmptyconcBQTerm() )) {

            s += ","+prettyPrint( tomMatch339__end__4.getHeadconcBQTerm() );
            min=1;
          }if ( tomMatch339__end__4.isEmptyconcBQTerm() ) {tomMatch339__end__4=(( tom.engine.adt.code.types.BQTermList )((Object)tom_Args));} else {tomMatch339__end__4= tomMatch339__end__4.getTailconcBQTerm() ;}}} while(!( (tomMatch339__end__4==(( tom.engine.adt.code.types.BQTermList )((Object)tom_Args))) ));}}}}

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() )+"("+s.substring(min, s.length())+")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() )+"()";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {


        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() )+"(" + prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getHeadTerm() ) + ", " + prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getTailTerm() ) +")";
      }}}}{if ( (((Object)subject) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )((Object)subject)) instanceof tom.engine.adt.code.types.BQTerm) ) {if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)subject))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {

        return prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getAstName() )+"(" + prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getTailTerm() ) + ", " + prettyPrint( (( tom.engine.adt.code.types.BQTerm )((Object)subject)).getHeadTerm() ) +")";
      }}}}}


    return subject.toString();
  }
}
