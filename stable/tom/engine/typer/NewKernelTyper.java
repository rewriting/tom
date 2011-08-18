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
* Claudia Tavares  e-mail: Claudia.Tavares@loria.fr
* Jean-Christophe Bach e-mail: Jeanchristophe.Bach@loria.fr
*
**/



package tom.engine.typer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.logging.Logger;

import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.code.types.*;
import tom.engine.adt.typeconstraints.types.*;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class NewKernelTyper {


  private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.Sequence )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ) == null )) {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.Sequence.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ),tom_append_list_Sequence(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.Sequence.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.Sequence.make(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( begin instanceof tom.library.sl.Sequence ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.Choice )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ) ==null )) {
        return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.Choice.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ),tom_append_list_Choice(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.Choice.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.Choice.make(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( begin instanceof tom.library.sl.Choice ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_SequenceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 == null )) {
      return l2;
    } else if(( l2 == null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.SequenceId )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ) == null )) {
        return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.SequenceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.FIRST) ),tom_append_list_SequenceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.SequenceId.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.SequenceId.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_SequenceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.SequenceId.make(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_SequenceId(((( begin instanceof tom.library.sl.SequenceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.SequenceId.THEN) ): null ),end,tail)) ;
  }
  
  private static   tom.library.sl.Strategy  tom_append_list_ChoiceId( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {
    if(( l1 ==null )) {
      return l2;
    } else if(( l2 ==null )) {
      return l1;
    } else if(( l1 instanceof tom.library.sl.ChoiceId )) {
      if(( ( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ) ==null )) {
        return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),l2) ;
      } else {
        return  tom.library.sl.ChoiceId.make(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.FIRST) ),tom_append_list_ChoiceId(( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.ChoiceId.THEN) ),l2)) ;
      }
    } else {
      return  tom.library.sl.ChoiceId.make(l1,l2) ;
    }
  }
  private static   tom.library.sl.Strategy  tom_get_slice_ChoiceId( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {
    if( (begin.equals(end)) ) {
      return tail;
    } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals( null )) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.library.sl.ChoiceId.make(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_ChoiceId(((( begin instanceof tom.library.sl.ChoiceId ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.ChoiceId.THEN) ): null ),end,tail)) ;
  }
  private static  tom.library.sl.Strategy  tom_make_AUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) , tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.Identity() )) ), null ) ) , null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_EUCtl( tom.library.sl.Strategy  s1,  tom.library.sl.Strategy  s2) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(s2, tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s1, tom.library.sl.Sequence.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("x") )) ), null ) ) , null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  s) { 
return ( 
 tom.library.sl.Choice.make(s, tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) )

;
}
private static  tom.library.sl.Strategy  tom_make_Repeat( tom.library.sl.Strategy  s) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make( tom.library.sl.Sequence.make(s, tom.library.sl.Sequence.make(( new tom.library.sl.MuVar("_x") ), null ) ) , tom.library.sl.Choice.make(( new tom.library.sl.Identity() ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try( tom.library.sl.Sequence.make(v, tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), null ) ) )) ))

;
}
private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { 
return (
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) 

;
}
private static  tom.library.sl.Strategy  tom_make_TopDownIdStopOnSuccess( tom.library.sl.Strategy  v) { 
return (
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ), null ) ) ) )) 

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDown( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Choice.make(v, tom.library.sl.Choice.make(( new tom.library.sl.One(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_RepeatId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_OnceTopDownId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.ChoiceId.make(v, tom.library.sl.ChoiceId.make(( new tom.library.sl.OneId(( new tom.library.sl.MuVar("_x") )) ), null ) ) ) ))

;
}
private static  tom.library.sl.Strategy  tom_make_InnermostId( tom.library.sl.Strategy  v) { 
return ( 
( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ), tom.library.sl.Sequence.make(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ), tom.library.sl.Sequence.make( tom.library.sl.SequenceId.make(v, tom.library.sl.SequenceId.make(( new tom.library.sl.MuVar("_x") ), null ) ) , null ) ) ) ))

;
}

  private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_append_list_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList l1,  tom.engine.adt.typeconstraints.types.TypeConstraintList  l2) {
    if( l1.isEmptyconcTypeConstraint() ) {
      return l2;
    } else if( l2.isEmptyconcTypeConstraint() ) {
      return l1;
    } else if(  l1.getTailconcTypeConstraint() .isEmptyconcTypeConstraint() ) {
      return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,l2) ;
    } else {
      return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( l1.getHeadconcTypeConstraint() ,tom_append_list_concTypeConstraint( l1.getTailconcTypeConstraint() ,l2)) ;
    }
  }
  private static   tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_get_slice_concTypeConstraint( tom.engine.adt.typeconstraints.types.TypeConstraintList  begin,  tom.engine.adt.typeconstraints.types.TypeConstraintList  end, tom.engine.adt.typeconstraints.types.TypeConstraintList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTypeConstraint()  ||  (end== tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( begin.getHeadconcTypeConstraint() ,( tom.engine.adt.typeconstraints.types.TypeConstraintList )tom_get_slice_concTypeConstraint( begin.getTailconcTypeConstraint() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {
    if( l1.isEmptyconcTomTerm() ) {
      return l2;
    } else if( l2.isEmptyconcTomTerm() ) {
      return l1;
    } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {
      return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;
    } else {
      return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomtype.types.TomTypeList  tom_append_list_concTomType( tom.engine.adt.tomtype.types.TomTypeList l1,  tom.engine.adt.tomtype.types.TomTypeList  l2) {
    if( l1.isEmptyconcTomType() ) {
      return l2;
    } else if( l2.isEmptyconcTomType() ) {
      return l1;
    } else if(  l1.getTailconcTomType() .isEmptyconcTomType() ) {
      return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,l2) ;
    } else {
      return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( l1.getHeadconcTomType() ,tom_append_list_concTomType( l1.getTailconcTomType() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomtype.types.TomTypeList  tom_get_slice_concTomType( tom.engine.adt.tomtype.types.TomTypeList  begin,  tom.engine.adt.tomtype.types.TomTypeList  end, tom.engine.adt.tomtype.types.TomTypeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomType()  ||  (end== tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make( begin.getHeadconcTomType() ,( tom.engine.adt.tomtype.types.TomTypeList )tom_get_slice_concTomType( begin.getTailconcTomType() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_append_list_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList l1,  tom.engine.adt.tomtype.types.TypeOptionList  l2) {
    if( l1.isEmptyconcTypeOption() ) {
      return l2;
    } else if( l2.isEmptyconcTypeOption() ) {
      return l1;
    } else if(  l1.getTailconcTypeOption() .isEmptyconcTypeOption() ) {
      return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,l2) ;
    } else {
      return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( l1.getHeadconcTypeOption() ,tom_append_list_concTypeOption( l1.getTailconcTypeOption() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomtype.types.TypeOptionList  tom_get_slice_concTypeOption( tom.engine.adt.tomtype.types.TypeOptionList  begin,  tom.engine.adt.tomtype.types.TypeOptionList  end, tom.engine.adt.tomtype.types.TypeOptionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTypeOption()  ||  (end== tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( begin.getHeadconcTypeOption() ,( tom.engine.adt.tomtype.types.TypeOptionList )tom_get_slice_concTypeOption( begin.getTailconcTypeOption() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.CodeList  tom_append_list_concCode( tom.engine.adt.code.types.CodeList l1,  tom.engine.adt.code.types.CodeList  l2) {
    if( l1.isEmptyconcCode() ) {
      return l2;
    } else if( l2.isEmptyconcCode() ) {
      return l1;
    } else if(  l1.getTailconcCode() .isEmptyconcCode() ) {
      return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.codelist.ConsconcCode.make( l1.getHeadconcCode() ,tom_append_list_concCode( l1.getTailconcCode() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.CodeList  tom_get_slice_concCode( tom.engine.adt.code.types.CodeList  begin,  tom.engine.adt.code.types.CodeList  end, tom.engine.adt.code.types.CodeList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcCode()  ||  (end== tom.engine.adt.code.types.codelist.EmptyconcCode.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.codelist.ConsconcCode.make( begin.getHeadconcCode() ,( tom.engine.adt.code.types.CodeList )tom_get_slice_concCode( begin.getTailconcCode() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.BQTermList  tom_append_list_concBQTerm( tom.engine.adt.code.types.BQTermList l1,  tom.engine.adt.code.types.BQTermList  l2) {
    if( l1.isEmptyconcBQTerm() ) {
      return l2;
    } else if( l2.isEmptyconcBQTerm() ) {
      return l1;
    } else if(  l1.getTailconcBQTerm() .isEmptyconcBQTerm() ) {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( l1.getHeadconcBQTerm() ,tom_append_list_concBQTerm( l1.getTailconcBQTerm() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTermList  tom_get_slice_concBQTerm( tom.engine.adt.code.types.BQTermList  begin,  tom.engine.adt.code.types.BQTermList  end, tom.engine.adt.code.types.BQTermList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcBQTerm()  ||  (end== tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make( begin.getHeadconcBQTerm() ,( tom.engine.adt.code.types.BQTermList )tom_get_slice_concBQTerm( begin.getTailconcBQTerm() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.code.types.BQTerm  tom_append_list_Composite( tom.engine.adt.code.types.BQTerm l1,  tom.engine.adt.code.types.BQTerm  l2) {
    if( l1.isEmptyComposite() ) {
      return l2;
    } else if( l2.isEmptyComposite() ) {
      return l1;
    } else if(  l1.getTailComposite() .isEmptyComposite() ) {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,l2) ;
    } else {
      return  tom.engine.adt.code.types.bqterm.ConsComposite.make( l1.getHeadComposite() ,tom_append_list_Composite( l1.getTailComposite() ,l2)) ;
    }
  }
  private static   tom.engine.adt.code.types.BQTerm  tom_get_slice_Composite( tom.engine.adt.code.types.BQTerm  begin,  tom.engine.adt.code.types.BQTerm  end, tom.engine.adt.code.types.BQTerm  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyComposite()  ||  (end== tom.engine.adt.code.types.bqterm.EmptyComposite.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.code.types.bqterm.ConsComposite.make( begin.getHeadComposite() ,( tom.engine.adt.code.types.BQTerm )tom_get_slice_Composite( begin.getTailComposite() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {
    if( l1.isEmptyconcConstraintInstruction() ) {
      return l2;
    } else if( l2.isEmptyconcConstraintInstruction() ) {
      return l1;
    } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {
      return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;
    } else {
      return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {
    if( l1.isEmptyconcTomName() ) {
      return l2;
    } else if( l2.isEmptyconcTomName() ) {
      return l1;
    } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {
      return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;
    } else {
      return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {
    if( l1.isEmptyconcSlot() ) {
      return l2;
    } else if( l2.isEmptyconcSlot() ) {
      return l1;
    } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {
      return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;
    } else {
      return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {
    if( l1.isEmptyconcOption() ) {
      return l2;
    } else if( l2.isEmptyconcOption() ) {
      return l1;
    } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {
      return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;
    } else {
      return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {
    if( l1.isEmptyAndConstraint() ) {
      return l2;
    } else if( l2.isEmptyAndConstraint() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
      if(  l1.getTailAndConstraint() .isEmptyAndConstraint() ) {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,l2) ;
      } else {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make( l1.getHeadAndConstraint() ,tom_append_list_AndConstraint( l1.getTailAndConstraint() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_AndConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyAndConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getHeadAndConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_AndConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )? begin.getTailAndConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_append_list_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  l1,  tom.engine.adt.tomconstraint.types.Constraint  l2) {
    if( l1.isEmptyOrConstraint() ) {
      return l2;
    } else if( l2.isEmptyOrConstraint() ) {
      return l1;
    } else if( ((l1 instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (l1 instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
      if(  l1.getTailOrConstraint() .isEmptyOrConstraint() ) {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,l2) ;
      } else {
        return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make( l1.getHeadOrConstraint() ,tom_append_list_OrConstraint( l1.getTailOrConstraint() ,l2)) ;
      }
    } else {
      return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(l1,l2) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.Constraint  tom_get_slice_OrConstraint( tom.engine.adt.tomconstraint.types.Constraint  begin,  tom.engine.adt.tomconstraint.types.Constraint  end, tom.engine.adt.tomconstraint.types.Constraint  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyOrConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getHeadOrConstraint() :begin),( tom.engine.adt.tomconstraint.types.Constraint )tom_get_slice_OrConstraint((( ((begin instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || (begin instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )? begin.getTailOrConstraint() : tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ),end,tail)) ;
  }
  
  private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {
    if( l1.isEmptyconcConstraint() ) {
      return l2;
    } else if( l2.isEmptyconcConstraint() ) {
      return l1;
    } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {
      return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;
    } else {
      return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;
    }
  }
  private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {
    if( (begin==end) ) {
      return tail;
    } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {
      /* code to avoid a call to make, and thus to avoid looping during list-matching */
      return begin;
    }
    return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;
  }
  

private static Logger logger = Logger.getLogger("tom.engine.typer.NewKernelTyper");




private int freshTypeVarCounter;
private int limTVarSymbolTable;

/*
* pem: why use a state variable here ?
*/
// List for variables of pattern (match constraints)
private TomList varPatternList;
// List for variables of subject and of numeric constraints
private BQTermList varList;

/*
* pem: why use a state variable here ?
*/
// List for type variables of patterns 
private TomTypeList positiveTVarList;
// List for equation constraints (for fresh type variables)
private TypeConstraintList equationConstraints;
// List for subtype constraints (for fresh type variables)
private TypeConstraintList subtypeConstraints;
// Set of pairs (freshVar,type)
private HashMap<TomType,TomType> substitutions;
// Set of supertypes for each type
private HashMap<String,TomTypeList> dependencies = new
HashMap<String,TomTypeList>();

private SymbolTable symbolTable;

private String currentInputFileName;
private boolean lazyType = false;

protected void setLazyType() {
lazyType = true;
}

protected void setSymbolTable(SymbolTable symbolTable) {
this.symbolTable = symbolTable;
}

protected void putSymbol(String name, TomSymbol astSymbol) {
symbolTable.putSymbol(name,astSymbol);
}

protected SymbolTable getSymbolTable() {
return symbolTable;
}

protected void setCurrentInputFileName(String currentInputFileName) {
this.currentInputFileName = currentInputFileName;
}

protected String getCurrentInputFileName() {
return currentInputFileName;
}
protected TomType getCodomain(TomSymbol tSymbol) {
return TomBase.getSymbolCodomain(tSymbol);
}

protected TomSymbol getSymbolFromTerm(TomTerm tTerm) {
return TomBase.getSymbolFromTerm(tTerm, symbolTable);
}

protected TomSymbol getSymbolFromTerm(BQTerm bqTerm) {
return TomBase.getSymbolFromTerm(bqTerm,symbolTable);
}

protected TomSymbol getSymbolFromName(String tName) {
return TomBase.getSymbolFromName(tName, symbolTable);
}
/*
protected TomSymbol getSymbolFromName(String tName) {
return symbolTable.getSymbolFromName(tName);
}
*/

protected TomSymbol getSymbolFromType(TomType tType) {
return TomBase.getSymbolFromType(tType,symbolTable); 
}

/**
* The method <code>addSubstitution</code> adds a substitutions (i.e. a pair
* (type1,type2) where type2 is the substitution for type1) into the
* global list "substitutions" and saturate it.
* For example, to add a pair (X,Y) where X is a type variable and Y is a type
* which can be a type variable or a ground type, we follow two steps:
* <p>
* STEP 1:  a) put(X,Z) if (Y,Z) is in substitutions or
*          b) put(X,Y) otherwise
* <p>
* STEP 2:  a) put(W,Z) after step 1.a or put(W,Y) after step 1.b
*             if there exist (W,X) in substitutions for each (W,X) in substitutions
*          b) do nothing otherwise
* @param key   the first argument of the pair to be inserted (i.e. the type1) 
* @param value the second argument of the pair to be inserted (i.e. the type2) 
*/
private void addSubstitution(TomType key, TomType value) {
/* STEP 1 */
TomType newValue = value;
if (substitutions.containsKey(value)) {
newValue = substitutions.get(value); 
} 
substitutions.put(key,newValue);

/* STEP 2 */
if (substitutions.containsValue(key)) {
TomType valueOfCurrentKey;
for (TomType currentKey : substitutions.keySet()) {
valueOfCurrentKey = substitutions.get(currentKey);
if (valueOfCurrentKey == key) {
substitutions.put(currentKey,newValue);
}
}
}
}

/**
* The method <code>getType</code> gets the type of a term by consulting the
* SymbolTable.
* @param bqTerm  the BQTerm requesting a type
* @return        the type of the BQTerm
*/
protected TomType getType(BQTerm bqTerm) {

{
{
if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch343_6= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_4= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_5= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch343_1= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_3= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch343_6= true ;
tomMatch343_3=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_1= tomMatch343_3.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch343_6= true ;
tomMatch343_4=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_1= tomMatch343_4.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.FunctionCall) ) {
{
tomMatch343_6= true ;
tomMatch343_5=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_1= tomMatch343_5.getAstType() ;

}
}
}
}
}
}
}
if (tomMatch343_6) {
return 
tomMatch343_1; 

}

}

}
{
if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch343_22= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_14= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_10= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_17= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_13= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_16= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_11= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_12= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_15= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch343_18= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch343_8= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch343_22= true ;
tomMatch343_10=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_10.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConstant) ) {
{
tomMatch343_22= true ;
tomMatch343_11=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_11.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildTerm) ) {
{
tomMatch343_22= true ;
tomMatch343_12=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_12.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyList) ) {
{
tomMatch343_22= true ;
tomMatch343_13=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_13.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConsList) ) {
{
tomMatch343_22= true ;
tomMatch343_14=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_14.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendList) ) {
{
tomMatch343_22= true ;
tomMatch343_15=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_15.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildEmptyArray) ) {
{
tomMatch343_22= true ;
tomMatch343_16=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_16.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildConsArray) ) {
{
tomMatch343_22= true ;
tomMatch343_17=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_17.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BuildAppendArray) ) {
{
tomMatch343_22= true ;
tomMatch343_18=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch343_8= tomMatch343_18.getAstName() ;

}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
}
if (tomMatch343_22) {
if ( (tomMatch343_8 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch343_8) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch343_8.getString() );
//DEBUG System.out.println("In getType with BQAppl " + `bqTerm + "\n");
//DEBUG System.out.println("In getType with type " + getCodomain(tSymbol) + "\n");
return getCodomain(tSymbol);


}
}
}

}

}


}

throw new TomRuntimeException("getType(BQTerm): should not be here.");
}

/**
* The method <code>getType</code> gets the type of a term by consulting the
* SymbolTable
* @param tTerm the TomTerm requesting a type
* @return      the type of the TomTerm
*/
protected TomType getType(TomTerm tTerm) {

{
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
return getType(
 (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getTomTerm() ); 

}
}
}

}
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch344_9= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch344_5= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch344_7= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch344_8= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch344_9= true ;
tomMatch344_7=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch344_5= tomMatch344_7.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch344_9= true ;
tomMatch344_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch344_5= tomMatch344_8.getAstType() ;

}
}
}
}
}
if (tomMatch344_9) {
return 
tomMatch344_5; 

}

}

}
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch344_11= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getNameList() ;
if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch344_11) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch344_11) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch344_11.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch344_18= tomMatch344_11.getHeadconcTomName() ;
if ( (tomMatch344_18 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch344_18) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

TomSymbol tSymbol = getSymbolFromName(
 tomMatch344_18.getString() );
return getCodomain(tSymbol);


}
}
}
}
}
}
}

}


}

throw new TomRuntimeException("getType(TomTerm): should not be here.");
}

/**
* The method <code>getInfoFromTomTerm</code> creates a pair
* (name,information) for a given term by consulting its attributes.
* @param tTerm  the TomTerm requesting the informations
* @return       the information about the TomTerm
*/
protected Info getInfoFromTomTerm(TomTerm tTerm) {

{
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
return getInfoFromTomTerm(
 (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getTomTerm() ); 

}
}
}

}
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch345_10= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch345_8= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch345_5= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch345_6= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch345_9= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch345_10= true ;
tomMatch345_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch345_5= tomMatch345_8.getOptions() ;
tomMatch345_6= tomMatch345_8.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch345_10= true ;
tomMatch345_9=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch345_5= tomMatch345_9.getOptions() ;
tomMatch345_6= tomMatch345_9.getAstName() ;

}
}
}
}
}
if (tomMatch345_10) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch345_6, tomMatch345_5) ; 


}

}

}
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch345_13= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getNameList() ;
if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch345_13) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch345_13) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
if (!( tomMatch345_13.isEmptyconcTomName() )) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch345_13.getHeadconcTomName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)).getOptions() ) ; 


}
}
}
}
}

}


}

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
}

/**
* The method <code>getInfoFromBQTerm</code> creates a pair
* (name,information) for a given term by consulting its attributes.
* @param bqTerm the BQTerm requesting the informations
* @return       the information about the BQTerm
*/
protected Info getInfoFromBQTerm(BQTerm bqTerm) {

{
{
if ( (((Object)bqTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch346_7= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch346_1= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch346_6= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch346_4= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch346_2= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch346_5= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch346_7= true ;
tomMatch346_4=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch346_1= tomMatch346_4.getOptions() ;
tomMatch346_2= tomMatch346_4.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch346_7= true ;
tomMatch346_5=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch346_1= tomMatch346_5.getOptions() ;
tomMatch346_2= tomMatch346_5.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch346_7= true ;
tomMatch346_6=(( tom.engine.adt.code.types.BQTerm )((Object)bqTerm));
tomMatch346_1= tomMatch346_6.getOptions() ;
tomMatch346_2= tomMatch346_6.getAstName() ;

}
}
}
}
}
}
}
if (tomMatch346_7) {

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tomMatch346_2, tomMatch346_1) ; 


}

}

}

}

return 
 tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tom.engine.adt.tomname.types.tomname.Name.make("") ,  tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) ; 
}

/**
* The method <code>setLimTVarSymbolTable</code> sets the lower bound of the
* counter of type variables. This methods is called by the TyperPlugin
* after replacing all unknown types of the SymbolTable by type variables and
* before start the type inference.
* @param freshTVarSymbolTable  the lower bound of the counter of type
*                              variables
*/
protected void setLimTVarSymbolTable(int freshTVarSymbolTable) {
limTVarSymbolTable = freshTVarSymbolTable;
}

/**
* The method <code>getFreshTlTIndex</code> increments the counter of type variables. 
* @return  the incremented counter of type variables
*/
protected int getFreshTlTIndex() {
return freshTypeVarCounter++;
}

/**
* The method <code>getUnknownFreshTypeVar</code> generates a fresh type
* variable (by considering the global counter of type variables)
* @return  a new (fresh) type variable
*/
protected TomType getUnknownFreshTypeVar() {
TomType tType = symbolTable.TYPE_UNKNOWN;

{
{
if ( (((Object)tType) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
return 
 tom.engine.adt.tomtype.types.tomtype.TypeVar.make( (( tom.engine.adt.tomtype.types.TomType )((Object)tType)).getTomType() , getFreshTlTIndex()) ; 

}
}
}

}

}

throw new TomRuntimeException("getUnknownFreshTypeVar: should not be here.");
}

/**
* The method <code>containsConstraint</code> checks if a given constraint
* already exists in a constraint type list. The method considers symmetry for
* equation constraints. 
* @param tConstraint the constraint to be considered
* @param tCList      the type constraint list to be traversed
* @return            'true' if the constraint already exists in the list
*                    'false' otherwise            
*/
protected boolean containsConstraint(TypeConstraint tConstraint, TypeConstraintList
tCList) {

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch348__end__9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch348__end__9.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch348_14= tomMatch348__end__9.getHeadconcTypeConstraint() ;
boolean tomMatch348_19= false ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch348_16= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch348_15= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch348_12= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch348_13= null ;
if ( (tomMatch348_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch348_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch348_19= true ;
tomMatch348_15=tomMatch348_14;
tomMatch348_12= tomMatch348_15.getType1() ;
tomMatch348_13= tomMatch348_15.getType2() ;

}
} else {
if ( (tomMatch348_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch348_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch348_19= true ;
tomMatch348_16=tomMatch348_14;
tomMatch348_12= tomMatch348_16.getType1() ;
tomMatch348_13= tomMatch348_16.getType2() ;

}
}
}
}
}
if (tomMatch348_19) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ==tomMatch348_12) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ==tomMatch348_13) ) {
return true; 
}
}
}

}
if ( tomMatch348__end__9.isEmptyconcTypeConstraint() ) {
tomMatch348__end__9=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch348__end__9= tomMatch348__end__9.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch348__end__9==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch348__end__29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch348__end__29.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch348_34= tomMatch348__end__29.getHeadconcTypeConstraint() ;
if ( (tomMatch348_34 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch348_34) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() == tomMatch348_34.getType1() ) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() == tomMatch348_34.getType2() ) ) {
return true; 
}
}
}
}
}
if ( tomMatch348__end__29.isEmptyconcTypeConstraint() ) {
tomMatch348__end__29=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch348__end__29= tomMatch348__end__29.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch348__end__29==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch348__end__47=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch348__end__47.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch348_52= tomMatch348__end__47.getHeadconcTypeConstraint() ;
if ( (tomMatch348_52 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch348_52) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() == tomMatch348_52.getType1() ) ) {
if ( ( (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() == tomMatch348_52.getType2() ) ) {
return true; 
}
}
}
}
}
if ( tomMatch348__end__47.isEmptyconcTypeConstraint() ) {
tomMatch348__end__47=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch348__end__47= tomMatch348__end__47.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch348__end__47==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}
}
}
}

}


}

return false;
} 

/*
* pem: use if(...==... && typeConstraints.contains(...))
*/
/**
* The method <code>addEqConstraint</code> insert an equation (i.e. a type
* constraint) into a given type constraint list only if this constraint does
* not yet exist into the list and if it does not contains "EmptyTypes". 
* @param tConstraint the equation to be inserted into the type constraint list
* @param tCList      the constraint type list where the constraint will be
*                    inserted
* @return            the list resulting of the insertion
*/
protected TypeConstraintList addEqConstraint(TypeConstraint tConstraint,
TypeConstraintList tCList) {
if (!containsConstraint(tConstraint,tCList)) {

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch349_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch349_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch349_1;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch349_2;
boolean tomMatch349_12= false ;
if ( (tomMatch349_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch349_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t2==tomMatch349_2) ) {
tomMatch349_12= true ;
}
}
}
if (!(tomMatch349_12)) {
boolean tomMatch349_11= false ;
if ( (tomMatch349_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch349_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t1==tomMatch349_1) ) {
tomMatch349_11= true ;
}
}
}
if (!(tomMatch349_11)) {
if (!( (tom_t2==tom_t1) )) {

return 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}
}

}

}
}
}

}

}

}
return tCList;
}

/**
* The method <code>addSubConstraint</code> insert a subtype constraint (i.e. a type
* constraint) into a given type constraint list only if this constraint does
* not yet exist into the list and if it does not contains "EmptyTypes". 
* @param tConstraint the subtype constraint to be inserted into the type
*                    constraint list
* @param tCList      the constraint type list where the constraint will be
*                    inserted
* @return            the list resulting of the insertion
*/
protected TypeConstraintList addSubConstraint(TypeConstraint tConstraint,
TypeConstraintList tCList) {
if (!containsConstraint(tConstraint,tCList)) {
//DEBUG System.out.println("addSubConstraint: tCList = " + tCList);

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch350_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch350_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch350_1;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch350_2;
boolean tomMatch350_12= false ;
if ( (tomMatch350_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch350_2) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t2==tomMatch350_2) ) {
tomMatch350_12= true ;
}
}
}
if (!(tomMatch350_12)) {
boolean tomMatch350_11= false ;
if ( (tomMatch350_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch350_1) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {
if ( (tom_t1==tomMatch350_1) ) {
tomMatch350_11= true ;
}
}
}
if (!(tomMatch350_11)) {
if (!( (tom_t2==tom_t1) )) {

return
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(tCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}
}

}

}
}
}

}

}

}
return tCList;
}

/**
* The method <code>generateDependencies</code> generates a
* hashMap called "dependencies" having pairs (typeName,supertypesList), where
* supertypeslist is a list with all the related proper supertypes for each
* ground type used into a code. The list is obtained by reflexive and
* transitive closure over the direct supertype relation defined by the user
* when defining the mappings.
* For example, to generate the supertypes of T2, where T2 is a ground type, we
* follow two steps:
* <p>
* STEP 1:  a) get the list supertypes_T3, put(T2,({T3} U supertype_T3)) and go
*          to step 2, if there exists a declaration of form T2<:T3
*          b) put(T2,{}), otherwise 
* <p>
* STEP 2:  put(T1,(supertypes_T1 U supertypes_T2)), for each (T1,{...,T2,...}) in dependencies
*/
// TODO: optimization 1 - add a test before adding a new pair in the hash map "dependencies" 
// TODO: optimization 2 - generate the dependencies once at the beginning of
// type inference and for all types of symbol table instead of only "used
// types".
protected void generateDependencies() {
TomTypeList superTypes;
TomTypeList supOfSubTypes;
String currentTypeName;
for(TomType currentType:symbolTable.getUsedTypes()) {
currentTypeName = currentType.getTomType();
superTypes = 
 tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
//DEBUG System.out.println("In generateDependencies -- for 1 : currentType = " +
//DEBUG    currentType);

{
{
if ( (((Object)currentType) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)currentType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)currentType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch351_1= (( tom.engine.adt.tomtype.types.TomType )((Object)currentType)).getTypeOptions() ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch351_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch351_1) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch351__end__8=tomMatch351_1;
do {
{
if (!( tomMatch351__end__8.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch351_12= tomMatch351__end__8.getHeadconcTypeOption() ;
if ( (tomMatch351_12 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch351_12) instanceof tom.engine.adt.tomtype.types.typeoption.SubtypeDecl) ) {
 String  tom_supTypeName= tomMatch351_12.getTomType() ;

//DEBUG System.out.println("In generateDependencies -- match : supTypeName = "
//DEBUG     + `supTypeName + " and supType = " + supType);
if (dependencies.containsKey(
tom_supTypeName)) {
superTypes = dependencies.get(
tom_supTypeName); 
}
TomType supType = symbolTable.getType(
tom_supTypeName);
if (supType != null) {
superTypes = 
 tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(supType,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;  

/* STEP 2 */
for(String subType:dependencies.keySet()) {
supOfSubTypes = dependencies.get(
subType);
//DEBUG System.out.println("In generateDependencies -- for 2: supOfSubTypes = " +
//DEBUG     supOfSubTypes);

{
{
if ( (((Object)supOfSubTypes) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch352__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes));
do {
{
if (!( tomMatch352__end__4.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tomMatch352_8= tomMatch352__end__4.getHeadconcTomType() ;
if ( (tomMatch352_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch352_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if (  (( tom.engine.adt.tomtype.types.TomType )((Object)currentType)).getTomType() .equals( tomMatch352_8.getTomType() ) ) {

/* 
* Replace list of superTypes of "subType" by a new one
* containing the superTypes of "currentType" which is also a
* superType 
*/
dependencies.put(subType,
tom_append_list_concTomType(supOfSubTypes,tom_append_list_concTomType(superTypes, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )));


}
}
}
}
if ( tomMatch352__end__4.isEmptyconcTomType() ) {
tomMatch352__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes));
} else {
tomMatch352__end__4= tomMatch352__end__4.getTailconcTomType() ;
}

}
} while(!( (tomMatch352__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supOfSubTypes))) ));
}
}

}

}

}
} else {
TomMessage.error(logger,getCurrentInputFileName(),0,
TomMessage.typetermNotDefined,
tom_supTypeName);
}


}
}
}
if ( tomMatch351__end__8.isEmptyconcTypeOption() ) {
tomMatch351__end__8=tomMatch351_1;
} else {
tomMatch351__end__8= tomMatch351__end__8.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch351__end__8==tomMatch351_1) ));
}
}
}
}

}

}

//DEBUG System.out.println("In generateDependencies -- end: superTypes = " +
//DEBUG     superTypes);
dependencies.put(currentTypeName,superTypes);
}
}


protected void addPositiveTVar(TomType tType) {
positiveTVarList = 
 tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(tType,tom_append_list_concTomType(positiveTVarList, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;
}

/**
* The method <code>addTomTerm</code> insert a TomTerm into the global
* <code>varPatternList</code>.
* @param tTerm  the TomTerm to be inserted
*/
protected void addTomTerm(TomTerm tTerm) {
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make(tTerm,tom_append_list_concTomTerm(varPatternList, tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() )) ;
}

/**
* The method <code>addBQTerm</code> insert a BQTerm into the global
* <code>varList</code>.
* @param bqTerm  the BQTerm to be inserted
*/
protected void addBQTerm(BQTerm bqTerm) {
varList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(bqTerm,tom_append_list_concBQTerm(varList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}

/**
* The method <code>resetVarList</code> checks if <code>varList</code> contains
* a BQTerm which is in the <code>localVarPatternList</code> but is not in
* <code>varPatternList</code> (i.e. the globalVarPatternList</code>. Then,
* this BQTerm is removed from <code>varList</code>.
* @param localVarPatternList   the TomList to be reset
*/
protected void resetVarList(TomList localVarPatternList) {
BQTermList bqTList = varList;
for(TomTerm tTerm: varPatternList.getCollectionconcTomTerm()) {

{
{
if ( (((Object)tTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch353_27= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch353_6= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch353_3= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch353_5= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch353_27= true ;
tomMatch353_5=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch353_3= tomMatch353_5.getAstName() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch353_27= true ;
tomMatch353_6=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm));
tomMatch353_3= tomMatch353_6.getAstName() ;

}
}
}
}
}
if (tomMatch353_27) {
if ( (((Object)localVarPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((Object)bqTList) instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch353__end__10=(( tom.engine.adt.code.types.BQTermList )((Object)bqTList));
do {
{
if (!( tomMatch353__end__10.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch353_14= tomMatch353__end__10.getHeadconcBQTerm() ;
boolean tomMatch353_26= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch353_13= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch353_15= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch353_16= null ;
if ( (tomMatch353_14 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch353_14) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch353_26= true ;
tomMatch353_15=tomMatch353_14;
tomMatch353_13= tomMatch353_15.getAstName() ;

}
} else {
if ( (tomMatch353_14 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch353_14) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch353_26= true ;
tomMatch353_16=tomMatch353_14;
tomMatch353_13= tomMatch353_16.getAstName() ;

}
}
}
}
}
if (tomMatch353_26) {
if ( (tomMatch353_3==tomMatch353_13) ) {
boolean tomMatch353_25= false ;
if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch353__end__20=(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList));
do {
{
if (!( tomMatch353__end__20.isEmptyconcTomTerm() )) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tTerm))== tomMatch353__end__20.getHeadconcTomTerm() ) ) {
tomMatch353_25= true ;
}
}
if ( tomMatch353__end__20.isEmptyconcTomTerm() ) {
tomMatch353__end__20=(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList));
} else {
tomMatch353__end__20= tomMatch353__end__20.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch353__end__20==(( tom.engine.adt.tomterm.types.TomList )((Object)localVarPatternList))) ));
}
if (!(tomMatch353_25)) {

bqTList = 
tom_append_list_concBQTerm(tom_get_slice_concBQTerm((( tom.engine.adt.code.types.BQTermList )((Object)bqTList)),tomMatch353__end__10, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ),tom_append_list_concBQTerm( tomMatch353__end__10.getTailconcBQTerm() , tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ));


}

}
}

}
if ( tomMatch353__end__10.isEmptyconcBQTerm() ) {
tomMatch353__end__10=(( tom.engine.adt.code.types.BQTermList )((Object)bqTList));
} else {
tomMatch353__end__10= tomMatch353__end__10.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch353__end__10==(( tom.engine.adt.code.types.BQTermList )((Object)bqTList))) ));
}
}
}
}

}

}

}

}
varList = bqTList;
}

/**
* The method <code>init</code> reset the counter of type variables
* <code>freshTypeVarCounter</code> and empties all global lists and hashMaps which means to
* empty <code>varPatternList</code>, <code>varList</code>,
* <code>equationConstraints</code>, <code>subtypeConstraints</code> and <code>substitutions</code>
*/
private void init() {
freshTypeVarCounter = limTVarSymbolTable;
varPatternList = 
 tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ;
varList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;
positiveTVarList = 
 tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
equationConstraints = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
subtypeConstraints = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
substitutions = new HashMap<TomType,TomType>();
}

/**
* The method <code>collectKnownTypesFromCode</code> creates an instance of
* the class <code>CollectKnownTypes</code> and calls its method
* <code>visitLight</code> to traverse a code. 
* @param  subject the code to be traversed/transformed
* @return         the code resulting of a transformation
*/
private Code collectKnownTypesFromCode(Code subject) {
try {
return 
tom_make_TopDownIdStopOnSuccess(tom_make_CollectKnownTypes(this)).visitLight(subject);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("typeUnknownTypes: failure on " + subject);
}
}

/**
* The class <code>CollectKnownTypes</code> is generated from a strategy which
* initially types all terms by using their correspondent type in symbol table
* or a fresh type variable :
* CASE 1 : Type(name, EmptyTargetLanguageType()) -> Type(name, foundType) if
* name is in TypeTable
* CASE 2 : Type(name, EmptyTargetLanguageType()) -> TypeVar(name, Index(i))
* if name is not in TypeTable
* @param nkt an instance of object NewKernelTyper
* @return    the code resulting of a transformation
*/

public static class CollectKnownTypes extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public CollectKnownTypes( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {
return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TargetLanguageType  tomMatch354_2= (( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)).getTlType() ;
 String  tom_tomType= (( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)).getTomType() ;
if ( (tomMatch354_2 instanceof tom.engine.adt.tomtype.types.TargetLanguageType) ) {
if ( ((( tom.engine.adt.tomtype.types.TargetLanguageType )tomMatch354_2) instanceof tom.engine.adt.tomtype.types.targetlanguagetype.EmptyTargetLanguageType) ) {

TomType newType = nkt.symbolTable.getType(
tom_tomType);
if (newType == null) {
// This happens when :
// * tomType != unknown type AND (newType == null)
// * tomType == unknown type
newType = 
 tom.engine.adt.tomtype.types.tomtype.TypeVar.make(tom_tomType, nkt.getFreshTlTIndex()) ;
}
return newType;


}
}
}
}
}

}

}
return _visit_TomType(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_CollectKnownTypes( NewKernelTyper  t0) { 
return new CollectKnownTypes(t0);
}


/**
* The method <code>inferAllTypes</code> is the start-up of the inference
* process. It is a generic method and it is called for the first time by the
* NewTyper.
* @param term        the expression needing type inference
* @param contextType the type related to the current expression
* @return            the resulting typed expression 
*/
public <T extends tom.library.sl.Visitable> T inferAllTypes(T term, TomType contextType) {
try {
return

tom_make_TopDownStopOnSuccess(tom_make_inferTypes(contextType,this)).visitLight(term); 
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferAllTypes: failure on " + term);
}
}

/**
* The class <code>inferTypes</code> is generated from a strategy which
* tries to infer types of all variables on a given expression
* <p> 
* It starts by searching for a Code <code>Tom</code> or
* <code>TomInclude</code> and calling <code>inferCodeList</code> in order to
* apply rule CT-BLOCK for each block of ConstraintInstruction.
* <p>
* Then it searches for a Instruction
* <code>Match(constraintInstructionList,option)</code> and calls
* <code>inferConstraintInstructionList</code> in order to apply rule CT-RULE
* for each single constraintInstruction
* <p>
* Then it searches for variables and star variables (TomTerms and BQTerms) and
* applies rules CT-ALIAS, CT-ANTI, CT-VAR, CT-SVAR, CT-FUN,
* CT-EMPTY, CT-ELEM, CT-MERGE or CT-STAR to a "pattern" (a TomTerm) or a
* "subject" (a BQTerm) in order to infer types of its variables.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* CT-ALIAS rule:
* IF found "x@e:A and "x:T" already exists in context
* THEN adds a type constraint "A = T" and infers type of "e:A"
* <p>
* CT-ANTI rule:
* IF found "!(e):A"
* THEN infers type of "e:A"
* <p>
* CT-VAR rule (resp. CT-SVAR): 
* IF found "x:A" (resp. "x*:A") and "x:A1" (resp. "x*:A1") already exists in
*    context 
* THEN adds a type constraint "A1 <: A"
* <p>
* CT-FUN rule:
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and add a type constraint "T <:A"
* <p>
* CT-EMPTY rule:
* IF found "l():A" and "l:T1*->T" exists in SymbolTable
* THEN adds a type constraint "T <: A"       
* <p>
* CT-ELEM rule:
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
* <p>
* CT-MERGE rule:
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" represents a list with
*      head symbol "l"
* <p>
* CT-STAR rule:
* Equals to CT-MERGE but with a star variable "x*" instead of "e"
* This rule is necessary because it differed from CT-MERGE in the
* sense of the type of the last argument ("x*" here) is unknown 
* @param contextType the fresh generated previously and attributed to the term
* @param nkt an instance of object NewKernelTyper
*/

public static class inferTypes extends tom.library.sl.AbstractStrategyBasic {
private  tom.engine.adt.tomtype.types.TomType  contextType;
private  NewKernelTyper  nkt;
public inferTypes( tom.engine.adt.tomtype.types.TomType  contextType,  NewKernelTyper  nkt) {
super(( new tom.library.sl.Fail() ));
this.contextType=contextType;
this.nkt=nkt;
}
public  tom.engine.adt.tomtype.types.TomType  getcontextType() {
return contextType;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));
}
if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.code.types.Code) ) {
return ((T)visit_Code((( tom.engine.adt.code.types.Code )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.Code  _visit_Code( tom.engine.adt.code.types.Code  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.Code )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
 tom.engine.adt.code.types.BQTerm  tom_bqVar=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));

//DEBUG System.out.println("InferTypes:BQTerm bqVar -- contextType = " +
//DEBUG     contextType);
nkt.checkNonLinearityOfBQVariables(
tom_bqVar);
TypeConstraintList newSubConstraints = nkt.subtypeConstraints;
nkt.subtypeConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ) ) ,newSubConstraints);  
//DEBUG System.out.println("InferTypes:BQTerm bqVar -- constraint = " +
//DEBUG `aType + " <: " + contextType);
return 
tom_bqVar;


}
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
 tom.engine.adt.code.types.BQTerm  tom_bqVarStar=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));

//DEBUG System.out.println("InferTypes:BQTerm bqVarStar -- contextType = " +
//DEBUG     contextType);
nkt.checkNonLinearityOfBQVariables(
tom_bqVarStar);
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstType() , contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ) ) ,newEqConstraints);  
//DEBUG System.out.println("InferTypes:BQTerm bqVarStar -- constraint = " +
//DEBUG `aType + " = " + contextType);
return 
tom_bqVarStar;


}
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch355_14= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getAstName() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getOptions() ;
if ( (tomMatch355_14 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch355_14) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 String  tom_name= tomMatch355_14.getString() ;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch355_14;
 tom.engine.adt.code.types.BQTermList  tom_bqTList= (( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)).getArgs() ;

//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. tomName = " + `name);
TomSymbol tSymbol = nkt.getSymbolFromName(
tom_name);
if (tSymbol == null) {
//The contextType is used here, so it must be a ground type, not a
//type variable
//DEBUG System.out.println("visit contextType = " + contextType);
tSymbol = nkt.getSymbolFromType(contextType);
if (tSymbol != null && 
tom_name.equals("")) {
// In case of contextType is "TypeVar(name,i)"

tom_aName= tSymbol.getAstName();
}
}

TomType codomain = contextType;
if (tSymbol == null) {
tSymbol = 
 tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
} else {
TomSymbol newtSymbol = tSymbol;

{
{
if ( (((Object)newtSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch356_2= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getAstName() ;
if ( (tomMatch356_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch356_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch356_8= tomMatch356_2.getCodomain() ;
if ( (tomMatch356_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch356_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

codomain = 
tomMatch356_8;
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
// Apply decoration for types of list operators
TypeOptionList newTOptions = 
 tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch356_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;
codomain = 
 tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch356_8.getTomType() ,  tomMatch356_8.getTlType() ) ;
tSymbol =

 tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch356_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getOptions() ) ; 
}  


}
}
}
}
}
}
}

}

}

TypeConstraintList newSubConstraints = nkt.subtypeConstraints;
nkt.subtypeConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newSubConstraints);
}

BQTermList newBQTList = 
tom_bqTList;
if (!
tom_bqTList.isEmptyconcBQTerm()) {
//DEBUG System.out.println("\n Test pour BQTerm-inferTypes in BQAppl. bqTList = " + `bqTList);
newBQTList =
nkt.inferBQTermList(
tom_bqTList,
tSymbol,codomain);
}

// TO VERIFY

{
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

return 
 tom.engine.adt.code.types.bqterm.FunctionCall.make(tom_aName, contextType, newBQTList) ; 


}
}
}

}

}

return 
 tom.engine.adt.code.types.bqterm.BQAppl.make(tom_optionList, tom_aName, newBQTList) ;


}
}
}
}
}

}


}
return _visit_BQTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
nkt.inferAllTypes(
 (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getTomTerm() ,contextType); 

}
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ;
 tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));

//DEBUG System.out.println("InferTypes:TomTerm var = " + `var);
nkt.checkNonLinearityOfVariables(
tom_var);
TypeConstraintList newSubConstraints = nkt.subtypeConstraints;
nkt.subtypeConstraints =
nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ) ) ,newSubConstraints);  
//DEBUG System.out.println("InferTypes:TomTerm var -- constraint = " +
//DEBUG     `aType + " <: " + contextType);
ConstraintList newCList = 
tom_cList;

{
{
if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch359_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;
if ( (tomMatch359_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch359_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch359_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasvar -- constraint = " +
//DEBUG   nkt.getType(`boundTerm) + " = " + `contextType);
//nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,newEqConstraints); 


}
}
}
}
}
}

}

}




return 
tom_var.setConstraints(newCList);


}
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstType() ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ;
 tom.engine.adt.tomterm.types.TomTerm  tom_varStar=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));

//DEBUG System.out.println("InferTypes:TomTerm varStar = " + `varStar);
nkt.checkNonLinearityOfVariables(
tom_varStar);
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getAstName() ,  (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ) ) ,newEqConstraints);  
//DEBUG System.out.println("InferTypes:TomTerm varStar -- constraint = " +
//DEBUG     `aType + " = " + contextType);
ConstraintList newCList = 
tom_cList;

{
{
if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch360_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;
if ( (tomMatch360_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch360_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch360_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasvar -- constraint = " +
//DEBUG   nkt.getType(`boundTerm) + " = " + `contextType);
//nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), tom_aType, nkt.getInfoFromTomTerm(tom_boundTerm)) ,newEqConstraints); 


}
}
}
}
}
}

}

}

return 
tom_varStar.setConstraints(newCList);


}
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 tom.engine.adt.tomname.types.TomNameList  tomMatch358_20= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getNameList() ;
 tom.engine.adt.tomoption.types.OptionList  tom_optionList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getOptions() ;
if ( (((( tom.engine.adt.tomname.types.TomNameList )tomMatch358_20) instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || ((( tom.engine.adt.tomname.types.TomNameList )tomMatch358_20) instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {
 tom.engine.adt.tomname.types.TomNameList  tom_nList=tomMatch358_20;
if (!( tomMatch358_20.isEmptyconcTomName() )) {
 tom.engine.adt.tomname.types.TomName  tomMatch358_29= tomMatch358_20.getHeadconcTomName() ;
if ( (tomMatch358_29 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch358_29) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {
 tom.engine.adt.tomslot.types.SlotList  tom_sList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getSlots() ;
 tom.engine.adt.tomconstraint.types.ConstraintList  tom_cList= (( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)).getConstraints() ;

// In case of a String, tomName is "" for ("a","b")
TomSymbol tSymbol = nkt.getSymbolFromName(
 tomMatch358_29.getString() );
// IF_1
if (tSymbol == null) {
//The contextType is used here, so it must be a ground type, not a
//type variable
tSymbol = nkt.getSymbolFromType(contextType);

// IF_2
if (tSymbol != null) {
// In case of contextType is "TypeVar(name,i)"

tom_nList= 
 tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ;
} 
}
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. tSymbol = " + `tSymbol);
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl. astName = " +`concTomName(tSymbol.getAstName()));
//DEBUG System.out.println("\n Test pour TomTerm-inferTypes in RecordAppl.
//tSymbol = " + tSymbol);

TomType codomain = contextType;

// IF_3
if (tSymbol == null) {
//DEBUG System.out.println("tSymbol is still null!");
tSymbol = 
 tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol.make() ;
} else {
// This code can not be moved to IF_2 because tSymbol may don't be
// "null" since the begginning and then does not enter into neither IF_1 nor
// IF_2
TomSymbol newtSymbol = tSymbol;

{
{
if ( (((Object)newtSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch361_2= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getTypesToType() ;
 tom.engine.adt.tomname.types.TomName  tom_symName= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getAstName() ;
if ( (tomMatch361_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch361_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch361_8= tomMatch361_2.getCodomain() ;
if ( (tomMatch361_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch361_8) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {

codomain = 
tomMatch361_8;
if(TomBase.isListOperator(
tSymbol) || TomBase.isArrayOperator(
tSymbol)) {
// Apply decoration for types of list operators
TypeOptionList newTOptions = 
 tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption.make( tom.engine.adt.tomtype.types.typeoption.WithSymbol.make(tom_symName) ,tom_append_list_concTypeOption( tomMatch361_8.getTypeOptions() , tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption.make() )) ;
codomain = 
 tom.engine.adt.tomtype.types.tomtype.Type.make(newTOptions,  tomMatch361_8.getTomType() ,  tomMatch361_8.getTlType() ) ;
tSymbol =

 tom.engine.adt.tomsignature.types.tomsymbol.Symbol.make(tom_symName,  tom.engine.adt.tomtype.types.tomtype.TypesToType.make( tomMatch361_2.getDomain() , codomain) ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getPairNameDeclList() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)newtSymbol)).getOptions() ) ; 
}  


}
}
}
}
}
}
}

}

}

TypeConstraintList newSubConstraints = nkt.subtypeConstraints;
nkt.subtypeConstraints = nkt.addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(codomain, contextType,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make( tomMatch358_20.getHeadconcTomName() , tom_optionList) ) ,newSubConstraints);
}

ConstraintList newCList = 
tom_cList;

{
{
if ( (((Object)tom_cList) instanceof tom.engine.adt.tomconstraint.types.ConstraintList) ) {
if ( (((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint) || ((( tom.engine.adt.tomconstraint.types.ConstraintList )(( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList))) instanceof tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint)) ) {
if (!( (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).isEmptyconcConstraint() )) {
 tom.engine.adt.tomconstraint.types.Constraint  tomMatch362_4= (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getHeadconcConstraint() ;
if ( (tomMatch362_4 instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tomMatch362_4) instanceof tom.engine.adt.tomconstraint.types.constraint.AliasTo) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_boundTerm= tomMatch362_4.getVar() ;
if (  (( tom.engine.adt.tomconstraint.types.ConstraintList )((Object)tom_cList)).getTailconcConstraint() .isEmptyconcConstraint() ) {

//DEBUG System.out.println("InferTypes:TomTerm aliasrecordappl -- constraint = " +
//DEBUG     nkt.getType(`boundTerm) + " = " + contextType);
//nkt.addConstraint(`Equation(nkt.getType(boundTerm),contextType,nkt.getInfoFromTomTerm(boundTerm))); 
TypeConstraintList newEqConstraints = nkt.equationConstraints;
nkt.equationConstraints =
nkt.addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(nkt.getType(tom_boundTerm), codomain, nkt.getInfoFromTomTerm(tom_boundTerm)) ,newEqConstraints); 


}
}
}
}
}
}

}

}


SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
if (!
tom_sList.isEmptyconcSlot()) {

newSList=
nkt.inferSlotList(
tom_sList,tSymbol,codomain);
}
return 
 tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_optionList, tom_nList, newSList, newCList) ;


}
}
}
}
}
}
}

}


}
return _visit_TomTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)) instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomVisit )(( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg))) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) {

//DEBUG System.out.println("vnode = " + `vNode);
//DEBUG System.out.println("ciList= " + `ciList);

BQTermList BQTList = nkt.varList;
ConstraintInstructionList newCIList =
nkt.inferConstraintInstructionList(
 (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getAstConstraintInstructionList() );
nkt.varList = BQTList;
return 
 tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make( (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getVNode() , newCIList,  (( tom.engine.adt.tomsignature.types.TomVisit )((Object)tom__arg)).getOptions() ) ;


}
}
}

}

}
return _visit_TomVisit(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)) instanceof tom.engine.adt.tominstruction.types.Instruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.Instruction )(( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg))) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) {

BQTermList BQTList = nkt.varList;
ConstraintInstructionList newCIList =
nkt.inferConstraintInstructionList(
 (( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)).getConstraintInstructionList() );
nkt.varList = BQTList;
return 
 tom.engine.adt.tominstruction.types.instruction.Match.make(newCIList,  (( tom.engine.adt.tominstruction.types.Instruction )((Object)tom__arg)).getOptions() ) ;


}
}
}

}

}
return _visit_Instruction(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.Code  visit_Code( tom.engine.adt.code.types.Code  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.Code) ) {
boolean tomMatch365_5= false ;
 tom.engine.adt.code.types.Code  tomMatch365_3= null ;
 tom.engine.adt.code.types.CodeList  tomMatch365_1= null ;
 tom.engine.adt.code.types.Code  tomMatch365_4= null ;
if ( ((( tom.engine.adt.code.types.Code )((Object)tom__arg)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)tom__arg))) instanceof tom.engine.adt.code.types.code.Tom) ) {
{
tomMatch365_5= true ;
tomMatch365_3=(( tom.engine.adt.code.types.Code )((Object)tom__arg));
tomMatch365_1= tomMatch365_3.getCodeList() ;

}
} else {
if ( ((( tom.engine.adt.code.types.Code )((Object)tom__arg)) instanceof tom.engine.adt.code.types.Code) ) {
if ( ((( tom.engine.adt.code.types.Code )(( tom.engine.adt.code.types.Code )((Object)tom__arg))) instanceof tom.engine.adt.code.types.code.TomInclude) ) {
{
tomMatch365_5= true ;
tomMatch365_4=(( tom.engine.adt.code.types.Code )((Object)tom__arg));
tomMatch365_1= tomMatch365_4.getCodeList() ;

}
}
}
}
}
if (tomMatch365_5) {

nkt.generateDependencies();
//DEBUG System.out.println("Dependencies: " + nkt.dependencies);
//DEBUG System.out.println("Code with term = " + `code + " and contextType = " +
//DEBUG     contextType);
CodeList newCList = nkt.inferCodeList(
tomMatch365_1);
return 
(( tom.engine.adt.code.types.Code )((Object)tom__arg)).setCodeList(newCList);


}

}

}

}
return _visit_Code(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_inferTypes( tom.engine.adt.tomtype.types.TomType  t0,  NewKernelTyper  t1) { 
return new inferTypes(t0,t1);
}


/**
* The method <code>checkNonLinearityOfVariables</code> searches for variables
* occurring more than once in a condition.
* <p>
* For each variable of type
* "TomTerm" that already exists in <code>varPatternList</code> or in
* <code>varList</code>, a type constraint is added to
* <code>equationConstraints</code> to ensure that  both
* variables have same type (this happens in case of non-linearity).
* <p>
* OBS.: we also need to check the <code>varList</code> since a Variable/VariableStar can have
* occurred in a previous condition as a BQVariable/BQVariableStar, in the
* case of a composed condition
* e.g. (x < 10 ) && f(x) << e -> { action }
* @param var the variable to have the linearity checked
*/
private void checkNonLinearityOfVariables(TomTerm var) {

{
{
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch366_41= false ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch366_3= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch366_5= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch366_4= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_8= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_7= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch366_41= true ;
tomMatch366_7=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));
tomMatch366_3= tomMatch366_7.getOptions() ;
tomMatch366_4= tomMatch366_7.getAstName() ;
tomMatch366_5= tomMatch366_7.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch366_41= true ;
tomMatch366_8=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));
tomMatch366_3= tomMatch366_8.getOptions() ;
tomMatch366_4= tomMatch366_8.getAstName() ;
tomMatch366_5= tomMatch366_8.getAstType() ;

}
}
}
}
}
if (tomMatch366_41) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch366_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch366_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch366_5;
if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch366__end__12=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
do {
{
if (!( tomMatch366__end__12.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_23= tomMatch366__end__12.getHeadconcTomTerm() ;
boolean tomMatch366_38= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch366_21= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_25= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_24= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch366_22= null ;
if ( (tomMatch366_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch366_23) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch366_38= true ;
tomMatch366_24=tomMatch366_23;
tomMatch366_21= tomMatch366_24.getAstName() ;
tomMatch366_22= tomMatch366_24.getAstType() ;

}
} else {
if ( (tomMatch366_23 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch366_23) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch366_38= true ;
tomMatch366_25=tomMatch366_23;
tomMatch366_21= tomMatch366_25.getAstName() ;
tomMatch366_22= tomMatch366_25.getAstType() ;

}
}
}
}
}
if (tomMatch366_38) {
if ( (tom_aName==tomMatch366_21) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch366_22;
boolean tomMatch366_37= false ;
if ( (tom_aType1==tomMatch366_22) ) {
if ( (tom_aType2==tomMatch366_22) ) {
tomMatch366_37= true ;
}
}
if (!(tomMatch366_37)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints = addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints);


}

}
}

}
if ( tomMatch366__end__12.isEmptyconcTomTerm() ) {
tomMatch366__end__12=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
} else {
tomMatch366__end__12= tomMatch366__end__12.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch366__end__12==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));
}
}
if ( (((Object)varList) instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch366__end__18=(( tom.engine.adt.code.types.BQTermList )((Object)varList));
do {
{
if (!( tomMatch366__end__18.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch366_28= tomMatch366__end__18.getHeadconcBQTerm() ;
boolean tomMatch366_40= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch366_26= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch366_27= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch366_30= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch366_29= null ;
if ( (tomMatch366_28 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch366_28) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch366_40= true ;
tomMatch366_29=tomMatch366_28;
tomMatch366_26= tomMatch366_29.getAstName() ;
tomMatch366_27= tomMatch366_29.getAstType() ;

}
} else {
if ( (tomMatch366_28 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch366_28) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch366_40= true ;
tomMatch366_30=tomMatch366_28;
tomMatch366_26= tomMatch366_30.getAstName() ;
tomMatch366_27= tomMatch366_30.getAstType() ;

}
}
}
}
}
if (tomMatch366_40) {
if ( (tom_aName==tomMatch366_26) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch366_27;
boolean tomMatch366_39= false ;
if ( (tom_aType1==tomMatch366_27) ) {
if ( (tom_aType2==tomMatch366_27) ) {
tomMatch366_39= true ;
}
}
if (!(tomMatch366_39)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints = addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints);


}

}
}

}
if ( tomMatch366__end__18.isEmptyconcBQTerm() ) {
tomMatch366__end__18=(( tom.engine.adt.code.types.BQTermList )((Object)varList));
} else {
tomMatch366__end__18= tomMatch366__end__18.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch366__end__18==(( tom.engine.adt.code.types.BQTermList )((Object)varList))) ));
}
}

}

}

}
{
if ( (((Object)var) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch366_63= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch366_45= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_47= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch366_44= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_48= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch366_63= true ;
tomMatch366_47=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));
tomMatch366_44= tomMatch366_47.getAstName() ;
tomMatch366_45= tomMatch366_47.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)var)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)var))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch366_63= true ;
tomMatch366_48=(( tom.engine.adt.tomterm.types.TomTerm )((Object)var));
tomMatch366_44= tomMatch366_48.getAstName() ;
tomMatch366_45= tomMatch366_48.getAstType() ;

}
}
}
}
}
if (tomMatch366_63) {
 tom.engine.adt.tomtype.types.TomType  tom_aType=tomMatch366_45;
if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch366__end__52=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
do {
{
if (!( tomMatch366__end__52.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_57= tomMatch366__end__52.getHeadconcTomTerm() ;
boolean tomMatch366_62= false ;
 tom.engine.adt.tomname.types.TomName  tomMatch366_55= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch366_56= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_58= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch366_59= null ;
if ( (tomMatch366_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch366_57) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch366_62= true ;
tomMatch366_58=tomMatch366_57;
tomMatch366_55= tomMatch366_58.getAstName() ;
tomMatch366_56= tomMatch366_58.getAstType() ;

}
} else {
if ( (tomMatch366_57 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch366_57) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch366_62= true ;
tomMatch366_59=tomMatch366_57;
tomMatch366_55= tomMatch366_59.getAstName() ;
tomMatch366_56= tomMatch366_59.getAstType() ;

}
}
}
}
}
if (tomMatch366_62) {
if ( (tomMatch366_44==tomMatch366_55) ) {
if ( (tom_aType==tomMatch366_56) ) {

//DEBUG System.out.println("Add type '" + `aType + "' of var '" + `var +"'\n");

addPositiveTVar(tom_aType);


}
}
}

}
if ( tomMatch366__end__52.isEmptyconcTomTerm() ) {
tomMatch366__end__52=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
} else {
tomMatch366__end__52= tomMatch366__end__52.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch366__end__52==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));
}
}
}

}

}


}

}

/**
* The method <code>checkNonLinearityOfBQVariables</code> searches for variables
* occurring more than once in a condition.
* <p>
* For each variable of type
* "BQTerm" that already exists in <code>varPatternList</code> or in
* <code>varList</code>, a type constraint is added to
* <code>equationConstraints</code> to ensure that  both
* variables have same type (this happens in case of non-linearity).
* <p>
* OBS.: we also need to check the <code>varPatternList</code> since a
* BQVariable/BQVariableStar can have occurred in a previous condition as a
* Variable/VariableStar, in the case of a composed condition or of a inner match
* e.g. f(x) << e && (x < 10 ) -> { action } 
* @param bqvar the variable to have the linearity checked
*/
private void checkNonLinearityOfBQVariables(BQTerm bqvar) {

{
{
if ( (((Object)bqvar) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch367_41= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch367_5= null ;
 tom.engine.adt.tomoption.types.OptionList  tomMatch367_3= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch367_8= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch367_4= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch367_7= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqvar)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqvar))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch367_41= true ;
tomMatch367_7=(( tom.engine.adt.code.types.BQTerm )((Object)bqvar));
tomMatch367_3= tomMatch367_7.getOptions() ;
tomMatch367_4= tomMatch367_7.getAstName() ;
tomMatch367_5= tomMatch367_7.getAstType() ;

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)bqvar)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)bqvar))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch367_41= true ;
tomMatch367_8=(( tom.engine.adt.code.types.BQTerm )((Object)bqvar));
tomMatch367_3= tomMatch367_8.getOptions() ;
tomMatch367_4= tomMatch367_8.getAstName() ;
tomMatch367_5= tomMatch367_8.getAstType() ;

}
}
}
}
}
if (tomMatch367_41) {
 tom.engine.adt.tomoption.types.OptionList  tom_optionList=tomMatch367_3;
 tom.engine.adt.tomname.types.TomName  tom_aName=tomMatch367_4;
 tom.engine.adt.tomtype.types.TomType  tom_aType1=tomMatch367_5;
if ( (((Object)varList) instanceof tom.engine.adt.code.types.BQTermList) ) {
if ( (((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm) || ((( tom.engine.adt.code.types.BQTermList )(( tom.engine.adt.code.types.BQTermList )((Object)varList))) instanceof tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm)) ) {
 tom.engine.adt.code.types.BQTermList  tomMatch367__end__12=(( tom.engine.adt.code.types.BQTermList )((Object)varList));
do {
{
if (!( tomMatch367__end__12.isEmptyconcBQTerm() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch367_23= tomMatch367__end__12.getHeadconcBQTerm() ;
boolean tomMatch367_38= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch367_24= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch367_21= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch367_22= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch367_25= null ;
if ( (tomMatch367_23 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch367_23) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch367_38= true ;
tomMatch367_24=tomMatch367_23;
tomMatch367_21= tomMatch367_24.getAstName() ;
tomMatch367_22= tomMatch367_24.getAstType() ;

}
} else {
if ( (tomMatch367_23 instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )tomMatch367_23) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch367_38= true ;
tomMatch367_25=tomMatch367_23;
tomMatch367_21= tomMatch367_25.getAstName() ;
tomMatch367_22= tomMatch367_25.getAstType() ;

}
}
}
}
}
if (tomMatch367_38) {
if ( (tom_aName==tomMatch367_21) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch367_22;
boolean tomMatch367_37= false ;
if ( (tom_aType1==tomMatch367_22) ) {
if ( (tom_aType2==tomMatch367_22) ) {
tomMatch367_37= true ;
}
}
if (!(tomMatch367_37)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints); 

}

}
}

}
if ( tomMatch367__end__12.isEmptyconcBQTerm() ) {
tomMatch367__end__12=(( tom.engine.adt.code.types.BQTermList )((Object)varList));
} else {
tomMatch367__end__12= tomMatch367__end__12.getTailconcBQTerm() ;
}

}
} while(!( (tomMatch367__end__12==(( tom.engine.adt.code.types.BQTermList )((Object)varList))) ));
}
}
if ( (((Object)varPatternList) instanceof tom.engine.adt.tomterm.types.TomList) ) {
if ( (((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) {
 tom.engine.adt.tomterm.types.TomList  tomMatch367__end__18=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
do {
{
if (!( tomMatch367__end__18.isEmptyconcTomTerm() )) {
 tom.engine.adt.tomterm.types.TomTerm  tomMatch367_28= tomMatch367__end__18.getHeadconcTomTerm() ;
boolean tomMatch367_40= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch367_29= null ;
 tom.engine.adt.tomname.types.TomName  tomMatch367_26= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch367_27= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch367_30= null ;
if ( (tomMatch367_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch367_28) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch367_40= true ;
tomMatch367_29=tomMatch367_28;
tomMatch367_26= tomMatch367_29.getAstName() ;
tomMatch367_27= tomMatch367_29.getAstType() ;

}
} else {
if ( (tomMatch367_28 instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )tomMatch367_28) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch367_40= true ;
tomMatch367_30=tomMatch367_28;
tomMatch367_26= tomMatch367_30.getAstName() ;
tomMatch367_27= tomMatch367_30.getAstType() ;

}
}
}
}
}
if (tomMatch367_40) {
if ( (tom_aName==tomMatch367_26) ) {
 tom.engine.adt.tomtype.types.TomType  tom_aType2=tomMatch367_27;
boolean tomMatch367_39= false ;
if ( (tom_aType1==tomMatch367_27) ) {
if ( (tom_aType2==tomMatch367_27) ) {
tomMatch367_39= true ;
}
}
if (!(tomMatch367_39)) {

TypeConstraintList newEqConstraints = equationConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_aType1, tom_aType2,  tom.engine.adt.typeconstraints.types.info.PairNameOptions.make(tom_aName, tom_optionList) ) ,newEqConstraints); 

}

}
}

}
if ( tomMatch367__end__18.isEmptyconcTomTerm() ) {
tomMatch367__end__18=(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList));
} else {
tomMatch367__end__18= tomMatch367__end__18.getTailconcTomTerm() ;
}

}
} while(!( (tomMatch367__end__18==(( tom.engine.adt.tomterm.types.TomList )((Object)varPatternList))) ));
}
}

}

}

}

}

}

/**
* The method <code>inferCodeList</code> applies rule CT-BLOCK. It starts
* inference process which takes one code at a time
* <ul>
*  <li> all lists and hashMaps are reset
*  <li> each code is typed with fresh type variables
*  <li> each code is traversed in order to generate type constraints
*  <li> the type constraints of "equationConstraints" and
*        "subtypeConstraints" lists are solved at the end
*        of the current code generating a mapping (a set of
*        substitutions for each type variable)
*  <li> the mapping is applied over the code and the symbol table
* </ul>
* <p>
* CT-BLOCK rule:
* IF found "(rule_1,...,rule_n)" where "rule_i" are ConstraintInstructions
* THEN infers types of each ConstraintInstruction
* @param cList the tom code list to be type inferred
* @return      the tom typed code list
*/
private CodeList inferCodeList(CodeList cList) {
CodeList newCList = 
 tom.engine.adt.code.types.codelist.EmptyconcCode.make() ;
for (Code code : cList.getCollectionconcCode()) {
init();
code =  collectKnownTypesFromCode(
code);
//DEBUG System.out.println("------------- Code typed with typeVar:\n code = " +
//DEBUG    `code);
code = inferAllTypes(code,
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
//DEBUG printGeneratedConstraints(subtypeConstraints);
solveConstraints();
//DEBUG System.out.println("substitutions = " + substitutions);
code = replaceInCode(code);
//DEBUG System.out.println("------------- Code typed with substitutions:\n code = " +
//DEBUG `code);
replaceInSymbolTable();
newCList = 
 tom.engine.adt.code.types.codelist.ConsconcCode.make(code,tom_append_list_concCode(newCList, tom.engine.adt.code.types.codelist.EmptyconcCode.make() )) ;
}
return newCList.reverse();
}

/**
* The method <code>inferConstraintInstructionList</code> applies rule CT-RULE
* to a pair "condition -> action" in order to collect all variables occurring
* in the condition and put them into <code>varPatternList</code> (for those
* variables occurring in match constraints) and <code>varList</code> (for
* those variables occurring in numeric constraints) to be able to handle
* non-linearity.
* <p>
* The condition (left-hand side) is traversed and then the action (right-hand
* side) is traversed in order to generate type constraints.
* <p>
* CT-RULE rule:
* IF found "cond --> action)" where "action" is a list of terms or a code to
*    be type inferred
* THEN infers types of condition (by calling <code>inferConstraint</code>
*      method) and action (by calling <code>inferAllTypes</code>) 
* @param ciList  the list of pairs "condition -> action" to be type inferred 
* @return        the typed list resulting
*/
private ConstraintInstructionList inferConstraintInstructionList(ConstraintInstructionList ciList) {
ConstraintInstructionList newCIList = 
 tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ;
for (ConstraintInstruction cInst :
ciList.getCollectionconcConstraintInstruction()) {
try {

{
{
if ( (((Object)cInst) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)) instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {
if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )(( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst))) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {
 tom.engine.adt.tomconstraint.types.Constraint  tom_constraint= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getConstraint() ;

// Store variable lists in new variables and reinitialize them
BQTermList globalVarList = varList;
TomList globalVarPatternList = varPatternList;


tom_make_TopDownCollect(tom_make_CollectVars(this)).visitLight(
tom_constraint);

Constraint newConstraint = inferConstraint(
tom_constraint);
//DEBUG System.out.println("inferConstraintInstructionList: action " +
//DEBUG     `action);
Instruction newAction = 
inferAllTypes( (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getAction() , tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );

varPatternList = globalVarPatternList;
resetVarList(globalVarPatternList);
newCIList =

 tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )((Object)cInst)).getOptions() ) ,tom_append_list_concConstraintInstruction(newCIList, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;


}
}
}

}

}

} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("inferConstraintInstructionList: failure on " + 
cInst);
}
}
return newCIList.reverse();
}

/**
* The class <code>CollectVars</code> is generated from a strategy which
* collect all variables (Variable, VariableStar, BQVariable, BQVariableStar
* occurring in a condition.
* @param nkt an instance of object NewKernelTyper
*/

public static class CollectVars extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public CollectVars( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.code.types.BQTerm) ) {
return ((T)visit_BQTerm((( tom.engine.adt.code.types.BQTerm )v),introspector));
}
if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  _visit_BQTerm( tom.engine.adt.code.types.BQTerm  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.code.types.BQTerm )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.code.types.BQTerm  visit_BQTerm( tom.engine.adt.code.types.BQTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch369_4= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch369_3= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch369_2= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch369_4= true ;
tomMatch369_2=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {
{
tomMatch369_4= true ;
tomMatch369_3=(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg));

}
}
}
}
}
if (tomMatch369_4) {

nkt.addBQTerm(
(( tom.engine.adt.code.types.BQTerm )((Object)tom__arg)));
//localVList = `concBQTerm(bqvar,localVList*);


}

}

}

}
return _visit_BQTerm(tom__arg,introspector);

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch370_4= false ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch370_2= null ;
 tom.engine.adt.tomterm.types.TomTerm  tomMatch370_3= null ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {
{
tomMatch370_4= true ;
tomMatch370_2=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));

}
} else {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
{
tomMatch370_4= true ;
tomMatch370_3=(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg));

}
}
}
}
}
if (tomMatch370_4) {

//DEBUG System.out.println("CollectVars : var = " + `var);
nkt.addTomTerm(
(( tom.engine.adt.tomterm.types.TomTerm )((Object)tom__arg)));
//localVPList = `concTomTerm(var,localVPList*);


}

}

}

}
return _visit_TomTerm(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_CollectVars( NewKernelTyper  t0) { 
return new CollectVars(t0);
}


/**
* The method <code>inferConstraint</code> applies rule CT-MATCH, CT-NUM, CT-CONJ,
* or CT-DISJ to a "condition" in order to infer the types of its variables.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* CT-MATCH rule:
* IF found "e1 << [A] e2" 
* THEN infers type of e1 and e2 and add the type constraints "T1 = A" and "A
* <: T2"
* <p>
* CT-NUM rule:
* IF found "e1 == e2", "e1 <= e2", "e1 < e2", "e1 >= e2" or "e1 > e2"
* THEN infers type of e1 and e2 and add the type constraints "A <:T1" and "A
* <: T2", where "A" is a fresh type variable   
* <p>
* CT-CONJ rule (resp. CT-DISJ):
* IF found "cond1 && cond2" (resp. "cond1 || cond2")
* THEN infers type of cond1 and cond2 (by calling
* <code>inferConstraint</code> for each condition)
* @param constraint  the "condition" to be type inferred 
* @return            the typed condition resulting
*/
private Constraint inferConstraint(Constraint constraint) {

{
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {
 tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getPattern() ;
 tom.engine.adt.code.types.BQTerm  tom_subject= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getSubject() ;
 tom.engine.adt.tomtype.types.TomType  tom_aType= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getAstType() ;

//DEBUG System.out.println("inferConstraint l1 -- subject = " + `subject);
TomType tPattern = getType(
tom_pattern);
TomType tSubject = getType(
tom_subject);
if (tPattern == null || tPattern == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tPattern = getUnknownFreshTypeVar();
}
if (tSubject == null || tSubject == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tSubject = getUnknownFreshTypeVar();
}
//DEBUG System.out.println("inferConstraint: match -- constraint " +
//DEBUG     tPattern + " = " + tSubject);

{
{
if ( (((Object)tom_aType) instanceof tom.engine.adt.tomtype.types.TomType) ) {
boolean tomMatch372_4= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch372_2= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch372_3= null ;
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
{
tomMatch372_4= true ;
tomMatch372_2=(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType));

}
} else {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
{
tomMatch372_4= true ;
tomMatch372_3=(( tom.engine.adt.tomtype.types.TomType )((Object)tom_aType));

}
}
}
}
}
if (tomMatch372_4) {

/* T_pattern = T_cast and T_cast <: T_subject */
TypeConstraintList newEqConstraints = equationConstraints;
TypeConstraintList newSubConstraints = subtypeConstraints;
equationConstraints =
addEqConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tPattern, tom_aType, getInfoFromTomTerm(tom_pattern)) ,newEqConstraints);
subtypeConstraints = addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_aType, tSubject, getInfoFromBQTerm(tom_subject)) ,newSubConstraints);


}

}

}

}

TomTerm newPattern = 
inferAllTypes(tom_pattern,tPattern);
BQTerm newSubject = 
inferAllTypes(tom_subject,tSubject);
return 
 tom.engine.adt.tomconstraint.types.constraint.MatchConstraint.make(newPattern, newSubject, tom_aType) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {
 tom.engine.adt.code.types.BQTerm  tom_left= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getLeft() ;
 tom.engine.adt.code.types.BQTerm  tom_right= (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getRight() ;

TomType tLeft = getType(
tom_left);
TomType tRight = getType(
tom_right);
if (tLeft == null || tLeft == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tLeft = getUnknownFreshTypeVar();
}
if (tRight == null || tRight == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
tRight = getUnknownFreshTypeVar();
}
//DEBUG System.out.println("inferConstraint: match -- constraint " +
//DEBUG     tLeft + " = " + tRight);

// To represent the relationshipo between both argument types
TomType lowerType = getUnknownFreshTypeVar();
TypeConstraintList newSubConstraints = subtypeConstraints;
newSubConstraints =
addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tLeft, getInfoFromBQTerm(tom_left)) ,newSubConstraints);
subtypeConstraints =
addSubConstraint(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(lowerType, tRight, getInfoFromBQTerm(tom_right)) ,newSubConstraints);
BQTerm newLeft = inferAllTypes(
tom_left,tLeft);
BQTerm newRight = inferAllTypes(
tom_right,tRight);
return 
 tom.engine.adt.tomconstraint.types.constraint.NumericConstraint.make(newLeft, newRight,  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getType() ) ;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyAndConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )  ) )) {

ConstraintList cList = 
 tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadAndConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailAndConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
Constraint newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ;
for (Constraint cArg : cList.getCollectionconcConstraint()) {
cArg = inferConstraint(cArg);
newAConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(newAConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsAndConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyAndConstraint.make() ) ) ;
}
return newAConstraint;


}
}
}

}
{
if ( (((Object)constraint) instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {
if ( (((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )(( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) ) {
if (!( (  (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).isEmptyOrConstraint()  ||  ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint))== tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )  ) )) {

ConstraintList cList = 
 tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getHeadOrConstraint() ):((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)))), tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make((( (((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint) || ((( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)) instanceof tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint)) )?( (( tom.engine.adt.tomconstraint.types.Constraint )((Object)constraint)).getTailOrConstraint() ):( tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() )), tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ) ;
Constraint newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ;
for (Constraint cArg : cList.getCollectionconcConstraint()) {
cArg = inferConstraint(cArg);
newOConstraint = 
 tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(newOConstraint, tom.engine.adt.tomconstraint.types.constraint.ConsOrConstraint.make(cArg, tom.engine.adt.tomconstraint.types.constraint.EmptyOrConstraint.make() ) ) ;
}
return newOConstraint;


}
}
}

}


}

return constraint;
}

/**
* The method <code>inferSlotList</code> infers types of the arguments of
* lists and functions (which are TomTerms) 
* <p> 
* It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
* to each argument in order to infer the types of its variables.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* Continuation of CT-STAR rule (applying to premises):
* IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "x", where "x" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-ELEM rule (applying to premises which are
* not lists):
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
* <p>
* Continuation of CT-MERGE rule (applying to premises which are lists with
* the same operator):
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-FUN rule (applying to premises):
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and add a type constraint "T <:A"
* <p>
* @param sList       a list of arguments of a list/function
* @param tSymbol     the TomSymbol related to the list/function
* @param contextType the codomain of the list/function 
* @return            the typed list of arguments resulting
*/
private SlotList inferSlotList(SlotList sList, TomSymbol tSymbol, TomType
contextType) {
TomType argType = contextType;
SlotList newSList = 
 tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;

{
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

TomName argName;
TomTerm argTerm;
TomSymbol argSymb;
for (Slot slot : sList.getCollectionconcSlot()) {
argName = slot.getSlotName();
argTerm = slot.getAppl();
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch374_3= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch374_3= true ;
}
}
if (!(tomMatch374_3)) {

//DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
argType = getUnknownFreshTypeVar();
//DEBUG System.out.println("InferSlotList getUnknownFreshTypeVar = " +
//DEBUG     `argType);

}

}

}

}

}
argTerm = 
inferAllTypes(argTerm,argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
}
return newSList.reverse(); 


}
}
}

}
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch373_5= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;
if ( (tomMatch373_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch373_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch373_8= tomMatch373_5.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch373_9= tomMatch373_5.getCodomain() ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch373_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch373_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch373_8.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch373_8.getHeadconcTomType() ;
if ( (tomMatch373_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch373_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch373_12= tomMatch373_9.getTypeOptions() ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch373_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch373_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch373__end__21=tomMatch373_12;
do {
{
if (!( tomMatch373__end__21.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch373_24= tomMatch373__end__21.getHeadconcTypeOption() ;
if ( (tomMatch373_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch373_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {

TomTerm argTerm;
TomSymbol argSymb;
for (Slot slot : sList.getCollectionconcSlot()) {
argTerm = slot.getAppl();
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

/* Case CT-STAR rule (applying to premises) */
argType = 
tomMatch373_9;


}
}
}

}
{
if ( (((Object)argTerm) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
boolean tomMatch375_6= false ;
if ( ((( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm)) instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
if ( ((( tom.engine.adt.tomterm.types.TomTerm )(( tom.engine.adt.tomterm.types.TomTerm )((Object)argTerm))) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {
tomMatch375_6= true ;
}
}
if (!(tomMatch375_6)) {

//DEBUG System.out.println("InferSlotList CT-ELEM -- tTerm = " + `tTerm);
/* Case CT-ELEM rule (applying to premises which are not lists) */
argType = 
tom_headTTList;


}

}

}


}

} else if (
 (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() != argSymb.getAstName()) {
/*
* Case CT-ELEM rule where premise is a list
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) | a() and B = ListB(A*)
* ListB(ListA(a()))
*/
argType = 
tom_headTTList;
} 

/* Case CT-MERGE rule (applying to premises) */
argTerm = 
inferAllTypes(argTerm,argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(slot.getSlotName(), argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
}
return newSList.reverse(); 


}
}
}
if ( tomMatch373__end__21.isEmptyconcTypeOption() ) {
tomMatch373__end__21=tomMatch373_12;
} else {
tomMatch373__end__21= tomMatch373__end__21.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch373__end__21==tomMatch373_12) ));
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch373_27= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;
if ( (tomMatch373_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch373_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch373_30= tomMatch373_27.getCodomain() ;
if ( (tomMatch373_30 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch373_30) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch373_33= tomMatch373_30.getTypeOptions() ;
boolean tomMatch373_44= false ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch373_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch373_33) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch373__end__39=tomMatch373_33;
do {
{
if (!( tomMatch373__end__39.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch373_42= tomMatch373__end__39.getHeadconcTypeOption() ;
if ( (tomMatch373_42 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch373_42) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch373_44= true ;
}
}
}
if ( tomMatch373__end__39.isEmptyconcTypeOption() ) {
tomMatch373__end__39=tomMatch373_33;
} else {
tomMatch373__end__39= tomMatch373__end__39.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch373__end__39==tomMatch373_33) ));
}
if (!(tomMatch373_44)) {

TomTerm argTerm;
TomName argName;
for (Slot slot : sList.getCollectionconcSlot()) {
argName = slot.getSlotName();
argType = TomBase.getSlotType(tSymbol,argName);
argTerm = 
inferAllTypes(slot.getAppl(),argType);
newSList = 
 tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(argName, argTerm) ,tom_append_list_concSlot(newSList, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
//DEBUG System.out.println("InferSlotList CT-FUN -- end of for with slotappl = " + `argTerm);
}
return newSList.reverse(); 


}

}
}
}
}
}
}
}

}


}

throw new TomRuntimeException("inferSlotList: failure on " + 
sList);
}

/**
* The method <code>inferBQTermList</code> infers types of the arguments of
* lists, functions and calls of methods (which are BQTerms) 
* <p> 
* It continues the application of rules CT-FUN, CT-ELEM, CT-MERGE or CT-STAR
* to each argument in order to infer the types of its variables.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* Continuation of CT-STAR rule (applying to premises):
* IF found "l(e1,...,en,x*):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "x", where "x" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-ELEM rule (applying to premises which are
* not lists):
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" does not represent a list with head symbol "l"
* <p>
* Continuation of CT-MERGE rule (applying to premises which are lists with
* the same operator):
* IF found "l(e1,...,en,e):A" and "l:T1*->T" exists in SymbolTable
* THEN infers type of both sublist "l(e1,...,en)" and last argument
*      "e" and adds a type constraint "T <: A", where "e" represents a list with
*      head symbol "l"
* <p>
* Continuation of CT-FUN rule (applying to premises):
* IF found "f(e1,...,en):A" and "f:T1,...,Tn->T" exists in SymbolTable
* THEN infers type of arguments and add a type constraint "T <:A"
* <p>
* @param bqList      a list of arguments of a list/function/method
* @param tSymbol     the TomSymbol related to the list/function
* @param contextType the codomain of the list/function 
* @return            the typed list of arguments resulting
*/
private BQTermList inferBQTermList(BQTermList bqTList, TomSymbol tSymbol, TomType
contextType) {
TomType argType = contextType;
BQTermList newBQTList = 
 tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() ;

{
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.EmptySymbol) ) {

for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argTerm = 
inferAllTypes(argTerm, tom.engine.adt.tomtype.types.tomtype.EmptyType.make() );
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}
return newBQTList.reverse(); 


}
}
}

}
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch376_5= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;
if ( (tomMatch376_5 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_5) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch376_8= tomMatch376_5.getDomain() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch376_9= tomMatch376_5.getCodomain() ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch376_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )tomMatch376_8) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if (!( tomMatch376_8.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_headTTList= tomMatch376_8.getHeadconcTomType() ;
if ( (tomMatch376_9 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_9) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376_12= tomMatch376_9.getTypeOptions() ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch376_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch376_12) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__21=tomMatch376_12;
do {
{
if (!( tomMatch376__end__21.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch376_24= tomMatch376__end__21.getHeadconcTypeOption() ;
if ( (tomMatch376_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {

TomSymbol argSymb;
for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argSymb = getSymbolFromTerm(argTerm);
if(!(TomBase.isListOperator(
argSymb) || TomBase.isArrayOperator(
argSymb))) {

{
{
if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {

/*
* We don't know what is into the Composite
* It can be a BQVariableStar or a list operator or a list of
* CompositeBQTerm or something else
*/
argType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;


}
}

}
{
if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariableStar) ) {

/* Case CT-STAR rule (applying to premises) */
argType = 
tomMatch376_9;


}
}
}

}
{
if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
boolean tomMatch377_10= false ;
 tom.engine.adt.code.types.BQTerm  tomMatch377_9= null ;
 tom.engine.adt.code.types.BQTerm  tomMatch377_8= null ;
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQVariable) ) {
{
tomMatch377_10= true ;
tomMatch377_8=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));

}
} else {
if ( ((( tom.engine.adt.code.types.BQTerm )((Object)argTerm)) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.BQAppl) ) {
{
tomMatch377_10= true ;
tomMatch377_9=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));

}
}
}
}
}
if (tomMatch377_10) {

argType = 
tom_headTTList;


}

}

}


}

} else if (
 (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() != argSymb.getAstName()) {
/*
* Case CT-ELEM rule which premise is a list
* A list with a sublist whose constructor is different
* e.g. 
* A = ListA(A*) and B = ListB(A*) | b()
* ListB(ListA(a()))
*/
argType = 
tom_headTTList;
}

/* Case CT-MERGE rule (applying to premises) */
argTerm = 
inferAllTypes(argTerm,argType);
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
}
return newBQTList.reverse(); 


}
}
}
if ( tomMatch376__end__21.isEmptyconcTypeOption() ) {
tomMatch376__end__21=tomMatch376_12;
} else {
tomMatch376__end__21= tomMatch376__end__21.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch376__end__21==tomMatch376_12) ));
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)tSymbol) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)) instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {
if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )(( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol))) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch376_28= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getTypesToType() ;
if ( (tomMatch376_28 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_28) instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch376_34= tomMatch376_28.getCodomain() ;
if ( (tomMatch376_34 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch376_34) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376_37= tomMatch376_34.getTypeOptions() ;
 tom.engine.adt.tomslot.types.PairNameDeclList  tom_pNDList= (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getPairNameDeclList() ;
boolean tomMatch376_48= false ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch376_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch376_37) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch376__end__43=tomMatch376_37;
do {
{
if (!( tomMatch376__end__43.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch376_46= tomMatch376__end__43.getHeadconcTypeOption() ;
if ( (tomMatch376_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch376_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch376_48= true ;
}
}
}
if ( tomMatch376__end__43.isEmptyconcTypeOption() ) {
tomMatch376__end__43=tomMatch376_37;
} else {
tomMatch376__end__43= tomMatch376__end__43.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch376__end__43==tomMatch376_37) ));
}
if (!(tomMatch376_48)) {

TomTypeList symDomain = 
 tomMatch376_28.getDomain() ;
if(
tom_pNDList.length() != bqTList.length()) {
Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getOptions() );

{
{
if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
TomMessage.symbolNumberArgument,
 (( tom.engine.adt.tomsignature.types.TomSymbol )((Object)tSymbol)).getAstName() .getString(),
tom_pNDList.length(),bqTList.length());


}
}
}

}

}

} else {
for (BQTerm argTerm : bqTList.getCollectionconcBQTerm()) {
argType = symDomain.getHeadconcTomType();

{
{
if ( (((Object)argTerm) instanceof tom.engine.adt.code.types.BQTerm) ) {
if ( (((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.ConsComposite) || ((( tom.engine.adt.code.types.BQTerm )(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) instanceof tom.engine.adt.code.types.bqterm.EmptyComposite)) ) {
 tom.engine.adt.code.types.BQTerm  tomMatch379__end__4=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));
do {
{
if (!( tomMatch379__end__4.isEmptyComposite() )) {
 tom.engine.adt.code.types.BQTerm  tomMatch379_5= tomMatch379__end__4.getTailComposite() ;
 tom.engine.adt.code.types.BQTerm  tomMatch379__end__8=tomMatch379_5;
do {
{
if (!( tomMatch379__end__8.isEmptyComposite() )) {
argType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ; 

}
if ( tomMatch379__end__8.isEmptyComposite() ) {
tomMatch379__end__8=tomMatch379_5;
} else {
tomMatch379__end__8= tomMatch379__end__8.getTailComposite() ;
}

}
} while(!( (tomMatch379__end__8==tomMatch379_5) ));
}
if ( tomMatch379__end__4.isEmptyComposite() ) {
tomMatch379__end__4=(( tom.engine.adt.code.types.BQTerm )((Object)argTerm));
} else {
tomMatch379__end__4= tomMatch379__end__4.getTailComposite() ;
}

}
} while(!( (tomMatch379__end__4==(( tom.engine.adt.code.types.BQTerm )((Object)argTerm))) ));
}
}

}

}

argTerm = 
inferAllTypes(argTerm,argType);
newBQTList = 
 tom.engine.adt.code.types.bqtermlist.ConsconcBQTerm.make(argTerm,tom_append_list_concBQTerm(newBQTList, tom.engine.adt.code.types.bqtermlist.EmptyconcBQTerm.make() )) ;
symDomain = symDomain.getTailconcTomType();
//DEBUG System.out.println("InferBQTermList CT-FUN -- end of for with bqappl = " + `argTerm);
}
}
return newBQTList.reverse(); 


}

}
}
}
}
}
}
}

}


}

throw new TomRuntimeException("inferBQTermList: failure on " + 
bqTList);
}

/**
* The method <code>solveConstraints</code> calls
* <code>solveEquationConstraints</code> to solve the list of equation
* constraints and generate a set os substitutions for type variables. Then
* the substitutions are applied to the list of subtype constraints and the
* method <code>solveEquationConstraints</code> to solve this list. 
*/
private void solveConstraints() {
//DEBUG System.out.println("\nsolveConstraints 1:");
//DEBUG printGeneratedConstraints(equationConstraints);
//DEBUG printGeneratedConstraints(subtypeConstraints);
boolean errorFound = solveEquationConstraints(equationConstraints);
if (!errorFound) {

{
{
if ( (((Object)subtypeConstraints) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch380_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)subtypeConstraints)).isEmptyconcTypeConstraint() ) {
tomMatch380_2= true ;
}
}
if (!(tomMatch380_2)) {

TypeConstraintList simplifiedConstraints =
replaceInSubtypingConstraints(subtypeConstraints);
replaceInPositiveTVarList();
//DEBUG printGeneratedConstraints(simplifiedConstraints);
simplifiedConstraints = 
solveSubtypingConstraints(simplifiedConstraints);
//DEBUG System.out.println("\nResulting subtype constraints!!");
//DEBUG printGeneratedConstraints(simplifiedConstraints);

}

}

}

}

}
}

/**
* The method <code>solveEquationConstraints</code> tries to solve all
* equations constraints collected during the inference process.
* <p> 
* There exists 3 kinds of types : variable types Ai, ground types Ti and
* ground types Ti^c which are decorated with a given symbol c. Since a type
* constraints is a pair (type1,type2) representing an equation relation
* between two types, them the set of all possibilities of arrangement between
* types is a sequence with repetition. Then, we have 9 possible cases (since
* 3^2 = 9).
* <p>
* CASE 1: tCList = {(T1 = T2),...)} and Map
*  --> detectFail(T1 = T2) to verify if T1 is equals to T2 
* <p>
* CASE 2: tCList = {(T1 = T2^c),...)} and Map
*  --> detectFail(T1 = T2^c) to verify if T1 is equals to T2 
* <p>
* CASE 3: tCList = {(T1^c = T2),...)} and Map
*  --> detectFail(T1^c = T2) to verify if T1 is equals to T2 
* <p>
* CASE 4: tCList = {(T1^a = T2^b),...)} and Map
*  --> detectFail(T1^a = T2^b) to verify if T1^a is equals to T2^b 
* <p>
* CASE 5: tCList = {(T1 = A1),...)} and Map 
*  a) Map(A1) does not exist   --> {(A,T1)} U Map 
*  b) Map(A1) = T2             --> detectFail(T1 = T2)
*  c) Map(A1) = A2             --> {(A2,T1)} U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 6: tCList = {(T1^c = A1),...)} and Map 
*  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
*  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
*  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 7: tCList = {(A1 = T1),...)} and Map 
*  a) Map(A1) does not exist   --> {(A1,T1)} U Map 
*  b) Map(A1) = T2             --> detectFail(T1 = T2)
*  c) Map(A1) = A2             --> {(A2,A1)} U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 8: tCList = {(A1 = T1^c),...)} and Map 
*  a) Map(A1) does not exist   --> {(A1,T1^c)} U Map 
*  b) Map(A1) = T2 (or T2^b)   --> detectFail(T1^c = T2) (or detectFail(T1^c = T2^b))
*  c) Map(A1) = A2             --> {(A2,T1^c)} U Map, since Map is saturated and
*                                  then Map(A2) does not exist 
* <p>
* CASE 9: tCList = {(A1 = A2),...)} and Map
*  a) Map(A1) = T1 (or T1^a) and
*    i)    Map(A2) does not exist    --> {(A2,T1)} U Map (or {(A2,T1^a)} U Map) 
*    ii)   Map(A2) = T2 (or T2^b)    --> detectFail(T1 = T2) (or detectFail(T1^a = T2^b))
*    iii)  Map(A2) = A3              --> {(A3,T1)} U Map (or {(A3,T1^a)} U Map), since Map is saturated and
*                                        then Map(A3) does not exist 
*
*  b) Map(A1) = A3 and
*    i)    Map(A2) does not exist    --> {(A2,A3)} U Map 
*    ii)   Map(A2) = T2 (or T2^b)    --> {(A3,T2)} U Map, since Map is saturated and
*                                        then Map(A3) does not exist 
*    iii)  Map(A2) = A4              --> {(A3,A4)} U Map, since Map is saturated and
*                                        then Map(A3) does not exist 
*  c) Map(A1) does not exist and
*    i)    Map(A2) does not exist    --> {(A1,A2)} U Map
*    ii)   Map(A2) = T1 (or T1^a)    --> {(A1,T1)} U Map (or {(A1,T1^a)} U Map)
*    iii)  Map(A2) = A3              --> {(A1,A3)} U Map 
* @param tCList the equation constraint list to be replaced
* @return       the status of the list, if errors were found or not
*/
private boolean solveEquationConstraints(TypeConstraintList tCList) {
boolean errorFound = false;
for (TypeConstraint tConstraint :
tCList.getCollectionconcTypeConstraint()) {
matchBlockAdd :
{

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch381_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch381_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType1=tomMatch381_1;
 tom.engine.adt.tomtype.types.TomType  tom_groundType2=tomMatch381_2;
boolean tomMatch381_12= false ;
if ( (tomMatch381_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType1==tomMatch381_1) ) {
tomMatch381_12= true ;
}
}
}
if (!(tomMatch381_12)) {
boolean tomMatch381_11= false ;
if ( (tomMatch381_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType2==tomMatch381_2) ) {
tomMatch381_11= true ;
}
}
}
if (!(tomMatch381_11)) {
if (!( (tom_groundType2==tom_groundType1) )) {

//DEBUG System.out.println("In solveEquationConstraints:" + `groundType1 +
//DEBUG     " = " + `groundType2);
errorFound = (errorFound || 
detectFail((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))));
break matchBlockAdd;


}
}

}

}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch381_14= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch381_15= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch381_14;
if ( (tomMatch381_15 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_15) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch381_15;
boolean tomMatch381_24= false ;
if ( (tomMatch381_14 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_14) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch381_14) ) {
tomMatch381_24= true ;
}
}
}
if (!(tomMatch381_24)) {

if (substitutions.containsKey(
tom_typeVar)) {
TomType mapTypeVar = substitutions.get(
tom_typeVar);
if (!isTypeVar(mapTypeVar)) {
errorFound = (errorFound || 

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_groundType, mapTypeVar,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
} else {
// if (isTypeVar(mapTypeVar))
addSubstitution(mapTypeVar,
tom_groundType);
}
} else {
addSubstitution(
tom_typeVar,
tom_groundType);
}
break matchBlockAdd;


}

}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch381_26= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch381_27= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
if ( (tomMatch381_26 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_26) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=tomMatch381_26;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch381_27;
boolean tomMatch381_36= false ;
if ( (tomMatch381_27 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_27) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch381_27) ) {
tomMatch381_36= true ;
}
}
}
if (!(tomMatch381_36)) {

if (substitutions.containsKey(
tom_typeVar)) {
TomType mapTypeVar = substitutions.get(
tom_typeVar);
if (!isTypeVar(mapTypeVar)) {
errorFound = (errorFound || 

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar, tom_groundType,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
} else {
// if (isTypeVar(mapTypeVar))
addSubstitution(mapTypeVar,
tom_groundType);
}
} else {
addSubstitution(
tom_typeVar,
tom_groundType);
}
break matchBlockAdd;


}

}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch381_38= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch381_39= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
if ( (tomMatch381_38 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_38) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar1=tomMatch381_38;
if ( (tomMatch381_39 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch381_39) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar2=tomMatch381_39;
if (!( (tom_typeVar2==tom_typeVar1) )) {

TomType mapTypeVar1;
TomType mapTypeVar2;
if (substitutions.containsKey(
tom_typeVar1) && substitutions.containsKey(
tom_typeVar2)) {
mapTypeVar1 = substitutions.get(
tom_typeVar1);
mapTypeVar2 = substitutions.get(
tom_typeVar2);
if (isTypeVar(mapTypeVar1)) {
addSubstitution(mapTypeVar1,mapTypeVar2);
} else {
if (isTypeVar(mapTypeVar2)) {
addSubstitution(mapTypeVar2,mapTypeVar1);
} else {
errorFound = (errorFound || 

detectFail( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(mapTypeVar1, mapTypeVar2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ));
}
}
break matchBlockAdd;
} else if (substitutions.containsKey(
tom_typeVar1)) {
mapTypeVar1 = substitutions.get(
tom_typeVar1);
addSubstitution(
tom_typeVar2,mapTypeVar1);
break matchBlockAdd;
} else if (substitutions.containsKey(
tom_typeVar2)){
mapTypeVar2 = substitutions.get(
tom_typeVar2);
addSubstitution(
tom_typeVar1,mapTypeVar2);
break matchBlockAdd;
} else {
addSubstitution(
tom_typeVar1,
tom_typeVar2);
break matchBlockAdd;
}


}
}
}
}
}
}
}
}

}


}

}
}
return errorFound;
}

/**
* The method <code>detectFail</code> checks if a type constraint
* relating two ground types has a solution. In the negative case, an
* 'incompatible types' message error is printed.  
* <p> 
* There exists 2 kinds of ground types: ground types Ti and
* ground types Ti^c which are decorated with a given symbol c. Considering
* type constraints as pairs (type1,type2) representing an equation or
* a subtype relation between two ground types, them the set of all possibilities of arrangement between
* ground types is a sequence with repetition. Then, we have 4 possible cases (since
* 2^2 = 4).
* <p>
* CASE 1: tCList = {(T1 = T2),...)} and Map
*  a) --> true if T1 is different from T2
*  b) --> false if T1 is equals to T2
* <p>
* CASE 2: tCList = {(T1 = T2^c),...)} and Map
*  a) --> true if T1 is different from T2
*  b) --> false if T1 is equals to T2
* <p>
* CASE 3: tCList = {(T1^c = T2),...)} and Map
*  a) --> true if T1 is different from T2
*  b) --> false if T1 is equals to T2
* <p>
* CASE 4: tCList = {(T1^a = T2^b),...)} and Map
*  a) --> true if T1 is different from T2 or 'a' is different from 'b'
*  b) --> false if T1 is equals to T2 and 'a' is equals to 'b'
* <p>
* CASE 5: tCList = {(T1 <: T2),...)} and Map
*  a) --> true if T1 is not subtype of T2
*  b) --> false if T1 is subtype of T2
* <p>
* CASE 6: tCList = {(T1 <: T2^c),...)} and Map
*   --> true
* <p>
* CASE 7: tCList = {(T1^c <: T2),...)} and Map
*  a) --> true if T1 is not subtype of T2
*  b) --> false if T1 is subtype of T2
* <p>
* CASE 8: tCList = {(T1^a <: T2^b),...)} and Map
*  a) --> true if T1 is not subtype of T2 or 'a' is different from 'b'
*  b) --> false if T1 is subtype of T2 and 'a' is equals to 'b'
* <p>
* @param tConstraint the type constraint to be verified 
* @return            the status of the list, if errors were found or not
*/
private boolean detectFail(TypeConstraint tConstraint) {
if (!lazyType) {

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch382_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch382_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
if ( (tomMatch382_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch382_1) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tom_tName1= tomMatch382_1.getTomType() ;
if ( (tomMatch382_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch382_2) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 String  tomMatch382_8= tomMatch382_2.getTomType() ;
 String  tom_tName2=tomMatch382_8;
boolean tomMatch382_13= false ;
if ( tom_tName1.equals(tomMatch382_8) ) {
if ( tom_tName2.equals(tomMatch382_8) ) {
tomMatch382_13= true ;
}
}
if (!(tomMatch382_13)) {
if (!( "unknown type".equals(tom_tName1) )) {
if (!( "unknown type".equals(tom_tName2) )) {

printErrorIncompatibility(
tConstraint);
return true;


}
}
}

}
}
}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch382_17= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch382_18= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
if ( (tomMatch382_17 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch382_17) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= tomMatch382_17.getTypeOptions() ;
if ( (tomMatch382_18 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch382_18) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch382_25= tomMatch382_18.getTypeOptions() ;
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2=tomMatch382_25;
if (  tomMatch382_17.getTomType() .equals( tomMatch382_18.getTomType() ) ) {
if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch382__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
do {
{
if (!( tomMatch382__end__32.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch382_43= tomMatch382__end__32.getHeadconcTypeOption() ;
if ( (tomMatch382_43 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch382_43) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch382__end__38=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
do {
{
if (!( tomMatch382__end__38.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch382_46= tomMatch382__end__38.getHeadconcTypeOption() ;
if ( (tomMatch382_46 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch382_46) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch382_45= tomMatch382_46.getRootSymbolName() ;
boolean tomMatch382_53= false ;
if ( ( tomMatch382_43.getRootSymbolName() ==tomMatch382_45) ) {
if ( (tomMatch382_45==tomMatch382_45) ) {
tomMatch382_53= true ;
}
}
if (!(tomMatch382_53)) {
boolean tomMatch382_52= false ;
if ( (tom_tOptions1==tomMatch382_25) ) {
if ( (tom_tOptions2==tomMatch382_25) ) {
tomMatch382_52= true ;
}
}
if (!(tomMatch382_52)) {

printErrorIncompatibility(
tConstraint);
return true;


}

}

}
}
}
if ( tomMatch382__end__38.isEmptyconcTypeOption() ) {
tomMatch382__end__38=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
} else {
tomMatch382__end__38= tomMatch382__end__38.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch382__end__38==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));
}
}
}
}
}
if ( tomMatch382__end__32.isEmptyconcTypeOption() ) {
tomMatch382__end__32=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
} else {
tomMatch382__end__32= tomMatch382__end__32.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch382__end__32==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));
}
}
}
}
}
}
}
}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch382_56= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch382_56;
boolean tomMatch382_61= false ;
if ( (tom_t1==tomMatch382_56) ) {
if ( (tom_t2==tomMatch382_56) ) {
tomMatch382_61= true ;
}
}
if (!(tomMatch382_61)) {

if (!isSubtypeOf(
tom_t1,
tom_t2)) {
printErrorIncompatibility(
tConstraint);
return true;
}


}

}
}
}

}


}

} 
return false;
}

/**
* The method <code>replaceInSubtypingConstraints</code> applies the
* substitutions to a given type constraint list.
* @param tCList the type constraint list to be replaced
* @return       the type constraint list resulting of replacement
*/
private TypeConstraintList replaceInSubtypingConstraints(TypeConstraintList
tCList) {
TypeConstraintList replacedtCList = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
TomType mapT1;
TomType mapT2;
for (TypeConstraint tConstraint: tCList.getCollectionconcTypeConstraint()) {

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;

mapT1 = substitutions.get(
tom_t1);
mapT2 = substitutions.get(
tom_t2); 
if (mapT1 == null) {
mapT1 = 
tom_t1;
}
if (mapT2 == null) {
mapT2 = 
tom_t2;
}
if (mapT1 != mapT2) {
replacedtCList =

addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(mapT1, mapT2,  (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getInfo() ) ,replacedtCList);
}


}
}
}

}

}


}
return replacedtCList;
}

private void replaceInPositiveTVarList() {
TomTypeList replacedPositiveTVarList = 
 tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() ;
TomType mapTVar;
for (TomType tVar: positiveTVarList.getCollectionconcTomType()) {
mapTVar = substitutions.get(tVar);
//DEBUG System.out.println("mapTVar = " + mapTVar);
if (mapTVar == null) {
mapTVar = tVar;
} else {

{
{
if ( (((Object)mapTVar) instanceof tom.engine.adt.tomtype.types.TomType) ) {
boolean tomMatch384_3= false ;
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)mapTVar)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)mapTVar))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
tomMatch384_3= true ;
}
}
if (!(tomMatch384_3)) {
mapTVar = tVar; 
}

}

}

}

}
replacedPositiveTVarList =

 tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType.make(mapTVar,tom_append_list_concTomType(replacedPositiveTVarList, tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType.make() )) ;
}
positiveTVarList = replacedPositiveTVarList;
}

/*
private void calculatePolarities(TypeConstraintList tCList) {
TomTypeList newPTVarList = `concTomType();
for (TomType tVar: positiveTVarList.getCollectionconcTomType()) { 
%match(tCList) {
concTypeConstraint(_*,Subtype[Type1=tVar1@TypeVar[],Type2=tVar2@TypeVar[]],_*)
-> {
if (tVar == `tVar1) {
newPTVarList = `concTomType(tVar2,newPTVarList*);
} else if (tVar == `tVar2){
newPTVarList = `concTomType(tVar1,newPTVarList*);
}
}
}
}
positiveTVarList = `concTomType(newPTVarList*,positiveTVarList*);
}
*/

/**
* The method <code>solveSubtypingConstraints</code> do constraint propagation
* by calling simplification subtyping constraints algorithms to solve the list of
* subtyping constraints. Then if no errors were found, another algorithm is
* called in order to generate solutions for the list. This combination of
* algorithms is done untill the list is empty.
* Otherwise, the simplification is stopped and all error messages are printed  
* <p>
* Propagation of constraints:
*  - PHASE 1: Simplification in equantions
*  - PHASE 2: Reduction in closed form
*  - PHASE 3: Verification of type inconsistencies 
*  - PHASE 4: Canonization
* Generation of solutions
* @param tCList  the subtyping constraint list to be replaced
* @return        the empty solved list or the list that has no solutions
*/
private TypeConstraintList solveSubtypingConstraints(TypeConstraintList tCList) {
TypeConstraintList solvedConstraints = tCList;
TypeConstraintList simplifiedConstraints = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
try {
solvedConstraints = 

tom_make_RepeatId(tom_make_normalizeConstraints(this)).visitLight(solvedConstraints);
//calculatePolarities(simplifiedConstraints);
//solvedConstraints = simplifiedConstraints;
//while (!solvedConstraints.isEmptyconcTypeConstraint()){
while (solvedConstraints != simplifiedConstraints){
simplifiedConstraints = checkInconsistencies(solvedConstraints);

{
{
if ( (((Object)simplifiedConstraints) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch385__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints));
do {
{
if (!( tomMatch385__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch385_7= tomMatch385__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch385_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch385_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint) ) {

System.out.println("Error!!");
return simplifiedConstraints;

}
}
}
if ( tomMatch385__end__4.isEmptyconcTypeConstraint() ) {
tomMatch385__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints));
} else {
tomMatch385__end__4= tomMatch385__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch385__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)simplifiedConstraints))) ));
}
}

}

}

simplifiedConstraints = 
tom_make_RepeatId(tom_make_applyCanonization(this)).visitLight(simplifiedConstraints);
//System.out.println("\n\n###### Before ######");
//printGeneratedConstraints(simplifiedConstraints);
//simplifiedConstraints = garbageCollect(simplifiedConstraints);
//System.out.println("\n\n###### After ######");
//printGeneratedConstraints(simplifiedConstraints);
solvedConstraints = generateSolutions(simplifiedConstraints);
}
solvedConstraints = analyseRemaining(solvedConstraints);
//solvedConstraints = generateSolutions(simplifiedConstraints);
//if (solvedConstraints == simplifiedConstraints) {
//  return simplifiedConstraints;
//}
//DEBUG System.out.println("\nResulting subtype constraints!!");
//DEBUG printGeneratedConstraints(solvedConstraints);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("solveSubtypingConstraints: failure on " +
solvedConstraints);
}
return solvedConstraints;
}

/**
* The method <code>normalizeConstraints</code> is generated by a
* strategy which simplifies a subtype constraints list.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* PHASE 1: Simplification in equations (apply anti-symetry of the partial
* order "<:"): 
* tCList = {T1 <: T2, T2 <: T1} U tCList' and Map 
*  --> solveEquationConstraints(T1 = T2) U tCList' and Map
* tCList = {A1 <: A2, A2 <: A1} U tCList' and Map 
*  --> solveEquationConstraints(A1 = A2) U tCList' and Map
* <p>
* PHASE 2: Reduction in closed form (apply transitivity of the partial
* order "<:"):
* tCList = {T1 <: A,A <: T2} U tCList' and Map --> {T1 <: T2} U tCList and Map
* tCList = {A1 <:A,A <: A2} U tCList' and Map --> {A1 <: A2} U tCList and Map
* <p>
* @param nkt an instance of object NewKernelTyper
* @return    the subtype constraint list resulting
*/

public static class normalizeConstraints extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public normalizeConstraints( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
do {
{
if (!( tomMatch386__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_14= tomMatch386__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch386_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch386_14.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch386_14.getType2() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386_5= tomMatch386__end__4.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__8=tomMatch386_5;
do {
{
if (!( tomMatch386__end__8.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_18= tomMatch386__end__8.getHeadconcTypeConstraint() ;
if ( (tomMatch386_18 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_18) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t2== tomMatch386_18.getType1() ) ) {
if ( (tom_t1== tomMatch386_18.getType2() ) ) {

//DEBUG System.out.println("\nsolve1: " + `tcl);
nkt.solveEquationConstraints(
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.Equation.make(tom_t1, tom_t2,  tomMatch386_14.getInfo() ) , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) );
return
nkt.
tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch386__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint(tom_get_slice_concTypeConstraint(tomMatch386_5,tomMatch386__end__8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ),tom_append_list_concTypeConstraint( tomMatch386__end__8.getTailconcTypeConstraint() , tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));


}
}
}
}
}
if ( tomMatch386__end__8.isEmptyconcTypeConstraint() ) {
tomMatch386__end__8=tomMatch386_5;
} else {
tomMatch386__end__8= tomMatch386__end__8.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__8==tomMatch386_5) ));
}
}
}
if ( tomMatch386__end__4.isEmptyconcTypeConstraint() ) {
tomMatch386__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
} else {
tomMatch386__end__4= tomMatch386__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
do {
{
if (!( tomMatch386__end__27.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_36= tomMatch386__end__27.getHeadconcTypeConstraint() ;
if ( (tomMatch386_36 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_36) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch386_35= tomMatch386_36.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch386_36.getType1() ;
if ( (tomMatch386_35 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch386_35) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386_28= tomMatch386__end__27.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__31=tomMatch386_28;
do {
{
if (!( tomMatch386__end__31.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_43= tomMatch386__end__31.getHeadconcTypeConstraint() ;
if ( (tomMatch386_43 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_43) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tomMatch386_35== tomMatch386_43.getType1() ) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch386_43.getType2() ;
if ( (((Object)tom_tcl) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch386_58= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));
do {
{
if (!( tomMatch386__end__48.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_54= tomMatch386__end__48.getHeadconcTypeConstraint() ;
if ( (tomMatch386_54 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_54) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t1== tomMatch386_54.getType1() ) ) {
if ( (tom_t2== tomMatch386_54.getType2() ) ) {
tomMatch386_58= true ;
}
}
}
}
}
if ( tomMatch386__end__48.isEmptyconcTypeConstraint() ) {
tomMatch386__end__48=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));
} else {
tomMatch386__end__48= tomMatch386__end__48.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__48==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) ));
}
if (!(tomMatch386_58)) {

//DEBUG System.out.println("\nsolve2a: tcl =" + `tcl);
return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch386_43.getInfo() ) ,tom_tcl);


}

}
}
}
}
}
if ( tomMatch386__end__31.isEmptyconcTypeConstraint() ) {
tomMatch386__end__31=tomMatch386_28;
} else {
tomMatch386__end__31= tomMatch386__end__31.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__31==tomMatch386_28) ));
}
}
}
}
}
if ( tomMatch386__end__27.isEmptyconcTypeConstraint() ) {
tomMatch386__end__27=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
} else {
tomMatch386__end__27= tomMatch386__end__27.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__27==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
do {
{
if (!( tomMatch386__end__64.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_74= tomMatch386__end__64.getHeadconcTypeConstraint() ;
if ( (tomMatch386_74 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_74) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch386_71= tomMatch386_74.getType1() ;
if ( (tomMatch386_71 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch386_71) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2= tomMatch386_74.getType2() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386_65= tomMatch386__end__64.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__68=tomMatch386_65;
do {
{
if (!( tomMatch386__end__68.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_80= tomMatch386__end__68.getHeadconcTypeConstraint() ;
if ( (tomMatch386_80 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_80) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t1= tomMatch386_80.getType1() ;
if ( (tomMatch386_71== tomMatch386_80.getType2() ) ) {
if ( (((Object)tom_tcl) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch386_95= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch386__end__85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));
do {
{
if (!( tomMatch386__end__85.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch386_91= tomMatch386__end__85.getHeadconcTypeConstraint() ;
if ( (tomMatch386_91 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch386_91) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
if ( (tom_t1== tomMatch386_91.getType1() ) ) {
if ( (tom_t2== tomMatch386_91.getType2() ) ) {
tomMatch386_95= true ;
}
}
}
}
}
if ( tomMatch386__end__85.isEmptyconcTypeConstraint() ) {
tomMatch386__end__85=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl));
} else {
tomMatch386__end__85= tomMatch386__end__85.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__85==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom_tcl))) ));
}
if (!(tomMatch386_95)) {

//DEBUG System.out.println("\nsolve2b: tcl = " + `tcl);
return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2,  tomMatch386_74.getInfo() ) ,tom_tcl);


}

}
}
}
}
}
if ( tomMatch386__end__68.isEmptyconcTypeConstraint() ) {
tomMatch386__end__68=tomMatch386_65;
} else {
tomMatch386__end__68= tomMatch386__end__68.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__68==tomMatch386_65) ));
}
}
}
}
}
if ( tomMatch386__end__64.isEmptyconcTypeConstraint() ) {
tomMatch386__end__64=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
} else {
tomMatch386__end__64= tomMatch386__end__64.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch386__end__64==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));
}
}

}


}
return _visit_TypeConstraintList(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_normalizeConstraints( NewKernelTyper  t0) { 
return new normalizeConstraints(t0);
}


/**
* The method <code>checkInconsistencies</code> verify if there are type
* inconsistencies. This is done for each type constraint of tCList.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* tCList = {T1 <: T2} U tCList' and Map --> detectFail(T1 <: T2) U tCList and Map
* Note that the constraint "{T1 <: T2}" must be keeped in tCList to avoid an
* infinity loop when re-applying <code>normalizeConstraints</code> method.
* <p>
* @param nkt an instance of object NewKernelTyper
* @return    the subtype constraint list resulting
*/
private TypeConstraintList checkInconsistencies(TypeConstraintList tCList) {
/* PHASE 3 */
boolean errorFound = false;
TypeConstraintList simplifiedtCList = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ;
for (TypeConstraint tConstraint :
tCList.getCollectionconcTypeConstraint()) {
matchBlock :
{

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch387_1= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch387_2= (( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)).getType2() ;
boolean tomMatch387_10= false ;
if ( (tomMatch387_1 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch387_1) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
tomMatch387_10= true ;
}
}
if (!(tomMatch387_10)) {
boolean tomMatch387_9= false ;
if ( (tomMatch387_2 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch387_2) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
tomMatch387_9= true ;
}
}
if (!(tomMatch387_9)) {

//DEBUG System.out.println("\nsolve3: tConstraint=" + `tConstraint);
errorFound = (errorFound || detectFail(
tConstraint));
break matchBlock;


}

}

}
}
}

}
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {

simplifiedtCList = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make(tConstraint,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}

}


}

}
}
if (errorFound) { 
simplifiedtCList = 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(simplifiedtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;
}
return simplifiedtCList;
}

/*
private TypeConstraintList garbageCollect(TypeConstraintList tCList) {
TypeConstraintList simplifiedtCList = `concTypeConstraint();
for (TypeConstraint tConstraint :
tCList.getCollectionconcTypeConstraint()) {
%match {
Subtype[Type1=t1,Type2=t2] << tConstraint &&
(concTomType(_*,t1,_*) << positiveTVarList || 
concTomType(_*,t2,_*) << positiveTVarList) -> {
simplifiedtCList = `concTypeConstraint(tConstraint,simplifiedtCList*);
}
}
}
return simplifiedtCList;
}
*/

/**
* The method <code>applyCanonization</code> is generated by a
* strategy which simplifies a subtype constraints list by looking for exactly
* one non-variable lower and one non-variable upper bound for each type
* variable, i.e. put tCList in a canonical form.
* <p>
* Let 'Ai' and 'Ti' be type variables and ground types, respectively:
* <p>
* - Upper bound:
* tCList = {A <: T1,A <:T2} U tCList' and Map 
*   --> {A <: T1} U tCList' and Map
*       if T1 <: T2 
*   --> {A <: T2} U tCList' and Map
*       if T2 <: T1
*   --> {False} U tCList' and Map
*       if T1 and T2 are not comparable
* - Lower bound:
* tCList = {T1 <: A,T2 <: A} U tCList' and Map
*   --> {T <: A} U tCList' and Map
*       if there exists T such that T is the lowest upper bound of T1 and T2
*   --> {False} U tCList' and Map
*       if there does not exist T such that T is the lowest upper bound of T1 and T2
* <p>
* @param nkt an instance of object NewKernelTyper
* @return    the subtype constraint list resulting
*/

public static class applyCanonization extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public applyCanonization( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
return ((T)visit_TypeConstraintList((( tom.engine.adt.typeconstraints.types.TypeConstraintList )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  _visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.typeconstraints.types.TypeConstraintList )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.typeconstraints.types.TypeConstraintList  visit_TypeConstraintList( tom.engine.adt.typeconstraints.types.TypeConstraintList  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch388__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch388__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch388_14= tomMatch388__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch388_14 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch388_14) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch388_11= tomMatch388_14.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch388_12= tomMatch388_14.getType2() ;
if ( (tomMatch388_11 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_11) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch388_11;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch388_12;
 tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch388_14.getInfo() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388_5= tomMatch388__end__4.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388__end__8=tomMatch388_5;
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch388_5,tomMatch388__end__8, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch388__end__8.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch388_20= tomMatch388__end__8.getHeadconcTypeConstraint() ;
if ( (tomMatch388_20 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch388_20) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch388_19= tomMatch388_20.getType2() ;
if ( (tom_tVar== tomMatch388_20.getType1() ) ) {
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch388_19;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch388__end__8.getTailconcTypeConstraint() ;
boolean tomMatch388_30= false ;
if ( (tomMatch388_12 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_12) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t1==tomMatch388_12) ) {
tomMatch388_30= true ;
}
}
}
if (!(tomMatch388_30)) {
boolean tomMatch388_29= false ;
if ( (tomMatch388_19 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_19) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t2==tomMatch388_19) ) {
tomMatch388_29= true ;
}
}
}
if (!(tomMatch388_29)) {

//DEBUG System.out.println("\nsolve4a: " + `constraint + " and " + `c2);
TomType lowerType = nkt.
minType(tom_t1,tom_t2);
//DEBUG System.out.println("\nminType(" + `t1.getTomType() + "," +
//DEBUG     `t2.getTomType() + ") = " + lowerType);

if (lowerType == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
nkt.printErrorIncompatibility(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
return 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
}

return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_tVar, lowerType, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));


}

}

}
}
}
}
if ( tomMatch388__end__8.isEmptyconcTypeConstraint() ) {
tomMatch388__end__8=tomMatch388_5;
} else {
tomMatch388__end__8= tomMatch388__end__8.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch388__end__8==tomMatch388_5) ));
}
}
}
}
}
if ( tomMatch388__end__4.isEmptyconcTypeConstraint() ) {
tomMatch388__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
} else {
tomMatch388__end__4= tomMatch388__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch388__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));
}
}

}
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388__end__35=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl1=tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg)),tomMatch388__end__35, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch388__end__35.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch388_45= tomMatch388__end__35.getHeadconcTypeConstraint() ;
if ( (tomMatch388_45 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch388_45) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch388_42= tomMatch388_45.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch388_43= tomMatch388_45.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_t1=tomMatch388_42;
if ( (tomMatch388_43 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_43) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar=tomMatch388_43;
 tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch388_45.getInfo() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388_36= tomMatch388__end__35.getTailconcTypeConstraint() ;
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch388__end__39=tomMatch388_36;
do {
{
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl2=tom_get_slice_concTypeConstraint(tomMatch388_36,tomMatch388__end__39, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
if (!( tomMatch388__end__39.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch388_51= tomMatch388__end__39.getHeadconcTypeConstraint() ;
if ( (tomMatch388_51 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch388_51) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch388_49= tomMatch388_51.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tom_t2=tomMatch388_49;
if ( (tom_tVar== tomMatch388_51.getType2() ) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tcl3= tomMatch388__end__39.getTailconcTypeConstraint() ;
boolean tomMatch388_61= false ;
if ( (tomMatch388_42 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_42) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t1==tomMatch388_42) ) {
tomMatch388_61= true ;
}
}
}
if (!(tomMatch388_61)) {
boolean tomMatch388_60= false ;
if ( (tomMatch388_49 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch388_49) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_t2==tomMatch388_49) ) {
tomMatch388_60= true ;
}
}
}
if (!(tomMatch388_60)) {

//DEBUG System.out.println("\nsolve4b: " + `constraint + " and " + `c2);
TomType supType = nkt.
supType(tom_t1,tom_t2);
//DEBUG System.out.println("\nsupType(" + `t1.getTomType() + "," +
//DEBUG    `t2.getTomType() + ") = " + supType);

if (supType == 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ) {
nkt.printErrorIncompatibility(
 tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(tom_t1, tom_t2, tom_info) );
return 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )))) ; 
}

return
nkt.
addSubConstraint( tom.engine.adt.typeconstraints.types.typeconstraint.Subtype.make(supType, tom_tVar, tom_info) ,tom_append_list_concTypeConstraint(tom_tcl1,tom_append_list_concTypeConstraint(tom_tcl2,tom_append_list_concTypeConstraint(tom_tcl3, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ))));


}

}

}
}
}
}
if ( tomMatch388__end__39.isEmptyconcTypeConstraint() ) {
tomMatch388__end__39=tomMatch388_36;
} else {
tomMatch388__end__39= tomMatch388__end__39.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch388__end__39==tomMatch388_36) ));
}
}
}
}
}
if ( tomMatch388__end__35.isEmptyconcTypeConstraint() ) {
tomMatch388__end__35=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg));
} else {
tomMatch388__end__35= tomMatch388__end__35.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch388__end__35==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tom__arg))) ));
}
}

}


}
return _visit_TypeConstraintList(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_applyCanonization( NewKernelTyper  t0) { 
return new applyCanonization(t0);
}


/**
* The method <code>findVar</code> checks if a given type variable occurs in
* a given type constraint list.
* @param tVar   the type variable to be found
* @param tCList the type constraint list to be checked
* @return        'true' if the type variable occurs in the list
*                'false' otherwise
*/
private boolean findVar(TomType tVar, TypeConstraintList tCList) {

{
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch389__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch389__end__5.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_9= tomMatch389__end__5.getHeadconcTypeConstraint() ;
boolean tomMatch389_13= false ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_10= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch389_8= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_11= null ;
if ( (tomMatch389_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch389_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch389_13= true ;
tomMatch389_10=tomMatch389_9;
tomMatch389_8= tomMatch389_10.getType1() ;

}
} else {
if ( (tomMatch389_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch389_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch389_13= true ;
tomMatch389_11=tomMatch389_9;
tomMatch389_8= tomMatch389_11.getType1() ;

}
}
}
}
}
if (tomMatch389_13) {
if ( (((Object)tVar) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( (tomMatch389_8==(( tom.engine.adt.tomtype.types.TomType )((Object)tVar))) ) {
return true; 
}
}
}

}
if ( tomMatch389__end__5.isEmptyconcTypeConstraint() ) {
tomMatch389__end__5=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch389__end__5= tomMatch389__end__5.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch389__end__5==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}

}
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch389__end__19=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch389__end__19.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_23= tomMatch389__end__19.getHeadconcTypeConstraint() ;
boolean tomMatch389_27= false ;
 tom.engine.adt.tomtype.types.TomType  tomMatch389_22= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_25= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch389_24= null ;
if ( (tomMatch389_23 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch389_23) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch389_27= true ;
tomMatch389_24=tomMatch389_23;
tomMatch389_22= tomMatch389_24.getType2() ;

}
} else {
if ( (tomMatch389_23 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch389_23) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch389_27= true ;
tomMatch389_25=tomMatch389_23;
tomMatch389_22= tomMatch389_25.getType2() ;

}
}
}
}
}
if (tomMatch389_27) {
if ( (((Object)tVar) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( (tomMatch389_22==(( tom.engine.adt.tomtype.types.TomType )((Object)tVar))) ) {
return true; 
}
}
}

}
if ( tomMatch389__end__19.isEmptyconcTypeConstraint() ) {
tomMatch389__end__19=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch389__end__19= tomMatch389__end__19.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch389__end__19==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}

}


}

return false;
}

/**
* The method <code>minType</code> tries to find the common lowertype
* between two given ground types.
* @param t1 a type
* @param t2 another type
* @return    the lowertype between 't1' and 't2' if the subtype relation
*            holds between them (since a multiple inheritance is forbidden)
*            the 'EmptyType()' otherwise
*/
private TomType minType(TomType t1, TomType t2) {
//DEBUG System.out.println("minType: t1 = " + t1 + " and t2 = " + t2);
//DEBUG System.out.println("minType: isSubtypeOf(t1,t2) = " + isSubtypeOf(t1,t2));
if (isSubtypeOf(t1,t2)) { return t1; } 
if (isSubtypeOf(t2,t1)) { return t2; }
//DEBUG System.out.println("case 3");
return 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
}

/**
* The method <code>isSubtypeOf</code> checks if type t1 is a subtype of type
* t2 considering symmetry and type decorations of both t1 and t2.
* <p> 
* There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
* ground types Ti^c which are decorated with a given symbol c. Considering a
* partial order over ground types "<:" as a binary relation where "T1^c1 <: T2^c2"
* is equivalent to "T1 is subtype of T2 and (c1 == c2 or c2 == ?)", then the set of all possibilities of arrangement between
* ground types is a sequence with repetition. Then, we have 4 possible cases (since
* 2^2 = 4).
* <p>
* CASE 1: T1^? <: T2^?
*  a) --> true if T1 == T2 or T1 is a proper subtype of T2
*  b) --> false otherwise
* <p>
* CASE 2: T1^? <: T2^c
*   --> false
* <p>
* CASE 3: T1^c <: T2^?
*  a) --> true if T1 == T2 or T1 is a proper subtype of T2
*  b) --> false otherwise
* <p>
* CASE 4: T1^c1 <: T2^c2
*  a) --> true if 'a' is equals to 'b' and (T1 == T2 or T1 is a proper
*  subtype of T2)
*  b) --> false otherwise
* <p>
* @param t1 a ground (decorated) type
* @param t2 another ground (decorated) type
* @return   true if t1 <: t2 and false otherwise
*/
private boolean isSubtypeOf(TomType t1, TomType t2) {
//DEBUG System.out.println("isSubtypeOf: t1 = " + t1 + " and t2 = " + t2);

{
{
if ( (((Object)t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions1= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTypeOptions() ;
 String  tom_tName1= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTomType() ;
if ( (((Object)t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tom_tOptions2= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTypeOptions() ;
 String  tom_tName2= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTomType() ;

TomTypeList supTypet1 = dependencies.get(
tom_tName1);

{
{
if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
boolean tomMatch391_9= false ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
do {
{
if (!( tomMatch391__end__4.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_7= tomMatch391__end__4.getHeadconcTypeOption() ;
if ( (tomMatch391_7 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_7) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch391_9= true ;
}
}
}
if ( tomMatch391__end__4.isEmptyconcTypeOption() ) {
tomMatch391__end__4=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
} else {
tomMatch391__end__4= tomMatch391__end__4.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__4==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));
}
if (!(tomMatch391_9)) {
if ( tom_tName2.equals(tom_tName1) ) {

//DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - i");
return true; 

}
}

}

}
{
if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((Object)supTypet1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch391__end__15=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));
do {
{
if (!( tomMatch391__end__15.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tomMatch391_19= tomMatch391__end__15.getHeadconcTomType() ;
if ( (tomMatch391_19 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch391_19) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
boolean tomMatch391_29= false ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__24=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
do {
{
if (!( tomMatch391__end__24.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_27= tomMatch391__end__24.getHeadconcTypeOption() ;
if ( (tomMatch391_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
tomMatch391_29= true ;
}
}
}
if ( tomMatch391__end__24.isEmptyconcTypeOption() ) {
tomMatch391__end__24=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
} else {
tomMatch391__end__24= tomMatch391__end__24.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__24==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));
}
if (!(tomMatch391_29)) {
if (  tomMatch391_19.getTomType() .equals(tom_tName2) ) {

//DEBUG System.out.println("isSubtypeOf: cases 1a and 3a - ii");
return true; 

}
}

}
}
}
if ( tomMatch391__end__15.isEmptyconcTomType() ) {
tomMatch391__end__15=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));
} else {
tomMatch391__end__15= tomMatch391__end__15.getTailconcTomType() ;
}

}
} while(!( (tomMatch391__end__15==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) ));
}
}
}

}
{
if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__35=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
do {
{
if (!( tomMatch391__end__35.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_45= tomMatch391__end__35.getHeadconcTypeOption() ;
if ( (tomMatch391_45 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_45) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__41=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
do {
{
if (!( tomMatch391__end__41.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_48= tomMatch391__end__41.getHeadconcTypeOption() ;
if ( (tomMatch391_48 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_48) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( ( tomMatch391_45.getRootSymbolName() == tomMatch391_48.getRootSymbolName() ) ) {
if ( tom_tName2.equals(tom_tName1) ) {

//DEBUG System.out.println("isSubtypeOf: case 4a - i");
return true; 

}
}
}
}
}
if ( tomMatch391__end__41.isEmptyconcTypeOption() ) {
tomMatch391__end__41=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
} else {
tomMatch391__end__41= tomMatch391__end__41.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__41==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));
}
}
}
}
}
if ( tomMatch391__end__35.isEmptyconcTypeOption() ) {
tomMatch391__end__35=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
} else {
tomMatch391__end__35= tomMatch391__end__35.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__35==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));
}
}

}
{
if ( (((Object)tom_tOptions1) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__57=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
do {
{
if (!( tomMatch391__end__57.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_73= tomMatch391__end__57.getHeadconcTypeOption() ;
if ( (tomMatch391_73 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_73) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( (((Object)tom_tOptions2) instanceof tom.engine.adt.tomtype.types.TypeOptionList) ) {
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch391__end__63=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
do {
{
if (!( tomMatch391__end__63.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch391_76= tomMatch391__end__63.getHeadconcTypeOption() ;
if ( (tomMatch391_76 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch391_76) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( ( tomMatch391_73.getRootSymbolName() == tomMatch391_76.getRootSymbolName() ) ) {
if ( (((Object)supTypet1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch391__end__69=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));
do {
{
if (!( tomMatch391__end__69.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tomMatch391_79= tomMatch391__end__69.getHeadconcTomType() ;
if ( (tomMatch391_79 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch391_79) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if (  tomMatch391_79.getTomType() .equals(tom_tName2) ) {

//DEBUG System.out.println("isSubtypeOf: case 4a - ii");
return true; 

}
}
}
}
if ( tomMatch391__end__69.isEmptyconcTomType() ) {
tomMatch391__end__69=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1));
} else {
tomMatch391__end__69= tomMatch391__end__69.getTailconcTomType() ;
}

}
} while(!( (tomMatch391__end__69==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypet1))) ));
}
}
}
}
}
}
if ( tomMatch391__end__63.isEmptyconcTypeOption() ) {
tomMatch391__end__63=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2));
} else {
tomMatch391__end__63= tomMatch391__end__63.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__63==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions2))) ));
}
}
}
}
}
if ( tomMatch391__end__57.isEmptyconcTypeOption() ) {
tomMatch391__end__57=(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1));
} else {
tomMatch391__end__57= tomMatch391__end__57.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch391__end__57==(( tom.engine.adt.tomtype.types.TypeOptionList )((Object)tom_tOptions1))) ));
}
}

}


}

return false;


}
}
}
}
}
}

}

}

// Remain cases
return false;
}

/**
* The method <code>supType</code> tries to find the lowest common uppertype
* of two given ground types.
* <p>
* There exists 2 kinds of ground types: ground types Ti (represented by Ti^?) and
* ground types Ti^c which are decorated with a given symbol c. Considering
* them by pairs, then the set of all possibilities of arrangement between
* ground types is a sequence with repetition. Then, we have 4 possible cases (since
* 2^2 = 4).
* <p>
* CASE 1: T1^? and  T2^?
*  a) --> T2^? if T1 is a subtype of T2
*  b) --> T1^? if T2 is a subtype of T1
*  c) --> emptyType otherwise
* <p>
* CASE 2: T1^? and T2^c
*  a) --> T1^? if T2 is a subtype of T1
*  b) --> T3^? if T3 is the join between T1 and T2
*  c) --> emptyType otherwise
* <p>
* CASE 3: T1^c and T2^? 
*  similar to CASE 2.
* <p>
* CASE 4: T1^c1 and T2^c2
*  - 4.1 : T1 == T2
*      --> T1^?
*  - 4.2 : T1 != T2
*      a) --> T2^c1 if T1 is a subtype of T2 and c1 == c2
*      b) --> T1^c1 if T2 is a subtype of T1 and c1 == c2
*      c) --> T3^? if T3 is the join between T1 and T2 
*      d) --> emptyType otherwise
* <p>
* OBS.: when the subtype relation does not hold between two types 'T1' and
* 'T2', the method considers the intersection of the supertypes lists
* supertypes_T1 and supertypes_T2 and searches for the 'immediate' common
* supertype 'Ti' (i.e. the one which has the bigger supertypes_Ti list) 
* @param t1  a type
* @param t2  another type
* @return    the uppertype between 't1' and 't2' if the subtype relation
*            holds between them or the lowest common uppertype of them if
*            they are not in subtype relation 
*            the 'EmptyType()' otherwise
*/
private TomType supType(TomType t1, TomType t2) {
/* CASES 1a, 1b, 2a, 3a, 4.2a and 4.2b */
if (isSubtypeOf(t1,t2)) { return t2; } 
if (isSubtypeOf(t2,t1)) { return t1; }
TomTypeList supTypes1 = dependencies.get(t1.getTomType());
TomTypeList supTypes2 = dependencies.get(t2.getTomType());

{
{
if ( (((Object)t1) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch392_2= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTypeOptions() ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch392_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch392_2) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch392__end__13=tomMatch392_2;
do {
{
if (!( tomMatch392__end__13.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch392_24= tomMatch392__end__13.getHeadconcTypeOption() ;
if ( (tomMatch392_24 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch392_24) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
 String  tom_tName= (( tom.engine.adt.tomtype.types.TomType )((Object)t1)).getTomType() ;
if ( (((Object)t2) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)t2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)t2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch392_6= (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTypeOptions() ;
if ( (((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch392_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.ConsconcTypeOption) || ((( tom.engine.adt.tomtype.types.TypeOptionList )tomMatch392_6) instanceof tom.engine.adt.tomtype.types.typeoptionlist.EmptyconcTypeOption)) ) {
 tom.engine.adt.tomtype.types.TypeOptionList  tomMatch392__end__19=tomMatch392_6;
do {
{
if (!( tomMatch392__end__19.isEmptyconcTypeOption() )) {
 tom.engine.adt.tomtype.types.TypeOption  tomMatch392_27= tomMatch392__end__19.getHeadconcTypeOption() ;
if ( (tomMatch392_27 instanceof tom.engine.adt.tomtype.types.TypeOption) ) {
if ( ((( tom.engine.adt.tomtype.types.TypeOption )tomMatch392_27) instanceof tom.engine.adt.tomtype.types.typeoption.WithSymbol) ) {
if ( tom_tName.equals( (( tom.engine.adt.tomtype.types.TomType )((Object)t2)).getTomType() ) ) {
boolean tomMatch392_30= false ;
if ( ( tomMatch392_24.getRootSymbolName() == tomMatch392_27.getRootSymbolName() ) ) {
tomMatch392_30= true ;
}
if (!(tomMatch392_30)) {

//DEBUG System.out.println("\nIn supType: case 4.1");
// Return the equivalent groudn type without decoration
return symbolTable.getType(
tom_tName); 


}

}
}
}
}
if ( tomMatch392__end__19.isEmptyconcTypeOption() ) {
tomMatch392__end__19=tomMatch392_6;
} else {
tomMatch392__end__19= tomMatch392__end__19.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch392__end__19==tomMatch392_6) ));
}
}
}
}
}
}
}
if ( tomMatch392__end__13.isEmptyconcTypeOption() ) {
tomMatch392__end__13=tomMatch392_2;
} else {
tomMatch392__end__13= tomMatch392__end__13.getTailconcTypeOption() ;
}

}
} while(!( (tomMatch392__end__13==tomMatch392_2) ));
}
}
}
}

}
{
if ( (((Object)supTypes1) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((Object)supTypes2) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
boolean tomMatch392_36= false ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if ( (( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes1)).isEmptyconcTomType() ) {
tomMatch392_36= true ;
}
}
if (!(tomMatch392_36)) {
boolean tomMatch392_35= false ;
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
if ( (( tom.engine.adt.tomtype.types.TomTypeList )((Object)supTypes2)).isEmptyconcTomType() ) {
tomMatch392_35= true ;
}
}
if (!(tomMatch392_35)) {

int st1Size = 
supTypes1.length();
int st2Size = 
supTypes2.length();

int currentIntersectionSize = -1;
int commonTypeIntersectionSize = -1;
TomType lowestCommonType = 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
//DEBUG System.out.println("\nIn supType: cases 2b, 3b, 4.2c");
for (TomType currentType:
supTypes1.getCollectionconcTomType()) {
currentIntersectionSize = dependencies.get(currentType.getTomType()).length();
if (
supTypes2.getCollectionconcTomType().contains(currentType) &&
(currentIntersectionSize > commonTypeIntersectionSize)) {
commonTypeIntersectionSize = currentIntersectionSize;
lowestCommonType = currentType;
}
}
return lowestCommonType;


}

}

}
}

}


}

/* Remaining CASES */
return 
 tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
}

/**
* The method <code>generateSolutions</code> chooses a solution for a subtype
* constraint list when many possibilities are available. The algorithm picks
* the lowest possible upper bound proposed for each type variable. For this
* reason, the order of rules is important.
* <p>
* A subtype constraint can be written by 3 formats:
* F1 - A <: T
* F2 - T <: A
* F3 - A1 <: A2
* where 'Ai' and 'Ti' are respectively type variables and ground types (and
* the constraint 'T1 <: T2' was already handled by garbage collecting in
* <code>solveSubtypeConstraints</code> method.
* These constraints are enumerated by cases:
* <p>
* CASE 1:
* tCList = {A <: T} U tCList' and Map 
*  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
* <p>
* CASE 2:
* tCList = {T <: A} U tCList' and Map 
*  -->  tCList = tCList' and {(A,T)} U Map if A is not in Var(tCList')
* <p>
* CASE 3:
* tCList = {A1 <: A2} U tCList' and Map 
*  -->  tCList = tCList' and {(A1,A2)} U Map if Ai are not in tCList'
* <p>
* Considering combination of {F1 x F3}, {F2 x F3}, {F3 x F3} and {F1 x F2} (since 
* {F1 x F1} and {F2 x F2} are treated by PHASE 4 of
* <code>solveSubtypingConstraints</code> or by previous cases):
* <p>
* CASE 4 {F1 x F3} and {F3 x F1}:
*    a) tCList = {A <: T, A <: A1} U tCList' and Map
*      -->  tCList = {T <: A1} U [A/T]tCList' and {(A,T)} U Map
*    TODO : verify rule b
*    b) tCList = {A <: T, A1 <: A} U tCList' and Map 
*      --> tCList = tClist' and Map or do nothing?
*    c) tCList = {A <: T, A1 <: A2} U tCList' and Map 
*      --> CASE 1 for A <: T and CASE 3 for A1 <: A2 
* <p>
* CASE 5 {F2 x F3} and {F3 x F2}:
*    a) tCList = {T <: A, A1 <: A} U tCList' and Map 
*      -->  tCList = {A1 <: T} U [A/T]tCList' and {(A,T)} U Map
*    TODO : verify rule b
*    b) tCList = {T <: A, A <: A1} U tCList' and Map 
*      --> tCList = tClist' and Map or do nothing?
*    c) tCList = {T <: A, A1 <: A2} U tCList' and Map 
*      --> CASE 2 for T <: A and CASE 3 for A1 <: A2
* <p>
* CASE 6 {F1 x F2} and {F2 x F1}:
*    a) tCList = {A <: T, T <: A1} U tCList' and Map 
*      --> CASE 1 for A <: T and CASE 2 for T <: A1 
*    b) tCList = {A <: T, T1 <: A1} U tCList' and Map 
*      --> CASE 1 for A <: T and CASE 2 for T1 <: A1 
*    c) tCList = {A <: T, T1 <: A} U tCList' and Map 
*      -->  tCList = {T1 <: T} U [A/T]tCList' and {(A,T)} U Map
*     Note: in this case T1 is different from T (since the case where T1 is
*     equals to T is treated by PHASE 1 of
*     <code>solveSubtypingConstraints</code>.
* <p>
* CASE 7 {F3 x F3}:
*    a) tCList = {A <: A1, A <: A2} U tCList' and Map 
*      --> Nothing, java must infer the right type 
*    b) tCList = {A <: A1, A2 <: A3} U tCList' and Map 
*      --> Nothing, java must infer the right type 
*    c) tCList = {A1 <: A, A2 <: A} U tCList' and Map 
*      --> Nothing, java must infer the right type 
*    d) tCList = {A1 <: A, A <: A2} U tCList' and Map 
*      --> Nothing, java must infer the right type 
* <p>
* @param nkt an instance of object NewKernelTyper
* @return    the subtype constraint list resulting
*/
private TypeConstraintList generateSolutions(TypeConstraintList tCList) {
TypeConstraintList newtCList = tCList;
matchBlockSolve :
{

{
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch393__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch393__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch393_9= tomMatch393__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch393_9 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch393_9) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch393_7= tomMatch393_9.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch393_8= tomMatch393_9.getType2() ;
if ( (tomMatch393_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch393_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch393_8;
boolean tomMatch393_16= false ;
if ( (tomMatch393_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch393_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch393_8) ) {
tomMatch393_16= true ;
}
}
}
if (!(tomMatch393_16)) {

//DEBUG System.out.println("\nenumerateSolutions1: " + `c1);
TypeConstraintList newLeftTCL = 
tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)),tomMatch393__end__4, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
TypeConstraintList newRightTCL = 
 tomMatch393__end__4.getTailconcTypeConstraint() ;
//if (!`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
// Same code of cases 7 and 8 of solveEquationConstraints
addSubstitution(
tomMatch393_7,
tom_groundType);
newtCList =

replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(newLeftTCL,tom_append_list_concTypeConstraint(newRightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
break matchBlockSolve;
//}


}

}
}
}
}
}
if ( tomMatch393__end__4.isEmptyconcTypeConstraint() ) {
tomMatch393__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch393__end__4= tomMatch393__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch393__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}

}
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch393__end__21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch393__end__21.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch393_26= tomMatch393__end__21.getHeadconcTypeConstraint() ;
if ( (tomMatch393_26 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch393_26) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch393_24= tomMatch393_26.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch393_25= tomMatch393_26.getType2() ;
 tom.engine.adt.tomtype.types.TomType  tom_groundType=tomMatch393_24;
if ( (tomMatch393_25 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch393_25) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
boolean tomMatch393_33= false ;
if ( (tomMatch393_24 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch393_24) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tom_groundType==tomMatch393_24) ) {
tomMatch393_33= true ;
}
}
}
if (!(tomMatch393_33)) {

//DEBUG System.out.println("\nenumerateSolutions2: " + `c1);
TypeConstraintList newLeftTCL = 
tom_get_slice_concTypeConstraint((( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)),tomMatch393__end__21, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() );
TypeConstraintList newRightTCL = 
 tomMatch393__end__21.getTailconcTypeConstraint() ;
//if (!`findVar(tVar,concTypeConstraint(leftTCL,rightTCL))) {
addSubstitution(
tomMatch393_25,
tom_groundType);
newtCList =

replaceInSubtypingConstraints(tom_append_list_concTypeConstraint(newLeftTCL,tom_append_list_concTypeConstraint(newRightTCL, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )));
break matchBlockSolve;
//}


}

}
}
}
}
}
if ( tomMatch393__end__21.isEmptyconcTypeConstraint() ) {
tomMatch393__end__21=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch393__end__21= tomMatch393__end__21.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch393__end__21==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}

}


}

}
return newtCList;
}

private TypeConstraintList analyseRemaining(TypeConstraintList tCList) {
TypeConstraintList newtCList = tCList;

{
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tomMatch394__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
do {
{
if (!( tomMatch394__end__4.isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch394_10= tomMatch394__end__4.getHeadconcTypeConstraint() ;
if ( (tomMatch394_10 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch394_10) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.tomtype.types.TomType  tomMatch394_7= tomMatch394_10.getType1() ;
 tom.engine.adt.tomtype.types.TomType  tomMatch394_8= tomMatch394_10.getType2() ;
if ( (tomMatch394_7 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch394_7) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
if ( (tomMatch394_8 instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )tomMatch394_8) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.typeconstraints.types.Info  tom_info= tomMatch394_10.getInfo() ;
{
{
if ( (((Object)positiveTVarList) instanceof tom.engine.adt.tomtype.types.TomTypeList) ) {
if ( (((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)positiveTVarList))) instanceof tom.engine.adt.tomtype.types.tomtypelist.ConsconcTomType) || ((( tom.engine.adt.tomtype.types.TomTypeList )(( tom.engine.adt.tomtype.types.TomTypeList )((Object)positiveTVarList))) instanceof tom.engine.adt.tomtype.types.tomtypelist.EmptyconcTomType)) ) {
 tom.engine.adt.tomtype.types.TomTypeList  tomMatch395__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)positiveTVarList));
do {
{
if (!( tomMatch395__end__4.isEmptyconcTomType() )) {
 tom.engine.adt.tomtype.types.TomType  tom_tVar= tomMatch395__end__4.getHeadconcTomType() ;
if ( (tomMatch394_7==tom_tVar) ) {
printErrorGuessMatch(tom_info);
newtCList =

 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}
if ( (tomMatch394_8==tom_tVar) ) {
printErrorGuessMatch(tom_info);
newtCList =

 tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint.make( tom.engine.adt.typeconstraints.types.typeconstraint.FalseTypeConstraint.make() ,tom_append_list_concTypeConstraint(newtCList, tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() )) ;


}

}
if ( tomMatch395__end__4.isEmptyconcTomType() ) {
tomMatch395__end__4=(( tom.engine.adt.tomtype.types.TomTypeList )((Object)positiveTVarList));
} else {
tomMatch395__end__4= tomMatch395__end__4.getTailconcTomType() ;
}

}
} while(!( (tomMatch395__end__4==(( tom.engine.adt.tomtype.types.TomTypeList )((Object)positiveTVarList))) ));
}
}

}

}



}
}
}
}
}
}
}
if ( tomMatch394__end__4.isEmptyconcTypeConstraint() ) {
tomMatch394__end__4=(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList));
} else {
tomMatch394__end__4= tomMatch394__end__4.getTailconcTypeConstraint() ;
}

}
} while(!( (tomMatch394__end__4==(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) ));
}
}

}

}

return newtCList;
}

private void printErrorGuessMatch(Info info) {

{
{
if ( (((Object)info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )((Object)info)) instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )((Object)info))) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch396_1= (( tom.engine.adt.typeconstraints.types.Info )((Object)info)).getAstName() ;
if ( (tomMatch396_1 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch396_1) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.typeconstraints.types.Info )((Object)info)).getOptions() );

{
{
if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
TomMessage.cannotGuessMatchType,
 tomMatch396_1.getString() ); 


}
}
}

}

}



}
}
}
}
}

}

}

}

/**
* The method <code>isTypeVar</code> checks if a given type is a type variable.
* @param type the type to be checked
* @return     'true' if teh type is a variable type
*             'false' otherwise
*/
private boolean isTypeVar(TomType type) {

{
{
if ( (((Object)type) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)type)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)type))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
return true; 
}
}
}

}

}

return false;
}

/**
* The method <code>printErrorIncompatibility</code> prints an 'incompatible types' message
* enriched by informations about a given type constraint.
* @param tConstraint  the type constraint to be printed
*/
private void printErrorIncompatibility(TypeConstraint tConstraint) {

{
{
if ( (((Object)tConstraint) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
boolean tomMatch399_23= false ;
 tom.engine.adt.typeconstraints.types.Info  tomMatch399_6= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch399_8= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch399_4= null ;
 tom.engine.adt.tomtype.types.TomType  tomMatch399_5= null ;
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch399_9= null ;
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
{
tomMatch399_23= true ;
tomMatch399_8=(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint));
tomMatch399_4= tomMatch399_8.getType1() ;
tomMatch399_5= tomMatch399_8.getType2() ;
tomMatch399_6= tomMatch399_8.getInfo() ;

}
} else {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint)) instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint))) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
{
tomMatch399_23= true ;
tomMatch399_9=(( tom.engine.adt.typeconstraints.types.TypeConstraint )((Object)tConstraint));
tomMatch399_4= tomMatch399_9.getType1() ;
tomMatch399_5= tomMatch399_9.getType2() ;
tomMatch399_6= tomMatch399_9.getInfo() ;

}
}
}
}
}
if (tomMatch399_23) {
 tom.engine.adt.tomtype.types.TomType  tom_tType1=tomMatch399_4;
 tom.engine.adt.tomtype.types.TomType  tom_tType2=tomMatch399_5;
 tom.engine.adt.typeconstraints.types.Info  tom_info=tomMatch399_6;
if ( (((Object)tom_tType1) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (((Object)tom_tType2) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2))) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {
if ( (((Object)tom_info) instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)) instanceof tom.engine.adt.typeconstraints.types.Info) ) {
if ( ((( tom.engine.adt.typeconstraints.types.Info )(( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info))) instanceof tom.engine.adt.typeconstraints.types.info.PairNameOptions) ) {
 tom.engine.adt.tomname.types.TomName  tomMatch399_16= (( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)).getAstName() ;
if ( (tomMatch399_16 instanceof tom.engine.adt.tomname.types.TomName) ) {
if ( ((( tom.engine.adt.tomname.types.TomName )tomMatch399_16) instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

Option option = TomBase.findOriginTracking(
 (( tom.engine.adt.typeconstraints.types.Info )((Object)tom_info)).getOptions() );

{
{
if ( (((Object)option) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )((Object)option)) instanceof tom.engine.adt.tomoption.types.Option) ) {
if ( ((( tom.engine.adt.tomoption.types.Option )(( tom.engine.adt.tomoption.types.Option )((Object)option))) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {

TomMessage.error(logger,
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getFileName() , 
 (( tom.engine.adt.tomoption.types.Option )((Object)option)).getLine() ,
TomMessage.incompatibleTypes,
 (( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType1)).getTomType() ,
 (( tom.engine.adt.tomtype.types.TomType )((Object)tom_tType2)).getTomType() ,
 tomMatch399_16.getString() ); 


}
}
}

}

}



}
}
}
}
}
}
}
}
}
}
}
}

}

}

}

}

/**
* The method <code>replaceInCode</code> calls the strategy
* <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
* a given Code.
* @param code the code to be replaced
* @return     the replaced code resulting
*/
private Code replaceInCode(Code code) {
Code replacedCode = code;
try {
replacedCode = 
tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(code);
//`InnermostId(checkTypeOfVariables(this)).visitLight(replacedCode);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("replaceInCode: failure on " +
replacedCode);
}
return replacedCode;
}

/**
* The method <code>replaceInSymbolTable</code> calls the strategy
* <code>replaceFreshTypeVar</code> to apply substitution on each type variable occuring in
* the Symbol Table.
*/
private void replaceInSymbolTable() {
for(String tomName:symbolTable.keySymbolIterable()) {
//DEBUG System.out.println("replaceInSymboltable() - tomName : " + tomName);
TomSymbol tSymbol = getSymbolFromName(tomName);
//DEBUG System.out.println("replaceInSymboltable() - tSymbol before strategy: "
//DEBUG     + tSymbol);
try {
tSymbol = 
tom_make_InnermostId(tom_make_replaceFreshTypeVar(this)).visitLight(tSymbol);
} catch(tom.library.sl.VisitFailure e) {
throw new TomRuntimeException("replaceInSymbolTable: failure on " +
tSymbol);
}
//DEBUG System.out.println("replaceInSymboltable() - tSymbol after strategy: "
//DEBUG     + tSymbol);
symbolTable.putSymbol(tomName,tSymbol);
}
}

/**
* The class <code>replaceFreshTypeVar</code> is generated from a strategy
* which replace each type variable occurring in a given expression by its corresponding substitution. 
* @param nkt an instance of object NewKernelTyper
* @return    the expression resulting of a transformation
*/

public static class replaceFreshTypeVar extends tom.library.sl.AbstractStrategyBasic {
private  NewKernelTyper  nkt;
public replaceFreshTypeVar( NewKernelTyper  nkt) {
super(( new tom.library.sl.Identity() ));
this.nkt=nkt;
}
public  NewKernelTyper  getnkt() {
return nkt;
}
public tom.library.sl.Visitable[] getChildren() {
tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];
stratChilds[0] = super.getChildAt(0);
return stratChilds;}
public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {
super.setChildAt(0, children[0]);
return this;
}
public int getChildCount() {
return 1;
}
public tom.library.sl.Visitable getChildAt(int index) {
switch (index) {
case 0: return super.getChildAt(0);
default: throw new IndexOutOfBoundsException();
}
}
public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {
switch (index) {
case 0: return super.setChildAt(0, child);
default: throw new IndexOutOfBoundsException();
}
}
@SuppressWarnings("unchecked")
public <T> T visitLight(T v, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {
return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));
}
if (!(( null  == environment))) {
return ((T)any.visit(environment,introspector));
} else {
return any.visitLight(v,introspector);
}

}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
if (!(( null  == environment))) {
return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));
} else {
return any.visitLight(arg,introspector);
}
}
@SuppressWarnings("unchecked")
public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector)
 throws tom.library.sl.VisitFailure {
{
{
if ( (((Object)tom__arg) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg)) instanceof tom.engine.adt.tomtype.types.TomType) ) {
if ( ((( tom.engine.adt.tomtype.types.TomType )(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg))) instanceof tom.engine.adt.tomtype.types.tomtype.TypeVar) ) {
 tom.engine.adt.tomtype.types.TomType  tom_typeVar=(( tom.engine.adt.tomtype.types.TomType )((Object)tom__arg));

if (nkt.substitutions.containsKey(
tom_typeVar)) {
return nkt.substitutions.get(
tom_typeVar);
} 


}
}
}

}

}
return _visit_TomType(tom__arg,introspector);

}
}
private static  tom.library.sl.Strategy  tom_make_replaceFreshTypeVar( NewKernelTyper  t0) { 
return new replaceFreshTypeVar(t0);
}


/**
* The class <code>checkTypeOfVariables</code> is generated from a strategy
* which checks if all cast type (in a MatchConstraint) have been inferred. In
* the negative case, a 'canotGuessMatchType' message error is printed.
* @param nkt an instance of object NewKernelTyper
*/
/*
%strategy checkTypeOfVariables(nkt:NewKernelTyper) extends Identity() {
visit Constraint {
MatchConstraint[Pattern=Variable[Options=oList,AstName=Name(name),AstType=TypeVar(_,_)],Subject=BQVariable[]] -> {
Option option = TomBase.findOriginTracking(`oList);
%match(option) {
OriginTracking(_,line,fileName) -> {
TomMessage.error(logger,`fileName, `line,
TomMessage.cannotGuessMatchType,`name); 
}
}
}
}
}*/

/**
* The method <code>printGeneratedConstraints</code> prints braces and calls the method
* <code>printEachConstraint</code> for a given list.
* @param tCList the type constraint list to be printed
*/
public void printGeneratedConstraints(TypeConstraintList tCList) {

{
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
boolean tomMatch402_2= false ;
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if ( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() ) {
tomMatch402_2= true ;
}
}
if (!(tomMatch402_2)) {

System.out.print("\n------ Type Constraints : \n {");
printEachConstraint(tCList);
System.out.print("}");

}

}

}

}

}

/**
* The method <code>printEachConstraint</code> prints symbols '=' and '<:' and calls the method
* <code>printType</code> for each type occurring in a given type constraint.
* @param tCList the type constraint list to be printed
*/
public void printEachConstraint(TypeConstraintList tCList) {

{
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch403_7= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getHeadconcTypeConstraint() ;
if ( (tomMatch403_7 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch403_7) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Equation) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getTailconcTypeConstraint() ;

printType(
 tomMatch403_7.getType1() );
System.out.print(" = ");
printType(
 tomMatch403_7.getType2() );
if (
tom_tailtCList!= 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
System.out.print(", "); 
printEachConstraint(
tom_tailtCList);
}


}
}
}
}
}

}
{
if ( (((Object)tCList) instanceof tom.engine.adt.typeconstraints.types.TypeConstraintList) ) {
if ( (((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.ConsconcTypeConstraint) || ((( tom.engine.adt.typeconstraints.types.TypeConstraintList )(( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList))) instanceof tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint)) ) {
if (!( (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).isEmptyconcTypeConstraint() )) {
 tom.engine.adt.typeconstraints.types.TypeConstraint  tomMatch403_16= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getHeadconcTypeConstraint() ;
if ( (tomMatch403_16 instanceof tom.engine.adt.typeconstraints.types.TypeConstraint) ) {
if ( ((( tom.engine.adt.typeconstraints.types.TypeConstraint )tomMatch403_16) instanceof tom.engine.adt.typeconstraints.types.typeconstraint.Subtype) ) {
 tom.engine.adt.typeconstraints.types.TypeConstraintList  tom_tailtCList= (( tom.engine.adt.typeconstraints.types.TypeConstraintList )((Object)tCList)).getTailconcTypeConstraint() ;

printType(
 tomMatch403_16.getType1() );
System.out.print(" <: ");
printType(
 tomMatch403_16.getType2() );
if (
tom_tailtCList!= 
 tom.engine.adt.typeconstraints.types.typeconstraintlist.EmptyconcTypeConstraint.make() ) {
System.out.print(", "); 
printEachConstraint(
tom_tailtCList);
}


}
}
}
}
}

}


}

}

/**
* The method <code>printType</code> prints a given type.
* @param tCList the type to be printed
*/   
public void printType(TomType type) {
System.out.print(type);
}
} // NewKernelTyper
