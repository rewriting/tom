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
 * Antoine Reilles        e-mail: Antoine.Reilles@loria.fr
 *
 **/

package tom.engine.verifier;

import tom.engine.*;
import aterm.*;
import java.util.*;
import tom.engine.tools.SymbolTable;

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
import tom.engine.adt.il.*;
import tom.engine.adt.il.types.*;
import tom.library.sl.Strategy;
import tom.library.sl.VisitFailure;
import tom.library.sl.Introspector;
import tom.library.sl.AbstractStrategyBasic;

import tom.engine.exception.TomRuntimeException;

public class Verifier {

  // ------------------------------------------------------------
        private static   tom.engine.adt.tominstruction.types.InstructionList  tom_append_list_concInstruction( tom.engine.adt.tominstruction.types.InstructionList l1,  tom.engine.adt.tominstruction.types.InstructionList  l2) {     if( l1.isEmptyconcInstruction() ) {       return l2;     } else if( l2.isEmptyconcInstruction() ) {       return l1;     } else if(  l1.getTailconcInstruction() .isEmptyconcInstruction() ) {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( l1.getHeadconcInstruction() ,tom_append_list_concInstruction( l1.getTailconcInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.InstructionList  tom_get_slice_concInstruction( tom.engine.adt.tominstruction.types.InstructionList  begin,  tom.engine.adt.tominstruction.types.InstructionList  end, tom.engine.adt.tominstruction.types.InstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcInstruction()  ||  (end== tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction.make( begin.getHeadconcInstruction() ,( tom.engine.adt.tominstruction.types.InstructionList )tom_get_slice_concInstruction( begin.getTailconcInstruction() ,end,tail)) ;   }         private static   tom.engine.adt.il.types.InstrList  tom_append_list_semicolon( tom.engine.adt.il.types.InstrList l1,  tom.engine.adt.il.types.InstrList  l2) {     if( l1.isEmptysemicolon() ) {       return l2;     } else if( l2.isEmptysemicolon() ) {       return l1;     } else if(  l1.getTailsemicolon() .isEmptysemicolon() ) {       return  tom.engine.adt.il.types.instrlist.Conssemicolon.make( l1.getHeadsemicolon() ,l2) ;     } else {       return  tom.engine.adt.il.types.instrlist.Conssemicolon.make( l1.getHeadsemicolon() ,tom_append_list_semicolon( l1.getTailsemicolon() ,l2)) ;     }   }   private static   tom.engine.adt.il.types.InstrList  tom_get_slice_semicolon( tom.engine.adt.il.types.InstrList  begin,  tom.engine.adt.il.types.InstrList  end, tom.engine.adt.il.types.InstrList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptysemicolon()  ||  (end== tom.engine.adt.il.types.instrlist.Emptysemicolon.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.il.types.instrlist.Conssemicolon.make( begin.getHeadsemicolon() ,( tom.engine.adt.il.types.InstrList )tom_get_slice_semicolon( begin.getTailsemicolon() ,end,tail)) ;   }      private static   tom.engine.adt.il.types.ExprList  tom_append_list_concExpr( tom.engine.adt.il.types.ExprList l1,  tom.engine.adt.il.types.ExprList  l2) {     if( l1.isEmptyconcExpr() ) {       return l2;     } else if( l2.isEmptyconcExpr() ) {       return l1;     } else if(  l1.getTailconcExpr() .isEmptyconcExpr() ) {       return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make( l1.getHeadconcExpr() ,l2) ;     } else {       return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make( l1.getHeadconcExpr() ,tom_append_list_concExpr( l1.getTailconcExpr() ,l2)) ;     }   }   private static   tom.engine.adt.il.types.ExprList  tom_get_slice_concExpr( tom.engine.adt.il.types.ExprList  begin,  tom.engine.adt.il.types.ExprList  end, tom.engine.adt.il.types.ExprList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcExpr()  ||  (end== tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make( begin.getHeadconcExpr() ,( tom.engine.adt.il.types.ExprList )tom_get_slice_concExpr( begin.getTailconcExpr() ,end,tail)) ;   }      private static   tom.engine.adt.il.types.SubstitutionList  tom_append_list_subs( tom.engine.adt.il.types.SubstitutionList l1,  tom.engine.adt.il.types.SubstitutionList  l2) {     if( l1.isEmptysubs() ) {       return l2;     } else if( l2.isEmptysubs() ) {       return l1;     } else if(  l1.getTailsubs() .isEmptysubs() ) {       return  tom.engine.adt.il.types.substitutionlist.Conssubs.make( l1.getHeadsubs() ,l2) ;     } else {       return  tom.engine.adt.il.types.substitutionlist.Conssubs.make( l1.getHeadsubs() ,tom_append_list_subs( l1.getTailsubs() ,l2)) ;     }   }   private static   tom.engine.adt.il.types.SubstitutionList  tom_get_slice_subs( tom.engine.adt.il.types.SubstitutionList  begin,  tom.engine.adt.il.types.SubstitutionList  end, tom.engine.adt.il.types.SubstitutionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptysubs()  ||  (end== tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.il.types.substitutionlist.Conssubs.make( begin.getHeadsubs() ,( tom.engine.adt.il.types.SubstitutionList )tom_get_slice_subs( begin.getTailsubs() ,end,tail)) ;   }      private static   tom.engine.adt.il.types.TermList  tom_append_list_concTerm( tom.engine.adt.il.types.TermList l1,  tom.engine.adt.il.types.TermList  l2) {     if( l1.isEmptyconcTerm() ) {       return l2;     } else if( l2.isEmptyconcTerm() ) {       return l1;     } else if(  l1.getTailconcTerm() .isEmptyconcTerm() ) {       return  tom.engine.adt.il.types.termlist.ConsconcTerm.make( l1.getHeadconcTerm() ,l2) ;     } else {       return  tom.engine.adt.il.types.termlist.ConsconcTerm.make( l1.getHeadconcTerm() ,tom_append_list_concTerm( l1.getTailconcTerm() ,l2)) ;     }   }   private static   tom.engine.adt.il.types.TermList  tom_get_slice_concTerm( tom.engine.adt.il.types.TermList  begin,  tom.engine.adt.il.types.TermList  end, tom.engine.adt.il.types.TermList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTerm()  ||  (end== tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.il.types.termlist.ConsconcTerm.make( begin.getHeadconcTerm() ,( tom.engine.adt.il.types.TermList )tom_get_slice_concTerm( begin.getTailconcTerm() ,end,tail)) ;   }         private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) );       } else {         return ( (tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):new tom.library.sl.Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) );       } else {         return ( (tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):new tom.library.sl.Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.SequenceId) )) {       if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {         return ( (l2==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) );       } else {         return ( (tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)==null)?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ):new tom.library.sl.SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.SequenceId(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin):new tom.library.sl.SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( (begin instanceof tom.library.sl.SequenceId) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ):( null )),end,tail)) );   }       private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( null )) )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( (( null )==null)?( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ):new tom.library.sl.Sequence(( (( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )==null)?v:new tom.library.sl.SequenceId(v,( (( null )==null)?( new tom.library.sl.MuVar("_x") ):new tom.library.sl.SequenceId(( new tom.library.sl.MuVar("_x") ),( null )) )) ),( null )) )) )) ) );}    











  // ------------------------------------------------------------

  private SymbolTable symbolTable;
  private boolean camlsemantics = false;

  public Verifier(boolean camlsemantics) {
    super();
    this.camlsemantics = camlsemantics;
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  public SymbolTable getSymbolTable() {
    return symbolTable;
  }

  public boolean isCamlSemantics() {
    return camlsemantics;
  }

  public Term termFromTomTerm(TomTerm tomterm) {
    {{if ( (tomterm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomterm) instanceof tom.engine.adt.tomterm.types.tomterm.ExpressionToTomTerm) ) {

        return termFromExpresssion( (( tom.engine.adt.tomterm.types.TomTerm )tomterm).getAstExpression() );
      }}}{if ( (tomterm instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomterm) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        return termFromTomName( (( tom.engine.adt.tomterm.types.TomTerm )tomterm).getAstName() );
      }}}}

    System.out.println("termFromTomTerm don't know how to handle this: " + tomterm);
    return  tom.engine.adt.il.types.term.repr.make("foirade") ;
  }

  Variable variableFromTomName(TomName name) {
    {{if ( (name instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )name) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        return  tom.engine.adt.il.types.variable.var.make( (( tom.engine.adt.tomname.types.TomName )name).getString() ) ;
      }}}{if ( (name instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )name) instanceof tom.engine.adt.tomname.types.tomname.PositionName) ) {

        return  tom.engine.adt.il.types.variable.var.make(TomBase.tomNumberListToString( (( tom.engine.adt.tomname.types.TomName )name).getNumberList() )) ;
      }}}{if ( (name instanceof tom.engine.adt.tomname.types.TomName) ) {if ( ((( tom.engine.adt.tomname.types.TomName )name) instanceof tom.engine.adt.tomname.types.tomname.EmptyName) ) {

        return  tom.engine.adt.il.types.variable.var.make("emptyName") ;
      }}}}

    return  tom.engine.adt.il.types.variable.var.make("error while building variable name") ;
  }

  Term termFromTomName(TomName name) {
    return  tom.engine.adt.il.types.term.tau.make( tom.engine.adt.il.types.absterm.absvar.make(variableFromTomName(name)) ) ;
  }

  public Term termFromExpresssion(Expression expression) {
    {{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.GetSlot) ) { tom.engine.adt.tomname.types.TomName  tomMatch332NameNumber_freshVar_1= (( tom.engine.adt.tomexpression.types.Expression )expression).getAstName() ;if ( (tomMatch332NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        Term term = termFromTomTerm( (( tom.engine.adt.tomexpression.types.Expression )expression).getVariable() );
        return  tom.engine.adt.il.types.term.slot.make( tom.engine.adt.il.types.symbol.fsymbol.make( tomMatch332NameNumber_freshVar_1.getString() ) , term,  (( tom.engine.adt.tomexpression.types.Expression )expression).getSlotNameString() ) ;
      }}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.TomTermToExpression) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch332NameNumber_freshVar_8= (( tom.engine.adt.tomexpression.types.Expression )expression).getAstTerm() ;if ( (tomMatch332NameNumber_freshVar_8 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        Term term = termFromTomName( tomMatch332NameNumber_freshVar_8.getAstName() );
        return term;
      }}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.Cast) ) {

        return termFromExpresssion( (( tom.engine.adt.tomexpression.types.Expression )expression).getSource() );
      }}}}

    System.out.println("termFromExpression don't know how to handle this: " + expression);
    return  tom.engine.adt.il.types.term.repr.make("autre foirade avec " + expression) ;
  }

  public Expr exprFromExpression(Expression expression) {
    {{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.TrueTL) ) {
 return  tom.engine.adt.il.types.expr.iltrue.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ) ; }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.FalseTL) ) {
 return  tom.engine.adt.il.types.expr.ilfalse.make() ; }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.IsFsym) ) { tom.engine.adt.tomname.types.TomName  tomMatch333NameNumber_freshVar_5= (( tom.engine.adt.tomexpression.types.Expression )expression).getAstName() ;if ( (tomMatch333NameNumber_freshVar_5 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

        Term term = termFromTomTerm( (( tom.engine.adt.tomexpression.types.Expression )expression).getVariable() );
        return  tom.engine.adt.il.types.expr.isfsym.make(term,  tom.engine.adt.il.types.symbol.fsymbol.make( tomMatch333NameNumber_freshVar_5.getString() ) ) ;
      }}}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.EqualTerm) ) {

        return  tom.engine.adt.il.types.expr.eq.make(termFromTomTerm( (( tom.engine.adt.tomexpression.types.Expression )expression).getKid1() ), termFromTomTerm( (( tom.engine.adt.tomexpression.types.Expression )expression).getKid2() )) ;
      }}}{if ( (expression instanceof tom.engine.adt.tomexpression.types.Expression) ) {if ( ((( tom.engine.adt.tomexpression.types.Expression )expression) instanceof tom.engine.adt.tomexpression.types.expression.IsSort) ) {
 return  tom.engine.adt.il.types.expr.iltrue.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ) ; }}}}

    System.out.println("exprFromExpression don't know how to handle this: " + expression);
    return  tom.engine.adt.il.types.expr.ilfalse.make() ;
  }

  public Instr instrFromInstructionList(InstructionList instrlist) {
    InstrList list =  tom.engine.adt.il.types.instrlist.Emptysemicolon.make() ;
    while (!instrlist.isEmptyconcInstruction()) {
      Instruction i = (Instruction) instrlist.getHeadconcInstruction();
      instrlist = instrlist.getTailconcInstruction();
      list = tom_append_list_semicolon(list, tom.engine.adt.il.types.instrlist.Conssemicolon.make(instrFromInstruction(i), tom.engine.adt.il.types.instrlist.Emptysemicolon.make() ) );
    }
    return  tom.engine.adt.il.types.instr.sequence.make(list) ;
  }

  public Instr instrFromInstruction(Instruction automata) {
    {{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) {

        return  tom.engine.adt.il.types.instr.accept.make( (( tom.engine.adt.tominstruction.types.Instruction )automata).getPositivePattern() .toATerm(),  (( tom.engine.adt.tominstruction.types.Instruction )automata).getNegativePatternList() .toATerm()) ;
      }}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.If) ) {


        return  tom.engine.adt.il.types.instr.ITE.make(exprFromExpression( (( tom.engine.adt.tominstruction.types.Instruction )automata).getCondition() ), instrFromInstruction( (( tom.engine.adt.tominstruction.types.Instruction )automata).getSuccesInst() ), instrFromInstruction( (( tom.engine.adt.tominstruction.types.Instruction )automata).getFailureInst() )) 

;
      }}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch334NameNumber_freshVar_16= false ; tom.engine.adt.tomexpression.types.Expression  tomMatch334NameNumber_freshVar_11= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch334NameNumber_freshVar_10= null ; tom.engine.adt.tominstruction.types.Instruction  tomMatch334NameNumber_freshVar_12= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {{tomMatch334NameNumber_freshVar_16= true ;tomMatch334NameNumber_freshVar_10= (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable() ;tomMatch334NameNumber_freshVar_11= (( tom.engine.adt.tominstruction.types.Instruction )automata).getSource() ;tomMatch334NameNumber_freshVar_12= (( tom.engine.adt.tominstruction.types.Instruction )automata).getAstInstruction() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {{tomMatch334NameNumber_freshVar_16= true ;tomMatch334NameNumber_freshVar_10= (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable() ;tomMatch334NameNumber_freshVar_11= (( tom.engine.adt.tominstruction.types.Instruction )automata).getSource() ;tomMatch334NameNumber_freshVar_12= (( tom.engine.adt.tominstruction.types.Instruction )automata).getAstInstruction() ;}}}if ( tomMatch334NameNumber_freshVar_16== true  ) {if ( (tomMatch334NameNumber_freshVar_10 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        Variable thevar = variableFromTomName( tomMatch334NameNumber_freshVar_10.getAstName() );
        return  tom.engine.adt.il.types.instr.ILLet.make(thevar, termFromExpresssion(tomMatch334NameNumber_freshVar_11), instrFromInstruction(tomMatch334NameNumber_freshVar_12)) 

;
      }}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch334NameNumber_freshVar_18= (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable() ;if ( (tomMatch334NameNumber_freshVar_18 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

        Variable thevar = variableFromTomName( tomMatch334NameNumber_freshVar_18.getAstName() );
        return  tom.engine.adt.il.types.instr.ILLet.make(thevar, termFromExpresssion( (( tom.engine.adt.tominstruction.types.Instruction )automata).getSource() ),  tom.engine.adt.il.types.instr.refuse.make() ) 

; /* check that refuse is correct here */
      }}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {boolean tomMatch334NameNumber_freshVar_29= false ; tom.engine.adt.tominstruction.types.Instruction  tomMatch334NameNumber_freshVar_26= null ; tom.engine.adt.tomexpression.types.Expression  tomMatch334NameNumber_freshVar_25= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch334NameNumber_freshVar_24= null ;if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.Let) ) {{tomMatch334NameNumber_freshVar_29= true ;tomMatch334NameNumber_freshVar_24= (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable() ;tomMatch334NameNumber_freshVar_25= (( tom.engine.adt.tominstruction.types.Instruction )automata).getSource() ;tomMatch334NameNumber_freshVar_26= (( tom.engine.adt.tominstruction.types.Instruction )automata).getAstInstruction() ;}} else {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.LetRef) ) {{tomMatch334NameNumber_freshVar_29= true ;tomMatch334NameNumber_freshVar_24= (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable() ;tomMatch334NameNumber_freshVar_25= (( tom.engine.adt.tominstruction.types.Instruction )automata).getSource() ;tomMatch334NameNumber_freshVar_26= (( tom.engine.adt.tominstruction.types.Instruction )automata).getAstInstruction() ;}}}if ( tomMatch334NameNumber_freshVar_29== true  ) {if ( (tomMatch334NameNumber_freshVar_24 instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {

        return instrFromInstruction(tomMatch334NameNumber_freshVar_26);
      }}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.Assign) ) {if ( ( (( tom.engine.adt.tominstruction.types.Instruction )automata).getVariable()  instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {

        return  tom.engine.adt.il.types.instr.refuse.make() ; /* check that refuse is correct here */
      }}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.CompiledPattern) ) {

        return instrFromInstruction( (( tom.engine.adt.tominstruction.types.Instruction )automata).getAutomataInst() );
      }}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch334NameNumber_freshVar_39= (( tom.engine.adt.tominstruction.types.Instruction )automata).getInstList() ;if ( ((tomMatch334NameNumber_freshVar_39 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch334NameNumber_freshVar_39 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {if (!( tomMatch334NameNumber_freshVar_39.isEmptyconcInstruction() )) {if (  tomMatch334NameNumber_freshVar_39.getTailconcInstruction() .isEmptyconcInstruction() ) {

        return instrFromInstruction( tomMatch334NameNumber_freshVar_39.getHeadconcInstruction() );
      }}}}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.AbstractBlock) ) { tom.engine.adt.tominstruction.types.InstructionList  tomMatch334NameNumber_freshVar_44= (( tom.engine.adt.tominstruction.types.Instruction )automata).getInstList() ;if ( ((tomMatch334NameNumber_freshVar_44 instanceof tom.engine.adt.tominstruction.types.instructionlist.ConsconcInstruction) || (tomMatch334NameNumber_freshVar_44 instanceof tom.engine.adt.tominstruction.types.instructionlist.EmptyconcInstruction)) ) {

        return instrFromInstructionList(tomMatch334NameNumber_freshVar_44);
      }}}}{if ( (automata instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )automata) instanceof tom.engine.adt.tominstruction.types.instruction.Nop) ) {

        // tom uses nop in the iffalse part of ITE
        return  tom.engine.adt.il.types.instr.refuse.make() ;
      }}}}

    System.out.println("instrFromInstruction don't know how to handle this : " + automata);
    return  tom.engine.adt.il.types.instr.refuse.make() ;
  }

  private SubstitutionList abstractSubstitutionFromAccept(Instr instr) {
    SubstitutionList substitution =  tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ;
    {{if ( (instr instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )instr) instanceof tom.engine.adt.il.types.instr.accept) ) {

        Constraint positivePattern = Constraint.fromTerm( (( tom.engine.adt.il.types.Instr )instr).getPositive() );
        ArrayList<TomTerm> subjectList = new ArrayList<TomTerm>();
        try{
          tom_make_TopDown(tom_make_CollectSubjects(subjectList)).visitLight(positivePattern);
        }catch(VisitFailure e){
          throw new TomRuntimeException("VisitFailure in Verifier.abstractSubstitutionFromAccept:" + e.getMessage()); 
        }
        for(TomTerm subject: subjectList){
          {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {

              substitution = tom_append_list_subs(substitution, tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.is.make(variableFromTomName( (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ), termFromTomTerm(subject)) , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) )


;
            }}}}
          
        }
      }}}}

    return substitution;
  }

  public static class CollectSubjects extends tom.library.sl.AbstractStrategyBasic {private  java.util.List  list;public CollectSubjects( java.util.List  list) {super(( new tom.library.sl.Identity() ));this.list=list;}public  java.util.List  getlist() {return list;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {


        list.add( (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() );
      }}}}return _visit_Constraint(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectSubjects( java.util.List  t0) { return new CollectSubjects(t0);}



  public Collection build_tree(Instruction automata) {
    // System.out.println("Build derivation tree for: " + automata);

    // collects the accept in the automata
    Collection localAccepts = collectAccept(automata);

    Iterator iter = localAccepts.iterator();
    Collection treeList = new HashSet();
    while(iter.hasNext()) {
      Instr localAccept = (Instr) iter.next();

      // builds the initial abstract substitution
      SubstitutionList initialsubstitution = abstractSubstitutionFromAccept(localAccept);
      Environment startingenv =  tom.engine.adt.il.types.environment.env.make(initialsubstitution, instrFromInstruction(automata)) 
;

      Deriv startingderiv =  tom.engine.adt.il.types.deriv.ebs.make(startingenv,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) , localAccept) ) 
;

      Collection treeListPre = applySemanticsRules(startingderiv);
      // replace substitutions in trees
      Iterator it = treeListPre.iterator();
      while(it.hasNext()) {
        DerivTree tree = (DerivTree) it.next();
        SubstitutionList outputsubst = getOutputSubstitution(tree);
        tree = replaceUndefinedSubstitution(tree,outputsubst);
        treeList.add(tree);
      }
    }

    return treeList;
  }

  public Map<Instr,Expr> getConstraints(Instruction automata) {
    // collects the accept in the automata
    Collection localAccepts = collectAccept(automata);

    Iterator iter = localAccepts.iterator();
    Map<Instr,Expr> constraintList = new HashMap<Instr,Expr>();
    while(iter.hasNext()) {
      Instr localAccept = (Instr) iter.next();

      // builds the initial abstract substitution
      SubstitutionList initialsubstitution = abstractSubstitutionFromAccept(localAccept);
      Expr constraints = buildConstraint(initialsubstitution,
          instrFromInstruction(automata),
          localAccept);
      constraintList.put(localAccept,constraints);
    }
    return constraintList;
  }

  public static class substitutionCollector extends tom.library.sl.AbstractStrategyBasic {private  SubstRef  outsubst;public substitutionCollector( SubstRef  outsubst) {super(( new tom.library.sl.Identity() ));this.outsubst=outsubst;}public  SubstRef  getoutsubst() {return outsubst;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Expr  visit_Expr( tom.engine.adt.il.types.Expr  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )tom__arg) instanceof tom.engine.adt.il.types.expr.iltrue) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch338NameNumber_freshVar_1= (( tom.engine.adt.il.types.Expr )tom__arg).getSubst() ;if ( ((tomMatch338NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch338NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch338NameNumber_freshVar_1.isEmptysubs() )) {if ( ( tomMatch338NameNumber_freshVar_1.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch338NameNumber_freshVar_1.getTailsubs() .isEmptysubs() ) {( new tom.library.sl.Fail() )


.visitLight((( tom.engine.adt.il.types.Expr )tom__arg));
      }}}}}}}{if ( (tom__arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )tom__arg) instanceof tom.engine.adt.il.types.expr.iltrue) ) {

        // outsubst.set(`x);
        outsubst.set( (( tom.engine.adt.il.types.Expr )tom__arg).getSubst() );
      }}}}return _visit_Expr(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Expr  _visit_Expr( tom.engine.adt.il.types.Expr  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.il.types.Expr )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.il.types.Expr) ) {return ((T)visit_Expr((( tom.engine.adt.il.types.Expr )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_substitutionCollector( SubstRef  t0) { return new substitutionCollector(t0);}


  public SubstitutionList collectSubstitutionInConstraint(Expr expr) {
    SubstRef output = new SubstRef( tom.engine.adt.il.types.substitutionlist.Emptysubs.make() );
    try {
      ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?tom_make_substitutionCollector(output):new tom.library.sl.Sequence(tom_make_substitutionCollector(output),( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ))) ).visitLight(expr);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy substitutionCollector failed");
    }
    return output.get();
  }

  public static class outputSubstitutionCollector extends tom.library.sl.AbstractStrategyBasic {private  SubstRef  outsubst;public outputSubstitutionCollector( SubstRef  outsubst) {super(( new tom.library.sl.Identity() ));this.outsubst=outsubst;}public  SubstRef  getoutsubst() {return outsubst;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Deriv  visit_Deriv( tom.engine.adt.il.types.Deriv  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )tom__arg) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch339NameNumber_freshVar_1= (( tom.engine.adt.il.types.Deriv )tom__arg).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch339NameNumber_freshVar_2= (( tom.engine.adt.il.types.Deriv )tom__arg).getRhs() ;if ( (tomMatch339NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.environment.env) ) {if ( ( tomMatch339NameNumber_freshVar_1.getI()  instanceof tom.engine.adt.il.types.instr.accept) ) {if ( (tomMatch339NameNumber_freshVar_2 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch339NameNumber_freshVar_8= tomMatch339NameNumber_freshVar_2.getSubs() ;if ( ((tomMatch339NameNumber_freshVar_8 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch339NameNumber_freshVar_8 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch339NameNumber_freshVar_8.isEmptysubs() )) {if ( ( tomMatch339NameNumber_freshVar_8.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch339NameNumber_freshVar_8.getTailsubs() .isEmptysubs() ) {if ( ( tomMatch339NameNumber_freshVar_2.getI()  instanceof tom.engine.adt.il.types.instr.accept) ) {


        outsubst.set( tomMatch339NameNumber_freshVar_1.getSubs() );
      }}}}}}}}}}}}return _visit_Deriv(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Deriv  _visit_Deriv( tom.engine.adt.il.types.Deriv  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.il.types.Deriv )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.il.types.Deriv) ) {return ((T)visit_Deriv((( tom.engine.adt.il.types.Deriv )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_outputSubstitutionCollector( SubstRef  t0) { return new outputSubstitutionCollector(t0);}



  public SubstitutionList getOutputSubstitution(DerivTree subject) {
    SubstRef output = new SubstRef( tom.engine.adt.il.types.substitutionlist.Emptysubs.make() );
    try {
      tom_make_TopDown(tom_make_outputSubstitutionCollector(output)).visitLight(subject);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy outputSubstitutionCollector failed");
    }
    return output.get();
  }

  public static class acceptCollector extends tom.library.sl.AbstractStrategyBasic {private  java.util.Collection  store;public acceptCollector( java.util.Collection  store) {super(( new tom.library.sl.Identity() ));this.store=store;}public  java.util.Collection  getstore() {return store;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.TypedAction) ) {


        store.add( tom.engine.adt.il.types.instr.accept.make( (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getPositivePattern() .toATerm(),  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getNegativePatternList() .toATerm()) );
      }}}}return _visit_Instruction(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_acceptCollector( java.util.Collection  t0) { return new acceptCollector(t0);}



  public Collection collectAccept(Instruction subject) {
    Collection result = new HashSet();
    try {
      tom_make_TopDown(tom_make_acceptCollector(result)).visitLight(subject);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy collectAccept failed");
    }
    return result;
  }


  /**
   * The axioms the mapping has to verify
   */
  protected Seq seqFromTerm(Term sp) {
    TermList ded =  tom.engine.adt.il.types.termlist.ConsconcTerm.make(sp, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ;
    {{if ( (sp instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )sp) instanceof tom.engine.adt.il.types.term.appSubsT) ) {

        TermList follow = applyMappingRules(replaceVariablesInTerm(sp));
        ded = tom_append_list_concTerm(ded,tom_append_list_concTerm(follow, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ));
      }}}}

    return  tom.engine.adt.il.types.seq.dedterm.make(tom_append_list_concTerm(ded, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() )) ;
  }

  protected ExprList exprListFromExpr(Expr sp) {
    ExprList ded =  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(sp, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ;
    {{if ( (sp instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )sp) instanceof tom.engine.adt.il.types.expr.appSubsE) ) {

        ExprList follow = applyExprRules(replaceVariablesInExpr(sp));
        ded = tom_append_list_concExpr(ded,tom_append_list_concExpr(follow, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ));
      }}}}


    // System.out.println("dedexpr gives: " + ded);
    return ded;
  }

  protected SubstitutionList reduceSubstitutionWithMappingRules(SubstitutionList subst) {
    {{if ( (subst instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if ( (( tom.engine.adt.il.types.SubstitutionList )subst).isEmptysubs() ) {

        return subst;
      }}}}{if ( (subst instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( (( tom.engine.adt.il.types.SubstitutionList )subst).isEmptysubs() )) { tom.engine.adt.il.types.Substitution  tomMatch343NameNumber_freshVar_8= (( tom.engine.adt.il.types.SubstitutionList )subst).getHeadsubs() ;if ( (tomMatch343NameNumber_freshVar_8 instanceof tom.engine.adt.il.types.substitution.is) ) {

        SubstitutionList tail = reduceSubstitutionWithMappingRules( (( tom.engine.adt.il.types.SubstitutionList )subst).getTailsubs() );
        return  tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.is.make( tomMatch343NameNumber_freshVar_8.getVar() , reduceTermWithMappingRules(replaceVariablesInTerm( tom.engine.adt.il.types.term.appSubsT.make(tail,  tomMatch343NameNumber_freshVar_8.getTerm() ) ))) ,tom_append_list_subs(tail, tom.engine.adt.il.types.substitutionlist.Emptysubs.make() )) ;
      }}}}}{if ( (subst instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )subst) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( (( tom.engine.adt.il.types.SubstitutionList )subst).isEmptysubs() )) {if ( ( (( tom.engine.adt.il.types.SubstitutionList )subst).getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {

        SubstitutionList tail = reduceSubstitutionWithMappingRules( (( tom.engine.adt.il.types.SubstitutionList )subst).getTailsubs() );
        return  tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() ,tom_append_list_subs(tail, tom.engine.adt.il.types.substitutionlist.Emptysubs.make() )) ;
      }}}}}}

    return subst;
  }
  protected Expr reduceWithMappingRules(Expr ex) {
    {{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.eq) ) { tom.engine.adt.il.types.Term  tomMatch344NameNumber_freshVar_1= (( tom.engine.adt.il.types.Expr )ex).getLt() ; tom.engine.adt.il.types.Term  tomMatch344NameNumber_freshVar_2= (( tom.engine.adt.il.types.Expr )ex).getRt() ;if ( (tomMatch344NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.term.tau) ) {if ( (tomMatch344NameNumber_freshVar_2 instanceof tom.engine.adt.il.types.term.tau) ) {

        return  tom.engine.adt.il.types.expr.teq.make( tomMatch344NameNumber_freshVar_1.getAbst() ,  tomMatch344NameNumber_freshVar_2.getAbst() ) ;
      }}}}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.isfsym) ) { tom.engine.adt.il.types.Term  tomMatch344NameNumber_freshVar_9= (( tom.engine.adt.il.types.Expr )ex).getT() ;if ( (tomMatch344NameNumber_freshVar_9 instanceof tom.engine.adt.il.types.term.tau) ) {

        return  tom.engine.adt.il.types.expr.tisfsym.make( tomMatch344NameNumber_freshVar_9.getAbst() ,  (( tom.engine.adt.il.types.Expr )ex).getSymbol() ) ;
      }}}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.eq) ) {

        // first reduce the argument
        return reduceWithMappingRules( tom.engine.adt.il.types.expr.eq.make(reduceTermWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getLt() ), reduceTermWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getRt() )) );
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.isfsym) ) {

        return reduceWithMappingRules( tom.engine.adt.il.types.expr.isfsym.make(reduceTermWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getT() ),  (( tom.engine.adt.il.types.Expr )ex).getSymbol() ) );
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.ilnot) ) {

        return  tom.engine.adt.il.types.expr.ilnot.make(reduceWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getExp() )) ;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.iltrue) ) {

        return  tom.engine.adt.il.types.expr.iltrue.make(reduceSubstitutionWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getSubst() )) ;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

        return ex;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.iland) ) {

        return  tom.engine.adt.il.types.expr.iland.make(reduceWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getLeft() ), reduceWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getRight() )) ;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.ilor) ) {

        return  tom.engine.adt.il.types.expr.ilor.make(reduceWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getLeft() ), reduceWithMappingRules( (( tom.engine.adt.il.types.Expr )ex).getRight() )) ;
      }}}}

    System.out.println("reduceWithMappingRules : nothing applies to:" + ex);
    return ex;
  }

  protected Term reduceTermWithMappingRules(Term trm) {
    {{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.tau) ) {

        return trm;
      }}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.subterm) ) { tom.engine.adt.il.types.Term  tomMatch345NameNumber_freshVar_4= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch345NameNumber_freshVar_4 instanceof tom.engine.adt.il.types.term.subterm) ) {

        return reduceTermWithMappingRules( tom.engine.adt.il.types.term.subterm.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() , reduceTermWithMappingRules(tomMatch345NameNumber_freshVar_4),  (( tom.engine.adt.il.types.Term )trm).getIndex() ) );
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.slot) ) { tom.engine.adt.il.types.Term  tomMatch345NameNumber_freshVar_10= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch345NameNumber_freshVar_10 instanceof tom.engine.adt.il.types.term.slot) ) {

        return reduceTermWithMappingRules( tom.engine.adt.il.types.term.slot.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() , reduceTermWithMappingRules(tomMatch345NameNumber_freshVar_10),  (( tom.engine.adt.il.types.Term )trm).getName() ) );
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.subterm) ) { tom.engine.adt.il.types.Term  tomMatch345NameNumber_freshVar_16= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch345NameNumber_freshVar_16 instanceof tom.engine.adt.il.types.term.tau) ) {

        // we shall test if term t has symbol s
        AbsTerm term =  tom.engine.adt.il.types.absterm.st.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() ,  tomMatch345NameNumber_freshVar_16.getAbst() ,  (( tom.engine.adt.il.types.Term )trm).getIndex() ) ;
        return  tom.engine.adt.il.types.term.tau.make(term) ;
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.slot) ) { tom.engine.adt.il.types.Term  tomMatch345NameNumber_freshVar_23= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch345NameNumber_freshVar_23 instanceof tom.engine.adt.il.types.term.tau) ) {

        // we shall test if term t has symbol s
        AbsTerm term =  tom.engine.adt.il.types.absterm.sl.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() ,  tomMatch345NameNumber_freshVar_23.getAbst() ,  (( tom.engine.adt.il.types.Term )trm).getName() ) ;
        return  tom.engine.adt.il.types.term.tau.make(term) ;
      }}}}}

    System.out.println("reduceTermWithMappingRules : nothing applies to:" + trm);
    return trm;
  }

  protected TermList applyMappingRules(Term trm) {
    {{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.tau) ) {

        return  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ;
      }}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.subterm) ) { tom.engine.adt.il.types.Term  tomMatch346NameNumber_freshVar_4= (( tom.engine.adt.il.types.Term )trm).getT() ; tom.engine.adt.il.types.Symbol  tom_s= (( tom.engine.adt.il.types.Term )trm).getSymbol() ;if ( (tomMatch346NameNumber_freshVar_4 instanceof tom.engine.adt.il.types.term.subterm) ) { int  tom_index= (( tom.engine.adt.il.types.Term )trm).getIndex() ;

        // first reduce the argument
        TermList reduced = applyMappingRules(tomMatch346NameNumber_freshVar_4);
        TermList res =  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ;
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          if (head.istau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmptyconcTerm()) {
              Term h = hl.getHeadconcTerm();
              res = tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.subterm.make(tom_s, h, tom_index) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) );
              hl = hl.getTailconcTerm();
            }
          } else {
            res = tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.subterm.make(tom_s, head, tom_index) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) );
          }
          reduced = reduced.getTailconcTerm();
        }
        return tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() );
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.slot) ) { tom.engine.adt.il.types.Term  tomMatch346NameNumber_freshVar_10= (( tom.engine.adt.il.types.Term )trm).getT() ; tom.engine.adt.il.types.Symbol  tom_s= (( tom.engine.adt.il.types.Term )trm).getSymbol() ;if ( (tomMatch346NameNumber_freshVar_10 instanceof tom.engine.adt.il.types.term.slot) ) { String  tom_slotName= (( tom.engine.adt.il.types.Term )trm).getName() ;

        // first reduce the argument
        TermList reduced = applyMappingRules(tomMatch346NameNumber_freshVar_10);
        TermList res =  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ;
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          if (head.istau()) {
            TermList hl = applyMappingRules(head);
            while(!hl.isEmptyconcTerm()) {
              Term h = hl.getHeadconcTerm();
              res = tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.slot.make(tom_s, h, tom_slotName) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) );
              hl = hl.getTailconcTerm();
            }
          } else {
            res = tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.slot.make(tom_s, head, tom_slotName) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) );
          }
          reduced = reduced.getTailconcTerm();
        }
        return tom_append_list_concTerm(res, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() );
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.subterm) ) { tom.engine.adt.il.types.Term  tomMatch346NameNumber_freshVar_16= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch346NameNumber_freshVar_16 instanceof tom.engine.adt.il.types.term.tau) ) {

        // we shall test if term t has symbol s
        AbsTerm term =  tom.engine.adt.il.types.absterm.st.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() ,  tomMatch346NameNumber_freshVar_16.getAbst() ,  (( tom.engine.adt.il.types.Term )trm).getIndex() ) ;
        return  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.tau.make(term) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ) ;
      }}}}{if ( (trm instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )trm) instanceof tom.engine.adt.il.types.term.slot) ) { tom.engine.adt.il.types.Term  tomMatch346NameNumber_freshVar_23= (( tom.engine.adt.il.types.Term )trm).getT() ;if ( (tomMatch346NameNumber_freshVar_23 instanceof tom.engine.adt.il.types.term.tau) ) {

        // we shall test if term t has symbol s
        AbsTerm term =  tom.engine.adt.il.types.absterm.sl.make( (( tom.engine.adt.il.types.Term )trm).getSymbol() ,  tomMatch346NameNumber_freshVar_23.getAbst() ,  (( tom.engine.adt.il.types.Term )trm).getName() ) ;
        return  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.ConsconcTerm.make( tom.engine.adt.il.types.term.tau.make(term) , tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ) ;
      }}}}}

    System.out.println("apply TermRules : nothing applies to:" + trm);
    return  tom.engine.adt.il.types.termlist.ConsconcTerm.make(trm, tom.engine.adt.il.types.termlist.EmptyconcTerm.make() ) ;
  }

  protected ExprList applyExprRules(Expr ex) {
    {{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.eq) ) { tom.engine.adt.il.types.Term  tomMatch347NameNumber_freshVar_1= (( tom.engine.adt.il.types.Expr )ex).getLt() ; tom.engine.adt.il.types.Term  tomMatch347NameNumber_freshVar_2= (( tom.engine.adt.il.types.Expr )ex).getRt() ;if ( (tomMatch347NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.term.tau) ) {if ( (tomMatch347NameNumber_freshVar_2 instanceof tom.engine.adt.il.types.term.tau) ) {

        return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.teq.make( tomMatch347NameNumber_freshVar_1.getAbst() ,  tomMatch347NameNumber_freshVar_2.getAbst() ) , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ) ;
      }}}}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.isfsym) ) { tom.engine.adt.il.types.Term  tomMatch347NameNumber_freshVar_9= (( tom.engine.adt.il.types.Expr )ex).getT() ;if ( (tomMatch347NameNumber_freshVar_9 instanceof tom.engine.adt.il.types.term.tau) ) {

        return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.tisfsym.make( tomMatch347NameNumber_freshVar_9.getAbst() ,  (( tom.engine.adt.il.types.Expr )ex).getSymbol() ) , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ) ;
      }}}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.eq) ) {

        // first reduce the argument
        Term reducedl = applyMappingRules( (( tom.engine.adt.il.types.Expr )ex).getLt() ).reverse().getHeadconcTerm();
        Term reducedr = applyMappingRules( (( tom.engine.adt.il.types.Expr )ex).getRt() ).reverse().getHeadconcTerm();

        ExprList taill = applyExprRules( tom.engine.adt.il.types.expr.eq.make(reducedl, reducedr) );
        ExprList res =  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex,tom_append_list_concExpr(taill, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() )) ;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.isfsym) ) {

        // first reduce the argument
        TermList reduced = applyMappingRules( (( tom.engine.adt.il.types.Expr )ex).getT() );
        ExprList res =  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ;
        while(!reduced.isEmptyconcTerm()) {
          Term head = reduced.getHeadconcTerm();
          res = tom_append_list_concExpr(res, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.isfsym.make(head,  (( tom.engine.adt.il.types.Expr )ex).getSymbol() ) , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) );
          reduced = reduced.getTailconcTerm();
        }
        {{if ( (res instanceof tom.engine.adt.il.types.ExprList) ) {if ( (((( tom.engine.adt.il.types.ExprList )res) instanceof tom.engine.adt.il.types.exprlist.ConsconcExpr) || ((( tom.engine.adt.il.types.ExprList )res) instanceof tom.engine.adt.il.types.exprlist.EmptyconcExpr)) ) { tom.engine.adt.il.types.ExprList  tomMatch348NameNumber_end_4=(( tom.engine.adt.il.types.ExprList )res);do {{if (!( tomMatch348NameNumber_end_4.isEmptyconcExpr() )) {if (  tomMatch348NameNumber_end_4.getTailconcExpr() .isEmptyconcExpr() ) {

            ExprList taill = applyExprRules( tomMatch348NameNumber_end_4.getHeadconcExpr() );
            return tom_append_list_concExpr(tom_get_slice_concExpr((( tom.engine.adt.il.types.ExprList )res),tomMatch348NameNumber_end_4, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ),tom_append_list_concExpr(taill, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ));
          }}if ( tomMatch348NameNumber_end_4.isEmptyconcExpr() ) {tomMatch348NameNumber_end_4=(( tom.engine.adt.il.types.ExprList )res);} else {tomMatch348NameNumber_end_4= tomMatch348NameNumber_end_4.getTailconcExpr() ;}}} while(!( (tomMatch348NameNumber_end_4==(( tom.engine.adt.il.types.ExprList )res)) ));}}}}

      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.ilnot) ) {

        ExprList exprList = applyExprRules( (( tom.engine.adt.il.types.Expr )ex).getExp() );
        ExprList newExprList =  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ;
        while(!exprList.isEmptyconcExpr()) {
          Expr localExpr = exprList.getHeadconcExpr();
          exprList = exprList.getTailconcExpr();
          newExprList = tom_append_list_concExpr(newExprList, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.ilnot.make(localExpr) , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) );
        }
        return newExprList;
      }}}{if ( (ex instanceof tom.engine.adt.il.types.Expr) ) {boolean tomMatch347NameNumber_freshVar_27= false ;if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.iltrue) ) {tomMatch347NameNumber_freshVar_27= true ;} else {if ( ((( tom.engine.adt.il.types.Expr )ex) instanceof tom.engine.adt.il.types.expr.ilfalse) ) {tomMatch347NameNumber_freshVar_27= true ;}}if ( tomMatch347NameNumber_freshVar_27== true  ) {

        return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ;
      }}}}

    System.out.println("apply ExprRules : nothing applies to:" + ex);
    return  tom.engine.adt.il.types.exprlist.ConsconcExpr.make(ex, tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) ;
  }

  protected Expr buildConstraint(SubstitutionList substitution, Instr pil,Instr goal) {
    {{if ( (pil instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )pil) instanceof tom.engine.adt.il.types.instr.sequence) ) { tom.engine.adt.il.types.InstrList  tomMatch349NameNumber_freshVar_1= (( tom.engine.adt.il.types.Instr )pil).getInstrlist() ;if ( ((tomMatch349NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.instrlist.Conssemicolon) || (tomMatch349NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.instrlist.Emptysemicolon)) ) {if (!( tomMatch349NameNumber_freshVar_1.isEmptysemicolon() )) { tom.engine.adt.il.types.Instr  tom_h= tomMatch349NameNumber_freshVar_1.getHeadsemicolon() ; tom.engine.adt.il.types.InstrList  tom_t= tomMatch349NameNumber_freshVar_1.getTailsemicolon() ;

        Expr goalFromHead = buildConstraint(substitution,tom_h,goal);
        if (!tom_t.isEmptysemicolon()) {
          Expr refuseFromHead = buildConstraint(substitution,tom_h, tom.engine.adt.il.types.instr.refuse.make() );
          Expr goalFromTail = buildConstraint(substitution, tom.engine.adt.il.types.instr.sequence.make(tom_t) ,goal);
          if(this.isCamlSemantics()) {
            return  tom.engine.adt.il.types.expr.ilor.make(goalFromHead,  tom.engine.adt.il.types.expr.iland.make(refuseFromHead, goalFromTail) ) ;
          } else {
            return  tom.engine.adt.il.types.expr.ilor.make(goalFromHead, goalFromTail) ;
          }
        } else {
          return goalFromHead;
        }
      }}}}}{if ( (pil instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )pil) instanceof tom.engine.adt.il.types.instr.ILLet) ) {

        // update the substitution
        Term t = replaceVariablesInTerm( tom.engine.adt.il.types.term.appSubsT.make(substitution,  (( tom.engine.adt.il.types.Instr )pil).getT() ) );
        substitution = tom_append_list_subs(substitution, tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.is.make( (( tom.engine.adt.il.types.Instr )pil).getVar() , t) , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) );
        //return `iland(eq(tau(absvar(x)),u),buildConstraint(substitution,i,goal));
        return buildConstraint(substitution, (( tom.engine.adt.il.types.Instr )pil).getBody() ,goal);
      }}}{if ( (pil instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )pil) instanceof tom.engine.adt.il.types.instr.ITE) ) {

        Expr closedExpr = replaceVariablesInExpr( tom.engine.adt.il.types.expr.appSubsE.make(substitution,  (( tom.engine.adt.il.types.Instr )pil).getE() ) );
        Expr constraintTrue  =  tom.engine.adt.il.types.expr.iland.make(closedExpr, buildConstraint(substitution, (( tom.engine.adt.il.types.Instr )pil).getIft() ,goal)) ;
        Expr constraintFalse =  tom.engine.adt.il.types.expr.iland.make( tom.engine.adt.il.types.expr.ilnot.make(closedExpr) , buildConstraint(substitution, (( tom.engine.adt.il.types.Instr )pil).getIff() ,goal)) ;
        return  tom.engine.adt.il.types.expr.ilor.make(constraintTrue, constraintFalse) ;
      }}}{if ( (pil instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )pil) instanceof tom.engine.adt.il.types.instr.refuse) ) {

        if (pil == goal) {
          return  tom.engine.adt.il.types.expr.iltrue.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ) ;
        } else {
          return  tom.engine.adt.il.types.expr.ilfalse.make() ;
        }
      }}}{if ( (pil instanceof tom.engine.adt.il.types.Instr) ) {if ( ((( tom.engine.adt.il.types.Instr )pil) instanceof tom.engine.adt.il.types.instr.accept) ) {

        if (pil == goal) {
          return  tom.engine.adt.il.types.expr.iltrue.make(substitution) ;
        } else {
          return  tom.engine.adt.il.types.expr.ilfalse.make() ;
        }
      }}}}

    // default case, should not happen
    return  tom.engine.adt.il.types.expr.ilfalse.make() ;
  }

  protected Collection applySemanticsRules(Deriv post) {
    Collection c = new HashSet();
    {{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )post) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_1= (( tom.engine.adt.il.types.Deriv )post).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_2= (( tom.engine.adt.il.types.Deriv )post).getRhs() ;if ( (tomMatch350NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.Instr  tomMatch350NameNumber_freshVar_5= tomMatch350NameNumber_freshVar_1.getI() ; tom.engine.adt.il.types.SubstitutionList  tom_e= tomMatch350NameNumber_freshVar_1.getSubs() ;if ( (tomMatch350NameNumber_freshVar_5 instanceof tom.engine.adt.il.types.instr.sequence) ) { tom.engine.adt.il.types.InstrList  tomMatch350NameNumber_freshVar_7= tomMatch350NameNumber_freshVar_5.getInstrlist() ;if ( ((tomMatch350NameNumber_freshVar_7 instanceof tom.engine.adt.il.types.instrlist.Conssemicolon) || (tomMatch350NameNumber_freshVar_7 instanceof tom.engine.adt.il.types.instrlist.Emptysemicolon)) ) {if (!( tomMatch350NameNumber_freshVar_7.isEmptysemicolon() )) { tom.engine.adt.il.types.Instr  tom_h= tomMatch350NameNumber_freshVar_7.getHeadsemicolon() ;if ( (tomMatch350NameNumber_freshVar_2 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch350NameNumber_freshVar_9= tomMatch350NameNumber_freshVar_2.getSubs() ;if ( ((tomMatch350NameNumber_freshVar_9 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch350NameNumber_freshVar_9 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch350NameNumber_freshVar_9.isEmptysubs() )) {if ( ( tomMatch350NameNumber_freshVar_9.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch350NameNumber_freshVar_9.getTailsubs() .isEmptysubs() ) { tom.engine.adt.il.types.Instr  tom_ip= tomMatch350NameNumber_freshVar_2.getI() ;

        if(instructionContains(tom_h,tom_ip)) {
          // ends the derivation
          Deriv up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_e, tom_h) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) , tom_ip) ) ;
          Collection pre_list = applySemanticsRules(up);

          Iterator it = pre_list.iterator();
          while(it.hasNext()) {
            DerivTree pre = (DerivTree) it.next();
            c.add( tom.engine.adt.il.types.derivtree.derivrule.make("seqa", post, pre,  tom.engine.adt.il.types.seq.seq.make() ) );
          }
        } else {
          // continue the derivation with t
          Deriv up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_e,  tom.engine.adt.il.types.instr.sequence.make( tomMatch350NameNumber_freshVar_7.getTailsemicolon() ) ) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) , tom_ip) ) ;
          Collection post_list = applySemanticsRules(up);

          if(this.isCamlSemantics()) {
            up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_e, tom_h) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ,  tom.engine.adt.il.types.instr.refuse.make() ) ) ;
            Collection pre_list = applySemanticsRules(up);
            Iterator it = pre_list.iterator();
            while(it.hasNext()) {
              DerivTree pre = (DerivTree) it.next();
              Iterator it2 = post_list.iterator();
              while(it2.hasNext()) {
                DerivTree pre2 = (DerivTree) it2.next();
                c.add( tom.engine.adt.il.types.derivtree.derivrule2.make("seqb", post, pre, pre2,  tom.engine.adt.il.types.seq.seq.make() ) );
              }
            }
          } else {
            Iterator it = post_list.iterator();
            while(it.hasNext()) {
              DerivTree pre = (DerivTree) it.next();
              c.add( tom.engine.adt.il.types.derivtree.derivrule2.make("seqb", post,  tom.engine.adt.il.types.derivtree.endderiv.make() , pre,  tom.engine.adt.il.types.seq.seq.make() ) );
            }
          }
        }
      }}}}}}}}}}}}{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )post) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_19= (( tom.engine.adt.il.types.Deriv )post).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_20= (( tom.engine.adt.il.types.Deriv )post).getRhs() ;if ( (tomMatch350NameNumber_freshVar_19 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.Instr  tomMatch350NameNumber_freshVar_23= tomMatch350NameNumber_freshVar_19.getI() ; tom.engine.adt.il.types.SubstitutionList  tom_e= tomMatch350NameNumber_freshVar_19.getSubs() ;if ( (tomMatch350NameNumber_freshVar_23 instanceof tom.engine.adt.il.types.instr.ILLet) ) {if ( (tomMatch350NameNumber_freshVar_20 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch350NameNumber_freshVar_29= tomMatch350NameNumber_freshVar_20.getSubs() ;if ( ((tomMatch350NameNumber_freshVar_29 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch350NameNumber_freshVar_29 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch350NameNumber_freshVar_29.isEmptysubs() )) {if ( ( tomMatch350NameNumber_freshVar_29.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch350NameNumber_freshVar_29.getTailsubs() .isEmptysubs() ) {


        // build condition
        Seq cond = seqFromTerm( tom.engine.adt.il.types.term.appSubsT.make(tom_e,  tomMatch350NameNumber_freshVar_23.getT() ) );
        // find "t"
        Term t = null;
        {{if ( (cond instanceof tom.engine.adt.il.types.Seq) ) {if ( ((( tom.engine.adt.il.types.Seq )cond) instanceof tom.engine.adt.il.types.seq.dedterm) ) { tom.engine.adt.il.types.TermList  tomMatch351NameNumber_freshVar_1= (( tom.engine.adt.il.types.Seq )cond).getTerms() ;if ( ((tomMatch351NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.termlist.ConsconcTerm) || (tomMatch351NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.termlist.EmptyconcTerm)) ) { tom.engine.adt.il.types.TermList  tomMatch351NameNumber_end_6=tomMatch351NameNumber_freshVar_1;do {{if (!( tomMatch351NameNumber_end_6.isEmptyconcTerm() )) {if (  tomMatch351NameNumber_end_6.getTailconcTerm() .isEmptyconcTerm() ) {
 t =  tomMatch351NameNumber_end_6.getHeadconcTerm() ; }}if ( tomMatch351NameNumber_end_6.isEmptyconcTerm() ) {tomMatch351NameNumber_end_6=tomMatch351NameNumber_freshVar_1;} else {tomMatch351NameNumber_end_6= tomMatch351NameNumber_end_6.getTailconcTerm() ;}}} while(!( (tomMatch351NameNumber_end_6==tomMatch351NameNumber_freshVar_1) ));}}}}{if ( (cond instanceof tom.engine.adt.il.types.Seq) ) {
 if (t == null) {
            System.out.println("seqFromTerm has a problem with " + cond);
          }
          }}}

        Deriv up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_append_list_subs(tom_e, tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.is.make( tomMatch350NameNumber_freshVar_23.getVar() , t) , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ),  tomMatch350NameNumber_freshVar_23.getBody() ) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ,  tomMatch350NameNumber_freshVar_20.getI() ) 
            ) 


;
        Collection pre_list = applySemanticsRules(up);
        Iterator it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add( tom.engine.adt.il.types.derivtree.derivrule.make("let", post, pre, cond) );
        }
      }}}}}}}}}}{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )post) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_36= (( tom.engine.adt.il.types.Deriv )post).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_37= (( tom.engine.adt.il.types.Deriv )post).getRhs() ;if ( (tomMatch350NameNumber_freshVar_36 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.Instr  tomMatch350NameNumber_freshVar_40= tomMatch350NameNumber_freshVar_36.getI() ; tom.engine.adt.il.types.SubstitutionList  tom_e= tomMatch350NameNumber_freshVar_36.getSubs() ;if ( (tomMatch350NameNumber_freshVar_40 instanceof tom.engine.adt.il.types.instr.ITE) ) {if ( (tomMatch350NameNumber_freshVar_37 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch350NameNumber_freshVar_46= tomMatch350NameNumber_freshVar_37.getSubs() ;if ( ((tomMatch350NameNumber_freshVar_46 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch350NameNumber_freshVar_46 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch350NameNumber_freshVar_46.isEmptysubs() )) {if ( ( tomMatch350NameNumber_freshVar_46.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch350NameNumber_freshVar_46.getTailsubs() .isEmptysubs() ) { tom.engine.adt.il.types.Instr  tom_ip= tomMatch350NameNumber_freshVar_37.getI() ;


        // build condition
        ExprList cond = exprListFromExpr( tom.engine.adt.il.types.expr.appSubsE.make(tom_e,  tomMatch350NameNumber_freshVar_40.getE() ) );

        Deriv up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_e,  tomMatch350NameNumber_freshVar_40.getIft() ) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) , tom_ip) ) ;
        String rulename = "iftrue";

        Collection pre_list = applySemanticsRules(up);
        Iterator it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add( tom.engine.adt.il.types.derivtree.derivrule.make(rulename, post, pre,  tom.engine.adt.il.types.seq.dedexpr.make(tom_append_list_concExpr(cond, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.iltrue.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ) , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) )) ) );
        }

        up =  tom.engine.adt.il.types.deriv.ebs.make( tom.engine.adt.il.types.environment.env.make(tom_e,  tomMatch350NameNumber_freshVar_40.getIff() ) ,  tom.engine.adt.il.types.environment.env.make( tom.engine.adt.il.types.substitutionlist.Conssubs.make( tom.engine.adt.il.types.substitution.undefsubs.make() , tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) , tom_ip) ) ;
        rulename = "iffalse";

        pre_list = applySemanticsRules(up);
        it = pre_list.iterator();
        while(it.hasNext()) {
          DerivTree pre = (DerivTree) it.next();
          c.add( tom.engine.adt.il.types.derivtree.derivrule.make(rulename, post, pre,  tom.engine.adt.il.types.seq.dedexpr.make(tom_append_list_concExpr(cond, tom.engine.adt.il.types.exprlist.ConsconcExpr.make( tom.engine.adt.il.types.expr.ilfalse.make() , tom.engine.adt.il.types.exprlist.EmptyconcExpr.make() ) )) ) );
        }
      }}}}}}}}}}{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )post) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_53= (( tom.engine.adt.il.types.Deriv )post).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_54= (( tom.engine.adt.il.types.Deriv )post).getRhs() ;if ( (tomMatch350NameNumber_freshVar_53 instanceof tom.engine.adt.il.types.environment.env) ) {if ( ( tomMatch350NameNumber_freshVar_53.getI()  instanceof tom.engine.adt.il.types.instr.accept) ) {if ( (tomMatch350NameNumber_freshVar_54 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch350NameNumber_freshVar_60= tomMatch350NameNumber_freshVar_54.getSubs() ;if ( ((tomMatch350NameNumber_freshVar_60 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch350NameNumber_freshVar_60 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch350NameNumber_freshVar_60.isEmptysubs() )) {if ( ( tomMatch350NameNumber_freshVar_60.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch350NameNumber_freshVar_60.getTailsubs() .isEmptysubs() ) {if ( ( tomMatch350NameNumber_freshVar_54.getI()  instanceof tom.engine.adt.il.types.instr.accept) ) {


        c.add( tom.engine.adt.il.types.derivtree.derivrule.make("axiom_accept", post,  tom.engine.adt.il.types.derivtree.endderiv.make() ,  tom.engine.adt.il.types.seq.seq.make() ) );
      }}}}}}}}}}}{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {if ( ((( tom.engine.adt.il.types.Deriv )post) instanceof tom.engine.adt.il.types.deriv.ebs) ) { tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_68= (( tom.engine.adt.il.types.Deriv )post).getLhs() ; tom.engine.adt.il.types.Environment  tomMatch350NameNumber_freshVar_69= (( tom.engine.adt.il.types.Deriv )post).getRhs() ;if ( (tomMatch350NameNumber_freshVar_68 instanceof tom.engine.adt.il.types.environment.env) ) {if ( ( tomMatch350NameNumber_freshVar_68.getI()  instanceof tom.engine.adt.il.types.instr.refuse) ) {if ( (tomMatch350NameNumber_freshVar_69 instanceof tom.engine.adt.il.types.environment.env) ) { tom.engine.adt.il.types.SubstitutionList  tomMatch350NameNumber_freshVar_75= tomMatch350NameNumber_freshVar_69.getSubs() ;if ( ((tomMatch350NameNumber_freshVar_75 instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || (tomMatch350NameNumber_freshVar_75 instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( tomMatch350NameNumber_freshVar_75.isEmptysubs() )) {if ( ( tomMatch350NameNumber_freshVar_75.getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  tomMatch350NameNumber_freshVar_75.getTailsubs() .isEmptysubs() ) {if ( ( tomMatch350NameNumber_freshVar_69.getI()  instanceof tom.engine.adt.il.types.instr.refuse) ) {

        c.add( tom.engine.adt.il.types.derivtree.derivrule.make("axiom_refuse", post,  tom.engine.adt.il.types.derivtree.endderiv.make() ,  tom.engine.adt.il.types.seq.seq.make() ) );
      }}}}}}}}}}}{if ( (post instanceof tom.engine.adt.il.types.Deriv) ) {

        if (c.isEmpty()) {
          //System.out.println("Error " + post);
        }
      }}}

    return c;
  }

  public static class stratInstructionContains extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.il.types.Instr  goal;private  java.util.Collection  c;public stratInstructionContains( tom.engine.adt.il.types.Instr  goal,  java.util.Collection  c) {super(( new tom.library.sl.Identity() ));this.goal=goal;this.c=c;}public  tom.engine.adt.il.types.Instr  getgoal() {return goal;}public  java.util.Collection  getc() {return c;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Instr  visit_Instr( tom.engine.adt.il.types.Instr  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.il.types.Instr) ) { tom.engine.adt.il.types.Instr  tom_x=(( tom.engine.adt.il.types.Instr )tom__arg);


        if (tom_x== goal) {
          c.add(goal);
          ( new tom.library.sl.Fail() ).visitLight(tom_x);
        }
      }}}return _visit_Instr(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Instr  _visit_Instr( tom.engine.adt.il.types.Instr  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.il.types.Instr )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.il.types.Instr) ) {return ((T)visit_Instr((( tom.engine.adt.il.types.Instr )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_stratInstructionContains( tom.engine.adt.il.types.Instr  t0,  java.util.Collection  t1) { return new stratInstructionContains(t0,t1);}


  protected boolean instructionContains(Instr i, Instr goal) {
    Collection collect = new HashSet();
    try {
      ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?tom_make_stratInstructionContains(goal,collect):new tom.library.sl.Sequence(tom_make_stratInstructionContains(goal,collect),( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) ))) ).visitLight(i);
    } catch(tom.library.sl.VisitFailure e) {
      System.out.println("strategy instructionContains failed");
    }
    return !collect.isEmpty();
  }

  /**
   * To replace undefsubst in tree by the computed value
   * which leads to axiom
   */
  public static class replaceUndefsubs extends tom.library.sl.AbstractStrategyBasic {private  tom.engine.adt.il.types.SubstitutionList  arg;public replaceUndefsubs( tom.engine.adt.il.types.SubstitutionList  arg) {super(( new tom.library.sl.Identity() ));this.arg=arg;}public  tom.engine.adt.il.types.SubstitutionList  getarg() {return arg;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.SubstitutionList  visit_SubstitutionList( tom.engine.adt.il.types.SubstitutionList  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )tom__arg) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )tom__arg) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( (( tom.engine.adt.il.types.SubstitutionList )tom__arg).isEmptysubs() )) {if ( ( (( tom.engine.adt.il.types.SubstitutionList )tom__arg).getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {if (  (( tom.engine.adt.il.types.SubstitutionList )tom__arg).getTailsubs() .isEmptysubs() ) {


        return arg;
      }}}}}}}return _visit_SubstitutionList(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.SubstitutionList  _visit_SubstitutionList( tom.engine.adt.il.types.SubstitutionList  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.il.types.SubstitutionList )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.il.types.SubstitutionList) ) {return ((T)visit_SubstitutionList((( tom.engine.adt.il.types.SubstitutionList )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replaceUndefsubs( tom.engine.adt.il.types.SubstitutionList  t0) { return new replaceUndefsubs(t0);}



  private DerivTree replaceUndefinedSubstitution(DerivTree subject,
      SubstitutionList subs) {
    try {
      subject = (DerivTree) tom_make_TopDown(tom_make_replaceUndefsubs(subs)).visitLight(subject);
    } catch (tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("Strategy replaceUndefsubs failed");
    }
    return subject;
  }

  




  private class SubstRef {
    private SubstitutionList sublist;
    public SubstRef(SubstitutionList slist) {
      sublist = slist;
    }
    public void set(SubstitutionList ssublist) {
      this.sublist = ssublist;
    }
    public SubstitutionList get() {
      return sublist;
    }
  }

  /**
   * These functions deals with substitution application
   */
  public static class replaceVariableByTerm extends tom.library.sl.AbstractStrategyBasic {private  java.util.Map  map;public replaceVariableByTerm( java.util.Map  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.Map  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Term  visit_Term( tom.engine.adt.il.types.Term  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )tom__arg) instanceof tom.engine.adt.il.types.term.tau) ) { tom.engine.adt.il.types.AbsTerm  tomMatch354NameNumber_freshVar_1= (( tom.engine.adt.il.types.Term )tom__arg).getAbst() ;if ( (tomMatch354NameNumber_freshVar_1 instanceof tom.engine.adt.il.types.absterm.absvar) ) { tom.engine.adt.il.types.Variable  tomMatch354NameNumber_freshVar_3= tomMatch354NameNumber_freshVar_1.getVarname() ;if ( (tomMatch354NameNumber_freshVar_3 instanceof tom.engine.adt.il.types.variable.var) ) { tom.engine.adt.il.types.Variable  tom_v=tomMatch354NameNumber_freshVar_3;


        if (map.containsKey(tom_v)) {
          return (Term)map.get(tom_v);
        }
        return (( tom.engine.adt.il.types.Term )tom__arg);
      }}}}}}return _visit_Term(tom__arg,introspector); }@SuppressWarnings("unchecked")public  tom.engine.adt.il.types.Term  _visit_Term( tom.engine.adt.il.types.Term  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!( environment== null  )) {return (( tom.engine.adt.il.types.Term )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }@SuppressWarnings("unchecked")public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.il.types.Term) ) {return ((T)visit_Term((( tom.engine.adt.il.types.Term )v),introspector));}if (!( environment== null  )) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replaceVariableByTerm( java.util.Map  t0) { return new replaceVariableByTerm(t0);}



  public Term replaceVariablesInTerm(Term subject) {
    {{if ( (subject instanceof tom.engine.adt.il.types.Term) ) {if ( ((( tom.engine.adt.il.types.Term )subject) instanceof tom.engine.adt.il.types.term.appSubsT) ) { tom.engine.adt.il.types.Term  tom_term= (( tom.engine.adt.il.types.Term )subject).getT() ;

        Map map = buildVariableMap( (( tom.engine.adt.il.types.Term )subject).getSubs() , new HashMap());
        Term t = tom_term;
        try {
          t = (Term) tom_make_TopDown(tom_make_replaceVariableByTerm(map)).visitLight(tom_term);
        } catch (tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("Strategy replaceVariableByTerm failed");
        }
        return t;
      }}}}

    return subject;
  }

  public Expr replaceVariablesInExpr(Expr subject) {
    {{if ( (subject instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )subject) instanceof tom.engine.adt.il.types.expr.appSubsE) ) { tom.engine.adt.il.types.Expr  tom_term= (( tom.engine.adt.il.types.Expr )subject).getE() ;

        Map map = buildVariableMap( (( tom.engine.adt.il.types.Expr )subject).getSubs() , new HashMap());
        Expr t = tom_term;
        try {
          t = (Expr) tom_make_TopDown(tom_make_replaceVariableByTerm(map)).visitLight(tom_term);
        } catch (tom.library.sl.VisitFailure e) {
          throw new TomRuntimeException("Strategy replaceVariableByTerm failed");
        }
        return t;
      }}}}

    return subject;
  }

  private Map buildVariableMap(SubstitutionList sublist, Map map) {
    {{if ( (sublist instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if ( (( tom.engine.adt.il.types.SubstitutionList )sublist).isEmptysubs() ) {
 return map; }}}}{if ( (sublist instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( (( tom.engine.adt.il.types.SubstitutionList )sublist).isEmptysubs() )) {if ( ( (( tom.engine.adt.il.types.SubstitutionList )sublist).getHeadsubs()  instanceof tom.engine.adt.il.types.substitution.undefsubs) ) {
 return buildVariableMap( (( tom.engine.adt.il.types.SubstitutionList )sublist).getTailsubs() ,map);}}}}}{if ( (sublist instanceof tom.engine.adt.il.types.SubstitutionList) ) {if ( (((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Conssubs) || ((( tom.engine.adt.il.types.SubstitutionList )sublist) instanceof tom.engine.adt.il.types.substitutionlist.Emptysubs)) ) {if (!( (( tom.engine.adt.il.types.SubstitutionList )sublist).isEmptysubs() )) { tom.engine.adt.il.types.Substitution  tomMatch357NameNumber_freshVar_13= (( tom.engine.adt.il.types.SubstitutionList )sublist).getHeadsubs() ;if ( (tomMatch357NameNumber_freshVar_13 instanceof tom.engine.adt.il.types.substitution.is) ) {

        map.put( tomMatch357NameNumber_freshVar_13.getVar() , tomMatch357NameNumber_freshVar_13.getTerm() );
        return buildVariableMap( (( tom.engine.adt.il.types.SubstitutionList )sublist).getTailsubs() ,map);
      }}}}}}

    return null;
  }

  public void mappingReduce(Map input) {
    Iterator it = input.keySet().iterator();
    while(it.hasNext()) {
      Object key = it.next();
      Expr value = (Expr) input.get(key);
      input.put(key,reduceWithMappingRules(value));
    }
  }

  public void booleanReduce(Map input) {
    Iterator it = input.keySet().iterator();
    while(it.hasNext()) {
      Object key = it.next();
      Expr value = (Expr) input.get(key);
      input.put(key,booleanSimplify(value));
    }
  }

  public Expr booleanSimplify(Expr expr) {
    Strategy booleanSimplifier = new BooleanSimplifier();
    Expr res =  tom.engine.adt.il.types.expr.ilfalse.make() ;
    try {
      res = (Expr) tom_make_InnermostId(booleanSimplifier).visitLight(expr);
    } catch (tom.library.sl.VisitFailure e) {
      System.out.println("humm");
    }
    return res;
  }

  public class BooleanSimplifier extends AbstractStrategyBasic {
    public BooleanSimplifier() {
      super(( new tom.library.sl.Identity() ));
    }

    public Object visitLight(Object o, Introspector i) throws tom.library.sl.VisitFailure {
      if (o instanceof Expr) {
        Expr arg = (Expr) o;
        {{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.iland) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getLeft()  instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

            return  tom.engine.adt.il.types.expr.ilfalse.make() ;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.iland) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getRight()  instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

            return  tom.engine.adt.il.types.expr.ilfalse.make() ;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilor) ) { tom.engine.adt.il.types.Expr  tomMatch358NameNumber_freshVar_11= (( tom.engine.adt.il.types.Expr )arg).getLeft() ;if ( (tomMatch358NameNumber_freshVar_11 instanceof tom.engine.adt.il.types.expr.iltrue) ) {

            return tomMatch358NameNumber_freshVar_11;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilor) ) { tom.engine.adt.il.types.Expr  tomMatch358NameNumber_freshVar_17= (( tom.engine.adt.il.types.Expr )arg).getRight() ;if ( (tomMatch358NameNumber_freshVar_17 instanceof tom.engine.adt.il.types.expr.iltrue) ) {

            return tomMatch358NameNumber_freshVar_17;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilor) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getLeft()  instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

            return  (( tom.engine.adt.il.types.Expr )arg).getRight() ;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilor) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getRight()  instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

            return  (( tom.engine.adt.il.types.Expr )arg).getLeft() ;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilnot) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getExp()  instanceof tom.engine.adt.il.types.expr.iltrue) ) {

            return  tom.engine.adt.il.types.expr.ilfalse.make() ;
          }}}}{if ( (arg instanceof tom.engine.adt.il.types.Expr) ) {if ( ((( tom.engine.adt.il.types.Expr )arg) instanceof tom.engine.adt.il.types.expr.ilnot) ) {if ( ( (( tom.engine.adt.il.types.Expr )arg).getExp()  instanceof tom.engine.adt.il.types.expr.ilfalse) ) {

            return  tom.engine.adt.il.types.expr.iltrue.make( tom.engine.adt.il.types.substitutionlist.Emptysubs.make() ) ;
          }}}}}

      }
      return any.visitLight(o,i);
    }
  }

}
