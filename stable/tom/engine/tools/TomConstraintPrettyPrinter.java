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

import tom.library.sl.*;

public class TomConstraintPrettyPrinter {

        private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }    

  public TomConstraintPrettyPrinter() { }

  public String prettyPrint(tom.library.sl.Visitable subject) {
    
    {{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AssignTo) ) {


        return "AssignTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AssignPositionTo) ) {


        return "AssignPositionTo("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.TrueConstraint) ) {


        return "true";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.FalseConstraint) ) {


        return "false";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.Negate) ) {


        return "!"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getc() ); 
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.IsSortConstraint) ) {


        return "IsSort("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getAstType() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTomTerm() )+")";      
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadAndConstraint() )+" &\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailAndConstraint() );
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadOrConstraint() )+" ||\n"+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailOrConstraint() );
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraintDisjunction) ) {


        return "("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getHeadOrConstraintDisjunction() )+" | "+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getTailOrConstraintDisjunction() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getPattern() )+" << "+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getSubject() );
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.AntiMatchConstraint) ) {


        return "Anti("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getConstraint() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {


        return prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getPattern() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getType() )+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getSubject() );
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyListConstraint) ) {


        return "IsEmptyList("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )subject) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyArrayConstraint) ) {


        return "IsEmptyArray("+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomconstraint.types.Constraint )subject).getIndex() )+")";
      }}}}{{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessThan) ) {





        return "<";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumLessOrEqualThan) ) {

        return "<=";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterThan) ) {

        return ">";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumGreaterOrEqualThan) ) {

        return ">=";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumDifferent) ) {

        return "<>";
      }}}{if ( (subject instanceof tom.engine.adt.tomconstraint.types.NumericConstraintType) ) {if ( ((( tom.engine.adt.tomconstraint.types.NumericConstraintType )subject) instanceof tom.engine.adt.tomconstraint.types.numericconstrainttype.NumEqual) ) {

        return "==";
      }}}}{{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm) ) {




        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstExpression() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {


        return "_";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {


        return "_";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {


        return prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ); 
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.ListHead) ) {


        return "ListHead("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.ListTail) ) {


        return "ListTail("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.SymbolOf) ) {


        return "SymbolOf("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getGroundTerm() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Subterm) ) {


        return "Subterm("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getSlotName() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getGroundTerm() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableHeadList) ) {


        return "VariableHeadList("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getBegin() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getEnd() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableHeadArray) ) {


        return "VariableHeadArray("+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getOpname() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getSubject() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getBeginIndex() )+","+prettyPrint( (( tom.engine.adt.tomterm.types.TomTerm )subject).getEndIndex() )+")";
      }}}}{{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {





        return "("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstType() )+") "+ prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getSource() ); 
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceList) ) {


        return "GetSliceList("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getTail() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSliceArray) ) {


        return "GetSliceArray("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getSubjectListName() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableBeginAST() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariableEndAST() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetSize) ) {


        return "GetSize("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.GetElement) ) {


        return "GetElement("+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getVariable() )+","+prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getIndex() )+")";
      }}}{if ( (subject instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )subject) instanceof tom.engine.adt.tomexpression.types.expression.TomTermToExpression) ) {


        return prettyPrint( (( tom.engine.adt.tomexpression.types.Expression )subject).getAstTerm() );
      }}}}{{if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {





        return "t"+ TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )subject).getNumberList() );
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )subject) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  (( tom.engine.adt.tomname.types.TomName )subject).getString() ;
      }}}}{{if ( (subject instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( (((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( (( tom.engine.adt.tomname.types.TomNameList )subject).isEmptyconcTomName() )) {if (  (( tom.engine.adt.tomname.types.TomNameList )subject).getTailconcTomName() .isEmptyconcTomName() ) {





        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getHeadconcTomName() );
      }}}}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNameList) ) {if ( ((( tom.engine.adt.tomname.types.TomNameList )subject) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getHeadconcTomName() )+"."+prettyPrint( (( tom.engine.adt.tomname.types.TomNameList )subject).getTailconcTomName() );
      }}}}{{if ( (subject instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )subject) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tomMatch244NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )subject).getTomType() ;if ( (tomMatch244NameNumber_freshVar_1 instanceof tom.engine.adt.tomtype.types.tomtype.ASTTomType) ) {



 return  tomMatch244NameNumber_freshVar_1.getString() ; }}}}}{{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Position) ) {




        return "" +  (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.NameNumber) ) {


        return prettyPrint( (( tom.engine.adt.tomname.types.TomNumber )subject).getAstName() );
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.ListNumber) ) {


        return "listNumber"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.Begin) ) {


        return "begin"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}{if ( (subject instanceof tom.engine.adt.tomname.types.TomNumber) ) {if ( ((( tom.engine.adt.tomname.types.TomNumber )subject) instanceof tom.engine.adt.tomname.types.tomnumber.End) ) {


        return "end"+ (( tom.engine.adt.tomname.types.TomNumber )subject).getInteger() ;
      }}}}


    return subject.toString();
  }

}
