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

package tom.engine.typer;

import java.util.*;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import tom.engine.TomBase;
import tom.engine.TomMessage;
import tom.engine.exception.TomRuntimeException;

import tom.engine.adt.tomsignature.*;
import tom.engine.adt.tomconstraint.types.*;
import tom.engine.adt.tomconstraint.types.constraint.*;
import tom.engine.adt.tomdeclaration.types.*;
import tom.engine.adt.tomexpression.types.*;
import tom.engine.adt.tominstruction.types.*;
import tom.engine.adt.tomname.types.*;
import tom.engine.adt.tomoption.types.*;
import tom.engine.adt.tomsignature.types.*;
import tom.engine.adt.tomterm.types.*;
import tom.engine.adt.tomslot.types.*;
import tom.engine.adt.tomtype.types.*;

import tom.engine.tools.SymbolTable;
import tom.engine.tools.ASTFactory;

import tom.library.sl.*;

public class KernelTyper {
          private static   tom.library.sl.Strategy  tom_append_list_Sequence( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 == null )) {       return l2;     } else if(( l2 == null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Sequence) )) {       if(( ((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )) == null )) {         return ( (l2==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),l2) );       } else {         return ( (tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)==null)?((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1):new tom.library.sl.Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.FIRST) ):l1),tom_append_list_Sequence(((( (l1 instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Sequence(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Sequence( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end == null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin):new tom.library.sl.Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Sequence(((( (begin instanceof tom.library.sl.Sequence) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Sequence.THEN) ):( null )),end,tail)) );   }      private static   tom.library.sl.Strategy  tom_append_list_Choice( tom.library.sl.Strategy  l1,  tom.library.sl.Strategy  l2) {     if(( l1 ==null )) {       return l2;     } else if(( l2 ==null )) {       return l1;     } else if(( (l1 instanceof tom.library.sl.Choice) )) {       if(( ((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ):( null )) ==null )) {         return ( (l2==null)?((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):l1):new tom.library.sl.Choice(((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):l1),l2) );       } else {         return ( (tom_append_list_Choice(((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ):( null )),l2)==null)?((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):l1):new tom.library.sl.Choice(((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.FIRST) ):l1),tom_append_list_Choice(((( (l1 instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)l1.getChildAt(tom.library.sl.Choice.THEN) ):( null )),l2)) );       }     } else {       return ( (l2==null)?l1:new tom.library.sl.Choice(l1,l2) );     }   }   private static   tom.library.sl.Strategy  tom_get_slice_Choice( tom.library.sl.Strategy  begin,  tom.library.sl.Strategy  end, tom.library.sl.Strategy  tail) {     if( (begin.equals(end)) ) {       return tail;     } else if( (end.equals(tail))  && (( end ==null ) ||  (end.equals(( null ))) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return ( (( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)==null)?((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin):new tom.library.sl.Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.FIRST) ):begin),( tom.library.sl.Strategy )tom_get_slice_Choice(((( (begin instanceof tom.library.sl.Choice) ))?( (tom.library.sl.Strategy)begin.getChildAt(tom.library.sl.Choice.THEN) ):( null )),end,tail)) );   }     private static  tom.library.sl.Strategy  tom_make_Try( tom.library.sl.Strategy  v) { return ( ( (( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.Identity() ):new tom.library.sl.Choice(( new tom.library.sl.Identity() ),( null )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDown( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) )) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownCollect( tom.library.sl.Strategy  v) { return ( ( new tom.library.sl.Mu(( new tom.library.sl.MuVar("_x") ),tom_make_Try(( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )==null)?v:new tom.library.sl.Sequence(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ):new tom.library.sl.Sequence(( new tom.library.sl.All(( new tom.library.sl.MuVar("_x") )) ),( null )) )) ))) ) );}private static  tom.library.sl.Strategy  tom_make_TopDownStopOnSuccess( tom.library.sl.Strategy  v) { return  (( new tom.library.sl.Mu(( new tom.library.sl.MuVar("x") ),( (( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )==null)?v:new tom.library.sl.Choice(v,( (( null )==null)?( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ):new tom.library.sl.Choice(( new tom.library.sl.All(( new tom.library.sl.MuVar("x") )) ),( null )) )) )) )) ;}    







  private SymbolTable symbolTable;

  public KernelTyper() {
    super();
  }

  public void setSymbolTable(SymbolTable symbolTable) {
    this.symbolTable = symbolTable;
  }

  private SymbolTable getSymbolTable() {
    return symbolTable;
  }

  protected TomSymbol getSymbolFromName(String tomName) {
    return TomBase.getSymbolFromName(tomName, getSymbolTable());
  }

  protected TomSymbol getSymbolFromType(TomType type) {
    {{if ( (type instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )type) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {

        return TomBase.getSymbolFromType( tom.engine.adt.tomtype.types.tomtype.Type.make( (( tom.engine.adt.tomtype.types.TomType )type).getTomType() ,  (( tom.engine.adt.tomtype.types.TomType )type).getTlType() ) , getSymbolTable()); 
      }}}}

    return TomBase.getSymbolFromType(type, getSymbolTable()); 
  }
  // ------------------------------------------------------------
     private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_append_list_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList l1,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  l2) {     if( l1.isEmptyconcConstraintInstruction() ) {       return l2;     } else if( l2.isEmptyconcConstraintInstruction() ) {       return l1;     } else if(  l1.getTailconcConstraintInstruction() .isEmptyconcConstraintInstruction() ) {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,l2) ;     } else {       return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( l1.getHeadconcConstraintInstruction() ,tom_append_list_concConstraintInstruction( l1.getTailconcConstraintInstruction() ,l2)) ;     }   }   private static   tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_get_slice_concConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstructionList  begin,  tom.engine.adt.tominstruction.types.ConstraintInstructionList  end, tom.engine.adt.tominstruction.types.ConstraintInstructionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraintInstruction()  ||  (end== tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( begin.getHeadconcConstraintInstruction() ,( tom.engine.adt.tominstruction.types.ConstraintInstructionList )tom_get_slice_concConstraintInstruction( begin.getTailconcConstraintInstruction() ,end,tail)) ;   }      private static   tom.engine.adt.tomname.types.TomNameList  tom_append_list_concTomName( tom.engine.adt.tomname.types.TomNameList l1,  tom.engine.adt.tomname.types.TomNameList  l2) {     if( l1.isEmptyconcTomName() ) {       return l2;     } else if( l2.isEmptyconcTomName() ) {       return l1;     } else if(  l1.getTailconcTomName() .isEmptyconcTomName() ) {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,l2) ;     } else {       return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( l1.getHeadconcTomName() ,tom_append_list_concTomName( l1.getTailconcTomName() ,l2)) ;     }   }   private static   tom.engine.adt.tomname.types.TomNameList  tom_get_slice_concTomName( tom.engine.adt.tomname.types.TomNameList  begin,  tom.engine.adt.tomname.types.TomNameList  end, tom.engine.adt.tomname.types.TomNameList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomName()  ||  (end== tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make( begin.getHeadconcTomName() ,( tom.engine.adt.tomname.types.TomNameList )tom_get_slice_concTomName( begin.getTailconcTomName() ,end,tail)) ;   }      private static   tom.engine.adt.tomterm.types.TomList  tom_append_list_concTomTerm( tom.engine.adt.tomterm.types.TomList l1,  tom.engine.adt.tomterm.types.TomList  l2) {     if( l1.isEmptyconcTomTerm() ) {       return l2;     } else if( l2.isEmptyconcTomTerm() ) {       return l1;     } else if(  l1.getTailconcTomTerm() .isEmptyconcTomTerm() ) {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,l2) ;     } else {       return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( l1.getHeadconcTomTerm() ,tom_append_list_concTomTerm( l1.getTailconcTomTerm() ,l2)) ;     }   }   private static   tom.engine.adt.tomterm.types.TomList  tom_get_slice_concTomTerm( tom.engine.adt.tomterm.types.TomList  begin,  tom.engine.adt.tomterm.types.TomList  end, tom.engine.adt.tomterm.types.TomList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcTomTerm()  ||  (end== tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm.make( begin.getHeadconcTomTerm() ,( tom.engine.adt.tomterm.types.TomList )tom_get_slice_concTomTerm( begin.getTailconcTomTerm() ,end,tail)) ;   }      private static   tom.engine.adt.tomoption.types.OptionList  tom_append_list_concOption( tom.engine.adt.tomoption.types.OptionList l1,  tom.engine.adt.tomoption.types.OptionList  l2) {     if( l1.isEmptyconcOption() ) {       return l2;     } else if( l2.isEmptyconcOption() ) {       return l1;     } else if(  l1.getTailconcOption() .isEmptyconcOption() ) {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,l2) ;     } else {       return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( l1.getHeadconcOption() ,tom_append_list_concOption( l1.getTailconcOption() ,l2)) ;     }   }   private static   tom.engine.adt.tomoption.types.OptionList  tom_get_slice_concOption( tom.engine.adt.tomoption.types.OptionList  begin,  tom.engine.adt.tomoption.types.OptionList  end, tom.engine.adt.tomoption.types.OptionList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcOption()  ||  (end== tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomoption.types.optionlist.ConsconcOption.make( begin.getHeadconcOption() ,( tom.engine.adt.tomoption.types.OptionList )tom_get_slice_concOption( begin.getTailconcOption() ,end,tail)) ;   }      private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_append_list_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList l1,  tom.engine.adt.tomconstraint.types.ConstraintList  l2) {     if( l1.isEmptyconcConstraint() ) {       return l2;     } else if( l2.isEmptyconcConstraint() ) {       return l1;     } else if(  l1.getTailconcConstraint() .isEmptyconcConstraint() ) {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,l2) ;     } else {       return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( l1.getHeadconcConstraint() ,tom_append_list_concConstraint( l1.getTailconcConstraint() ,l2)) ;     }   }   private static   tom.engine.adt.tomconstraint.types.ConstraintList  tom_get_slice_concConstraint( tom.engine.adt.tomconstraint.types.ConstraintList  begin,  tom.engine.adt.tomconstraint.types.ConstraintList  end, tom.engine.adt.tomconstraint.types.ConstraintList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcConstraint()  ||  (end== tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomconstraint.types.constraintlist.ConsconcConstraint.make( begin.getHeadconcConstraint() ,( tom.engine.adt.tomconstraint.types.ConstraintList )tom_get_slice_concConstraint( begin.getTailconcConstraint() ,end,tail)) ;   }      private static   tom.engine.adt.tomslot.types.SlotList  tom_append_list_concSlot( tom.engine.adt.tomslot.types.SlotList l1,  tom.engine.adt.tomslot.types.SlotList  l2) {     if( l1.isEmptyconcSlot() ) {       return l2;     } else if( l2.isEmptyconcSlot() ) {       return l1;     } else if(  l1.getTailconcSlot() .isEmptyconcSlot() ) {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,l2) ;     } else {       return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( l1.getHeadconcSlot() ,tom_append_list_concSlot( l1.getTailconcSlot() ,l2)) ;     }   }   private static   tom.engine.adt.tomslot.types.SlotList  tom_get_slice_concSlot( tom.engine.adt.tomslot.types.SlotList  begin,  tom.engine.adt.tomslot.types.SlotList  end, tom.engine.adt.tomslot.types.SlotList  tail) {     if( (begin==end) ) {       return tail;     } else if( (end==tail)  && ( end.isEmptyconcSlot()  ||  (end== tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ) )) {       /* code to avoid a call to make, and thus to avoid looping during list-matching */       return begin;     }     return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( begin.getHeadconcSlot() ,( tom.engine.adt.tomslot.types.SlotList )tom_get_slice_concSlot( begin.getTailconcSlot() ,end,tail)) ;   }     

  // ------------------------------------------------------------

  /**
   * If a variable with a type X is found, then all the variables that have the same name and 
   * with type 'unknown' get this type
   *  - apply this for each rhs
   */
  protected TomTerm propagateVariablesTypes(TomTerm subject){
    try{
      return tom_make_TopDown(tom_make_ProcessRhsForVarTypePropagation()).visitLight(subject);  
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("propagateVariablesTypes: failure on " + subject);
    }
  }
  public static class ProcessRhsForVarTypePropagation extends tom.library.sl.BasicStrategy {public ProcessRhsForVarTypePropagation() {super(( new tom.library.sl.Identity() ));}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tominstruction.types.ConstraintInstruction  visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {if ( ((( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) { tom.engine.adt.tomconstraint.types.Constraint  tom_constr= (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getConstraint() ;


        HashMap<String,TomType> varTypes = new HashMap<String,TomType>();
        tom_make_TopDown(tom_make_CollectAllVariablesTypes(varTypes)).visitLight(tom_constr);        
        Constraint c = tom_make_TopDown(tom_make_PropagateVariablesTypes(varTypes)).visitLight(tom_constr);        
        return  tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(c,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getAction() ,  (( tom.engine.adt.tominstruction.types.ConstraintInstruction )tom__arg).getOption() ) ;
      }}}}return _visit_ConstraintInstruction(tom__arg,introspector); }public  tom.engine.adt.tominstruction.types.ConstraintInstruction  _visit_ConstraintInstruction( tom.engine.adt.tominstruction.types.ConstraintInstruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.ConstraintInstruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.ConstraintInstruction) ) {return ((T)visit_ConstraintInstruction((( tom.engine.adt.tominstruction.types.ConstraintInstruction )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_ProcessRhsForVarTypePropagation() { return new ProcessRhsForVarTypePropagation();}public static class CollectAllVariablesTypes extends tom.library.sl.BasicStrategy {private  java.util.HashMap  map;public CollectAllVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch248NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;if ( (tomMatch248NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;if ( (tom_type instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch248NameNumber_freshVar_8= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )tom_type) instanceof tom.engine.adt.tomtype.types.tomtype.EmptyType) ) {tomMatch248NameNumber_freshVar_8= true ;}if ((tomMatch248NameNumber_freshVar_8 ==  false )) {





        if(tom_type!=SymbolTable.TYPE_UNKNOWN) {
          map.put( tomMatch248NameNumber_freshVar_2.getString() ,tom_type);
        }
      }}}}}}}return _visit_TomTerm(tom__arg,introspector); }public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectAllVariablesTypes( java.util.HashMap  t0) { return new CollectAllVariablesTypes(t0);}public static class PropagateVariablesTypes extends tom.library.sl.BasicStrategy {private  java.util.HashMap  map;public PropagateVariablesTypes( java.util.HashMap  map) {super(( new tom.library.sl.Identity() ));this.map=map;}public  java.util.HashMap  getmap() {return map;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) { tom.engine.adt.tomname.types.TomName  tomMatch249NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstName() ;if ( (tomMatch249NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch249NameNumber_freshVar_1.getString() ; tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;





        if(tom_type==SymbolTable.TYPE_UNKNOWN || tom_type.isEmptyType()) {
          if (map.containsKey(tom_name)) {
            return (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).setAstType((TomType)map.get(tom_name)); 
          }
        }
      }}}}}return _visit_TomTerm(tom__arg,introspector); }public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_PropagateVariablesTypes( java.util.HashMap  t0) { return new PropagateVariablesTypes(t0);}


  

  /*
   * The "typeVariable" phase types RecordAppl into Variable
   * we focus on
   * - Match
   *
   * The types of subjects are inferred from the patterns
   *
   * Variable and TermAppl are typed in the TomTerm case
   */

  protected tom.library.sl.Visitable typeVariable(TomType contextType, tom.library.sl.Visitable subject) {
    if(contextType == null) {
      throw new TomRuntimeException("typeVariable: null contextType");
    }
    try {
      //System.out.println("typeVariable: " + contextType);
      //System.out.println("typeVariable subject: " + subject);
      tom.library.sl.Visitable res = tom_make_TopDownStopOnSuccess(tom_make_replace_typeVariable(contextType,this)).visitLight(subject);
      //System.out.println("res: " + res);
      return res;
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("typeVariable: failure on " + subject);
    }
  }

  public static class replace_typeVariable extends tom.library.sl.BasicStrategy {private  tom.engine.adt.tomtype.types.TomType  contextType;private  KernelTyper  kernelTyper;public replace_typeVariable( tom.engine.adt.tomtype.types.TomType  contextType,  KernelTyper  kernelTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.kernelTyper=kernelTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  KernelTyper  getkernelTyper() {return kernelTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomoption.types.Option  visit_Option( tom.engine.adt.tomoption.types.Option  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomoption.types.Option) ) {if ( ((( tom.engine.adt.tomoption.types.Option )tom__arg) instanceof tom.engine.adt.tomoption.types.option.OriginTracking) ) {


 return (( tom.engine.adt.tomoption.types.Option )tom__arg); }}}}return _visit_Option(tom__arg,introspector); }public  tom.engine.adt.tomsignature.types.TargetLanguage  visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.TL) ) {



 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.ITL) ) {
 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {if ( ((( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg) instanceof tom.engine.adt.tomsignature.types.targetlanguage.Comment) ) {
 return (( tom.engine.adt.tomsignature.types.TargetLanguage )tom__arg); }}}}return _visit_TargetLanguage(tom__arg,introspector); }public  tom.engine.adt.tomtype.types.TomType  visit_TomType( tom.engine.adt.tomtype.types.TomType  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomtype.types.TomType) ) {if ( ((( tom.engine.adt.tomtype.types.TomType )tom__arg) instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) {




        TomType type = kernelTyper.getType( (( tom.engine.adt.tomtype.types.TomType )tom__arg).getString() );
        if(type != null) {
          return type;
        } else {
          return (( tom.engine.adt.tomtype.types.TomType )tom__arg); // useful for SymbolTable.TYPE_UNKNOWN
        }
      }}}}return _visit_TomType(tom__arg,introspector); }public  tom.engine.adt.tomsignature.types.TomVisit  visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {if ( ((( tom.engine.adt.tomsignature.types.TomVisit )tom__arg) instanceof tom.engine.adt.tomsignature.types.tomvisit.VisitTerm) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getAstConstraintInstructionList() ;




        TomType newType = (TomType)kernelTyper.typeVariable(contextType, (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getVNode() );
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        tom_make_TopDownCollect(tom_make_CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(tom_constraintInstructionList);
        return  tom.engine.adt.tomsignature.types.tomvisit.VisitTerm.make(newType, kernelTyper.typeConstraintInstructionList(newType,tom_constraintInstructionList,matchAndNumericConstraints),  (( tom.engine.adt.tomsignature.types.TomVisit )tom__arg).getOption() ) ;
      }}}}return _visit_TomVisit(tom__arg,introspector); }public  tom.engine.adt.tominstruction.types.Instruction  visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tominstruction.types.Instruction) ) {if ( ((( tom.engine.adt.tominstruction.types.Instruction )tom__arg) instanceof tom.engine.adt.tominstruction.types.instruction.Match) ) { tom.engine.adt.tominstruction.types.ConstraintInstructionList  tom_constraintInstructionList= (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getConstraintInstructionList() ;









        TomType newType = contextType;
        HashSet<Constraint> matchAndNumericConstraints = new HashSet<Constraint>();
        tom_make_TopDownCollect(tom_make_CollectMatchAndNumericConstraints(matchAndNumericConstraints)).visitLight(tom_constraintInstructionList);
        return  tom.engine.adt.tominstruction.types.instruction.Match.make(kernelTyper.typeConstraintInstructionList(newType,tom_constraintInstructionList,matchAndNumericConstraints),  (( tom.engine.adt.tominstruction.types.Instruction )tom__arg).getOption() ) ;
      }}}}return _visit_Instruction(tom__arg,introspector); }public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch255NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getNameList() ; tom.engine.adt.tomoption.types.OptionList  tom_option= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getOption() ;if ( ((tomMatch255NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch255NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) { tom.engine.adt.tomname.types.TomNameList  tom_nameList=tomMatch255NameNumber_freshVar_2;if (!( tomMatch255NameNumber_freshVar_2.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch255NameNumber_freshVar_10= tomMatch255NameNumber_freshVar_2.getHeadconcTomName() ;if ( (tomMatch255NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_tomName= tomMatch255NameNumber_freshVar_10.getString() ; tom.engine.adt.tomslot.types.SlotList  tom_slotList= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getSlots() ; tom.engine.adt.tomconstraint.types.ConstraintList  tom_constraints= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;




        TomSymbol tomSymbol = null;
        if(tom_tomName.equals("")) {
          try {
            tomSymbol = kernelTyper.getSymbolFromType(contextType);
            if(tomSymbol==null) {
              throw new TomRuntimeException("No symbol found for type '" + contextType + "'");
            }
            tom_nameList=  tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName.make(tomSymbol.getAstName(), tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName.make() ) ;
          } catch(UnsupportedOperationException e) {
            // contextType has no AstType slot
            tomSymbol = null;
          }
        } else {
          tomSymbol = kernelTyper.getSymbolFromName(tom_tomName);
        }

        if(tomSymbol != null) {
          SlotList subterm = kernelTyper.typeVariableList(tomSymbol, tom_slotList);
          ConstraintList newConstraints = (ConstraintList)kernelTyper.typeVariable(TomBase.getSymbolCodomain(tomSymbol),tom_constraints);
          return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) ;
        } else {
          //System.out.println("contextType = " + contextType);

          {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch256NameNumber_freshVar_2= false ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {tomMatch256NameNumber_freshVar_2= true ;} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {tomMatch256NameNumber_freshVar_2= true ;}}if ((tomMatch256NameNumber_freshVar_2 ==  true )) {

              SlotList subterm = kernelTyper.typeVariableList( tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol.make() , tom_slotList);
              ConstraintList newConstraints = (ConstraintList)kernelTyper.typeVariable((( tom.engine.adt.tomtype.types.TomType )contextType),tom_constraints);
              return  tom.engine.adt.tomterm.types.tomterm.RecordAppl.make(tom_option, tom_nameList, subterm, newConstraints) ;
            }}}{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {


              // do nothing
              //System.out.println("contextType = " + contextType);
              //System.out.println("subject        = " + subject);
            }}}

        }
      }}}}}}{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch255NameNumber_freshVar_17= false ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch255NameNumber_freshVar_13= null ; tom.engine.adt.tomtype.types.TomType  tomMatch255NameNumber_freshVar_12= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch255NameNumber_freshVar_17= true ;tomMatch255NameNumber_freshVar_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;tomMatch255NameNumber_freshVar_13= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom__arg) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariable) ) {{tomMatch255NameNumber_freshVar_17= true ;tomMatch255NameNumber_freshVar_12= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getAstType() ;tomMatch255NameNumber_freshVar_13= (( tom.engine.adt.tomterm.types.TomTerm )tom__arg).getConstraints() ;}}}if ((tomMatch255NameNumber_freshVar_17 ==  true )) {if ( (tomMatch255NameNumber_freshVar_12 instanceof tom.engine.adt.tomtype.types.tomtype.TomTypeAlone) ) { tom.engine.adt.tomterm.types.TomTerm  tom_var=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);


        TomType localType = kernelTyper.getType( tomMatch255NameNumber_freshVar_12.getString() );
        //System.out.println("localType = " + localType);
        if(localType != null) {
          // The variable has already a known type
          return tom_var.setAstType(localType);
        }

        //System.out.println("contextType = " + contextType);
        {{if ( (contextType instanceof tom.engine.adt.tomtype.types.TomType) ) {boolean tomMatch257NameNumber_freshVar_4= false ; tom.engine.adt.tomtype.types.TomType  tomMatch257NameNumber_freshVar_2= null ; tom.engine.adt.tomtype.types.TomType  tomMatch257NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) {{tomMatch257NameNumber_freshVar_4= true ;tomMatch257NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch257NameNumber_freshVar_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}} else {if ( ((( tom.engine.adt.tomtype.types.TomType )contextType) instanceof tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol) ) {{tomMatch257NameNumber_freshVar_4= true ;tomMatch257NameNumber_freshVar_1= (( tom.engine.adt.tomtype.types.TomType )contextType).getTomType() ;tomMatch257NameNumber_freshVar_2= (( tom.engine.adt.tomtype.types.TomType )contextType).getTlType() ;}}}if ((tomMatch257NameNumber_freshVar_4 ==  true )) {

            TomType ctype =  tom.engine.adt.tomtype.types.tomtype.Type.make(tomMatch257NameNumber_freshVar_1, tomMatch257NameNumber_freshVar_2) ;
            ConstraintList newConstraints = (ConstraintList)kernelTyper.typeVariable(ctype,tomMatch255NameNumber_freshVar_13);
            TomTerm newVar = tom_var.setAstType(ctype);
            //System.out.println("newVar = " + newVar);
            return newVar.setConstraints(newConstraints);
          }}}}

      }}}}}return _visit_TomTerm(tom__arg,introspector); }public  tom.engine.adt.tominstruction.types.Instruction  _visit_Instruction( tom.engine.adt.tominstruction.types.Instruction  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tominstruction.types.Instruction )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.engine.adt.tomoption.types.Option  _visit_Option( tom.engine.adt.tomoption.types.Option  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomoption.types.Option )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.engine.adt.tomsignature.types.TomVisit  _visit_TomVisit( tom.engine.adt.tomsignature.types.TomVisit  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomsignature.types.TomVisit )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.engine.adt.tomtype.types.TomType  _visit_TomType( tom.engine.adt.tomtype.types.TomType  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomtype.types.TomType )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.engine.adt.tomsignature.types.TargetLanguage  _visit_TargetLanguage( tom.engine.adt.tomsignature.types.TargetLanguage  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomsignature.types.TargetLanguage )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tominstruction.types.Instruction) ) {return ((T)visit_Instruction((( tom.engine.adt.tominstruction.types.Instruction )v),introspector));}if ( (v instanceof tom.engine.adt.tomoption.types.Option) ) {return ((T)visit_Option((( tom.engine.adt.tomoption.types.Option )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TomVisit) ) {return ((T)visit_TomVisit((( tom.engine.adt.tomsignature.types.TomVisit )v),introspector));}if ( (v instanceof tom.engine.adt.tomtype.types.TomType) ) {return ((T)visit_TomType((( tom.engine.adt.tomtype.types.TomType )v),introspector));}if ( (v instanceof tom.engine.adt.tomsignature.types.TargetLanguage) ) {return ((T)visit_TargetLanguage((( tom.engine.adt.tomsignature.types.TargetLanguage )v),introspector));}if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replace_typeVariable( tom.engine.adt.tomtype.types.TomType  t0,  KernelTyper  t1) { return new replace_typeVariable(t0,t1);}



  /*
   *
   * @param contextType
   * @param constraintInstructionList a list of ConstraintInstruction
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   */
  private ConstraintInstructionList typeConstraintInstructionList(TomType contextType, ConstraintInstructionList constraintInstructionList, Collection<Constraint> matchAndNumericConstraints) {
    {{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if ( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() ) {

        return constraintInstructionList; 
      }}}}{if ( (constraintInstructionList instanceof tom.engine.adt.tominstruction.types.ConstraintInstructionList) ) {if ( (((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction) || ((( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList) instanceof tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction)) ) {if (!( (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).isEmptyconcConstraintInstruction() )) { tom.engine.adt.tominstruction.types.ConstraintInstruction  tomMatch258NameNumber_freshVar_9= (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getHeadconcConstraintInstruction() ;if ( (tomMatch258NameNumber_freshVar_9 instanceof tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction) ) {

 
        try {
          Collection<TomTerm> lhsVariable = new HashSet<TomTerm>();
          Constraint newConstraint = tom_make_TopDownStopOnSuccess(tom_make_typeConstraint(contextType,lhsVariable,matchAndNumericConstraints,this)).visitLight( tomMatch258NameNumber_freshVar_9.getConstraint() );
          TomList varList = ASTFactory.makeList(lhsVariable);
          Instruction newAction = (Instruction) replaceInstantiatedVariable(varList, tomMatch258NameNumber_freshVar_9.getAction() );
          newAction = (Instruction) typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ,newAction);
          ConstraintInstructionList newTail = typeConstraintInstructionList(contextType, (( tom.engine.adt.tominstruction.types.ConstraintInstructionList )constraintInstructionList).getTailconcConstraintInstruction() ,matchAndNumericConstraints);
          return  tom.engine.adt.tominstruction.types.constraintinstructionlist.ConsconcConstraintInstruction.make( tom.engine.adt.tominstruction.types.constraintinstruction.ConstraintInstruction.make(newConstraint, newAction,  tomMatch258NameNumber_freshVar_9.getOption() ) ,tom_append_list_concConstraintInstruction(newTail, tom.engine.adt.tominstruction.types.constraintinstructionlist.EmptyconcConstraintInstruction.make() )) ;
        } catch(VisitFailure e) {}
      }}}}}}
        
    throw new TomRuntimeException("Bad ConstraintInstruction: " + constraintInstructionList);
  }

  /**
   * Try to guess the type for the subjects
   * @param contextType the context in which the constraint is typed
   * @param lhsVariable (computed by this strategy) the list of variables that occur in all the lhs 
   * @param matchAndNumericConstraints a collection of MatchConstraint and NumericConstraint
   * @param kernelTyper the current class
   */
  public static class typeConstraint extends tom.library.sl.BasicStrategy {private  tom.engine.adt.tomtype.types.TomType  contextType;private  java.util.Collection  lhsVariable;private  java.util.Collection  matchAndNumericConstraints;private  KernelTyper  kernelTyper;public typeConstraint( tom.engine.adt.tomtype.types.TomType  contextType,  java.util.Collection  lhsVariable,  java.util.Collection  matchAndNumericConstraints,  KernelTyper  kernelTyper) {super(( new tom.library.sl.Fail() ));this.contextType=contextType;this.lhsVariable=lhsVariable;this.matchAndNumericConstraints=matchAndNumericConstraints;this.kernelTyper=kernelTyper;}public  tom.engine.adt.tomtype.types.TomType  getcontextType() {return contextType;}public  java.util.Collection  getlhsVariable() {return lhsVariable;}public  java.util.Collection  getmatchAndNumericConstraints() {return matchAndNumericConstraints;}public  KernelTyper  getkernelTyper() {return kernelTyper;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch259NameNumber_freshVar_4= false ; tom.engine.adt.tomterm.types.TomTerm  tomMatch259NameNumber_freshVar_2= null ; tom.engine.adt.tomterm.types.TomTerm  tomMatch259NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {{tomMatch259NameNumber_freshVar_4= true ;tomMatch259NameNumber_freshVar_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;tomMatch259NameNumber_freshVar_2= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ;}} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {{tomMatch259NameNumber_freshVar_4= true ;tomMatch259NameNumber_freshVar_1= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getPattern() ;tomMatch259NameNumber_freshVar_2= (( tom.engine.adt.tomconstraint.types.Constraint )tom__arg).getSubject() ;}}}if ((tomMatch259NameNumber_freshVar_4 ==  true )) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern=tomMatch259NameNumber_freshVar_1; tom.engine.adt.tomterm.types.TomTerm  tom_subject=tomMatch259NameNumber_freshVar_2; tom.engine.adt.tomconstraint.types.Constraint  tom_constraint=(( tom.engine.adt.tomconstraint.types.Constraint )tom__arg);


        boolean isNumeric = (tom_constraint) instanceof NumericConstraint ? true:false;
        TomTerm newSubject = null;
        TomType newSubjectType = null;        
        {{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch260NameNumber_freshVar_8= false ; tom.engine.adt.tomname.types.TomName  tomMatch260NameNumber_freshVar_2= null ; tom.engine.adt.tomconstraint.types.ConstraintList  tomMatch260NameNumber_freshVar_4= null ; tom.engine.adt.tomoption.types.OptionList  tomMatch260NameNumber_freshVar_1= null ; tom.engine.adt.tomtype.types.TomType  tomMatch260NameNumber_freshVar_3= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch260NameNumber_freshVar_8= true ;tomMatch260NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getOption() ;tomMatch260NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ;tomMatch260NameNumber_freshVar_3= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstType() ;tomMatch260NameNumber_freshVar_4= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getConstraints() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch260NameNumber_freshVar_8= true ;tomMatch260NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getOption() ;tomMatch260NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ;tomMatch260NameNumber_freshVar_3= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstType() ;tomMatch260NameNumber_freshVar_4= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getConstraints() ;}}}if ((tomMatch260NameNumber_freshVar_8 ==  true )) {if ( (tomMatch260NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {

            TomTerm newVariable = null;
            // tomType may be a TomTypeAlone or a type from an typed variable
            String type = TomBase.getTomType(tomMatch260NameNumber_freshVar_3);
            //System.out.println("match type = " + type);
            if(kernelTyper.getType(type) == null) {
              /* the subject is a variable with an unknown type */
              newSubjectType = kernelTyper.guessSubjectType(tom_subject,matchAndNumericConstraints);
              if(newSubjectType != null) {
                newVariable =  tom.engine.adt.tomterm.types.tomterm.Variable.make(tomMatch260NameNumber_freshVar_1, tomMatch260NameNumber_freshVar_2, newSubjectType, tomMatch260NameNumber_freshVar_4) ;
              } else {
                if (!isNumeric) {
                  throw new TomRuntimeException("No symbol found for name '" +  tomMatch260NameNumber_freshVar_2.getString() + "'");
                }
              }
            } else {
              newVariable = tom_subject;
            }
            if(newVariable == null) {
              if (!isNumeric) { 
                throw new TomRuntimeException("Type cannot be guessed for '" + tom_subject+ "'");
              }
            } else {
              newSubject = newVariable;
              newSubjectType = newVariable.getAstType();
            }                  
          }}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch260NameNumber_freshVar_17= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch260NameNumber_freshVar_10= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch260NameNumber_freshVar_17= true ;tomMatch260NameNumber_freshVar_10= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch260NameNumber_freshVar_17= true ;tomMatch260NameNumber_freshVar_10= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ;}}}if ((tomMatch260NameNumber_freshVar_17 ==  true )) {if ( ((tomMatch260NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch260NameNumber_freshVar_10 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch260NameNumber_freshVar_10.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch260NameNumber_freshVar_16= tomMatch260NameNumber_freshVar_10.getHeadconcTomName() ;if ( (tomMatch260NameNumber_freshVar_16 instanceof tom.engine.adt.tomname.types.tomname.Name) ) { String  tom_name= tomMatch260NameNumber_freshVar_16.getString() ;


            TomSymbol symbol = kernelTyper.getSymbolFromName(tom_name);
            TomType type = null;
            if(symbol!=null) {
              type = TomBase.getSymbolCodomain(symbol);
            } else {
              // unknown function call
              type = kernelTyper.guessSubjectType(tom_subject,matchAndNumericConstraints);
            }
            if(type != null) {
              newSubject =  tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm.make((( tom.engine.adt.tomterm.types.TomTerm )tom_subject), type) ;
            } else {
              if (!isNumeric) {
                throw new TomRuntimeException("No symbol found for name '" + tom_name+ "'");
              }
            }
            newSubjectType = type;                    
          }}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.TomTypeToTomTerm) ) { tom.engine.adt.tomtype.types.TomType  tom_type= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstType() ;


            newSubject =  tom.engine.adt.tomterm.types.tomterm.Variable.make( tom.engine.adt.tomoption.types.optionlist.EmptyconcOption.make() ,  tom.engine.adt.tomname.types.tomname.Name.make("tom__arg") , tom_type,  tom.engine.adt.tomconstraint.types.constraintlist.EmptyconcConstraint.make() ) ;
            newSubjectType = tom_type;
          }}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) {


            
            newSubjectType =  (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstType() ;
            newSubject = (( tom.engine.adt.tomterm.types.TomTerm )tom_subject);
          }}}}

 // end match subject     
        // if it is numeric, we do not care about the type
        // we transform the lhs and rhs into buildTerms with empty type
        if (isNumeric) {
          newSubjectType =  tom.engine.adt.tomtype.types.tomtype.EmptyType.make() ;
          newSubject = tom_subject;
          {{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {
 newSubject =  tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm.make(tom_subject, newSubjectType) ;}}}}{{if ( (tom_pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_pattern) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {tom_pattern


=  tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm.make(tom_pattern, newSubjectType) ; }}}}

        } else {
          newSubjectType = (TomType)kernelTyper.typeVariable(contextType,newSubjectType);
          newSubject = (TomTerm)kernelTyper.typeVariable(newSubjectType, newSubject);                  
        }
        TomTerm newPattern = (TomTerm)kernelTyper.typeVariable(newSubjectType, tom_pattern);
        TomBase.collectVariable(lhsVariable,newPattern);
        return tom_constraint.setPattern(newPattern).setSubject(newSubject);               
      }}}}return _visit_Constraint(tom__arg,introspector); }public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_typeConstraint( tom.engine.adt.tomtype.types.TomType  t0,  java.util.Collection  t1,  java.util.Collection  t2,  KernelTyper  t3) { return new typeConstraint(t0,t1,t2,t3);}



  private TomType guessSubjectType(TomTerm subject, Collection matchConstraints) {
    for(Object constr:matchConstraints) {
      {{if ( (constr instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )constr) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) { tom.engine.adt.tomterm.types.TomTerm  tom_pattern= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getPattern() ; tom.engine.adt.tomterm.types.TomTerm  tom_s= (( tom.engine.adt.tomconstraint.types.Constraint )constr).getSubject() ;

          // we want two terms to be equal even if their option is different 
          // ( because of their possition for example )
matchL:  {{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstName() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstType() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getArgs() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getArgs() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getSlots() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getSlots() ) ) {
break matchL;}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) { tom.engine.adt.tomterm.types.TomList  tom_tomList= (( tom.engine.adt.tomterm.types.TomTerm )subject).getAttrList() ;if ( ( (( tom.engine.adt.tomterm.types.TomTerm )subject).getChildList() ==tom_tomList) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getNameList() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getNameList() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAttrList() ==tom_tomList) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getChildList() ==tom_tomList) ) {
 break matchL; }}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )subject) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch264NameNumber_freshVar_46= (( tom.engine.adt.tomterm.types.TomTerm )subject).getTomTerm() ;if ( (tomMatch264NameNumber_freshVar_46 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_s) instanceof tom.engine.adt.tomterm.types.tomterm.BuildReducedTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tomMatch264NameNumber_freshVar_52= (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getTomTerm() ;if ( (tomMatch264NameNumber_freshVar_52 instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {if ( ( tomMatch264NameNumber_freshVar_52.getNameList() == tomMatch264NameNumber_freshVar_46.getNameList() ) ) {if ( ( tomMatch264NameNumber_freshVar_52.getArgs() == tomMatch264NameNumber_freshVar_46.getArgs() ) ) {if ( ( (( tom.engine.adt.tomterm.types.TomTerm )tom_s).getAstType() == (( tom.engine.adt.tomterm.types.TomTerm )subject).getAstType() ) ) {
break matchL;}}}}}}}}}}{if ( (subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( (tom_s instanceof tom.engine.adt.tomterm.types.TomTerm) ) {
 continue; }}}}

         TomTerm patt = tom_pattern;
         {{if ( (tom_pattern instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_pattern) instanceof tom.engine.adt.tomterm.types.tomterm.AntiTerm) ) {
 patt =  (( tom.engine.adt.tomterm.types.TomTerm )tom_pattern).getTomTerm() ; }}}}{{if ( (patt instanceof tom.engine.adt.tomterm.types.TomTerm) ) {boolean tomMatch266NameNumber_freshVar_8= false ; tom.engine.adt.tomname.types.TomNameList  tomMatch266NameNumber_freshVar_1= null ;if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.TermAppl) ) {{tomMatch266NameNumber_freshVar_8= true ;tomMatch266NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) {{tomMatch266NameNumber_freshVar_8= true ;tomMatch266NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}} else {if ( ((( tom.engine.adt.tomterm.types.TomTerm )patt) instanceof tom.engine.adt.tomterm.types.tomterm.XMLAppl) ) {{tomMatch266NameNumber_freshVar_8= true ;tomMatch266NameNumber_freshVar_1= (( tom.engine.adt.tomterm.types.TomTerm )patt).getNameList() ;}}}}if ((tomMatch266NameNumber_freshVar_8 ==  true )) {if ( ((tomMatch266NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch266NameNumber_freshVar_1 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch266NameNumber_freshVar_1.isEmptyconcTomName() )) { tom.engine.adt.tomname.types.TomName  tomMatch266NameNumber_freshVar_7= tomMatch266NameNumber_freshVar_1.getHeadconcTomName() ;if ( (tomMatch266NameNumber_freshVar_7 instanceof tom.engine.adt.tomname.types.tomname.Name) ) {


        
             TomSymbol symbol = null;
             symbol = getSymbolFromName( tomMatch266NameNumber_freshVar_7.getString() );
             // System.out.println("name = " + `name);
             if( symbol != null ) {
               return TomBase.getSymbolCodomain(symbol);
             }
           }}}}}}}

        }}}}

    }// for    
    return null;
  }

  /*
   * perform type inference of subterms (subtermList)
   * under a given operator (symbol)
   */
  private SlotList typeVariableList(TomSymbol symbol, SlotList subtermList) {
    if(symbol == null) {
      throw new TomRuntimeException("typeVariableList: null symbol");
    }

    if(subtermList.isEmptyconcSlot()) {
      return  tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() ;
    }

    //System.out.println("symbol = " + symbol.getastname());
    {{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.emptySymbol) ) {if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch267NameNumber_freshVar_8= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot() ;if ( (tomMatch267NameNumber_freshVar_8 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) {

        /*
         * if the top symbol is unknown, the subterms
         * are typed in an empty context
         */
        SlotList sl = typeVariableList((( tom.engine.adt.tomsignature.types.TomSymbol )symbol), (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() );
        return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make( tomMatch267NameNumber_freshVar_8.getSlotName() , (TomTerm)typeVariable( tom.engine.adt.tomtype.types.tomtype.EmptyType.make() , tomMatch267NameNumber_freshVar_8.getAppl() )) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
      }}}}}}}{if ( (symbol instanceof tom.engine.adt.tomsignature.types.TomSymbol) ) {if ( ((( tom.engine.adt.tomsignature.types.TomSymbol )symbol) instanceof tom.engine.adt.tomsignature.types.tomsymbol.Symbol) ) { tom.engine.adt.tomtype.types.TomType  tomMatch267NameNumber_freshVar_12= (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getTypesToType() ;if ( (tomMatch267NameNumber_freshVar_12 instanceof tom.engine.adt.tomtype.types.tomtype.TypesToType) ) { tom.engine.adt.tomtype.types.TomType  tomMatch267NameNumber_freshVar_15= tomMatch267NameNumber_freshVar_12.getCodomain() ;if ( (tomMatch267NameNumber_freshVar_15 instanceof tom.engine.adt.tomtype.types.tomtype.Type) ) { tom.engine.adt.tomtype.types.TomType  tom_codomain=tomMatch267NameNumber_freshVar_15; tom.engine.adt.tomsignature.types.TomSymbol  tom_symb=(( tom.engine.adt.tomsignature.types.TomSymbol )symbol);if ( (subtermList instanceof tom.engine.adt.tomslot.types.SlotList) ) {if ( (((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || ((( tom.engine.adt.tomslot.types.SlotList )subtermList) instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if (!( (( tom.engine.adt.tomslot.types.SlotList )subtermList).isEmptyconcSlot() )) { tom.engine.adt.tomslot.types.Slot  tomMatch267NameNumber_freshVar_25= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getHeadconcSlot() ;if ( (tomMatch267NameNumber_freshVar_25 instanceof tom.engine.adt.tomslot.types.slot.PairSlotAppl) ) { tom.engine.adt.tomname.types.TomName  tom_slotName= tomMatch267NameNumber_freshVar_25.getSlotName() ; tom.engine.adt.tomterm.types.TomTerm  tom_slotAppl= tomMatch267NameNumber_freshVar_25.getAppl() ; tom.engine.adt.tomslot.types.SlotList  tom_tail= (( tom.engine.adt.tomslot.types.SlotList )subtermList).getTailconcSlot() ;



          //System.out.println("codomain = " + `codomain);
          // process a list of subterms and a list of types
          if(TomBase.isListOperator(tom_symb) || TomBase.isArrayOperator(tom_symb)) {
            /*
             * todo:
             * when the symbol is an associative operator,
             * the signature has the form: list conc( element* )
             * the list of types is reduced to the singleton { element }
             *
             * consider a pattern: conc(e1*,x,e2*,y,e3*)
             *  assign the type "element" to each subterm: x and y
             *  assign the type "list" to each subtermlist: e1*,e2* and e3*
             */

            //System.out.println("listoperator: " + `symb);
            //System.out.println("subtermlist: " + subtermList);
            //System.out.println("slotAppl: " + `slotAppl);

            {{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {

                ConstraintList newconstraints = (ConstraintList)typeVariable(tom_codomain, (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getConstraints() );
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName,  tom.engine.adt.tomterm.types.tomterm.VariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getOption() ,  (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getAstName() ,  tom.engine.adt.tomtype.types.tomtype.TypeWithSymbol.make( tomMatch267NameNumber_freshVar_15.getTomType() ,  tomMatch267NameNumber_freshVar_15.getTlType() ,  (( tom.engine.adt.tomsignature.types.TomSymbol )symbol).getAstName() ) , newconstraints) ) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl) instanceof tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar) ) {


                ConstraintList newconstraints = (ConstraintList)typeVariable(tom_codomain, (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getConstraints() );
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName,  tom.engine.adt.tomterm.types.tomterm.UnamedVariableStar.make( (( tom.engine.adt.tomterm.types.TomTerm )tom_slotAppl).getOption() , tom_codomain, newconstraints) ) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
              }}}{if ( (tom_slotAppl instanceof tom.engine.adt.tomterm.types.TomTerm) ) {


                TomType domaintype =  tomMatch267NameNumber_freshVar_12.getDomain() .getHeadconcTomType();
                SlotList sl = typeVariableList(tom_symb,tom_tail);
                SlotList res =  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName, (TomTerm)typeVariable(domaintype,tom_slotAppl)) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
                //System.out.println("domaintype = " + domaintype);
                //System.out.println("res = " + res);
                return res;

              }}}

          } else {
            SlotList sl = typeVariableList(tom_symb,tom_tail);
            return  tom.engine.adt.tomslot.types.slotlist.ConsconcSlot.make( tom.engine.adt.tomslot.types.slot.PairSlotAppl.make(tom_slotName, (TomTerm)typeVariable(TomBase.getSlotType(tom_symb,tom_slotName),tom_slotAppl)) ,tom_append_list_concSlot(sl, tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot.make() )) ;
          }
        }}}}}}}}}}

    throw new TomRuntimeException("typeVariableList: strange case: '" + symbol + "'");
  }

  public static class replace_replaceInstantiatedVariable extends tom.library.sl.BasicStrategy {private  tom.engine.adt.tomterm.types.TomList  instantiatedVariable;public replace_replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  instantiatedVariable) {super(( new tom.library.sl.Fail() ));this.instantiatedVariable=instantiatedVariable;}public  tom.engine.adt.tomterm.types.TomList  getinstantiatedVariable() {return instantiatedVariable;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomterm.types.TomTerm  visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomterm.types.TomTerm) ) { tom.engine.adt.tomterm.types.TomTerm  tom_subject=(( tom.engine.adt.tomterm.types.TomTerm )tom__arg);{{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.RecordAppl) ) { tom.engine.adt.tomname.types.TomNameList  tomMatch270NameNumber_freshVar_2= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getNameList() ; tom.engine.adt.tomslot.types.SlotList  tomMatch270NameNumber_freshVar_3= (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getSlots() ;if ( ((tomMatch270NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.ConsconcTomName) || (tomMatch270NameNumber_freshVar_2 instanceof tom.engine.adt.tomname.types.tomnamelist.EmptyconcTomName)) ) {if (!( tomMatch270NameNumber_freshVar_2.isEmptyconcTomName() )) {if (  tomMatch270NameNumber_freshVar_2.getTailconcTomName() .isEmptyconcTomName() ) {if ( ((tomMatch270NameNumber_freshVar_3 instanceof tom.engine.adt.tomslot.types.slotlist.ConsconcSlot) || (tomMatch270NameNumber_freshVar_3 instanceof tom.engine.adt.tomslot.types.slotlist.EmptyconcSlot)) ) {if ( tomMatch270NameNumber_freshVar_3.isEmptyconcSlot() ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch270NameNumber_end_11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch270NameNumber_end_11.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch270NameNumber_freshVar_15= tomMatch270NameNumber_end_11.getHeadconcTomTerm() ;boolean tomMatch270NameNumber_freshVar_17= false ; tom.engine.adt.tomname.types.TomName  tomMatch270NameNumber_freshVar_14= null ;if ( (tomMatch270NameNumber_freshVar_15 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch270NameNumber_freshVar_17= true ;tomMatch270NameNumber_freshVar_14= tomMatch270NameNumber_freshVar_15.getAstName() ;}} else {if ( (tomMatch270NameNumber_freshVar_15 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch270NameNumber_freshVar_17= true ;tomMatch270NameNumber_freshVar_14= tomMatch270NameNumber_freshVar_15.getAstName() ;}}}if ((tomMatch270NameNumber_freshVar_17 ==  true )) {if ( (tomMatch270NameNumber_freshVar_14== tomMatch270NameNumber_freshVar_2.getHeadconcTomName() ) ) {




            return  tomMatch270NameNumber_end_11.getHeadconcTomTerm() ;
          }}}if ( tomMatch270NameNumber_end_11.isEmptyconcTomTerm() ) {tomMatch270NameNumber_end_11=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch270NameNumber_end_11= tomMatch270NameNumber_end_11.getTailconcTomTerm() ;}}} while(!( (tomMatch270NameNumber_end_11==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch270NameNumber_end_25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch270NameNumber_end_25.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch270NameNumber_freshVar_29= tomMatch270NameNumber_end_25.getHeadconcTomTerm() ;boolean tomMatch270NameNumber_freshVar_31= false ; tom.engine.adt.tomname.types.TomName  tomMatch270NameNumber_freshVar_28= null ;if ( (tomMatch270NameNumber_freshVar_29 instanceof tom.engine.adt.tomterm.types.tomterm.Variable) ) {{tomMatch270NameNumber_freshVar_31= true ;tomMatch270NameNumber_freshVar_28= tomMatch270NameNumber_freshVar_29.getAstName() ;}} else {if ( (tomMatch270NameNumber_freshVar_29 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {{tomMatch270NameNumber_freshVar_31= true ;tomMatch270NameNumber_freshVar_28= tomMatch270NameNumber_freshVar_29.getAstName() ;}}}if ((tomMatch270NameNumber_freshVar_31 ==  true )) {if ( (tomMatch270NameNumber_freshVar_28== (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ) ) {

            return  tomMatch270NameNumber_end_25.getHeadconcTomTerm() ;
          }}}if ( tomMatch270NameNumber_end_25.isEmptyconcTomTerm() ) {tomMatch270NameNumber_end_25=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch270NameNumber_end_25= tomMatch270NameNumber_end_25.getTailconcTomTerm() ;}}} while(!( (tomMatch270NameNumber_end_25==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}{if ( (tom_subject instanceof tom.engine.adt.tomterm.types.TomTerm) ) {if ( ((( tom.engine.adt.tomterm.types.TomTerm )tom_subject) instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( (instantiatedVariable instanceof tom.engine.adt.tomterm.types.TomList) ) {if ( (((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.ConsconcTomTerm) || ((( tom.engine.adt.tomterm.types.TomList )instantiatedVariable) instanceof tom.engine.adt.tomterm.types.tomlist.EmptyconcTomTerm)) ) { tom.engine.adt.tomterm.types.TomList  tomMatch270NameNumber_end_39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);do {{if (!( tomMatch270NameNumber_end_39.isEmptyconcTomTerm() )) { tom.engine.adt.tomterm.types.TomTerm  tomMatch270NameNumber_freshVar_43= tomMatch270NameNumber_end_39.getHeadconcTomTerm() ;if ( (tomMatch270NameNumber_freshVar_43 instanceof tom.engine.adt.tomterm.types.tomterm.VariableStar) ) {if ( ( tomMatch270NameNumber_freshVar_43.getAstName() == (( tom.engine.adt.tomterm.types.TomTerm )tom_subject).getAstName() ) ) {

            return  tomMatch270NameNumber_end_39.getHeadconcTomTerm() ;
          }}}if ( tomMatch270NameNumber_end_39.isEmptyconcTomTerm() ) {tomMatch270NameNumber_end_39=(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable);} else {tomMatch270NameNumber_end_39= tomMatch270NameNumber_end_39.getTailconcTomTerm() ;}}} while(!( (tomMatch270NameNumber_end_39==(( tom.engine.adt.tomterm.types.TomList )instantiatedVariable)) ));}}}}}}

      }}}return _visit_TomTerm(tom__arg,introspector); }public  tom.engine.adt.tomterm.types.TomTerm  _visit_TomTerm( tom.engine.adt.tomterm.types.TomTerm  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomterm.types.TomTerm )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomterm.types.TomTerm) ) {return ((T)visit_TomTerm((( tom.engine.adt.tomterm.types.TomTerm )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_replace_replaceInstantiatedVariable( tom.engine.adt.tomterm.types.TomList  t0) { return new replace_replaceInstantiatedVariable(t0);}



  protected tom.library.sl.Visitable replaceInstantiatedVariable(TomList instantiatedVariable, tom.library.sl.Visitable subject) {
    try {
      //System.out.println("varlist = " + instantiatedVariable);
      //System.out.println("subject = " + subject);
      return tom_make_TopDownStopOnSuccess(tom_make_replace_replaceInstantiatedVariable(instantiatedVariable)).visitLight(subject);
    } catch(tom.library.sl.VisitFailure e) {
      throw new TomRuntimeException("replaceInstantiatedVariable: failure on " + instantiatedVariable);
    }
  }

  private TomType getType(String tomName) {
    TomType tomType = getSymbolTable().getType(tomName);
    return tomType;
  }

  /**
   * Collect the constraints (match and numeric)
   */
  public static class CollectMatchAndNumericConstraints extends tom.library.sl.BasicStrategy {private  java.util.Collection  constrList;public CollectMatchAndNumericConstraints( java.util.Collection  constrList) {super(( new tom.library.sl.Identity() ));this.constrList=constrList;}public  java.util.Collection  getconstrList() {return constrList;}public tom.library.sl.Visitable[] getChildren() {tom.library.sl.Visitable[] stratChilds = new tom.library.sl.Visitable[getChildCount()];stratChilds[0] = super.getChildAt(0);return stratChilds;}public tom.library.sl.Visitable setChildren(tom.library.sl.Visitable[] children) {super.setChildAt(0, children[0]);return this;}public int getChildCount() {return 1;}public tom.library.sl.Visitable getChildAt(int index) {switch (index) {case 0: return super.getChildAt(0);default: throw new IndexOutOfBoundsException();}}public tom.library.sl.Visitable setChildAt(int index, tom.library.sl.Visitable child) {switch (index) {case 0: return super.setChildAt(0, child);default: throw new IndexOutOfBoundsException();}}public  tom.engine.adt.tomconstraint.types.Constraint  visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  tom__arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {{{if ( (tom__arg instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {boolean tomMatch271NameNumber_freshVar_2= false ;if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.MatchConstraint) ) {tomMatch271NameNumber_freshVar_2= true ;} else {if ( ((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg) instanceof tom.engine.adt.tomconstraint.types.constraint.NumericConstraint) ) {tomMatch271NameNumber_freshVar_2= true ;}}if ((tomMatch271NameNumber_freshVar_2 ==  true )) {

        
        constrList.add((( tom.engine.adt.tomconstraint.types.Constraint )tom__arg));
        throw new VisitFailure();// to stop the top-down
      }}}}return _visit_Constraint(tom__arg,introspector); }public  tom.engine.adt.tomconstraint.types.Constraint  _visit_Constraint( tom.engine.adt.tomconstraint.types.Constraint  arg, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if (!((environment ==  null ))) {return (( tom.engine.adt.tomconstraint.types.Constraint )any.visit(environment,introspector));} else {return any.visitLight(arg,introspector);} }public <T> T visitLight(T v, tom.library.sl.Introspector introspector) throws tom.library.sl.VisitFailure {if ( (v instanceof tom.engine.adt.tomconstraint.types.Constraint) ) {return ((T)visit_Constraint((( tom.engine.adt.tomconstraint.types.Constraint )v),introspector));}if (!((environment ==  null ))) {return ((T)any.visit(environment,introspector));} else {return any.visitLight(v,introspector);} }}private static  tom.library.sl.Strategy  tom_make_CollectMatchAndNumericConstraints( java.util.Collection  t0) { return new CollectMatchAndNumericConstraints(t0);}


}

